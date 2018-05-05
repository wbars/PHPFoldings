package com.wbars.php.folding.functionCallProviders;

import com.intellij.lang.folding.FoldingDescriptor;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.jetbrains.php.lang.psi.elements.FunctionReference;
import com.jetbrains.php.lang.psi.elements.PhpTypedElement;
import com.jetbrains.php.lang.psi.resolve.types.PhpType;
import com.wbars.php.folding.FoldingDescriptorBuilder;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.regex.Pattern;

public class ArraySliceCallFoldingProvider extends FunctionCallFoldingProvider {

  private static final Pattern NEGATIVE_NUMBER_PATTERN = Pattern.compile("^-\\d+$");

  @Override
  public String getCheckBoxName() {
    return "Replace 'array_slice' call with slice notation";
  }

  @Override
  public String getName() {
    return "function_array_slice";
  }

  @Override
  protected boolean isAvailable(FunctionReference functionCall) {
    return StringUtil.equals(functionCall.getName(), "array_slice");
  }

  @Override
  public void addDescriptors(FunctionReference functionCall, List<FoldingDescriptor> descriptors) {
    final PsiElement[] parameters = functionCall.getParameters();
    if (parameters.length == 2 || parameters.length == 3) {
      final PsiElement array = parameters[0];
      final PsiElement offset = parameters[1];
      final PsiElement length = parameters.length == 3 ? parameters[2] : null;
      if (isZero(offset) || length == null || isNegativeInteger(length)) {
        final FoldingDescriptorBuilder fold = new FoldingDescriptorBuilder(functionCall, "array_slice", descriptors);
        fold.fromStart(functionCall).toStart(array).empty();
        fold.fromEnd(array).toStart(offset).text("[");
        if (length != null) {
          fold.fromEnd(offset).toStart(length).text(":");
          fold.fromEnd(length).toEnd(functionCall).text("]");
        } else {
          fold.fromEnd(offset).toEnd(functionCall).text(":]");
        }
      }
    }
  }

  private static boolean isZero(@Nullable PsiElement offset) {
    return isInteger(offset) && StringUtil.equals(offset.getText(), "0");
  }

  private static boolean isNegativeInteger(@Nullable PsiElement length) {
    return isInteger(length) && NEGATIVE_NUMBER_PATTERN.matcher(length.getText()).matches();
  }

  @Contract("null -> false")
  private static boolean isInteger(@Nullable PsiElement element) {
    return element instanceof PhpTypedElement && PhpType.INT.equals((((PhpTypedElement)element).getType()));
  }
}
