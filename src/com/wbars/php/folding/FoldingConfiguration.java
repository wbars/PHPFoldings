package com.wbars.php.folding;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("ALL")
@State(name = "PHPFoldingCustomFoldingSettings", storages = @Storage("editor.codeinsight.xml"))
public class FoldingConfiguration implements PersistentStateComponent<FoldingConfiguration.State> {
  @NotNull private FoldingConfiguration.State myState = new FoldingConfiguration.State();

  public void setCollapseThisPrefixFields(boolean value) {
    myState.THIS_PREFIX_FIELDS = value;
  }

  public boolean isCollapseThisPrefixFields() {
    return myState.THIS_PREFIX_FIELDS;
  }

  public void setCollapseThisPrefixMethods(boolean value) {
    myState.THIS_PREFIX_METHODS = value;
  }

  public boolean isCollapseThisPrefixMethods() {
    return myState.THIS_PREFIX_METHODS;
  }

  public boolean isCollapseGetter() {
    return myState.GETTERS;
  }

  public void setCollapseGetter(boolean value) {
    myState.GETTERS = value;
  }

  public boolean isCollapseSetter() {
    return myState.SETTERS;
  }

  public void setCollapseSetter(boolean value) {
    myState.SETTERS = value;
  }

  public boolean isCollapseDataClass() {
    return myState.DATA_CLASS;
  }

  public void setCollapseDataClass(boolean value) {
    myState.DATA_CLASS = value;
  }

  public boolean isCollapseLambda() {
    return myState.LAMBDA;
  }

  public void setCollapseLambda(boolean value) {
    myState.LAMBDA = value;
  }

  public boolean isCollapseArrays() {
    return myState.ARRAYS;
  }

  public void setCollapseArrays(boolean value) {
    myState.ARRAYS = value;
  }

  public boolean getFunctionCallProviderValue(String name) {
    return myState.getCustomValue(name);
  }

  public void setFunctionCallProviderValue(String name, boolean value) {
    myState.setCustomValue(name, value);
  }

  public boolean isCollapseSelfPrefixFields() {
    return myState.SELF_PREFIX_FIELDS;
  }

  public void setCollapseSelfPrefixFields(boolean value) {
    myState.SELF_PREFIX_FIELDS = value;
  }

  public boolean isCollapseSelfPrefixMethods() {
    return myState.SELF_PREFIX_METHODS;
  }

  public void setCollapseSelfPrefixMethods(boolean value) {
    myState.SELF_PREFIX_METHODS = value;
  }

  public boolean isCollapseSelfPrefixConstants() {
    return myState.SELF_PREFIX_CONSTANTS;
  }

  public void setCollapseSelfPrefixConstants(boolean value) {
    myState.SELF_PREFIX_CONSTANTS = value;
  }

  public static FoldingConfiguration getInstance() {
    return ServiceManager.getService(FoldingConfiguration.class);
  }
  public static class State {
    public boolean THIS_PREFIX_FIELDS = true;
    public boolean THIS_PREFIX_METHODS = true;
    public boolean SELF_PREFIX_FIELDS = true;
    public boolean SELF_PREFIX_METHODS = true;
    public boolean SELF_PREFIX_CONSTANTS = true;
    public boolean GETTERS = true;
    public boolean SETTERS = true;
    public boolean DATA_CLASS = true;
    public boolean LAMBDA = true;
    public boolean ARRAYS = true;
    private final Map<String, Boolean> custom = new HashMap<>();

    public boolean getCustomValue(String name) {
      return custom.getOrDefault(name, true);
    }

    public void setCustomValue(String name, boolean value) {
      custom.put(name, value);
    }
  }
  @Override
  public void loadState(@NotNull State state) {
    myState = state;
  }

  @NotNull
  @Override
  public State getState() {
    return myState;
  }
}
