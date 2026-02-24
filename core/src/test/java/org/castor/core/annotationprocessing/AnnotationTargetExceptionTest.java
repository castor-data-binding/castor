/*
 * Copyright 2009 Werner Guttmann
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
package org.castor.core.annotationprocessing;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Unit tests for {@link AnnotationTargetException}.
 *
 * @author Test Generator
 * @since 1.4.2
 */
public class AnnotationTargetExceptionTest {

  private static final String TEST_MESSAGE = "Test exception message";
  private static final String TEST_CAUSE_MESSAGE = "Test cause message";

  /**
   * should_CreateException_When_NoArgsConstructorCalled
   */
  @Test
  public void should_CreateException_When_NoArgsConstructorCalled() {
    AnnotationTargetException exception = new AnnotationTargetException();
    assertNotNull(exception);
    assertNull(exception.getMessage());
    assertNull(exception.getCause());
  }

  /**
   * should_CreateExceptionWithMessage_When_MessageConstructorCalled
   */
  @Test
  public void should_CreateExceptionWithMessage_When_MessageConstructorCalled() {
    AnnotationTargetException exception = new AnnotationTargetException(TEST_MESSAGE);
    assertNotNull(exception);
    assertEquals(TEST_MESSAGE, exception.getMessage());
    assertNull(exception.getCause());
  }

  /**
   * should_CreateExceptionWithCause_When_CauseConstructorCalled
   */
  @Test
  public void should_CreateExceptionWithCause_When_CauseConstructorCalled() {
    Throwable cause = new RuntimeException(TEST_CAUSE_MESSAGE);
    AnnotationTargetException exception = new AnnotationTargetException(cause);
    assertNotNull(exception);
    assertEquals(cause, exception.getCause());
    assertEquals(TEST_CAUSE_MESSAGE, exception.getCause().getMessage());
  }

  /**
   * should_CreateExceptionWithMessageAndCause_When_FullConstructorCalled
   */
  @Test
  public void should_CreateExceptionWithMessageAndCause_When_FullConstructorCalled() {
    Throwable cause = new RuntimeException(TEST_CAUSE_MESSAGE);
    AnnotationTargetException exception =
        new AnnotationTargetException(TEST_MESSAGE, cause);
    assertNotNull(exception);
    assertEquals(TEST_MESSAGE, exception.getMessage());
    assertEquals(cause, exception.getCause());
    assertEquals(TEST_CAUSE_MESSAGE, exception.getCause().getMessage());
  }

  /**
   * should_ExtendException_When_ClassInstantiated
   */
  @Test
  public void should_ExtendException_When_ClassInstantiated() {
    assertTrue(Exception.class.isAssignableFrom(AnnotationTargetException.class));
  }

  /**
   * should_HandleNullMessage_When_NullMessageProvided
   */
  @Test
  public void should_HandleNullMessage_When_NullMessageProvided() {
    AnnotationTargetException exception = new AnnotationTargetException((String) null);
    assertNotNull(exception);
    assertNull(exception.getMessage());
  }

  /**
   * should_HandleNullCause_When_NullCauseProvided
   */
  @Test
  public void should_HandleNullCause_When_NullCauseProvided() {
    AnnotationTargetException exception = new AnnotationTargetException((Throwable) null);
    assertNotNull(exception);
    assertNull(exception.getCause());
  }

  /**
   * should_PreserveStackTrace_When_ExceptionThrown
   */
  @Test
  public void should_PreserveStackTrace_When_ExceptionThrown() {
    try {
      throw new AnnotationTargetException(TEST_MESSAGE);
    } catch (AnnotationTargetException e) {
      assertNotNull(e.getStackTrace());
      assertTrue(e.getStackTrace().length > 0);
      assertEquals(TEST_MESSAGE, e.getMessage());
    }
  }

  /**
   * should_ChainExceptions_When_CauseAndMessageProvided
   */
  @Test
  public void should_ChainExceptions_When_CauseAndMessageProvided() {
    Throwable originalCause = new IllegalArgumentException("Original cause");
    AnnotationTargetException exception =
        new AnnotationTargetException("Wrapper message", originalCause);

    assertNotNull(exception);
    assertEquals("Wrapper message", exception.getMessage());
    assertEquals(originalCause, exception.getCause());
    assertTrue(exception.getCause() instanceof IllegalArgumentException);
  }

  /**
   * should_SupportStringConversion_When_ToStringCalled
   */
  @Test
  public void should_SupportStringConversion_When_ToStringCalled() {
    AnnotationTargetException exception =
        new AnnotationTargetException(TEST_MESSAGE);

    String toString = exception.toString();
    assertNotNull(toString);
    assertTrue(toString.contains("AnnotationTargetException"));
    assertTrue(toString.contains(TEST_MESSAGE));
  }
}
