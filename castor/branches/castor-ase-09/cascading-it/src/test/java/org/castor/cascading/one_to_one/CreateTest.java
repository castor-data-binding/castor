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
 * This class is part of the functional test suite for Castor JDO and assists in
 * testing JPA annotation support.
 * 
 * @author Werner Guttmann
 * @since 1.3.1
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

	@After
	public void tearDown() throws Exception {
		this.simpleJdbcTemplate.update("DELETE FROM OneToOne_Book");
		this.simpleJdbcTemplate.update("DELETE FROM OneToOne_Author");
		db.getCacheManager().expireCache();
	}
	
	@Test
	@NotTransactional
	public void createAutoStore() throws Exception {
		db.setAutoStore(true);

		Author author = new Author();
		author.setId(2);

		Book book = new Book();
		book.setId(1);
		book.setAuthor(author);

		// persist book and therefore author
		// (because cascading=true for the relation book --> author)
		db.begin();
		db.create(book);
		db.commit();

		// now let's see if book & author were properly commited/created
		db.begin();
		Book db_book = db.load(Book.class, 1);
		Author db_author = db.load(Author.class, 2);
		db.commit();

		assertEquals(1, db_book.getId());
		assertEquals(2, db_author.getId());

		db.begin();
		db_book = db.load(Book.class, 1);
		db_author = db.load(Author.class, 2);
		db.remove(db_book);
		db.remove(db_author);
		db.commit();
	}

	@Test
	@NotTransactional
	public void createCascading() throws Exception {

		Author author = new Author();
		author.setId(2);

		Book book = new Book();
		book.setId(1);
		book.setAuthor(author);

		// persist book and therefore author
		// (because cascading=true for the relation book --> author)
		db.begin();
		db.create(book);
		db.commit();

		// now let's see if book & author were properly committed/created
		db.begin();
		Book db_book = db.load(Book.class, 1);
		Author db_author = db.load(Author.class, 2);
		db.commit();

		assertEquals(1, db_book.getId());
		assertEquals(2, db_author.getId());
		
		db.begin();
		db_book = db.load(Book.class, 1);
		db_author = db.load(Author.class, 2);
		db.remove(db_book);
		db.remove(db_author);
		db.commit();
		
	}

	@Test
	@NotTransactional
	public void createNewAuthorForBookAutoStore() throws Exception {
		
		db.setAutoStore(true);
		
		Author author = new Author();
		author.setId(2);

		Book book = new Book();
		book.setId(1);
		book.setAuthor(author);

		// persist book and therefore author
		// (because cascading=true for the relation book --> author)
		db.begin();
		db.create(book);
		db.commit();

		Author newAuthor = new Author();
		newAuthor.setId(4);

		// now let's see if book & author were properly committed/created
		db.begin();
		Book db_book = db.load(Book.class, 1);
		Author db_author = db.load(Author.class, 2);
		db_book.setAuthor(newAuthor);
		db.commit();

		db.begin();
		Book db_book2 = db.load(Book.class, 1);
		Author db_author2 = db.load(Author.class, 4);
		db_author = db.load(Author.class, 2);
		db.commit();

		assertEquals(1, db_book2.getId());
		assertEquals(2, db_author.getId());
		assertEquals(4, db_author2.getId());
		
		db.begin();
		db_book = db.load(Book.class, 1);
		db_author = db.load(Author.class, 2);
		db.remove(db_book);
		db.remove(db_author);
		db.commit();
	}

	@Test
	@NotTransactional
	public void createNewAuthorForBookCascading() throws Exception {
		
		db.setAutoStore(true);
		
		Author author = new Author();
		author.setId(2);

		Book book = new Book();
		book.setId(1);
		book.setAuthor(author);

		// persist book and therefore author
		// (because cascading=true for the relation book --> author)
		db.begin();
		db.create(book);
		db.commit();

		Author newAuthor = new Author();
		newAuthor.setId(4);

		// now let's see if book & author were properly commited/created
		db.begin();
		Book db_book = db.load(Book.class, 1);
		Author db_author = db.load(Author.class, 2);
		db_book.setAuthor(newAuthor);
		db.commit();

		db.begin();
		Book db_book2 = db.load(Book.class, 1);
		Author db_author2 = db.load(Author.class, 4);
		db_author = db.load(Author.class, 2);
		db.commit();

		assertEquals(1, db_book2.getId());
		assertEquals(2, db_author.getId());
		assertEquals(4, db_author2.getId());
		
		db.begin();
		db_book = db.load(Book.class, 1);
		db_author = db.load(Author.class, 2);
		db.remove(db_book);
		db.remove(db_author);
		db.commit();
	}

	@Test
	@Transactional
	@ExpectedException(PersistenceException.class)
	public void createWithNullValueAutoStore() throws Exception {
		db.setAutoStore(true);
		
		Book book = new Book();
		book.setId(1);
		book.setAuthor(null);

		// persist book and therefore null as author
		// (because cascading=true for the relation book --> author)
		// foreign key is not null -> exception should be thrown
		db.begin();
		db.create(book);
		db.commit();
	}

	@Test
	@Transactional
	@ExpectedException(PersistenceException.class)
	public void createWithNullValueCascading() throws Exception {
		Book book = new Book();
		book.setId(1);
		book.setAuthor(null);

		// persist book and therefore null as author
		// (because cascading=true for the relation book --> author)
		// foreign key is not null -> exception should be thrown
		db.begin();
		db.create(book);
		db.commit();
	}

	@Test
	@NotTransactional
	public void createWithExistingIdAutostore() throws Exception {

		db.setAutoStore(true);
		
		Author author = new Author();
		author.setId(2);

		Book book = new Book();
		book.setId(1);
		book.setAuthor(author);

		// persist book and therefore author
		// (because cascading=true for the relation book --> author)
		db.begin();
		db.create(book);
		db.commit();

		Author newAuthor = new Author();
		newAuthor.setId(2);

		Book newBook = new Book();
		newBook.setId(3);
		newBook.setAuthor(newAuthor);

		// persist book and therefore a second Author with id=2
		// (because cascading=true for the relation book --> author)
		// key duplicate -> Exception should be thrown!
		try {
			db.begin();
			db.create(newBook);
			db.commit();
			fail("Exception should have been thrown!");
		} catch (DuplicateIdentityException ex) {
			db.rollback(); // illegal Insert has to be rolled back!
			// everything ok, 'cause Exception has been thrown during create
		}
	}
	
	@Test
	@NotTransactional
	public void createWithExistingIdCascading() throws Exception {

		db.setAutoStore(true);
		
		Author author = new Author();
		author.setId(2);

		Book book = new Book();
		book.setId(1);
		book.setAuthor(author);

		// persist book and therefore author
		// (because cascading=true for the relation book --> author)
		db.begin();
		db.create(book);
		db.commit();

		Author newAuthor = new Author();
		newAuthor.setId(2);

		Book newBook = new Book();
		newBook.setId(3);
		newBook.setAuthor(newAuthor);

		// persist book and therefore a second Author with id=2
		// (because cascading=true for the relation book --> author)
		// key duplicate -> Exception should be thrown!
		try {
			db.begin();
			db.create(newBook);
			db.commit();
			fail("Exception should have been thrown!");
		} catch (DuplicateIdentityException ex) {
			db.rollback(); // illegal Insert has to be rolled back!
			// everything ok, 'cause Exception has been thrown during create
		}
	}
}