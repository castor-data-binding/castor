package org.exolab.castor.jdo.drivers;

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
import org.exolab.castor.util.LocalConfiguration;

/**
 * @author Administrator
 *
 */
public class PreparedStatementProxy implements PreparedStatement {

	/** Commons logger. */
	private static final Log log = LogFactory.getLog (PreparedStatementProxy.class);
	
    /** Has property of LocalConfiguration been read? */
    private static boolean      _isConfigured = false;
    
    /** Should connections been wrapped by a proxy? */
    private static boolean      _useProxies = false;
    
	/** PreparedStatement to be proxied. */
	private PreparedStatement preparedStatement;
	
	/** Connection instance associated with this PreparedStatement */
	private Connection connection;
	
	/** SQL Parameter mapping */
	private Map parameters = new HashMap();
	
	/** The SQL statement to be executed  */
	private String sqlStatement = null; 

	/** List of batch statements associated with this instance. */
	private List batchStatements = new ArrayList();

	/**
	 * Factory method for creating a PreparedStamentProxy
	 * @param statement Prepared statement to be proxied.
	 * @param sql SQL string.
	 * @param connection JDBC connection
	 * @return Prepared statement proxy.
	 */
	public static PreparedStatement newPreparedStatementProxy(
            PreparedStatement statement, String sql, Connection connection) {
        
        if (!_isConfigured) {
            String propertyValue = LocalConfiguration.getInstance().getProperty(
                    "org.exolab.castor.persist.useProxies", "true");
            _useProxies = Boolean.valueOf(propertyValue).booleanValue();
            _isConfigured = true;
        }

		if (!_useProxies) {
            return statement;
		} else {
            return new PreparedStatementProxy(statement, sql, connection);
        }
	}
	
	/**
	 * Creates an instance of this class.
	 * @param statement Prepared statement to be proxied.
	 * @param sql SQL string.
	 * @param connection JDBC connection
	 */
	private PreparedStatementProxy(PreparedStatement statement, String sql, Connection connection) {
		
		if (log.isDebugEnabled()) {
			log.debug ("Creating prepared statement proxy for SQL statement " + sql);
		}
		
		this.preparedStatement = statement; 
		this.sqlStatement = sql;
		this.connection = connection;
	}
	
