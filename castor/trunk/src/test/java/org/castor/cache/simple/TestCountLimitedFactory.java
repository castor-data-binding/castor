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
package org.castor.cache.simple;

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
public final class TestCountLimitedFactory extends TestCase {
    public static Test suite() {
        TestSuite suite = new TestSuite("CountLimitedFactory Tests");

        suite.addTest(new TestCountLimitedFactory("testConstructor"));
        suite.addTest(new TestCountLimitedFactory("testGetCacheType"));
        suite.addTest(new TestCountLimitedFactory("testGetCacheClassName"));
        suite.addTest(new TestCountLimitedFactory("testGetCache"));
        suite.addTest(new TestCountLimitedFactory("testShutdown"));

        return suite;
    }

    public TestCountLimitedFactory(final String name) { super(name); }

    public void testConstructor() {
        CacheFactory cf = new CountLimitedFactory();
        assertTrue(cf instanceof CountLimitedFactory);
    }

    public void testGetCacheType() {
        CacheFactory cf = new CountLimitedFactory();
        assertEquals("count-limited", cf.getCacheType());
    }

    public void testGetCacheClassName() {
        CacheFactory cf = new CountLimitedFactory();
        String classname = "org.castor.cache.simple.CountLimited";
        assertEquals(classname, cf.getCacheClassName());
    }

    public void testGetCache() {
        CacheFactory cf = new CountLimitedFactory();
        try {
            Cache c = cf.getCache(null);
            assertTrue(c instanceof CountLimited);
        } catch (CacheAcquireException ex) {
            fail("Failed to get instance of CountLimited from factroy");
        }
    }

    public void testShutdown() {
        CacheFactory cf = new CountLimitedFactory();
        cf.shutdown();
    }
}
