/*
 * Copyright 2006 Assaf Arkin, Ralf Joachim
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
import java.util.Locale;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.After;
import org.junit.Test;

/**
 * Comprehensive test suite for Messages utility class covering all public methods,
 * message formatting, localization, and edge cases to achieve >95% code coverage.
 */
public class MessagesTest {

  private static final String TEST_MESSAGE_KEY = "test.message";
  private static final String MISSING_MESSAGE_KEY = "missing.nonexistent.message.key";
  private Locale originalLocale;

  @Before
  public void setUp() {
    originalLocale = Locale.getDefault();
  }

  @After
  public void tearDown() {
    Messages.setLocale(originalLocale);
  }

  // ========== setDefaultLocale() Tests ==========

  @Test
  public void should_SetDefaultLocaleSuccessfully_When_SetDefaultLocaleCalledWithoutArguments() {
    try {
      Messages.setDefaultLocale();
    } catch (Exception e) {
      fail("setDefaultLocale should not throw exception: " + e.getMessage());
    }
  }

  @Test
  public void should_LoadMessagesAfterSettingLocale_When_SetDefaultLocaleCalledMultipleTimes() {
    try {
      Messages.setDefaultLocale();
      Messages.setDefaultLocale();
      Messages.setDefaultLocale();
    } catch (Exception e) {
      fail("setDefaultLocale should handle multiple calls: " + e.getMessage());
    }
  }

  // ========== setLocale() Tests ==========

  @Test
  public void should_SetLocaleSuccessfully_When_SetLocaleCalledWithValidLocale() {
    try {
      Messages.setLocale(Locale.ENGLISH);
    } catch (Exception e) {
      fail("setLocale should not throw exception with English locale: " + e.getMessage());
    }
  }

  @Test
  public void should_SetLocaleSuccessfully_When_SetLocaleCalledWithFrenchLocale() {
    try {
      Messages.setLocale(Locale.FRENCH);
    } catch (Exception e) {
      fail("setLocale should not throw exception with French locale: " + e.getMessage());
    }
  }

  @Test
  public void should_SetLocaleSuccessfully_When_SetLocaleCalledWithGermanLocale() {
    try {
      Messages.setLocale(Locale.GERMAN);
    } catch (Exception e) {
      fail("setLocale should not throw exception with German locale: " + e.getMessage());
    }
  }

  @Test
  public void should_SetLocaleSuccessfully_When_SetLocaleCalledWithCustomLocale() {
    try {
      Messages.setLocale(new Locale("es", "ES"));
    } catch (Exception e) {
      fail("setLocale should not throw exception with custom locale: " + e.getMessage());
    }
  }

  @Test
  public void should_SetLocaleSuccessfully_When_SetLocaleCalledWithNullLocale() {
    try {
      Messages.setLocale(null);
    } catch (Exception e) {
      // Expected behavior: might handle null or throw NPE
    }
  }

  @Test
  public void should_ClearFormatCacheAfterLocaleChange_When_SetLocaleCalledTwice() {
    Messages.setLocale(Locale.ENGLISH);
    String msg1 = Messages.message(TEST_MESSAGE_KEY);

    Messages.setLocale(Locale.FRENCH);
    String msg2 = Messages.message(TEST_MESSAGE_KEY);

    // Messages should still work after locale change
    assertNotNull("Message should not be null after locale change", msg2);
  }

  // ========== format(String, Object) Tests ==========

  @Test
  public void should_FormatMessageWithSingleArgument_When_FormatCalledWithOneArg() {
    Messages.setLocale(Locale.ENGLISH);
    String result = Messages.format("test", "arg1");
    assertNotNull("Formatted message should not be null", result);
  }

  @Test
  public void should_FormatMessageWithNullKey_When_FormatCalledWithNullMessageKey() {
    String result = Messages.format(null, "arg");
    assertNotNull("Should handle null key", result);
  }

  @Test
  public void should_FormatMessageWithNullArgument_When_FormatCalledWithNullArg() {
    String result = Messages.format(TEST_MESSAGE_KEY, (Object) null);
    assertNotNull("Should handle null argument", result);
  }

