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
package org.castor.cpa.test.test03;

import java.util.Enumeration;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.castor.cpa.test.framework.CPATestCase;
import org.castor.cpa.test.framework.xml.types.DatabaseEngineType;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.PersistenceException;

/**
 * Tests that modification to read only objects are not persist in the database.
 */
public final class TestReadOnly extends CPATestCase {
    /** The <a href="http://jakarta.apache.org/commons/logging/">Jakarta
     *  Commons Logging</a> instance used for all logging. */
    private static final Log LOG = LogFactory.getLog(TestReadOnly.class);
    private static final String NEW_VALUE = "new value";
    private static final String DBNAME = "test03";
    private static final String MAPPING = "/org/castor/cpa/test/test03/mapping.xml";
    
    private Database _db;

    public TestReadOnly(final String name) {
        super(name);
    }

    // Test are only included/excluded for engines that have been tested with this test suite.

    public boolean include(final DatabaseEngineType engine) {
        return (engine == DatabaseEngineType.DERBY)
            || (engine == DatabaseEngineType.MYSQL)
            || (engine == DatabaseEngineType.ORACLE);
    }

    /**
     * Get a jdo.Database and create a test object for readOnly test
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

        Enumeration<?> enumeration = oql.execute();
        Sample    object;
        if (enumeration.hasMoreElements()) {
            object = (Sample) enumeration.nextElement();
            object.setValue1(Sample.DEFAULT_VALUE_1);
            object.setValue2(Sample.DEFAULT_VALUE_2);
        } else {
            object = new Sample();
            LOG.debug("Creating new object: " + object);
            _db.create(object);
        } 
        
        _db.commit();
    }

    public void tearDown() throws PersistenceException {
        if (_db.isActive()) { _db.rollback(); }
        _db.close();
    }

    /**
     * Tests that modification to read only objects are not persist in the 
     * database.
     */
    public void testReadOnly() throws PersistenceException {
        OQLQuery oql;
        Sample object;
        Enumeration<?> enumeration;

        // load an object using readOnly mode
        _db.begin();
        
        oql = _db.getOQLQuery("SELECT object FROM "
                + Sample.class.getName() + " object WHERE id = $1");
        oql.bind(Sample.DEFAULT_ID);
        
        enumeration = oql.execute(Database.READONLY);
        object = (Sample) enumeration.nextElement();
        LOG.debug("Retrieved object: " + object);
        object.setValue1(NEW_VALUE);
        LOG.debug("Modified object: " + object);
        
        _db.commit();
        
        // read the object from another transaction to see
        // if changes is not persisted.
        _db.begin();
        
        oql.bind(Sample.DEFAULT_ID);
        enumeration = oql.execute(Database.READONLY);
        object = (Sample) enumeration.nextElement();
        LOG.debug("Retrieved object: " + object);
        if (object.getValue1().equals(NEW_VALUE)) {
            LOG.error("Error: modified object was stored");
            fail("Modified object was stored");
        } else {
            LOG.debug("OK: object is read-only");
        }
        _db.commit();
    }
}
