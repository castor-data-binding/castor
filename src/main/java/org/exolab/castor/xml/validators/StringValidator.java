/*
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

import org.exolab.castor.xml.TypeValidator;
import org.exolab.castor.xml.ValidationContext;
import org.exolab.castor.xml.ValidationException;
import org.exolab.castor.xml.XMLConstants;

/**
 * The String Validation class.
 *
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a>
 * @version $Revision$ $Date: 2004-12-11 02:13:52 -0700 (Sat, 11 Dec 2004) $
 */
public class StringValidator extends PatternValidator implements TypeValidator {

    /** If true, this field must be present. */
    private boolean             _required   = false;
    /** Whitespace handing for this String.  (Really belongs elsewhere.) */
    private String              _whiteSpace = XMLConstants.WHITESPACE_PRESERVE;
    /** Exact length required of the string.  0 means no length requirement. */
    private int                 _length     = 0;
    /** Minimum length required of the string.  0 means no minimum length requirement. */
    private int                 _minLength  = 0;
    /** Maximum length required of the string.  -1 means no maximum length requirement. */
    private int                 _maxLength  = -1;
    /** Fixed value of this string, null if none is set. */
    private String              _fixed      = null;

    /**
     * Creates a new StringValidator with no restrictions.
     */
    public StringValidator() {
        super();
    } // -- StringValidator

    /**
     * Clears the fixed value for this ShortValidator.
     */
    public void clearFixed() {
        _fixed = null;
    } // -- clearFixed

    /**
     * Sets the fixed value in which all valid Strings must match.
     *
     * @param fixedValue
     *            the fixed value that all Strings must match
     */
    public void setFixed(final String fixedValue) {
        this._fixed = fixedValue;
    } // -- setFixedValue

    /**
     * Only used for backward compatibility for object model generated with an
     * old version of Castor.
     *
     * @param fixedValue
     *            the fixed value that all Strings must match
     *
     * @deprecated since 0.9.4_beta
     */
    public void setFixedValue(final String fixedValue) {
        setFixed(fixedValue);
    }

    /**
     * Sets the maximum string length for String validation. To pass validation,
     * a String must be shorter than or equal to this length. To remove this
     * facet, set a negative value.
     *
     * @param maxLength
     *            the maximum length for valid Strings
     */
    public void setMaxLength(final int maxLength) {
        this._maxLength = maxLength;
    } // -- setMaxLength

    /**
     * Sets the minimum string length for String validation. To pass validation,
     * a String must be longer than or equal to this length. To remove this
     * facet, set a negative value.
     *
     * @param minLength
     *            the minimum length for valid Strings
     */
    public void setMinLength(final int minLength) {
        this._minLength = minLength;
    } // -- setMinLength

    /**
     * Sets the required string length for String validation. To pass
     * validation, a String must be exactly this many characters long. To remove
     * this facet, set a negative value.
     *
     * @param length
     *            the required length for valid Strings
     */
    public void setLength(final int length) {
        this._length = length;
        setMaxLength(length);
        setMinLength(length);
    } // -- setLength

    /**
     * Sets whether or not a String is required (non null).
     *
     * @param required
     *            the flag indicating whether this string must be non-null.
     */
    public void setRequired(final boolean required) {
        this._required = required;
    } // -- setRequired

    /**
     * Sets the whiteSpace facet of the validator.
     * <p>
     * The value of the whiteSpace facet must be one of the following:
     * <ul>
     * <li>preserve</li>
     * <li>replace</li>
     * <li>collapse</li>
     * </ul>
     * any other value will generate a Warning and set the whiteSpace to
     * preserve.
     * <p>
     * FIXME:  This is not really a function of validation, but of XML
     * processing before the string is returned from the XML processor.  This
     * should be moved to the FieldHandler, or somewhere else, but not here.
     *
     * @param value
     *            the whiteSpace value
     */
    public void setWhiteSpace(final String value) {
        if (value.equals(XMLConstants.WHITESPACE_PRESERVE)) {
            this._whiteSpace = value;
        } else if (value.equals(XMLConstants.WHITESPACE_REPLACE)) {
            this._whiteSpace = value;
        } else if (value.equals(XMLConstants.WHITESPACE_COLLAPSE)) {
            this._whiteSpace = value;
        } else {
            System.out.println("Warning: '" + value
                    + "' is a bad entry for the whiteSpace value");
            this._whiteSpace = XMLConstants.WHITESPACE_PRESERVE;
        }
    } // -- setWhiteSpace

