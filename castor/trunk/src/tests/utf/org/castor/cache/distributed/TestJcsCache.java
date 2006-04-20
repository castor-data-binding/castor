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
package utf.org.castor.cache.distributed;

import java.util.HashMap;
import java.util.Properties;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import org.castor.cache.Cache;
import org.castor.cache.CacheAcquireException;
import org.castor.cache.distributed.JcsCache;

/**
 * @author <a href="mailto:ralf DOT joachim AT syscon-world DOT de">Ralf Joachim</a>
 * @version $Revision$ $Date$
 * @since 1.0
 */
public final class TestJcsCache extends TestCase {
    private static final boolean DISABLE_LOGGING = true;
    
    public static Test suite() {
        TestSuite suite = new TestSuite("JcsCache Tests");

        suite.addTest(new TestJcsCache("testStaticProperties"));
        suite.addTest(new TestJcsCache("testConstructor"));
        suite.addTest(new TestJcsCache("testGetType"));
        suite.addTest(new TestJcsCache("testInitialize"));
        suite.addTest(new TestJcsCache("testClose"));
        suite.addTest(new TestJcsCache("testUnsupportedMethods"));
        suite.addTest(new TestJcsCache("testContainsKey"));
        suite.addTest(new TestJcsCache("testGet"));
        suite.addTest(new TestJcsCache("testPut"));
        suite.addTest(new TestJcsCache("testRemove"));
        suite.addTest(new TestJcsCache("testPutAll"));
        suite.addTest(new TestJcsCache("testClear"));

        return suite;
    }

    public TestJcsCache(final String name) { super(name); }

    public void testStaticProperties() {
        assertEquals("jcs", JcsCache.TYPE);
        assertEquals("org.apache.jcs.JCS", JcsCache.IMPLEMENTATION);
    }

    public void testConstructor() {
        Cache c = new JcsCache();
        assertTrue(c instanceof JcsCache);
    }

    public void testGetType() {
        Cache c = new JcsCache();
        assertEquals("jcs", c.getType());
    }

    public void testInitialize() {
        Logger logger = Logger.getLogger(JcsCache.class);
        Level level = logger.getLevel();
        
        JcsCache c = new JcsCache();
        int counter = DistributedCacheFactoryMock.getCounter();
        
        Properties params = new Properties();
        params.put(Cache.PARAM_NAME, "dummy jcs cache");
        
        if (DISABLE_LOGGING) { logger.setLevel(Level.FATAL); }

        try {
            DistributedCacheFactoryMock.setException(new Exception("dummy"));
            c.initialize(DistributedCacheFactoryMock.class.getName(), params);
            fail("Failed to trow exception at initialize of JcsCache instance");
        } catch (CacheAcquireException ex) {
            assertEquals(counter, DistributedCacheFactoryMock.getCounter());
        }
        
        logger.setLevel(level);
        
        try {
            DistributedCacheFactoryMock.setException(null);
            c.initialize(DistributedCacheFactoryMock.class.getName(), params);
            assertEquals(counter + 1, DistributedCacheFactoryMock.getCounter());
            assertEquals("dummy jcs cache", DistributedCacheFactoryMock.getName());
            assertEquals("dummy jcs cache", c.getName());
        } catch (CacheAcquireException ex) {
            fail("Failed to initialize JcsCache instance");
        }
    }

    public void testClose() {
        Cache c = new JcsCache();
        c.close();
    }
    
    private Cache initialize() {
        JcsCache c = new JcsCache();

        Properties params = new Properties();
        params.put(Cache.PARAM_NAME, "dummy jcs cache");

        try {
            c.initialize(DistributedCacheFactoryMock.class.getName(), params);
        } catch (CacheAcquireException ex) {
            fail("Failed to initialize JcsCache instance");
        }
        
        return c;
    }

    public void testUnsupportedMethods() {
        Cache c = initialize();
        
        try {
            c.size();
            fail("size() should throw an UnsupportedOperationException");
        } catch (UnsupportedOperationException ex) {
            assertEquals("size()", ex.getMessage());
        }
        
        try {
            c.isEmpty();
            fail("isEmpty() should throw an UnsupportedOperationException");
        } catch (UnsupportedOperationException ex) {
            assertEquals("isEmpty()", ex.getMessage());
        }
        
        try {
            c.containsValue(new Object());
            fail("containsValue() should throw an UnsupportedOperationException");
        } catch (UnsupportedOperationException ex) {
            assertEquals("containsValue(Object)", ex.getMessage());
        }
        
        try {
            c.keySet();
            fail("keySet() should throw an UnsupportedOperationException");
        } catch (UnsupportedOperationException ex) {
            assertEquals("keySet()", ex.getMessage());
        }
        
        try {
            c.values();
            fail("values() should throw an UnsupportedOperationException");
        } catch (UnsupportedOperationException ex) {
            assertEquals("values()", ex.getMessage());
        }
        
        try {
            c.entrySet();
            fail("entrySet() should throw an UnsupportedOperationException");
        } catch (UnsupportedOperationException ex) {
            assertEquals("entrySet()", ex.getMessage());
        }
    }

