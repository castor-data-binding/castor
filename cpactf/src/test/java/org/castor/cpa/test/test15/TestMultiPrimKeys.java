/*
 * Copyright 2009 Udai Gupta, Ralf Joachim
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
package org.castor.cpa.test.test15;

import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.castor.cpa.test.framework.CPATestCase;
import org.castor.cpa.test.framework.xml.types.DatabaseEngineType;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.persist.spi.Identity;

/**
 * Test for multiple columns primary Keys. These tests create data objects
 * model of different types that make uses of primary keys, modify the objects
 * and the relationship between objects and verify if changes made is persisted
 * properly.
 */
public final class TestMultiPrimKeys extends CPATestCase {
    /**
     * The <a href="http://jakarta.apache.org/commons/logging/">Jakarta
     * Commons Logging</a> instance used for all logging.
     */
    private static final Log LOG = LogFactory.getLog(TestMultiPrimKeys.class);
    
    private static final String DBNAME = "test15";
    private static final String MAPPING = "/org/castor/cpa/test/test15/mapping.xml";
    private Database _db;

    /**
     * Constructor
     *
     * @param category The test suite of these test cases
     */
    public TestMultiPrimKeys(final String name) {
        super(name);
    }

    // Test are only included/excluded for engines that have been tested with this test suite.

    public boolean include(final DatabaseEngineType engine) {
        return (engine == DatabaseEngineType.MYSQL)
            || (engine == DatabaseEngineType.DERBY);
    }
    
    /**
     * Get a JDO database and direct JDBC connection. Clean up old values in
     * tables using JDBC conneciton and create different types of data object
     * that make use of multiple columns primary keys.
     */
    public void setUp() throws Exception {
        _db = getJDOManager(DBNAME, MAPPING).getDatabase();
        _db.begin();
        Connection conn = _db.getJdbcConnection();
        // delete everything directly
        LOG.debug("Delete everything");
        Statement stmt = conn.createStatement();
        stmt.executeUpdate("DELETE FROM test15_person");
        stmt.executeUpdate("DELETE FROM test15_employee");
        stmt.executeUpdate("DELETE FROM test15_payroll");
        stmt.executeUpdate("DELETE FROM test15_address");
        stmt.executeUpdate("DELETE FROM test15_contract");
        stmt.executeUpdate("DELETE FROM test15_category");
        _db.commit();
    }

    /**
     * Release the JDO Database
     */
    public void tearDown() throws PersistenceException {
        if (_db.isActive()) { _db.rollback(); }
        _db.close();
    }
    
    /**
     * Create a data object model, modify the model and verify if 
     * all changes persisted properly.
     */
    public void testRun() throws PersistenceException {
        createPerson();

        Identity fullname = new Identity("First", "Person");
        
        // now test if the persisted objects model is created as expected
        loadAndModifyPerson1(fullname);
        loadAndModifyPerson2(fullname);
    }

    private void createPerson() throws PersistenceException {
        _db.begin();
        
        // create first person an employee that extends person
        // and uses multiple columns primary keys as its identity
        PrimaryKeysEmployee person = new PrimaryKeysEmployee("First", "Person",
                getDate(1922, 2, 2), getDate(2000, 2, 2));

        // create first address
        PrimaryKeysAddress address1 = new PrimaryKeysAddress(1, "#1 Address Street",
                "First City", "AB", "10000");
        address1.setPerson(person);

        // create second address
        PrimaryKeysAddress address2 = new PrimaryKeysAddress(2, "2nd Ave",
                "Second City", "BC", "22222");
        address2.setPerson(person);

        // create third address
        PrimaryKeysAddress address3 = new PrimaryKeysAddress(3, "3rd Court",
                "Third Ave", "AB", "30003");
        address3.setPerson(person);

        // the employee and address relationship is a 1:n relationship
        ArrayList<PrimaryKeysAddress> addresslist = new ArrayList<PrimaryKeysAddress>();
        addresslist.add(address1);
        addresslist.add(address2);
        addresslist.add(address3);

        // add addresses into the employee's
        person.setAddress(addresslist);

        // payroll represent an 1:1 dependent relationship with employee
        PrimaryKeysPayRoll pr = new PrimaryKeysPayRoll(1, 15, 25);
        pr.setEmployee(person);
        person.setPayRoll(pr);

        // contract-category is n:m relationship with contract
        PrimaryKeysCategory cc1 = new PrimaryKeysCategory(
                101, "Full-time junior");
        _db.create(cc1);

        PrimaryKeysCategory cc2 = new PrimaryKeysCategory(
                102, "Full-time employee");
        _db.create(cc2);
        
        ArrayList<PrimaryKeysCategory> category = new ArrayList<PrimaryKeysCategory>();
        category.add(cc1);
        category.add(cc2);

        // contract is a 1:n dependent relationship with employee 
        // (with both side uses multiple primary keys)
        PrimaryKeysContract con = new PrimaryKeysContract(1001, 78,
                  "80 hours a week, no pay holiday, no sick leave, "
                + "arrive office at 7:30am everyday");
        con.setEmployee(person);
        con.setCategory(category);
        person.setContract(con);
        
        _db.create(person);
        _db.commit();
        
        LOG.info("OK: The complex models created successfully");
    }
    
