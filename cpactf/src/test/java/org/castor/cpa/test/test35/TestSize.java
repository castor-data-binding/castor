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
package org.castor.cpa.test.test35;

import java.sql.Connection;
import java.sql.SQLException;

import org.castor.cpa.test.framework.CPATestCase;
import org.castor.cpa.test.framework.xml.types.DatabaseEngineType;
import org.castor.jdo.util.JDOUtils;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.QueryResults;

public final class TestSize extends CPATestCase {
    private static final String DBNAME = "test35";
    private static final String MAPPING = "/org/castor/cpa/test/test35/mapping.xml";
    private Database _db;

    /**
     * Constructor
     *
     * @param category The test suite of these tests
     */
    public TestSize(final String name) {
        super(name);
    }    
    
    // Test are only included/excluded for engines that have been tested with this test suite.

    public boolean include(final DatabaseEngineType engine) {
        return (engine == DatabaseEngineType.DERBY)
            || (engine == DatabaseEngineType.MYSQL)
            || (engine == DatabaseEngineType.POSTGRESQL)
            || (engine == DatabaseEngineType.ORACLE);
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
            conn.setAutoCommit(false);
            conn.createStatement().executeUpdate("DELETE FROM test35_entity");
            _db.commit();
        } catch (SQLException ex) {
            ex.printStackTrace();
        } catch (PersistenceException e) {
            e.printStackTrace();
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
     * Very simple test to do a query and call size()
     */
    public void testSizeA() throws PersistenceException {
        _db.begin();
        OQLQuery oqlquery = _db.getOQLQuery(
                "SELECT object FROM " + Entity.class.getName() + " object");
        QueryResults enumeration = oqlquery.execute(true);
        assertTrue("size should be > 0", enumeration.size() > 0);
        _db.commit();
    }

    /**
     * Test going through enumeration and calling size. This tests the 
     * implemention because it internally moves the cursor and then 
     * moves it back.
     */
    public void testSizeB() throws PersistenceException {
        _db.begin();
        OQLQuery oqlquery = _db.getOQLQuery(
                "SELECT object FROM " + Entity.class.getName() + " object");
        QueryResults enumeration = oqlquery.execute(true);
        while (enumeration.hasMore()) {
            enumeration.next();
            assertTrue("size should be > 0", enumeration.size() > 0);
            assertEquals("size should be ==25", enumeration.size(), 25);
        }
        _db.commit();
    }
    
    /**
     * Does size return the right results?
     */
    public void testSizeC() throws PersistenceException {
        _db.begin();
        OQLQuery oqlquery = _db.getOQLQuery(
                "SELECT object FROM " + Entity.class.getName() + " object");
        QueryResults enumeration = oqlquery.execute(true);
        int expectedSize = enumeration.size();
        int realSize = 0;
        while (enumeration.hasMore()) {
            enumeration.next();
            realSize++;
        }
        _db.commit();
        assertEquals("realsize didn't equal expectedsize", realSize, expectedSize);
    }

    /**
     * Shouldn't fail with a non scrollable resultset.
     */
    public void testSizeD() {
        try {
            _db.begin();
            OQLQuery oqlquery = _db.getOQLQuery(
                    "SELECT object FROM " + Entity.class.getName() + " object");
            oqlquery.execute(false);
            _db.commit();
            assertTrue(true);
        } catch (Exception ex) {
            fail("Calling size() on a non-scrollable ResultSet shouldn't fail.");
        }
    }    
}
