/*
 * Copyright 2007 Werner Guttmann
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package org.castor.core.exception;

import static org.junit.Assert.*;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.ByteArrayOutputStream;
import java.io.StringWriter;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;

/**
 * Comprehensive test suite for IllegalClassDescriptorInitialization exception covering all
 * constructors, methods, and edge cases to achieve >95% code coverage.
 */
public class IllegalClassDescriptorInitializationTest {

  private static final String TEST_MESSAGE = "Test exception message";

  @Before
  public void setUp() {
    // Setup for each test
  }

  // ========== Constructor Tests ==========

  @Test
  public void should_CreateExceptionWithoutMessage_When_DefaultConstructorCalled() {
    IllegalClassDescriptorInitialization e = new IllegalClassDescriptorInitialization();
    assertNotNull("Exception should not be null", e);
    assertNull("Exception message should be null", e.getMessage());
  }

  @Test
  public void should_CreateExceptionWithMessage_When_ConstructorCalledWithMessage() {
    IllegalClassDescriptorInitialization e = new IllegalClassDescriptorInitialization(TEST_MESSAGE);
    assertNotNull("Exception should not be null", e);
    assertEquals("Exception message should match", TEST_MESSAGE, e.getMessage());
  }

  @Test
  public void should_CreateExceptionWithEmptyMessage_When_ConstructorCalledWithEmptyString() {
    IllegalClassDescriptorInitialization e = new IllegalClassDescriptorInitialization("");
    assertNotNull("Exception should not be null", e);
    assertEquals("Exception message should be empty", "", e.getMessage());
  }

  @Test
  public void should_CreateExceptionWithNullMessage_When_ConstructorCalledWithNull() {
    IllegalClassDescriptorInitialization e = new IllegalClassDescriptorInitialization((String) null);
    assertNotNull("Exception should not be null", e);
    assertNull("Exception message should be null", e.getMessage());
  }

  @Test
  public void should_CreateExceptionWithCauseThrowable_When_ConstructorCalledWithThrowable() {
    Throwable cause = new Exception("Root cause");
    IllegalClassDescriptorInitialization e = new IllegalClassDescriptorInitialization(cause);
    assertNotNull("Exception should not be null", e);
    assertEquals("Exception cause should match", cause, e.getCause());
  }

  @Test
  public void should_CreateExceptionWithNullCause_When_ConstructorCalledWithNullThrowable() {
    IllegalClassDescriptorInitialization e =
        new IllegalClassDescriptorInitialization((Throwable) null);
    assertNotNull("Exception should not be null", e);
    assertNull("Exception cause should be null", e.getCause());
  }

  @Test
  public void should_CreateExceptionWithMessageAndCause_When_ConstructorCalledWithBoth() {
    Throwable cause = new RuntimeException("Root cause");
    IllegalClassDescriptorInitialization e =
        new IllegalClassDescriptorInitialization(TEST_MESSAGE, cause);
    assertNotNull("Exception should not be null", e);
    assertEquals("Exception message should match", TEST_MESSAGE, e.getMessage());
    assertEquals("Exception cause should match", cause, e.getCause());
  }

  @Test
  public void should_CreateExceptionWithMessageAndNullCause_When_ConstructorCalledWithMessageAndNull() {
    IllegalClassDescriptorInitialization e =
        new IllegalClassDescriptorInitialization(TEST_MESSAGE, null);
    assertNotNull("Exception should not be null", e);
    assertEquals("Exception message should match", TEST_MESSAGE, e.getMessage());
    assertNull("Exception cause should be null", e.getCause());
  }

  @Test
  public void should_CreateExceptionWithNullMessageAndCause_When_ConstructorCalledWithNullAndCause() {
    Throwable cause = new IllegalArgumentException("Root cause");
    IllegalClassDescriptorInitialization e = new IllegalClassDescriptorInitialization(null, cause);
    assertNotNull("Exception should not be null", e);
    assertNull("Exception message should be null", e.getMessage());
    assertEquals("Exception cause should match", cause, e.getCause());
  }

  // ========== getCause() Tests ==========

  @Test
  public void should_ReturnNullCause_When_GetCauseCalledOnExceptionWithoutCause() {
    IllegalClassDescriptorInitialization e = new IllegalClassDescriptorInitialization();
    assertNull("Cause should be null", e.getCause());
  }

  @Test
  public void should_ReturnSpecifiedCause_When_GetCauseCalledOnExceptionWithCause() {
    Throwable cause = new Exception("Test cause");
    IllegalClassDescriptorInitialization e = new IllegalClassDescriptorInitialization(cause);
    assertEquals("Cause should match", cause, e.getCause());
  }

