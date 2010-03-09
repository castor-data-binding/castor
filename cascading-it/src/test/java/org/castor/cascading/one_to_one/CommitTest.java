package org.castor.cascading.one_to_one;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.DuplicateIdentityException;
import org.exolab.castor.jdo.JDOManager;
import org.exolab.castor.jdo.ObjectNotFoundException;
import org.exolab.castor.jdo.PersistenceException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.ExpectedException;
import org.springframework.test.annotation.NotTransactional;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.transaction.BeforeTransaction;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

/**
 * Test Castor's ability to auto-commit changes across relationship boundaries
 * @author Michael Schroeder
 */
@ContextConfiguration(locations = { "spring-config-delete.xml" })
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
public class CommitTest extends AbstractTransactionalJUnit4SpringContextTests {

	@Autowired
	private JDOManager jdoManager;

	Database db;

	@Before
	@BeforeTransaction
	public void setUp() throws Exception {
		db = jdoManager.getDatabase();
		deleteFromTables("OneToOne_Book", "OneToOne_Author");
	}
	
	@Test
	@NotTransactional
	public void commit() throws Exception {
	    Author a = new Author();
	    a.setId(1);
	    a.setName("John Jackson");
	    
	    Book b = new Book();
	    b.setId(1);
	    b.setName("My Life");
	    b.setAuthor(a);
	    
	    db.begin();
	    db.create(a);  // let's not take any chances
	    db.create(b);
	    db.commit();
	    
	    // get the book from the db and through that change the author's name
	    db.begin();
	    Book db_b = db.load(Book.class, 1);
	    db_b.getAuthor().setName("Jack Johnson");
	    db.commit();
	    
	    db.begin();
	    Author db_a = db.load(Author.class, 1);
	    db.commit();
	    
	    assertEquals("Jack Johnson", db_a.getName());
	}
	
	@Test
	@NotTransactional
	public void commitLater() throws Exception {
	    Author a = new Author();
	    a.setId(1);
	    a.setName("John Jackson");
	    
	    Book b = new Book();
	    b.setId(1);
	    b.setName("My Life");
	    b.setAuthor(a);
	    
	    db.begin();
	    db.create(a);  // let's not take any chances
	    db.create(b);
	    db.commit();
	    
	    // get the book from the db...
	    db.begin();
	    Book db_b = db.load(Book.class, 1);	    
	    db.commit();
	    
	    // ...and now, in a different transaction, change the name of the author through the book
	    db.begin();
	    db_b.getAuthor().setName("Jack Johnson");
	    db.commit();
	    
	    db.begin();
	    Author db_a = db.load(Author.class, 1);
	    db.commit();
	    
	    // this fails, but I guess castor intentionally doesn't provide this kind of functionality in this way
	    assertEquals("Jack Johnson", db_a.getName());
	}
}