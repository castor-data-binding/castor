/*
 * Copyright 2008 Oleg Nitz, Bruce Snyder, Ralf Joachim
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

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.castor.core.util.Messages;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.engine.JDBCSyntax;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.persist.spi.PersistenceFactory;
import org.exolab.castor.persist.spi.QueryExpression;

/**
 * HIGH-LOW key generators.
 * 
 * @see HighLowKeyGeneratorFactory
 * @author <a href="mailto:on AT ibis DOT odessa DOT ua">Oleg Nitz</a>
 * @author <a href="mailto:bruce DOT snyder AT gmail DOT com">Bruce Snyder</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision$ $Date: 2006-04-10 16:39:24 -0600 (Mon, 10 Apr 2006) $
 */
public final class HighLowKeyGenerator implements KeyGenerator {
    //-----------------------------------------------------------------------------------

    /** The <a href="http://jakarta.apache.org/commons/logging/">Jakarta
     *  Commons Logging</a> instance used for all logging. */
    private static final Log LOG = LogFactory.getLog(HighLowKeyGenerator.class);
    
    private static final String SEQ_TABLE = "table";

    private static final String SEQ_KEY = "key-column";

    private static final String SEQ_VALUE = "value-column";

    private static final String GRAB_SIZE = "grab-size";

    private static final String SAME_CONNECTION = "same-connection";

    private static final String GLOBAL = "global";

    private final Map < String, HighLowValueHandler < ? extends Object > > _handlers =
        new HashMap < String, HighLowValueHandler < ? extends Object > > ();

    private final PersistenceFactory _factory;
    
    private final int _sqlType;

    /** Sequence table name. */ 
    private String _seqTable;

    /** Sequence table key column name. */
    private String _seqKey;

    /** Sequence table value column name. */
    private String _seqValue;

    /** Grab size. */
    private int _grabSize;

    /** Shell the same connection be used?
     *  <br/>
     *  Note: This is less efficient, but in EJB envirinment we have no choice for now. */ 
    private boolean _sameConnection;

    /** Shell globally unique identities be generated? */
    private boolean _global;

    //-----------------------------------------------------------------------------------

    /**
     * Initialize the HIGH-LOW key generator.
     */
    public HighLowKeyGenerator(final PersistenceFactory factory, final Properties params,
            final int sqlType) throws MappingException {
        _factory = factory;
        _sqlType = sqlType;
        
        supportsSqlType(sqlType);
        
        initFromParameters(params);
    }
    
    public void initFromParameters(final Properties params) throws MappingException {
        _seqTable = params.getProperty(SEQ_TABLE);
        if (_seqTable == null) {
            throw new MappingException(Messages.format(
                    "mapping.KeyGenParamNotSet", SEQ_TABLE, getClass().getName()));
        }

        _seqKey = params.getProperty(SEQ_KEY);
        if (_seqKey == null) {
            throw new MappingException(Messages.format(
                    "mapping.KeyGenParamNotSet", SEQ_KEY, getClass().getName()));
        }

        _seqValue = params.getProperty(SEQ_VALUE);
        if (_seqValue == null) {
            throw new MappingException(Messages.format(
                    "mapping.KeyGenParamNotSet", SEQ_VALUE, getClass().getName()));
        }

        String grabSize = params.getProperty(GRAB_SIZE, "10");
        try {
            _grabSize = Integer.parseInt(grabSize);
        } catch (NumberFormatException except) {
            _grabSize = 0;
        }
        if (_grabSize <= 0) {
            throw new MappingException(Messages.format(
                    "mapping.wrongKeyGenParam", grabSize, GRAB_SIZE, getClass().getName()));
        }

        _sameConnection = "true".equals(params.getProperty(SAME_CONNECTION));
        _global = "true".equals(params.getProperty(GLOBAL));
    }

