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
 * Copyright 1999, 2000 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id$
 */

package org.exolab.castor.xml;

//-- xml related imports
import org.xml.sax.*;
import org.apache.xml.serialize.Serializer;
import org.apache.xml.serialize.OutputFormat;
import org.exolab.castor.mapping.Mapping;
import org.exolab.castor.mapping.FieldHandler;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.xml.util.*;
import org.exolab.castor.util.Configuration;
import org.exolab.castor.util.Messages;
import org.exolab.castor.util.MimeBase64Encoder;
import org.exolab.castor.util.List;
import org.exolab.castor.util.Stack;

import org.xml.sax.helpers.AttributeListImpl;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.io.Writer;
import java.lang.reflect.*;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;


/**
 * A Marshaller to allowing serializing Java Object's to XML
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a>
 * @version $Revision$ $Date$
**/
public class Marshaller {

    /**
     * Message name for a non sax capable serializer error
    **/
    private static final String SERIALIZER_NOT_SAX_CAPABLE
        = "conf.serializerNotSaxCapable";


    /**
     * The namespace declaration String
    **/
    private static final String XMLNS  = "xmlns";

    /**
     * Namespace declaration for xml schema instance
    **/
    private static final String XSI_PREFIX = "xsi";

    /**
     * The xsi:type attribute
    **/
    private static final String XSI_TYPE = "xsi:type";

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
     * The default namespace
    **/
    private String _defaultNamespace = null;

    /**
     * The validation flag
    **/
    private boolean _validate = false;

    
    /**
     * The current namespace scoping
    **/
    private List _nsScope = null;

    /**
     * The ClassDescriptorResolver used for resolving XMLClassDescriptors
    **/
    private ClassDescriptorResolver _cdResolver = null;

    private Hashtable        _cdCache      = null;
    private DocumentHandler  _handler      = null;
    private Serializer       _serializer   = null;

    /**
     * The depth of the sub tree, 0 denotes document level
    **/
    int depth = 0;

    private List   _packages = null;

    private Stack   _parents  = null;

    /**
     * An instance of StringClassDescriptor
    **/
    private static final StringClassDescriptor _StringClassDescriptor
        = new StringClassDescriptor();

    /**
     * Creates a new Marshaller
    **/
    public Marshaller( DocumentHandler handler ) {
        if ( handler == null )
            throw new IllegalArgumentException( "Argument 'handler' is null." );
        _handler         = handler;

        // call internal initializer
        initialize();
    } //-- Marshaller


    /**
     * Creates a new Marshaller with the given writer
     * @param out the Writer to serialize to
    **/
    public Marshaller( Writer out )
        throws IOException
    {
        if (out == null)
            throw new IllegalArgumentException( "Argument 'out' is null.");

        // call internal initializer
        initialize();

        _serializer = Configuration.getSerializer();

        if (_serializer == null)
            throw new RuntimeException("Unable to obtain serailizer");

        _serializer.setOutputCharStream( out );
        _handler = _serializer.asDocumentHandler();
        if ( _handler == null ) {
            String err = Messages.format( this.SERIALIZER_NOT_SAX_CAPABLE,
                                          _serializer.getClass().getName() );
            throw new RuntimeException( err );
        }
    } //-- Marshaller


    /**
     * Initializes this Marshaller. This is common code shared among
     * the Constructors
    **/
    private void initialize() {
        _nsPrefixKeyHash = new Hashtable(3);
        _nsURIKeyHash    = new Hashtable(3);
        _nsScope         = new List(3);
        _packages        = new List(3);
        _cdResolver      = new ClassDescriptorResolverImpl();
        _cdCache         = new Hashtable(3);
        _parents         = new Stack();
        _validate        = Configuration.marshallingValidation();
    } //-- initialize();

