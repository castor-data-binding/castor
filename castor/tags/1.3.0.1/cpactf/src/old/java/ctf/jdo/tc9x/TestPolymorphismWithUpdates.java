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
package ctf.jdo.tc9x;

import harness.CastorTestCase;
import harness.TestHarness;
import jdo.JDOCategory;

import org.exolab.castor.jdo.Database;

/**
 * Tests that modification to read only objects are not persist in the 
 * database.
 */
public final class TestPolymorphismWithUpdates extends CastorTestCase {
    
    private JDOCategory _category;

    /**
     * Constructor
     * @param category the test suite that this test case belongs
     */
    public TestPolymorphismWithUpdates(final TestHarness category) {
        super(category, "TC94", "Polymorphism tests");
        _category = (JDOCategory) category;
    }

    /**
     * Tests that modification to read only objects are not persist in the 
     * database.
     */
    public void runTest() throws Exception {
        testLoadAndUpdateLaptop();
    }

    public void testLoadAndUpdateLaptop() throws Exception {
        Database database = _category.getDatabase();
        
        database.begin();
        Laptop laptop = (Laptop) database.load(Laptop.class, new Integer(1));
        database.commit();
        
        assertNotNull(laptop);
        assertEquals("ctf.jdo.tc9x.Laptop", laptop.getClass().getName());
        assertEquals(1, laptop.getId());
        assertEquals("laptop 1", laptop.getName());
        
        laptop.setName("laptop 1x");

        database.begin();
        database.update(laptop);
        database.commit();
        
        database.begin();
        laptop = (Laptop) database.load(Laptop.class, new Integer(1));
        database.commit();
        
        assertNotNull(laptop);
        assertEquals("ctf.jdo.tc9x.Laptop", laptop.getClass().getName());
        assertEquals(1, laptop.getId());
        assertEquals("laptop 1x", laptop.getName());

        laptop.setName("laptop 1");

        database.begin();
        database.update(laptop);
        database.commit();
        
        database.begin();
        laptop = (Laptop) database.load(Laptop.class, new Integer(1));
        database.commit();

        assertNotNull(laptop);
        assertEquals("ctf.jdo.tc9x.Laptop", laptop.getClass().getName());
        assertEquals(1, laptop.getId());
        assertEquals("laptop 1", laptop.getName());

        database.close();
    }

    public void testLoadAndUpdateLaptopRollback() throws Exception {
        Database database = _category.getDatabase();
        
        database.begin();
        Laptop laptop = (Laptop) database.load(Laptop.class, new Integer(1));
        database.commit();
        
        assertNotNull(laptop);
        assertEquals("ctf.jdo.tc9x.Laptop", laptop.getClass().getName());
        assertEquals(1, laptop.getId());
        assertEquals("laptop 1", laptop.getName());
        
        laptop.setName("laptop 1x");

        database.begin();
        database.update(laptop);
        database.commit();
        
        database.begin();
        laptop = (Laptop) database.load(Laptop.class, new Integer(1));
        database.commit();
        
        assertNotNull(laptop);
        assertEquals("ctf.jdo.tc9x.Laptop", laptop.getClass().getName());
        assertEquals(1, laptop.getId());
        assertEquals("laptop 1x", laptop.getName());

        laptop.setName("laptop 1");

        database.begin();
        database.update(laptop);
        database.rollback();
        
        database.begin();
        laptop = (Laptop) database.load(Laptop.class, new Integer(1));
        database.commit();

        assertNotNull(laptop);
        assertEquals("ctf.jdo.tc9x.Laptop", laptop.getClass().getName());
        assertEquals(1, laptop.getId());
        assertEquals("laptop 1x", laptop.getName());

        laptop.setName("laptop 1");

        database.begin();
        database.update(laptop);
        database.commit();
        
        database.begin();
        laptop = (Laptop) database.load(Laptop.class, new Integer(1));
        database.commit();

        assertNotNull(laptop);
        assertEquals("ctf.jdo.tc9x.Laptop", laptop.getClass().getName());
        assertEquals(1, laptop.getId());
        assertEquals("laptop 1", laptop.getName());
        database.close();
    }

