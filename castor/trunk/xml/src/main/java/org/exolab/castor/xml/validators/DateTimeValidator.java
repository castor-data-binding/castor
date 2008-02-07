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
 * $Id$
 * Date         Author           Changes
 * 05/30/2001   Arnaud Blandin   Created
 */
package org.exolab.castor.xml.validators;

import org.exolab.castor.types.DateTime;
import org.exolab.castor.types.DateTimeBase;

import org.exolab.castor.xml.ValidationContext;
import org.exolab.castor.xml.ValidationException;
import org.exolab.castor.xml.TypeValidator;

/**
 * The Date/Time Validation class. This class handles validation for all
 * XML Schema date & time types.
 *
 * @version $Revision$ $Date:  $
 */
public class DateTimeValidator extends PatternValidator implements TypeValidator {
    /** Maximum allowed value (Inclusive) for a valid instance. */
    private DateTimeBase _maxInclusive;
    /** Maximum allowed value (Exclusive) for a valid instance. */
    private DateTimeBase _maxExclusive;
    /** Minimum allowed value (Inclusive) for a valid instance. */
    private DateTimeBase _minInclusive;
    /** Minimum allowed value (Exclusive) for a valid instance. */
    private DateTimeBase _minExclusive;
    /** Fixed value.  If not null, a valid DateTime instance MUST have this value. */
    private DateTimeBase _fixed;

    /**
     * No-arg constructor.
     */
    public DateTimeValidator() {
        super();
    } //-- DateTimeValidator

    /**
     * Clears the fixed value for this DateTimeValidator.
     */
    public void clearFixed() {
        _fixed = null;
    } // -- clearFixed

    /**
     * Clears the maximum value for this DateTimeValidator.
     */
    public void clearMax() {
        _maxInclusive = null;
        _maxExclusive = null;
    } // -- clearMax

    /**
     * Clears the minimum value for this DateTimeValidator.
     */
    public void clearMin() {
        _minInclusive = null;
        _minExclusive = null;
    } // -- clearMin

    /**
     * Returns the configured fixed value for date/time validation.  Returns
     * null if no fixed value has been configured.
     *
     * @return the fixed value to validate against.
     */
    public DateTimeBase getFixed() {
        return _fixed;
    } // -- getFixed

    /**
     * Returns the configured inclusive maximum value for date/time validation.
     * Returns null if no inclusive maximum has been configured.
     *
     * @return the maximum (inclusive) value to validate against.
     */
    public DateTimeBase getMaxInclusive() {
        return _maxInclusive;
    } // -- getMaxInclusive

    /**
     * Returns the configured exclusive maximum value for date/time validation.
     * Returns null if no exclusive maximum has been configured.
     *
     * @return the maximum (exclusive) value to validate against.
     */
    public DateTimeBase getMaxExclusive() {
        return _maxExclusive;
    } // -- getMaxInclusive

    /**
     * Returns the configured inclusive minimum value for date/time validation.
     * Returns null if no inclusive minimum has been configured.
     *
     * @return the minimum inclusive value to validate against.
     */
    public DateTimeBase getMinInclusive() {
        return _minInclusive;
    } // -- getMinInclusive

    /**
     * Returns the configured exclusive minimum value for date/time validation.
     * Returns null if no exclusive minimum has been configured.
     *
     * @return the minimum exclusive value to validate against.
     */
    public DateTimeBase getMinExclusive() {
        return _minExclusive;
    } // -- getMinInclusive

    /**
     * Returns true if a fixed value to validate against has been set.
     *
     * @return true if a fixed value has been set.
     */
    public boolean hasFixed() {
        return _fixed != null;
    } // -- hasFixed

    /**
     * Sets the fixed value for date/time validation.
     * <p>
     * NOTE: If maximum and/or minimum values have been set and the fixed value
     * is not within that max/min range, then no date/time will pass validation.
     * This is as according to the XML Schema spec.
     *
     * @param fixedValue
     *            the fixed value that a date/time validated with this validator
     *            must be equal to.
     */
    public void setFixed(final DateTimeBase fixedValue) {
        _fixed = fixedValue;
    }

    /**
     * Sets the minimum (exclusive) value for date/time validation.  To pass
     * validation, a date/time must be greater than this value.
     *
     * @param minValue
     *            the minimum (exclusive) value for date/time validation.
     */
    public void setMinExclusive(final DateTimeBase minValue) {
        _minExclusive = minValue;
        _minInclusive = null;
    } //-- setMinExclusive

    /**
     * Sets the minimum (inclusive) value for date/time validation.  To pass
     * validation, a date/time must be greater than or equal to this value.
     *
     * @param minValue
     *            the minimum (inclusive) value for date/time validation.
     */
    public void setMinInclusive(final DateTimeBase minValue) {
        _minExclusive = null;
        _minInclusive = minValue;
    } //-- setMinInclusive

