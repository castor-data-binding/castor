/*
 * Copyright 2005 Nick Stuart
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ctf.jdo.special.test1158;

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
