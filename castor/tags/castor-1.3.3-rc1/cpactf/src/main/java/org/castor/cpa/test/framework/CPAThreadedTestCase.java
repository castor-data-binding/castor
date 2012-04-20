/*
 * Copyright 2010 Ralf Joachim, Clovis Wichoski
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
package org.castor.cpa.test.framework;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Abstract base class for all multi threaded CPA test cases.
 * <br/>
 * Based on http://www.javaworld.com/jw-12-2000/jw-1221-junit.html?page=6
 * 
 * @author <a href="mailto:clovis AT supridatta DOT com DOT br">Clovis Wichoski</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision$ $Date$
 */
public abstract class CPAThreadedTestCase extends CPATestCase {
    //--------------------------------------------------------------------------
    
    /** The <a href="http://jakarta.apache.org/commons/logging/">Jakarta
     *  Commons Logging</a> instance used for all logging. */
    private static final Log LOG = LogFactory.getLog(CPAThreadedTestCase.class);
    
    
    /** Array of threads to execute. */
    private Thread[] _threads = null;

    //--------------------------------------------------------------------------

    /**
     * Default constructor.
     */
    public CPAThreadedTestCase() {
       super();
    }
    
    /**
     * Construct CPAThreadedTestCase with given name.
     * 
     * @param name Name of the test.
     */
    public CPAThreadedTestCase(final String name) {
       super(name);
    }
    
    //--------------------------------------------------------------------------

    /**
     * Create threads for all given Runnables, start them and wait until they died all.
     * 
     * @param runnables Array of Runnables to execute with this test case.
     */
    protected final void runTestRunnables(final CPAThreadedTestRunnable[] runnables) {
        if (runnables == null) {
            throw new IllegalArgumentException("No runnables to execute.");
        }
         
        // Initialize array of threads.
        _threads = new Thread[runnables.length];
        for (int i = 0; i < _threads.length; i++) {
            runnables[i].setParentTestCase(this);
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
    
    /**
     * Interrupt all Runnables belonging to this test.
     */
    protected final void interruptThreads() {
        for (int i = 0; i < _threads.length; i++) {
            _threads[i].interrupt();
        }
    }
    
    //--------------------------------------------------------------------------
}
