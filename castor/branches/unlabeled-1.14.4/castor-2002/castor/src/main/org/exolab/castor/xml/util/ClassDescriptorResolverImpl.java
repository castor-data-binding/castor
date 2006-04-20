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
 *    permission of Exoffice Technologies.  For written permission,
 *    please contact info@exolab.org.
 *
 * 4. Products derived from this Software may not be called "Exolab"
 *    nor may "Exolab" appear in their names without prior written
 *    permission of Exoffice Technologies. Exolab is a registered
 *    trademark of Exoffice Technologies.
 *
 * 5. Due credit should be given to the Exolab Project
 *    (http://www.exolab.org/).
 *
 * THIS SOFTWARE IS PROVIDED BY EXOFFICE TECHNOLOGIES AND CONTRIBUTORS
 * ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT
 * NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL
 * EXOFFICE TECHNOLOGIES OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * Copyright 1999 (C) Exoffice Technologies Inc. All Rights Reserved.
 *
 * $Id$
 */


package org.exolab.castor.xml.util;

import org.exolab.castor.xml.*;

import java.util.Hashtable;
import java.util.Enumeration;

/**
 * The default implementation of the ClassDescriptorResolver interface
 * @author <a href="mailto:kvisco@exoffice.com">Keith Visco</a>
 * @version $Revision$ $Date$
**/
public class ClassDescriptorResolverImpl
    implements ClassDescriptorResolver 
{
 
    private static final String DESCRIPTOR_PREFIX = "Descriptor";
    
    /**
     * internal cache for class descriptors
    **/
    private Hashtable _cache      = null;
    
    private boolean   _error      = false;
    private String    _errMessage = null;
    
    private XMLMappingLoader mappingLoader = null;
    
    private ClassLoader _loader  = null;
    
    /**
     * Creates a new ClassDescriptorResolverImpl
    **/
    public ClassDescriptorResolverImpl() {
        _cache = new Hashtable();
    } //-- ClassDescriptorResolverImpl

    /**
     * Creates a new ClassDescriptorResolverImpl with the given ClassLoader
     * @param loader the ClassLoader to use when loading ClassDescriptors
    **/
    public ClassDescriptorResolverImpl(ClassLoader loader) {
        _cache  = new Hashtable();
        _loader = loader;
    } //-- ClassDescriptorResolverImpl
    
    /**
     * Associates (or binds) a class type with a given MarshalInfo
     * @param type the Class to associate
     * @param mInfo the MarshalInfo to associate the given class with
    **/
    public void associate(Class type, XMLClassDescriptor classDesc) {
        _cache.put(type, classDesc);
    } //-- associate
    
    /**
     * Returns the last error message generated
     * If no error String exists, null will be returned
     * @return the last error message generated.
     * If no error String exists, null will be returned
    **/
    public String getErrorMessage() {
        return _errMessage;
    } //-- getErrorMessage
    
    public XMLMappingLoader getMappingLoader() {
        return mappingLoader;
    } //-- getXMLMappingLoader
    
    /**
     * Returns true if an error was generated on the last call
     * to one of the resolve methods
     * @return true if an error was generated on the last call
     * to one of the resolve methods
    **/
    public boolean error() {
        return _error;
    } //-- error
    
    /**
     * Returns the XMLClassDescriptor for the given class
     * @param type the Class to find the XMLClassDescriptor for
     * @return the XMLClassDescriptor for the given class
    **/
    public XMLClassDescriptor resolve(Class type) {
        
        clearError();
        
        if (type == null) return null;
        
        XMLClassDescriptor classDesc = (XMLClassDescriptor) _cache.get(type);
        
        if (classDesc != null) return classDesc;
        
        //-- check mapping loader first 
        //-- [proposed by George Stewart]
        if (mappingLoader != null) {            
            classDesc = (XMLClassDescriptor)mappingLoader.getDescriptor(type);
            if (classDesc != null) {
               _cache.put(type, classDesc);
               return classDesc;
            }
        }
       
         
        String className = type.getName() + DESCRIPTOR_PREFIX;
        try {
            ClassLoader loader = type.getClassLoader();
            Class dClass = loadClass(className, loader);            
            classDesc = (XMLClassDescriptor) dClass.newInstance();
            _cache.put(type, classDesc);
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
            setError(err);
            return null;
        }
        
        //-- create classDesc automatically if necessary
        if (classDesc == null) {
            try {
                classDesc = MarshalHelper.generateClassDescriptor(type);
                if (classDesc != null) _cache.put(type, classDesc);
            }
            catch (MarshalException mx) {
                String err = mx.toString();
                setError(err);
                return null;
            }
        }
        return classDesc;
    } //-- resolve
    
    /**
     * Returns the XMLClassDescriptor for the given class name
     * @param className the class name to find the XMLClassDescriptor for
     * @return the XMLClassDescriptor for the given class name
    **/
    public XMLClassDescriptor resolve(String className) {
        return resolve(className, null);
    } //-- resolve(String)
    
    /**
     * Returns the XMLClassDescriptor for the given class name
     * @param className the class name to find the XMLClassDescriptor for
     * @param loader the ClassLoader to use
     * @return the XMLClassDescriptor for the given class name
    **/
    public XMLClassDescriptor resolve(String className, ClassLoader loader) {
        
        XMLClassDescriptor classDesc = null;
        
        if ((className == null) || (className.length() == 0)) {
            clearError(); //-- clear previous error flag
            setError("Cannot resolve a null or zero-length class name.");
            return null;
        }
            


        //-- try and load class to check cache,
        Class _class = null;
        try {
            _class = loadClass(className, loader);
        }
        catch(ClassNotFoundException cnfe) { 
            //-- do nothing for now
        }
        
        if (_class != null) {
            classDesc = resolve(_class);
        }
        else clearError(); //-- clear error flag
        
        //-- try to load ClassDescriptor with no class being
        //-- present...does this make sense?
        if ((classDesc == null) && (_class == null)) {
            String dClassName = className+"Descriptor";
            try {
	            Class dClass = loadClass(dClassName, loader);
                classDesc = (XMLClassDescriptor) dClass.newInstance();
            }
            catch(InstantiationException ie)   { /* :-) */ }
            catch(ClassNotFoundException cnfe) { /* ;-) */ }
            catch(IllegalAccessException iae)  { /* :-Þ */ }
        }
        return classDesc;
    } //-- resolve(String, ClassLoader)
    
    /**
     * Returns the XMLClassDescriptor for the given xml name
     * @param className the class name to find the XMLClassDescriptor for
     * @param loader the ClassLoader to use
     * @return the XMLClassDescriptor for the given class name
    **/
    public XMLClassDescriptor resolveByXMLName
        (String xmlName, ClassLoader loader) 
    {
        if ((xmlName == null) || (xmlName.length() == 0)) {
            clearError(); //-- clear previous error flag
            setError("Cannot resolve a null or zero-length xml name.");
            return null;
        }
                
        XMLClassDescriptor classDesc = null;
                
        Enumeration enum = null;
        
        if (mappingLoader != null)
            enum = mappingLoader.listDescriptors();
        else
            enum = _cache.elements();
        
        while (enum.hasMoreElements()) {
            classDesc = (XMLClassDescriptor)enum.nextElement();
            if (xmlName.equals(classDesc.getXMLName())) {
                return classDesc;
            }
            classDesc = null;
        }
        
        return classDesc;
        
    } //-- resolveByXMLName
    
    /**
     * Sets the ClassLoader to use when loading class descriptors
     * @param loader the ClassLoader to use
    **/
    public void setClassLoader(ClassLoader loader) {
        this._loader = loader;
    } //-- setClassLoader
    
    public void setMappingLoader(XMLMappingLoader mappingLoader) {
        this.mappingLoader = mappingLoader;
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
        Class c = null;
        
        //-- use passed in loader
	    if ( loader != null )
		    return loader.loadClass(className);
		//-- use internal loader
		else if (_loader != null)
		    return _loader.loadClass(className);
		//-- no loader available use Class.forName
		return Class.forName(className);
    } //-- loadClass
    
    /**
     * Clears the error flag
    **/
    private void clearError() {
        _error = false;
    } //-- clearError
    
    /**
     * Sets the current error message to the given String
     * @param message the error message
    **/
    private void setError(String message) {
        _error = true;
        _errMessage = message;
    } //-- setError
    
} //-- ClassDescriptorResolverImpl




