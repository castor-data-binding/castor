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
 * Copyright 2000 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id$
 */

package ctf.jdo.tc1x;

import java.sql.Connection;
import java.sql.SQLException;

import harness.TestHarness;
import harness.CastorTestCase;

import jdo.JDOCategory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.PersistenceException;


/**
 * Test for serializable depedent object
 */
public final class TestSerializable extends CastorTestCase {
    /**
     * The <a href="http://jakarta.apache.org/commons/logging/">Jakarta
     * Commons Logging</a> instance used for all logging.
     */
    private static final Log LOG = LogFactory.getLog(TestSerializable.class);
    
    private Database       _db;

    private Connection     _conn;

    private JDOCategory    _category;

    public TestSerializable(final TestHarness category) {
        super(category, "TC13", "Serializable object tests");
        _category = (JDOCategory) category;
    }

    public void setUp() throws PersistenceException, SQLException {
        _db = _category.getDatabase();
        _conn = _category.getJDBCConnection(); 
    }

    public void runTest() throws PersistenceException, SQLException {
        // delete everything
        _conn.createStatement().executeUpdate("DELETE FROM tc1x_serial");

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

    public void tearDown() throws PersistenceException, SQLException {
        if (_db.isActive()) { _db.rollback(); }
        _db.close();
        _conn.close();
    }
}
