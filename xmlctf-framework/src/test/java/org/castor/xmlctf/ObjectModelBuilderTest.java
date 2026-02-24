package org.castor.xmlctf;

import static org.junit.Assert.*;
import org.junit.Test;

/**
 * Test suite for ObjectModelBuilder interface.
 */
public class ObjectModelBuilderTest {

  /**
   * Mock implementation of ObjectModelBuilder for testing.
   */
  private static class MockObjectModelBuilder implements ObjectModelBuilder {
    private Object instance;

    public MockObjectModelBuilder(Object instance) {
      this.instance = instance;
    }

    @Override
    public Object buildInstance() throws Exception {
      if (instance == null) {
        throw new Exception("Cannot build null instance");
      }
      return instance;
    }
  }

  /**
   * Mock implementation that throws an exception.
   */
  private static class FailingObjectModelBuilder implements ObjectModelBuilder {
    @Override
    public Object buildInstance() throws Exception {
      throw new Exception("Build failed");
    }
  }

  @Test
  public void testBuildInstanceWithString() throws Exception {
    String expectedString = "test";
    ObjectModelBuilder builder = new MockObjectModelBuilder(expectedString);
    Object result = builder.buildInstance();
    assertEquals(expectedString, result);
  }

  @Test
  public void testBuildInstanceWithInteger() throws Exception {
    Integer expectedInteger = 42;
    ObjectModelBuilder builder = new MockObjectModelBuilder(expectedInteger);
    Object result = builder.buildInstance();
    assertEquals(expectedInteger, result);
  }

  @Test
  public void testBuildInstanceWithObject() throws Exception {
    Object expectedObject = new Object();
    ObjectModelBuilder builder = new MockObjectModelBuilder(expectedObject);
    Object result = builder.buildInstance();
    assertSame(expectedObject, result);
  }

  @Test
  public void testBuildInstanceWithArray() throws Exception {
    String[] expectedArray = {"a", "b", "c"};
    ObjectModelBuilder builder = new MockObjectModelBuilder(expectedArray);
    Object result = builder.buildInstance();
    assertArrayEquals(expectedArray, (String[]) result);
  }

  @Test
  public void testBuildInstanceWithList() throws Exception {
    java.util.List<String> expectedList = java.util.Arrays.asList("x", "y", "z");
    ObjectModelBuilder builder = new MockObjectModelBuilder(expectedList);
    Object result = builder.buildInstance();
    assertEquals(expectedList, result);
  }

  @Test
  public void testBuildInstanceWithMap() throws Exception {
    java.util.Map<String, Integer> expectedMap = new java.util.HashMap<>();
    expectedMap.put("one", 1);
    expectedMap.put("two", 2);
    ObjectModelBuilder builder = new MockObjectModelBuilder(expectedMap);
    Object result = builder.buildInstance();
    assertEquals(expectedMap, result);
  }

  @Test
  public void testBuildInstanceThrowsException() {
    ObjectModelBuilder builder = new FailingObjectModelBuilder();
    try {
      builder.buildInstance();
      fail("Expected exception not thrown");
    } catch (Exception e) {
      assertEquals("Build failed", e.getMessage());
    }
  }

  @Test
  public void testBuildInstanceWithNullThrowsException() {
    ObjectModelBuilder builder = new MockObjectModelBuilder(null);
    try {
      builder.buildInstance();
      fail("Expected exception not thrown");
    } catch (Exception e) {
      assertEquals("Cannot build null instance", e.getMessage());
    }
  }

  @Test
  public void testMultipleBuildsReturnConsistentInstances() throws Exception {
    String expectedString = "consistent";
    ObjectModelBuilder builder = new MockObjectModelBuilder(expectedString);

    Object result1 = builder.buildInstance();
    Object result2 = builder.buildInstance();

    assertEquals(result1, result2);
  }

  @Test
  public void testBuildInstanceWithBoolean() throws Exception {
    Boolean expectedBoolean = true;
    ObjectModelBuilder builder = new MockObjectModelBuilder(expectedBoolean);
    Object result = builder.buildInstance();
    assertEquals(expectedBoolean, result);
  }

  @Test
  public void testBuildInstanceWithDouble() throws Exception {
    Double expectedDouble = 3.14159;
    ObjectModelBuilder builder = new MockObjectModelBuilder(expectedDouble);
    Object result = builder.buildInstance();
    assertEquals(expectedDouble, result);
  }

  @Test
  public void testBuildInstanceWithLong() throws Exception {
    Long expectedLong = 1234567890L;
    ObjectModelBuilder builder = new MockObjectModelBuilder(expectedLong);
    Object result = builder.buildInstance();
    assertEquals(expectedLong, result);
  }

