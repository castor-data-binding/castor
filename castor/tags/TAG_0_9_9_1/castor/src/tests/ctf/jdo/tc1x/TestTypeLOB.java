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

import java.io.IOException;
import java.io.StringReader;
import java.sql.SQLException;
import java.util.Enumeration;

import harness.TestHarness;
import harness.CastorTestCase;

import jdo.JDOCategory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.engine.ClobImpl;

/**
 * Test on BLOB and CLOB as a field type of data objects.
 */
public final class TestTypeLOB extends CastorTestCase {
    /**
     * The <a href="http://jakarta.apache.org/commons/logging/">Jakarta
     * Commons Logging</a> instance used for all logging.
     */
    private static final Log LOG = LogFactory.getLog(TestTypeLOB.class);
    
    private static final byte[] BLOB_VALUE = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};

    private static final String BLOB_STRING = new String(BLOB_VALUE);

    private static final String CLOB_VALUE = "0123456789";

    private JDOCategory    _category;

    private Database       _db;

    private OQLQuery       _oql;
    
    /**
     * Constructor
     *
     * @param category The test suite of these tests
     */
    public TestTypeLOB(final TestHarness category) {
        super(category, "TC11", "Type handling of LOB tests");
        _category = (JDOCategory) category;
    }


    public void setUp() throws PersistenceException {
        TypeLOB       types;
        Enumeration   enumeration;

        // Open transaction in order to perform JDO operations
        _db = _category.getDatabase();

        // Determine if test object exists, if not create it.
        // If it exists, set the name to some predefined value
        // that this test will later override.
        _db.begin();
        _oql = _db.getOQLQuery("SELECT types FROM " + TypeLOB.class.getName()
                + " types WHERE id = $(integer)1");
        // This one tests that bind performs type conversion
        _oql.bind(TypeLOB.DEFAULT_ID);
        enumeration = _oql.execute();
        if (enumeration.hasMoreElements()) {
            types = (TypeLOB) enumeration.nextElement();
            LOG.debug("Updating object: " + types);
        } else {
            types = new TypeLOB();
            types.setId(TypeLOB.DEFAULT_ID);
            LOG.debug("Creating new object: " + types);
            _db.create(types);
        }
        
        _db.commit();
    }

    public void runTest()
    throws PersistenceException, IOException, SQLException {
        TypeLOB       types;
        Enumeration   enumeration;
        int           len;
        byte[]        bbuf = new byte[BLOB_VALUE.length + 1];
        char[]        cbuf = new char[CLOB_VALUE.length() + 1];

        LOG.info("Testing BLOB and CLOB fields");
        _db.begin();
        _oql.bind(TypeLOB.DEFAULT_ID);
        enumeration = _oql.execute();
        if (enumeration.hasMoreElements()) {
            types = (TypeLOB) enumeration.nextElement();
            types.setBlob(BLOB_VALUE);
            types.setClob(CLOB_VALUE);
            //types.setBlob2(new ByteArrayInputStream(BLOB_VALUE));
            types.setClob2(new ClobImpl(new StringReader(CLOB_VALUE),
                                        CLOB_VALUE.length()));
        }
        _db.commit();

        _db.begin();
        _oql.bind(TypeLOB.DEFAULT_ID);
        enumeration = _oql.execute();
        if (enumeration.hasMoreElements()) {
            types = (TypeLOB) enumeration.nextElement();

            if ((types.getBlob() == null)
                    || !BLOB_STRING.equals(new String(types.getBlob()))) {
                
                LOG.error("BLOB value was not set");
                fail("BLOB value was not set");
            }
            if (!CLOB_VALUE.equals(types.getClob())) {
                LOG.error("CLOB value was not set");
                fail("CLOB value was not set");
            }
            /*
            if (types.getBlob2() == null) {
                LOG.error("InputStream value was not set");
                fail("InputStream value was not set");
            } else {
                len = types.getBlob2().read(bbuf);
                if (len <= 0) {
                    LOG.error("InputStream has zero length");
                    fail("InputStream has zero length");
                }
                if (!BLOB_STRING.equals(new String(bbuf, 0, len))) {
                    LOG.error("InputStream value is wrong");
                    fail("InputStream value mismatched!");
                }
            }
            */
            if (types.getClob2() == null) {
                LOG.error("Clob value was not set");
                fail("Clob value was not set");
            } else {
                long clobLen = types.getClob2().length();
                len = types.getClob2().getCharacterStream().read(cbuf);
                if ((clobLen != CLOB_VALUE.length())
                        || !(new String(cbuf, 0, len)).equals(CLOB_VALUE)) {
                    LOG.error("Clob value is wrong");
                    fail("Clob value mismatched!");
                }
            }
        } else {
            LOG.error("failed to load object");
            fail("failed to load object");
        }
        _db.commit();
        LOG.info("OK: BLOB and CLOB fields passed");
    }
}
