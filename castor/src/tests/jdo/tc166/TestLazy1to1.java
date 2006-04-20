package jdo.tc166;

import harness.CastorTestCase;
import harness.TestHarness;

import jdo.JDOCategory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.ObjectNotFoundException;
import org.exolab.castor.jdo.QueryResults;

/**
 * @author < a href="werner.guttmann@hmx.net">Werner Guttmann</a>
 */
public class TestLazy1to1 extends CastorTestCase {
    private JDOCategory     _category;
    private Database        _wrapper;

    public TestLazy1to1( TestHarness category ) {
        super( category, "tempTC166", "TestLazy1to1" );
        _category = (JDOCategory) category;
    }

    public TestLazy1to1( TestHarness category, String name, String description ) {
        super( category, name, description );
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
        child = (Lazy1to1Child) db.load (Lazy1to1Child.class, new Integer (1));
        parent = new Lazy1to1Parent();
        parent.setId(new Integer(20000));
        parent.setDescription ("parent 20000");
        parent.setChild (child);
        db.create (parent);
        db.commit();
        
        db.begin();
        parent = (Lazy1to1Parent ) db.load (Lazy1to1Parent.class, new Integer (20000));
        child = parent.getChild();
        assertNotNull (child);
        assertEquals (1, child.getId().intValue());
        db.rollback();
        
        db.begin();
        parent = (Lazy1to1Parent ) db.load (Lazy1to1Parent.class, new Integer (20000));
        db.remove (parent);
        db.commit();
        
        db.close();
    }

    public void testLoadChild() throws Exception {
		Lazy1to1Child child = null;

		Database db = _category.getDatabase();

		db.begin();
		child = (Lazy1to1Child) db.load (Lazy1to1Child.class, new Integer (1));
		assertChild (child, 1, "child 1");
		db.commit();
		
		db.close();
	}

    public void testLoadParentWhereChildIsNull() throws Exception {
        Lazy1to1Parent parent = null;
        
        Database db = _category.getDatabase();
        
        db.begin();
        parent = (Lazy1to1Parent) db.load (Lazy1to1Parent.class, new Integer (5));
        assertParent (parent, 5, "parent 5");
        assertNull (parent.getChild());
        db.commit();
        
        db.close();
    }

    public void testLoadParentWithAccess() throws Exception {
        Lazy1to1Parent parent = null;
        
        Database db = _category.getDatabase();
        
        db.begin();
        parent = (Lazy1to1Parent) db.load (Lazy1to1Parent.class, new Integer (1));
        db.commit();

        assertParent (parent, 1, "parent 1");
        Lazy1to1Child nature = parent.getChild();
        assertChild (nature, 1, "child 1");

        db.close();
    }

    public void testLoadParentWithoutAccess() throws Exception {
        Lazy1to1Parent parent = null;
        
        Database db = _category.getDatabase();
        
        db.begin();
        parent = (Lazy1to1Parent) db.load (Lazy1to1Parent.class, new Integer (1));
        assertParent (parent, 1, "parent 1");
        db.commit();
        
        db.close();
    }

    public void testSerializeParentWithAccess () throws Exception {
        File file = new File ("serialized.out");
        ObjectOutputStream out = new ObjectOutputStream (new FileOutputStream (file));
        Lazy1to1Parent parent = null;
        
        Database db = _category.getDatabase();
        
        db.begin();
        parent = (Lazy1to1Parent) db.load (Lazy1to1Parent.class, new Integer (1));
        assertParent (parent, 1, "parent 1");
        assertChild (parent.getChild(), 1, "child 1");
        db.commit();
        
        out.writeObject (parent);
        out.close();

        ObjectInputStream in = new ObjectInputStream (new FileInputStream (file));
        Lazy1to1Parent accountDeserialized = (Lazy1to1Parent) in.readObject ();
        assertNotNull (accountDeserialized);
        assertEquals (1, accountDeserialized.getId().intValue());
        assertChild(accountDeserialized.getChild(), 1, "child 1");
        
        db.close();
    }
    
    public void testSerializeParentWithoutAccess () throws Exception {
        File file = new File ("serialized.out");
        ObjectOutputStream out = new ObjectOutputStream (new FileOutputStream (file));
        
        Lazy1to1Parent parent = null;
        
        Database db = _category.getDatabase();
        
        db.begin();
        parent = (Lazy1to1Parent) db.load (Lazy1to1Parent.class, new Integer (1));
        assertParent (parent, 1, "parent 1");
        db.commit();
        
        out.writeObject (parent);
        out.close();
        
        ObjectInputStream in = new ObjectInputStream (new FileInputStream (file));
        parent = (Lazy1to1Parent) in.readObject ();
        assertNotNull (parent);
        assertEquals (1, parent.getId().intValue());
        assertChild(parent.getChild(), 1, "child 1");
        
        db.close();
    }

    public void testUpdateChild() throws Exception {
        Lazy1to1Child child = null;

        Database db = _category.getDatabase();

        db.begin();
        child = (Lazy1to1Child) db.load (Lazy1to1Child.class, new Integer (2));
        assertChild (child, 2, "child 2");
        child.setDescription ("child 22");
        db.commit();

        db.begin();
        child = (Lazy1to1Child) db.load (Lazy1to1Child.class, new Integer (2));
        assertChild (child, 2, "child 22");
        db.rollback();

        db.begin();
        child = (Lazy1to1Child) db.load (Lazy1to1Child.class, new Integer (2));
        assertChild (child, 2, "child 22");
        child.setDescription("child 2");
        db.commit();

        db.begin();
        child = (Lazy1to1Child) db.load (Lazy1to1Child.class, new Integer (2));
        assertChild (child, 2, "child 2");
        db.rollback();

        db.close();
    }

