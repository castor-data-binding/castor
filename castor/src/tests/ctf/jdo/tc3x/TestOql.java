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

package ctf.jdo.tc3x;

import harness.CastorTestCase;
import harness.TestHarness;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Statement;

import jdo.JDOCategory;

import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.QueryResults;

/**
 * A suite of general tests for the Castor JDO OQL engine. This test case may
 * grow out of control trying to cover all the supported QL syntax, 
 * especially in the new impl. If/when this happens, we'll just split it up 
 * into multiple test cases. 
 *
 * @author <a href="mailto:ferret AT frii DOT com">Bruce Snyder</a>
 */
public final class TestOql extends CastorTestCase {
    private static final int MIN_ID = 10;
    private static final int MAX_ID = 29;
    private static final int MIN_EXTENDS_ID = 30;
    private static final int MAX_EXTENDS_ID = 49;

    private JDOCategory    _category;
    private Database       _db;

    public TestOql(final TestHarness category) {
        super(category, "TC30", "OQL-supported syntax");
        _category = (JDOCategory) category;
    }

    public void setUp() throws PersistenceException {
        _db = _category.getDatabase();
    }

    public void runTest() throws Exception {
        // Populate the database here rather than in setUp(). The setUp()
        // method is run once before each test (and tearDown() is run once
        // after each test and I don't think that we want to populate/truncate
        // the database for each test. We're just selecting the data we're not
        // manipulating it. 
        populateDatabase();
        testBasicSelect();
        
        populateDatabaseExtends();
        testSelectWithFunctions();
    }

    /*
     * This method will truncate everything from the database and then
     * repopulate it. It needs to be generic enough to work across databases
     * so I would prefer to use straight JDBC calls. 
     */
    public void populateDatabase() throws Exception {
        _db.begin();
        Connection connection = _db.getJdbcConnection();
        Statement statement = connection.createStatement();
        statement.execute("delete from tc3x_extends");

        connection = _db.getJdbcConnection();
        statement = connection.createStatement();
        statement.execute("delete from tc3x_entity");
        _db.commit();
        
        _db.begin();
        for (int i = MIN_ID; i <= MAX_ID; ++i) {
            Entity obj = new Entity();
            obj.setId(i);
            obj.setValue1(Entity.DEFAULT_VALUE_1 + " " + Integer.toString(i));
            _db.create(obj);
        }
        _db.commit();
    }

    /*
     * This method will truncate everything from the database and then
     * repopulate it. It needs to be generic enough to work across databases
     * so I would prefer to use straight JDBC calls. 
     */
    public void populateDatabaseExtends() throws Exception {
        _db.begin();
        Connection connection = _db.getJdbcConnection();
        Statement statement = connection.createStatement();
        statement.execute("delete from tc3x_extends");

        connection = _db.getJdbcConnection();
        statement = connection.createStatement();
        statement.execute("delete from tc3x_entity");
        _db.commit();
        
        _db.begin();
        for (int i = MIN_ID; i <= MAX_ID; ++i) {
            Entity obj = new Entity();
            obj.setId(i);
            obj.setValue1(Entity.DEFAULT_VALUE_1 + " " + Integer.toString(i));
            _db.create(obj);
        }
        _db.commit();
        
        _db.begin();
        for (int i = MIN_EXTENDS_ID; i <= MAX_EXTENDS_ID; ++i) {
            ExtendsEntity ext = new ExtendsEntity();
            ext.setId(i);
            ext.setValue1(Entity.DEFAULT_VALUE_1 + " " + Integer.toString(i));
            _db.create(ext);
        }
        _db.commit();
    }

