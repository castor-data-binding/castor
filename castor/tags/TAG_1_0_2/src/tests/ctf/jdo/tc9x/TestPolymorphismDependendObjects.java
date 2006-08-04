/*
 * Copyright 2005 Werner Guttmann, Nick Stuart
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
import org.exolab.castor.jdo.PersistenceException;

/**
 * Castor CTF test case to test polymorphistic operations in the context of
 * depend relations
 * 
 * @author <a href=""mailto:nstuart">Nick Stuart</a>
 */
public final class TestPolymorphismDependendObjects extends CastorTestCase {
    private JDOCategory _category;

    /**
     * Constructor
     * @param category the test suite that this test case belongs
     */
    public TestPolymorphismDependendObjects(final TestHarness category) {
        super(category, "TC96", "Polymorphism tests for depend relations");
        _category = (JDOCategory) category;
    }

    /* (non-Javadoc)
     * @see junit.framework.TestCase#runTest()
     */
    public void runTest() throws Exception {
        testLoad();
    }

    public void testLoad() {
        try {
            Database db = _category.getDatabase();
            db.begin();
            try {
                ExtendedObject obj = (ExtendedObject) db.load(BaseObject.class,
                        new Integer(1));
                db.commit();
                assertNotNull(obj);
                assertNotNull(obj.getDependent());
                db.close();

            } catch (ClassCastException e) {
                db.rollback();
                db.close();
                fail();
            }
        } catch (PersistenceException e) {
            e.printStackTrace();
            fail();
        }
    }

     public void testSave() {
        Database db = null;
        try {
            db = _category.getDatabase();
            db.begin();
            try {
                ExtendedObject obj1 = (ExtendedObject) db.load(
                        ExtendedObject.class, new Integer(1));
                obj1.setDescription2(obj1.getDescription2() + " - 1");
                db.commit();
                db.close();

            } catch (ClassCastException e) {
                e.printStackTrace();
                db.rollback();
                db.close();
                fail();
            }
        } catch (PersistenceException e) {
            e.printStackTrace();
            fail();
        }
    }
}
