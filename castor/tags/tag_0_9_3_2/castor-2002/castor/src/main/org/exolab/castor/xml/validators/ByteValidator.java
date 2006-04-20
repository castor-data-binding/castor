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
 * Copyright 2001 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id$
 */


package org.exolab.castor.xml.validators;

import org.exolab.castor.xml.*;

/**
 * The Byte Validation class. This class handles validation
 * for the byte type.
 * @author <a href="mailto:visco@intalio.com">Keith Visco</a>
 * @author <a href="mailto:blandin@intalio.com">Arnaud Blandin</a>
 * @version $Revision$ $Date$
**/
public class ByteValidator extends PatternValidator
    implements TypeValidator
{

    private boolean useMin   = false;
    private boolean useMax   = false;
    private boolean useFixed = false;

    private byte min = 0;
    private byte max = 0;

    private byte fixed = 0;

    /**
     * Creates a new ByteValidator with no restrictions
    **/
    public ByteValidator() {
        super();
    } //-- ByteValidator

    /**
     * Clears the fixed value for this IntegerValidator
    **/
    public void clearFixed() {
        useFixed = false;
    } //-- clearFixed

    /**
     * Clears the maximum value for this ByteValidator
    **/
    public void clearMax() {
        useMax = false;
    } //-- clearMax

    /**
     * Clears the minimum value for this ByteValidator
    **/
    public void clearMin() {
        useMin = false;
    } //-- clearMin

    /**
     * Sets the fixed value that bytes validated with this
     * validated must be equal to
     * @param fixedValue the fixed value a byte validated with
     * this validator must be equal to.
     * <BR>
     * NOTE: Using Fixed values takes preceedence over using max and mins,
     * and is really the same as setting both max-inclusive and
     * min-inclusive to the same value
    **/
    public void setFixed(byte fixedValue) {
        useFixed = true;
        this.fixed = fixedValue;
    } //-- setFixed

    /**
     * Sets the minimum value that bytes validated with this
     * validator must be greater than
     * @param minValue the minimum value a byte validated with this
     * validator must be greater than
    **/
    public void setMinExclusive(byte minValue) {
        useMin = true;
        min = (byte) (minValue+1);
    } //-- setMinExclusive

    /**
     * Sets the minimum value that bytes validated with this
     * validator are allowed to be
     * @param minValue the minimum value a byte validated with this
     * validator may be
    **/
    public void setMinInclusive(byte minValue) {
        useMin = true;
        min = minValue;
    } //-- setMinInclusive

    /**
     * Sets the maximum value that bytes validated with this
     * validator must be less than
     * @param maxValue the maximum value a byte validated with this
     * validator must be less than
    **/
    public void setMaxExclusive(byte maxValue) {
        useMax = true;
        max = (byte)(maxValue-1);
    } //-- setMaxExclusive

    /**
     * Sets the maximum value that bytes validated with this
     * validator are allowed to be
     * @param maxValue the maximum value a byte validated with this
     * validator may be
    **/
    public void setMaxInclusive(byte maxValue) {
        useMax = true;
        max = maxValue;
    } //-- setMaxInclusive

    public void validate(byte b)
        throws ValidationException
    {

        if (useFixed) {
            if (b != fixed) {
                String err = b + " is not equal to the fixed value of "
                    + fixed;
                throw new ValidationException(err);
            }
            return;
        }

        if (useMin) {
            if (b < min) {
                String err = b + " is less than the minimum allowable ";
                err += "value of " + min;
                throw new ValidationException(err);
            }
        }
        if (useMax) {
            if (b > max) {
                String err = b + " is greater than the maximum allowable ";
                err += "value of " + max;
                throw new ValidationException(err);
            }
        }

        if (hasPattern())
            super.validate(Byte.toString(b));

    } //-- validate

    /**
     * Validates the given Object
     * @param object the Object to validate
    **/
    public void validate(Object object)
        throws ValidationException
    {
        if (object == null) {
            String err = "ByteValidator cannot validate a null object.";
            throw new ValidationException(err);
        }

        byte value = 0;
        try {
            value = ((Byte)object).byteValue();
        }
        catch(Exception ex) {
            String err = "Expecting a Byte, received instead: ";
            err += object.getClass().getName();
            throw new ValidationException(err);
        }
        validate(value);
    } //-- validate

} //-- IntegerValidator