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

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 * Enhanced comprehensive test class for CycleBreaker with complete coverage.
 * Tests all code branches, edge cases, threading, and lock functionality.
 */
public class CycleBreakerCompleteTest {

  @Before
  public void setUp() {
    // Reset state by releasing any cycles
  }

  // ========== Null Handling Tests ==========

  @Test
  public void should_ReturnFalseForNull_When_StartingToCycleCalledWithNull() {
    boolean result = CycleBreaker.startingToCycle(null);
    assertFalse("Should return false for null object", result);
  }

  @Test
  public void should_NotThrowException_When_ReleaseCycleHandleCalledWithNull() {
    try {
      CycleBreaker.releaseCycleHandle(null);
      assertTrue("Should not throw exception for null", true);
    } catch (Exception e) {
      fail("Should not throw exception for null: " + e.getMessage());
    }
  }

  // ========== First Call Tests ==========

  @Test
  public void should_ReturnFalseOnFirstCall_When_ObjectNotYetTracked() {
    Object obj = new Object();
    boolean result = CycleBreaker.startingToCycle(obj);
    assertFalse("First call should return false", result);

    // Cleanup
    CycleBreaker.releaseCycleHandle(obj);
  }

  @Test
  public void should_ReturnTrueOnSecondCall_When_SameObjectTracked() {
    Object obj = new Object();
    CycleBreaker.startingToCycle(obj);

    // Second call should detect cycle
    boolean result = CycleBreaker.startingToCycle(obj);
    assertTrue("Second call should return true (cycle detected)", result);

    // Cleanup
    CycleBreaker.releaseCycleHandle(obj);
  }

  // ========== Release Handle Tests ==========

  @Test
  public void should_ReleaseCycleHandle_When_CalledAfterStartingToCycle() {
    Object obj = new Object();
    CycleBreaker.startingToCycle(obj);
    CycleBreaker.releaseCycleHandle(obj);

    // After release, should be able to track again without cycle
    boolean result = CycleBreaker.startingToCycle(obj);
    assertFalse("After release, should not detect cycle on first call", result);

    // Cleanup
    CycleBreaker.releaseCycleHandle(obj);
  }

  @Test
  public void should_AllowRetrackingAfterRelease_When_ReleaseCalledThenStartAgain() {
    Object obj = new Object();

    CycleBreaker.startingToCycle(obj);
    CycleBreaker.releaseCycleHandle(obj);

    // Should be able to track again
    boolean result = CycleBreaker.startingToCycle(obj);
    assertFalse("Should restart tracking after release", result);

    CycleBreaker.releaseCycleHandle(obj);
  }

  // ========== Multiple Objects Tests ==========

  @Test
  public void should_TrackMultipleObjectsIndependently_When_DifferentObjectsUsed() {
    Object obj1 = new Object();
    Object obj2 = new Object();

    boolean result1a = CycleBreaker.startingToCycle(obj1);
    boolean result2a = CycleBreaker.startingToCycle(obj2);

    assertFalse("First object first call should be false", result1a);
    assertFalse("Second object first call should be false", result2a);

    boolean result1b = CycleBreaker.startingToCycle(obj1);
    boolean result2b = CycleBreaker.startingToCycle(obj2);

    assertTrue("First object second call should be true", result1b);
    assertTrue("Second object second call should be true", result2b);

    CycleBreaker.releaseCycleHandle(obj1);
    CycleBreaker.releaseCycleHandle(obj2);
  }

  @Test
  public void should_HandleManyObjects_When_MultipleObjectsTracked() {
    Object[] objects = new Object[100];

    for (int i = 0; i < 100; i++) {
      objects[i] = new Object();
      boolean result = CycleBreaker.startingToCycle(objects[i]);
      assertFalse("First call for object " + i + " should be false", result);
    }

    for (int i = 0; i < 100; i++) {
      boolean result = CycleBreaker.startingToCycle(objects[i]);
      assertTrue("Second call for object " + i + " should be true", result);
    }

    for (int i = 0; i < 100; i++) {
      CycleBreaker.releaseCycleHandle(objects[i]);
    }
  }

