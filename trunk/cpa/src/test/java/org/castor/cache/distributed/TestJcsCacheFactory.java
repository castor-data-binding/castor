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
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision$ $Date: 2006-04-29 04:11:14 -0600 (Sat, 29 Apr 2006) $
 * @since 1.0
 */
public final class TestJcsCacheFactory extends TestCase {
    public static Test suite() {
        TestSuite suite = new TestSuite("JcsCacheFactory Tests");

        suite.addTest(new TestJcsCacheFactory("testConstructor"));
        suite.addTest(new TestJcsCacheFactory("testGetCacheType"));
        suite.addTest(new TestJcsCacheFactory("testGetCacheClassName"));
        suite.addTest(new TestJcsCacheFactory("testGetCache"));
        suite.addTest(new TestJcsCacheFactory("testShutdown"));

        return suite;
    }

    public TestJcsCacheFactory(final String name) { super(name); }

    public void testConstructor() {
        CacheFactory cf = new JcsCacheFactory();
        assertTrue(cf instanceof JcsCacheFactory);
    }

    public void testGetCacheType() {
        CacheFactory cf = new JcsCacheFactory();
        assertEquals("jcs", cf.getCacheType());
    }

    public void testGetCacheClassName() {
        CacheFactory cf = new JcsCacheFactory();
        String classname = "org.castor.cache.distributed.JcsCache";
        assertEquals(classname, cf.getCacheClassName());
    }

    public void testGetCache() {
        CacheFactory cf = new JcsCacheFactory();
        try {
            Cache c = cf.getCache(null);
            assertTrue(c instanceof JcsCache);
        } catch (CacheAcquireException ex) {
            fail("Failed to get instance of JcsCache from factroy");
        }
    }

    public void testShutdown() {
        CacheFactory cf = new JcsCacheFactory();
        int counter = DistributedCacheFactoryMock.getCounter();
        
        DistributedCacheFactoryMock.setException(null);
        cf.shutdown();
        assertEquals(counter, DistributedCacheFactoryMock.getCounter());
    }
}
