package org.castor.cpa.test.test2860.OneToOne;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.castor.cpa.test.framework.CPATestCase;
import org.castor.cpa.test.framework.xml.types.DatabaseEngineType;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.DuplicateIdentityException;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.TransactionAbortedException;

/**
 * Verifies the correct behaviour of cascading set to "create".
 * 
 * @author Michael Schroeder
 * @author Ivo Friedberg
 */
public class TestCreateOneToOne extends CPATestCase {
    private final static String DBNAME = "test2860";
    private final static String MAPPING = "/org/castor/cpa/test/test2860/OneToOne/mapping_create.xml";
    private Database _db;
    
    public TestCreateOneToOne(final String name) {
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
		author.setId(2);
		
		Book book = new Book();
		book.setId(1);
		book.setAuthor(author);
		
		// persist book and therefore author 
		// (because cascading=true for the relation book --> author)
		_db.begin();
		_db.create(book);
		_db.commit();
		
		// now let's see if book & author were properly commited/created
		_db.begin();
		Book db_book = _db.load(Book.class, 1);
		Author db_author = _db.load(Author.class, 2);
		_db.commit();
		
		clearTables();
		
		assertEquals(1, db_book.getId());
		assertEquals(2, db_author.getId());
		_db.close();
    }
    
    public void testCreateNewAuthorForBook() throws PersistenceException, SQLException {
		// cascading must be independent of autostore
		_db.setAutoStore(false);
		
		Author author = new Author();
		author.setId(2);
		
		Book book = new Book();
		book.setId(1);
		book.setAuthor(author);
		
		// persist book and therefore author 
		// (because cascading=true for the relation book --> author)
		_db.begin();
		_db.create(book);
		_db.commit();
		
		Author newAuthor = new Author();
		newAuthor.setId(4);
		
		// now let's see if book & author were properly commited/created
		_db.begin();
		Book db_book = _db.load(Book.class, 1);
		Author db_author = _db.load(Author.class, 2);
		db_book.setAuthor(newAuthor);
		_db.commit();
		
		_db.begin();
		Book db_book2 = _db.load(Book.class, 1);
		Author db_author2 = _db.load(Author.class, 4);
		db_author = _db.load(Author.class, 2);
		_db.commit();
		
		
		clearTables();
		
		assertEquals(1, db_book2.getId());		
		assertEquals(2, db_author.getId());
		assertEquals(4, db_author2.getId());
		_db.close();
		
	} 

    public void testCreateWithNullValue() throws PersistenceException, SQLException {
		// cascading must be independent of autostore
		_db.setAutoStore(false);
		
		Book book = new Book();
		book.setId(1);
		book.setAuthor(null);
		
		// persist book and therefore null as author
		// (because cascading=true for the relation book --> author)
		// foreign key is not null -> exception should be thrown
		try {
			_db.begin();
			_db.create(book);
			_db.commit();
			fail("Exception should have been thrown!");
		} catch(PersistenceException ex) {
			// everything ok, 'cause Exception has been thrown during create
		} finally {
		    clearTables();
		    _db.close();
		}
	}
    
    public void testCreateWithExistingId() throws PersistenceException, SQLException {
		// cascading must be independent of autostore
		_db.setAutoStore(false);
		
		Author author = new Author();
		author.setId(2);
		
		Book book = new Book();
		book.setId(1);
		book.setAuthor(author);
		
		// persist book and therefore author 
		// (because cascading=true for the relation book --> author)
		_db.begin();
		_db.create(book);
		_db.commit();
		
		Author newAuthor = new Author();
		newAuthor.setId(2);
		
		Book newBook = new Book();
		newBook.setId(3);
		newBook.setAuthor(newAuthor);
		
		// persist book and therefore a second Author with id=2
		// (because cascading=true for the relation book --> author)
		// key duplicate -> Exception should be thrown!
		try {
			_db.begin();
			_db.create(newBook);
			_db.commit();
			fail("Exception should have been thrown!");
		} catch(DuplicateIdentityException ex) {
			_db.rollback();		// illegal Insert has to be rolled back!
			// everything ok, 'cause Exception has been thrown during create
		} finally {	
		    clearTables();
		    _db.close();
		}		
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
                    "DELETE FROM test2860_oto_book");
        deleteBooks.executeUpdate();
    	deleteBooks.close();
    	
        PreparedStatement deleteAuthors =
                _db.getJdbcConnection().prepareStatement(
                        "DELETE FROM test2860_oto_author");
        deleteAuthors.executeUpdate();
        deleteAuthors.close();
    	_db.commit();
    	_db.close();
    }
}