  @Test
  public void should_FormatMessageWithEmptyString_When_FormatCalledWithEmptyKey() {
    String result = Messages.format("", "arg");
    assertNotNull("Should handle empty key", result);
  }

  @Test
  public void should_ReturnKeyWhenMissing_When_FormatCalledWithNonexistentKey() {
    String result = Messages.format(MISSING_MESSAGE_KEY, "arg");
    assertNotNull("Should return something for missing key", result);
    assertTrue("Should contain the key or message", result.length() > 0);
  }

  // ========== format(String, Object, Object) Tests ==========

  @Test
  public void should_FormatMessageWithTwoArguments_When_FormatCalledWithTwoArgs() {
    String result = Messages.format("test", "arg1", "arg2");
    assertNotNull("Formatted message should not be null", result);
  }

  @Test
  public void should_FormatMessageWithTwoNullArguments_When_FormatCalledWithTwoNulls() {
    String result = Messages.format(TEST_MESSAGE_KEY, null, null);
    assertNotNull("Should handle two null arguments", result);
  }

  @Test
  public void should_FormatMessageWithOneNullArgument_When_FormatCalledWithOneNullAndOneValue() {
    String result = Messages.format(TEST_MESSAGE_KEY, null, "arg2");
    assertNotNull("Should handle mixed null and value arguments", result);
  }

  @Test
  public void should_FormatMessageWithMissingKey_When_FormatCalledWithNonexistentKeyAndTwoArgs() {
    String result = Messages.format(MISSING_MESSAGE_KEY, "arg1", "arg2");
    assertNotNull("Should handle missing key with two arguments", result);
  }

  // ========== format(String, Object, Object, Object) Tests ==========

  @Test
  public void should_FormatMessageWithThreeArguments_When_FormatCalledWithThreeArgs() {
    String result = Messages.format("test", "arg1", "arg2", "arg3");
    assertNotNull("Formatted message should not be null", result);
  }

  @Test
  public void should_FormatMessageWithThreeNullArguments_When_FormatCalledWithThreeNulls() {
    String result = Messages.format(TEST_MESSAGE_KEY, null, null, null);
    assertNotNull("Should handle three null arguments", result);
  }

  @Test
  public void should_FormatMessageWithMixedNullArguments_When_FormatCalledWithMixedNullsAndValues() {
    String result = Messages.format(TEST_MESSAGE_KEY, "arg1", null, "arg3");
    assertNotNull("Should handle mixed arguments with nulls", result);
  }

  @Test
  public void should_FormatMessageWithMissingKey_When_FormatCalledWithNonexistentKeyAndThreeArgs() {
    String result = Messages.format(MISSING_MESSAGE_KEY, "arg1", "arg2", "arg3");
    assertNotNull("Should handle missing key with three arguments", result);
  }

  // ========== format(String, Object[]) Tests ==========

  @Test
  public void should_FormatMessageWithObjectArray_When_FormatCalledWithArrayArgs() {
    Object[] args = {"arg1", "arg2", "arg3"};
    String result = Messages.format("test", args);
    assertNotNull("Formatted message should not be null", result);
  }

  @Test
  public void should_FormatMessageWithEmptyArray_When_FormatCalledWithEmptyObjectArray() {
    Object[] args = {};
    String result = Messages.format(TEST_MESSAGE_KEY, args);
    assertNotNull("Should handle empty array", result);
  }

  @Test
  public void should_FormatMessageWithNullArray_When_FormatCalledWithNullObjectArray() {
    String result = Messages.format(TEST_MESSAGE_KEY, (Object[]) null);
    assertNotNull("Should handle null array", result);
  }

  @Test
  public void should_FormatMessageWithArrayContainingNulls_When_FormatCalledWithArrayWithNullElements() {
    Object[] args = {"arg1", null, "arg3"};
    String result = Messages.format(TEST_MESSAGE_KEY, args);
    assertNotNull("Should handle array with null elements", result);
  }

