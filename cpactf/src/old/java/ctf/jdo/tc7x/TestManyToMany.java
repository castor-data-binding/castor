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
 * Test for many-to-many relationship. A many to many relationship
 * is stored in a relational database as a separated table.
 */
public final class TestManyToMany extends CastorTestCase {
    private static final int PERSON_1_ID = 1;
    private static final int PERSON_2_ID = 2;
    private static final int PERSON_3_ID = 3;
    private static final int PERSON_4_ID = 4;
    private static final int GROUP_A_ID = 201;
    private static final int GROUP_B_ID = 202;

    private Database _db;
    private JDOCategory _category;
    private OQLQuery _oql;
    private ManyPerson _person1;
    private ManyPerson _person2;
    private ManyPerson _person3;
    private ManyPerson _person4;
    private ManyGroup _groupA;
    private ManyGroup _groupB;

    /**
     * Constructor
     *
     * @param category The test suite of these tests
     */
    public TestManyToMany(final TestHarness category) {
        super(category, "TC73", "ManyToMany");
        _category = (JDOCategory) category;
    }

    public void setUp() throws PersistenceException {
        _db = _category.getDatabase();
    }
    
    public void runTest() throws PersistenceException {
        _stream.println("Running...");
        _stream.println("");

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
                + ManyGroup.class.getName() + " object WHERE object.id < $1");
        oqlclean.bind(Integer.MAX_VALUE);
        QueryResults enumeration = oqlclean.execute();
        while (enumeration.hasMore()) {
            _groupA = (ManyGroup) enumeration.next();
            _stream.println("Retrieved object: " + _groupA);
            _db.remove(_groupA);
            _stream.println("Deleted object: " + _groupA);
        }
        _db.commit();
    }

    private void deletePersons() throws PersistenceException {
        _db.begin();
        OQLQuery oqlclean = _db.getOQLQuery("SELECT object FROM " 
                + ManyPerson.class.getName() + " object WHERE object.id < $1");
        oqlclean.bind(Integer.MAX_VALUE);
        QueryResults enumeration = oqlclean.execute();
        while (enumeration.hasMore()) {
            _person1 = (ManyPerson) enumeration.next();
            _stream.println("Retrieved object: " + _person1);
            _db.remove(_person1);
            _stream.println("Deleted object: " + _person1);
        } 
        _db.commit();
    }

    private void create() throws PersistenceException {
        // create new group and new people, don't link them yet.
        // This test for null collection handling
        _db.begin();
        _oql = _db.getOQLQuery("SELECT object FROM " 
                + ManyGroup.class.getName() + " object WHERE id = $1");
        _stream.println("Creating new group with people!");
        _person1 = new ManyPerson();
        _person1.setValue1("I am person 1");
        _person1.setId(PERSON_1_ID);
        _person1.setGroup(null);
        _person1.setSthelse("Something else");
        _person1.setHelloworld("Hello World!");
        _db.create(_person1);
        _db.commit();

        // create new group with two people
        _db.begin();
        _stream.println("Creating new group with people!");
        _person1 = (ManyPerson) _db.load(ManyPerson.class, new Integer(PERSON_1_ID));
        _person1.setValue1("I am person 1");
        ArrayList gPerson1 = new ArrayList();
        _person1.setId(PERSON_1_ID);
        _person1.setGroup(gPerson1);
        _person1.setSthelse("Something else");
        _person1.setHelloworld("Hello World!");

        _person2 = new ManyPerson();
        _person2.setValue1("I am person 2");
        ArrayList gPerson2 = new ArrayList();
        _person2.setId(PERSON_2_ID);
        _person2.setGroup(gPerson2);
        _person2.setSthelse("Something else");
        _person2.setHelloworld("Hello World!");

        _person3 = new ManyPerson();
        _person3.setValue1("I am person 3");
        ArrayList gPerson3 = new ArrayList();
        _person3.setId(PERSON_3_ID);
        _person3.setGroup(gPerson3);
        _person3.setSthelse("Something else for person 3");
        _person3.setHelloworld("Hello World!");

        _person4 = new ManyPerson();
        _person4.setValue1("I am person 4");
        ArrayList gPerson4 = new ArrayList();
        _person4.setId(PERSON_4_ID);
        _person4.setGroup(gPerson4);
        _person4.setSthelse("Something else for person 4");
        _person4.setHelloworld("Hello World!");

        _groupA = new ManyGroup();
        _groupA.setValue1("Group A");
        ArrayList al = new ArrayList();
        al.add(_person1);
        al.add(_person2);
        _groupA.setId(GROUP_A_ID);
        _groupA.setPeople(al);

        _groupB = new ManyGroup();
        _groupB.setValue1("Group B");
        _groupB.setId(GROUP_B_ID);
        ArrayList bl = new ArrayList();
        bl.add(_person2);
        _groupB.setPeople(bl);
        gPerson1.add(_groupA);
        gPerson2.add(_groupA);
        gPerson2.add(_groupB);

        _db.create(_groupA);
        _db.create(_person2);
        _db.create(_groupB);

        _stream.println("object created: " + _groupA);
        _db.commit();
    }

    private void check1() throws PersistenceException {
        _stream.println("Load the objects and modify it");
        _db.begin();
        _oql.bind(GROUP_A_ID);
        _groupA = null;
        _person1 = null;
        _person2 = null;
        QueryResults enumeration = _oql.execute();
        if (enumeration.hasMore()) {
            _groupA = (ManyGroup) enumeration.next();
            _stream.println("Retrieved object: " + _groupA);
            Collection p = _groupA.getPeople();
            if (p != null) {
                Iterator itor = p.iterator();
                if (itor.hasNext()) { _person1 = (ManyPerson) itor.next(); }
                if (itor.hasNext()) { _person2 = (ManyPerson) itor.next(); }
                if (itor.hasNext()) { fail("Error: more people than expected!"); }
            
                if ((_person1 == null) || (_person2 == null)) {
                    fail("Error: expect two people in group");
                }

                if ((_person1.getId() == PERSON_2_ID)
                        && (_person2.getId() == PERSON_1_ID)) {
                    ManyPerson temp = _person1;
                    _person1 = _person2;
                    _person2 = temp;
                }
                
                if ((_person1.getId() == PERSON_1_ID)
                        && (_person2.getId() == PERSON_2_ID)) {
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
                    ManyGroup tempGroup = (ManyGroup) groupItor.next();
                    int tempId = tempGroup.getId();
                    if ((tempId != GROUP_A_ID) && (tempId != GROUP_B_ID)) {
                        fail("Error: unexpect group found");
                    }

                    groupItor.hasNext();
                    tempGroup = (ManyGroup) groupItor.next();
                    if (tempGroup.getId() == tempId) {
                        fail("Error: duplicated group found");
                    }
                    if ((tempGroup.getId() != GROUP_A_ID)
                            && (tempGroup.getId() != GROUP_B_ID)) {
                        fail("Error: unexpect group found");
                    }

                    // remove person 2
                    itor = p.iterator();
                    while (itor.hasNext()) {
                        _person2 = (ManyPerson) itor.next();
                        if (_person2.getId() == PERSON_2_ID) {
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
                fail("Error: related object not found[1]!");
            }
        } else {
            fail("Error: object not found!");
        }
        _db.commit();
    }

    private void check2() throws PersistenceException {
        _stream.println("Load the objects again to see if changes done are effective");
        _db.begin();
        _oql.bind(GROUP_A_ID);
        _groupA = null;
        _person1 = null;
        _person2 = null;
        QueryResults enumeration = _oql.execute();
        if (enumeration.hasMore()) {
            _groupA = (ManyGroup) enumeration.next();
            _stream.println("Retrieved object: " + _groupA);
            Collection p = _groupA.getPeople();
            if (p != null) {
                Iterator itor = p.iterator();
                if (itor.hasNext()) { _person1 = (ManyPerson) itor.next(); }
                if (itor.hasNext()) { _person3 = (ManyPerson) itor.next(); }

                // swap if the order is wrong
                if ((_person1.getId() == PERSON_3_ID)
                        && (_person3.getId() == PERSON_1_ID)) {
                    ManyPerson temp = _person1;
                    _person1 = _person3;
                    _person3 = temp;
                }
                if (itor.hasNext()) {
                    fail("Error: more people than expected! "
                            + "1:(" + _person1 + ") 2: (" + itor.next() + ")");
                }

                if (_person1 == null) { fail("Error: expect person1 in group"); }

                if (_person1.getId() == PERSON_1_ID) {
                    // check if the value is valid for person1 and chnage value of person1
                    if ((_person1.getValue1() == null)
                            || !_person1.getValue1().equals("New person 1 value")) {
                        fail("Error: unexpected person value");
                    }
                } else {
                    fail("Error: people in group is not the same as expected!");
                }

                if (_person3.getId() == PERSON_3_ID) {
                    // check if the value is valid for person1 and chnage value of person1
                    if ((_person3.getValue1() == null)
                            || !_person3.getValue1().equals("I am person 3")) {
                        fail("Error: unexpected person value");
                    }
                } else {
                    fail("Error: people in group is not the same as expected!");
                }

            } else {
                fail("Error: related object not found[2]!");
            }
        } else {
            fail("Error: object not found!");
        }

        // check if person 2 contains only one group, and the group is B
        _person2 = (ManyPerson) _db.load(ManyPerson.class, new Integer(PERSON_2_ID));
        // make sure person 2 contains 2 groups
        if ((_person2.getGroup() == null) || (_person2.getGroup().size() != 1)) {
            fail("Error: expected group not found [3]");
        }

        Iterator groupItor = _person2.getGroup().iterator();
        groupItor.hasNext();
        ManyGroup tempGroup = (ManyGroup) groupItor.next();
        if (tempGroup.getId() != GROUP_B_ID) {
            fail("Error: unexpected group found [1]: " + tempGroup.getId());
        }

        // remove all group from person2
        _person2.setGroup(null);
        _db.commit();
    }

    private void check3() throws PersistenceException {
        _db.begin();
        // check if person 2 contains no group
        _person2 = (ManyPerson) _db.load(ManyPerson.class, new Integer(PERSON_2_ID));
        if ((_person2.getGroup() != null) && (_person2.getGroup().size() != 0)) {
            fail("Error: expected group not found [1]");
        }
        _db.remove(_person2);
        _db.commit();
    }

    private void check4() throws PersistenceException {
        _db.begin();
        // check if group a and group b contains no person2
        _groupA = (ManyGroup) _db.load(ManyGroup.class, new Integer(GROUP_A_ID));
        Iterator groupItor = _groupA.getPeople().iterator();
        while (groupItor.hasNext()) {
            _person2 = (ManyPerson) groupItor.next();
            if (_person2.getId() == PERSON_2_ID) {
                fail("Error: person2 is not removed");
            }
        }
        _groupB = (ManyGroup) _db.load(ManyGroup.class, new Integer(GROUP_B_ID));
        if ((_groupB.getPeople() != null) && (_groupB.getPeople().size() != 0)) {
            fail("Error: person2 is not removed");
        }

        // make a dangerous add (add to only one side)
        // user shouldn't rely on this behavior, but 
        // should always link both side before commit
        _person1 = (ManyPerson) _db.load(ManyPerson.class, new Integer(PERSON_1_ID));
        _person1.getGroup().add(_groupB);
        _db.commit();
    }

    private void check5() throws PersistenceException {
        // check if adding group into existing collection work
        _db.begin();
        _person1 = (ManyPerson) _db.load(ManyPerson.class, new Integer(PERSON_1_ID));
        Iterator tempItor = _person1.getGroup().iterator();
        if (!tempItor.hasNext()) {
            fail("Error: expected group from person1 not found");
        }
        _groupA = (ManyGroup) tempItor.next();
        int tempGroupId = _groupA.getId();
        if ((tempGroupId != GROUP_A_ID) && (tempGroupId != GROUP_B_ID)) {
            fail("Error: unexpected group from person1 found");
        }

        if (!tempItor.hasNext()) {
            fail("Error: expected group from person1 not found");
        }
        _groupA = (ManyGroup) tempItor.next();
        if (tempGroupId == _groupA.getId()) {
            fail("Error: duplicated group found!");
        }
        if ((_groupA.getId() != GROUP_A_ID) && (_groupA.getId() != GROUP_B_ID)) {
            fail("Error: unexpected group from person1 found");
        }
        _db.commit();
    }

    private void check6() throws PersistenceException {
        // test long transaction support
        _db.begin();
        _groupA = (ManyGroup) _db.load(ManyGroup.class, new Integer(GROUP_A_ID));
        _db.commit();

        _stream.println("Modifing object outside of transaction");
        // remove person 3
        Iterator it = _groupA.getPeople().iterator();
        while (it.hasNext()) {
            _person3 = (ManyPerson) it.next();
            if (_person3.getId() == PERSON_3_ID) {
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
            _person1 = (ManyPerson) it.next();
            if (_person1.getId() == PERSON_1_ID) { break; }
        }
        _person1.setValue1("New new value for person 1");

        _stream.println("Update object to a new transaction");
        _db.setAutoStore(true);
        _db.begin();
        _db.update(_groupA);
        _db.commit();
    }

    private void check7() throws PersistenceException {
        _stream.println("Load the objects again to see if changes done are effective");
        _db.begin();
        _oql.bind(GROUP_A_ID);
        _groupA = null;
        _person1 = null;
        _person2 = null;
        QueryResults enumeration = _oql.execute();
        if (enumeration.hasMore()) {
            _groupA = (ManyGroup) enumeration.next();
            _stream.println("Retrieved object: " + _groupA);
            Collection p = _groupA.getPeople();
            if (p != null) {
                Iterator itor = p.iterator();
                if (itor.hasNext()) {
                    _person1 = (ManyPerson) itor.next();
                } else {
                    fail("Erorr: less people than expected!");
                }
                if (itor.hasNext()) {
                    _person4 = (ManyPerson) itor.next();
                } else {
                    fail("Erorr: less people than expected!");
                }

                // swap if the order is wrong
                if ((_person1.getId() == PERSON_4_ID)
                        && (_person4.getId() == PERSON_1_ID)) {
                    ManyPerson temp = _person1;
                    _person1 = _person4;
                    _person4 = temp;
                }
                if (itor.hasNext()) {
                    fail("Error: more people than expected! "
                            + "1:(" + _person1 + ") 2: (" + itor.next() + ")");
                }

                if (_person1 == null) {
                    fail("Error: expect person1 in group");
                }

                if (_person1.getId() == PERSON_1_ID) {
                    // check if the value is valid for person1 and chnage value of person1
                    if ((_person1.getValue1() == null)
                            || !_person1.getValue1().equals(
                                    "New new value for person 1")) {
                        fail("Error: unexpected person value");
                    }
                } else {
                    fail("Error: people in group is not the same as expected!");
                }

                if (_person4.getId() == PERSON_4_ID) {
                    // check if the value is valid for person1 and chnage value of person1
                    if ((_person4.getValue1() == null)
                            || !_person4.getValue1().equals("I am person 4")) {
                        fail("Error: unexpected person value");
                    }
                } else {
                    fail("Error: people in group is not the same as expected!");
                }

            } else {
                fail("Error: related object not found[3]!");
            }
        } else {
            fail("Error: object not found!");
        }
        _person3 = (ManyPerson) _db.load(ManyPerson.class, new Integer(PERSON_3_ID));
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
        _person3 = (ManyPerson) _db.load(ManyPerson.class, new Integer(PERSON_3_ID));
        Iterator tempItor = _person3.getGroup().iterator();
        if (!tempItor.hasNext()) { fail("Error: group not found"); }
        _groupA = (ManyGroup) tempItor.next();
        if (_groupA.getId() != GROUP_A_ID) { fail("Error: unexpected group found"); }
        if (tempItor.hasNext()) { fail("Error: too many group"); }
        _db.commit();
    }

    public void tearDown() throws PersistenceException {
        if (_db.isActive()) { _db.rollback(); }
        _db.close();
    }
}


