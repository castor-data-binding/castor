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
 * Copyright 2000-2003 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id$
 * Date         Author           Changes
 * 10/31/2000   Arnaud Blandin   Created
 */
package org.exolab.castor.xml.validators;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;

import org.exolab.castor.xml.TypeValidator;
import org.exolab.castor.xml.ValidationContext;
import org.exolab.castor.xml.ValidationException;

/**
 * The Decimal Validation class. This class handles validation for the Decimal
 * type.
 * <p>
 * TODO: enumeration
 *
 * @author <a href="mailto:blandin@intalio.com">Arnaud Blandin</a>
 * @author <a href="mailto:edward.kuns@aspect.com">Edward Kuns</a>
 * @version $Revision$ $Date: 2006-04-25 15:08:23 -0600 (Tue, 25 Apr 2006) $
 */
public class DecimalValidator extends PatternValidator implements TypeValidator {

    private static Method bdMethodToPlainString = null;

    static {
        try {
            bdMethodToPlainString = BigDecimal.class.getMethod("toPlainString", (Class[]) null);
        } catch (NoSuchMethodException e) {
            // If it does not exist, we're in Java 1.4.2 or earlier
        }
    }

    private BigDecimal _fixed           = null;

    private BigDecimal _min             = null;

    private BigDecimal _max             = null;

    private int        _totalDigits     = -1;

    private int        _fractionDigits  = -1;

    private boolean    _hasMinExclusive = false;

    private boolean    _hasMaxExclusive = false;

    /**
     * Creates a new DecimalValidator with no restrictions
     */
    public DecimalValidator() {
        super();
    } // -- decimalValidator

    /**
     * Clears the maximum value for this DecimalValidator
     */
    public void clearMax() {
        _max = null;
        _hasMaxExclusive = false;
    } // -- clearMax

    /**
     * Clears the minimum value for this DecimalValidator
     */
    public void clearMin() {
        _min = null;
        _hasMinExclusive = false;
    } // -- clearMin

    /**
     * Returns the fixed value that decimals validated with this validator must
     * be equal to. A null value is returned if no fixed value has been
     * specified.
     *
     * @return the fixed value to validate against.
     */
    public BigDecimal getFixed() {
        return _fixed;
    } // -- getFixed

    /**
     * Returns the maximum value that decimals validated with this validator
     * must be equal to or less than. A null value is returned if no maximum
     * value has been specified.
     *
     * @return the maximum inclusive value to validate against.
     */
    public BigDecimal getMaxInclusive() {
        return _max;
    } // -- getMaxInclusive

    /**
     * Returns the minimum value that decimals validated with this validator
     * must be equal to or greater than. A null value is returned if no minimum
     * value has been specified.
     *
     * @return the minimum inclusive value to validate against.
     */
    public BigDecimal getMinInclusive() {
        return _min;
    } // -- getMinInclusive

    /**
     * Returns true if a fixed value, to validate against, has been set.
     *
     * @return true if a fixed value has been set.
     */
    public boolean hasFixed() {
        return (_fixed != null);
    } // -- hasFixed

    /**
     * Sets the minimum value that decimals validated with this validator must
     * be greater than
     *
     * @param minValue
     *            the minimum value an decimal validated with this validator
     *            must be greater than
     */
    public void setMinExclusive(BigDecimal minValue) {
        if (minValue == null) {
            throw new IllegalArgumentException("argument 'minValue' must not be null.");
        }
        _min = minValue;
        _hasMinExclusive = true;
    } // -- setMinExclusive

    /**
     * Sets the minimum value that decimals validated with this validator are
     * allowed to be
     *
     * @param minValue
     *            the minimum value an decimal validated with this validator may
     *            be
     */
    public void setMinInclusive(BigDecimal minValue) {
        _min = minValue;
        _hasMinExclusive = false;
    } // -- setMinInclusive

    /**
     * Sets the maximum value that decimals validated with this validator must
     * be less than
     *
     * @param maxValue
     *            the maximum value an decimal validated with this validator
     *            must be less than
     */
    public void setMaxExclusive(BigDecimal maxValue) {
        if (maxValue == null) {
            throw new IllegalArgumentException("argument 'maxValue' must not be null.");
        }
        _max = maxValue;
        _hasMaxExclusive = true;
    } // -- setMaxExclusive

    /**
     * Sets the maximum value that decimals validated with this validator are
     * allowed to be
     *
     * @param maxValue
     *            the maximum value an decimal validated with this validator may
     *            be
     */
    public void setMaxInclusive(BigDecimal maxValue) {
        _max = maxValue;
        _hasMaxExclusive = false;
    } // -- setMaxInclusive

    /**
     * Sets the totalDigits facet for this decimal validator.
     *
     * @param totalDig
     *            the value of totalDigits (must be >0)
     */
    public void setTotalDigits(int totalDig) {
        if (totalDig <= 0) {
            throw new IllegalArgumentException(
                    "DecimalValidator: the totalDigits facet must be positive");
        }
        _totalDigits = totalDig;
    }

    /**
     * Sets the fractionDigits facet for this decimal validator.
     *
     * @param fractionDig
     *            the value of fractionDigits (must be >=0)
     */
    public void setFractionDigits(int fractionDig) {
        if (fractionDig < 0) {
            throw new IllegalArgumentException(
                    "DecimalValidator: the fractionDigits facet must be positive");
        }
        _fractionDigits = fractionDig;
    }

