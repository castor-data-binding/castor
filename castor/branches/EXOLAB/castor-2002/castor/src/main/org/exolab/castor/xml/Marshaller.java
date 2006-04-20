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

//-- xml related imports
import org.xml.sax.*;
import org.apache.xml.serialize.Serializer;
import org.exolab.castor.util.Configuration;
import org.xml.sax.helpers.AttributeListImpl;

import java.io.PrintWriter;
import java.io.Serializable;
import java.io.Writer;
import java.lang.reflect.*;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;


/**
 * A Marshaller to allowing Marshalling Java Object's to XML
 * @author <a href="mailto:kvisco@exoffice.com">Keith Visco</a>
 * @version $Revision$ $Date$
**/
public class Marshaller {
    
    
    /**
     * A flag indicating whether or not to generate 
     * debug information
    **/
    private boolean _debug = false;
    

    /**
     * The print writer used for logging
    **/
    private PrintWriter _logWriter = null;
    
    /**
     * The NameSpace Prefix to URI table
    **/
    private Hashtable _nsPrefixKeyHash = null;
    
    /**
     * The NameSpace URI to Prefix table
    **/
    private Hashtable _nsURIKeyHash = null;
    
    /**
     * The current namespace scoping
    **/
    private Vector _nsScope = null;
    
    /**
     * The MarshalInfoResolver used for Resolving MarshalInfo classes
    **/
    private MarshalInfoResolver _mResolver = null;
    
    /**
     * The depth of the sub tree, 0 denotes document level
    **/
    int depth = 0;
    
    private Vector _packages = null;
    
    /**
     * Creates a new Marshaller
    **/
    private Marshaller() {
        _nsPrefixKeyHash = new Hashtable(3);
        _nsURIKeyHash    = new Hashtable(3);
        _nsScope         = new Vector(3);
        _packages        = new Vector(3);
        _mResolver       = new CachingMarshalInfoResolver();
    } //-- Marshaller
    
    
    /**
     * Marshals the given Object as XML using the given Writer.
     * @param obj the Object to marshal
     * @param out the Writer to marshal to
     * @exception java.io.IOException
     * @exception org.xml.sax.SAXException
    **/
    public static void marshal(Object obj, Writer out) 
        throws java.io.IOException, org.xml.sax.SAXException
    {
        marshal(obj, out, null);
    } //-- void marshal(Writer) 

    /**
     * Marshals the given Object as XML using the given Writer.
     * @param obj the Object to marshal
     * @param out the Writer to marshal to
     * @param logger the PrintWriter to write log information to
     * @exception java.io.IOException
     * @exception org.xml.sax.SAXException
    **/
    public static void marshal(Object obj, Writer out, PrintWriter logger) 
        throws java.io.IOException, org.xml.sax.SAXException
    {
        marshal(obj, out, logger, Configuration.debug());
    } //-- void marshal(Writer) 

    /**
     * Marshals the given Object as XML using the given Writer.
     * @param obj the Object to marshal
     * @param out the Writer to marshal to
     * @param logger the PrintWriter to write log information to
     * @exception java.io.IOException
     * @exception org.xml.sax.SAXException
    **/
    public static void marshal
        (Object obj, Writer out, PrintWriter logger, boolean debug) 
        throws java.io.IOException, org.xml.sax.SAXException
    {
        //-- we must have a valid element before marshalling
        //validate(false);
        marshal(obj, Configuration.getSerializer( out ), logger, debug);
        out.flush();
    } //-- void marshal(Writer) 


    /**
     * Marshals the given Object as XML using the given DocumentHandler.
     * @param obj the Object to marshal
     * @param handler the DocumentHandler to marshal to
     * @exception java.io.IOException
     * @exception org.xml.sax.SAXException
    **/
    public static void marshal(Object object, DocumentHandler handler) 
        throws java.io.IOException, org.xml.sax.SAXException
    {
        Marshaller marshaller = new Marshaller();
        marshaller.validate(object);
        marshaller.marshal(object, null, handler);
    } //-- marshal
    
    /**
     * Marshals the given Object as XML using the given DocumentHandler.
     * @param obj the Object to marshal
     * @param handler the DocumentHandler to marshal to
     * @param logger the PrintWriter to write log messages to
     * @param debug a flag indicating whether or not to generate debug 
     * information
     * @exception java.io.IOException
     * @exception org.xml.sax.SAXException
    **/
    public static void marshal 
    (
        Object object, 
        DocumentHandler handler, 
        PrintWriter logger, 
        boolean debug 
    ) 
        throws java.io.IOException, org.xml.sax.SAXException
    {
        Marshaller marshaller = new Marshaller();
        marshaller.validate(object);
        marshaller.setLogWriter(logger);
        marshaller.setDebug(debug);
        marshaller.marshal(object, null, handler);
    } //-- marshal
    
