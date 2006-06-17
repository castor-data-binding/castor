/*
 * Copyright 2005 Ralf Joachim
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
package ctf.jdo.tc8x;

import harness.CastorTestCase;
import harness.TestHarness;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import jdo.JDOCategory;

import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.ObjectNotFoundException;
import org.exolab.castor.jdo.QueryResults;
import org.exolab.castor.mapping.AccessMode;

/**
 * @author < a href="werner.guttmann@hmx.net">Werner Guttmann</a>
 */
public final class TestLazy1to1 extends CastorTestCase {
    private JDOCategory     _category;

    public TestLazy1to1(final TestHarness category) {
        super(category, "TC87", "TestLazy1to1");
        _category = (JDOCategory) category;
    }

    public void setUp() throws Exception {
        super.setUp();
    }

    public void runTest() throws Exception {
        testCreateParent();
        testLoadChild();
        testLoadParentWhereChildIsNull();
        testLoadParentWithAccess();
        testLoadParentWithoutAccess();
        testSerializeParentWithAccess();
        testSerializeParentWithoutAccess();
        testUpdateChild();
        testUpdateParentMemberWithAccess();
        testUpdateParentMemberWithoutAccess();
        testUpdateParentSetChildToNullWithAccess();
        testLoadBookWithLazyAuthorProperty();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testCreateParent () throws Exception {
        Lazy1to1Parent parent = null;
        Lazy1to1Child child = null;
        
        Database db = _category.getDatabase();
        
        db.begin();
        child = (Lazy1to1Child) db.load(Lazy1to1Child.class, new Integer (1));
        parent = new Lazy1to1Parent();
        parent.setId(new Integer(20000));
        parent.setDescription ("parent 20000");
        parent.setChild(child);
        db.create(parent);
        db.commit();
        
        db.begin();
        parent = (Lazy1to1Parent ) db.load(Lazy1to1Parent.class, new Integer (20000));
        child = parent.getChild();
        assertNotNull(child);
        assertEquals(1, child.getId().intValue());
        db.rollback();
        
        db.begin();
        parent = (Lazy1to1Parent) db.load(Lazy1to1Parent.class, new Integer (20000));
        db.remove(parent);
        db.commit();
        
        db.close();
    }

    public void testLoadChild() throws Exception {
        Lazy1to1Child child = null;

        Database db = _category.getDatabase();

        db.begin();
        child = (Lazy1to1Child) db.load(Lazy1to1Child.class, new Integer (1));
        assertChild(child, 1, "child 1");
        db.commit();
        
        db.close();
    }

    public void testLoadChildReadOnly() throws Exception {
        Lazy1to1Child child = null;

        Database db = _category.getDatabase();

        db.begin();
        child = (Lazy1to1Child) db.load(Lazy1to1Child.class, new Integer (1),
                                        AccessMode.ReadOnly);
        assertChild(child, 1, "child 1");
        db.commit();
        
        db.close();
    }

    public void testLoadParentWhereChildIsNull() throws Exception {
        Lazy1to1Parent parent = null;
        
        Database db = _category.getDatabase();
        
        db.begin();
        parent = (Lazy1to1Parent) db.load(Lazy1to1Parent.class, new Integer (5));
        assertParent(parent, 5, "parent 5");
        assertNull(parent.getChild());
        db.commit();
        
        db.close();
    }

    public void testLoadParentWithAccess() throws Exception {
        Lazy1to1Parent parent = null;
        
        Database db = _category.getDatabase();
        
        db.begin();
        parent = (Lazy1to1Parent) db.load(Lazy1to1Parent.class, new Integer (1));
        db.commit();

        assertParent(parent, 1, "parent 1");
        Lazy1to1Child nature = parent.getChild();
        assertChild(nature, 1, "child 1");

        db.close();
    }

    public void testLoadParentReadOnlyWithAccessWithoutChange() throws Exception {
        Lazy1to1Parent parent = null;
        
        Database db = _category.getDatabase();
        
        db.begin();
        parent = (Lazy1to1Parent) db.load(Lazy1to1Parent.class, new Integer (1),
                                          AccessMode.ReadOnly);
        db.commit();

        assertParent(parent, 1, "parent 1");
        Lazy1to1Child nature = parent.getChild();
        assertChild(nature, 1, "child 1");

        // re-load to assert that nothing has changed
        db.begin();
        parent = (Lazy1to1Parent) db.load(Lazy1to1Parent.class, new Integer (1));
        db.commit();

        assertParent(parent, 1, "parent 1");
        assertChild(parent.getChild(), 1, "child 1");

        db.close();
    }

    public void testLoadParentReadOnlyWithAccessWithChange() throws Exception {
        Lazy1to1Parent parent = null;
        
        Database db = _category.getDatabase();
        
        db.begin();
        parent = (Lazy1to1Parent) db.load(Lazy1to1Parent.class, new Integer (1),
                                          AccessMode.ReadOnly);

        assertParent(parent, 1, "parent 1");
        Lazy1to1Child nature = parent.getChild();
        assertChild(nature, 1, "child 1");

        parent.getChild().setDescription("child changed");

        db.commit();

        // re-load to assert that nothing has changed
        db.begin();
        parent = (Lazy1to1Parent) db.load(Lazy1to1Parent.class, new Integer (1));
        db.rollback();

        assertParent(parent, 1, "parent 1");
        assertChild(parent.getChild(), 1, "child 1");

        db.close();
    }

    public void testLoadParentWithoutAccess() throws Exception {
        Lazy1to1Parent parent = null;
        
        Database db = _category.getDatabase();
        
        db.begin();
        parent = (Lazy1to1Parent) db.load(Lazy1to1Parent.class, new Integer (1));
        assertParent(parent, 1, "parent 1");
        db.commit();
        
        db.close();
    }

    public void testLoadParentReadOnlyWithoutAccess() throws Exception {
        
        Lazy1to1Parent parent = null;
        
        Database db = _category.getDatabase();
        
        db.begin();
        parent = (Lazy1to1Parent) db.load(Lazy1to1Parent.class, new Integer (1),
                                          AccessMode.ReadOnly);
        assertParent(parent, 1, "parent 1");
        db.commit();
        
        db.close();
    }

    public void testSerializeParentWithAccess () throws Exception {
        File file = new File ("serialized.out");
        ObjectOutputStream out = new ObjectOutputStream (new FileOutputStream (file));
        Lazy1to1Parent parent = null;
        
        Database db = _category.getDatabase();
        
        db.begin();
        parent = (Lazy1to1Parent) db.load(Lazy1to1Parent.class, new Integer (1));
        assertParent(parent, 1, "parent 1");
        assertChild(parent.getChild(), 1, "child 1");
        db.commit();
        
        out.writeObject (parent);
        out.close();

        ObjectInputStream in = new ObjectInputStream (new FileInputStream (file));
        Lazy1to1Parent accountDeserialized = (Lazy1to1Parent) in.readObject ();
        assertNotNull(accountDeserialized);
        assertEquals(1, accountDeserialized.getId().intValue());
        assertChild(accountDeserialized.getChild(), 1, "child 1");
        
        db.close();
    }
    
    public void testSerializeParentWithoutAccess () throws Exception {
        File file = new File("serialized.out");
        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream (file));
        
        Lazy1to1Parent parent = null;
        
        Database db = _category.getDatabase();
        
        db.begin();
        parent = (Lazy1to1Parent) db.load(Lazy1to1Parent.class, new Integer (1));
        assertParent (parent, 1, "parent 1");
        db.commit();
        
        out.writeObject (parent);
        out.close();
        
        ObjectInputStream in = new ObjectInputStream(new FileInputStream (file));
        parent = (Lazy1to1Parent) in.readObject ();
        assertNotNull(parent);
        assertEquals(1, parent.getId().intValue());
        assertChild(parent.getChild(), 1, "child 1");
        
        db.close();
    }

