package ctf.jdo.bugs.template;

import junit.framework.TestCase;

import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.JDOManager;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.QueryResults;

public final class TestTemplate extends TestCase {
    private static final String JDO_CONF_FILE = "jdo-conf.xml";
    private static final String DATABASE_NAME = "template";
    
    private JDOManager _jdo = null;

    protected void setUp() throws Exception {
        super.setUp();
        
        String config = getClass().getResource(JDO_CONF_FILE).toString();
        JDOManager.loadConfiguration(config, getClass().getClassLoader());
        _jdo = JDOManager.createInstance(DATABASE_NAME);
    }
    
    public void testQueryEntity() throws Exception {
        Database db = _jdo.getDatabase();
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
