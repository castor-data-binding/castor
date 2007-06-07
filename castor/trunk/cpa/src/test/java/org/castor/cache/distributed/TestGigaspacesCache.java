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

import junit.framework.TestCase;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.castor.cache.Cache;
import org.castor.cache.CacheAcquireException;

/**
 * JUnit test case for Gigaspaces cache 
 * @author <a href="mailto:werner DOT guttmann AT gmx DOT net">Werner Guttmann</a>
 * @version $Revision: 5951 $ $Date: 2006-04-29 04:11:14 -0600 (Sat, 29 Apr 2006) $
 * @since 1.0.1
 */
public final class TestGigaspacesCache extends TestCase {
    private static final boolean DISABLE_LOGGING = true;
    
    public TestGigaspacesCache(final String name) { 
        super(name); 
    }

    public void testStaticProperties() {
        assertEquals("gigaspaces", GigaspacesCache.TYPE);
        assertEquals("com.j_spaces.map.CacheFinder", GigaspacesCache.IMPLEMENTATION);
    }

    public void testConstructor() {
        Cache c = new GigaspacesCache();
        assertTrue(c instanceof GigaspacesCache);
    }

    public void testGetType() {
        Cache c = new GigaspacesCache();
        assertEquals("gigaspaces", c.getType());
    }

    public void testInitialize() {
        Logger logger = Logger.getLogger(GigaspacesCache.class);
        Level level = logger.getLevel();
        
        GigaspacesCache c = new GigaspacesCache();
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
        
//        try {
//            DistributedCacheFactoryMock.setException(null);
//            c.initialize(DistributedCacheFactoryMock.class.getName(), params);
//            assertEquals(counter + 1, DistributedCacheFactoryMock.getCounter());
//            assertEquals("dummy gigaspaces cache", DistributedCacheFactoryMock.getName());
//            assertEquals("dummy gigaspacescache", c.getName());
//        } catch (CacheAcquireException ex) {
//            fail("Failed to initialize GigaspacesCache instance");
//        }
    }

//    public void testClose() {
//        Logger logger = Logger.getLogger(GigaspacesCache.class);
//        Level level = logger.getLevel();
//        
//        GigaspacesCache c = new GigaspacesCache();
//        int counter = DistributedCacheMock.getCounter();
//        
//        c.close();
//        assertEquals(counter, DistributedCacheMock.getCounter());
//        
//        Properties params = new Properties();
//        params.put(Cache.PARAM_NAME, "dummy coherence cache");
//        
//        try {
//            DistributedCacheFactoryMock.setException(null);
//            c.initialize(DistributedCacheFactoryMock.class.getName(), params);
//            assertEquals(counter, DistributedCacheMock.getCounter());
//        } catch (CacheAcquireException ex) {
//            fail("Failed to initialize CoherenceCache instance");
//        }
//        
//        if (DISABLE_LOGGING) { logger.setLevel(Level.FATAL); }
//        
//        DistributedCacheMock.setException(new Exception("dummy"));
//        c.close();
//        assertEquals(counter, DistributedCacheMock.getCounter());
//        
//        logger.setLevel(level);
//        
//        DistributedCacheMock.setException(null);
//        c.close();
//        assertEquals(counter + 1, DistributedCacheMock.getCounter());
//    }
//    
//    private Cache initialize() throws CacheAcquireException {
//        
//        GigaspacesCacheFactory factory = new GigaspacesCacheFactory();
//        Cache c = factory.getCache(getClass().getClassLoader());
//        
//        Properties params = new Properties();
//        params.put(Cache.PARAM_NAME, "dummy gigaspaces cache");
//
//        c.initialize(params);
//        
//        return c;
//    }
//    
//    public void testGetOnEmptyCache() throws CacheAcquireException {
//        
//        Cache c = initialize();
//        
//        Object value = c.get("first key");
//        assertNull(value);
//    }    
//
//    public void testPutAndGet() throws CacheAcquireException {
//        
//        Cache c = initialize();
//        
//        c.put("first key", "first value");
//        Object value = c.get("first key");
//        assertNotNull(value);
//    }    
//
//    public void testPutAndGetAndRemove() throws CacheAcquireException {
//        
//        Cache c = initialize();
//        
//        c.put("first key", "first value");
//        
//        Object value = c.get("first key");
//        assertNotNull(value);
//        
//        c.remove("first key");
//        value = c.get("first key");
//        assertNull(value);
//        
//    }    

}
