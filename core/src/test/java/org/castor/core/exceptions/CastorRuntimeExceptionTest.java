/*
 * Copyright 2007 Ralf Joachim
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
package org.castor.core.exceptions;

import static org.junit.Assert.*;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.ByteArrayOutputStream;
import java.io.StringWriter;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.After;
import org.junit.Test;

/**
 * Comprehensive test suite for CastorRuntimeException covering all constructors,
 * methods, and edge cases to achieve >95% code coverage.
 */
public class CastorRuntimeExceptionTest {

  private CastorRuntimeException _exception;
  private static final String TEST_MESSAGE = "Test exception message";

  @Before
  public void setUp() {
    _exception = new CastorRuntimeException();
  }

  @After
  public void tearDown() {
    _exception = null;
  }

  // ========== Constructor Tests ==========

  @Test
  public void should_CreateExceptionWithoutMessage_When_DefaultConstructorCalled() {
    CastorRuntimeException e = new CastorRuntimeException();
    assertNotNull("Exception should not be null", e);
    assertNull("Exception message should be null", e.getMessage());
    assertNull("Exception cause should be null", e.getCause());
  }

  @Test
  public void should_CreateExceptionWithMessage_When_ConstructorCalledWithMessage() {
    CastorRuntimeException e = new CastorRuntimeException(TEST_MESSAGE);
    assertNotNull("Exception should not be null", e);
    assertEquals("Exception message should match", TEST_MESSAGE, e.getMessage());
    assertNull("Exception cause should be null", e.getCause());
  }

  @Test
  public void should_CreateExceptionWithEmptyMessage_When_ConstructorCalledWithEmptyString() {
    CastorRuntimeException e = new CastorRuntimeException("");
    assertNotNull("Exception should not be null", e);
    assertEquals("Exception message should be empty", "", e.getMessage());
  }

  @Test
  public void should_CreateExceptionWithNullMessage_When_ConstructorCalledWithNull() {
    CastorRuntimeException e = new CastorRuntimeException((String) null);
    assertNotNull("Exception should not be null", e);
    assertNull("Exception message should be null", e.getMessage());
  }

  @Test
  public void should_CreateExceptionWithCauseThrowable_When_ConstructorCalledWithThrowable() {
    Throwable cause = new Exception("Root cause");
    CastorRuntimeException e = new CastorRuntimeException(cause);
    assertNotNull("Exception should not be null", e);
    assertEquals("Exception cause should match", cause, e.getCause());
    assertEquals("Exception message should be cause message", cause.getMessage(), e.getMessage());
  }

  @Test
  public void should_CreateExceptionWithNullCause_When_ConstructorCalledWithNullThrowable() {
    CastorRuntimeException e = new CastorRuntimeException((Throwable) null);
    assertNotNull("Exception should not be null", e);
    assertNull("Exception cause should be null", e.getCause());
    assertNull("Exception message should be null", e.getMessage());
  }

  @Test
  public void should_CreateExceptionWithMessageAndCause_When_ConstructorCalledWithBoth() {
    Throwable cause = new RuntimeException("Root cause");
    CastorRuntimeException e = new CastorRuntimeException(TEST_MESSAGE, cause);
    assertNotNull("Exception should not be null", e);
    assertEquals("Exception message should match", TEST_MESSAGE, e.getMessage());
    assertEquals("Exception cause should match", cause, e.getCause());
  }

  @Test
  public void should_CreateExceptionWithMessageAndNullCause_When_ConstructorCalledWithMessageAndNull() {
    CastorRuntimeException e = new CastorRuntimeException(TEST_MESSAGE, null);
    assertNotNull("Exception should not be null", e);
    assertEquals("Exception message should match", TEST_MESSAGE, e.getMessage());
    assertNull("Exception cause should be null", e.getCause());
  }