    /**
     * It is an error if this method is called with an AttributeDescriptor.
     * @param handler the DocumentHandler to marshal to
     * @exception java.io.IOException 
     * @exception org.xml.sax.SAXException when an error occurs
     * during marshaling
    **/
    public void marshal
        (Object object, MarshalDescriptor descriptor, DocumentHandler handler) 
        throws java.io.IOException, org.xml.sax.SAXException
    {
        
        if (object == null) {
            String err = "null passed as parameter to Marshaller#marshal"; 
            throw new SAXException(err);
        }
        
        Class _class = object.getClass();
        
        
        
        if (descriptor == null)
            descriptor = new SimpleMarshalDescriptor(_class, null);

        //-- calculate Object's name
        String name = descriptor.getXMLName();
        if (name == null) {
            name = _class.getName();
            //-- remove package information from name
            int idx = name.lastIndexOf('.');
            if (idx >= 0) {
                name = name.substring(idx+1);
            }
            //-- remove capitalization
            name = MarshalHelper.toXMLName(name);
        }
            
        MarshalInfo marshalInfo = descriptor.getMarshalInfo();
        if (marshalInfo == null) {
            
            //-- check for primitive or String, we need to use
            //-- the special #isPrimitive method of this class
            //-- so that we can check for the primitive wrapper 
            //-- classes
            if (isPrimitive(_class) || (_class == String.class)) {
                
                //-- look for marshalInfo based on element name
                String cname = MarshalHelper.toJavaName(name,true);
                
                for (int i = 0; i < _packages.size(); i++) {
                    String pkgName = (String)_packages.elementAt(i);
                    String className = pkgName+cname;
                    marshalInfo = MarshalHelper.getMarshalInfo(className);
                    if (marshalInfo != null) break;
                }
                if (marshalInfo == null) {
                    marshalInfo = new StringMarshalInfo();
                }
            }
            else {
                //-- save package information for use when searching
                //-- for MarshalInfo classes
                String className = _class.getName();
                int idx = className.lastIndexOf(".");
                if (idx > 0) {
                    String pkgName = className.substring(0,idx+1);
                    if (!_packages.contains(pkgName))
                        _packages.addElement(pkgName);
                }
                marshalInfo = getMarshalInfo(_class);
            }
            
            if (marshalInfo == null) {
                
                //-- make sure we are allowed to marshal Object
                if ((_class == Void.class) ||
                    (_class == Object.class) ||
                    (_class == Class.class)) {
                        
                    throw new MarshalException
                        (MarshalException.BASE_CLASS_OR_VOID_ERR);
                }
                else if ((!_class.isPrimitive()) && 
		            (!Serializable.class.isAssignableFrom( _class ))) {
		                
		            if (depth == 0) {
                        throw new MarshalException
                            (MarshalException.NON_SERIALIZABLE_ERR);
                    }
                }
                return;
            }
        }
        
        
        //-- handle Attributes
        AttributeListImpl atts = new AttributeListImpl();
        
        MarshalDescriptor[] descriptors = marshalInfo.getAttributeDescriptors();
        for (int i = 0; i < descriptors.length; i++) {
            
            if (descriptors[i] == null) continue;
            
            MarshalDescriptor attDescriptor = descriptors[i];
            
            String xmlName = attDescriptor.getXMLName();
            
            //-- handle attribute namespaces
            //-- [need to add this support]
            
            Object value = null;
            try {
                value = attDescriptor.getValue(object);
            }
            catch(InvocationTargetException ite) {
                continue;
            }
            catch(IllegalAccessException iae) {
                continue;
            }
            if (value == null) continue;
            
            atts.addAttribute(xmlName, null, value.toString());
        }
        
        //------------------/
        //- Create element -/
        //------------------/
        
        //-- namespace management
        boolean addedNamespace = false;
        
        String nsPrefix = descriptor.getNameSpacePrefix();
        if (nsPrefix == null) nsPrefix = marshalInfo.getNameSpacePrefix();
            
        String nsURI = descriptor.getNameSpaceURI();
        if (nsURI == null) nsURI = marshalInfo.getNameSpaceURI();
        
        if ((nsURI == null) && (nsPrefix != null)) {
            nsURI = (String) _nsPrefixKeyHash.get(nsPrefix);
        }
        else if ((nsPrefix == null) && (nsURI != null)) {
            nsPrefix = (String) _nsURIKeyHash.get(nsURI);
        }
        
        if (nsURI != null) {
            if (!_nsScope.contains(nsURI)) {
                String attName = "xmlns";
                if (nsPrefix != null) {
                    if (nsPrefix.length() > 0) 
                        attName += ":" + nsPrefix;
                    _nsScope.addElement(nsURI);
                    addedNamespace = true;
                }
                atts.addAttribute(attName, null, nsURI);
            }
        }
        
        if ((nsPrefix != null) && (nsPrefix.length() > 0))
            handler.startElement(nsPrefix+":"+name, atts);
        else 
            handler.startElement(name, atts);
        
        
        //-- handle text content
        MarshalDescriptor cdesc = marshalInfo.getContentDescriptor();
        if (cdesc != null) {
            Object obj = null;
            try {
                obj = cdesc.getValue(object);
            }
            catch(InvocationTargetException ite) {}
            catch(IllegalAccessException iae) {};
            if (obj != null) {
                String str = obj.toString();
                if ((str != null) && (str.length() > 0)) {
                    char[] chars = str.toCharArray();
                    handler.characters(chars, 0, chars.length);
                }
            }
        }
        /* special case for Strings and primitives */
        else if ((_class == String.class) || isPrimitive(_class)) { 
            char[] chars = object.toString().toCharArray();
            handler.characters(chars,0,chars.length);
        }

        
        
        //-- handle daughter elements
        descriptors = marshalInfo.getElementDescriptors();
        
        ++depth;
        for (int i = 0; i < descriptors.length; i++) {
            
            MarshalDescriptor elemDescriptor = descriptors[i];
            
            Object obj = null;
            try {
                obj = elemDescriptor.getValue(object);
            }
            catch(InvocationTargetException ite) {
                continue;
            }
            catch(IllegalAccessException iae) {
                continue;
            }
            if (obj == null) continue;
            
            Class type = obj.getClass();
            
            //-- handle arrays
            if (type.isArray()) {
                int length = Array.getLength(obj);
                for (int j = 0; j < length; j++) {
                    marshal(Array.get(obj, j), elemDescriptor, handler);
                }
            }
            //-- handle enumerations
            else if (obj instanceof java.util.Enumeration) {
                Enumeration enum = (Enumeration)obj;
                while (enum.hasMoreElements()) {
                    marshal(enum.nextElement(), elemDescriptor, handler);
                }
            }
            //-- handle vectors
            else if (obj instanceof java.util.Vector) {
                Vector v = (Vector) obj;
                int length = v.size();
                for (int j = 0; j < length; j++) {
                    marshal(v.elementAt(j), elemDescriptor, handler);
                }
            }
            else marshal(obj, elemDescriptor, handler);
        }
        --depth;
        
        //-- finish element
        if ((nsPrefix != null) && (nsPrefix.length() > 0))
            handler.endElement(nsPrefix+":"+name);
        else 
            handler.endElement(name);
        
        if (addedNamespace) _nsScope.removeElement(nsURI);
        
    } //-- void marshal(DocumentHandler) 
    
    
    /**
     * Sets the flag to turn on and off debugging
     * @param debug the flag indicating whether or not debug information
     * should be generated
    **/
    public void setDebug(boolean debug) {
        this._debug = debug;
    } //-- setDebug
    
