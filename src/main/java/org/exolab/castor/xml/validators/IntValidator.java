/*
 * Copyright 2006 Werner Guttmann
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.exolab.castor.xml.validators;

import org.exolab.castor.xml.TypeValidator;
import org.exolab.castor.xml.ValidationContext;
import org.exolab.castor.xml.ValidationException;

/**
 * The Int Validation class. This class handles validation for the primitive
 * <code>int</code> and <code>java.lang.Integer</code> types as well as all
 * derived types such as unsigned-short.
 *
 * @author <a href="mailto:werner DOT guttmann AT gmx DOT net">Werner Guttmann</a>
 * @version $Revision: 6571 $ $Date: 2006-04-25 15:08:23 -0600 (Tue, 25 Apr
 *          2006) $
 */
public class IntValidator extends PatternValidator implements TypeValidator {

    /** If true, we perform "minimum value" validation. */
    private boolean _useMin       = false;
    /** If true, we perform "maximum value" validation. */
    private boolean _useMax       = false;
    /** If true, we perform "fixed" validation. */
    private boolean _useFixed     = false;
    /** Minimum value (inclusive) for this int.  (Not used unless _useMin == true.) */
    private int     _min          = 0;
    /** Maximum value (inclusive) for this int.  (Not used unless _useMax == true.) */
    private int     _max          = 0;
    /** Maximum number of digits in this int. (Not applied if < 0.) */
    private int     _totalDigits = -1;
    /** Fixed value of this int. (Not used unless _useFixed == true.) */
    private int     _fixed        = 0;

    /**
     * Creates a new IntValidator with no restrictions.
     */
    public IntValidator() {
        super();
    } // -- IntegerValidator

    /**
     * Clears the fixed value for this IntValidator.
     */
    public void clearFixed() {
        _useFixed = false;
    } // -- clearFixed

    /**
     * Clears the maximum value for this IntValidator.
     */
    public void clearMax() {
        _useMax = false;
    } // -- clearMax

    /**
     * Clears the minimum value for this IntValidator.
     */
    public void clearMin() {
        _useMin = false;
    } // -- clearMin

    /**
     * Returns the configured fixed value for int validation. Returns null if no
     * fixed value has been configured.
     *
     * @return the fixed value to validate against.
     */
    public Integer getFixed() {
        if (_useFixed) {
            return new Integer(_fixed);
        }
        return null;
    } // -- getFixed

    /**
     * Returns the configured maximum value for int validation. Returns null if
     * no maximum has been configured.
     *
     * @return the maximum (inclusive) value to validate against.
     */
    public Integer getMaxInclusive() {
        if (_useMax) {
            return new Integer(_max);
        }
        return null;
    } // -- getMaxInclusive

    /**
     * Returns the configured minimum value for int validation. Returns null if
     * no minimum has been configured.
     *
     * @return the minimum inclusive value to validate against.
     */
    public Integer getMinInclusive() {
        if (_useMin) {
            return new Integer(_min);
        }
        return null;
    } // -- getMinInclusive

    /**
     * Returns the configured maximum number of digits (inclusive) for int
     * validation. Returns null if no maximum number of digits has been
     * configured.
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
     * Sets the fixed value for int validation.
     * <p>
     * NOTE: If maximum and/or minimum values have been set and the fixed value
     * is not within that max/min range, then no int will pass validation. This
     * is as according to the XML Schema spec.
     *
     * @param fixedValue
     *            the fixed value that a int validated with this validator must
     *            be equal to.
     */
    public void setFixed(final int fixedValue) {
        _useFixed = true;
        this._fixed = fixedValue;
    } // -- setFixed

    /**
     * Sets the fixed value for int validation.
     * <p>
     * NOTE: If maximum and/or minimum values have been set and the fixed value
     * is not within that max/min range, then no int will pass validation. This
     * is as according to the XML Schema spec.
     *
     * @param fixedValue
     *            the fixed value that a int validated with this validator must
     *            be equal to.
     */
    public void setFixed(final Integer fixedValue) {
        _useFixed = true;
        this._fixed = fixedValue.intValue();
    }

    /**
     * Sets the minimum (exclusive) value for int validation. To pass
     * validation, a int must be greater than this value.
     *
     * @param minValue
     *            the minimum (exclusive) value for int validation.
     */
    public void setMinExclusive(final int minValue) {
        _useMin = true;
        _min = minValue + 1;
    } // -- setMinExclusive

    /**
     * Sets the minimum (inclusive) value for int validation. To pass
     * validation, a int must be greater than or equal to this value.
     *
     * @param minValue
     *            the minimum (inclusive) value for int validation.
     */
    public void setMinInclusive(final int minValue) {
        _useMin = true;
        _min = minValue;
    } // -- setMinInclusive

    /**
     * Sets the maximum (exclusive) value for int validation. To pass
     * validation, a int must be less than this value.
     *
     * @param maxValue
     *            the maximum (exclusive) value for int validation.
     */
    public void setMaxExclusive(final int maxValue) {
        _useMax = true;
        _max = maxValue - 1;
    } // -- setMaxExclusive

    /**
     * Sets the maximum (inclusive) value for int validation. To pass
     * validation, a int must be less than or equal to this value.
     *
     * @param maxValue
     *            the maximum (inclusive) value for int validation.
     */
    public void setMaxInclusive(final int maxValue) {
        _useMax = true;
        _max = maxValue;
    } // -- setMaxInclusive

    /**
     * Sets the maximum number of digits for int validation. To pass validation,
     * a int must have this many digits or fewer. Leading zeros are not counted.
     *
     * @param totalDig
     *            the maximum (inclusive) number of digits for int validation.
     *            (must be > 0)
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
     * @param i
     *            the long to validate
     * @param context
     *            the ValidationContext
     * @throws ValidationException if the object fails validation.
     */
    public void validate(final int i, final ValidationContext context)
                                                    throws ValidationException {
        if (_useFixed && i != _fixed) {
            String err = "int " + i + " is not equal to the fixed value: " + _fixed;
            throw new ValidationException(err);
        }

        if (_useMin && i < _min) {
            String err = "int " + i + " is less than the minimum allowed value: " + _min;
            throw new ValidationException(err);
        }

        if (_useMax && i > _max) {
            String err = "int " + i + " is greater than the maximum allowed value: " + _max;
            throw new ValidationException(err);
        }

        if (_totalDigits != -1) {
            int length = Integer.toString(i).length();
            if (i < 0) {
                length--;
            }
            if (length > _totalDigits) {
                String err = "int " + i + " has too many digits -- must have "
                        + _totalDigits + " digits or fewer.";
                throw new ValidationException(err);
            }
        }

        if (hasPattern()) {
            super.validate(Integer.toString(i), context);
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
            String err = "IntValidator cannot validate a null object.";
            throw new ValidationException(err);
        }

        int value = 0;
        try {
            value = ((Integer) object).intValue();
        } catch (Exception ex) {
            String err = "Expecting an Integer, received instead an instance of: ";
            err += object.getClass().getName();
            throw new ValidationException(err);
        }
        validate(value, context);
    } //-- validate

} //-- IntegerValidator
