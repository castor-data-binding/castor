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
package utf.org.castor.cache.hashbelt.container;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.castor.cache.hashbelt.container.Container;
import org.castor.cache.hashbelt.container.FastIteratingContainer;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * @author <a href="mailto:ralf DOT joachim AT syscon-world DOT de">Ralf Joachim</a>
 * @version $Revision$ $Date$
 * @since 1.0
 */
public final class TestFastIteratingContainer extends TestCase {
    public static Test suite() {
        TestSuite suite = new TestSuite("FastIteratingContainer Tests");

        suite.addTest(new TestFastIteratingContainer("testConstructor"));
        suite.addTest(new TestFastIteratingContainer("testTimestamp"));

        suite.addTest(new TestFastIteratingContainer("testBasics"));

        suite.addTest(new TestFastIteratingContainer("testContainsKey"));
        suite.addTest(new TestFastIteratingContainer("testContainsValue"));
        suite.addTest(new TestFastIteratingContainer("testClear"));

        suite.addTest(new TestFastIteratingContainer("testSize"));
        suite.addTest(new TestFastIteratingContainer("testIsEmpty"));
        suite.addTest(new TestFastIteratingContainer("testGet"));
        suite.addTest(new TestFastIteratingContainer("testPut"));
        suite.addTest(new TestFastIteratingContainer("testRemove"));
        suite.addTest(new TestFastIteratingContainer("testPutAll"));
        
        suite.addTest(new TestFastIteratingContainer("testKeySet"));
        suite.addTest(new TestFastIteratingContainer("testValues"));
        suite.addTest(new TestFastIteratingContainer("testEntrySet"));

        suite.addTest(new TestFastIteratingContainer("testKeyIterator"));
        suite.addTest(new TestFastIteratingContainer("testValueIterator"));

        return suite;
    }

    public TestFastIteratingContainer(final String name) { super(name); }
    
    public void testConstructor() {
        Object container = new FastIteratingContainer();
        assertTrue(container instanceof Container);
        assertTrue(container instanceof FastIteratingContainer);
    }
    
    public void testTimestamp() {
        Container container = new FastIteratingContainer();
        assertEquals(0L, container.getTimestamp());
        
        long before = System.currentTimeMillis();
        container.updateTimestamp();
        long after = System.currentTimeMillis();
        assertTrue(before <= container.getTimestamp());
        assertTrue(after >= container.getTimestamp());
    }
    
    public void testBasics() {
        Container container = new FastIteratingContainer();

        assertFalse(container.containsKey("first key"));
        assertFalse(container.containsKey("second key"));

        assertNull(container.put("first key", "first value"));

        assertTrue(container.containsKey("first key"));
        assertFalse(container.containsKey("second key"));

        assertNull(container.put("second key", "second value"));

        assertTrue(container.containsKey("first key"));
        assertTrue(container.containsKey("second key"));
    }

    private Container initialize() {
        Container container = new FastIteratingContainer();

        assertNull(container.put("first key", "first value"));
        assertNull(container.put("second key", "second value"));
        assertNull(container.put("third key", "third value"));
        
        return container;
    }
    
    public void testContainsKey() {
        Container container = initialize();

        assertTrue(container.containsKey("first key"));
        assertTrue(container.containsKey("second key"));
        assertTrue(container.containsKey("third key"));
        assertFalse(container.containsKey("fourth key"));
        assertFalse(container.containsKey("fifth key"));
    }

    public void testContainsValue() {
        Container container = initialize();

        assertTrue(container.containsValue("first value"));
        assertTrue(container.containsValue("second value"));
        assertTrue(container.containsValue("third value"));
        assertFalse(container.containsValue("fourth value"));
        assertFalse(container.containsValue("fifth value"));
    }

    public void testClear() {
        Container container = initialize();

        container.clear();

        assertFalse(container.containsKey("first key"));
        assertFalse(container.containsKey("second key"));
        assertFalse(container.containsKey("third key"));
        assertFalse(container.containsKey("fourth key"));
        assertFalse(container.containsKey("fifth key"));
    }

    public void testSize() {
        Container container = initialize();

        assertEquals(3, container.size());
        container.clear();
        assertEquals(0, container.size());
    }

    public void testIsEmpty() {
        Container container = initialize();

        assertFalse(container.isEmpty());
        container.clear();
        assertTrue(container.isEmpty());
    }

    public void testGet() {
        Container container = initialize();

        assertEquals("first value", container.get("first key"));
        assertEquals("second value", container.get("second key"));
        assertEquals("third value", container.get("third key"));
        assertNull(container.get("fourth key"));
        assertNull(container.get("fifth key"));
    }

    public void testPut() {
        Container container = initialize();

        assertEquals("third value", container.put("third key", "alternate third value"));
        assertNull(container.put("fourth key", "forth value"));

        assertTrue(container.containsKey("first key"));
        assertTrue(container.containsKey("second key"));
        assertTrue(container.containsKey("third key"));
        assertTrue(container.containsKey("fourth key"));
        assertFalse(container.containsKey("fifth key"));
    }

    public void testRemove() {
        Container container = initialize();

        assertEquals("third value", container.remove("third key"));

        assertTrue(container.containsKey("first key"));
        assertTrue(container.containsKey("second key"));
        assertFalse(container.containsKey("third key"));
        assertFalse(container.containsKey("fourth key"));
        assertFalse(container.containsKey("fifth key"));
    }

    public void testPutAll() {
        Container container = initialize();

        HashMap map = new HashMap();
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
        Container container = initialize();

        Set set = container.keySet();
        
        assertEquals(3, set.size());
        assertTrue(set.contains("first key"));
        assertTrue(set.contains("second key"));
        assertTrue(set.contains("third key"));
    }

    public void testValues() {
        Container container = initialize();

        Collection col = container.values();
        
        assertEquals(3, col.size());
        assertTrue(col.contains("first value"));
        assertTrue(col.contains("second value"));
        assertTrue(col.contains("third value"));
    }

    public void testEntrySet() {
        Container container = initialize();

        Set set = container.entrySet();
        
        assertEquals(3, set.size());
        
        Object[] objs = set.toArray();
        HashMap map = new HashMap();
        for (int i = 0; i < 3; i++) {
            assertTrue(objs[i] instanceof Map.Entry);
            Map.Entry entry = (Map.Entry) objs[i];
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
        Container container = new FastIteratingContainer();
        Iterator iter = container.keyIterator();
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
        Container container = new FastIteratingContainer();
        Iterator iter = container.valueIterator();
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
}
