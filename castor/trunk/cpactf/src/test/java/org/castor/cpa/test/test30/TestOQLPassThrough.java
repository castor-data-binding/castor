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

public final class TestOQLPassThrough extends CPATestCase {
    private static final Log LOG = LogFactory.getLog(TestOQLPassThrough.class);
    
    private static final String DBNAME = "test30";
    
    private Database _db;
    
    public TestOQLPassThrough(final String name) {
        super(name);
    }

    public boolean include(final DatabaseEngineType engine) {
        return (engine == DatabaseEngineType.MYSQL 
                || engine == DatabaseEngineType.DERBY);
    }
    
    public void setUp() throws Exception {
       _db = getJDOManager(DBNAME).getDatabase();

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
        statement.execute("DELETE FROM test30_group");
        
        GroupEntity group = new GroupEntity();
        _db.create(group);
       _db.commit();
    }
    
    public void testBasicSelectGroupEntity1() throws Exception {
        LOG.debug("Testing testBasicSelectGroupEntity1");

        _db.begin();
        OQLQuery query = _db.getOQLQuery(
                "CALL SQL " 
                + "select id, value1 " 
                + "from test30_group entity " 
                + "where entity.id >= $1 and entity.id <= $1 " 
                + " AS " + GroupEntity.class.getName());
        query.bind(GroupEntity.DEFAULT_ID);
        QueryResults res = query.execute();

        assertTrue(res.hasMore());
        GroupEntity obj = (GroupEntity) res.next();
        assertEquals(GroupEntity.DEFAULT_ID, obj.getId());
        assertEquals(GroupEntity.DEFAULT_VALUE, obj.getValue1());
        
        assertFalse(res.hasMore());
        
        _db.commit();
    }
    
    public void testBasicSelectGroupEntity2() throws Exception {
        LOG.debug("Testing testBasicSelectGroupEntity2");

        _db.begin();
        OQLQuery query = _db.getOQLQuery(
                "CALL SQL " 
                + "select id, value1 " 
                + "from test30_group entity " 
                + "where entity.id >= $1 and entity.id <= $1 and "
                + "entity.value1 = $2 "
                + " AS " + GroupEntity.class.getName());
        query.bind(GroupEntity.DEFAULT_ID);
        query.bind(GroupEntity.DEFAULT_VALUE);
        QueryResults res = query.execute();

        assertTrue(res.hasMore());
        GroupEntity obj = (GroupEntity) res.next();
        assertEquals(GroupEntity.DEFAULT_ID, obj.getId());
        assertEquals(GroupEntity.DEFAULT_VALUE, obj.getValue1());
        
        assertFalse(res.hasMore());
        
        _db.commit();
    }
}
