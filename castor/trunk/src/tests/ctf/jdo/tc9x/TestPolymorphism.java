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

import java.math.BigDecimal;
import java.util.Collection;
import java.util.LinkedList;

import harness.CastorTestCase;
import harness.TestHarness;

import jdo.JDOCategory;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.ObjectNotFoundException;
import org.exolab.castor.jdo.QueryResults;
import org.exolab.castor.persist.spi.Complex;

/**
 * Tests that modification to read only objects are not persist in the 
 * database.
 */
public final class TestPolymorphism extends CastorTestCase {
    
    private JDOCategory _category;

    /**
     * Constructor
     * @param category the test suite that this test case belongs
     */
    public TestPolymorphism(final TestHarness category) {
        super(category, "TC97", "Polymorphism tests");
        _category = (JDOCategory) category;
    }

    /**
     * Tests that modification to read only objects are not persist in the 
     * database.
     */
    public void runTest() throws Exception {
        testLoadLaptop();
        testCreateAndLoadLaptop();
        testLoadLaptopMulti();
        testLoadServer();
        testLoadServerMulti();
        testLoadComputer();
        testCreateAndLoadComputer();
        testLoadComputerMulti();
        testLoadTruck();
        testLoadCar();
        testCreateAndLoadCar();
        testLoadOwner();
        testLoadM();
        testQueryOwner();
        testQueryComputers();
        testQueryLaptops();
        testQueryServers();
        testQueryProducts();
        testOQLQueryWithParameter();
//        testOQLQueryWithoutParameter();
    }

    public void testLoadLaptop() throws Exception {
        Database database = _category.getDatabase();
        
        database.begin();
        Laptop laptop = (Laptop) database.load(Laptop.class, new Integer(1));
        database.commit();
        
        assertNotNull(laptop);
        assertEquals("ctf.jdo.tc9x.Laptop", laptop.getClass().getName());
        assertEquals(1, laptop.getId());
        assertEquals("laptop 1", laptop.getName());
        
        database.close();
    }

    public void testCreateAndLoadLaptop() throws Exception {
        Database database = _category.getDatabase();
        
        database.begin();
        ProductDetail detail = new ProductDetail();
        detail.setId(10);
        detail.setCategory("category 10");
        detail.setLocation("location 10");
        database.create(detail);
        database.commit();
        
        
        database.begin();
        Laptop laptop = new Laptop();
        laptop.setId(10);
        laptop.setName("laptop 10");
        laptop.setCpu("centrino");
        laptop.setResolution("1600");
        laptop.setWeight(2750);
        laptop.setDetail((ProductDetail) database.load(
                ProductDetail.class, new Integer(10)));
        database.create(laptop);

        Owner owner = new Owner ();
        owner.setId(new Integer (10));
        owner.setName("owner 10");
        owner.setProduct(laptop);
        database.commit();
        
        database.begin();
        laptop = (Laptop) database.load (Laptop.class, new Integer(10));
        database.commit();
        
        assertNotNull (laptop);
        assertEquals("ctf.jdo.tc9x.Laptop", laptop.getClass().getName());
        assertEquals(10, laptop.getId());
        assertEquals("laptop 10", laptop.getName());
        
        database.begin();
        database.remove(database.load(Laptop.class, new Integer(10)));
        database.remove(database.load(ProductDetail.class, new Integer(10)));
        database.commit();
        
        database.begin();
        try {
            laptop = (Laptop) database.load(Laptop.class, new Integer(10));
            fail("Laptop with id 10 still exists.");
        } catch (ObjectNotFoundException e) {
           assertEquals("The object of type ctf.jdo.tc9x.Laptop with identity 10 " 
                   + "was not found in persistent storage", e.getMessage());
        }
        database.commit();
        
        database.close();
    }

    public void testLoadLaptopMulti() throws Exception {
        Database database = _category.getDatabase();
        
        database.begin();
        LaptopMulti laptop = (LaptopMulti) database.load(LaptopMulti.class,
                new Complex(new Integer (1), new Integer(1)));
        database.commit();
        
        assertNotNull(laptop);
        assertEquals("ctf.jdo.tc9x.LaptopMulti", laptop.getClass().getName());
        assertEquals("laptop 1", laptop.getName());
        assertEquals(1, laptop.getId1());
        assertEquals(1, laptop.getId2());
        
        database.close();
    }

