package org.castor.cpa.test.test1073;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.castor.cpa.test.framework.CPATestCase;
import org.castor.cpa.test.framework.xml.types.DatabaseEngineType;

import java.sql.Connection;

import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.JDOManager;
import org.exolab.castor.types.Time;

public final class TestTime extends CPATestCase {
    private static final String DBNAME = "test1073";
    private static final String MAPPING = "/org/castor/cpa/test/test1073/mapping.xml";
    
    public static Test suite() throws Exception {
        TestSuite suite = new TestSuite(TestTime.class.getName());

        suite.addTest(new TestTime("delete"));
        suite.addTest(new TestTime("create"));
        suite.addTest(new TestTime("update"));
        suite.addTest(new TestTime("load"));

        return suite;
    }

    public TestTime(final String name) {
        super(name);
    }

    // Test are only included/excluded for engines that have been tested with this test suite.

    public boolean include(final DatabaseEngineType engine) {
        return (engine == DatabaseEngineType.MYSQL)
            || (engine == DatabaseEngineType.DERBY)
            || (engine == DatabaseEngineType.SQL_SERVER)
            || (engine == DatabaseEngineType.ORACLE);
    }
    
    public void delete() throws Exception {
        JDOManager jdo = getJDOManager(DBNAME, MAPPING);
        
        // Delete all records to avoid problems with previous runs
        Connection conn = jdo.getConnectionFactory().createConnection();
        conn.setAutoCommit(true);
        conn.createStatement().execute("DELETE FROM test1073_time");
        conn.close();
    }
    
    public void create() throws Exception {
        Database db = getJDOManager(DBNAME, MAPPING).getDatabase();

        TimeEntity entity = new TimeEntity();
        entity.setId(1);
        entity.setLongTimeLocal(null);
        entity.setLongTimeUTC(null);
        entity.setStringTimeLocal(null);
        entity.setStringTimeUTC(null);

        db.begin();
        db.create(entity);
        db.commit();
        db.close();
    }
    
    public void update() throws Exception {
        Database db = getJDOManager(DBNAME, MAPPING).getDatabase();
        db.begin();

        TimeEntity entity = (TimeEntity) db.load(TimeEntity.class, new Integer(1));

        assertEquals(1, entity.getId());
        assertNull(entity.getLongTimeLocal());
        assertNull(entity.getLongTimeUTC());
        assertNull(entity.getStringTimeLocal());
        assertNull(entity.getStringTimeUTC());

        entity.setLongTimeLocal(new Time("12:34:56.789"));
        entity.setLongTimeUTC(new Time("23:11:22-05:00"));
        entity.setStringTimeLocal(new Time("11:11:11.111"));
        entity.setStringTimeUTC(new Time("02:33:44+03:00"));
        
        db.commit();
        db.close();
    }
    
    public void load() throws Exception {
        Database db = getJDOManager(DBNAME, MAPPING).getDatabase();
        db.begin();

        TimeEntity entity = (TimeEntity) db.load(TimeEntity.class, new Integer(1));

        assertEquals(1, entity.getId());
        assertTrue(new Time("12:34:56.789").equals(entity.getLongTimeLocal()));
        assertTrue(new Time("23:11:22-05:00").equals(entity.getLongTimeUTC()));
        assertTrue(new Time("11:11:11.111").equals(entity.getStringTimeLocal()));
        assertTrue(new Time("02:33:44+03:00").equals(entity.getStringTimeUTC()));
        
        db.commit();
        db.close();
    }
}
