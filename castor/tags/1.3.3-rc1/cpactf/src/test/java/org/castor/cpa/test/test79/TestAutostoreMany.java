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

package org.castor.cpa.test.test79;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.castor.cpa.test.framework.CPATestCase;
import org.castor.cpa.test.framework.xml.types.DatabaseEngineType;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.ObjectNotFoundException;
import org.exolab.castor.jdo.QueryResults;

public final class TestAutostoreMany extends CPATestCase {
    private static final String DBNAME = "test79";
    private static final String MAPPING = "/org/castor/cpa/test/test79/mapping.xml";

    private Database _db;

    public TestAutostoreMany(final String name) {
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

    public void testCreateNoEntityTwo() throws Exception {
        _db.begin();
        AutostoreMainMany main = new AutostoreMainMany();
        main.setId(new Integer(100));
        main.setName("main.100");
        _db.create(main);
        _db.commit();

        _db.begin();
        main = _db.load(AutostoreMainMany.class, new Integer(100));
        assertNotNull(main);
        assertEquals(100, main.getId().intValue());
        assertEquals("main.100", main.getName());
        assertNotNull(main.getAssociatedMany());
        assertEquals(0, main.getAssociatedMany().size());
        _db.commit();

        _db.begin();
        main = _db.load(AutostoreMainMany.class, new Integer(100));
        _db.remove(main);
        _db.commit();

        _db.close();
    }

    public void testCreateWithEntityTwoNoAutoStore() throws Exception {
        _db.begin();
        AutostoreAssociatedMany entityTwo = new AutostoreAssociatedMany();
        entityTwo.setId(new Integer(200));
        entityTwo.setName("entity1.200");
        List<AutostoreAssociatedMany> associatedManys = Collections.singletonList(entityTwo);
        AutostoreMainMany entityOne = new AutostoreMainMany();
        entityOne.setId(new Integer(200));
        entityOne.setName("entity2.200");
        entityOne.setAssociatedMany(associatedManys);
        _db.create(entityOne);
        _db.create(entityTwo);
        _db.commit();

        _db.begin();
        entityOne = _db.load(AutostoreMainMany.class, new Integer(200));
        assertNotNull(entityOne);
        assertEquals(200, entityOne.getId().intValue());
        assertEquals("entity2.200", entityOne.getName());
        assertNotNull(entityOne.getAssociatedMany());
        _db.commit();

        _db.begin();
        entityOne = _db.load(AutostoreMainMany.class, new Integer(200));
        _db.remove(entityOne);
        _db.commit();

        _db.begin();
        entityTwo = _db.load(AutostoreAssociatedMany.class, new Integer(200));
        _db.remove(entityTwo);
        _db.commit();

        _db.close();
    }

    public void testCreateWithEntityTwoWithAutoStoreDeleteWithoutAutoStore() throws Exception {
        _db.setAutoStore(true);

        _db.begin();
        AutostoreAssociatedMany entityTwo = new AutostoreAssociatedMany();
        entityTwo.setId(new Integer(300));
        entityTwo.setName("entity2.300");
        AutostoreMainMany entityOne = new AutostoreMainMany();
        entityOne.setId(new Integer(300));
        entityOne.setName("entity1.300");
        entityOne.setAssociatedMany(Collections.singletonList(entityTwo));
        _db.create(entityOne);
        _db.commit();

        _db.begin();
        entityOne = _db.load(AutostoreMainMany.class, new Integer(300));
        assertNotNull(entityOne);
        assertEquals(300, entityOne.getId().intValue());
        assertEquals("entity1.300", entityOne.getName());
        assertNotNull(entityOne.getAssociatedMany());

        List<AutostoreAssociatedMany> associatedManys = entityOne.getAssociatedMany();
        entityTwo = associatedManys.iterator().next();
        assertEquals(300, entityTwo.getId().intValue());
        assertEquals("entity2.300", entityTwo.getName());
        _db.commit();

        _db.setAutoStore(false);

        _db.begin();
        entityOne = _db.load(AutostoreMainMany.class, new Integer(300));
        _db.remove(entityOne);
        _db.commit();

        _db.begin();
        entityTwo = _db.load(AutostoreAssociatedMany.class, new Integer(300));
        assertNotNull(entityTwo);
        assertEquals(300, entityTwo.getId().intValue());
        assertEquals("entity2.300", entityTwo.getName());

        try {
            entityOne = _db.load(AutostoreMainMany.class, new Integer(300));
            fail("Expected ObjectNotFoundException");
        } catch (ObjectNotFoundException e) {
            //
        }
        _db.commit();

        _db.begin();
        entityTwo = _db.load(AutostoreAssociatedMany.class, new Integer(300));
        _db.remove(entityTwo);
        _db.commit();

        _db.begin();
        try {
            entityTwo = _db.load(AutostoreAssociatedMany.class, new Integer(300));
            fail("Expected ObjectNotFoundException");
        } catch (ObjectNotFoundException e) {
            //
        }
        _db.commit();

        _db.close();
    }

    public void testCreateWithEntityTwoWithAutoStoreDeleteWithAutoStore() throws Exception {
        _db.setAutoStore(true);

        _db.begin();
        AutostoreAssociatedMany entityTwo = new AutostoreAssociatedMany();
        entityTwo.setId(new Integer(300));
        entityTwo.setName("entity2.300");
        List<AutostoreAssociatedMany> manys = new ArrayList<AutostoreAssociatedMany>();
        manys.add(entityTwo);
        AutostoreMainMany entityOne = new AutostoreMainMany();
        entityOne.setId(new Integer(300));
        entityOne.setName("entity1.300");
        entityOne.setAssociatedMany(manys);
        _db.create(entityOne);
        _db.commit();

        _db.begin();
        entityOne = _db.load(AutostoreMainMany.class, new Integer(300));
        assertNotNull(entityOne);
        assertEquals(300, entityOne.getId().intValue());
        assertEquals("entity1.300", entityOne.getName());
        assertNotNull(entityOne.getAssociatedMany());
        assertEquals(1, entityOne.getAssociatedMany().size());

        AutostoreAssociatedMany many = entityOne.getAssociatedMany().iterator().next();
        assertEquals(300, many.getId().intValue());
        _db.commit();

        _db.begin();
        many = _db.load(AutostoreAssociatedMany.class, new Integer(300));
        assertNotNull(many);
        assertEquals(300, many.getId().intValue());
        assertEquals("entity2.300", many.getName());
        _db.commit();

        _db.begin();
        entityOne = _db.load(AutostoreMainMany.class, new Integer(300));
        _db.remove(entityOne);
        _db.commit();

        _db.begin();
        try {
            entityOne = _db.load(AutostoreMainMany.class, new Integer(300));
            fail("Expected ObjectNotFoundException");
        } catch (ObjectNotFoundException e) {
            //
        }
        try {
            many = _db.load(AutostoreAssociatedMany.class, new Integer(300));
            // TODO remove once support for cascading delete has been added
            // fail("Expected ObjectNotFoundException");
        } catch (ObjectNotFoundException e) {
            //
        }
        _db.commit();

        // TODO remove once support for cascading delete has been added
        _db.begin();
        many = _db.load(AutostoreAssociatedMany.class, new Integer(300));
        _db.remove(many);
        _db.commit();

        _db.close();
    }

    public void testQueryEntityOne() throws Exception {
        _db.begin();

        OQLQuery query = _db.getOQLQuery("SELECT entity FROM "
                + AutostoreMainMany.class.getName() + " entity WHERE id = $1");
        query.bind(new Integer(1));
        QueryResults results = query.execute();

        AutostoreMainMany entity = (AutostoreMainMany) results.next();

        assertNotNull(entity);
        assertEquals(new Integer(1), entity.getId());

        List<AutostoreAssociatedMany> associatedManys = entity.getAssociatedMany();

        assertNotNull(associatedManys);

        Iterator<AutostoreAssociatedMany> iter = associatedManys.iterator();

        assertTrue(iter.hasNext());
        AutostoreAssociatedMany associatedMany = iter.next();
        assertEquals(new Integer(1), associatedMany.getId());

        assertTrue(iter.hasNext());
        associatedMany = iter.next();
        assertEquals(new Integer(2), associatedMany.getId());

        assertFalse(iter.hasNext());

        _db.commit();
        _db.close();
    }
}
