package com.wbars.php.folding;

import com.intellij.testFramework.fixtures.LightPlatformCodeInsightFixtureTestCase;

public class FoldingTestCase extends LightPlatformCodeInsightFixtureTestCase {
  @Override
  protected String getTestDataPath() {
    return "tests/testData";
  }

  private void doTest() {
    final String filePath = getTestDataPath() + "/" + getTestName(true) + ".php";
    myFixture.configureByFile(filePath);
    myFixture.testFolding(filePath);
  }

  public void testArrayMerge() {
    doTest();
  }

  public void testMethodsAndProperties() {
    doTest();
  }

  public void testGetters() {
    doTest();
  }

  public void testSetters() {
    doTest();
  }

  public void testLambdas() {
    doTest();
  }

  public void testStrpos() {
    doTest();
  }

  public void testInArray() {
    doTest();
  }

  public void testArrayCreation() {
    doTest();
  }

  public void testClassConstantSelf() {
    doTest();
  }

  public void testNotInstanceof() {
    doTest();
  }

  public void testNamespaceReference() {
    doTest();
  }

  public void testEmptinessCheckWithCountCall() {
    doTest();
  }
}