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
import org.castor.cpa.test.framework.CPATestCase;
import org.castor.cpa.test.framework.xml.types.DatabaseEngineType;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.PersistenceException;
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
public final class TestDeadlock extends CPATestCase {
    /**  The <a href="http://jakarta.apache.org/commons/logging/">Jakarta
     * Commons Logging</a> instance used for all logging. */
    private static final Log LOG = LogFactory.getLog(TestDeadlock.class);
    
    /**
     * The time a transaction sleep and wait for another transaction to
     * process
     */
    public static final long  WAIT = 1000;

    private static final String DBNAME = "test04";
    private static final String MAPPING = "/org/castor/cpa/test/test04/mapping.xml";
    private Database _db;

    /**  AccessMode used in the tests */
    public static AccessMode      _accessMode;

    /** The java object to be synchronized on */
    public static Object          _lock;

    /**  The JDO database used for first concurrent transactions */
    private Database        _firstDb;

    /** The thread that the first transaction is running on */
    private FirstThread     _firstThread;

    public Exception       _firstProblem;

    /** The JDO database used for second concurrent transactions */
    private Database        _secondDb;

    /** The thread that the second transaction is running on  */
    public static SecondThread    _secondThread;

    public Exception       _secondProblem;

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
            || (engine == DatabaseEngineType.MYSQL);
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
    public void testDeadlock() throws InterruptedException {
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
    public void runOnce(final AccessMode accessMode) throws InterruptedException {
        LOG.debug("Note: this test uses a 2 seconds delay between "
                + "threads. CPU and database load might cause the test to not "
                + "perform synchronously, resulting in erroneous results. Make "
                + "sure that execution is not hampered by CPU/datebase load.");

        // Run two threads in parallel attempting to update the
        // two objects in a different order, with the first
        // suceeding and second failing
        _accessMode = accessMode;
        _lock = new Object();
        _firstThread = new FirstThread(_firstDb, this);
        _firstProblem = null;
        _secondThread = new SecondThread(_secondDb, this);
        _secondProblem = null;

        _secondThread.start();
        _firstThread.start();

        _firstThread.join();
        _secondThread.join();

        if (!_firstThread._resultOk || !_secondThread._resultOk) {
            if (!_firstThread._resultOk) {
                LOG.error("first failed: ", _firstProblem);
            }
            if (!_secondThread._resultOk) {
                LOG.error("second failed: ", _secondProblem);
            }
            fail("unexpected deadlock behavior");
        }
    }
    
    


}
