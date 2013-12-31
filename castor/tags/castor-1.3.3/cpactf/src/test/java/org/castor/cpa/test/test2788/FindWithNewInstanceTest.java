/*
 * Copyright 2009 Lukas Lang
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
package org.castor.cpa.test.test2788;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.castor.cpa.test.framework.CPATestCase;
import org.castor.cpa.test.framework.xml.types.DatabaseEngineType;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.PersistenceException;

/**
 * Verifies the correct behavior of the
 * {@link Database#load(Class, Object, Object)} method.
 * 
 * @author lukas.lang
 */
public final class FindWithNewInstanceTest extends CPATestCase {
    /** Name of the database. */
    private static final String DBNAME = "test2788";

    /** Mapping file for this test case. */
    private static final String MAPPING = "/org/castor/cpa/test/test2788/mapping.xml";

    /** The {@link Database} to use. */
    private Database _db;

    /**
     * Constructor taking a name.
     * 
     * @param name the name of the test.
     */
    public FindWithNewInstanceTest(final String name) {
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

    @Override
    public void setUp() throws Exception {
        _db = getJDOManager(DBNAME, MAPPING).getDatabase();
        _db.getCacheManager().expireCache();
    }

    /**
     * Loads a {@link Book} into a given instance.
     * 
     * @throws PersistenceException in case persistence fails.
     * @throws SQLException in case clean up fails.
     * 
     */
    public void testLoadWithGivenInstance() throws PersistenceException, SQLException {
        Book book = new Book();
        book.setId(1);

        // Persist book.
        _db.begin();
        _db.create(book);
        _db.commit();

        // Load book into a new instance.
        _db.begin();
        Book newBook = new Book();
        Book returned = _db.load(Book.class, 1, newBook);
        _db.commit();

        clearTables();
        
        _db.close();

        assertNotSame(book, returned);
        assertSame(newBook, returned);
    }

    /**
     * Deletes all tuples from the tables 'detachment_employee'.
     * 
     * @throws PersistenceException if JDBC connection cannot be obtained.
     * @throws SQLException if execution fails.
     */
    protected void clearTables() throws PersistenceException, SQLException {
        _db.begin();
        PreparedStatement deleteEmployees = _db.getJdbcConnection().prepareStatement(
                "DELETE FROM test2788_book");
        deleteEmployees.executeUpdate();
        deleteEmployees.close();
        _db.commit();
    }
}