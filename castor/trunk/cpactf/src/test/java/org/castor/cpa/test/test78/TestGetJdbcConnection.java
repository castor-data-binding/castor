/*
 * Copyright 2008 Udai Gupta
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
package org.castor.cpa.test.test78;

import java.sql.Connection;

import org.castor.cpa.test.framework.CPATestCase;
import org.castor.cpa.test.framework.xml.types.DatabaseEngineType;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.PersistenceException;

public final class TestGetJdbcConnection extends CPATestCase {
    private static final String DBNAME = "test78";
    private static final String MAPPING = "/org/castor/cpa/test/test78/mapping.xml";
    private Database _db;


    public TestGetJdbcConnection(final String name) {
        super(name);
    }

    // Test are only included/excluded for engines that have been tested with this test suite.

    public boolean include(final DatabaseEngineType engine) {
        return (engine == DatabaseEngineType.DERBY)
            || (engine == DatabaseEngineType.MYSQL)
            || (engine == DatabaseEngineType.POSTGRESQL);
    }

    public void setUp() throws Exception {
        _db = getJDOManager(DBNAME, MAPPING).getDatabase();
    }
    
    public void tearDown() throws Exception {
        if (_db.isActive()) {
            _db.rollback();
        }
        _db.close();
    }
    
    public void testGetJdbcConnection() throws Exception {
        _db.begin();
        Connection connection = _db.getJdbcConnection();
        assertNotNull(connection);
        _db.commit();
    }

    public void testGetJdbcConnectionWithoutActiveTransaction() throws Exception {
        try {
            _db.getJdbcConnection();
            fail("Should have received PersistenceException (no active transaction)");
        } catch (PersistenceException e) {
            assertEquals("No transaction in progress for the current thread. Please "
                    + "start a transaction before trying to obtain the JDBC connection",
                    e.getMessage());
        } 
    }
}
