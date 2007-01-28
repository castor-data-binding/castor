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
 * $Id: Element.java 0000 2007-01-11 00:00:00Z ekuns $
 */
package org.exolab.castor.tests.framework.xmldiff.xml.nodes;

import java.util.Iterator;
import java.util.LinkedList;

import org.exolab.castor.tests.framework.xmldiff.xml.Location;

/**
 * A representation of an Element XML node.
 *
 * @author <a href="mailto:edward.kuns@aspect.com">Edward Kuns</a>
 * @version $Revision: 0000 $ $Date: 2007-01-11 00:00:00 -0600 (Thu, 11 Jan 2007) $
 * @since Castor 1.1
 */
public class Element extends ParentNode {

    /** Serial Version UID. */
    private static final long serialVersionUID = 7995110660306473483L;
    /** Our list of Attributes. */
    private final LinkedList  _attributes      = new LinkedList();
    /** Our list of Namespaces. */
    private final LinkedList  _namespaces      = new LinkedList();
    /** Our location */
    private Location          _location        = null;

    /**
     * Creates a new Element.
     *
     * @param namespace the namespace URI for this node. (May be null.)
     * @param localName the localname of this node. (Cannot be null.)
     */
    public Element(final String namespace, final String localName) {
        super(namespace, localName, XMLNode.ELEMENT);
    }

    /**
     * Returns an Iterator over the List of Attributes.
     * @return an Iterator over the List of Attributes.
     */
    public Iterator getAttributeIterator() {
        return _attributes.iterator();
    }

    /**
     * Returns the value of the named attribute, or null if the node has no such
     * attribute. If the argument <tt>uri</tt> is null, the node's namespace
     * URI will be used.
     *
     * @param uri The attribute's namespace URI, or null
     * @param localName The attribute's local name
     * @return The attribute's value, or null if no such attribute exists
     */
    public String getAttribute(final String uri, final String localName) {
        for (Iterator i = _attributes.iterator(); i.hasNext();) {
            Attribute attr = (Attribute) i.next();
            if (namespacesEqual(uri, attr.getNamespaceURI())
                && attr.getLocalName().equals(localName)) {
                return attr.getStringValue();
            }
        }
        return null;
    }

    /**
     * Returns the namespace URI associated with this namespace prefix, as
     * defined in the context of this node. Returns null if the prefix is
     * undefined. Returns an empty String if the prefix is defined and
     * associated with no namespace.
     *
     * @param prefix The namespace prefix
     * @return The namespace URI, or null.
     */
    public String getNamespaceURI(final String prefix) {
        String desiredPrefix = (prefix == null) ? "" : prefix;

        for (Iterator i = _namespaces.iterator(); i.hasNext(); ) {
            Namespace ns = (Namespace) i.next();
            if (desiredPrefix.equals(ns.getPrefix())) {
                return ns.getNamespaceUri();
            }
        }

        // If we didn't find it, search our parent
        return super.getNamespaceURI(desiredPrefix);
    }

    /**
     * Returns the namespace prefix associated with this namespace URI, as
     * defined in the context of this node. Returns null if no prefix is defined
     * for this namespace URI. Returns an empty string if the default prefix is
     * associated with this namespace URI.
     *
     * @param uri The namespace URI
     * @return The namespace prefix, or null
     */
    public String getNamespacePrefix(final String uri) {
        String desiredUri = (uri == null) ? "" : uri;

        for (Iterator i = _namespaces.iterator(); i.hasNext(); ) {
            Namespace ns = (Namespace) i.next();
            if (desiredUri.equals(ns.getNamespaceUri())) {
                return ns.getPrefix();
            }
        }

        // If we didn't find it locally, search our parent
        ParentNode parent = getParentNode();
        if (parent != null && parent instanceof Element) {
            return ((Element)parent).getNamespacePrefix(desiredUri);
        }

        // If no parent, return null
        return null;
    }

    /**
     * Adds the given Attribute to this Element.
     *
     * @param attr the Attribute to add
     */
    public void addAttribute(final Attribute attr) {
        if (attr == null) {
            return;
        }

        attr.setParent(this);
        _attributes.add(attr);
    }

    /**
     * Adds the given Namespace to this Element.
     *
     * @param namespace the Namespace to add
     */
    public void addNamespace(final Namespace namespace) {
        if (namespace == null) {
            return;
        }

        _namespaces.add(namespace);
    }

    /**
     * Returns true if the given two namespace URI strings are equal.
     *
     * @param ns1 The first namespace URI to compare
     * @param ns2 The second namespace URI to compare
     * @return true if the given two namespace URI strings are equal.
     */
    private boolean namespacesEqual(final String ns1, final String ns2) {
        final String namespace1 = (ns1 == null) ? "" : ns1;
        final String namespace2 = (ns2 == null) ? "" : ns2;

        return namespace1.equals(namespace2);
    }

    /**
     * Sets the location of this Element in the document.
     * @param location the location of this Element in the document.
     */
    public void setLocation(final Location location) {
        _location = location;
    }

    /**
     * Returns the location of this Element in the document.
     * @return the location of this Element in the document.
     */
    public Location getLocation() {
        return _location;
    }

}
