package com.wbars.php.folding;

import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.jetbrains.php.lang.psi.elements.PhpTypedElement;
import com.jetbrains.php.lang.psi.resolve.types.PhpType;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

import java.util.regex.Pattern;

public class NumberUtils {
  private static final Pattern NEGATIVE_NUMBER_PATTERN = Pattern.compile("^-\\d+$");

  public static boolean isZero(@Nullable PsiElement offset) {
    return isInteger(offset) && StringUtil.equals(offset.getText(), "0");
  }

  @Contract("null -> false")
  private static boolean isInteger(@Nullable PsiElement element) {
    return element instanceof PhpTypedElement && PhpType.INT.equals((((PhpTypedElement)element).getType()));
  }

  public static boolean isNegativeInteger(@Nullable PsiElement length) {
    return isInteger(length) && NEGATIVE_NUMBER_PATTERN.matcher(length.getText()).matches();
  }
}
