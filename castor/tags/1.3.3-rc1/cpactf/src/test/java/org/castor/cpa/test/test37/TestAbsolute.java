/*
 * Copyright 2008 Udai Gupta, Ralf Joachim
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
package org.castor.cpa.test.test37;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.castor.cpa.test.framework.CPATestCase;
import org.castor.cpa.test.framework.xml.types.DatabaseEngineType;
import org.castor.jdo.util.JDOUtils;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.QueryResults;

/**
 * Test for OQLResults.absolute().
 */
public final class TestAbsolute extends CPATestCase {
    private static final Log LOG = LogFactory.getLog(TestAbsolute.class);
    private static final String DBNAME = "test37";
    private static final String MAPPING = "/org/castor/cpa/test/test37/mapping.xml";
    private Database _db;

    /**
     * Constructor
     *
     * @param category The test suite of these tests
     */
    public TestAbsolute(final String name) {
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
    
    public void setUp() throws Exception {
        _db = getJDOManager(DBNAME, MAPPING).getDatabase();

        removeRecords();
        createRecords();
    }

    public void tearDown() throws PersistenceException {
        if (_db.isActive()) { _db.rollback(); }
        _db.close();
    }

    public void removeRecords() {
        Connection conn = null;
        try {
            _db.begin();
            conn = _db.getJdbcConnection();
            conn.createStatement().executeUpdate("DELETE FROM test37_entity");
            _db.commit();
        } catch (SQLException ex) {
            ex.printStackTrace();
        } catch (PersistenceException ex) {
            ex.printStackTrace();
        } finally {
            JDOUtils.closeConnection (conn);
        }
    }

    public void createRecords() throws PersistenceException {
        _db.begin();
            for (int i = 0; i < 25; i++) {
                Entity newTRN = new Entity();
                newTRN.setId(i);
                _db.create(newTRN);
            }
        _db.commit();
    }

    /**
     * Very simple test to do a query and call absolute(x)
     */
    public void testAbsoluteA() throws PersistenceException {
        _db.begin();
        OQLQuery oqlquery = _db.getOQLQuery(
                "SELECT object FROM " + Entity.class.getName() + " object");
        QueryResults enumeration = oqlquery.execute(true);
        assertTrue("should have been able to move to 1", enumeration.absolute(1));
        assertTrue("should have been able to move to 5", enumeration.absolute(5));
        assertTrue("should have been able to move to 10", enumeration.absolute(10));
        assertTrue("should have been able to move to 15", enumeration.absolute(15));
        assertTrue("should have been able to move to 20", enumeration.absolute(20));
        assertTrue("should have been able to move to 25", enumeration.absolute(25));
        _db.commit();
    }

    /**
     * Test going through enumeration and calling absolute
     * to simulate the .next(); call
     */
    public void testAbsoluteB() throws PersistenceException {
        _db.begin();
        OQLQuery oqlquery = _db.getOQLQuery(
                "SELECT object FROM " + Entity.class.getName() + " object");
        QueryResults enumeration = oqlquery.execute(true);
        int next = 1;
        boolean hasMore = true;
        while (enumeration.hasMore() && hasMore) {
            LOG.debug("at: " + next);
            hasMore = enumeration.absolute(next);
            next++;
        }
        _db.commit();
    }

    /**
     * Both should fail as -50 is less than 0 and 99999 is greater than
     * the 25 objects in the db
     */
    public void testAbsoluteC() throws PersistenceException {
        _db.begin();
        OQLQuery oqlquery = _db.getOQLQuery(
                "SELECT object FROM " + Entity.class.getName() + " object");
        QueryResults enumeration = oqlquery.execute(true);
        assertFalse("shouldn't be able to move to -50", enumeration.absolute(-50));
        assertFalse("shouldn't be able to move to 99999", enumeration.absolute(99999));
        _db.commit();
    }
}
