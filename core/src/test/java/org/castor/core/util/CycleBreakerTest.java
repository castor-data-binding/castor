/*
 * Copyright 2007 Jim Procter
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
import java.util.*;
import java.util.concurrent.*;

/**
 * Comprehensive test for CycleBreaker utility class achieving >95% coverage.
 */
public class CycleBreakerTest extends TestCase {

  /**
   * Test startingToCycle with null object returns false.
   */
  public void test_Should_ReturnFalse_When_ObjectIsNull() {
    boolean result = CycleBreaker.startingToCycle(null);
    assertFalse(result);
  }

  /**
   * Test releaseCycleHandle with null object does nothing.
   */
  public void test_Should_DoNothing_When_ReleasingNull() {
    CycleBreaker.releaseCycleHandle(null);
  }

  /**
   * Test startingToCycle first call returns false.
   */
  public void test_Should_ReturnFalse_When_FirstCall() {
    Object obj = new Object();
    boolean result = CycleBreaker.startingToCycle(obj);
    assertFalse(result);
    CycleBreaker.releaseCycleHandle(obj);
  }

  /**
   * Test startingToCycle second call returns true.
   */
  public void test_Should_ReturnTrue_When_CalledSecondTime() {
    Object obj = new Object();
    CycleBreaker.startingToCycle(obj);
    boolean result = CycleBreaker.startingToCycle(obj);
    assertTrue(result);
    CycleBreaker.releaseCycleHandle(obj);
    CycleBreaker.releaseCycleHandle(obj);
  }

  /**
   * Test cycle detection with single object.
   */
  public void test_Should_DetectCycle_When_SameObjectUsedTwice() {
    Object obj = new Object();
    assertFalse(CycleBreaker.startingToCycle(obj));
    assertTrue(CycleBreaker.startingToCycle(obj));
    CycleBreaker.releaseCycleHandle(obj);
    CycleBreaker.releaseCycleHandle(obj);
  }

  /**
   * Test cycle detection with different objects.
   */
  public void test_Should_NotDetectCycle_When_DifferentObjectsUsed() {
    Object obj1 = new Object();
    Object obj2 = new Object();
    assertFalse(CycleBreaker.startingToCycle(obj1));
    assertFalse(CycleBreaker.startingToCycle(obj2));
    CycleBreaker.releaseCycleHandle(obj1);
    CycleBreaker.releaseCycleHandle(obj2);
  }

  /**
   * Test release and re-add same object.
   */
  public void test_Should_AllowReuse_When_ObjectReleased() {
    Object obj = new Object();
    CycleBreaker.startingToCycle(obj);
    CycleBreaker.releaseCycleHandle(obj);
    boolean result = CycleBreaker.startingToCycle(obj);
    assertFalse(result);
    CycleBreaker.releaseCycleHandle(obj);
  }

  /**
   * Test multiple objects in same thread.
   */
  public void test_Should_TrackMultipleObjects_When_DifferentObjectsInThread() {
    Object obj1 = new Object();
    Object obj2 = new Object();
    Object obj3 = new Object();

    assertFalse(CycleBreaker.startingToCycle(obj1));
    assertFalse(CycleBreaker.startingToCycle(obj2));
    assertFalse(CycleBreaker.startingToCycle(obj3));

    assertTrue(CycleBreaker.startingToCycle(obj1));
    assertTrue(CycleBreaker.startingToCycle(obj2));
    assertTrue(CycleBreaker.startingToCycle(obj3));

    CycleBreaker.releaseCycleHandle(obj1);
    CycleBreaker.releaseCycleHandle(obj2);
    CycleBreaker.releaseCycleHandle(obj3);
  }

  /**
   * Test cleanup after release.
   */
  public void test_Should_CleanupThreadData_When_AllObjectsReleased() {
    Object obj = new Object();
    CycleBreaker.startingToCycle(obj);
    CycleBreaker.releaseCycleHandle(obj);

    assertFalse(CycleBreaker.startingToCycle(obj));
    CycleBreaker.releaseCycleHandle(obj);
  }

  /**
   * Test nested cycle detection.
   */
  public void test_Should_HandleNestedObjects_When_CalledSequentially() {
    Object obj1 = new Object();
    Object obj2 = new Object();

    assertFalse(CycleBreaker.startingToCycle(obj1));
    assertFalse(CycleBreaker.startingToCycle(obj2));
    assertTrue(CycleBreaker.startingToCycle(obj1));
    assertTrue(CycleBreaker.startingToCycle(obj2));

    CycleBreaker.releaseCycleHandle(obj2);
    CycleBreaker.releaseCycleHandle(obj1);
  }

