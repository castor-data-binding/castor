/*
 * Copyright 2005 Ralf Joachim
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
package ctf.jdo.tc9x;

import harness.CastorTestCase;
import harness.TestHarness;
import jdo.JDOCategory;

import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.QueryResults;

public final class TestPolymorphismDegenerated extends CastorTestCase {
    private JDOCategory _category;

    /**
     * Constructor
     * @param category the test suite that this test case belongs
     */
    public TestPolymorphismDegenerated(final TestHarness category) {
        super(category, "TC93", "Polymorphism Degenerated tests");
        _category = (JDOCategory) category;
    }

    /**
     * Tests that modification to read only objects are not persist in the 
     * database.
     */
    public void runTest() throws Exception {
        testLoad();
        testQuery();
        testRetrievingSubscriptions();
    }

    public void testLoad() throws Exception {
        Database db = _category.getDatabase();
        db.begin();

        Object obj1 = db.load(Foo.class, new Integer(1));
        assertTrue(obj1 instanceof Foo);
        assertFalse(obj1 instanceof Bar);

        Object obj2 = db.load(Foo.class, new Integer(2));
        assertTrue(obj2 instanceof Foo);
        assertTrue(obj2 instanceof Bar);
    }

    public void testQuery() throws Exception {
        Database db = _category.getDatabase();
        db.begin();

        String oql = "select o from " + Foo.class.getName() + " o";
        QueryResults qrs = db.getOQLQuery(oql).execute();

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
        Database db = _category.getDatabase();
        db.begin();

        String oql = "select o from " + Subscription.class.getName()
                + " o where o.customer = 2";
        QueryResults qrs = db.getOQLQuery(oql).execute();
        
        assertEquals(2, qrs.size());
        
        while (qrs.hasMore()) {
            assertTrue(qrs.next() instanceof Subscription);
        }
        qrs.close();
        
        db.rollback();
    }
}
