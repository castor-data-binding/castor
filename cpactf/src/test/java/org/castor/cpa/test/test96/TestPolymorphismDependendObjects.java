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
package org.castor.cpa.test.test96;

import org.castor.cpa.test.framework.CPATestCase;
import org.castor.cpa.test.framework.xml.types.DatabaseEngineType;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.PersistenceException;

public final class TestPolymorphismDependendObjects extends CPATestCase {
    private static final String DBNAME = "test96";
    private static final String MAPPING = "/org/castor/cpa/test/test96/mapping.xml";

    public TestPolymorphismDependendObjects(final String name) {
        super(name);
    }

    // Test are only included/excluded for engines that have been tested with this test suite.

    public boolean include(final DatabaseEngineType engine) {
        return (engine == DatabaseEngineType.DERBY)
            || (engine == DatabaseEngineType.MYSQL);
    }

    public void testLoad() throws Exception {
        try {
            Database db = getJDOManager(DBNAME, MAPPING).getDatabase();
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

     public void xtestSave() throws Exception {
        Database db = null;
        try {
            db = getJDOManager(DBNAME, MAPPING).getDatabase();
            db.begin();
            try {
                ExtendedObject obj1 = db.load(
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
