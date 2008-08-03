/*
 * Copyright 2007 Ralf Joachim
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
package ctf.jdo.tc20x;

import harness.CastorTestCase;
import harness.TestHarness;

import jdo.JDOCategory;

import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.PersistenceException;

public final class TestForeignKeyFirst extends CastorTestCase {
    private JDOCategory _category;

    public TestForeignKeyFirst(final TestHarness category) {
        super(category, "TC202", "ForeignKeyFirst tests");
        _category = (JDOCategory) category;
    }

    public void runTest() throws PersistenceException {
        testLoad();
    }
    
    public void testLoad() throws PersistenceException {
        Database db = _category.getDatabase();

        db.begin();

        Object entity = db.load(ForeignKeyFirstEntity1.class, new Integer(1));
        assertNotNull(entity);

        db.commit();

        if (db != null) {
            if (db.isActive()) { db.rollback(); }

            db.close();
            db = null;
        }
    }
}