    public void testUpdateChild() throws Exception {
        Lazy1to1Child child = null;

        Database db = _category.getDatabase();

        db.begin();
        child = (Lazy1to1Child) db.load(Lazy1to1Child.class, new Integer (2));
        assertChild(child, 2, "child 2");
        child.setDescription ("child 22");
        db.commit();

        db.begin();
        child = (Lazy1to1Child) db.load(Lazy1to1Child.class, new Integer (2));
        assertChild(child, 2, "child 22");
        db.rollback();

        db.begin();
        child = (Lazy1to1Child) db.load(Lazy1to1Child.class, new Integer (2));
        assertChild(child, 2, "child 22");
        child.setDescription("child 2");
        db.commit();

        db.begin();
        child = (Lazy1to1Child) db.load(Lazy1to1Child.class, new Integer (2));
        assertChild(child, 2, "child 2");
        db.rollback();

        db.close();
    }

    public void testUpdateParentMemberWithAccess() throws Exception {
        Lazy1to1Parent parent = null;
        Lazy1to1Child child = null;
        
        Database db = _category.getDatabase();
        
        db.begin();
        parent = (Lazy1to1Parent) db.load(Lazy1to1Parent.class, new Integer (1));
        assertParent(parent, 1, "parent 1");
        child = parent.getChild();
        assertChild(child, 1, "child 1");
        parent.setDescription("parent 11");
        db.commit();

        db.begin();
        parent = (Lazy1to1Parent) db.load(Lazy1to1Parent.class, new Integer (1));
        assertParent(parent, 1, "parent 11");
        child = parent.getChild();
        assertChild(child, 1, "child 1");
        db.rollback();

        db.begin();
        parent = (Lazy1to1Parent) db.load(Lazy1to1Parent.class, new Integer (1));
        assertParent(parent, 1, "parent 11");
        child = parent.getChild();
        assertChild(child, 1, "child 1");
        parent.setDescription("parent 1");
        db.commit();

        db.begin();
        parent = (Lazy1to1Parent) db.load(Lazy1to1Parent.class, new Integer (1));
        assertParent(parent, 1, "parent 1");
        child = parent.getChild();
        assertChild(child, 1, "child 1");
        db.rollback();

        db.close();
    }

