package org.castor.cpa.persistence.sql.keygen;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.Properties;

import org.castor.cpa.persistence.sql.query.PersistenceFactoryMock;
import org.castor.jdo.jpa.info.JPATableGeneratorDescriptor;
import org.exolab.castor.mapping.MappingException;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.MockitoAnnotations;

public class TableKeyGeneratorTest {

    TableKeyGenerator generator;
    PersistenceFactoryMock mockFactory;
    int sqlType;
    @Mock
    Connection connection;
    @Mock
    ResultSet result;
    @Mock
    PreparedStatement statement;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mockFactory = new PersistenceFactoryMock();
        Properties params = new Properties();
        JPATableGeneratorDescriptor descriptor = new JPATableGeneratorDescriptor();
        descriptor.setPrimaryKeyType(Long.class);
        params.put(TableKeyGenerator.DESCRIPTOR_KEY, descriptor);
        sqlType = Types.INTEGER;
        generator = new TableKeyGenerator(mockFactory, params, sqlType);
    }

    @SuppressWarnings("cast")
    @Test
    public void isKeyGenerator() throws Exception {
        assertTrue(generator instanceof KeyGenerator);
    }

    @Test
    public void descriptorWillBeRetrievedFromProperties() throws Exception {
        assertNotNull(generator.getDescriptor());
    }

    @Test
    public void nullDescriptorResultsInDefaultValues() throws Exception {
        Properties params = new Properties();
        generator = new TableKeyGenerator(mockFactory, params, sqlType);
        assertNotNull(generator.getDescriptor());
        assertEquals(TableKeyGenerator.DEFAULT_TABLE_NAME, generator
                .getDescriptor().getTable());
        assertEquals(TableKeyGenerator.DEFAULT_ALLOCATION_SIZE, generator
                .getDescriptor().getAllocationSize());
        assertEquals(TableKeyGenerator.DEFAULT_INITIAL_VALUE, generator
                .getDescriptor().getInitialValue());
        assertEquals(TableKeyGenerator.DEFAULT_PK_COLUMN_NAME, generator
                .getDescriptor().getPkColumnName());
        assertEquals(TableKeyGenerator.DEFAULT_VALUE_COLUMN_NAME, generator
                .getDescriptor().getValueColumnName());
        assertEquals(TableKeyGenerator.DEFAULT_PK_COLUMN_VALUE, generator
                .getDescriptor().getPkColumnValue());
    }
    
    @Test
    public void idWillBeGeneratedFromDefaultValues() throws Exception {
        when(result.next()).thenReturn(true);
        when(result.getObject(1)).thenReturn(2l);
        when(statement.executeQuery()).thenReturn(result);
        when(connection.prepareStatement(anyString())).thenReturn(statement);
        assertEquals(50, generator.generateKey(connection, "tableName", "primKeyName"));
    }

    @Test
    public void nullRetrievedValueWillBeSetToDefaultInitialSize() throws Exception {
        when(result.next()).thenReturn(true);
        when(result.getObject(1)).thenReturn(null);
        when(statement.executeQuery()).thenReturn(result);
        when(connection.prepareStatement(anyString())).thenReturn(statement);
        assertEquals(50, generator.generateKey(connection, "tableName", "primKeyName"));
    }
    
    @Test
    public void correctQueryWillBeExecuted() throws Exception {
        when(result.next()).thenReturn(true);
        when(result.getObject(1)).thenReturn(null);
        when(statement.executeQuery()).thenReturn(result);
        when(connection.prepareStatement(anyString())).thenReturn(statement);
        generator.generateKey(connection, "tableName", "primKeyName");
        verify(connection).prepareStatement("SELECT " + TableKeyGenerator.DEFAULT_VALUE_COLUMN_NAME + " FROM "
                + TableKeyGenerator.DEFAULT_TABLE_NAME + " WHERE "
                + TableKeyGenerator.DEFAULT_PK_COLUMN_NAME + "='"
                + TableKeyGenerator.DEFAULT_PK_COLUMN_VALUE + "'");
    }
    
    @Test
    public void updateWillBeExecutedUponSelect() throws Exception {
        when(result.next()).thenReturn(true);
        when(result.getObject(1)).thenReturn(null);
        when(statement.executeQuery()).thenReturn(result);
        when(connection.prepareStatement(anyString())).thenReturn(statement);
        generator.generateKey(connection, "tableName", "primKeyName");
        verify(connection).prepareStatement(contains("SELECT"));
        verify(connection).prepareStatement("UPDATE " + TableKeyGenerator.DEFAULT_TABLE_NAME + " SET "
                + TableKeyGenerator.DEFAULT_VALUE_COLUMN_NAME + "=" + TableKeyGenerator.DEFAULT_ALLOCATION_SIZE + " WHERE "
                + TableKeyGenerator.DEFAULT_PK_COLUMN_NAME + "='"
                + TableKeyGenerator.DEFAULT_PK_COLUMN_VALUE + "'");
    }
    
    @Test(expected=MappingException.class)
    public void nonNumericSqlTypeCausesMappingException() throws Exception {
        new TableKeyGenerator(mockFactory, new Properties(), Types.CHAR);
    }
}
