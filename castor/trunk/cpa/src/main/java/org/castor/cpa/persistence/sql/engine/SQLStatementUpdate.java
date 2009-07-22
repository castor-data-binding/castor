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
package org.castor.cpa.persistence.sql.engine;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.castor.cpa.persistence.sql.query.Update;
import org.castor.cpa.persistence.sql.query.QueryContext;
import org.castor.cpa.persistence.sql.query.condition.AndCondition;
import org.castor.cpa.persistence.sql.query.QueryConstants;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.castor.core.util.Messages;
import org.castor.persist.ProposedEntity;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.engine.SQLColumnInfo;
import org.exolab.castor.jdo.engine.SQLEngine;
import org.exolab.castor.jdo.engine.SQLFieldInfo;
import org.exolab.castor.jdo.engine.nature.ClassDescriptorJDONature;
import org.exolab.castor.persist.spi.Identity;
import org.exolab.castor.persist.spi.PersistenceFactory;

/**
 * SQLStatementStore class that makes use of Update class hierarchy to generate sql
 * query structure. It provides parameter binding support to the prepared statement
 * and then executes it.
 */
public final class SQLStatementUpdate {
    //-----------------------------------------------------------------------------------    

    /** The <a href="http://jakarta.apache.org/commons/logging/">Jakarta
     *  Commons Logging</a> instance used for all logging. */
    private static final Log LOG = LogFactory.getLog(SQLStatementUpdate.class);
    
    /** Name space to prepend to set parameter names to distinguish them from parameters
     *  of where clause. */
    private static final String SET_PARAM_NAMESPACE = "SET:";
    
    /** ThreadLocal attribute to store prepared statement for a
     *  particular connection that is unique to one thread. */
    private static final ThreadLocal<PreparedStatement> PREPARED_STATEMENT = 
        new ThreadLocal<PreparedStatement>();
    
    //-----------------------------------------------------------------------------------    

    /** Name of engine descriptor. */
    private final String _type;
    
    /** Column information for identities specific to the particular engine instance. */
    private final SQLColumnInfo[] _ids;
    
    /** Contains the Information specific to particular engine instance. */
    private final SQLFieldInfo[] _fields;
    
    /** Persistence factory for the database engine the entity is persisted in.
     *  Used to format the SQL statement. */
    private final PersistenceFactory _factory;

    /** Update SQL statement class hierarchy. */
    private Update _update;
    
    /** Indicates whether there is a field to persist at all; in the case of 
     *  EXTEND relationships where no additional attributes are defined in the 
     *  extending class, this might NOT be the case; in general, a class has to have
     *  at least one field that is to be persisted. */
    private boolean _hasFieldsToPersist;

    /** SQLStatementUpdateCheck instance to check the failure reason of the update statement.*/
    private final SQLStatementUpdateCheck _statementUpdateCheck;
    
    //-----------------------------------------------------------------------------------    

    /**
    * Constructor.
    * 
    * @param engine SQL engine for all persistence operations at entities of the type this
     *        class is responsible for. Holds all required information of the entity type.
    * @param factory Persistence factory for the database engine the entity is persisted in.
     *        Used to format the SQL statement.
    * @param load
    */
    public SQLStatementUpdate(final SQLEngine engine, final PersistenceFactory factory,
                             final String load) {
        _type = engine.getDescriptor().getJavaClass().getName();
        _ids = engine.getColumnInfoForIdentities();
        _fields = engine.getInfo();

        _factory = factory;

        buildStatement(new ClassDescriptorJDONature(engine.getDescriptor()).getTableName());        

        _statementUpdateCheck = new SQLStatementUpdateCheck(engine, load);
}
    
