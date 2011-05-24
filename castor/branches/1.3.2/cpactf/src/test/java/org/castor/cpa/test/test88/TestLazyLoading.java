/*
 * Copyright 2008 Udai Gupta
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
package org.castor.cpa.test.test88;

import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.castor.cpa.test.framework.CPATestCase;
import org.castor.cpa.test.framework.xml.types.DatabaseEngineType;
import org.castor.persist.proxy.LazyCollection;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.DuplicateIdentityException;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.TransactionAbortedException;
import org.exolab.castor.persist.spi.Identity;

/**
 * Test for lazy loading of collection supported by Castor. The object in a lazy
 * collection is not loaded from the DBMS until is it requested by the
 * collection.
 */
public final class TestLazyLoading extends CPATestCase {
    private static final Log LOG = LogFactory.getLog(TestLazyLoading.class);

    private static final String DBNAME = "test88";
    private static final String MAPPING = "/org/castor/cpa/test/test88/mapping.xml";

    private Database _db;

    public TestLazyLoading(final String name) {
        super(name);
    }

    // Test are only included/excluded for engines that have been tested with this test suite.

    public boolean include(final DatabaseEngineType engine) {
        return (engine == DatabaseEngineType.DERBY)
            || (engine == DatabaseEngineType.HSQL)
            || (engine == DatabaseEngineType.MYSQL)
            || (engine == DatabaseEngineType.ORACLE)
            || (engine == DatabaseEngineType.POSTGRESQL);
    }

    public void setUp() throws Exception {
        _db = getJDOManager(DBNAME, MAPPING).getDatabase();
        _db.begin();
        // delete everything directly
        LOG.info("Delete everything");
        Statement stmt = _db.getJdbcConnection().createStatement();
        stmt.executeUpdate("DELETE FROM test88_pks_person");
        stmt.executeUpdate("DELETE FROM test88_pks_employee");
        stmt.executeUpdate("DELETE FROM test88_pks_payroll");
        stmt.executeUpdate("DELETE FROM test88_pks_address");
        stmt.executeUpdate("DELETE FROM test88_pks_category_contract");
        stmt.executeUpdate("DELETE FROM test88_pks_contract");
        stmt.executeUpdate("DELETE FROM test88_pks_category");
        stmt.executeUpdate("DELETE FROM test88_pks_project");
        _db.commit();
        createDataObjects();
    }

    public void tearDown() throws Exception {
        if (_db.isActive()) {
            _db.rollback();
        }
        _db.close();
    }

