/*
 * Copyright 2009 Udai Gupta, Ralf Joachim
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
package org.castor.cpa.test.test95;

import org.castor.cpa.test.framework.CPATestCase;
import org.castor.cpa.test.framework.xml.types.DatabaseEngineType;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.ObjectNotFoundException;

/**
 * Tests that modification to read only objects are not persist in the _db.
 */
public final class TestPolymorphismWithKeyGen extends CPATestCase {
    private static final String DBNAME = "test95";
    private static final String MAPPING = "/org/castor/cpa/test/test95/mapping.xml";
    private Database _db;

    public TestPolymorphismWithKeyGen(final String name) {
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

    public void tearDown() throws Exception {
        if (_db.isActive()) {
            _db.rollback();
        }
        _db.close();
    }

    public void testCreateAndLoadLaptop() throws Exception {

        _db.begin();
        ProductDetail detail = new ProductDetail();
        detail.setId(10);
        detail.setCategory("category 10");
        detail.setLocation("location 10");
        _db.create(detail);
        _db.commit();

        _db.begin();
        LaptopKeyGen laptop = new LaptopKeyGen();
        laptop.setName("laptop 10");
        laptop.setCpu("centrino");
        laptop.setResolution("1600");
        laptop.setWeight(2750);
        laptop.setDetail(_db.load(ProductDetail.class,
            new Integer(10)));
        _db.create(laptop);
        _db.commit();

        int laptopId = laptop.getId();

        _db.begin();
        laptop = _db.load(LaptopKeyGen.class, new Integer(
                laptopId));
        _db.commit();

        assertNotNull(laptop);
        assertEquals("org.castor.cpa.test.test95.LaptopKeyGen", laptop.getClass().getName());
        assertEquals(laptopId, laptop.getId());
        assertEquals("laptop 10", laptop.getName());

        _db.begin();
        _db.remove(_db.load(LaptopKeyGen.class, new Integer(laptopId)));
        _db.commit();

        _db.begin();
        try {
            laptop = _db.load(LaptopKeyGen.class, new Integer(laptopId));
            fail("Laptop with id " + laptopId + " still exists.");
        } catch (ObjectNotFoundException e) {
            assertTrue("The object of type org.castor.cpa.test.test95.Laptop with identity "
                    + laptopId + " was not found in persistent storage", true);
        }

        _db.commit();
    }
}
