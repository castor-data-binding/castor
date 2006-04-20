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
 * Copyright 2000-2001 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id$
 */

package ctf.jdo.tc0x;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Random;

import harness.CastorTestCase;
import harness.TestHarness;

import jdo.JDOCategory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.LockNotGrantedException;
import org.exolab.castor.jdo.TransactionAbortedException;
import org.exolab.castor.jdo.TransactionNotInProgressException;
import org.exolab.castor.jdo.ObjectNotFoundException;

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
public final class TestCacheLeakage extends CastorTestCase {
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

    /** Number of retrials attempt to load per each trial */
    private static final int NUM_OF_RETRIAL = 20;

    /** The base time in milliseconds between each attempts */
    private static final int SLEEP_BASE_TIME = 100;

    /** Number of load and releases of read threads to race on target */
    private static final int NUM_OF_READ_THREADS = 5;

    /** The JDO test suite these test cases belongs to */
    private JDOCategory    _category;

    /** The JDBC database */
    private Database       _db;

    /** The JDBC connection used to initalizes tables for tests */
    private Connection _conn;

    /** The java class of the target data objects */
    private Class _classType;

    /** The cache type used in the current test */
    private int _cacheType;

    /** Indicates leakage detected */
    private boolean _errLeak;

    /** Indicates error detected */
    private boolean _errCount;

    /**
     * Constructor
     *
     * @param category the test suite of these test cases
     */
    public TestCacheLeakage(final TestHarness category) {
        super(category, "TC07", "Cache leakage tests");
        _category = (JDOCategory) category;
        _errLeak = false;
        _errCount = false;
    }

    /**
     * Get a JDO Database and get a JDBC connection
     */
    public void setUp() throws PersistenceException, SQLException {
        _db = _category.getDatabase();
        _conn = _category.getJDBCConnection();
        _conn.setAutoCommit(false);
    }

    /**
     * Run the stress test for four different cache setting
     */
    public void runTest() throws PersistenceException, SQLException {
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
        _conn.close();

        if (_db.isActive()) { _db.rollback(); }
        _db.close();
    }

