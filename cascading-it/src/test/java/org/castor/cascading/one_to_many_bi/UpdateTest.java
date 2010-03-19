package org.castor.cascading.one_to_many_bi;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.Iterator;

import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.JDOManager;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.NotTransactional;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.transaction.BeforeTransaction;
import org.springframework.test.context.transaction.TransactionConfiguration;

// TODO: there seem to be problems with the tables not getting cleared! FIX THIS FIRST!

/**
 * Verifies the correct behaviour of cascading set to "create".
 * 
 * @author Ivo Friedberg
 * @author Michael Schroeder
 */
@ContextConfiguration(locations = { "spring-config-update.xml" })
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
public class UpdateTest extends AbstractTransactionalJUnit4SpringContextTests {

    @Autowired
    private JDOManager jdoManager;

    Database db;

    @Before
    @BeforeTransaction
    public void setUp() throws Exception {
	db = jdoManager.getDatabase();
	deleteFromTables("OneToMany_Bi_Book", "OneToMany_Bi_Author");
    }
    
    // TODO: there seems to be a problem with the transactions / clearing the tables

    @Test
    @NotTransactional
    public void changeBook_AutoStore() throws Exception {
	db.setAutoStore(true);

	// author --> { book1, book2 }
	// book1 --> author
	// book2 --> author

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
    @NotTransactional
    public void changeBook_Cascading() throws Exception {
	db.setAutoStore(false);

	// author --> { book1, book2 }
	// book1 --> author
	// book2 --> author

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
    @NotTransactional
    public void removeBook_AutoStore() throws Exception {
	db.setAutoStore(true);

	// author --> { book1, book2 }
	// book1 --> author
	// book2 --> author
	
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

	// remove book2
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
    @NotTransactional
    public void removeBook_Cascading() throws Exception {
	db.setAutoStore(false);

	// author --> { book1, book2 }
	// book1 --> author
	// book2 --> author
	
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

	// remove book2
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
    @NotTransactional
    public void addBook_AutoStore() throws Exception {
	db.setAutoStore(true);

	// author --> { book1, book2 }
	// book1 --> author
	// book2 --> author
	
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

	// add book3
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
    @NotTransactional
    public void addBookCascading() throws Exception {
	db.setAutoStore(false);

	// author --> { book1, book2 }
	// book1 --> author
	// book2 --> author
	
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

	// add book3
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
    @NotTransactional
    public void withNullValue_AutoStore() throws Exception {
	db.setAutoStore(true);

	// author --> { book1, book2 }
	// book1 --> author
	// book2 --> author
	
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

	// set books null
	db_author.setBooks(null);

	// update objects
	try {
	    db.begin();
	    db.update(db_author);
	    db.commit();
	    fail("Expected exception");
	} catch (Exception e) {
	    // ok
	}
    }

    @Test
    @NotTransactional
    public void withNullValue_Cascading() throws Exception {
	db.setAutoStore(false);

	// author --> { book1, book2 }
	// book1 --> author
	// book2 --> author
	
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

	// set books null
	db_author.setBooks(null);

	// update objects
	try {
	    db.begin();
	    db.update(db_author);
	    db.commit();
	    fail("Expected exception");
	} catch (Exception e) {
	    // ok
	}
    }
}
