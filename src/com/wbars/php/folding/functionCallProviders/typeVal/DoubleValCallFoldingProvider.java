package com.wbars.php.folding.functionCallProviders.typeVal;

import org.jetbrains.annotations.NotNull;

public class DoubleValCallFoldingProvider extends TypeValCallAbstractFoldingProvider {
  @NotNull
  @Override
  protected String getFunctionName() {
    return "doubleval";
  }

  @Override
  protected String getType() {
    return "float";
  }
}
