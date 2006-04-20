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
 *    permission of Exoffice Technologies.  For written permission,
 *    please contact info@exolab.org.
 *
 * 4. Products derived from this Software may not be called "Exolab"
 *    nor may "Exolab" appear in their names without prior written
 *    permission of Exoffice Technologies. Exolab is a registered
 *    trademark of Exoffice Technologies.
 *
 * 5. Due credit should be given to the Exolab Project
 *    (http://www.exolab.org/).
 *
 * THIS SOFTWARE IS PROVIDED BY EXOFFICE TECHNOLOGIES AND CONTRIBUTORS
 * ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT
 * NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL
 * EXOFFICE TECHNOLOGIES OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * Copyright 1999 (C) Exoffice Technologies Inc. All Rights Reserved.
 *
 * $Id$
 */


package jdo;


import java.io.IOException;
import java.util.Enumeration;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.LockNotGrantedException;
import org.exolab.castor.jdo.TransactionAbortedException;
import org.exolab.jtf.CWVerboseStream;
import org.exolab.jtf.CWTestCase;
import org.exolab.exceptions.CWClassConstructorException;


/**
 * Simple test for deadlock detection. Will report to the console two
 * concurrent transactions working on the same objects. The first transaction
 * will succeed, the second will fail.
 */
public class Deadlock
    extends CWTestCase
{


    private Database       _db;


    public static final long  Wait = 1000;


    public Deadlock()
        throws CWClassConstructorException
    {
        super( "TC02", "Test deadlock detection" );
        try {
            _db = JDOTests.getDatabase();
        } catch ( Exception except ) {
            throw new CWClassConstructorException( except.toString() );
        }
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
        } catch ( Exception except ) {
            try {
                stream.writeVerbose( "Error: " + except );
            } catch ( IOException except2 ) { }
            except.printStackTrace();
            result = false;
        }
        return result;
    }


    public boolean runOnce( CWVerboseStream stream, short accessMode )
    {
        boolean result = true;

        try {
            OQLQuery      oql;
            TestObject    object;
            Enumeration   enum;
            
            // Open transaction in order to perform JDO operations
            _db.begin();
            
            // Create two objects in the database -- need something to lock
            oql = _db.getOQLQuery( "SELECT object FROM jdo.TestObject object WHERE id = $1" );
            oql.bind( TestObject.DefaultId );
            enum = oql.execute();
            if ( enum.hasMoreElements() ) {
                object = (TestObject) enum.nextElement();
                stream.writeVerbose( "Retrieved object: " + object );
                object.setFirst( TestObject.DefaultFirst );
                object.setSecond( TestObject.DefaultSecond );
            } else {
                object = new TestObject();
                stream.writeVerbose( "Creating new object: " + object );
                _db.create( object );
            }
            oql.bind( TestObject.DefaultId + 1 );
            enum = oql.execute();
            if ( enum.hasMoreElements() ) {
                object = (TestObject) enum.nextElement();
                stream.writeVerbose( "Retrieved object: " + object );
                object.setFirst( TestObject.DefaultFirst );
                object.setSecond( TestObject.DefaultSecond );
            } else {
                object = new TestObject();
                object.setId( TestObject.DefaultId + 1 );
                stream.writeVerbose( "Creating new object: " + object );
                _db.create( object );
            }
            _db.commit();

            stream.writeVerbose( "Note: this test uses a 2 second delay between threads. CPU and database load might cause the test to not perform synchronously, resulting in erroneous results. Make sure that execution is not hampered by CPU/datebase load." );
        
            // Run two threads in parallel attempting to update the
            // two objects in a different order, with the first
            // suceeding and second failing
            FirstThread  first;
            SecondThread second;
            
            first = new FirstThread();
            first._db = JDOTests.getDatabase();
            first._stream = stream;
            first._accessMode = accessMode;
            first.start();
            second = new SecondThread();
            second._db = JDOTests.getDatabase();
            second._stream = stream;
            second._accessMode = accessMode;
            second.start();

            first.join();
            second.join();
            result = first._result & second._result;
            
        } catch ( Exception except ) {
            try {
                stream.writeVerbose( "Error: " + except );
            } catch ( IOException except2 ) { }
            except.printStackTrace();
            result = false;
        }
        return result;

    }
    
    
    static class FirstThread
        extends Thread
    {
        
        private Database         _db;


        private CWVerboseStream  _stream;


        private boolean          _result = true;


        private short            _accessMode;


        public void run()
        {
            TestObject   object;
            long         start;

            try {
                _db.begin();
                start = System.currentTimeMillis();

                // Load first object and change something about it (otherwise will not write)
                _stream.writeVerbose( "First: Loading object " + TestObject.DefaultId );
                object = (TestObject) _db.load( TestObject.class,
                                               new Integer( TestObject.DefaultId ), _accessMode );
                object.setFirst( TestObject.DefaultFirst + ":1" );
                _stream.writeVerbose( "First: Modified to " + object );
                
                // Give the other thread a 2 second opportunity.
                sleep( start + Wait - System.currentTimeMillis() );
                start = System.currentTimeMillis();
                
                _stream.writeVerbose( "First: Loading object " + ( TestObject.DefaultId  + 1 ) );
                object = (TestObject) _db.load( TestObject.class,
                                               new Integer( TestObject.DefaultId + 1 ), _accessMode );
                object.setSecond( TestObject.DefaultSecond + ":1" );
                _stream.writeVerbose( "First: Modified to " + object );
                
                // Give the other thread a 2 second opportunity.
                sleep( Math.max( start + Wait - System.currentTimeMillis(), 0 ) );

                // Attempt to commit the transaction, must acquire a write
                // lock blocking until the first transaction completes.
                _stream.writeVerbose( "First: Committing" );
                _db.commit();
                _stream.writeVerbose( "First: Committed" );
                _db.close();
            } catch ( Exception except ) {
                _result = false;
                try {
                    _stream.writeVerbose( "First: " + except );
                } catch ( IOException e2 ) { }
                except.printStackTrace();
                _result = false;
            }
        }
        
    }
    
    
    static class SecondThread
        extends Thread
    {

        
        private Database         _db;


        private CWVerboseStream  _stream;


        private boolean          _result = true;


        private short            _accessMode;


        public void run()
        {
            TestObject   object;
            Database     db = null;
            long         start;

            try {
                _db.begin();
                
                // Give the other thread a 2 second opportunity.
                sleep( Wait / 2 );
                start = System.currentTimeMillis();
                
                // Load first object and change something about it (otherwise will not write)
                _stream.writeVerbose( "Second: Loading object " + ( TestObject.DefaultId + 1 ) );
                object = (TestObject) _db.load( TestObject.class,
                                               new Integer( TestObject.DefaultId + 1 ), _accessMode );
                object.setSecond( TestObject.DefaultSecond + ":2" );
                _stream.writeVerbose( "Second: Modified to " + object );
                
                // Give the other thread a 2 second opportunity.
                sleep( start + Wait - System.currentTimeMillis() );
                start = System.currentTimeMillis();
                
                _stream.writeVerbose( "Second: Loading object " + TestObject.DefaultId );
                try {
                    object = (TestObject) _db.load( TestObject.class,
                                                    new Integer( TestObject.DefaultId ), _accessMode );
                } catch ( LockNotGrantedException except ) {
                    if ( _accessMode == Database.Exclusive ||
                         _accessMode == Database.DbLocked ) {
                        _stream.writeVerbose( "OK: Deadlock detected" );
                    } else {
                        _result = false;
                        _stream.writeVerbose( "Error: " + except );
                    }
                    _db.rollback();
                    _db.close();
                    return;
                }
                object.setFirst( TestObject.DefaultFirst + ":2" );
                _stream.writeVerbose( "Second: Modified to " + object );

                // Give the other thread a 2 second opportunity.
                sleep( start + Wait - System.currentTimeMillis() );
                start = System.currentTimeMillis();
                
                // Attempt to commit the transaction, must acquire a write
                // lock blocking until the first transaction completes.
                _stream.writeVerbose( "Second: Committing" );
                _db.commit();
                _result = false;
                _stream.writeVerbose( "Error: deadlock not detected" );
                _stream.writeVerbose( "Second: Committed" );
                _db.close();
            } catch ( TransactionAbortedException except ) {
                try {
                    if ( except.getException() instanceof LockNotGrantedException )
                        _stream.writeVerbose( "OK: Deadlock detected" );
                    else {
                        _result = false;
                        _stream.writeVerbose( "Error: " + except );
                    }
                    _stream.writeVerbose( "Second: aborting" );
                } catch ( IOException e2 ) { }
            } catch ( Exception except ) {
                _result = false;
                try {
                    _stream.writeVerbose( "Error: " + except );
                } catch ( IOException e2 ) { }
                except.printStackTrace();
            }
        }
        
    }
    
    
}


