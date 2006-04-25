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

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.castor.util.ConfigKeys;
import org.castor.util.Configuration;

/**
 * Proxy class for JDBC PreparedStatement class, to allow information gathering
 * for the purpose of SQL statement logging.
 * 
 * @author <a href="werner DOT guttmann AT gmx DOT net">Werner Guttmann</a>
 * @version $Revision$ $Date$
 * @since 1.0M3
 */
public final class PreparedStatementProxy implements PreparedStatement {

    /** Commons logger. */
    private static final Log LOG = LogFactory.getLog(PreparedStatementProxy.class);
    
    /** Has property of LocalConfiguration been read? */
    private static boolean _isConfigured = false;
    
    /** Should connections been wrapped by a proxy? */
    private static boolean _useProxies = false;
    
    /** PreparedStatement to be proxied. */
    private PreparedStatement _preparedStatement;
    
    /** Connection instance associated with this PreparedStatement */
    private Connection _connection;
    
    /** SQL Parameter mapping */
    private Map _parameters = new HashMap();
    
    /** The SQL statement to be executed  */
    private String _sqlStatement = null; 

    /** List of batch statements associated with this instance. */
    private List _batchStatements = new ArrayList();

    /**
     * Factory method for creating a PreparedStamentProxy.
     * 
     * @param statement Prepared statement to be proxied.
     * @param sql SQL string.
     * @param connection JDBC connection.
     * @return Prepared statement proxy.
     */
    public static PreparedStatement newPreparedStatementProxy(
            final PreparedStatement statement, final String sql,
            final Connection connection) {
        
        if (!_isConfigured) {
            _useProxies = Configuration.getInstance().getProperty(
                    ConfigKeys.USE_JDBC_PROXIES, true);
            _isConfigured = true;
        }

        if (!_useProxies) {
            return statement;
        }
        return new PreparedStatementProxy(statement, sql, connection);
    }
    
    /**
     * Creates an instance of this class.
     * 
     * @param statement Prepared statement to be proxied.
     * @param sql SQL string.
     * @param connection JDBC connection.
     */
    private PreparedStatementProxy(final PreparedStatement statement,
            final String sql, final Connection connection) {
        
        if (LOG.isDebugEnabled()) {
            LOG.debug ("Creating prepared statement proxy for SQL statement " + sql);
        }
        
        _preparedStatement = statement; 
        _sqlStatement = sql;
        _connection = connection;
    }
    
    /**
     * {@inheritDoc}
     * @see PreparedStatement#addBatch()
     */
    public void addBatch() throws SQLException {
        _preparedStatement.addBatch();
    }
    
    /**
     * {@inheritDoc}
     * @see PreparedStatement#addBatch(String)
     */
    public void addBatch(final String arg0) throws SQLException {
        _batchStatements.add (arg0);
        _preparedStatement.addBatch(arg0);
    }
    
    /**
     * {@inheritDoc}
     * @see PreparedStatement#cancel()
     */
    public void cancel() throws SQLException {
        _preparedStatement.cancel();
    }
    
    /**
     * {@inheritDoc}
     * @see PreparedStatement#clearBatch()
     */
    public void clearBatch() throws SQLException {
        _batchStatements.clear();
        _preparedStatement.clearBatch();
    }
    
    /**
     * {@inheritDoc}
     * @see PreparedStatement#clearParameters()
     */
    public void clearParameters() throws SQLException {
        _parameters.clear();
        _preparedStatement.clearParameters();
    }
    
    /**
     * {@inheritDoc}
     * @see PreparedStatement#clearWarnings()
     */
    public void clearWarnings() throws SQLException {
        _preparedStatement.clearWarnings();
    }
    
    /**
     * {@inheritDoc}
     * @see PreparedStatement#close()
     */
    public void close() throws SQLException {
        _preparedStatement.close();
    }
    
    /**
     * {@inheritDoc}
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(final Object arg0) {
        return _preparedStatement.equals(arg0);
    }
    
    /**
     * {@inheritDoc}
     * @see PreparedStatement#execute()
     */
    public boolean execute() throws SQLException {
        return _preparedStatement.execute();
    }
    
