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

import org.exolab.castor.xml.util.*;

import org.exolab.castor.mapping.FieldDescriptor;
import org.exolab.castor.mapping.FieldHandler;
import org.exolab.castor.mapping.ValidityException;

import java.util.Enumeration;
import java.util.Vector;
import java.lang.reflect.*;

/**
 * The class which performs Validation on an Object model
 * @author <a href="mailto:kvisco@exoffice.com">Keith Visco</a>
 * @version $Revision$ $Date$
**/
public class Validator {
    
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
        
        FieldDescriptor[] fields = classDesc.getFields();
        
        if (fields != null) {
            for (int i = 0; i < fields.length; i++) {
                FieldDescriptor fieldDesc = fields[i];
                if (fieldDesc == null) continue;
                FieldHandler handler = fieldDesc.getHandler();
                try {
                    if (handler != null) handler.checkValidity(object);
                }
                catch(ValidityException vx) {
                    throw new ValidationException(vx);
                }
            }
        }
        
    } //-- validate
    
    /*
    private void validate
        (Object object, FieldDescriptor fieldDesc) 
        throws ValidationException
    {
     
        //-- probably pointless, but just to be safe
        if (fieldDesc == null) return;
        
        switch(vRule.getType()) {
            case ValidationRule.ATTRIBUTE:
                MarshalDescriptor[] atts = mInfo.getAttributeDescriptors();
                for (int i = 0; i < atts.length; i++) {
                    if (atts[i].matches(vRule.getName())) {
                        validate(object, vRule, atts[i]);
                        break;
                    }
                }
                break;
            case ValidationRule.ELEMENT:
                MarshalDescriptor[] elements = mInfo.getElementDescriptors();
                for (int i = 0; i < elements.length; i++) {
                    if (elements[i].matches(vRule.getName())) {
                        validate(object, vRule, elements[i]);
                        break;
                    }
                }
                break;
            case ValidationRule.TEXT:
                MarshalDescriptor content = mInfo.getContentDescriptor();
                if (content != null) {
                    validate(object, vRule, content);
                }
                break;
            case ValidationRule.GROUP:
                GroupValidationRule gvr = (GroupValidationRule)vRule;
                Enumeration enum = gvr.rules();
                while (enum.hasMoreElements()) {
                    ValidationRule rule = (ValidationRule)enum.nextElement();
                    validate(object, rule, mInfo);
                }
                break;
            default:
                break;
        }
    } //-- validate
    
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
    
    //-------------------/
    //- Private Methods -/
    //-------------------/
    
    /**
     * Returns true if the given class type should be
     * treated as a primitive. Wrapper objects such
     * as java.lang.Integer, and java.lang.Float, will
     * be treated as primitives.
     * @param type the Class to check
     * @return true if the given class should be treated
     * as a primitive type.
    **/
    private boolean isPrimitive(Class type) {
        
        if (type.isPrimitive()) return true;
        
        if ((type == Boolean.class)   ||
            (type == Byte.class)      ||
            (type == Character.class) ||
            (type == Double.class)    ||
            (type == Float.class)     ||
            (type == Integer.class)   ||
            (type == Long.class)      ||
            (type == Short.class)) 
            return true;
            
       return false;
       
    } //-- isPrimitive
    
} //-- Validator