    public void testLoadAndUpdateCar() throws Exception {
        Database database = _category.getDatabase();
        
        database.begin();
        Truck truck = (Truck) database.load(Truck.class, new Integer(5));
        database.commit();
        
        assertNotNull(truck);
        assertEquals("ctf.jdo.tc9x.Truck", truck.getClass().getName());
        assertEquals(5, truck.getId());
        assertEquals("truck 5", truck.getName());
        
        truck.setName("truck 5t");

        database.begin();
        database.update(truck);
        database.commit();
        
        database.begin();
        truck = (Truck) database.load(Truck.class, new Integer(5));
        database.commit();
        
        assertNotNull(truck);
        assertEquals("ctf.jdo.tc9x.Truck", truck.getClass().getName());
        assertEquals(5, truck.getId());
        assertEquals("truck 5t", truck.getName());

        truck.setName("truck 5");

        database.begin();
        database.update(truck);
        database.commit();
        
        database.begin();
        truck = (Truck) database.load(Truck.class, new Integer(5));
        database.commit();

        assertNotNull(truck);
        assertEquals("ctf.jdo.tc9x.Truck", truck.getClass().getName());
        assertEquals(5, truck.getId());
        assertEquals("truck 5", truck.getName());

        database.close();
    }

    public void testLoadAndUpdateTruck() throws Exception {
        Database database = _category.getDatabase();
        
        database.begin();
        Car car = (Car) database.load(Car.class, new Integer(5));
        database.commit();
        
        assertNotNull(car);
        assertEquals("ctf.jdo.tc9x.Car", car.getClass().getName());
        assertEquals(5, car.getId());
        assertEquals("truck 5", car.getName());
        
        car.setName("truck 5t");

        database.begin();
        database.update(car);
        database.commit();
        
        database.begin();
        car = (Car) database.load(Car.class, new Integer(5));
        database.commit();
        
        assertNotNull(car);
        assertEquals("ctf.jdo.tc9x.Car", car.getClass().getName());
        assertEquals(5, car.getId());
        assertEquals("truck 5t", car.getName());

        car.setName("truck 5");

        database.begin();
        database.update(car);
        database.commit();
        
        database.begin();
        car = (Car) database.load(Car.class, new Integer(5));
        database.commit();

        assertNotNull(car);
        assertEquals("ctf.jdo.tc9x.Car", car.getClass().getName());
        assertEquals(5, car.getId());
        assertEquals("truck 5", car.getName());

        database.close();
    }

    public void testLoadAndUpdateProduct() throws Exception {
        Database database = _category.getDatabase();
        
        database.begin();
        Product product = (Product) database.load(Product.class, new Integer(5));
        database.commit();
        
        assertNotNull(product);
        assertEquals("ctf.jdo.tc9x.Product", product.getClass().getName());
        assertEquals(5, product.getId());
        assertEquals("truck 5", product.getName());
        
        product.setName("truck 5t");

        database.begin();
        database.update(product);
        database.commit();
        
        database.begin();
        product = (Product) database.load(Product.class, new Integer(5));
        database.commit();
        
        assertNotNull(product);
        assertEquals("ctf.jdo.tc9x.Product", product.getClass().getName());
        assertEquals(5, product.getId());
        assertEquals("truck 5t", product.getName());

        product.setName("truck 5");

        database.begin();
        database.update(product);
        database.commit();
        
        database.begin();
        product = (Product) database.load(Product.class, new Integer(5));
        database.commit();

        assertNotNull(product);
        assertEquals("ctf.jdo.tc9x.Product", product.getClass().getName());
        assertEquals(5, product.getId());
        assertEquals("truck 5", product.getName());

        database.close();
    }
}
