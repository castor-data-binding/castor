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

import java.util.HashMap;
import java.util.Properties;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.castor.cache.Cache;
import org.castor.cache.CacheAcquireException;

/**
 * @author <a href="mailto:ralf DOT joachim AT syscon-world DOT de">Ralf Joachim</a>
 * @version $Revision$ $Date$
 * @since 1.0
 */
public final class TestOsCache extends TestCase {
    private static final boolean DISABLE_LOGGING = true;
    
    public static Test suite() {
        TestSuite suite = new TestSuite("OsCache Tests");

        suite.addTest(new TestOsCache("testStaticProperties"));
        suite.addTest(new TestOsCache("testConstructor"));
        suite.addTest(new TestOsCache("testGetType"));
        suite.addTest(new TestOsCache("testUnsupported"));
        suite.addTest(new TestOsCache("testInitialize"));
        suite.addTest(new TestOsCache("testClose"));
        suite.addTest(new TestOsCache("testGet"));
        suite.addTest(new TestOsCache("testContainsKey"));
        suite.addTest(new TestOsCache("testPut"));
        suite.addTest(new TestOsCache("testRemove"));
        suite.addTest(new TestOsCache("testPutAll"));
        suite.addTest(new TestOsCache("testClear"));

        return suite;
    }

    public TestOsCache(final String name) { super(name); }

    public void testStaticProperties() {
        assertEquals("oscache", OsCache.TYPE);
        assertEquals("com.opensymphony.oscache.general.GeneralCacheAdministrator",
                     OsCache.IMPLEMENTATION);
        assertEquals("com.opensymphony.oscache.base.NeedsRefreshException",
                     OsCache.NEEDS_REFRESH_EXCEPTION);
    }

    public void testConstructor() {
        Cache c = new OsCache(null);
        assertTrue(c instanceof OsCache);
    }

    public void testGetType() {
        Cache c = new OsCache(null);
        assertEquals("oscache", c.getType());
    }

    public void testUnsupported() {
        Cache c = new OsCache(null);
        
        try {
            c.size();
            fail("UnsupportedOperationException should have been thrown.");
        } catch (UnsupportedOperationException ex) {
            assertEquals("size()", ex.getMessage());
        } catch (Throwable t) {
            fail("UnsupportedOperationException should have been thrown.");
        }
        
        try {
            c.isEmpty();
            fail("UnsupportedOperationException should have been thrown.");
        } catch (UnsupportedOperationException ex) {
            assertEquals("isEmpty()", ex.getMessage());
        } catch (Throwable t) {
            fail("UnsupportedOperationException should have been thrown.");
        }
        
        try {
            c.containsValue("test");
            fail("UnsupportedOperationException should have been thrown.");
        } catch (UnsupportedOperationException ex) {
            assertEquals("containsValue(Object)", ex.getMessage());
        } catch (Throwable t) {
            fail("UnsupportedOperationException should have been thrown.");
        }
        
        try {
            c.keySet();
            fail("UnsupportedOperationException should have been thrown.");
        } catch (UnsupportedOperationException ex) {
            assertEquals("keySet()", ex.getMessage());
        } catch (Throwable t) {
            fail("UnsupportedOperationException should have been thrown.");
        }
        
        try {
            c.values();
            fail("UnsupportedOperationException should have been thrown.");
        } catch (UnsupportedOperationException ex) {
            assertEquals("values()", ex.getMessage());
        } catch (Throwable t) {
            fail("UnsupportedOperationException should have been thrown.");
        }
        
        try {
            c.entrySet();
            fail("UnsupportedOperationException should have been thrown.");
        } catch (UnsupportedOperationException ex) {
            assertEquals("entrySet()", ex.getMessage());
        } catch (Throwable t) {
            fail("UnsupportedOperationException should have been thrown.");
        }
    }

    public void testInitialize() {
        Logger logger = Logger.getLogger(OsCache.class);
        Level level = logger.getLevel();
        
        int counter = DistributedOsCacheMock.getCounter();

        OsCache c = new OsCache("test");
        
        Properties params = new Properties();
        params.put(Cache.PARAM_NAME, "dummy oscache");
        
        if (DISABLE_LOGGING) { logger.setLevel(Level.FATAL); }

        try {
            c.initialize(params);
            fail("Failed to trow exception at initialize of OsCache instance");
        } catch (CacheAcquireException ex) {
            assertEquals(counter, DistributedOsCacheMock.getCounter());
        }
        
        logger.setLevel(level);
        
        c = new OsCache(new DistributedOsCacheMock());
        
        try {
            c.initialize(params);
            assertEquals(counter, DistributedOsCacheMock.getCounter());
            assertEquals("dummy oscache", c.getName());
        } catch (CacheAcquireException ex) {
            fail("Failed to initialize OsCache instance");
        }
    }

    public void testClose() {
        Cache c = new OsCache(null);
        c.close();
    }
    
    private Cache initialize() {
        OsCache c = new OsCache(new DistributedOsCacheMock());

        Properties params = new Properties();
        params.put(Cache.PARAM_NAME, "dummy oscache");

        try {
            c.initialize(params);
        } catch (CacheAcquireException ex) {
            fail("Failed to initialize OsCache instance");
        }
        
        return c;
    }

