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
import java.util.Iterator;

/**
 * Implementation of DTD Attribute declaration specification.
 * @author <a href="mailto:totok@intalio.com">Alexander Totok</a>
 * @version $Revision$ $Date: 2006-04-25 15:08:23 -0600 (Tue, 25 Apr 2006) $
 */
public class Attribute {

   private static final short CDATA = 0;
   private static final short ID = 1;
   private static final short IDREF = 2;
   private static final short IDREFS = 3;
   private static final short ENTITY = 4;
   private static final short ENTITIES = 5;
   private static final short NMTOKEN = 6;
   private static final short NMTOKENS = 7;
   private static final short NOTATION = 8;
   private static final short Enumeration = 9;

   private static final short DEFAULT = 10;
   private static final short REQUIRED = 11;
   private static final short IMPLIED = 12;
   private static final short FIXED = 13;

   /**
    * Name of the attribute.
    */
   private String name;

   /**
    * Element owning this attribute.
    */
   private Element element;

   /**
    * Type of the attribute. Value may be
    * {@link #CDATA CDATA}, {@link #ID ID}, {@link #IDREF IDREF},
    * {@link #IDREFS IDREFS}, {@link #ENTITY ENTITY}, {@link #ENTITIES ENTITIES},
    * {@link #NMTOKEN NMTOKEN}, {@link #NMTOKENS NMTOKENS},
    * {@link #NOTATION NOTATION}, {@link #Enumeration Enumeration} or -1,
    * if unspecified.
    */
   private short type = -1;

   /**
    * Specifies occurance of the attribute. Value may be {@link #DEFAULT DEFAULT}
    * - default attribute value is specified, presence of the attribute
    * is not required, {@link #REQUIRED REQUIRED} - the presence ot
    * the attribute is required and no default value is specified,
    * {@link #IMPLIED IMPLIED} - default value is not specified and presence
    * of the attribute is not required, or {@link #FIXED FIXED} -
    * the attribute must have fixed value, which is specified, however
    * attribute may be present, but its value must match the default value.
    */
   private short occuranceType;

   /**
    * Default value of the attribute.
    */
   private String defaultValue = null;

   /**
    * Possible values of the attribute (if the attribute is of <tt>NOTATION</tt>
    * or <tt>Enumeration</tt> type).
    */
   private HashSet values;

   /**
    * Constructor, setting name, owning element of the attribute and
    * occurance specification to <tt>DEFAULT</tt>.
    * @param element must not be <tt>null</tt>.
    * @param name must not be <tt>null</tt> or equal to empty String.
    */
   public Attribute(Element element, String name) {

      if (name == null || name.equals("")) {
         String err = "Attribute constructor: name must not be empty.";
         throw new IllegalArgumentException(err);
      }

      if (element == null) {
         String err = "Attribute constructor: element must not be null.";
         throw new IllegalArgumentException(err);
      }

      this.name = name;
      this.element = element;
      values = new HashSet();
      occuranceType = DEFAULT;
   } //-- Attribute

   /**
    * Returns the name of the attribute.
    */
   public String getName() {
      return name;
   } //-- getName

   /**
    * Returns Element owning this attribute.
    */
   public Element getElement() {
      return element;
   } //-- getElement

   /**
    * Returns {@link java.util.Iterator iterator} of the set of possible values,
    * if of <tt>NOTATION</tt> or <tt>Enumeration</tt> type, <tt>null</tt> otherwise.
    */
   public Iterator getValues() {
      if (isNOTATIONType() || isEnumerationType()) return values.iterator();
      return null;
   } //-- getValues

   /**
    * Sets the type of the attribute to <tt>CDATA</tt>.
    */
   public void setStringType() {
      type = CDATA;
   } //-- setStringType

   /**
    * <b>True</b> if the attribute is of <tt>CDATA</tt> type,
    * <b>false</b> otherwise.
    */
   public boolean isStringType() {
      return type == CDATA;
   } //-- isStringType

   /**
    * Sets the type of the attribute to <tt>ID</tt>.
    */
   public void setIDType() {
      type = ID;
   } //-- setIDType

   /**
    * <b>True</b> if the attribute is of <tt>ID</tt> type, <b>false</b> otherwise.
    */
   public boolean isIDType() {
      return type == ID;
   } //-- isIDType

   /**
    * Sets the type of the attribute to <tt>IDREF</tt>.
    */
   public void setIDREFType() {
      type = IDREF;
   } //-- setIDREFType

   /**
    * <b>True</b> if the attribute is of <tt>IDREF</tt> type,
    * <b>false</b> otherwise.
    */
   public boolean isIDREFType() {
      return type == IDREF;
   } //-- isIDREFType

   /**
    * Sets the type of the attribute to <tt>IDREFS</tt>.
    */
   public void setIDREFSType() {
      type = IDREFS;
   } //-- setIDREFSType

   /**
    * <b>True</b> if the attribute is of <tt>IDREFS</tt> type,
    * <b>false</b> otherwise.
    */
   public boolean isIDREFSType() {
      return type == IDREFS;
   } //-- isIDREFSType

