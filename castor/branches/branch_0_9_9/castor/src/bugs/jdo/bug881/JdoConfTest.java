package jdo.bug881;

import junit.framework.TestCase;

import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.JDOManager;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.QueryResults;
import org.exolab.castor.jdo.engine.DatabaseRegistry;

public class JdoConfTest extends TestCase {
    public JdoConfTest() { super("bug881"); }

    public void setUp() throws Exception { super.setUp(); }

    public void tearDown() throws Exception {
        DatabaseRegistry.clear();

        super.tearDown();
    }

    public void testLoadConfA() throws Exception {
        JDOManager.loadConfiguration(
                getClass().getResource("jdo-conf-a.xml").toString());

        JDOManager jdoA = JDOManager.createInstance("test-a");
        Database dbA = jdoA.getDatabase();
        dbA.begin();
        
        OQLQuery oqlA = dbA.getOQLQuery(
                "select p from " + Product.class.getName() + " p");
        QueryResults resultsA = oqlA.execute();
        resultsA.close();
        
        dbA.commit();
        dbA.close();

        jdoA = null;
    }

    public void testLoadConfB() throws Exception {
        JDOManager.loadConfiguration(
                getClass().getResource("jdo-conf-b.xml").toString());

        JDOManager jdoB = JDOManager.createInstance("test-b");
        Database dbB = jdoB.getDatabase();
        dbB.begin();
        
        OQLQuery oqlB = dbB.getOQLQuery(
                "select p from " + Product.class.getName() + " p");
        QueryResults resultsB = oqlB.execute();
        resultsB.close();
        
        dbB.commit();
        dbB.close();

        jdoB = null;
    }

    public void testLoadConfAB() throws Exception {
        JDOManager.loadConfiguration(
                getClass().getResource("jdo-conf-ab.xml").toString());

        JDOManager jdoA = JDOManager.createInstance("test-a");
        Database dbA = jdoA.getDatabase();
        dbA.begin();
        
        OQLQuery oqlA = dbA.getOQLQuery(
                "select p from " + Product.class.getName() + " p");
        QueryResults resultsA = oqlA.execute();
        resultsA.close();
        
        dbA.commit();
        dbA.close();

        jdoA = null;

        JDOManager jdoB = JDOManager.createInstance("test-b");
        Database dbB = jdoB.getDatabase();
        dbB.begin();
        
        OQLQuery oqlB = dbB.getOQLQuery(
                "select p from " + Product.class.getName() + " p");
        QueryResults resultsB = oqlB.execute();
        resultsB.close();
        
        dbB.commit();
        dbB.close();

        jdoB = null;
    }

    public void testLoadConfAandB() throws Exception {
        // Load configuration for A first followed by B.
        // Why should this behave different as loading A and B
        // from one configuration containing both?
        JDOManager.loadConfiguration(
                getClass().getResource("jdo-conf-a.xml").toString());
        JDOManager.loadConfiguration(
                getClass().getResource("jdo-conf-b.xml").toString());

        JDOManager jdoA = JDOManager.createInstance("test-a");
        Database dbA = jdoA.getDatabase();
        dbA.begin();
        
        OQLQuery oqlA = dbA.getOQLQuery(
                "select p from " + Product.class.getName() + " p");
        QueryResults resultsA = oqlA.execute();
        resultsA.close();
        
        dbA.commit();
        dbA.close();

        jdoA = null;

        JDOManager jdoB = JDOManager.createInstance("test-b");
        Database dbB = jdoB.getDatabase();
        dbB.begin();
        
        OQLQuery oqlB = dbB.getOQLQuery(
                "select p from " + Product.class.getName() + " p");
        QueryResults resultsB = oqlB.execute();
        resultsB.close();
        
        dbB.commit();
        dbB.close();

        jdoB = null;
    }

    public void testLoadConfBandAB() throws Exception {
        // Load configuration for B first followed by a configuration
        // containing A and B. This means B is loaded twice.
        // What shell we do with the second configuration containg A and B?
        // Ignore it completely, only ignore second configuration of B or
        // or replace the first configuration of B with the second one.
        JDOManager.loadConfiguration(
                getClass().getResource("jdo-conf-b.xml").toString());
        JDOManager.loadConfiguration(
                getClass().getResource("jdo-conf-ab.xml").toString());

        JDOManager jdoA = JDOManager.createInstance("test-a");
        Database dbA = jdoA.getDatabase();
        dbA.begin();
        
        OQLQuery oqlA = dbA.getOQLQuery(
                "select p from " + Product.class.getName() + " p");
        QueryResults resultsA = oqlA.execute();
        resultsA.close();
        
        dbA.commit();
        dbA.close();

        jdoA = null;

        JDOManager jdoB = JDOManager.createInstance("test-b");
        Database dbB = jdoB.getDatabase();
        dbB.begin();
        
        OQLQuery oqlB = dbB.getOQLQuery(
                "select p from " + Product.class.getName() + " p");
        QueryResults resultsB = oqlB.execute();
        resultsB.close();
        
        dbB.commit();
        dbB.close();

        jdoB = null;
    }

    public void testConfABandA() throws Exception {
        // Load configuration for A and B first followed by a configuration
        // containing only A. This means A is loaded twice.
        // What shell we do with the second configuration containg A?
        // Ignore it completely, only ignore second configuration of A or
        // or replace the first configuration of A with the second one.
        JDOManager.loadConfiguration(
                getClass().getResource("jdo-conf-ab.xml").toString());
        JDOManager.loadConfiguration(
                getClass().getResource("jdo-conf-a.xml").toString());

        JDOManager jdoA = JDOManager.createInstance("test-a");
        Database dbA = jdoA.getDatabase();
        dbA.begin();
        
        OQLQuery oqlA = dbA.getOQLQuery(
                "select p from " + Product.class.getName() + " p");
        QueryResults resultsA = oqlA.execute();
        resultsA.close();
        
        dbA.commit();
        dbA.close();

        jdoA = null;

        JDOManager jdoB = JDOManager.createInstance("test-a");
        Database dbB = jdoB.getDatabase();
        dbB.begin();
        
        OQLQuery oqlB = dbB.getOQLQuery(
                "select p from " + Product.class.getName() + " p");
        QueryResults resultsB = oqlB.execute();
        resultsB.close();
        
        dbB.commit();
        dbB.close();

        jdoB = null;

        JDOManager jdoC = JDOManager.createInstance("test-b");
        Database dbC = jdoC.getDatabase();
        dbC.begin();
        
        OQLQuery oqlC = dbC.getOQLQuery(
                "select p from " + Product.class.getName() + " p");
        QueryResults resultsC = oqlC.execute();
        resultsC.close();
        
        dbC.commit();
        dbC.close();

        jdoC = null;
    }
}
