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
 * The Int Validation class. This class handles validation for the 'int'
 * type as well as all derived types such as unsigned-short
 *
 * @author <a href="mailto:werner DOT guttmann AT gmx DOT net">Werner Guttmann</a>
 * @version $Revision: 6571 $ $Date: 2006-04-25 15:08:23 -0600 (Tue, 25 Apr 2006) $
 */
public class IntValidator 
    extends PatternValidator 
    implements TypeValidator {

    private boolean useMin       = false;

    private boolean useMax       = false;

    private boolean useFixed     = false;

    private int    min          = 0;

    private int    max          = 0;

    private int     _totalDigits = -1;

    private int    fixed        = 0;

    /**
     * Creates a new IntValidator with no restrictions
     */
    public IntValidator() {
        super();
    } // -- IntegerValidator

    /**
     * Clears the fixed value for this IntValidator
     */
    public void clearFixed() {
        useFixed = false;
    } // -- clearFixed

    /**
     * Clears the maximum value for this IntValidator
     */
    public void clearMax() {
        useMax = false;
    } // -- clearMax

    /**
     * Clears the minimum value for this IntValidator
     */
    public void clearMin() {
        useMin = false;
    } // -- clearMin

    /**
     * Returns the fixed value that ints validated with this validator must
     * be equal to. A null value is returned if no fixed value has been
     * specified.
     *
     * @return the fixed value to validate against.
     */
    public Integer getFixed() {
        if (useFixed) {
            return new Integer(fixed);
        }
        return null;
    } // -- getFixed

    /**
     * Returns the maximum value that ints validated with this validator
     * must be equal to or less than. A null value is returned if no maximum
     * value has been specified.
     *
     * @return the maximum inclusive value to validate against.
     */
    public Integer getMaxInclusive() {
        if (useMax) {
            return new Integer(max);
        }
        return null;
    } // -- getMaxInclusive

    /**
     * Returns the minimum value that ints validated with this validator
     * must be equal to or greater than. A null value is returned if no minimum
     * value has been specified.
     *
     * @return the minimum inclusive value to validate against.
     */
    public Integer getMinInclusive() {
        if (useMin) {
            return new Integer(min);
        }
        return null;
    } // -- getMinInclusive

    /**
     * Returns the total number of digits that integers validated with this
     * validator must have. A null value is returned if no total number of
     * digits has been specified.
     *
     * @return the total number of digits
     */
    public Integer getTotalDigits() {
        if (_totalDigits >= 0) {
            return new Integer(_totalDigits);
        }
        return null;
    } // -- getTotalDigits

    /**
     * Returns true if a fixed value, to validate against, has been set.
     *
     * @return true if a fixed value has been set.
     */
    public boolean hasFixed() {
        return useFixed;
    } // -- hasFixed

    /**
     * Sets the fixed value that ints validated with this validated must be
     * equal to
     *
     * @param fixedValue
     *            the fixed value an integer validated with this validator must
     *            be equal to. <BR>
     *            NOTE: Using Fixed values takes preceedence over using max and
     *            mins, and is really the same as setting both max-inclusive and
     *            min-inclusive to the same value
     */
    public void setFixed(int fixedValue) {
        useFixed = true;
        this.fixed = fixedValue;
    } // -- setFixed

    public void setFixed(Integer fixedValue) {
        useFixed = true;
        this.fixed = fixedValue.intValue();
    }

    /**
     * Sets the minimum value that int validated with this validator must
     * be greater than
     *
     * @param minValue
     *            the minimum value an integer validated with this validator
     *            must be greater than
     */
    public void setMinExclusive(int minValue) {
        useMin = true;
        min = minValue + 1;
    } // -- setMinExclusive

    /**
     * Sets the minimum value that ints validated with this validator are
     * allowed to be
     *
     * @param minValue
     *            the minimum value an integer validated with this validator may
     *            be
     */
    public void setMinInclusive(int minValue) {
        useMin = true;
        min = minValue;
    } // -- setMinInclusive

    /**
     * Sets the maximum value that integers validated with this validator must
     * be less than
     *
     * @param maxValue
     *            the maximum value an integer validated with this validator
     *            must be less than
     */
    public void setMaxExclusive(int maxValue) {
        useMax = true;
        max = maxValue - 1;
    } // -- setMaxExclusive

    /**
     * Sets the maximum value that integers validated with this validator are
     * allowed to be
     *
     * @param maxValue
     *            the maximum value an integer validated with this validator may
     *            be
     */
    public void setMaxInclusive(int maxValue) {
        useMax = true;
        max = maxValue;
    } // -- setMaxInclusive

    /**
     * Sets the totalDigits facet for this Integer type.
     *
     * @param totalDig
     *            the value of totalDigits (must be >0)
     */
    public void setTotalDigits(int totalDig) {
        if (totalDig <= 0) {
            throw new IllegalArgumentException(
                    "IntegerValidator: the totalDigits facet must be positive");
        }
        _totalDigits = totalDig;
    }

    /**
     * Validates the given Object
     *
     * @param i
     *            the long to validate
     * @param context
     *            the ValidationContext
     * @throws ValidationException if the object fails validation.
     */
    public void validate(int i, ValidationContext context) throws ValidationException {
        if (useFixed && i != fixed) {
            String err = i + " is not equal to the fixed value of " + fixed;
            throw new ValidationException(err);
        }

        if (useMin && i < min) {
            String err = i + " is less than the minimum allowable value of " + min;
            throw new ValidationException(err);
        }

        if (useMax && i > max) {
            String err = i + " is greater than the maximum allowable value of " + max;
            throw new ValidationException(err);
        }

        if (_totalDigits != -1) {
            int length = Integer.toString(i).length();
            if (i < 0) {
                length--;
            }
            if (length > _totalDigits) {
                String err = i
                        + " doesn't have the correct number of digits, it must be less than or equal to "
                        + _totalDigits;
                throw new ValidationException(err);
            }
        }

        if (hasPattern()) {
            super.validate(Integer.toString(i), context);
        }
    } // -- validate

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