	/**
	 * @see PreparedStatement#addBatch()
	 */
	public void addBatch() throws SQLException {
		preparedStatement.addBatch();
	}
	/**
	 * @see PreparedStatement#addBatch(String)
	 */
	public void addBatch(String arg0) throws SQLException {
		batchStatements.add (arg0);
		preparedStatement.addBatch(arg0);
	}
	/**
	 * @see PreparedStatement#cancel()
	 */
	public void cancel() throws SQLException {
		preparedStatement.cancel();
	}
	/**
	 * @see PreparedStatement#clearBatch()
	 */
	public void clearBatch() throws SQLException {
		batchStatements.clear();
		preparedStatement.clearBatch();
	}
	/**
	 * @see PreparedStatement#clearParameters()
	 */
	public void clearParameters() throws SQLException {
		parameters.clear();
		preparedStatement.clearParameters();
	}
	/**
	 * @see PreparedStatement#clearWarnings()
	 */
	public void clearWarnings() throws SQLException {
		preparedStatement.clearWarnings();
	}
	/**
	 * @see PreparedStatement#close()
	 */
	public void close() throws SQLException {
		preparedStatement.close();
	}
	/*
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object arg0) {
		return preparedStatement.equals(arg0);
	}
	/**
	 * @see PreparedStatement#execute()
	 */
	public boolean execute() throws SQLException {
		return preparedStatement.execute();
	}
	/**
	 * @param arg0
	 * @return
	 * @throws java.sql.SQLException
	 */
	public boolean execute(String arg0) throws SQLException {
		sqlStatement = arg0;
		return preparedStatement.execute(arg0);
	}
	/**
	 * @param arg0
	 * @param arg1
	 * @return
	 * @throws java.sql.SQLException
	 */
	public boolean execute(String arg0, int arg1) throws SQLException {
		sqlStatement = arg0;
		return preparedStatement.execute(arg0, arg1);
	}
	/**
	 * @param arg0
	 * @param arg1
	 * @return
	 * @throws java.sql.SQLException
	 */
	public boolean execute(String arg0, int[] arg1) throws SQLException {
		sqlStatement = arg0;
		return preparedStatement.execute(arg0, arg1);
	}
	/**
	 * @param arg0
	 * @param arg1
	 * @return
	 * @throws java.sql.SQLException
	 */
	public boolean execute(String arg0, String[] arg1) throws SQLException {
		sqlStatement = arg0;
		return preparedStatement.execute(arg0, arg1);
	}
	/**
	 * @return
	 * @throws java.sql.SQLException
	 */
	public int[] executeBatch() throws SQLException {
		return preparedStatement.executeBatch();
	}
	/**
	 * @return
	 * @throws java.sql.SQLException
	 */
	public ResultSet executeQuery() throws SQLException {
		return preparedStatement.executeQuery();
	}
	/**
	 * @param arg0
	 * @return
	 * @throws java.sql.SQLException
	 */
	public ResultSet executeQuery(String arg0) throws SQLException {
		sqlStatement = arg0;
		return preparedStatement.executeQuery(arg0);
	}
	/**
	 * @return
	 * @throws java.sql.SQLException
	 */
	public int executeUpdate() throws SQLException {
		return preparedStatement.executeUpdate();
	}
	/**
	 * @param arg0
	 * @return
	 * @throws java.sql.SQLException
	 */
	public int executeUpdate(String arg0) throws SQLException {
		sqlStatement = arg0;
		return preparedStatement.executeUpdate(arg0);
	}
	/**
	 * @param arg0
	 * @param arg1
	 * @return
	 * @throws java.sql.SQLException
	 */
	public int executeUpdate(String arg0, int arg1) throws SQLException {
		sqlStatement = arg0;
		return preparedStatement.executeUpdate(arg0, arg1);
	}
	/**
	 * @param arg0
	 * @param arg1
	 * @return
	 * @throws java.sql.SQLException
	 */
	public int executeUpdate(String arg0, int[] arg1) throws SQLException {
		sqlStatement = arg0;
		return preparedStatement.executeUpdate(arg0, arg1);
	}
	/**
	 * @param arg0
	 * @param arg1
	 * @return
	 * @throws java.sql.SQLException
	 */
	public int executeUpdate(String arg0, String[] arg1) throws SQLException {
		sqlStatement = arg0;
		return preparedStatement.executeUpdate(arg0, arg1);
	}
	/**
	 * @return
	 * @throws java.sql.SQLException
	 */
	public Connection getConnection() throws SQLException {
		//return preparedStatement.getConnection();
		return connection;
	}
	/**
	 * @return
	 * @throws java.sql.SQLException
	 */
	public int getFetchDirection() throws SQLException {
		return preparedStatement.getFetchDirection();
	}
	/**
	 * @return
	 * @throws java.sql.SQLException
	 */
	public int getFetchSize() throws SQLException {
		return preparedStatement.getFetchSize();
	}
	/**
	 * @return
	 * @throws java.sql.SQLException
	 */
	public ResultSet getGeneratedKeys() throws SQLException {
		return preparedStatement.getGeneratedKeys();
	}
	/**
	 * @return
	 * @throws java.sql.SQLException
	 */
	public int getMaxFieldSize() throws SQLException {
		return preparedStatement.getMaxFieldSize();
	}
	/**
	 * @return
	 * @throws java.sql.SQLException
	 */
	public int getMaxRows() throws SQLException {
		return preparedStatement.getMaxRows();
	}
	/**
	 * @return
	 * @throws java.sql.SQLException
	 */
	public ResultSetMetaData getMetaData() throws SQLException {
		return preparedStatement.getMetaData();
	}
	/**
	 * @return
	 * @throws java.sql.SQLException
	 */
	public boolean getMoreResults() throws SQLException {
		return preparedStatement.getMoreResults();
	}
	/**
	 * @param arg0
	 * @return
	 * @throws java.sql.SQLException
	 */
	public boolean getMoreResults(int arg0) throws SQLException {
		return preparedStatement.getMoreResults(arg0);
	}
	/**
	 * @return
	 * @throws java.sql.SQLException
	 */
	public ParameterMetaData getParameterMetaData() throws SQLException {
		return preparedStatement.getParameterMetaData();
	}
	/**
	 * @return
	 * @throws java.sql.SQLException
	 */
	public int getQueryTimeout() throws SQLException {
		return preparedStatement.getQueryTimeout();
	}
	/**
	 * @return
	 * @throws java.sql.SQLException
	 */
	public ResultSet getResultSet() throws SQLException {
		return preparedStatement.getResultSet();
	}
	/**
	 * @return
	 * @throws java.sql.SQLException
	 */
	public int getResultSetConcurrency() throws SQLException {
		return preparedStatement.getResultSetConcurrency();
	}
	/**
	 * @return
	 * @throws java.sql.SQLException
	 */
	public int getResultSetHoldability() throws SQLException {
		return preparedStatement.getResultSetHoldability();
	}
	/**
	 * @return
	 * @throws java.sql.SQLException
	 */
	public int getResultSetType() throws SQLException {
		return preparedStatement.getResultSetType();
	}
	/**
	 * @return
	 * @throws java.sql.SQLException
	 */
	public int getUpdateCount() throws SQLException {
		return preparedStatement.getUpdateCount();
	}
	/**
	 * @return
	 * @throws java.sql.SQLException
	 */
	public SQLWarning getWarnings() throws SQLException {
		return preparedStatement.getWarnings();
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return preparedStatement.hashCode();
	}
	
