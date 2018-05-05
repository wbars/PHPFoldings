package com.wbars.php.folding.functionCallProviders;

import com.intellij.lang.folding.FoldingDescriptor;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import com.jetbrains.php.lang.PhpLangUtil;
import com.jetbrains.php.lang.psi.elements.*;
import com.jetbrains.php.lang.psi.resolve.types.PhpType;
import com.wbars.php.folding.FoldingDescriptorBuilder;

import java.util.List;

import static com.jetbrains.php.lang.lexer.PhpTokenTypes.*;
import static com.jetbrains.php.lang.psi.PhpPsiUtil.isOfType;

public class StrPosCallFoldingProvider extends FunctionCallFoldingProvider {
  private static final TokenSet EQUALITY_SET = TokenSet.create(opEQUAL, opNOT_EQUAL, opIDENTICAL, opNOT_IDENTICAL);
  private static final String DOES_NOT_CONTAIN = " does not contain ";
  private static final String CONTAINS = " contains ";

  @Override
  public String getCheckBoxName() {
    return "Replace 'strpos' based conditions with 'contains'/does't contain'/'starts with'";
  }

  @Override
  public String getName() {
    return "function_strpos";
  }

  @Override
  public boolean isAvailable(FunctionReference functionCall) {
    final PsiElement parent = functionCall.getParent();
    final String functionName = functionCall.getName();
    if (parent != null && (StringUtil.equals(functionName, "strpos") || StringUtil.equals(functionName, "stripos")) && functionCall.getParameters().length == 2) {
      if (parent instanceof UnaryExpression) {
        return isOfType(((UnaryExpression)parent).getOperation(), opNOT);
      }
      if (parent instanceof BinaryExpression) {
        return EQUALITY_SET.contains(((BinaryExpression)parent).getOperationType()) &&
               (isNumberOrBoolean(((BinaryExpression)parent).getLeftOperand()) ||
                isNumberOrBoolean(((BinaryExpression)parent).getRightOperand()));
      }
      return parent instanceof If;
    }
    return false;
  }

  private boolean isNumberOrBoolean(PsiElement element) {
    return (element instanceof PhpTypedElement && PhpType.INT.equals((((PhpTypedElement)element).getType()))) ||
           element instanceof ConstantReference &&
           (PhpLangUtil.isTrue((ConstantReference)element) || PhpLangUtil.isFalse((ConstantReference)element));
  }

  @Override
  public void addDescriptors(FunctionReference functionCall, List<FoldingDescriptor> descriptors) {
    final PsiElement parent = functionCall.getParent();
    if (parent instanceof UnaryExpression) {
      addDescriptors(functionCall, ((UnaryExpression)parent), descriptors);
    }
    else if (parent instanceof BinaryExpression) {
      addDescriptors(functionCall, ((BinaryExpression)parent), descriptors);
    }
    else if (parent instanceof If) {
      addDescriptorsUnaryTrue(functionCall, ((If)parent), descriptors);
    }
  }

  private void addDescriptorsUnaryTrue(FunctionReference call, If ifStatement, List<FoldingDescriptor> descriptors) {
    final PhpPsiElement condition = ifStatement.getCondition();
    if (condition != null) {
      addDescriptors(call, condition, descriptors, CONTAINS);
    }
  }

  private void addDescriptors(FunctionReference call, UnaryExpression unaryExpression, List<FoldingDescriptor> descriptors) {
    addDescriptors(call, unaryExpression, descriptors, DOES_NOT_CONTAIN);
  }

  private void addDescriptors(FunctionReference functionCall, BinaryExpression expression, List<FoldingDescriptor> myDescriptors) {
    final PsiElement left = expression.getLeftOperand();
    final PsiElement right = expression.getRightOperand();
    if (left != null && right != null) {
      final String argument = isNumberOrBoolean(left) ? left.getText() : isNumberOrBoolean(right) ? right.getText() : null;
      final String notPrefix = getPlaceholder(argument, expression.getOperationType());
      if (notPrefix != null) {
        addDescriptors(functionCall, expression, myDescriptors, notPrefix);
      }
    }
  }

  private void addDescriptors(FunctionReference functionCall, PsiElement parent, List<FoldingDescriptor> myDescriptors, String placeholder) {
    final FoldingDescriptorBuilder fold = new FoldingDescriptorBuilder(parent, "startsWith", myDescriptors);
    final PsiElement[] parameters = functionCall.getParameters();
    fold.fromStart(parent).toStart(parameters[0]).empty();
    fold.fromEnd(parameters[0]).toStart(parameters[1]).text(placeholder);
    fold.fromEnd(parameters[1]).toEnd(parent).empty();
  }

  private static String getPlaceholder(String argument, IElementType type) {
    final boolean falseLiteral = StringUtil.equals(argument, "false");
    if (type == opIDENTICAL) return StringUtil.equals(argument, "0") ? " starts with " : falseLiteral ? DOES_NOT_CONTAIN : null;
    if (type == opEQUAL && StringUtil.equals(argument, "true") || type == opNOT_IDENTICAL && falseLiteral) return CONTAINS;
    return null;
  }
}
