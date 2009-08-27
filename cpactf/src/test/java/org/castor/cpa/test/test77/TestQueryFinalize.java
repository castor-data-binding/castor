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
package org.castor.cpa.test.test77;

import java.sql.Statement;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.castor.cpa.test.framework.CPATestCase;
import org.castor.cpa.test.framework.xml.types.DatabaseEngineType;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.QueryResults;

public final class TestQueryFinalize extends CPATestCase {
    /**
     * The <a href="http://jakarta.apache.org/commons/logging/">Jakarta Commons
     * Logging</a> instance used for all logging.
     */
    private static final Log LOG = LogFactory.getLog(TestQueryFinalize.class);
    private static final String DBNAME = "test77";
    private static final String MAPPING = "/org/castor/cpa/test/test77/mapping.xml";
    private Database _db;

    public TestQueryFinalize(final String name) {
        super(name);
    }

    // Test are only included/excluded for engines that have been tested with this test suite.

    public boolean include(final DatabaseEngineType engine) {
        return (engine == DatabaseEngineType.DERBY)
            || (engine == DatabaseEngineType.MYSQL)
            || (engine == DatabaseEngineType.ORACLE)
            || (engine == DatabaseEngineType.POSTGRESQL);
    }

    /**
     * Get a JDO database
     */
    public void setUp() throws Exception {
        _db = getJDOManager(DBNAME, MAPPING).getDatabase();
        _db.begin();
        LOG.info("Delete everything");
        Statement stmt = _db.getJdbcConnection().createStatement();
        stmt.executeUpdate("delete from test77_depend2");
        stmt.executeUpdate("delete from test77_master");
        stmt.executeUpdate("delete from test77_depend1");
        _db.commit();
    }

    public void tearDown() throws Exception {
        if (_db.isActive()) {
            _db.rollback();
        }
        _db.close();
    }

    /**
     * Query will be garbage collectable after return
     */
    private QueryResults getResults(final Database db) throws Exception {
        OQLQuery query = db.getOQLQuery("SELECT t FROM "
                + Master.class.getName() + " t");
        return query.execute();
    }

    public void testQueryFinalize() throws Exception {
        LOG.info("Create many master objects");
        _db.begin();
        for (int i = 0; i < 100; i++) {
            Master master = new Master();
            _db.create(master);
        }
        _db.commit();

        LOG.info("query master objects");
        try {
            _db.begin();
            QueryResults results = getResults(_db);
            LOG.info("query can be garbage collected");
            while (results.hasMore()) {
                Master master = (Master) results.next();
                LOG.info(master.getId());
            }
            _db.commit();
        } catch (Exception e) {
            fail("Exception thrown iterating over results : " + e);
        }
    }
}
