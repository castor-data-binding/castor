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
package org.exolab.castor.xml;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

/**
 * This class can be used to instantiate a class. It's also possible to
 * instantiate a class with a given value.
 * <p>
 * To do that first call initHandlers() to create a map with classes and types
 * as key and a function that describes how that class is going to be created.<br>
 * Second call setType. Third call getObject.
 * </p>
 * This class uses the command pattern. It implements the abstract Command and
 * is used as Command Invoker
 * 
 * @author <a href="mailto:philipp DOT erlacher AT gmail DOT com">Philipp
 *         Erlacher</a>
 * 
 */
public class PrimitiveObject {

    Class<?> type;
    String value;
    Object object;

    static private Map<Class<?>, PrimitiveObject> typeHandlers = null;

    public PrimitiveObject() {
        super();
        initHandlers();
    }

    /**
     * Creates a map with a classes and types as keys and handlers as values.
     */
    private void initHandlers() {

        if (typeHandlers != null) {
            return;
        }

        typeHandlers = new HashMap<Class<?>, PrimitiveObject>();

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
     * Sets the type and value for the class which should be instantiated
     * 
     * @param type
     *            Class
     * @param value
     *            String
     */
    public void setType(final Class<?> type) {
        this.type = type;
    }

    public void setValue(String value) {
        this.value = value;
        this.object = value;
    }

    /**
     * returns an instantiated Object
     * 
     * @return
     */
    public Object getObject() {

        value = trimNumericValues();

        PrimitiveObject handler = lookupHandler(type);

        if (handler == null) {
            handler = getDefaultHandler();
        }

        handler.setType(type);
        handler.setValue(value);

        return handler.getObject();
    }

    /**
     * Trim any numeric values
     * 
     * @return trimmed value or null
     */
    private String trimNumericValues() {
        if (value == null) {
            return null;
        }

        return value.trim();
    }

    /**
     * Looks up a handler from the map for the given type. <br>
     * Returns null if there isn't any suitable handler.
     * 
     * @param type
     * @return a handler to instantiate the given class
     */
    private PrimitiveObject lookupHandler(Class<?> type) {

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
     * returns a default Handler for Object instantiation
     * 
     * @return
     */
    private PrimitiveObject getDefaultHandler() {
        return new PrimitiveDefault();
    }

    /**
     * checks if value is null or has length zero
     * 
     * @return
     */
    boolean isNull() {
        return ((value == null) || (value.length() == 0));
    }

}
