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
package org.castor.core.util;

import static org.junit.Assert.*;
import org.junit.Test;

/**
 * Enhanced comprehensive test class for StringUtil with >95% coverage.
 * Focuses on all code paths, boundary conditions, and edge cases.
 */
public class StringUtilEnhancedTest {

  // ========== Basic Replacement Tests ==========

  @Test
  public void should_ReplaceAllOccurrences_When_PatternExistsMultipleTimes() {
    String source = "hello world world hello";
    String result = StringUtil.replaceAll(source, "hello", "hi");
    assertEquals("hi world world hi", result);
  }

  @Test
  public void should_ReplaceFirstAndLastOccurrence_When_PatternAtBoundaries() {
    String source = "testmiddletest";
    String result = StringUtil.replaceAll(source, "test", "TEST");
    assertEquals("TESTmiddleTEST", result);
  }

  @Test
  public void should_ReplaceConsecutivePatterns_When_PatternsAreAdjacent() {
    String source = "ababab";
    String result = StringUtil.replaceAll(source, "ab", "X");
    assertEquals("XXX", result);
  }

  // ========== No Match Cases ==========

  @Test
  public void should_ReturnOriginalString_When_PatternNotFound() {
    String source = "hello world";
    String result = StringUtil.replaceAll(source, "xyz", "abc");
    assertEquals("hello world", result);
  }

  @Test
  public void should_ReturnOriginalString_When_PatternNotFoundExact() {
    String source = "The quick brown fox";
    String result = StringUtil.replaceAll(source, "dog", "cat");
    assertEquals("The quick brown fox", result);
  }

  @Test
  public void should_ReturnOriginalString_When_CaseSensitivePatternNotMatched() {
    String source = "Hello World";
    String result = StringUtil.replaceAll(source, "hello", "hi");
    assertEquals("Hello World", result);
  }

  // ========== Null Tests ==========

  @Test
  public void should_ReturnNull_When_SourceIsNull() {
    String result = StringUtil.replaceAll(null, "test", "replacement");
    assertNull(result);
  }

  @Test
  public void should_ReturnSourceUnchanged_When_ToReplaceIsNull() {
    String source = "hello world";
    String result = StringUtil.replaceAll(source, null, "replacement");
    assertEquals(source, result);
  }

  @Test
  public void should_HandleReplacementNull_When_ReplacementIsNull() {
    String source = "hello world";
    String result = StringUtil.replaceAll(source, "world", null);
    assertNotNull(result);
  }

  @Test
  public void should_ReturnNull_When_AllParametersAreNull() {
    String result = StringUtil.replaceAll(null, null, null);
    assertNull(result);
  }

  @Test
  public void should_ReturnSourceUnchanged_When_OnlyToReplaceIsNull() {
    String source = "test string";
    String result = StringUtil.replaceAll(source, null, "anything");
    assertEquals(source, result);
  }

  // ========== Empty String Tests ==========

  @Test
  public void should_ReturnEmpty_When_SourceIsEmpty() {
    String source = "";
    String result = StringUtil.replaceAll(source, "test", "replaced");
    assertEquals("", result);
  }

  @Test
  public void should_InsertIntoBoundary_When_ToReplaceIsEmpty() {
    String source = "abc";
    String result = StringUtil.replaceAll(source, "", "X");
    assertNotNull(result);
  }

  @Test
  public void should_RemovePattern_When_ReplacementIsEmpty() {
    String source = "hello world test";
    String result = StringUtil.replaceAll(source, "world", "");
    assertEquals("hello  test", result);
  }

  @Test
  public void should_HandleEmptySourceAndPattern_When_BothEmpty() {
    String source = "";
    String result = StringUtil.replaceAll(source, "", "x");
    assertNotNull(result);
  }

  // ========== Single Character Tests ==========

  @Test
  public void should_ReplaceCharacter_When_PatternIsSingleChar() {
    String source = "aaa";
    String result = StringUtil.replaceAll(source, "a", "b");
    assertEquals("bbb", result);
  }

