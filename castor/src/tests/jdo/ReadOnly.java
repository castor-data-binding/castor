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
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.ObjectModifiedException;

import junit.framework.TestSuite;
import junit.framework.TestCase;
import junit.framework.Assert;
import harness.TestHarness;
import harness.CastorTestCase;

/**
 * Tests that modification to read only objects are not persist in the 
 * database.
 */
public class ReadOnly extends CastorTestCase {

    /**
     * The test suite that this test case belongs
     */
    private JDOCategory    _category;

    /**
     * The JDO Database
     */
    private Database       _db;

    static final String    NewValue = "new value";

    /**
     * Constructor
     *
     * @param category the test suite that this test case belongs
     */
    public ReadOnly( TestHarness category ) {
        super( category, "TC05", "Read only tests" );
        _category = (JDOCategory) category;
    }

    /**
     * Get a jdo.Database and create a test object for readOnly test
     */
    public void setUp() 
            throws PersistenceException {

        OQLQuery      oql;
        TestObject    object;
        Enumeration   enumeration;

        // Open transaction in order to perform JDO operations
        _db = _category.getDatabase();
        _db.begin();
    
        // Determine if test object exists, if not create it.
        // If it exists, set the name to some predefined value
        // that this test will later override.
        oql = _db.getOQLQuery( "SELECT object FROM jdo.TestObject object WHERE id = $1" );
        oql.bind( TestObject.DefaultId );
        enumeration = oql.execute();
        if ( enumeration.hasMoreElements() ) {
            object = (TestObject) enumeration.nextElement();
            object.setValue1( TestObject.DefaultValue1 );
            object.setValue2( TestObject.DefaultValue2 );
        } else {
            object = new TestObject();
            stream.println( "Creating new object: " + object );
            _db.create( object );
        } 
        _db.commit();
    }

    /**
     * Tests that modification to read only objects are not persist in the 
     * database.
     */
    public void runTest() 
            throws PersistenceException {

        OQLQuery      oql;
        TestObject    object;
        Enumeration   enumeration;

        // load an object using readOnly mode
        _db.begin();
        oql = _db.getOQLQuery( "SELECT object FROM jdo.TestObject object WHERE id = $1" );
        oql.bind( TestObject.DefaultId );
        enumeration = oql.execute( Database.ReadOnly );
        object = (TestObject) enumeration.nextElement();
        stream.println( "Retrieved object: " + object );
        object.setValue1( NewValue );
        stream.println( "Modified object: " + object );
        _db.commit();
        
        // read the object from another transaction to see
        // if changes is not persisted.
        _db.begin();
        oql.bind( TestObject.DefaultId );
        enumeration = oql.execute( Database.ReadOnly );
        object = (TestObject) enumeration.nextElement();
        stream.println( "Retrieved object: " + object );
        if ( object.getValue1().equals( NewValue ) ) {
            stream.println( "Error: modified object was stored" );
            fail("Modified object was stored");
        } else
            stream.println( "OK: object is read-only" );
        _db.commit();

    }

    /**
     * Close the database used in this test
     */
    public void tearDown()
            throws PersistenceException {
        if ( _db.isActive() ) _db.rollback();
        _db.close();
    }

}

