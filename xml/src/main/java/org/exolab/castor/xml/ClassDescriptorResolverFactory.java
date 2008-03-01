package org.exolab.castor.xml;

import org.castor.mapping.BindingType;
import org.exolab.castor.xml.util.JDOClassDescriptorResolverImpl;
import org.exolab.castor.xml.util.XMLClassDescriptorResolverImpl;
import org.exolab.castor.xml.util.resolvers.CastorXMLStrategy;

public class ClassDescriptorResolverFactory {

    public static ClassDescriptorResolver createClassDescriptorResolver(BindingType type) {
        if (type == BindingType.JDO) {
            return new JDOClassDescriptorResolverImpl();
        } else if (type == BindingType.XML) {
            XMLClassDescriptorResolver resolver = new XMLClassDescriptorResolverImpl();
            // for cases in which users really work with the factory only and not
            // with any kind of InternalContext...
            resolver.setResolverStrategy(new CastorXMLStrategy());
            return resolver;
        }
        return null;
    }
}
