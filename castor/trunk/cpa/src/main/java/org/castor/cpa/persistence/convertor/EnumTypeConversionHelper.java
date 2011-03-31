package org.castor.cpa.persistence.convertor;

/**
 * Can be used to get a type-safe enum constant value via its ordinal value.
 */
public class EnumTypeConversionHelper {

    private final Class<?> _enumType;

    public EnumTypeConversionHelper(final Class<?> enumType) {
        _enumType = enumType;
    }

    public Class<?> getEnumConstantValueByOrdinal(final int ordinal) {
        return (Class<?>) _enumType.getEnumConstants()[ordinal];
    }

}
