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
package ctf.jdo.tc7x;

import harness.CastorTestCase;
import harness.TestHarness;

import jdo.JDOCategory;

import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.ObjectNotFoundException;
import org.exolab.castor.jdo.QueryResults;

public class TestAutostore extends CastorTestCase {

    private JDOCategory    _category;

    /**
     * Constructor
     *
     * @param category The test suite for these tests
     */
    public TestAutostore(final TestHarness category) {
        super(category, "TC79a", "Test auto-store attribute");
        _category = (JDOCategory) category;
    }

    public void runTest() throws Exception {
        testCreateNoEntityTwo();
        testCreateWithEntityTwoNoAutoStore();
        testCreateWithEntityTwoWithAutoStoreDeleteWithAutoStore();
        testCreateWithEntityTwoWithAutoStoreDeleteWithoutAutoStore();
        testQueryEntityOne();
    }

    public void testCreateNoEntityTwo() throws Exception {
        Database db = _category.getDatabase();
        db.begin();
        AutostoreMain entityOne = new AutostoreMain();
        entityOne.setId(new Integer(100));
        entityOne.setName("entity1.100");
        db.create(entityOne);
        db.commit();
        
        db.begin();
        entityOne = (AutostoreMain) db.load(AutostoreMain.class, new Integer(100));
        assertNotNull(entityOne);
        assertEquals(100, entityOne.getId().intValue());
        assertEquals("entity1.100", entityOne.getName());
        assertNull(entityOne.getAssociatedOne());
        db.commit();

        db.begin();
        entityOne = (AutostoreMain) db.load(AutostoreMain.class, new Integer(100));
        db.remove(entityOne);
        db.commit();
        db.close();
}

    public void testCreateWithEntityTwoNoAutoStore() throws Exception {
        Database db = _category.getDatabase();
        db.begin();
        AutostoreAssociated1 entityTwo = new AutostoreAssociated1();
        entityTwo.setId(new Integer(200));
        entityTwo.setName("entity1.200");
        AutostoreMain entityOne = new AutostoreMain();
        entityOne.setId(new Integer(200));
        entityOne.setName("entity2.200");
        entityOne.setAssociatedOne(entityTwo);
        db.create(entityOne);
        db.commit();
        
        db.begin();
        entityOne = (AutostoreMain) db.load(AutostoreMain.class, new Integer(200));
        assertNotNull(entityOne);
        assertEquals(200, entityOne.getId().intValue());
        assertEquals("entity2.200", entityOne.getName());
        assertNull(entityOne.getAssociatedOne());
        db.commit();

        db.begin();
        entityOne = (AutostoreMain) db.load(AutostoreMain.class, new Integer(200));
        db.remove(entityOne);
        db.commit();
        
        db.close();
    }

    public void testCreateWithEntityTwoWithAutoStoreDeleteWithoutAutoStore()
    throws Exception {
        Database db = _category.getDatabase();
        db.setAutoStore(true);

        db.begin();
        AutostoreAssociated1 assocOne = new AutostoreAssociated1();
        assocOne.setId(new Integer(300));
        assocOne.setName("entity2.300");
        AutostoreMain main = new AutostoreMain();
        main.setId(new Integer(300));
        main.setName("entity1.300");
        main.setAssociatedOne(assocOne);
        db.create(main);
        db.commit();
        
        db.begin();
        main = (AutostoreMain) db.load(AutostoreMain.class, new Integer(300));
        assertNotNull(main);
        assertEquals(300, main.getId().intValue());
        assertEquals("entity1.300", main.getName());
        assertNotNull(main.getAssociatedOne());
        assertEquals(300, main.getAssociatedOne().getId().intValue());
        db.commit();

        db.begin();
        assocOne = (AutostoreAssociated1) 
            db.load(AutostoreAssociated1.class, new Integer(300));
        assertNotNull(assocOne);
        assertEquals(300, assocOne.getId().intValue());
        assertEquals("entity2.300", assocOne.getName());
        db.commit();
        
        db.setAutoStore(false);
        
        db.begin();
        main = (AutostoreMain) db.load(AutostoreMain.class, new Integer(300));
        db.remove(main);
        db.commit();
        
        db.begin();
        assocOne = (AutostoreAssociated1) 
            db.load(AutostoreAssociated1.class, new Integer(300));
        assertNotNull(assocOne);
        assertEquals(300, assocOne.getId().intValue());
        assertEquals("entity2.300", assocOne.getName());
        
        try {
            main = (AutostoreMain) db.load(AutostoreMain.class, new Integer(300));
            fail("Expected ObjectNotFoundException");
        } catch (ObjectNotFoundException e) {
            //
        }
        db.commit();

        db.begin();
        assocOne = (AutostoreAssociated1) 
            db.load(AutostoreAssociated1.class, new Integer(300));
        db.remove(assocOne);
        db.commit();

        db.begin();
        try {
            assocOne = (AutostoreAssociated1) 
                db.load(AutostoreAssociated1.class, new Integer(300));
            fail("Expected ObjectNotFoundException");
        } catch (ObjectNotFoundException e) {
            //
        }
        db.commit();

        db.close();
    }

