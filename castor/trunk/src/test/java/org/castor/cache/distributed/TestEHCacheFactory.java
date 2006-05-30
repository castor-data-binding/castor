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

import org.castor.cache.Cache;
import org.castor.cache.CacheAcquireException;
import org.castor.cache.CacheFactory;

/**
 * @author <a href="mailto:werner DOT guttmann AT gmx DOT net">Werner Guttmann</a>
 * @version $Revision$ $Date: 2006-01-06 13:10:47 0100 (Fr, 06 Jän 2006) $
 * @since 1.0.1
 */
public final class TestEHCacheFactory extends TestCase {
    
    public static Test suite() {
        TestSuite suite = new TestSuite("EHCacheFactory Tests");

        suite.addTest(new TestEHCacheFactory("testConstructor"));
        suite.addTest(new TestEHCacheFactory("testGetCacheType"));
        suite.addTest(new TestEHCacheFactory("testGetCacheClassName"));
        suite.addTest(new TestEHCacheFactory("testGetCache"));
        suite.addTest(new TestEHCacheFactory("testShutdown"));

        return suite;
    }

    public TestEHCacheFactory(final String name) { super(name); }

    public void testConstructor() {
        CacheFactory cf = new EHCacheFactory();
        assertTrue(cf instanceof EHCacheFactory);
    }

    public void testGetCacheType() {
        CacheFactory cf = new EHCacheFactory();
        assertEquals("ehcache", cf.getCacheType());
    }

    public void testGetCacheClassName() {
        CacheFactory cf = new EHCacheFactory();
        String classname = "org.castor.cache.distributed.EHCache";
        assertEquals(classname, cf.getCacheClassName());
    }

    public void testGetCache() {
        CacheFactory cf = new EHCacheFactory();
        try {
            Cache c = cf.getCache(null);
            assertTrue(c instanceof EHCache);
        } catch (CacheAcquireException ex) {
            fail("Failed to get instance of EHCache from factroy");
        }
    }

    public void testShutdown() {
        CacheFactory cf = new EHCacheFactory();
        int counter = DistributedCacheFactoryMock.getCounter();
        
        DistributedCacheFactoryMock.setException(null);
        cf.shutdown();
        assertEquals(counter, DistributedCacheFactoryMock.getCounter());
    }
}