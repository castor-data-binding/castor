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
 * Copyright 1999-2003 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id$
 */
package org.exolab.castor.xml.validators;

import org.castor.xml.XMLProperties;
import org.exolab.castor.xml.TypeValidator;
import org.exolab.castor.xml.ValidationContext;
import org.exolab.castor.xml.ValidationException;

/**
 * The Integer Validation class. This class handles validation for the primitive
 * <code>long</code> and <code>java.lang.Long</code> types as well as all
 * xsd:integer-derived types such as positive-integer and negative-integer
 *
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a>
 * @version $Revision$ $Date: 2006-04-25 15:08:23 -0600 (Tue, 25 Apr
 *          2006) $
 */
public class IntegerValidator extends PatternValidator implements TypeValidator {

    /** If true, we perform "minimum value" validation. */
    private boolean _useMin       = false;
    /** If true, we perform "maximum value" validation. */
    private boolean _useMax       = false;
    /** If true, we perform "fixed" validation. */
    private boolean _useFixed     = false;
    /** Minimum value (inclusive) for this long.  (Not used unless _useMin == true.) */
    private long    _min          = 0;
    /** Maximum value (inclusive) for this long.  (Not used unless _useMax == true.) */
    private long    _max          = 0;
    /** Maximum number of digits in this long. (Not applied if < 0.) */
    private int     _totalDigits = -1;
    /** Fixed value of this long. (Not used unless _useFixed == true.) */
    private long    _fixed        = 0;

    /**
     * Creates a new IntegerValidator with no restrictions.
     */
    public IntegerValidator() {
        super();
    }

    /**
     * Clears the fixed value for this IntegerValidator.
     */
    public void clearFixed() {
        _useFixed = false;
    }

    /**
     * Clears the maximum value for this IntegerValidator.
     */
    public void clearMax() {
        _useMax = false;
    }

    /**
     * Clears the minimum value for this IntegerValidator.
     */
    public void clearMin() {
        _useMin = false;
    }

    /**
     * Returns the configured fixed value for Integer validation.  Returns
     * null if no fixed value has been configured.
     *
     * @return the fixed value to validate against.
     */
    public Long getFixed() {
        if (_useFixed) {
            return new Long(_fixed);
        }
        return null;
    }

    /**
     * Returns the configured maximum value for xsd:integer validation. Returns
     * null if no maximum has been configured.
     *
     * @return the maximum (inclusive) value to validate against.
     */
    public Long getMaxInclusive() {
        if (_useMax) {
            return new Long(_max);
        }
        return null;
    }

    /**
     * Returns the configured minimum value for xsd:integer validation. Returns
     * null if no minimum has been configured.
     *
     * @return the minimum (inclusive) value to validate against.
     */
    public Long getMinInclusive() {
        if (_useMin) {
            return new Long(_min);
        }
        return null;
    }

    /**
     * Returns the configured maximum number of digits (inclusive) for
     * xsd:integer validation. Returns null if no maximum number of digits has
     * been configured.
     *
     * @return the maximum number of digits to validate against.
     */
    public Integer getTotalDigits() {
        if (_totalDigits >= 0) {
            return new Integer(_totalDigits);
        }
        return null;
    }

    /**
     * Returns true if a fixed value to validate against has been set.
     *
     * @return true if a fixed value has been set.
     */
    public boolean hasFixed() {
        return _useFixed;
    }

    /**
     * Sets the fixed value that integers validated with this validated must be
     * equal to.
     * <p>
     * NOTE: Using Fixed values takes preceedence over using max and mins, and
     * is really the same as setting both max-inclusive and min-inclusive to the
     * same value
     *
     * @param fixedValue
     *            the fixed value an integer validated with this validator must
     *            be equal to.
     */
    public void setFixed(final long fixedValue) {
        _useFixed = true;
        this._fixed = fixedValue;
    }

    /**
     * Sets the fixed value that integers validated with this validated must be
     * equal to.
     * <p>
     * NOTE: Using Fixed values takes preceedence over using max and mins, and
     * is really the same as setting both max-inclusive and min-inclusive to the
     * same value
     * 
     * Added for backward compatibility with old &lt;xs:integer&gt; implementation.
     *
     * @param fixedValue
     *            the fixed value an integer validated with this validator must
     *            be equal to.
     */
    public void setFixed(final int fixedValue) {
        _useFixed = true;
        this._fixed = fixedValue;
    }

    /**
     * Sets the fixed value for xsd:Integer validation.
     * <p>
     * NOTE: If maximum and/or minimum values have been set and the fixed value
     * is not within that max/min range, then no xsd:Integer will pass
     * validation. This is as according to the XML Schema spec.
     *
     * @param fixedValue
     *            the fixed value that a xsd:Integer validated with this
     *            validator must be equal to.
     */
    public void setFixed(final Long fixedValue) {
        _useFixed = true;
        this._fixed = fixedValue.intValue();
    }

    /**
     * Sets the minimum (exclusive) value for xsd:integer validation.  To pass
     * validation, an xsd:Integer must be greater than this value.
     *
     * @param minValue
     *            the minimum (exclusive) value for xsd:Integer validation.
     */
    public void setMinExclusive(final long minValue) {
        _useMin = true;
        _min = minValue + 1;
    }

