package org.castor.cascading.one_to_many_bi;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.Vector;

import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.DuplicateIdentityException;
import org.exolab.castor.jdo.JDOManager;
import org.exolab.castor.jdo.PersistenceException;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
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
@ContextConfiguration(locations = { "spring-config-create.xml" })
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
public class CreateTest extends AbstractTransactionalJUnit4SpringContextTests {

	@Autowired
	private JDOManager jdoManager;

	Database db;

	@Before
	public void setUp() throws Exception {
		db = jdoManager.getDatabase();
	}

	// TODO: this hangs every single time
	// @After
	public void tearDown() {
		this.simpleJdbcTemplate
				.update("DELETE FROM OneToMany_Bi_Book WHERE 1=1");
		this.simpleJdbcTemplate
				.update("DELETE FROM OneToMany_Bi_Author WHERE 1=1");
		db.getCacheManager().expireCache();
	}

	@Test
	@Transactional
	public void create_AutoStore() throws Exception {
		db.setAutoStore(true);
		create();

	}

	@Test
	@Transactional
	public void create_Cascading() throws Exception {
		db.setAutoStore(false);
		create();
	}

	public void create() throws Exception {
		Author author = new Author();
		author.setId(1);

		Book book = new Book();
		book.setId(2);
		Book book2 = new Book();
		book2.setId(3);

		Vector collection = new Vector();
		collection.add(book);
		collection.add(book2);

		author.setBooks(collection);

		// persist book and therefore author
		// (because cascading=true for the relation book --> author)
		db.begin();
		db.create(author);
		db.commit();

		// now let's see if book & author were properly commited/created
		db.begin();
		Author db_author = db.load(Author.class, 1);
		Book db_book = db.load(Book.class, 2);
		Book db_book2 = db.load(Book.class, 3);
		db.commit();

		assertEquals(1, db_author.getId());
		assertEquals(2, db_book.getId());
		assertEquals(3, db_book2.getId());
	}

	@Test
	@Transactional
	public void createNewAuthorForBook_AutoStore() throws Exception {
		db.setAutoStore(false);
		createNewAuthorForBook();
	}

	@Test
	@Transactional
	public void createNewAuthorForBook_Cascading() throws Exception {
		db.setAutoStore(true);
		createNewAuthorForBook();
	}

	public void createNewAuthorForBook() throws Exception {
		Author author = new Author();
		author.setId(1);

		Book book = new Book();
		book.setId(2);
		Book book2 = new Book();
		book2.setId(3);

		Vector collection = new Vector();
		collection.add(book);
		collection.add(book2);

		author.setBooks(collection);

		// persist book and therefore author
		// (because cascading=true for the relation book --> author)
		db.begin();
		db.create(book);
		db.commit();

		Book newBook = new Book();
		newBook.setId(4);

		// now let's see if book & author were properly commited/created
		db.begin();
		Author db_author = db.load(Author.class, 1);
		collection.clear();
		collection.add(book);
		collection.add(newBook);
		db_author.setBooks(collection);
		db.commit();

		db.begin();
		db_author = db.load(Author.class, 1);
		Book db_book = db.load(Book.class, 2);
		Book db_newBook = db.load(Book.class, 4);
		db.commit();

		assertEquals(2, db_book.getId());
		assertEquals(1, db_author.getId());
		assertEquals(4, db_newBook.getId());
	}

	@Test
	@Transactional
	public void createWithNullValue_AutoStore() throws Exception {
		db.setAutoStore(true);
		createWithNullValue();
	}

	@Test
	@Transactional
	public void createWithNullValue_Cascading() throws Exception {
		db.setAutoStore(false);
		createWithNullValue();
	}

	public void createWithNullValue() throws Exception {
		Author author = new Author();
		author.setId(1);

		Book book = new Book();
		book.setId(2);

		Vector collection = new Vector();
		collection.add(book);
		collection.add(null);

		author.setBooks(collection);

		// should work properly (null should be ignored
		try {
			db.begin();
			db.create(author);
			db.commit();
		} catch (PersistenceException ex) {
			fail("Unexpected Exception");
		}

		Book book2 = new Book();
		book2.setId(3);

		// should not work, cause book needs to have a value
		// in it's foreign key relation!
		try {
			db.begin();
			db.create(book2);
			db.commit();
			fail("An Exception should have been throwed");
		} catch (PersistenceException ex) {
			// everything as it should be!
		}

	}

	@Test
	@Transactional
	public void createWithExistingId_AutoStore() throws Exception {
		db.setAutoStore(true);
		createWithExistingId();
	}

	@Test
	@Transactional
	public void createWithExistingId_Cascading() throws Exception {
		db.setAutoStore(false);
		createWithExistingId();
	}

	public void createWithExistingId() throws Exception {
		Author author = new Author();
		author.setId(1);

		Book book = new Book();
		book.setId(2);

		Vector collection = new Vector();
		collection.add(book);
		author.setBooks(collection);

		// persist author and therefore book
		// (because cascading=true for the relation author --> Book)
		db.begin();
		db.create(author);
		db.commit();

		Author newAuthor = new Author();
		newAuthor.setId(3);

		Book newBook = new Book();
		newBook.setId(2);

		collection.clear();
		collection.add(newBook);
		newAuthor.setBooks(collection);

		// persist book and therefore a second Author with id=2
		// (because cascading=true for the relation book --> author)
		// key duplicate -> Exception should be thrown!
		try {
			db.begin();
			db.create(newAuthor);
			db.commit();
			fail("Exception should have been thrown!");
		} catch (DuplicateIdentityException ex) {
			// TODO look why rollback doesn't work

			db.rollback(); // illegal Insert comment has to be rolled back!
			// everything ok, cause Exception has been thrown while commit!
		}

	}
}
