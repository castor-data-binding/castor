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
import java.sql.SQLException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.castor.core.util.Messages;
import org.castor.jdo.engine.SQLTypeInfos;
import org.castor.persist.ProposedEntity;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.engine.nature.ClassDescriptorJDONature;
import org.exolab.castor.persist.spi.Identity;
import org.exolab.castor.persist.spi.PersistenceFactory;
import org.exolab.castor.persist.spi.QueryExpression;

public final class SQLStatementStore {
    /** The <a href="http://jakarta.apache.org/commons/logging/">Jakarta
     *  Commons Logging</a> instance used for all logging. */
    private static final Log LOG = LogFactory.getLog(SQLStatementStore.class);
    
    /** ThreadLocal attribute to store prepared statement for a
     *  particular connection that is unique to one thread. */
    private static final ThreadLocal<PreparedStatement> PREPARED_STATEMENT = 
        new ThreadLocal<PreparedStatement>();
    
    private final PersistenceFactory _factory;
    
    /** The name of engine descriptor. */
    private final String _type;

    /** Table name of the engine descriptor. */
    private final String _mapTo;
    
    /** Contains the Information specific to particular engine instancde. */
    private final SQLFieldInfo[] _fields;
    
    /** Column information for identities specific to the particular engine instance. */
    private final SQLColumnInfo[] _ids;
    
    /**SQLStatementUpdateCheck instance to check the failure reason of the udpate statement.*/
    private final SQLStatementUpdateCheck _statementUpdateCheck;

    /** Indicates whether there is a field to persist at all; in the case of 
     *  EXTEND relationships where no additional attributes are defined in the 
     *  extending class, this might NOT be the case; in general, a class has to have
     *  at least one field that is to be persisted. */
    private boolean _hasFieldsToPersist = false;

    private String _statementLazy;

    private String _statementDirty;
        
    private String _storeStatement;
    
    private int _offsetNewEntity;
    
    private int _offsetIdentity;
    
    private int _offsetOldEntity;

   /**
    * Constructor.
    * 
    * @param engine SQL engine for all persistence operations at entities of the type this
     *        class is responsible for. Holds all required information of the entity type.
    * @param factory Persistence factory for the database engine the entity is persisted in.
     *        Used to format the SQL statement.
    * @param load
    */
    public SQLStatementStore(final SQLEngine engine, final PersistenceFactory factory,
                             final String load) {
        _factory = factory;
        _type = engine.getDescriptor().getJavaClass().getName();
        _mapTo = new ClassDescriptorJDONature(engine.getDescriptor()).getTableName();
        _fields = engine.getInfo();
        _ids = engine.getColumnInfoForIdentities();
        _statementUpdateCheck = new SQLStatementUpdateCheck(engine, load);

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
    }
    
    /**
     * Build SQL statement to store entities of the type this class.
     */
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
            
            //Calculating offsets for parameter binding. Starting offset is 1
            _offsetNewEntity = 1;
            //Count is calculated above. Count the represents the number of stored values
            _offsetIdentity = 1 + count;
            //Next offset is the addition of last offset and length of ID's
            _offsetOldEntity = _offsetIdentity + _ids.length;
            
            _statementDirty = sql.toString();
            
