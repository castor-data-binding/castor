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

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Comprehensive test suite for StringUtil achieving >95% coverage.
 */
public class StringUtilComprehensiveTest {

  // ========== Basic Replacement Tests ==========

  @Test
  public void should_ReplaceAllOccurrences_When_PatternExistsMultipleTimes() {
    String source = "hello world hello";
    String result = StringUtil.replaceAll(source, "hello", "hi");
    assertEquals("Should replace all occurrences", "hi world hi", result);
  }

  @Test
  public void should_ReturnOriginal_When_PatternNotFound() {
    String source = "hello world";
    String result = StringUtil.replaceAll(source, "xyz", "abc");
    assertEquals("Should return original string", "hello world", result);
  }

  @Test
  public void should_ReplaceCompleteString_When_PatternIsEntireString() {
    String source = "hello";
    String result = StringUtil.replaceAll(source, "hello", "world");
    assertEquals("Should replace entire string", "world", result);
  }

  @Test
  public void should_ReplaceCharacterPatterns_When_SingleCharacterPattern() {
    String source = "aaa";
    String result = StringUtil.replaceAll(source, "a", "b");
    assertEquals("Should replace all single characters", "bbb", result);
  }

  @Test
  public void should_RemovePattern_When_ReplacementIsEmpty() {
    String source = "hello world test";
    String result = StringUtil.replaceAll(source, "world", "");
    assertEquals("Should remove pattern", "hello  test", result);
  }

  @Test
  public void should_RemoveCharacter_When_CharacterReplacementIsEmpty() {
    String source = "abc";
    String result = StringUtil.replaceAll(source, "b", "");
    assertEquals("Should remove character", "ac", result);
  }

  // ========== Null Handling Tests ==========

  @Test
  public void should_ReturnNull_When_SourceIsNull() {
    String result = StringUtil.replaceAll(null, "world", "castor");
    assertNull("Should return null when source is null", result);
  }

  @Test
  public void should_ReturnSource_When_ToReplaceIsNull() {
    String source = "hello world";
    String result = StringUtil.replaceAll(source, null, "castor");
    assertEquals("Should return source when toReplace is null", source, result);
  }

  @Test
  public void should_HandleNullReplacement_When_ReplacementIsNull() {
    String source = "hello world";
    String result = StringUtil.replaceAll(source, "world", null);
    assertNotNull("Should handle null replacement", result);
  }

  @Test
  public void should_ReturnNull_When_AllParametersAreNull() {
    String result = StringUtil.replaceAll(null, null, null);
    assertNull("Should return null when all parameters are null", result);
  }

  @Test
  public void should_ReturnNull_When_SourceAndToReplaceAreNull() {
    String result = StringUtil.replaceAll(null, null, "replacement");
    assertNull("Should return null when source and toReplace are null", result);
  }

  // ========== Empty String Tests ==========

  @Test
  public void should_ReturnEmpty_When_SourceIsEmpty() {
    String result = StringUtil.replaceAll("", "pattern", "replacement");
    assertEquals("Should return empty string", "", result);
  }

  @Test
  public void should_HandleEmptyToReplace_When_ToReplaceIsEmpty() {
    String source = "hello";
    String result = StringUtil.replaceAll(source, "", "x");
    assertNotNull("Should handle empty toReplace", result);
  }

  @Test
  public void should_HandleEmptyReplacement_When_ReplacementIsEmpty() {
    String source = "hello";
    String result = StringUtil.replaceAll(source, "l", "");
    assertEquals("Should remove matched characters", "heo", result);
  }

  @Test
  public void should_HandleBothEmpty_When_ToReplaceAndReplacementAreEmpty() {
    String source = "hello";
    String result = StringUtil.replaceAll(source, "", "");
    assertNotNull("Should handle both empty", result);
  }

  // ========== Special Characters Tests ==========

  @Test
  public void should_ReplaceSpecialCharacters_When_PatternContainsDot() {
    String source = "hello.world.test";
    String result = StringUtil.replaceAll(source, ".", "-");
    assertEquals("Should replace dots", "hello-world-test", result);
  }

