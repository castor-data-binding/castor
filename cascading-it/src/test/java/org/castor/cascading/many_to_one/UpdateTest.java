package org.castor.cascading.many_to_one;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.JDOManager;
import org.exolab.castor.jdo.PersistenceException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.NotTransactional;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

//TODO: The OneToMany mapping seems very corrupt. tests should work after fixing this!

/**
 * Verifies the correct behaviour of cascading set to "create".
 * 
 * @author Ivo Friedberg
 */
@ContextConfiguration(locations = { "spring-config-update.xml" })
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
public class UpdateTest extends AbstractTransactionalJUnit4SpringContextTests {

	@Autowired
	private JDOManager jdoManager;

	Database db;

	@Before
	public void setUp() throws Exception {
		db = jdoManager.getDatabase();
		deleteFromTables("ManyToOne_Book", "ManyToOne_Author");
	}

	@Test
	@NotTransactional
	public void changeAuthorAutoStore() throws Exception {
		db.setAutoStore(true);
		
		Author author = new Author();
		author.setId(11);
		author.setName("Jack");

		Book book1 = new Book();
		book1.setId(12);
		book1.setName("book1");
		book1.setAuthor(author);

		Book book2 = new Book();
		book2.setId(13);
		book2.setName("book2");
		book2.setAuthor(author);

		// create objects
		db.begin();
		db.create(author);
		db.create(book1);
		db.create(book2);
		db.commit();

		// load objects
		db.begin();
		Book db_book1 = db.load(Book.class, 12);
		db.commit();

		// change author
		db_book1.getAuthor().setName("new jack");

		// update objects
		db.begin();
		db.update(db_book1);
		db.commit();

		// load objects again and check
		db.begin();
		Author db_author = db.load(Author.class, 11);
		db.commit();

		assertEquals(db_author.getName(), "new jack");
	}

	@Test
	@NotTransactional
	public void changeAuthorCascading() throws Exception {
		Author author = new Author();
		author.setId(11);
		author.setName("Jack");

		Book book1 = new Book();
		book1.setId(12);
		book1.setName("book1");
		book1.setAuthor(author);

		Book book2 = new Book();
		book2.setId(13);
		book2.setName("book2");
		book2.setAuthor(author);

		// create objects
		db.begin();
		db.create(author);
		db.create(book1);
		db.create(book2);
		db.commit();

		// load objects
		db.begin();
		Book db_book1 = db.load(Book.class, 12);
		db.commit();

		// change author
		db_book1.getAuthor().setName("new jack");

		// update objects
		db.begin();
		db.update(db_book1);
		db.commit();

		// load objects again and check
		db.begin();
		Author db_author = db.load(Author.class, 11);
		db.commit();

		assertEquals(db_author.getName(), "new jack");
	}

	@Test
	@Transactional
	public void withNullValue_AutoStore() throws Exception {
		db.setAutoStore(true);
		
		Author author = new Author();
		author.setId(111);
		author.setName("Jack");

		Book book1 = new Book();
		book1.setId(112);
		book1.setName("book1");
		book1.setAuthor(author);

		// create objects
		db.begin();
		db.create(author);
		db.create(book1);
		db.commit();

		// load objects
		db.begin();
		Book db_book1 = db.load(Book.class, 112);
		db.commit();

		// set null
		db_book1.setAuthor(null);

		try {
			// update objects
			db.begin();
			db.update(db_book1);
			db.commit();
			fail("Expected exception");
		} catch (PersistenceException e) {
			// ok
		}
	}

	@Test
	@Transactional
	public void withNullValue_Cascading() throws Exception {
		db.setAutoStore(false);
		
		Author author = new Author();
		author.setId(111);
		author.setName("Jack");

		Book book1 = new Book();
		book1.setId(112);
		book1.setName("book1");
		book1.setAuthor(author);

		// create objects
		db.begin();
		db.create(author);
		db.create(book1);
		db.commit();

		// load objects
		db.begin();
		Book db_book1 = db.load(Book.class, 112);
		db.commit();

		// set null
		db_book1.setAuthor(null);

		try {
			// update objects
			db.begin();
			db.update(db_book1);
			db.commit();
			fail("Expected exception");
		} catch (PersistenceException e) {
			// ok
		}
	}
}
