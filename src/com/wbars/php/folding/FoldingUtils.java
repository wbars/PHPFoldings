package com.wbars.php.folding;

import com.intellij.psi.PsiElement;

public class FoldingUtils {
  public static int getEndOffset(PsiElement element) {
    return element.getTextRange().getEndOffset();
  }
}
