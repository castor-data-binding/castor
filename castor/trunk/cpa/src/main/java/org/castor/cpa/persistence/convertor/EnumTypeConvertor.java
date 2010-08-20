package org.castor.cpa.persistence.convertor;

import java.lang.reflect.Method;

/**
 * Custom convertor used to handle enum types.
 */
public class EnumTypeConvertor extends AbstractSimpleTypeConvertor {

    private final Method _method;

    public EnumTypeConvertor(final Class<?> fromType, final Class<?> toType,
                             final Method method) {
        super(fromType, toType);
        _method = method;
    }

    /**
     * {@inheritDoc}
     */
    public Object convert(final Object object) {
        try { // Invoking method for conversion via reflection.
            return _method.invoke(this.toType(), (String) object);
        } catch (Exception ex) {
            return null;
        }
    }

}