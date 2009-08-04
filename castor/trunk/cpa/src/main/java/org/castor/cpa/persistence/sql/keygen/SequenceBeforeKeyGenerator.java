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
import org.castor.cpa.persistence.sql.driver.DB2Factory;
import org.castor.cpa.persistence.sql.driver.InterbaseFactory;
import org.castor.cpa.persistence.sql.driver.PostgreSQLFactory;
import org.castor.cpa.persistence.sql.driver.OracleFactory;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.persist.spi.PersistenceFactory;

public final class SequenceBeforeKeyGenerator extends AbstractBeforeKeyGenerator {
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
            return getValue("SELECT "
                    + _factory.quoteName(getSeqName(tableName, primKeyName) + ".currval")
                    + " FROM " + _factory.quoteName(tableName), conn);
        }
    }
    
    private class PostgresqlType extends SequenceKeyGenValueHandler {
        protected Object getValue(final Connection conn, final String tableName,
                final String primKeyName, final Properties props) throws Exception {
            String sql = "SELECT nextval('\"" + getSeqName(tableName, primKeyName) + "\"')";
            return getValue(sql, conn);
        }
    }
        
    private class DB2Type extends SequenceKeyGenValueHandler {
        protected Object getValue(final Connection conn, final String tableName,
                final String primKeyName, final Properties props) throws Exception {
            return getValue("SELECT nextval FOR " + getSeqName(tableName, primKeyName)
                    + " FROM SYSIBM.SYSDUMMY1", conn);
        }
    }
        
    private class InterbaseType extends SequenceKeyGenValueHandler {
        protected Object getValue(final Connection conn, final String tableName,
                final String primKeyName, final Properties props) throws Exception {
            return getValue("SELECT gen_id(" + getSeqName(tableName, primKeyName) + ","
                    + _increment + ") FROM rdb$database", conn);
        }
    }
    
    private class OracleType extends SequenceKeyGenValueHandler {
        protected Object getValue(final Connection conn, final String tableName,
                final String primKeyName, final Properties props) throws Exception {
            return getValue("SELECT "
                    + _factory.quoteName(getSeqName(tableName, primKeyName) + ".nextval")
                    + " FROM DUAL", conn);
        }
    }
    
    //-----------------------------------------------------------------------------------
    
    /** The <a href="http://jakarta.apache.org/commons/logging/">Jakarta
     *  Commons Logging</a> instance used for all logging. */
    private static final Log LOG = LogFactory.getLog(SequenceBeforeKeyGenerator.class);
     
    private static final int STRING_KEY_LENGTH = 8;
    
    private PersistenceFactory _factory;

    private boolean _triggerPresent;
    
    private String _seqName;
    
    private int _increment;
    
    private KeyGeneratorTypeHandler<? extends Object> _typeHandler;

    private SequenceKeyGenValueHandler _type = null;

    //-----------------------------------------------------------------------------------
    
    /**
     * Initialize the SEQUENCE key generator for BEFORE_INSERT style 
     * {@link #generateKey} is called before INSERT. 
     * 
     * @param factory A PersistenceFactory instance.
     * @param params
     * @param sqlType A SQLTypidentifier.
     * @throws MappingException if this key generator is not compatible with the
     *         persistance factory.
     */
    public SequenceBeforeKeyGenerator(final PersistenceFactory factory, final Properties params,
            final int sqlType) throws MappingException {
        super(factory);
        _factory = factory;        
        _triggerPresent = "true".equals(params.getProperty("trigger", "false"));
        _seqName = params.getProperty("sequence", "{0}_seq");

        try {
            _increment = Integer.parseInt(params.getProperty("increment", "1"));
        } catch (NumberFormatException nfe) {
            _increment = 1;
        }
        
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
        String factoryName = _factory.getFactoryName();
        if (InterbaseFactory.FACTORY_NAME.equals(factoryName)) {
            _type = new InterbaseType();
        } else if (DB2Factory.FACTORY_NAME.equals(factoryName)) {
            _type = new DB2Type();
        } else if (PostgreSQLFactory.FACTORY_NAME.equals(factoryName)) {
            _type = new PostgresqlType();
        } else if (OracleFactory.FACTORY_NAME.equals(factoryName)) {
            _type = new OracleType();
        } else {
            _type = new DefaultType();
        }
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
    
    //-----------------------------------------------------------------------------------
}
