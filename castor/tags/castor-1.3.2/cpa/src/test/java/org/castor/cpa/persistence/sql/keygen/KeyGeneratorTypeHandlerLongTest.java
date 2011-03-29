package org.castor.cpa.persistence.sql.keygen;

import java.sql.ResultSet;

import org.castor.cpa.persistence.sql.keygen.typehandler.KeyGeneratorTypeHandlerLong;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;

public class KeyGeneratorTypeHandlerLongTest {

    KeyGeneratorTypeHandlerLong handler;
    
    @Mock
    ResultSet result;
    
    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        handler = new KeyGeneratorTypeHandlerLong(true);
        when(result.next()).thenReturn(true);
        when(result.getLong(1)).thenReturn(new Long(5));
    }
    
    @Test
    public void handlerReturnsActualValue() throws Exception {         
        assertEquals(new Long(5), handler.getValue(result));
    }
    
    @Test
    public void handlerReturnsNextValue() throws Exception {
        assertEquals(new Long(6), handler.getNextValue(result));
    }
    
    @Test
    public void handlerReturnsNextValueWithAllocationSize() throws Exception {
        handler = new KeyGeneratorTypeHandlerLong(true, 20);
        assertEquals(new Long(25), handler.getNextValue(result));
    }
}
