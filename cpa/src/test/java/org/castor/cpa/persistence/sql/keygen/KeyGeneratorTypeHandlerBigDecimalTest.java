package org.castor.cpa.persistence.sql.keygen;

import java.math.BigDecimal;
import java.sql.ResultSet;

import org.castor.cpa.persistence.sql.keygen.typehandler.KeyGeneratorTypeHandlerBigDecimal;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;

public class KeyGeneratorTypeHandlerBigDecimalTest {
    
    KeyGeneratorTypeHandlerBigDecimal handler;
    
    @Mock
    ResultSet result;
    
    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        handler = new KeyGeneratorTypeHandlerBigDecimal(true);
        when(result.next()).thenReturn(true);
        when(result.getBigDecimal(1)).thenReturn(new BigDecimal(5));
    }
    
    @Test
    public void handlerReturnsActualValue() throws Exception {         
        assertEquals(new BigDecimal(5), handler.getValue(result));
    }
    
    @Test
    public void handlerReturnsNextValue() throws Exception {
        assertEquals(new BigDecimal(6), handler.getNextValue(result));
    }
    
    @Test
    public void handlerReturnsNextValueWithAllocationSize() throws Exception {
        handler = new KeyGeneratorTypeHandlerBigDecimal(true, 20);
        assertEquals(new BigDecimal(25), handler.getNextValue(result));
    }
}
