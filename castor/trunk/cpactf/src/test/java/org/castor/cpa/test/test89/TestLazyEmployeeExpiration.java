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
package org.castor.cpa.test.test89;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.castor.cpa.test.framework.CPATestCase;
import org.castor.cpa.test.framework.xml.types.DatabaseEngineType;
import org.exolab.castor.jdo.CacheManager;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.persist.spi.Identity;

/**
 * Expire Cache test. Tests the ability to clear objects from the cache. This
 * includes clearing objects by class or type, or individually, by object
 * identities.
 */
public final class TestLazyEmployeeExpiration extends CPATestCase {
    private static final Log LOG = LogFactory
            .getLog(TestLazyEmployeeExpiration.class);

    private static final boolean BY_TYPE_OR_CLASS = true;
    private static final boolean BY_OBJECT_IDENTITY = false;

    private static final Date JDO_ORIGINAL_DATE = Date.valueOf("2000-01-02");
    private static final Date JDO_UPDATED_DATE = Date.valueOf("2001-03-04");
    private static final Date JDBC_UPDATED_DATE = Date.valueOf("2002-05-06");

    private static final String JDO_ORIGINAL_STRING = "Original JDO String";
    private static final String JDO_UPDATED_STRING = "Updated Using JDO";
    private static final String JDBC_UPDATED_STRING = "Updated Using JDBC";

    private static final String DBNAME = "test89";
    private static final String MAPPING = "/org/castor/cpa/test/test89/mapping.xml";
    private Database _db;