    /**
     * {@inheritDoc}
     * @see java.sql.Statement#execute(java.lang.String)
     */
    public boolean execute(final String arg0) throws SQLException {
        _sqlStatement = arg0;
        return _preparedStatement.execute(arg0);
    }
    
    /**
     * {@inheritDoc}
     * @see java.sql.Statement#execute(java.lang.String, int)
     */
    public boolean execute(final String arg0, final int arg1) throws SQLException {
        _sqlStatement = arg0;
        return _preparedStatement.execute(arg0, arg1);
    }
    
    /**
     * {@inheritDoc}
     * @see java.sql.Statement#execute(java.lang.String, int[])
     */
    public boolean execute(final String arg0, final int[] arg1) throws SQLException {
        _sqlStatement = arg0;
        return _preparedStatement.execute(arg0, arg1);
    }
    
    /**
     * {@inheritDoc}
     * @see java.sql.Statement#execute(java.lang.String, java.lang.String[])
     */
    public boolean execute(final String arg0, final String[] arg1) throws SQLException {
        _sqlStatement = arg0;
        return _preparedStatement.execute(arg0, arg1);
    }
    
    /**
     * {@inheritDoc}
     * @see java.sql.Statement#executeBatch()
     */
    public int[] executeBatch() throws SQLException {
        return _preparedStatement.executeBatch();
    }
    
    /**
     * {@inheritDoc}
     * @see java.sql.PreparedStatement#executeQuery()
     */
    public ResultSet executeQuery() throws SQLException {
        return _preparedStatement.executeQuery();
    }
    
    /**
     * {@inheritDoc}
     * @see java.sql.Statement#executeQuery(java.lang.String)
     */
    public ResultSet executeQuery(final String arg0) throws SQLException {
        _sqlStatement = arg0;
        return _preparedStatement.executeQuery(arg0);
    }
    
    /**
     * {@inheritDoc}
     * @see java.sql.PreparedStatement#executeUpdate()
     */
    public int executeUpdate() throws SQLException {
        return _preparedStatement.executeUpdate();
    }
    
    /**
     * {@inheritDoc}
     * @see java.sql.Statement#executeUpdate(java.lang.String)
     */
    public int executeUpdate(final String arg0) throws SQLException {
        _sqlStatement = arg0;
        return _preparedStatement.executeUpdate(arg0);
    }
    
    /**
     * {@inheritDoc}
     * @see java.sql.Statement#executeUpdate(java.lang.String, int)
     */
    public int executeUpdate(final String arg0, final int arg1)
    throws SQLException {
        _sqlStatement = arg0;
        return _preparedStatement.executeUpdate(arg0, arg1);
    }
    
    /**
     * {@inheritDoc}
     * @see java.sql.Statement#executeUpdate(java.lang.String, int[])
     */
    public int executeUpdate(final String arg0, final int[] arg1)
    throws SQLException {
        _sqlStatement = arg0;
        return _preparedStatement.executeUpdate(arg0, arg1);
    }
    
    /**
     * {@inheritDoc}
     * @see java.sql.Statement#executeUpdate(java.lang.String, java.lang.String[])
     */
    public int executeUpdate(final String arg0, final String[] arg1)
    throws SQLException {
        _sqlStatement = arg0;
        return _preparedStatement.executeUpdate(arg0, arg1);
    }
    
    /**
     * {@inheritDoc}
     * @see java.sql.Statement#getConnection()
     */
    public Connection getConnection() throws SQLException {
        return _connection;
    }
    
    /**
     * {@inheritDoc}
     * @see java.sql.Statement#getFetchDirection()
     */
    public int getFetchDirection() throws SQLException {
        return _preparedStatement.getFetchDirection();
    }
    
    /**
     * {@inheritDoc}
     * @see java.sql.Statement#getFetchSize()
     */
    public int getFetchSize() throws SQLException {
        return _preparedStatement.getFetchSize();
    }
    
