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
public final class TestLRUHashbeltFactory extends TestCase {
    public static Test suite() {
        TestSuite suite = new TestSuite("LRUHashbeltFactory Tests");

        suite.addTest(new TestLRUHashbeltFactory("testConstructor"));
        suite.addTest(new TestLRUHashbeltFactory("testGetCacheType"));
        suite.addTest(new TestLRUHashbeltFactory("testGetCacheClassName"));
        suite.addTest(new TestLRUHashbeltFactory("testGetCache"));
        suite.addTest(new TestLRUHashbeltFactory("testShutdown"));

        return suite;
    }

    public TestLRUHashbeltFactory(final String name) { super(name); }

    public void testConstructor() {
        CacheFactory cf = new LRUHashbeltFactory();
        assertTrue(cf instanceof LRUHashbeltFactory);
    }

    public void testGetCacheType() {
        CacheFactory cf = new LRUHashbeltFactory();
        assertEquals("lru", cf.getCacheType());
    }

    public void testGetCacheClassName() {
        CacheFactory cf = new LRUHashbeltFactory();
        String classname = "org.castor.cache.hashbelt.LRUHashbelt";
        assertEquals(classname, cf.getCacheClassName());
    }

    public void testGetCache() {
        CacheFactory cf = new LRUHashbeltFactory();
        try {
            Cache c = cf.getCache(null);
            assertTrue(c instanceof LRUHashbelt);
        } catch (CacheAcquireException ex) {
            fail("Failed to get instance of LRUHashbelt from factroy");
        }
    }

    public void testShutdown() {
        CacheFactory cf = new LRUHashbeltFactory();
        cf.shutdown();
    }
}
