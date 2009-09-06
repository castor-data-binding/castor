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
 */
package ctf.jdo.tc3x;

import java.sql.Connection;
import java.sql.SQLException;

import harness.CastorTestCase;
import harness.TestHarness;
import jdo.JDOCategory;

import org.castor.jdo.util.JDOUtils;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.QueryResults;

public class TestSize extends CastorTestCase {
    private Database       _db;

    private JDOCategory    _category;

    /**
     * Constructor
     *
     * @param category The test suite of these tests
     */
    public TestSize(final TestHarness category) {
        super(category, "TC35", "Size");
        _category = (JDOCategory) category;
    }

    public TestSize(final TestHarness category, final String name,
            final String description) {
        
        super(category, name, description);
        _category = (JDOCategory) category;
    }
    
    public final Database getDatabase() { return _db; }

    public final void runTest() throws PersistenceException {
        removeRecords();
        createRecords();
        testSizeA();
        testSizeB();
        testSizeC();
        testSizeD();
    }

    public final void setUp() throws PersistenceException {
        _db = _category.getDatabase();
    }
    
    public final void removeRecords() throws PersistenceException {
        Connection conn = null;
        try {
            conn = _category.getJDBCConnection();
            conn.setAutoCommit(false);
            conn.createStatement().executeUpdate("DELETE FROM tc3x_entity");
            conn.commit();
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            JDOUtils.closeConnection (conn);
        }
    }
    
    public final void createRecords() throws PersistenceException {
        _db.begin();
        for (int i = 0; i < 25; i++) {
            Entity newTRN = new Entity();
            newTRN.setId(i);
            _db.create(newTRN);
        }
        _db.commit();       
    }

    /**
     * Very simple test to do a query and call size()
     */
    public final void testSizeA() throws PersistenceException {
        _db.begin();
        OQLQuery oqlquery = _db.getOQLQuery(
                "SELECT object FROM " + Entity.class.getName() + " object");
        QueryResults enumeration = oqlquery.execute(true);
        assertTrue("size should be > 0", enumeration.size() > 0);
        _db.commit();
    }

    /**
     * Test going through enumeration and calling size. This tests the 
     * implemention because it internally moves the cursor and then 
     * moves it back.
     */
    public final void testSizeB() throws PersistenceException {
        _db.begin();
        OQLQuery oqlquery = _db.getOQLQuery(
                "SELECT object FROM " + Entity.class.getName() + " object");
        QueryResults enumeration = oqlquery.execute(true);
        while (enumeration.hasMore()) {
            enumeration.next();
            assertTrue("size should be > 0", enumeration.size() > 0);
            assertEquals("size should be ==25", enumeration.size(), 25);
        }
        _db.commit();
    }
    
    /**
     * Does size return the right results?
     */
    public final void testSizeC() throws PersistenceException {
        _db.begin();
        OQLQuery oqlquery = _db.getOQLQuery(
                "SELECT object FROM " + Entity.class.getName() + " object");
        QueryResults enumeration = oqlquery.execute(true);
        int expectedSize = enumeration.size();
        int realSize = 0;
        while (enumeration.hasMore()) {
            enumeration.next();
            realSize++;
        }
        _db.commit();
        assertEquals("realsize didn't equal expectedsize", realSize, expectedSize);
    }

    /**
     * Should fail with a non scrollable resultset.
     */
    public void testSizeD() {
        try {
            _db.begin();
            OQLQuery oqlquery = _db.getOQLQuery(
                    "SELECT object FROM " + Entity.class.getName() + " object");
            oqlquery.execute(false);
            _db.commit();
            // This test fails when executed against PostgreSQL. 
            fail ("Calling size() on a non-scrollable ResultSet should fail.");
        } catch (Exception ex) {
            assertTrue(true);
        }
    }    
    
    public final void tearDown() throws PersistenceException {
        if (_db.isActive()) { _db.rollback(); }
        _db.close();
    }
}
