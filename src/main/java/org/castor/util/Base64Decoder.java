/*
 * Copyright 2005 Ralf Joachim
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.castor.util;

import java.io.ByteArrayOutputStream;

/**
 * Class decodes a Base64 encoded string back into the original byte representation
 * that can be read as byte array.
 *
 * @author <a href="mailto:ralf DOT joachim AT syscon DOT eu">Ralf Joachim</a>
 * @version $Revision$ $Date: 2005-08-05 13:58:36 -0600 (Fri, 05 Aug 2005) $
 * @since 0.9.9
 */
public final class Base64Decoder {
    /** Mask buffer to or with first sextet. */
    private static final int    SEXTET_1_MASK = 0x03FFFF;
    
    /** Mask buffer to or with second sextet. */
    private static final int    SEXTET_2_MASK = 0xFC0FFF;

    /** Mask buffer to or with third sextet. */
    private static final int    SEXTET_3_MASK = 0xFFF03F;
    
    /** Mask buffer to or with forth sextet. */
    private static final int    SEXTET_4_MASK = 0xFFFFC0;
    
    /** Number of bits to shift for one sextet. */
    private static final int    SHIFT_1_SEXTET = 6;
    
    /** Number of bits to shift for two sextet. */
    private static final int    SHIFT_2_SEXTET = 12;
    
    /** Number of bits to shift for three sextet. */
    private static final int    SHIFT_3_SEXTET = 18;
    
    /** Second sextets in buffer. */
    private static final int    SEXTET_2 = 2;
    
    /** Third sextets in buffer. */
    private static final int    SEXTET_3 = 3;
    
    /** Forth sextets in buffer. */
    private static final int    SEXTET_4 = 4;
    
    /** Mask an octet. */
    private static final int    OCTET_MASK = 0xFF;
    
    /** Number of bits to shift for one octet. */
    private static final int    SHIFT_1_OCTET = 8;
    
    /** Number of bits to shift for two octet. */
    private static final int    SHIFT_2_OCTET = 16;
    
    /** White space character (out of range 0 - 63). */
    private static final byte SPC = 127;

    /** Padding character (out of range 0 - 63). */
    private static final byte PAD = 64;

    /** Array to translate base64 characters into sextet byte values. */
    private static final byte[] MAP = {
            SPC, SPC, SPC, SPC, SPC, SPC, SPC, SPC,     // 00-07
            SPC, SPC, SPC, SPC, SPC, SPC, SPC, SPC,     // 08-0F
            SPC, SPC, SPC, SPC, SPC, SPC, SPC, SPC,     // 10-17
            SPC, SPC, SPC, SPC, SPC, SPC, SPC, SPC,     // 18-1F
            SPC, SPC, SPC, SPC, SPC, SPC, SPC, SPC,     // 20-27
            SPC, SPC, SPC,  62, SPC, SPC, SPC,  63,     // 28-2F '   +   /'
             52,  53,  54,  55,  56,  57,  58,  59,     // 30-37 '01234567'
             60,  61, SPC, SPC, SPC, PAD, SPC, SPC,     // 38-3F '89   =  '
            SPC,   0,   1,   2,   3,   4,   5,   6,     // 40-47 ' ABCDEFG'
              7,   8,   9,  10,  11,  12,  13,  14,     // 48-4F 'HIJKLMNO'
             15,  16,  17,  18,  19,  20,  21,  22,     // 50-57 'PQRSTUVW'
             23,  24,  25, SPC, SPC, SPC, SPC, SPC,     // 58-5F 'XYZ     '
            SPC,  26,  27,  28,  29,  30,  31,  32,     // 60-67 ' abcdefg'
             33,  34,  35,  36,  37,  38,  39,  40,     // 68-6F 'hijklmno'
             41,  42,  43,  44,  45,  46,  47,  48,     // 70-77 'pqrstuvw'
             49,  50,  51, SPC, SPC, SPC, SPC, SPC,     // 78-7F 'xyz     '
            SPC, SPC, SPC, SPC, SPC, SPC, SPC, SPC,     // 80-87
            SPC, SPC, SPC, SPC, SPC, SPC, SPC, SPC,     // 88-8F
            SPC, SPC, SPC, SPC, SPC, SPC, SPC, SPC,     // 90-97
            SPC, SPC, SPC, SPC, SPC, SPC, SPC, SPC,     // 98-9F
            SPC, SPC, SPC, SPC, SPC, SPC, SPC, SPC,     // A0-A7
            SPC, SPC, SPC, SPC, SPC, SPC, SPC, SPC,     // A8-AF
            SPC, SPC, SPC, SPC, SPC, SPC, SPC, SPC,     // B0-B7
            SPC, SPC, SPC, SPC, SPC, SPC, SPC, SPC,     // B8-BF
            SPC, SPC, SPC, SPC, SPC, SPC, SPC, SPC,     // C0-C7
            SPC, SPC, SPC, SPC, SPC, SPC, SPC, SPC,     // C8-CF
            SPC, SPC, SPC, SPC, SPC, SPC, SPC, SPC,     // D0-D7
            SPC, SPC, SPC, SPC, SPC, SPC, SPC, SPC,     // D8-DF
            SPC, SPC, SPC, SPC, SPC, SPC, SPC, SPC,     // E0-E7
            SPC, SPC, SPC, SPC, SPC, SPC, SPC, SPC,     // E8-EF
            SPC, SPC, SPC, SPC, SPC, SPC, SPC, SPC,     // F0-F7
            SPC, SPC, SPC, SPC, SPC, SPC, SPC, SPC,     // F8-FF
        };

