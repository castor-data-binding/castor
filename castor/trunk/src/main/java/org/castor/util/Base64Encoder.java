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

/**
 * Class encodes the bytes written to the OutPutStream to a Base64 encoded string.
 * The encoded string can be retrieved by as a whole by the toString() method or
 * splited into lines of 72 characters by the toStringArray() method.
 *
 * @author <a href="mailto:ralf DOT joachim AT syscon-world DOT de">Ralf Joachim</a>
 * @version $Revision$ $Date: 2005-08-05 13:58:36 -0600 (Fri, 05 Aug 2005) $
 * @since 0.9.9
 */
 public final class Base64Encoder {
    /** Third octets in buffer. */
    private static final int    OCTET_3 = 3;
    
    /** Mask buffer to or with first octet. */
    private static final int    OCTET_1_MASK = 0x00FFFF;
    
    /** Mask buffer to or with second octet. */
    private static final int    OCTET_2_MASK = 0xFF00FF;

    /** Mask buffer to or with third octet. */
    private static final int    OCTET_3_MASK = 0xFFFF00;
    
    /** Mask an octet. */
    private static final int    OCTET_MASK = 0xFF;
    
    /** Number of bits to shift for one octet. */
    private static final int    SHIFT_1_OCTET = 8;
    
    /** Number of bits to shift for two octet. */
    private static final int    SHIFT_2_OCTET = 16;
    
    /** Mask a sextet. */
    private static final int    SEXTET_MASK = 0x3F;
    
    /** Number of bits to shift for one sextet. */
    private static final int    SHIFT_1_SEXTET = 6;
    
    /** Number of bits to shift for two sextet. */
    private static final int    SHIFT_2_SEXTET = 12;
    
    /** Number of bits to shift for three sextet. */
    private static final int    SHIFT_3_SEXTET = 18;
    
    /** Array to convert sextet byte values into base64 characters. */
    private static final char[] MAP = {
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H',     // 00-07
            'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P',     // 08-15
            'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X',     // 16-23
            'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f',     // 24-31
            'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n',     // 32-39
            'o', 'p', 'q', 'r', 's', 't', 'u', 'v',     // 40-47
            'w', 'x', 'y', 'z', '0', '1', '2', '3',     // 48-55
            '4', '5', '6', '7', '8', '9', '+', '/',     // 56-63
        };

    /** 24-bit buffer to translate 3 octets into 4 sextets. */
    private int     _buffer = 0;

    /** Number of octets in buffer. */
    private int     _octets = 0;

    /** Stream buffer for encoded characters waiting to be read. */
    private StringBuffer    _stream = new StringBuffer();

    /**
     * Encode given byte array into a encoded character array.
     * 
     * @param bytes The byte array to be encoded.
     * @return Base64 encoded characters as an array.
     */
    public static char[] encode(final byte[] bytes) {
        Base64Encoder enc = new Base64Encoder();
        enc.translate(bytes);
        return enc.getCharArray();
    }

    /**
     * Construct a Base64Encoder.
     */
    public Base64Encoder() { }

    /**
     * Reset Base64Encoder to its initial state. Take care using this method as it
     * throws all previously written bytes away.
     */
    public void reset() {
        _buffer = 0;
        _octets = 0;
        _stream = new StringBuffer();
    }

    /**
     * Translate all bytes of given array by appending each to octet buffer. If
     * buffer contains 3 octets its content will be encoded to 4 sextet byte values
     * which are converted to a base64 character each. All characters are appended
     * to a StringBuffer.
     * 
     * @param bytes The byte array to be encoded.
     */
    public void translate(final byte[] bytes) {
        for (int i = 0; i < bytes.length; i++) {
            byte b = bytes[i];

            if (_octets == 0) {
                _buffer = (_buffer & OCTET_1_MASK) | ((b & OCTET_MASK) << SHIFT_2_OCTET);
            } else if (_octets == 1) {
                _buffer = (_buffer & OCTET_2_MASK) | ((b & OCTET_MASK) << SHIFT_1_OCTET);
            } else {
                _buffer = (_buffer & OCTET_3_MASK) | (b & OCTET_MASK);
            }

            if ((++_octets) == OCTET_3) { encode(); }
        }
    }

    /**
     * Encode 4 sextets from buffer and add them to StringBuffer.
     */
    private void encode() {
        _stream.append(MAP[SEXTET_MASK & (_buffer >> SHIFT_3_SEXTET)]);   // sextet 1
        _stream.append(MAP[SEXTET_MASK & (_buffer >> SHIFT_2_SEXTET)]);   // sextet 2
        _stream.append(MAP[SEXTET_MASK & (_buffer >> SHIFT_1_SEXTET)]);   // sextet 3
        _stream.append(MAP[SEXTET_MASK & _buffer]);                       // sextet 4
        _buffer = 0;
        _octets = 0;
    }

    /**
     * Encode the remaining sextets from buffer and add them to to StringBuffer.
     */
    private void encodeWithPadding() {
        _stream.append(MAP[SEXTET_MASK & (_buffer >> SHIFT_3_SEXTET)]);   // sextet 1
        _stream.append(MAP[SEXTET_MASK & (_buffer >> SHIFT_2_SEXTET)]);   // sextet 2
        if (_octets <= 1) {                                               // sextet 3
            _stream.append('=');
        } else {
            _stream.append(MAP[SEXTET_MASK & (_buffer >> SHIFT_1_SEXTET)]);
        }
        if (_octets <= 2) {                                               // sextet 4
            _stream.append('=');
        } else {
            _stream.append(MAP[SEXTET_MASK & _buffer]);
        }
        _buffer = 0;
        _octets = 0;
    }

    /**
     * Get Base64 encoded characters as an array.
     * 
     * @return Base64 encoded characters as an array.
     */
    public char[] getCharArray() {
        if (_octets > 0) { encodeWithPadding(); }
        char[] chars = new char[_stream.length()];
        if (_stream.length() > 0) { _stream.getChars(0, _stream.length(), chars, 0); }
        return chars;
    }
}
