package org.castor.cpa.persistence.sql.keygen;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.sql.ResultSet;

import org.castor.cpa.persistence.sql.keygen.typehandler.KeyGeneratorTypeHandlerBigDecimal;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public final class KeyGeneratorTypeHandlerBigDecimalTest {
    private KeyGeneratorTypeHandlerBigDecimal _handler;
    @Mock
    private ResultSet _result;
    
    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        _handler = new KeyGeneratorTypeHandlerBigDecimal(true);
        when(_result.next()).thenReturn(true);
        when(_result.getBigDecimal(1)).thenReturn(new BigDecimal(5));
    }
    
    @Test
    public void handlerReturnsActualValue() throws Exception {         
        assertEquals(new BigDecimal(5), _handler.getValue(_result));
    }
    
    @Test
    public void handlerReturnsNextValue() throws Exception {
        assertEquals(new BigDecimal(6), _handler.getNextValue(_result));
    }
    
    @Test
    public void handlerReturnsNextValueWithAllocationSize() throws Exception {
        _handler = new KeyGeneratorTypeHandlerBigDecimal(true, 20);
        assertEquals(new BigDecimal(25), _handler.getNextValue(_result));
    }
}
