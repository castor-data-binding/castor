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
package ctf.jdo.tc7x;

import harness.CastorTestCase;
import harness.TestHarness;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import jdo.JDOCategory;

import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.QueryResults;

/**
 * Test for many to many relationship with key generators being used in both
 * side of the relationship. The tests create data objects, modify it and
 * verify the modification is persisted properly. These tests also test for
 * many to many relationship with long transaction support: it loads a
 * set of data objects in one transaction, modify it and update it into 
 * another transaction. 
 */
public final class TestManyToManyKeyGen extends CastorTestCase {
    private Database _db;
    private JDOCategory _category;
    private OQLQuery _oql;
    private ManyPersonKeyGen _person1;
    private ManyPersonKeyGen _person2;
    private ManyPersonKeyGen _person3;
    private ManyPersonKeyGen _person4;
    private ManyGroupKeyGen _groupA;
    private ManyGroupKeyGen _groupB;
    private int _person1Id;
    private int _person2Id;
    private int _person3Id;
    private int _person4Id;
    private int _groupAId;
    private int _groupBId;

    /**
     * Constructor
     *
     * @param category The test suite of these test cases.
     */
    public TestManyToManyKeyGen(final TestHarness category) {
        super(category, "TC74", "ManyToManyKeyGen");
        _category = (JDOCategory) category;
    }

    /**
     * Get a JDO database
     */
    public void setUp() throws PersistenceException {
        _db = _category.getDatabase();
    }

    /**
     * The tests create data objects, modify it and
     * verify the modification is persisted properly. These tests also test for
     * many to many relationship with long transaction support: it loads a
     * set of data objects in one transaction, modify it and update it into 
     * another transaction. 
     */
    public void runTest() throws PersistenceException {
        stream.println("Running...");
        stream.println("");

        deleteGroups();
        deletePersons();
        create();
        check1();
        check2();
        check3();
        check4();
        check5();
        check6();
        check7();
        check8();
    }

    private void deleteGroups() throws PersistenceException {
        _db.begin();
        OQLQuery oqlclean = _db.getOQLQuery("SELECT object FROM " 
                + ManyGroupKeyGen.class.getName() + " object WHERE object.id < $1");
        oqlclean.bind(Integer.MAX_VALUE);
        QueryResults enumeration = oqlclean.execute();
        while (enumeration.hasMore()) {
            _groupA = (ManyGroupKeyGen) enumeration.next();
            stream.println("Retrieved object: " + _groupA);
            _db.remove(_groupA);
            stream.println("Deleted object: " + _groupA);
        }
        _db.commit();
    }
    
    private void deletePersons() throws PersistenceException {
        _db.begin();
        OQLQuery oqlclean = _db.getOQLQuery("SELECT object FROM " 
                + ManyPersonKeyGen.class.getName() + " object WHERE object.id < $1");
        oqlclean.bind(Integer.MAX_VALUE);
        QueryResults enumeration = oqlclean.execute();
        while (enumeration.hasMore()) {
            _person1 = (ManyPersonKeyGen) enumeration.next();
            stream.println("Retrieved object: " + _person1);
            _db.remove(_person1);
            stream.println("Deleted object: " + _person1);
        } 
        _db.commit();
    }
    
