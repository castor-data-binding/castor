package org.castor.cpa.persistence.convertor;

/**
 * Can be used to get a type-safe enum constant value via its ordinal value.
 */
public class EnumTypeConversionHelper {

    private final Class<?> enumType;

    public EnumTypeConversionHelper(final Class<?> enumType) {
        this.enumType = enumType;
    }

    public Class<?> getEnumConstantValueByOrdinal(int ordinal) {
        return (Class<?>) this.enumType.getEnumConstants()[ordinal];
    }

}
