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


import java.io.IOException;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Collection;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.lang.Math;
import java.util.Vector;
import java.util.Random;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.QueryResults;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.QueryException;
import org.exolab.castor.jdo.LockNotGrantedException;
import org.exolab.castor.jdo.TransactionAbortedException;
import org.exolab.castor.jdo.TransactionNotInProgressException;
import org.exolab.castor.jdo.ObjectModifiedException;
import org.exolab.castor.jdo.DuplicateIdentityException;
import java.util.ArrayList;

import junit.framework.TestSuite;
import junit.framework.TestCase;
import junit.framework.Assert;
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

        _db = _category.getDatabase( verbose );
    }

    public void removeRecords()
            throws PersistenceException {

        _db.begin();
        QueryResults enum;
         OQLQuery oqlquery = _db.getOQLQuery( "SELECT object FROM jdo.TestRaceNone object" );
        enum = oqlquery.execute(true);
        while (enum.hasMore()) {
            _db.remove(enum.next());
        }
        _db.commit();
    }

    public void createRecords()
            throws PersistenceException {

        _db.begin();
            for ( int i=0; i<25; i++ ) {
                TestRaceNone newTRN = new TestRaceNone();
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
        QueryResults enum;
        OQLQuery oqlquery = _db.getOQLQuery( "SELECT object FROM jdo.TestRaceNone object" );
        enum = oqlquery.execute(true);
        assertTrue("should have been able to move to 1", enum.absolute(1));
        assertTrue("should have been able to move to 5",enum.absolute(5));
        assertTrue("should have been able to move to 10",enum.absolute(10));
        assertTrue("should have been able to move to 15",enum.absolute(15));
        assertTrue("should have been able to move to 20",enum.absolute(20));
        assertTrue("should have been able to move to 25",enum.absolute(25));
        _db.commit();
    }

    /**
    test going through enumeration and calling absolute
    to simulate the .next(); call
    */
    public void testAbsoluteB()
            throws PersistenceException {

        _db.begin();
        QueryResults enum;
        OQLQuery oqlquery = _db.getOQLQuery( "SELECT object FROM jdo.TestRaceNone object" );
        enum = oqlquery.execute(true);
        int next = 1;
        boolean hasMore = true;
        while (enum.hasMore() && hasMore ==true) {
            stream.println("at: " + next);
            hasMore = enum.absolute(next);
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
        QueryResults enum;
        OQLQuery oqlquery = _db.getOQLQuery( "SELECT object FROM jdo.TestRaceNone object" );
        enum = oqlquery.execute(true);
        assertTrue("shouldn't be able to move to -50", enum.absolute(-50) == false);
        assertTrue("shouldn't be able to move to 99999",enum.absolute(99999) == false);
        _db.commit();
    }

    /**
        Should fail with a non scrollable resultset.?
    */
    public void testAbsoluteD()
            throws PersistenceException {

        try {
            _db.begin();
            QueryResults enum;
            OQLQuery oqlquery = _db.getOQLQuery( "SELECT object FROM jdo.TestRaceNone object" );
            enum = oqlquery.execute(false);
            // following should fail.
            enum.absolute(5);
            _db.commit();
            assertTrue("Shouldn't reach here, calling absolute on a non-scrollable resultset should fail",false);
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