    private void create() throws PersistenceException {
        // create new group and new people, don't link them yet.
        // This test for null collection handling
        _db.begin();
        _oql = _db.getOQLQuery("SELECT object FROM " 
                + ManyGroupKeyGen.class.getName() + " object WHERE id = $1");
        stream.println("Creating new group with people!");
        _person1 = new ManyPersonKeyGen();
        _person1.setValue1("I am person 1");
        _person1.setGroup(null);
        _person1.setSthelse("Something else");
        _person1.setHelloworld("Hello World!");
        _db.create(_person1);
        _db.commit();
        _person1Id = _person1.getId();

        // create new group with two people
        _db.begin();
        _oql = _db.getOQLQuery("SELECT object FROM " 
                + ManyGroupKeyGen.class.getName() + " object WHERE id = $1");
        stream.println("Creating new group with people!");
        _person1 = (ManyPersonKeyGen) _db.load(
                ManyPersonKeyGen.class, new Integer(_person1Id));
        _person1.setValue1("I am person 1");
        ArrayList gPerson1 = new ArrayList();
        _person1.setGroup(gPerson1);
        _person1.setSthelse("Something else");
        _person1.setHelloworld("Hello World!");

        _person2 = new ManyPersonKeyGen();
        _person2.setValue1("I am person 2");
        ArrayList gPerson2 = new ArrayList();
        _person2.setGroup(gPerson2);
        _person2.setSthelse("Something else");
        _person2.setHelloworld("Hello World!");

        _person3 = new ManyPersonKeyGen();
        _person3.setValue1("I am person 3");
        ArrayList gPerson3 = new ArrayList();
        _person3.setGroup(gPerson3);
        _person3.setSthelse("Something else for person 3");
        _person3.setHelloworld("Hello World!");

        _person4 = new ManyPersonKeyGen();
        _person4.setValue1("I am person 4");
        ArrayList gPerson4 = new ArrayList();
        _person4.setGroup(gPerson4);
        _person4.setSthelse("Something else for person 4");
        _person4.setHelloworld("Hello World!");

        _groupA = new ManyGroupKeyGen();
        _groupA.setValue1("Group A");
        ArrayList al = new ArrayList();
        al.add(_person1);
        al.add(_person2);
        _groupA.setPeople(al);

        _groupB = new ManyGroupKeyGen();
        _groupB.setValue1("Group B");
        ArrayList bl = new ArrayList();
        bl.add(_person2);
        _groupB.setPeople(bl);
        gPerson1.add(_groupA);
        gPerson2.add(_groupA);
        gPerson2.add(_groupB);

        _db.create(_groupA);
        _db.create(_person2);
        _db.create(_groupB);

        stream.println("object created: " + _groupA);
        _db.commit();

        _groupAId = _groupA.getId();
        _groupBId = _groupB.getId();
        _person1Id = _person1.getId();
        _person2Id = _person2.getId();
    }
    
