/*
 * Copyright 2009 Udai Gupta, Ralf Joachim
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
package org.castor.cpa.test.test200;

import java.sql.Connection;
import java.sql.Statement;
import java.util.Iterator;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.castor.cpa.test.framework.CPATestCase;
import org.castor.cpa.test.framework.xml.types.DatabaseEngineType;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.QueryResults;

/**
 * Tests that modification to read only objects are not persist in the database.
 */
public final class TestSelfRelation extends CPATestCase {
    private static final String DBNAME = "test200";
    private static final String MAPPING = "/org/castor/cpa/test/test200/mapping.xml";
    
    public static Test suite() throws Exception {
        TestSuite suite = new TestSuite(TestSelfRelation.class.getName());

        suite.addTest(new TestSelfRelation("testInitialize"));
        suite.addTest(new TestSelfRelation("testCreate"));
        suite.addTest(new TestSelfRelation("testLoad"));
        suite.addTest(new TestSelfRelation("testOQL"));
        suite.addTest(new TestSelfRelation("testUpdate"));

        return suite;
    }

    public TestSelfRelation(final String name) {
        super(name);
    }

    // Test are only included/excluded for engines that have been tested with this test suite.

    public boolean include(final DatabaseEngineType engine) {
        return (engine == DatabaseEngineType.DERBY)
            || (engine == DatabaseEngineType.HSQL)
            || (engine == DatabaseEngineType.MYSQL)
            || (engine == DatabaseEngineType.POSTGRESQL);
    }

    public boolean exclude(final DatabaseEngineType engine) {
        return (engine == DatabaseEngineType.ORACLE);
    }

    public void testInitialize() throws Exception {
        Database db = getJDOManager(DBNAME, MAPPING).getDatabase();
        db.begin();
        Connection connection = db.getJdbcConnection();
        connection.setAutoCommit(false);

        // delete everything directly
        Statement stmt = connection.createStatement();
        stmt.executeUpdate("DELETE FROM test200_self_rel_folder");
        //insert data
        stmt.execute("INSERT INTO test200_self_rel_folder "
                + "( id , name ) VALUES ( 1 , 'parent' ) ");
        stmt.execute("INSERT INTO test200_self_rel_folder "
                + "( id , name , parent_id ) VALUES ( 2 , 'first child' , 1 ) ");
        stmt.execute("INSERT INTO test200_self_rel_folder "
                + "( id , name , parent_id ) VALUES ( 3 , 'second child' , 1 ) ");
        db.commit();
        db.close();
    }
    
    /**
     * this tests creating a folder
     */
    public void testCreate() throws Exception {
        SelfRelationFolder folder = new SelfRelationFolder();
        SelfRelationFolder child = new SelfRelationFolder();
        SelfRelationFolder grandChild = new SelfRelationFolder();
        SelfRelationFolder greatGrandChild = new SelfRelationFolder();
        folder.setId(new Integer(5));
        folder.setName("Test Folder");
        child.setId(new Integer(6));
        child.setName("Test Child");
        folder.addChild(child);
        grandChild.setId(new Integer(7));
        grandChild.setName("Test Grandchild");
        child.addChild(grandChild);
        greatGrandChild.setId(new Integer(8));
        greatGrandChild.setName("Test Greatgrandchild");
        grandChild.addChild(greatGrandChild);
        
        Database db = getJDOManager(DBNAME, MAPPING).getDatabase();
        db.begin();
        db.create(folder);
        db.create(child);
        db.create(grandChild);
        db.create(greatGrandChild);
        db.commit();
        db.close();
    }
    
