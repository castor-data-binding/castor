/*
 * Copyright 2010 Assaf Arkin, Thomas Yip, Bruce Snyder, Werner Guttmann,
 *                Ralf Joachim, Ahmad Hassan, Dennis Butterstein
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

import java.sql.SQLException;

import org.castor.cpa.persistence.sql.query.Update;
import org.castor.cpa.persistence.sql.query.condition.AndCondition;
import org.castor.cpa.persistence.sql.query.condition.Condition;
import org.castor.cpa.persistence.sql.query.expression.Column;
import org.castor.cpa.persistence.sql.query.expression.Parameter;
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

/**
 * SQLStatementStore class that makes use of Update class hierarchy to generate sql
 * query structure. It provides parameter binding support to the prepared statement
 * and then executes it.
 * 
 * @author <a href="mailto:arkin AT intalio DOT com">Assaf Arkin</a>
 * @author <a href="mailto:tyip AT leafsoft DOT com">Thomas Yip</a>
 * @author <a href="mailto:bruce DOT snyder AT gmail DOT com">Bruce Snyder</a>
 * @author <a href="mailto:werner DOT guttmann AT gmx DOT net">Werner Guttmann</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @author <a href="mailto:ahmad DOT hassan AT gmail DOT com">Ahmad Hassan</a>
 * @author <a href="mailto:madsheepscarer AT googlemail DOT com">Dennis Butterstein</a>
 * @version $Revision$ $Date: 2006-04-25 15:08:23 -0600 (Tue, 25 Apr 2006) $
*/
public final class SQLStatementUpdate {
    //-----------------------------------------------------------------------------------    

    /** The <a href="http://jakarta.apache.org/commons/logging/">Jakarta
     *  Commons Logging</a> instance used for all logging. */
    private static final Log LOG = LogFactory.getLog(SQLStatementUpdate.class);
    
    /** Name space to prepend to set parameter names to distinguish them from parameters
     *  of where clause. */
    private static final String SET_PARAM_NAMESPACE = "SET:";

    //-----------------------------------------------------------------------------------    

    /** Name of engine descriptor. */
    private final String _type;
    
    /** Column information for identities specific to the particular engine instance. */
    private final SQLColumnInfo[] _ids;
    
    /** Contains the Information specific to particular engine instance. */
    private final SQLFieldInfo[] _fields;

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
     */
    public SQLStatementUpdate(final SQLEngine engine) {
        _type = engine.getDescriptor().getJavaClass().getName();
        _ids = engine.getColumnInfoForIdentities();
        _fields = engine.getInfo();

        buildStatement(new ClassDescriptorJDONature(engine.getDescriptor()).getTableName());

        _statementUpdateCheck = new SQLStatementUpdateCheck(engine);
    }
    
    /**
     * Build SQL statement to update entities of the type this class.
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
                    _update.addAssignment(new Column(columns[j].getName()),
                            new Parameter(SET_PARAM_NAMESPACE + columns[j].getName()));
                    ++count;
                }
            }
        }

        _hasFieldsToPersist = (count > 0);
        
        if (LOG.isTraceEnabled()) {
            LOG.trace("hasFieldsToPersist = " + _hasFieldsToPersist);
        }
    }
    
    //-----------------------------------------------------------------------------------    

    /**
     * Stores the identity to the database using JDBC Connection.
     * 
     * @param conn An Open JDBC Connection.
     * @param identity Identity of the object to update.
     * @param newentity Entity holding the new values to set with update.
     * @param oldentity Entity holding the old values to check for concurrent modifications.
     * @return Always returns <code>null</code>. 
     * @throws PersistenceException If failed to update object in database. This could happen
     *         if a database access error occurs, type of one of the values to bind is ambiguous,
     *         identity or column size mismatch or object to be updated does not exist.
     */
    public Object executeStatement(final CastorConnection conn, final Identity identity,
            final ProposedEntity newentity, final ProposedEntity oldentity)
    throws PersistenceException {
        // only execute an update statement if there are fields to persist
        if (_hasFieldsToPersist) {

            CastorStatement stmt = conn.createStatement();

            // build condition for identities
            Condition condition = new AndCondition();
            for (int i = 0; i < _ids.length; i++) {
                String name = _ids[i].getName();
                condition.and(new Column(name).equal(new Parameter(name)));
            }

            appendOldEntityCondition(oldentity, condition);

            try {
                stmt.prepareStatement(_update, condition);

                // bind new entity
                bindNewEntity(newentity, stmt);

                // bind identity
                bindIdentity(identity, stmt);

                // bind old entity
                bindOldEntity(oldentity, stmt);

                // executes prepared statement (SAP DB returns -1 here)
                if (stmt.executeUpdate() <= 0) {
                    // check whether the object had been modified or deleted and 
                    // raise appropriate exception
                    _statementUpdateCheck.updateFailureCheck(conn, identity);
                }
            } catch (SQLException ex) {
                LOG.fatal(Messages.format("jdo.storeFatal", _type, stmt.toString()), ex);
                throw new PersistenceException(Messages.format("persist.nested", ex), ex);
            } finally {
                // close statement
                try {
                    stmt.close();
                } catch (SQLException e) {
                    LOG.warn("Problem closing JDBC statement", e);
                }
            }
        }
        return null;
    }

