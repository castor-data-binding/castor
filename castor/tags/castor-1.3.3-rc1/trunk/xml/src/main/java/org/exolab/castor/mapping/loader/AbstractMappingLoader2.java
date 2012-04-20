package org.exolab.castor.mapping.loader;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.exolab.castor.mapping.ClassDescriptor;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.mapping.MappingLoader;

public abstract class AbstractMappingLoader2 implements MappingLoader {

    /** The class loader to use. */
    private ClassLoader _loader;
    
    /** A flag indicating whether or not mappings can be redefined. */
    private boolean _allowRedefinitions = false;
    
    /** Has loadMapping been called? */
    private boolean _loaded = false;

    /** 
     * All class descriptors in the original order. 
     */
    private List<ClassDescriptor> _descriptors = new Vector<ClassDescriptor>();

    /** 
     * All class descriptors added so far, keyed by class name. 
     */
    private Map<String, ClassDescriptor> _descriptorsByClassname = 
        new Hashtable<String, ClassDescriptor>();

    public AbstractMappingLoader2(final ClassLoader loader) {
        setClassLoader(loader);
    }
    
    public final void clear() {
        _allowRedefinitions = false;
        _loaded = false;
        _descriptors.clear();
        _descriptorsByClassname.clear();
    }

    /**
     * @see org.exolab.castor.mapping.MappingLoader#setClassLoader(java.lang.ClassLoader)
     * {@inheritDoc}
     */
    public final void setClassLoader(final ClassLoader loader) {
        if (loader == null) {
            _loader = getClass().getClassLoader();
        } else {
            _loader = loader;
        }
    }

    /**
     * @see org.exolab.castor.mapping.MappingLoader#getClassLoader()
     * {@inheritDoc}
     */
    public final ClassLoader getClassLoader() {
        return _loader;
    }

    /**
     * Enables or disables the ability to allow the redefinition of class mappings.
     * 
     * @param allow A boolean that when true enables redefinitions.
     */
    public final void setAllowRedefinitions(boolean allow) {
        _allowRedefinitions = allow;
    }
    
    /**
     * Is the ability to allow redefinitions enabled or disabled?
     * 
     * @return A boolean that when true enables redefinitions.
     */
    public final boolean isAllowRedefinition() {
        return _allowRedefinitions;
    }

    /**
     * Adds a class descriptor. Will throw a mapping exception if a descriptor for this class
     * already exists.
     *
     * @param descriptor The descriptor to add.
     * @throws MappingException A descriptor for this class already exists.
     */
    protected final void addDescriptor(final ClassDescriptor descriptor)
    throws MappingException {
        String classname = descriptor.getJavaClass().getName();
        if (_descriptorsByClassname.containsKey(classname)) {
            if (!isAllowRedefinition()) {
                throw new MappingException("mapping.duplicateDescriptors", classname);
            }
            for (Iterator<ClassDescriptor> iterator = _descriptors.iterator(); iterator.hasNext(); ) {
                ClassDescriptor d = iterator.next();
                if (classname.equals(d.getJavaClass().getName())) {
                    iterator.remove();
                }
            }
            _descriptors.add(descriptor);
        } else {
            _descriptors.add(descriptor);
        }
        
        //-- if we make it here...add class
        _descriptorsByClassname.put(classname, descriptor);
    }

    /**
     * @see org.exolab.castor.mapping.MappingLoader#getDescriptor(java.lang.String)
     * {@inheritDoc}
     */
    public final ClassDescriptor getDescriptor(final String classname) {
        if (classname == null) { return null; }
        return _descriptorsByClassname.get(classname);
    }

//    /**
//     * @see org.exolab.castor.mapping.MappingLoader#descriptorIterator()
//     * {@inheritDoc}
//     */
//    public final Iterator<ClassDescriptor> descriptorIterator() {
//        return _descriptors.iterator();
//    }

    public final List<ClassDescriptor> getDescriptors() {
        return _descriptors;
    }
    
    /**
     * Return if mapping should be loaded with this MappingLoader instance or if another
     * mapping have been loaded previously. If no mapping have been loaded previously
     * then prevent any other mapping to be loaded later on.
     * 
     * @return <code>true</code> if mapping should be loaded, <code>false</code>
     *         otherwise.
     */
    protected final boolean loadMapping() {
        if (_loaded) { return false; }
        _loaded = true;
        return true;
    }
    
}
