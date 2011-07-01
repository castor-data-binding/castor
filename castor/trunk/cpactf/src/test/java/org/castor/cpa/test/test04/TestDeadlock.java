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
package org.castor.cpa.test.test04;

import java.util.Enumeration;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.castor.cpa.test.framework.CPAThreadedTestCase;
import org.castor.cpa.test.framework.CPAThreadedTestRunnable;
import org.castor.cpa.test.framework.xml.types.DatabaseEngineType;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.LockNotGrantedException;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.TransactionAbortedException;
import org.exolab.castor.jdo.TransactionNotInProgressException;
import org.exolab.castor.mapping.AccessMode;

/**
 * Tests for deadlock detection. Will report to the console two concurrent 
 * transactions working on the same objects. The first transaction will 
 * succeed, the second will fail. All three access modes: Shared (aka
 * optimistic locking), Exclusive (aka pessimistic locking) and DbLocked 
 * (premissitic memory locking with DBMS's locking) are covered by these 
 * test cases. These tests passed if LockNotGrantedException is thrown 
 * in the appropriate time for the access mode in action.
 */
public final class TestDeadlock extends CPAThreadedTestCase {
    /**  The <a href="http://jakarta.apache.org/commons/logging/">Jakarta
     * Commons Logging</a> instance used for all logging. */
    private static final Log LOG = LogFactory.getLog(TestDeadlock.class);
    
    /**
     * The time a transaction sleep and wait for another transaction to
     * process
     */
    public static final long WAIT = 2000;

    protected static final String DBNAME = "test04";
    protected static final String MAPPING = "/org/castor/cpa/test/test04/mapping.xml";
    private Database _db;

    /** The java object to be synchronized on */
    protected Object _lock = new Object();

    /**  The JDO database used for first concurrent transactions */
    private Database _firstDb;

    /** The thread that the first transaction is running on */
    private FirstRunnable _firstRunnable;

    public Exception _firstProblem;

    /** The JDO database used for second concurrent transactions */
    private Database _secondDb;

    /** The thread that the second transaction is running on  */
    public SecondRunnable _secondRunnable;

    public Exception _secondProblem;

    /**
     * Constructor
     *
     * @param category The test suit of these test cases
     */
    public TestDeadlock(final String name) {
        super(name);
    }

    // Test are only included/excluded for engines that have been tested with this test suite.

    public boolean include(final DatabaseEngineType engine) {
        return (engine == DatabaseEngineType.DERBY)
            || (engine == DatabaseEngineType.HSQL)
            || (engine == DatabaseEngineType.MYSQL)
            || (engine == DatabaseEngineType.ORACLE)
            || (engine == DatabaseEngineType.POSTGRESQL);
    }

    /**
     * Get the JDO Databases and create data objects into the database
     */
    public void setUp() throws Exception {
        _db = getJDOManager(DBNAME, MAPPING).getDatabase();
        _firstDb = getJDOManager(DBNAME, MAPPING).getDatabase();
        _secondDb = getJDOManager(DBNAME, MAPPING).getDatabase();

        Sample object;
        Enumeration<?> enumeration;
        
        // Open transaction in order to perform JDO operations
        _db.begin();
        
        // Create two objects in the database -- need something to lock
        OQLQuery oql = _db.getOQLQuery("SELECT object FROM "
                + Sample.class.getName() + " object WHERE id = $1");
        oql.bind(Sample.DEFAULT_ID);

        enumeration = oql.execute();
        if (enumeration.hasMoreElements()) {
            object = (Sample) enumeration.nextElement();
            LOG.debug("Retrieved object: " + object);
            object.setValue1(Sample.DEFAULT_VALUE_1);
            object.setValue2(Sample.DEFAULT_VALUE_2);
        } else {
            object = new Sample();
            LOG.debug("Creating new object: " + object);
            _db.create(object);
        }
        
        oql.bind(Sample.DEFAULT_ID + 1);
        
        enumeration = oql.execute();
        if (enumeration.hasMoreElements()) {
            object = (Sample) enumeration.nextElement();
            LOG.debug("Retrieved object: " + object);
            object.setValue1(Sample.DEFAULT_VALUE_1);
            object.setValue2(Sample.DEFAULT_VALUE_2);
        } else {
            object = new Sample();
            object.setId(Sample.DEFAULT_ID + 1);
            LOG.debug("Creating new object: " + object);
            _db.create(object);
        }

        _db.commit();
    }

    /**
     * Run tests for each of the three access modes.
     * <p>
     * Notice that some database have their own deadlock detection mechanisms 
     * implemented. Depending on the stricktness of the algorithm, when 
     * these tests are runing in DbLocked mode, some database might throw 
     * an exception to resolve the deadlock before Castor JDO detects it.
     */
    public void testDeadlock() {
        LOG.info("Running in access mode shared");
        runOnce(Database.SHARED);

        LOG.info("Running in access mode exclusive");
        runOnce(Database.EXCLUSIVE);

        LOG.info("Running in access mode db-locked");
        runOnce(Database.DBLOCKED);
    }

    /**
     * Close the JDO databases used in the these test cases.
     */
    public void tearDown() throws PersistenceException {
        if (_db.isActive()) { _db.rollback(); }
        _db.close();
        if (_firstDb.isActive()) { _firstDb.rollback(); }
        _firstDb.close();
        if (_secondDb.isActive()) { _secondDb.rollback(); }
        _secondDb.close();
    }
    
