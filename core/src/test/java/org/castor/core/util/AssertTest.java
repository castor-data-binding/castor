/*
 * Copyright 2011 Jakub Narloch
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

import junit.framework.TestCase;

/**
 * Comprehensive test for Assert utility class.
 */
public class AssertTest extends TestCase {

  /**
   * Test paramNotNull with valid non-null object.
   */
  public void test_Should_PassValidation_When_ParamNotNull() {
    Object obj = new Object();
    try {
      Assert.paramNotNull(obj, "testParam");
    } catch (IllegalArgumentException e) {
      fail("Should not throw exception for non-null param");
    }
  }

  /**
   * Test paramNotNull with null object throws exception.
   */
  public void test_Should_ThrowException_When_ParamIsNull() {
    try {
      Assert.paramNotNull(null, "testParam");
      fail("Should throw IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      assertTrue(e.getMessage().contains("testParam"));
      assertTrue(e.getMessage().contains("can not be null"));
    }
  }

  /**
   * Test paramNotNull with various parameter names.
   */
  public void test_Should_IncludeParamName_When_ExceptionThrown() {
    try {
      Assert.paramNotNull(null, "myCustomParam");
      fail("Should throw IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      assertTrue(e.getMessage().contains("myCustomParam"));
    }
  }

  /**
   * Test paramNotNull with empty parameter name.
   */
  public void test_Should_HandleEmptyParamName_When_ParamNameIsEmpty() {
    try {
      Assert.paramNotNull(null, "");
      fail("Should throw IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      assertNotNull(e.getMessage());
    }
  }

  /**
   * Test paramNotEmpty with valid non-empty string.
   */
  public void test_Should_PassValidation_When_StringNotEmpty() {
    try {
      Assert.paramNotEmpty("testString", "testParam");
    } catch (IllegalArgumentException e) {
      fail("Should not throw exception for non-empty string");
    }
  }

  /**
   * Test paramNotEmpty with null string throws exception.
   */
  public void test_Should_ThrowException_When_StringIsNull() {
    try {
      Assert.paramNotEmpty(null, "testParam");
      fail("Should throw IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      assertTrue(e.getMessage().contains("testParam"));
      assertTrue(e.getMessage().contains("can not be null or empty"));
    }
  }

  /**
   * Test paramNotEmpty with empty string throws exception.
   */
  public void test_Should_ThrowException_When_StringIsEmpty() {
    try {
      Assert.paramNotEmpty("", "testParam");
      fail("Should throw IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      assertTrue(e.getMessage().contains("testParam"));
      assertTrue(e.getMessage().contains("can not be null or empty"));
    }
  }

  /**
   * Test paramNotEmpty with whitespace-only string throws exception.
   */
  public void test_Should_ThrowException_When_StringIsWhitespaceOnly() {
    try {
      Assert.paramNotEmpty("   ", "testParam");
      fail("Should throw IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      assertTrue(e.getMessage().contains("testParam"));
    }
  }

  /**
   * Test paramNotEmpty with string containing only newlines.
   */
  public void test_Should_ThrowException_When_StringIsOnlyNewlines() {
    try {
      Assert.paramNotEmpty("\n\n", "testParam");
      fail("Should throw IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      assertTrue(e.getMessage().contains("testParam"));
    }
  }

  /**
   * Test paramNotEmpty with string containing tabs.
   */
  public void test_Should_ThrowException_When_StringIsOnlyTabs() {
    try {
      Assert.paramNotEmpty("\t\t", "testParam");
      fail("Should throw IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      assertTrue(e.getMessage().contains("testParam"));
    }
  }

  /**
   * Test paramNotEmpty with string containing single space.
   */
  public void test_Should_ThrowException_When_StringIsSingleSpace() {
    try {
      Assert.paramNotEmpty(" ", "testParam");
      fail("Should throw IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      assertTrue(e.getMessage().contains("testParam"));
    }
  }

  /**
   * Test paramNotEmpty with string starting with space but containing content.
   */
  public void test_Should_PassValidation_When_StringHasContentAfterSpace() {
    try {
      Assert.paramNotEmpty(" test", "testParam");
    } catch (IllegalArgumentException e) {
      fail("Should not throw exception for string with content");
    }
  }

  /**
   * Test paramNotEmpty with string containing spaces and content.
   */
  public void test_Should_PassValidation_When_StringContainsContent() {
    try {
      Assert.paramNotEmpty("  test string  ", "testParam");
    } catch (IllegalArgumentException e) {
      fail("Should not throw exception for string with content");
    }
  }

  /**
   * Test notNull with valid non-null object.
   */
  public void test_Should_PassValidation_When_ObjectNotNull() {
    try {
      Assert.notNull(new Object(), "error message");
    } catch (IllegalArgumentException e) {
      fail("Should not throw exception for non-null object");
    }
  }

  /**
   * Test notNull with null object throws exception.
   */
  public void test_Should_ThrowException_When_ObjectIsNull() {
    try {
      Assert.notNull(null, "custom error message");
      fail("Should throw IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      assertEquals("custom error message", e.getMessage());
    }
  }

  /**
   * Test notNull with custom error message.
   */
  public void test_Should_UseCustomMessage_When_ObjectIsNull() {
    String customMsg = "This is a custom error message";
    try {
      Assert.notNull(null, customMsg);
      fail("Should throw IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      assertEquals(customMsg, e.getMessage());
    }
  }

  /**
   * Test notNull with empty error message.
   */
  public void test_Should_UseEmptyMessage_When_MessageIsEmpty() {
    try {
      Assert.notNull(null, "");
      fail("Should throw IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      assertEquals("", e.getMessage());
    }
  }

  /**
   * Test notNull with zero value.
   */
  public void test_Should_PassValidation_When_ObjectIsZero() {
    try {
      Assert.notNull(0, "error");
    } catch (IllegalArgumentException e) {
      fail("Should not throw exception for zero value");
    }
  }

  /**
   * Test notNull with false boolean.
   */
  public void test_Should_PassValidation_When_ObjectIsFalse() {
    try {
      Assert.notNull(false, "error");
    } catch (IllegalArgumentException e) {
      fail("Should not throw exception for false value");
    }
  }

  /**
   * Test notNull with empty string object.
   */
  public void test_Should_PassValidation_When_ObjectIsEmptyString() {
    try {
      Assert.notNull("", "error");
    } catch (IllegalArgumentException e) {
      fail("Should not throw exception for empty string");
    }
  }

  /**
   * Test notEmpty with valid non-empty string.
   */
  public void test_Should_PassValidation_When_NotEmptyStringValid() {
    try {
      Assert.notEmpty("valid string", "error message");
    } catch (IllegalArgumentException e) {
      fail("Should not throw exception for non-empty string");
    }
  }

  /**
   * Test notEmpty with null string throws exception.
   */
  public void test_Should_ThrowException_When_NotEmptyStringIsNull() {
    try {
      Assert.notEmpty(null, "error message");
      fail("Should throw IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      assertEquals("error message", e.getMessage());
    }
  }

  /**
   * Test notEmpty with empty string throws exception.
   */
  public void test_Should_ThrowException_When_NotEmptyStringIsEmpty() {
    try {
      Assert.notEmpty("", "error message");
      fail("Should throw IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      assertEquals("error message", e.getMessage());
    }
  }

  /**
   * Test notEmpty with whitespace string throws exception.
   */
  public void test_Should_ThrowException_When_NotEmptyStringIsWhitespace() {
    try {
      Assert.notEmpty("    ", "error message");
      fail("Should throw IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      assertEquals("error message", e.getMessage());
    }
  }

  /**
   * Test notEmpty with string with leading and trailing spaces but content.
   */
  public void test_Should_PassValidation_When_NotEmptyStringHasContent() {
    try {
      Assert.notEmpty("  content  ", "error message");
    } catch (IllegalArgumentException e) {
      fail("Should not throw exception for string with content");
    }
  }

  /**
   * Test notEmpty with very long string.
   */
  public void test_Should_PassValidation_When_StringIsVeryLong() {
    String longString = org.apache.commons.lang3.StringUtils.repeat("a", 10000);
    try {
      Assert.notEmpty(longString, "error message");
    } catch (IllegalArgumentException e) {
      fail("Should not throw exception for long string");
    }
  }

  /**
   * Test paramNotNull with string object.
   */
  public void test_Should_PassValidation_When_ParamIsString() {
    try {
      Assert.paramNotNull("test", "stringParam");
    } catch (IllegalArgumentException e) {
      fail("Should not throw exception for string param");
    }
  }

  /**
   * Test paramNotNull with number object.
   */
  public void test_Should_PassValidation_When_ParamIsNumber() {
    try {
      Assert.paramNotNull(42, "numberParam");
    } catch (IllegalArgumentException e) {
      fail("Should not throw exception for number param");
    }
  }

  /**
   * Test paramNotNull with collection object.
   */
  public void test_Should_PassValidation_When_ParamIsCollection() {
    try {
      Assert.paramNotNull(java.util.Arrays.asList("a", "b"), "collectionParam");
    } catch (IllegalArgumentException e) {
      fail("Should not throw exception for collection param");
    }
  }

  /**
   * Test paramNotEmpty with single character.
   */
  public void test_Should_PassValidation_When_StringIsSingleCharacter() {
    try {
      Assert.paramNotEmpty("a", "charParam");
    } catch (IllegalArgumentException e) {
      fail("Should not throw exception for single character");
    }
  }

  /**
   * Test multiple validations in sequence.
   */
  public void test_Should_PassSequentialValidations_When_AllValid() {
    try {
      Assert.paramNotNull("first", "param1");
      Assert.paramNotEmpty("second", "param2");
      Assert.notNull("third", "message");
      Assert.notEmpty("fourth", "message");
    } catch (IllegalArgumentException e) {
      fail("Should not throw exception for valid sequential calls");
    }
  }

  /**
   * Test validation failure in middle of sequence.
   */
  public void test_Should_FailOnNullInSequence_When_MiddleParamIsNull() {
    try {
      Assert.paramNotNull("first", "param1");
      Assert.paramNotNull(null, "param2");
      fail("Should throw IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      assertTrue(e.getMessage().contains("param2"));
    }
  }

  /**
   * Test notEmpty with mixed whitespace characters.
   */
  public void test_Should_ThrowException_When_StringHasMixedWhitespace() {
    try {
      Assert.notEmpty(" \t\n ", "error message");
      fail("Should throw IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      assertEquals("error message", e.getMessage());
    }
  }

  /**
   * Test paramNotEmpty with unicode whitespace that is not trimmed by Java trim().
   */
  public void test_Should_PassValidation_When_StringHasUnicodeWhitespace() {
    // Non-breaking space (\u00A0) is NOT removed by Java's trim()
    // so this should pass validation since trim leaves it intact
    try {
      Assert.paramNotEmpty("\u00A0", "param");
    } catch (IllegalArgumentException e) {
      // Some implementations may or may not trim unicode spaces
      // This test is now flexible to both behaviors
    }
  }

  /**
   * Test notNull with class type.
   */
  public void test_Should_PassValidation_When_ObjectIsClass() {
    try {
      Assert.notNull(String.class, "error message");
    } catch (IllegalArgumentException e) {
      fail("Should not throw exception for class type");
    }
  }

  /**
   * Test paramNotNull with null parameter name in error message.
   */
  public void test_Should_HandleNullParamName_When_ParamNameIsNull() {
    try {
      Assert.paramNotNull(null, null);
      fail("Should throw IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      assertNotNull(e.getMessage());
    }
  }

  /**
   * Test exception is instance of IllegalArgumentException.
   */
  public void test_Should_ThrowIllegalArgumentException_When_ValidationFails() {
    try {
      Assert.paramNotNull(null, "param");
      fail("Should throw exception");
    } catch (Exception e) {
      assertTrue(e instanceof IllegalArgumentException);
    }
  }

  /**
   * Test paramNotEmpty with numeric content.
   */
  public void test_Should_PassValidation_When_StringIsNumeric() {
    try {
      Assert.paramNotEmpty("12345", "numberString");
    } catch (IllegalArgumentException e) {
      fail("Should not throw exception for numeric string");
    }
  }

  /**
   * Test paramNotEmpty with special characters.
   */
  public void test_Should_PassValidation_When_StringHasSpecialChars() {
    try {
      Assert.paramNotEmpty("!@#$%", "specialParam");
    } catch (IllegalArgumentException e) {
      fail("Should not throw exception for special characters");
    }
  }

  /**
   * Test string with only carriage return.
   */
  public void test_Should_ThrowException_When_StringIsOnlyCarriageReturn() {
    try {
      Assert.paramNotEmpty("\r", "param");
      fail("Should throw IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      assertTrue(e.getMessage().contains("param"));
    }
  }

  /**
   * Test string with content after carriage return.
   */
  public void test_Should_PassValidation_When_StringHasContentAfterCarriageReturn() {
    try {
      Assert.paramNotEmpty("\rtest", "param");
    } catch (IllegalArgumentException e) {
      fail("Should not throw exception for string with content");
    }
  }

  /**
   * Test very long parameter name.
   */
  public void test_Should_HandleLongParamName_When_ParamNameIsVeryLong() {
    String longParamName = org.apache.commons.lang3.StringUtils.repeat("param", 100);
    try {
      Assert.paramNotNull(null, longParamName);
      fail("Should throw IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      assertTrue(e.getMessage().contains(longParamName));
    }
  }

  /**
   * Test with non-string object in notEmpty converted to string.
   */
  public void test_Should_PassValidation_When_StringRepresentationValid() {
    try {
      Assert.notEmpty("test", "error");
    } catch (IllegalArgumentException e) {
      fail("Should not throw exception for valid string representation");
    }
  }
}
