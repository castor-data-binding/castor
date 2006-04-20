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
public class ConcurrentSybase
    extends CWTestCase
{


    private Database       _db;


    private Connection     _conn;


    private JDOCategory    _category;


    static final String    JDBCValue = "jdbc value";


    static final String    JDOValue = "jdo value";


    public ConcurrentSybase( CWTestCategory category )
        throws CWClassConstructorException
    {
        super( "TC03", "Concurrent access" );
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

    class JDBCUpdate extends Thread {
        Connection _jconn;
        BooleanBin _checker;
        BooleanBin _error;
        String _table;
        String _colname;
        String _value;
        int _id;

        JDBCUpdate( Connection conn, BooleanBin checker, BooleanBin error, 
                String table, String colname, String value, int id ) {
            _jconn = conn;
            _checker = checker;
            _error = error;
            _table = table;
            _colname = colname;
            _value = value;
            _id = id;
        }

        public void run() {
            try {
                // Perform direct JDBC access and override the value of that table
                _conn.setAutoCommit( false );
                System.out.println("before jdbc update");
                _conn.createStatement().execute( "UPDATE "+_table+" SET "+_colname+
                        "='" + _value + "' WHERE id=" + _id );
                _conn.commit();
                _error.value = false;
            } catch ( Exception e ) {
                _error.value = true;
            } finally {
                System.out.println("%%%%%%%%% jdbc returning %%%%%%%%%");
                _checker.value = true;
            }
        }
    }


    class JDBCUpdateExt extends Thread {
        Connection _jconn;
        BooleanBin _checker;
        BooleanBin _error;
        String _table;
        String _colname;
        String _value;
        int _id;

        JDBCUpdateExt( Connection conn, BooleanBin checker, BooleanBin error, 
                String table, String colname, String value, int id ) {
            _jconn = conn;
            _checker = checker;
            _error = error;
            _table = table;
            _colname = colname;
            _value = value;
            _id = id;
        }

        public void run() {
            try {
                // Perform direct JDBC access and override the value of that table
                _conn.setAutoCommit( false );
                System.out.println("before jdbc update");
                _conn.createStatement().execute( "UPDATE "+_table+" SET "+_colname+
                        "='" + _value + "' WHERE id=" + _id );
                _conn.commit();
                _error.value = false;
            } catch ( Exception e ) {
                _error.value = true;
            } finally {
                System.out.println("%%%%%%%%% jdbc returning %%%%%%%%%");
                _checker.value = true;
            }
        }
    }


    public boolean run( CWVerboseStream stream )
    {
        boolean result = true;

        try {

            System.out.println("type: "+Boolean.TYPE+" class: "+Boolean.class+" equals? "+(Boolean.TYPE==Boolean.class));
            _db = _category.getDatabase( stream.verbose() );
            _conn = _category.getJDBCConnection(); 

            stream.writeVerbose( "Running in access mode db-locked" );
            if ( ! runOnce( stream, Database.DbLocked ) )
                result = false;
            stream.writeVerbose( "" );
/*
            stream.writeVerbose( "Running in access mode shared" );
            if ( ! runOnce( stream, Database.Shared ) )
                result = false;
            stream.writeVerbose( "" );
            stream.writeVerbose( "Running in access mode exclusive" );
            if ( ! runOnce( stream, Database.Exclusive ) )
                result = false;
            stream.writeVerbose( "" );
*/
            _db.close();
            _conn.close();
        } catch ( Exception except ) {
            except.printStackTrace();
            stream.writeVerbose( "Error: " + except );
            except.printStackTrace();
            result = false;
        }
        return result;
    }

    class BooleanBin {
        boolean value;
        BooleanBin( boolean init ) {
            value = init;
        }
    }

    private boolean runOnce( CWVerboseStream stream, short accessMode )
    {
        boolean result = true;

        try {
            OQLQuery      oql;
            TestObject    object;
            TestObjectExtends    objectExt;
            Enumeration   enum;
            JDBCUpdate    jdbc;
            BooleanBin    checker;
            BooleanBin    error;

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
            
            System.out.println("first load/commit done!");
            
            // Open a new transaction in order to conduct test
            _db.begin();
            oql.bind( new Integer( TestObject.DefaultId ) );
            object = (TestObject) oql.execute( accessMode ).nextElement();
            //object.setValue1( JDOValue );
            
            System.out.println("second load done!");
            checker = new BooleanBin( false );
            error = new BooleanBin( true );
            jdbc = new JDBCUpdate( _conn, checker, error, 
                    "test_table", "value1", JDBCValue, TestObject.DefaultId );
            jdbc.start();

            Thread.currentThread().sleep( 500 );

            // Commit JDO transaction, this should report object modified
            // exception
            stream.writeVerbose( "Committing JDO update: dirty checking field modified" );

            if ( accessMode != Database.DbLocked ) {
                try {
                    _db.commit();
                    System.out.println("########### jdo returning ###########");
                    stream.writeVerbose( "Error: ObjectModifiedException not thrown" );
                    result = false;
                } catch ( ObjectModifiedException except ) {
                    stream.writeVerbose( "OK: ObjectModifiedException thrown" );
                }
            } else {
                if ( checker.value != true ) {
                    System.out.println("########### jdo returning ###########");
                    Thread.currentThread().sleep( 100 );
                    _db.commit();
                    if ( checker.value == true && error.value == false ) {
                        stream.writeVerbose( "OK: The Object is locked and unlocked in the database" );
                    } else {
                        result = false;
                    }
                } else {
                    stream.writeVerbose("Error: Object isn't locked in the database");
                    result = false;
                    _db.commit();
                }

            }



            System.out.println("===============================");




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
            
            System.out.println("first load/commit done!");
            
            // Open a new transaction in order to conduct test
            _db.begin();
            oql.bind( new Integer( TestObject.DefaultId ) );
            object = (TestObject) oql.execute( accessMode ).nextElement();
            object.setValue1( JDOValue );
            
            System.out.println("second load done!");
            checker = new BooleanBin( false );
            error = new BooleanBin( true );
            jdbc = new JDBCUpdate( _conn, checker, error, 
                    "test_table", "value2", JDBCValue, TestObject.DefaultId );
            jdbc.start();

            Thread.currentThread().sleep( 500 );

            // Commit JDO transaction, this should report object modified
            // exception
            stream.writeVerbose( "Committing JDO update: dirty checking field modified" );

            if ( accessMode != Database.DbLocked ) {
                try {
                    _db.commit();
                    System.out.println("########### jdo returning ###########");
                    stream.writeVerbose( "OK: ObjectModifiedException not thrown" );
                    result = false;
                } catch ( ObjectModifiedException except ) {
                    stream.writeVerbose( "Error: ObjectModifiedException thrown" );
                }
            } else {
                if ( checker.value == true ) {
                    stream.writeVerbose("Error: Object isn't locked in the database");
                    result = false;
                }

                _db.commit();
                System.out.println("########### jdo returning ###########");
                Thread.currentThread().sleep( 100 );
                if ( checker.value == true && error.value == false ) {
                    stream.writeVerbose( "OK: The Object is locked and unlocked in the database" );
                } else {
                    result = false;
                }
            }













            // Open transaction in order to perform JDO operations
            _db.begin();
        
            // Determine if test object exists, if not create it.
            // If it exists, set the name to some predefined value
            // that this test will later override.
            oql = _db.getOQLQuery( "SELECT object FROM jdo.TestObjectExtends object WHERE id = $1" );
            oql.bind( TestObjectExtends.DefaultId );
            enum = oql.execute();
            if ( enum.hasMoreElements() ) {
                objectExt = (TestObjectExtends) enum.nextElement();
                stream.writeVerbose( "Retrieved object: " + objectExt );
                objectExt.setValue3( TestObjectExtends.DefaultValue3 );
                objectExt.setValue4( TestObjectExtends.DefaultValue4 );
            } else {
                objectExt = new TestObjectExtends();
                stream.writeVerbose( "Creating new object: " + objectExt );
                _db.create( objectExt );
            }
            _db.commit();
            
            System.out.println("first load/commit done!");
            
            // Open a new transaction in order to conduct test
            _db.begin();
            oql.bind( new Integer( TestObjectExtends.DefaultId ) );
            objectExt = (TestObjectExtends) oql.execute( accessMode ).nextElement();
            objectExt.setValue1( JDOValue );
            
            System.out.println("second load done!");
            checker = new BooleanBin( false );
            error = new BooleanBin( true );
            jdbc = new JDBCUpdate( _conn, checker, error, 
                    "test_table_extends", "value3", JDBCValue, TestObjectExtends.DefaultId );
            jdbc.start();

            Thread.currentThread().sleep( 500 );

            // Commit JDO transaction, this should report object modified
            // exception
            stream.writeVerbose( "Committing JDO update: dirty checking field modified" );

            if ( accessMode != Database.DbLocked ) {
                try {
                    _db.commit();
                    System.out.println("########### jdo returning ###########");
                    stream.writeVerbose( "Error: ObjectModifiedException not thrown" );
                    result = false;
                } catch ( ObjectModifiedException except ) {
                    stream.writeVerbose( "OK: ObjectModifiedException thrown" );
                }
            } else {
                if ( checker.value == true ) {
                    stream.writeVerbose("Error: Object isn't locked in the database");
                    result = false;
                }

                _db.commit();
                System.out.println("########### jdo returning ###########");
                Thread.currentThread().sleep( 100 );
                if ( checker.value == true && error.value == false ) {
                    stream.writeVerbose( "OK: The Object is locked and unlocked in the database" );
                } else {
                    result = false;
                }
            }



            System.out.println("===============================");




            // Open transaction in order to perform JDO operations
            _db.begin();
        
            // Determine if test object exists, if not create it.
            // If it exists, set the name to some predefined value
            // that this test will later override.
            oql = _db.getOQLQuery( "SELECT object FROM jdo.TestObjectExtends object WHERE id = $1" );
            oql.bind( TestObjectExtends.DefaultId );
            enum = oql.execute();
            if ( enum.hasMoreElements() ) {
                objectExt = (TestObjectExtends) enum.nextElement();
                stream.writeVerbose( "Retrieved object: " + object );
                objectExt.setValue1( TestObjectExtends.DefaultValue3 );
                objectExt.setValue2( TestObjectExtends.DefaultValue4 );
            } else {
                objectExt = new TestObjectExtends();
                stream.writeVerbose( "Creating new object: " + objectExt );
                _db.create( objectExt );
            }
            _db.commit();
            
            System.out.println("first load/commit done!");
            
            // Open a new transaction in order to conduct test
            _db.begin();
            oql.bind( new Integer( TestObjectExtends.DefaultId ) );
            objectExt = (TestObjectExtends) oql.execute( accessMode ).nextElement();
            objectExt.setValue1( JDOValue );
            
            System.out.println("second load done!");
            checker = new BooleanBin( false );
            error = new BooleanBin( true );
            jdbc = new JDBCUpdate( _conn, checker, error, 
                    "test_table_extends", "value4", JDBCValue, TestObjectExtends.DefaultId );
            jdbc.start();

            Thread.currentThread().sleep( 500 );

            // Commit JDO transaction, this should report object modified
            // exception
            stream.writeVerbose( "Committing JDO update: dirty checking field modified" );

            if ( accessMode != Database.DbLocked ) {
                try {
                    _db.commit();
                    System.out.println("########### jdo returning ###########");
                    stream.writeVerbose( "OK: ObjectModifiedException not thrown" );
                    result = false;
                } catch ( ObjectModifiedException except ) {
                    stream.writeVerbose( "Error: ObjectModifiedException thrown" );
                }
            } else {
                if ( checker.value == true ) {
                    stream.writeVerbose("Error: Object isn't locked in the database");
                    result = false;
                }

                _db.commit();
                System.out.println("########### jdo returning ###########");
                Thread.currentThread().sleep( 100 );
                if ( checker.value == true && error.value == false ) {
                    stream.writeVerbose( "OK: The Object is locked and unlocked in the database" );
                } else {
                    result = false;
                }
            }

        } catch ( Exception except ) {
            stream.writeVerbose( "Error: " + except );
            except.printStackTrace();
            result = false;
        }
        return result;
    }


}

