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
 * Copyright 2000-2001 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id$
 */


package jdo;


import java.sql.Connection;
import java.sql.SQLException;
import java.util.Random;

import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.LockNotGrantedException;
import org.exolab.castor.jdo.TransactionAbortedException;
import org.exolab.castor.jdo.TransactionNotInProgressException;
import org.exolab.castor.jdo.ObjectNotFoundException;
import harness.TestHarness;
import harness.CastorTestCase;


/**
 * This is a concurrent stress test. This test creates one writing thread
 * that continuously creates, loads and modifies data objects. It loads 
 * and test if the modifications has succeed, loads again and removes 
 * data objects and creates a new object with the same identity again....; 
 * multiple read threads are created and continuously read the data objects 
 * from transactions and commit the transactions without modifying any
 * data object. The 'read and commit' actions essentially lock and unlock 
 * the data object. These tests pass if all modifications to data objects 
 * via the write thread is properly persisted and there is no deadlock 
 * occurring. Passing the tests confirm Castor JDO properly lock and release 
 * objects. 
 * <p>
 * Tests are performed on all four different cache types. (note, these tests 
 * may failed if the number of JDBC connections available to Castor JDO is 
 * too small. To resolve the problem, reduce the number of read threads or 
 * increase the available connections.)
 */
public class CacheLeakage extends CastorTestCase {

    /**
     * Number of target data objects to be created and deleted
     */
    private final static int NUM_OF_CREATE_DELETE = 10;

    /**
     * Number of trial of loads and releases on each object
     */
    private final static int NUM_OF_READ = 50;

    /**
     * Number of retrials attempt to load per each trial
     */
    private final static int NUM_OF_RETRIAL = 20;

    /**
     * The base time in milliseconds between each attempts
     */
    private final static int SLEEP_BASE_TIME = 100;

    /**
     * Number of load and releases of read threads to race on 
     * the target object
     */
    private final static int NUM_OF_READ_THREADS = 5;

    /**
     * The JDO test suite these test cases belongs to
     */
    private JDOCategory    _category;

    /**
     * The JDO database
     */
    Database _db;

    /**
     * The JDBC connection used to initalizes tables for tests
     */
    Connection _conn;

    /**
     * The class name of the target data objects
     */
    String _className;

    /**
     * The java class of the target data objects
     */
    Class _classType;

    /**
     * The cache type used in the current test
     */
    int _cacheType;

    /**
     * Indicates leakage detected
     */
    boolean _errLeak;

    /**
     * Indicates error detected
     */
    boolean _errCount;

    private final static int COUNT_LIMITED = 0;

    private final static int TIME_LIMITED  = 1;

    private final static int NO_CACHE      = 2;

    private final static int UNLIMITED     = 3;

    /**
     * Constructor
     *
     * @param category the test suite of these test cases
     */
    public CacheLeakage( TestHarness category ) {

        super( category, "TC08", "Cache leakage" );
        _category = (JDOCategory) category;
        _errLeak = false;
        _errCount = false;
    }

    /**
     * Get a JDO Database and get a JDBC connection
     */
    public void setUp() 
            throws PersistenceException, SQLException {

        _db = _category.getDatabase();
        _conn = _category.getJDBCConnection();
        _conn.setAutoCommit( false );
    }

    /**
     * Run the stress test for four different cache setting
     */
    public void runTest() 
            throws PersistenceException, SQLException {

        _db = _category.getDatabase();
        _conn = _category.getJDBCConnection();
        _conn.setAutoCommit( false );

        _cacheType = Database.Shared;
        runOnce();

        _cacheType = Database.Exclusive;
        runOnce();

        _cacheType = Database.DbLocked;
        runOnce();

        _cacheType = Database.ReadOnly; 
        runOnce();

        assertTrue( "Element leak not detected!", !_errLeak );
        assertTrue( "Race condition not happened!", !_errCount );
    }

    public void tearDown() 
            throws PersistenceException, SQLException {
        if ( _db.isActive() ) _db.rollback();
        _db.close();
        _conn.close();
    }