    /**
     * Method that appends the fields other than identities to where condition of SQL statement.
     * 
     * @param oldentity Entity holding the old values to check for concurrent modifications.
     * @param condition Condition to add old entity Conditions.
     * @throws PersistenceException If identity size mismatches or column length mismatches
     */
    private void appendOldEntityCondition(final ProposedEntity oldentity, final Condition condition)
    throws PersistenceException {
        
        if (oldentity.getFields() != null) {
            for (int i = 0; i < _fields.length; ++i) {
                if (_fields[i].isStore() && _fields[i].isDirtyCheck()) {
                    SQLColumnInfo[] columns = _fields[i].getColumnInfo();
                    Object value = oldentity.getField(i);
                    if (value == null) { 
                        // append 'is NULL' in case the value is null    
                        for (int j = 0; j < columns.length; j++) {
                            String name = columns[j].getName();
                            condition.and(new Column(name).isNull());
                        }
                    } else if (value instanceof Identity) {
                        // raise exception if identity size doesn't match column length
                        Identity identity = (Identity) value;
                        if (identity.size() != columns.length) {
                            throw new PersistenceException("Size of identity field mismatch!");
                        }

                        // traverse through all the columns of the identity and append it to SQL
                        // dependent on weather the value of that column is null or not
                        for (int j = 0; j < columns.length; j++) {
                            String name = columns[j].getName();
                            if (identity.get(j) == null) {
                                condition.and(new Column(name).isNull());
                            } else {
                                condition.and(new Column(name).equal(new Parameter(name)));
                            }
                        }
                    } else {
                        // append condition with parameter for all normal fields
                        for (int j = 0; j < columns.length; j++) {
                            String name = columns[j].getName();
                            condition.and(new Column(name).equal(new Parameter(name)));
                        }
                    }
                }
            }
        }
    }

    /**
     * Bind values of new entity to the prepared statement.
     * 
     * @param newentity Entity holding the new values to set with update.
     * @param stmt CastorStatement containing query relevant data to be extended by parameters.
     * @throws PersistenceException If identity or column size mismatches.
     * @throws SQLException If database access error occurs.
     */
    private void bindNewEntity(final ProposedEntity newentity, final CastorStatement stmt)
    throws PersistenceException, SQLException {
        // bind fields of the row to be stored into the preparedStatement
        for (int i = 0; i < _fields.length; ++i) {
            if (_fields[i].isStore()) {
                SQLColumnInfo[] columns = _fields[i].getColumnInfo();
                Object value = newentity.getField(i);
                if (value == null) {
                    for (int j = 0; j < columns.length; j++) {
                         stmt.bindParameter(SET_PARAM_NAMESPACE + columns[j].getName(), 
                                 null, columns[j].getSqlType());
                    }
                } else if (value instanceof Identity) {
                    Identity id = (Identity) value;
                    if (id.size() != columns.length) {
                        throw new PersistenceException("Size of identity field mismatch!");
                    }
                    
                    for (int j = 0; j < columns.length; j++) {
                       stmt.bindParameter(SET_PARAM_NAMESPACE + columns[j].getName(), 
                               columns[j].toSQL(id.get(j)), columns[j].getSqlType());
                    }
                } else {
                    if (columns.length != 1) {
                        throw new PersistenceException("Complex field expected!");
                    }
                    
                     stmt.bindParameter(SET_PARAM_NAMESPACE + columns[0].getName(),
                             columns[0].toSQL(value), columns[0].getSqlType());
                }
            }
        }
    }

    /**
     * Bind identity values to the prepared statement.
     * 
     * @param identity Identity of the object to update.
     * @param stmt CastorStatement containing query relevant data to be extended by parameters.
     * @throws SQLException If a database access error occurs.
     */
    private void  bindIdentity(final Identity identity, final CastorStatement stmt) 
    throws SQLException {
        // bind the identity of the row to be stored into the preparedStatement
        for (int i = 0; i < _ids.length; i++) {
            stmt.bindParameter(_ids[i].getName(), _ids[i].toSQL(identity.get(i)),
                    _ids[i].getSqlType());

            if (LOG.isTraceEnabled()) {
                LOG.trace(Messages.format("jdo.bindingIdentity", _ids[i].getName(),
                        _ids[i].toSQL(identity.get(i))));
            }
        }
    }

    /**
     * Bind values of old entity to the prepared statement.
     * 
     * @param oldentity Entity holding the old values to check for concurrent modifications.
     * @param stmt CastorStatement containing query relevant data to be extended by parameters.
     * @throws PersistenceException If identity or column size mismatches.
     * @throws SQLException If database access error occurs.
     */
    private void bindOldEntity(final ProposedEntity oldentity, final CastorStatement stmt) 
    throws PersistenceException, SQLException {
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
                               stmt.bindParameter(columns[j].getName(), columns[j].toSQL(id.get(j)),
                                       columns[j].getSqlType());
                        }
                    } else {
                        if (columns.length != 1) {
                            throw new PersistenceException("Complex field expected!");
                        }
                        
                           stmt.bindParameter(columns[0].getName(), columns[0].toSQL(value),
                                   columns[0].getSqlType());
                    }
                }
            }
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug(Messages.format("jdo.storing", _type, stmt.toString()));
        }
    }

    //----------------------------------------------------------------------------------------------
}