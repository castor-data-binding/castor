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

/**
 * Implementation of DTD General Entity declaration specification.
 * @author <a href="mailto:totok@intalio.com">Alexander Totok</a>
 * @version $Revision$ $Date$
 */
public class GeneralEntity {

   private static final short INTERNAL = 0;
   private static final short EXTERNAL_PUBLIC = 1;
   private static final short EXTERNAL_SYSTEM = 2;

   /**
    * Name of the general entity.
    */
   private String name = null;

   /**
    * DTD document owning this General Entity declaration.
    */
   private DTDdocument document = null;

   /**
    * Value of the general entity - <b>replacement text</b>.
    */
   private String value = null;

   /**
    * Type of the general entity. Value may be {@link #INTERNAL INTERNAL} -
    * the entity is internal and the {@link #value value} is specified,
    * {@link #EXTERNAL_PUBLIC EXTERNAL_PUBLIC} - the
    * {@link #pubIdentifier pubIdentifier} and {@link #sysIdentifier sysIdentifier}
    * are specified, {@link #EXTERNAL_SYSTEM EXTERNAL_SYSTEM} - the
    * {@link #sysIdentifier sysIdentifier} only is specified, or -1
    * if unspecified.
    */
   private short type = - 1;

   /**
    * <b>Public identifier</b> of the general entity.
    */
   private String pubIdentifier = null;

   /**
    * <b>System identifier</b> of the general entity.
    */
   private String sysIdentifier = null;

   /**
    * Name of associated <b>NOTATION</b>.
    */
   private String ndata = null;

   /**
    * Default constructor.
    */
   public GeneralEntity() {
   }

   /**
    * Constructor, setting name and owning DTD document of the general entity.
    * @param document must not be <tt>null</tt>.
    * @param name must not be <tt>null</tt> or equal to empty String.
    */
   public GeneralEntity(DTDdocument document, String name) {

      if (document == null) {
         String err = "GeneralEntity constructor: document must not be null.";
         throw new IllegalArgumentException(err);
      }

      if (name == null || name.equals("")) {
         String err = "GeneralEntity constructor: name must not be empty.";
         throw new IllegalArgumentException(err);
      }

      this.name = name;
      this.document = document;
   } //-- GeneralEntity

   /**
    * Returns the name of the general entity.
    */
   public String getName() {
      return name;
   } //-- getName


   /**
    * Return DTD document owning this General Entity declaration.
    */
   public DTDdocument getDocument() {
      return document;
   } //-- getDocument

   /**
    * Sets the value (replacement text) of the general entity, making it
    * internal parsed entity.
    * @param value must not be <tt>null</tt>.
    */
   public void setValue(String value) {
      if (value == null) {
         String err = "GeneralEntity: can not set null value.";
         throw new IllegalArgumentException(err);
      }
      type = INTERNAL;
      this.value = value;
   } //-- setValue

   /**
    * <b>True</b> if internal entity, <b>false</b> otherwise.
    */
   public boolean isInternal() {
      return type == INTERNAL;
   } //-- isInternal

   /**
    * Returns the value of the entity (replacement text) if internal entity,
    * <tt>null</tt> otherwise.
    */
   public String getValue() {
      if (isInternal()) return value;
      return null;
   } //-- getValue

   /**
    * Sets the general entity to <tt>EXTERNAL_PUBLIC</tt>.
    * @param pubId public identifier - must not be <tt>null</tt>.
    * @param sysId system identifier - must not be <tt>null</tt>.
    */
   public void setExternalPublic(String pubId, String sysId) {
      if (pubId == null) {
         String err = "GeneralEntity: can not set null public ID.";
         throw new IllegalArgumentException(err);
      }

      if (sysId == null) {
         String err = "GeneralEntity: can not set null system ID.";
         throw new IllegalArgumentException(err);
      }

      type = EXTERNAL_PUBLIC;
      pubIdentifier = pubId;
      sysIdentifier = sysId;
   } //-- setExternalPublic

   /**
    * <b>True</b> if <tt>EXTERNAL_PUBLIC</tt> entity, <b>false</b> otherwise.
    */
   public boolean isExternalPublic() {
      return type == EXTERNAL_PUBLIC;
   } //-- isExternalPublic

   /**
    * Sets the general entity to <tt>EXTERNAL_SYSTEM</tt>.
    * @param sysId system identifier - must not be <tt>null</tt>.
    */
   public void setExternalSystem(String sysId) {
      if (sysId == null) {
         String err = "GeneralEntity: can not set null system ID.";
         throw new IllegalArgumentException(err);
      }

      type = EXTERNAL_SYSTEM;
      sysIdentifier = sysId;
   } //-- setExternalSystem

   /**
    * <b>True</b> if <tt>EXTERNAL_SYSTEM</tt> entity, <b>false</b> otherwise.
    */
   public boolean isExternalSystem() {
      return type == EXTERNAL_SYSTEM;
   } //-- isExternalSystem

   /**
    * Returns system identifier, if <tt>EXTERNAL_PUBLIC</tt> or
    * <tt>EXTERNAL_SYSTEM</tt> entity, <tt>null</tt> otherwise.
    */
   public String getSysIdentifier() {
      if (isExternalSystem() || isExternalPublic()) return sysIdentifier;
      return null;
   } //-- getSysIdentifier

   /**
    * Returns public identifier, if <tt>EXTERNAL_PUBLIC</tt> entity,
    * <tt>null</tt> otherwise.
    */
   public String getPubIdentifier() {
      if (isExternalPublic()) return pubIdentifier;
      return null;
   } //-- getPubIdentifier

   /**
    * Sets the associated notation.
    * @param notationName - must not be <tt>null</tt> or equal to empty String.
    */
   public void setNDATA(String notationName) {
      if (notationName == null || notationName.equals("")) {
         String err = "General Entity: can not set empty associated notation name.";
         throw new IllegalArgumentException(err);
      }
      ndata = notationName;
   } //-- setNDATA

   /**
    * <b>True</b> if external unparsed entity, that is if external and
    * associated notation name is specified, <b>false</b> otherwise.
    */
   public boolean isExternalUnparsed() {
      if ((isExternalPublic() || isExternalSystem()) && ndata != null) return true;
      return false;
   } //-- isUnparsed

   /**
    * Returns name of associated notation, if external entity,
    * <tt>null</tt> otherwise.
    */
   public String getNotation() {
      if (isExternalPublic() || isExternalSystem()) return ndata;
      return null;
   } //-- getNotation

} //-- GeneralEntity
