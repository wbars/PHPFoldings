package com.wbars.php.folding;

import com.intellij.lang.ASTNode;
import com.intellij.lang.folding.FoldingDescriptor;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.jetbrains.php.lang.PhpLangUtil;
import com.jetbrains.php.lang.lexer.PhpTokenTypes;
import com.jetbrains.php.lang.psi.PhpPsiUtil;
import com.jetbrains.php.lang.psi.elements.*;
import com.jetbrains.php.lang.psi.visitors.PhpElementVisitor;
import com.wbars.php.folding.functionCallProviders.FunctionCallFoldingProvider;
import one.util.streamex.StreamEx;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static com.intellij.util.ObjectUtils.tryCast;
import static com.jetbrains.php.lang.psi.PhpPsiUtil.getNextSiblingIgnoreWhitespace;
import static com.jetbrains.php.lang.psi.PhpPsiUtil.isOfType;

public class FoldingVisitor extends PhpElementVisitor {
  private final List<FoldingDescriptor> myDescriptors;
  private final boolean myQuick;
  private final FoldingConfiguration myConfiguration;

  FoldingVisitor(List<FoldingDescriptor> descriptors, boolean quick) {
    myDescriptors = descriptors;
    myQuick = quick;
    myConfiguration = FoldingConfiguration.getInstance();
  }

  @NotNull
  private FoldingDescriptorBuilder fold(PsiElement element, String name) {
    return new FoldingDescriptorBuilder(element, name, myDescriptors);
  }


  @Override
  public void visitPhpFunctionCall(FunctionReference functionCall) {
    super.visitPhpFunctionCall(functionCall);
    for (FunctionCallFoldingProvider provider : FunctionCallFoldingProvider.getAllProviders()) {
      if (provider.isAvailable(functionCall, myConfiguration)) {
        provider.addDescriptors(functionCall, myDescriptors);
      }
    }
  }

  @Override
  public void visitPhpMethodReference(MethodReference methodReference) {
    super.visitPhpMethodReference(methodReference);
    final PhpExpression classReference = methodReference.getClassReference();
    final ASTNode nameNode = methodReference.getNameNode();
    if (nameNode != null) {
      if (myConfiguration.isCollapseThisPrefixMethods() && classReference != null && PhpLangUtil.isThisReference(classReference)) {
        fold(methodReference, "thisCall").fromStart(methodReference).toStart(nameNode).empty();
      }
      else if (!myQuick || ApplicationManager.getApplication().isUnitTestMode()) {
        if (myConfiguration.isCollapseGetter() && isGetter(methodReference)) {
          fold(methodReference, "getter").fromStart(nameNode).toEnd(methodReference).text(getFieldName(nameNode.getText()));
        }
        if (myConfiguration.isCollapseSetter() && isSetter(methodReference)) {
          final PsiElement parameter = methodReference.getParameters()[0];
          final FoldingDescriptorBuilder fold = fold(methodReference, "setter");
          final String name = getFieldSetterName(methodReference.getName());
          fold.fromStart(methodReference.getNameNode()).toStart(parameter).text(name + " = ");
          fold.fromEnd(parameter).toEnd(methodReference).empty();
        }
      }
      else if (myConfiguration.isCollapseGetter() && (!myQuick || ApplicationManager.getApplication().isUnitTestMode()) && isGetter(methodReference)) {
        fold(methodReference, "setter").fromStart(nameNode).toEnd(methodReference).text(getFieldName(nameNode.getText()));
      }
    }
  }

  @Override
  public void visitPhpClass(PhpClass clazz) {
    if (myConfiguration.isCollapseDataClass() && clazz.getSuperFQN() != null) {
      int trivialMethodsCount = 0;
      final Method[] methods = clazz.getOwnMethods();
      List<String> fields = new ArrayList<>();
      if (methods.length == 0) return;
      for (Field field : clazz.getOwnFields()) {
        final String fieldName = field.getName();
        boolean hasGetter = StreamEx.of(methods).anyMatch(m -> isGetter(fieldName, m));
        boolean hasSetter = StreamEx.of(methods).anyMatch(m -> isSetter(fieldName, m));
        if (!hasGetter && !hasSetter) return;
        if (hasGetter) trivialMethodsCount++;
        if (hasSetter) trivialMethodsCount++;
        final String modifier = hasSetter || !field.getModifier().isPrivate() ? "var" : "val";
        fields.add(modifier + " $" + fieldName + ";");
      }
      if (trivialMethodsCount == methods.length) {
        fold(clazz, "pojo").text(clazz, clazz.getName() + "(" + StreamEx.of(fields).joining(" ") + ")");
      }
    }
  }

  private boolean isSetter(String fieldName, Method method) {
    final String name = StringUtil.decapitalize(StringUtil.trimStart(method.getName(), "set"));
    if (!StringUtil.equals(name, fieldName)) return false;
    final Parameter[] parameters = method.getParameters();
    if (parameters.length != 1) return false;
    final AssignmentExpression assignment = tryCast(getSingleBodyStatement(method), AssignmentExpression.class);
    if (assignment == null) return false;
    final FieldReference fieldReference = tryCast(assignment.getVariable(), FieldReference.class);
    if (fieldReference == null ||
        !PhpLangUtil.isThisReference(fieldReference.getClassReference()) ||
        !StringUtil.equals(fieldReference.getName(), fieldName)) {
      return false;
    }
    final Variable value = tryCast(assignment.getValue(), Variable.class);
    return value != null && StringUtil.equals(parameters[0].getName(), value.getName());
  }

