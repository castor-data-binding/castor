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
package org.castor.cpa.test.test29;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.castor.cpa.test.framework.CPATestCase;
import org.castor.cpa.test.framework.xml.types.DatabaseEngineType;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.PersistenceException;

public final class TestDependentOrder extends CPATestCase {
    private static final Log LOG = LogFactory.getLog(TestDependentOrder.class);
    private static final String DBNAME = "test29";
    private static final String MAPPING = "/org/castor/cpa/test/test29/mapping.xml";
    private Database _db;

    /**
     * Constructor
     * @param category The test suite for these tests
     */
    public TestDependentOrder(final String name) {
        super(name);
    }

    // Test are only included/excluded for engines that have been tested with this test suite.

    public boolean include(final DatabaseEngineType engine) {
        return (engine == DatabaseEngineType.DERBY)
            || (engine == DatabaseEngineType.HSQL)
            || (engine == DatabaseEngineType.MYSQL)
            || (engine == DatabaseEngineType.ORACLE)
            || (engine == DatabaseEngineType.POSTGRESQL)
            || (engine == DatabaseEngineType.SQL_SERVER)
            || (engine == DatabaseEngineType.SAPDB);
    }

    /**
     * Get a JDO database
     */
    public void setUp() throws Exception {
        _db = getJDOManager(DBNAME, MAPPING).getDatabase();
        _db.begin();
        Connection conn = _db.getJdbcConnection();
        LOG.debug("Delete everything");
        Statement stmt = conn.createStatement();
        stmt.executeUpdate("delete from test29_depend2");
        stmt.executeUpdate("delete from test29_depend_master");
        stmt.executeUpdate("delete from test29_depend1");
        _db.commit();
    }

    public void testRun() throws PersistenceException {
        _db.begin();

        LOG.debug("Build master object and its dependent objects");

        // no ids needed, they come from the key-gen
        DependMaster master = new DependMaster();
        Depend1 depend1 = new Depend1();
        master.setDepend1(depend1);
        Depend2 depend2 = new Depend2();
        master.addDepend2(depend2);

        LOG.debug("Create object tree in db");
        _db.create(master);
        _db.commit();
        LOG.debug("depend1_id after creation : " + master.getDepend1().getId());

        _db.begin();
        try {
            LOG.debug("read depend1_id from db");
            PreparedStatement pstmt = _db.getJdbcConnection().prepareStatement(
                    "select depend1_id from test29_depend_master where id=?");
            LOG.debug("master id: " + master.getId());
            pstmt.setInt(1, master.getId());
            pstmt.execute();
            ResultSet result = pstmt.getResultSet();
            if (!result.next()) {
                LOG.error("Master object not created");
                fail("Master object not created");
            }

            LOG.debug("depend1_id in db : " + result.getInt("depend1_id"));

            if (result.getInt("depend1_id") == 0) {
                LOG.error("Depend1 object not linked to Master object");
                fail("Depend1 object not linked to Master object");
            }
        } catch (SQLException e) {
            LOG.error("Exception when checking master object row", e);
            fail("Exception when checking master object row: " + e);
        }

        _db.commit();

        // test for bug 973 Dependent objects deletion order problem
        try {
            LOG.debug("Deleting master object");
            _db.begin();
            master = _db.load(DependMaster.class, new Integer(master.getId()));
            _db.remove(master);
            _db.commit();
        } catch (Exception e) {
            LOG.error("Exception thrown", e);
            fail("Exception thrown " + e);
        }
    }

    public void tearDown() throws PersistenceException {
        if (_db.isActive()) { _db.rollback(); }
        _db.close();
    }
}