    public void testGet() {
        Logger logger = Logger.getLogger(OsCache.class);
        Level level = logger.getLevel();
        
        Cache c = initialize();
        
        if (DISABLE_LOGGING) { logger.setLevel(Level.FATAL); }

        try {
            DistributedOsCacheMock.setException(new Exception("dummy"));
            c.get("first key");
            fail("Failed to trow exception at get() of OsCache instance");
        } catch (IllegalStateException ex) {
            assertTrue(true);
        }
        
        logger.setLevel(level);
        
        DistributedOsCacheMock.setException(null);
        int counter = DistributedOsCacheMock.getCounter();
        assertEquals("first value", c.get("first key"));
        assertEquals(counter + 1, DistributedOsCacheMock.getCounter());
        assertEquals("second value", c.get("second key"));
        assertEquals(counter + 2, DistributedOsCacheMock.getCounter());
        assertEquals("third value", c.get("third key"));
        assertEquals(counter + 3, DistributedOsCacheMock.getCounter());
        assertNull(c.get("fourth key"));
        assertEquals(counter + 4, DistributedOsCacheMock.getCounter());
        assertNull(c.get("fifth key"));
        assertEquals(counter + 5, DistributedOsCacheMock.getCounter());
    }

    public void testContainsKey() {
        Logger logger = Logger.getLogger(OsCache.class);
        Level level = logger.getLevel();
        
        Cache c = initialize();
        
        if (DISABLE_LOGGING) { logger.setLevel(Level.FATAL); }

        try {
            DistributedOsCacheMock.setException(new Exception("dummy"));
            c.containsKey("first key");
            fail("Failed to trow exception at containsKey() of OsCache instance");
        } catch (IllegalStateException ex) {
            assertTrue(true);
        }
        
        logger.setLevel(level);
        
        DistributedOsCacheMock.setException(null);
        assertTrue(c.containsKey("first key"));
        assertTrue(c.containsKey("second key"));
        assertTrue(c.containsKey("third key"));
        assertFalse(c.containsKey("fourth key"));
        assertFalse(c.containsKey("fifth key"));
    }

    public void testPut() {
        Logger logger = Logger.getLogger(OsCache.class);
        Level level = logger.getLevel();
        
        Cache c = initialize();
        
        if (DISABLE_LOGGING) { logger.setLevel(Level.FATAL); }

        try {
            DistributedOsCacheMock.setException(new Exception("dummy"));
            c.put("fourth key", "forth value");
            fail("Failed to trow exception at put() of OsCache instance");
        } catch (IllegalStateException ex) {
            DistributedOsCacheMock.setException(null);
            assertFalse(c.containsKey("fourth key"));
        }
        
        logger.setLevel(level);
        
        DistributedOsCacheMock.setException(null);
        assertEquals("third value", c.put("third key", "alternate third value"));
        assertNull(c.put("fourth key", "forth value"));

        assertTrue(c.containsKey("first key"));
        assertTrue(c.containsKey("second key"));
        assertTrue(c.containsKey("third key"));
        assertTrue(c.containsKey("fourth key"));
        assertFalse(c.containsKey("fifth key"));
    }

    public void testRemove() {
        Logger logger = Logger.getLogger(OsCache.class);
        Level level = logger.getLevel();
        
        Cache c = initialize();
        
        if (DISABLE_LOGGING) { logger.setLevel(Level.FATAL); }

        try {
            DistributedOsCacheMock.setException(new Exception("dummy"));
            c.remove("third key");
            fail("Failed to trow exception at remove() of OsCache instance");
        } catch (IllegalStateException ex) {
            DistributedOsCacheMock.setException(null);
            assertTrue(c.containsKey("third key"));
        }
        
        logger.setLevel(level);
        
        DistributedOsCacheMock.setException(null);
        assertEquals("third value", c.remove("third key"));

        assertTrue(c.containsKey("first key"));
        assertTrue(c.containsKey("second key"));
        assertFalse(c.containsKey("third key"));
        assertFalse(c.containsKey("fourth key"));
        assertFalse(c.containsKey("fifth key"));
    }

    public void testPutAll() {
        Logger logger = Logger.getLogger(OsCache.class);
        Level level = logger.getLevel();
        
        Cache c = initialize();
        
        HashMap map = new HashMap();
        map.put("fourth key", "forth value");
        map.put("fifth key", "fifth value");
        
        if (DISABLE_LOGGING) { logger.setLevel(Level.FATAL); }

        try {
            DistributedOsCacheMock.setException(new Exception("dummy"));
            c.putAll(map);
            fail("Failed to trow exception at put() of JcsCache instance");
        } catch (IllegalStateException ex) {
            DistributedOsCacheMock.setException(null);
            assertFalse(c.containsKey("fourth key"));
            assertFalse(c.containsKey("fifth key"));
        }
        
        logger.setLevel(level);
        
        DistributedOsCacheMock.setException(null);
        c.putAll(map);
        assertTrue(c.containsKey("first key"));
        assertTrue(c.containsKey("second key"));
        assertTrue(c.containsKey("third key"));
        assertTrue(c.containsKey("fourth key"));
        assertTrue(c.containsKey("fifth key"));
    }

    public void testClear() {
        Logger logger = Logger.getLogger(OsCache.class);
        Level level = logger.getLevel();
        
        Cache c = initialize();
        
        if (DISABLE_LOGGING) { logger.setLevel(Level.FATAL); }

        try {
            DistributedOsCacheMock.setException(new Exception("dummy"));
            c.clear();
            fail("Failed to trow exception at clear() of JcsCache instance");
        } catch (IllegalStateException ex) {
            DistributedOsCacheMock.setException(null);
            assertTrue(c.containsKey("first key"));
            assertTrue(c.containsKey("second key"));
            assertTrue(c.containsKey("third key"));
            assertFalse(c.containsKey("fourth key"));
            assertFalse(c.containsKey("fifth key"));
        }
        
        logger.setLevel(level);
        
        DistributedOsCacheMock.setException(null);
        c.clear();

        assertFalse(c.containsKey("first key"));
        assertFalse(c.containsKey("second key"));
        assertFalse(c.containsKey("third key"));
        assertFalse(c.containsKey("fourth key"));
        assertFalse(c.containsKey("fifth key"));
    }
}
