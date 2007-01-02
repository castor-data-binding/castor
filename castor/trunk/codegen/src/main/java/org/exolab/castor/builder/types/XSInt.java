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
 * The XML Schema 'Int' type.
 *
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a>
 * @version $Revision$ $Date: 2006-04-25 15:08:23 -0600 (Tue, 25 Apr 2006) $
 */
public class XSInt extends XSPatternBase {

    /** Maximum integer (inclusive). */
    private Integer _maxInclusive = null;
    /** Maximum integer (exclusive). */
    private Integer _maxExclusive = null;
    /** Minimum integer (inclusive). */
    private Integer _minInclusive = null;
    /** Minimum integer (exclusive). */
    private Integer _minExclusive = null;
    /** Maximum (inclusive) number of digits. */
    private int  _totalDigits = -1;

    /** The JType represented by this XSType. */
    private final JType _jType;
    /** True if this type is implemented using the wrapper class. */
    private final boolean _asWrapper;

    /**
     * No-arg constructor.
     */
    public XSInt() {
        this(false);
    }

    /**
     * Constructs a new XSInt.
     * @param asWrapper if true, use the java.lang wrapper class.
     */
    public XSInt(final boolean asWrapper) {
        this(asWrapper, XSType.INT_TYPE);
    } // -- XSInt

    /**
     * Constructs a new XSInt.  This constructor is used by derived
     * classes.
     * @param asWrapper if true, use the java.lang wrapper class.
     * @param type the type code to be constructed, either XSType.INT_TYPE
     * or one of the types derived from it.
     */
    protected XSInt(final boolean asWrapper, final short type) {
        super(type);
         _asWrapper = asWrapper;
         
         if (_asWrapper) {
             _jType = new JClass("java.lang.Integer");
         } else {
             _jType = JType.INT;
         }
         
         setMinInclusive(Integer.MIN_VALUE);
         setMaxInclusive(Integer.MAX_VALUE);
    } //-- XSInteger

    /**
     * Returns the JType that this XSType represents.
     * @return the JType that this XSType represents.
     */
    public final JType getJType() {
        return _jType;
    }

    /**
     * Returns the maximum exclusive value that this XSInt can hold.
     *
     * @return the maximum exclusive value that this XSInt can hold. If no
     *         maximum exclusive value has been set, Null will be returned
     * @see #getMaxInclusive
     */
    public final Integer getMaxExclusive() {
        return _maxExclusive;
    } // -- getMaxExclusive

    /**
     * Returns the maximum inclusive value that this XSInt can hold.
     *
     * @return the maximum inclusive value that this XSInt can hold. If no
     *         maximum inclusive value has been set, Null will be returned
     * @see #getMaxExclusive
     */
    public final Integer getMaxInclusive() {
        return _maxInclusive;
    } // -- getMaxInclusive

    /**
     * Returns the minimum exclusive value that this XSInt can hold.
     *
     * @return the minimum exclusive value that this XSInt can hold. If no
     *         minimum exclusive value has been set, Null will be returned
     * @see #getMinInclusive()
     * @see #setMaxInclusive(int)
     */
    public final Integer getMinExclusive() {
        return _minExclusive;
    } // -- getMinExclusive

    /**
     * Returns the minimum inclusive value that this XSInt can hold.
     *
     * @return the minimum inclusive value that this XSInt can hold. If no
     *         minimum inclusive value has been set, Null will be returned
     * @see #getMinExclusive
     */
    public final Integer getMinInclusive() {
        return _minInclusive;
    } // -- getMinInclusive

    /**
     * Returns the totalDigits facet value of this XSInteger.
     * @return the totalDigits facet value of this XSInteger.
     */
    public final int getTotalDigits() {
        return _totalDigits;
    }

    /**
     * Returns true if a maximum (inclusive or exclusive) has been set.
     * @return true if a maximum (inclusive or exclusive) has been set.
     */
    public final boolean hasMaximum() {
        return _maxInclusive != null || _maxExclusive != null;
    } //-- hasMaximum

    /**
     * Returns true if a minimum (inclusive or exclusive) has been set.
     * @return true if a minimum (inclusive or exclusive) has been set.
     */
    public final boolean hasMinimum() {
        return _minInclusive != null || _minExclusive != null;
    } //-- hasMinimum

    /**
     * Sets the maximum exclusive value that this XSInt can hold.
     *
     * @param max
     *            the maximum exclusive value this XSInt can be
     * @see #setMaxInclusive(Integer)
     */
    public final void setMaxExclusive(final int max) {
        _maxExclusive = new Integer(max);
        _maxInclusive = null;
    } // -- setMaxExclusive

    /**
     * Sets the maximum exclusive value that this XSInt can hold.
     *
     * @param max
     *            the maximum exclusive value this XSInt can be
     * @see #setMaxInclusive(int)
     */
    public final void setMaxExclusive(final Integer max) {
        _maxExclusive = max;
        _maxInclusive = null;
    } // -- setMaxExclusive

    /**
     * Sets the maximum inclusive value that this XSInt can hold.
     *
     * @param max
     *            the maximum inclusive value this XSInt can be
     * @see #setMaxExclusive(Integer)
     */
    public final void setMaxInclusive(final int max) {
        _maxInclusive = new Integer(max);
        _maxExclusive = null;
    } // -- setMaxInclusive

    /**
     * Sets the maximum inclusive value that this XSInt can hold.
     *
     * @param max
     *            the maximum inclusive value this XSInt can be
     * @see #setMaxExclusive(int)
     */
    public final void setMaxInclusive(final Integer max) {
        _maxInclusive = max;
        _maxExclusive = null;
    } // -- setMaxInclusive

