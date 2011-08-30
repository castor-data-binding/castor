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
package org.castor.cpa.test.test38;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.castor.cpa.test.framework.CPATestCase;
import org.castor.cpa.test.framework.xml.types.DatabaseEngineType;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.QueryResults;

public final class TestCallSql extends CPATestCase {
    private static final Log LOG = LogFactory.getLog(TestCallSql.class);
    private static final String DBNAME = "test38";
    private static final String MAPPING = "/org/castor/cpa/test/test38/mapping.xml";
    private Database _db;

    public TestCallSql(final String name) {
        super(name);
    }

    // Test are only included/excluded for engines that have been tested with this test suite.

    public boolean include(final DatabaseEngineType engine) {
        return (engine == DatabaseEngineType.DERBY)
            || (engine == DatabaseEngineType.HSQL)
            || (engine == DatabaseEngineType.MYSQL)
            || (engine == DatabaseEngineType.ORACLE)
            || (engine == DatabaseEngineType.POSTGRESQL)
            || (engine == DatabaseEngineType.SQL_SERVER)
            || (engine == DatabaseEngineType.SAPDB);
    }
    
    public void setUp() throws Exception {
        _db = getJDOManager(DBNAME, MAPPING).getDatabase();
    }

    public void tearDown() throws PersistenceException {
        _db.close();
    }

    public void testSimple() throws Exception {
        LOG.debug("CALL SQL query with simple (stand-alone) object");
        _db.begin();
        CallEntity test = new CallEntity();
        test.setId(55);
        test.setValue1("value1");
        test.setValue2("value2");
        _db.create(test);
        _db.commit();
        
        _db.begin();
        OQLQuery oql = _db.getOQLQuery("CALL SQL SELECT id, value1 , value2 "
                + "FROM test38_call WHERE (id = $1) AS " + CallEntity.class.getName());
        oql.bind(55);
        QueryResults enumeration = oql.execute();
        CallEntity objectEx = null;
        if (enumeration.hasMore()) {
            objectEx = (CallEntity) enumeration.next();
            LOG.debug("Retrieved object: " + objectEx);
        } else {
            fail("test object not found");
        }
        oql.close();
        _db.commit();
        
        _db.begin();
        test = _db.load(CallEntity.class, new Integer(55));
        _db.remove(test);
        _db.commit();
    }

    public void testExtended() throws Exception {
        _db.begin();

        // Determine if test object exists, if not create it.
        // If it exists, set the name to some predefined value
        // that this test will later override.
        OQLQuery oql = _db.getOQLQuery(
                "SELECT object FROM " + Entity.class.getName() + " object WHERE id = $1");
        oql.bind(50);
        QueryResults enumeration = oql.execute();
        if (enumeration.hasMore()) {
            Entity object = (Entity) enumeration.next();
            LOG.debug("Retrieved object: " + object);
            object.setValue1(Entity.DEFAULT_VALUE_1);
            object.setValue2(Entity.DEFAULT_VALUE_2);
        } else {
            Entity object = new Entity();
            object.setId(50);
            LOG.debug("Creating new object: " + object);
            _db.create(object);
        }
        oql.close();
        _db.commit();

        try {
            LOG.debug("CALL SQL query with object part of an extend hierarchy");
            _db.begin();
            oql = _db.getOQLQuery("CALL SQL "
                    + "SELECT test38_entity.id,test38_entity.value1,test38_entity.value2,"
                    + "test38_extends.id,test38_extends.value3,test38_extends.value4 "
                    + "FROM test38_entity LEFT OUTER JOIN test38_extends "
                    + "ON test38_entity.id=test38_extends.id "
                    + "WHERE (test38_entity.id = $1) AS " + Entity.class.getName());
            oql.bind(50);
            enumeration = oql.execute();
            if (enumeration.hasMore()) {
                Entity object = (Entity) enumeration.next();
                LOG.debug("Retrieved object: " + object);
            } else {
                fail("test object not found");
            }
            oql.close();
            _db.commit();
        } catch (Exception ex) {
            fail("Exception thrown " + ex);
        }
    }
}
