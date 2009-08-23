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
package org.castor.cpa.test.test39;

import java.util.Enumeration;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.castor.cpa.test.framework.CPATestCase;
import org.castor.cpa.test.framework.xml.types.DatabaseEngineType;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.PersistenceException;

/**
 * Test for store procedure support in a OQL query. Castor enables user
 * to invoke store procedures of a DBMS throught a OQL query.
 */
public final class TestStoredProcedure extends CPATestCase {
    private static final Log LOG = LogFactory.getLog(TestStoredProcedure.class);
    private static final String DBNAME = "test39";
    private static final String MAPPING = "/org/castor/cpa/test/test39/mapping.xml";
    private static final String USER1 = "user1"; 
    private static final String USER2 = "user2";
    private static final String GROUP1 = "group1";
    private static final String GROUP2 = "group2";
    private Database _db;

    public TestStoredProcedure(final String name) {
        super(name);
    }

    // Test are only included/excluded for engines that have been tested with this test suite.
    
    // Handling of stored procedures is broken and will be investigated under CASTOR-2481
    // If that's resolved ORACLE engine should work again

    public boolean include(final DatabaseEngineType engine) {
        return false;
//        return (engine == DatabaseEngineType.ORACLE);
    }
    
    public boolean exclude(final DatabaseEngineType engine) {
        return (engine == DatabaseEngineType.DERBY)
            || (engine == DatabaseEngineType.MYSQL);
    }
    
    public void setUp() throws Exception {
        _db = getJDOManager(DBNAME, MAPPING).getDatabase();
    }

    public void tearDown() throws PersistenceException {
        _db.close();
    }

    public void test() throws PersistenceException {
        OQLQuery oql;
        Entity object;
        Enumeration<?> enumeration;
        
        // Open transaction in order to perform JDO operations
        _db.begin();        
        // Remove all objects. Then create three objects with the given field values.
        oql = _db.getOQLQuery(
                "SELECT object FROM " + Entity.class.getName() + " object");
        enumeration = oql.execute();
        while (enumeration.hasMoreElements()) {
            object = (Entity) enumeration.nextElement();
            _db.remove(object);
        }
        _db.commit();

        _db.begin();
        object = new Entity();
        object.setId(1);
        object.setValue1(USER1);
        LOG.debug("Creating the first new object: " + object);
        _db.create(object);

        object = new Entity();
        object.setId(2);
        object.setValue1(USER1);
        object.setValue2(GROUP2);
        LOG.debug("Creating the second new object: " + object);
        _db.create(object);

        object = new Entity();
        object.setId(3);
        object.setValue1(USER2);
        object.setValue2(GROUP1);
        LOG.debug("Creating the third new object: " + object);
        _db.create(object);

        _db.commit();

        // Now get the created objects using the stored procedure
        // The stored procedure sp_check_permission stands for some
        // application-specific algorithm of checking permissions and 
        // returns the set of all objects to which access is granted.
        // In this test we assume that value1 holds the name of the user
        // who created the object, and value2 holds the name of the group
        // who has read access to the object. We pass the user name "user1" 
        // and the name of his group "group1" and fetch sequencially two
        // result sets: one by the rule "creator can access object", second
        // by the rule "read access group".
        // We expect to fetch objects with identities 1,2,3.
        _db.begin();
        oql = _db.getOQLQuery(
                "CALL proc_check_permissions($1, $2) AS " + Entity.class.getName());
        oql.bind(USER1);
        oql.bind(GROUP1);
        enumeration = oql.execute();

        int resCnt = 0;
        for (int i = 1; enumeration.hasMoreElements(); i++) {
            object = (Entity) enumeration.nextElement();
            LOG.debug("Fetched object: " + object);
            resCnt++;
        }
        if (resCnt != 3) {
            LOG.debug("Error: Wrong number of objects in the result");
            fail("Wrong number of objects in the result");
        }
        _db.commit();
    }
}
