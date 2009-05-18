/*
 * Copyright 2006 Assaf Arkin, Thomas Yip, Bruce Snyder, Werner Guttmann, Ralf Joachim
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
import java.sql.SQLException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.castor.core.util.Messages;
import org.castor.jdo.engine.SQLTypeInfos;
import org.castor.persist.ProposedEntity;
import org.exolab.castor.jdo.ObjectDeletedException;
import org.exolab.castor.jdo.ObjectModifiedException;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.engine.nature.ClassDescriptorJDONature;
import org.exolab.castor.mapping.ClassDescriptor;
import org.exolab.castor.persist.spi.Identity;
import org.exolab.castor.persist.spi.PersistenceFactory;
import org.exolab.castor.persist.spi.QueryExpression;

public final class SQLStatementStore {
    /** The <a href="http://jakarta.apache.org/commons/logging/">Jakarta
     *  Commons Logging</a> instance used for all logging. */
    private static final Log LOG = LogFactory.getLog(SQLStatementStore.class);

    private final SQLEngine _engine;
    
    private final PersistenceFactory _factory;
    
    private final String _type;

    private final String _mapTo;
    
    private final SQLFieldInfo[] _fields;
    
    private final SQLColumnInfo[] _ids;

    /** Indicates whether there is a field to persist at all; in the case of 
     *  EXTEND relationships where no additional attributes are defined in the 
     *  extending class, this might NOT be the case; in general, a class has to have
     *  at least one field that is to be persisted. */
    private boolean _hasFieldsToPersist = false;

    private String _statementLazy;

    private String _statementDirty;
    
    private String _statementLoad;
    
    private String _storeStatement;

    private PreparedStatement _preparedStatement;
    
    private ResultSet _resultSet;
    
    private int _count;

    public SQLStatementStore(final SQLEngine engine, final PersistenceFactory factory,
                             final String load) {
        _engine = engine;
        _factory = factory;
        _type = engine.getDescriptor().getJavaClass().getName();
        _mapTo = new ClassDescriptorJDONature(engine.getDescriptor()).getTableName();
        _fields = _engine.getInfo();
        _ids = _engine.getColumnInfoForIdentities();

        // iterate through all fields to check whether there is a field
        // to persist at all; in the case of extend relationships where no 
        // additional attributes are defined in the extending class, this 
        // might NOT be the case        
        for (int i = 0; i < _fields.length; ++i) {
            if (_fields[i].isStore()) {
                _hasFieldsToPersist = true;
                break;
            }
        }

        if (LOG.isTraceEnabled()) {
            LOG.trace("hasFieldsToPersist = " + _hasFieldsToPersist);
        }

        buildStatement();
        
        _statementLoad = load;
    }
    
    private void buildStatement() {
        StringBuffer sql = new StringBuffer("UPDATE ");
        sql.append(_factory.quoteName(_mapTo));
        
        // append the SET clause only if there are any fields that need to be persisted.
        if (_hasFieldsToPersist) {
            sql.append(" SET ");
            
            int count = 0;
            for (int i = 0; i < _fields.length; ++i) {
                if (_fields[i].isStore()) {
                    SQLColumnInfo[] columns = _fields[i].getColumnInfo();
                    for (int j = 0; j < columns.length; j++) {
                        if (count > 0) { sql.append(','); }
                        sql.append(_factory.quoteName(columns[j].getName()));
                        sql.append("=?");
                        ++count;
                    }
                }
            }
            
            sql.append(JDBCSyntax.WHERE);

            for (int i = 0; i < _ids.length; i++) {
                if (i > 0) { sql.append(" AND "); }
                sql.append(_factory.quoteName(_ids[i].getName()));
                sql.append(QueryExpression.OP_EQUALS);
                sql.append(JDBCSyntax.PARAMETER);
            }

            _statementLazy = sql.toString();
            
            if (LOG.isTraceEnabled()) {
                LOG.trace(Messages.format("jdo.updating", _type, _statementLazy));
            }

            for (int i = 0; i < _fields.length; ++i) {
                if (_fields[i].isStore() && _fields[i].isDirtyCheck()) {
                    SQLColumnInfo[] columns = _fields[i].getColumnInfo();
                    for (int j = 0; j < columns.length; j++) {
                        sql.append(" AND ");
                        sql.append(_factory.quoteName(columns[j].getName()));
                        sql.append("=?");
                    }
                }
            }
            
            _statementDirty = sql.toString();
            
            if (LOG.isTraceEnabled()) {
                LOG.trace(Messages.format("jdo.updating", _type, _statementDirty));
            }
        } 
    }
    
    public synchronized Object executeStatement(final Connection conn, final Identity identity,
                                   final ProposedEntity newentity,
                                   final ProposedEntity oldentity)
    throws PersistenceException {
        // Must store record in parent table first.
        // All other dependents are stored independently.
        SQLEngine extended = _engine.getExtends();
        if (extended != null) {
            // | quick and very dirty hack to try to make multiple class on the same table work
            ClassDescriptor extDesc = extended.getDescriptor();
            if (!new ClassDescriptorJDONature(extDesc).getTableName().equals(_mapTo)) {
                extended.store(conn, identity, newentity, oldentity);
            }
        }

        // Only build and execute an UPDATE statement if the class to be updated has 
        // fields to persist.
        if (_hasFieldsToPersist) {
            try {
                _storeStatement = getStoreStatement(oldentity);

                //get PreparedStatment for the connection
                prepareStatement(conn, _storeStatement);
            
                //For binding fields and id's with StoreStatement
                _count = 1;
                bindData(identity, newentity, oldentity);
                
                //executes prepared statement
                int result;
                result = executeUpdate();
                if (result <= 0) { // SAP DB returns -1 here
                    // If no update was performed, the object has been previously
                    // removed from persistent storage or has been modified if
                    // dirty checking. Determine which is which.
                    closeStatement();
                    if (oldentity.getFields() != null) {
                        prepareStatement(conn, _statementLoad);                   
                     
                        //Binds identity
                        _count = 1;
                        bindIdentity(identity);
                    
                        //Load Data into resultset
                        executeQuery();
                        
                        //Process Resultset data
                        processData (identity, oldentity);                
                    }
                    throw new ObjectDeletedException(Messages.format(
                            "persist.objectDeleted", _type, identity));
                }                
            } catch (SQLException except) {
                LOG.fatal(Messages.format("jdo.storeFatal", _type,  _storeStatement), except);
                throw new PersistenceException(Messages.format("persist.nested", except), except);
            } finally {
                //close statement
                closeStatement();
            }
        }
        return null;
    }

    /**
     * If the RDBMS doesn't support setNull for "WHERE fld=?" and requires
     * "WHERE fld IS NULL", we need to modify the statement.
     */
    private String getStoreStatement(final ProposedEntity oldentity)
    throws PersistenceException {
        if (oldentity.getFields() == null) {
            return _statementLazy;
        } else if (_factory.supportsSetNullInWhere()) {
            return _statementDirty;
        } else {
            int pos = _statementDirty.length() - 1;
            
            StringBuffer sql = new StringBuffer(pos * 4);
            sql.append(_statementDirty);
            
            for (int i = _fields.length - 1; i >= 0; i--) {
                if (_fields[i].isStore() && _fields[i].isDirtyCheck()) {
                    SQLColumnInfo[] columns = _fields[i].getColumnInfo();
                    Object value = oldentity.getField(i);
                    if (value == null) {
                        for (int j = columns.length - 1; j >= 0; j--) {
                            pos = nextParameter(true, sql, pos);
                        }
                    } else if (value instanceof Identity) {
                        Identity identity = (Identity) value;
                        if (identity.size() != columns.length) {
                            throw new PersistenceException("Size of identity field mismatch!");
                        }

                        for (int j = columns.length - 1; j >= 0; j--) {
                            pos = nextParameter((identity.get(j) == null), sql, pos);
                        }
                    } else {
                        if (columns.length != 1) {
                            throw new PersistenceException("Complex field expected!");
                        }

                        pos = nextParameter(false, sql, pos);
                    }
                }
            }
            return sql.toString();
        }
    }
    
    /**
     * if isNull, replace next "=?" with " IS NULL", otherwise skip next "=?",
     * move "pos" to the left.
     * 
     * @param isNull True if =? should be replaced with 'IS NULL'
     * @param sb StringBUffer holding the SQL statement to be modified 
     * @param pos The current position (where to apply the replacement).
     * @return The next position.
     */
    private int nextParameter(final boolean isNull, final StringBuffer sb, final int pos) {
        int internalpos = pos;
        for ( ; internalpos > 0; internalpos--) {
            if ((sb.charAt(internalpos - 1) == '=') && (sb.charAt(internalpos) == '?')) {
                break;
            }
        }
        if (internalpos > 0) {
            internalpos--;
            if (isNull) {
                sb.delete(internalpos, internalpos + 2);
                sb.insert(internalpos, " IS NULL");
            }
        }
        return internalpos;
    }
    
    /**
     * Prepares the SQL Statement.
     * 
     * @param conn
     * @throws PersistenceException
     * @throws SQLException 17 May 2009
     */
    private void prepareStatement (final Connection conn, String statement) throws PersistenceException, SQLException {    	     	
        _preparedStatement = conn.prepareStatement(statement);
         
         if (LOG.isTraceEnabled()) {
             LOG.trace(Messages.format("jdo.storing", _type, _preparedStatement.toString()));
         }
    }
    
    /**
     * Binds data.
     * 
     * @param identity
     * @param newentity
     * @param oldentity
     * @throws PersistenceException
     * @throws SQLException 18 May 2009
     */
    private void bindData (final Identity identity, final ProposedEntity newentity,
            final ProposedEntity oldentity) throws PersistenceException, SQLException {
        //Binds new entities
        bindNewEntity (newentity);
        
        //binds identity
        bindIdentity (identity);
        
        //binds old entities
        bindOldEntity (oldentity);
    }
    
    /**
     * Binds new entities.
     * 
     * @param newentity
     * @throws PersistenceException
     * @throws SQLException 18 May 2009
     */
    private void bindNewEntity(final ProposedEntity newentity)
    throws PersistenceException, SQLException {
        // bind fields of the row to be stored into the preparedStatement
        for (int i = 0; i < _fields.length; ++i) {
            if (_fields[i].isStore()) {
                SQLColumnInfo[] columns = _fields[i].getColumnInfo();
                Object value = newentity.getField(i);
                if (value == null) {
                    for (int j = 0; j < columns.length; j++) {
                        _preparedStatement.setNull(_count++, columns[j].getSqlType());
                    }
                } else if (value instanceof Identity) {
                    Identity id = (Identity) value;
                    if (id.size() != columns.length) {
                        throw new PersistenceException("Size of identity field mismatch!");
                    }
                    
                    for (int j = 0; j < columns.length; j++) {
                        SQLTypeInfos.setValue(_preparedStatement, _count++,
                                columns[j].toSQL(id.get(j)), columns[j].getSqlType());
                    }
                } else {
                    if (columns.length != 1) {
                        throw new PersistenceException("Complex field expected!");
                    }
                    
                    SQLTypeInfos.setValue(_preparedStatement, _count++, columns[0].toSQL(value),
                            columns[0].getSqlType());
                }
            }
        }
    }
    
    /**
     * Binds Identity.
     * 
     * @param identity
     * @throws PersistenceException
     * @throws SQLException 18 May 2009
     */
    private void  bindIdentity (final Identity identity) throws PersistenceException, SQLException {
        // bind the identity of the row to be stored into the preparedStatement
        if (identity.size() != _ids.length) {
            throw new PersistenceException("Size of identity field mismatched!");
        }
        
        for (int i = 0; i < _ids.length; i++) {
            _preparedStatement.setObject(_count++, _ids[i].toSQL(identity.get(i)));

            if (LOG.isTraceEnabled()) {
                LOG.trace(Messages.format("jdo.bindingIdentity", _ids[i].getName(),
                        _ids[i].toSQL(identity.get(i))));
            }
        }   
    }
    
    /**
     * Binds old Entities.
     * 
     * @param oldentity
     * @throws PersistenceException
     * @throws SQLException 18 May 2009
     */
    private void bindOldEntity(final ProposedEntity oldentity) throws PersistenceException, SQLException {    	
        // bind the old fields of the row to be stored into the preparedStatement
        if (oldentity.getFields() != null) {
            boolean supportsSetNull = _factory.supportsSetNullInWhere();
            
            for (int i = 0; i < _fields.length; ++i) {
                if (_fields[i].isStore() && _fields[i].isDirtyCheck()) {
                    SQLColumnInfo[] columns = _fields[i].getColumnInfo();
                    Object value = oldentity.getField(i);
                    if (value == null) {
                        if (supportsSetNull) {
                            for (int j = 0; j < columns.length; j++) {
                                _preparedStatement.setNull(_count++, columns[j].getSqlType());
                            }
                        }
                    } else if (value instanceof Identity) {
                        Identity id = (Identity) value;
                        if (id.size() != columns.length) {
                            throw new PersistenceException(
                                    "Size of identity field mismatch!");
                        }
                        
                        for (int j = 0; j < columns.length; j++) {
                            SQLTypeInfos.setValue(_preparedStatement, _count++,
                                    columns[j].toSQL(id.get(j)), columns[j].getSqlType());
                            
                            if (LOG.isTraceEnabled()) {
                                LOG.trace(Messages.format("jdo.bindingField",
                                        columns[j].getName(), columns[j].toSQL(id.get(j))));
                            }
                        }
                    } else {
                        if (columns.length != 1) {
                            throw new PersistenceException("Complex field expected!");
                        }
                        
                        SQLTypeInfos.setValue(_preparedStatement, _count++, columns[0].toSQL(value),
                                columns[0].getSqlType());
                    
                        if (LOG.isTraceEnabled()) {
                            LOG.trace(Messages.format("jdo.bindingField",
                                    columns[0].getName(), columns[0].toSQL(value)));
                        }
                    }
                }
            }
        }
        
        if (LOG.isDebugEnabled()) {
            LOG.debug(Messages.format("jdo.storing", _type, _preparedStatement.toString()));
        }
    }
    
    /**
     * executeUpdate.
     * 
     * @return
     * @throws SQLException 18 May 2009
     */
    private int executeUpdate () throws SQLException {
        int result;
        result = _preparedStatement.executeUpdate();
        
        return result;
    } 
    
    /**
     * executeQuery.
     * 
     * @throws SQLException
     */
    private void executeQuery () throws SQLException {
        _resultSet = _preparedStatement.executeQuery();     
    }
    
    /**
     * process the ResultSet.
     * 
     * @param identity
     * @param oldentity
     * @throws SQLException
     * @throws ObjectModifiedException 18 May 2009
     */
    private void processData (final Identity identity, final ProposedEntity oldentity) throws SQLException, ObjectModifiedException {
        if (_resultSet.next()) {                     
             StringBuffer enlistFieldsNotMatching = new StringBuffer();
             
             int numberOfFieldsNotMatching = 0;
             for (int i = 0; i < _fields.length; i++) {
                 SQLColumnInfo[] columns = _fields[i].getColumnInfo();
                 Object value = oldentity.getField(i);
                 Object currentField = columns[0].toJava(_resultSet.getObject(
                         columns[0].getName()));
                 if (_fields[i].getTableName().compareTo(_mapTo) == 0) {
                     if ((value == null) || ((value != null)
                             && (currentField == null))) {
                         enlistFieldsNotMatching.append("(" + _type + ")."
                                 + columns[0].getName() + ": ");
                         enlistFieldsNotMatching.append("[" + value + "/"
                                 + currentField + "]"); 
                     } else if (!value.equals(currentField)) {
                         if (numberOfFieldsNotMatching >= 1) {
                             enlistFieldsNotMatching.append(", ");
                         }
                         enlistFieldsNotMatching.append("(" + _type + ")."
                                 + columns[0].getName() + ": ");
                         enlistFieldsNotMatching.append("[" + value + "/"
                                 + currentField + "]"); 
                         numberOfFieldsNotMatching++;
                     }
                 }
             }

             throw new ObjectModifiedException(Messages.format(
                     "persist.objectModified", _type, identity,
                     enlistFieldsNotMatching.toString()));
         }
    }
    
    /**
     * closes the opened statement.
     */
    private void closeStatement() {
        try {
            // Close the insert/select statement
            if (_preparedStatement != null) { _preparedStatement.close(); }
        } catch (SQLException except2) {
            LOG.warn("Problem closing JDBC statement", except2);
        }
    }
}