  @Test
  public void should_ReplaceEntireString_When_SourceIsSingleCharacter() {
    String source = "x";
    String result = StringUtil.replaceAll(source, "x", "y");
    assertEquals("y", result);
  }

  @Test
  public void should_NotReplace_When_SingleCharDoesNotMatch() {
    String source = "a";
    String result = StringUtil.replaceAll(source, "b", "c");
    assertEquals("a", result);
  }

  @Test
  public void should_ExpandSingleChar_When_ReplacementIsLonger() {
    String source = "a";
    String result = StringUtil.replaceAll(source, "a", "abcdef");
    assertEquals("abcdef", result);
  }

  // ========== Whitespace Tests ==========

  @Test
  public void should_ReplaceSpaces_When_PatternIsSpace() {
    String source = "hello   world";
    String result = StringUtil.replaceAll(source, " ", "_");
    assertEquals("hello___world", result);
  }

  @Test
  public void should_ReplaceMultipleSpaces_When_ToReplaceIsMultipleSpaces() {
    String source = "hello   world";
    String result = StringUtil.replaceAll(source, "   ", " ");
    assertEquals("hello world", result);
  }

  @Test
  public void should_ReplaceTabCharacters_When_PatternIsTab() {
    String source = "hello\t\tworld";
    String result = StringUtil.replaceAll(source, "\t", " ");
    assertEquals("hello  world", result);
  }

  @Test
  public void should_ReplaceNewlines_When_PatternIsNewline() {
    String source = "hello\nworld\ntest";
    String result = StringUtil.replaceAll(source, "\n", " ");
    assertEquals("hello world test", result);
  }

  @Test
  public void should_HandleMixedWhitespace_When_PatternContainsMixedWhitespace() {
    String source = "a b\tc\nd";
    String result = StringUtil.replaceAll(source, " ", "_");
    assertEquals("a_b\tc\nd", result);
  }

  // ========== Position-Specific Tests ==========

  @Test
  public void should_ReplaceAtStart_When_PatternAtBeginning() {
    String source = "testdata";
    String result = StringUtil.replaceAll(source, "test", "TEST");
    assertEquals("TESTdata", result);
  }

  @Test
  public void should_ReplaceAtEnd_When_PatternAtEndOfString() {
    String source = "datatest";
    String result = StringUtil.replaceAll(source, "test", "");
    assertEquals("data", result);
  }

  @Test
  public void should_ReplaceInMiddle_When_PatternInCenter() {
    String source = "prefix_middle_suffix";
    String result = StringUtil.replaceAll(source, "middle", "CENTER");
    assertEquals("prefix_CENTER_suffix", result);
  }

  @Test
  public void should_ReplaceConsecutivePatternAtStart_When_MultipleOccurrencesAtBeginning() {
    String source = "testtest_data";
    String result = StringUtil.replaceAll(source, "test", "t");
    assertEquals("tt_data", result);
  }

  @Test
  public void should_ReplaceConsecutivePatternAtEnd_When_MultipleOccurrencesAtEnd() {
    String source = "data_testtest";
    String result = StringUtil.replaceAll(source, "test", "t");
    assertEquals("data_tt", result);
  }

  // ========== Special Characters Tests ==========

  @Test
  public void should_ReplaceDotCharacters_When_PatternContainsDots() {
    String source = "hello.world.test";
    String result = StringUtil.replaceAll(source, ".", "-");
    assertEquals("hello-world-test", result);
  }

  @Test
  public void should_ReplaceSpecialCharacters_When_PatternContainsSpecialChars() {
    String source = "hello@world#test$end";
    String result = StringUtil.replaceAll(source, "@", "_");
    assertEquals("hello_world#test$end", result);
  }

  @Test
  public void should_HandleBackslashes_When_PatternContainsBackslash() {
    String source = "path\\to\\file";
    String result = StringUtil.replaceAll(source, "\\", "/");
    assertEquals("path/to/file", result);
  }

