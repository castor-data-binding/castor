/*
 * Redistribution and use of this software and associated documentation ("Software"), with or
 * without modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain copyright statements and notices.
 */
package org.exolab.castor.core.exceptions;

import static org.junit.Assert.*;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.ByteArrayOutputStream;
import java.io.StringWriter;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;

/**
 * Comprehensive test suite for CastorIllegalStateException covering all constructors,
 * methods, and edge cases to achieve >95% code coverage.
 */
public class CastorIllegalStateExceptionTest {

  private static final String TEST_MESSAGE = "Test exception message";

  @Before
  public void setUp() {
    // Setup for each test
  }

  // ========== Constructor Tests ==========

  @Test
  public void should_CreateExceptionWithoutMessage_When_DefaultConstructorCalled() {
    CastorIllegalStateException e = new CastorIllegalStateException();
    assertNotNull("Exception should not be null", e);
    assertNull("Exception message should be null", e.getMessage());
    assertNull("Exception cause should be null", e.getCause());
  }

  @Test
  public void should_CreateExceptionWithMessage_When_ConstructorCalledWithMessage() {
    CastorIllegalStateException e = new CastorIllegalStateException(TEST_MESSAGE);
    assertNotNull("Exception should not be null", e);
    assertEquals("Exception message should match", TEST_MESSAGE, e.getMessage());
    assertNull("Exception cause should be null", e.getCause());
  }

  @Test
  public void should_CreateExceptionWithEmptyMessage_When_ConstructorCalledWithEmptyString() {
    CastorIllegalStateException e = new CastorIllegalStateException("");
    assertNotNull("Exception should not be null", e);
    assertEquals("Exception message should be empty", "", e.getMessage());
  }

  @Test
  public void should_CreateExceptionWithNullMessage_When_ConstructorCalledWithNull() {
    CastorIllegalStateException e = new CastorIllegalStateException((String) null);
    assertNotNull("Exception should not be null", e);
    assertNull("Exception message should be null", e.getMessage());
  }

  @Test
  public void should_CreateExceptionWithCauseThrowable_When_ConstructorCalledWithThrowable() {
    Throwable cause = new Exception("Root cause");
    CastorIllegalStateException e = new CastorIllegalStateException(cause);
    assertNotNull("Exception should not be null", e);
    assertEquals("Exception cause should match", cause, e.getCause());
    assertNull("Exception message should be null when only cause provided", e.getMessage());
  }

  @Test
  public void should_CreateExceptionWithNullCause_When_ConstructorCalledWithNullThrowable() {
    CastorIllegalStateException e = new CastorIllegalStateException((Throwable) null);
    assertNotNull("Exception should not be null", e);
    assertNull("Exception cause should be null", e.getCause());
  }

  @Test
  public void should_CreateExceptionWithMessageAndCause_When_ConstructorCalledWithBoth() {
    Throwable cause = new RuntimeException("Root cause");
    CastorIllegalStateException e = new CastorIllegalStateException(TEST_MESSAGE, cause);
    assertNotNull("Exception should not be null", e);
    assertEquals("Exception message should match", TEST_MESSAGE, e.getMessage());
    assertEquals("Exception cause should match", cause, e.getCause());
  }

  @Test
  public void should_CreateExceptionWithMessageAndNullCause_When_ConstructorCalledWithMessageAndNull() {
    CastorIllegalStateException e = new CastorIllegalStateException(TEST_MESSAGE, null);
    assertNotNull("Exception should not be null", e);
    assertEquals("Exception message should match", TEST_MESSAGE, e.getMessage());
    assertNull("Exception cause should be null", e.getCause());
  }

  @Test
  public void should_CreateExceptionWithNullMessageAndCause_When_ConstructorCalledWithNullAndCause() {
    Throwable cause = new IllegalArgumentException("Root cause");
    CastorIllegalStateException e = new CastorIllegalStateException(null, cause);
    assertNotNull("Exception should not be null", e);
    assertNull("Exception message should be null", e.getMessage());
    assertEquals("Exception cause should match", cause, e.getCause());
  }

