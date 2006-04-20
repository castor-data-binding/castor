/**
 * Redistribution and use of this software and associated documentation
 * ("Software"), with or without modification, are permitted provided
 * that the following conditions are met:
 *
 * 1. Redistributions of source code must retain copyright
 *    statements and notices.  Redistributions must also contain a
 *    copy of this document.
 *
 * 2. Redistributions in binary form must reproduce the
 *    above copyright notice, this list of conditions and the
 *    following disclaimer in the documentation and/or other
 *    materials provided with the distribution.
 *
 * 3. The name "Exolab" must not be used to endorse or promote
 *    products derived from this Software without prior written
 *    permission of Intalio, Inc.  For written permission,
 *    please contact info@exolab.org.
 *
 * 4. Products derived from this Software may not be called "Exolab"
 *    nor may "Exolab" appear in their names without prior written
 *    permission of Intalio, Inc. Exolab is a registered
 *    trademark of Intalio, Inc.
 *
 * 5. Due credit should be given to the Exolab Project
 *    (http://www.exolab.org/).
 *
 * THIS SOFTWARE IS PROVIDED BY INTALIO, INC. AND CONTRIBUTORS
 * ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT
 * NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL
 * INTALIO, INC. OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * Copyright 1999-2001 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id$
 */

package ctf.jdo.tc0x;

import java.util.Enumeration;

import harness.CastorTestCase;
import harness.TestHarness;

import jdo.JDOCategory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

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
public final class TestDeadlock extends CastorTestCase {
    /**
     * The <a href="http://jakarta.apache.org/commons/logging/">Jakarta
     * Commons Logging</a> instance used for all logging.
     */
    private static final Log LOG = LogFactory.getLog(TestDeadlock.class);
    
    /**
     * The time a transaction sleep and wait for another transaction to
     * process
     */
    public static final long  WAIT = 1000;

    /**
     * The JDO test suite for this test case
     */
    private JDOCategory     _category;

    /**
     * The JDO database used for setUp
     */
    private Database        _db;

    /**
     * AccessMode used in the tests
     */
    private AccessMode      _accessMode;

    /**
     * The java object to be synchronized on
     */
    private Object          _lock;

    /**
     * The JDO database used for first concurrent transactions
     */
    private Database        _firstDb;

    /**
     * The thread that the first transaction is running on
     */
    private FirstThread     _firstThread;

    private Exception       _firstProblem;

    /**
     * The JDO database used for second concurrent transactions
     */
    private Database        _secondDb;

    /**
     * The thread that the second transaction is running on
     */
    private SecondThread    _secondThread;

    private Exception       _secondProblem;

    /**
     * Constructor
     *
     * @param category The test suit of these test cases
     */
    public TestDeadlock(final TestHarness category) {
        super(category, "TC04", "Deadlock detection tests");
        _category = (JDOCategory) category;
    }

