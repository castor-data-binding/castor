package org.castor.cache.hashbelt.multithreaded;

import java.util.Properties;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.castor.cache.Cache;
import org.castor.cache.CacheAcquireException;
import org.castor.cache.hashbelt.LRUHashbelt;

/**
 * Test multithreaded LRUHashbelt.
 */
public final class TestMultiThreadedLRUHashbelt extends AbstractTestMultiThreadedHashbelt {

    public static Test suite() {
        TestSuite suite = new TestSuite("MultiThreadedLRUHashbelt Tests");

        suite.addTest(new TestMultiThreadedLRUHashbelt("testPutThenGet"));
        suite.addTest(new TestMultiThreadedLRUHashbelt("testGetThenPut"));
        suite.addTest(new TestMultiThreadedLRUHashbelt("testContainsKey"));
        suite.addTest(new TestMultiThreadedLRUHashbelt("testContainsValue"));
        suite.addTest(new TestMultiThreadedLRUHashbelt("testClear"));
        suite.addTest(new TestMultiThreadedLRUHashbelt("testSize"));
        suite.addTest(new TestMultiThreadedLRUHashbelt("testIsEmpty"));
        suite.addTest(new TestMultiThreadedLRUHashbelt("testRemove"));
        suite.addTest(new TestMultiThreadedLRUHashbelt("testPutAll"));
        suite.addTest(new TestMultiThreadedLRUHashbelt("testKeySet"));
        suite.addTest(new TestMultiThreadedLRUHashbelt("testValues"));
        suite.addTest(new TestMultiThreadedLRUHashbelt("testEntrySet"));
        
        return suite;
    }

    public TestMultiThreadedLRUHashbelt(final String name) {
        super(name);
    }

    @Override
    protected Cache<String, String> initialize() throws CacheAcquireException {
        Cache<String, String> cache = new LRUHashbelt<String, String>();
        Properties params = new Properties();
        params.put(Cache.PARAM_NAME, "dummy1");
        cache.initialize(params);

        return cache;
    }

}
