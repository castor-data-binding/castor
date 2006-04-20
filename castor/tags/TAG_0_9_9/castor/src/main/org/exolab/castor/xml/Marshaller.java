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

package org.exolab.castor.xml;


//-- castor imports
import org.castor.util.Base64Encoder;
import org.exolab.castor.mapping.CollectionHandler;
import org.exolab.castor.mapping.MapItem;
import org.exolab.castor.mapping.Mapping;
import org.exolab.castor.mapping.FieldHandler;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.mapping.MapHandler;
import org.exolab.castor.mapping.handlers.MapHandlers;
import org.exolab.castor.mapping.loader.CollectionHandlers;

import org.exolab.castor.types.AnyNode;
import org.exolab.castor.xml.descriptors.RootArrayDescriptor;
import org.exolab.castor.xml.descriptors.StringClassDescriptor;
import org.exolab.castor.xml.handlers.DateFieldHandler;
import org.exolab.castor.xml.handlers.EnumFieldHandler;
import org.exolab.castor.xml.util.*;
import org.exolab.castor.util.Configuration;
import org.exolab.castor.util.LocalConfiguration;
import org.exolab.castor.util.List;
import org.exolab.castor.util.Messages;
import org.exolab.castor.util.Stack;

//-- misc xml related imports
import org.xml.sax.*;
import org.xml.sax.helpers.AttributesImpl;

import org.w3c.dom.Node;
import org.apache.xml.serialize.Serializer;
import org.apache.xml.serialize.OutputFormat;

import java.io.IOException;
import java.io.PrintWriter;
//import java.io.Serializable;
import java.io.Writer;
import java.lang.reflect.Array;
import java.util.Enumeration;


/**
 * A Marshaller that serializes Java Object's to XML
 *
 * Note: This class is not thread safe, and not intended to be, 
 * so please create a new Marshaller for each thread if it
 * is to be used in a multithreaded environment.
 *
 * @author <a href="mailto:keith AT kvisco DOT com">Keith Visco</a>
 * @version $Revision$ $Date$
 */
public class Marshaller extends MarshalFramework {


    //---------------------------/
    //- Private Class variables -/
    //---------------------------/

    /**
     * The CDATA type..uses for SAX attributes
    **/
    private static final String CDATA = "CDATA";

    /**
     * Default prefix for use when creating
     * namespace prefixes.
    **/
    private static final String DEFAULT_PREFIX = "ns";


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
     * Namespace prefix counter
    **/
    private int NAMESPACE_COUNTER = 0;

    /**
     * An instance of StringClassDescriptor
    **/
    private static final StringClassDescriptor _StringClassDescriptor
        = new StringClassDescriptor();

    
    //---------------------------/
    //- Public member variables -/
    //---------------------------/

    /**
     * A static flag used to enable debugging when using
     * the static marshal methods.
    **/
    public static boolean enableDebug = false;

    //----------------------------/
    //- Private member variables -/
    //----------------------------/

    /**
     * A boolean to indicate whether or not we are
     * marshalling as a complete document or not.
    **/
    private boolean _asDocument = true;

    /**
     * The ClassDescriptorResolver used for resolving XMLClassDescriptors
    **/
    private ClassDescriptorResolver _cdResolver = null;

    /**
     * A flag indicating whether or not to generate
     * debug information
    **/
    private boolean _debug = false;

    /**
     * The depth of the sub tree, 0 denotes document level
    **/
    int depth = 0;

    /**
     * The output format to use with the serializer.
     * This will be null if the user passed in their
     * own DocumentHandler.
    **/
    private OutputFormat _format = null;

    /**
     * The ContentHandler we are marshalling to
    **/
    private ContentHandler  _handler      = null;

    /**
     * The print writer used for logging
    **/
    private PrintWriter _logWriter = null;

    /**
     * Castor configuration
     */
    private Configuration _config = null;
    
    /**
     * flag to indicate whether or not to use xsi:type
    **/
	private boolean _marshalExtendedType = true;

    /**
     * The registered MarshalListener to receive
     * notifications of pre and post marshal for
     * each object in the tree being marshalled.
    **/
    private MarshalListener _marshalListener = null;

    /**
     * The namespace stack
    **/
    private Namespaces _namespaces = null;

    /**
     * The XMLNaming instance being used.
    **/
    private XMLNaming _naming = null;

    /**
     * A handle to the DOM node being marshalled. This
     * will be null a DOM node was not passed in
     * as an argument to a marshal method.
    **/
	private Node _node = null;

	/**
	 * Insert NameSpace prefix declarations at the root node
	 */
	//private boolean _nsPrefixAtRoot = false;

    /**
     * current java packages being used during marshalling
    **/
    private List   _packages = null;

    /**
     * A stack of parent objects...to prevent circular
     * references from being marshalled.
    **/
    private Stack   _parents  = null;

    /**
     * A list of ProcessingInstructions to output
     * upon marshalling of the document
    **/
    private List _processingInstructions = null;

	/**
	 * Name of the root element to use
	 */
	private String _rootElement  = null;

    /**
     * A boolean to indicate keys from a map
     * should be saved when necessary
     */
    private boolean _saveMapKeys = true;
    
    /**
     * The serializer that is being used for marshalling.
     * This may be null if the user passed in a DocumentHandler.
    **/
    private Serializer       _serializer   = null;

    /**
     * A flag to allow suppressing namespaces
     */
    private boolean _suppressNamespaces = false;
    
    /**
     * A flag to allow suppressing the xsi:type attribute
     */
    private boolean _suppressXSIType = false;
    
    private boolean _useXSITypeAtRoot = false;
    
    /**
     * The set of optional top-level attributes
     * set by the user.
    **/
    private AttributeSetImpl _topLevelAtts = null;

    /**
     * The AttributeList which is to be used during marshalling,
     * instead of creating a bunch of new ones. 
     */
    private AttributesImpl _attributes = null;
    
    /**
     * The validation flag
    **/
    private boolean _validate = false;

    
    /**
     * Creates a new Marshaller with the given DocumentHandler.
     *
     * @param handler the DocumentHandler to "marshal" to.
    **/
    public Marshaller( DocumentHandler handler ) {
        if ( handler == null )
            throw new IllegalArgumentException( "Argument 'handler' is null." );
        
        _handler = new DocumentHandlerAdapter(handler);

        // call internal initializer
        initialize();
    } //-- Marshaller


    /**
     * Creates a new Marshaller with the given SAX ContentHandler.
     *
     * @param handler the ContentHandler to "marshal" to.
    **/
    public Marshaller( ContentHandler handler )
        throws IOException
    {
        if ( handler == null )
            throw new IllegalArgumentException( "Argument 'handler' is null." );

        _handler = handler;

        // call internal initializer
        initialize();
    } //-- Marshaller

    /**
     * Creates a new Marshaller with the given writer.
     *
     * @param out the Writer to serialize to
    **/
    public Marshaller( Writer out )
        throws IOException
    {
        if (out == null)
            throw new IllegalArgumentException( "Argument 'out' is null.");

        // call internal initializer
        initialize();

        _serializer = _config.getSerializer();

        if (_serializer == null)
            throw new RuntimeException("Unable to obtain serializer");

        _serializer.setOutputCharStream( out );

        //-- Due to a Xerces Serializer bug that doesn't allow declaring
        //-- multiple prefixes to the same namespace, we use the old
        //-- DocumentHandler format and process namespaces ourselves        
        _handler = new DocumentHandlerAdapter(_serializer.asDocumentHandler());
        
        if ( _handler == null ) {
            String err = Messages.format( SERIALIZER_NOT_SAX_CAPABLE,
                                          _serializer.getClass().getName() );
            throw new RuntimeException( err );
        }

    } //-- Marshaller

	/**
	 * Creates a new Marshaller for the given DOM Node.
	 *
	 * @param node the DOM node to marshal into.
	**/
	public Marshaller( Node node )
	{
        if ( node == null )
            throw new IllegalArgumentException( "Argument 'node' is null." );
		_node = node;
		_handler = new DocumentHandlerAdapter(new SAX2DOMHandler( node ));

        // call internal initializer
        initialize();
	} //-- Marshaller

