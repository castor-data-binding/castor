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
package org.castor.cpa.test.test94;

import org.castor.cpa.test.framework.CPATestCase;
import org.castor.cpa.test.framework.xml.types.DatabaseEngineType;
import org.exolab.castor.jdo.Database;

/**
 * Tests that modification to read only objects are not persist in the 
 * _db.
 */
public final class TestPolymorphismWithUpdates extends CPATestCase {
    private static final String DBNAME = "test94";
    private static final String MAPPING = "/org/castor/cpa/test/test94/mapping.xml";
    private Database _db;

    public TestPolymorphismWithUpdates(final String name) {
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

    public void testLoadAndUpdateLaptop() throws Exception {
        _db.begin();
        Laptop laptop = _db.load(Laptop.class, new Integer(1));
        _db.commit();
        
        assertNotNull(laptop);
        assertEquals("org.castor.cpa.test.test94.Laptop", laptop.getClass().getName());
        assertEquals(1, laptop.getId());
        assertEquals("laptop 1", laptop.getName());
        
        laptop.setName("laptop 1x");

        _db.begin();
        _db.update(laptop);
        _db.commit();
        
        _db.begin();
        laptop = _db.load(Laptop.class, new Integer(1));
        _db.commit();
        
        assertNotNull(laptop);
        assertEquals("org.castor.cpa.test.test94.Laptop", laptop.getClass().getName());
        assertEquals(1, laptop.getId());
        assertEquals("laptop 1x", laptop.getName());

        laptop.setName("laptop 1");

        _db.begin();
        _db.update(laptop);
        _db.commit();
        
        _db.begin();
        laptop =  _db.load(Laptop.class, new Integer(1));
        _db.commit();

        assertNotNull(laptop);
        assertEquals("org.castor.cpa.test.test94.Laptop", laptop.getClass().getName());
        assertEquals(1, laptop.getId());
        assertEquals("laptop 1", laptop.getName());

        _db.close();
    }

    public void testLoadAndUpdateLaptopRollback() throws Exception {
        _db.begin();
        Laptop laptop =  _db.load(Laptop.class, new Integer(1));
        _db.commit();
        
        assertNotNull(laptop);
        assertEquals("org.castor.cpa.test.test94.Laptop", laptop.getClass().getName());
        assertEquals(1, laptop.getId());
        assertEquals("laptop 1", laptop.getName());
        
        laptop.setName("laptop 1x");

        _db.begin();
        _db.update(laptop);
        _db.commit();
        
        _db.begin();
        laptop =  _db.load(Laptop.class, new Integer(1));
        _db.commit();
        
        assertNotNull(laptop);
        assertEquals("org.castor.cpa.test.test94.Laptop", laptop.getClass().getName());
        assertEquals(1, laptop.getId());
        assertEquals("laptop 1x", laptop.getName());

        laptop.setName("laptop 1");

        _db.begin();
        _db.update(laptop);
        _db.rollback();
        
        _db.begin();
        laptop =  _db.load(Laptop.class, new Integer(1));
        _db.commit();

        assertNotNull(laptop);
        assertEquals("org.castor.cpa.test.test94.Laptop", laptop.getClass().getName());
        assertEquals(1, laptop.getId());
        assertEquals("laptop 1x", laptop.getName());

        laptop.setName("laptop 1");

        _db.begin();
        _db.update(laptop);
        _db.commit();
        
        _db.begin();
        laptop =  _db.load(Laptop.class, new Integer(1));
        _db.commit();

        assertNotNull(laptop);
        assertEquals("org.castor.cpa.test.test94.Laptop", laptop.getClass().getName());
        assertEquals(1, laptop.getId());
        assertEquals("laptop 1", laptop.getName());
    }

    public void testLoadAndUpdateCar() throws Exception {
        _db.begin();
        Truck truck =  _db.load(Truck.class, new Integer(5));
        _db.commit();
        
        assertNotNull(truck);
        assertEquals("org.castor.cpa.test.test94.Truck", truck.getClass().getName());
        assertEquals(5, truck.getId());
        assertEquals("truck 5", truck.getName());
        
        truck.setName("truck 5t");

        _db.begin();
        _db.update(truck);
        _db.commit();
        
        _db.begin();
        truck =  _db.load(Truck.class, new Integer(5));
        _db.commit();
        
        assertNotNull(truck);
        assertEquals("org.castor.cpa.test.test94.Truck", truck.getClass().getName());
        assertEquals(5, truck.getId());
        assertEquals("truck 5t", truck.getName());

        truck.setName("truck 5");

        _db.begin();
        _db.update(truck);
        _db.commit();
        
        _db.begin();
        truck =  _db.load(Truck.class, new Integer(5));
        _db.commit();

        assertNotNull(truck);
        assertEquals("org.castor.cpa.test.test94.Truck", truck.getClass().getName());
        assertEquals(5, truck.getId());
        assertEquals("truck 5", truck.getName());
    }

    //FIXME this test was excluded in old cpacft
    public void xtestLoadAndUpdateTruck() throws Exception {
        
        _db.begin();
        Car car =  _db.load(Car.class, new Integer(5));
        _db.commit();
        
        assertNotNull(car);
        assertEquals("org.castor.cpa.test.test94.Car", car.getClass().getName());
        assertEquals(5, car.getId());
        assertEquals("truck 5", car.getName());
        
        car.setName("truck 5t");

        _db.begin();
        _db.update(car);
        _db.commit();
        
        _db.begin();
        car =  _db.load(Car.class, new Integer(5));
        _db.commit();
        
        assertNotNull(car);
        assertEquals("org.castor.cpa.test.test94.Car", car.getClass().getName());
        assertEquals(5, car.getId());
        assertEquals("truck 5t", car.getName());

        car.setName("truck 5");

        _db.begin();
        _db.update(car);
        _db.commit();
        
        _db.begin();
        car =  _db.load(Car.class, new Integer(5));
        _db.commit();

        assertNotNull(car);
        assertEquals("org.castor.cpa.test.test94.Car", car.getClass().getName());
        assertEquals(5, car.getId());
        assertEquals("truck 5", car.getName());

    }

    //FIXME this test was excluded in old cpacft
    public void xtestLoadAndUpdateProduct() throws Exception {
        
        _db.begin();
        Product product = _db.load(Product.class, new Integer(5));
        _db.commit();
        
        assertNotNull(product);
        assertEquals("org.castor.cpa.test.test94.Product", product.getClass().getName());
        assertEquals(5, product.getId());
        assertEquals("truck 5", product.getName());
        
        product.setName("truck 5t");

        _db.begin();
        _db.update(product);
        _db.commit();
        
        _db.begin();
        product =  _db.load(Product.class, new Integer(5));
        _db.commit();
        
        assertNotNull(product);
        assertEquals("org.castor.cpa.test.test94.Product", product.getClass().getName());
        assertEquals(5, product.getId());
        assertEquals("truck 5t", product.getName());

        product.setName("truck 5");

        _db.begin();
        _db.update(product);
        _db.commit();
        
        _db.begin();
        product =  _db.load(Product.class, new Integer(5));
        _db.commit();

        assertNotNull(product);
        assertEquals("org.castor.cpa.test.test94.Product", product.getClass().getName());
        assertEquals(5, product.getId());
        assertEquals("truck 5", product.getName());
    }
}
