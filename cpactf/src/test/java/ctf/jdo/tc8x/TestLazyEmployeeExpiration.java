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
 * Copyright 1999-2001 (C) Intalio, Inc. All Rights Reserved.
 */
package ctf.jdo.tc8x;

import harness.CastorTestCase;
import harness.TestHarness;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;

import jdo.JDOCategory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.exolab.castor.jdo.CacheManager;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.persist.spi.Identity;

/**
 * Expire Cache test. Tests the ability to clear objects from the cache.  This
 * includes clearing objects by class or type, or individually, by 
 * object identities.
 */
public final class TestLazyEmployeeExpiration extends CastorTestCase {
    /** The <a href="http://jakarta.apache.org/commons/logging/">Jakarta
     *  Commons Logging</a> instance used for all logging. */
    private static final Log LOG = LogFactory.getLog(TestLazyEmployeeExpiration.class);
    
    private static final boolean BY_TYPE_OR_CLASS   = true;
    private static final boolean BY_OBJECT_IDENTITY = false;

    private static final Date JDO_ORIGINAL_DATE  = Date.valueOf("2000-01-02");
    private static final Date JDO_UPDATED_DATE   = Date.valueOf("2001-03-04");
    private static final Date JDBC_UPDATED_DATE  = Date.valueOf("2002-05-06");

    private static final String JDO_ORIGINAL_STRING  = "Original JDO String";
    private static final String JDO_UPDATED_STRING   = "Updated Using JDO";
    private static final String JDBC_UPDATED_STRING  = "Updated Using JDBC";

    private Database            _db;
    private JDOCategory         _category;
    private Connection          _conn;
    private PreparedStatement   _updatePersonStatement;
    private PreparedStatement   _updateEmployeeStatement;
    private PreparedStatement   _updateAddressStatement;
    
    /** 
     * Constructor
     * @param category A {@link TestHarness} instance.
     */
    public TestLazyEmployeeExpiration(final TestHarness category) {
        super(category, "TC89", "Expire Lazy Employee");
        _category = (JDOCategory) category;
    }

    /**
     * Initializes fields
     * @throws SQLException An exception related to the execution of a SQLstatement.
     */
    public void setUp() throws SQLException {
        try {
            _db   = _category.getDatabase();
        } catch (Exception e) {
            LOG.warn("Problem obtaining Datavase instance.", e);
        }
        _conn = _category.getJDBCConnection();

        // initialze JDBC connection
       try {
            _updatePersonStatement = _conn.prepareStatement(
                "update tc8x_pks_person set bday=? where fname=? and lname=?");
            _updateEmployeeStatement = _conn.prepareStatement(
                "update tc8x_pks_employee set start_date=? where fname=? and lname=?");
            _updateAddressStatement = _conn.prepareStatement(
                "update tc8x_pks_address set street=? where id=?");
        } catch (java.sql.SQLException e) {
            fail("Failed to establish JDBC Connection");
        }
    }

    /**
     * Calls the individual tests embedded in this test case
     * @throws PersistenceException ???
     * @throws SQLException An exception related to the execution of a SQLstatement.
     */
    public void runTest() throws PersistenceException, SQLException {
        testExpireCache(BY_OBJECT_IDENTITY);
        testExpireCache(BY_TYPE_OR_CLASS);
    }

    /**
     * Close the database and JDBC connection
     * @throws PersistenceException
     * @throws SQLException An exception related to the execution of a SQLstatement.
     */
    public void tearDown() throws PersistenceException, SQLException {
        if (_db.isActive()) { _db.rollback(); }
        _db.close();
        _conn.close();
    }

    /**
     * Test the expire cache logic.  This method includes steps to 1) create a 
     * test data set, 2) enter read and write transactions (to test the proper
     * execution of the expire cache logic while transactions are in progress),
     * 3) update several objects outside of JDO using JDBC, 5) validate that
     * subsequent read/write operations on the objects expired from the cache
     * actually reflect values from the backend database, and, finally, 
     * 6) remove the test data set.
     *
     * @param expireByType when <code>true</code> this method will expire
     *      objects from the cache by specifying a single type or class of
     *      objects to be expired; when <code>false</code> this method will
     *      expire objects from the cache using individual object identities
     */
    public void testExpireCache(final boolean expireByType) {
        LOG.info("starting testExpireCache "
                + (expireByType ? "by type" : "by object identity"));
        try {

            // delete any data left over from previous tests
            deleteTestDataSet();
            try {
                createTestDataSet();
            } catch (Exception e) {
                // attempt to delete any test data that was created.
                deleteTestDataSet();
                fail("Failed to create test data set");
            }

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
            if (!validReadTransaction("First", "Person")) { success = false; }
            if (!validWriteTransaction("First", "Person")) { success = false; }

            if (success) {
                LOG.info("Test Completed Successfully.");
            } else {
                fail("Cache was not properly expired");
            }
            deleteTestDataSet();
        } catch (Exception e) {
            LOG.error("ERROR: fatal exception encountered during test", e);
            fail("Exception encountered during test");
        }
    }

