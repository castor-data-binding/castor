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
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.LockNotGrantedException;
import org.exolab.castor.jdo.TransactionAbortedException;
import org.exolab.jtf.CWVerboseStream;
import org.exolab.jtf.CWTestCase;
import org.exolab.jtf.CWTestCategory;
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


    private Database       _firstDb;


    private Database       _secondDb;


    private JDOCategory    _category;


    public static final long  Wait = 1000;


    private short            _accessMode;


    private Object           _lock;


    private Thread           _first;


    private Thread           _second;


    public Deadlock( CWTestCategory category )
        throws CWClassConstructorException
    {
        super( "TC02", "Deadlock detection" );
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
            _firstDb = _category.getDatabase( stream.verbose() );
            _secondDb = _category.getDatabase( stream.verbose() );

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
            _firstDb.close();
            _secondDb.close();
        } catch ( Exception except ) {
            stream.writeVerbose( "Error: " + except );
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
                object.setValue1( TestObject.DefaultValue1 );
                object.setValue2( TestObject.DefaultValue2 );
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
                object.setValue1( TestObject.DefaultValue1 );
                object.setValue2( TestObject.DefaultValue2 );
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
            _accessMode = accessMode;
            _lock = new Object();
            _first = new FirstThread( _firstDb, stream );
            _second = new SecondThread( _secondDb, stream );

            _second.start();
            _first.start();

            _first.join();
            _second.join();
            result = ( (FirstThread) _first ).result() & ( (SecondThread) _second ).result();

        } catch ( Exception except ) {
            stream.writeVerbose( "Error: " + except );
            except.printStackTrace();
            result = false;
        }
        return result;

    }
    
    
    class FirstThread
        extends Thread
    {
        
        private Database         _db;


        private CWVerboseStream  _stream;


        private boolean          _result = true;


        FirstThread( Database db, CWVerboseStream stream )
        {
            _db = db;
            _stream = stream;
        }


        boolean result()
        {
            return _result;
        }


        public void run()
        {
            TestObject   object;
            long         start;

            try {
                _db.begin();
                start = System.currentTimeMillis();

                // Load first object and change something about it (otherwise will not write)
                _stream.writeVerbose( "1.1: Loading object " + TestObject.DefaultId );
                object = (TestObject) _db.load( TestObject.class,
                                               new Integer( TestObject.DefaultId ), _accessMode );
                object.setValue1( TestObject.DefaultValue1 + ":1" );
                _stream.writeVerbose( "1.2: Modified to " + object );
                
                // Notify the other thread that it may proceed and suspend
                synchronized ( _lock ) {
                    _lock.notify();
                    _lock.wait();
                }
                // Give the other thread a 2 second opportunity.
                // sleep( start + Wait - System.currentTimeMillis() );
                //start = System.currentTimeMillis();
                
                _stream.writeVerbose( "1.3: Loading object " + ( TestObject.DefaultId  + 1 ) );
                object = (TestObject) _db.load( TestObject.class,
                                               new Integer( TestObject.DefaultId + 1 ), _accessMode );
                object.setValue2( TestObject.DefaultValue2 + ":1" );
                _stream.writeVerbose( "1.4: Modified to " + object );
                
                // Notify the other thread that it may proceed and suspend
                if ( _second.isAlive() ) {
                    synchronized ( _lock ) {
                        _lock.notify();
                        _lock.wait();
                    }
                }
                // Give the other thread a 2 second opportunity.
                //sleep( Math.max( start + Wait - System.currentTimeMillis(), 0 ) );

                // Attempt to commit the transaction, must acquire a write
                // lock blocking until the first transaction completes.
                _stream.writeVerbose( "1.5: Committing" );
                _db.commit();
                _stream.writeVerbose( "1.6: Committed" );
            } catch ( Exception except ) {
                _result = false;
                _stream.writeVerbose( "1.X: " + except );
                except.printStackTrace();
                _result = false;
            }
        }
        
    }
    
    
    class SecondThread
        extends Thread
    {

        
        private Database         _db;


        private CWVerboseStream  _stream;


        private boolean          _result = true;


        SecondThread( Database db, CWVerboseStream stream )
        {
            _db = db;
            _stream = stream;
        }


        boolean result()
        {
            return _result;
        }


        public void run()
        {
            TestObject   object;
            Database     db = null;
            long         start;

            try {
                _db.begin();
                
                // Suspend
                synchronized ( _lock ) {
                    _lock.wait();
                }
                // Give the other thread a 2 second opportunity.
                //sleep( Wait / 2 );
                //start = System.currentTimeMillis();
                
                // Load first object and change something about it (otherwise will not write)
                _stream.writeVerbose( "2.1: Loading object " + ( TestObject.DefaultId + 1 ) );
                object = (TestObject) _db.load( TestObject.class,
                                               new Integer( TestObject.DefaultId + 1 ), _accessMode );
                object.setValue2( TestObject.DefaultValue2 + ":2" );
                _stream.writeVerbose( "2.2: Modified to " + object );
                
                // Notify the other thread that it may proceed and suspend
                synchronized ( _lock ) {
                    _lock.notify();
                    _lock.wait( 2000 );
                }
                // Give the other thread a 2 second opportunity.
                // sleep( start + Wait - System.currentTimeMillis() );
                // start = System.currentTimeMillis();
                
                _stream.writeVerbose( "2.3: Loading object " + TestObject.DefaultId );
                try {
                    object = (TestObject) _db.load( TestObject.class,
                                                    new Integer( TestObject.DefaultId ), _accessMode );
                } catch ( LockNotGrantedException except ) {
                    if ( _accessMode == Database.Exclusive ||
                         _accessMode == Database.DbLocked ) {
                        _stream.writeVerbose( "2.X OK: Deadlock detected" );
                    } else {
                        _result = false;
                        _stream.writeVerbose( "2.X Error: " + except );
                    }
                    _db.rollback();
                    return;
                }
                object.setValue1( TestObject.DefaultValue1 + ":2" );
                _stream.writeVerbose( "2.4: Modified to " + object );

                // Notify the other thread that it may proceed and suspend
                synchronized ( _lock ) {
                    _lock.notify();
                    _lock.wait( 2000 );
                }
                // Give the other thread a 2 second opportunity.
                //sleep( start + Wait - System.currentTimeMillis() );
                //start = System.currentTimeMillis();
                
                // Attempt to commit the transaction, must acquire a write
                // lock blocking until the first transaction completes.
                _stream.writeVerbose( "2.5: Committing" );
                _db.commit();
                _result = false;
                _stream.writeVerbose( "2.6 Error: deadlock not detected" );
                _stream.writeVerbose( "2.6 Second: Committed" );
            } catch ( TransactionAbortedException except ) {
                if ( except.getException() instanceof LockNotGrantedException )
                    _stream.writeVerbose( "2.X OK: Deadlock detected" );
                else {
                    _result = false;
                    _stream.writeVerbose( "2.X Error: " + except );
                }
                _stream.writeVerbose( "2.X Second: aborting" );
            } catch ( Exception except ) {
                _result = false;
                _stream.writeVerbose( "2.X Error: " + except );
                except.printStackTrace();
            }
        }
        
    }
    
    
}


