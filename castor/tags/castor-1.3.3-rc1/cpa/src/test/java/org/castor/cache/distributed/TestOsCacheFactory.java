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

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.castor.cache.Cache;
import org.castor.cache.CacheAcquireException;
import org.castor.cache.CacheFactory;

/**
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision$ $Date$
 * @since 1.0
 */
public final class TestOsCacheFactory extends TestCase {
    private static final boolean DISABLE_LOGGING = true;
    
    public static Test suite() {
        TestSuite suite = new TestSuite("OsCacheFactory Tests");

        suite.addTest(new TestOsCacheFactory("testConstructor"));
        suite.addTest(new TestOsCacheFactory("testGetCacheType"));
        suite.addTest(new TestOsCacheFactory("testGetCacheClassName"));
        suite.addTest(new TestOsCacheFactory("testGetCache"));
        suite.addTest(new TestOsCacheFactory("testShutdown"));

        return suite;
    }

    public TestOsCacheFactory(final String name) { super(name); }

    public void testConstructor() {
        CacheFactory<String, String> cf = new OsCacheFactory<String, String>();
        assertTrue(cf instanceof OsCacheFactory);
    }

    public void testGetCacheType() {
        CacheFactory<String, String> cf = new OsCacheFactory<String, String>();
        assertEquals("oscache", cf.getCacheType());
    }

    public void testGetCacheClassName() {
        CacheFactory<String, String> cf = new OsCacheFactory<String, String>();
        String classname = "org.castor.cache.distributed.OsCache";
        assertEquals(classname, cf.getCacheClassName());
    }

    public void testGetCache() {
        Logger logger = Logger.getLogger(OsCacheFactory.class);
        Level level = logger.getLevel();

        OsCacheFactory<String, String> cf1 = new OsCacheFactory<String, String>();
        try {
            Cache<String, String> c1 = cf1.getCache(DistributedOsCacheMock.class.getName(), null);
            assertTrue(c1 instanceof OsCache);
        } catch (CacheAcquireException ex) {
            fail("Failed to get instance of OsCache from factroy");
        }

        if (DISABLE_LOGGING) { logger.setLevel(Level.FATAL); }

        OsCacheFactory<String, String> cf2 = new OsCacheFactory<String, String>();
        try {
            cf2.getCache("org.castor.UnkownCache", null);
            fail("Failed to trow exception at initialize of OsCache instance");
        } catch (CacheAcquireException ex) {
            assertNotNull(ex);
            assertTrue(ex.getCause() instanceof ClassNotFoundException);
        }
        
        logger.setLevel(level);
    }

    public void testShutdown() {
        Logger logger = Logger.getLogger(OsCacheFactory.class);
        Level level = logger.getLevel();

        OsCacheFactory<String, String> cf = new OsCacheFactory<String, String>();
        try {
            Cache<String, String> c = cf.getCache(DistributedOsCacheMock.class.getName(), null);
            assertTrue(c instanceof OsCache);
        } catch (CacheAcquireException ex) {
            fail("Failed to get instance of OsCache from factroy");
        }

        int counter = DistributedOsCacheMock.getCounter();
        
        DistributedOsCacheMock.setException(null);
        cf.shutdown();
        assertEquals(counter + 1, DistributedOsCacheMock.getCounter());

        if (DISABLE_LOGGING) { logger.setLevel(Level.FATAL); }

        DistributedOsCacheMock.setException(new NullPointerException());
        try {
            cf.shutdown();
            fail("Failed to trow exception at shutdown of OsCache instance");
        } catch (RuntimeException ex) {
            assertEquals(counter + 1, DistributedOsCacheMock.getCounter());
            assertTrue(ex instanceof IllegalStateException);
        }
        
        logger.setLevel(level);
    }
}
