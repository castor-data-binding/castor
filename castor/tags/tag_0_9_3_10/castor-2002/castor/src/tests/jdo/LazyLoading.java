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
 *
 */


package jdo; 


import java.io.IOException;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Collection;
import java.util.ArrayList;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.lang.Math;
import java.util.Vector;
import java.util.Random;
import java.util.Date;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.QueryResults;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.QueryException;
import org.exolab.castor.jdo.LockNotGrantedException;
import org.exolab.castor.jdo.TransactionAbortedException;
import org.exolab.castor.jdo.TransactionNotInProgressException;
import org.exolab.castor.jdo.ObjectModifiedException;
import org.exolab.castor.jdo.DuplicateIdentityException;
import org.exolab.castor.persist.spi.Complex;

import junit.framework.TestSuite;
import junit.framework.TestCase;
import junit.framework.Assert;
import harness.TestHarness;
import harness.CastorTestCase;


/**
 * Test for lazy loading of collection supported by Castor.
 * The object in a lazy collection is not loaded from the
 * DBMS until is it requested by the collection.
 */
public class LazyLoading extends CastorTestCase {

    private Database       _db;


    private Connection     _conn;


    private JDOCategory    _category;


    public LazyLoading( TestHarness category ) {

        super( category, "TC26", "Lazy Loading" );
        _category = (JDOCategory) category;
    }

    public void setUp() 
            throws PersistenceException, SQLException {
        _db = _category.getDatabase( verbose );
        _conn = _category.getJDBCConnection();
        _conn.setAutoCommit( false );

        // delete everything directly
        stream.println( "Delete everything" );
        Statement stmt = _conn.createStatement();
        stmt.executeUpdate("DELETE FROM test_pks_person");
        stmt.executeUpdate("DELETE FROM test_pks_employee");
        stmt.executeUpdate("DELETE FROM test_pks_payroll");
        stmt.executeUpdate("DELETE FROM test_pks_address");
        stmt.executeUpdate("DELETE FROM test_pks_contract");
        stmt.executeUpdate("DELETE FROM test_pks_category");
        stmt.executeUpdate("DELETE FROM test_pks_project");
        _conn.commit();

        createDataObjects();
    }

    public void createDataObjects() 
            throws PersistenceException, SQLException {

        _db.begin();
        // create person 1
        TestLazyEmployee person = new TestLazyEmployee();
        person.setFirstName( "First" );
        person.setLastName( "Person" );
        person.setBirthday( new Date(1922, 2, 2) );
        person.setStartDate( new Date(2000, 2, 2) );

        TestLazyAddress address1 = new TestLazyAddress();
        address1.setId(1);
        address1.setStreet("#1 Address Street");
        address1.setCity("First City");
        address1.setState("AB");
        address1.setZip("10000");
        address1.setPerson( person );

        TestLazyAddress address2 = new TestLazyAddress();
        address2.setId(2);
        address2.setStreet("2nd Ave");
        address2.setCity("Second City");
        address2.setState("BC");
        address2.setZip("22222");
        address2.setPerson( person );

        TestLazyAddress address3 = new TestLazyAddress();
        address3.setId(3);
        address3.setStreet("3rd Court");
        address3.setCity("Third Ave");
        address3.setState("AB");
        address3.setZip("30003");
        address3.setPerson( person );

        ArrayList addresslist = new ArrayList();
        addresslist.add( address1 );
        addresslist.add( address2 );
        addresslist.add( address3 );

        person.setAddress( addresslist );

        TestLazyPayRoll pr1 = new TestLazyPayRoll();
        pr1.setId( 1 );
        pr1.setHoliday( 15 );
        pr1.setHourlyRate( 25 );
        pr1.setEmployee( person );
        person.setPayRoll( pr1 );

        TestLazyContractCategory cc = new TestLazyContractCategory();
        cc.setId( 101 );
        cc.setName("Full-time slave");
        _db.create( cc );

        TestLazyContractCategory cc2 = new TestLazyContractCategory();
        cc2.setId( 102 );
        cc2.setName("Full-time employee");
        _db.create( cc2 );
        ArrayList category = new ArrayList();
        category.add( cc );
        category.add( cc2 );

        TestLazyContract con = new TestLazyContract();
        con.setPolicyNo(1001);
        con.setComment("80 hours a week, no pay hoilday, no sick leave, arrive office at 7:30am everyday");
        con.setContractNo(78);
        con.setEmployee( person );
        con.setCategory( category );
        person.setContract( con );
        _db.create( person );
        _db.commit();
    }