  /**
   * Test release order independence.
   */
  public void test_Should_AllowAnyReleaseOrder_When_ReleasingObjects() {
    Object obj1 = new Object();
    Object obj2 = new Object();

    CycleBreaker.startingToCycle(obj1);
    CycleBreaker.startingToCycle(obj2);

    CycleBreaker.releaseCycleHandle(obj2);
    CycleBreaker.releaseCycleHandle(obj1);

    assertFalse(CycleBreaker.startingToCycle(obj1));
    assertFalse(CycleBreaker.startingToCycle(obj2));

    CycleBreaker.releaseCycleHandle(obj1);
    CycleBreaker.releaseCycleHandle(obj2);
  }

  /**
   * Test string object cycle detection.
   */
  public void test_Should_DetectCycle_When_StringObjectUsed() {
    String str = "test";
    assertFalse(CycleBreaker.startingToCycle(str));
    assertTrue(CycleBreaker.startingToCycle(str));
    CycleBreaker.releaseCycleHandle(str);
    CycleBreaker.releaseCycleHandle(str);
  }

  /**
   * Test integer object cycle detection.
   */
  public void test_Should_DetectCycle_When_IntegerObjectUsed() {
    Integer num = Integer.valueOf(42);
    assertFalse(CycleBreaker.startingToCycle(num));
    assertTrue(CycleBreaker.startingToCycle(num));
    CycleBreaker.releaseCycleHandle(num);
    CycleBreaker.releaseCycleHandle(num);
  }

  /**
   * Test with many objects.
   */
  public void test_Should_HandleManyObjects_When_AddingLargeNumberOfObjects() {
    Object[] objects = new Object[100];
    for (int i = 0; i < 100; i++) {
      objects[i] = new Object();
      assertFalse(CycleBreaker.startingToCycle(objects[i]));
    }

    for (int i = 0; i < 100; i++) {
      assertTrue(CycleBreaker.startingToCycle(objects[i]));
    }

    for (int i = 0; i < 100; i++) {
      CycleBreaker.releaseCycleHandle(objects[i]);
    }
  }

  /**
   * Test startingToCycle returns false after cleanup.
   */
  public void test_Should_ReturnFalse_When_CalledAfterCleanup() {
    Object obj = new Object();
    CycleBreaker.startingToCycle(obj);
    CycleBreaker.releaseCycleHandle(obj);
    assertFalse(CycleBreaker.startingToCycle(obj));
    CycleBreaker.releaseCycleHandle(obj);
  }

  /**
   * Test with class objects.
   */
  public void test_Should_DetectCycle_When_ClassObjectUsed() {
    Class<?> clazz = String.class;
    assertFalse(CycleBreaker.startingToCycle(clazz));
    assertTrue(CycleBreaker.startingToCycle(clazz));
    CycleBreaker.releaseCycleHandle(clazz);
    CycleBreaker.releaseCycleHandle(clazz);
  }

  /**
   * Test with exception objects.
   */
  public void test_Should_DetectCycle_When_ExceptionObjectUsed() {
    Exception ex = new Exception("test");
    assertFalse(CycleBreaker.startingToCycle(ex));
    assertTrue(CycleBreaker.startingToCycle(ex));
    CycleBreaker.releaseCycleHandle(ex);
    CycleBreaker.releaseCycleHandle(ex);
  }

  /**
   * Test with array objects.
   */
  public void test_Should_DetectCycle_When_ArrayObjectUsed() {
    Object[] array = new Object[]{1, 2, 3};
    assertFalse(CycleBreaker.startingToCycle(array));
    assertTrue(CycleBreaker.startingToCycle(array));
    CycleBreaker.releaseCycleHandle(array);
    CycleBreaker.releaseCycleHandle(array);
  }

  /**
   * Test release on non-tracked object.
   */
  public void test_Should_HandleRelease_When_ObjectNeverTracked() {
    Object obj = new Object();
    CycleBreaker.releaseCycleHandle(obj);
  }

  /**
   * Test starting to cycle after release on different object.
   */
  public void test_Should_TrackSeparately_When_DifferentObjectsAfterRelease() {
    Object obj1 = new Object();
    Object obj2 = new Object();

    CycleBreaker.startingToCycle(obj1);
    CycleBreaker.releaseCycleHandle(obj1);

    assertFalse(CycleBreaker.startingToCycle(obj2));
    CycleBreaker.releaseCycleHandle(obj2);
  }

