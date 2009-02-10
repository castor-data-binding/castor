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
 * The Double Validation class. This class handles validation for the primitive
 * <code>double</code> and <code>java.lang.Double</code> types.
 *
 * @author <a href="mailto:blandin@intalio.com">Arnaud Blandin</a>
 * @version $Revision$ $Date: 2003-03-03 02:57:21 -0700 (Mon, 03 Mar 2003) $
 */
public class DoubleValidator extends PatternValidator implements TypeValidator {

    /** If true, we perform "minimum inclusive value" validation. */
    private boolean _useMinInclusive  = false;
    /** If true, we perform "minimum exclusive value" validation. */
    private boolean _useMinExclusive  = false;
    /** If true, we perform "maximum inclusive value" validation. */
    private boolean _useMaxInclusive  = false;
    /** If true, we perform "maximum exclusive value" validation. */
    private boolean _useMaxExclusive  = false;
    /** If true, we perform "fixed" validation. */
    private boolean _useFixed            = false;
    /** Minimum value (inclusive) for this double.  (Not used unless _useMinInclusive == true.) */
    private double  _minInclusive        = 0;
    /** Minimum value (exclusive) for this double.  (Not used unless _useMinExclusive == true.) */
    private double  _minExclusive        = 0;
    /** Maximum value (inclusive) for this double.  (Not used unless _useMaxInclusive == true.) */
    private double  _maxInclusive        = 0;
    /** Maximum value (exclusive) for this double.  (Not used unless _useMaxExclusive == true.) */
    private double  _maxExclusive        = 0;
    /** Fixed value of this double. (Not used unless _isFixed == true.) */
    private double  _fixed               = 0;

    /**
     * Creates a new DoubleValidator with no restrictions.
     */
    public DoubleValidator() {
        super();
    } // -- doubleValidator

    /**
     * Clears the fixed value for this DoubleValidator.
     */
    public void clearFixed() {
        _useFixed = false;
    } // -- clearFixed

    /**
     * Clears the maximum value for this DoubleValidator.
     */
    public void clearMax() {
        _useMaxExclusive = false;
        _useMaxInclusive = false;
    } // -- clearMax

    /**
     * Clears the minimum value for this DoubleValidator.
     */
    public void clearMin() {
        _useMinExclusive = false;
        _useMinInclusive = false;
    } // -- clearMin

    /**
     * Returns the configured fixed value for double validation. Returns null if
     * no fixed value has been configured.
     *
     * @return the fixed value to validate against.
     */
    public Double getFixed() {
        if (_useFixed) {
            return new Double(_fixed);
        }
        return null;
    } // -- getFixed

    /**
     * Returns the configured inclusive maximum value for double validation.
     * Returns null if no inclusive maximum has been configured.
     *
     * @return the inclusive maximum value to validate against.
     */
    public Double getMaxInclusive() {
        if (_useMaxInclusive) {
            return new Double(_maxInclusive);
        }
        return null;
    } // -- getMaxInclusive

    /**
     * Returns the configured exclusive maximum value for double validation.
     * Returns null if no exclusive maximum has been configured.
     *
     * @return the exclusive maximum value to validate against.
     */
    public Double getMaxExclusive() {
        if (_useMaxExclusive) {
            return new Double(_maxExclusive);
        }
        return null;
    } // -- getMaxInclusive

    /**
     * Returns the configured inclusive minimum value for double validation.
     * Returns null if no inclusive minimum has been configured.
     *
     * @return the inclusive minimum value to validate against.
     */
    public Double getMinInclusive() {
        if (_useMinInclusive) {
            return new Double(_minInclusive);
        }
        return null;
    } // -- getMinInclusive

    /**
     * Returns the configured exclusive minimum value for double validation.
     * Returns null if no exclusive minimum has been configured.
     *
     * @return the exclusive minimum value to validate against.
     */
    public Double getMinExclusive() {
        if (_useMinExclusive) {
            return new Double(_minExclusive);
        }
        return null;
    } // -- getMinInclusive

    /**
     * Returns true if a fixed value to validate against has been set.
     *
     * @return true if a fixed value has been set.
     */
    public boolean hasFixed() {
        return _useFixed;
    } // -- hasFixed

    /**
     * Sets the fixed value for double validation.
     * <p>
     * NOTE: If maximum and/or minimum values have been set and the fixed value
     * is not within that max/min range, then no double will pass validation.
     * This is as according to the XML Schema spec.
     *
     * @param fixedValue
     *            the fixed value that a double validated with this validator
     *            must be equal to.
     */
    public void setFixed(final double fixedValue) {
        _fixed = fixedValue;
        _useFixed = true;
    } // -- setMinExclusive

    /**
     * Sets the minimum (exclusive) value for double validation. To pass
     * validation, a double must be greater than this value.
     *
     * @param minValue
     *            the minimum (exclusive) value for double validation.
     */
    public void setMinExclusive(final double minValue) {
        _minExclusive = minValue;
        _useMinExclusive = true;
    } // -- setMinExclusive

    /**
     * Sets the minimum (inclusive) value for double validation. To pass
     * validation, a double must be greater than or equal to this value.
     *
     * @param minValue
     *            the minimum (inclusive) value for double validation.
     */
    public void setMinInclusive(final double minValue) {
        _minInclusive = minValue;
        _useMinInclusive = true;
    } // -- setMinInclusive

    /**
     * Sets the maximum (exclusive) value for double validation.  To pass
     * validation, a double must be less than this value.
     *
     * @param maxValue
     *            the maximum (exclusive) value for double validation.
     */
    public void setMaxExclusive(final double maxValue) {
        _maxExclusive = maxValue;
        _useMaxExclusive = true;
    } // -- setMaxExclusive

    /**
     * Sets the maximum (inclusive) value for double validation.  To pass
     * validation, a double must be less than or equal to this value.
     *
     * @param maxValue
     *            the maximum (inclusive) value for double validation.
     */
    public void setMaxInclusive(final double maxValue) {
        _maxInclusive = maxValue;
        _useMaxInclusive = true;
    } // --setMaxInclusive

    /**
     * Validates the given Object.
     *
     * @param d
     *            the double to validate
     * @param context
     *            the ValidationContext
     * @throws ValidationException if the object fails validation.
     */
    public void validate(final double d, final ValidationContext context)
                                                    throws ValidationException {
        if (_useFixed && d != _fixed) {
            String err = "double " + d + " is not equal to the fixed value: " + _fixed;
            throw new ValidationException(err);
        }

        if (_useMinInclusive && d < _minInclusive) {
            String err = "double " + d + " is less than the minimum allowed value: "
                    + _minInclusive;
            throw new ValidationException(err);
        }

        if (_useMinExclusive && d <= _minExclusive) {
            String err = "double " + d
                    + " is less than or equal to the maximum exclusive value: " + _minExclusive;
            throw new ValidationException(err);
        }

        if (_useMaxInclusive && d > _maxInclusive) {
            String err = "double " + d + " is greater than the maximum allowed value: "
                    + _maxInclusive;
            throw new ValidationException(err);
        }

        if (_useMaxExclusive && d >= _maxExclusive) {
            String err = "double " + d
                    + " is greater than or equal to the maximum exclusive value: " + _maxExclusive;
            throw new ValidationException(err);
        }

        if (hasPattern()) {
            super.validate(Double.toString(d), context);
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