    public void runTest() 
            throws PersistenceException, SQLException {

        // the following test are designed to be run in sequence. 
        testGeneral();

        testCollection();

        testComplex();
    }

    public void testGeneral() 
            throws PersistenceException, SQLException {

        _db.begin();
        Complex fullname = new Complex( "First", "Person" );

        TestLazyEmployee loadPerson = (TestLazyEmployee) _db.load( TestLazyEmployee.class, fullname );
        
        if ( loadPerson.getBirthday().equals(new Date(1922, 2, 2)) &&
                loadPerson.getFirstName().equals("First") && loadPerson.getLastName().equals("Person") ) {
            stream.println("OK: Employee is valid");

            Collection address = loadPerson.getAddress();
            Iterator itor = address.iterator();
            TestLazyAddress[] addresses = { null, null, null };
            TestLazyAddress addr;
            while ( itor.hasNext() ) {
                addr = (TestLazyAddress)itor.next();
                if ( addr.getId() < 1 || addr.getId() > 3 ) {
                    _db.rollback();
                    stream.println("Error: Address id is incorrect");
                    fail("address id is incorrect");
                }
                addresses[addr.getId()-1] = addr;
            }

            if ( addresses[0] == null || !addresses[0].getStreet().equals("#1 Address Street") 
                    || !addresses[0].getCity().equals("First City") || !addresses[0].getState().equals("AB") 
                    || !addresses[0].getZip().equals("10000") || addresses[0].getPerson() != loadPerson ) {
                stream.println("Error: Address 1 is incorrect: "+addresses[0]);
                _db.rollback();
                fail("address 1 is incorrect");
            } 
            stream.println("OK: Address 1 are valid");

            if ( addresses[1] == null || !addresses[1].getStreet().equals("2nd Ave") 
                    || !addresses[1].getCity().equals("Second City") || !addresses[1].getState().equals("BC") 
                    || !addresses[1].getZip().equals("22222") || addresses[1].getPerson() != loadPerson ) {
                stream.println("Error: Address 2 is incorrect");
                _db.rollback();
                fail("address 2 is incorrect");
            }
            stream.println("OK: Address 2 are valid");

            TestLazyPayRoll payroll = loadPerson.getPayRoll();
            if ( payroll == null || payroll.getId() != 1 || payroll.getHoliday() != 15 
                    || payroll.getEmployee() != loadPerson || payroll.getHourlyRate() != 25 ) {
                stream.println("Error: PayRoll loaded incorrect");
                _db.rollback();
                fail("payroll is incorrect");
            }
            stream.println("OK: PayRoll is valid");

            TestLazyContract cont = loadPerson.getContract();
            if ( cont == null || cont.getPolicyNo() != 1001 || cont.getEmployee() != loadPerson 
                    || cont.getContractNo() != 78 ) {
                stream.println("Error: Contract is not what expected!");
                stream.println("employe==null? "+cont.getEmployee()+"/"+cont.getEmployee().getFirstName()+"/"+cont.getEmployee().getLastName());
                stream.println("loadPerson? "+loadPerson+"/"+loadPerson.getFirstName()+"/"+loadPerson.getLastName());                   
                _db.rollback();
                fail("contract is incorrect");
            }
            stream.println("OK: Contract is valid");

            Collection catelist = cont.getCategory();
            itor = catelist.iterator();
            TestLazyContractCategory cate;
            while ( itor.hasNext() ) {
                cate = (TestLazyContractCategory) itor.next();
                if ( cate.getId() == 101 && cate.getName().equals("Full-time slave") ) {
                } else if ( cate.getId() == 102 && cate.getName().equals("Full-time employee") ) {
                } else {
                    stream.println("Error: Category is incorrect");
                    _db.rollback();
                    fail("category is incorrect");
                }                           
            }
            stream.println("OK: Categories are valid");

            // now modified the object and store it
            address.remove( addresses[0] );
            addresses[1].setStreet("New Second Street");
            
        } else {
            _db.rollback();
            stream.println("Error: FirstName, LastName or Birthday is incorrect!");
            fail("FirstName, LastName or Birthday is incorrect!");
        }
        _db.commit();

        // test and see if changes made succeed
        _db.begin();
        loadPerson = (TestLazyEmployee) _db.load( TestLazyEmployee.class, fullname );
        
        if ( loadPerson.getBirthday().equals(new Date(1922, 2, 2)) &&
                loadPerson.getFirstName().equals("First") && loadPerson.getLastName().equals("Person") ) {
            stream.println("OK: Employee is valid");

            Collection address = loadPerson.getAddress();
            Iterator itor = address.iterator();
            TestLazyAddress[] addresses = { null, null, null };
            TestLazyAddress addr;
            while ( itor.hasNext() ) {
                addr = (TestLazyAddress)itor.next();
                if ( addr.getId() < 1 || addr.getId() > 3 ) {
                    _db.rollback();
                    stream.println("Error: Address id is incorrect");
                    fail("address id is incorrect");
                }
                addresses[addr.getId()-1] = addr;
            }

            if ( addresses[0] != null ) {
                stream.println("Error: Address 1 is not deleted: "+addresses[0]);
                fail("address 1 is not deleted");
            }
            stream.println("OK: Address 1 is deleted");

            if ( addresses[1] == null || !addresses[1].getStreet().equals("New Second Street") 
                    || !addresses[1].getCity().equals("Second City") || !addresses[1].getState().equals("BC") 
                    || !addresses[1].getZip().equals("22222") || addresses[1].getPerson() != loadPerson ) {
                stream.println("Error: Address 2 is incorrect");
                _db.rollback();
                fail("address 2 is incorrect");
            }
            stream.println("OK: Address 2 are valid: "+addresses[1]);

            TestLazyPayRoll payroll = loadPerson.getPayRoll();
            if ( payroll == null || payroll.getId() != 1 || payroll.getHoliday() != 15 
                    || payroll.getEmployee() != loadPerson || payroll.getHourlyRate() != 25 ) {
                stream.println("Error: PayRoll loaded incorrect");
                _db.rollback();
                fail("payroll is incorrect");
            }
            stream.println("OK: PayRoll is valid");

            TestLazyContract cont = loadPerson.getContract();
            if ( cont == null || cont.getPolicyNo() != 1001 || cont.getEmployee() != loadPerson 
                    || cont.getContractNo() != 78 ) {
                stream.println("Error: Contract is not what expected!");
                stream.println("employe==null? "+cont.getEmployee()+"/"+cont.getEmployee().getFirstName()+"/"+cont.getEmployee().getLastName());
                stream.println("loadPerson? "+loadPerson+"/"+loadPerson.getFirstName()+"/"+loadPerson.getLastName());                   
                _db.rollback();
                fail("contract is incorrect");
            }
            stream.println("OK: Contract is valid");

            Collection catelist = cont.getCategory();
            itor = catelist.iterator();
            TestLazyContractCategory cate;
            while ( itor.hasNext() ) {
                cate = (TestLazyContractCategory) itor.next();
                if ( cate.getId() == 101 && cate.getName().equals("Full-time slave") ) {
                } else if ( cate.getId() == 102 && cate.getName().equals("Full-time employee") ) {
                } else {
                    stream.println("Error: Category is incorrect");
                    _db.rollback();
                    fail("category is incorrect");
                }                           
            }
            stream.println("OK: Categories are valid");

        } else {
            _db.rollback();
            stream.println("Error: FirstName, LastName or Birthday is incorrect!");
            fail("FirstName, LastName or Birthday is incorrect!");
        }
        _db.commit();
    }

