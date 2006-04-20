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
 * 10/31/2000   Arnaud Blandin   Created
 */


package org.exolab.castor.xml.validators;

import org.exolab.castor.xml.*;
import java.math.BigDecimal;

/**
 * The Decimal Validation class. This class handles validation
 * for the Decimal type.
 *
 * TODO : pattern, enumeration
 * @author <a href="mailto:blandin@intalio.com">Arnaud Blandin</a>
 * @version $Revision$ $Date$
 */
public class DecimalValidator implements TypeValidator
{

    private BigDecimal _fixed = null;
    private BigDecimal _min = null;
    private BigDecimal _max = null;
    private int _totalDigits = -1 ;
    private int _fractionDigits = -1;
    
    private boolean _hasMinExclusive = false;
    private boolean _hasMaxExclusive = false;
    
    /**
     * Creates a new DecimalValidator with no restrictions
    **/
    public DecimalValidator() {
        super();
    } //-- decimalValidator

    /**
     * Clears the maximum value for this DecimalValidator
    **/
    public void clearMax() {
        _max = null;
        _hasMaxExclusive = false;
    } //-- clearMax

    /**
     * Clears the minimum value for this DecimalValidator
    **/
    public void clearMin() {
        _min = null;
        _hasMinExclusive = false;
    } //-- clearMin

    /**
     * Returns the fixed value that decimals validated with this
     * validator must be equal to. A null value is returned
     * if no fixed value has been specified.
     *
     * @return the fixed value to validate against.
     */
    public BigDecimal getFixed() {
        return _fixed;
    } //-- getFixed

    /**
     * Returns the maximum value that decimals validated with this
     * validator must be equal to or less than. A null value 
     * is returned if no maximum value has been specified.
     *
     * @return the maximum inclusive value to validate against.
     */
    public BigDecimal getMaxInclusive() {
        return _max;
    } //-- getMaxInclusive
    
    /**
     * Returns the minimum value that decimals validated with this
     * validator must be equal to or greater than. A null value 
     * is returned if no minimum value has been specified.
     *
     * @return the minimum inclusive value to validate against.
     */
    public BigDecimal getMinInclusive() {
        return _min;
    } //-- getMinInclusive


    /**
     * Returns true if a fixed value, to validate against, has been
     * set.
     *
     * @return true if a fixed value has been set.
     */
    public boolean hasFixed() {
        return (_fixed != null);
    } //-- hasFixed

    /**
     * Sets the minimum value that decimals validated with this
     * validator must be greater than
     * @param minValue the minimum value an decimal validated with this
     * validator must be greater than
    **/
    public void setMinExclusive(BigDecimal minValue) {
        if (minValue == null)
            throw new IllegalArgumentException("argument 'minValue' must not be null.");
        _min = minValue;
        _hasMinExclusive = true;
    } //-- setMinExclusive

    /**
     * Sets the minimum value that decimals validated with this
     * validator are allowed to be
     * @param minValue the minimum value an decimal validated with this
     * validator may be
    **/
    public void setMinInclusive(BigDecimal minValue) {
        _min = minValue;
        _hasMinExclusive = true;
    } //-- setMinInclusive

    /**
     * Sets the maximum value that decimals validated with this
     * validator must be less than
     * @param maxValue the maximum value an decimal validated with this
     * validator must be less than
    **/
    public void setMaxExclusive(BigDecimal maxValue) {
        if (maxValue == null)
            throw new IllegalArgumentException("argument 'maxValue' must not be null.");
        _max = maxValue;
        _hasMaxExclusive = true;
   } //-- setMaxExclusive

    /**
     * Sets the maximum value that decimals validated with this
     * validator are allowed to be
     * @param maxValue the maximum value an decimal validated with this
     * validator may be
    **/
    public void setMaxInclusive(BigDecimal maxValue) {
        _max = maxValue;
        _hasMaxExclusive = false;
    } //-- setMaxInclusive

    /**
     * Sets the totalDigits facet for this decimal validator.
     * @param totalDig the value of totalDigits (must be >0)
     */
     public void setTotalDigits(int totalDig) {
          if (totalDig <= 0)
              throw new IllegalArgumentException("DecimalValidator: the totalDigits facet must be positive");
          else _totalDigits = totalDig;
     }

    /**
     * Sets the fractionDigits facet for this decimal validator.
     *
     * @param fractionDig the value of fractionDigits (must be >=0)
     */
     public void setFractionDigits(int fractionDig) {
          if (fractionDig < 0)
              throw new IllegalArgumentException("DecimalValidator: the fractionDigits facet must be positive");
          else _fractionDigits = fractionDig;
     }

    /**
     * Sets the fixed value the decimal to validate must
     * be equal to.
     *
     * @param fixed the fixed value
     */
    public void setFixed(BigDecimal fixed) {
        _fixed = fixed;
    } //-- setMinExclusive

    public void validate(BigDecimal bd) throws ValidationException {

        if (_fixed != null) {
            if (!bd.equals(_fixed)) {
                String err = bd + " is not equal to the fixed value of ";
                err += _fixed;
                throw new ValidationException(err);
            }
        }

        if (_min != null) {
            if (bd.compareTo(_min)==-1) {
                String err = bd + " is less than the minimum allowable ";
                err += "value of " + _min;
                throw new ValidationException(err);
            } else if ( (bd.compareTo(_min) == 0) && (_hasMinExclusive)) {
                String err = bd + " cannot be equal to the minimum allowable ";
                err += "value of " + _min;
                throw new ValidationException(err);
            }
        }

        if (_max != null) {
            if (bd .compareTo(_max)==1) {
                String err = bd + " is greater than the maximum allowable ";
                err += "value of " + _max;
                throw new ValidationException(err);           
            } else if ( (bd.compareTo(_max) == 0) && (_hasMaxExclusive)) {
                String err = bd + " cannot be equal to the maximum allowable ";
                err += "value of " + _max;
                throw new ValidationException(err);
            }
        }

        //we need to handle it by using a String
        if (_totalDigits != -1) {
            String temp = bd.toString();
            int length = (temp.indexOf('.') ==  -1)?temp.length():temp.length()-1;
            if (length > _totalDigits){
                String err = bd + " doesn't have the correct number of digits, it must be less than or equal to "+_totalDigits;
                throw new ValidationException(err);
            }
            temp = null;
        }
        if (_fractionDigits != -1) {

            if (bd.scale() > _fractionDigits){
                String err = bd + " doesn't have the correct number of digits in the fraction part: , it must be less than or equal to "+_fractionDigits;
                throw new ValidationException(err);
            }
        }

        //-- do the validation of pattern

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
            String err = "decimalValidator cannot validate a null object.";
            throw new ValidationException(err);
        }

        BigDecimal value = null;
        try {
             value = new java.math.BigDecimal(object.toString());
        }
        catch(Exception ex) {
            String err = "Expecting a decimal, received instead: ";
            err += object.getClass().getName();
            throw new ValidationException(err);
        }
        validate(value);
    } //-- validate

} //-- decimalValidator