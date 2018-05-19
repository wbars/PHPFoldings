package com.wbars.php.folding.functionCallProviders;

import com.intellij.lang.folding.FoldingDescriptor;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.jetbrains.php.lang.psi.elements.FunctionReference;
import com.wbars.php.folding.FoldingDescriptorBuilder;

import java.util.List;

public class ArrayPushCallFoldingProvider extends FunctionCallFoldingProvider {
  @Override
  public String getCheckBoxName() {
    return "Replace 'array_push' call with assignment";
  }

  @Override
  public String getName() {
    return "array_push_call";
  }

  @Override
  protected boolean isAvailable(FunctionReference functionCall) {
    return StringUtil.equals(functionCall.getName(), "array_push");
  }

  @Override
  public void addDescriptors(FunctionReference functionCall, List<FoldingDescriptor> descriptors) {
    final PsiElement[] parameters = functionCall.getParameters();
    if (parameters.length == 2) {
      final FoldingDescriptorBuilder fold = new FoldingDescriptorBuilder(functionCall, getName(), descriptors);
      fold.fromStart(functionCall).toStart(parameters[0]).empty();
      fold.fromEnd(parameters[0]).toStart(parameters[1]).text("[] = ");
      fold.fromEnd(parameters[1]).toEnd(functionCall).empty();
    }
  }
}
