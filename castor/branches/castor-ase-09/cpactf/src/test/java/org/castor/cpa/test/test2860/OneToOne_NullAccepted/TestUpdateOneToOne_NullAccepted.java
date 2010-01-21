package org.castor.cpa.test.test2860.OneToOne_NullAccepted;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.castor.cpa.test.framework.CPATestCase;
import org.castor.cpa.test.framework.xml.types.DatabaseEngineType;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.DuplicateIdentityException;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.TransactionAbortedException;

/**
 * Verifies the correct behaviour of cascading set to "create" with a schema
 * that allows null values.
 * 
 * @author Michael Schroeder
 * @author Ivo Friedberg
 */
public class TestUpdateOneToOne_NullAccepted extends CPATestCase {
    private final static String DBNAME = "test2860";
    private final static String MAPPING = "/org/castor/cpa/test/test2860/OneToOne_NullAccepted/mapping_update.xml";
    private Database _db;

    public TestUpdateOneToOne_NullAccepted(final String name) {
	super(name);
    }

    // TODO: test other dbs
    // Test are only included/excluded for engines that have been tested with
    // this test suite.
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
	author.setId(12);
	author.setName("Jack");
	
	Book book = new Book();
	book.setId(11);
	book.setAuthor(author);
	book.setName("Book");
	
	// create objects
	_db.begin();
	_db.create(author);
	_db.create(book);
	_db.commit();
	
	// load objects
	_db.begin();
	Book db_book = _db.load(Book.class, 11);
	_db.commit();
	
	// change author	
	db_book.getAuthor().setName("Joe");
	
	// update objects
	_db.begin();
	_db.update(db_book);
	_db.commit();
	
	// load objects again and check
	_db.begin();
	Book db_book2 = _db.load(Book.class, 11);
	_db.commit();
	
	clearTables();
	_db.close();
	
	assertEquals(db_book2.getAuthor().getName(), "Joe");
    }
        
    public void testUpdate_newAuthor() throws Exception {
	// cascading must be independent of autostore
	_db.setAutoStore(false);
	
	Author author = new Author();
	author.setId(12);
	author.setName("Jack");
	
	Book book = new Book();
	book.setId(11);
	book.setAuthor(author);
	book.setName("Book");
	
	// create objects
	_db.begin();
	_db.create(author);
	_db.create(book);
	_db.commit();
	
	// load objects
	_db.begin();
	Book db_book = _db.load(Book.class, 11);
	_db.commit();
	
	// replace author
	Author author2 = new Author();
	author2.setId(13);
	author2.setName("John");

	db_book.setAuthor(author2);
	
	// update objects
	_db.begin();
	_db.update(db_book);
	_db.commit();
	
	// load objects again and check
	_db.begin();
	Book db_book2 = _db.load(Book.class, 11);
	_db.commit();
		
	clearTables();
	_db.close();
	
	assertEquals(db_book2.getAuthor().getName(), "John");
    }
    
    public void testUpdate_withNullValue() throws Exception {
	// cascading must be independent of autostore
	_db.setAutoStore(false);
	
	Author author = new Author();
	author.setId(12);
	author.setName("Jack");
	
	Book book = new Book();
	book.setId(11);
	book.setAuthor(author);
	book.setName("Book");
	
	// create objects
	_db.begin();
	_db.create(author);
	_db.create(book);
	_db.commit();
	
	// load objects
	_db.begin();
	Book db_book = _db.load(Book.class, 11);
	_db.commit();
	
	// replace author with null
	db_book.setAuthor(null);
	
	// there shouldn't be any exception, the schema allows null
	// update objects
	_db.begin();
	_db.update(db_book);
	_db.commit();

	clearTables();
	_db.close();

    }

    /**
     * Deletes all tuples from the db.
     * 
     * @throws PersistenceException
     *             if JDBC connection cannot be obtained.
     * @throws SQLException
     *             if execution fails.
     */
    protected void clearTables() throws PersistenceException, SQLException {
	if (!_db.isActive()) _db.begin();

	PreparedStatement deleteBooks = _db.getJdbcConnection().prepareStatement("DELETE FROM test2860_oto_null_book");
	deleteBooks.executeUpdate();
	deleteBooks.close();

	PreparedStatement deleteAuthors = _db.getJdbcConnection().prepareStatement(
		"DELETE FROM test2860_oto_null_author");
	deleteAuthors.executeUpdate();
	deleteAuthors.close();
	_db.commit();
	_db.close();
    }
}
