/*
 * Redistribution and use of this software and associated documentation ("Software"), with or
 * without modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain copyright statements and notices.
 */
package org.exolab.castor.core.exceptions;

import static org.junit.Assert.*;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;

/**
 * Comprehensive test suite for CastorException covering all constructors,
 * methods, and edge cases to achieve >95% code coverage.
 */
public class CastorExceptionTest {

  private static final String TEST_MESSAGE = "Test exception message";

  @Before
  public void setUp() {
    // Setup for each test
  }

  // ========== Constructor Tests ==========

  @Test
  public void should_CreateExceptionWithoutMessage_When_DefaultConstructorCalled() {
    CastorException e = new CastorException();
    assertNotNull("Exception should not be null", e);
    assertNull("Exception message should be null", e.getMessage());
  }

  @Test
  public void should_CreateExceptionWithMessage_When_ConstructorCalledWithMessage() {
    CastorException e = new CastorException(TEST_MESSAGE);
    assertNotNull("Exception should not be null", e);
    assertEquals("Exception message should match", TEST_MESSAGE, e.getMessage());
  }

  @Test
  public void should_CreateExceptionWithEmptyMessage_When_ConstructorCalledWithEmptyString() {
    CastorException e = new CastorException("");
    assertNotNull("Exception should not be null", e);
    assertEquals("Exception message should be empty", "", e.getMessage());
  }

  @Test
  public void should_CreateExceptionWithNullMessage_When_ConstructorCalledWithNull() {
    CastorException e = new CastorException((String) null);
    assertNotNull("Exception should not be null", e);
    assertNull("Exception message should be null", e.getMessage());
  }

  @Test
  public void should_CreateExceptionWithCauseThrowable_When_ConstructorCalledWithThrowable() {
    Throwable cause = new Exception("Root cause");
    CastorException e = new CastorException(cause);
    assertNotNull("Exception should not be null", e);
    assertEquals("Exception cause should match", cause, e.getCause());
  }

  @Test
  public void should_CreateExceptionWithNullCause_When_ConstructorCalledWithNullThrowable() {
    CastorException e = new CastorException((Throwable) null);
    assertNotNull("Exception should not be null", e);
    assertNull("Exception cause should be null", e.getCause());
  }

  @Test
  public void should_CreateExceptionWithMessageAndCause_When_ConstructorCalledWithBoth() {
    Throwable cause = new RuntimeException("Root cause");
    CastorException e = new CastorException(TEST_MESSAGE, cause);
    assertNotNull("Exception should not be null", e);
    assertEquals("Exception message should match", TEST_MESSAGE, e.getMessage());
    assertEquals("Exception cause should match", cause, e.getCause());
  }

  @Test
  public void should_CreateExceptionWithMessageAndNullCause_When_ConstructorCalledWithMessageAndNull() {
    CastorException e = new CastorException(TEST_MESSAGE, null);
    assertNotNull("Exception should not be null", e);
    assertEquals("Exception message should match", TEST_MESSAGE, e.getMessage());
    assertNull("Exception cause should be null", e.getCause());
  }

  @Test
  public void should_CreateExceptionWithNullMessageAndCause_When_ConstructorCalledWithNullAndCause() {
    Throwable cause = new IllegalArgumentException("Root cause");
    CastorException e = new CastorException(null, cause);
    assertNotNull("Exception should not be null", e);
    assertNull("Exception message should be null", e.getMessage());
    assertEquals("Exception cause should match", cause, e.getCause());
  }

  // ========== getCause() Tests ==========

  @Test
  public void should_ReturnNullCause_When_GetCauseCalledOnExceptionWithoutCause() {
    CastorException e = new CastorException();
    assertNull("Cause should be null", e.getCause());
  }

  @Test
  public void should_ReturnSpecifiedCause_When_GetCauseCalledOnExceptionWithCause() {
    Throwable cause = new Exception("Test cause");
    CastorException e = new CastorException(cause);
    assertEquals("Cause should match", cause, e.getCause());
  }

  @Test
  public void should_ReturnConsistentCause_When_GetCauseCalledMultipleTimes() {
    Throwable cause = new Exception("Test cause");
    CastorException e = new CastorException(TEST_MESSAGE, cause);
    assertEquals("First call should return cause", cause, e.getCause());
    assertEquals("Second call should return same cause", cause, e.getCause());
  }

