package org.castor.xmlctf.compiler;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import org.junit.Test;

/**
 * Test suite for CompilationException class.
 */
public class CompilationExceptionTest {

    // ===== Constructor tests =====

    @Test
    public void testCompilationExceptionNoArg() {
        CompilationException ex = new CompilationException();
        assertNotNull(ex);
        assertNull(ex.getCause());
    }

    @Test
    public void testCompilationExceptionWithMessage() {
        String message = "Test compilation error";
        CompilationException ex = new CompilationException(message);
        assertEquals(message, ex.getMessage());
        assertNull(ex.getCause());
    }

    @Test
    public void testCompilationExceptionWithCause() {
        Throwable cause = new RuntimeException("Root cause");
        CompilationException ex = new CompilationException(cause);
        assertEquals(cause, ex.getCause());
    }

    @Test
    public void testCompilationExceptionWithMessageAndCause() {
        String message = "Compilation failed";
        Throwable cause = new IOException("File not found");
        CompilationException ex = new CompilationException(message, cause);
        assertEquals(message, ex.getMessage());
        assertEquals(cause, ex.getCause());
    }

    // ===== getCause() tests =====

    @Test
    public void testGetCauseReturnsNull() {
        CompilationException ex = new CompilationException();
        assertNull(ex.getCause());
    }

    @Test
    public void testGetCauseReturnsProvidedCause() {
        Throwable cause = new Exception("Original error");
        CompilationException ex = new CompilationException(cause);
        assertEquals(cause, ex.getCause());
    }

    @Test
    public void testGetCauseWithNestedCause() {
        Throwable rootCause = new Exception("Root");
        Throwable cause = new RuntimeException("Wrapped", rootCause);
        CompilationException ex = new CompilationException(cause);
        assertEquals(cause, ex.getCause());
    }

    // ===== printStackTrace() tests =====

    @Test
    public void testPrintStackTraceNoArg() {
        CompilationException ex = new CompilationException("Test error");
        // Capture stderr to avoid polluting test output
        PrintStream oldErr = System.err;
        try {
            System.setErr(new PrintStream(new ByteArrayOutputStream()));
            ex.printStackTrace();
        } finally {
            System.setErr(oldErr);
        }
    }

