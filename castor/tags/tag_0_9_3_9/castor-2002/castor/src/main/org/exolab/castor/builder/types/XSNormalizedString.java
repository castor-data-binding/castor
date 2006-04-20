
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
 * Copyright 2000 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id$
 */
package org.exolab.castor.builder.types;

import org.exolab.castor.xml.schema.SimpleType;
import org.exolab.castor.xml.schema.Facet;
import org.exolab.javasource.JType;
import org.exolab.javasource.JClass;

import java.util.Enumeration;

/**
 * <p>The XSType representing a normalizedString type.
 * <p>normalizedString is simply a XSString with some specific validation
 * @author <a href="blandin@intalio.com">Arnaud Blandin</a>
 * @version $Revision$ $Date$
 */
public final class XSNormalizedString extends XSPatternBase {

    /**
     * The JType represented by this XSType
    **/
    private static final JType jType
        = new JClass("java.lang.String");

    /**
     * The length facet
     */
    private int _length = 0;

    /**
     * The max length facet
    **/
    private int maxLength = -1;

    /**
     * The min length facet
    **/
    private int minLength =  0;

    /**
     * Creates a new XSString
     */
     public  XSNormalizedString() {
        super(XSType.NORMALIZEDSTRING_TYPE);
    }


    /**
     * Returns the Cdata necessary to convert an Object to
     * an instance of this XSType. This method is really only useful
     * for primitive types
     * @param variableName the name of the Object
     * @return the String necessary to convert an Object to an
     * instance of this XSType
    **/
    public String createFromJavaObjectCode(String variableName) {
        return "(String)"+variableName;
    } //-- fromJavaObject

    /**
     * Returns the JType that this XSType represents
     * @return the JType that this XSType represents
    **/
    public JType getJType() {
        return this.jType;
    } //-- getJType

    /**
     * Returns the maximum length occurances of this type can be.
     * A negative value denotes no maximum length
     * @return the maximum length facet
    **/
    public int getMaxLength() {
        return maxLength;
    } //-- getMaxLength

    /**
     * Returns the minimum length occurances of this type can be.
     * @return the minimum length facet
    **/
    public int getMinLength() {
        return minLength;
    } //-- getMinLength

    /**
     * Returns the length that this type must have
     * @return the length that this type must have
     */
     public int getLength() {
        return this._length;
     }
    /**
     * Returns true if a maximum length has been set
     * @return true if a maximum length has been set
    **/
    public boolean hasMaxLength() {
        return (maxLength >= 0);
    } //-- hasMaxLength

    /**
     * Returns true if a minimum length has been set
     * @return true if a minimum length has been set
    **/
    public boolean hasMinLength() {
        return (minLength > 0);
    } //-- hasMinLength

    /**
     * Returns true if a length has been set
     * @return true if a length has been set
     */
    public boolean hasLength() {
        return (_length > 0);
    }

    /**
     * Sets the length of this XSCDATA
     * While setting the length, the maxLength and minLength are also
     * set up to this length
     * @param length the length to set
     * @see #setMaxLength
     * @see #setMinLength
     */
     public void setLength(int length) {
        this._length = length;
        setMaxLength(length);
        setMinLength(length);
    }

    /**
     * Sets the maximum length of this XSCDATA. To remove the max length
     * facet, use a negative value.
     * @param maxLength the maximum length for occurances of this type
    **/
    public void setMaxLength(int maxLength) {
        this.maxLength = maxLength;
    } //-- setMaxLength

    /**
     * Sets the minimum length of this XSCDATA.
     * @param minLength the minimum length for occurances of this type
    **/
    public void setMinLength(int minLength) {
        this.minLength = minLength;
    } //-- setMinLength

    public void setFacets(SimpleType simpleType) {
     //-- copy valid facets
        Enumeration enum = getFacets(simpleType);
        while (enum.hasMoreElements()) {

            Facet facet = (Facet)enum.nextElement();
            String name = facet.getName();

            //-- maxLength
            if (Facet.MAX_LENGTH.equals(name))
                setMaxLength(facet.toInt());
            //-- minLength
            else if (Facet.MIN_LENGTH.equals(name))
                setMinLength(facet.toInt());
            //-- length
            else if (Facet.LENGTH.equals(name))
                setLength(facet.toInt());
            else if (Facet.PATTERN.equals(name))
                setPattern(facet.getValue());

        }
    }
} //-- XSCData