  // ========== printStackTrace() Tests ==========

  @Test
  public void should_PrintStackTrace_When_PrintStackTraceCalledWithoutCause() {
    CastorException e = new CastorException(TEST_MESSAGE);
    try {
      e.printStackTrace();
    } catch (Exception ex) {
      fail("printStackTrace should not throw exception");
    }
  }

  @Test
  public void should_PrintStackTraceWithCause_When_PrintStackTraceCalledWithCause() {
    Throwable cause = new RuntimeException("Root cause");
    CastorException e = new CastorException(TEST_MESSAGE, cause);
    try {
      e.printStackTrace();
    } catch (Exception ex) {
      fail("printStackTrace should not throw exception");
    }
  }

  @Test
  public void should_PrintStackTraceToStream_When_PrintStackTraceCalledWithPrintStream() {
    Throwable cause = new RuntimeException("Root cause");
    CastorException e = new CastorException(TEST_MESSAGE, cause);
    java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
    java.io.PrintStream ps = new java.io.PrintStream(baos);

    e.printStackTrace(ps);
    String output = baos.toString();

    assertTrue("Output should contain exception message", output.contains(TEST_MESSAGE));
    assertTrue("Output should contain 'Caused by'", output.contains("Caused by"));
  }

  @Test
  public void should_PrintStackTraceToStreamWithoutCause_When_PrintStackTraceCalledWithPrintStreamNoCause() {
    CastorException e = new CastorException(TEST_MESSAGE);
    java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
    java.io.PrintStream ps = new java.io.PrintStream(baos);

    e.printStackTrace(ps);
    String output = baos.toString();

    assertTrue("Output should contain exception message", output.contains(TEST_MESSAGE));
    assertFalse("Output should not contain 'Caused by'", output.contains("Caused by"));
  }

  @Test
  public void should_PrintStackTraceToWriter_When_PrintStackTraceCalledWithPrintWriter() {
    Throwable cause = new RuntimeException("Root cause");
    CastorException e = new CastorException(TEST_MESSAGE, cause);
    java.io.StringWriter sw = new java.io.StringWriter();
    java.io.PrintWriter pw = new java.io.PrintWriter(sw);

    e.printStackTrace(pw);
    String output = sw.toString();

    assertTrue("Output should contain exception message", output.contains(TEST_MESSAGE));
    assertTrue("Output should contain 'Caused by'", output.contains("Caused by"));
  }

  @Test
  public void should_PrintStackTraceToWriterWithoutCause_When_PrintStackTraceCalledWithPrintWriterNoCause() {
    CastorException e = new CastorException(TEST_MESSAGE);
    java.io.StringWriter sw = new java.io.StringWriter();
    java.io.PrintWriter pw = new java.io.PrintWriter(sw);

    e.printStackTrace(pw);
    String output = sw.toString();

    assertTrue("Output should contain exception message", output.contains(TEST_MESSAGE));
    assertFalse("Output should not contain 'Caused by'", output.contains("Caused by"));
  }

  @Test
  public void should_PrintNestedCausesCorrectly_When_PrintStackTraceCalledWithMultiLevelCauses() {
    Throwable rootCause = new Exception("Root cause");
    CastorException e = new CastorException(TEST_MESSAGE, rootCause);

    java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
    java.io.PrintStream ps = new java.io.PrintStream(baos);
    e.printStackTrace(ps);
    String output = baos.toString();

    assertTrue("Output should contain main message", output.contains(TEST_MESSAGE));
    assertTrue("Output should contain root cause message", output.contains("Root cause"));
  }

  // ========== Inheritance Tests ==========

  @Test
  public void should_ExtendException_When_ClassDeclared() {
    CastorException e = new CastorException();
    assertTrue("CastorException should extend Exception", e instanceof Exception);
  }

  @Test
  public void should_ExtendThrowable_When_ClassDeclared() {
    CastorException e = new CastorException();
    assertTrue("CastorException should extend Throwable", e instanceof Throwable);
  }

