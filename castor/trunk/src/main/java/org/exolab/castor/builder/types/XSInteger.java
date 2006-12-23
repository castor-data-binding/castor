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

import org.exolab.castor.xml.schema.Facet;
import org.exolab.castor.xml.schema.SimpleType;
import org.exolab.javasource.JClass;
import org.exolab.javasource.JSourceCode;
import org.exolab.javasource.JType;

/**
 * The XML Schema Integer type.
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a>
 * @version $Revision$ $Date: 2006-04-25 15:08:23 -0600 (Tue, 25 Apr 2006) $
 */
public class XSInteger extends XSPatternBase {

    /** Maximum integer (inclusive). */
    private Long _maxInclusive = null;
    /** Maximum integer (exclusive). */
    private Long _maxExclusive = null;
    /** Minimum integer (inclusive). */
    private Long _minInclusive = null;
    /** Minimum integer (exclusive). */
    private Long _minExclusive = null;
    /** Total number of digits. */
    private int  _totalDigits = -1;

    /** The JType represented by this XSType. */
    private final JType _jType;
    /** True if this type is implemented using the wrapper class. */
    private final boolean _asWrapper;

    /**
     * No-arg constructor.
     */
    public XSInteger() {
        this(false);
    }

    /**
     * Constructs a new XSInteger.
     * @param asWrapper if true, use the java.lang wrapper class.
     */
    public XSInteger(final boolean asWrapper) {
        super(XSType.INTEGER_TYPE);
         _asWrapper = asWrapper;
         _jType = (_asWrapper) ? new JClass("java.lang.Long") : JType.LONG;
    } //-- XSInteger

    /**
     * Constructs a new XSInteger.  This constructor is used by derived
     * classes.
     * @param asWrapper if true, use the java.lang wrapper class.
     * @param type the type code to be constructed, either XSType.INTEGER_TYPE
     * or one of the types derived from it.
     */
    protected XSInteger(final boolean asWrapper, final short type) {
        super(type);
         _asWrapper = asWrapper;
         _jType = (_asWrapper) ? new JClass("java.lang.Long") : JType.LONG;
    } //-- XSInteger

    /**
     * Returns the JType that this XSType represents.
     * @return the JType that this XSType represents.
     */
    public JType getJType() {
        return _jType;
    }

    /**
     * Returns the maximum exclusive value that this XSInteger can hold.
     * @return the maximum exclusive value that this XSInteger can hold. If
     * no maximum exclusive value has been set, Null will be returned
     * @see #getMaxInclusive
     */
    public Long getMaxExclusive() {
        return _maxExclusive;
    } //-- getMaxExclusive

    /**
     * Returns the maximum inclusive value that this XSInteger can hold.
     * @return the maximum inclusive value that this XSInteger can hold. If
     * no maximum inclusive value has been set, Null will be returned
     * @see #getMaxExclusive
     */
    public Long getMaxInclusive() {
        return _maxInclusive;
    } //-- getMaxInclusive

    /**
     * Returns the minimum exclusive value that this XSInteger can hold.
     * @return the minimum exclusive value that this XSInteger can hold. If
     * no minimum exclusive value has been set, Null will be returned
     * @see #getMinInclusive()
     * @see #setMaxInclusive(int)
     */
    public Long getMinExclusive() {
        return _minExclusive;
    } //-- getMinExclusive

