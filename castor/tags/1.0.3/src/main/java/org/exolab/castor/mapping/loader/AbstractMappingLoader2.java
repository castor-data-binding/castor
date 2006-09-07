package org.exolab.castor.mapping.loader;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.exolab.castor.mapping.ClassDescriptor;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.mapping.MappingLoader;
import org.exolab.castor.mapping.xml.MappingRoot;

public abstract class AbstractMappingLoader2 implements MappingLoader {
    //--------------------------------------------------------------------------

    /** The class loader to use. */
    private ClassLoader _loader;
    
    /** A flag indicating whether or not mappings can be redefined. */
    private boolean _allowRedefinitions = false;
    
    /** Has loadMapping been called? */
    private boolean _loaded = false;

    /** All class descriptors in the original order. */
    private List _descriptors = new Vector();

    /** All class descriptors added so far, keyed by classname. */
    private Map _descriptorsByClassname = new Hashtable();

    //--------------------------------------------------------------------------

    public AbstractMappingLoader2(final ClassLoader loader) {
        setClassLoader(loader);
    }
    
    public final void clear() {
        _allowRedefinitions = false;
        _loaded = false;
        _descriptors.clear();
        _descriptorsByClassname.clear();
    }

    //--------------------------------------------------------------------------

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

    //--------------------------------------------------------------------------
    
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
        return (ClassDescriptor) _descriptorsByClassname.get(classname);
    }

    /**
     * @see org.exolab.castor.mapping.MappingLoader#descriptorIterator()
     * {@inheritDoc}
     */
    public final Iterator descriptorIterator() {
        return _descriptors.iterator();
    }

    //--------------------------------------------------------------------------
    
    /**
     * Loads the mapping from the specified mapping object if not loaded previously.
     *
     * @param mapping The mapping information.
     * @param param Arbitrary parameter that can be used by subclasses.
     * @throws MappingException The mapping file is invalid.
     */
    public final void loadMapping(final MappingRoot mapping, final Object param)
    throws MappingException {
        if (!_loaded) {
            _loaded = true;
            
            loadMappingInternal(mapping, param);
        }
    }
    
    /**
     * Loads the mapping from the specified mapping object. Calls {@link #createDescriptor} to
     * create each descriptor and {@link #addDescriptor} to store it. Also loads all the included
     * mapping files.
     *
     * @param mapping The mapping information.
     * @param param Arbitrary parameter that can be used by subclasses.
     * @throws MappingException The mapping file is invalid.
     */
    protected abstract void loadMappingInternal(MappingRoot mapping, Object param)
    throws MappingException;
    
    //--------------------------------------------------------------------------
}
