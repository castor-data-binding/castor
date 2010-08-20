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
import org.castor.cpa.persistence.sql.keygen.typehandler.KeyGeneratorTypeHandler;
import org.castor.cpa.persistence.sql.keygen.typehandler.KeyGeneratorTypeHandlerFactory;
import org.castor.jdo.jpa.info.JPATableGeneratorDescriptor;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.persist.spi.PersistenceFactory;

public class TableKeyGenerator extends AbstractBeforeKeyGenerator {

    private static final Log LOG = LogFactory.getLog(TableKeyGenerator.class);
    
    public static final String DESCRIPTOR_KEY = "descriptor";
    public static final String DEFAULT_TABLE_NAME = "GENERATOR_TABLE";
    public static final int DEFAULT_ALLOCATION_SIZE = 50;
    public static final int DEFAULT_INITIAL_VALUE = 0;
    public static final String DEFAULT_PK_COLUMN_NAME = "ID_NAME";
    public static final String DEFAULT_VALUE_COLUMN_NAME = "ID_VALUE";
    public static final String DEFAULT_PK_COLUMN_VALUE = "ID_GEN";

    private PersistenceFactory factory;
    private JPATableGeneratorDescriptor descriptor;
    private int sqlType;

    /**
     * Creates an instance of this key generator.
     * 
     * @param factory The current {@link PersistenceFactory} instance.
     * @param params Parameters for the key generator.
     * @param sqlType The SQL type of the identity field.
     * @throws MappingException
     */
    public TableKeyGenerator(PersistenceFactory factory, Properties params,
            int sqlType) throws MappingException {
        super(factory);
        this.factory = factory;
        this.sqlType = sqlType;
        this.descriptor = (JPATableGeneratorDescriptor) params.get(DESCRIPTOR_KEY);

        initializeDescriptor();

        assertNumericSqlType(sqlType);
    }

    private void assertNumericSqlType(int sqlType) throws MappingException {
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
        if (descriptor == null) {
            descriptor = new JPATableGeneratorDescriptor();
        }
        if ((descriptor.getTable() == null) || ("".equals(descriptor.getTable()))) {
            descriptor.setTable(DEFAULT_TABLE_NAME);
        }
        if (descriptor.getAllocationSize() < 1) {
            descriptor.setAllocationSize(DEFAULT_ALLOCATION_SIZE);
        }
        if (descriptor.getInitialValue() < 0) {
            descriptor.setInitialValue(DEFAULT_INITIAL_VALUE);
        }
        if ((descriptor.getPkColumnName() == null)
                || ("".equals(descriptor.getPkColumnName()))) {
            descriptor.setPkColumnName(DEFAULT_PK_COLUMN_NAME);
        }
        if ((descriptor.getValueColumnName() == null)
                || ("".equals(descriptor.getValueColumnName()))) {
            descriptor.setValueColumnName(DEFAULT_VALUE_COLUMN_NAME);
        }
        if ((descriptor.getPkColumnValue() == null)
                || ("".equals(descriptor.getPkColumnValue()))) {
            descriptor.setPkColumnValue(DEFAULT_PK_COLUMN_VALUE);
        }
    }

    public Object generateKey(Connection connection, String tableName,
            String primKeyName) throws PersistenceException {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Generating table-based primary key " + primKeyName
                    + " for table " + tableName);
        }
        String sql = "SELECT " + descriptor.getValueColumnName() + " FROM "
                + descriptor.getTable() + " WHERE "
                + descriptor.getPkColumnName() + "='"
                + descriptor.getPkColumnValue() + "'";
        Object key;
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet result = statement.executeQuery();
            KeyGeneratorTypeHandler<?> handler = KeyGeneratorTypeHandlerFactory
                    .getTypeHandler(sqlType, descriptor.getAllocationSize());
            key = handler.getNextValue(result);
            sql = "UPDATE " + descriptor.getTable() + " SET "
                    + descriptor.getValueColumnName() + "="
                    + key + " WHERE "
                    + descriptor.getPkColumnName() + "='"
                    + descriptor.getPkColumnValue() + "'";
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
        return factory;
    }

    public JPATableGeneratorDescriptor getDescriptor() {
        return descriptor;
    }

}