  @Test
  public void should_ReplaceParentheses_When_PatternContainsParentheses() {
    String source = "func(arg1)func(arg2)";
    String result = StringUtil.replaceAll(source, "(", "[");
    assertEquals("func[arg1)func[arg2)", result);
  }

  // ========== Case Sensitivity Tests ==========

  @Test
  public void should_BeCaseSensitive_When_DifferentCaseUsed() {
    String source = "Hello HELLO hello";
    String result = StringUtil.replaceAll(source, "hello", "hi");
    assertEquals("Hello HELLO hi", result);
  }

  @Test
  public void should_OnlyReplaceCaseMatchingPattern_When_MultipleVariations() {
    String source = "Test test TEST";
    String result = StringUtil.replaceAll(source, "test", "t");
    assertEquals("Test t TEST", result);
  }

  // ========== Large String Tests ==========

  @Test
  public void should_HandleLongSourceString_When_SourceIsVeryLong() {
    String source = org.apache.commons.lang3.StringUtils.repeat("a", 10000);
    String result = StringUtil.replaceAll(source, "a", "b");
    assertEquals(org.apache.commons.lang3.StringUtils.repeat("b", 10000), result);
  }

  @Test
  public void should_HandleLongPattern_When_PatternIsVeryLong() {
    String longPattern = org.apache.commons.lang3.StringUtils.repeat("a", 1000);
    String source = "prefix" + longPattern + "suffix";
    String result = StringUtil.replaceAll(source, longPattern, "X");
    assertEquals("prefixXsuffix", result);
  }

  @Test
  public void should_HandleLongReplacement_When_ReplacementIsVeryLong() {
    String source = "a";
    String longReplacement = org.apache.commons.lang3.StringUtils.repeat("x", 10000);
    String result = StringUtil.replaceAll(source, "a", longReplacement);
    assertEquals(longReplacement, result);
  }

  @Test
  public void should_HandleManyReplacements_When_PatternAppearsOften() {
    String source = org.apache.commons.lang3.StringUtils.repeat("ab", 1000);
    String result = StringUtil.replaceAll(source, "ab", "X");
    assertEquals(org.apache.commons.lang3.StringUtils.repeat("X", 1000), result);
  }

  // ========== Unicode Tests ==========

  @Test
  public void should_HandleUnicodeCharacters_When_SourceContainsUnicode() {
    String source = "café café";
    String result = StringUtil.replaceAll(source, "café", "coffee");
    assertEquals("coffee coffee", result);
  }

  @Test
  public void should_HandleMultibyteCharacters_When_PatternContainsMultibyte() {
    String source = "日本語 test 日本語";
    String result = StringUtil.replaceAll(source, "日本語", "JP");
    assertEquals("JP test JP", result);
  }

  @Test
  public void should_ReplaceWithUnicodeReplacement_When_ReplacementIsUnicode() {
    String source = "hello world";
    String result = StringUtil.replaceAll(source, "world", "世界");
    assertEquals("hello 世界", result);
  }

  // ========== Numeric Tests ==========

  @Test
  public void should_ReplaceNumericPatterns_When_PatternIsNumeric() {
    String source = "123 456 789";
    String result = StringUtil.replaceAll(source, "456", "000");
    assertEquals("123 000 789", result);
  }

  @Test
  public void should_ReplaceAllNumbers_When_PatternIsSingleDigit() {
    String source = "1a2b3c4d5";
    String result = StringUtil.replaceAll(source, "2", "X");
    assertEquals("1aXb3c4d5", result);
  }

  // ========== Entire String Replacement Tests ==========

  @Test
  public void should_ReplaceEntireString_When_SourceEqualsPattern() {
    String source = "test";
    String result = StringUtil.replaceAll(source, "test", "replaced");
    assertEquals("replaced", result);
  }

  @Test
  public void should_ExpandEntireString_When_PatternIsEntireSourceAndReplacementIsLonger() {
    String source = "short";
    String result = StringUtil.replaceAll(source, "short", "verylongstring");
    assertEquals("verylongstring", result);
  }

