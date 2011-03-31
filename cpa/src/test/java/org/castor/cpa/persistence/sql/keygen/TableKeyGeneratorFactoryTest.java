package org.castor.cpa.persistence.sql.keygen;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.sql.Types;
import java.util.Properties;

import org.castor.cpa.persistence.sql.query.PersistenceFactoryMock;
import org.junit.Before;
import org.junit.Test;

public final class TableKeyGeneratorFactoryTest {
    private TableKeyGeneratorFactory _factory;
    
    @Before
    public void setUp() throws Exception {
        _factory = new TableKeyGeneratorFactory();
    }
    
    @SuppressWarnings("cast")
    @Test
    public void isKeyGeneratorFactory() throws Exception {
        assertTrue(_factory instanceof KeyGeneratorFactory); 
    }
    
    @Test
    public void hasNameTable() throws Exception {
        assertEquals(TableKeyGeneratorFactory.NAME, _factory.getKeyGeneratorName());
    }
    
    @Test
    public void returnsTableKeyGenerator() throws Exception {
        PersistenceFactoryMock mockFactory = new PersistenceFactoryMock();
        Properties params = new Properties();
        int sqlType = Types.BIGINT;
        assertNotNull(_factory.getKeyGenerator(mockFactory, params, sqlType));
    }
}
