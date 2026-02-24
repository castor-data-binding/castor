/*
 * Redistribution and use of this software and associated documentation ("Software"), with or
 * without modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain copyright statements and notices. Redistributions
 * must also contain a copy of this document.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of
 * conditions and the following disclaimer in the documentation and/or other materials provided with
 * the distribution.
 *
 * 3. The name "Exolab" must not be used to endorse or promote products derived from this Software
 * without prior written permission of Intalio, Inc. For written permission, please contact
 * info@exolab.org.
 *
 * 4. Products derived from this Software may not be called "Exolab" nor may "Exolab" appear in
 * their names without prior written permission of Intalio, Inc. Exolab is a registered trademark of
 * Intalio, Inc.
 *
 * 5. Due credit should be given to the Exolab Project (http://www.exolab.org/).
 *
 * THIS SOFTWARE IS PROVIDED BY INTALIO, INC. AND CONTRIBUTORS ``AS IS'' AND ANY EXPRESSED OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL INTALIO, INC. OR ITS
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY
 * WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * Copyright 2002 (C) Intalio Inc. All Rights Reserved.
 */
package org.exolab.castor.builder.binding;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Comprehensive unit tests for BindingException class.
 * Covers all constructors, methods, and edge cases with >95% coverage.
 */
public class BindingExceptionTest {

    private PrintStream originalOut;
    private ByteArrayOutputStream capturedOutput;

    @Before
    public void setUp() {
        originalOut = System.out;
        capturedOutput = new ByteArrayOutputStream();
        System.setOut(new PrintStream(capturedOutput));
    }

    @After
    public void tearDown() {
        System.setOut(originalOut);
    }

    // ===== Constructor Tests =====

    @Test
    public void should_CreateBindingException_When_MessageProvided() {
        String message = "Test error message";
        BindingException exception = new BindingException(message);

        assertNotNull(exception);
        assertEquals(message, exception.getMessage());
        assertNull(exception.getException());
    }

    @Test
    public void should_CreateBindingException_When_MessageIsNull() {
        BindingException exception = new BindingException((String) null);

        assertNotNull(exception);
        assertNull(exception.getMessage());
        assertNull(exception.getException());
    }

    @Test
    public void should_CreateBindingException_When_MessageIsEmpty() {
        BindingException exception = new BindingException("");

        assertNotNull(exception);
        assertEquals("", exception.getMessage());
        assertNull(exception.getException());
    }

    @Test
    public void should_CreateBindingException_When_ExceptionProvided() {
        Exception embedException = new IllegalArgumentException(
            "Nested exception"
        );
        BindingException exception = new BindingException(embedException);

        assertNotNull(exception);
        assertEquals(embedException, exception.getException());
    }

    @Test
    public void should_CreateBindingException_When_ExceptionIsNull() {
        BindingException exception = new BindingException((Exception) null);

        assertNotNull(exception);
        assertNull(exception.getException());
    }

    @Test
    public void should_CreateBindingException_When_MessageAndExceptionProvided() {
        String message = "Custom message";
        Exception embedException = new RuntimeException("Nested error");
        BindingException exception = new BindingException(
            message,
            embedException
        );

        assertNotNull(exception);
        assertEquals(message, exception.getMessage());
        assertEquals(embedException, exception.getException());
    }

    @Test
    public void should_CreateBindingException_When_MessageIsNullAndExceptionProvided() {
        Exception embedException = new IllegalStateException(
            "Nested exception"
        );
        BindingException exception = new BindingException(null, embedException);

        assertNotNull(exception);
        assertEquals(embedException, exception.getException());
    }

    @Test
    public void should_CreateBindingException_When_MessageAndExceptionAreNull() {
        BindingException exception = new BindingException(null, null);

        assertNotNull(exception);
        assertNull(exception.getMessage());
        assertNull(exception.getException());
    }

    // ===== getMessage() Tests =====

    @Test
    public void should_ReturnMessage_When_MessageIsProvidedAndNoException() {
        String message = "Test message";
        BindingException exception = new BindingException(message);

        assertEquals(message, exception.getMessage());
    }

