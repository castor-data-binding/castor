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
                "73D51ABBD89CB8196F0EFB6892F94D68FCCC2C35F0B84609E5F12C55DD85ABA8D5D9BEF76808F3B572E5900112B81927BA5BB5F67E1BDA28B4049BF0E4AED78DB15D7BF2FC0C34E9A99DE4EF3BC2B17C8137AD659878F9E93DF1F658367ACA286452474B9EF3765E24E9A88173724DDDFB04B01DCCEB0C8AEAD641C58DAD569581BAEEA87C10D40A47902028E61CFDC243D9D16008AABC9FB77CC723A56017E14F1CE8B1698341734A6823CE02043E016B544901214A2DDAB82FEC85C0B9FE0549C475BE5B887BB4B8995B24FB5C6846F88B527B4F9D4C1391F1678B23BA4F9C9CD7BC93EB5776F4F03675344864294661C5949FAF17B130FCF6482F971A5500",
                encodedBytes);

        final byte[] decodedBytes = HexDecoder.decode(encodedBytes);

        assertTrue("Bad decoded bytes", Arrays.equals(bytes, decodedBytes));

    }

}
