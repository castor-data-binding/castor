/*
 * Copyright 2005 Ralf Joachim
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
package ctf.jdo.special.test1214;

import java.net.URL;

import junit.framework.TestCase;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.castor.jdo.engine.DatabaseRegistry;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.JDOManager;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.QueryResults;
import org.exolab.castor.mapping.MappingException;

/**
 * @author < a href="werner.guttmann@hmx.net">Werner Guttmann</a>
 */
public final class Test1214 extends TestCase {
    private static final Log LOG = LogFactory.getLog(Test1214.class);
    
    public static void main(final String[] args) throws Exception {
        Test1214 test = new Test1214();
        test.setUp();
        test.testDoubleEntities();
        test.testIntEntities();
        test.testIntAndDoubleEntities();
        test.testIntAndIntegerEntities();
        test.testIntegerEntities();
        test.testIntegerAndDoubleEntities();
        test.testLoadNullIntAndIntegerEntities();
        test.tearDown();
    }
    
    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        DatabaseRegistry.clear();
        
        super.tearDown();
    }

    public void testDoubleEntities() throws Exception {
        try {
            URL config = getClass().getResource("jdo-conf-double.xml");
            JDOManager.loadConfiguration(config.toString());
            fail();
        } catch (MappingException ex) {
            assertEquals("org.exolab.castor.mapping.MappingException",
                    ex.getClass().getName());
        }
    }

    public void testIntEntities() throws Exception {
        URL config = getClass().getResource("jdo-conf-int.xml");
        JDOManager.loadConfiguration(config.toString());
        JDOManager jdo = JDOManager.createInstance("test1214-int");

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
        URL config = getClass().getResource("jdo-conf-int-and-double.xml");
        JDOManager.loadConfiguration(config.toString());
        JDOManager jdo = JDOManager.createInstance("test1214-int-and-double");
        
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
        URL config = getClass().getResource("jdo-conf-int-and-integer.xml");
        JDOManager.loadConfiguration(config.toString());
        JDOManager jdo = JDOManager.createInstance("test1214-int-and-integer");
        
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
        URL config = getClass().getResource("jdo-conf-integer.xml");
        JDOManager.loadConfiguration(config.toString());
        JDOManager jdo = JDOManager.createInstance("test1214-integer");
        
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
        URL config = getClass().getResource("jdo-conf-integer-and-double.xml");
        JDOManager.loadConfiguration(config.toString());
        JDOManager jdo = JDOManager.createInstance("test1214-integer-and-double");
        
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
        URL config = getClass().getResource("jdo-conf-int-and-integer.xml");
        JDOManager.loadConfiguration(config.toString());
        JDOManager jdo = JDOManager.createInstance("test1214-int-and-integer");
        
        Database db = jdo.getDatabase();
        
        db.begin();
        IntAndIntegerEntity iEnt = (IntAndIntegerEntity) db.load(
                IntAndIntegerEntity.class, new Integer(5));
        db.commit();
        
        assertNull(iEnt.getProperty());
        assertTrue(iEnt.getId() == 5);
        
        jdo = null;
    }
}
