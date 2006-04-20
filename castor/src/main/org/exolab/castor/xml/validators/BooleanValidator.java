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
 * Copyright 2001-2003 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id$
 */


package org.exolab.castor.xml.validators;

import org.exolab.castor.xml.*;

/**
 * The Boolean Validation class. This class handles validation
 * for the boolean type. This will only check that a boolean has the proper fixed value.
 *
 * @author <a href="mailto:blandin@intalio.com">Arnaud Blandin</a>
 * @version $Revision$ $Date$
 */
public class BooleanValidator implements TypeValidator
{

    private boolean _useFixed = false;

    private boolean _fixed = false;

    /**
     * Creates a new BooleanValidator with no restrictions
    **/
    public BooleanValidator() {
        super();
    } //-- ByteValidator

    /**
     * Clears the fixed value for this BooleanValidator
    **/
    public void clearFixed() {
        _useFixed = false;
    } //-- clearFixed
    
    /**
     * Returns the fixed value that boolean validated with this
     * validator must be equal to. A null value is returned
     * if no fixed value has been specified.
     *
     * @return the fixed value to validate against.
     */
    public Boolean getFixed() {
        if (_useFixed) {
            return new Boolean(_fixed);
        }
        return null;
    } //-- getFixed


    /**
     * Returns true if a fixed value, to validate against, has been
     * set.
     *
     * @return true if a fixed value has been set.
     */
    public boolean hasFixed() {
        return _useFixed;
    } //-- hasFixed
    
    /**
     * Sets the fixed value that boolean validated with this
     * validator must be equal to.
     * @param fixedValue the fixed value a boolean validated with
     * this validator must be equal to.
     */
    public void setFixed(boolean fixedValue) {
        _useFixed = true;
        _fixed = fixedValue;
    } //-- setFixed

    /**
     * Sets the fixed value that boolean validated with this
     * validator must be equal to.
     * @param fixedValue the fixed value a boolean validated with
     * this validator must be equal to.
     */
    public void setFixed(Boolean fixedValue) {
        _useFixed = true;
        _fixed = fixedValue.booleanValue();
    }

    public void validate(boolean b)
        throws ValidationException
    {

        if (_useFixed) {
            if (b != _fixed) {
                String err = b + " is not equal to the fixed value of "
                    + _fixed;
                throw new ValidationException(err);
            }
            return;
        }

    } //-- validate

    /**
     * Validates the given Object.
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
            String err = "BooleanValidator cannot validate a null object.";
            throw new ValidationException(err);
        }

        boolean value = false;
        try {
            value = ((Boolean)object).booleanValue();
        }
        catch(Exception ex) {
            String err = "Expecting a Boolean, received instead: ";
            err += object.getClass().getName();
            throw new ValidationException(err);
        }
        validate(value);
    } //-- validate

} //-- BooleanValidator