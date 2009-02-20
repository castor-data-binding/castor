package org.castor.cpa.test.test241;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.castor.cpa.test.framework.CPATestCase;
import org.castor.cpa.test.framework.xml.types.DatabaseEngineType;

import java.sql.Connection;

import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.JDOManager;
import org.exolab.castor.jdo.ObjectNotFoundException;

public final class TestObjectNotFound extends CPATestCase {
    private static final String DBNAME = "test241";
    private static final String MAPPING = "/org/castor/cpa/test/test241/mapping.xml";
    
    public static Test suite() throws Exception {
        TestSuite suite = new TestSuite(TestObjectNotFound.class.getName());

        suite.addTest(new TestObjectNotFound("delete"));
        suite.addTest(new TestObjectNotFound("create"));

        return suite;
    }

    public TestObjectNotFound(final String name) {
        super(name);
    }

    // Test are only included/excluded for engines that have been tested with this test suite.

    public boolean include(final DatabaseEngineType engine) {
        return (engine == DatabaseEngineType.MYSQL)
            || (engine == DatabaseEngineType.DERBY);
    }
    
    public void delete() throws Exception {
        JDOManager jdo = getJDOManager(DBNAME, MAPPING);
        
        // Delete all records to avoid problems with previous runs
        Connection conn = jdo.getConnectionFactory().createConnection();
        conn.setAutoCommit(true);
        conn.createStatement().execute("DELETE FROM test241_prod");
        conn.close();
    }
    
    public void create() throws Exception {
        Database db = getJDOManager(DBNAME, MAPPING).getDatabase();
        db.begin();

        Product product = null;
        try {
            product = (Product) db.load(Product.class, new Integer(1));
            fail("ObjectNotFoundException should be thrown as product loaded does not exist");
        } catch (ObjectNotFoundException ex) {
            product = new Product();
            product.setId(1);
            product.setName("some product");
            db.create(product);
        } catch (Exception ex) {
            fail("ObjectNotFoundException expected");
        }
        
        db.commit();
        db.close();
    }
}
