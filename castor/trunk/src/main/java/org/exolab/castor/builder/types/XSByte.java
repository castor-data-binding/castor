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
 * Copyright 2001-2002 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id$
 */
package org.exolab.castor.builder.types;

import java.util.Enumeration;

import org.exolab.castor.xml.schema.Facet;
import org.exolab.castor.xml.schema.SimpleType;
import org.exolab.javasource.JClass;
import org.exolab.javasource.JSourceCode;
import org.exolab.javasource.JType;

/**
 * The XML Schema xsd:byte type.
 * @author <a href="mailto:blandin@intalio.com">Arnaud Blandin</a>
 * @version $Revision$ $Date: 2006-04-25 15:08:23 -0600 (Tue, 25 Apr 2006) $
 */
public class XSByte extends XSPatternBase {

    /** Maximum byte (inclusive). */
    private Byte _maxInclusive = null;
    /** Maximum byte (exclusive). */
    private Byte _maxExclusive = null;
    /** Minimum byte (inclusive). */
    private Byte _minInclusive = null;
    /** Minimum byte (exclusive). */
    private Byte _minExclusive = null;

    /** The JType represented by this XSType. */
    private final JType _jType;
    /** True if this type is implemented using the wrapper class. */
    private final boolean _asWrapper;

    /**
     * No-arg constructor.
     */
    public XSByte() {
         this(false);
    }

    /**
     * Constructs a new XSByte.
     * @param asWrapper if true, use the java.lang wrapper class.
     */
    public XSByte(final boolean asWrapper) {
        super(XSType.BYTE_TYPE);
        _asWrapper = asWrapper;
        _jType = (_asWrapper) ? new JClass("java.lang.Byte") : JType.BYTE;
    } //-- XSByte

    /**
     * Returns the JType that this XSType represents.
     * @return the JType that this XSType represents.
     */
    public JType getJType() {
        return _jType;
    }

    /**
     * Returns the maximum exclusive value that this XSByte can hold.
     *
     * @return the maximum exclusive value that this XSByte can hold. If no
     *         maximum exclusive value has been set, Null will be returned
     * @see #getMaxInclusive
     */
    public Byte getMaxExclusive() {
        return _maxExclusive;
    } //-- getMaxExclusive

    /**
     * Returns the maximum inclusive value that this XSByte can hold.
     *
     * @return the maximum inclusive value that this XSByte can hold. If no
     *         maximum inclusive value has been set, Null will be returned
     * @see #getMaxExclusive
     */
    public Byte getMaxInclusive() {
        return _maxInclusive;
    } //-- getMaxInclusive

    /**
     * Returns the minimum exclusive value that this XSByte can hold.
     *
     * @return the minimum exclusive value that this XSByte can hold. If no
     *         minimum exclusive value has been set, Null will be returned
     * @see #getMinInclusive()
     * @see #setMaxInclusive(byte)
     */
    public Byte getMinExclusive() {
        return _minExclusive;
    } //-- getMinExclusive

    /**
     * Returns the minimum inclusive value that this XSByte can hold.
     *
     * @return the minimum inclusive value that this XSByte can hold. If no
     *         minimum inclusive value has been set, Null will be returned
     * @see #getMinExclusive
     */
    public Byte getMinInclusive() {
        return _minInclusive;
    } //-- getMinInclusive

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
     * Sets the maximum exclusive value that this XSByte can hold.
     *
     * @param max
     *            the maximum exclusive value this XSByte can be
     * @see #setMaxInclusive(Byte)
     */
    public void setMaxExclusive(final byte max) {
        _maxExclusive = new Byte(max);
        _maxInclusive = null;
    } //-- setMaxExclusive

    /**
     * Sets the maximum exclusive value that this XSByte can hold.
     *
     * @param max
     *            the maximum exclusive value this XSByte can be
     * @see #setMaxInclusive(byte)
     */
    public void setMaxExclusive(final Byte max) {
        _maxExclusive = max;
        _maxInclusive = null;
    } //-- setMaxExclusive

    /**
     * Sets the maximum inclusive value that this XSByte can hold.
     *
     * @param max
     *            the maximum inclusive value this XSByte can be
     * @see #setMaxExclusive(Byte)
     */
    public void setMaxInclusive(final byte max) {
        _maxInclusive = new Byte(max);
        _maxExclusive = null;
    } //-- setMaxInclusive