    private void loadAndModifyPerson1(final Identity fullname)
    throws PersistenceException {
        _db.begin();

        PrimaryKeysEmployee loadPerson = (PrimaryKeysEmployee) _db.load(
                PrimaryKeysEmployee.class, fullname);
        
        if (loadPerson.getBirthday().equals(getDate(1922, 2, 2))
                && loadPerson.getFirstName().equals("First")
                && loadPerson.getLastName().equals("Person")) {
            
            LOG.info("OK: Employee is valid");

            ArrayList<PrimaryKeysAddress> address = loadPerson.getAddress();
            Iterator<PrimaryKeysAddress> itor1 = address.iterator();
            PrimaryKeysAddress[] addresses = {null, null, null};
            PrimaryKeysAddress addr;
            while (itor1.hasNext()) {
                addr = itor1.next();
                if ((addr.getId() < 1) || (addr.getId() > 3)) {
                    _db.rollback();
                    LOG.error("Address id is incorrect");
                    fail("Address id is incorrect");
                }
                addresses[addr.getId() - 1] = addr;
            }

            if ((addresses[0] == null)
                    || !addresses[0].getStreet().equals("#1 Address Street") 
                    || !addresses[0].getCity().equals("First City")
                    || !addresses[0].getState().equals("AB") 
                    || !addresses[0].getZip().equals("10000")
                    || (addresses[0].getPerson() != loadPerson)) {
                
                LOG.error("Address 1 is incorrect");
                _db.rollback();
                fail("Address 1 is incorrect");
            }
            LOG.debug("OK: Address 1 is valid");

            if ((addresses[1] == null)
                    || !addresses[1].getStreet().equals("2nd Ave") 
                    || !addresses[1].getCity().equals("Second City")
                    || !addresses[1].getState().equals("BC") 
                    || !addresses[1].getZip().equals("22222")
                    || (addresses[1].getPerson() != loadPerson)) {
                
                LOG.error("Address 2 is incorrect");
                _db.rollback();
                fail("Address 2 is incorrect");
            }
            LOG.info("OK: Address 2 is valid");

            PrimaryKeysPayRoll payroll = loadPerson.getPayRoll();
            if ((payroll == null)
                    || (payroll.getId() != 1)
                    || (payroll.getHoliday() != 15)
                    || (payroll.getEmployee() != loadPerson)
                    || (payroll.getHourlyRate() != 25)) {
                
                LOG.error("PayRoll loaded incorrect");
                _db.rollback();
                fail("PayRoll loaded is incorrect");
            }
            LOG.info("OK: PayRoll is valid");

            PrimaryKeysContract cont = loadPerson.getContract();
            if ((cont == null)
                    || (cont.getPolicyNo() != 1001)
                    || (cont.getEmployee() != loadPerson) 
                    || (cont.getContractNo() != 78)) {
                
                LOG.error("Contract are not what's expected!");
                _db.rollback();
                fail("Contract are not expected");
            }
            LOG.info("OK: Contract is valid");

            ArrayList<PrimaryKeysCategory> catelist = cont.getCategory();
            Iterator<PrimaryKeysCategory> itor2 = catelist.iterator();
            PrimaryKeysCategory cate;
            while (itor2.hasNext()) {
                cate = itor2.next();
                if (((cate.getId() != 101)
                        || !cate.getName().equals("Full-time junior"))
                        && ((cate.getId() != 102)
                        || !cate.getName().equals("Full-time employee"))) {
                            
                    LOG.error("Category is incorrect");
                    _db.rollback();
                    fail("category is incorrect");
                }                           
            }
            LOG.info("OK: Categories are valid");

            // now modify it!
            address.remove(addresses[0]);
            addresses[1].setStreet("New Second Street");
        } else {
            _db.rollback();
            LOG.error("FirstName, LastName or Birth is incorrect!");
            fail("The firstName, LastName or Birth is incorrect!");
        }               
        _db.commit();
    }
    
