/*
 *
 * The contents of this file are subject to the Netscape Public
 * License Version 1.1 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of
 * the License at http://www.mozilla.org/NPL/
 *
 * Software distributed under the License is distributed on an "AS
 * IS" basis, WITHOUT WARRANTY OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * rights and limitations under the License.
 *
 * The Original Code is mozilla.org code.
 *
 * The Initial Developer of the Original Code is Netscape
 * Communications Corporation.  Portions created by Netscape are
 * Copyright (C) 1999 Netscape Communications Corporation. All
 * Rights Reserved.
 *
 * Contributor(s): 
 */


package org.exolab.castor.util;


import java.io.ByteArrayOutputStream;


/**
 * Base 64 text to byte decoded. To produce the binary  array from
 * base 64 encoding call {@link #translate} for each sequence of
 * characters and {@link #getByteArray} to mark closure of the
 * character stream and retrieve the binary contents.
 *
 * @author Based on code from the Mozilla Directory SDK
 */
public final class MimeBase64Decoder
{


    private ByteArrayOutputStream out = new ByteArrayOutputStream();

    private byte token[] = new byte[4];      // input buffer

    private byte bytes[] = new byte[3];      // output buffer

    private int token_length = 0;            // input buffer length

    static private final byte NUL = 127;     // must be out of range 0-64

    static private final byte EOF = 126;     // must be out of range 0-64

    static private final byte map[] = {
        NUL, NUL, NUL, NUL, NUL, NUL, NUL, NUL,      //   000-007
        NUL, NUL, NUL, NUL, NUL, NUL, NUL, NUL,      //   010-017
        NUL, NUL, NUL, NUL, NUL, NUL, NUL, NUL,      //   020-027
        NUL, NUL, NUL, NUL, NUL, NUL, NUL, NUL,      //   030-037
        NUL, NUL, NUL, NUL, NUL, NUL, NUL, NUL,      //   040-047   !"#$%&'
        NUL, NUL, NUL, 62,  NUL, NUL, NUL, 63,      //   050-057  ()*+,-./
        52,  53,  54,  55,  56,  57,  58,  59,      //   060-067  01234567
        60,  61,  NUL, NUL, NUL, EOF, NUL, NUL,      //   070-077  89:;<=>?
        
        NUL,   0,   1,   2,   3,   4,   5,   6,      //   100-107  @ABCDEFG
        7,   8,   9,  10,  11,  12,  13,  14,      //   110-117  HIJKLMNO
        15,  16,  17,  18,  19,  20,  21,  22,      //   120-127  PQRSTUVW
        23,  24,  25, NUL, NUL, NUL, NUL, NUL,      //   130-137  XYZ[\]^_
        NUL,  26,  27,  28,  29,  30,  31,  32,      //   140-147  `abcdefg
        33,  34,  35,  36,  37,  38,  39,  40,      //   150-157  hijklmno
        41,  42,  43,  44,  45,  46,  47,  48,      //   160-167  pqrstuvw
        49,  50,  51, NUL, NUL, NUL, NUL, NUL,      //   170-177  xyz{|}~
        
        NUL, NUL, NUL, NUL, NUL, NUL, NUL, NUL,      //   200-207
        NUL, NUL, NUL, NUL, NUL, NUL, NUL, NUL,      //   210-217
        NUL, NUL, NUL, NUL, NUL, NUL, NUL, NUL,      //   220-227
        NUL, NUL, NUL, NUL, NUL, NUL, NUL, NUL,      //   230-237
        NUL, NUL, NUL, NUL, NUL, NUL, NUL, NUL,      //   240-247
        NUL, NUL, NUL, NUL, NUL, NUL, NUL, NUL,      //   250-257
        NUL, NUL, NUL, NUL, NUL, NUL, NUL, NUL,      //   260-267
        NUL, NUL, NUL, NUL, NUL, NUL, NUL, NUL,      //   270-277
        
        NUL, NUL, NUL, NUL, NUL, NUL, NUL, NUL,      //   300-307
        NUL, NUL, NUL, NUL, NUL, NUL, NUL, NUL,      //   310-317
        NUL, NUL, NUL, NUL, NUL, NUL, NUL, NUL,      //   320-327
        NUL, NUL, NUL, NUL, NUL, NUL, NUL, NUL,      //   330-337
        NUL, NUL, NUL, NUL, NUL, NUL, NUL, NUL,      //   340-347
        NUL, NUL, NUL, NUL, NUL, NUL, NUL, NUL,      //   350-357
        NUL, NUL, NUL, NUL, NUL, NUL, NUL, NUL,      //   360-367
        NUL, NUL, NUL, NUL, NUL, NUL, NUL, NUL,      //   370-377
    };


