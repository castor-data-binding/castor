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
 */


package org.exolab.castor.xml.validators;

import org.exolab.castor.xml.*;

/**
 * The Integer Validation class. This class handles validation
 * for the integer type as well as all integer derived types
 * such as positive-integer and negative-integer
 *
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a>
 * @version $Revision$ $Date: 2003-03-03 02:57:21 -0700 (Mon, 03 Mar 2003) $
 */
public class LongValidator extends PatternValidator
    implements TypeValidator
{

    private boolean useMin   = false;
    private boolean useMax   = false;
    private boolean useFixed = false;

    private long min = 0;
    private long max = 0;

    private long fixed = 0;

    /**
     * Creates a new LongValidator with no restrictions
    **/
    public LongValidator() {
        super();
    } //-- LongValidator

    /**
     * Clears the fixed value for this LongValidator
    **/
    public void clearFixed() {
        useFixed = false;
    } //-- clearFixed

    /**
     * Clears the maximum value for this LongValidator
    **/
    public void clearMax() {
        useMax = false;
    } //-- clearMax

    /**
     * Clears the minimum value for this LongValidator
    **/
    public void clearMin() {
        useMin = false;
    } //-- clearMin
    /**
     * Returns the fixed value that longs validated with this
     * validator must be equal to. A null value is returned
     * if no fixed value has been specified.
     *
     * @return the fixed value to validate against.
     */
    public Long getFixed() {
        if (useFixed) {
            return new Long(fixed);
        }
        return null;
    } //-- getFixed

    /**
     * Returns the maximum value that longs validated with this
     * validator must be equal to or less than. A null value 
     * is returned if no maximum value has been specified.
     *
     * @return the maximum inclusive value to validate against.
     */
    public Long getMaxInclusive() {
        if (useMax) {
            return new Long(max);
        }
        return null;
    } //-- getMaxInclusive
    
    /**
     * Returns the minimum value that longs validated with this
     * validator must be equal to or greater than. A null value 
     * is returned if no minimum value has been specified.
     *
     * @return the minimum inclusive value to validate against.
     */
    public Long getMinInclusive() {
        if (useMin) {
            return new Long(min);
        }
        return null;
    } //-- getMinInclusive

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
     * @param fixedValue the fixed value that a long validated with
     * this validator must be equal to.
     * <BR>
     * NOTE: Using Fixed values takes preceedence over using max and mins,
     * and is really the same as setting both max-inclusive and
     * min-inclusive to the same value
    **/
    public void setFixed(long fixedValue) {
        useFixed = true;
        this.fixed = fixedValue;
    } //-- setFixed

    /**
     * Sets the minimum value that longs validated with this
     * validator must be greater than
     * @param minValue the minimum value that a long validated with this
     * validator must be greater than
    **/
    public void setMinExclusive(long minValue) {
        useMin = true;
        min = minValue+1;
    } //-- setMinExclusive

    /**
     * Sets the minimum value that longs validated with this
     * validator are allowed to be
     * @param minValue the minimum value that a long validated with this
     * validator may be
    **/
    public void setMinInclusive(long minValue) {
        useMin = true;
        min = minValue;
    } //-- setMinInclusive

    /**
     * Sets the maximum value that longs validated with this
     * validator must be less than
     * @param maxValue the maximum value that a long validated
     * with this validator must be less than
    **/
    public void setMaxExclusive(long maxValue) {
        useMax = true;
        max = maxValue-1;
    } //-- setMaxExclusive

    /**
     * Sets the maximum value that longs validated with this
     * validator are allowed to be
     * @param maxValue the maximum value that a long validated
     * with this validator may be
    **/
    public void setMaxInclusive(long maxValue) {
        useMax = true;
        max = maxValue;
    } //-- setMaxInclusive

    public void validate(long value, ValidationContext context)
        throws ValidationException
    {

        if (useFixed) {
            if (value != fixed) {
                String err = value + " is not equal to the fixed value of "
                    + fixed;
                throw new ValidationException(err);
            }
            return;
        }

        if (useMin) {
            if (value < min) {
                String err = value + " is less than the minimum allowable ";
                err += "value of " + min;
                throw new ValidationException(err);
            }
        }
        if (useMax) {
            if (value > max) {
                String err = value + " is greater than the maximum allowable ";
                err += "value of " + max;
                throw new ValidationException(err);
            }
        }

        if (hasPattern())
            super.validate(Long.toString(value), context);

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
            String err = "LongValidator cannot validate a null object.";
            throw new ValidationException(err);
        }

        long value = 0;
        try {
            value = ((Long)object).longValue();
        }
        catch(Exception ex) {
            String err = "Expecting a Long, received instead: ";
            err += object.getClass().getName();
            throw new ValidationException(err);
        }
        validate(value, context);
    } //-- validate

} //-- LongValidator