    /**
     * Create the requisite objects in the database
     * @throws Exception A general exception.
     */
    private void createTestDataSet() throws Exception {
        LOG.info("creating test data set..."); 
        Database db = null;
        try {
            db = this._category.getDatabase();
            db.begin();

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

            ArrayList addresslist = new ArrayList();
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
            db.create(cc);

            LazyContractCategory cc2 = new LazyContractCategory();
            cc2.setId(102);
            cc2.setName("Full-time employee");
            db.create(cc2);
            
            ArrayList category = new ArrayList();
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
            
            db.create(person);
            
            db.commit();
            db.close();
        } catch (Exception e) {
            LOG.error("createTestDataSet: exception caught", e);
            throw e;
        }
    }

    /**
     * Update a person outside of JDO using a JDBC connection.
     *
     * @param firstName first part of primary key of object to be updated
     * @param lastName first part of primary key of object to be updated
     * @param newBirthdate new value of persons birthdate
     */
    private void updatePersonUsingJDBC(
            final String firstName, final String lastName, final Date newBirthdate) {
        
        LOG.info("updatePersonUsingJDBC: updating " + firstName + " " + lastName);

        try {
            _updatePersonStatement.setDate(1, newBirthdate);
            _updatePersonStatement.setString(2, firstName);
            _updatePersonStatement.setString(3, lastName);
            int rc = _updatePersonStatement.executeUpdate();
            if (rc <= 0) {
                LOG.error("updatePersonUsingJDBC: error updating person, return = " + rc);
                return;
            }
        } catch (Exception e) {
            LOG.error("updatePersonUsingJDBC: exception updating person", e);
        }
    }

    /**
     * Update a person outside of JDO using a JDBC connection.
     *
     * @param firstName first part of primary key of object to be updated
     * @param lastName first part of primary key of object to be updated
     * @param newStartDate new value of persons birthdate
     */
    private void updateEmplUsingJDBC(
            final String firstName, final String lastName, final Date newStartDate) {
        
        LOG.info("updateEmployeeUsingJDBC: updating " + firstName + " " + lastName);

        try {
            _updateEmployeeStatement.setDate(1, newStartDate);
            _updateEmployeeStatement.setString(2, firstName);
            _updateEmployeeStatement.setString(3, lastName);
            int rc = _updateEmployeeStatement.executeUpdate();
            if (rc <= 0) {
                LOG.error("updateEmplUsingJDBC: error updating employee, return = " + rc);
                return;
            }
        } catch (Exception e) {
            LOG.error("updateEmplUsingJDBC: exception updating employee", e);
        }
    }

    /**
     * Update an address outside of JDO using a JDBC connection.
     *
     * @param addressId primary key of object to be updated
     * @param newStreet new value of addresses street field
     */
    private void updateAddrUsingJDBC(final int addressId, final String newStreet) {
        LOG.info("updateAddressUsingJDBC: updating " + addressId);

        try {
            _updateAddressStatement.setString(1, newStreet);
            _updateAddressStatement.setInt(2, addressId);
            int rc = _updateAddressStatement.executeUpdate();
            if (rc <= 0) {
                LOG.error("updateAddrUsingJDBC: error updating address, return = " + rc);
                return;
            }
        } catch (Exception e) {
            LOG.error("updateAddrUsingJDBC: exception updating address", e);
        }
    }

