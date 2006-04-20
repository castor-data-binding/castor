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
package org.castor.jdo.drivers;

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

import org.castor.util.ConfigKeys;
import org.castor.util.Configuration;

/**
 * Proxy class for JDBC Connection class, to allow information gathering
 * for the purpose of SQL statement logging.
 * 
 * @author <a href="werner DOT guttmann AT gmx DOT net">Werner Guttmann</a>
 * @version $Revision$ $Date$
 * @since 1.0M3
 */
public final class ConnectionProxy implements java.sql.Connection {

    /** Jakarta Common Log instance. */
    private static final Log LOG = LogFactory.getLog(ConnectionProxy.class);
    
    /** Default calling location, equals 'unknwon'. */
    private static final String DEFAULT_CALLED_BY = "unknown";
    
    /** Has property of LocalConfiguration been read? */
    private static boolean _isConfigured = false;
    
    /** Should connections been wrapped by a proxy? */
    private static boolean _useProxies = false;
    
    /** The JDBC Connection instance to proxy. */
    private Connection _connection;
    
    /** Name of the class that created this ConnectionProxy instance. */
    private String _calledBy;
    
    /**
     * Factory method for creating a ConnectionProxy.  
     * @param connection The JDBC connection to proxy.
     * @return The JDBC connection proxy.
     */
    public static Connection newConnectionProxy(final Connection connection) {
        return newConnectionProxy(connection, DEFAULT_CALLED_BY);
    }

