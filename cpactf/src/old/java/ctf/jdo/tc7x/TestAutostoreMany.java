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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import jdo.JDOCategory;

import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.ObjectNotFoundException;
import org.exolab.castor.jdo.QueryResults;

public class TestAutostoreMany extends CastorTestCase {

    private JDOCategory    _category;

    /**
     * Constructor
     *
     * @param category The test suite for these tests
     */
    public TestAutostoreMany(final TestHarness category) {
        super(category, "TC79aa", "Test auto-store attribute for 1:M relations");
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
        AutostoreMainMany main = new AutostoreMainMany();
        main.setId(new Integer(100));
        main.setName("main.100");
        db.create(main);
        db.commit();
        
        db.begin();
        main = (AutostoreMainMany) db.load(AutostoreMainMany.class, new Integer(100));
        assertNotNull(main);
        assertEquals(100, main.getId().intValue());
        assertEquals("main.100", main.getName());
        assertNotNull(main.getAssociatedMany());
        assertEquals(0, main.getAssociatedMany().size());
        db.commit();

        db.begin();
        main = (AutostoreMainMany) db.load(AutostoreMainMany.class, new Integer(100));
        db.remove(main);
        db.commit();

        db.close();
    }

    public void testCreateWithEntityTwoNoAutoStore() throws Exception {
        Database db = _category.getDatabase();
        
        db.begin();
        AutostoreAssociatedMany entityTwo = new AutostoreAssociatedMany();
        entityTwo.setId(new Integer(200));
        entityTwo.setName("entity1.200");
        List associatedManys = Collections.singletonList(entityTwo);
        AutostoreMainMany entityOne = new AutostoreMainMany();
        entityOne.setId(new Integer(200));
        entityOne.setName("entity2.200");
        entityOne.setAssociatedMany(associatedManys);
        db.create(entityOne);
        db.create(entityTwo);
        db.commit();
        
        db.begin();
        entityOne = (AutostoreMainMany) 
            db.load(AutostoreMainMany.class, new Integer(200));
        assertNotNull(entityOne);
        assertEquals(200, entityOne.getId().intValue());
        assertEquals("entity2.200", entityOne.getName());
        assertNotNull(entityOne.getAssociatedMany());
        db.commit();

        db.begin();
        entityOne = (AutostoreMainMany) 
            db.load(AutostoreMainMany.class, new Integer(200));
        db.remove(entityOne);
        db.commit();

        db.begin();
        entityTwo = (AutostoreAssociatedMany) 
            db.load(AutostoreAssociatedMany.class, new Integer(200));
        db.remove(entityTwo);
        db.commit();

        db.close();
    }

