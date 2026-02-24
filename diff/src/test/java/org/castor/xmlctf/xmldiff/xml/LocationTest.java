/*
 * Copyright 2007 Edward Kuns
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
package org.castor.xmlctf.xmldiff.xml;

import static org.junit.Assert.*;

import org.junit.Test;
import org.xml.sax.Locator;

public class LocationTest {

    private static class MockLocator implements Locator {
        private int lineNumber;
        private int columnNumber;

        public MockLocator(int line, int column) {
            this.lineNumber = line;
            this.columnNumber = column;
        }

        @Override
        public String getPublicId() {
            return null;
        }

        @Override
        public String getSystemId() {
            return null;
        }

        @Override
        public int getLineNumber() {
            return lineNumber;
        }

        @Override
        public int getColumnNumber() {
            return columnNumber;
        }
    }

    @Test
    public void should_CreateLocation_WithValidLocator() {
        Locator locator = new MockLocator(5, 10);
        Location location = new Location(locator);

        assertEquals(5, location.getLineNumber());
        assertEquals(10, location.getColumnNumber());
    }

    @Test
    public void should_ReturnLineNumber() {
        Locator locator = new MockLocator(100, 50);
        Location location = new Location(locator);

        assertEquals(100, location.getLineNumber());
    }

    @Test
    public void should_ReturnColumnNumber() {
        Locator locator = new MockLocator(100, 50);
        Location location = new Location(locator);

        assertEquals(50, location.getColumnNumber());
    }

    @Test
    public void should_HandleStartingLineAndColumn() {
        Locator locator = new MockLocator(1, 0);
        Location location = new Location(locator);

        assertEquals(1, location.getLineNumber());
        assertEquals(0, location.getColumnNumber());
    }

    @Test
    public void should_HandleLargeLineNumbers() {
        Locator locator = new MockLocator(100000, 500);
        Location location = new Location(locator);

        assertEquals(100000, location.getLineNumber());
    }

    @Test
    public void should_HandleLargeColumnNumbers() {
        Locator locator = new MockLocator(50, 100000);
        Location location = new Location(locator);

        assertEquals(100000, location.getColumnNumber());
    }

    @Test
    public void should_HandleZeroLineNumber() {
        Locator locator = new MockLocator(0, 10);
        Location location = new Location(locator);

        assertEquals(0, location.getLineNumber());
    }

    @Test
    public void should_HandleZeroColumnNumber() {
        Locator locator = new MockLocator(10, 0);
        Location location = new Location(locator);

        assertEquals(0, location.getColumnNumber());
    }

    @Test
    public void should_HandleBothZero() {
        Locator locator = new MockLocator(0, 0);
        Location location = new Location(locator);

        assertEquals(0, location.getLineNumber());
        assertEquals(0, location.getColumnNumber());
    }

    @Test
    public void should_CreateMultipleLocations() {
        Location loc1 = new Location(new MockLocator(1, 0));
        Location loc2 = new Location(new MockLocator(2, 5));
        Location loc3 = new Location(new MockLocator(3, 10));

        assertEquals(1, loc1.getLineNumber());
        assertEquals(2, loc2.getLineNumber());
        assertEquals(3, loc3.getLineNumber());
    }

    @Test
    public void should_HandleConsecutiveLocations() {
        Location[] locations = new Location[5];
        for (int i = 0; i < 5; i++) {
            locations[i] = new Location(new MockLocator(i + 1, i * 10));
        }

        for (int i = 0; i < 5; i++) {
            assertEquals(i + 1, locations[i].getLineNumber());
            assertEquals(i * 10, locations[i].getColumnNumber());
        }
    }

    @Test
    public void should_PreserveMidDocumentLocation() {
        Locator locator = new MockLocator(150, 75);
        Location location = new Location(locator);

        assertEquals(150, location.getLineNumber());
        assertEquals(75, location.getColumnNumber());
    }

    @Test
    public void should_HandleEndOfDocumentLocation() {
        Locator locator = new MockLocator(500, 1000);
        Location location = new Location(locator);

        assertEquals(500, location.getLineNumber());
        assertEquals(1000, location.getColumnNumber());
    }

    @Test
    public void should_HandleSingleCharacterLine() {
        Locator locator = new MockLocator(10, 1);
        Location location = new Location(locator);

        assertEquals(10, location.getLineNumber());
        assertEquals(1, location.getColumnNumber());
    }

    @Test
    public void should_HandleVariousLineAndColumnCombinations() {
        int[][] testCases = {
            {1, 0}, {1, 1}, {10, 50}, {100, 100}, {1000, 5000}
        };

        for (int[] testCase : testCases) {
            Location location = new Location(new MockLocator(testCase[0], testCase[1]));
            assertEquals(testCase[0], location.getLineNumber());
            assertEquals(testCase[1], location.getColumnNumber());
        }
    }

    @Test
    public void should_ReturnImmutableValues() {
        Locator locator = new MockLocator(5, 10);
        Location location = new Location(locator);

        int line1 = location.getLineNumber();
        int col1 = location.getColumnNumber();

        int line2 = location.getLineNumber();
        int col2 = location.getColumnNumber();

        assertEquals(line1, line2);
        assertEquals(col1, col2);
    }

    @Test
    public void should_HandleTypicalXMLStartLocation() {
        Locator locator = new MockLocator(1, 0);
        Location location = new Location(locator);

        assertEquals(1, location.getLineNumber());
        assertEquals(0, location.getColumnNumber());
    }

    @Test
    public void should_HandleTypicalXMLEndLocation() {
        Locator locator = new MockLocator(10, 50);
        Location location = new Location(locator);

        assertEquals(10, location.getLineNumber());
        assertEquals(50, location.getColumnNumber());
    }

    @Test
    public void should_CreateLocationFromVariousLocators() {
        for (int i = 1; i <= 10; i++) {
            Location location = new Location(new MockLocator(i, i * 2));
            assertEquals(i, location.getLineNumber());
            assertEquals(i * 2, location.getColumnNumber());
        }
    }

}
