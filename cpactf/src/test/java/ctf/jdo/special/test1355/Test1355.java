package ctf.jdo.special.test1355;

import java.util.Iterator;

import junit.framework.TestCase;

import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.JDOManager;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.QueryResults;
import org.exolab.castor.mapping.AccessMode;

import ctf.jdo.special.test1355.model.GolfCourse;
import ctf.jdo.special.test1355.model.GolfCourseHole;
import ctf.jdo.special.test1355.model.GolfCourseTees;

/**
 * Demonstrates a bug? present in
 */
public final class Test1355 extends TestCase {
    private JDOManager _manager = null;
    
    public static void main(final String[] args) throws Exception {
        Test1355 test = new Test1355();
        test.setUp();
        test.testLoadLazyCollectionWithoutLazyOneToOne();
        test.testLoadLazyCollectionWithLazyOneToOne();
        test.tearDown();
    }
    
    protected void setUp() throws Exception {
        String config = getClass().getResource("jdo-conf.xml").toString();
        JDOManager.loadConfiguration(config);
        _manager = JDOManager.createInstance("test1355");
    }

    protected void tearDown() { }
    
    /**
     * Load
     */
    public void testLoadLazyCollectionWithoutLazyOneToOne() throws Exception {
        Database database = _manager.getDatabase();
        database.begin();

        String oql = "SELECT o FROM " + GolfCourse.class.getName() + " o WHERE id=$1";
        OQLQuery query = database.getOQLQuery(oql);
        query.bind(1);
        QueryResults queryResults = query.execute(AccessMode.ReadOnly);
        while (queryResults.hasMore()) {
            GolfCourse course = (GolfCourse) queryResults.next();

            // shouldn't have to load city, but Castor 1.0M3 throws an exception without
            // debug("Loaded " + course.getCity().getId());

            Iterator tees = course.getTees().iterator();
            while (tees.hasNext()) {
                GolfCourseTees tee = (GolfCourseTees) tees.next();
                debug("Loaded " + tee.getClass().getName());
                Iterator holes = tee.getHoles().iterator();
                while (holes.hasNext()) {
                    GolfCourseHole hole = (GolfCourseHole) holes.next();
                    debug("Loaded " + hole.getClass().getName());
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
        Database database = _manager.getDatabase();
        database.begin();

        String oql = "SELECT o FROM " + GolfCourse.class.getName() + " o WHERE id=$1";
        OQLQuery query = database.getOQLQuery(oql);
        query.bind(1);
        QueryResults queryResults = query.execute(AccessMode.ReadOnly);
        while (queryResults.hasMore()) {
            GolfCourse course = (GolfCourse) queryResults.next();

            course.getCity();

            Iterator tees = course.getTees().iterator();
            while (tees.hasNext()) {
                GolfCourseTees tee = (GolfCourseTees) tees.next();
                debug("Loaded " + tee.getClass().getName());
                Iterator holes = tee.getHoles().iterator();
                while (holes.hasNext()) {
                    GolfCourseHole hole = (GolfCourseHole) holes.next();
                    debug("Loaded " + hole.getClass().getName());
                }
            }
        }
        database.commit();
        database.close();
    }

    private void debug(final String msg) {
        System.out.println(msg);
    }
}
