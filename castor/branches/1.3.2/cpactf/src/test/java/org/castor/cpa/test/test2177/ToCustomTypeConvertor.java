package org.castor.cpa.test.test2177;

import org.castor.cpa.persistence.convertor.AbstractSimpleTypeConvertor;
import org.junit.Ignore;

@Ignore
public final class ToCustomTypeConvertor extends AbstractSimpleTypeConvertor {
    public ToCustomTypeConvertor() {
        super(String.class, CustomType.class);
    }

    public Object convert(final Object object) {
        return new CustomType((String) object);
    }
}
