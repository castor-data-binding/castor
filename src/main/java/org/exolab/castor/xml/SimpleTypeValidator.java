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
 * Copyright 1999 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id$
 */
package org.exolab.castor.xml;

import java.lang.reflect.Array;
import java.util.Enumeration;
import java.util.Vector;

/**
 * A class for defining simple rules used for validating a content model.
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a>
 * @version $Revision$ $Date: 2004-10-01 07:25:46 -0600 (Fri, 01 Oct 2004) $
 */
public class SimpleTypeValidator implements TypeValidator {

    /** The minimum number of occurences allowed. */
    private int minOccurs = 0;
    /** The maximum number of occurences allowed. */
    private int maxOccurs = -1;
    /** The type validate to delegate validation to. */
    private TypeValidator validator = null;

    /**
     * Creates a default SimpleTypeValidator.
     */
    public SimpleTypeValidator() {
        super();
    }

    /**
     * Creates a SimpleTypeValidator using the given TypeValidator for
     * delegating validation.
     * @param validator The TypeValidator to use
     */
    public SimpleTypeValidator(final TypeValidator validator) {
        super();
        this.validator = validator;
    }

    /**
     * Sets the maximum number of times that the described field may occur.
     *
     * @param maxOccurs the maximum number of times that the described field may
     *        occur.
     */
    public void setMaxOccurs(final int maxOccurs) {
        this.maxOccurs = maxOccurs;
    }

    /**
     * Sets the minimum number of times that the described field may occur.
     *
     * @param minOccurs the minimum number of times that the described field may
     *        occur.
     */
    public void setMinOccurs(final int minOccurs) {
        this.minOccurs = minOccurs;
    }

    /**
     * Sets the TypeValidator to delegate validation to.
     *
     * @param validator the TypeValidator to delegate validation to.
     */
    public void setValidator(final TypeValidator validator) {
        this.validator = validator;
    }

    /**
     * Validates the given Object.
     *
     * @param object the Object to validate.
     * @param context the ValidationContext.
     * @throws ValidationException if validation fails.
     */
    public void validate(final Object object, final ValidationContext context) throws ValidationException {
        boolean required = (minOccurs > 0);

        if (object == null && required) {
            String err = "This field is required and cannot be null.";
            throw new ValidationException(err);
        }

        if (object != null) {
            Class type = object.getClass();

            int size = 1;
            boolean byteArray = false;
            if (type.isArray()) {
                byteArray = (type.getComponentType() == Byte.TYPE);
                if (!byteArray) {
                    size = Array.getLength(object);
                }
            }

            //-- check minimum
            if (size < minOccurs) {
                String err = "A minimum of " + minOccurs
                             + " instance(s) of this field is required.";
                throw new ValidationException(err);
            }

            //-- check maximum
            if (maxOccurs >= 0 && size > maxOccurs) {
                String err = "A maximum of " + maxOccurs
                             + " instance(s) of this field are allowed.";
                throw new ValidationException(err);
            }

            if (validator == null) {
                return;
            }

            //-- check type
            if (isPrimitive(type) || (type == String.class)) {
                validator.validate(object, context);
            } else if (byteArray) {
                //-- do nothing for now
            } else if (type.isArray()) {
                size = Array.getLength(object);
                for (int i = 0; i < size; i++) {
                    validator.validate(Array.get(object, i), context);
                }
            } else if (object instanceof java.util.Enumeration) {
                Enumeration enumeration = (Enumeration)object;
                while (enumeration.hasMoreElements()) {
                    validator.validate(enumeration.nextElement(), context);
                }
            } else if (object instanceof java.util.Vector) {
                Vector vector = (Vector)object;
                for (int i = 0; i < vector.size(); i++) {
                    validator.validate(vector.elementAt(i), context);
                }
            } else {
                validator.validate(object, context);
            }
        }
    }

    //-------------------/
    //- Private Methods -/
    //-------------------/

    /**
     * Returns true if the given class type should be treated as a primitive.
     * Wrapper objects such as java.lang.Integer, and java.lang.Float, will be
     * treated as primitives.
     *
     * @param type the Class to check
     * @return true if the given class should be treated as a primitive type.
     */
    private boolean isPrimitive(final Class type) {
        if (type.isPrimitive()) {
            return true;
        }

        return (type == Boolean.class || type == Byte.class || type == Character.class
                || type == Double.class || type == Float.class || type == Integer.class
                || type == Long.class || type == Short.class);
    }

}
