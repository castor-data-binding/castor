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

import java.math.BigDecimal;
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

public final class TestOQLCountFunction extends CPATestCase {
    private static final Log LOG = LogFactory.getLog(TestOQLCountFunction.class);
    
    private static final String DBNAME = "test30";
    private static final String MAPPING = "/org/castor/cpa/test/test30/mapping.xml";
    
    private static final int MIN_ID = 10;
    private static final int MAX_ID = 29;
    private static final int MIN_EXTENDS_ID = 30;
    private static final int MAX_EXTENDS_ID = 49;
    
    private Database _db;
    
    public TestOQLCountFunction(final String name) {
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
            || (engine == DatabaseEngineType.SQL_SERVER)
            || (engine == DatabaseEngineType.DERBY);
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
    private void reset() throws Exception {
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
       
        for (int i = MIN_EXTENDS_ID; i <= MAX_EXTENDS_ID; ++i) {
            ExtendsEntity ext = new ExtendsEntity();
            ext.setId(i);
            ext.setValue1(Entity.DEFAULT_VALUE_1 + " " + Integer.toString(i));
            _db.create(ext);
        }
        
        _db.commit();
    }
    
    /*
     * obtain number of ExtendsEntity instances
    */
    public void testSelectCountExtends1() throws Exception {    
        LOG.debug("Testing testSelectCountExtends1");
        
        _db.begin();
        String oql = "SELECT count(x.id) FROM " + ExtendsEntity.class.getName() + " x";
        tryFunctionQuery(_db.getOQLQuery(oql), 20); 
        _db.commit();
    }
    
   /*
    * obtain number of Entity instances
     */
    public void testSelectCountExtends2() throws Exception {
        LOG.debug("Testing testSelectCountExtends2");
        
        _db.begin();
        String oql = "SELECT count(x.id) FROM " + Entity.class.getName() + " x";
        tryFunctionQuery(_db.getOQLQuery(oql), 40);
        _db.commit();
    }
   
   /*
     * obtain distinct number of Entity instances
     */
   public void testSelectCountExtends3() throws Exception {       
        LOG.debug("Testing testSelectCountExtends3");

        _db.begin();
        String oql = "SELECT count(distinct x.id) FROM " + Entity.class.getName() + " x";
        tryFunctionQuery(_db.getOQLQuery(oql), 40);
        _db.commit();
    }
 
    /*
     * test received result set
     */
    private void tryFunctionQuery(final OQLQuery query, final int countExpected)
    throws PersistenceException {
       QueryResults res = query.execute();
        long functionValue = 0;

        try {
            if (res.hasMore()) {
                Object obj = res.next();
               if (obj instanceof Long) {
                   functionValue = ((Long) obj).longValue();
                } else if (obj instanceof BigDecimal) {
                    functionValue = ((BigDecimal) obj).longValue();
               } else if (obj instanceof Integer) {
                    functionValue = ((Integer) obj).longValue();
               }
            }
        } finally {
            res.close();
        }
        assertEquals("number of objects found", countExpected, functionValue);
    }
}
