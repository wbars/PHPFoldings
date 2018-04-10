package com.wbars.php.folding.functionCallProviders;

import com.intellij.lang.ASTNode;
import com.intellij.lang.folding.FoldingDescriptor;
import com.intellij.lang.folding.NamedFoldingDescriptor;
import com.intellij.openapi.editor.FoldingGroup;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.jetbrains.php.lang.psi.elements.FunctionReference;

import java.util.List;

import static com.wbars.php.folding.FoldingUtils.getEndOffset;

public class InArrayCallFoldingProvider extends FunctionCallFoldingProvider{
  @Override
  public String getCheckBoxName() {
    return "Replace 'in_array' function calls with 'in'";
  }

  @Override
  public String getName() {
    return "function_in_array";
  }
  @Override
  public boolean isAvailable(FunctionReference functionCall) {
    return StringUtil.equals(functionCall.getName(), "in_array");
  }

  @Override
  public void addDescriptors(FunctionReference functionCall, List<FoldingDescriptor> descriptors) {
    final PsiElement[] parameters = functionCall.getParameters();
    if (parameters.length <= 1 || parameters.length >= 3) return;
    final FoldingGroup group = FoldingGroup.newGroup("function_call_in_array");
    final ASTNode node = functionCall.getNode();
    descriptors.add(new NamedFoldingDescriptor(node, new TextRange(functionCall.getTextOffset(), parameters[0].getTextOffset()), group, ""));
    descriptors.add(new NamedFoldingDescriptor(node, new TextRange(getEndOffset(parameters[0]), parameters[1].getTextOffset()), group, " in "));
    descriptors.add(new NamedFoldingDescriptor(node, new TextRange(getEndOffset(parameters[1]), getEndOffset(functionCall)), group, ""));
  }
}
