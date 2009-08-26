/*
 * Copyright 2009 Udai Gupta, Ralf Joachim
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
package org.castor.cpa.test.test07;

import java.sql.SQLException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.castor.cpa.test.framework.CPATestCase;
import org.castor.cpa.test.framework.xml.types.DatabaseEngineType;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.JDOManager;
import org.exolab.castor.jdo.PersistenceException;

/**
 * This is a concurrent stress test. This test creates one writing thread
 * that continuously creates, loads and modifies data objects. It loads 
 * and test if the modifications has succeed, loads again and removes 
 * data objects and creates a new object with the same identity again....; 
 * multiple read threads are created and continuously read the data objects 
 * from transactions and commit the transactions without modifying any
 * data object. The 'read and commit' actions essentially lock and unlock 
 * the data object. These tests pass if all modifications to data objects 
 * via the write thread is properly persisted and there is no deadlock 
 * occurring. Passing the tests confirm Castor JDO properly lock and release 
 * objects. 
 * <p>
 * Tests are performed on all four different cache types. (note, these tests 
 * may failed if the number of JDBC connections available to Castor JDO is 
 * too small. To resolve the problem, reduce the number of read threads or 
 * increase the available connections.)
 */
public final class TestCacheLeakage extends CPATestCase {
    /**
     * The <a href="http://jakarta.apache.org/commons/logging/">Jakarta
     * Commons Logging</a> instance used for all logging.
     */
    private static final Log LOG = LogFactory.getLog(TestCacheLeakage.class);
    
    private static final int COUNT_LIMITED = 0;

    private static final int TIME_LIMITED  = 1;

    private static final int NO_CACHE      = 2;

    private static final int UNLIMITED     = 3;

    /** Number of target data objects to be created and deleted */
    private static final int NUM_OF_CREATE_DELETE = 10;

    /** Number of trial of loads and releases on each object */
    private static final int NUM_OF_READ = 50;

    /** Number of load and releases of read threads to race on target */
    private static final int NUM_OF_READ_THREADS = 5;

    private static final String DBNAME = "test07";
    private static final String MAPPING = "/org/castor/cpa/test/test07/mapping.xml";
    private Database _db;
    
    private JDOManager _jdo;

    /** The java class of the target data objects */
    private static Class<?> _classType;

    /** The cache type used in the current test */
    private static int _cacheType;

    /** Indicates leakage detected */
    private static boolean _errLeak;

    /** Indicates error detected */
    private static boolean _errCount;

    /**
     * Constructor
     *
     * @param category the test suite of these test cases
     */
    public TestCacheLeakage(final String name) {
        super(name);
        _errLeak = false;
        _errCount = false;
    }

    // Test are only included/excluded for engines that have been tested with this test suite.

    public boolean include(final DatabaseEngineType engine) {
        return (engine == DatabaseEngineType.DERBY)
            || (engine == DatabaseEngineType.MYSQL);
    }

    /**
     * Get a JDO Database and get a JDBC connection
     */
    public void setUp() throws Exception {
        _jdo = getJDOManager(DBNAME, MAPPING);
        _db = _jdo.getDatabase();
    }

    /**
     * Run the stress test for four different cache setting
     */
    public void testCacheLeakage() throws Exception {
        _cacheType = TIME_LIMITED;
        runOnce();

        _cacheType = NO_CACHE;
        runOnce();

        _cacheType = UNLIMITED;
        runOnce();

        _cacheType = COUNT_LIMITED; 
        runOnce();

        assertTrue("Element leak not detected!", !_errLeak);
        assertTrue("Race condition not happened!", !_errCount);
    }

    public void tearDown() throws PersistenceException, SQLException {
        if (_db.isActive()) { _db.rollback(); }
        _db.close();
    }

    /**
     * Helper class to run the stress tests once for a cache type.
     */
    public void runOnce() throws Exception {
        // clear the table
        _db.begin();
        int del = _db.getJdbcConnection().createStatement().executeUpdate(
                "DELETE FROM test07_race");
        _db.commit();
        LOG.debug("row(s) deleted in table core_race: " + del);


        // set the className and classType to be used
        switch (_cacheType) {
        case COUNT_LIMITED:
            _classType = RaceCount.class;
            break;
        case TIME_LIMITED:
            _classType = RaceTime.class;
            break;
        case NO_CACHE:
            _classType = RaceNone.class;
            break;
        case UNLIMITED:
            _classType = RaceUnlimited.class;
            break;
        default:
            LOG.error("Unknown cache type");
        }

        CreateDeleteThread cdThread = new CreateDeleteThread(
                this, _jdo, _cacheType, NUM_OF_CREATE_DELETE);

        ReadThread[] rThread =  new ReadThread[NUM_OF_READ_THREADS];
        for (int i = 0; i < NUM_OF_READ_THREADS; i++) {
            rThread[i] = new ReadThread(this, cdThread, _jdo, NUM_OF_READ);
            rThread[i].start();
        }

        cdThread.start();
        
        // Polling the test case to see if it is finished
        try {
            while (!cdThread.isDone()) {
                Thread.sleep(500);
            }

            // Joins all the finished threads
            cdThread.join();
            for (int i = 0; i < NUM_OF_READ_THREADS; i++) {
                rThread[i].join();
            }
        } catch (InterruptedException ex) {
            fail(ex.toString());
        }
    }

    public static Class<?> getClassType() {
        return _classType;
    }

    public static void setClassType(final Class<?> classType) {
        _classType = classType;
    }

    public static int getCacheType() {
        return _cacheType;
    }

    public static void setCacheType(final int cacheType) {
        _cacheType = cacheType;
    }

    public static boolean isErrLeak() {
        return _errLeak;
    }

    public static void setErrLeak(final boolean errLeak) {
        _errLeak = errLeak;
    }

    public static boolean isErrCount() {
        return _errCount;
    }

    public static void setErrCount(final boolean errCount) {
        _errCount = errCount;
    }
}