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
package org.castor.cache.simple;

import java.util.Collection;
import java.util.Map;
import java.util.HashMap;
import java.util.Properties;
import java.util.Set;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.castor.cache.Cache;
import org.castor.cache.CacheAcquireException;
import org.castor.cache.simple.CountLimited;

/**
 * @author <a href="mailto:ralf DOT joachim AT syscon-world DOT de">Ralf Joachim</a>
 * @version $Revision$ $Date$
 * @since 1.0
 */
public final class TestCountLimited extends TestCase {
    public static Test suite() {
        TestSuite suite = new TestSuite("CountLimited Tests");

        suite.addTest(new TestCountLimited("testBasics"));

        suite.addTest(new TestCountLimited("testContainsKey"));
        suite.addTest(new TestCountLimited("testContainsValue"));
        suite.addTest(new TestCountLimited("testClear"));

        suite.addTest(new TestCountLimited("testSize"));
        suite.addTest(new TestCountLimited("testIsEmpty"));
        suite.addTest(new TestCountLimited("testGet"));
        suite.addTest(new TestCountLimited("testPut"));
        suite.addTest(new TestCountLimited("testRemove"));
        suite.addTest(new TestCountLimited("testPutAll"));
        
        suite.addTest(new TestCountLimited("testKeySet"));
        suite.addTest(new TestCountLimited("testValues"));
        suite.addTest(new TestCountLimited("testEntrySet"));

        suite.addTest(new TestCountLimited("testExpire"));
        
        return suite;
    }

    public TestCountLimited(final String name) { super(name); }
    
    public void testBasics() throws CacheAcquireException {
        assertEquals("count-limited", CountLimited.TYPE);
        assertEquals("capacity", CountLimited.PARAM_CAPACITY);
        assertEquals(30, CountLimited.DEFAULT_CAPACITY);

        Cache cache = new CountLimited();
        assertTrue(cache instanceof CountLimited);

        assertEquals("count-limited", cache.getType());
        assertEquals(30, ((CountLimited) cache).getCapacity());
        assertEquals(Cache.DEFAULT_NAME, cache.getName());
        
        Properties params = new Properties();
        params.put(Cache.PARAM_NAME, "dummy1");
        cache.initialize(params);
        assertEquals(30, ((CountLimited) cache).getCapacity());
        assertEquals("dummy1", cache.getName());
        
        params.clear();
        params.put(Cache.PARAM_NAME, "dummy2");
        params.put(CountLimited.PARAM_CAPACITY, "-10");
        cache.initialize(params);
        assertEquals(30, ((CountLimited) cache).getCapacity());
        assertEquals("dummy2", cache.getName());
        
        params.clear();
        params.put(Cache.PARAM_NAME, "dummy3");
        params.put(CountLimited.PARAM_CAPACITY, "0");
        cache.initialize(params);
        assertEquals(30, ((CountLimited) cache).getCapacity());
        assertEquals("dummy3", cache.getName());
        
        params.clear();
        params.put(Cache.PARAM_NAME, "dummy4");
        params.put(CountLimited.PARAM_CAPACITY, "10");
        cache.initialize(params);
        assertEquals(10, ((CountLimited) cache).getCapacity());
        assertEquals("dummy4", cache.getName());
        
        assertFalse(cache.containsKey("first key"));
        assertFalse(cache.containsKey("second key"));

        assertNull(cache.put("first key", "first value"));

        assertTrue(cache.containsKey("first key"));
        assertFalse(cache.containsKey("second key"));

        assertNull(cache.put("second key", "second value"));

        assertTrue(cache.containsKey("first key"));
        assertTrue(cache.containsKey("second key"));
    }

    private Cache initialize() {
        Cache cache = new CountLimited();

        try {
            Properties params = new Properties();
            params.put(Cache.PARAM_NAME, "dummy");
            params.put(CountLimited.PARAM_CAPACITY, new Integer(10));
            cache.initialize(params);
        } catch (CacheAcquireException ex) {
            fail("Unexpected CacheAcquireException at initialization.");
        }
        
        assertNull(cache.put("first key", "first value"));
        assertNull(cache.put("second key", "second value"));
        assertNull(cache.put("third key", "third value"));
        
        return cache;
    }
    
