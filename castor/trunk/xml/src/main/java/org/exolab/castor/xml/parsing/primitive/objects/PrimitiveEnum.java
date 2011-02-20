/*
 * Copyright 2005 Philipp Erlacher
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
package org.exolab.castor.xml.parsing.primitive.objects;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.commons.lang.StringUtils;

/**
 * This class is part of the command pattern implementation to instantiate an
 * object. It is used as a command by the command invoker {@link PrimitiveObject}.
 * 
 * @author <a href="mailto:philipp DOT erlacher AT gmail DOT com">Philipp
 *         Erlacher</a>
 * 
 */
class PrimitiveEnum extends PrimitiveObject {

    @Override
    public Object getObject() {
        if (StringUtils.isEmpty(value)) {
            return null;
        }

        // discover the fromValue Method
        try {
            Method valueOfMethod = type.getMethod("fromValue",
                    new Class[] { String.class });
            return valueOfMethod.invoke(null, new Object[] { value });

        } catch (NoSuchMethodException e) {
            // do nothing, check valueOf method
        } catch (IllegalArgumentException e) {
            throw new IllegalStateException(e.toString());
        } catch (IllegalAccessException e) {
            throw new IllegalStateException(e.toString());
        } catch (InvocationTargetException e) {
            if (e.getTargetException() instanceof RuntimeException) {
                throw (RuntimeException) e.getTargetException();
            }
        }

        // backwards compability, check valueOf method to support
        // "simple" enums without value object
        try {
            Method valueOfMethod = type.getMethod("valueOf",
                    new Class[] { String.class });
            return valueOfMethod.invoke(null, new Object[] { value });

        } catch (IllegalAccessException e) {
            throw new IllegalStateException(e.toString());
        } catch (InvocationTargetException e) {
            if (e.getTargetException() instanceof RuntimeException) {
                throw (RuntimeException) e.getTargetException();
            }
        } catch (NoSuchMethodException e) {
            String err = type.getName()
                    + " does not contain the required method: public static "
                    + type.getName() + " valueOf(String);";
            throw new IllegalArgumentException(err);
        }

        return value;
    }

}