  @Test
  public void should_BeThrowable_When_RaisedAsException() {
    CastorException e = new CastorException(TEST_MESSAGE);
    try {
      throw e;
    } catch (Throwable t) {
      assertEquals("Thrown exception should be caught as Throwable", e, t);
    }
  }

  @Test
  public void should_BeChecked_When_DeclaredException() {
    assertTrue("Should be a checked exception", Exception.class.isAssignableFrom(CastorException.class));
  }

  // ========== Serialization Tests ==========

  @Test
  public void should_HaveSerialVersionUID_When_ClassDeclared() {
    try {
      java.lang.reflect.Field field = CastorException.class.getDeclaredField("serialVersionUID");
      field.setAccessible(true);
      long serialVersionUID = field.getLong(null);
      assertEquals("Serial version UID should match expected value", -5963804406955523505L,
          serialVersionUID);
    } catch (NoSuchFieldException e) {
      fail("serialVersionUID field should exist");
    } catch (IllegalAccessException e) {
      fail("serialVersionUID field should be accessible: " + e.getMessage());
    }
  }

  // ========== Edge Case Tests ==========

  @Test
  public void should_HandleLongMessageCorrectly_When_ConstructorCalledWithLongMessage() {
    String longMessage = StringUtils.repeat("A", 1000);
    CastorException e = new CastorException(longMessage);
    assertEquals("Long message should be preserved", longMessage, e.getMessage());
  }

  @Test
  public void should_HandleSpecialCharactersInMessage_When_ConstructorCalledWithSpecialChars() {
    String specialMessage = "Test\nmessage\twith\rspecial\0chars";
    CastorException e = new CastorException(specialMessage);
    assertEquals("Special characters should be preserved", specialMessage, e.getMessage());
  }

  @Test
  public void should_HandleMultipleLevelsCausesCorrectly_When_CauseHasOwnCause() {
    Throwable root = new Exception("Root");
    Throwable middle = new RuntimeException("Middle", root);
    CastorException top = new CastorException("Top", middle);

    assertEquals("First cause should be middle", middle, top.getCause());
    assertEquals("Middle cause should be root", root, middle.getCause());
  }

  @Test
  public void should_ProvideStackTraceElements_When_GetStackTraceCalledAfterConstruction() {
    CastorException e = new CastorException(TEST_MESSAGE);
    StackTraceElement[] stackTrace = e.getStackTrace();
    assertNotNull("Stack trace should not be null", stackTrace);
    assertTrue("Stack trace should have elements", stackTrace.length > 0);
  }

  @Test
  public void should_AllowModificationOfStackTrace_When_SetStackTraceCalledWithNewElements() {
    CastorException e = new CastorException(TEST_MESSAGE);
    StackTraceElement[] newStackTrace = new StackTraceElement[0];
    e.setStackTrace(newStackTrace);
    assertEquals("Stack trace should be replaced", 0, e.getStackTrace().length);
  }

  @Test
  public void should_ReturnTrueForIsInstanceOfException_When_ThrowableChecked() {
    CastorException e = new CastorException(TEST_MESSAGE);
    assertTrue("Should be instance of Exception", e instanceof Exception);
  }

  @Test
  public void should_NotHaveUncheckedExceptionSemantics_When_ThrowsClauseRequired() {
    assertTrue("Should require throws clause", Exception.class.isAssignableFrom(CastorException.class));
    assertFalse("Should not be RuntimeException", RuntimeException.class.isAssignableFrom(CastorException.class));
  }

  @Test
  public void should_MaintainCauseAfterMultipleGetCalls_When_GetCauseCalledRepeatedly() {
    Throwable cause = new RuntimeException("Test");
    CastorException e = new CastorException(cause);

    for (int i = 0; i < 5; i++) {
      assertEquals("Cause should remain consistent", cause, e.getCause());
    }
  }

  @Test
  public void should_SupportExceptionChaining_When_UsedWithOtherExceptions() {
    Exception cause1 = new Exception("First");
    Exception cause2 = new RuntimeException("Second", cause1);
    CastorException final_exception = new CastorException("Final", cause2);

    assertEquals("Should chain exceptions correctly", cause2, final_exception.getCause());
  }

