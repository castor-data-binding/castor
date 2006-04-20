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
 *    permission of Exoffice Technologies.  For written permission,
 *    please contact info@exolab.org.
 *
 * 4. Products derived from this Software may not be called "Exolab"
 *    nor may "Exolab" appear in their names without prior written
 *    permission of Exoffice Technologies. Exolab is a registered
 *    trademark of Exoffice Technologies.
 *
 * 5. Due credit should be given to the Exolab Project
 *    (http://www.exolab.org/).
 *
 * THIS SOFTWARE IS PROVIDED BY EXOFFICE TECHNOLOGIES AND CONTRIBUTORS
 * ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT
 * NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL
 * EXOFFICE TECHNOLOGIES OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * Copyright 1999 (C) Exoffice Technologies Inc. All Rights Reserved.
 *
 * $Id$
 */


package org.exolab.castor.xml;

/**
 * The basic Validation interface class
 * @author <a href="mailto:kvisco@exoffice.com">Keith Visco</a>
 * @version $Revision$ $Date$
**/
public class IntegerValidator implements TypeValidator {
    
    
    private boolean useMin = false;
    private boolean useMax = false;
    
    private int min = 0;
    private int max = 0;
    
    /**
     * Creates a new IntegerValidator with no restrictions
    **/
    public IntegerValidator() {
        super();
    } //-- IntegerValidator
    
    /**
     * Sets the minimum value that integers validated with this
     * validator must be greater than
     * @param minValue the minimum value an integer validated with this
     * validator must be greater than
    **/
    public void setMinExclusive(int minValue) {
        min = minValue+1;
    } //-- setMinExclusive
    
    /**
     * Sets the minimum value that integers validated with this
     * validator are allowed to be
     * @param minValue the minimum value an integer validated with this
     * validator may be
    **/
    public void setMinInclusive(int minValue) {
        min = minValue;
    } //-- setMinInclusive

    /**
     * Sets the maximum value that integers validated with this
     * validator must be less than
     * @param maxValue the maximum value an integer validated with this
     * validator must be less than
    **/
    public void setMaxExclusive(int maxValue) {
        max = maxValue-1;
    } //-- setMaxExclusive
    
    /**
     * Sets the maximum value that integers validated with this
     * validator are allowed to be
     * @param maxValue the maximum value an integer validated with this
     * validator may be
    **/
    public void setMaxInclusive(int maxValue) {
        max = maxValue;
    } //-- setMaxInclusive
    
    public void validate(int i) 
        throws ValidationException 
    {
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
    } //-- validate
    
    /**
     * Validates the given Object
     * @param object the Object to validate
    **/
    public void validate(Object object)
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
        validate(value);
    } //-- validate
    
} //-- IntegerValidator