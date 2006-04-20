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
 * Copyright 2002 (C) Intalio, Inc. All Rights Reserved.
 *
 */


package jdo;


import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.QueryResults;
import org.exolab.castor.jdo.PersistenceException;

import harness.TestHarness;
import harness.CastorTestCase;


/**
 * Test for OQLResults.absolute().
 */
public class Absolute extends CastorTestCase {

    private Database       _db;

    private JDOCategory    _category;

    /**
     * Constructor
     *
     * @param category The test suite of these tests
     */
    public Absolute( TestHarness category ) {

        super( category, "TC67", "Absolute" );
        _category = (JDOCategory) category;
    }

    public void runTest()
            throws PersistenceException {

        removeRecords();
        createRecords();
        testAbsoluteA();
        testAbsoluteB();
        testAbsoluteC();
    }

    public void setUp()
            throws PersistenceException {

        _db = _category.getDatabase();
    }

    public void removeRecords()
            throws PersistenceException {

        _db.begin();
        QueryResults enumeration;
         OQLQuery oqlquery = _db.getOQLQuery( "SELECT object FROM jdo.TestObject object" );
        enumeration = oqlquery.execute(true);
        while (enumeration.hasMore()) {
            _db.remove(enumeration.next());
        }
        _db.commit();
    }

    public void createRecords()
            throws PersistenceException {

        _db.begin();
            for ( int i=0; i<25; i++ ) {
                TestObject newTRN = new TestObject();
                newTRN.setId(i);
                _db.create( newTRN );
            }
        _db.commit();
    }

    /**
    Very simple test to do a query and call .absolute(x)
    */
    public void testAbsoluteA()
            throws PersistenceException {

        _db.begin();
        QueryResults enumeration;
        OQLQuery oqlquery = _db.getOQLQuery( "SELECT object FROM jdo.TestObject object" );
        enumeration = oqlquery.execute(true);
        assertTrue("should have been able to move to 1", enumeration.absolute(1));
        assertTrue("should have been able to move to 5",enumeration.absolute(5));
        assertTrue("should have been able to move to 10",enumeration.absolute(10));
        assertTrue("should have been able to move to 15",enumeration.absolute(15));
        assertTrue("should have been able to move to 20",enumeration.absolute(20));
        assertTrue("should have been able to move to 25",enumeration.absolute(25));
        _db.commit();
    }

    /**
    test going through enumeration and calling absolute
    to simulate the .next(); call
    */
    public void testAbsoluteB()
            throws PersistenceException {

        _db.begin();
        QueryResults enumeration;
        OQLQuery oqlquery = _db.getOQLQuery( "SELECT object FROM jdo.TestObject object" );
        enumeration = oqlquery.execute(true);
        int next = 1;
        boolean hasMore = true;
        while (enumeration.hasMore() && hasMore ==true) {
            stream.println("at: " + next);
            hasMore = enumeration.absolute(next);
            next++;
        }
        _db.commit();
    }

    /**
        both should fail as -50 is less than 0 and 99999 is greater than the 25 objects in the db
    */
    public void testAbsoluteC()
            throws PersistenceException {

        _db.begin();
        QueryResults enumeration;
        OQLQuery oqlquery = _db.getOQLQuery( "SELECT object FROM jdo.TestObject object" );
        enumeration = oqlquery.execute(true);
        assertFalse("shouldn't be able to move to -50", enumeration.absolute(-50));
        assertFalse("shouldn't be able to move to 99999",enumeration.absolute(99999));
        _db.commit();
    }

    /**
        Should fail with a non scrollable resultset.?
    */
    public void testAbsoluteD() {

        try {
            _db.begin();
            QueryResults enumeration;
            OQLQuery oqlquery = _db.getOQLQuery( "SELECT object FROM jdo.TestObject object" );
            enumeration = oqlquery.execute(false);
            // following should fail.
            enumeration.absolute(5);
            _db.commit();
            fail ("Shouldn't reach here, calling absolute on a non-scrollable resultset should fail");
        } catch (Exception e) {
            assertTrue(true);
        }
    }

    public void tearDown()
            throws PersistenceException {

        if ( _db.isActive() ) _db.rollback();
        _db.close();
    }
}
