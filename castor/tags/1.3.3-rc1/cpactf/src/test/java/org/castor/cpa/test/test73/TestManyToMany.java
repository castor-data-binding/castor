/*
 * Copyright 2008 Udai Gupta, Ralf Joachim
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.castor.cpa.test.test73;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.castor.cpa.test.framework.CPATestCase;
import org.castor.cpa.test.framework.xml.types.DatabaseEngineType;
import org.castor.cpa.test.test74.TestManyToManyKeyGen;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.QueryResults;

/**
 * Test for many-to-many relationship. A many to many relationship is stored in
 * a relational database as a separated table.
 */
public final class TestManyToMany extends CPATestCase {

    private static final int PERSON_1_ID = 1;
    private static final int PERSON_2_ID = 2;
    private static final int PERSON_3_ID = 3;
    private static final int PERSON_4_ID = 4;
    private static final int GROUP_A_ID = 201;
    private static final int GROUP_B_ID = 202;

    private static final Log LOG = LogFactory.getLog(TestManyToManyKeyGen.class);

    private static final String DBNAME = "test73";
    private static final String MAPPING = "/org/castor/cpa/test/test73/mapping.xml";
    private Database _db;
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
     * @param category
     *            The test suite of these tests
     */
    public TestManyToMany(final String name) {
        super(name);
    }

    // Test are only included/excluded for engines that have been tested with this test suite.

    public boolean include(final DatabaseEngineType engine) {
        return (engine == DatabaseEngineType.DERBY)
            || (engine == DatabaseEngineType.HSQL)
            || (engine == DatabaseEngineType.MYSQL)
            || (engine == DatabaseEngineType.ORACLE)
            || (engine == DatabaseEngineType.POSTGRESQL)
            || (engine == DatabaseEngineType.SQL_SERVER)
            || (engine == DatabaseEngineType.SAPDB);
    }

    public void setUp() throws Exception {
        _db = getJDOManager(DBNAME, MAPPING).getDatabase();
    }

    public void tearDown() throws Exception {
        if (_db.isActive()) {
            _db.rollback();
        }
        _db.close();
    }

    public void testManyToMany() throws PersistenceException {
        LOG.debug("Running...");
        LOG.debug("");

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
        OQLQuery oqlclean = _db.getOQLQuery("SELECT object FROM " + ManyGroup.class.getName()
                + " object WHERE object.id < $1");
        oqlclean.bind(Integer.MAX_VALUE);
        QueryResults enumeration = oqlclean.execute();
        while (enumeration.hasMore()) {
            _groupA = (ManyGroup) enumeration.next();
            LOG.debug("Retrieved object: " + _groupA);
            _db.remove(_groupA);
            LOG.debug("Deleted object: " + _groupA);
        }
        _db.commit();
    }

    private void deletePersons() throws PersistenceException {
        _db.begin();
        OQLQuery oqlclean = _db.getOQLQuery("SELECT object FROM " + ManyPerson.class.getName()
                + " object WHERE object.id < $1");
        oqlclean.bind(Integer.MAX_VALUE);
        QueryResults enumeration = oqlclean.execute();
        while (enumeration.hasMore()) {
            _person1 = (ManyPerson) enumeration.next();
            LOG.debug("Retrieved object: " + _person1);
            _db.remove(_person1);
            LOG.debug("Deleted object: " + _person1);
        }
        _db.commit();
    }