    public void testContainsKey() {
        Logger logger = Logger.getLogger(JcsCache.class);
        Level level = logger.getLevel();
        
        Cache c = initialize();
        
        if (DISABLE_LOGGING) { logger.setLevel(Level.FATAL); }

        try {
            DistributedJcsCacheMock.setException(new Exception("dummy"));
            c.containsKey("first key");
            fail("Failed to trow exception at containsKey() of JcsCache instance");
        } catch (IllegalStateException ex) {
            assertTrue(true);
        }
        
        logger.setLevel(level);
        
        DistributedJcsCacheMock.setException(null);
        assertTrue(c.containsKey("first key"));
        assertTrue(c.containsKey("second key"));
        assertTrue(c.containsKey("third key"));
        assertFalse(c.containsKey("fourth key"));
        assertFalse(c.containsKey("fifth key"));
    }

    public void testGet() {
        Logger logger = Logger.getLogger(JcsCache.class);
        Level level = logger.getLevel();
        
        Cache c = initialize();
        
        if (DISABLE_LOGGING) { logger.setLevel(Level.FATAL); }

        try {
            DistributedJcsCacheMock.setException(new Exception("dummy"));
            c.get("first key");
            fail("Failed to trow exception at get() of JcsCache instance");
        } catch (IllegalStateException ex) {
            assertTrue(true);
        }
        
        logger.setLevel(level);
        
        DistributedJcsCacheMock.setException(null);
        assertEquals("first value", c.get("first key"));
        assertEquals("second value", c.get("second key"));
        assertEquals("third value", c.get("third key"));
        assertNull(c.get("fourth key"));
        assertNull(c.get("fifth key"));
    }

    public void testPut() {
        Logger logger = Logger.getLogger(JcsCache.class);
        Level level = logger.getLevel();
        
        Cache c = initialize();
        
        if (DISABLE_LOGGING) { logger.setLevel(Level.FATAL); }

        try {
            DistributedJcsCacheMock.setException(new Exception("dummy"));
            c.put("fourth key", "forth value");
            fail("Failed to trow exception at put() of JcsCache instance");
        } catch (IllegalStateException ex) {
            DistributedJcsCacheMock.setException(null);
            assertFalse(c.containsKey("fourth key"));
        }
        
        logger.setLevel(level);
        
        DistributedJcsCacheMock.setException(null);
        assertEquals("third value", c.put("third key", "alternate third value"));
        assertNull(c.put("fourth key", "forth value"));

        assertTrue(c.containsKey("first key"));
        assertTrue(c.containsKey("second key"));
        assertTrue(c.containsKey("third key"));
        assertTrue(c.containsKey("fourth key"));
        assertFalse(c.containsKey("fifth key"));
    }

    public void testRemove() {
        Logger logger = Logger.getLogger(JcsCache.class);
        Level level = logger.getLevel();
        
        Cache c = initialize();
        
        if (DISABLE_LOGGING) { logger.setLevel(Level.FATAL); }

        try {
            DistributedJcsCacheMock.setException(new Exception("dummy"));
            c.remove("third key");
            fail("Failed to trow exception at remove() of JcsCache instance");
        } catch (IllegalStateException ex) {
            DistributedJcsCacheMock.setException(null);
            assertTrue(c.containsKey("third key"));
        }
        
        logger.setLevel(level);
        
        DistributedJcsCacheMock.setException(null);
        assertEquals("third value", c.remove("third key"));

        assertTrue(c.containsKey("first key"));
        assertTrue(c.containsKey("second key"));
        assertFalse(c.containsKey("third key"));
        assertFalse(c.containsKey("fourth key"));
        assertFalse(c.containsKey("fifth key"));
    }

    public void testPutAll() {
        Logger logger = Logger.getLogger(JcsCache.class);
        Level level = logger.getLevel();
        
        Cache c = initialize();
        
        HashMap map = new HashMap();
        map.put("fourth key", "forth value");
        map.put("fifth key", "fifth value");
        
        if (DISABLE_LOGGING) { logger.setLevel(Level.FATAL); }

        try {
            DistributedJcsCacheMock.setException(new Exception("dummy"));
            c.putAll(map);
            fail("Failed to trow exception at put() of JcsCache instance");
        } catch (IllegalStateException ex) {
            DistributedJcsCacheMock.setException(null);
            assertFalse(c.containsKey("fourth key"));
            assertFalse(c.containsKey("fifth key"));
        }
        
        logger.setLevel(level);
        
        DistributedJcsCacheMock.setException(null);
        c.putAll(map);
        assertTrue(c.containsKey("first key"));
        assertTrue(c.containsKey("second key"));
        assertTrue(c.containsKey("third key"));
        assertTrue(c.containsKey("fourth key"));
        assertTrue(c.containsKey("fifth key"));
    }

    public void testClear() {
        Logger logger = Logger.getLogger(JcsCache.class);
        Level level = logger.getLevel();
        
        Cache c = initialize();
        
        if (DISABLE_LOGGING) { logger.setLevel(Level.FATAL); }

        try {
            DistributedJcsCacheMock.setException(new Exception("dummy"));
            c.clear();
            fail("Failed to trow exception at clear() of JcsCache instance");
        } catch (IllegalStateException ex) {
            DistributedJcsCacheMock.setException(null);
            assertTrue(c.containsKey("first key"));
            assertTrue(c.containsKey("second key"));
            assertTrue(c.containsKey("third key"));
            assertFalse(c.containsKey("fourth key"));
            assertFalse(c.containsKey("fifth key"));
        }
        
        logger.setLevel(level);
        
        DistributedJcsCacheMock.setException(null);
        c.clear();

        assertFalse(c.containsKey("first key"));
        assertFalse(c.containsKey("second key"));
        assertFalse(c.containsKey("third key"));
        assertFalse(c.containsKey("fourth key"));
        assertFalse(c.containsKey("fifth key"));
    }
}
