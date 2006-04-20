/*
 * Created on 18.01.2005
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package org.exolab.castor.jdo.drivers;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.exolab.castor.jdo.drivers.ConnectionProxy;

import junit.framework.TestCase;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;

/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class TestConnectionProxies extends TestCase {

	private static final String JDBC_URL = "jdbc:mysql://localhost:3306/test";
    private static final String PASSWORD = "test";
    private static final String USER_NAME = "test";
    private static final String DRIVER_NAME = "com.mysql.jdbc.Driver";
    
    private static final Log log = LogFactory.getLog (TestConnectionProxies.class);
	
	private ConnectionProxy connectionProxy = null;
	
	/* (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		
		Class.forName (DRIVER_NAME);
		Properties properties = new Properties();
		properties.put("user", USER_NAME);
		properties.put ("password", PASSWORD);
		this.connectionProxy = ConnectionProxy.newConnectionProxy(DriverManager.getConnection(JDBC_URL, properties), getClass().getName());
		
	}
	
	public void testSomething () throws Exception {
		log.debug (this.connectionProxy.toString());
	}
    
    public void testPreparedStatementProxy () throws Exception {
        PreparedStatement preparedStatement = 
            this.connectionProxy.prepareStatement("select * from test_item where iid = ?");
        preparedStatement.setInt(1, 100);
        
        log.debug (preparedStatement);
        
        ResultSet results = preparedStatement.executeQuery();
        assertNotNull(results);
        assertTrue (results.next());
    }
}