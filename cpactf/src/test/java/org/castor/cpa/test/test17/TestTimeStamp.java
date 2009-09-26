/*
 * Copyright 2009 Udai Gupta, Ralf Joachim
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
package org.castor.cpa.test.test17;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.castor.cpa.test.framework.CPATestCase;
import org.castor.cpa.test.framework.xml.types.DatabaseEngineType;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.QueryResults;

public final class TestTimeStamp extends CPATestCase {
    private static final Log LOG = LogFactory.getLog(TestTimeStamp.class);
    
    private static final String DBNAME = "test17";
    private static final String MAPPING = "/org/castor/cpa/test/test17/mapping.xml";
    private Database       _db;

    // Test are only included/excluded for engines that have been tested with this test suite.

    public boolean include(final DatabaseEngineType engine) {
        return (engine == DatabaseEngineType.DERBY)
            || (engine == DatabaseEngineType.HSQL)
            || (engine == DatabaseEngineType.MYSQL)
            || (engine == DatabaseEngineType.ORACLE)
            || (engine == DatabaseEngineType.POSTGRESQL);
    }

    /**
     * Get a JDO database
     */
    public void setUp() throws Exception { }
    
    public void tearDown() throws PersistenceException { }

    public void testRun() throws Exception {
        OQLQuery              oql;
        TimeStampableObject   object;
        QueryResults          enumeration;

        // Remove and create the object
        _db = getJDOManager(DBNAME, MAPPING).getDatabase();
        _db.begin();
        oql = _db.getOQLQuery("select obj from "
                + TimeStampableObject.class.getName() + " obj");
        enumeration = oql.execute();
        if (enumeration.hasMore()) {
            object = (TimeStampableObject) enumeration.next();
            LOG.debug("Removing object: " + object);
            _db.remove(object);
        } 
        _db.commit();
        oql.close();
        _db.close();
        
        _db = getJDOManager(DBNAME, MAPPING).getDatabase();
        _db.begin();
        object = new TimeStampableObject();
        LOG.debug("Creating new object: " + object);
        _db.create(object);
        _db.commit();
        _db.close();

        LOG.debug("Object timestamp: " + object.jdoGetTimeStamp());


        // Load the object
        _db = getJDOManager(DBNAME, MAPPING).getDatabase();
        _db.begin();
        oql = _db.getOQLQuery("select obj from "
                + TimeStampableObject.class.getName() + " obj");
        enumeration = oql.execute();

        if (enumeration.hasMore()) {
            object = (TimeStampableObject) enumeration.next();
            LOG.debug("Loaded object: " + object);
        }
        _db.rollback();
        oql.close();
        _db.close();

        // Change an attribute on the TestObject2
        object.setValue2("changed value");
        LOG.debug("Changed object: " + object);

        // Update the object
        _db = getJDOManager(DBNAME, MAPPING).getDatabase();
        _db.begin();
        LOG.debug("Updating object: " + object);
        _db.update(object);
        _db.commit();
        _db.close();

        LOG.debug("Object timestamp: " + object.jdoGetTimeStamp());
    }
}
