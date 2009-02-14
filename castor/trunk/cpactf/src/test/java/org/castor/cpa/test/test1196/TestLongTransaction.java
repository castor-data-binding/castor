package org.castor.cpa.test.test1196;

import java.sql.Connection;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.castor.cpa.test.framework.CPATestCase;
import org.castor.cpa.test.framework.xml.types.DatabaseEngineType;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.JDOManager;
import org.exolab.castor.jdo.ObjectModifiedException;

public final class TestLongTransaction extends CPATestCase {
    private static final String DBNAME = "test1196";
    private static final String MAPPING = "/org/castor/cpa/test/test1196/mapping.xml";

    public static Test suite() throws Exception {
        TestSuite suite = new TestSuite(TestLongTransaction.class.getName());

        suite.addTest(new TestLongTransaction("deleteUnidirectional"));
        suite.addTest(new TestLongTransaction("createUnidirectional"));
        suite.addTest(new TestLongTransaction("changeUnidirectional"));
        suite.addTest(new TestLongTransaction("removeUnidirectional"));

        suite.addTest(new TestLongTransaction("deleteSimpleBidirectional"));
        suite.addTest(new TestLongTransaction("createSimpleBidirectional"));
        suite.addTest(new TestLongTransaction("changeSimpleBidirectional"));
        suite.addTest(new TestLongTransaction("removeSimpleBidirectional"));

        suite.addTest(new TestLongTransaction("deleteComplexBidirectional"));
        suite.addTest(new TestLongTransaction("createComplexBidirectional"));
        suite.addTest(new TestLongTransaction("changeComplexBidirectional"));
        suite.addTest(new TestLongTransaction("removeComplexBidirectional"));

        return suite;
    }

    public TestLongTransaction(final String name) {
        super(name);
    }

    // Test are only included/excluded for engines that have been tested with this test suite.

    public boolean include(final DatabaseEngineType engine) {
        return (engine == DatabaseEngineType.MYSQL)
            || (engine == DatabaseEngineType.ORACLE)
            || (engine == DatabaseEngineType.DERBY);
    }
    
    // SQL_SERVER is excluded until issue CASTOR-2634 is resolved
    
    public boolean exclude(final DatabaseEngineType engine) {
        return (engine == DatabaseEngineType.SQL_SERVER);
    }
    
    public void deleteUnidirectional() throws Exception {
        JDOManager jdo = getJDOManager(DBNAME, MAPPING);
        
        // Delete all records to avoid problems with previous runs
        Connection conn = jdo.getConnectionFactory().createConnection();
        conn.createStatement().execute("DELETE FROM TEST1196_STATE");
        conn.createStatement().execute("DELETE FROM TEST1196_COUNTRY");
        conn.close();
    }

    public void createUnidirectional() throws Exception {
        Country country = null;
        State state = null;
        
        Database db = getJDOManager(DBNAME, MAPPING).getDatabase();
        db.begin();
        
        //Country and states of BRAZIL
        country = new Country();
        country.setOid("AAAACTBR");
        country.setName("BRAZIL");
        db.create(country);
        
        state = new State();
        state.setOid("AASTBRPR");
        state.setName("PARANA");
        state.setCountry(country);
        db.create(state);
        
        state = new State();
        state.setOid("AASTBRSP");
        state.setName("SAO PAULO");
        state.setCountry(country);
        db.create(state);
        
        //Country and states of UNITED STATES
        country = new Country();
        country.setOid("AAAACTUS");
        country.setName("UNITED STATES");
        db.create(country);
        
        state = new State();
        state.setOid("AASTUSTX");
        state.setName("TEXAS");
        state.setCountry(country);
        db.create(state);
        
        state = new State();
        state.setOid("AASTUSCL");
        state.setName("COLORADO");
        state.setCountry(country);
        db.create(state);
        
        //Country for test
        country = new Country();
        country.setOid("AAAACTTS");
        country.setName("COUNTRY FOR TEST");
        db.create(country);
        
        db.commit();
        db.close();
    }
    
    public void changeUnidirectional() throws Exception {
        Database db = getJDOManager(DBNAME, MAPPING).getDatabase();
        
        //1. client1 load Parana State
        db.begin();
        State paranaStateClient1 = (State) db.load(State.class, "AASTBRPR");
        db.commit();
        
        //2. client1 load COUNTRY FOR TEST
        db.begin();
        Country countryTestClient1 = (Country) db.load(Country.class, "AAAACTTS");
        db.commit();
        
        //3. client2 load COUNTRY FOR TEST
        db.begin();
        Country countryTestClient2 = (Country) db.load(Country.class, "AAAACTTS");
        db.commit();
        
        //4. client2 changes COUNTRY FOR TEST
        countryTestClient2.setName("COUNTRY FOR TEST CHANGED");

        db.begin();
        db.update(countryTestClient2);
        db.commit();
        
        //5. client1 will change state of Parana State 
        paranaStateClient1.setCountry(countryTestClient1);

        db.begin();
        db.update(paranaStateClient1);
        db.commit();

        db.close();
    }
    
