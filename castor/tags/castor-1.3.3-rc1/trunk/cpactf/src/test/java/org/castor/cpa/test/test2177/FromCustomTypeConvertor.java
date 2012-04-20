package org.castor.cpa.test.test2177;

import org.castor.cpa.persistence.convertor.AbstractSimpleTypeConvertor;
import org.junit.Ignore;

@Ignore
public final class FromCustomTypeConvertor extends AbstractSimpleTypeConvertor {
    public FromCustomTypeConvertor() {
        super(CustomType.class, String.class);
    }

    public Object convert(final Object object) {
        if (object == null) {
            return null;
        }
        return ((CustomType) object).toString();
    }
}