    public void testLoadServer() throws Exception {
        Database database = _category.getDatabase();
        
        database.begin();
        Server server = (Server) database.load(Server.class, new Integer(3));
        database.commit();
        
        assertNotNull(server);
        assertEquals("ctf.jdo.tc9x.Server", server.getClass().getName());
        assertEquals(3, server.getId());
        assertEquals("server 3", server.getName());
        
        database.close();
    }

    public void testLoadServerMulti() throws Exception {
        Database database = _category.getDatabase();
        
        database.begin();
        ServerMulti server = (ServerMulti) database.load(ServerMulti.class,
                new Complex(new Integer(3), new Integer(3)));
        database.commit();
        
        assertNotNull(server);
        assertEquals("ctf.jdo.tc9x.ServerMulti", server.getClass().getName());
        assertEquals(3, server.getId1());
        assertEquals(3, server.getId2());
        assertEquals("server 3", server.getName());
        
        database.close();
    }
    
    public void testLoadComputer() throws Exception {
        Database database = _category.getDatabase();
        
        database.begin();
        Computer computer = (Computer) database.load(Computer.class, new Integer(2));
        database.commit();
        
        assertNotNull(computer);
        assertEquals("ctf.jdo.tc9x.Laptop", computer.getClass().getName());
        assertEquals(2, computer.getId());
        assertEquals("laptop 2", computer.getName());
        
        database.close();
    }
    
    public void testCreateAndLoadComputer() throws Exception {
        Database database = _category.getDatabase();

        database.begin();
        Order order = new Order();
        order.setId(12);
        order.setName("order 12");
        database.create(order);
        database.commit();

        database.begin();
        ProductDetail detail = new ProductDetail();
        detail.setId(12);
        detail.setCategory("category 12");
        detail.setLocation("location 12");
        database.create(detail);
        database.commit();
        
        database.begin();
        Laptop laptop = new Laptop();
        laptop.setId(12);
        laptop.setName("laptop 12");
        laptop.setCpu("centrino");
        laptop.setResolution("1600");
        laptop.setWeight(2450);
        laptop.setDetail((ProductDetail) 
                database.load(ProductDetail.class, new Integer(12)));
        database.create(laptop);
        Collection orders = new LinkedList();
        orders.add(database.load(Order.class, new Integer(12)));
        laptop.setOrders(orders);
        database.commit();

        database.begin();
        Computer computer = (Computer) database.load(Computer.class, new Integer(12));
        database.commit();
        
        assertNotNull(computer);
        assertEquals("ctf.jdo.tc9x.Laptop", computer.getClass().getName());
        assertEquals(12, computer.getId());
        assertEquals("laptop 12", computer.getName());
        
        database.begin();
        computer = (Computer) database.load(Computer.class, new Integer(12)); 
        database.remove(computer);
        database.remove(database.load (ProductDetail.class, new Integer(12)));
        database.remove(database.load(Order.class, new Integer(12)));
        database.commit();
        
        database.close();
    }

    public void testLoadComputerMulti() throws Exception {
        Database database = _category.getDatabase();
        
        database.begin();
        ComputerMulti computer = (LaptopMulti) database.load(ComputerMulti.class,
                new Complex(new Integer(1), new Integer(1)));
        database.commit();
        
        assertNotNull(computer);
        assertEquals(1, computer.getId1());
        assertEquals(1, computer.getId2());
        assertEquals("laptop 1", computer.getName());
        
        database.close();
    }

    public void testLoadTruck() throws Exception {
        Database database = _category.getDatabase();
        
        database.begin();
        Truck truck = (Truck) database.load(Truck.class, new Integer(5));
        database.commit();
        
        assertNotNull(truck);
        assertEquals(5, truck.getId());
        
        database.close();
    }