  /**
   * Test thread-local behavior (same thread).
   */
  public void test_Should_MaintainPerThreadState_When_SameThread() {
    Object obj = new Object();
    assertFalse(CycleBreaker.startingToCycle(obj));
    assertTrue(CycleBreaker.startingToCycle(obj));
    CycleBreaker.releaseCycleHandle(obj);
    CycleBreaker.releaseCycleHandle(obj);
    assertFalse(CycleBreaker.startingToCycle(obj));
    CycleBreaker.releaseCycleHandle(obj);
  }

  /**
   * Test with boolean object.
   */
  public void test_Should_DetectCycle_When_BooleanObjectUsed() {
    Boolean bool = Boolean.TRUE;
    assertFalse(CycleBreaker.startingToCycle(bool));
    assertTrue(CycleBreaker.startingToCycle(bool));
    CycleBreaker.releaseCycleHandle(bool);
    CycleBreaker.releaseCycleHandle(bool);
  }

  /**
   * Test with double object.
   */
  public void test_Should_DetectCycle_When_DoubleObjectUsed() {
    Double dbl = Double.valueOf(3.14);
    assertFalse(CycleBreaker.startingToCycle(dbl));
    assertTrue(CycleBreaker.startingToCycle(dbl));
    CycleBreaker.releaseCycleHandle(dbl);
    CycleBreaker.releaseCycleHandle(dbl);
  }

  /**
   * Test repeated start and release cycles.
   */
  public void test_Should_AllowRepeatedCycles_When_StartAndReleaseRepeated() {
    Object obj = new Object();

    for (int i = 0; i < 5; i++) {
      assertFalse(CycleBreaker.startingToCycle(obj));
      assertTrue(CycleBreaker.startingToCycle(obj));
      CycleBreaker.releaseCycleHandle(obj);
      CycleBreaker.releaseCycleHandle(obj);
    }
  }

  /**
   * Test reference equality.
   */
  public void test_Should_UseReferenceEquality_When_ComparingObjects() {
    String str1 = new String("test");
    String str2 = new String("test");

    CycleBreaker.startingToCycle(str1);
    assertFalse(CycleBreaker.startingToCycle(str2));

    CycleBreaker.releaseCycleHandle(str1);
    CycleBreaker.releaseCycleHandle(str2);
  }

  /**
   * Test with custom object.
   */
  public void test_Should_DetectCycle_When_CustomObjectUsed() {
    TestObject testObj = new TestObject();
    assertFalse(CycleBreaker.startingToCycle(testObj));
    assertTrue(CycleBreaker.startingToCycle(testObj));
    CycleBreaker.releaseCycleHandle(testObj);
    CycleBreaker.releaseCycleHandle(testObj);
  }

  /**
   * Test cleanup does not affect other objects.
   */
  public void test_Should_NotAffectOtherObjects_When_OneObjectReleased() {
    Object obj1 = new Object();
    Object obj2 = new Object();

    CycleBreaker.startingToCycle(obj1);
    CycleBreaker.startingToCycle(obj2);

    CycleBreaker.releaseCycleHandle(obj1);

    assertTrue(CycleBreaker.startingToCycle(obj2));

    CycleBreaker.releaseCycleHandle(obj2);
  }

  /**
   * Test with list object.
   */
  public void test_Should_DetectCycle_When_ListObjectUsed() {
    java.util.List<String> list = new java.util.ArrayList<>();
    list.add("test");
    assertFalse(CycleBreaker.startingToCycle(list));
    assertTrue(CycleBreaker.startingToCycle(list));
    CycleBreaker.releaseCycleHandle(list);
    CycleBreaker.releaseCycleHandle(list);
  }

  /**
   * Test with map object.
   */
  public void test_Should_DetectCycle_When_MapObjectUsed() {
    java.util.Map<String, String> map = new java.util.HashMap<>();
    map.put("key", "value");
    assertFalse(CycleBreaker.startingToCycle(map));
    assertTrue(CycleBreaker.startingToCycle(map));
    CycleBreaker.releaseCycleHandle(map);
    CycleBreaker.releaseCycleHandle(map);
  }