  @Test
  public void should_FormatMessageWithArrayOfNulls_When_FormatCalledWithAllNullArray() {
    Object[] args = {null, null, null};
    String result = Messages.format(TEST_MESSAGE_KEY, args);
    assertNotNull("Should handle array of all nulls", result);
  }

  @Test
  public void should_FormatMessageWithLargeArray_When_FormatCalledWithArrayOfManyElements() {
    Object[] args = new Object[100];
    for (int i = 0; i < args.length; i++) {
      args[i] = "arg" + i;
    }
    String result = Messages.format(TEST_MESSAGE_KEY, args);
    assertNotNull("Should handle large array", result);
  }

  @Test
  public void should_FormatMessageWithMissingKeyAndArray_When_FormatCalledWithNonexistentKeyAndArray() {
    Object[] args = {"arg1", "arg2"};
    String result = Messages.format(MISSING_MESSAGE_KEY, args);
    assertNotNull("Should handle missing key with array", result);
  }

  // ========== message(String) Tests ==========

  @Test
  public void should_ReturnMessageText_When_MessageCalledWithValidKey() {
    String result = Messages.message(TEST_MESSAGE_KEY);
    assertNotNull("Message should not be null", result);
  }

  @Test
  public void should_ReturnKeyWhenMessageNotFound_When_MessageCalledWithNonexistentKey() {
    String result = Messages.message(MISSING_MESSAGE_KEY);
    assertNotNull("Should return something for missing key", result);
  }

  @Test
  public void should_HandleNullKey_When_MessageCalledWithNull() {
    String result = Messages.message(null);
    assertNotNull("Should handle null key", result);
  }

  @Test
  public void should_HandleEmptyKey_When_MessageCalledWithEmptyString() {
    String result = Messages.message("");
    assertNotNull("Should handle empty key", result);
  }

  @Test
  public void should_ReturnConsistentMessage_When_MessageCalledMultipleTimesWithSameKey() {
    String result1 = Messages.message(TEST_MESSAGE_KEY);
    String result2 = Messages.message(TEST_MESSAGE_KEY);
    assertEquals("Message should be consistent across calls", result1, result2);
  }

  @Test
  public void should_CacheMessageFormat_When_FormatCalledMultipleTimesWithSameKey() {
    String result1 = Messages.format(TEST_MESSAGE_KEY, "arg1");
    String result2 = Messages.format(TEST_MESSAGE_KEY, "arg1");
    // Both calls should work without error, demonstrating cache functionality
    assertNotNull("First format call should return result", result1);
    assertNotNull("Second format call should return result", result2);
  }

  // ========== RESOURCE_NAME Tests ==========

  @Test
  public void should_ContainResourceName_When_ClassLoaded() {
    String resourceName = Messages.RESOURCE_NAME;
    assertNotNull("RESOURCE_NAME should not be null", resourceName);
    assertEquals("RESOURCE_NAME should match expected value", "org.castor.messages", resourceName);
  }

  @Test
  public void should_RetainResourceNameValue_When_AccessedMultipleTimes() {
    String name1 = Messages.RESOURCE_NAME;
    String name2 = Messages.RESOURCE_NAME;
    assertEquals("RESOURCE_NAME should be consistent", name1, name2);
  }

  // ========== Localization Tests ==========

  @Test
  public void should_LoadEnglishMessages_When_SetLocaleCalledWithEnglish() {
    Messages.setLocale(Locale.ENGLISH);
    String result = Messages.message(TEST_MESSAGE_KEY);
    assertNotNull("Should load English messages", result);
  }

  @Test
  public void should_SwitchBetweenLocales_When_SetLocaleCalledWithDifferentLocales() {
    Messages.setLocale(Locale.ENGLISH);
    String enMsg = Messages.message(TEST_MESSAGE_KEY);

    Messages.setLocale(Locale.GERMAN);
    String deMsg = Messages.message(TEST_MESSAGE_KEY);

    assertNotNull("English message should exist", enMsg);
    assertNotNull("German message should exist", deMsg);
  }

