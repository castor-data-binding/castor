/*
 * Copyright 2009 Oleg Nitz, Stein M. Hugubakken, Bruce Snyder
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
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.castor.core.util.Messages;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.persist.spi.PersistenceFactory;

/**
 * IDENTITY key generator.
 * 
 * @see IdentityKeyGeneratorFactory
 * @author <a href="on@ibis.odessa.ua">Oleg Nitz</a>
 * @author <a href="mailto:dulci@start.no">Stein M. Hugubakken</a>
 * @author <a href="bruce DOT snyder AT gmail DOT com">Bruce Snyder</a>
 * @version $Revision$ $Date: 2006-04-25 15:08:23 -0600 (Tue, 25 Apr 2006) $
 */
public final class IdentityKeyGenerator extends AbstractAfterKeyGenerator {
    //-----------------------------------------------------------------------------------

    /** The <a href="http://jakarta.apache.org/commons/logging/">Jakarta
     *  Commons Logging</a> instance used for all logging. */
    private static final Log LOG = LogFactory.getLog(IdentityKeyGenerator.class);
     
    /** The key length used to initiaize KeyGeneratorTypeHandlerString instance. */
    private static final int STRING_KEY_LENGTH = 8;
    
    /** Persistence factory for the database engine the entity is persisted in.
     *  Used to format the SQL statement. */
    private final PersistenceFactory _factory;
    
    private KeyGeneratorTypeHandler<? extends Object> _typeHandler;
    
    //-----------------------------------------------------------------------------------

    /**
     * Initialize the IDENTITY key generator.
     * 
     * @param factory A PersistenceFactory instance.
     * @param sqlType A SQLTypidentifier.
     * @throws MappingException if this key generator is not compatible with the
     *         persistance factory.
     */
    public IdentityKeyGenerator(final PersistenceFactory factory, final int sqlType)
    throws MappingException {
        super(factory);
        
        _factory = factory;

        if (!_factory.isKeyGeneratorIdentitySupported()) {
            String msg = Messages.format("mapping.keyGenNotCompatible",
                    getClass().getName(), _factory.getFactoryName()); 
            throw new MappingException(msg);
        }

        if (!_factory.isKeyGeneratorIdentityTypeSupported(sqlType)) {
            String msg = Messages.format("mapping.keyGenSQLType",
                    getClass().getName(), new Integer(sqlType));
            throw new MappingException(msg);
        }

        initSqlTypeHandler(sqlType);
    }

    private void initSqlTypeHandler(final int sqlType) {
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

    //-----------------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    public Object generateKey(final Connection conn, final String tableName,
            final String primKeyName, final Properties props) throws PersistenceException {
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            // prepares the statement
            String sql = _factory.getIdentityQueryString(tableName, primKeyName);
            stmt = conn.prepareStatement(sql);
            
            // execute the prepared statement
            rs = stmt.executeQuery();

            // process result set using appropriate handler and return its value
            return _typeHandler.getValue(rs);
        } catch (SQLException e) {
            LOG.error("Problem generating new key", e);
            String msg = Messages.format("persist.keyGenSQL", 
                    this.getClass().getName(), e.toString());
            throw new PersistenceException(msg);            
        } finally {
            try {
                if (rs != null) { rs.close(); }
                if (stmt != null) { stmt.close(); }
            } catch (SQLException e) {
                LOG.warn("Problem closing JDBC statement", e);
            }
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
    public String patchSQL(final String insert, final String primKeyName) {
        return insert;
    }

    //-----------------------------------------------------------------------------------
}