    public void testContainsKey() {
        Cache cache = initialize();

        assertTrue(cache.containsKey("first key"));
        assertTrue(cache.containsKey("second key"));
        assertTrue(cache.containsKey("third key"));
        assertFalse(cache.containsKey("fourth key"));
        assertFalse(cache.containsKey("fifth key"));
    }

    public void testContainsValue() {
        Cache cache = initialize();

        assertTrue(cache.containsValue("first value"));
        assertTrue(cache.containsValue("second value"));
        assertTrue(cache.containsValue("third value"));
        assertFalse(cache.containsValue("fourth value"));
        assertFalse(cache.containsValue("fifth value"));
    }

    public void testClear() {
        Cache cache = initialize();

        cache.clear();

        assertFalse(cache.containsKey("first key"));
        assertFalse(cache.containsKey("second key"));
        assertFalse(cache.containsKey("third key"));
        assertFalse(cache.containsKey("fourth key"));
        assertFalse(cache.containsKey("fifth key"));
    }

    public void testSize() {
        Cache cache = initialize();

        assertEquals(3, cache.size());
        cache.clear();
        assertEquals(0, cache.size());
    }

    public void testIsEmpty() {
        Cache cache = initialize();

        assertFalse(cache.isEmpty());
        cache.clear();
        assertTrue(cache.isEmpty());
    }

    public void testGet() {
        Cache cache = initialize();

        assertEquals("first value", cache.get("first key"));
        assertEquals("second value", cache.get("second key"));
        assertEquals("third value", cache.get("third key"));
        assertNull(cache.get("fourth key"));
        assertNull(cache.get("fifth key"));
    }

    public void testPut() {
        Cache cache = initialize();

        assertEquals("third value", cache.put("third key", "alternate third value"));
        assertNull(cache.put("fourth key", "forth value"));

        assertTrue(cache.containsKey("first key"));
        assertTrue(cache.containsKey("second key"));
        assertTrue(cache.containsKey("third key"));
        assertTrue(cache.containsKey("fourth key"));
        assertFalse(cache.containsKey("fifth key"));
    }

    public void testRemove() {
        Cache cache = initialize();

        assertEquals("third value", cache.remove("third key"));

        assertTrue(cache.containsKey("first key"));
        assertTrue(cache.containsKey("second key"));
        assertFalse(cache.containsKey("third key"));
        assertFalse(cache.containsKey("fourth key"));
        assertFalse(cache.containsKey("fifth key"));
    }

    public void testPutAll() {
        Cache cache = initialize();

        HashMap map = new HashMap();
        map.put("fourth key", "forth value");
        map.put("fifth key", "fifth value");
        
        cache.putAll(map);
        
        assertTrue(cache.containsKey("first key"));
        assertTrue(cache.containsKey("second key"));
        assertTrue(cache.containsKey("third key"));
        assertTrue(cache.containsKey("fourth key"));
        assertTrue(cache.containsKey("fifth key"));
    }

    public void testKeySet() {
        Cache cache = initialize();

        Set set = cache.keySet();
        
        assertEquals(3, set.size());
        assertTrue(set.contains("first key"));
        assertTrue(set.contains("second key"));
        assertTrue(set.contains("third key"));
    }

    public void testValues() {
        Cache cache = initialize();

        Collection col = cache.values();
        
        assertEquals(3, col.size());
        assertTrue(col.contains("first value"));
        assertTrue(col.contains("second value"));
        assertTrue(col.contains("third value"));
    }

