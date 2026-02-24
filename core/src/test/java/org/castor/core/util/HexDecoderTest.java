package org.castor.core.util;

import junit.framework.TestCase;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Test for HexDecoder.
 */
public class HexDecoderTest extends TestCase {

  /**
   * Test encode method with byte array.
   */
  public void test_Should_EncodeByteArray_When_InputIsValid() throws IOException {
    byte[] data = new byte[] {(byte) 0x48, (byte) 0x65, (byte) 0x6C, (byte) 0x6C, (byte) 0x6F}; // "Hello"
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    int result = HexDecoder.encode(data, 0, data.length, out);

    assertEquals(10, result);
    assertEquals("48656C6C6F", out.toString());
  }

  /**
   * Test encode method with offset and length.
   */
  public void test_Should_EncodeWithOffset_When_OffsetAndLengthProvided() throws IOException {
    byte[] data = new byte[] {(byte) 0x41, (byte) 0x42, (byte) 0x43, (byte) 0x44, (byte) 0x45};
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    int result = HexDecoder.encode(data, 1, 3, out);

    assertEquals(6, result);
    assertEquals("424344", out.toString());
  }

  /**
   * Test encode static method with string result.
   */
  public void test_Should_EncodeToString_When_UsingStaticMethod() {
    byte[] data = new byte[] {(byte) 0x48, (byte) 0x69}; // "Hi"
    String result = HexDecoder.encode(data);

    assertEquals("4869", result);
  }

  /**
   * Test decode static method with string input.
   */
  public void test_Should_DecodeFromString_When_UsingStaticMethod() {
    String hexString = "48656C6C6F"; // "Hello"
    byte[] result = HexDecoder.decode(hexString);

    assertNotNull(result);
    assertEquals(5, result.length);
    assertEquals("Hello", new String(result));
  }

  /**
   * Test decode method with byte array.
   */
  public void test_Should_DecodeByteArray_When_InputIsValid() throws IOException {
    byte[] hexBytes = "48656C6C6F".getBytes();
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    int result = HexDecoder.decode(hexBytes, 0, hexBytes.length, out);

    assertEquals(5, result);
    assertEquals("Hello", out.toString());
  }

  /**
   * Test decode with offset and length.
   */
  public void test_Should_DecodeWithOffset_When_OffsetAndLengthProvided() throws IOException {
    byte[] hexBytes = "48656C6C6F4445".getBytes();
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    int result = HexDecoder.decode(hexBytes, 0, 10, out);

    assertEquals(5, result);
  }

  /**
   * Test decode with lowercase hex characters.
   */
  public void test_Should_DecodeLowercaseHex_When_InputIsLowercase() {
    String hexString = "48656c6c6f"; // lowercase
    byte[] result = HexDecoder.decode(hexString);

    assertNotNull(result);
    assertEquals("Hello", new String(result));
  }

  /**
   * Test decode with uppercase hex characters.
   */
  public void test_Should_DecodeUppercaseHex_When_InputIsUppercase() {
    String hexString = "48656C6C6F"; // uppercase
    byte[] result = HexDecoder.decode(hexString);

    assertNotNull(result);
    assertEquals("Hello", new String(result));
  }

  /**
   * Test decode with mixed case hex characters.
   */
  public void test_Should_DecodeMixedCaseHex_When_InputIsMixed() {
    String hexString = "48656C6c6f"; // mixed case
    byte[] result = HexDecoder.decode(hexString);

    assertNotNull(result);
    assertEquals("Hello", new String(result));
  }

  /**
   * Test decode with spaces.
   */
  public void test_Should_IgnoreSpaces_When_InputContainsSpaces() {
    String hexString = "48 65 6C 6C 6F";
    byte[] result = HexDecoder.decode(hexString);

    assertNotNull(result);
    assertEquals("Hello", new String(result));
  }

  /**
   * Test decode with newlines.
   */
  public void test_Should_IgnoreNewlines_When_InputContainsNewlines() {
    String hexString = "4865\n6C6C\n6F";
    byte[] result = HexDecoder.decode(hexString);

    assertNotNull(result);
    assertEquals("Hello", new String(result));
  }

  /**
   * Test decode with tabs.
   */
  public void test_Should_IgnoreTabs_When_InputContainsTabs() {
    String hexString = "48\t65\t6C\t6C\t6F";
    byte[] result = HexDecoder.decode(hexString);

    assertNotNull(result);
    assertEquals("Hello", new String(result));
  }

  /**
   * Test decode with carriage return.
   */
  public void test_Should_IgnoreCarriageReturn_When_InputContainsCarriageReturn() {
    String hexString = "4865\r6C6C\r6F";
    byte[] result = HexDecoder.decode(hexString);

    assertNotNull(result);
    assertEquals("Hello", new String(result));
  }

  /**
   * Test decode with all whitespace types.
   */
  public void test_Should_IgnoreAllWhitespace_When_InputContainsMixedWhitespace() {
    String hexString = "48 65\n6C\t6C\r6F";
    byte[] result = HexDecoder.decode(hexString);

    assertNotNull(result);
    assertEquals("Hello", new String(result));
  }

  /**
   * Test decode with trailing whitespace.
   */
  public void test_Should_IgnoreTrailingWhitespace_When_InputHasTrailingSpaces() {
    String hexString = "48656C6C6F   \n\t\r";
    byte[] result = HexDecoder.decode(hexString);

    assertNotNull(result);
    assertEquals("Hello", new String(result));
  }

