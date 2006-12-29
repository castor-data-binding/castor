/**
 * Redistribution and use of this software and associated documentation
 * ("Software"), with or without modification, are permitted provided
 * that the following conditions are met:
 *
 * 1. Redistributions of source code must retain copyright
 *    statements and notices.  Redistributions must also contain a
 *    copy of this document.
 *
 * 2. Redistributions in binary form must reproduce the
 *    above copyright notice, this list of conditions and the
 *    following disclaimer in the documentation and/or other
 *    materials provided with the distribution.
 *
 * 3. The name "Exolab" must not be used to endorse or promote
 *    products derived from this Software without prior written
 *    permission of Intalio, Inc.  For written permission,
 *    please contact info@exolab.org.
 *
 * 4. Products derived from this Software may not be called "Exolab"
 *    nor may "Exolab" appear in their names without prior written
 *    permission of Intalio, Inc. Exolab is a registered
 *    trademark of Intalio, Inc.
 *
 * 5. Due credit should be given to the Exolab Project
 *    (http://www.exolab.org/).
 *
 * THIS SOFTWARE IS PROVIDED BY INTALIO, INC. AND CONTRIBUTORS
 * ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT
 * NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL
 * INTALIO, INC. OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * Copyright 1999-2004 (C) Intalio, Inc. All Rights Reserved.
 *
 * This file was originally developed by Keith Visco during the
 * course of employment at Intalio Inc.
 * All portions of this file developed by Keith Visco after Jan 19 2005 are
 * Copyright (C) 2005 Keith Visco. All Rights Reserved.
 *
 * $Id$
 */
package org.exolab.castor.xml.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.castor.mapping.BindingType;
import org.castor.mapping.MappingUnmarshaller;
import org.exolab.castor.mapping.ClassDescriptor;
import org.exolab.castor.mapping.Mapping;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.mapping.MappingLoader;
import org.exolab.castor.util.LocalConfiguration;
import org.exolab.castor.util.Configuration.Property;
import org.exolab.castor.xml.ClassDescriptorEnumeration;
import org.exolab.castor.xml.Introspector;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ResolverException;
import org.exolab.castor.xml.XMLClassDescriptor;
import org.exolab.castor.xml.XMLClassDescriptorResolver;
import org.exolab.castor.xml.XMLConstants;
import org.exolab.castor.xml.XMLMappingLoader;

/**
 * The default implementation of the ClassDescriptorResolver interface.
 *
 * @author <a href="mailto:keith AT kvisco DOT com">Keith Visco</a>
 * @version $Revision$ $Date: 2006-04-25 15:08:23 -0600 (Tue, 25 Apr 2006) $
 */
