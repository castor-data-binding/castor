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
 * $Id: Text.java 0000 2007-01-11 00:00:00Z ekuns $
 */
package org.exolab.castor.tests.framework.xmldiff.xml.nodes;

/**
 * A class representing an XML Text node.
 *
 * @author <a href="mailto:edward.kuns@aspect.com">Edward Kuns</a>
 * @version $Revision: 0000 $ $Date: 2007-01-11 00:00:00 -0600 (Thu, 11 Jan 2007) $
 * @since Castor 1.1
 */
public class Text extends XMLNode {

    /** Serial Version UID. */
    private static final long serialVersionUID = 6684442960507498460L;
    /** The value of this text node. */
    private String _value = null;

    /**
     * Creates a new Text node with the given initial value.
     *
     * @param value the text value of this XML Text node.
     */
    public Text(final String value) {
        super(null, null, XMLNode.TEXT);
        _value = value;
    }

    /**
     * Returns the string value of this Text node.
     * @return The string value of this Text node.
     */
    public String getStringValue() {
        return _value;
    }

    /**
     * Sets the value for this text node.
     * @param value The new value for this text node.
     */
    public void setValue(final String value) {
        this._value = value;
    }

}