    /**
     * Sets whether or not to marshal as a document which includes
     * the XML declaration, and if necessary the DOCTYPE declaration.
     * By default the Marshaller will marshal as a well formed
     * XML fragment (no XML declaration or DOCTYPE).
     *
     * @param asDocument a boolean, when true, indicating to marshal
     * as a complete XML document.
    **/
    public void setMarshalAsDocument(boolean asDocument) {

        if (_serializer != null) {
            OutputFormat format = Configuration.getOutputFormat();
            format.setOmitXMLDeclaration( ! asDocument );
            _serializer.setOutputFormat( format );

            try {
                _handler = _serializer.asDocumentHandler();
            }
            catch (java.io.IOException iox) {
                //-- we can ignore this exception since it shouldn't
                //-- happen. If _serializer is not null, it means
                //-- we've already called this method sucessfully
                //-- in the Marshaller() constructor
            }
        }

    } //-- setMarshalAsDocument

    /**
     * Sets the given mapping to be used by the marshalling
     * Framework. If a ClassDescriptorResolver exists
     * This mapping will be added to the existing Resolver. Otherwise
     * a new ClassDescriptorResolver will be created.
     *
     * @param mapping the mapping to using during marshalling
    **/
    public void setMapping( Mapping mapping )
        throws MappingException
    {
        if (_cdResolver == null)
            _cdResolver = new ClassDescriptorResolverImpl();

        _cdResolver.setMappingLoader( (XMLMappingLoader) mapping.getResolver( Mapping.XML ) );
    } //-- setMapping

    /**
     * Sets the mapping for the given Namespace prefix
     * @param nsPrefix the namespace prefix
     * @param nsURI the namespace that the prefix resolves to
    **/
    public void setNamespaceMapping(String nsPrefix, String nsURI) {

        if ((nsURI == null) || (nsURI.length() == 0)) {
            String err = "namespace URI must not be null.";
            throw new IllegalArgumentException(err);
        }

        if ((nsPrefix == null) || (nsPrefix.length() == 0)) {
            _defaultNamespace = nsURI;
            return;
        }

        _nsPrefixKeyHash.put(nsPrefix, nsURI);
        _nsURIKeyHash.put(nsURI, nsPrefix);

    } //-- setNamespacePrefix

    /**
     * Sets the ClassDescriptorResolver to use during unmarshalling
     * @param cdr the ClassDescriptorResolver to use
     * @see setMapping
     * <BR />
     * <B>Note:</B> This method will nullify any Mapping
     * currently being used by this Marshaller
    **/
    public void setResolver( ClassDescriptorResolver cdr ) {

        if (cdr != null) _cdResolver = cdr;

    } //-- setResolver

    /**
     * Sets whether or not to validate the object model
     * before marshalling. By default validation is enabled.
     * This method is really for debugging.
     * I do not recommend turning off validation, since
     * you could marshal a document, which you can then
     * not unmarshal. If you know the object model
     * is guaranteed to be valid, disabling validation will
     * improve performace.
     *
     * @param validate the boolean indicating whether or not to
     * validate the object model before marshalling.
    **/
    public void setValidation(boolean validate) {
        _validate = validate;
    } //-- setValidation

    /**
     * Marshals the given Object as XML using the given writer
     * @param obj the Object to marshal
     * @param out the writer to marshal to
     * @exception org.exolab.castor.xml.MarshalException
     * @exception org.exolab.castor.xml.ValidationException
    **/
    public static void marshal(Object object, Writer out)
        throws MarshalException, ValidationException
    {
        Marshaller marshaller;

        try {
            marshaller = new Marshaller(out);
            marshaller.marshal(object);
        } catch ( IOException except ) {
            throw new MarshalException( except );
        }
    } //-- marshal

    /**
     * Marshals the given Object as XML using the given DocumentHandler
     * to send events to.
     * @param obj the Object to marshal
     * @param handler the DocumentHandler to marshal to
     * @exception org.exolab.castor.xml.MarshalException
     * @exception org.exolab.castor.xml.ValidationException
    **/
    public static void marshal(Object object, DocumentHandler handler)
        throws MarshalException, ValidationException
    {
        Marshaller marshaller;

        marshaller = new Marshaller(handler);
        marshaller.marshal(object);
    } //-- marshal

    /**
     * Marshals the given Object as XML using the DocumentHandler
     * for this Marshaller.
     * @param obj the Object to marshal
     * @exception org.exolab.castor.xml.MarshalException
     * @exception org.exolab.castor.xml.ValidationException
    **/
    public void marshal(Object object)
        throws MarshalException, ValidationException
    {
        validate(object);
        marshal(object, null, _handler);
    } //-- marshal

