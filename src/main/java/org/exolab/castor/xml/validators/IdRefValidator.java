/*
 * Copyright 2006 Werner Guttmann
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.exolab.castor.xml.validators;

import org.exolab.castor.mapping.ClassDescriptor;
import org.exolab.castor.mapping.FieldDescriptor;
import org.exolab.castor.mapping.FieldHandler;
import org.exolab.castor.xml.ClassDescriptorResolver;
import org.exolab.castor.xml.TypeValidator;
import org.exolab.castor.xml.ValidationContext;
import org.exolab.castor.xml.ValidationException;

/**
 * The IDREF Validation class.
 *
 * @author <a href="mailto:werner DOT guttmann AT gmx DOT net">Werner Guttman</a>
 * @version $Revision: 0000 $ $Date: $
 */
public class IdRefValidator implements TypeValidator {

    /**
     * Creates a new IdRefValidator with no restrictions.
     */
    public IdRefValidator() {
        super();
    } //-- IdValidator

    /**
     * Validates the given Object.
     *
     * @param object the Object to validate
     * @param context the ValidationContext
     * @throws ValidationException if the object fails validation.
     */
    public void validate(final Object object, final ValidationContext context)
    throws ValidationException {
        // we need a target Object
        if (object == null) {
            String err = "The object associated with IDREF \"" + object + "\" is null!";
            throw new ValidationException(err);
        }

        // get the id of the target object
        String id = null;
        try {
            ClassDescriptorResolver classDescriptorResolver = context.getResolver();
            ClassDescriptor classDescriptor = classDescriptorResolver.resolve(object.getClass());
            FieldDescriptor fieldDescriptor = classDescriptor.getIdentity();
            FieldHandler fieldHandler = fieldDescriptor.getHandler();
            id = (String) fieldHandler.getValue(object);
        } catch (Exception e) {
            String err = "The object associated with IDREF \"" + object
            + "\" of type " + object.getClass() + " has no ID!";
            throw new ValidationException(err);
        }

        if (id == null) {
            String err = "The object associated with IDREF \"" + object + "\" has no ID!";
            throw new ValidationException(err);
        }

        // check if referenced id exists, otherwise put it into "currently unresolved" queue
        context.checkIdRef(id);

        // validate(value, context);
    } //-- validate

}
