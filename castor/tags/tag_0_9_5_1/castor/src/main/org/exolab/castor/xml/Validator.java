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
 * Copyright 1999-2002 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id$
 */


package org.exolab.castor.xml;

import org.exolab.castor.xml.util.*;

import org.exolab.castor.mapping.FieldDescriptor;
import org.exolab.castor.mapping.FieldHandler;
import org.exolab.castor.mapping.ValidityException;

import java.lang.reflect.*;


/**
 * A class which can perform Validation on an Object model.
 * This class uses the ClassDescriptors and FieldDescriptors
 * to perform the validation.
 *
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a>
 * @version $Revision$ $Date$
 */
public class Validator implements ClassValidator {

    /**
     * Creates a new Validator
    **/
    public Validator() {
        super();
    } //-- Validator

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
     * @param context the ValidationContext to use during validation.
     */
    public void validate(Object object, ValidationContext context)
        throws ValidationException
    {

        if (object == null) {
            throw new ValidationException("Cannot validate a null Object.");
        }
        
        if (context == null) {
            context = new ValidationContext();
        }

        if (context.getResolver() == null) {
            context.setResolver(new ClassDescriptorResolverImpl());
        }

        XMLClassDescriptor classDesc = null;

        if (! MarshalFramework.isPrimitive(object.getClass()))
            classDesc = context.getResolver().resolve(object.getClass());

        //-- we cannot validate an object if ClassDescriptor is null
        if (classDesc == null) return;

        TypeValidator validator = classDesc.getValidator();

        XMLFieldDescriptor fieldDesc = null;

        try {
            if (validator != null) {
                validator.validate(object, context);
            }
            //-- default validation
            else {
                //-- just validate each field
                FieldDescriptor[] fields = classDesc.getFields();
                if (fields != null) {
                    for (int i = 0; i < fields.length; i++) {
                        fieldDesc = (XMLFieldDescriptor)fields[i];
                        if (fieldDesc == null) continue;
                        FieldValidator fieldValidator
                            = fieldDesc.getValidator();
                        if (fieldValidator != null)
                            fieldValidator.validate(object, context);
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
            if (classDesc.getXMLName() != null)
                 loc.addParent(classDesc.getXMLName());
            throw vx;
        }

    } //-- validate

    /**
     * Validates an Object model, ClassDescriptor classes will be used
     * to perform Validation. If no ClassDescriptor class exists, one
     * will be dynamically created
     * @param the given Object in which to validate
     * @param cdResolver the ClassDescriptorResolver used for finding
     * XMLClassDescriptors, this may be null
    **
    public static void validate
        (Object object, ClassDescriptorResolver cdResolver)
        throws ValidationException
    {
        Validator validator = new Validator();
        validator.setResolver(cdResolver);
        validator.validate(object);
    } //-- validate

    */

} //-- Validator
