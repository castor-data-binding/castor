/*
 * Copyright 2011 Wensheng Dao, Ralf Joachim
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
package org.castor.cpa.test.test3121;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.castor.cpa.test.framework.CPAThreadedTestCase;
import org.castor.cpa.test.framework.CPAThreadedTestRunnable;
import org.castor.cpa.test.framework.xml.types.DatabaseEngineType;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.ObjectNotFoundException;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.TransactionNotInProgressException;
import org.exolab.castor.persist.ObjectDeletedWaitingForLockException;

/**
 * Tests for concurrent delete, load and create. Will report to the console 
 * two concurrent transactions working on the same objects. The first transaction 
 * will delete the object, the second will load or create the object. 
 */
public final class TestDelete extends CPAThreadedTestCase {
    /** The <a href="http://jakarta.apache.org/commons/logging/">Jakarta
     *  Commons Logging</a> instance used for all logging. */
    private static final Log LOG = LogFactory.getLog(TestDelete.class);
    
    /** The time a transaction sleep and wait for another transaction to process. */
    public static final long WAIT = 2000;

    public static final Integer ID = new Integer(1);
    
    private static final String DBNAME = "test3121";
    private static final String MAPPING = "/org/castor/cpa/test/test3121/mapping.xml";

    /** The java object to be synchronized on */
    private Object _lock = new Object();;

    /**  The JDO database used for first concurrent transactions */
    private Database _firstDb;

    /** The thread that the first transaction is running on */
    private CPAThreadedTestRunnable _firstRunnable;

    public Exception _firstProblem;

    /** The JDO database used for second concurrent transactions */
    private Database _secondDb;

    /** The thread that the second transaction is running on  */
    public CPAThreadedTestRunnable _secondRunnable;

    public Exception _secondProblem;

    private Database _db;
    
    /**
     * Constructor
     *
     * @param category The test suit of these test cases
     */
    public TestDelete(final String name) {
        super(name);
    }

    // Test are only included/excluded for engines that have been tested with this test suite.

    public boolean include(final DatabaseEngineType engine) {
        return (engine == DatabaseEngineType.DERBY)
            || (engine == DatabaseEngineType.HSQL)
            || (engine == DatabaseEngineType.MYSQL)
            || (engine == DatabaseEngineType.ORACLE)
            || (engine == DatabaseEngineType.POSTGRESQL)
            || (engine == DatabaseEngineType.SQL_SERVER)
            || (engine == DatabaseEngineType.SAPDB);
    }

    /**
     * Get the JDO Databases
     */
    public void setUp() throws Exception {
        _db = getJDOManager(DBNAME, MAPPING).getDatabase();
        _firstDb = getJDOManager(DBNAME, MAPPING).getDatabase();
        _secondDb = getJDOManager(DBNAME, MAPPING).getDatabase();
    }

    /**
     * Close the JDO databases used in the these test cases.
     */
    public void tearDown() throws PersistenceException {
        if (_db.isActive()) {
            _db.rollback();
        }
        _db.close();
        if (_firstDb.isActive()) {
            _firstDb.rollback();
        }
        _firstDb.close();
        if (_secondDb.isActive()) {
            _secondDb.rollback();
        }
        _secondDb.close();
    }
    
    /**
     * Create the data object into the database
     * @throws PersistenceException
     */
    private void initDatabase() throws PersistenceException {
        _db.begin(); 
        Sample object = new Sample();
        object.setId(1);
        object.setValue1("value1");
        object.setValue2("value2");
        _db.create(object);
        _db.commit();
    }
    
    /**
     * Creates threads to test for delete then load behaviors.
     * @throws PersistenceException 
     */
    public void testDeleteThenLoad() throws PersistenceException {
        initDatabase();
        // Run two threads in parallel, the first thread deletes the object, the second thread
        // loads the object.
        _firstRunnable = new DeleteForLoadRunnable(this);
        _firstProblem = null;
        _secondRunnable = new LoadRunnable(this);
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
            fail("unexpected exception happened.");
        }
    }
    
    /**
     * Creates threads to test for delete then create behaviors.
     * @throws PersistenceException 
     */
    public void testDeleteThenCreate() throws PersistenceException {
        initDatabase();
        // Run two threads in parallel, the first thread deletes the object, the second thread
        // creates the object.
        _firstRunnable = new DeleteForCreateRunnable(this);
        _firstProblem = null;
        _secondRunnable = new CreateRunnable(this);
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
            fail("unexpected exception happened.");
        }
    }
    
    public void deleteObjectForLoad() {          
        try {
            _firstDb.begin();
            
            // Sleep one second, and make sure the second thread run first.  
            Thread.sleep(WAIT);
            
            LOG.debug("1.1: Loading object " + ID);
            Sample object = _firstDb.load(Sample.class, ID);
            
            LOG.debug("1.2: Deleting object " + ID);
            _firstDb.remove(object);

            synchronized (_lock) {
                _lock.notify();
                _lock.wait(WAIT);
            }
            
            LOG.debug("1.3: Committing");
            _firstDb.commit();
            
            LOG.debug("1.4: Committed");
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
    
    public void loadObject() {
        try {
            _secondDb.begin(); 

            // Suspend and give the other thread a opportunity.
            synchronized (_lock) {
                _lock.wait();
            }

            LOG.debug("2.1: Loading object " + ID);
            
            try {
                _secondDb.load(Sample.class, ID);
            } catch (ObjectNotFoundException ex) {
                if (ex.getCause() instanceof ObjectDeletedWaitingForLockException) {
                    LOG.debug("2.X OK: ObjectDeletedWaitingForLockException detected");
                } else {
                    _secondProblem = ex;
                    LOG.info("2.X Error: ", ex);
                }
                LOG.debug("2.X Second: aborting");
                _secondDb.rollback();
                return;
            }
            LOG.info("2.2 Error: the object exists.");
            _secondProblem = new Exception("the object exists.");
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
    
    public void deleteObjectForCreate() {          
        try {
            _firstDb.begin();
            
            // Sleep one second, and make sure the second thread run first.  
            Thread.sleep(WAIT);
            
            LOG.debug("1.1: Loading object " + ID);
            Sample object = _firstDb.load(Sample.class, ID);
            
            LOG.debug("1.2: Deleting object " + ID);
            _firstDb.remove(object);
            
            LOG.debug("1.3: Committing");
            _firstDb.commit();
            
            synchronized (_lock) {
                _lock.notify();
            }
            
            LOG.debug("1.4: Committed");
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
    
    public void createObject() {
        try {
            _secondDb.begin(); 

            // Suspend and give the other thread a opportunity.
            synchronized (_lock) {
                _lock.wait();
            }
            
            LOG.debug("2.1: Creating object " + ID);
            
            try {
                Sample object = new Sample();
                object.setId(1);
                object.setValue1("value1");
                object.setValue2("value2");
                _secondDb.create(object);
            } catch (Exception ex) {
                _secondProblem = ex;
                LOG.info("2.X Error: ", ex);
                _secondDb.rollback();
                return;
            }
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
