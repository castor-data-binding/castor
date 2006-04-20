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
 *
 */

package jdo;

import java.util.ArrayList;
import java.util.Iterator;

import java.sql.*;

import org.xml.sax.ContentHandler;

import org.exolab.castor.jdo.JDO;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.QueryResults;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.util.Logger;

import junit.framework.TestSuite;
import junit.framework.TestCase;
import junit.framework.Assert;
import harness.TestHarness;
import harness.CastorTestCase;

/**
 * Expire Cache test. Tests the ability to clear objects from the cache.  This
 * includes clearing objects by class or type, or individually, by 
 * object identities.
 */
public class ExpireManyToMany extends CastorTestCase {
    
    private static int PERSONS_PER_GROUP           = 4;

    private static final boolean BY_TYPE_OR_CLASS   = true;
    private static final boolean BY_OBJECT_IDENTITY = false;

    private static final String JDO_ORIGINAL_VALUE  = "Original JDO String";
    private static final String JDO_UPDATED_VALUE   = "Updated Using JDO";
    private static final String JDBC_UPDATED_VALUE  = "Updated Using JDBC";

    private ArrayList           _txnList = new ArrayList();
    private Database            _db;
    private JDOCategory         _category;
    private Connection          _conn;
    private PreparedStatement   _updateGroupStatement;
    private PreparedStatement   _updatePersonStatement;
    
    private int groupAId = 201, groupBId = 202, groupCId = 203, groupDId = 204;
    private int person1Id = 1, person2Id = 2, person3Id = 3, person4Id = 4;
    
    private boolean debug = false;
    
    /** 
     * Constructor
     */
    public ExpireManyToMany(TestHarness category) {
        super( category, "TC71", "Expire Many-To-Many" );
        _category = (JDOCategory) category;
    }

    /**
     * Initializes fields
     */
    public void setUp()
            throws SQLException {

        try {
            _db   = _category.getDatabase(verbose);
        }
        catch(Exception e) {
        }
        _conn = _category.getJDBCConnection();

        // initialze JDBC connection
       try {
            _updateGroupStatement = 
                _conn.prepareStatement("update test_many_group set value1=? where gid=?");
            _updatePersonStatement = 
                _conn.prepareStatement("update test_many_person set value1=? where pid=?");
        }
        catch (java.sql.SQLException e) {
            fail("Failed to establish JDBC Connection");
        }

    }

    /**
     * Calls the individual tests embedded in this test case
     */
    public void runTest() 
            throws PersistenceException, SQLException {
                
        testExpireCache(BY_OBJECT_IDENTITY);
        testExpireCache(BY_TYPE_OR_CLASS);
        
    }

