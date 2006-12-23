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
 * Date         Author          Changes
 * 06/01/2001   Arnaud Blandin  Upgrade to XML Schema Recommendation
 * 10/31/200    Arnaud Blandin  support for min/max, scale&precision facets
 */
package org.exolab.castor.builder.types;

import java.util.Enumeration;

import org.exolab.castor.xml.schema.Facet;
import org.exolab.castor.xml.schema.SimpleType;
import org.exolab.javasource.JClass;
import org.exolab.javasource.JSourceCode;
import org.exolab.javasource.JType;

/**
 * The xsd:decimal XML Schema datatype.
 * @author <a href="mailto:andrew.fawcett@coda.com">Andrew Fawcett</a>
 */
public  class XSDecimal extends XSPatternBase {

    /** The JType represented by this XSType. */
    private static final JType JTYPE = new JClass("java.math.BigDecimal");

    /** Maximum decimal (inclusive). */
    private java.math.BigDecimal _maxInclusive = null;
    /** Maximum decimal (exclusive). */
    private java.math.BigDecimal _maxExclusive = null;
    /** Minimum decimal (inclusive). */
    private java.math.BigDecimal _minInclusive = null;
    /** Minimum decimal (exclusive). */
    private java.math.BigDecimal _minExclusive = null;
    /** Total number of digits. */
    private int  _totalDigits = -1;
    /** Total number of fraction digits. */
    private int _fractionDigits = -1;

    /**
     * No-arg constructor.
     */
    public XSDecimal() {
        super(XSType.DECIMAL_TYPE);
    } //-- XSNMToken

    /**
     * Returns the Java code neccessary to create a new instance of the JType
     * associated with this XSType.
     *
     * @return the Java code neccessary to create a new instance of the JType
     *         associated with this XSType.
     */
    public String newInstanceCode() {
        String result = "new java.math.BigDecimal(0);";
        return result;
    }

    /**
     * Returns the String necessary to convert an Object to an instance of this
     * XSType. This method is really only useful for primitive types
     *
     * @param variableName
     *            the name of the Object
     * @return the String necessary to convert an Object to an instance of this
     *         XSType
     */
    public String createFromJavaObjectCode(final String variableName) {
        return "(java.math.BigDecimal) " + variableName;
    } //-- fromJavaObject

    /**
     * Returns the maximum exclusive value that this XSInteger can hold.
     *
     * @return the maximum exclusive value that this XSInteger can hold. If no
     *         maximum exclusive value has been set, Null will be returned
     * @see #getMaxInclusive
     */
    public java.math.BigDecimal getMaxExclusive() {
        return _maxExclusive;
    } //-- getMaxExclusive

    /**
     * Returns the maximum inclusive value that this XSInteger can hold.
     *
     * @return the maximum inclusive value that this XSInteger can hold. If no
     *         maximum inclusive value has been set, Null will be returned
     * @see #getMaxExclusive
     */
    public java.math.BigDecimal getMaxInclusive() {
        return _maxInclusive;
    } //-- getMaxInclusive

    /**
     * Returns the minimum exclusive value that this XSInteger can hold.
     *
     * @return the minimum exclusive value that this XSInteger can hold. If no
     *         minimum exclusive value has been set, Null will be returned
     * @see #getMinInclusive
     * @see #setMaxInclusive
     */
    public java.math.BigDecimal getMinExclusive() {
        return _minExclusive;
    } //-- getMinExclusive

    /**
     * Returns the minimum inclusive value that this XSInteger can hold.
     *
     * @return the minimum inclusive value that this XSInteger can hold. If no
     *         minimum inclusive value has been set, Null will be returned
     * @see #getMinExclusive
     */
    public java.math.BigDecimal getMinInclusive() {
        return _minInclusive;
    } //-- getMinInclusive
    /**
     * Returns the totalDigits facet value of this XSInteger.
     * @return the totalDigits facet value of this XSInteger.
     */
    public int getTotalDigits() {
        return _totalDigits;
    }

    /**
     * Returns the fractionDigits facet value of this XSInteger.
     * @return the fractionDigits facet value of this XSInteger.
     */
    public int getFractionDigits() {
        return _fractionDigits;
    }

    /**
     * Returns true if a maximum (inclusive or exclusive) has been set.
     * @return true if a maximum (inclusive or exclusive) has been set.
     */
    public boolean hasMaximum() {
        return _maxInclusive != null || _maxExclusive != null;
    } //-- hasMaximum

    /**
     * Returns true if a minimum (inclusive or exclusive) has been set.
     * @return true if a minimum (inclusive or exclusive) has been set.
     */
    public boolean hasMinimum() {
        return _minInclusive != null || _minExclusive != null;
    } //-- hasMinimum

    /**
     * Sets the maximum exclusive value that this XSDecimal can hold.
     *
     * @param max
     *            the maximum exclusive value this XSDecimal can be
     * @see #setMaxInclusive
     */
    public void setMaxExclusive(final java.math.BigDecimal max) {
        _maxExclusive = max;
        _maxInclusive = null;
    } //-- setMaxExclusive

    /**
     * Sets the maximum inclusive value that this XSDecimal can hold.
     *
     * @param max
     *            the maximum inclusive value this XSDecimal can be
     * @see #setMaxExclusive
     */
    public void setMaxInclusive(final java.math.BigDecimal max) {
        _maxInclusive = max;
        _maxExclusive = null;
    } //-- setMaxInclusive

