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
 * 05/30/2001   Arnaud Blandin   Created
 */

package org.exolab.castor.xml.validators;

import org.exolab.castor.types.DateTimeBase;

import org.exolab.castor.xml.ValidationException;
import org.exolab.castor.xml.TypeValidator;

public class DateTimeValidator implements TypeValidator {


    private DateTimeBase _maxInclusive;
    private DateTimeBase _maxExclusive;
    private DateTimeBase _minInclusive;
    private DateTimeBase _minExclusive;

    public DateTimeValidator() {
        super();
    } //-- DateTimeValidator

   /**
     * Sets the maximum exclusive value that this Date/Time can hold.
     * @param max the maximum exclusive value this Date/Time can be
     * @see setMaxInclusive
    **/
    public void setMaxExclusive(DateTimeBase max) {
        _maxExclusive = max;
        _maxInclusive = null;
    } //-- setMaxExclusive

    /**
     * Sets the maximum inclusive value that this Date/Time can hold.
     * @param max the maximum inclusive value this Date/Time can be
     * @see setMaxExclusive
    **/
    public void setMaxInclusive(DateTimeBase max) {
        _maxInclusive = max;
        _maxExclusive = null;
    } //-- setMaxInclusive


    /**
     * Sets the minimum exclusive value that this Date/Time can hold.
     * @param max the minimum exclusive value this Date/Time can be
     * @see setMinInclusive
    **/
    public void setMinExclusive(DateTimeBase min) {
        _minExclusive = min;
        _minInclusive = null;
    } //-- setMinExclusive

    /**
     * Sets the minimum inclusive value that this Date/Time can hold.
     * @param max the minimum inclusive value this Date/Time can be
     * @see setMinExclusive
    **/
    public void setMinInclusive(DateTimeBase min) {
        _minInclusive = min;
        _minExclusive = null;
    } //-- setMinInclusive

    /**
     * Validates a Date/Time instance
     * @param dateTime the date/time type to validate
     * @throw ValidationException
     */
    public void validate(DateTimeBase dateTime) throws ValidationException {

        boolean isThereMinInclusive = (_minInclusive != null);
        boolean isThereMinExclusive = (_minExclusive != null);
        boolean isThereMaxInclusive = (_maxInclusive != null);
        boolean isThereMaxExclusive = (_maxExclusive != null);

        if (isThereMinExclusive && isThereMinInclusive) {
            throw new ValidationException("both minInclusive and minExclusive"
                                          +"are set up");
        }

        if (isThereMaxExclusive && isThereMaxInclusive) {
            throw new ValidationException("both maxInclusive and maxExclusive"
                                          +"are set up");
        }
        if (isThereMinInclusive) {
            if ( dateTime.compareTo(_minInclusive) != DateTimeBase.GREATER_THAN) {
                String err = dateTime + " must be greater than the minimum allowable ";
                err += "value of " + _minInclusive;
                throw new ValidationException(err);
            }
        }

         if (isThereMinExclusive) {
            if ( (dateTime.compareTo(_minExclusive) != DateTimeBase.GREATER_THAN) &&
                  !dateTime.equals(_minExclusive) )
            {
                String err = dateTime + " must be greater than (or equal to) the minimum allowable ";
                err += "value of " + _minExclusive;
                throw new ValidationException(err);
            }
        }

         if (isThereMaxInclusive) {
            if ( dateTime.compareTo(_maxInclusive) != DateTimeBase.LESS_THAN) {
                String err = dateTime + " must be less than the maximum allowable ";
                err += "value of " + _maxInclusive;
                throw new ValidationException(err);
            }
        }

         if (isThereMaxExclusive) {
            if ( (dateTime.compareTo(_maxExclusive) != DateTimeBase.LESS_THAN) &&
                  !dateTime.equals(_maxExclusive) )
            {
                String err = dateTime + " must be less than (or equal to) the maximum allowable ";
                err += "value of " + _maxExclusive;
                throw new ValidationException(err);
            }
        }

        //use the pattern validator
        /*if (hasPattern()) {
            //something to do...
        }*/

    } //-- validate

    /**
     * Validates the given Object
     * @param object the Object to validate
    **/
    public void validate(Object object)
        throws ValidationException
    {
        if (object == null) {
            String err = "DateTimeValidator cannot validate a null object.";
            throw new ValidationException(err);
        }

        DateTimeBase value = null;

        try {
            value = (DateTimeBase)object;
        } catch(Exception ex) {
            String err = ex.toString()+"\nExpecting a RecurringDuration, received instead: ";
            err += object.getClass().getName();
            throw new ValidationException(err);
        }
        validate(value);
    } //-- validate
}//-- RecurringDurationValidator