  // ========== getCause() Tests ==========

  @Test
  public void should_ReturnNullCause_When_GetCauseCalledOnExceptionWithoutCause() {
    CastorIllegalStateException e = new CastorIllegalStateException();
    assertNull("Cause should be null", e.getCause());
  }

  @Test
  public void should_ReturnSpecifiedCause_When_GetCauseCalledOnExceptionWithCause() {
    Throwable cause = new Exception("Test cause");
    CastorIllegalStateException e = new CastorIllegalStateException(cause);
    assertEquals("Cause should match", cause, e.getCause());
  }

  @Test
  public void should_ReturnConsistentCause_When_GetCauseCalledMultipleTimes() {
    Throwable cause = new Exception("Test cause");
    CastorIllegalStateException e = new CastorIllegalStateException(TEST_MESSAGE, cause);
    assertEquals("First call should return cause", cause, e.getCause());
    assertEquals("Second call should return same cause", cause, e.getCause());
  }

  @Test
  public void should_ReturnCauseFromConstructor_When_GetCauseCalledAfterConstructionWithCause() {
    Throwable cause = new RuntimeException("Root");
    CastorIllegalStateException e = new CastorIllegalStateException(cause);
    assertEquals("Should return cause set in constructor", cause, e.getCause());
  }

  // ========== printStackTrace() Tests ==========

  @Test
  public void should_PrintStackTrace_When_PrintStackTraceCalledWithoutCause() {
    CastorIllegalStateException e = new CastorIllegalStateException(TEST_MESSAGE);
    try {
      e.printStackTrace();
    } catch (Exception ex) {
      fail("printStackTrace should not throw exception");
    }
  }

  @Test
  public void should_PrintStackTraceWithCause_When_PrintStackTraceCalledWithCause() {
    Throwable cause = new RuntimeException("Root cause");
    CastorIllegalStateException e = new CastorIllegalStateException(TEST_MESSAGE, cause);
    try {
      e.printStackTrace();
    } catch (Exception ex) {
      fail("printStackTrace should not throw exception");
    }
  }

  @Test
  public void should_PrintStackTraceToStream_When_PrintStackTraceCalledWithPrintStream() {
    Throwable cause = new RuntimeException("Root cause");
    CastorIllegalStateException e = new CastorIllegalStateException(TEST_MESSAGE, cause);
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    PrintStream ps = new PrintStream(baos);

    e.printStackTrace(ps);
    String output = baos.toString();

    assertTrue("Output should contain exception message", output.contains(TEST_MESSAGE));
    assertTrue("Output should contain 'Caused by'", output.contains("Caused by"));
  }

  @Test
  public void should_PrintStackTraceToStreamWithoutCause_When_PrintStackTraceCalledWithPrintStreamNoCause() {
    CastorIllegalStateException e = new CastorIllegalStateException(TEST_MESSAGE);
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    PrintStream ps = new PrintStream(baos);

    e.printStackTrace(ps);
    String output = baos.toString();

    assertTrue("Output should contain exception message", output.contains(TEST_MESSAGE));
    assertFalse("Output should not contain 'Caused by'", output.contains("Caused by"));
  }

  @Test
  public void should_PrintStackTraceToWriter_When_PrintStackTraceCalledWithPrintWriter() {
    Throwable cause = new RuntimeException("Root cause");
    CastorIllegalStateException e = new CastorIllegalStateException(TEST_MESSAGE, cause);
    StringWriter sw = new StringWriter();
    PrintWriter pw = new PrintWriter(sw);

    e.printStackTrace(pw);
    String output = sw.toString();

    assertTrue("Output should contain exception message", output.contains(TEST_MESSAGE));
    assertTrue("Output should contain 'Caused by'", output.contains("Caused by"));
  }

  @Test
  public void should_PrintStackTraceToWriterWithoutCause_When_PrintStackTraceCalledWithPrintWriterNoCause() {
    CastorIllegalStateException e = new CastorIllegalStateException(TEST_MESSAGE);
    StringWriter sw = new StringWriter();
    PrintWriter pw = new PrintWriter(sw);

    e.printStackTrace(pw);
    String output = sw.toString();

    assertTrue("Output should contain exception message", output.contains(TEST_MESSAGE));
    assertFalse("Output should not contain 'Caused by'", output.contains("Caused by"));
  }

