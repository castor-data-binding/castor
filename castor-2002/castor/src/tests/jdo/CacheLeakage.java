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
import java.sql.ResultSet;
import java.lang.Math;
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
import org.exolab.castor.jdo.ObjectNotFoundException;
import org.exolab.castor.jdo.DuplicateIdentityException;
import org.exolab.jtf.CWVerboseStream;
import org.exolab.jtf.CWTestCase;
import org.exolab.jtf.CWTestCategory;
import org.exolab.exceptions.CWClassConstructorException;


/**
 * Concurrent access test. Tests a JDO modification and concurrent
 * JDBC modification to determine if JDO can detect the modification
 * with dirty checking.
 */
public class CacheLeakage extends CWTestCase {

    private final static int NUM_OF_CREATE_DELETE = 50;

    private final static int NUM_OF_READ = 200;

    private final static int NUM_OF_RETRIAL = 20;

    private final static int SLEEP_BASE_TIME = 10;

    private JDOCategory    _category;

    Database _db;

    Connection _conn;

    String _className;

    Class _classType;

    int _cacheType;

    boolean _errLeak;

    boolean _errCount;

    public CacheLeakage( CWTestCategory category )
        throws CWClassConstructorException
    {
        super( "TC17", "Cache leakage" );
        _category = (JDOCategory) category;
        _errLeak = false;
        _errCount = false;
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
        try {
            _db = _category.getDatabase( stream.verbose() );
            _conn = _category.getJDBCConnection();

            boolean result = true;
            for ( int i=0; i < 4; i++ ) {
                _cacheType = i;
                if ( !runOnce( stream ) )
                    result = false;
            }
            _db.close();
            _conn.close();
            if ( _errLeak )
                System.out.println("Element leak happened!");
            if ( _errCount )
                System.out.println("Sum do not match!");

            return result && !_errLeak && !_errCount;
        } catch ( Exception e ) {
            stream.write( "Error: "+ e );
            return false;
        }

    }

    public boolean runOnce( CWVerboseStream stream ) {
        OQLQuery      oql;
        TestObjectEx    object;
        Enumeration   enum;
        Database db2;

        boolean result = true;

        try {
            // clear the table
            int del = _conn.createStatement().executeUpdate( "DELETE FROM test_race" );
            stream.writeVerbose( "row deleted in table test_race: " + del );
            _conn.commit();

            switch ( _cacheType ) {
            case 0:
                _className = "jdo.TestRaceCount";
                _classType = jdo.TestRaceCount.class;
                break;
            case 1:
                _className = "jdo.TestRaceTime";
                _classType = jdo.TestRaceTime.class;
                break;
            case 2:
                _className = "jdo.TestRaceNone";
                _classType = jdo.TestRaceNone.class;
                break;
            case 3:
                _className = "jdo.TestRaceUnlimited";
                _classType = jdo.TestRaceUnlimited.class;
                break;
            }

            CreateDeleteThread cdThread = new CreateDeleteThread( stream, _category, _cacheType, NUM_OF_CREATE_DELETE );

            ReadThread rThread =  new ReadThread( stream, cdThread, _category, NUM_OF_READ );

            cdThread.start();
            rThread.start();

            while ( !cdThread.isDone() /*&& !rThread.isDone()*/ ) {
                Thread.currentThread().sleep( 500 );
            }
            
            // create threads, make a race so each thread
            // keeping increment to the pairs of number.
        } catch ( Exception except ) {
            stream.writeVerbose( "Error: " + except );
            except.printStackTrace();
            result = false;
        }
        return result;
    }
    class ReadThread extends Thread {
        Database db;
        int trial;
        int cachetype;
        boolean isDone;
        Random ran;
        Integer id = new Integer(5);
        CWVerboseStream stream;
        CreateDeleteThread other;
        ReadThread( CWVerboseStream stream, CreateDeleteThread other, JDOCategory c, int n ) throws Exception {
            this.db = c.getDatabase( stream.verbose() );
            this.trial = n;
            this.stream = stream;
            this.ran = new Random();
            this.other = other;
        }
        public void run() {
            boolean succeed;
            int trials = 0;
            TestRace tr;
            try {
                for ( int i=0; i < trial && !other.isDone(); i++ ) {
                    try {
                        // load it and modify it
                        db.begin();
                        succeed = false;
                        trials = 0;
                        while ( !succeed && trials < NUM_OF_RETRIAL && !other.isDone() ) {
                            trials++;
                            try {
                                tr = (TestRace) db.load( _classType, id, Database.Shared );
                                                    // may throw ObjectNotFoundException
                                                    // LockNotGrantedException
                                db.commit();
                                succeed = true;
                            } catch ( LockNotGrantedException e ) {
                                succeed = false;
                                // ethernet way of retry
                                Thread.currentThread().sleep( (long) ((SLEEP_BASE_TIME^trials) * ran.nextDouble()) );
                            } catch ( ObjectNotFoundException e ) {
                                succeed = false;
                                // ethernet way of retry
                                Thread.currentThread().sleep( (long) ((SLEEP_BASE_TIME^trials) * ran.nextDouble()) );
                            } catch ( TransactionAbortedException e ) {
                                succeed = false;
                                // ethernet way of retry
                                Thread.currentThread().sleep( (long) ((SLEEP_BASE_TIME^trials) * ran.nextDouble()) );
                            }
                            Thread.currentThread().sleep( 0 );
                        }
                        if ( db.isActive() ) 
                            db.rollback();
                    
                    } catch ( Exception e ) {
                    }
                }
            } finally {
                isDone = true;
            }
        }
        boolean isDone() {
            return isDone;
        }
    }
    class CreateDeleteThread extends Thread {
        Database db;
        int trial;
        boolean isDone;
        Random ran;
        CWVerboseStream stream;
        int cachetype;