    public void testGeneral() throws PersistenceException {
        LOG.info("Running testGeneral...");

        Calendar cal = Calendar.getInstance();
        Identity fullname = new Identity("First", "Person");
        LazyEmployee loadPerson;

        _db.begin();
        loadPerson = _db.load(LazyEmployee.class, fullname);

        cal.set(1922, 2, 2, 0, 0, 0);
        cal.set(Calendar.MILLISECOND, 0);
        if (loadPerson.getBirthday().equals(cal.getTime())
                && loadPerson.getFirstName().equals("First")
                && loadPerson.getLastName().equals("Person")) {
            LOG.info("OK: Employee is valid");

            Collection<LazyAddress> address = loadPerson.getAddress();
            Iterator<LazyAddress> itor = address.iterator();
            LazyAddress[] addresses = {null, null, null};
            LazyAddress addr;
            while (itor.hasNext()) {
                addr = itor.next();
                if ((addr.getId() < 1) || (addr.getId() > 3)) {
                    LOG.error("Error: Address id is incorrect");
                    _db.rollback();
                    fail("address id is incorrect");
                }
                addresses[addr.getId() - 1] = addr;
            }

            if ((addresses[0] == null)
                    || !addresses[0].getStreet().equals("#1 Address Street")
                    || !addresses[0].getCity().equals("First City")
                    || !addresses[0].getState().equals("AB")
                    || !addresses[0].getZip().equals("10000")
                    || (addresses[0].getPerson() != loadPerson)) {
                LOG.error("Error: Address 1 is incorrect: " + addresses[0]);
                _db.rollback();
                fail("address 1 is incorrect");
            }
            LOG.info("OK: Address 1 are valid");

            if ((addresses[1] == null)
                    || !addresses[1].getStreet().equals("2nd Ave")
                    || !addresses[1].getCity().equals("Second City")
                    || !addresses[1].getState().equals("BC")
                    || !addresses[1].getZip().equals("22222")
                    || (addresses[1].getPerson() != loadPerson)) {
                LOG.error("Error: Address 2 is incorrect");
                _db.rollback();
                fail("address 2 is incorrect");
            }
            LOG.info("OK: Address 2 are valid");

            checkGeneral(loadPerson);

            // now modified the object and store it
            address.remove(addresses[0]);
            addresses[1].setStreet("New Second Street");
        } else {
            LOG.error("Error: FirstName, LastName or Birthday is incorrect!");
            _db.rollback();
            fail("FirstName, LastName or Birthday is incorrect!");
        }
        _db.commit();

        // test and see if changes made succeed
        _db.begin();
        loadPerson = _db.load(LazyEmployee.class, fullname);

        cal.set(1922, 2, 2, 0, 0, 0);
        cal.set(Calendar.MILLISECOND, 0);
        if (loadPerson.getBirthday().equals(cal.getTime())
                && loadPerson.getFirstName().equals("First")
                && loadPerson.getLastName().equals("Person")) {
            LOG.info("OK: Employee is valid");

            Collection<LazyAddress> address = loadPerson.getAddress();
            Iterator<LazyAddress> itor = address.iterator();
            LazyAddress[] addresses = {null, null, null};
            LazyAddress addr;
            while (itor.hasNext()) {
                addr = itor.next();
                if ((addr.getId() < 1) || (addr.getId() > 3)) {
                    LOG.error("Error: Address id is incorrect");
                    _db.rollback();
                    fail("address id is incorrect");
                }
                addresses[addr.getId() - 1] = addr;
            }

            if (addresses[0] != null) {
                LOG.error("Error: Address 1 is not deleted: " + addresses[0]);
                fail("address 1 is not deleted");
            }
            LOG.info("OK: Address 1 is deleted");

            if ((addresses[1] == null)
                    || !addresses[1].getStreet().equals("New Second Street")
                    || !addresses[1].getCity().equals("Second City")
                    || !addresses[1].getState().equals("BC")
                    || !addresses[1].getZip().equals("22222")
                    || (addresses[1].getPerson() != loadPerson)) {
                LOG.error("Error: Address 2 is incorrect");
                _db.rollback();
                fail("address 2 is incorrect");
            }
            LOG.info("OK: Address 2 are valid: " + addresses[1]);

            checkGeneral(loadPerson);
        } else {
            LOG.error("Error: FirstName, LastName or Birthday is incorrect!");
            _db.rollback();
            fail("FirstName, LastName or Birthday is incorrect!");
        }
        _db.commit();
    }

    public void testCollection() throws PersistenceException {
        LOG.info("Running testCollection...");

        Identity fullname = new Identity("First", "Person");
        LazyPerson loadPerson;

        // test java.util.Collection.clear() for lazy loading (bug 801)
        _db.begin();
        loadPerson = _db.load(LazyEmployee.class, fullname);

        Collection<LazyAddress> addresses = loadPerson.getAddress();
        addresses.clear();
        _db.commit();

        _db.begin();
        loadPerson = _db.load(LazyEmployee.class, fullname);
        addresses = loadPerson.getAddress();

        // check if clear() work
        if (!addresses.isEmpty()) {
            LOG.error("Error: Collection.clear() is not working!");
            fail("Error: Collection.clear() is not working!");
        }

        // modify the collection to test java.util.Collection.addAll()
        // for lazy loading (bug 801)
        Collection<LazyAddress> c = new ArrayList<LazyAddress>();

        LazyAddress address = new LazyAddress();
        address.setId(101);
        address.setStreet("Mattrew Street");
        address.setCity("Rome City");
        address.setState("RM");
        address.setZip("10000");
        address.setPerson(loadPerson);
        c.add(address);

        address = new LazyAddress();
        address.setId(102);
        address.setStreet("Luke Street");
        address.setCity("Rome City");
        address.setState("RM");
        address.setZip("10000");
        address.setPerson(loadPerson);
        c.add(address);

        address = new LazyAddress();
        address.setId(103);
        address.setStreet("John Street");
        address.setCity("Rome City");
        address.setState("RM");
        address.setZip("10000");
        address.setPerson(loadPerson);
        c.add(address);

        addresses.addAll(c);
        _db.commit();

        // check if add all work
        _db.begin();
        loadPerson = _db.load(LazyEmployee.class, fullname);
        addresses = loadPerson.getAddress();
        Iterator<LazyAddress> itor = addresses.iterator();

        boolean hasAddr1, hasAddr2, hasAddr3;
        hasAddr1 = false;
        hasAddr2 = false;
        hasAddr3 = false;
        while (itor.hasNext()) {
            address = itor.next();
            if (address.getId() == 101) {
                hasAddr1 = true;
            } else if (address.getId() == 102) {
                hasAddr2 = true;
            } else if (address.getId() == 103) {
                hasAddr3 = true;
            } else {
                LOG.error("Error: Address with unexpected id is found! "
                        + address);
                fail("Erorr: Address with unexpected id is found! " + address);
            }
        }
        if (!hasAddr1 || !hasAddr2 || !hasAddr3) {
            LOG.error("Error: Collection.addAll( Collection ) fail");
            fail("Error: Collection.addAll( Collection ) fail");
        }
        _db.commit();
    }

