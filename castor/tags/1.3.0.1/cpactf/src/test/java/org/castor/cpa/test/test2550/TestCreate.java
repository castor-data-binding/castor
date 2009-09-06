package org.castor.cpa.test.test2550;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.castor.cpa.test.framework.CPATestCase;
import org.castor.cpa.test.framework.xml.types.DatabaseEngineType;

import java.sql.Connection;
import java.sql.SQLException;

import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.JDOManager;
import org.exolab.castor.jdo.PersistenceException;

public final class TestCreate extends CPATestCase {
    private static final String DBNAME = "test2550";
    private static final String MAPPING = "/org/castor/cpa/test/test2550/mapping.xml";
    
    public static Test suite() throws Exception {
        TestSuite suite = new TestSuite(TestCreate.class.getName());

        suite.addTest(new TestCreate("delete"));
        suite.addTest(new TestCreate("createValid"));
        suite.addTest(new TestCreate("createNullConstraint"));

        return suite;
    }

    public TestCreate(final String name) {
        super(name);
    }

    // Test are only included/excluded for engines that have been tested with this test suite.

    public boolean include(final DatabaseEngineType engine) {
        return (engine == DatabaseEngineType.ORACLE);
    }
    
    // Test uses sequence key generator which is not supported by mysql and mssql.

    public boolean exclude(final DatabaseEngineType engine) {
        return (engine == DatabaseEngineType.MYSQL)
            || (engine == DatabaseEngineType.SQL_SERVER);
    }
    
    public void delete() throws Exception {
        JDOManager jdo = getJDOManager(DBNAME, MAPPING);
        
        // Delete all records to avoid problems with previous runs
        Connection conn = jdo.getConnectionFactory().createConnection();
        conn.setAutoCommit(true);
        conn.createStatement().execute("DELETE FROM TEST2550_ENTITY");
        conn.close();
    }
    
    public void createValid() throws Exception {
        Database db = getJDOManager(DBNAME, MAPPING).getDatabase();
        db.begin();

        Entity entity = new Entity(null, Entity.DEFAULT_NAME);
        db.create(entity);
        
        db.commit();
        db.close();
    }
    
    public void createNullConstraint() throws Exception {
        Database db = getJDOManager(DBNAME, MAPPING).getDatabase();
        db.begin();

        Entity entity = new Entity(null, null);
        try {
            db.create(entity);
            fail("Expected SQLException");
        } catch (PersistenceException ex) {
            assertTrue(ex.getCause() instanceof SQLException);
        }
        
        db.commit();
        db.close();
    }
}
