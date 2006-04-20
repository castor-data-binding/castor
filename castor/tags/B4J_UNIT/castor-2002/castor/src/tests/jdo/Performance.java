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
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Iterator;
import java.util.Set;
import java.util.HashSet;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.ObjectModifiedException;
import org.exolab.castor.jdo.QueryResults;
import org.exolab.jtf.CWVerboseStream;
import org.exolab.jtf.CWTestCase;
import org.exolab.jtf.CWTestCategory;
import org.exolab.exceptions.CWClassConstructorException;


/**
 * Concurrent access test. Tests a JDO modification and concurrent
 * JDBC modification to determine if JDO can detect the modification
 * with dirty checking.
 */
public class Performance extends CWTestCase {

    public final static int NUMBER_OF_PERSON = 100;

    public final static int NUMBER_OF_ADDRESS_PER_PERSON = 20;

    public final static int NUMBER_OF_LOAD = 200;

    private Database       _db; 


    private Connection     _conn;


    private JDOCategory    _category;


    static final String    JDBCValue = "jdbc value";


    static final String    JDOValue = "jdo value";


    public Performance( CWTestCategory category )
            throws CWClassConstructorException {
        super( "TC63", "Test on database's outter join and alternatives" );
        _category = (JDOCategory) category;
    }


    public void preExecute() {
        super.preExecute();
    }


