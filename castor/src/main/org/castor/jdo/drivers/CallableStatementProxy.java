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
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ParameterMetaData;
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

import org.exolab.castor.util.LocalConfiguration;
import org.exolab.castor.util.Configuration.Property;

/**
 * Proxy class for JDBC CallableStatement class, to allow information gathering
 * for the purpose of SQL statement logging.
 * 
 * @author <a href="werner DOT guttmann AT gmx DOT net">Werner Guttmann</a>
 * @version $Revision$ $Date$
 * @since 1.0M3
 */
public final class CallableStatementProxy implements CallableStatement {

    /** Commons logger. */
    private static final Log LOG = LogFactory.getLog(CallableStatementProxy.class);

    /** Has property of LocalConfiguration been read? */
    private static boolean _isConfigured = false;

    /** Should connections been wrapped by a proxy? */
    private static boolean _useProxies = false;

    /** CallableStatement to be proxied. */
    private CallableStatement _callableStatement;

    /** Connection instance associated with this CallableStatement. */
    private Connection _connection;

    /** SQL Parameter mapping. */
    private Map _parameters = new HashMap();

    /** The SQL statement to be executed.  */
    private String _sqlStatement = null;

    /** List of batch statements associated with this instance. */
    private List _batchStatements = new ArrayList();

    /**
     * Factory method for creating a CallableStamentProxy
     * 
     * @param statement Callable statement to be proxied.
     * @param sql SQL string.
     * @param connection JDBC connection.
     * @return Callable statement proxy.
     */
    public static CallableStatement newCallableStatementProxy(
            final CallableStatement statement, final String sql,
            final Connection connection) {

        if (!_isConfigured) {
            String propertyValue = LocalConfiguration.getInstance().getProperty(
                Property.PROPERTY_USE_JDBC_PROXIES, "true");
            _useProxies = Boolean.valueOf(propertyValue).booleanValue();
            _isConfigured = true;
        }

        if (!_useProxies) {
            return statement;
        } else {
            return new CallableStatementProxy(statement, sql, connection);
        }
    }

    /**
     * Creates an instance of this class.
     * 
     * @param statement Callable statement to be proxied.
     * @param sql SQL string.
     * @param connection JDBC connection.
     */
    private CallableStatementProxy(final CallableStatement statement,
            final String sql, final Connection connection) {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Creating callable statement proxy for SQL statement " + sql);
        }

