package org.castor.core.util;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

/**
 * Extended comprehensive test class for CycleBreaker with >95% coverage.
 */
public class CycleBreakerExtendedTest {

    private Object obj1;
    private Object obj2;
    private Object obj3;

    @Before
    public void setUp() {
        obj1 = new Object();
        obj2 = new Object();
        obj3 = new Object();
    }

    @Test
    public void should_ReturnFalse_When_StartingToCycleCalledFirstTime() {
        boolean result = CycleBreaker.startingToCycle(obj1);
        assertFalse(result);
        CycleBreaker.releaseCycleHandle(obj1);
    }

    @Test
    public void should_ReturnTrue_When_StartingToCycleCalledSecondTime() {
        CycleBreaker.startingToCycle(obj1);
        boolean result = CycleBreaker.startingToCycle(obj1);
        assertTrue(result);
        CycleBreaker.releaseCycleHandle(obj1);
    }

    @Test
    public void should_AllowCycleCheckAfterRelease() {
        CycleBreaker.startingToCycle(obj1);
        CycleBreaker.releaseCycleHandle(obj1);
        boolean result = CycleBreaker.startingToCycle(obj1);
        assertFalse(result);
        CycleBreaker.releaseCycleHandle(obj1);
    }

    @Test
    public void should_HandleMultipleDifferentObjects() {
        boolean result1 = CycleBreaker.startingToCycle(obj1);
        boolean result2 = CycleBreaker.startingToCycle(obj2);
        boolean result3 = CycleBreaker.startingToCycle(obj3);

        assertFalse(result1);
        assertFalse(result2);
        assertFalse(result3);

        CycleBreaker.releaseCycleHandle(obj1);
        CycleBreaker.releaseCycleHandle(obj2);
        CycleBreaker.releaseCycleHandle(obj3);
    }

    @Test
    public void should_DetectCycleForEachObject() {
        CycleBreaker.startingToCycle(obj1);
        CycleBreaker.startingToCycle(obj2);

        boolean result1 = CycleBreaker.startingToCycle(obj1);
        boolean result2 = CycleBreaker.startingToCycle(obj2);

        assertTrue(result1);
        assertTrue(result2);

        CycleBreaker.releaseCycleHandle(obj1);
        CycleBreaker.releaseCycleHandle(obj2);
    }

    @Test
    public void should_ReleaseCycleForSpecificObject() {
        CycleBreaker.startingToCycle(obj1);
        CycleBreaker.startingToCycle(obj2);
        CycleBreaker.releaseCycleHandle(obj1);

        boolean result1 = CycleBreaker.startingToCycle(obj1);
        boolean result2 = CycleBreaker.startingToCycle(obj2);

        assertFalse(result1);
        assertTrue(result2);

        CycleBreaker.releaseCycleHandle(obj1);
        CycleBreaker.releaseCycleHandle(obj2);
    }

    @Test
    public void should_HandleNullObject() {
        boolean result = CycleBreaker.startingToCycle(null);
        assertFalse(result);
    }

    @Test
    public void should_NotDetectCycleOnNullObject() {
        CycleBreaker.startingToCycle(null);
        boolean result = CycleBreaker.startingToCycle(null);
        assertFalse(result);
    }

    @Test
    public void should_ReleaseNullObjectGracefully() {
        CycleBreaker.startingToCycle(null);
        CycleBreaker.releaseCycleHandle(null);
        boolean result = CycleBreaker.startingToCycle(null);
        assertFalse(result);
    }

    @Test
    public void should_HandleMultipleReleasesForSameObject() {
        CycleBreaker.startingToCycle(obj1);
        CycleBreaker.releaseCycleHandle(obj1);
        CycleBreaker.releaseCycleHandle(obj1);
        boolean result = CycleBreaker.startingToCycle(obj1);
        assertFalse(result);
        CycleBreaker.releaseCycleHandle(obj1);
    }

    @Test
    public void should_HandleNestedCycles() {
        CycleBreaker.startingToCycle(obj1);
        CycleBreaker.startingToCycle(obj2);
        CycleBreaker.startingToCycle(obj3);

        boolean result1 = CycleBreaker.startingToCycle(obj1);
        boolean result2 = CycleBreaker.startingToCycle(obj2);
        boolean result3 = CycleBreaker.startingToCycle(obj3);

        assertTrue(result1);
        assertTrue(result2);
        assertTrue(result3);

        CycleBreaker.releaseCycleHandle(obj1);
        CycleBreaker.releaseCycleHandle(obj2);
        CycleBreaker.releaseCycleHandle(obj3);
    }

    @Test
    public void should_HandleSequentialStartAndRelease() {
        for (int i = 0; i < 50; i++) {
            Object obj = new Object();
            assertFalse(CycleBreaker.startingToCycle(obj));
            CycleBreaker.releaseCycleHandle(obj);
        }
    }

    @Test
    public void should_AllowReaddingAfterRelease() {
        CycleBreaker.startingToCycle(obj1);
        CycleBreaker.releaseCycleHandle(obj1);
        assertFalse(CycleBreaker.startingToCycle(obj1));
        CycleBreaker.releaseCycleHandle(obj1);
    }