    public void postExecute() {
        super.postExecute();
    }
    public boolean run( CWVerboseStream stream ) {
        boolean result = true;

        try {
            _db = _category.getDatabase( stream.verbose() );
            _conn = _category.getJDBCConnection(); 
            createData();
            System.out.println();
            System.out.println("pretest");
            runOnce( stream );
            System.out.println();
            System.out.println("real test");
            runOnce( stream );
            // clear up
            _db.close();
            _conn.close();
        } catch ( Exception except ) {
            stream.writeVerbose( "Error: " + except );
            except.printStackTrace();
            result = false;
        }
        return result;
    }
    private void createData() throws SQLException {
        long startTime, endTime;
        Statement st = _conn.createStatement();
        st.executeUpdate("DELETE test_rel_person");
        st.executeUpdate("DELETE test_rel_employee");
        st.executeUpdate("DELETE test_rel_address");
        st.executeUpdate("DELETE test_rel_payroll");
        startTime = System.currentTimeMillis();
        PreparedStatement stat = _conn.prepareStatement("INSERT INTO test_rel_person VALUES (?, ?, ?, ?)");
        for ( int i=0; i < NUMBER_OF_PERSON; i++ ) {
            stat.setInt( 1, i+NUMBER_OF_PERSON*100 );
            stat.setString( 2, "FirstName"+i );
            stat.setString( 3, "LastName" +i );
            stat.setObject( 4, new java.sql.Date( (315360000l + (i * 24 * 3600) )*1000l ) );
            stat.executeUpdate();
        }

        stat = _conn.prepareStatement("INSERT INTO test_rel_employee VALUES (?, ?)");
        for ( int i=0; i < NUMBER_OF_PERSON; i++ ) {
            stat.setInt( 1, i+NUMBER_OF_PERSON*100 );
            stat.setObject( 2, new java.sql.Date( (615360000l + (i * 24 * 3600) )*1000l ) );
            stat.executeUpdate();            
        }

        stat = _conn.prepareStatement("INSERT INTO test_rel_address VALUES (?, ?, ?, ?, ?, ?)");
        for ( int i=0; i < NUMBER_OF_PERSON; i++ ) {
            for ( int j=0; j < NUMBER_OF_ADDRESS_PER_PERSON; j++ ) {
                stat.setInt( 1, i+NUMBER_OF_PERSON*100 );
                stat.setInt( 2, i*100+NUMBER_OF_PERSON*100+j );
                stat.setString( 3, "street"+(i+NUMBER_OF_PERSON*100+j) );
                stat.setString( 4, "city"+(i+NUMBER_OF_PERSON*100+j) );
                stat.setString( 5, "state"+(i+NUMBER_OF_PERSON*100+j ) );
                stat.setString( 6, (i*100+j)+"" );
                stat.executeUpdate();
            }
        }
        stat = _conn.prepareStatement("INSERT INTO test_rel_payroll VALUES (?, ?, ?, ?)");
        for ( int i=0; i < NUMBER_OF_PERSON; i++ ) {
            stat.setInt( 1, i+NUMBER_OF_PERSON*100 );
            stat.setInt( 2, i+NUMBER_OF_PERSON*200 );
            stat.setInt( 3, 200 );
            stat.setInt( 4, 300 );
            stat.executeUpdate();          
        }
        endTime = System.currentTimeMillis();
        System.out.println("Time taken to create table: "+(endTime-startTime));
        _conn.commit();
    }
    public void runOnce( CWVerboseStream stream ) throws Exception {
            // generate random data
            // add data into table
            PreparedStatement stat, stat1, stat2, stat3, stat4, stat5, stat6;
            Iterator i1, i2;
            ResultSet rs;
            int row, field, st;
            long startTime, endTime;
            Object id;
            Set set1 = new HashSet(30);
            Set set2 = new HashSet(30);

            System.out.println("Outter join:");
            /*
            String joinStat = "SELECT test_rel_person.pid, test_rel_person.fname, "+
                "test_rel_person.lname, test_rel_person.bday, test_rel_employee.start_date, test_rel_address.id, test_rel_payroll.id "+
                "FROM test_rel_person, test_rel_employee, test_rel_address, test_rel_payroll "+
                "WHERE test_rel_person.pid=test_rel_employee.pid AND test_rel_person.pid=test_rel_address.pid(+) "+
                "AND test_rel_person.pid=test_rel_address.pid(+) AND test_rel_person.pid=test_rel_payroll.pid(+) " +
                "AND test_rel_person.pid=?";
            */
            String joinStat = "SELECT test_rel_person.pid, test_rel_person.fname, "+
                "test_rel_person.fname, test_rel_person.bday, test_rel_employee.start_date, test_rel_address.id, test_rel_payroll.id "+
                "FROM test_rel_person, test_rel_employee, test_rel_address, test_rel_payroll "+
                "WHERE test_rel_person.pid=test_rel_employee.pid AND test_rel_person.pid*=test_rel_address.pid "+
                "AND test_rel_person.pid*=test_rel_address.pid AND test_rel_person.pid*=test_rel_payroll.pid " +
                "AND test_rel_person.pid=?";
            stat = _conn.prepareStatement( joinStat );
            stat1 = _conn.prepareStatement("SELECT test_rel_address.id "+
                "FROM test_rel_address "+
                "WHERE test_rel_address.pid=?");
            stat2 = _conn.prepareStatement("SELECT test_rel_payroll.id "+
                "FROM test_rel_payroll "+
                "WHERE test_rel_payroll.pid=?");
            stat3 = _conn.prepareStatement("SELECT * "+
                "FROM test_rel_address "+
                "WHERE test_rel_address.id=?");
            stat4 = _conn.prepareStatement("SELECT * "+
                "FROM test_rel_payroll "+
                "WHERE test_rel_payroll.id=?");
            stat5 = _conn.prepareStatement("SELECT * "+
                "FROM test_rel_address "+
                "WHERE test_rel_address.pid=?");
            stat6 = _conn.prepareStatement("SELECT * "+
                "FROM test_rel_payroll "+
                "WHERE test_rel_payroll.pid=?");

            row = 0;
            field = 0;
            st = 0;
            // ------
            // Case 1
            // ------
            startTime = System.currentTimeMillis(); 
            for ( int i=0; i < NUMBER_OF_LOAD; i++ ) {
                set1.clear();
                set2.clear();
                stat.setInt( 1, NUMBER_OF_PERSON*100 + Math.max(0, ((int)(Math.random() * NUMBER_OF_PERSON))) );
                rs = stat.executeQuery();
                ++st;
                while ( rs.next() ) {
                    ++row;
                    for ( int j=1; j <8; j++ ) {
                        rs.getObject(j);
                        ++field;
                    }
                    set1.add( rs.getObject(6) );
                    ++field;
                    set2.add( rs.getObject(7) );
                    ++field;
                }
                rs.close();

                i1 = set1.iterator();
                while ( i1.hasNext() ) {                    
                    stat3.setObject( 1, i1.next() );
                    rs = stat3.executeQuery();
                    ++st;
                    if ( rs.next() ) {
                        ++row;
                        for ( int j=1; j < 7; j++ ) {
                            rs.getObject( j );
                            ++field;
                        }
                    } else {
                        throw new Exception("Object not found!");
                    }
                    rs.close();
                }
                i2 = set2.iterator();
                while ( i2.hasNext() ) {
                    stat4.setObject( 1, i2.next() );
                    rs = stat4.executeQuery();
                    ++st;
                    if ( rs.next() ) {
                        ++row;
                        for ( int j=1; j < 5; j++ ) {
                            rs.getObject( j );
                            ++field;
                        }
                    } else {
                        throw new Exception("Object not found!");
                    }
                    rs.close();
                }
            }            
            endTime = System.currentTimeMillis();
            System.out.println("time taken: "+((endTime-startTime))+" stat executed: "+st+" no. of row: "+row+" no. of field: "+field);
            _conn.commit();
            // ------
            // Case 2
            // ------
            System.out.println("Extra queries to load dependent id, instead of outer join.");
            row = 0;
            field = 0;
            st = 0;
            startTime = System.currentTimeMillis();
            for ( int i=0; i < NUMBER_OF_LOAD; i++ ) {
                set1.clear();
                set2.clear();
                // load employee
                stat.setInt( 1, NUMBER_OF_PERSON*100 + Math.max(0, ((int)(Math.random() * NUMBER_OF_PERSON))) );
                rs = stat.executeQuery();
                ++st;
                if ( rs.next() ) {
                    ++row;
                    id = rs.getObject(1);
                    for ( int j=2; j <6; j++ ) {
                        rs.getObject(j);
                        ++field;
                    }                
                    rs.close();

                    // load dependent ids
                    stat1.setObject( 1, id );
                    rs = stat1.executeQuery();
                    ++st;
                    while ( rs.next() ) {
                        ++row;
                        set1.add( rs.getObject(1) );
                        ++field;
                    }
                    rs.close();
                    stat2.setObject( 1, id );
                    rs = stat2.executeQuery();
                    ++st;
                    while ( rs.next() ) {
                        ++row;
                        set2.add( rs.getObject(1) );
                        ++field;
                    }
                    rs.close();

                    // load dependent objects
                    i1 = set1.iterator();
                    Object o;
                    while ( i1.hasNext() ) {                    
                        o = i1.next();
                        stat3.setObject( 1, o );
                        rs = stat3.executeQuery();
                        ++st;
                        if ( rs.next() ) {
                            ++row;
                            for ( int j=1; j < 7; j++ ) {
                                rs.getObject( j );
                                ++field;
                            }
                        } else {
                            throw new Exception("Object not found!"+o+"i: "+i);
                        }
                        rs.close();
                    }
                    i2 = set2.iterator();
                    while ( i2.hasNext() ) {
                        stat4.setObject( 1, i2.next() );
                        rs = stat4.executeQuery();
                        ++st;
                        if ( rs.next() ) {
                            ++row;
                            for ( int j=1; j < 5; j++ ) {
                                rs.getObject( j );
                                ++field;
                            }
                        } else {
                            throw new Exception("Object not found!");
                        }
                        rs.close();
                    }
                } else {
                   throw new Exception("Object not found!");
                }
            }
            endTime = System.currentTimeMillis();
            System.out.println("time taken: "+((endTime-startTime))+" stat executed: "+st+" no. of row: "+row+" no. of field: "+field);
            _conn.commit();

            // ------
            // Case 3
            // ------
            System.out.println("No extra and no outer join");
            row = 0;
            field = 0;
            st = 0;
            startTime = System.currentTimeMillis();
            for ( int i=0; i < NUMBER_OF_LOAD; i++ ) {
                // load employee
                stat.setInt( 1, NUMBER_OF_PERSON*100 + Math.max(0, ((int)(Math.random() * NUMBER_OF_PERSON))) );
                rs = stat.executeQuery();
                ++st;
                if ( rs.next() ) {
                    ++row;
                    id = rs.getObject(1);
                    for ( int j=2; j <6; j++ ) {
                        ++field;
                        rs.getObject(j);
                    }                
                    rs.close();

                    // load dependent objects
                    stat5.setObject( 1, id );
                    rs = stat5.executeQuery();
                    ++st;
                    while ( rs.next() ) {
                        ++row;
                        for ( int j=1; j < 7; j++ ) {
                            rs.getObject(j);                        
                            ++field;
                        }
                    }
                    rs.close();
                    stat6.setObject( 1, id );
                    rs = stat6.executeQuery();
                    ++st;
                    while ( rs.next() ) {
                        ++row;
                        for ( int j=1; j <5; j++ ) {
                            rs.getObject(j);                        
                            ++field;
                        }
                    }
                    rs.close();
                } else {
                    throw new Exception("Object not found!");
                }
            }
            endTime = System.currentTimeMillis();
            System.out.println("time taken: "+((endTime-startTime))+" stat executed: "+st+" no. of row: "+row+" no. of field: "+field);
            _conn.commit();

            // ------
            // Case 4
            // ------
            System.out.println("No extra and no outer join and reload");
            row = 0;
            field = 0;
            st = 0;
            startTime = System.currentTimeMillis();
            for ( int i=0; i < NUMBER_OF_LOAD; i++ ) {
                set1.clear();
                set2.clear();
                // load employee
                stat.setInt( 1, NUMBER_OF_PERSON*100 + Math.max(0, ((int)(Math.random() * NUMBER_OF_PERSON))) );
                rs = stat.executeQuery();
                ++st;
                if ( rs.next() ) {
                    ++row;
                    id = rs.getObject(1);
                    for ( int j=2; j <6; j++ ) {
                        ++field;
                        rs.getObject(j);
                    }                
                    rs.close();

                    // load dependent objects
                    stat5.setObject( 1, id );
                    rs = stat5.executeQuery();
                    ++st;
                    while ( rs.next() ) {
                        ++row;
                        for ( int j=1; j < 7; j++ ) {
                            if ( j != 2 )
                                rs.getObject(j);
                            else 
                                set1.add( rs.getObject(j) );
                            ++field;
                        }
                    }
                    rs.close();
                    stat6.setObject( 1, id );
                    rs = stat6.executeQuery();
                    ++st;
                    while ( rs.next() ) {
                        ++row;
                        for ( int j=1; j <5; j++ ) {
                            if ( j != 2 )
                                rs.getObject(j);                        
                            else
                                rs.getObject(j);
                            ++field;
                        }
                    }
                    rs.close();

                    i1 = set1.iterator();
                    while ( i1.hasNext() ) {                    
                        stat3.setObject( 1, i1.next() );
                        rs = stat3.executeQuery();
                        ++st;
                        if ( rs.next() ) {
                            ++row;
                            for ( int j=1; j < 7; j++ ) {
                                rs.getObject( j );
                                ++field;
                            }
                        } else {
                            throw new Exception("Object not found!");
                        }
                        rs.close();
                    }
                    i2 = set2.iterator();
                    while ( i2.hasNext() ) {
                        stat4.setObject( 1, i2.next() );
                        rs = stat4.executeQuery();
                        ++st;
                        if ( rs.next() ) {
                            ++row;
                            for ( int j=1; j < 5; j++ ) {
                                rs.getObject( j );
                                ++field;
                            }
                        } else {
                            throw new Exception("Object not found!");
                        }
                        rs.close();
                    }

                } else {
                    throw new Exception("Object not found!");
                }
            }
            endTime = System.currentTimeMillis();
            System.out.println("time taken: "+((endTime-startTime))+" stat executed: "+st+" no. of row: "+row+" no. of field: "+field);
            _conn.commit();
            stat.close();
            stat1.close();
            stat2.close();
            stat3.close();
            stat4.close();
            stat5.close();
            stat6.close();
    
            // ---------------------------------------------------
            // prepareStatement everytime
            // ---------------------------------------------------
            // ------
            // Case 4
            // ------
            System.out.println("Outer Join (prepare statement everytime)");
            st = 0;
            row = 0;
            field = 0;
            startTime = System.currentTimeMillis(); 
            for ( int i=0; i < NUMBER_OF_LOAD; i++ ) {
                stat = _conn.prepareStatement( joinStat );                
                stat3 = _conn.prepareStatement("SELECT * "+
                    "FROM test_rel_address "+
                    "WHERE test_rel_address.id=?");
                stat4 = _conn.prepareStatement("SELECT * "+
                    "FROM test_rel_payroll "+
                    "WHERE test_rel_payroll.id=?");

                set1.clear();
                set2.clear();
                stat.setInt( 1, NUMBER_OF_PERSON*100 + Math.max(0, ((int)(Math.random() * NUMBER_OF_PERSON))) );
                rs = stat.executeQuery();
                ++st;
                while ( rs.next() ) {
                    ++row;
                    for ( int j=1; j <8; j++ ) {
                        rs.getObject(j);
                        ++field;
                    }
                    set1.add( rs.getObject(6) );
                    ++field;
                    set2.add( rs.getObject(7) );
                    ++field;
                }
                rs.close();

                i1 = set1.iterator();
                while ( i1.hasNext() ) {                    
                    stat3.setObject( 1, i1.next() );
                    rs = stat3.executeQuery();
                    ++st;
                    if ( rs.next() ) {
                        ++row;
                        for ( int j=1; j < 7; j++ ) {
                            rs.getObject( j );
                            ++field;
                        }
                    } else {
                        throw new Exception("Object not found!");
                    }
                    rs.close();
                }
                i2 = set2.iterator();
                while ( i2.hasNext() ) {
                    stat4.setObject( 1, i2.next() );
                    rs = stat4.executeQuery();
                    ++st;
                    if ( rs.next() ) {
                        ++row;
                        for ( int j=1; j < 5; j++ ) {
                            rs.getObject( j );
                            ++field;
                        }
                    } else {
                        throw new Exception("Object not found!");
                    }
                    rs.close();
                }

                stat.close();
                stat3.close();
                stat4.close();
            }            
            endTime = System.currentTimeMillis();
            System.out.println("time taken: "+((endTime-startTime))+" stat executed: "+st+" no. of row: "+row+" no. of field: "+field);
            _conn.commit();

            // ------
            // Case 5
            // ------
            System.out.println("Extra queries to load dependent id, instead of outer join. (prepare statement everytime)");
            startTime = System.currentTimeMillis();
            st = 0;
            row = 0;
            field = 0;
            for ( int i=0; i < NUMBER_OF_LOAD; i++ ) {
                set1.clear();
                set2.clear();
                // load employee
                stat = _conn.prepareStatement( joinStat);
                stat1 = _conn.prepareStatement("SELECT test_rel_address.id "+
                    "FROM test_rel_address "+
                    "WHERE test_rel_address.pid=?");
                stat2 = _conn.prepareStatement("SELECT test_rel_payroll.id "+
                    "FROM test_rel_payroll "+
                    "WHERE test_rel_payroll.pid=?");
                stat3 = _conn.prepareStatement("SELECT * "+
                    "FROM test_rel_address "+
                    "WHERE test_rel_address.id=?");
                stat4 = _conn.prepareStatement("SELECT * "+
                    "FROM test_rel_payroll "+
                    "WHERE test_rel_payroll.id=?");
                stat.setInt( 1, NUMBER_OF_PERSON*100 + Math.max(0, ((int)(Math.random() * NUMBER_OF_PERSON))) );
                rs = stat.executeQuery();
                ++st;
                if ( rs.next() ) {
                    ++row;
                    id = rs.getObject(1);
                    for ( int j=2; j <6; j++ ) {
                        rs.getObject(j);
                        ++field;
                    }                
                    rs.close();

                    // load dependent ids
                    stat1.setObject( 1, id );
                    rs = stat1.executeQuery();
                    ++st;
                    while ( rs.next() ) {
                        ++row;
                        set1.add( rs.getObject(1) );
                        ++field;
                    }
                    rs.close();

                    stat2.setObject( 1, id );
                    rs = stat2.executeQuery();
                    ++st;
                    while ( rs.next() ) {
                        ++row;
                        set2.add( rs.getObject(1) );
                        ++field;
                    }
                    rs.close();

                    // load dependent objects
                    i1 = set1.iterator();
                    while ( i1.hasNext() ) {                    
                        stat3.setObject( 1, i1.next() );
                        rs = stat3.executeQuery();
                        ++st;
                        if ( rs.next() ) {
                            ++row;
                            for ( int j=1; j < 7; j++ ) {
                                rs.getObject( j );
                                ++field;
                            }
                        }
                        rs.close();
                    }
                    i2 = set2.iterator();
                    while ( i2.hasNext() ) {
                        stat4.setObject( 1, i2.next() );
                        rs = stat4.executeQuery();
                        ++st;
                        if ( rs.next() ) {
                            ++row;
                            for ( int j=1; j < 5; j++ ) {
                                rs.getObject( j );
                                ++field;
                            }
                        }
                        rs.close();
                    }
                } else {
                    throw new Exception("Object not found!");
                }
                stat.close();
                stat1.close();
                stat2.close();
                stat3.close();
                stat4.close();
            }
            endTime = System.currentTimeMillis();
            System.out.println("time taken: "+((endTime-startTime))+" stat executed: "+st+" no. of row: "+row+" no. of field: "+field);
            _conn.commit();

            // ------
            // Case 6
            // ------
            System.out.println("No extra and no outer join (prepare statement everytime)");
            st = 0;
            row = 0;
            field = 0;
            startTime = System.currentTimeMillis();
            for ( int i=0; i < NUMBER_OF_LOAD; i++ ) {
                set1.clear();
                set2.clear();
                // load employee
                stat = _conn.prepareStatement("SELECT test_rel_person.pid, test_rel_person.fname, test_rel_person.lname, " + 
                    "test_rel_person.bday, test_rel_employee.start_date " +
                    "FROM test_rel_person, test_rel_employee "+
                    "WHERE test_rel_person.pid=test_rel_employee.pid AND test_rel_person.pid=?" );
                stat5 = _conn.prepareStatement("SELECT * "+
                    "FROM test_rel_address "+
                    "WHERE test_rel_address.pid=?");
                stat6 = _conn.prepareStatement("SELECT * "+
                    "FROM test_rel_payroll "+
                    "WHERE test_rel_payroll.pid=?");

                stat.setInt( 1, NUMBER_OF_PERSON*100 + Math.max(0, ((int)(Math.random() * NUMBER_OF_PERSON))) );
                rs = stat.executeQuery();
                ++st;
                if ( rs.next() ) {
                    ++row;
                    id = rs.getObject(1);
                    for ( int j=2; j <6; j++ ) {
                        ++field;
                        rs.getObject(j);
                    }                
                    rs.close();

                    // load dependent objects
                    stat5.setObject( 1, id );
                    rs = stat5.executeQuery();
                    ++st;
                    while ( rs.next() ) {
                        ++row;
                        for ( int j=1; j < 7; j++ ) {
                            rs.getObject(j);                        
                            ++field;
                        }
                    }
                    rs.close();

                    stat6.setObject( 1, id );
                    rs = stat6.executeQuery();
                    ++st;
                    while ( rs.next() ) {
                        ++row;
                        for ( int j=1; j < 5; j++ ) {
                            rs.getObject(j);                        
                            ++field;
                        }
                    }
                    rs.close();
                } else {
                    throw new Exception("Object not found!");
                }
                stat.close();
                stat5.close();
                stat6.close();
            }
            endTime = System.currentTimeMillis();
            System.out.println("time taken: "+((endTime-startTime))+" stat executed: "+st+" no. of row: "+row+" no. of field: "+field);
            _conn.commit();
    }
}

