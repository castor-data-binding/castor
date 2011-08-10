package org.castor.cache.hashbelt.multithreaded;

import java.util.Properties;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.castor.cache.Cache;
import org.castor.cache.CacheAcquireException;
import org.castor.cache.hashbelt.AbstractHashbelt;
import org.castor.cache.hashbelt.LRUHashbelt;
import org.castor.cache.hashbelt.container.FastIteratingContainer;

/**
 * Test multithreaded LRUHashbelt with FastIteratingContainer.
 */
public final class TestMultiThreadedLRUHashbeltWithFastIteratingContainer
        extends AbstractTestMultiThreadedHashbelt {

    public static Test suite() {
        TestSuite suite = new TestSuite("MultiThreadedLRUHashbeltWithFastIteratingContainer Tests");

        suite.addTest(new TestMultiThreadedLRUHashbeltWithFastIteratingContainer(
                "testPutThenGet"));
        suite.addTest(new TestMultiThreadedLRUHashbeltWithFastIteratingContainer(
                "testGetThenPut"));
        suite.addTest(new TestMultiThreadedLRUHashbeltWithFastIteratingContainer(
                "testContainsKey"));
        suite.addTest(new TestMultiThreadedLRUHashbeltWithFastIteratingContainer(
                "testContainsValue"));
        suite.addTest(new TestMultiThreadedLRUHashbeltWithFastIteratingContainer(
                "testClear"));
        suite.addTest(new TestMultiThreadedLRUHashbeltWithFastIteratingContainer(
                "testSize"));
        suite.addTest(new TestMultiThreadedLRUHashbeltWithFastIteratingContainer(
                "testIsEmpty"));
        suite.addTest(new TestMultiThreadedLRUHashbeltWithFastIteratingContainer(
                "testRemove"));
        suite.addTest(new TestMultiThreadedLRUHashbeltWithFastIteratingContainer(
                "testPutAll"));
        suite.addTest(new TestMultiThreadedLRUHashbeltWithFastIteratingContainer(
                "testKeySet"));
        suite.addTest(new TestMultiThreadedLRUHashbeltWithFastIteratingContainer(
                "testValues"));
        suite.addTest(new TestMultiThreadedLRUHashbeltWithFastIteratingContainer(
                "testEntrySet"));

        return suite;
    }

    public TestMultiThreadedLRUHashbeltWithFastIteratingContainer(final String name) {
        super(name);
    }

    @Override
    protected Cache initialize() throws CacheAcquireException {
        Cache cache = new LRUHashbelt();
        Properties params = new Properties();
        params.put(Cache.PARAM_NAME, "dummy1");
        params.put(AbstractHashbelt.PARAM_CONTAINER_CLASS, FastIteratingContainer.class.getName());
        cache.initialize(params);

        return cache;
    }

}
