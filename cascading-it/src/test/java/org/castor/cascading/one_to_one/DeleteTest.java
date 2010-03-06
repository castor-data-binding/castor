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
import org.exolab.castor.jdo.ObjectNotFoundException;
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
@ContextConfiguration(locations = { "spring-config-delete.xml" })
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
public class DeleteTest extends AbstractTransactionalJUnit4SpringContextTests {

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
	public void deleteAutoStore() throws Exception {
		db.setAutoStore(true);

		Author author = new Author();
		author.setId(2);

		Book book = new Book();
		book.setId(1);
		book.setAuthor(author);

		// persist author and book
		db.begin();
		db.create(author);
		db.create(book);
		db.commit();

		// we assume that both have been created successfully
		
		// now delete
		db.begin();
		Book db_book = db.load(Book.class, 1);
		Author db_author = db.load(Author.class, 2);
		db.remove(db_book);
		db.commit();
		
		// has the author also been removed?
		try {
		    db.begin();		
		    Author db_author2 = db.load(Author.class, 2);
		    db.commit();
		    fail("Author should have been deleted!");
		} catch (ObjectNotFoundException ex) {
		    db.rollback();
		}
	}
	
	@Test
	@NotTransactional
	public void deleteCascading() throws Exception {
		db.setAutoStore(false);

		Author author = new Author();
		author.setId(2);

		Book book = new Book();
		book.setId(1);
		book.setAuthor(author);

		// persist author and book
		db.begin();
		db.create(author);
		db.create(book);
		db.commit();

		// we assume that both have been created successfully
		
		// now delete
		db.begin();
		Book db_book = db.load(Book.class, 1);
		Author db_author = db.load(Author.class, 2);
		db.remove(db_book);
		db.commit();
		
		// has the author also been removed?
		try {
		    db.begin();		
		    Author db_author2 = db.load(Author.class, 2);
		    db.commit();
		    fail("Author should have been deleted!");
		} catch (ObjectNotFoundException ex) {
		    db.rollback();
		}
	}
}