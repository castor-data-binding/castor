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
 * Copyright 1999 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id$
 */
package org.exolab.castor.builder.info;

/**
 * A class to hold group information.
 * 
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a>
 * @version $Revision$ $Date: 2003-03-03 00:05:44 -0700 (Mon, 03 Mar 2003) $
 */
public final class GroupInfo {
    /**
     * The compositor value that indicates that all fields are required, but
     * order is not important. <I>default</I>
     */
    public static final int ALL    = 0;
    /**
     * The compositor value that indicates that only one field may be present.
     */
    public static final int CHOICE = 1;
    /**
     * The compositor value that indicates that all fields are required and
     * order is important.
     */
    public static final int SEQUENCE = 2;

    /** A flag indicating if the object described by this XML info can appear  more than once. */
    private boolean _multivalued = false;
    /** indicates the XML object must appear at least once. */
    private boolean _required = false;
    /** The compositor for this XMLInfo. */
    private int _compositor = ALL;
    /** The minimum occurance for this group. */
    private int _minOccurs = 1;
    /** The maximum occurance for this group. */
    private int _maxOccurs = 1;

    /**
     * Creates a new GroupInfo.
     */
    public GroupInfo() {
        super();
    } //-- GroupInfo

    /**
     * Returns the maximum occurance for this group.
     *
     * @return the maximum occurance for this group.
     */
    public int getMaxOccurs() {
        return _maxOccurs;
    } //-- getMaxOccurs

    /**
     * Returns the minimum occurance for this group.
     *
     * @return the minimum occurance for this group.
     */
    public int getMinOccurs() {
        return _minOccurs;
    } //-- getMinOccurs

    /**
     * Return whether or not the object described by this XMLInfo is
     * multi-valued (appears more than once in the XML document).
     *
     * @return true if this object can appear more than once.
     */
    public boolean isMultivalued() {
        return _multivalued;
    } //-- isMultivalued

    /**
     * Return true if the XML object described by this GroupInfo must appear at
     * least once in the XML document (or object model).
     *
     * @return true if the XML object must appear at least once.
     */
    public boolean isRequired() {
        return _required;
    } //-- isRequired

    /**
     * Returns true if the compositor of this GroupInfo is a choice.
     * @return true if the compositor of this GroupInfo is a choice.
     */
    public boolean isChoice() {
        return (_compositor == CHOICE);
    } //-- isChoice

    /**
     * Returns true if the compositor of this GroupInfo is a sequence.
     *
     * @return true if the compositor of this GroupInfo is a sequence.
     */
    public boolean isSequence() {
        return (_compositor == SEQUENCE);
    } //-- isSequence

    /**
     * Sets the compositor for the fields of this group to be "all".
     */
    public void setAsAll() {
        this._compositor = ALL;
    } //-- setAsAll

    /**
     * Sets the compositor for the fields of this group to be a choice.
     */
    public void setAsChoice() {
        this._compositor = CHOICE;
    } //-- setAsChoice

    /**
     * Sets the compositor for the fields of this group to be a sequence.
     */
    public void setAsSequence() {
        this._compositor = SEQUENCE;
    } //-- setAsSequence

    /**
     * Sets the maximum occurance for this group.
     *
     * @param maxOccurs
     *            the maximum occurance this group must appear
     */
    public void setMaxOccurs(final int maxOccurs) {
        _maxOccurs = (maxOccurs < 0) ? -1 : maxOccurs;
    } //-- setMaxOccurs

    /**
     * Sets the minimum occurance for this group.
     *
     * @param minOccurs
     *            the minimum occurance this group must appear
     */
    public void setMinOccurs(final int minOccurs) {
        _minOccurs = (minOccurs < 0) ? 1 : minOccurs;
    } //-- setMinOccurs

    /**
     * Sets whether the XML object can appear more than once in the XML document.
     *
     * @param multivalued
     *            the boolean indicating whether or not the object can appear
     *            more than once
     */
    public void setMultivalued(final boolean multivalued) {
        this._multivalued = multivalued;
    } //-- setMultivalued

    /**
     * Sets whether or not the XML object must appear at least once.
     *
     * @param required
     *            the flag indicating whether or not this XML object is required
     */
    public void setRequired(final boolean required) {
        this._required = required;
    } //-- setRequired

} //-- GroupInfo