    /**
     * Marshals the given object, using the given descriptor
     * and document handler.
     *
     * <BR/>
     * <B>Note:</B>
     * <I>
     *   It is an error if this method is called with an
     *   AttributeDescriptor.
     * </I>
     * @param descriptor the XMLFieldDescriptor for the given object
     * @param handler the DocumentHandler to marshal to
     * @exception org.exolab.castor.xml.MarshalException
     * @exception org.exolab.castor.xml.ValidationException
     * during marshaling
    **/
    private void marshal
        (Object object,
         XMLFieldDescriptor descriptor,
         DocumentHandler handler)
        throws MarshalException, ValidationException
    {
        if (object == null) {
            String err = "Marshaller#marshal: null parameter: 'object'";
            throw new IllegalArgumentException(err);
        }


        //-- add object to stack so we don't potentially get into
        //-- an endlessloop
        if (_parents.search(object) >= 0) return;
        _parents.push(object);

        Class _class = object.getClass();

        boolean byteArray = false;
        if (_class.isArray())
            byteArray = (_class.getComponentType() == Byte.TYPE);

        if (descriptor == null) {
            descriptor = new XMLFieldDescriptorImpl(_class, "root", null, null);
        }

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

        //-- obtain the class descriptor
        XMLClassDescriptor classDesc = null;
        boolean saveType = false; /* flag for xsi:type */

        if (_class == descriptor.getFieldType())
            classDesc = (XMLClassDescriptor)descriptor.getClassDescriptor();

        if (classDesc == null) {

            //-- check for primitive or String, we need to use
            //-- the special #isPrimitive method of this class
            //-- so that we can check for the primitive wrapper
            //-- classes
            if (isPrimitive(_class) || (_class == String.class) || byteArray)
            {

                classDesc = _StringClassDescriptor;

                //-- check to see if we need to save the xsi:type
                //-- for this class
                saveType = (descriptor.getFieldType() == Object.class);

            }
            else {
                //-- save package information for use when searching
                //-- for MarshalInfo classes
                String className = _class.getName();
                int idx = className.lastIndexOf(".");
                if (idx > 0) {
                    String pkgName = className.substring(0,idx+1);
                    if (!_packages.contains(pkgName))
                        _packages.add(pkgName);
                }

                classDesc = getClassDescriptor(_class);
                if (descriptor.getXMLName()==null)
                    name = classDesc.getXMLName();
                    
                //-- check to see if we to save the xsi:type
                //-- for this class
                saveType = (_class != descriptor.getFieldType());
            }

            if (classDesc == null) {

                //-- make sure we are allowed to marshal Object
                if ((_class == Void.class) ||
                    (_class == Object.class) ||
                    (_class == Class.class)) {

                    throw new MarshalException
                        (MarshalException.BASE_CLASS_OR_VOID_ERR);
                }
                _parents.pop();
                return;
            }
        }


        //-- handle Attributes
        AttributeListImpl atts = new AttributeListImpl();

        XMLFieldDescriptor[] descriptors = classDesc.getAttributeDescriptors();
        for (int i = 0; i < descriptors.length; i++) {

            if (descriptors[i] == null) continue;

            XMLFieldDescriptor attDescriptor = descriptors[i];

            String xmlName = attDescriptor.getXMLName();

            //-- handle attribute namespaces
            //-- [need to add this support]

            Object value = null;

            try {
                value = attDescriptor.getHandler().getValue(object);
            }
            catch(IllegalStateException ise) {
                continue;
            }

            //-- handle IDREFs
            if (attDescriptor.isReference() && (value != null)) {
                XMLClassDescriptor cd = getClassDescriptor(value.getClass());
                String err = null;
                if (cd != null) {
                    XMLFieldDescriptor fieldDesc
                        = (XMLFieldDescriptor) cd.getIdentity();
                    if (fieldDesc != null) {
                        FieldHandler fieldHandler = fieldDesc.getHandler();
                        if (fieldHandler != null) {
                            try {
                                value = fieldHandler.getValue(value);
                            }
                            catch(IllegalStateException ise) {
                                err = ise.toString();
                            }
                        }
                        else {
                            err = "FieldHandler for Identity descriptor is null.";
                        }
                    }
                    else err = "No identity descriptor available";
                }
                else  {
                    err = "Unable to resolve ClassDescriptor for: " +
                        value.getClass().getName();
                }

                if (err != null) {
                    String errMsg = "Unable to save reference to: " +
                        cd.getXMLName() + " from element: " +
                        classDesc.getXMLName() +
                        " due to the following error: ";
                    throw new MarshalException(errMsg);
                }
            }

            if (value == null) continue;

            atts.addAttribute(xmlName, null, value.toString());
        }

        //-- xsi:type
        if (saveType) {
            saveType = declareNamespace(XSI_PREFIX,
                MarshalHelper.XSI_NAMESPACE, atts);
            atts.addAttribute(XSI_TYPE, null, "java:"+_class.getName());
        }

        //------------------/
        //- Create element -/
        //------------------/

        //-- namespace management
        String nsPrefix = descriptor.getNameSpacePrefix();
        if (nsPrefix == null) nsPrefix = classDesc.getNameSpacePrefix();

        String nsURI = descriptor.getNameSpaceURI();
        if (nsURI == null) nsURI = classDesc.getNameSpaceURI();

        if ((nsURI == null) && (nsPrefix != null)) {
            nsURI = (String) _nsPrefixKeyHash.get(nsPrefix);
        }
        else if ((nsPrefix == null) && (nsURI != null)) {
            nsPrefix = (String) _nsURIKeyHash.get(nsURI);
        }

        boolean declaredNS = false;
        if (nsURI != null)
            declaredNS = declareNamespace(nsPrefix, nsURI, atts);


        String qName = null;
        if (nsPrefix != null) {
            int len = nsPrefix.length();
            if (len > 0) {
                StringBuffer sb = new StringBuffer(len+name.length()+1);
                sb.append(nsPrefix);
                sb.append(':');
                sb.append(name);
                qName = sb.toString();
            }
            else qName = name;
        }
        else qName = name;

        try {
            handler.startElement(qName, atts);
        }
        catch (org.xml.sax.SAXException sx) {
            throw new MarshalException(sx);
        }


        //-- handle text content
        XMLFieldDescriptor cdesc = classDesc.getContentDescriptor();
        if (cdesc != null) {
            Object obj = null;
            try {
                obj = cdesc.getHandler().getValue(object);
            }
            catch(IllegalStateException ise) {};
            if (obj != null) {
                String str = obj.toString();
                if ((str != null) && (str.length() > 0)) {
                    char[] chars = str.toCharArray();
                    try {
                        handler.characters(chars, 0, chars.length);
                    }
                    catch(org.xml.sax.SAXException sx) {
                        throw new MarshalException(sx);
                    }
                }
            }
        }
        // special case for byte[]
        else if (byteArray) {
            //-- Base64Encoding
            MimeBase64Encoder encoder = new MimeBase64Encoder();
            encoder.translate((byte[])object);
            char[] chars = encoder.getCharArray();
            try {
                handler.characters(chars, 0, chars.length);
            }
            catch(org.xml.sax.SAXException sx) {
                throw new MarshalException(sx);
            }
        }
        /* special case for Strings and primitives */
        else if ((_class == String.class) || isPrimitive(_class)) {
            char[] chars = object.toString().toCharArray();
            try {
                handler.characters(chars,0,chars.length);
            }
            catch(org.xml.sax.SAXException sx) {
                throw new MarshalException(sx);
            }
        }



        //-- handle daughter elements
        descriptors = classDesc.getElementDescriptors();

        ++depth;
        for (int i = 0; i < descriptors.length; i++) {

            XMLFieldDescriptor elemDescriptor = descriptors[i];
            Object obj = null;
            try {
                obj = elemDescriptor.getHandler().getValue(object);
            }
            catch(IllegalStateException ise) {
                continue;
            }
            if (obj == null) continue;

            Class type = obj.getClass();

            //-- handle arrays
            if (type.isArray()) {

                //-- special case for byte[]
                if (type.getComponentType() == Byte.TYPE) {
                    marshal(obj, elemDescriptor, handler);
                }
                else {
                    int length = Array.getLength(obj);
                    for (int j = 0; j < length; j++) {
                        Object item = Array.get(obj, j);
                        if (item != null)
                            marshal(item, elemDescriptor, handler);
                    }
                }
            }
            //-- handle enumerations
            else if (obj instanceof java.util.Enumeration) {
                Enumeration enum = (Enumeration)obj;
                while (enum.hasMoreElements()) {
                    Object item = enum.nextElement();
                    if (item != null)
                        marshal(item, elemDescriptor, handler);
                }
            }
            //-- handle vectors
            else if (obj instanceof java.util.Vector) {
                marshal(obj, elemDescriptor, handler);
                //Vector v = (Vector) obj;
                //int length = v.size();
                //for (int j = 0; j < length; j++) {
                //    marshal(v.elementAt(j), elemDescriptor, handler);
                //}
            }
            else marshal(obj, elemDescriptor, handler);
        }

        //-- finish element
        try {
            handler.endElement(qName);
        }
        catch(org.xml.sax.SAXException sx) {
            throw new MarshalException(sx);
        }

        --depth;
        _parents.pop();
        if (declaredNS) _nsScope.remove(nsURI);
        if (saveType) _nsScope.remove(MarshalHelper.XSI_NAMESPACE);

    } //-- void marshal(DocumentHandler)


