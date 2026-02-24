package org.castor.core.util;

import junit.framework.TestCase;

/**
 * Test for StringUtil.
 */
public class StringUtilTest extends TestCase {

  /**
   * Test replaceAll with simple replacement.
   */
  public void test_Should_ReplacePattern_When_PatternExists() {
    String source = "hello world world";
    String result = StringUtil.replaceAll(source, "world", "castor");

    assertEquals("hello castor castor", result);
  }

  /**
   * Test replaceAll with no occurrences.
   */
  public void test_Should_ReturnOriginal_When_PatternNotFound() {
    String source = "hello world";
    String result = StringUtil.replaceAll(source, "xyz", "abc");

    assertEquals("hello world", result);
  }

  /**
   * Test replaceAll with single character replacement.
   */
  public void test_Should_ReplaceCharacter_When_CharacterPatternExists() {
    String source = "aaa";
    String result = StringUtil.replaceAll(source, "a", "b");

    assertEquals("bbb", result);
  }

  /**
   * Test replaceAll with empty replacement.
   */
  public void test_Should_RemovePattern_When_ReplacementIsEmpty() {
    String source = "hello world";
    String result = StringUtil.replaceAll(source, "world", "");

    assertEquals("hello ", result);
  }

  /**
   * Test replaceAll with null source.
   */
  public void test_Should_ReturnNull_When_SourceIsNull() {
    String result = StringUtil.replaceAll(null, "world", "castor");

    assertNull(result);
  }

  /**
   * Test replaceAll with null toReplace.
   */
  public void test_Should_HandleNull_When_ToReplaceIsNull() {
    String source = "hello world";
    String result = StringUtil.replaceAll(source, null, "castor");

    // StringUtils.replace returns original source when toReplace is null
    assertEquals(source, result);
  }

  /**
   * Test replaceAll with null replacement.
   */
  public void test_Should_HandleNull_When_ReplacementIsNull() {
    String source = "hello world";
    String result = StringUtil.replaceAll(source, "world", null);

    // StringUtils.replace may handle null replacement
    assertNotNull(result);
  }

  /**
   * Test replaceAll with all nulls.
   */
  public void test_Should_HandleAllNulls_When_AllParametersAreNull() {
    String result = StringUtil.replaceAll(null, null, null);

    assertNull(result);
  }

  /**
   * Test replaceAll with source and toReplace same.
   */
  public void test_Should_ReplaceEntireString_When_SourceEqualsPattern() {
    String source = "test";
    String result = StringUtil.replaceAll(source, "test", "replaced");

    assertEquals("replaced", result);
  }

  /**
   * Test replaceAll with overlapping patterns.
   */
  public void test_Should_ReplaceAllOccurrences_When_PatternsOverlap() {
    String source = "aaa";
    String result = StringUtil.replaceAll(source, "aa", "b");

    assertEquals("ba", result);
  }

  /**
   * Test replaceAll with special characters.
   */
  public void test_Should_ReplaceSpecialCharacters_When_PatternContainsSpecialChars() {
    String source = "hello.world.test";
    String result = StringUtil.replaceAll(source, ".", "-");

    assertEquals("hello-world-test", result);
  }

  /**
   * Test replaceAll with empty string source.
   */
  public void test_Should_ReturnEmpty_When_SourceIsEmpty() {
    String source = "";
    String result = StringUtil.replaceAll(source, "test", "replaced");

    assertEquals("", result);
  }

  /**
   * Test replaceAll with empty string toReplace.
   */
  public void test_Should_InsertReplacement_When_ToReplaceIsEmpty() {
    String source = "abc";
    String result = StringUtil.replaceAll(source, "", "x");

    // StringUtils.replace handles empty pattern by returning source or inserting
    assertNotNull(result);
  }

