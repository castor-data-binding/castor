/*
 * Copyright 2007 Edward Kuns
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
 *
 * $Id: Location.java 0000 2007-01-09 00:10:00-0600 ekuns $
 */
package org.castor.xmlctf.xmldiff.xml;

import org.xml.sax.Locator;

/**
 * An immutable class for storing XML file location information.
 *
 * @author <a href="mailto:edward.kuns@aspect.com">Edward Kuns</a>
 * @version $Revision: 0000 $ $Date: 2007-01-09 00:10:00 -0600 (Tue, 09 Jan 2007) $
 * @since Castor 1.1
 */
public class Location {

    /** Line number. */
    private final int _line;
    /** Column number. */
    private final int _column;

    /**
     * Creates a new Location with the current values from the provided Locator.
     *
     * @param locator a SAX locator
     */
    public Location(Locator locator) {
        _line   = locator.getLineNumber();
        _column = locator.getColumnNumber();
    }

    /**
     * Returns the column number.
     * @return the column number.
     */
    public int getColumnNumber() {
        return _column;
    }

    /**
     * Returns the line number.
     * @return the line number.
     */
    public int getLineNumber() {
        return _line;
    }

}
