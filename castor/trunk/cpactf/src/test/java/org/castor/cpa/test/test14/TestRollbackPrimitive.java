/*
 * Copyright 2009 Ralf Joachim
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
package org.castor.cpa.test.test14;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.castor.cpa.test.framework.CPATestCase;
import org.castor.cpa.test.framework.xml.types.DatabaseEngineType;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.PersistenceException;

public final class TestRollbackPrimitive extends CPATestCase {
    /** The <a href="http://jakarta.apache.org/commons/logging/">Jakarta
     *  Commons Logging</a> instance used for all logging. */
    private static final Log LOG = LogFactory.getLog(TestRollbackPrimitive.class);
    private static final String DBNAME = "test14";
    private static final String MAPPING = "/org/castor/cpa/test/test14/mapping.xml";
    private Database    _db;

    public TestRollbackPrimitive(final String name) {
        super(name);
    }

    // Test are only included/excluded for engines that have been tested with this test suite.

    public boolean include(final DatabaseEngineType engine) {
        return (engine == DatabaseEngineType.MYSQL)
            || (engine == DatabaseEngineType.DERBY);
    }
    
    /**
     * Get a JDO database
     */
    public void setUp() throws Exception {
        _db = getJDOManager(DBNAME, MAPPING).getDatabase();
    }

    public void tearDown() throws PersistenceException {
        _db.close();
    }

    public void test() throws PersistenceException {
        RollbackObject object = null;

        _db.begin();
        try {
            object = (RollbackObject) _db.load(RollbackObject.class, new Long(3));
            LOG.debug("Loaded: " + object);
        } catch (Exception e) {
            object = new RollbackObject();
            _db.create(object);
            LOG.debug("Created: " + object);
        }
        _db.commit();

        
        _db.begin();
        object = (RollbackObject) _db.load(RollbackObject.class, new Long(3));
        long number = object.getNumber();
        object.setNumber(9);
        LOG.debug("Changed: " + object);
        _db.rollback();
        LOG.debug("Rolled back: " + object);
        
        if (object.getNumber() != number) {
            LOG.error("change has not been rolled back");
            fail("change has not been rolled back");
        }
    }
}
