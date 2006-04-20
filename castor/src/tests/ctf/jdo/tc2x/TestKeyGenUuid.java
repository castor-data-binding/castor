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

package ctf.jdo.tc2x;

import harness.TestHarness;
import harness.CastorTestCase;

import jdo.JDOCategory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.QueryResults;

/**
 * Test for generic key generators (UUID).
 */
public final class TestKeyGenUuid extends CastorTestCase {
    private static final Log LOG = LogFactory.getLog(TestKeyGenUuid.class);

    private JDOCategory _category;
    private Database _db;

    public TestKeyGenUuid(final TestHarness category) {
        this("TC24", "Key generator: UUID", category);
    }

    public TestKeyGenUuid(final String name, final String description, 
            final TestHarness category) {
        super(category, name, description);
        _category = (JDOCategory) category;
    }

    public void setUp() throws Exception {
        _db = _category.getDatabase();
    }

    public void runTest() throws Exception {
        testOneKeyGen(UuidObject.class, UuidExtends.class);
    }

    public void tearDown() throws Exception {
        if (_db.isActive()) { _db.rollback(); }
        _db.close();
    }

    /**
     * The main goal of the test is to verify key generators in the case
     * of "extends" relation between two classes.
     * For each key generator we have a pair of classes: TestXXXObject and
     * TestXXXExtends which use key generator XXX.
     */
    protected void testOneKeyGen(final Class objClass, final Class extClass)
    throws Exception {
        OQLQuery oql;
        UuidObject object;
        UuidObject ext;
        QueryResults enumeration;
        
        // Open transaction in order to perform JDO operations
        _db.begin();

        // Create first object
        object = (UuidObject) objClass.newInstance();
        LOG.debug("Creating first object: " + object);
        _db.create(object);
        LOG.debug("Created first object: " + object);

        // Create second object
        ext = (UuidObject) extClass.newInstance();
        LOG.debug("Creating second object: " + ext);
        _db.create(ext);
        LOG.debug("Created second object: " + ext);

        _db.commit();

        _db.begin();

        // Find the first object and remove it
        //object = (TestUuidObject) _db.load( objClass, object.getId() );
        oql = _db.getOQLQuery();
        oql.create("SELECT object FROM " + objClass.getName()
                + " object WHERE id = $1");
        oql.bind(object.getId());
        enumeration = oql.execute();
        LOG.debug("Removing first object: " + object);
        if (enumeration.hasMore()) {
            object = (UuidObject) enumeration.next();
            _db.remove(object);
            LOG.debug("OK: Removed");
        } else {
            LOG.error("first object not found");
            fail("first object not found");
        }

        // Find the second object and remove it
        //ext = (TestUuidObject) _db.load( extClass, ext.getId() );
        oql = _db.getOQLQuery();
        oql.create("SELECT ext FROM " + extClass.getName()
                + " ext WHERE id = $1");
        oql.bind(ext.getId());
        enumeration = oql.execute();
        LOG.debug("Removing second object: " + ext);
        if (enumeration.hasMore()) {
            ext = (UuidObject) enumeration.next();
            _db.remove(ext);
            LOG.debug("OK: Removed");
        } else {
            LOG.error("second object not found");
            fail("second object not found");
        }
        _db.commit();
    }
}
