package org.castor.cpa.persistence.sql.keygen;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.sql.ResultSet;

import org.castor.cpa.persistence.sql.keygen.typehandler.KeyGeneratorTypeHandlerShort;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public final class KeyGeneratorTypeHandlerShortTest {
    private KeyGeneratorTypeHandlerShort _handler;
    @Mock
    private ResultSet _result;
    
    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        _handler = new KeyGeneratorTypeHandlerShort(true);
        when(_result.next()).thenReturn(true);
        when(_result.getShort(1)).thenReturn((short) 5);
    }
    
    @Test
    public void handlerReturnsActualValue() throws Exception {         
        assertEquals(new Short((short) 5), _handler.getValue(_result));
    }
    
    @Test
    public void handlerReturnsNextValue() throws Exception {
        assertEquals(new Short((short) 6), _handler.getNextValue(_result));
    }
    
    @Test
    public void handlerReturnsNextValueWithAllocationSize() throws Exception {
        _handler = new KeyGeneratorTypeHandlerShort(true, 20);
        assertEquals(new Short((short) 25), _handler.getNextValue(_result));
    }
}
