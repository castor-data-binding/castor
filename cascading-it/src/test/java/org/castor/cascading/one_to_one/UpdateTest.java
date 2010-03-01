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

/**
 * This class is part of the functional test suite for Castor JDO and assists in
 * testing JPA annotation support.
 * 
 * @author Werner Guttmann
 * @since 1.3.1
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

	@After
	public void tearDown() {
		deleteFromTables("OneToOne_Book", "OneToOne_Author");
		db.getCacheManager().expireCache();
	}

	@Test
	@NotTransactional
	public void changeAuthorAutoStore() throws Exception {
		db.setAutoStore(true);
		Author author = new Author();
		author.setId(12);
		author.setName("Jack");

		Book book = new Book();
		book.setId(11);
		book.setAuthor(author);
		book.setName("Book");

		// create objects
		db.begin();
		db.create(author);
		db.create(book);
		db.commit();

		// load objects
		db.begin();
		Book db_book = db.load(Book.class, 11);
		db.commit();

		// change author
		db_book.getAuthor().setName("Joe");

		// update objects
		db.begin();
		db.update(db_book);
		db.commit();

		// load objects again and check
		db.begin();
		Book db_book2 = db.load(Book.class, 11);
		db.commit();

		assertEquals(db_book2.getAuthor().getName(), "Joe");
	}

	@Test
	@NotTransactional
	public void changeAuthorCascading() throws Exception {
		Author author = new Author();
		author.setId(12);
		author.setName("Jack");

		Book book = new Book();
		book.setId(11);
		book.setAuthor(author);
		book.setName("Book");

		// create objects
		db.begin();
		db.create(author);
		db.create(book);
		db.commit();

		// load objects
		db.begin();
		Book db_book = db.load(Book.class, 11);
		db.commit();

		// change author
		db_book.getAuthor().setName("Joe");

		// update objects
		db.begin();
		db.update(db_book);
		db.commit();

		// load objects again and check
		db.begin();
		Book db_book2 = db.load(Book.class, 11);
		db.commit();

		assertEquals(db_book2.getAuthor().getName(), "Joe");
	}

	@Test
	@Transactional
	public void newAuthor_AutoStore() throws Exception {
		db.setAutoStore(true);
		newAuthor();
	}

	@Test
	@Transactional
	public void newAuthor_Cascading() throws Exception {
		db.setAutoStore(false);
		newAuthor();
	}

	public void newAuthor() throws Exception {
		Author author = new Author();
		author.setId(12);
		author.setName("Jack");

		Book book = new Book();
		book.setId(11);
		book.setAuthor(author);
		book.setName("Book");

		// create objects
		db.begin();
		db.create(author);
		db.create(book);
		db.commit();

		// load objects
		db.begin();
		Book db_book = db.load(Book.class, 11);
		db.commit();

		// replace author
		Author author2 = new Author();
		author2.setId(13);
		author2.setName("John");

		db_book.setAuthor(author2);

		// update objects
		db.begin();
		db.update(db_book);
		db.commit();

		// load objects again and check
		db.begin();
		Book db_book2 = db.load(Book.class, 11);
		db.commit();

		assertEquals(db_book2.getAuthor().getName(), "John");
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
		Author author = new Author();
		author.setId(12);
		author.setName("Jack");

		Book book = new Book();
		book.setId(11);
		book.setAuthor(author);
		book.setName("Book");

		// create objects
		db.begin();
		db.create(author);
		db.create(book);
		db.commit();

		// load objects
		db.begin();
		Book db_book = db.load(Book.class, 11);
		db.commit();

		// replace author with null
		db_book.setAuthor(null);

		// foreign key is not null -> exception should be thrown
		try {
			// update objects
			db.begin();
			db.update(db_book);
			db.commit();
			fail("Exception should have bennt thrown!");
		} catch (PersistenceException e) {
			// everything ok, 'cause Exception has been thrown during update
		}
	}

}