    public void testComplex() throws PersistenceException {
        LOG.info("Running testComplex...");

        Identity fullname = new Identity("First", "Person");
        LazyEmployee loadPerson;

        // set up the data object
        _db.begin();
        loadPerson = _db.load(LazyEmployee.class, fullname);
        Collection<LazyProject> projects = loadPerson.getProjects();

        LazyProject project = new LazyProject();
        project.setId(1001);
        project.setName("Project One");
        project.setOwner(loadPerson);
        projects.add(project);
        _db.create(project);

        project = new LazyProject();
        project.setId(1002);
        project.setName("Project Two");
        project.setOwner(loadPerson);
        projects.add(project);
        _db.create(project);

        project = new LazyProject();
        project.setId(1003);
        project.setName("Project Three");
        project.setOwner(loadPerson);
        projects.add(project);
        _db.create(project);

        _db.commit();

        // reload and test bug 823
        _db.begin();
        project = _db.load(LazyProject.class, new Integer(1002));
        _db.remove(project);

        loadPerson = _db.load(LazyEmployee.class, fullname);
        projects = loadPerson.getProjects();

        Iterator<LazyProject> itor = projects.iterator();
        while (itor.hasNext()) {
            project = itor.next();
            if (project.getId() == 1002) {
                itor.remove();
                break;
            }
        }

        itor = projects.iterator();
        while (itor.hasNext()) {
            project = itor.next();
            if (project.getId() == 1002) {
                itor.remove();
                break;
            }
        }
        _db.commit();

        // reload and make sure the cache is consistent
        _db.begin();
        loadPerson = _db.load(LazyEmployee.class, fullname);
        projects = loadPerson.getProjects();
        itor = projects.iterator();
        int id1 = 0;
        if (itor.hasNext()) {
            project = itor.next();
            id1 = project.getId();
            if ((project.getId() != 1001) && (project.getId() != 1003)) {
                LOG.error("Error: found project1 with unexpected id "
                        + project.getId());
                fail("Error: found project1 with unexpected id "
                        + project.getId());
            }
        } else {
            LOG.error("Error: expected project is not found");
            fail("Error: expected project is not found");
        }

        if (itor.hasNext()) {
            project = itor.next();
            if ((project.getId() == id1)
                    || ((project.getId() != 1001) && (project.getId() != 1003))) {
                LOG.error("Error: found project2 with unexpected id "
                        + project.getId());
                fail("Error: found project2 with unexpected id "
                        + project.getId());
            }
        } else {
            LOG.error("Error: expected project is not found");
            fail("Error: expected project is not found");
        }

        if (itor.hasNext()) {
            project = itor.next();
            LOG.error("Error: unexpected project is found: " + project.getId());
            fail("Error: unexpected project is found: " + project.getId());
        }
        _db.commit();
    }