    /** 24-bit buffer to translate 4 sextets into 3 octets. */
    private int                     _buffer = 0;

    /** Number of octets in buffer. */
    private int                     _sextets = 0;

    /** Stream buffer for decoded octets waiting to be read. */
    private ByteArrayOutputStream   _stream = new ByteArrayOutputStream();
    
    /**
     * Decode given string into a decoded byte array.
     * 
     * @param str Base64 String to be decoded.
     * @return All decoded octets as byte array.
     */
    public static byte[] decode(final String str) {
        Base64Decoder dec = new Base64Decoder();
        dec.translate(str);
        return dec.getByteArray();
    }

    /**
     * Construct a default Base64Decoder waiting on calls to its translate()
     * method.
     */
    public Base64Decoder() { }

    /**
     * Translate every base64 character from given string into a sextet byte value
     * by using above translation array. The sextets are then shiftet into an buffer
     * until the buffer contains 4 sextets which are then decoded into 3 octets.
     * The translate and decode process is continued until all characters of given
     * string are evaluated. If there are remaing sextets in the buffer they also
     * will be converted into octets at the end. All the converted octets are added
     * to the list for later read.
     * 
     * @param string    Base64 String to be decoded.
     */
    public void translate(final String string) {
        int len = string.length();
        int index = 0;
        int data = MAP[string.charAt(index)];
        while ((index < len) && (data != PAD)) {
            if (data != SPC) {
                if (_sextets == 0) {
                    _buffer = (_buffer & SEXTET_1_MASK) | (data << SHIFT_3_SEXTET);
                } else if (_sextets == 1) {
                    _buffer = (_buffer & SEXTET_2_MASK) | (data << SHIFT_2_SEXTET);
                } else if (_sextets == 2) {
                    _buffer = (_buffer & SEXTET_3_MASK) | (data << SHIFT_1_SEXTET);
                } else {
                    _buffer = (_buffer & SEXTET_4_MASK) | data;
                }

                if ((++_sextets) == SEXTET_4) { decode(); }
            }

            if (++index < len) { data = MAP[string.charAt(index)]; }
        }

        if (_sextets > 0) { decodeWithPadding(); }
    }

    /**
     * Decode 3 octets from buffer and add them to list of octets to read.
     */
    private void decode() {
        _stream.write((byte) ((_buffer >> SHIFT_2_OCTET) & OCTET_MASK));     // octet 1
        _stream.write((byte) ((_buffer >> SHIFT_1_OCTET) & OCTET_MASK));     // octet 2
        _stream.write((byte) (_buffer & OCTET_MASK));                        // octet 3
        _buffer = 0;
        _sextets = 0;
    }

    /**
     * Decode the remaining octets from buffer and add them to list of octets to read.
     */
    private void decodeWithPadding() {
        if (_sextets >= SEXTET_2) {                                     // octet 1
            _stream.write((byte) ((_buffer >> SHIFT_2_OCTET) & OCTET_MASK));
        }
        if (_sextets >= SEXTET_3) {                                     // octet 2
            _stream.write((byte) ((_buffer >> SHIFT_1_OCTET) & OCTET_MASK));
        }
        if (_sextets >= SEXTET_4) {                                     // octet 3
            _stream.write((byte) (_buffer & OCTET_MASK));
        }
        _buffer = 0;
        _sextets = 0;
    }

    /**
     * Get all decoded octets as byte array.
     * 
     * @return All decoded octets as byte array.
     */
    public byte[] getByteArray() {
        return _stream.toByteArray();
    }
}
