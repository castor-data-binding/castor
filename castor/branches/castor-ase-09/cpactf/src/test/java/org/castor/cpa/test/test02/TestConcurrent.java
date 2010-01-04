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
package org.castor.cpa.test.test02;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.castor.cpa.test.framework.CPATestCase;
import org.castor.cpa.test.framework.xml.types.DatabaseEngineType;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.JDOManager;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.ObjectModifiedException;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.QueryResults;
import org.exolab.castor.mapping.AccessMode;

/**
 * Concurrent access test. Tests a JDO modification and concurrent
 * JDBC modification to determine if JDO can detect the modification
 * with dirty checking.
 */
public final class TestConcurrent extends CPATestCase {
    public static final String JDBC_VALUE = "jdbc value";
    public static final String JDO_VALUE = "jdo value";
    public static final int WAIT_FOR_CONCURRENT_UPDATE = 2000;

    /** The <a href="http://jakarta.apache.org/commons/logging/">Jakarta
     *  Commons Logging</a> instance used for all logging. */
    private static final Log LOG = LogFactory.getLog(TestConcurrent.class);
    
    private static final String DBNAME = "test02";
    private static final String MAPPING = "/org/castor/cpa/test/test02/mapping.xml";
    
    private Database _db;

    /** 
     * Constructor
     */
    public TestConcurrent(final String name) {
        super(name);
    }

    // Test are only included/excluded for engines that have been tested with this test suite.

    public boolean include(final DatabaseEngineType engine) {
        return (engine == DatabaseEngineType.MYSQL)
            || (engine == DatabaseEngineType.ORACLE)
            || (engine == DatabaseEngineType.POSTGRESQL);
    }

    // Test fails on DERBY even if isolation levels and locking are supported
    // HSQL does not support isolation levels and locking
    public boolean exclude(final DatabaseEngineType engine) {
        return (engine == DatabaseEngineType.DERBY)
            || (engine == DatabaseEngineType.HSQL);
    }

    /**
     * Initializes fields
     */
    public void setUp() throws Exception {
        _db = getJDOManager(DBNAME, MAPPING).getDatabase();
    }

    /**
     * Close the database and JDBC connection
     */
    public void tearDown() throws PersistenceException, SQLException {
        if (_db.isActive()) { _db.rollback(); }
        _db.close();
    }

    /**
     * Test for concurrent modification detection in Shared Mode.
     * (Optimistic Locking Mode)
     * This test contains two parts. The first part tests if Castor 
     * JDO can detect concurrent modification done directly via
     * JDBC in Shared Mode. The second part test if Castor can
     * ignores concurrent modification done to fields that 
     * indicates dirty check should not be done.
     */
    public void testAccessModeShared() throws Exception {
        LOG.info("Running in access mode shared");
        
        // part 1
        runDirtyChecked(Database.SHARED);

        // part 2
        runDirtyIgnored(Database.SHARED);
    }

    /**
     * Test for concurrent modification detection in Exclusive Mode.
     * (Pessimistic Locking Mode)
     * This test contains two parts. The first part tests if Castor 
     * JDO can detect concurrent modification done directly via
     * JDBC in Shared Mode. The second part tests if Castor can
     * ignore concurrent modification done to fields that 
     * are indicated dirty check should not be done.
     */
    public void testAccessModeExclusive() throws Exception {
        LOG.info("Running in access mode exclusive");

        // part 1
        runDirtyChecked(Database.EXCLUSIVE);

        // part 2
        runDirtyIgnored(Database.EXCLUSIVE);
    }

    /**
     * Test for concurrent modification detection in DbLocked Mode.
     * (Pessimistic Locking Mode plus database row lock)
     * This test contains two parts. The first part tests if Castor 
     * JDO can detect concurrent modification done directly via
     * JDBC in Shared Mode. The second part tests if Castor can
     * ignores concurrent modification done to fields that are
     * indicated dirty check should not be done.
     * (note: some databases don't support database lock and will
     * fails this test case)
     */
    public void testAccessModeDbLocked() throws Exception {
        LOG.info("Running in access mode db-locked");

        // part 1
        runDirtyChecked(Database.DBLOCKED);

        // part 2
        runDirtyIgnored(Database.DBLOCKED);
    }

