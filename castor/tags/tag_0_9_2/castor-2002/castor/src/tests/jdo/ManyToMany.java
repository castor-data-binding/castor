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
 */
public class ManyToMany extends CWTestCase {

    private Database       _db;


    private Connection     _conn;


    private JDOCategory    _category;


    public ManyToMany( CWTestCategory category )
        throws CWClassConstructorException
    {
        super( "TC23", "ManyToMany" );
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
        try {
            TestManyGroup group;
            TestManyPerson person1;
            TestManyPerson person2;
            TestManyPerson temp;
            OQLQuery oql;
            OQLQuery oqlp;
            QueryResults enum;


            _db = _category.getDatabase( stream.verbose() );
            _conn = _category.getJDBCConnection(); 

            stream.writeVerbose( "Running..." );
            stream.writeVerbose( "" );

            _db.begin();

            // select an group and delete it, if it exist!
            oql = _db.getOQLQuery( "SELECT object FROM jdo.TestManyGroup object WHERE id = $1" );
            oql.bind( 1 );
            enum = oql.execute();
            if ( enum.hasMore() ) {
                group = (TestManyGroup) enum.next();
                stream.writeVerbose( "Retrieved object: " + group );
                _db.remove( group );
                stream.writeVerbose( "Deleted object: " + group );
            } else {
                stream.writeVerbose("Group 1 not found, creating new one");
            }

            oqlp = _db.getOQLQuery( "SELECT object FROM jdo.TestManyPerson object WHERE id = $1" );
            oqlp.bind( 100 );
            enum = oqlp.execute();
            if ( enum.hasMore() ) {
                person1 = (TestManyPerson) enum.next();
                stream.writeVerbose( "Retrieved object: " + person1 );
                _db.remove( person1 );
                stream.writeVerbose( "Deleted object: " + person1 );
            } else {
                stream.writeVerbose("Person 100 not found, creating new one");
            }

            oqlp.bind( 200 );
            enum = oqlp.execute();
            if ( enum.hasMore() ) {
                person1 = (TestManyPerson) enum.next();
                stream.writeVerbose( "Retrieved object: " + person1 );
                _db.remove( person1 );
                stream.writeVerbose( "Deleted object: " + person1 );
            } else {
                stream.writeVerbose("Person 200 not found, creating new one");
            }
            _db.commit();


            // create new group with two people
            _db.begin();
            stream.writeVerbose("Creating new group with people!");
            person1 = new TestManyPerson();
            person1.setValue1("I am person 1");
            ArrayList gPerson1 = new ArrayList();
            person1.setGroup( gPerson1 );
            person1.setId( 100 );
            person1.setSthelse("Something else");
            person1.setHelloworld("Hello World!");


            person2 = new TestManyPerson();
            person2.setValue1("I am person 2");
            ArrayList gPerson2 = new ArrayList();
            person2.setGroup( gPerson2 );
            person2.setId( 200 );
            person2.setSthelse("Something else");
            person2.setHelloworld("Hello World!");

            group = new TestManyGroup();
            group.setId( 1 );
            group.setValue1("Group A");
            ArrayList al = new ArrayList();
            al.add( person1 );
            al.add( person2 );
            group.setPeople( al );

            gPerson1.add( group );
            gPerson2.add( group );

            _db.create( group );
            _db.create( person1 );
            _db.create( person2 );

            stream.writeVerbose("object created: " + group);
            Collection ppl = group.getPeople();
            _db.commit();
                
            // load the object and modify it
            stream.writeVerbose("Load the objects and modify it");
            _db.begin();
            oql.bind( 1 );
            group = null;
            person1 = null;
            person2 = null;
            enum = oql.execute();
            if ( enum.hasMore() ) {
                group = (TestManyGroup) enum.next();
                stream.writeVerbose( "Retrieved object: " + group );
                Collection p = group.getPeople();
                if ( p != null ) {
                    Iterator itor = p.iterator();
                    if ( itor.hasNext() ) 
                        person1 = (TestManyPerson) itor.next();
                    if ( itor.hasNext() )
                        person2 = (TestManyPerson) itor.next();
                    if ( itor.hasNext() )
                        throw new Exception("Error: more people than expected!");
                
                    if ( person1 == null || person2 == null )
                        throw new Exception("Error: expect two people in group");

                    if ( person1.getId() == 200 && person2.getId() == 100 ) {
                        temp = person1;
                        person1 = person2;
                        person2 = temp;
                    }
                    
                    if (person1.getId() == 100 && person2.getId() == 200) {
                        // check if the value is valid for person1 and chnage value of person1
                        if ( person1.getValue1() == null || !person1.getValue1().equals("I am person 1") ) {
                            result = false;
                            throw new Exception("Error: unexpected person value");
                        } else {
                            person1.setValue1("New person 1 value");
                        }

                        // check if the value is valid for person1 and remove person2
                        if ( person2.getValue1() == null || !person2.getValue1().equals("I am person 2") ) {
                            result = false;
                            throw new Exception("Error: unexpected person value");
                        }
                        itor = p.iterator();
                        while ( itor.hasNext() ) {
                            person1 = (TestManyPerson) itor.next();
                            if ( person1.getId() == 200 )
                                itor.remove();
                        }
                    } else {
                        result = false;
                        throw new Exception("Error: people in group is not the same as expected!");
                    }
                } else {
                    result = false;
                    throw new Exception("Error: related object not found!");
                }
            } else {
                result = false;
                throw new Exception("Error: object not found!");
            }
            _db.commit();

            // load again to see if the changes done are effective
            stream.writeVerbose("Load the objects again to see if changes done are effective");
            _db.begin();
            oql.bind( 1 );
            group = null;
            person1 = null;
            person2 = null;
            enum = oql.execute();
            if ( enum.hasMore() ) {
                group = (TestManyGroup) enum.next();
                stream.writeVerbose( "Retrieved object: " + group );
                Collection p = group.getPeople();
                if ( p != null ) {
                    Iterator itor = p.iterator();
                    if ( itor.hasNext() ) 
                        person1 = (TestManyPerson) itor.next();
                    if ( itor.hasNext() ) {
                        throw new Exception("Error: more people than expected! 1:("+person1+") 2: ("+itor.next()+")");
                    }
                
                    if ( person1 == null )
                        throw new Exception("Error: expect person1 in group");

                    if ( person1.getId() == 100 ) {
                        // check if the value is valid for person1 and chnage value of person1
                        if ( person1.getValue1() == null || !person1.getValue1().equals("New person 1 value") ) {
                            result = false;
                            throw new Exception("Error: unexpected person value");
                        } 
                    } else {
                        result = false;
                        throw new Exception("Error: people in group is not the same as expected!");
                    }
                } else {
                    result = false;
                    throw new Exception("Error: related object not found!");
                }
            } else {
                result = false;
                throw new Exception("Error: object not found!");
            }
            _db.commit();

        } catch ( Exception e ) {
            result = false;
            e.printStackTrace();
        }

        return result;
    }
}


