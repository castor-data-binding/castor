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

package ctf.jdo.tc1x;

import harness.CastorTestCase;
import harness.TestHarness;

import jdo.JDOCategory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.QueryResults;

/**
 * Test for nested-field support in Castor JDO. A nested-field
 * is java field that maps to multiple database fields.
 */
public final class TestNestedFields extends CastorTestCase {
    /**
     * The <a href="http://jakarta.apache.org/commons/logging/">Jakarta
     * Commons Logging</a> instance used for all logging.
     */
    private static final Log LOG = LogFactory.getLog(TestNestedFields.class);
    
    private JDOCategory    _category;

    private Database       _db;

    public TestNestedFields(final TestHarness category) {
        super(category, "TC16", "Nested fields tests");
        _category = (JDOCategory) category;
    }

    public void setUp() throws PersistenceException {
        _db = _category.getDatabase();
    }

    public void runTest() throws PersistenceException {
        OQLQuery         oql;
        QueryResults     results;
        NestedObject     t;

        // Open transaction in order to perform JDO operations
        _db.begin();
        oql = _db.getOQLQuery("SELECT t FROM "
                + NestedObject.class.getName() + " t WHERE id = $1");
        oql.bind(NestedObject.DEFAULT_ID);
        results = oql.execute();
        if (results.hasMore()) {
            t = (NestedObject) results.next();
            _db.remove(t);
            LOG.debug("Deleting object: " + t);
        }
        _db.commit();
        
        _db.begin();
        t = new NestedObject();
        LOG.debug("Creating new object: " + t);
        _db.create(t);
        _db.commit();
        
        _db.begin();
        oql.bind(NestedObject.DEFAULT_ID);
        results = oql.execute();
        if (results.hasMore()) {
            t = (NestedObject) results.next();
            String nv1 = t.getNested1().getValue1();
            String nv2 = t.getNested2().getNested3().getValue2();
            if (NestedObject.DEFAULT_VALUE1.equals(nv1)
                    && NestedObject.DEFAULT_VALUE2.equals(nv2)) {
                
                LOG.debug("OK: Created object: " + t);
            } else {
                LOG.error("Creating object: " + t);
                fail("Creating object failed");
            }
        }
        _db.commit();
        oql.close();

        LOG.info("Testing nested fields in OQLQuery...");
        _db.begin();
        oql = _db.getOQLQuery("SELECT t FROM "
                + NestedObject.class.getName()
                + " t WHERE nested2.nested3.value2 = $1");
        oql.bind(NestedObject.DEFAULT_VALUE2);
        results = oql.execute();
        if (results.hasMore()) {
            LOG.info("OK: Nested fields persisted");
        } else {
            LOG.error("Failed to persist nested fields");
            fail("Failed to persist nested fields");
        }
        oql.close();
        _db.commit();
    }

    public void tearDown() throws PersistenceException {
        if (_db.isActive()) { _db.rollback(); }
        _db.close();
    }
}
