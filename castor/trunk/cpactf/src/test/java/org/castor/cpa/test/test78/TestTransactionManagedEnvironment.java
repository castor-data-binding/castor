/*
 * Copyright 2008 Udai Gupta
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
package org.castor.cpa.test.test78;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.castor.cpa.test.framework.CPATestCase;
import org.castor.cpa.test.framework.xml.types.DatabaseEngineType;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.QueryResults;
import org.exolab.castor.jdo.TransactionNotInProgressException;

// Removed mockejb.jar as it is only used by this test which
// never gets executed at the moment.
// import org.mockejb.jndi.MockContextFactory;

/**
 * To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Generation - Code and Comments
 */
public final class TestTransactionManagedEnvironment extends CPATestCase {
    /**
     * The <a href="http://jakarta.apache.org/commons/logging/">Jakarta Commons
     * Logging</a> instance used for all logging.
     */
    private static final Log LOG = LogFactory
            .getLog(TestTransactionManagedEnvironment.class);
    private static final String DBNAME = "test78";
    private static final String MAPPING = "/org/castor/cpa/test/test78/mapping.xml";
    private Database _db;

    public TestTransactionManagedEnvironment(final String name) {
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

    public void setUp() throws Exception {
        _db = getJDOManager(DBNAME, MAPPING).getDatabase();
    }

    public void tearDown() throws Exception {
        if (_db.isActive()) {
            _db.rollback();
        }
        _db.close();
    }

    public void testOQL() throws Exception {
        OQLQuery oql;
        QueryResults enumeration;
        try {
            _db.begin();
            // execute some test OQL
            oql = _db.getOQLQuery("SELECT master FROM "
                    + Master.class.getName() + " master");
            enumeration = oql.execute();
            while (enumeration.hasMore()) {
                enumeration.next();
            }
            _db.commit();
        } catch (TransactionNotInProgressException e) {
            LOG.error(e.getClass().getName(), e);
            // if this exception occurs, the JDO
            // failed to correctly aquire the transaction
            // from the TransactionManager.
            fail("Transaction not aquired");
        } 
    }
}
