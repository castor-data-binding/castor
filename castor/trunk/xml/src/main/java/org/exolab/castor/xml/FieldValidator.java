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
 * Copyright 2000-2004 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id$
 */
package org.exolab.castor.xml;

import java.lang.reflect.Array;
import java.text.MessageFormat;
import java.util.Enumeration;
import java.util.List;
import java.util.Vector;

import org.exolab.castor.mapping.FieldHandler;
import org.exolab.castor.xml.location.XPathLocation;

/**
 * Handles field validation.
 *
 * @author <a href="mailto:kvisco-at-intalio.com">Keith Visco</a>
 * @version $Revision$ $Date: 2004-10-08 22:58:55 -0600 (Fri, 08 Oct 2004) $
 */
public class FieldValidator extends Validator {

    /** Default value for descriptor names. If a Descriptor XML name value is
     * set to this, then no name has been assigned yet. */
    private static final String ERROR_NAME   = "-error-if-this-is-used-";
    /** Default minimum occurrance. */
    private static final int    DEFAULT_MIN  = 0;
    /** Default maximum occurrance. */
    private static final int    DEFAULT_MAX  = -1;

    /** Minimum number of occurrences for this element to pass validation. */
    private int                 _minOccurs   = DEFAULT_MIN;
    /** Maximum number of occurrences for this element to pass validation. */
    private int                 _maxOccurs   = DEFAULT_MAX;
    /** The Field Descriptor describing the field we validate. */
    private XMLFieldDescriptor  _descriptor  = null;
    /** The actual type validator which is used to validate single instances of
     * the field. */
    private TypeValidator       _validator   = null;
    
    /**
     * Creates a default FieldValidator.
     */
    public FieldValidator() {
        super();
    }

    /**
     * Creates a new FieldValidator using the given TypeValidator.
     * @param validator the TypeValidator to delegate validation to
     */
    public FieldValidator(final TypeValidator validator) {
        super();
        this._validator = validator;
    }

    /**
     * Returns the mimimum number of occurances for a given object.
     *
     * @return The mimimum number of occurances for a given object. A zero value
     *         denotes no lower bound (ie. the object is optional).
     */
    public int getMinOccurs() {
        return _minOccurs;
    }

    /**
     * Returns the maximum number of occurances for a given object.
     *
     * @return The maximum number of occurances for a given object. A negative
     *         value denotes no upper bound.
     */
    public int getMaxOccurs() {
        return _maxOccurs;
    }

    /**
     * Returns the TypeValidator.
     * @return the TypeValidator.
     */
    public TypeValidator getTypeValidator() {
        return _validator;
    }

    /**
     * Returns true if a TypeValidator has been set.
     * @return true if a TypeValidator has been set.
     */
    public boolean hasTypeValidator() {
        return _validator != null;
    }

    /**
     * Sets the mimimum number of occurances for a given object. A zero, or
     * negative value denotes no lower bound (i.e., the object is optional).
     *
     * @param minOccurs the minimum number of times an object must occur in
     *        order to be valid.
     */
    public void setMinOccurs(final int minOccurs) {
        this._minOccurs = (minOccurs < 0) ? 0 : minOccurs;
    }

    /**
     * Sets the maximum number of occurances for a given object. A negative
     * value denotes no upper bound.
     *
     * @param maxOccurs the maximum number of times an object may occur.
     */
    public void setMaxOccurs(final int maxOccurs) {
        this._maxOccurs = maxOccurs;
    }

    /**
     * Sets the field descriptor to use for obtaining information about the
     * field to validate, such as the field name, the field handler, etc.
     *
     * @param descriptor the field descriptor for the field to validate
     */
    public void setDescriptor(final XMLFieldDescriptor descriptor) {
        this._descriptor = descriptor;
    }

    public void setValidator(final TypeValidator validator) {
        this._validator = validator;
    }

