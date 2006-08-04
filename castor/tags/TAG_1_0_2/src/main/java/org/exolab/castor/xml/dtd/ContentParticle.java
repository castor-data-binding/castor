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

import java.util.Vector;
import java.util.Enumeration;

/**
 * Implementation of DTD Content Particle specification, used to define the content
 * of an element.
 * @author <a href="mailto:totok@intalio.com">Alexander Totok</a>
 * @version $Revision$ $Date: 2003-03-03 00:05:44 -0700 (Mon, 03 Mar 2003) $
 */
public class ContentParticle {

   private static final short REFERENCE = 0;
   private static final short SEQ = 1;
   private static final short CHOICE = 2;

   private static final short ONE = 3;
   private static final short ZERO_OR_ONE = 4;
   private static final short ZERO_OR_MORE = 5;
   private static final short ONE_OR_MORE = 6;

   /**
    * Type of the content particle. Value may be {@link #REFERENCE REFERENCE}
    * - content is exactly one child-element, {@link #SEQ SEQ} - content
    * is a choice list of content particles, {@link #CHOICE CHOICE}
    * - content is a sequence list of content particles, or -1 if unspecified.
    */
   private short type = -1;

   /**
    * Specifies occurance of the content particle. Value may be
    * {@link #ONE ONE} - one occurance, {@link #ZERO_OR_ONE ZERO_OR_ONE}
    * - zero or one occurance, {@link #ONE_OR_MORE ONE_OR_MORE} - one or more
    * occurances, {@link #ZERO_OR_MORE ZERO_OR_MORE} - zero or more
    * occurances.
    */
   private short occuranceSpec;

   /**
    * Name of the child, if the content is exactly one child-element.
    */
   private String reference = null;

   /**
    * Content particles - children of this content particle, if the type is
    * a sequence list or choice list of content particles.
    */
   private Vector children = null;

   /**
    * Constructor, setting occurance specification to <tt>ONE</tt>, by default.
    */
   public ContentParticle() {
      occuranceSpec = ONE;
      children = new Vector();
   } //-- ContentParticle

   /**
    * Creates content particle and sets its type to <tt>REFERENCE</tt>, that is
    * the content is exactly one child-element.
    * @param reference name of this child-element.
    */
   public ContentParticle(String reference) {
      this();
      setReferenceType(reference);
   } //-- ContentParticle

   /**
    * Makes the content particle represent the content with exactly one
    * child-element.
    * @param reference the name of this child-element - must not be
    * <tt>null</tt> or equal to empty String.
    */
   public void setReferenceType(String reference) {

      if (reference == null || reference.equals("")) {
         String err = "ContentParticle: name of the reference child must not be empty.";
         throw new IllegalArgumentException(err);
      }

      type = REFERENCE;
      this.reference = reference;
   } //-- setReferenceType

   /**
    * <b>True</b> if the content is exactly on child-element,
    * <b>false</b> otherwise.
    */
   public boolean isReferenceType() {
      return type == REFERENCE;
   } //-- isReferenceType

   /**
    * Sets the content to sequence list of content particles.
    */
   public void setSeqType() {
      type = SEQ;
   } //-- setSeqType

   /**
    * <b>True</b> if the content is a sequence list of content particles,
    * <b>false</b> otherwise.
    */
   public boolean isSeqType() {
      return type == SEQ;
   } //-- isSeqType

   /**
    * Sets the content to choice list of content particles.
    */
   public void setChoiceType() {
      type = CHOICE;
   } //-- setChoiceType

   /**
    * <b>True</b> if the content is a choice list of content particles,
    * <b>false</b> otherwise.
    */
   public boolean isChoiceType() {
      return type == CHOICE;
   } //-- isChoiceType

   /**
    * Returns the name of the child element, if content is exactly one child.
    */
   public String getReference() {
      return reference;
   } //-- getReference

   /**
    * Returns enumeration of the children - content particles, that form
    * the content of this Content Particle, if has
    * <tt>SEQ</tt> or <tt>CHOICE</tt> type (sequence or choice list),
    * <tt>null</tt> otherwise.
    */
   public Enumeration getChildren() {
      if (isSeqType() || isChoiceType()) return children.elements();
      return null;
   } //-- getChildren

   /**
    * Sets occurance specification of the content particle to <tt>ONE</tt>.
    */
   public void setOneOccurance() {
      occuranceSpec = ONE;
   } //-- setOneOccurance

   /**
    * <b>True</b> if ocurence specification of the content particle is <tt>ONE</tt>,
    * <b>false</b> otherwise.
    */
   public boolean isOneOccurance() {
      return occuranceSpec == ONE;
   } //-- isOneOccurance

   /**
    * Sets occurance specification of the content particle to
    * <tt>ZERO_OR_ONE</tt>.
    */
   public void setZeroOrOneOccurance() {
      occuranceSpec = ZERO_OR_ONE;
   } //-- setZeroOrOneOccurance

   /**
    * <b>True</b> if occurance specification of the content particle
    * is <tt>ZERO_OR_ONE</tt>, <b>false</b> otherwise.
    */
   public boolean isZeroOrOneOccurance() {
      return occuranceSpec == ZERO_OR_ONE;
   } //-- isZeroOrOneOccurance

   /**
    * Sets occurance specification of the content particle to
    * <tt>ONE_OR_MORE</tt>.
    */
   public void setOneOrMoreOccurances() {
      occuranceSpec = ONE_OR_MORE;
   } //-- setOneOrMoreOccurances

   /**
    * <b>True</b> if occurance specification of the content particle
    * is <tt>ONE_OR_MORE</tt>, <b>false</b> otherwise.
    */
   public boolean isOneOrMoreOccurances() {
      return occuranceSpec == ONE_OR_MORE;
   } //-- isOneOrMoreOccurances

   /**
    * Sets occurance specification of the content particle to
    * <tt>ZERO_OR_MORE</tt>.
    */
   public void setZeroOrMoreOccurances() {
      occuranceSpec = ZERO_OR_MORE;
   } //-- setZeroOrMoreOccurances

   /**
    * <b>True</b> if occurance specification of the content particle
    * is <tt>ZERO_OR_MORE</tt>, <b>false</b> otherwise.
    */
   public boolean isZeroOrMoreOccurances() {
      return occuranceSpec == ZERO_OR_MORE;
   } //-- isZeroOrMoreOccurances

   /**
    * Adds child to the vector of child elements (content particles).
    * @param cp content particle to add to the vector of children.
    */
   public synchronized void addChild(ContentParticle cp) {
      children.add(cp);
   } //-- addChild

} //-- ContentParticle
