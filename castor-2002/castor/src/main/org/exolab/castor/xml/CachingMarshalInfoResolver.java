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


package org.exolab.castor.xml;

import java.util.Hashtable;

/**
 * The default implementation of the MarshalInfoResolver interface
 * @author <a href="mailto:kvisco@exoffice.com">Keith Visco</a>
 * @version $Revision$ $Date$
**/
public class CachingMarshalInfoResolver 
    implements MarshalInfoResolver 
{
    
    
    private String IO_ERR = 
        "IOException was thrown in MarshalHelper#getMarshalInfo(Class), " +
        "no additional information available.";
        
    private Hashtable _cache = null;
    
    private boolean _error      = false;
    private String  _errMessage = null;
    
    
    public CachingMarshalInfoResolver() {
        _cache = new Hashtable();
    } //-- CachingMarshalInfoResolver
    
    /**
     * Associates (or binds) a class type with a given MarshalInfo
     * @param type the Class to associate
     * @param mInfo the MarshalInfo to associate the given class with
    **/
    public void associate(Class type, MarshalInfo mInfo) {
        _cache.put(type, mInfo);
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
     * Returns the MarshalInfo for the given class, if no
     * MarshalInfo class is found, null will be returned
     * @param type the Class to find the MarshalInfo for
     * @return the MarshalInfo for the given class
    **/
    public MarshalInfo resolve(Class type) {
        
        clearError();
        
        if (type == null) return null;
        
        MarshalInfo mInfo = (MarshalInfo) _cache.get(type);
        if (mInfo != null) return mInfo;
        try {
            mInfo = MarshalHelper.getMarshalInfo(type);
        }
        catch(java.io.IOException ex) {
            String err = ex.getMessage();
            if (err == null) setError(IO_ERR);
            else setError(err);
        }
        
        if (mInfo != null) {
            _cache.put(type, mInfo);
        }
        return mInfo;
    } //-- resolve
    
    /**
     * Returns the MarshalInfo for the given class name
     * @param className the class name to find the MarshalInfo for
     * @return the MarshalInfo for the given class name
    **/
    public MarshalInfo resolve(String className) {
        return resolve(className, null);
    } //-- resolve(String)
    
    /**
     * Returns the MarshalInfo for the given class name
     * @param className the class name to find the MarshalInfo for
     * @param loader the ClassLoader to use
     * @return the MarshalInfo for the given class name
    **/
    public MarshalInfo resolve(String className, ClassLoader loader) {
        
        MarshalInfo mInfo = null;
        
        if ((className == null) || (className.length() == 0)) {
            clearError(); //-- clear error flag
            return mInfo;
        }
            


        //-- try and load class and check cache,
        Class _class = null;
        try {
	        if ( loader != null )
		        _class = loader.loadClass(className);
	        else
		        _class = Class.forName(className);
        }
        catch(ClassNotFoundException cnfe) {}
        
        if (_class != null) {
            mInfo = resolve(_class);
        }
        else clearError(); //-- clear error flag
        
        //-- try to load just MarshalInfo
        if ((mInfo == null) && (_class == null)) {
            String mClassName = className+"MarshalInfo";
            try {
	            Class mClass;
	            if ( loader != null )
		            mClass = loader.loadClass(mClassName);
	            else
		            mClass = Class.forName(mClassName);
    		        
                mInfo = (MarshalInfo) mClass.newInstance();
            }
            catch(InstantiationException ie)   { /* :-) */ }
            catch(ClassNotFoundException cnfe) { /* ;-) */ }
            catch(IllegalAccessException iae)  { /* :-Þ */ }
        }
        
        return mInfo;
    } //-- resolve(String, ClassLoader)
    
    
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
    
} //-- CachingMarshalInfoResolver