    /**
     * Sets the PrintWriter used for logging
     * @param printWriter the PrintWriter to use for logging
    **/
    public void setLogWriter(PrintWriter printWriter) {
        this._logWriter = printWriter;
    } //-- setLogWriter
    
    /**
     * Finds and returns a MarshalInfo for the given class. If
     * a MarshalInfo could not be found one will attempt to 
     * be generated. 
     * @param _class the Class to get the MarshalInfo for
    **/
    private MarshalInfo getMarshalInfo(Class _class) 
        throws java.io.IOException
    {
        MarshalInfo mInfo = _mResolver.resolve(_class);
        if (_mResolver.error()) {
            throw new java.io.IOException(_mResolver.getErrorMessage());
        }
        return mInfo;
    } //-- getMarshalInfo
    
    
    private void validate(Object object) 
        throws java.io.IOException
    {
        //-- we must have a valid element before marshalling
        try {
           Validator.validate(object, _mResolver);
        }
        catch(ValidationException valex) {
            String err = "validation error: " + valex.getMessage();
            throw new java.io.IOException(err);
        }
    }
    
    private boolean isPrimitive(Class type) {
        
        if (type.isPrimitive()) return true;
        
        if ((type == Boolean.class)   ||
            (type == Byte.class)      ||
            (type == Character.class) ||
            (type == Double.class)    ||
            (type == Float.class)     ||
            (type == Integer.class)   ||
            (type == Long.class)      ||
            (type == Short.class)) 
            return true;
            
       return false;
       
    } //-- isPrimitive
    
} //-- Marshaller

