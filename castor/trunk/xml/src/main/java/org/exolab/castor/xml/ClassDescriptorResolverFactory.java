package org.exolab.castor.xml;

import org.castor.mapping.BindingType;
import org.exolab.castor.xml.util.JDOClassDescriptorResolverImpl;
import org.exolab.castor.xml.util.XMLClassDescriptorResolverImpl;

public class ClassDescriptorResolverFactory {

    public static ClassDescriptorResolver createClassDescriptorResolver(BindingType type) {
        
        ClassDescriptorResolver resolver = null;
        
        if (type == BindingType.JDO) {
            resolver = new JDOClassDescriptorResolverImpl();
        } else if (type == BindingType.XML) {
            resolver = new XMLClassDescriptorResolverImpl();
        }
        return resolver;
    }
}