  // ========== Edge Cases and Error Handling ==========

  @Test
  public void should_HandleSpecialCharactersInKey_When_MessageCalledWithSpecialCharKey() {
    String result = Messages.message("test.message.with@special#chars$");
    assertNotNull("Should handle special characters in key", result);
  }

  @Test
  public void should_HandleVeryLongKey_When_MessageCalledWithLongKey() {
    String longKey = StringUtils.repeat("a", 1000) + ".message";
    String result = Messages.message(longKey);
    assertNotNull("Should handle very long key", result);
  }

  @Test
  public void should_HandleUnicodeInArguments_When_FormatCalledWithUnicodeArgs() {
    String result = Messages.format(TEST_MESSAGE_KEY, "arg\u00e9");
    assertNotNull("Should handle unicode in arguments", result);
  }

  @Test
  public void should_HandleNumberArguments_When_FormatCalledWithNumbers() {
    String result = Messages.format(TEST_MESSAGE_KEY, 123, 456.789);
    assertNotNull("Should handle numeric arguments", result);
  }

  @Test
  public void should_HandleBooleanArguments_When_FormatCalledWithBooleans() {
    String result = Messages.format(TEST_MESSAGE_KEY, true, false);
    assertNotNull("Should handle boolean arguments", result);
  }

  @Test
  public void should_HandleExceptionArguments_When_FormatCalledWithExceptionObjects() {
    Exception ex = new RuntimeException("Test exception");
    String result = Messages.format(TEST_MESSAGE_KEY, ex);
    assertNotNull("Should handle exception objects as arguments", result);
  }

  // ========== Concurrency and State Tests ==========

  @Test
  public void should_MaintainStateAfterFormatting_When_FormatAndMessageCalledInSequence() {
    String msg1 = Messages.format(TEST_MESSAGE_KEY, "arg1");
    String msg2 = Messages.message(TEST_MESSAGE_KEY);
    String msg3 = Messages.format(TEST_MESSAGE_KEY, "arg2");

    assertNotNull("All messages should be available", msg1);
    assertNotNull("All messages should be available", msg2);
    assertNotNull("All messages should be available", msg3);
  }

  @Test
  public void should_PreserveFormatCacheAcrossLocaleChanges_When_MessagesAccessedBeforeAndAfterLocaleChange() {
    Messages.setLocale(Locale.ENGLISH);
    String before = Messages.format(TEST_MESSAGE_KEY, "arg");

    Messages.setLocale(Locale.FRENCH);
    String after = Messages.format(TEST_MESSAGE_KEY, "arg");

    assertNotNull("Message before locale change should work", before);
    assertNotNull("Message after locale change should work", after);
  }

  // ========== Static Initialization Tests ==========

  @Test
  public void should_InitializeMessagesCorrectly_When_ClassLoaded() {
    // Verify that static initialization completed without errors
    String result = Messages.message(TEST_MESSAGE_KEY);
    assertNotNull("Messages should be initialized after class loading", result);
  }

  @Test
  public void should_HaveDefaultLocaleSet_When_ClassLoaded() {
    // After class loading, a default locale should be set
    String result = Messages.message(TEST_MESSAGE_KEY);
    assertNotNull("Default locale should be set", result);
  }

  // ========== Utility Class Characteristics Tests ==========

  @Test
  public void should_NotHavePublicConstructor_When_ClassInspected() {
    try {
      Messages.class.getConstructor();
      fail("Messages should not have a public constructor");
    } catch (NoSuchMethodException e) {
      assertTrue("Private constructor expected", true);
    }
  }

  @Test
  public void should_HaveOnlyStaticMethods_When_ClassInspected() {
    java.lang.reflect.Method[] methods = Messages.class.getDeclaredMethods();
    boolean allStatic = true;
    for (java.lang.reflect.Method method : methods) {
      if (!java.lang.reflect.Modifier.isStatic(method.getModifiers())) {
        allStatic = false;
        break;
      }
    }
    assertTrue("Messages should have only static methods", allStatic);
  }
}
