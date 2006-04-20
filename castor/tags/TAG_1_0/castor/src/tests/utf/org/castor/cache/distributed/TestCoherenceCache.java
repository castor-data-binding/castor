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

import java.util.Properties;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import org.castor.cache.Cache;
import org.castor.cache.CacheAcquireException;
import org.castor.cache.distributed.CoherenceCache;

/**
 * @author <a href="mailto:ralf DOT joachim AT syscon-world DOT de">Ralf Joachim</a>
 * @version $Revision$ $Date$
 * @since 1.0
 */
public final class TestCoherenceCache extends TestCase {
    private static final boolean DISABLE_LOGGING = true;
    
    public static Test suite() {
        TestSuite suite = new TestSuite("CoherenceCache Tests");

        suite.addTest(new TestCoherenceCache("testStaticProperties"));
        suite.addTest(new TestCoherenceCache("testConstructor"));
        suite.addTest(new TestCoherenceCache("testGetType"));
        suite.addTest(new TestCoherenceCache("testInitialize"));
        suite.addTest(new TestCoherenceCache("testClose"));

        return suite;
    }

    public TestCoherenceCache(final String name) { super(name); }

    public void testStaticProperties() {
        assertEquals("coherence", CoherenceCache.TYPE);
        assertEquals("com.tangosol.net.CacheFactory", CoherenceCache.IMPLEMENTATION);
    }

    public void testConstructor() {
        Cache c = new CoherenceCache();
        assertTrue(c instanceof CoherenceCache);
    }

    public void testGetType() {
        Cache c = new CoherenceCache();
        assertEquals("coherence", c.getType());
    }

    public void testInitialize() {
        Logger logger = Logger.getLogger(CoherenceCache.class);
        Level level = logger.getLevel();
        
        CoherenceCache c = new CoherenceCache();
        int counter = DistributedCacheFactoryMock.getCounter();
        
        Properties params = new Properties();
        params.put(Cache.PARAM_NAME, "dummy coherence cache");
        
        if (DISABLE_LOGGING) { logger.setLevel(Level.FATAL); }

        try {
            DistributedCacheFactoryMock.setException(new Exception("dummy"));
            c.initialize(DistributedCacheFactoryMock.class.getName(), params);
            fail("Failed to trow exception at initialize of CoherenceCache instance");
        } catch (CacheAcquireException ex) {
            assertEquals(counter, DistributedCacheFactoryMock.getCounter());
        }
        
        logger.setLevel(level);
        
        try {
            DistributedCacheFactoryMock.setException(null);
            c.initialize(DistributedCacheFactoryMock.class.getName(), params);
            assertEquals(counter + 1, DistributedCacheFactoryMock.getCounter());
            assertEquals("dummy coherence cache", DistributedCacheFactoryMock.getName());
            assertEquals("dummy coherence cache", c.getName());
        } catch (CacheAcquireException ex) {
            fail("Failed to initialize CoherenceCache instance");
        }
    }

    public void testClose() {
        Logger logger = Logger.getLogger(CoherenceCache.class);
        Level level = logger.getLevel();
        
        CoherenceCache c = new CoherenceCache();
        int counter = DistributedCacheMock.getCounter();
        
        c.close();
        assertEquals(counter, DistributedCacheMock.getCounter());
        
        Properties params = new Properties();
        params.put(Cache.PARAM_NAME, "dummy coherence cache");
        
        try {
            DistributedCacheFactoryMock.setException(null);
            c.initialize(DistributedCacheFactoryMock.class.getName(), params);
            assertEquals(counter, DistributedCacheMock.getCounter());
        } catch (CacheAcquireException ex) {
            fail("Failed to initialize CoherenceCache instance");
        }
        
        if (DISABLE_LOGGING) { logger.setLevel(Level.FATAL); }
        
        DistributedCacheMock.setException(new Exception("dummy"));
        c.close();
        assertEquals(counter, DistributedCacheMock.getCounter());
        
        logger.setLevel(level);
        
        DistributedCacheMock.setException(null);
        c.close();
        assertEquals(counter + 1, DistributedCacheMock.getCounter());
    }
}
