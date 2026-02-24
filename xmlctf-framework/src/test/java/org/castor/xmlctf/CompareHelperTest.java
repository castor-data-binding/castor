package org.castor.xmlctf;

import static org.junit.Assert.*;
import org.junit.Test;

public class CompareHelperTest {

  @Test
  public void testEqualsWithBothNull() {
    assertTrue(CompareHelper.equals(null, null));
  }

  @Test
  public void testEqualsWithFirstNull() {
    assertFalse(CompareHelper.equals(null, "test"));
  }

  @Test
  public void testEqualsWithSecondNull() {
    assertFalse(CompareHelper.equals("test", null));
  }

  @Test
  public void testEqualsWithDifferentClasses() {
    assertFalse(CompareHelper.equals("test", 123));
  }

  @Test
  public void testEqualsWithEqualStrings() {
    assertTrue(CompareHelper.equals("test", "test"));
  }

  @Test
  public void testEqualsWithDifferentStrings() {
    assertFalse(CompareHelper.equals("test1", "test2"));
  }

  @Test
  public void testEqualsWithEqualIntegers() {
    assertTrue(CompareHelper.equals(42, 42));
  }

  @Test
  public void testEqualsWithDifferentIntegers() {
    assertFalse(CompareHelper.equals(42, 43));
  }

  @Test
  public void testEqualsWithEqualIntArrays() {
    assertTrue(CompareHelper.equals(new int[]{1, 2, 3}, new int[]{1, 2, 3}));
  }

  @Test
  public void testEqualsWithDifferentIntArrayLengths() {
    assertFalse(CompareHelper.equals(new int[]{1, 2}, new int[]{1, 2, 3}));
  }

  @Test
  public void testEqualsWithDifferentIntArrayValues() {
    assertFalse(CompareHelper.equals(new int[]{1, 2, 3}, new int[]{1, 2, 4}));
  }

  @Test
  public void testEqualsWithEqualByteArrays() {
    assertTrue(CompareHelper.equals(new byte[]{1, 2, 3}, new byte[]{1, 2, 3}));
  }

  @Test
  public void testEqualsWithDifferentByteArrayLengths() {
    assertFalse(CompareHelper.equals(new byte[]{1, 2}, new byte[]{1, 2, 3}));
  }

  @Test
  public void testEqualsWithDifferentByteArrayValues() {
    assertFalse(CompareHelper.equals(new byte[]{1, 2, 3}, new byte[]{1, 2, 4}));
  }

  @Test
  public void testEqualsWithEqualBooleanArrays() {
    assertTrue(CompareHelper.equals(new boolean[]{true, false}, new boolean[]{true, false}));
  }

  @Test
  public void testEqualsWithDifferentBooleanArrayValues() {
    assertFalse(CompareHelper.equals(new boolean[]{true, false}, new boolean[]{false, true}));
  }

  @Test
  public void testEqualsWithEqualCharArrays() {
    assertTrue(CompareHelper.equals(new char[]{'a', 'b'}, new char[]{'a', 'b'}));
  }

  @Test
  public void testEqualsWithDifferentCharArrayValues() {
    assertFalse(CompareHelper.equals(new char[]{'a', 'b'}, new char[]{'a', 'c'}));
  }

  @Test
  public void testEqualsWithEqualDoubleArrays() {
    assertTrue(CompareHelper.equals(new double[]{1.0, 2.0}, new double[]{1.0, 2.0}));
  }

  @Test
  public void testEqualsWithDifferentDoubleArrayValues() {
    assertFalse(CompareHelper.equals(new double[]{1.0, 2.0}, new double[]{1.0, 2.1}));
  }

  @Test
  public void testEqualsWithEqualFloatArrays() {
    assertTrue(CompareHelper.equals(new float[]{1.0f, 2.0f}, new float[]{1.0f, 2.0f}));
  }

  @Test
  public void testEqualsWithDifferentFloatArrayValues() {
    assertFalse(CompareHelper.equals(new float[]{1.0f, 2.0f}, new float[]{1.0f, 2.1f}));
  }

  @Test
  public void testEqualsWithEqualLongArrays() {
    assertTrue(CompareHelper.equals(new long[]{1L, 2L}, new long[]{1L, 2L}));
  }

  @Test
  public void testEqualsWithDifferentLongArrayValues() {
    assertFalse(CompareHelper.equals(new long[]{1L, 2L}, new long[]{1L, 3L}));
  }

  @Test
  public void testEqualsWithEqualShortArrays() {
    assertTrue(CompareHelper.equals(new short[]{1, 2}, new short[]{1, 2}));
  }

  @Test
  public void testEqualsWithDifferentShortArrayValues() {
    assertFalse(CompareHelper.equals(new short[]{1, 2}, new short[]{1, 3}));
  }

