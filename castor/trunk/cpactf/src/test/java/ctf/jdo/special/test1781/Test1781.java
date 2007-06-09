package ctf.jdo.special.test1781;

import java.util.Properties;

import junit.framework.TestCase;

import org.castor.jdo.conf.JdoConf;
import org.castor.jdo.util.JDOConfFactory;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.JDOManager;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.QueryResults;

public final class Test1781 extends TestCase {
    private static final String DATABASE = "test1781";
    private static final String ENGINE = "mysql";
    private static final String DATASOURCE = "org.apache.commons.dbcp.BasicDataSource";
    private static final String DRIVER = "org.gjt.mm.mysql.Driver";
    private static final String CONNECT = "jdbc:mysql://localhost/test1781";
    private static final String USERNAME = "test";
    private static final String PASSWORD = "test";
    private static final String MAPPING = "mapping.xml";
    
    public static void main(final String[] args) throws Exception {
        Test1781 test = new Test1781();
        test.setUp();
        test.testJdoDriverConf();
        test.testJdoDatasourceConf();
        test.tearDown();
    }
    
    protected void setUp() throws Exception {
        super.setUp();
    }
    
    protected void tearDown() throws Exception {
        super.tearDown();
    }
    
    public void testJdoDriverConf() throws Exception {
        // create driver configuration
        org.castor.jdo.conf.Driver driverConf =
            JDOConfFactory.createDriver(DRIVER, CONNECT, USERNAME, PASSWORD);
        
        // create mapping configuration
        org.castor.jdo.conf.Mapping mappingConf =
            JDOConfFactory.createMapping(getClass().getResource(MAPPING).toString());

        // create database configuration
        org.castor.jdo.conf.Database dbConf =
            JDOConfFactory.createDatabase(DATABASE, ENGINE, driverConf, mappingConf);
        
        // test configuration
        loadEntity(DATABASE, JDOConfFactory.createJdoConf(dbConf));
    }
    
    public void testJdoDatasourceConf() throws Exception {
        Properties props = new Properties();
        props.put("driver-class-name", DRIVER);
        props.put("url", CONNECT);
        props.put("username", USERNAME);
        props.put("password", PASSWORD);
        props.put("max-active", "10");
        
        // create driver configuration
        org.castor.jdo.conf.DataSource datasourceConf =
            JDOConfFactory.createDataSource(DATASOURCE, props);
        
        // create mapping configuration
        org.castor.jdo.conf.Mapping mappingConf =
            JDOConfFactory.createMapping(getClass().getResource(MAPPING).toString());

        // create database configuration
        org.castor.jdo.conf.Database dbConf =
            JDOConfFactory.createDatabase(DATABASE, ENGINE, datasourceConf, mappingConf);
        
        // test configuration
        loadEntity(DATABASE, JDOConfFactory.createJdoConf(dbConf));
    }
    
    private void loadEntity(final String dbname, final JdoConf conf) throws Exception {
        // create and load jdo configuration
        JDOManager.loadConfiguration(conf, null);
        
        // create jdo instance
        JDOManager jdo = JDOManager.createInstance(dbname);

        // create database instance and execute transaction
        Database db = jdo.getDatabase();
        db.begin();
        
        OQLQuery query = db.getOQLQuery("SELECT entity FROM "
                + Entity.class.getName() + " entity WHERE id = $1");
        query.bind(new Integer(1));
        QueryResults results = query.execute();
        
        Entity entity = (Entity) results.next();

        assertNotNull(entity);
        assertEquals(new Integer(1), entity.getId());
        
        db.commit();
        db.close();
    }
}