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
 * Copyright 2001-2003 (C) Intalio, Inc. All Rights Reserved.
 *
 */
package org.exolab.castor.xml.validators;

import org.exolab.castor.xml.TypeValidator;
import org.exolab.castor.xml.ValidationContext;
import org.exolab.castor.xml.ValidationException;

/**
 * The Short Validation class. This class handles validation for the short type.
 *
 * @author <a href="mailto:visco@intalio.com">Keith Visco</a>
 * @author <a href="mailto:blandin@intalio.com">Arnaud Blandin</a>
 * @version $Revision$ $Date: 2003-03-03 02:57:21 -0700 (Mon, 03 Mar
 *          2003) $
 */
public class ShortValidator extends PatternValidator implements TypeValidator {

    private boolean useMin   = false;

    private boolean useMax   = false;

    private boolean useFixed = false;

    private short   min      = 0;

    private short   max      = 0;

    private int     _totalDigits = -1;

    private short   fixed    = 0;

    /**
     * Creates a new ShortValidator with no restrictions
     */
    public ShortValidator() {
        super();
    } // -- ShortValidator

    /**
     * Clears the fixed value for this ShortValidator
     */
    public void clearFixed() {
        useFixed = false;
    } // -- clearFixed

    /**
     * Clears the maximum value for this ShortValidator
     */
    public void clearMax() {
        useMax = false;
    } // -- clearMax

    /**
     * Clears the minimum value for this ShortValidator
     */
    public void clearMin() {
        useMin = false;
    } // -- clearMin

    /**
     * Returns the fixed value that shorts validated with this validator must be
     * equal to. A null value is returned if no fixed value has been specified.
     *
     * @return the fixed value to validate against.
     */
    public Short getFixed() {
        if (useFixed) {
            return new Short(fixed);
        }
        return null;
    } // -- getFixed

    /**
     * Returns the maximum value that shorts validated with this validator must
     * be equal to or less than. A null value is returned if no maximum value
     * has been specified.
     *
     * @return the maximum inclusive value to validate against.
     */
    public Short getMaxInclusive() {
        if (useMax) {
            return new Short(max);
        }
        return null;
    } // -- getMaxInclusive

    /**
     * Returns the minimum value that shorts validated with this validator must
     * be equal to or greater than. A null value is returned if no minimum value
     * has been specified.
     *
     * @return the minimum inclusive value to validate against.
     */
    public Short getMinInclusive() {
        if (useMin) {
            return new Short(min);
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
     * Sets the fixed value that shorts validated with this validated must be
     * equal to.
     * <p>
     * NOTE: Using Fixed values takes precedence over using max and mins, and is
     * really the same as setting both max-inclusive and min-inclusive to the
     * same value
     *
     * @param fixedValue
     *            the fixed value a short validated with this validator must be
     *            equal to.
     */
    public void setFixed(short fixedValue) {
        useFixed = true;
        this.fixed = fixedValue;
    } // -- setFixed

    /**
     * Sets the minimum value that shorts validated with this validator must be
     * greater than.
     *
     * @param minValue
     *            the minimum value a short validated with this validator must
     *            be greater than
     */
    public void setMinExclusive(short minValue) {
        useMin = true;
        min = (short) (minValue + 1);
    } // -- setMinExclusive

    /**
     * Sets the minimum value that shorts validated with this validator are
     * allowed to be
     *
     * @param minValue
     *            the minimum value a short validated with this validator may be
     */
    public void setMinInclusive(short minValue) {
        useMin = true;
        min = minValue;
    } // -- setMinInclusive

    /**
     * Sets the maximum value that shorts validated with this validator must be
     * less than
     *
     * @param maxValue
     *            the maximum value a short validated with this validator must
     *            be less than
     */
    public void setMaxExclusive(short maxValue) {
        useMax = true;
        max = (short) (maxValue - 1);
    } // -- setMaxExclusive

    /**
     * Sets the maximum value that shorts validated with this validator are
     * allowed to be
     *
     * @param maxValue
     *            the maximum value a short validated with this validator may be
     */
    public void setMaxInclusive(short maxValue) {
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
     * @param s
     *            the short to validate
     * @param context
     *            the ValidationContext
     * @throws ValidationException if the object fails validation.
     */
    public void validate(short s, ValidationContext context) throws ValidationException {
        if (useFixed && s != fixed) {
            String err = s + " is not equal to the fixed value of " + fixed;
            throw new ValidationException(err);
        }

        if (useMin && s < min) {
            String err = s + " is less than the minimum allowable value of " + min;
            throw new ValidationException(err);
        }

        if (useMax && s > max) {
            String err = s + " is greater than the maximum allowable value of " + max;
            throw new ValidationException(err);
        }

        if (_totalDigits != -1) {
            int length = Short.toString(s).length();
            if (s < 0) {
                length--;
            }
            if (length > _totalDigits) {
                String err = s
                        + " doesn't have the correct number of digits, it must be less than or equal to "
                        + _totalDigits;
                throw new ValidationException(err);
            }
        }

        if (hasPattern()) {
            super.validate(Short.toString(s), context);
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
            String err = "ShortValidator cannot validate a null object.";
            throw new ValidationException(err);
        }

        short value = 0;
        try {
            value = ((Short) object).shortValue();
        } catch (Exception ex) {
            String err = "Expecting a Short, received instead: "
                    + object.getClass().getName();
            throw new ValidationException(err);
        }
        validate(value, context);
    } //-- validate

} //-- ShortValidator
