/*
 * Copyright 2006 Ralf Joachim
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
package org.castor.cache.hashbelt.container;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision$ $Date$
 * @since 1.0
 */
public final class TestWeakReferenceContainer extends TestCase {
    public static Test suite() {
        TestSuite suite = new TestSuite("WeakReferenceContainer Tests");

        suite.addTest(new TestWeakReferenceContainer("testConstructor"));
        suite.addTest(new TestWeakReferenceContainer("testTimestamp"));

        suite.addTest(new TestWeakReferenceContainer("testBasics"));

        suite.addTest(new TestWeakReferenceContainer("testContainsKey"));
        suite.addTest(new TestWeakReferenceContainer("testContainsValue"));
        suite.addTest(new TestWeakReferenceContainer("testClear"));

        suite.addTest(new TestWeakReferenceContainer("testSize"));
        suite.addTest(new TestWeakReferenceContainer("testIsEmpty"));
        suite.addTest(new TestWeakReferenceContainer("testGet"));
        suite.addTest(new TestWeakReferenceContainer("testPut"));
        suite.addTest(new TestWeakReferenceContainer("testRemove"));
        suite.addTest(new TestWeakReferenceContainer("testPutAll"));
        
        suite.addTest(new TestWeakReferenceContainer("testKeySet"));
        suite.addTest(new TestWeakReferenceContainer("testValues"));
        suite.addTest(new TestWeakReferenceContainer("testEntrySet"));

        suite.addTest(new TestWeakReferenceContainer("testKeyIterator"));
        suite.addTest(new TestWeakReferenceContainer("testValueIterator"));

        suite.addTest(new TestWeakReferenceContainer("testWeakReferences"));

        return suite;
    }
    
    public TestWeakReferenceContainer(final String name) { super(name); }
    
    public void testConstructor() {
        Object container = new WeakReferenceContainer();
        assertTrue(container instanceof Container);
        assertTrue(container instanceof WeakReferenceContainer);
    }
    
    public void testTimestamp() {
        Container container = new WeakReferenceContainer();
        assertEquals(0L, container.getTimestamp());
        
        long before = System.currentTimeMillis();
        container.updateTimestamp();
        long after = System.currentTimeMillis();
        assertTrue(before <= container.getTimestamp());
        assertTrue(after >= container.getTimestamp());
    }
    
    public void testBasics() {
        Container<String, String> container = new WeakReferenceContainer<String, String>();

        assertFalse(container.containsKey("first key"));
        assertFalse(container.containsKey("second key"));

        assertNull(container.put("first key", "first value"));

        assertTrue(container.containsKey("first key"));
        assertFalse(container.containsKey("second key"));

        assertNull(container.put("second key", "second value"));

        assertTrue(container.containsKey("first key"));
        assertTrue(container.containsKey("second key"));
    }

    private Container<String, String> initialize() {
        Container<String, String> container = new WeakReferenceContainer<String, String>();

        assertNull(container.put("first key", "first value"));
        assertNull(container.put("second key", "second value"));
        assertNull(container.put("third key", "third value"));
        
        return container;
    }
    
    public void testContainsKey() {
        Container<String, String> container = initialize();

        assertTrue(container.containsKey("first key"));
        assertTrue(container.containsKey("second key"));
        assertTrue(container.containsKey("third key"));
        assertFalse(container.containsKey("fourth key"));
        assertFalse(container.containsKey("fifth key"));
    }

    public void testContainsValue() {
        Container<String, String> container = initialize();

        assertTrue(container.containsValue("first value"));
        assertTrue(container.containsValue("second value"));
        assertTrue(container.containsValue("third value"));
        assertFalse(container.containsValue("fourth value"));
        assertFalse(container.containsValue("fifth value"));
    }

    public void testClear() {
        Container<String, String> container = initialize();

        container.clear();

        assertFalse(container.containsKey("first key"));
        assertFalse(container.containsKey("second key"));
        assertFalse(container.containsKey("third key"));
        assertFalse(container.containsKey("fourth key"));
        assertFalse(container.containsKey("fifth key"));
    }

    public void testSize() {
        Container<String, String> container = initialize();

        assertEquals(3, container.size());
        container.clear();
        assertEquals(0, container.size());
    }

    public void testIsEmpty() {
        Container<String, String> container = initialize();

        assertFalse(container.isEmpty());
        container.clear();
        assertTrue(container.isEmpty());
    }

    public void testGet() {
        Container<String, String> container = initialize();

        assertEquals("first value", container.get("first key"));
        assertEquals("second value", container.get("second key"));
        assertEquals("third value", container.get("third key"));
        assertNull(container.get("fourth key"));
        assertNull(container.get("fifth key"));
    }

    public void testPut() {
        Container<String, String> container = initialize();

        assertEquals("third value", container.put("third key", "alternate third value"));
        assertNull(container.put("fourth key", "forth value"));

        assertTrue(container.containsKey("first key"));
        assertTrue(container.containsKey("second key"));
        assertTrue(container.containsKey("third key"));
        assertTrue(container.containsKey("fourth key"));
        assertFalse(container.containsKey("fifth key"));
    }

