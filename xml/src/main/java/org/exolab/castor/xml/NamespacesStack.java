/*
 * Copyright 2011 Jakub Narloch
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
package org.exolab.castor.xml;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.helpers.AttributeListImpl;

/**
 * This class is an actual namespace stack implementation, responsible for
 * adding and removing namespace scopes, as well as resolving namespace urls and
 * prefixes by traversing all the namespace stack.
 * 
 * @author <a href="mailto:jmnarloch AT gmail DOT com">Jakub Narloch</a>
 * @since 1.3.3
 */
@SuppressWarnings("deprecation")
public class NamespacesStack {

   private final Log logger = LogFactory.getLog(this.getClass());
   
   /**
    * Represents the string used for indicating default namespace, by default
    * empty string is used.
    */
   private static final String DEFAULT_NAMESPACE = "";

   /**
    * Represents a stack of namespaces.
    */
   private List<Namespaces> namespaceStack = new ArrayList<Namespaces>();

   /**
    * Adds a namespace to current scope.
    * 
    * @param namespacePrefix
    *           the namespace prefix
    * @param namespaceUri
    *           the namespace uri
    */
   public void addNamespace(String namespacePrefix, String namespaceUri) {
      Namespaces namespaces = getCurrentNamespaceScope();
      if (namespaces != null) {
         namespaces.addNamespace(namespacePrefix, namespaceUri);
      }
   }

   /**
    * Adds the default namespace to current scope.
    * 
    * @param namespaceUri
    *           the namespace uri
    */
   public void addDefaultNamespace(String namespaceUri) {
      addNamespace(DEFAULT_NAMESPACE, namespaceUri);
   }

   /**
    * Removes the namespace from current scope using the namespace prefix
    * 
    * @param namespacePrefix
    *           the prefix of the namespace to remove
    */
   public void removeNamespace(String namespacePrefix) {
      getCurrentNamespaceScope().removeNamespace(namespacePrefix);
   }

   /**
    * Retrieves the namespace uri bound to specified prefix.
    * 
    * @param namespacePrefix
    *           the namespace prefix
    * 
    * @return the namespace uri bound to the prefix, or null if no namespace has
    *         been bound for the given prefix
    */
   public String getNamespaceURI(String namespacePrefix) {
      String namespaceUri = null;
      for (int ind = namespaceStack.size() - 1; ind >= 0 && namespaceUri == null; ind--) {
         namespaceUri = namespaceStack.get(ind).getNamespaceURI(namespacePrefix);
      }
      return namespaceUri;
   }

   /**
    * Declares the namespaces using the attribute list.
    * 
    * @param attributeList
    *           the list of attributes containing the namespaces
    * @param localOnly
    *           whether the namespaces will only registered in current namespace
    *           context or in entire stack
    */
   @SuppressWarnings("deprecation")
   public void declareAsAttributes(AttributeListImpl attributeList, boolean localOnly) {
      getCurrentNamespaceScope().declareAsAttributes(attributeList);
      if (!localOnly) {
         for (int ind = namespaceStack.size() - 1; ind >= 0; ind--) {

            namespaceStack.get(ind).declareAsAttributes(attributeList);
         }
      }
   }

   /**
    * Retrieves the default namespace namespace uri.
    * 
    * @return the default namespace uri
    */
   public String getDefaultNamespaceURI() {
      return getNamespaceURI(DEFAULT_NAMESPACE);
   }

   /**
    * Retrieves the namespace prefix for the given namespace uri.
    * 
    * @param namespaceUri
    *           the namespace uri
    * 
    * @return the
    */
   public String getNamespacePrefix(String namespaceUri) {
      String namespacePrefix = null;
      for (int ind = namespaceStack.size() - 1; ind >= 0 && namespacePrefix == null; ind--) {
         namespacePrefix = namespaceStack.get(ind).getNamespacePrefix(namespaceUri);
      }
      return namespacePrefix;
   }

   /**
    * Retrieves the non default namespace prefix for the given namespace uri.
    * 
    * @param namespaceUri
    *           the namespace uri
    * 
    * @return the
    */
   public String getNonDefaultNamespacePrefix(String namespaceUri) {
      String namespacePrefix = null;
      for (int ind = namespaceStack.size() - 1; ind >= 0 && namespacePrefix == null; ind--) {
         namespacePrefix = namespaceStack.get(ind).getNonDefaultNamespacePrefix(namespaceUri);
      }
      return namespacePrefix;
   }

   /**
    * Retrieves the namespace prefixes registered in current scope.
    * 
    * @return the enumeration of namespace prefixes
    */
   public Enumeration<String> getLocalNamespacePrefixes() {
      Namespaces namespaces = getCurrentNamespaceScope();
      return namespaces != null ? namespaces.getLocalNamespacePrefixes() : null;
   }

   /**
    * Adds a new namespace scope.
    */
   public void addNewNamespaceScope() {
      namespaceStack.add(new Namespaces());
   }

   /**
    * Removes the namespace scope.
    */
   public void removeNamespaceScope() {
      // removes the current namespace
      if (namespaceStack.size() > 0) {
         namespaceStack.remove(namespaceStack.size() - 1);
      } else {
         this.logger.error("Trying to remove a namespaces scope from an empty stack of Namespaces");
      }
   }

   /**
    * Retrieves the current namespace scope.
    * 
    * @return the current namespace scope.
    */
   public Namespaces getCurrentNamespaceScope() {
      if (namespaceStack.size() == 0) {
         addNewNamespaceScope();
      }
      return namespaceStack.get(namespaceStack.size() - 1);
   }
}