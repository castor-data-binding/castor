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
package org.castor.cpa.test.test83;

import org.castor.cpa.test.framework.CPATestCase;
import org.castor.cpa.test.framework.xml.types.DatabaseEngineType;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.persist.spi.Identity;

/**
 * JUnit test to test drive specification of class identity through the use of
 * the identity attribute on the <field> mapping.
 */
public final class TestIdentityPerFieldMapping extends CPATestCase {
    private static final String DBNAME = "test83";
    private static final String MAPPING = "/org/castor/cpa/test/test83/mapping.xml";
    private Database _db;

    public TestIdentityPerFieldMapping(final String name) {
        super(name);
    }

    // Test are only included/excluded for engines that have been tested with this test suite.

    public boolean include(final DatabaseEngineType engine) {
        return (engine == DatabaseEngineType.DERBY)
            || (engine == DatabaseEngineType.HSQL)
            || (engine == DatabaseEngineType.MYSQL)
            || (engine == DatabaseEngineType.ORACLE)
            || (engine == DatabaseEngineType.POSTGRESQL)
            || (engine == DatabaseEngineType.SQL_SERVER)
            || (engine == DatabaseEngineType.SAPDB);
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

    public void testQueryEntityOne() throws Exception {
        _db.begin();

        Parent entity = _db.load(Parent.class, new Integer(1));

        assertNotNull(entity);
        assertEquals(new Integer(1), entity.getId());

        _db.commit();
        _db.close();
    }

    public void testLoadChild() throws Exception {
        _db.begin();

        Child child = _db.load(Child.class, new Integer(1));

        assertNotNull(child);
        assertEquals(new Integer(1), child.getId());

        _db.commit();
        _db.close();
    }

    public void testLoadEntityWithCompoundId() throws Exception {
        _db.begin();

        ParentWithCompoundId child = _db.load(ParentWithCompoundId.class,
                new Identity(new Integer(1), new Integer(1)));

        assertNotNull(child);
        assertEquals(new Integer(1), child.getId1());
        assertEquals(new Integer(1), child.getId2());

        _db.commit();
        _db.close();
    }

    public void testLoadChildWithCompoundId() throws Exception {
        _db.begin();

        ChildWithCompoundId child = _db.load(ChildWithCompoundId.class,
                new Identity(new Integer(1), new Integer(1)));

        assertNotNull(child);
        assertEquals(new Integer(1), child.getId1());
        assertEquals(new Integer(1), child.getId2());

        _db.commit();
        _db.close();
    }

}