  @Test
  public void should_PrintNestedCausesCorrectly_When_PrintStackTraceCalledWithMultiLevelCauses() {
    Throwable rootCause = new Exception("Root cause");
    CastorIllegalStateException e = new CastorIllegalStateException(TEST_MESSAGE, rootCause);

    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    PrintStream ps = new PrintStream(baos);
    e.printStackTrace(ps);
    String output = baos.toString();

    assertTrue("Output should contain main message", output.contains(TEST_MESSAGE));
    assertTrue("Output should contain root cause message", output.contains("Root cause"));
  }

  @Test
  public void should_HandleNullPrintStream_When_PrintStackThrownWithPrintStreamNull() {
    CastorIllegalStateException e = new CastorIllegalStateException(TEST_MESSAGE);
    try {
      e.printStackTrace((PrintStream) null);
      fail("Should throw NullPointerException");
    } catch (NullPointerException ex) {
      assertTrue("NullPointerException should be caught", true);
    }
  }

  @Test
  public void should_HandleNullPrintWriter_When_PrintStackThrownWithPrintWriterNull() {
    CastorIllegalStateException e = new CastorIllegalStateException(TEST_MESSAGE);
    try {
      e.printStackTrace((PrintWriter) null);
      fail("Should throw NullPointerException");
    } catch (NullPointerException ex) {
      assertTrue("NullPointerException should be caught", true);
    }
  }

  // ========== Inheritance Tests ==========

  @Test
  public void should_ExtendIllegalStateException_When_ClassDeclared() {
    CastorIllegalStateException e = new CastorIllegalStateException();
    assertTrue("CastorIllegalStateException should extend IllegalStateException",
        e instanceof IllegalStateException);
  }

  @Test
  public void should_ExtendRuntimeException_When_ClassDeclared() {
    CastorIllegalStateException e = new CastorIllegalStateException();
    assertTrue("CastorIllegalStateException should extend RuntimeException",
        e instanceof RuntimeException);
  }

  @Test
  public void should_ExtendThrowable_When_ClassDeclared() {
    CastorIllegalStateException e = new CastorIllegalStateException();
    assertTrue("CastorIllegalStateException should extend Throwable", e instanceof Throwable);
  }

  @Test
  public void should_BeThrowable_When_RaisedAsException() {
    CastorIllegalStateException e = new CastorIllegalStateException(TEST_MESSAGE);
    try {
      throw e;
    } catch (Throwable t) {
      assertEquals("Thrown exception should be caught as Throwable", e, t);
    }
  }

  @Test
  public void should_BeUnchecked_When_NotDeclaredInThrowsClause() {
    // CastorIllegalStateException is unchecked (extends RuntimeException)
    assertTrue("Should be an unchecked exception",
        RuntimeException.class.isAssignableFrom(CastorIllegalStateException.class));
  }

  // ========== Serialization Tests ==========

  @Test
  public void should_HaveSerialVersionUID_When_ClassDeclared() {
    try {
      java.lang.reflect.Field field = CastorIllegalStateException.class.getDeclaredField("serialVersionUID");
      field.setAccessible(true);
      long serialVersionUID = field.getLong(null);
      assertEquals("Serial version UID should match expected value", 2351884252990815335L,
          serialVersionUID);
    } catch (NoSuchFieldException e) {
      fail("serialVersionUID field should exist");
    } catch (IllegalAccessException e) {
      fail("serialVersionUID field should be accessible: " + e.getMessage());
    }
  }

  // ========== Private _cause Field Tests ==========

  @Test
  public void should_StoreCauseInPrivateField_When_ConstructorCalledWithCause() {
    Throwable cause = new RuntimeException("Test");
    CastorIllegalStateException e = new CastorIllegalStateException(cause);
    assertEquals("Should store cause in private field", cause, e.getCause());
  }