    /**
     * Build SQL statement to store entities of the type this class.
     *
     * @param mapTo Table name retrieved from Class Descriptor trough JDO Nature.
     */
    private void buildStatement(final String mapTo) {        
        _update = new Update(mapTo);

        // add assignments to update statement
        int count = 0;
        for (int i = 0; i < _fields.length; ++i) {
            if (_fields[i].isStore()) {
                SQLColumnInfo[] columns = _fields[i].getColumnInfo();
                for (int j = 0; j < columns.length; j++) {
                    _update.addAssignment(columns[j].getName(),
                            SET_PARAM_NAMESPACE + columns[j].getName());
                    ++count;
                }
            }
        }        
        
        // add conditions for identities
        for (int i = 0; i < _ids.length; i++) {
            _update.addCondition(_ids[i].getName());
        } 
        
        _hasFieldsToPersist = (count > 0);
        
        if (LOG.isTraceEnabled()) {
            LOG.trace("hasFieldsToPersist = " + _hasFieldsToPersist);
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
        // only execute an update statement if there are fields to persist
        if (_hasFieldsToPersist) {
            //Inializing new QueryContext object
            QueryContext ctx = new QueryContext(_factory);
            
            synchronized (this) {
                // remember a copy of original conditions of update statement
                AndCondition copy = new AndCondition(_update.getCondition());

                try {
                    //Append fields other than identities
                    appendOldEntityCondition(oldentity);
                    
                    /* Walking through the Update class hierarchy, build sql string
                     * and stores it in QueryContext instance. */
                    _update.toString(ctx);
                } finally {
                    // restore original condition 
                    _update.setCondition(copy);
                }
            }

            try {
                //get PreparedStatment for the connection
                prepareStatement(conn, ctx);

                //Binds new entities
                bindNewEntity(newentity, ctx);
                
                //binds identity
                bindIdentity(identity, ctx);
                
                //binds old entities
                bindOldEntity(oldentity, ctx);

                //executes prepared statement
                if (executeUpdate() <= 0) { // SAP DB returns -1 here
                    /*Check whether the object had been modified or deleted and 
                    raise appropriate exception*/
                    _statementUpdateCheck.updateFailureCheck(conn, identity, oldentity);
                }                
            } catch (SQLException ex) {
                LOG.fatal(Messages.format("jdo.storeFatal", _type,  ctx.toString()), ex);
                throw new PersistenceException(Messages.format("persist.nested", ex), ex);
            } finally {
                // close statement
                closeStatement();
            }
        }
        return null;
    }
    
    /**
     * Method that appends the fields other than identities in where clause of SQL statement.
     * 
     * @param oldentity
     * @throws PersistenceException If identity size mismatches or column length mismatches
     */
    private void appendOldEntityCondition(final ProposedEntity oldentity) 
    throws PersistenceException {
        if (oldentity.getFields() != null) {
            for (int i = 0; i < _fields.length; ++i) {
                if (_fields[i].isStore() && _fields[i].isDirtyCheck()) {
                    SQLColumnInfo[] columns = _fields[i].getColumnInfo();
                    Object value = oldentity.getField(i);
                    if (value == null) { 
                        //Append 'is NULL' incase the value is null    
                        for (int j = 0; j < columns.length; j++) {
                           _update.addNullCondition(columns[j].getName());
                        }
                    } else if (value instanceof Identity) {
                        //Raise exception if identity size doesn't match column length
                        Identity identity = (Identity) value;
                        if (identity.size() != columns.length) {
                            throw new PersistenceException("Size of identity field mismatch!");
                        }

                        //Traverse through all the columns and append it to SQL based on whether
                        //the value of that column is null or not.
                        for (int j = 0; j < columns.length; j++) {
                            if (identity.get(j) == null) {
                                _update.addNullCondition(columns[j].getName());

                            } else {
                                _update.addCondition(columns[j].getName());
                            }
                        }
                    } else {
                        for (int j = 0; j < columns.length; j++) {
                           _update.addCondition(columns[j].getName());
                        }
                    }
                }
            }
        }
    }
    
    /**
     * Prepares the SQL Statement.
     * 
     * @param conn An Open JDBC Connection
     * @throws SQLException If a database access error occurs.
     */
    private void prepareStatement(final Connection conn, final QueryContext ctx) 
    throws SQLException { 
        PreparedStatement preparedStatement = conn.prepareStatement(ctx.toString());

        // set prepared statement in thread local variable
        PREPARED_STATEMENT.set(preparedStatement);
         
         if (LOG.isTraceEnabled()) {
             LOG.trace(Messages.format("jdo.storing", _type, preparedStatement.toString()));
         }
    }
    
    /**
     * Binds new entities.
     * 
     * @param newentity
     * @throws PersistenceException If identity size mismatches
     *  or column length mismatches
     * @throws SQLException If database access error occurs
     */
    private void bindNewEntity(final ProposedEntity newentity, final QueryContext ctx)
    throws PersistenceException, SQLException {
        // get prepared statement from thread local variable
        PreparedStatement preparedStatement = PREPARED_STATEMENT.get();        
        
        // bind fields of the row to be stored into the preparedStatement
        for (int i = 0; i < _fields.length; ++i) {
            if (_fields[i].isStore()) {
                SQLColumnInfo[] columns = _fields[i].getColumnInfo();
                Object value = newentity.getField(i);
                if (value == null) {
                    for (int j = 0; j < columns.length; j++) {
                         ctx.bindParameter(preparedStatement, 
                                 SET_PARAM_NAMESPACE + columns[j].getName(), 
                                 null, columns[j].getSqlType());
                    }
                } else if (value instanceof Identity) {
                    Identity id = (Identity) value;
                    if (id.size() != columns.length) {
                        throw new PersistenceException("Size of identity field mismatch!");
                    }
                    
                    for (int j = 0; j < columns.length; j++) {
                       ctx.bindParameter(preparedStatement, 
                               SET_PARAM_NAMESPACE + columns[j].getName(), 
                               columns[j].toSQL(id.get(j)), columns[j].getSqlType());
                        
                    }
                } else {
                    if (columns.length != 1) {
                        throw new PersistenceException("Complex field expected!");
                    }
                    
                     ctx.bindParameter(preparedStatement, 
                             SET_PARAM_NAMESPACE + columns[0].getName(),
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
    private void  bindIdentity(final Identity identity, final QueryContext ctx) 
    throws PersistenceException, SQLException {
        // get prepared statement from thread local variable
        PreparedStatement preparedStatement = PREPARED_STATEMENT.get();        
                
        // bind the identity of the row to be stored into the preparedStatement
        for (int i = 0; i < _ids.length; i++) {
            ctx.bindParameter(preparedStatement, _ids[i].getName(), 
                    _ids[i].toSQL(identity.get(i)), _ids[i].getSqlType());

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
    private void bindOldEntity(final ProposedEntity oldentity, final QueryContext ctx) 
    throws PersistenceException, SQLException {   
        // get prepared statement from thread local variable
        PreparedStatement preparedStatement = PREPARED_STATEMENT.get();        
        
        // bind the old fields of the row to be stored into the preparedStatement
        if (oldentity.getFields() != null) {            
            for (int i = 0; i < _fields.length; ++i) {
                if (_fields[i].isStore() && _fields[i].isDirtyCheck()) {
                    SQLColumnInfo[] columns = _fields[i].getColumnInfo();
                    Object value = oldentity.getField(i);
                    if (value == null) {
                        continue;
                    } else if (value instanceof Identity) {
                        Identity id = (Identity) value;
                        if (id.size() != columns.length) {
                            throw new PersistenceException("Size of identity field mismatch!");
                        }
                        
                        for (int j = 0; j < columns.length; j++) {
                               ctx.bindParameter(preparedStatement, columns[j].getName(), 
                                       columns[j].toSQL(id.get(j)), columns[j].getSqlType());

                        }
                    } else {
                        if (columns.length != 1) {
                            throw new PersistenceException("Complex field expected!");
                        }
                        
                           ctx.bindParameter(preparedStatement, columns[0].getName(), 
                                   columns[0].toSQL(value), columns[0].getSqlType());
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
        // get prepared statement from thread local variable
        PreparedStatement preparedStatement = PREPARED_STATEMENT.get();       
        return preparedStatement.executeUpdate();
    } 
        
    /**
     * Close the prepared statement.
     */
    private void closeStatement() {
        // get prepared statement from thread local variable
        PreparedStatement preparedStatement = PREPARED_STATEMENT.get();

        try {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
        } catch (Exception ex) {
            LOG.warn("Problem closing JDBC statement", ex);
        }
    }

    //-----------------------------------------------------------------------------------    
}