    /*
     * Test many different variations of the basic SELECT statement.
     */
    public void testBasicSelect() throws PersistenceException {
        OQLQuery query;

        _db.begin();

        // fetch all available data
        query = _db.getOQLQuery(
                "select x from " + Entity.class.getName() + " x");
        tryQuery(query, MAX_ID - MIN_ID + 1);

        // query only one object, expecting one
        assertTrue("internal error: MIN_ID<=15 && MAX_ID>=15",
                (MIN_ID <= 15) && (MAX_ID >= 15));
        query = _db.getOQLQuery(
                "select x from " + Entity.class.getName() + " x where id=15");
        assertTrue("internal error", MIN_ID > 1);
        tryQuery(query, 1);

        // query only one object, expecting none
        assertTrue("internal error: MIN_ID>1", MIN_ID > 1);
        query = _db.getOQLQuery(
                "select x from " + Entity.class.getName() + " x where id=1");
        tryQuery(query, 0);

        // query using bind variable parameter, find one object
        query = _db.getOQLQuery(
                "select x from " + Entity.class.getName() + " x where id=$1");
        query.bind(MIN_ID);
        tryQuery(query, 1);

        // query using bind variable parameter, find nothing
        query = _db.getOQLQuery(
                "select x from " + Entity.class.getName() + " x where id=$1");
        query.bind(MIN_ID - 1);
        tryQuery(query, 0);

        // query using comparison between bind variable parameter and constant,
        // find all objects
        query = _db.getOQLQuery(
                "select x from " + Entity.class.getName() + " x where $(int)1 = 1000");
        query.bind(1000);
        tryQuery(query, MAX_ID + 1 - MIN_ID);

        // query using comparison between bind variable parameter and constant,
        // find no objects
        query = _db.getOQLQuery(
                "select x from " + Entity.class.getName() + " x where $(int)1 = 1000");
        query.bind(2000);
        tryQuery(query, 0);


        // query using 1 bind variable parameter, find all but the first and last object
        query = _db.getOQLQuery(
                "select x from " + Entity.class.getName() + " x where id>$1 and id<$2");
        query.bind(MIN_ID);
        query.bind(MAX_ID);
        tryQuery(query, MAX_ID + 1 - MIN_ID - 2);

        // query using 2 bind variable parameters, find all but the first and last object
        query = _db.getOQLQuery(
                "select x from " + Entity.class.getName() + " x where id<$2 and id>$1");
        query.bind(MIN_ID);
        query.bind(MAX_ID);
        tryQuery(query, MAX_ID + 1 - MIN_ID - 2);

        // query using "BETWEEN" operator, finding all records
        query = _db.getOQLQuery(
                "select x from " + Entity.class.getName()
                + " x where id between $1 and $2");
        query.bind(MIN_ID);
        query.bind(MAX_ID);
        tryQuery(query, MAX_ID + 1 - MIN_ID);

        // query using "BETWEEN" operator, finding no records
        query = _db.getOQLQuery(
                "select x from " + Entity.class.getName()
                + " x where id between $2 and $1");
        query.bind(MIN_ID);
        query.bind(MAX_ID);
        tryQuery(query, 0);

        // query using string constants containing a question mark in the WHERE clause,
        // finding all records
        query = _db.getOQLQuery(
                "select x from " + Entity.class.getName()
                + " x where \"abc123?הצü\" = \"abc123?הצü\"");
        tryQuery(query, MAX_ID + 1 - MIN_ID);

        // query using string constants containing a question mark in the WHERE clause,
        // finding no records
        query = _db.getOQLQuery(
                "select x from " + Entity.class.getName()
                + " x where \"abc\" = \"?123\"");
        tryQuery(query, 0);

        // query using "IN" operator
        assertTrue("internal error: MIN_ID<=15 && MAX_ID>=18",
                (MIN_ID <= 15) && (MAX_ID >= 15));
        assertTrue("internal error: MIN_ID>5", MIN_ID > 5);
        query = _db.getOQLQuery(
                "select x from " + Entity.class.getName()
                + " x where id in list(5, 15, 18)");
        tryQuery(query, 2);

        // query using "IN" operator and bind variables, find all objects
        query = _db.getOQLQuery(
                "select x from " + Entity.class.getName()
                + " x where id in list($1, $2, $3)");
        query.bind(MIN_ID);
        query.bind((MIN_ID + MAX_ID) / 2);
        query.bind(MAX_ID);
        tryQuery(query, 3);

        // query using "IN" operator and bind variables, find some objects
        query = _db.getOQLQuery(
                "select x from " + Entity.class.getName()
                + " x where id in list($1, $2)");
        query.bind(MIN_ID);
        query.bind(MAX_ID + 5);
        tryQuery(query, 1);

        // query using "IN" operator and string values, find one object
        query = _db.getOQLQuery(
                "select x from " + Entity.class.getName()
                + " x where value1 in list(\"XXX\", \"one 21\", 'A')");
        tryQuery(query, 1);

        _db.commit();
    }

    /*
     * test received result set
     */
    public void tryQuery(final OQLQuery query, final int countExpected)
    throws PersistenceException {
        QueryResults res = query.execute();
        int count = 0;

        try {
            while (res.hasMore()) {
                Entity obj = (Entity) res.next();

                String val = Entity.DEFAULT_VALUE_1 + " " + Integer.toString(obj.getId());
                assertEquals("value1", val, obj.getValue1());
                assertEquals("value2", Entity.DEFAULT_VALUE_2, obj.getValue2());

                ++count;
            }
        } finally {
            res.close();
        }

        assertEquals("number of objects found", countExpected, count);
    }

    /*
     * test received result set
     */
    public void tryFunctionQuery(final OQLQuery query, final int countExpected)
    throws PersistenceException {
        QueryResults res = query.execute();
        long functionValue = 0;

        try {
            if (res.hasMore()) {
                Object obj = res.next();
                if (obj instanceof Long) {
                    functionValue = ((Long) obj).longValue();
                } else if (obj instanceof BigDecimal) {
                    functionValue = ((BigDecimal) obj).longValue();
                } else if (obj instanceof Integer) {
                    functionValue = ((Integer) obj).longValue();
                }
            }
        } finally {
            res.close();
        }

        assertEquals("number of objects found", countExpected, functionValue);
    }

    /*
     * Test SQL functions (e.g. count(), max(), first(), last(), avg(), etc.).
     */
    public void testSelectWithFunctions() throws PersistenceException {
        _db.begin();

         // obtain number of TestObject instances
        OQLQuery query = _db.getOQLQuery(
                "SELECT count(x.id) FROM " + Entity.class.getName() + " x");
        tryFunctionQuery(query, 40);

        // obtain distinct number of TestObject instances
        query = _db.getOQLQuery(
                "SELECT count(distinct x.id) FROM " + Entity.class.getName() + " x");
        tryFunctionQuery(query, 40);

        // obtain number of TestObjectExtends instances
        query = _db.getOQLQuery(
                "SELECT count(x.id) FROM " + ExtendsEntity.class.getName() + " x");
        tryFunctionQuery(query, 20);
    }

    public void tearDown() throws PersistenceException {
        if (_db.isActive()) { _db.rollback(); }
        _db.close();
    }
}
