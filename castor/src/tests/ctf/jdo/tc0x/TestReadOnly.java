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

package ctf.jdo.tc0x;

import java.util.Enumeration;

import harness.CastorTestCase;
import harness.TestHarness;

import jdo.JDOCategory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.PersistenceException;

/**
 * Tests that modification to read only objects are not persist in the 
 * database.
 */
public final class TestReadOnly extends CastorTestCase {
    /**
     * The <a href="http://jakarta.apache.org/commons/logging/">Jakarta
     * Commons Logging</a> instance used for all logging.
     */
    private static final Log LOG = LogFactory.getLog(TestReadOnly.class);
    
    private static final String    NEW_VALUE = "new value";

    private JDOCategory    _category;

    private Database       _db;

    /**
     * Constructor
     *
     * @param category the test suite that this test case belongs
     */
    public TestReadOnly(final TestHarness category) {
        super(category, "TC03", "Read only tests");
        _category = (JDOCategory) category;
    }

    /**
     * Get a jdo.Database and create a test object for readOnly test
     */
    public void setUp() throws PersistenceException {
        // Open transaction in order to perform JDO operations
        _db = _category.getDatabase();
        _db.begin();
    
        // Determine if test object exists, if not create it.
        // If it exists, set the name to some predefined value
        // that this test will later override.
        OQLQuery oql = _db.getOQLQuery("SELECT object FROM "
                + Sample.class.getName() + " object WHERE id = $1");
        oql.bind(Sample.DEFAULT_ID);

        Enumeration enumeration = oql.execute();
        Sample    object;
        if (enumeration.hasMoreElements()) {
            object = (Sample) enumeration.nextElement();
            object.setValue1(Sample.DEFAULT_VALUE_1);
            object.setValue2(Sample.DEFAULT_VALUE_2);
        } else {
            object = new Sample();
            LOG.debug("Creating new object: " + object);
            _db.create(object);
        } 
        
        _db.commit();
    }

    /**
     * Tests that modification to read only objects are not persist in the 
     * database.
     */
    public void runTest() throws PersistenceException {
        OQLQuery      oql;
        Sample    object;
        Enumeration   enumeration;

        // load an object using readOnly mode
        _db.begin();
        
        oql = _db.getOQLQuery("SELECT object FROM "
                + Sample.class.getName() + " object WHERE id = $1");
        oql.bind(Sample.DEFAULT_ID);
        
        enumeration = oql.execute(Database.ReadOnly);
        object = (Sample) enumeration.nextElement();
        LOG.debug("Retrieved object: " + object);
        object.setValue1(NEW_VALUE);
        LOG.debug("Modified object: " + object);
        
        _db.commit();
        
        // read the object from another transaction to see
        // if changes is not persisted.
        _db.begin();
        
        oql.bind(Sample.DEFAULT_ID);
        enumeration = oql.execute(Database.ReadOnly);
        object = (Sample) enumeration.nextElement();
        LOG.debug("Retrieved object: " + object);
        if (object.getValue1().equals(NEW_VALUE)) {
            LOG.error("Error: modified object was stored");
            fail("Modified object was stored");
        } else {
            LOG.debug("OK: object is read-only");
        }
        _db.commit();
    }

    /**
     * Close the database used in this test
     */
    public void tearDown() throws PersistenceException {
        if (_db.isActive()) { _db.rollback(); }
        _db.close();
    }
}
