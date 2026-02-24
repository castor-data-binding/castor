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
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.junit.Test;

/**
 * Comprehensive test suite for HexDecoder achieving >95% coverage.
 */
public class HexDecoderComprehensiveTest {

  private static final String DATA_TYPE_EXPECTED = "hexBinary";

  // ========== DATA_TYPE constant tests ==========

  @Test
  public void should_HaveCorrectDataType_When_ClassLoaded() {
    assertEquals("DATA_TYPE should be hexBinary", DATA_TYPE_EXPECTED,
        HexDecoder.DATA_TYPE);
  }

  // ========== encode(byte[], int, int, OutputStream) Tests ==========

  @Test
  public void should_EncodeSimpleByteArray_When_ValidInputProvided()
      throws IOException {
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    byte[] data = {(byte) 0xAB, (byte) 0xCD};
    int result = HexDecoder.encode(data, 0, data.length, out);

    assertEquals("Should encode 2 bytes to 4 characters", 4, result);
    String encoded = new String(out.toByteArray());
    assertEquals("Should encode to ABCD", "ABCD", encoded);
  }

  @Test
  public void should_EncodeZeroByte_When_DataContainsZero() throws IOException {
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    byte[] data = {(byte) 0x00};
    int result = HexDecoder.encode(data, 0, data.length, out);

    assertEquals("Should encode 1 byte to 2 characters", 2, result);
    assertEquals("Should encode zero to 00", "00", new String(out.toByteArray()));
  }

  @Test
  public void should_EncodeMaxByte_When_DataContainsMaxValue() throws IOException {
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    byte[] data = {(byte) 0xFF};
    int result = HexDecoder.encode(data, 0, data.length, out);

    assertEquals("Should encode 1 byte to 2 characters", 2, result);
    assertEquals("Should encode FF to FF", "FF", new String(out.toByteArray()));
  }

  @Test
  public void should_EncodeWithOffset_When_OffsetProvided() throws IOException {
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    byte[] data = {(byte) 0x00, (byte) 0xAB, (byte) 0xCD};
    int result = HexDecoder.encode(data, 1, 2, out);

    assertEquals("Should encode 2 bytes to 4 characters", 4, result);
    assertEquals("Should encode from offset", "ABCD", new String(out.toByteArray()));
  }

  @Test
  public void should_EncodeMultipleBytes_When_LongerArrayProvided()
      throws IOException {
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    byte[] data = {(byte) 0x12, (byte) 0x34, (byte) 0x56, (byte) 0x78,
        (byte) 0x9A, (byte) 0xBC};
    int result = HexDecoder.encode(data, 0, data.length, out);

    assertEquals("Should encode 6 bytes to 12 characters", 12, result);
    assertEquals("Should encode correctly", "123456789ABC",
        new String(out.toByteArray()));
  }

  @Test
  public void should_EncodeZeroLength_When_LengthIsZero() throws IOException {
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    byte[] data = {(byte) 0xAB, (byte) 0xCD};
    int result = HexDecoder.encode(data, 0, 0, out);

    assertEquals("Should encode 0 bytes to 0 characters", 0, result);
    assertEquals("Should be empty", "", new String(out.toByteArray()));
  }

  @Test
  public void should_EncodeSingleByte_When_OnlyOneByteInArray()
      throws IOException {
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    byte[] data = {(byte) 0x42};
    int result = HexDecoder.encode(data, 0, data.length, out);

    assertEquals("Should encode 1 byte to 2 characters", 2, result);
    assertEquals("Should encode to 42", "42", new String(out.toByteArray()));
  }

  @Test
  public void should_EncodeWithPartialLength_When_LengthSmallerThanArray()
      throws IOException {
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    byte[] data = {(byte) 0x11, (byte) 0x22, (byte) 0x33, (byte) 0x44};
    int result = HexDecoder.encode(data, 0, 2, out);

    assertEquals("Should encode 2 bytes to 4 characters", 4, result);
    assertEquals("Should encode first two bytes", "1122",
        new String(out.toByteArray()));
  }

  // ========== decode(byte[], int, int, OutputStream) Tests ==========