    private void create() throws PersistenceException {
        // create new group and new people, don't link them yet.
        // This test for null collection handling
        _db.begin();
        _oql = _db.getOQLQuery("SELECT object FROM " + ManyGroup.class.getName()
                + " object WHERE id = $1");
        LOG.debug("Creating new group with people!");
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
        LOG.debug("Creating new group with people!");
        _person1 = _db.load(ManyPerson.class, new Integer(PERSON_1_ID));
        _person1.setValue1("I am person 1");
        ArrayList < ManyGroup > gPerson1 = new ArrayList < ManyGroup > ();
        _person1.setId(PERSON_1_ID);
        _person1.setGroup(gPerson1);
        _person1.setSthelse("Something else");
        _person1.setHelloworld("Hello World!");

        _person2 = new ManyPerson();
        _person2.setValue1("I am person 2");
        ArrayList < ManyGroup > gPerson2 = new ArrayList < ManyGroup > ();
        _person2.setId(PERSON_2_ID);
        _person2.setGroup(gPerson2);
        _person2.setSthelse("Something else");
        _person2.setHelloworld("Hello World!");

        _person3 = new ManyPerson();
        _person3.setValue1("I am person 3");
        ArrayList < ManyGroup > gPerson3 = new ArrayList < ManyGroup > ();
        _person3.setId(PERSON_3_ID);
        _person3.setGroup(gPerson3);
        _person3.setSthelse("Something else for person 3");
        _person3.setHelloworld("Hello World!");

        _person4 = new ManyPerson();
        _person4.setValue1("I am person 4");
        ArrayList < ManyGroup > gPerson4 = new ArrayList < ManyGroup > ();
        _person4.setId(PERSON_4_ID);
        _person4.setGroup(gPerson4);
        _person4.setSthelse("Something else for person 4");
        _person4.setHelloworld("Hello World!");

        _groupA = new ManyGroup();
        _groupA.setValue1("Group A");
        ArrayList < ManyPerson > al = new ArrayList < ManyPerson > ();
        al.add(_person1);
        al.add(_person2);
        _groupA.setId(GROUP_A_ID);
        _groupA.setPeople(al);

        _groupB = new ManyGroup();
        _groupB.setValue1("Group B");
        _groupB.setId(GROUP_B_ID);
        ArrayList < ManyPerson > bl = new ArrayList < ManyPerson > ();
        bl.add(_person2);
        _groupB.setPeople(bl);
        gPerson1.add(_groupA);
        gPerson2.add(_groupA);
        gPerson2.add(_groupB);

        _db.create(_groupA);
        _db.create(_person2);
        _db.create(_groupB);

        LOG.debug("object created: " + _groupA);
        _db.commit();
    }

