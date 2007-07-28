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
import org.castor.jdo.engine.SQLTypeInfos;
import org.castor.persist.ProposedEntity;
import org.castor.util.Messages;
import org.exolab.castor.jdo.ObjectDeletedException;
import org.exolab.castor.jdo.ObjectModifiedException;
import org.exolab.castor.jdo.PersistenceException;
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

    /** Indicates whether there is a field to persist at all; in the case of 
     *  EXTEND relationships where no additional attributes are defined in the 
     *  extending class, this might NOT be the case; in general, a class has to have
     *  at least one field that is to be persisted. */
    private boolean _hasFieldsToPersist = false;

    private String _statementLazy;

    private String _statementDirty;
    
    private String _statementLoad;

    public SQLStatementStore(final SQLEngine engine, final PersistenceFactory factory,
                             final String load) {
        _engine = engine;
        _factory = factory;
        _type = engine.getDescriptor().getJavaClass().getName();
        _mapTo = engine.getDescriptor().getTableName();
        
        // iterate through all fields to check whether there is a field
        // to persist at all; in the case of extend relationships where no 
        // additional attributes are defined in the extending class, this 
        // might NOT be the case
        SQLFieldInfo[] fields = _engine.getInfo();
        for (int i = 0 ; i < fields.length ; ++i) {
            if (fields[i].isStore()) {
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
            SQLFieldInfo[] fields = _engine.getInfo();
            for (int i = 0 ; i < fields.length ; ++i) {
                if (fields[i].isStore()) {
                    SQLColumnInfo[] columns = fields[i].getColumnInfo();
                    for (int j = 0; j < columns.length; j++) {
                        if (count > 0) { sql.append(','); }
                        sql.append(_factory.quoteName(columns[j].getName()));
                        sql.append("=?");
                        ++count;
                    }
                }
            }
            
            sql.append(JDBCSyntax.WHERE);

            SQLColumnInfo[] ids = _engine.getColumnInfoForIdentities();
            for (int i = 0; i < ids.length; i++) {
                if (i > 0) { sql.append(" AND "); }
                sql.append(_factory.quoteName(ids[i].getName()));
                sql.append(QueryExpression.OP_EQUALS);
                sql.append(JDBCSyntax.PARAMETER);
            }

            _statementLazy = sql.toString();
            
            if (LOG.isTraceEnabled()) {
                LOG.trace(Messages.format("jdo.updating", _type, _statementLazy));
            }

            for (int i = 0 ; i < fields.length ; ++i) {
                if (fields[i].isStore() && fields[i].isDirtyCheck()) {
                    SQLColumnInfo[] columns = fields[i].getColumnInfo();
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
    
    public Object executeStatement(final Connection conn, final Identity identity,
                                   final ProposedEntity newentity,
                                   final ProposedEntity oldentity)
    throws PersistenceException {
        PreparedStatement stmt = null;
        int count;
        String storeStatement = null;

        // Must store record in parent table first.
        // All other dependents are stored independently.
        SQLEngine extended = _engine.getExtends();
        if (extended != null) {
            // | quick and very dirty hack to try to make multiple class on the same table work
            if (!extended.getDescriptor().getTableName().equals(_mapTo)) {
                extended.store(conn, identity, newentity, oldentity);
            }
        }

        // Only build and execute an UPDATE statement if the class to be updated has 
        // fields to persist.
        if (_hasFieldsToPersist) {
            try {
                storeStatement = getStoreStatement(oldentity);
                stmt = conn.prepareStatement(storeStatement);
                
                if (LOG.isTraceEnabled()) {
                    LOG.trace(Messages.format("jdo.storing", _type, stmt.toString()));
                }
                
                count = 1;
                
                // bind fields of the row to be stored into the preparedStatement
                SQLFieldInfo[] fields = _engine.getInfo();
                for (int i = 0 ; i < fields.length ; ++i) {
                    if (fields[i].isStore()) {
                        SQLColumnInfo[] columns = fields[i].getColumnInfo();
                        Object value = newentity.getField(i);
                        if (value == null) {
                            for (int j = 0; j < columns.length; j++) {
                                stmt.setNull(count++, columns[j].getSqlType());
                            }
                        } else if (value instanceof Identity) {
                            Identity id = (Identity) value;
                            if (id.size() != columns.length) {
                                throw new PersistenceException("Size of identity field mismatch!");
                            }
                            
                            for (int j = 0; j < columns.length; j++) {
                                SQLTypeInfos.setValue(stmt, count++, columns[j].toSQL(id.get(j)), columns[j].getSqlType());
                            }
                        } else {
                            if (columns.length != 1) {
                                throw new PersistenceException("Complex field expected!");
                            }
                            
                            SQLTypeInfos.setValue(stmt, count++, columns[0].toSQL(value), columns[0].getSqlType());
                        }
                    }
                }
                
                // bind the identity of the row to be stored into the preparedStatement
                SQLColumnInfo[] ids = _engine.getColumnInfoForIdentities();
                if (identity.size() != ids.length) {
                    throw new PersistenceException("Size of identity field mismatched!");
                }
                
                for (int i = 0; i < ids.length; i++) {
                    stmt.setObject(count++, ids[i].toSQL(identity.get(i)));
                    if (LOG.isTraceEnabled()) {
                        LOG.trace(Messages.format("jdo.bindingIdentity", ids[i].getName(), ids[i].toSQL(identity.get(i))));
                    }
                }                    
                
                // bind the old fields of the row to be stored into the preparedStatement
                if (oldentity.getFields() != null) {
                    boolean supportsSetNull = ((BaseFactory) _factory).supportsSetNullInWhere();
                    
                    for (int i = 0 ; i < fields.length ; ++i) {
                        if (fields[i].isStore() && fields[i].isDirtyCheck()) {
                            SQLColumnInfo[] columns = fields[i].getColumnInfo();
                            Object value = oldentity.getField(i);
                            if (value == null) {
                                if (supportsSetNull) {
                                    for (int j = 0; j < columns.length; j++) {
                                        stmt.setNull(count++, columns[j].getSqlType());
                                    }
                                }
                            } else if (value instanceof Identity) {
                                Identity id = (Identity) value;
                                if (id.size() != columns.length) {
                                    throw new PersistenceException("Size of identity field mismatch!");
                                }
                                
                                for (int j = 0; j < columns.length; j++) {
                                    SQLTypeInfos.setValue(stmt, count++, columns[j].toSQL(id.get(j)), columns[j].getSqlType());
                                    
                                    if (LOG.isTraceEnabled()) {
                                        LOG.trace(Messages.format("jdo.bindingField", columns[j].getName(), columns[j].toSQL(id.get(j))));
                                    }
                                }
                            } else {
                                if (columns.length != 1) {
                                    throw new PersistenceException("Complex field expected!");
                                }
                                
                                SQLTypeInfos.setValue(stmt, count++, columns[0].toSQL(value), columns[0].getSqlType() );
                            
                                if (LOG.isTraceEnabled()) {
                                    LOG.trace(Messages.format("jdo.bindingField", columns[0].getName(), columns[0].toSQL(value)));
                                }
                            }
                        }
                    }
                }
                
                if (LOG.isDebugEnabled()) {
                    LOG.debug(Messages.format("jdo.storing", _type, stmt.toString()));
                }

                if (stmt.executeUpdate() <= 0) { // SAP DB returns -1 here
                    // If no update was performed, the object has been previously
                    // removed from persistent storage or has been modified if
                    // dirty checking. Determine which is which.
                    stmt.close();

                    if (oldentity.getFields() != null) {
                        stmt = conn.prepareStatement(_statementLoad);
                        
                        if (LOG.isTraceEnabled()) {
                            LOG.trace(Messages.format("jdo.storing", _type, stmt.toString()));
                        }
                        
                        // bind the identity to the prepareStatement
                        count = 1;
                        for (int i = 0; i < ids.length; i++) {
                            stmt.setObject(count++, ids[i].toSQL(identity.get(i)));
                        }
                        
                        ResultSet res = stmt.executeQuery();
                        if (res.next()) {                     
                            StringBuffer enlistFieldsNotMatching = new StringBuffer();
                            
                            int numberOfFieldsNotMatching = 0;
                            for (int i = 0; i < fields.length; i++) {
                                SQLColumnInfo[] columns = fields[i].getColumnInfo();
                                Object value = oldentity.getField(i);
                                Object currentField = columns[0].toJava(res.getObject(columns[0].getName()));
                                if (fields[i].getTableName().compareTo(_mapTo) == 0) {
                                    if ((value == null) || ((value != null) && (currentField == null))) {
                                        enlistFieldsNotMatching.append("(" + _type + ")." + columns[0].getName() + ": ");
                                        enlistFieldsNotMatching.append("[" + value + "/" + currentField + "]"); 
                                    } else if (!value.equals(currentField) ) {
                                        if (numberOfFieldsNotMatching >= 1) {
                                            enlistFieldsNotMatching.append(", ");
                                        }
                                        enlistFieldsNotMatching.append("(" + _type + ")." + columns[0].getName() + ": ");
                                        enlistFieldsNotMatching.append("[" + value + "/" + currentField + "]"); 
                                        numberOfFieldsNotMatching++;
                                    }
                                }
                            }
                            throw new ObjectModifiedException(Messages.format("persist.objectModified", _type, identity, enlistFieldsNotMatching.toString()));
                        }
                    }
                    throw new ObjectDeletedException(Messages.format("persist.objectDeleted", _type, identity));
                }                
            } catch (SQLException except) {
                LOG.fatal(Messages.format("jdo.storeFatal", _type,  storeStatement), except);
                throw new PersistenceException(Messages.format("persist.nested", except), except);
            } finally {
                try {
                    // Close the insert/select statement
                    if (stmt != null) { stmt.close(); }
                } catch (SQLException except2) {
                    LOG.warn("Problem closing JDBC statement", except2);
                }
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
        } else if (((BaseFactory) _factory).supportsSetNullInWhere()) {
            return _statementDirty;
        } else {
            int pos = _statementDirty.length() - 1;
            
            StringBuffer sql = new StringBuffer(pos * 4);
            sql.append(_statementDirty);
            
            SQLFieldInfo[] _fields = _engine.getInfo();
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
}