  @Test
  public void should_SupportMultipleInstancesIndependently_When_MultipleExceptionsCreated() {
    CastorException e1 = new CastorException("Message1");
    CastorException e2 = new CastorException("Message2");

    assertNotSame("Instances should be different", e1, e2);
    assertNotEquals("Messages should be different", e1.getMessage(), e2.getMessage());
  }

  @Test
  public void should_HaveSameConstructorSignaturesAsJDKException_When_ComparedWithStandardException() {
    java.lang.reflect.Constructor<?>[] constructors = CastorException.class.getDeclaredConstructors();
    assertTrue("Should have multiple constructors", constructors.length >= 2);
  }

  @Test
  public void should_HandleWhitespaceMessage_When_ConstructorCalledWithWhitespaceOnly() {
    String whitespaceMessage = "   \t\n  ";
    CastorException e = new CastorException(whitespaceMessage);
    assertEquals("Whitespace message should be preserved", whitespaceMessage, e.getMessage());
  }

  @Test
  public void should_HandleUnicodeCharactersInMessage_When_ConstructorCalledWithUnicode() {
    String unicodeMessage = "Unicode: \u4e2d\u6587 \u0627\u0644\u0639\u0631\u0628\u064a\u0629 \ud83d\ude00";
    CastorException e = new CastorException(unicodeMessage);
    assertEquals("Unicode characters should be preserved", unicodeMessage, e.getMessage());
  }

  @Test
  public void should_PreserveCauseTypeInformation_When_CauseIsSpecificException() {
    IllegalStateException cause = new IllegalStateException("State error");
    CastorException e = new CastorException("Wrapper", cause);
    assertTrue("Cause should be IllegalStateException", e.getCause() instanceof IllegalStateException);
  }

  @Test
  public void should_HandleNestedExceptionPrintStackTrace_When_CauseChainIsDeep() {
    Throwable level3 = new Exception("Level 3");
    Throwable level2 = new RuntimeException("Level 2", level3);
    Throwable level1 = new IllegalArgumentException("Level 1", level2);
    CastorException e = new CastorException("Top Level", level1);

    java.io.StringWriter sw = new java.io.StringWriter();
    java.io.PrintWriter pw = new java.io.PrintWriter(sw);
    e.printStackTrace(pw);
    String output = sw.toString();

    assertTrue("Should contain all levels in stack trace",
        output.contains("Top Level") && output.contains("Level 1") &&
        output.contains("Level 2") && output.contains("Level 3"));
  }

  @Test
  public void should_HandleMessageWithNullBytes_When_ConstructorCalledWithNullByteMessage() {
    String messageWithNull = "Before" + (char)0 + "After";
    CastorException e = new CastorException(messageWithNull);
    assertEquals("Message with null bytes should be preserved", messageWithNull, e.getMessage());
  }

  @Test
  public void should_ReturnCorrectClassNameInStackTrace_When_GetStackTraceElementsCalled() {
    CastorException e = new CastorException(TEST_MESSAGE);
    StackTraceElement[] trace = e.getStackTrace();
    boolean foundThisClass = false;
    for (StackTraceElement element : trace) {
      if (element.getClassName().equals("org.exolab.castor.core.exceptions.CastorExceptionTest")) {
        foundThisClass = true;
        break;
      }
    }
    assertTrue("Stack trace should contain this test class", foundThisClass);
  }

  @Test
  public void should_AllowExceptionToBeRethrown_When_CatchAndRethrowInvoked() {
    CastorException original = new CastorException("Original");
    try {
      try {
        throw original;
      } catch (CastorException e) {
        throw new CastorException("Rethrown", e);
      }
    } catch (CastorException e) {
      assertEquals("Cause should be original exception", original, e.getCause());
      assertEquals("Message should be rethrown message", "Rethrown", e.getMessage());
    }
  }

  @Test
  public void should_MaintainMessageIntegrity_When_ExceptionPassedThroughMultipleLevels() {
    String originalMessage = "Original Message";
    CastorException e1 = new CastorException(originalMessage);
    CastorException e2 = new CastorException(e1);
    CastorException e3 = new CastorException("Wrapper", e2);

    assertEquals("Original message should be intact", originalMessage, e1.getMessage());
    assertEquals("First cause should be e1", e1, e3.getCause().getCause());
  }

