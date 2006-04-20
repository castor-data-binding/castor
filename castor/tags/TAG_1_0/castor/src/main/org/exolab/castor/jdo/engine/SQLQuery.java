/*
 * Copyright 2005 Assaf Arkin, Thomas Yip, Bruce Snyder, Werner Guttmann, Ralf Joachim
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
 *
 * $Id$
 */
package org.exolab.castor.jdo.engine;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.castor.jdo.engine.CounterRef;
import org.castor.jdo.engine.SQLTypeInfos;
import org.castor.persist.ProposedObject;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.mapping.AccessMode;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.persist.spi.Complex;
import org.exolab.castor.persist.spi.Persistence;
import org.exolab.castor.persist.spi.PersistenceFactory;
import org.exolab.castor.persist.spi.PersistenceQuery;
import org.exolab.castor.util.Messages;
import org.exolab.castor.util.SqlBindParser;

/**
 * @author <a href="mailto:arkin AT intalio DOT com">Assaf Arkin</a>
 * @author <a href="mailto:yip AT intalio DOT com">Thomas Yip</a>
 * @author <a href="mailto:ferret AT frii DOT com">Bruce Snyder</a>
 * @author <a href="mailto:werner DOT guttmann AT gmx DOT net">Werner Guttmann</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon-world DOT de">Ralf Joachim</a>
 * @version $Revision$ $Date$
 * @since 1.0
 */
public final class SQLQuery implements PersistenceQuery {
    /** The <a href="http://jakarta.apache.org/commons/logging/">Jakarta
     *  Commons Logging</a> instance used for all logging. */
    private static final Log LOG = LogFactory.getLog(SQLQuery.class);

    private PreparedStatement _stmt;

    private ResultSet _rs;

    private SQLEngine _engine;
    
    private final SQLEngine _requestedEngine;
    
    private final PersistenceFactory _requestedFactory;

    private final Class[] _types;

    private final Object[]_values;

    private final String _sql;

    private Object[] _lastIdentity;

    private int[] _identSqlType;

    private boolean _resultSetDone;

    private Object[] _fields;
    
    /** Indicates whether the SQL query executed is issued as part of a SQL 
     *  CALL statement or not. */
    private boolean _isCallSql = false;

    /**
     * Creates an instance of SQLQuery.
     * 
     * @param engine SQLEngine instance
     * @param sql The SQL statement to execute
     * @param types Types of the class used.
     * @param isCallSql true if the SQL is issued as part of a CALL SQL statement.
     */
    SQLQuery(final SQLEngine engine, final PersistenceFactory factory, final String sql,
             final Class[] types, final boolean isCallSql) {
        
        _engine = engine;
        _requestedEngine = engine;
        _requestedFactory = factory;
        _types = types;
        _values = new Object[ _types.length ];
        _sql = sql;
        _identSqlType = new int[_engine.getDescriptor().getIdentities().length];
        for (int i = 0; i < _identSqlType.length; i++) {
            _identSqlType[i] = ((JDOFieldDescriptor) _engine.getDescriptor().getIdentities()[i]).getSQLType()[0];
        }
        
        _isCallSql = isCallSql;
    }

    public void setParameter(final int index, final Object value)
    throws ArrayIndexOutOfBoundsException, IllegalArgumentException {
        _values[index] = value;
    }

    public Class getResultType() {
        return _engine.getDescriptor().getJavaClass();
    }

    /**
     * Move to an absolute position within a ResultSet. 
     * use the jdbc 2.0 method to move to an absolute position in the
     * resultset.
     * 
     * @param row The row to move to
     * @return True if the move was successful.
     * @throws PersistenceException Indicates a problem in moving to an absolute position.
     */
    public boolean absolute(final int row) throws PersistenceException {
        boolean retval = false;
        try {
           if (_rs != null) {
              retval = _rs.absolute(row);
           }
        } catch (SQLException e) {
           throw new PersistenceException(e.getMessage(), e);
        }
        return retval;
    }

