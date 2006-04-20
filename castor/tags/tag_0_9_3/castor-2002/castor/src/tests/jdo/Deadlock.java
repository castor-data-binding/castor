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
 * Copyright 1999-2001 (C) Intalio, Inc. All Rights Reserved.
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

import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.framework.Assert;
import harness.TestHarness;
import harness.CastorTestCase;

/**
 * Tests for deadlock detection. Will report to the console two concurrent 
 * transactions working on the same objects. The first transaction will 
 * succeed, the second will fail. All three access modes: Shared (aka
 * optimistic locking), Exclusive (aka pessimistic locking) and DbLocked 
 * (premissitic memory locking with DBMS's locking) are covered by these 
 * test cases. These tests passed if LockNotGrantedException is thrown 
 * in the appropriate time for the access mode in action.
 */
public class Deadlock extends CastorTestCase {

    /**
     * The JDO database used for setUp
     */
    private Database       _db;

    /**
     * The JDO database used for first concurrent transactions
     */
    private Database       _firstDb;

    /**
     * The JDO database used for second concurrent transactions
     */
    private Database       _secondDb;

    /**
     * The JDO test suite for this test case
     */
    private JDOCategory    _category;

    /**
     * The time a transaction sleep and wait for another transaction to
     * process
     */
    public static final long  Wait = 1000;

    /**
     * AccessMode used in the tests
     */
    private short            _accessMode;

    /**
     * The java object to be synchronized on
     */
    private Object           _lock;

    /**
     * The thread that the first transaction is running on
     */
    private Thread           _first;

    /**
     * The thread that the second transaction is running on
     */
    private Thread           _second;


    /**
     * Constructor
     *
     * @param category The test suit of these test cases
     */
    public Deadlock( TestHarness category ) {
        super( category, "TC06", "Deadlock detection" );
        _category = (JDOCategory) category;
    }