    // Fast routine that assumes full 4-char tokens with no '=' in them.
    //
    private void decode_token()
    {
        int num = ((token[0] << 18) |
                   (token[1] << 12) |
                   (token[2] << 6) |
                   (token[3]));
        
        bytes[0] = (byte) (0xFF & (num >> 16));
        bytes[1] = (byte) (0xFF & (num >> 8));
        bytes[2] = (byte) (0xFF & num);
        
        out.write(bytes,0,3);
    }
    
    
    // Hairier routine that deals with the final token, which can have fewer
    // than four characters, and that might be padded with '='.
    //
    private final void decode_final_token()
    {
        
        byte b0 = token[0];
        byte b1 = token[1];
        byte b2 = token[2];
        byte b3 = token[3];
        
        int eq_count = 0;

        if (b0 == EOF) { b0 = 0; eq_count++; }
        if (b1 == EOF) { b1 = 0; eq_count++; }
        if (b2 == EOF) { b2 = 0; eq_count++; }
        if (b3 == EOF) { b3 = 0; eq_count++; }

        int num = ((b0 << 18) | (b1 << 12) | (b2 << 6) | (b3));

        // eq_count will be 0, 1, or 2.
        // No "=" padding means 4 bytes mapped to 3, the normal case,
        //        not handled in this routine.
        // "xxx=" means 3 bytes mapped to 2.
        // "xx==" means 2 bytes mapped to 1.
        // "x===" can't happen, because "x" would then be encoding
        //        only 6 bits, not 8, the minimum possible.
        
        out.write((byte) (num >> 16));             // byte 1, count = 0 or 1 or 2
        if (eq_count <= 1) {
            out.write((byte) ((num >> 8) & 0xFF)); // byte 2, count = 0 or 1
            if (eq_count == 0) {
                out.write((byte) (num & 0xFF));    // byte 3, count = 0
            }
        }
    }


    public final void translate( char[] ch, int offset, int length )
    {
        if (token == null) // already saw eof marker?
            return;
        
        for (int i = offset; i < offset + length; i++) {
            byte t = map[(ch[i]&0xff)];
            
            if ( t == EOF ) {
                eof();
            } else if (t != NUL) {
                token[token_length++] = t;
            }
            if (token_length == 4) {
                decode_token();
                token_length = 0;
            }
        }
    }
    

    public final void translate( String str )
    {
        if (token == null) // already saw eof marker?
            return;
        int length = str.length();
        for (int i = 0; i < length; i++) {
            byte t = map[(str.charAt(i)&0xff)];
            
            if ( t == EOF ) {
                eof();
            } else if (t != NUL) {
                token[token_length++] = t;
            }
            if (token_length == 4) {
                decode_token();
                token_length = 0;
            }
        }
    }


    private void eof()
    {
        if (token != null && token_length != 0) {
            while (token_length < 4)
                token[token_length++] = EOF;
            decode_final_token();
        }
        token_length = 0;
        token = new byte[4];
        bytes = new byte[3];
    }
    
    
    public byte[] getByteArray()
    {
        eof();
        return out.toByteArray();
    }


}