    /**
     * This loads a folder
     */
    public void testLoad() throws Exception {
        Database db = getJDOManager(DBNAME, MAPPING).getDatabase();
        db.begin();
        SelfRelationFolder folder = db.load(
                SelfRelationFolder.class, new Integer(1));
        assertEquals("parent", folder.getName());
        assertEquals(1, folder.getId().intValue());
        
        // assertNull(folder.getParent());
        assertNotNull(folder.getChildren());
        
        Iterator<SelfRelationFolder> i = folder.getChildren().iterator();
        assertNotNull(i);
        assertTrue("No child loaded", i.hasNext());
        
        int counter = 0;
        
        // load first expected child, and assert its properties
        assertTrue(i.hasNext());
        SelfRelationFolder child = i.next();
        assertNotNull(child);
        assertEquals(2, child.getId().intValue());
        assertNotNull(child.getParent());
        // assertEquals(1, child.getParent().getId().intValue());
        counter++;
        
        // load second expected child, and assert its properties
        assertTrue(i.hasNext());
        child = i.next();
        assertNotNull(child);
        assertEquals(3, child.getId().intValue());
        assertNotNull(child.getParent());
        // assertEquals(1, child.getParent().getId().intValue());
        counter++;
        
        assertTrue("At least two children should have been loaded.", counter >= 2);
        
        db.commit();
        db.close();
    }

    public void testOQL() throws Exception {
        Database db = getJDOManager(DBNAME, MAPPING).getDatabase();
        db.begin();
        OQLQuery oql = db.getOQLQuery("SELECT a FROM "
                + SelfRelationFolder.class.getName() + " a");
        QueryResults results = oql.execute();
        assertTrue(results.hasMore());
        
        int counter = 0;
        SelfRelationFolder f;

        f = (SelfRelationFolder) results.next();
        assertNotNull(f);
        assertEquals(1, f.getId().intValue());
        assertEquals("parent", f.getName());
        counter++;

        f = (SelfRelationFolder) results.next();
        assertNotNull(f);
        assertEquals(2, f.getId().intValue());
        assertEquals("first child", f.getName());
        counter++;

        f = (SelfRelationFolder) results.next();
        assertNotNull(f);
        assertEquals(3, f.getId().intValue());
        assertEquals("second child", f.getName());
        counter++;

        f = (SelfRelationFolder) results.next();
        assertNotNull(f);
        assertEquals(5, f.getId().intValue());
        assertEquals("Test Folder", f.getName());
        counter++;

        f = (SelfRelationFolder) results.next();
        assertNotNull(f);
        assertEquals(6, f.getId().intValue());
        assertEquals("Test Child", f.getName());
        counter++;

        f = (SelfRelationFolder) results.next();
        assertNotNull(f);
        assertEquals(7, f.getId().intValue());
        assertEquals("Test Grandchild", f.getName());
        counter++;

        f = (SelfRelationFolder) results.next();
        assertNotNull(f);
        assertEquals(8, f.getId().intValue());
        assertEquals("Test Greatgrandchild", f.getName());
        counter++;
        
        assertEquals("At least 7 folders should have been returned", 7, counter);

        oql.close();
        db.commit();
        db.close();
    }

    /*
     * This tests updating a folder
     */
    public void testUpdate() throws Exception {
        Database db = getJDOManager(DBNAME, MAPPING).getDatabase();
        db.begin();
        // load a folder, and assert its properties
        SelfRelationFolder folder = db.load(SelfRelationFolder.class, new Integer(6));
        assertNotNull(folder);
        assertEquals(6, folder.getId().intValue());
        assertEquals("Test Child", folder.getName());
        assertNotNull(folder.getParent());
        assertNotNull(folder.getChildren());
        assertEquals(1, folder.getChildren().size());
        db.commit();
        
        folder.setName("Test Update");
        
        db.begin();
        db.update(folder);
        db.commit();
        
        db.begin();
        // load a folder, and assert its properties
        folder = db.load(SelfRelationFolder.class, new Integer(6));
        assertNotNull(folder);
        assertEquals(6, folder.getId().intValue());
        assertEquals("Test Update", folder.getName());
        assertNotNull(folder.getParent());
        assertNotNull(folder.getChildren());
        assertEquals(1, folder.getChildren().size());
        db.rollback();
        db.close();
    }
}
