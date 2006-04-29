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

import org.castor.util.Base64Encoder;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public final class TestBase64Encoder extends TestCase {
    private static final String[] MAP = {
            "AA==", "AQ==", "Ag==", "Aw==", "BA==", "BQ==", "Bg==", "Bw==",
            "CA==", "CQ==", "Cg==", "Cw==", "DA==", "DQ==", "Dg==", "Dw==",
            "EA==", "EQ==", "Eg==", "Ew==", "FA==", "FQ==", "Fg==", "Fw==",
            "GA==", "GQ==", "Gg==", "Gw==", "HA==", "HQ==", "Hg==", "Hw==",
            "IA==", "IQ==", "Ig==", "Iw==", "JA==", "JQ==", "Jg==", "Jw==",
            "KA==", "KQ==", "Kg==", "Kw==", "LA==", "LQ==", "Lg==", "Lw==",
            "MA==", "MQ==", "Mg==", "Mw==", "NA==", "NQ==", "Ng==", "Nw==",
            "OA==", "OQ==", "Og==", "Ow==", "PA==", "PQ==", "Pg==", "Pw==",
            "QA==", "QQ==", "Qg==", "Qw==", "RA==", "RQ==", "Rg==", "Rw==",
            "SA==", "SQ==", "Sg==", "Sw==", "TA==", "TQ==", "Tg==", "Tw==",
            "UA==", "UQ==", "Ug==", "Uw==", "VA==", "VQ==", "Vg==", "Vw==",
            "WA==", "WQ==", "Wg==", "Ww==", "XA==", "XQ==", "Xg==", "Xw==",
            "YA==", "YQ==", "Yg==", "Yw==", "ZA==", "ZQ==", "Zg==", "Zw==",
            "aA==", "aQ==", "ag==", "aw==", "bA==", "bQ==", "bg==", "bw==",
            "cA==", "cQ==", "cg==", "cw==", "dA==", "dQ==", "dg==", "dw==",
            "eA==", "eQ==", "eg==", "ew==", "fA==", "fQ==", "fg==", "fw==",
            "gA==", "gQ==", "gg==", "gw==", "hA==", "hQ==", "hg==", "hw==",
            "iA==", "iQ==", "ig==", "iw==", "jA==", "jQ==", "jg==", "jw==",
            "kA==", "kQ==", "kg==", "kw==", "lA==", "lQ==", "lg==", "lw==",
            "mA==", "mQ==", "mg==", "mw==", "nA==", "nQ==", "ng==", "nw==",
            "oA==", "oQ==", "og==", "ow==", "pA==", "pQ==", "pg==", "pw==",
            "qA==", "qQ==", "qg==", "qw==", "rA==", "rQ==", "rg==", "rw==",
            "sA==", "sQ==", "sg==", "sw==", "tA==", "tQ==", "tg==", "tw==",
            "uA==", "uQ==", "ug==", "uw==", "vA==", "vQ==", "vg==", "vw==",
            "wA==", "wQ==", "wg==", "ww==", "xA==", "xQ==", "xg==", "xw==",
            "yA==", "yQ==", "yg==", "yw==", "zA==", "zQ==", "zg==", "zw==",
            "0A==", "0Q==", "0g==", "0w==", "1A==", "1Q==", "1g==", "1w==",
            "2A==", "2Q==", "2g==", "2w==", "3A==", "3Q==", "3g==", "3w==",
            "4A==", "4Q==", "4g==", "4w==", "5A==", "5Q==", "5g==", "5w==",
            "6A==", "6Q==", "6g==", "6w==", "7A==", "7Q==", "7g==", "7w==",
            "8A==", "8Q==", "8g==", "8w==", "9A==", "9Q==", "9g==", "9w==",
            "+A==", "+Q==", "+g==", "+w==", "/A==", "/Q==", "/g==", "/w==" };

    public static Test suite() {
        TestSuite suite = new TestSuite("Base64Encoder Tests");

        suite.addTest(new TestBase64Encoder("testAllBytes"));
        suite.addTest(new TestBase64Encoder("testPadding"));

        return suite;
    }

    public TestBase64Encoder(final String name) { super(name); }

    public void testAllBytes() {
        for (int i = 0; i < 256; i++) {
            assertEquals(MAP[i], new String(Base64Encoder.encode(new byte[] {(byte) i})));
        }
    }

    public void testPadding() {
        String str;

        str = new String(Base64Encoder.encode(new byte[] {33}));
        assertEquals("IQ==", str);

        str = new String(Base64Encoder.encode(new byte[] {33, 33}));
        assertEquals("ISE=", str);

        str = new String(Base64Encoder.encode(new byte[] {33, 33, 33}));
        assertEquals("ISEh", str);

        str = new String(Base64Encoder.encode(new byte[] {33, 33, 33, 34}));
        assertEquals("ISEhIg==", str);
    }
}