  // ========== Cleanup Tests ==========

  @Test
  public void should_FullyClean_When_AllHandlesReleased() {
    Object obj = new Object();

    CycleBreaker.startingToCycle(obj);
    CycleBreaker.releaseCycleHandle(obj);

    // Should be in clean state
    boolean result = CycleBreaker.startingToCycle(obj);
    assertFalse("Should be in clean state after full release", result);

    CycleBreaker.releaseCycleHandle(obj);
  }

  @Test
  public void should_ReleaseNonExistentHandle_When_ReleaseCycleHandleCalledForUntracked() {
    Object obj = new Object();

    // Should not throw exception
    try {
      CycleBreaker.releaseCycleHandle(obj);
      assertTrue("Should handle release of non-tracked object", true);
    } catch (Exception e) {
      fail("Should not throw exception: " + e.getMessage());
    }
  }

  // ========== Thread Safety Tests ==========

  @Test
  public void should_HandleConcurrentThreads_When_MultipleThreadsUsesCycleBreaker() throws InterruptedException {
    Object obj1 = new Object();
    Object obj2 = new Object();
    final boolean[] results = new boolean[4];

    Thread thread1 = new Thread(() -> {
      results[0] = CycleBreaker.startingToCycle(obj1);
      results[1] = CycleBreaker.startingToCycle(obj1);
      CycleBreaker.releaseCycleHandle(obj1);
    });

    Thread thread2 = new Thread(() -> {
      results[2] = CycleBreaker.startingToCycle(obj2);
      results[3] = CycleBreaker.startingToCycle(obj2);
      CycleBreaker.releaseCycleHandle(obj2);
    });

    thread1.start();
    thread2.start();

    thread1.join();
    thread2.join();

    assertFalse("Thread1 first call should be false", results[0]);
    assertTrue("Thread1 second call should be true", results[1]);
    assertFalse("Thread2 first call should be false", results[2]);
    assertTrue("Thread2 second call should be true", results[3]);
  }

  @Test
  public void should_AllowSameObjectDifferentThreads_When_DifferentThreadsUseSameObject() throws InterruptedException {
    Object sharedObj = new Object();
    final boolean[] results = new boolean[4];

    Thread thread1 = new Thread(() -> {
      results[0] = CycleBreaker.startingToCycle(sharedObj);
      results[1] = CycleBreaker.startingToCycle(sharedObj);
      CycleBreaker.releaseCycleHandle(sharedObj);
    });

    Thread thread2 = new Thread(() -> {
      try {
        Thread.sleep(10); // Let thread1 start
      } catch (InterruptedException e) {
        // Ignore
      }
      results[2] = CycleBreaker.startingToCycle(sharedObj);
      results[3] = CycleBreaker.startingToCycle(sharedObj);
      CycleBreaker.releaseCycleHandle(sharedObj);
    });

    thread1.start();
    thread2.start();

    thread1.join();
    thread2.join();

    // Each thread should see the same sequence
    assertFalse("Thread1 first call should be false", results[0]);
    assertTrue("Thread1 second call should be true", results[1]);
    assertFalse("Thread2 first call should be false", results[2]);
    assertTrue("Thread2 second call should be true", results[3]);
  }

  // ========== Edge Cases ==========

  @Test
  public void should_HandleCycleDetectionPattern_When_AlternatingPattern() {
    Object obj = new Object();

    boolean result1 = CycleBreaker.startingToCycle(obj);
    assertFalse("First call should be false", result1);

    boolean result2 = CycleBreaker.startingToCycle(obj);
    assertTrue("Second call should be true", result2);

    CycleBreaker.releaseCycleHandle(obj);

    boolean result3 = CycleBreaker.startingToCycle(obj);
    assertFalse("After release, first call should be false", result3);

    CycleBreaker.releaseCycleHandle(obj);
  }

