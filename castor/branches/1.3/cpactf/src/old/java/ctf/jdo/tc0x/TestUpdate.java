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
package ctf.jdo.tc0x;

import java.sql.SQLException;

import harness.CastorTestCase;
import harness.TestHarness;

import jdo.JDOCategory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.PersistenceException;

public final class TestUpdate extends CastorTestCase {
    /**
     * The <a href="http://jakarta.apache.org/commons/logging/">Jakarta
     * Commons Logging</a> instance used for all logging.
     */
    private static final Log LOG = LogFactory.getLog(TestUpdate.class);
    
    private JDOCategory    _category;

    private Database       _db;

    /**
     * Constructor
     *
     * @param category The test suite for these tests
     */
    public TestUpdate(final TestHarness category) {
        super(category, "TC05", "Update rollback tests");
        _category = (JDOCategory) category;
    }

    /**
     * Get a JDO database
     */
    public void setUp() throws PersistenceException, SQLException {
        _db = _category.getDatabase();
    }

    public void runTest() throws PersistenceException, SQLException {
        Sample    object;

        try {
            _db.begin();
  
            object = new Sample();
            object.setId(50);
            LOG.debug("trying to update non timestampable object");
            _db.update(object);
            _db.commit();
            LOG.error("should fail");
            fail("should fail");
        } catch (Exception ex) {
            LOG.debug("catched exception: ", ex);
            try {
                LOG.debug("trying to rollback");
                _db.rollback();
            } catch (Exception ex2) {
                LOG.error("rollback should work: ", ex2);
                fail("rollback should work: " + ex2);
            }
        }
    }

    public void tearDown() throws PersistenceException {
        if (_db.isActive()) { _db.rollback(); } 
        _db.close();
    }
}
