package org.castor.cpa.test.test2567;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.castor.cpa.test.framework.CPATestCase;
import org.castor.cpa.test.framework.xml.types.DatabaseEngineType;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.Query;
import org.exolab.castor.jdo.QueryResults;
import org.exolab.castor.mapping.AccessMode;

public final class TestQuery extends CPATestCase {
    private static final Log LOG = LogFactory.getLog(TestQuery.class);
    
    private static final String DBNAME = "test2567";
    private static final String MAPPING = "/org/castor/cpa/test/test2567/mapping.xml";
    
    public static Test suite() throws Exception {
        TestSuite suite = new TestSuite(TestQuery.class.getName());

        suite.addTest(new TestQuery("query"));

        return suite;
    }

    public TestQuery(final String name) {
        super(name);
    }

    // Test are only included/excluded for engines that have been tested with this test suite.

    public boolean include(final DatabaseEngineType engine) {
        return (engine == DatabaseEngineType.ORACLE)
            || (engine == DatabaseEngineType.MYSQL)
            || (engine == DatabaseEngineType.SQL_SERVER)
            || (engine == DatabaseEngineType.DERBY);
    }
    
    public void query() throws Exception {
        Database db = getJDOManager(DBNAME, MAPPING).getDatabase();
        db.begin();

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date loadDate = df.parse("2004-04-26");

        String oql = "select t from " + Entity.class.getName() + " t " + " where t.loadDate=$1";
        Query qry = db.getOQLQuery(oql);
        qry.bind(loadDate);
        QueryResults qrs = qry.execute(AccessMode.ReadOnly);
        while (qrs.hasMore()) {
            Entity entity = (Entity) qrs.next();
            if (LOG.isTraceEnabled()) { LOG.trace(entity); }
        }
        
        db.commit();
        db.close();
    }
}