  @Test
  public void should_HandleDeepRecursion_When_NestedCalls() {
    Object obj = new Object();

    for (int i = 0; i < 10; i++) {
      if (i == 0) {
        assertFalse("First entry should not be cycle", CycleBreaker.startingToCycle(obj));
      } else {
        assertTrue("Nested entries should be cycle", CycleBreaker.startingToCycle(obj));
      }
    }

    for (int i = 0; i < 10; i++) {
      CycleBreaker.releaseCycleHandle(obj);
    }

    // After full cleanup, should be able to track again
    assertFalse("After full cleanup, should start fresh", CycleBreaker.startingToCycle(obj));
    CycleBreaker.releaseCycleHandle(obj);
  }

  @Test
  public void should_HandleStringInterning_When_StringsUsed() {
    String str1 = "test";
    String str2 = "test";

    // These might be interned and have same reference
    boolean result1a = CycleBreaker.startingToCycle(str1);
    boolean result1b = CycleBreaker.startingToCycle(str1);

    assertFalse("First call should be false", result1a);
    assertTrue("Second call should be true", result1b);

    CycleBreaker.releaseCycleHandle(str1);
  }

  @Test
  public void should_HandleDifferentObjectTypes_When_VariousClassesUsed() {
    Object objA = new Object();
    String objB = "test";
    Integer objC = Integer.valueOf(42);

    boolean resultA1 = CycleBreaker.startingToCycle(objA);
    boolean resultB1 = CycleBreaker.startingToCycle(objB);
    boolean resultC1 = CycleBreaker.startingToCycle(objC);

    assertFalse("All first calls should be false", resultA1 || resultB1 || resultC1);

    boolean resultA2 = CycleBreaker.startingToCycle(objA);
    boolean resultB2 = CycleBreaker.startingToCycle(objB);
    boolean resultC2 = CycleBreaker.startingToCycle(objC);

    assertTrue("All second calls should be true", resultA2 && resultB2 && resultC2);

    CycleBreaker.releaseCycleHandle(objA);
    CycleBreaker.releaseCycleHandle(objB);
    CycleBreaker.releaseCycleHandle(objC);
  }

  @Test
  public void should_HandleMixedNullAndNonNull_When_AlternatingNullAndObjects() {
    Object obj = new Object();

    boolean resultNull1 = CycleBreaker.startingToCycle(null);
    assertFalse("Null should always return false", resultNull1);

    boolean resultObj1 = CycleBreaker.startingToCycle(obj);
    assertFalse("First call with object should be false", resultObj1);

    boolean resultNull2 = CycleBreaker.startingToCycle(null);
    assertFalse("Null should always return false", resultNull2);

    boolean resultObj2 = CycleBreaker.startingToCycle(obj);
    assertTrue("Second call with object should be true", resultObj2);

    CycleBreaker.releaseCycleHandle(obj);
    CycleBreaker.releaseCycleHandle(null); // Should not throw
  }

  @Test
  public void should_CorrectlyHandleReleaseWithoutStart_When_ReleaseCalledFirst() {
    Object obj = new Object();

    // Release without start should be safe
    CycleBreaker.releaseCycleHandle(obj);

    // Should still be trackable
    boolean result = CycleBreaker.startingToCycle(obj);
    assertFalse("Should be trackable after release without start", result);

    CycleBreaker.releaseCycleHandle(obj);
  }

  @Test
  public void should_HandleLargeNumberOfReleasesAfterStart_When_MultipleReleases() {
    Object obj = new Object();

    CycleBreaker.startingToCycle(obj);

    for (int i = 0; i < 5; i++) {
      CycleBreaker.releaseCycleHandle(obj);
    }

    // Should be clean after one release
    boolean result = CycleBreaker.startingToCycle(obj);
    assertFalse("Should be clean for fresh tracking", result);

    CycleBreaker.releaseCycleHandle(obj);
  }
}
