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

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

/**
 * This class is used as a factory to get an instance of a class with a given
 * value.
 * 
 */
public class PrimitiveObjectFactory {

    static private Map<Class<?>, PrimitiveObject> typeHandlers = new HashMap<Class<?>, PrimitiveObject>();

    static {
        typeHandlers.put(String.class, new PrimitiveString());

        typeHandlers.put(Enum.class, new PrimitiveEnum());

        typeHandlers.put(Integer.TYPE, new PrimitiveInteger());
        typeHandlers.put(Integer.class, new PrimitiveInteger());

        typeHandlers.put(Boolean.TYPE, new PrimitiveBoolean());
        typeHandlers.put(Boolean.class, new PrimitiveBoolean());

        typeHandlers.put(Double.TYPE, new PrimitiveDouble());
        typeHandlers.put(Double.class, new PrimitiveDouble());

        typeHandlers.put(Long.TYPE, new PrimitiveLong());
        typeHandlers.put(Long.class, new PrimitiveLong());

        typeHandlers.put(Character.TYPE, new PrimitiveChar());
        typeHandlers.put(Character.class, new PrimitiveChar());

        typeHandlers.put(Short.TYPE, new PrimitiveShort());
        typeHandlers.put(Short.class, new PrimitiveShort());

        typeHandlers.put(Float.TYPE, new PrimitiveFloat());
        typeHandlers.put(Float.class, new PrimitiveFloat());

        typeHandlers.put(Byte.TYPE, new PrimitiveByte());
        typeHandlers.put(Byte.class, new PrimitiveByte());

        typeHandlers.put(BigInteger.class, new PrimitiveBigInteger());

        typeHandlers.put(BigDecimal.class, new PrimitiveBigDecimal());
    }
    
    /**
     * returns an instantiated Object
     * 
     * @return
     */
    public static Object getObject(Class<?> type, String value) {

        PrimitiveObject handler = lookupHandler(type);

        if (handler == null) {
            handler = getDefaultHandler();
        }

        handler.setType(type);
        handler.setValue(trimNumericValues(value));

        return handler.getObject();
    }

    /**
     * Looks up a handler from the map for the given type. <br>
     * Returns null if there isn't any suitable handler.
     * 
     * @param type
     * @return a handler to instantiate the given class
     */
    private static PrimitiveObject lookupHandler(Class<?> type) {

        if (type == null) {
            return null;
        }

        PrimitiveObject result = typeHandlers.get(type);

        if (result == null) {
            result = typeHandlers.get(type.getSuperclass());
        }

        return result;
    }

    /**
     * Trim any numeric values
     * 
     * @param value
     *            Value to be trimmed, can be null
     * @return trimmed value or null
     */
    private static String trimNumericValues(String value) {
        if (value == null) {
            return null;
        }

        return value.trim();
    }

    /**
     * returns a default Handler for Object instantiation
     * 
     * @return primitiveObject Primitive
     */
    private static PrimitiveObject getDefaultHandler() {
        return new PrimitiveObject();
    }

}