  /**
   * Test decode with leading whitespace.
   */
  public void test_Should_IgnoreLeadingWhitespace_When_InputHasLeadingSpaces() {
    String hexString = "   \n\t\r48656C6C6F";
    byte[] result = HexDecoder.decode(hexString);

    assertNotNull(result);
    assertEquals("Hello", new String(result));
  }

  /**
   * Test encode with single byte.
   */
  public void test_Should_EncodeSingleByte_When_InputLengthIsOne() throws IOException {
    byte[] data = new byte[] {(byte) 0xFF};
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    int result = HexDecoder.encode(data, 0, 1, out);

    assertEquals(2, result);
    assertEquals("FF", out.toString());
  }

  /**
   * Test encode with zero byte.
   */
  public void test_Should_EncodeZeroByte_When_InputIs0x00() throws IOException {
    byte[] data = new byte[] {(byte) 0x00};
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    HexDecoder.encode(data, 0, 1, out);

    assertEquals("00", out.toString());
  }

  /**
   * Test decode with single hex pair.
   */
  public void test_Should_DecodeSingleHexPair_When_InputLengthIsTwo() {
    String hexString = "FF";
    byte[] result = HexDecoder.decode(hexString);

    assertNotNull(result);
    assertEquals(1, result.length);
    assertEquals((byte) 0xFF, result[0]);
  }

  /**
   * Test decode with zero hex pair.
   */
  public void test_Should_DecodeZeroHexPair_When_InputIs00() {
    String hexString = "00";
    byte[] result = HexDecoder.decode(hexString);

    assertNotNull(result);
    assertEquals(1, result.length);
    assertEquals(0, result[0]);
  }

  /**
   * Test DATA_TYPE constant.
   */
  public void test_Should_HaveCorrectDataType_When_CheckingConstant() {
    assertEquals("hexBinary", HexDecoder.DATA_TYPE);
  }

  /**
   * Test encode with all byte values 0-255.
   */
  public void test_Should_EncodeAllByteValues_When_InputSpansFullRange() throws IOException {
    byte[] data = new byte[256];
    for (int i = 0; i < 256; i++) {
      data[i] = (byte) i;
    }
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    int result = HexDecoder.encode(data, 0, data.length, out);

    assertEquals(512, result);
  }

  /**
   * Test decode method with byte array and offset.
   */
  public void test_Should_DecodeByteArrayWithOffset_When_OffsetProvided() throws IOException {
    byte[] hexBytes = "XXXX48656C6C6F".getBytes();
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    int result = HexDecoder.decode(hexBytes, 4, 10, out);

    assertEquals(5, result);
    assertEquals("Hello", out.toString());
  }

  /**
   * Test encode produces uppercase output.
   */
  public void test_Should_ProduceUppercaseOutput_When_EncodingBytes() {
    byte[] data = new byte[] {(byte) 0xAB, (byte) 0xCD, (byte) 0xEF};
    String result = HexDecoder.encode(data);

    assertEquals("ABCDEF", result);
  }

  /**
   * Test decode with numbers only.
   */
  public void test_Should_DecodeNumericHex_When_InputIsNumbers() {
    String hexString = "30313233343536"; // "0123456"
    byte[] result = HexDecoder.decode(hexString);

    assertNotNull(result);
    assertEquals("0123456", new String(result));
  }

  /**
   * Test encode then decode roundtrip.
   */
  public void test_Should_RoundtripSuccessfully_When_EncodingThenDecoding() {
    byte[] original = new byte[] {(byte) 0x48, (byte) 0x65, (byte) 0x6C, (byte) 0x6C, (byte) 0x6F};
    String encoded = HexDecoder.encode(original);
    byte[] decoded = HexDecoder.decode(encoded);

    assertEquals(original.length, decoded.length);
    for (int i = 0; i < original.length; i++) {
      assertEquals(original[i], decoded[i]);
    }
  }

  /**
   * Test decode with very long input.
   */
  public void test_Should_DecodeLongHexString_When_InputIsLong() {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < 100; i++) {
      sb.append("48656C6C6F");
    }
    byte[] result = HexDecoder.decode(sb.toString());

    assertNotNull(result);
    assertEquals(500, result.length);
  }

  /**
   * Test encode with offset at end of array.
   */
  public void test_Should_EncodeWithOffsetAtEnd_When_LengthIsZero() throws IOException {
    byte[] data = new byte[] {(byte) 0x41, (byte) 0x42, (byte) 0x43};
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    int result = HexDecoder.encode(data, 3, 0, out);

    assertEquals(0, result);
    assertEquals("", out.toString());
  }

  /**
   * Test decode with empty hex string.
   */
  public void test_Should_DecodeEmptyString_When_InputIsEmpty() {
    String hexString = "";
    byte[] result = HexDecoder.decode(hexString);

    assertNotNull(result);
    assertEquals(0, result.length);
  }

  /**
   * Test decode with only whitespace.
   */
  public void test_Should_DecodeWhitespaceOnly_When_InputIsOnlyWhitespace() {
    String hexString = "   \n\t\r   ";
    byte[] result = HexDecoder.decode(hexString);

    assertNotNull(result);
    assertEquals(0, result.length);
  }

  /**
   * Test decode method with byte array and whitespace.
   */
  public void test_Should_DecodeByteArrayWithWhitespace_When_InputHasSpaces() throws IOException {
    byte[] hexBytes = "48 65 6C 6C 6F".getBytes();
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    int result = HexDecoder.decode(hexBytes, 0, hexBytes.length, out);

    assertEquals(5, result);
    assertEquals("Hello", out.toString());
  }
}
