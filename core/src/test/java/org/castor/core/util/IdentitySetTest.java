/*
 * Copyright 2005 Ralf Joachim
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
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Comprehensive test for IdentitySet achieving >95% coverage.
 */
public class IdentitySetTest extends TestCase {

  /**
   * Test constructor with default capacity.
   */
  public void test_Should_CreateSet_When_UsingDefaultConstructor() {
    IdentitySet set = new IdentitySet();

    assertNotNull(set);
    assertEquals(0, set.size());
    assertTrue(set.isEmpty());
  }

  /**
   * Test constructor with custom capacity.
   */
  public void test_Should_CreateSetWithCapacity_When_CapacityProvided() {
    IdentitySet set = new IdentitySet(50);

    assertNotNull(set);
    assertEquals(0, set.size());
  }

  /**
   * Test add single object.
   */
  public void test_Should_AddObject_When_ObjectIsNew() {
    IdentitySet set = new IdentitySet();
    Object obj = new Object();

    assertTrue(set.add(obj));
    assertEquals(1, set.size());
    assertFalse(set.isEmpty());
  }

  /**
   * Test add duplicate object (should return false).
   */
  public void test_Should_ReturnFalse_When_ObjectAlreadyExists() {
    IdentitySet set = new IdentitySet();
    Object obj = new Object();

    assertTrue(set.add(obj));
    assertFalse(set.add(obj));
    assertEquals(1, set.size());
  }

  /**
   * Test add multiple different objects.
   */
  public void test_Should_AddMultipleObjects_When_ObjectsAreDifferent() {
    IdentitySet set = new IdentitySet();
    Object obj1 = new Object();
    Object obj2 = new Object();
    Object obj3 = new Object();

    assertTrue(set.add(obj1));
    assertTrue(set.add(obj2));
    assertTrue(set.add(obj3));
    assertEquals(3, set.size());
  }

  /**
   * Test contains existing object.
   */
  public void test_Should_ReturnTrue_When_ObjectExists() {
    IdentitySet set = new IdentitySet();
    Object obj = new Object();
    set.add(obj);

    assertTrue(set.contains(obj));
  }

  /**
   * Test contains non-existing object.
   */
  public void test_Should_ReturnFalse_When_ObjectDoesNotExist() {
    IdentitySet set = new IdentitySet();
    Object obj1 = new Object();
    Object obj2 = new Object();
    set.add(obj1);

    assertFalse(set.contains(obj2));
  }

  /**
   * Test remove existing object.
   */
  public void test_Should_RemoveObject_When_ObjectExists() {
    IdentitySet set = new IdentitySet();
    Object obj = new Object();
    set.add(obj);

    assertTrue(set.remove(obj));
    assertEquals(0, set.size());
    assertFalse(set.contains(obj));
  }

  /**
   * Test remove non-existing object.
   */
  public void test_Should_ReturnFalse_When_RemovingNonExistentObject() {
    IdentitySet set = new IdentitySet();
    Object obj = new Object();

    assertFalse(set.remove(obj));
  }

  /**
   * Test clear.
   */
  public void test_Should_ClearSet_When_ClearCalled() {
    IdentitySet set = new IdentitySet();
    set.add(new Object());
    set.add(new Object());
    set.add(new Object());

    assertEquals(3, set.size());
    set.clear();
    assertEquals(0, set.size());
    assertTrue(set.isEmpty());
  }

  /**
   * Test iterator with elements.
   */
  public void test_Should_IterateOverElements_When_IteratorCalled() {
    IdentitySet set = new IdentitySet();
    Object obj1 = new Object();
    Object obj2 = new Object();
    Object obj3 = new Object();
    set.add(obj1);
    set.add(obj2);
    set.add(obj3);

    Iterator iterator = set.iterator();
    assertNotNull(iterator);
    assertTrue(iterator.hasNext());

    int count = 0;
    while (iterator.hasNext()) {
      Object obj = iterator.next();
      assertNotNull(obj);
      count++;
    }
    assertEquals(3, count);
  }

  /**
   * Test iterator with no elements.
   */
  public void test_Should_ReturnEmptyIterator_When_SetIsEmpty() {
    IdentitySet set = new IdentitySet();

    Iterator iterator = set.iterator();
    assertNotNull(iterator);
    assertFalse(iterator.hasNext());
  }