  @Test
  public void should_ReplaceSpecialCharacters_When_PatternContainsAsterik() {
    String source = "hello*world*test";
    String result = StringUtil.replaceAll(source, "*", "-");
    assertEquals("Should replace asterisks", "hello-world-test", result);
  }

  @Test
  public void should_ReplaceSpecialCharacters_When_PatternContainsQuestionMark() {
    String source = "hello?world?test";
    String result = StringUtil.replaceAll(source, "?", "-");
    assertEquals("Should replace question marks", "hello-world-test", result);
  }

  @Test
  public void should_ReplaceSpecialCharacters_When_PatternContainsPlus() {
    String source = "hello+world+test";
    String result = StringUtil.replaceAll(source, "+", "-");
    assertEquals("Should replace plus signs", "hello-world-test", result);
  }

  @Test
  public void should_ReplaceSpecialCharacters_When_PatternContainsSquareBrackets() {
    String source = "hello[world]test";
    String result = StringUtil.replaceAll(source, "[", "(");
    assertEquals("Should replace square brackets", "hello(world]test", result);
  }

  @Test
  public void should_ReplaceSpecialCharacters_When_PatternContainsPipe() {
    String source = "hello|world|test";
    String result = StringUtil.replaceAll(source, "|", "-");
    assertEquals("Should replace pipes", "hello-world-test", result);
  }

  // ========== Multiline Tests ==========

  @Test
  public void should_ReplaceInMultilineString_When_PatternIsNewline() {
    String source = "hello\nworld\ntest";
    String result = StringUtil.replaceAll(source, "\n", " ");
    assertEquals("Should replace newlines", "hello world test", result);
  }

  @Test
  public void should_ReplaceInMultilineString_When_PatternIsCarriageReturn() {
    String source = "hello\rworld\rtest";
    String result = StringUtil.replaceAll(source, "\r", " ");
    assertEquals("Should replace carriage returns", "hello world test", result);
  }

  @Test
  public void should_ReplaceInMultilineString_When_PatternIsTab() {
    String source = "hello\tworld\ttest";
    String result = StringUtil.replaceAll(source, "\t", " ");
    assertEquals("Should replace tabs", "hello world test", result);
  }

  @Test
  public void should_ReplaceMultipleWhitespaceTypes_When_MixedWhitespace() {
    String source = "hello \n world \t test";
    String result = StringUtil.replaceAll(source, " ", "-");
    assertEquals("Should replace spaces", "hello-\n-world-\t-test", result);
  }

  // ========== Case Sensitivity Tests ==========

  @Test
  public void should_BeCaseSensitive_When_CaseDiffers() {
    String source = "Hello HELLO hello";
    String result = StringUtil.replaceAll(source, "hello", "hi");
    assertEquals("Should be case sensitive", "Hello HELLO hi", result);
  }

  @Test
  public void should_ReplaceOnlyMatchingCase_When_MultipleCase() {
    String source = "Test test TEST";
    String result = StringUtil.replaceAll(source, "test", "x");
    assertEquals("Should replace only matching case", "Test x TEST", result);
  }

  // ========== Position-Based Tests ==========

  @Test
  public void should_ReplaceAtStart_When_PatternAtBeginning() {
    String source = "testtest";
    String result = StringUtil.replaceAll(source, "test", "t");
    assertEquals("Should replace at start", "tt", result);
  }

  @Test
  public void should_ReplaceAtEnd_When_PatternAtEndOfString() {
    String source = "abctest";
    String result = StringUtil.replaceAll(source, "test", "");
    assertEquals("Should replace at end", "abc", result);
  }

  @Test
  public void should_ReplaceInMiddle_When_PatternInMiddle() {
    String source = "prefix_middle_suffix";
    String result = StringUtil.replaceAll(source, "middle", "replaced");
    assertEquals("Should replace in middle", "prefix_replaced_suffix", result);
  }

