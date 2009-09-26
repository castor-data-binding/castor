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
import java.sql.ResultSet;
import java.sql.SQLException;

import org.castor.cpa.test.framework.CPATestCase;
import org.castor.cpa.test.framework.xml.types.DatabaseEngineType;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.PersistenceException;

/**
 * Verifies correct detachment behavior using a version field.
 * 
 * @author lukas.lang
 */
public final class VersionTest extends CPATestCase {
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
    public VersionTest(final String name) {
        super(name);
    }

    // Test are only included/excluded for engines that have been tested with this test suite.

    public boolean include(final DatabaseEngineType engine) {
        return (engine == DatabaseEngineType.DERBY)
            || (engine == DatabaseEngineType.HSQL)
            || (engine == DatabaseEngineType.MYSQL)
            || (engine == DatabaseEngineType.ORACLE)
            || (engine == DatabaseEngineType.POSTGRESQL);
    }
    
    @Override
    public void setUp() throws Exception {
        _db = getJDOManager(DBNAME, MAPPING).getDatabase();
        _db.getCacheManager().expireCache();
    }

    /**
     * Performs a simple long transaction with an update on an {@link Employee}
     * instance.
     * 
     * @throws PersistenceException if any persistence operation fails.
     * @throws SQLException in case clear of tables fails.
     */
    public void testDetachmentAndUpdate() throws PersistenceException, SQLException {
        Employee employee = new Employee();
        employee.setId(1);
        employee.setName("unit-test-name-1");
        employee.setVersion(0L);

        // Persist book.
        _db.begin();
        _db.create(employee);
        _db.commit();

        // CHange the title.
        employee.setName("new-unit-test-name-1");

        // Update book.
        _db.begin();
        _db.update(employee);
        _db.commit();

        // Verify book.
        _db.begin();
        Employee loadedEmployee = _db.load(Employee.class, 1L);
        _db.commit();

        assertNotNull(loadedEmployee);
        assertEquals("new-unit-test-name-1", loadedEmployee.getName());

        clearTables();
        _db.close();
    }

    /**
     * Performs a create on an {@link Employee} instance and assures that a
     * version is created.
     * 
     * @throws PersistenceException if any persistence operation fails.
     * @throws SQLException in case JDBC connection can not be obtained.
     */
    public void testCreate() throws PersistenceException, SQLException {
        Employee employee = new Employee();
        employee.setId(1L);
        employee.setName("unit-test-name-2");

        // Persist book.
        _db.begin();
        _db.create(employee);
        _db.commit();

        _db.begin();
        PreparedStatement prepareStatement =
                _db.getJdbcConnection().prepareStatement(
                        "SELECT version FROM test2763_employee WHERE id=1");
        ResultSet resultSet = prepareStatement.executeQuery();

        assertNotNull(resultSet);
        assertTrue(resultSet.next());

        // Assure, version is set.
        assertNotNull(resultSet.getLong(1));
        assertTrue(1 < resultSet.getLong(1));

        prepareStatement.close();
        _db.commit();

        clearTables();
        _db.close();
    }

    /**
     * Load an inserted {@link Employee} from the database.
     * 
     * @throws PersistenceException in case persistence fails.
     * @throws SQLException in case prepared statements fail.
     */
    public void testLoad() throws PersistenceException, SQLException {
        _db.begin();
        // Natively insert an employee.
        PreparedStatement prepareStatement =
                _db.getJdbcConnection().prepareStatement(
                                "INSERT INTO test2763_employee (id, name, version) "
                                        + "VALUES (1, 'unit-test-name-3', 20000000000)");
        prepareStatement.executeUpdate();
        prepareStatement.close();
        _db.commit();

        // Load employee.
        _db.begin();
        Employee employee = _db.load(Employee.class, 1L);
        _db.commit();

        // Assure, all fields are set.
        assertNotNull(employee);
        assertEquals("unit-test-name-3", employee.getName());
        assertEquals(20000000000L, employee.getVersion());

        clearTables();
        _db.close();
    }

    /**
     * Loads an {@link Employee} and verifies whether object modification check
     * on update works.
     * 
     * @throws SQLException in case insert fails.
     * @throws PersistenceException if any database operation fails.
     * @throws InterruptedException in case sleep fails.
     */
    public void testDirtyCheck() throws PersistenceException, SQLException,
            InterruptedException {
        Employee employee = new Employee();
        employee.setId(1L);
        employee.setName("unit-test-name-4");

        // Persist book.
        _db.begin();
        _db.create(employee);
        _db.commit();

        // Load employee.
        _db.begin();
        employee = _db.load(Employee.class, 1L);
        assertNotNull(employee);
        assertEquals("unit-test-name-4", employee.getName());
        _db.commit();

        // Busy waiting to let ensure different time stamps.
        Thread.sleep(10);

        // Load employee again.
        _db.begin();
        Employee employee2 = _db.load(Employee.class, 1L);

        // Modify employee and persist changes by committing.
        employee2.setName("new-unit-test-name-4");
        _db.commit();

        // Try to update the old employee.
        employee.setName("unit-test-should-fail-name");
        try {
            _db.begin();
            _db.update(employee);
            _db.commit();
            fail("Object version is old! Object should be detected as modified!");
        } catch (Exception e) {
            // Dirty check works fine.
            _db.rollback();
        }

        // Verify that only the first change was persisted..
        _db.begin();
        Employee loadedEmployee = _db.load(Employee.class, 1L);
        assertNotNull(loadedEmployee);
        assertEquals("new-unit-test-name-4", loadedEmployee.getName());
        _db.commit();

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
                        "DELETE FROM test2763_employee");
        deleteEmployees.executeUpdate();
        deleteEmployees.close();
        _db.commit();
    }
}
