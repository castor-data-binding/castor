/*
 * Redistribution and use of this software and associated documentation
 * ("Software"), with or without modification, are permitted provided that the
 * following conditions are met:
 *
 * 1. Redistributions of source code must retain copyright statements and
 * notices. Redistributions must also contain a copy of this document.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 * 3. The name "Exolab" must not be used to endorse or promote products derived
 * from this Software without prior written permission of Intalio, Inc. For
 * written permission, please contact info@exolab.org.
 *
 * 4. Products derived from this Software may not be called "Exolab" nor may
 * "Exolab" appear in their names without prior written permission of Intalio,
 * Inc. Exolab is a registered trademark of Intalio, Inc.
 *
 * 5. Due credit should be given to the Exolab Project (http://www.exolab.org/).
 *
 * THIS SOFTWARE IS PROVIDED BY INTALIO, INC. AND CONTRIBUTORS ``AS IS'' AND ANY
 * EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL INTALIO, INC. OR ITS CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * Copyright 2000 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id$
 */
package org.exolab.castor.xml.handlers;

import java.lang.reflect.Array;
import java.util.StringTokenizer;

import org.exolab.castor.mapping.FieldHandler;
import org.exolab.castor.mapping.ValidityException;
import org.exolab.castor.xml.TypeValidator;
import org.exolab.castor.xml.ValidationException;
import org.exolab.castor.xml.XMLFieldHandler;

/**
 * A FieldHandler for the XML Schema Collection type.
 * <p>
 * TODO : support all kind of XSList.
 *
 * @author <a href="blandin@intalio.com">Arnaud Blandin</a>
 * @version $Revision$ $Date: 2003-03-03 00:05:44 -0700 (Mon, 03 Mar
 *          2003) $
 */
public class CollectionFieldHandler extends XMLFieldHandler {

    /** The field handler to which we delegate. */
    private final FieldHandler  _handler;
    /** Type validator to use to validate an instance of this type. */
    private final TypeValidator _validator;

    // ----------------/
    // - Constructors -/
    // ----------------/

    /**
     * Creates a new CollectionFieldHandler using the given FieldHandler for
     * delegation.
     *
     * @param fieldHandler the fieldHandler for delegation.
     */
    public CollectionFieldHandler(final FieldHandler fieldHandler) {
        this(fieldHandler, null);
    } // -- CollectionFieldHandler

    /**
     * Creates a new CollectionFieldHandler using the given FieldHandler for
     * delegation and the given type validator for validation.
     *
     * @param fieldHandler the fieldHandler for delegation.
     * @param validator Type validator to use to validate an instance of this type.
     */
    public CollectionFieldHandler(final FieldHandler fieldHandler, final TypeValidator validator) {
        super();
        if (fieldHandler == null) {
            String err = "The FieldHandler argument passed to "
                    + "the constructor of CollectionFieldHandler must not be null.";
            throw new IllegalArgumentException(err);
        }
        this._handler = fieldHandler;
        this._validator = validator;
    } // -- CollectionFieldHandler

    // ------------------/
    // - Public Methods -/
    // ------------------/

    /**
     * Sets the value of the field associated with this descriptor. If paased a
     * String, then String is tokenized and each token is added as an individual
     * instance to the collection.
     *
     * @param target the object in which to set the value
     * @param value the value of the field
     * @throws IllegalStateException if any value provided fails validation.
     */
    public void setValue(final Object target, final Object value)
                                        throws java.lang.IllegalStateException {
        if (value == null) {
            return;
        }

        // If not a String, assume we are passed something the field handler understands
        if (!(value instanceof String)) {
            _handler.setValue(target, value);
            return;
        }

        StringTokenizer temp = new StringTokenizer((java.lang.String) value, " ");
        final int size = temp.countTokens();
        for (int i = 0; i < size; i++) {
            String tempValue = temp.nextToken();
            try {
                if (_validator != null) {
                    _validator.validate(tempValue, null);
                }
            } catch (ValidationException e) {
                throw new IllegalStateException(e.getMessage());
            }
            _handler.setValue(target, tempValue);
        }
    } // -- setValue

    /**
     * Gets the value of the field associated with this descriptor. If the value
     * is an array, it returns a string 'representing' this array
     *
     * @param target the object from which to get the value
     * @return the value of the field associated with this descriptor.
     * @throws IllegalStateException if any value provided fails validation.
     */
    public Object getValue(final Object target) throws java.lang.IllegalStateException {
        // Needs to return the proper object
        Object temp = _handler.getValue(target);

        if (temp == null) {
            return temp;
        }

        if (!temp.getClass().isArray()) {
            return temp.toString();
        }

        int size = Array.getLength(temp);
        if (size == 0) {
            return null;
        }

        StringBuffer result = new StringBuffer();
        for (int i = 0; i < size; i++) {
            if (i > 0) {
                result.append(' ');
            }
            Object obj = Array.get(temp, i);
            result.append(obj.toString());
        }
        return result.toString();
    }

    /**
     * Sets the value of the field to a default value.
     *
     * @param target The object to reset
     * @throws IllegalStateException if the Java object has changed and is no
     *         longer supported by this handler or the handler is not
     *         compatible with the Java object
     */
    public void resetValue(final Object target) throws java.lang.IllegalStateException {
        _handler.resetValue(target);
    }

    /**
     * Checks the field validity. Returns successfully if the field can be
     * stored, is valid, etc, throws an exception otherwise.
     *
     * @param object The object
     * @throws ValidityException The field is invalid, is required and null, or
     *         any other validity violation
     * @throws IllegalStateException The Java object has changed and is no
     *         longer supported by this handler, or the handler is not
     *         compatiable with the Java object
     */
    public void checkValidity(final Object object) throws ValidityException, IllegalStateException {
        // -- do nothing for now
    } // -- checkValidity

    /**
     * Creates a new instance of the object described by this field.
     *
     * @param parent The object for which the field is created
     * @return A new instance of the field's value
     * @throws IllegalStateException This field is a simple type and cannot be
     *         instantiated
     */
    public Object newInstance(final Object parent) throws IllegalStateException {
        return null;
    } // -- newInstance

    /**
     * Returns true if the given object is an XMLFieldHandler that is equivalent
     * to the delegated handler. An equivalent XMLFieldHandler is an
     * XMLFieldHandler that is an instances of the same class.
     * @param obj The object to compare to <code>this</code>.
     *
     * @return true if the given object is an XMLFieldHandler that is equivalent
     *         to this one.
     */
    public boolean equals(final Object obj) {
        if (obj == null || !(obj instanceof XMLFieldHandler)) {
            return false;
        }
        return _handler.getClass().isInstance(obj);
    } //-- equals

} //-- CollectionFieldHandler
