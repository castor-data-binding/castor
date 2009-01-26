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
package org.castor.cpa.test.test1002;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.castor.core.util.AbstractProperties;
import org.castor.cpa.CPAProperties;
import org.castor.cpa.test.framework.CPATestCase;
import org.castor.cpa.test.framework.xml.types.DatabaseEngineType;
import org.castor.jdo.conf.JdoConf;
import org.castor.jdo.util.JDOConfFactory;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.JDOManager;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.QueryResults;

public final class Test1002 extends CPATestCase {
    private static final Log LOG = LogFactory.getLog(Test1002.class);
    
    private static final String DBNAME = "test1002";
    private static final String MAPPING = "/org/castor/cpa/test/test1002/mapping.xml";

    private Object _memInitFlag;
    
    // Test are only included/excluded for engines that have been tested with this test suite.

    public boolean include(final DatabaseEngineType engine) {
        return (engine == DatabaseEngineType.MYSQL) 
            || (engine == DatabaseEngineType.ORACLE);
    }
    
    protected void setUp() throws Exception {
        super.setUp();
        
        AbstractProperties properties = getProperties();
        _memInitFlag = properties.getObject(CPAProperties.INITIALIZE_AT_LOAD);
        properties.put(CPAProperties.INITIALIZE_AT_LOAD, Boolean.toString(false));
    }
    
    protected void tearDown() throws Exception {
        AbstractProperties properties = getProperties();
        if (_memInitFlag != null) {
            properties.put(CPAProperties.INITIALIZE_AT_LOAD, _memInitFlag);
        } else {
            properties.remove(CPAProperties.INITIALIZE_AT_LOAD);
        }

        super.tearDown();
    }
    
    public void testLoad() {
        try {
            JdoConf jdoConf = createJdoConf(getJdoConf(DBNAME, MAPPING));
            JDOManager.loadConfiguration(jdoConf, getJdoConfBaseURL());
        } catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
            fail(ex.getMessage());
        }
        
        try {
            executeQuery(JDOManager.createInstance(DBNAME).getDatabase());
        } catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
            fail(ex.getMessage());
        }

        JDOManager.disposeInstance(DBNAME);
    }

    private JdoConf createJdoConf(final JdoConf orgConf) {
        org.castor.jdo.conf.Database orgDB = orgConf.getDatabase(0);
        
        // the jndi configuration should be ignored as it never get accessed
        org.castor.jdo.conf.Jndi jndi = JDOConfFactory.createJNDI(
                "java:comp/env/jdbc/SimpleTest");
        org.castor.jdo.conf.Database jndiDB = JDOConfFactory.createDatabase(
                orgDB.getName() + "-jndi", orgDB.getEngine(), jndi,
                "jndi:/localhost/test/WEB-INF/conf/common.xml");
        
        org.castor.jdo.conf.Database[] dbs = new org.castor.jdo.conf.Database[] {jndiDB, orgDB};
        return JDOConfFactory.createJdoConf(dbs, orgConf.getTransactionDemarcation());
    }
    
    private void executeQuery(final Database db) throws PersistenceException {
        db.begin();
        
        OQLQuery oql = db.getOQLQuery(
                "select p from " + Product.class.getName() + " p");
        QueryResults results = oql.execute();
        results.close();
        
        db.commit();
        db.close();
    }
}