  @Test
  public void testBuildInstanceWithComplexObject() throws Exception {
    class ComplexObject {
      String name;
      int value;

      ComplexObject(String name, int value) {
        this.name = name;
        this.value = value;
      }
    }

    ComplexObject expectedObject = new ComplexObject("test", 100);
    ObjectModelBuilder builder = new MockObjectModelBuilder(expectedObject);
    Object result = builder.buildInstance();
    assertSame(expectedObject, result);
    assertEquals("test", ((ComplexObject) result).name);
    assertEquals(100, ((ComplexObject) result).value);
  }

  @Test
  public void testBuilderIsReusable() throws Exception {
    String value = "reusable";
    ObjectModelBuilder builder = new MockObjectModelBuilder(value);

    for (int i = 0; i < 5; i++) {
      Object result = builder.buildInstance();
      assertEquals(value, result);
    }
  }

  @Test
  public void testBuildInstanceWithEmptyString() throws Exception {
    String expectedString = "";
    ObjectModelBuilder builder = new MockObjectModelBuilder(expectedString);
    Object result = builder.buildInstance();
    assertEquals(expectedString, result);
  }

  @Test
  public void testBuildInstanceWithZero() throws Exception {
    Integer expectedInteger = 0;
    ObjectModelBuilder builder = new MockObjectModelBuilder(expectedInteger);
    Object result = builder.buildInstance();
    assertEquals(expectedInteger, result);
  }

  @Test
  public void testBuildInstanceWithNegativeNumber() throws Exception {
    Integer expectedInteger = -42;
    ObjectModelBuilder builder = new MockObjectModelBuilder(expectedInteger);
    Object result = builder.buildInstance();
    assertEquals(expectedInteger, result);
  }

  @Test
  public void testBuildInstanceWithSpecialCharacters() throws Exception {
    String expectedString = "!@#$%^&*()_+-=[]{}|;:',.<>?/";
    ObjectModelBuilder builder = new MockObjectModelBuilder(expectedString);
    Object result = builder.buildInstance();
    assertEquals(expectedString, result);
  }

  @Test
  public void testInterfaceMethodSignature() throws Exception {
    // Verify that the interface has the buildInstance method
    ObjectModelBuilder builder = new MockObjectModelBuilder("test");
    assertTrue(builder instanceof ObjectModelBuilder);
    assertNotNull(builder.buildInstance());
  }

  @Test
  public void testBuildInstanceWithByteArray() throws Exception {
    byte[] expectedArray = {1, 2, 3, 4, 5};
    ObjectModelBuilder builder = new MockObjectModelBuilder(expectedArray);
    Object result = builder.buildInstance();
    assertArrayEquals(expectedArray, (byte[]) result);
  }

  @Test
  public void testBuildInstanceWithBooleanArray() throws Exception {
    boolean[] expectedArray = {true, false, true};
    ObjectModelBuilder builder = new MockObjectModelBuilder(expectedArray);
    Object result = builder.buildInstance();

    boolean[] resultArray = (boolean[]) result;
    assertEquals(expectedArray.length, resultArray.length);
    for (int i = 0; i < expectedArray.length; i++) {
      assertEquals(expectedArray[i], resultArray[i]);
    }
  }

  @Test
  public void testBuildInstanceWithCharArray() throws Exception {
    char[] expectedArray = {'a', 'b', 'c'};
    ObjectModelBuilder builder = new MockObjectModelBuilder(expectedArray);
    Object result = builder.buildInstance();

    char[] resultArray = (char[]) result;
    assertEquals(expectedArray.length, resultArray.length);
    for (int i = 0; i < expectedArray.length; i++) {
      assertEquals(expectedArray[i], resultArray[i]);
    }
  }

  @Test
  public void testBuildInstanceException() throws Exception {
    // Test exception handling path
    ObjectModelBuilder failingBuilder = new FailingObjectModelBuilder();
    Exception caughtException = null;

    try {
      failingBuilder.buildInstance();
    } catch (Exception e) {
      caughtException = e;
    }

    assertNotNull(caughtException);
    assertEquals("Build failed", caughtException.getMessage());
  }

  @Test
  public void testBuildInstanceBranchCoverage() throws Exception {
    // Test both success and failure paths
    ObjectModelBuilder successBuilder = new MockObjectModelBuilder("success");
    Object successResult = successBuilder.buildInstance();
    assertNotNull(successResult);

    ObjectModelBuilder failBuilder = new FailingObjectModelBuilder();
    try {
      failBuilder.buildInstance();
      fail("Should have thrown exception");
    } catch (Exception e) {
      assertNotNull(e.getMessage());
    }
  }
}
