package org.castor.cpa.test.test1206;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.castor.cpa.test.framework.CPATestCase;
import org.castor.cpa.test.framework.xml.types.DatabaseEngineType;

import java.sql.Connection;
import java.sql.ResultSet;

import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.JDOManager;

public final class TestDirtyIgnore extends CPATestCase {
    private static final String DBNAME = "test1206";
    private static final String MAPPING = "/org/castor/cpa/test/test1206/mapping.xml";
    
    public static Test suite() throws Exception {
        TestSuite suite = new TestSuite(TestDirtyIgnore.class.getName());

        suite.addTest(new TestDirtyIgnore("delete"));
        suite.addTest(new TestDirtyIgnore("create"));
        suite.addTest(new TestDirtyIgnore("changeDirtyReference"));
        suite.addTest(new TestDirtyIgnore("remove"));

        return suite;
    }

    public TestDirtyIgnore(final String name) {
        super(name);
    }

    public boolean include(final DatabaseEngineType engine) {
        return (engine == DatabaseEngineType.MYSQL)
            || (engine == DatabaseEngineType.ORACLE);
    }
    
    public void delete() throws Exception {
        JDOManager jdo = getJDOManager(DBNAME, MAPPING);
        
        // Delete all records to avoid problems with previous runs
        Connection conn = jdo.getConnectionFactory().createConnection();
        conn.setAutoCommit(true);
        conn.createStatement().execute("DELETE FROM TEST1206_STATE");
        conn.createStatement().execute("DELETE FROM TEST1206_COUNTRY");
        conn.close();
    }
    
    public void create() throws Exception {
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
    
    public void changeDirtyReference() throws Exception {
        Database db = getJDOManager(DBNAME, MAPPING).getDatabase();
        
        //1. Client loads objects and changes country of state
        db.begin();
        State state = (State) db.load(State.class, "AASTBRPR");
        Country country = (Country) db.load(Country.class, "AAAACTTS");
        state.setCountry(country);
        db.commit();
        
        //2. reload objects from updated cache
        db.begin();
        state = (State) db.load(State.class, "AASTBRPR");
        db.commit();
        
        //3. lets check if database changes too
        JDOManager jdo = getJDOManager(DBNAME, MAPPING);
        Connection conn = jdo.getConnectionFactory().createConnection();
        conn.setAutoCommit(true);
        ResultSet rs = conn.createStatement().executeQuery(
                "SELECT * FROM TEST1206_STATE WHERE oid = '" + state.getOid() + "'");
        if (rs.next()) {
            String persCountry = rs.getString("COUNTRY");
            String cacheCountry = state.getCountry().getOid();
            if (!persCountry.equals(cacheCountry)) {
                System.out.println("database: '" + persCountry + "'");
                System.out.println("cache: '" + cacheCountry + "'");
                throw new Exception("Object in database don't reflect changes in cache!");
            }
        }
        conn.close();
    }
    
    public void remove() throws Exception {
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
}
