package com.wbars.php.folding;

import com.intellij.application.options.editor.CodeFoldingOptionsProvider;
import com.intellij.openapi.options.BeanConfigurable;
import com.wbars.php.folding.functionCallProviders.FunctionCallFoldingProvider;

public class FoldingProvider extends BeanConfigurable<FoldingConfiguration.State> implements CodeFoldingOptionsProvider {
  public FoldingProvider() {
    super(FoldingConfiguration.getInstance().getState());
    FoldingConfiguration settings = FoldingConfiguration.getInstance();
    checkBox("Collapse '$this->' prefix for fields", settings::isCollapseThisPrefixFields, settings::setCollapseThisPrefixFields);
    checkBox("Collapse '$this->' prefix for methods", settings::isCollapseThisPrefixMethods, settings::setCollapseThisPrefixMethods);
    checkBox("Collapse 'self::' prefix for fields", settings::isCollapseSelfPrefixFields, settings::setCollapseSelfPrefixFields);
    checkBox("Collapse 'self::' prefix for methods", settings::isCollapseSelfPrefixMethods, settings::setCollapseSelfPrefixMethods);
    checkBox("Collapse 'self::' prefix for class constants", settings::isCollapseSelfPrefixConstants, settings::setCollapseSelfPrefixConstants);
    checkBox("Replace getters with field names", settings::isCollapseGetter, settings::setCollapseGetter);
    checkBox("Replace setters with field assignment", settings::isCollapseSetter, settings::setCollapseSetter);
    checkBox("Collapse data classes", settings::isCollapseDataClass, settings::setCollapseDataClass);
    checkBox("Collapse lambdas", settings::isCollapseLambda, settings::setCollapseLambda);
    checkBox("Collapse full array creation syntax to short version", settings::isCollapseArrays, settings::setCollapseArrays);
    checkBox("Replace negation of instanceof with 'not instanceof'", settings::isCollapseNotInstanceof, settings::setCollapseNotInstanceof);

    for (FunctionCallFoldingProvider provider : FunctionCallFoldingProvider.getAllProviders()) {
      checkBox(provider.getCheckBoxName(), () -> settings.getFunctionCallProviderValue(provider.getName()),
               value -> settings.setFunctionCallProviderValue(provider.getName(), value));
    }
  }
}
