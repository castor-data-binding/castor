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
 * $Id: LongValidator.java 5951 2006-05-30 22:18:48Z bsnyder $
 */
package org.exolab.castor.xml.validators;

import java.math.BigInteger;

import org.exolab.castor.xml.TypeValidator;
import org.exolab.castor.xml.ValidationContext;
import org.exolab.castor.xml.ValidationException;

/**
 * The BigInteger Validation class. Handles validation for the
 * <code>java.math.BigInteger</code> type.
 *
 * @author <a href="mailto:werner DOT guttmann AT gmx DOT net">Werner Guttmann</a>
 * @version $Revision: 5951 $ $Date: 2003-03-03 02:57:21 -0700 (Mon, 03 Mar
 *          2003) $
 */
public class BigIntegerValidator extends PatternValidator implements TypeValidator {

    /** If true, we perform "minimum value" validation. */
    private boolean    _useMin       = false;
    /** If true, we perform "maximum value" validation. */
    private boolean    _useMax       = false;
    /** If true, we perform "fixed" validation. */
    private boolean    _useFixed     = false;
    /** Minimum value (inclusive) for this BigInteger.  (Not used unless _useMin == true.) */
    private BigInteger _min          = BigInteger.valueOf(0);
    /** Maximum value (inclusive) for this BigInteger.  (Not used unless _useMax == true.) */
    private BigInteger _max          = BigInteger.valueOf(0);
    /** Maximum number of digits in this BigInteger. (Not applied if < 0.) */
    private int        _totalDigits  = -1;
    /** Fixed value of this BigInteger. (Not used unless _useFixed == true.) */
    private BigInteger _fixed        = BigInteger.valueOf(0);

    /**
     * Creates a new BigIntegerValidator with no restrictions.
     */
    public BigIntegerValidator() {
        super();
    } // -- BigIntegerValidator

    /**
     * Clears the fixed value for this BigIntegerValidator.
     */
    public void clearFixed() {
        _useFixed = false;
    } // -- clearFixed

    /**
     * Clears the maximum value for this BigIntegerValidator.
     */
    public void clearMax() {
        _useMax = false;
    } // -- clearMax

    /**
     * Clears the minimum value for this BigIntegerValidator.
     */
    public void clearMin() {
        _useMin = false;
    } // -- clearMin

    /**
     * Returns the configured fixed value for BigInteger validation.  Returns
     * null if no fixed value has been configured.
     *
     * @return the fixed value to validate against.
     */
    public BigInteger getFixed() {
        if (_useFixed) {
            return _fixed;
        }
        return null;
    } // -- getFixed

    /**
     * Returns the configured maximum value for BigInteger validation.  Returns
     * null if no maximum has been configured.
     *
     * @return the maximum (inclusive) value to validate against.
     */
    public BigInteger getMaxInclusive() {
        if (_useMax) {
            return _max;
        }
        return null;
    } // -- getMaxInclusive

    /**
     * Returns the configured minimum value for BigInteger validation.  Returns
     * null if no minimum has been configured.
     *
     * @return the minimum (inclusive) value to validate against.
     */
    public BigInteger getMinInclusive() {
        if (_useMin) {
            return _min;
        }
        return null;
    } // -- getMinInclusive

    /**
     * Returns the configured maximum number of digits (inclusive) for
     * BigInteger validation. Returns null if no maximum number of digits has
     * been configured.
     *
     * @return the maximum number of digits to validate against.
     */
    public Integer getTotalDigits() {
        if (_totalDigits >= 0) {
            return new Integer(_totalDigits);
        }
        return null;
    } // -- getTotalDigits

    /**
     * Returns true if a fixed value to validate against has been set.
     *
     * @return true if a fixed value has been set.
     */
    public boolean hasFixed() {
        return _useFixed;
    } // -- hasFixed

    /**
     * Sets the fixed value for BigInteger validation.
     * <p>
     * NOTE: If maximum and/or minimum values have been set and the fixed value
     * is not within that max/min range, then no BigInteger will pass
     * validation. This is as according to the XML Schema spec.
     *
     * @param fixedValue
     *            the fixed value that a BigInteger validated with this
     *            validator must be equal to.
     */
    public void setFixed(final BigInteger fixedValue) {
        _useFixed = true;
        this._fixed = fixedValue;
    } // -- setFixed