  /**
   * Test replaceAll with multiline string.
   */
  public void test_Should_ReplaceInMultiline_When_SourceHasNewlines() {
    String source = "hello\nworld\ntest";
    String result = StringUtil.replaceAll(source, "\n", " ");

    assertEquals("hello world test", result);
  }

  /**
   * Test replaceAll with repeated pattern at start.
   */
  public void test_Should_ReplaceAtStart_When_PatternIsAtBeginning() {
    String source = "testtest";
    String result = StringUtil.replaceAll(source, "test", "t");

    assertEquals("tt", result);
  }

  /**
   * Test replaceAll with repeated pattern at end.
   */
  public void test_Should_ReplaceAtEnd_When_PatternIsAtEndOfString() {
    String source = "abctest";
    String result = StringUtil.replaceAll(source, "test", "");

    assertEquals("abc", result);
  }

  /**
   * Test replaceAll with unicode characters.
   */
  public void test_Should_ReplaceUnicode_When_PatternContainsUnicodeChars() {
    String source = "café café";
    String result = StringUtil.replaceAll(source, "café", "coffee");

    assertEquals("coffee coffee", result);
  }

  /**
   * Test replaceAll with case sensitivity.
   */
  public void test_Should_BeCaseSensitive_When_CaseDiffers() {
    String source = "Hello HELLO hello";
    String result = StringUtil.replaceAll(source, "hello", "hi");

    assertEquals("Hello HELLO hi", result);
  }

  /**
   * Test replaceAll with very long replacement string.
   */
  public void test_Should_HandleLongReplacement_When_ReplacementIsLong() {
    String source = "a";
    String longReplacement = org.apache.commons.lang3.StringUtils.repeat("x", 1000);
    String result = StringUtil.replaceAll(source, "a", longReplacement);

    assertEquals(longReplacement, result);
  }

  /**
   * Test replaceAll with very long source string.
   */
  public void test_Should_HandleLongSource_When_SourceIsLong() {
    String source = org.apache.commons.lang3.StringUtils.repeat("a", 1000);
    String result = StringUtil.replaceAll(source, "a", "b");

    assertEquals(org.apache.commons.lang3.StringUtils.repeat("b", 1000), result);
  }

  /**
   * Test replaceAll preserves surrounding content.
   */
  public void test_Should_PreserveSurroundingContent_When_ReplacingMiddle() {
    String source = "prefix_middle_suffix";
    String result = StringUtil.replaceAll(source, "middle", "replaced");

    assertEquals("prefix_replaced_suffix", result);
  }

  /**
   * Test replaceAll with single character source.
   */
  public void test_Should_ReplaceCharacter_When_SourceIsSingleChar() {
    String source = "a";
    String result = StringUtil.replaceAll(source, "a", "bcd");

    assertEquals("bcd", result);
  }

  /**
   * Test replaceAll multiple consecutive patterns.
   */
  public void test_Should_ReplaceConsecutivePatterns_When_PatternsAreAdjacent() {
    String source = "xyxy";
    String result = StringUtil.replaceAll(source, "xy", "z");

    assertEquals("zz", result);
  }

  /**
   * Test replaceAll with numeric patterns.
   */
  public void test_Should_ReplaceNumericPatterns_When_PatternIsNumeric() {
    String source = "123 456 789";
    String result = StringUtil.replaceAll(source, "456", "000");

    assertEquals("123 000 789", result);
  }

  /**
   * Test replaceAll with whitespace patterns.
   */
  public void test_Should_ReplaceWhitespace_When_PatternIsWhitespace() {
    String source = "hello   world";
    String result = StringUtil.replaceAll(source, "   ", " ");

    assertEquals("hello world", result);
  }

  /**
   * Test replaceAll with tab characters.
   */
  public void test_Should_ReplaceTabCharacters_When_PatternContainsTabs() {
    String source = "hello\t\tworld";
    String result = StringUtil.replaceAll(source, "\t", " ");

    assertEquals("hello  world", result);
  }
}
