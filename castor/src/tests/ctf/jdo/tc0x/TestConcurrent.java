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
 * $Id$
 */

package ctf.jdo.tc0x;

import java.sql.Connection;
import java.sql.SQLException;

import harness.CastorTestCase;
import harness.TestHarness;

import jdo.JDOCategory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.ObjectModifiedException;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.QueryResults;
import org.exolab.castor.mapping.AccessMode;

/**
 * Concurrent access test. Tests a JDO modification and concurrent
 * JDBC modification to determine if JDO can detect the modification
 * with dirty checking.
 */
public final class TestConcurrent extends CastorTestCase {
    /**
     * The <a href="http://jakarta.apache.org/commons/logging/">Jakarta
     * Commons Logging</a> instance used for all logging.
     */
    private static final Log LOG = LogFactory.getLog(TestConcurrent.class);
    
    private static final String    JDBC_VALUE = "jdbc value";

    private static final String    JDO_VALUE = "jdo value";

    private JDOCategory    _category;

    private Database       _db;

    private Connection     _conn;

    /** 
     * Constructor
     */
    public TestConcurrent(final TestHarness category) {
        super(category, "TC02", "Concurrent access tests");
        _category = (JDOCategory) category;
    }

    /**
     * Initializes fields
     */
    public void setUp() throws PersistenceException, SQLException {
        _db = _category.getDatabase();
        _conn = _category.getJDBCConnection(); 
    }

    /**
     * Calls the individual tests embedded in this test case
     */
    public void runTest() throws PersistenceException, SQLException {
        testAccessModeShared();
        testAccessModeExclusive();
        testAccessModeDbLocked();
    }

    /**
     * Close the database and JDBC connection
     */
    public void tearDown() throws PersistenceException, SQLException {
        if (_db.isActive()) { _db.rollback(); }
        _db.close();
    }

    /**
     * Test for concurrent modification detection in Shared Mode.
     * (Optimistic Locking Mode)
     * This test contains two parts. The first part tests if Castor 
     * JDO can detect concurrent modification done directly via
     * JDBC in Shared Mode. The second part test if Castor can
     * ignores concurrent modification done to fields that 
     * indicates dirty check should not be done.
     */
    public void testAccessModeShared()
    throws PersistenceException, SQLException {
        LOG.info("Running in access mode shared");
        
        // part 1
        testDirtyChecked(Database.Shared);

        // part 2
        testDirtyIgnored(Database.Shared);
    }

    /**
     * Test for concurrent modification detection in Exclusive Mode.
     * (Pessimistic Locking Mode)
     * This test contains two parts. The first part tests if Castor 
     * JDO can detect concurrent modification done directly via
     * JDBC in Shared Mode. The second part tests if Castor can
     * ignore concurrent modification done to fields that 
     * are indicated dirty check should not be done.
     */
    public void testAccessModeExclusive()
    throws PersistenceException, SQLException {
        LOG.info("Running in access mode exclusive");

        // part 1
        testDirtyChecked(Database.Exclusive);

        // part 2
        testDirtyIgnored(Database.Exclusive);
    }

    /**
     * Test for concurrent modification detection in DbLocked Mode.
     * (Pessimistic Locking Mode plus database row lock)
     * This test contains two parts. The first part tests if Castor 
     * JDO can detect concurrent modification done directly via
     * JDBC in Shared Mode. The second part tests if Castor can
     * ignores concurrent modification done to fields that are
     * indicated dirty check should not be done.
     * (note: some databases don't support database lock and will
     * fails this test case)
     */
    public void testAccessModeDbLocked()
    throws PersistenceException, SQLException {
        LOG.info("Running in access mode db-locked");

        // part 1
        testDirtyChecked(Database.DbLocked);

        // part 2
        testDirtyIgnored(Database.DbLocked);
    }

