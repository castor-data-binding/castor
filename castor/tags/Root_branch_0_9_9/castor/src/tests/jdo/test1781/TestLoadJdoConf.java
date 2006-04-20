package jdo.test1781;

import junit.framework.TestCase;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.JDOManager;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.QueryResults;
import org.exolab.castor.jdo.util.JDOConfFactory;

public final class TestLoadJdoConf extends TestCase {
    private static final Log LOG = LogFactory.getLog(TestLoadJdoConf.class);;
    
    public static void main(final String[] args) throws Exception {
        TestLoadJdoConf test = new TestLoadJdoConf();
        test.setUp();
        test.testQueryEntityOne();
        test.tearDown();
    }
    
    public TestLoadJdoConf() {
        super();
    }
    
    public TestLoadJdoConf(final String name) {
        super(name);
    }

    protected void setUp() throws Exception {
        super.setUp();
    }
    
    protected void tearDown() throws Exception {
        super.tearDown();
    }
    
    /**
     * Test method.
     * @throws Exception For any exception thrown.
     */
    public void testQueryEntityOne() throws Exception {
        String DATABASE = "test";
        String ENGINE = "mysql";
        String DRIVER = "org.gjt.mm.mysql.Driver";
        String CONNECT = "jdbc:mysql://localhost/bugTemplate";
        String USERNAME = "test";
        String PASSWORD = "test";
        String MAPPING = "test1781-mapping.xml";
        
        // create driver configuration
        org.exolab.castor.jdo.conf.Driver driverConf =
            JDOConfFactory.createDriver(DRIVER, CONNECT, USERNAME, PASSWORD);
        
        // create mapping configuration
        org.exolab.castor.jdo.conf.Mapping mappingConf =
            JDOConfFactory.createMapping(getClass().getResource(MAPPING).toString());

        // create database configuration
        org.exolab.castor.jdo.conf.Database dbConf =
            JDOConfFactory.createDatabase(DATABASE, ENGINE, driverConf, mappingConf);
        
        // create and load jdo configuration
        JDOManager.loadConfiguration(JDOConfFactory.createJdoConf(dbConf));
        
        // create jdo instance
        JDOManager jdo = JDOManager.createInstance(DATABASE);

        // create database instance and execute transaction
        Database db = jdo.getDatabase();
        db.begin();
        
        OQLQuery query = db.getOQLQuery("SELECT entity FROM "
                + EntityOne.class.getName() + " entity WHERE id = $1");
        query.bind(new Integer(1));
        QueryResults results = query.execute();
        
        EntityOne entity = (EntityOne) results.next();

        assertNotNull(entity);
        assertEquals(new Integer(1), entity.getId());
        
        db.commit();
        db.close();
    }
}
