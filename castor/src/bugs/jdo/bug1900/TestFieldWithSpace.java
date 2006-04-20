package jdo.bug1900;

import junit.framework.TestCase;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.JDOManager;

/**
 * @author nstuart
 */
public final class TestFieldWithSpace extends TestCase {
    private static final Log LOG = LogFactory.getLog(TestFieldWithSpace.class);
    
    public void testInsert() throws Exception {
        LOG.info("Starting Field with Space test.");
        
        String config = getClass().getResource("jdo-conf.xml").toString();
        JDOManager.loadConfiguration(config);
        JDOManager jdo = JDOManager.createInstance("1900");
        Database db = jdo.getDatabase();
        
        BeanObject object = new BeanObject();
        object.setField1("Test Field 1.");
        object.setField2("Test Field 2.");
        db.begin();
        db.create(object);
        db.commit();
    }
}