    /**
     * Declares the given namespace, if not already in scope
     *
     * @param nsPrefix the namespace prefix
     * @param nsURI the namespace URI to declare
     * @param atts the AttributeListImpl to create the namespace
     * declaration
     * @return true if the namespace was not in scope and was
     *  sucessfully declared, other false
    **/
    private boolean declareNamespace
        (String nsPrefix, String nsURI, AttributeListImpl atts)
    {

        boolean declared = false;

        if (nsURI != null) {

            if (!_nsScope.contains(nsURI)) {
                String attName = XMLNS;

                if (nsPrefix != null) {
                    int len = nsPrefix.length();
                    if (len > 0) {
                        StringBuffer buf = new StringBuffer(6+len);
                        buf.append(XMLNS);
                        buf.append(':');
                        buf.append(nsPrefix);
                        attName = buf.toString();
                    }
                }

                _nsScope.add(nsURI);
                atts.addAttribute(attName, null, nsURI);
                declared = true;
            }
        }
        return declared;
    } //-- declareNamespace

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
     * Finds and returns an XMLClassDescriptor for the given class. If
     * a XMLClassDescriptor could not be found, this method will attempt to
     * create one automatically using reflection.
     * @param _class the Class to get the XMLClassDescriptor for
     * @exception MarshalException when there is a problem
     * retrieving or creating the XMLClassDescriptor for the given class
    **/
    private XMLClassDescriptor getClassDescriptor(Class _class)
        throws MarshalException
    {
        XMLClassDescriptor classDesc = _cdResolver.resolve(_class);
        if (_cdResolver.error()) {
            throw new MarshalException(_cdResolver.getErrorMessage());
        }
        return classDesc;
    } //-- getClassDescriptor

