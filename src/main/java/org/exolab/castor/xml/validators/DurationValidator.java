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
 * 10/26/2000   Arnaud Blandin   Created
 */
package org.exolab.castor.xml.validators;

import org.exolab.castor.xml.ValidationContext;
import org.exolab.castor.xml.ValidationException;
import org.exolab.castor.xml.TypeValidator;
import org.exolab.castor.types.Duration;

/**
 * The Duration Validation class. This class handles validation for the Castor
 * XML Schema duration type.
 *
 * @author <a href="mailto:visco@intalio.com">Keith Visco</a>
 * @author <a href="mailto:blandin@intalio.com">Arnaud Blandin</a>
 * @version $Revision$ $Date: 2003-03-03 02:57:21 -0700 (Mon, 03 Mar 2003) $
 */
public class DurationValidator extends PatternValidator implements TypeValidator {

    /** Maximum allowed value (Inclusive) for a valid instance.  (null if not set.) */
    private Duration _maxInclusive;
    /** Maximum allowed value (Exclusive) for a valid instance.  (null if not set.) */
    private Duration _maxExclusive;
    /** Minimum allowed value (Inclusive) for a valid instance.  (null if not set.) */
    private Duration _minInclusive;
    /** Minimum allowed value (Exclusive) for a valid instance.  (null if not set.) */
    private Duration _minExclusive;
    /** Fixed value.  If not null, a valid duration instance MUST have this value. */
    private Duration _fixed;

    /**
     * No-arg constructor.
     */
    public DurationValidator() {
        super();
    } //-- TimeDurationValidator

    /**
     * Clears the fixed value for this Duration.
     */
    public void clearFixed() {
        _fixed = null;
    } // -- clearFixed

    /**
     * Clears the maximum value for this Duration.
     */
    public void clearMax() {
        _maxInclusive = null;
        _maxExclusive = null;
    } // -- clearMax

    /**
     * Clears the minimum value for this Duration.
     */
    public void clearMin() {
        _minInclusive = null;
        _minExclusive = null;
    } // -- clearMin

    /**
     * Returns the configured fixed value for Duration validation. Returns null
     * if no fixed value has been configured.
     *
     * @return the fixed value to validate against.
     */
    public Duration getFixed() {
        return _fixed;
    } // -- getFixed

    /**
     * Returns the configured inclusive maximum value for Duration validation.
     * Returns null if no inclusive maximum has been configured.
     *
     * @return the inclusive maximum value to validate against.
     */
    public Duration getMaxInclusive() {
        return _maxInclusive;
    } // -- getMaxInclusive

    /**
     * Returns the configured exclusive maximum value for Duration validation.
     * Returns null if no exclusive maximum has been configured.
     *
     * @return the exclusive maximum value to validate against.
     */
    public Duration getMaxExclusive() {
        return _maxExclusive;
    } // -- getMaxInclusive

    /**
     * Returns the configured inclusive minimum value for Duration validation.
     * Returns null if no inclusive minimum has been configured.
     *
     * @return the inclusive minimum value to validate against.
     */
    public Duration getMinInclusive() {
        return _minInclusive;
    } // -- getMinInclusive

    /**
     * Returns the configured exclusive minimum value for Duration validation.
     * Returns null if no exclusive minimum has been configured.
     *
     * @return the exclusive minimum value to validate against.
     */
    public Duration getMinExclusive() {
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
     * Sets the fixed value for Duration validation.
     * <p>
     * NOTE: If maximum and/or minimum values have been set and the fixed value
     * is not within that max/min range, then no Duration will pass
     * validation. This is as according to the XML Schema spec.
     *
     * @param fixedValue
     *            the fixed value that a Duration validated with this
     *            validator must be equal to.
     */
    public void setFixed(final Duration fixedValue) {
        _fixed = fixedValue;
    }

    /**
     * Sets the minimum (exclusive) value for Duration validation.  To pass
     * validation, a Duration must be greater than this value.
     *
     * @param minValue
     *            the minimum (exclusive) value for Duration validation.
     */
    public void setMinExclusive(final Duration minValue) {
        _minExclusive = minValue;
        _minInclusive = null;
    } //-- setMinExclusive

    /**
     * Sets the minimum (inclusive) value for Duration validation.  To pass
     * validation, a Duration must be greater than or equal to this value.
     *
     * @param minValue
     *            the minimum (inclusive) value for Duration validation.
     */
    public void setMinInclusive(final Duration minValue) {
        _minInclusive = minValue;
        _minExclusive = null;
    } //-- setMinInclusive

    /**
     * Sets the maximum (exclusive) value for Duration validation.  To pass
     * validation, a Duration must be less than this value.
     *
     * @param maxValue
     *            the maximum (exclusive) value for Duration validation.
     */
    public void setMaxExclusive(final Duration maxValue) {
        _maxExclusive = maxValue;
        _maxInclusive = null;
    } //-- setMaxExclusive

    /**
     * Sets the maximum (inclusive) value for Duration validation.  To pass
     * validation, a Duration must be less than or equal to this value.
     *
     * @param maxValue
     *            the maximum (inclusive) value for Duration validation.
     */
    public void setMaxInclusive(final Duration maxValue) {
        _maxInclusive = maxValue;
        _maxExclusive = null;
    } //-- setMaxInclusive

    /**
     * Validate a duration instance.
     * @param duration the duration to validate
     * @throws ValidationException if the duration fails validation
     */
    public void validate(final Duration duration) throws ValidationException {
        validate(duration, (ValidationContext) null);
    }

    /**
     * Validates a duration instance.
     * @param duration the duration type to validate
     * @param context the ValidationContext
     * @throws ValidationException if the duration fails validation
     */
    public void validate(final Duration duration, final ValidationContext context)
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

        if (_fixed != null && !duration.equal(_fixed)) {
            String err = "Duration " + duration + " is not equal to the fixed value: " + _fixed;
            throw new ValidationException(err);
        }

        if (isThereMinInclusive && _minInclusive.isGreater(duration)) {
            String err = "Duration " + duration + " is less than the minimum allowed value: "
                    + _minInclusive;
            throw new ValidationException(err);
        }

        if (isThereMinExclusive && (_minExclusive.isGreater(duration)
                                    || duration.equals(_minExclusive))) {
             String err = "Duration " + duration
                     + " is less than or equal to the minimum exclusive value: " + _minExclusive;
             throw new ValidationException(err);
        }

        if (isThereMaxInclusive && duration.isGreater(_maxInclusive)) {
            String err = "Duration " + duration + " is greater than the maximum allowed value "
                    + _maxInclusive;
            throw new ValidationException(err);
        }

        if (isThereMaxExclusive && ((duration.isGreater(_maxExclusive))
                                    || duration.equals(_maxExclusive))) {
            String err = "Duration " + duration
                    + " is greater than or equal to the maximum exclusive value: " + _maxExclusive;
            throw new ValidationException(err);
        }

        if (hasPattern()) {
            super.validate(duration.toString(), context);
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
            String err = "durationValidator cannot validate a null object.";
            throw new ValidationException(err);
        }

        if (object instanceof String) {
            try {
                Duration duration = Duration.parseDuration((String) object);
                validate(duration, context);
                return;
            } catch (java.text.ParseException pe) {
                String err = "String provided fails to parse into a Duration: " + (String) object;
                throw new ValidationException(err, pe);
            }
        }

        Duration value = null;
        try {
            value = (Duration) object;
        } catch (Exception ex) {
            String err = "Expecting a duration, received instead: " + object.getClass().getName();
            throw new ValidationException(err);
        }

        validate(value, context);
    } //-- validate

}
