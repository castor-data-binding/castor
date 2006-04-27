/*
 * Copyright 2005 Werner Guttmann
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
package ctf.jdo.tc8x;

import jdo.JDOCategory;

import org.exolab.castor.jdo.Database;

import harness.CastorTestCase;
import harness.TestHarness;

/**
 * Tests that modification to read only objects are not persist in the 
 * database.
 */
public final class TestDepends extends CastorTestCase {
    private JDOCategory _category;

    /**
     * Constructor
     * @param category the test suite that this test case belongs
     */
    public TestDepends(final TestHarness category) {
        super(category, "TC81", "Dependent relation test");
        _category = (JDOCategory) category;
    }

    /**
     * Tests that modification to read only objects are not persist in the 
     * database.
     */
    public void runTest() throws Exception {
        testDepends();
    }

    
    public void testDepends() throws Exception {
        Database db = null;
        MasterObject master = new MasterObject();
        master.setDescrip("This is the descrip.");
        db = _category.getDatabase();
        db.begin();
        db.create(master);
        db.commit();

        assertTrue(master.getId() != 0);

        //THIS Part Works!
        db.begin();
        DependentObject depends = new DependentObject();
        depends.setDescrip("Description");
        master.setDepends(depends);
        db.update(master);
        db.commit();

        assertTrue(master.getId() != 0);
        int masterId = master.getId();

        db.begin();
        master = (MasterObject) db.load(MasterObject.class, new Integer(masterId));
        assertNotNull(master.getDepends());
        master.setDepends(null);
        db.commit();

        db.begin();
        master = (MasterObject) db.load(MasterObject.class, new Integer(masterId));
        assertNull(master.getDepends());
        db.commit();

        //THIS part doesn't!
        db.begin();
        master = (MasterObject) db.load(MasterObject.class, new Integer(masterId));
        depends = new DependentObject();
        depends.setDescrip("Description");
        master.setDepends(depends);
        db.commit();

        db.begin();
        master = (MasterObject) db.load(MasterObject.class, new Integer(masterId));
        assertNotNull(master.getDepends());
        db.commit();

        db.begin();
        master = (MasterObject) db.load(MasterObject.class, new Integer(
                masterId));
        assertNotNull(master);
        db.remove(master);
        db.commit();
    }
    
}
