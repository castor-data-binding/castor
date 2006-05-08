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


package ctf.jdo.tc7x;

import harness.CastorTestCase;
import harness.TestHarness;

import java.sql.Connection;

import jdo.JDOCategory;

import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.PersistenceException;

public class TestGetJdbcConnection extends CastorTestCase {
    private JDOCategory    _category;

    private Database       _db;


    /**
     * Constructor
     *
     * @param category The test suite for these tests
     */
    public TestGetJdbcConnection( TestHarness category ) 
    {
        super( category, "TC78", "JDBC connection" );
        _category = (JDOCategory) category;
    }

    /**
     * Test setup, including getting a JDO Database
     * @see junit.framework.TestCase#setUp()
     */
    public void setUp() throws PersistenceException
    {
        _db = _category.getDatabase();
    }

    
    public void runTest() throws Exception 
    {
        testGetJdbcConnection();
        testGetJdbcConnectionWithoutActiveTransaction();
    }
    
    private void testGetJdbcConnection() throws Exception {
        _db.begin();
        Connection connection = _db.getJdbcConnection();
        assertNotNull(connection);
        _db.commit();
        _db.close();
        
    }

    private void testGetJdbcConnectionWithoutActiveTransaction() throws Exception {
        try {
            _db.getJdbcConnection();
            fail("Should have received PersistenceException (no active transaction)");
        } catch (PersistenceException e) {
            assertEquals ("No transaction in progress for the current thread. Please start a transaction before trying to obtain the JDBC connection", e.getMessage());
        }
        finally {
            if (_db.isActive()) {
                _db.rollback();
            }
            if (_db.isActive()) {
            _db.close();
            }
        }
    }

}