    /**
     * Returns the minimum inclusive value that this XSInteger can hold.
     * @return the minimum inclusive value that this XSInteger can hold. If
     * no minimum inclusive value has been set, Null will be returned
     * @see #getMinExclusive
     */
    public Long getMinInclusive() {
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
     * Sets the maximum exclusive value that this XSInteger can hold.
     * @param max the maximum exclusive value this XSInteger can be
     * @see #setMaxInclusive(Integer)
     */
    public void setMaxExclusive(final long max) {
        _maxExclusive = new Long(max);
        _maxInclusive = null;
    } //-- setMaxExclusive

    /**
     * Sets the maximum exclusive value that this XSInteger can hold.
     * @param max the maximum exclusive value this XSInteger can be
     * @see #setMaxInclusive(int)
     */
    public void setMaxExclusive(final Long max) {
        _maxExclusive = max;
        _maxInclusive = null;
    } //-- setMaxExclusive

    /**
     * Sets the maximum inclusive value that this XSInteger can hold.
     * @param max the maximum inclusive value this XSInteger can be
     * @see #setMaxExclusive(Integer)
     */
    public void setMaxInclusive(final long max) {
        _maxInclusive = new Long(max);
        _maxExclusive = null;
    } //-- setMaxInclusive

    /**
     * Sets the maximum inclusive value that this XSInteger can hold.
     * @param max the maximum inclusive value this XSInteger can be
     * @see #setMaxExclusive(int)
     */
    public void setMaxInclusive(final Long max) {
        _maxInclusive = max;
        _maxExclusive = null;
    } //-- setMaxInclusive

    /**
     * Sets the minimum exclusive value that this XSInteger can hold.
     * @param min the minimum exclusive value this XSInteger can be
     * @see #setMinInclusive(Integer)
     */
    public void setMinExclusive(final long min) {
        _minExclusive = new Long(min);
        _minInclusive = null;
    } //-- setMinExclusive

    /**
     * Sets the minimum exclusive value that this XSInteger can hold.
     * @param min the minimum exclusive value this XSInteger can be
     * @see #setMinInclusive(int)
     */
    public void setMinExclusive(final Long min) {
        _minExclusive = min;
        _minInclusive = null;
    } //-- setMinExclusive

    /**
     * Sets the minimum inclusive value that this XSInteger can hold.
     * @param min the minimum inclusive value this XSInteger can be
     * @see #setMinExclusive(Integer)
     */
    public void setMinInclusive(final long min) {
        _minInclusive = new Long(min);
        _minExclusive = null;
    } //-- setMinInclusive

    /**
     * Sets the minimum inclusive value that this XSInteger can hold.
     * @param min the minimum inclusive value this XSInteger can be
     * @see #setMinExclusive(int)
     */
    public void setMinInclusive(final Long min) {
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
                setMaxExclusive(facet.toLong());
            } else if (Facet.MAX_INCLUSIVE.equals(name)) {
                setMaxInclusive(facet.toLong());
            } else if (Facet.MIN_EXCLUSIVE.equals(name)) {
                setMinExclusive(facet.toLong());
            } else if (Facet.MIN_INCLUSIVE.equals(name)) {
                setMinInclusive(facet.toLong());
            } else if (Facet.PATTERN.equals(name)) {
                setPattern(facet.getValue());
            } else if (Facet.TOTALDIGITS.equals(name)) {
                setTotalDigits(facet.toInt());
            }
        }
    } //-- setFacets

    /**
     * Returns the String necessary to convert an instance of this XSType
     * to an Object. This method is really only useful for primitive types
     * @param variableName the name of the instance variable
     * @return the String necessary to convert an instance of this XSType
     * to an Object
     */
    public String createToJavaObjectCode(final String variableName) {
        if (_asWrapper) {
            return super.createToJavaObjectCode(variableName);
        }

        return "new java.lang.Long(" + variableName + ")";
    } //-- toJavaObject

    /**
     * Returns the String necessary to convert an Object to
     * an instance of this XSType. This method is really only useful
     * for primitive types
     * @param variableName the name of the Object
     * @return the String necessary to convert an Object to an
     * instance of this XSType
     */
    public String createFromJavaObjectCode(final String variableName) {
        StringBuffer sb = new StringBuffer("((java.lang.Long) ");
        sb.append(variableName);
        sb.append(")");
         if (!_asWrapper) {
            sb.append(".intValue()");
        }
        return sb.toString();
    } //-- fromJavaObject

    /**
     * Creates the validation code for an instance of this XSType. The validation
     * code should if necessary create a newly configured TypeValidator, that
     * should then be added to a FieldValidator instance whose name is provided.
     *
     * @param fixedValue a fixed value to use if any
     * @param jsc the JSourceCode to fill in.
     * @param fieldValidatorInstanceName the name of the FieldValidator
     * that the configured TypeValidator should be added to.
     */
    public void validationCode(final JSourceCode jsc, final String fixedValue,
                               final String fieldValidatorInstanceName) {
        jsc.add("org.exolab.castor.xml.validators.IntegerValidator typeValidator"
                + " = new org.exolab.castor.xml.validators.IntegerValidator();");

        if (hasMinimum()) {
            Long min = getMinExclusive();
            if (min != null) {
                jsc.add("typeValidator.setMinExclusive(");
            } else {
                min = getMinInclusive();
                jsc.add("typeValidator.setMinInclusive(");
            }
            jsc.append(min.toString());
            jsc.append(");");
        }
        if (hasMaximum()) {
            Long max = getMaxExclusive();
            if (max != null) {
                jsc.add("typeValidator.setMaxExclusive(");
            } else {
                max = getMaxInclusive();
                jsc.add("typeValidator.setMaxInclusive(");
            }
            jsc.append(max.toString());
            jsc.append(");");
        }

        // -- fixed values
        if (fixedValue != null) {
            // -- make sure we have a valid value...
            // -- Only if we are not using Object

            if (this._jType == JType.LONG) {
                Long.parseLong(fixedValue);
            }

            jsc.add("typeValidator.setFixed(");
            jsc.append(fixedValue);
            jsc.append(");");
        }

        // -- pattern facet
        String pattern = getPattern();
        if (pattern != null) {
            jsc.add("typeValidator.setPattern(\"");
            jsc.append(escapePattern(pattern));
            jsc.append("\");");
        }

        // -- totalDigits
        int totalDigits = getTotalDigits();
        if (totalDigits != -1) {
            jsc.add("typeValidator.setTotalDigits(");
            jsc.append(Integer.toString(totalDigits));
            jsc.append(");");
        }

        jsc.add(fieldValidatorInstanceName + ".setValidator(typeValidator);");
    }

} // -- XSInteger
