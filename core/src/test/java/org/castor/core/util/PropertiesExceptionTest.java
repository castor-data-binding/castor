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
package org.castor.core.util;

import static org.junit.Assert.*;

import org.castor.core.exceptions.CastorRuntimeException;
import org.junit.Test;

/**
 * Unit tests for {@link PropertiesException}.
 */
public class PropertiesExceptionTest {

  private static final String TEST_MESSAGE = "Test exception message";
  private static final String TEST_CAUSE_MESSAGE = "Test cause message";

  /**
   * should_CreateException_When_NoArgsConstructorCalled
   */
  @Test
  public void should_CreateException_When_NoArgsConstructorCalled() {
    PropertiesException exception = new PropertiesException();
    assertNotNull(exception);
    assertNull(exception.getMessage());
    assertNull(exception.getCause());
  }

  /**
   * should_CreateExceptionWithMessage_When_MessageConstructorCalled
   */
  @Test
  public void should_CreateExceptionWithMessage_When_MessageConstructorCalled() {
    PropertiesException exception = new PropertiesException(TEST_MESSAGE);
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
    PropertiesException exception = new PropertiesException(cause);
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
    PropertiesException exception = new PropertiesException(TEST_MESSAGE, cause);
    assertNotNull(exception);
    assertEquals(TEST_MESSAGE, exception.getMessage());
    assertEquals(cause, exception.getCause());
    assertEquals(TEST_CAUSE_MESSAGE, exception.getCause().getMessage());
  }

  /**
   * should_ExtendCastorRuntimeException_When_ClassInstantiated
   */
  @Test
  public void should_ExtendCastorRuntimeException_When_ClassInstantiated() {
    assertTrue(CastorRuntimeException.class.isAssignableFrom(PropertiesException.class));
    assertTrue(RuntimeException.class.isAssignableFrom(PropertiesException.class));
  }

  /**
   * should_HaveCorrectSerialVersionUID_When_ClassLoaded
   */
  @Test
  public void should_HaveCorrectSerialVersionUID_When_ClassLoaded() throws Exception {
    java.lang.reflect.Field field = PropertiesException.class.getDeclaredField("serialVersionUID");
    field.setAccessible(true);
    assertEquals(4446761026170253291L, field.getLong(null));
  }

  /**
   * should_HandleNullMessage_When_NullMessageProvided
   */
  @Test
  public void should_HandleNullMessage_When_NullMessageProvided() {
    PropertiesException exception = new PropertiesException((String) null);
    assertNotNull(exception);
    assertNull(exception.getMessage());
  }

  /**
   * should_HandleNullCause_When_NullCauseProvided
   */
  @Test
  public void should_HandleNullCause_When_NullCauseProvided() {
    PropertiesException exception = new PropertiesException((Throwable) null);
    assertNotNull(exception);
    assertNull(exception.getCause());
  }

  /**
   * should_PreserveStackTrace_When_ExceptionThrown
   */
  @Test
  public void should_PreserveStackTrace_When_ExceptionThrown() {
    try {
      throw new PropertiesException(TEST_MESSAGE);
    } catch (PropertiesException e) {
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
    PropertiesException exception = new PropertiesException("Wrapper message", originalCause);

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
    PropertiesException exception = new PropertiesException(TEST_MESSAGE);

    String toString = exception.toString();
    assertNotNull(toString);
    assertTrue(toString.contains("PropertiesException"));
    assertTrue(toString.contains(TEST_MESSAGE));
  }

  /**
   * should_BeCatchableAsRuntimeException_When_ThrowingPropertiesException
   */
  @Test
  public void should_BeCatchableAsRuntimeException_When_ThrowingPropertiesException() {
    try {
      throw new PropertiesException(TEST_MESSAGE);
    } catch (RuntimeException e) {
      assertTrue(e instanceof PropertiesException);
      assertEquals(TEST_MESSAGE, e.getMessage());
    }
  }

  /**
   * should_BeCatchableAsCastorRuntimeException_When_ThrowingPropertiesException
   */
  @Test
  public void should_BeCatchableAsCastorRuntimeException_When_ThrowingPropertiesException() {
    try {
      throw new PropertiesException(TEST_MESSAGE);
    } catch (CastorRuntimeException e) {
      assertTrue(e instanceof PropertiesException);
      assertEquals(TEST_MESSAGE, e.getMessage());
    }
  }
}
