package com.wbars.php.folding;

import com.intellij.lang.ASTNode;
import com.intellij.lang.folding.FoldingDescriptor;
import com.intellij.lang.folding.NamedFoldingDescriptor;
import com.intellij.openapi.editor.FoldingGroup;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;

import java.util.List;

public class FoldingDescriptorBuilder {
  private final FoldingGroup myGroup;
  private final ASTNode myNode;
  private final List<FoldingDescriptor> myDescriptors;
  private int myFromOffset = -1;
  private int myToOffset = -1;

  public FoldingDescriptorBuilder(PsiElement element, String name, List<FoldingDescriptor> descriptors) {
    myGroup = FoldingGroup.newGroup(name);
    myNode = element.getNode();
    myDescriptors = descriptors;
  }

  public void empty() {
    text("");
  }

  public void text(PsiElement element, String text) {
    fromStart(element).toEnd(element).text(text);
  }

  public void text(String text) {
    final TextRange range = new TextRange(myFromOffset, myToOffset);
    if (!range.isEmpty()) {
      myDescriptors.add(new NamedFoldingDescriptor(myNode, range, myGroup, text));
    }
    //todo replace with inner builder, now to expensive for just two variables
    myFromOffset = -1;
    myToOffset = -1;
  }

  public void text(PsiElement element) {
    text(element.getText());
  }

  public void text(ASTNode node) {
    text(node.getText());
  }

  public FoldingDescriptorBuilder fromStart(PsiElement element) {
    assertCanBeStarted();
    myFromOffset = element.getTextOffset();
    return this;
  }

  public FoldingDescriptorBuilder fromStart(ASTNode node) {
    assertCanBeStarted();
    myFromOffset = node.getStartOffset();
    return this;
  }

  public FoldingDescriptorBuilder fromEnd(PsiElement element) {
    assertCanBeStarted();
    myFromOffset = element.getTextRange().getEndOffset();
    return this;
  }

  public FoldingDescriptorBuilder toStart(PsiElement element) {
    assertCanBeEnded();
    myToOffset = element.getTextOffset();
    return this;
  }

  public FoldingDescriptorBuilder toStart(ASTNode node) {
    assertCanBeEnded();
    myToOffset = node.getTextRange().getStartOffset();
    return this;
  }

  public FoldingDescriptorBuilder toEnd(PsiElement element) {
    assertCanBeEnded();
    myToOffset = element.getTextRange().getEndOffset();
    return this;
  }

  public FoldingDescriptorBuilder toEnd(ASTNode node) {
    assertCanBeEnded();
    myToOffset = node.getTextRange().getEndOffset();
    return this;
  }

  private void assertCanBeStarted() {
    assert myFromOffset == -1;
    assert myToOffset == -1;
  }

  private void assertCanBeEnded() {
    assert myFromOffset >= 0;
    assert myToOffset == -1;
  }
}
