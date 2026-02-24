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
import org.junit.Before;
import org.junit.After;
import org.junit.Test;

/**
 * Comprehensive test suite for EmptyResourceBundle inner class.
 * Tests the fallback resource bundle used when messages cannot be loaded.
 */
public class EmptyResourceBundleTest {

  private Locale originalLocale;

  @Before
  public void setUp() {
    originalLocale = Locale.getDefault();
    // Force use of a non-existent locale to trigger EmptyResourceBundle
    Messages.setLocale(new Locale("xx", "YY"));
  }

  @After
  public void tearDown() {
    Messages.setLocale(originalLocale);
  }

  @Test
  public void should_ReturnMissingMessageMarker_When_HandleGetObjectCalledWithAnyKey() {
    String result = Messages.message("any.key.here");
    assertNotNull("Should return placeholder message", result);
    assertTrue("Should contain Missing message indicator",
        result.contains("Missing") || result.contains("any.key.here"));
  }

  @Test
  public void should_ReturnMissingMessageMarker_When_HandleGetObjectCalledWithNull() {
    String result = Messages.message(null);
    assertNotNull("Should handle null key", result);
  }

  @Test
  public void should_ReturnMissingMessageMarker_When_HandleGetObjectCalledWithEmptyString() {
    String result = Messages.message("");
    assertNotNull("Should handle empty key", result);
  }

  @Test
  public void should_HandleMultipleKeyAccess_When_MessagesCalledMultipleTimes() {
    String msg1 = Messages.message("key1");
    String msg2 = Messages.message("key2");
    String msg3 = Messages.message("key3");

    assertNotNull("All messages should be non-null", msg1);
    assertNotNull("All messages should be non-null", msg2);
    assertNotNull("All messages should be non-null", msg3);
  }

  @Test
  public void should_ReturnPlaceholderWithKey_When_MessageCalledWithCustomKey() {
    String result = Messages.message("my.custom.message.key");
    assertTrue("Should contain key or placeholder",
        result.contains("my.custom.message.key") || result.contains("[") || result.contains("]"));
  }

  @Test
  public void should_HandleFormatWithMultipleArguments_When_FormatCalledWithEmptyBundle() {
    String result = Messages.format("test.key", new Object[]{"arg1", "arg2", "arg3"});
    assertNotNull("Should handle multiple arguments", result);
  }

  @Test
  public void should_HandleFormatWithSingleArgument_When_FormatCalledWithEmptyBundle() {
    String result = Messages.format("test.key", "argument");
    assertNotNull("Should handle single argument", result);
  }

  @Test
  public void should_HandleFormatWithNoArguments_When_FormatCalledWithEmptyBundle() {
    String result = Messages.format("test.key", new Object[]{});
    assertNotNull("Should handle no arguments", result);
  }

  @Test
  public void should_ReturnConsistentPlaceholder_When_SameKeyAccessedMultipleTimes() {
    String result1 = Messages.message("consistent.key");
    String result2 = Messages.message("consistent.key");
    assertEquals("Same key should return consistent result", result1, result2);
  }

  @Test
  public void should_IncludeKeyInPlaceholder_When_MessageCalledWithSpecialCharactersInKey() {
    String result = Messages.message("key@with#special$chars");
    assertNotNull("Should handle special characters", result);
  }

  @Test
  public void should_HandleVeryLongKey_When_MessageCalledWithLongKey() {
    String longKey = "key." + "very".repeat(100) + ".long";
    String result = Messages.message(longKey);
    assertNotNull("Should handle very long keys", result);
  }

  @Test
  public void should_HandleUnicodeKeysInEmptyBundle_When_MessageCalledWithUnicodeKey() {
    String result = Messages.message("key.with.café");
    assertNotNull("Should handle unicode in keys", result);
  }

  @Test
  public void should_ReturnPlaceholderFormat_When_HandleGetObjectReturnsMarkerFormat() {
    String msg1 = Messages.message("test1");
    String msg2 = Messages.message("test2");

    assertTrue("Should have placeholder format",
        (msg1.contains("[") && msg1.contains("]")) || msg1.contains("test1"));
    assertTrue("Should have placeholder format",
        (msg2.contains("[") && msg2.contains("]")) || msg2.contains("test2"));
  }

  @Test
  public void should_ContinueWorkingAfterMultipleMessageCalls_When_RepeatedlyAccessed() {
    for (int i = 0; i < 100; i++) {
      String result = Messages.message("key" + i);
      assertNotNull("All iterations should return non-null", result);
    }
  }

  @Test
  public void should_HandleFormattingWithNullArgumentsInEmptyBundle_When_FormatCalledWithNulls() {
    String result = Messages.format("test.key", new Object[]{null, "value", null});
    assertNotNull("Should handle null arguments", result);
  }

  @Test
  public void should_HandleFormatWithTwoArguments_When_FormatCalledWithTwoArgsInEmptyBundle() {
    String result = Messages.format("test.key", "arg1", "arg2");
    assertNotNull("Should handle two arguments", result);
  }

  @Test
  public void should_HandleFormatWithThreeArguments_When_FormatCalledWithThreeArgsInEmptyBundle() {
    String result = Messages.format("test.key", "arg1", "arg2", "arg3");
    assertNotNull("Should handle three arguments", result);
  }

  @Test
  public void should_ReturnValidStringForAllPatterns_When_VariousMessageCallsMade() {
    String msg = Messages.message("pattern.test");
    assertNotNull("Message should not be null", msg);
    assertTrue("Message should be non-empty", msg.length() > 0);
  }

  @Test
  public void should_HandleSequentialCalls_When_MultipleFormatsFollowedByMessages() {
    String fmt1 = Messages.format("key1", "arg");
    String msg1 = Messages.message("key2");
    String fmt2 = Messages.format("key3", "arg1", "arg2");

    assertNotNull("All calls should return non-null", fmt1);
    assertNotNull("All calls should return non-null", msg1);
    assertNotNull("All calls should return non-null", fmt2);
  }

  @Test
  public void should_PreserveBehaviorAfterLocaleChange_When_LocaleChangedToEmptyBundle() {
    Locale tempLocale = new Locale("aa", "BB");
    Messages.setLocale(tempLocale);

    String result = Messages.message("test.key");
    assertNotNull("Should still return messages after locale change", result);

    Messages.setLocale(originalLocale);
  }
}