    /**
     * Validates the given Object.
     *
     * @param object the Object that contains the field to validate
     * @param context the ValidationContext
     * @throws ValidationException if validation fails
     */
    public void validate(final Object object, final ValidationContext context)
                                                      throws ValidationException {
        if (_descriptor == null || object == null || context.isValidated(object)) {
            return;
        }

        // Don't validate "transient" fields.
        if (_descriptor.isTransient()) {
            return;
        }

        FieldHandler handler = _descriptor.getHandler();
        if (handler == null) {
            return;
        }

        // Get the value of the field
        Object value = handler.getValue(object);
        if (value == null) {
            if (!_descriptor.isRequired() || _descriptor.isNillable()) {
                return;
            }
            // deal with lenient id/idref validation accordingly, skipping exception handling
            // in this case
            if (_descriptor.isRequired() 
                    && _descriptor.getSchemaType() != null
                    && _descriptor.getSchemaType().equals("IDREF") 
                    && context.getInternalContext().getLenientIdValidation()) {
                return;
            }
            StringBuffer buff = new StringBuffer();
            if (!ERROR_NAME.equals(_descriptor.getXMLName())) {
                buff.append(MessageFormat.format(resourceBundle.getString("validatorField.error.required.field.whose"), 
                        new Object[] {_descriptor.getFieldName(), object.getClass().getName(), _descriptor.getXMLName()}));
            }
            else {
                buff.append(MessageFormat.format(resourceBundle.getString("validatorField.error.required.field"), 
                        new Object[] {_descriptor.getFieldName(), object.getClass().getName()}));
            }
            
            throw new ValidationException(buff.toString());
        }

        if (_descriptor.isReference()) {
            if (_validator != null) {
                _validator.validate(value, context);
            }
            return;
        }

        // Prevent endless loop!  Have we seen this object yet?
        if (context != null) {
            if (context.isValidated(object)) {
                return;
            }
            //-- mark object as processed
            context.addValidated(object);
        }

        // We are now ready to do actual validation

        Class type = value.getClass();
        int size = 1;
        long occurence = -1;

        try {
            if (type.isArray()) {
                // We don't validate Byte array types
                if (type.getComponentType() != Byte.TYPE) {
                    size = Array.getLength(value);
                    if (_validator != null) {
                        for (int i = 0; i < size; i++) {
                            occurence = i + 1;
                            _validator.validate(Array.get(value, i), context);
                        }
                    } else {
                        for (int i = 0; i < size; i++) {
                            super.validate(Array.get(value, i), context);
                        }
                    }
                }
            } else if (value instanceof Enumeration) {
                // <NOTE>
                // The following code should be changed to use CollectionHandler
                // </NOTE>
                size = 0;
                for (Enumeration enumeration = (Enumeration) value; 
                   enumeration.hasMoreElements(); ) {
                    ++size;
                    validateInstance(context, enumeration.nextElement());
                }
            } else if (value instanceof Vector) {
                Vector vector = (Vector) value;
                size = vector.size();
                for (int i = 0; i < size; i++) {
                    occurence = i + 1;
                    validateInstance(context, vector.elementAt(i));
                }
            } else if (value instanceof List) {
                List list = (List) value;
                size = list.size();
                for (int i = 0; i < size; i++) {
                    occurence = i + 1;
                    validateInstance(context, list.get(i));
                }
            } else {
                validateInstance(context, value);
            }
        } catch (ValidationException vx) {
            //-- add additional validation information
            String err = MessageFormat.format(resourceBundle.getString("validatorField.error.exception"), 
                    new Object[] { _descriptor.getFieldName(), object.getClass().getName()});
            ValidationException validationException = new ValidationException(err, vx);
            addLocationInformation(_descriptor, validationException, occurence);
            throw validationException;
        }

        // Check sizes of collection

        // Check minimum.
        // If any items exist (size != 0) or the descriptor is marked as
        // required then we need to report the error. Otherwise size == 0 and
        // field is not required, so no error.
        if (size < _minOccurs && (size != 0 || _descriptor.isRequired())) {
            StringBuffer buff = new StringBuffer();
            if (!ERROR_NAME.equals(_descriptor.getXMLName())) {
                buff.append(MessageFormat.format(resourceBundle.getString("validatorField.error.exception.min.occurs.whose"), 
                        new Object[] { _minOccurs, _descriptor.getFieldName(), object.getClass().getName(),
                    _descriptor.getXMLName()}));
            } else {
                buff.append(MessageFormat.format(resourceBundle.getString("validatorField.error.exception.min.occurs"), 
                        new Object[] { _minOccurs, _descriptor.getFieldName(), object.getClass().getName()}));
            }
            throw new ValidationException(buff.toString());
        }

        // Check maximum.
        if (_maxOccurs >= 0 && size > _maxOccurs) {
            StringBuffer buff = new StringBuffer();
            if (!ERROR_NAME.equals(_descriptor.getXMLName())) {
                buff.append(MessageFormat.format(resourceBundle.getString("validatorField.error.exception.max.occurs.whose"), 
                        new Object[] { _maxOccurs, _descriptor.getFieldName(), object.getClass().getName(), _descriptor.getXMLName()}));
            } else {
                buff.append(MessageFormat.format(resourceBundle.getString("validatorField.error.exception.max.occurs"), 
                        new Object[] { _maxOccurs, _descriptor.getFieldName(), object.getClass().getName()}));
            }

            throw new ValidationException(buff.toString());
        }

        if (context != null) {
            context.removeValidated(object);
        }
    }

    /**
     * Validate an individual instance.
     * @param context the validation context.
     * @param value The instance to validate.
     * @throws ValidationException if validation fails
     */
    private void validateInstance(final ValidationContext context, final Object value)
                                                            throws ValidationException {
        if (_validator != null) {
            _validator.validate(value, context);
        } else {
            super.validate(value, context);
        }
    }

    /**
     * Adds location information to the {@link ValidationException} instance.
     * @param fieldDescriptor The {@link XMLFieldDescriptor} instance whose has been responsible for
     * generating the error.
     * @param e The {@link ValidationException} to enrich.
     * @param occurence If greater than 0, denotes the position of the invalid collection value.
     */
    private void addLocationInformation(final XMLFieldDescriptor fieldDescriptor,
            final ValidationException e, final long occurence) {
        XPathLocation loc = (XPathLocation) e.getLocation();
        if (loc == null) {
            loc = new XPathLocation();
            e.setLocation(loc);
            String xmlName = fieldDescriptor.getXMLName();
            if (occurence > 0) {
                xmlName += "[" + occurence + "]";
            }
            if (fieldDescriptor.getNodeType() == NodeType.Attribute) {
                loc.addAttribute(xmlName);
            } else {
                loc.addChild(xmlName);
            }
        }
    }

}
