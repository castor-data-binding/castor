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
package org.castor.cache.hashbelt;

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
import org.castor.cache.hashbelt.AbstractHashbelt;
import org.castor.cache.hashbelt.FIFOHashbelt;
import org.castor.cache.hashbelt.container.MapContainer;
import org.castor.cache.hashbelt.reaper.NullReaper;

/**
 * @author <a href="mailto:ralf DOT joachim AT syscon-world DOT de">Ralf Joachim</a>
 * @version $Revision$ $Date$
 * @since 1.0
 */
public final class TestFIFOHashbelt extends TestCase {
    public static Test suite() {
        TestSuite suite = new TestSuite("FIFOHashbelt Tests");

        suite.addTest(new TestFIFOHashbelt("testBasics"));

        suite.addTest(new TestFIFOHashbelt("testContainsKey"));
        suite.addTest(new TestFIFOHashbelt("testContainsValue"));
        suite.addTest(new TestFIFOHashbelt("testClear"));

        suite.addTest(new TestFIFOHashbelt("testSize"));
        suite.addTest(new TestFIFOHashbelt("testIsEmpty"));
        suite.addTest(new TestFIFOHashbelt("testGet"));
        suite.addTest(new TestFIFOHashbelt("testPut"));
        suite.addTest(new TestFIFOHashbelt("testRemove"));
        suite.addTest(new TestFIFOHashbelt("testPutAll"));
        
        suite.addTest(new TestFIFOHashbelt("testKeySet"));
        suite.addTest(new TestFIFOHashbelt("testValues"));
        suite.addTest(new TestFIFOHashbelt("testEntrySet"));

        return suite;
    }

    public TestFIFOHashbelt(final String name) { super(name); }
    
    public void testBasics() {
        assertEquals("fifo", FIFOHashbelt.TYPE);
        assertEquals("containers", AbstractHashbelt.PARAM_CONTAINERS);
        assertEquals(10, AbstractHashbelt.DEFAULT_CONTAINERS);
        assertEquals("container-class", AbstractHashbelt.PARAM_CONTAINER_CLASS);
        assertEquals(MapContainer.class, AbstractHashbelt.DEFAULT_CONTAINER_CLASS);
        assertEquals("reaper-class", AbstractHashbelt.PARAM_REAPER_CLASS);
        assertEquals(NullReaper.class, AbstractHashbelt.DEFAULT_REAPER_CLASS);
        assertEquals("capacity", AbstractHashbelt.PARAM_CAPACITY);
        assertEquals(0, AbstractHashbelt.DEFAULT_CAPACITY);
        assertEquals("ttl", AbstractHashbelt.PARAM_TTL);
        assertEquals(60, AbstractHashbelt.DEFAULT_TTL);
        assertEquals("monitor", AbstractHashbelt.PARAM_MONITOR);
        assertEquals(0, AbstractHashbelt.DEFAULT_MONITOR);

        Cache cache = new FIFOHashbelt();
        assertTrue(cache instanceof FIFOHashbelt);
        assertEquals("fifo", cache.getType());

        Properties params = new Properties();
        params.put(Cache.PARAM_NAME, "dummy1");
        try {
            cache.initialize(params);
        } catch (CacheAcquireException ex) {
            fail("Failed to initialize FIFOHashbelt instance");
        }

        assertFalse(cache.containsKey("first key"));
        assertFalse(cache.containsKey("second key"));

        assertNull(cache.put("first key", "first value"));

        assertTrue(cache.containsKey("first key"));
        assertFalse(cache.containsKey("second key"));

        assertNull(cache.put("second key", "second value"));

        assertTrue(cache.containsKey("first key"));
        assertTrue(cache.containsKey("second key"));
    }

    private Cache initialize() throws CacheAcquireException {
        Cache cache = new FIFOHashbelt();
        Properties params = new Properties();
        params.put(Cache.PARAM_NAME, "dummy1");
        cache.initialize(params);

        assertNull(cache.put("first key", "first value"));
        assertNull(cache.put("second key", "second value"));
        assertNull(cache.put("third key", "third value"));
        
        return cache;
    }
    
    public void testContainsKey() throws CacheAcquireException {
        Cache cache = initialize();

        assertTrue(cache.containsKey("first key"));
        assertTrue(cache.containsKey("second key"));
        assertTrue(cache.containsKey("third key"));
        assertFalse(cache.containsKey("fourth key"));
        assertFalse(cache.containsKey("fifth key"));
    }

    public void testContainsValue() throws CacheAcquireException {
        Cache cache = initialize();

        assertTrue(cache.containsValue("first value"));
        assertTrue(cache.containsValue("second value"));
        assertTrue(cache.containsValue("third value"));
        assertFalse(cache.containsValue("fourth value"));
        assertFalse(cache.containsValue("fifth value"));
    }

    public void testClear() throws CacheAcquireException {
        Cache cache = initialize();

        cache.clear();

        assertFalse(cache.containsKey("first key"));
        assertFalse(cache.containsKey("second key"));
        assertFalse(cache.containsKey("third key"));
        assertFalse(cache.containsKey("fourth key"));
        assertFalse(cache.containsKey("fifth key"));
    }

    public void testSize() throws CacheAcquireException {
        Cache cache = initialize();

        assertEquals(3, cache.size());
        cache.clear();
        assertEquals(0, cache.size());
    }

    public void testIsEmpty() throws CacheAcquireException {
        Cache cache = initialize();

        assertFalse(cache.isEmpty());
        cache.clear();
        assertTrue(cache.isEmpty());
    }

    public void testGet() throws CacheAcquireException {
        Cache cache = initialize();

        assertEquals("first value", cache.get("first key"));
        assertEquals("second value", cache.get("second key"));
        assertEquals("third value", cache.get("third key"));
        assertNull(cache.get("fourth key"));
        assertNull(cache.get("fifth key"));
    }

    public void testPut() throws CacheAcquireException {
        Cache cache = initialize();

        assertEquals("third value", cache.put("third key", "alternate third value"));
        assertNull(cache.put("fourth key", "forth value"));

        assertTrue(cache.containsKey("first key"));
        assertTrue(cache.containsKey("second key"));
        assertTrue(cache.containsKey("third key"));
        assertTrue(cache.containsKey("fourth key"));
        assertFalse(cache.containsKey("fifth key"));
    }

    public void testRemove() throws CacheAcquireException {
        Cache cache = initialize();

        assertEquals("third value", cache.remove("third key"));

        assertTrue(cache.containsKey("first key"));
        assertTrue(cache.containsKey("second key"));
        assertFalse(cache.containsKey("third key"));
        assertFalse(cache.containsKey("fourth key"));
        assertFalse(cache.containsKey("fifth key"));
    }

    public void testPutAll() throws CacheAcquireException {
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

    public void testKeySet() throws CacheAcquireException {
        Cache cache = initialize();

        Set set = cache.keySet();
        
        assertEquals(3, set.size());
        assertTrue(set.contains("first key"));
        assertTrue(set.contains("second key"));
        assertTrue(set.contains("third key"));
    }

    public void testValues() throws CacheAcquireException {
        Cache cache = initialize();

        Collection col = cache.values();
        
        assertEquals(3, col.size());
        assertTrue(col.contains("first value"));
        assertTrue(col.contains("second value"));
        assertTrue(col.contains("third value"));
    }

    public void testEntrySet() throws CacheAcquireException {
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
}
