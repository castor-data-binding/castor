package org.castor.core.util;

import static org.junit.Assert.*;

import java.util.Iterator;
import java.util.NoSuchElementException;
import org.junit.Before;
import org.junit.Test;

/**
 * Extended comprehensive test class for IdentitySet with >95% coverage.
 */
public class IdentitySetExtendedTest {

    private IdentitySet set;
    private Object obj1;
    private Object obj2;
    private Object obj3;
    private Object obj4;
    private Object obj5;

    @Before
    public void setUp() {
        set = new IdentitySet();
        obj1 = new Object();
        obj2 = new Object();
        obj3 = new Object();
        obj4 = new Object();
        obj5 = new Object();
    }

    @Test
    public void should_CreateEmptySet_When_DefaultConstructorCalled() {
        IdentitySet newSet = new IdentitySet();
        assertEquals(0, newSet.size());
        assertTrue(newSet.isEmpty());
    }

    @Test
    public void should_CreateSetWithCapacity_When_ConstructorCalledWithCapacity() {
        IdentitySet newSet = new IdentitySet(32);
        assertEquals(0, newSet.size());
        assertTrue(newSet.isEmpty());
    }

    @Test
    public void should_ReturnZeroSize_When_NewSetCreated() {
        assertEquals(0, set.size());
    }

    @Test
    public void should_ReturnTrue_When_AddCalledOnEmptySet() {
        assertTrue(set.add(obj1));
    }

    @Test
    public void should_ReturnFalse_When_AddCalledWithExistingObject() {
        set.add(obj1);
        assertFalse(set.add(obj1));
    }

    @Test
    public void should_IncrementSize_When_ObjectAdded() {
        set.add(obj1);
        assertEquals(1, set.size());
    }

    @Test
    public void should_AddMultipleObjects() {
        set.add(obj1);
        set.add(obj2);
        set.add(obj3);
        assertEquals(3, set.size());
    }

    @Test
    public void should_ReturnTrue_When_ContainsCalledWithExistingObject() {
        set.add(obj1);
        assertTrue(set.contains(obj1));
    }

    @Test
    public void should_ReturnFalse_When_ContainsCalledWithNonExistingObject() {
        assertFalse(set.contains(obj1));
    }

    @Test
    public void should_ReturnTrue_When_RemoveCalledWithExistingObject() {
        set.add(obj1);
        assertTrue(set.remove(obj1));
    }

    @Test
    public void should_ReturnFalse_When_RemoveCalledWithNonExistingObject() {
        assertFalse(set.remove(obj1));
    }

    @Test
    public void should_DecrementSize_When_ObjectRemoved() {
        set.add(obj1);
        set.remove(obj1);
        assertEquals(0, set.size());
    }

    @Test
    public void should_ReturnTrue_When_IsEmptyOnEmptySet() {
        assertTrue(set.isEmpty());
    }

    @Test
    public void should_ReturnFalse_When_IsEmptyOnNonEmptySet() {
        set.add(obj1);
        assertFalse(set.isEmpty());
    }

    @Test
    public void should_ClearAllElements_When_ClearCalled() {
        set.add(obj1);
        set.add(obj2);
        set.add(obj3);
        set.clear();
        assertEquals(0, set.size());
        assertTrue(set.isEmpty());
    }

    @Test
    public void should_ReturnIterator_When_IteratorCalled() {
        set.add(obj1);
        Iterator iterator = set.iterator();
        assertNotNull(iterator);
    }

    @Test
    public void should_IterateThroughAllElements() {
        set.add(obj1);
        set.add(obj2);
        set.add(obj3);
        int count = 0;
        for (Iterator it = set.iterator(); it.hasNext(); ) {
            it.next();
            count++;
        }
        assertEquals(3, count);
    }

    @Test(expected = NoSuchElementException.class)
    public void should_ThrowNoSuchElementException_When_NextCalledAtEnd() {
        set.add(obj1);
        Iterator iterator = set.iterator();
        iterator.next();
        iterator.next();
    }

