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
package ctf.jdo.tc3x;

import harness.CastorTestCase;
import harness.TestHarness;

import java.sql.SQLException;

import jdo.JDOCategory;

import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.QueryException;
import org.exolab.castor.jdo.QueryResults;

public final class TestNamedQueries extends CastorTestCase {
    
    private static final int    ENTITY_COUNT  = 5;
    private static final String SELECT_ALL_ENTITY_ONE     = "selectAllEntity";
    private static final String SELECT_ALL_ENTITY_HINT    = "selectEntitiesWithHint";
    private static final String SELECT_ENTITY_ONE_BY_ID   = "selectEntityById";
    private static final String SELECT_KNIGHTS_WHO_SAY_NI = "selectAllNightsWhoSayNi";
    private static final String QUERY_WITH_BAD_SYNTAX     = "queryBadSyntax";
    
    private JDOCategory    _category;

    private Database       _db;

    /**
     * Constructor
     *
     * @param category The test suite for these tests
     */
    public TestNamedQueries(final TestHarness category) {
        super(category, "TC38a", "Named query support");
        _category = (JDOCategory) category;
    }

    /**
     * Get a JDO database
     */
    public void setUp() throws PersistenceException, SQLException {
        _db = _category.getDatabase();
        // persist few EntityOne class instances
        _db.begin();
        for (int i = 0; i < ENTITY_COUNT; i++) {
            _db.create(new NQEntity(new Integer(i), String.valueOf(i)));
        }
        _db.commit();       
        
    }

    public void runTest() throws Exception {
        testNamedQuery();
        testNamedQueryIgnoreHint();
        testBadSyntaxNamedQuery();
        testNonExistentNamedQuery();
    }

    /**
     * Test method.
     * @throws Exception For any exception thrown.
     */
    public void testNamedQuery() throws Exception {
        Database db = _category.getDatabase();
        
        // load all EntityOne instances using a named query
        db.begin();
        
        OQLQuery query = db.getNamedQuery(SELECT_ALL_ENTITY_ONE);        
        QueryResults results = query.execute();
        int i = 0;
        while (results.hasMore()) {
            NQEntity entity = (NQEntity) results.next();
            assertNotNull(entity);
            i++;
        }
        assertTrue(i >= ENTITY_COUNT);
        db.commit();
        
        // load EntityOne with Id=1 from persistent store
        db.begin();
        
        query = db.getNamedQuery(SELECT_ENTITY_ONE_BY_ID);
        query.bind(new Integer(0));
        results = query.execute();
        
        NQEntity entity = (NQEntity) results.next();

        assertNotNull(entity);
        assertEquals(new Integer(0), entity.getId());
        
        db.commit();
        db.close();
    }
    
    /**
     * Test method.
     * @throws Exception For any exception thrown.
     */
    public void testNonExistentNamedQuery() throws Exception {
        Database db = _category.getDatabase();
        
        // try to get non-existent named query
        db.begin();
        OQLQuery query = null;            
        try {
            query = db.getNamedQuery(SELECT_KNIGHTS_WHO_SAY_NI);
            fail("Database.getNamedQuery() should have thrown a QueryException");
        } catch (QueryException e) {
            //  great, this is what we expect
            //  check if it's the correct one
            if (!e.getMessage().startsWith("Cannot find a named query")) {
                throw e;
            }
        } finally {
            if (query != null) {
                query.close();
            }
        }
        
        db.commit();
        db.close();
    }
    
    /**
     * Test method.
     * @throws Exception For any exception thrown.
     */
    public void testBadSyntaxNamedQuery() throws Exception {
        Database db = _category.getDatabase();
        
        // try to load non-existent named query
        db.begin();
        try {
            OQLQuery query = db.getNamedQuery(QUERY_WITH_BAD_SYNTAX);            
            query.close(); //this shouldn't happen
        } catch (QueryException e) {
            // great, this is what we expect
            // check if it's the correct one
            if (!e.getMessage().startsWith("Could not find class")) {
                throw e;
            }
        }
        db.commit();
        db.close();
    }
    
    /**
     * Test method.
     * @throws Exception For any exception thrown.
     */
    public void testNamedQueryIgnoreHint() throws Exception {
        Database db = _category.getDatabase();        
        // load all EntityOne instances using a named query
        // and then use same query but with hints
        db.begin();
        
        OQLQuery query = db.getNamedQuery(SELECT_ALL_ENTITY_ONE);        
        QueryResults results = query.execute();
        int countFirst = 0;
        while (results.hasMore()) {
            NQEntity entity = (NQEntity) results.next();
            assertNotNull(entity);
            countFirst++;
        }
        query = db.getNamedQuery(SELECT_ALL_ENTITY_HINT);        
        results = query.execute();
        int countSecond = 0;
        while (results.hasMore()) {
            NQEntity entity = (NQEntity) results.next();
            assertNotNull(entity);
            countSecond++;
        }
        assertEquals(countFirst, countSecond);
        
        db.commit();     
        db.close();
    }
    
    public void tearDown() throws Exception {
        Database db = _category.getDatabase();
        db.begin();
        db.getJdbcConnection().prepareStatement("delete from tc3x_nq_entity").executeUpdate();
        db.commit();
        _db.close();
    }
}
