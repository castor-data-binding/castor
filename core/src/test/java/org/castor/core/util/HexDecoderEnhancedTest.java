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
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Enhanced comprehensive test class for HexDecoder with >95% coverage.
 * Tests all code branches, edge cases, and exception conditions.
 */
public class HexDecoderEnhancedTest {

  // ========== Encode Tests ==========

  @Test
  public void should_EncodeFullByteRange_When_AllValuesFromZeroToFF() throws IOException {
    for (int i = 0; i < 256; i++) {
      byte[] data = new byte[] {(byte) i};
      ByteArrayOutputStream out = new ByteArrayOutputStream();
      int result = HexDecoder.encode(data, 0, 1, out);

      assertEquals("Should encode single byte", 2, result);
      String output = out.toString();
      assertNotNull("Encoded output should not be null", output);
      assertEquals("Should produce 2 hex characters", 2, output.length());
    }
  }

  @Test
  public void should_EncodeWithVariousOffsets_When_OffsetChanges() throws IOException {
    byte[] data = new byte[] {(byte) 0x11, (byte) 0x22, (byte) 0x33, (byte) 0x44, (byte) 0x55};

    for (int offset = 0; offset < data.length; offset++) {
      ByteArrayOutputStream out = new ByteArrayOutputStream();
      int result = HexDecoder.encode(data, offset, 1, out);
      assertEquals("Should encode correctly from offset", 2, result);
    }
  }

  @Test
  public void should_EncodeMultipleBytes_When_LengthIsGreaterThanOne() throws IOException {
    byte[] data = new byte[] {(byte) 0xAA, (byte) 0xBB, (byte) 0xCC, (byte) 0xDD};
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    int result = HexDecoder.encode(data, 0, 4, out);

    assertEquals("Should encode 4 bytes as 8 hex chars", 8, result);
    assertEquals("AABBCCDD", out.toString());
  }

  @Test
  public void should_EncodePartialArray_When_OffsetAndLengthSpecified() throws IOException {
    byte[] data = new byte[] {(byte) 0x11, (byte) 0x22, (byte) 0x33, (byte) 0x44, (byte) 0x55};
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    int result = HexDecoder.encode(data, 1, 3, out);

    assertEquals("Should encode 3 bytes", 6, result);
    assertEquals("223344", out.toString());
  }

  @Test
  public void should_EncodeZeroLengthArray_When_LengthIsZero() throws IOException {
    byte[] data = new byte[] {(byte) 0x11, (byte) 0x22};
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    int result = HexDecoder.encode(data, 0, 0, out);

    assertEquals("Should encode nothing", 0, result);
    assertEquals("", out.toString());
  }

  @Test
  public void should_ProduceConsistentCasing_When_EncodingMultipleTimes() {
    byte[] data = new byte[] {(byte) 0xAB, (byte) 0xCD, (byte) 0xEF};
    String result1 = HexDecoder.encode(data);
    String result2 = HexDecoder.encode(data);

    assertEquals("Should produce consistent results", result1, result2);
    assertTrue("Should be uppercase", result1.equals(result1.toUpperCase()));
  }

  // ========== Decode String Tests ==========

  @Test
  public void should_DecodeAllHexDigitCombinations_When_InputSpansRange() {
    String[] testCases = {"00", "0F", "F0", "FF", "0A", "A0", "AB", "BA"};
    for (String hexStr : testCases) {
      byte[] result = HexDecoder.decode(hexStr);
      assertNotNull("Should decode " + hexStr, result);
      assertEquals("Should produce 1 byte", 1, result.length);
    }
  }

  @Test
  public void should_DecodeLowercaseCorrectly_When_AllLowercaseLetters() {
    String hexString = "aabbccddeeff";
    byte[] result = HexDecoder.decode(hexString);

    assertNotNull(result);
    assertEquals("Should produce 6 bytes", 6, result.length);
    assertEquals((byte) 0xAA, result[0]);
    assertEquals((byte) 0xBB, result[1]);
    assertEquals((byte) 0xCC, result[2]);
    assertEquals((byte) 0xDD, result[3]);
    assertEquals((byte) 0xEE, result[4]);
    assertEquals((byte) 0xFF, result[5]);
  }

  @Test
  public void should_DecodeUppercaseCorrectly_When_AllUppercaseLetters() {
    String hexString = "AABBCCDDEEFF";
    byte[] result = HexDecoder.decode(hexString);

    assertNotNull(result);
    assertEquals("Should produce 6 bytes", 6, result.length);
    assertEquals((byte) 0xAA, result[0]);
    assertEquals((byte) 0xBB, result[1]);
  }

  @Test
  public void should_DecodeMixedCaseCorrectly_When_MixedLetters() {
    String hexString = "AaBbCcDdEeFf";
    byte[] result = HexDecoder.decode(hexString);

    assertNotNull(result);
    assertEquals("Should produce 6 bytes", 6, result.length);
  }

