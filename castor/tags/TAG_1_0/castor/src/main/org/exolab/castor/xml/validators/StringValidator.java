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
 * The String Validation class
 *
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a>
 * @version $Revision$ $Date$
 */
public class StringValidator extends PatternValidator
    implements TypeValidator
{


    private final static String PRESERVE = "preserve";
    private final static String REPLACE = "replace";
    private final static String COLLAPSE = "collapse";

    private String  fixed      = null;

    private boolean required   = false;

    private int _length        = 0;
    private int     minLength  = 0;

    private int     maxLength  = -1;
    private String _whiteSpace = PRESERVE;


    /**
     * Creates a new StringValidator with no restrictions
    **/
    public StringValidator() {
        super();
    } //-- StringValidator


    /**
     * Sets the fixed value in which all valid Strings must match.
     * @param fixedValue the fixed value that all Strings must match
    **/
    public void setFixed(String fixedValue) {
        this.fixed = fixedValue;
    } //-- setFixedValue

    /**
     * Only used for backward compatibility for object model generated with an old
     * version of Castor
     * @deprecated since 0.9.4_beta
     *
     */
     public void setFixedValue(String fixedValue) {
          setFixed(fixedValue);
     }
    /**
     * Sets the maximum length of that a valid String must be.
     * To remove the max length facet, use a negative value.
     * @param maxLength the maximum length for valid Strings
    **/
    public void setMaxLength(int maxLength) {
        this.maxLength = maxLength;
    } //-- setMaxLength

    /**
     * Sets the minimum length that valid Strings must be
     * @param minLength the minimum length that valid Strings must be
    **/
    public void setMinLength(int minLength) {
        this.minLength = minLength;
    } //-- setMinLength

    /**
     * Sets the length that valid Strings must be
     * @param length the length that valid Strings must be
     */
     public void setLength( int length) {
        this._length = length;
        setMaxLength(length);
        setMinLength(length);
    }//-- setLength

    /**
     * Sets whether or not a String is required (non null)
     * @param required the flag indicating whether Strings are required
    **/
    public void setRequired(boolean required) {
        this.required = required;
    } //-- setRequired

     /**
     * <p>Sets the whiteSpace facet of the validator
     * <p>The value of the whiteSpace facet must be one of the following:
     * <ul>
     *  <li>preserve</li>
     *  <li>replace</li>
     *  <li>collapse</li>
     * </ul>
     * any other value will generate a Warning and set the whiteSpace to preserved
     * @param whiteSpace the whiteSpace value
     */
     public void setWhiteSpace(String value) {
        if (value.equals(PRESERVE))
            this._whiteSpace = value;
        else if (value.equals(REPLACE))
            this._whiteSpace = value;
        else if (value.equals(COLLAPSE))
            this._whiteSpace = value;
        else {
            System.out.println("Warning : "+value+" is a bad entry for the whiteSpace value");
            this._whiteSpace = value;
        }
     }//-- setWhiteSpace

    /**
     * NOTE: THIS METHOD SHOULD NOT BE HERE..SHOULD BE MOVED
     * TO A FieldHandler or to the Unmarshaller...but not
     * here!!! (kvisco 20030125)
     *
     * Normalizes the given string according to the whiteSpace
     * facet used
     *
     * @param value the String to normalize
     */
    public String normalize (String value) {

        if (value == null) return null;

        if (value.length() == 0) return value;
        
        char[] chars = value.toCharArray();
        int length = chars.length;

        for (int i=0; i<length; i++) {
            switch (chars[i]) {
                case '\t':
                case '\r':
                case '\n':
                    chars[i] = ' ';
                    break;
                default:
                    continue;
            }
        }

        if (_whiteSpace.equals(COLLAPSE)) {
            //-- heavy method to keep compatibility
            //-- with JDK 1.1 (can't use Vector.toArray)
            char[] temp = new char[chars.length];
            int temp_count = 0;
            int i = 0;
            while (i<length-1) {
                if (chars[i] ==' ') {
                    //--put the first space
                    temp[temp_count] = chars[i];
                    temp_count++;
                    //--skip the others
                     i++;
                     while (i<length-1 && chars[i] == ' ')
                         i++;
                     continue;
                }
                temp[temp_count] = chars[i];
                temp_count++;
                i++;
            }
            //--we are at the end
            if (chars[i] != ' ')
                temp[temp_count] = chars[i];

            length = ++temp_count;
            chars = temp;
        }

        return new String(chars,0,length);
    }

    public void validate(String value, ValidationContext context)
        throws ValidationException
    {

       if (value == null) {
            if (required && (!isNillable())) {
                String err = "this is a required field and cannot be null.";
                throw new ValidationException(err);
            }
        }
        else {
       if (fixed != null) {
                if (!fixed.equals(value)) {
                    String err = "strings of this type must be equal to the "
                        + "fixed value of " + fixed;
                    throw new ValidationException(err);
                }
            }
            int len = value.length();
            if ( (_length > 0) && (len != _length) ) {
                String err = "strings of this type must have a length of "
                            +_length;
                throw new ValidationException(err);
            }
            if ((minLength > 0) && (len < minLength)) {
                String err = "strings of this type must have a minimum "
                    + "length of " + minLength;
                throw new ValidationException(err);
            }
            else if ((maxLength >= 0) && (len > maxLength)) {
                String err = "strings of this type must have a maximum "
                    + "length of " + maxLength;
                throw new ValidationException(err);
            }
            if (hasPattern()) super.validate(value, context);

            if ( !this._whiteSpace.equals(PRESERVE) )
                normalize(value);
        }
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
            if (required) {
                String err = "this is a required field and cannot be null.";
                throw new ValidationException(err);
            }
        }
        else {
            validate(object.toString(), context);
        }
    } //-- validate


} //-- StringValidator

