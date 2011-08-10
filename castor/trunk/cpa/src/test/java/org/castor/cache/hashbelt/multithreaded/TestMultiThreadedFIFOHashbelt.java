package org.castor.cache.hashbelt.multithreaded;

import java.util.Properties;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.castor.cache.Cache;
import org.castor.cache.CacheAcquireException;
import org.castor.cache.hashbelt.FIFOHashbelt;

/**
 * Test multithreaded FIFOHashbelt.
 */
public final class TestMultiThreadedFIFOHashbelt extends AbstractTestMultiThreadedHashbelt {
    
    public static Test suite() {
        TestSuite suite = new TestSuite("MultiThreadedFIFOHashbelt Tests");

        suite.addTest(new TestMultiThreadedFIFOHashbelt("testPutThenGet"));
        suite.addTest(new TestMultiThreadedFIFOHashbelt("testGetThenPut"));
        suite.addTest(new TestMultiThreadedFIFOHashbelt("testContainsKey"));
        suite.addTest(new TestMultiThreadedFIFOHashbelt("testContainsValue"));
        suite.addTest(new TestMultiThreadedFIFOHashbelt("testClear"));
        suite.addTest(new TestMultiThreadedFIFOHashbelt("testSize"));
        suite.addTest(new TestMultiThreadedFIFOHashbelt("testIsEmpty"));
        suite.addTest(new TestMultiThreadedFIFOHashbelt("testRemove"));
        suite.addTest(new TestMultiThreadedFIFOHashbelt("testPutAll"));
        suite.addTest(new TestMultiThreadedFIFOHashbelt("testKeySet"));
        suite.addTest(new TestMultiThreadedFIFOHashbelt("testValues"));
        suite.addTest(new TestMultiThreadedFIFOHashbelt("testEntrySet"));
        
        return suite;
    }

    public TestMultiThreadedFIFOHashbelt(final String name) {
        super(name);
    }

    @Override
    protected Cache initialize() throws CacheAcquireException {
        Cache cache = new FIFOHashbelt();
        Properties params = new Properties();
        params.put(Cache.PARAM_NAME, "dummy1");
        cache.initialize(params);

        return cache;
    }
}