    /**
     * Initializes this Marshaller. This is common code shared among
     * the Constructors
    **/
    private void initialize() {
        _config          = LocalConfiguration.getInstance();
        _debug           = enableDebug;
        _namespaces      = new Namespaces();
        _packages        = new List(3);
        _cdResolver      = new ClassDescriptorResolverImpl();
        _parents         = new Stack();
        _validate        = _config.marshallingValidation();
        _naming          = XMLNaming.getInstance();
        _processingInstructions = new List(3);
        _attributes      = new AttributesImpl();
        _topLevelAtts    = new AttributeSetImpl();
        
        //-- saveMapKeys
        String val = _config.getProperty(Configuration.Property.SaveMapKeys, "true");
        if ("false".equalsIgnoreCase(val) || "off".equalsIgnoreCase(val)) {
            _saveMapKeys = false;
        }
        
    } //-- initialize();

    /**
     * Adds the given processing instruction data to the set of
     * processing instructions to output during marshalling.
     *
     * @param target the processing instruction target
     * @param data the processing instruction data
    **/
    public void addProcessingInstruction(String target, String data) {

        if ((target == null) || (target.length() == 0)) {
            String err = "the argument 'target' must not be null or empty.";
            throw new IllegalArgumentException(err);
        }

        if (data == null) {
            String err = "the argument 'data' must not be null.";
            throw new IllegalArgumentException(err);
        }
        _processingInstructions.add(new ProcessingInstruction(target, data));
    } //-- addProcessingInstruction

   /**
    * Sets the document type definition for the serializer. Note that this method
    * cannot be called if you've passed in your own DocumentHandler.
    *
    * @param publicId the public identifier
    * @param systemId the system identifier
    */
    public void setDoctype(String publicId, String systemId) {
     
        if (_serializer != null) {
            if (_format == null) {
                _format = _config.getOutputFormat();
            }
            _format.setDoctype(publicId, systemId);
            //-- reset output format, this needs to be done
            //-- any time a change occurs to the format.
            _serializer.setOutputFormat( _format );
            try {
                //-- Due to a Xerces Serializer bug that doesn't allow declaring
                //-- multiple prefixes to the same namespace, we use the old
                //-- DocumentHandler format and process namespaces ourselves        
                _handler = new DocumentHandlerAdapter(_serializer.asDocumentHandler());
            }
            catch (java.io.IOException iox) {
                //-- we can ignore this exception since it shouldn't
                //-- happen. If _serializer is not null, it means
                //-- we've already called this method sucessfully
                //-- in the Marshaller() constructor
            }
        }
        else {
            String error = "doctype cannot be set if you've passed in "+
            "your own DocumentHandler";
            throw new IllegalStateException(error);
        }
    } //-- setDoctype

    
    /**
     * Sets whether or not to marshal as a document which includes
     * the XML declaration, and if necessary the DOCTYPE declaration.
     * By default the Marshaller will marshal as a well formed
     * XML document including the XML Declaration.
     * 
     * If the given boolean is true, the Marshaller will marshal 
     * as a well formed XML fragment (no XML declaration or DOCTYPE).
     * 
     * This method is basically the same as calling 
     * #setMarshalAsDocument(false);
     * 
     * @param supressXMLDeclaration a boolean that when true
     * includes that generated XML should not contain 
     * the XML declaration.
     * @see #setMarshalAsDocument
     */
    public void setSupressXMLDeclaration(boolean supressXMLDeclaration) {
        setMarshalAsDocument(!supressXMLDeclaration);
    } //-- setSupressXMLDeclaration
    