    /**
     * Sets the maximum (exclusive) value for date/time validation.  To pass
     * validation, a date/time must be less than this value.
     *
     * @param maxValue
     *            the maximum (exclusive) value for date/time validation.
     */
    public void setMaxExclusive(final DateTimeBase maxValue) {
        _maxExclusive = maxValue;
        _maxInclusive = null;
    } //-- setMaxExclusive

    /**
     * Sets the maximum (inclusive) value for date/time validation.  To pass
     * validation, a date/time must be less than or equal to this value.
     *
     * @param maxValue
     *            the maximum (inclusive) value for date/time validation.
     */
    public void setMaxInclusive(final DateTimeBase maxValue) {
        _maxExclusive = null;
        _maxInclusive = maxValue;
    } //-- setMaxInclusive

    /**
     * Validates a Date/Time instance.
     * @param dateTime the date/time type to validate
     * @throws ValidationException if the DateTime fails validation
     */
    public void validate(final DateTimeBase dateTime) throws ValidationException {
        validate(dateTime, (ValidationContext) null);
    }

    /**
     * Validates a Date/Time instance.
     * @param dateTime the date/time type to validate
     * @param context the ValidationContext
     * @throws ValidationException if the DateTime fails validation
     */
    public void validate(final DateTimeBase dateTime, final ValidationContext context)
                                                    throws ValidationException {
        boolean isThereMinInclusive = (_minInclusive != null);
        boolean isThereMinExclusive = (_minExclusive != null);
        boolean isThereMaxInclusive = (_maxInclusive != null);
        boolean isThereMaxExclusive = (_maxExclusive != null);

        if (isThereMinExclusive && isThereMinInclusive) {
            throw new ValidationException("Both minInclusive and minExclusive are defined");
        }

        if (isThereMaxExclusive && isThereMaxInclusive) {
            throw new ValidationException("Both maxInclusive and maxExclusive are defined");
        }

        if (_fixed != null) {
            int comparison = dateTime.compareTo(_fixed);
            if (comparison == DateTimeBase.INDETERMINATE) {
                String err = dateTime.getClass().getName() + " " + dateTime
                        + " comparison to the fixed value " + _fixed
                        + " is indeterminate";
                throw new ValidationException(err);
            } else if (comparison != DateTimeBase.EQUALS) {
                String err = dateTime.getClass().getName() + " " + dateTime
                        + " is not equal to the fixed value: " + _fixed;
                throw new ValidationException(err);
            }
        }

        if (isThereMinInclusive && dateTime.compareTo(_minInclusive) != DateTimeBase.GREATER_THAN
                && !dateTime.equals(_minInclusive)) {
            String err = dateTime.getClass().getName() + " " + dateTime
                    + " is less than the minimum allowed value: " + _minInclusive;
            throw new ValidationException(err);
        }

        if (isThereMinExclusive && dateTime.compareTo(_minExclusive) != DateTimeBase.GREATER_THAN) {
            String err =  dateTime.getClass().getName() + " " + dateTime
                    + " is less than or equal to the minimum (exclusive) value: " + _minExclusive;
            throw new ValidationException(err);
        }

        if (isThereMaxInclusive && dateTime.compareTo(_maxInclusive) != DateTimeBase.LESS_THAN
                && !dateTime.equals(_maxInclusive)) {
            String err = dateTime.getClass().getName() + " " + dateTime
                    + " is greater than the maximum allowed value: " + _maxInclusive;
            throw new ValidationException(err);
        }

        if (isThereMaxExclusive && dateTime.compareTo(_maxExclusive) != DateTimeBase.LESS_THAN) {
            String err =  dateTime.getClass().getName() + " " + dateTime
                    + " is greater than or equal to the maximum (exclusive) value: "
                    + _maxExclusive;
            throw new ValidationException(err);
        }

        if (hasPattern()) {
            super.validate(dateTime.toString(), context);
        }
    } //-- validate

    /**
     * Validates the given Object.
     *
     * @param object the Object to validate
     * @throws ValidationException if the object fails validation
     */
    public void validate(final Object object) throws ValidationException {
        validate(object, (ValidationContext) null);
    } //-- validate

    /**
     * Validates the given Object.
     *
     * @param object the Object to validate
     * @param context the ValidationContext
     * @throws ValidationException if the object fails validation
     */
    public void validate(final Object object, final ValidationContext context)
                                                    throws ValidationException {
        if (object == null) {
            String err = "DateTimeValidator cannot validate a null object.";
            throw new ValidationException(err);
        }

        if (object instanceof String) {
            try {
                DateTime dateTime = new DateTime((String) object);
                validate(dateTime, context);
                return;
            } catch (java.text.ParseException pe) {
                String err = "String provided fails to parse into a DateTime: " + (String) object;
                throw new ValidationException(err, pe);
            }
        }

        DateTimeBase value = null;

        try {
            value = (DateTimeBase) object;
        } catch (Exception ex) {
            String err = ex.toString() + "\nExpecting a DateTime, received instead: "
                         + object.getClass().getName();
            throw new ValidationException(err);
        }

        validate(value, context);
    } //-- validate

}