    /**
     * Helper class to run the stress tests once for a cache type.
     */
    public void runOnce() 
            throws PersistenceException, SQLException {

        // clear the table
        int del = _conn.createStatement().executeUpdate( "DELETE FROM test_race" );
        stream.println( "row deleted in table test_race: " + del );
        _conn.commit();

        // set the className and classType to be used
        switch ( _cacheType ) {
        case COUNT_LIMITED:
            _className = "jdo.TestRaceCount";
            _classType = jdo.TestRaceCount.class;
            break;
        case TIME_LIMITED:
            _className = "jdo.TestRaceTime";
            _classType = jdo.TestRaceTime.class;
            break;
        case NO_CACHE:
            _className = "jdo.TestRaceNone";
            _classType = jdo.TestRaceNone.class;
            break;
        case UNLIMITED:
            _className = "jdo.TestRaceUnlimited";
            _classType = jdo.TestRaceUnlimited.class;
            break;
        }

        CreateDeleteThread cdThread = new CreateDeleteThread( this, _category, _cacheType, NUM_OF_CREATE_DELETE );

        ReadThread[] rThread =  new ReadThread[NUM_OF_READ_THREADS];
        for ( int i=0; i < NUM_OF_READ_THREADS; i++ ) {
            rThread[i] = new ReadThread( this, cdThread, _category, NUM_OF_READ );
            rThread[i].start();
        }

        cdThread.start();
        

        // Polling the test case to see if it is finished
        try {
            while ( !cdThread.isDone() ) {
                Thread.sleep( 500 );
            }

            // Joins all the finished threads
            cdThread.join();
            for ( int i=0; i < NUM_OF_READ_THREADS; i++ ) {
                rThread[i].join();
            }
        } catch ( InterruptedException e ) {
            fail( e.toString() );
        }
    }

    /**
     * Multiple read threads are created and continuously read the data 
     * objects that is modifing by the WriteThread. A read threads does
     * not modify the data objects but commit the transaction. It gives
     * stress to Castor JDO and intend to test if the right behavior is
     * not affect by stress.
     */
    private class ReadThread extends Thread {
        Database db;
        int trial;
        int cachetype;
        boolean isDone;
        Random ran;
        CacheLeakage  theTest;
        CreateDeleteThread other;
        Integer            id = new Integer(5);

        ReadThread( CacheLeakage theTest, CreateDeleteThread other, JDOCategory c, int n ) 
                throws PersistenceException {

            this.db = c.getDatabase();
            this.trial = n;
            this.ran = new Random();
            this.other = other;
            this.theTest = theTest;
        }

        public void run() {
            boolean succeed;
            int trials = 0;
            try {
                for ( int i=0; i < trial && !other.isDone(); i++ ) {
                    try {
                        // loads it and releases it
                        db.begin();
                        succeed = false;
                        trials = 0;

                        while ( !succeed && trials < NUM_OF_RETRIAL && !other.isDone() ) {
                            trials++;
                            try {
                                db.load( _classType, id, Database.Shared );
                                // may throw ObjectNotFoundException
                                // LockNotGrantedException
                                db.commit();
                                succeed = true;
                            } catch ( LockNotGrantedException e ) {
                                succeed = false;
                                // ethernet way of retry
                                Thread.sleep( (long) ((SLEEP_BASE_TIME^trials) * ran.nextDouble()) );
                            } catch ( ObjectNotFoundException e ) {
                                succeed = false;
                                // ethernet way of retry
                                Thread.sleep( (long) ((SLEEP_BASE_TIME^trials) * ran.nextDouble()) );
                            } catch ( TransactionAbortedException e ) {
                                succeed = false;
                                // ethernet way of retry
                                Thread.sleep( (long) ((SLEEP_BASE_TIME^trials) * ran.nextDouble()) );
                            }
                        }
                        if ( db.isActive() ) 
                            db.rollback();
                    
                    } catch ( Exception e ) {
                        fail( e.toString() );
                    }
                }
            } finally {
                isDone = true;
                try {
                    db.close();
                } catch ( PersistenceException e ) {
                    fail( e.toString() );
                }
            }
        }
        boolean isDone() {
            return isDone;
        }
    }

    /**
     * This is write thread that continuously create, load and modify, 
     * load and test if the modification is succeed, load and remove 
     * data objects and create again. This threads is the only thread 
     * that modifies data objects. If any inconsistency detected, for
     * examples, modification is not persisted, deleted object reappears,
     * created object disappeared, we can confirm there is problem in
     * the concurrent engine of Castor JDO.
     */
    private class CreateDeleteThread extends Thread {
        CacheLeakage theTest;
        Database     db;
        int          trial;
        boolean      isDone;
        Random       ran;
        int          cachetype;

