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
 * Copyright 1999-2000 (C) Intalio, Inc. All Rights Reserved.
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
 * TODO : pattern, enumeration
 * @author <a href="mailto:blandin@intalio.com">Arnaud Blandin</a>
 * @version $Revision$ $Date$
**/
public class DecimalValidator implements TypeValidator
{

    private boolean useMin   = false;
    private boolean useMax   = false;


    private BigDecimal min = null;
    private BigDecimal max = null;
    private Integer precision = null;
    private Integer scale = null;

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
        useMax = false;
    } //-- clearMax

    /**
     * Clears the minimum value for this DecimalValidator
    **/
    public void clearMin() {
        useMin = false;
    } //-- clearMin

    public void setPrecision(int p) {

        precision = new Integer(p);
    }

    public void setScale(int s) {
        scale = new Integer(s);
    }

    /**
     * Sets the minimum value that decimals validated with this
     * validator must be greater than
     * @param minValue the minimum value an decimal validated with this
     * validator must be greater than
    **/
    public void setMinExclusive(BigDecimal minValue) {
        useMin = true;
        min = minValue.add(new BigDecimal(1));
    } //-- setMinExclusive

    /**
     * Sets the minimum value that decimals validated with this
     * validator are allowed to be
     * @param minValue the minimum value an decimal validated with this
     * validator may be
    **/
    public void setMinInclusive(BigDecimal minValue) {
        useMin = true;
        min = minValue;
    } //-- setMinInclusive

    /**
     * Sets the maximum value that decimals validated with this
     * validator must be less than
     * @param maxValue the maximum value an decimal validated with this
     * validator must be less than
    **/
    public void setMaxExclusive(BigDecimal maxValue) {
        useMax = true;
        max = maxValue.subtract(new BigDecimal(1));
   } //-- setMaxExclusive

    /**
     * Sets the maximum value that decimals validated with this
     * validator are allowed to be
     * @param maxValue the maximum value an decimal validated with this
     * validator may be
    **/
    public void setMaxInclusive(BigDecimal maxValue) {
        useMax = true;
        max = maxValue;
    } //-- setMaxInclusive

    public void validate(BigDecimal bd) throws ValidationException {

        if (useMin) {
            if (bd.compareTo(min)==-1) {
                String err = bd + " is less than the minimum allowable ";
                err += "value of " + min;
                throw new ValidationException(err);
            }
        }

        if (useMax) {
            if (bd .compareTo(max)==1) {
                String err = bd + " is greater than the maximum allowable ";
                err += "value of " + max;
                throw new ValidationException(err);
            }
        }

        if ( (scale!=null) && (bd.scale() > scale.intValue()) ) {
                String err = "the SCALE of "+bd+": "+bd.scale()+" must be lower";
                err += " than the value of " + scale;
                throw new ValidationException(err);
        }

        if ( (precision != null) && (precision.intValue() < scale.intValue())) {
                String err = "the SCALE of "+bd+": "+scale+" must be lower";
                err += " than the value of the PRECISION :" + precision;
                throw new ValidationException(err);
        }

        String temp = bd.toString();
        //remove the '.'
        int len = temp.indexOf(".")!=-1 ? temp.toCharArray().length-1 :
                                          temp.toCharArray().length;


        if ( (precision != null) &&
            (precision.intValue() < len) ) {
                String err = "the number of digits of "+bd+" must be lower";
                err += " than the value of " + precision;
                throw new ValidationException(err);
        }
        //return to the garbage collector
        temp = null;

        //-- do the validation of pattern

    } //-- validate

    /**
     * Validates the given Object
     * @param object the Object to validate
    **/
    public void validate(Object object) throws ValidationException {
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