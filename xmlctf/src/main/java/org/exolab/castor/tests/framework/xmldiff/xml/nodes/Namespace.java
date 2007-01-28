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
 * $Id: Namespace.java 0000 2007-01-11 00:00:00Z ekuns $
 */
package org.exolab.castor.tests.framework.xmldiff.xml.nodes;

/**
 * A class representing a Namespace for an Element.  Objects of this type
 * are not part of the XML node tree.
 *
 * @author <a href="mailto:edward.kuns@aspect.com">Edward Kuns</a>
 * @version $Revision: 0000 $ $Date: 2007-01-11 00:00:00 -0600 (Thu, 11 Jan 2007) $
 * @since Castor 1.1
 */
public class Namespace  {

    /** Prefix for this Namespace */
    private final String      _prefix;
    /** Namespace URI of this Namespace. */
    private final String      _namespaceUri;

    /**
     * Creates a new Namespace object for an XML Element.
     *
     * @param prefix the prefix of this namespace binding. (May be null.)
     * @param namespaceURI the namespace URI mapped to this prefix. (May be
     *        null.)
     */
    public Namespace(final String prefix, final String namespaceURI) {
        _prefix        = prefix;
        _namespaceUri  = namespaceURI;
    }

    /**
     * Returns the prefix of this namespace.
     * @return The prefix of this namespace.
     */
    public String getPrefix() {
        return _prefix;
    }

    /**
     * Returns the string value of the namespace.
     * @return The string value of the namespace.
     */
    public String getNamespaceUri() {
        return _namespaceUri;
    }

}
