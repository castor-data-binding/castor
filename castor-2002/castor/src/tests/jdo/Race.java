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
public class Race extends CWTestCase {

    private final static int NUM_OF_RACING_THREADS = 16;

    private final static int NUM_OF_VALUE_PAIRS = 4;

    private final static int NUM_OF_TRIALS = 5;

    private JDOCategory    _category;

    Database _db;

    Connection _conn;

    String _className;

    Class _classType;

    boolean _leak;

    public Race( CWTestCategory category )
        throws CWClassConstructorException
    {
        super( "TC18", "Race" );
        _category = (JDOCategory) category;
        _leak = false;
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
            _conn.setAutoCommit( false );

            boolean result = true;
            for ( int i=0; i < 4; i++ ) {
                if ( !runOnce( stream, i ) )
                    result = false;
            }
            _db.close();
            _conn.close();
            if ( _leak )
                System.out.println("Element leak happened!");
            return result && !_leak;
        } catch ( Exception e ) {
            stream.write( "Error: "+ e );
            return false;
        }

    }

    public boolean runOnce( CWVerboseStream stream, int cachetype ) {
        OQLQuery      oql;
        TestObjectEx    object;
        Enumeration   enum;
        Database db2;

        boolean result = true;

        try {
            //_db = _category.getDatabase( stream.verbose() );
            //_conn = _category.getJDBCConnection();


            // clear the table
            int del = _conn.createStatement().executeUpdate( "DELETE FROM test_race" );
            stream.writeVerbose( "row deleted in table test_race: " + del );
            _conn.commit();

            // create pairs of number
            _db.begin();
            TestRace[] jdos = new TestRace[NUM_OF_VALUE_PAIRS];
            TestRaceSyn[] controls = new TestRaceSyn[NUM_OF_VALUE_PAIRS];
            switch ( cachetype ) {
            case 0:
                _className = "jdo.TestRaceCount";
                _classType = jdo.TestRaceCount.class;
                for ( int i=0; i<jdos.length; i++ ) {
                    jdos[i] = new TestRaceCount();
                    jdos[i].setId(i);
                    _db.create( jdos[i] );
                    controls[i] = new TestRaceSyn();
                }
                break;
            case 1:
                _className = "jdo.TestRaceTime";
                _classType = jdo.TestRaceTime.class;
                for ( int i=0; i<jdos.length; i++ ) {
                    jdos[i] = new TestRaceTime();
                    jdos[i].setId(i);
                    _db.create( jdos[i] );
                    controls[i] = new TestRaceSyn();
                }
                break;
            case 2:
                _className = "jdo.TestRaceNone";
                _classType = jdo.TestRaceNone.class;
                for ( int i=0; i<jdos.length; i++ ) {
                    jdos[i] = new TestRaceNone();
                    jdos[i].setId(i);
                    _db.create( jdos[i] );
                    controls[i] = new TestRaceSyn();
                }
                break;
            case 3:
                _className = "jdo.TestRaceUnlimited";
                _classType = jdo.TestRaceUnlimited.class;
                for ( int i=0; i<jdos.length; i++ ) {
                    jdos[i] = new TestRaceUnlimited();
                    jdos[i].setId(i);
                    _db.create( jdos[i] );
                    controls[i] = new TestRaceSyn();
                }
                break;
            }

            try {
                _db.commit();
            } catch ( Exception e ) {
                e.printStackTrace();
                return false;
            }

            // create threads, make a race so each thread
            // keeping increment to the pairs of number.
            RaceThread[] ts = new RaceThread[NUM_OF_RACING_THREADS];

            for ( int i=0; i<ts.length; i++ ) {
                ts[i] = new RaceThread( stream, _category, controls, cachetype, NUM_OF_TRIALS );
                ts[i].start();
            }

            // wait till everybody done
            boolean isAllDone = false;
            int num;
            while ( !isAllDone ) {
                Thread.currentThread().sleep( 1000 );
                num = 0;
                for ( int i=0; i<ts.length; i++ ) {
                    if ( ts[i].isDone() ) {
                        num++;
                    }
                }
                if ( num == ts.length )
                    isAllDone = true;
            }

            // see if their sum agree
            _db.begin();
            num = 0;
            for ( int i=0; i<jdos.length; i++ ) {
                oql = _db.getOQLQuery( "SELECT object FROM "+_className+" object WHERE id = $1" );
                oql.bind( i );
                enum = oql.execute();
                if ( enum.hasMoreElements() ) {
                    TestRace tr = (TestRace) enum.nextElement();
                    if ( tr.getValue1() == controls[i].getValue1() )
                        num++;
                    stream.writeVerbose( "Number Pair "+i+" -- JDO: "+tr.getValue1()+" control: "
                        + controls[i].getValue1() + " total trials: " + NUM_OF_TRIALS * NUM_OF_RACING_THREADS );
                    stream.writeVerbose( "If Number Pair agree with control, then the test pass" );
                    stream.writeVerbose( "total trails is often not agree with the control. " +
                        "The reason is that if two thread interseted on the same object. only one will " +
                        "get the lock. and, the other abort." );
                }
            }
            _db.commit();

            // report result
            if ( num != jdos.length ) {
                result = false;
                stream.writeVerbose("Error: Racing condition occurs!");
            } else {
                stream.writeVerbose("Racing condition passed! :)");
            }
            //_db.close();
            //_conn.close();
        } catch ( Exception except ) {
            stream.writeVerbose( "Error: " + except );
            except.printStackTrace();
            result = false;
        }
        return result;
    }
    class RaceThread extends Thread {
        Database db;
        TestRaceSyn[] tr;
        int trial;
        int cachetype;
        boolean isDone;
        Random ran;
        CWVerboseStream stream;
        RaceThread( CWVerboseStream stream, JDOCategory c, TestRaceSyn[] tr, int cachetype, int n ) throws Exception {
            this.db = c.getDatabase( stream.verbose() );
            this.tr = tr;
            this.trial = n;
            this.stream = stream;
            this.ran = new Random();
            this.cachetype = cachetype;
        }
        public void run() {
            try {
                int num = 0;
                stream.writeVerbose("start testing");
                out:
                for ( int j=0; j<trial; j++ ) {
                    some:
                    for ( int i=0; i<tr.length; i++ ) {
                        boolean isOk = false;
                        int count = 0;

                        // select and inc the jdo object.
                        little:
                        while ( !isOk ) {
                            try {
                                if ( (i % 4) == 0 || (i % 4) == 1 ) {
                                    db.begin();
                                    OQLQuery oql = db.getOQLQuery( "SELECT object FROM "+_className+" object WHERE id = $1" );
                                    oql.bind( i );
                                    QueryResults enum = oql.execute();
                                    if ( enum.hasMore() ) {
                                        TestRace tr = (TestRace) enum.next();
                                        tr.incValue1();
                                        db.commit();
                                        isOk = true;
                                    } else {
                                        stream.writeVerbose("Error: "+" element not found!! missed in cache????\n");
                                        if ( db.isActive() ) try { db.rollback(); } catch ( Exception e ) {}
                                        throw new NoSuchElementException("No element found (a).");
                                    }
                                } else if ( (i % 4) == 10  ) {
                                    db.begin();
                                    OQLQuery oql = db.getOQLQuery( "SELECT object FROM "+_className+" object WHERE id = $1" );
                                    oql.bind( i );
                                    Enumeration enum = oql.execute();
                                    if ( enum.hasMoreElements() ) {
                                        TestRace tr = (TestRace) enum.nextElement();
                                        tr.incValue1();
                                        db.commit();
                                        isOk = true;
                                    } else {
                                        if ( db.isActive() ) try { db.rollback(); } catch ( Exception e ) {}
                                        throw new NoSuchElementException("No element found (b).");
                                    }
                                } else if ( (i % 4) == 2 ) {
                                    db.begin();
                                    stream.writeVerbose( "trying Database.load()" );
                                    TestRace tr = (TestRace) db.load( _classType, new Integer(i), Database.Shared );
                                    if ( tr != null ) {
                                        tr.incValue1();
                                        db.commit();
                                        isOk = true;
                                    } else {
                                        stream.writeVerbose("Error: "+" element not found!! missed in cache????\n");
                                        if ( db.isActive() ) try { db.rollback(); } catch ( Exception e ) {}
                                        throw new NoSuchElementException("No element found (c).");                                        
                                    }
                                } else if ( (i % 4 == 3 ) ) {
                                    db.begin();
                                    stream.writeVerbose( "trying Database.load()" );
                                    TestRace tr = (TestRace) db.load( _classType, new Integer(i), Database.Exclusive );
                                    if ( tr != null ) {
                                        tr.incValue1();
                                        db.commit();
                                        isOk = true;
                                    } else {
                                        stream.writeVerbose("Error: "+" element not found!! missed in cache????\n");
                                        if ( db.isActive() ) try { db.rollback(); } catch ( Exception e ) {}
                                        throw new NoSuchElementException("No element found (c).");                                        
                                    }
                                } else {
                                    throw new IllegalArgumentException("??????????????");
                                }
                            } catch ( TransactionAbortedException e ) {
                                count++;
                                // this exception should happen one in a while.
                                stream.writeVerbose( "Excepted exception: " + e );
                                if ( db.isActive() ) try { db.rollback(); } catch ( Exception ee ) {}
                                if ( count > 10 ) {
                                    break some;
                                }
                            } catch ( LockNotGrantedException e ) {
                                count++;
                                stream.writeVerbose( "Excepted exception: " + e);
                                if ( db.isActive() ) try { db.rollback(); } catch ( Exception ee ) {}
                                if ( count > 10 ) {
                                    break some;
                                }
                            } catch ( QueryException e ) {
                                stream.writeVerbose( "Thread will be killed. Unexcepted exception: " );
                                e.printStackTrace();
                                if ( db.isActive() ) try { db.rollback(); } catch ( Exception ee ) {}
                                _leak = true;
                                break out;
                            } catch ( TransactionNotInProgressException e ) {
                                stream.writeVerbose( "Thread will be killed. Unexcepted exception: " );
                                e.printStackTrace();
                                if ( db.isActive() ) try { db.rollback(); } catch ( Exception ee ) {}
                                _leak = true;
                                break out;
                            } catch ( PersistenceException e ) {
                                stream.writeVerbose( "Thread will be killed. Unexcepted exception: " );
                                e.printStackTrace();
                                if ( db.isActive() ) try { db.rollback(); } catch ( Exception ee ) {}
                                _leak = true;
                                break out;
                            } catch ( NoSuchElementException e ) {
                                stream.writeVerbose( "Thread will be killed. Element not found: entry leakage in cache" );
                                e.printStackTrace();
                                if ( db.isActive() ) try { db.rollback(); } catch ( Exception ee ) {}
                                _leak = true;
                                break out;
                            } catch ( Exception e ) {
                                stream.writeVerbose( "Thread will be killed. Element not found: other exception: "+e );
                                e.printStackTrace();
                                if ( db.isActive() ) try { db.rollback(); } catch ( Exception ee ) {}
                                _leak = true;
                                break out;
                            }
                        }

                        // inc the control value. (these objects are thread safe)
                        tr[i].incValue1();



                        // make some non-deterministicity. otherwise, we are just lining up
                        // thread and won't discover problem.
                        //if ( ran.nextDouble() < 0.3 ) {
                            try {
                                Thread.currentThread().sleep( (long) (100 * ran.nextDouble()) );
                            } catch ( InterruptedException e ) {
                                System.out.println(e);
                                break out;
                            }
                        //}
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
    class NoSuchElementException extends Exception {
        NoSuchElementException( String name ) {
            super( name );
        }
    }
}