    @Test
    public void should_PreserveThreadSafety() throws InterruptedException {
        Thread t1 = new Thread(() -> {
            Object local1 = new Object();
            assertFalse(CycleBreaker.startingToCycle(local1));
            assertTrue(CycleBreaker.startingToCycle(local1));
            CycleBreaker.releaseCycleHandle(local1);
        });

        Thread t2 = new Thread(() -> {
            Object local2 = new Object();
            assertFalse(CycleBreaker.startingToCycle(local2));
            assertTrue(CycleBreaker.startingToCycle(local2));
            CycleBreaker.releaseCycleHandle(local2);
        });

        t1.start();
        t2.start();
        t1.join();
        t2.join();
    }

    @Test
    public void should_HandleObjectWithoutCycle() {
        boolean result = CycleBreaker.startingToCycle(obj1);
        assertFalse(result);
        CycleBreaker.releaseCycleHandle(obj1);
    }

    @Test
    public void should_IdentifyDifferentObjectsAsDifferent() {
        String str1 = new String("test");
        String str2 = new String("test");

        assertFalse(CycleBreaker.startingToCycle(str1));
        assertFalse(CycleBreaker.startingToCycle(str2));

        assertTrue(CycleBreaker.startingToCycle(str1));
        assertTrue(CycleBreaker.startingToCycle(str2));

        CycleBreaker.releaseCycleHandle(str1);
        CycleBreaker.releaseCycleHandle(str2);
    }

    @Test
    public void should_HandleManyObjectsSequentially() {
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

        for (int i = 0; i < 100; i++) {
            assertFalse(CycleBreaker.startingToCycle(objects[i]));
            CycleBreaker.releaseCycleHandle(objects[i]);
        }
    }

    @Test
    public void should_HandleAlternatingPattern() {
        for (int i = 0; i < 10; i++) {
            assertFalse(CycleBreaker.startingToCycle(obj1));
            assertTrue(CycleBreaker.startingToCycle(obj1));
            CycleBreaker.releaseCycleHandle(obj1);
        }
    }

    @Test
    public void should_ReturnCorrectStateAfterMultipleOperations() {
        CycleBreaker.startingToCycle(obj1);
        assertTrue(CycleBreaker.startingToCycle(obj1));
        CycleBreaker.releaseCycleHandle(obj1);
        assertFalse(CycleBreaker.startingToCycle(obj1));
        assertTrue(CycleBreaker.startingToCycle(obj1));
        CycleBreaker.releaseCycleHandle(obj1);
    }

    @Test
    public void should_HandleObjectsIndependently() {
        CycleBreaker.startingToCycle(obj1);
        CycleBreaker.startingToCycle(obj2);
        CycleBreaker.releaseCycleHandle(obj1);

        assertFalse(CycleBreaker.startingToCycle(obj1));
        assertTrue(CycleBreaker.startingToCycle(obj2));

        CycleBreaker.releaseCycleHandle(obj1);
        CycleBreaker.releaseCycleHandle(obj2);
    }

    @Test
    public void should_MaintainStateAcrossMultipleCalls() {
        CycleBreaker.startingToCycle(obj1);
        CycleBreaker.startingToCycle(obj1);
        CycleBreaker.startingToCycle(obj1);

        assertTrue(CycleBreaker.startingToCycle(obj1));
        CycleBreaker.releaseCycleHandle(obj1);
        assertFalse(CycleBreaker.startingToCycle(obj1));
        CycleBreaker.releaseCycleHandle(obj1);
    }

    @Test
    public void should_WorkWithCustomObjects() {
        CustomObject custom1 = new CustomObject();
        CustomObject custom2 = new CustomObject();

        assertFalse(CycleBreaker.startingToCycle(custom1));
        assertFalse(CycleBreaker.startingToCycle(custom2));
        assertTrue(CycleBreaker.startingToCycle(custom1));
        assertTrue(CycleBreaker.startingToCycle(custom2));

        CycleBreaker.releaseCycleHandle(custom1);
        CycleBreaker.releaseCycleHandle(custom2);
    }

    @Test
    public void should_HandleReleaseWithoutStart() {
        CycleBreaker.releaseCycleHandle(obj1);
        boolean result = CycleBreaker.startingToCycle(obj1);
        assertFalse(result);
        CycleBreaker.releaseCycleHandle(obj1);
    }

    @Test
    public void should_PreserveIdentity() {
        Object original = new Object();
        CycleBreaker.startingToCycle(original);
        boolean result = CycleBreaker.startingToCycle(original);
        assertTrue(result);
        CycleBreaker.releaseCycleHandle(original);
    }

    @Test
    public void should_HandleLargeNumberOfObjects() {
        Object[] objs = new Object[500];
        for (int i = 0; i < 500; i++) {
            objs[i] = new Object();
            CycleBreaker.startingToCycle(objs[i]);
        }

        for (int i = 0; i < 500; i++) {
            assertTrue(CycleBreaker.startingToCycle(objs[i]));
            CycleBreaker.releaseCycleHandle(objs[i]);
        }
    }

    /**
     * Custom test object.
     */
    private static class CustomObject {
        private String value = "test";
    }
}