    public void removeUnidirectional() throws Exception {
        Country country = null;
        State state = null;
        
        Database db = getJDOManager(DBNAME, MAPPING).getDatabase();
        db.begin();
        
        state = (State) db.load(State.class, "AASTBRPR");
        db.remove(state);
        
        state = (State) db.load(State.class, "AASTBRSP");
        db.remove(state);
        
        state = (State) db.load(State.class, "AASTUSTX");
        db.remove(state);
        
        state = (State) db.load(State.class, "AASTUSCL");
        db.remove(state);
        
        country = (Country) db.load(Country.class, "AAAACTBR");
        db.remove(country);

        country = (Country) db.load(Country.class, "AAAACTUS");
        db.remove(country);
        
        country = (Country) db.load(Country.class, "AAAACTTS");
        db.remove(country);
        
        db.commit();
        db.close();
    }
    
    public void deleteSimpleBidirectional() throws Exception {
        JDOManager jdo = getJDOManager(DBNAME, MAPPING);
        
        // Delete all records to avoid problems with previous runs
        Connection conn = jdo.getConnectionFactory().createConnection();
        conn.createStatement().execute("UPDATE TEST1196_CAR SET DRIVER=null");
        conn.createStatement().execute("UPDATE TEST1196_DRIVER SET CAR=null");
        conn.createStatement().execute("DELETE FROM TEST1196_CAR");
        conn.createStatement().execute("DELETE FROM TEST1196_DRIVER");
        conn.close();
    }

    public void createSimpleBidirectional() throws Exception {
        Car car1, car2 = null;
        Driver driver = null;
        
        Database db = getJDOManager(DBNAME, MAPPING).getDatabase();
        db.begin();
        
        car1 = new Car();
        car1.setOid("AAACAR01");
        car1.setName("CAR 1");
        db.create(car1);

        car2 = new Car();
        car2.setOid("AAACAR02");
        car2.setName("CAR 2");
        db.create(car2);
        
        driver = new Driver();
        driver.setOid("AAADRV01");
        driver.setName("DRIVER 1");
        db.create(driver);
        
        db.commit();
        db.close();
    }
    
    public void changeSimpleBidirectional() throws Exception {
        Database db = getJDOManager(DBNAME, MAPPING).getDatabase();
        
        //1. client1 load CAR ONE
        db.begin();
        Car car1Client1 = (Car) db.load(Car.class, "AAACAR01");
        db.commit();
        
        //2. client1 load DRIVER ONE
        db.begin();
        Driver driver1Client1 = (Driver) db.load(Driver.class, "AAADRV01");
        db.commit();
        
        //3. client2 load DRIVER ONE
        db.begin();
        Driver driver1Client2 = (Driver) db.load(Driver.class, "AAADRV01");
        db.commit();
        
        //4. client2 changes DRIVER ONE
        driver1Client2.setName("DRIVER 1 CHANGED");

        db.begin();
        db.update(driver1Client2);
        db.commit();
        
        //5. client1 change associates CAR ONE with DRIVER ONE
        car1Client1.setDriver(driver1Client1);
        driver1Client1.setCar(car1Client1);

        //5.1 try to write, to test ObjectModifiedException
        try {
            db.begin();
            db.update(car1Client1);
            db.update(driver1Client1);
            db.commit();
        } catch (ObjectModifiedException ome) {
            db.rollback();
            
            //5.2 client1 reload DRIVER ONE
            db.begin();
            driver1Client1 = (Driver) db.load(Driver.class, "AAADRV01");
            db.commit();
            
            //5.3 redo changes
            car1Client1.setDriver(driver1Client1);
            driver1Client1.setCar(car1Client1);
            
            db.begin();
            db.update(car1Client1);
            db.update(driver1Client1);
            db.commit();
        } finally {
            db.close();
        }
    }
    
    public void removeSimpleBidirectional() throws Exception {
        Car car;
        Driver driver;
        
        Database db = getJDOManager(DBNAME, MAPPING).getDatabase();
        db.begin();
        //1. removes references
        
        car = (Car) db.load(Car.class, "AAACAR01");
        car.setDriver(null);
        
        car = (Car) db.load(Car.class, "AAACAR02");
        car.setDriver(null);

        driver = (Driver) db.load(Driver.class, "AAADRV01");
        driver.setCar(null);
        
        db.commit();
        db.begin();
        // 2. removes objects
        
        car = (Car) db.load(Car.class, "AAACAR01");
        db.remove(car);
        
        car = (Car) db.load(Car.class, "AAACAR02");
        db.remove(car);

        driver = (Driver) db.load(Driver.class, "AAADRV01");
        db.remove(driver);
        
        db.commit();
        db.close();
    }
    
