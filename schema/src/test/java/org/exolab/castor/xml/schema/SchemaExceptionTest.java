package org.exolab.castor.xml.schema;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Comprehensive test suite for SchemaException class
 */
public class SchemaExceptionTest {

    @Test
    public void testDefaultConstructor() {
        SchemaException exception = new SchemaException();
        assertNotNull(exception);
        assertNull(exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    public void testConstructorWithMessage() {
        String message = "Test schema exception message";
        SchemaException exception = new SchemaException(message);
        assertNotNull(exception);
        assertEquals(message, exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    public void testConstructorWithNullMessage() {
        SchemaException exception = new SchemaException((String) null);
        assertNotNull(exception);
        assertNull(exception.getMessage());
    }

    @Test
    public void testConstructorWithEmptyMessage() {
        String message = "";
        SchemaException exception = new SchemaException(message);
        assertNotNull(exception);
        assertEquals(message, exception.getMessage());
    }

    @Test
    public void testConstructorWithThrowable() {
        RuntimeException cause = new RuntimeException("Cause exception");
        SchemaException exception = new SchemaException(cause);
        assertNotNull(exception);
        assertEquals(cause, exception.getCause());
    }

    @Test
    public void testConstructorWithNullThrowable() {
        SchemaException exception = new SchemaException((Throwable) null);
        assertNotNull(exception);
        assertNull(exception.getCause());
    }

    @Test
    public void testConstructorWithMessageAndThrowable() {
        String message = "Schema validation error";
        RuntimeException cause = new RuntimeException("Root cause");
        SchemaException exception = new SchemaException(message, cause);
        assertNotNull(exception);
        assertEquals(message, exception.getMessage());
        assertEquals(cause, exception.getCause());
    }

    @Test
    public void testConstructorWithMessageAndNullThrowable() {
        String message = "Test message";
        SchemaException exception = new SchemaException(message, null);
        assertNotNull(exception);
        assertEquals(message, exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    public void testConstructorWithNullMessageAndThrowable() {
        RuntimeException cause = new RuntimeException("Cause");
        SchemaException exception = new SchemaException(null, cause);
        assertNotNull(exception);
        assertEquals(cause, exception.getCause());
    }

    @Test
    public void testExceptionInheritance() {
        SchemaException exception = new SchemaException("Test");
        assertTrue(exception instanceof org.exolab.castor.xml.XMLException);
        assertTrue(exception instanceof Exception);
        assertTrue(exception instanceof Throwable);
    }

    @Test
    public void testThrowAndCatchException() {
        try {
            throw new SchemaException("Schema parsing error");
        } catch (SchemaException e) {
            assertEquals("Schema parsing error", e.getMessage());
        }
    }

    @Test
    public void testThrowDefaultConstructorException() {
        try {
            throw new SchemaException();
        } catch (SchemaException e) {
            assertNull(e.getMessage());
        }
    }

    @Test
    public void testThrowWithCauseException() {
        RuntimeException cause = new RuntimeException("Original error");
        try {
            throw new SchemaException("Wrapped error", cause);
        } catch (SchemaException e) {
            assertEquals("Wrapped error", e.getMessage());
            assertEquals(cause, e.getCause());
            assertEquals("Original error", e.getCause().getMessage());
        }
    }

    @Test
    public void testExceptionWithLongMessage() {
        String longMessage = "This is a very long error message that describes " +
                "a complex schema validation error with multiple details about " +
                "what went wrong during the validation process including " +
                "element names, attribute values, and constraint violations.";
        SchemaException exception = new SchemaException(longMessage);
        assertEquals(longMessage, exception.getMessage());
    }

    @Test
    public void testSerialVersionUID() throws Exception {
        java.lang.reflect.Field field = SchemaException.class.getDeclaredField("serialVersionUID");
        assertNotNull(field);
        assertEquals(long.class, field.getType());
        field.setAccessible(true);
        long serialVersionUID = field.getLong(null);
        assertEquals(7814714272702298809L, serialVersionUID);
    }

    @Test
    public void testExceptionToString() {
        SchemaException exception1 = new SchemaException("Test error");
        String toString = exception1.toString();
        assertNotNull(toString);
        assertTrue(toString.contains("SchemaException"));

        SchemaException exception2 = new SchemaException();
        String toString2 = exception2.toString();
        assertNotNull(toString2);
    }

    @Test
    public void testMultipleInstances() {
        SchemaException exception1 = new SchemaException("Error 1");
        SchemaException exception2 = new SchemaException("Error 2");
        SchemaException exception3 = new SchemaException();

        assertNotNull(exception1);
        assertNotNull(exception2);
        assertNotNull(exception3);

        assertNotEquals(exception1, exception2);
        assertNotEquals(exception1, exception3);

        assertEquals("Error 1", exception1.getMessage());
        assertEquals("Error 2", exception2.getMessage());
        assertNull(exception3.getMessage());
    }

    @Test
    public void testExceptionWithSpecialCharacters() {
        String message = "Schema error: <element> & \"attribute\" at line\n10\ttab\rcarriage";
        SchemaException exception = new SchemaException(message);
        assertEquals(message, exception.getMessage());
    }

    @Test
    public void testExceptionWithUnicodeCharacters() {
        String message = "Schema错误: エラー occurred ошибка";
        SchemaException exception = new SchemaException(message);
        assertEquals(message, exception.getMessage());
    }

    @Test
    public void testNestedExceptionChain() {
        Exception rootCause = new IllegalArgumentException("Root cause");
        RuntimeException middleCause = new RuntimeException("Middle cause", rootCause);
        SchemaException topException = new SchemaException("Top exception", middleCause);

        assertEquals("Top exception", topException.getMessage());
        assertEquals(middleCause, topException.getCause());
        assertEquals(rootCause, topException.getCause().getCause());
    }

    @Test
    public void testExceptionWithIOExceptionCause() {
        java.io.IOException ioCause = new java.io.IOException("File not found");
        SchemaException exception = new SchemaException("Failed to read schema", ioCause);
        assertEquals("Failed to read schema", exception.getMessage());
        assertEquals(ioCause, exception.getCause());
    }

    @Test
    public void testExceptionStackTrace() {
        SchemaException exception = new SchemaException("Test exception");
        StackTraceElement[] stackTrace = exception.getStackTrace();
        assertNotNull(stackTrace);
        assertTrue(stackTrace.length > 0);
    }
}