    /**
     * Uses the underlying db's cursors to move to the last row in the
     * result set, get the row number via getRow(), then move back to
     * where ever the user was positioned in the resultset.
     * 
     * @return The size of the current result set. 
     * @throws PersistenceException If the excution of this method failed. 
     */
    public int size() throws PersistenceException {
        int whereIAm = 1; // first
        int retval = 0; // default size is 0;
        try {
           if (_rs != null) {
              whereIAm = _rs.getRow();
              if (_rs.last()) {
                 retval = _rs.getRow();
              } else {
                 retval = 0;
              }
              // go back from whence I came.
              if (whereIAm > 0) {
                 _rs.absolute(whereIAm);
              } else {
                 _rs.beforeFirst();
              }
           }
        } catch (SQLException se) {
           throw new PersistenceException(se.getMessage());
        }
        return retval;
    }

    public void execute(final Object conn, final AccessMode accessMode,
                        final boolean scrollable)
    throws PersistenceException {
        // create SQL statement from _sql, replacing bind expressions like "?1" by "?"
        String sql = SqlBindParser.getJdbcSql(_sql);

        _lastIdentity = null;

        try {
            if (scrollable) {
                _stmt = ((Connection) conn).prepareStatement(sql, java.sql.ResultSet.TYPE_SCROLL_INSENSITIVE, java.sql.ResultSet.CONCUR_READ_ONLY);
            } else {
                _stmt = ((Connection) conn).prepareStatement(sql);
            }

             // bind variable values on _values to the JDBC statement _stmt using the bind variable order in _sql 
            SqlBindParser.bindJdbcValues(_stmt, _sql, _values);

             // erase bind values
            for (int i = 0; i < _values.length; ++i) {
                _values[i] = null;
            }

            if (LOG.isDebugEnabled()) {
                LOG.debug (Messages.format ("jdo.executingSql", sql));
            }

            _rs = _stmt.executeQuery();
            _resultSetDone = false;
        } catch (SQLException except) {
            if (_stmt != null) {
                try {
                    _stmt.close();
                } catch ( SQLException e2 ) {
                    LOG.warn("Problem closing JDBC statement", e2);
                }
            }
            _resultSetDone = true;
            throw new PersistenceException(Messages.format("persist.nested", except) + " while executing "+ _sql, except);
        }
    }

    // Load a number of sql columns (from the current row of _rs) into an identity.
    private Object loadIdentity() throws SQLException, PersistenceException {
        // We can't retrieve a next identity if we have no rows of data left :-)
        if (_resultSetDone) { return null; }

        boolean  empty = false;
        Object[] returnId = new Object[_engine.getColumnInfoForIdentities().length];
        Object   tmp;

        empty = true;
        for (int i = 0; i < _engine.getColumnInfoForIdentities().length; i++) {
            tmp = SQLTypeInfos.getValue(_rs, 1+i, _identSqlType[i]);
            returnId[i] = _engine.getColumnInfoForIdentities()[i].toJava(tmp);
            if (tmp != null) {
                empty = false;
            }
        }

        if (!empty) {
            switch (_engine.getColumnInfoForIdentities().length) {
            case 1:
                return returnId[0];
            case 2:
                return new Complex(returnId[0], returnId[1]);
            default:
                return new Complex(returnId);
            }
        }
        return null;
    }

    // Get the next identity that is different from <identity>.
    public Object nextIdentity(Object identity) throws PersistenceException {
        try {
            if (_lastIdentity == null) {
                if (_resultSetDone || !_rs.next()) {
                    _resultSetDone = true;
                    return null;
                }
            }

            // Look if the current row in our ResultSet already belongs to a different id.
            _lastIdentity = identityToSQL(identity);
            identity = loadIdentity();

            if (identitiesEqual(_lastIdentity, identityToSQL(identity))) {
                // This will fetch the object data into our internal _fields[] and thus also
                // "skip" all rows till the first one with a new identity.
                fetchRaw(null);
            }

            identity = loadIdentity();

            // This will fetch the object data into our internal _fields[] and thus also
            // "skip" all rows till the first one with a new identity.
            fetchRaw(null);

        } catch (SQLException except) {
            _lastIdentity = null;
            throw new PersistenceException(Messages.format("persist.nested", except), except);
        }
        return identity;
    }

