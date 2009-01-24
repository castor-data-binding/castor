package org.castor.cpa.test.test881;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.castor.cpa.test.framework.CPATestCase;
import org.castor.cpa.test.framework.xml.types.DatabaseEngineType;
import org.castor.jdo.conf.JdoConf;
import org.castor.jdo.util.JDOConfFactory;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.JDOManager;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.QueryResults;

public final class Test881 extends CPATestCase {
    private static final String DBNAME = "test881";
    private static final String MAPPING = "/org/castor/cpa/test/test881/mapping.xml";

    public static Test suite() {
        TestSuite suite = new TestSuite(Test881.class.getName());
        
        suite.addTest(new Test881("testLoadConfA"));
        suite.addTest(new Test881("testLoadConfB"));
        suite.addTest(new Test881("testLoadConfAB"));
        suite.addTest(new Test881("testLoadConfAandB"));
        suite.addTest(new Test881("testLoadConfBandAB"));
        suite.addTest(new Test881("testLoadConfABandA"));

        return suite;
    }
    
    public Test881(final String name) {
        super(name);
    }
    
    // Test are only included/excluded for engines that have been tested with this test suite.

    public boolean include(final DatabaseEngineType engine) {
        return (engine == DatabaseEngineType.MYSQL) 
            || (engine == DatabaseEngineType.ORACLE);
    }
    
    public void testLoadConfA() throws Exception {
        JdoConf aConf = createJdoConfA(getJdoConf(DBNAME, MAPPING));
        JDOManager.loadConfiguration(aConf, getJdoConfBaseURL());

        executeQuery(JDOManager.createInstance(DBNAME + "-a").getDatabase());
        
        JDOManager.disposeInstance(DBNAME + "-a");
    }

    public void testLoadConfB() throws Exception {
        JdoConf bConf = createJdoConfB(getJdoConf(DBNAME, MAPPING));
        JDOManager.loadConfiguration(bConf, getJdoConfBaseURL());

        executeQuery(JDOManager.createInstance(DBNAME + "-b").getDatabase());
        
        JDOManager.disposeInstance(DBNAME + "-b");
    }

    public void testLoadConfAB() throws Exception {
        JdoConf abConf = createJdoConfAB(getJdoConf(DBNAME, MAPPING));
        JDOManager.loadConfiguration(abConf, getJdoConfBaseURL());

        executeQuery(JDOManager.createInstance(DBNAME + "-a").getDatabase());
        executeQuery(JDOManager.createInstance(DBNAME + "-b").getDatabase());
        
        JDOManager.disposeInstance(DBNAME + "-a");
        JDOManager.disposeInstance(DBNAME + "-b");
    }

    public void testLoadConfAandB() throws Exception {
        // Load configuration for A first followed by B.
        // Why should this behave different as loading A and B
        // from one configuration containing both?
        JdoConf aConf = createJdoConfA(getJdoConf(DBNAME, MAPPING));
        JDOManager.loadConfiguration(aConf, getJdoConfBaseURL());
        JdoConf bConf = createJdoConfB(getJdoConf(DBNAME, MAPPING));
        JDOManager.loadConfiguration(bConf, getJdoConfBaseURL());

        executeQuery(JDOManager.createInstance(DBNAME + "-a").getDatabase());
        executeQuery(JDOManager.createInstance(DBNAME + "-b").getDatabase());
        
        JDOManager.disposeInstance(DBNAME + "-a");
        JDOManager.disposeInstance(DBNAME + "-b");
    }

    public void testLoadConfBandAB() throws Exception {
        // Load configuration for B first followed by a configuration
        // containing A and B. This means B is loaded twice.
        // What shell we do with the second configuration containg A and B?
        // Ignore it completely, only ignore second configuration of B or
        // or replace the first configuration of B with the second one.
        JdoConf bConf = createJdoConfB(getJdoConf(DBNAME, MAPPING));
        JDOManager.loadConfiguration(bConf, getJdoConfBaseURL());
        JdoConf abConf = createJdoConfAB(getJdoConf(DBNAME, MAPPING));
        JDOManager.loadConfiguration(abConf, getJdoConfBaseURL());

        executeQuery(JDOManager.createInstance(DBNAME + "-a").getDatabase());
        executeQuery(JDOManager.createInstance(DBNAME + "-b").getDatabase());
        
        JDOManager.disposeInstance(DBNAME + "-a");
        JDOManager.disposeInstance(DBNAME + "-b");
    }

