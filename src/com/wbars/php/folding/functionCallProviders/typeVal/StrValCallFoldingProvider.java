package com.wbars.php.folding.functionCallProviders.typeVal;

import org.jetbrains.annotations.NotNull;

public class StrValCallFoldingProvider extends TypeValCallAbstractFoldingProvider {
  @Override
  protected String getType() {
    return "string";
  }

  @NotNull
  @Override
  protected String getFunctionName() {
    return "strval";
  }
}
