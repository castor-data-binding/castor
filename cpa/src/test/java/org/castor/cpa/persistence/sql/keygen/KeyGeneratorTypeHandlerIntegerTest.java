package org.castor.cpa.persistence.sql.keygen;

import java.sql.ResultSet;

import org.castor.cpa.persistence.sql.keygen.typehandler.KeyGeneratorTypeHandlerInteger;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;

public class KeyGeneratorTypeHandlerIntegerTest {

    KeyGeneratorTypeHandlerInteger handler;
    
    @Mock
    ResultSet result;
    
    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        handler = new KeyGeneratorTypeHandlerInteger(true);
        when(result.next()).thenReturn(true);
        when(result.getInt(1)).thenReturn(new Integer(5));
    }
    
    @Test
    public void handlerReturnsActualValue() throws Exception {         
        assertEquals(new Integer(5), handler.getValue(result));
    }
    
    @Test
    public void handlerReturnsNextValue() throws Exception {
        assertEquals(new Integer(6), handler.getNextValue(result));
    }
    
    @Test
    public void handlerReturnsNextValueWithAllocationSize() throws Exception {
        handler = new KeyGeneratorTypeHandlerInteger(true, 20);
        assertEquals(new Integer(25), handler.getNextValue(result));
    }
}
