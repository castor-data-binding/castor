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
package org.exolab.castor.jdo.drivers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;

import junit.framework.TestCase;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.castor.jdo.drivers.ConnectionProxy;

/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public final class TestConnectionProxies extends TestCase {

    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/test";
    private static final String PASSWORD = "test";
    private static final String USER_NAME = "test";
    private static final String DRIVER_NAME = "com.mysql.jdbc.Driver";
    
    private static final Log LOG = LogFactory.getLog (TestConnectionProxies.class);
    
    private Connection _connectionProxy = null;
    
    /* (non-Javadoc)
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        
        Class.forName (DRIVER_NAME);
        Properties properties = new Properties();
        properties.put("user", USER_NAME);
        properties.put ("password", PASSWORD);
        _connectionProxy = ConnectionProxy.newConnectionProxy(
                DriverManager.getConnection(JDBC_URL, properties), getClass().getName());
        
    }
    
    public void testSomething () throws Exception {
        LOG.debug (this._connectionProxy.toString());
    }
    
    public void testPreparedStatementProxy () throws Exception {
        PreparedStatement preparedStatement = _connectionProxy.prepareStatement(
                "select * from test_item where iid = ?");
        preparedStatement.setInt(1, 100);
        
        LOG.debug (preparedStatement);
        
        ResultSet results = preparedStatement.executeQuery();
        assertNotNull(results);
        assertTrue (results.next());
    }
}