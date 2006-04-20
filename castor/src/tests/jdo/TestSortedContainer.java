package jdo;

import java.util.Iterator;
import java.util.SortedSet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.QueryResults;

import harness.CastorTestCase;
import harness.TestHarness;

/**
 * Test for different collection types supported by Castor JDO.
 * This test creates data objects that each has a collection as
 * a field type.
 */
public class TestSortedContainer extends CastorTestCase {
    
    private static Log LOG = LogFactory.getLog(TestSortedContainer.class);

    private JDOCategory _category;

    public TestSortedContainer(TestHarness category) {
        super(category, "tempTC128a", "Test sorted collections");
        _category = (JDOCategory) category;
    }

    public void runTest() throws Exception {
        testQuerySortedCollection();
    }
    
    /**
     * Test method.
     * @throws Exception For any exception thrown.
     */
    public void testQuerySortedCollection() throws Exception {
        Database db = _category.getDatabase();
        db.begin();
        
        OQLQuery query = db.getOQLQuery("SELECT c FROM "
                + SortedContainer.class.getName() + " c WHERE id = $1");
        query.bind(new Integer(1));
        QueryResults results = query.execute();
        
        SortedContainer entity = null;
        
        entity = (SortedContainer) results.next();
        assertNotNull(entity);
        assertEquals(new Integer(1), entity.getId());
        assertNotNull (entity.getTwos());
        assertEquals(2, entity.getTwos().size());
 
        SortedSet twos = entity.getTwos();
        Iterator iterator = twos.iterator();
        
        SortedContainerItem two = null;
        int i = 1;
        while (iterator.hasNext()) {
            two = (SortedContainerItem) iterator.next();
            LOG.error(two);
            assertNotNull(two);
            assertEquals(new Integer(i), two.getId());
            i++;
        }

        db.commit();
        db.close();
    }
}
