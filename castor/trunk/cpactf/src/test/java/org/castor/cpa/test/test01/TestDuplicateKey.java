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
package org.castor.cpa.test.test01;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.castor.cpa.test.framework.CPATestCase;
import org.castor.cpa.test.framework.xml.types.DatabaseEngineType;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.DuplicateIdentityException;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.QueryResults;

/**
 * Tests for duplicate key detection. 
 * 1/ Try to create an object with an identity same as another loaded
 *    (in memory) object.
 * 2/ Try to create an object with an identity same as another object what is
 *    not loaded by in the database.
 */
public final class TestDuplicateKey extends CPATestCase {
    /** The <a href="http://jakarta.apache.org/commons/logging/">Jakarta
     *  Commons Logging</a> instance used for all logging.*/
    private static final Log LOG = LogFactory.getLog(TestDuplicateKey.class);
    private static final String DBNAME = "test01";
    private static final String MAPPING = "/org/castor/cpa/test/test01/mapping.xml";
    private Database _db;

    public TestDuplicateKey(final String name) {
        super(name);
    }

    // Test are only included/excluded for engines that have been tested with this test suite.

    public boolean include(final DatabaseEngineType engine) {
        return (engine == DatabaseEngineType.DERBY)
            || (engine == DatabaseEngineType.HSQL)
            || (engine == DatabaseEngineType.MYSQL)
            || (engine == DatabaseEngineType.ORACLE)
            || (engine == DatabaseEngineType.POSTGRESQL)
            || (engine == DatabaseEngineType.SQL_SERVER);
    }

    /**
     * Creates data objects used by these tests
     */
    public void setUp() throws Exception {
        // Open transaction in order to perform JDO operations
        _db = getJDOManager(DBNAME, MAPPING).getDatabase();
        _db.begin();
        
        // Determine if test object exists, if not create it.
        // If it exists, set the name to some predefined value
        // that this test will later override.
        OQLQuery oql = _db.getOQLQuery("SELECT object FROM "
                + Sample.class.getName() + " object WHERE id = $1");
        oql.bind(Sample.DEFAULT_ID);
        
        QueryResults enumeration = oql.execute();
        Sample    object;
        if (enumeration.hasMore()) {
            object = (Sample) enumeration.next();
            LOG.debug("Updating object: " + object);
        } else {
            object = new Sample();
            LOG.debug("Creating new object: " + object);
            _db.create(object);
        }
        _db.commit();
    }

    /**
     * Close the database
     */
    public void tearDown() throws PersistenceException {
        if (_db.isActive()) { _db.rollback(); }
        _db.close();
    }

    /**
     * Try to create two objects with the same identity. This test passes 
     * if an DuplicateIdentityException is thrown when the second object 
     * with duplicated identity is created.
     */
    public void testDuplicateIdentityAsInMemory()
    throws PersistenceException {
        // Attempt to create a new object with the same identity,
        // while one is in memory. Will report duplicate key from
        // the cache engine.
        _db.begin();
        
        OQLQuery oql = _db.getOQLQuery("SELECT object FROM "
                + Sample.class.getName() + " object WHERE id = $1");
        oql.bind(new Integer(Sample.DEFAULT_ID));
        
        QueryResults enumeration = oql.execute();
        while (enumeration.hasMore()) {
            enumeration.next();
        }
        
        Sample object = new Sample();
        LOG.debug("Creating new object: " + object);
        LOG.debug("Will report duplicate identity from cache engine");
        try {
            _db.create(object);
            // expected exception
            fail("DuplicateIdentityException not thrown");
            LOG.error("Error: DuplicateIdentityException not thrown");
        } catch (DuplicateIdentityException except) {
            LOG.debug("OK: DuplicateIdentityException thrown");
        } catch (PersistenceException except) {
            LOG.error("Error: ", except);
            throw except;
        } finally {
            _db.commit();
        }
    }

    /**
     * Try to create an object that has an identity which is the same 
     * as another object that is not loaded by in the database.  This 
     * test case passes if a DuplicateIdentityException is thrown when 
     * the object with duplicated identity is created.
     */
    public void testDuplicateIdentityAsInDatabase()
    throws PersistenceException {
        _db.begin();
        
        Sample object = new Sample();
        LOG.debug("Creating new object: " + object);
        LOG.debug("Will report duplicate identity from SQL engine");
        try {
            _db.create(object);
            LOG.error("Error: DuplicateIdentityException not thrown");
        } catch (DuplicateIdentityException except) {
            LOG.debug("OK: DuplicateIdentityException thrown");
        } catch (PersistenceException except) {
            LOG.error("Error: ", except);
        } finally {
            _db.commit();
        }
    }
}
