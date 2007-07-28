/*
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

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.exolab.castor.mapping.ClassDescriptor;
import org.exolab.castor.mapping.MappingLoader;
import org.exolab.castor.util.Configuration;
import org.exolab.castor.util.LocalConfiguration;
import org.exolab.castor.util.Configuration.Property;
import org.exolab.castor.xml.ResolverException;
import org.exolab.castor.xml.XMLClassDescriptor;
import org.exolab.castor.xml.XMLClassDescriptorResolver;
import org.exolab.castor.xml.util.resolvers.CastorXMLStrategy;
import org.exolab.castor.xml.util.resolvers.ResolveHelpers;

/**
 * The default implementation of the ClassDescriptorResolver interface.
 *
 * @author <a href="mailto:keith AT kvisco DOT com">Keith Visco</a>
 * @version $Revision$ $Date: 2006-04-25 15:08:23 -0600 (Tue, 25 Apr 2006) $
 */
public class XMLClassDescriptorResolverImpl implements XMLClassDescriptorResolver {
    private static final Log LOG = LogFactory.getLog(XMLClassDescriptorResolverImpl.class);
    
    /**
     * The strategy to use for all resolve actions.
     */
    private ResolverStrategy _strategy;
   
    /**
     * The place where all rsolving strategies and their commands put the results into
     * and can be read from.
     */
    private DescriptorCacheImpl _descriptorCache;

    /**
     * The mapping loader is read from the XMLClassDescriptorResolver and used in the
     * strategy - so we need to keep it.
     */
    private MappingLoader _mappingLoader;
    
    /**
     * The class loader which is used when no other class loader was specified in
     * the resolve call. If this loader is also not set the context loader of the
     * current thread is used.
     */
    private ClassLoader _loader;

    /**
     * Creates a new ClassDescriptorResolverImpl.
     */
    public XMLClassDescriptorResolverImpl() {
        Configuration config = LocalConfiguration.getInstance();
        boolean loadPackageMappings = config.getBoolean(
                Property.LOAD_PACKAGE_MAPPING,
                Property.DEFAULT_LOAD_PACKAGE_MAPPING);
        
        _strategy = new CastorXMLStrategy();
        _strategy.setProperty(
                ResolverStrategy.PROPERTY_LOAD_PACKAGE_MAPPINGS,
                Boolean.valueOf(loadPackageMappings));

        _descriptorCache = new DescriptorCacheImpl();
        _mappingLoader = null;
        _loader = null;
    } //-- ClassDescriptorResolverImpl

    /**
     * {@inheritDoc}
     */
    public ClassDescriptor resolve(final Class type) throws ResolverException {
        if (type == null) {
            String message = "Type argument must not be null for resolve";
            LOG.warn(message);
            throw new IllegalArgumentException(message);
        }
        
        if (_descriptorCache.getDescriptor(type.getName()) != null) {
            return _descriptorCache.getDescriptor(type.getName());
        }
        
        ClassLoader l = _loader;
        if (l == null) { l = type.getClassLoader(); }
        if (l == null) { l = Thread.currentThread().getContextClassLoader(); }
        
        return this.resolve(type.getName(), l);
    } // -- resolve(Class)

    /**
     * {@inheritDoc}
     */
    public XMLClassDescriptor resolve(final String className) throws ResolverException {
        if (className == null || className.length() == 0) {
            String message = "Cannot resolve a null or zero-length class name.";
            LOG.warn(message);
            throw new IllegalArgumentException(message);
        }
        
        if (_descriptorCache.getDescriptor(className) != null) {
            return _descriptorCache.getDescriptor(className);
        }
        
        ClassLoader l = _loader;
        if (l == null) { l = Thread.currentThread().getContextClassLoader(); }
        
        return this.resolve(className, l);
    } // -- resolve(String)

    /**
     * {@inheritDoc}
     */
    public XMLClassDescriptor resolve(final String className, final ClassLoader loader)
    throws ResolverException {
        if (className == null || className.length() == 0) {
            String message = "Cannot resolve a null or zero-length class name.";
            LOG.warn(message);
            throw new IllegalArgumentException(message);
        }
        
        if (_descriptorCache.getDescriptor(className) != null) {
            return _descriptorCache.getDescriptor(className);
        }
        
        ClassLoader l = loader;
        if (l == null) { l = _loader; }
        if (l == null) { l = Thread.currentThread().getContextClassLoader(); }
        
        _strategy.setProperty(ResolverStrategy.PROPERTY_CLASS_LOADER, l);
        return (XMLClassDescriptor) _strategy.resolveClass(_descriptorCache, className);
    } //-- resolve(String, ClassLoader)

