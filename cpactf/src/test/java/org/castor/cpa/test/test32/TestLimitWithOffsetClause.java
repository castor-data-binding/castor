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
package org.castor.cpa.test.test32;

import java.sql.Connection;
import java.sql.Statement;

import org.castor.cpa.test.framework.CPATestCase;
import org.castor.cpa.test.framework.xml.types.DatabaseEngineType;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.QueryResults;
import org.exolab.castor.jdo.oql.OQLSyntaxException;

public final class TestLimitWithOffsetClause extends CPATestCase {
    private static final int LIMIT = 5;
    private static final int OFFSET = 2;
    private static final String DBNAME = "test32";
    private static final String MAPPING = "/org/castor/cpa/test/test32/mapping.xml";
    private Database _db;

    /**
     * Constructor
     *
     * @param category The test suite for these tests
     */
    public TestLimitWithOffsetClause(final String name) {
        super(name);
    }  
    
    // Test are only included/excluded for engines that have been tested with this test suite.

    public boolean include(final DatabaseEngineType engine) {
        return (engine == DatabaseEngineType.MYSQL)
            || (engine == DatabaseEngineType.POSTGRESQL)
            || (engine == DatabaseEngineType.ORACLE);
    }

    /**
     * Get a JDO database
     */
    public void setUp() throws Exception {
        _db = getJDOManager(DBNAME, MAPPING).getDatabase();
        _db.begin();

        Connection connection = _db.getJdbcConnection();
        Statement statement = connection.createStatement();
        statement.executeUpdate("DELETE FROM test32_entity");
        statement.close();

        _db.commit();

        _db.begin();
        for (int i = 1; i < 16; i++) {
            Entity object = new Entity();
            object.setId(i);
            object.setValue1("val1" + i);
            object.setValue2("val2" + i);
            _db.create (object);
        }
        _db.commit();
    }

    public void tearDown() throws PersistenceException {
        if (_db.isActive()) { _db.rollback(); }
        _db.close();
    }

    public void testLimitWithOffset() throws PersistenceException {
        _db.begin();

        OQLQuery query = _db.getOQLQuery("select t from "
                + Entity.class.getName() + " t order by id limit $1 offset $2");

        query.bind(LIMIT);
        query.bind(OFFSET);

        QueryResults results = query.execute();
        assertNotNull(results);
        for (int i = 1 + OFFSET; i <= OFFSET + LIMIT; i++) {
            Entity testObject = (Entity) results.next();
            assertEquals(i, testObject.getId());
        }
        assertTrue(!results.hasMore());

        _db.commit();
    }

    public void testOffsetWithoutLimit() throws PersistenceException {
        _db.begin();
        try {
            _db.getOQLQuery(
                    "select t from " + Entity.class.getName() + " t offset $1");
        } catch (OQLSyntaxException ex) {
            assertEquals("org.exolab.castor.jdo.oql.OQLSyntaxException", ex.getClass().getName());
            return;
        } finally {
            _db.commit();
        }
    }
}