    public void deleteComplexBidirectional() throws Exception {
        JDOManager jdo = getJDOManager(DBNAME, MAPPING);
        
        // Delete all records to avoid problems with previous runs
        Connection conn = jdo.getConnectionFactory().createConnection();
        conn.createStatement().execute("DELETE FROM TEST1196_COMPUTER");
        conn.createStatement().execute("DELETE FROM TEST1196_ORDERITEM");
        conn.createStatement().execute("DELETE FROM TEST1196_PRODUCT");
        conn.createStatement().execute("DELETE FROM TEST1196_ORDER");
        conn.close();
    }

    public void createComplexBidirectional() throws Exception {
        Computer computer1, computer2 = null;
        Order order = null;
        OrderItem orderItem = null;
        
        Database db = getJDOManager(DBNAME, MAPPING).getDatabase();
        db.begin();
        
        computer1 = new Computer();
        computer1.setOid("AAAACP01");
        computer1.setName("COMPUTER 01");
        computer1.setNumber("CP01");
        db.create(computer1);
        
        computer2 = new Computer();
        computer2.setOid("AAAACP02");
        computer2.setName("COMPUTER 02");
        computer2.setNumber("CP02");
        db.create(computer2);
        
        orderItem = new OrderItem();
        orderItem.setOid("AAOR01I1");
        orderItem.setQuantity(new Integer(1));
        orderItem.setProduct(computer1);

        order = new Order();
        order.setOid("AAAAOR01");
        order.setNumber(new Integer(1));
        order.addOrderItem(orderItem);
        db.create(order);
        
        computer1.setOrderItem(orderItem);
        
        db.commit();
        db.close();
    }
    
    public void changeComplexBidirectional() throws Exception {
        Database db = getJDOManager(DBNAME, MAPPING).getDatabase();
        
        //1. client1 load order
        db.begin();
        Order orderClient1 = (Order) db.load(Order.class, "AAAAOR01");
        db.commit();
        
        //2. client1 load COMPUTER 2
        db.begin();
        Computer computer2Client1 = (Computer) db.load(Computer.class, "AAAACP02");
        db.commit();
        
        //3. client2 load COMPUTER 2
        db.begin();
        Computer computer2Client2 = (Computer) db.load(Computer.class, "AAAACP02");
        db.commit();
        
        //4. client2 changes COMPUTER 2
        computer2Client2.setName("COMPUTER 2 CHANGED");

        db.begin();
        db.update(computer2Client2);
        db.commit();
        
        //5. client1 will change computer of order item
        OrderItem orderItem = orderClient1.getOrderItem("AAOR01I1");
        Computer computer1Client1 = (Computer) orderItem.getProduct();
        computer1Client1.setOrderItem(null);
        orderItem.setProduct(computer2Client1);
        computer2Client1.setOrderItem(orderItem);

        //5.1 try to write, to test ObjectModifiedException
        try {
            db.begin();
            db.update(orderClient1);
            db.update(computer1Client1);
            db.update(computer2Client1);
            db.commit();
        } catch (ObjectModifiedException ome) {
            db.rollback();
            
            //5.2 client1 reload COMPUTER 2
            db.begin();
            computer2Client1 = (Computer) db.load(Computer.class, "AAAACP02");
            db.commit();
            
            // TODO [CW]: 5.3 client 1 must reload COMPUTER 1 too
            //because a BUG in rollback that dont rollback timestamps
            db.begin();
            computer1Client1 = (Computer) db.load(Computer.class, "AAAACP01");
            db.commit();
            
            //5.3 redo changes
            orderItem = orderClient1.getOrderItem("AAOR01I1");
            orderItem.setProduct(computer2Client1);
            computer2Client1.setOrderItem(orderItem);
            computer1Client1.setOrderItem(null);
            
            db.begin();
            db.update(orderClient1);
            db.update(computer1Client1);
            db.update(computer2Client1);
            db.commit();
        } finally {
            db.close();
        }
    }
    
    public void removeComplexBidirectional() throws Exception {
        Computer computer;
        Order order;
        OrderItem orderItem;
        
        Database db = getJDOManager(DBNAME, MAPPING).getDatabase();
        db.begin();
        
        order = (Order) db.load(Order.class, "AAAAOR01");
        orderItem = order.getOrderItems().get(0);
        computer = (Computer) orderItem.getProduct();
        computer.setOrderItem(null);
        orderItem.setProduct(null);
        
        db.commit();
        db.begin();

        order = (Order) db.load(Order.class, "AAAAOR01");
        db.remove(order);

        computer = (Computer) db.load(Computer.class, "AAAACP01");
        db.remove(computer);

        computer = (Computer) db.load(Computer.class, "AAAACP02");
        db.remove(computer);
        
        db.commit();
        db.close();
    }
}
