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
package org.castor.cache.distributed;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.castor.cache.Cache;

/**
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision$ $Date: 2006-04-29 03:57:35 -0600 (Sat, 29 Apr 2006) $
 * @since 1.0
 */
public final class TestAbstractDistributedCache extends TestCase {
    private Cache _cache;
    
    public static Test suite() {
        TestSuite suite = new TestSuite("AbstractDistributeCache Tests");

        suite.addTest(new TestAbstractDistributedCache("testContainsKey"));
        suite.addTest(new TestAbstractDistributedCache("testContainsValue"));
        suite.addTest(new TestAbstractDistributedCache("testClear"));

        suite.addTest(new TestAbstractDistributedCache("testSize"));
        suite.addTest(new TestAbstractDistributedCache("testIsEmpty"));
        suite.addTest(new TestAbstractDistributedCache("testGet"));
        suite.addTest(new TestAbstractDistributedCache("testPut"));
        suite.addTest(new TestAbstractDistributedCache("testRemove"));
        suite.addTest(new TestAbstractDistributedCache("testPutAll"));
        
        suite.addTest(new TestAbstractDistributedCache("testKeySet"));
        suite.addTest(new TestAbstractDistributedCache("testValues"));
        suite.addTest(new TestAbstractDistributedCache("testEntrySet"));

        return suite;
    }

    public TestAbstractDistributedCache(final String name) { super(name); }

    protected void setUp() {
        _cache = new CacheMock();
    }

    protected void tearDown() {
        _cache = null;
    }
    
    public void testContainsKey() {
        assertTrue(_cache.containsKey("first key"));
        assertTrue(_cache.containsKey("second key"));
        assertTrue(_cache.containsKey("third key"));
        assertFalse(_cache.containsKey("fourth key"));
        assertFalse(_cache.containsKey("fifth key"));
    }

    public void testContainsValue() {
        assertTrue(_cache.containsValue("first value"));
        assertTrue(_cache.containsValue("second value"));
        assertTrue(_cache.containsValue("third value"));
        assertFalse(_cache.containsValue("fourth value"));
        assertFalse(_cache.containsValue("fifth value"));
    }

    public void testClear() {
        _cache.clear();

        assertFalse(_cache.containsKey("first key"));
        assertFalse(_cache.containsKey("second key"));
        assertFalse(_cache.containsKey("third key"));
        assertFalse(_cache.containsKey("fourth key"));
        assertFalse(_cache.containsKey("fifth key"));
    }

    public void testSize() {
        assertEquals(3, _cache.size());
        _cache.clear();
        assertEquals(0, _cache.size());
    }

    public void testIsEmpty() {
        assertFalse(_cache.isEmpty());
        _cache.clear();
        assertTrue(_cache.isEmpty());
    }

    public void testGet() {
        assertEquals("first value", _cache.get("first key"));
        assertEquals("second value", _cache.get("second key"));
        assertEquals("third value", _cache.get("third key"));
        assertNull(_cache.get("fourth key"));
        assertNull(_cache.get("fifth key"));
    }

    public void testPut() {
        assertEquals("third value", _cache.put("third key", "alternate third value"));
        assertNull(_cache.put("fourth key", "forth value"));

        assertTrue(_cache.containsKey("first key"));
        assertTrue(_cache.containsKey("second key"));
        assertTrue(_cache.containsKey("third key"));
        assertTrue(_cache.containsKey("fourth key"));
        assertFalse(_cache.containsKey("fifth key"));
    }

    public void testRemove() {
        assertEquals("third value", _cache.remove("third key"));

        assertTrue(_cache.containsKey("first key"));
        assertTrue(_cache.containsKey("second key"));
        assertFalse(_cache.containsKey("third key"));
        assertFalse(_cache.containsKey("fourth key"));
        assertFalse(_cache.containsKey("fifth key"));
    }

    public void testPutAll() {
        HashMap map = new HashMap();
        map.put("fourth key", "forth value");
        map.put("fifth key", "fifth value");
        
        _cache.putAll(map);
        
        assertTrue(_cache.containsKey("first key"));
        assertTrue(_cache.containsKey("second key"));
        assertTrue(_cache.containsKey("third key"));
        assertTrue(_cache.containsKey("fourth key"));
        assertTrue(_cache.containsKey("fifth key"));
    }

    public void testKeySet() {
        Set set = _cache.keySet();
        
        assertEquals(3, set.size());
        assertTrue(set.contains("first key"));
        assertTrue(set.contains("second key"));
        assertTrue(set.contains("third key"));
    }

    public void testValues() {
        Collection col = _cache.values();
        
        assertEquals(3, col.size());
        assertTrue(col.contains("first value"));
        assertTrue(col.contains("second value"));
        assertTrue(col.contains("third value"));
    }

    public void testEntrySet() {
        Set set = _cache.entrySet();
        
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