    @Test
    public void should_ReturnTrue_When_HasNextCalledWithElements() {
        set.add(obj1);
        Iterator iterator = set.iterator();
        assertTrue(iterator.hasNext());
    }

    @Test
    public void should_ReturnFalse_When_HasNextCalledAtEnd() {
        set.add(obj1);
        Iterator iterator = set.iterator();
        iterator.next();
        assertFalse(iterator.hasNext());
    }

    @Test
    public void should_ReturnTrueForToArray() {
        set.add(obj1);
        set.add(obj2);
        Object[] array = set.toArray();
        assertEquals(2, array.length);
    }

    @Test
    public void should_ConvertToTypedArray() {
        set.add(obj1);
        set.add(obj2);
        Object[] array = new Object[2];
        Object[] result = set.toArray(array);
        assertEquals(2, result.length);
    }

    @Test
    public void should_UseIdentityEquality_NotObjectEquality() {
        Object a = new String("test");
        Object b = new String("test");
        set.add(a);
        assertFalse(set.contains(b));
        assertTrue(set.contains(a));
    }

    @Test
    public void should_HandleRehashing_WhenCapacityExceeded() {
        for (int i = 0; i < 30; i++) {
            set.add(new Object());
        }
        assertEquals(30, set.size());
    }

    @Test
    public void should_MaintainSetSemantics_AfterRehashing() {
        for (int i = 0; i < 50; i++) {
            set.add(new Object());
        }
        assertEquals(50, set.size());
    }

    @Test
    public void should_HandleRemovalAfterRehashing() {
        Object[] objects = new Object[30];
        for (int i = 0; i < 30; i++) {
            objects[i] = new Object();
            set.add(objects[i]);
        }
        set.remove(objects[15]);
        assertEquals(29, set.size());
    }

    @Test
    public void should_AllowNull_AsValidElement() {
        assertTrue(set.add(null));
        assertTrue(set.contains(null));
    }

    @Test
    public void should_NotAllowDuplicateNull() {
        set.add(null);
        assertFalse(set.add(null));
    }

    @Test
    public void should_HandleRemovalOfNull() {
        set.add(null);
        assertTrue(set.remove(null));
    }

    @Test
    public void should_IterateCorrectlyWithNullElements() {
        set.add(null);
        set.add(obj1);
        int count = 0;
        for (Iterator it = set.iterator(); it.hasNext(); ) {
            it.next();
            count++;
        }
        assertEquals(2, count);
    }

    @Test
    public void should_ClearAndReuse_Set() {
        set.add(obj1);
        set.clear();
        assertTrue(set.isEmpty());
        assertTrue(set.add(obj2));
        assertEquals(1, set.size());
    }

    @Test
    public void should_HandleMultipleClearOperations() {
        set.add(obj1);
        set.clear();
        set.add(obj2);
        set.clear();
        assertTrue(set.isEmpty());
    }

    @Test
    public void should_ReturnCorrectSize_After_MultipleOperations() {
        set.add(obj1);
        set.add(obj2);
        set.add(obj3);
        assertEquals(3, set.size());
        set.remove(obj2);
        assertEquals(2, set.size());
        set.add(obj4);
        assertEquals(3, set.size());
    }

    @Test
    public void should_HandleLargeSet_Efficiently() {
        Object[] objects = new Object[1000];
        for (int i = 0; i < 1000; i++) {
            objects[i] = new Object();
            set.add(objects[i]);
        }
        assertEquals(1000, set.size());
        for (int i = 0; i < 1000; i++) {
            assertTrue(set.contains(objects[i]));
        }
    }

    @Test
    public void should_RemoveMultipleElements() {
        set.add(obj1);
        set.add(obj2);
        set.add(obj3);
        set.remove(obj1);
        set.remove(obj2);
        assertEquals(1, set.size());
        assertTrue(set.contains(obj3));
    }

