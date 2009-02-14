/*
 * Copyright 2008 Udai Gupta, Ralf Joachim
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
package org.castor.cpa.test.test30;

import java.sql.Connection;
import java.sql.Statement;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.castor.cpa.test.framework.CPATestCase;
import org.castor.cpa.test.framework.xml.types.DatabaseEngineType;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.QueryResults;

public final class TestOQLCondition extends CPATestCase {
    private static final Log LOG = LogFactory.getLog(TestOQLCondition.class);
    
    private static final String DBNAME = "test30";
    private static final String MAPPING = "/org/castor/cpa/test/test30/mapping.xml";
    
    private static final int MIN_ID = 10;
    private static final int MAX_ID = 29;
    
    private Database _db;
    
    public TestOQLCondition(final String name) {
        super(name);
    }

    // Test are only included/excluded for engines that have been tested with this test suite.
    //
    // Configuration of previous test suite.
    // Inc: db2, postgresql, sapdb, sybase, pointbase, progress
    // Exc: hsql

    public boolean include(final DatabaseEngineType engine) {
        return (engine == DatabaseEngineType.MYSQL) 
            || (engine == DatabaseEngineType.ORACLE)
            || (engine == DatabaseEngineType.DERBY);
    }
    
    // SQL_SERVER is excluded until issue CASTOR-2634 is resolved
    
    public boolean exclude(final DatabaseEngineType engine) {
        return (engine == DatabaseEngineType.SQL_SERVER);
    }

    public void setUp() throws Exception {
        _db = getJDOManager(DBNAME, MAPPING).getDatabase();

        reset();
    }
    
    public void tearDown() throws PersistenceException {
        if (_db.isActive()) { _db.rollback(); }
        _db.close();
    }

    /*********************************************************************
     * This method will truncate everything from the database and then
     * repopulate it. It needs to be generic enough to work across
     * databases so I would prefer to use straight JDBC calls. 
     *********************************************************************/
    public void reset() throws Exception {
        _db.begin();

        Connection connection = _db.getJdbcConnection();
        Statement statement = connection.createStatement();
        statement.execute("DELETE FROM test30_extends");
        statement.execute("DELETE FROM test30_entity");
        
        for (int i = MIN_ID; i <= MAX_ID; ++i) {
            Entity obj = new Entity();
            obj.setId(i);
            obj.setValue1(Entity.DEFAULT_VALUE_1 + " " + Integer.toString(i));
            _db.create(obj);
        }
        _db.commit();
    }
    
    /*
     * fetch all available data
     */
    public void testBasicSelect1() throws Exception {
        LOG.debug("Testing testBasicSelect1");

        _db.begin();
        String oql = "select x from " + Entity.class.getName() + " x";
        tryQuery(_db.getOQLQuery(oql), MAX_ID - MIN_ID + 1);
        _db.commit();
    }
    
    /*
     * query only one object, expecting one
     */
    public void testBasicSelect2() throws Exception {
        LOG.debug("Testing testBasicSelect2");

        assertTrue("internal error: MIN_ID>1", MIN_ID > 1);
        assertTrue("internal error: MIN_ID<=15", MIN_ID <= 15);
        assertTrue("internal error: MAX_ID>=15", MAX_ID >= 15);

        _db.begin();
        String oql = "select x from " + Entity.class.getName() + " x where id=15";
        tryQuery(_db.getOQLQuery(oql), 1);  
        _db.commit();
    }
    
    /*
     * query only one object, expecting none
     */
    public void testBasicSelect3() throws Exception {
        LOG.debug("Testing testBasicSelect3");

        assertTrue("internal error: MIN_ID>1", MIN_ID > 1);

        _db.begin();
        String oql = "select x from " + Entity.class.getName() + " x where id=1";
        tryQuery(_db.getOQLQuery(oql), 0);    
        _db.commit();
    }
    
    /*
     * query using bind variable parameter, find one object
     */
    public void testBasicSelect4() throws Exception {
        LOG.debug("Testing testBasicSelect4");

        _db.begin();
        String oql = "select x from " + Entity.class.getName() + " x where id=$1";
        OQLQuery query = _db.getOQLQuery(oql);
        query.bind(MIN_ID);
        tryQuery(query, 1);    
        _db.commit();
    }
    
    /*
     * query using bind variable parameter, find nothing
     */
    public void testBasicSelect5() throws Exception {
        LOG.debug("Testing testBasicSelect5");
        
        _db.begin();
        String oql = "select x from " + Entity.class.getName() + " x where id=$1";
        OQLQuery query = _db.getOQLQuery(oql);
        query.bind(MIN_ID - 1);
        tryQuery(query, 0);    
        _db.commit();
    }
    
    /*
     * query using comparison between bind variable parameter and constant, find all objects
     */
    public void testBasicSelect6() throws Exception {
        LOG.debug("Testing testBasicSelect6");

        _db.begin();
        String oql = "select x from " + Entity.class.getName() + " x where $(int)1 = 1000";
        OQLQuery query = _db.getOQLQuery(oql);
        query.bind(1000);
        tryQuery(query, MAX_ID + 1 - MIN_ID);    
        _db.commit();
    }
    
    /*
     * query using comparison between bind variable parameter and constant, find no objects
     */
    public void testBasicSelect7() throws Exception {
        LOG.debug("Testing testBasicSelect7");

        _db.begin();
        String oql = "select x from " + Entity.class.getName() + " x where $(int)1 = 1000";
        OQLQuery query = _db.getOQLQuery(oql);
        query.bind(2000);
        tryQuery(query, 0);    
        _db.commit();
    }
    
    /*
     * query using 1 bind variable parameters in two places
     */
    public void testBasicSelect8() throws Exception {
        LOG.debug("Testing testBasicSelect8");

        _db.begin();
        String oql = "select x from " + Entity.class.getName() + " x where id>$1 and id<$2";
        OQLQuery query = _db.getOQLQuery(oql);
        query.bind(MIN_ID);
        query.bind(MAX_ID);
        tryQuery(query, MAX_ID + 1 - MIN_ID - 2);    
        _db.commit();
    }
    
    /*
     * query using 1 bind variable parameter, find all but the first and last object
     */
    public void testBasicSelect9() throws Exception {
        LOG.debug("Testing testBasicSelect9");

        _db.begin();
        String oql = "select x from " + Entity.class.getName() + " x where id>$1 and id<$2";
        OQLQuery query = _db.getOQLQuery(oql);
        query.bind(MIN_ID);
        query.bind(MAX_ID);
        tryQuery(query, MAX_ID + 1 - MIN_ID - 2);    
        _db.commit();
    }
    
    /*
     * query using 2 bind variable parameters, find all but the first and last object
     */
    public void testBasicSelect10() throws Exception {
        LOG.debug("Testing testBasicSelect10");

        _db.begin();
        String oql = "select x from " + Entity.class.getName() + " x where id<$2 and id>$1";
        OQLQuery query = _db.getOQLQuery(oql);
        query.bind(MIN_ID);
        query.bind(MAX_ID);
        tryQuery(query, MAX_ID + 1 - MIN_ID - 2);    
        _db.commit();
    }
    
    /*
     * query using "BETWEEN" operator, finding all records
     */
    public void testBasicSelect11() throws Exception {
        LOG.debug("Testing testBasicSelect11");

        _db.begin();
        String oql = "select x from " + Entity.class.getName() + " x where id between $1 and $2";
        OQLQuery query = _db.getOQLQuery(oql);
        query.bind(MIN_ID);
        query.bind(MAX_ID);
        tryQuery(query, MAX_ID + 1 - MIN_ID);    
        _db.commit();
    }
    
    /*
     * query using "BETWEEN" operator, finding no records
     */
    public void testBasicSelect12() throws Exception {
        LOG.debug("Testing testBasicSelect12");

        _db.begin();
        String oql = "select x from " + Entity.class.getName() + " x where id between $2 and $1";
        OQLQuery query = _db.getOQLQuery(oql);
        query.bind(MIN_ID);
        query.bind(MAX_ID);
        tryQuery(query, 0);    
        _db.commit();
    }
    
    /*
     * query using string constants containing a question mark in the WHERE clause,
     * finding all records
     */
    public void testBasicSelect13() throws Exception {
        LOG.debug("Testing testBasicSelect13");

        _db.begin();
        String oql = "select x from " + Entity.class.getName()
                + " x where \"abc123????\" = \"abc123????\"";
        OQLQuery query = _db.getOQLQuery(oql);
        tryQuery(query, MAX_ID + 1 - MIN_ID);    
        _db.commit();
    }
    
    /*
     * query using string constants containing a question mark in the WHERE clause,
     * finding no records
     */
    public void testBasicSelect14() throws Exception {
        LOG.debug("Testing testBasicSelect14");

        _db.begin();
        String oql = "select x from " + Entity.class.getName()
                + " x where \"abc\" = \"?123\"";
        OQLQuery query = _db.getOQLQuery(oql);
        tryQuery(query, 0);    
        _db.commit();
    }
    
    /*
     * query using "IN" operator
     */
    public void testBasicSelect15() throws Exception {
        LOG.debug("Testing testBasicSelect15");

        assertTrue("internal error: MIN_ID>5", MIN_ID > 5);
        assertTrue("internal error: MIN_ID<=15", MIN_ID <= 15);
        assertTrue("internal error: MAX_ID>=18", MAX_ID >= 15);

        _db.begin();
        String oql = "select x from " + Entity.class.getName()
                + " x where id in list(5, 15, 18)";
        OQLQuery query = _db.getOQLQuery(oql);
        tryQuery(query, 2);    
        _db.commit();
    }
    
    /*
     * query using "IN" operator and bind variables, find all objects
     */
    public void testBasicSelect16() throws Exception {
        LOG.debug("Testing testBasicSelect16");

        _db.begin();
        String oql = "select x from " + Entity.class.getName()
                + " x where id in list($1, $2, $3)";
        OQLQuery query = _db.getOQLQuery(oql);
        query.bind(MIN_ID);
        query.bind((MIN_ID + MAX_ID) / 2);
        query.bind(MAX_ID);
        tryQuery(query, 3);    
        _db.commit();
    }
    
    /*
     * query using "IN" operator and bind variables, find some objects
     */
    public void testBasicSelect17() throws Exception {
        LOG.debug("Testing testBasicSelect17");

        _db.begin();
        String oql = "select x from " + Entity.class.getName()
                + " x where id in list($1, $2)";
        OQLQuery query = _db.getOQLQuery(oql);
        query.bind(MIN_ID);
        query.bind(MAX_ID + 5);
        tryQuery(query, 1);    
        _db.commit();
    }
    
    /*
    * query using "IN" operator and string values, find one object
     */
    public void testBasicSelect18() throws Exception {
        LOG.debug("Testing testBasicSelect18");

        _db.begin();
        String oql = "select x from " + Entity.class.getName()
                + " x where value1 in list(\"XXX\", \"one 21\", 'A')";
        OQLQuery query = _db.getOQLQuery(oql);
        tryQuery(query, 1);   
        _db.commit();
    }
    
    /*
     * test received result set
     */
    private void tryQuery(final OQLQuery query, final int countExpected)
    throws PersistenceException {
        QueryResults res = query.execute();
        int count = 0;
        try {
            while (res.hasMore()) {
               Entity obj = (Entity) res.next();
               String val = Entity.DEFAULT_VALUE_1 + " " + Integer.toString(obj.getId());
               assertEquals("value1", val, obj.getValue1());
               assertEquals("value2", Entity.DEFAULT_VALUE_2, obj.getValue2());
               ++count;
           }
       } finally {
           res.close();
       }

       assertEquals("number of objects found", countExpected, count);
   }
}