    public void testIterWithAdd() throws PersistenceException {
        LOG.info("Running testIterWithAdd...");

        // Tests iterating over a lazy-loaded Collection that has
        // had data added

        ArrayList<LazyAddress> masterData = new ArrayList<LazyAddress>();
        Identity fullname = new Identity("First", "Person");
        LazyPerson loadPerson;

        // test java.util.Collection.clear() for lazy loading (bug 801)
        _db.begin();
        loadPerson = _db.load(LazyEmployee.class, fullname);

        Collection<LazyAddress> addresses = loadPerson.getAddress();
        // Store the list in the database at the start of the transaction,
        // for comparison purposes
        Iterator<LazyAddress> it = addresses.iterator();
        while (it.hasNext()) {
            masterData.add(it.next());
        }

        _db.rollback();

        // Now start over, and add something to the collection. Then try
        // iterating and clearing the collection
        _db.begin();
        loadPerson = _db.load(LazyEmployee.class, fullname);
        addresses = loadPerson.getAddress();
        LazyAddress la = new LazyAddress();
        la.setId(999);
        la.setStreet("Rogue Street");
        la.setCity("Rogue City");
        la.setState("RS");
        la.setZip("10666");
        la.setPerson(loadPerson);
        addresses.add(la);

        LOG.debug("masterData size: " + masterData.size());
        LOG.debug("addresses size: " + addresses.size());
        if (addresses.size() != (masterData.size() + 1)) {
            fail("Lazy collection size is different from what is expected");
        }

        boolean matchNewElement = false;
        int matchCount = 0;
        it = addresses.iterator();

        /*
         * The problem with the following block is that the second loop is
         * entered but never exited.
         */

        bigloop: while (it.hasNext()) {
            LazyAddress addr1 = it.next();

            Iterator<LazyAddress> it2 = masterData.iterator();
            while (it2.hasNext()) {
                LazyAddress addr2 = it2.next();

                LOG.debug("addr1: " + addr1);
                LOG.debug("addr2: " + addr2);

                if (addr2.equals(addr1)) {
                    LOG.debug("matched");
                    matchCount++;
                    continue bigloop;
                } else if (addr1 == la) {
                    LOG.debug("matched lazy");
                    matchNewElement = true;
                    matchCount++;
                    continue bigloop;
                } else {
                    LOG.debug("no match");
                }
            }
            LOG.debug("newly added:" + la + "@" + System.identityHashCode(la));
            LOG.debug("matchNewElement " + matchNewElement);
            LOG.debug("matchCount " + matchCount);

            LOG
                    .error("Error: found unexpected address in the new lazy collection");
            fail("found unexpected address in the new lazy collection:" + addr1
                    + "@" + System.identityHashCode(la));
        }

        if (!matchNewElement) {
            LOG.error("Error: Newly added element is missing");
            fail("Newly added element is missing");
        }

        if (matchCount != (masterData.size() + 1)) {
            LOG
                    .error("Error: Lazy collection contains unexpected number of elements");
            fail("Lazy collection contains unexpected number of elements. expected: "
                    + (masterData.size() + 1) + " found: " + matchCount);
        }

        addresses.clear();
        if (!addresses.isEmpty()) {
            LOG.error("Error: clear failed in testIterWithAdd");
            fail("Error: clear failed in testIterWithAdd");
        }

        _db.rollback();
    }

    public void testIterWithDelete() throws PersistenceException {
        LOG.info("Running testIterWithDelete...");

        // Tests iterating over a lazy-loaded Collection that has
        // had data deleted
        ArrayList<LazyAddress> masterData = new ArrayList<LazyAddress>();
        Identity fullname = new Identity("First", "Person");
        LazyEmployee loadPerson;

        // First add a record, then commit
        _db.begin();
        loadPerson = _db.load(LazyEmployee.class, fullname);
        Collection<LazyAddress> addresses = loadPerson.getAddress();
        LazyAddress la = new LazyAddress();
        la.setId(999);
        la.setStreet("Rogue Street");
        la.setCity("Rogue City");
        la.setState("RS");
        la.setZip("10666");
        la.setPerson(loadPerson);
        addresses.add(la);
        _db.commit();

        // New transaction
        _db.begin();

        // test java.util.Collection.clear() for lazy loading (bug 801)
        loadPerson = _db.load(LazyEmployee.class, fullname);

        addresses = loadPerson.getAddress();
        // Store the list in the database at the start of the transaction,
        // for comparison purposes. Select a victim for deletion.
        Iterator<LazyAddress> it = addresses.iterator();
        // victim is last element to test bug 1022
        int victim = addresses.size() - 1;
        int recNo = 0;
        LazyAddress victimAddr = null;
        while (it.hasNext()) {
            LazyAddress addr = it.next();
            if (recNo++ == victim) {
                victimAddr = addr;
            } else {
                masterData.add(addr);
            }
        }

        _db.rollback();

        // Now start over, and add something to the collection. Then try
        // iterating and clearing the collection
        _db.begin();
        loadPerson = _db.load(LazyEmployee.class, fullname);
        addresses = loadPerson.getAddress();
        addresses.remove(victimAddr);

        Iterator<LazyAddress> it2 = addresses.iterator();

        while (it2.hasNext()) {

            LazyAddress addr = it2.next();
            if (addr.equals(victimAddr)) {
                LOG
                        .error("Error: Deleted record should not show up in iteration");
                fail("Error: Deleted record should not show up in iteration");
            } else if (!masterData.remove(addr)) {
                LOG
                        .error("Error: unrecognized element from list in testIterWithDelete");
                fail("Error: unrecognized element from list in testIterWithDelete");
            }
        }

        _db.rollback();

        if (!masterData.isEmpty()) {
            LOG.error("Error: iteration/deletion failed in testIterWithDelete");
            fail("Error: iteration/deletion failed in testIterWithDelete");
        }
    }

