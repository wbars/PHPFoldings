package com.wbars.php.folding.functionCallProviders;

import com.intellij.lang.folding.FoldingDescriptor;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.jetbrains.php.lang.psi.elements.FunctionReference;
import com.wbars.php.folding.FoldingDescriptorBuilder;

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
    final PsiElement[] parameters = functionCall.getParameters();
    if (parameters.length <= 1) return;
    final FoldingDescriptorBuilder fold = new FoldingDescriptorBuilder(functionCall, "function_call_array_merge", descriptors);
    fold.fromStart(functionCall).toStart(parameters[0]).empty();
    for (int i = 0, length = parameters.length; i < length - 1; i++) {
      fold.fromEnd(parameters[i]).toStart(parameters[i + 1]).text(" :: ");
    }
    fold.fromEnd(parameters[parameters.length - 1]).toEnd(functionCall).empty();
  }
}