    private void check1() throws PersistenceException {
        LOG.debug("Load the objects and modify it");
        _db.begin();
        _oql.bind(GROUP_A_ID);
        _groupA = null;
        _person1 = null;
        _person2 = null;
        QueryResults enumeration = _oql.execute();
        if (enumeration.hasMore()) {
            _groupA = (ManyGroup) enumeration.next();
            LOG.debug("Retrieved object: " + _groupA);
            Collection < ManyPerson > p = _groupA.getPeople();
            if (p != null) {
                Iterator < ManyPerson > itor = p.iterator();
                if (itor.hasNext()) {
                    _person1 = itor.next();
                }
                if (itor.hasNext()) {
                    _person2 = itor.next();
                }
                if (itor.hasNext()) {
                    fail("Error: more people than expected!");
                }

                if ((_person1 == null) || (_person2 == null)) {
                    fail("Error: expect two people in group");
                }

                if ((_person1.getId() == PERSON_2_ID) && (_person2.getId() == PERSON_1_ID)) {
                    ManyPerson temp = _person1;
                    _person1 = _person2;
                    _person2 = temp;
                }

                if ((_person1.getId() == PERSON_1_ID) && (_person2.getId() == PERSON_2_ID)) {
                    // check if the value is valid for person1 and chnage value
                    // of person1
                    if ((_person1.getValue1() == null)
                            || !_person1.getValue1().equals("I am person 1")) {
                        fail("Error: unexpected person value");
                    } else {
                        _person1.setValue1("New person 1 value");
                    }

                    // check if the value is valid for person1 and remove
                    // person2
                    if ((_person2.getValue1() == null)
                            || !_person2.getValue1().equals("I am person 2")) {
                        fail("Error: unexpected person value");
                    }

                    // make sure person 2 contains 2 groups
                    if ((_person2.getGroup() == null) || (_person2.getGroup().size() != 2)) {
                        fail("Error: expected group not found [2]");
                    }
                    Iterator < ManyGroup > groupItor = _person2.getGroup().iterator();

                    groupItor.hasNext();
                    ManyGroup tempGroup = groupItor.next();
                    int tempId = tempGroup.getId();
                    if ((tempId != GROUP_A_ID) && (tempId != GROUP_B_ID)) {
                        fail("Error: unexpect group found");
                    }

                    groupItor.hasNext();
                    tempGroup = groupItor.next();
                    if (tempGroup.getId() == tempId) {
                        fail("Error: duplicated group found");
                    }
                    if ((tempGroup.getId() != GROUP_A_ID) && (tempGroup.getId() != GROUP_B_ID)) {
                        fail("Error: unexpect group found");
                    }

                    // remove person 2
                    itor = p.iterator();
                    while (itor.hasNext()) {
                        _person2 = itor.next();
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
        LOG.debug("Load the objects again to see if changes done are effective");
        _db.begin();
        _oql.bind(GROUP_A_ID);
        _groupA = null;
        _person1 = null;
        _person2 = null;
        QueryResults enumeration = _oql.execute();
        if (enumeration.hasMore()) {
            _groupA = (ManyGroup) enumeration.next();
            LOG.debug("Retrieved object: " + _groupA);
            Collection < ManyPerson > p = _groupA.getPeople();
            if (p != null) {
                Iterator < ManyPerson > itor = p.iterator();
                if (itor.hasNext()) {
                    _person1 = itor.next();
                }
                if (itor.hasNext()) {
                    _person3 = itor.next();
                }

                // swap if the order is wrong
                if ((_person1.getId() == PERSON_3_ID) && (_person3.getId() == PERSON_1_ID)) {
                    ManyPerson temp = _person1;
                    _person1 = _person3;
                    _person3 = temp;
                }
                if (itor.hasNext()) {
                    fail("Error: more people than expected! " + "1:(" + _person1 + ") 2: ("
                            + itor.next() + ")");
                }

                if (_person1 == null) {
                    fail("Error: expect person1 in group");
                }

                if (_person1.getId() == PERSON_1_ID) {
                    // check if the value is valid for person1 and chnage value
                    // of person1
                    if ((_person1.getValue1() == null)
                            || !_person1.getValue1().equals("New person 1 value")) {
                        fail("Error: unexpected person value");
                    }
                } else {
                    fail("Error: people in group is not the same as expected!");
                }

                if (_person3.getId() == PERSON_3_ID) {
                    // check if the value is valid for person1 and chnage value
                    // of person1
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
        _person2 = _db.load(ManyPerson.class, new Integer(PERSON_2_ID));
        // make sure person 2 contains 2 groups
        if ((_person2.getGroup() == null) || (_person2.getGroup().size() != 1)) {
            fail("Error: expected group not found [3]");
        }

        Iterator < ManyGroup > groupItor = _person2.getGroup().iterator();
        groupItor.hasNext();
        ManyGroup tempGroup = groupItor.next();
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
        _person2 = _db.load(ManyPerson.class, new Integer(PERSON_2_ID));
        if ((_person2.getGroup() != null) && (_person2.getGroup().size() != 0)) {
            fail("Error: expected group not found [1]");
        }
        _db.remove(_person2);
        _db.commit();
    }

    private void check4() throws PersistenceException {
        _db.begin();
        // check if group a and group b contains no person2
        _groupA = _db.load(ManyGroup.class, new Integer(GROUP_A_ID));
        Iterator < ManyPerson > groupItor = _groupA.getPeople().iterator();
        while (groupItor.hasNext()) {
            _person2 = groupItor.next();
            if (_person2.getId() == PERSON_2_ID) {
                fail("Error: person2 is not removed");
            }
        }
        _groupB = _db.load(ManyGroup.class, new Integer(GROUP_B_ID));
        if ((_groupB.getPeople() != null) && (_groupB.getPeople().size() != 0)) {
            fail("Error: person2 is not removed");
        }

        // make a dangerous add (add to only one side)
        // user shouldn't rely on this behavior, but
        // should always link both side before commit
        _person1 = _db.load(ManyPerson.class, new Integer(PERSON_1_ID));
        _person1.getGroup().add(_groupB);
        _db.commit();
    }

    private void check5() throws PersistenceException {
        // check if adding group into existing collection work
        _db.begin();
        _person1 = _db.load(ManyPerson.class, new Integer(PERSON_1_ID));
        Iterator < ManyGroup > tempItor = _person1.getGroup().iterator();
        if (!tempItor.hasNext()) {
            fail("Error: expected group from person1 not found");
        }
        _groupA = tempItor.next();
        int tempGroupId = _groupA.getId();
        if ((tempGroupId != GROUP_A_ID) && (tempGroupId != GROUP_B_ID)) {
            fail("Error: unexpected group from person1 found");
        }

        if (!tempItor.hasNext()) {
            fail("Error: expected group from person1 not found");
        }
        _groupA = tempItor.next();
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
        _groupA = _db.load(ManyGroup.class, new Integer(GROUP_A_ID));
        _db.commit();

        LOG.debug("Modifing object outside of transaction");
        // remove person 3
        Iterator < ManyPerson > it = _groupA.getPeople().iterator();
        while (it.hasNext()) {
            _person3 = it.next();
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
            _person1 = it.next();
            if (_person1.getId() == PERSON_1_ID) {
                break;
            }
        }
        _person1.setValue1("New new value for person 1");

        LOG.debug("Update object to a new transaction");
        _db.setAutoStore(true);
        _db.begin();
        _db.update(_groupA);
        _db.commit();
    }

    private void check7() throws PersistenceException {
        LOG.debug("Load the objects again to see if changes done are effective");
        _db.begin();
        _oql.bind(GROUP_A_ID);
        _groupA = null;
        _person1 = null;
        _person2 = null;
        QueryResults enumeration = _oql.execute();
        if (enumeration.hasMore()) {
            _groupA = (ManyGroup) enumeration.next();
            LOG.debug("Retrieved object: " + _groupA);
            Collection < ManyPerson > p = _groupA.getPeople();
            if (p != null) {
                Iterator < ManyPerson > itor = p.iterator();
                if (itor.hasNext()) {
                    _person1 = itor.next();
                } else {
                    fail("Erorr: less people than expected!");
                }
                if (itor.hasNext()) {
                    _person4 = itor.next();
                } else {
                    fail("Erorr: less people than expected!");
                }

                // swap if the order is wrong
                if ((_person1.getId() == PERSON_4_ID) && (_person4.getId() == PERSON_1_ID)) {
                    ManyPerson temp = _person1;
                    _person1 = _person4;
                    _person4 = temp;
                }
                if (itor.hasNext()) {
                    fail("Error: more people than expected! " + "1:(" + _person1 + ") 2: ("
                            + itor.next() + ")");
                }

                if (_person1 == null) {
                    fail("Error: expect person1 in group");
                }

                if (_person1.getId() == PERSON_1_ID) {
                    // check if the value is valid for person1 and chnage value
                    // of person1
                    if ((_person1.getValue1() == null)
                            || !_person1.getValue1().equals("New new value for person 1")) {
                        fail("Error: unexpected person value");
                    }
                } else {
                    fail("Error: people in group is not the same as expected!");
                }

                if (_person4.getId() == PERSON_4_ID) {
                    // check if the value is valid for person1 and chnage value
                    // of person1
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
        _person3 = _db.load(ManyPerson.class, new Integer(PERSON_3_ID));
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
        _person3 = _db.load(ManyPerson.class, new Integer(PERSON_3_ID));
        Iterator < ManyGroup > tempItor = _person3.getGroup().iterator();
        if (!tempItor.hasNext()) {
            fail("Error: group not found");
        }
        _groupA = tempItor.next();
        if (_groupA.getId() != GROUP_A_ID) {
            fail("Error: unexpected group found");
        }
        if (tempItor.hasNext()) {
            fail("Error: too many group");
        }
        _db.commit();
    }
}