            if (LOG.isTraceEnabled()) {
                LOG.trace(Messages.format("jdo.updating", _type, _statementDirty));
            }
        } 
    }
    
    /**
     * Stores the identity to the database using JDBC Connection.
     * 
     * @param conn An Open JDBC Connection
     * @param identity
     * @param newentity
     * @param oldentity
     * @return Always returns null
     * @throws PersistenceException If failed to store object in the database. This could happen
     *         if a database access error occurs, identity size mismatches
     *         or column length mismatches
     */
    public Object executeStatement(final Connection conn, final Identity identity,
                                   final ProposedEntity newentity,
                                   final ProposedEntity oldentity)
    throws PersistenceException {
        // Only build and execute an UPDATE statement if the class to be updated has 
        // fields to persist.
        if (_hasFieldsToPersist) {
            try {
                _storeStatement = getStoreStatement(oldentity);

                //get PreparedStatment for the connection
                prepareStatement(conn, _storeStatement);
            
                //For binding fields and id's with StoreStatement
                bindData(identity, newentity, oldentity);
                
                //executes prepared statement
                int result;
                result = executeUpdate();
                if (result <= 0) { // SAP DB returns -1 here
                    /*Check whether the object had been modified or deleted and 
                    raise appropriate exception*/
                    _statementUpdateCheck.updateFailureCheck(conn, identity, oldentity);
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
     * 
     * @param oldentity
     * @return String containing SQL
     * @throws PersistenceException If identity size mismatches
     *  or column length mismatches
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
     * @param conn An Open JDBC Connection
     * @param statement An SQL string for generating prepared statement
     * @throws SQLException If a database access error occurs.
     */
    private void prepareStatement (final Connection conn, final String statement) 
    throws SQLException { 
        PreparedStatement preparedStatement = conn.prepareStatement(statement);
        
        // set prepared statement in thread local variable
        PREPARED_STATEMENT.set(preparedStatement);
         
         if (LOG.isTraceEnabled()) {
             LOG.trace(Messages.format("jdo.storing", _type, preparedStatement.toString()));
         }
    }
    
    /**
     * Bind identity values to the prepared statement.
     * 
     * @param identity
     * @param newentity
     * @param oldentity
     * @throws PersistenceException If a database access error occurs 
     * or type of one of the values to bind is ambiguous.
     * @throws SQLException If a database access error occurs or type of one of the values to
     *         bind is ambiguous.
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
     * @throws PersistenceException If identity size mismatches
     *  or column length mismatches
     * @throws SQLException If database access error occurs
     */
    private void bindNewEntity(final ProposedEntity newentity)
    throws PersistenceException, SQLException {
        // get prepared statement from thread local variable
        PreparedStatement preparedStatement = PREPARED_STATEMENT.get();        
        int offset = _offsetNewEntity;
        
        // bind fields of the row to be stored into the preparedStatement
        for (int i = 0; i < _fields.length; ++i) {
            if (_fields[i].isStore()) {
                SQLColumnInfo[] columns = _fields[i].getColumnInfo();
                Object value = newentity.getField(i);
                if (value == null) {
                    for (int j = 0; j < columns.length; j++) {
                        preparedStatement.setNull(offset++, columns[j].getSqlType());
                    }
                } else if (value instanceof Identity) {
                    Identity id = (Identity) value;
                    if (id.size() != columns.length) {
                        throw new PersistenceException("Size of identity field mismatch!");
                    }
                    
                    for (int j = 0; j < columns.length; j++) {
                        SQLTypeInfos.setValue(preparedStatement, offset++,
                                columns[j].toSQL(id.get(j)), columns[j].getSqlType());
                    }
                } else {
                    if (columns.length != 1) {
                        throw new PersistenceException("Complex field expected!");
                    }
                    
                    SQLTypeInfos.setValue(preparedStatement, offset++,
                            columns[0].toSQL(value), columns[0].getSqlType());
                }
            }
        }        
    }
    
    /**
     * Binds Identity.
     * 
     * @param identity
     * @throws PersistenceException If identity size mismatches
     *  or column length mismatches
     * @throws SQLException If database access error occurs
     */
    private void  bindIdentity (final Identity identity) 
    throws PersistenceException, SQLException {
        // get prepared statement from thread local variable
        PreparedStatement preparedStatement = PREPARED_STATEMENT.get();        
        int offset = _offsetIdentity;
                
        // bind the identity of the row to be stored into the preparedStatement
        for (int i = 0; i < _ids.length; i++) {
            preparedStatement.setObject(offset++, _ids[i].toSQL(identity.get(i)));

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
     * @throws PersistenceException If identity size mismatches
     *  or column length mismatches
     * @throws SQLException If database access error occurs
     */
    private void bindOldEntity(final ProposedEntity oldentity) 
    throws PersistenceException, SQLException {   
        // get prepared statement from thread local variable
        PreparedStatement preparedStatement = PREPARED_STATEMENT.get();        
        int offset = _offsetOldEntity;
        
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
                                preparedStatement.setNull(offset++, columns[j].getSqlType());
                            }
                        }
                    } else if (value instanceof Identity) {
                        Identity id = (Identity) value;
                        if (id.size() != columns.length) {
                            throw new PersistenceException(
                                    "Size of identity field mismatch!");
                        }
                        
                        for (int j = 0; j < columns.length; j++) {
                            SQLTypeInfos.setValue(preparedStatement, offset++,
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
                        
                        SQLTypeInfos.setValue(preparedStatement, offset++, columns[0].toSQL(value),
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
            LOG.debug(Messages.format("jdo.storing", _type, preparedStatement.toString()));
        }        
    }
    
    /**
     * executeUpdate.
     * 
     * @return int represent the number of rows affected
     * @throws SQLException If a database access error occurs
     */
    private int executeUpdate () throws SQLException {
        int result;
        // get prepared statement from thread local variable
        PreparedStatement preparedStatement = PREPARED_STATEMENT.get();       
        result = preparedStatement.executeUpdate();
        
        return result;
    } 
        
    /**
     * closes the opened statement.
     */
    private void closeStatement() {
        try {
            // get prepared statement from thread local variable
            PreparedStatement preparedStatement = PREPARED_STATEMENT.get();
            
            // Close the insert/select statement
            if (preparedStatement != null) { preparedStatement.close(); }
        } catch (SQLException except2) {
            LOG.warn("Problem closing JDBC statement", except2);
        }
    }
}