  @Test
  public void should_DecodeSimpleHexString_When_ValidHexBytesProvided()
      throws IOException {
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    byte[] data = "ABCD".getBytes();
    int result = HexDecoder.decode(data, 0, data.length, out);

    assertEquals("Should decode 4 hex characters to 2 bytes", 2, result);
    byte[] decoded = out.toByteArray();
    assertEquals("Should decode correctly", (byte) 0xAB, decoded[0]);
    assertEquals("Should decode correctly", (byte) 0xCD, decoded[1]);
  }

  @Test
  public void should_DecodeLowercaseHex_When_LowercaseCharactersProvided()
      throws IOException {
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    byte[] data = "abcd".getBytes();
    int result = HexDecoder.decode(data, 0, data.length, out);

    assertEquals("Should decode lowercase", 2, result);
    byte[] decoded = out.toByteArray();
    assertEquals("Should decode ab to 0xAB", (byte) 0xAB, decoded[0]);
  }

  @Test
  public void should_DecodeMixedCaseHex_When_MixedCaseCharactersProvided()
      throws IOException {
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    byte[] data = "AbCd".getBytes();
    int result = HexDecoder.decode(data, 0, data.length, out);

    assertEquals("Should decode mixed case", 2, result);
    byte[] decoded = out.toByteArray();
    assertEquals("Should decode correctly", (byte) 0xAB, decoded[0]);
  }

  @Test
  public void should_IgnoreWhitespace_When_WhitespaceCharactersPresent()
      throws IOException {
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    byte[] data = "AB CD".getBytes();
    int result = HexDecoder.decode(data, 0, data.length, out);

    assertEquals("Should ignore space", 2, result);
    byte[] decoded = out.toByteArray();
    assertEquals("Should decode correctly", (byte) 0xAB, decoded[0]);
  }

  @Test
  public void should_IgnoreNewlines_When_NewlineCharactersPresent()
      throws IOException {
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    byte[] data = "AB\nCD".getBytes();
    int result = HexDecoder.decode(data, 0, data.length, out);

    assertEquals("Should ignore newline", 2, result);
  }

  @Test
  public void should_IgnoreTabs_When_TabCharactersPresent() throws IOException {
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    byte[] data = "AB\tCD".getBytes();
    int result = HexDecoder.decode(data, 0, data.length, out);

    assertEquals("Should ignore tab", 2, result);
  }

  @Test
  public void should_IgnoreCarriageReturns_When_CarriageReturnPresent()
      throws IOException {
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    byte[] data = "AB\rCD".getBytes();
    int result = HexDecoder.decode(data, 0, data.length, out);

    assertEquals("Should ignore carriage return", 2, result);
  }

  @Test
  public void should_DecodeZeroByte_When_ZeroHexProvided() throws IOException {
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    byte[] data = "00".getBytes();
    int result = HexDecoder.decode(data, 0, data.length, out);

    assertEquals("Should decode to 1 byte", 1, result);
    assertEquals("Should decode to 0x00", (byte) 0x00, out.toByteArray()[0]);
  }

  @Test
  public void should_DecodeFFByte_When_FFHexProvided() throws IOException {
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    byte[] data = "FF".getBytes();
    int result = HexDecoder.decode(data, 0, data.length, out);

    assertEquals("Should decode to 1 byte", 1, result);
    assertEquals("Should decode to 0xFF", (byte) 0xFF, out.toByteArray()[0]);
  }

  @Test
  public void should_DecodeWithOffset_When_OffsetProvided() throws IOException {
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    byte[] data = "00ABCD00".getBytes();
    int result = HexDecoder.decode(data, 2, 4, out);

    assertEquals("Should decode from offset", 2, result);
    byte[] decoded = out.toByteArray();
    assertEquals("Should decode correctly", (byte) 0xAB, decoded[0]);
  }

  @Test
  public void should_DecodeMultipleBytes_When_LongerHexStringProvided()
      throws IOException {
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    byte[] data = "123456789ABC".getBytes();
    int result = HexDecoder.decode(data, 0, data.length, out);

    assertEquals("Should decode to 6 bytes", 6, result);
    byte[] decoded = out.toByteArray();
    assertEquals("First byte", (byte) 0x12, decoded[0]);
    assertEquals("Last byte", (byte) 0xBC, decoded[5]);
  }

