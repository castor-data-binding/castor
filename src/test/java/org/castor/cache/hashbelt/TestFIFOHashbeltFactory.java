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
package org.castor.cache.hashbelt;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.castor.cache.Cache;
import org.castor.cache.CacheAcquireException;
import org.castor.cache.CacheFactory;

/**
 * @author <a href="mailto:ralf DOT joachim AT syscon-world DOT de">Ralf Joachim</a>
 * @version $Revision$ $Date: 2006-04-29 04:11:14 -0600 (Sat, 29 Apr 2006) $
 * @since 1.0
 */
public final class TestFIFOHashbeltFactory extends TestCase {
    public static Test suite() {
        TestSuite suite = new TestSuite("FIFOHashbeltFactory Tests");

        suite.addTest(new TestFIFOHashbeltFactory("testConstructor"));
        suite.addTest(new TestFIFOHashbeltFactory("testGetCacheType"));
        suite.addTest(new TestFIFOHashbeltFactory("testGetCacheClassName"));
        suite.addTest(new TestFIFOHashbeltFactory("testGetCache"));
        suite.addTest(new TestFIFOHashbeltFactory("testShutdown"));

        return suite;
    }

    public TestFIFOHashbeltFactory(final String name) { super(name); }

    public void testConstructor() {
        CacheFactory cf = new FIFOHashbeltFactory();
        assertTrue(cf instanceof FIFOHashbeltFactory);
    }

    public void testGetCacheType() {
        CacheFactory cf = new FIFOHashbeltFactory();
        assertEquals("fifo", cf.getCacheType());
    }

    public void testGetCacheClassName() {
        CacheFactory cf = new FIFOHashbeltFactory();
        String classname = "org.castor.cache.hashbelt.FIFOHashbelt";
        assertEquals(classname, cf.getCacheClassName());
    }

    public void testGetCache() {
        CacheFactory cf = new FIFOHashbeltFactory();
        try {
            Cache c = cf.getCache(null);
            assertTrue(c instanceof FIFOHashbelt);
        } catch (CacheAcquireException ex) {
            fail("Failed to get instance of FIFOHashbelt from factroy");
        }
    }

    public void testShutdown() {
        CacheFactory cf = new FIFOHashbeltFactory();
        cf.shutdown();
    }
}
