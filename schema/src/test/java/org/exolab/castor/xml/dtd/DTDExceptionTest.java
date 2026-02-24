package org.exolab.castor.xml.dtd;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Comprehensive test suite for DTDException class
 */
public class DTDExceptionTest {

    @Test
    public void testDefaultConstructor() {
        DTDException exception = new DTDException();
        assertNotNull(exception);
        assertNull(exception.getMessage());
    }

    @Test
    public void testConstructorWithMessage() {
        String message = "Test DTD exception message";
        DTDException exception = new DTDException(message);
        assertNotNull(exception);
        assertEquals(message, exception.getMessage());
    }

    @Test
    public void testConstructorWithNullMessage() {
        DTDException exception = new DTDException(null);
        assertNotNull(exception);
        assertNull(exception.getMessage());
    }

    @Test
    public void testConstructorWithEmptyMessage() {
        String message = "";
        DTDException exception = new DTDException(message);
        assertNotNull(exception);
        assertEquals(message, exception.getMessage());
    }

    @Test
    public void testExceptionIsThrowable() {
        DTDException exception = new DTDException("Test message");
        assertTrue(exception instanceof Exception);
        assertTrue(exception instanceof Throwable);
    }

    @Test
    public void testThrowAndCatchException() {
        try {
            throw new DTDException("DTD parsing error");
        } catch (DTDException e) {
            assertEquals("DTD parsing error", e.getMessage());
        }
    }

    @Test
    public void testThrowDefaultConstructorException() {
        try {
            throw new DTDException();
        } catch (DTDException e) {
            assertNull(e.getMessage());
        }
    }

    @Test
    public void testExceptionWithLongMessage() {
        String longMessage = "This is a very long error message that describes " +
                "a complex DTD parsing error with multiple details about " +
                "what went wrong during the parsing process including " +
                "line numbers, column positions, and semantic validation issues.";
        DTDException exception = new DTDException(longMessage);
        assertEquals(longMessage, exception.getMessage());
    }

    @Test
    public void testSerialVersionUID() throws Exception {
        // Test that the class has a serialVersionUID field
        java.lang.reflect.Field field = DTDException.class.getDeclaredField("serialVersionUID");
        assertNotNull(field);
        assertEquals(long.class, field.getType());
        field.setAccessible(true);
        long serialVersionUID = field.getLong(null);
        assertEquals(4760130120041855808L, serialVersionUID);
    }

    @Test
    public void testExceptionToString() {
        DTDException exception1 = new DTDException("Test error");
        String toString = exception1.toString();
        assertNotNull(toString);
        assertTrue(toString.contains("DTDException"));
        assertTrue(toString.contains("Test error"));

        DTDException exception2 = new DTDException();
        String toString2 = exception2.toString();
        assertNotNull(toString2);
        assertTrue(toString2.contains("DTDException"));
    }

    @Test
    public void testMultipleInstances() {
        DTDException exception1 = new DTDException("Error 1");
        DTDException exception2 = new DTDException("Error 2");
        DTDException exception3 = new DTDException();

        assertNotNull(exception1);
        assertNotNull(exception2);
        assertNotNull(exception3);

        assertNotEquals(exception1, exception2);
        assertNotEquals(exception1, exception3);
        assertNotEquals(exception2, exception3);

        assertEquals("Error 1", exception1.getMessage());
        assertEquals("Error 2", exception2.getMessage());
        assertNull(exception3.getMessage());
    }

    @Test
    public void testExceptionWithSpecialCharacters() {
        String message = "DTD error: <element> & \"attribute\" at line\n10\ttab\rcarriage";
        DTDException exception = new DTDException(message);
        assertEquals(message, exception.getMessage());
    }

    @Test
    public void testExceptionWithUnicodeCharacters() {
        String message = "DTD错误: エラー occurred ошибка";
        DTDException exception = new DTDException(message);
        assertEquals(message, exception.getMessage());
    }
}