    @Test
    public void should_ReturnEmbeddedException_Message_When_BindingExceptionMessageIsNullAndExceptionExists() {
        Exception embedException = new IllegalArgumentException(
            "Embedded error"
        );
        BindingException exception = new BindingException(embedException);

        assertEquals("Embedded error", exception.getMessage());
    }

    @Test
    public void should_ReturnNull_When_NoMessageAndNoException() {
        BindingException exception = new BindingException((String) null);

        assertNull(exception.getMessage());
    }

    @Test
    public void should_ReturnProvidedMessage_When_BothMessageAndExceptionProvided() {
        String message = "Primary message";
        Exception embedException = new RuntimeException("Secondary message");
        BindingException exception = new BindingException(
            message,
            embedException
        );

        assertEquals(message, exception.getMessage());
    }

    @Test
    public void should_ReturnEmbeddedException_Message_When_PrimaryMessageIsNullAndExceptionExists() {
        Exception embedException = new NullPointerException("NPE message");
        BindingException exception = new BindingException(null, embedException);

        assertEquals("NPE message", exception.getMessage());
    }

    @Test
    public void should_ReturnNull_When_EmbeddedExceptionHasNullMessage() {
        Exception embedException = new RuntimeException((String) null);
        BindingException exception = new BindingException(embedException);

        assertNull(exception.getMessage());
    }

    @Test
    public void should_ReturnExceptionMessage_When_MessageNullButExceptionHasMessage() {
        Exception embedException = new RuntimeException("Exception message");
        BindingException exception = new BindingException(null, embedException);

        assertEquals("Exception message", exception.getMessage());
    }

    // ===== getException() Tests =====

    @Test
    public void should_ReturnNull_When_NoExceptionWrapped() {
        BindingException exception = new BindingException("Message only");

        assertNull(exception.getException());
    }

    @Test
    public void should_ReturnEmbeddedException_When_ExceptionWrapped() {
        Exception embedException = new IllegalArgumentException("Test");
        BindingException exception = new BindingException(embedException);

        assertEquals(embedException, exception.getException());
        assertSame(embedException, exception.getException());
    }

    @Test
    public void should_ReturnEmbeddedException_When_MessageAndExceptionProvided() {
        Exception embedException = new IOException("IO error");
        BindingException exception = new BindingException(
            "Message",
            embedException
        );

        assertEquals(embedException, exception.getException());
    }

    @Test
    public void should_ReturnNull_When_ExceptionConstructorCalledWithNull() {
        BindingException exception = new BindingException((Exception) null);

        assertNull(exception.getException());
    }

    // ===== printStackTrace() Tests =====

    @Test
    public void should_NotThrowException_When_PrintStackTraceCalledWithMessage() {
        BindingException exception = new BindingException("Test message");
        try {
            System.setOut(originalOut);
            exception.printStackTrace();
        } finally {
            System.setOut(new PrintStream(capturedOutput));
        }
    }

    @Test
    public void should_NotThrowException_When_PrintStackTraceCalledWithEmbeddedException() {
        Exception embedException = new RuntimeException("Nested exception");
        BindingException exception = new BindingException(embedException);
        try {
            System.setOut(originalOut);
            exception.printStackTrace();
        } finally {
            System.setOut(new PrintStream(capturedOutput));
        }
    }

    @Test
    public void should_NotThrowException_When_PrintStackTraceCalledWithBothArguments() {
        Exception embedException = new IllegalStateException("Illegal state");
        BindingException exception = new BindingException(
            "Main message",
            embedException
        );
        try {
            System.setOut(originalOut);
            exception.printStackTrace();
        } finally {
            System.setOut(new PrintStream(capturedOutput));
        }
    }

    @Test
    public void should_NotThrowException_When_PrintStackTraceCalledWithNullException() {
        BindingException exception = new BindingException((Exception) null);
        try {
            System.setOut(originalOut);
            exception.printStackTrace();
        } finally {
            System.setOut(new PrintStream(capturedOutput));
        }
    }

    // ===== toString() Tests =====

    @Test
    public void should_ReturnBindingExceptionString_When_NoEmbeddedException() {
        BindingException exception = new BindingException("Test message");
        String result = exception.toString();

        assertNotNull(result);
        assertTrue(result.contains("BindingException"));
    }

