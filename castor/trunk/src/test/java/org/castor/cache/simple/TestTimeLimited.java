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
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.castor.cache.Cache;
import org.castor.cache.CacheAcquireException;

/**
 * @author <a href="mailto:ralf DOT joachim AT syscon-world DOT de">Ralf Joachim</a>
 * @version $Revision$ $Date$
 * @since 1.0
 */
public final class TestTimeLimited extends TestCase {
    public static Test suite() {
        TestSuite suite = new TestSuite("TimeLimited Tests");

        suite.addTest(new TestTimeLimited("testBasics"));

        suite.addTest(new TestTimeLimited("testContainsKey"));
        suite.addTest(new TestTimeLimited("testContainsValue"));
        suite.addTest(new TestTimeLimited("testClear"));

        suite.addTest(new TestTimeLimited("testSize"));
        suite.addTest(new TestTimeLimited("testIsEmpty"));
        suite.addTest(new TestTimeLimited("testGet"));
        suite.addTest(new TestTimeLimited("testPut"));
        suite.addTest(new TestTimeLimited("testRemove"));
        suite.addTest(new TestTimeLimited("testPutAll"));
        
        suite.addTest(new TestTimeLimited("testKeySet"));
        suite.addTest(new TestTimeLimited("testValues"));
        suite.addTest(new TestTimeLimited("testEntrySet"));

        suite.addTest(new TestTimeLimited("testExpire"));
        
        return suite;
    }

    public TestTimeLimited(final String name) { super(name); }
    
