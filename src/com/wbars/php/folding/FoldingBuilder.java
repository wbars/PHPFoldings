package com.wbars.php.folding;

import com.intellij.lang.ASTNode;
import com.intellij.lang.folding.FoldingBuilderEx;
import com.intellij.lang.folding.FoldingDescriptor;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.project.DumbAware;
import com.intellij.psi.PsiElement;
import com.intellij.psi.SyntaxTraverser;
import com.jetbrains.php.lang.psi.PhpFile;
import com.jetbrains.php.lang.psi.elements.PhpPsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class FoldingBuilder extends FoldingBuilderEx implements DumbAware {
  @NotNull
  @Override
  public FoldingDescriptor[] buildFoldRegions(@NotNull PsiElement element, @NotNull Document document, boolean quick) {
    List<FoldingDescriptor> myDescriptors = new ArrayList<>();
    if (element instanceof PhpFile) {
      final FoldingVisitor visitor = new FoldingVisitor(myDescriptors, quick);
      SyntaxTraverser.psiTraverser(element).filter(PhpPsiElement.class).filter(PsiElement::isValid).forEach(visitor::apply);
    }
    return myDescriptors.toArray(new FoldingDescriptor[0]);
  }

  @Nullable
  @Override
  public String getPlaceholderText(@NotNull ASTNode node) {
    return null;
  }

  @Override
  public boolean isCollapsedByDefault(@NotNull ASTNode node) {
    return true;
  }
}
