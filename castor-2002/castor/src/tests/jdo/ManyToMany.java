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
            TestManyGroup groupA, groupB;
            TestManyPerson person1;
            TestManyPerson person2;
            TestManyPerson person3;
            TestManyPerson person4;
            TestManyPerson temp;
            ArrayList al;
            OQLQuery oql;
            OQLQuery oqlp;
            QueryResults enum;
            int groupAId = 201, groupBId = 202;
            int person1Id = 1, person2Id = 2, person3Id = 3, person4Id = 4;


            _db = _category.getDatabase( stream.verbose() );
            _conn = _category.getJDBCConnection(); 

            stream.writeVerbose( "Running..." );
            stream.writeVerbose( "" );

            _db.begin();

            // select an group and delete it, if it exist!
            OQLQuery oqlclean = _db.getOQLQuery( "SELECT object FROM jdo.TestManyGroup object WHERE object.id < $1" );
            oqlclean.bind( Integer.MAX_VALUE );
            enum = oqlclean.execute();
            while ( enum.hasMore() ) {
                groupA = (TestManyGroup) enum.next();
                stream.writeVerbose( "Retrieved object: " + groupA );
                _db.remove( groupA );
                stream.writeVerbose( "Deleted object: " + groupA );
            }
            _db.commit();

            _db.begin();
            oqlclean = _db.getOQLQuery( "SELECT object FROM jdo.TestManyPerson object WHERE object.id < $1" );
            oqlclean.bind( Integer.MAX_VALUE );
            enum = oqlclean.execute();
            while ( enum.hasMore() ) {
                person1 = (TestManyPerson) enum.next();
                stream.writeVerbose( "Retrieved object: " + person1 );
                _db.remove( person1 );
                stream.writeVerbose( "Deleted object: " + person1 );
            } 
            _db.commit();


            // create new group with two people
            _db.begin();
            oql = _db.getOQLQuery( "SELECT object FROM jdo.TestManyGroup object WHERE id = $1" );
            oqlp = _db.getOQLQuery( "SELECT object FROM jdo.TestManyPerson object WHERE id = $1" );
            stream.writeVerbose("Creating new group with people!");
            person1 = new TestManyPerson();
            person1.setValue1("I am person 1");
            ArrayList gPerson1 = new ArrayList();
            person1.setId(person1Id);
            person1.setGroup( gPerson1 );
            person1.setSthelse("Something else");
            person1.setHelloworld("Hello World!");

            person2 = new TestManyPerson();
            person2.setValue1("I am person 2");
            ArrayList gPerson2 = new ArrayList();
            person2.setId(person2Id);
            person2.setGroup( gPerson2 );
            person2.setSthelse("Something else");
            person2.setHelloworld("Hello World!");

            person3 = new TestManyPerson();
            person3.setValue1("I am person 3");
            ArrayList gPerson3 = new ArrayList();
            person3.setId(person3Id);
            person3.setGroup( gPerson3 );
            person3.setSthelse("Something else for person 3");
            person3.setHelloworld("Hello World!");

            person4 = new TestManyPerson();
            person4.setValue1("I am person 4");
            ArrayList gPerson4 = new ArrayList();
            person4.setId(person4Id);
            person4.setGroup( gPerson4 );
            person4.setSthelse("Something else for person 4");
            person4.setHelloworld("Hello World!");

            groupA = new TestManyGroup();
            groupA.setValue1("Group A");
            al = new ArrayList();
            al.add( person1 );
            al.add( person2 );
            groupA.setId(groupAId);
            groupA.setPeople( al );

            groupB = new TestManyGroup();
            groupB.setValue1("Group B");
            groupB.setId(groupBId);
            gPerson1.add( groupA );
            gPerson2.add( groupA );

            _db.create( groupA );
            _db.create( person1 );
            _db.create( person2 );

            stream.writeVerbose("object created: " + groupA);
            Collection ppl = groupA.getPeople();
            _db.commit();

            // load the object and modify it
            stream.writeVerbose("Load the objects and modify it");
            _db.begin();
            oql.bind( groupAId );
            groupA = null;
            person1 = null;
            person2 = null;
            enum = oql.execute();
            if ( enum.hasMore() ) {
                groupA = (TestManyGroup) enum.next();
                stream.writeVerbose( "Retrieved object: " + groupA );
                Collection p = groupA.getPeople();
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

                    if ( person1.getId() == person2Id && person2.getId() == person1Id ) {
                        temp = person1;
                        person1 = person2;
                        person2 = temp;
                    }
                    
                    if ( person1.getId() == person1Id && person2.getId() == person2Id ) {
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
                        // remove person 2
                        itor = p.iterator();
                        while ( itor.hasNext() ) {
                            person2 = (TestManyPerson) itor.next();
                            if ( person2.getId() == person2Id ) {
                                itor.remove();
                                break;
                            }
                        }
                        // add person 3
                        groupA.getPeople().add( person3 );
                        person3.getGroup().add( groupA );
                        _db.create( person3 );
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
            if ( person3Id == person2Id )
                throw new Exception("Error: unexpected id swapping ocurrs!");

            // load again to see if the changes done are effective
            stream.writeVerbose("Load the objects again to see if changes done are effective");
            _db.begin();
            oql.bind( groupAId );
            groupA = null;
            person1 = null;
            person2 = null;
            enum = oql.execute();
            if ( enum.hasMore() ) {
                groupA = (TestManyGroup) enum.next();
                stream.writeVerbose( "Retrieved object: " + groupA );
                Collection p = groupA.getPeople();
                if ( p != null ) {
                    Iterator itor = p.iterator();
                    if ( itor.hasNext() ) 
                        person1 = (TestManyPerson) itor.next();
                    if ( itor.hasNext() )
                        person3 = (TestManyPerson) itor.next();

                    // swap if the order is wrong
                    if ( person1.getId() == person3Id && person3.getId() == person1Id ) {
                        temp = person1;
                        person1 = person3;
                        person3 = temp;
                    }
                    if ( itor.hasNext() ) {
                        throw new Exception("Error: more people than expected! 1:("+person1+") 2: ("+itor.next()+")");
                    }

                    if ( person1 == null )
                        throw new Exception("Error: expect person1 in group");

                    if ( person1.getId() == person1Id ) {
                        // check if the value is valid for person1 and chnage value of person1
                        if ( person1.getValue1() == null || !person1.getValue1().equals("New person 1 value") ) {
                            result = false;
                            throw new Exception("Error: unexpected person value");
                        }
                    } else {
                        result = false;
                        throw new Exception("Error: people in group is not the same as expected!");
                    }

                    if ( person3.getId() == person3Id ) {
                        // check if the value is valid for person1 and chnage value of person1
                        if ( person3.getValue1() == null || !person3.getValue1().equals("I am person 3") ) {
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

            // test long transaction support
            stream.writeVerbose("Modifing object outside of transaction");
            // remove person 3
            Iterator it = groupA.getPeople().iterator();
            while ( it.hasNext() ) {
                person3 = (TestManyPerson) it.next();
                if ( person3.getId() == person3Id ) {
                    it.remove();
                    break;
                }
            }
            person3.getGroup().clear();
            // add person 4
            groupA.getPeople().add( person4 );
            person4.getGroup().add( groupA );
            // find person 1
            person1 = null;
            it = groupA.getPeople().iterator();
            while ( it.hasNext() ) {
                person1 = (TestManyPerson) it.next();
                if ( person1.getId() == person1Id )
                    break;
            }
            person1.setValue1("New new value for person 1");

            stream.writeVerbose("Update object to a new transaction");
            _db.setAutoStore( true );
            _db.begin();
            _db.update( groupA );
            _db.commit();

            person4Id = person4.getId();
            // load again to see if the changes done are effective
            stream.writeVerbose("Load the objects again to see if changes done are effective");
            _db.begin();
            oql.bind( groupAId );
            groupA = null;
            person1 = null;
            person2 = null;
            enum = oql.execute();
            if ( enum.hasMore() ) {
                groupA = (TestManyGroup) enum.next();
                stream.writeVerbose( "Retrieved object: " + groupA );
                Collection p = groupA.getPeople();
                if ( p != null ) {
                    Iterator itor = p.iterator();
                    if ( itor.hasNext() ) 
                        person1 = (TestManyPerson) itor.next();
                    else 
                        throw new Exception("Erorr: less people than expected!");
                    if ( itor.hasNext() )
                        person4 = (TestManyPerson) itor.next();
                    else 
                        throw new Exception("Erorr: less people than expected!");

                    // swap if the order is wrong
                    if ( person1.getId() == person4Id && person4.getId() == person1Id ) {
                        temp = person1;
                        person1 = person4;
                        person4 = temp;
                    }
                    if ( itor.hasNext() ) {
                        throw new Exception("Error: more people than expected! 1:("+person1+") 2: ("+itor.next()+")");
                    }

                    if ( person1 == null )
                        throw new Exception("Error: expect person1 in group");

                    if ( person1.getId() == person1Id ) {
                        // check if the value is valid for person1 and chnage value of person1
                        if ( person1.getValue1() == null || !person1.getValue1().equals("New new value for person 1") ) {
                            result = false;
                            throw new Exception("Error: unexpected person value");
                        }
                    } else {
                        result = false;
                        throw new Exception("Error: people in group is not the same as expected!");
                    }

                    if ( person4.getId() == person4Id ) {
                        // check if the value is valid for person1 and chnage value of person1
                        if ( person4.getValue1() == null || !person4.getValue1().equals("I am person 4") ) {
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
            e.printStackTrace();
            if ( _db.isActive() )
                try {
                    _db.rollback();
                } catch ( Exception ee ) {
                }
            result = false;
        }

        return result;
    }
}


