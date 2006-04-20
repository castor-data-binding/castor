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
 * Copyright 1999-2003 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id$
 */


package org.exolab.castor.xml.validators;

import org.exolab.castor.xml.*;

/**
 * The Integer Validation class. This class handles validation
 * for the integer type as well as all integer derived types
 * such as positive-integer and negative-integer
 *
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a>
 * @version $Revision$ $Date$
 */
public class IntegerValidator extends PatternValidator
    implements TypeValidator
{

    private boolean useMin   = false;
    private boolean useMax   = false;
    private boolean useFixed = false;

    private int min = 0;
    private int max = 0;
    private int  _totalDigits = -1;


    private int fixed = 0;

    /**
     * Creates a new IntegerValidator with no restrictions
    **/
    public IntegerValidator() {
        super();
    } //-- IntegerValidator

    /**
     * Clears the fixed value for this IntegerValidator
    **/
    public void clearFixed() {
        useFixed = false;
    } //-- clearFixed

    /**
     * Clears the maximum value for this IntegerValidator
    **/
    public void clearMax() {
        useMax = false;
    } //-- clearMax

    /**
     * Clears the minimum value for this IntegerValidator
    **/
    public void clearMin() {
        useMin = false;
    } //-- clearMin

    /**
     * Returns the fixed value that integers validated with this
     * validator must be equal to. A null value is returned
     * if no fixed value has been specified.
     *
     * @return the fixed value to validate against.
     */
    public Integer getFixed() {
        if (useFixed) {
            return new Integer(fixed);
        }
        return null;
    } //-- getFixed

    /**
     * Returns the maximum value that integers validated with this
     * validator must be equal to or less than. A null value 
     * is returned if no maximum value has been specified.
     *
     * @return the maximum inclusive value to validate against.
     */
    public Integer getMaxInclusive() {
        if (useMax) {
            return new Integer(max);
        }
        return null;
    } //-- getMaxInclusive
    
    /**
     * Returns the minimum value that integers validated with this
     * validator must be equal to or greater than. A null value 
     * is returned if no minimum value has been specified.
     *
     * @return the minimum inclusive value to validate against.
     */
    public Integer getMinInclusive() {
        if (useMin) {
            return new Integer(min);
        }
        return null;
    } //-- getMinInclusive

    /**
     * Returns the total number of digits that integers validated with this
     * validator must have. A null value 
     * is returned if no total number of digits has been specified.
     *
     * @return the total number of digits
     */
    public Integer getTotalDigits() {
        if (_totalDigits >= 0) {
            return new Integer(_totalDigits);
        }
        return null;
    } //-- getTotalDigits
    
    /**
     * Returns true if a fixed value, to validate against, has been
     * set.
     *
     * @return true if a fixed value has been set.
     */
    public boolean hasFixed() {
        return useFixed;
    } //-- hasFixed
    
    /**
     * Sets the fixed value that integers validated with this
     * validated must be equal to
     * @param fixedValue the fixed value an integer validated with
     * this validator must be equal to.
     * <BR>
     * NOTE: Using Fixed values takes preceedence over using max and mins,
     * and is really the same as setting both max-inclusive and
     * min-inclusive to the same value
    **/
    public void setFixed(int fixedValue) {
        useFixed = true;
        this.fixed = fixedValue;
    } //-- setFixed

    public void setFixed(Integer fixedValue) {
        useFixed = true;
        this.fixed = fixedValue.intValue();
    }
    /**
     * Sets the minimum value that integers validated with this
     * validator must be greater than
     * @param minValue the minimum value an integer validated with this
     * validator must be greater than
    **/
    public void setMinExclusive(int minValue) {
        useMin = true;
        min = minValue+1;
    } //-- setMinExclusive

    /**
     * Sets the minimum value that integers validated with this
     * validator are allowed to be
     * @param minValue the minimum value an integer validated with this
     * validator may be
    **/
    public void setMinInclusive(int minValue) {
        useMin = true;
        min = minValue;
    } //-- setMinInclusive

    /**
     * Sets the maximum value that integers validated with this
     * validator must be less than
     * @param maxValue the maximum value an integer validated with this
     * validator must be less than
    **/
    public void setMaxExclusive(int maxValue) {
        useMax = true;
        max = maxValue-1;
    } //-- setMaxExclusive

    /**
     * Sets the maximum value that integers validated with this
     * validator are allowed to be
     * @param maxValue the maximum value an integer validated with this
     * validator may be
    **/
    public void setMaxInclusive(int maxValue) {
        useMax = true;
        max = maxValue;
    } //-- setMaxInclusive

      /**
     * Sets the totalDigits facet for this Integer type.
     * @param totalDig the value of totalDigits (must be >0)
     */
     public void setTotalDigits(int totalDig) {
          if (totalDig <= 0)
              throw new IllegalArgumentException("IntegerValidator: the totalDigits facet must be positive");
          else _totalDigits = totalDig;
     }

    public void validate(int i, ValidationContext context)
        throws ValidationException
    {

        if (useFixed) {
            if (i != fixed) {
                String err = i + " is not equal to the fixed value of "
                    + fixed;
                throw new ValidationException(err);
            }
            return;
        }

        if (useMin) {
            if (i < min) {
                String err = i + " is less than the minimum allowable ";
                err += "value of " + min;
                throw new ValidationException(err);
            }
        }
        if (useMax) {
            if (i > max) {
                String err = i + " is greater than the maximum allowable ";
                err += "value of " + max;
                throw new ValidationException(err);
            }
        }

        if (_totalDigits != -1) {
            int length = Integer.toString(i).length();
            if ( length > _totalDigits ) {
                String err = i + " doesn't have the correct number of digits, it must be less than or equal to "+_totalDigits;
                throw new ValidationException(err);
            }
        }

        if (hasPattern())
            super.validate(Integer.toString(i), context);

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
            String err = "IntegerValidator cannot validate a null object.";
            throw new ValidationException(err);
        }

        int value = 0;
        try {
            value = ((Integer)object).intValue();
        }
        catch(Exception ex) {
            String err = "Expecting an Integer, received instead: ";
            err += object.getClass().getName();
            throw new ValidationException(err);
        }
        validate(value, context);
    } //-- validate

} //-- IntegerValidator