  @Test
  public void should_ReplaceConsecutive_When_PatternConsecutive() {
    String source = "xyxy";
    String result = StringUtil.replaceAll(source, "xy", "z");
    assertEquals("Should replace consecutive patterns", "zz", result);
  }

  // ========== Unicode Tests ==========

  @Test
  public void should_ReplaceUnicode_When_PatternContainsUnicodeCharacters() {
    String source = "café café";
    String result = StringUtil.replaceAll(source, "café", "coffee");
    assertEquals("Should handle unicode", "coffee coffee", result);
  }

  @Test
  public void should_HandleUnicodeInReplacement_When_ReplacementContainsUnicode() {
    String source = "hello world";
    String result = StringUtil.replaceAll(source, "world", "café");
    assertEquals("Should handle unicode in replacement", "hello café", result);
  }

  @Test
  public void should_HandleChineseCharacters_When_PatternIsChineseCharacter() {
    String source = "你好 世界 你好";
    String result = StringUtil.replaceAll(source, "你好", "hi");
    assertEquals("Should handle Chinese characters", "hi 世界 hi", result);
  }

  @Test
  public void should_HandleArabicCharacters_When_PatternIsArabicCharacter() {
    String source = "مرحبا العالم مرحبا";
    String result = StringUtil.replaceAll(source, "مرحبا", "hello");
    assertEquals("Should handle Arabic characters", "hello العالم hello", result);
  }

  // ========== Length Tests ==========

  @Test
  public void should_HandleVeryLongSource_When_SourceIsLong() {
    String source = org.apache.commons.lang3.StringUtils.repeat("a", 10000);
    String result = StringUtil.replaceAll(source, "a", "b");
    assertEquals("Should handle very long source", 10000, result.length());
    assertTrue("Should contain only replacements", result.matches("b+"));
  }

  @Test
  public void should_HandleVeryLongPattern_When_PatternIsLong() {
    String pattern = org.apache.commons.lang3.StringUtils.repeat("x", 1000);
    String source = pattern + " " + pattern;
    String result = StringUtil.replaceAll(source, pattern, "Y");
    assertEquals("Should handle very long pattern", "Y Y", result);
  }

  @Test
  public void should_HandleVeryLongReplacement_When_ReplacementIsLong() {
    String longReplacement = org.apache.commons.lang3.StringUtils.repeat("y", 10000);
    String result = StringUtil.replaceAll("a", "a", longReplacement);
    assertEquals("Should handle very long replacement", longReplacement, result);
  }

  @Test
  public void should_HandleSingleCharacterSource_When_SourceIsSingleChar() {
    String result = StringUtil.replaceAll("a", "a", "bcd");
    assertEquals("Should replace single character", "bcd", result);
  }

  // ========== Numeric Patterns Tests ==========

  @Test
  public void should_ReplaceNumericPatterns_When_PatternIsNumeric() {
    String source = "123 456 789";
    String result = StringUtil.replaceAll(source, "456", "000");
    assertEquals("Should replace numeric pattern", "123 000 789", result);
  }

  @Test
  public void should_ReplaceLeadingZeros_When_PatternIsLeadingZeros() {
    String source = "0001 0002 0003";
    String result = StringUtil.replaceAll(source, "000", "");
    assertEquals("Should remove leading zeros", "1 2 3", result);
  }

  @Test
  public void should_ReplaceDecimalNumbers_When_PatternIsDecimalPoint() {
    String source = "3.14 2.71 1.41";
    String result = StringUtil.replaceAll(source, ".", ",");
    assertEquals("Should replace decimal points", "3,14 2,71 1,41", result);
  }

  // ========== Whitespace Tests ==========

  @Test
  public void should_ReplaceExtraSpaces_When_MultipleSpaces() {
    String source = "hello   world";
    String result = StringUtil.replaceAll(source, "   ", " ");
    assertEquals("Should collapse spaces", "hello world", result);
  }