  @Test
  public void testEqualsWithEqualObjectArrays() {
    assertTrue(CompareHelper.equals(new String[]{"a", "b"}, new String[]{"a", "b"}));
  }

  @Test
  public void testEqualsWithDifferentObjectArrayValues() {
    assertFalse(CompareHelper.equals(new String[]{"a", "b"}, new String[]{"a", "c"}));
  }

  @Test
  public void testEqualsWithEmptyArrays() {
    assertTrue(CompareHelper.equals(new int[]{}, new int[]{}));
  }

  @Test
  public void testEqualsWithDifferentArrayComponentTypes() {
    assertFalse(CompareHelper.equals(new int[]{1, 2}, new Integer[]{1, 2}));
  }

  @Test
  public void testComparePrimitiveArrayBooleanTrue() {
    assertTrue(CompareHelper.comparePrimitiveArray(new boolean[]{true, false, true},
        new boolean[]{true, false, true}));
  }

  @Test
  public void testComparePrimitiveArrayBooleanFalse() {
    assertFalse(CompareHelper.comparePrimitiveArray(new boolean[]{true, false},
        new boolean[]{false, true}));
  }

  @Test
  public void testComparePrimitiveArrayByteTrue() {
    assertTrue(
        CompareHelper.comparePrimitiveArray(new byte[]{1, 2, 3}, new byte[]{1, 2, 3}));
  }

  @Test
  public void testComparePrimitiveArrayByteFalse() {
    assertFalse(CompareHelper.comparePrimitiveArray(new byte[]{1, 2, 3}, new byte[]{1, 2, 4}));
  }

  @Test
  public void testComparePrimitiveArrayCharTrue() {
    assertTrue(CompareHelper.comparePrimitiveArray(new char[]{'a', 'b'}, new char[]{'a', 'b'}));
  }

  @Test
  public void testComparePrimitiveArrayCharFalse() {
    assertFalse(CompareHelper.comparePrimitiveArray(new char[]{'a', 'b'}, new char[]{'a', 'c'}));
  }

  @Test
  public void testComparePrimitiveArrayDoubleTrue() {
    assertTrue(CompareHelper.comparePrimitiveArray(new double[]{1.0, 2.0}, new double[]{1.0, 2.0}));
  }

  @Test
  public void testComparePrimitiveArrayDoubleFalse() {
    assertFalse(
        CompareHelper.comparePrimitiveArray(new double[]{1.0, 2.0}, new double[]{1.0, 2.1}));
  }

  @Test
  public void testComparePrimitiveArrayFloatTrue() {
    assertTrue(CompareHelper.comparePrimitiveArray(new float[]{1.0f, 2.0f}, new float[]{1.0f, 2.0f}));
  }

  @Test
  public void testComparePrimitiveArrayFloatFalse() {
    assertFalse(
        CompareHelper.comparePrimitiveArray(new float[]{1.0f, 2.0f}, new float[]{1.0f, 2.1f}));
  }

  @Test
  public void testComparePrimitiveArrayIntTrue() {
    assertTrue(CompareHelper.comparePrimitiveArray(new int[]{1, 2, 3}, new int[]{1, 2, 3}));
  }

  @Test
  public void testComparePrimitiveArrayIntFalse() {
    assertFalse(CompareHelper.comparePrimitiveArray(new int[]{1, 2, 3}, new int[]{1, 2, 4}));
  }

  @Test
  public void testComparePrimitiveArrayLongTrue() {
    assertTrue(CompareHelper.comparePrimitiveArray(new long[]{1L, 2L}, new long[]{1L, 2L}));
  }

  @Test
  public void testComparePrimitiveArrayLongFalse() {
    assertFalse(CompareHelper.comparePrimitiveArray(new long[]{1L, 2L}, new long[]{1L, 3L}));
  }

  @Test
  public void testComparePrimitiveArrayShortTrue() {
    assertTrue(CompareHelper.comparePrimitiveArray(new short[]{1, 2}, new short[]{1, 2}));
  }

  @Test
  public void testComparePrimitiveArrayShortFalse() {
    assertFalse(CompareHelper.comparePrimitiveArray(new short[]{1, 2}, new short[]{1, 3}));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testComparePrimitiveArrayWithInvalidType() {
    // Create a custom class to simulate an unexpected primitive type
    Object customArray = new Object();
    CompareHelper.comparePrimitiveArray(customArray, customArray);
  }

  @Test
  public void testEqualsWithNullElements() {
    // Test with null elements in object arrays - should throw NullPointerException
    try {
      CompareHelper.equals(new String[]{null, "b"}, new String[]{"a", "b"});
      fail("Expected NullPointerException");
    } catch (NullPointerException e) {
      // Expected
    }
  }

}
