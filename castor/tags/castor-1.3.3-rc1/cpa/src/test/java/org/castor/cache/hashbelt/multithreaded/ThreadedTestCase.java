
package org.castor.cache.hashbelt.multithreaded;

import junit.framework.TestCase;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Abstract base class for all multithreaded hashbelt cache test cases.
 * <br/>
 */
public abstract class ThreadedTestCase extends TestCase {
    //--------------------------------------------------------------------------
    
    /** The <a href="http://jakarta.apache.org/commons/logging/">Jakarta
     *  Commons Logging</a> instance used for all logging. */
    private static final Log LOG = LogFactory.getLog(ThreadedTestCase.class);
    
    
    /** Array of threads to execute. */
    private Thread[] _threads = null;

    //--------------------------------------------------------------------------

    /**
     * Default constructor.
     */
    public ThreadedTestCase() {
       super();
    }
    
    /**
     * Construct ThreadedTestCase with given name.
     * 
     * @param name Name of the test.
     */
    public ThreadedTestCase(final String name) {
       super(name);
    }
    
    //--------------------------------------------------------------------------

    /**
     * Create threads for all given Runnables, start them and wait until they died all.
     * 
     * @param runnables Array of Runnables to execute with this test case.
     */
    protected final void runTestRunnables(final Runnable[] runnables) {
        if (runnables == null) {
            throw new IllegalArgumentException("No runnables to execute.");
        }
         
        // Initialize array of threads.
        _threads = new Thread[runnables.length];
        for (int i = 0; i < _threads.length; i++) {
            _threads[i] = new Thread(runnables[i]);
        }

        // Start all threads of thread array.
        for (int i = 0; i < _threads.length; i++) {
            _threads[i].start();
        }
        
        // Wait for all threads to die.
        try {
            for (int i = 0; i < _threads.length; i++) {
                _threads[i].join();
            }
        } catch (InterruptedException ignore) {
            LOG.debug("Thread join interrupted.");
        }
        
        _threads = null;
    }
    
    //--------------------------------------------------------------------------
}
