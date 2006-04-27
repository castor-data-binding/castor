/*
 * Copyright 2005 Ralf Joachim
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package utf.org.castor.util;

import java.util.ArrayList;
import java.util.Iterator;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.castor.util.IdentitySet;

/**
 * Run tests of the org.castor.util.IdentitySet.
 * 
 * @author <a href="mailto:ralf DOT joachim AT syscon-world DOT de">Ralf Joachim</a>
 * @version $Revision$ $Date$
 */
public final class TestIdentitySet extends TestCase {
    public static Test suite() {
        TestSuite suite = new TestSuite("IdentitySet Tests");

        suite.addTest(new TestIdentitySet("testConstructor"));
        suite.addTest(new TestIdentitySet("testAdd"));
        suite.addTest(new TestIdentitySet("testClear"));
        suite.addTest(new TestIdentitySet("testContains"));
        suite.addTest(new TestIdentitySet("testRemove"));
        suite.addTest(new TestIdentitySet("testIterator"));
        suite.addTest(new TestIdentitySet("testRehash"));
        suite.addTest(new TestIdentitySet("testToArray"));
        
        return suite;
    }

    public TestIdentitySet(final String name) { super(name); }

    protected void setUp() { }

    protected void tearDown() { }
    
    public void testConstructor() {
        IdentitySet set1 = new IdentitySet();
        assertTrue(set1.isEmpty());
        assertTrue(set1.size() == 0);

        IdentitySet set2 = new IdentitySet(123);
        assertTrue(set2.isEmpty());
        assertTrue(set2.size() == 0);
    }
    
    public void testAdd() {
        IdentitySet set = new IdentitySet();
        Object key = new Integer(123);
        
        assertTrue(set.add(key));
        assertFalse(set.isEmpty());
        assertTrue(set.size() == 1);
        
        assertFalse(set.add(key));
        assertFalse(set.isEmpty());
        assertTrue(set.size() == 1);
        
        assertTrue(set.add(new Integer(123)));
        assertFalse(set.isEmpty());
        assertTrue(set.size() == 2);
    }

    public void testClear() {
        IdentitySet set = new IdentitySet();

        set.add(new Integer(123));
        assertFalse(set.isEmpty());
        assertTrue(set.size() == 1);
        
        set.clear();
        assertTrue(set.isEmpty());
        assertTrue(set.size() == 0);
    }

    public void testContains() {
        IdentitySet map = new IdentitySet();
        Object key1 = new Integer(123);
        Object key2 = new Integer(123);

        assertFalse(map.contains(key1));
        assertFalse(map.contains(key2));
        
        map.add(key1);
        assertTrue(map.contains(key1));
        assertFalse(map.contains(key2));
        
        map.add(key1);
        assertTrue(map.contains(key1));
        assertFalse(map.contains(key2));

        map.add(key2);
        assertTrue(map.contains(key1));
        assertTrue(map.contains(key2));
    }

    public void testRemove() {
        IdentitySet set = new IdentitySet();
        Object key1 = new Integer(123);
        Object key2 = new Integer(123);
        Object key3 = new Integer(123);

        set.add(key1);
        set.add(key2);
        set.add(key3);
        assertTrue(set.size() == 3);
        assertTrue(set.contains(key1));
        assertTrue(set.contains(key2));
        assertTrue(set.contains(key3));
        
        set.remove(key2);
        assertTrue(set.size() == 2);
        assertTrue(set.contains(key1));
        assertFalse(set.contains(key2));
        assertTrue(set.contains(key3));
        
        set.remove(key3);
        assertTrue(set.size() == 1);
        assertTrue(set.contains(key1));
        assertFalse(set.contains(key2));
        assertFalse(set.contains(key3));
        
        set.remove(key1);
        assertTrue(set.size() == 0);
        assertFalse(set.contains(key1));
        assertFalse(set.contains(key2));
        assertFalse(set.contains(key3));
    }

    public void testIterator() {
        IdentitySet set = new IdentitySet();
        Object key1 = new Integer(123);
        Object key2 = new Integer(123);
        Object key3 = new Integer(123);

        set.add(key1);
        set.add(key2);
        set.add(key3);
        assertTrue(set.size() == 3);

        Iterator iter = set.iterator();
        while (iter.hasNext()) {
            Object test = iter.next();
            assertTrue((test == key1) || (test == key2) || (test == key3));
        }
    }

    public void testRehash() {
        ArrayList keys = new ArrayList();
        IdentitySet set = new IdentitySet();
        
        for (int i = 0; i < 100; i++) {
            Object key = new Integer(i);
            keys.add(key);
            set.add(key);
        }
        
        for (int i = 0; i < 100; i++) {
            Object key = keys.get(i);
            assertTrue(set.contains(key));
        }
    }

    public void testToArray() {
        IdentitySet set = new IdentitySet();
        Object key1 = new Integer(123);
        Object key2 = new Integer(123);
        Object key3 = new Integer(123);

        set.add(key1);
        set.add(key2);
        set.add(key3);
        assertTrue(set.size() == 3);

        Object[] oArr = set.toArray();
        if (oArr[0] == key1) {
            if (oArr[1] == key2) {
                assertTrue(oArr[2] == key3);
            } else {
                assertTrue(oArr[2] == key2);
                assertTrue(oArr[1] == key3);
            }
        } else if (oArr[0] == key2) {
            if (oArr[1] == key1) {
                assertTrue(oArr[2] == key3);
            } else {
                assertTrue(oArr[2] == key1);
                assertTrue(oArr[1] == key3);
            }
        } else {
            assertTrue(oArr[0] == key3);
            if (oArr[1] == key1) {
                assertTrue(oArr[2] == key2);
            } else {
                assertTrue(oArr[2] == key1);
                assertTrue(oArr[1] == key2);
            }
        }

        Integer[] iArr = new Integer[1];
        iArr = (Integer[]) set.toArray(iArr);
        if (iArr[0] == key1) {
            if (iArr[1] == key2) {
                assertTrue(iArr[2] == key3);
            } else {
                assertTrue(iArr[2] == key2);
                assertTrue(iArr[1] == key3);
            }
        } else if (iArr[0] == key2) {
            if (iArr[1] == key1) {
                assertTrue(iArr[2] == key3);
            } else {
                assertTrue(iArr[2] == key1);
                assertTrue(iArr[1] == key3);
            }
        } else {
            assertTrue(iArr[0] == key3);
            if (iArr[1] == key1) {
                assertTrue(iArr[2] == key2);
            } else {
                assertTrue(iArr[2] == key1);
                assertTrue(iArr[1] == key2);
            }
        }
    }
}
