/*
 * Copyright 2008 Udai Gupta, Ralf Joachim
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
package org.castor.cpa.test.test71;

import java.util.List;

import org.castor.cpa.test.framework.CPATestCase;
import org.castor.cpa.test.framework.xml.types.DatabaseEngineType;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.QueryResults;

/**
 * Test for different collection types supported by Castor JDO.
 * This test creates data objects that each has a collection as
 * a field type.
 */
public final class TestSpecialCollections extends CPATestCase {
    private static final String DBNAME = "test71";
    private static final String MAPPING = "/org/castor/cpa/test/test71/mapping.xml";
    private Database _db;

    public TestSpecialCollections(final String name) {
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
        if (_db.isActive()) { _db.rollback(); }
        _db.close();
    }
    
    /**
     * Test method.
     * @throws Exception For any exception thrown.
     */
    public void testQueryEntityOne() throws Exception {

        _db.begin();
        OQLQuery aquery = _db.getOQLQuery(
                "SELECT c FROM " + Container.class.getName() + " c order by c.id");
        QueryResults aresults = aquery.execute();
        int i = 1;
        while (aresults.hasMore()) {
            Container container = (Container) aresults.next();
            assertNotNull(container);
            assertEquals(new Integer(i), container.getId());
            
            List<ContainerItem> containerItems = container.getProp();
            assertNotNull(containerItems);
            assertTrue(containerItems.size() > 0);
            i++;
        }
        _db.commit();
    }
}
