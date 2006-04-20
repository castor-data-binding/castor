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
import org.w3c.dom.*;
import org.apache.xml.serialize.Serializer;
import org.apache.xml.serialize.OutputFormat;
import org.exolab.castor.mapping.ClassDescriptor;
import org.exolab.castor.mapping.Mapping;
import org.exolab.castor.mapping.FieldHandler;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.types.AnyNode;
import org.exolab.castor.xml.descriptors.StringClassDescriptor;
import org.exolab.castor.xml.handlers.DateFieldHandler;
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
public class Marshaller extends MarshalFramework {

    /**
     * Message name for a non sax capable serializer error
    **/
    private static final String SERIALIZER_NOT_SAX_CAPABLE
        = "conf.serializerNotSaxCapable";

    /**
     * The XSI Namespace URI
    **/
    public static final String XSI_NAMESPACE
        = "http://www.w3.org/2001/XMLSchema-instance";

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
     * The CDATA type..uses for SAX attributes
    **/
    private static final String CDATA = "CDATA";

    /**
     * Constants used for the QName handling
     */
    private static final String QNAME_NAME = "QName";

    private static final String DEFAULT_PREFIX = "ns";

    private static int NAMESPACE_COUNTER = 0;

    /**
     * A static flag used to enable debugging when using
     * the static marshal methods.
    **/
    public static boolean enableDebug = false;

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
	 * Insert NameSpace prefix declarations at the root node
	 */
	private boolean _nsPrefixAtRoot = false;

	/**
	 * Name of the root element to use
	 */
	private String _rootElement  = null;

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

     /**
      * The namespace stack
      */
     private Namespaces _namespaces = null;

    private DocumentHandler  _handler      = null;
    private Serializer       _serializer   = null;
    private XMLNaming        _naming       = null;
	private Node             _node         = null;

    /**
     * A boolean to indicate whether or not we are
     * marshalling as a complete document or not.
    **/
    private boolean _asDocument = true;

    /**
     * The depth of the sub tree, 0 denotes document level
    **/
    int depth = 0;

    private List   _packages = null;

    private Stack   _parents  = null;

	private boolean _marshalExtendedType = true;

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
        _handler = handler;

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
            throw new RuntimeException("Unable to obtain serializer");

        _serializer.setOutputCharStream( out );