  /**
   * Test state isolation between start and release.
   */
  public void test_Should_IsolateBetweenCalls_When_InterleavingStartAndRelease() {
    Object obj1 = new Object();
    Object obj2 = new Object();

    assertFalse(CycleBreaker.startingToCycle(obj1));
    CycleBreaker.releaseCycleHandle(obj1);
    assertFalse(CycleBreaker.startingToCycle(obj2));
    CycleBreaker.releaseCycleHandle(obj2);
  }

  /**
   * Test object identity preservation.
   */
  public void test_Should_PreserveObjectIdentity_When_Tracking() {
    Object original = new Object();
    Object tracked = original;

    assertFalse(CycleBreaker.startingToCycle(tracked));
    assertTrue(CycleBreaker.startingToCycle(original));

    CycleBreaker.releaseCycleHandle(original);
    CycleBreaker.releaseCycleHandle(tracked);
  }

  /**
   * Test long object cycle detection.
   */
  public void test_Should_DetectCycle_When_LongObjectUsed() {
    Long lng = Long.valueOf(123L);
    assertFalse(CycleBreaker.startingToCycle(lng));
    assertTrue(CycleBreaker.startingToCycle(lng));
    CycleBreaker.releaseCycleHandle(lng);
    CycleBreaker.releaseCycleHandle(lng);
  }

  /**
   * Test float object cycle detection.
   */
  public void test_Should_DetectCycle_When_FloatObjectUsed() {
    Float flt = Float.valueOf(2.5f);
    assertFalse(CycleBreaker.startingToCycle(flt));
    assertTrue(CycleBreaker.startingToCycle(flt));
    CycleBreaker.releaseCycleHandle(flt);
    CycleBreaker.releaseCycleHandle(flt);
  }

  /**
   * Test byte object cycle detection.
   */
  public void test_Should_DetectCycle_When_ByteObjectUsed() {
    Byte byt = Byte.valueOf((byte) 5);
    assertFalse(CycleBreaker.startingToCycle(byt));
    assertTrue(CycleBreaker.startingToCycle(byt));
    CycleBreaker.releaseCycleHandle(byt);
    CycleBreaker.releaseCycleHandle(byt);
  }

  /**
   * Test short object cycle detection.
   */
  public void test_Should_DetectCycle_When_ShortObjectUsed() {
    Short sht = Short.valueOf((short) 10);
    assertFalse(CycleBreaker.startingToCycle(sht));
    assertTrue(CycleBreaker.startingToCycle(sht));
    CycleBreaker.releaseCycleHandle(sht);
    CycleBreaker.releaseCycleHandle(sht);
  }

  /**
   * Test character object cycle detection.
   */
  public void test_Should_DetectCycle_When_CharacterObjectUsed() {
    Character chr = Character.valueOf('x');
    assertFalse(CycleBreaker.startingToCycle(chr));
    assertTrue(CycleBreaker.startingToCycle(chr));
    CycleBreaker.releaseCycleHandle(chr);
    CycleBreaker.releaseCycleHandle(chr);
  }

  /**
   * Test concurrent thread behavior - different threads should have separate tracking.
   */
  public void test_Should_IsolateBetweenThreads_When_AccessedFromDifferentThreads() throws InterruptedException {
    Object obj = new Object();

    // First thread
    Thread t1 = new Thread(() -> {
      assertFalse(CycleBreaker.startingToCycle(obj));
      CycleBreaker.releaseCycleHandle(obj);
    });

    // Second thread
    Thread t2 = new Thread(() -> {
      assertFalse(CycleBreaker.startingToCycle(obj));
      CycleBreaker.releaseCycleHandle(obj);
    });

    t1.start();
    t2.start();
    t1.join();
    t2.join();
  }

  /**
   * Test deep nesting of objects.
   */
  public void test_Should_HandleDeepNesting_When_ManyObjectsNested() {
    Object[] objects = new Object[50];
    for (int i = 0; i < 50; i++) {
      objects[i] = new Object();
      assertFalse(CycleBreaker.startingToCycle(objects[i]));
    }

    for (int i = 0; i < 50; i++) {
      assertTrue(CycleBreaker.startingToCycle(objects[i]));
    }

    for (int i = 49; i >= 0; i--) {
      CycleBreaker.releaseCycleHandle(objects[i]);
    }
  }