    private void loadAndModifyPerson2(final Identity fullname)
    throws PersistenceException {
        _db.begin();
        
        PrimaryKeysEmployee loadPerson = (PrimaryKeysEmployee) _db.load(
                PrimaryKeysEmployee.class, fullname);
        if (loadPerson.getBirthday().equals(getDate(1922, 2, 2))
                && loadPerson.getFirstName().equals("First")
                && loadPerson.getLastName().equals("Person")) {
            
            LOG.info("OK: Employee is valid");

            ArrayList<PrimaryKeysAddress> address = loadPerson.getAddress();
            Iterator<PrimaryKeysAddress> itor1 = address.iterator();
            PrimaryKeysAddress[] addresses = {null, null, null};
            PrimaryKeysAddress addr;
            while (itor1.hasNext()) {
                addr = itor1.next();
                if ((addr.getId() < 1) || (addr.getId() > 3)) {
                    _db.rollback();
                    LOG.error("Address id is incorrect");
                    fail("Address id is incorrect");
                }
                addresses[addr.getId() - 1] = addr;
            }

            if (addresses[0] != null) {
                LOG.error("Address 1 is not deleted");
                _db.rollback();
                fail("Address 1 is not deleted");
            }
            LOG.info("OK: Address 1 is deleted");

            if ((addresses[1] == null)
                    || !addresses[1].getStreet().equals("New Second Street") 
                    || !addresses[1].getCity().equals("Second City")
                    || !addresses[1].getState().equals("BC") 
                    || !addresses[1].getZip().equals("22222")
                    || (addresses[1].getPerson() != loadPerson)) {
                
                LOG.error("Address 2 is incorrect");
                _db.rollback();
                fail("Address 2 is incorrect");
            }
            LOG.info("OK: Address 2 are valid");

            PrimaryKeysPayRoll payroll = loadPerson.getPayRoll();
            if ((payroll == null)
                    || (payroll.getId() != 1)
                    || (payroll.getHoliday() != 15) 
                    || (payroll.getEmployee() != loadPerson)
                    || (payroll.getHourlyRate() != 25)) {
                
                LOG.error("PayRoll loaded incorrect");
                _db.rollback();
                fail("Payroll is incorrect");
            }
            LOG.info("OK: PayRoll is valid");

            PrimaryKeysContract cont = loadPerson.getContract();
            if ((cont == null)
                    || (cont.getPolicyNo() != 1001)
                    || (cont.getEmployee() != loadPerson) 
                    || (cont.getContractNo() != 78)) {
                
                LOG.error("Contract are not what expected!");
                LOG.error("employe==null? " + cont.getEmployee()
                        + "/" + cont.getEmployee().getFirstName()
                        + "/" + cont.getEmployee().getLastName());
                LOG.error("loadPerson? " + loadPerson
                        + "/" + loadPerson.getFirstName()
                        + "/" + loadPerson.getLastName());                  
                _db.rollback();
                fail("Contract is incorrect");
            }
            LOG.info("OK: Contract is valid");

            ArrayList<PrimaryKeysCategory> catelist = cont.getCategory();
            Iterator<PrimaryKeysCategory> itor2 = catelist.iterator();
            PrimaryKeysCategory cate;
            while (itor2.hasNext()) {
                cate = itor2.next();
                if (((cate.getId() != 101)
                        || !cate.getName().equals("Full-time junior"))
                        && ((cate.getId() != 102)
                        || !cate.getName().equals("Full-time employee"))) {

                    LOG.error("Category is incorrect");
                    _db.rollback();
                    fail("Category is incorrect");
                }                           
            }
            LOG.info("OK: Categories are valid");

            // now modify it!
            address.remove(addresses[0]);
            addresses[1].setStreet("New Second Street");
        } else {
            _db.rollback();
            LOG.error("FirstName, LastName or Birth is incorrect!");
            fail("FirstName, LastName or Birth is incorrect!");
        }               
        _db.commit();
    }
    
    private Date getDate(final int year, final int month, final int day) {
        Calendar cal = Calendar.getInstance();
        cal.set(year, month, day, 0, 0, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }
}
