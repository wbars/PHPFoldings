package com.wbars.php.folding.functionCallProviders;

import com.intellij.lang.ASTNode;
import com.intellij.lang.folding.FoldingDescriptor;
import com.intellij.lang.folding.NamedFoldingDescriptor;
import com.intellij.openapi.editor.FoldingGroup;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.jetbrains.php.lang.psi.PhpPsiUtil;
import com.jetbrains.php.lang.psi.elements.FunctionReference;
import com.jetbrains.php.lang.psi.elements.ParameterList;

import java.util.List;

public class ArrayMergeCallFoldingProvider extends FunctionCallFoldingProvider {
  @Override
  public String getCheckBoxName() {
    return "Replace array_merge' function calls with '::'";
  }

  @Override
  public String getName() {
    return "function_array_merge";
  }

  @Override
  public boolean isAvailable(FunctionReference functionCall) {
    return StringUtil.equals(functionCall.getName(), "array_merge");
  }

  @Override
  public void addDescriptors(FunctionReference functionCall, List<FoldingDescriptor> descriptors) {
    final FoldingGroup group = FoldingGroup.newGroup("function_call_array_merge");
    final PsiElement[] parameters = functionCall.getParameters();
    if (parameters.length <= 1) return;
    final ASTNode node = functionCall.getNode();
    final ASTNode nameNode = functionCall.getNameNode();
    if (nameNode == null) return;
    final TextRange range = nameNode.getTextRange();
    final ParameterList list = functionCall.getParameterList();
    if (list == null) return;
    descriptors.add(new NamedFoldingDescriptor(node, range, group, ""));
    descriptors.add(new NamedFoldingDescriptor(node, range, group, ""));
    final PsiElement openBrace = PhpPsiUtil.getPrevSiblingIgnoreWhitespace(list, true);
    final PsiElement closeBrace = PhpPsiUtil.getNextSiblingIgnoreWhitespace(list, true);
    if (openBrace == null || closeBrace == null) return;
    descriptors.add(new NamedFoldingDescriptor(node, new TextRange(openBrace.getTextOffset(), parameters[0].getTextOffset()), group, ""));
    for (int i = 0, length = parameters.length; i < length - 1; i++) {
      final PsiElement currentParameter = parameters[i];
      final PsiElement nextParameter = parameters[i + 1];
      descriptors.add(new NamedFoldingDescriptor(node, new TextRange(currentParameter.getTextRange().getEndOffset(), nextParameter.getTextOffset()), group, " :: "));
    }
    descriptors.add(new NamedFoldingDescriptor(node, new TextRange(parameters[parameters.length - 1].getTextRange().getEndOffset(), closeBrace.getTextRange().getEndOffset()), group, ""));
  }
}
