/*
 * Copyright 2009 Udai Gupta, Ralf Joachim
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.castor.cpa.test.test93;

import org.castor.cpa.test.framework.CPATestCase;
import org.castor.cpa.test.framework.xml.types.DatabaseEngineType;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.QueryResults;

public final class TestPolymorphismDegenerated extends CPATestCase {
    private static final String DBNAME = "test93";
    private static final String MAPPING = "/org/castor/cpa/test/test93/mapping.xml";
    private Database _db;

    public TestPolymorphismDegenerated(final String name) {
        super(name);
    }

    // Test are only included/excluded for engines that have been tested with this test suite.

    public boolean include(final DatabaseEngineType engine) {
        return (engine == DatabaseEngineType.DERBY)
            || (engine == DatabaseEngineType.MYSQL)
            || (engine == DatabaseEngineType.POSTGRESQL);
    }

    public boolean exclude(final DatabaseEngineType engine) {
        return (engine == DatabaseEngineType.ORACLE);
    }

    public void setUp() throws Exception {
        _db = getJDOManager(DBNAME, MAPPING).getDatabase();
    }

    public void tearDown() throws Exception {
        if (_db.isActive()) {
            _db.rollback();
        }
        _db.close();
    }

    public void testLoad() throws Exception {
        _db.begin();

        Object obj1 = _db.load(Foo.class, new Integer(1));
        assertTrue(obj1 instanceof Foo);
        assertFalse(obj1 instanceof Bar);

        Object obj2 = _db.load(Foo.class, new Integer(2));
        assertTrue(obj2 instanceof Foo);
        assertTrue(obj2 instanceof Bar);
    }

    public void testQuery() throws Exception {
        _db.begin();

        String oql = "select o from " + Foo.class.getName() + " o";
        QueryResults qrs = _db.getOQLQuery(oql).execute();

        while (qrs.hasMore()) {
            Object obj = qrs.next();
            assertTrue(obj instanceof Foo);
            
            // Do it this way, using the id of the item, rather than relying 
            // on the expected ordering of the resultset.
            Foo foo = (Foo) obj;
            if (foo.getID() == 1) {
                assertFalse(obj instanceof Bar);
            } else if (foo.getID() == 2) {
                assertTrue(obj instanceof Bar);
            } else {
                fail();
            }
        }
    }

    public void testRetrievingSubscriptions() throws Exception {
        _db.begin();

        String oql = "select o from " + Subscription.class.getName()
                + " o where o.customer = 2";
        QueryResults qrs = _db.getOQLQuery(oql).execute(true);
        
        assertEquals(2, qrs.size());
        
        while (qrs.hasMore()) {
            assertTrue(qrs.next() instanceof Subscription);
        }
        qrs.close();
        
        _db.rollback();
    }
}