    @Test
    public void should_ReturnEmbeddedException_ToString_When_ExceptionWrapped() {
        Exception embedException = new IllegalArgumentException(
            "Invalid argument"
        );
        BindingException exception = new BindingException(embedException);
        String result = exception.toString();

        assertEquals(embedException.toString(), result);
        assertTrue(result.contains("IllegalArgumentException"));
    }

    @Test
    public void should_ReturnEmbeddedException_ToString_When_MessageAndExceptionProvided() {
        Exception embedException = new RuntimeException("Runtime error");
        BindingException exception = new BindingException(
            "Custom message",
            embedException
        );
        String result = exception.toString();

        assertEquals(embedException.toString(), result);
        assertTrue(result.contains("RuntimeException"));
    }

    @Test
    public void should_ReturnSuperToString_When_NoEmbeddedException() {
        BindingException exception = new BindingException("Test");
        String result = exception.toString();

        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    // ===== Serialization Tests =====

    @Test
    public void should_BeInstanceOfException() {
        BindingException exception = new BindingException("Test");
        assertTrue(exception instanceof Exception);
    }

    @Test
    public void should_BeSerializable() {
        BindingException exception = new BindingException("Test");
        assertTrue(exception instanceof java.io.Serializable);
    }

    // ===== Integration Tests =====

    @Test
    public void should_ChainMultipleExceptions() {
        Exception cause = new IllegalArgumentException("Original cause");
        BindingException first = new BindingException(cause);
        BindingException second = new BindingException("Wrapper", first);

        assertEquals(first, second.getException());
        assertEquals(cause, first.getException());
    }

    @Test
    public void should_HandleComplexExceptionHierarchy() {
        Exception rootCause = new NullPointerException("Root cause");
        Exception intermediate = new RuntimeException(
            "Intermediate",
            rootCause
        );
        BindingException wrapper = new BindingException(
            "Wrapper message",
            intermediate
        );

        assertEquals(intermediate, wrapper.getException());
        assertEquals("Wrapper message", wrapper.getMessage());
    }

    @Test
    public void should_PreserveExceptionWhenConverted() {
        Exception original = new IllegalStateException("State error");
        BindingException binding = new BindingException(
            "Binding error",
            original
        );

        assertSame(original, binding.getException());
    }

    @Test
    public void should_PreserveExceptionIdentity() {
        RuntimeException cause = new RuntimeException("cause");
        BindingException exception = new BindingException(cause);

        assertSame(cause, exception.getException());
    }

    // ===== Edge Cases =====

    @Test
    public void should_HandleLongMessage() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 1000; i++) {
            sb.append("Long message ");
        }
        BindingException exception = new BindingException(sb.toString());