    public void testUpdateParentMemberWithAccess() throws Exception {
        Lazy1to1Parent parent = null;
        Lazy1to1Child child = null;
        
        Database db = _category.getDatabase();
        
        db.begin();
        parent = (Lazy1to1Parent) db.load (Lazy1to1Parent.class, new Integer (1));
        assertParent (parent, 1, "parent 1");
        child = parent.getChild();
        assertChild (child, 1, "child 1");
        parent.setDescription ("parent 11");
        db.commit();

        db.begin();
        parent = (Lazy1to1Parent) db.load (Lazy1to1Parent.class, new Integer (1));
        assertParent (parent, 1, "parent 11");
        child = parent.getChild();
        assertChild (child, 1, "child 1");
        db.rollback();

        db.begin();
        parent = (Lazy1to1Parent) db.load (Lazy1to1Parent.class, new Integer (1));
        assertParent (parent, 1, "parent 11");
        child = parent.getChild();
        assertChild (child, 1, "child 1");
        parent.setDescription ("parent 1");
        db.commit();

        db.begin();
        parent = (Lazy1to1Parent) db.load (Lazy1to1Parent.class, new Integer (1));
        assertParent (parent, 1, "parent 1");
        child = parent.getChild();
        assertChild (child, 1, "child 1");
        db.rollback();

        db.close();
    }

    public void testUpdateParentMemberWithoutAccess() throws Exception {
        Lazy1to1Parent parent = null;
        
        Database db = _category.getDatabase();
        
        db.begin();
        parent = (Lazy1to1Parent) db.load (Lazy1to1Parent.class, new Integer (1));
        assertParent (parent, 1, "parent 1");
        parent.setDescription ("parent 11");
        db.commit();

        db.begin();
        parent = (Lazy1to1Parent) db.load (Lazy1to1Parent.class, new Integer (1));
        assertParent (parent, 1, "parent 11");
        db.commit();

        db.begin();
        parent = (Lazy1to1Parent) db.load (Lazy1to1Parent.class, new Integer (1));
        assertParent (parent, 1, "parent 11");
        parent.setDescription ("parent 1");
        db.commit();

        db.begin();
        parent = (Lazy1to1Parent) db.load (Lazy1to1Parent.class, new Integer (1));
        assertParent (parent, 1, "parent 1");
        db.commit();

        db.close();
    }
    
    public void testUpdateParentSetChildToNullWithAccess() throws Exception {
        Lazy1to1Parent parent = null;
        Lazy1to1Child childNew = null;
        
        Database db = _category.getDatabase();
        
        db.begin();
        parent = (Lazy1to1Parent) db.load (Lazy1to1Parent.class, new Integer (1));
        assertParent (parent, 1, "parent 1");
        assertChild (parent.getChild(), 1, "child 1");
        parent.setChild (null);
        db.commit();
        
        db.begin();
        parent = (Lazy1to1Parent) db.load (Lazy1to1Parent.class, new Integer (1));
        assertParent (parent, 1, "parent 1");
        assertNull (parent.getChild());
        db.rollback();

        db.begin();
        parent = (Lazy1to1Parent) db.load (Lazy1to1Parent.class, new Integer (1));
        assertParent (parent, 1, "parent 1");
        childNew = (Lazy1to1Child) db.load (Lazy1to1Child.class, new Integer (1));
        assertChild (childNew, 1, "child 1");
        parent.setChild (childNew);
        db.commit();

        db.begin();
        parent = (Lazy1to1Parent) db.load (Lazy1to1Parent.class, new Integer (1));
        assertParent (parent, 1, "parent 1");
        assertChild (parent.getChild(), 1, "child 1");
        db.rollback();

        db.close();
    }

	private void assertParent (Lazy1to1Parent account, int id, String description) {
		assertNotNull (account);
		assertEquals (id, account.getId().intValue());
		assertEquals (description, account.getDescription());
	}

	private void assertChild (Lazy1to1Child nature, int id, String description) {
		assertNotNull (nature);
		assertEquals (id, nature.getId().intValue());
		assertEquals (description, nature.getDescription());
	}
    
    public void testLoadBookWithLazyAuthorProperty() throws Exception {
        _wrapper = _category.getDatabase();
        _wrapper.begin();

        Lazy1to1Author author;
        try {
            author = (Lazy1to1Author) _wrapper.load(Lazy1to1Author.class, new Long(1));
        } catch (ObjectNotFoundException e) {
            fail("Database should contain an author with id=1");
        }

        Database db = _category.getDatabase();
        db.begin();

        OQLQuery qry = db.getOQLQuery("SELECT o FROM " + Lazy1to1Book.class.getName() + " o");
        QueryResults results = qry.execute();
        assertTrue("Should have a book in db, but couldn't find one: ", results.hasMore());

        Lazy1to1Book book = null;
        if (results.hasMore()){
            book = (Lazy1to1Book) results.next();
            Lazy1to1Author currentAuthor = book.getAuthor();

            assertNotNull("book should have author", currentAuthor);
            assertNotNull("book's author should have a last name", currentAuthor.getLastName());
        }

        db.commit();
        db.close();

        if (_wrapper != null) {
            try {
                _wrapper.commit();
            } catch (Exception ex) {
                ex.printStackTrace();
                fail("Unexpected exception committing wrapper db txn: " + ex.getMessage());
            } finally{
                _wrapper.close();
            }
        }
    }
}
