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

import org.exolab.castor.xml.TypeValidator;
import org.exolab.castor.xml.ValidationContext;
import org.exolab.castor.xml.ValidationException;

/**
 * The IDREFS Validation class.
 *
 * @author <a href="mailto:werner DOT guttmann AT gmx DOT net">Werner Guttman</a>
 * @version $Revision: 0000 $ $Date: $
 */
public class IdRefsValidator implements TypeValidator {

    /** Our IdRefValidator. */
    private IdRefValidator _idRefValidator;

    /**
     * Creates a new IdRefsValidator with no restrictions.
     */
    public IdRefsValidator() {
        super();
        _idRefValidator = new IdRefValidator();
    }

    /**
     * Validates the given Object.
     *
     * @param object the Object to validate
     * @param context the ValidationContext
     * @throws ValidationException if the object fails validation.
     */
    public void validate(final Object object, final ValidationContext context)
                                                    throws ValidationException {
        if (object == null) {
             String err = "The object of type IDREFS is null!";
             throw new ValidationException(err);
        }

        Object[] objects = (Object[]) object;
        for (int i = 0; i < objects.length; i++) {
            _idRefValidator.validate(objects[i], context);
        }

        // validate(value, context);
    } //-- validate

}
