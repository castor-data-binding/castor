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


import java.io.IOException;
import java.io.OutputStream;
import java.util.Enumeration;
import org.exolab.castor.jdo.DataObjects;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.DuplicateIdentityException;
import org.exolab.castor.jdo.QueryResults;
import org.exolab.castor.persist.OID;

import junit.framework.TestSuite;
import junit.framework.TestCase;
import junit.framework.TestResult;
import junit.framework.Assert;
import harness.TestHarness;
import harness.CastorTestCase;

/**
 * Tests for duplicate key detection. 
 * 1/ Try to create an object with an identity same as another loaded (in memory) 
 *    object.
 * 2/ Try to create an object with an identity same as another object what is not
 *    loaded by in the database.
 */
public class DuplicateKey extends CastorTestCase {

    private JDOCategory    _category;

    private Database       _db;

    public DuplicateKey( TestHarness category ) {

        super( category, "TC01", "Duplicate key detection" );
        _category = (JDOCategory) category;
    }

    /**
     * Creates data objects used by these tests
     */
    public void setUp() 
            throws PersistenceException {

        OQLQuery      oql;
        TestObject    object;
        QueryResults  enumeration;
        
        // Open transaction in order to perform JDO operations
        _db = _category.getDatabase();
        _db.begin();
        
        // Determine if test object exists, if not create it.
        // If it exists, set the name to some predefined value
        // that this test will later override.
        oql = _db.getOQLQuery( "SELECT object FROM jdo.TestObject object WHERE id = $1" );
        oql.bind( TestObject.DefaultId );
        enumeration = oql.execute();
        if ( enumeration.hasMore() ) {
            object = (TestObject) enumeration.next();
            stream.println( "Updating object: " + object );
        } else {
            object = new TestObject();
            stream.println( "Creating new object: " + object );
            _db.create( object );
        }
        _db.commit();
    }

    /**
     * Calls the individual tests embedded in this test case
     */
    public void runTest() 
            throws PersistenceException {
        testDuplicateIdentityAsInMemory();
        testDuplicateIdentityAsInDatabase();
    }

    /**
     * Try to create two objects with the same identity. This test passes 
     * if an DuplicateIdentityException is thrown when the second object 
     * with duplicated identity is created.
     */
    public void testDuplicateIdentityAsInMemory() 
            throws PersistenceException {

        TestObject    object;
        OQLQuery      oql;
        QueryResults  enumeration;

        // Attempt to create a new object with the same identity,
        // while one is in memory. Will report duplicate key from
        // the cache engine.
        _db.begin();
        oql = _db.getOQLQuery( "SELECT object FROM jdo.TestObject object WHERE id = $1" );
        oql.bind( new Integer( TestObject.DefaultId ) );
        enumeration = oql.execute();
        while ( enumeration.hasMore() )
            enumeration.next();
        
        object = new TestObject();
        stream.println( "Creating new object: " + object );
        stream.println( "Will report duplicate identity from cache engine" );
        try {
            _db.create( object );
            // expected exception
            fail("DuplicateIdentityException not thrown");
            stream.println( "Error: DuplicateIdentityException not thrown" );
        } catch ( DuplicateIdentityException except ) {
            stream.println( "OK: DuplicateIdentityException thrown" );
        } catch ( PersistenceException except ) {
            stream.println( "Error: " + except );
            throw except;
        } finally {
            _db.commit();
        }
    }

    /**
     * Try to create an object that has an identity which is the same 
     * as another object that is not loaded by in the database.  This 
     * test case passes if a DuplicateIdentityException is thrown when 
     * the object with duplicated identity is created.
     */
    public void testDuplicateIdentityAsInDatabase() 
            throws PersistenceException {

        _db.begin();
        TestObject object = new TestObject();
        stream.println( "Creating new object: " + object );
        stream.println( "Will report duplicate identity from SQL engine" );
        try {
            _db.create( object );
            //result = false;
            stream.println( "Error: DuplicateIdentityException not thrown" );
        } catch ( DuplicateIdentityException except ) {
            stream.println( "OK: DuplicateIdentityException thrown" );
        } catch ( PersistenceException except ) {
            // result = false;
            stream.println( "Error: " + except );
        } finally {
            _db.commit();
        }
    }

    /**
     * Close the database
     */
    public void tearDown() 
            throws PersistenceException {
        if ( _db.isActive() ) _db.rollback();
        _db.close();
    }
}
