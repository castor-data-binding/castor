package org.castor.cpa.test.test2860.ManyToMany_NullAccepted;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Vector;

import org.castor.cpa.test.framework.CPATestCase;
import org.castor.cpa.test.framework.xml.types.DatabaseEngineType;
import org.castor.cpa.test.test2860.ManyToOne.Author;
import org.castor.cpa.test.test2860.ManyToOne.Book;
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
public class TestCreateOneToMany_NullAccepted extends CPATestCase {
    private final static String DBNAME = "test2860";
    private final static String MAPPING = "/org/castor/cpa/test/test2860/ManyToOne_NullAccepted/mapping_create.xml";
    private Database _db;
    
    public TestCreateOneToMany_NullAccepted(final String name) {
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
    
    public void testCreate() throws PersistenceException, SQLException {
		// cascading must be independent of autostore
		_db.setAutoStore(false);
		
		Author author = new Author();
		author.setId(1);
		
		Book book = new Book();
		book.setId(2);
		book.setAuthor(author);
		Book book2 = new Book();
		book2.setId(3);
		book.setAuthor(author);
		
		
		
		// persist book and therefore author 
		// (because cascading=true for the relation book --> author)
		_db.begin();
		_db.create(book);
		_db.create(book2);
		_db.commit();
		
		// now let's see if book & author were properly commited/created
		_db.begin();
		Author db_author = _db.load(Author.class, 1);
		Book db_book = _db.load(Book.class, 2);
		Book db_book2 = _db.load(Book.class, 3);
		_db.commit();
		
		clearTables();
		
		assertEquals(1, db_author.getId());
		assertEquals(2, db_book.getId());
		assertEquals(3, db_book2.getId());
		_db.close();
    }
    
    public void testCreateNewAuthorForBook() throws PersistenceException, SQLException {
		// cascading must be independent of autostore
		_db.setAutoStore(false);
		
		Author author = new Author();
		author.setId(1);
		
		Book book = new Book();
		book.setId(2);
		book.setAuthor(author);
		Book book2 = new Book();
		book2.setId(3);
		book.setAuthor(author);
		
		
		// persist book and therefore author 
		// (because cascading=true for the relation book --> author)
		_db.begin();
		_db.create(book);
		_db.create(book2);
		_db.commit();
		
		Author newAuthor = new Author();
		newAuthor.setId(4);
		
		// now let's see if book & author were properly commited/created
		_db.begin();
		Book db_book = _db.load(Book.class, 2);
		db_book.setAuthor(newAuthor);
		_db.commit();
		
		_db.begin();
		db_book = _db.load(Book.class, 2);
		Book db_book2 = _db.load(Book.class, 3);
		Author db_author = _db.load(Author.class, 1);
		Author db_newAuthor = _db.load(Author.class, 4);
		_db.commit();
		
		
		clearTables();
		
		assertEquals(2, db_book.getId());		
		assertEquals(3, db_book2.getId());		
		assertEquals(1, db_author.getId());
		assertEquals(4, db_newAuthor.getId());
		_db.close();
		
	} 

    public void testCreateWithNullValue() throws PersistenceException, SQLException {
		// cascading must be independent of autostore
		_db.setAutoStore(false);
		
		Author author = new Author();
		author.setId(1);
		
		Book book = new Book();
		book.setId(2);
		book.setAuthor(null);
		
		//should work cause null is allowed
		try {
			_db.begin();
			_db.create(author);
			_db.commit();
			
		} catch(PersistenceException ex) {
			fail("An exception has been throwed!");
		}

		clearTables();
		
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
                    "DELETE FROM test2860_mto_null_book");
        deleteBooks.executeUpdate();
    	deleteBooks.close();
    	
        PreparedStatement deleteAuthors =
                _db.getJdbcConnection().prepareStatement(
                        "DELETE FROM test2860_mto_null_author");
        deleteAuthors.executeUpdate();
        deleteAuthors.close();
    	_db.commit();
    	_db.close();
    }
}
