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
 * Copyright 1999 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id$
 */

package ctf.jdo.tc0x;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Random;

import harness.CastorTestCase;
import harness.TestHarness;

import jdo.JDOCategory;

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

/**
 * Concurrent stress test. Stress tests on data objects modification
 * by multiple thread and detect if by any race condition occurs.
 */
public final class TestRaceCondition extends CastorTestCase {
    /**
     * The <a href="http://jakarta.apache.org/commons/logging/">Jakarta
     * Commons Logging</a> instance used for all logging.
     */
    private static final Log LOG = LogFactory.getLog(TestRaceCondition.class);
    
    private static final int NUM_OF_RACING_THREADS = 8;

    private static final int NUM_OF_VALUE_PAIRS = 4;

    private static final int NUM_OF_TRIALS = 5;

    private JDOCategory    _category;

    private Database       _masterDB;

    private Database[]     _dbForRace;

    private Connection     _conn;

    private String         _className;

    private Thread         _thread;

    private Class          _classType;

    private boolean        _leak;

    public TestRaceCondition(final TestHarness category) {
        super(category, "TC06", "Race tests");
        _category = (JDOCategory) category;
    }

    public void setUp() throws PersistenceException, SQLException {
        _dbForRace = new Database[NUM_OF_RACING_THREADS];
        for (int i = 0; i < NUM_OF_RACING_THREADS; i++) {
            _dbForRace[i] = _category.getDatabase();
        }
        
        _masterDB = _category.getDatabase();
        
        _conn = _category.getJDBCConnection();
        _conn.setAutoCommit(false);
    }

    public void runTest() throws Exception {
        _thread = Thread.currentThread();

        for (int i = 0; i < 4; i++) {
            runOnce(i);
        }
    }

    public void tearDown() throws Exception {
        _conn.close();

        if (_masterDB.isActive()) { _masterDB.rollback(); }
        _masterDB.close();
        
        for (int i = 0; i < NUM_OF_RACING_THREADS; i++) {
            if (_dbForRace[i].isActive()) { _dbForRace[i].rollback(); }
            _dbForRace[i].close();
        }
    }

    private Thread getMain() {
        return _thread;
    }

    public void runOnce(final int cachetype) throws Exception {
        Enumeration     enumeration;
        OQLQuery        oql;

        // clear the table
        int del = _conn.createStatement().executeUpdate(
                "DELETE FROM tc0x_race");
        LOG.debug("row deleted in table core_race: " + del);
        _conn.commit();

        // create pairs of number
        _masterDB.begin();
        
        Race[] jdos = new Race[NUM_OF_VALUE_PAIRS];
        RaceSync[] controls = new RaceSync[NUM_OF_VALUE_PAIRS];
        switch (cachetype) {
        case 0:
            _classType = ctf.jdo.tc0x.RaceCount.class;
            _className = _classType.getName();
            for (int i = 0; i < jdos.length; i++) {
                jdos[i] = new RaceCount();
                jdos[i].setId(i);
                _masterDB.create(jdos[i]);
                
                controls[i] = new RaceSync();
            }
            break;
        case 1:
            _classType = ctf.jdo.tc0x.RaceTime.class;
            _className = _classType.getName();
            for (int i = 0; i < jdos.length; i++) {
                jdos[i] = new RaceTime();
                jdos[i].setId(i);
                _masterDB.create(jdos[i]);
                
                controls[i] = new RaceSync();
            }
            break;
        case 2:
            _classType = ctf.jdo.tc0x.RaceNone.class;
            _className = _classType.getName();
            for (int i = 0; i < jdos.length; i++) {
                jdos[i] = new RaceNone();
                jdos[i].setId(i);
                _masterDB.create(jdos[i]);
                
                controls[i] = new RaceSync();
            }
            break;
        case 3:
            _classType = ctf.jdo.tc0x.RaceUnlimited.class;
            _className = _classType.getName();
            for (int i = 0; i < jdos.length; i++) {
                jdos[i] = new RaceUnlimited();
                jdos[i].setId(i);
                _masterDB.create(jdos[i]);
                
                controls[i] = new RaceSync();
            }
            break;
        default:
            LOG.error("Unknown cache type");
        }

        _masterDB.commit();

        // create threads, make a race so each thread
        // keeping increment to the pairs of number.
        RaceThread[] ts = new RaceThread[NUM_OF_RACING_THREADS];

        for (int i = 0; i < ts.length; i++) {
            ts[i] = new RaceThread(this, _dbForRace[i], controls,
                                   cachetype, NUM_OF_TRIALS);
            ts[i].start();
        }

        // wait till everybody done
        boolean isAllDone = false;
        int num;
        while (!isAllDone) {
            Thread.sleep(1000);
            num = 0;
            for (int i = 0; i < ts.length; i++) {
                if (ts[i].isDone()) {
                    num++;
                }
            }
            if (num == ts.length) {
                isAllDone = true;
            }
        }

        // see if their sum agree
        _masterDB.begin();
        
        num = 0;
        for (int i = 0; i < jdos.length; i++) {
            oql = _masterDB.getOQLQuery("SELECT object FROM " + _className
                    + " object WHERE id = $1");
            oql.bind(i);
            enumeration = oql.execute();
            if (enumeration.hasMoreElements()) {
                Race tr = (Race) enumeration.nextElement();
                if (tr.getValue1() == controls[i].getValue1()) { num++; }
                LOG.debug("Number Pair " + i + " -- JDO: "
                        + tr.getValue1() + " control: "
                        + controls[i].getValue1() + " total trials: "
                        + NUM_OF_TRIALS * NUM_OF_RACING_THREADS);
                LOG.debug("Test passes if Number Pair equals control.");
                LOG.debug("Total trails often not equals control. "
                        + "The reason for this is, that if two thread are "
                        + "interested on the same object only one will "
                        + "get the lock and the other will abort.");
            }
        }
        
        _masterDB.commit();

        for (int i = 0; i < ts.length; i++) {
            if (ts[i].getException() != null) {
                LOG.error("Fatal exception thrown: ", ts[i].getException());
                fail("Fatal exception thrown");
            }
        }
        
        // report result
        assertTrue("Value in database does not match the expected value",
                num == jdos.length);
        assertTrue("Leak detected", !_leak);
    }
    