    /**
     * {@inheritDoc}
     * @see java.sql.Statement#getGeneratedKeys()
     */
    public ResultSet getGeneratedKeys() throws SQLException {
        return _preparedStatement.getGeneratedKeys();
    }
    
    /**
     * {@inheritDoc}
     * @see java.sql.Statement#getMaxFieldSize()
     */
    public int getMaxFieldSize() throws SQLException {
        return _preparedStatement.getMaxFieldSize();
    }
    
    /**
     * {@inheritDoc}
     * @see java.sql.Statement#getMaxRows()
     */
    public int getMaxRows() throws SQLException {
        return _preparedStatement.getMaxRows();
    }
    
    /**
     * {@inheritDoc}
     * @see java.sql.PreparedStatement#getMetaData()
     */
    public ResultSetMetaData getMetaData() throws SQLException {
        return _preparedStatement.getMetaData();
    }
    
    /**
     * {@inheritDoc}
     * @see java.sql.Statement#getMoreResults()
     */
    public boolean getMoreResults() throws SQLException {
        return _preparedStatement.getMoreResults();
    }
    
    /**
     * {@inheritDoc}
     * @see java.sql.Statement#getMoreResults(int)
     */
    public boolean getMoreResults(final int arg0) throws SQLException {
        return _preparedStatement.getMoreResults(arg0);
    }
    
    /**
     * {@inheritDoc}
     * @see java.sql.PreparedStatement#getParameterMetaData()
     */
    public ParameterMetaData getParameterMetaData() throws SQLException {
        return _preparedStatement.getParameterMetaData();
    }
    
    /**
     * {@inheritDoc}
     * @see java.sql.Statement#getQueryTimeout()
     */
    public int getQueryTimeout() throws SQLException {
        return _preparedStatement.getQueryTimeout();
    }
    
    /**
     * {@inheritDoc}
     * @see java.sql.Statement#getResultSet()
     */
    public ResultSet getResultSet() throws SQLException {
        return _preparedStatement.getResultSet();
    }
    
    /**
     * {@inheritDoc}
     * @see java.sql.Statement#getResultSetConcurrency()
     */
    public int getResultSetConcurrency() throws SQLException {
        return _preparedStatement.getResultSetConcurrency();
    }
    
    /**
     * {@inheritDoc}
     * @see java.sql.Statement#getResultSetHoldability()
     */
    public int getResultSetHoldability() throws SQLException {
        return _preparedStatement.getResultSetHoldability();
    }
    
    /**
     * {@inheritDoc}
     * @see java.sql.Statement#getResultSetType()
     */
    public int getResultSetType() throws SQLException {
        return _preparedStatement.getResultSetType();
    }
    
    /**
     * {@inheritDoc}
     * @see java.sql.Statement#getUpdateCount()
     */
    public int getUpdateCount() throws SQLException {
        return _preparedStatement.getUpdateCount();
    }
    
    /**
     * {@inheritDoc}
     * @see java.sql.Statement#getWarnings()
     */
    public SQLWarning getWarnings() throws SQLException {
        return _preparedStatement.getWarnings();
    }
    
    /**
     * {@inheritDoc}
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {
        return _preparedStatement.hashCode();
    }
    
    /**
     * {@inheritDoc}
     * @see java.sql.PreparedStatement#setArray(int, java.sql.Array)
     */
    public void setArray(final int arg0, final Array arg1) throws SQLException {
        _parameters.put(new Integer (arg0), arg1);
        _preparedStatement.setArray(arg0, arg1);
    }
    
    /**
     * {@inheritDoc}
     * @see java.sql.PreparedStatement#setAsciiStream(int, java.io.InputStream, int)
     */
    public void setAsciiStream(final int arg0, final InputStream arg1, final int arg2)
    throws SQLException {
        _preparedStatement.setAsciiStream(arg0, arg1, arg2);
    }
    