    /**
     * Sets the minimum (exclusive) value for BigInteger validation.  To pass
     * validation, a BigInteger must be greater than this value.
     *
     * @param minValue
     *            the minimum (exclusive) value for BigInteger validation.
     */
    public void setMinExclusive(final BigInteger minValue) {
        _useMin = true;
        _min = minValue.add(BigInteger.valueOf(1));
    } // -- setMinExclusive

    /**
     * Sets the minimum (inclusive) value for BigInteger validation.  To pass
     * validation, a BigInteger must be greater than or equal to this value.
     *
     * @param minValue
     *            the minimum (inclusive) value for BigInteger validation.
     */
    public void setMinInclusive(final BigInteger minValue) {
        _useMin = true;
        _min = minValue;
    } // -- setMinInclusive

    /**
     * Sets the maximum (exclusive) value for BigInteger validation.  To pass
     * validation, a BigInteger must be less than this value.
     *
     * @param maxValue
     *            the maximum (exclusive) value for BigInteger validation.
     */
    public void setMaxExclusive(final BigInteger maxValue) {
        _useMax = true;
        _max = maxValue.subtract(BigInteger.valueOf(-1));
    } // -- setMaxExclusive

    /**
     * Sets the maximum (inclusive) value for BigInteger validation.  To pass
     * validation, a BigInteger must be less than or equal to this value.
     *
     * @param maxValue
     *            the maximum (inclusive) value for BigInteger validation.
     */
    public void setMaxInclusive(final BigInteger maxValue) {
        _useMax = true;
        _max = maxValue;
    } // -- setMaxInclusive

    /**
     * Sets the maximum number of digits for BigInteger validation. To pass
     * validation, a BigInteger must have this many digits or fewer. Leading
     * zeros are not counted.
     *
     * @param totalDig
     *            the maximum (inclusive) number of digits for BigInteger
     *            validation. (must be > 0)
     */
    public void setTotalDigits(final int totalDig) {
        if (totalDig <= 0) {
            throw new IllegalArgumentException(
                    "IntegerValidator: the totalDigits facet must be positive");
        }
        _totalDigits = totalDig;
    }

    /**
     * Validates the given Object.
     *
     * @param value
     *            the BigInteger to validate
     * @param context
     *            the ValidationContext
     * @throws ValidationException if the object fails validation.
     */
    public void validate(final BigInteger value, final ValidationContext context)
                                                    throws ValidationException {
        if (_useFixed && value != _fixed) {
            String err = "BigInteger " + value + " is not equal to the fixed value: " + _fixed;
            throw new ValidationException(err);
        }

        if (_useMin && value.compareTo(_min) == -1) {
            String err = "BigInteger " + value + " is less than the minimum allowed value: " + _min;
            throw new ValidationException(err);
        }

        if (_useMax && value.compareTo(_max) == 1) {
            String err = "BigInteger " + value + " is greater than the maximum allowed value: "
                    + _max;
            throw new ValidationException(err);
        }

        if (_totalDigits != -1) {
            int length = value.toString().length();
            if (value.compareTo(new BigInteger("0")) == -1) {
                length--;
            }
            if (length > _totalDigits) {
                String err = "BigInteger " + value + " has too many digits -- must have "
                        + _totalDigits + " digits or fewer.";
                throw new ValidationException(err);
            }
        }

        if (hasPattern()) {
            super.validate(value.toString(), context);
        }
    } // -- validate

    /**
     * Validates the given Object.
     *
     * @param object
     *            the Object to validate
     * @throws ValidationException
     *             if the object fails validation.
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
     * @throws ValidationException
     *             if the object fails validation.
     */
    public void validate(final Object object, final ValidationContext context)
                                                    throws ValidationException {
        if (object == null) {
            String err = "BigIntegerValidator cannot validate a null object.";
            throw new ValidationException(err);
        }

        BigInteger value = BigInteger.valueOf(0);
        try {
            value = (BigInteger) object;
        } catch (Exception ex) {
            String err = "Expecting a BigInteger, received instead: ";
            err += object.getClass().getName();
            throw new ValidationException(err);
        }
        validate(value, context);
    } // -- validate

} // -- BigIntegerValidator
