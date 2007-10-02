package ctf.jdo.special.test2177;

import org.castor.cpa.persistence.convertor.AbstractSimpleTypeConvertor;

public final class FromCustomTypeConvertor extends AbstractSimpleTypeConvertor {
    public FromCustomTypeConvertor() {
        super(CustomType.class, String.class);
    }

    public Object convert(final Object object) {
        if (object == null) { return null; }
        return ((CustomType) object).toString();
    }
}
