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
 * The Decimal Validation class. This class handles validation for the
 * <code>BigDecimal</code> type.
 *
 * @author <a href="mailto:blandin@intalio.com">Arnaud Blandin</a>
 * @author <a href="mailto:edward.kuns@aspect.com">Edward Kuns</a>
 * @version $Revision$ $Date: 2006-04-25 15:08:23 -0600 (Tue, 25 Apr
 *          2006) $
 */
public class DecimalValidator extends PatternValidator implements TypeValidator {

    /** Reference to the Method BigDecimal.toPlainString(), only in JDK 5 or later. */
    private static Method _bdMethodToPlainString = null;

    static {
        try {
            _bdMethodToPlainString = BigDecimal.class.getMethod("toPlainString", (Class[]) null);
        } catch (NoSuchMethodException e) {
            // If it does not exist, we're in Java 1.4.2 or earlier
        }
    }

    /** Fixed value of this short. (Not used if null.) */
    private BigDecimal _fixed           = null;
    /** Minimum value (inclusive or exclusive) for this BigDecimal.  (Not used if null.) */
    private BigDecimal _min             = null;
    /** Maximum value (inclusive or exclusive) for this BigDecimal.  (Not used if null.) */
    private BigDecimal _max             = null;
    /** Maximum number of significant digits in this BigDecimal. (Not applied if < 0.) */
    private int        _totalDigits     = -1;
    /** Maximum number of fractional digits in this BigDecimal. (Not applied if < 0.) */
    private int        _fractionDigits  = -1;
    /** If true, the minimum value is an <b>exclusive</b> value. */
    private boolean    _hasMinExclusive = false;
    /** If true, the maximum value is an <b>exclusive</b> value. */
    private boolean    _hasMaxExclusive = false;

    /**
     * Creates a new DecimalValidator with no restrictions.
     */
    public DecimalValidator() {
        super();
    } // -- decimalValidator

    /**
     * Clears the fixed value for this BigIntegerValidator.
     */
    public void clearFixed() {
        _fixed = null;
    } // -- clearFixed

    /**
     * Clears the maximum value for this DecimalValidator.
     */
    public void clearMax() {
        _max = null;
        _hasMaxExclusive = false;
    } // -- clearMax

    /**
     * Clears the minimum value for this DecimalValidator.
     */
    public void clearMin() {
        _min = null;
        _hasMinExclusive = false;
    } // -- clearMin

    /**
     * Returns the configured fixed value for BigDecimal validation.  Returns
     * null if no fixed value has been configured.
     *
     * @return the fixed value to validate against.
     */
    public BigDecimal getFixed() {
        return _fixed;
    } // -- getFixed

    /**
     * Returns the configured inclusive maximum value for BigDecimal validation.
     * Returns null if no inclusive maximum has been configured.
     *
     * @return the maximum inclusive value to validate against.
     */
    public BigDecimal getMaxInclusive() {
        return (_hasMaxExclusive) ? null : _max;
    } // -- getMaxInclusive

    /**
     * Returns the configured exclusive maximum value for BigDecimal validation.
     * Returns null if no exclusive maximum has been configured.
     *
     * @return the maximum exclusive value to validate against.
     */
    public BigDecimal getMaxExclusive() {
        return (_hasMaxExclusive) ? _max : null;
    } // -- getMaxInclusive

    /**
     * Returns the configured inclusive minimum value for BigDecimal validation.
     * Returns null if no inclusive minimum has been configured.
     *
     * @return the minimum inclusive value to validate against.
     */
    public BigDecimal getMinInclusive() {
        return (_hasMinExclusive) ? null : _min;
    } // -- getMinInclusive

    /**
     * Returns the configured exclusive minimum value for BigDecimal validation.
     * Returns null if no exclusive minimum has been configured.
     *
     * @return the minimum exclusive value to validate against.
     */
    public BigDecimal getMinExclusive() {
        return (_hasMinExclusive) ? _min : null;
    } // -- getMinInclusive

    /**
     * Returns true if a fixed value to validate against has been set.
     *
     * @return true if a fixed value has been set.
     */
    public boolean hasFixed() {
        return (_fixed != null);
    } // -- hasFixed

    /**
     * Sets the fixed value for BigDecimal validation.
     * <p>
     * NOTE: If maximum and/or minimum values have been set and the fixed value
     * is not within that max/min range, then no BigDecimal will pass
     * validation. This is as according to the XML Schema spec.
     *
     * @param fixedValue
     *            the fixed value that a BigDecimal validated with this
     *            validator must be equal to.
     */
    public void setFixed(final BigDecimal fixedValue) {
        _fixed = fixedValue;
    } // -- setMinExclusive

    /**
     * Sets the minimum (exclusive) value for BigDecimal validation.  To pass
     * validation, a BigDecimal must be greater than this value.
     *
     * @param minValue
     *            the minimum (exclusive) value for BigDecimal validation.
     */
    public void setMinExclusive(final BigDecimal minValue) {
        if (minValue == null) {
            throw new IllegalArgumentException("argument 'minValue' must not be null.");
        }
        _min = minValue;
        _hasMinExclusive = true;
    } // -- setMinExclusive

    /**
     * Sets the minimum (inclusive) value for BigDecimal validation.  To pass
     * validation, a BigDecimal must be greater than or equal to this value.
     *
     * @param minValue
     *            the minimum (inclusive) value for BigDecimal validation.
     */
    public void setMinInclusive(final BigDecimal minValue) {
        _min = minValue;
        _hasMinExclusive = false;
    } // -- setMinInclusive

