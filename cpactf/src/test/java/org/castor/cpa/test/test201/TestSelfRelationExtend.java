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
package org.castor.cpa.test.test201;

import java.sql.Connection;
import java.sql.Statement;
import java.util.Collection;
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
public final class TestSelfRelationExtend extends CPATestCase {
    private static final String PARENT_TABLE_NAME = "test201_self_rel_parent";
    private static final String EXTEND_TABLE_NAME = "test201_self_rel_extend";

    private static final String DBNAME = "test201";
    private static final String MAPPING = "/org/castor/cpa/test/test201/mapping.xml";

    public static Test suite() throws Exception {
        TestSuite suite = new TestSuite(TestSelfRelationExtend.class.getName());

        suite.addTest(new TestSelfRelationExtend("testInitialize"));
        suite.addTest(new TestSelfRelationExtend("testCreate"));
        suite.addTest(new TestSelfRelationExtend("testLoad"));
        suite.addTest(new TestSelfRelationExtend("testOQLExtend"));
        suite.addTest(new TestSelfRelationExtend("testOQLParent"));
        suite.addTest(new TestSelfRelationExtend("testUpdateSimpleProperty"));

        return suite;
    }

    public TestSelfRelationExtend(final String name) {
        super(name);
    }

    // Test are only included/excluded for engines that have been tested with this test suite.

    public boolean include(final DatabaseEngineType engine) {
        return (engine == DatabaseEngineType.DERBY)
            || (engine == DatabaseEngineType.HSQL)
            || (engine == DatabaseEngineType.MYSQL)
            || (engine == DatabaseEngineType.SQL_SERVER);
    }

    public boolean exclude(final DatabaseEngineType engine) {
        return (engine == DatabaseEngineType.ORACLE)
            || (engine == DatabaseEngineType.POSTGRESQL);
    }

    public void testInitialize() throws Exception {
        Database db = getJDOManager(DBNAME, MAPPING).getDatabase();
        db.begin();
        Connection connection = db.getJdbcConnection();
        connection.setAutoCommit(false);

        // delete everything directly
        Statement stmt = connection.createStatement();
        stmt.executeUpdate("DELETE FROM " + PARENT_TABLE_NAME);
        stmt.executeUpdate("DELETE FROM " + EXTEND_TABLE_NAME);
        // insert data
        stmt.execute("INSERT INTO " + PARENT_TABLE_NAME
                + " (id, name) VALUES (1, 'parent')");
        stmt.execute("INSERT INTO " + PARENT_TABLE_NAME
                + " (id, name) VALUES (2, 'first child')");
        stmt.execute("INSERT INTO " + PARENT_TABLE_NAME
                + " (id, name) VALUES (3, 'second child')");
        stmt.execute("INSERT INTO " + EXTEND_TABLE_NAME
                + " (id, parent_id) VALUES (1, null)");
        stmt.execute("INSERT INTO " + EXTEND_TABLE_NAME
                + " (id, parent_id) VALUES (2, 1)");
        stmt.execute("INSERT INTO " + EXTEND_TABLE_NAME
                + " (id, parent_id) VALUES (3, 1)");
        db.commit();
        db.close();
    }
    
