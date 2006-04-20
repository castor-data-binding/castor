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
 * Copyright 2001-2003 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id$
 */

package org.exolab.adaptx.xslt.util;

import org.xml.sax.helpers.AttributeListImpl;

import java.util.Enumeration;
import java.util.Vector;

/**
 * A class for handling Namespace declaration and scoping
 *
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a>
 * @version $Revision$ $Date$
**/
public final class Namespaces {

    /**
     * The reserved XML Namespace Prefix 
     */
    public static final String XML_NAMESPACE_PREFIX = "xml";
    
    /**
     * The reserved XML 1.0 Namespace URI 
     */
    public static final String XML_NAMESPACE = "http://www.w3.org/XML/1998/namespace";
    
    
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
    public synchronized void addNamespace(String prefix, String uri) {

        if (uri == null) {
            throw new IllegalArgumentException("Namespace URI must not be null");
        }

        //-- adjust prefix to prevent null value
        if (prefix == null) prefix = "";

        //-- Make sure prefix is not equal to "xml"
        if (XML_NAMESPACE_PREFIX.equalsIgnoreCase(prefix)) {
            if (!XML_NAMESPACE.equals(uri)) {
                String err = "The prefix 'xml' is reserved (XML 1.0 Specification) " +
                    "and cannot be declared.";
                throw new IllegalArgumentException(err);
            }
            //-- if we make it here, just ignore it (it's already supported internally)
            return;
        }
        //-- make sure URI is not equal to the XML 1.0 namespace
        else if (XML_NAMESPACE.equals(uri)) {
            String err = "The namespace '" + XML_NAMESPACE;
            err += "' is reserved (XML 1.0 Specification) and cannot be declared.";
            throw new IllegalArgumentException(err);
        }
        
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
     * Returns an Enumeration of local namespace URIs for this Namespaces.
     *
     * @return an Enumeration of local namespace URIs.
    **/
    public Enumeration getLocalNamespaces() {
        return new NamespaceEnumerator(_first);
    } //-- getLocalNamespace

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
        
        //-- handle built-in namespace URIs
        if (XML_NAMESPACE_PREFIX.equals(prefix)) {
            return XML_NAMESPACE;
        }
        
        return null;

    } //-- method: getNamespaceURI

    /**
     * Returns the Namespace prefix associated with the given URI.
     * If multiple namespace prefixes have been declared, then
     * the first one found is returned. To obtain all prefixes see
     * <code>#getNamespacePrefixes</code>.
     *
     * @param nsURI the namespace URI to lookup
     * @return the namespace prefix associated with the given URI
    **/
    public String getNamespacePrefix(String nsURI) {
        //-- prevent null value
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
        
        //-- handle built-in namespace prefixes
        if (XML_NAMESPACE.equals(nsURI)) {
            return XML_NAMESPACE_PREFIX;
        }
        
        return null;

    } //-- method: getNamespacePrefix

    /**
     * Returns all namespace prefixes declared locally
     * 
     * @return an Enumeration of locally declared namespace prefixes
     */
    public Enumeration getLocalNamespacePrefixes() {
        return new NamespaceEnumerator(_first, NamespaceEnumerator.PREFIX);
    } //-- method: getLocalNamespacePrefixes

    /**
     * Returns all namespace prefixes associated with the given URI,
     * including those from parent scopes. 
     * 
     * @param nsURI the namespace URI to lookup
     * @return the namespace prefixes associated with the given URI
    **/
    public String[] getNamespacePrefixes(String nsURI) {
        return getNamespacePrefixes(nsURI, false);
    } //-- method: getNamespacePrefixes

    /**
     * Returns the Namespace prefixes associated with the given URI.
     *
     * @param nsURI the namespace URI to lookup
     * @param local a boolean that when true indicates only the local
     * scope is searched. 
     * @return the namespace prefixes associated with the given URI
    **/
    public String[] getNamespacePrefixes(String nsURI, boolean local) {
        //-- prevent null value
        if (nsURI == null)
            throw new IllegalArgumentException("Namespace URI must not be null.");

        Vector prefixes = new Vector(3);
        getNamespacePrefixes(nsURI, local, prefixes);
        
        String[] pArray = new String[prefixes.size()];
        prefixes.copyInto(pArray);
        return pArray;

    } //-- method: getNamespacePrefixes

