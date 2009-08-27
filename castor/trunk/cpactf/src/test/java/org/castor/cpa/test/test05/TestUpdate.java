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
package org.castor.cpa.test.test05;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.castor.cpa.test.framework.CPATestCase;
import org.castor.cpa.test.framework.xml.types.DatabaseEngineType;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.PersistenceException;

public final class TestUpdate extends CPATestCase {
    /** The <a href="http://jakarta.apache.org/commons/logging/">Jakarta
     *  Commons Logging</a> instance used for all logging. */
    private static final Log LOG = LogFactory.getLog(TestUpdate.class);
    private static final String DBNAME = "test05";
    private static final String MAPPING = "/org/castor/cpa/test/test05/mapping.xml";
    
    private Database _db;

    /**
     * Constructor
     *
     * @param category The test suite for these tests
     */
    public TestUpdate(final String name) {
        super(name);
    }

    // Test are only included/excluded for engines that have been tested with this test suite.

    public boolean include(final DatabaseEngineType engine) {
        return (engine == DatabaseEngineType.DERBY)
            || (engine == DatabaseEngineType.MYSQL)
            || (engine == DatabaseEngineType.ORACLE);
    }
    
    /**
     * Get a JDO database
     */
    public void setUp() throws Exception {
        _db = getJDOManager(DBNAME, MAPPING).getDatabase();
    }

    public void testUpdate() {
        Sample    object;

        try {
            _db.begin();
  
            object = new Sample();
            object.setId(50);
            LOG.debug("trying to update non timestampable object");
            _db.update(object);
            _db.commit();
            LOG.error("should fail");
            fail("should fail");
        } catch (Exception ex) {
            LOG.debug("catched exception: ", ex);
            try {
                LOG.debug("trying to rollback");
                _db.rollback();
            } catch (Exception ex2) {
                LOG.error("rollback should work: ", ex2);
                fail("rollback should work: " + ex2);
            }
        }
    }

    public void tearDown() throws PersistenceException {
        if (_db.isActive()) { _db.rollback(); } 
        _db.close();
    }
}
