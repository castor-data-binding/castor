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

import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.PersistenceException;

import harness.TestHarness;
import harness.CastorTestCase;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class CircularRef extends CastorTestCase 
{
    private JDOCategory    _category;
    private Database       _db;
    private Connection     _conn;

    /**
     * Constructor
     *
     * @param category The test suite for these tests
     */
    public CircularRef(TestHarness category) 
    {
        super(category, "tempTC32", "Circular References using key-gen");
        _category = (JDOCategory) category;
    }

    /**
     * Get a JDO database
     */
    public void setUp() 
        throws PersistenceException, SQLException
    {
        _db = _category.getDatabase();
        _conn = _category.getJDBCConnection();
        _conn.setAutoCommit(false);

        stream.println("Delete everything");
        Statement stmt = _conn.createStatement();
        stmt.executeUpdate("delete from circ_brother");
        stmt.executeUpdate("delete from circ_sister");
        _conn.commit();

    }

    public void runTest() 
        throws PersistenceException, SQLException
    {
        _db.begin();

        stream.println("Build brother and sister (circurlarly referring) objects");

        // no ids needed, they come from the key-gen
        Brother brother = new Brother();
        Sister sister = new Sister();
        brother.setSister(sister);
        sister.setBrother(brother);
        stream.println("Create object tree in db");

        try
        {
            _db.create(brother);
            _db.create(sister);
            _db.commit();            
        }
        catch (Exception e) 
        {
            e.printStackTrace();
            fail("Unable to create objects with circular reference " + e);
        }

    }

    public void tearDown() throws PersistenceException 
    {
        if (_db.isActive()) _db.rollback();
        _db.close();
    }
}