    public void close() {
        if (_rs != null) {
            try {
                _rs.close();
            } catch (SQLException except) {
                LOG.warn("Problem closing JDBC ResultSet", except);
            }
            _rs = null;
        }
        if (_stmt != null) {
            try {
                _stmt.close();
            } catch (SQLException except) {
                LOG.warn("Problem closing JDBC statement", except);
            }
            _stmt = null;
        }
    }

    private Object[] identityToSQL(final Object identity) {
        Object[] sqlIdentity = new Object[_engine.getColumnInfoForIdentities().length];

        if (identity != null) {
            // Split complex identity into array of single objects.
            if (_engine.getColumnInfoForIdentities().length > 1) {
                Complex id = (Complex) identity;
                for (int i = 0; i < _engine.getColumnInfoForIdentities().length; i++) {
                    sqlIdentity[i] = id.get(i);
                }
            } else {
                sqlIdentity[0] = identity;
            }
        }
        return sqlIdentity;
    }

    private Object loadSingleField(final int i, final CounterRef counterReference)
    throws SQLException, PersistenceException {
        String currentTableName = counterReference.getTableName();
        int count = counterReference.getCounter();
        Object[] temp = new Object[_engine.getInfo()[i].getColumnInfo().length];
        boolean notNull = false;
        Object field;
        
        String fieldTableName = _engine.getInfo()[i].getTableName();
        String fieldColumnName = _engine.getInfo()[i].getColumnInfo()[0].getName();
        String fieldName = fieldTableName + "." + fieldColumnName;
        
        ResultSetMetaData metaData = _rs.getMetaData();
        while (true) {
            String metaTableName = metaData.getTableName(count);
            String metaColumnName = metaData.getColumnName(count);
            if (fieldColumnName.equalsIgnoreCase(metaColumnName)) {
                if (!_isCallSql) {
                    if (fieldTableName.equalsIgnoreCase(metaTableName)) {
                        break;
                    } else if ("".equals(metaTableName)) {
                        break;
                    }
                 } else {
                     // if we are running as a result of a CALL SQL statement, let's relax our checks.
                     break;
                }
            } else if (fieldName.equalsIgnoreCase(metaColumnName)) {
                break;
            }

            count++;
        }
        
        if (_engine.getInfo()[i].getColumnInfo().length == 1) {
            field = _engine.getInfo()[i].getColumnInfo()[0].toJava(SQLTypeInfos.getValue(_rs, count, _engine.getInfo()[i].getColumnInfo()[0].getSqlType()));
            count++;
        } else {
            for (int j = 0; j < _engine.getInfo()[i].getColumnInfo().length; j++) {
                temp[j] = _engine.getInfo()[i].getColumnInfo()[j].toJava(SQLTypeInfos.getValue(_rs, count, _engine.getInfo()[i].getColumnInfo()[j].getSqlType()));
                count++;
                if (temp[j] != null) {
                    notNull = true;
                }
            }
            if (notNull) {
                field = new Complex( _engine.getInfo()[i].getColumnInfo().length, temp );
            } else {
                field = null;
            }
        }
        counterReference.setCounter(count);
        counterReference.setTableName(currentTableName);
        return field;
    }