   /**
    * Sets the type of the attribute to <tt>ENTITY</tt>.
    */
   public void setENTITYType() {
      type = ENTITY;
   } //-- setENTITYType

   /**
    * <b>True</b> if the attribute is of <tt>ENTITY</tt> type,
    * <b>false</b> otherwise.
    */
   public boolean isENTITYType() {
      return type == ENTITY;
   } //-- isENTITYType

   /**
    * Sets the type of the attribute to <tt>ENTITIES</tt>.
    */
   public void setENTITIESType() {
      type = ENTITIES;
   } //-- setENTITIESType

   /**
    * <b>True</b> if the attribute is of <tt>ENTITIES</tt> type,
    * <b>false</b> otherwise.
    */
   public boolean isENTITIESType() {
      return type == ENTITIES;
   } //-- isENTITIESType

   /**
    * Sets the type of the attribute to <tt>NMTOKEN</tt>.
    */
   public void setNMTOKENType() {
      type = NMTOKEN;
   } //-- setNMTOKENType

   /**
    * <b>True</b> if the attribute is of <tt>NMTOKEN</tt> type,
    * <b>false</b> otherwise.
    */
   public boolean isNMTOKENType() {
      return type == NMTOKEN;
   } //-- isNMTOKENType

   /**
    * Sets the type of the attribute to <tt>NMTOKENS</tt>.
    */
   public void setNMTOKENSType() {
      type = NMTOKENS;
   } //-- setNMTOKENSType

   /**
    * <b>True</b> if the attribute is of <tt>NMTOKENS</tt> type,
    * <b>false</b> otherwise.
    */
   public boolean isNMTOKENSType() {
      return type == NMTOKENS;
   } //-- isNMTOKENSType

   /**
    * Sets the type of the attribute to <tt>NOTATION</tt>.
    */
   public void setNOTATIONType() {
      type = NOTATION;
   } //-- setNOTATIONType

   /**
    * <b>True</b> if the attribute is of <tt>NOTATION</tt> type,
    * <b>false</b> otherwise.
    */
   public boolean isNOTATIONType() {
      return type == NOTATION;
   } //-- isNOTATIONType

   /**
    * Sets the type of the attribute to <tt>Enumeration</tt>.
    */
   public void setEnumerationType() {
      type = Enumeration;
   } //-- setEnumerationType

   /**
    * <b>True</b> if the attribute is of <tt>Enumeration</tt> type, <b>false</b> otherwise.
    */
   public boolean isEnumerationType() {
      return type == Enumeration;
   } //-- isEnumerationType

   /**
    * Sets occurance specification to <tt>DEFAULT</tt>.
    */
   public void setDEFAULT() {
      occuranceType = DEFAULT;
   } //-- setDEFAULT

   /**
    * <b>True</b> if attribute's default value is specified, <b>false</b> otherwise.
    */
   public boolean isDEFAULT() {
      return occuranceType == DEFAULT;
   } //-- isDEFAULT

   /**
    * Sets occurance specification to <tt>REQUIRED</tt>.
    */
   public void setREQUIRED() {
      occuranceType = REQUIRED;
   } //-- setREQUIRED

   /**
    * <b>True</b> if the attribute is required, <b>false</b> otherwise.
    */
   public boolean isREQUIRED() {
      return occuranceType == REQUIRED;
   } //-- isREQUIRED

   /**
    * Sets occurance specification to <tt>IMPLIED</tt>.
    */
   public void setIMPLIED() {
      occuranceType = IMPLIED;
   } //-- setIMOLIED

   /**
    * <b>True</b> if no default value for the attribute is provided
    * ("IMPLIED" specification), <b>false</b> otherwise.
    */
   public boolean isIMPLIED() {
      return occuranceType == IMPLIED;
   } //-- isIMPLIED

   /**
    * Sets occurance specification to <tt>FIXED</tt>.
    */
   public void setFIXED() {
      occuranceType = FIXED;
   } //-- setFIXED

   /**
    * <b>True</b> if the attribute has fixed value, <b>false</b> otherwise.
    */
   public boolean isFIXED() {
      return occuranceType == FIXED;
   } //-- isFIXED

   /**
    * Sets default value.
    */
   public void setDefaultValue(String value) {
      defaultValue = value;
   } //-- setDefaultValue

   /**
    * Returns default value.
    */
   public String getDefaultValue() {
      return defaultValue;
   } //-- getDefaultValue

   /**
    * Adds the <tt>value</tt> to the set of possible values.
    * @throws DTDException if the <tt>value</tt> is already contained
    * in the set of possible values.
    */
   public synchronized void addValue(String value) throws DTDException {
      if (values.contains(value)) {
         String err = "The value \"" + value + "\" is already contained in the set";
         err += " of possible values of \"" + name + "\" attribute.";
         throw new DTDException(err);
      }
      values.add(value);
   } //-- addvalue

} //-- Attribute