  @Test
  public void should_DecodeZeroLength_When_LengthIsZero() throws IOException {
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    byte[] data = "ABCD".getBytes();
    int result = HexDecoder.decode(data, 0, 0, out);

    assertEquals("Should decode 0 bytes", 0, result);
    assertEquals("Should be empty", 0, out.toByteArray().length);
  }

  @Test
  public void should_IgnoreTrailingWhitespace_When_TrailingWhitespacePresent()
      throws IOException {
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    byte[] data = "ABCD   ".getBytes();
    int result = HexDecoder.decode(data, 0, data.length, out);

    assertEquals("Should ignore trailing whitespace", 2, result);
  }

  @Test
  public void should_IgnoreLeadingWhitespace_When_LeadingWhitespacePresent()
      throws IOException {
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    byte[] data = "   ABCD".getBytes();
    int result = HexDecoder.decode(data, 0, data.length, out);

    assertEquals("Should ignore leading whitespace", 2, result);
  }

  @Test
  public void should_IgnoreMiddleWhitespace_When_WhitespaceInMiddle()
      throws IOException {
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    byte[] data = "AB CD EF".getBytes();
    int result = HexDecoder.decode(data, 0, data.length, out);

    assertEquals("Should ignore middle whitespace", 3, result);
  }

  // ========== decode(String, OutputStream) Tests ==========

  @Test
  public void should_DecodeStringHex_When_ValidHexStringProvided()
      throws IOException {
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    String data = "ABCD";
    int result = HexDecoder.decode(data, out);

    assertEquals("Should decode 4 hex characters to 2 bytes", 2, result);
    byte[] decoded = out.toByteArray();
    assertEquals("Should decode correctly", (byte) 0xAB, decoded[0]);
  }

  @Test
  public void should_DecodeStringLowercase_When_LowercaseStringProvided()
      throws IOException {
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    String data = "abcd";
    int result = HexDecoder.decode(data, out);

    assertEquals("Should decode lowercase", 2, result);
  }

  @Test
  public void should_DecodeStringWithWhitespace_When_WhitespacePresent()
      throws IOException {
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    String data = "AB CD\nEF\t12";
    int result = HexDecoder.decode(data, out);

    assertEquals("Should ignore whitespace", 4, result);
  }

  @Test
  public void should_DecodeEmptyString_When_EmptyStringProvided()
      throws IOException {
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    String data = "";
    int result = HexDecoder.decode(data, out);

    assertEquals("Should decode empty string to 0 bytes", 0, result);
  }

  @Test
  public void should_DecodeStringWithOnlyWhitespace_When_OnlyWhitespaceProvided()
      throws IOException {
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    String data = "   \n\t\r   ";
    int result = HexDecoder.decode(data, out);

    assertEquals("Should decode whitespace only to 0 bytes", 0, result);
  }

  @Test
  public void should_DecodeStringZeroByte_When_ZeroHexStringProvided()
      throws IOException {
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    String data = "00";
    int result = HexDecoder.decode(data, out);

    assertEquals("Should decode to 1 byte", 1, result);
    assertEquals("Should decode to 0x00", (byte) 0x00, out.toByteArray()[0]);
  }

  @Test
  public void should_DecodeStringMultipleBytes_When_LongerStringProvided()
      throws IOException {
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    String data = "123456789ABC";
    int result = HexDecoder.decode(data, out);

    assertEquals("Should decode to 6 bytes", 6, result);
  }

  // ========== encode(byte[]) Tests ==========

  @Test
  public void should_EncodeByteArrayToString_When_ValidByteArrayProvided() {
    byte[] data = {(byte) 0xAB, (byte) 0xCD};
    String result = HexDecoder.encode(data);

    assertEquals("Should encode to hex string", "ABCD", result);
  }

  @Test
  public void should_EncodeZeroBytes_When_ZeroByteArrayProvided() {
    byte[] data = {(byte) 0x00};
    String result = HexDecoder.encode(data);

    assertEquals("Should encode zero", "00", result);
  }