    /**
     * This method is called by the tests and preform the actual
     * concurrent modification test.
     *
     * @param accessMode the access mode that is used in the concurrent
     *        modification tests
     */
    private void testDirtyChecked(final AccessMode accessMode)
    throws PersistenceException, SQLException {
        OQLQuery       oql;
        Sample         object;
        QueryResults   enumeration;

        // Open transaction in order to perform JDO operations
        _db.begin();
    
        // Determine if test object exists, if not create it.
        // If it exists, set the name to some predefined value
        // that this test will later override.
        oql = _db.getOQLQuery("SELECT object FROM "
                + Sample.class.getName() + " object WHERE id = $1");
        oql.bind(Sample.DEFAULT_ID);

        enumeration = oql.execute();
        if (enumeration.hasMore()) {
            object = (Sample) enumeration.next();
            LOG.debug("Retrieved object: " + object);
            object.setValue1(Sample.DEFAULT_VALUE_1);
            object.setValue2(Sample.DEFAULT_VALUE_2);
        } else {
            object = new Sample();
            LOG.debug("Creating new object: " + object);
            _db.create(object);
        }

        _db.commit();
        
        // Open a new transaction in order to conduct test
        _db.begin();
        
        oql.bind(new Integer(Sample.DEFAULT_ID));
        object = (Sample) oql.execute(accessMode).nextElement();
        object.setValue1(JDO_VALUE);
        
        // Perform direct JDBC access and override the value of that table
        if (accessMode != Database.DbLocked) {
            _conn.createStatement().execute(
                    "UPDATE tc0x_sample SET value1='" + JDBC_VALUE
                    + "' WHERE id=" + Sample.DEFAULT_ID);
            LOG.debug("OK: Updated object from JDBC");
        } else {
            Thread th = new Thread() {
                public void run() {
                    Connection conn = null;
                    try {
                        conn = _category.getJDBCConnection(); 
                        conn.createStatement().execute(
                                "UPDATE tc0x_sample SET value1='" + JDBC_VALUE
                                + "' WHERE id=" + Sample.DEFAULT_ID);
                        conn.close();
                    } catch (Exception ex) {
                    } finally {
                        try {
                            if (conn != null) { conn.close(); }
                        } catch (Exception ex) {
                        }
                    }
                }
            };
            th.start();
            synchronized (this) {
                try {
                    wait(5000);
                    if (th.isAlive()) {
                        th.interrupt();
                        LOG.debug("OK: Cannot update object from JDBC");
                    } else {
                        LOG.error("Error: Updated object from JDBC");
                        fail("Updated test object from JDBC");
                    }
                } catch (InterruptedException ex) {
                }
            }
        }

        // Commit JDO transaction, this should report object modified exception
        LOG.debug("Committing JDO update: dirty checking field modified");
        if (accessMode != Database.DbLocked) {
            try {
                _db.commit();
                LOG.error("Error: ObjectModifiedException not thrown");
                fail("ObjectModifiedException not thrown");
            } catch (ObjectModifiedException ex) {
                LOG.debug("OK: ObjectModifiedException thrown");
            }
        } else {
            try {
                _db.commit();
                LOG.debug("OK: ObjectModifiedException not thrown");
                // After _db.commit the concurrent update will be performed.
                // and we need to undo it.
                _conn.createStatement().execute(
                        "UPDATE tc0x_sample SET value1='" + JDO_VALUE
                        + "' WHERE id=" + Sample.DEFAULT_ID);
            } catch (ObjectModifiedException ex) {
                _db.rollback();
                LOG.error("Error: ObjectModifiedException thrown");
                fail("ObjectModifiedException not thrown");
            }
        }
    }

    /**
     * This method is called by the tests and preform the actual
     * concurrent modification test.
     *
     * @param accessMode the access mode that is used in the concurrent
     *        modification tests
     */
    private void testDirtyIgnored(final AccessMode accessMode)
    throws PersistenceException, SQLException {
        OQLQuery      oql;
        Sample    object;
        QueryResults   enumeration;

        // Open transaction in order to perform JDO operations
        _db.begin();
    
        // Determine if test object exists, if not create it.
        // If it exists, set the name to some predefined value
        // that this test will later override.
        oql = _db.getOQLQuery("SELECT object FROM "
                + Sample.class.getName() + " object WHERE id = $1");
        oql.bind(Sample.DEFAULT_ID);
        
        enumeration = oql.execute();
        if (enumeration.hasMore()) {
            object = (Sample) enumeration.next();
            LOG.debug("Retrieved object: " + object);
            object.setValue1(Sample.DEFAULT_VALUE_1);
            object.setValue2(Sample.DEFAULT_VALUE_2);
        } else {
            object = new Sample();
            LOG.debug("Creating new object: " + object);
            _db.create(object);
        }
        
        _db.commit();

        // Open a new transaction in order to conduct test
        _db.begin();
        
        oql.bind(new Integer(Sample.DEFAULT_ID));
        object = (Sample) oql.execute(accessMode).nextElement();
        object.setValue2(JDO_VALUE);
        
        // Perform direct JDBC access and override the value of that table
        if (accessMode != Database.DbLocked) {
            _conn.createStatement().execute(
                    "UPDATE tc0x_sample SET value2='" + JDBC_VALUE
                    + "' WHERE id=" + Sample.DEFAULT_ID);
            LOG.debug("Updated test object from JDBC");
        }
    
        // Commit JDO transaction, this should report object modified exception
        LOG.debug("Commit update: no dirty checking field not modified");
        try {
            _db.commit();
            LOG.debug("OK: ObjectModifiedException not thrown");
        } catch (ObjectModifiedException ex) {
            if (_db.isActive()) { _db.rollback(); }
            LOG.error("Error: ObjectModifiedException thrown", ex);
            fail("ObjectModifiedException thrown");
        }
    }
}