    /**
     * this tests creating a folder
     */
    public void testCreate() throws Exception {
        SelfRelationFolderExtend folder = new SelfRelationFolderExtend();
        SelfRelationFolderExtend child = new SelfRelationFolderExtend();
        SelfRelationFolderExtend grandChild = new SelfRelationFolderExtend();
        SelfRelationFolderExtend greatGrandChild = new SelfRelationFolderExtend();
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
        SelfRelationFolderExtend folder = db.load(
            SelfRelationFolderExtend.class, new Integer(1));
        assertEquals("parent", folder.getName());
        assertEquals(1, folder.getId().intValue());

        assertNotNull(folder.getChildren());

        Iterator<SelfRelationFolderExtend> i = folder.getChildren().iterator();
        assertNotNull(i);
        assertTrue("No child loaded", i.hasNext());

        int counter = 0;

        // load first expected child, and assert its properties
        assertTrue(i.hasNext());
        SelfRelationFolderExtend child = i.next();
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

    public void testOQLExtend() throws Exception {
        Database db = getJDOManager(DBNAME, MAPPING).getDatabase();
        db.begin();
        OQLQuery oql = db.getOQLQuery("SELECT a FROM "
                + SelfRelationFolderExtend.class.getName() + " a");
        QueryResults results = oql.execute();
        assertTrue(results.hasMore());

        int counter = 0;
        SelfRelationFolderExtend f;

        f = (SelfRelationFolderExtend) results.next();
        assertNotNull(f);
        assertEquals(1, f.getId().intValue());
        assertEquals("parent", f.getName());
        counter++;

        f = (SelfRelationFolderExtend) results.next();
        assertNotNull(f);
        assertEquals(2, f.getId().intValue());
        assertEquals("first child", f.getName());
        counter++;

        f = (SelfRelationFolderExtend) results.next();
        assertNotNull(f);
        assertEquals(3, f.getId().intValue());
        assertEquals("second child", f.getName());
        counter++;

        f = (SelfRelationFolderExtend) results.next();
        assertNotNull(f);
        assertEquals(5, f.getId().intValue());
        assertEquals("Test Folder", f.getName());
        counter++;

        f = (SelfRelationFolderExtend) results.next();
        assertNotNull(f);
        assertEquals(6, f.getId().intValue());
        assertEquals("Test Child", f.getName());
        counter++;

        f = (SelfRelationFolderExtend) results.next();
        assertNotNull(f);
        assertEquals(7, f.getId().intValue());
        assertEquals("Test Grandchild", f.getName());
        counter++;

        f = (SelfRelationFolderExtend) results.next();
        assertNotNull(f);
        assertEquals(8, f.getId().intValue());
        assertEquals("Test Greatgrandchild", f.getName());
        counter++;

        assertEquals("At least 7 folders should have been returned", 7, counter);

        oql.close();
        db.commit();
        db.close();
    }

    public void testOQLParent() throws Exception {
        Database db = getJDOManager(DBNAME, MAPPING).getDatabase();
        db.begin();
        OQLQuery oql = db.getOQLQuery("SELECT a FROM "
                + SelfRelationFolderParent.class.getName() + " a");
        QueryResults results = oql.execute();
        assertTrue(results.hasMore());

        int counter = 0;
        SelfRelationFolderParent f;

        f = (SelfRelationFolderParent) results.next();
        assertNotNull(f);
        assertTrue(f instanceof SelfRelationFolderExtend);
        assertEquals(1, f.getId().intValue());
        assertEquals("parent", f.getName());
        Collection<SelfRelationFolderExtend> children = ((SelfRelationFolderExtend) f)
                .getChildren();
        assertNotNull(children);
        assertEquals(2, children.size());
        counter++;

        f = (SelfRelationFolderParent) results.next();
        assertNotNull(f);
        assertTrue(f instanceof SelfRelationFolderExtend);
        assertEquals(2, f.getId().intValue());
        assertEquals("first child", f.getName());
        children = ((SelfRelationFolderExtend) f).getChildren();
        assertNotNull(children);
        assertEquals(0, children.size());
        counter++;

        f = (SelfRelationFolderParent) results.next();
        assertNotNull(f);
        assertTrue(f instanceof SelfRelationFolderExtend);
        assertEquals(3, f.getId().intValue());
        assertEquals("second child", f.getName());
        children = ((SelfRelationFolderExtend) f).getChildren();
        assertNotNull(children);
        assertEquals(0, children.size());
        counter++;

        f = (SelfRelationFolderParent) results.next();
        assertNotNull(f);
        assertTrue(f instanceof SelfRelationFolderExtend);
        assertEquals(5, f.getId().intValue());
        assertEquals("Test Folder", f.getName());
        children = ((SelfRelationFolderExtend) f).getChildren();
        assertNotNull(children);
        assertEquals(1, children.size());
        counter++;

        f = (SelfRelationFolderParent) results.next();
        assertNotNull(f);
        assertTrue(f instanceof SelfRelationFolderExtend);
        assertEquals(6, f.getId().intValue());
        assertEquals("Test Child", f.getName());
        children = ((SelfRelationFolderExtend) f).getChildren();
        assertNotNull(children);
        assertEquals(1, children.size());
        counter++;

        f = (SelfRelationFolderParent) results.next();
        assertNotNull(f);
        assertTrue(f instanceof SelfRelationFolderExtend);
        assertEquals(7, f.getId().intValue());
        assertEquals("Test Grandchild", f.getName());
        children = ((SelfRelationFolderExtend) f).getChildren();
        assertNotNull(children);
        assertEquals(1, children.size());
        counter++;

        f = (SelfRelationFolderParent) results.next();
        assertNotNull(f);
        assertTrue(f instanceof SelfRelationFolderExtend);
        assertEquals(8, f.getId().intValue());
        assertEquals("Test Greatgrandchild", f.getName());
        children = ((SelfRelationFolderExtend) f).getChildren();
        assertNotNull(children);
        assertEquals(0, children.size());
        counter++;

        assertEquals("At least 7 folders should have been returned", 7, counter);

        oql.close();
        db.commit();
        db.close();
    }

    /**
     * This tests updating a folder
     */
    public void testUpdateSimpleProperty() throws Exception {
        Database db = getJDOManager(DBNAME, MAPPING).getDatabase();
        db.begin();
        // load a folder, and assert its properties
        SelfRelationFolderExtend folder = db.load(SelfRelationFolderExtend.class, new Integer(6));
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
        folder = db.load(SelfRelationFolderExtend.class, new Integer(6));
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
