package org.castor.cascading.one_to_many_bi;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.Vector;

import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.DuplicateIdentityException;
import org.exolab.castor.jdo.JDOManager;
import org.exolab.castor.jdo.PersistenceException;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.NotTransactional;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.transaction.BeforeTransaction;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

/**
 * Verifies the correct behaviour of cascading set to "create".
 * 
 * @author Ivo Friedberg
 * @author Michael Schroeder
 */
@ContextConfiguration(locations = { "spring-config-create.xml" })
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
public class CreateTest extends AbstractTransactionalJUnit4SpringContextTests {

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
    public void create_AutoStore() throws Exception {
	db.setAutoStore(true);

	// author --> { book, book2 }
	// book --> author
	// book2 --> author

	Author author = new Author();
	author.setId(1);

	Book book = new Book();
	book.setId(2);
	book.setAuthor(author);

	Book book2 = new Book();
	book2.setId(3);
	book2.setAuthor(author);

	author.setBooks(Arrays.asList(book, book2));

	// create author and therefore books
	// (because autostore = true)
	db.begin();
	db.create(author);
	db.commit();

	// check if everything's been created properly
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
    @NotTransactional
    public void create_Cascading() throws Exception {
	db.setAutoStore(false);

	// author --> { book, book2 }
	// book --> author
	// book2 --> author

	Author author = new Author();
	author.setId(1);

	Book book = new Book();
	book.setId(2);
	book.setAuthor(author);

	Book book2 = new Book();
	book2.setId(3);
	book2.setAuthor(author);

	author.setBooks(Arrays.asList(book, book2));

	// create author and therefore books
	// (because cascading = create)
	db.begin();
	db.create(author);
	db.commit();

	// check if everything's been created properly
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
    @NotTransactional
    public void modifyCollection_AutoStore() throws Exception {
	db.setAutoStore(true);

	// author --> { book, book2 }
	// book --> author
	// book2 --> author

	Author author = new Author();
	author.setId(1);

	Book book = new Book();
	book.setId(2);
	book.setAuthor(author);

	Book book2 = new Book();
	book2.setId(3);
	book2.setAuthor(author);

	author.setBooks(Arrays.asList(book, book2));

	// create author and therefore books
	// (because autostore = true)
	db.begin();
	db.create(author);
	db.commit();

	// change author's collection by replacing book2 with newBook
	// (which hasn't been created yet)
	Book newBook = new Book();
	newBook.setId(4);
	newBook.setAuthor(author);

	db.begin();
	Author db_author = db.load(Author.class, 1);
	db_author.setBooks(Arrays.asList(book, newBook));
	db.commit();

	// check if everything's been created properly
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
    @NotTransactional
    public void modifyCollection_Cascading() throws Exception {
	db.setAutoStore(false);

	// author --> { book, book2 }
	// book --> author
	// book2 --> author

	Author author = new Author();
	author.setId(1);

	Book book = new Book();
	book.setId(2);
	book.setAuthor(author);

	Book book2 = new Book();
	book2.setId(3);
	book2.setAuthor(author);

	author.setBooks(Arrays.asList(book, book2));

	// create author and therefore books
	// (because autostore = true)
	db.begin();
	db.create(author);
	db.commit();

	// change author's collection by replacing book2 with newBook
	// (which hasn't been created yet)
	Book newBook = new Book();
	newBook.setId(4);
	newBook.setAuthor(author);

	db.begin();
	Author db_author = db.load(Author.class, 1);
	db_author.setBooks(Arrays.asList(book, newBook));
	db.commit();

	// check if everything's been created properly
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
    @NotTransactional
    public void createWithNullValue_AutoStore() throws Exception {
	db.setAutoStore(true);

	Author author = new Author();
	author.setId(1);

	Book book = new Book();
	book.setId(2);
	book.setAuthor(author);

	author.setBooks(Arrays.asList(book, null));

	// should work, because null value in collection should be ignored
	// TODO: SHOULD IT REALLY? WHAT DOES IT MATTER? (this one fails and I don't think thats necessarily a bug)
	try {
	    db.begin();
	    db.create(author);
	    db.commit();
	} catch (PersistenceException ex) {
	    fail("Unexpected exception: " + ex.getMessage());
	}

	Book book2 = new Book();
	book2.setId(3);
	book2.setAuthor(null);

	// should not work, because book needs to have an author set
	try {
	    db.begin();
	    db.create(book2);
	    db.commit();
	    fail("An exception should have been thrown");
	} catch (PersistenceException ex) {
	    // ok
	}
    }

    @Test
    @NotTransactional
    public void createWithNullValue_Cascading() throws Exception {
	db.setAutoStore(false);

	Author author = new Author();
	author.setId(1);

	Book book = new Book();
	book.setId(2);
	book.setAuthor(author);

	author.setBooks(Arrays.asList(book, null));

	// should work, because null value in collection should be ignored
	// TODO: SHOULD IT REALLY? WHAT DOES IT MATTER? (this one fails and I don't think thats necessarily a bug)
	try {
	    db.begin();
	    db.create(author);
	    db.commit();
	} catch (PersistenceException ex) {
	    fail("Unexpected exception: " + ex.getMessage());
	}

	Book book2 = new Book();
	book2.setId(3);
	book2.setAuthor(null);

	// should not work, because book needs to have an author set
	try {
	    db.begin();
	    db.create(book2);
	    db.commit();
	    fail("An exception should have been thrown");
	} catch (PersistenceException ex) {
	    // ok
	}
    }

    @Test
    @NotTransactional
    public void createWithExistingId_AutoStore() throws Exception {
	db.setAutoStore(true);

	Author author = new Author();
	author.setId(1);

	Book book = new Book();
	book.setId(2);
	book.setAuthor(author);

	author.setBooks(Arrays.asList(book));

	// create author and therefore book
	// (because autostore = true)
	db.begin();
	db.create(author);
	db.commit();

	// now try to persist a new author with a new book that's using an id that already exists
	Author newAuthor = new Author();
	newAuthor.setId(3);

	Book newBook = new Book();
	newBook.setId(2);
	newBook.setAuthor(newAuthor);

	newAuthor.setBooks(Arrays.asList(newBook));

	try {
	    db.begin();
	    db.create(newAuthor);
	    db.commit();
	    fail("An exception should have been thrown");
	} catch (DuplicateIdentityException ex) {
	    // ok
	    db.rollback(); // illegal insert has to be rolled back
	}
    }

    @Test
    @NotTransactional
    public void createWithExistingId_Cascading() throws Exception {
	db.setAutoStore(false);

	Author author = new Author();
	author.setId(1);

	Book book = new Book();
	book.setId(2);
	book.setAuthor(author);

	author.setBooks(Arrays.asList(book));

	// create author and therefore book
	// (because cascading = create)
	db.begin();
	db.create(author);
	db.commit();

	// now try to persist a new author with a new book that's using an id that already exists
	Author newAuthor = new Author();
	newAuthor.setId(3);

	Book newBook = new Book();
	newBook.setId(2);
	newBook.setAuthor(newAuthor);

	newAuthor.setBooks(Arrays.asList(newBook));

	try {
	    db.begin();
	    db.create(newAuthor);
	    db.commit();
	    fail("An exception should have been thrown");
	} catch (DuplicateIdentityException ex) {
	    // ok
	    db.rollback(); // illegal insert has to be rolled back
	}
    }
}
