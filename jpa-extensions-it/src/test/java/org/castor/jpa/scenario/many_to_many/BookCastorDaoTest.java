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
package org.castor.jpa.scenario.many_to_many;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.castor.spring.orm.CastorObjectRetrievalFailureException;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.JDOManager;
import org.exolab.castor.jdo.PersistenceException;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

/**
 * This class is part of the functional test suite for Castor JDO
 * and assists in testing JPA annotation support.
 * 
 * @author Werner Guttmann
 * @since 1.3.1
 */
// @Ignore
@ContextConfiguration(locations = { "spring-config.xml" })
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
public class BookCastorDaoTest extends
        AbstractTransactionalJUnit4SpringContextTests {

    private static Log LOG = LogFactory.getLog(BookCastorDaoTest.class);

    @Autowired
    protected JDOManager jdoManager;

    @Autowired
    private BookDao bookDao;

    @Autowired
    private AuthorDao authorDao;
    
//    @SuppressWarnings("unused")
//    @Before
//    @After
//    public void cleanData() throws PersistenceException, SQLException {
//        Database database = this.jdoManager.getDatabase();
//        database.begin();
//        Connection connection = database.getJdbcConnection();
//        Statement statement = connection.createStatement();
//        statement.executeUpdate("delete from ManyToMany_author");
//        statement.executeUpdate("delete from ManyToMany_book");
//        statement.close();
////        connection.close();
//        database.commit();
//        database.close();
//    }

    @Test
    @Transactional
    public void saveBookWithEmptyAuthor() {
        Book book = new Book();
        book.setIsbn(1234);
        book.setTitle("First");
        book.setAuthors(new LinkedList<Author>());

        this.bookDao.saveBook(book);

        Book got = this.bookDao.getBook(1234);
        assertNotNull(got);
        assertEquals("First", got.getTitle());
        
        //cleanup data
        
        this.bookDao.deleteBook(got);
        try {
            Book checkIfDeleted = this.bookDao.getBook(1234);
            fail ("No book with identifier of >1234< should exist.");
        }
        catch (CastorObjectRetrievalFailureException e) {
            // expected
        }
    }

    @Test
    @Rollback(false)
    public void saveBookWithNoAuthorAndAddAuthorLater() throws PersistenceException {

        Database db = jdoManager.getDatabase();
        db.setAutoStore(true);

        db.begin();
        Book book = new Book();
        book.setIsbn(1234);
        book.setTitle("FirstBook");
        this.bookDao.saveBook(book);
        db.commit();
        
        db.begin();
        Book lookup = this.bookDao.getBook(1234);
        assertNotNull(lookup);
        assertEquals(1234, lookup.getIsbn());
        db.commit();
        
        db.begin();
        Author author = new Author();
        author.setId(1234);
        author.setName("FirstAuthor");
        this.authorDao.saveAuthor(author);
        db.commit();
        
        db.begin();
        Author authorLookup = this.authorDao.getAuthor(1234);
        assertNotNull(authorLookup);
        assertEquals(1234, authorLookup.getId());
        db.commit();
        
        db.begin();
        authorLookup = this.authorDao.getAuthor(1234);
        lookup = this.bookDao.getBook(1234);
        lookup.setAuthors(Collections.singletonList(authorLookup));
        db.commit();
        
        db.begin();
        Book bookLookup = this.bookDao.getBook(1234);
        assertNotNull(bookLookup);
        assertEquals(1234, bookLookup.getIsbn());
        assertNotNull(bookLookup.getAuthors());
        assertFalse(bookLookup.getAuthors().isEmpty());
        assertEquals(1, bookLookup.getAuthors().size());
        db.commit();

        db.begin();
        Book book2Delete = this.bookDao.getBook(1234);
        this.bookDao.deleteBook(book2Delete);
        Author author2Delete = this.authorDao.getAuthor(1234);
        this.authorDao.deleteAuthor(author2Delete);
        db.commit();

        db.begin();
        try {
            this.bookDao.getBook(1234);
            fail ("No book with identifier of >1234< should exist.");
        }
        catch (CastorObjectRetrievalFailureException e) {
            // expected
        }
        try {
            this.authorDao.getAuthor(1234);
            fail ("No author with identifier of >1234< should exist.");
        }
        catch (CastorObjectRetrievalFailureException e) {
            // expected
        }
        db.commit();

    }

    @Test
    @Rollback(false)
    public void save() throws PersistenceException {

        Database db = jdoManager.getDatabase();
        db.setAutoStore(true);

        db.begin();

        Book book = new Book();
        book.setIsbn(1234);
        book.setTitle("FirstBook");
        this.bookDao.saveBook(book);

        Author author1 = new Author();
        author1.setId(1234);
        author1.setName("FirstAuthor");

        Author author2 = new Author();
        author2.setId(5678);
        author2.setName("SecondAuthor");

        this.authorDao.saveAuthor(author1);
        this.authorDao.saveAuthor(author2);

        db.commit();

        db.begin();
        book = this.bookDao.getBook(1234);
        author1 = this.authorDao.getAuthor(1234);
        author2 = this.authorDao.getAuthor(5678);

        LinkedList<Book> books = new LinkedList<Book>();
        books.add(book);
        author1.setBooks(books);

        books = new LinkedList<Book>();
        books.add(book);
        author2.setBooks(books);

        List<Author> authors = new LinkedList<Author>();
        authors.add(author1);
        authors.add(author2);
        book.setAuthors(authors);
        book.setTitle("first book");
        LOG.debug("Committing M:N link");
        db.commit();

        Book got = this.bookDao.getBook(1234);
        assertNotNull(got);
        assertEquals("first book", got.getTitle());
        assertEquals(2, got.getAuthors().size());
        Iterator<Author> iterator = got.getAuthors().iterator();
        assertEquals("FirstAuthor", iterator.next().getName());
        assertEquals("SecondAuthor", iterator.next().getName());
        
        // delete object instances
        this.bookDao.deleteBook(got);
        
        this.authorDao.deleteAuthor(author1);
        this.authorDao.deleteAuthor(author2);
        
        try {
            this.bookDao.getBook(1234);
            fail ("No book with identifier of >1234< should exist.");
        }
        catch (CastorObjectRetrievalFailureException e) {
            // expected
        }

        try {
            this.authorDao.getAuthor(1234);
            fail ("No author with identifier of >1234< should exist.");
        }
        catch (CastorObjectRetrievalFailureException e) {
            // expected
        }

        try {
            this.authorDao.getAuthor(5678);
            fail ("No author with identifier of >5678< should exist.");
        }
        catch (CastorObjectRetrievalFailureException e) {
            // expected
        }

    }

    @Test
    @Transactional
    public void delete() {
        Author author = new Author();
        author.setId(1234);
        author.setName("FirstAuthor");
        author.setBooks(new LinkedList<Book>());

        List<Author> authors = new LinkedList<Author>();
        authors.add(author);

        Book book = new Book();
        book.setIsbn(1234);
        book.setTitle("FirstBook");
        book.setAuthors(authors);

        this.bookDao.saveBook(book);

        Book got = this.bookDao.getBook(1234);
        assertNotNull(got);
        assertEquals("FirstBook", got.getTitle());
        assertEquals(1, got.getAuthors().size());
        assertEquals("FirstAuthor", got.getAuthors().iterator().next()
                .getName());

        this.bookDao.deleteBook(book);

        try {
            this.bookDao.getBook(1234);
            fail();
        } catch (CastorObjectRetrievalFailureException e) {
        }
    }

    @Test
    @Transactional
    public void updateAddAuthor() throws PersistenceException {
        Database db = jdoManager.getDatabase();

        Author author = new Author();
        author.setId(1234);
        author.setName("FirstAuthor");
        author.setBooks(new LinkedList<Book>());

        List<Author> authors = new LinkedList<Author>();
        authors.add(author);

        Book book = new Book();
        book.setIsbn(1234);
        book.setTitle("FirstBook");
        book.setAuthors(authors);

        db.begin();
        this.bookDao.saveBook(book);
        db.commit();

        db.begin();
        Book got = this.bookDao.getBook(1234);
        assertNotNull(got);
        assertEquals("FirstBook", got.getTitle());
        assertEquals(1, got.getAuthors().size());
        Collection<Author> gotAuthors = got.getAuthors();
        assertEquals("FirstAuthor", gotAuthors.iterator().next().getName());

        Author toAdd = new Author();
        toAdd.setId(123);
        toAdd.setName("SecondAuthor");
        toAdd.setBooks(new LinkedList<Book>());

        gotAuthors.add(toAdd);
        got.setAuthors(gotAuthors);
        assertEquals(2, got.getAuthors().size());
        db.commit();

        Book updated = this.bookDao.getBook(1234);

        assertNotNull(updated);
        assertEquals("FirstBook", updated.getTitle());
        Collection<Author> updatedAuthors = updated.getAuthors();

        assertEquals(2, updatedAuthors.size());
        Iterator<Author> authorIterator = updatedAuthors.iterator();
        assertEquals("FirstAuthor", authorIterator.next().getName());
        assertEquals("SecondAuthor", authorIterator.next().getName());

        db.begin();
        this.bookDao.deleteBook(updated);
        db.commit();
        
        db.close();

    }

}
