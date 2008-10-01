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
 * Copyright 2000 (C) Intalio Inc. All Rights Reserved.
 *
 * $Id$
 */

package org.exolab.castor.xml.dtd;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Enumeration;

/**
 * Implementation of DTD Element declaration specification.
 * @author <a href="mailto:totok@intalio.com">Alexander Totok</a>
 * @version $Revision$ $Date: 2006-04-25 15:08:23 -0600 (Tue, 25 Apr 2006) $
 */
public class Element {

   private static final short ANY = 0;
   private static final short EMPTY = 1;
   private static final short MIXED = 2;
   private static final short ELEMENTS_ONLY = 3;

   /**
    * Name of the element.
    */
   private String name;

   /**
    * DTD document owning this element
    */
   private DTDdocument document;

   /**
    * Content type of the element. Value may be {@link #ANY ANY}, {@link #EMPTY EMPTY},
    * {@link #MIXED MIXED}, {@link #ELEMENTS_ONLY ELEMENTS_ONLY} or
    * -1, if unspecified.
    */
   private short contentType = -1;

   /**
    * Set of names of children of the element,
    * if the element has <tt>MIXED</tt> content.
    */
   private HashSet mixedChildren = null;

   /**
    * Content Particle representing content of the element, if the element has
    * <tt>ELEMENTS_ONLY</tt> content.
    */
   private ContentParticle content = null;

   /**
    * Attributes of the element.
    */
   private Hashtable attributes;

   /**
    * Constructor, setting the name of the element and owning DTD document.
    * @param document must not be null.
    */
   public Element(DTDdocument document, String name) {

      if (document == null) {
         String err = "Element constructor: document must not be null.";
         throw new IllegalArgumentException(err);
      }

      this.name = name;
      this.document = document;
      attributes = new Hashtable();
   } //-- Element

   /**
    * Constructor, setting owning DTD document of the element.
    * @param document must not be null.
    */
   public Element(DTDdocument document) {
      this(document, null);
   } //-- Element

   /**
    * Returns the name of the element.
    */
   public String getName() {
      return name;
   } //-- getName

   /**
    * Returns DTD document owning this element.
    */
   public DTDdocument getDocument() {
      return document;
   } //-- getDocument

   /**
    * Returns {@link java.util.Iterator iterator} of the set of mixed children,
    * if of <tt>MIXED</tt> content, <tt>null</tt> otherwise.
    */
   public Iterator getMixedContentChildren() {
      if (isMixedContent()) return mixedChildren.iterator();
      return null;
   } //-- getMixedContentChildren

   /**
    * Returns enumeration of the attributes of the element.
    */
   public Enumeration getAttributes() {
      return attributes.elements();
   } //-- getAttributes

   /**
    * Returns {@link org.exolab.castor.xml.dtd.ContentParticle Content Particle},
    * representing the content of the element, if has <tt>ELEMENTS_ONLY</tt>
    * content, <tt>null</tt> otherwise.
    */
   public ContentParticle getContent() {
      if (isElemOnlyContent()) return content;
      return null;
   } //-- getContent

   /**
    * Sets the name of the element.
    */
   public void setName(String name) {
      this.name = name;
   } //-- setName

   /**
    * Sets the content type of the element to <tt>ANY</tt>.
    */
   public void setAnyContent() {
      contentType = ANY;
   } //-- setAnyContent

   /**
    * <b>True</b> if the element is of <tt>ANY</tt> content type,
    * <b>false</b> otherwise.
    */
   public boolean isAnyContent() {
      return contentType == ANY;
   } //-- isAnyContent

   /**
    * Sets the content type of the element to <tt>EMPTY</tt>.
    */
   public void setEmptyContent() {
      contentType = EMPTY;
   } //-- setEmptyContent

   /**
    * <b>True</b> if the element is of <tt>EMPTY</tt> content type, <b>false</b> otherwise.
    */
   public boolean isEmptyContent() {
      return contentType == EMPTY;
   } //-- isEmptyContent

   /**
    * Sets the content type of the element to <tt>MIXED</tt>.
    */
   public void setMixedContent() {
      contentType = MIXED;
      mixedChildren = new HashSet();
   } //-- setMixedContent

   /**
    * <b>True</b> if the element is of <tt>MIXED</tt> content type, <b>false</b> otherwise.
    */
   public boolean isMixedContent() {
      return contentType == MIXED;
   } //-- isMixedContent

   /**
    * Sets the content type of the element to <tt>ELEMENTS_ONLY</tt>.
    * @param cp Content Particle representing content of the element.
    */
   public void setElemOnlyContent(ContentParticle cp) {
      contentType = ELEMENTS_ONLY;
      content = cp;
   } //-- setChildrenContent

   /**
    * <b>True</b> if the element is of <tt>ELEMENTS_ONLY</tt> content type,
    * <b>false</b> otherwise.
    */
   public boolean isElemOnlyContent() {
      return contentType == ELEMENTS_ONLY;
   } //-- isChildrenContent

   /**
    * Adds name of a <tt>child</tt> to the set of children's names.
    * @throws DTDException if there already exists the child with the same name.
    */
   public synchronized void addMixedContentChild(String child) throws DTDException {
      if (mixedChildren.contains(child)) {
         String err = "Element \"" + name + "\" already contains child element ";
         err += "\"" + child + "\".";
         throw new DTDException(err);
      }
      mixedChildren.add(child);
   } //-- addChild

   /**
    * Adds attribute to the element. If the element already has the attribute
    * with the same name, does nothing.
    */
   public synchronized void addAttribute(Attribute attribute) {
      String name = attribute.getName();
      if (!attributes.containsKey(name)) attributes.put(name,attribute);
   } //-- addAttribute

} //-- Element