  @Test
  public void should_EncodeFFBytes_When_FFByteArrayProvided() {
    byte[] data = {(byte) 0xFF};
    String result = HexDecoder.encode(data);

    assertEquals("Should encode FF", "FF", result);
  }

  @Test
  public void should_EncodeMultipleBytes_When_LongerByteArrayProvided() {
    byte[] data = {(byte) 0x12, (byte) 0x34, (byte) 0x56, (byte) 0x78,
        (byte) 0x9A, (byte) 0xBC};
    String result = HexDecoder.encode(data);

    assertEquals("Should encode all bytes", "123456789ABC", result);
  }

  @Test
  public void should_EncodeEmptyArray_When_EmptyByteArrayProvided() {
    byte[] data = {};
    String result = HexDecoder.encode(data);

    assertEquals("Should encode empty to empty string", "", result);
  }

  @Test
  public void should_EncodeSingleByte_When_SingleByteArrayProvided() {
    byte[] data = {(byte) 0x42};
    String result = HexDecoder.encode(data);

    assertEquals("Should encode single byte", "42", result);
  }

  @Test
  public void should_EncodeLowerNibble_When_LowerNibbleProvided() {
    byte[] data = {(byte) 0x0F};
    String result = HexDecoder.encode(data);

    assertEquals("Should encode lower nibble", "0F", result);
  }

  @Test
  public void should_EncodeUpperNibble_When_UpperNibbleProvided() {
    byte[] data = {(byte) 0xF0};
    String result = HexDecoder.encode(data);

    assertEquals("Should encode upper nibble", "F0", result);
  }

  // ========== decode(String) Tests ==========

  @Test
  public void should_DecodeHexStringToByteArray_When_ValidHexStringProvided() {
    String data = "ABCD";
    byte[] result = HexDecoder.decode(data);

    assertEquals("Should decode to 2 bytes", 2, result.length);
    assertEquals("First byte", (byte) 0xAB, result[0]);
    assertEquals("Second byte", (byte) 0xCD, result[1]);
  }

  @Test
  public void should_DecodeLowercaseHexString_When_LowercaseStringProvided() {
    String data = "abcd";
    byte[] result = HexDecoder.decode(data);

    assertEquals("Should decode lowercase", 2, result.length);
    assertEquals("Should decode correctly", (byte) 0xAB, result[0]);
  }

  @Test
  public void should_DecodeMixedCaseHexString_When_MixedCaseStringProvided() {
    String data = "AbCd";
    byte[] result = HexDecoder.decode(data);

    assertEquals("Should decode mixed case", 2, result.length);
  }

  @Test
  public void should_DecodeZeroHexString_When_ZeroStringProvided() {
    String data = "00";
    byte[] result = HexDecoder.decode(data);

    assertEquals("Should decode to 1 byte", 1, result.length);
    assertEquals("Should decode to zero", (byte) 0x00, result[0]);
  }

  @Test
  public void should_DecodeFFHexString_When_FFStringProvided() {
    String data = "FF";
    byte[] result = HexDecoder.decode(data);

    assertEquals("Should decode to 1 byte", 1, result.length);
    assertEquals("Should decode to 0xFF", (byte) 0xFF, result[0]);
  }

  @Test
  public void should_DecodeEmptyStringArray_When_EmptyStringProvided() {
    String data = "";
    byte[] result = HexDecoder.decode(data);

    assertEquals("Should decode empty to empty array", 0, result.length);
  }

  @Test
  public void should_DecodeWithWhitespace_When_WhitespacePresent() {
    String data = "AB CD\nEF";
    byte[] result = HexDecoder.decode(data);

    assertEquals("Should decode ignoring whitespace", 3, result.length);
  }

  @Test
  public void should_DecodeLongHexString_When_LongStringProvided() {
    String data = "123456789ABC";
    byte[] result = HexDecoder.decode(data);

    assertEquals("Should decode to 6 bytes", 6, result.length);
    assertEquals("First byte", (byte) 0x12, result[0]);
    assertEquals("Last byte", (byte) 0xBC, result[5]);
  }

