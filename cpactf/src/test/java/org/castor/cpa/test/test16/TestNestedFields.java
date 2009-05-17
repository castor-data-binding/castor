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
package org.castor.cpa.test.test16;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.castor.cpa.test.framework.CPATestCase;
import org.castor.cpa.test.framework.xml.types.DatabaseEngineType;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.QueryResults;

/**
 * Test for nested-field support in Castor JDO. A nested-field
 * is java field that maps to multiple database fields.
 */
public final class TestNestedFields extends CPATestCase {
    /** The <a href="http://jakarta.apache.org/commons/logging/">Jakarta
     *  Commons Logging</a> instance used for all logging. */
    private static final Log LOG = LogFactory.getLog(TestNestedFields.class);
    
    private static final String DBNAME = "test16";
    private static final String MAPPING = "/org/castor/cpa/test/test16/mapping.xml";
    private Database _db;

    public TestNestedFields(final String name) {
        super(name);
    }

    // Test are only included/excluded for engines that have been tested with this test suite.

    public boolean include(final DatabaseEngineType engine) {
        return (engine == DatabaseEngineType.MYSQL)
            || (engine == DatabaseEngineType.DERBY);
    }
    
    public void setUp() throws Exception {
        _db = getJDOManager(DBNAME, MAPPING).getDatabase();
    }

    public void tearDown() throws PersistenceException {
        if (_db.isActive()) { _db.rollback(); }
        _db.close();
    }
    
    public void testRun() throws PersistenceException {
        OQLQuery         oql;
        QueryResults     results;
        NestedObject     t;

        // Open transaction in order to perform JDO operations
        _db.begin();
        oql = _db.getOQLQuery("SELECT t FROM "
                + NestedObject.class.getName() + " t WHERE id = $1");
        oql.bind(NestedObject.DEFAULT_ID);
        results = oql.execute();
        if (results.hasMore()) {
            t = (NestedObject) results.next();
            _db.remove(t);
            LOG.debug("Deleting object: " + t);
        }
        _db.commit();
        
        _db.begin();
        t = new NestedObject();
        LOG.debug("Creating new object: " + t);
        _db.create(t);
        _db.commit();
        
        _db.begin();
        oql.bind(NestedObject.DEFAULT_ID);
        results = oql.execute();
        if (results.hasMore()) {
            t = (NestedObject) results.next();
            String nv1 = t.getNested1().getValue1();
            String nv2 = t.getNested2().getNested3().getValue2();
            if (NestedObject.DEFAULT_VALUE1.equals(nv1)
                    && NestedObject.DEFAULT_VALUE2.equals(nv2)) {
                
                LOG.debug("OK: Created object: " + t);
            } else {
                LOG.error("Creating object: " + t);
                fail("Creating object failed");
            }
        }
        _db.commit();
        oql.close();

        LOG.info("Testing nested fields in OQLQuery...");
        _db.begin();
        oql = _db.getOQLQuery("SELECT t FROM "
                + NestedObject.class.getName()
                + " t WHERE nested2.nested3.value2 = $1");
        oql.bind(NestedObject.DEFAULT_VALUE2);
        results = oql.execute();
        if (results.hasMore()) {
            LOG.info("OK: Nested fields persisted");
        } else {
            LOG.error("Failed to persist nested fields");
            fail("Failed to persist nested fields");
        }
        oql.close();
        _db.commit();
    }
}