    public void testCreateWithEntityTwoWithAutoStoreDeleteWithoutAutoStore()
    throws Exception {
        Database db = _category.getDatabase();
        db.setAutoStore(true);

        db.begin();
        AutostoreAssociatedMany entityTwo = new AutostoreAssociatedMany();
        entityTwo.setId(new Integer(300));
        entityTwo.setName("entity2.300");
        AutostoreMainMany entityOne = new AutostoreMainMany();
        entityOne.setId(new Integer(300));
        entityOne.setName("entity1.300");
        entityOne.setAssociatedMany(Collections.singletonList(entityTwo));
        db.create(entityOne);
        db.commit();
        
        db.begin();
        entityOne = (AutostoreMainMany) 
            db.load(AutostoreMainMany.class, new Integer(300));
        assertNotNull(entityOne);
        assertEquals(300, entityOne.getId().intValue());
        assertEquals("entity1.300", entityOne.getName());
        assertNotNull(entityOne.getAssociatedMany());
        
        List associatedManys = entityOne.getAssociatedMany();
        entityTwo = (AutostoreAssociatedMany) associatedManys.iterator().next();
        assertEquals(300, entityTwo.getId().intValue());
        assertEquals("entity2.300", entityTwo.getName());
        db.commit();
        
        db.setAutoStore(false);
        
        db.begin();
        entityOne = (AutostoreMainMany) 
            db.load(AutostoreMainMany.class, new Integer(300));
        db.remove(entityOne);
        db.commit();
        
        db.begin();
        entityTwo = (AutostoreAssociatedMany) 
            db.load(AutostoreAssociatedMany.class, new Integer(300));
        assertNotNull(entityTwo);
        assertEquals(300, entityTwo.getId().intValue());
        assertEquals("entity2.300", entityTwo.getName());
        
        try {
            entityOne = (AutostoreMainMany) 
                db.load(AutostoreMainMany.class, new Integer(300));
            fail("Expected ObjectNotFoundException");
        } catch (ObjectNotFoundException e) {
            //
        }
        db.commit();

        db.begin();
        entityTwo = (AutostoreAssociatedMany) 
            db.load(AutostoreAssociatedMany.class, new Integer(300));
        db.remove(entityTwo);
        db.commit();

        db.begin();
        try {
            entityTwo = (AutostoreAssociatedMany) 
                db.load(AutostoreAssociatedMany.class, new Integer(300));
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
        AutostoreAssociatedMany entityTwo = new AutostoreAssociatedMany();
        entityTwo.setId(new Integer(300));
        entityTwo.setName("entity2.300");
        List manys = new ArrayList();
        manys.add(entityTwo);
        AutostoreMainMany entityOne = new AutostoreMainMany();
        entityOne.setId(new Integer(300));
        entityOne.setName("entity1.300");
        entityOne.setAssociatedMany(manys);
        db.create(entityOne);
        db.commit();
        
        db.begin();
        entityOne = (AutostoreMainMany) 
            db.load(AutostoreMainMany.class, new Integer(300));
        assertNotNull(entityOne);
        assertEquals(300, entityOne.getId().intValue());
        assertEquals("entity1.300", entityOne.getName());
        assertNotNull(entityOne.getAssociatedMany());
        assertEquals(1, entityOne.getAssociatedMany().size());
        
        AutostoreAssociatedMany many = (AutostoreAssociatedMany) 
            entityOne.getAssociatedMany().iterator().next();
        assertEquals(300, many.getId().intValue());
        db.commit();

        db.begin();
        many = (AutostoreAssociatedMany) 
            db.load(AutostoreAssociatedMany.class, new Integer(300));
        assertNotNull(many);
        assertEquals(300, many.getId().intValue());
        assertEquals("entity2.300", many.getName());
        db.commit();
        
        db.begin();
        entityOne = (AutostoreMainMany) 
            db.load(AutostoreMainMany.class, new Integer(300));
        db.remove(entityOne);
        db.commit();
        
        db.begin();
        try {
            entityOne = (AutostoreMainMany) 
                db.load(AutostoreMainMany.class, new Integer(300));
            fail("Expected ObjectNotFoundException");
        } catch (ObjectNotFoundException e) {
            //
        }
        try {
            many = (AutostoreAssociatedMany) 
                db.load(AutostoreAssociatedMany.class, new Integer(300));
            // TODO remove once support for cascading delete has been added
//            fail("Expected ObjectNotFoundException");
        } catch (ObjectNotFoundException e) {
            //
        }
        db.commit();

        // TODO remove once support for cascading delete has been added
        db.begin();
        many = (AutostoreAssociatedMany) 
            db.load(AutostoreAssociatedMany.class, new Integer(300));
        db.remove(many);
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
                + AutostoreMainMany.class.getName() + " entity WHERE id = $1");
        query.bind(new Integer(1));
        QueryResults results = query.execute();
        
        AutostoreMainMany entity = (AutostoreMainMany) results.next();

        assertNotNull(entity);
        assertEquals(new Integer(1), entity.getId());
        
        List associatedManys = entity.getAssociatedMany();
        
        assertNotNull(associatedManys);
        
        Iterator iter = associatedManys.iterator();
        
        assertTrue(iter.hasNext());
        AutostoreAssociatedMany associatedMany = (AutostoreAssociatedMany) 
            iter.next();
        assertEquals(new Integer(1), associatedMany.getId());
        
        assertTrue(iter.hasNext());
        associatedMany = (AutostoreAssociatedMany) 
        iter.next();
        assertEquals(new Integer(2), associatedMany.getId());

        assertFalse(iter.hasNext());

        db.commit();
        db.close();
    }
}
