/*
 * Copyright 2009 Ralf Joachim
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
package org.castor.cpa.test.test75;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.castor.cpa.test.framework.CPATestCase;
import org.castor.cpa.test.framework.xml.types.DatabaseEngineType;
import org.exolab.castor.jdo.CacheManager;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.JDOManager;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.QueryResults;

/**
 * Expire Cache test. Tests the ability to clear objects from the cache.  This
 * includes clearing objects by class or type, or individually, by 
 * object identities.
 */
public final class TestExpireManyToMany extends CPATestCase {
    /** The <a href="http://jakarta.apache.org/commons/logging/">Jakarta
     *  Commons Logging</a> instance used for all logging. */
    private static final Log LOG = LogFactory.getLog(TestExpireManyToMany.class);

    private static final String DBNAME = "test75";
    private static final String MAPPING = "/org/castor/cpa/test/test75/mapping.xml";

    private static final boolean BY_TYPE_OR_CLASS   = true;
    private static final boolean BY_OBJECT_IDENTITY = false;

    private static final String JDO_ORIGINAL_VALUE  = "Original JDO String";
    private static final String JDO_UPDATED_VALUE   = "Updated Using JDO";
    private static final String JDBC_UPDATED_VALUE  = "Updated Using JDBC";

    private Database            _db;
    private Connection          _conn;
    private PreparedStatement   _updateGroupStatement;
    private PreparedStatement   _updatePersonStatement;
    
    private int _groupAId = 201, _groupBId = 202, _groupCId = 203, _groupDId = 204;
    private int _person1Id = 1, _person2Id = 2, _person3Id = 3, _person4Id = 4;
    
    /** 
     * Constructor
     */
    public TestExpireManyToMany(final String name) {
        super(name);
    }

    // Test are only included/excluded for engines that have been tested with this test suite.

    public boolean include(final DatabaseEngineType engine) {
        return (engine == DatabaseEngineType.DERBY)
            || (engine == DatabaseEngineType.HSQL)
            || (engine == DatabaseEngineType.MYSQL)
            || (engine == DatabaseEngineType.POSTGRESQL);
    }

    /**
     * Initializes fields
     */
    public void setUp() throws Exception {
        JDOManager jdo = getJDOManager(DBNAME, MAPPING);
        
        _db = jdo.getDatabase();
        _conn = jdo.getConnectionFactory().createConnection();

        // initialze JDBC connection
       try {
            _updateGroupStatement = _conn.prepareStatement(
                    "update test75_group set value1=? where gid=?");
            _updatePersonStatement = _conn.prepareStatement(
                    "update test75_person set value1=? where pid=?");
        } catch (java.sql.SQLException e) {
            fail("Failed to establish JDBC Connection");
        }
    }

    /**
     * Close the database and JDBC connection
     */
    public void tearDown() throws PersistenceException, SQLException {
        if (_db.isActive()) { _db.rollback(); }
        _db.close();
        _conn.close();
    }

