package org.castor.core.util;

import junit.framework.TestCase;

/**
 * Test for Base64Encoder.
 */
public class Base64EncoderTest extends TestCase {

  /**
   * Test static encode method with simple bytes.
   */
  public void test_Should_EncodeSimpleBytes_When_InputIsValid() {
    byte[] input = new byte[] {(byte) 0x48, (byte) 0x65, (byte) 0x6C, (byte) 0x6C, (byte) 0x6F}; // "Hello"
    char[] result = Base64Encoder.encode(input);

    assertNotNull(result);
    String encoded = new String(result);
    assertEquals("SGVsbG8=", encoded);
  }

  /**
   * Test encode with empty byte array.
   */
  public void test_Should_ReturnEmpty_When_InputIsEmpty() {
    byte[] input = new byte[0];
    char[] result = Base64Encoder.encode(input);

    assertNotNull(result);
    assertEquals(0, result.length);
  }

  /**
   * Test translate method.
   */
  public void test_Should_TranslateBytes_When_TranslateIsCalled() {
    Base64Encoder encoder = new Base64Encoder();
    byte[] input = new byte[] {(byte) 0x48, (byte) 0x65, (byte) 0x6C, (byte) 0x6C, (byte) 0x6F};
    encoder.translate(input);
    char[] result = encoder.getCharArray();

    assertNotNull(result);
    assertEquals("SGVsbG8=", new String(result));
  }

  /**
   * Test encode with single byte.
   */
  public void test_Should_EncodeSingleByte_When_InputHasOneByte() {
    byte[] input = new byte[] {(byte) 0x41}; // 'A'
    char[] result = Base64Encoder.encode(input);

    assertNotNull(result);
    assertEquals("QQ==", new String(result));
  }

  /**
   * Test encode with two bytes.
   */
  public void test_Should_EncodeTwoBytes_When_InputHasTwoBytes() {
    byte[] input = new byte[] {(byte) 0x41, (byte) 0x42}; // "AB"
    char[] result = Base64Encoder.encode(input);

    assertNotNull(result);
    assertEquals("QUI=", new String(result));
  }

  /**
   * Test encode with three bytes (no padding needed).
   */
  public void test_Should_EncodeThreeBytes_When_InputHasThreeBytes() {
    byte[] input = new byte[] {(byte) 0x41, (byte) 0x42, (byte) 0x43}; // "ABC"
    char[] result = Base64Encoder.encode(input);

    assertNotNull(result);
    assertEquals("QUJD", new String(result));
  }

  /**
   * Test encode with four bytes.
   */
  public void test_Should_EncodeFourBytes_When_InputHasFourBytes() {
    byte[] input = new byte[] {(byte) 0x41, (byte) 0x42, (byte) 0x43, (byte) 0x44}; // "ABCD"
    char[] result = Base64Encoder.encode(input);

    assertNotNull(result);
    assertEquals("QUJDRA==", new String(result));
  }

  /**
   * Test getCharArray.
   */
  public void test_Should_ReturnCharArray_When_GetCharArrayCalled() {
    Base64Encoder encoder = new Base64Encoder();
    byte[] input = new byte[] {(byte) 0x41, (byte) 0x42, (byte) 0x43};
    encoder.translate(input);
    char[] result = encoder.getCharArray();

    assertNotNull(result);
    assertTrue(result.length > 0);
  }

  /**
   * Test reset method.
   */
  public void test_Should_ResetState_When_ResetCalled() {
    Base64Encoder encoder = new Base64Encoder();
    byte[] input = new byte[] {(byte) 0x41};
    encoder.translate(input);
    encoder.reset();

    char[] result = encoder.getCharArray();
    assertEquals(0, result.length);
  }

  /**
   * Test multiple translations.
   */
  public void test_Should_AccumulateTranslations_When_TranslateCalledMultipleTimes() {
    Base64Encoder encoder = new Base64Encoder();
    encoder.translate(new byte[] {(byte) 0x41, (byte) 0x42, (byte) 0x43});
    encoder.translate(new byte[] {(byte) 0x44});
    char[] result = encoder.getCharArray();

    assertNotNull(result);
    assertTrue(result.length > 0);
  }

  /**
   * Test encode with all zero bytes.
   */
  public void test_Should_EncodeZeroBytes_When_InputIsAllZeros() {
    byte[] input = new byte[] {(byte) 0x00, (byte) 0x00, (byte) 0x00};
    char[] result = Base64Encoder.encode(input);

    assertNotNull(result);
    assertEquals("AAAA", new String(result));
  }

  /**
   * Test encode with all 0xFF bytes.
   */
  public void test_Should_EncodeFFBytes_When_InputIsAllFF() {
    byte[] input = new byte[] {(byte) 0xFF, (byte) 0xFF, (byte) 0xFF};
    char[] result = Base64Encoder.encode(input);

    assertNotNull(result);
    assertEquals("////", new String(result));
  }

  /**
   * Test encode with mixed bytes.
   */
  public void test_Should_EncodeMixedBytes_When_InputHasMixedValues() {
    byte[] input = new byte[] {(byte) 0x00, (byte) 0x7F, (byte) 0xFF};
    char[] result = Base64Encoder.encode(input);

    assertNotNull(result);
    assertTrue(result.length > 0);
  }

