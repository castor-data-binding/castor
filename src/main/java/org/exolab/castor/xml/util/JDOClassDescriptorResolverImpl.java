package org.exolab.castor.xml.util;

import java.util.HashMap;
import java.util.Map;

import org.exolab.castor.mapping.ClassDescriptor;
import org.exolab.castor.mapping.MappingLoader;
import org.exolab.castor.xml.ClassDescriptorResolver;
import org.exolab.castor.xml.ResolverException;

public class JDOClassDescriptorResolverImpl 
    implements ClassDescriptorResolver {

    private Map _classToClassDescriptors = new HashMap();
    private MappingLoader _mappingLoader;
    
    public JDOClassDescriptorResolverImpl() {
        super();
    }
    /**
     * Returns the ClassDescriptor for the given class
     * 
     * @param type the Class to find the ClassDescriptor for
     * @return the ClassDescriptor for the given class
     */
    public ClassDescriptor resolve(Class type) 
    throws ResolverException
    {
        
        if (type == null) {
            return null;
        }
        
        // TODO: use ClassDescriptor instead
        ClassDescriptor classDesc = (ClassDescriptor) _classToClassDescriptors.get(type);
        
        if (classDesc != null) {
            return classDesc;
        }
        
        // -- check mapping loader first
        if (_mappingLoader != null) {            
            classDesc = _mappingLoader.getDescriptor(type.getName());
            if (classDesc != null) {
                _classToClassDescriptors.put(type, classDesc);
                return classDesc;
            }
        }

        // TODO: consider for future extensions

// String pkgName = getPackageName(type.getName());
//        
// //-- check package mapping
// Mapping mapping = loadPackageMapping(pkgName, type.getClassLoader());
// if (mapping != null) {
// try {
// MappingLoader mappingLoader = mapping.getResolver(BindingType.JDO);
// classDesc = mappingLoader.getDescriptor(type);
// }
// catch(MappingException mx) {}
// if (classDesc != null) {
// associate(type, classDesc);
// return classDesc;
// }
// }
        
        return classDesc;
    } // -- resolve
    
    public MappingLoader getMappingLoader()
    {
        return _mappingLoader;
    }

    public void setMappingLoader(MappingLoader mappingLoader)
    {
        this._mappingLoader = mappingLoader;
    }

    
}