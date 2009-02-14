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
package org.castor.cpa.test.test1158;

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
 * @author nstuart
 */
public final class Test1158 extends CPATestCase {
    private static final Log LOG = LogFactory.getLog(Test1158.class);
    
    private static final String DBNAME = "test1158";
    private static final String MAPPING = "/org/castor/cpa/test/test1158/mapping.xml";

    public static Test suite() {
        TestSuite suite = new TestSuite(Test1158.class.getName());
        
        suite.addTest(new Test1158("testLoad"));
        suite.addTest(new Test1158("testSave"));

        return suite;
    }
    
    public Test1158(final String name) {
        super(name);
    }
    
    // Test are only included/excluded for engines that have been tested with this test suite.

    public boolean include(final DatabaseEngineType engine) {
        return (engine == DatabaseEngineType.MYSQL) 
            || (engine == DatabaseEngineType.ORACLE)
            || (engine == DatabaseEngineType.SQL_SERVER)
            || (engine == DatabaseEngineType.DERBY);
    }
    
    public void testLoad() {
        try {
            Database db = getJDOManager(DBNAME, MAPPING).getDatabase();
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
                ExtendedObject obj1 = (ExtendedObject) db.load(
                        ExtendedObject.class, new Integer(1));
                obj1.setDescription2(obj1.getDescription2() + " - 1");
                db.commit();
                db.close();
                fail();
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
