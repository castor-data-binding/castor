/*
 * Copyright 2006 Ralf Joachim
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package org.castor.core.util;

import junit.framework.TestCase;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Vector;

/**
 * Comprehensive test for EnumerationIterator.
 */
public class EnumerationIteratorTest extends TestCase {

  /**
   * Test constructor with empty enumeration.
   */
  public void test_Should_CreateIterator_When_ConstructorCalledWithEnumeration() {
    Vector<String> vector = new Vector<>();
    EnumerationIterator iterator = new EnumerationIterator(vector.elements());

    assertNotNull(iterator);
  }

  /**
   * Test hasNext returns false on empty enumeration.
   */
  public void test_Should_ReturnFalse_When_EnumerationIsEmpty() {
    Vector<String> vector = new Vector<>();
    EnumerationIterator iterator = new EnumerationIterator(vector.elements());

    assertFalse(iterator.hasNext());
  }

  /**
   * Test hasNext returns true with elements.
   */
  public void test_Should_ReturnTrue_When_EnumerationHasElements() {
    Vector<String> vector = new Vector<>();
    vector.add("test");
    EnumerationIterator iterator = new EnumerationIterator(vector.elements());

    assertTrue(iterator.hasNext());
  }

  /**
   * Test next returns element.
   */
  public void test_Should_ReturnElement_When_NextCalled() {
    Vector<String> vector = new Vector<>();
    vector.add("test");
    EnumerationIterator iterator = new EnumerationIterator(vector.elements());

    assertEquals("test", iterator.next());
  }

  /**
   * Test next throws NoSuchElementException on empty enumeration.
   */
  public void test_Should_ThrowException_When_NextCalledOnEmpty() {
    Vector<String> vector = new Vector<>();
    EnumerationIterator iterator = new EnumerationIterator(vector.elements());

    try {
      iterator.next();
      fail("Should throw NoSuchElementException");
    } catch (NoSuchElementException e) {
      // Expected
    }
  }

  /**
   * Test remove throws UnsupportedOperationException.
   */
  public void test_Should_ThrowUnsupported_When_RemoveCalled() {
    Vector<String> vector = new Vector<>();
    vector.add("test");
    EnumerationIterator iterator = new EnumerationIterator(vector.elements());
    iterator.next();

    try {
      iterator.remove();
      fail("Should throw UnsupportedOperationException");
    } catch (UnsupportedOperationException e) {
      // Expected
    }
  }

  /**
   * Test iteration over multiple elements.
   */
  public void test_Should_IterateMultipleElements_When_EnumerationHasMultipleElements() {
    Vector<String> vector = new Vector<>();
    vector.add("first");
    vector.add("second");
    vector.add("third");

    EnumerationIterator iterator = new EnumerationIterator(vector.elements());

    assertTrue(iterator.hasNext());
    assertEquals("first", iterator.next());
    assertTrue(iterator.hasNext());
    assertEquals("second", iterator.next());
    assertTrue(iterator.hasNext());
    assertEquals("third", iterator.next());
    assertFalse(iterator.hasNext());
  }

  /**
   * Test hasNext multiple calls.
   */
  public void test_Should_ReturnSameValue_When_HasNextCalledMultipleTimes() {
    Vector<String> vector = new Vector<>();
    vector.add("test");
    EnumerationIterator iterator = new EnumerationIterator(vector.elements());

    assertTrue(iterator.hasNext());
    assertTrue(iterator.hasNext());
    assertTrue(iterator.hasNext());
  }

  /**
   * Test next after hasNext.
   */
  public void test_Should_ReturnElement_When_NextCalledAfterHasNext() {
    Vector<String> vector = new Vector<>();
    vector.add("test");
    EnumerationIterator iterator = new EnumerationIterator(vector.elements());

    if (iterator.hasNext()) {
      assertEquals("test", iterator.next());
    }
  }

  /**
   * Test with null elements.
   */
  public void test_Should_HandleNull_When_EnumerationContainsNull() {
    Vector<String> vector = new Vector<>();
    vector.add(null);
    vector.add("test");

    EnumerationIterator iterator = new EnumerationIterator(vector.elements());

    assertTrue(iterator.hasNext());
    assertNull(iterator.next());
    assertTrue(iterator.hasNext());
    assertEquals("test", iterator.next());
  }

  /**
   * Test with different object types.
   */
  public void test_Should_HandleDifferentTypes_When_EnumerationHasMixedTypes() {
    Vector<Object> vector = new Vector<>();
    vector.add("string");
    vector.add(123);
    vector.add(45.67);

    EnumerationIterator iterator = new EnumerationIterator(vector.elements());

    assertTrue(iterator.hasNext());
    assertEquals("string", iterator.next());
    assertTrue(iterator.hasNext());
    assertEquals(123, iterator.next());
    assertTrue(iterator.hasNext());
    assertEquals(45.67, iterator.next());
  }