  @Test
  public void should_CreateExceptionWithNullMessageAndCause_When_ConstructorCalledWithNullAndCause() {
    Throwable cause = new IllegalArgumentException("Root cause");
    CastorRuntimeException e = new CastorRuntimeException(null, cause);
    assertNotNull("Exception should not be null", e);
    assertNull("Exception message should be null", e.getMessage());
    assertEquals("Exception cause should match", cause, e.getCause());
  }

  // ========== initCause() Tests ==========

  @Test
  public void should_InitializeCauseSuccessfully_When_InitCauseCalledWithValidThrowable() {
    CastorRuntimeException e = new CastorRuntimeException();
    Throwable cause = new RuntimeException("Test cause");
    Throwable result = e.initCause(cause);
    assertEquals("initCause should return the exception itself", e, result);
    assertEquals("Cause should be set", cause, e.getCause());
  }

  @Test
  public void should_InitializeCauseWithNull_When_InitCauseCalledWithNull() {
    CastorRuntimeException e = new CastorRuntimeException();
    Throwable result = e.initCause(null);
    assertEquals("initCause should return the exception itself", e, result);
    assertNull("Cause should be null", e.getCause());
  }

  @Test
  public void should_ThrowIllegalArgumentException_When_InitCauseCalledWithSelf() {
    CastorRuntimeException e = new CastorRuntimeException();
    try {
      e.initCause(e);
      fail("Should throw IllegalArgumentException when cause is self");
    } catch (IllegalArgumentException ex) {
      assertTrue("Exception should be caught", true);
    }
  }

  @Test
  public void should_ThrowIllegalStateException_When_InitCauseCalledTwice() {
    CastorRuntimeException e = new CastorRuntimeException();
    Throwable cause1 = new RuntimeException("Cause 1");
    e.initCause(cause1);

    Throwable cause2 = new RuntimeException("Cause 2");
    try {
      e.initCause(cause2);
      fail("Should throw IllegalStateException when initCause called twice");
    } catch (IllegalStateException ex) {
      assertTrue("Exception should be caught", true);
    }
  }

  @Test
  public void should_NotAllowInitCauseAgain_When_CauseAlreadyInitializedInConstructor() {
    Throwable cause = new RuntimeException("Initial cause");
    CastorRuntimeException e = new CastorRuntimeException(TEST_MESSAGE, cause);

    try {
      e.initCause(new RuntimeException("Another cause"));
      fail("Should throw IllegalStateException");
    } catch (IllegalStateException ex) {
      assertTrue("Exception should be caught", true);
    }
  }

  @Test
  public void should_PreventCircularCauseChain_When_InitCauseCalledWithSelf() {
    CastorRuntimeException e = new CastorRuntimeException();
    try {
      e.initCause(e);
      fail("Should throw IllegalArgumentException when cause is self");
    } catch (IllegalArgumentException ex) {
      assertTrue("Exception should be caught", true);
    }
  }

  // ========== getCause() Tests ==========

  @Test
  public void should_ReturnNullCause_When_GetCauseCalledOnExceptionWithoutCause() {
    CastorRuntimeException e = new CastorRuntimeException();
    assertNull("Cause should be null", e.getCause());
  }

  @Test
  public void should_ReturnSpecifiedCause_When_GetCauseCalledOnExceptionWithCause() {
    Throwable cause = new Exception("Test cause");
    CastorRuntimeException e = new CastorRuntimeException(cause);
    assertEquals("Cause should match", cause, e.getCause());
  }

  @Test
  public void should_ReturnInitializedCause_When_GetCauseCalledAfterInitCause() {
    CastorRuntimeException e = new CastorRuntimeException();
    Throwable cause = new RuntimeException("Initialized cause");
    e.initCause(cause);
    assertEquals("Cause should match", cause, e.getCause());
  }

  @Test
  public void should_ReturnConsistentCause_When_GetCauseCalledMultipleTimes() {
    Throwable cause = new Exception("Test cause");
    CastorRuntimeException e = new CastorRuntimeException(TEST_MESSAGE, cause);
    assertEquals("First call should return cause", cause, e.getCause());
    assertEquals("Second call should return same cause", cause, e.getCause());
  }

