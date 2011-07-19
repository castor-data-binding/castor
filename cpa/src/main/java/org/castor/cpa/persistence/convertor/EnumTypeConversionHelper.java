package org.castor.cpa.persistence.convertor;

/**
 * Can be used to get a type-safe enum constant value via its ordinal value.
 * 
 * @author <a href="mailto:wguttmn AT codehaus DOT org">Werner Guttmann</a>
 * @version $Revision: 7134 $ $Date: 2006-04-25 15:08:23 -0600 (Tue, 25 Apr 2006) $
 */
public class EnumTypeConversionHelper {
    //-----------------------------------------------------------------------------------
    
    private final Class<?> _enumType;

    //-----------------------------------------------------------------------------------
    
    public EnumTypeConversionHelper(final Class<?> enumType) {
        _enumType = enumType;
    }
    
    //-----------------------------------------------------------------------------------

    public Class<?> getEnumConstantValueByOrdinal(final int ordinal) {
        return (Class<?>) _enumType.getEnumConstants()[ordinal];
    }

    //-----------------------------------------------------------------------------------
}
