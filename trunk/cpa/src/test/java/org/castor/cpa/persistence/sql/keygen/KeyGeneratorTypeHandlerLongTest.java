package org.castor.cpa.persistence.sql.keygen;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.sql.ResultSet;

import org.castor.cpa.persistence.sql.keygen.typehandler.KeyGeneratorTypeHandlerLong;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public final class KeyGeneratorTypeHandlerLongTest {
    private KeyGeneratorTypeHandlerLong _handler;
    @Mock
    private ResultSet _result;
    
    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        _handler = new KeyGeneratorTypeHandlerLong(true);
        when(_result.next()).thenReturn(true);
        when(_result.getLong(1)).thenReturn(new Long(5));
    }
    
    @Test
    public void handlerReturnsActualValue() throws Exception {         
        assertEquals(new Long(5), _handler.getValue(_result));
    }
    
    @Test
    public void handlerReturnsNextValue() throws Exception {
        assertEquals(new Long(6), _handler.getNextValue(_result));
    }
    
    @Test
    public void handlerReturnsNextValueWithAllocationSize() throws Exception {
        _handler = new KeyGeneratorTypeHandlerLong(true, 20);
        assertEquals(new Long(25), _handler.getNextValue(_result));
    }
}