    /**
     * Calls the individual tests embedded in this test case
     */
    public void testExpireCache() {
        testExpireCache(BY_OBJECT_IDENTITY);
        testExpireCache(BY_TYPE_OR_CLASS);
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
        LOG.debug("starting testExpireCache "
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

            // "prime" the cache
            validReadTransaction(_groupAId, JDO_ORIGINAL_VALUE, true);
            validReadTransaction(_groupBId, JDO_ORIGINAL_VALUE, true);
            validReadTransaction(_groupCId, JDO_ORIGINAL_VALUE, true);
            validReadTransaction(_groupDId, JDO_ORIGINAL_VALUE, true);

            // now update the database outside of JDO
            updateGroupUsingJDBC(_groupAId);
            updateGroupUsingJDBC(_groupBId);
            updateGroupUsingJDBC(_groupCId);
            updateGroupUsingJDBC(_groupDId);

            updatePersonUsingJDBC(_person1Id);
            updatePersonUsingJDBC(_person2Id);
            updatePersonUsingJDBC(_person3Id);
            updatePersonUsingJDBC(_person4Id);

            // and force the cache to expire
            expire(expireByType);

            // validate that cached field values are not used in
            // subsequent read/write operations
            boolean success = true;
            if (!validReadTransaction(_groupAId, JDBC_UPDATED_VALUE, true)) {
                success = false;
            }
            if (!validWriteTransaction(_groupAId)) { success = false; }

            // don't validate people because they should now be cached with the
            // JDO_UPDATE_VALUE and won't pass the following validReadTransaction tests.
            if (!validReadTransaction(_groupBId, JDBC_UPDATED_VALUE, false)) {
                success = false;
            }
            if (!validWriteTransaction(_groupBId)) { success = false; }

            if (!validReadTransaction(_groupCId, JDBC_UPDATED_VALUE, false)) {
                success = false;
            }
            if (!validWriteTransaction(_groupCId)) { success = false; }

            if (!validReadTransaction(_groupDId, JDBC_UPDATED_VALUE, false)) {
                success = false;
            }
            if (!validWriteTransaction(_groupDId)) { success = false; }

            if (success) {
                LOG.debug("Test Completed Successfully.");
            } else {
                fail("Cache was not properly expired");
            }

            deleteTestDataSet();
        } catch (Exception e) {
            LOG.warn("ERROR: fatal exception encountered during test", e);
            fail("Exception encountered during test");
        }
    }

