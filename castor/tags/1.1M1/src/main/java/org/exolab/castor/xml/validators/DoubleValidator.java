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
 * 12/06/2000   Arnaud Blandin   Created
 */
package org.exolab.castor.xml.validators;

import org.exolab.castor.xml.TypeValidator;
import org.exolab.castor.xml.ValidationContext;
import org.exolab.castor.xml.ValidationException;

/**
 * The Double Validation class. This class handles validation for the double
 * type.
 *
 * @author <a href="mailto:blandin@intalio.com">Arnaud Blandin</a>
 * @version $Revision$ $Date: 2003-03-03 02:57:21 -0700 (Mon, 03 Mar 2003) $
 */
public class DoubleValidator extends PatternValidator implements TypeValidator {

    private boolean _isFixed             = false;

    private double  _fixed               = 0;

    private boolean _isThereMinInclusive = false;

    private double  _minInclusive        = 0;

    private boolean _isThereMaxInclusive = false;

    private double  _maxInclusive        = 0;

    private boolean _isThereMinExclusive = false;

    private double  _minExclusive        = 0;

    private boolean _isThereMaxExclusive = false;

    private double  _maxExclusive        = 0;

    /**
     * Creates a new DoubleValidator with no restrictions
     */
    public DoubleValidator() {
        super();
    } // -- doubleValidator

    /**
     * Sets the fixed value the double to validate must be equal to.
     *
     * @param fixed
     *            the fixed value
     */
    public void setFixed(double fixed) {
        _fixed = fixed;
        _isFixed = true;
    } // -- setMinExclusive

    /**
     * Sets the minimum value that decimals validated with this validator must
     * be greater than
     *
     * @param minValue
     *            the minimum value an doublevalidated with this validator must
     *            be greater than
     */
    public void setMinExclusive(double minValue) {
        _minExclusive = minValue;
        _isThereMinExclusive = true;
    } // -- setMinExclusive

    /**
     * Sets the minimum value that decimals validated with this validator are
     * allowed to be
     *
     * @param minValue
     *            the minimum value an doublevalidated with this validator may
     *            be
     */
    public void setMinInclusive(double minValue) {
        _minInclusive = minValue;
        _isThereMinInclusive = true;
    } // -- setMinInclusive

    /**
     * Sets the maximum value that decimals validated with this validator must
     * be less than
     *
     * @param maxValue
     *            the maximum value an doublevalidated with this validator must
     *            be less than
     */
    public void setMaxExclusive(double maxValue) {
        _maxExclusive = maxValue;
        _isThereMaxExclusive = true;
    } // -- setMaxExclusive

    /**
     * Sets the maximum value that decimals validated with this validator are
     * allowed to be
     *
     * @param maxValue
     *            the maximum value an doublevalidated with this validator may
     *            be
     */
    public void setMaxInclusive(double maxValue) {
        _maxInclusive = maxValue;
        _isThereMaxInclusive = true;
    } // --setMaxInclusive

    /**
     * Validates the given Object
     *
     * @param d
     *            the double to validate
     * @param context
     *            the ValidationContext
     * @throws ValidationException if the object fails validation.
     */
    public void validate(double d, ValidationContext context) throws ValidationException {
        if (_isFixed && d != _fixed) {
            String err = d + " is not equal to the fixed value of " + _fixed;
            throw new ValidationException(err);
        }

        if (_isThereMinInclusive && d < _minInclusive) {
            String err = d + " is less than the minimum allowable value of " + _minInclusive;
            throw new ValidationException(err);
        }

        if (_isThereMinExclusive && d <= _minExclusive) {
            String err = d + " is less than the minimum allowable value of " + _minExclusive;
            throw new ValidationException(err);
        }

        if (_isThereMaxInclusive && d > _maxInclusive) {
            String err = d + " is greater than the maximum allowable value of " + _maxInclusive;
            throw new ValidationException(err);
        }

        if (_isThereMaxExclusive && d >= _maxExclusive) {
            String err = d + " is greater than the maximum allowable value of " + _maxExclusive;
            throw new ValidationException(err);
        }

        if (hasPattern()) {
            super.validate(Double.toString(d), context);
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
            String err = "doubleValidator cannot validate a null object.";
            throw new ValidationException(err);
        }

        double value = 0;
        try {
            value = new java.lang.Double(object.toString()).doubleValue();
        } catch (Exception ex) {
            String err = "Expecting a double, received instead: ";
            err += object.getClass().getName();
            throw new ValidationException(err);
        }
        validate(value, context);
    } //-- validate

} //-- doubleValidator