    /**
     * Factory method for creating a ConnectionProxy.
     * 
     * @param connection The JDBC connection to proxy.
     * @param calledBy Name of the class using creating and this proxy class. 
     * @return The JDBC connection proxy.
     */
    public static Connection newConnectionProxy(final Connection connection,
                                                final String calledBy) {
        
        if (!_isConfigured) {
            _useProxies = Configuration.getInstance().getProperty(
                    ConfigKeys.USE_JDBC_PROXIES, true);
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
     * 
     * @param connection JDBC Connectio instance to be proxied.
     * @param calledBy Name of the class using creating and this proxy class. 
     */
    private ConnectionProxy(final Connection connection, final String calledBy) {
        _connection = connection;
        _calledBy = calledBy;
        if (LOG.isDebugEnabled()) {
            LOG.debug("Creating instance of ConnectionProxy for calling class "
                    + _calledBy);
        }
    }

    /**
     * {@inheritDoc}
     * @see java.sql.Connection#clearWarnings()
     */
    public void clearWarnings() throws SQLException {
        _connection.clearWarnings();
    }
    
    /**
     * {@inheritDoc}
     * @see java.sql.Connection#close()
     */
    public void close() throws SQLException {
        if (LOG.isDebugEnabled()) {
            LOG.debug ("Closing JDBC Connection instance.");
        }
        _connection.close();
    }
    
    /**
     * {@inheritDoc}
     * @see java.sql.Connection#commit()
     */
    public void commit() throws SQLException {
        if (LOG.isDebugEnabled()) {
            LOG.debug ("Committing JDBC Connection instance.");
        }
        _connection.commit();
    }
    
    /**
     * {@inheritDoc}
     * @see java.sql.Connection#createStatement()
     */
    public Statement createStatement() throws SQLException {
        if (LOG.isDebugEnabled()) {
            LOG.debug ("Creating JDBC Statement for Connection instance.");
        }
        return _connection.createStatement();
    }
    
    /**
     * {@inheritDoc}
     * @see java.sql.Connection#createStatement(int, int)
     */
    public Statement createStatement(final int arg0, final int arg1)
    throws SQLException {
        return _connection.createStatement(arg0, arg1);
    }
    
    /**
     * {@inheritDoc}
     * @see java.sql.Connection#createStatement(int, int, int)
     */
    public Statement createStatement(final int arg0, final int arg1, final int arg2)
    throws SQLException {
        return _connection.createStatement(arg0, arg1, arg2);
    }
    
    /**
     * {@inheritDoc}
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(final Object arg0) {
        return _connection.equals(arg0);
    }
    
    /**
     * {@inheritDoc}
     * @see java.sql.Connection#getAutoCommit()
     */
    public boolean getAutoCommit() throws SQLException {
        return _connection.getAutoCommit();
    }
    
    /**
     * {@inheritDoc}
     * @see java.sql.Connection#getCatalog()
     */
    public String getCatalog() throws SQLException {
        return _connection.getCatalog();
    }

    /**
     * {@inheritDoc}
     * @see java.sql.Connection#getHoldability()
     */
    public int getHoldability() throws SQLException {
        return _connection.getHoldability();
    }
    
    /**
     * {@inheritDoc}
     * @see java.sql.Connection#getMetaData()
     */
    public DatabaseMetaData getMetaData() throws SQLException {
        return _connection.getMetaData();
    }
    
    /**
     * {@inheritDoc}
     * @see java.sql.Connection#getTransactionIsolation()
     */
    public int getTransactionIsolation() throws SQLException {
        return _connection.getTransactionIsolation();
    }
    
    /**
     * {@inheritDoc}
     * @see java.sql.Connection#getTypeMap()
     */
    public Map getTypeMap() throws SQLException {
        return _connection.getTypeMap();
    }
    
    /**
     * {@inheritDoc}
     * @see java.sql.Connection#getWarnings()
     */
    public SQLWarning getWarnings() throws SQLException {
        return _connection.getWarnings();
    }

    /**
     * {@inheritDoc}
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {
        return _connection.hashCode();
    }
    
    /**
     * {@inheritDoc}
     * @see java.sql.Connection#isClosed()
     */
    public boolean isClosed() throws SQLException {
        return _connection.isClosed();
    }
    
    /**
     * {@inheritDoc}
     * @see java.sql.Connection#isReadOnly()
     */
    public boolean isReadOnly() throws SQLException {
        return _connection.isReadOnly();
    }
    
    /**
     * {@inheritDoc}
     * @see java.sql.Connection#nativeSQL(java.lang.String)
     */
    public String nativeSQL(final String arg0) throws SQLException {
        return _connection.nativeSQL(arg0);
    }
    
    /**
     * {@inheritDoc}
     * @see java.sql.Connection#prepareCall(java.lang.String)
     */
    public CallableStatement prepareCall(final String arg0) throws SQLException {
        return CallableStatementProxy.newCallableStatementProxy(
                _connection.prepareCall(arg0), arg0, this);
    }
    
    /**
     * {@inheritDoc}
     * @see java.sql.Connection#prepareCall(java.lang.String, int, int)
     */
    public CallableStatement prepareCall(final String arg0,
            final int arg1, final int arg2) throws SQLException {
        
        return CallableStatementProxy.newCallableStatementProxy(
                _connection.prepareCall(arg0, arg1, arg2), arg0, this);
    }
    
    /**
     * {@inheritDoc}
     * @see java.sql.Connection#prepareCall(java.lang.String, int, int, int)
     */
    public CallableStatement prepareCall(final String arg0, final int arg1,
            final int arg2, final int arg3) throws SQLException {
        
        return CallableStatementProxy.newCallableStatementProxy(
                _connection.prepareCall(arg0, arg1, arg2, arg3), arg0, this);
    }
    
    /**
     * {@inheritDoc}
     * @see java.sql.Connection#prepareStatement(java.lang.String)
     */
    public PreparedStatement prepareStatement(final String arg0) throws SQLException {
        if (LOG.isDebugEnabled()) {
            LOG.debug ("Creating JDBC Statement for Connection instance with " + arg0);
        }
        return PreparedStatementProxy.newPreparedStatementProxy(
                _connection.prepareStatement(arg0), arg0, this);
    }
    
    /**
     * {@inheritDoc}
     * @see java.sql.Connection#prepareStatement(java.lang.String, int)
     */
    public PreparedStatement prepareStatement(final String arg0, final int arg1)
    throws SQLException {
        if (LOG.isDebugEnabled()) {
            LOG.debug ("Creating JDBC Statement for Connection instance with " + arg0);
        }
        return PreparedStatementProxy.newPreparedStatementProxy(
                _connection.prepareStatement(arg0, arg1), arg0, this);
    }
    
    /**
     * {@inheritDoc}
     * @see java.sql.Connection#prepareStatement(java.lang.String, int, int)
     */
    public PreparedStatement prepareStatement(final String arg0,
            final int arg1, final int arg2) throws SQLException {
        
        if (LOG.isDebugEnabled()) {
            LOG.debug ("Creating JDBC Statement for Connection instance with " + arg0);
        }
        return PreparedStatementProxy.newPreparedStatementProxy(
                _connection.prepareStatement(arg0, arg1, arg2), arg0, this);
    }
    
    /**
     * {@inheritDoc}
     * @see java.sql.Connection#prepareStatement(java.lang.String, int, int, int)
     */
    public PreparedStatement prepareStatement(final String arg0, final int arg1,
            final int arg2, final int arg3) throws SQLException {
        
        if (LOG.isDebugEnabled()) {
            LOG.debug ("Creating JDBC Statement for Connection instance with " + arg0);
        }
        return PreparedStatementProxy.newPreparedStatementProxy(
                _connection.prepareStatement(arg0, arg1, arg2, arg3), arg0, this);
    }
    
    /**
     * {@inheritDoc}
     * @see java.sql.Connection#prepareStatement(java.lang.String, int[])
     */
    public PreparedStatement prepareStatement(final String arg0, final int[] arg1)
    throws SQLException {
        if (LOG.isDebugEnabled()) {
            LOG.debug ("Creating JDBC Statement for Connection instance with " + arg0);
        }
        return PreparedStatementProxy.newPreparedStatementProxy(
                _connection.prepareStatement(arg0, arg1), arg0, this);
    }
    
    /**
     * {@inheritDoc}
     * @see java.sql.Connection#prepareStatement(java.lang.String, java.lang.String[])
     */
    public PreparedStatement prepareStatement(final String arg0, final String[] arg1)
    throws SQLException {
        if (LOG.isDebugEnabled()) {
            LOG.debug ("Creating JDBC Statement for Connection instance with " + arg0);
        }
        return PreparedStatementProxy.newPreparedStatementProxy(
                _connection.prepareStatement(arg0, arg1), arg0, this);
    }
    
    /**
     * {@inheritDoc}
     * @see java.sql.Connection#releaseSavepoint(java.sql.Savepoint)
     */
    public void releaseSavepoint(final Savepoint arg0) throws SQLException {
        _connection.releaseSavepoint(arg0);
    }
    
    /**
     * {@inheritDoc}
     * @see java.sql.Connection#rollback()
     */
    public void rollback() throws SQLException {
        if (LOG.isDebugEnabled()) {
            LOG.debug ("Rolling back JDBC Connection instance.");
        }
        _connection.rollback();
    }
    
    /**
     * {@inheritDoc}
     * @see java.sql.Connection#rollback(java.sql.Savepoint)
     */
    public void rollback(final Savepoint arg0) throws SQLException {
        _connection.rollback(arg0);
    }
    
    /**
     * {@inheritDoc}
     * @see java.sql.Connection#setAutoCommit(boolean)
     */
    public void setAutoCommit(final boolean arg0) throws SQLException {
        _connection.setAutoCommit(arg0);
    }
    
    /**
     * {@inheritDoc}
     * @see java.sql.Connection#setCatalog(java.lang.String)
     */
    public void setCatalog(final String arg0) throws SQLException {
        _connection.setCatalog(arg0);
    }
    
    /**
     * {@inheritDoc}
     * @see java.sql.Connection#setHoldability(int)
     */
    public void setHoldability(final int arg0) throws SQLException {
        _connection.setHoldability(arg0);
    }
    
    /**
     * {@inheritDoc}
     * @see java.sql.Connection#setReadOnly(boolean)
     */
    public void setReadOnly(final boolean arg0) throws SQLException {
        _connection.setReadOnly(arg0);
    }
    
    /**
     * {@inheritDoc}
     * @see java.sql.Connection#setSavepoint()
     */
    public Savepoint setSavepoint() throws SQLException {
        return _connection.setSavepoint();
    }
    
    /**
     * {@inheritDoc}
     * @see java.sql.Connection#setSavepoint(java.lang.String)
     */
    public Savepoint setSavepoint(final String arg0) throws SQLException {
        return _connection.setSavepoint(arg0);
    }
    
    /**
     * {@inheritDoc}
     * @see java.sql.Connection#setTransactionIsolation(int)
     */
    public void setTransactionIsolation(final int arg0) throws SQLException {
        _connection.setTransactionIsolation(arg0);
    }
    
    /**
     * {@inheritDoc}
     * @see java.sql.Connection#setTypeMap(java.util.Map)
     */
    public void setTypeMap(final Map arg0) throws SQLException {
        _connection.setTypeMap(arg0);
    }

    /**
     * {@inheritDoc}
     * @see java.lang.Object#toString()
     */
    public String toString() {
        StringBuffer buffer = new StringBuffer ();
        buffer.append (getClass().getName() + " created and called by " + _calledBy);
        return buffer.toString();
    }
}
