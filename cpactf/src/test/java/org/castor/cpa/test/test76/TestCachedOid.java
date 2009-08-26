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
package org.castor.cpa.test.test76;

import java.sql.Statement;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.castor.cpa.test.framework.CPATestCase;
import org.castor.cpa.test.framework.xml.types.DatabaseEngineType;
import org.exolab.castor.jdo.Database;

public final class TestCachedOid extends CPATestCase {
    /** The <a href="http://jakarta.apache.org/commons/logging/">Jakarta
     *  Commons Logging</a> instance used for all logging. */
    private static final Log LOG = LogFactory.getLog(TestCachedOid.class);
    
    private static final String DBNAME = "test76";
    private static final String MAPPING = "/org/castor/cpa/test/test76/mapping.xml";
    private Database       _db;

    /**
     * Constructor
     *
     * @param category The test suite for these tests
     */
    public TestCachedOid(final String name) {
        super(name);
    }

    // Test are only included/excluded for engines that have been tested with this test suite.

    public boolean include(final DatabaseEngineType engine) {
        return (engine == DatabaseEngineType.DERBY)
            || (engine == DatabaseEngineType.MYSQL);
    }

    public boolean exclude(final DatabaseEngineType engine) {
        return (engine == DatabaseEngineType.POSTGRESQL);
    }

    public void setUp() throws Exception {
        _db = getJDOManager(DBNAME, MAPPING).getDatabase();
        _db.begin();
        LOG.info("Delete everything");
        Statement stmt = _db.getJdbcConnection().createStatement();
        stmt.executeUpdate("delete from test76_depend2");
        stmt.executeUpdate("delete from test76_master");
        stmt.executeUpdate("delete from test76_depend1");
        _db.commit();
    }

    public void tearDown() throws Exception  {
        if (_db.isActive()) { _db.rollback(); }
        _db.close();
    }
    
    public void testCachedOid() throws Exception {
        _db.begin();

        LOG.info("Build master object and its dependent objects");

        // no ids needed, they come from the key-gen
        Master master = new Master();
        Depend2 depend2 = new Depend2();
        master.addDepend2(depend2);

        LOG.info("Create object tree in db");
        _db.create(master);
        _db.commit();

        // test for bug 1163 : Lock conflict when loading an object present in the cache
        LOG.info("Loading master object in db-locked mode");
        _db.begin();
        _db.load(Master.class, new Integer(master.getId()), Database.DBLOCKED);
        _db.commit();
    }

}
