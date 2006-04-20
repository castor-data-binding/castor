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

package ctf.jdo.tc3x;

import harness.TestHarness;

import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.QueryResults;
import org.exolab.castor.jdo.oql.OQLSyntaxException;

public final class TestLimitWithOffsetExtend extends TestLimitClause {
    private static final int OFFSET = 2;

    /**
     * Constructor
     *
     * @param category The test suite for these tests
     */
    public TestLimitWithOffsetExtend(final TestHarness category) {
        super(category, "TC34", "Test limit clause with offset at extended object");
    }

    public void testLimitWithOffset() throws PersistenceException {
        getDatabase().begin();

        OQLQuery query = getDatabase().getOQLQuery("select t from "
                + Entity.class.getName() + " t order by id limit $1 offset $2");

        query.bind(LIMIT);
        query.bind(OFFSET);

        QueryResults results = query.execute();
        assertNotNull (results);
        // size() not available using an Oracle DB assertEquals (LIMIT, results.size());
        for (int i = 1 + OFFSET; i <= OFFSET + LIMIT; i++) {
            Entity testObject = (Entity) results.next();
            assertEquals(i, testObject.getId());
        }
        assertTrue(!results.hasMore());

        getDatabase().commit();
    }

    public void testOffsetWithoutLimit() throws PersistenceException {
        getDatabase().begin();

        try {
            getDatabase().getOQLQuery(
                    "select t from " + Entity.class.getName() + " t offset $1");
        } catch (OQLSyntaxException ex) {
            assertEquals("org.exolab.castor.jdo.oql.OQLSyntaxException",
                    ex.getClass().getName());
            return;
        } finally {
            getDatabase().commit();
        }
    }

    public void runTest() throws Exception {
        super.runTest();
        testLimitWithOffset();
        testOffsetWithoutLimit();
    }
}