  // ========== printStackTrace() Tests ==========

  @Test
  public void should_PrintStackTrace_When_PrintStackTraceCalledWithoutCause() {
    CastorRuntimeException e = new CastorRuntimeException(TEST_MESSAGE);
    try {
      e.printStackTrace();
    } catch (Exception ex) {
      fail("printStackTrace should not throw exception");
    }
  }

  @Test
  public void should_PrintStackTraceWithCause_When_PrintStackTraceCalledWithCause() {
    Throwable cause = new RuntimeException("Root cause");
    CastorRuntimeException e = new CastorRuntimeException(TEST_MESSAGE, cause);
    try {
      e.printStackTrace();
    } catch (Exception ex) {
      fail("printStackTrace should not throw exception");
    }
  }

  @Test
  public void should_PrintStackTraceToStream_When_PrintStackTraceCalledWithPrintStream() {
    Throwable cause = new RuntimeException("Root cause");
    CastorRuntimeException e = new CastorRuntimeException(TEST_MESSAGE, cause);
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    PrintStream ps = new PrintStream(baos);

    e.printStackTrace(ps);
    String output = baos.toString();

    assertTrue("Output should contain exception message", output.contains(TEST_MESSAGE));
    assertTrue("Output should contain 'Caused by'", output.contains("Caused by"));
  }

  @Test
  public void should_PrintStackTraceToStreamWithoutCause_When_PrintStackTraceCalledWithPrintStreamNoCause() {
    CastorRuntimeException e = new CastorRuntimeException(TEST_MESSAGE);
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
    CastorRuntimeException e = new CastorRuntimeException(TEST_MESSAGE, cause);
    StringWriter sw = new StringWriter();
    PrintWriter pw = new PrintWriter(sw);

    e.printStackTrace(pw);
    String output = sw.toString();

    assertTrue("Output should contain exception message", output.contains(TEST_MESSAGE));
    assertTrue("Output should contain 'Caused by'", output.contains("Caused by"));
  }

  @Test
  public void should_PrintStackTraceToWriterWithoutCause_When_PrintStackTraceCalledWithPrintWriterNoCause() {
    CastorRuntimeException e = new CastorRuntimeException(TEST_MESSAGE);
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
    CastorRuntimeException e = new CastorRuntimeException(TEST_MESSAGE, rootCause);

    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    PrintStream ps = new PrintStream(baos);
    e.printStackTrace(ps);
    String output = baos.toString();

    assertTrue("Output should contain main message", output.contains(TEST_MESSAGE));
    assertTrue("Output should contain root cause message", output.contains("Root cause"));
  }

  @Test
  public void should_HandleNullPrintStream_When_PrintStackThrownWithPrintStreamNull() {
    CastorRuntimeException e = new CastorRuntimeException(TEST_MESSAGE);
    try {
      e.printStackTrace((PrintStream) null);
      fail("Should throw NullPointerException");
    } catch (NullPointerException ex) {
      assertTrue("NullPointerException should be caught", true);
    }
  }

  @Test
  public void should_HandleNullPrintWriter_When_PrintStackThrownWithPrintWriterNull() {
    CastorRuntimeException e = new CastorRuntimeException(TEST_MESSAGE);
    try {
      e.printStackTrace((PrintWriter) null);
      fail("Should throw NullPointerException");
    } catch (NullPointerException ex) {
      assertTrue("NullPointerException should be caught", true);
    }
  }

  // ========== Inheritance Tests ==========

  @Test
  public void should_ExtendRuntimeException_When_ClassDeclared() {
    assertTrue("CastorRuntimeException should extend RuntimeException",
        _exception instanceof RuntimeException);
  }

  @Test
  public void should_ExtendThrowable_When_ClassDeclared() {
    assertTrue("CastorRuntimeException should extend Throwable",
        _exception instanceof Throwable);
  }