  /**
   * Test remove on first element throws.
   */
  public void test_Should_ThrowOnRemoveAtStart_When_RemoveCalledImmediately() {
    Vector<String> vector = new Vector<>();
    vector.add("test");
    EnumerationIterator iterator = new EnumerationIterator(vector.elements());

    try {
      iterator.remove();
      fail("Should throw UnsupportedOperationException");
    } catch (UnsupportedOperationException e) {
      // Expected
    }
  }

  /**
   * Test iterator implements Iterator interface.
   */
  public void test_Should_ImplementIterator_When_CheckingInterface() {
    Vector<String> vector = new Vector<>();
    EnumerationIterator iterator = new EnumerationIterator(vector.elements());

    assertTrue(iterator instanceof Iterator);
  }

  /**
   * Test with large enumeration.
   */
  public void test_Should_HandleLargeEnumeration_When_EnumerationHasManyElements() {
    Vector<Integer> vector = new Vector<>();
    for (int i = 0; i < 1000; i++) {
      vector.add(i);
    }

    EnumerationIterator iterator = new EnumerationIterator(vector.elements());

    int count = 0;
    while (iterator.hasNext()) {
      iterator.next();
      count++;
    }

    assertEquals(1000, count);
  }

  /**
   * Test exhausting iterator.
   */
  public void test_Should_BeExhausted_When_AllElementsConsumed() {
    Vector<String> vector = new Vector<>();
    vector.add("a");
    vector.add("b");

    EnumerationIterator iterator = new EnumerationIterator(vector.elements());

    iterator.next();
    iterator.next();

    assertFalse(iterator.hasNext());
  }

  /**
   * Test next after exhaustion throws.
   */
  public void test_Should_ThrowAfterExhausted_When_NextCalledAfterEnd() {
    Vector<String> vector = new Vector<>();
    vector.add("test");

    EnumerationIterator iterator = new EnumerationIterator(vector.elements());
    iterator.next();

    try {
      iterator.next();
      fail("Should throw NoSuchElementException");
    } catch (NoSuchElementException e) {
      // Expected
    }
  }

  /**
   * Test with single element.
   */
  public void test_Should_HandleSingleElement_When_EnumerationHasOneElement() {
    Vector<String> vector = new Vector<>();
    vector.add("only");

    EnumerationIterator iterator = new EnumerationIterator(vector.elements());

    assertTrue(iterator.hasNext());
    assertEquals("only", iterator.next());
    assertFalse(iterator.hasNext());
  }

  /**
   * Test constructor with different vector sizes.
   */
  public void test_Should_CreateIteratorForDifferentSizes_When_ConstructorCalledWithVariousSizes() {
    for (int size = 0; size < 10; size++) {
      Vector<Integer> vector = new Vector<>();
      for (int i = 0; i < size; i++) {
        vector.add(i);
      }

      EnumerationIterator iterator = new EnumerationIterator(vector.elements());
      assertNotNull(iterator);
    }
  }

  /**
   * Test multiple iterators from same enumeration.
   */
  public void test_Should_AllowMultipleIterators_When_CreatedFromSameEnumeration() {
    Vector<String> vector = new Vector<>();
    vector.add("a");
    vector.add("b");

    EnumerationIterator it1 = new EnumerationIterator(vector.elements());
    EnumerationIterator it2 = new EnumerationIterator(vector.elements());

    assertEquals(it1.next(), "a");
    assertEquals(it2.next(), "a");
  }

  /**
   * Test sequence of operations.
   */
  public void test_Should_HandleSequence_When_PerformingManyOperations() {
    Vector<String> vector = new Vector<>();
    vector.add("x");
    vector.add("y");
    vector.add("z");

    EnumerationIterator iterator = new EnumerationIterator(vector.elements());

    assertTrue(iterator.hasNext());
    assertEquals("x", iterator.next());
    assertTrue(iterator.hasNext());
    assertTrue(iterator.hasNext());
    assertEquals("y", iterator.next());
    assertEquals("z", iterator.next());
    assertFalse(iterator.hasNext());
  }

  /**
   * Test with boolean values.
   */
  public void test_Should_HandleBooleanValues_When_EnumerationContainsBooleans() {
    Vector<Boolean> vector = new Vector<>();
    vector.add(true);
    vector.add(false);

    EnumerationIterator iterator = new EnumerationIterator(vector.elements());

    assertTrue(iterator.hasNext());
    assertEquals(true, iterator.next());
    assertEquals(false, iterator.next());
  }

  /**
   * Test with duplicate elements.
   */
  public void test_Should_HandleDuplicates_When_EnumerationHasDuplicateElements() {
    Vector<String> vector = new Vector<>();
    vector.add("same");
    vector.add("same");
    vector.add("same");

    EnumerationIterator iterator = new EnumerationIterator(vector.elements());

    int count = 0;
    while (iterator.hasNext()) {
      assertEquals("same", iterator.next());
      count++;
    }

    assertEquals(3, count);
  }

