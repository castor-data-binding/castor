package org.castor.cpa.test.test1355;

import java.util.Iterator;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.castor.cpa.test.framework.CPATestCase;
import org.castor.cpa.test.framework.xml.types.DatabaseEngineType;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.QueryResults;
import org.exolab.castor.mapping.AccessMode;

public final class Test1355 extends CPATestCase {
    private static final Log LOG = LogFactory.getLog(Test1355.class);
    
    private static final String DBNAME = "test1355";
    private static final String MAPPING = "/org/castor/cpa/test/test1355/mapping.xml";

    public static Test suite() {
        TestSuite suite = new TestSuite(Test1355.class.getName());
        
        suite.addTest(new Test1355("testLoadLazyCollectionWithoutLazyOneToOne"));
        suite.addTest(new Test1355("testLoadLazyCollectionWithLazyOneToOne"));

        return suite;
    }
    
    public Test1355(final String name) {
        super(name);
    }
    
    public boolean include(final DatabaseEngineType engine) {
        return (engine == DatabaseEngineType.MYSQL)
            || (engine == DatabaseEngineType.DERBY)
            || (engine == DatabaseEngineType.ORACLE);
    }
    
    /**
     * Load
     */
    public void testLoadLazyCollectionWithoutLazyOneToOne() throws Exception {
        Database database = getJDOManager(DBNAME, MAPPING).getDatabase();
        database.begin();

        String oql = "SELECT o FROM " + GolfCourse.class.getName() + " o WHERE id=$1";
        OQLQuery query = database.getOQLQuery(oql);
        query.bind(1);
        QueryResults queryResults = query.execute(AccessMode.ReadOnly);
        while (queryResults.hasMore()) {
            GolfCourse course = (GolfCourse) queryResults.next();

            // shouldn't have to load city, but Castor 1.0M3 throws an exception without
            // LOG.debug("Loaded " + course.getCity().getId());

            Iterator < GolfCourseTees > tees = course.getTees().iterator();
            while (tees.hasNext()) {
                GolfCourseTees tee = tees.next();
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Loaded " + tee.getClass().getName());
                }
                Iterator < GolfCourseHole > holes = tee.getHoles().iterator();
                while (holes.hasNext()) {
                    GolfCourseHole hole = holes.next();
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("Loaded " + hole.getClass().getName());
                    }
                }
            }
        }
        database.commit();
        database.close();
    }

    /**
     * Load
     */
    public void testLoadLazyCollectionWithLazyOneToOne() throws Exception {
        Database database = getJDOManager(DBNAME, MAPPING).getDatabase();
        database.begin();

        String oql = "SELECT o FROM " + GolfCourse.class.getName() + " o WHERE id=$1";
        OQLQuery query = database.getOQLQuery(oql);
        query.bind(1);
        QueryResults queryResults = query.execute(AccessMode.ReadOnly);
        while (queryResults.hasMore()) {
            GolfCourse course = (GolfCourse) queryResults.next();

            course.getCity();

            Iterator < GolfCourseTees > tees = course.getTees().iterator();
            while (tees.hasNext()) {
                GolfCourseTees tee = tees.next();
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Loaded " + tee.getClass().getName());
                }
                Iterator < GolfCourseHole > holes = tee.getHoles().iterator();
                while (holes.hasNext()) {
                    GolfCourseHole hole = holes.next();
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("Loaded " + hole.getClass().getName());
                    }
                }
            }
        }
        database.commit();
        database.close();
    }
}