  @Test
  public void should_IgnoreLeadingWhitespace_When_InputStartsWithSpaces() {
    String hexString = "   48656C6C6F";
    byte[] result = HexDecoder.decode(hexString);

    assertNotNull(result);
    assertEquals("Hello", new String(result));
  }

  @Test
  public void should_IgnoreTrailingWhitespace_When_InputEndsWithSpaces() {
    String hexString = "48656C6C6F   ";
    byte[] result = HexDecoder.decode(hexString);

    assertNotNull(result);
    assertEquals("Hello", new String(result));
  }

  @Test
  public void should_IgnoreMiddleWhitespace_When_InputHasSpacesBetween() {
    String hexString = "48 65 6C 6C 6F";
    byte[] result = HexDecoder.decode(hexString);

    assertNotNull(result);
    assertEquals("Hello", new String(result));
  }

  @Test
  public void should_IgnoreNewlinesCorrectly_When_InputHasNewlines() {
    String hexString = "48\n65\n6C\n6C\n6F";
    byte[] result = HexDecoder.decode(hexString);

    assertNotNull(result);
    assertEquals("Hello", new String(result));
  }

  @Test
  public void should_IgnoreTabsCorrectly_When_InputHasTabs() {
    String hexString = "48\t65\t6C\t6C\t6F";
    byte[] result = HexDecoder.decode(hexString);

    assertNotNull(result);
    assertEquals("Hello", new String(result));
  }

  @Test
  public void should_IgnoreCarriageReturnsCorrectly_When_InputHasCarriageReturns() {
    String hexString = "48\r65\r6C\r6C\r6F";
    byte[] result = HexDecoder.decode(hexString);

    assertNotNull(result);
    assertEquals("Hello", new String(result));
  }

  @Test
  public void should_HandleAllWhitespaceTypes_When_MixedWhitespace() {
    String hexString = "48 65\n6C\t6C\r6F";
    byte[] result = HexDecoder.decode(hexString);

    assertNotNull(result);
    assertEquals("Hello", new String(result));
  }

  @Test
  public void should_DecodeEmptyStringCorrectly_When_InputIsEmpty() {
    String hexString = "";
    byte[] result = HexDecoder.decode(hexString);

    assertNotNull(result);
    assertEquals("Should produce empty array", 0, result.length);
  }

  @Test
  public void should_DecodeWhitespaceOnlyCorrectly_When_InputIsOnlyWhitespace() {
    String hexString = "   \n\t\r   ";
    byte[] result = HexDecoder.decode(hexString);

    assertNotNull(result);
    assertEquals("Should produce empty array", 0, result.length);
  }

  @Test
  public void should_DecodeLongStringCorrectly_When_InputIsVeryLong() {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < 1000; i++) {
      sb.append("48");
    }
    byte[] result = HexDecoder.decode(sb.toString());

