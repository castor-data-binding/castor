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
package org.castor.cpa.test.test78;

import org.castor.cpa.test.framework.CPATestCase;
import org.castor.cpa.test.framework.xml.types.DatabaseEngineType;
import org.exolab.castor.jdo.Database;

/**
 * Tests the correct working of @link
org.exolab.castor.jdo.Database#isLocked().
 */
public final class TestIsPersistent extends CPATestCase {
    private static final String DBNAME = "test78";
    private static final String MAPPING = "/org/castor/cpa/test/test78/mapping.xml";
    private Database _db;

    public TestIsPersistent(final String name) {
        super(name);
    }

    // Test are only included/excluded for engines that have been tested with this test suite.

    public boolean include(final DatabaseEngineType engine) {
        return (engine == DatabaseEngineType.DERBY)
            || (engine == DatabaseEngineType.MYSQL)
            || (engine == DatabaseEngineType.POSTGRESQL);
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

    public void testIsPersistentEntity() throws Exception {
        _db.begin();
        Limit item = new Limit();
        item.setId(111);
        item.setValue1("value1 111");
        item.setValue2("value2 111");
        _db.create(item);
        _db.commit();

        _db.begin();
        item = _db.load(Limit.class, new Integer(111));

        assertNotNull(item);
        assertEquals(111, item.getId());

        assertTrue(_db.isPersistent(item));
        _db.commit();

        _db.begin();
        item = _db.load(Limit.class, new Integer(111));
        _db.remove(item);
        _db.commit();
    }

    public void testIsNotPersistentEntity() throws Exception {
        _db.begin();
        Limit item112 = new Limit();
        item112.setId(112);
        item112.setValue1("value1 112");
        item112.setValue2("value2 112");

        assertFalse(_db.isPersistent(item112));
        _db.commit();
    }
}