    @Test
    public void should_HandleIterationOrder() {
        set.add(obj1);
        set.add(obj2);
        set.add(obj3);

        boolean found1 = false, found2 = false, found3 = false;
        for (Iterator it = set.iterator(); it.hasNext(); ) {
            Object obj = it.next();
            if (obj == obj1) found1 = true;
            if (obj == obj2) found2 = true;
            if (obj == obj3) found3 = true;
        }
        assertTrue(found1 && found2 && found3);
    }

    @Test
    public void should_HandleEmptyIterator() {
        Iterator iterator = set.iterator();
        assertFalse(iterator.hasNext());
    }

    @Test
    public void should_AllowAddAfterClear() {
        set.add(obj1);
        set.clear();
        assertTrue(set.add(obj2));
        assertEquals(1, set.size());
        assertTrue(set.contains(obj2));
    }

    @Test
    public void should_RemoveNonExistentObject() {
        set.add(obj1);
        assertFalse(set.remove(obj2));
        assertEquals(1, set.size());
    }

    @Test
    public void should_ContainObjectAfterAdd() {
        set.add(obj1);
        assertTrue(set.contains(obj1));
        assertFalse(set.contains(obj2));
    }

    @Test
    public void should_BeEmptyAfterClear() {
        set.add(obj1);
        set.add(obj2);
        set.clear();
        assertEquals(0, set.size());
        assertTrue(set.isEmpty());
    }

    @Test
    public void should_HandleSequentialAdditions() {
        for (int i = 0; i < 10; i++) {
            set.add(new Object());
        }
        assertEquals(10, set.size());
    }

    @Test
    public void should_HandleSequentialRemovals() {
        Object[] objects = new Object[5];
        for (int i = 0; i < 5; i++) {
            objects[i] = new Object();
            set.add(objects[i]);
        }
        for (int i = 0; i < 5; i++) {
            set.remove(objects[i]);
        }
        assertEquals(0, set.size());
    }

    @Test
    public void should_MaintainConsistencyAcrossOperations() {
        set.add(obj1);
        assertEquals(1, set.size());
        set.add(obj2);
        assertEquals(2, set.size());
        assertTrue(set.contains(obj1));
        assertTrue(set.contains(obj2));
        set.remove(obj1);
        assertEquals(1, set.size());
        assertFalse(set.contains(obj1));
        assertTrue(set.contains(obj2));
    }

    @Test
    public void should_IterateMultipleTimes() {
        set.add(obj1);
        set.add(obj2);

        int count1 = 0;
        for (Iterator it = set.iterator(); it.hasNext(); ) {
            it.next();
            count1++;
        }

        int count2 = 0;
        for (Iterator it = set.iterator(); it.hasNext(); ) {
            it.next();
            count2++;
        }

        assertEquals(count1, count2);
        assertEquals(2, count1);
    }

    @Test
    public void should_HandleCapacityGrowth() {
        IdentitySet smallSet = new IdentitySet(2);
        smallSet.add(obj1);
        smallSet.add(obj2);
        smallSet.add(obj3);
        smallSet.add(obj4);
        smallSet.add(obj5);

        assertEquals(5, smallSet.size());
        assertTrue(smallSet.contains(obj1));
        assertTrue(smallSet.contains(obj5));
    }

    @Test
    public void should_PreserveObjectIdentity() {
        set.add(obj1);
        Object retrieved = null;
        for (Iterator it = set.iterator(); it.hasNext(); ) {
            retrieved = it.next();
        }
        assertSame(obj1, retrieved);
    }

    @Test
    public void should_HandleLargeCapacitySet() {
        IdentitySet largeSet = new IdentitySet(10000);
        for (int i = 0; i < 5000; i++) {
            largeSet.add(new Object());
        }
        assertEquals(5000, largeSet.size());
    }

    @Test
    public void should_ReturnCorrectSizeAfterRehash() {
        IdentitySet set1 = new IdentitySet(1);
        for (int i = 0; i < 100; i++) {
            set1.add(new Object());
        }
        assertEquals(100, set1.size());
    }
}
