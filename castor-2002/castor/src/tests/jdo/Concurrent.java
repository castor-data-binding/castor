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
import java.sql.Connection;
import java.sql.SQLException;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.ObjectModifiedException;
import org.exolab.jtf.CWVerboseStream;
import org.exolab.jtf.CWTestCase;
import org.exolab.jtf.CWTestCategory;
import org.exolab.exceptions.CWClassConstructorException;


/**
 * Concurrent access test. Tests a JDO modification and concurrent
 * JDBC modification to determine if JDO can detect the modification
 * with dirty checking.
 */
public class Concurrent
    extends CWTestCase
{


    private Database       _db;


    private Connection     _conn;


    private JDOCategory    _category;


    static final String    JDBCValue = "jdbc value";


    static final String    JDOValue = "jdo value";


    public Concurrent( CWTestCategory category )
        throws CWClassConstructorException
    {
        super( "TC01", "Concurrent access" );
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


    public boolean run( CWVerboseStream stream )
    {
        boolean result = true;

        try {
            _db = _category.getDatabase( stream.verbose() );
            _conn = _category.getJDBCConnection(); 

            stream.writeVerbose( "Running in access mode shared" );
            if ( ! runOnce( stream, Database.Shared ) )
                result = false;
            stream.writeVerbose( "" );
            stream.writeVerbose( "Running in access mode exclusive" );
            if ( ! runOnce( stream, Database.Exclusive ) )
                result = false;
            stream.writeVerbose( "" );
            stream.writeVerbose( "Running in access mode db-locked" );
            if ( ! runOnce( stream, Database.DbLocked ) )
                result = false;
            stream.writeVerbose( "" );

            _db.close();
            _conn.close();
        } catch ( Exception except ) {
            stream.writeVerbose( "Error: " + except );
            except.printStackTrace();
            result = false;
        }
        return result;
    }


    private boolean runOnce( CWVerboseStream stream, short accessMode )
    {
        boolean result = true;

        try {
            OQLQuery      oql;
            TestObject    object;
            Enumeration   enum;

            // Open transaction in order to perform JDO operations
            _db.begin();
        
            // Determine if test object exists, if not create it.
            // If it exists, set the name to some predefined value
            // that this test will later override.
            oql = _db.getOQLQuery( "SELECT object FROM jdo.TestObject object WHERE id = $1" );
            oql.bind( TestObject.DefaultId );
            enum = oql.execute();
            if ( enum.hasMoreElements() ) {
                object = (TestObject) enum.nextElement();
                stream.writeVerbose( "Retrieved object: " + object );
                object.setValue1( TestObject.DefaultValue1 );
                object.setValue2( TestObject.DefaultValue2 );
            } else {
                object = new TestObject();
                stream.writeVerbose( "Creating new object: " + object );
                _db.create( object );
            }
            _db.commit();
            
            
            // Open a new transaction in order to conduct test
            _db.begin();
            oql.bind( new Integer( TestObject.DefaultId ) );
            object = (TestObject) oql.execute(  accessMode ).nextElement();
            object.setValue1( JDOValue );
            
            // Perform direct JDBC access and override the value of that table
            _conn.setAutoCommit( false );
            _conn.createStatement().execute( "UPDATE test_table SET value1='" + JDBCValue +
                                             "' WHERE id=" + TestObject.DefaultId );
            _conn.commit();
            stream.writeVerbose( "Updated test object from JDBC" );
        
            // Commit JDO transaction, this should report object modified
            // exception
            stream.writeVerbose( "Committing JDO update: dirty checking field modified" );
            if ( accessMode != Database.DbLocked ) {
                try {
                    _db.commit();
                    stream.writeVerbose( "Error: ObjectModifiedException not thrown" );
                    result = false;
                } catch ( ObjectModifiedException except ) {
                    stream.writeVerbose( "OK: ObjectModifiedException thrown" );
                }
            } else {
                try {
                    _db.commit();
                    stream.writeVerbose( "OK: ObjectModifiedException not thrown" );
                } catch ( ObjectModifiedException except ) {
                    result = false;
                    stream.writeVerbose( "Error: ObjectModifiedException thrown" );
                }
            }

            // Open a new transaction in order to conduct test
            _db.begin();
            oql.bind( new Integer( TestObject.DefaultId ) );
            object = (TestObject) oql.execute(  accessMode ).nextElement();
            object.setValue2( JDOValue );
            
            // Perform direct JDBC access and override the value of that table
            _conn.setAutoCommit( false );
            _conn.createStatement().execute( "UPDATE test_table SET value2='" + JDBCValue +
                                             "' WHERE id=" + TestObject.DefaultId );
            _conn.commit();
            stream.writeVerbose( "Updated test object from JDBC" );
        
            // Commit JDO transaction, this should report object modified
            // exception
            stream.writeVerbose( "Committing JDO update: no dirty checking field not modified" );
            try {
                _db.commit();
                stream.writeVerbose( "OK: ObjectModifiedException not thrown" );
            } catch ( ObjectModifiedException except ) {
                result = false;
                stream.writeVerbose( "Error: ObjectModifiedException thrown" );
            }
        } catch ( Exception except ) {
            stream.writeVerbose( "Error: " + except );
            except.printStackTrace();
            result = false;
        }
        return result;
    }


}

