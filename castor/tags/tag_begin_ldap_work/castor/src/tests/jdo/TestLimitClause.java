/**
 * Redistribution and use of this software and associated documentation
 * ("Software"), with or without modification, are permitted provided
 * that the following conditions are met:
 *
 * 1. Redistributions of source code must retain copyright
 *    statements and notices.  Redistributions must also contain a
 *    copy of this document.
 *
 * 2. Redistributions in binary form must reproduce the
 *    above copyright notice, this list of conditions and the
 *    following disclaimer in the documentation and/or other
 *    materials provided with the distribution.
 *
 * 3. The name "Exolab" must not be used to endorse or promote
 *    products derived from this Software without prior written
 *    permission of Intalio, Inc.  For written permission,
 *    please contact info@exolab.org.
 *
 * 4. Products derived from this Software may not be called "Exolab"
 *    nor may "Exolab" appear in their names without prior written
 *    permission of Intalio, Inc. Exolab is a registered
 *    trademark of Intalio, Inc.
 *
 * 5. Due credit should be given to the Exolab Project
 *    (http://www.exolab.org/).
 *
 * THIS SOFTWARE IS PROVIDED BY INTALIO, INC. AND CONTRIBUTORS
 * ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT
 * NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL
 * INTALIO, INC. OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * Copyright 1999 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id$
 */


package jdo;

import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.QueryResults;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.engine.DatabaseImpl;
import harness.TestHarness;
import harness.CastorTestCase;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class TestLimitClause extends CastorTestCase
{

	protected static final int LIMIT = 5;
    protected JDOCategory    _category;
    private Database       _db;

    /**
     * Constructor
     *
     * @param category The test suite for these tests
     */
    public TestLimitClause( TestHarness category )
    {
        super( category, "TC62", "Test limit clause" );
        _category = (JDOCategory) category;
    }

	/**
	 * @param suite
	 * @param name
	 * @param description
	 */
	public TestLimitClause(TestHarness suite, String name, String description) {
		super(suite, name, description);
		_category = (JDOCategory) suite;
	}
	
    /**
     * Get a JDO database
     */
    public void setUp()
        throws PersistenceException, SQLException
    {

    	TestObject object = null;

        _db = _category.getDatabase();

        _db.begin();

        Connection connection = (Connection) ((DatabaseImpl) _db).getConnection();

        Statement statement = connection.createStatement();
        statement.executeUpdate ("DELETE FROM test_table");

        statement.close();

        _db.commit();

        for (int i = 1; i < 16; i++ ) {
            _db.begin();
            object = new TestObject();
            object.setId (i);
            object.setValue1("val1" + i);
            object.setValue2("val2" + i);
            _db.create (object);
            _db.commit();
        }

    }

    public void tearDown() throws PersistenceException
    {
        if ( _db.isActive() ) _db.rollback();
        _db.close();
    }

	public void testLimit()
    throws PersistenceException
    {
        TestObject testObject = null;

        _db.begin();
        OQLQuery query = _db.getOQLQuery("select t from jdo.TestObject t order by id limit $1");
        query.bind(LIMIT);
        QueryResults results = query.execute();
        assertNotNull (results);
        /*size() not available using an Oracle DB
        assertEquals (LIMIT, results.size()); */
        for (int i = 1; i <= LIMIT; i++) {
            testObject = (TestObject) results.next();
            assertEquals(i, testObject.getId());
        }
        assertTrue(!results.hasMore());

    	_db.commit();
    }

	public void runTest() throws Exception {
		testLimit();
	}

}