    /**
     * {@inheritDoc}
     */
    public XMLClassDescriptor resolveByXMLName(final String xmlName, final String namespaceURI,
            final ClassLoader loader) {
        
        if (xmlName == null || xmlName.length() == 0) {
            String message = "Cannot resolve a null or zero-length class name.";
            LOG.warn(message);
            throw new IllegalArgumentException(message);
        }
        
        // @TODO Joachim 2007-05-05 the class loader is NOT used!
        // get a list of all descriptors with the correct xmlName, regardless of their namespace
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

            if (ResolveHelpers.namespaceEquals(namespaceURI, descriptor.getNameSpaceURI())) {
                return descriptor;
            }
        }

        // no exact match and too many possible matches...
        return null;
    } //-- resolveByXMLName

    /**
     * {@inheritDoc}
     */
    public Iterator resolveAllByXMLName(final String xmlName, final String namespaceURI,
            final ClassLoader loader) {
        
        if (xmlName == null || xmlName.length() == 0) {
            String message = "Cannot resolve a null or zero-length xml name.";
            LOG.warn(message);
            throw new IllegalArgumentException(message);
        }

        // get all descriptors with the matching xml name
        return _descriptorCache.getDescriptors(xmlName);
    } //-- resolveAllByXMLName

    /**
     * {@inheritDoc}
     */
    public MappingLoader getMappingLoader() {
        return _mappingLoader;
    } //-- getXMLMappingLoader

    /**
     * {@inheritDoc}
     */
    public void setClassLoader(ClassLoader loader) {
        _loader = loader;
    } //-- setClassLoader

    /**
     * Enables or disables introspection. Introspection is enabled by default.
     *
     * @param enable a flag to indicate whether or not introspection is allowed.
     */
    public void setIntrospection(boolean enable) {
        // used by XMLMappingLoader.createResolver()
        _strategy.setProperty(ResolverStrategy.PROPERTY_USE_INTROSPECTION, new Boolean(enable));
    } //-- setIntrospection

    /**
     * Sets whether or not to look for and load package specific mapping files (".castor.xml").
     *
     * @param loadPackageMappings a boolean that enables or disables the loading of package
     *        specific mapping files
     */
    public void setLoadPackageMappings(boolean loadPackageMappings) {
        _strategy.setProperty(
                ResolverStrategy.PROPERTY_LOAD_PACKAGE_MAPPINGS,
                Boolean.valueOf(loadPackageMappings));
    } //-- setLoadPackageMappings

    /**
     * {@inheritDoc}
     */
    public void setMappingLoader(MappingLoader mappingLoader) {
        _mappingLoader = mappingLoader;
        if (_mappingLoader != null) {
            Iterator descriptors = _mappingLoader.descriptorIterator();
            while (descriptors.hasNext()) {
                XMLClassDescriptor descriptor = (XMLClassDescriptor) descriptors.next();
                _descriptorCache.addDescriptor(descriptor.getClass().getName(), descriptor);
            }
            _strategy.setProperty(ResolverStrategy.PROPERTY_MAPPING_LOADER, _mappingLoader);
        }
    } //-- setMappingLoader

    /**
     * {@inheritDoc}
     */
    public void addClass(final String className) throws ResolverException {
        this.resolve(className);
    }

    /**
     * {@inheritDoc}
     */
    public void addClasses(final String[] classNames) throws ResolverException {
        for (int i = 0; i < classNames.length; i++) {
            String className = classNames[i];
            this.addClass(className);
        }
    }

    /**
     * {@inheritDoc}
     */
    public void addClass(final Class clazz) throws ResolverException {
        this.resolve(clazz);
    }

    /**
     * {@inheritDoc}
     */
    public void addClasses(final Class[] clazzes) throws ResolverException {
        for (int i = 0; i < clazzes.length; i++) {
            Class clazz = clazzes[i];
            this.addClass(clazz);
        }
    }

    /**
     * {@inheritDoc}
     */
    public void addPackage(final String packageName) throws ResolverException {
        _strategy.resolvePackage(_descriptorCache, packageName);
    }

    /**
     * {@inheritDoc}
     */
    public void addPackages(final String[] packageNames) throws ResolverException {
        for (int i = 0; i < packageNames.length; i++) {
            String packageName = packageNames[i];
            this.addPackage(packageName);
        }
    }
       
    /**
     * {@inheritDoc}
     */
    public void loadClassDescriptors(final String packageName) throws ResolverException {
        String message = "Already deprecated in the interface!";
        LOG.warn(message);
        throw new UnsupportedOperationException();
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
    private static class DescriptorCacheImpl implements ResolverStrategy.ResolverResults {

        private static final Log LOG2 = LogFactory.getLog(DescriptorCacheImpl.class);

        private static final String INTERNAL_CONTAINER_NAME = "-error-if-this-is-used-";

        /** List of class names a descriptor is not available for. */
        private List _missingTypes;

        /** Map of cached descriptors with the class names they describe as key. */
        private Map _typeMap;

        /** Map of cached descriptors with their XML names as key. */
        private Map _xmlNameMap;

        /**
         * Default constructor.<br>
         * <br>
         * Initializes alls list and maps.
         */
        public DescriptorCacheImpl() {
            super();
            
            LOG2.debug("New instance!");
            
            _typeMap = new HashMap();
            _xmlNameMap = new HashMap();
            _missingTypes = new ArrayList();
        } //--- DescriptorCacheImpl

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
         * @param className The class name to be used for mapping the given descriptor.
         * @param descriptor The descriptor to be mapped.
         * 
         * @see #INTERNAL_CONTAINER_NAME
         */
        public void addDescriptor(final String className, final XMLClassDescriptor descriptor) {
            if ((className == null) || (className.length() == 0)) {
                String message = "Class name to insert ClassDescriptor must not be null";
                LOG2.warn(message);
                throw new IllegalArgumentException(message);
            }
            
            if (descriptor == null) {
                if (LOG2.isDebugEnabled()) {
                    LOG2.debug("Adding class name to missing classes: " + className);
                }
                _missingTypes.add(className);
                return;
            }
            
            if (LOG2.isDebugEnabled()) {
                LOG2.debug("Adding descriptor class for: " + className + " descriptor: " + descriptor);
            }
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
        } //-- addDescriptor

        /**
         * Gets the descriptor that is mapped to the given class name.
         * 
         * @param className The class name to get a descriptor for.
         * @return The descriptor mapped to the given name or <code>null</code>
         *         if no descriptor is stored in this cache.
         */
        public XMLClassDescriptor getDescriptor(final String className) {
            if ((className == null) || ("".equals(className)) || (_missingTypes.contains(className))) {
                return null;
            }
            
            XMLClassDescriptor ret = (XMLClassDescriptor) _typeMap.get(className);
            if (LOG2.isDebugEnabled()) {
                LOG2.debug("Get descriptor for: " + className + " found: " + ret);
            }
            return ret;
        } //-- getDescriptor

        /**
         * Gets a list of descriptors that have the given XML name.<br>
         * <br>
         * This method will return all previously cached descriptors with the
         * given XML name regardless of their namespace.
         * 
         * @param xmlName The XML name of the descriptors to get.
         * @return A list of descriptors with the given XML name or an empty
         *         list if no such descriptor is stored in this cache. This
         *         method will never return <code>null</code>!
         */
        public List getDescriptorList(final String xmlName) {
            List list = (List) _xmlNameMap.get(xmlName);

            if (list == null) {
                list = new ArrayList();
                _xmlNameMap.put(xmlName, list);
            }

            return list;
        } //-- getDescriptorList

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
         * @param xmlName The XML name of the descriptors to get.
         * @return An iterator over descriptors with the given XML name. If no
         *         descriptor with the given XML name is stored in this cache,
         *         the iterator will never return an object (.hasNext() will
         *         immediately return <code>false</code>). This method will
         *         never return <code>null</code>!
         * 
         * @see ConcurrentModificationException
         * @see List#iterator()
         */
        public Iterator getDescriptors(final String xmlName) {
            return this.getDescriptorList(xmlName).iterator();
        } //-- getDescriptors

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
         * @param className The class name to be checked.
         * @return <code>true</code> If the given class name was stated to
         *         have no descriptor by a previous call to
         *         <code>addMissingDescriptor</code> with exactly the same
         *         class name. <code>false</code> otherwise.
         * 
         * @see #addMissingDescriptor(String)
         */
        public boolean isMissingDescriptor(final String className) {
            return _missingTypes.contains(className);
        } //-- isMissingDescriptor

        /**
         * To add not only a single descriptor but a map of descriptors at once.
         * 
         * @param descriptor a Map of className (String) and XMLClassDescriptor pairs
         */
        public void addAllDescriptors(final Map descriptors) {
            if ((descriptors == null) || (descriptors.isEmpty())) {
                LOG2.debug("Called addAllDescriptors with null or empty descriptor map");
                return;
            }
            
            for (Iterator iter = descriptors.keySet().iterator(); iter.hasNext();) {
                String clsName = (String) iter.next();
                this.addDescriptor(clsName, (XMLClassDescriptor)descriptors.get(clsName));
            }
        } //-- addAllDescriptors
    } // -- DescriptorCacheImpl
} // -- ClassDescriptorResolverImpl