    @Test
    public void testPrintStackTraceWithPrintStream() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);

        CompilationException ex = new CompilationException("Test error");
        ex.printStackTrace(ps);

        String output = baos.toString();
        assertTrue(output.contains("CompilationException"));
        assertTrue(output.contains("Test error"));
    }

    @Test
    public void testPrintStackTraceWithPrintStreamAndCause() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);

        Throwable cause = new RuntimeException("Underlying cause");
        CompilationException ex = new CompilationException("Test error", cause);
        ex.printStackTrace(ps);

        String output = baos.toString();
        assertTrue(output.contains("CompilationException"));
        assertTrue(output.contains("Test error"));
        assertTrue(output.contains("Caused by"));
        assertTrue(output.contains("Underlying cause"));
    }

    @Test
    public void testPrintStackTraceWithPrintWriter() {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);

        CompilationException ex = new CompilationException("Test error");
        ex.printStackTrace(pw);

        String output = sw.toString();
        assertTrue(output.contains("CompilationException"));
        assertTrue(output.contains("Test error"));
    }

    @Test
    public void testPrintStackTraceWithPrintWriterAndCause() {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);

        Throwable cause = new RuntimeException("Underlying cause");
        CompilationException ex = new CompilationException("Test error", cause);
        ex.printStackTrace(pw);

        String output = sw.toString();
        assertTrue(output.contains("CompilationException"));
        assertTrue(output.contains("Test error"));
        assertTrue(output.contains("Caused by"));
        assertTrue(output.contains("Underlying cause"));
    }

    // ===== Inheritance tests =====

    @Test
    public void testCompilationExceptionIsRuntimeException() {
        CompilationException ex = new CompilationException("Test");
        assertTrue(ex instanceof RuntimeException);
    }

    @Test
    public void testCompilationExceptionIsThrowable() {
        CompilationException ex = new CompilationException("Test");
        assertTrue(ex instanceof Throwable);
    }

    // ===== Message tests =====

    @Test
    public void testMessagePropagation() {
        String message = "Compilation failed at line 42";
        CompilationException ex = new CompilationException(message);
        assertEquals(message, ex.getMessage());
    }

    @Test
    public void testMessageWithSpecialCharacters() {
        String message = "Error: 'syntax error' at position [10]";
        CompilationException ex = new CompilationException(message);
        assertEquals(message, ex.getMessage());
    }

    @Test
    public void testNullMessage() {
        CompilationException ex = new CompilationException((String) null);
        assertNull(ex.getMessage());
    }

    // ===== Cause chain tests =====

    @Test
    public void testCauseChain() {
        Throwable root = new Exception("Root cause");
        Throwable middle = new RuntimeException("Middle cause", root);
        CompilationException top = new CompilationException(
            "Top cause",
            middle
        );

        assertEquals(middle, top.getCause());
        assertEquals(root, middle.getCause());
    }

    @Test
    public void testCauseReplacement() {
        Throwable cause1 = new Exception("First cause");
        CompilationException ex = new CompilationException(cause1);
        assertEquals(cause1, ex.getCause());

        // Create new exception with different cause
        Throwable cause2 = new RuntimeException("Second cause");
        CompilationException ex2 = new CompilationException(cause2);
        assertEquals(cause2, ex2.getCause());
        assertNotEquals(cause1, ex2.getCause());
    }

    // ===== Exception chaining tests =====

    @Test
    public void testThrowAndCatchCompilationException() {
        try {
            throw new CompilationException("Test compilation error");
        } catch (CompilationException ex) {
            assertEquals("Test compilation error", ex.getMessage());
            assertNull(ex.getCause());
        }
    }

    @Test
    public void testThrowAndCatchWithCause() {
        try {
            try {
                throw new IOException("IO problem");
            } catch (IOException e) {
                throw new CompilationException("Compilation failed", e);
            }
        } catch (CompilationException ex) {
            assertEquals("Compilation failed", ex.getMessage());
            assertTrue(ex.getCause() instanceof IOException);
            assertEquals("IO problem", ex.getCause().getMessage());
        }
    }

    // ===== Multiple printStackTrace calls =====

    @Test
    public void testMultiplePrintStackTraceCalls() {
        CompilationException ex = new CompilationException(
            "Test",
            new Exception("Cause")
        );

        StringWriter sw1 = new StringWriter();
        PrintWriter pw1 = new PrintWriter(sw1);
        ex.printStackTrace(pw1);

        StringWriter sw2 = new StringWriter();
        PrintWriter pw2 = new PrintWriter(sw2);
        ex.printStackTrace(pw2);

        // Both outputs should contain the same information
        String output1 = sw1.toString();
        String output2 = sw2.toString();
        assertTrue(output1.contains("CompilationException"));
        assertTrue(output2.contains("CompilationException"));
    }

    // ===== Edge cases =====

    @Test
    public void testCompilationExceptionWithEmptyMessage() {
        CompilationException ex = new CompilationException("");
        assertEquals("", ex.getMessage());
    }

    @Test
    public void testCompilationExceptionWithLongMessage() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 1000; i++) {
            sb.append("A");
        }
        String longMessage = sb.toString();
        CompilationException ex = new CompilationException(longMessage);
        assertEquals(longMessage, ex.getMessage());
    }

    @Test
    public void testPrintStackTraceWithDifferentExceptionTypes() {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);

        Throwable[] causes = {
            new IOException("IO Error"),
            new ClassNotFoundException("Class not found"),
            new SecurityException("Security violation"),
            new UnsupportedOperationException("Operation not supported"),
        };

        for (Throwable cause : causes) {
            StringWriter swLocal = new StringWriter();
            PrintWriter pwLocal = new PrintWriter(swLocal);
            CompilationException ex = new CompilationException(
                "Error with " + cause.getClass().getSimpleName(),
                cause
            );
            ex.printStackTrace(pwLocal);
            String output = swLocal.toString();
            assertTrue(output.contains("Caused by"));
        }
    }

    @Test
    public void testExceptionStackTraceContainsClassNameAndMessage() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);

        CompilationException ex = new CompilationException(
            "Syntax error in source"
        );
        ex.printStackTrace(ps);

        String output = baos.toString();
        assertTrue(
            output.contains("org.castor.xmlctf.compiler.CompilationException")
        );
    }

    @Test
    public void testCauseWithoutMessage() {
        Throwable cause = new RuntimeException();
        CompilationException ex = new CompilationException(
            "Top level error",
            cause
        );
        assertNotNull(ex.getCause());
        assertEquals("Top level error", ex.getMessage());
    }

    @Test
    public void testMessageAndCauseIndependence() {
        String message = "Compilation failed";
        Throwable cause = new Exception("Parse error");
        CompilationException ex = new CompilationException(message, cause);

        // Verify they are independent
        assertEquals(message, ex.getMessage());
        assertEquals(cause, ex.getCause());
        assertNotEquals(message, cause.getMessage());
    }
}
