package org.castor.cpa.persistence.convertor;

public class EnumToOrdinal extends AbstractSimpleTypeConvertor {

    /**
     * Creates an instance of this class.
     */
    public EnumToOrdinal() {
        super(Enum.class, int.class);
    }

    /**
     * {@inheritDoc}
     */
    public Object convert(final Object object) {
        try {
            return object.getClass().getMethod("ordinal").invoke(object);
        } catch (Exception ex) {
            return null;
        }
    }

}