        _callableStatement = statement;
        _sqlStatement = sql;
        _connection = connection;
    }

    /**
     * @see java.sql.PreparedStatement#addBatch()
     */
    public void addBatch() throws SQLException {
        _callableStatement.addBatch();
    }

    /**
     * @see java.sql.Statement#addBatch(java.lang.String)
     */
    public void addBatch(final String arg0) throws SQLException {
        _batchStatements.add(arg0);
        _callableStatement.addBatch(arg0);
    }

    /**
     * @see java.sql.Statement#cancel()
     */
    public void cancel() throws SQLException {
        _callableStatement.cancel();
    }

    /**
     * @see java.sql.Statement#clearBatch()
     */
    public void clearBatch() throws SQLException {
        _batchStatements.clear();
        _callableStatement.clearBatch();
    }

    /**
     * @see PreparedStatement#clearParameters()
     */
    public void clearParameters() throws SQLException {
        _parameters.clear();
        _callableStatement.clearParameters();
    }

    /**
     * @see java.sql.Statement#clearWarnings()
     */
    public void clearWarnings() throws SQLException {
        _callableStatement.clearWarnings();
    }

    /**
     * @see java.sql.Statement#close()
     */
    public void close() throws SQLException {
        _callableStatement.close();
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(final Object arg0) {
        return _callableStatement.equals(arg0);
    }

    /**
     * @see java.sql.PreparedStatement#execute()
     */
    public boolean execute() throws SQLException {
        return _callableStatement.execute();
    }

    /**
     * @see java.sql.Statement#execute(java.lang.String)
     */
    public boolean execute(final String arg0) throws SQLException {
        _sqlStatement = arg0;
        return _callableStatement.execute(arg0);
    }

    /**
     * @see java.sql.Statement#execute(java.lang.String, int)
     */
    public boolean execute(final String arg0, final int arg1) throws SQLException {
        _sqlStatement = arg0;
        return _callableStatement.execute(arg0, arg1);
    }

    /**
     * @see java.sql.Statement#execute(java.lang.String, int[])
     */
    public boolean execute(final String arg0, final int[] arg1) throws SQLException {
        _sqlStatement = arg0;
        return _callableStatement.execute(arg0, arg1);
    }

    /**
     * @see java.sql.Statement#execute(java.lang.String, java.lang.String[])
     */
    public boolean execute(final String arg0, final String[] arg1) throws SQLException {
        _sqlStatement = arg0;
        return _callableStatement.execute(arg0, arg1);
    }

    /**
     * @see java.sql.Statement#executeBatch()
     */
    public int[] executeBatch() throws SQLException {
        return _callableStatement.executeBatch();
    }

    /**
     * @see java.sql.PreparedStatement#executeQuery()
     */
    public ResultSet executeQuery() throws SQLException {
        return _callableStatement.executeQuery();
    }

    /**
     * @see java.sql.Statement#executeQuery(java.lang.String)
     */
    public ResultSet executeQuery(final String arg0) throws SQLException {
        _sqlStatement = arg0;
        return _callableStatement.executeQuery(arg0);
    }

    /**
     * @see java.sql.PreparedStatement#executeUpdate()
     */
    public int executeUpdate() throws SQLException {
        return _callableStatement.executeUpdate();
    }

    /**
     * @see java.sql.Statement#executeUpdate(java.lang.String)
     */
    public int executeUpdate(final String arg0) throws SQLException {
        _sqlStatement = arg0;
        return _callableStatement.executeUpdate(arg0);
    }

    /**
     * @see java.sql.Statement#executeUpdate(java.lang.String, int)
     */
    public int executeUpdate(final String arg0, final int arg1) throws SQLException {
        _sqlStatement = arg0;
        return _callableStatement.executeUpdate(arg0, arg1);
    }
    
    /**
     * @see java.sql.Statement#executeUpdate(java.lang.String, int[])
     */
    public int executeUpdate(final String arg0, final int[] arg1) throws SQLException {
        _sqlStatement = arg0;
        return _callableStatement.executeUpdate(arg0, arg1);
    }

    /**
     * @see java.sql.Statement#executeUpdate(java.lang.String, java.lang.String[])
     */
    public int executeUpdate(final String arg0, final String[] arg1) throws SQLException {
        _sqlStatement = arg0;
        return _callableStatement.executeUpdate(arg0, arg1);
    }

    /**
     * @see java.sql.Statement#getConnection()
     */
    public Connection getConnection() throws SQLException {
        //return callableStatement.getConnection();
        return _connection;
    }

    /**
     * @see java.sql.Statement#getFetchDirection()
     */
    public int getFetchDirection() throws SQLException {
        return _callableStatement.getFetchDirection();
    }

    /**
     * @see java.sql.Statement#getFetchSize()
     */
    public int getFetchSize() throws SQLException {
        return _callableStatement.getFetchSize();
    }

    /**
     * @see java.sql.Statement#getGeneratedKeys()
     */
    public ResultSet getGeneratedKeys() throws SQLException {
        return _callableStatement.getGeneratedKeys();
    }

    /**
     * @see java.sql.Statement#getMaxFieldSize()
     */
    public int getMaxFieldSize() throws SQLException {
        return _callableStatement.getMaxFieldSize();
    }

    /**
     * @see java.sql.Statement#getMaxRows()
     */
    public int getMaxRows() throws SQLException {
        return _callableStatement.getMaxRows();
    }

    /**
     * @see java.sql.PreparedStatement#getMetaData()
     */
    public ResultSetMetaData getMetaData() throws SQLException {
        return _callableStatement.getMetaData();
    }

    /**
     * @see java.sql.Statement#getMoreResults()
     */
    public boolean getMoreResults() throws SQLException {
        return _callableStatement.getMoreResults();
    }

    /**
     * @see java.sql.Statement#getMoreResults(int)
     */
    public boolean getMoreResults(final int arg0) throws SQLException {
        return _callableStatement.getMoreResults(arg0);
    }

    /**
     * @see java.sql.PreparedStatement#getParameterMetaData()
     */
    public ParameterMetaData getParameterMetaData() throws SQLException {
        return _callableStatement.getParameterMetaData();
    }

    /**
     * @see java.sql.Statement#getQueryTimeout()
     */
    public int getQueryTimeout() throws SQLException {
        return _callableStatement.getQueryTimeout();
    }

    /**
     * @see java.sql.Statement#getResultSet()
     */
    public ResultSet getResultSet() throws SQLException {
        return _callableStatement.getResultSet();
    }

    /**
     * @see java.sql.Statement#getResultSetConcurrency()
     */
    public int getResultSetConcurrency() throws SQLException {
        return _callableStatement.getResultSetConcurrency();
    }
    
    /**
     * @see java.sql.Statement#getResultSetHoldability()
     */
    public int getResultSetHoldability() throws SQLException {
        return _callableStatement.getResultSetHoldability();
    }

    /**
     * @see java.sql.Statement#getResultSetType()
     */
    public int getResultSetType() throws SQLException {
        return _callableStatement.getResultSetType();
    }

    /**
     * @see java.sql.Statement#getUpdateCount()
     */
    public int getUpdateCount() throws SQLException {
        return _callableStatement.getUpdateCount();
    }

    /**
     * @see java.sql.Statement#getWarnings()
     */
    public SQLWarning getWarnings() throws SQLException {
        return _callableStatement.getWarnings();
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {
        return _callableStatement.hashCode();
    }

    /**
     * @see java.sql.PreparedStatement#setArray(int, java.sql.Array)
     */
    public void setArray(final int arg0, final Array arg1) throws SQLException {
        _parameters.put(new Integer(arg0), arg1);
        _callableStatement.setArray(arg0, arg1);
    }

    /**
     * @see java.sql.PreparedStatement#setAsciiStream(int, java.io.InputStream, int)
     */
    public void setAsciiStream(final int arg0, final InputStream arg1, final int arg2)
    throws SQLException {
        _callableStatement.setAsciiStream(arg0, arg1, arg2);
    }

    /**
     * @see java.sql.PreparedStatement#setBigDecimal(int, java.math.BigDecimal)
     */
    public void setBigDecimal(final int arg0, final BigDecimal arg1)
    throws SQLException {
        _parameters.put(new Integer(arg0), arg1);
        _callableStatement.setBigDecimal(arg0, arg1);
    }

    /**
     * @see java.sql.PreparedStatement#setBinaryStream(int, java.io.InputStream, int)
     */
    public void setBinaryStream(final int arg0, final InputStream arg1, final int arg2)
    throws SQLException {
        _parameters.put(new Integer(arg0), arg1);
        _callableStatement.setBinaryStream(arg0, arg1, arg2);
    }

    /**
     * @see java.sql.PreparedStatement#setBlob(int, java.sql.Blob)
     */
    public void setBlob(final int arg0, final Blob arg1) throws SQLException {
        _parameters.put(new Integer(arg0), arg1);
        _callableStatement.setBlob(arg0, arg1);
    }

    /**
     * @see java.sql.PreparedStatement#setBoolean(int, boolean)
     */
    public void setBoolean(final int arg0, final boolean arg1) throws SQLException {
        _parameters.put(new Integer(arg0), new Boolean(arg1));
        _callableStatement.setBoolean(arg0, arg1);
    }

    /**
     * @see java.sql.PreparedStatement#setByte(int, byte)
     */
    public void setByte(final int arg0, final byte arg1) throws SQLException {
        _parameters.put(new Integer(arg0), new Byte(arg1));
        _callableStatement.setByte(arg0, arg1);
    }

    /**
     * @see java.sql.PreparedStatement#setBytes(int, byte[])
     */
    public void setBytes(final int arg0, final byte[] arg1) throws SQLException {
        _parameters.put(new Integer(arg0), arg1);
        _callableStatement.setBytes(arg0, arg1);
    }

    /**
     * @see java.sql.PreparedStatement#setCharacterStream(int, java.io.Reader, int)
     */
    public void setCharacterStream(final int arg0, final Reader arg1, final int arg2)
    throws SQLException {
        _parameters.put(new Integer(arg0), arg1);
        _callableStatement.setCharacterStream(arg0, arg1, arg2);
    }

    /**
     * @see java.sql.PreparedStatement#setClob(int, java.sql.Clob)
     */
    public void setClob(final int arg0, final Clob arg1) throws SQLException {
        _parameters.put(new Integer(arg0), arg1);
        _callableStatement.setClob(arg0, arg1);
    }

    /**
     * @see java.sql.Statement#setCursorName(java.lang.String)
     */
    public void setCursorName(final String arg0) throws SQLException {
        _callableStatement.setCursorName(arg0);
    }

    /**
     * @see java.sql.PreparedStatement#setDate(int, java.sql.Date)
     */
    public void setDate(final int arg0, final Date arg1) throws SQLException {
        _parameters.put(new Integer(arg0), arg1);
        _callableStatement.setDate(arg0, arg1);
    }

    /**
     * @see java.sql.PreparedStatement#setDate(int, java.sql.Date, java.util.Calendar)
     */
    public void setDate(final int arg0, final Date arg1, final Calendar arg2)
    throws SQLException {
        _parameters.put(new Integer(arg0), arg1);
        _callableStatement.setDate(arg0, arg1, arg2);
    }

    /**
     * @see java.sql.PreparedStatement#setDouble(int, double)
     */
    public void setDouble(final int arg0, final double arg1) throws SQLException {
        _parameters.put(new Integer(arg0), new Double(arg1));
        _callableStatement.setDouble(arg0, arg1);
    }

    /**
     * @see java.sql.Statement#setEscapeProcessing(boolean)
     */
    public void setEscapeProcessing(final boolean arg0) throws SQLException {
        _callableStatement.setEscapeProcessing(arg0);
    }

    /**
     * @see java.sql.Statement#setFetchDirection(int)
     */
    public void setFetchDirection(final int arg0) throws SQLException {
        _callableStatement.setFetchDirection(arg0);
    }

    /**
     * @see java.sql.Statement#setFetchSize(int)
     */
    public void setFetchSize(final int arg0) throws SQLException {
        _callableStatement.setFetchSize(arg0);
    }

    /**
     * @see java.sql.PreparedStatement#setFloat(int, float)
     */
    public void setFloat(final int arg0, final float arg1) throws SQLException {
        _parameters.put(new Integer(arg0), new Float(arg1));
        _callableStatement.setFloat(arg0, arg1);
    }

    /**
     * @see java.sql.PreparedStatement#setInt(int, int)
     */
    public void setInt(final int arg0, final int arg1) throws SQLException {
        _parameters.put(new Integer(arg0), new Integer(arg1));
        _callableStatement.setInt(arg0, arg1);
    }

    /**
     * @see java.sql.PreparedStatement#setLong(int, long)
     */
    public void setLong(final int arg0, final long arg1) throws SQLException {
        _parameters.put(new Integer(arg0), new Long(arg1));
        _callableStatement.setLong(arg0, arg1);
    }

    /**
     * @see java.sql.Statement#setMaxFieldSize(int)
     */
    public void setMaxFieldSize(final int arg0) throws SQLException {
        _callableStatement.setMaxFieldSize(arg0);
    }

    /**
     * @see java.sql.Statement#setMaxRows(int)
     */
    public void setMaxRows(final int arg0) throws SQLException {
        _callableStatement.setMaxRows(arg0);
    }

    /**
     * @see java.sql.PreparedStatement#setNull(int, int)
     */
    public void setNull(final int arg0, final int arg1) throws SQLException {
        _parameters.put(new Integer(arg0), "null");
        _callableStatement.setNull(arg0, arg1);
    }
    
    /**
     * @see java.sql.PreparedStatement#setNull(int, int, java.lang.String)
     */
    public void setNull(final int arg0, final int arg1, final String arg2)
    throws SQLException {
        _parameters.put(new Integer(arg0), "null");
        _callableStatement.setNull(arg0, arg1, arg2);
    }

    /**
     * @see java.sql.PreparedStatement#setObject(int, java.lang.Object)
     */
    public void setObject(final int arg0, final Object arg1) throws SQLException {
        _parameters.put(new Integer(arg0), arg1);
        _callableStatement.setObject(arg0, arg1);
    }

    /**
     * @see java.sql.PreparedStatement#setObject(int, java.lang.Object, int)
     */
    public void setObject(final int arg0, final Object arg1, final int arg2)
    throws SQLException {
        _parameters.put(new Integer(arg0), arg1);
        _callableStatement.setObject(arg0, arg1, arg2);
    }

    /**
     * @see java.sql.PreparedStatement#setObject(int, java.lang.Object, int, int)
     */
    public void setObject(final int arg0, final Object arg1,
            final int arg2, final int arg3) throws SQLException {
        
        _parameters.put(new Integer(arg0), arg1);
        _callableStatement.setObject(arg0, arg1, arg2, arg3);
    }

    /**
     * @see java.sql.Statement#setQueryTimeout(int)
     */
    public void setQueryTimeout(final int arg0) throws SQLException {
        _callableStatement.setQueryTimeout(arg0);
    }

    /**
     * @see java.sql.PreparedStatement#setRef(int, java.sql.Ref)
     */
    public void setRef(final int arg0, final Ref arg1) throws SQLException {
        _callableStatement.setRef(arg0, arg1);
    }

    /**
     * @see java.sql.PreparedStatement#setShort(int, short)
     */
    public void setShort(final int arg0, final short arg1) throws SQLException {
        _parameters.put(new Integer(arg0), new Short(arg1));
        _callableStatement.setShort(arg0, arg1);
    }

    /**
     * @see java.sql.PreparedStatement#setString(int, java.lang.String)
     */
    public void setString(final int arg0, final String arg1) throws SQLException {
        _parameters.put(new Integer(arg0), arg1);
        _callableStatement.setString(arg0, arg1);
    }

    /**
     * @see java.sql.PreparedStatement#setTime(int, java.sql.Time)
     */
    public void setTime(final int arg0, final Time arg1) throws SQLException {
        _parameters.put(new Integer(arg0), arg1);
        _callableStatement.setTime(arg0, arg1);
    }
    
    /**
     * @see java.sql.PreparedStatement#setTime(int, java.sql.Time, java.util.Calendar)
     */
    public void setTime(final int arg0, final Time arg1, final Calendar arg2)
    throws SQLException {
        _parameters.put(new Integer(arg0), arg1);
        _callableStatement.setTime(arg0, arg1, arg2);
    }

    /**
     * @see java.sql.PreparedStatement#setTimestamp(int, java.sql.Timestamp)
     */
    public void setTimestamp(final int arg0, final Timestamp arg1) throws SQLException {
        _parameters.put(new Integer(arg0), arg1);
        _callableStatement.setTimestamp(arg0, arg1);
    }

    /**
     * @see java.sql.PreparedStatement
     *      #setTimestamp(int, java.sql.Timestamp, java.util.Calendar)
     */
    public void setTimestamp(final int arg0, final Timestamp arg1, final Calendar arg2)
    throws SQLException {
        _parameters.put(new Integer(arg0), arg1);
        _callableStatement.setTimestamp(arg0, arg1, arg2);
    }

    /**
     * @deprecated
     * @see java.sql.PreparedStatement#setUnicodeStream(int, java.io.InputStream, int)
     */
    public void setUnicodeStream(final int arg0, final InputStream arg1, final int arg2)
    throws SQLException {
        _callableStatement.setUnicodeStream(arg0, arg1, arg2);
    }

    /**
     * @see java.sql.PreparedStatement#setURL(int, java.net.URL)
     */
    public void setURL(final int arg0, final URL arg1) throws SQLException {
        _parameters.put(new Integer(arg0), arg1);
        _callableStatement.setURL(arg0, arg1);
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {
        StringBuffer buffer = new StringBuffer();
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
                buffer.append("'" + _parameters.get(key).toString() + "'");
            } else {
                buffer.append(partOfStatement);
                buffer.append("?");
            }
        }
        return buffer.toString();
    }

    //----------------------------------------------------------------------
    // CallableStatement interface
    
    /**
     * @see java.sql.CallableStatement#registerOutParameter(int, int)
     */
    public void registerOutParameter(final int parameterIndex, final int sqlType)
    throws SQLException {
        _callableStatement.registerOutParameter(parameterIndex, sqlType);
    }

    /**
     * @see java.sql.CallableStatement#registerOutParameter(int, int, int)
     */
    public void registerOutParameter(final int parameterIndex, 
            final int sqlType, final int scale) throws SQLException {
        
        _callableStatement.registerOutParameter(parameterIndex, sqlType, scale);
    }

    /**
     * @see java.sql.CallableStatement#wasNull()
     */
    public boolean wasNull() throws SQLException {
        return _callableStatement.wasNull();
    }

    /**
     * @see java.sql.CallableStatement#getString(int)
     */
    public String getString(final int parameterIndex) throws SQLException {
        return _callableStatement.getString(parameterIndex);
    }

    /**
     * @see java.sql.CallableStatement#getBoolean(int)
     */
    public boolean getBoolean(final int parameterIndex) throws SQLException {
        return _callableStatement.getBoolean(parameterIndex);
    }

    /**
     * @see java.sql.CallableStatement#getByte(int)
     */
    public byte getByte(final int parameterIndex) throws SQLException {
        return _callableStatement.getByte(parameterIndex);
    }

    /**
     * @see java.sql.CallableStatement#getShort(int)
     */
    public short getShort(final int parameterIndex) throws SQLException {
        return _callableStatement.getShort(parameterIndex);
    }

    /**
     * @see java.sql.CallableStatement#getInt(int)
     */
    public int getInt(final int parameterIndex) throws SQLException {
        return _callableStatement.getInt(parameterIndex);
    }

    /**
     * @see java.sql.CallableStatement#getLong(int)
     */
    public long getLong(final int parameterIndex) throws SQLException {
        return _callableStatement.getLong(parameterIndex);
    }

    /**
     * @see java.sql.CallableStatement#getFloat(int)
     */
    public float getFloat(final int parameterIndex) throws SQLException {
        return _callableStatement.getFloat(parameterIndex);
    }

    /**
     * @see java.sql.CallableStatement#getDouble(int)
     */
    public double getDouble(final int parameterIndex) throws SQLException {
        return _callableStatement.getDouble(parameterIndex);
    }

    /**
     * @deprecated
     * @see java.sql.CallableStatement#getBigDecimal(int, int)
     */
    public BigDecimal getBigDecimal(final int parameterIndex, final int scale)
    throws SQLException {
        return _callableStatement.getBigDecimal(parameterIndex, scale);
    }

    /**
     * @see java.sql.CallableStatement#getBytes(int)
     */
    public byte[] getBytes(final int parameterIndex) throws SQLException {
        return _callableStatement.getBytes(parameterIndex);
    }

    /**
     * @see java.sql.CallableStatement#getDate(int)
     */
    public Date getDate(final int parameterIndex) throws SQLException {
        return _callableStatement.getDate(parameterIndex);
    }

    /**
     * @see java.sql.CallableStatement#getTime(int)
     */
    public Time getTime(final int parameterIndex) throws SQLException {
        return _callableStatement.getTime(parameterIndex);
    }

    /**
     * @see java.sql.CallableStatement#getTimestamp(int)
     */
    public Timestamp getTimestamp(final int parameterIndex) throws SQLException {
        return _callableStatement.getTimestamp(parameterIndex);
    }

    //----------------------------------------------------------------------
    // Advanced features

    /**
     * @see java.sql.CallableStatement#getObject(int)
     */
    public Object getObject(final int parameterIndex) throws SQLException {
        return _callableStatement.getObject(parameterIndex);
    }

    //----------------------------------------------------------------------
    // JDBC 2.0

    /**
     * @see java.sql.CallableStatement#getBigDecimal(int)
     */
    public BigDecimal getBigDecimal(final int parameterIndex) throws SQLException {
        return _callableStatement.getBigDecimal(parameterIndex);
    }

    /**
     * @see java.sql.CallableStatement#getObject(int, java.util.Map)
     */
    public Object getObject(final int i, final Map map) throws SQLException {
        return _callableStatement.getObject(i, map);
    }

    /**
     * @see java.sql.CallableStatement#getRef(int)
     */
    public Ref getRef(final int i) throws SQLException {
        return _callableStatement.getRef(i);
    }

    /**
     * @see java.sql.CallableStatement#getBlob(int)
     */
    public Blob getBlob(final int i) throws SQLException {
        return _callableStatement.getBlob(i);
    }

    /**
     * @see java.sql.CallableStatement#getClob(int)
     */
    public Clob getClob(final int i) throws SQLException {
        return _callableStatement.getClob(i);
    }

    /**
     * @see java.sql.CallableStatement#getArray(int)
     */
    public Array getArray(final int i) throws SQLException {
        return _callableStatement.getArray(i);
    }

    /**
     * @see java.sql.CallableStatement#getDate(int, java.util.Calendar)
     */
    public Date getDate(final int parameterIndex, final Calendar cal)
    throws SQLException {
        return _callableStatement.getDate(parameterIndex, cal);
    }

    /**
     * @see java.sql.CallableStatement#getTime(int, java.util.Calendar)
     */
    public Time getTime(final int parameterIndex, final Calendar cal)
    throws SQLException {
        return _callableStatement.getTime(parameterIndex, cal);
    }

    /**
     * @see java.sql.CallableStatement#getTimestamp(int, java.util.Calendar)
     */
    public Timestamp getTimestamp(final int parameterIndex, final Calendar cal)
    throws SQLException {
        return _callableStatement.getTimestamp(parameterIndex, cal);
    }

    /**
     * @see java.sql.CallableStatement#registerOutParameter(int, int, java.lang.String)
     */
    public void registerOutParameter(final int paramIndex,
            final int sqlType, final String typeName) throws SQLException {
        
        _callableStatement.registerOutParameter(paramIndex, sqlType, typeName);
    }

    //--------------------------JDBC 3.0-----------------------------

    /**
     * @see java.sql.CallableStatement#registerOutParameter(java.lang.String, int)
     */
    public void registerOutParameter(final String parameterName, final int sqlType)
    throws SQLException {
        _callableStatement.registerOutParameter(parameterName, sqlType);
    }

    /**
     * @see java.sql.CallableStatement#registerOutParameter(java.lang.String, int, int)
     */
    public void registerOutParameter(final String parameterName,
            final int sqlType, final int scale) throws SQLException {
        _callableStatement.registerOutParameter(parameterName, sqlType, scale);
    }

    /**
     * @see java.sql.CallableStatement
     *      #registerOutParameter(java.lang.String, int, java.lang.String)
     */
    public void registerOutParameter(final String parameterName,
            final int sqlType, final String typeName) throws SQLException {
        _callableStatement.registerOutParameter(parameterName, sqlType, typeName);
    }

    /**
     * @see java.sql.CallableStatement#getURL(int)
     */
    public URL getURL(final int parameterIndex) throws SQLException {
        return _callableStatement.getURL(parameterIndex);
    }

    /**
     * @see java.sql.CallableStatement#setURL(java.lang.String, java.net.URL)
     */
    public void setURL(final String parameterName, final URL val) throws SQLException {
        _callableStatement.setURL(parameterName, val);
    }

    /**
     * @see java.sql.CallableStatement#setNull(java.lang.String, int)
     */
    public void setNull(final String parameterName, final int sqlType)
    throws SQLException {
        _callableStatement.setNull(parameterName, sqlType);
    }

    /**
     * @see java.sql.CallableStatement#setBoolean(java.lang.String, boolean)
     */
    public void setBoolean(final String parameterName, final boolean x)
    throws SQLException {
        _callableStatement.setBoolean(parameterName, x);
    }

    /**
     * @see java.sql.CallableStatement#setByte(java.lang.String, byte)
     */
    public void setByte(final String parameterName, final byte x)
    throws SQLException {
        _callableStatement.setByte(parameterName, x);
    }

    /**
     * @see java.sql.CallableStatement#setShort(java.lang.String, short)
     */
    public void setShort(final String parameterName, final short x)
    throws SQLException {
        _callableStatement.setShort(parameterName, x);
    }

    /**
     * @see java.sql.CallableStatement#setInt(java.lang.String, int)
     */
    public void setInt(final String parameterName, final int x)
    throws SQLException {
        _callableStatement.setInt(parameterName, x);
    }

    /**
     * @see java.sql.CallableStatement#setLong(java.lang.String, long)
     */
    public void setLong(final String parameterName, final long x)
    throws SQLException {
        _callableStatement.setLong(parameterName, x);
    }

    /**
     * @see java.sql.CallableStatement#setFloat(java.lang.String, float)
     */
    public void setFloat(final String parameterName, final float x)
    throws SQLException {
        _callableStatement.setFloat(parameterName, x);
    }

    /**
     * @see java.sql.CallableStatement#setDouble(java.lang.String, double)
     */
    public void setDouble(final String parameterName, final double x)
    throws SQLException {
        _callableStatement.setDouble(parameterName, x);
    }

    /**
     * @see java.sql.CallableStatement
     *      #setBigDecimal(java.lang.String, java.math.BigDecimal)
     */
    public void setBigDecimal(final String parameterName, final BigDecimal x)
    throws SQLException {
        _callableStatement.setBigDecimal(parameterName, x);
    }

    /**
     * @see java.sql.CallableStatement#setString(java.lang.String, java.lang.String)
     */
    public void setString(final String parameterName, final String x)
    throws SQLException {
        _callableStatement.setString(parameterName, x);
    }

    /**
     * @see java.sql.CallableStatement#setBytes(java.lang.String, byte[])
     */
    public void setBytes(final String parameterName, final byte[] x)
    throws SQLException {
        _callableStatement.setBytes(parameterName, x);
    }

    /**
     * @see java.sql.CallableStatement#setDate(java.lang.String, java.sql.Date)
     */
    public void setDate(final String parameterName, final Date x)
    throws SQLException {
        _callableStatement.setDate(parameterName, x);
    }

    /**
     * @see java.sql.CallableStatement#setTime(java.lang.String, java.sql.Time)
     */
    public void setTime(final String parameterName, final Time x) throws SQLException {
        _callableStatement.setTime(parameterName, x);
    }

    /**
     * @see java.sql.CallableStatement#setTimestamp(java.lang.String, java.sql.Timestamp)
     */
    public void setTimestamp(final String parameterName, final Timestamp x)
    throws SQLException {
        _callableStatement.setTimestamp(parameterName, x);
    }

    /**
     * @see java.sql.CallableStatement
     *      #setAsciiStream(java.lang.String, java.io.InputStream, int)
     */
    public void setAsciiStream(final String parameterName,
            final InputStream x, final int length) throws SQLException {
        
        _callableStatement.setAsciiStream(parameterName, x, length);
    }

    /**
     * @see java.sql.CallableStatement
     *      #setBinaryStream(java.lang.String, java.io.InputStream, int)
     */
    public void setBinaryStream(final String parameterName,
            final InputStream x, final int length) throws SQLException {
        
        _callableStatement.setBinaryStream(parameterName, x, length);
    }

    /**
     * @see java.sql.CallableStatement
     *      #setObject(java.lang.String, java.lang.Object, int, int)
     */
    public void setObject(final String parameterName, final Object x,
            final int targetSqlType, final int scale) throws SQLException {
        
        _callableStatement.setObject(parameterName, x, targetSqlType, scale);
    }

    /**
     * @see java.sql.CallableStatement#setObject(java.lang.String, java.lang.Object, int)
     */
    public void setObject(final String parameterName,
            final Object x, final int targetSqlType) throws SQLException {
        
        _callableStatement.setObject(parameterName, x, targetSqlType);
    }

    /**
     * @see java.sql.CallableStatement#setObject(java.lang.String, java.lang.Object)
     */
    public void setObject(final String parameterName, final Object x)
    throws SQLException {
        _callableStatement.setObject(parameterName, x);
    }

    /**
     * @see java.sql.CallableStatement
     *      #setCharacterStream(java.lang.String, java.io.Reader, int)
     */
    public void setCharacterStream(final String parameterName,
            final Reader reader, final int length) throws SQLException {
        
        _callableStatement.setCharacterStream(parameterName, reader, length);
    }

    /**
     * @see java.sql.CallableStatement
     *      #setDate(java.lang.String, java.sql.Date, java.util.Calendar)
     */
    public void setDate(final String parameterName, final Date x, final Calendar cal)
    throws SQLException {
        _callableStatement.setDate(parameterName, x, cal);
    }

    /**
     * @see java.sql.CallableStatement
     *      #setTime(java.lang.String, java.sql.Time, java.util.Calendar)
     */
    public void setTime(final String parameterName, final Time x, final Calendar cal)
    throws SQLException {
        _callableStatement.setTime(parameterName, x, cal);
    }

    /**
     * @see java.sql.CallableStatement
     *      #setTimestamp(java.lang.String, java.sql.Timestamp, java.util.Calendar)
     */
    public void setTimestamp(final String parameterName,
            final Timestamp x, final Calendar cal) throws SQLException {
        
        _callableStatement.setTimestamp(parameterName, x, cal);
    }

    /**
     * @see java.sql.CallableStatement#setNull(java.lang.String, int, java.lang.String)
     */
    public void setNull(final String parameterName,
            final int sqlType, final String typeName) throws SQLException {
        
        _callableStatement.setNull(parameterName, sqlType, typeName);
    }

    /**
     * @see java.sql.CallableStatement#getString(java.lang.String)
     */
    public String getString(final String parameterName) throws SQLException {
        return _callableStatement.getString(parameterName);
    }

    /**
     * @see java.sql.CallableStatement#getBoolean(java.lang.String)
     */
    public boolean getBoolean(final String parameterName) throws SQLException {
        return _callableStatement.getBoolean(parameterName);
    }

    /**
     * @see java.sql.CallableStatement#getByte(java.lang.String)
     */
    public byte getByte(final String parameterName) throws SQLException {
        return _callableStatement.getByte(parameterName);
    }

    /**
     * @see java.sql.CallableStatement#getShort(java.lang.String)
     */
    public short getShort(final String parameterName) throws SQLException {
        return _callableStatement.getShort(parameterName);
    }

    /**
     * @see java.sql.CallableStatement#getInt(java.lang.String)
     */
    public int getInt(final String parameterName) throws SQLException {
       return _callableStatement.getInt(parameterName);
    }

    /**
     * @see java.sql.CallableStatement#getLong(java.lang.String)
     */
    public long getLong(final String parameterName) throws SQLException {
        return _callableStatement.getLong(parameterName);
    }

    /**
     * @see java.sql.CallableStatement#getFloat(java.lang.String)
     */
    public float getFloat(final String parameterName) throws SQLException {
        return _callableStatement.getFloat(parameterName);
    }

    /**
     * @see java.sql.CallableStatement#getDouble(java.lang.String)
     */
    public double getDouble(final String parameterName) throws SQLException {
        return _callableStatement.getDouble(parameterName);
    }

    /**
     * @see java.sql.CallableStatement#getBytes(java.lang.String)
     */
    public byte[] getBytes(final String parameterName) throws SQLException {
       return _callableStatement.getBytes(parameterName);
    }

    /**
     * @see java.sql.CallableStatement#getDate(java.lang.String)
     */
    public Date getDate(final String parameterName) throws SQLException {
        return _callableStatement.getDate(parameterName);
    }

    /**
     * @see java.sql.CallableStatement#getTime(java.lang.String)
     */
    public Time getTime(final String parameterName) throws SQLException {
        return _callableStatement.getTime(parameterName);
    }

    /**
     * @see java.sql.CallableStatement#getTimestamp(java.lang.String)
     */
    public Timestamp getTimestamp(final String parameterName) throws SQLException {
        return _callableStatement.getTimestamp(parameterName);
    }

    /**
     * @see java.sql.CallableStatement#getObject(java.lang.String)
     */
    public Object getObject(final String parameterName) throws SQLException {
        return _callableStatement.getObject(parameterName);
    }

    /**
     * @see java.sql.CallableStatement#getBigDecimal(java.lang.String)
     */
    public BigDecimal getBigDecimal(final String parameterName) throws SQLException {
        return _callableStatement.getBigDecimal(parameterName);
    }

    /**
     * @see java.sql.CallableStatement#getObject(java.lang.String, java.util.Map)
     */
    public Object getObject(final String parameterName, final Map map)
    throws SQLException {
        return _callableStatement.getObject(parameterName, map);
    }

    /**
     * @see java.sql.CallableStatement#getRef(java.lang.String)
     */
    public Ref getRef(final String parameterName) throws SQLException {
        return _callableStatement.getRef(parameterName);
    }

    /**
     * @see java.sql.CallableStatement#getBlob(java.lang.String)
     */
    public Blob getBlob(final String parameterName) throws SQLException {
        return _callableStatement.getBlob(parameterName);
    }

    /**
     * @see java.sql.CallableStatement#getClob(java.lang.String)
     */
    public Clob getClob(final String parameterName) throws SQLException {
        return _callableStatement.getClob(parameterName);
    }

    /**
     * @see java.sql.CallableStatement#getArray(java.lang.String)
     */
    public Array getArray(final String parameterName) throws SQLException {
        return _callableStatement.getArray(parameterName);
    }

    /**
     * @see java.sql.CallableStatement#getDate(java.lang.String, java.util.Calendar)
     */
    public Date getDate(final String parameterName, final Calendar cal)
    throws SQLException {
        return _callableStatement.getDate(parameterName, cal);
    }

    /**
     * @see java.sql.CallableStatement#getTime(java.lang.String, java.util.Calendar)
     */
    public Time getTime(final String parameterName, final Calendar cal)
    throws SQLException {
        return _callableStatement.getTime(parameterName, cal);
    }

    /**
     * @see java.sql.CallableStatement#getTimestamp(java.lang.String, java.util.Calendar)
     */
    public Timestamp getTimestamp(final String parameterName, final Calendar cal)
    throws SQLException {
        return _callableStatement.getTimestamp(parameterName, cal);
    }

    /**
     * @see java.sql.CallableStatement#getURL(java.lang.String)
     */
    public URL getURL(final String parameterName) throws SQLException {
        return _callableStatement.getURL(parameterName);
    }
}

