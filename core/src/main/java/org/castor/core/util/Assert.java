/*
 * Copyright 2011 Jakub Narloch
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
package org.castor.core.util;

/**
 * A helper class that defines a method used for validating the input arguments.
 *
 * @author <a href="mailto:jmnarloch AT gmail DOT com">Jakub Narloch</a>
 * @version 1.3.3
 * @since 1.3.3
 */
public class Assert {

    /**
     * Represents the error message used as exception description when a parameter is null.
     */
    private static final String NULL_PARAM_MESSAGE = "Parameter %s can not be null.";

    /**
     * Represents the error message used as exception description when a parameter is null or empty string.
     */
    private static final String EMPTY_PARAM_MESSAGE = "Parameter %s can not be null or empty.";

    /**
     * Creates new instance of {@link Assert} class. </p> Private constructor prevents instantiation outside this
     * class.
     */
    private Assert() {
        // empty constructor
    }

    /**
     * Asserts that passed parameter is not null. In case it is, then an 
     * {@link IllegalArgumentException} is thrown.
     *
     * @param param the parameter to check
     * @param paramName the parameter name
     *
     * @throws IllegalArgumentException if param is null
     */
    public static void paramNotNull(Object param, String paramName) {
        notNull(param, String.format(NULL_PARAM_MESSAGE, paramName));
    }

    /**
     * Asserts that passed parameter is not null and not empty. In case it is, 
     * then an {@link IllegalArgumentException} is thrown.
     *
     * @param param the parameter to check
     * @param paramName the parameter name
     *
     * @throws IllegalArgumentException if param is null or empty string
     */
    public static void paramNotEmpty(String param, String paramName) {
        notEmpty(param, String.format(EMPTY_PARAM_MESSAGE, paramName));
    }

    /**
     * Asserts that passed object instance is not null. In case it is, 
     * then an {@link IllegalArgumentException} is thrown.
     *
     * @param obj the object instance to check
     * @param msg the error message to use
     *
     * @throws IllegalArgumentException if obj is null
     */
    public static void notNull(Object obj, String msg) {
        if (obj == null) {
            throw new IllegalArgumentException(msg);
        }
    }

    /**
     * Asserts that passed object instance is not null and not empty. In case 
     * it is, then an {@link IllegalArgumentException} is thrown.
     *
     * @param obj the object instance to check
     * @param msg the error message to use
     *
     * @throws IllegalArgumentException if obj is null or empty string
     */
    public static void notEmpty(String obj, String msg) {
        notNull(obj, msg);
        if (obj.trim().length() == 0) {
            throw new IllegalArgumentException(msg);
        }
    }
}
