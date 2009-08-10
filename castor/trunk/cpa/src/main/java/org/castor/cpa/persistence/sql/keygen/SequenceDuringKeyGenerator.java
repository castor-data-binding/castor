/*
 * Copyright 2009 Ahmad Hassan, Ralf Joachim
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
package org.castor.cpa.persistence.sql.keygen;

import java.sql.Connection;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.text.MessageFormat;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.exolab.castor.jdo.PersistenceException;
import org.castor.core.util.Messages;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.persist.spi.PersistenceFactory;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.DuplicateIdentityException;
import org.exolab.castor.jdo.engine.SQLColumnInfo;
import org.exolab.castor.jdo.engine.SQLEngine;
import org.exolab.castor.jdo.engine.SQLFieldInfo;
import org.exolab.castor.jdo.engine.nature.ClassDescriptorJDONature;
import org.castor.cpa.persistence.sql.engine.SQLStatementInsertCheck;
import org.castor.cpa.persistence.sql.query.Insert;
import org.castor.cpa.persistence.sql.query.QueryContext;
import org.castor.persist.ProposedEntity;
import org.exolab.castor.mapping.ClassDescriptor;
import org.exolab.castor.persist.spi.Identity;

/**
 * SEQUENCE key generator.
 * 
 * @author <a href="on@ibis.odessa.ua">Oleg Nitz</a>
 * @author <a href="bruce DOT snyder AT gmail DOT com">Bruce Snyder</a>
 * @version $Revision: 8241 $ $Date: 2006-04-13 06:47:36 -0600 (Thu, 13 Apr 2006) $
 * @see SequenceKeyGeneratorFactory
 */
public final class SequenceDuringKeyGenerator extends AbstractKeyGenerator {
    //-----------------------------------------------------------------------------------

    private abstract class SequenceKeyGenValueHandler {
        private KeyGenerator _keyGenerator;
        private KeyGeneratorTypeHandler<? extends Object> _typeHandler;

        protected abstract Object getValue(Connection conn, String tableName,
                String primKeyName, Properties props) throws Exception;