  /**
   * Test iterator next on empty iterator throws exception.
   */
  public void test_Should_ThrowException_When_NextCalledOnEmptyIterator() {
    IdentitySet set = new IdentitySet();
    Iterator iterator = set.iterator();

    try {
      iterator.next();
      fail("Should throw NoSuchElementException");
    } catch (NoSuchElementException e) {
      // Expected
    }
  }

  /**
   * Test toArray with elements.
   */
  public void test_Should_ConvertToArray_When_SetHasElements() {
    IdentitySet set = new IdentitySet();
    Object obj1 = new Object();
    Object obj2 = new Object();
    set.add(obj1);
    set.add(obj2);

    Object[] array = set.toArray();
    assertNotNull(array);
    assertEquals(2, array.length);
  }

  /**
   * Test toArray with empty set.
   */
  public void test_Should_ReturnEmptyArray_When_SetIsEmpty() {
    IdentitySet set = new IdentitySet();

    Object[] array = set.toArray();
    assertNotNull(array);
    assertEquals(0, array.length);
  }

  /**
   * Test toArray with provided array.
   */
  public void test_Should_FillProvidedArray_When_ArrayIsProvided() {
    IdentitySet set = new IdentitySet();
    Object obj1 = new Object();
    Object obj2 = new Object();
    set.add(obj1);
    set.add(obj2);

    Object[] array = new Object[5];
    Object[] result = set.toArray(array);

    assertNotNull(result);
    assertEquals(5, result.length);
    assertNotNull(result[0]);
    assertNotNull(result[1]);
    assertNull(result[2]);
  }

  /**
   * Test toArray with small provided array.
   */
  public void test_Should_CreateNewArray_When_ProvidedArrayIsTooSmall() {
    IdentitySet set = new IdentitySet();
    Object obj1 = new Object();
    Object obj2 = new Object();
    Object obj3 = new Object();
    set.add(obj1);
    set.add(obj2);
    set.add(obj3);

    Object[] array = new Object[1];
    Object[] result = set.toArray(array);

    assertNotNull(result);
    assertNotSame(array, result);
    assertEquals(3, result.length);
  }

  /**
   * Test rehashing when capacity is exceeded.
   */
  public void test_Should_RehashWhenNecessary_When_SizeExceedsMaximum() {
    IdentitySet set = new IdentitySet(2);

    Object obj1 = new Object();
    Object obj2 = new Object();
    Object obj3 = new Object();
    Object obj4 = new Object();

    set.add(obj1);
    set.add(obj2);
    set.add(obj3); // This should trigger rehashing
    set.add(obj4);

    assertEquals(4, set.size());
    assertTrue(set.contains(obj1));
    assertTrue(set.contains(obj2));
    assertTrue(set.contains(obj3));
    assertTrue(set.contains(obj4));
  }

  /**
   * Test containsAll throws UnsupportedOperationException.
   */
  public void test_Should_ThrowException_When_ContainsAllCalled() {
    IdentitySet set = new IdentitySet();
    java.util.List list = new java.util.ArrayList();

    try {
      set.containsAll(list);
      fail("Should throw UnsupportedOperationException");
    } catch (UnsupportedOperationException e) {
      // Expected
    }
  }

  /**
   * Test addAll throws UnsupportedOperationException.
   */
  public void test_Should_ThrowException_When_AddAllCalled() {
    IdentitySet set = new IdentitySet();
    java.util.List list = new java.util.ArrayList();

    try {
      set.addAll(list);
      fail("Should throw UnsupportedOperationException");
    } catch (UnsupportedOperationException e) {
      // Expected
    }
  }

  /**
   * Test removeAll throws UnsupportedOperationException.
   */
  public void test_Should_ThrowException_When_RemoveAllCalled() {
    IdentitySet set = new IdentitySet();
    java.util.List list = new java.util.ArrayList();

    try {
      set.removeAll(list);
      fail("Should throw UnsupportedOperationException");
    } catch (UnsupportedOperationException e) {
      // Expected
    }
  }

