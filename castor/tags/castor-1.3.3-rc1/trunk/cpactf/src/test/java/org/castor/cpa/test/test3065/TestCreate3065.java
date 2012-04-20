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
public final class TestCreate3065 extends CPATestCase {
    private static final Log LOG = LogFactory.getLog(TestCreate3065.class);
    
    private static final String DBNAME = "test3065";
    private static final String MAPPING = "/org/castor/cpa/test/test3065/mapping.xml";

    public static Test suite() {
        TestSuite suite = new TestSuite(TestCreate3065.class.getName());
        
        suite.addTest(new TestCreate3065("testCreateExtendedObject"));
        suite.addTest(new TestCreate3065("testCreateExtendedExtendedObject"));
        suite.addTest(new TestCreate3065("testCreateExtendedObject2"));
        suite.addTest(new TestCreate3065("testCreateBaseObject"));

        return suite;
    }
    
    public TestCreate3065(final String name) {
        super(name);
    }
    
    // Test are only included/excluded for engines that have been tested with this test suite.

    public boolean include(final DatabaseEngineType engine) {
        return (engine == DatabaseEngineType.DERBY)
            || (engine == DatabaseEngineType.HSQL)
            || (engine == DatabaseEngineType.MYSQL)
            || (engine == DatabaseEngineType.ORACLE)
            || (engine == DatabaseEngineType.POSTGRESQL)
            || (engine == DatabaseEngineType.SQL_SERVER)
            || (engine == DatabaseEngineType.SAPDB);
    }
    
    public void testCreateExtendedObject() {
        try {
            Database db = getJDOManager(DBNAME, MAPPING).getDatabase();
            db.begin();
            try {
                ExtendedObject eo = new ExtendedObject();
                eo.setId(3);
                eo.setSaved(true);
                eo.setDescription("Object 3");
                eo.setDescription2("This is an extended object 3");
                db.create(eo);
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
    
    public void testCreateExtendedExtendedObject() {
        try {
            Database db = getJDOManager(DBNAME, MAPPING).getDatabase();
            db.begin();
            try {
                ExtendedExtendedObject eo = new ExtendedExtendedObject();
                eo.setId(4);
                eo.setDescription("Object 4");
                eo.setDescription2("Extended object 4");
                eo.setDescription3("ExtendedExtendedObject 4");
                db.create(eo);
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
    
    public void testCreateExtendedObject2() {
        try {
            Database db = getJDOManager(DBNAME, MAPPING).getDatabase();
            db.begin();
            try {
                ExtendedObject2 eo = new ExtendedObject2();
                eo.setId(5);
                eo.setDescription("Object 5");
                eo.setDescription2("ExtendedObject2 object 5");
                db.create(eo);
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
    
    public void testCreateBaseObject() {
        try {
            Database db = getJDOManager(DBNAME, MAPPING).getDatabase();
            db.begin();
            try {
                BaseObject bo = new BaseObject();
                bo.setId(6);
                bo.setDescription("Base Object 6");
                db.create(bo);
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
}
