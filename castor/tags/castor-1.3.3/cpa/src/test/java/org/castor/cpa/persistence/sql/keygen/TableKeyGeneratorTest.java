package org.castor.cpa.persistence.sql.keygen;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.contains;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.Properties;

import org.castor.cpa.jpa.info.JPATableGeneratorDescriptor;
import org.exolab.castor.mapping.MappingException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public final class TableKeyGeneratorTest {
    private TableKeyGenerator _generator;
    private int _sqlType;
    @Mock
    private Connection _connection;
    @Mock
    private ResultSet _result;
    @Mock
    private PreparedStatement _statement;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        Properties params = new Properties();
        JPATableGeneratorDescriptor descriptor = new JPATableGeneratorDescriptor();
        descriptor.setPrimaryKeyType(Long.class);
        params.put(TableKeyGenerator.DESCRIPTOR_KEY, descriptor);
        _sqlType = Types.INTEGER;
        _generator = new TableKeyGenerator(null, params, _sqlType);
    }

    @SuppressWarnings("cast")
    @Test
    public void isKeyGenerator() throws Exception {
        assertTrue(_generator instanceof KeyGenerator);
    }

    @Test
    public void descriptorWillBeRetrievedFromProperties() throws Exception {
        assertNotNull(_generator.getDescriptor());
    }

    @Test
    public void nullDescriptorResultsInDefaultValues() throws Exception {
        Properties params = new Properties();
        _generator = new TableKeyGenerator(null, params, _sqlType);
        assertNotNull(_generator.getDescriptor());
        assertEquals(TableKeyGenerator.DEFAULT_TABLE_NAME, _generator
                .getDescriptor().getTable());
        assertEquals(TableKeyGenerator.DEFAULT_ALLOCATION_SIZE, _generator
                .getDescriptor().getAllocationSize());
        assertEquals(TableKeyGenerator.DEFAULT_INITIAL_VALUE, _generator
                .getDescriptor().getInitialValue());
        assertEquals(TableKeyGenerator.DEFAULT_PK_COLUMN_NAME, _generator
                .getDescriptor().getPkColumnName());
        assertEquals(TableKeyGenerator.DEFAULT_VALUE_COLUMN_NAME, _generator
                .getDescriptor().getValueColumnName());
        assertEquals(TableKeyGenerator.DEFAULT_PK_COLUMN_VALUE, _generator
                .getDescriptor().getPkColumnValue());
    }
    
    @Test
    public void idWillBeGeneratedFromDefaultValues() throws Exception {
        when(_result.next()).thenReturn(true);
        when(_result.getObject(1)).thenReturn(2L);
        when(_statement.executeQuery()).thenReturn(_result);
        when(_connection.prepareStatement(anyString())).thenReturn(_statement);
        assertEquals(50, _generator.generateKey(_connection, "tableName", "primKeyName"));
    }

    @Test
    public void nullRetrievedValueWillBeSetToDefaultInitialSize() throws Exception {
        when(_result.next()).thenReturn(true);
        when(_result.getObject(1)).thenReturn(null);
        when(_statement.executeQuery()).thenReturn(_result);
        when(_connection.prepareStatement(anyString())).thenReturn(_statement);
        assertEquals(50, _generator.generateKey(_connection, "tableName", "primKeyName"));
    }
    
    @Test
    public void correctQueryWillBeExecuted() throws Exception {
        when(_result.next()).thenReturn(true);
        when(_result.getObject(1)).thenReturn(null);
        when(_statement.executeQuery()).thenReturn(_result);
        when(_connection.prepareStatement(anyString())).thenReturn(_statement);
        _generator.generateKey(_connection, "tableName", "primKeyName");
        verify(_connection).prepareStatement(
                "SELECT " + TableKeyGenerator.DEFAULT_VALUE_COLUMN_NAME + " FROM "
                + TableKeyGenerator.DEFAULT_TABLE_NAME + " WHERE "
                + TableKeyGenerator.DEFAULT_PK_COLUMN_NAME + "='"
                + TableKeyGenerator.DEFAULT_PK_COLUMN_VALUE + "'");
    }
    
    @Test
    public void updateWillBeExecutedUponSelect() throws Exception {
        when(_result.next()).thenReturn(true);
        when(_result.getObject(1)).thenReturn(null);
        when(_statement.executeQuery()).thenReturn(_result);
        when(_connection.prepareStatement(anyString())).thenReturn(_statement);
        _generator.generateKey(_connection, "tableName", "primKeyName");
        verify(_connection).prepareStatement(contains("SELECT"));
        verify(_connection).prepareStatement(
                "UPDATE " + TableKeyGenerator.DEFAULT_TABLE_NAME + " SET "
                + TableKeyGenerator.DEFAULT_VALUE_COLUMN_NAME + "="
                + TableKeyGenerator.DEFAULT_ALLOCATION_SIZE + " WHERE "
                + TableKeyGenerator.DEFAULT_PK_COLUMN_NAME + "='"
                + TableKeyGenerator.DEFAULT_PK_COLUMN_VALUE + "'");
    }
    
    @Test(expected = MappingException.class)
    public void nonNumericSqlTypeCausesMappingException() throws Exception {
        new TableKeyGenerator(null, new Properties(), Types.CHAR);
    }
}
