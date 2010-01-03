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

import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.JDOManager;
import org.exolab.castor.jdo.LockNotGrantedException;
import org.exolab.castor.jdo.ObjectNotFoundException;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.TransactionAbortedException;
import org.junit.Ignore;

/**
 * Multiple read threads are created and continuously read the data 
 * objects that is modifing by the WriteThread. A read threads does
 * not modify the data objects but commit the transaction. It gives
 * stress to Castor JDO and intend to test if the right behavior is
 * not affect by stress.
 */
@Ignore
public class ReadThread extends Thread {

    private CreateDeleteThread  _other;
    private Database            _db;
    private int                 _trials;
    private boolean             _isDone;
    private Random              _random;

    ReadThread(final TestCacheLeakage parent,
               final CreateDeleteThread other,
               final JDOManager jdo, final int trials) throws Exception {
        _other = other;
        _db = jdo.getDatabase();
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
                           && trials < CreateDeleteThread.NUM_OF_RETRIAL) {
                        
                        trials++;
                        try {
                            _db.load(TestCacheLeakage.getClassType(), Race.DEFAULT_ID, 
                                    Database.SHARED);
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
                    Assert.fail(ex.toString());
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
    
    private void sleep(final int trials) throws InterruptedException {
        // ethernet way of retry
        Thread.sleep((long) ((CreateDeleteThread.SLEEP_BASE_TIME ^ trials)
                             * _random.nextDouble()));
    }
    
    public boolean isDone() {
        return _isDone;
    }
}
