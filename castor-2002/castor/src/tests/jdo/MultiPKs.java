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
 * $Id$
 */


package jdo;


import java.io.IOException;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Collection;
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
import org.exolab.jtf.CWVerboseStream;
import org.exolab.jtf.CWTestCase;
import org.exolab.jtf.CWTestCategory;
import org.exolab.exceptions.CWClassConstructorException;
import java.util.ArrayList;


/**
 * Concurrent access test. Tests a JDO modification and concurrent
 * JDBC modification to determine if JDO can detect the modification
 * with dirty checking.
 */
public class MultiPKs extends CWTestCase {

    private Database       _db;


    private Connection     _conn;


    private JDOCategory    _category;


    public MultiPKs( CWTestCategory category )
        throws CWClassConstructorException
    {
        super( "TC22", "Multiple Columns Primary Keys" );
        _category = (JDOCategory) category;
    }

    public void preExecute()
    {
        super.preExecute();
    }

    public void postExecute()
    {
        super.postExecute();
    }


    public boolean run( CWVerboseStream stream ) {
        boolean result = true;
        try {
            _db = _category.getDatabase( stream.verbose() );
            _conn = _category.getJDBCConnection();
            _conn.setAutoCommit( false );

			// delete everything directly
            stream.writeVerbose( "Delete everything" );
			Statement stmt = _conn.createStatement();
			stmt.executeUpdate("DELETE FROM test_pks_person");
			stmt.executeUpdate("DELETE FROM test_pks_employee");
			stmt.executeUpdate("DELETE FROM test_pks_payroll");
			stmt.executeUpdate("DELETE FROM test_pks_address");
			stmt.executeUpdate("DELETE FROM test_pks_contract");
			stmt.executeUpdate("DELETE FROM test_pks_category");
			_conn.commit();

            _db.begin();
			// create person 1
			TestPKsEmployee person = new TestPKsEmployee();
			person.setFirstName( "First" );
			person.setLastName( "Person" );
			person.setBirthday( new Date(1922, 2, 2) );
            person.setStartDate( new Date(2000, 2, 2) );

			TestPKsAddress address1 = new TestPKsAddress();
			address1.setId(1);
			address1.setStreet("#1 Address Street");
			address1.setCity("First City");
			address1.setState("AB");
            address1.setZip("10000");
			address1.setPerson( person );

			TestPKsAddress address2 = new TestPKsAddress();
			address2.setId(2);
			address2.setStreet("2nd Ave");
			address2.setCity("Second City");
			address2.setState("BC");
            address2.setZip("22222");
			address2.setPerson( person );

			TestPKsAddress address3 = new TestPKsAddress();
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

			TestPKsPayRoll pr1 = new TestPKsPayRoll();
			pr1.setId( 1 );
			pr1.setHoliday( 15 );
			pr1.setHourlyRate( 25 );
			pr1.setEmployee( person );
			person.setPayRoll( pr1 );

			TestPKsContractCategory cc = new TestPKsContractCategory();
			cc.setId( 101 );
			cc.setName("Full-time slave");
			_db.create( cc );

			TestPKsContractCategory cc2 = new TestPKsContractCategory();
			cc2.setId( 102 );
			cc2.setName("Full-time employee");
			_db.create( cc2 );
			ArrayList category = new ArrayList();
			category.add( cc );
			category.add( cc2 );

			TestPKsContract con = new TestPKsContract();
			con.setPolicyNo(1001);
			con.setComment("80 hours a week, no pay hoilday, no sick leave, arrive office at 7:30am everyday");
			con.setContractNo(78);
			con.setEmployee( person );
			con.setCategory( category );
			person.setContract( con );
			_db.create( person );
			_db.commit();			




			_db.begin();
			Complex fullname = new Complex( "First", "Person" );

			TestPKsEmployee loadPerson = (TestPKsEmployee) _db.load( TestPKsEmployee.class, fullname );
			
			if ( loadPerson.getBirthday().equals(new Date(1922, 2, 2)) &&
					loadPerson.getFirstName().equals("First") && loadPerson.getLastName().equals("Person") ) {
				stream.writeVerbose("OK: Employee is valid");

				ArrayList address = loadPerson.getAddress();
				Iterator itor = address.iterator();
				TestPKsAddress[] addresses = { null, null, null };
				TestPKsAddress addr;
				while ( itor.hasNext() ) {
				    addr = (TestPKsAddress)itor.next();
					if ( addr.getId() < 1 || addr.getId() > 3 ) {
						_db.rollback();
						stream.writeVerbose("Error: Address id is wrong");
						return false;
					}
					addresses[addr.getId()-1] = addr;
				}

				if ( addresses[0] == null || !addresses[0].getStreet().equals("#1 Address Street") 
						|| !addresses[0].getCity().equals("First City") || !addresses[0].getState().equals("AB") 
						|| !addresses[0].getZip().equals("10000") || addresses[0].getPerson() != loadPerson ) {
					stream.writeVerbose("Error: Address 1 is wrong");
					_db.rollback();
					return false;
				}
				stream.writeVerbose("OK: Address 1 are valid");

				if ( addresses[1] == null || !addresses[1].getStreet().equals("2nd Ave") 
						|| !addresses[1].getCity().equals("Second City") || !addresses[1].getState().equals("BC") 
						|| !addresses[1].getZip().equals("22222") || addresses[1].getPerson() != loadPerson ) {
					stream.writeVerbose("Error: Address 2 is wrong");
					_db.rollback();
					return false;
				}
				stream.writeVerbose("OK: Address 2 are valid");

				TestPKsPayRoll payroll = loadPerson.getPayRoll();
				if ( payroll == null || payroll.getId() != 1 || payroll.getHoliday() != 15 
						|| payroll.getEmployee() != loadPerson || payroll.getHourlyRate() != 25 ) {
					stream.writeVerbose("Error: PayRoll loaded wrong");
					_db.rollback();
					return false;
				}
				stream.writeVerbose("OK: PayRoll is valid");

				TestPKsContract cont = loadPerson.getContract();
				if ( cont == null || cont.getPolicyNo() != 1001 || cont.getEmployee() != loadPerson 
						|| cont.getContractNo() != 78 ) {
					stream.writeVerbose("Error: Contract are not what's expected!");
			    	_db.rollback();
					return false;
				}
				stream.writeVerbose("OK: Contract is valid");

				ArrayList catelist = cont.getCategory();
				itor = catelist.iterator();
				TestPKsContractCategory cate;
				while ( itor.hasNext() ) {
				    cate = (TestPKsContractCategory) itor.next();
					if ( cate.getId() == 101 && cate.getName().equals("Full-time slave") ) {
					} else if ( cate.getId() == 102 && cate.getName().equals("Full-time employee") ) {
					} else {
						stream.writeVerbose("Error: Category is wrong");
						_db.rollback();
						return false;
					}							
				}
				stream.writeVerbose("OK: Categories are valid");

				// now modify it!
				address.remove( addresses[0] );
				addresses[1].setStreet("New Second Street");
			} else {
				_db.rollback();
				stream.writeVerbose("Error: FirstName, LastName or Birthday is wrong!");
				return false;
			}				
			_db.commit();


			_db.begin();
			loadPerson = (TestPKsEmployee) _db.load( TestPKsEmployee.class, fullname );
			if ( loadPerson.getBirthday().equals(new Date(1922, 2, 2)) &&
					loadPerson.getFirstName().equals("First") && loadPerson.getLastName().equals("Person") ) {
				stream.writeVerbose("OK: Employee is valid");

				ArrayList address = loadPerson.getAddress();
				Iterator itor = address.iterator();
				TestPKsAddress[] addresses = { null, null, null };
				TestPKsAddress addr;
				while ( itor.hasNext() ) {
				    addr = (TestPKsAddress)itor.next();
					if ( addr.getId() < 1 || addr.getId() > 3 ) {
						_db.rollback();
						stream.writeVerbose("Error: Address id is wrong");
						return false;
					}
					addresses[addr.getId()-1] = addr;
				}

				if ( addresses[0] != null ) {
					stream.writeVerbose("Error: Address 1 is not deleted");
					_db.rollback();
					return false;
				}
				stream.writeVerbose("OK: Address 1 is deleted");

				if ( addresses[1] == null || !addresses[1].getStreet().equals("New Second Street") 
						|| !addresses[1].getCity().equals("Second City") || !addresses[1].getState().equals("BC") 
						|| !addresses[1].getZip().equals("22222") || addresses[1].getPerson() != loadPerson ) {
					stream.writeVerbose("Error: Address 2 is wrong");
					_db.rollback();
					return false;
				}
				stream.writeVerbose("OK: Address 2 are valid");

				TestPKsPayRoll payroll = loadPerson.getPayRoll();
				if ( payroll == null || payroll.getId() != 1 || payroll.getHoliday() != 15 
						|| payroll.getEmployee() != loadPerson || payroll.getHourlyRate() != 25 ) {
					stream.writeVerbose("Error: PayRoll loaded wrong");
					_db.rollback();
					return false;
				}
				stream.writeVerbose("OK: PayRoll is valid");

				TestPKsContract cont = loadPerson.getContract();
				if ( cont == null || cont.getPolicyNo() != 1001 || cont.getEmployee() != loadPerson 
						|| cont.getContractNo() != 78 ) {
					stream.writeVerbose("Error: Contract are not what expected!");
					stream.writeVerbose("employe==null? "+cont.getEmployee()+"/"+cont.getEmployee().getFirstName()+"/"+cont.getEmployee().getLastName());
					stream.writeVerbose("loadPerson? "+loadPerson+"/"+loadPerson.getFirstName()+"/"+loadPerson.getLastName());					
					stream.writeVerbose("person? "+person+"/"+person.getFirstName()+"/"+person.getLastName());					
					_db.rollback();
					return false;
				}
				stream.writeVerbose("OK: Contract is valid");

				ArrayList catelist = cont.getCategory();
				itor = catelist.iterator();
				TestPKsContractCategory cate;
				while ( itor.hasNext() ) {
				    cate = (TestPKsContractCategory) itor.next();
					if ( cate.getId() == 101 && cate.getName().equals("Full-time slave") ) {
					} else if ( cate.getId() == 102 && cate.getName().equals("Full-time employee") ) {
					} else {
						stream.writeVerbose("Error: Category is wrong");
						_db.rollback();
						return false;
					}							
				}
				stream.writeVerbose("OK: Categories are valid");

				// now modify it!
				address.remove( addresses[0] );
				addresses[1].setStreet("New Second Street");
			} else {
				_db.rollback();
				stream.writeVerbose("Error: FirstName, LastName or Birthday is wrong!");
				return false;
			}				
			_db.commit();


        } catch ( Exception e ) {
            result = false;
            e.printStackTrace();
        }
        return result;
    }
}




