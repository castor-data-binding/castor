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
 * Test for many-to-many relationship. A many to many relationship
 * is stored in a relational database as a separated table.
 */
public class Size extends CastorTestCase 
{

    private Database       _db;

    private JDOCategory    _category;

    /**
     * Constructor
     *
     * @param category The test suite of these tests
     */
    public Size( TestHarness category ) 
    {
        super( category, "TC66", "Size" );
        _category = (JDOCategory) category;
    }

    public void runTest() throws PersistenceException 
    {
        removeRecords();
        createRecords();
        testSizeA();
        testSizeB();
        testSizeC();
        testSizeD();
    }

    public void setUp() throws PersistenceException 
    {
        _db = _category.getDatabase( verbose );
    }
    public void removeRecords() throws PersistenceException 
    {
        _db.begin();
        QueryResults enum;
        OQLQuery oqlquery = _db.getOQLQuery( "SELECT object FROM jdo.TestRaceNone object" );
        enum = oqlquery.execute(true);
        while (enum.hasMore())
        {
            _db.remove(enum.next());
        }
        _db.commit();

    }
    public void createRecords() throws PersistenceException 
    {
        _db.begin();
        for ( int i=0; i<25; i++ ) 
        {
            TestRaceNone newTRN = new TestRaceNone();
            newTRN.setId(i);
            _db.create( newTRN );
        }
        _db.commit();       
    }

    /**
     * Very simple test to do a query and call .size()
     */
    public void testSizeA() throws PersistenceException 
    {
        _db.begin();
        QueryResults enum;
         OQLQuery oqlquery = _db.getOQLQuery( "SELECT object FROM jdo.TestRaceNone object" );
        enum = oqlquery.execute(true);
        assert("size should be > 0",enum.size() > 0);
        _db.commit();
    }

    /**
     * Test going through enumeration and calling size. This tests the 
     * implemention because it internally moves the cursor and then 
     * moves it back.
     */
    public void testSizeB() throws PersistenceException 
    {
        _db.begin();
        QueryResults enum;
        OQLQuery oqlquery = _db.getOQLQuery( "SELECT object FROM jdo.TestRaceNone object" );
        enum = oqlquery.execute(true);
        while (enum.hasMore())
        {
            enum.next();
            assert("size should be > 0", enum.size() > 0);
            assert("size should be ==25", enum.size() == 25);
        }
        _db.commit();
    }
    
    /**
     * Does size return the right results?
     */
    public void testSizeC() throws PersistenceException 
    {
        _db.begin();
        QueryResults enum;
        OQLQuery oqlquery = _db.getOQLQuery( "SELECT object FROM jdo.TestRaceNone object" );
        enum = oqlquery.execute(true);
        int expectedSize = enum.size();
        int realSize = 0;
        while (enum.hasMore())
        {
            enum.next();
            realSize ++;
        }
        _db.commit();
        assert("realsize didn't equal expectedsize", realSize==expectedSize);
    }

    /**
     * Should fail with a non scrollable resultset.
     */
    public void testSizeD() throws PersistenceException 
    {
        try
        {
            _db.begin();
            QueryResults enum;
            OQLQuery oqlquery = _db.getOQLQuery( "SELECT object FROM jdo.TestRaceNone object" );
            enum = oqlquery.execute(false);
            // following should fail.
            int expectedSize = enum.size();
            _db.commit();
            // This test fails when executed against PostgreSQL. 
            assert("Calling size() on a non-scrollable ResultSet should fail (unless using PostgreSQL).",false);
        }
        catch (Exception e)
        {
            assert(true);
        }
    
    }    
    
    public void tearDown() throws PersistenceException 
    {
        if ( _db.isActive() ) _db.rollback();
        _db.close();
    }
}

