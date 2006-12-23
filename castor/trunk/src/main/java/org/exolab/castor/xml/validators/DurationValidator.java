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

public class DurationValidator extends PatternValidator implements TypeValidator {

    /** Maximum allowed value (Inclusive) for a valid instance. */
    private Duration _maxInclusive;
    /** Maximum allowed value (Exclusive) for a valid instance. */
    private Duration _maxExclusive;
    /** Minimum allowed value (Inclusive) for a valid instance. */
    private Duration _minInclusive;
    /** Minimum allowed value (Exclusive) for a valid instance. */
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
     * Sets the maximum exclusive value that this Duration can hold.
     * @param max the maximum exclusive value this Duration can be
     * @see #setMaxInclusive
     */
    public void setMaxExclusive(Duration max) {
        _maxExclusive = max;
        _maxInclusive = null;
    } //-- setMaxExclusive

    /**
     * Sets the maximum inclusive value that this Duration can hold.
     * @param max the maximum inclusive value this Duration can be
     * @see #setMaxExclusive
     */
    public void setMaxInclusive(Duration max) {
        _maxInclusive = max;
        _maxExclusive = null;
    } //-- setMaxInclusive

    /**
     * Sets the minimum exclusive value that this Duration can hold.
     * @param min the minimum exclusive value this Duration can be
     * @see #setMinInclusive
     */
    public void setMinExclusive(Duration min) {
        _minExclusive = min;
        _minInclusive = null;
    } //-- setMinExclusive

    /**
     * Sets the minimum inclusive value that this Duration can hold.
     * @param min the minimum inclusive value this Duration can be
     * @see #setMinExclusive
     */
    public void setMinInclusive(Duration min) {
        _minInclusive = min;
        _minExclusive = null;
    } //-- setMinInclusive

    /**
     * Sets the fixed value that this Duration must equal.
     * @param fixed the fixed value that this Duration must equal.
     */
    public void setFixed(Duration fixed) {
        _fixed = fixed;
    }

    /**
     * Validate a duration instance
     * @param duration the duration to validate
     * @throws ValidationException if the duration fails validation
     */
    public void validate(Duration duration) throws ValidationException {
        validate(duration, (ValidationContext)null);
    }

    /**
     * Validates a duration instance.
     * @param duration the duration type to validate
     * @param context the ValidationContext
     * @throws ValidationException if the duration fails validation
     */
    public void validate(Duration duration, ValidationContext context) throws ValidationException {
        boolean isThereMinInclusive = (_minInclusive != null);
        boolean isThereMinExclusive = (_minExclusive != null);
        boolean isThereMaxInclusive = (_maxInclusive != null);
        boolean isThereMaxExclusive = (_maxExclusive != null);

        if (isThereMinExclusive && isThereMinInclusive) {
            throw new ValidationException("both minInclusive and minExclusive are defined");
        }

        if (isThereMaxExclusive && isThereMaxInclusive) {
            throw new ValidationException("both maxInclusive and maxExclusive are defined");
        }

        if (isThereMinInclusive && _minInclusive.isGreater(duration)) {
            String err = duration + " is less than the minimum allowable value of " + _minInclusive;
            throw new ValidationException(err);
        }

        if (isThereMinExclusive && (_minExclusive.isGreater(duration) || duration.equals(_minExclusive))) {
             String err = duration + " is less than the minimum allowable value of " + _minExclusive;
             throw new ValidationException(err);
        }

        if (isThereMaxInclusive && duration.isGreater(_maxInclusive)) {
            String err = duration + " is greater than the maximum allowable value of " + _maxInclusive;
            throw new ValidationException(err);
        }

        if (isThereMaxExclusive && ((duration.isGreater(_maxExclusive)) || duration.equals(_maxExclusive))) {
            String err = duration + " is greater than the maximum allowable value of " + _maxExclusive;
            throw new ValidationException(err);
        }

        if (_fixed != null && !duration.equal(_fixed)) {
            String err = duration + " must be equal to the fixed value: " + _fixed;
            throw new ValidationException(err);
        }

        if (hasPattern()) {
            super.validate(duration.toString(), context);
        }
    } //-- validate

    /**
     * Validates the given Object
     *
     * @param object the Object to validate
     * @throws ValidationException if the object fails validation
     */
    public void validate(Object object) throws ValidationException {
        validate(object, (ValidationContext)null);
    } //-- validate

    /**
     * Validates the given Object.
     *
     * @param object the Object to validate
     * @param context the ValidationContext
     * @throws ValidationException if the object fails validation
     */
    public void validate(Object object, ValidationContext context) throws ValidationException {
        if (object == null) {
            String err = "durationValidator cannot validate a null object.";
            throw new ValidationException(err);
        }

        if (object instanceof String) {
            try {
                Duration duration = Duration.parseDuration((String)object);
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
        } catch(Exception ex) {
            String err = "Expecting a duration, received instead: " + object.getClass().getName();
            throw new ValidationException(err);
        }

        validate(value, context);
    } //-- validate

}