  private boolean isGetter(MethodReference methodReference) {
    if (methodReference.getParameters().length != 0) return false;
    final String name = methodReference.getName();
    if (name == null || !StringUtil.startsWith(name, "get")) return false;
    return StreamEx.of(methodReference.resolveGlobal(false)).select(Method.class).anyMatch(method -> isGetter(name, method));
  }

  private boolean isSetter(MethodReference methodReference) {
    if (methodReference.getParameters().length != 1) return false;
    final String name = methodReference.getName();
    if (name == null || !StringUtil.startsWith(name, "set")) return false;
    final String fieldName = getFieldSetterName(name);
    return StreamEx.of(methodReference.resolveGlobal(false)).select(Method.class).anyMatch(method -> isSetter(fieldName, method));
  }

  private PsiElement getSingleBodyStatement(Method method) {
    if (method == null) return null;
    final PsiElement body = PhpPsiUtil.getChildByCondition(method, GroupStatement.INSTANCEOF);
    if (body == null) return null;
    final PsiElement[] children = body.getChildren();
    if (children.length != 1) return null;
    return !(children[0] instanceof PhpReturn) && children[0].getChildren().length == 1 ? children[0].getChildren()[0] : children[0];
  }

  private boolean isGetter(String fieldName, Method method) {
    final Parameter[] parameters = method.getParameters();
    if (parameters.length != 0) return false;
    final PhpReturn phpReturn = tryCast(getSingleBodyStatement(method), PhpReturn.class);
    if (phpReturn == null) return false;
    final FieldReference returnArgument = tryCast(phpReturn.getArgument(), FieldReference.class);
    if (returnArgument == null) return false;
    final PhpExpression reference = returnArgument.getClassReference();
    return reference != null &&
           PhpLangUtil.isThisReference(reference) &&
           StringUtil.equals(returnArgument.getName(), getFieldName(fieldName));
  }

  private String getFieldName(String getterName) {
    return StringUtil.decapitalize(StringUtil.trimStart(getterName, "get"));
  }

  private String getFieldSetterName(String setterName) {
    return StringUtil.decapitalize(StringUtil.trimStart(setterName, "set"));
  }

  @Override
  public void visitPhpFieldReference(FieldReference fieldReference) {
    super.visitPhpFieldReference(fieldReference);
    final PhpExpression classReference = fieldReference.getClassReference();
    if (myConfiguration.isCollapseThisPrefixFields() && classReference != null && PhpLangUtil.isThisReference(classReference)) {
      final ASTNode nameNode = fieldReference.getNameNode();
      if (nameNode != null) {
        fold(fieldReference, "thisField").fromStart(fieldReference).toStart(nameNode).empty();
      }
    }
  }

  @Override
  public void visitPhpFunction(Function function) {
    super.visitPhpFunction(function);
    if (myConfiguration.isCollapseLambda() && function.isClosure()) {
      final FoldingDescriptorBuilder fold = fold(function, "lambda");
      final GroupStatement body = PhpPsiUtil.getChildByCondition(function, GroupStatement.INSTANCEOF);
      final ParameterList list = PhpPsiUtil.getChildByCondition(function, ParameterList.INSTANCEOF);
      if (body != null && list != null) {
        final PsiElement closeBrace = PhpPsiUtil.getNextSiblingIgnoreWhitespace(list, true);
        final PsiElement openBrace = PhpPsiUtil.getPrevSiblingIgnoreWhitespace(list, true);
        if (openBrace != null && closeBrace != null) {
          fold.fromStart(function.getFirstChild()).toStart(openBrace).empty();
          final PsiElement[] statements = body.getStatements();
          if (statements.length == 1 && statements[0] instanceof PhpReturn) {
            final PhpReturn phpReturn = tryCast(statements[0], PhpReturn.class);
            if (phpReturn == null) return;
            final PsiElement argument = phpReturn.getArgument();
            if (argument == null) return;
            fold.fromEnd(closeBrace).toStart(argument).text(" -> ");
            fold.fromEnd(argument).toEnd(body.getLastChild()).empty();
          }
          else {
            fold.fromEnd(closeBrace).toEnd(body.getFirstChild()).text(" -> {");
            fold.text(body.getLastChild(), "}");
          }
        }
      }
    }
  }

  @Override
  public void visitPhpArrayCreationExpression(ArrayCreationExpression expression) {
    if (!expression.isShortSyntax() && myConfiguration.isCollapseArrays()) {
      final FoldingDescriptorBuilder fold = fold(expression, "lambda");
      final PsiElement firstChild = expression.getFirstChild();
      final PsiElement openParen = getNextSiblingIgnoreWhitespace(firstChild, true);
      final PsiElement closeParen = expression.getLastChild();
      if (isOfType(openParen, PhpTokenTypes.chLPAREN) && isOfType(closeParen, PhpTokenTypes.chRPAREN)) {
        fold.fromStart(expression).toEnd(openParen).text("[");
        fold.text(closeParen, "]");
      }
    }
  }
}
