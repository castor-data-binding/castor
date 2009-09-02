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
package org.castor.cpa.test.test06;

import java.util.Enumeration;
import java.util.Random;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.LockNotGrantedException;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.QueryException;
import org.exolab.castor.jdo.QueryResults;
import org.exolab.castor.jdo.TransactionAbortedException;
import org.exolab.castor.jdo.TransactionNotInProgressException;
import org.junit.Ignore;

@Ignore
public class RaceThread extends Thread {
    /** The <a href="http://jakarta.apache.org/commons/logging/">Jakarta Commons
     *  Logging</a> instance used for all logging. */
    private static final Log LOG = LogFactory.getLog(RaceThread.class);

    private TestRaceCondition _parent;
    private Database _db;
    private RaceSync[] _race;
    private int _trials;
    private Random _random;
    private boolean _isDone;
    private Exception _fatal;

    RaceThread(final TestRaceCondition parent, final Database db, final RaceSync[] race,
            final int trials) {

        _parent = parent;
        _db = db;
        _race = race;
        _trials = trials;
        _random = new Random();
    }

    public void run() {
        try {
            LOG.info("start testing");
            out: for (int j = 0; j < _trials; j++) {
                some: for (int i = 0; i < _race.length; i++) {
                    boolean isOk = false;
                    int count = 0;

                    // select and inc the jdo object.
                    while (!isOk) {
                        try {
                            isOk = process(i);
                        } catch (TransactionAbortedException ex) {
                            // this exception should happen one in a while.
                            count++;
                            rollbackExpected(_db, ex);
                            if (count > 10) {
                                break some;
                            }
                        } catch (LockNotGrantedException ex) {
                            count++;
                            rollbackExpected(_db, ex);
                            if (count > 10) {
                                break some;
                            }
                        } catch (QueryException ex) {
                            rollbackUnexpected(_db, ex, "Unexcepted exception: ");
                            break out;
                        } catch (TransactionNotInProgressException ex) {
                            rollbackUnexpected(_db, ex, "Unexcepted exception: ");
                            break out;
                        } catch (PersistenceException ex) {
                            rollbackUnexpected(_db, ex, "Unexcepted exception: ");
                            break out;
                        } catch (NoSuchElementException ex) {
                            rollbackUnexpected(_db, ex, "Element not found (leakage): ");
                            break out;
                        } catch (Exception ex) {
                            rollbackUnexpected(_db, ex, "Element not found (other): ");
                            break out;
                        }
                    }

                    // inc the control value (objects are thread safe)
                    _race[i].incValue1();

                    // make some non-deterministicity. otherwise, we are
                    // just lining up thread and won't discover problem.
                    try {
                        Thread.sleep((long) (100 * _random.nextDouble()));
                    } catch (InterruptedException ex) {
                        LOG.error(ex);
                        break out;
                    }
                }
            }
        } catch (Exception ex) {
            _fatal = ex;
        } finally {
            try {
                _isDone = true;
                _parent.getMain().join();
            } catch (Exception ee) {
                if (_fatal != null) {
                    _fatal = ee;
                }
            }
        }
    }

    private boolean process(final int i) throws Exception {
        if ((i % 4) == 0) {
            _db.begin();
            LOG.debug("trying Query.execute()");
            OQLQuery oql = _db.getOQLQuery("SELECT object FROM " + TestRaceCondition.getClassName()
                    + " object WHERE id = $1");
            oql.bind(i);
            QueryResults enumeration = oql.execute();
            if (enumeration.hasMore()) {
                Race tr = (Race) enumeration.next();
                tr.incValue1();
                _db.commit();
                return true;
            }
            LOG.error("Error: element not found!! missed in cache?\n");
            rollback(_db);
            throw new NoSuchElementException("No element found (a).");
        } else if ((i % 4) == 1) {
            _db.begin();
            LOG.debug("trying Query.execute()");
            OQLQuery oql = _db.getOQLQuery("SELECT object FROM " + TestRaceCondition.getClassName()
                    + " object WHERE id = $1");
            oql.bind(i);
            Enumeration<?> enumeration = oql.execute();
            if (enumeration.hasMoreElements()) {
                Race tr = (Race) enumeration.nextElement();
                tr.incValue1();
                _db.commit();
                return true;
            }
            LOG.error("Error: element not found!! missed in cache?\n");
            rollback(_db);
            throw new NoSuchElementException("No element found (b).");
        } else if ((i % 4) == 2) {
            _db.begin();
            LOG.debug("trying Database.load()");
            Race tr = (Race) _db.load(TestRaceCondition.getClassType(), new Integer(i),
                Database.SHARED);
            if (tr != null) {
                tr.incValue1();
                _db.commit();
                return true;
            }
            LOG.error("Error: element not found!! missed in cache?\n");
            rollback(_db);
            throw new NoSuchElementException("No element found (c).");
        } else if ((i % 4) == 3) {
            _db.begin();
            LOG.debug("trying Database.load()");
            Race tr = (Race) _db.load(TestRaceCondition.getClassType(), new Integer(i),
                Database.EXCLUSIVE);
            if (tr != null) {
                tr.incValue1();
                _db.commit();
                return true;
            }
            LOG.error("Error: element not found!! missed in cache?\n");
            rollback(_db);
            throw new NoSuchElementException("No element found (c).");
        } else {
            throw new IllegalArgumentException("??????????????");
        }
    }

    private void rollbackExpected(final Database db, final Exception ex) {
        LOG.info("Excepted exception: ", ex);
        rollback(db);
    }

    private void rollbackUnexpected(final Database db, final Exception ex, final String msg) {
        LOG.error("Thread will be killed. " + msg, ex);
        rollback(db);
        TestRaceCondition.setLeak(true);
    }

    private void rollback(final Database db) {
        if (db.isActive()) {
            try {
                db.rollback();
            } catch (Exception ex) {
            }
        }
    }

    public boolean isDone() {
        return _isDone;
    }

    public Exception getException() {
        return _fatal;
    }
}