    public void testEntrySet() {
        Cache cache = initialize();

        Set set = cache.entrySet();
        
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
    
    public void testExpire() {
        Cache cache = new CountLimited();

        try {
            Properties params = new Properties();
            params.put(Cache.PARAM_NAME, "dummy");
            params.put(CountLimited.PARAM_CAPACITY, "3");
            cache.initialize(params);
        } catch (CacheAcquireException ex) {
            fail("Unexpected CacheAcquireException at initialization.");
        }
        
        assertEquals(0, cache.size());

        assertNull(cache.put("a", "#a"));
        assertEquals(1, cache.size());
        assertTrue(cache.containsKey("a"));
        
        assertNull(cache.put("b", "#b"));
        assertEquals(2, cache.size());
        assertTrue(cache.containsKey("a"));
        assertTrue(cache.containsKey("b"));
        
        assertNull(cache.put("c", "#c"));
        assertEquals(3, cache.size());
        assertTrue(cache.containsKey("a"));
        assertTrue(cache.containsKey("b"));
        assertTrue(cache.containsKey("c"));
        
        assertNull(cache.put("d", "#d"));
        assertEquals(3, cache.size());
        assertTrue(cache.containsKey("b"));
        assertTrue(cache.containsKey("c"));
        assertTrue(cache.containsKey("d"));
        
        assertEquals("#c", cache.put("c", "#c1"));
        assertEquals(3, cache.size());
        assertTrue(cache.containsKey("b"));
        assertTrue(cache.containsKey("c"));
        assertTrue(cache.containsKey("d"));
        
        assertEquals("#c1", cache.put("c", "#c2"));
        assertEquals(3, cache.size());
        assertTrue(cache.containsKey("b"));
        assertTrue(cache.containsKey("c"));
        assertTrue(cache.containsKey("d"));
        
        assertEquals("#c2", cache.put("c", "#c3"));
        assertEquals(3, cache.size());
        assertTrue(cache.containsKey("b"));
        assertTrue(cache.containsKey("c"));
        assertTrue(cache.containsKey("d"));
        
        assertEquals("#b", cache.put("b", "#b"));
        assertEquals(3, cache.size());
        assertTrue(cache.containsKey("b"));
        assertTrue(cache.containsKey("c"));
        assertTrue(cache.containsKey("d"));
        
        assertNull(cache.put("e", "#e"));
        assertEquals(3, cache.size());
        assertTrue(cache.containsKey("c"));
        assertTrue(cache.containsKey("d"));
        assertTrue(cache.containsKey("e"));

        assertNull(cache.put("f", "#f"));
        assertEquals(3, cache.size());
        assertTrue(cache.containsKey("d"));
        assertTrue(cache.containsKey("e"));
        assertTrue(cache.containsKey("f"));

        assertEquals("#e", cache.remove("e"));
        assertEquals(2, cache.size());
        assertTrue(cache.containsKey("d"));
        assertTrue(cache.containsKey("f"));

        assertNull(cache.put("g", "#g"));
        assertEquals(2, cache.size());
        assertTrue(cache.containsKey("f"));
        assertTrue(cache.containsKey("g"));

        assertEquals("#f", cache.remove("f"));
        assertEquals(1, cache.size());
        assertTrue(cache.containsKey("g"));

        assertNull(cache.remove("b"));
        assertEquals(1, cache.size());
        assertTrue(cache.containsKey("g"));

        assertEquals("#g", cache.remove("g"));
        assertEquals(0, cache.size());

        assertNull(cache.remove("x"));
        assertEquals(0, cache.size());

        assertNull(cache.put("a", "#a"));
        assertEquals(1, cache.size());
        assertTrue(cache.containsKey("a"));
        
        assertNull(cache.put("b", "#b"));
        assertEquals(2, cache.size());
        assertTrue(cache.containsKey("a"));
        assertTrue(cache.containsKey("b"));
        
        assertNull(cache.put("c", "#c"));
        assertEquals(3, cache.size());
        assertTrue(cache.containsKey("a"));
        assertTrue(cache.containsKey("b"));
        assertTrue(cache.containsKey("c"));
        
        assertNull(cache.put("d", "#d"));
        assertEquals(3, cache.size());
        assertTrue(cache.containsKey("b"));
        assertTrue(cache.containsKey("c"));
        assertTrue(cache.containsKey("d"));
    }
}
