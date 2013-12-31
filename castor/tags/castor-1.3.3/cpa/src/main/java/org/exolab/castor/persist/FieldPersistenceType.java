/*
 * Copyright 2010 Werner Guttmann
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
package org.exolab.castor.persist;

/**
 * Enumeration for field type in the context of persistence; it is distinguished between 
 * primitive fields, serializable fields and relational fields (one-to-one, one-to-many 
 * and many-to-many).
 * 
 * @author me
 * @since 1.3.2
 */
public enum FieldPersistenceType {

    PRIMITIVE(0), 
    SERIALIZABLE(1), 
    PERSISTANCECAPABLE(2), 
    ONE_TO_MANY(3), 
    MANY_TO_MANY(4);

    /**
     * Field value.
     */
    private final int _value;

    /**
     * Creates an instance of this enum instance.
     * 
     * @param value The numeric value.
     */
    private FieldPersistenceType(final int value) {
        _value = value;
    }

    /**
     * Method fromValue().
     * 
     * @param value The actual value as int.
     * @return the constant for this value.
     */
    public static FieldPersistenceType fromValue(final int value) {
        switch (value) {
        case 0:
            return PRIMITIVE;
        case 1:
            return SERIALIZABLE;
        case 2:
            return PERSISTANCECAPABLE;
        case 3:
            return ONE_TO_MANY;
        case 4:
            return MANY_TO_MANY;
        }

        throw new IllegalArgumentException(value
                + " is not a valid fiel dpersistence type.");
    }

    /**
     * Method fromValue.
     * 
     * @param value The actual value.
     * @return the constant for this value.
     */
    public static FieldPersistenceType fromValue(final short value) {
        return fromValue((int) value);
    }

    /**
     * Method value.
     * 
     * @return the value of this constant.
     */
    public short value() {
        return (short) this._value;
    }

}