    public void testBasics() throws CacheAcquireException {
        assertEquals("time-limited", TimeLimited.TYPE);
        assertEquals("ttl", TimeLimited.PARAM_TTL);
        assertEquals(30, TimeLimited.DEFAULT_TTL);

        Cache cache = new TimeLimited();
        assertTrue(cache instanceof TimeLimited);

        assertEquals("time-limited", cache.getType());
        assertEquals(30, ((TimeLimited) cache).getTTL());
        assertEquals(Cache.DEFAULT_NAME, cache.getName());
        
        Properties params = new Properties();
        params.put(Cache.PARAM_NAME, "dummy1");
        cache.initialize(params);
        assertEquals(30, ((TimeLimited) cache).getTTL());
        assertEquals("dummy1", cache.getName());
        
        params.clear();
        params.put(Cache.PARAM_NAME, "dummy2");
        params.put(TimeLimited.PARAM_TTL, "-10");
        cache.initialize(params);
        assertEquals(30, ((TimeLimited) cache).getTTL());
        assertEquals("dummy2", cache.getName());
        
        params.clear();
        params.put(Cache.PARAM_NAME, "dummy3");
        params.put(TimeLimited.PARAM_TTL, "0");
        cache.initialize(params);
        assertEquals(30, ((TimeLimited) cache).getTTL());
        assertEquals("dummy3", cache.getName());
        
        params.clear();
        params.put(Cache.PARAM_NAME, "dummy4");
        params.put(TimeLimited.PARAM_TTL, "10");
        cache.initialize(params);
        assertEquals(10, ((TimeLimited) cache).getTTL());
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
        Cache cache = new TimeLimited();

        try {
            Properties params = new Properties();
            params.put(Cache.PARAM_NAME, "dummy");
            params.put(TimeLimited.PARAM_TTL, new Integer(10));
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
    
    public void testExpire() throws InterruptedException {
        Cache cache = new TimeLimited();

        try {
            Properties params = new Properties();
            params.put(Cache.PARAM_NAME, "dummy");
            params.put(TimeLimited.PARAM_TTL, "5");
            cache.initialize(params);
        } catch (CacheAcquireException ex) {
            fail("Unexpected CacheAcquireException at initialization.");
        }

        assertEquals(0, cache.size());
        
        assertNull(cache.put("a", "#a"));
        assertEquals(1, cache.size());
        assertTrue(cache.containsKey("a"));
        
        Thread.sleep(1000);

        assertEquals("#a", cache.put("a", "#a"));
        assertEquals(1, cache.size());
        assertTrue(cache.containsKey("a"));

        Thread.sleep(100);

        assertNull(cache.put("b", "#b"));
        assertEquals(2, cache.size());
        assertTrue(cache.containsKey("a"));
        assertTrue(cache.containsKey("b"));

        Thread.sleep(200);

        assertNull(cache.put("c", "#c"));
        assertEquals(3, cache.size());
        assertTrue(cache.containsKey("a"));
        assertTrue(cache.containsKey("b"));
        assertTrue(cache.containsKey("c"));

        Thread.sleep(1000);

        assertNull(cache.put("d", "#d"));
        assertEquals(4, cache.size());
        assertTrue(cache.containsKey("a"));
        assertTrue(cache.containsKey("b"));
        assertTrue(cache.containsKey("c"));
        assertTrue(cache.containsKey("d"));

        Thread.sleep(500);
        
        assertNull(cache.put("e", "#e"));
        assertEquals(5, cache.size());
        assertTrue(cache.containsKey("a"));
        assertTrue(cache.containsKey("b"));
        assertTrue(cache.containsKey("c"));
        assertTrue(cache.containsKey("d"));
        assertTrue(cache.containsKey("e"));

        Thread.sleep(1000);

        assertNull(cache.put("f", "#f"));
        assertEquals(6, cache.size());
        assertTrue(cache.containsKey("a"));
        assertTrue(cache.containsKey("b"));
        assertTrue(cache.containsKey("c"));
        assertTrue(cache.containsKey("d"));
        assertTrue(cache.containsKey("e"));
        assertTrue(cache.containsKey("f"));

        Thread.sleep(1000);

        assertNull(cache.put("g", "#g"));
        assertEquals(7, cache.size());
        assertTrue(cache.containsKey("a"));
        assertTrue(cache.containsKey("b"));
        assertTrue(cache.containsKey("c"));
        assertTrue(cache.containsKey("d"));
        assertTrue(cache.containsKey("e"));
        assertTrue(cache.containsKey("f"));
        assertTrue(cache.containsKey("g"));

        Thread.sleep(1000);
        
        assertNull(cache.put("h", "#h"));
        assertEquals(8, cache.size());
        assertTrue(cache.containsKey("a"));
        assertTrue(cache.containsKey("b"));
        assertTrue(cache.containsKey("c"));
        assertTrue(cache.containsKey("d"));
        assertTrue(cache.containsKey("e"));
        assertTrue(cache.containsKey("f"));
        assertTrue(cache.containsKey("g"));
        assertTrue(cache.containsKey("h"));

        Thread.sleep(950);
        
        assertEquals(7, cache.size());
        assertTrue(cache.containsKey("b"));
        assertTrue(cache.containsKey("c"));
        assertTrue(cache.containsKey("d"));
        assertTrue(cache.containsKey("e"));
        assertTrue(cache.containsKey("f"));
        assertTrue(cache.containsKey("g"));
        assertTrue(cache.containsKey("h"));

        Thread.sleep(1000);
        
        assertEquals(5, cache.size());
        assertTrue(cache.containsKey("d"));
        assertTrue(cache.containsKey("e"));
        assertTrue(cache.containsKey("f"));
        assertTrue(cache.containsKey("g"));
        assertTrue(cache.containsKey("h"));

        Thread.sleep(1100);
        
        assertEquals(3, cache.size());
        assertTrue(cache.containsKey("f"));
        assertTrue(cache.containsKey("g"));
        assertTrue(cache.containsKey("h"));

        Thread.sleep(1000);
        
        assertEquals(2, cache.size());
        assertTrue(cache.containsKey("g"));
        assertTrue(cache.containsKey("h"));

        Thread.sleep(1000);
        
        assertEquals(1, cache.size());
        assertTrue(cache.containsKey("h"));

        Thread.sleep(1000);
        
        assertEquals(0, cache.size());
    }
}
