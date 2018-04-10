package com.wbars.php.folding.functionCallProviders;

import com.intellij.lang.folding.FoldingDescriptor;
import com.intellij.openapi.extensions.ExtensionPointName;
import com.intellij.openapi.extensions.Extensions;
import com.jetbrains.php.lang.psi.elements.FunctionReference;
import com.wbars.php.folding.FoldingConfiguration;

import java.util.List;

public abstract class FunctionCallFoldingProvider {
  private static final ExtensionPointName<FunctionCallFoldingProvider> EP_NAME = ExtensionPointName.create("com.wbars.php.folding.functionCallFoldingProvider");

  public abstract String getCheckBoxName();

  public abstract String getName();

  public static FunctionCallFoldingProvider[] getAllProviders() {
    return Extensions.getExtensions(EP_NAME);
  }

  protected abstract boolean isAvailable(FunctionReference functionCall);
  public abstract void addDescriptors(FunctionReference functionCall, List<FoldingDescriptor> descriptors);

  public final boolean isAvailable(FunctionReference functionCall, FoldingConfiguration configuration) {
    return configuration.getFunctionCallProviderValue(getName()) && isAvailable(functionCall);
  }
}