    /**
     * Sets whether or not to marshal as a document which includes
     * the XML declaration, and if necessary the DOCTYPE declaration.
     * By default the Marshaller will marshal as a well formed
     * XML document including the XML Declaration.
     * 
     * If the given boolean is false, the Marshaller will marshal 
     * as a well formed XML fragment (no XML declaration or DOCTYPE).
     *
     * This method is basically the same as calling 
     * #setSupressXMLDeclaration(true);
     * 
     * @param asDocument a boolean, when true, indicating to marshal
     * as a complete XML document.
     * @see #setSupressXMLDeclaration
     */
    public void setMarshalAsDocument(boolean asDocument) {

        _asDocument = asDocument;

        if (_serializer != null) {

            if (_format == null) {
                _format = _config.getOutputFormat();
            }
            _format.setOmitXMLDeclaration( ! asDocument );
            _format.setOmitDocumentType( ! asDocument );

            //-- reset output format, this needs to be done
            //-- any time a change occurs to the format.
            _serializer.setOutputFormat( _format );
            try {
                //-- Due to a Xerces Serializer bug that doesn't allow declaring
                //-- multiple prefixes to the same namespace, we use the old
                //-- DocumentHandler format and process namespaces ourselves        
                _handler = new DocumentHandlerAdapter(_serializer.asDocumentHandler());
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
     * Sets an optional MarshalListener to recieve pre and post
     * marshal notification for each Object in the tree.
     * MarshalListener is only for complex objects that map
     * into elements, simpleTypes and types that map into
     * attributes do not cause any pre and post event notifications.
     * Current only one (1) listener is allowed. If you need
     * register multiple listeners, you will have to create
     * your own master listener that will forward the
     * event notifications and manage the multiple
     * listeners.
     *
     * @param listener the MarshalListener to set.
    **/
    public void setMarshalListener(MarshalListener listener) {
        _marshalListener = listener;
    } //-- setMarshalListener

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
	 * @deprecated
	 */
	public void setNSPrefixAtRoot(boolean nsPrefixAtRoot)
	{
	    // leaving for now...backward compatability
		//_nsPrefixAtRoot = nsPrefixAtRoot;
	}

	/**
	 * Returns True if the given namespace mappings will be declared at the root node.
	 * @return Returns True if the given namespace mappings will be declared at the root node.
	 * @deprecated
	 */
	public boolean getNSPrefixAtRoot()
	{
		return true;
	}
    
    /**
     * Returns the ClassDescriptorResolver for use during marshalling
     * 
     * @return the ClassDescriptorResolver 
     * @see #setResolver
     */
    public ClassDescriptorResolver getResolver() {

        if (_cdResolver == null) {
            _cdResolver = new ClassDescriptorResolverImpl();
        }
        return _cdResolver;

    } //-- getResolver

    /**
     * Sets the ClassDescriptorResolver to use during marshalling
     * 
     * <BR />
     * <B>Note:</B> This method will nullify any Mapping
     * currently being used by this Marshaller
     * 
     * @param cdr the ClassDescriptorResolver to use
     * @see #setMapping
     * @see #getResolver
     */
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
     * Marshals the given Object as XML using the given ContentHandler
     * to send events to.
     * @param obj the Object to marshal
     * @param handler the ContentHandler to marshal to
     * @exception org.exolab.castor.xml.MarshalException
     * @exception org.exolab.castor.xml.ValidationException
    **/
    public static void marshal(Object object, ContentHandler handler)
        throws MarshalException, ValidationException, IOException
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
              AnyNode2SAX2.fireEvents((AnyNode)object, _handler, _namespaces);
           } catch(SAXException e) {
                throw new MarshalException(e);
           }
        }
        else {
             validate(object);
             MarshalState mstate = new MarshalState(object, "root");
             if (_asDocument) {
                try {
                    _handler.startDocument();
                    //-- handle processing instructions
                    for (int i = 0; i < _processingInstructions.size(); i++) {
                        ProcessingInstruction pi = (ProcessingInstruction)
                            _processingInstructions.get(i);
                        _handler.processingInstruction(pi.getTarget(),
                            pi.getData());
                    }
                    marshal(object, null, _handler, mstate);
                    _handler.endDocument();
                } catch (SAXException sx) {
                    throw new MarshalException(sx);
                }
             }
             else {
                marshal(object, null, _handler, mstate);
             }
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
         ContentHandler handler,
         final MarshalState mstate)
        throws MarshalException, ValidationException
    {
        
        
        if (object == null) {
            String err = "Marshaller#marshal: null parameter: 'object'";
            throw new MarshalException(err);
        }

        if (descriptor != null && descriptor.isTransient())
            return;

        //-- notify listener
        if (_marshalListener != null) {
            if (!_marshalListener.preMarshal(object))
                return;
        }
        
        //-- handle AnyNode
        if (object instanceof AnyNode) {
           try {
               AnyNode2SAX2.fireEvents((AnyNode) object, handler, _namespaces);
           }catch (SAXException e) {
               throw new MarshalException(e);
           }
           return;
        }

        boolean containerField = false;

        if (descriptor != null && descriptor.isContainer()) {
            containerField = true;
        }

        //-- add object to stack so we don't potentially get into
        //-- an endlessloop
        if (_parents.search(object) >= 0) return;
        _parents.push(object);

        final boolean isNil = (object instanceof NilObject);
        
        Class _class = null;
        
        if (!isNil) {
            _class = object.getClass();
        }
        else {
        	_class = ((NilObject)object).getClassDescriptor().getJavaClass();
        }
        
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
			
	    boolean autoNameByClass = false;
        if (name == null) {
            autoNameByClass = true;
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
        
        if (object instanceof NilObject) {
            classDesc = ((NilObject)object).getClassDescriptor();
        }
        else if (_class == descriptor.getFieldType()) {
            classDesc = (XMLClassDescriptor)descriptor.getClassDescriptor();
        }

        if (classDesc == null) {
            //-- check for primitive or String, we need to use
            //-- the special #isPrimitive method of this class
            //-- so that we can check for the primitive wrapper
            //-- classes
            if (isPrimitive(_class) || byteArray) {
                classDesc = _StringClassDescriptor;
                //-- check to see if we need to save the xsi:type
                //-- for this class
                Class fieldType = descriptor.getFieldType();
                if (_class != fieldType) {
                    while (fieldType.isArray()) {
                        fieldType = fieldType.getComponentType();
                    }
                    saveType = (!primitiveOrWrapperEquals(_class, fieldType));
                }
            }
            else {
                saveType = _class.isArray();
                //-- save package information for use when searching
                //-- for MarshalInfo classes
                String className = _class.getName();
                int idx = className.lastIndexOf(".");
                String pkgName = null;
                if (idx > 0) {
                    pkgName = className.substring(0,idx+1);
                    if (!_packages.contains(pkgName))
                        _packages.add(pkgName);
                }

                if (_marshalExtendedType) {
                    //-- Check to see if we can determine the class or 
                    //-- ClassDescriptor from the type specified in the 
                    //-- FieldHandler or from the current CDR state
                    
                    if ((_class != descriptor.getFieldType()) || atRoot) {
                        
                        saveType = true;
                        
                        boolean containsDesc = false;
                        
                        //-- if we're not at the root, check to see if we can resolve name.
                        //-- if we're at the root, the name will most likely be resolvable
                        //-- due to the validation step, so in most cases, if we are not
                        //-- using a mapping we need the xsi:type at the root
                        if (!atRoot) {
                            String nsURI = descriptor.getNameSpaceURI();
                            XMLClassDescriptor tmpDesc = null;
                            try {
                                tmpDesc = _cdResolver.resolveByXMLName(name, nsURI, null);
                            }
                            catch(ResolverException rx) {
                                //-- exception not important as we're simply
                                //-- testing to see if we can resolve during
                                //-- unmarshalling
                            }
                            
                            if (tmpDesc != null) {
                            	Class tmpType = tmpDesc.getJavaClass();
                                if (tmpType == _class) {
                                	containsDesc = (!tmpType.isInterface());
                                }
                            }
                        }
                        
                        if (!containsDesc) {
                            //-- check for class mapping, we don't use the
                            //-- resolver directly because it will try to
                            //-- load a compiled descriptor, or introspect
                            //-- one
                            if (atRoot) {
                                if (_useXSITypeAtRoot) {
                                	XMLMappingLoader ml = _cdResolver.getMappingLoader();
                                	if (ml != null) {
                                		containsDesc = (ml.getDescriptor(_class) != null);
                                    }
                                }
                                else {
                                    //-- prevent xsi:type from appearing
                                    //-- on root
                                    containsDesc = true;
                                }
                                
                            }
                            
                            //-- The following logic needs to be expanded to use
                            //-- namespace -to- package mappings
                            if ((!containsDesc) && (pkgName == null)) {
                                //-- check to see if the class name is guessable
                                //-- from the xml name
                                classDesc = getClassDescriptor(_class);                                
                                if (classDesc != null) {
                                    String tmpName1 = classDesc.getXMLName();
                                    String tmpName2 = _naming.toXMLName(_class.getName());
                                    if (tmpName2.equals(tmpName1))
                                        saveType = false;
                                }
                            }
                        }
                        
                        if (containsDesc) saveType = false;
                        
                    }
                    
                    //  marshal as the actual type
                    if (classDesc == null)
                        classDesc = getClassDescriptor(_class);
                    
			    } //-- end if (marshalExtendedType)
			    else {
			        // marshall as the base field type
				    _class = descriptor.getFieldType();
				    classDesc = getClassDescriptor(_class);
                } 

                //-- If we are marshalling an array as the top
                //-- level object, or if we run into a multi
                //-- dimensional array, use the special 
                //-- ArrayDescriptor
                if ((classDesc == null) && _class.isArray()) {
                    classDesc = new RootArrayDescriptor(_class);
                    if (atRoot) {
                        containerField = (!_asDocument);
                    }
                }
            } //-- end else not primitive

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
        
        //-- handle auto-naming by class
        if (autoNameByClass) {
            if (classDesc.getXMLName() != null) {
                name = classDesc.getXMLName();
            }
        }
        
        //-- at this point naming should be done, update
        //-- MarshalState.xmlName if root element
        if (atRoot) {
            mstate._xmlName = name;
        }
        
        //------------------------------------------------/
        //- Next few sections of code deal with xsi:type -/
        //- prevention, if necessary                     -/
        //------------------------------------------------/

        //-- Allow user to prevent xsi:type
        saveType = (saveType && (!_suppressXSIType));
        
        //-- Suppress xsi:type for special types
        if (saveType) {
            //-- java.util.Enumeration and java.util.Date fix
            if (descriptor.getHandler() instanceof DateFieldHandler)
                saveType = false;
            else if (descriptor.getHandler() instanceof EnumFieldHandler)
                saveType = false;
            else if (isNil) 
                saveType = false;
        }
        

        //-- Suppress 'xsi:type' attributes when Castor is able to infer
        //-- the correct type during unmarshalling
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
             String xmlElementName = name;
             String xmlNamespace = descriptor.getNameSpaceURI();

             // We try to find if there is a XMLClassDescriptor associated
             // with the XML name of this class
             XMLClassDescriptor xmlElementNameClassDesc = null;
             try {
                 xmlElementNameClassDesc = _cdResolver.resolveByXMLName(xmlElementName, null, null);
             }
             catch(ResolverException rx) {
                 //-- exception not important as we're simply
                 //-- testing to see if we can resolve during
                 //-- unmarshalling
             }

             // Test if we are not dealing with a source generated vector
             if ((xmlElementName != null) && (xmlElementNameClassDesc != null)) {
                 // More than one class can map to a given element name
                 try {
                     ClassDescriptorEnumeration cdEnum = _cdResolver.resolveAllByXMLName(xmlElementName, null, null);
                     for (; cdEnum.hasNext();) {
                         xmlElementNameClassDesc = cdEnum.getNext();
                         if (_class == xmlElementNameClassDesc.getJavaClass())
                             break;
                          //reset the classDescriptor --> none has been found
                          xmlElementNameClassDesc = null;
                     }
                 }
                 catch(ResolverException rx) {
                     xmlElementNameClassDesc = null;
                 }

                 //make sure we only run into this logic if the classDescriptor
                 //is coming from a mapping file.
                 if  (xmlElementNameClassDesc instanceof XMLClassDescriptorAdapter) {

                    // Try to find a field descriptor directly in the parent object
                    XMLClassDescriptor tempContaining = (XMLClassDescriptor)descriptor.getContainingClassDescriptor();

                    //--if no containing class descriptor
                    //--it means the container class could have been introspected
                    //--so no need to enter the logic
                    if (tempContaining != null) {
                        XMLFieldDescriptor fieldDescMatch =
                            tempContaining.getFieldDescriptor(xmlElementName, xmlNamespace, NodeType.Element);

                        // Try to find a field descriptor by inheritance in the parent object
                        InheritanceMatch[] matches =
                              searchInheritance(xmlElementName, null, tempContaining, _cdResolver);

                        if (matches.length == 1) {

                            boolean foundTheRightClass = ((xmlElementNameClassDesc != null) && (_class == xmlElementNameClassDesc.getJavaClass()));

                            boolean oneAndOnlyOneMatchedField
                              = ((fieldDescMatch != null) ||
                                  (matches[0].parentFieldDesc == descriptor));

                            // Can we remove the xsi:type ?
                            if (foundTheRightClass && oneAndOnlyOneMatchedField) {
                                saveType = false;
                                //no name swapping for now
                            }
                        }//lengh is one
                    }
                 }//the classDesc comes from a mapping file
             }
         }//--- End of "if (saveType)"

                
        //------------------------/
        //- Namespace Management -/
        //------------------------/
        
        //-- Set a new namespace scoping
        //-- Note: We still need to declare a new scope even if
        //-- we are suppressing most namespaces. Certain elements
        //-- like xsi:type and xsi:nil will require a namespace 
        //-- declaration and cannot be suppressed.
        if (!atRoot) {
            _namespaces = _namespaces.createNamespaces();
        }
        
        String nsPrefix = "";
        String nsURI = "";
        
        if (!_suppressNamespaces) {
        
            //-- Must be done before any attributes are processed
            //-- since attributes can be namespaced as well.
    
            nsPrefix = descriptor.getNameSpacePrefix();
            if (nsPrefix == null) nsPrefix = classDesc.getNameSpacePrefix();
    
            nsURI = descriptor.getNameSpaceURI();
            if (nsURI == null) nsURI = classDesc.getNameSpaceURI();
    
            if ((nsURI == null) && (nsPrefix != null)) {
                nsURI = _namespaces.getNamespaceURI(nsPrefix);
            }
            else if ((nsPrefix == null) && (nsURI != null)) {
                nsPrefix = (String) _namespaces.getNamespacePrefix(nsURI);
            }
            //-- declare namespace at this element scope?
            if (nsURI != null) {
                String defaultNamespace = _namespaces.getNamespaceURI("");
    		    if ((nsPrefix == null) && (!nsURI.equals(defaultNamespace)))
    		    {
    		        if ((defaultNamespace == null) && atRoot) {
    		            nsPrefix = "";
    		        }
    		        else nsPrefix = DEFAULT_PREFIX + (++NAMESPACE_COUNTER);
    		    }
    			declareNamespace(nsPrefix, nsURI);
    		}
    		else {
                nsURI = "";
    		    //-- redeclare default namespace as empty
    		    String defaultNamespace = _namespaces.getNamespaceURI("");
    		    if ((defaultNamespace != null) && (!"".equals(defaultNamespace)))
    		        _namespaces.addNamespace("", "");
    		}
        }
        
        

        //---------------------/
        //- handle attributes -/
        //---------------------/

        AttributesImpl atts = new AttributesImpl();

        //-- user defined attributes
        if (atRoot) {
            //-- declare xsi prefix if necessary
            if (_topLevelAtts.getSize() > 0)
                _namespaces.addNamespace(XSI_PREFIX, XSI_NAMESPACE);

            for (int i = 0; i < _topLevelAtts.getSize(); i++) {
                String localName = _topLevelAtts.getName(i);
                String qName = localName;
                String ns = "";
                if (!_suppressNamespaces) {
                    ns = _topLevelAtts.getNamespace(i);
                    String prefix = null;
                    if ((ns != null) && (ns.length() > 0)) {
                        prefix = _namespaces.getNonDefaultNamespacePrefix(ns);
                    }
                    if ((prefix != null) && (prefix.length() > 0)) {
                        qName = prefix + ':' + qName;
                    }
                    if (ns == null) ns = "";
                }
                atts.addAttribute(ns, localName, qName, CDATA,
                    _topLevelAtts.getValue(i));
            }
        }

        //----------------------------
        //-- process attr descriptors
        //----------------------------

        int nestedAttCount = 0;
        XMLFieldDescriptor[] nestedAtts = null;
        XMLFieldDescriptor[] descriptors = null;
        if ((!descriptor.isReference()) && (!isNil)) {
            descriptors = classDesc.getAttributeDescriptors();
        }
        else {
            // references don't have attributes
            descriptors = NO_FIELD_DESCRIPTORS;
        }
        
        for (int i = 0; i < descriptors.length; i++) {
            if (descriptors[i] == null) continue;
            String path = descriptors[i].getLocationPath();
            if ((path != null) && (path.length() > 0)) {
                //-- save for later
                if (nestedAtts == null) {
                    nestedAtts = new XMLFieldDescriptor[descriptors.length - i];
                }
                nestedAtts[nestedAttCount++] = descriptors[i];
                continue;
            }
            processAttribute(object, descriptors[i], atts);
        }
        
        //-- handle ancestor nested attributes
        if (mstate.nestedAttCount > 0) {
            for (int i = 0; i < mstate.nestedAtts.length; i++) {
                XMLFieldDescriptor attDesc = mstate.nestedAtts[i];
                if (attDesc == null) continue;
                String path = attDesc.getLocationPath();
                if (name.equals(path)) {
                    mstate.nestedAtts[i] = null;
                    mstate.nestedAttCount = 0;
                    processAttribute(mstate.getOwner(), attDesc, atts);
                }
            }
        }
        

        //-- Look for attributes in container fields,
        //-- (also handle container in container)
        if (!isNil) processContainerAttributes(object, classDesc, atts);

        //-- xml:space
        String attValue = descriptor.getProperty(XMLFieldDescriptor.PROPERTY_XML_SPACE);
        if (attValue != null) {
            atts.addAttribute(Namespaces.XML_NAMESPACE, SPACE_ATTR, XML_SPACE_ATTR, CDATA, attValue);
        }
        
        //-- xml:lang
        attValue = descriptor.getProperty(XMLFieldDescriptor.PROPERTY_XML_LANG);
        if (attValue != null) {
            atts.addAttribute(Namespaces.XML_NAMESPACE, LANG_ATTR, XML_LANG_ATTR, CDATA, attValue);
        }
        
        
        //------------------/
        //- Create element -/
        //------------------/


        //-- save xsi:type information, if necessary

        if (saveType) {
            //-- declare XSI namespace, if necessary
            declareNamespace(XSI_PREFIX, XSI_NAMESPACE);

            //-- calculate type name, either use class name or
            //-- schema type name. If XMLClassDescriptor is introspected,
            //-- or is the default XMLClassDescriptorImpl, then
            //-- use java:classname, otherwise use XML name.
            String typeName = classDesc.getXMLName();
            
            //-- Check for introspection...
            boolean introspected = false;
            if (classDesc instanceof InternalXMLClassDescriptor)
                introspected = ((InternalXMLClassDescriptor)classDesc).introspected();
            else
                introspected = Introspector.introspected(classDesc);
                
            if ((typeName == null) || introspected) {
                typeName = JAVA_PREFIX + _class.getName();
            }
            else if (classDesc instanceof RootArrayDescriptor) {
                typeName = JAVA_PREFIX + _class.getName();
            }
            else {
                String dcn = classDesc.getClass().getName();
                if (dcn.equals(XMLClassDescriptorImpl.class.getName())) {
                    typeName = JAVA_PREFIX + _class.getName();
                }
                else {
                    //-- calculate proper prefix
                    String tns = classDesc.getNameSpaceURI();
                    String prefix = null;
                    if ((tns != null) && (tns.length() > 0)) {
                        prefix = _namespaces.getNamespacePrefix(tns);
                        if ((prefix != null) && (prefix.length() > 0)) {
                            typeName = prefix + ':' + typeName;
                        }
                    }
                }
            }
            //-- save type information
            atts.addAttribute(XSI_NAMESPACE, TYPE_ATTR, XSI_TYPE, CDATA, typeName);
        }
        
        if (isNil) {
            //-- declare XSI namespace, if necessary
            declareNamespace(XSI_PREFIX, XSI_NAMESPACE);
            //-- add xsi:nil="true"
        	atts.addAttribute(XSI_NAMESPACE, NIL_ATTR, XSI_NIL_ATTR, CDATA, TRUE_VALUE);
        }

       //check if the value is a QName that needs to
       //be resolved ({URI}value -> ns:value)
       //This should be done BEFORE declaring the namespaces as attributes
       //because we can declare new namespace during the QName resolution
       String valueType = descriptor.getSchemaType();
       if ((valueType != null) && (valueType.equals(QNAME_NAME))) {
           object = resolveQName(object, descriptor);
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

        
        Object firstNonNullValue = null;
        int    firstNonNullIdx   = 0;
        
        try {
            
            
            if (!containerField) {
                
                
                //-- isNillable?
                if ((!isNil) && descriptor.isNillable()) {
                    XMLFieldDescriptor desc = classDesc.getContentDescriptor();
                	descriptors = classDesc.getElementDescriptors();
                    int descCount = descriptors.length;
                    boolean isNilContent = (descCount > 0) || (desc != null);
                    
                    //-- check content descriptor for a valid value
                    if (desc != null) {
                        Object value = desc.getHandler().getValue(object);
                        if (value != null) {
                            isNilContent = false;
                            descCount = 0; 
                        }
                        else if (desc.isNillable() && desc.isRequired()) {
                            isNilContent = false;
                            descCount = 0; 
                        }
                    }
                    
                    
                    for (int i = 0; i < descCount; i++) {
                        desc = descriptors[i];
                        if (desc == null) continue;
                        Object value = desc.getHandler().getValue(object);
                        if (value != null) {
                            isNilContent = false;
                            firstNonNullIdx = i;
                            firstNonNullValue = value;
                            break;
                        }
                        else if (desc.isNillable() && desc.isRequired()) {
                        	isNilContent = false;
                            firstNonNullIdx = i;
                            firstNonNullValue = new NilObject(classDesc, desc);
                            break;
                        }
                    }
                    
                    if (isNilContent) {
                        declareNamespace(XSI_PREFIX, XSI_NAMESPACE);
                        atts.addAttribute(XSI_NAMESPACE, NIL_ATTR, XSI_NIL_ATTR, CDATA, TRUE_VALUE);
                    }
                }
                
                //-- declare all necesssary namespaces
                _namespaces.sendStartEvents(handler);
                //-- Make sure qName is not null
                if (qName == null) {
                    //-- hopefully this never happens, but if it does, it means
                    //-- we have a bug in our naming logic
                    String err = "Error in deriving name for type: " + 
                        _class.getName() + ", please report bug to: " +
                        "http://castor.exolab.org.";
                    throw new IllegalStateException(err);
                }
                
                
                handler.startElement(nsURI, name, qName, atts);
            } 
        }
        catch (org.xml.sax.SAXException sx) {
            throw new MarshalException(sx);
        }
        
        
        //---------------------------------------
        //-- process all child content, including
        //-- text nodes + daughter elements
        //---------------------------------------
        
        Stack wrappers = null;
        

        //----------------------
        //-- handle text content
        //----------------------
        
        if (!isNil) {
            
            XMLFieldDescriptor cdesc = null;
            if (!descriptor.isReference()) {
                cdesc = classDesc.getContentDescriptor();
            }
            if (cdesc != null) {
                Object obj = null;
                try {
                    obj = cdesc.getHandler().getValue(object);
                }
                catch(IllegalStateException ise) {}
                
                if (obj != null) {
                    
                    //-- <Wrapper>
                    //-- handle XML path
                    String path = cdesc.getLocationPath();
                    String currentLoc = null;
                    
                    if (path != null) {
                        _attributes.clear();
                        if (wrappers == null) {
                            wrappers  = new Stack();
                        }
                        try {
                            while (path != null) {
                                
                                String elemName = null;
                                int idx = path.indexOf('/');
                                
                                if (idx > 0) {
                                    elemName = path.substring(0, idx);
                                    path = path.substring(idx+1);
                                }
                                else {
                                    elemName = path;
                                    path = null;
                                }
                                
                                //-- save current location without namespace
                                //-- information for now.
                                if (currentLoc == null)
                                    currentLoc = elemName;
                                else
                                    currentLoc = currentLoc + "/" + elemName;
                                    
                                String elemQName = elemName;
                                if ((nsPrefix != null) && (nsPrefix.length() > 0)) {
                                    elemQName = nsPrefix + ':' + elemName;
                                }
                                wrappers.push(new WrapperInfo(elemName, elemQName, currentLoc));
                                
                                
                                _attributes.clear();
                                if (nestedAttCount > 0) {
                                    for (int na = 0; na < nestedAtts.length; na++) {
                                        if (nestedAtts[na] == null) continue;
                                        String tmpPath = nestedAtts[na].getLocationPath();
                                        if (tmpPath.equals(currentLoc)) {
                                            processAttribute(object, nestedAtts[na],_attributes);
                                            nestedAtts[na] = null;
                                            --nestedAttCount;
                                        }
                                    }
                                }
                                handler.startElement(nsURI, elemName, elemQName, _attributes);
                            }
                        }
                        catch(SAXException sx) {
                            throw new MarshalException(sx);
                        }
                    }
                    //-- </Wrapper>
                    
                    char[] chars = null;
                    Class objType = obj.getClass();
                    if (objType.isArray() && (objType.getComponentType() == Byte.TYPE)) {
                        //-- handle base64 content
                        chars = Base64Encoder.encode((byte[]) obj);
                    } else {
                        //-- all other types
                        String str = obj.toString();
                        if ((str != null) && (str.length() > 0)) {
                            chars = str.toCharArray();
                        }
                    }
                    if ((chars != null) && (chars.length > 0)) {
                        try {
                            handler.characters(chars, 0, chars.length);
                        }
                        catch(org.xml.sax.SAXException sx) {
                            throw new MarshalException(sx);
                        }
                    }
                }
            }
            //-- element references
            else if (descriptor.isReference()) {
                Object id = getObjectID(object);
                if (id != null) {
                    char[] chars = id.toString().toCharArray();
                    try {
                        handler.characters(chars, 0, chars.length);
                    }
                    catch(org.xml.sax.SAXException sx) {
                        throw new MarshalException(sx);
                    }
                }
            }
            // special case for byte[]
            else if (byteArray) {
                //-- Base64Encoding
                char[] chars = Base64Encoder.encode((byte[]) object);
                try {
                    handler.characters(chars, 0, chars.length);
                } catch (org.xml.sax.SAXException sx) {
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
        }

        //---------------------------
        //-- handle daughter elements
        //---------------------------
        
        if (isNil || descriptor.isReference()) {
        	descriptors = NO_FIELD_DESCRIPTORS;
        }
        else {
            descriptors = classDesc.getElementDescriptors();
        }

        ++depth;
        
        //-- marshal elements        
        for (int i = firstNonNullIdx; i < descriptors.length; i++) {

            XMLFieldDescriptor elemDescriptor = descriptors[i];
            Object obj = null;
            boolean nil = false;
            
            //-- used previously cached value?
            if ((i == firstNonNullIdx) && (firstNonNullValue != null)) {
            	obj = firstNonNullValue;
            }
            //-- obtain value from handler
            else {
                try {
                    obj = elemDescriptor.getHandler().getValue(object);
                }
                catch(IllegalStateException ise) {
                    continue;
                }
            }
            
            if (obj == null) {
                if (elemDescriptor.isNillable() && (elemDescriptor.isRequired())) {
                    nil = true;
                }
                else continue;
            }
            
            
            //-- handle XML path
            String path = elemDescriptor.getLocationPath();
            String currentLoc = null;
            //-- Wrapper/Location cleanup
            if (wrappers != null) {
                try {
                    while (!wrappers.empty()) {
                        WrapperInfo wInfo = (WrapperInfo)wrappers.peek();
                        if (path != null) {
                            if (wInfo.location.equals(path)) {
                                path = null;
                                break;
                            }
                            else if (path.startsWith(wInfo.location + "/")) {
                                path = path.substring(wInfo.location.length()+1);
                                currentLoc = wInfo.location;
                                break;
                            }
                        }
                        handler.endElement(nsURI, wInfo.localName, wInfo.qName);
                        wrappers.pop();
                    }
                }
                catch(SAXException sx) {
                    throw new MarshalException(sx);
                }
            }
            
            
            if (path != null) {
                _attributes.clear();
                if (wrappers == null) {
                    wrappers  = new Stack();
                }
                try {
                    while (path != null) {
                        
                        String elemName = null;
                        int idx = path.indexOf('/');
                        
                        if (idx > 0) {
                            elemName = path.substring(0, idx);
                            path = path.substring(idx+1);
                        }
                        else {
                            elemName = path;
                            path = null;
                        }
                        
                        //-- save current location without namespace
                        //-- information for now.
                        if (currentLoc == null)
                            currentLoc = elemName;
                        else
                            currentLoc = currentLoc + "/" + elemName;
                            
                        String elemQName = elemName;
                        if ((nsPrefix != null) && (nsPrefix.length() > 0)) {
                            elemQName = nsPrefix + ':' + elemName;
                        }
                        wrappers.push(new WrapperInfo(elemName, elemQName, currentLoc));
                        
                        
                        _attributes.clear();
                        if (nestedAttCount > 0) {
                            for (int na = 0; na < nestedAtts.length; na++) {
                                if (nestedAtts[na] == null) continue;
                                String tmpPath = nestedAtts[na].getLocationPath();
                                if (tmpPath.equals(currentLoc)) {
                                    processAttribute(object, nestedAtts[na],_attributes);
                                    nestedAtts[na] = null;
                                    --nestedAttCount;
                                }
                            }
                        }
                        handler.startElement(nsURI, elemName, elemQName, _attributes);
                    }
                }
                catch(SAXException sx) {
                    throw new MarshalException(sx);
                }
            }
            
            if (nil) {
                obj = new NilObject(classDesc, elemDescriptor);
            }

            final Class type = obj.getClass();
            
            MarshalState myState = mstate.createMarshalState(object, name);
            myState.nestedAtts = nestedAtts;
            myState.nestedAttCount = nestedAttCount;
            
            
            //-- handle byte arrays
            if (type.isArray() && (type.getComponentType() == Byte.TYPE)) {
                marshal(obj, elemDescriptor, handler, myState);
            }
            //-- handle all other collection types
            else if (isCollection(type)) {
                boolean processCollection = true;
                if (_saveMapKeys) {
                    MapHandler mapHandler = MapHandlers.getHandler(type);
                    if (mapHandler != null) {
                        processCollection = false;
                        MapItem item = new MapItem();
                        Enumeration keys = mapHandler.keys(obj);
                        while (keys.hasMoreElements()) {
                            item.setKey(keys.nextElement());
                            item.setValue(mapHandler.get(obj, item.getKey()));
                            marshal(item, elemDescriptor, handler, myState);
                        }                        
                    }
                    
                }
                if (processCollection) {
                    CollectionHandler colHandler = getCollectionHandler(type);
                    Enumeration enumeration = colHandler.elements(obj);
                    while (enumeration.hasMoreElements()) {
                        Object item = enumeration.nextElement();
                        if (item != null) {
                            marshal(item, elemDescriptor, handler, myState);
                        }
                    }
                }
            }
            //-- otherwise just marshal object as is
            else marshal(obj, elemDescriptor, handler, myState);
            
            if (nestedAttCount > 0) {
                nestedAttCount = myState.nestedAttCount;
            }
            
        }
        
        
        //-- Wrapper/Location cleanup for elements
        if (wrappers != null) {
            try {
                while (!wrappers.empty()) {
                    WrapperInfo wInfo = (WrapperInfo)wrappers.pop();
                    handler.endElement(nsURI, wInfo.localName, wInfo.qName);
                }
            }
            catch(SAXException sx) {
                throw new MarshalException(sx);
            }
        }
        
        
        
        //-- Handle any additional attribute locations that were
        //-- not handled when dealing with wrapper elements
        if (nestedAttCount > 0) {
            if (wrappers == null) wrappers = new Stack();
            for (int i = 0; i < nestedAtts.length; i++) {
                if (nestedAtts[i] == null) continue;
                String path = nestedAtts[i].getLocationPath();
                //-- Make sure attribute has value before continuing
                //-- We really could use a FieldHandler#hasValue() 
                //-- method (since sometimes getValue() methods may
                //-- be expensive and we don't always want to call it
                //-- multiple times)
                if (nestedAtts[i].getHandler().getValue(object) == null) {
                    nestedAtts[i] = null;
                    -- nestedAttCount;
                    continue;
                }
                String currentLoc = null;
                try {
                    while (path != null) {                        
                        int idx = path.indexOf('/');
                        String elemName = null;
                        if (idx > 0) {
                            elemName = path.substring(0,idx);
                            path = path.substring(idx+1);
                        }
                        else {
                            elemName = path;
                            path = null;
                        }
                        if (currentLoc == null)
                            currentLoc = elemName;
                        else
                            currentLoc = currentLoc + "/" + elemName;
                            
                        String elemQName = elemName;
                        if ((nsPrefix != null) && (nsPrefix.length() > 0)) {
                            elemQName = nsPrefix + ':' + elemName;
                        }
                        wrappers.push(new WrapperInfo(elemName, elemQName, null));
                        
                        _attributes.clear();
                        if (path == null) {
                            processAttribute(object, nestedAtts[i],_attributes);
                            nestedAtts[i] = null;
                            --nestedAttCount;
                        }
                        if (nestedAttCount > 0) {
                            for (int na = i+1; na < nestedAtts.length; na++) {
                                if (nestedAtts[na] == null) continue;
                                String tmpPath = nestedAtts[na].getLocationPath();
                                if (tmpPath.equals(currentLoc)) {
                                    processAttribute(object, nestedAtts[na],_attributes);
                                    nestedAtts[na] = null;
                                    --nestedAttCount;
                                }
                            }
                        }
                        handler.startElement(nsURI, elemName, elemQName, _attributes);
                    }
                
                    while (!wrappers.empty()) {
                        WrapperInfo wInfo = (WrapperInfo)wrappers.pop();
                        handler.endElement(nsURI, wInfo.localName, wInfo.qName);
                    }
                } catch (Exception e) {
                    throw new MarshalException(e);
                }        
            }
        } // if (nestedAttCount > 0)

        //-- finish element
        try {
            if (!containerField) {
                handler.endElement(nsURI, name, qName);
                //-- undeclare all necesssary namespaces
                _namespaces.sendEndEvents(handler);
            }
        }
        catch(org.xml.sax.SAXException sx) {
            throw new MarshalException(sx);
        }

        --depth;
        _parents.pop();
        if (!atRoot) _namespaces = _namespaces.getParent();

        //-- notify listener of post marshal
        if (_marshalListener != null)
            _marshalListener.postMarshal(object);

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
     * @return true if the namespace was not in scope and was
     *  sucessfully declared, other false
    **/
    private boolean declareNamespace(String nsPrefix, String nsURI)
    {
        boolean declared = false;
        
        //-- make sure it's not already declared...
        if ( (nsURI != null) && (nsURI.length() != 0)) {
            
            String tmpURI = _namespaces.getNamespaceURI(nsPrefix);
            if ((tmpURI != null) && (tmpURI.equals(nsURI))) {
                return declared;
            }
            String tmpPrefix = _namespaces.getNamespacePrefix(nsURI);
            if ((tmpPrefix == null) || (!tmpPrefix.equals(nsPrefix))) {
                _namespaces.addNamespace(nsPrefix, nsURI);
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
     * Sets the encoding for the serializer. Note that this method
     * cannot be called if you've passed in your own DocumentHandler.
     *
     * @param encoding the encoding to set
    **/
    public void setEncoding(String encoding) {

        if (_serializer != null) {
            if (_format == null) {
                _format = _config.getOutputFormat();
            }
            _format.setEncoding(encoding);
            //-- reset output format, this needs to be done
            //-- any time a change occurs to the format.
            _serializer.setOutputFormat( _format );
            try {
                //-- Due to a Xerces Serializer bug that doesn't allow declaring
                //-- multiple prefixes to the same namespace, we use the old
                //-- DocumentHandler format and process namespaces ourselves        
                _handler = new DocumentHandlerAdapter(_serializer.asDocumentHandler());
            }
            catch (java.io.IOException iox) {
                //-- we can ignore this exception since it shouldn't
                //-- happen. If _serializer is not null, it means
                //-- we've already called this method sucessfully
                //-- in the Marshaller() constructor
            }
        }
        else {
            String error = "encoding cannot be set if you've passed in "+
            "your own DocumentHandler";
            throw new IllegalStateException(error);
        }
    } //-- setEncoding

    /**
     * Sets the value for the xsi:noNamespaceSchemaLocation attribute.
     * When set, this attribute will appear on the root element
     * of the marshalled document.
     *
     * @param schemaLocation the URI location of the schema
     * to which the marshalled document is an instance of.
    **/
    public void setNoNamespaceSchemaLocation(String schemaLocation) {
        if (schemaLocation == null) {
            //-- remove if necessary
            //-- to be added later.
        }
        else {
            _topLevelAtts.setAttribute(XSI_NO_NAMESPACE_SCHEMA_LOCATION,
                schemaLocation, XSI_NAMESPACE);
        }
    } //-- setNoNamespaceSchemaLocation

    /**
     * Sets the value for the xsi:schemaLocation attribute.
     * When set, this attribute will appear on the root element
     * of the marshalled document.
     *
     * @param schemaLocation the URI location of the schema
     * to which the marshalled document is an instance of.
    **/
    public void setSchemaLocation(String schemaLocation) {
        if (schemaLocation == null) {
            //-- remove if necessary
            //-- to be added later.
        }
        else {
            _topLevelAtts.setAttribute(XSI_SCHEMA_LOCATION,
                schemaLocation, XSI_NAMESPACE);
        }
    } //-- setSchemaLocation
    
    /**
     * Sets whether or not namespaces are output. By default
     * the Marshaller will output namespace declarations and
     * prefix elements and attributes with their respective
     * namespace prefix. This method can be used to prevent
     * the usage of namespaces.
     * 
     * @param suppressNamespaces a boolean that when true
     * will prevent namespaces from being output.
     */
    public void setSuppressNamespaces(boolean suppressNamespaces) {
        _suppressNamespaces = suppressNamespaces;
    } //-- setSuppressNamespaces

    /**
     * Sets whether or not the xsi:type attribute should appear
     * on the marshalled document.
     *
     * @param suppressXSIType a boolean that when true will prevent
     * xsi:type attribute from being used in the marshalling process.
     */
     public void setSuppressXSIType(boolean suppressXSIType) {
        _suppressXSIType = suppressXSIType;
     } //-- setSuppressXSIType

     /**
      * Sets whether or not to output the xsi:type at the root
      * element. This is usually needed when the root element
      * type cannot be determined by the element name alone.
      * By default xsi:type will not be output on the root
      * element.
      * 
      * @param useXSITypeAtRoot a boolean that when true indicates
      * that the xsi:type should be output on the root element.
      */
     public void setUseXSITypeAtRoot(boolean useXSITypeAtRoot) {
         _useXSITypeAtRoot = useXSITypeAtRoot;
     } //-- setUseXSITypeAtRoot
     
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
        
        try {
            if (!isPrimitive(_class))
                classDesc = _cdResolver.resolve(_class);
        }
        catch(ResolverException rx) {
            Throwable actual = rx.getCause();
            if (actual instanceof MarshalException) {
                throw (MarshalException)actual;
            }
            if (actual != null) {
                throw new MarshalException(actual);
            }
            throw new MarshalException(rx);
        }
        
        if (classDesc != null)
            classDesc = new InternalXMLClassDescriptor(classDesc);
            
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
        XMLClassDescriptor classDesc = null;
        
        try {
            classDesc = _cdResolver.resolve(className, loader);
        }
        catch(ResolverException rx) {
            Throwable actual = rx.getCause();
            if (actual instanceof MarshalException) {
                throw (MarshalException)actual;
            }
            if (actual != null) {
                throw new MarshalException(actual);
            }
            throw new MarshalException(rx);
        }
        
        if (classDesc != null)
            classDesc = new InternalXMLClassDescriptor(classDesc);
        
        return classDesc;
    } //-- getClassDescriptor


    /**
     * Processes the attribute associated with the given attDescriptor and parent
     * object.
     *
     * @param atts the SAX attribute list to add the attribute to
     */
    private void processAttribute
        (Object object, XMLFieldDescriptor attDescriptor, AttributesImpl atts) 
        throws MarshalException
    {
        if (attDescriptor == null) return;
        
        //-- process Namespace nodes from Object Model,
        //-- if necessary.
        if (attDescriptor.getNodeType() == NodeType.Namespace) {
            if (!_suppressNamespaces) {
                Object map = attDescriptor.getHandler().getValue(object);
                MapHandler mapHandler = MapHandlers.getHandler(map);
                if (mapHandler != null) {
                    Enumeration keys = mapHandler.keys(map);
                    while (keys.hasMoreElements()) {
                        Object key = keys.nextElement();
                        Object val = mapHandler.get(map, key);
                        declareNamespace(key.toString(), val.toString());
                    }
                }
            }
            return;
        }

        String localName = attDescriptor.getXMLName();
        String qName     = localName;

        //-- handle attribute namespaces
        String namespace = "";
        if (!_suppressNamespaces) {
            namespace = attDescriptor.getNameSpaceURI();
            if ((namespace != null) && (namespace.length() > 0)) {
                String prefix = attDescriptor.getNameSpacePrefix();
                if ((prefix == null) || (prefix.length() == 0))
                    prefix = _namespaces.getNonDefaultNamespacePrefix(namespace);
    
                if ((prefix == null) || (prefix.length() == 0)) {
                    //-- automatically create namespace prefix?
                    prefix = DEFAULT_PREFIX + (++NAMESPACE_COUNTER);
                }
                declareNamespace(prefix, namespace);
                qName = prefix + ':' + qName;
            }
            else namespace = "";
        }

        Object value = null;

        try {
            value = attDescriptor.getHandler().getValue(object);
        }
        catch(IllegalStateException ise) {
            return;
        }

        //-- handle IDREF(S)
        if (attDescriptor.isReference() && (value != null)) {

            if (attDescriptor.isMultivalued()) {
                Enumeration enumeration = null;
                if (value instanceof Enumeration) {
                    enumeration = (Enumeration)value;
                }
                else {
                    CollectionHandler colHandler = null;
                    try {
                        colHandler = CollectionHandlers.getHandler(value.getClass());
                    }
                    catch(MappingException mx) {
                        throw new MarshalException(mx);
                    }
                    enumeration = colHandler.elements(value);
                }
                if (enumeration.hasMoreElements()) {
                    StringBuffer sb = new StringBuffer();
                    for (int v = 0; enumeration.hasMoreElements(); v++) {
                        if (v > 0) sb.append(' ');
                        sb.append(getObjectID(enumeration.nextElement()).toString());
                    }
                    value = sb;
                }
                else value = null;
            }
            else {
                value = getObjectID(value);
            }
        }        
        //-- handle multi-value attributes
        else if (attDescriptor.isMultivalued()) {
            Enumeration enumeration = null;
            if (value instanceof Enumeration) {
                enumeration = (Enumeration)value;
            }
            else {
                CollectionHandler colHandler = null;
                try {
                    colHandler = CollectionHandlers.getHandler(value.getClass());
                }
                catch(MappingException mx) {
                    throw new MarshalException(mx);
                }
                enumeration = colHandler.elements(value);
            }
            if (enumeration.hasMoreElements()) {
                StringBuffer sb = new StringBuffer();
                for (int v = 0; enumeration.hasMoreElements(); v++) {
                    if (v > 0) sb.append(' ');
                    sb.append(enumeration.nextElement()).toString();
                }
                value = sb;
            }
            else value = null;
        }               
        else if (value != null) {
            //-- handle base64 content
            Class objType = value.getClass();
            if (objType.isArray() && (objType.getComponentType() == Byte.TYPE)) {
                value = Base64Encoder.encode((byte[]) value);
            }
        }        

        if (value != null) {
            //check if the value is a QName that needs to
            //be resolved ({URI}value -> ns:value).
            String valueType = attDescriptor.getSchemaType();
            if ((valueType != null) && (valueType.equals(QNAME_NAME)))
                    value = resolveQName(value, attDescriptor);

            atts.addAttribute(namespace, localName, qName, CDATA, value.toString());
        }
    } //-- processAttribute
    

    /**
     * Processes the attributes for container objects
     *
     * @param target the object currently being marshalled.
     * @param classDesc the XMLClassDescriptor for the target object
     * @param atts the SAX attributes list to add attributes to
     */
    private void processContainerAttributes
        (Object target, XMLClassDescriptor classDesc, AttributesImpl atts)
        throws MarshalException
    {
        if (classDesc instanceof XMLClassDescriptorImpl) {
            if (!((XMLClassDescriptorImpl)classDesc).hasContainerFields())
                return;
        }

        XMLFieldDescriptor[] elemDescriptors = classDesc.getElementDescriptors();
        for (int i = 0; i < elemDescriptors.length; i++) {
            if (elemDescriptors[i] == null) continue;
            if (!elemDescriptors[i].isContainer()) continue;
            processContainerAttributes(target, elemDescriptors[i], atts);
        }
    } //-- processContainerAttributes
            
    /**
     * Processes the attributes for container objects
     *
     * @param target the object currently being marshalled.
     * @param containerFieldDesc the XMLFieldDescriptor for the containter to process
     * @param atts the SAX attributes list to add any necessary attributes to.
     */
    private void processContainerAttributes
        (Object target, XMLFieldDescriptor containerFieldDesc, AttributesImpl atts)
        throws MarshalException
    {
        if (target.getClass().isArray()) {
             int length = Array.getLength(target);
             for (int j = 0; j < length; j++) {
                  Object item = Array.get(target, j);
                  if (item != null)
                      processContainerAttributes(item, containerFieldDesc, atts);
             }
             return;
        }
        else if (target instanceof Enumeration) {
            Enumeration enumeration = (Enumeration)target;
            while (enumeration.hasMoreElements()) {
                Object item = enumeration.nextElement();
                if (item != null)
                    processContainerAttributes(item, containerFieldDesc, atts);
            }
            return;
        }

        Object containerObject = containerFieldDesc.getHandler().getValue(target);

        if (containerObject == null) return;

        XMLClassDescriptor containerClassDesc 
            = (XMLClassDescriptor)containerFieldDesc.getClassDescriptor();
            
        if (containerClassDesc == null) {
            containerClassDesc = getClassDescriptor(containerFieldDesc.getFieldType());
            if (containerClassDesc == null) return;
        }

        // Look for attributes
        XMLFieldDescriptor[] attrDescriptors = containerClassDesc.getAttributeDescriptors();
        for (int idx = 0; idx < attrDescriptors.length; idx++) {
            if (attrDescriptors[idx] == null) continue;
            processAttribute(containerObject, attrDescriptors[idx], atts);
        }

        // recursively process containers
        processContainerAttributes(containerObject, containerClassDesc, atts);
    } //-- processContainerAttributes

    /**
     * Resolve a QName value ({URI}value) by declaring a namespace after
     * having retrieved the prefix.
     */
    private Object resolveQName(Object value, XMLFieldDescriptor fieldDesc) {
        if ( (value == null) || !(value instanceof String))
            return value;
        if (!(fieldDesc instanceof XMLFieldDescriptorImpl))
           return value;

        String result = (String)value;

        String nsURI = null;
        int idx = -1;
        if ((result.length() > 0) && (result.charAt(0) == '{')) {
            idx = result.indexOf('}');
            if (idx <= 0) {
                String err = "Bad QName value :'"+result+"', it should follow the pattern '{URI}value'";
                throw new IllegalArgumentException(err);
            }
            nsURI = result.substring(1, idx);
        }
        else return value;

        String prefix = ((XMLFieldDescriptorImpl)fieldDesc).getQNamePrefix();
        //no prefix provided, check if one has been previously defined
        if (prefix == null)
            prefix = _namespaces.getNamespacePrefix(nsURI);
        //if still no prefix, use a naming algorithm (ns+counter).
        if (prefix == null)
            prefix = DEFAULT_PREFIX+(++NAMESPACE_COUNTER);
        result = (prefix.length() != 0)?prefix+":"+result.substring(idx+1):result.substring(idx+1);
        declareNamespace(prefix, nsURI);
        return result;
    }

    private void validate(Object object)
        throws ValidationException
    {
        if  (_validate) {
            //-- we must have a valid element before marshalling
            Validator validator = new Validator();
            ValidationContext context = new ValidationContext();
            context.setConfiguration(_config);
            context.setResolver(_cdResolver);
            validator.validate(object, context);
        }
    }
    
    /**
     * Inner-class used for handling wrapper elements 
     * and locations
     */
    static class WrapperInfo {
        
        String localName  = null;
        String qName      = null;
        String location   = null;
        
        WrapperInfo(String localName, String qName, String location) {
            this.localName = localName;
            this.qName = qName;
            this.location = location;
        }
    }
    
    
    static class MarshalState {
        
        String xpath = null;
        XMLFieldDescriptor[] nestedAtts = null;
        int nestedAttCount = 0;
        
        private MarshalState _parent = null;        
        private Object _owner        = null;
        private String _xmlName      = null;
        
        MarshalState(Object owner, String xmlName) {
            if (owner == null) {
                String err = "The argument 'owner' must not be null";
                throw new IllegalArgumentException(err);
            }
            if (xmlName == null) {
                String err = "The argument 'xmlName' must not be null";
                throw new IllegalArgumentException(err);
            }
            _owner = owner;
            _xmlName = xmlName;
        }
        
        
        MarshalState createMarshalState(Object owner, String xmlName) {
            MarshalState ms = new MarshalState(owner, xmlName);
            ms._parent = this;
            return ms;
        }
        
        String getXPath() {
            if (xpath == null) {
                if (_parent != null) {
                    xpath = _parent.getXPath() + "/" + _xmlName;
                }
                else {
                    xpath = _xmlName;
                }
            }
            return xpath;
        }
        
        Object getOwner() {
            return _owner;
        }        
        
        MarshalState getParent() {
            return _parent;
        }
    }
    
    /**
     * A wrapper for a "Nil" object
     * 
     */
    public static class NilObject {
        
        private XMLClassDescriptor _classDesc = null;
        private XMLFieldDescriptor _fieldDesc = null;
        
        NilObject(XMLClassDescriptor classDesc, XMLFieldDescriptor fieldDesc) {
            _classDesc = classDesc;
            _fieldDesc = fieldDesc;
        }
        
        /**
         * Returns the associated XMLClassDescriptor
         * 
         * @return the XMLClassDescriptor
         */
        public XMLClassDescriptor getClassDescriptor() {
            return _classDesc;
        }
        
        /**
         * Returns the associated XMLFieldDescriptor
         * 
         * @return the associated XMLFieldDescriptor
         */
        public XMLFieldDescriptor getFieldDescriptor() {
            return _fieldDesc;
        }
    }

} //-- Marshaller