public class XMLClassDescriptorResolverImpl
    implements XMLClassDescriptorResolver {

    /**
     * internal cache for class loading (this is used to avoid trying to load
     * missing classes again and again).
     */
    private ClassCache _classCache       = null;

    /**
     * Internal cache for class descriptors that were created from CDR files,
     * class files, or by introspection.
     */
    private DescriptorCache _descriptorCache  = null;

    /**
     * The introspector to use, if necessary, to
     * create dynamic ClassDescriptors.
     */
    private Introspector _introspector = null;

    /**
     * The classloader to use.
     */
    private ClassLoader _loader  = null;

    /**
     * MappingLoader instance for finding user-defined
     * mappings from a mapping-file.
     */
    private XMLMappingLoader _mappingLoader = null;

    /**
     * A flag to indicate the use of introspection.
     */
    private boolean _useIntrospection = true;


    /**
     * Creates a new ClassDescriptorResolverImpl.
     */
    public XMLClassDescriptorResolverImpl() {
        _classCache = new ClassCache();
        _descriptorCache = new DescriptorCache();

        boolean loadPackageMappings = Boolean.valueOf(LocalConfiguration.getInstance().getProperties().getProperty(Property.LOAD_PACKAGE_MAPPING, Property.DEFAULT_LOAD_PACKAGE_MAPPING)).booleanValue();
        _descriptorCache.setLoadPackageMappings(loadPackageMappings);
    } //-- ClassDescriptorResolverImpl


    /**
     * Returns the Introspector being used by this ClassDescriptorResolver.
     * This allows for configuration of the Introspector.
     *
     * @return the Introspector being used by this ClassDescriptorResolver
     */
    public Introspector getIntrospector() {
        if (_introspector == null) {
            _introspector = new Introspector(_loader);
        }

        return _introspector;
    } //-- getIntrospector

    /**
     * {@inheritDoc}
     *
     * @see org.exolab.castor.xml.ClassDescriptorResolver#getMappingLoader()
     */
    public MappingLoader getMappingLoader() {
        return _mappingLoader;
    } //-- getXMLMappingLoader


    /**
     * {@inheritDoc}
     *
     * @see org.exolab.castor.xml.XMLClassDescriptorResolver#resolveXML(java.lang.Class)
     */
    public XMLClassDescriptor resolveXML(Class type) throws ResolverException {
        if (type == null) {
            return null;
        }

        final String className = type.getName();
        if (_descriptorCache.isMissingDescriptor(className)) {
            // we know the descriptor for this type's missing...
            return null;
        }

        // now the usual procedure...
        final ClassLoader classLoader = this.getClassLoader(type.getClassLoader());
        XMLClassDescriptor descriptor = this.getDescriptor(className, classLoader);
        if (descriptor != null) {
            return descriptor;
        }

        // if not found create a descriptor by introspection (if enabled)
        descriptor = this.createDescriptor(type);
        if (descriptor != null) {
            return descriptor;
        }

        // we were unable to find a descriptor for that type - note this for
        // future reference
        _descriptorCache.addMissingDescriptor(className);
        return null;
    } // -- resolve

    /**
     * {@inheritDoc}
     *
     * @see org.exolab.castor.xml.ClassDescriptorResolver#resolve(java.lang.Class)
     */
    public ClassDescriptor resolve(Class type) throws ResolverException {
        return resolveXML(type);
    }

    /**
     * {@inheritDoc}
     *
     * @see org.exolab.castor.xml.XMLClassDescriptorResolver#resolve(java.lang.String)
     */
    public XMLClassDescriptor resolve(String className) throws ResolverException {
        return resolve(className, null);
    } // -- resolve(String)

    /**
     * {@inheritDoc}
     *
     * @see org.exolab.castor.xml.XMLClassDescriptorResolver#resolve(java.lang.String,
     *      java.lang.ClassLoader)
     */
    public XMLClassDescriptor resolve(String className, ClassLoader loader) throws ResolverException {
        if (className == null || className.length() == 0) {
            String error = "Cannot resolve a null or zero-length class name.";
            throw new IllegalArgumentException(error);
        }

        if (_descriptorCache.isMissingDescriptor(className)) {
            return null;
        }

        final ClassLoader classLoader = this.getClassLoader(loader);
        XMLClassDescriptor descriptor = this.getDescriptor(className, classLoader);
        if (descriptor != null) {
            return descriptor;
        }

        // -- try to load class for creating a descriptor via introspection
        Class _class = _classCache.loadClass(className, classLoader);
        if (_class != null) {
            descriptor = this.createDescriptor(_class);
            if (descriptor != null) {
                return descriptor;
            }
        }

        _descriptorCache.addMissingDescriptor(className);
        return null;
    } //-- resolve(String, ClassLoader)

    /**
     * {@inheritDoc}
     *
     * @see org.exolab.castor.xml.XMLClassDescriptorResolver#resolveByXMLName(java.lang.String, java.lang.String, java.lang.ClassLoader)
     */
    public XMLClassDescriptor resolveByXMLName(String xmlName, String namespaceURI, ClassLoader loader) {
        if (xmlName == null || xmlName.length() == 0) {
            throw new IllegalArgumentException("Cannot resolve a null or zero-length xml name.");
        }

        // get a list of all descriptors with the correct xmlName, regardless of
        // their namespace
        List possibleMatches = _descriptorCache.getDescriptorList(xmlName);
        if (possibleMatches.size() == 0) {
            // nothing matches that XML name
            return null;
        }

        if (possibleMatches.size() == 1) {
            // we have exactly one possible match - that's our result
            // (if it has the right namespace, it's an exact match, if not its
            // the only possible match)
            return (XMLClassDescriptor) possibleMatches.get(0);
        }

        // we have more than one result - only an exact match can be the result
        for (Iterator i = possibleMatches.iterator(); i.hasNext();) {
            XMLClassDescriptor descriptor = (XMLClassDescriptor) i.next();

            if (this.namespaceEquals(namespaceURI, descriptor.getNameSpaceURI())) {
                return descriptor;
            }
        }

        // no exact match and too many possible matches...
        return null;
    } //-- resolveByXMLName

    /**
     * {@inheritDoc}
     *
     * @see org.exolab.castor.xml.XMLClassDescriptorResolver#resolveAllByXMLName(java.lang.String, java.lang.String, java.lang.ClassLoader)
     */
    public ClassDescriptorEnumeration resolveAllByXMLName(String xmlName, String namespaceURI, ClassLoader loader) {
        if (xmlName == null || xmlName.length() == 0) {
            String error = "Cannot resolve a null or zero-length xml name.";
            throw new IllegalArgumentException(error);
        }

        // get all descriptors with the correct xml name
        return new XCDEnumerator(_descriptorCache.getDescriptors(xmlName));
    } //-- resolveAllByXMLName


    /**
     * {@inheritDoc}
     *
     * @see org.exolab.castor.xml.XMLClassDescriptorResolver#setClassLoader(java.lang.ClassLoader)
     */
    public void setClassLoader(ClassLoader loader) {
        this._loader = loader;
    } //-- setClassLoader

    /**
     * Enables or disables introspection. Introspection is
     * enabled by default.
     *
     * @param enable a flag to indicate whether or not introspection
     * is allowed.
    **/
    public void setIntrospection(boolean enable) {
        _useIntrospection = enable;
    } //-- setIntrospection

    /**
     * Sets whether or not to look for and load package specific
     * mapping files (".castor.xml" files).
     *
     * @param loadPackageMappings a boolean that enables or
     * disables the loading of package specific mapping files
     */
    public void setLoadPackageMappings(boolean loadPackageMappings) {
        _descriptorCache.setLoadPackageMappings(loadPackageMappings);
    } //-- setLoadPackageMappings

    /**
     * {@inheritDoc}
     *
     * @see org.exolab.castor.xml.ClassDescriptorResolver#setMappingLoader(org.exolab.castor.mapping.MappingLoader)
     */
    public void setMappingLoader(MappingLoader mappingLoader) {
        _mappingLoader = (XMLMappingLoader) mappingLoader;

        if (_mappingLoader != null) {
            Iterator descriptors = _mappingLoader.descriptorIterator();
            while (descriptors.hasNext()) {
                XMLClassDescriptor descriptor = (XMLClassDescriptor) descriptors.next();
                _descriptorCache.addDescriptor(descriptor);
            }
        }
    } //-- setMappingLoader

    /**
     * Creates an XMLClassDescriptor for the given type by using introspection.<br>
     * This method will rely on the <code>Introspector</code> set with
     * <code>setIntrospector</code>.<br>
     * If a descriptor is successfully created it will be added to the
     * DescriptorCache. <br>
     * <br>
     * <b>NOTE</b>: If this XMLClassDescriptorResolver is NOT configured to use
     * introspection this method will NOT create an descriptor.<br>
     *
     * @param type
     *            The type to create an descriptor for.
     * @return The created XMLClassDescriptor or <code>null</code> if not
     *         descriptor could be created or creating descriptors via
     *         introspection is disabled.
     * @throws ResolverException
     *             If creating the descriptor failed.
     */
    private XMLClassDescriptor createDescriptor(Class type) throws ResolverException {
        if (!_useIntrospection) {
            return null;
        }

        try {
            XMLClassDescriptor descriptor = this.getIntrospector().generateClassDescriptor(type);
            if (descriptor != null) {
                _descriptorCache.addDescriptor(type.getName(), descriptor);
                return descriptor;
            }
        } catch (MarshalException mx) {
            throw new ResolverException(mx);
        }

        return null;
    }

    /**
     * Gets the <code>ClassLoader</code> that's actually to be used (e.g. for
     * loading resources).<br>
     * The actual <code>ClassLoader</code> is determined in the following way:
     * <lu>
     * <li> If the passed in "preferred" loader is not <code>null</code>, it
     * is used.
     * <li> If the loader of this XMLClassDescriptor is not <code>null</code>,
     * it is used.
     * <li> The context class loader of the current thread is used. </lu>
     *
     * @param loader The "preferred" <code>ClassLoader</code>.
     * @return The loader to be used.
     */
    private ClassLoader getClassLoader(ClassLoader loader) {
        if (loader != null) {
            return loader;
        }

        if (_loader != null) {
            return _loader;
        }

        return Thread.currentThread().getContextClassLoader();
    }

    /**
     * Tries to load an XMLClassDescriptor directly from an existing .class
     * file.<br>
     * The file that is searched for must be located in the classpath, have the
     * name <code>className</code> + "Descriptor", and contain a valid
     * XMLClassDescriptor.<br>
     * If a descriptor is found it is added to the internal descriptor cache.
     *
     * @param className
     *            The name of the class to load the descriptor for (This is NOT
     *            the class name of the descriptor!)
     * @param loader
     *            The preferred <code>ClassLoader</code>
     * @return The <code>XMLClassDescriptor</code> loaded from the
     *         corresponding .class file or <code>null</code> if the .class
     *         file does not exist or does not containa valid
     *         <code>XMLClassDescriptor</code>.
     */
    private XMLClassDescriptor loadDescriptorClass(String className, ClassLoader loader) {
        String descriptorClassName = className + XMLConstants.DESCRIPTOR_SUFFIX;
        Class descriptorClass = _classCache.loadClass(descriptorClassName, this.getClassLoader(loader));
        if (descriptorClass == null) {
            return null;
        }

        try {
            XMLClassDescriptor descriptor = (XMLClassDescriptor) descriptorClass.newInstance();
            _descriptorCache.addDescriptor(className, descriptor);
            return descriptor;
        } catch (InstantiationException ie) {
            // -- do nothing for now
        } catch (IllegalAccessException iae) {
            // -- do nothing for now
        }
        return null;
    }

    /**
     * Compares the two strings for equality. A Null and empty
     * strings are considered equal.
     *
     * @return true if the two strings are considered equal.
     */
    private boolean namespaceEquals(String ns1, String ns2) {
        if (ns1 == null) {
            return ns2 == null || ns2.length() == 0;
        }

        if (ns2 == null) {
            return ns1.length() == 0;
        }

        return ns1.equals(ns2);
    } //-- namespaceEquals

    /**
     * Gets the XMLClassDescriptor for the class with the given name.<br>
     *
     * The descriptor is searched in the following resources are search: <lu>
     * <li>The internal descriptor cache of this XMLClassDescriptorResolver
     * <li>The MappingLoader of this XMLClassDescriptorResolver
     * <li>The package mapping of the package the given class is located in
     * <li>The CDR file of the package the given class is located in
     * <li>The class file of the corresponding descriptor class (which is
     * className + "Descriptor") </lu> <br>
     * <br>
     * If any of these resources yield an XMLClassDescriptor it is added to the
     * internal cache and returned as result.
     *
     * @param className
     *            The class to get the descriptor for.
     * @param loader
     *            The preferred <code>ClassLoader</code> to be used.
     * @return An <code>XMLClassDescriptor</code> for the given class or
     *         <code>null</code> if no descriptor can be found.
     * @throws ResolverException
     *             If an CDR file for the package of the given class is
     *             available but cannot be processed.
     */
    private XMLClassDescriptor getDescriptor(String className, ClassLoader loader) throws ResolverException {
        // check our cache
        XMLClassDescriptor descriptor = _descriptorCache.getDescriptor(className);
        if (descriptor != null) {
            return descriptor;
        }

        // first check mapping loader
        if (_mappingLoader != null) {
            descriptor = (XMLClassDescriptor) _mappingLoader.getDescriptor(className);
            if (descriptor != null) {
                _descriptorCache.addDescriptor(className, descriptor);
                return descriptor;
            }
        }

        // check package mappings and package list
        final String packageName = this.getPackageName(className);
        _descriptorCache.loadPackageMapping(packageName, loader);
        _descriptorCache.loadCDRList(packageName, loader);

        // check our cache again
        descriptor = _descriptorCache.getDescriptor(className);
        if (descriptor != null) {
            return descriptor;
        }

        // try loading the descriptor from .class file
        descriptor = this.loadDescriptorClass(className, loader);
        if (descriptor == null) {
            // If we didn't find the descriptor, look in a lower package
            int offset = className.lastIndexOf(".");
            if (offset > 0 && !className.endsWith(".")) {
                String newClassName = className.substring(0, offset+1)
                        + XMLConstants.DESCRIPTOR_PACKAGE + className.substring(offset);
                descriptor = this.loadDescriptorClass(newClassName, loader);
            }
        }
        if (descriptor != null) {
            return descriptor;
        }

        return null;
    }

    /**
     * Gets the package name of the given class name.
     *
     * @param className
     *            The class name to retrieve the package name from.
     * @return The package name or the empty String if <code>className</code>
     *         is <code>null</code> or does not contain a package.
     */
    private String getPackageName(String className) {
        if (className == null) {
            return "";
        }

        int idx = className.lastIndexOf('.');
        if (idx >= 0) {
            return className.substring(0, idx);
        }
        return "";
    }


    /**
     * Internal cache for Class objects.<br>
     * <br>
     * The cache keeps a list of classes that could not be loaded to prevent
     * loading those classes again.
     *
     * @author <a href="mailto:stevendolg AT gxm DOT at">Steven Dolg</a>
     */
    private class ClassCache {

        /**
         * A list of classes that could not be loaded.
         */
        private List _missingClasses = new ArrayList();

        /**
         * Tries to load the <code>Class</code> object with the given name and
         * the given preferred <code>ClassLoader</code>.<br>
         * <br>
         * If the requested class could not be loaded its name is stored in the
         * list of missing classes. Further requests to load such classes will
         * prevent asking the class loader again.
         *
         * @param className
         *            The name of the class to be loaded. This must be a fully
         *            qualified name (i.e. including the package).
         * @param loader
         *            The preferred <code>ClassLoader</code>.
         * @return The <code>Class</code> loaded by the
         *         <code>ClassLoader</code> or <code>null</code> if the
         *         class could not be loaded or is contained in the list of
         *         missing classes.
         */
        public Class loadClass(String className, ClassLoader loader) {
            if (this._missingClasses.contains(className)) {
                return null;
            }

            try {
                // use passed in loader
                if (loader != null) {
                    return loader.loadClass(className);
                }
                // no loader available use Class.forName
                // actually this should never happen
                return Class.forName(className);
            } catch (NoClassDefFoundError ncdfe) {
                // This can happen if we try to load a class with invalid
                // case for example foo instead Foo.
            } catch (ClassNotFoundException e) {
            }

            this._missingClasses.add(className);
            return null;
        }
    }

    /**
     * Internal cache for XMLClassDescriptors.<br>
     * <br>
     * The cache maintains all descriptors loaded by its
     * <code>XMLClassDescriptorResolver</code>. It also keeps track of
     * mapping files and CDR lists that have been loaded. Just like the
     * ClassCache it also has a list of missing descriptors to avoid trying to
     * load those descriptors again.
     *
     * The cached descriptors are available via the name of the classes they
     * describe or via their XML name from a mapping file.
     *
     * @author <a href="mailto:stevendolg AT gxm DOT at">Steven Dolg</a>
     */
    private class DescriptorCache {

        private static final String PKG_CDR_LIST_FILE       = ".castor.cdr";
        private static final String PKG_MAPPING_FILE        = ".castor.xml";
        private static final String INTERNAL_CONTAINER_NAME = "-error-if-this-is-used-";

        /**
         * List of class names a descriptor is not available for.
         */
        private List                _missingTypes;
        /**
         * Map of cached descriptors with the class names they describe as key.
         */
        private Map                 _typeMap;
        /**
         * Map of cached descriptors with their XML names as key.
         */
        private Map                 _xmlNameMap;
        /**
         * List of package mapping name that haven already been tried to load.
         * (Both successfully and unsuccessfully).
         */
        private List                _loadedPackageMappings;
        /**
         * List of CDR file that have already been tried to load. (Both
         * successfully and unsuccessfully).
         */
        private List                _loadedCDRLists;
        /**
         * Flag indicating whether package mappings should be loaded or not.
         *
         * @see XMLClassDescriptorResolverImpl#setLoadPackageMappings(boolean)
         */
        private boolean             _loadPackageMappings    = true;

        /**
         * Default constructor.<br>
         * <br>
         * Initializes alls list and maps.
         */
        public DescriptorCache() {
            super();

            _typeMap = new HashMap();
            _xmlNameMap = new HashMap();
            _missingTypes = new ArrayList();
            _loadedPackageMappings = new ArrayList();
            _loadedCDRLists = new ArrayList();
        }

        /**
         * Adds a descriptor to this caches maps.<br>
         * The descriptor is mapped both with the class name and its XML name.
         *
         * The descriptor will not be mapped with its XML name is
         * <code>null</code>, the empty string (""), or has the value of the
         * constant INTERNAL_CONTAINER_NAME.
         *
         * If there already is a descriptor for the given <code>className</code>
         * and/or the descriptor's XML name the previously cached descriptor is
         * replaced.
         *
         * @param className
         *            The class name to be used for mapping the given
         *            descriptor.
         * @param descriptor
         *            The descriptor to be mapped.
         *
         * @see #INTERNAL_CONTAINER_NAME
         */
        private void addDescriptor(String className, XMLClassDescriptor descriptor) {
            _typeMap.put(className, descriptor);

            String xmlName = descriptor.getXMLName();
            // ignore descriptors with an empty XMLName
            if (xmlName == null || xmlName.length() == 0) {
                return;
            }

            // ignore descriptors with the internal XMLName
            if (INTERNAL_CONTAINER_NAME.equals(xmlName)) {
                return;
            }

            List descriptorList = this.getDescriptorList(descriptor.getXMLName());
            if (!descriptorList.contains(descriptor)) {
                descriptorList.add(descriptor);
            }
        }

        /**
         * Adds the given descriptor to this DescriptorCache.<br>
         * The descriptor will be mapped to its class name and its XML name.
         *
         * @see #addDescriptor(String, XMLClassDescriptor) for details.
         *
         * @param descriptor
         *            The XMLClassDescriptor to be added.
         */
        public void addDescriptor(XMLClassDescriptor descriptor) {
            this.addDescriptor(descriptor.getJavaClass().getName(), descriptor);
        }

        /**
         * Adds the given class name to the list of classes a descriptor is
         * unavailable for.
         *
         * @param className
         *            The class name to be added.
         *
         * @see #isMissingDescriptor(String)
         */
        public void addMissingDescriptor(String className) {
            _missingTypes.add(className);
        }

        /**
         * Gets the descriptor that is mapped to the given class name.
         *
         * @param className
         *            The class name to get a descriptor for.
         * @return The descriptor mapped to the given name or <code>null</code>
         *         if no descriptor is stored in this cache.
         */
        public XMLClassDescriptor getDescriptor(String className) {
            return (XMLClassDescriptor) _typeMap.get(className);
        }

        /**
         * Gets a list of descriptors that have the given XML name.<br>
         * <br>
         * This method will return all previously cached descriptors with the
         * given XML name regardless of their namespace.
         *
         * @param xmlName
         *            The XML name of the descriptors to get.
         * @return A list of descriptors with the given XML name or an empty
         *         list if no such descriptor is stored in this cache. This
         *         method will never return <code>null</code>!
         */
        public List getDescriptorList(String xmlName) {
            List list = (List) _xmlNameMap.get(xmlName);

            if (list == null) {
                list = new ArrayList();
                _xmlNameMap.put(xmlName, list);
            }

            return list;
        }

        /**
         * Gets an iterator over all descriptors that have the given XML name.<br>
         * <br>
         * This method will return an iterator over all previously cached
         * descriptors with the given XML name regardless of their namespace.<br>
         * <br>
         * NOTE: If an descriptor with the XML name in question is added to this
         * cache, the iterator will fail due to an
         * <code>ConcurrentModificationException</code>
         *
         * @param xmlName
         *            The XML name of the descriptors to get.
         * @return An iterator over descriptors with the given XML name. If no
         *         descriptor with the given XML name is stored in this cache,
         *         the iterator will never return an object (.hasNext() will
         *         immediately return <code>false</code>). This method will
         *         never return <code>null</code>!
         *
         * @see ConcurrentModificationException
         * @see List#iterator()
         */
        public Iterator getDescriptors(String xmlName) {
            return this.getDescriptorList(xmlName).iterator();
        }

        /**
         * Checks whether the given class name is contained in the list of class
         * names the descriptor is found to be missing.<br>
         * <br>
         * NOTE: This does not check whether a descriptor is stored within this
         * cache or not. This rather checks the list of class names the
         * XMLClassDescriptorResolverImpl found to have no descriptor. The cache
         * itself has no means of determining that this is the case and thus
         * will never add/remove class names to/from it.
         *
         * @param className
         *            The class name to be checked.
         * @return <code>true</code> If the given class name was stated to
         *         have no descriptor by a previous call to
         *         <code>addMissingDescriptor</code> with exactly the same
         *         class name. <code>false</code> otherwise.
         *
         * @see #addMissingDescriptor(String)
         */
        public boolean isMissingDescriptor(String className) {
            return _missingTypes.contains(className);
        }

        /**
         * Tries to load the CDR file for the given package name using the
         * provided class loader.<br>
         * <br>
         * If the CDR file is available and could be loaded properly the
         * descriptors listed in it are added to this cache.
         *
         * If a descriptor is listed in the CDR file for the given package but
         * could not be loaded (e.g. because the reference class file is not
         * available) the descriptor is ignored but no exception is thrown.
         *
         * If a CDR file is not available for the given package this method will
         * not load any descriptors and not throw any exceptions.
         *
         * Further calls to this method with the same package name will not be
         * processed.
         *
         * @param packageName
         *            The package to load the CDR file for.
         * @param loader
         *            The ClassLoader to be used for both loading the CDR file
         *            and the descriptor classes listed in it. This must not be
         *            <code>null</code>!
         * @throws ResolverException
         *             If a CDR file is available but cannot be opened or read.
         */
        public synchronized void loadCDRList(String packageName, ClassLoader loader) throws ResolverException {
            if (_loadedCDRLists.contains(packageName)) {
                return;
            }

            _loadedCDRLists.add(packageName);
            URL url = loader.getResource(this.getQualifiedFileName(PKG_CDR_LIST_FILE, packageName));
            if (url == null) {
                return;
            }

            try {
                Properties cdrList = this.getProperties(url);

                final Enumeration classes = cdrList.keys();
                while (classes.hasMoreElements()) {
                    String className = (String) classes.nextElement();
                    String descriptorClassName = (String) cdrList.get(className);
                    try {
                        Class descriptorClass = loader.loadClass(descriptorClassName);
                        this.addDescriptor(className, ((XMLClassDescriptor) descriptorClass.newInstance()));
                    } catch (Exception e) {
                        // -- TODO: report error, but continue
                    }
                }
            } catch (java.io.IOException iox) {
                throw new ResolverException(iox);
            }
        }

        /**
         * Tries to load the package mapping file for the given package using
         * the provided class loader.<br>
         * <br>
         * If the mapping file is available and could be loaded properly the
         * descriptors listed in it are added to this cache.<br>
         * If the loading of package mapping files is disabled this method will
         * do nothing.<br>
         * If a mapping file is not available for the given package this method
         * will not load any descriptors and not throw any exceptions.<br>
         * <br>
         * The mapping file - if available - is loaded using the
         * <code>MappingLoader</code>.<br>
         * <br>
         * Further calls to this method with the same package name will not be
         * processed.
         *
         * @param packageName
         *            The name of the package to load the mapping file for.
         * @param loader
         *            The ClassLoader to be used for loading the mapping file.
         *            This must not be null!
         * @see #setLoadPackageMappings(boolean)
         * @see MappingLoader
         */
        public synchronized void loadPackageMapping(String packageName, ClassLoader loader) {
            if (!_loadPackageMappings || _loadedPackageMappings.contains(packageName)) {
                return;
            }

            _loadedPackageMappings.add(packageName);
            try {
                final Mapping mapping = this.loadMapping(packageName, loader);
                if (mapping != null) {
                    MappingUnmarshaller unmarshaller = new MappingUnmarshaller();
                    MappingLoader mappingLoader = unmarshaller.getMappingLoader(mapping, BindingType.XML);
                    Iterator descriptors = mappingLoader.descriptorIterator();
                    while (descriptors.hasNext()) {
                        XMLClassDescriptor descriptor = (XMLClassDescriptor) descriptors.next();
                        this.addDescriptor(descriptor);
                    }
                }
            } catch (MappingException e) {
                // TODO: report error (or should this exception be thrown?)
            }
        }

        /**
         * Enables or disabled the loading of package mapping files.
         *
         * @param loadPackageMappings
         *            <code>true</code> if package mapping files should be
         *            loaded. <code>false</code> if not.
         */
        public void setLoadPackageMappings(boolean loadPackageMappings) {
            _loadPackageMappings = loadPackageMappings;
        }

        /**
         * Creates a Properties object and initializes it with the contents of
         * the given URL.<br>
         * The provided URL must exist and be suitable for loading properties
         * from.
         *
         * @param url
         *            The URL to load the properties from. This must not be
         *            null!
         * @return The loaded properties.
         * @throws IOException
         *             If loading the properties from the given ULR failed.
         *
         * @see Properties
         * @see Properties#load(InputStream)
         */
        private Properties getProperties(URL url) throws java.io.IOException {
            Properties cdrList = new Properties();

            java.io.InputStream stream = url.openStream();
            cdrList.load(stream);
            stream.close();

            return cdrList;
        }

        /**
         * Qualifies the given <code>fileName</code> with the given
         * <code>packageName</code> and returns the resulting file path.<br>
         * If <code>packageName</code> is <code>null</code> or a zero-length
         * String, this method will return <code>fileName</code>.<br>
         *
         * @param fileName
         *            The file name to be qualified.
         * @param packageName
         *            The package name to be used for qualifying.
         * @return The qualified file path.
         */
        private String getQualifiedFileName(String fileName, String packageName) {
            if (packageName == null || packageName.length() == 0) {
                return fileName;
            }

            StringBuffer result = new StringBuffer();
            result.append(packageName.replace('.', '/'));
            result.append('/');
            result.append(fileName);
            return result.toString();
        }

        /**
         * Loads a package mapping file for the given package name using the
         * provided ClassLoader.<br>
         *
         * @param packageName
         *            The name of the package to load the mapping file for.
         * @param loader
         *            The loader to be used for loading the mapping for. This
         *            must not be <code>null</code>!
         * @return The loaded Mapping or <code>null</code> if no mapping file
         *         is available for the given package.
         * @throws MappingException
         */
        private Mapping loadMapping(String packageName, ClassLoader loader) throws MappingException {
            URL url = loader.getResource(this.getQualifiedFileName(PKG_MAPPING_FILE, packageName));
            if (url == null) {
                return null;
            }

            try {
                Mapping mapping = new Mapping(loader);
                mapping.loadMapping(url);
                return mapping;
            } catch (java.io.IOException ioex) {
                throw new MappingException(ioex);
            }
        }
    }

    /**
     * A locally used implementation of ClassDescriptorEnumeration
     */
    class XCDEnumerator implements ClassDescriptorEnumeration {

        private final Iterator _descriptors;

        /**
         * Creates an XCDEnumerator
         */
        XCDEnumerator(Iterator descriptors) {
            super();

            _descriptors = descriptors;
        }

        /**
         * {@inheritDoc}
         *
         * @see org.exolab.castor.xml.ClassDescriptorEnumeration#getNext()
         */
        public XMLClassDescriptor getNext() {
            return (XMLClassDescriptor) _descriptors.next();
        }

        /**
         * {@inheritDoc}
         *
         * @see org.exolab.castor.xml.ClassDescriptorEnumeration#hasNext()
         */
        public boolean hasNext() {
            return _descriptors.hasNext();
        }
    } //-- ClassDescriptorEnumeration
} // -- ClassDescriptorResolverImpl