        _handler = _serializer.asDocumentHandler();
        if ( _handler == null ) {
            String err = Messages.format( this.SERIALIZER_NOT_SAX_CAPABLE,
                                          _serializer.getClass().getName() );
            throw new RuntimeException( err );
        }

    } //-- Marshaller

	/**
	 * Creates a new Marshaller
	 */
	public Marshaller( Node node )
	{
        if ( node == null )
            throw new IllegalArgumentException( "Argument 'node' is null." );
		_node = node;
		_handler = new SAX2DOMHandler( node );

        // call internal initializer
        initialize();
	} //-- Marshaller

    /**
     * Initializes this Marshaller. This is common code shared among
     * the Constructors
    **/
    private void initialize() {
        _debug           = enableDebug;
        _namespaces      = new Namespaces();
        _nsScope         = new List(3);
        _packages        = new List(3);
        _cdResolver      = new ClassDescriptorResolverImpl();
        _parents         = new Stack();
        _validate        = Configuration.marshallingValidation();
        _naming          = XMLNaming.getInstance();

        setNamespaceMapping( XSI_PREFIX, XSI_NAMESPACE );
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

        _asDocument = asDocument;

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

        _namespaces.addNamespace(nsPrefix, nsURI);

    } //-- setNamespacePrefix

	/**
	 * Sets the name of the root element to use
	 * @param The name of the root element to use
	 */
	public void setRootElement(String rootElement)
	{
		_rootElement = rootElement;
	} //-- setRootElement

	/**
	 * Returns the name of the root element to use
	 * @return Returns the name of the root element to use
	 */
	public String getRootElement()
	{
		return _rootElement;
	} //-- getRootElement

	/**
	 * Set to True to declare the given namespace mappings at the root node. Default is False.
	 * @param nsPrefixAtRoot
	 */
	public void setNSPrefixAtRoot(boolean nsPrefixAtRoot)
	{
		_nsPrefixAtRoot = nsPrefixAtRoot;
	}

	/**
	 * Returns True if the given namespace mappings will be declared at the root node.
	 * @return Returns True if the given namespace mappings will be declared at the root node.
	 */
	public boolean getNSPrefixAtRoot()
	{
		return _nsPrefixAtRoot;
	}

    /**
     * Sets the ClassDescriptorResolver to use during unmarshalling
     * @param cdr the ClassDescriptorResolver to use
     * @see #setMapping
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
	 * If True the marshaller will use the 'xsi:type' attribute
	 * to marshall a field value that extended the defined field type.
	 * Default is True.
	 */
	public void setMarshalExtendedType(boolean marshalExtendedType)
	{
		_marshalExtendedType = marshalExtendedType;
	} //-- setMarshalExtendedType

	/**
	 * If True the marshaller will use the 'xsi:type' attribute
	 * to marshall a field value that extended the defined field type.
	 * Default is True.
	 * @return If True the marshaller will use the 'xsi:type' attribute
	 * to marshall a field value that extended the defined field type.
	 * Default is True.
	 */
	public boolean getMarshalExtendedType()
	{
		return _marshalExtendedType;
	} //-- setMarshallExtendedType

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
        if (object == null)
            throw new MarshalException("object must not be null");

        if (enableDebug) {
            System.out.print("- Marshaller called using ");
            System.out.println("*static* marshal(Object, Writer)");
        }
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
        if (object == null)
            throw new MarshalException("object must not be null");

        if (enableDebug) {
            System.out.print("- Marshaller called using ");
            System.out.println("*static* marshal(Object, DocumentHandler)");
        }
        Marshaller marshaller;
        marshaller = new Marshaller(handler);
        marshaller.marshal(object);
    } //-- marshal

    /**
     * Marshals the given Object as XML using the given DOM Node
     * to send events to.
     * @param obj the Object to marshal
     * @param node the DOM Node to marshal to
     * @exception org.exolab.castor.xml.MarshalException
     * @exception org.exolab.castor.xml.ValidationException
    **/
    public static void marshal(Object object, Node node)
        throws MarshalException, ValidationException
    {
        if (object == null)
            throw new MarshalException("object must not be null");

        if (enableDebug) {
            System.out.print("- Marshaller called using ");
            System.out.println("*static* marshal(Object, Node)");
        }
        Marshaller marshaller;
        marshaller = new Marshaller(node);
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
        if (object == null)
            throw new MarshalException("object must not be null");

        if (_debug) {
            System.out.println("Marshalling " + object.getClass().getName());
        }
        if (object instanceof AnyNode) {
           try{
              AnyNode2SAX.fireEvents((AnyNode)object, _handler);
           } catch(SAXException e) {
                throw new MarshalException(e);
           }
        }
        else {
             validate(object);
             if (_asDocument) {
                try {
                    _handler.startDocument();
                    marshal(object, null, _handler);
                    _handler.endDocument();
                } catch (SAXException sx) {
                    throw new MarshalException(sx);
                }
             }
             else marshal(object, null, _handler);
        }

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
            throw new MarshalException(err);
        }

        if (descriptor != null && descriptor.isTransient())
            return;

        if (object instanceof AnyNode) {
           try {
               AnyNode2SAX.fireEvents((AnyNode) object, handler);
           }catch (SAXException e) {
               throw new MarshalException(e);
           }
           return;
        }

        boolean containerField = false;

        if (descriptor != null && descriptor.isContainer())
            containerField = true;

        //-- add object to stack so we don't potentially get into
        //-- an endlessloop
        if (_parents.search(object) >= 0) return;
        _parents.push(object);

        Class _class = object.getClass();

        boolean byteArray = false;
        if (_class.isArray())
            byteArray = (_class.getComponentType() == Byte.TYPE);

	    boolean atRoot = false;
        if (descriptor == null) {
            descriptor = new XMLFieldDescriptorImpl(_class, "root", null, null);
			atRoot = true;
        }

        //-- calculate Object's name
        String name = descriptor.getXMLName();
		if (atRoot && _rootElement!=null)
			name = _rootElement;
        if (name == null) {
            name = _class.getName();
            //-- remove package information from name
            int idx = name.lastIndexOf('.');
            if (idx >= 0) {
                name = name.substring(idx+1);
            }
            //-- remove capitalization
            name = _naming.toXMLName(name);
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
            if (isPrimitive(_class) || byteArray) {
                classDesc = _StringClassDescriptor;
                //-- check to see if we need to save the xsi:type
                //-- for this class
                if (_class != descriptor.getFieldType()) {
                    saveType = (!descriptor.getFieldType().isPrimitive());
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
                        _packages.add(pkgName);
            }

    		if (_marshalExtendedType) {
				//  marshall as the actual value
				classDesc = getClassDescriptor(_class);
				saveType = (_class != descriptor.getFieldType());
			} else {
			    // marshall as the base field type
				_class = descriptor.getFieldType();
				classDesc = getClassDescriptor(_class);
            }

			if (atRoot && _rootElement!=null)
				name = _rootElement;
			else if (descriptor.getXMLName()==null)
                  name = classDesc.getXMLName();
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

        //-- Suppress 'xsi:type' attributes when Castor is able to infer the
        //-- information from the mapping file
        //-- XXXX Date fix
        if (saveType && (descriptor.getHandler() instanceof DateFieldHandler))
            saveType = false;
        //-- XXXX end Date fix

        if (saveType) {
            // When the type of the instance of the field is not the
            // type specified for the field, it might be necessary to
            // store the type explicitly (using xsi:type) to avoid
            // confusion during unmarshalling.
            //
            // However, it might be possible to use the XMLName of the
            // instance rather than the XMLName of the field. If
            // Castor could find back the type from the name of the
            // element, there is no need to add an xsi:type.
            //
            // In order to do that, there is two conditions:
            // 1. Castor should be able to find the right class to
            // instantiate form the XMLName of the instance.
            // 2. Castor should be sure than when using the XMLName of
            // the instance, there is only one field wich will match
            // that name (and that it is the current field)

            // XML Name associated with the class we are marshalling
            String xmlElementName = classDesc.getXMLName();

            // We try to find if there is a XMLClassDescriptor associated
            // with the XML name of this class
            XMLClassDescriptor xmlElementNameClassDesc = _cdResolver.resolveByXMLName(xmlElementName, null, null);

            // Try to find a field descriptor directly in the parent object
            XMLFieldDescriptor fieldDescMatch = ((XMLClassDescriptor)descriptor.getContainingClassDescriptor()).getFieldDescriptor(xmlElementName, NodeType.Element);
            
            // Try to find a field descriptor by inheritance in the parent object
            Vector inheritancesList = UnmarshalHandler.searchInheritance(xmlElementName, null, (XMLClassDescriptor)descriptor.getContainingClassDescriptor(), _cdResolver);

            boolean foundTheRightClass = ((xmlElementNameClassDesc != null) && (_class == xmlElementNameClassDesc.getJavaClass()));

            boolean oneAndOnlyOneMatchedField = ((fieldDescMatch != null) || 
                                                 ((inheritancesList.size() == 1) && 
                                                  (((UnmarshalHandler.InheritanceMatch)inheritancesList.get(0))._parentFieldDesc == descriptor)));

            // Can we remove the xsi:type ?
            if (foundTheRightClass && oneAndOnlyOneMatchedField) {
                saveType = false;
                name     = xmlElementName;
            }
        }//--- End of "if (saveType)"

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

            //-- handle IDREF(S)
            if (attDescriptor.isReference() && (value != null)) {

                if (attDescriptor.isMultivalued()) {
                    Object[] objects = (Object[])value;
                    if (objects.length == 0) continue;
                    StringBuffer sb = new StringBuffer();
                    for (int v = 0; v < objects.length; v++) {
                        if (v > 0) sb.append(' ');
                        sb.append(getObjectID(objects[v]).toString());
                    }
                    value = sb;
                }
                else {
                    value = getObjectID(value);
                }
            }

            if (value == null) continue;

            //check if the value is a QName that needs to
            //be resolved ({URI}value -> ns:value).
            String valueType = attDescriptor.getSchemaType();
            if ((valueType != null) && (valueType.equals(QNAME_NAME)))
                 value = resolveQName(value, attDescriptor, atts);

            atts.addAttribute(xmlName, CDATA, value.toString());
        }


        //-- Look for attributes in container fields,
        //-- (also handle container in container)
        /* REMOVED For now (KV)
        processContainerAttributes(object, classDesc, atts);
        */


        //-- xsi:type
        if (saveType) {
            saveType = declareNamespace(XSI_PREFIX, XSI_NAMESPACE, atts);
            atts.addAttribute(XSI_TYPE, CDATA, "java:"+_class.getName());
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
            nsURI = _namespaces.getNamespaceURI(nsPrefix);
        }
        else if ((nsPrefix == null) && (nsURI != null)) {
            nsPrefix = (String) _namespaces.getNamespacePrefix(nsURI);
        }

        boolean declaredNS = false;
		//-- declare namespace at this element scope?
		if (nsURI != null)
			//-- only if prefix not already been declared at root (via setNSPrefixAtRoot method)
			if (!(_nsPrefixAtRoot && nsPrefix!=null))
				declaredNS = declareNamespace(nsPrefix, nsURI, atts);

		//-- declare all namespace prefix at root (via setNSPrefixAtRoot method)?
		if (_nsPrefixAtRoot && atRoot)
		{
			//-- insert all namespace declarations at the root level
			_namespaces.declareAsAttributes(atts);
		}

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

       //check if the value is a QName that needs to
       //be resolved ({URI}value -> ns:value)
       String valueType = descriptor.getSchemaType();
       if ((valueType != null) && (valueType.equals(QNAME_NAME)))
           object = resolveQName(object, descriptor, atts);

        try {
            if (!containerField)
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
        else if (isPrimitive(_class)) {

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

            else marshal(obj, elemDescriptor, handler);
        }
        //-- finish element
        try {
            if (!containerField)
                handler.endElement(qName);
        }
        catch(org.xml.sax.SAXException sx) {
            throw new MarshalException(sx);
        }

        --depth;
        _parents.pop();
        if (declaredNS) _nsScope.remove(nsURI);
        if (saveType) _nsScope.remove(XSI_NAMESPACE);

    } //-- void marshal(DocumentHandler)

    /**
     * Retrieves the ID for the given Object
     *
     * @param object the Object to retrieve the ID for
     * @return the ID for the given Object
    **/
    private Object getObjectID(Object object)
        throws MarshalException
    {
        if (object == null) return null;

        Object id = null;
        XMLClassDescriptor cd = getClassDescriptor(object.getClass());
        String err = null;
        if (cd != null) {
            XMLFieldDescriptor fieldDesc
                = (XMLFieldDescriptor) cd.getIdentity();
            if (fieldDesc != null) {
                FieldHandler fieldHandler = fieldDesc.getHandler();
                if (fieldHandler != null) {
                    try {
                        id = fieldHandler.getValue(object);
                    }
                    catch(IllegalStateException ise) {
                        err = ise.toString();
                    }
                }//fieldHandler != null
                else {
                    err = "FieldHandler for Identity descriptor is null.";
                }
            }//fieldDesc != null
            else err = "No identity descriptor available";
        }//cd!=null
        else  {
            err = "Unable to resolve ClassDescriptor.";
        }
        if (err != null) {
            String errMsg = "Unable to resolve ID for instance of class '";
            errMsg += object.getClass().getName();
            errMsg += "' due to the following error: ";
            throw new MarshalException(errMsg + err);
        }
        return id;
    } //-- getID


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

        if ( (nsURI != null) && (nsURI.length() != 0)) {

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
                atts.addAttribute(attName, CDATA, nsURI);
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
        XMLClassDescriptor classDesc = null;
        if (!isPrimitive(_class))
            classDesc = _cdResolver.resolve(_class);

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


    private void processContainerAttributes
        (Object target, XMLClassDescriptor classDesc, AttributeListImpl atts)
        throws MarshalException
    {


        XMLFieldDescriptor[] elemDescriptors = classDesc.getElementDescriptors();

        for (int i = 0; i < elemDescriptors.length; i++) {
            if (elemDescriptors[i] == null) continue;
            if (!elemDescriptors[i].isContainer()) continue;

            XMLFieldDescriptor elemDescriptor  = elemDescriptors[i];

            Object containerObject = elemDescriptor.getHandler().getValue(target);

            if (containerObject == null) continue;

            XMLClassDescriptor containerClassDesc = (XMLClassDescriptor)elemDescriptor.getClassDescriptor();
            if (containerClassDesc == null) {
                containerClassDesc = getClassDescriptor(elemDescriptor.getFieldType());
                if (containerClassDesc == null) continue;
            }

            // Look for attributes
            XMLFieldDescriptor[] attrDescriptors = containerClassDesc.getAttributeDescriptors();
            for (int idx = 0; idx < attrDescriptors.length; idx++) {
                if (attrDescriptors[idx] == null) continue;

                String xmlName = attrDescriptors[idx].getXMLName();
                Object value = null;
                try {
                    value = attrDescriptors[idx].getHandler().getValue(containerObject);
                }
                catch(IllegalStateException ise) {
                    continue;
                }
                if (value == null) continue;
                atts.addAttribute(xmlName, CDATA, value.toString());
            }


            // recursively process containers
            processContainerAttributes(containerObject, containerClassDesc, atts);
        }

    } //-- processContainerAttributes

    /**
     * Resolve a QName value ({URI}value) by declaring a namespace after
     * having retrieved the prefix.
     */
    private Object resolveQName(Object value, XMLFieldDescriptor fieldDesc, AttributeListImpl atts) {
        if ( (value == null) || !(value instanceof String))
            return value;
        if (!(fieldDesc instanceof XMLFieldDescriptorImpl))
           return value;
        String result = (String)value;
        if (result.indexOf('}') <= 0) {
             String err = "Bad QName value :'"+result+"', it should follow the pattern '{URI}value'";
             throw new IllegalArgumentException(err);
        }
        int idx = result.indexOf('}');
        if (idx <= 0){
             String err = "Bad QName value :'"+result+"', it should follow the pattern '{URI}value'";
             throw new IllegalArgumentException(err);
        }
        String nsURI = result.substring(1, idx);
        String prefix = ((XMLFieldDescriptorImpl)fieldDesc).getQNamePrefix();
        //no prefix provided, check if one has been previously defined
        if (prefix == null)
            prefix = _namespaces.getNamespacePrefix(nsURI);
        //if still no prefix, use a naming algorithm (ns+counter).
        if (prefix == null)
            prefix = DEFAULT_PREFIX+(++NAMESPACE_COUNTER);
        result = (prefix.length() != 0)?prefix+":"+result.substring(idx+1):result.substring(idx+1);
        declareNamespace(prefix, nsURI, atts);
        return result;
    }

    private void validate(Object object)
        throws ValidationException
    {
        if  (_validate) {
            //-- we must have a valid element before marshalling
            Validator validator = new Validator();
            validator.validate(object, _cdResolver);
        }
    }

} //-- Marshaller


