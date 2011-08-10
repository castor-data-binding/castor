package org.castor.cache.hashbelt.multithreaded;

import java.util.Properties;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.castor.cache.Cache;
import org.castor.cache.CacheAcquireException;
import org.castor.cache.hashbelt.AbstractHashbelt;
import org.castor.cache.hashbelt.FIFOHashbelt;
import org.castor.cache.hashbelt.container.WeakReferenceContainer;

/**
 * Test multithreaded FIFOHashbelt with WeakReferenceContainer.
 */
public final class TestMultiThreadedFIFOHashbeltWithWeakReferenceContainer
        extends AbstractTestMultiThreadedHashbelt {

    public static Test suite() {
        TestSuite suite = new TestSuite(
                "MultiThreadedFIFOHashbeltWithWeakReferenceContainer Tests");

        suite.addTest(new TestMultiThreadedFIFOHashbeltWithWeakReferenceContainer(
                "testPutThenGet"));
        suite.addTest(new TestMultiThreadedFIFOHashbeltWithWeakReferenceContainer(
                "testGetThenPut"));
        suite.addTest(new TestMultiThreadedFIFOHashbeltWithWeakReferenceContainer(
                "testContainsKey"));
        suite.addTest(new TestMultiThreadedFIFOHashbeltWithWeakReferenceContainer(
                "testContainsValue"));
        suite.addTest(new TestMultiThreadedFIFOHashbeltWithWeakReferenceContainer(
                "testClear"));
        suite.addTest(new TestMultiThreadedFIFOHashbeltWithWeakReferenceContainer(
                "testSize"));
        suite.addTest(new TestMultiThreadedFIFOHashbeltWithWeakReferenceContainer(
                "testIsEmpty"));
        suite.addTest(new TestMultiThreadedFIFOHashbeltWithWeakReferenceContainer(
                "testRemove"));
        suite.addTest(new TestMultiThreadedFIFOHashbeltWithWeakReferenceContainer(
                "testPutAll"));
        suite.addTest(new TestMultiThreadedFIFOHashbeltWithWeakReferenceContainer(
                "testKeySet"));
        suite.addTest(new TestMultiThreadedFIFOHashbeltWithWeakReferenceContainer(
                "testValues"));
        suite.addTest(new TestMultiThreadedFIFOHashbeltWithWeakReferenceContainer(
                "testEntrySet"));

        return suite;
    }

    public TestMultiThreadedFIFOHashbeltWithWeakReferenceContainer(final String name) {
        super(name);
    }

    @Override
    protected Cache initialize() throws CacheAcquireException {
        Cache cache = new FIFOHashbelt();
        Properties params = new Properties();
        params.put(Cache.PARAM_NAME, "dummy1");
        params.put(AbstractHashbelt.PARAM_CONTAINER_CLASS, WeakReferenceContainer.class.getName());
        cache.initialize(params);

        return cache;
    }
}
