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
package ctf.jdo.tc1x;

import java.sql.SQLException;

import harness.CastorTestCase;
import harness.TestHarness;

import jdo.JDOCategory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.QueryResults;

public final class TestTimeStamp extends CastorTestCase {
    /**
     * The <a href="http://jakarta.apache.org/commons/logging/">Jakarta
     * Commons Logging</a> instance used for all logging.
     */
    private static final Log LOG = LogFactory.getLog(TestTimeStamp.class);
    
    private JDOCategory    _category;

    private Database       _db;

    /**
     * Constructor
     *
     * @param category The test suite for these tests
     */
    public TestTimeStamp(final TestHarness category) {
        super(category, "TC17", "Timestamp tests");
        _category = (JDOCategory) category;
    }

    /**
     * Get a JDO database
     */
    public void setUp() throws PersistenceException, SQLException { }

    public void runTest() throws PersistenceException, SQLException {
        OQLQuery              oql;
        TimeStampableObject   object;
        QueryResults          enumeration;

        // Remove and create the object
        _db = _category.getDatabase();
        _db.begin();
        oql = _db.getOQLQuery("select obj from "
                + TimeStampableObject.class.getName() + " obj");
        enumeration = oql.execute();
        if (enumeration.hasMore()) {
            object = (TimeStampableObject) enumeration.next();
            LOG.debug("Removing object: " + object);
            _db.remove(object);
        } 
        _db.commit();
        oql.close();
        _db.close();
        
        _db = _category.getDatabase();
        _db.begin();
        object = new TimeStampableObject();
        LOG.debug("Creating new object: " + object);
        _db.create(object);
        _db.commit();
        _db.close();

        LOG.debug("Object timestamp: " + object.jdoGetTimeStamp());


        // Load the object
        _db = _category.getDatabase();
        _db.begin();
        oql = _db.getOQLQuery("select obj from "
                + TimeStampableObject.class.getName() + " obj");
        enumeration = oql.execute();

        if (enumeration.hasMore()) {
            object = (TimeStampableObject) enumeration.next();
            LOG.debug("Loaded object: " + object);
        }
        _db.rollback();
        oql.close();
        _db.close();

        // Change an attribute on the TestObject2
        object.setValue2("changed value");
        LOG.debug("Changed object: " + object);

        // Update the object
        _db = _category.getDatabase();
        _db.begin();
        LOG.debug("Updating object: " + object);
        _db.update(object);
        _db.commit();
        _db.close();

        LOG.debug("Object timestamp: " + object.jdoGetTimeStamp());
    }

    public void tearDown() throws PersistenceException { }
}