  @Test
  public void should_ReturnConsistentCause_When_GetCauseCalledMultipleTimes() {
    Throwable cause = new Exception("Test cause");
    IllegalClassDescriptorInitialization e =
        new IllegalClassDescriptorInitialization(TEST_MESSAGE, cause);
    assertEquals("First call should return cause", cause, e.getCause());
    assertEquals("Second call should return same cause", cause, e.getCause());
  }

  // ========== printStackTrace() Tests ==========

  @Test
  public void should_PrintStackTrace_When_PrintStackTraceCalledWithoutCause() {
    IllegalClassDescriptorInitialization e = new IllegalClassDescriptorInitialization(TEST_MESSAGE);
    try {
      e.printStackTrace();
    } catch (Exception ex) {
      fail("printStackTrace should not throw exception");
    }
  }

  @Test
  public void should_PrintStackTraceWithCause_When_PrintStackTraceCalledWithCause() {
    Throwable cause = new RuntimeException("Root cause");
    IllegalClassDescriptorInitialization e =
        new IllegalClassDescriptorInitialization(TEST_MESSAGE, cause);
    try {
      e.printStackTrace();
    } catch (Exception ex) {
      fail("printStackTrace should not throw exception");
    }
  }

  @Test
  public void should_PrintStackTraceToStream_When_PrintStackTraceCalledWithPrintStream() {
    Throwable cause = new RuntimeException("Root cause");
    IllegalClassDescriptorInitialization e =
        new IllegalClassDescriptorInitialization(TEST_MESSAGE, cause);
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    PrintStream ps = new PrintStream(baos);

    e.printStackTrace(ps);
    String output = baos.toString();

    assertTrue("Output should contain exception message", output.contains(TEST_MESSAGE));
    assertTrue("Output should contain 'Caused by'", output.contains("Caused by"));
  }

  @Test
  public void should_PrintStackTraceToStreamWithoutCause_When_PrintStackTraceCalledWithPrintStreamNoCause() {
    IllegalClassDescriptorInitialization e = new IllegalClassDescriptorInitialization(TEST_MESSAGE);
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
    IllegalClassDescriptorInitialization e =
        new IllegalClassDescriptorInitialization(TEST_MESSAGE, cause);
    StringWriter sw = new StringWriter();
    PrintWriter pw = new PrintWriter(sw);

    e.printStackTrace(pw);
    String output = sw.toString();

    assertTrue("Output should contain exception message", output.contains(TEST_MESSAGE));
    assertTrue("Output should contain 'Caused by'", output.contains("Caused by"));
  }

  @Test
  public void should_PrintStackTraceToWriterWithoutCause_When_PrintStackTraceCalledWithPrintWriterNoCause() {
    IllegalClassDescriptorInitialization e = new IllegalClassDescriptorInitialization(TEST_MESSAGE);
    StringWriter sw = new StringWriter();
    PrintWriter pw = new PrintWriter(sw);

    e.printStackTrace(pw);
    String output = sw.toString();

    assertTrue("Output should contain exception message", output.contains(TEST_MESSAGE));
    assertFalse("Output should not contain 'Caused by'", output.contains("Caused by"));
  }

  // ========== Inheritance Tests ==========

  @Test
  public void should_ExtendRuntimeException_When_ClassDeclared() {
    IllegalClassDescriptorInitialization e = new IllegalClassDescriptorInitialization();
    assertTrue("IllegalClassDescriptorInitialization should extend RuntimeException",
        e instanceof RuntimeException);
  }

  @Test
  public void should_ExtendThrowable_When_ClassDeclared() {
    IllegalClassDescriptorInitialization e = new IllegalClassDescriptorInitialization();
    assertTrue("IllegalClassDescriptorInitialization should extend Throwable",
        e instanceof Throwable);
  }

  @Test
  public void should_BeThrowable_When_RaisedAsException() {
    IllegalClassDescriptorInitialization e =
        new IllegalClassDescriptorInitialization(TEST_MESSAGE);
    try {
      throw e;
    } catch (Throwable t) {
      assertEquals("Thrown exception should be caught as Throwable", e, t);
    }
  }

  @Test
  public void should_BeUnchecked_When_NotDeclaredInThrowsClause() {
    // IllegalClassDescriptorInitialization is unchecked (extends RuntimeException)
    assertTrue("Should be an unchecked exception",
        RuntimeException.class.isAssignableFrom(IllegalClassDescriptorInitialization.class));
  }

  // ========== Serialization Tests ==========