    public void testLazyCollectionRollback() throws PersistenceException {
        LOG.info("Running testLazyCollectionRollback...");

        Identity fullname = new Identity("First", "Person");
        LazyEmployee loadPerson;

        // set up the data object
        _db.begin();
        loadPerson = _db.load(LazyEmployee.class, fullname);
        Collection<LazyProject> projects = loadPerson.getProjects();
        LazyProject project = new LazyProject();

        project = new LazyProject();
        project.setId(1002);
        project.setName("Project Two");
        project.setOwner(loadPerson);
        projects.add(project);
        _db.create(project);

        _db.commit();

        _db.begin();
        loadPerson = _db.load(LazyEmployee.class, fullname);
        projects = loadPerson.getProjects();

        // is the collection populated?
        assertTrue("The projects collection is not valid!.", (projects != null)
                && (projects.size() > 0));

        // is the collection instanceof Lazy?
        assertTrue(
                "Collection has to be lazy! It is "
                        + loadPerson.getProjects().getClass(),
                loadPerson.getProjects() instanceof LazyCollection);

        // OK, the collection of projects is there, let's test a rollback for
        // bug #1046
        LOG.info("Rolling back transaction");
        _db.rollback();

        // test it again since the rollback - is the collection instanceof Lazy?
        assertTrue(
                "Collection has to be lazy! It is "
                        + loadPerson.getProjects().getClass(),
                loadPerson.getProjects() instanceof LazyCollection);
    }

    public void testMasterUpdate() throws PersistenceException {
        Identity fullname = new Identity("First", "Person");
        LazyPerson loadPerson;

        // 1. load master object, add a new dependent object and commit
        _db.begin();
        loadPerson = _db.load(LazyEmployee.class, fullname);
        LazyAddress address = new LazyAddress();
        address.setId(201);
        address.setStreet("Halfpipe 1");
        address.setCity("Skater Heaven");
        address.setPerson(loadPerson);
        loadPerson.getAddress().add(address);
        _db.commit();

        // 2. reuse master and commit. if we get a DuplicateIdentityException,
        // the dependent object (id=201) was created twice.
        _db.begin();
        _db.update(loadPerson);
        try {
            _db.commit();
        } catch (TransactionAbortedException e) {
            if (e.getCause() instanceof DuplicateIdentityException) {
                LOG
                        .error("Error: The dependent object Address was just duplicated");
                fail("The dependent object Address was just duplicated");
            } else {
                throw e;
            }
        }
        // No DuplicateIdentityException means, that just one instance of
        // Address (id=201)
        // was created in the database. That's what we want to have.
    }

/*    public void testManyToMany() throws PersistenceException {
        LOG.info("Running testManyToMany...");

        _db.begin();

        String key = "a1";
        LazyNToNA a1 = (LazyNToNA) _db.load(LazyNToNA.class, key);

        // The object a1 should have two TestLazyNToNB objects in its
        // "refs" collection
        Collection<LazyNToNB> lazyRefs = a1.getRefs();
        if (lazyRefs.size() != 2) {
            LOG
                    .error("Error: incorrect initial collection size in testManyToMany");
            fail("Error: incorrect initial collection size in testManyToMany");
        }

        lazyRefs.clear();
        _db.commit();

        // Now if we re-load the object in a new transaction, there should
        // be no objects in the "refs" collection

        _db.begin();
        a1 = (LazyNToNA) _db.load(LazyNToNA.class, key);

        // The object a1 should have two TestLazyNToNB objects in its
        // "refs" collection
        lazyRefs = a1.getRefs();
        if (lazyRefs.size() > 2) {
            LOG
                    .error("Error: incorrect final collection size in testManyToMany");
            fail("Error: incorrect final collection size in testManyToMany");
        }

        _db.commit();
    }*/

