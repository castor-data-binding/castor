package ctf.jdo.tc7x;

import harness.CastorTestCase;
import harness.TestHarness;

import java.sql.SQLException;
import java.util.List;

import jdo.JDOCategory;

import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.QueryResults;

/**
 * Test for different collection types supported by Castor JDO.
 * This test creates data objects that each has a collection as
 * a field type.
 */
public final class TestSpecialCollections extends CastorTestCase {
    private JDOCategory _category;

    public TestSpecialCollections(final TestHarness category) {
        super(category, "TC71", "Test special collections");
        _category = (JDOCategory) category;
    }

    public void setUp() throws PersistenceException, SQLException { }

    public void runTest() throws Exception {
        testQueryEntityOne();
    }
    
    protected void tearDown() throws Exception {
        super.tearDown();
    }
    
    /**
     * Test method.
     * @throws Exception For any exception thrown.
     */
    public void testQueryEntityOne() throws Exception {
        Database db = _category.getDatabase();
        db.begin();
        OQLQuery aquery = db.getOQLQuery(
                "SELECT c FROM " + Container.class.getName() + " c");
        QueryResults aresults = aquery.execute();
        int i = 1;
        while (aresults.hasMore()) {
            Container container = (Container) aresults.next();
            assertNotNull(container);
            assertEquals(new Integer(i), container.getId());
            
            List containerItems = container.getProp();
            assertNotNull(containerItems);
            assertTrue(containerItems.size() > 0);
            i++;
        }
        db.commit();
        db.close();
    }
}