    /**
     * {@inheritDoc}
     * @see java.sql.PreparedStatement#setBigDecimal(int, java.math.BigDecimal)
     */
    public void setBigDecimal(final int arg0, final BigDecimal arg1)
    throws SQLException {
        _parameters.put (new Integer (arg0), arg1);
        _preparedStatement.setBigDecimal(arg0, arg1);
    }
    
    /**
     * {@inheritDoc}
     * @see java.sql.PreparedStatement#setBinaryStream(int, java.io.InputStream, int)
     */
    public void setBinaryStream(final int arg0, final InputStream arg1, final int arg2)
    throws SQLException {
        _parameters.put (new Integer (arg0), arg1);
        _preparedStatement.setBinaryStream(arg0, arg1, arg2);
    }
    
    /**
     * {@inheritDoc}
     * @see java.sql.PreparedStatement#setBlob(int, java.sql.Blob)
     */
    public void setBlob(final int arg0, final Blob arg1) throws SQLException {
        _parameters.put (new Integer (arg0), arg1);
        _preparedStatement.setBlob(arg0, arg1);
    }
    
    /**
     * {@inheritDoc}
     * @see java.sql.PreparedStatement#setBoolean(int, boolean)
     */
    public void setBoolean(final int arg0, final boolean arg1) throws SQLException {
        _parameters.put (new Integer (arg0), new Boolean (arg1));
        _preparedStatement.setBoolean(arg0, arg1);
    }
    
    /**
     * {@inheritDoc}
     * @see java.sql.PreparedStatement#setByte(int, byte)
     */
    public void setByte(final int arg0, final byte arg1) throws SQLException {
        _parameters.put (new Integer (arg0), new Byte(arg1));
        _preparedStatement.setByte(arg0, arg1);
    }
    
    /**
     * {@inheritDoc}
     * @see java.sql.PreparedStatement#setBytes(int, byte[])
     */
    public void setBytes(final int arg0, final byte[] arg1) throws SQLException {
        _parameters.put (new Integer (arg0), arg1);
        _preparedStatement.setBytes(arg0, arg1);
    }
    
    /**
     * {@inheritDoc}
     * @see java.sql.PreparedStatement#setCharacterStream(int, java.io.Reader, int)
     */
    public void setCharacterStream(final int arg0, final Reader arg1, final int arg2)
    throws SQLException {
        _parameters.put (new Integer (arg0), arg1);
        _preparedStatement.setCharacterStream(arg0, arg1, arg2);
    }
    
    /**
     * {@inheritDoc}
     * @see java.sql.PreparedStatement#setClob(int, java.sql.Clob)
     */
    public void setClob(final int arg0, final Clob arg1) throws SQLException {
        _parameters.put (new Integer (arg0), arg1);
        _preparedStatement.setClob(arg0, arg1);
    }
    
    /**
     * {@inheritDoc}
     * @see java.sql.Statement#setCursorName(java.lang.String)
     */
    public void setCursorName(final String arg0) throws SQLException {
        _preparedStatement.setCursorName(arg0);
    }
    
    /**
     * {@inheritDoc}
     * @see java.sql.PreparedStatement#setDate(int, java.sql.Date)
     */
    public void setDate(final int arg0, final Date arg1) throws SQLException {
        _parameters.put (new Integer (arg0), arg1);
        _preparedStatement.setDate(arg0, arg1);
    }
    
    /**
     * {@inheritDoc}
     * @see java.sql.PreparedStatement#setDate(int, java.sql.Date, java.util.Calendar)
     */
    public void setDate(final int arg0, final Date arg1, final Calendar arg2)
    throws SQLException {
        _parameters.put (new Integer (arg0), arg1);
        _preparedStatement.setDate(arg0, arg1, arg2);
    }
    
    /**
     * {@inheritDoc}
     * @see java.sql.PreparedStatement#setDouble(int, double)
     */
    public void setDouble(final int arg0, final double arg1) throws SQLException {
        _parameters.put (new Integer (arg0), new Double (arg1));
        _preparedStatement.setDouble(arg0, arg1);
    }
    
