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
package org.castor.cpa.test.test80;

import java.util.Iterator;
import java.util.List;

import org.castor.cpa.test.framework.CPATestCase;
import org.castor.cpa.test.framework.xml.types.DatabaseEngineType;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.QueryResults;

/**
 * Tests that modification to read only objects are not persist in the database.
 */
public final class TestSelfReferentialExtend extends CPATestCase {
    private static final String DBNAME = "test80";
    private static final String MAPPING = "/org/castor/cpa/test/test80/mapping.xml";
    private Database _db;

    public TestSelfReferentialExtend(final String name) {
        super(name);
    }

    // Test are only included/excluded for engines that have been tested with this test suite.

    public boolean include(final DatabaseEngineType engine) {
        return (engine == DatabaseEngineType.DERBY)
            || (engine == DatabaseEngineType.MYSQL)
            || (engine == DatabaseEngineType.ORACLE)
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

    public void testQueryParent() throws Exception {
        _db.begin();

        OQLQuery query = _db.getOQLQuery("SELECT entity FROM "
                + SelfReferentialParent.class.getName()
                + " entity WHERE id = $1");
        query.bind(new Integer(1));
        QueryResults results = query.execute();

        SelfReferentialParent entity = (SelfReferentialParent) results.next();

        assertNotNull(entity);
        assertEquals(new Integer(1), entity.getId());

        List<SelfReferentialParent> children = entity.getEntities();
        assertNotNull(children);
        assertEquals(2, children.size());

        Iterator<SelfReferentialParent> childrenIterator = children.iterator();

        SelfReferentialParent child = childrenIterator.next();
        assertNotNull(child);
        assertEquals(2, child.getId().intValue());
        assertNotNull(child.getEntities());
        assertEquals(0, child.getEntities().size());

        child = childrenIterator.next();
        assertNotNull(child);
        assertEquals(3, child.getId().intValue());
        assertNotNull(child.getEntities());
        assertEquals(0, child.getEntities().size());

        _db.commit();
    }

    public void testLoadParent() throws Exception {
        _db.begin();

        Object obj = _db.load(SelfReferentialParent.class, new Integer(1));
        SelfReferentialParent entity = (SelfReferentialParent) obj;

        assertNotNull(entity);
        assertEquals(new Integer(1), entity.getId());

        List<SelfReferentialParent> children = entity.getEntities();
        assertNotNull(children);
        assertEquals(2, children.size());

        Iterator<SelfReferentialParent> childrenIterator = children.iterator();

        SelfReferentialParent child = childrenIterator.next();
        assertNotNull(child);
        assertEquals(2, child.getId().intValue());
        assertNotNull(child.getEntities());
        assertEquals(0, child.getEntities().size());

        child = childrenIterator.next();
        assertNotNull(child);
        assertEquals(3, child.getId().intValue());
        assertNotNull(child.getEntities());
        assertEquals(0, child.getEntities().size());

        _db.commit();
    }

    public void testLoadChild() throws Exception {
        _db.begin();

        Object obj = _db.load(SelfReferentialChild.class, new Integer(1));
        SelfReferentialChild child = (SelfReferentialChild) obj;

        assertNotNull(child);
        assertEquals(new Integer(1), child.getId());

        List<SelfReferentialParent> children = child.getEntities();
        assertNotNull(children);
        assertEquals(2, children.size());

        Iterator<SelfReferentialParent> childrenIterator = children.iterator();

        SelfReferentialParent entity = childrenIterator.next();
        assertNotNull(entity);
        assertEquals(2, entity.getId().intValue());
        assertNotNull(entity.getEntities());
        assertEquals(0, entity.getEntities().size());

        entity = childrenIterator.next();
        assertNotNull(entity);
        assertEquals(3, entity.getId().intValue());
        assertNotNull(entity.getEntities());
        assertEquals(0, entity.getEntities().size());

        _db.commit();
    }

}
