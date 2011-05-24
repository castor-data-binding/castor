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

import java.util.Properties;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.castor.cache.Cache;
import org.castor.cache.CacheAcquireException;

/**
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision$ $Date: 2006-04-29 04:11:14 -0600 (Sat, 29 Apr 2006) $
 * @since 1.0
 */
public final class TestFKCache extends TestCase {
    private static final boolean DISABLE_LOGGING = true;
    
    public static Test suite() {
        TestSuite suite = new TestSuite("FKCache Tests");

        suite.addTest(new TestFKCache("testStaticProperties"));
        suite.addTest(new TestFKCache("testConstructor"));
        suite.addTest(new TestFKCache("testGetType"));
        suite.addTest(new TestFKCache("testInitialize"));
        suite.addTest(new TestFKCache("testClose"));

        return suite;
    }

    public TestFKCache(final String name) { super(name); }

    public void testStaticProperties() {
        assertEquals("fkcache", FKCache.TYPE);
        assertEquals("javax.util.jcache.CacheAccessFactory", FKCache.IMPLEMENTATION);
    }

    public void testConstructor() {
        Cache c = new FKCache();
        assertTrue(c instanceof FKCache);
    }

    public void testGetType() {
        Cache c = new FKCache();
        assertEquals("fkcache", c.getType());
    }

    public void testInitialize() {
        Logger logger = Logger.getLogger(FKCache.class);
        Level level = logger.getLevel();
        
        FKCache c = new FKCache();
        int counter = DistributedCacheFactoryMock.getCounter();
        
        Properties params = new Properties();
        params.put(Cache.PARAM_NAME, "dummy fkcache");
        
        if (DISABLE_LOGGING) { logger.setLevel(Level.FATAL); }

        try {
            DistributedCacheFactoryMock.setException(new Exception("dummy"));
            c.initialize(DistributedCacheFactoryMock.class.getName(), params);
            fail("Failed to trow exception at initialize of FKCache instance");
        } catch (CacheAcquireException ex) {
            assertEquals(counter, DistributedCacheFactoryMock.getCounter());
        }
        
        logger.setLevel(level);
        
        try {
            DistributedCacheFactoryMock.setException(null);
            c.initialize(DistributedCacheFactoryMock.class.getName(), params);
            assertEquals(counter + 2, DistributedCacheFactoryMock.getCounter());
            assertEquals("dummy cache", DistributedCacheFactoryMock.getName());
            assertEquals("dummy fkcache", c.getName());
        } catch (CacheAcquireException ex) {
            fail("Failed to initialize FKCache instance");
        }
    }

    public void testClose() {
        Cache c = new FKCache();
        int counter = DistributedCacheMock.getCounter();
        
        c.close();
        assertEquals(counter, DistributedCacheMock.getCounter());
    }
}