    public void testCollection() 
            throws PersistenceException, SQLException {

        Complex fullname = new Complex( "First", "Person" );

        // test java.util.Collection.clear() for lazy loading (bug 801)
        _db.begin();
        TestLazyPerson loadPerson = (TestLazyEmployee) _db.load( TestLazyEmployee.class, fullname );
        
        Collection addresses = loadPerson.getAddress();
        addresses.clear();
        _db.commit();

        _db.begin();
        loadPerson = (TestLazyEmployee) _db.load( TestLazyEmployee.class, fullname );
        addresses = loadPerson.getAddress();

        // check if clear() work
        if ( !addresses.isEmpty() ) {
            stream.println("Error: Collection.clear() is not working!");
            fail("Error: Collection.clear() is not working!");
        }

        // modify the collection to test java.util.Collection.addAll() 
        // for lazy loading (bug 801)
        Collection c = new ArrayList();

        TestLazyAddress address = new TestLazyAddress();
        address.setId(101);
        address.setStreet("Mattrew Street");
        address.setCity("Rome City");
        address.setState("RM");
        address.setZip("10000");
        address.setPerson( loadPerson );
        c.add( address );

        address = new TestLazyAddress();
        address.setId(102);
        address.setStreet("Luke Street");
        address.setCity("Rome City");
        address.setState("RM");
        address.setZip("10000");
        address.setPerson( loadPerson );
        c.add( address );

        address = new TestLazyAddress();
        address.setId(103);
        address.setStreet("John Street");
        address.setCity("Rome City");
        address.setState("RM");
        address.setZip("10000");
        address.setPerson( loadPerson );
        c.add( address );

        addresses.addAll( c );
        _db.commit();

        // check if add all work
        _db.begin();
        loadPerson = (TestLazyEmployee) _db.load( TestLazyEmployee.class, fullname );
        addresses = loadPerson.getAddress();
        Iterator itor = addresses.iterator();

        boolean hasAddr1, hasAddr2, hasAddr3;
        hasAddr1 = hasAddr2 = hasAddr3 = false;
        while ( itor.hasNext() ) {
            address = (TestLazyAddress) itor.next();
            if ( address.getId() == 101 )
                hasAddr1 = true;
            else if ( address.getId() == 102 )
                hasAddr2 = true;
            else if ( address.getId() == 103 )
                hasAddr3 = true;
            else {
                stream.println("Error: Address with unexpected id is found! "+address);
                fail("Erorr: Address with unexpected id is found! "+address);
            }
        }
        if ( !hasAddr1 || !hasAddr2 || !hasAddr3 ) {
            stream.println("Error: Collection.addAll( Collection ) fail");
            fail("Error: Collection.addAll( Collection ) fail");
        }
        _db.commit();
    }

