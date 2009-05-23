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
package org.castor.cpa.test.test1499;

import org.castor.cpa.test.framework.CPATestCase;
import org.castor.cpa.test.framework.xml.types.DatabaseEngineType;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.QueryException;
import org.exolab.castor.jdo.QueryResults;

public final class TestNamedQueries extends CPATestCase {
    private static final String DBNAME = "test1499";
    private static final String MAPPING = "/org/castor/cpa/test/test1499/mapping.xml";

    private static final int ENTITY_COUNT = 5;
    private static final String SELECT_ALL_ENTITY_ONE = "selectAllEntity";
    private static final String SELECT_ALL_ENTITY_HINT = "selectEntitiesWithHint";
    private static final String SELECT_ENTITY_ONE_BY_ID = "selectEntityById";
    private static final String SELECT_KNIGHTS_WHO_SAY_NI = "selectAllNightsWhoSayNi";
    private static final String QUERY_WITH_BAD_SYNTAX = "queryBadSyntax";
    
    public TestNamedQueries(final String name) {
        super(name);
    }

    // Test are only included/excluded for engines that have been tested with this test suite.

    public boolean include(final DatabaseEngineType engine) {
        return (engine == DatabaseEngineType.MYSQL)
            || (engine == DatabaseEngineType.SAPDB)
            || (engine == DatabaseEngineType.DERBY)
            || (engine == DatabaseEngineType.ORACLE);
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
        testNamedQuery();
        testNamedQueryIgnoreHint();
        testBadSyntaxNamedQuery();
        testNonExistentNamedQuery();
    }

    public void tearDown() throws Exception {
        Database db = getJDOManager(DBNAME, MAPPING).getDatabase();
        
        db.begin();
        for (int i = 0; i < ENTITY_COUNT; i++) {
            Entity entity = (Entity) db.load(Entity.class, new Integer(i));
            db.remove(entity);
        }
        db.commit();       

        if (db.isActive()) { db.rollback(); }
        db.close();
    }

    public void testNamedQuery() throws Exception {
        Database db = getJDOManager(DBNAME, MAPPING).getDatabase();
        
        // load all EntityOne instances using a named query
        db.begin();
        
        OQLQuery query = db.getNamedQuery(SELECT_ALL_ENTITY_ONE);        
        QueryResults results = query.execute();
        int i = 0;
        while (results.hasMore()) {
            Entity entity = (Entity) results.next();
            assertNotNull(entity);
            i++;
        }
        assertTrue(i >= ENTITY_COUNT);
        db.commit();
        
        // load EntityOne with Id=1 from persistent store
        db.begin();
        
        query = db.getNamedQuery(SELECT_ENTITY_ONE_BY_ID);
        query.bind(new Integer(0));
        results = query.execute();
        
        Entity entity = (Entity) results.next();

        assertNotNull(entity);
        assertEquals(new Integer(0), entity.getId());
        
        db.commit();
        db.close();
    }
    
    public void testNonExistentNamedQuery() throws Exception {
        Database db = getJDOManager(DBNAME, MAPPING).getDatabase();
        
        // try to get non-existent named query
        db.begin();
        OQLQuery query = null;            
        try {
            query = db.getNamedQuery(SELECT_KNIGHTS_WHO_SAY_NI);
            fail("Database.getNamedQuery() should have thrown a QueryException");
        } catch (QueryException e) {
            //  great, this is what we expect
            //  check if it's the correct one
            if (!e.getMessage().startsWith("Cannot find a named query")) {
                throw e;
            }
        } finally {
            if (query != null) {
                query.close();
            }
        }
        
        db.commit();
        db.close();
    }
    
    public void testBadSyntaxNamedQuery() throws Exception {
        Database db = getJDOManager(DBNAME, MAPPING).getDatabase();
        
        // try to load non-existent named query
        db.begin();
        try {
            OQLQuery query = db.getNamedQuery(QUERY_WITH_BAD_SYNTAX);            
            query.close(); //this shouldn't happen
        } catch (QueryException e) {
            // great, this is what we expect
            // check if it's the correct one
            if (!e.getMessage().startsWith("Could not find class")) {
                throw e;
            }
        }
        db.commit();
        db.close();
    }
    
    public void testNamedQueryIgnoreHint() throws Exception {
        Database db = getJDOManager(DBNAME, MAPPING).getDatabase();

        // load all EntityOne instances using a named query
        // and then use same query but with hints
        db.begin();
        
        OQLQuery query = db.getNamedQuery(SELECT_ALL_ENTITY_ONE);        
        QueryResults results = query.execute();
        int countFirst = 0;
        while (results.hasMore()) {
            Entity entity = (Entity) results.next();
            assertNotNull(entity);
            countFirst++;
        }
        
        query = db.getNamedQuery(SELECT_ALL_ENTITY_HINT);        
        results = query.execute();
        int countSecond = 0;
        while (results.hasMore()) {
            Entity entity = (Entity) results.next();
            assertNotNull(entity);
            countSecond++;
        }
        assertEquals(countFirst, countSecond);
        
        db.commit();     
        db.close();
    }
}
