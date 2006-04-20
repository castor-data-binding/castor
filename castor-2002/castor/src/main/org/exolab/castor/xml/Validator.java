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
 * Copyright 1999 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id$
 */


package org.exolab.castor.xml;

import org.exolab.castor.xml.util.*;

import org.exolab.castor.mapping.FieldDescriptor;
import org.exolab.castor.mapping.FieldHandler;
import org.exolab.castor.mapping.ValidityException;

import java.util.Enumeration;
import java.util.Vector;
import java.lang.reflect.*;

import org.exolab.castor.util.Stack;

/**
 * The class which performs Validation on an Object model
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a>
 * @version $Revision$ $Date$
**/
public class Validator implements TypeValidator {
    
    private ClassDescriptorResolver _cdResolver = null;
    
    private static Object[] emptyParams = new Object[0];
    
    
    /**
     * Creates a new Validator
    **/
    public Validator() {
        super();
    } //-- Validator
    
    /**
     * Sets the ClassDescriptorResolver to use for finding XMLClassDescriptors
     * @param cdResolver the ClassDescriptorResolver to use
    **/

    public void setResolver(ClassDescriptorResolver cdResolver) {
        this._cdResolver = cdResolver;
    } //-- setResolver
    
    /**
     * Validates an Object model, MarshalInfo classes will be used
     * to perform Validation. If no MarshalInfo class exists, one
     * will be dynamically created
     * @param the given Object in which to validate
    **/
    public void validate(Object object) 
        throws ValidationException 
    {
        
        if (object == null) {
            throw new ValidationException("cannot validate a null Object.");
        } 
        
        ClassDescriptorResolver cdResolver = _cdResolver;
        if (cdResolver == null)
            cdResolver = new ClassDescriptorResolverImpl();
      
        XMLClassDescriptor classDesc = cdResolver.resolve(object.getClass());
        
        //-- we cannot validate object if ClassDescriptor is null
        if (classDesc == null) return;
        
        TypeValidator validator = classDesc.getValidator();
        
        XMLFieldDescriptor fieldDesc = null;
        
        try {
            if (validator != null)
                validator.validate(object);
            //-- default validation
            else {
                //-- just validate each field
                FieldDescriptor[] fields = classDesc.getFields();
                if (fields != null) {
                    for (int i = 0; i < fields.length; i++) {
                        fieldDesc = (XMLFieldDescriptor)fields[i];
                        if (fieldDesc == null) continue;
                        validate(object, fieldDesc);
                    }
                }
            }
        }
        catch (ValidationException vx) {
            //-- add location information
            XPathLocation loc = (XPathLocation)vx.getLocation();
            if (loc == null) {
                loc = new XPathLocation();
                vx.setLocation(loc);
                if (fieldDesc != null) {
                    if (fieldDesc.getNodeType() == NodeType.Attribute)
                        loc.addAttribute(fieldDesc.getXMLName());
                    else
                        loc.addChild(fieldDesc.getXMLName());
                }
            }
            loc.addParent(classDesc.getXMLName());
            throw vx;
        }
        
    } //-- validate
    
    /**
     * Validates the field described by the given FieldDescriptor
     * @param parent the object containing the field to validate
     * @param fieldDesc the XMLFieldDescriptor of the field to validate
    **/
    private void validate(Object parent, XMLFieldDescriptor fieldDesc) 
        throws ValidationException
    {
            
       FieldValidator validator = fieldDesc.getValidator();
        
        if (validator != null)
            validator.validate(parent, this);
        /*
        //-- do default validation
        else {
            FieldHandler handler = fieldDesc.getHandler();
            if (handler != null) {
                Object value = handler.getValue(parent);
                if (fieldDesc.isRequired()) {
                    if (value == null) {
                        String err = "The xml field: " + fieldDesc.getXMLName();
                        err += " is a required field, but it's value is null.";
                        throw new ValidationException(err);
                    }
                }
                //-- recursively handle validation
                if ( value != null )
                    validate(value);
            }
        }
        */
        
    } //-- validate
    
    /*
      *** Moved to FieldValidator ***
      
    private void validate
        (Object object, ValidationRule vRule, MarshalDescriptor desc) 
        throws ValidationException
    {
        
        int min = vRule.getMinOccurs();
        int max = vRule.getMaxOccurs();
        
        boolean required = (min > 0);
        
        Object val = null;
        try {
            val = desc.getValue(object);
        }
        catch(Exception ex) {
            throw new ValidationException(ex.getMessage());
        }
        
        if ((val == null) && (required)) {
            String err = desc.getXMLName();
            err += " is a required field.";
            throw new ValidationException(err);
        }
        
        if (val != null) {
            Class type = val.getClass();
            
            int size = 1;
            boolean byteArray = false;
            if (type.isArray()) {
                byteArray = (type.getComponentType() == Byte.TYPE);
                if (!byteArray) size = Array.getLength(val);
            }
            
            //-- check minimum
            if (size < min) {
                String err = "A minimum of " + min + " ";
                err += desc.getXMLName() + " object(s) are required.";
                throw new ValidationException(err);
            }
            
            //-- check maximum
            if ((max >= 0) && (size > max)) {
                String err = "A maximum of " + max + " ";
                err += desc.getXMLName() + " object(s) are required.";
                throw new ValidationException(err);
            }
            
            //-- check type
            if (isPrimitive(type) || (type == String.class)) {
                TypeValidator typeValidator = vRule.getTypeValidator();
                if (typeValidator != null) {
                    typeValidator.validate(val);
                }
            }
            else if (byteArray) { 
                //-- do nothing for now
            }
            else if (type.isArray()) {
                size = Array.getLength(val);
                for (int i = 0; i < size; i++) {
                    validate(Array.get(val, i));
                }
            }
            else if (val instanceof java.util.Enumeration) {
                Enumeration enum = (Enumeration)val;
                while (enum.hasMoreElements())
                    validate(enum.nextElement());
            }
            else if (val instanceof java.util.Vector) {
                Vector vector = (Vector)val;
                for (int i = 0; i < vector.size(); i++) {
                    validate(vector.elementAt(i));
                }
            }
            else validate(val);
                
        }
    } //-- validate(Object, ValidationRule, MarshalDescriptor)
        
    */
    
    /**
     * Validates an Object model, ClassDescriptor classes will be used
     * to perform Validation. If no ClassDescriptor class exists, one
     * will be dynamically created
     * @param the given Object in which to validate
     * @param cdResolver the ClassDescriptorResolver used for finding
     * XMLClassDescriptors, this may be null
    **/
    public static void validate
        (Object object, ClassDescriptorResolver cdResolver) 
        throws ValidationException
    {
        Validator validator = new Validator();
        validator.setResolver(cdResolver);
        validator.validate(object);
    } //-- validate
    
    
    
} //-- Validator
