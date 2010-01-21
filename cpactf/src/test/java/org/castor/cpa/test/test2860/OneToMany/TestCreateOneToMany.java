package org.castor.cpa.test.test2860.OneToMany;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Vector;

import org.castor.cpa.test.framework.CPATestCase;
import org.castor.cpa.test.framework.xml.types.DatabaseEngineType;
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
public class TestCreateOneToMany extends CPATestCase {
    private final static String DBNAME = "test2860";
    private final static String MAPPING = "/org/castor/cpa/test/test2860/OneToMany/mapping_create.xml";
    private Database _db;
    
    public TestCreateOneToMany(final String name) {
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
		Book book2 = new Book();
		book2.setId(3);
		
		Vector collection = new Vector();
		collection.add(book);
		collection.add(book2);
		
		author.setBooks(collection);
		
		// persist book and therefore author 
		// (because cascading=true for the relation book --> author)
		_db.begin();
		_db.create(author);
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
		Book book2 = new Book();
		book2.setId(3);
		
		Vector collection = new Vector();
		collection.add(book);
		collection.add(book2);
		
		author.setBooks(collection);
		
		// persist book and therefore author 
		// (because cascading=true for the relation book --> author)
		_db.begin();
		_db.create(book);
		_db.commit();
		
		Book newBook = new Book();
		newBook.setId(4);
		
		// now let's see if book & author were properly commited/created
		_db.begin();
		Author db_author = _db.load(Author.class, 1);
		collection.clear();
		collection.add(book);
		collection.add(newBook);
		db_author.setBooks(collection);
		_db.commit();
		
		_db.begin();
		db_author = _db.load(Author.class, 1);
		Book db_book = _db.load(Book.class, 2);
		Book db_newBook = _db.load(Book.class, 4);
		_db.commit();
		
		
		clearTables();
		
		assertEquals(2, db_book.getId());		
		assertEquals(1, db_author.getId());
		assertEquals(4, db_newBook.getId());
		_db.close();
		
	} 

    public void testCreateWithNullValue() throws PersistenceException, SQLException {
		// cascading must be independent of autostore
		_db.setAutoStore(false);
		
		Author author = new Author();
		author.setId(1);
		
		Book book = new Book();
		book.setId(2);
		
		Vector collection = new Vector();
		collection.add(book);
		collection.add(null);
		
		author.setBooks(collection);
		
		// should work properly (null should be ignored
		try {
			_db.begin();
			_db.create(author);
			_db.commit();
		} catch(PersistenceException ex) {
			fail("Unexpected Exception");
		}
		
		Book book2 = new Book();
		book2.setId(3);

		//should not work, cause book needs to have a value
		// in it's foreign key relation!
		try {
			_db.begin();
			_db.create(book2);
			_db.commit();
			fail("An Exception should have been throwed");
		} catch (PersistenceException ex) {
			//everything as it should be!
		}

		clearTables();
		
	}
    
    public void testCreateWithExistingId() throws PersistenceException, SQLException {
		// cascading must be independent of autostore
		_db.setAutoStore(false);
		
		Author author = new Author();
		author.setId(1);
		
		Book book = new Book();
		book.setId(2);
		
		Vector collection = new Vector();
		collection.add(book);
		author.setBooks(collection);
		
		// persist author and therefore book 
		// (because cascading=true for the relation author --> Book)
		_db.begin();
		_db.create(author);
		_db.commit();
		
		Author newAuthor = new Author();
		newAuthor.setId(3);
		
		Book newBook = new Book();
		newBook.setId(2);
		
		collection.clear();
		collection.add(newBook);
		newAuthor.setBooks(collection);
		
		// persist book and therefore a second Author with id=2
		// (because cascading=true for the relation book --> author)
		// key duplicate -> Exception should be thrown!
		try {
			_db.begin();
			_db.create(newAuthor);
			_db.commit();
			fail("Exception should have been thrown!");
		} catch(DuplicateIdentityException ex) {
			//TODO look why rollback doesn't work
			
			_db.rollback();		//illegal Insert comment has to be rolled back!
			//everything ok, cause Exception has been thrown while commit!
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