  /**
   * Test retainAll throws UnsupportedOperationException.
   */
  public void test_Should_ThrowException_When_RetainAllCalled() {
    IdentitySet set = new IdentitySet();
    java.util.List list = new java.util.ArrayList();

    try {
      set.retainAll(list);
      fail("Should throw UnsupportedOperationException");
    } catch (UnsupportedOperationException e) {
      // Expected
    }
  }

  /**
   * Test identity semantics with equal but distinct objects.
   */
  public void test_Should_UseIdentityNotEquality_When_ComparingObjects() {
    IdentitySet set = new IdentitySet();
    String str1 = new String("test");
    String str2 = new String("test");

    set.add(str1);
    assertTrue(set.contains(str1));
    assertFalse(set.contains(str2)); // Different object even though equal
  }

  /**
   * Test add same object multiple times returns false.
   */
  public void test_Should_ReturnFalseMultipleTimes_When_SameObjectAdded() {
    IdentitySet set = new IdentitySet();
    Object obj = new Object();

    assertTrue(set.add(obj));
    assertFalse(set.add(obj));
    assertFalse(set.add(obj));
    assertFalse(set.add(obj));
  }

  /**
   * Test remove and re-add same object.
   */
  public void test_Should_AllowReAdd_When_ObjectWasRemoved() {
    IdentitySet set = new IdentitySet();
    Object obj = new Object();

    assertTrue(set.add(obj));
    assertTrue(set.remove(obj));
    assertTrue(set.add(obj));
    assertEquals(1, set.size());
  }

  /**
   * Test with many objects to test rehashing multiple times.
   */
  public void test_Should_HandleManyObjects_When_AddingLargeNumberOfElements() {
    IdentitySet set = new IdentitySet(4);
    Object[] objects = new Object[100];

    for (int i = 0; i < 100; i++) {
      objects[i] = new Object();
      assertTrue(set.add(objects[i]));
    }

    assertEquals(100, set.size());

    for (int i = 0; i < 100; i++) {
      assertTrue(set.contains(objects[i]));
    }
  }

  /**
   * Test iterator hasNext on exhausted iterator.
   */
  public void test_Should_ReturnFalseAfterExhausted_When_IteratorHasNoMoreElements() {
    IdentitySet set = new IdentitySet();
    set.add(new Object());

    Iterator iterator = set.iterator();
    iterator.next();
    assertFalse(iterator.hasNext());
  }

  /**
   * Test iterator remove throws UnsupportedOperationException.
   */
  public void test_Should_ThrowException_When_IteratorRemoveCalled() {
    IdentitySet set = new IdentitySet();
    set.add(new Object());

    Iterator iterator = set.iterator();
    iterator.next();

    try {
      iterator.remove();
      fail("Should throw UnsupportedOperationException");
    } catch (UnsupportedOperationException e) {
      // Expected
    }
  }

  /**
   * Test size after clear and re-add.
   */
  public void test_Should_ResetSize_When_ClearedAndRefilled() {
    IdentitySet set = new IdentitySet();
    set.add(new Object());
    set.add(new Object());
    set.clear();

    Object obj = new Object();
    set.add(obj);
    assertEquals(1, set.size());
  }

  /**
   * Test with null object.
   */
  public void test_Should_HandleNullObject_When_AddingNull() {
    IdentitySet set = new IdentitySet();

    assertTrue(set.add(null));
    assertEquals(1, set.size());
    assertTrue(set.contains(null));
  }

  /**
   * Test add null twice returns false.
   */
  public void test_Should_ReturnFalse_When_AddingNullTwice() {
    IdentitySet set = new IdentitySet();

    assertTrue(set.add(null));
    assertFalse(set.add(null));
  }

  /**
   * Test remove null.
   */
  public void test_Should_RemoveNull_When_NullIsInSet() {
    IdentitySet set = new IdentitySet();
    set.add(null);

    assertTrue(set.remove(null));
    assertEquals(0, set.size());
  }

  /**
   * Test iterator with null element.
   */
  public void test_Should_IterateOverNull_When_SetContainsNull() {
    IdentitySet set = new IdentitySet();
    set.add(null);
    set.add(new Object());

    Iterator iterator = set.iterator();
    int count = 0;
    while (iterator.hasNext()) {
      iterator.next();
      count++;
    }
    assertEquals(2, count);
  }