    /**
     * Sets the fixed value the decimal to validate must be equal to.
     *
     * @param fixed
     *            the fixed value
     */
    public void setFixed(BigDecimal fixed) {
        _fixed = fixed;
    } // -- setMinExclusive

    /**
     * Validates the given Object
     *
     * @param bd
     *            the BigDecimal to validate
     * @param context
     *            the ValidationContext
     * @throws ValidationException if the object fails validation.
     */
    public void validate(BigDecimal bd, ValidationContext context) throws ValidationException {
        if (_fixed != null && !bd.equals(_fixed)) {
            String err = bd + " is not equal to the fixed value of " + _fixed;
            throw new ValidationException(err);
        }

        if (_min != null) {
            if (bd.compareTo(_min) == -1) {
                String err = bd + " is less than the minimum allowable value of " + _min;
                throw new ValidationException(err);
            } else if ((bd.compareTo(_min) == 0) && (_hasMinExclusive)) {
                String err = bd + " cannot be equal to the minimum allowable value of " + _min;
                throw new ValidationException(err);
            }
        }

        if (_max != null) {
            if (bd.compareTo(_max) == 1) {
                String err = bd + " is greater than the maximum allowable value of " + _max;
                throw new ValidationException(err);
            } else if ((bd.compareTo(_max) == 0) && (_hasMaxExclusive)) {
                String err = bd + " cannot be equal to the maximum allowable value of " + _max;
                throw new ValidationException(err);
            }
        }

        // For digit counting, we are not supposed to count leading or trailing zeros
        BigDecimal clean = stripTrailingZeros(bd);

        if (_totalDigits != -1) {
            String temp = toStringForBigDecimal(clean);
            int length = temp.length();
            if (temp.indexOf('-') == 0) {
                --length;
            }
            if (temp.indexOf('.') != -1) {
                --length;
            }
            if (length > _totalDigits) {
                String err = bd
                        + " has too many significant digits, it must be less than or equal to "
                        + _totalDigits;
                throw new ValidationException(err);
            }
            temp = null;
        }

        if (_fractionDigits != -1 && clean.scale() > _fractionDigits) {
            String err = bd + " has too many significant fraction digits: it must be less than or equal to "
                        + _fractionDigits;
            throw new ValidationException(err);
        }

        if (hasPattern()) {
            super.validate(toStringForBigDecimal(bd), context);
        }
    } // -- validate

    /**
     * Because Sun broke API compatibility between Java 1.4 and Java 5, we have
     * to do this the hard way.
     *
     * @param bd  the BigDecimal to toString() on.
     * @return what <i>should be</i> toString() for BigDecimal
     * @see <a href="http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=6364896">
     *      This Sun bug report calling for better documentation of this change</a>
     */
    private String toStringForBigDecimal(BigDecimal bd) {
        if (bdMethodToPlainString != null) {
            try {
                // For Java 1.5 or later, use toPlainString() to get what we want
                return (String) bdMethodToPlainString.invoke(bd, (Object[]) null);
            } catch (IllegalAccessException e) {
                // Cannot occur, so just fall through to toString()
            } catch (InvocationTargetException e) {
                // Cannot occur, so just fall through to toString()
            }
        }

        // For Java 1.4.2 or earlier, use toString() to get what we want
        return bd.toString();
    }

    /**
     * Trims trailing zeros from the provided BigDecimal. Since BigDecimals are
     * immutable, the value passed in is not changed.
     * <p>
     * The JDK 5 API provides a method to do this, but earlier releases of Java
     * do not. Rather than reflectively use this method for early releases and
     * the API-provided one for releases after Java 5, we'll just use this
     * method for all Java releases.
     *
     * @param bd
     *            the BigDecimal to trim
     * @return a new BigDecimal with trailing zeros removed
     */
    private BigDecimal stripTrailingZeros(final BigDecimal bd) {
        BigDecimal ret = null;
        try {
            for (int i = bd.scale(); i >= 0; i--) {
                ret = bd.setScale(i);
            }
        } catch (ArithmeticException e) {
            // We've removed all trailing zeros
        }
        return (ret == null) ? bd : ret;
    }

    /**
     * Validates the given Object
     *
     * @param object
     *            the Object to validate
     * @throws ValidationException if the object fails validation.
     */
    public void validate(Object object) throws ValidationException {
        validate(object, (ValidationContext) null);
    } // -- validate

    /**
     * Validates the given Object
     *
     * @param object
     *            the Object to validate
     * @param context
     *            the ValidationContext
     * @throws ValidationException if the object fails validation.
     */
    public void validate(Object object, ValidationContext context) throws ValidationException {
        if (object == null) {
            String err = "decimalValidator cannot validate a null object.";
            throw new ValidationException(err);
        }

        BigDecimal value = null;
        if (object instanceof BigDecimal) {
            value = (BigDecimal) object;
        } else {
            try {
                value = new java.math.BigDecimal(object.toString());
            } catch (Exception ex) {
                String err = "Expecting a decimal, received instead: " + object.getClass().getName();
                throw new ValidationException(err);
            }
        }
        validate(value, context);
    } //-- validate

} //-- decimalValidator
