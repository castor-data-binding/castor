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
import org.castor.cpa.persistence.sql.driver.SapDbFactory;
import org.castor.cpa.persistence.sql.driver.InterbaseFactory;
import org.castor.cpa.persistence.sql.driver.PostgreSQLFactory;
import org.castor.cpa.persistence.sql.driver.OracleFactory;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.persist.spi.PersistenceFactory;

/**
 * Extends AbstractBeforeKeyGenerator and implements additional methods specific
 * to Sequence Key generator. It invovles the fetching the Table ID before the 
 * record is inserted into the table. 
 * 
 * @author <a href="mailto:ahmad DOT hassan AT gmail DOT com">Ahmad Hassan</a>
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision$ $Date: 2009-07-13 17:22:43 (Tue, 28 Jul 2009) $
 */
public final class SequenceBeforeKeyGenerator extends AbstractBeforeKeyGenerator {
    //-----------------------------------------------------------------------------------

    /**
     * Implements database engine specific subclasses which generates the
     * database specific query systex for fetching ID from the database and then
     * SequenceKeyGetValueHandler runs that query using JDBC connection.
     */
    private abstract class SequenceKeyGenValueHandler {
        
        /** key generator for producing identities for objects after 
         * they are created in the database.
         */
        private KeyGenerator _keyGenerator;
        
        /** Particular type handler instance. */
        private KeyGeneratorTypeHandler<? extends Object> _typeHandler;

        /** Abstract method that must be implemented by subclasses of this class and
         * responsible for running query to get identity. 
         * 
         * @param conn An open JDBC connection.
         * @param tableName Name of the table from which identity will be fetched.
         * @param primKeyName Primary key of the table.
         * @param props database engine specific properties.  
         * 
         * @return Identity.
         * @throws Exception If fails to retrieve  identity. 
         */
        protected abstract Object getValue(Connection conn, String tableName,
                String primKeyName, Properties props) throws Exception;

        /**
         * Method that runs sql query using the provided JDBC connection.
         * 
         * @param sql A sql query
         * @param conn An open JDBC connection
         * 
         * @return Query results containing the identity value. 
         * @throws PersistenceException If fails to retrive identity value from resultset or
         *                              database error occurs. 
         */
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

        /**
         * Sets the KeyGenerator instance value.
         *
         * @param generator Provided keyGenerator instance.
         */
        public void setGenerator(final KeyGenerator generator) {
            _keyGenerator = generator;
        }

        /**
         * Sets typeHandler object with the value provided.
         * 
         * @param typeHandler Provided typeHandler instance.
         */
        public void setTypeHandler(final KeyGeneratorTypeHandler<? extends Object> typeHandler) {
            _typeHandler = typeHandler;
        }
    }

    /**
     * Implements SequenceKeyGenValueHandler that generates sql query used as 
     * a default query except for the particular database engine types.
     */
    private class DefaultType extends SequenceKeyGenValueHandler {
        
        /**
         * Generates sql select query for fetching identity and then calss the
         * base class getValue method of query execution.
         * 
         * @param conn An open JDBC connection.
         * @param tableName Name of the table from which identity will be fetched.
         * @param primKeyName Primary key of the table.
         * @param props database engine specific properties. 
         * @return ResutlSet containing identity.
         * @throws Exception If database error occurs. 
         */
        protected Object getValue(final Connection conn, final String tableName,
                final String primKeyName, final Properties props) throws Exception {
            return getValue("SELECT "
                    + _factory.quoteName(getSeqName(tableName, primKeyName) + ".currval")
                    + " FROM " + _factory.quoteName(tableName), conn);
        }
    }
    
    /**
     * Implements SequenceKeyGenValueHandler that generates sql query for fetching
     * identity from Postgressql database.  
     */
    private class PostgresqlType extends SequenceKeyGenValueHandler {
        
        /**
         * Generates sql select query for fetching identity and then calss the
         * base class getValue method of query execution.
         * 
         * @param conn An open JDBC connection.
         * @param tableName Name of the table from which identity will be fetched.
         * @param primKeyName Primary key of the table.
         * @param props database engine specific properties. 
         * @return ResutlSet containing identity.
         * @throws Exception If database error occurs. 
         */
        protected Object getValue(final Connection conn, final String tableName,
                final String primKeyName, final Properties props) throws Exception {
            String sql = "SELECT nextval('\"" + getSeqName(tableName, primKeyName) + "\"')";
            return getValue(sql, conn);
        }
    }
        
    /**
     * Implements SequenceKeyGenValueHandler that generates sql query for fetching
     * identity from DB2 database.  
     */
    private class DB2Type extends SequenceKeyGenValueHandler {
        
