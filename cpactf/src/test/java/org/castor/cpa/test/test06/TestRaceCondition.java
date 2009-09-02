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

import java.sql.Connection;
import java.sql.Statement;
import java.util.Enumeration;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.castor.cpa.test.framework.CPATestCase;
import org.castor.cpa.test.framework.xml.types.DatabaseEngineType;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.JDOManager;
import org.exolab.castor.jdo.OQLQuery;

/**
 * Concurrent stress test. Stress tests on data objects modification by multiple
 * thread and detect if by any race condition occurs.
 */
public final class TestRaceCondition extends CPATestCase {
    /** The <a href="http://jakarta.apache.org/commons/logging/">Jakarta Commons
     *  Logging</a> instance used for all logging. */
    private static final Log LOG = LogFactory.getLog(TestRaceCondition.class);

    private static final int NUM_OF_RACING_THREADS = 8;
    private static final int NUM_OF_VALUE_PAIRS = 4;
    private static final int NUM_OF_TRIALS = 5;

    private static final String DBNAME = "test06";
    private static final String MAPPING = "/org/castor/cpa/test/test06/mapping.xml";
    
    private JDOManager _jdo;

    private Database _masterDB;

    private Database[] _dbForRace;

    private Thread _thread;

    private static String _className;

    private static Class<?> _classType;

    private static boolean _leak;

    public TestRaceCondition(final String name) {
        super(name);
    }

    // Test are only included/excluded for engines that have been tested with this test suite.

    public boolean include(final DatabaseEngineType engine) {
        return (engine == DatabaseEngineType.HSQL)
            || (engine == DatabaseEngineType.MYSQL)
            || (engine == DatabaseEngineType.POSTGRESQL);
    }

    // Test fails on DERBY
    public boolean exclude(final DatabaseEngineType engine) {
        return (engine == DatabaseEngineType.DERBY);
    }

    public void setUp() throws Exception {
        _jdo = getJDOManager(DBNAME, MAPPING);

        _masterDB = _jdo.getDatabase();
        
        _dbForRace = new Database[NUM_OF_RACING_THREADS];
        for (int i = 0; i < NUM_OF_RACING_THREADS; i++) {
            _dbForRace[i] = _jdo.getDatabase();
        }
    }

    public void testRaceCondition() throws Exception {
        _thread = Thread.currentThread();

        for (int i = 0; i < 4; i++) {
            runOnce(i);
        }
    }

    public void tearDown() throws Exception {
        if (_masterDB.isActive()) {
            _masterDB.rollback();
        }
        _masterDB.close();

        for (int i = 0; i < NUM_OF_RACING_THREADS; i++) {
            if (_dbForRace[i].isActive()) {
                _dbForRace[i].rollback();
            }
            _dbForRace[i].close();
        }
    }

    public void runOnce(final int cachetype) throws Exception {
        // clear the table
        Connection conn = _jdo.getConnectionFactory().createConnection();
        Statement stmt = conn.createStatement();
        int del = stmt.executeUpdate("DELETE FROM test06_race");
        stmt.close();
        conn.close();
        LOG.debug("row deleted in table core_race: " + del);

        // create pairs of number
        _masterDB.begin();

        Race[] jdos = new Race[NUM_OF_VALUE_PAIRS];
        RaceSync[] controls = new RaceSync[NUM_OF_VALUE_PAIRS];
        switch (cachetype) {
        case 0:
            _classType = RaceCount.class;
            _className = _classType.getName();
            for (int i = 0; i < jdos.length; i++) {
                jdos[i] = new RaceCount();
                jdos[i].setId(i);
                _masterDB.create(jdos[i]);

                controls[i] = new RaceSync();
            }
            break;
        case 1:
            _classType = RaceTime.class;
            _className = _classType.getName();
            for (int i = 0; i < jdos.length; i++) {
                jdos[i] = new RaceTime();
                jdos[i].setId(i);
                _masterDB.create(jdos[i]);

                controls[i] = new RaceSync();
            }
            break;
        case 2:
            _classType = RaceNone.class;
            _className = _classType.getName();
            for (int i = 0; i < jdos.length; i++) {
                jdos[i] = new RaceNone();
                jdos[i].setId(i);
                _masterDB.create(jdos[i]);

                controls[i] = new RaceSync();
            }
            break;
        case 3:
            _classType = RaceUnlimited.class;
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
            ts[i] = new RaceThread(this, _dbForRace[i], controls, NUM_OF_TRIALS);
            ts[i].start();
        }

        // wait till everybody done
        boolean isAllDone = false;
        int num;
        while (!isAllDone) {
            Thread.sleep(200);
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
            OQLQuery oql = _masterDB.getOQLQuery("SELECT object FROM " + _className
                    + " object WHERE id = $1");
            oql.bind(i);
            Enumeration<?> enumeration = oql.execute();
            if (enumeration.hasMoreElements()) {
                Race tr = (Race) enumeration.nextElement();
                if (tr.getValue1() == controls[i].getValue1()) {
                    num++;
                }
                LOG.debug("Number Pair " + i + " -- JDO: " + tr.getValue1() + " control: "
                        + controls[i].getValue1() + " total trials: " + NUM_OF_TRIALS
                        * NUM_OF_RACING_THREADS);
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
        assertTrue("Value in database does not match the expected value", num == jdos.length);
        assertTrue("Leak detected", !_leak);
    }

    public Thread getMain() {
        return _thread;
    }

    public static String getClassName() {
        return _className;
    }

    public static void setClassName(final String className) {
        _className = className;
    }

    public static Class<?> getClassType() {
        return _classType;
    }

    public static void setClassType(final Class<?> classType) {
        _classType = classType;
    }

    public static boolean isLeak() {
        return _leak;
    }

    public static void setLeak(final boolean leak) {
        _leak = leak;
    }
}