    /**
     * Finds and returns an XMLClassDescriptor for the given class. If
     * a XMLClassDescriptor could not be found, this method will attempt to
     * create one automatically using reflection.
     * @param _class the Class to get the XMLClassDescriptor for
     * @exception MarshalException when there is a problem
     * retrieving or creating the XMLClassDescriptor for the given class
    **/
    private XMLClassDescriptor getClassDescriptor
        (String className, ClassLoader loader)
        throws MarshalException
    {
        XMLClassDescriptor classDesc = _cdResolver.resolve(className, loader);
        if (_cdResolver.error()) {
            throw new MarshalException(_cdResolver.getErrorMessage());
        }
        return classDesc;
    } //-- getClassDescriptor

    private void validate(Object object)
        throws ValidationException
    {
        if  (_validate) {
            //-- we must have a valid element before marshalling
            Validator.validate(object, _cdResolver);
        }
    }

    /**
     * Returns true if the given class should be treated as a primitive
     * type
     * @return true if the given class should be treated as a primitive
     * type
    **/
    private boolean isPrimitive(Class type) {

        if (type.isPrimitive()) return true;
        if ((type == Boolean.class) || (type == Character.class))
            return true;
            
        return (type.getSuperclass() == Number.class);

    } //-- isPrimitive

} //-- Marshaller