  /**
   * Test remove throws consistently.
   */
  public void test_Should_AlwaysThrow_When_RemoveCalledMultipleTimes() {
    Vector<String> vector = new Vector<>();
    vector.add("test");

    EnumerationIterator iterator = new EnumerationIterator(vector.elements());

    try {
      iterator.remove();
      fail("Should throw");
    } catch (UnsupportedOperationException e) {
      // Expected
    }

    try {
      iterator.remove();
      fail("Should throw again");
    } catch (UnsupportedOperationException e) {
      // Expected
    }
  }

  /**
   * Test hasNext before any next calls.
   */
  public void test_Should_ReturnTrue_When_HasNextCalledBeforeNext() {
    Vector<String> vector = new Vector<>();
    vector.add("element");

    EnumerationIterator iterator = new EnumerationIterator(vector.elements());
    assertTrue(iterator.hasNext());
  }

  /**
   * Test next returns correct sequence.
   */
  public void test_Should_ReturnInOrder_When_ElementsRetrieved() {
    Vector<Integer> vector = new Vector<>();
    for (int i = 1; i <= 5; i++) {
      vector.add(i);
    }

    EnumerationIterator iterator = new EnumerationIterator(vector.elements());

    for (int i = 1; i <= 5; i++) {
      assertEquals(i, iterator.next());
    }
  }

  /**
   * Test with object array elements.
   */
  public void test_Should_HandleArrays_When_EnumerationContainsArrays() {
    Vector<Object> vector = new Vector<>();
    vector.add(new int[]{1, 2, 3});
    vector.add(new String[]{"a", "b"});

    EnumerationIterator iterator = new EnumerationIterator(vector.elements());

    assertTrue(iterator.hasNext());
    Object first = iterator.next();
    assertNotNull(first);
    assertTrue(iterator.hasNext());
    Object second = iterator.next();
    assertNotNull(second);
  }

  /**
   * Test empty then non-empty vector states.
   */
  public void test_Should_TransitionStates_When_VectorChanges() {
    Vector<String> vector = new Vector<>();
    EnumerationIterator emptyIterator = new EnumerationIterator(vector.elements());
    assertFalse(emptyIterator.hasNext());

    vector.add("element");
    EnumerationIterator nonEmptyIterator = new EnumerationIterator(vector.elements());
    assertTrue(nonEmptyIterator.hasNext());
  }

  /**
   * Test hasNext and next interaction patterns.
   */
  public void test_Should_SupportTypicalPattern_When_UsingStandardIteratorPattern() {
    Vector<String> vector = new Vector<>();
    vector.add("a");
    vector.add("b");

    EnumerationIterator iterator = new EnumerationIterator(vector.elements());
    int count = 0;

    while (iterator.hasNext()) {
      Object element = iterator.next();
      assertNotNull(element);
      count++;
    }

    assertEquals(2, count);
  }

  /**
   * Test remove before next throws.
   */
  public void test_Should_ThrowBeforeNext_When_RemoveCalledWithoutNext() {
    Vector<String> vector = new Vector<>();
    vector.add("test");

    EnumerationIterator iterator = new EnumerationIterator(vector.elements());

    try {
      iterator.remove();
      fail("Should throw UnsupportedOperationException");
    } catch (UnsupportedOperationException e) {
      // Expected
    }
  }

  /**
   * Test with special string values.
   */
  public void test_Should_HandleSpecialStrings_When_StringsAreSpecial() {
    Vector<String> vector = new Vector<>();
    vector.add("");
    vector.add(" ");
    vector.add("\t");
    vector.add("\n");

    EnumerationIterator iterator = new EnumerationIterator(vector.elements());

    assertEquals("", iterator.next());
    assertEquals(" ", iterator.next());
    assertEquals("\t", iterator.next());
    assertEquals("\n", iterator.next());
  }

  /**
   * Test consistency across multiple passes.
   */
  public void test_Should_BeConsistent_When_CalledMultipleTimes() {
    Vector<String> vector = new Vector<>();
    vector.add("test");

    for (int pass = 0; pass < 3; pass++) {
      EnumerationIterator iterator = new EnumerationIterator(vector.elements());
      assertTrue(iterator.hasNext());
      assertEquals("test", iterator.next());
      assertFalse(iterator.hasNext());
    }
  }

  /**
   * Test type casting of returned objects.
   */
  public void test_Should_AllowTypeCasting_When_ElementsAreCastable() {
    Vector<String> vector = new Vector<>();
    vector.add("castable");

    EnumerationIterator iterator = new EnumerationIterator(vector.elements());

    Object obj = iterator.next();
    String str = (String) obj;
    assertEquals("castable", str);
  }
}
