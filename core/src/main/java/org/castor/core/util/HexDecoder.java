/*
 * Copyright 2007 Werner Guttmann
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
package org.castor.core.util;

import java.io.IOException;
import java.io.OutputStream;
import java.io.ByteArrayOutputStream;

/**
 * Hex encoder/decoder implementation (borrowed from BouncyCastle=.
 * 
 * @author Johan Lindquist
 * @since 1.1.1
 * @version $Revision$
 */
public final class HexDecoder {
    
    /**
     * Identifies the data type supported by this decoder.
     */
    public static final String DATA_TYPE = "hexBinary";

    /**
     * Initial size of the decoding table.
     */
    private static final int DECODING_TABLE_SIZE = 128;

    /**
     * Encoding table.
     */
    protected static final byte[] ENCODING_TABLE = {
        (byte) '0', (byte) '1', (byte) '2', (byte) '3',
        (byte) '4', (byte) '5', (byte) '6', (byte) '7',
        (byte) '8', (byte) '9', (byte) 'A', (byte) 'B',
        (byte) 'C', (byte) 'D', (byte) 'E', (byte) 'F' 
    };

    /**
     * Decoding table.
     */
    protected static final byte[] DECODING_TABLE = new byte[DECODING_TABLE_SIZE];

    /**
     * Initialize the decoding table.
     */
    protected static void initialiseDecodingTable() {
        for (int i = 0; i < ENCODING_TABLE.length; i++) {
            DECODING_TABLE[ENCODING_TABLE[i]] = (byte) i;
        }

        // deal with lower case letters as well
        DECODING_TABLE['a'] = DECODING_TABLE['A'];
        DECODING_TABLE['b'] = DECODING_TABLE['B'];
        DECODING_TABLE['c'] = DECODING_TABLE['C'];
        DECODING_TABLE['d'] = DECODING_TABLE['D'];
        DECODING_TABLE['e'] = DECODING_TABLE['E'];
        DECODING_TABLE['f'] = DECODING_TABLE['F'];
    }

    static {
        initialiseDecodingTable();
    }

    /**
     * Creates an instance of this class. 
     */
    private HexDecoder() {
        // Nothing to do ...
    }

    /**
     * Encodes the input data producing a Hex output stream.
     * @param data The input data to be HEX encoded
     * @param off Initiak offset
     * @param length Initial length of the input data array
     * @param out The {@link OutputStream} instance holding the encoded input data.
     * @return the number of bytes produced.
     * @throws IOException If encoding fails.
     */
    public static int encode(final byte[] data, final int off, final int length, 
            final OutputStream out) throws IOException {
        for (int i = off; i < (off + length); i++) {
            int v = data[i] & 0xff;

            out.write(ENCODING_TABLE[(v >>> 4)]);
            out.write(ENCODING_TABLE[v & 0xf]);
        }

        return length * 2;
    }

    /**
     * Indicates whether a given character should be ignored during en-/decoding.
     * @param c The character at question.
     * @return True if the given character should be ignored.
     */
    private static boolean ignore(final char c) {
        return (c == '\n' || c == '\r' || c == '\t' || c == ' ');
    }

    /**
     * Decodes the Hex encoded byte data writing it to the given output stream,
     * whitespace characters will be ignored.
     * @param data The data to be encoded
     * @param off Initial offset.
     * @param length Initial length
     * @param out The {@link OutputStream} instance
     * @return the number of bytes produced.
     * @throws IOException If encoding failed.
     */
    public static int decode(final byte[] data, final int off, final int length,
            final OutputStream out) throws IOException {
        byte b1, b2;
        int outLen = 0;

        int end = off + length;

        while (end > off) {
            if (!ignore((char) data[end - 1])) {
                break;
            }

            end--;
        }

        int i = off;
        while (i < end) {
            while (i < end && ignore((char) data[i])) {
                i++;
            }

            b1 = DECODING_TABLE[data[i++]];

            while (i < end && ignore((char) data[i])) {
                i++;
            }

            b2 = DECODING_TABLE[data[i++]];

            out.write((b1 << 4) | b2);

            outLen++;
        }

        return outLen;
    }

    /**
     * Decodes the Hex encoded String data writing it to the given output stream,
     * whitespace characters will be ignored.
     * 
     * @param data The data to be encoded
     * @param out The {@link OutputStream} instance
     * @return the number of bytes produced.
     * @throws IOException If encoding failed.
     */
    public static int decode(final String data, final OutputStream out) throws IOException {
        byte b1, b2;
        int length = 0;

        int end = data.length();

        while (end > 0) {
            if (!ignore(data.charAt(end - 1))) {
                break;
            }

            end--;
        }

        int i = 0;
        while (i < end) {
            while (i < end && ignore(data.charAt(i))) {
                i++;
            }

            b1 = DECODING_TABLE[data.charAt(i++)];

            while (i < end && ignore(data.charAt(i))) {
                i++;
            }

            b2 = DECODING_TABLE[data.charAt(i++)];

            out.write((b1 << 4) | b2);

            length++;
        }

        return length;
    }

    /**
     * Encodes the input data producing a Hex output stream.
     * @param data Input data to encode.
     * @return the number of bytes produced.
     */
    public static String encode(final byte[] data) {
        try {
            final ByteArrayOutputStream out = new ByteArrayOutputStream();
            encode(data, 0, data.length, out);
            out.close();
            return new String(out.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * Decodes the HEX input data producing a output stream.
     * @param data Input data to be decoded.
     * @return A byte array representing the decoded input data.
     */
    public static byte[] decode(final String data) {
        try {
            final ByteArrayOutputStream out = new ByteArrayOutputStream();
            decode(data, out);
            out.close();
            return out.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage(), e);
        }
    }

}
