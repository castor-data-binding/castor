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
package ctf.jdo.tc1x;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

import harness.CastorTestCase;
import harness.TestHarness;

import jdo.JDOCategory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.persist.spi.Complex;

/**
 * Test for multiple columns primary Keys. These tests create data objects
 * model of different types that make uses of primary keys, modify the objects
 * and the relationship between objects and verify if changes made is persisted
 * properly.
 */
public final class TestMultiPrimKeys extends CastorTestCase {
    /**
     * The <a href="http://jakarta.apache.org/commons/logging/">Jakarta
     * Commons Logging</a> instance used for all logging.
     */
    private static final Log LOG = LogFactory.getLog(TestMultiPrimKeys.class);
    
    private JDOCategory    _category;

    private Database       _db;

    private Connection     _conn;

    /**
     * Constructor
     *
     * @param category The test suite of these test cases
     */
    public TestMultiPrimKeys(final TestHarness category) {
        super(category, "TC15", "Multiple columns primary keys tests");
        _category = (JDOCategory) category;
    }

    /**
     * Get a JDO database and direct JDBC connection. Clean up old values in
     * tables using JDBC conneciton and create different types of data object
     * that make use of multiple columns primary keys.
     */
    public void setUp() throws PersistenceException, SQLException {
        _db = _category.getDatabase();
        _conn = _category.getJDBCConnection();
        _conn.setAutoCommit(false);

        // delete everything directly
        LOG.debug("Delete everything");
        Statement stmt = _conn.createStatement();
        stmt.executeUpdate("DELETE FROM tc1x_pks_person");
        stmt.executeUpdate("DELETE FROM tc1x_pks_employee");
        stmt.executeUpdate("DELETE FROM tc1x_pks_payroll");
        stmt.executeUpdate("DELETE FROM tc1x_pks_address");
        stmt.executeUpdate("DELETE FROM tc1x_pks_contract");
        stmt.executeUpdate("DELETE FROM tc1x_pks_category");
        
        _conn.commit();
        _conn.close();
    }

    /**
     * Create a data object model, modify the model and verify if 
     * all changes persisted properly.
     */
    public void runTest() throws PersistenceException {
        createPerson();

        Complex fullname = new Complex("First", "Person");
        
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
        ArrayList addresslist = new ArrayList();
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
        
        ArrayList category = new ArrayList();
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
    
    private void loadAndModifyPerson1(final Complex fullname)
    throws PersistenceException {
        _db.begin();

        PrimaryKeysEmployee loadPerson = (PrimaryKeysEmployee) _db.load(
                PrimaryKeysEmployee.class, fullname);
        
        if (loadPerson.getBirthday().equals(getDate(1922, 2, 2))
                && loadPerson.getFirstName().equals("First")
                && loadPerson.getLastName().equals("Person")) {
            
            LOG.info("OK: Employee is valid");

            ArrayList address = loadPerson.getAddress();
            Iterator itor = address.iterator();
            PrimaryKeysAddress[] addresses = {null, null, null};
            PrimaryKeysAddress addr;
            while (itor.hasNext()) {
                addr = (PrimaryKeysAddress) itor.next();
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

            ArrayList catelist = cont.getCategory();
            itor = catelist.iterator();
            PrimaryKeysCategory cate;
            while (itor.hasNext()) {
                cate = (PrimaryKeysCategory) itor.next();
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
    
    private void loadAndModifyPerson2(final Complex fullname)
    throws PersistenceException {
        _db.begin();
        
        PrimaryKeysEmployee loadPerson = (PrimaryKeysEmployee) _db.load(
                PrimaryKeysEmployee.class, fullname);
        if (loadPerson.getBirthday().equals(getDate(1922, 2, 2))
                && loadPerson.getFirstName().equals("First")
                && loadPerson.getLastName().equals("Person")) {
            
            LOG.info("OK: Employee is valid");

            ArrayList address = loadPerson.getAddress();
            Iterator itor = address.iterator();
            PrimaryKeysAddress[] addresses = {null, null, null};
            PrimaryKeysAddress addr;
            while (itor.hasNext()) {
                addr = (PrimaryKeysAddress) itor.next();
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

            ArrayList catelist = cont.getCategory();
            itor = catelist.iterator();
            PrimaryKeysCategory cate;
            while (itor.hasNext()) {
                cate = (PrimaryKeysCategory) itor.next();
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
        cal.setTimeInMillis(0);
        cal.set(year, month, day);
        return cal.getTime();
    }
    
    /**
     * Release the JDO Database
     */
    public void tearDown() throws PersistenceException {
        if (_db.isActive()) { _db.rollback(); }
        _db.close();
    }
}
