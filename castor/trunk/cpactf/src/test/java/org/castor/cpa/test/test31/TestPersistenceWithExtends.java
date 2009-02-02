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
package org.castor.cpa.test.test31;

import java.sql.Connection;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.castor.cpa.test.framework.CPATestCase;
import org.castor.cpa.test.framework.xml.types.DatabaseEngineType;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.QueryResults;

public final class TestPersistenceWithExtends extends CPATestCase {
    private static final Log LOG = LogFactory.getLog(TestPersistenceWithExtends.class);

    private static final String DBNAME = "test31";
    private static final String MAPPING = "/org/castor/cpa/test/test31/mapping.xml";
    
    private Database _db;
    
    public TestPersistenceWithExtends(final String name) {
        super(name);
    }    
    
    // Test are only included/excluded for engines that have been tested with this test suite.
    //
    // Configuration of previous test suite.
    // Inc: db2, postgresql, sybase, pointbase, progress
    // Exc: hsql, sapdb, derby

    public boolean include(final DatabaseEngineType engine) {
        return (engine == DatabaseEngineType.MYSQL);
    }

    // SQL_SERVER is excluded until issue CASTOR-2634 is resolved

    public boolean exclude(final DatabaseEngineType engine) {
        return (engine == DatabaseEngineType.ORACLE)
            || (engine == DatabaseEngineType.SQL_SERVER);
    }

    public void setUp() throws Exception {
        _db = getJDOManager(DBNAME, MAPPING).getDatabase();
        
        reset();
    }

    public void tearDown() throws PersistenceException {
        if (_db.isActive()) { _db.rollback(); }
        _db.close();
    }

    private void reset() throws Exception {
       _db.begin();
       Connection conn = _db.getJdbcConnection();
       conn.createStatement().executeUpdate("DELETE FROM test31_relation");
       conn.createStatement().executeUpdate("DELETE FROM test31_extends2");
       conn.createStatement().executeUpdate("DELETE FROM test31_extends1");
       conn.createStatement().executeUpdate("DELETE FROM test31_related");
       conn.createStatement().executeUpdate("DELETE FROM test31_persistent");
       conn.createStatement().executeUpdate("DELETE FROM test31_group");
       _db.commit();
    }
    
    public void test() throws Exception {
        // create data objects for test
        Date date = createTestObjects();
            
        // query on extends object
        queryOnExtends(date);
    
        // query on many-to-many relation
        queryManyToMany();
        
        // remove old test objects
        removeTestObjects();
    }
    
    private Date createTestObjects() throws Exception {
        _db.begin();
        
        GroupEntity group1 = new GroupEntity();
        LOG.debug("Creating group: " + group1);
        _db.create(group1);

        GroupEntity group2 = new GroupEntity();
        group2.setId(GroupEntity.DEFAULT_ID + 1);
        LOG.debug("Creating group: " + group2);
        _db.create(group2);
        
        ExtendsEntity2 entity20 = new ExtendsEntity2();
        entity20.setId(PersistentEntity.DEFAULT_ID + 20);
        entity20.setGroup(group2);
        LOG.debug("Creating new object: " + entity20);
        _db.create(entity20);
        
        Date date = new Date();
        Thread.sleep(2000);
        
        ExtendsEntity1 entity10 = new ExtendsEntity1();
        entity10.setId(PersistentEntity.DEFAULT_ID + 10);
        entity10.setGroup(group1);
        entity10.getList().add(entity20);
        LOG.debug("Creating new object: " + entity10);
        _db.create(entity10);
        
        ExtendsEntity2 entity21 = new ExtendsEntity2();
        entity21.setId(PersistentEntity.DEFAULT_ID + 21);
        entity21.setGroup(group2);
        LOG.debug("Creating new object: " + entity21);
        _db.create(entity21);
        
        ExtendsEntity1 entity11 = new ExtendsEntity1();
        entity11.setId(PersistentEntity.DEFAULT_ID + 11);
        entity11.setGroup(group2);
        entity11.getList().add(entity21);
        LOG.debug("Creating new object: " + entity11);
        _db.create(entity11);

        _db.commit();
        
        return date;
    }
    
    private void queryOnExtends(final Date date) throws Exception {
        int cnt;

        _db.begin();
        
        String oql = "SELECT t FROM " + ExtendsEntity1.class.getName() + " t "
                   + "WHERE t.group.id=$1";
        OQLQuery qry = _db.getOQLQuery(oql);
        qry.bind(GroupEntity.DEFAULT_ID);
        QueryResults qrs = qry.execute();
        for (cnt = 0; qrs.hasMore(); cnt++) {
            ExtendsEntity1 entity1 = (ExtendsEntity1) qrs.next();
            LOG.debug("Retrieved object: " + entity1);
            if (entity1.getExt() != 0) {
                LOG.debug("Error: ext field = " + entity1.getExt());
                fail("ext field retrieval failed");
            }
        }
        qry.close();
        if (cnt == 1) {
            LOG.info("OK");
        } else {
            LOG.info("Error: retrieved " + cnt + " objects");
            fail("result size mismatch in the query on extends object");
        }
        
        oql = "SELECT t FROM " + PersistentEntity.class.getName() + " t "
            + "WHERE t.creationTime<=$1";
        qry = _db.getOQLQuery(oql);
        qry.bind(date);
        qrs = qry.execute();
        for (cnt = 0; qrs.hasMore(); cnt++) {
            PersistentEntity p = (PersistentEntity) qrs.next();
            LOG.debug("Retrieved object: " + p);
        }
        qry.close();
        if (cnt == 1) {
            LOG.info("OK");
        } else {
            LOG.info("Error: retrieved " + cnt + " objects");
            fail("result size mismatch in the query on base object");
        }

        _db.commit();
    }
    
    public void queryManyToMany() throws Exception {
        int cnt;

        _db.begin();
        
        String oql = "SELECT t FROM " + ExtendsEntity1.class.getName() + " t "
                   + "WHERE t.list.id=$1";
        OQLQuery qry = _db.getOQLQuery(oql);
        qry.bind(PersistentEntity.DEFAULT_ID + 21);
        QueryResults qrs = qry.execute();
        for (cnt = 0; qrs.hasMore(); cnt++) {
            ExtendsEntity1 entity1 = (ExtendsEntity1) qrs.next();
            LOG.debug("Retrieved object: " + entity1);
        }
        qry.close();
        if (cnt == 1) {
            LOG.info("OK");
        } else {
            LOG.info("Error: retrieved " + cnt + " objects");
            fail("result size mismatch in the query on many-to-many");
        }
        _db.commit();
    }
    
    private void removeTestObjects() throws Exception {
        _db.begin();
        
        String oql = "SELECT t FROM " + ExtendsEntity1.class.getName() + " t";
        OQLQuery qry = _db.getOQLQuery(oql);
        QueryResults qrs = qry.execute();
        while (qrs.hasMore()) { _db.remove(qrs.next()); }
        qry.close();
        
        oql = "SELECT t FROM " + ExtendsEntity2.class.getName() + " t";
        qry = _db.getOQLQuery(oql);
        qrs = qry.execute();
        while (qrs.hasMore()) { _db.remove(qrs.next()); }
        qry.close();

        oql = "SELECT t FROM " + GroupEntity.class.getName() + " t";
        qry = _db.getOQLQuery(oql);
        qrs = qry.execute();
        while (qrs.hasMore()) { _db.remove(qrs.next()); }
        qry.close();
        
        _db.commit();
    }
}
