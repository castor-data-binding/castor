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
import org.castor.cache.simple.TimeLimited;
import org.castor.cache.simple.TimeLimitedFactory;

/**
 * @author <a href="mailto:ralf DOT joachim AT syscon-world DOT de">Ralf Joachim</a>
 * @version $Revision$ $Date$
 * @since 1.0
 */
public final class TestTimeLimitedFactory extends TestCase {
    public static Test suite() {
        TestSuite suite = new TestSuite("TimeLimitedFactory Tests");

        suite.addTest(new TestTimeLimitedFactory("testConstructor"));
        suite.addTest(new TestTimeLimitedFactory("testGetCacheType"));
        suite.addTest(new TestTimeLimitedFactory("testGetCacheClassName"));
        suite.addTest(new TestTimeLimitedFactory("testGetCache"));
        suite.addTest(new TestTimeLimitedFactory("testShutdown"));

        return suite;
    }

    public TestTimeLimitedFactory(final String name) { super(name); }

    public void testConstructor() {
        CacheFactory cf = new TimeLimitedFactory();
        assertTrue(cf instanceof TimeLimitedFactory);
    }

    public void testGetCacheType() {
        CacheFactory cf = new TimeLimitedFactory();
        assertEquals("time-limited", cf.getCacheType());
    }

    public void testGetCacheClassName() {
        CacheFactory cf = new TimeLimitedFactory();
        String classname = "org.castor.cache.simple.TimeLimited";
        assertEquals(classname, cf.getCacheClassName());
    }

    public void testGetCache() {
        CacheFactory cf = new TimeLimitedFactory();
        try {
            Cache c = cf.getCache(null);
            assertTrue(c instanceof TimeLimited);
        } catch (CacheAcquireException ex) {
            fail("Failed to get instance of TimeLimited from factroy");
        }
    }

    public void testShutdown() {
        CacheFactory cf = new TimeLimitedFactory();
        cf.shutdown();
    }
}
