package org.castor.cache.hashbelt.multithreaded;

import java.util.Properties;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.castor.cache.Cache;
import org.castor.cache.CacheAcquireException;
import org.castor.cache.hashbelt.AbstractHashbelt;
import org.castor.cache.hashbelt.LRUHashbelt;
import org.castor.cache.hashbelt.container.WeakReferenceContainer;

/**
 * Test multithreaded LRUHashbelt with WeadReferenceContainer.
 */
public final class TestMultiThreadedLRUHashbeltWithWeadReferenceContainer
        extends AbstractTestMultiThreadedHashbelt {

    public static Test suite() {
        TestSuite suite = new TestSuite("MultiThreadedLRUHashbeltWithWeadReferenceContainer Tests");

        suite.addTest(new TestMultiThreadedLRUHashbeltWithWeadReferenceContainer(
                "testPutThenGet"));
        suite.addTest(new TestMultiThreadedLRUHashbeltWithWeadReferenceContainer(
                "testGetThenPut"));
        suite.addTest(new TestMultiThreadedLRUHashbeltWithWeadReferenceContainer(
                "testContainsKey"));
        suite.addTest(new TestMultiThreadedLRUHashbeltWithWeadReferenceContainer(
                "testContainsValue"));
        suite.addTest(new TestMultiThreadedLRUHashbeltWithWeadReferenceContainer(
                "testClear"));
        suite.addTest(new TestMultiThreadedLRUHashbeltWithWeadReferenceContainer(
                "testSize"));
        suite.addTest(new TestMultiThreadedLRUHashbeltWithWeadReferenceContainer(
                "testIsEmpty"));
        suite.addTest(new TestMultiThreadedLRUHashbeltWithWeadReferenceContainer(
                "testRemove"));
        suite.addTest(new TestMultiThreadedLRUHashbeltWithWeadReferenceContainer(
                "testPutAll"));
        suite.addTest(new TestMultiThreadedLRUHashbeltWithWeadReferenceContainer(
                "testKeySet"));
        suite.addTest(new TestMultiThreadedLRUHashbeltWithWeadReferenceContainer(
                "testValues"));
        suite.addTest(new TestMultiThreadedLRUHashbeltWithWeadReferenceContainer(
                "testEntrySet"));

        return suite;
    }

    public TestMultiThreadedLRUHashbeltWithWeadReferenceContainer(final String name) {
        super(name);
    }

    @Override
    protected Cache<String, String> initialize() throws CacheAcquireException {
        Cache<String, String> cache = new LRUHashbelt<String, String>();
        Properties params = new Properties();
        params.put(Cache.PARAM_NAME, "dummy1");
        params.put(AbstractHashbelt.PARAM_CONTAINER_CLASS, WeakReferenceContainer.class.getName());
        cache.initialize(params);

        return cache;
    }

}
