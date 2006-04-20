package org.exolab.castor.jdo.drivers;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Savepoint;
import java.sql.Statement;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.exolab.castor.util.LocalConfiguration;

/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class ConnectionProxy implements java.sql.Connection {

	/** Default calling location, equals 'unknwon'. */
	private static final String DEFAULT_CALLED_BY = "unknown";
	
	/** Jakarta Common Log instance. */
	private static final Log _log = LogFactory.getLog(ConnectionProxy.class);
	
	/** Has property of LocalConfiguration been read? */
	private static boolean		_isConfigured = false;
	
	/** Should connections been wrapped by a proxy? */
	private static boolean		_useProxies = false;
	
	/** 
	 * The JDBC Connection instance to proxy.
	 */
	private Connection connection;
	
	/**
	 * Name of the class that created this ConnectionProxy instance.
	 */
	private String calledBy;
	
	/**
	 * Factory method for creating a ConnectionProxy.  
	 * @param connection The JDBC connection to proxy.
	 * @return The JDBC connection proxy.
	 */
	public static Connection newConnectionProxy(Connection connection) {
		return newConnectionProxy(connection, DEFAULT_CALLED_BY);
	}

	/**
	 * Factory method for creating a ConnectionProxy.  
	 * @param connection The JDBC connection to proxy.
	 * @param calledBy Name of the class using creating and this proxy class. 
	 * @return The JDBC connection proxy.
	 */
	public static Connection newConnectionProxy(Connection connection, String calledBy) {
        if (!_isConfigured) {
            String propertyValue = LocalConfiguration.getInstance().getProperty(
                    "org.exolab.castor.persist.useProxies", "true");
            _useProxies = Boolean.valueOf(propertyValue).booleanValue();
            _isConfigured = true;
        }
        
		if (!_useProxies) {
            return connection;
        } else {
            return new ConnectionProxy(connection, calledBy);
        }
	}

	/**
	 * Creates an instance of ConnectionProxy. 
	 * @param connection JDBC Connectio instance to be proxied.
	 * @param calledBy Name of the class using creating and this proxy class. 
	 */
	private ConnectionProxy(Connection connection, String calledBy) {
		this.connection = connection;
		this.calledBy = calledBy;
		if (_log.isDebugEnabled()) {
			_log.debug ("Creating instance of ConnectionProxy for calling class " + this.calledBy);
		}
	}

	/**
	 * @throws java.sql.SQLException
	 */
	public void clearWarnings() throws SQLException {
		this.connection.clearWarnings();
	}
	/**
	 * @throws java.sql.SQLException
	 */
	public void close() throws SQLException {
		if (_log.isDebugEnabled()) {
			_log.debug ("Closing JDBC Connection instance.");
		}
		this.connection.close();
	}
	/**
	 * @throws java.sql.SQLException
	 */
	public void commit() throws SQLException {
		if (_log.isDebugEnabled()) {
			_log.debug ("Committing JDBC Connection instance.");
		}
		this.connection.commit();
	}
	/**
	 * @return
	 * @throws java.sql.SQLException
	 */
	public Statement createStatement() throws SQLException {
		if (_log.isDebugEnabled()) {
			_log.debug ("Creating JDBC Statement for Connection instance.");
		}
		return this.connection.createStatement();
	}
	/**
	 * @param arg0
	 * @param arg1
	 * @return
	 * @throws java.sql.SQLException
	 */
	public Statement createStatement(int arg0, int arg1) throws SQLException {
		return this.connection.createStatement(arg0, arg1);
	}
	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @return
	 * @throws java.sql.SQLException
	 */
	public Statement createStatement(int arg0, int arg1, int arg2)
			throws SQLException {
		return this.connection.createStatement(arg0, arg1, arg2);
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object arg0) {
		return this.connection.equals(arg0);
	}
	/**
	 * @return
	 * @throws java.sql.SQLException
	 */
	public boolean getAutoCommit() throws SQLException {
		return this.connection.getAutoCommit();
	}
	/**
	 * @return
	 * @throws java.sql.SQLException
	 */
	public String getCatalog() throws SQLException {
		return this.connection.getCatalog();
	}
	/**
	 * @return
	 * @throws java.sql.SQLException
	 */
	public int getHoldability() throws SQLException {
		return this.connection.getHoldability();
	}
	/**
	 * @return
	 * @throws java.sql.SQLException
	 */
	public DatabaseMetaData getMetaData() throws SQLException {
		return this.connection.getMetaData();
	}
	/**
	 * @return
	 * @throws java.sql.SQLException
	 */
	public int getTransactionIsolation() throws SQLException {
		return this.connection.getTransactionIsolation();
	}
	/**
	 * @return
	 * @throws java.sql.SQLException
	 */
	public Map getTypeMap() throws SQLException {
		return this.connection.getTypeMap();
	}
	/**
	 * @return
	 * @throws java.sql.SQLException
	 */
	public SQLWarning getWarnings() throws SQLException {
		return this.connection.getWarnings();
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return this.connection.hashCode();
	}
	/**
	 * @return
	 * @throws java.sql.SQLException
	 */
	public boolean isClosed() throws SQLException {
		return this.connection.isClosed();
	}
	/**
	 * @return
	 * @throws java.sql.SQLException
	 */
	public boolean isReadOnly() throws SQLException {
		return this.connection.isReadOnly();
	}
	/**
	 * @param arg0
	 * @return
	 * @throws java.sql.SQLException
	 */
	public String nativeSQL(String arg0) throws SQLException {
		return this.connection.nativeSQL(arg0);
	}
	/**
	 * @param arg0
	 * @return
	 * @throws java.sql.SQLException
	 */
	public CallableStatement prepareCall(String arg0) throws SQLException {
		return this.connection.prepareCall(arg0);
	}
	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @return
	 * @throws java.sql.SQLException
	 */
	public CallableStatement prepareCall(String arg0, int arg1, int arg2)
			throws SQLException {
		return this.connection.prepareCall(arg0, arg1, arg2);
	}
	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @param arg3
	 * @return
	 * @throws java.sql.SQLException
	 */
	public CallableStatement prepareCall(String arg0, int arg1, int arg2,
			int arg3) throws SQLException {
		return this.connection.prepareCall(arg0, arg1, arg2, arg3);
	}
	/**
	 * @param arg0
	 * @return
	 * @throws java.sql.SQLException
	 */
	public PreparedStatement prepareStatement(String arg0) 
		throws SQLException 
	{
		if (_log.isDebugEnabled()) {
			_log.debug ("Creating JDBC Statement for Connection instance with " + arg0);
		}
		return PreparedStatementProxy.newPreparedStatementProxy(this.connection.prepareStatement(arg0), arg0, this);
	}
	/**
	 * @param arg0
	 * @param arg1
	 * @return
	 * @throws java.sql.SQLException
	 */
	public PreparedStatement prepareStatement(String arg0, int arg1)
		throws SQLException 
	{
		if (_log.isDebugEnabled()) {
			_log.debug ("Creating JDBC Statement for Connection instance with " + arg0);
		}
		return PreparedStatementProxy.newPreparedStatementProxy(this.connection.prepareStatement(arg0, arg1), arg0, this);
	}
	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @return
	 * @throws java.sql.SQLException
	 */
	public PreparedStatement prepareStatement(String arg0, int arg1, int arg2)
		throws SQLException 
	{
		if (_log.isDebugEnabled()) {
			_log.debug ("Creating JDBC Statement for Connection instance with " + arg0);
		}
		return PreparedStatementProxy.newPreparedStatementProxy(this.connection.prepareStatement(arg0, arg1, arg2), arg0, this);
	}
    
	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @param arg3
	 * @return
	 * @throws java.sql.SQLException
	 */
	public PreparedStatement prepareStatement(String arg0, int arg1, int arg2,
			int arg3) 
		throws SQLException 
	{
		if (_log.isDebugEnabled()) {
			_log.debug ("Creating JDBC Statement for Connection instance with " + arg0);
		}
		return PreparedStatementProxy.newPreparedStatementProxy(this.connection.prepareStatement(arg0, arg1, arg2, arg3), arg0, this);
	}
    
	/**
	 * @param arg0
	 * @param arg1
	 * @return
	 * @throws java.sql.SQLException
	 */
	public PreparedStatement prepareStatement(String arg0, int[] arg1)
		throws SQLException 
	{
		if (_log.isDebugEnabled()) {
			_log.debug ("Creating JDBC Statement for Connection instance with " + arg0);
		}
		return PreparedStatementProxy.newPreparedStatementProxy(this.connection.prepareStatement(arg0, arg1), arg0, this);
	}
	/**
	 * @param arg0
	 * @param arg1
	 * @return
	 * @throws java.sql.SQLException
	 */
	public PreparedStatement prepareStatement(String arg0, String[] arg1)
		throws SQLException 
	{
		if (_log.isDebugEnabled()) {
			_log.debug ("Creating JDBC Statement for Connection instance with " + arg0);
		}
		return PreparedStatementProxy.newPreparedStatementProxy(this.connection.prepareStatement(arg0, arg1), arg0, this);
	}
	/**
	 * @param arg0
	 * @throws java.sql.SQLException
	 */
	public void releaseSavepoint(Savepoint arg0) throws SQLException {
		this.connection.releaseSavepoint(arg0);
	}
	/**
	 * @throws java.sql.SQLException
	 */
	public void rollback() throws SQLException {
		if (_log.isDebugEnabled()) {
			_log.debug ("Rolling back JDBC Connection instance.");
		}
		this.connection.rollback();
	}
	/**
	 * @param arg0
	 * @throws java.sql.SQLException
	 */
	public void rollback(Savepoint arg0) throws SQLException {
		this.connection.rollback(arg0);
	}
	/**
	 * @param arg0
	 * @throws java.sql.SQLException
	 */
	public void setAutoCommit(boolean arg0) throws SQLException {
		this.connection.setAutoCommit(arg0);
	}
	/**
	 * @param arg0
	 * @throws java.sql.SQLException
	 */
	public void setCatalog(String arg0) throws SQLException {
		this.connection.setCatalog(arg0);
	}
	/**
	 * @param arg0
	 * @throws java.sql.SQLException
	 */
	public void setHoldability(int arg0) throws SQLException {
		this.connection.setHoldability(arg0);
	}
	/**
	 * @param arg0
	 * @throws java.sql.SQLException
	 */
	public void setReadOnly(boolean arg0) throws SQLException {
		this.connection.setReadOnly(arg0);
	}
	/**
	 * @return
	 * @throws java.sql.SQLException
	 */
	public Savepoint setSavepoint() throws SQLException {
		return this.connection.setSavepoint();
	}
	/**
	 * @param arg0
	 * @return
	 * @throws java.sql.SQLException
	 */
	public Savepoint setSavepoint(String arg0) throws SQLException {
		return this.connection.setSavepoint(arg0);
	}
	/**
	 * @param arg0
	 * @throws java.sql.SQLException
	 */
	public void setTransactionIsolation(int arg0) throws SQLException {
		this.connection.setTransactionIsolation(arg0);
	}
	/**
	 * @param arg0
	 * @throws java.sql.SQLException
	 */
	public void setTypeMap(Map arg0) throws SQLException {
		this.connection.setTypeMap(arg0);
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		StringBuffer buffer = new StringBuffer ();
		buffer.append (getClass().getName() + " created and called by " + this.calledBy);
		return buffer.toString();
	}
}
