package org.castor.cascading.many_to_many;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.JDOManager;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.NotTransactional;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;

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
    public void setUp() throws Exception {
	db = jdoManager.getDatabase();
	deleteFromTables("ManyToMany_BookAuthor", "ManyToMany_Book", "ManyToMany_Author");
    }

    @Test
    @NotTransactional
    public void create_AutoStore() throws Exception {
	db.setAutoStore(true);

	// author1 --> { book1, book2 }
	// author2 --> { book1, book2, book3 }
	
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

	author1.setBooks(Arrays.asList(book1, book2));
	author2.setBooks(Arrays.asList(book1, book2, book3));

	// persist author1 --> creates book1 & book2 --> creates author2 --> creates book3
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
}
