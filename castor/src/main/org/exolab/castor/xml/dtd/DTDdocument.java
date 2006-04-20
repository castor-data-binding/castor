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

import java.util.Hashtable;
import java.util.Enumeration;

/**
 * Implementation of DTD document specification.
 * @author <a href="mailto:totok@intalio.com">Alexander Totok</a>
 * @version $Revision$ $Date$
 */
public class DTDdocument {

   /**
    * Name of the document.
    */
   private String name = null;

   /**
    * Element declarations in the document.
    */
   private Hashtable elements;

   /**
    * Notation declarations in the document.
    */
   private Hashtable notations;

   /**
    * General Entity declarations in the document.
    */
   private Hashtable generalEntities;

   public DTDdocument() {
      elements = new Hashtable();
      notations = new Hashtable();
      generalEntities = new Hashtable();
   } //-- DTDdocument

   /**
    * Constructor, setting the name of the document.
    */
   public DTDdocument(String name) {
      this();
      this.name = name;
   } //-- DTDdocument

   /**
    * Returns the name of the document.
    */
   public String getName() {
      return name;
   } //-- getName;

   /**
    * Adds Element Declaration to the document.
    * @throws DTDException if an element has no name or there already exists
    * element with the same name in the document.
    */
   public synchronized void addElement(Element element) throws DTDException {

      String name = element.getName();

      if (name == null) {
         String err = "An element declaration must contain a name.";
         throw new DTDException(err);
      }
      if (elements.containsKey(name)) {
         String err = "An element declaration already exists with the given name: ";
         throw new DTDException(err + name);
      }

      elements.put(name,element);

   } //-- addElement

   /**
    * Returns requested Element Declaration.
    * @return Element Declaration with the given <tt>name</tt>,
    * <tt>null</tt> if there is no Element with this name in the document.
    */
   public Element getElement(String name) {
      return (Element)elements.get(name);
   } //-- getElement

   /**
    * Adds a Notation Declaration to the document.
    * @throws DTDException if a notation has no name or there already exists
    * notation with the same name in the document.
    */
   public synchronized void addNotation(Notation notation)
                                     throws DTDException {

      String name = notation.getName();

      if (name == null) {
         String err = "A notation declaration must contain a name.";
         throw new DTDException(err);
      }
      if (notations.containsKey(name)) {
         String err = "A notation declaration already exists with the given name: ";
         throw new DTDException(err + name);
      }

      notations.put(name,notation);

   } //-- addNotation

   /**
    * Returns requested Notation Declaration.
    * @return Notation Declaration with the given <tt>name</tt>,
    * <tt>null</tt> if there is no Notation with this name in the document.
    */
   public Notation getNotation(String name) {
      return (Notation)notations.get(name);
   } //-- getNotation

   /**
    * Adds General Entity Declaration to the document. If there already exists
    * General Entity with the same name in the document, does nothing.
    */
   public synchronized void addGeneralEntity(GeneralEntity generalEntity) {

      String name = generalEntity.getName();

      if (!generalEntities.containsKey(name))
         generalEntities.put(name,generalEntity);

   } //-- addGeneralEntity

   /**
    * Returns requested Genaral Entity Declaration.
    * @return General Entity Declaration with the given <tt>name</tt>,
    * <tt>null</tt> if there is no General Entity with this name in the document.
    */
   public GeneralEntity getGeneralEntity(String name) {
      return (GeneralEntity)generalEntities.get(name);
   } //-- getGeneralEntities

   /**
    * Returns enumeration of the Element declarations in the DTD document.
    */
   public Enumeration getElements() {
      return elements.elements();
   } //-- getElements

   /**
    * Returns enumeration of the General Entity declarations in the DTD document.
    */
   public Enumeration getGeneralEntities() {
      return generalEntities.elements();
   } //-- getGeneralEntities

   /**
    * Returns enumeration of the Notation declarations in the DTD document.
    */
   public Enumeration getNotations() {
      return notations.elements();
   } //-- getNotations

} //-- DTDdocument