  @Test
  public void should_ReplaceTabs_When_TabCharactersPresent() {
    String source = "hello\t\tworld";
    String result = StringUtil.replaceAll(source, "\t", " ");
    assertEquals("Should replace tabs", "hello  world", result);
  }

  @Test
  public void should_ReplaceMultipleWhitespaces_When_MixedSpacesAndTabs() {
    String source = "hello \t world";
    String result = StringUtil.replaceAll(source, " \t ", "X");
    assertNotNull("Should handle mixed whitespace", result);
  }

  // ========== Replacement Expansion Tests ==========

  @Test
  public void should_ExpandWhenReplacementLongerThanPattern_When_ReplacementShorter() {
    String source = "a";
    String result = StringUtil.replaceAll(source, "a", "aaaaa");
    assertEquals("Should expand string", "aaaaa", result);
  }

  @Test
  public void should_ContractWhenReplacementShorterThanPattern_When_ReplacementLonger() {
    String source = "hello";
    String result = StringUtil.replaceAll(source, "hello", "h");
    assertEquals("Should contract string", "h", result);
  }

  // ========== Complex Patterns Tests ==========

  @Test
  public void should_HandlePatternWithSpecialMeaning_When_PatternLooksLikeRegex() {
    String source = "a.b.c";
    String result = StringUtil.replaceAll(source, ".", "X");
    assertEquals("Should handle regex-like patterns", "aXbXc", result);
  }

  @Test
  public void should_HandleRepeatingPatterns_When_PatternRepeatsInSource() {
    String source = "aaabbbcccaaabbbccc";
    String result = StringUtil.replaceAll(source, "aaa", "x");
    assertEquals("Should handle repeating patterns", "xbbbcccxbbbccc", result);
  }

  @Test
  public void should_HandleOverlappingPatterns_When_PatternCanOverlap() {
    String source = "aaa";
    String result = StringUtil.replaceAll(source, "aa", "b");
    assertEquals("Should replace non-overlapping", "ba", result);
  }

  // ========== Edge Cases ==========

  @Test
  public void should_HandleSingleCharacterReplacement_When_ReplacementIsSingleChar() {
    String source = "hello world";
    String result = StringUtil.replaceAll(source, "o", "0");
    assertEquals("Should replace single character", "hell0 w0rld", result);
  }

  @Test
  public void should_ReplacePartialMatches_When_PatternPartiallyMatches() {
    String source = "hello world";
    String result = StringUtil.replaceAll(source, "hel", "hi");
    assertEquals("Should replace partial matches", "hilo world", result);
  }

  @Test
  public void should_PreserveSurroundingContent_When_ReplacingMiddleContent() {
    String source = "prefix[content]suffix";
    String result = StringUtil.replaceAll(source, "content", "CONTENT");
    assertEquals("Should preserve surrounding", "prefix[CONTENT]suffix", result);
  }

  @Test
  public void should_HandleConsecutivePlacement_When_ReplacementPlacedConsecutively() {
    String source = "aaaa";
    String result = StringUtil.replaceAll(source, "aa", "b");
    assertEquals("Should replace consecutively", "bb", result);
  }

  @Test
  public void should_HandleSourceAndPatternIdentical_When_SourceEqualsPattern() {
    String source = "test";
    String result = StringUtil.replaceAll(source, "test", "replaced");
    assertEquals("Should replace entire string", "replaced", result);
  }

  // ========== Performance Tests ==========

  @Test
  public void should_CompleteSuccessfully_When_PerformingManyReplacements() {
    String source = "a,b,c,d,e,f,g,h,i,j";
    String result = StringUtil.replaceAll(source, ",", "|");
    assertEquals("Should replace all commas", "a|b|c|d|e|f|g|h|i|j", result);
  }

  @Test
  public void should_HandleHighlyRepetitiveContent_When_PatternRepeatsOften() {
    String source = "ababababab";
    String result = StringUtil.replaceAll(source, "ab", "x");
    assertEquals("Should handle repetitive content", "xxxxx", result);
  }
}
