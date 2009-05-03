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
package org.castor.cpa.test.test13;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.castor.cpa.test.framework.CPATestCase;
import org.castor.cpa.test.framework.xml.types.DatabaseEngineType;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.PersistenceException;

/**
 * Test for serializable depedent object
 */
public final class TestSerializable extends CPATestCase {
    private static final Log LOG = LogFactory.getLog(TestSerializable.class);
    
    private static final String DBNAME = "test13";
    private static final String MAPPING = "/org/castor/cpa/test/test13/mapping.xml";
    private Database       _db;

    public TestSerializable(final String name) {
        super(name);
    }

    // Test are only included/excluded for engines that have been tested with this test suite.

    public boolean include(final DatabaseEngineType engine) {
        return (engine == DatabaseEngineType.MYSQL)
            || (engine == DatabaseEngineType.DERBY);
    }
    
    public void setUp() throws Exception {
        _db = getJDOManager(DBNAME, MAPPING).getDatabase();
    }

    public void tearDown() throws PersistenceException, SQLException {
        if (_db.isActive()) { _db.rollback(); }
        _db.close();
    }

    public void testSerializable() throws Exception {
        // delete everything
        _db.begin();
        Connection conn = _db.getJdbcConnection();
        conn.createStatement().executeUpdate("DELETE FROM test13_serial");
        _db.commit();

        // create new object with an serializable dependent object
        _db.begin();
        SerializableReferer master = new SerializableReferer();
        master.setId(1);
        master.setSerializableObject(new SerializableObject());
        master.getSerializableObject().setString("Very cool!");
        master.getSerializableObject().setInts(new int[] {1, 3, 5, 7, 9});
        _db.create(master);
        _db.commit();

        SerializableReferer testSerial;
        SerializableObject test;
        
        // test if object created properly
        _db.begin();
        testSerial = (SerializableReferer) _db.load(SerializableReferer.class,
                                                    new Integer(1));
        if (testSerial == null) {
            LOG.error("Object creation failed!");
            fail("Object creation failed!");
        }
        
        test = testSerial.getSerializableObject();
        if ((test == null) || !test.getString().equals("Very cool!")
                || (test.getInts() == null) || (test.getInts().length != 5)
                || (test.getInts()[0] != 1) || (test.getInts()[1] != 3)
                || (test.getInts()[2] != 5) || (test.getInts()[3] != 7)
                || (test.getInts()[4] != 9)) {
            
            LOG.error("dependent objects creation failed!" + testSerial);
            fail("dependent objects creation failed!" + testSerial);
        }

        // modify the object
        int[] ints = test.getInts();
        ints[1] = 103;
        ints[3] = 107;
        test.setInts(ints);
        test.setString("Very very cool!");
        _db.commit();

        _db.begin();
        testSerial = (SerializableReferer) _db.load(SerializableReferer.class,
                                                    new Integer(1));
        if (testSerial == null) {
            LOG.error("dependent modification failed!" + testSerial);
            fail("dependent modification failed!" + testSerial);
        }
        
        test = testSerial.getSerializableObject();
        if ((test == null) || !test.getString().equals("Very very cool!")
                || (test.getInts() == null) || (test.getInts().length != 5)
                || (test.getInts()[0] != 1) || (test.getInts()[1] != 103)
                || (test.getInts()[2] != 5) || (test.getInts()[3] != 107)
                || (test.getInts()[4] != 9)) {
            
            LOG.error("dependent modification failed!" + testSerial);
            fail("dependent modification failed!" + testSerial);
        }

        // set the field to null;
        test.setInts(null);
        test.setString(null);
        _db.commit();

        _db.begin();
        testSerial = (SerializableReferer) _db.load(SerializableReferer.class,
                                                    new Integer(1));
        if (testSerial == null) {
            LOG.error("dependent modfiication failed!" + testSerial);
            fail("dependent modfiication failed!" + testSerial);
        }
            
        test = testSerial.getSerializableObject();
        if ((test == null) || (test.getString() != null)
                || (test.getInts() != null)) {
            
            LOG.error("dependent modification failed!" + testSerial);
            fail("dependent modification failed!" + testSerial);
        }

        // setSerializableObject( null );
        testSerial.setSerializableObject(null);
        _db.commit();

        _db.begin();
        testSerial = (SerializableReferer) _db.load(SerializableReferer.class,
                                                    new Integer(1));
        if ((testSerial == null)
                || (testSerial.getSerializableObject() != null)) {
            
            LOG.error("dependent modfiication failed!" + testSerial);
            fail("dependent modfiication failed!" + testSerial);
        }
        _db.commit();
    }
}
