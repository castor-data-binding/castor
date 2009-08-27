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
import org.exolab.castor.jdo.TransactionNotInProgressException;
import org.junit.Ignore;

@Ignore
public class FirstThread extends Thread {
    /**  The <a href="http://jakarta.apache.org/commons/logging/">Jakarta
     * Commons Logging</a> instance used for all logging. */
    private static final Log LOG = LogFactory.getLog(FirstThread.class);
    
    private Database _db;

    public boolean _resultOk;

    private TestDeadlock _theTest;

    FirstThread(final Database db, final TestDeadlock theTest) {
        _db = db;
        _theTest = theTest;
    }

    public void run() {
        Sample object;

        try {
            _db.begin();

            // Load first object and change something about it
            // (otherwise will not write)
            LOG.debug("1.1: Loading object " + Sample.DEFAULT_ID);
            object = _db.load(Sample.class, new Integer(Sample.DEFAULT_ID), 
                             TestDeadlock._accessMode);
            object.setValue1(Sample.DEFAULT_VALUE_1 + ":1");
            LOG.debug("1.2: Modified to " + object);

            // Notify the other thread that it may proceed and suspend
            // to give the other thread a opportunity.
            synchronized (TestDeadlock._lock) {
                TestDeadlock._lock.notify();
                TestDeadlock._lock.wait();
            }

            LOG.debug("1.3: Loading object " + (Sample.DEFAULT_ID + 1));
            object = _db.load(Sample.class, new Integer(Sample.DEFAULT_ID + 1), 
                TestDeadlock._accessMode);
            object.setValue2(Sample.DEFAULT_VALUE_2 + ":1");
            LOG.debug("1.4: Modified to " + object);

            // Notify the other thread that it may proceed and suspend
            // to give the other thread a 2 second opportunity.
            if (TestDeadlock._secondThread.isAlive()) {
                synchronized (TestDeadlock._lock) {
                    TestDeadlock._lock.notify();
                    TestDeadlock._lock.wait(2000);
                }
            }

            // Attempt to commit the transaction, must acquire a write
            // lock blocking until the first transaction completes.
            LOG.debug("1.5: Committing");

            _db.commit();

            if (TestDeadlock._secondThread.isAlive()) {
                synchronized (TestDeadlock._lock) {
                    TestDeadlock._lock.notify();
                }
            }

            LOG.debug("1.6: Committed");
            _resultOk = true;
        } catch (Exception ex) {
            _theTest._firstProblem = ex;
            LOG.info("1.X: ", ex);
        } finally {
            try {
                if (_db.isActive()) {
                    _db.rollback();
                }
            } catch (TransactionNotInProgressException ex) {
            }
        }
    }
}
