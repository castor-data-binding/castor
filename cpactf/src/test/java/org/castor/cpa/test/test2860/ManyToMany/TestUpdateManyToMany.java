package org.castor.cpa.test.test2860.ManyToMany;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Vector;

import org.castor.cpa.test.framework.CPATestCase;
import org.castor.cpa.test.framework.xml.types.DatabaseEngineType;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.DuplicateIdentityException;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.TransactionAbortedException;

/**
 * @author Michael Schroeder
 * @author Ivo Friedberg
 */
public class TestUpdateManyToMany extends CPATestCase {
    private final static String DBNAME = "test2860";
    private final static String MAPPING = "/org/castor/cpa/test/test2860/ManyToMany/mapping_update.xml";
    private Database _db;
    
    public TestUpdateManyToMany(final String name) {
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

	public void testUpdate_changeA() throws Exception {
		// we want to test what works with autostore
		_db.setAutoStore(true);

		Author author = new Author();
		author.setId(2);
		author.setName("Jack");

		Vector authors = new Vector();
		authors.add(author);
		
		Book book = new Book();
		book.setId(1);
		book.setAuthors(authors);
		book.setName("Book");

		// create objects
		_db.begin();
		_db.create(author);
		_db.create(book);
		_db.commit();

		// load objects
		_db.begin();
		Book db_book = _db.load(Book.class, 1);
		_db.commit();

		// change a
		((Author)db_book.getAuthors().get(0)).setName("Joe");

		// update objects
		_db.begin();
		_db.update(db_book);
		_db.commit();

		// load objects again and check
		_db.begin();
		Book db_book2 = _db.load(Book.class, 1);
		_db.commit();

		clearTables();
		_db.close();

		assertEquals(((Author)db_book2.getAuthors().get(0)).getName(), "Joe");
	}

	/*public void testUpdate_changeA_autostoreFalse() throws Exception {
		// we want to test what works without autostore
		_db.setAutoStore(false);

		Author author = new Author();
		author.setId(2);
		author.setName("Jack");

		Book book = new Book();
		book.setId(1);
		book.setAuthor(author);
		book.setName("Book");

		// create objects
		_db.begin();
		_db.create(author);
		_db.create(book);
		_db.commit();

		// load objects
		_db.begin();
		Book db_book = _db.load(Book.class, 1);
		_db.commit();

		// change a
		db_book.getAuthor().setName("Joe");

		// update objects
		_db.begin();
		_db.update(db_book);
		_db.commit();

		// load objects again and check
		_db.begin();
		Book db_book2 = _db.load(Book.class, 1);
		_db.commit();

		clearTables();
		_db.close();

		// autstore == false --> name shouldn't have been changed
		assertEquals(db_book2.getAuthor().getName(), "Jack");
	}

	public void testUpdate_changeB() throws Exception {
		// we want to test what works with autostore
		_db.setAutoStore(true);

		Author author = new Author();
		author.setId(2);
		author.setName("Jack");

		Book book = new Book();
		book.setId(1);
		book.setAuthor(author);
		book.setName("Book");

		// create objects
		_db.begin();
		_db.create(author);
		_db.create(book);
		_db.commit();

		// load objects
		_db.begin();
		Book db_book = _db.load(Book.class, 1);
		_db.commit();

		// change b
		db_book.setName("Another Book");

		// update objects
		_db.begin();
		_db.update(db_book);
		_db.commit();

		// load objects again and check
		_db.begin();
		Book db_book2 = _db.load(Book.class, 1);
		_db.commit();

		clearTables();
		_db.close();

		assertEquals(db_book2.getName(), "Another Book");
	}

	public void testUpdate_changeB_autostoreFalse() throws Exception {
		// we want to test what works without autostore
		// this should be exactly the same as above
		_db.setAutoStore(false);

		Author author = new Author();
		author.setId(2);
		author.setName("Jack");

		Book book = new Book();
		book.setId(1);
		book.setAuthor(author);
		book.setName("Book");

		// create objects
		_db.begin();
		_db.create(author);
		_db.create(book);
		_db.commit();

		// load objects
		_db.begin();
		Book db_book = _db.load(Book.class, 1);
		_db.commit();

		// change b
		db_book.setName("Another Book");

		// update objects
		_db.begin();
		_db.update(db_book);
		_db.commit();

		// load objects again and check
		_db.begin();
		Book db_book2 = _db.load(Book.class, 1);
		_db.commit();

		clearTables();
		_db.close();

		assertEquals(db_book2.getName(), "Another Book");
	}

	public void testUpdate_newA() throws Exception {
		// we want to test what works with autostore
		_db.setAutoStore(true);

		Author author = new Author();
		author.setId(2);
		author.setName("Jack");

		Book book = new Book();
		book.setId(1);
		book.setAuthor(author);
		book.setName("Book");

		// create objects
		_db.begin();
		_db.create(author);
		_db.create(book);
		_db.commit();

		// load objects
		_db.begin();
		Book db_book = _db.load(Book.class, 1);
		_db.commit();

		// replace a
		Author author2 = new Author();
		author2.setId(3);
		author2.setName("John");

		db_book.setAuthor(author2);

		// update objects
		_db.begin();
		_db.update(db_book);
		_db.commit();

		// load objects again and check
		_db.begin();
		Book db_book2 = _db.load(Book.class, 1);
		_db.commit();

		clearTables();
		_db.close();

		assertEquals(db_book2.getAuthor().getName(), "John");
	}

	public void testUpdate_newA_autostoreFalse() throws Exception {
		// we want to test what works without autostore
		_db.setAutoStore(false);

		Author author = new Author();
		author.setId(2);
		author.setName("Jack");

		Book book = new Book();
		book.setId(1);
		book.setAuthor(author);
		book.setName("Book");

		// create objects
		_db.begin();
		_db.create(author);
		_db.create(book);
		_db.commit();

		// load objects
		_db.begin();
		Book db_book = _db.load(Book.class, 1);
		_db.commit();

		// replace a
		Author author2 = new Author();
		author2.setId(3);
		author2.setName("John");

		db_book.setAuthor(author2);

		try {
			// update objects
			_db.begin();
			_db.update(db_book);
			_db.commit();
			fail("Exception expected");
		} catch (TransactionAbortedException e) {
			// ok
		}

		clearTables();
		_db.close();
	}
	*/
	
	/**
	 * Deletes all tuples from the db.
	 * 
	 * @throws PersistenceException
	 *             if JDBC connection cannot be obtained.
	 * @throws SQLException
	 *             if execution fails.
	 */
	protected void clearTables() throws PersistenceException, SQLException {
		if (!_db.isActive())
			_db.begin();

		PreparedStatement deleteBooks = _db.getJdbcConnection()
				.prepareStatement("DELETE FROM test2860_update_book");
		deleteBooks.executeUpdate();
		deleteBooks.close();

		PreparedStatement deleteAuthors = _db.getJdbcConnection()
				.prepareStatement("DELETE FROM test2860_update_author");
		deleteAuthors.executeUpdate();
		deleteAuthors.close();
		_db.commit();
		_db.close();
	}
}