    /**
     * Close the database and JDBC connection
     */
    public void tearDown() 
            throws PersistenceException, SQLException {
        if ( _db.isActive() ) _db.rollback();
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
    public void testExpireCache(boolean expireByType) {

        log("starting testExpireCache "+(expireByType ? "by type" : "by object identity"));
        try {
            
            // delete any data left over from previous tests
            deleteTestDataSet();
            try {
                createTestDataSet();
            }
            catch (Exception e) {
                // attempt to delete any test data that was created.
                deleteTestDataSet();
                fail("Failed to create test data set");
            }

            // "prime" the cache
            validReadTransaction( groupAId, JDO_ORIGINAL_VALUE, true );
            validReadTransaction( groupBId, JDO_ORIGINAL_VALUE, true);
            validReadTransaction( groupCId, JDO_ORIGINAL_VALUE, true );
            validReadTransaction( groupDId, JDO_ORIGINAL_VALUE, true );

            // now update the database outside of JDO
            updateGroupUsingJDBC( groupAId );
            updateGroupUsingJDBC( groupBId );
            updateGroupUsingJDBC( groupCId );
            updateGroupUsingJDBC( groupDId );

            updatePersonUsingJDBC( person1Id );
            updatePersonUsingJDBC( person2Id );
            updatePersonUsingJDBC( person3Id );
            updatePersonUsingJDBC( person4Id );

            // and force the cache to expire
            expire(expireByType);

            // validate that cached field values are not used in
            // subsequent read/write operations
            boolean success = true;
            if (!validReadTransaction( groupAId, JDBC_UPDATED_VALUE, true ) ) success = false;
            if (!validWriteTransaction( groupAId ) ) success = false;

            // don't validate people because they should now be cached with the
            // JDO_UPDATE_VALUE and won't pass the following validReadTransaction tests.
            if (!validReadTransaction( groupBId, JDBC_UPDATED_VALUE, false )) success = false;
            if (!validWriteTransaction( groupBId )) success = false;

            if (!validReadTransaction( groupCId, JDBC_UPDATED_VALUE, false )) success = false;
            if (!validWriteTransaction( groupCId )) success = false;

            if (!validReadTransaction( groupDId, JDBC_UPDATED_VALUE, false )) success = false;
            if (!validWriteTransaction( groupDId )) success = false;

            if (success) 
                log("Test Completed Successfully.");
            else {
                fail("Cache was not properly expired");
            }

            deleteTestDataSet();
        }
        catch (Exception e) {
            log("ERROR: fatal exception encountered during test");
            fail("Exception encountered during test");
//            e.printStackTrace();
        }
    }

    /**
     * Create the requisite objects in the database
     */
    private void createTestDataSet() throws Exception {
        log("creating test data set...");
        TestManyGroup groupA, groupB, groupC, groupD;
        TestManyPerson person1, person2, person3, person4;
        ArrayList al, bl, c1, d1;
        Database db = null;
        try {
            db = this._category.getDatabase( verbose );
            db.begin();

            //
            // create four persons
            //
            person1 = new TestManyPerson();
            ArrayList gPerson1 = new ArrayList();
            person1.setId(person1Id);
            person1.setGroup( gPerson1 );
            person1.setSthelse("Something else");
            person1.setHelloworld("Hello World!");
            person1.setValue1(JDO_ORIGINAL_VALUE);

            person2 = new TestManyPerson();
            ArrayList gPerson2 = new ArrayList();
            person2.setId(person2Id);
            person2.setGroup( gPerson2 );
            person2.setSthelse("Something else");
            person2.setHelloworld("Hello World!");
            person2.setValue1(JDO_ORIGINAL_VALUE);

            person3 = new TestManyPerson();
            ArrayList gPerson3 = new ArrayList();
            person3.setId(person3Id);
            person3.setGroup( gPerson3 );
            person3.setSthelse("Something else for person 3");
            person3.setHelloworld("Hello World!");
            person3.setValue1(JDO_ORIGINAL_VALUE);

            person4 = new TestManyPerson();
            ArrayList gPerson4 = new ArrayList();
            person4.setId(person4Id);
            person4.setGroup( gPerson4 );
            person4.setSthelse("Something else for person 4");
            person4.setHelloworld("Hello World!");
            person4.setValue1(JDO_ORIGINAL_VALUE);

            //
            // create four groups, assign all persons to each group
            //
            groupA = new TestManyGroup();
            groupA.setValue1(JDO_ORIGINAL_VALUE);
            al = new ArrayList();
            al.add( person1 );
            al.add( person2 );
            al.add( person3 );
            al.add( person4 );
            groupA.setId(groupAId);
            groupA.setPeople( al );

            groupB = new TestManyGroup();
            groupB.setValue1(JDO_ORIGINAL_VALUE);
            groupB.setId(groupBId);
            bl = new ArrayList();
            bl.add( person1 );
            bl.add( person2 );
            bl.add( person3 );
            bl.add( person4 );
            groupB.setPeople( bl );

            groupC = new TestManyGroup();
            groupC.setValue1(JDO_ORIGINAL_VALUE);
            c1 = new ArrayList();
            c1.add( person1 );
            c1.add( person2 );
            c1.add( person3 );
            c1.add( person4 );
            groupC.setId(groupCId);
            groupC.setPeople( c1 );

            groupD = new TestManyGroup();
            groupD.setValue1(JDO_ORIGINAL_VALUE);
            d1 = new ArrayList();
            d1.add( person1 );
            d1.add( person2 );
            d1.add( person3 );
            d1.add( person4 );
            groupD.setId(groupDId);
            groupD.setPeople( d1 );

            //
            // assign all groups to each person
            //
            gPerson1.add( groupA );
            gPerson1.add( groupB );
            gPerson1.add( groupC );
            gPerson1.add( groupD );

            gPerson2.add( groupA );
            gPerson2.add( groupB );
            gPerson2.add( groupC );
            gPerson2.add( groupD );

            gPerson3.add( groupA );
            gPerson3.add( groupB );
            gPerson3.add( groupC );
            gPerson3.add( groupD );

            gPerson4.add( groupA );
            gPerson4.add( groupB );
            gPerson4.add( groupC );
            gPerson4.add( groupD );

            //
            // create persistent groups and persons
            //
            db.create( person1 );
            db.create( person2 );
            db.create( person3 );
            db.create( person4 );
            
            db.create( groupA );
            db.create( groupB );
            db.create( groupC );
            db.create( groupD );
            
            db.commit();
        }
        catch (Exception e) {
            log("createTestDataSet: exception caught: "+e.getMessage());
            throw e;
        }

    }

    /**
     * Update an object outside of JDO using a JDBC connection.
     *
     * @param masterId primary key of object to be updated
     */
    private void updateGroupUsingJDBC( int groupId ) {
        log("updating group "+groupId+" using JDBC...");

        try {
            _updateGroupStatement.setString(1, JDBC_UPDATED_VALUE);
            _updateGroupStatement.setInt(2, groupId);
            int rc = _updateGroupStatement.executeUpdate();
            if( rc <= 0 ){
                log("updateGroupUsingJDBC: error updating group "+groupId+", return code = "+rc);
                return;
            }
        }
        catch (Exception e) {
            log("updateGroupUsingJDBC: exception updating group "+groupId+": "+e.getMessage());
//                e.printStackTrace();
        }
    }

    /**
     * Update an object outside of JDO using a JDBC connection.
     *
     * @param masterId primary key of object to be updated
     */
    private void updatePersonUsingJDBC( int personId ) {
        log("updating person "+personId+" using JDBC...");

        try {
            _updatePersonStatement.setString(1, JDBC_UPDATED_VALUE);
            _updatePersonStatement.setInt(2, personId);
            int rc = _updatePersonStatement.executeUpdate();
            if( rc <= 0 ){
                log("updatePersonUsingJDBC: error updating person "+personId+", return code = "+rc);
                return;
            }
        }
        catch (Exception e) {
            log("updatePersonUsingJDBC: exception updating person "+personId+": "+e.getMessage());
//                e.printStackTrace();
        }
    }

    /**
     * Setup and execute a database expire cache request.
     *
     * @param expireByType when <code>true</code> this method will request that
     *      objects be expired from the cache by specifying a single type or
     *      class of objects to be expired; when <code>false</code> this method
     *      will request that objects be expired from the cache using individual
     *      object identities
     */
    private void expire(boolean byType) {
        log("expiring cache...");

        Class[] typeArray = null;
        Object[] identityArray = null;

        if ( byType ) {
            typeArray = new Class[2];
            typeArray[0] = TestManyGroup.class;
            typeArray[1] = TestManyPerson.class;
        }
        else {
            typeArray = new Class[4];
            identityArray = new Object[4];

            typeArray[0] = TestManyGroup.class;
            identityArray[0] = new Integer(groupAId);

            typeArray[0] = TestManyGroup.class;
            identityArray[1] = new Integer(groupBId);

            typeArray[0] = TestManyGroup.class;
            identityArray[2] = new Integer(groupCId);

            typeArray[0] = TestManyGroup.class;
            identityArray[3] = new Integer(groupDId);
        }

        try {
            this._category.getDatabase( verbose ).expireCache(typeArray, identityArray);
        }
        catch (Exception e) {
            log("expireCache: exception encountered clearing cache: "+e.getMessage());
//            e.printStackTrace();
        }
    }

    /**
     * Read, or load, an object and validate that the field values
     * reflect values updated outside of JDO and not from previously
     * cached values.
     *
     * @param groupId primary key of object to be read
     */
    private boolean validReadTransaction( int groupId, String expectedValue, boolean checkPeople ) {
        log("validating read transaction for group "+groupId+"...");
        Database db = null;
        boolean valid = true;
        try {
            db = this._category.getDatabase( verbose );
            db.begin();
            TestManyGroup group = (TestManyGroup)db.load(TestManyGroup.class, new Integer(groupId));
            if (group.getValue1().compareTo(expectedValue) != 0) {
                log("validReadTransaction: value in group "+group.getId()+
                    " does not match expected value, value: "+group.getValue1()+
                    ", expected: "+expectedValue);
                valid = false;
            }
            if (checkPeople) {
                Iterator itor = group.getPeople().iterator();
                while ( itor.hasNext() ) {
                    TestManyPerson person = (TestManyPerson)itor.next();
                    if (person.getValue1().compareTo(expectedValue) != 0) {
                        log("validReadTransaction: value in person "+person.getId()+
                            " does not match expected value, value: "+person.getValue1()+
                            ", expected: "+expectedValue);
                        valid = false;
                    }
                }
            }
            db.rollback();
            db.close();
        }
        catch (Exception e) {
            if (db != null) {
                try {
                    db.close();
                }
                catch (Exception se) {
                }
            }
            log("validReadTransaction: exception caught while validating read for group "+groupId+" : "+e.getMessage());
//                    e.printStackTrace();
            valid = false;
        }
        return valid;
    }

    /**
     * Modify fields of an object and validate that the transaction completes
     * successfully.
     *
     * @param masterId primary key of object to be updated
     */
    private boolean validWriteTransaction( int groupId ) {
        log("validating write transaction for group "+groupId+"...");
        Database db = null;
        try {
            db = this._category.getDatabase( verbose );
            db.begin();
            TestManyGroup group = (TestManyGroup)db.load(TestManyGroup.class, new Integer(groupId));
            group.setValue1(JDO_UPDATED_VALUE);
            Iterator itor = group.getPeople().iterator();
            while ( itor.hasNext() ) {
                TestManyPerson person = (TestManyPerson)itor.next();
                person.setValue1(JDO_UPDATED_VALUE);
            }
            db.commit();
            db.close();
        }
        catch (Exception e) {
            if (db != null) {
                try {
                    db.close();
                }
                catch (Exception se) {
                }
            }
            log("validWriteTransaction: exception caught while validating write for group "+groupId+" : "+e.getMessage());
//            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * Delete all objects in test data set.
     */
    private void deleteTestDataSet() {
        log("deleting test data set...");
        OQLQuery oql;
        OQLQuery oqlp;
        QueryResults enum;
        TestManyGroup group = null;
        TestManyPerson person = null;
        Database db = null;
        try {
            // select an group and delete it, if it exist!
            db = this._category.getDatabase( verbose );
            db.begin();
            OQLQuery oqlclean = db.getOQLQuery( "SELECT object FROM jdo.TestManyGroup object WHERE object.id < $1" );
            oqlclean.bind( Integer.MAX_VALUE );
            enum = oqlclean.execute();
            while ( enum.hasMore() ) {
                group = (TestManyGroup) enum.next();
                stream.println( "Retrieved object: " + group );
                db.remove( group );
                stream.println( "Deleted object: " + group );
            }
            db.commit();

            db.begin();
            oqlclean = db.getOQLQuery( "SELECT object FROM jdo.TestManyPerson object WHERE object.id < $1" );
            oqlclean.bind( Integer.MAX_VALUE );
            enum = oqlclean.execute();
            while ( enum.hasMore() ) {
                person = (TestManyPerson) enum.next();
                stream.println( "Retrieved object: " + person );
                db.remove( person );
                stream.println( "Deleted object: " + person );
            } 
            db.commit();
        }
        catch (Exception e) {
            if (db != null) {
                try {
                    db.close();
                }
                catch (Exception se) {
                }
            }
            log("deleteTestDataSet: exception caught: "+e.getMessage());
        }
    }

    /**
     * log a message
     */
    private void log(String s)
    {
        if (debug)
            System.out.println(s);
        else
            stream.println(s);
    }

}