    //-----------------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    public synchronized Object generateKey(final Connection conn, final String tableName,
            final String primKeyName, final Properties props) throws PersistenceException {
        String internalTableName = tableName;
        if (_global) { internalTableName = "<GLOBAL>"; }
        
        HighLowValueHandler < ? extends Object > handler = _handlers.get(internalTableName);
        if (handler == null) {
            if (_sqlType == Types.INTEGER) {
                KeyGeneratorTypeHandlerInteger typeHandler = new KeyGeneratorTypeHandlerInteger(false);
                handler = new HighLowValueHandler < Integer > (internalTableName, _grabSize, typeHandler);
            } else if (_sqlType == Types.BIGINT) {
                KeyGeneratorTypeHandlerLong typeHandler = new KeyGeneratorTypeHandlerLong(false);
                handler = new HighLowValueHandler < Long > (internalTableName, _grabSize, typeHandler);
            } else {
                KeyGeneratorTypeHandlerBigDecimal typeHandler = new KeyGeneratorTypeHandlerBigDecimal(false);
                handler = new HighLowValueHandler < BigDecimal > (internalTableName, _grabSize, typeHandler);
            }
            _handlers.put(internalTableName, handler);
        }
        
        if (!handler.hasNext()) {
            // Create "SELECT seq_val FROM seq_table WHERE seq_key='table'"
            // with database-dependent keyword for lock
            // Note: Some databases (InstantDB, HypersonicSQL) don't support such locks.
            QueryExpression query = _factory.getQueryExpression();
            query.addColumn(_seqTable, _seqValue);
            query.addCondition(_seqTable, _seqKey, QueryExpression.OP_EQUALS, JDBCSyntax.PARAMETER);
            String lockSQL = query.getStatement(true);
            
            // For the case that "SELECT FOR UPDATE" is not supported, perform dirty checking
            String updateSQL = "UPDATE " +  _seqTable
                + " SET " + _seqValue + "=" + JDBCSyntax.PARAMETER
                + JDBCSyntax.WHERE + _seqKey + QueryExpression.OP_EQUALS + JDBCSyntax.PARAMETER
                + JDBCSyntax.AND + _seqValue + QueryExpression.OP_EQUALS + JDBCSyntax.PARAMETER;

            String maxSQL = JDBCSyntax.SELECT + "MAX(" + primKeyName + ") "
                + "FROM " + internalTableName;

            String insertSQL = "INSERT INTO " + _seqTable
                + " (" + _seqKey + "," + _seqValue + ") VALUES (?, ?)";
            
            PreparedStatement stmt = null;
            try {
                // Separate connection should be committed/rolled back at this point
                if (!_sameConnection) { conn.rollback(); }

                // Retry 7 times (lucky number)
                boolean success = false;
                for (int i = 0; !success && (i < 7); i++) {
                    stmt = conn.prepareStatement(lockSQL);
                    handler.bindTable(stmt, 1);
                    ResultSet rs = stmt.executeQuery();
                    handler.init(rs);
                    boolean found = rs.isFirst();
                    stmt.close();
                    
                    if (found) {
                        stmt = conn.prepareStatement(updateSQL);
                        handler.bindMax(stmt, 1);
                        handler.bindTable(stmt, 2);
                        handler.bindLast(stmt, 3);
                        success = (stmt.executeUpdate() == 1);
                        stmt.close();
                    } else {
                        if (!_global) {
                            stmt = conn.prepareStatement(maxSQL);
                            rs = stmt.executeQuery();
                            handler.init(rs);
                            stmt.close();
                        }
                        
                        stmt = conn.prepareStatement(insertSQL);
                        handler.bindTable(stmt, 1);
                        handler.bindMax(stmt, 2);
                        success = (stmt.executeUpdate() == 1);
                        stmt.close();
                    }
                }
                
                if (success) {
                    if (!_sameConnection) { conn.commit(); }
                } else {
                    if (!_sameConnection) { conn.rollback(); }
                    throw new PersistenceException(Messages.format(
                            "persist.keyGenFailed", getClass().getName()));
                }
            } catch (SQLException ex) {
                try {
                    if (!_sameConnection) { conn.rollback(); }
                } catch (SQLException ex2) {
                    LOG.warn ("Problem rolling back JDBC transaction.", ex2);
                }
                throw new PersistenceException(Messages.format(
                        "persist.keyGenSQL", getClass().getName(), ex.toString()), ex);
            } finally {
                try {
                    if (stmt != null) { stmt.close(); }
                } catch (SQLException ex) {
                    LOG.warn (Messages.message("persist.stClosingFailed"), ex);
                }
            }
        }

        return handler.next();
    }

    /**
     * {@inheritDoc}
     */
    public void supportsSqlType(final int sqlType) throws MappingException {
        if ((sqlType != Types.INTEGER)
                && (sqlType != Types.BIGINT)
                && (sqlType != Types.NUMERIC)
                && (sqlType != Types.DECIMAL)) {
            throw new MappingException(Messages.format("mapping.keyGenSQLType",
                    getClass().getName(), new Integer(sqlType)));
        }
    }

    /**
     * {@inheritDoc}
     */
    public byte getStyle() {
        return BEFORE_INSERT;
    }

    /**
     * {@inheritDoc}
     */
    public boolean isInSameConnection() {
        return _sameConnection;
    }

    /**
     * {@inheritDoc}
     */
    public String patchSQL(final String insert, final String primKeyName) {
        return insert;
    }

    //-----------------------------------------------------------------------------------
}