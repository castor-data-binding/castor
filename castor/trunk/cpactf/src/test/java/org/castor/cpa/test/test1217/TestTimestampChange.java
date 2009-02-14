package org.castor.cpa.test.test1217;

import java.sql.Connection;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.castor.cpa.test.framework.CPATestCase;
import org.castor.cpa.test.framework.xml.types.DatabaseEngineType;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.JDOManager;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.QueryResults;

public final class TestTimestampChange extends CPATestCase {
    private static final String DBNAME = "test1217";
    private static final String MAPPING = "/org/castor/cpa/test/test1217/mapping.xml";
    
    public static Test suite() throws Exception {
        TestSuite suite = new TestSuite(TestTimestampChange.class.getName());

        suite.addTest(new TestTimestampChange("delete"));
        suite.addTest(new TestTimestampChange("populate"));
        suite.addTest(new TestTimestampChange("querySimple"));
        suite.addTest(new TestTimestampChange("queryComplex"));
        suite.addTest(new TestTimestampChange("loadComplex"));

        return suite;
    }

    public TestTimestampChange(final String name) {
        super(name);
    }

    // Test are only included/excluded for engines that have been tested with this test suite.

    public boolean include(final DatabaseEngineType engine) {
        return (engine == DatabaseEngineType.MYSQL);
    }
    
    public void delete() throws Exception {
        JDOManager jdo = getJDOManager(DBNAME, MAPPING);
        
        // Delete all records to avoid problems with previous runs
        Connection conn = jdo.getConnectionFactory().createConnection();
        conn.setAutoCommit(true);
        conn.createStatement().execute("DELETE FROM TEST1217_PRODUCT");
        conn.createStatement().execute("DELETE FROM TEST1217_EXTENDED");
        conn.createStatement().execute("DELETE FROM TEST1217_BASE");
        conn.createStatement().execute("DELETE FROM TEST1217_PERSON");
        conn.close();
    }

    public void populate() throws Exception {
        Database db = getJDOManager(DBNAME, MAPPING).getDatabase();
        db.begin();
        
        Person company = new Person("CP1", "COMPANY 1");
        db.create(company);
        
        BasePart part = new ExtendedPart("PD1", "PRODUCT 1", "FACTN1"); 
        db.create(part);
            
        Product product = new Product("CPPD1", "1", new Double(55), company, part);
        db.create(product);
        
        db.commit();
        db.close();
    }
    
    public void querySimple() throws Exception {
        Database db = getJDOManager(DBNAME, MAPPING).getDatabase();
        BasePart part1 = null;
        BasePart part2 = null;
        
        db.begin();
        OQLQuery query = db.getOQLQuery("SELECT o FROM " + BasePart.class.getName() + " o");
        QueryResults results = query.execute(Database.READONLY);
        if (results.hasMore()) {
            part1 = (BasePart) results.next();
            assertEquals("first query result class must be an ExtendedPart!",
                    ExtendedPart.class, part1.getClass());
        }
        db.commit();
        
        db.begin();
        query = db.getOQLQuery("SELECT o FROM " + BasePart.class.getName() + " o");
        results = query.execute(Database.READONLY);
        if (results.hasMore()) {
            part2 = (BasePart) results.next();
            assertEquals("second query result class must be an ExtendedPart!",
                    ExtendedPart.class, part2.getClass());
        }
        db.commit();
        db.close();
        
        assertEquals("Timestamp must be equal!",
                part1.jdoGetTimeStamp(), part2.jdoGetTimeStamp());
    }
    
    public void queryComplex() throws Exception {
        Database db = getJDOManager(DBNAME, MAPPING).getDatabase();
        BasePart part = null;
        Product product = null;
        
        db.begin();
        OQLQuery query = db.getOQLQuery("SELECT o FROM " + BasePart.class.getName() + " o");
        QueryResults results = query.execute(Database.READONLY);
        if (results.hasMore()) {
            part = (BasePart) results.next();
            assertEquals("first query result class must be an ExtendedPart!",
                    ExtendedPart.class, part.getClass());
        }
        db.commit();
        
        db.begin();
        query = db.getOQLQuery("SELECT o FROM " + Product.class.getName() + " o WHERE part=$1");
        query.bind(part.getOid());
        results = query.execute(Database.READONLY);
        if (results.hasMore()) {
            product = (Product) results.next();
            assertEquals("second query result class must be an ExtendedPart!",
                    ExtendedPart.class, product.getPart().getClass());
        }
        db.commit();
        db.close();
        
        assertEquals("Timestamp must be equal!",
                part.jdoGetTimeStamp(), product.getPart().jdoGetTimeStamp());
    }
    
    public void loadComplex() throws Exception {
        Database db = getJDOManager(DBNAME, MAPPING).getDatabase();
        db.begin();
        Product product = (Product) db.load(Product.class, "CPPD1");
        assertEquals("third query result class must be a Product!",
                Product.class, product.getClass());
        assertEquals("third query result class must be a Person!",
                Person.class, product.getCompany().getClass());
        assertEquals("third query result class must be an ExtendedPart!",
                ExtendedPart.class, product.getPart().getClass());
        db.commit();
        
        db.begin();
        Person person = (Person) db.load(Person.class, "CP1");
        assertEquals("second query result class must be a Person!",
                Person.class, person.getClass());
        db.commit();
        
        assertEquals("Timestamp must be equal!",
                person.jdoGetTimeStamp(), product.getCompany().jdoGetTimeStamp());

        db.begin();
        BasePart part = (BasePart) db.load(BasePart.class, "PD1");
        assertEquals("first query result class must be an ExtendedPart!",
                ExtendedPart.class, part.getClass());
        db.commit();
        
        assertEquals("Timestamp must be equal!",
                part.jdoGetTimeStamp(), product.getPart().jdoGetTimeStamp());

        db.close();
    }
}
