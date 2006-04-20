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

import org.exolab.castor.jdo.DataObjects;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.QueryResults;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.DuplicateIdentityException;
import org.exolab.castor.jdo.TransactionAbortedException;
import org.exolab.castor.jdo.ObjectModifiedException;

import junit.framework.TestSuite;
import junit.framework.TestCase;
import junit.framework.Assert;
import harness.TestHarness;
import harness.CastorTestCase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DependentOrder extends CastorTestCase 
{

    private JDOCategory    _category;

    private Database       _db;

    private Connection     _conn;

    /**
     * Constructor
     *
     * @param category The test suite for these tests
     */
    public DependentOrder( TestHarness category ) 
    {
        super( category, "TC31", "Dependent object order using key-gen" );
        _category = (JDOCategory) category;
    }

    /**
     * Get a JDO database
     */
    public void setUp() 
        throws PersistenceException, SQLException
    {
        _db = _category.getDatabase();
        _conn = _category.getJDBCConnection();
        _conn.setAutoCommit( false );

        stream.println( "Delete everything" );
        Statement stmt = _conn.createStatement();
        stmt.executeUpdate( "delete from depend2" );
        stmt.executeUpdate( "delete from master" );
        stmt.executeUpdate( "delete from depend1" );
        _conn.commit();

    }

    public void runTest() 
        throws PersistenceException, SQLException
    {
        _db.begin();

        stream.println( "Build master object and its dependent objects" );

        // no ids needed, they come from the key-gen
        Master master = new Master();
        Depend1 depend1 = new Depend1();
        master.setDepend1(depend1);
        Depend2 depend2 = new Depend2();
        master.addDepend2(depend2);

        stream.println( "Create object tree in db" );
        _db.create(master);
        _db.commit();
        stream.println( "depend1_id after creation : " + master.getDepend1().getId() );

        _db.begin();
        try 
        {
            stream.println( "read depend1_id from db" );
            PreparedStatement pstmt =
                _conn.prepareStatement( "select depend1_id from master where id=?" );
            stream.println( "master id: " + master.getId() );
            pstmt.setInt( 1, master.getId() );
            pstmt.execute();
            ResultSet result = pstmt.getResultSet();
            if (!result.next()) 
            {
                fail("Master object not created");
            }

            stream.println( "depend1_id in db : " + result.getInt( "depend1_id" ) );

            if ( result.getInt( "depend1_id" ) == 0 ) 
            {
                fail( "Depend1 object not linked to Master object" );
            }
        } 
        catch ( SQLException e ) 
        {
            fail( "Exception when checking master object row: " + e );
        }

        _db.commit();

				// test for bug 973 Dependent objects deletion order problem
        try 
        {
            stream.println( "Deleting master object" );
            _db.begin();
            master = ( Master ) _db.load( Master.class, new Integer( master.getId() ) );
            _db.remove( master );
            _db.commit();
        } 
        catch ( Exception e ) 
        {
            fail( "Exception thrown " + e );
        }

    }

    public void tearDown() throws PersistenceException 
    {
        if ( _db.isActive() ) _db.rollback();
        _db.close();
    }
}
