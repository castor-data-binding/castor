/*
 * Copyright 2009 Werner Guttmann
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.castor.cascading.one_to_one;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.DuplicateIdentityException;
import org.exolab.castor.jdo.JDOManager;
import org.exolab.castor.jdo.PersistenceException;
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
	deleteFromTables("OneToOne_Book", "OneToOne_Author");
    }

    @Test
    @NotTransactional
    public void create_AutoStore() throws Exception {
	db.setAutoStore(true);

	// book --> author
	
	Author author = new Author();
	author.setId(2);

	Book book = new Book();
	book.setId(1);
	book.setAuthor(author);

	// persist book and therefore author
	// (because autostore = true)
	db.begin();
	db.create(book);
	db.commit();

	// check if everything's been created properly
	db.begin();
	Book db_book = db.load(Book.class, 1);
	Author db_author = db.load(Author.class, 2);
	db.commit();

	assertEquals(1, db_book.getId());
	assertEquals(2, db_author.getId());
    }

    @Test
    @NotTransactional
    public void create_Cascading() throws Exception {
	db.setAutoStore(false);

	// book --> author
	
	Author author = new Author();
	author.setId(2);

	Book book = new Book();
	book.setId(1);
	book.setAuthor(author);

	// persist book and therefore author
	// (because cascading = create)
	db.begin();
	db.create(book);
	db.commit();

	// check if everything's been created properly
	db.begin();
	Book db_book = db.load(Book.class, 1);
	Author db_author = db.load(Author.class, 2);
	db.commit();

	assertEquals(1, db_book.getId());
	assertEquals(2, db_author.getId());
    }

    @Test
    @NotTransactional
    public void replaceAuthor_AutoStore() throws Exception {
	db.setAutoStore(true);

	// book --> author
	
	Author author = new Author();
	author.setId(2);

	Book book = new Book();
	book.setId(1);
	book.setAuthor(author);

	// persist book and therefore author
	// (because autostore = true)
	db.begin();
	db.create(book);
	db.commit();

	// replace the book's author with newAuthor
	
	Author newAuthor = new Author();
	newAuthor.setId(4);

	db.begin();
	Book db_book = db.load(Book.class, 1);
	db_book.setAuthor(newAuthor);
	db.commit();

	// check if it worked
	db.begin();
	Book db_book2 = db.load(Book.class, 1);
	Author db_author = db.load(Author.class, 2);
	Author db_author2 = db.load(Author.class, 4);
	db.commit();

	assertEquals(1, db_book2.getId());
	assertEquals(2, db_author.getId());
	assertEquals(4, db_author2.getId());
	assertEquals(4, db_book2.getAuthor().getId());
    }

    @Test
    @NotTransactional
    public void replaceAuthor_Cascading() throws Exception {
	db.setAutoStore(false);
	
	// book --> author
	
	Author author = new Author();
	author.setId(2);

	Book book = new Book();
	book.setId(1);
	book.setAuthor(author);

	// persist book and therefore author
	// (because cascading = create)
	db.begin();
	db.create(book);
	db.commit();

	// replace the book's author with newAuthor
	
	Author newAuthor = new Author();
	newAuthor.setId(4);

	db.begin();
	Book db_book = db.load(Book.class, 1);
	db_book.setAuthor(newAuthor);
	db.commit();

	// check if it worked
	db.begin();
	Book db_book2 = db.load(Book.class, 1);
	Author db_author = db.load(Author.class, 2);
	Author db_author2 = db.load(Author.class, 4);
	db.commit();

	assertEquals(1, db_book2.getId());
	assertEquals(2, db_author.getId());
	assertEquals(4, db_author2.getId());
	assertEquals(4, db_book2.getAuthor().getId());
    }

    @Test
    @Transactional
    @ExpectedException(PersistenceException.class)
    public void createWithNullValue_AutoStore() throws Exception {
	db.setAutoStore(true);

	Book book = new Book();
	book.setId(1);
	book.setAuthor(null);

	// should throw exception since book needs author
	db.begin();
	db.create(book);
	db.commit();
    }

    @Test
    @Transactional
    @ExpectedException(PersistenceException.class)
    public void createWithNullValue_Cascading() throws Exception {
	db.setAutoStore(false);
	
	Book book = new Book();
	book.setId(1);
	book.setAuthor(null);

	// should throw exception since book needs author
	db.begin();
	db.create(book);
	db.commit();
    }

    @Test
    @NotTransactional
    public void createWithExistingId_AutoStore() throws Exception {
	db.setAutoStore(true);

	// book --> author
	
	Author author = new Author();
	author.setId(2);

	Book book = new Book();
	book.setId(1);
	book.setAuthor(author);

	// persist book and therefore author
	// (because autostore = true)
	db.begin();
	db.create(book);
	db.commit();

	// now try to persist a new book with a new author that's using an id that already exists
	
	Author newAuthor = new Author();
	newAuthor.setId(2);

	Book newBook = new Book();
	newBook.setId(3);
	newBook.setAuthor(newAuthor);

	try {
	    db.begin();
	    db.create(newBook);
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

	// book --> author
	
	Author author = new Author();
	author.setId(2);

	Book book = new Book();
	book.setId(1);
	book.setAuthor(author);

	// persist book and therefore author
	// (because cascading = create)
	db.begin();
	db.create(book);
	db.commit();

	// now try to persist a new book with a new author that's using an id that already exists
	
	Author newAuthor = new Author();
	newAuthor.setId(2);

	Book newBook = new Book();
	newBook.setId(3);
	newBook.setAuthor(newAuthor);

	try {
	    db.begin();
	    db.create(newBook);
	    db.commit();
	    fail("An exception should have been thrown");
	} catch (DuplicateIdentityException ex) {
	    // ok
	    db.rollback(); // illegal insert has to be rolled back
	}
    }
}