    public void testUpdateParentMemberWithoutAccess() throws Exception {
        Lazy1to1Parent parent = null;
        
        Database db = _category.getDatabase();
        
        db.begin();
        parent = (Lazy1to1Parent) db.load(Lazy1to1Parent.class, new Integer (1));
        assertParent(parent, 1, "parent 1");
        parent.setDescription("parent 11");
        db.commit();

        db.begin();
        parent = (Lazy1to1Parent) db.load(Lazy1to1Parent.class, new Integer (1));
        assertParent(parent, 1, "parent 11");
        db.commit();

        db.begin();
        parent = (Lazy1to1Parent) db.load(Lazy1to1Parent.class, new Integer (1));
        assertParent(parent, 1, "parent 11");
        parent.setDescription("parent 1");
        db.commit();

        db.begin();
        parent = (Lazy1to1Parent) db.load(Lazy1to1Parent.class, new Integer (1));
        assertParent(parent, 1, "parent 1");
        db.commit();

        db.close();
    }
    
    public void testUpdateParentSetChildToNullWithAccess() throws Exception {
        Lazy1to1Parent parent = null;
        Lazy1to1Child childNew = null;
        
        Database db = _category.getDatabase();
        
        db.begin();
        parent = (Lazy1to1Parent) db.load(Lazy1to1Parent.class, new Integer (1));
        assertParent(parent, 1, "parent 1");
        assertChild(parent.getChild(), 1, "child 1");
        parent.setChild(null);
        db.commit();
        
        db.begin();
        parent = (Lazy1to1Parent) db.load(Lazy1to1Parent.class, new Integer (1));
        assertParent(parent, 1, "parent 1");
        assertNull(parent.getChild());
        db.rollback();

        db.begin();
        parent = (Lazy1to1Parent) db.load(Lazy1to1Parent.class, new Integer (1));
        assertParent(parent, 1, "parent 1");
        childNew = (Lazy1to1Child) db.load(Lazy1to1Child.class, new Integer (1));
        assertChild(childNew, 1, "child 1");
        parent.setChild(childNew);
        db.commit();

        db.begin();
        parent = (Lazy1to1Parent) db.load(Lazy1to1Parent.class, new Integer (1));
        assertParent(parent, 1, "parent 1");
        assertChild(parent.getChild(), 1, "child 1");
        db.rollback();

        db.close();
    }

    private void assertParent(final Lazy1to1Parent account,
                              final int id, final String description) {
        assertNotNull(account);
        assertEquals(id, account.getId().intValue());
        assertEquals(description, account.getDescription());
    }

    private void assertChild(final Lazy1to1Child nature,
                             final int id, final String description) {
        assertNotNull(nature);
        assertEquals(id, nature.getId().intValue());
        assertEquals(description, nature.getDescription());
    }
    
    public void testLoadBookWithLazyAuthorProperty() throws Exception {
        Database db1 = _category.getDatabase();
        db1.begin();

        try {
            db1.load(Lazy1to1Author.class, new Long(1));
        } catch (ObjectNotFoundException e) {
            fail("Database should contain an author with id=1");
        }

        Database db2 = _category.getDatabase();
        db2.begin();

        OQLQuery qry = db2.getOQLQuery(
                "SELECT o FROM " + Lazy1to1Book.class.getName() + " o");
        QueryResults results = qry.execute();
        assertTrue("Couldn't find the book in db: ", results.hasMore());

        Lazy1to1Book book = null;
        if (results.hasMore()) {
            book = (Lazy1to1Book) results.next();
            Lazy1to1Author currentAuthor = book.getAuthor();

            assertNotNull("book should have author", currentAuthor);
            assertNotNull("author should have a last name", currentAuthor.getLastName());
        }

        db2.commit();
        db2.close();

        if (db1 != null) {
            try {
                db1.commit();
            } catch (Exception ex) {
                ex.printStackTrace();
                fail("Unexpected exception committing wrapper db: " + ex.getMessage());
            } finally {
                db1.close();
            }
        }
    }
}