    private void checkGeneral(final LazyEmployee loadPerson)
            throws PersistenceException {
        LazyPayRoll payroll = loadPerson.getPayRoll();
        if ((payroll == null) || (payroll.getId() != 1)
                || (payroll.getHoliday() != 15)
                || (payroll.getEmployee() != loadPerson)
                || (payroll.getHourlyRate() != 25)) {
            LOG.error("Error: PayRoll loaded incorrect");
            _db.rollback();
            fail("payroll is incorrect");
        }
        LOG.info("OK: PayRoll is valid");

        LazyContract cont = loadPerson.getContract();
        if ((cont == null) || (cont.getPolicyNo() != 1001)
                || (cont.getEmployee() != loadPerson)
                || (cont.getContractNo() != 78)) {
            LOG.error("Error: Contract is not what expected!");
            LOG.debug("employe==null ? " + cont.getEmployee() + "/"
                    + cont.getEmployee().getFirstName() + "/"
                    + cont.getEmployee().getLastName());
            LOG.debug("loadPerson ? " + loadPerson + "/"
                    + loadPerson.getFirstName() + "/"
                    + loadPerson.getLastName());
            _db.rollback();
            fail("contract is incorrect");
        }
        LOG.info("OK: Contract is valid");

        Collection<LazyContractCategory> catelist = cont.getCategory();
        Iterator<LazyContractCategory> itor = catelist.iterator();
        LazyContractCategory cate;
        while (itor.hasNext()) {
            cate = itor.next();
            if (!(((cate.getId() == 101) && cate.getName().equals(
                    "Full-time slave")) || ((cate.getId() == 102) && cate
                    .getName().equals("Full-time employee")))) {
                LOG.error("Error: Category is incorrect");
                _db.rollback();
                fail("category is incorrect");
            }
        }
        LOG.info("OK: Categories are valid");
    }

    private void createDataObjects() throws PersistenceException {
        Calendar cal = Calendar.getInstance();

        _db.begin();
        // create person 1
        LazyEmployee person = new LazyEmployee();
        person.setFirstName("First");
        person.setLastName("Person");
        cal.set(1922, 2, 2, 0, 0, 0);
        cal.set(Calendar.MILLISECOND, 0);
        person.setBirthday(cal.getTime());
        cal.set(2000, 2, 2);
        cal.set(Calendar.MILLISECOND, 0);
        person.setStartDate(cal.getTime());

        LazyAddress address1 = new LazyAddress();
        address1.setId(1);
        address1.setStreet("#1 Address Street");
        address1.setCity("First City");
        address1.setState("AB");
        address1.setZip("10000");
        address1.setPerson(person);

        LazyAddress address2 = new LazyAddress();
        address2.setId(2);
        address2.setStreet("2nd Ave");
        address2.setCity("Second City");
        address2.setState("BC");
        address2.setZip("22222");
        address2.setPerson(person);

        LazyAddress address3 = new LazyAddress();
        address3.setId(3);
        address3.setStreet("3rd Court");
        address3.setCity("Third Ave");
        address3.setState("AB");
        address3.setZip("30003");
        address3.setPerson(person);

        ArrayList<LazyAddress> addresslist = new ArrayList<LazyAddress>();
        addresslist.add(address1);
        addresslist.add(address2);
        addresslist.add(address3);

        person.setAddress(addresslist);

        LazyPayRoll pr1 = new LazyPayRoll();
        pr1.setId(1);
        pr1.setHoliday(15);
        pr1.setHourlyRate(25);
        pr1.setEmployee(person);
        person.setPayRoll(pr1);

        LazyContractCategory cc = new LazyContractCategory();
        cc.setId(101);
        cc.setName("Full-time slave");
        _db.create(cc);

        LazyContractCategory cc2 = new LazyContractCategory();
        cc2.setId(102);
        cc2.setName("Full-time employee");
        _db.create(cc2);
        ArrayList<LazyContractCategory> category = new ArrayList<LazyContractCategory>();
        category.add(cc);
        category.add(cc2);

        LazyContract con = new LazyContract();
        con.setPolicyNo(1001);
        con.setComment("80 hours a week, no pay holiday, "
                + "no sick leave, arrive office at 7:30am everyday");
        con.setContractNo(78);
        con.setEmployee(person);
        con.setCategory(category);
        person.setContract(con);
        _db.create(person);
        _db.commit();
    }

}