    private class RaceThread extends Thread {
        private TestRaceCondition   _parent;
        private Database            _db;
        private RaceSync[]          _race;
        private int                 _cachetype;
        private int                 _trials;
        private Random              _random;
        private boolean             _isDone;
        private Exception           _fatal;

        RaceThread(final TestRaceCondition parent, final Database db,
                   final RaceSync[] race, final int cachetype,
                   final int trials) {
            
            _parent      = parent;
            _db          = db;
            _race         = race;
            _cachetype   = cachetype;
            _trials      = trials;
            _random      = new Random();
        }

        public void run() {
            try {
                LOG.info("start testing");
                out:
                for (int j = 0; j < _trials; j++) {
                    some:
                    for (int i = 0; i < _race.length; i++) {
                        boolean isOk = false;
                        int count = 0;

                        // select and inc the jdo object.
                        little:
                        while (!isOk) {
                            try {
                                isOk = process(i);
                            } catch (TransactionAbortedException ex) {
                                // this exception should happen one in a while.
                                count++;
                                rollbackExpected(_db, ex);
                                if (count > 10) { break some; }
                            } catch (LockNotGrantedException ex) {
                                count++;
                                rollbackExpected(_db, ex);
                                if (count > 10) { break some; }
                            } catch (QueryException ex) {
                                rollbackUnexpected(_db, ex,
                                        "Unexcepted exception: ");
                                break out;
                            } catch (TransactionNotInProgressException ex) {
                                rollbackUnexpected(_db, ex,
                                        "Unexcepted exception: ");
                                break out;
                            } catch (PersistenceException ex) {
                                rollbackUnexpected(_db, ex,
                                        "Unexcepted exception: ");
                                break out;
                            } catch (NoSuchElementException ex) {
                                rollbackUnexpected(_db, ex,
                                        "Element not found (leakage): ");
                                break out;
                            } catch (Exception ex) {
                                rollbackUnexpected(_db, ex,
                                        "Element not found (other): ");
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
                    if (_fatal != null) { _fatal = ee; }
                }
            }
        }
        
        private boolean process(final int i) throws Exception {
            if ((i % 4) == 0) {
                _db.begin();
                OQLQuery oql = _db.getOQLQuery("SELECT object FROM "
                        + _className + " object WHERE id = $1");
                oql.bind(i);
                QueryResults enumeration = oql.execute();
                if (enumeration.hasMore()) {
                    Race tr = (Race) enumeration.next();
                    tr.incValue1();
                    _db.commit();
                    return true;
                } else {
                    LOG.error("Error: element not found!! missed in cache?\n");
                    rollback(_db);
                    throw new NoSuchElementException("No element found (a).");
                }
            } else if ((i % 4) == 1) {
                _db.begin();
                OQLQuery oql = _db.getOQLQuery("SELECT object FROM "
                        + _className + " object WHERE id = $1");
                oql.bind(i);
                Enumeration enumeration = oql.execute();
                if (enumeration.hasMoreElements()) {
                    Race tr = (Race) enumeration.nextElement();
                    tr.incValue1();
                    _db.commit();
                    return true;
                } else {
                    LOG.error("Error: element not found!! missed in cache?\n");
                    rollback(_db);
                    throw new NoSuchElementException("No element found (b).");
                }
            } else if ((i % 4) == 2) {
                _db.begin();
                LOG.debug("trying Database.load()");
                Race tr = (Race) _db.load(_classType, new Integer(i),
                                         Database.Shared);
                if (tr != null) {
                    tr.incValue1();
                    _db.commit();
                    return true;
                } else {
                    LOG.error("Error: element not found!! missed in cache?\n");
                    rollback(_db);
                    throw new NoSuchElementException("No element found (c).");
                }
            } else if ((i % 4) == 3) {
                _db.begin();
                LOG.debug("trying Database.load()");
                Race tr = (Race) _db.load(_classType, new Integer(i),
                                         Database.Exclusive);
                if (tr != null) {
                    tr.incValue1();
                    _db.commit();
                    return true;
                } else {
                    LOG.error("Error: element not found!! missed in cache?\n");
                    rollback(_db);
                    throw new NoSuchElementException("No element found (c).");
                }
            } else {
                throw new IllegalArgumentException("??????????????");
            }
        }

        private void rollbackExpected(final Database db, final Exception ex) {
            LOG.info("Excepted exception: ", ex);
            rollback(db);
        }
        
        private void rollbackUnexpected(final Database db, final Exception ex,
                                        final String msg) {
            
            LOG.error("Thread will be killed. " + msg, ex);
            rollback(db);
            _leak = true;
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
    
    private class NoSuchElementException extends Exception {
        NoSuchElementException(final String name) {
            super(name);
        }
    }
}

