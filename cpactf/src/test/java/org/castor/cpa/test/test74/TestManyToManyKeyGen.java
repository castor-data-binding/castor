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
package org.castor.cpa.test.test74;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.castor.cpa.test.framework.CPATestCase;
import org.castor.cpa.test.framework.xml.types.DatabaseEngineType;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.QueryResults;

/**
 * Test for many to many relationship with key generators being used in both
 * side of the relationship. The tests create data objects, modify it and verify
 * the modification is persisted properly. These tests also test for many to
 * many relationship with long transaction support: it loads a set of data
 * objects in one transaction, modify it and update it into another transaction.
 */
public final class TestManyToManyKeyGen extends CPATestCase {

    private static final Log LOG = LogFactory.getLog(TestManyToManyKeyGen.class);

    private static final String DBNAME = "test74";
    private static final String MAPPING = "/org/castor/cpa/test/test74/mapping.xml";
    private Database _db;
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
     * @param category
     *            The test suite of these test cases.
     */
    public TestManyToManyKeyGen(final String name) {
        super(name);
    }

    // Test are only included/excluded for engines that have been tested with this test suite.

    public boolean include(final DatabaseEngineType engine) {
        return (engine == DatabaseEngineType.DERBY)
            || (engine == DatabaseEngineType.HSQL)
            || (engine == DatabaseEngineType.MYSQL)
            || (engine == DatabaseEngineType.ORACLE)
            || (engine == DatabaseEngineType.POSTGRESQL)
            || (engine == DatabaseEngineType.SQL_SERVER);
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

    /**
     * The tests create data objects, modify it and verify the modification is
     * persisted properly. These tests also test for many to many relationship
     * with long transaction support: it loads a set of data objects in one
     * transaction, modify it and update it into another transaction.
     */
    public void testManytoManyKeyGen() throws PersistenceException {
        LOG.debug("Running...");

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
        OQLQuery oqlclean = _db.getOQLQuery("SELECT object FROM " + ManyGroupKeyGen.class.getName()
                + " object WHERE object.id < $1");
        oqlclean.bind(Integer.MAX_VALUE);
        QueryResults enumeration = oqlclean.execute();
        while (enumeration.hasMore()) {
            _groupA = (ManyGroupKeyGen) enumeration.next();
            LOG.debug("Retrieved object: " + _groupA);
            _db.remove(_groupA);
            LOG.debug("Deleted object: " + _groupA);
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
        _oql = _db.getOQLQuery("SELECT object FROM " + ManyGroupKeyGen.class.getName()
                + " object WHERE id = $1");
        LOG.debug("Creating new group with people!");
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
        _oql = _db.getOQLQuery("SELECT object FROM " + ManyGroupKeyGen.class.getName()
                + " object WHERE id = $1");
        LOG.debug("Creating new group with people!");
        _person1 = _db.load(ManyPersonKeyGen.class, new Integer(_person1Id));
        _person1.setValue1("I am person 1");
        ArrayList < ManyGroupKeyGen > gPerson1 = new ArrayList < ManyGroupKeyGen > ();
        _person1.setGroup(gPerson1);
        _person1.setSthelse("Something else");
        _person1.setHelloworld("Hello World!");

        _person2 = new ManyPersonKeyGen();
        _person2.setValue1("I am person 2");
        ArrayList < ManyGroupKeyGen > gPerson2 = new ArrayList < ManyGroupKeyGen > ();
        _person2.setGroup(gPerson2);
        _person2.setSthelse("Something else");
        _person2.setHelloworld("Hello World!");

        _person3 = new ManyPersonKeyGen();
        _person3.setValue1("I am person 3");
        ArrayList < ManyGroupKeyGen > gPerson3 = new ArrayList < ManyGroupKeyGen > ();
        _person3.setGroup(gPerson3);
        _person3.setSthelse("Something else for person 3");
        _person3.setHelloworld("Hello World!");

        _person4 = new ManyPersonKeyGen();
        _person4.setValue1("I am person 4");
        ArrayList < ManyGroupKeyGen > gPerson4 = new ArrayList < ManyGroupKeyGen > ();
        _person4.setGroup(gPerson4);
        _person4.setSthelse("Something else for person 4");
        _person4.setHelloworld("Hello World!");

        _groupA = new ManyGroupKeyGen();
        _groupA.setValue1("Group A");
        ArrayList < ManyPersonKeyGen > al = new ArrayList < ManyPersonKeyGen > ();
        al.add(_person1);
        al.add(_person2);
        _groupA.setPeople(al);

        _groupB = new ManyGroupKeyGen();
        _groupB.setValue1("Group B");
        ArrayList < ManyPersonKeyGen > bl = new ArrayList < ManyPersonKeyGen > ();
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

        _groupAId = _groupA.getId();
        _groupBId = _groupB.getId();
        _person1Id = _person1.getId();
        _person2Id = _person2.getId();
    }

    private void check1() throws PersistenceException {
        LOG.debug("Load the objects and modify it");
        _db.begin();
        _oql.bind(_groupAId);
        _groupA = null;
        _person1 = null;
        _person2 = null;
        QueryResults enumeration = _oql.execute();
        if (enumeration.hasMore()) {
            _groupA = (ManyGroupKeyGen) enumeration.next();
            LOG.debug("Retrieved object: " + _groupA);
            Collection < ManyPersonKeyGen > p = _groupA.getPeople();
            if (p != null) {
                Iterator < ManyPersonKeyGen > itor = p.iterator();
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

                if ((_person1.getId() == _person2Id) && (_person2.getId() == _person1Id)) {
                    ManyPersonKeyGen temp = _person1;
                    _person1 = _person2;
                    _person2 = temp;
                }

                if ((_person1.getId() == _person1Id) && (_person2.getId() == _person2Id)) {
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
                    Iterator < ManyGroupKeyGen > groupItor = _person2.getGroup().iterator();

                    groupItor.hasNext();
                    ManyGroupKeyGen tempGroup = groupItor.next();
                    int tempId = tempGroup.getId();
                    if ((tempId != _groupAId) && (tempId != _groupBId)) {
                        fail("Error: unexpect group found");
                    }

                    groupItor.hasNext();
                    tempGroup = groupItor.next();
                    if (tempGroup.getId() == tempId) {
                        fail("Error: duplicated group found");
                    }
                    if ((tempGroup.getId() != _groupAId) && (tempGroup.getId() != _groupBId)) {
                        fail("Error: unexpect group found");
                    }

                    // remove person 2
                    itor = p.iterator();
                    while (itor.hasNext()) {
                        _person2 = itor.next();
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
        if (_person3Id == _person2Id) {
            fail("Error: unexpected id swapping ocurrs!");
        }
    }

    private void check2() throws PersistenceException {
        LOG.debug("Load the objects again to see if changes done are effective");
        _db.begin();
        _oql.bind(_groupAId);
        _groupA = null;
        _person1 = null;
        _person2 = null;
        QueryResults enumeration = _oql.execute();
        if (enumeration.hasMore()) {
            _groupA = (ManyGroupKeyGen) enumeration.next();
            LOG.debug("Retrieved object: " + _groupA);
            Collection < ManyPersonKeyGen > p = _groupA.getPeople();
            if (p != null) {
                Iterator < ManyPersonKeyGen > itor = p.iterator();
                if (itor.hasNext()) {
                    _person1 = itor.next();
                }
                if (itor.hasNext()) {
                    _person3 = itor.next();
                }

                // swap if the order is wrong
                if ((_person1.getId() == _person3Id) && (_person3.getId() == _person1Id)) {
                    ManyPersonKeyGen temp = _person1;
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

                if (_person1.getId() == _person1Id) {
                    // check if the value is valid for person1 and chnage value
                    // of person1
                    if ((_person1.getValue1() == null)
                            || !_person1.getValue1().equals("New person 1 value")) {
                        fail("Error: unexpected person value");
                    }
                } else {
                    fail("Error: people in group is not the same as expected!");
                }

                if (_person3.getId() == _person3Id) {
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
                fail("Error: related object not found!");
            }
        } else {
            fail("Error: object not found!");
        }

        // check if person 2 contains only one group, and the group is B
        _person2 = _db.load(ManyPersonKeyGen.class, new Integer(_person2Id));
        // make sure person 2 contains 2 groups
        if ((_person2.getGroup() == null) || (_person2.getGroup().size() != 1)) {
            fail("Error: expected group not found [3]");
        }

        Iterator < ManyGroupKeyGen > groupItor = _person2.getGroup().iterator();
        groupItor.hasNext();
        ManyGroupKeyGen tempGroup = groupItor.next();
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
        _person2 = _db.load(ManyPersonKeyGen.class, new Integer(_person2Id));
        if ((_person2.getGroup() != null) && (_person2.getGroup().size() != 0)) {
            fail("Error: expected group not found [1]");
        }
        _db.remove(_person2);
        _db.commit();
    }

    private void check4() throws PersistenceException {
        _db.begin();
        // check if group a and group b contains no person2
        _groupA = _db.load(ManyGroupKeyGen.class, new Integer(_groupAId));
        Iterator < ManyPersonKeyGen > groupItor = _groupA.getPeople().iterator();
        while (groupItor.hasNext()) {
            _person2 = groupItor.next();
            if (_person2.getId() == _person2Id) {
                fail("Error: person2 is not removed");
            }
        }
        _groupB = _db.load(ManyGroupKeyGen.class, new Integer(_groupBId));
        if ((_groupB.getPeople() != null) && (_groupB.getPeople().size() != 0)) {
            fail("Error: person2 is not removed");
        }

        // make a dangerous add (add to only one side)
        // user shouldn't rely on this behavior, but
        // should always link both side before commit
        _person1 = _db.load(ManyPersonKeyGen.class, new Integer(_person1Id));
        _person1.getGroup().add(_groupB);
        _db.commit();
    }

    private void check5() throws PersistenceException {
        // check if adding group into existing collection work
        _db.begin();
        _person1 = _db.load(ManyPersonKeyGen.class, new Integer(_person1Id));
        Iterator < ManyGroupKeyGen > tempItor = _person1.getGroup().iterator();
        if (!tempItor.hasNext()) {
            fail("Error: expected group from person1 not found");
        }
        _groupA = tempItor.next();
        int tempGroupId = _groupA.getId();
        if ((tempGroupId != _groupAId) && (tempGroupId != _groupBId)) {
            fail("Error: unexpected group from person1 found");
        }

        if (!tempItor.hasNext()) {
            fail("Error: expected group from person1 not found");
        }
        _groupA = tempItor.next();
        if (tempGroupId == _groupA.getId()) {
            fail("Error: duplicated group found!");
        }
        if ((_groupA.getId() != _groupAId) && (_groupA.getId() != _groupBId)) {
            fail("Error: unexpected group from person1 found");
        }
        _db.commit();
    }

    private void check6() throws PersistenceException {
        // test long transaction support
        _db.begin();
        _groupA = _db.load(ManyGroupKeyGen.class, new Integer(_groupAId));
        _db.commit();

        LOG.debug("Modifing object outside of transaction");
        // remove person 3
        Iterator < ManyPersonKeyGen > it = _groupA.getPeople().iterator();
        while (it.hasNext()) {
            _person3 = it.next();
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
            _person1 = it.next();
            if (_person1.getId() == _person1Id) {
                break;
            }
        }
        _person1.setValue1("New new value for person 1");

        LOG.debug("Update object to a new transaction");
        _db.setAutoStore(true);
        _db.begin();
        _db.update(_groupA);
        _db.commit();

        _person4Id = _person4.getId();
    }

    private void check7() throws PersistenceException {
        LOG.debug("Load the objects again to see if changes done are effective");
        _db.begin();
        _oql.bind(_groupAId);
        _groupA = null;
        _person1 = null;
        _person2 = null;
        QueryResults enumeration = _oql.execute();
        if (enumeration.hasMore()) {
            _groupA = (ManyGroupKeyGen) enumeration.next();
            LOG.debug("Retrieved object: " + _groupA);
            Collection < ManyPersonKeyGen > p = _groupA.getPeople();
            if (p != null) {
                Iterator < ManyPersonKeyGen > itor = p.iterator();
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
                if ((_person1.getId() == _person4Id) && (_person4.getId() == _person1Id)) {
                    ManyPersonKeyGen temp = _person1;
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

                if (_person1.getId() == _person1Id) {
                    // check if the value is valid for person1 and chnage value
                    // of person1
                    if ((_person1.getValue1() == null)
                            || !_person1.getValue1().equals("New new value for person 1")) {
                        fail("Error: unexpected person value");
                    }
                } else {
                    fail("Error: people in group is not the same as expected!");
                }

                if (_person4.getId() == _person4Id) {
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
                fail("Error: related object not found!");
            }
        } else {
            fail("Error: object not found!");
        }
        _person3 = _db.load(ManyPersonKeyGen.class, new Integer(_person3Id));
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
        _person3 = _db.load(ManyPersonKeyGen.class, new Integer(_person3Id));
        Iterator < ManyGroupKeyGen > tempItor = _person3.getGroup().iterator();
        if (!tempItor.hasNext()) {
            fail("Error: group not found");
        }
        _groupA = tempItor.next();
        if (_groupA.getId() != _groupAId) {
            fail("Error: unexpected group found");
        }
        if (tempItor.hasNext()) {
            fail("Error: too many group");
        }
        _db.commit();
    }
}
