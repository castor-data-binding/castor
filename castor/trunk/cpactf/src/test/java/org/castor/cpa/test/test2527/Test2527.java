package org.castor.cpa.test.test2527;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.castor.cpa.test.framework.CPATestCase;
import org.castor.cpa.test.framework.xml.types.DatabaseEngineType;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.QueryResults;

public final class Test2527 extends CPATestCase {
    private static final Log LOG = LogFactory.getLog(Test2527.class);
    
    private static final String DBNAME = "test2527";
    private static final String MAPPING = "/org/castor/cpa/test/test2527/mapping.xml";

    public static Test suite() {
        TestSuite suite = new TestSuite(Test2527.class.getName());
        
        suite.addTest(new Test2527("test"));

        return suite;
    }
    
    public Test2527(final String name) {
        super(name);
    }
    
    public boolean include(final DatabaseEngineType engine) {
        return (engine == DatabaseEngineType.MYSQL)
            || (engine == DatabaseEngineType.ORACLE);
    }
    
    public void test() throws Exception {
        Database db = getJDOManager(DBNAME, MAPPING).getDatabase();

        db.begin();
        LOG.info("Begin transaction to query log entries");

        OQLQuery oql = db.getOQLQuery("SELECT e FROM " + LogEntry.class.getName() + " e");

        QueryResults results = oql.execute();
        while (results.hasMore())  {
            LogEntry entry = (LogEntry) results.next();
            int id = entry.getId().intValue();
            boolean isRefering = (entry instanceof ReferingLogEntry);
            boolean isException = (entry.getException() != null);
            
            if (LOG.isDebugEnabled()) {
                LOG.debug(id + "/" + isRefering + "/" + isException);
            }

            switch (id) {
            case 1:
            case 2:
            case 7:
            case 9:
            case 10:
            case 12:
            case 15:
                assertFalse(isRefering);
                assertFalse(isException);
                break;
            case 3:
            case 4:
            case 8:
            case 13:
                assertFalse(isRefering);
                assertTrue(isException);
                break;
            case 5:
            case 6:
            case 11:
            case 14:
                assertTrue(isRefering);
                assertFalse(isException);
                break;
            default:
                fail();
            }
        }

        LOG.info("End transaction to query log entries");
        db.commit();
        db.close();
    }
}