    /**
     * {@inheritDoc}
     * @see java.sql.Statement#setEscapeProcessing(boolean)
     */
    public void setEscapeProcessing(final boolean arg0) throws SQLException {
        _preparedStatement.setEscapeProcessing(arg0);
    }
    
    /**
     * {@inheritDoc}
     * @see java.sql.Statement#setFetchDirection(int)
     */
    public void setFetchDirection(final int arg0) throws SQLException {
        _preparedStatement.setFetchDirection(arg0);
    }
    
    /**
     * {@inheritDoc}
     * @see java.sql.Statement#setFetchSize(int)
     */
    public void setFetchSize(final int arg0) throws SQLException {
        _preparedStatement.setFetchSize(arg0);
    }
    
    /**
     * {@inheritDoc}
     * @see java.sql.PreparedStatement#setFloat(int, float)
     */
    public void setFloat(final int arg0, final float arg1) throws SQLException {
        _parameters.put (new Integer (arg0), new Float (arg1));
        _preparedStatement.setFloat(arg0, arg1);
    }
    
    /**
     * {@inheritDoc}
     * @see java.sql.PreparedStatement#setInt(int, int)
     */
    public void setInt(final int arg0, final int arg1) throws SQLException {
        _parameters.put (new Integer (arg0), new Integer (arg1));
        _preparedStatement.setInt(arg0, arg1);
    }
    
    /**
     * {@inheritDoc}
     * @see java.sql.PreparedStatement#setLong(int, long)
     */
    public void setLong(final int arg0, final long arg1) throws SQLException {
        _parameters.put (new Integer (arg0), new Long (arg1));
        _preparedStatement.setLong(arg0, arg1);
    }
    
    /**
     * {@inheritDoc}
     * @see java.sql.Statement#setMaxFieldSize(int)
     */
    public void setMaxFieldSize(final int arg0) throws SQLException {
        _preparedStatement.setMaxFieldSize(arg0);
    }
    
    /**
     * {@inheritDoc}
     * @see java.sql.Statement#setMaxRows(int)
     */
    public void setMaxRows(final int arg0) throws SQLException {
        _preparedStatement.setMaxRows(arg0);
    }
    
    /**
     * {@inheritDoc}
     * @see java.sql.PreparedStatement#setNull(int, int)
     */
    public void setNull(final int arg0, final int arg1) throws SQLException {
        _parameters.put (new Integer (arg0), "null");
        _preparedStatement.setNull(arg0, arg1);
    }
    
    /**
     * {@inheritDoc}
     * @see java.sql.PreparedStatement#setNull(int, int, java.lang.String)
     */
    public void setNull(final int arg0, final int arg1, final String arg2)
    throws SQLException {
        _parameters.put (new Integer (arg0), "null");
        _preparedStatement.setNull(arg0, arg1, arg2);
    }
    
    /**
     * {@inheritDoc}
     * @see java.sql.PreparedStatement#setObject(int, java.lang.Object)
     */
    public void setObject(final int arg0, final Object arg1) throws SQLException {
        _parameters.put (new Integer (arg0), arg1);
        _preparedStatement.setObject(arg0, arg1);
    }
    
    /**
     * {@inheritDoc}
     * @see java.sql.PreparedStatement#setObject(int, java.lang.Object, int)
     */
    public void setObject(final int arg0, final Object arg1, final int arg2)
    throws SQLException {
        _parameters.put (new Integer (arg0), arg1);
        _preparedStatement.setObject(arg0, arg1, arg2);
    }
    
    /**
     * {@inheritDoc}
     * @see java.sql.PreparedStatement#setObject(int, java.lang.Object, int, int)
     */
    public void setObject(final int arg0, final Object arg1,
            final int arg2, final int arg3) throws SQLException {
        
        _parameters.put (new Integer (arg0), arg1);
        _preparedStatement.setObject(arg0, arg1, arg2, arg3);
    }
    
    /**
     * {@inheritDoc}
     * @see java.sql.Statement#setQueryTimeout(int)
     */
    public void setQueryTimeout(final int arg0) throws SQLException {
        _preparedStatement.setQueryTimeout(arg0);
    }
    
