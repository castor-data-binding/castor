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
import org.exolab.jtf.CWVerboseStream;
import org.exolab.jtf.CWTestCase;
import org.exolab.jtf.CWTestCategory;
import org.exolab.exceptions.CWClassConstructorException;
import java.util.ArrayList;


/**
 * Concurrent access test. Tests a JDO modification and concurrent
 * JDBC modification to determine if JDO can detect the modification
 * with dirty checking.
 *
 */
public class Collections extends CWTestCase {

    private Database       _db;


    private Connection     _conn;


    private JDOCategory    _category;


    public Collections( CWTestCategory category )
        throws CWClassConstructorException
    {
        super( "TC28", "Collections" );
        _category = (JDOCategory) category;
    }

    public void preExecute()
    {
        super.preExecute();
    }

    public void postExecute()
    {
        super.postExecute();
    }


    public boolean run( CWVerboseStream stream ) {
        boolean result = true;
        boolean passed = true;
        try {
            passed = runOnce( stream, TestColCollection.class );
            if ( !passed ) result = false;

            passed = runOnce( stream, TestColArrayList.class );
            if ( !passed ) result = false;

            passed = runOnce( stream, TestColVector.class );
            if ( !passed ) result = false;

            passed = runOnce( stream, TestColSet.class );
            if ( !passed ) result = false;
            /*
            passed = runOnce( stream, TestColHashSet.class );
            if ( !passed ) result = false;
            */
            
            passed = runOnce( stream, TestColMap.class );
            if ( !passed ) result = false;

            /*
            passed = runOnce( stream, TestColHashMap.class );
            if ( !passed ) result = false;
            */
            passed = runOnce( stream, TestColHashtable.class );
            if ( !passed ) result = false;

        } catch ( Exception e ) {
            return false;
        }
        return result;
    }

    public boolean runOnce( CWVerboseStream stream, Class masterClass ) {
        String masterName = masterClass.getName();

        try {

            _db = _category.getDatabase( stream.verbose() );
            _conn = _category.getJDBCConnection(); 
            _conn.setAutoCommit( false );

            stream.writeVerbose( "Running..." );
            stream.writeVerbose( "" );

            // delete everything
            _conn.createStatement().executeUpdate( "DELETE FROM test_col" );
            _conn.createStatement().executeUpdate( "DELETE FROM test_item" );
            _conn.commit();

            // create new TestCol object with elements
            _db.begin();
            TestCol testCol = (TestCol) masterClass.newInstance();
            testCol.setId( 1 );
            _db.create( testCol );
            for ( int i=0; i < 5; i++ ) {
                TestItem newItem = new TestItem( 100+i );
                testCol.addItem( newItem );
                _db.create( newItem );
            }
            _db.commit();
            // test if object created properly
            _db.begin();
            testCol = (TestCol) _db.load( masterClass, new Integer(1) );
            if ( testCol == null )
                throw new Exception( "Object creation failed!" );
                
            if ( testCol.itemSize() != 5 ||
                    !testCol.containsItem( new TestItem( 100 ) ) ||
                    !testCol.containsItem( new TestItem( 101 ) ) ||
                    !testCol.containsItem( new TestItem( 102 ) ) ||
                    !testCol.containsItem( new TestItem( 103 ) ) ||
                    !testCol.containsItem( new TestItem( 104 ) ) )
                throw new Exception( "Related objects creation failed!" );

            testCol.removeItem( new TestItem( 100 ) );
            testCol.removeItem( new TestItem( 103 ) );
            TestItem newItem = new TestItem( 106 );
            testCol.addItem( newItem );
            _db.create( newItem );
            newItem = new TestItem( 107 );
            testCol.addItem( newItem );
            _db.create( newItem );
            _db.commit();
            
            // test if add and remove work properly.
            _db.begin();
            testCol = (TestCol) _db.load( masterClass, new Integer(1) );
            if ( testCol == null )
                throw new Exception( "Object add/remove failed! " + testCol );

            if ( testCol.itemSize() != 5 ||
                    !testCol.containsItem( new TestItem( 106 ) ) ||
                    !testCol.containsItem( new TestItem( 101 ) ) ||
                    !testCol.containsItem( new TestItem( 102 ) ) ||
                    !testCol.containsItem( new TestItem( 107 ) ) ||
                    !testCol.containsItem( new TestItem( 104 ) ) )
                throw new Exception( "Related add/remove failed!" + testCol );

            // test if add and remove rollback properly.
            testCol.removeItem( new TestItem( 102 ) );
            testCol.removeItem( new TestItem( 104 ) );
            newItem = new TestItem( 108 );
            testCol.addItem( newItem );
            newItem = new TestItem( 109 );
            testCol.addItem( newItem );
            _db.create( newItem );
            _db.rollback();

            if ( testCol.itemSize() != 5 ||
                    !testCol.containsItem( new TestItem( 106 ) ) ||
                    !testCol.containsItem( new TestItem( 101 ) ) ||
                    !testCol.containsItem( new TestItem( 102 ) ) ||
                    !testCol.containsItem( new TestItem( 107 ) ) ||
                    !testCol.containsItem( new TestItem( 104 ) ) )
                throw new Exception( "Related add/remove rollback failed!" + testCol );

            // shoud test for update too
        } catch ( Exception e ) {
            e.printStackTrace();

            stream.writeVerbose( "Exception: "+ e );
            try {
                if ( _db.isActive() )
                    _db.close();
                return false;
            } catch ( Exception ex ) {
                return false;
            }
        }
        return true;
    }
}