    private void check1() throws PersistenceException {
        stream.println("Load the objects and modify it");
        _db.begin();
        _oql.bind(_groupAId);
        _groupA = null;
        _person1 = null;
        _person2 = null;
        QueryResults enumeration = _oql.execute();
        if (enumeration.hasMore()) {
            _groupA = (ManyGroupKeyGen) enumeration.next();
            stream.println("Retrieved object: " + _groupA);
            Collection p = _groupA.getPeople();
            if (p != null) {
                Iterator itor = p.iterator();
                if (itor.hasNext()) { _person1 = (ManyPersonKeyGen) itor.next(); }
                if (itor.hasNext()) { _person2 = (ManyPersonKeyGen) itor.next(); }
                if (itor.hasNext()) { fail("Error: more people than expected!"); }
            
                if ((_person1 == null) || (_person2 == null)) {
                    fail("Error: expect two people in group");
                }

                if ((_person1.getId() == _person2Id)
                        && (_person2.getId() == _person1Id)) {
                    ManyPersonKeyGen temp = _person1;
                    _person1 = _person2;
                    _person2 = temp;
                }
                
                if ((_person1.getId() == _person1Id)
                        && (_person2.getId() == _person2Id)) {
                    // check if the value is valid for person1 and chnage value of person1
                    if ((_person1.getValue1() == null)
                            || !_person1.getValue1().equals("I am person 1")) {
                        fail("Error: unexpected person value");
                    } else {
                        _person1.setValue1("New person 1 value");
                    }

                    // check if the value is valid for person1 and remove person2
                    if ((_person2.getValue1() == null)
                            || !_person2.getValue1().equals("I am person 2")) {
                        fail("Error: unexpected person value");
                    }

                    // make sure person 2 contains 2 groups
                    if ((_person2.getGroup() == null)
                            || (_person2.getGroup().size() != 2)) {
                        fail("Error: expected group not found [2]");
                    }
                    Iterator groupItor = _person2.getGroup().iterator();

                    groupItor.hasNext();
                    ManyGroupKeyGen tempGroup = (ManyGroupKeyGen) groupItor.next();
                    int tempId = tempGroup.getId();
                    if ((tempId != _groupAId) && (tempId != _groupBId)) {
                        fail("Error: unexpect group found");
                    }

                    groupItor.hasNext();
                    tempGroup = (ManyGroupKeyGen) groupItor.next();
                    if (tempGroup.getId() == tempId) {
                        fail("Error: duplicated group found");
                    }
                    if ((tempGroup.getId() != _groupAId)
                            && (tempGroup.getId() != _groupBId)) {
                        fail("Error: unexpect group found");
                    }

                    // remove person 2
                    itor = p.iterator();
                    while (itor.hasNext()) {
                        _person2 = (ManyPersonKeyGen) itor.next();
                        if (_person2.getId() == _person2Id) {
                            itor.remove();
                            break;
                        }
                    }
                    // add person 3
                    _groupA.getPeople().add(_person3);
                    _person3.getGroup().add(_groupA);
                    _db.create(_person3);
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
        _person3Id = _person3.getId();
        if (_person3Id == _person2Id) { fail("Error: unexpected id swapping ocurrs!"); }
    }
    
    private void check2() throws PersistenceException {
        stream.println("Load the objects again to see if changes done are effective");
        _db.begin();
        _oql.bind(_groupAId);
        _groupA = null;
        _person1 = null;
        _person2 = null;
        QueryResults enumeration = _oql.execute();
        if (enumeration.hasMore()) {
            _groupA = (ManyGroupKeyGen) enumeration.next();
            stream.println("Retrieved object: " + _groupA);
            Collection p = _groupA.getPeople();
            if (p != null) {
                Iterator itor = p.iterator();
                if (itor.hasNext()) { _person1 = (ManyPersonKeyGen) itor.next(); }
                if (itor.hasNext()) { _person3 = (ManyPersonKeyGen) itor.next(); }

                // swap if the order is wrong
                if ((_person1.getId() == _person3Id)
                        && (_person3.getId() == _person1Id)) {
                    ManyPersonKeyGen temp = _person1;
                    _person1 = _person3;
                    _person3 = temp;
                }
                if (itor.hasNext()) {
                    fail("Error: more people than expected! "
                            + "1:(" + _person1 + ") 2: (" + itor.next() + ")");
                }

                if (_person1 == null) { fail("Error: expect person1 in group"); }

                if (_person1.getId() == _person1Id) {
                    // check if the value is valid for person1 and chnage value of person1
                    if ((_person1.getValue1() == null)
                            || !_person1.getValue1().equals("New person 1 value")) {
                        fail("Error: unexpected person value");
                    }
                } else {
                    fail("Error: people in group is not the same as expected!");
                }

                if (_person3.getId() == _person3Id) {
                    // check if the value is valid for person1 and chnage value of person1
                    if ((_person3.getValue1() == null)
                            || !_person3.getValue1().equals("I am person 3")) {
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
        _person2 = (ManyPersonKeyGen) _db.load(
                ManyPersonKeyGen.class, new Integer(_person2Id));
        // make sure person 2 contains 2 groups
        if ((_person2.getGroup() == null) || (_person2.getGroup().size() != 1)) {
            fail("Error: expected group not found [3]");
        }

        Iterator groupItor = _person2.getGroup().iterator();
        groupItor.hasNext();
        ManyGroupKeyGen tempGroup = (ManyGroupKeyGen) groupItor.next();
        if (tempGroup.getId() != _groupBId) {
            fail("Error: unexpected group found [1]: " + tempGroup.getId());
        }

        // remove all group from person2
        _person2.setGroup(null);
        _db.commit();
    }
    
    private void check3() throws PersistenceException {
        _db.begin();
        // check if person 2 contains no group
        _person2 = (ManyPersonKeyGen) _db.load(
                ManyPersonKeyGen.class, new Integer(_person2Id));
        if ((_person2.getGroup() != null) && (_person2.getGroup().size() != 0)) {
            fail("Error: expected group not found [1]");
        }
        _db.remove(_person2);
        _db.commit();
    }
    
    private void check4() throws PersistenceException {
        _db.begin();
        // check if group a and group b contains no person2
        _groupA = (ManyGroupKeyGen) _db.load(
                ManyGroupKeyGen.class, new Integer(_groupAId));
        Iterator groupItor = _groupA.getPeople().iterator();
        while (groupItor.hasNext()) {
            _person2 = (ManyPersonKeyGen) groupItor.next();
            if (_person2.getId() == _person2Id) { fail("Error: person2 is not removed"); }
        }
        _groupB = (ManyGroupKeyGen) _db.load(
                ManyGroupKeyGen.class, new Integer(_groupBId));
        if ((_groupB.getPeople() != null) && (_groupB.getPeople().size() != 0)) {
            fail("Error: person2 is not removed");
        }

        // make a dangerous add (add to only one side)
        // user shouldn't rely on this behavior, but 
        // should always link both side before commit
        _person1 = (ManyPersonKeyGen) _db.load(
                ManyPersonKeyGen.class, new Integer(_person1Id));
        _person1.getGroup().add(_groupB);
        _db.commit();
    }
    
    private void check5() throws PersistenceException {
        // check if adding group into existing collection work
        _db.begin();
        _person1 = (ManyPersonKeyGen) _db.load(
                ManyPersonKeyGen.class, new Integer(_person1Id));
        Iterator tempItor = _person1.getGroup().iterator();
        if (!tempItor.hasNext()) {
            fail("Error: expected group from person1 not found");
        }
        _groupA = (ManyGroupKeyGen) tempItor.next();
        int tempGroupId = _groupA.getId();
        if ((tempGroupId != _groupAId) && (tempGroupId != _groupBId)) {
            fail("Error: unexpected group from person1 found");
        }

        if (!tempItor.hasNext()) { fail("Error: expected group from person1 not found"); }
        _groupA = (ManyGroupKeyGen) tempItor.next();
        if (tempGroupId == _groupA.getId()) { fail("Error: duplicated group found!"); }
        if ((_groupA.getId() != _groupAId) && (_groupA.getId() != _groupBId)) {
            fail("Error: unexpected group from person1 found");
        }
        _db.commit();
    }
    
    private void check6() throws PersistenceException {
        // test long transaction support
        _db.begin();
        _groupA = (ManyGroupKeyGen) _db.load(
                ManyGroupKeyGen.class, new Integer(_groupAId));
        _db.commit();

        stream.println("Modifing object outside of transaction");
        // remove person 3
        Iterator it = _groupA.getPeople().iterator();
        while (it.hasNext()) {
            _person3 = (ManyPersonKeyGen) it.next();
            if (_person3.getId() == _person3Id) {
                it.remove();
                break;
            }
        }
        _person3.getGroup().clear();
        // add person 4
        _groupA.getPeople().add(_person4);
        _person4.getGroup().add(_groupA);
        // find person 1
        _person1 = null;
        it = _groupA.getPeople().iterator();
        while (it.hasNext()) {
            _person1 = (ManyPersonKeyGen) it.next();
            if (_person1.getId() == _person1Id) { break; }
        }
        _person1.setValue1("New new value for person 1");

        stream.println("Update object to a new transaction");
        _db.setAutoStore(true);
        _db.begin();
        _db.update(_groupA);
        _db.commit();

        _person4Id = _person4.getId();
    }
    
    private void check7() throws PersistenceException {
        stream.println("Load the objects again to see if changes done are effective");
        _db.begin();
        _oql.bind(_groupAId);
        _groupA = null;
        _person1 = null;
        _person2 = null;
        QueryResults enumeration = _oql.execute();
        if (enumeration.hasMore()) {
            _groupA = (ManyGroupKeyGen) enumeration.next();
            stream.println("Retrieved object: " + _groupA);
            Collection p = _groupA.getPeople();
            if (p != null) {
                Iterator itor = p.iterator();
                if (itor.hasNext()) {
                    _person1 = (ManyPersonKeyGen) itor.next();
                } else {
                    fail("Erorr: less people than expected!");
                }
                if (itor.hasNext()) {
                    _person4 = (ManyPersonKeyGen) itor.next();
                } else {
                    fail("Erorr: less people than expected!");
                }

                // swap if the order is wrong
                if ((_person1.getId() == _person4Id)
                        && (_person4.getId() == _person1Id)) {
                    ManyPersonKeyGen temp = _person1;
                    _person1 = _person4;
                    _person4 = temp;
                }
                if (itor.hasNext()) {
                    fail("Error: more people than expected! "
                            + "1:(" + _person1 + ") 2: (" + itor.next() + ")");
                }

                if (_person1 == null) { fail("Error: expect person1 in group"); }

                if (_person1.getId() == _person1Id) {
                    // check if the value is valid for person1 and chnage value of person1
                    if ((_person1.getValue1() == null)
                            || !_person1.getValue1().equals(
                                    "New new value for person 1")) {
                        fail("Error: unexpected person value");
                    }
                } else {
                    fail("Error: people in group is not the same as expected!");
                }

                if (_person4.getId() == _person4Id) {
                    // check if the value is valid for person1 and chnage value of person1
                    if ((_person4.getValue1() == null)
                            || !_person4.getValue1().equals("I am person 4")) {
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
        _person3 = (ManyPersonKeyGen) _db.load(
                ManyPersonKeyGen.class, new Integer(_person3Id));
        _db.commit();
    }
    
    private void check8() throws PersistenceException {
        // modify and commit to long trans
        _groupA.getPeople().add(_person3);
        _person3.getGroup().add(_groupA);
        _db.begin();
        _db.update(_groupA);
        _db.commit();

        // load and check
        _db.begin();
        _person3 = (ManyPersonKeyGen) _db.load(
                ManyPersonKeyGen.class, new Integer(_person3Id));
        Iterator tempItor = _person3.getGroup().iterator();
        if (!tempItor.hasNext()) { fail("Error: group not found"); }
        _groupA = (ManyGroupKeyGen) tempItor.next();
        if (_groupA.getId() != _groupAId) { fail("Error: unexpected group found"); }
        if (tempItor.hasNext()) { fail("Error: too many group"); }
        _db.commit();
    }
    
    /**
     * Close the JDO database
     */
    public void tearDown() throws PersistenceException {
        if (_db.isActive()) { _db.rollback(); }
        _db.close();
    }
}


