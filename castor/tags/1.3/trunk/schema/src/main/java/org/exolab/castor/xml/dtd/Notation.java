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
 * Implementation of DTD Notation declaration specification.
 * @author <a href="mailto:totok@intalio.com">Alexander Totok</a>
 * @version $Revision$ $Date: 2003-03-03 00:05:44 -0700 (Mon, 03 Mar 2003) $
 */
public class Notation {

   private static final short PUBLIC = 0;
   private static final short SYSTEM = 0;

   /**
    * Name of the notation.
    */
   private String name;

   /**
    * DTD document owning this Notation.
    */
   private DTDdocument document;

   /**
    * Type of the notation. Value may be the {@link #PUBLIC PUBLIC} -
    * {@link #pubIdentifier pubIdentifier} and {@link #sysIdentifier sysIdentifier}
    * are specified, {@link #SYSTEM SYSTEM} - the
    * {@link #sysIdentifier sysIdentifier} only is specified, or -1
    * if unspecified.
    */
   private short type = -1;

   /**
    * <b>Public identifier</b> of the notation.
    */
   private String pubIdentifier = null;

   /**
    * <b>System identifier</b> of the notation.
    */
   private String sysIdentifier = null;

   /**
    * Constructor, setting name and owning DTD document of the notation.
    * @param document must not be null.
    * @param name must not be null or equal to empty String.
    */
   public Notation(DTDdocument document, String name) {
      if (document == null) {
         String err = "Notation constructor: document must not be null";
         throw new IllegalArgumentException(err);
      }

      if (name == null || name.equals("")) {
         String err = "Notation constructor: name must not be empty.";
         throw new IllegalArgumentException(err);
      }

      this.name = name;
      this.document = document;
   } //-- Notation

   /**
    * Returns the name of the notation.
    */
   public String getName() {
      return name;
   } //-- getName

   /**
    * Returns DTD document owning this notation.
    */
   public DTDdocument getDocument() {
      return document;
   } //-- getDocument

   /**
    * Sets the notation to <tt>PUBLIC</tt>.
    * @param pubId public identifier - must not be <tt>null</tt>.
    * @param sysId system identifier - must not be <tt>null</tt>.
    */
   public void setPublic(String pubId, String sysId) {
      if (pubId == null) {
         String err = "Notation: can not set null public ID.";
         throw new IllegalArgumentException(err);
      }

      if (sysId == null) {
         String err = "Notation: can not set null system ID.";
         throw new IllegalArgumentException(err);
      }

      type = PUBLIC;
      pubIdentifier = pubId;
      sysIdentifier = sysId;
   } //-- setPublic

   /**
    * <b>True</b> if <tt>PUBLIC</tt> notation, <b>false</b> otherwise.
    */
   public boolean isPublic() {
      return type == PUBLIC;
   } //-- isPublic()

   /**
    * Sets the notation to <tt>SYSTEM</tt>.
    * @param sysId system identifier - must not be <tt>null</tt>.
    */
   public void setSystem(String sysId) {
      if (sysId == null) {
         String err = "Notation: can not set null system ID.";
         throw new IllegalArgumentException(err);
      }

      type = SYSTEM;
      sysIdentifier = sysId;
   } //-- setSystem

   /**
    * <b>True</b> if <tt>SYSTEM</tt> notation, <b>false</b> otherwise.
    */
   public boolean isSystem() {
      return type == SYSTEM;
   } //-- isSystem

   /**
    * Returns public identifier.
    */
   public String getPubIdentifier() {
      return pubIdentifier;
   } //-- getPubIdentifier

   /**
    * Returns system identifier.
    */
   public String getSysIdentifier() {
      return sysIdentifier;
   } //-- getSysIdentifier

} //-- Notation