    /**
     * This method is called by the tests and preform the actual
     * concurrent modification test.
     *
     * @param accessMode the access mode that is used in the concurrent
     *        modification tests
     */
    private void runDirtyChecked(final AccessMode accessMode) throws Exception {
        JDOManager jdo = getJDOManager(DBNAME, MAPPING);

        OQLQuery       oql;
        Sample         object;
        QueryResults   enumeration;

        // Open transaction in order to perform JDO operations
        _db.begin();
    
        // Determine if test object exists, if not create it.
        // If it exists, set the name to some predefined value
        // that this test will later override.
        oql = _db.getOQLQuery("SELECT object FROM "
                + Sample.class.getName() + " object WHERE id = $1");
        oql.bind(Sample.DEFAULT_ID);

        enumeration = oql.execute();
        if (enumeration.hasMore()) {
            object = (Sample) enumeration.next();
            LOG.debug("Retrieved object: " + object);
            object.setValue1(Sample.DEFAULT_VALUE_1);
            object.setValue2(Sample.DEFAULT_VALUE_2);
        } else {
            object = new Sample();
            LOG.debug("Creating new object: " + object);
            _db.create(object);
        }

        _db.commit();
        
        // Open a new transaction in order to conduct test
        _db.begin();
        
        oql.bind(new Integer(Sample.DEFAULT_ID));
        object = (Sample) oql.execute(accessMode).nextElement();
        object.setValue1(JDO_VALUE);
        
        // Perform direct JDBC access and override the value of that table
        if (accessMode != Database.DBLOCKED) {
            Connection conn = jdo.getConnectionFactory().createConnection();
            Statement stmt = conn.createStatement();
            stmt.execute("UPDATE test02_sample SET value1='" + JDBC_VALUE + "' "
                    + "WHERE id=" + Sample.DEFAULT_ID);
            stmt.close();
            conn.close();
            LOG.debug("OK: Updated object from JDBC");
        } else {
            Thread th = new ConcurrentUpdateThread(jdo);
            th.start();
            synchronized (this) {
                try {
                    wait(WAIT_FOR_CONCURRENT_UPDATE);
                    if (th.isAlive()) {
                        th.interrupt();
                        LOG.debug("OK: Cannot update object from JDBC");
                    } else {
                        LOG.error("Error: Updated object from JDBC");
                        fail("Updated test object from JDBC");
                    }
                } catch (InterruptedException ex) {
                }
            }
        }

        // Commit JDO transaction, this should report object modified exception
        LOG.debug("Committing JDO update: dirty checking field modified");
        if (accessMode != Database.DBLOCKED) {
            try {
                _db.commit();
                LOG.error("Error: ObjectModifiedException not thrown");
                fail("ObjectModifiedException not thrown");
            } catch (ObjectModifiedException ex) {
                LOG.debug("OK: ObjectModifiedException thrown");
            }
        } else {
            try {
                _db.commit();
                LOG.debug("OK: ObjectModifiedException not thrown");
                // After _db.commit the concurrent update will be performed.
                // and we need to undo it.
                Connection conn = jdo.getConnectionFactory().createConnection();
                Statement stmt = conn.createStatement();
                stmt.execute("UPDATE test02_sample SET value1='" + JDO_VALUE + "' "
                        + "WHERE id=" + Sample.DEFAULT_ID);
                stmt.close();
                conn.close();
            } catch (ObjectModifiedException ex) {
                _db.rollback();
                LOG.error("Error: ObjectModifiedException thrown");
                fail("ObjectModifiedException not thrown");
            }
        }
    }

    /**
     * This method is called by the tests and preform the actual
     * concurrent modification test.
     *
     * @param accessMode the access mode that is used in the concurrent
     *        modification tests
     */
    private void runDirtyIgnored(final AccessMode accessMode) throws Exception {
        JDOManager jdo = getJDOManager(DBNAME, MAPPING);

        Sample object;

        // Open transaction in order to perform JDO operations
        _db.begin();
    
        // Determine if test object exists, if not create it.
        // If it exists, set the name to some predefined value
        // that this test will later override.
        OQLQuery oql = _db.getOQLQuery("SELECT object FROM "
                + Sample.class.getName() + " object WHERE id = $1");
        oql.bind(Sample.DEFAULT_ID);
        QueryResults enumeration = oql.execute();
        if (enumeration.hasMore()) {
            object = (Sample) enumeration.next();
            LOG.debug("Retrieved object: " + object);
            object.setValue1(Sample.DEFAULT_VALUE_1);
            object.setValue2(Sample.DEFAULT_VALUE_2);
        } else {
            object = new Sample();
            LOG.debug("Creating new object: " + object);
            _db.create(object);
        }
        
        _db.commit();

        // Open a new transaction in order to conduct test
        _db.begin();
        
        oql.bind(new Integer(Sample.DEFAULT_ID));
        object = (Sample) oql.execute(accessMode).nextElement();
        object.setValue2(JDO_VALUE);
        
        // Perform direct JDBC access and override the value of that table
        if (accessMode != Database.DBLOCKED) {
            Connection conn = jdo.getConnectionFactory().createConnection();
            Statement stmt = conn.createStatement();
            stmt.execute("UPDATE test02_sample SET value2='" + JDBC_VALUE + "' "
                    + "WHERE id=" + Sample.DEFAULT_ID);
            stmt.close();
            conn.close();
            LOG.debug("Updated test object from JDBC");
        }
    
        // Commit JDO transaction, this should report object modified exception
        LOG.debug("Commit update: no dirty checking field not modified");
        try {
            _db.commit();
            LOG.debug("OK: ObjectModifiedException not thrown");
        } catch (ObjectModifiedException ex) {
            if (_db.isActive()) { _db.rollback(); }
            LOG.error("Error: ObjectModifiedException thrown", ex);
            fail("ObjectModifiedException thrown");
        }
    }
}