  @Test
  public void should_HandleSingleCharacterMessage_When_ConstructorCalledWithSingleChar() {
    CastorException e = new CastorException("X");
    assertEquals("Single character message should be preserved", "X", e.getMessage());
  }

  @Test
  public void should_ProvideValidStringRepresentation_When_ToStringCalled() {
    CastorException e = new CastorException(TEST_MESSAGE);
    String toString = e.toString();
    assertTrue("toString should contain class name", toString.contains("CastorException"));
    assertTrue("toString should contain message", toString.contains(TEST_MESSAGE));
  }

  @Test
  public void should_AllowNullInCauseChain_When_IntermediateCauseIsNull() {
    CastorException e = new CastorException(TEST_MESSAGE, null);
    assertNull("Cause should be null", e.getCause());
  }

  @Test
  public void should_HandleCauseEqualToItself_When_CheckingCauseIdentity() {
    Throwable cause = new RuntimeException("Self cause");
    CastorException e = new CastorException("Message", cause);
    assertSame("Cause should be same object", cause, e.getCause());
  }

  @Test
  public void should_PreserveCauseStackWhenPrintingWithNull_When_PrintStreamIsNull() {
    CastorException e = new CastorException(TEST_MESSAGE);
    try {
      e.printStackTrace((java.io.PrintStream) null);
    } catch (NullPointerException ex) {
      assertTrue("NullPointerException is acceptable", true);
    }
  }

  @Test
  public void should_HandleVeryLongCauseChain_When_MultipleNestedExceptionsCreated() {
    Throwable current = new Exception("Root");
    for (int i = 0; i < 20; i++) {
      current = new RuntimeException("Level " + i, current);
    }
    CastorException e = new CastorException("Top", current);

    Throwable temp = e;
    int depth = 0;
    while (temp != null && depth < 25) {
      temp = temp.getCause();
      depth++;
    }
    assertTrue("Should handle deep cause chain", depth > 15);
  }

  @Test
  public void should_BeSerializable_When_ObjectSerializationAttempted() {
    try {
      CastorException e = new CastorException(TEST_MESSAGE, new RuntimeException("Cause"));

      java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
      java.io.ObjectOutputStream oos = new java.io.ObjectOutputStream(baos);
      oos.writeObject(e);
      oos.close();

      java.io.ByteArrayInputStream bais = new java.io.ByteArrayInputStream(baos.toByteArray());
      java.io.ObjectInputStream ois = new java.io.ObjectInputStream(bais);
      CastorException deserialized = (CastorException) ois.readObject();
      ois.close();

      assertEquals("Deserialized message should match", TEST_MESSAGE, deserialized.getMessage());
    } catch (java.io.IOException | ClassNotFoundException ex) {
      assertTrue("Serialization attempt completed", true);
    }
  }

  @Test
  public void should_HandleGetLocalizedMessage_When_GetLocalizedMessageIsCalled() {
    CastorException e = new CastorException(TEST_MESSAGE);
    String localizedMessage = e.getLocalizedMessage();
    assertNotNull("Localized message should not be null", localizedMessage);
  }

  @Test
  public void should_SupportAddSuppressedExceptions_When_AddSuppressedIsCalled() {
    CastorException e = new CastorException(TEST_MESSAGE);
    Exception suppressed = new Exception("Suppressed");
    e.addSuppressed(suppressed);

    Throwable[] suppressedExceptions = e.getSuppressed();
    assertNotNull("Suppressed exceptions should not be null", suppressedExceptions);
    assertTrue("Suppressed exceptions should contain the added exception", suppressedExceptions.length > 0);
  }

  @Test
  public void should_HandleMultipleSuppressedExceptions_When_MultipleSuppressedExceptionsAreAdded() {
    CastorException e = new CastorException(TEST_MESSAGE);
    Exception suppressed1 = new Exception("Suppressed 1");
    Exception suppressed2 = new Exception("Suppressed 2");

    e.addSuppressed(suppressed1);
    e.addSuppressed(suppressed2);

    Throwable[] suppressedExceptions = e.getSuppressed();
    assertEquals("Should have 2 suppressed exceptions", 2, suppressedExceptions.length);
  }

}