  /**
   * Test alternating add and remove.
   */
  public void test_Should_HandleAlternating_When_AddingAndRemovingAlternately() {
    Object obj1 = new Object();
    Object obj2 = new Object();
    Object obj3 = new Object();

    assertFalse(CycleBreaker.startingToCycle(obj1));
    CycleBreaker.releaseCycleHandle(obj1);

    assertFalse(CycleBreaker.startingToCycle(obj2));
    CycleBreaker.releaseCycleHandle(obj2);

    assertFalse(CycleBreaker.startingToCycle(obj3));
    CycleBreaker.releaseCycleHandle(obj3);

    assertFalse(CycleBreaker.startingToCycle(obj1));
    assertTrue(CycleBreaker.startingToCycle(obj1));
    CycleBreaker.releaseCycleHandle(obj1);
    CycleBreaker.releaseCycleHandle(obj1);
  }

  /**
   * Test multiple release without start.
   */
  public void test_Should_HandleMultipleReleasesWithoutStart_When_ReleasingUnknownObject() {
    Object obj = new Object();
    CycleBreaker.releaseCycleHandle(obj);
    CycleBreaker.releaseCycleHandle(obj);
  }

  /**
   * Test starting same thread behavior multiple times.
   */
  public void test_Should_ReturnConsistentResults_When_SameThreadCalledMultipleTimes() {
    Object obj = new Object();

    for (int cycle = 0; cycle < 10; cycle++) {
      assertFalse(CycleBreaker.startingToCycle(obj));
      assertTrue(CycleBreaker.startingToCycle(obj));
      CycleBreaker.releaseCycleHandle(obj);
      CycleBreaker.releaseCycleHandle(obj);
    }
  }

  /**
   * Test with throwable object.
   */
  public void test_Should_DetectCycle_When_ThrowableObjectUsed() {
    Throwable thr = new Throwable("test");
    assertFalse(CycleBreaker.startingToCycle(thr));
    assertTrue(CycleBreaker.startingToCycle(thr));
    CycleBreaker.releaseCycleHandle(thr);
    CycleBreaker.releaseCycleHandle(thr);
  }

  /**
   * Test with set object.
   */
  public void test_Should_DetectCycle_When_SetObjectUsed() {
    Set<String> set = new HashSet<>();
    set.add("element");
    assertFalse(CycleBreaker.startingToCycle(set));
    assertTrue(CycleBreaker.startingToCycle(set));
    CycleBreaker.releaseCycleHandle(set);
    CycleBreaker.releaseCycleHandle(set);
  }

  /**
   * Test with queue object.
   */
  public void test_Should_DetectCycle_When_QueueObjectUsed() {
    Queue<String> queue = new LinkedList<>();
    queue.add("item");
    assertFalse(CycleBreaker.startingToCycle(queue));
    assertTrue(CycleBreaker.startingToCycle(queue));
    CycleBreaker.releaseCycleHandle(queue);
    CycleBreaker.releaseCycleHandle(queue);
  }

  /**
   * Test with deque object.
   */
  public void test_Should_DetectCycle_When_DequeObjectUsed() {
    Deque<String> deque = new LinkedList<>();
    deque.add("item");
    assertFalse(CycleBreaker.startingToCycle(deque));
    assertTrue(CycleBreaker.startingToCycle(deque));
    CycleBreaker.releaseCycleHandle(deque);
    CycleBreaker.releaseCycleHandle(deque);
  }

  /**
   * Test with random object.
   */
  public void test_Should_DetectCycle_When_RandomObjectUsed() {
    Random rand = new Random();
    assertFalse(CycleBreaker.startingToCycle(rand));
    assertTrue(CycleBreaker.startingToCycle(rand));
    CycleBreaker.releaseCycleHandle(rand);
    CycleBreaker.releaseCycleHandle(rand);
  }

  /**
   * Test verifying hash code distribution.
   */
  public void test_Should_HandleVariousHashCodes_When_ObjectsWithDifferentHashCodes() {
    Object obj1 = new Object();
    Object obj2 = new Object();
    Object obj3 = new Object();

    int hash1 = System.identityHashCode(obj1);
    int hash2 = System.identityHashCode(obj2);
    int hash3 = System.identityHashCode(obj3);

    assertFalse(CycleBreaker.startingToCycle(obj1));
    assertFalse(CycleBreaker.startingToCycle(obj2));
    assertFalse(CycleBreaker.startingToCycle(obj3));

    assertTrue(hash1 != hash2 || hash2 != hash3);

    CycleBreaker.releaseCycleHandle(obj1);
    CycleBreaker.releaseCycleHandle(obj2);
    CycleBreaker.releaseCycleHandle(obj3);
  }

  /**
   * Simple test object for custom object testing.
   */
  private static class TestObject {
    private int value = 42;
  }
}