    /**
     * Normalizes the given string according to the whiteSpace facet used.
     * <p>
     * FIXME: THIS METHOD SHOULD NOT BE HERE..SHOULD BE MOVED TO A FieldHandler
     * or to the Unmarshaller...but not here!!! (kvisco 20030125)
     * @param value
     *            the String to normalize
     * @return the normalized string.
     */
    public String normalize(final String value) {
        if (value == null) {
            return null;
        }

        if (value.length() == 0) {
            return value;
        }

        char[] chars = value.toCharArray();
        int length = chars.length;

        for (int i = 0; i < length; i++) {
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

        if (_whiteSpace.equals(XMLConstants.WHITESPACE_COLLAPSE)) {
            // -- heavy method to keep compatibility
            // -- with JDK 1.1 (can't use Vector.toArray)
            char[] temp = new char[chars.length];
            int tempCount = 0;
            int i = 0;
            while (i < length - 1) {
                if (chars[i] == ' ') {
                    // --put the first space
                    temp[tempCount] = chars[i];
                    tempCount++;
                    // --skip the others
                    i++;
                    while (i < length - 1 && chars[i] == ' ') {
                        i++;
                    }
                    continue;
                }
                temp[tempCount] = chars[i];
                tempCount++;
                i++;
            }
            // --we are at the end
            if (chars[i] != ' ') {
                temp[tempCount] = chars[i];
            }

            length = ++tempCount;
            chars = temp;
        }

        return new String(chars, 0, length);
    }

    /**
     * Validates the given Object.
     *
     * @param value
     *            the string to validate
     * @param context
     *            the ValidationContext
     * @throws ValidationException if the object fails validation.
     */
    public void validate(final String value, final ValidationContext context)
                                                    throws ValidationException {
        if (value == null) {
            if (_required && !isNillable()) {
                String err = "this is a required field and cannot be null.";
                throw new ValidationException(err);
            }
            return;
        }

        if (_fixed != null && !_fixed.equals(value)) {
            String err = "strings of this type must be equal to the fixed value of " + _fixed;
            throw new ValidationException(err);
        }

        int len = value.length();

        if (_length > 0 && len != _length) {
            String err = "Strings of this type must have a length of " + _length + " characters";
            throw new ValidationException(err);
        }

        if (_minLength > 0 && len < _minLength) {
            String err = "Strings of this type must have a minimum length of " + _minLength
                    + " characters";
            throw new ValidationException(err);
        }

        if (_maxLength >= 0 && len > _maxLength) {
            String err = "Strings of this type must have a maximum length of " + _maxLength
                    + " characters";
            throw new ValidationException(err);
        }

        if (hasPattern()) {
            super.validate(value, context);
        }

        if (!this._whiteSpace.equals(XMLConstants.WHITESPACE_PRESERVE)) {
            normalize(value);
        }
    } // -- validate

    /**
     * Validates the given Object.
     *
     * @param object
     *            the Object to validate
     * @throws ValidationException if the object fails validation.
     */
    public void validate(final Object object) throws ValidationException {
        validate(object, (ValidationContext) null);
    } // -- validate

    /**
     * Validates the given Object.
     *
     * @param object
     *            the Object to validate
     * @param context
     *            the ValidationContext
     * @throws ValidationException if the object fails validation.
     */
    public void validate(final Object object, final ValidationContext context)
                                                    throws ValidationException {
        if (object == null) {
            if (_required) {
                String err = "this is a required field and cannot be null.";
                throw new ValidationException(err);
            }
            return;
        }

        validate(object.toString(), context);
    } //-- validate

} //-- StringValidator
