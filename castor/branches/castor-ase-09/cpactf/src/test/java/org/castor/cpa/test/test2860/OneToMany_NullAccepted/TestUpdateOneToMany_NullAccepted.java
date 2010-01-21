package org.castor.cpa.test.test2860.OneToMany_NullAccepted;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import org.castor.cpa.test.framework.CPATestCase;
import org.castor.cpa.test.framework.xml.types.DatabaseEngineType;
import org.castor.cpa.test.test2860.OneToMany.Author;
import org.castor.cpa.test.test2860.OneToMany.Book;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.DuplicateIdentityException;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.TransactionAbortedException;

//TODO: The OneToMany mapping seems very corrupt. tests should work after fixing this!

/**
 * Verifies the correct behaviour of cascading set to "create".
 * 
 * @author Ivo Friedberg
 */
public class TestUpdateOneToMany_NullAccepted extends CPATestCase {
    private final static String DBNAME = "test2860";
    private final static String MAPPING = "/org/castor/cpa/test/test2860/OneToMany/mapping_update.xml";
    private Database _db;
    
    public TestUpdateOneToMany_NullAccepted(final String name) {
	super(name);
    }
    
    // TODO: test other dbs
    // Test are only included/excluded for engines that have been tested with this test suite.
    public boolean include(final DatabaseEngineType engine) {
        return (engine == DatabaseEngineType.DERBY);
    }

    @Override
    public void setUp() throws Exception {
        _db = getJDOManager(DBNAME, MAPPING).getDatabase();
        _db.getCacheManager().expireCache();
    }
    
    public void testUpdate_changeBook() throws Exception {
	// cascading must be independent of autostore
	_db.setAutoStore(false);
	
	Book book1 = new Book();
	book1.setId(12);
	book1.setName("book1");
	
	Book book2 = new Book();
	book2.setId(13);
	book2.setName("book2");
		
	Author author = new Author();
	author.setId(11);
	author.setName("Jack");
	
	Collection<Book> books = Arrays.asList(book1,book2);
	author.setBooks(books);	
	
	// create objects
	_db.begin();
	_db.create(book1);
	_db.create(book2);
	_db.create(author);
	_db.commit();
	
	// load objects
	_db.begin();
	Author db_author = _db.load(Author.class, 11);
	_db.commit();
	
	// change author	
	Iterator<Book> iter = db_author.getBooks().iterator();
	while (iter.hasNext()) {
	    Book b = iter.next();
	    if (b.getId() == 12) {
		b.setName("new name for book1");
	    }	    
	}

	// update objects
	_db.begin();
	_db.update(db_author);
	_db.commit();
	
	// load objects again and check
	_db.begin();
	Author db_author2 = _db.load(Author.class, 11);
	_db.commit();
	
	clearTables();
	_db.close();
	
	iter = db_author2.getBooks().iterator();
	while (iter.hasNext()) {
	    Book b = iter.next();
	    if (b.getId() == 12) {
		assertEquals(b.getName(), "new name for book1");
	    }
	}
    }
    
    public void testUpdate_removeBook() throws Exception {
	// cascading must be independent of autostore
	_db.setAutoStore(false);
	
	Book book1 = new Book();
	book1.setId(12);
	book1.setName("book1");
	
	Book book2 = new Book();
	book2.setId(13);
	book2.setName("book2");
		
	Author author = new Author();
	author.setId(11);
	author.setName("Jack");
	
	author.setBooks(Arrays.asList(book1,book2));	
	
	// create objects
	_db.begin();
	_db.create(book1);
	_db.create(book2);
	_db.create(author);
	_db.commit();
	
	// load objects
	_db.begin();
	Author db_author = _db.load(Author.class, 11);
	_db.commit();
	
	// remove book
	db_author.setBooks(Arrays.asList(book2));
	
	// update objects
	_db.begin();
	_db.update(db_author);
	_db.commit();
	
	// load objects again and check
	_db.begin();
	Author db_author2 = _db.load(Author.class, 11);
	_db.commit();
	
	clearTables();
	_db.close();
	
	assertEquals(db_author2.getBooks().size(), 1);
	assertEquals(((Book)db_author2.getBooks().toArray()[0]).getId(), 13);
    }
    
    public void testUpdate_addBook() throws Exception {
	// cascading must be independent of autostore
	_db.setAutoStore(false);
	
	Book book1 = new Book();
	book1.setId(12);
	book1.setName("book1");
	
	Book book2 = new Book();
	book2.setId(13);
	book2.setName("book2");
		
	Author author = new Author();
	author.setId(11);
	author.setName("Jack");
	
	author.setBooks(Arrays.asList(book1,book2));	
	
	// create objects
	_db.begin();
	_db.create(book1);
	_db.create(book2);
	_db.create(author);
	_db.commit();
	
	// load objects
	_db.begin();
	Author db_author = _db.load(Author.class, 11);
	_db.commit();
	
	// add book
	Book book3 = new Book();
	book3.setId(14);
	book3.setName("book3");
	db_author.setBooks(Arrays.asList(book1,book2,book3));
	
	// update objects
	_db.begin();
	_db.update(db_author);
	_db.commit();
	
	// load objects again and check
	_db.begin();
	Author db_author2 = _db.load(Author.class, 11);
	_db.commit();
	
	clearTables();
	_db.close();
	
	assertEquals(db_author2.getBooks().size(), 3);
    }
    
    public void testUpdate_setNull() throws Exception {
	// cascading must be independent of autostore
	_db.setAutoStore(false);
	
	Book book1 = new Book();
	book1.setId(12);
	book1.setName("book1");
	
	Book book2 = new Book();
	book2.setId(13);
	book2.setName("book2");
		
	Author author = new Author();
	author.setId(11);
	author.setName("Jack");
	
	author.setBooks(Arrays.asList(book1,book2));	
	
	// create objects
	_db.begin();
	_db.create(book1);
	_db.create(book2);
	_db.create(author);
	_db.commit();
	
	// load objects
	_db.begin();
	Author db_author = _db.load(Author.class, 11);
	_db.commit();
	
	// set null
	db_author.setBooks(null);
	
	// update objects
	_db.begin();
	_db.update(db_author);
	_db.commit();
	fail("Exception expected");

	clearTables();
	_db.close();
    }
    
    /**
     * Deletes all tuples from the db.
     * 
     * @throws PersistenceException if JDBC connection cannot be obtained.
     * @throws SQLException if execution fails.
     */
    protected void clearTables() throws PersistenceException, SQLException {
        if(!_db.isActive())
        	_db.begin();
        
        PreparedStatement deleteBooks =
            _db.getJdbcConnection().prepareStatement(
                    "DELETE FROM test2860_otm_book");
        deleteBooks.executeUpdate();
    	deleteBooks.close();
    	
        PreparedStatement deleteAuthors =
                _db.getJdbcConnection().prepareStatement(
                        "DELETE FROM test2860_otm_author");
        deleteAuthors.executeUpdate();
        deleteAuthors.close();
    	_db.commit();
    	_db.close();
    }
}