    /**
     * Sets the minimum exclusive value that this XSDecimal can hold.
     *
     * @param min
     *            the minimum exclusive value this XSDecimal can be
     * @see #setMinInclusive
     */
    public void setMinExclusive(final java.math.BigDecimal min) {
        _minExclusive = min;
        _minInclusive = null;
    } //-- setMinExclusive

    /**
     * Sets the minimum inclusive value that this XSDecimalcan hold.
     *
     * @param min
     *            the minimum inclusive value this XSDecimal can be
     * @see #setMinExclusive
     */
    public void setMinInclusive(final java.math.BigDecimal  min) {
        _minInclusive = min;
        _minExclusive = null;
    } //-- setMinInclusive

    /**
     * Sets the totalDigits facet for this XSInteger.
     * @param totalDig the value of totalDigits (must be >0)
     */
    public void setTotalDigits(final int totalDig) {
        if (totalDig <= 0) {
            throw new IllegalArgumentException(this.getName()
                    + ": the totalDigits facet must be positive");
        }
        _totalDigits = totalDig;
    }

    /**
     * Sets the fractionDigits facet for this XSInteger.
     * @param fractionDig the value of fractionDigits (must be >=0)
     */
    public void setFractionDigits(final int fractionDig) {
        if (fractionDig < 0) {
            throw new IllegalArgumentException(this.getName()
                    + ": the fractionDigits facet must be positive");
        }
        _fractionDigits = fractionDig;
    }

    /**
     * Transfer facets from the provided simpleType to <code>this</code>.
     *
     * @param simpleType
     *            The SimpleType containing our facets.
     * @see org.exolab.castor.builder.types.XSType#getFacets
     */
    public void setFacets(final SimpleType simpleType) {
        Enumeration enumeration = getFacets(simpleType);
        while (enumeration.hasMoreElements()) {
            Facet facet = (Facet) enumeration.nextElement();
            String name = facet.getName();

            if (Facet.MAX_EXCLUSIVE.equals(name)) {
                setMaxExclusive(new java.math.BigDecimal(facet.getValue()));
            } else if (Facet.MAX_INCLUSIVE.equals(name)) {
                setMaxInclusive(new java.math.BigDecimal(facet.getValue()));
            } else if (Facet.MIN_EXCLUSIVE.equals(name)) {
                setMinExclusive(new java.math.BigDecimal(facet.getValue()));
            } else if (Facet.MIN_INCLUSIVE.equals(name)) {
                setMinInclusive(new java.math.BigDecimal(facet.getValue()));
            } else if (Facet.TOTALDIGITS.equals(name)) {
                setTotalDigits(facet.toInt());
            } else if (Facet.FRACTIONDIGITS.equals(name)) {
                setFractionDigits(facet.toInt());
            } else if (Facet.PATTERN.equals(name)) {
                setPattern(facet.getValue());
            } else if (Facet.WHITESPACE.equals(name)) {
                // If this facet is set correctly, we don't need to do anything
                if (!facet.getValue().equals(Facet.WHITESPACE_COLLAPSE)) {
                    throw new IllegalArgumentException("Warning: The facet 'whitespace'"
                            + " can only be set to '"
                            + Facet.WHITESPACE_COLLAPSE + "' for 'decimal'.");
                }
            }
        }
    } //-- setFacets

    /**
     * Returns the JType that this XSType represents.
     * @return the JType that this XSType represents.
     */
    public JType getJType() {
        return XSDecimal.JTYPE;
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
        jsc.add("org.exolab.castor.xml.validators.DecimalValidator typeValidator"
                + " = new org.exolab.castor.xml.validators.DecimalValidator();");

        if (_minExclusive != null) {
            jsc.add("java.math.BigDecimal min = new java.math.BigDecimal(\"" + _minExclusive + "\");");
            jsc.add("typeValidator.setMinExclusive(min);");
        } else if (_minInclusive != null) {
            jsc.add("java.math.BigDecimal min = new java.math.BigDecimal(\"" + _minInclusive + "\");");
            jsc.add("typeValidator.setMinInclusive(min);");
        }

        if (_maxExclusive != null) {
            jsc.add("java.math.BigDecimal max = new java.math.BigDecimal(\"" + _maxExclusive + "\");");
            jsc.add("typeValidator.setMaxExclusive(max);");
        } else if (_maxInclusive != null) {
            jsc.add("java.math.BigDecimal max = new java.math.BigDecimal(\"" + _maxInclusive + "\");");
            jsc.add("typeValidator.setMaxInclusive(max);");
        }

        //-- totalDigits
        int totalDigits = getTotalDigits();

        if (totalDigits != -1) {
            jsc.add("typeValidator.setTotalDigits(");
            jsc.append(Integer.toString(totalDigits));
            jsc.append(");");
        }

        //-- fractionDigits
        int fractionDigits = getFractionDigits();
        if (fractionDigits != -1) {
            jsc.add("typeValidator.setFractionDigits(");
            jsc.append(Integer.toString(fractionDigits));
            jsc.append(");");
        }

        //-- fixed values
        if (fixedValue != null) {
            jsc.add("typeValidator.setFixed(");
            jsc.append(fixedValue);
            jsc.append(");");
        }

        //-- pattern facet
        String pattern = getPattern();
        if (pattern != null) {
            jsc.add("typeValidator.setPattern(\"");
            jsc.append(escapePattern(pattern));
            jsc.append("\");");
        }

        jsc.add(fieldValidatorInstanceName + ".setValidator(typeValidator);");
    }

}
