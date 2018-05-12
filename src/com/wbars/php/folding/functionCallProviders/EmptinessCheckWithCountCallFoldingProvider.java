package com.wbars.php.folding.functionCallProviders;

import com.intellij.lang.ASTNode;
import com.intellij.lang.folding.FoldingDescriptor;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import com.intellij.util.ObjectUtils;
import com.jetbrains.php.lang.lexer.PhpTokenTypes;
import com.jetbrains.php.lang.psi.elements.BinaryExpression;
import com.jetbrains.php.lang.psi.elements.FunctionReference;
import com.wbars.php.folding.FoldingDescriptorBuilder;
import com.wbars.php.folding.NumberUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static com.jetbrains.php.lang.psi.PhpPsiUtil.isOfType;

public class EmptinessCheckWithCountCallFoldingProvider extends FunctionCallFoldingProvider {

  private static final TokenSet EQUALS_SET = TokenSet.create(PhpTokenTypes.opIDENTICAL, PhpTokenTypes.opEQUAL);
  private static final TokenSet NOT_EQUALS_SET = TokenSet.create(PhpTokenTypes.opNOT_IDENTICAL, PhpTokenTypes.opNOT_EQUAL);

  @Override
  public String getCheckBoxName() {
    return "Emptiness check with count/sizeof call";
  }

  @Override
  public String getName() {
    return "function_call_sizeof_condition";
  }

  @Override
  protected boolean isAvailable(FunctionReference functionCall) {
    return (StringUtil.equals(functionCall.getName(), "count") || StringUtil.equals(functionCall.getName(), "sizeof")) &&
           functionCall.getParameters().length == 1;
  }

  @Override
  public void addDescriptors(FunctionReference functionCall, List<FoldingDescriptor> descriptors) {
    final BinaryExpression relation = ObjectUtils.tryCast(functionCall.getParent(), BinaryExpression.class);
    if (relation != null) {
      if (isOfType(relation.getOperation(), PhpTokenTypes.tsCOMPARE_OPS)) {
        final String foldName = getFoldName(relation, functionCall);
        final ASTNode nameNode = functionCall.getNameNode();
        if (foldName != null && nameNode != null) {
          final FoldingDescriptorBuilder fold = new FoldingDescriptorBuilder(functionCall, "count_empty", descriptors);
          fold.fromStart(relation).toStart(functionCall).empty();
          fold.text(nameNode.getPsi(), foldName);
          fold.fromEnd(functionCall).toEnd(relation).empty();
        }
      }
    }
  }

  @Nullable
  private static String getFoldName(@NotNull BinaryExpression relation, @NotNull FunctionReference functionCall) {
    final PsiElement leftOperand = relation.getLeftOperand();
    final boolean functionCallIsLeftOperand = leftOperand == functionCall;
    if (NumberUtils.isZero(functionCallIsLeftOperand ? relation.getRightOperand() : leftOperand)) {
      final IElementType operationType = relation.getOperationType();
      if (EQUALS_SET.contains(operationType)) {
        return "empty";
      }
      else if (NOT_EQUALS_SET.contains(operationType) ||
               (functionCallIsLeftOperand ? operationType != PhpTokenTypes.opLESS : operationType != PhpTokenTypes.opGREATER)) {
        return "notEmpty";
      }
    }
    return null;
  }
}
