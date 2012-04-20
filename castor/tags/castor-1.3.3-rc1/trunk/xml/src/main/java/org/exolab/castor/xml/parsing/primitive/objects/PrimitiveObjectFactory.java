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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This class is used as a factory to get an instance of a class with a given
 * value.
 */
public class PrimitiveObjectFactory {

    private Map<Class<?>, Class<? extends PrimitiveObject>> typeHandlers = new HashMap<Class<?>, Class<? extends PrimitiveObject>>();

    private final Log logger = LogFactory.getLog(this.getClass());
    
    private static PrimitiveObjectFactory primitiveObjectFactory;
    
    public static synchronized PrimitiveObjectFactory getInstance() {
       if (primitiveObjectFactory == null) {
          primitiveObjectFactory = new PrimitiveObjectFactory();
       }
       return primitiveObjectFactory;
    }
    
    private PrimitiveObjectFactory() {
        typeHandlers.put(String.class, PrimitiveString.class);

        typeHandlers.put(Enum.class, PrimitiveEnum.class);

        typeHandlers.put(Integer.TYPE, PrimitiveInteger.class);
        typeHandlers.put(Integer.class, PrimitiveInteger.class);

        typeHandlers.put(Boolean.TYPE, PrimitiveBoolean.class);
        typeHandlers.put(Boolean.class, PrimitiveBoolean.class);

        typeHandlers.put(Double.TYPE, PrimitiveDouble.class);
        typeHandlers.put(Double.class, PrimitiveDouble.class);

        typeHandlers.put(Long.TYPE, PrimitiveLong.class);
        typeHandlers.put(Long.class, PrimitiveLong.class);

        typeHandlers.put(Character.TYPE, PrimitiveChar.class);
        typeHandlers.put(Character.class, PrimitiveChar.class);

        typeHandlers.put(Short.TYPE, PrimitiveShort.class);
        typeHandlers.put(Short.class, PrimitiveShort.class);

        typeHandlers.put(Float.TYPE, PrimitiveFloat.class);
        typeHandlers.put(Float.class, PrimitiveFloat.class);

        typeHandlers.put(Byte.TYPE, PrimitiveByte.class);
        typeHandlers.put(Byte.class, PrimitiveByte.class);

        typeHandlers.put(BigInteger.class, PrimitiveBigInteger.class);

        typeHandlers.put(BigDecimal.class, PrimitiveBigDecimal.class);
    }
    
    /**
     * returns an instantiated Object
     * 
     * @return
     */
    public Object getObject(Class<?> type, String value) {

        PrimitiveObject handler = lookupHandler(type);

        if (handler == null) {
            handler = getDefaultHandler();
        }

        if (type != String.class) {
            value = trimNumericValues(value);
        }

        return handler.getObject(type, value);
    }

    /**
     * Looks up a handler from the map for the given type. <br>
     * Returns null if there isn't any suitable handler.
     * 
     * @param type
     * @return a handler to instantiate the given class
     */
    private PrimitiveObject lookupHandler(Class<?> type) {
       
       PrimitiveObject instance = null;

        if (type == null) {
            return null;
        }

        Class<? extends PrimitiveObject> result = typeHandlers.get(type);

        if (result == null) {
            result = typeHandlers.get(type.getSuperclass());
        }

        if (result != null) {
           try {
              instance = result.newInstance();
           } catch (InstantiationException e) {
              this.logger.error("Problem instantiating an instance of " + result.getName());
              e.printStackTrace();
           } catch (IllegalAccessException e) {
              this.logger.error("Problem accessing default constructor of " + result.getName());
              e.printStackTrace();
           }
        }
        
        return instance;
    }

    /**
     * Trim any numeric values
     * 
     * @param value
     *            Value to be trimmed, can be null
     * @return trimmed value or null
     */
    private String trimNumericValues(String value) {
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
    private PrimitiveObject getDefaultHandler() {
        return new PrimitiveObject();
    }

}
