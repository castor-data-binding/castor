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

import org.castor.cpa.test.framework.CPATestCase;
import org.castor.cpa.test.framework.xml.types.DatabaseEngineType;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.ObjectNotFoundException;
import org.exolab.castor.jdo.QueryResults;

public final class TestAutostore extends CPATestCase {
    private static final String DBNAME = "test79";
    private static final String MAPPING = "/org/castor/cpa/test/test79/mapping.xml";

    private Database _db;

    public TestAutostore(final String name) {
        super(name);
    }

    // Test are only included/excluded for engines that have been tested with this test suite.

    public boolean include(final DatabaseEngineType engine) {
        return (engine == DatabaseEngineType.DERBY)
            || (engine == DatabaseEngineType.HSQL)
            || (engine == DatabaseEngineType.MYSQL)
            || (engine == DatabaseEngineType.ORACLE)
            || (engine == DatabaseEngineType.POSTGRESQL);
    }

    public void setUp() throws Exception {
        _db = getJDOManager(DBNAME, MAPPING).getDatabase();
    }

    public void testCreateNoEntityTwo() throws Exception {
        _db.begin();
        AutostoreMain entityOne = new AutostoreMain();
        entityOne.setId(new Integer(100));
        entityOne.setName("entity1.100");
        _db.create(entityOne);
        _db.commit();

        _db.begin();
        entityOne = _db.load(AutostoreMain.class, new Integer(100));
        assertNotNull(entityOne);
        assertEquals(100, entityOne.getId().intValue());
        assertEquals("entity1.100", entityOne.getName());
        assertNull(entityOne.getAssociatedOne());
        _db.commit();

        _db.begin();
        entityOne = _db.load(AutostoreMain.class, new Integer(100));
        _db.remove(entityOne);
        _db.commit();
        _db.close();
    }

    public void testCreateWithEntityTwoNoAutoStore() throws Exception {
        _db.begin();
        AutostoreAssociated1 entityTwo = new AutostoreAssociated1();
        entityTwo.setId(new Integer(200));
        entityTwo.setName("entity1.200");
        AutostoreMain entityOne = new AutostoreMain();
        entityOne.setId(new Integer(200));
        entityOne.setName("entity2.200");
        entityOne.setAssociatedOne(entityTwo);
        _db.create(entityOne);
        _db.commit();

        _db.begin();
        entityOne = _db.load(AutostoreMain.class, new Integer(200));
        assertNotNull(entityOne);
        assertEquals(200, entityOne.getId().intValue());
        assertEquals("entity2.200", entityOne.getName());
        assertNull(entityOne.getAssociatedOne());
        _db.commit();

        _db.begin();
        entityOne = _db.load(AutostoreMain.class, new Integer(200));
        _db.remove(entityOne);
        _db.commit();

        _db.close();
    }

    public void testCreateWithEntityTwoWithAutoStoreDeleteWithoutAutoStore() throws Exception {
        _db.setAutoStore(true);

        _db.begin();
        AutostoreAssociated1 assocOne = new AutostoreAssociated1();
        assocOne.setId(new Integer(300));
        assocOne.setName("entity2.300");
        AutostoreMain main = new AutostoreMain();
        main.setId(new Integer(300));
        main.setName("entity1.300");
        main.setAssociatedOne(assocOne);
        _db.create(main);
        _db.commit();

        _db.begin();
        main = _db.load(AutostoreMain.class, new Integer(300));
        assertNotNull(main);
        assertEquals(300, main.getId().intValue());
        assertEquals("entity1.300", main.getName());
        assertNotNull(main.getAssociatedOne());
        assertEquals(300, main.getAssociatedOne().getId().intValue());
        _db.commit();

        _db.begin();
        assocOne = _db.load(AutostoreAssociated1.class, new Integer(300));
        assertNotNull(assocOne);
        assertEquals(300, assocOne.getId().intValue());
        assertEquals("entity2.300", assocOne.getName());
        _db.commit();

        _db.setAutoStore(false);

        _db.begin();
        main = _db.load(AutostoreMain.class, new Integer(300));
        _db.remove(main);
        _db.commit();

        _db.begin();
        assocOne = _db.load(AutostoreAssociated1.class, new Integer(300));
        assertNotNull(assocOne);
        assertEquals(300, assocOne.getId().intValue());
        assertEquals("entity2.300", assocOne.getName());

        try {
            main = _db.load(AutostoreMain.class, new Integer(300));
            fail("Expected ObjectNotFoundException");
        } catch (ObjectNotFoundException e) {
            //
        }
        _db.commit();

        _db.begin();
        assocOne = _db.load(AutostoreAssociated1.class, new Integer(300));
        _db.remove(assocOne);
        _db.commit();

        _db.begin();
        try {
            assocOne = _db.load(AutostoreAssociated1.class, new Integer(300));
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
        AutostoreAssociated1 assocOne = new AutostoreAssociated1();
        assocOne.setId(new Integer(300));
        assocOne.setName("entity2.300");
        AutostoreMain main = new AutostoreMain();
        main.setId(new Integer(300));
        main.setName("entity1.300");
        main.setAssociatedOne(assocOne);
        _db.create(main);
        _db.commit();

        _db.begin();
        main = _db.load(AutostoreMain.class, new Integer(300));
        assertNotNull(main);
        assertEquals(300, main.getId().intValue());
        assertEquals("entity1.300", main.getName());
        assertNotNull(main.getAssociatedOne());
        assertEquals(300, main.getAssociatedOne().getId().intValue());
        _db.commit();

        _db.begin();
        assocOne = _db.load(AutostoreAssociated1.class, new Integer(300));
        assertNotNull(assocOne);
        assertEquals(300, assocOne.getId().intValue());
        assertEquals("entity2.300", assocOne.getName());
        _db.commit();

        _db.begin();
        main = _db.load(AutostoreMain.class, new Integer(300));
        _db.remove(main);
        _db.commit();

        _db.begin();
        try {
            main = _db.load(AutostoreMain.class, new Integer(300));
            fail("Expected ObjectNotFoundException");
        } catch (ObjectNotFoundException e) {
            //
        }
        try {
            assocOne = _db.load(AutostoreAssociated1.class, new Integer(300));
            // TODO remove once support for cascading delete has been added
            // fail("Expected ObjectNotFoundException");
        } catch (ObjectNotFoundException e) {
            //
        }
        _db.commit();

        // TODO remove once support for cascading deletes has been added
        _db.begin();
        assocOne = _db.load(AutostoreAssociated1.class, new Integer(300));
        _db.remove(assocOne);
        _db.commit();

        _db.close();
    }

    public void testQueryEntityOne() throws Exception {
        _db.begin();

        OQLQuery query = _db.getOQLQuery("SELECT entity FROM "
                + AutostoreMain.class.getName() + " entity WHERE id = $1");
        query.bind(new Integer(1));
        QueryResults results = query.execute();

        AutostoreMain entity = (AutostoreMain) results.next();

        assertNotNull(entity);
        assertEquals(new Integer(1), entity.getId());

        AutostoreAssociated1 entity2 = entity.getAssociatedOne();

        assertNotNull(entity2);
        assertEquals(new Integer(1), entity2.getId());

        _db.commit();
        _db.close();
    }
}