    private Object loadMultiField(final int i, final CounterRef counterReference,
                                  final Object field)
    throws SQLException, PersistenceException {
        int count = counterReference.getCounter();
        Object[]  temp = new Object[_engine.getInfo()[i].getColumnInfo().length];
        boolean notNull = false;
        ArrayList res;

        String fieldTableName = _engine.getInfo()[i].getTableName();
        String firstColumnOfField = _engine.getInfo()[i].getColumnInfo()[0].getName();
        
        ResultSetMetaData metaData = _rs.getMetaData();
        String columnNamePerMetaData = metaData.getColumnName(count);
        String tableNamePerMetaData = metaData.getTableName(count);
        
        while (!(firstColumnOfField.equalsIgnoreCase(columnNamePerMetaData)
                 && (fieldTableName.equalsIgnoreCase(tableNamePerMetaData)
                         || tableNamePerMetaData.startsWith(fieldTableName)
                         || "".equals(tableNamePerMetaData)))) {
            count++;
            columnNamePerMetaData = metaData.getColumnName(count);
            tableNamePerMetaData = metaData.getTableName(count);
        }

        if (field == null) {
            res = new ArrayList();
        } else {
            res = (ArrayList) field;
        }

        for (int j = 0; j < _engine.getInfo()[i].getColumnInfo().length; j++) {
            temp[j] = _engine.getInfo()[i].getColumnInfo()[j].toJava(SQLTypeInfos.getValue(_rs, count, _engine.getInfo()[i].getColumnInfo()[j].getSqlType()));
            if (temp[j] != null) {
                notNull = true;
            }
            count++;
        }
        if (notNull) {
            if (_engine.getInfo()[i].getColumnInfo().length == 1) {
                if (!res.contains(temp[0])) {
                    res.add(temp[0]);
                }
            } else {
                Complex com = new Complex(_engine.getInfo()[i].getColumnInfo().length, temp);
                if (!res.contains(com)) {
                    res.add( com );
                }
            }
        }
        counterReference.setCounter(count);
        
        return res;
    }

    private int loadRow(final Object[] fields, final int numberOfFields,
                        final boolean isFirst)
    throws SQLException, PersistenceException {
        // skip the identity columns first; in other words, look at field columns only
        int count = _engine.getColumnInfoForIdentities().length + 1;

        String tableName = null;

        // TODO: wrong, as it could be that the first field is not part of the root class.
        if (numberOfFields > 0) {
            tableName = _engine.getInfo()[0].getTableName();
            
            // Load all the fields.
            CounterRef counterReference = new CounterRef ();
            counterReference.setCounter(count);
            counterReference.setTableName(tableName);
            
            for (int i = 0 ; i < numberOfFields ; ++i) {
                if (_engine.getInfo()[i].isMulti()) {
                    counterReference.setCounter(count);
                    fields[i] = loadMultiField( i, counterReference, fields[i] );
                    count = counterReference.getCounter(); 
                } else if (isFirst) {
                    // Non-multi fields have to be done one only once, so this is skipped
                    // if we have already read the first row.
                    counterReference.setCounter(count);
                    fields[i] = loadSingleField(i, counterReference);
                    count = counterReference.getCounter();
                }
            }
        }
        return count;
    }

    private Object[] loadSQLIdentity() throws SQLException {
        Object[] identity = new Object[_engine.getColumnInfoForIdentities().length];

        // Load the identity from the current row.
        for (int i = 0; i < _engine.getColumnInfoForIdentities().length; i++) {
            identity[i] = SQLTypeInfos.getValue(_rs, 1+i, _identSqlType[i]);
        }
        return identity;
    }

    private boolean identitiesEqual(final Object[] wantedIdentity,
                                    final Object[] currentIdentity) {
        
        // Check if the given identities differ.
        for (int i = 0; i < wantedIdentity.length; i++) {
            if ((wantedIdentity[i] == null) || (currentIdentity[i] == null)) {
                if (wantedIdentity[i] != currentIdentity[i]) {
                    return false;
                }
            } else if (!wantedIdentity[i].toString().equals(currentIdentity[i].toString())) {
                return false;
            }
        }
        return true;
    }

    /**
     * @see org.exolab.castor.persist.spi.PersistenceQuery#fetch(org.exolab.castor.persist.ProposedObject, java.lang.Object)
     */
    public Object fetch(final ProposedObject proposedObject, final Object identity)
    throws PersistenceException {
        Object[] fields = proposedObject.getFields();
        
        // Fill the given fields[] with the "cached" stuff from our _fields[] .
        for (int i = 0; i < _fields.length; i++) {
            fields[i] = _fields[i];
        }
        
        return null;
    }

