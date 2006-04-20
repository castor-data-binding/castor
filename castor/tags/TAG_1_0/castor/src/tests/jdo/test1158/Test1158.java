/*
 * TestRunner.java
 *
 * Created on June 23, 2005, 12:59 PM
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package jdo.test1158;

import junit.framework.TestCase;

import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.JDOManager;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.mapping.MappingException;

/**
 * @author nstuart
 */
public class Test1158 extends TestCase {
    private static JDOManager manager;
    
    public void setUp() {
        if (manager == null) {
            try {
                JDOManager.loadConfiguration(getClass().getResource("jdo-conf.xml").toString());
                manager = JDOManager.createInstance("test-db");
            } catch (MappingException pe) {
                pe.printStackTrace();
                fail();
            }
        }
    }
    
    public void testLoad() {
        try {
            Database db = manager.getDatabase();
            db.begin();
            try {
                db.load(ExtendedObject.class, new Integer(1));
                db.commit();
                db.close();
                fail();
            } catch (ClassCastException e) {
                db.rollback();
                db.close();
            }
        } catch (PersistenceException e) {
            e.printStackTrace();
            fail();
        }
    }
    
    public void testSave() {
        Database db = null;
        try {
            db = manager.getDatabase();
            db.begin();
            try {
                ExtendedObject obj1 = (ExtendedObject) db.load(ExtendedObject.class, new Integer(1));
                obj1.setDescription2(obj1.getDescription2() + " - 1");
                db.commit();
                db.close();
                fail();
            } catch (ClassCastException e) {
                db.rollback();
                db.close();
            }
        } catch (PersistenceException e) {
            e.printStackTrace();
            fail();
        }
    }
}
