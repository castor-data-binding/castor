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
 * $Id: Attribute.java 0000 2007-01-11 00:00:00Z ekuns $
 */
package org.castor.xmlctf.xmldiff.xml.nodes;


/**
 * A class representing an Attribute XML node.
 *
 * @author <a href="mailto:edward.kuns@aspect.com">Edward Kuns</a>
 * @version $Revision: 0000 $ $Date: 2007-01-11 00:00:00 -0600 (Thu, 11 Jan 2007) $
 * @since Castor 1.1
 */
public class Attribute extends XMLNode {

    /** Serial Version UID. */
    private static final long serialVersionUID = 2499101510478363466L;
    /** Attribute value. */
    private final String      _value;

    /**
     * Creates a new Attribute.
     *
     * @param namespace the namespace URI for this node. (May be null.)
     * @param localName the localname of this node. (Cannot be null.)
     * @param value the String value of this attribute. (Cannot be null.)
     */
    public Attribute(String namespace, String localName, String value) {
        super(namespace, localName, XMLNode.ATTRIBUTE);
        if (localName == null || localName.length() == 0) {
            throw new IllegalArgumentException("An Attribute localname must be non-null "
                                               + "and must have a non-zero length");
        }
        this._value = value;
    } // -- Attribute

    /**
     * Returns the string value of this attribute.
     * @return The string value of this attribute.
     */
    public String getStringValue() {
        return _value;
    }

}