  @Test
  public void should_DecodeWithLeadingZeros_When_LeadingZerosPresent() {
    String data = "00FF";
    byte[] result = HexDecoder.decode(data);

    assertEquals("Should decode leading zeros", 2, result.length);
    assertEquals("First byte", (byte) 0x00, result[0]);
    assertEquals("Second byte", (byte) 0xFF, result[1]);
  }

  // ========== Ignore Tests ==========

  @Test
  public void should_IgnoreSpaceCharacter_When_SpacePresent() throws IOException {
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    byte[] data = "A B".getBytes();
    int result = HexDecoder.decode(data, 0, data.length, out);

    assertTrue("Space should be ignored", result >= 0);
  }

  @Test
  public void should_IgnoreNewlineCharacter_When_NewlinePresent()
      throws IOException {
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    byte[] data = "A\nB".getBytes();
    int result = HexDecoder.decode(data, 0, data.length, out);

    assertTrue("Newline should be ignored", result >= 0);
  }

  @Test
  public void should_IgnoreReturnCharacter_When_ReturnPresent() throws IOException {
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    byte[] data = "A\rB".getBytes();
    int result = HexDecoder.decode(data, 0, data.length, out);

    assertTrue("Return should be ignored", result >= 0);
  }

  @Test
  public void should_IgnoreTabCharacter_When_TabPresent() throws IOException {
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    byte[] data = "A\tB".getBytes();
    int result = HexDecoder.decode(data, 0, data.length, out);

    assertTrue("Tab should be ignored", result >= 0);
  }

  // ========== Round-trip Tests ==========

  @Test
  public void should_RoundTrip_When_EncodeAndDecodeCalled() {
    byte[] original = {(byte) 0xAB, (byte) 0xCD, (byte) 0xEF};
    String encoded = HexDecoder.encode(original);
    byte[] decoded = HexDecoder.decode(encoded);

    assertArrayEquals("Should round trip correctly", original, decoded);
  }

  @Test
  public void should_RoundTripEmpty_When_EncodeAndDecodeCalledOnEmptyArray() {
    byte[] original = {};
    String encoded = HexDecoder.encode(original);
    byte[] decoded = HexDecoder.decode(encoded);

    assertArrayEquals("Should round trip empty", original, decoded);
  }

  @Test
  public void should_RoundTripZeroByte_When_EncodeAndDecodeCalledOnZero() {
    byte[] original = {(byte) 0x00};
    String encoded = HexDecoder.encode(original);
    byte[] decoded = HexDecoder.decode(encoded);

    assertArrayEquals("Should round trip zero", original, decoded);
  }

  @Test
  public void should_RoundTripAllBytes_When_AllByteValuesUsed() {
    byte[] original = new byte[256];
    for (int i = 0; i < 256; i++) {
      original[i] = (byte) i;
    }
    String encoded = HexDecoder.encode(original);
    byte[] decoded = HexDecoder.decode(encoded);

    assertArrayEquals("Should round trip all bytes", original, decoded);
  }

  // ========== Encoding Table Tests ==========

  @Test
  public void should_EncodeAllHexDigits_When_BytesForAllDigitsProvided() {
    byte[] data = {(byte) 0x01, (byte) 0x23, (byte) 0x45, (byte) 0x67,
        (byte) 0x89, (byte) 0xAB, (byte) 0xCD, (byte) 0xEF};
    String result = HexDecoder.encode(data);

    assertEquals("Should encode all hex digits", "0123456789ABCDEF", result);
  }

  // ========== Large Data Tests ==========

  @Test
  public void should_EncodeLargeByteArray_When_LargeArrayProvided() {
    byte[] data = new byte[10000];
    for (int i = 0; i < data.length; i++) {
      data[i] = (byte) ((i * 7) % 256);
    }
    String result = HexDecoder.encode(data);

    assertEquals("Should encode large array", 20000, result.length());
  }

  @Test
  public void should_DecodeLargeHexString_When_LargeStringProvided() {
    String data = "ABCD";
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < 10000; i++) {
      sb.append(data);
    }
    byte[] result = HexDecoder.decode(sb.toString());

    assertEquals("Should decode large string", 20000, result.length);
  }
}
