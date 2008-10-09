/*
 * Copyright 2008 Oleg Nitz, Leonardo Souza Mario Bueno, Bruce Snyder, Ralf Joachim
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
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.castor.core.util.Messages;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.QueryException;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.persist.spi.KeyGenerator;
import org.exolab.castor.persist.spi.PersistenceFactory;
import org.exolab.castor.persist.spi.QueryExpression;

/**
 * MAX key generators.
 * 
 * @see MaxKeyGeneratorFactory
 * @author <a href="mailto:on AT ibis DOT odessa DOT ua">Oleg Nitz</a>
 * @author <a href="mailto:leonardo AT itera DOT com DOT br">Leonardo Souza Mario Bueno</a>
 * @author <a href="mailto:bruce DOT snyder AT gmail DOT com">Bruce Snyder</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision$ $Date: 2006-04-25 15:08:23 -0600 (Tue, 25 Apr 2006) $
 */
public final class MaxKeyGenerator implements KeyGenerator {
    //-----------------------------------------------------------------------------------

    private interface MaxSqlTypeHandler {
        Object getValue(ResultSet rs) throws SQLException;
    }

    private static class MaxIntegerSqlTypeHandler implements MaxSqlTypeHandler {
        private static final Integer ONE = new Integer(1);
        
        /**
         * {@inheritDoc}
         */
        public Object getValue(final ResultSet rs) throws SQLException {
            if (!rs.next()) { return ONE; }
            return new Integer(rs.getInt(1) + 1);
        }
    }

    private static class MaxLongSqlTypeHandler implements MaxSqlTypeHandler {
        private static final Long ONE = new Long(1);
        
        /**
         * {@inheritDoc}
         */
        public Object getValue(final ResultSet rs) throws SQLException {
            if (!rs.next()) { return ONE; }
            return new Long(rs.getLong(1) + 1);
        }
    }

    private static class MaxBigDecimalSqlTypeHandler implements MaxSqlTypeHandler {
        private static final BigDecimal ONE = new BigDecimal(1);

        /**
         * {@inheritDoc}
         */
        public Object getValue(final ResultSet rs) throws SQLException {
            if (!rs.next()) { return ONE; }
            BigDecimal max = rs.getBigDecimal(1);
            if (max == null) { return ONE; }
            return max.add(ONE);
        }
    }

    //-----------------------------------------------------------------------------------

    /** The <a href="http://jakarta.apache.org/commons/logging/">Jakarta
     *  Commons Logging</a> instance used for all logging. */
    private static final Log LOG = LogFactory.getLog(MaxKeyGenerator.class);
    
    private final PersistenceFactory _factory;
    
    private MaxSqlTypeHandler _sqlTypeHandler;
    
    //-----------------------------------------------------------------------------------

    /**
     * Initialize the MAX key generator.
     */
    public MaxKeyGenerator(final PersistenceFactory factory, final int sqlType)
    throws MappingException {
        _factory = factory;

        supportsSqlType(sqlType);
        initSqlTypeHandler(sqlType);
    }

    private void initSqlTypeHandler(final int sqlType) {
        if (sqlType == Types.INTEGER) {
            _sqlTypeHandler = new MaxIntegerSqlTypeHandler();
        } else if (sqlType == Types.BIGINT) {
            _sqlTypeHandler = new MaxLongSqlTypeHandler();
        } else {
            _sqlTypeHandler = new MaxBigDecimalSqlTypeHandler();
        }
    }
    
    //-----------------------------------------------------------------------------------

    /**
     * Generate a new key for the specified table as "MAX(primary_key) + 1".
     * If there is no records in the table, then the value 1 is returned.
     * <br/>
     * {@inheritDoc}
     */
    public Object generateKey(final Connection conn, final String tableName,
            final String primKeyName, final Properties props) throws PersistenceException {
        PreparedStatement stmt = null;
        try {
            String sql = getQueryExpression(tableName, primKeyName);
            stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            return _sqlTypeHandler.getValue(rs);
        } catch (SQLException ex) {
            throw new PersistenceException(Messages.format(
                    "persist.keyGenSQL", getClass().getName(), ex.toString()), ex);
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException ex) {
                    LOG.warn (Messages.message ("persist.stClosingFailed"), ex);
                }
            }
        }
    }

    /**
     * Get query string to retrive next primary key value.
     * 
     * @param table The table name.
     * @param column The primary key column name.
     * @return The query string to retrive next primary key value.
     * @throws QueryException The query cannot be constructed for this engine.
     */
    private String getQueryExpression(final String table, final String column)
    throws QueryException {
        /*
         * Pre 1.2.1 release of Castor we used a different query for MySQL that
         * does not create a database lock. The only reason for doing so is, that
         * MySQL has not supported such locks at some point. According to my tests
         * this is not the case any more so I decided to comment out this special
         * handling for MySQL as using locks may prevent us from getting
         * DuplikateIdentityExceptions. (20.05.2008/RJ)
         * 
         * // Create SQL sentence of the form
         * // "SELECT MAX(pk) FROM table"
         * // without lock
         * QueryExpression query = _factory.getQueryExpression();
         * query.addSelect("MAX(" + _factory.quoteName(column) + ")");
         * query.addTable(table);
         * return query.getStatement(false);
         */

        // Create SQL sentence of the form
        // "SELECT pk FROM table WHERE pk=(SELECT MAX(t1.pk) FROM table t1)"
        // with database-dependent keyword for lock
        QueryExpression query = _factory.getQueryExpression();
        query.addColumn(table, column);
        query.addCondition(table, column, QueryExpression.OP_EQUALS,
                "(SELECT MAX(t1." + _factory.quoteName(column) + ") FROM "
                + _factory.quoteName(table) + " t1)");
        return query.getStatement(true);
    }
    
    /**
     * {@inheritDoc}
     */
    public void supportsSqlType(final int sqlType) throws MappingException {
        if ((sqlType != Types.INTEGER)
                && (sqlType != Types.BIGINT)
                && (sqlType != Types.NUMERIC)
                && (sqlType != Types.DECIMAL)) {
            throw new MappingException(Messages.format(
                    "mapping.keyGenSQLType", getClass().getName(), new Integer(sqlType)));
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
        return true;
    }

    /**
     * {@inheritDoc}
     */
    public String patchSQL(final String insert, final String primKeyName) {
        return insert;
    }

    //-----------------------------------------------------------------------------------
}


