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
 * @author <a href="mailto:ralf DOT joachim AT syscon-world DOT de">Ralf Joachim</a>
 * @version $Revision: 5951 $ $Date: 2006-04-29 04:11:14 -0600 (Sat, 29 Apr 2006) $
 * @since 1.0
 */
public final class TestGigaspacesCacheFactory extends TestCase {
    private static final boolean DISABLE_LOGGING = true;
    
    public static Test suite() {
        TestSuite suite = new TestSuite("CoherenceCacheFactory Tests");

        suite.addTest(new TestGigaspacesCacheFactory("testConstructor"));
        suite.addTest(new TestGigaspacesCacheFactory("testGetCacheType"));
        suite.addTest(new TestGigaspacesCacheFactory("testGetCacheClassName"));
        suite.addTest(new TestGigaspacesCacheFactory("testGetCache"));
        suite.addTest(new TestGigaspacesCacheFactory("testShutdown"));

        return suite;
    }

    public TestGigaspacesCacheFactory(final String name) { super(name); }

    public void testConstructor() {
        CacheFactory cf = new GigaspacesCacheFactory();
        assertTrue(cf instanceof GigaspacesCacheFactory);
    }

    public void testGetCacheType() {
        CacheFactory cf = new GigaspacesCacheFactory();
        assertEquals("gigaspaces", cf.getCacheType());
    }

    public void testGetCacheClassName() {
        CacheFactory cf = new GigaspacesCacheFactory();
        String classname = "org.castor.cache.distributed.GigaspacesCache";
        assertEquals(classname, cf.getCacheClassName());
    }

    public void testGetCache() {
        CacheFactory cf = new GigaspacesCacheFactory();
        try {
            Cache c = cf.getCache(null);
            assertTrue(c instanceof GigaspacesCache);
        } catch (CacheAcquireException ex) {
            fail("Failed to get instance of GigaspacesCache from factroy");
        }
    }

    public void testShutdown() {
        Logger logger = Logger.getLogger(GigaspacesCacheFactory.class);
        Level level = logger.getLevel();
        
        GigaspacesCacheFactory cf = new GigaspacesCacheFactory();
        int counter = DistributedCacheFactoryMock.getCounter();
        
        if (DISABLE_LOGGING) { logger.setLevel(Level.FATAL); }

        DistributedCacheFactoryMock.setException(null);
        cf.shutdown(Object.class.getName());
        assertEquals(counter, DistributedCacheFactoryMock.getCounter());
        
        DistributedCacheFactoryMock.setException(new Exception("dummy"));
        cf.shutdown(DistributedCacheFactoryMock.class.getName());
        assertEquals(counter, DistributedCacheFactoryMock.getCounter());

        logger.setLevel(level);
        
        DistributedCacheFactoryMock.setException(null);
        cf.shutdown(DistributedCacheFactoryMock.class.getName());
        assertEquals(counter + 1, DistributedCacheFactoryMock.getCounter());
    }
}