        CreateDeleteThread( CWVerboseStream stream, JDOCategory c, int cachetype, int n ) throws Exception {
            this.db = c.getDatabase( stream.verbose() );
            this.trial = n;
            this.stream = stream;
            this.ran = new Random();
            this.cachetype = cachetype;
        }
        public void run() {
            try {
                int num = 0;
                stream.writeVerbose("start testing");
                TestRace tr;
                TestRace testrace;
                OQLQuery oql;
                QueryResults qr;
                boolean succeed;
                int trials;
                Integer id = new Integer(5);

                out:
                for ( int i=0; i<trial; i++ ) {
                    // create, modified, delete object
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
     
                        // create object
                        //try {
                            db.begin();
                            db.create( testrace );  // may throw duplicateIdentityException
                            db.commit();
                        //} catch ( Exception e ) {
                        //    e.printStackTrace();
                        //}

                        // load it and modify it
                        succeed = false;
                        trials = 0;
                        while ( !succeed && trials < NUM_OF_RETRIAL ) {
                            Thread.currentThread().sleep( 0 );
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
                                if ( db.isActive() ) db.rollback();
                                Thread.currentThread().sleep( (long) ((SLEEP_BASE_TIME^trials) * ran.nextDouble()) );
                            } catch ( TransactionAbortedException e ) {
                                succeed = false;
                                // ethernet way of retry
                                if ( db.isActive() ) db.rollback();
                                Thread.currentThread().sleep( (long) ((SLEEP_BASE_TIME^trials) * ran.nextDouble()) );
                            } 
                        }
                        if ( db.isActive() ) 
                            db.rollback();

                        // load it and release it
                        succeed = false;
                        trials = 0;
                        while ( !succeed && trials < NUM_OF_RETRIAL ) {
                            Thread.currentThread().sleep( 0 );
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
                                if ( db.isActive() ) db.rollback();
                                Thread.currentThread().sleep( (long) ((SLEEP_BASE_TIME^trials) * ran.nextDouble()) );
                            } 
                        }
                        if ( db.isActive() ) 
                            db.rollback();

                        // load it and delete it
                        succeed = false;
                        trials = 0;
                        while ( !succeed && trials < NUM_OF_RETRIAL ) {
                            Thread.currentThread().sleep( 0 );
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
                                if ( db.isActive() ) db.rollback();
                                Thread.currentThread().sleep( (long) ((SLEEP_BASE_TIME^trials) * ran.nextDouble()) );
                            } catch ( TransactionAbortedException e ) {
                                succeed = false;
                                // ethernet way of retry
                                if ( db.isActive() ) db.rollback();
                                Thread.currentThread().sleep( (long) ((SLEEP_BASE_TIME^trials) * ran.nextDouble()) );
                            }
                        }
                        if ( db.isActive() ) 
                            db.rollback();
                        if ( !succeed )
                            throw new Exception("Transaction can't not lock the object within "+trials+" trials");

                    } catch ( TransactionNotInProgressException e ) {
                        stream.writeVerbose( "Thread <CreateDelete> will be killed. Unexcepted exception: "+e.getException() );
                        e.printStackTrace();
                        if ( db.isActive() ) try { db.rollback(); } catch ( TransactionNotInProgressException ee ) {}
                        _errLeak = true;
                        break out;
                    } catch ( PersistenceException e ) {
                        stream.writeVerbose( "Thread <CreateDelete> will be killed. Unexcepted exception: " );
                        e.printStackTrace();
                        if ( db.isActive() ) try { db.rollback(); } catch ( TransactionNotInProgressException ee ) {}
                        _errLeak = true;
                        break out;
                    } catch ( Exception e ) {
                        stream.writeVerbose( "Thread <CreateDelete> will be killed. Element not found: other exception: "+e );
                        e.printStackTrace();
                        if ( db.isActive() ) try { db.rollback(); } catch ( TransactionNotInProgressException ee ) {}
                        _errLeak = true;
                        break out;
                    }
                }

            } finally {
                isDone = true;
            }
        }
        boolean isDone() {
            return isDone;
        }
    }
}


