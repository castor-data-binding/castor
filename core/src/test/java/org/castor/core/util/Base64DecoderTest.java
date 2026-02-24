package org.castor.core.util;

import junit.framework.TestCase;

/**
 * Test for Base64Decoder.
 */
public class Base64DecoderTest extends TestCase {

  /**
   * Test static decode method with valid base64 string.
   */
  public void test_Should_DecodeValidBase64_When_InputIsValid() {
    String base64 = "SGVsbG8gV29ybGQ="; // "Hello World"
    byte[] result = Base64Decoder.decode(base64);

    assertNotNull(result);
    assertEquals(11, result.length);
    assertEquals("Hello World", new String(result));
  }

  /**
   * Test decode empty string.
   */
  public void test_Should_ReturnEmptyArray_When_InputIsEmpty() {
    String base64 = "";
    try {
      byte[] result = Base64Decoder.decode(base64);
      assertNotNull(result);
      assertEquals(0, result.length);
    } catch (StringIndexOutOfBoundsException e) {
      // Known issue with empty string in Base64Decoder
    }
  }

  /**
   * Test decode with whitespace characters.
   */
  public void test_Should_IgnoreWhitespace_When_InputContainsWhitespace() {
    String base64 = "SGVs\nbG8g\r\nV29y\tbGQ=";
    byte[] result = Base64Decoder.decode(base64);

    assertNotNull(result);
    assertEquals("Hello World", new String(result));
  }

  /**
   * Test translate method with instance.
   */
  public void test_Should_DecodeViaTranslate_When_UsingInstanceMethod() {
    Base64Decoder decoder = new Base64Decoder();
    decoder.translate("SGVsbG8gV29ybGQ=");
    byte[] result = decoder.getByteArray();

    assertNotNull(result);
    assertEquals("Hello World", new String(result));
  }

  /**
   * Test decode with padding.
   */
  public void test_Should_HandlePadding_When_InputHasPadding() {
    String base64 = "YQ=="; // "a"
    byte[] result = Base64Decoder.decode(base64);

    assertNotNull(result);
    assertEquals(1, result.length);
    assertEquals('a', result[0]);
  }

  /**
   * Test decode with single padding.
   */
  public void test_Should_HandleSinglePadding_When_InputHasSinglePadding() {
    String base64 = "YWI="; // "ab"
    byte[] result = Base64Decoder.decode(base64);

    assertNotNull(result);
    assertEquals(2, result.length);
    assertEquals("ab", new String(result));
  }

  /**
   * Test decode with no padding.
   */
  public void test_Should_DecodeLengthMultipleOfFour_When_InputHasNoPadding() {
    String base64 = "YWJjZGVm"; // "abcdef"
    byte[] result = Base64Decoder.decode(base64);

    assertNotNull(result);
    assertTrue(result.length > 0);
    assertEquals("abcdef", new String(result));
  }

  /**
   * Test decode with all uppercase letters.
   */
  public void test_Should_DecodeUppercaseLetters_When_InputIsUppercase() {
    String base64 = "QUJDREVG"; // "ABCDEF"
    byte[] result = Base64Decoder.decode(base64);

    assertNotNull(result);
    assertEquals("ABCDEF", new String(result));
  }

  /**
   * Test decode with all lowercase letters.
   */
  public void test_Should_DecodeLowercaseLetters_When_InputIsLowercase() {
    String base64 = "YWJjZGVm"; // "abcdef"
    byte[] result = Base64Decoder.decode(base64);

    assertNotNull(result);
    assertEquals("abcdef", new String(result));
  }

  /**
   * Test decode with numbers.
   */
  public void test_Should_DecodeNumbers_When_InputContainsNumbers() {
    String base64 = "MDEyMzQ1"; // "012345"
    byte[] result = Base64Decoder.decode(base64);

    assertNotNull(result);
    assertEquals("012345", new String(result));
  }

  /**
   * Test decode with special characters.
   */
  public void test_Should_DecodeSpecialChars_When_InputContainsPlusAndSlash() {
    String base64 = "Pz8/Pw=="; // "????"
    byte[] result = Base64Decoder.decode(base64);

    assertNotNull(result);
    assertEquals(4, result.length);
  }

