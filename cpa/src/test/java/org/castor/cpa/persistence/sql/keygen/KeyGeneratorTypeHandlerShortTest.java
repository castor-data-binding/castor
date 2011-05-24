package org.castor.cpa.persistence.sql.keygen;

import java.sql.ResultSet;

import org.castor.cpa.persistence.sql.keygen.typehandler.KeyGeneratorTypeHandlerShort;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;

public class KeyGeneratorTypeHandlerShortTest {

    KeyGeneratorTypeHandlerShort handler;
    
    @Mock
    ResultSet result;
    
    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        handler = new KeyGeneratorTypeHandlerShort(true);
        when(result.next()).thenReturn(true);
        when(result.getShort(1)).thenReturn((short)5);
    }
    
    @Test
    public void handlerReturnsActualValue() throws Exception {         
        assertEquals(new Short((short)5), handler.getValue(result));
    }
    
    @Test
    public void handlerReturnsNextValue() throws Exception {
        assertEquals(new Short((short)6), handler.getNextValue(result));
    }
    
    @Test
    public void handlerReturnsNextValueWithAllocationSize() throws Exception {
        handler = new KeyGeneratorTypeHandlerShort(true, 20);
        assertEquals(new Short((short)25), handler.getNextValue(result));
    }
}