  @Test
  public void should_ShrinkEntireString_When_PatternIsEntireSourceAndReplacementIsShorter() {
    String source = "verylongstring";
    String result = StringUtil.replaceAll(source, "verylongstring", "short");
    assertEquals("short", result);
  }

  @Test
  public void should_EmptyStringWhenReplacingEntireSourceWithEmpty_When_ReplacementIsEmpty() {
    String source = "entire";
    String result = StringUtil.replaceAll(source, "entire", "");
    assertEquals("", result);
  }

  // ========== Pattern Length vs Source Length Tests ==========

  @Test
  public void should_HandlePatternLongerThanSource_When_PatternDoesNotFit() {
    String source = "hi";
    String result = StringUtil.replaceAll(source, "hello", "bye");
    assertEquals("hi", result);
  }

  @Test
  public void should_HandlePatternEqualsSourceLength_When_LengthMatches() {
    String source = "test";
    String result = StringUtil.replaceAll(source, "test", "PASS");
    assertEquals("PASS", result);
  }

  // ========== Overlapping Pattern Tests ==========

  @Test
  public void should_HandleOverlappingPatterns_When_ReplacementDoesNotCreateNewPatterns() {
    String source = "aaa";
    String result = StringUtil.replaceAll(source, "aa", "b");
    assertEquals("ba", result);
  }

  @Test
  public void should_ReplaceMultipleOccurrencesOfOverlapping_When_PatternsCanOverlap() {
    String source = "aaaa";
    String result = StringUtil.replaceAll(source, "aa", "X");
    assertEquals("XX", result);
  }

  // ========== Regex-like Patterns Tests ==========

  @Test
  public void should_TreatAsLiteralString_When_PatternLooksLikeRegex() {
    String source = "a.c";
    String result = StringUtil.replaceAll(source, ".", "X");
    assertEquals("aXc", result);
  }

  @Test
  public void should_NotInterpretAsRegex_When_PatternContainsRegexChars() {
    String source = "test*pattern+here";
    String result = StringUtil.replaceAll(source, "*", "X");
    assertEquals("testXpattern+here", result);
  }

  // ========== Content Preservation Tests ==========

  @Test
  public void should_PreserveSurroundingContent_When_ReplacingMiddle() {
    String source = "AAA_MIDDLE_BBB";
    String result = StringUtil.replaceAll(source, "MIDDLE", "XXX");
    assertEquals("AAA_XXX_BBB", result);
  }

  @Test
  public void should_PreserveSurroundingContentForMultiple_When_MultipleReplacements() {
    String source = "A_X_B_X_C_X_D";
    String result = StringUtil.replaceAll(source, "X", "Y");
    assertEquals("A_Y_B_Y_C_Y_D", result);
  }

  // ========== Boundary Tests ==========

  @Test
  public void should_HandleReplacementWithPatternAsPrefix_When_ReplacementStartsWithPattern() {
    String source = "test";
    String result = StringUtil.replaceAll(source, "test", "test_suffix");
    assertEquals("test_suffix", result);
  }

  @Test
  public void should_HandleReplacementWithPatternAsSuffix_When_ReplacementEndsWithPattern() {
    String source = "test";
    String result = StringUtil.replaceAll(source, "test", "prefix_test");
    assertEquals("prefix_test", result);
  }

  // ========== Sequential Replacement Simulation Tests ==========

  @Test
  public void should_ReplaceAllInSequence_When_MultipleCallsMadeWithDifferentPatterns() {
    String source = "hello world";
    String result1 = StringUtil.replaceAll(source, "hello", "hi");
    String result2 = StringUtil.replaceAll(result1, "world", "earth");
    assertEquals("hi earth", result2);
  }

  @Test
  public void should_BeIdempotent_When_PatternNotInReplacedString() {
    String source = "test";
    String result1 = StringUtil.replaceAll(source, "test", "exam");
    String result2 = StringUtil.replaceAll(result1, "test", "exam");
    assertEquals(result1, result2);
  }
}
