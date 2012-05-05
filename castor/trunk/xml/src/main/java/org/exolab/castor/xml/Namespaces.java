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

package org.exolab.castor.xml;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.castor.core.util.Assert;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributeListImpl;

/**
 * A class for handling Namespace declaration and scoping
 * 
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a>
 * @version $Revision$ $Date: 2004-09-09 23:04:08 -0600 (Thu, 09 Sep
 *          2004) $
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
    * The CDATA type..uses for SAX attributes
    */
   private static final String CDATA = "CDATA";

   /**
    * The namespace declaration String
    **/
   private static final String XMLNS = "xmlns";

   /**
    * Represents a collection of all registered namespaces.
    */
   private final List<Namespace> namespaces = new ArrayList<Namespace>();

   /**
    * Represents a {@link Map} instance that contains all registered namespaces.
    */
   private final Map<String, Namespace> namespaceMap = new HashMap<String, Namespace>();

   public Namespaces() {
      super();
      namespaceMap.put(XML_NAMESPACE_PREFIX, new Namespace(XML_NAMESPACE_PREFIX, XML_NAMESPACE));
   }
   /**
    * Adds the given namespace declaration to this Namespaces instance
    * 
    * @param prefix
    *           the namespace prefix
    * @param uri
    *           the namespace URI to be associated with the given prefix
    * 
    * @throws IllegalArgumentException
    *            if uri is null
    **/
   public synchronized void addNamespace(String prefix, String uri) {

      // checks the input parameter
      Assert.notNull(uri, "Namespace URI must not be null");

      // -- adjust prefix to prevent null value
      if (prefix == null)
         prefix = "";

      // -- Make sure prefix is not equal to "xml"
      if (XML_NAMESPACE_PREFIX.equalsIgnoreCase(prefix)) {
         if (!XML_NAMESPACE.equals(uri)) {
            String err = "The prefix 'xml' is reserved (XML 1.0 Specification) " + "and cannot be declared.";
            throw new IllegalArgumentException(err);
         }
         // -- if we make it here, just ignore it (it's already supported
         // internally)
         return;
      }
      // -- make sure URI is not equal to the XML 1.0 namespace
      else if (XML_NAMESPACE.equals(uri)) {
         String err = "The namespace '" + XML_NAMESPACE;
         err += "' is reserved (XML 1.0 Specification) and cannot be declared.";
         throw new IllegalArgumentException(err);
      }

      // adds the namespace
      Namespace namespace;
      if (namespaceMap.containsKey(prefix)) {
         namespaceMap.get(prefix).setUri(uri);
      } else {
         namespace = new Namespace(prefix, uri);
         namespaces.add(namespace);
         namespaceMap.put(prefix, namespace);
      }
   }

   /**
    * Returns an Enumeration of local namespace URIs for this Namespaces.
    * 
    * @return an Enumeration of local namespace URIs.
    **/
   public Enumeration<String> getLocalNamespaces() {
      return new NamespaceEnumerator(namespaces.iterator());
   }

   /**
    * Returns the Namespace URI associated with the given prefix
    * 
    * @param prefix
    *           the namespace prefix to lookup
    * @return the namespace URI associated with the given prefix; null if the
    *         given namespace prefix is not bound.
    **/
   public String getNamespaceURI(String prefix) {
      // -- adjust prefix to prevent null value
      if (prefix == null)
         prefix = "";

//      // -- handle built-in namespace URIs
//      if (XML_NAMESPACE_PREFIX.equals(prefix)) {
//         return XML_NAMESPACE;
//      }

      Namespace namespace = namespaceMap.get(prefix);

      if (namespace != null) {
         return namespace.getUri();
      }

      return null;
   }

   /**
    * Returns the Namespace prefix associated with the given URI. If multiple
    * namespace prefixes have been declared, then the first one found is
    * returned. To obtain all prefixes see <code>#getNamespacePrefixes</code>.
    * 
    * @param nsURI
    *           the namespace URI to lookup
    * @return the namespace prefix associated with the given URI
    * 
    * @throws IllegalArgumentException
    *            if nsURI is null
    **/
   public String getNamespacePrefix(String nsURI) {

      // check the input parameter
      Assert.notNull(nsURI, "Namespace URI must not be null.");

      for (Namespace namespace : namespaces) {

         if (nsURI.equals(namespace.getUri())) {
            return namespace.getPrefix();
         }
      }

      // -- handle built-in namespace prefixes
      if (XML_NAMESPACE.equals(nsURI)) {
         return XML_NAMESPACE_PREFIX;
      }

      return null;

   }

   /**
    * Returns all namespace prefixes declared locally
    * 
    * @return an Enumeration of locally declared namespace prefixes
    */
   public Enumeration<String> getLocalNamespacePrefixes() {
      return new NamespaceEnumerator(namespaces.iterator(), NamespaceEnumerator.PREFIX);
   }

   /**
    * Returns the Namespace prefixes associated with the given URI.
    * 
    * @param nsURI
    *           the namespace URI to lookup
    * @param local
    *           a boolean that when true indicates only the local scope is
    *           searched.
    * @return the namespace prefixes associated with the given URI
    * 
    * @throws IllegalArgumentException
    *            if nsURI is null
    **/
   public String[] getNamespacePrefixes(String nsURI) {

      // check the result
      Assert.notNull(nsURI, "Namespace URI must not be null.");

      List<String> prefixes = new ArrayList<String>();
      for (Namespace namespace : namespaces) {
         if (namespace.getUri().equals(nsURI)) {
            prefixes.add(namespace.getPrefix());
         }
      }

      return prefixes.toArray(new String[0]);
   }

   /**
    * Returns the Namespace prefix associated with the given URI. Or null if no
    * prefix has been declared. This method will ignore the default namespace.
    * This is useful when dealing with attributes that do not use the default
    * namespace.
    * 
    * @param nsURI
    *           the namespace URI to lookup
    * @return the namespace prefix associated with the given URI
    * 
    * @throws IllegalArgumentException
    *            if nsURI is null
    **/
   public String getNonDefaultNamespacePrefix(String nsURI) {
      Assert.notNull(nsURI, "Namespace URI must not be null.");
      for (Namespace namespace : namespaces) {
         if (nsURI.equals(namespace.getUri()) && namespace.getPrefix().length() > 0) {
            return namespace.getPrefix();
         }
      }

      // -- handle built-in namespace prefixes
      if (XML_NAMESPACE.equals(nsURI)) {
         return XML_NAMESPACE_PREFIX;
      }

      return null;

   }

   /**
    * Removes the namespace declaration for the given prefix. This is a local
    * action only, the namespace declaration will not be removed from any parent
    * Namespaces object.
    * 
    * @param prefix
    *           the namespace prefix to remove the binding of
    * @return true if the namespace declaration was removed, otherwise false.
    */
   public synchronized boolean removeNamespace(String prefix) {
      if (prefix == null) {
         return false;
      }

      if (namespaceMap.containsKey(prefix)) {
         Namespace namespace = namespaceMap.get(prefix);
         namespaceMap.remove(prefix);
         namespaces.remove(namespace);

         return true;
      }

      return false;
   }

   /**
    * Calls the given ContentHandler's endPrefixMapping method for each locally
    * declared namespace
    * 
    * @param handler
    *           the ContentHandler
    */
   public void sendEndEvents(ContentHandler handler) throws SAXException {
      for (Namespace namespace : namespaces) {
         handler.endPrefixMapping(namespace.getPrefix());
      }
   }

   /**
    * Calls the given ContentHandler's startPrefixMapping method for each
    * locally declared namespace
    * 
    * @param handler
    *           the ContentHandler
    */
   public void sendStartEvents(ContentHandler handler) throws SAXException {
      for (Namespace namespace : namespaces) {
         handler.startPrefixMapping(namespace.getPrefix(), namespace.getUri());
      }
   }

   /**
    * Declare the namespaces of this stack in as attributes.
    * 
    * @param atts
    *           the Attribute List to fill in.
    */
   @SuppressWarnings("deprecation")
   public void declareAsAttributes(AttributeListImpl atts) {

      String attName = null;
      for (Namespace ns : namespaces) {
         if (ns.prefix != null) {
            int len = ns.prefix.length();
            if (len > 0) {
               StringBuffer buf = new StringBuffer(6 + len);
               buf.append(XMLNS);
               buf.append(':');
               buf.append(ns.prefix);
               attName = buf.toString();
               atts.addAttribute(attName, CDATA, ns.uri);
            }
            // case with no prefix but a nsURI
            else {
               atts.addAttribute(XMLNS, CDATA, ns.uri);
            }
         } // ns.prefix!=null
         else {
            atts.addAttribute(XMLNS, CDATA, ns.uri);
         }
      }
   }

   /**
    * An internal class used to represent an XML namespace.
    **/
   class Namespace {

      /**
       * The namespace uri.
       */
      private String uri;
      
      /**
       * The namespace prefix bound to the uri.
       */
      private String prefix;


      /**
       * Creates new {@link Namespace} instance, the namespace prefix and uri
       * remains uninitialized.
       */
      Namespace() {
         super();
      }

      Namespace(String prefix, String uri) {
         this.prefix = prefix;
         this.uri = uri;
      }

      /**
       * Retrieves the namespace prefix.
       * 
       * @return the namespace prefix
       */
      public String getPrefix() {
         return prefix;
      }

      /**
       * Sets the namespace prefix
       * 
       * @param prefix
       *           the namespace prefix
       */
      public void setPrefix(String prefix) {
         this.prefix = prefix;
      }

      /**
       * Retrieves the namespace uri.
       * 
       * @return the namespace uri
       */
      public String getUri() {
         return uri;
      }

      /**
       * Sets the namespace uri
       * 
       * @param uri
       *           the namespace uri
       */
      public void setUri(String uri) {
         this.uri = uri;
      }

      /**
       * {@inheritDoc}
       */
      @Override
      public boolean equals(Object object) {
         if (this == object) {
            return true;
         }
         if (object == null || getClass() != object.getClass()) {
            return false;
         }

         Namespace namespace = (Namespace) object;

         if (prefix != null ? !prefix.equals(namespace.prefix) : namespace.prefix != null)
            return false;
         if (uri != null ? !uri.equals(namespace.uri) : namespace.uri != null)
            return false;

         return true;
      }

      /**
       * {@inheritDoc}
       */
      @Override
      public int hashCode() {
         int result = prefix != null ? prefix.hashCode() : 0;
         result = 31 * result + (uri != null ? uri.hashCode() : 0);
         return result;
      }
   }

   /**
    * A simple Enumeration for Namespace objects
    */
   static class NamespaceEnumerator implements java.util.Enumeration<String> {
      public static final int URI = 0;
      public static final int PREFIX = 1;

      private int _returnType = URI;

      private Iterator<Namespace> namespaceIterator;

      NamespaceEnumerator(Iterator<Namespace> namespaceIterator) {
         this.namespaceIterator = namespaceIterator;
      }

      NamespaceEnumerator(Iterator<Namespace> namespaceIterator, int returnType) {
         this.namespaceIterator = namespaceIterator;
         _returnType = returnType;
      }

      public boolean hasMoreElements() {
         return namespaceIterator.hasNext();
      }

      public String nextElement() {

         String result;
         Namespace ns = namespaceIterator.next();

         if (_returnType == URI) {
            result = ns.getUri();
         } else {
            result = ns.getPrefix();
         }

         return result;
      }

   }

}