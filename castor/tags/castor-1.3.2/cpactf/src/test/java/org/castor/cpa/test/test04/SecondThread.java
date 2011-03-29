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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.LockNotGrantedException;
import org.exolab.castor.jdo.TransactionAbortedException;
import org.exolab.castor.jdo.TransactionNotInProgressException;
import org.junit.Ignore;

@Ignore
public class SecondThread extends Thread {
    /**  The <a href="http://jakarta.apache.org/commons/logging/">Jakarta
     * Commons Logging</a> instance used for all logging. */
    private static final Log LOG = LogFactory.getLog(SecondThread.class);
    
    private Database         _db;

    private TestDeadlock     _theTest;

    public boolean          _resultOk;

    SecondThread(final Database db, final TestDeadlock theTest) {
        _db = db;
        _theTest = theTest;
    }

    public void run() {
        Sample   object;

        try {
            _db.begin(); 

            // Suspend and give the other thread a opportunity.
            synchronized (TestDeadlock._lock) {
                TestDeadlock._lock.wait();
            }
            
            // Load first object and change something about it
            // (otherwise will not write)
            LOG.debug("2.1: Loading object " + (Sample.DEFAULT_ID + 1));
            object = _db.load(Sample.class,
                    new Integer(Sample.DEFAULT_ID + 1), TestDeadlock._accessMode);
            object.setValue2(Sample.DEFAULT_VALUE_2 + ":2");
            LOG.debug("2.2: Modified to " + object);
            
            // Notify the other thread that it may proceed and suspend
            // to give the other thread a 2 second opportunity.
            synchronized (TestDeadlock._lock) {
                TestDeadlock._lock.notify();
                TestDeadlock._lock.wait(2000);
            }
            
            LOG.debug("2.3: Loading object " + Sample.DEFAULT_ID);
            try {
                object = _db.load(Sample.class,
                        new Integer(Sample.DEFAULT_ID), TestDeadlock._accessMode);
            } catch (LockNotGrantedException ex) {
                if (TestDeadlock._accessMode == Database.EXCLUSIVE
                        || TestDeadlock._accessMode == Database.DBLOCKED) {
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
            synchronized (TestDeadlock._lock) {
                TestDeadlock._lock.notify();
                TestDeadlock._lock.wait(2000);
            }
            
            // Attempt to commit the transaction, must acquire a write
            // lock blocking until the first transaction completes.
            LOG.debug("2.5: Committing");
            
            _db.commit();

            synchronized (TestDeadlock._lock) {
                TestDeadlock._lock.notify();
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