    /**
     * Get the JDO Databases and create data objects into the database
     */
    public void setUp()
            throws PersistenceException {

        _db = _category.getDatabase( verbose );
        _firstDb = _category.getDatabase( verbose );
        _secondDb = _category.getDatabase( verbose );

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
            stream.println( "Retrieved object: " + object );
            object.setValue1( TestObject.DefaultValue1 );
            object.setValue2( TestObject.DefaultValue2 );
        } else {
            object = new TestObject();
            stream.println( "Creating new object: " + object );
            _db.create( object );
        }
        oql.bind( TestObject.DefaultId + 1 );
        enum = oql.execute();
        if ( enum.hasMoreElements() ) {
            object = (TestObject) enum.nextElement();
            stream.println( "Retrieved object: " + object );
            object.setValue1( TestObject.DefaultValue1 );
            object.setValue2( TestObject.DefaultValue2 );
        } else {
            object = new TestObject();
            object.setId( TestObject.DefaultId + 1 );
            stream.println( "Creating new object: " + object );
            _db.create( object );
        }
        _db.commit();
    }

    /**
     * Run tests for each of the three access modes.
     * <p>
     * Notice that some database have their own deadlock detection mechanisms 
     * implemented. Depending on the strickiness of the algorithm, when 
     * these tests are runing in DbLocked mode, some database might throws 
     * an exception to resolve the deadlock before Castor JDO detects it.
     */
    public void runTest() 
            throws PersistenceException, InterruptedException {

        stream.println( "Running in access mode shared" );
        runOnce( Database.Shared );
        stream.println( "" );

        stream.println( "Running in access mode exclusive" );
        runOnce( Database.Exclusive );
        stream.println( "" );

        stream.println( "Running in access mode db-locked" );
        runOnce( Database.DbLocked );
        stream.println( "" );
    }

    /**
     * Creates threads to test for deadlock detection behaviors.
     */
    public void runOnce( short accessMode ) 
            throws PersistenceException, InterruptedException {

        stream.println( "Note: this test uses a 2 second delay between threads. CPU and database load might cause the test to not perform synchronously, resulting in erroneous results. Make sure that execution is not hampered by CPU/datebase load." );

        // Run two threads in parallel attempting to update the
        // two objects in a different order, with the first
        // suceeding and second failing
        _accessMode = accessMode;
        _lock = new Object();
        _first = new FirstThread( _firstDb, this );
        _second = new SecondThread( _secondDb, this );

        _second.start();
        _first.start();

        _first.join();
        _second.join();
    }
    
    /**
     * Close the JDO databases used in the these test cases.
     */
    public void tearDown() 
            throws PersistenceException {
        if ( _db.isActive() ) _db.rollback();
        _db.close();
        if ( _firstDb.isActive() ) _firstDb.rollback();
        _firstDb.close();
        if ( _secondDb.isActive() ) _secondDb.rollback();
        _secondDb.close();
    }
    
    private class FirstThread extends Thread {
        
        private Database         _db;

        private boolean          _result = true;

        private Deadlock         _theTest;

        FirstThread( Database db, Deadlock theTest ) {
            _db      = db;
            _theTest = theTest;
        }


        boolean result() {
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
                _theTest.stream.println( "1.1: Loading object " + TestObject.DefaultId );
                object = (TestObject) _db.load( TestObject.class,
                                               new Integer( TestObject.DefaultId ), _accessMode );
                object.setValue1( TestObject.DefaultValue1 + ":1" );
                _theTest.stream.println( "1.2: Modified to " + object );
                
                // Notify the other thread that it may proceed and suspend
                synchronized ( _lock ) {
                    _lock.notify();
                    _lock.wait();
                }
                // Give the other thread a 2 second opportunity.
                // sleep( start + Wait - System.currentTimeMillis() );
                //start = System.currentTimeMillis();
                
                _theTest.stream.println( "1.3: Loading object " + ( TestObject.DefaultId  + 1 ) );
                object = (TestObject) _db.load( TestObject.class,
                                               new Integer( TestObject.DefaultId + 1 ), _accessMode );
                object.setValue2( TestObject.DefaultValue2 + ":1" );
                _theTest.stream.println( "1.4: Modified to " + object );
                
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
                _theTest.stream.println( "1.5: Committing" );
                _db.commit();
                _theTest.stream.println( "1.6: Committed" );
            } catch ( Exception except ) {
                fail( "1.X: " + except );
            }
        }
        
    }


    private class SecondThread extends Thread {

        private Database         _db;

        private Deadlock         _theTest;

        private boolean          _result = true;


        SecondThread( Database db, Deadlock theTest ) {
            _db = db;
            _theTest = theTest;
        }


        boolean result() {
            return _result;
        }


        public void run() {

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
                _theTest.stream.println( "2.1: Loading object " + ( TestObject.DefaultId + 1 ) );
                object = (TestObject) _db.load( TestObject.class,
                                               new Integer( TestObject.DefaultId + 1 ), _accessMode );
                object.setValue2( TestObject.DefaultValue2 + ":2" );
                _theTest.stream.println( "2.2: Modified to " + object );
                
                // Notify the other thread that it may proceed and suspend
                synchronized ( _lock ) {
                    _lock.notify();
                    _lock.wait( 2000 );
                }
                // Give the other thread a 2 second opportunity.
                // sleep( start + Wait - System.currentTimeMillis() );
                // start = System.currentTimeMillis();
                
                _theTest.stream.println( "2.3: Loading object " + TestObject.DefaultId );
                try {
                    object = (TestObject) _db.load( TestObject.class,
                                                    new Integer( TestObject.DefaultId ), _accessMode );
                } catch ( LockNotGrantedException except ) {
                    if ( _accessMode == Database.Exclusive ||
                         _accessMode == Database.DbLocked ) {
                        _theTest.stream.println( "2.X OK: Deadlock detected" );
                    } else {
                        _result = false;
                        _theTest.stream.println( "2.X Error: " + except );
                    }
                    _db.rollback();
                    return;
                }
                object.setValue1( TestObject.DefaultValue1 + ":2" );
                _theTest.stream.println( "2.4: Modified to " + object );

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
                _theTest.stream.println( "2.5: Committing" );
                _db.commit();
                _result = false;
                _theTest.stream.println( "2.6 Error: deadlock not detected" );
                _theTest.stream.println( "2.6 Second: Committed" );
            } catch ( TransactionAbortedException except ) {
                if ( except.getException() instanceof LockNotGrantedException )
                    _theTest.stream.println( "2.X OK: Deadlock detected" );
                else {
                    _result = false;
                    _theTest.stream.println( "2.X Error: " + except );
                }
                _theTest.stream.println( "2.X Second: aborting" );
            } catch ( Exception except ) {
                _result = false;
                _theTest.stream.println( "2.X Error: " + except );
                except.printStackTrace();
            }
        }
        
    }

}


