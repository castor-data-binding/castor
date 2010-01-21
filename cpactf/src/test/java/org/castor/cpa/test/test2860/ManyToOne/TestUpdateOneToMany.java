package org.castor.cpa.test.test2860.ManyToOne;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
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
public class TestUpdateOneToMany extends CPATestCase {
    private final static String DBNAME = "test2860";
    private final static String MAPPING = "/org/castor/cpa/test/test2860/OneToMany/mapping_update.xml";
    private Database _db;
    
    public TestUpdateOneToMany(final String name) {
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
    
    public void testUpdate_changeAuthor() throws Exception {
	// cascading must be independent of autostore
	_db.setAutoStore(false);
	
	Author author = new Author();
	author.setId(11);
	author.setName("Jack");
	
	Book book1 = new Book();
	book1.setId(12);
	book1.setName("book1");
	book1.setAuthor(author);
	
	Book book2 = new Book();
	book2.setId(13);
	book2.setName("book2");
	book2.setAuthor(author);
	
	// create objects
	_db.begin();
	_db.create(author);
	_db.create(book1);
	_db.create(book2);
	_db.commit();
	
	// load objects
	_db.begin();
	Book db_book1 = _db.load(Book.class, 12);
	_db.commit();
	
	// change author
	db_book1.getAuthor().setName("new jack");
	
	// update objects
	_db.begin();
	_db.update(db_book1);
	_db.commit();
	
	// load objects again and check
	_db.begin();
	Author db_author = _db.load(Author.class, 11);
	_db.commit();
	
	clearTables();
	_db.close();
	
	assertEquals(db_author.getName(), "new jack");	
    }
    
    public void testUpdate_setNull() throws Exception {
	// cascading must be independent of autostore
	_db.setAutoStore(false);
	
	Author author = new Author();
	author.setId(11);
	author.setName("Jack");
	
	Book book1 = new Book();
	book1.setId(12);
	book1.setName("book1");
	book1.setAuthor(author);
	
	// create objects
	_db.begin();
	_db.create(author);
	_db.create(book1);
	_db.commit();
	
	// load objects
	_db.begin();
	Book db_book1 = _db.load(Book.class, 12);
	_db.commit();
	
	// set null
	db_book1.setAuthor(null);

	try {
	    // update objects
	    _db.begin();
	    _db.update(db_book1);
	    _db.commit();
	    fail("Expected exception");
	} catch (PersistenceException e) {
	    // ok
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
