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
 * The ID Validation class.
 *
 * @author <a href="mailto:werner DOT guttmann AT gmx DOT net">Werner Guttman</a>
 * @version $Revision: 5951 $ $Date: 2003-03-03 02:57:21 -0700 (Mon, 03 Mar 2003) $
 */
public class IdValidator extends StringValidator implements TypeValidator {

    /**
     * Creates a new IdValidator with no restrictions.
     */
    public IdValidator() {
        super();
    } //-- IdValidator

    /**
     * Validates the given Object
     *
     * @param value
     *            the string to validate
     * @param context
     *            the ValidationContext
     * @throws ValidationException if the object fails validation.
     */
    public void validate(String value, ValidationContext context) throws ValidationException {
        super.validate(value, context);
    } //-- validate

    /**
     * Validates the given Object
     *
     * @param object the Object to validate
     * @throws ValidationException if the object fails validation.
     */
    public void validate(Object object) throws ValidationException {
        validate(object, (ValidationContext)null);
    } //-- validate

    /**
     * Validates the given Object
     *
     * @param object the Object to validate
     * @param context the ValidationContext
     * @throws ValidationException if the object fails validation.
     */
    public void validate(Object object, ValidationContext context) throws ValidationException {
        if (object == null) {
            String err = "IdValidator cannot validate a null object.";
            throw new ValidationException(err);
        }

        String value = null;
        if (!(object instanceof String)) {
            throw new ValidationException("IDs should be of type String");
        }

        value = (String) object;

        if (value.equals("")) {
            String err = "Invalud ID value: '' is not a valid value.";
            throw new ValidationException(err);
        }

        context.addID(value);

        // validate(value, context);
    } //-- validate

} //-- IdValidator