    /**
     * {@inheritDoc}
     * @see java.sql.PreparedStatement#setRef(int, java.sql.Ref)
     */
    public void setRef(final int arg0, final Ref arg1) throws SQLException {
        _preparedStatement.setRef(arg0, arg1);
    }
    
    /**
     * {@inheritDoc}
     * @see java.sql.PreparedStatement#setShort(int, short)
     */
    public void setShort(final int arg0, final short arg1) throws SQLException {
        _parameters.put (new Integer (arg0), new Short (arg1));
        _preparedStatement.setShort(arg0, arg1);
    }
    
    /**
     * {@inheritDoc}
     * @see java.sql.PreparedStatement#setString(int, java.lang.String)
     */
    public void setString(final int arg0, final String arg1) throws SQLException {
        _parameters.put (new Integer (arg0), arg1);
        _preparedStatement.setString(arg0, arg1);
    }
    
    /**
     * {@inheritDoc}
     * @see java.sql.PreparedStatement#setTime(int, java.sql.Time)
     */
    public void setTime(final int arg0, final Time arg1) throws SQLException {
        _parameters.put (new Integer (arg0), arg1);
        _preparedStatement.setTime(arg0, arg1);
    }
    
    /**
     * {@inheritDoc}
     * @see java.sql.PreparedStatement#setTime(int, java.sql.Time, java.util.Calendar)
     */
    public void setTime(final int arg0, final Time arg1, final Calendar arg2)
    throws SQLException {
        _parameters.put (new Integer (arg0), arg1);
        _preparedStatement.setTime(arg0, arg1, arg2);
    }
    
    /**
     * {@inheritDoc}
     * @see java.sql.PreparedStatement#setTimestamp(int, java.sql.Timestamp)
     */
    public void setTimestamp(final int arg0, final Timestamp arg1)
    throws SQLException {
        _parameters.put (new Integer (arg0), arg1);
        _preparedStatement.setTimestamp(arg0, arg1);
    }
    
    /**
     * {@inheritDoc}
     * @see java.sql.PreparedStatement
     *      #setTimestamp(int, java.sql.Timestamp, java.util.Calendar)
     */
    public void setTimestamp(final int arg0, final Timestamp arg1, final Calendar arg2)
    throws SQLException {
        _parameters.put (new Integer (arg0), arg1);
        _preparedStatement.setTimestamp(arg0, arg1, arg2);
    }
    
    /**
     * {@inheritDoc}
     * @deprecated
     * @see java.sql.PreparedStatement#setUnicodeStream(int, java.io.InputStream, int)
     */
    public void setUnicodeStream(final int arg0, final InputStream arg1, final int arg2)
    throws SQLException {
        _preparedStatement.setUnicodeStream(arg0, arg1, arg2);
    }
    
    /**
     * {@inheritDoc}
     * @see java.sql.PreparedStatement#setURL(int, java.net.URL)
     */
    public void setURL(final int arg0, final URL arg1) throws SQLException {
        _parameters.put (new Integer (arg0), arg1);
        _preparedStatement.setURL(arg0, arg1);
    }
    
    /**
     * {@inheritDoc}
     * @see java.lang.Object#toString()
     */
    public String toString() {
        StringBuffer buffer = new StringBuffer ();
        StringTokenizer tokenizer = new StringTokenizer(_sqlStatement, "?");
        String partOfStatement;
        List parameterValues = new ArrayList(_parameters.keySet());
        Collections.sort(parameterValues); 
        Iterator iter = parameterValues.iterator();
        Object key = null;
        while (tokenizer.hasMoreTokens()) {
            partOfStatement = tokenizer.nextToken();
            if (iter.hasNext()) {
                key = iter.next(); 
                buffer.append(partOfStatement);
                buffer.append("'" + _parameters.get (key).toString() + "'");
            } else {
                buffer.append(partOfStatement);
                buffer.append("?");
            }
        }
        return buffer.toString();
    }
}
