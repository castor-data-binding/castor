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
package org.castor.cpa.test.test2806;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.castor.cpa.test.framework.CPATestCase;
import org.castor.cpa.test.framework.xml.types.DatabaseEngineType;
import org.castor.persist.proxy.LazyCGLIB;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.PersistenceException;

/**
 * Verifies the correct behavior of lazy one to one relation.
 * 
 * @author lukas.lang
 */
public final class LazyTest extends CPATestCase {

    /** Name of the database. */
    private static final String DBNAME = "lazy";

    /** Mapping file for this test case. */
    private static final String MAPPING = "/org/castor/cpa/test/test2806/mapping.xml";

    /** The {@link Database} to use. */
    private Database _db;

    /**
     * Constructor taking a name.
     * 
     * @param name
     *            the name of the test.
     */
    public LazyTest(final String name) {
        super(name);
    }

    public boolean include(final DatabaseEngineType engine) {
        return (engine == DatabaseEngineType.DERBY);
    }

    @Override
    public void setUp() throws Exception {
        _db = getJDOManager(DBNAME, MAPPING).getDatabase();
        _db.getCacheManager().expireCache();
    }

    /**
     * Loads an entity from the database. The referenced parent object must not
     * be materialized as long as not accessed.
     * 
     * @throws PersistenceException
     *             in case persistence fails.
     * @throws SQLException
     *             in case clean up fails.
     * 
     */
    public void testLazyOneToOne() throws PersistenceException, SQLException {
        // Create entities.
        Parent parent = new Parent();
        parent.setId(1);
        Child child = new Child();
        child.setId(1);
        child.setParent(parent);

        // Persist entities.
        _db.setAutoStore(true);
        _db.begin();
        _db.create(child);
        _db.commit();
        _db.setAutoStore(false);

        // Clear cache.
        _db.getCacheManager().expireCache();

        // Obtain child.
        _db.begin();
        Child loadedChild = _db.load(Child.class, 1);
        _db.commit();

        assertNotNull(loadedChild);

        // Parent object must not be materialized.
        assertTrue(loadedChild.getParent() instanceof LazyCGLIB);
        LazyCGLIB proxy = (LazyCGLIB) loadedChild.getParent();
        assertFalse(proxy.interceptedHasMaterialized());

        clearTables();

        _db.close();
    }

    /**
     * Loads an entity from the database and updates it within a transaction.
     * The referenced parent object must not be materialized as long as not
     * accessed.
     * 
     * @throws PersistenceException
     *             in case persistence fails.
     * @throws SQLException
     *             in case clean up fails.
     * 
     */
    public void testUpdateChild() throws PersistenceException, SQLException {
        // Create entities.
        Parent parent = new Parent();
        parent.setId(1);
        Child child = new Child();
        child.setId(1);
        child.setTitle("unit-test-title");
        child.setParent(parent);

        // Persist entities.
        _db.setAutoStore(true);
        _db.begin();
        _db.create(child);
        _db.commit();
        _db.setAutoStore(false);

        // Clear cache.
        _db.getCacheManager().expireCache();

        // Obtain child.
        _db.begin();
        Child loadedChild = _db.load(Child.class, 1);
        loadedChild.setTitle("new-unit-test-title");
        _db.commit();

        assertNotNull(loadedChild);

        // Parent object must not be materialized.
        assertTrue(loadedChild.getParent() instanceof LazyCGLIB);
        LazyCGLIB proxy = (LazyCGLIB) loadedChild.getParent();
        assertFalse(proxy.interceptedHasMaterialized());
        
        clearTables();

        _db.close();
    }

    /**
     * Loads an entity from the database and updates the parent it within a
     * transaction. The referenced parent object must now be materialized.
     * 
     * @throws PersistenceException
     *             in case persistence fails.
     * @throws SQLException
     *             in case clean up fails.
     * 
     */
    public void testUpdateParent() throws PersistenceException, SQLException {
        // Create entities.
        Parent parent = new Parent();
        parent.setId(1);
        Child child = new Child();
        child.setId(1);
        child.setTitle("unit-test-title");
        child.setParent(parent);

        // Persist entities.
        _db.setAutoStore(true);
        _db.begin();
        _db.create(child);
        _db.commit();
        _db.setAutoStore(false);

        // Clear cache.
        _db.getCacheManager().expireCache();

        // Obtain child.
        _db.begin();
        Child loadedChild = _db.load(Child.class, 1);
        loadedChild.setTitle("new-unit-test-title");
        loadedChild.getParent().setTitle("parent-unit-test-title");
        _db.commit();

        assertNotNull(loadedChild);

        // Parent object must not be materialized.
        assertTrue(loadedChild.getParent() instanceof LazyCGLIB);
        LazyCGLIB proxy = (LazyCGLIB) loadedChild.getParent();
        assertTrue(proxy.interceptedHasMaterialized());

        clearTables();

        _db.close();
    }

    /**
     * Deletes all tuples from the tables 'detachment_employee'.
     * 
     * @throws PersistenceException
     *             if JDBC connection cannot be obtained.
     * @throws SQLException
     *             if execution fails.
     */
    protected void clearTables() throws PersistenceException, SQLException {
        _db.begin();
        PreparedStatement deleteChild = _db.getJdbcConnection().prepareStatement(
                "DELETE FROM test2806_child");
        deleteChild.executeUpdate();
        deleteChild.close();
        PreparedStatement deleteParent = _db.getJdbcConnection().prepareStatement(
                "DELETE FROM test2806_parent");
        deleteParent.executeUpdate();
        deleteParent.close();
        _db.commit();
    }
}