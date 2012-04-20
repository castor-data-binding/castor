/*
 * Copyright 2009 Werner Guttmann, Ralf Joachim
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
package org.castor.cpa.test.test1503;

import org.castor.cpa.test.framework.CPATestCase;
import org.castor.cpa.test.framework.xml.types.DatabaseEngineType;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.QueryResults;

public final class TestNamedNativeQueries extends CPATestCase {
    private static final String DBNAME = "test1503";
    private static final String MAPPING = "/org/castor/cpa/test/test1503/mapping.xml";

    private static final int ENTITY_COUNT = 5;
    private static final String SQL_SELECT_ALL_FROM_ENTITY = "SELECT * FROM test1503_entity";
    private static final String NATIVE_SELECT_ALL_ENTITY_ONE = "nativeSelectEntity";
    
    public TestNamedNativeQueries(final String name) {
        super(name);
    }

    // Test are only included/excluded for engines that have been tested with this test suite.

    public boolean include(final DatabaseEngineType engine) {
        return (engine == DatabaseEngineType.DERBY)
            || (engine == DatabaseEngineType.HSQL)
            || (engine == DatabaseEngineType.MYSQL)
            || (engine == DatabaseEngineType.ORACLE)
            || (engine == DatabaseEngineType.POSTGRESQL)
            || (engine == DatabaseEngineType.SAPDB)
            || (engine == DatabaseEngineType.SQL_SERVER);
    }
    
    public void setUp() throws Exception {
        Database db = getJDOManager(DBNAME, MAPPING).getDatabase();
        
        db.begin();
        for (int i = 0; i < ENTITY_COUNT; i++) {
            db.create(new Entity(new Integer(i), String.valueOf(i)));
        }
        db.commit();       

        if (db.isActive()) { db.rollback(); }
        db.close();
    }

    public void runTest() throws Exception {
        testNativeQuery();
        testNamedNativeQuery();
    }

    public void tearDown() throws Exception {
        Database db = getJDOManager(DBNAME, MAPPING).getDatabase();
        
        db.begin();
        for (int i = 0; i < ENTITY_COUNT; i++) {
            Entity entity = db.load(Entity.class, new Integer(i));
            db.remove(entity);
        }
        db.commit();       

        if (db.isActive()) { db.rollback(); }
        db.close();
    }

    public void testNativeQuery() throws Exception {
        Database db = getJDOManager(DBNAME, MAPPING).getDatabase();
        
        db.begin();        
        OQLQuery query = db.getNativeQuery(SQL_SELECT_ALL_FROM_ENTITY, Entity.class);
        assertNotNull(query);
        QueryResults results = query.execute();
        assertNotNull(results);
        while (results.hasMore()) {
            Entity entity = (Entity) results.next();
            assertNotNull(entity);
        }            
        db.commit();
        db.close();        
    }
    
    public void testNamedNativeQuery() throws Exception {
        Database db = getJDOManager(DBNAME, MAPPING).getDatabase();
        
        db.begin();        
        OQLQuery query = db.getNamedQuery(NATIVE_SELECT_ALL_ENTITY_ONE);
        assertNotNull(query);
        QueryResults results = query.execute();
        assertNotNull(results);
        while (results.hasMore()) {
            Entity entity = (Entity) results.next();
            assertNotNull(entity);
        }            
        db.commit();
        db.close();        
    }
}
