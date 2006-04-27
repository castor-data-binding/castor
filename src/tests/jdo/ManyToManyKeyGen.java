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
 */
package jdo;

import java.util.Iterator;
import java.util.Collection;
import java.util.ArrayList;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.QueryResults;
import org.exolab.castor.jdo.PersistenceException;

import harness.TestHarness;
import harness.CastorTestCase;

/**
 * Test for many to many relationship with key generators being used in both
 * side of the relationship. The tests create data objects, modify it and
 * verify the modification is persisted properly. These tests also test for
 * many to many relationship with long transaction support: it loads a
 * set of data objects in one transaction, modify it and update it into 
 * another transaction. 
 */
public class ManyToManyKeyGen extends CastorTestCase {

    private Database       _db;


    private JDOCategory    _category;

    /**
     * Constructor
     *
     * @param category The test suite of these test cases.
     */
    public ManyToManyKeyGen( TestHarness category ) {
        super( category, "tempTC31a", "ManyToManyKeyGen" );
        _category = (JDOCategory) category;
    }

    /**
     * Get a JDO database
     */
    public void setUp() 
            throws PersistenceException {
        _db = _category.getDatabase();
    }

    /**
     * The tests create data objects, modify it and
     * verify the modification is persisted properly. These tests also test for
     * many to many relationship with long transaction support: it loads a
     * set of data objects in one transaction, modify it and update it into 
     * another transaction. 
     */
    public void runTest() 
            throws PersistenceException {

        TestManyGroupKeyGen groupA, groupB;
        TestManyPersonKeyGen person1;
        TestManyPersonKeyGen person2;
        TestManyPersonKeyGen person3;
        TestManyPersonKeyGen person4;
        TestManyPersonKeyGen temp;
        ArrayList al, bl;
        OQLQuery oql;
        QueryResults enumeration;
        int groupAId, groupBId;
        int person1Id, person2Id, person3Id, person4Id;


        stream.println( "Running..." );
        stream.println( "" );

        _db.begin();

        // select an group and delete it, if it exist!
        OQLQuery oqlclean = _db.getOQLQuery( "SELECT object FROM jdo.TestManyGroupKeyGen object WHERE object.id < $1" );
        oqlclean.bind( Integer.MAX_VALUE );
        enumeration = oqlclean.execute();
        while ( enumeration.hasMore() ) {
            groupA = (TestManyGroupKeyGen) enumeration.next();
            stream.println( "Retrieved object: " + groupA );
            _db.remove( groupA );
            stream.println( "Deleted object: " + groupA );
        }
        _db.commit();

        _db.begin();
        oqlclean = _db.getOQLQuery( "SELECT object FROM jdo.TestManyPersonKeyGen object WHERE object.id < $1" );
        oqlclean.bind( Integer.MAX_VALUE );
        enumeration = oqlclean.execute();
        while ( enumeration.hasMore() ) {
            person1 = (TestManyPersonKeyGen) enumeration.next();
            stream.println( "Retrieved object: " + person1 );
            _db.remove( person1 );
            stream.println( "Deleted object: " + person1 );
        } 
        _db.commit();


        // create new group and new people, don't link them yet.
        // This test for null collection handling
        _db.begin();
        oql = _db.getOQLQuery( "SELECT object FROM jdo.TestManyGroupKeyGen object WHERE id = $1" );
        stream.println("Creating new group with people!");
        person1 = new TestManyPersonKeyGen();
        person1.setValue1("I am person 1");
        person1.setGroup( null );
        person1.setSthelse("Something else");
        person1.setHelloworld("Hello World!");
        _db.create( person1 );
        _db.commit();
        person1Id = person1.getId();

        // create new group with two people
        _db.begin();
        oql = _db.getOQLQuery( "SELECT object FROM jdo.TestManyGroupKeyGen object WHERE id = $1" );
        stream.println("Creating new group with people!");
        person1 = (TestManyPersonKeyGen) _db.load( TestManyPersonKeyGen.class, new Integer(person1Id) );
        person1.setValue1("I am person 1");
        ArrayList gPerson1 = new ArrayList();
        person1.setGroup( gPerson1 );
        person1.setSthelse("Something else");
        person1.setHelloworld("Hello World!");

        person2 = new TestManyPersonKeyGen();
        person2.setValue1("I am person 2");
        ArrayList gPerson2 = new ArrayList();
        person2.setGroup( gPerson2 );
        person2.setSthelse("Something else");
        person2.setHelloworld("Hello World!");

        person3 = new TestManyPersonKeyGen();
        person3.setValue1("I am person 3");
        ArrayList gPerson3 = new ArrayList();
        person3.setGroup( gPerson3 );
        person3.setSthelse("Something else for person 3");
        person3.setHelloworld("Hello World!");

        person4 = new TestManyPersonKeyGen();
        person4.setValue1("I am person 4");
        ArrayList gPerson4 = new ArrayList();
        person4.setGroup( gPerson4 );
        person4.setSthelse("Something else for person 4");
        person4.setHelloworld("Hello World!");

        groupA = new TestManyGroupKeyGen();
        groupA.setValue1("Group A");
        al = new ArrayList();
        al.add( person1 );
        al.add( person2 );
        groupA.setPeople( al );

        groupB = new TestManyGroupKeyGen();
        groupB.setValue1("Group B");
        bl = new ArrayList();
        bl.add( person2 );
        groupB.setPeople( bl );
        gPerson1.add( groupA );
        gPerson2.add( groupA );
        gPerson2.add( groupB );

        _db.create( groupA );
        _db.create( person2 );
        _db.create( groupB );

        stream.println("object created: " + groupA);
        _db.commit();

        groupAId = groupA.getId();
        groupBId = groupB.getId();
        person1Id = person1.getId();
        person2Id = person2.getId();

        // load the object and modify it
        stream.println("Load the objects and modify it");
        _db.begin();
        oql.bind( groupAId );
        groupA = null;
        person1 = null;
        person2 = null;
        enumeration = oql.execute();
        if ( enumeration.hasMore() ) {
            groupA = (TestManyGroupKeyGen) enumeration.next();
            stream.println( "Retrieved object: " + groupA );
            Collection p = groupA.getPeople();
            if ( p != null ) {
                Iterator itor = p.iterator();
                if ( itor.hasNext() ) 
                    person1 = (TestManyPersonKeyGen) itor.next();
                if ( itor.hasNext() )
                    person2 = (TestManyPersonKeyGen) itor.next();
                if ( itor.hasNext() )
                    fail("Error: more people than expected!");
            
                if ( person1 == null || person2 == null )
                    fail("Error: expect two people in group");

                if ( person1.getId() == person2Id && person2.getId() == person1Id ) {
                    temp = person1;
                    person1 = person2;
                    person2 = temp;
                }
                
                if ( person1.getId() == person1Id && person2.getId() == person2Id ) {
                    // check if the value is valid for person1 and chnage value of person1
                    if ( person1.getValue1() == null || !person1.getValue1().equals("I am person 1") ) {
                        fail("Error: unexpected person value");
                    } else {
                        person1.setValue1("New person 1 value");
                    }

                    // check if the value is valid for person1 and remove person2
                    if ( person2.getValue1() == null || !person2.getValue1().equals("I am person 2") ) {
                        fail("Error: unexpected person value");
                    }

                    // make sure person 2 contains 2 groups
                    if ( person2.getGroup() == null || person2.getGroup().size() != 2 ) {
                        fail("Error: expected group not found [2]" );
                    }
                    Iterator groupItor = person2.getGroup().iterator();

                    groupItor.hasNext();
                    TestManyGroupKeyGen tempGroup = (TestManyGroupKeyGen)groupItor.next();
                    int tempId = tempGroup.getId();
                    if ( tempId != groupAId && tempId != groupBId )
                        fail( "Error: unexpect group found" );

                    groupItor.hasNext();
                    tempGroup = (TestManyGroupKeyGen)groupItor.next();
                    if ( tempGroup.getId() == tempId )
                        fail ( "Error: duplicated group found" );
                    if ( tempGroup.getId() != groupAId && tempGroup.getId() != groupBId )
                        fail( "Error: unexpect group found" );

                    // remove person 2
                    itor = p.iterator();
                    while ( itor.hasNext() ) {
                        person2 = (TestManyPersonKeyGen) itor.next();
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
                    fail("Error: people in group is not the same as expected!");
                }
            } else {
                fail("Error: related object not found!");
            }
        } else {
            fail("Error: object not found!");
        }
        _db.commit();
        person3Id = person3.getId();
        if ( person3Id == person2Id )
            fail("Error: unexpected id swapping ocurrs!");

        // load again to see if the changes done are effective
        stream.println("Load the objects again to see if changes done are effective");
        _db.begin();
        oql.bind( groupAId );
        groupA = null;
        person1 = null;
        person2 = null;
        enumeration = oql.execute();
        if ( enumeration.hasMore() ) {
            groupA = (TestManyGroupKeyGen) enumeration.next();
            stream.println( "Retrieved object: " + groupA );
            Collection p = groupA.getPeople();
            if ( p != null ) {
                Iterator itor = p.iterator();
                if ( itor.hasNext() ) 
                    person1 = (TestManyPersonKeyGen) itor.next();
                if ( itor.hasNext() )
                    person3 = (TestManyPersonKeyGen) itor.next();

                // swap if the order is wrong
                if ( person1.getId() == person3Id && person3.getId() == person1Id ) {
                    temp = person1;
                    person1 = person3;
                    person3 = temp;
                }
                if ( itor.hasNext() ) {
                    fail("Error: more people than expected! 1:("+person1+") 2: ("+itor.next()+")");
                }

                if ( person1 == null )
                    fail("Error: expect person1 in group");

                if ( person1.getId() == person1Id ) {
                    // check if the value is valid for person1 and chnage value of person1
                    if ( person1.getValue1() == null || !person1.getValue1().equals("New person 1 value") ) {
                        fail("Error: unexpected person value");
                    }
                } else {
                    fail("Error: people in group is not the same as expected!");
                }

                if ( person3.getId() == person3Id ) {
                    // check if the value is valid for person1 and chnage value of person1
                    if ( person3.getValue1() == null || !person3.getValue1().equals("I am person 3") ) {
                        fail("Error: unexpected person value");
                    }
                } else {
                    fail("Error: people in group is not the same as expected!");
                }

            } else {
                fail("Error: related object not found!");
            }
        } else {
            fail("Error: object not found!");
        }

        // check if person 2 contains only one group, and the group is B
        person2 = (TestManyPersonKeyGen)_db.load( TestManyPersonKeyGen.class, new Integer(person2Id) );
        // make sure person 2 contains 2 groups
        if ( person2.getGroup() == null || person2.getGroup().size() != 1 ) {
            fail("Error: expected group not found [3]" );
        }

        Iterator groupItor = person2.getGroup().iterator();
        groupItor.hasNext();
        TestManyGroupKeyGen tempGroup = (TestManyGroupKeyGen)groupItor.next();
        if ( tempGroup.getId() != groupBId )
            fail( "Error: unexpected group found [1]: "+tempGroup.getId() );

        // remove all group from person2
        person2.setGroup( null );
        _db.commit();

        _db.begin();
        // check if person 2 contains no group
        person2 = (TestManyPersonKeyGen)_db.load( TestManyPersonKeyGen.class, new Integer(person2Id) );
        if ( person2.getGroup() != null && person2.getGroup().size() != 0 ) {
            fail("Error: expected group not found [1]" );
        }
        _db.remove( person2 );
        _db.commit();

        _db.begin();
        // check if group a and group b contains no person2
        groupA = (TestManyGroupKeyGen)_db.load( TestManyGroupKeyGen.class, new Integer(groupAId) );
        groupItor = groupA.getPeople().iterator();
        while ( groupItor.hasNext() ) {
            person2 = (TestManyPersonKeyGen) groupItor.next();
            if ( person2.getId() == person2Id )
                fail("Error: person2 is not removed");
        }
        groupB = (TestManyGroupKeyGen)_db.load( TestManyGroupKeyGen.class, new Integer(groupBId) );
        if ( groupB.getPeople() != null && groupB.getPeople().size() != 0 )
            fail("Error: person2 is not removed");

        // make a dangerous add (add to only one side)
        // user shouldn't rely on this behavior, but 
        // should always link both side before commit
        person1 = (TestManyPersonKeyGen)_db.load( TestManyPersonKeyGen.class, new Integer(person1Id) );
        person1.getGroup().add(groupB);
        _db.commit();

        // check if adding group into existing collection work
        _db.begin();
        person1 = (TestManyPersonKeyGen)_db.load( TestManyPersonKeyGen.class, new Integer(person1Id) );
        Iterator tempItor = person1.getGroup().iterator();
        if ( !tempItor.hasNext() )
            fail("Error: expected group from person1 not found");
        groupA = (TestManyGroupKeyGen)tempItor.next();
        int tempGroupId = groupA.getId();
        if ( tempGroupId != groupAId && tempGroupId != groupBId )
            fail("Error: unexpected group from person1 found");

        if ( !tempItor.hasNext() )
            fail("Error: expected group from person1 not found");
        groupA = (TestManyGroupKeyGen)tempItor.next();
        if ( tempGroupId == groupA.getId() )
            fail("Error: duplicated group found!");
        if ( groupA.getId() != groupAId && groupA.getId() != groupBId )
            fail("Error: unexpected group from person1 found");
        _db.commit();

        // test long transaction support
        _db.begin();
        groupA = (TestManyGroupKeyGen)_db.load( TestManyGroupKeyGen.class, new Integer(groupAId) );
        _db.commit();

        stream.println("Modifing object outside of transaction");
        // remove person 3
        Iterator it = groupA.getPeople().iterator();
        while ( it.hasNext() ) {
            person3 = (TestManyPersonKeyGen) it.next();
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
            person1 = (TestManyPersonKeyGen) it.next();
            if ( person1.getId() == person1Id )
                break;
        }
        person1.setValue1("New new value for person 1");

        stream.println("Update object to a new transaction");
        _db.setAutoStore( true );
        _db.begin();
        _db.update( groupA );
        _db.commit();

        person4Id = person4.getId();
        // load again to see if the changes done are effective
        stream.println("Load the objects again to see if changes done are effective");
        _db.begin();
        oql.bind( groupAId );
        groupA = null;
        person1 = null;
        person2 = null;
        enumeration = oql.execute();
        if ( enumeration.hasMore() ) {
            groupA = (TestManyGroupKeyGen) enumeration.next();
            stream.println( "Retrieved object: " + groupA );
            Collection p = groupA.getPeople();
            if ( p != null ) {
                Iterator itor = p.iterator();
                if ( itor.hasNext() ) 
                    person1 = (TestManyPersonKeyGen) itor.next();
                else 
                    fail("Erorr: less people than expected!");
                if ( itor.hasNext() )
                    person4 = (TestManyPersonKeyGen) itor.next();
                else 
                    fail("Erorr: less people than expected!");

                // swap if the order is wrong
                if ( person1.getId() == person4Id && person4.getId() == person1Id ) {
                    temp = person1;
                    person1 = person4;
                    person4 = temp;
                }
                if ( itor.hasNext() ) {
                    fail("Error: more people than expected! 1:("+person1+") 2: ("+itor.next()+")");
                }

                if ( person1 == null )
                    fail("Error: expect person1 in group");

                if ( person1.getId() == person1Id ) {
                    // check if the value is valid for person1 and chnage value of person1
                    if ( person1.getValue1() == null || !person1.getValue1().equals("New new value for person 1") ) {
                        fail("Error: unexpected person value");
                    }
                } else {
                    fail("Error: people in group is not the same as expected!");
                }

                if ( person4.getId() == person4Id ) {
                    // check if the value is valid for person1 and chnage value of person1
                    if ( person4.getValue1() == null || !person4.getValue1().equals("I am person 4") ) {
                        fail("Error: unexpected person value");
                    }
                } else {
                    fail("Error: people in group is not the same as expected!");
                }

            } else {
                fail("Error: related object not found!");
            }
        } else {
            fail("Error: object not found!");
        }
        person3 = (TestManyPersonKeyGen)_db.load( TestManyPersonKeyGen.class, new Integer(person3Id) );
        _db.commit();

        // modify and commit to long trans
        groupA.getPeople().add( person3 );
        person3.getGroup().add( groupA );
        _db.begin();
        _db.update( groupA );
        _db.commit();

        // load and check
        _db.begin();
        person3 = (TestManyPersonKeyGen)_db.load( TestManyPersonKeyGen.class, new Integer(person3Id) );
        tempItor = person3.getGroup().iterator();
        if ( !tempItor.hasNext() )
            fail( "Error: group not found" );
        groupA = (TestManyGroupKeyGen)tempItor.next();
        if ( groupA.getId() != groupAId )
            fail( "Error: unexpected group found" );
        if ( tempItor.hasNext() )
            fail( "Error: too many group" );
        _db.commit();
    }

    /**
     * Close the JDO database
     */
    public void tearDown() 
            throws PersistenceException {
        if ( _db.isActive() ) _db.rollback();
        _db.close();
    }
}


