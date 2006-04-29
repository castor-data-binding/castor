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
package org.castor.cache;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import org.castor.cache.AbstractCacheFactory;
import org.castor.cache.Cache;
import org.castor.cache.CacheAcquireException;
import org.castor.cache.CacheFactory;

/**
 * @author <a href="mailto:ralf DOT joachim AT syscon-world DOT de">Ralf Joachim</a>
 * @version $Revision$ $Date$
 * @since 1.0
 */
public final class TestAbstractCacheFactory extends TestCase {
    private static final boolean DISABLE_LOGGING = true;
    
    public static Test suite() {
        TestSuite suite = new TestSuite("AbstractCacheFactory Tests");

        suite.addTest(new TestAbstractCacheFactory("testGetCache"));
        suite.addTest(new TestAbstractCacheFactory("testShutdown"));

        return suite;
    }

    public TestAbstractCacheFactory(final String name) { super(name); }

    public void testGetCache() {
        Logger logger = Logger.getLogger(AbstractCacheFactory.class);
        Level level = logger.getLevel();
        
        CacheFactory cf = new CacheFactoryMock();
        
        try {
            Cache c = cf.getCache(null);
            assertTrue(c instanceof CacheMock);
        } catch (CacheAcquireException ex) {
            fail("Failed to get instance of CacheMock from factroy");
        }
        
        try {
            Cache c = cf.getCache(this.getClass().getClassLoader());
            assertTrue(c instanceof CacheMock);
        } catch (CacheAcquireException ex) {
            fail("Failed to get instance of CacheMock from factroy");
        }
        
        if (DISABLE_LOGGING) { logger.setLevel(Level.FATAL); }

        try {
            ((CacheFactoryMock) cf).setCacheClassName("org.castor.cache.UnknownCache");
            cf.getCache(null);
            fail("Should have failed to get instance of CacheMock from factroy");
        } catch (CacheAcquireException ex) {
            assertNotNull(ex.getMessage());
            assertEquals(ClassNotFoundException.class, ex.getCause().getClass());
        }

        logger.setLevel(level);
    }

    public void testShutdown() {
        CacheFactory cf = new CacheFactoryMock();
        cf.shutdown();
    }
}
