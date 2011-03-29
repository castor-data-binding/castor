/*
 * Copyright 2009 Ralf Joachim
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
package org.exolab.castor.types;

import java.text.ParseException;

import junit.framework.TestCase;

public class TestTime extends TestCase {
    public void testLocal() {
        Time someTime = null;
        try {
            someTime = new Time("23:01:02");
        } catch (ParseException e) {
            fail("Probem parsing date");
        }
        long temp = someTime.toLong();
        Time otherTime = new Time(temp, false);
        assertTrue(someTime.equals(otherTime));
    }

    public void testTimeZone() {
        Time someTime = null;
        try {
            someTime = new Time("23:01:02+05:00");
        } catch (ParseException e) {
            fail("Probem parsing date");
        }
        long temp = someTime.toLong();
        Time otherTime = new Time(temp, true);
        assertTrue(someTime.equals(otherTime));
    }

    public void testTimeZoneOverflow() {
        Time someTime = null;
        try {
            someTime = new Time("23:01:02-05:00");
        } catch (ParseException e) {
            fail("Probem parsing date");
        }
        long temp = someTime.toLong();
        Time otherTime = new Time(temp, true);
        assertTrue(someTime.equals(otherTime));
    }
}
