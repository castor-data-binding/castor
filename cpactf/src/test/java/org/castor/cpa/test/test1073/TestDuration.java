package org.castor.cpa.test.test1073;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.castor.cpa.test.framework.CPATestCase;
import org.castor.cpa.test.framework.xml.types.DatabaseEngineType;

import java.sql.Connection;

import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.JDOManager;
import org.exolab.castor.types.Duration;

public final class TestDuration extends CPATestCase {
    private static final String DBNAME = "test1073";
    private static final String MAPPING = "/org/castor/cpa/test/test1073/mapping.xml";
    
    public static Test suite() throws Exception {
        TestSuite suite = new TestSuite(TestDuration.class.getName());

        suite.addTest(new TestDuration("delete"));
        suite.addTest(new TestDuration("create"));
        suite.addTest(new TestDuration("update"));
        suite.addTest(new TestDuration("load"));

        return suite;
    }

    public TestDuration(final String name) {
        super(name);
    }

    // Test are only included/excluded for engines that have been tested with this test suite.

    public boolean include(final DatabaseEngineType engine) {
        return (engine == DatabaseEngineType.MYSQL)
            || (engine == DatabaseEngineType.DERBY)
            || (engine == DatabaseEngineType.SQL_SERVER)
            || (engine == DatabaseEngineType.SAPDB)
            || (engine == DatabaseEngineType.ORACLE)
            || (engine == DatabaseEngineType.POSTGRESQL);
    }
    
    public void delete() throws Exception {
        JDOManager jdo = getJDOManager(DBNAME, MAPPING);
        
        // Delete all records to avoid problems with previous runs
        Connection conn = jdo.getConnectionFactory().createConnection();
        conn.setAutoCommit(true);
        conn.createStatement().execute("DELETE FROM test1073_duration");
        conn.close();
    }
    
    public void create() throws Exception {
        Database db = getJDOManager(DBNAME, MAPPING).getDatabase();

        DurationEntity entity = new DurationEntity();
        entity.setId(1);
        entity.setLongDuration(null);
        entity.setStringDuration(null);

        db.begin();
        db.create(entity);
        db.commit();
        db.close();
    }
    
    public void update() throws Exception {
        Database db = getJDOManager(DBNAME, MAPPING).getDatabase();
        db.begin();

        DurationEntity entity = (DurationEntity) db.load(DurationEntity.class, new Integer(1));

        assertEquals(1, entity.getId());
        assertNull(entity.getLongDuration());
        assertNull(entity.getStringDuration());

        Duration ld = new Duration();
        ld.setValue((short) 0, (short) 0, (short) 0, (short) 23, (short) 59, (short) 59, 999);
        entity.setLongDuration(ld);
        
        Duration sd = new Duration();
        sd.setValue((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6, 7);
        entity.setStringDuration(sd);
        
        db.commit();
        db.close();
    }
    
    public void load() throws Exception {
        Database db = getJDOManager(DBNAME, MAPPING).getDatabase();
        db.begin();

        DurationEntity entity = (DurationEntity) db.load(DurationEntity.class, new Integer(1));

        assertEquals(1, entity.getId());

        Duration ld = new Duration();
        ld.setValue((short) 0, (short) 0, (short) 0, (short) 23, (short) 59, (short) 59, 999);
        assertTrue(ld.equals(entity.getLongDuration()));
        
        Duration sd = new Duration();
        sd.setValue((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6, 7);
        assertTrue(sd.equals(entity.getStringDuration()));
        
        db.commit();
        db.close();
    }
}
