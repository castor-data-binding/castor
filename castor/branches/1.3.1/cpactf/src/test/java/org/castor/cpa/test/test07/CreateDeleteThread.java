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

import java.util.Random;

import junit.framework.Assert;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.JDOManager;
import org.exolab.castor.jdo.LockNotGrantedException;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.TransactionAbortedException;
import org.exolab.castor.jdo.TransactionNotInProgressException;
import org.junit.Ignore;

/**
 * This is write thread that continuously create, load and modify, 
 * load and test if the modification is succeed, load and remove 
 * data objects and create again. This threads is the only thread 
 * that modifies data objects. If any inconsistency detected, for
 * examples, modification is not persisted, deleted object reappears,
 * created object disappeared, we can confirm there is problem in
 * the concurrent engine of Castor JDO.
 */
@Ignore
public class CreateDeleteThread extends Thread {
    /**
     * The <a href="http://jakarta.apache.org/commons/logging/">Jakarta
     * Commons Logging</a> instance used for all logging.
     */
    private static final Log LOG = LogFactory.getLog(CreateDeleteThread.class);


    private Database            _db;
    private int                 _cachetype;
    private int                 _trials;
    private boolean             _isDone;
    private Random              _random;
    
    /** Number of retrials attempt to load per each trial */
    public static final int NUM_OF_RETRIAL = 20;

    /** The base time in milliseconds between each attempts */
    public static final int SLEEP_BASE_TIME = 100;
    
    CreateDeleteThread(final TestCacheLeakage parent,
                               final JDOManager jdo,
                               final int cachetype, final int trials) throws Exception {
        _db = jdo.getDatabase();
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
                            tr = (Race) _db.load(TestCacheLeakage.getClassType(),
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
                            tr = (Race) _db.load(TestCacheLeakage.getClassType(),
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
                            tr = (Race) _db.load(TestCacheLeakage.getClassType(),
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
                    TestCacheLeakage.setErrLeak(true);
                    break out;
                } catch (PersistenceException ex) {
                    LOG.error("Thread <CreateDelete> will be killed. "
                            + "Unexcepted exception: ", ex);
                    rollbackWhenActive();
                    TestCacheLeakage.setErrLeak(true);
                    break out;
                } catch (Exception ex) {
                    LOG.error("Thread <CreateDelete> will be killed. "
                            + "Element not found (other): ", ex);
                    rollbackWhenActive();
                    TestCacheLeakage.setErrLeak(true);
                    break out;
                }
            }
        } finally {
            _isDone = true;
            try {
                _db.close();
            } catch (PersistenceException ex) {
                Assert.fail(ex.toString());
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

