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
package org.castor.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.castor.core.util.IdentityMap;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Run tests of the org.castor.util.IdentityMap.
 * 
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision$ $Date: 2006-04-29 04:11:14 -0600 (Sat, 29 Apr 2006) $
 */
public final class TestIdentityMap extends TestCase {
    public static Test suite() {
        TestSuite suite = new TestSuite("IdentityMap Tests");

        suite.addTest(new TestIdentityMap("testConstructor"));
        suite.addTest(new TestIdentityMap("testPut"));
        suite.addTest(new TestIdentityMap("testClear"));
        suite.addTest(new TestIdentityMap("testContainsKey"));
        suite.addTest(new TestIdentityMap("testGet"));
        suite.addTest(new TestIdentityMap("testRemove"));
        suite.addTest(new TestIdentityMap("testKeySet"));
        suite.addTest(new TestIdentityMap("testEntrySet"));
        suite.addTest(new TestIdentityMap("testRehash"));
        
        return suite;
    }

    public TestIdentityMap(final String name) { super(name); }

    protected void setUp() { }

    protected void tearDown() { }
    
    public void testConstructor() {
        IdentityMap map = new IdentityMap();
        assertTrue(map.isEmpty());
        assertTrue(map.size() == 0);
    }
    
    public void testPut() {
        IdentityMap map = new IdentityMap();
        Object key = new Integer(123);
        Object original = "original value";
        Object replaced = "replaced value";
        Object result;
        
        result = map.put(key, original);
        assertFalse(map.isEmpty());
        assertTrue(map.size() == 1);
        assertNull(result);
        
        result = map.put(key, replaced);
        assertFalse(map.isEmpty());
        assertTrue(map.size() == 1);
        assertTrue(result == original);
        
        result = map.put(new Integer(123), "value");
        assertFalse(map.isEmpty());
        assertTrue(map.size() == 2);
        assertNull(result);
    }

    public void testClear() {
        IdentityMap map = new IdentityMap();

        map.put(new Integer(123), "value");
        assertFalse(map.isEmpty());
        assertTrue(map.size() == 1);
        
        map.clear();
        assertTrue(map.isEmpty());
        assertTrue(map.size() == 0);
    }

    public void testContainsKey() {
        IdentityMap map = new IdentityMap();
        Object key1 = new Integer(123);
        Object key2 = new Integer(123);

        assertFalse(map.containsKey(key1));
        assertFalse(map.containsKey(key2));
        
        map.put(key1, "original value");
        assertTrue(map.containsKey(key1));
        assertFalse(map.containsKey(key2));
        
        map.put(key1, "replaced value");
        assertTrue(map.containsKey(key1));
        assertFalse(map.containsKey(key2));

        map.put(key2, "value");
        assertTrue(map.containsKey(key1));
        assertTrue(map.containsKey(key2));
    }

    public void testGet() {
        IdentityMap map = new IdentityMap();
        Object key1 = new Integer(123);
        Object original = "original value";
        Object replaced = "replaced value";
        Object key2 = new Integer(123);
        Object value = "value";

        assertNull(map.get(key1));
        assertNull(map.get(key2));
        
        map.put(key1, original);
        assertTrue(map.get(key1) == original);
        assertNull(map.get(key2));
        
        map.put(key1, replaced);
        assertTrue(map.get(key1) == replaced);
        assertNull(map.get(key2));

        map.put(key2, value);
        assertTrue(map.get(key1) == replaced);
        assertTrue(map.get(key2) == value);
    }

    public void testRemove() {
        IdentityMap map = new IdentityMap();
        Object key1 = new Integer(123);
        Object key2 = new Integer(123);
        Object key3 = new Integer(123);

        map.put(key1, "value 1");
        map.put(key2, "value 2");
        map.put(key3, "value 3");
        assertTrue(map.size() == 3);
        assertTrue(map.containsKey(key1));
        assertTrue(map.containsKey(key2));
        assertTrue(map.containsKey(key3));
        
        map.remove(key2);
        assertTrue(map.size() == 2);
        assertTrue(map.containsKey(key1));
        assertFalse(map.containsKey(key2));
        assertTrue(map.containsKey(key3));
        
        map.remove(key3);
        assertTrue(map.size() == 1);
        assertTrue(map.containsKey(key1));
        assertFalse(map.containsKey(key2));
        assertFalse(map.containsKey(key3));
        
        map.remove(key1);
        assertTrue(map.size() == 0);
        assertFalse(map.containsKey(key1));
        assertFalse(map.containsKey(key2));
        assertFalse(map.containsKey(key3));
    }

    public void testKeySet() {
        IdentityMap map = new IdentityMap();
        Object key1 = new Integer(123);
        Object key2 = new Integer(123);
        Object key3 = new Integer(123);

        map.put(key1, "value 1");
        map.put(key2, "value 2");
        map.put(key3, "value 3");
        
        Set set = map.keySet();
        assertTrue(set.size() == 3);
        
        Iterator iter = set.iterator();
        while (iter.hasNext()) {
            Object test = iter.next();
            assertTrue((test == key1) || (test == key2) || (test == key3));
        }
    }

    public void testEntrySet() {
        IdentityMap map = new IdentityMap();
        Object key1 = new Integer(123);
        Object key2 = new Integer(123);
        Object key3 = new Integer(123);

        map.put(key1, "value 1");
        map.put(key2, "value 2");
        map.put(key3, "value 3");
        
        Set set = map.entrySet();
        assertTrue(set.size() == 3);
        
        Iterator iter = set.iterator();
        while (iter.hasNext()) {
            Object test = iter.next();
            assertTrue(test instanceof Map.Entry);
            Map.Entry entry = (Map.Entry) test;
            Object key = entry.getKey();
            Object value = entry.getValue();
            assertTrue((key == key1) || (key == key2) || (key == key3));
            if (key == key1) { assertEquals(value, "value 1"); }
            if (key == key2) { assertEquals(value, "value 2"); }
            if (key == key3) { assertEquals(value, "value 3"); }
        }
    }

    public void testRehash() {
        ArrayList keys = new ArrayList();
        IdentityMap map = new IdentityMap();
        
        for (int i = 0; i < 100; i++) {
            Object key = new Integer(i);
            Object value = "value " + i;
            keys.add(key);
            map.put(key, value);
        }
        
        for (int i = 0; i < 100; i++) {
            Object key = keys.get(i);
            assertTrue(map.containsKey(key));
            Object value = "value " + i;
            assertTrue(value.equals(map.get(key)));
        }
    }
}
