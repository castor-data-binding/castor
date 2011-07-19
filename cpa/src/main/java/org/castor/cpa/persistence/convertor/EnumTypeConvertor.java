package org.castor.cpa.persistence.convertor;

import java.lang.reflect.Method;

/**
 * Custom convertor used to handle enum types.
 * 
 * @author <a href="mailto:wguttmn AT codehaus DOT org">Werner Guttmann</a>
 * @version $Revision: 7134 $ $Date: 2006-04-25 15:08:23 -0600 (Tue, 25 Apr 2006) $
 */
public class EnumTypeConvertor extends AbstractSimpleTypeConvertor {
    //-----------------------------------------------------------------------------------
    
    private final Method _method;
    
    //-----------------------------------------------------------------------------------

    public EnumTypeConvertor(final Class<?> fromType, final Class<?> toType,
                             final Method method) {
        super(fromType, toType);
        _method = method;
    }
    
    //-----------------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    public final Object convert(final Object object) {
        try { // Invoking method for conversion via reflection.
            return _method.invoke(this.toType(), (String) object);
        } catch (Exception ex) {
            return null;
        }
    }

    //-----------------------------------------------------------------------------------
}