        /**
         * Generates sql select query for fetching identity and then calss the
         * base class getValue method of query execution.
         * 
         * @param conn An open JDBC connection.
         * @param tableName Name of the table from which identity will be fetched.
         * @param primKeyName Primary key of the table.
         * @param props database engine specific properties. 
         * @return ResutlSet containing identity.
         * @throws Exception If database error occurs. 
         */
        protected Object getValue(final Connection conn, final String tableName,
                final String primKeyName, final Properties props) throws Exception {
            return getValue("SELECT nextval FOR " + getSeqName(tableName, primKeyName)
                    + " FROM SYSIBM.SYSDUMMY1", conn);
        }
    }
        
    /**
     * Implements SequenceKeyGenValueHandler that generates sql query for fetching
     * identity from Interbase database.  
     */
    private class InterbaseType extends SequenceKeyGenValueHandler {
        
        /**
         * Generates sql select query for fetching identity and then calss the
         * base class getValue method of query execution.
         * 
         * @param conn An open JDBC connection.
         * @param tableName Name of the table from which identity will be fetched.
         * @param primKeyName Primary key of the table.
         * @param props database engine specific properties. 
         * @return ResutlSet containing identity.
         * @throws Exception If database error occurs. 
         */
        protected Object getValue(final Connection conn, final String tableName,
                final String primKeyName, final Properties props) throws Exception {
            return getValue("SELECT gen_id(" + getSeqName(tableName, primKeyName) + ","
                    + _increment + ") FROM rdb$database", conn);
        }
    }
    
    /**
     * Implements SequenceKeyGenValueHandler that generates sql query for fetching
     * identity from oracle database.  
     */
    private class OracleType extends SequenceKeyGenValueHandler {
        
        /**
         * Generates sql select query for fetching identity and then calss the
         * base class getValue method of query execution.
         * 
         * @param conn An open JDBC connection.
         * @param tableName Name of the table from which identity will be fetched.
         * @param primKeyName Primary key of the table.
         * @param props database engine specific properties. 
         * @return ResutlSet containing identity.
         * @throws Exception If database error occurs. 
         */
        protected Object getValue(final Connection conn, final String tableName,
                final String primKeyName, final Properties props) throws Exception {
            return getValue("SELECT "
                    + _factory.quoteName(getSeqName(tableName, primKeyName) + ".nextval")
                    + " FROM DUAL", conn);
        }
    }
    
    /**
     * Implements SequenceKeyGenValueHandler that generates sql query for fetching
     * identity from sap/max database.  
     */
    private class SapDbType extends SequenceKeyGenValueHandler {
        
        /**
         * Generates sql select query for fetching identity and then calss the
         * base class getValue method of query execution.
         * 
         * @param conn An open JDBC connection.
         * @param tableName Name of the table from which identity will be fetched.
         * @param primKeyName Primary key of the table.
         * @param props database engine specific properties. 
         * @return ResutlSet containing identity.
         * @throws Exception If database error occurs. 
         */
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
     
    /** Key length used for KeyGeneratorTypeHandlerString. */
    private static final int STRING_KEY_LENGTH = 8;
    
    /** Persistence factory for the database engine the entity is persisted in.
     *  Used to format the SQL statement. */
    private PersistenceFactory _factory;
    
    /** Sequence name associated with the table. */
    private String _seqName;
    
    /** Value used in fetching identity from Interbase database. */
    private int _increment;
    
    /** Particular type handler instance. */
    private KeyGeneratorTypeHandler<? extends Object> _typeHandler;

    /** Sequence Key Handler type. */
    private SequenceKeyGenValueHandler _type = null;

    //-----------------------------------------------------------------------------------
    
    /**
     * Initialize the SEQUENCE key generator for BEFORE_INSERT style 
     * {@link #generateKey} is called before INSERT. 
     * 
     * @param factory A PersistenceFactory instance.
     * @param params Database specific properties. 
     * @param sqlType A SQLTypidentifier.
     * @throws MappingException if this key generator is not compatible with the
     *         persistance factory.
     */
    public SequenceBeforeKeyGenerator(final PersistenceFactory factory, final Properties params,
            final int sqlType) throws MappingException {
        super(factory);
        _factory = factory;        
        _seqName = params.getProperty("sequence", "{0}_seq");

        try {
            _increment = Integer.parseInt(params.getProperty("increment", "1"));
        } catch (NumberFormatException nfe) {
            _increment = 1;
        }
        
        initSqlTypeHandler(sqlType);
        initType();
    }

    /**
     * Initialize the Handler based on SQL Type.
     * 
     * @param sqlType A SQLTypidentifier.
     */
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
    
    /**
     * Formats the sequence name using name of the table and ID.
     * 
     * @param tableName Name of the table.
     * @param primKeyName ID of the table.
     * @return Strign representing formatted sequence name.
     */
    private String getSeqName(final String tableName, final String primKeyName) {
        return MessageFormat.format(_seqName, new Object[] {tableName, primKeyName});
    }
    
    /** Instantiate class properties i.e type and typeHandler based on the 
     * factory type. */
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
        } else if (SapDbFactory.FACTORY_NAME.equals(factoryName)) {
            _type = new SapDbType();
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