    public void testCreateWithEntityTwoWithAutoStoreDeleteWithAutoStore() 
    throws Exception {
        Database db = _category.getDatabase();
        db.setAutoStore(true);

        db.begin();
        AutostoreAssociated1 assocOne = new AutostoreAssociated1();
        assocOne.setId(new Integer(300));
        assocOne.setName("entity2.300");
        AutostoreMain main = new AutostoreMain();
        main.setId(new Integer(300));
        main.setName("entity1.300");
        main.setAssociatedOne(assocOne);
        db.create(main);
        db.commit();
        
        db.begin();
        main = (AutostoreMain) db.load(AutostoreMain.class, new Integer(300));
        assertNotNull(main);
        assertEquals(300, main.getId().intValue());
        assertEquals("entity1.300", main.getName());
        assertNotNull(main.getAssociatedOne());
        assertEquals(300, main.getAssociatedOne().getId().intValue());
        db.commit();

        db.begin();
        assocOne = (AutostoreAssociated1) 
            db.load(AutostoreAssociated1.class, new Integer(300));
        assertNotNull(assocOne);
        assertEquals(300, assocOne.getId().intValue());
        assertEquals("entity2.300", assocOne.getName());
        db.commit();
        
        db.begin();
        main = (AutostoreMain) db.load(AutostoreMain.class, new Integer(300));
        db.remove(main);
        db.commit();
        
        db.begin();
        try {
            main = (AutostoreMain) db.load(AutostoreMain.class, new Integer(300));
            fail("Expected ObjectNotFoundException");
        } catch (ObjectNotFoundException e) {
            //
        }
        try {
            assocOne = (AutostoreAssociated1) 
                db.load(AutostoreAssociated1.class, new Integer(300));
            // TODO: remove once support for cascading delete has been added
//            fail("Expected ObjectNotFoundException");
        } catch (ObjectNotFoundException e) {
            //
        }
        db.commit();

        // TODO: remove once support for cascading deletes has been added
        db.begin();
        assocOne = (AutostoreAssociated1) 
        db.load(AutostoreAssociated1.class, new Integer(300));
            db.remove(assocOne);
        db.commit();

        db.close();
    }

    /**
     * Test method.
     * @throws Exception For any exception thrown.
     */
    public void testQueryEntityOne() throws Exception {
        Database db = _category.getDatabase();
        db.begin();
        
        OQLQuery query = db.getOQLQuery("SELECT entity FROM "
                + AutostoreMain.class.getName() + " entity WHERE id = $1");
        query.bind(new Integer(1));
        QueryResults results = query.execute();
        
        AutostoreMain entity = (AutostoreMain) results.next();

        assertNotNull(entity);
        assertEquals(new Integer(1), entity.getId());
        
        AutostoreAssociated1 entity2 = entity.getAssociatedOne();
        
        assertNotNull(entity2);
        assertEquals(new Integer(1), entity2.getId());
        
        
        db.commit();
        db.close();
    }
}
