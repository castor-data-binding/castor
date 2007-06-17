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
package org.castor.util;

import java.util.Arrays;
import java.util.Random;

import junit.framework.TestCase;

/**
 * JUnit test case for HEX en-/coding.
 * @author Johan Lindquist
 * @version $Revision$
 */
public class TestHex extends TestCase {

    public void testEncodeDecode() {
        byte[] bytes = new byte[256];

        new Random(1L).nextBytes(bytes);
        final String encodedBytes = HexDecoder.encode(bytes);
        assertEquals(
                "Bad encoded data",
                "73d51abbd89cb8196f0efb6892f94d68fccc2c35f0b84609e5f12c55dd85aba8d5d9bef76808f3b572e5900112b81927ba5bb5f67e1bda28b4049bf0e4aed78db15d7bf2fc0c34e9a99de4ef3bc2b17c8137ad659878f9e93df1f658367aca286452474b9ef3765e24e9a88173724dddfb04b01dcceb0c8aead641c58dad569581baeea87c10d40a47902028e61cfdc243d9d16008aabc9fb77cc723a56017e14f1ce8b1698341734a6823ce02043e016b544901214a2ddab82fec85c0b9fe0549c475be5b887bb4b8995b24fb5c6846f88b527b4f9d4c1391f1678b23ba4f9c9cd7bc93eb5776f4f03675344864294661c5949faf17b130fcf6482f971a5500",
                encodedBytes);

        final byte[] decodedBytes = HexDecoder.decode(encodedBytes);

        assertTrue("Bad decoded bytes", Arrays.equals(bytes, decodedBytes));

    }

}
