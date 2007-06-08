package jdo.c1355;

import java.util.Iterator;

import jdo.c1355.app.GolfCourse;
import jdo.c1355.app.GolfCourseHole;
import jdo.c1355.app.GolfCourseTees;

import junit.framework.TestCase;

import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.JDOManager;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.QueryResults;
import org.exolab.castor.mapping.AccessMode;

/**
 * Demonstrates a bug? present in
 */
public class LazyLoadRelationshipTest extends TestCase {

	private JDOManager jdoMgr = null;
    
	protected void setUp() throws Exception {
        String config = getClass().getResource("jdo-conf.xml").toString();
		JDOManager.loadConfiguration(config);
		jdoMgr = JDOManager.createInstance("test");
    }

	/**
	 * Load
	 */
	public void testLoadLazyCollectionWithoutLazyOneToOne() throws Exception {
		Database database = jdoMgr.getDatabase();
        database.begin();

        String oql = "SELECT o FROM " + GolfCourse.class.getName() + " o WHERE id=$1";
        OQLQuery query = database.getOQLQuery(oql);
        query.bind(1);
        QueryResults queryResults = query.execute(AccessMode.ReadOnly);
        while (queryResults.hasMore()) {
            GolfCourse course = (GolfCourse) queryResults.next();

            //shouldn't have to load city, but Castor 1.0M3 throws an exception without it
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
        Database database = jdoMgr.getDatabase();
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

	public void debug(String msg) {
		System.out.println(msg);
	}
}
