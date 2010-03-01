package org.castor.cascading.many_to_one;

import static org.junit.Assert.assertEquals;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.JDOManager;
import org.exolab.castor.jdo.PersistenceException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.ExpectedException;
import org.springframework.test.annotation.NotTransactional;
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

	@After
	public void tearDown() {
		deleteFromTables("ManyToOne_Book", "ManyToOne_Author");
		db.getCacheManager().expireCache();
	}

	@Test
	@NotTransactional
	public void createAutoStore() throws Exception {
		db.setAutoStore(true);
		Author author = new Author();
		author.setId(1);

		Book book = new Book();
		book.setId(2);
		book.setAuthor(author);
		Book book2 = new Book();
		book2.setId(3);
		book2.setAuthor(author);

		// persist book and therefore author
		// (because cascading=true for the relation book --> author)
		db.begin();
		db.create(book);
		db.create(book2);
		db.commit();

		// now let's see if book & author were properly committed/created
		db.begin();
		Author db_author = db.load(Author.class, 1);
		Book db_book = db.load(Book.class, 2);
		Book db_book2 = db.load(Book.class, 3);
		db.commit();

		assertEquals(1, db_author.getId());
		assertEquals(2, db_book.getId());
		assertEquals(3, db_book2.getId());

		db.begin();
		db_author = db.load(Author.class, 1);
		db_book = db.load(Book.class, 2);
		db_book2 = db.load(Book.class, 3);
		db.remove(db_author);
		db.remove(db_book);
		db.remove(db_book2);
		db.commit();
	}

	@Test
	@NotTransactional
	public void createCasacading() throws Exception {
		Author author = new Author();
		author.setId(1);

		Book book = new Book();
		book.setId(2);
		book.setAuthor(author);
		Book book2 = new Book();
		book2.setId(3);
		book2.setAuthor(author);

		// persist book and therefore author
		// (because cascading=true for the relation book --> author)
		db.begin();
		db.create(book);
		db.create(book2);
		db.commit();

		// now let's see if book & author were properly committed/created
		db.begin();
		Author db_author = db.load(Author.class, 1);
		Book db_book = db.load(Book.class, 2);
		Book db_book2 = db.load(Book.class, 3);
		db.commit();

		assertEquals(1, db_author.getId());
		assertEquals(2, db_book.getId());
		assertEquals(3, db_book2.getId());

		db.begin();
		db_author = db.load(Author.class, 1);
		db_book = db.load(Book.class, 2);
		db_book2 = db.load(Book.class, 3);
		db.remove(db_author);
		db.remove(db_book);
		db.remove(db_book2);
		db.commit();
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
		book.setAuthor(author);
		Book book2 = new Book();
		book2.setId(3);
		book2.setAuthor(author);

		// persist book and therefore author
		// (because cascading=true for the relation book --> author)
		db.begin();
		db.create(book);
		db.create(book2);
		db.commit();

		Author newAuthor = new Author();
		newAuthor.setId(4);

		// now let's see if book & author were properly commited/created
		db.begin();
		Book db_book = db.load(Book.class, 2);
		db_book.setAuthor(newAuthor);
		db.commit();

		db.begin();
		db_book = db.load(Book.class, 2);
		Book db_book2 = db.load(Book.class, 3);
		Author db_author = db.load(Author.class, 1);
		Author db_newAuthor = db.load(Author.class, 4);
		db.commit();

		assertEquals(2, db_book.getId());
		assertEquals(3, db_book2.getId());
		assertEquals(1, db_author.getId());
		assertEquals(4, db_newAuthor.getId());
	}

	@Test
	@Transactional
	@ExpectedException(PersistenceException.class)
	public void createWithNullValue_AutoStore() throws Exception {
		db.setAutoStore(true);
		createWithNullValue();
	}

	@Test
	@Transactional
	@ExpectedException(PersistenceException.class)
	public void createWithNullValue_Cascading() throws Exception {
		db.setAutoStore(false);
		createWithNullValue();
	}

	public void createWithNullValue() throws Exception {
		Book book = new Book();
		book.setId(2);
		book.setAuthor(null);

		// should not work cause null is not allowed
		db.begin();
		db.create(book);
		db.commit();
	}

}