    public void testComplex()
            throws PersistenceException, SQLException {

        Complex fullname = new Complex( "First", "Person" );

        // set up the data object
        _db.begin();
        TestLazyEmployee loadPerson = (TestLazyEmployee) _db.load( TestLazyEmployee.class, fullname );
        Collection projects = loadPerson.getProjects();

        TestLazyProject project = new TestLazyProject();
        project.setId(1001);
        project.setName("Project One");
        project.setOwner(loadPerson);
        projects.add( project );
        _db.create( project );

        project = new TestLazyProject();
        project.setId(1002);
        project.setName("Project Two");
        project.setOwner(loadPerson);
        projects.add( project );
        _db.create( project );

        project = new TestLazyProject();
        project.setId(1003);
        project.setName("Project Three");
        project.setOwner(loadPerson);
        projects.add( project );
        _db.create( project );

        _db.commit();

        // reload and test bug 823
        _db.begin();
        project = (TestLazyProject) _db.load( TestLazyProject.class, new Integer(1002) );
        _db.remove( project );

        loadPerson = (TestLazyEmployee) _db.load( TestLazyEmployee.class, fullname );
        projects = loadPerson.getProjects();

        Iterator itor = projects.iterator();
        while ( itor.hasNext() ) {
            project = (TestLazyProject) itor.next();
            if ( project.getId() == 1002 ) {
                itor.remove();
                break;
            }
        }

        itor = projects.iterator();
        while ( itor.hasNext() ) {
            project = (TestLazyProject) itor.next();
            if ( project.getId() == 1002 ) {
                itor.remove();
                break;
            }
        }
        _db.commit();

        // reload and make sure the cache is consistent
        _db.begin();
        loadPerson = (TestLazyEmployee) _db.load( TestLazyEmployee.class, fullname );
        projects = loadPerson.getProjects();
        itor = projects.iterator();
        int id1 = 0;
        if ( itor.hasNext() ) {
            project = (TestLazyProject) itor.next();
            id1 = project.getId();
            if ( project.getId()!=1001 && project.getId()!=1003 ) {
                stream.println("Error: found project1 with unexpected id "+project.getId());
                fail("Error: found project1 with unexpected id "+project.getId());
            }
        } else {
            stream.println("Error: expected project is not found");
            fail("Error: expected project is not found");
        }

        if ( itor.hasNext() ) {
            project = (TestLazyProject) itor.next();
            if ( project.getId() == id1 || (project.getId() != 1001 && project.getId() != 1003) ) {
                stream.println("Error: found project2 with unexpected id "+project.getId());
                fail("Error: found project2 with unexpected id "+project.getId());
            }
        } else {
            stream.println("Error: expected project is not found");
            fail("Error: expected project is not found");
        }

        if ( itor.hasNext() ) {
            project = (TestLazyProject) itor.next();
            stream.println("Error: unexpected project is found: "+project.getId());
            fail("Error: unexpected project is found: "+project.getId());
        }
        _db.commit();
    }

    public void tearDown()
            throws PersistenceException, SQLException  {
        if ( _db.isActive() ) _db.rollback();
        _db.close();
        _conn.close();
    }

}