    /**
     * Create the requisite objects in the database
     */
    private void createTestDataSet() throws Exception {
        LOG.debug("creating test data set...");
        ManyGroup groupA, groupB, groupC, groupD;
        ManyPerson person1, person2, person3, person4;
        ArrayList<ManyPerson> al;
        ArrayList<ManyPerson> bl;
        ArrayList<ManyPerson> c1;
        ArrayList<ManyPerson> d1;
        Database db = null;
        try {
            db = getJDOManager(DBNAME, MAPPING).getDatabase();
            db.begin();

            //
            // create four persons
            //
            person1 = new ManyPerson();
            ArrayList<ManyGroup> gPerson1 = new ArrayList<ManyGroup>();
            person1.setId(_person1Id);
            person1.setGroup(gPerson1);
            person1.setSthelse("Something else");
            person1.setHelloworld("Hello World!");
            person1.setValue1(JDO_ORIGINAL_VALUE);

            person2 = new ManyPerson();
            ArrayList<ManyGroup> gPerson2 = new ArrayList<ManyGroup>();
            person2.setId(_person2Id);
            person2.setGroup(gPerson2);
            person2.setSthelse("Something else");
            person2.setHelloworld("Hello World!");
            person2.setValue1(JDO_ORIGINAL_VALUE);

            person3 = new ManyPerson();
            ArrayList<ManyGroup> gPerson3 = new ArrayList<ManyGroup>();
            person3.setId(_person3Id);
            person3.setGroup(gPerson3);
            person3.setSthelse("Something else for person 3");
            person3.setHelloworld("Hello World!");
            person3.setValue1(JDO_ORIGINAL_VALUE);

            person4 = new ManyPerson();
            ArrayList<ManyGroup> gPerson4 = new ArrayList<ManyGroup>();
            person4.setId(_person4Id);
            person4.setGroup(gPerson4);
            person4.setSthelse("Something else for person 4");
            person4.setHelloworld("Hello World!");
            person4.setValue1(JDO_ORIGINAL_VALUE);

            //
            // create four groups, assign all persons to each group
            //
            groupA = new ManyGroup();
            groupA.setValue1(JDO_ORIGINAL_VALUE);
            al = new ArrayList<ManyPerson>();
            al.add(person1);
            al.add(person2);
            al.add(person3);
            al.add(person4);
            groupA.setId(_groupAId);
            groupA.setPeople(al);

            groupB = new ManyGroup();
            groupB.setValue1(JDO_ORIGINAL_VALUE);
            groupB.setId(_groupBId);
            bl = new ArrayList<ManyPerson>();
            bl.add(person1);
            bl.add(person2);
            bl.add(person3);
            bl.add(person4);
            groupB.setPeople(bl);

            groupC = new ManyGroup();
            groupC.setValue1(JDO_ORIGINAL_VALUE);
            c1 = new ArrayList<ManyPerson>();
            c1.add(person1);
            c1.add(person2);
            c1.add(person3);
            c1.add(person4);
            groupC.setId(_groupCId);
            groupC.setPeople(c1);

            groupD = new ManyGroup();
            groupD.setValue1(JDO_ORIGINAL_VALUE);
            d1 = new ArrayList<ManyPerson>();
            d1.add(person1);
            d1.add(person2);
            d1.add(person3);
            d1.add(person4);
            groupD.setId(_groupDId);
            groupD.setPeople(d1);

            //
            // assign all groups to each person
            //
            gPerson1.add(groupA);
            gPerson1.add(groupB);
            gPerson1.add(groupC);
            gPerson1.add(groupD);

            gPerson2.add(groupA);
            gPerson2.add(groupB);
            gPerson2.add(groupC);
            gPerson2.add(groupD);

            gPerson3.add(groupA);
            gPerson3.add(groupB);
            gPerson3.add(groupC);
            gPerson3.add(groupD);

            gPerson4.add(groupA);
            gPerson4.add(groupB);
            gPerson4.add(groupC);
            gPerson4.add(groupD);

            //
            // create persistent groups and persons
            //
            db.create(person1);
            db.create(person2);
            db.create(person3);
            db.create(person4);
            
            db.create(groupA);
            db.create(groupB);
            db.create(groupC);
            db.create(groupD);
            
            db.commit();
        } catch (Exception e) {
            LOG.warn("createTestDataSet: exception caught: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Update an object outside of JDO using a JDBC connection.
     *
     * @param groupId primary key of object to be updated
     */
    private void updateGroupUsingJDBC(final int groupId) {
        LOG.debug("updating group " + groupId + " using JDBC...");

        try {
            _updateGroupStatement.setString(1, JDBC_UPDATED_VALUE);
            _updateGroupStatement.setInt(2, groupId);
            int rc = _updateGroupStatement.executeUpdate();
            if (rc <= 0) {
                LOG.warn("updateGroupUsingJDBC: error updating group "
                        + groupId + ", return code = " + rc);
            }
        } catch (Exception e) {
            LOG.warn("updateGroupUsingJDBC: exception updating group "
                    + groupId + ": " + e.getMessage(), e);
        }
    }

    /**
     * Update an object outside of JDO using a JDBC connection.
     *
     * @param personId primary key of object to be updated
     */
    private void updatePersonUsingJDBC(final int personId) {
        LOG.debug("updating person " + personId + " using JDBC...");

        try {
            _updatePersonStatement.setString(1, JDBC_UPDATED_VALUE);
            _updatePersonStatement.setInt(2, personId);
            int rc = _updatePersonStatement.executeUpdate();
            if (rc <= 0) {
                LOG.warn("updatePersonUsingJDBC: error updating person "
                        + personId + ", return code = " + rc);
                return;
            }
        } catch (Exception e) {
            LOG.warn("updatePersonUsingJDBC: exception updating person "
                    + personId + ": " + e.getMessage(), e);
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
        LOG.debug("expiring cache...");

        Object[] identityArray = null;
        try {
            CacheManager cacheManager = _db.getCacheManager();
            if (byType) {
                Class<?>[] typeArray = new Class[2];
                typeArray[0] = ManyGroup.class;
                typeArray[1] = ManyPerson.class;
                cacheManager.expireCache(typeArray);
            } else {
                identityArray = new Object[4];
                identityArray[0] = new Integer(_groupAId);
                identityArray[1] = new Integer(_groupBId);
                identityArray[2] = new Integer(_groupCId);
                identityArray[3] = new Integer(_groupDId);
                cacheManager.expireCache(ManyGroup.class, identityArray);
            }
        } catch (Exception e) {
            LOG.warn("expireCache: exception encountered clearing cache: " + e.getMessage());
        }
    }

    /**
     * Read, or load, an object and validate that the field values
     * reflect values updated outside of JDO and not from previously
     * cached values.
     *
     * @param groupId primary key of object to be read
     */
    private boolean validReadTransaction(final int groupId,
            final String expectedValue, final boolean checkPeople) {
        LOG.debug("validating read transaction for group " + groupId + "...");
        Database db = null;
        boolean valid = true;
        try {
            db = getJDOManager(DBNAME, MAPPING).getDatabase();
            db.begin();
            ManyGroup group = db.load(ManyGroup.class, new Integer(groupId));
            if (group.getValue1().compareTo(expectedValue) != 0) {
                LOG.warn("validReadTransaction: value in group " + group.getId()
                        + " does not match expected value, value: " + group.getValue1()
                        + ", expected: " + expectedValue);
                valid = false;
            }
            if (checkPeople) {
                Iterator<ManyPerson> itor = group.getPeople().iterator();
                while (itor.hasNext()) {
                    ManyPerson person = itor.next();
                    if (person.getValue1().compareTo(expectedValue) != 0) {
                        LOG.warn("validReadTransaction: value in person " + person.getId()
                                + " does not match expected value, value: "
                                + person.getValue1() + ", expected: " + expectedValue);
                        valid = false;
                    }
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
            LOG.warn("validReadTransaction: exception caught while validating read for group "
                    + groupId + " : " + e.getMessage(), e);
            valid = false;
        }
        return valid;
    }

    /**
     * Modify fields of an object and validate that the transaction completes
     * successfully.
     *
     * @param groupId primary key of object to be updated
     */
    private boolean validWriteTransaction(final int groupId) {
        LOG.debug("validating write transaction for group " + groupId + "...");
        Database db = null;
        try {
            db = getJDOManager(DBNAME, MAPPING).getDatabase();
            db.begin();
            ManyGroup group = db.load(ManyGroup.class, new Integer(groupId));
            group.setValue1(JDO_UPDATED_VALUE);
            Iterator<ManyPerson> itor = group.getPeople().iterator();
            while (itor.hasNext()) {
                ManyPerson person = itor.next();
                person.setValue1(JDO_UPDATED_VALUE);
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
            LOG.warn("validWriteTransaction: exception caught while validating group write "
                    + groupId + " : " + e.getMessage(), e);
            return false;
        }
        return true;
    }

    /**
     * Delete all objects in test data set.
     */
    private void deleteTestDataSet() {
        LOG.debug("deleting test data set...");
        QueryResults enumeration;
        ManyGroup group = null;
        ManyPerson person = null;
        Database db = null;
        try {
            // select an group and delete it, if it exist!
            db = getJDOManager(DBNAME, MAPPING).getDatabase();
            db.begin();
            OQLQuery oqlclean = db.getOQLQuery(
                    "SELECT object FROM " + ManyGroup.class.getName()
                    + " object WHERE object.id < $1");
            oqlclean.bind(Integer.MAX_VALUE);
            enumeration = oqlclean.execute();
            while (enumeration.hasMore()) {
                group = (ManyGroup) enumeration.next();
                LOG.debug("Retrieved object: " + group);
                db.remove(group);
                LOG.debug("Deleted object: " + group);
            }
            db.commit();

            db.begin();
            oqlclean = db.getOQLQuery(
                    "SELECT object FROM " + ManyPerson.class.getName()
                    + " object WHERE object.id < $1");
            oqlclean.bind(Integer.MAX_VALUE);
            enumeration = oqlclean.execute();
            while (enumeration.hasMore()) {
                person = (ManyPerson) enumeration.next();
                LOG.debug("Retrieved object: " + person);
                db.remove(person);
                LOG.debug("Deleted object: " + person);
            } 
            db.commit();
        } catch (Exception e) {
            if (db != null) {
                try {
                    db.close();
                } catch (Exception se) {
                    LOG.warn("Problem closing Database instance.", se);
                }
            }
            LOG.warn("deleteTestDataSet: exception caught: " + e.getMessage(), e);
        }
    }
}