/*
 * Copyright 2011
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
package org.castor.cpa.test.test3065;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.castor.cpa.test.framework.CPATestCase;
import org.castor.cpa.test.framework.xml.types.DatabaseEngineType;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.mapping.MappingException;

/**
 * @author wensheng dou
 */
public final class Test3065 extends CPATestCase {
    private static final Log LOG = LogFactory.getLog(Test3065.class);
    
    private static final String DBNAME = "test3065";
    private static final String MAPPING = "/org/castor/cpa/test/test3065/mapping.xml";

    public static Test suite() {
        TestSuite suite = new TestSuite(Test3065.class.getName());
        
        suite.addTest(new Test3065("testLoadExtendedObject"));
        suite.addTest(new Test3065("testLoadExtendedExtendedObject"));
        suite.addTest(new Test3065("testLoadExtendedObject2"));
        suite.addTest(new Test3065("testLoadBaseObject"));
        suite.addTest(new Test3065("testSave"));

        return suite;
    }
    
    public Test3065(final String name) {
        super(name);
    }
    
    // Test are only included/excluded for engines that have been tested with this test suite.

    public boolean include(final DatabaseEngineType engine) {
        return (engine == DatabaseEngineType.DERBY)
            || (engine == DatabaseEngineType.MYSQL)
            || (engine == DatabaseEngineType.ORACLE)
            || (engine == DatabaseEngineType.POSTGRESQL);
    }
    
    public void testLoadExtendedObject() {
        try {
            Database db = getJDOManager(DBNAME, MAPPING).getDatabase();
            db.begin();
            try {
                db.load(ExtendedObject.class, new Integer(1));
                db.commit();
                db.close();
            } catch (ClassCastException e) {
                db.rollback();
                db.close();
            }
        } catch (MappingException ex) {
            LOG.error(ex.getMessage(), ex);
            fail(ex.getMessage());
        } catch (PersistenceException ex) {
            LOG.error(ex.getMessage(), ex);
            fail(ex.getMessage());
        }
    }
    
    public void testLoadExtendedExtendedObject() {
        try {
            Database db = getJDOManager(DBNAME, MAPPING).getDatabase();
            db.begin();
            try {
                db.load(ExtendedExtendedObject.class, new Integer(1));
                db.commit();
                db.close();
            } catch (ClassCastException e) {
                db.rollback();
                db.close();
            }
        } catch (MappingException ex) {
            LOG.error(ex.getMessage(), ex);
            fail(ex.getMessage());
        } catch (PersistenceException ex) {
            LOG.error(ex.getMessage(), ex);
            fail(ex.getMessage());
        }
    }
    
    public void testLoadExtendedObject2() {
        try {
            Database db = getJDOManager(DBNAME, MAPPING).getDatabase();
            db.begin();
            try {
                db.load(ExtendedObject2.class, new Integer(2));
                db.commit();
                db.close();
            } catch (ClassCastException e) {
                db.rollback();
                db.close();
            }
        } catch (MappingException ex) {
            LOG.error(ex.getMessage(), ex);
            fail(ex.getMessage());
        } catch (PersistenceException ex) {
            LOG.error(ex.getMessage(), ex);
            fail(ex.getMessage());
        }
    }
    
    public void testLoadBaseObject() {
        try {
            Database db = getJDOManager(DBNAME, MAPPING).getDatabase();
            db.begin();
            try {
                db.load(BaseObject.class, new Integer(1));
                db.commit();
                db.close();
            } catch (ClassCastException e) {
                db.rollback();
                db.close();
            }
        } catch (MappingException ex) {
            LOG.error(ex.getMessage(), ex);
            fail(ex.getMessage());
        } catch (PersistenceException ex) {
            LOG.error(ex.getMessage(), ex);
            fail(ex.getMessage());
        }
    }
    
    public void testSave() {
        try {
            Database db = getJDOManager(DBNAME, MAPPING).getDatabase();
            db.begin();
            try {
                ExtendedObject obj1 = db.load(ExtendedObject.class, new Integer(1));
                obj1.setDescription2(obj1.getDescription2() + " - 1");
                db.commit();
                db.close();
            } catch (ClassCastException ex) {
                db.rollback();
                db.close();
            }
        } catch (MappingException ex) {
            LOG.error(ex.getMessage(), ex);
            fail(ex.getMessage());
        } catch (PersistenceException ex) {
            LOG.error(ex.getMessage(), ex);
            fail(ex.getMessage());
        }
    }
}