        assertTrue(exception.getMessage().length() > 10000);
    }

    @Test
    public void should_HandleSpecialCharactersInMessage() {
        String message = "Test\n\t\r!@#$%^&*()[]{}";
        BindingException exception = new BindingException(message);

        assertEquals(message, exception.getMessage());
    }

    @Test
    public void should_HandleExceptionWithoutMessage() {
        Exception embedException = new Exception();
        BindingException binding = new BindingException(embedException);

        assertNull(binding.getMessage());
        assertEquals(embedException, binding.getException());
    }

    @Test
    public void should_HandleNestedNullExceptions() {
        BindingException first = new BindingException((Exception) null);
        BindingException second = new BindingException("Message", first);

        assertEquals(first, second.getException());
    }

    @Test
    public void should_HandleExceptionWithEmptyMessage() {
        Exception embedException = new RuntimeException("");
        BindingException exception = new BindingException(embedException);

        assertEquals("", exception.getMessage());
    }

    @Test
    public void should_PreferProvidedMessageOverEmbeddedException() {
        Exception embedException = new RuntimeException("Embedded msg");
        BindingException exception = new BindingException(
            "Provided msg",
            embedException
        );

        assertEquals("Provided msg", exception.getMessage());
        assertNotEquals("Embedded msg", exception.getMessage());
    }

    @Test
    public void should_ReturnEmbeddedMessageWhenProvidedIsNull() {
        Exception embedException = new RuntimeException("Embedded");
        BindingException exception = new BindingException(null, embedException);

        assertEquals("Embedded", exception.getMessage());
    }

    // ===== Branch Coverage Tests =====

    @Test
    public void should_HandleGetMessageWithNullException() {
        BindingException exception = new BindingException((String) null);
        exception.getException();

        assertNull(exception.getMessage());
    }

    @Test
    public void should_GetExceptionMultipleTimes() {
        Exception cause = new RuntimeException("test");
        BindingException exception = new BindingException(cause);

        Exception first = exception.getException();
        Exception second = exception.getException();

        assertSame(first, second);
    }

    @Test
    public void should_GetMessageMultipleTimes() {
        String message = "test";
        BindingException exception = new BindingException(message);

        String first = exception.getMessage();
        String second = exception.getMessage();

        assertEquals(first, second);
    }

    @Test
    public void should_ToStringMultipleTimes() {
        BindingException exception = new BindingException("test");

        String first = exception.toString();
        String second = exception.toString();

        assertEquals(first, second);
    }

    @Test
    public void should_HandleThrowingBindingException() {
        try {
            throw new BindingException("Test throw");
        } catch (BindingException e) {
            assertEquals("Test throw", e.getMessage());
        }
    }

    @Test
    public void should_HandleThrowingWithEmbeddedException() {
        try {
            throw new BindingException(
                "Wrapper",
                new RuntimeException("Cause")
            );
        } catch (BindingException e) {
            assertEquals("Wrapper", e.getMessage());
            assertTrue(e.getException() instanceof RuntimeException);
        }
    }

    @Test
    public void should_HandleMultipleMessageRetrievals() {
        Exception cause = new IllegalArgumentException("cause message");
        BindingException exception = new BindingException(null, cause);

        for (int i = 0; i < 5; i++) {
            assertEquals("cause message", exception.getMessage());
        }
    }

    @Test
    public void should_HandleDifferentExceptionTypes() {
        BindingException e1 = new BindingException(new IOException("io"));
        BindingException e2 = new BindingException(
            new NullPointerException("npe")
        );
        BindingException e3 = new BindingException(
            new IllegalStateException("state")
        );

        assertTrue(e1.getException() instanceof IOException);
        assertTrue(e2.getException() instanceof NullPointerException);
        assertTrue(e3.getException() instanceof IllegalStateException);
    }

    @Test
    public void should_HandleConstructorWithMessageOnlyMultipleTimes() {
        for (int i = 0; i < 3; i++) {
            BindingException exception = new BindingException("Message " + i);
            assertEquals("Message " + i, exception.getMessage());
            assertNull(exception.getException());
        }
    }

    @Test
    public void should_HandleConstructorWithExceptionOnlyMultipleTimes() {
        for (int i = 0; i < 3; i++) {
            RuntimeException cause = new RuntimeException("Cause " + i);
            BindingException exception = new BindingException(cause);
            assertEquals(cause, exception.getException());
        }
    }

    @Test
    public void should_HandleConstructorWithBothArgumentsMultipleTimes() {
        for (int i = 0; i < 3; i++) {
            Exception cause = new RuntimeException("Cause " + i);
            BindingException exception = new BindingException(
                "Message " + i,
                cause
            );
            assertEquals("Message " + i, exception.getMessage());
            assertEquals(cause, exception.getException());
        }
    }

    @Test
    public void should_VerifyInheritanceFromException() {
        BindingException exception = new BindingException("test");
        assertTrue(exception instanceof Throwable);
        assertTrue(exception instanceof Exception);
    }

    @Test
    public void should_HandleGetStackTrace() {
        BindingException exception = new BindingException("test");
        StackTraceElement[] trace = exception.getStackTrace();

        assertNotNull(trace);
        assertTrue(trace.length > 0);
    }

    @Test
    public void should_HandleCauseChaining() {
        Exception root = new RuntimeException("root");
        Exception intermediate = new IOException("intermediate", root);
        BindingException binding = new BindingException(
            "binding",
            intermediate
        );

        assertEquals(intermediate, binding.getException());
    }
}
