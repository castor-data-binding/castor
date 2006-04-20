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

import java.util.Enumeration;
import java.util.Vector;
import java.lang.reflect.*;

/**
 * The class which performs Validation on an Object model
 * @author <a href="mailto:kvisco@exoffice.com">Keith Visco</a>
 * @version $Revision$ $Date$
**/
public class Validator {
    
    private MarshalInfoResolver _mResolver = null;
    
    private static Object[] emptyParams = new Object[0];
    
    /**
     * Creates a new Validator
    **/
    public Validator() {
        super();
    } //-- Validator
    
    /**
     * Sets the MarshalInfoResolver used for finding MarshalInfo
     * classes
     * @param mResolver the MarshalInfoResolver used for finding
     * MarshalInfo classes
    **/

    public void setResolver(MarshalInfoResolver mResolver) {
        this._mResolver = mResolver;
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
        
        MarshalInfoResolver mResolver = _mResolver;
        if (mResolver == null)
            mResolver = new CachingMarshalInfoResolver();
      
        MarshalInfo mInfo = mResolver.resolve(object.getClass());
        
        ValidationRule[] rules = mInfo.getValidationRules();
        
        if (rules != null) {
            for (int i = 0; i < rules.length; i++)
                validate(object, rules[i], mInfo);
        }
        
    } //-- validate
    
    private void validate
        (Object object, ValidationRule vRule, MarshalInfo mInfo) 
        throws ValidationException
    {
     
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
            Class _class = val.getClass();
            
            int size = 1;
            if (_class.isArray()) {
                size = Array.getLength(val);
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
            if (_class.isPrimitive() || (_class == String.class)) {
                TypeValidator typeValidator = vRule.getTypeValidator();
                if (typeValidator != null) {
                    typeValidator.validate(val);
                }
            }
            else if (_class.isArray()) {
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
        
    
    /**
     * Validates an Object model, MarshalInfo classes will be used
     * to perform Validation. If no MarshalInfo class exists, one
     * will be dynamically created
     * @param the given Object in which to validate
     * @param mResolver the MarshalInfoResolver used for finding
     * MarshalInfo classes, this may be null
    **/
    public static void validate(Object object, MarshalInfoResolver mResolver) 
        throws ValidationException
    {
        Validator validator = new Validator();
        validator.setResolver(mResolver);
        validator.validate(object);
    } //-- validate
    
} //-- Validator