	/**
	 * @param arg0
	 * @param arg1
	 * @throws java.sql.SQLException
	 */
	public void setArray(int arg0, Array arg1) throws SQLException {
		parameters.put(new Integer (arg0), arg1);
		preparedStatement.setArray(arg0, arg1);
	}
	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @throws java.sql.SQLException
	 */
	public void setAsciiStream(int arg0, InputStream arg1, int arg2)
			throws SQLException {
		preparedStatement.setAsciiStream(arg0, arg1, arg2);
	}
	/**
	 * @param arg0
	 * @param arg1
	 * @throws java.sql.SQLException
	 */
	public void setBigDecimal(int arg0, BigDecimal arg1) throws SQLException {
		parameters.put (new Integer (arg0), arg1);
		preparedStatement.setBigDecimal(arg0, arg1);
	}
	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @throws java.sql.SQLException
	 */
	public void setBinaryStream(int arg0, InputStream arg1, int arg2)
			throws SQLException {
		parameters.put (new Integer (arg0), arg1);
		preparedStatement.setBinaryStream(arg0, arg1, arg2);
	}
	/**
	 * @param arg0
	 * @param arg1
	 * @throws java.sql.SQLException
	 */
	public void setBlob(int arg0, Blob arg1) throws SQLException {
		parameters.put (new Integer (arg0), arg1);
		preparedStatement.setBlob(arg0, arg1);
	}
	/**
	 * @param arg0
	 * @param arg1
	 * @throws java.sql.SQLException
	 */
	public void setBoolean(int arg0, boolean arg1) throws SQLException {
		parameters.put (new Integer (arg0), new Boolean (arg1));
		preparedStatement.setBoolean(arg0, arg1);
	}
	/**
	 * @param arg0
	 * @param arg1
	 * @throws java.sql.SQLException
	 */
	public void setByte(int arg0, byte arg1) throws SQLException {
		parameters.put (new Integer (arg0), new Byte(arg1));
		preparedStatement.setByte(arg0, arg1);
	}
	/**
	 * @param arg0
	 * @param arg1
	 * @throws java.sql.SQLException
	 */
	public void setBytes(int arg0, byte[] arg1) throws SQLException {
		parameters.put (new Integer (arg0), arg1);
		preparedStatement.setBytes(arg0, arg1);
	}
	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @throws java.sql.SQLException
	 */
	public void setCharacterStream(int arg0, Reader arg1, int arg2)
			throws SQLException {
		parameters.put (new Integer (arg0), arg1);
		preparedStatement.setCharacterStream(arg0, arg1, arg2);
	}
	/**
	 * @param arg0
	 * @param arg1
	 * @throws java.sql.SQLException
	 */
	public void setClob(int arg0, Clob arg1) throws SQLException {
		parameters.put (new Integer (arg0), arg1);
		preparedStatement.setClob(arg0, arg1);
	}
	/**
	 * @param arg0
	 * @throws java.sql.SQLException
	 */
	public void setCursorName(String arg0) throws SQLException {
		preparedStatement.setCursorName(arg0);
	}
	/**
	 * @param arg0
	 * @param arg1
	 * @throws java.sql.SQLException
	 */
	public void setDate(int arg0, Date arg1) throws SQLException {
		parameters.put (new Integer (arg0), arg1);
		preparedStatement.setDate(arg0, arg1);
	}
	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @throws java.sql.SQLException
	 */
	public void setDate(int arg0, Date arg1, Calendar arg2) throws SQLException {
		parameters.put (new Integer (arg0), arg1);
		preparedStatement.setDate(arg0, arg1, arg2);
	}
	/**
	 * @param arg0
	 * @param arg1
	 * @throws java.sql.SQLException
	 */
	public void setDouble(int arg0, double arg1) throws SQLException {
		parameters.put (new Integer (arg0), new Double (arg1));
		preparedStatement.setDouble(arg0, arg1);
	}
	/**
	 * @param arg0
	 * @throws java.sql.SQLException
	 */
	public void setEscapeProcessing(boolean arg0) throws SQLException {
		preparedStatement.setEscapeProcessing(arg0);
	}
	/**
	 * @param arg0
	 * @throws java.sql.SQLException
	 */
	public void setFetchDirection(int arg0) throws SQLException {
		preparedStatement.setFetchDirection(arg0);
	}
	/**
	 * @param arg0
	 * @throws java.sql.SQLException
	 */
	public void setFetchSize(int arg0) throws SQLException {
		preparedStatement.setFetchSize(arg0);
	}
	/**
	 * @param arg0
	 * @param arg1
	 * @throws java.sql.SQLException
	 */
	public void setFloat(int arg0, float arg1) throws SQLException {
		parameters.put (new Integer (arg0), new Float (arg1));
		preparedStatement.setFloat(arg0, arg1);
	}
	/**
	 * @param arg0
	 * @param arg1
	 * @throws java.sql.SQLException
	 */
	public void setInt(int arg0, int arg1) throws SQLException {
		parameters.put (new Integer (arg0), new Integer (arg1));
		preparedStatement.setInt(arg0, arg1);
	}
	/**
	 * @param arg0
	 * @param arg1
	 * @throws java.sql.SQLException
	 */
	public void setLong(int arg0, long arg1) throws SQLException {
		parameters.put (new Integer (arg0), new Long (arg1));
		preparedStatement.setLong(arg0, arg1);
	}
	/**
	 * @param arg0
	 * @throws java.sql.SQLException
	 */
	public void setMaxFieldSize(int arg0) throws SQLException {
		preparedStatement.setMaxFieldSize(arg0);
	}
	/**
	 * @param arg0
	 * @throws java.sql.SQLException
	 */
	public void setMaxRows(int arg0) throws SQLException {
		preparedStatement.setMaxRows(arg0);
	}
	/**
	 * @param arg0
	 * @param arg1
	 * @throws java.sql.SQLException
	 */
	public void setNull(int arg0, int arg1) throws SQLException {
		parameters.put (new Integer (arg0), "null");
		preparedStatement.setNull(arg0, arg1);
	}
	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @throws java.sql.SQLException
	 */
	public void setNull(int arg0, int arg1, String arg2) throws SQLException {
		parameters.put (new Integer (arg0), "null");
		preparedStatement.setNull(arg0, arg1, arg2);
	}
	/**
	 * @param arg0
	 * @param arg1
	 * @throws java.sql.SQLException
	 */
	public void setObject(int arg0, Object arg1) throws SQLException {
		parameters.put (new Integer (arg0), arg1);
		preparedStatement.setObject(arg0, arg1);
	}
	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @throws java.sql.SQLException
	 */
	public void setObject(int arg0, Object arg1, int arg2) throws SQLException {
		parameters.put (new Integer (arg0), arg1);
		preparedStatement.setObject(arg0, arg1, arg2);
	}
	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @param arg3
	 * @throws java.sql.SQLException
	 */
	public void setObject(int arg0, Object arg1, int arg2, int arg3)
			throws SQLException {
		parameters.put (new Integer (arg0), arg1);
		preparedStatement.setObject(arg0, arg1, arg2, arg3);
	}
	/**
	 * @param arg0
	 * @throws java.sql.SQLException
	 */
	public void setQueryTimeout(int arg0) throws SQLException {
		preparedStatement.setQueryTimeout(arg0);
	}
	/**
	 * @param arg0
	 * @param arg1
	 * @throws java.sql.SQLException
	 */
	public void setRef(int arg0, Ref arg1) throws SQLException {
		preparedStatement.setRef(arg0, arg1);
	}
	/**
	 * @param arg0
	 * @param arg1
	 * @throws java.sql.SQLException
	 */
	public void setShort(int arg0, short arg1) throws SQLException {
		parameters.put (new Integer (arg0), new Short (arg1));
		preparedStatement.setShort(arg0, arg1);
	}
	/**
	 * @param arg0
	 * @param arg1
	 * @throws java.sql.SQLException
	 */
	public void setString(int arg0, String arg1) throws SQLException {
		parameters.put (new Integer (arg0), arg1);
		preparedStatement.setString(arg0, arg1);
	}
	/**
	 * @param arg0
	 * @param arg1
	 * @throws java.sql.SQLException
	 */
	public void setTime(int arg0, Time arg1) throws SQLException {
		parameters.put (new Integer (arg0), arg1);
		preparedStatement.setTime(arg0, arg1);
	}
	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @throws java.sql.SQLException
	 */
	public void setTime(int arg0, Time arg1, Calendar arg2) throws SQLException {
		parameters.put (new Integer (arg0), arg1);
		preparedStatement.setTime(arg0, arg1, arg2);
	}
	/**
	 * @param arg0
	 * @param arg1
	 * @throws java.sql.SQLException
	 */
	public void setTimestamp(int arg0, Timestamp arg1) throws SQLException {
		parameters.put (new Integer (arg0), arg1);
		preparedStatement.setTimestamp(arg0, arg1);
	}
	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @throws java.sql.SQLException
	 */
	public void setTimestamp(int arg0, Timestamp arg1, Calendar arg2)
			throws SQLException {
		parameters.put (new Integer (arg0), arg1);
		preparedStatement.setTimestamp(arg0, arg1, arg2);
	}
	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @throws java.sql.SQLException
	 */
	public void setUnicodeStream(int arg0, InputStream arg1, int arg2)
			throws SQLException {
		preparedStatement.setUnicodeStream(arg0, arg1, arg2);
	}
	/**
	 * @param arg0
	 * @param arg1
	 * @throws java.sql.SQLException
	 */
	public void setURL(int arg0, URL arg1) throws SQLException {
		parameters.put (new Integer (arg0), arg1);
		preparedStatement.setURL(arg0, arg1);
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		StringBuffer buffer = new StringBuffer ();
		StringTokenizer tokenizer = new StringTokenizer( sqlStatement, "?" );
		String partOfStatement;
        List parameterValues = new ArrayList(parameters.keySet());
        Collections.sort(parameterValues); 
		Iterator iter = parameterValues.iterator();
		Object key = null;
		while ( tokenizer.hasMoreTokens() ) {
			partOfStatement = tokenizer.nextToken();
			if (iter.hasNext()) {
				key = iter.next(); 
				buffer.append (partOfStatement);
				buffer.append  ("'" + parameters.get (key).toString() + "'");
			} else {
				buffer.append (partOfStatement);
				buffer.append  ("?");
			}
		}
		return buffer.toString();
	}

}