    public void testLoadCar() throws Exception {
        Database database = _category.getDatabase();
        
        database.begin();
        Object object = database.load(Car.class, new Integer(5));
        assertNotNull (object);
        assertEquals ("ctf.jdo.tc9x.Truck", object.getClass().getName());
        Truck truck = (Truck) object; 
        database.commit();
        
        assertNotNull(truck);
        assertEquals(5, truck.getId());
        
        database.close();
    }

    public void testCreateAndLoadCar() throws Exception {
        Database database = _category.getDatabase();

        database.begin();
        Order order = new Order();
        order.setId(11);
        order.setName("order 11");
        database.create(order);
        database.commit();
        
        database.begin();
        ProductDetail detail = new ProductDetail();
        detail.setId(11);
        detail.setCategory("category 11");
        detail.setLocation("location 11");
        database.create(detail);
        database.commit();
        
        database.begin();
        Truck truck = new Truck();
        truck.setId(11);
        truck.setName("truck 11");
        truck.setKw(112);
        truck.setMake("Fiat");
        truck.setMaxWeight(3750);
        truck.setDetail((ProductDetail) 
                database.load(ProductDetail.class, new Integer(11)));
        database.create(truck);
        Collection orders = new LinkedList();
        orders.add(database.load(Order.class, new Integer(11)));
        truck.setOrders(orders);
        database.commit();
        
        database.begin();
        Object object = database.load(Car.class, new Integer(11));
        assertNotNull(object);
        assertEquals("ctf.jdo.tc9x.Truck", object.getClass().getName());
        Truck loadedTruck = (Truck) object; 
        database.commit();
        
        assertNotNull(loadedTruck);
        assertEquals(11, loadedTruck.getId());
        
        database.begin();
        database.remove(database.load (Car.class, new Integer(11)));
        database.remove(database.load (ProductDetail.class, new Integer(11)));
        database.remove(database.load(Order.class, new Integer(11)));
        database.commit();
        
        database.close();
    }

    public void testLoadOwner() throws Exception {
        Database database = _category.getDatabase();
        
        database.begin();
        Owner owner = (Owner) database.load(Owner.class, new Integer(1));
        database.commit();
        
        assertNotNull(owner);
        assertEquals(1, owner.getId().intValue());
        assertEquals("owner 1", owner.getName());
        
        Product product = owner.getProduct();
        assertNotNull(product);
        assertEquals(1, product.getId());
        assertEquals("ctf.jdo.tc9x.Laptop", product.getClass().getName());
        
        database.close();
    }
   
    public void testLoadM() throws Exception {
        Database database = _category.getDatabase();
        
        database.begin();
        M m = (M) database.load(M.class, new Integer(1));
        database.commit();
        
        assertNotNull(m);
        assertEquals(1, m.getId());
        assertEquals("m1", m.getName());
        
        Collection ns = m.getNs();
        assertNotNull(ns);
        assertEquals(2, ns.size());
        
        database.close();
    }

    public void testQueryOwner () throws Exception {
        Database database = _category.getDatabase();
        
        database.begin();
        OQLQuery query = database.getOQLQuery("select owner from "
                + Owner.class.getName() + " as owner");
        QueryResults results = query.execute();
        
        if (results.hasMore()) {
            int counter = 1;
            while (results.hasMore()) {
                Owner owner = (Owner) results.next();
                assertNotNull(owner);
                assertEquals(counter, owner.getId().intValue());
                
                counter += 1;
            }
        } else {
            fail("Query does not return any Computer instances.");
        }
        database.commit();
        
        database.close();
    }

    public void testQueryComputers () throws Exception {
        String[] classNames = { 
                "ctf.jdo.tc9x.Laptop", 
                "ctf.jdo.tc9x.Laptop",
                "ctf.jdo.tc9x.Server",
                "ctf.jdo.tc9x.Server"
        };
        
        Database database = _category.getDatabase();
        
        database.begin();
        OQLQuery query = database.getOQLQuery("select computer from "
                + Computer.class.getName() + " as computer order by computer.id");
        QueryResults results = query.execute();
        
        if (results.hasMore()) {
            int counter = 1;
            while (results.hasMore()) {
                Computer computer = (Computer) results.next();
                assertNotNull(computer);
                assertEquals(counter, computer.getId());
                assertEquals(classNames[counter - 1], computer.getClass().getName());
                
                counter += 1;
            }
        } else {
            fail("Query does not return any Computer instances.");
        }
        database.commit();
        
        database.close();
    }