  @Test
  public void should_StoreCauseInPrivateField_When_ConstructorCalledWithMessageAndCause() {
    Throwable cause = new RuntimeException("Test");
    CastorIllegalStateException e = new CastorIllegalStateException(TEST_MESSAGE, cause);
    assertEquals("Should store cause in private field", cause, e.getCause());
  }

  // ========== Edge Case Tests ==========

  @Test
  public void should_HandleLongMessageCorrectly_When_ConstructorCalledWithLongMessage() {
    String longMessage = StringUtils.repeat("A", 1000);
    CastorIllegalStateException e = new CastorIllegalStateException(longMessage);
    assertEquals("Long message should be preserved", longMessage, e.getMessage());
  }

  @Test
  public void should_HandleSpecialCharactersInMessage_When_ConstructorCalledWithSpecialChars() {
    String specialMessage = "Test\nmessage\twith\rspecial\0chars";
    CastorIllegalStateException e = new CastorIllegalStateException(specialMessage);
    assertEquals("Special characters should be preserved", specialMessage, e.getMessage());
  }

  @Test
  public void should_HandleMultipleLevelsCausesCorrectly_When_CauseHasOwnCause() {
    Throwable root = new Exception("Root");
    Throwable middle = new RuntimeException("Middle", root);
    CastorIllegalStateException top = new CastorIllegalStateException("Top", middle);

    assertEquals("First cause should be middle", middle, top.getCause());
    assertEquals("Middle cause should be root", root, middle.getCause());
  }

  @Test
  public void should_ProvideStackTraceElements_When_GetStackTraceCalledAfterConstruction() {
    CastorIllegalStateException e = new CastorIllegalStateException(TEST_MESSAGE);
    StackTraceElement[] stackTrace = e.getStackTrace();
    assertNotNull("Stack trace should not be null", stackTrace);
    assertTrue("Stack trace should have elements", stackTrace.length > 0);
  }

  @Test
  public void should_AllowModificationOfStackTrace_When_SetStackTraceCalledWithNewElements() {
    CastorIllegalStateException e = new CastorIllegalStateException(TEST_MESSAGE);
    StackTraceElement[] newStackTrace = new StackTraceElement[0];
    e.setStackTrace(newStackTrace);
    assertEquals("Stack trace should be replaced", 0, e.getStackTrace().length);
  }

  @Test
  public void should_ReturnTrueForIsInstanceOfRuntimeException_When_ThrowableChecked() {
    CastorIllegalStateException e = new CastorIllegalStateException(TEST_MESSAGE);
    assertTrue("Should be instance of RuntimeException", e instanceof RuntimeException);
  }

  @Test
  public void should_MaintainCauseAfterMultipleGetCalls_When_GetCauseCalledRepeatedly() {
    Throwable cause = new RuntimeException("Test");
    CastorIllegalStateException e = new CastorIllegalStateException(cause);

    for (int i = 0; i < 5; i++) {
      assertEquals("Cause should remain consistent", cause, e.getCause());
    }
  }

  @Test
  public void should_SupportExceptionChaining_When_UsedWithOtherExceptions() {
    Exception cause1 = new Exception("First");
    Exception cause2 = new RuntimeException("Second", cause1);
    CastorIllegalStateException final_exception = new CastorIllegalStateException("Final", cause2);

    assertEquals("Should chain exceptions correctly", cause2, final_exception.getCause());
  }

  @Test
  public void should_SupportMultipleInstancesIndependently_When_MultipleExceptionsCreated() {
    CastorIllegalStateException e1 = new CastorIllegalStateException("Message1");
    CastorIllegalStateException e2 = new CastorIllegalStateException("Message2");

    assertNotSame("Instances should be different", e1, e2);
    assertNotEquals("Messages should be different", e1.getMessage(), e2.getMessage());
  }

  @Test
  public void should_BeFinal_When_ClassInspected() {
    int modifiers = CastorIllegalStateException.class.getModifiers();
    assertTrue("CastorIllegalStateException should be final",
        java.lang.reflect.Modifier.isFinal(modifiers));
  }
}