    /**
     * Returns the Namespace prefix associated with the given URI.
     * Or null if no prefix has been declared. This method will
     * ignore the default namespace. This is useful when dealing
     * with attributes that do not use the default namespace.
     *
     * @param nsURI the namespace URI to lookup
     * @return the namespace prefix associated with the given URI
    **/
    public String getNonDefaultNamespacePrefix(String nsURI) {
        //-- adjust prefix to prevent null value
        if (nsURI == null)
            throw new IllegalArgumentException("Namespace URI must not be null.");

        Namespace ns = _first;
        while (ns != null) {
            if (ns.uri.equals(nsURI)) {
                if (ns.prefix.length() > 0) {
                    return ns.prefix;
                }
            }
            ns = ns.next;
        }

        if (_parent != null) {
            return _parent.getNonDefaultNamespacePrefix(nsURI);
        }
        
        //-- handle built-in namespace prefixes
        if (XML_NAMESPACE.equals(nsURI)) {
            return XML_NAMESPACE_PREFIX;
        }
        
        return null;

    } //-- method: getNonDefaultNamespacePrefix

    /**
     * Returns the parent Namespaces for this Namespaces instance.
     *
     * @return the parent Namespaces for this Namespaces instance.
    **/
    public Namespaces getParent() {
        return _parent;
    } //-- method: getParent

    /**
     * Removes the namespace declaration for the given prefix.
     * This is a local action only, the namespace declaration
     * will not be removed from any parent Namespaces object.
     *
     * @param prefix the namespace prefix to remove the binding of
     * @return true if the namespace declaration was removed, 
     * otherwise false.
     */
    public synchronized boolean removeNamespace(String prefix) {
        if (prefix == null) return false;
        
        Namespace ns = _first;
        Namespace previous = null;
        
        while (ns != null) {
            if (ns.prefix.equals(prefix)) {
                if (ns == _first) {
                    _first = _first.next;
                    if (_last == ns) {
                        _last = null;
                    }
                }
                else {
                    previous.next = ns.next;
                    if (_last == ns) {
                        _last = previous;
                    }
                }
                return true;
            }
            previous = ns;
            ns = ns.next;
        }

        return false;
        
    } //-- method: removeNamespace

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
    public void declareAsAttributes(AttributeListImpl atts, boolean localOnly) {

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
                    atts.addAttribute(attName, CDATA, ns.uri);
                }
                //case with no prefix but a nsURI
                else {
                   atts.addAttribute(XMLNS, CDATA, ns.uri);
                }
            } //ns.prefix!=null
            else {
                atts.addAttribute(XMLNS, CDATA, ns.uri);
            }

            ns = ns.next;
        }

        if ((!localOnly) && (_parent != null)) {
            _parent.declareAsAttributes(atts, false);
        }
    } //method:declareAsAttributes


    /**
     * Adds the namespace prefixes associated with the given URI to the
     * given Vector.
     *
     * @param nsURI the namespace URI to lookup
     * @param local a boolean that when true indicates only the local
     * scope is searched. 
     * @param prefixes the Vector to add the prefixes to
    **/
    private void getNamespacePrefixes(String nsURI, boolean local, Vector prefixes) {

        Namespace ns = _first;
        while (ns != null) {
            if (ns.uri.equals(nsURI)) {
                prefixes.addElement(ns.prefix);
            }
            ns = ns.next;
        }

        if ((_parent != null) && (!local)) {
            _parent.getNamespacePrefixes(nsURI, local, prefixes);
        }

    } //-- method: getNamespacePrefixes

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

    /**
     * A simple Enumeration for Namespace objects
     */
    static class NamespaceEnumerator
        implements java.util.Enumeration
    {
        public static final int URI = 0;
        public static final int PREFIX = 1;

        private Namespace _namespace = null;
        private int _returnType = URI;

        NamespaceEnumerator(Namespace namespace) {
            _namespace = namespace;
        }
        
        NamespaceEnumerator(Namespace namespace, int returnType) {
            _namespace = namespace;
            _returnType = returnType;
        }

        public boolean hasMoreElements() {
            return (_namespace != null);
        }

        public Object nextElement() {
            String obj = null;
            if (_namespace != null) {
                if (_returnType == URI)
                    obj = _namespace.uri;
                else 
                    obj = _namespace.prefix;
                _namespace = _namespace.next;
            }
            return obj;
        }
        
    } //-- class: NamespaceEnumerator

} //-- class: Namespaces