  /**
   * Test getCharArray on empty encoder.
   */
  public void test_Should_ReturnEmptyArray_When_GetCharArrayOnEmptyEncoder() {
    Base64Encoder encoder = new Base64Encoder();
    char[] result = encoder.getCharArray();

    assertNotNull(result);
    assertEquals(0, result.length);
  }

  /**
   * Test constructor creates valid encoder.
   */
  public void test_Should_CreateValidEncoder_When_ConstructorCalled() {
    Base64Encoder encoder = new Base64Encoder();

    assertNotNull(encoder);
  }

  /**
   * Test encode and decode roundtrip.
   */
  public void test_Should_RoundtripSuccessfully_When_EncodingAndDecoding() {
    byte[] original = new byte[] {(byte) 0x48, (byte) 0x65, (byte) 0x6C, (byte) 0x6C, (byte) 0x6F};
    char[] encoded = Base64Encoder.encode(original);
    String encodedStr = new String(encoded);
    byte[] decoded = Base64Decoder.decode(encodedStr);

    assertEquals(original.length, decoded.length);
    for (int i = 0; i < original.length; i++) {
      assertEquals(original[i], decoded[i]);
    }
  }

  /**
   * Test encode with long byte array.
   */
  public void test_Should_EncodeLongByteArray_When_InputIsLong() {
    byte[] input = new byte[1000];
    for (int i = 0; i < 1000; i++) {
      input[i] = (byte) (i % 256);
    }
    char[] result = Base64Encoder.encode(input);

    assertNotNull(result);
    assertTrue(result.length > 0);
  }

  /**
   * Test translate single byte.
   */
  public void test_Should_TranslateSingleByte_When_TranslateSingleByteArray() {
    Base64Encoder encoder = new Base64Encoder();
    encoder.translate(new byte[] {(byte) 0x41});
    char[] result = encoder.getCharArray();

    assertNotNull(result);
    assertEquals("QQ==", new String(result));
  }

  /**
   * Test reset then translate.
   */
  public void test_Should_AllowTranslateAfterReset_When_ResetCalled() {
    Base64Encoder encoder = new Base64Encoder();
    encoder.translate(new byte[] {(byte) 0x41});
    encoder.reset();
    encoder.translate(new byte[] {(byte) 0x42});
    char[] result = encoder.getCharArray();

    assertNotNull(result);
    assertEquals("Qg==", new String(result));
  }

  /**
   * Test padding with one byte remaining.
   */
  public void test_Should_PaddingWithOneByte_When_RemainderIsOne() {
    byte[] input = new byte[] {(byte) 0x41, (byte) 0x42, (byte) 0x43, (byte) 0x44, (byte) 0x45}; // 5 bytes = 1 remainder
    char[] result = Base64Encoder.encode(input);

    assertNotNull(result);
    String encoded = new String(result);
    assertTrue(encoded.endsWith("="));
  }

  /**
   * Test padding with two bytes remaining.
   */
  public void test_Should_PaddingWithTwoBytes_When_RemainderIsTwo() {
    byte[] input = new byte[] {(byte) 0x41, (byte) 0x42, (byte) 0x43, (byte) 0x44, (byte) 0x45}; // 5 bytes = 2 remainder
    char[] result = Base64Encoder.encode(input);

    assertNotNull(result);
    String encoded = new String(result);
    assertTrue(encoded.endsWith("="));
  }

  /**
   * Test specific byte values.
   */
  public void test_Should_EncodeSpecificBytes_When_InputHasSpecialValues() {
    byte[] input = new byte[] {(byte) 0x3F}; // One byte that produces specific sextets
    char[] result = Base64Encoder.encode(input);

    assertNotNull(result);
    assertTrue(result.length > 0);
  }

  /**
   * Test multiple resets.
   */
  public void test_Should_AllowMultipleResets_When_ResetCalledSequentially() {
    Base64Encoder encoder = new Base64Encoder();
    encoder.translate(new byte[] {(byte) 0x41});
    encoder.reset();
    encoder.reset();

    char[] result = encoder.getCharArray();
    assertEquals(0, result.length);
  }

  /**
   * Test sequential translations without reset.
   */
  public void test_Should_ContinueEncoding_When_MultipleTranslatesCalled() {
    Base64Encoder encoder = new Base64Encoder();
    encoder.translate(new byte[] {(byte) 0x41});
    encoder.translate(new byte[] {(byte) 0x42});
    encoder.translate(new byte[] {(byte) 0x43});
    char[] result = encoder.getCharArray();

    assertNotNull(result);
    assertEquals("QUJD", new String(result));
  }

  /**
   * Test encode produces only valid base64 characters.
   */
  public void test_Should_ProduceValidBase64_When_EncodingAnyBytes() {
    byte[] input = new byte[] {(byte) 0xFF, (byte) 0xAA, (byte) 0x55};
    char[] result = Base64Encoder.encode(input);

    String encoded = new String(result);
    for (char c : encoded.toCharArray()) {
      boolean isValid = (c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z') ||
                       (c >= '0' && c <= '9') || c == '+' || c == '/' || c == '=';
      assertTrue("Invalid character in base64: " + c, isValid);
    }
  }
}
