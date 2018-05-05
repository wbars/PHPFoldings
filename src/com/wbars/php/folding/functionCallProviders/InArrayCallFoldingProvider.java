package com.wbars.php.folding.functionCallProviders;

import com.intellij.lang.folding.FoldingDescriptor;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.jetbrains.php.lang.psi.elements.FunctionReference;
import com.wbars.php.folding.FoldingDescriptorBuilder;

import java.util.List;

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
    final FoldingDescriptorBuilder fold = new FoldingDescriptorBuilder(functionCall, "function_call_in_array", descriptors);
    fold.fromStart(functionCall).toStart(parameters[0]).empty();
    fold.fromEnd(parameters[0]).toStart(parameters[1]).text(" in ");
    fold.fromEnd(parameters[1]).toEnd(functionCall).empty();
  }
}