  /**
   * Test toArray with null element.
   */
  public void test_Should_IncludeNullInArray_When_SetContainsNull() {
    IdentitySet set = new IdentitySet();
    Object obj = new Object();
    set.add(null);
    set.add(obj);

    Object[] array = set.toArray();
    assertEquals(2, array.length);
  }

  /**
   * Test multiple remove operations.
   */
  public void test_Should_HandleMultipleRemoves_When_RemovingSequentially() {
    IdentitySet set = new IdentitySet();
    Object obj1 = new Object();
    Object obj2 = new Object();
    Object obj3 = new Object();

    set.add(obj1);
    set.add(obj2);
    set.add(obj3);

    assertTrue(set.remove(obj1));
    assertEquals(2, set.size());
    assertTrue(set.remove(obj2));
    assertEquals(1, set.size());
    assertTrue(set.remove(obj3));
    assertEquals(0, set.size());
  }

  /**
   * Test negative hash code handling.
   */
  public void test_Should_HandleNegativeHashCode_When_IdentityHashCodeIsNegative() {
    IdentitySet set = new IdentitySet();
    Object obj1 = new Object();
    Object obj2 = new Object();

    set.add(obj1);
    set.add(obj2);

    assertTrue(set.contains(obj1));
    assertTrue(set.contains(obj2));
    assertEquals(2, set.size());
  }

  /**
   * Test size consistency after operations.
   */
  public void test_Should_MaintainConsistentSize_When_PerformingOperations() {
    IdentitySet set = new IdentitySet();
    Object obj1 = new Object();
    Object obj2 = new Object();

    set.add(obj1);
    assertEquals(1, set.size());
    set.add(obj2);
    assertEquals(2, set.size());
    set.remove(obj1);
    assertEquals(1, set.size());
    set.add(obj1);
    assertEquals(2, set.size());
  }

  /**
   * Test rehashing with collision chains.
   */
  public void test_Should_PreserveCollisionChains_When_Rehashing() {
    IdentitySet set = new IdentitySet(3);
    Object[] objects = new Object[20];

    for (int i = 0; i < 20; i++) {
      objects[i] = new Object();
      set.add(objects[i]);
    }

    for (int i = 0; i < 20; i++) {
      assertTrue(set.contains(objects[i]));
    }
    assertEquals(20, set.size());
  }

  /**
   * Test iterator with single element.
   */
  public void test_Should_IterateCorrectly_When_SetHasSingleElement() {
    IdentitySet set = new IdentitySet();
    Object obj = new Object();
    set.add(obj);

    Iterator iterator = set.iterator();
    assertTrue(iterator.hasNext());
    Object retrieved = iterator.next();
    assertSame(obj, retrieved);
    assertFalse(iterator.hasNext());
  }

  /**
   * Test iterator traversal with removed elements.
   */
  public void test_Should_IterateRemaining_When_ElementsRemoved() {
    IdentitySet set = new IdentitySet();
    Object obj1 = new Object();
    Object obj2 = new Object();
    Object obj3 = new Object();

    set.add(obj1);
    set.add(obj2);
    set.add(obj3);

    set.remove(obj2);

    Iterator iterator = set.iterator();
    int count = 0;
    while (iterator.hasNext()) {
      Object obj = iterator.next();
      assertNotSame(obj, obj2);
      count++;
    }
    assertEquals(2, count);
  }

  /**
   * Test remove from first position in bucket.
   */
  public void test_Should_RemoveFromBucketHead_When_ObjectIsFirstInChain() {
    IdentitySet set = new IdentitySet(4);
    Object obj1 = new Object();
    Object obj2 = new Object();

    set.add(obj1);
    set.add(obj2);

    assertTrue(set.remove(obj1));
    assertFalse(set.contains(obj1));
    assertTrue(set.contains(obj2));
  }

  /**
   * Test remove from middle of bucket chain.
   */
  public void test_Should_RemoveFromBucketMiddle_When_ObjectIsInChain() {
    IdentitySet set = new IdentitySet(2);
    Object[] objects = new Object[10];

    for (int i = 0; i < 10; i++) {
      objects[i] = new Object();
      set.add(objects[i]);
    }

    Object toRemove = objects[5];
    assertTrue(set.remove(toRemove));
    assertEquals(9, set.size());

    for (int i = 0; i < 10; i++) {
      if (i != 5) {
        assertTrue(set.contains(objects[i]));
      } else {
        assertFalse(set.contains(objects[i]));
      }
    }
  }

