package org.castor.cascading.many_to_many;

import static org.junit.Assert.assertEquals;

import java.util.Vector;

import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.JDOManager;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

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

	// @After
	public void tearDown() {
		this.simpleJdbcTemplate
				.update("DELETE FROM ManyToMany_BookAuthor WHERE 1=1");
		this.simpleJdbcTemplate.update("DELETE FROM ManyToMany_Book WHERE 1=1");
		this.simpleJdbcTemplate
				.update("DELETE FROM ManyToMany_Author WHERE 1=1");
		db.getCacheManager().expireCache();
	}

	@Test
	@Transactional
	public void create_AutoStore() throws Exception {
		db.setAutoStore(true);
		create();

	}

	/*
	 * @Test
	 * 
	 * @Transactional public void create_Cascading() throws Exception {
	 * db.setAutoStore(false); create(); }
	 */

	public void create() throws Exception {
		Author author1 = new Author();
		author1.setId(1);

		Author author2 = new Author();
		author2.setId(2);

		Book book1 = new Book();
		book1.setId(11);
		Book book2 = new Book();
		book2.setId(12);
		Book book3 = new Book();
		book3.setId(13);

		Vector collection1 = new Vector();
		collection1.add(book1);
		collection1.add(book2);
		author1.setBooks(collection1);

		Vector collection2 = new Vector();
		collection2.add(book1);
		collection2.add(book2);
		collection2.add(book3);
		author2.setBooks(collection2);

		// persist author1 and therefore book2 and therefore author2 and
		// therefore book3
		db.begin();
		db.create(author1);
		db.commit();

		// now let's see if book & author were properly commited/created
		db.begin();
		Author db_author1 = db.load(Author.class, 1);
		Author db_author2 = db.load(Author.class, 2);
		Book db_book1 = db.load(Book.class, 11);
		Book db_book2 = db.load(Book.class, 12);
		Book db_book3 = db.load(Book.class, 13);
		db.commit();

		assertEquals(1, db_author1.getId());
		assertEquals(2, db_author2.getId());
		assertEquals(11, db_book1.getId());
		assertEquals(12, db_book2.getId());
		assertEquals(13, db_book3.getId());
	}

	/*
	 * @Test
	 * 
	 * @Transactional public void createNewAuthorForBook_AutoStore() throws
	 * Exception { db.setAutoStore(false); createNewAuthorForBook(); }
	 * 
	 * @Test
	 * 
	 * @Transactional public void createNewAuthorForBook_Cascading() throws
	 * Exception { db.setAutoStore(true); createNewAuthorForBook(); }
	 * 
	 * public void createNewAuthorForBook() throws Exception { Author author =
	 * new Author(); author.setId(1);
	 * 
	 * Book book = new Book(); book.setId(2); book.setAuthor(author); Book book2
	 * = new Book(); book2.setId(3); book2.setAuthor(author);
	 * 
	 * // persist book and therefore author // (because cascading=true for the
	 * relation book --> author) db.begin(); db.create(book); db.create(book2);
	 * db.commit();
	 * 
	 * Author newAuthor = new Author(); newAuthor.setId(4);
	 * 
	 * // now let's see if book & author were properly commited/created
	 * db.begin(); Book db_book = db.load(Book.class, 2);
	 * db_book.setAuthor(newAuthor); db.commit();
	 * 
	 * db.begin(); db_book = db.load(Book.class, 2); Book db_book2 =
	 * db.load(Book.class, 3); Author db_author = db.load(Author.class, 1);
	 * Author db_newAuthor = db.load(Author.class, 4); db.commit();
	 * 
	 * assertEquals(2, db_book.getId()); assertEquals(3, db_book2.getId());
	 * assertEquals(1, db_author.getId()); assertEquals(4,
	 * db_newAuthor.getId()); }
	 * 
	 * @Test
	 * 
	 * @Transactional
	 * 
	 * @ExpectedException(PersistenceException.class) public void
	 * createWithNullValue_AutoStore() throws Exception { db.setAutoStore(true);
	 * createWithNullValue(); }
	 * 
	 * @Test
	 * 
	 * @Transactional
	 * 
	 * @ExpectedException(PersistenceException.class) public void
	 * createWithNullValue_Cascading() throws Exception {
	 * db.setAutoStore(false); createWithNullValue(); }
	 * 
	 * public void createWithNullValue() throws Exception { Book book = new
	 * Book(); book.setId(2); book.setAuthor(null);
	 * 
	 * // should not work cause null is not allowed db.begin(); db.create(book);
	 * db.commit(); }
	 */

}
