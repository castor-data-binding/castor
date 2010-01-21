package org.castor.cpa.test.test2860.ManyToMany;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Vector;

import org.castor.cpa.test.framework.CPATestCase;
import org.castor.cpa.test.framework.xml.types.DatabaseEngineType;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.DuplicateIdentityException;
import org.exolab.castor.jdo.PersistenceException;


/**
 * Verifies the correct behaviour of cascading set to "create".
 * 
 * @author Ivo Friedberg
 */
public class TestCreateManyToMany extends CPATestCase {
    private final static String DBNAME = "test2860";
    private final static String MAPPING = "/org/castor/cpa/test/test2860/ManyToMany/mapping_create.xml";
    private Database _db;
    
    public TestCreateManyToMany(final String name) {
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
		//TODO implement cascading for M:N.. beofre that this test
    	//is here to look what of M:N relations is implemented using autostore
		_db.setAutoStore(true);
		
		Author author = new Author();
		author.setId(1);
		author.setName("Hans Maier");
		
		Vector authors = new Vector();
		authors.add(author);
		
		Book book = new Book();
		book.setId(3);
		book.setName("Buch1");
		book.setAuthors(authors);
		
		_db.begin();
		_db.create(book);
		_db.commit();
		
		Author author2 = new Author();
		author.setId(2);
		author.setName("Hans Huber");
		
		authors.add(author2);
		Book book2 = new Book();
		book2.setId(4);
		book2.setName("Buch2");
		book2.setAuthors(authors);
		
		
		
		// persist books and therefore authors
		// first book only persists author 1; scnd should then put author
		// two also in persistent storage

		_db.begin();
		_db.create(book2);
		_db.commit();
		
		// now let's see if book & author were properly commited/created
		_db.begin();
		Author db_author = _db.load(Author.class, 1);
		Author db_author2 = _db.load(Author.class, 2);
		Book db_book = _db.load(Book.class, 3);
		Book db_book2 = _db.load(Book.class, 4);
		_db.commit();
		
		clearTables();
		assertEquals(1, db_author.getId());
		assertEquals(2, db_author2.getId());
		assertEquals(3, db_book.getId());
		assertEquals(1, db_book.getAuthors().size());
		assertEquals(4, db_book2.getId());
		assertEquals(2, db_book2.getAuthors().size());
		_db.close();
    }
    
    /*public void testCreateNewAuthorForBook() throws PersistenceException, SQLException {
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
		
		//should not work cause null is not allowed
		try {
			_db.begin();
			_db.create(author);
			_db.commit();
			fail("An exception should have been throwed!");
		} catch(PersistenceException ex) {
			//everything ok
		}

		clearTables();
		
	}
    */
    
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
                    "DELETE FROM test2860_mto_book");
        deleteBooks.executeUpdate();
    	deleteBooks.close();
    	
        PreparedStatement deleteAuthors =
                _db.getJdbcConnection().prepareStatement(
                        "DELETE FROM test2860_mto_author");
        deleteAuthors.executeUpdate();
        deleteAuthors.close();
    	_db.commit();
    	_db.close();
    }
}