    private Object fetchRaw(final Object identity) throws PersistenceException {
        // maybe we can optimize a little bit here when we have time.
        // Instead of creating new Object[] and ArrayList for each 
        // "multi field" each fetchRaw is called, we might reuse them.

        int originalFieldNumber = _requestedEngine.getInfo().length;
        if (_requestedEngine.getDescriptor().isExtended()) {
            Collection extendingClassDescriptors = _requestedEngine.getDescriptor().getExtendedBy();
            int numberOfExtendLevels = SQLHelper.numberOfExtendingClassDescriptors(_requestedEngine.getDescriptor());
            JDOClassDescriptor leafDescriptor = null;
            Object[] returnValues = null;
            try {
                returnValues = SQLHelper.calculateNumberOfFields(extendingClassDescriptors, _requestedEngine.getColumnInfoForIdentities().length, _requestedEngine.getInfo().length, numberOfExtendLevels, this._rs);
            } catch (SQLException e) {
                LOG.error ("Problem calculating number of concrete fields.", e);
                throw new PersistenceException ("Problem calculating number of concrete fields.", e);
            }
            
            leafDescriptor = (JDOClassDescriptor) returnValues[0];
            
            if (leafDescriptor != null) {
                if (!leafDescriptor.getJavaClass().getName().equals(_requestedEngine.getDescriptor().getJavaClass().getName())) {
                    originalFieldNumber = ((Integer) returnValues[1]).intValue();
                    
                    Persistence newEngine =null;
                    try {
                        newEngine = _requestedFactory.getPersistence(leafDescriptor);
                    } catch (MappingException e) {
                        LOG.error("Problem obtaining persistence engine for " + leafDescriptor.getJavaClass().getName(), e);
                        throw new PersistenceException("Problem obtaining persistence engine for " + leafDescriptor.getJavaClass().getName(), e);
                    } 
                    _engine = (SQLEngine) newEngine;
                }
            }
        }
        
        _fields = new Object[originalFieldNumber];

        // It would prove a little difficult to fetch if we don't have any rows with data left :-)
        if (_resultSetDone) { return null; }

        Object   stamp = null;

        Object[] wantedIdentity;
        Object[] currentIdentity;

        try {
            // If identity given, we want only to load data for this object.
            // Otherwise we just load the identity from the current row.
            if (identity != null) {
                wantedIdentity = identityToSQL( identity );
            } else {
                wantedIdentity = loadSQLIdentity();
            }

            // Load first (and perhaps only) row of object data from _rs into <_fields> array.
            // As we assume that we have called fetch() immediatly after nextIdentity(),
            // we can be sure that it belongs to the object we want. This is probably not the
            // safest programming style, but has to suffice currently :-)
            loadRow(_fields, originalFieldNumber, true);

            // We move forward in the ResultSet, until we see another identity or run out of rows.
            while (_rs.next()) {
                // Load identity from current row.
                currentIdentity = loadSQLIdentity();

                // Compare with wantedIdentity and determine if it is a new one.
                if (identitiesEqual(wantedIdentity, currentIdentity)) {
                    // Load next row of object data from _rs into <_fields> array.
                    loadRow(_fields, originalFieldNumber, false);
                } else {
                    // We are done with all the rows for our obj. and still have rows left.
                    _lastIdentity = currentIdentity;

                    // As stamp is never set, this function always returns null ... ???
                    // (Don't ask me, it was like that before I modified the code! :-)
                    return stamp;
                }
            }

            // We are done with all the rows for our obj. and don't have any rows left.
            _resultSetDone = true;
            _lastIdentity = null;
        } catch (SQLException except) {
            throw new PersistenceException(Messages.format("persist.nested", except), except);
        }

        return null;
    }
}