    /**
     * Setup and execute a database expire cache request.
     *
     * @param byType when <code>true</code> this method will request that
     *      objects be expired from the cache by specifying a single type or
     *      class of objects to be expired; when <code>false</code> this method
     *      will request that objects be expired from the cache using individual
     *      object identities
     */
    private void expire(final boolean byType) {
        LOG.info("expiring cache...");

        try {
            CacheManager cacheManager = _db.getCacheManager();
            if (byType) {
                Class[] typeArray = new Class[5];
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
     * Read, or load, an object and validate that the field values
     * reflect values updated outside of JDO and not from previously
     * cached values.
     * @param firstName primary key of object to be read
     * @param lastName primary key of object to be read
     * @return True if read transaction is valid.
     */
    private boolean validReadTransaction(final String firstName, final String lastName) {
        LOG.info("validating read transaction for person "
                + firstName + " " + lastName + "...");
        Database db = null;
        boolean valid = true;
        try {
            db = _category.getDatabase();
            db.begin();
            
            Identity fullname = new Identity("First", "Person");
            LazyEmployee person;
            
            person = (LazyEmployee) db.load(LazyEmployee.class, fullname);
            if (person.getBirthday().compareTo(JDBC_UPDATED_DATE) != 0) {
                LOG.debug("validReadTransaction: birthdate for "
                        + person.getFirstName() + " " + person.getLastName()
                        + " does not match expected value, value: "
                        + person.getBirthday().toString()
                        + ", expected: " + JDBC_UPDATED_DATE.toString());
                valid = false;
            }
            if (person.getStartDate().compareTo(JDBC_UPDATED_DATE) != 0) {
                LOG.debug("validReadTransaction: start date for "
                        + person.getFirstName() + " " + person.getLastName()
                        + " does not match expected value, value: "
                        + person.getStartDate().toString()
                        + ", expected: " + JDBC_UPDATED_DATE.toString());
                valid = false;
            }
            Iterator itor = person.getAddress().iterator();
            while (itor.hasNext()) {
                LazyAddress address = (LazyAddress) itor.next();
                String expectedStreet = new String(
                        Integer.toString(address.getId()) + JDBC_UPDATED_STRING);
                if (address.getStreet().compareTo(expectedStreet) != 0) {
                    LOG.debug("validReadTransaction: street in address "
                            + address.getId()
                            + " does not match expected value, value: "
                            + address.getStreet()
                            + ", expected: " + expectedStreet);
                    valid = false;
                }
            }
            db.rollback();
            db.close();
        } catch (Exception e) {
            if (db != null) {
                try {
                    db.close();
                } catch (Exception se) {
                    LOG.warn("Problem closing Database instance.", se);
                }
            }
            LOG.error("validReadTransaction: exception while validating read for "
                    + firstName + " " + lastName, e);
            valid = false;
        }
        return valid;
    }

    /**
     * Modify fields of an object and validate that the transaction completes
     * successfully.
     * @param firstName First name.
     * @param lastName Last name.
     * @return True if write transaction is valid.
     */
    private boolean validWriteTransaction(final String firstName, final String lastName) {
        LOG.info("validating write transaction for group "
                + firstName + " " + lastName + "...");
        Database db = null;
        try {
            db = _category.getDatabase();
            db.begin();
            
            Identity fullname = new Identity("First", "Person");
            LazyEmployee person;
            
            person = (LazyEmployee) db.load(LazyEmployee.class, fullname);
            person.setBirthday(JDO_UPDATED_DATE);
            Iterator itor = person.getAddress().iterator();
            while (itor.hasNext()) {
                LazyAddress address = (LazyAddress) itor.next();
                String newStreet = new String(
                        Integer.toString(address.getId()) + JDO_UPDATED_STRING);
                address.setStreet(newStreet);
            }
            db.commit();
            db.close();
        } catch (Exception e) {
            if (db != null) {
                try {
                    db.close();
                } catch (Exception se) {
                    LOG.warn("Problem closing Database instance.", se);
                }
            }
            LOG.error("validWriteTransaction: exception while validating write for "
                    + firstName + " " + lastName, e);
            return false;
        }
        return true;
    }

    /**
     * Delete all objects in test data set.
     */
    private void deleteTestDataSet() {
        LOG.info("deleting test data set...");
        try {
            Statement stmt = _conn.createStatement();
            stmt.executeUpdate("DELETE FROM tc8x_pks_person");
            stmt.executeUpdate("DELETE FROM tc8x_pks_employee");
            stmt.executeUpdate("DELETE FROM tc8x_pks_payroll");
            stmt.executeUpdate("DELETE FROM tc8x_pks_address");
            stmt.executeUpdate("DELETE FROM tc8x_pks_contract");
            stmt.executeUpdate("DELETE FROM tc8x_pks_category");
            stmt.executeUpdate("DELETE FROM tc8x_pks_project");
        } catch (Exception e) {
            LOG.error("deleteTestDataSet: exception caught", e);
        }
    }
}