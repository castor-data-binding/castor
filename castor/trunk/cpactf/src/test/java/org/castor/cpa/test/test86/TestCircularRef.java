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
package org.castor.cpa.test.test86;

import java.sql.Statement;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.castor.cpa.test.framework.CPATestCase;
import org.castor.cpa.test.framework.xml.types.DatabaseEngineType;
import org.exolab.castor.jdo.Database;

public final class TestCircularRef extends CPATestCase {
    private static final Log LOG = LogFactory.getLog(TestCircularRef.class);
    private static final String DBNAME = "test86";
    private static final String MAPPING = "/org/castor/cpa/test/test86/mapping.xml";
    private Database _db;

    public TestCircularRef(final String name) {
        super(name);
    }

    // Test are only included/excluded for engines that have been tested with this test suite.

    public boolean include(final DatabaseEngineType engine) {
        return (engine == DatabaseEngineType.ORACLE)
            || (engine == DatabaseEngineType.POSTGRESQL);
    }
    
    // Sequence key generator is not supported by DERBY and MYSQL
    public boolean exclude(final DatabaseEngineType engine) {
        return (engine == DatabaseEngineType.DERBY)
            || (engine == DatabaseEngineType.MYSQL);
    }
    
    /**
     * Get a JDO database
     */
    public void setUp() throws Exception {
        _db = getJDOManager(DBNAME, MAPPING).getDatabase();
        _db.begin();
        LOG.info("Delete everything");
        Statement stmt = _db.getJdbcConnection().createStatement();
        stmt.executeUpdate("delete from test86_circ_brother");
        stmt.executeUpdate("delete from test86_circ_sister");
        _db.commit();
    }
    
    public void tearDown() throws Exception {
        if (_db.isActive()) { _db.rollback(); }
        _db.close();
    }
    
    public void testCircularRef() throws Exception {
        _db.begin();

        LOG.info("Build brother and sister (circurlarly referring) objects");

        // no ids needed, they come from the key-gen
        CircBrother brother = new CircBrother();
        CircSister sister = new CircSister();
        brother.setSister(sister);
        sister.setBrother(brother);
        LOG.info("Create object tree in db");

        try {
            _db.create(brother);
            _db.create(sister);
            _db.commit();            
        } catch (Exception e) {
            LOG.error(e);
            fail("Unable to create objects with circular reference " + e);
        }
    }
}