    public void testQueryLaptops () throws Exception {
        Database database = _category.getDatabase();
        
        database.begin();
        OQLQuery query = database.getOQLQuery("select l from "
                + Laptop.class.getName() + " as l order by l.id");
        QueryResults results = query.execute();
        
        if (results.hasMore()) {
            int counter = 1;
            Laptop laptop = null;
            while (results.hasMore()) {
                laptop = (Laptop) results.next();
                assertNotNull(laptop);
                assertEquals(counter, laptop.getId());
                
                counter += 1;
            }
        } else {
            fail("Query does not return any Laptop instances.");
        }
        database.commit();
        
        database.close();
    }
    
    public void testQueryServers () throws Exception {
        Database database = _category.getDatabase();
        
        database.begin();
        OQLQuery query = database.getOQLQuery("select s from "
                + Server.class.getName() + " as s order by s.id");
        QueryResults results = query.execute();
        
        if (results.hasMore()) {
            int counter = 3;
            while (results.hasMore()) {
                Server server = (Server) results.next();
                assertNotNull(server);
                assertEquals(counter, server.getId());
                
                counter += 1;
            }
        } else {
            fail("Query does not return any Server instances.");
        }
        database.commit();
        
        database.close();
    }

    public void testQueryProducts () throws Exception {
        String[] classNames = { 
                "ctf.jdo.tc9x.Laptop", 
                "ctf.jdo.tc9x.Laptop",
                "ctf.jdo.tc9x.Server",
                "ctf.jdo.tc9x.Server",
                "ctf.jdo.tc9x.Truck"
        };
        
        Database database = _category.getDatabase();
        
        database.begin();
        OQLQuery query = database.getOQLQuery("select product from "
                + Product.class.getName() + " as product order by product.id");
        QueryResults results = query.execute();
        
        if (results.hasMore()) {
            int counter = 1;
            while (results.hasMore()) {
                Product product = (Product) results.next();
                assertNotNull(product);
                assertEquals(counter, product.getId());
                assertEquals(classNames[counter - 1], product.getClass().getName());
                
                counter += 1;
            }
        } else {
            fail("Query does not return any Product instances.");
        }
        database.commit();
        
        database.close();
    }
    
    public void testOQLQueryWithParameter () throws Exception {
        Database database = _category.getDatabase();
        
        database.begin();
        OQLQuery query = database.getOQLQuery("SELECT count(laptop.id) FROM "
                + Laptop.class.getName() + " laptop WHERE laptop.resolution = $1");
        query.bind("1024");
        QueryResults results = query.execute();
        
        if (results.hasMore()) {
            Object obj = results.next();
            Long count = null;
            if (obj instanceof Long) {
                count = (Long) obj;
            } else if (obj instanceof Integer) {
                count = new Long(((Integer) obj).intValue());
            } else if (obj instanceof BigDecimal) {
                count = new Long(((BigDecimal) obj).longValue());
            }
            assertNotNull(count);
            assertEquals(1, count.intValue());
        }
        
        database.commit();
        
        database.close();
    }
    
//    public void testOQLQueryWithoutParameter () throws Exception {
//        Database database = _category.getDatabase();
//        
//        database.begin();
//        OQLQuery query = database.getOQLQuery("SELECT count(laptop.id) FROM " 
//                + Laptop.class.getName() + " laptop WHERE laptop.resolution = '1024'");
//        QueryResults results = query.execute();
//        
//        if (results.hasMore()) {
//            Long count = (Long) results.next();
//            assertNotNull(count);
//            assertEquals(1, count.intValue());
//        }
//
//        database.commit();
//        
//        database.close();
//    }
}