    public void testLoadConfABandA() throws Exception {
        // Load configuration for A and B first followed by a configuration
        // containing only A. This means A is loaded twice.
        // What shell we do with the second configuration containg A?
        // Ignore it completely, only ignore second configuration of A or
        // or replace the first configuration of A with the second one.
        JdoConf abConf = createJdoConfAB(getJdoConf(DBNAME, MAPPING));
        JDOManager.loadConfiguration(abConf, getJdoConfBaseURL());
        JdoConf aConf = createJdoConfA(getJdoConf(DBNAME, MAPPING));
        JDOManager.loadConfiguration(aConf, getJdoConfBaseURL());

        executeQuery(JDOManager.createInstance(DBNAME + "-a").getDatabase());
        executeQuery(JDOManager.createInstance(DBNAME + "-b").getDatabase());
        
        JDOManager.disposeInstance(DBNAME + "-a");
        JDOManager.disposeInstance(DBNAME + "-b");
    }
    
    private JdoConf createJdoConfA(final JdoConf orgConf) {
        org.castor.jdo.conf.Database orgDB = orgConf.getDatabase(0);
        
        org.castor.jdo.conf.Database aDB = new org.castor.jdo.conf.Database();
        aDB.setName(orgDB.getName() + "-a");
        aDB.setEngine(orgDB.getEngine());
        aDB.setDatabaseChoice(orgDB.getDatabaseChoice());
        aDB.setMapping(orgDB.getMapping());
        
        return JDOConfFactory.createJdoConf(aDB, orgConf.getTransactionDemarcation());
    }
    
    private JdoConf createJdoConfB(final JdoConf orgConf) {
        org.castor.jdo.conf.Database orgDB = orgConf.getDatabase(0);
        
        org.castor.jdo.conf.Database bDB = new org.castor.jdo.conf.Database();
        bDB.setName(orgDB.getName() + "-b");
        bDB.setEngine(orgDB.getEngine());
        bDB.setDatabaseChoice(orgDB.getDatabaseChoice());
        bDB.setMapping(orgDB.getMapping());
        
        return JDOConfFactory.createJdoConf(bDB, orgConf.getTransactionDemarcation());
    }
    
    private JdoConf createJdoConfAB(final JdoConf orgConf) {
        org.castor.jdo.conf.Database orgDB = orgConf.getDatabase(0);
        
        org.castor.jdo.conf.Database aDB = new org.castor.jdo.conf.Database();
        aDB.setName(orgDB.getName() + "-a");
        aDB.setEngine(orgDB.getEngine());
        aDB.setDatabaseChoice(orgDB.getDatabaseChoice());
        aDB.setMapping(orgDB.getMapping());
        
        org.castor.jdo.conf.Database bDB = new org.castor.jdo.conf.Database();
        bDB.setName(orgDB.getName() + "-b");
        bDB.setEngine(orgDB.getEngine());
        bDB.setDatabaseChoice(orgDB.getDatabaseChoice());
        bDB.setMapping(orgDB.getMapping());
        
        org.castor.jdo.conf.Database[] dbs = new org.castor.jdo.conf.Database[] {aDB, bDB};
        return JDOConfFactory.createJdoConf(dbs, orgConf.getTransactionDemarcation());
    }
    
    private void executeQuery(final Database db) throws PersistenceException {
        db.begin();
        
        OQLQuery oql = db.getOQLQuery(
                "select p from " + Product.class.getName() + " p");
        QueryResults results = oql.execute();
        results.close();
        
        db.commit();
        db.close();
    }
}
