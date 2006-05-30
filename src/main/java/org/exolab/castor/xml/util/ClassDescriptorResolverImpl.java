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


import org.exolab.castor.xml.ClassDescriptorEnumeration;
import org.exolab.castor.xml.ClassDescriptorResolver;
import org.exolab.castor.xml.Introspector;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ResolverException;
import org.exolab.castor.xml.XMLClassDescriptor;
import org.exolab.castor.xml.XMLMappingLoader;

import org.exolab.castor.mapping.Mapping;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.mapping.MappingResolver;
import org.exolab.castor.mapping.loader.MappingLoader;

import java.net.URL;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;

/**
 * The default implementation of the ClassDescriptorResolver interface
 *
 * @author <a href="mailto:keith AT kvisco DOT com">Keith Visco</a>
 * @version $Revision$ $Date$
 */
public class ClassDescriptorResolverImpl
    implements ClassDescriptorResolver 
{
 
    private static final String PKG_MAPPING_FILE   = ".castor.xml";
    private static final String PKG_CDR_LIST_FILE  = ".castor.cdr";
    private static final String DESCRIPTOR_PREFIX  = "Descriptor";
    private static final String INTERNAL_CONTAINER_NAME = "-error-if-this-is-used-";

    /**
     * A "null" Mapping file reference, used to mark that
     * the resolver has already attempted file.io to load
     * a package mapping
     */
    private static final Mapping NULL_MAPPING = new Mapping();
    
    /**
     * A "null" Properties file reference, used to mark that
     * the resolver has already attempted file.io to load
     * a package cdr list file
     */
    private static final Properties NULL_CDR_FILE = new Properties();
    
    /**
     * internal cache for class descriptors
     * with the class as the key
     */
    private Hashtable _cacheViaClass = null;
    
    /**
     * internal cache for class descriptors
     * with the xml-name as the key
     */
    private Hashtable _cacheViaName = null;
    
    /**
     * Keeps a list of ClassNotFoundExceptions
     * for faster lookups than using loadClass().
     * hopefully this won't eat up too much
     * memory.
     */
    private Hashtable _classNotFoundList = null;
    
    private Hashtable _packageMappings   = null;
    
    private Hashtable _packageCDList     = null;
    
    
    /**
     * The introspector to use, if necessary, to 
     * create dynamic ClassDescriptors
     */
    private Introspector _introspector = null;
    
    /**
     * The classloader to use
     */
    private ClassLoader _loader  = null;
    
    /**
     * A flag to indicate whether or not to use
     * the default package mappings, true by
     * default
     */
    private boolean _loadPackageMappings = true;
    
    /**
     * MappingLoader instance for finding user-defined
     * mappings from a mapping-file
     */
    private XMLMappingLoader _mappingLoader = null;
    
    /** 
     * A flag to indicate the use of introspection
     */
    private boolean _useIntrospection = true;
        
    
    /**
     * Creates a new ClassDescriptorResolverImpl
     */
    public ClassDescriptorResolverImpl() {
        _cacheViaClass   = new Hashtable();
        _cacheViaName    = new Hashtable();
        _packageMappings = new Hashtable();
        _packageCDList   = new Hashtable();
    } //-- ClassDescriptorResolverImpl


    /**
     * Creates a new ClassDescriptorResolverImpl with the given ClassLoader
     *
     * @param loader the ClassLoader to use when loading ClassDescriptors
     */
    public ClassDescriptorResolverImpl(ClassLoader loader) {
        this();
        _loader = loader;
    } //-- ClassDescriptorResolverImpl

    
    /**
     * Associates (or binds) a class type with a given ClassDescriptor
     *
     * @param type the Class to associate with the given descriptor
     * @param classDesc the ClassDescriptor to associate the given 
     * class with
     */
    public void associate(Class type, XMLClassDescriptor classDesc) {
        
        if (type == null) {
            throw new IllegalArgumentException("argument 'type' must not be null.");
        }
        
        //-- handle remove
        if (classDesc == null) {
            if (type != null) {
                _cacheViaClass.remove(type);
            }
        }
        else {
            _cacheViaClass.put(type, classDesc);
            String xmlName = classDesc.getXMLName();
            if ((xmlName != null) && (xmlName.length() > 0)) {
                if (!INTERNAL_CONTAINER_NAME.equals(xmlName)) {
                    String nameKey = xmlName;
                    String ns = classDesc.getNameSpaceURI();             
                    if ((ns != null) && (ns.length() > 0)) {
                        nameKey = ns + ':' + xmlName;
                    }
                    _cacheViaName.put(nameKey, classDesc);
                }
            }
        }
    } //-- associate
    
    /**
     * Returns the Introspector being used by this ClassDescriptorResolver.
     * This allows for configuration of the Introspector.
     *
     * @return the Introspector being used by this ClassDescriptorResolver
    **/
    public Introspector getIntrospector() {
        if (_introspector == null)
            _introspector = new Introspector(_loader);
        return _introspector;
    } //-- getIntrospector
    
    public XMLMappingLoader getMappingLoader() {
        return _mappingLoader;
    } //-- getXMLMappingLoader
    
    /**
     * Returns the XMLClassDescriptor for the given class
     * @param type the Class to find the XMLClassDescriptor for
     * @return the XMLClassDescriptor for the given class
    **/
    public XMLClassDescriptor resolve(Class type) 
        throws ResolverException
    {
        
        if (type == null) return null;
        
        XMLClassDescriptor classDesc = (XMLClassDescriptor) _cacheViaClass.get(type);
        
        if (classDesc != null) return classDesc;
        
        //-- check mapping loader first 
        //-- [proposed by George Stewart]
        if (_mappingLoader != null) {            
            classDesc = (XMLClassDescriptor)_mappingLoader.getDescriptor(type);
            if (classDesc != null) {
               _cacheViaClass.put(type, classDesc);
               return classDesc;
            }
        }

        String pkgName = getPackageName(type.getName());
        
        //-- check package mapping
        Mapping mapping = loadPackageMapping(pkgName, type.getClassLoader());
        if (mapping != null) {
            try {
                MappingLoader mapLoader = (MappingLoader)mapping.getResolver(Mapping.XML);
                classDesc = (XMLClassDescriptor) mapLoader.getDescriptor(type);
            }
            catch(MappingException mx) {}
            if (classDesc != null) {
                associate(type, classDesc);
                return classDesc;
            }
        }
        
        //-- load package list
        if (loadPackageList(pkgName, type.getClassLoader())) {
            classDesc = (XMLClassDescriptor) _cacheViaClass.get(type);
            if (classDesc != null) return classDesc;
        }
         
        String className = type.getName() + DESCRIPTOR_PREFIX;
        try {
            ClassLoader loader = type.getClassLoader();
            Class dClass = loadClass(className, loader);            
            classDesc = (XMLClassDescriptor) dClass.newInstance();
            _cacheViaClass.put(type, classDesc);
        }
        catch(ClassNotFoundException cnfe) { 
            /* 
             This is ok, since we are just checking if the
             Class exists...if not we create one.
            */ 
        }
        catch(Exception ex) {
            String err = "instantiation error for class: " + className;
            err += "; " + ex.toString();
            throw new ResolverException(err, ex);
        }
        
        //-- create classDesc automatically if necessary
        if ((classDesc == null) && _useIntrospection) {
            try {
                classDesc = getIntrospector().generateClassDescriptor(type);
                if (classDesc != null) {
                    _cacheViaClass.put(type, classDesc);
                }
            }
            catch (MarshalException mx) {
                throw new ResolverException(mx);
            }
        }
        return classDesc;
    } //-- resolve
    
    /**
     * Returns the XMLClassDescriptor for the given class name
     * @param className the class name to find the XMLClassDescriptor for
     * @return the XMLClassDescriptor for the given class name
    **/
    public XMLClassDescriptor resolve(String className) 
        throws ResolverException
    {
        return resolve(className, null);
    } //-- resolve(String)
    
    /**
     * Returns the XMLClassDescriptor for the given class name
     * @param className the class name to find the XMLClassDescriptor for
     * @param loader the ClassLoader to use
     * @return the XMLClassDescriptor for the given class name
    **/
    public XMLClassDescriptor resolve(String className, ClassLoader loader) 
        throws ResolverException
    {
        
        XMLClassDescriptor classDesc = null;
        
        if ((className == null) || (className.length() == 0)) {
            String error = "Cannot resolve a null or zero-length class name.";
            throw new IllegalArgumentException(error);
        }
            
        //-- first check mapping loader
        if (_mappingLoader != null) {
            classDesc = (XMLClassDescriptor)_mappingLoader.getDescriptor(className);
            if (classDesc != null)
                return classDesc;
        }
        
        //-- check package mapping
        String pkgName = getPackageName(className);
        Mapping mapping = loadPackageMapping(pkgName, loader);
        if (mapping != null) {
            try {
                MappingLoader mapLoader = (MappingLoader)mapping.getResolver(Mapping.XML);
                classDesc = (XMLClassDescriptor) mapLoader.getDescriptor(className);
            }
            catch(MappingException mx) {}
            if (classDesc != null) {
                if (classDesc.getJavaClass() != null) {
                    associate(classDesc.getJavaClass(), classDesc);
                }
                return classDesc;
            }
        }
        
        //-- try and load class to check cache,
        Class _class = null;
        try {
            _class = loadClass(className, loader);
        }
        catch(NoClassDefFoundError ncdfe){ 
           /* A mapped element and a Java Class name can be spelled 
              the same, but have different capitalization. This situation 
              will throw this type of error- the ClassLoader will complain 
              that the class is the 'wrong name' (i.e.
              saltyfood vs. SaltyFood).
            */
            //-- save exception, for future calls with
            //-- the same classname
            saveClassNotFound(className, 
                new ClassNotFoundException(ncdfe.getMessage()));
        }
        catch(ClassNotFoundException cnfe) { 
            //-- save exception, for future calls with
            //-- the same classname
            saveClassNotFound(className, cnfe);
        }
        
        if (_class != null) {
            classDesc = resolve(_class);
        }
        
        //-- try to load ClassDescriptor with no class being
        //-- present...does this make sense?
        if ((classDesc == null) && (_class == null)) {
            String dClassName = className+DESCRIPTOR_PREFIX;
            try {
	            Class dClass = loadClass(dClassName, loader);
                classDesc = (XMLClassDescriptor) dClass.newInstance();
                if (classDesc.getJavaClass() != null) {
                    associate(classDesc.getJavaClass(), classDesc);
                }
            }
            catch(InstantiationException ie) {  
                //-- do nothing for now
            }
            catch(IllegalAccessException iae)  {
                //-- do nothing for now
            }
            catch(ClassNotFoundException cnfe) { 
                //-- save exception, for future calls with
                //-- the same classname
                saveClassNotFound(className, cnfe);
            }
        }
        return classDesc;
    } //-- resolve(String, ClassLoader)
    
    /**
     * Saves the ClassNotFoundException for future calls to the resolver
     * 
     * @param className
     * @param cnfe
     */
    private void saveClassNotFound
        (String className, ClassNotFoundException cnfe) 
    {
        //-- save exception, for future calls with
        //-- the same classname
        if (_classNotFoundList == null) {
            _classNotFoundList = new Hashtable();
        }
        _classNotFoundList.put(className, cnfe);
        
    } //-- saveClassNotFound
    
    /**
     * Returns the first XMLClassDescriptor that matches the given
     * XML name and namespaceURI. Null is returned if no descriptor
     * can be found.
     *
     * @param className the class name to find the XMLClassDescriptor for
     * @param loader the ClassLoader to use
     * @return the XMLClassDescriptor for the given XML name
    **/
    public XMLClassDescriptor resolveByXMLName
        (String xmlName, String namespaceURI, ClassLoader loader) 
    {
        if ((xmlName == null) || (xmlName.length() == 0)) {
            String error = "Cannot resolve a null or zero-length xml name.";
            throw new IllegalArgumentException(error);
        }
        
        XMLClassDescriptor classDesc = null;
        Enumeration enumeration             = null;
        
        //-- check name cache first...
        String nameKey = xmlName;
        if ((namespaceURI != null) && (namespaceURI.length() > 0))
            nameKey = namespaceURI + ':' + xmlName;
            
        classDesc = (XMLClassDescriptor)_cacheViaName.get(nameKey);
        if(classDesc != null)
            return classDesc;

        //-- next check mapping loader...
        XMLClassDescriptor possibleMatch = null;
        if (_mappingLoader != null) {
            enumeration = _mappingLoader.listDescriptors();
            while (enumeration.hasMoreElements()) {
                classDesc = (XMLClassDescriptor)enumeration.nextElement();
                if (xmlName.equals(classDesc.getXMLName())) {
                    if (namespaceEquals(namespaceURI, classDesc.getNameSpaceURI())) {
                        _cacheViaName.put(nameKey, classDesc);
                        return classDesc;
                    }
                    possibleMatch = classDesc;
                }
                classDesc = null;
            }
        }
        
        //-- next look in local cache
        enumeration = _cacheViaClass.elements();
        while (enumeration.hasMoreElements()) {
            classDesc = (XMLClassDescriptor)enumeration.nextElement();
            if (xmlName.equals(classDesc.getXMLName())) {
                if (namespaceEquals(namespaceURI, classDesc.getNameSpaceURI())) {
                    _cacheViaName.put(nameKey, classDesc);
                    return classDesc;
                }
                if (possibleMatch == null) 
                    possibleMatch = classDesc;
                else if (possibleMatch != classDesc) {
                    //-- too many possible matches, return null.
                    possibleMatch = null;
                }
            }
            classDesc = null;
        }
        
        //-- next look in package mappings
        Enumeration mappings = _packageMappings.elements();
        while (mappings.hasMoreElements()) {
            Mapping mapping = (Mapping)mappings.nextElement();
            try {
                MappingResolver resolver = mapping.getResolver(Mapping.XML);
                enumeration = resolver.listDescriptors();
                while (enumeration.hasMoreElements()) {
                    classDesc = (XMLClassDescriptor)enumeration.nextElement();
                    if (xmlName.equals(classDesc.getXMLName())) {
                        if (namespaceEquals(namespaceURI, classDesc.getNameSpaceURI())) {
                            _cacheViaName.put(nameKey, classDesc);
                            return classDesc;
                        }
                        if (possibleMatch == null) 
                            possibleMatch = classDesc;
                        else if (possibleMatch != classDesc) {
                            //-- too many possible matches, return null.
                            possibleMatch = null;
                        }
                    }
                }
            }
            catch(MappingException mx) {}
            classDesc = null;
        }
        
        
        if (classDesc == null) 
            classDesc = possibleMatch;
            
        return classDesc;
   
    } //-- resolveByXMLName

    /**
     * Returns an enumeration of XMLClassDescriptor objects that
     * match the given xml name
     *
     * @param className the class name to find the XMLClassDescriptor for
     * @param loader the ClassLoader to use
     * @return an enumeration of XMLClassDescriptor objects.
    **/
    public ClassDescriptorEnumeration resolveAllByXMLName
        (String xmlName, String namespaceURI, ClassLoader loader)
    {
        if ((xmlName == null) || (xmlName.length() == 0)) {
            String error = "Cannot resolve a null or zero-length xml name.";
            throw new IllegalArgumentException(error);
        }
        
        XCDEnumerator xcdEnumerator  = new XCDEnumerator();
        XMLClassDescriptor classDesc = null;
        Enumeration enumeration             = null;
        
        //-- check mapping loader first
        if (_mappingLoader != null) {
            enumeration = _mappingLoader.listDescriptors();
            while (enumeration.hasMoreElements()) {
                classDesc = (XMLClassDescriptor)enumeration.nextElement();
                if (xmlName.equals(classDesc.getXMLName())) {
                    xcdEnumerator.add(classDesc);
                }
            }
        }
        
        //-- next look in local cache
        enumeration = _cacheViaClass.elements();
        while (enumeration.hasMoreElements()) {
            classDesc = (XMLClassDescriptor)enumeration.nextElement();
            if (xmlName.equals(classDesc.getXMLName())) {
                xcdEnumerator.add(classDesc);
            }
        }
        
        //-- next look in package mappings
        Enumeration mappings = _packageMappings.elements();
        while (mappings.hasMoreElements()) {
            Mapping mapping = (Mapping)mappings.nextElement();
            try {
                MappingResolver resolver = mapping.getResolver(Mapping.XML);
                enumeration = resolver.listDescriptors();
                while (enumeration.hasMoreElements()) {
                    classDesc = (XMLClassDescriptor)enumeration.nextElement();
                    if (xmlName.equals(classDesc.getXMLName())) {
                        xcdEnumerator.add(classDesc);
                    }
                }
            }
            catch(MappingException mx) {}
        }
        
        return xcdEnumerator;
        
    } //-- resolveByXMLName
    
    
    /**
     * Sets the ClassLoader to use when loading class descriptors
     * @param loader the ClassLoader to use
    **/
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
        _loadPackageMappings = loadPackageMappings;
    } //-- setLoadPackageMappings
    
    public void setMappingLoader(XMLMappingLoader mappingLoader) {
        _mappingLoader = mappingLoader;
    } //-- setMappingLoader
    
    //-------------------/
    //- Private Methods -/
    //-------------------/
    
    /**
     * Loads and returns the class with the given class name using the 
     * given loader.
     * @param className the name of the class to load
     * @param loader the ClassLoader to use, this may be null.
    **/
    private Class loadClass(String className, ClassLoader loader) 
        throws ClassNotFoundException
    {
        //-- for performance, check the classNotFound list
        //-- first to prevent searching through all the 
        //-- possible classpaths etc for something we
        //-- already know doesn't exist.
        //-- check classNotFoundList 
        if (_classNotFoundList != null) {
            Object exception = _classNotFoundList.get(className);
            if (exception != null)
                throw (ClassNotFoundException)exception;
        }
        
        try {
            //-- use passed in loader
    	    if ( loader != null )
    		    return loader.loadClass(className);
    		//-- use internal loader
    		else if (_loader != null)
    		    return _loader.loadClass(className);
    		//-- no loader available use Class.forName
    		return Class.forName(className);
        }
        catch(NoClassDefFoundError ncdfe) {
            //-- This can happen if we try to load a class with invalid case,
            //-- for example foo instead Foo.
            throw new ClassNotFoundException(ncdfe.getMessage());
        }
    } //-- loadClass
    
    /** 
     * Compares the two strings for equality. A Null and empty
     * strings are considered equal.
     *
     * @return true if the two strings are considered equal.
     */
    private boolean namespaceEquals(String ns1, String ns2) {
        if (ns1 == null) {
            return ((ns2 == null) || (ns2.length() == 0));
        }
        
        if (ns2 == null) {
            return (ns1.length() == 0);
        }
        
        return ns1.equals(ns2);
    } //-- namespaceEquals
    
    /**
     * Loads a package mapping file
     * 
     * @param packageName
     * @param loader
     * @return true if the package list was loaded during this call
     * @throws ResolverException
     */
    private synchronized boolean loadPackageList
        (String packageName, ClassLoader loader) 
        throws ResolverException
    {
        if (packageName == null) packageName = "";
        
        Properties list = (Properties) _packageCDList.get(packageName);
        if (list != null) {
            return false;
        }
        
        String filename;
        
        if (packageName.length() == 0) {
            filename = PKG_CDR_LIST_FILE;
        }
        else {
            filename = packageName.replace('.', '/');
            filename = filename + "/" + PKG_CDR_LIST_FILE;
        }
        
        if (loader == null) loader = _loader;
        if (loader == null) loader = getClass().getClassLoader();
        if (loader == null) loader = Thread.currentThread().getContextClassLoader();
        URL url = loader.getResource(filename);
        if (url != null) {
            try {
                list = new Properties();
                list.load(url.openStream());
                _packageCDList.put(packageName, list);
                
                Enumeration classes = list.keys();
                XMLClassDescriptor xcd = null;
                while (classes.hasMoreElements()) {
                    String className1 = (String)classes.nextElement();
                    String className2 = list.getProperty(className1);
                    try {
                        Class type = loadClass(className1, loader);
                        Class desc = loadClass(className2, loader);
                        xcd = (XMLClassDescriptor) desc.newInstance();
                        associate(type, xcd);
                    }
                    catch(ClassNotFoundException cnfx) {
                        //-- TODO: report error, but
                        //-- continue
                    }
                    catch(InstantiationException ix) {
                        //-- TODO: report error, but
                        //-- continue
                    }
                    catch(IllegalAccessException iax) {
                        //-- TODO: report error, but
                        //-- continue
                        
                    }
                    
                }
                return true;
            }
            catch(java.io.IOException iox) {
                throw new ResolverException(iox);
            }
        }
		_packageCDList.put(packageName, NULL_CDR_FILE);
        return false;
        
    } //-- loadPackageList
    

    /**
     * Loads a package mapping file
     * 
     * @param packageName
     * @param loader
     * @return
     * @throws ResolverException
     */
    private synchronized Mapping loadPackageMapping
        (String packageName, ClassLoader loader) 
        throws ResolverException
    {
        if (!_loadPackageMappings) return null;
        if (packageName == null) packageName = "";
        
        Mapping mapping = (Mapping)_packageMappings.get(packageName);
        if (mapping != null) {
            if (mapping == NULL_MAPPING) {
                return null;
            }
            return mapping;
        }
        
        String filename;
        
        if (packageName.length() == 0) {
            filename = PKG_MAPPING_FILE;
        }
        else {
            filename = packageName.replace('.', '/');
            filename = filename + "/" + PKG_MAPPING_FILE;
        }
        
        if (loader == null) loader = _loader;
        if (loader == null) loader = getClass().getClassLoader();
        if (loader == null) loader = Thread.currentThread().getContextClassLoader();
        URL url = loader.getResource(filename);
        if (url != null) {
            try {
                mapping = new Mapping(loader);
                mapping.loadMapping(url);
                _packageMappings.put(packageName, mapping);
            }
            catch(java.io.IOException iox) {
                throw new ResolverException(iox);
            }
            catch(MappingException mx) {
                throw new ResolverException(mx);
            }
        }
        else {
            _packageMappings.put(packageName, NULL_MAPPING);
        }
        
        return mapping;
    } //-- loadPackageMapping
    
    private String getPackageName(String className) {
        if (className == null) return null;
        int idx = className.lastIndexOf('.');
        if (idx >= 0) {
            return className.substring(0, idx);
        }
        return null;
    }
    
    /**
     * A locally used implementation of ClassDescriptorEnumeration
     */
    class XCDEnumerator implements ClassDescriptorEnumeration {
        
        private Entry _current = null;
        
        private Entry _last = null;
        
        /**
        * Creates an XCDEnumerator
        **/
        XCDEnumerator() {
            super();
        } //-- XCDEnumerator
        
        /**
        * Adds the given XMLClassDescriptor to this XCDEnumerator
        **/
        protected void add(XMLClassDescriptor classDesc) {
            
            Entry entry = new Entry();
            entry.classDesc = classDesc;
            if (_current == null) {
                _current = entry;
                _last    = entry;
            }
            else {
                _last.next = entry;
                _last = entry;
            }
        } //-- add
        
        /** 
        * Returns true if there are more XMLClassDescriptors
        * available.
        *
        * @return true if more XMLClassDescriptors exist within this
        * enumeration.
        **/
        public boolean hasNext() {
            return (_current != null);
        } //-- hasNext
        
        /**
        * Returns the next XMLClassDescriptor in this enumeration.
        *
        * @return the next XMLClassDescriptor in this enumeration.
        **/
        public XMLClassDescriptor getNext() {
            if (_current == null) return null;
            Entry entry = _current;
            _current = _current.next;
            return entry.classDesc;
        } //-- getNext
            
        class Entry {
            XMLClassDescriptor classDesc = null;
            Entry next = null;
        }
        
    } //-- ClassDescriptorEnumeration
    
} //-- ClassDescriptorResolverImpl




