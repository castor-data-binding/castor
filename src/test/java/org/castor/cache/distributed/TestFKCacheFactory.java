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
 * @author <a href="mailto:ralf DOT joachim AT syscon-world DOT de">Ralf Joachim</a>
 * @version $Revision$ $Date$
 * @since 1.0
 */
public final class TestFKCacheFactory extends TestCase {
    public static Test suite() {
        TestSuite suite = new TestSuite("FKCacheFactory Tests");

        suite.addTest(new TestFKCacheFactory("testConstructor"));
        suite.addTest(new TestFKCacheFactory("testGetCacheType"));
        suite.addTest(new TestFKCacheFactory("testGetCacheClassName"));
        suite.addTest(new TestFKCacheFactory("testGetCache"));
        suite.addTest(new TestFKCacheFactory("testShutdown"));

        return suite;
    }

    public TestFKCacheFactory(final String name) { super(name); }

    public void testConstructor() {
        CacheFactory cf = new FKCacheFactory();
        assertTrue(cf instanceof FKCacheFactory);
    }

    public void testGetCacheType() {
        CacheFactory cf = new FKCacheFactory();
        assertEquals("fkcache", cf.getCacheType());
    }

    public void testGetCacheClassName() {
        CacheFactory cf = new FKCacheFactory();
        String classname = "org.castor.cache.distributed.FKCache";
        assertEquals(classname, cf.getCacheClassName());
    }

    public void testGetCache() {
        CacheFactory cf = new FKCacheFactory();
        try {
            Cache c = cf.getCache(null);
            assertTrue(c instanceof FKCache);
        } catch (CacheAcquireException ex) {
            fail("Failed to get instance of FKCache from factroy");
        }
    }

    public void testShutdown() {
        CacheFactory cf = new FKCacheFactory();
        int counter = DistributedCacheFactoryMock.getCounter();
        
        DistributedCacheFactoryMock.setException(null);
        cf.shutdown();
        assertEquals(counter, DistributedCacheFactoryMock.getCounter());
    }
}
