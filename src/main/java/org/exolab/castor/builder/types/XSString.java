/*
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
 * Copyright 1999-2002 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id$
 */
package org.exolab.castor.builder.types;

import java.util.Enumeration;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.exolab.castor.xml.schema.Facet;
import org.exolab.castor.xml.schema.SimpleType;
import org.exolab.javasource.JClass;
import org.exolab.javasource.JSourceCode;
import org.exolab.javasource.JType;

/**
 * The XML Schema String type.
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a>
 * @version $Revision$ $Date: 2005-03-05 06:42:06 -0700 (Sat, 05 Mar 2005) $
 */
public final class XSString extends XSPatternBase {

    /** The JType represented by this XSType. */
    private static final JType JTYPE = new JClass("java.lang.String");
    /** Jakarta's common-logging logger. */
    private static final Log LOG = LogFactory.getLog(XSString.class);

    /** The length facet. */
    private int _length        = 0;
    /** The max length facet. */
    private int _maxLength     = -1;
    /** The min length facet. */
    private int _minLength     = 0;
    /** the whiteSpace facet. */
    private String _whiteSpace = "preserve";

    /**
     * Creates a new XSString.
     */
    public XSString() {
        super(XSType.STRING_TYPE);
    }

    /**
     * Returns the String necessary to convert an Object to an instance of this
     * XSType. This method is really only useful for primitive types.
     *
     * @param variableName
     *            the name of the Object
     * @return the String necessary to convert an Object to an instance of this
     *         XSType
     */
    public String createFromJavaObjectCode(final String variableName) {
        return "(java.lang.String) " + variableName;
    } //-- fromJavaObject

    /**
     * Returns the JType that this XSType represents.
     * @return the JType that this XSType represents.
     */
    public JType getJType() {
        return XSString.JTYPE;
    } //-- getJType

    /**
     * Returns the maximum length occurances of this type can be.
     * A negative value denotes no maximum length.
     * @return the maximum length facet
     */
    public int getMaxLength() {
        return _maxLength;
    } //-- getMaxLength

    /**
     * Returns the minimum length occurances of this type can be.
     * @return the minimum length facet
     */
    public int getMinLength() {
        return _minLength;
    } //-- getMinLength

    /**
     * Returns the length that this type must have.
     * @return the length that this type must have.
     */
    public int getLength() {
        return this._length;
    }

    /**
     * Returns the whiteSpace facet of this type.
     * @return the whiteSpace facet of this type.
     */
    public String getWhiteSpace() {
        return this._whiteSpace;
    }

    /**
     * Returns true if a maximum length has been set.
     * @return true if a maximum length has been set.
     */
    public boolean hasMaxLength() {
        return (_maxLength >= 0);
    } //-- hasMaxLength

    /**
     * Returns true if a minimum length has been set.
     * @return true if a minimum length has been set.
     */
    public boolean hasMinLength() {
        return (_minLength > 0);
    } //-- hasMinLength

    /**
     * Returns true if a length has been set.
     * @return true if a length has been set.
     */
    public boolean hasLength() {
        return (_length > 0);
    }

    /**
     * Returns true if the whiteSpace facet is used.
     * @return true if the whiteSpace facet is used.
     */
    public boolean hasWhiteSpace() {
        return (_whiteSpace != null);
    }

    /**
     * Sets the length of this XSString. While setting the length, the maxLength
     * and minLength are also set up to this length.
     *
     * @param length
     *            the length to set
     * @see #setMaxLength
     * @see #setMinLength
     */
    public void setLength(final int length) {
        this._length = length;
        setMaxLength(length);
        setMinLength(length);
    }

    /**
     * Sets the maximum length of this XSString. To remove the max length facet,
     * use a negative value.
     *
     * @param maxLength
     *            the maximum length for occurances of this type
     */
    public void setMaxLength(final int maxLength) {
        this._maxLength = maxLength;
    } //-- setMaxLength

