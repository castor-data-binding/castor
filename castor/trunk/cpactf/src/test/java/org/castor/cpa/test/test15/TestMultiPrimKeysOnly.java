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
package org.castor.cpa.test.test15;

import java.sql.Connection;
import java.sql.Statement;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.castor.cpa.test.framework.CPATestCase;
import org.castor.cpa.test.framework.xml.types.DatabaseEngineType;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.persist.spi.Identity;

/**
 * Test for multiple columns primary Keys. These tests create data objects
 * model of different types that make uses of primary keys, modify the objects
 * and the relationship between objects and verify if changes made is persisted
 * properly.
 */
public final class TestMultiPrimKeysOnly extends CPATestCase {
    /**
     * The <a href="http://jakarta.apache.org/commons/logging/">Jakarta
     * Commons Logging</a> instance used for all logging.
     */
    private static final Log LOG = LogFactory.getLog(TestMultiPrimKeysOnly.class);
    
    private static final String DBNAME = "test15";
    private static final String MAPPING = "/org/castor/cpa/test/test15/mapping.xml";
    private Database _db;

    /**
     * Constructor
     *
     * @param category The test suite of these test cases
     */
    public TestMultiPrimKeysOnly(final String name) {
        super(name);
    }

    // Test are only included/excluded for engines that have been tested with this test suite.

    public boolean include(final DatabaseEngineType engine) {
        return (engine == DatabaseEngineType.MYSQL)
            || (engine == DatabaseEngineType.DERBY);
    }
    
    /**
     * Get a JDO database and direct JDBC connection. Clean up old values in
     * tables using JDBC conneciton and create different types of data object
     * that make use of multiple columns primary keys.
     */
    public void setUp() throws Exception {
        _db = getJDOManager(DBNAME, MAPPING).getDatabase();
        _db.begin();
        Connection conn = _db.getJdbcConnection();

        // delete everything directly
        LOG.debug("Delete everything");
        Statement stmt = conn.createStatement();
        stmt.executeUpdate("DELETE FROM test15_only");
        _db.commit();
    }
    
    /**
     * Release the JDO Database
     */
    public void tearDown() throws PersistenceException {
        if (_db.isActive()) { _db.rollback(); }
        _db.close();
    }
    
    public void testCreateAndLoadOnly() throws PersistenceException {
        _db.begin();
        PrimaryKeysOnly only = new PrimaryKeysOnly();
        only.setFirstName("werner");
        only.setLastName("guttmann");
        _db.create(only);
        _db.commit();
        
        _db.begin();
        Identity identity = new Identity(new Object[] {"werner", "guttmann"});
        PrimaryKeysOnly searched = (PrimaryKeysOnly) _db.load(PrimaryKeysOnly.class, identity);
        assertNotNull(searched);
        assertEquals("werner", searched.getFirstName());
        assertEquals("guttmann", searched.getLastName());
        _db.commit();
        
        _db.begin();
        PrimaryKeysOnly toDelete = (PrimaryKeysOnly) _db.load(PrimaryKeysOnly.class, identity);
        assertNotNull(toDelete);
        _db.remove(toDelete);
        _db.commit();
    }
}
