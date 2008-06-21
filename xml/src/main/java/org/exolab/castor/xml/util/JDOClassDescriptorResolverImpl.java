package org.exolab.castor.xml.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.exolab.castor.mapping.ClassDescriptor;
import org.exolab.castor.mapping.MappingLoader;
import org.exolab.castor.xml.ClassDescriptorResolver;
import org.exolab.castor.xml.ResolverException;
import org.exolab.castor.xml.util.resolvers.ResolveHelpers;

/**
 * JDO-specific {@link ClassDescriptorResolver} instance that provides functionality
 * to find or "resolve" {@link ClassDescriptor}s from a given class (name).
 */
public class JDOClassDescriptorResolverImpl implements ClassDescriptorResolver {

    /**
     * File name prefix used for JDO-specific descriptor classes.
     */
    private static final String JDO_DESCRIPTOR_PREFIX = "JDO";

    /**
     * File name suffix used for JDO-specific descriptor classes.
     */
    private static final String JDO_DESCRIPTOR_SUFFIX = "Descriptor";

    /**
     * Package name of the sub-package where descriptors can be found.
     */
    private static final String JDO_DESCRIPTOR_PACKAGE = "descriptors";
    
    /**
     * A key ({@link Class}) value ({@link ClassDescriptor}) 
     * pair to cache already resolved JDO class descriptors. 
     */
    private Map _classDescriptorCache = new HashMap();

    /**
     * A {@link MappingLoader} instance which this {@link ClassDescriptorResolver} instance
     * turns to to load {@link ClassDescriptor} instances as defined in a Castor
     * JDO mapping file.
     */
    private MappingLoader _mappingLoader;

    /**
     * List of manually added domain <code>Class</code>es.
     */
    private List _classes = new LinkedList();

    /**
     * List of manually added package names.
     */
    private List _packages = new LinkedList();

    /**
     * Creates an instance of this class, with no classed manually added.
     */
    public JDOClassDescriptorResolverImpl() {
        super();
    }

    /**
     * Returns the ClassDescriptor for the given class using the following
     * strategy.<br><br>
     * <ul>
     * <li>Lookup the class descriptor cache
     * <li>Call {@link JDOClassDescriptorResolverImpl#loadClassDescriptor(Class)}
     * <li>Ask the {@link MappingLoader}
     * </ul>
     * 
     * @param type
     *            the Class to find the ClassDescriptor for
     * @exception ResolverException Indicates that the given {@link Class} 
     *            cannot be resolved.
     * @return the ClassDescriptor for the given class
     */
    public ClassDescriptor resolve(final Class type) throws ResolverException {
        if (type == null) {
            return null;
        }

        ClassDescriptor classDesc;

        // TODO: use ClassDescriptor instead
        // 1) consult with cache
        classDesc = (ClassDescriptor) _classDescriptorCache.get(type);
        if (classDesc != null) {
            return classDesc;
        }

        // 2) load ClassDescriptor from file system
        classDesc = loadClassDescriptor(type);
        if (classDesc != null) {
            return classDesc;
        }

        // 3) load ClassDescriptor from mapping loader
        if (_mappingLoader != null) {
            classDesc = _mappingLoader.getDescriptor(type.getName());
            if (classDesc != null) {
                _classDescriptorCache.put(type, classDesc);
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

    /**
     * Tries to load a {@link ClassDescriptor} for the given type 
     * from the filesystem using a {@link ClassLoader} by
     * <ul>
     * <li>Lookup the same package</li>
     * <li>Lookup the subpackage <code>descriptors</code></li>
     * </ul>.
     * 
     * @param type to lookup the descriptor for.
     * @return an instance of ClassDescriptor if found. null if not.
     */
    private ClassDescriptor loadClassDescriptor(final Class type) {
        ClassDescriptor classDesc = null;
        ClassLoader classLoader = type.getClassLoader();
        StringBuffer descriptorClassName = new StringBuffer(type.getName());
        descriptorClassName.append(JDO_DESCRIPTOR_PREFIX);
        descriptorClassName.append(JDO_DESCRIPTOR_SUFFIX);
        // Try to load descriptor in the same package
        Class descriptorClass = ResolveHelpers.loadClass(
                classLoader, descriptorClassName.toString());

        // If descriptor was not found, lookup the descriptor package
        if (descriptorClass == null) {
            int offset = descriptorClassName.lastIndexOf(".");
            if (offset != -1) {
                descriptorClassName.insert(offset , ".");
                descriptorClassName.insert(offset + 1, JDO_DESCRIPTOR_PACKAGE);
                descriptorClass = ResolveHelpers.loadClass(
                        classLoader, descriptorClassName.toString());
            }
        }
        
        // Descriptor was found, instantiate and return it
        if (descriptorClass != null) {
            try {
                classDesc = (ClassDescriptor) descriptorClass.newInstance();
                // Put descriptor into cache
                _classDescriptorCache.put(type.getName(), classDesc);
                return classDesc;
            } catch (InstantiationException e) {
                new RuntimeException(e.getMessage());
            } catch (IllegalAccessException e) {
                new RuntimeException(e.getMessage());
            }
        }
        return classDesc;
    }

    /**
     * {@inheritDoc}
     * @see org.exolab.castor.xml.ClassDescriptorResolver#getMappingLoader()
     */
    public MappingLoader getMappingLoader() {
        return _mappingLoader;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.exolab.castor.xml.ClassDescriptorResolver#setMappingLoader(org.exolab.castor.mapping.MappingLoader)
     */
    public void setMappingLoader(final MappingLoader mappingLoader) {
        this._mappingLoader = mappingLoader;
    }

    /**
     * Adds a given {@link Class} instance manually, so that it can be loaded from 
     * the file system.
     * @param domainClass A given {@link Class} instance.
     */
    public void addClass(final Class domainClass) {
        _classes.add(domainClass);
    }

    /**
     * Adds a given package name manually, so that class descriptors can be loaded from 
     * this package (from the file system).
     * @param packageName A given package name.
     */
    public void addPackage(final String packageName) {
        _packages.add(packageName);
    }

    /**
     * Returns an iterator over all the known descriptors in the original order they have been
     * added. Each element is of type {@link ClassDescriptor}.
     * @return an {@link Iterator} over all the known JDO class descriptors.
     */
    public Iterator descriptorIterator() {
        List allDescriptors = new ArrayList();
        allDescriptors.addAll(_mappingLoader.getDescriptors());
        for (Iterator iterator = _classes.iterator(); iterator.hasNext(); ) {
            allDescriptors.add(loadClassDescriptor((Class) iterator.next()));
        }
        return allDescriptors.iterator();
    }
    
    /**
     * Returns the {@link ClassLoader} instance as used internally.
     * @return The {@link ClassLoader} instance used internally.
     */
    public ClassLoader getClassLoader() {
        return _mappingLoader.getClassLoader();
    }
}