    /**
     * Creates threads to test for deadlock detection behaviors.
     */
    public void runOnce(final AccessMode accessMode) {
        LOG.debug("Note: this test uses a 1 seconds delay between "
                + "threads. CPU and database load might cause the test to not "
                + "perform synchronously, resulting in erroneous results. Make "
                + "sure that execution is not hampered by CPU/datebase load.");

        // Run two threads in parallel attempting to update the
        // two objects in a different order, with the first
        // suceeding and second failing
        _firstRunnable = new FirstRunnable(this, accessMode);
        _firstProblem = null;
        _secondRunnable = new SecondRunnable(this, accessMode);
        _secondProblem = null;
        
        CPAThreadedTestRunnable[] ctr = {_firstRunnable, _secondRunnable};
        runTestRunnables(ctr);

        if (_firstProblem != null || _secondProblem != null) {
            if (_firstProblem != null) {
                LOG.error("first failed: ", _firstProblem);
            }
            if (_secondProblem != null) {
                LOG.error("second failed: ", _secondProblem);
            }
            fail("unexpected deadlock behavior");
        }
    }
    
    public void firstTransactionRun(final AccessMode accessMode) {
        Sample object;
            
        try {
            _firstDb.begin();
            
            // Sleep one second, and make sure the second thread run first.  
            Thread.sleep(WAIT);
            
            // Load first object and change something about it
            // (otherwise will not write)
            LOG.debug("1.1: Loading object " + Sample.DEFAULT_ID);
            object = _firstDb.load(Sample.class,
                    new Integer(Sample.DEFAULT_ID), accessMode);
            object.setValue1(Sample.DEFAULT_VALUE_1 + ":1");
            LOG.debug("1.2: Modified to " + object);

            // Notify the other thread that it may proceed and suspend
            // to give the other thread a opportunity.
            synchronized (_lock) {
                _lock.notify();
                _lock.wait();
            }

            LOG.debug("1.3: Loading object " + (Sample.DEFAULT_ID + 1));
            object = _firstDb.load(Sample.class, new Integer(
                    Sample.DEFAULT_ID + 1), accessMode);
            object.setValue2(Sample.DEFAULT_VALUE_2 + ":1");
            LOG.debug("1.4: Modified to " + object);

            // Notify the other thread that it may proceed and suspend
            // to give the other thread a 2 second opportunity.
            synchronized (_lock) {
                _lock.notify();
                _lock.wait(WAIT);
            }

            // Attempt to commit the transaction, must acquire a write
            // lock blocking until the first transaction completes.
            LOG.debug("1.5: Committing");

            _firstDb.commit();

            synchronized (_lock) {
                _lock.notify();
            }

            LOG.debug("1.6: Committed");
        } catch (Exception ex) {
            _firstProblem = ex;
            LOG.info("1.X: ", ex);
        } finally {
            try {
                if (_firstDb.isActive()) {
                    _firstDb.rollback();
                }
            } catch (TransactionNotInProgressException ex) {
            }
        }
    }
    
    public void secondTransactionRun(final AccessMode accessMode) {
        Sample   object;

        try {
            _secondDb.begin(); 

            // Suspend and give the other thread a opportunity.
            synchronized (_lock) {
                _lock.wait();
            }
            
            // Load first object and change something about it
            // (otherwise will not write)
            LOG.debug("2.1: Loading object " + (Sample.DEFAULT_ID + 1));
            object = _secondDb.load(Sample.class, new Integer(
                    Sample.DEFAULT_ID + 1), accessMode);
            object.setValue2(Sample.DEFAULT_VALUE_2 + ":2");
            LOG.debug("2.2: Modified to " + object);
            
            // Notify the other thread that it may proceed and suspend
            // to give the other thread a 2 second opportunity.
            synchronized (_lock) {
                _lock.notify();
                _lock.wait(WAIT);
            }
            
            LOG.debug("2.3: Loading object " + Sample.DEFAULT_ID);
            try {
                object = _secondDb.load(Sample.class, new Integer(
                        Sample.DEFAULT_ID), accessMode);
            } catch (LockNotGrantedException ex) {
                if (accessMode == Database.EXCLUSIVE
                        || accessMode == Database.DBLOCKED) {
                    LOG.debug("2.X OK: Deadlock detected");
                } else {
                    _secondProblem = ex;
                    LOG.info("2.X Error: ", ex);
                }
                _secondDb.rollback();
                return;
            }
            object.setValue1(Sample.DEFAULT_VALUE_1 + ":2");
            LOG.debug("2.4: Modified to " + object);

            // Notify the other thread that it may proceed and suspend
            // to give the other thread a 2 second opportunity.
            synchronized (_lock) {
                _lock.notify();
                _lock.wait(WAIT);
            }
            
            // Attempt to commit the transaction, must acquire a write
            // lock blocking until the first transaction completes.
            LOG.debug("2.5: Committing");
            
            _secondDb.commit();

            synchronized (_lock) {
                _lock.notify();
            }
            
            LOG.info("2.6 Error: no deadlock and second committed");
            _secondProblem = new Exception(
                    "deadlock not detected");
        } catch (TransactionAbortedException ex) {
            if (ex.getCause() instanceof LockNotGrantedException) {
                LOG.debug("2.X OK: Deadlock detected");
            } else {
                _secondProblem = ex;
                LOG.info("2.X Error: ", ex);
            }
            LOG.debug("2.X Second: aborting");
        } catch (Exception ex) {
            _secondProblem = ex;
            LOG.info("2.X Error: ", ex);
        } finally {
            try {
                if (_secondDb.isActive()) { _secondDb.rollback(); }
            } catch (TransactionNotInProgressException ex) {
            }
        }
    }
}
