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
package org.castor.cpa.test.test2763;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.castor.cpa.test.framework.CPATestCase;
import org.castor.cpa.test.framework.xml.types.DatabaseEngineType;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.PersistenceException;

/**
 * Verifies correct detachment behavior implementing {@link TimeStampable}
 * interface.
 * 
 * @author lukas.lang
 */
public final class TimeStampableTest extends CPATestCase {
    /** Name of the database. */
    private static final String DBNAME = "test2763";

    /** Mapping file for this test case. */
    private static final String MAPPING = "/org/castor/cpa/test/test2763/mapping.xml";

    /** The {@link Database} to use. */
    private Database _db;

    /**
     * Constructor taking a name.
     * 
     * @param name the name of the test.
     */
    public TimeStampableTest(final String name) {
        super(name);
    }

    // Test are only included/excluded for engines that have been tested with this test suite.

    public boolean include(final DatabaseEngineType engine) {
        return (engine == DatabaseEngineType.DERBY);
    }
    
    @Override
    public void setUp() throws Exception {
        _db = getJDOManager(DBNAME, MAPPING).getDatabase();
    }

    /**
     * Performs a simple long transaction with an update on a {@link Book}
     * instance.
     * 
     * @throws PersistenceException if any persistence operation fails.
     * @throws SQLException in case clear tables fails.
     */
    public void testDetachment() throws PersistenceException, SQLException {
        Book book = new Book();
        book.setId(1);
        book.setTitle("unit-test-title");

        // Persist book.
        _db.begin();
        _db.create(book);
        _db.commit();

        // Change the title.
        book.setTitle("new-unit-test-title");

        // Update book.
        _db.begin();
        _db.update(book);
        _db.commit();

        // Verify book.
        _db.begin();
        Book loadedBook = _db.load(Book.class, 1);
        _db.commit();

        assertNotNull(loadedBook);
        assertEquals("new-unit-test-title", book.getTitle());

        clearTables();
        
        _db.close();
    }

    /**
     * Performs a simple long transaction with an update on a {@link Book}
     * instance.
     * 
     * @throws PersistenceException if any persistence operation fails.
     * @throws SQLException if clear of tables fails.
     */
    public void testDetachmentWithLoadedObject() throws PersistenceException, SQLException {
        Book book = new Book();
        book.setId(1);
        book.setTitle("unit-test-title");

        // Persist book.
        _db.begin();
        _db.create(book);
        _db.commit();

        _db.begin();
        book = _db.load(Book.class, 1);
        _db.commit();

        // Change the title.
        book.setTitle("new-unit-test-title");

        // Update book.
        _db.begin();
        _db.update(book);
        _db.commit();

        // Verify book.
        _db.begin();
        Book loadedBook = _db.load(Book.class, 1);
        _db.commit();

        assertNotNull(loadedBook);
        assertEquals("new-unit-test-title", book.getTitle());

        clearTables();
        _db.close();
    }

    /**
     * Deletes all tuples from the tables 'detachment_employee'.
     * 
     * @throws PersistenceException if JDBC connection cannot be obtained.
     * @throws SQLException if execution fails.
     */
    protected void clearTables() throws PersistenceException, SQLException {
        _db.begin();
        PreparedStatement deleteEmployees =
                _db.getJdbcConnection().prepareStatement(
                        "DELETE FROM test2763_book");
        deleteEmployees.executeUpdate();
        deleteEmployees.close();
        _db.commit();
    }
}
