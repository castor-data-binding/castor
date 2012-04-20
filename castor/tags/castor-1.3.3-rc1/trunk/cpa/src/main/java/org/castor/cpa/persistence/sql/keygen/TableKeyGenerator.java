/*
 * Copyright 2010 Werner Guttmann
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
import org.castor.cpa.jpa.info.JPATableGeneratorDescriptor;
import org.castor.cpa.persistence.sql.keygen.typehandler.KeyGeneratorTypeHandler;
import org.castor.cpa.persistence.sql.keygen.typehandler.KeyGeneratorTypeHandlerFactory;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.persist.spi.PersistenceFactory;

/**
 * @author <a href="mailto:wguttmn AT codehaus DOT org">Werner Guttmann</a>
 * @version $Revision$ $Date$
 */
public class TableKeyGenerator extends AbstractBeforeKeyGenerator {
    //-----------------------------------------------------------------------------------

    private static final Log LOG = LogFactory.getLog(TableKeyGenerator.class);
    
    public static final String DESCRIPTOR_KEY = "descriptor";
    public static final String DEFAULT_TABLE_NAME = "GENERATOR_TABLE";
    public static final int DEFAULT_ALLOCATION_SIZE = 50;
    public static final int DEFAULT_INITIAL_VALUE = 0;
    public static final String DEFAULT_PK_COLUMN_NAME = "ID_NAME";
    public static final String DEFAULT_VALUE_COLUMN_NAME = "ID_VALUE";
    public static final String DEFAULT_PK_COLUMN_VALUE = "ID_GEN";
    
    //-----------------------------------------------------------------------------------

    private PersistenceFactory _factory;
    private JPATableGeneratorDescriptor _descriptor;
    private int _sqlType;
    
    //-----------------------------------------------------------------------------------

    /**
     * Creates an instance of this key generator.
     * 
     * @param factory The current {@link PersistenceFactory} instance.
     * @param params Parameters for the key generator.
     * @param sqlType The SQL type of the identity field.
     * @throws MappingException
     */
    public TableKeyGenerator(final PersistenceFactory factory, final Properties params,
            final int sqlType) throws MappingException {
        super(factory);
        _factory = factory;
        _sqlType = sqlType;
        _descriptor = (JPATableGeneratorDescriptor) params.get(DESCRIPTOR_KEY);

        initializeDescriptor();

        assertNumericSqlType(sqlType);
    }

    //-----------------------------------------------------------------------------------
    
    private void assertNumericSqlType(final int sqlType) throws MappingException {
        switch (sqlType) {
        case Types.BIGINT:
        case Types.SMALLINT:
        case Types.INTEGER:
        case Types.DECIMAL:
        case Types.NUMERIC:
            break;
        default:
            String msg = Messages.format("mapping.keyGenSQLType", getClass()
                    .getName(), sqlType);
            throw new MappingException(msg);
        }
    }

    private void initializeDescriptor() {
        if (_descriptor == null) {
            _descriptor = new JPATableGeneratorDescriptor();
        }
        if ((_descriptor.getTable() == null) || ("".equals(_descriptor.getTable()))) {
            _descriptor.setTable(DEFAULT_TABLE_NAME);
        }
        if (_descriptor.getAllocationSize() < 1) {
            _descriptor.setAllocationSize(DEFAULT_ALLOCATION_SIZE);
        }
        if (_descriptor.getInitialValue() < 0) {
            _descriptor.setInitialValue(DEFAULT_INITIAL_VALUE);
        }
        if ((_descriptor.getPkColumnName() == null)
                || ("".equals(_descriptor.getPkColumnName()))) {
            _descriptor.setPkColumnName(DEFAULT_PK_COLUMN_NAME);
        }
        if ((_descriptor.getValueColumnName() == null)
                || ("".equals(_descriptor.getValueColumnName()))) {
            _descriptor.setValueColumnName(DEFAULT_VALUE_COLUMN_NAME);
        }
        if ((_descriptor.getPkColumnValue() == null)
                || ("".equals(_descriptor.getPkColumnValue()))) {
            _descriptor.setPkColumnValue(DEFAULT_PK_COLUMN_VALUE);
        }
    }

    public Object generateKey(final Connection connection, final String tableName,
            final String primKeyName) throws PersistenceException {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Generating table-based primary key " + primKeyName
                    + " for table " + tableName);
        }
        String sql = "SELECT " + _descriptor.getValueColumnName() + " FROM "
                + _descriptor.getTable() + " WHERE "
                + _descriptor.getPkColumnName() + "='"
                + _descriptor.getPkColumnValue() + "'";
        Object key;
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet result = statement.executeQuery();
            KeyGeneratorTypeHandler<?> handler = KeyGeneratorTypeHandlerFactory
                    .getTypeHandler(_sqlType, _descriptor.getAllocationSize());
            key = handler.getNextValue(result);
            sql = "UPDATE " + _descriptor.getTable() + " SET "
                    + _descriptor.getValueColumnName() + "="
                    + key + " WHERE "
                    + _descriptor.getPkColumnName() + "='"
                    + _descriptor.getPkColumnValue() + "'";
            statement = connection.prepareStatement(sql);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new PersistenceException(e.getMessage());
        } catch (MappingException e) {
            throw new PersistenceException(e.getMessage());
        }

        return key;
    }

    public boolean isInSameConnection() {
        return true;
    }

    public PersistenceFactory getFactory() {
        return _factory;
    }

    public JPATableGeneratorDescriptor getDescriptor() {
        return _descriptor;
    }

    //-----------------------------------------------------------------------------------
}
