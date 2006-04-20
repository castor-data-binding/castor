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
package utf.org.castor.cache.simple;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.castor.cache.Cache;
import org.castor.cache.CacheAcquireException;
import org.castor.cache.CacheFactory;
import org.castor.cache.simple.NoCache;
import org.castor.cache.simple.NoCacheFactory;

/**
 * @author <a href="mailto:ralf DOT joachim AT syscon-world DOT de">Ralf Joachim</a>
 * @version $Revision$ $Date$
 * @since 1.0
 */
public final class TestNoCacheFactory extends TestCase {
    public static Test suite() {
        TestSuite suite = new TestSuite("NoCacheFactory Tests");

        suite.addTest(new TestNoCacheFactory("testConstructor"));
        suite.addTest(new TestNoCacheFactory("testGetCacheType"));
        suite.addTest(new TestNoCacheFactory("testGetCacheClassName"));
        suite.addTest(new TestNoCacheFactory("testGetCache"));
        suite.addTest(new TestNoCacheFactory("testShutdown"));

        return suite;
    }

    public TestNoCacheFactory(final String name) { super(name); }

    public void testConstructor() {
        CacheFactory cf = new NoCacheFactory();
        assertTrue(cf instanceof NoCacheFactory);
    }

    public void testGetCacheType() {
        CacheFactory cf = new NoCacheFactory();
        assertEquals("none", cf.getCacheType());
    }

    public void testGetCacheClassName() {
        CacheFactory cf = new NoCacheFactory();
        String classname = "org.castor.cache.simple.NoCache";
        assertEquals(classname, cf.getCacheClassName());
    }

    public void testGetCache() {
        CacheFactory cf = new NoCacheFactory();
        try {
            Cache c = cf.getCache(null);
            assertTrue(c instanceof NoCache);
        } catch (CacheAcquireException ex) {
            fail("Failed to get instance of NoCache from factroy");
        }
    }

    public void testShutdown() {
        CacheFactory cf = new NoCacheFactory();
        cf.shutdown();
    }
}
