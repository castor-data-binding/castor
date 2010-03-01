package org.castor.cascading.one_to_many_bi;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.JDOManager;
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
@ContextConfiguration(locations = { "spring-config-update.xml" })
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
public class UpdateTest extends AbstractTransactionalJUnit4SpringContextTests {

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
	public void changeBook_AutoStore() throws Exception {
		db.setAutoStore(true);
		changeBook();
	}

	@Test
	@Transactional
	public void changeBook_Cascading() throws Exception {
		db.setAutoStore(false);
		changeBook();
	}

	public void changeBook() throws Exception {
		Book book1 = new Book();
		book1.setId(12);
		book1.setName("book1");

		Book book2 = new Book();
		book2.setId(13);
		book2.setName("book2");

		Author author = new Author();
		author.setId(11);
		author.setName("Jack");

		Collection<Book> books = Arrays.asList(book1, book2);
		author.setBooks(books);

		// create objects
		db.begin();
		db.create(book1);
		db.create(book2);
		db.create(author);
		db.commit();

		// load objects
		db.begin();
		Author db_author = db.load(Author.class, 11);
		db.commit();

		// change author
		Iterator<Book> iter = db_author.getBooks().iterator();
		while (iter.hasNext()) {
			Book b = iter.next();
			if (b.getId() == 12) {
				b.setName("new name for book1");
			}
		}

		// update objects
		db.begin();
		db.update(db_author);
		db.commit();

		// load objects again and check
		db.begin();
		Author db_author2 = db.load(Author.class, 11);
		db.commit();

		iter = db_author2.getBooks().iterator();
		while (iter.hasNext()) {
			Book b = iter.next();
			if (b.getId() == 12) {
				assertEquals(b.getName(), "new name for book1");
			}
		}
	}

	@Test
	@Transactional
	public void removeBook_AutoStore() throws Exception {
		db.setAutoStore(true);
		removeBook();
	}

	@Test
	@Transactional
	public void removeBook_Cascading() throws Exception {
		db.setAutoStore(false);
		removeBook();
	}

	public void removeBook() throws Exception {
		Book book1 = new Book();
		book1.setId(12);
		book1.setName("book1");

		Book book2 = new Book();
		book2.setId(13);
		book2.setName("book2");

		Author author = new Author();
		author.setId(11);
		author.setName("Jack");

		author.setBooks(Arrays.asList(book1, book2));

		// create objects
		db.begin();
		db.create(book1);
		db.create(book2);
		db.create(author);
		db.commit();

		// load objects
		db.begin();
		Author db_author = db.load(Author.class, 11);
		db.commit();

		// remove book
		db_author.setBooks(Arrays.asList(book2));

		// update objects
		db.begin();
		db.update(db_author);
		db.commit();

		// load objects again and check
		db.begin();
		Author db_author2 = db.load(Author.class, 11);
		db.commit();

		assertEquals(db_author2.getBooks().size(), 1);
		assertEquals(((Book) db_author2.getBooks().toArray()[0]).getId(), 13);
	}

	@Test
	@Transactional
	public void addBook_AutoStore() throws Exception {
		db.setAutoStore(true);
		addBook();
	}

	@Test
	@Transactional
	public void addBookCascading() throws Exception {
		db.setAutoStore(false);
		addBook();
	}

	public void addBook() throws Exception {
		Book book1 = new Book();
		book1.setId(12);
		book1.setName("book1");

		Book book2 = new Book();
		book2.setId(13);
		book2.setName("book2");

		Author author = new Author();
		author.setId(11);
		author.setName("Jack");

		author.setBooks(Arrays.asList(book1, book2));

		// create objects
		db.begin();
		db.create(book1);
		db.create(book2);
		db.create(author);
		db.commit();

		// load objects
		db.begin();
		Author db_author = db.load(Author.class, 11);
		db.commit();

		// add book
		Book book3 = new Book();
		book3.setId(14);
		book3.setName("book3");
		db_author.setBooks(Arrays.asList(book1, book2, book3));

		// update objects
		db.begin();
		db.update(db_author);
		db.commit();

		// load objects again and check
		db.begin();
		Author db_author2 = db.load(Author.class, 11);
		db.commit();

		assertEquals(db_author2.getBooks().size(), 3);
	}

	@Test
	@Transactional
	public void withNullValue_AutoStore() throws Exception {
		db.setAutoStore(true);
		withNullValue();
	}

	@Test
	@Transactional
	public void withNullValue_Cascading() throws Exception {
		db.setAutoStore(false);
		withNullValue();
	}

	public void withNullValue() throws Exception {
		Book book1 = new Book();
		book1.setId(12);
		book1.setName("book1");

		Book book2 = new Book();
		book2.setId(13);
		book2.setName("book2");

		Author author = new Author();
		author.setId(11);
		author.setName("Jack");

		author.setBooks(Arrays.asList(book1, book2));

		// create objects
		db.begin();
		db.create(book1);
		db.create(book2);
		db.create(author);
		db.commit();

		// load objects
		db.begin();
		Author db_author = db.load(Author.class, 11);
		db.commit();

		// set null
		db_author.setBooks(null);

		// update objects
		try {
			db.begin();
			db.update(db_author);
			db.commit();
			fail("Exception expected");
		} catch (Exception e) {
			// ok
		}
	}

}