    /**
     * Get the JDO Databases and create data objects into the database
     */
    public void setUp() throws PersistenceException {
        _db = _category.getDatabase();
        _firstDb = _category.getDatabase();
        _secondDb = _category.getDatabase();

        Sample    object;
        Enumeration   enumeration;
        
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
    public void runTest() throws PersistenceException, InterruptedException {
        LOG.info("Running in access mode shared");
        runOnce(Database.Shared);

        LOG.info("Running in access mode exclusive");
        runOnce(Database.Exclusive);

        LOG.info("Running in access mode db-locked");
        runOnce(Database.DbLocked);
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
    public void runOnce(final AccessMode accessMode) 
    throws PersistenceException, InterruptedException {
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
    
    private class FirstThread extends Thread {
        private Database         _db;

        private boolean          _resultOk;

        private TestDeadlock     _theTest;

        FirstThread(final Database db, final TestDeadlock theTest) {
            _db      = db;
            _theTest = theTest;
        }

        boolean result() {
            return _resultOk;
        }

        public void run() {
            Sample   object;

            try {
                _db.begin();

                // Load first object and change something about it
                // (otherwise will not write)
                LOG.debug("1.1: Loading object " + Sample.DEFAULT_ID);
                object = (Sample) _db.load(Sample.class,
                        new Integer(Sample.DEFAULT_ID), _accessMode);
                object.setValue1(Sample.DEFAULT_VALUE_1 + ":1");
                LOG.debug("1.2: Modified to " + object);
                
                // Notify the other thread that it may proceed and suspend
                // to give the other thread a opportunity.
                synchronized (_lock) {
                    _lock.notify();
                    _lock.wait();
                }
                
                LOG.debug("1.3: Loading object " + (Sample.DEFAULT_ID + 1));
                object = (Sample) _db.load(Sample.class,
                        new Integer(Sample.DEFAULT_ID + 1), _accessMode);
                object.setValue2(Sample.DEFAULT_VALUE_2 + ":1");
                LOG.debug("1.4: Modified to " + object);
                
                // Notify the other thread that it may proceed and suspend
                // to give the other thread a 2 second opportunity.
                if (_secondThread.isAlive()) {
                    synchronized (_lock) {
                        _lock.notify();
                        _lock.wait(2000);
                    }
                }

                // Attempt to commit the transaction, must acquire a write
                // lock blocking until the first transaction completes.
                LOG.debug("1.5: Committing");
                
                _db.commit();

                if (_secondThread.isAlive()) {
                    synchronized (_lock) {
                        _lock.notify();
                    }
                }

                LOG.debug("1.6: Committed");
                _resultOk = true;
            } catch (Exception ex) {
                _theTest._firstProblem = ex;
                LOG.info("1.X: ", ex);
            } finally {
                try {
                    if (_db.isActive()) { _db.rollback(); }
                } catch (TransactionNotInProgressException ex) {
                }
            }
        }
    }

    private class SecondThread extends Thread {
        private Database         _db;

        private TestDeadlock     _theTest;

        private boolean          _resultOk;

        SecondThread(final Database db, final TestDeadlock theTest) {
            _db = db;
            _theTest = theTest;
        }

        boolean result() {
            return _resultOk;
        }

        public void run() {
            Sample   object;

            try {
                _db.begin(); 

                // Suspend and give the other thread a opportunity.
                synchronized (_lock) {
                    _lock.wait();
                }
                
                // Load first object and change something about it
                // (otherwise will not write)
                LOG.debug("2.1: Loading object " + (Sample.DEFAULT_ID + 1));
                object = (Sample) _db.load(Sample.class,
                        new Integer(Sample.DEFAULT_ID + 1), _accessMode);
                object.setValue2(Sample.DEFAULT_VALUE_2 + ":2");
                LOG.debug("2.2: Modified to " + object);
                
                // Notify the other thread that it may proceed and suspend
                // to give the other thread a 2 second opportunity.
                synchronized (_lock) {
                    _lock.notify();
                    _lock.wait(2000);
                }
                
                LOG.debug("2.3: Loading object " + Sample.DEFAULT_ID);
                try {
                    object = (Sample) _db.load(Sample.class,
                            new Integer(Sample.DEFAULT_ID), _accessMode);
                } catch (LockNotGrantedException ex) {
                    if (_accessMode == Database.Exclusive
                            || _accessMode == Database.DbLocked) {
                        LOG.debug("2.X OK: Deadlock detected");
                    } else {
                        _theTest._secondProblem = ex;
                        LOG.info("2.X Error: ", ex);
                    }
                    _db.rollback();
                    _resultOk = true;
                    return;
                }
                object.setValue1(Sample.DEFAULT_VALUE_1 + ":2");
                LOG.debug("2.4: Modified to " + object);

                // Notify the other thread that it may proceed and suspend
                // to give the other thread a 2 second opportunity.
                synchronized (_lock) {
                    _lock.notify();
                    _lock.wait(2000);
                }
                
                // Attempt to commit the transaction, must acquire a write
                // lock blocking until the first transaction completes.
                LOG.debug("2.5: Committing");
                
                _db.commit();

                synchronized (_lock) {
                    _lock.notify();
                }
                
                LOG.info("2.6 Error: no deadlock and second committed");
                _theTest._secondProblem = new Exception(
                        "deadlock not detected");
            } catch (TransactionAbortedException ex) {
                if (ex.getCause() instanceof LockNotGrantedException) {
                    LOG.debug("2.X OK: Deadlock detected");
                    _resultOk = true;
                } else {
                    _theTest._secondProblem = ex;
                    LOG.info("2.X Error: ", ex);
                }
                LOG.debug("2.X Second: aborting");
            } catch (Exception ex) {
                _theTest._secondProblem = ex;
                LOG.info("2.X Error: ", ex);
            } finally {
                try {
                    if (_db.isActive()) { _db.rollback(); }
                } catch (TransactionNotInProgressException ex) {
                }
            }
        }
    }
}