    public void testRemove() {
        Container<String, String> container = initialize();

        assertEquals("third value", container.remove("third key"));

        assertTrue(container.containsKey("first key"));
        assertTrue(container.containsKey("second key"));
        assertFalse(container.containsKey("third key"));
        assertFalse(container.containsKey("fourth key"));
        assertFalse(container.containsKey("fifth key"));
    }

    public void testPutAll() {
        Container<String, String> container = initialize();

        HashMap<String, String> map = new HashMap<String, String>();
        map.put("fourth key", "forth value");
        map.put("fifth key", "fifth value");
        
        container.putAll(map);
        
        assertTrue(container.containsKey("first key"));
        assertTrue(container.containsKey("second key"));
        assertTrue(container.containsKey("third key"));
        assertTrue(container.containsKey("fourth key"));
        assertTrue(container.containsKey("fifth key"));
    }

    public void testKeySet() {
        Container<String, String> container = initialize();

        Set<String> set = container.keySet();
        
        assertEquals(3, set.size());
        assertTrue(set.contains("first key"));
        assertTrue(set.contains("second key"));
        assertTrue(set.contains("third key"));
    }

    public void testValues() {
        Container<String, String> container = initialize();

        Collection<String> col = container.values();
        
        assertEquals(3, col.size());
        assertTrue(col.contains("first value"));
        assertTrue(col.contains("second value"));
        assertTrue(col.contains("third value"));
    }

    public void testEntrySet() {
        Container<String, String> container = initialize();

        Set<Map.Entry<String, String>> set = container.entrySet();
        
        assertEquals(3, set.size());
        
        HashMap<String, String> map = new HashMap<String, String>();
        for (Map.Entry<String, String> entry : set) {
            map.put(entry.getKey(), entry.getValue());
        }

        assertTrue(map.containsKey("first key"));
        assertEquals("first value", map.get("first key"));

        assertTrue(map.containsKey("second key"));
        assertEquals("second value", map.get("second key"));

        assertTrue(map.containsKey("third key"));
        assertEquals("third value", map.get("third key"));
    }

    public void testKeyIterator() {
        Container<Integer, String> container = new WeakReferenceContainer<Integer, String>();
        Iterator<Integer> iter = container.keyIterator();
        assertNotNull(iter);
        assertFalse(iter.hasNext());
        
        for (int i = 0; i < 10; i++) {
            container.put(new Integer(i), Integer.toString(i));
        }
        
        iter = container.keyIterator();
        assertNotNull(iter);
        for (int i = 0; i < 10; i++) {
            assertTrue(iter.hasNext());
            Object obj = iter.next();
            assertTrue(obj instanceof Integer);
            assertTrue(((Integer) obj).intValue() >= 0);
            assertTrue(((Integer) obj).intValue() < 10);
        }
    }
    
    public void testValueIterator() {
        Container<String, Integer> container = new WeakReferenceContainer<String, Integer>();
        Iterator<Integer> iter = container.valueIterator();
        assertNotNull(iter);
        assertFalse(iter.hasNext());
        
        for (int i = 0; i < 10; i++) {
            container.put(Integer.toString(i), new Integer(i));
        }
        
        iter = container.valueIterator();
        assertNotNull(iter);
        for (int i = 0; i < 10; i++) {
            assertTrue(iter.hasNext());
            Object obj = iter.next();
            assertTrue(obj instanceof Integer);
            assertTrue(((Integer) obj).intValue() >= 0);
            assertTrue(((Integer) obj).intValue() < 10);
        }
    }

    public void testWeakReferences() {
        Container<String, Integer> container = new WeakReferenceContainer<String, Integer>();
        for (int i = 0; i < 10; i++) {
            container.put(Integer.toString(i), new Integer(i));
        }

        assertTrue(container.containsKey("1"));
        assertTrue(container.containsValue(new Integer(1)));
        
        assertTrue(container.containsKey("2"));
        assertTrue(container.containsValue(new Integer(2)));
        
        assertTrue(container.containsKey("3"));
        assertTrue(container.containsValue(new Integer(3)));
        
        // keep strong reference to second value
        Object second = container.get("2");

        // run garbage collector
        Runtime.getRuntime().gc();
        Runtime.getRuntime().runFinalization();
        Runtime.getRuntime().gc();

        assertTrue(container.containsKey("1"));
        assertTrue(container.containsKey("2"));
        assertTrue(container.containsKey("3"));

        assertFalse(container.containsValue(new Integer(1)));
        assertTrue(container.containsValue(new Integer(2)));
        assertFalse(container.containsValue(new Integer(3)));

        assertFalse(container.containsKey("1"));
        assertTrue(container.containsKey("2"));
        assertFalse(container.containsKey("3"));

        assertTrue(second == container.get("2"));
    }
}
