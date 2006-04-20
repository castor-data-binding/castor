/*
 * TestLoadDatabase.java
 *
 * Created on February 18, 2005, 11:37 AM
 */

package jdo.test1865;

import junit.framework.TestCase;
import org.exolab.castor.jdo.JDOManager;

/**
 *
 * @author nstuart
 */
public final class TestLoadDatabase extends TestCase {
    public static void main(final String[] args) {
        new TestLoadDatabase().testLoad();
    }
    
    /** Creates a new instance of TestLoadDatabase */
    public TestLoadDatabase() { }
    
    public void testLoad() {
        try {
            JDOManager.loadConfiguration(
                    getClass().getResource("jdo-conf.xml").toString());
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
        try {
            JDOManager jdo = JDOManager.createInstance("database");
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }
}
