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
 * Copyright 1999-2003 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id$
 */

package org.exolab.castor.xml;


//-- castor imports
import org.exolab.castor.mapping.CollectionHandler;
import org.exolab.castor.mapping.ClassDescriptor;
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
import org.exolab.castor.util.MimeBase64Encoder;
import org.exolab.castor.util.NestedIOException;
import org.exolab.castor.util.Stack;

//-- misc xml related imports
import org.xml.sax.*;
import org.xml.sax.helpers.AttributeListImpl;
import org.xml.sax.helpers.ParserAdapter;

import org.w3c.dom.Node;
import org.apache.xml.serialize.Serializer;
import org.apache.xml.serialize.OutputFormat;

import java.io.IOException;
import java.io.PrintWriter;
//import java.io.Serializable;
import java.io.Writer;
import java.lang.reflect.Array;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

/**
 * A Marshaller to allowing serializing Java Object's to XML
 *
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a>
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
     * The document handler we are marshalling to
    **/
    private DocumentHandler  _handler      = null;

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
     * The serializer that is being used for marshalling.
     * This may be null if the user passed in a DocumentHandler.
    **/
    private Serializer       _serializer   = null;

    /**
     * A flag to allow suppressing the xsi:type attribute
     */
    private boolean _suppressXSIType = false;
    
    /**
     * The set of optional top-level attributes
     * set by the user.
    **/
    private AttributeSetImpl _topLevelAtts = null;

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
        _handler = handler;

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


        //-- wrap content handler to be compatable with
        //-- document handler
        if (handler instanceof DocumentHandler)
            _handler = (DocumentHandler)handler;
        else {
            try {
                ParserAdapter adapter = new ParserAdapter();
                adapter.setContentHandler(handler);
                _handler = adapter;
            }
            catch(SAXException sx) {
                throw new NestedIOException(sx);
            }
        }

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

        _handler = _serializer.asDocumentHandler();
        if ( _handler == null ) {
            String err = Messages.format( this.SERIALIZER_NOT_SAX_CAPABLE,
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
		_handler = new SAX2DOMHandler( node );

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
        _topLevelAtts    = new AttributeSetImpl();
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

            if (_format == null) {
                _format = _config.getOutputFormat();
            }
            _format.setOmitXMLDeclaration( ! asDocument );

            //-- reset output format, this needs to be done
            //-- any time a change occurs to the format.
            _serializer.setOutputFormat( _format );
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
              AnyNode2SAX.fireEvents((AnyNode)object, _handler, _namespaces);
           } catch(SAXException e) {
                throw new MarshalException(e);
           }
        }
        else {
             validate(object);
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
                    marshal(object, null, _handler);
                    _handler.endDocument();
                } catch (SAXException sx) {
                    throw new MarshalException(sx);
                }
             }
             else {
                marshal(object, null, _handler);
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
         DocumentHandler handler)
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
               AnyNode2SAX.fireEvents((AnyNode) object, handler, _namespaces);
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
                Class fieldType = descriptor.getFieldType();
                if (_class != fieldType) {
                    while (fieldType.isArray()) {
                        fieldType = fieldType.getComponentType();
                    }
                    saveType = (!primitiveOrWrapperEquals(_class, fieldType));
                }
            }
            else {
                saveType = (_class.isArray());
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
				
				//-- check to see if we need to save the type
				//-- information in the XML using xsi:type
				//-- If we can resolve the class based on the
				//-- XML name, we don't need to save the type.
				if ((_class != descriptor.getFieldType()) && 
				    (classDesc != null)) 
				{
				    String nsURI = classDesc.getNameSpaceURI();
				    String tmpName = name;
				    if (autoNameByClass) {
				        if (classDesc.getXMLName() != null) 
				            tmpName = classDesc.getXMLName();
				    }
				    
				    ClassDescriptor tmpCDesc = 
				        _cdResolver.resolveByXMLName(tmpName, nsURI, null);
				    if (tmpCDesc != null) {
				        saveType = (tmpCDesc.getClass() != classDesc.getClass());
				    }
				    else {
				        saveType = true;
				    }
				}
			} 
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

			if (atRoot && _rootElement!=null)
				name = _rootElement;
			else if (descriptor.getXMLName()==null)
			      if (classDesc != null) {
                    name = classDesc.getXMLName();
                  }
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
        
        //-- handle auto-naming by class
        if (autoNameByClass) {
            if (classDesc.getXMLName() != null)
                name = classDesc.getXMLName();
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

             // We try to find if there is a XMLClassDescriptor associated
             // with the XML name of this class
             XMLClassDescriptor xmlElementNameClassDesc = _cdResolver.resolveByXMLName(xmlElementName, null, null);

             // Test if we are not dealing with a source generated vector
             if ((xmlElementName != null) && (xmlElementNameClassDesc != null)) {
                 // More than one class can map to a given element name
                 ClassDescriptorEnumeration cdEnum= _cdResolver.resolveAllByXMLName(xmlElementName, null, null);
                 for (; cdEnum.hasNext();) {
                     xmlElementNameClassDesc = cdEnum.getNext();
                     if (_class == xmlElementNameClassDesc.getJavaClass())
                         break;
                      //reset the classDescriptor --> none has been found
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
                        XMLFieldDescriptor fieldDescMatch = tempContaining.getFieldDescriptor(xmlElementName, NodeType.Element);

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
        if (!atRoot) {
            _namespaces = _namespaces.createNamespaces();
        }
        
        //-- Must be done before any attributes are processed
        //-- since attributes can be namespaced as well.

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
		    //-- redeclare default namespace as empty
		    String defaultNamespace = _namespaces.getNamespaceURI("");
		    if ((defaultNamespace != null) && (!"".equals(defaultNamespace)))
		        _namespaces.addNamespace("", "");
		}
        
        

        //---------------------/
        //- handle attributes -/
        //---------------------/

        AttributeListImpl atts = new AttributeListImpl();

        //-- user defined attributes
        if (atRoot) {
            //-- declare xsi prefix if necessary
            if (_topLevelAtts.getSize() > 0)
                _namespaces.addNamespace(XSI_PREFIX, XSI_NAMESPACE);

            for (int i = 0; i < _topLevelAtts.getSize(); i++) {
                String attName = _topLevelAtts.getName(i);
                String ns = _topLevelAtts.getNamespace(i);
                String prefix = null;
                if ((ns != null) && (ns.length() > 0)) {
                    prefix = _namespaces.getNonDefaultNamespacePrefix(ns);
                }
                if ((prefix != null) && (prefix.length() > 0)) {
                    attName = prefix + ':' + attName;
                }
                atts.addAttribute(attName, CDATA,
                    _topLevelAtts.getValue(i));
            }
        }

        //----------------------------
        //-- process attr descriptors
        //----------------------------

        XMLFieldDescriptor[] descriptors = null;
        if (!descriptor.isReference()) {
            descriptors = classDesc.getAttributeDescriptors();
        }
        else {
            // references don't have attributes
            descriptors = new XMLFieldDescriptor[0];
        }
        
        for (int i = 0; i < descriptors.length; i++) {
            if (descriptors[i] == null) continue;
            processAttribute(object, descriptors[i], atts);
        }


        //-- Look for attributes in container fields,
        //-- (also handle container in container)
        processContainerAttributes(object, classDesc, atts);

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
            if ((typeName == null) || Introspector.introspected(classDesc)) {
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
            atts.addAttribute(XSI_TYPE, CDATA, typeName);
        }

       //check if the value is a QName that needs to
       //be resolved ({URI}value -> ns:value)
       //This should be done BEFORE declaring the namespaces as attributes
       //because we can declare new namespace during the QName resolution
       String valueType = descriptor.getSchemaType();
       if ((valueType != null) && (valueType.equals(QNAME_NAME)))
           object = resolveQName(object, descriptor);


        //-- declare all necesssary namespaces
		_namespaces.declareAsAttributes(atts, true);
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
            if (!containerField)
                handler.startElement(qName, atts);
        }
        catch (org.xml.sax.SAXException sx) {
            throw new MarshalException(sx);
        }

        //----------------------
        //-- handle text content
        //----------------------
        
        XMLFieldDescriptor cdesc = null;
        if (!descriptor.isReference()) {
            cdesc = classDesc.getContentDescriptor();
        }
        if (cdesc != null) {
            Object obj = null;
            try {
                obj = cdesc.getHandler().getValue(object);
            }
            catch(IllegalStateException ise) {};
            if (obj != null) {
                char[] chars = null;
                //-- handle base64 content
                Class objType = obj.getClass();
                if (objType.isArray() &&
                   (objType.getComponentType() == Byte.TYPE))
                {
                    MimeBase64Encoder encoder = new MimeBase64Encoder();
                    encoder.translate((byte[])obj);
                    chars = encoder.getCharArray();
                }
                //-- all other types
                else {
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

        //---------------------------
        //-- handle daughter elements
        //---------------------------
        
        descriptors = classDesc.getElementDescriptors();
        if (descriptor.isReference()) {
            descriptors = new XMLFieldDescriptor[0];
        }

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
            
            //-- handle byte arrays
            if (type.isArray() && (type.getComponentType() == Byte.TYPE)) {
                marshal(obj, elemDescriptor, handler);
            }
            //-- handle all other collection types
            else if (isCollection(type)) {
                CollectionHandler colHandler = getCollectionHandler(type);
                Enumeration enum = colHandler.elements(obj);
                while (enum.hasMoreElements()) {
                    Object item = enum.nextElement();
                    if (item != null) {
                        marshal(item, elemDescriptor, handler);
                    }
                }
            }
            //-- otherwise just marshal object as is
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
                _handler = _serializer.asDocumentHandler();
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


    /**
     * Processes the attribute associated with the given attDescriptor and parent
     * object.
     *
     * @param atts the SAX attribute list to add the attribute to
     */
    private void processAttribute
        (Object object, XMLFieldDescriptor attDescriptor, AttributeListImpl atts) 
        throws MarshalException
    {
        if (attDescriptor == null) return;
        
        //-- process Namespace nodes from Object Model,
        //-- if necessary.
        if (attDescriptor.getNodeType() == NodeType.Namespace) {
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
            return;
        }

        String xmlName = attDescriptor.getXMLName();

        //-- handle attribute namespaces
        String namespace = attDescriptor.getNameSpaceURI();
        if ((namespace != null) && (namespace.length() > 0)) {
            String prefix = attDescriptor.getNameSpacePrefix();
            if ((prefix == null) || (prefix.length() == 0))
                prefix = _namespaces.getNonDefaultNamespacePrefix(namespace);

            if ((prefix == null) || (prefix.length() == 0)) {
                //-- automatically create namespace prefix?
                prefix = DEFAULT_PREFIX + (++NAMESPACE_COUNTER);
            }
            declareNamespace(prefix, namespace);
            xmlName = prefix + ':' + xmlName;
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
                Enumeration enum = null;
                if (value instanceof Enumeration) {
                    enum = (Enumeration)value;
                }
                else {
                    CollectionHandler colHandler = null;
                    try {
                        colHandler = CollectionHandlers.getHandler(value.getClass());
                    }
                    catch(MappingException mx) {
                        throw new MarshalException(mx);
                    }
                    enum = colHandler.elements(value);
                }
                if (enum.hasMoreElements()) {
                    StringBuffer sb = new StringBuffer();
                    for (int v = 0; enum.hasMoreElements(); v++) {
                        if (v > 0) sb.append(' ');
                        sb.append(getObjectID(enum.nextElement()).toString());
                    }
                    value = sb;
                }
                else value = null;
            }
            else {
                value = getObjectID(value);
            }
        }

        if (value != null) {
            //check if the value is a QName that needs to
            //be resolved ({URI}value -> ns:value).
            String valueType = attDescriptor.getSchemaType();
            if ((valueType != null) && (valueType.equals(QNAME_NAME)))
                    value = resolveQName(value, attDescriptor);

            atts.addAttribute(xmlName, CDATA, value.toString());
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
        (Object target, XMLClassDescriptor classDesc, AttributeListImpl atts)
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
        (Object target, XMLFieldDescriptor containerFieldDesc, AttributeListImpl atts)
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

} //-- Marshaller