    /**
     * Helper class to run the stress tests once for a cache type.
     */
    public void runOnce() throws PersistenceException, SQLException {
        // clear the table
        int del = _conn.createStatement().executeUpdate(
                "DELETE FROM tc0x_race");
        LOG.debug("row(s) deleted in table core_race: " + del);
        _conn.commit();

        // set the className and classType to be used
        switch (_cacheType) {
        case COUNT_LIMITED:
            _classType = ctf.jdo.tc0x.RaceCount.class;
            break;
        case TIME_LIMITED:
            _classType = ctf.jdo.tc0x.RaceTime.class;
            break;
        case NO_CACHE:
            _classType = ctf.jdo.tc0x.RaceNone.class;
            break;
        case UNLIMITED:
            _classType = ctf.jdo.tc0x.RaceUnlimited.class;
            break;
        default:
            LOG.error("Unknown cache type");
        }

        CreateDeleteThread cdThread = new CreateDeleteThread(
                this, _category, _cacheType, NUM_OF_CREATE_DELETE);

        ReadThread[] rThread =  new ReadThread[NUM_OF_READ_THREADS];
        for (int i = 0; i < NUM_OF_READ_THREADS; i++) {
            rThread[i] = new ReadThread(this, cdThread, _category, NUM_OF_READ);
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

    /**
     * Multiple read threads are created and continuously read the data 
     * objects that is modifing by the WriteThread. A read threads does
     * not modify the data objects but commit the transaction. It gives
     * stress to Castor JDO and intend to test if the right behavior is
     * not affect by stress.
     */
    private class ReadThread extends Thread {
        private CreateDeleteThread  _other;
        private Database            _db;
        private int                 _trials;
        private boolean             _isDone;
        private Random              _random;

        ReadThread(final TestCacheLeakage parent,
                   final CreateDeleteThread other,
                   final JDOCategory category, final int trials)
        throws PersistenceException {
            _other = other;
            _db = category.getDatabase();
            _trials = trials;
            _random = new Random();
        }

        public void run() {
            boolean succeed;
            int trials = 0;
            try {
                for (int i = 0; i < _trials && !_other.isDone(); i++) {
                    try {
                        // loads it and releases it
                        _db.begin();
                        succeed = false;
                        trials = 0;

                        while (!succeed && !_other.isDone()
                               && trials < NUM_OF_RETRIAL) {
                            
                            trials++;
                            try {
                                _db.load(_classType, Race.DEFAULT_ID, 
                                        Database.Shared);
                                // may throw ObjectNotFoundException
                                // or LockNotGrantedException
                                _db.commit();
                                succeed = true;
                            } catch (LockNotGrantedException ex) {
                                succeed = false;
                                sleep(trials);
                            } catch (ObjectNotFoundException ex) {
                                succeed = false;
                                sleep(trials);
                            } catch (TransactionAbortedException ex) {
                                succeed = false;
                                sleep(trials);
                            }
                        }
                        
                        if (_db.isActive()) { _db.rollback(); }
                    } catch (Exception ex) {
                        fail(ex.toString());
                    }
                }
            } finally {
                _isDone = true;
                try {
                    _db.close();
                } catch (PersistenceException ex) {
                    fail(ex.toString());
                }
            }
        }
        
        private void sleep(final int trials) throws InterruptedException {
            // ethernet way of retry
            Thread.sleep((long) ((SLEEP_BASE_TIME ^ trials)
                                 * _random.nextDouble()));
        }
        
        public boolean isDone() {
            return _isDone;
        }
    }

    /**
     * This is write thread that continuously create, load and modify, 
     * load and test if the modification is succeed, load and remove 
     * data objects and create again. This threads is the only thread 
     * that modifies data objects. If any inconsistency detected, for
     * examples, modification is not persisted, deleted object reappears,
     * created object disappeared, we can confirm there is problem in
     * the concurrent engine of Castor JDO.
     */
    private class CreateDeleteThread extends Thread {
        private Database            _db;
        private int                 _cachetype;
        private int                 _trials;
        private boolean             _isDone;
        private Random              _random;

        CreateDeleteThread(final TestCacheLeakage parent,
                                   final JDOCategory category,
                                   final int cachetype, final int trials)
        throws PersistenceException {
            this._db = category.getDatabase();
            _cachetype = cachetype;
            _trials = trials;
            _random = new Random();
        }
        
        public void run() {
            try {
                LOG.info("start testing");
                Race tr;
                Race testrace;
                boolean succeed;
                int trials;

                out:
                for (int i = 0; i < _trials; i++) {
                    // create, modified, delete object, depending on the
                    // current cache type
                    try {
                        switch (_cachetype) {
                        case 0:
                            testrace = new RaceCount();
                            testrace.setId(5);
                            break;
                        case 1:
                            testrace = new RaceTime();
                            testrace.setId(5);
                            break;
                        case 2:
                            testrace = new RaceNone();
                            testrace.setId(5);
                            break;
                        case 3:
                            testrace = new RaceUnlimited();
                            testrace.setId(5);
                            break;
                        default:
                            testrace = null;
                        }
     
                        _db.begin();
                        _db.create(testrace);
                        // may throw DuplicateIdentityException
                        _db.commit();

                        // load it and modify it
                        succeed = false;
                        trials = 0;
                        while (!succeed && trials < NUM_OF_RETRIAL) {
                            trials++;
                            try {
                                _db.begin();
                                tr = (Race) _db.load(_classType,
                                                     Race.DEFAULT_ID);
                                // may throw ObjectNotFoundException
                                // or LockNotGrantedException
                                tr.incValue1();
                                _db.commit();
                                succeed = true;
                            } catch (LockNotGrantedException ex) {
                                succeed = false;
                                rollbackAndSleep(trials);
                            } catch (TransactionAbortedException ex) {
                                succeed = false;
                                rollbackAndSleep(trials);
                            } 
                        }
                        
                        if (_db.isActive()) { _db.rollback(); }

                        // load it and release it
                        succeed = false;
                        trials = 0;
                        while (!succeed && trials < NUM_OF_RETRIAL) {
                            trials++;
                            try {
                                _db.begin();
                                tr = (Race) _db.load(_classType,
                                                     Race.DEFAULT_ID);
                                // may throw ObjectNotFoundException
                                // or LockNotGrantedException
                                _db.commit();
                                succeed = true;
                            } catch (LockNotGrantedException ex) {
                                succeed = false;
                                rollbackAndSleep(trials);
                            } 
                        }
                        if (_db.isActive()) { _db.rollback(); }

                        // load it and delete it
                        succeed = false;
                        trials = 0;
                        while (!succeed && trials < NUM_OF_RETRIAL) {
                            trials++;    
                            try {
                                _db.begin();
                                tr = (Race) _db.load(_classType,
                                                     Race.DEFAULT_ID);
                                // may throw ObjectNotFoundException
                                // or LockNotGrantedException
                                _db.remove(tr);
                                _db.commit();
                                succeed = true;
                            } catch (LockNotGrantedException ex) {
                                succeed = false;
                                rollbackAndSleep(trials);
                            } catch (TransactionAbortedException ex) {
                                succeed = false;
                                rollbackAndSleep(trials);
                            }
                        }
                        
                        if (_db.isActive()) { _db.rollback(); }
                        
                        if (!succeed) {
                            throw new Exception(
                                    "Transaction can't lock the object within "
                                    + trials + " trials");
                        }
                    } catch (TransactionNotInProgressException ex) {
                        LOG.error("Thread <CreateDelete> will be killed. "
                                + "Unexcepted exception: ", ex);
                        rollbackWhenActive();
                        _errLeak = true;
                        break out;
                    } catch (PersistenceException ex) {
                        LOG.error("Thread <CreateDelete> will be killed. "
                                + "Unexcepted exception: ", ex);
                        rollbackWhenActive();
                        _errLeak = true;
                        break out;
                    } catch (Exception ex) {
                        LOG.error("Thread <CreateDelete> will be killed. "
                                + "Element not found (other): ", ex);
                        rollbackWhenActive();
                        _errLeak = true;
                        break out;
                    }
                }
            } finally {
                _isDone = true;
                try {
                    _db.close();
                } catch (PersistenceException ex) {
                    fail(ex.toString());
                }
            }
        }
        
        private void rollbackAndSleep(final int trials)
        throws TransactionNotInProgressException, InterruptedException {
            _db.rollback();
            // ethernet way of retry
            Thread.sleep((long) ((SLEEP_BASE_TIME ^ trials)
                                 * _random.nextDouble()));
        }
        
        private void rollbackWhenActive() {
            if (_db.isActive()) {
                try { _db.rollback(); } catch (Exception ee) { }
            }
        }
        
        public boolean isDone() {
            return _isDone;
        }
    }
}