    /**
     * Sets the maximum (exclusive) value for BigDecimal validation.  To pass
     * validation, a BigDecimal must be less than this value.
     *
     * @param maxValue
     *            the maximum (exclusive) value for BigDecimal validation.
     */
    public void setMaxExclusive(final BigDecimal maxValue) {
        if (maxValue == null) {
            throw new IllegalArgumentException("argument 'maxValue' must not be null.");
        }
        _max = maxValue;
        _hasMaxExclusive = true;
    } // -- setMaxExclusive

    /**
     * Sets the maximum (inclusive) value for BigDecimal validation.  To pass
     * validation, a BigDecimal must be less than or equal to this value.
     *
     * @param maxValue
     *            the maximum (inclusive) value for BigDecimal validation.
     */
    public void setMaxInclusive(final BigDecimal maxValue) {
        _max = maxValue;
        _hasMaxExclusive = false;
    } // -- setMaxInclusive

    /**
     * Sets the maximum number of digits for BigDecimal validation. To pass
     * validation, a BigDecimal must have this many digits or fewer. Leading
     * zeros are not counted.  Trailing zeros after the decimal point are not
     * counted.
     *
     * @param totalDig
     *            the maximum (inclusive) number of digits for BigDecimal
     *            validation. (must be > 0)
     */
    public void setTotalDigits(final int totalDig) {
        if (totalDig <= 0) {
            throw new IllegalArgumentException(
                    "DecimalValidator: the totalDigits facet must be positive");
        }
        _totalDigits = totalDig;
    }

    /**
     * Sets the maximum number of fraction digits for BigDecimal validation. To
     * pass validation, a BigDecimal must have this many digits or fewer
     * following the decimal point. Trailing zeros after the decimal point are
     * not counted.
     *
     * @param fractionDig
     *            the maximum (inclusive) number of fraction digits for
     *            BigDecimal validation. (must be > 0)
     */
    public void setFractionDigits(final int fractionDig) {
        if (fractionDig < 0) {
            throw new IllegalArgumentException(
                    "DecimalValidator: the fractionDigits facet must be positive");
        }
        _fractionDigits = fractionDig;
    }

    /**
     * Validates the given Object.
     *
     * @param bd
     *            the BigDecimal to validate
     * @param context
     *            the ValidationContext
     * @throws ValidationException if the object fails validation.
     */
    public void validate(final BigDecimal bd, final ValidationContext context)
                                                    throws ValidationException {
        if (_fixed != null && !bd.equals(_fixed)) {
            String err = "BigDecimal " + bd + " is not equal to the fixed value: " + _fixed;
            throw new ValidationException(err);
        }

        if (_min != null) {
            if (bd.compareTo(_min) == -1) {
                String err = "BigDecimal " + bd + " is less than the minimum allowed value: "
                        + _min;
                throw new ValidationException(err);
            } else if ((bd.compareTo(_min) == 0) && (_hasMinExclusive)) {
                String err = "BigDecimal " + bd
                        + " cannot be equal to the minimum exclusive value: " + _min;
                throw new ValidationException(err);
            }
        }

        if (_max != null) {
            if (bd.compareTo(_max) == 1) {
                String err = "BigDecimal " + bd + " is greater than the maximum allowed value: "
                        + _max;
                throw new ValidationException(err);
            } else if ((bd.compareTo(_max) == 0) && (_hasMaxExclusive)) {
                String err = "BigDecimal " + bd
                        + " cannot be equal to the maximum exclusive value: " + _max;
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
                String err = "BigDecimal " + bd + " has too many significant digits -- must have "
                        + _totalDigits + " or fewer";
                throw new ValidationException(err);
            }
            temp = null;
        }

        if (_fractionDigits != -1 && clean.scale() > _fractionDigits) {
            String err = "BigDecimal " + bd + " has too many fraction digits -- must have "
                    + _fractionDigits + " fraction digits or fewer";
            throw new ValidationException(err);
        }

        if (hasPattern()) {
            super.validate(toStringForBigDecimal(bd), context);
        }
    } // -- validate

    /**
     * Validates the given Object.
     *
     * @param object
     *            the Object to validate
     * @throws ValidationException if the object fails validation.
     */
    public void validate(final Object object) throws ValidationException {
        validate(object, (ValidationContext) null);
    } // -- validate

    /**
     * Validates the given Object.
     *
     * @param object
     *            the Object to validate
     * @param context
     *            the ValidationContext
     * @throws ValidationException if the object fails validation.
     */
    public void validate(final Object object, final ValidationContext context)
                                                    throws ValidationException {
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
                String err = "Expecting a decimal, received instead: "
                        + object.getClass().getName();
                throw new ValidationException(err);
            }
        }
        validate(value, context);
    } //-- validate

    /**
     * Because Sun broke API compatibility between Java 1.4 and Java 5, we have
     * to do this the hard way.
     *
     * @param bd  the BigDecimal to toString() on.
     * @return what <i>should be</i> toString() for BigDecimal
     * @see <a href="http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=6364896">
     *      This Sun bug report calling for better documentation of this change</a>
     */
    private String toStringForBigDecimal(final BigDecimal bd) {
        if (_bdMethodToPlainString != null) {
            try {
                // For Java 1.5 or later, use toPlainString() to get what we want
                return (String) _bdMethodToPlainString.invoke(bd, (Object[]) null);
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

} //-- decimalValidator
