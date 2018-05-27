package com.wbars.php.folding.functionCallProviders.typeVal;

import com.intellij.lang.folding.FoldingDescriptor;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.util.ArrayUtil;
import com.jetbrains.php.lang.psi.elements.FunctionReference;
import com.wbars.php.folding.FoldingDescriptorBuilder;
import com.wbars.php.folding.functionCallProviders.FunctionCallFoldingProvider;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public abstract class TypeValCallAbstractFoldingProvider extends FunctionCallFoldingProvider {
  protected abstract String getType();

  @NotNull
  protected String getFunctionName() {
    return getType() + "val";
  }

  @Override
  public String getCheckBoxName() {
    return String.format("Replace %s to %s cast", getFunctionName(), getType());
  }

  @Override
  public String getName() {
    return getFunctionName() + "_function_call";
  }

  @Override
  protected boolean isAvailable(FunctionReference functionCall) {
    return StringUtil.equals(functionCall.getName(), getFunctionName());
  }

  @Override
  public void addDescriptors(FunctionReference functionCall, List<FoldingDescriptor> descriptors) {
    final PsiElement parameter = ArrayUtil.getFirstElement(functionCall.getParameters());
    if (parameter != null) {
      final FoldingDescriptorBuilder fold = new FoldingDescriptorBuilder(functionCall, getName(), descriptors);
      fold.fromStart(functionCall).toStart(parameter).text("(" + getType() + ")");
      fold.fromEnd(parameter).toEnd(functionCall).empty();
    }
  }
}