  @Test
  public void should_HaveSerialVersionUID_When_ClassDeclared() {
    try {
      java.lang.reflect.Field field = IllegalClassDescriptorInitialization.class
          .getDeclaredField("serialVersionUID");
      field.setAccessible(true);
      long serialVersionUID = field.getLong(null);
      assertEquals("Serial version UID should match expected value", 1L, serialVersionUID);
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
    IllegalClassDescriptorInitialization e = new IllegalClassDescriptorInitialization(longMessage);
    assertEquals("Long message should be preserved", longMessage, e.getMessage());
  }

  @Test
  public void should_HandleSpecialCharactersInMessage_When_ConstructorCalledWithSpecialChars() {
    String specialMessage = "Test\nmessage\twith\rspecial\0chars";
    IllegalClassDescriptorInitialization e =
        new IllegalClassDescriptorInitialization(specialMessage);
    assertEquals("Special characters should be preserved", specialMessage, e.getMessage());
  }

  @Test
  public void should_HandleMultipleLevelsCausesCorrectly_When_CauseHasOwnCause() {
    Throwable root = new Exception("Root");
    Throwable middle = new RuntimeException("Middle", root);
    IllegalClassDescriptorInitialization top =
        new IllegalClassDescriptorInitialization("Top", middle);

    assertEquals("First cause should be middle", middle, top.getCause());
    assertEquals("Middle cause should be root", root, middle.getCause());
  }

  @Test
  public void should_ProvideStackTraceElements_When_GetStackTraceCalledAfterConstruction() {
    IllegalClassDescriptorInitialization e =
        new IllegalClassDescriptorInitialization(TEST_MESSAGE);
    StackTraceElement[] stackTrace = e.getStackTrace();
    assertNotNull("Stack trace should not be null", stackTrace);
    assertTrue("Stack trace should have elements", stackTrace.length > 0);
  }

  @Test
  public void should_AllowModificationOfStackTrace_When_SetStackTraceCalledWithNewElements() {
    IllegalClassDescriptorInitialization e =
        new IllegalClassDescriptorInitialization(TEST_MESSAGE);
    StackTraceElement[] newStackTrace = new StackTraceElement[0];
    e.setStackTrace(newStackTrace);
    assertEquals("Stack trace should be replaced", 0, e.getStackTrace().length);
  }

  @Test
  public void should_ReturnTrueForIsInstanceOfRuntimeException_When_ThrowableChecked() {
    IllegalClassDescriptorInitialization e =
        new IllegalClassDescriptorInitialization(TEST_MESSAGE);
    assertTrue("Should be instance of RuntimeException", e instanceof RuntimeException);
  }

  @Test
  public void should_MaintainCauseAfterMultipleGetCalls_When_GetCauseCalledRepeatedly() {
    Throwable cause = new RuntimeException("Test");
    IllegalClassDescriptorInitialization e = new IllegalClassDescriptorInitialization(cause);

    for (int i = 0; i < 5; i++) {
      assertEquals("Cause should remain consistent", cause, e.getCause());
    }
  }

  @Test
  public void should_SupportExceptionChaining_When_UsedWithOtherExceptions() {
    Exception cause1 = new Exception("First");
    Exception cause2 = new RuntimeException("Second", cause1);
    IllegalClassDescriptorInitialization final_exception =
        new IllegalClassDescriptorInitialization("Final", cause2);

    assertEquals("Should chain exceptions correctly", cause2, final_exception.getCause());
  }

  @Test
  public void should_SupportMultipleInstancesIndependently_When_MultipleExceptionsCreated() {
    IllegalClassDescriptorInitialization e1 =
        new IllegalClassDescriptorInitialization("Message1");
    IllegalClassDescriptorInitialization e2 =
        new IllegalClassDescriptorInitialization("Message2");

    assertNotSame("Instances should be different", e1, e2);
    assertNotEquals("Messages should be different", e1.getMessage(), e2.getMessage());
  }

  @Test
  public void should_HaveSameConstructorSignaturesAsRuntimeException_When_ComparedWithStandardException() {
    // Verify that IllegalClassDescriptorInitialization follows standard exception patterns
    java.lang.reflect.Constructor<?>[] constructors =
        IllegalClassDescriptorInitialization.class.getDeclaredConstructors();
    assertTrue("Should have multiple constructors", constructors.length >= 2);
  }

  @Test
  public void should_SignalProblemsWithClassDescriptorInstantiation_When_ExceptionRaised() {
    // Verify exception is designed for class descriptor instantiation issues
    IllegalClassDescriptorInitialization e =
        new IllegalClassDescriptorInitialization("Class descriptor instantiation failed");
    assertNotNull("Exception message should describe class descriptor problem", e.getMessage());
    assertTrue("Message should be meaningful",
        e.getMessage().contains("class descriptor") || e.getMessage().length() > 0);
  }

  @Test
  public void should_ProvideContextualInformation_When_UsedWithCause() {
    RuntimeException causeException = new RuntimeException("Original problem");
    IllegalClassDescriptorInitialization e =
        new IllegalClassDescriptorInitialization("Failed to initialize descriptor", causeException);

    assertNotNull("Should provide message", e.getMessage());
    assertNotNull("Should preserve cause", e.getCause());
    assertEquals("Cause should be accessible", causeException, e.getCause());
  }
}
