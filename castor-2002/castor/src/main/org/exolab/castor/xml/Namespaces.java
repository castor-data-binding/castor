/**
 * Redistribution and use of this software and associated documentation
 * ("Software"), with or without modification, are permitted provided
 * that the following conditions are met:
 *
 * 1. Redistributions of source code must retain copyright
 *    statements and notices.  Redistributions must also contain a
 *    copy of this document.
 *
 * 2. Redistributions in binary form must reproduce the
 *    above copyright notice, this list of conditions and the
 *    following disclaimer in the documentation and/or other
 *    materials provided with the distribution.
 *
 * 3. The name "Exolab" must not be used to endorse or promote
 *    products derived from this Software without prior written
 *    permission of Intalio, Inc.  For written permission,
 *    please contact info@exolab.org.
 *
 * 4. Products derived from this Software may not be called "Exolab"
 *    nor may "Exolab" appear in their names without prior written
 *    permission of Intalio, Inc. Exolab is a registered
 *    trademark of Intalio, Inc.
 *
 * 5. Due credit should be given to the Exolab Project
 *    (http://www.exolab.org/).
 *
 * THIS SOFTWARE IS PROVIDED BY INTALIO, INC. AND CONTRIBUTORS
 * ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT
 * NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL
 * INTALIO, INC. OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * Copyright 2001 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id$
 */

package org.exolab.castor.xml;

import org.xml.sax.helpers.AttributeListImpl;
/**
 * A class for handling Namespace declaration and scoping
 *
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a>
 * @version $Revision$ $Date$
**/
final class Namespaces {

    /**
     * The first namespace in this set of Namespaces
    **/
    private Namespace _first = null;

    /**
     * The last namespace in this set of Namespaces
    **/
    private Namespace _last  = null;


    private Namespaces _parent = null;

    /**
     * The CDATA type..uses for SAX attributes
     */
    private static final String CDATA = "CDATA";

    /**
     * The namespace declaration String
    **/
    private static final String XMLNS  = "xmlns";


    /**
     * Creates a new Namespaces instance
    **/
    public Namespaces() {
        super();
    } //-- Namespaces

    /**
     * Creates a new Namespaces instance
    **/
    public Namespaces(Namespaces parent) {
        super();
        _parent = parent;
    } //-- Namespaces

    /**
     * Adds the given namespace declaration to this Namespaces
     *
     * @param prefix the namespace prefix
     * @param uri the namespace URI to be associated with the given prefix
    **/
    public void addNamespace(String prefix, String uri) {

        if (uri == null) {
            throw new IllegalArgumentException("Namespace URI must not be null");
        }

        //-- adjust prefix to prevent null value
        if (prefix == null) prefix = "";

        if (_first == null) {
            _first = new Namespace(prefix, uri);
            _last  = _first;
        }
        //-- check for existing namespace declaration for
        //-- prefix
        else {
            boolean found = false;
            Namespace ns = _first;
            while (ns != null) {
                if (ns.prefix.equals(prefix)) {
                    found = true;
                    ns.uri = uri;
                    break;
                }
                ns = ns.next;
            }
            if (!found) {
                _last.next = new Namespace(prefix, uri);
                _last = _last.next;
            }
        }
    } //-- method: addNamespace

    /**
     * Creates a new Namespaces instance with this Namespaces as the parent
    **/
    public Namespaces createNamespaces() {
        return new Namespaces(this);
    } //-- method: createNamespaces

    /**
     * Returns the Namespace URI associated with the given prefix
     *
     * @param prefix the namespace prefix to lookup
     * @return the namespace URI associated with the given prefix
    **/
    public String getNamespaceURI(String prefix) {
        //-- adjust prefix to prevent null value
        if (prefix == null) prefix = "";

        Namespace ns = _first;
        while (ns != null) {
            if (ns.prefix.equals(prefix)) {
                return ns.uri;
            }
            ns = ns.next;
        }

        if (_parent != null) {
            return _parent.getNamespaceURI(prefix);
        }
        return null;

    } //-- method: getNamespaceURI

     /**
     * Returns the Namespace prefix associated with the given URI
     *
     * @param nsURI the namespace URI to lookup
     * @return the namespace prefix associated with the given URI
    **/
    public String getNamespacePrefix(String nsURI) {
        //-- adjust prefix to prevent null value
        if (nsURI == null)
            throw new IllegalArgumentException("Namespace URI must not be null.");

        Namespace ns = _first;
        while (ns != null) {
            if (ns.uri.equals(nsURI)) {
                return ns.prefix;
            }
            ns = ns.next;
        }

        if (_parent != null) {
            return _parent.getNamespacePrefix(nsURI);
        }
        return null;

    } //-- method: getNamespaceURI

    /**
     * Returns the parent Namespaces for this Namespaces instance.
     *
     * @return the parent Namespaces for this Namespaces instance.
    **/
    public Namespaces getParent() {
        return _parent;
    } //-- method: getParent

    /**
     * Sets the parent Namespaces for this Namespaces instance.
     *
     * @param namespaces the parent Namespaces
    **/
    public void setParent(Namespaces namespaces) {
        _parent = namespaces;
    } //-- method: setParent

    /**
     * Declare the namespaces of this stack in as attributes.
     * @param atts the Attribute List to fill in.
     */
    public void declareAsAttributes(AttributeListImpl atts) {

        Namespace ns = _first;
        String attName = null;
        while (ns != null) {
            if (ns.prefix != null) {
                int len = ns.prefix.length();
                if (len > 0) {
                    StringBuffer buf = new StringBuffer(6+len);
                    buf.append(XMLNS);
                    buf.append(':');
                    buf.append(ns.prefix);
                    attName = buf.toString();
                }
                atts.addAttribute(attName, CDATA, getNamespaceURI(ns.prefix));
            }

            ns = ns.next;
        }

        if (_parent != null) {
            _parent.declareAsAttributes(atts);
        }
    } //method:declareAsAttributes


    /**
     * An internal class used to represent a namespace
    **/
    class Namespace {

        String prefix = null;
        String uri    = null;

        Namespace next = null;

        Namespace() {
            super();
        }

        Namespace(String prefix, String uri) {
            this.prefix = prefix;
            this.uri    = uri;
        }
    } //-- class: Namespace

} //-- class: Namespaces