    /**
     * Sets the minimum exclusive value that this XSInt can hold.
     *
     * @param min
     *            the minimum exclusive value this XSInt can be
     * @see #setMinInclusive(Integer)
     */
    public final void setMinExclusive(final int min) {
        _minExclusive = new Integer(min);
        _minInclusive = null;
    } // -- setMinExclusive

    /**
     * Sets the minimum exclusive value that this XSInt can hold.
     *
     * @param min
     *            the minimum exclusive value this XSInt can be
     * @see #setMinInclusive(int)
     */
    public final void setMinExclusive(final Integer min) {
        _minExclusive = min;
        _minInclusive = null;
    } // -- setMinExclusive

    /**
     * Sets the minimum inclusive value that this XSInt can hold.
     *
     * @param min
     *            the minimum inclusive value this XSInt can be
     * @see #setMinExclusive(Integer)
     */
    public final void setMinInclusive(final int min) {
        _minInclusive = new Integer(min);
        _minExclusive = null;
    } // -- setMinInclusive

    /**
     * Sets the minimum inclusive value that this XSInt can hold.
     *
     * @param min
     *            the minimum inclusive value this XSInt can be
     * @see #setMinExclusive(int)
     */
    public final void setMinInclusive(final Integer min) {
        _minInclusive = min;
        _minExclusive = null;
    } // -- setMinInclusive

    /**
     * Sets the totalDigits facet for this XSInteger.
     * @param totalDig the value of totalDigits (must be > 0)
     */
     public final void setTotalDigits(final int totalDig) {
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
    public final void setFacets(final SimpleType simpleType) {
        // -- copy valid facets
        Enumeration enumeration = getFacets(simpleType);
        while (enumeration.hasMoreElements()) {
            Facet facet = (Facet) enumeration.nextElement();
            String name = facet.getName();

            if (Facet.MAX_EXCLUSIVE.equals(name)) {
                setMaxExclusive(facet.toInt());
            } else if (Facet.MAX_INCLUSIVE.equals(name)) {
                setMaxInclusive(facet.toInt());
            } else if (Facet.MIN_EXCLUSIVE.equals(name)) {
                setMinExclusive(facet.toInt());
            } else if (Facet.MIN_INCLUSIVE.equals(name)) {
                setMinInclusive(facet.toInt());
            } else if (Facet.PATTERN.equals(name)) {
                addPattern(facet.getValue());
            } else if (Facet.TOTALDIGITS.equals(name)) {
                setTotalDigits(facet.toInt());
            } else if (Facet.FRACTIONDIGITS.equals(name)) {
                if (facet.toInt() != 0) {
                    throw new IllegalArgumentException("fractionDigits must be 0 for "
                            + this.getName());
                }
            } else if (Facet.WHITESPACE.equals(name)) {
                // If this facet is set correctly, we don't need to do anything
                if (!facet.getValue().equals(Facet.WHITESPACE_COLLAPSE)) {
                    throw new IllegalArgumentException("Warning: The facet 'whitespace'"
                            + " can only be set to '"
                            + Facet.WHITESPACE_COLLAPSE + "' for '"
                            + this.getName() + "'.");
                }
            }
        }
    } // -- toXSInt

    /**
     * Returns the String necessary to convert an instance of this XSType to an
     * Object. This method is really only useful for primitive types.
     *
     * @param variableName
     *            the name of the instance variable
     * @return the String necessary to convert an instance of this XSType to an
     *         Object
     */
    public final String createToJavaObjectCode(final String variableName) {
        if (_asWrapper) {
            return super.createToJavaObjectCode(variableName);
        }

        return "new java.lang.Integer(" + variableName + ")";
    } // -- toJavaObject

    /**
     * Returns the String necessary to convert an Object to an instance of this
     * XSType. This method is really only useful for primitive types
     *
     * @param variableName
     *            the name of the Object
     * @return the String necessary to convert an Object to an instance of this
     *         XSType
     */
    public final String createFromJavaObjectCode(final String variableName) {
        StringBuffer sb = new StringBuffer("((java.lang.Integer) ");
        sb.append(variableName);
        sb.append(")");
        if (!_asWrapper) {
            sb.append(".intValue()");
        }
        return sb.toString();
    } // -- fromJavaObject

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
    public final void validationCode(final JSourceCode jsc, final String fixedValue,
                               final String fieldValidatorInstanceName) {
        jsc.add("org.exolab.castor.xml.validators.IntValidator typeValidator"
                + " = new org.exolab.castor.xml.validators.IntValidator();");

        if (_minExclusive != null) {
            jsc.add("typeValidator.setMinExclusive(" + _minExclusive + ");");
        } else if (_minInclusive != null) {
            jsc.add("typeValidator.setMinInclusive(" + _minInclusive + ");");
        }

        if (_maxExclusive != null) {
            jsc.add("typeValidator.setMaxExclusive(" + _maxExclusive + ");");
        } else if (_maxInclusive != null) {
            jsc.add("typeValidator.setMaxInclusive(" + _maxInclusive + ");");
        }

        // -- fixed values
        if (fixedValue != null) {
            // -- make sure we have a valid value...
            // -- Only if we are not using Object
            if (_jType == JType.INT) {
                Integer.parseInt(fixedValue);
            }

            jsc.add("typeValidator.setFixed(");
            jsc.append(fixedValue);
            jsc.append(");");
        }

        // pattern facet
        codePatternFacet(jsc, "typeValidator");

        // -- totalDigits
        int totalDigits = getTotalDigits();
        if (totalDigits != -1) {
            jsc.add("typeValidator.setTotalDigits(");
            jsc.append(Integer.toString(totalDigits));
            jsc.append(");");
        }

        jsc.add(fieldValidatorInstanceName + ".setValidator(typeValidator);");
    }

} // -- XSInt