    /**
     * Sets the maximum inclusive value that this XSByte can hold.
     *
     * @param max
     *            the maximum inclusive value this XSByte can be
     * @see #setMaxExclusive(byte)
     */
    public void setMaxInclusive(final Byte max) {
        _maxInclusive = max;
        _maxExclusive = null;
    } //-- setMaxInclusive

    /**
     * Sets the minimum exclusive value that this XSByte can hold.
     *
     * @param min
     *            the minimum exclusive value this XSByte can be
     * @see #setMinInclusive(Byte)
     */
    public void setMinExclusive(final byte min) {
        _minExclusive = new Byte(min);
        _minInclusive = null;
    } //-- setMinExclusive

    /**
     * Sets the minimum exclusive value that this XSByte can hold.
     *
     * @param min
     *            the minimum exclusive value this XSByte can be
     * @see #setMinInclusive(byte)
     */
    public void setMinExclusive(final Byte min) {
        _minExclusive = min;
        _minInclusive = null;
    } //-- setMinExclusive

    /**
     * Sets the minimum inclusive value that this XSByte can hold.
     *
     * @param min
     *            the minimum inclusive value this XSByte can be
     * @see #setMinExclusive(Byte)
     */
    public void setMinInclusive(final byte min) {
        _minInclusive = new Byte(min);
        _minExclusive = null;
    } //-- setMinInclusive

    /**
     * Sets the minimum inclusive value that this XSByte can hold.
     *
     * @param min
     *            the minimum inclusive value this XSByte can be
     * @see #setMinExclusive(byte)
     */
    public void setMinInclusive(final Byte min) {
        _minInclusive = min;
        _minExclusive = null;
    } //-- setMinInclusive

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

            if (Facet.MAX_EXCLUSIVE.equals(name)) {
                setMaxExclusive(facet.toByte());
            } else if (Facet.MAX_INCLUSIVE.equals(name)) {
                setMaxInclusive(facet.toByte());
            } else if (Facet.MIN_EXCLUSIVE.equals(name)) {
                setMinExclusive(facet.toByte());
            } else if (Facet.MIN_INCLUSIVE.equals(name)) {
                setMinInclusive(facet.toByte());
            } else if (Facet.PATTERN.equals(name)) {
                setPattern(facet.getValue());
            }
        }
    } //-- setFacets

    /**
     * Returns the String necessary to convert an instance of this XSType to an
     * Object. This method is really only useful for primitive types
     *
     * @param variableName
     *            the name of the instance variable
     * @return the String necessary to convert an instance of this XSType to an
     *         Object
     */
    public String createToJavaObjectCode(final String variableName) {
        if (_asWrapper) {
            return super.createToJavaObjectCode(variableName);
        }

        return "new Byte(" + variableName + ")";
    } //-- toJavaObject

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
        StringBuffer sb = new StringBuffer("((Byte) " + variableName + ")");
        if (!_asWrapper) {
            sb.append(".byteValue()");
        }
        return sb.toString();
    } //-- fromJavaObject

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
        jsc.add("org.exolab.castor.xml.validators.ByteValidator typeValidator"
                + " = new org.exolab.castor.xml.validators.ByteValidator();");

        if (hasMinimum()) {
            Byte min = getMinExclusive();
            if (min != null) {
                jsc.add("typeValidator.setMinExclusive(");
            } else {
                min = getMinInclusive();
                jsc.add("typeValidator.setMinInclusive(");
            }
            jsc.append("(byte)");
            jsc.append(min.toString());
            jsc.append(");");
        }
        if (hasMaximum()) {
            Byte max = getMaxExclusive();
            if (max != null) {
                jsc.add("typeValidator.setMaxExclusive(");
            } else {
                max = getMaxInclusive();
                jsc.add("typeValidator.setMaxInclusive(");
            }
            jsc.append("(byte) ");
            jsc.append(max.toString());
            jsc.append(");");
        }

        //-- fixed values
        if (fixedValue != null) {
            //-- make sure we have a valid value...
            Byte.parseByte(fixedValue);
            jsc.add("typeValidator.setFixed(");
            jsc.append("(byte) ");
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

} //-- XSByte
