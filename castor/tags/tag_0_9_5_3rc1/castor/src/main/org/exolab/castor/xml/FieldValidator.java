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
 * Copyright 2000 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id$
 */


package org.exolab.castor.xml;

import java.util.Vector;
import java.util.Enumeration;
import java.lang.reflect.Array;

import org.exolab.castor.mapping.FieldHandler;
import org.exolab.castor.mapping.ValidityException;

/**
 * Handles field validation
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a>
 * @version $Revision$ $Date$
**/
public class FieldValidator extends Validator {
    
    
    private static final String ERROR_NAME = "-error-if-this-is-used-";
    
    private int minOccurs =  0;  //-- default minimum occurance
    private int maxOccurs = -1;  //-- default maximum occurance [none]

    private XMLFieldDescriptor _descriptor = null;
    
    /**
     * The actual type validator which is used to validate
     * single instances of the field.
    **/
    private TypeValidator      _validator = null;
    
    /**
     * Creates a default FieldValidator
     *
    **/
    public FieldValidator() {    
        super();
    } //-- FieldValidator 

    /**
     * Creates a new FieldValidator using the given TypeValidator
     * @param validator the TypeValidator to delegate validation to
    **/
    public FieldValidator(TypeValidator validator) {
        super();
        this._validator = validator;
    } //-- FieldValidator
    
    /**
     * Returns the mimimum number of occurances for a given object.
     *
     * @returns the mimimum number of occurances for a given object.
     * A zero value denotes no lower bound 
     * (ie. the object is optional)
    **/
    public int getMinOccurs() {
        return minOccurs;
    } //-- getMinOccurs
    
    /**
     * Returns the maximum number of occurances for a given object.
     * 
     * @returns the maximum number of occurances for a given object.
     * A negative value denotes no upper bound.
     * 
    **/
    public int getMaxOccurs() {
        return maxOccurs;
    } //-- getMaxOccurs
    
    /**
     * Returns true if a TypeValidator has been set
     *
     * @return true if a TypeValidator has been set
    **/
    public TypeValidator getTypeValidator() {
        return _validator;
    } //-- getTypeValidator
    
    /**
     * Returns true if a TypeValidator has been set
     *
     * @return true if a TypeValidator has been set
    **/
    public boolean hasTypeValidator() {
        return (_validator != null);
    } //-- hasTypeValidator
    
    /**
     * Sets the mimimum number of occurances for a given object
     *
     * @param minOccurs the minimum number of times an object must occur
     * in order to be valid.
     * A zero, or negative value denotes no lower bound 
     * (ie. the object is optional)
    **/
    public void setMinOccurs(int minOccurs) {
        this.minOccurs = (minOccurs < 0) ? 0 : minOccurs;
    } //-- setMinOccurs
    
    /**
     * Sets the maximum number of occurances for a given object
     * 
     * @param maxOccurs the maximum number of times an object
     * may occur. A negative value denotes no upper bound.
     * 
    **/
    public void setMaxOccurs(int maxOccurs) {
        this.maxOccurs = maxOccurs;
    } //-- setMaxOccurs

    /**
     * Sets the field descriptor to use for obtaining information
     * about the field to validate, such as the field name, the field
     * handler, etc.
     * @param descriptor the field descriptor for the field to validate
    **/
    public void setDescriptor(XMLFieldDescriptor descriptor) {
        this._descriptor = descriptor;
    } //-- setDescriptor
    
    public void setValidator(TypeValidator validator) {
        this._validator = validator;
    } //-- setValidator
    
    /**
     * Validates the given Object
     *
     * @param object the Object that contains the field to validate
     * @param context the ValidationContext 
     */
    public void validate(Object object, ValidationContext context)
        throws ValidationException
    {
        if (_descriptor == null) return;
        
        //-- Do not validate references as these should
        //-- be validated elsewhere, validating them
        //-- here could cause endless loops
        if (_descriptor.isReference()) return;
        
        //-- don't validate "transient" fields...
        if (_descriptor.isTransient()) return;
        
        FieldHandler handler = _descriptor.getHandler();
        
        if (handler == null) return;
        
        //-- get the value of the field
        Object value = handler.getValue(object);
        
        if ((value == null) && (_descriptor.isRequired())) {
            String err = "The field '" + _descriptor.getFieldName() + "' ";
            if (!ERROR_NAME.equals(_descriptor.getXMLName())) {
                err += "(whose xml name is '" + _descriptor.getXMLName() + "') ";
            }
            err += "is a required field.";
            throw new ValidationException(err);
        }
        
        
        //-- check number of occurances occurance
        if (value != null) {

            Class type = value.getClass();
            
            int size = 1;
            boolean byteArray = false;
            if (type.isArray()) {
                byteArray = (type.getComponentType() == Byte.TYPE);
                if (!byteArray) {
                    size = Array.getLength(value);
                    if (_validator != null) {
                        for (int i = 0; i < size; i++)
                            _validator.validate(Array.get(value, i), context);
                    }
                    else {
                        for (int i = 0; i < size; i++)
                            super.validate(Array.get(value, i), context);
                    }
                }
            }
            //-- <NOTE>
            //-- The following code should be changed
            //-- to use CollectionHandler
            //-- </NOTE>
            else if (value instanceof Enumeration) {
                Enumeration enum = (Enumeration)value;
                size = 0;
                while (enum.hasMoreElements()) {
                    ++size;
                    Object obj = enum.nextElement();
                    if (_validator != null) 
                        _validator.validate(obj, context);
                    else
                        super.validate(obj, context);
                }
            }
            else if (value instanceof Vector) {
                Vector vector = (Vector)value;
                size = vector.size();
                for (int i = 0; i < vector.size(); i++) {
                    if (_validator != null)
                        _validator.validate(vector.elementAt(i), context);
                    else
                        super.validate(vector.elementAt(i), context);
                }
            }
            else {
                if (_validator != null)
                    _validator.validate(value, context);
                else
                    super.validate(value, context);
            }
            
            //-- Check size of collection
            
            //-- check minimum
            if (size < minOccurs) {
                //-- If any items exist (size != 0) or the descriptor
                //-- is marked as required then we need to report the
                //-- error. Otherwise size == 0 and field is not
                //-- required, so no error.
                if ((size != 0) || (_descriptor.isRequired())) {
                    String err = "A minimum of " + minOccurs + " ";
                    err += _descriptor.getFieldName() + " object(s) ";
                    if (!ERROR_NAME.equals(_descriptor.getXMLName())) {
                        err += "(whose xml name is '" + _descriptor.getXMLName() + "') ";
                    }
                    err += "are required.";
                    
                    throw new ValidationException(err);
                }
            }
            
            //-- check maximum
            if ((maxOccurs >= 0) && (size > maxOccurs)) {
                String err = "A maximum of " + maxOccurs + " ";
                err += _descriptor.getFieldName() + " object(s) ";
                if (!ERROR_NAME.equals(_descriptor.getXMLName())) {
                    err += "(whose xml name is '" + _descriptor.getXMLName() + "') ";
                }
                err += "are required.";
                throw new ValidationException(err);
            }
        }
        
    } //-- validate
    
} //-- FieldValidator