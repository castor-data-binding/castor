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
package org.castor.cpa.test.test84;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.castor.cpa.test.framework.CPATestCase;
import org.castor.cpa.test.framework.xml.types.DatabaseEngineType;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.QueryResults;

public final class TestTransientAttribute extends CPATestCase {
    private static final Log LOG = LogFactory.getLog(TestTransientAttribute.class);
    private static final String DBNAME = "test84";
    private static final String MAPPING = "/org/castor/cpa/test/test84/mapping.xml";
    private Database _db;

    public TestTransientAttribute(final String name) {
        super(name);
    }

    // Test are only included/excluded for engines that have been tested with
    // this test suite.

    public boolean include(final DatabaseEngineType engine) {
        return (engine == DatabaseEngineType.MYSQL)
            || (engine == DatabaseEngineType.DERBY);
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
    
    public void testTransientAttribute() throws Exception {
        _db.begin();
        
        TransientMaster entity = (TransientMaster) _db.load(
                TransientMaster.class, new Integer(1));
        assertNotNull(entity);
        assertEquals(new Integer(1), entity.getId());
        assertEquals("entity1", entity.getName());
        LOG.debug("loadedEntity.getProperty1() = " + entity.getProperty1());
        assertNull(entity.getProperty1());
        LOG.debug("loadedEntity.getProperty2() = " + entity.getProperty1());
        assertEquals(new Integer (2), entity.getProperty2());
        assertNull(entity.getProperty3());
        assertNull(entity.getEntityTwo());
        assertNull(entity.getEntityThrees());
        
        _db.rollback();
    
        _db.begin();
        
        OQLQuery query = _db.getOQLQuery("SELECT entity FROM "
                + TransientMaster.class.getName() + " entity WHERE id = $1");
        query.bind(new Integer(1));
        QueryResults results = query.execute();
        
        entity = (TransientMaster) results.next();

        assertNotNull(entity);
        assertEquals(new Integer(1), entity.getId());
        assertEquals("entity1", entity.getName());
        LOG.debug("loadedEntity.getProperty1() = " + entity.getProperty1());
        assertNull(entity.getProperty1());
        LOG.debug("loadedEntity.getProperty2() = " + entity.getProperty1());
        assertEquals(new Integer (2), entity.getProperty2());
        assertNull(entity.getProperty3());
        assertNull(entity.getEntityTwo());
        assertNull(entity.getEntityThrees());
        
        _db.rollback();

        _db.begin();

        TransientChildOne entityTwo = new TransientChildOne();
        entityTwo.setId(new Integer (200));
        entityTwo.setDescription("entity200");
        
        entity = new TransientMaster();
        entity.setId(new Integer (100));
        entity.setName("entity100");
        entity.setProperty1(new Integer (100));
        entity.setProperty2(new Integer (200));
        entity.setProperty3(new Integer (300));
        entity.setEntityTwo(entityTwo);
        _db.create(entity);
        
        _db.commit();

        _db.begin();
        
        TransientMaster loadedEntity = (TransientMaster) _db.load(
                TransientMaster.class, new Integer (100));
        assertNotNull(loadedEntity);
        assertEquals(new Integer(100), loadedEntity.getId());
        assertEquals("entity100", loadedEntity.getName());
        
        LOG.debug("loadedEntity.getProperty() = " + loadedEntity.getProperty1());
        
        assertNull(loadedEntity.getProperty1());
        LOG.debug("loadedEntity.getProperty2() = " + loadedEntity.getProperty2());
        assertEquals(new Integer (200), loadedEntity.getProperty2());
        LOG.debug("loadedEntity.getProperty3() = " + loadedEntity.getProperty3());
        assertNull(loadedEntity.getProperty3());
        assertNull(loadedEntity.getEntityTwo());
        assertNull(loadedEntity.getEntityThrees());
        
        _db.commit();

        _db.begin();
        
        entity = (TransientMaster) _db.load(TransientMaster.class, new Integer (1));
        assertNotNull(entity);
        assertEquals(new Integer(1), entity.getId());
        assertEquals("entity1", entity.getName());
        assertNull(entity.getProperty1());
        assertEquals(new Integer (2), entity.getProperty2());
        assertNull(entity.getEntityTwo());
        assertNull(entity.getEntityThrees());
        
        entity.setProperty2(new Integer (-2));
        
        _db.commit();

        _db.begin();
        
        entity = (TransientMaster) _db.load(TransientMaster.class, new Integer (1));
        assertNotNull(entity);
        assertEquals(new Integer(1), entity.getId());
        assertEquals("entity1", entity.getName());
        assertNull(entity.getProperty1());
        assertEquals(new Integer (-2), entity.getProperty2());
        assertNull(entity.getProperty3());
        assertNull(entity.getEntityTwo());
        assertNull(entity.getEntityThrees());
        
        entity.setProperty2(new Integer (2));
        
        _db.commit();
    }
}