    public TestLazyEmployeeExpiration(final String name) {
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

    public void testExpireCache() throws Exception {
        expireCache(BY_OBJECT_IDENTITY);
        expireCache(BY_TYPE_OR_CLASS);
    }

    /**
     * Test the expire cache logic. This method includes steps to 1) create a
     * test data set, 2) enter read and write transactions (to test the proper
     * execution of the expire cache logic while transactions are in progress),
     * 3) update several objects outside of JDO using JDBC, 5) validate that
     * subsequent read/write operations on the objects expired from the cache
     * actually reflect values from the backend database, and, finally, 6)
     * remove the test data set.
     * 
     * @param expireByType
     *            when <code>true</code> this method will expire objects from
     *            the cache by specifying a single type or class of objects to
     *            be expired; when <code>false</code> this method will expire
     *            objects from the cache using individual object identities
     * @throws Exception
     */
    public void expireCache(final boolean expireByType) throws Exception {

        LOG.info("starting testExpireCache "
                + (expireByType ? "by type" : "by object identity"));

        // delete any data left over from previous tests
        deleteTestDataSet();
        createTestDataSet();

        // now update the database outside of JDO
        updatePersonUsingJDBC("First", "Person", JDBC_UPDATED_DATE);
        updateEmplUsingJDBC("First", "Person", JDBC_UPDATED_DATE);

        updateAddrUsingJDBC(1, Integer.toString(1) + JDBC_UPDATED_STRING);
        updateAddrUsingJDBC(2, Integer.toString(2) + JDBC_UPDATED_STRING);
        updateAddrUsingJDBC(3, Integer.toString(3) + JDBC_UPDATED_STRING);

        // and force the cache to expire
        expire(expireByType);

        // validate that cached field values are not used in
        // subsequent read/write operations
        boolean success = true;
        if (!validReadTransaction("First", "Person")) {
            success = false;
        }
        if (!validWriteTransaction("First", "Person")) {
            success = false;
        }

        if (success) {
            LOG.info("Test Completed Successfully.");
        } else {
            fail("Cache was not properly expired");
        }
        deleteTestDataSet();

    }

    private void createTestDataSet() throws Exception {
        LOG.info("creating test data set...");
        _db.begin();

        LazyEmployee person = new LazyEmployee();
        person.setFirstName("First");
        person.setLastName("Person");
        person.setBirthday(JDO_ORIGINAL_DATE);
        person.setStartDate(JDO_ORIGINAL_DATE);

        LazyAddress address1 = new LazyAddress();
        address1.setId(1);
        address1.setStreet(Integer.toString(1) + JDO_ORIGINAL_STRING);
        address1.setCity("First City");
        address1.setState("AB");
        address1.setZip("10000");
        address1.setPerson(person);

        LazyAddress address2 = new LazyAddress();
        address2.setId(2);
        address2.setStreet(Integer.toString(2) + JDO_ORIGINAL_STRING);
        address2.setCity("Second City");
        address2.setState("BC");
        address2.setZip("22222");
        address2.setPerson(person);

        LazyAddress address3 = new LazyAddress();
        address3.setId(3);
        address3.setStreet(Integer.toString(3) + JDO_ORIGINAL_STRING);
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
        con.setComment("80 hours a week, no pay hoilday, "
                + "no sick leave, arrive office at 7:30am everyday");
        con.setContractNo(78);
        con.setEmployee(person);
        con.setCategory(category);
        person.setContract(con);
        _db.create(person);

        _db.commit();
    }

    /**
     * Update a person outside of JDO using a JDBC connection.
     * 
     * @param firstName
     *            first part of primary key of object to be updated
     * @param lastName
     *            first part of primary key of object to be updated
     * @param newBirthdate
     *            new value of persons birthdate
     * @throws Exception
     */
    private void updatePersonUsingJDBC(final String firstName,
            final String lastName, final Date newBirthdate) throws Exception {

        LOG.info("updatePersonUsingJDBC: updating " + firstName + " "
                + lastName);
        _db.begin();
        PreparedStatement updatePersonStatement = _db
                .getJdbcConnection()
                .prepareStatement(
                    "update test89_pks_person set bday=? where fname=? and lname=?");
        updatePersonStatement.setDate(1, newBirthdate);
        updatePersonStatement.setString(2, firstName);
        updatePersonStatement.setString(3, lastName);
        int rc = updatePersonStatement.executeUpdate();
        if (rc <= 0) {
            LOG.error(//
                    "updatePersonUsingJDBC: error updating person, return = "
                            + rc);
            return;
        }
        _db.commit();

    }

    /**
     * Update a person outside of JDO using a JDBC connection.
     * 
     * @param firstName
     *            first part of primary key of object to be updated
     * @param lastName
     *            first part of primary key of object to be updated
     * @param newStartDate
     *            new value of persons birthdate
     * @throws Exception
     */
    private void updateEmplUsingJDBC(final String firstName,
            final String lastName, final Date newStartDate) throws Exception {

        LOG.info("updateEmployeeUsingJDBC: updating " + firstName + " "
                + lastName);
        _db.begin();
        PreparedStatement updateEmployeeStatement = _db
                .getJdbcConnection()
                .prepareStatement(
                    "update test89_pks_employee set start_date=? where fname=? and lname=?");
        updateEmployeeStatement.setDate(1, newStartDate);
        updateEmployeeStatement.setString(2, firstName);
        updateEmployeeStatement.setString(3, lastName);
        int rc = updateEmployeeStatement.executeUpdate();
        if (rc <= 0) {
            LOG.error("updateEmplUsingJDBC: error updating employee, return = "
                    + rc);
            return;
        }
        _db.commit();
    }

    /**
     * Update an address outside of JDO using a JDBC connection.
     * 
     * @param addressId
     *            primary key of object to be updated
     * @param newStreet
     *            new value of addresses street field
     * @throws Exception
     */
    private void updateAddrUsingJDBC(final int addressId, final String newStreet)
            throws Exception {
        LOG.info("updateAddressUsingJDBC: updating " + addressId);
        _db.begin();
        PreparedStatement updateAddressStatement = _db
        .getJdbcConnection()
        .prepareStatement(
                "update test89_pks_address set street=? where id=?");
        updateAddressStatement.setString(1, newStreet);
        updateAddressStatement.setInt(2, addressId);
        int rc = updateAddressStatement.executeUpdate();
        if (rc <= 0) {
            LOG.error(//
                    "updateAddrUsingJDBC: error updating address, return = "
                            + rc);
            return;
        }
        _db.commit();
    }

    /**
     * Setup and execute a database expire cache request.
     * 
     * @param byType
     *            when <code>true</code> this method will request that objects
     *            be expired from the cache by specifying a single type or class
     *            of objects to be expired; when <code>false</code> this method
     *            will request that objects be expired from the cache using
     *            individual object identities
     */
    private void expire(final boolean byType) {
        LOG.info("expiring cache...");

        try {
            CacheManager cacheManager = _db.getCacheManager();
            if (byType) {
                Class<?>[] typeArray = new Class[5];
                typeArray[0] = LazyContract.class;
                typeArray[1] = LazyContractCategory.class;
                typeArray[2] = LazyPayRoll.class;
                typeArray[3] = LazyAddress.class;
                typeArray[4] = LazyEmployee.class;
                cacheManager.expireCache(typeArray);
            } else {
                Object[] identityArray = new Object[1];
                identityArray[0] = new Identity("First", "Person");
                cacheManager.expireCache(LazyEmployee.class, identityArray);
            }
        } catch (Exception e) {
            LOG.error("expireCache: exception encountered clearing cache", e);
        }
    }

    /**
     * Read, or load, an object and validate that the field values reflect
     * values updated outside of JDO and not from previously cached values.
     * 
     * @param firstName
     *            primary key of object to be read
     * @param lastName
     *            primary key of object to be read
     * @return True if read transaction is valid.
     */
    private boolean validReadTransaction(final String firstName,
            final String lastName) {
        LOG.info("validating read transaction for person " + firstName + " "
                + lastName + "...");
        boolean valid = true;

        try {
            _db.begin();

            Identity fullname = new Identity("First", "Person");
            LazyEmployee person;

            person = _db.load(LazyEmployee.class, fullname);
            if (person.getBirthday().compareTo(JDBC_UPDATED_DATE) != 0) {
                LOG.debug("validReadTransaction: birthdate for "
                        + person.getFirstName() + " " + person.getLastName()
                        + " does not match expected value, value: "
                        + person.getBirthday().toString() + ", expected: "
                        + JDBC_UPDATED_DATE.toString());
                valid = false;
            }
            if (person.getStartDate().compareTo(JDBC_UPDATED_DATE) != 0) {
                LOG.debug("validReadTransaction: start date for "
                        + person.getFirstName() + " " + person.getLastName()
                        + " does not match expected value, value: "
                        + person.getStartDate().toString() + ", expected: "
                        + JDBC_UPDATED_DATE.toString());
                valid = false;
            }
            Iterator<LazyAddress> itor = person.getAddress().iterator();
            while (itor.hasNext()) {
                LazyAddress address = itor.next();
                String expectedStreet = new String(Integer.toString(address
                        .getId())
                        + JDBC_UPDATED_STRING);
                if (address.getStreet().compareTo(expectedStreet) != 0) {
                    LOG.debug("validReadTransaction: street in address "
                            + address.getId()
                            + " does not match expected value, value: "
                            + address.getStreet() + ", expected: "
                            + expectedStreet);
                    valid = false;
                }
            }
            _db.rollback();
        } catch (Exception e) {
            LOG.error(
                "validReadTransaction: exception while validating read for "
                        + firstName + " " + lastName, e);
            valid = false;
        }
        return valid;
    }

    /**
     * Modify fields of an object and validate that the transaction completes
     * successfully.
     * 
     * @param firstName
     *            First name.
     * @param lastName
     *            Last name.
     * @return True if write transaction is valid.
     */
    private boolean validWriteTransaction(final String firstName,
            final String lastName) {
        LOG.info("validating write transaction for group " + firstName + " "
                + lastName + "...");
        try {
            _db.begin();
            Identity fullname = new Identity("First", "Person");
            LazyEmployee person;

            person = _db.load(LazyEmployee.class, fullname);
            person.setBirthday(JDO_UPDATED_DATE);
            Iterator<LazyAddress> itor = person.getAddress().iterator();
            while (itor.hasNext()) {
                LazyAddress address = itor.next();
                String newStreet = new String(Integer.toString(address.getId())
                        + JDO_UPDATED_STRING);
                address.setStreet(newStreet);
            }
            _db.commit();
        } catch (Exception e) {
            LOG.error(
                "validWriteTransaction: exception while validating write for "
                        + firstName + " " + lastName, e);
            return false;
        }
        return true;
    }

    /**
     * Delete all objects in test data set.
     * 
     * @throws Exception 
     */
    private void deleteTestDataSet() throws Exception {
        LOG.info("deleting test data set...");
        _db.begin();
        Statement stmt = _db.getJdbcConnection().createStatement();
        stmt.executeUpdate("DELETE FROM test89_pks_person");
        stmt.executeUpdate("DELETE FROM test89_pks_employee");
        stmt.executeUpdate("DELETE FROM test89_pks_payroll");
        stmt.executeUpdate("DELETE FROM test89_pks_address");
        stmt.executeUpdate("DELETE FROM test89_pks_contract");
        stmt.executeUpdate("DELETE FROM test89_pks_category");
        stmt.executeUpdate("DELETE FROM test89_pks_project");
        _db.commit();
    }
}