        public Object getValue(final String sql, final Connection conn)
        throws PersistenceException {
            PreparedStatement stmt = null;
            try {
                stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery();
                return _typeHandler.getValue(rs);
            } catch (SQLException e) {
                String msg = Messages.format("persist.keyGenSQL", 
                        _keyGenerator.getClass().getName(), e.toString());
                throw new PersistenceException(msg);
            } finally {
                if (stmt != null) {
                    try {
                        stmt.close();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }

        public void setGenerator(final KeyGenerator generator) {
            _keyGenerator = generator;
        }

        public void setTypeHandler(final KeyGeneratorTypeHandler<? extends Object> typeHandler) {
            _typeHandler = typeHandler;
        }
    }

    private class DefaultType extends SequenceKeyGenValueHandler {
        protected Object getValue(final Connection conn, final String tableName,
                final String primKeyName, final Properties props) throws Exception {
            // used for orcale and sap after_insert
            return getValue("SELECT "
                    + _factory.quoteName(getSeqName(tableName, primKeyName) + ".currval")
                    + " FROM " + _factory.quoteName(tableName), conn);
        }
    }
    
    //-----------------------------------------------------------------------------------
    
    /** The <a href="http://jakarta.apache.org/commons/logging/">Jakarta
     *  Commons Logging</a> instance used for all logging. */
    private static final Log LOG = LogFactory.getLog(SequenceDuringKeyGenerator.class);
     
    private static final int STRING_KEY_LENGTH = 8;
    
    private PersistenceFactory _factory;

    private boolean _triggerPresent;
    
    private String _seqName;
    
    private KeyGeneratorTypeHandler<? extends Object> _typeHandler;

    private SequenceKeyGenValueHandler _type = null;
    
    /** SQL engine for all persistence operations at entities of the type this
     * class is responsible for. Holds all required information of the entity type. */
    private SQLEngine _engine;
    
    /** An sql statement. */
   // private String _statement;
    
    /** Name of the Table extracted from Class descriptor. */
    private String _mapTo;
    
    /** Represents the engine type obtained from clas descriptor. */
    private String _engineType = null;
    
    /** QueryContext for SQL query building, specifying database specific quotations 
     *  and parameters binding. */
    private final QueryContext _ctx;

    //-----------------------------------------------------------------------------------
    
    /**
     * Initialize the SEQUENCE key generator for DURING_INSERT style 
     * {@link #generateKey} is never called.
     * 
     * @param factory A PersistenceFactory instance.
     * @param params
     * @param sqlType A SQLTypidentifier.
     * @throws MappingException if this key generator is not compatible with the
     *         persistance factory.
     */
    public SequenceDuringKeyGenerator(final PersistenceFactory factory, final Properties params,
            final int sqlType) throws MappingException {
        _factory = factory;        
        _triggerPresent = "true".equals(params.getProperty("trigger", "false"));
        _seqName = params.getProperty("sequence", "{0}_seq");
        _ctx = new QueryContext(_factory);
        
        initSqlTypeHandler(sqlType);
        initType();
    }

    protected void initSqlTypeHandler(final int sqlType) {
        if (sqlType == Types.INTEGER) {
            _typeHandler = new KeyGeneratorTypeHandlerInteger(true);
        } else if (sqlType == Types.BIGINT) {
            _typeHandler = new KeyGeneratorTypeHandlerLong(true);
        } else if ((sqlType == Types.CHAR) || (sqlType == Types.VARCHAR)) {
            _typeHandler = new KeyGeneratorTypeHandlerString(true, STRING_KEY_LENGTH);
        } else {
            _typeHandler = new KeyGeneratorTypeHandlerBigDecimal(true);
        }
    }
    
    private String getSeqName(final String tableName, final String primKeyName) {
        return MessageFormat.format(_seqName, new Object[] {tableName, primKeyName});
    }
    
    private void initType() {
        _type = new DefaultType();
        _type.setGenerator(this);
        _type.setTypeHandler(_typeHandler);
     }
    
    //-----------------------------------------------------------------------------------

    /**
     * @param conn An open connection within the given transaction.
     * @param tableName The table name.
     * @param primKeyName The primary key name.
     * @param props A temporary replacement for Principal object.
     * @return A new key.
     * @throws PersistenceException An error occured talking to persistent storage.
     */
    public Object generateKey(final Connection conn, final String tableName,
            final String primKeyName, final Properties props) throws PersistenceException {
        try {
            return _type.getValue(conn, tableName, primKeyName, props);
        } catch (Exception e) {
            LOG.error("Problem generating new key", e);
            throw new PersistenceException(Messages.format("persist.keyGenSQL", e));
        }
    }

    /**
     * {@inheritDoc}
     */
    public boolean isInSameConnection() {
        return true;
    }
    
    /**
     * {@inheritDoc}
     */
    public KeyGenerator buildStatement(final SQLEngine engine) {
        _engine = engine;
        ClassDescriptor clsDesc = _engine.getDescriptor();
        _engineType = clsDesc.getJavaClass().getName();
        _mapTo = new ClassDescriptorJDONature(clsDesc).getTableName();
        
        Insert insert = new Insert(_mapTo);

        // is it right to omit all identities in this case?
        // maybe we should support to define a separat keygen
        // for every identity or complex/custom keygen that
        // supports multiple columns.
        
        SQLFieldInfo[] fields = _engine.getInfo();
        for (int i = 0; i < fields.length; ++i) {
            if (fields[i].isStore()) {
                SQLColumnInfo[] columns = fields[i].getColumnInfo();
                for (int j = 0; j < columns.length; j++) {
                    insert.addInsert(columns[j].getName());
                   
                }
            }
        }
        SQLColumnInfo[] ids = _engine.getColumnInfoForIdentities();
        //_statement = this.patchSQL(_statement, ids[0].getName());
        if (!_triggerPresent) {
            insert.addSequence(_seqName, ids[0].getName());
        }
        
        insert.toString(_ctx);  
   
        if (LOG.isTraceEnabled()) {
            LOG.trace(Messages.format("jdo.creating", _engineType, _ctx.toString()));
        }
        return this;
    }

    /**
     * {@inheritDoc}
     */
    public Object executeStatement(final Database database, final Connection conn, 
            final Identity identity, final ProposedEntity entity) throws PersistenceException {
        SQLStatementInsertCheck lookupStatement = new SQLStatementInsertCheck(_engine, _factory);
        Identity internalIdentity = identity;
        SQLEngine extended = _engine.getExtends();

        PreparedStatement stmt = null;
        try {
            // must create record in the parent table first. all other dependents
            // are created afterwards. quick and very dirty hack to try to make
            // multiple class on the same table work.
            if (extended != null) {
                ClassDescriptor extDesc = extended.getDescriptor();
                if (!new ClassDescriptorJDONature(extDesc).getTableName().equals(_mapTo)) {
                    internalIdentity = extended.create(database, conn, entity, internalIdentity);
                }
            }
            
            String statement;
            SQLColumnInfo[] ids = _engine.getColumnInfoForIdentities();
            
            statement = _ctx.toString();
            statement += " RETURNING ";
            statement += _factory.quoteName(ids[0].getName());
            statement += " INTO ?";    
            statement = "{call " + statement + "}";
            
            stmt = conn.prepareCall(statement);
             
            if (LOG.isTraceEnabled()) {
                LOG.trace(Messages.format("jdo.creating", _engineType, stmt.toString()));
            }

            bindFields(entity, stmt);
            
            if (LOG.isTraceEnabled()) {
                LOG.trace(Messages.format("jdo.creating", _engineType, stmt.toString()));
            }

            // generate key during INSERT.
            CallableStatement cstmt = (CallableStatement) stmt;

            int sqlType = ids[0].getSqlType();
            cstmt.registerOutParameter(_ctx.parameterSize() + 1, sqlType);
            
            if (LOG.isDebugEnabled()) {
                LOG.debug(Messages.format("jdo.creating", _engineType, cstmt.toString()));
            }
            
            cstmt.execute();

            // first skip all results "for maximum portability"
            // as proposed in CallableStatement javadocs.
            while (cstmt.getMoreResults() || (cstmt.getUpdateCount() != -1)) {
                // no code to execute
            }

            // identity is returned in the last parameter.
            // workaround for INTEGER type in Oracle getObject returns BigDecimal.
            Object temp;
            if (sqlType == java.sql.Types.INTEGER) {
                temp = new Integer(cstmt.getInt(_ctx.parameterSize() + 1));
            } else {
                temp = cstmt.getObject(_ctx.parameterSize() + 1);
            }
            internalIdentity = new Identity(ids[0].toJava(temp));

            stmt.close();

            return internalIdentity;
        } catch (SQLException except) {
            LOG.fatal(Messages.format("jdo.storeFatal",  _engineType,  _ctx.toString()), except);

            Boolean isDupKey = _factory.isDuplicateKeyException(except);
            if (Boolean.TRUE.equals(isDupKey)) {
                throw new DuplicateIdentityException(Messages.format(
                        "persist.duplicateIdentity", _engineType, internalIdentity), except);
            } else if (Boolean.FALSE.equals(isDupKey)) {
                throw new PersistenceException(Messages.format("persist.nested", except), except);
            }

            // without an identity we can not check for duplicate key
            if (internalIdentity == null) {
                throw new PersistenceException(Messages.format("persist.nested", except), except);
            }

            // check for duplicate key the old fashioned way, after the INSERT
            // failed to prevent race conditions and optimize INSERT times.
            lookupStatement.insertDuplicateKeyCheck(conn, internalIdentity);

            try {
                if (stmt != null) { stmt.close(); }
            } catch (SQLException except2) {
                LOG.warn("Problem closing JDBC statement", except2);
            }
            
            throw new PersistenceException(Messages.format("persist.nested", except), except);
        }
    }
    
    /**
     * Binds parameters values to the PreparedStatement.
     * 
     * @param entity Entity instance from which field values to be fetached to
     *               bind with sql insert statement.
     * @param stmt PreparedStatement object containing sql staatement.
     * @throws SQLException If a database access error occurs.
     * @throws PersistenceException If identity size mismatches.
     */
    private void bindFields(final ProposedEntity entity, final PreparedStatement stmt
            ) throws SQLException, PersistenceException {
        SQLFieldInfo[] fields = _engine.getInfo();
        for (int i = 0; i < fields.length; ++i) {
            SQLColumnInfo[] columns = fields[i].getColumnInfo();
            if (fields[i].isStore()) {
                Object value = entity.getField(i);
                if (value == null) {
                    for (int j = 0; j < columns.length; j++) {
                        _ctx.bindParameter(stmt, columns[j].getName(), null, 
                                columns[j].getSqlType());
                    }
                } else if (value instanceof Identity) {
                    Identity identity = (Identity) value;
                    if (identity.size() != columns.length) {
                        throw new PersistenceException("Size of identity field mismatch!");
                    }
                    for (int j = 0; j < columns.length; j++) {
                        _ctx.bindParameter(stmt, columns[j].getName(), 
                                columns[j].toSQL(identity.get(j)), columns[j].getSqlType());
                    }
                } else {
                    if (columns.length != 1) {
                        throw new PersistenceException("Complex field expected!");
                    }
                    _ctx.bindParameter(stmt, columns[0].getName(), columns[0].toSQL(value), 
                            columns[0].getSqlType());
                }
            }
        }
    }
    
    //-----------------------------------------------------------------------------------
}