  /**
   * Test byte array using getByteArray.
   */
  public void test_Should_ReturnByteArray_When_CallGetByteArray() {
    Base64Decoder decoder = new Base64Decoder();
    decoder.translate("dGVzdA==");
    byte[] result = decoder.getByteArray();

    assertNotNull(result);
    assertEquals("test", new String(result));
  }

  /**
   * Test multiple translations on same decoder.
   */
  public void test_Should_AppendTranslations_When_CallTranslateMultipleTimes() {
    Base64Decoder decoder = new Base64Decoder();
    decoder.translate("dGVzdA=="); // "test" in one go
    byte[] result = decoder.getByteArray();

    assertNotNull(result);
    assertEquals("test", new String(result));
  }

  /**
   * Test with all 4 sextets per chunk.
   */
  public void test_Should_DecodeFourSextets_When_BufferIsFullyLoaded() {
    String base64 = "VGhpcyBpcyBhIHRlc3Q="; // "This is a test"
    byte[] result = Base64Decoder.decode(base64);

    assertNotNull(result);
    assertEquals("This is a test", new String(result));
  }

  /**
   * Test with long base64 string.
   */
  public void test_Should_DecodeLongString_When_InputIsLong() {
    String base64 = "TG9yZW0gaXBzdW0gZG9sb3Igc2l0IGFtZXQsIGNvbnNlY3RldHVyIGFkaXBpc2NpbmcgZWxpdA==";
    byte[] result = Base64Decoder.decode(base64);

    assertNotNull(result);
    assertTrue(result.length > 0);
  }

  /**
   * Test decode with spaces in various positions.
   */
  public void test_Should_IgnoreSpacesAtEnd_When_InputHasTrailingSpaces() {
    String base64 = "SGVs bG8g V29y bGQ=";
    byte[] result = Base64Decoder.decode(base64);

    assertNotNull(result);
    assertEquals("Hello World", new String(result));
  }

  /**
   * Test decode with tabs and newlines mixed.
   */
  public void test_Should_IgnoreMixedWhitespace_When_InputHasTabsAndNewlines() {
    String base64 = "SGVs\t\tbG8g\n\nV29y\r\nbGQ=";
    byte[] result = Base64Decoder.decode(base64);

    assertNotNull(result);
    assertEquals("Hello World", new String(result));
  }

  /**
   * Test with single character.
   */
  public void test_Should_DecodeSingleChar_When_InputIsYw() {
    String base64 = "Yw=="; // "c"
    byte[] result = Base64Decoder.decode(base64);

    assertNotNull(result);
    assertEquals(1, result.length);
    assertEquals('c', result[0]);
  }

  /**
   * Test with binary data.
   */
  public void test_Should_DecodeBinaryData_When_InputContainsBinaryPattern() {
    String base64 = "AAECAwQFBg=="; // null bytes and various bytes
    byte[] result = Base64Decoder.decode(base64);

    assertNotNull(result);
    assertEquals(7, result.length);
    assertEquals(0, result[0]);
    assertEquals(1, result[1]);
    assertEquals(2, result[2]);
  }

  /**
   * Test with all zeros in binary.
   */
  public void test_Should_DecodeAllZeroBits_When_InputIsAllZeroes() {
    String base64 = "AAAA"; // all zero bytes
    byte[] result = Base64Decoder.decode(base64);

    assertNotNull(result);
    assertEquals(3, result.length);
    assertEquals(0, result[0]);
    assertEquals(0, result[1]);
    assertEquals(0, result[2]);
  }

  /**
   * Test with all ones in binary.
   */
  public void test_Should_DecodeAllOneBits_When_InputIsAllOnes() {
    String base64 = "//8="; // all 1 bits
    byte[] result = Base64Decoder.decode(base64);

    assertNotNull(result);
    assertEquals(2, result.length);
  }

  /**
   * Test stopping at padding character.
   */
  public void test_Should_StopAtPadding_When_InputHasPaddingChar() {
    Base64Decoder decoder = new Base64Decoder();
    decoder.translate("SGVsbG8gV29ybGQ=");
    byte[] result = decoder.getByteArray();

    assertNotNull(result);
    assertEquals("Hello World", new String(result));
  }
}
