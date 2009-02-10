package org.exolab.castor.xml;

import org.castor.mapping.BindingType;
import org.exolab.castor.xml.util.JDOClassDescriptorResolverImpl;
import org.exolab.castor.xml.util.XMLClassDescriptorResolverImpl;
import org.exolab.castor.xml.util.resolvers.CastorXMLStrategy;

/**
 * A factory that - based upon the binding type specified - returns
 * {@link ClassDescriptorResolver} instances.
 *  
 * @author <a href="mailto: wguttmn AT codehaus DOT org">Werner Guttmann</a>
 *
 */
public class ClassDescriptorResolverFactory {

    /**
     * Returns the matching {@link ClassDescriptorResolver} instance.
     * @param type A binding type.
     * @return A {@link ClassDescriptorResolver} instance.
     */
    public static ClassDescriptorResolver createClassDescriptorResolver(final BindingType type) {
        if (type == BindingType.JDO) {
            return new JDOClassDescriptorResolverImpl();
        } else if (type == BindingType.XML) {
            XMLClassDescriptorResolver resolver = new XMLClassDescriptorResolverImpl();
            // for cases in which users really work with the factory only and not
            // with any kind of InternalContext...
            resolver.setResolverStrategy(new CastorXMLStrategy());
            return resolver;
        }
        // TODO: throw IllegalArgumentException instead ? 
        return null;
    }
}
