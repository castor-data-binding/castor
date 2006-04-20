/**
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

import org.exolab.castor.xml.*;

/**
 * The Float Validation class. This class handles validation
 * for the float type.
 *
 * @author <a href="mailto:blandin@intalio.com">Arnaud Blandin</a>
 * @version $Revision$ $Date$
 */
public class FloatValidator extends PatternValidator
    implements TypeValidator
{

    private boolean _isFixed = false;
    private float _fixed = 0;
    private boolean _isThereMinInclusive = false;
    private float _minInclusive = 0;
    private boolean _isThereMaxInclusive = false;
    private float _maxInclusive = 0;
    private boolean _isThereMinExclusive = false;
    private float _minExclusive = 0;
    private boolean _isThereMaxExclusive = false;
    private float _maxExclusive = 0;


    /**
     * Creates a new FloatValidator with no restrictions
    **/
    public FloatValidator() {
        super();
    } //-- FloatValidator



    /**
     * Sets the fixed value the float to validate must
     * be equal to.
     * @param fixed the fixed value
     */
    public void setFixed(float fixed) {
        _fixed = fixed;
        _isFixed = true;
    } //-- setMinExclusive

    /**
     * Sets the minimum value that floats validated with this
     * validator must be greater than
     * @param minValue the minimum value an float validated with this
     * validator must be greater than
     */
    public void setMinExclusive(float minValue) {
        _minExclusive = minValue;
        _isThereMinExclusive = true;
    } //-- setMinExclusive

    /**
     * Sets the minimum value that decimals validated with this
     * validator are allowed to be
     * @param minValue the minimum value an floatvalidated with this
     * validator may be
    **/
    public void setMinInclusive(float minValue) {
        _minInclusive = minValue;
        _isThereMinInclusive = true;
    } //-- setMinInclusive

    /**
     * Sets the maximum value that decimals validated with this
     * validator must be less than
     * @param maxValue the maximum value an floatvalidated with this
     * validator must be less than
    **/
    public void setMaxExclusive(float maxValue) {
        _maxExclusive = maxValue;
        _isThereMaxExclusive = true;
    } //-- setMaxExclusive

    /**
     * Sets the maximum value that decimals validated with this
     * validator are allowed to be
     * @param maxValue the maximum value an floatvalidated with this
     * validator may be
    **/
    public void setMaxInclusive(float maxValue) {
        _maxInclusive = maxValue;
        _isThereMaxInclusive = true;
    } //--setMaxInclusive


    /**
     * Sets the fixed value the float to validate must
     * be equal to.
     * @param fixed the fixed value
     */
    public void setFixed(Float fixed) {
        setFixed(fixed.floatValue());
    }
    /**
     * Sets the minimum value that decimals validated with this
     * validator must be greater than
     * @param minValue the minimum value an float validated with this
     * validator must be greater than
     */
    public void setMinExclusive(Float minValue) {
        setMinExclusive(minValue.floatValue());
    } //-- setMinExclusive

    /**
     * Sets the minimum value that decimals validated with this
     * validator are allowed to be
     * @param minValue the minimum value an float validated with this
     * validator may be
    **/
    public void setMinInclusive(Float minValue) {
        setMinInclusive(minValue.floatValue());
    } //-- setMinInclusive

    /**
     * Sets the maximum value that decimals validated with this
     * validator must be less than
     * @param maxValue the maximum value an float validated with this
     * validator must be less than
    **/
    public void setMaxExclusive(Float maxValue) {
        setMaxExclusive(maxValue.floatValue());
    } //-- setMaxExclusive

    /**
     * Sets the maximum value that decimals validated with this
     * validator are allowed to be
     * @param maxValue the maximum value an float validated with this
     * validator may be
    **/
    public void setMaxInclusive(Float maxValue) {
        setMaxInclusive(maxValue.floatValue());
    } //--setMaxInclusive

    public void validate(float d, ValidationContext context) 
        throws ValidationException 
    {

        if (_isFixed) {
            if (d != _fixed) {
                String err = d + " is not equal to the fixed value of ";
                err += _fixed;
                throw new ValidationException(err);
            }
        }

        if (_isThereMinInclusive) {
            if (d < _minInclusive ) {
                String err = d + " is less than the minimum allowable ";
                err += "value of " + _minInclusive;
                throw new ValidationException(err);
            }
        }

         if (_isThereMinExclusive) {
            if (d <=_minExclusive) {
                String err = d + " is less than the minimum allowable ";
                err += "value of " + _minExclusive;
                throw new ValidationException(err);
            }
        }

         if (_isThereMaxInclusive) {
            if ( d > _maxInclusive) {
                String err = d + " is greater than the maximum allowable ";
                err += "value of " + _maxInclusive;
                throw new ValidationException(err);
            }
        }

         if (_isThereMaxExclusive) {
            if ( d >= _maxExclusive) {
                String err = d + " is greater than the maximum allowable ";
                err += "value of " + _maxExclusive;
                throw new ValidationException(err);
            }
        }
        if (hasPattern())
            super.validate(Float.toString(d), context);

    } //-- validate

    /**
     * Validates the given Object
     *
     * @param object the Object to validate
     */
    public void validate(Object object) 
        throws ValidationException
    {
        validate(object, (ValidationContext)null);
    } //-- validate
    
    /**
     * Validates the given Object
     *
     * @param object the Object to validate
     * @param context the ValidationContext
     */
    public void validate(Object object, ValidationContext context) 
        throws ValidationException 
    {
        if (object == null) {
            String err = "FloatValidator cannot validate a null object.";
            throw new ValidationException(err);
        }

        float value = 0;
        try {
             value = new java.lang.Float(object.toString()).floatValue();
        }
        catch(Exception ex) {
            String err = "Expecting a float, received instead: ";
            err += object.getClass().getName();
            throw new ValidationException(err);
        }
        validate(value, context);
    } //-- validate

} //-- FloatValidator