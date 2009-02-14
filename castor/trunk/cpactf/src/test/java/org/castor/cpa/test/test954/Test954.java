/*
 * Copyright 2005 Werner Guttmann
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
package org.castor.cpa.test.test954;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.castor.core.util.AbstractProperties;
import org.castor.cpa.CPAProperties;
import org.castor.cpa.test.framework.CPATestCase;
import org.castor.cpa.test.framework.xml.types.DatabaseEngineType;
import org.castor.jdo.conf.JdoConf;
import org.castor.jdo.drivers.ConnectionProxy;
import org.castor.jdo.engine.AbstractConnectionFactory;
import org.castor.jdo.engine.DataSourceConnectionFactory;
import org.castor.jdo.engine.DriverConnectionFactory;
import org.castor.jdo.engine.JNDIConnectionFactory;
import org.exolab.castor.mapping.Mapping;

/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public final class Test954 extends CPATestCase {
    private static final Log LOG = LogFactory.getLog(Test954.class);
    
    private static final String DBNAME = "test954";
    private static final String MAPPING = "/org/castor/cpa/test/test954/mapping.xml";

    private Connection _connection;
    
    public static Test suite() {
        TestSuite suite = new TestSuite(Test954.class.getName());
        
        suite.addTest(new Test954("testToString"));
        suite.addTest(new Test954("testPrepareStatement"));

        return suite;
    }
    
    public Test954(final String name) {
        super(name);
    }
    
    // Test are only included/excluded for engines that have been tested with this test suite.

    public boolean include(final DatabaseEngineType engine) {
        return (engine == DatabaseEngineType.MYSQL) 
            || (engine == DatabaseEngineType.ORACLE)
            || (engine == DatabaseEngineType.SQL_SERVER)
            || (engine == DatabaseEngineType.DERBY);
   }
    
    protected void setUp() throws Exception {
        super.setUp();

        AbstractProperties properties = getProperties();
        boolean useProxies = properties.getBoolean(CPAProperties.USE_JDBC_PROXIES, true);

        AbstractConnectionFactory factory = null;
        
        JdoConf jdoConf = getJdoConf(DBNAME, MAPPING);
        if (jdoConf.getDatabase(0).getDatabaseChoice().getDriver() != null) {
            factory = new DriverConnectionFactory(jdoConf, 0, null);
        } else if (jdoConf.getDatabase(0).getDatabaseChoice().getDataSource() != null) {
            factory = new DataSourceConnectionFactory(jdoConf, 0, new Mapping());
        } else if (jdoConf.getDatabase(0).getDatabaseChoice().getDriver() != null) {
            factory = new JNDIConnectionFactory(jdoConf, 0, null);
        }
        
        factory.initializeFactory();
        
        _connection = factory.createConnection();
        
        // if connection is not wrapped by a proxy yet, create one now
        if (!useProxies) {
            ClassLoader loader = _connection.getClass().getClassLoader();
            Class < ? > [] interfaces = new Class < ? > [] {Connection.class};
            InvocationHandler handler = new ConnectionProxy(_connection, getClass().getName());
            _connection = (Connection) Proxy.newProxyInstance(loader, interfaces, handler);
        }
    }
    
    public void testToString() throws Exception {
        LOG.debug(_connection.toString());
    }
    
    public void testPrepareStatement() throws Exception {
        String sql = "select * from test954_prod where id = ?";
        PreparedStatement stmt = _connection.prepareStatement(sql);
        stmt.setInt(1, 1);
        
        LOG.debug(stmt);
        
        ResultSet results = stmt.executeQuery();
        assertNotNull(results);
        assertTrue (results.next());
    }
}