        private CreateDeleteThread( CacheLeakage theTest, JDOCategory c, int cachetype, int n ) 
                throws PersistenceException {

            this.db = c.getDatabase();
            this.trial = n;
            this.ran = new Random();
            this.cachetype = cachetype;
            this.theTest = theTest;
        }
        public void run() {
            try {
                CastorTestCase.stream.println("start testing");
                TestRace tr;
                TestRace testrace;
                boolean succeed;
                int trials;
                Integer id = new Integer(5);

                out:
                for ( int i=0; i<trial; i++ ) {
                    // create, modified, delete object, depending on the current
                    // cache type
                    try {
                        switch ( cachetype ) {
                        case 0:
                            testrace = new TestRaceCount();
                            testrace.setId(5);
                            break;
                        case 1:
                            testrace = new TestRaceTime();
                            testrace.setId(5);
                            break;
                        case 2:
                            testrace = new TestRaceNone();
                            testrace.setId(5);
                            break;
                        case 3:
                            testrace = new TestRaceUnlimited();
                            testrace.setId(5);
                            break;
                        default:
                            testrace = null;
                        }
     
                            db.begin();
                            db.create( testrace );  // may throw duplicateIdentityException
                            db.commit();

                        // load it and modify it
                        succeed = false;
                        trials = 0;
                        while ( !succeed && trials < NUM_OF_RETRIAL ) {
                            trials++;
                            try {
                                db.begin();
                                tr = (TestRace) db.load( _classType, id );
                                // may throw ObjectNotFoundException
                                // LockNotGrantedException
                                tr.incValue1();
                                db.commit();
                                succeed = true;
                            } catch ( LockNotGrantedException e ) {
                                succeed = false;
                                // ethernet way of retry
                                db.rollback();
                                Thread.sleep( (long) ((SLEEP_BASE_TIME^trials) * ran.nextDouble()) );
                            } catch ( TransactionAbortedException e ) {
                                succeed = false;
                                // ethernet way of retry
                                db.rollback();
                                Thread.sleep( (long) ((SLEEP_BASE_TIME^trials) * ran.nextDouble()) );
                            } 
                        }
                        if ( db.isActive() ) 
                            db.rollback();

                        // load it and release it
                        succeed = false;
                        trials = 0;
                        while ( !succeed && trials < NUM_OF_RETRIAL ) {
                            trials++;
                            try {
                                db.begin();
                                tr = (TestRace) db.load( _classType, id );
                                // may throw ObjectNotFoundException
                                // LockNotGrantedException
                                db.commit();
                                succeed = true;
                            } catch ( LockNotGrantedException e ) {
                                succeed = false;
                                // ethernet way of retry
                                db.rollback();
                                Thread.sleep( (long) ((SLEEP_BASE_TIME^trials) * ran.nextDouble()) );
                            } 
                        }
                        if ( db.isActive() ) 
                            db.rollback();

                        // load it and delete it
                        succeed = false;
                        trials = 0;
                        while ( !succeed && trials < NUM_OF_RETRIAL ) {
                            trials++;    
                            try {
                                db.begin();
                                tr = (TestRace) db.load( _classType, id );
                                                // may throw ObjectNotFoundException
                                                // LockNotGrantedException
                                db.remove( tr );
                                db.commit();
                                succeed = true;
                            } catch ( LockNotGrantedException e ) {
                                succeed = false;
                                db.rollback();
                                Thread.sleep( (long) ((SLEEP_BASE_TIME^trials) * ran.nextDouble()) );
                            } catch ( TransactionAbortedException e ) {
                                succeed = false;
                                // ethernet way of retry
                                db.rollback();
                                Thread.sleep( (long) ((SLEEP_BASE_TIME^trials) * ran.nextDouble()) );
                            }
                        }
                        if ( db.isActive() ) 
                            db.rollback();
                        if ( !succeed )
                            throw new Exception("Transaction can't not lock the object within "+trials+" trials");

                    } catch ( TransactionNotInProgressException e ) {
                        CastorTestCase.stream.println( "Thread <CreateDelete> will be killed. Unexcepted exception: "+e.getException() );
                        e.printStackTrace();
                        if ( db.isActive() ) try { db.rollback(); } catch ( Exception ee ) {}
                        _errLeak = true;
                        break out;
                    } catch ( PersistenceException e ) {
                        CastorTestCase.stream.println( "Thread <CreateDelete> will be killed. Unexcepted exception: " );
                        e.printStackTrace();
                        if ( db.isActive() ) try { db.rollback(); } catch ( Exception ee ) {}
                        _errLeak = true;
                        break out;
                    } catch ( Exception e ) {
                        CastorTestCase.stream.println( "Thread <CreateDelete> will be killed. Element not found: other exception: "+e );
                        e.printStackTrace();
                        if ( db.isActive() ) try { db.rollback(); } catch ( Exception ee ) {}
                        _errLeak = true;
                        break out;
                    }
                }

            } finally {
                isDone = true;
                try {
                    db.close();
                } catch ( PersistenceException e ) {
                    fail( e.toString() );
                }
            }
        }
        boolean isDone() {
            return isDone;
        }
    }
}