    /**
     * Sets the minimum (exclusive) value for xsd:integer validation.  To pass
     * validation, an xsd:Integer must be greater than this value.
     * 
     * Added for backward compatibility with old &lt;xs:integer&gt; implementation.
     *
     * @param minValue
     *            the minimum (exclusive) value for xsd:Integer validation.
     */
    public void setMinExclusive(final int minValue) {
        _useMin = true;
        _min = minValue + 1;
    }

    /**
     * Sets the minimum (inclusive) value for xsd:integer validation.  To pass
     * validation, an xsd:integer must be greater than or equal to this value.
     *
     * @param minValue
     *            the minimum (inclusive) value for xsd:integer validation.
     */
    public void setMinInclusive(final long minValue) {
        _useMin = true;
        _min = minValue;
    }

    /**
     * Sets the minimum (inclusive) value for xsd:integer validation.  To pass
     * validation, an xsd:integer must be greater than or equal to this value.
     *
     * Added for backward compatibility with old &lt;xs:integer&gt; implementation.
     *
     * @param minValue
     *            the minimum (inclusive) value for xsd:integer validation.
     */
    public void setMinInclusive(final int minValue) {
        _useMin = true;
        _min = minValue;
    }
    
    /**
     * Sets the maximum (exclusive) value for xsd:integer validation.  To pass
     * validation, a xsd:integer must be less than this value.
     *
     * @param maxValue
     *            the maximum (exclusive) value for xsd:integer validation.
     */
    public void setMaxExclusive(final long maxValue) {
        _useMax = true;
        _max = maxValue - 1;
    }

    /**
     * Sets the maximum (exclusive) value for xsd:integer validation.  To pass
     * validation, a xsd:integer must be less than this value.
     *
     * Added for backward compatibility with old &lt;xs:integer&gt; implementation.
     *
     * @param maxValue
     *            the maximum (exclusive) value for xsd:integer validation.
     */
    public void setMaxExclusive(final int maxValue) {
        _useMax = true;
        _max = maxValue - 1;
    }


    /**
     * Sets the maximum (inclusive) value for xsd:integer validation.  To pass
     * validation, a xsd:integer must be less than or equal to this value.
     *
     * @param maxValue
     *            the maximum (inclusive) value for xsd:integer validation.
     */
    public void setMaxInclusive(final long maxValue) {
        _useMax = true;
        _max = maxValue;
    }

    /**
     * Sets the maximum (inclusive) value for xsd:integer validation.  To pass
     * validation, a xsd:integer must be less than or equal to this value.
     *
     * Added for backward compatibility with old &lt;xs:integer&gt; implementation.
     *
     * @param maxValue
     *            the maximum (inclusive) value for xsd:integer validation.
     */
    public void setMaxInclusive(final int maxValue) {
        _useMax = true;
        _max = maxValue;
    }

    /**
     * Sets the maximum number of digits for xsd:integer validation. To pass
     * validation, a xsd:integer must have this many digits or fewer. Leading
     * zeros are not counted.
     *
     * @param totalDig
     *            the maximum (inclusive) number of digits for xsd:integer
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
     * @param i
     *            the long to validate
     * @param context
     *            the ValidationContext
     * @throws ValidationException if the object fails validation.
     */
    public void validate(final long i, final ValidationContext context)
                                                    throws ValidationException {
        if (_useFixed && i != _fixed) {
            String err = "long " + i + " is not equal to the fixed value: " + _fixed;
            throw new ValidationException(err);
        }

        if (_useMin && i < _min) {
            String err = "long " + i + " is less than the minimum allowed value: " + _min;
            throw new ValidationException(err);
        }

        if (_useMax && i > _max) {
            String err = "long " + i + " is greater than the maximum allowed value: " + _max;
            throw new ValidationException(err);
        }

        if (_totalDigits != -1) {
            int length = Long.toString(i).length();
            if (i < 0) {
                length--;
            }
            if (length > _totalDigits) {
                String err = "long " + i + " has too many digits -- must have "
                        + _totalDigits + " digits or fewer.";
                throw new ValidationException(err);
            }
        }

        if (hasPattern()) {
            super.validate(Long.toString(i), context);
        }
    }

    /**
     * Validates the given Object.
     *
     * @param object
     *            the Object to validate
     * @throws ValidationException if the object fails validation.
     */
    public void validate(final Object object) throws ValidationException {
        validate(object, (ValidationContext) null);
    }

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
            String err = "IntegerValidator cannot validate a null object.";
            throw new ValidationException(err);
        }

        long value = 0;
        try {
            value = ((Long) object).longValue();
        } catch (Exception ex) {
            String lenientProperty = context.getInternalContext()
                .getStringProperty(XMLProperties.LENIENT_INTEGER_VALIDATION);
            if (Boolean.valueOf(lenientProperty).booleanValue()) {
                try {
                    value = ((Integer) object).longValue();
                } catch (Exception e) {
                    String err = "Expecting a Long/Integer, received instead: ";
                    err += object.getClass().getName();
                    throw new ValidationException(err);
                }
            } else {
                String err = "Expecting an Long, received instead: ";
                err += object.getClass().getName();
                throw new ValidationException(err);
            }
        }
        validate(value, context);
    }

}
