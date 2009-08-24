/*
 * Copyright 2008 Udai Gupta
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
package org.castor.cpa.test.test82;

import org.castor.cpa.test.framework.CPATestCase;
import org.castor.cpa.test.framework.xml.types.DatabaseEngineType;
import org.exolab.castor.jdo.Database;

/**
 * Tests that modification to read only objects are not persist in the database.
 */
public final class TestDependsNoKeyGen extends CPATestCase {
    private static final String DBNAME = "test82";
    private static final String MAPPING = "/org/castor/cpa/test/test82/mapping.xml";
    private Database _db;

    public TestDependsNoKeyGen(final String name) {
        super(name);
    }

    // Test are only included/excluded for engines that have been tested with this test suite.

    public boolean include(final DatabaseEngineType engine) {
        return (engine == DatabaseEngineType.DERBY)
            || (engine == DatabaseEngineType.MYSQL);
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

    public void testDepends() throws Exception {

        MasterObjectNoKeyGen master = new MasterObjectNoKeyGen();
        master.setId(100);
        master.setDescrip("This is the descrip.");
        _db.begin();
        _db.create(master);
        _db.commit();

        assertTrue(master.getId() != 0);
        assertEquals(100, master.getId());

        // THIS Part Works!
        _db.begin();
        DependentObjectNoKeyGen depends = new DependentObjectNoKeyGen();
        depends.setId(101);
        depends.setDescrip("Description");
        master.setDepends(depends);
        _db.update(master);
        _db.commit();

        assertTrue(master.getId() != 0);
        assertEquals(100, master.getId());
        int masterId = master.getId();

        _db.begin();
        master = _db.load(MasterObjectNoKeyGen.class,
                new Integer(masterId));
        assertNotNull(master.getDepends());
        master.setDepends(null);
        _db.commit();

        _db.begin();
        master = _db.load(MasterObjectNoKeyGen.class,
                new Integer(masterId));
        assertNull(master.getDepends());
        _db.commit();

        // THIS part doesn't!
        _db.begin();
        master = _db.load(MasterObjectNoKeyGen.class,
                new Integer(masterId));
        depends = new DependentObjectNoKeyGen();
        depends.setId(102);
        depends.setDescrip("Description");
        master.setDepends(depends);
        _db.commit();

        _db.begin();
        master = _db.load(MasterObjectNoKeyGen.class,
                new Integer(masterId));
        assertNotNull(master.getDepends());
        _db.commit();

        _db.begin();
        master = _db.load(MasterObjectNoKeyGen.class,
                new Integer(masterId));
        assertNotNull(master);
        _db.remove(master);
        _db.commit();
    }
}