  @Test
  public void should_BeThrowable_When_RaisedAsException() {
    CastorRuntimeException e = new CastorRuntimeException(TEST_MESSAGE);
    try {
      throw e;
    } catch (Throwable t) {
      assertEquals("Thrown exception should be caught as Throwable", e, t);
    }
  }

  // ========== Serialization Tests ==========

  @Test
  public void should_HaveSerialVersionUID_When_ClassDeclared() {
    try {
      java.lang.reflect.Field field = CastorRuntimeException.class.getDeclaredField("serialVersionUID");
      field.setAccessible(true);
      long serialVersionUID = field.getLong(null);
      assertEquals("Serial version UID should match expected value", 3984585622253325513L,
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
    CastorRuntimeException e = new CastorRuntimeException(longMessage);
    assertEquals("Long message should be preserved", longMessage, e.getMessage());
  }

  @Test
  public void should_HandleSpecialCharactersInMessage_When_ConstructorCalledWithSpecialChars() {
    String specialMessage = "Test\nmessage\twith\rspecial\0chars";
    CastorRuntimeException e = new CastorRuntimeException(specialMessage);
    assertEquals("Special characters should be preserved", specialMessage, e.getMessage());
  }

  @Test
  public void should_HandleMultipleLevelsCausesCorrectly_When_CauseHasOwnCause() {
    Throwable root = new Exception("Root");
    Throwable middle = new RuntimeException("Middle", root);
    CastorRuntimeException top = new CastorRuntimeException("Top", middle);

    assertEquals("First cause should be middle", middle, top.getCause());
    assertEquals("Middle cause should be root", root, middle.getCause());
  }

  @Test
  public void should_ProvideStackTraceElements_When_GetStackTraceCalledAfterConstruction() {
    CastorRuntimeException e = new CastorRuntimeException(TEST_MESSAGE);
    StackTraceElement[] stackTrace = e.getStackTrace();
    assertNotNull("Stack trace should not be null", stackTrace);
    assertTrue("Stack trace should have elements", stackTrace.length > 0);
  }

  @Test
  public void should_AllowModificationOfStackTrace_When_SetStackTraceCalledWithNewElements() {
    CastorRuntimeException e = new CastorRuntimeException(TEST_MESSAGE);
    StackTraceElement[] originalStackTrace = e.getStackTrace();

    StackTraceElement[] newStackTrace = new StackTraceElement[0];
    e.setStackTrace(newStackTrace);

    assertEquals("Stack trace should be replaced", 0, e.getStackTrace().length);
  }

  @Test
  public void should_ReturnTrueForIsInstanceOfRuntimeException_When_ThrowableChecked() {
    CastorRuntimeException e = new CastorRuntimeException(TEST_MESSAGE);
    assertTrue("Should be instance of RuntimeException", e instanceof RuntimeException);
  }

  @Test
  public void should_NotHaveCheckedExceptionSemantics_When_UsedWithoutDeclaration() {
    // This test verifies that CastorRuntimeException can be thrown without throws clause
    try {
      throwCastorRuntimeException();
    } catch (CastorRuntimeException e) {
      assertEquals("Caught exception should have correct message", TEST_MESSAGE, e.getMessage());
    }
  }

  private void throwCastorRuntimeException() {
    throw new CastorRuntimeException(TEST_MESSAGE);
  }

  @Test
  public void should_MaintainCauseAfterMultipleGetCalls_When_GetCauseCalledRepeatedly() {
    Throwable cause = new RuntimeException("Test");
    CastorRuntimeException e = new CastorRuntimeException(cause);

    for (int i = 0; i < 5; i++) {
      assertEquals("Cause should remain consistent", cause, e.getCause());
    }
  }

  @Test
  public void should_SupportExceptionChaining_When_UsedWithOtherExceptions() {
    Exception cause1 = new Exception("First");
    Exception cause2 = new RuntimeException("Second", cause1);
    CastorRuntimeException final_exception = new CastorRuntimeException("Final", cause2);

    assertEquals("Should chain exceptions correctly", cause2, final_exception.getCause());
  }
}