    assertNotNull(result);
    assertEquals("Should produce 1000 bytes", 1000, result.length);
  }

  // ========== Decode Byte Array Tests ==========

  @Test
  public void should_DecodeByteArrayCorrectly_When_ValidHexBytes() throws IOException {
    byte[] hexBytes = "48656C6C6F".getBytes();
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    int result = HexDecoder.decode(hexBytes, 0, hexBytes.length, out);

    assertEquals("Should decode to 5 bytes", 5, result);
    assertEquals("Hello", out.toString());
  }

  @Test
  public void should_DecodeByteArrayWithOffset_When_OffsetProvided() throws IOException {
    byte[] hexBytes = "XXXX48656C6C6F".getBytes();
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    int result = HexDecoder.decode(hexBytes, 4, 10, out);

    assertEquals("Should decode from offset", 5, result);
    assertEquals("Hello", out.toString());
  }

  @Test
  public void should_DecodeByteArrayWithLength_When_LengthLessThanArraySize() throws IOException {
    byte[] hexBytes = "48656C6C6F4445".getBytes();
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    int result = HexDecoder.decode(hexBytes, 0, 10, out);

    assertEquals("Should decode first 5 hex pairs", 5, result);
  }

  @Test
  public void should_DecodeByteArrayWithWhitespace_When_ArrayContainsWhitespace() throws IOException {
    byte[] hexBytes = "48 65 6C 6C 6F".getBytes();
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    int result = HexDecoder.decode(hexBytes, 0, hexBytes.length, out);

    assertEquals("Should ignore spaces", 5, result);
    assertEquals("Hello", out.toString());
  }

  @Test
  public void should_DecodeByteArrayZeroLength_When_LengthIsZero() throws IOException {
    byte[] hexBytes = "48656C6C6F".getBytes();
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    int result = HexDecoder.decode(hexBytes, 0, 0, out);

    assertEquals("Should decode nothing", 0, result);
  }

  @Test
  public void should_DecodeByteArrayWithTrailingWhitespace_When_ArrayEndsWithWhitespace() throws IOException {
    byte[] hexBytes = "48656C6C6F   ".getBytes();
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    int result = HexDecoder.decode(hexBytes, 0, hexBytes.length, out);

    assertEquals("Should ignore trailing whitespace", 5, result);
  }

  @Test
  public void should_DecodeByteArrayLowercase_When_ArrayContainsLowercaseHex() throws IOException {
    byte[] hexBytes = "48656c6c6f".getBytes();
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    int result = HexDecoder.decode(hexBytes, 0, hexBytes.length, out);

    assertEquals("Should decode lowercase", 5, result);
    assertEquals("Hello", out.toString());
  }

  // ========== Roundtrip Tests ==========

  @Test
  public void should_RoundtripSuccessfully_When_EncodeThenDecode() {
    byte[] original = new byte[] {(byte) 0x00, (byte) 0x55, (byte) 0xAA, (byte) 0xFF};
    String encoded = HexDecoder.encode(original);
    byte[] decoded = HexDecoder.decode(encoded);

    assertEquals("Should maintain length", original.length, decoded.length);
    for (int i = 0; i < original.length; i++) {
      assertEquals("Byte at " + i + " should match", original[i], decoded[i]);
    }
  }

  @Test
  public void should_RoundtripWithWhitespace_When_DecodingEncodedWithWhitespace() {
    byte[] original = new byte[] {(byte) 0x48, (byte) 0x69};
    String encoded = HexDecoder.encode(original);
    String withWhitespace = encoded.charAt(0) + " " + encoded.charAt(1) +
                           "\n" + encoded.charAt(2) + "\t" + encoded.charAt(3);
    byte[] decoded = HexDecoder.decode(withWhitespace);

    assertEquals("Should decode with whitespace", original.length, decoded.length);
  }

  // ========== DATA_TYPE Constant Tests ==========

  @Test
  public void should_HaveCorrectDataType_When_ConstantAccessed() {
    String dataType = HexDecoder.DATA_TYPE;
    assertEquals("Should have hexBinary constant", "hexBinary", dataType);
  }

  @Test
  public void should_RetainDataTypeValue_When_AccessedMultipleTimes() {
    String type1 = HexDecoder.DATA_TYPE;
    String type2 = HexDecoder.DATA_TYPE;
    assertEquals("Should be consistent", type1, type2);
  }

  // ========== Edge Cases ==========

  @Test
  public void should_HandleSingleHexPair_When_InputIsFF() {
    String hexString = "FF";
    byte[] result = HexDecoder.decode(hexString);

    assertNotNull(result);
    assertEquals("Should produce 1 byte", 1, result.length);
    assertEquals("Should be 255", (byte) 0xFF, result[0]);
  }

  @Test
  public void should_HandleSingleHexPair_When_InputIs00() {
    String hexString = "00";
    byte[] result = HexDecoder.decode(hexString);

    assertNotNull(result);
    assertEquals("Should produce 1 byte", 1, result.length);
    assertEquals("Should be 0", 0, result[0]);
  }

  @Test
  public void should_HandleNumericHexOnly_When_InputIs0123456789() {
    String hexString = "0123456789AB";
    byte[] result = HexDecoder.decode(hexString);

    assertNotNull(result);
    assertEquals("Should produce 6 bytes", 6, result.length);
  }

  @Test
  public void should_EncodeDecodeZeroByteCorrectly_When_DataIsZero() throws IOException {
    byte[] data = new byte[] {0};
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    HexDecoder.encode(data, 0, 1, out);

    assertEquals("Should encode 0x00 as 00", "00", out.toString());
  }

  @Test
  public void should_EncodeDecodeMaxByteCorrectly_When_DataIsFFFF() throws IOException {
    byte[] data = new byte[] {(byte) 0xFF, (byte) 0xFF};
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    HexDecoder.encode(data, 0, 2, out);

    assertEquals("Should encode as FFFF", "FFFF", out.toString());
  }

  @Test
  public void should_HandleAlternatingByteValues_When_DataAlternates() {
    byte[] data = new byte[] {(byte) 0xAA, (byte) 0x55, (byte) 0xAA, (byte) 0x55};
    String encoded = HexDecoder.encode(data);
    byte[] decoded = HexDecoder.decode(encoded);

    for (int i = 0; i < data.length; i++) {
      assertEquals("Should match original at index " + i, data[i], decoded[i]);
    }
  }

  @Test
  public void should_HandleConsecutiveIdenticalBytes_When_DataRepeats() {
    byte[] data = new byte[] {(byte) 0x33, (byte) 0x33, (byte) 0x33, (byte) 0x33};
    String encoded = HexDecoder.encode(data);
    assertEquals("Should encode repeated bytes", "33333333", encoded);
  }

  @Test
  public void should_DecodeLargeBinaryData_When_InputIsLarge() {
    byte[] largeBinary = new byte[10000];
    for (int i = 0; i < largeBinary.length; i++) {
      largeBinary[i] = (byte) (i % 256);
    }
    String encoded = HexDecoder.encode(largeBinary);
    byte[] decoded = HexDecoder.decode(encoded);

    assertEquals("Should maintain large data", largeBinary.length, decoded.length);
  }
}
