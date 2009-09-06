/*
 * Copyright 2005 Werner Guttmann, Ralf Joachim
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
package org.castor.cpa.test.test356;

import java.text.MessageFormat;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.castor.cpa.test.framework.CPATestCase;
import org.castor.cpa.test.framework.xml.types.DatabaseEngineType;
import org.castor.jdo.conf.JdoConf;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.JDOManager;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.QueryResults;
import org.exolab.castor.mapping.MappingException;

/**
 * @author < a href="werner.guttmann@hmx.net">Werner Guttmann</a>
 */
public final class Test356 extends CPATestCase {
    private static final Log LOG = LogFactory.getLog(Test356.class);
    
    private static final String DBNAME = "test356";
    private static final String MAPPING = "/org/castor/cpa/test/test356/mapping-{0}.xml";

    public static Test suite() {
        TestSuite suite = new TestSuite(Test356.class.getName());
        
        suite.addTest(new Test356("testDoubleEntities"));
        suite.addTest(new Test356("testIntEntities"));
        suite.addTest(new Test356("testIntAndDoubleEntities"));
        suite.addTest(new Test356("testIntAndIntegerEntities"));
        suite.addTest(new Test356("testIntegerEntities"));
        suite.addTest(new Test356("testIntegerAndDoubleEntities"));
        suite.addTest(new Test356("testLoadNullIntAndIntegerEntities"));

        return suite;
    }
    
    public Test356(final String name) {
        super(name);
    }
    
    // Test are only included/excluded for engines that have been tested with this test suite.

    public boolean include(final DatabaseEngineType engine) {
        return (engine == DatabaseEngineType.MYSQL) 
            || (engine == DatabaseEngineType.ORACLE)
            || (engine == DatabaseEngineType.SQL_SERVER);
    }
    
    public void testDoubleEntities() throws Exception {
        try {
            JdoConf jdoConf = createJdoConf("double");
            JDOManager.loadConfiguration(jdoConf, getJdoConfBaseURL());
            fail();
        } catch (MappingException ex) {
            assertEquals("org.exolab.castor.mapping.MappingException",
                    ex.getClass().getName());
        }
    }

    public void testIntEntities() throws Exception {
        JdoConf jdoConf = createJdoConf("int");
        JDOManager.loadConfiguration(jdoConf, getJdoConfBaseURL());
        JDOManager jdo = JDOManager.createInstance(DBNAME + "-int");

        Database db = jdo.getDatabase();
        db.begin();

        OQLQuery query = db.getOQLQuery("SELECT e FROM "
                + IntEntity.class.getName() + " e WHERE is_defined(property)");
        QueryResults results = query.execute();
        IntEntity testEntity = null;
        while (results.hasMore()) {
            testEntity = (IntEntity) results.next();
            LOG.debug("Existing entity: " + testEntity);
        }

        db.commit();
        
        jdo = null;
    }

    public void testIntAndDoubleEntities() throws Exception {
        JdoConf jdoConf = createJdoConf("int-and-double");
        JDOManager.loadConfiguration(jdoConf, getJdoConfBaseURL());
        JDOManager jdo = JDOManager.createInstance(DBNAME + "-int-and-double");
        
        Database db = jdo.getDatabase();
        db.begin();
        
        OQLQuery query = db.getOQLQuery("SELECT e FROM "
                + IntAndDoubleEntity.class.getName() + " e WHERE is_defined(property)");
        QueryResults results = query.execute();
        IntAndDoubleEntity testEntity = null;
        while (results.hasMore()) {
            testEntity = (IntAndDoubleEntity) results.next();
            LOG.debug("Existing entity: " + testEntity);
        }
        
        db.commit();
        
        jdo = null;
    }

    public void testIntAndIntegerEntities() throws Exception {
        JdoConf jdoConf = createJdoConf("int-and-integer");
        JDOManager.loadConfiguration(jdoConf, getJdoConfBaseURL());
        JDOManager jdo = JDOManager.createInstance(DBNAME + "-int-and-integer");
        
        Database db = jdo.getDatabase();
        db.begin();
        
        OQLQuery query = db.getOQLQuery("SELECT e FROM "
                + IntAndIntegerEntity.class.getName() + " e WHERE is_defined(property)");
        QueryResults results = query.execute();
        IntAndIntegerEntity testEntity = null;
        while (results.hasMore()) {
            testEntity = (IntAndIntegerEntity) results.next();
            LOG.debug("Existing entity: " + testEntity);
        }
        
        db.commit();
        
        jdo = null;
    }

    public void testIntegerEntities() throws Exception {
        JdoConf jdoConf = createJdoConf("integer");
        JDOManager.loadConfiguration(jdoConf, getJdoConfBaseURL());
        JDOManager jdo = JDOManager.createInstance(DBNAME + "-integer");
        
        Database db = jdo.getDatabase();
        db.begin();
        
        OQLQuery query = db.getOQLQuery("SELECT e FROM "
                + IntegerEntity.class.getName() + " e WHERE is_defined(property)");
        QueryResults results = query.execute();
        IntegerEntity testEntity = null;
        while (results.hasMore()) {
            testEntity = (IntegerEntity) results.next();
            LOG.debug("Existing entity: " + testEntity);
        }
        
        db.commit();
        
        jdo = null;
    }

    public void testIntegerAndDoubleEntities() throws Exception {
        JdoConf jdoConf = createJdoConf("integer-and-double");
        JDOManager.loadConfiguration(jdoConf, getJdoConfBaseURL());
        JDOManager jdo = JDOManager.createInstance(DBNAME + "-integer-and-double");
        
        Database db = jdo.getDatabase();
        db.begin();
        
        OQLQuery query = db.getOQLQuery("SELECT e FROM "
                + IntegerAndDoubleEntity.class.getName()
                + " e WHERE is_defined(property)");
        QueryResults results = query.execute();
        IntegerAndDoubleEntity testEntity = null;
        while (results.hasMore()) {
            testEntity = (IntegerAndDoubleEntity) results.next();
            LOG.debug("Existing entity: " + testEntity);
        }
        
        db.commit();
        
        jdo = null;
    }
    
    public void testLoadNullIntAndIntegerEntities() throws Exception {
        JdoConf jdoConf = createJdoConf("int-and-integer");
        JDOManager.loadConfiguration(jdoConf, getJdoConfBaseURL());
        JDOManager jdo = JDOManager.createInstance(DBNAME + "-int-and-integer");
        
        Database db = jdo.getDatabase();
        
        db.begin();
        IntAndIntegerEntity iEnt = (IntAndIntegerEntity) db.load(
                IntAndIntegerEntity.class, new Integer(5));
        db.commit();
        
        assertNull(iEnt.getProperty());
        assertTrue(iEnt.getId() == 5);
        
        jdo = null;
    }
    
    private JdoConf createJdoConf(final String extension) {
        String mapping = MessageFormat.format(MAPPING, new Object[] {extension});
        return getJdoConf(DBNAME + "-" + extension, mapping);
    }
}