    /**
     * Sets the minimum length of this XSString.
     * @param minLength the minimum length for occurances of this type
     */
    public void setMinLength(final int minLength) {
        this._minLength = minLength;
    } //-- setMinLength

    /**
     * Sets the whiteSpace facet of this XSString.  The value of the whiteSpace
     * facet must be one of the following:
     * <ul>
     *  <li>preserve</li>
     *  <li>replace</li>
     *  <li>collapse</li>
     * </ul>
     * Any other value will generate a Warning and set the whiteSpace to
     * "preserve".
     * @param value the whiteSpace value
     */
    public void setWhiteSpace(final String value) {
        if (value.equals("preserve")) {
            this._whiteSpace = value;
        } else if (value.equals("replace")) {
            this._whiteSpace = value;
        } else if (value.equals("collapse")) {
            this._whiteSpace = value;
        } else {
            // TODO: consider throwing an exception/implement exception handling strategy
            LOG.warn("Warning : " + value + " is a bad entry for the whiteSpace value");
            this._whiteSpace = value;
        }
    }

    /**
     * Transfer facets from the provided simpleType to <code>this</code>.
     *
     * @param simpleType
     *            The SimpleType containing our facets.
     * @see org.exolab.castor.builder.types.XSType#getFacets
     */
    public void setFacets(final SimpleType simpleType) {
        //-- copy valid facets
        Enumeration enumeration = getFacets(simpleType);
        while (enumeration.hasMoreElements()) {
            Facet facet = (Facet) enumeration.nextElement();
            String name = facet.getName();

            if (Facet.MAX_LENGTH.equals(name)) {
                setMaxLength(facet.toInt());
            } else if (Facet.MIN_LENGTH.equals(name)) {
                setMinLength(facet.toInt());
            } else if (Facet.LENGTH.equals(name)) {
                setLength(facet.toInt());
            } else if (Facet.PATTERN.equals(name)) {
                addPattern(facet.getValue());
            } else if (Facet.WHITESPACE.equals(name)) {
                setWhiteSpace(facet.getValue());
            }
        }
    }

    /**
     * Creates the validation code for an instance of this XSType. The
     * validation code should if necessary create a newly configured
     * TypeValidator, that should then be added to a FieldValidator instance
     * whose name is provided.
     *
     * @param fixedValue
     *            a fixed value to use if any
     * @param jsc
     *            the JSourceCode to fill in.
     * @param fieldValidatorInstanceName
     *            the name of the FieldValidator that the configured
     *            TypeValidator should be added to.
     */
    public void validationCode(final JSourceCode jsc, final String fixedValue,
                               final String fieldValidatorInstanceName) {
        jsc.add("org.exolab.castor.xml.validators.StringValidator typeValidator ="
                + " new org.exolab.castor.xml.validators.StringValidator();");

        if (hasLength()) {
            jsc.add("typeValidator.setLength(");
            jsc.append(Integer.toString(getLength()));
            jsc.append(");");
        } else {
            if (hasMinLength()) {
                jsc.add("typeValidator.setMinLength(");
                jsc.append(Integer.toString(getMinLength()));
                jsc.append(");");
            }
            if (hasMaxLength()) {
                jsc.add("typeValidator.setMaxLength(");
                jsc.append(Integer.toString(getMaxLength()));
                jsc.append(");");
            }
        }

        if (hasWhiteSpace()) {
            jsc.add("typeValidator.setWhiteSpace(\"");
            jsc.append(getWhiteSpace());
            jsc.append("\");");
        }

        //-- fixed values
        if (fixedValue != null) {
            jsc.add("typeValidator.setFixed(");
            //fixed should be "the value"
            jsc.append(fixedValue);
            jsc.append(");");
        }

        // pattern facet
        codePatternFacet(jsc, "typeValidator");

        jsc.add(fieldValidatorInstanceName + ".setValidator(typeValidator);");
    }

} //-- XSString