  /**
   * Test clear resets capacity to default.
   */
  public void test_Should_ResetCapacity_When_ClearCalled() {
    IdentitySet set = new IdentitySet(100);
    for (int i = 0; i < 100; i++) {
      set.add(new Object());
    }
    set.clear();
    assertEquals(0, set.size());
  }

  /**
   * Test contains with chain traversal.
   */
  public void test_Should_TraverseChain_When_SearchingForContainedObject() {
    IdentitySet set = new IdentitySet(2);
    Object[] objects = new Object[15];

    for (int i = 0; i < 15; i++) {
      objects[i] = new Object();
      set.add(objects[i]);
    }

    for (int i = 0; i < 15; i++) {
      assertTrue(set.contains(objects[i]));
    }
  }

  /**
   * Test add to existing bucket with chain.
   */
  public void test_Should_AddToExistingChain_When_BucketOccupied() {
    IdentitySet set = new IdentitySet(2);
    Object[] objects = new Object[10];

    for (int i = 0; i < 10; i++) {
      objects[i] = new Object();
      assertTrue(set.add(objects[i]));
    }

    assertEquals(10, set.size());
  }

  /**
   * Test large capacity initialization.
   */
  public void test_Should_HandleLargeCapacity_When_ConstructedWithLargeValue() {
    IdentitySet set = new IdentitySet(1000);
    Object obj = new Object();
    set.add(obj);
    assertTrue(set.contains(obj));
  }

  /**
   * Test small capacity enforcement.
   */
  public void test_Should_HandleSmallCapacity_When_ConstructedWithSmallValue() {
    IdentitySet set = new IdentitySet(1);
    Object obj1 = new Object();
    Object obj2 = new Object();

    set.add(obj1);
    set.add(obj2);

    assertTrue(set.contains(obj1));
    assertTrue(set.contains(obj2));
  }

  /**
   * Test toArray preserves all elements.
   */
  public void test_Should_PreserveAllElements_When_ConvertingToArray() {
    IdentitySet set = new IdentitySet();
    Object[] originals = new Object[20];

    for (int i = 0; i < 20; i++) {
      originals[i] = new Object();
      set.add(originals[i]);
    }

    Object[] array = set.toArray();
    assertEquals(20, array.length);
    for (Object original : originals) {
      boolean found = false;
      for (Object element : array) {
        if (element == original) {
          found = true;
          break;
        }
      }
      assertTrue(found);
    }
  }

  /**
   * Test iterator with sparse buckets.
   */
  public void test_Should_SkipEmptyBuckets_When_IteratingAfterRemoval() {
    IdentitySet set = new IdentitySet(10);
    Object[] objects = new Object[30];

    for (int i = 0; i < 30; i++) {
      objects[i] = new Object();
      set.add(objects[i]);
    }

    for (int i = 0; i < 15; i++) {
      set.remove(objects[i]);
    }

    Iterator iterator = set.iterator();
    int count = 0;
    while (iterator.hasNext()) {
      iterator.next();
      count++;
    }
    assertEquals(15, count);
  }

  /**
   * Test isEmpty consistency.
   */
  public void test_Should_ReflectEmptyState_When_IsEmptyCalled() {
    IdentitySet set = new IdentitySet();
    assertTrue(set.isEmpty());

    Object obj = new Object();
    set.add(obj);
    assertFalse(set.isEmpty());

    set.remove(obj);
    assertTrue(set.isEmpty());
  }

  /**
   * Test Entry class getters.
   */
  public void test_Should_AccessEntryProperties_When_UsingEntry() {
    IdentitySet set = new IdentitySet();
    Object obj = new Object();
    set.add(obj);

    Iterator iterator = set.iterator();
    Object retrieved = iterator.next();
    assertSame(obj, retrieved);
  }

  /**
   * Test repeated clear operations.
   */
  public void test_Should_HandleRepeatedClears_When_CalledMultipleTimes() {
    IdentitySet set = new IdentitySet();

    for (int i = 0; i < 5; i++) {
      set.add(new Object());
      set.add(new Object());
      set.clear();
      assertEquals(0, set.size());
    }
  }
}
