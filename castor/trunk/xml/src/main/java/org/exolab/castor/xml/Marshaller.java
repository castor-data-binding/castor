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

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Stack;
import java.util.StringTokenizer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.castor.core.util.Base64Encoder;
import org.castor.core.util.HexDecoder;
import org.castor.core.util.Messages;
import org.castor.mapping.BindingType;
import org.castor.mapping.MappingUnmarshaller;
import org.castor.xml.InternalContext;
import org.castor.xml.XMLProperties;
import org.exolab.castor.mapping.ClassDescriptor;
import org.exolab.castor.mapping.CollectionHandler;
import org.exolab.castor.mapping.FieldHandler;
import org.exolab.castor.mapping.MapHandler;
import org.exolab.castor.mapping.MapItem;
import org.exolab.castor.mapping.Mapping;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.mapping.MappingLoader;
import org.exolab.castor.mapping.handlers.MapHandlers;
import org.exolab.castor.mapping.loader.CollectionHandlers;
import org.exolab.castor.types.AnyNode;
import org.exolab.castor.util.SafeStack;
import org.exolab.castor.xml.descriptors.RootArrayDescriptor;
import org.exolab.castor.xml.descriptors.StringClassDescriptor;
import org.exolab.castor.xml.handlers.DateFieldHandler;
import org.exolab.castor.xml.handlers.EnumFieldHandler;
import org.exolab.castor.xml.util.AnyNode2SAX2;
import org.exolab.castor.xml.util.AttributeSetImpl;
import org.exolab.castor.xml.util.DocumentHandlerAdapter;
import org.exolab.castor.xml.util.SAX2DOMHandler;
import org.exolab.castor.xml.util.XMLClassDescriptorAdapter;
import org.exolab.castor.xml.util.XMLClassDescriptorImpl;
import org.exolab.castor.xml.util.XMLFieldDescriptorImpl;
import org.w3c.dom.Node;
import org.xml.sax.ContentHandler;
import org.xml.sax.DocumentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

/**
 * A Marshaller that serializes Java Object's to XML
 *
 * Note: This class is not thread safe, and not intended to be,
 * so please create a new Marshaller for each thread if it
 * is to be used in a multithreaded environment.
 *
 * @author <a href="mailto:keith AT kvisco DOT com">Keith Visco</a>
 * @version $Revision$ $Date: 2006-04-13 06:47:36 -0600 (Thu, 13 Apr 2006) $
 */
public class Marshaller extends MarshalFramework {


    //---------------------------/
    //- Private Class variables -/
    //---------------------------/

    /**
     * Logger from commons-logging.
     */
    private static final Log LOG = LogFactory.getLog(Marshaller.class);

    /**
     * The CDATA type..uses for SAX attributes.
    **/
    private static final String CDATA = "CDATA";

    /**
     * Default prefix for use when creating
     * namespace prefixes.
    **/
    private static final String DEFAULT_PREFIX = "ns";


    /**
     * Message name for a non sax capable serializer error.
    **/
    private static final String SERIALIZER_NOT_SAX_CAPABLE
        = "conf.serializerNotSaxCapable";

    /**
     * Namespace declaration for xml schema instance.
     */
    private static final String XSI_PREFIX = "xsi";

    /**
     * The xsi:type attribute.
     */
    private static final String XSI_TYPE = "xsi:type";

    /**
     * Namespace prefix counter.
     */
    private int _namespaceCounter = 0;

    /**
     * An instance of StringClassDescriptor.
     */
    private static final StringClassDescriptor STRING_CLASS_DESCRIPTOR
        = new StringClassDescriptor();

    //----------------------------/
    //- Private member variables -/
    //----------------------------/

    /**
     * A boolean to indicate whether or not we are
     * marshalling as a complete document or not.
    **/
    private boolean _asDocument = true;

    /**
     * The depth of the sub tree, 0 denotes document level.
     */
    private int _depth = 0;

    /**
     * The output format to use with the serializer.
     * This will be null if the user passed in their
     * own DocumentHandler.
    **/
    private OutputFormat _format = null;

    /**
     * The ContentHandler we are marshalling to.
    **/
    private ContentHandler  _handler      = null;

    /**
     * flag to indicate whether or not to use xsi:type.
    **/
    private boolean _marshalExtendedType = true;

    /**
     * The registered MarshalListener to receive
     * notifications of pre and post marshal for
     * each object in the tree being marshalled.
    **/
    private MarshalListener _marshalListener = null;

    /**
     * The namespace stack.
    **/
    private Namespaces _namespaces = new Namespaces();

    /**
     * Records Java packages being used during marshalling.
    **/
    private List<String> _packages = new ArrayList<String>();

    /**
     * A stack of parent objects...to prevent circular
     * references from being marshalled.
    **/
    private Stack _parents  = new SafeStack();

    /**
     * A list of ProcessingInstructions to output
     * upon marshalling of the document.
    **/
    private List<ProcessingInstruction> _processingInstructions = new ArrayList<ProcessingInstruction>();

    /**
     * Name of the root element to use.
     */
    private String _rootElement  = null;

    /**
     * A boolean to indicate keys from a map
     * should be saved when necessary.
     */
    private boolean _saveMapKeys = true;

    /**
     * The serializer that is being used for marshalling.
     * This may be null if the user passed in a DocumentHandler.
    **/
    private Serializer       _serializer   = null;

    /**
     * A flag to allow suppressing namespaces.
     */
    private boolean _suppressNamespaces = false;

    /**
     * A flag to allow suppressing the xsi:type attribute.
     */
    private boolean _suppressXSIType = false;

    private boolean _useXSITypeAtRoot = false;

    /**
     * The set of optional top-level attributes
     * set by the user.
    **/
    private AttributeSetImpl _topLevelAtts = new AttributeSetImpl();

    /**
     * The AttributeList which is to be used during marshalling,
     * instead of creating a bunch of new ones.
     */
    private AttributesImpl _attributes = new AttributesImpl();

    /**
     * The validation flag.
     */
    private boolean _validate = false;

    /** Set of full class names of proxy interfaces. If the class to be marshalled implements
     *  one of them the superclass will be marshalled instead of the class itself. */
    private final Set<String> _proxyInterfaces = new HashSet<String>();

    /**
     * Creates a new {@link Marshaller} with the given SAX {@link DocumentHandler}.
     *
     * @param handler the SAX {@link DocumentHandler} to "marshal" to.
     * 
     * @deprecate Please use {@link XMLContext#createMarshaller()} and 
     *    {@link Marshaller#setDocumentHandler(DocumentHandler)} instead
     * 
     * @see {@link XMLContext#createMarshaller()}
     * @see {@link Marshaller#setDocumentHandler(DocumentHandler)}
     * @see XMLContext
     * 
    **/
    public Marshaller(final DocumentHandler handler) {
        super(null);
        if (handler == null) {
            throw new IllegalArgumentException("The given 'org.sax.DocumentHandler' " 
                    + "instance is null.");
        }

        setContentHandler(new DocumentHandlerAdapter(handler));
    }

    /**
     * Sets the given SAX {@link DocumentHandler} to 'marshal' into.
     *
     * @param handler the SAX {@link DocumentHandler} to "marshal" to.
    **/
    public void setDocumentHandler(final DocumentHandler handler) {
        if (handler == null) {
            throw new IllegalArgumentException("The given 'org.sax.DocumentHandler' " 
                    + "instance is null.");
        }

        setContentHandler(new DocumentHandlerAdapter(handler));
    }

    /**
     * Creates a new {@link Marshaller} with the given SAX {@link ContentHandler}.
     *
     * @param contentHandler the {@link ContentHandler} to "marshal" to.
     * 
     * @deprecate Please use {@link XMLContext#createMarshaller()} and 
     *    {@link Marshaller#setContentHandler(ContentHandler)} instead
     * 
     * @see {@link XMLContext#createMarshaller()}
     * @see {@link Marshaller#setContentHandler(ContentHandler)}
     * @see XMLContext
     * 
    **/
    public Marshaller(final ContentHandler contentHandler) {
        super(null);
        if (contentHandler == null) {
            throw new IllegalArgumentException("The given 'org.sax.ContentHandler' is null.");
        }

        setContentHandler(contentHandler);
    }
    
    /**
     * The one {@link Marshaller} constructor that is used by {@link XMLContext} which
     * sets an {@link InternalContext} that comes from outside. Writer or {@link ContentHandler}
     * have to be set in a second step.
     * @param internalContext the {@link InternalContext} to initialize the {@link Marshaller}
     * instance with
     */
    public Marshaller(final InternalContext internalContext) {
        super(internalContext);
    }

    /**
     * Creates a default instance of Marshaller, where the sink needs to be set
     * separately.
     */
    public Marshaller () {
        super(null);
    }

    /**
     * Creates a new Marshaller with the given writer.
     * @param out the Writer to serialize to
     * @throws IOException If the given {@link Writer} instance cannot be opened.
     * @deprecate Please use {@link XMLContext#createMarshaller()} and 
     *    {@link Marshaller#setWriter(Writer)} instead
     * 
     * @see {@link XMLContext#createMarshaller()}
     * @see {@link Marshaller#setWriter(Writer)}
     * @see XMLContext
     * 
    **/
    public Marshaller(final Writer out) throws IOException {
        super(null);
        setWriter(out);
    }

    /**
     * Sets the java.io.Writer to be used during marshalling.
     * 
     * @param out
     *            The writer to use for marshalling
     * @throws IOException
     *             If there's a problem accessing the java.io.Writer provided
     */
    public void setWriter (final Writer out) throws IOException {
        if (out == null) {
            throw new IllegalArgumentException("The given 'java.io.Writer instance' is null.");
        }
        configureSerializer(out);
    }


    private void configureSerializer(Writer out) throws IOException
    {
        _serializer = getInternalContext().getSerializer();

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
    }

    /**
     * Creates a new {@link Marshaller} for the given DOM {@link Node}.
     *
     * @param node the DOM {@link Node} to marshal into.
     * 
     * @deprecate Please use {@link XMLContext#createMarshaller()} and 
     *    {@link Marshaller#setNode(Node)} instead
     * 
     * @see {@link XMLContext#createMarshaller()}
     * @see {@link Marshaller#setNode(Node)}
     * @see XMLContext
     * 
     * 
    **/
    public Marshaller(final Node node) {
        super(null);
        if (node == null) {
            throw new IllegalArgumentException("The given org.w3c.dom.Node instance is null.");
        }
        setContentHandler(new DocumentHandlerAdapter(new SAX2DOMHandler(node)));
    }
    
    /**
     * Sets the W3C {@link Node} instance to marshal to.
     *
     * @param node the DOM {@link Node} to marshal into.
    **/
    public void setNode(final Node node) {
        if (node == null) {
            throw new IllegalArgumentException("The given org.w3c.dom.Node instance is null.");
        }
        setContentHandler(new DocumentHandlerAdapter(new SAX2DOMHandler(node)));
    }
    
  /**
   * To set the {@link InternalContext} to use, and to 
   * initialize {@link Marshaller} properties linked to it.
   * @param internalContext the {@link InternalContext} to use
   */
  public void setInternalContext(final InternalContext internalContext) {
      super.setInternalContext(internalContext);
      deriveProperties();
  }


  /**
     * Derive class-level properties from {@link XMLProperties} as defined
     * {@link InternalContext}. This method will be called after a new
     * {@link InternalContext} or a property has been set.
     * 
     * @link #setInternalContext(InternalContext)
     */
    private void deriveProperties() {
        _validate = getInternalContext().marshallingValidation();
        _saveMapKeys = getInternalContext().getBooleanProperty(
                XMLProperties.SAVE_MAP_KEYS).booleanValue();

        String prop = getInternalContext().getStringProperty(
                XMLProperties.PROXY_INTERFACES);
        if (prop != null) {
            StringTokenizer tokenizer = new StringTokenizer(prop, ", ");
            while (tokenizer.hasMoreTokens()) {
                _proxyInterfaces.add(tokenizer.nextToken());
            }
        }
    }
    
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
                _format = getInternalContext().getOutputFormat();
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
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Error setting up document handler", iox);
                }
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
                _format = getInternalContext().getOutputFormat();
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
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Error setting up document handler", iox);
                }
            }
        }

    } //-- setMarshalAsDocument

    /**
     * Sets the given mapping to be used by the marshalling Framework. If a resolver
     * exists this mapping will be added to the existing ClassDescriptorResolver.
     * Otherwise a new ClassDescriptorResolver will be created.
     *
     * @param mapping Mapping to using during marshalling.
     */
    public void setMapping(final Mapping mapping) throws MappingException {
//        if (_cdResolver == null) {
//            _cdResolver = (XMLClassDescriptorResolver) ClassDescriptorResolverFactory
//                .createClassDescriptorResolver(BindingType.XML);
//        }
        if ((getInternalContext() == null) 
                || (getInternalContext().getXMLClassDescriptorResolver() == null)) {
            String message = "No internal context or no class descriptor in context.";
            LOG.warn(message);
            throw new IllegalStateException(message);
        }

        MappingUnmarshaller mum = new MappingUnmarshaller();
        MappingLoader resolver = mum.getMappingLoader(mapping, BindingType.XML);
        getInternalContext().getXMLClassDescriptorResolver().setMappingLoader(resolver);
    }

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
     * Sets the mapping for the given Namespace prefix.
     * 
     * @param nsPrefix the namespace prefix
     * @param nsURI the namespace that the prefix resolves to
    **/
    public void setNamespaceMapping(final String nsPrefix, final String nsURI) {

        if ((nsURI == null) || (nsURI.length() == 0)) {
            String err = "namespace URI must not be null.";
            throw new IllegalArgumentException(err);
        }

        _namespaces.addNamespace(nsPrefix, nsURI);

    } //-- setNamespacePrefix

    /**
     * Sets the name of the root element to use.
     *
     * @param rootElement The name of the root element to use.
     */
    public void setRootElement(final String rootElement) {
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
    public XMLClassDescriptorResolver getResolver() {

//        if (_cdResolver == null) {
//            _cdResolver = (XMLClassDescriptorResolver)
//            ClassDescriptorResolverFactory.createClassDescriptorResolver(BindingType.XML);
//        }
        if ((getInternalContext() == null) 
                || (getInternalContext().getXMLClassDescriptorResolver() == null)) {
            String message = "No internal context or no class descriptor in context.";
            LOG.warn(message);
            throw new IllegalStateException(message);
        }
        return getInternalContext().getXMLClassDescriptorResolver();

    } //-- getResolver

    /**
     * Sets the ClassDescriptorResolver to use during marshalling.
     *
     * <BR />
     * <B>Note:</B> This method will nullify any Mapping
     * currently being used by this Marshaller
     *
     * @param cdr the ClassDescriptorResolver to use
     * @see #setMapping
     * @see #getResolver
     */
    public void setResolver(final XMLClassDescriptorResolver cdr) {

        if (cdr != null) {
            getInternalContext().setXMLClassDescriptorResolver(cdr);
//            _cdResolver = cdr;
        }

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
    
    public boolean getValidation() {
        return _validate;
    }

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
     * Marshals the given Object as XML using the given writer.
     *
     * @param object The Object to marshal.
     * @param out The writer to marshal to.
     * @exception org.exolab.castor.xml.MarshalException
     * @exception org.exolab.castor.xml.ValidationException
     */
    public static void marshal(Object object, Writer out)
    throws MarshalException, ValidationException {
        try {
            staticMarshal(object, new Marshaller(out));
        } catch (IOException e) {
            throw new MarshalException(e);
        }
    } //-- marshal

    /**
     * Marshals the given Object as XML using the given DocumentHandler
     * to send events to.
     *
     * @param object The Object to marshal.
     * @param handler The DocumentHandler to marshal to.
     * @exception org.exolab.castor.xml.MarshalException
     * @exception org.exolab.castor.xml.ValidationException
     */
    public static void marshal(Object object, DocumentHandler handler)
    throws MarshalException, ValidationException {
        staticMarshal(object, new Marshaller(handler));
    } //-- marshal

    /**
     * Marshals the given Object as XML using the given ContentHandler
     * to send events to.
     *
     * @param object The Object to marshal.
     * @param handler The ContentHandler to marshal to.
     * @exception org.exolab.castor.xml.MarshalException
     * @exception org.exolab.castor.xml.ValidationException
     */
    public static void marshal(Object object, ContentHandler handler)
    throws MarshalException, ValidationException, IOException {
        staticMarshal(object, new Marshaller(handler));
    } //-- marshal

    /**
     * Marshals the given Object as XML using the given DOM Node
     * to send events to.
     *
     * @param object The Object to marshal.
     * @param node The DOM Node to marshal to.
     * @exception org.exolab.castor.xml.MarshalException
     * @exception org.exolab.castor.xml.ValidationException
     */
    public static void marshal(Object object, Node node)
    throws MarshalException, ValidationException {
        staticMarshal(object, new Marshaller(node));
    } //-- marshal

    /**
     * Static helper method to marshal the given object using the
     * Marshaller instance provided.
     *
     * @param object The Object to marshal.
     * @param marshaller The {@link Marshaller} to use for marshalling.
     * @throws MarshalException as thrown by marshal(Object)
     * @throws ValidationException as thrown by marshal(Object)
     */
    private static void staticMarshal(final Object object, final Marshaller marshaller)
    throws MarshalException, ValidationException {
        if (object == null) {
            throw new MarshalException("object must not be null");
        }

        if (LOG.isInfoEnabled()) {
            LOG.info("Marshaller called using one of the *static* " + 
                    " marshal(Object, *) methods. This will ignore any " +
                    " mapping files as specified. Please consider switching to " +
                    " using Marshaller instances and calling one of the" +
                    " marshal(*) methods.");
        }

        marshaller.marshal(object);
    } //-- staticMarshal

    /**
     * Marshals the given Object as XML using the DocumentHandler
     * for this Marshaller.
     *
     * @param object The Object to marshal.
     * @exception org.exolab.castor.xml.MarshalException
     * @exception org.exolab.castor.xml.ValidationException
     */
    public void marshal(Object object)
    throws MarshalException, ValidationException {
        if (object == null)
            throw new MarshalException("object must not be null");

        if (LOG.isDebugEnabled()) {
            LOG.debug("Marshalling " + object.getClass().getName());
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
                        ProcessingInstruction pi = _processingInstructions.get(i);
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
            boolean toBeMarshalled = true;
            try {
                toBeMarshalled = _marshalListener.preMarshal(object);
            } catch (RuntimeException e) {
                LOG.error("Invoking #preMarshal() on your custom MarshalListener instance caused the following problem:", e);
            }
            if (!toBeMarshalled) {
                return;
            }
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
        if (_parents.search(object) >= 0) {
            return;
        }
        _parents.push(object);

        final boolean isNil = (object instanceof NilObject);

        Class<?> cls = null;

        if (!isNil) {
            cls = object.getClass();
            
            if (_proxyInterfaces.size() > 0) {
                boolean isProxy = false;
                
                Class<?>[] interfaces = cls.getInterfaces();
                for (int i = 0; i < interfaces.length; i++) {
                    if (_proxyInterfaces.contains(interfaces[i].getName())) { 
                        isProxy = true; 
                    }
                }
                
                if (isProxy) { cls = cls.getSuperclass(); }
            }
        } else {
            cls = ((NilObject) object).getClassDescriptor().getJavaClass();
        }

        boolean byteArray = false;
        if (cls.isArray())
            byteArray = (cls.getComponentType() == Byte.TYPE);

        boolean atRoot = false;
        if (descriptor == null) {
            descriptor = new XMLFieldDescriptorImpl(cls, "root", null, null);
            atRoot = true;
        }


        //-- calculate Object's name
        String name = descriptor.getXMLName();
        if (atRoot && _rootElement!=null)
            name = _rootElement;

        boolean autoNameByClass = false;
        if (name == null) {
            autoNameByClass = true;
            name = cls.getName();
            //-- remove package information from name
            int idx = name.lastIndexOf('.');
            if (idx >= 0) {
                name = name.substring(idx+1);
            }
            //-- remove capitalization
            name = getInternalContext().getXMLNaming().toXMLName(name);
        }

        //-- obtain the class descriptor
        XMLClassDescriptor classDesc = null;
        boolean saveType = false; /* flag for xsi:type */

        if (object instanceof NilObject) {
            classDesc = ((NilObject)object).getClassDescriptor();
        } else if (cls == descriptor.getFieldType()) {
            classDesc = (XMLClassDescriptor)descriptor.getClassDescriptor();
        }

        if (classDesc == null) {

            //-- check for primitive or String, we need to use
            //-- the special #isPrimitive method of this class
            //-- so that we can check for the primitive wrapper
            //-- classes
            if (isPrimitive(cls) || byteArray) {
                classDesc = STRING_CLASS_DESCRIPTOR;
                //-- check to see if we need to save the xsi:type
                //-- for this class
                Class<?> fieldType = descriptor.getFieldType();
                if (cls != fieldType) {
                    while (fieldType.isArray()) {
                        fieldType = fieldType.getComponentType();
                    }
                    saveType = (!primitiveOrWrapperEquals(cls, fieldType));
                }
            }
            else {
                saveType = cls.isArray();
                //-- save package information for use when searching
                //-- for MarshalInfo classes
                String className = cls.getName();
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

                    if ((cls != descriptor.getFieldType()) || atRoot) {

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
                                tmpDesc = getResolver().resolveByXMLName(name, nsURI, null);
                            }
                            catch(ResolverException rx) {
                                //-- exception not important as we're simply
                                //-- testing to see if we can resolve during
                                //-- unmarshalling
                                if (LOG.isDebugEnabled()) {
                                    LOG.debug("Error resolving", rx);
                                }
                            }

                            if (tmpDesc != null) {
                                Class<?> tmpType = tmpDesc.getJavaClass();
                                if (tmpType == cls) {
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
                                    XMLMappingLoader ml = (XMLMappingLoader) getResolver().getMappingLoader();
                                    if (ml != null) {
                                        containsDesc = (ml.getDescriptor(cls.getName()) != null);
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
                                classDesc = getClassDescriptor(cls);
                                if (classDesc != null) {
                                    String tmpName = classDesc.getXMLName();
                                    if (name.equals(tmpName))
                                        saveType = false;
                                }
                            }
                        }

                        if (containsDesc) saveType = false;

                    }

                    //  marshal as the actual type
                    if (classDesc == null)
                        classDesc = getClassDescriptor(cls);

                } //-- end if (marshalExtendedType)
                else {
                    // marshall as the base field type
                    cls = descriptor.getFieldType();
                    classDesc = getClassDescriptor(cls);
                }

                //-- If we are marshalling an array as the top
                //-- level object, or if we run into a multi
                //-- dimensional array, use the special
                //-- ArrayDescriptor
                if ((classDesc == null) && cls.isArray()) {
                    classDesc = new RootArrayDescriptor(cls);
                    if (atRoot) {
                        containerField = (!_asDocument);
                    }
                }
            } //-- end else not primitive

            if (classDesc == null) {
                //-- make sure we are allowed to marshal Object
                if ((cls == Void.class) ||
                    (cls == Object.class) ||
                    (cls == Class.class)) {

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
                 xmlElementNameClassDesc = getResolver().resolveByXMLName(xmlElementName, null, null);
             }
             catch(ResolverException rx) {
                 //-- exception not important as we're simply
                 //-- testing to see if we can resolve during
                 //-- unmarshalling
                 if (LOG.isDebugEnabled()) {
                    LOG.debug("Error resolving " + xmlElementName, rx);
                }
             }

             // Test if we are not dealing with a source generated vector
             if ((xmlElementName != null) && (xmlElementNameClassDesc != null)) {
                 // More than one class can map to a given element name
                 try {
                     Iterator<ClassDescriptor> classDescriptorIter = getResolver().resolveAllByXMLName(xmlElementName, null, null);
                     for (; classDescriptorIter.hasNext();) {
                         xmlElementNameClassDesc = (XMLClassDescriptor) classDescriptorIter.next();
                         if (cls == xmlElementNameClassDesc.getJavaClass()) {
                            break;
                         }
                         //reset the classDescriptor --> none has been found
                         xmlElementNameClassDesc = null;
                     }
                 }
                 catch(ResolverException rx) {
                     if (LOG.isDebugEnabled()) {
                         LOG.debug("Error resolving " + xmlElementName, rx);
                     }
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
                              searchInheritance(xmlElementName, null, tempContaining); // TODO: Joachim, _cdResolver);

                        if (matches.length == 1) {

                            boolean foundTheRightClass = ((xmlElementNameClassDesc != null) && (cls == xmlElementNameClassDesc.getJavaClass()));

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
                nsPrefix = _namespaces.getNamespacePrefix(nsURI);
            }
            //-- declare namespace at this element scope?
            if (nsURI != null) {
                String defaultNamespace = _namespaces.getNamespaceURI("");
                if ((nsPrefix == null) && (!nsURI.equals(defaultNamespace)))
                {
                    if ((defaultNamespace == null) && atRoot) {
                        nsPrefix = "";
                    }
                    else nsPrefix = DEFAULT_PREFIX + (++_namespaceCounter);
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
            XMLFieldDescriptor attributeDescriptor = descriptors[i];
            if (attributeDescriptor == null) {
                continue;
            }
            String path = attributeDescriptor.getLocationPath();
            if ((path != null) && (path.length() > 0)) {
                //-- save for later processing
                if (nestedAtts == null) {
                    nestedAtts = new XMLFieldDescriptor[descriptors.length - i];
                }
                nestedAtts[nestedAttCount] = attributeDescriptor;
                nestedAttCount++;
            } else {
                processAttribute(object, attributeDescriptor, atts);
            }
        }

        //-- handle ancestor nested attributes
        if (mstate._nestedAttCount > 0) {
            for (int i = 0; i < mstate._nestedAtts.length; i++) {
                XMLFieldDescriptor attributeDescriptor = mstate._nestedAtts[i];
                if (attributeDescriptor == null) {
                    continue;
                }
                String locationPath = attributeDescriptor.getLocationPath();
                if (name.equals(locationPath)) {
                    // indicate that this 'nested' attribute has been processed
                    mstate._nestedAtts[i] = null;
                    // decrease number of unprocessed 'nested' attributes by one
                    mstate._nestedAttCount--;
                    processAttribute(mstate.getOwner(), attributeDescriptor, atts);
                }
            }
        }

        //-- Look for attributes in container fields,
        //-- (also handle container in container)
        if (!isNil) processContainerAttributes(object, classDesc, atts);

        //-- xml:space
        String attValue = descriptor.getXMLProperty(XMLFieldDescriptor.PROPERTY_XML_SPACE);
        if (attValue != null) {
            atts.addAttribute(Namespaces.XML_NAMESPACE, SPACE_ATTR, XML_SPACE_ATTR, CDATA, attValue);
        }

        //-- xml:lang
        attValue = descriptor.getXMLProperty(XMLFieldDescriptor.PROPERTY_XML_LANG);
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

            boolean useJavaPrefix = false;
            if ((typeName == null) || introspected) {
                typeName = JAVA_PREFIX + cls.getName();
                useJavaPrefix  = true;
            }
            else if (classDesc instanceof RootArrayDescriptor) {
                typeName = JAVA_PREFIX + cls.getName();
                useJavaPrefix = true;
            }
            else {
                String dcn = classDesc.getClass().getName();
                if (dcn.equals(XMLClassDescriptorImpl.class.getName())) {
                    typeName = JAVA_PREFIX + cls.getName();
                    useJavaPrefix = true;
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
            if (useJavaPrefix) {
                //-- declare Java namespace, if necessary
                declareNamespace("java", "http://java.sun.com");
            }
        }

        if (isNil && !_suppressXSIType) {
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
                        cls.getName() + ", please report bug to: " +
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

        Stack<WrapperInfo> wrappers = null;


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
                catch(IllegalStateException ise) {
                    LOG.warn("Error getting value from: " + object, ise);
                }

                if (obj != null) {

                    //-- <Wrapper>
                    //-- handle XML path
                    String path = cdesc.getLocationPath();
                    String currentLoc = null;

                    if (path != null) {
                        _attributes.clear();
                        if (wrappers == null) {
                            wrappers  = new SafeStack<WrapperInfo>();
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
                    Class<?> objType = obj.getClass();
                    if (objType.isArray() && (objType.getComponentType() == Byte.TYPE)) {
                        //-- handle base64/hexbinary content
                        final String schemaType = descriptor.getSchemaType();
                        if (HexDecoder.DATA_TYPE.equals(schemaType)) {
                            chars = new String(HexDecoder.encode((byte[]) obj)).toCharArray();
                        } else {
                            chars = Base64Encoder.encode((byte[]) obj);
                        }
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
                //-- Base64Encoding / HexBinary
                String schemaType = descriptor.getSchemaType();
                String componentType = descriptor.getComponentType();
                char[] chars = new char[0];
                if ((descriptor.isMultivalued() && HexDecoder.DATA_TYPE.equals(componentType)) || 
                        HexDecoder.DATA_TYPE.equals(schemaType)) {
                    chars = new String(HexDecoder.encode((byte[]) object)).toCharArray();
                } else {
                    chars = Base64Encoder.encode((byte[]) object);
                }
                try {
                    handler.characters(chars, 0, chars.length);
                }  catch (org.xml.sax.SAXException sx) {
                    throw new MarshalException(sx);
                }
            }
            /* special case for Strings and primitives */
            else if (isPrimitive(cls)) {
                
                char[] chars;
                if (cls == java.math.BigDecimal.class) {
                    chars = convertBigDecimalToString(object).toCharArray();
                } else {
                    chars = object.toString().toCharArray();
                }
                try {
                    handler.characters(chars,0,chars.length);
                }
                catch(org.xml.sax.SAXException sx) {
                    throw new MarshalException(sx);
                }
            }
            else if (isEnum(cls)) {
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

        ++_depth;

        //-- marshal elements
        for (int i = firstNonNullIdx; i < descriptors.length; i++) {

            XMLFieldDescriptor elemDescriptor = descriptors[i];
            Object obj = null;
            boolean nil = false;

            //-- used previously cached value?
            if ((i == firstNonNullIdx) && (firstNonNullValue != null)) {
                obj = firstNonNullValue;
            } else {
                //-- obtain value from handler
                try {
                    obj = elemDescriptor.getHandler().getValue(object);
                } catch (IllegalStateException ise) {
                    LOG.warn("Error marshalling " + object, ise);
                    continue;
                }
            }
            
            if (obj == null 
                    || (obj instanceof Enumeration && !((Enumeration) obj).hasMoreElements())) {
                             if (elemDescriptor.isNillable() && (elemDescriptor.isRequired())) {
                    nil = true;
                } else {
                    continue;
                }
            }


            //-- handle XML path
            String path = elemDescriptor.getLocationPath();
            String currentLoc = null;
            //-- Wrapper/Location cleanup
            if (wrappers != null) {
                try {
                    while (!wrappers.empty()) {
                        WrapperInfo wInfo = wrappers.peek();
                        if (path != null) {
                            if (wInfo._location.equals(path)) {
                                path = null;
                                break;
                            }
                            else if (path.startsWith(wInfo._location + "/")) {
                                path = path.substring(wInfo._location.length()+1);
                                currentLoc = wInfo._location;
                                break;
                            }
                        }
                        handler.endElement(nsURI, wInfo._localName, wInfo._qName);
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
                    wrappers  = new SafeStack<WrapperInfo>();
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

            final Class<?> type = obj.getClass();

            MarshalState myState = mstate.createMarshalState(object, name);
            myState._nestedAtts = nestedAtts;
            myState._nestedAttCount = nestedAttCount;


            //-- handle byte arrays
            if (type.isArray() && (type.getComponentType() == Byte.TYPE)) {
                marshal(obj, elemDescriptor, handler, myState);
            } else if (type.isArray() && elemDescriptor.isDerivedFromXSList()) {
                Object buffer = processXSListType(obj, elemDescriptor);
                String elemName = elemDescriptor.getXMLName();
                String elemQName = elemName;
                if ((nsPrefix != null) && (nsPrefix.length() > 0)) {
                    elemQName = nsPrefix + ':' + elemName;
                }
                char[] chars = buffer.toString().toCharArray();
                try {
                    handler.startElement(nsURI, elemName, elemQName, _attributes);
                    handler.characters(chars,0,chars.length);
                    handler.endElement(nsURI, elemName, elemQName);
                }
                catch(org.xml.sax.SAXException sx) {
                    throw new MarshalException(sx);
                }
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
            else {
                marshal(obj, elemDescriptor, handler, myState);
            }

            if (nestedAttCount > 0) {
                nestedAttCount = myState._nestedAttCount;
            }

        }


        //-- Wrapper/Location cleanup for elements
        if (wrappers != null) {
            try {
                while (!wrappers.empty()) {
                    WrapperInfo wInfo = wrappers.peek();
                    boolean popStack = true;
                    if (nestedAttCount > 0) {
                        for (int na = 0; na < nestedAtts.length; na++) {
                            // TODO[LL]: refactor to avoid check against null
                            if (nestedAtts[na] == null) continue;
                            String nestedAttributePath = nestedAtts[na].getLocationPath();
                            if (nestedAttributePath.startsWith(wInfo._location + "/")) {
                                popStack = false;
                                break;
                            }
                        }
                    }
                    if (popStack) {
                        handler.endElement(nsURI, wInfo._localName, wInfo._qName);
                        wrappers.pop();
                    } else {
                        break;
                    }
                }
            }
            catch(SAXException sx) {
                throw new MarshalException(sx);
            }
        }

        // TODO: Handling additional attributes at the end causes elements to be marshalled in the wrong
        // order when element 'text' is null, but their attribute value is not null. Can this be fixed
        // to process element attributes even when the element text value is null?

        if (wrappers != null && !wrappers.isEmpty()) {
            dealWithNestedAttributesNested(object, handler, nsPrefix, nsURI,
                    nestedAttCount, nestedAtts, wrappers);
        }

        dealWithNestedAttributes(object, handler, nsPrefix, nsURI,
                nestedAttCount, nestedAtts, new SafeStack<WrapperInfo>());

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

        --_depth;
        _parents.pop();
        if (!atRoot) _namespaces = _namespaces.getParent();

        //-- notify listener of post marshal
        if (_marshalListener != null) {
            try {
            _marshalListener.postMarshal(object);
            } catch (RuntimeException e) {
                LOG.error("Invoking #postMarshal() on your custom MarshalListener instance caused the following problem:", e);
            }
        }

    } //-- void marshal(DocumentHandler)

    private void dealWithNestedAttributes(Object object,
            ContentHandler handler, String nsPrefix, String nsURI,
            int nestedAttCount, XMLFieldDescriptor[] nestedAtts, Stack<WrapperInfo> wrappers)
            throws MarshalException {
        //-- Handle any additional attribute locations that were
        //-- not handled when dealing with wrapper elements
        if (nestedAttCount > 0) {
            for (int i = 0; i < nestedAtts.length; i++) {
                if (nestedAtts[i] == null) continue;
                String path = nestedAtts[i].getLocationPath();
                String currentLoc = null;
                
                
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
                        WrapperInfo wInfo = wrappers.pop();
                        handler.endElement(nsURI, wInfo._localName, wInfo._qName);
                    }
                } catch (Exception e) {
                    throw new MarshalException(e);
                }
            }
        } // if (nestedAttCount > 0)
    }

    private void dealWithNestedAttributesNested(Object object,
            ContentHandler handler, String nsPrefix, String nsURI,
            int nestedAttCount, XMLFieldDescriptor[] nestedAtts, Stack<WrapperInfo> wrappers)
            throws MarshalException {
        //-- Handle any additional attribute locations that were
        //-- not handled when dealing with wrapper elements
        
        WrapperInfo wrapperInfo = wrappers.peek();
        String currentLocation = wrapperInfo._location;
        
        if (nestedAttCount > 0) {
            for (int i = 0; i < nestedAtts.length; i++) {
                if (nestedAtts[i] == null) continue;
                String nestedAttributePath = nestedAtts[i].getLocationPath();
                
                if (!nestedAttributePath.startsWith(currentLocation + "/")) {
                    continue;
                }
                
                nestedAttributePath = nestedAttributePath.substring(wrapperInfo._location.length() + 1);
                String currentLoc = currentLocation;
                
                
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
                try {
                    while (nestedAttributePath != null) {
                        int idx = nestedAttributePath.indexOf('/');
                        String elemName = null;
                        if (idx > 0) {
                            elemName = nestedAttributePath.substring(0,idx);
                            nestedAttributePath = nestedAttributePath.substring(idx+1);
                        }
                        else {
                            elemName = nestedAttributePath;
                            nestedAttributePath = null;
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
                        if (nestedAttributePath == null) {
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
                        WrapperInfo wInfo = wrappers.pop();
                        handler.endElement(nsURI, wInfo._localName, wInfo._qName);
                    }
                } catch (Exception e) {
                    throw new MarshalException(e);
                }
            }
        } // if (nestedAttCount > 0)
    }


    /**
     * Converts a {@link BigDecimal} instance value to its String representation. This
     * method will take into into account the Java version number, as the semantics of
     * BigDecimal.toString() have changed between Java 1.4 and Java 5.0 and above.
     * @param object The {@link BigDecimal} instance
     * @return The String representation of the {@link BigDecimal} instance
     * @throws MarshalException If invocation of BigDecimal#toPlainString() fails.
     */
    private String convertBigDecimalToString(Object object) throws MarshalException {
        String stringValue;
        float javaVersion = Float.parseFloat(System.getProperty("java.specification.version"));
        if (javaVersion >= 1.5) {
            // as of Java 5.0 and above, BigDecimal.toPlainString() should be used.
            // TODO: reconsider if we start using BigDecimal for XSTypes that can hold scientific values
            Method method;
            try {
                method = java.math.BigDecimal.class.getMethod("toPlainString", (Class[]) null);
                stringValue = (String) method.invoke(object, (Object[]) null);
            } catch (Exception e) {
                LOG.error("Problem accessing java.math.BigDecimal.toPlainString().", e);
                throw new MarshalException("Problem accessing java.math.BigDecimal.toPlainString().", e);
            }
        } else {
            // use BigDecimal.toString() with Java 1.4 and below
            stringValue = object.toString();
        }
        return stringValue;
    }

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
     * Sets the PrintWriter used for logging
     * @param printWriter the PrintWriter to use for logging
    **/
    public void setLogWriter(final PrintWriter printWriter) { }

    /**
     * Sets the encoding for the serializer. Note that this method
     * cannot be called if you've passed in your own DocumentHandler.
     *
     * @param encoding the encoding to set
    **/
    public void setEncoding(String encoding) {

        if (_serializer != null) {
            if (_format == null) {
                _format = getInternalContext().getOutputFormat();
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
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Error setting encoding to " + encoding, iox);
                }
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
     * @param cls the Class to get the XMLClassDescriptor for
     * @exception MarshalException when there is a problem
     * retrieving or creating the XMLClassDescriptor for the given class
    **/
    private XMLClassDescriptor getClassDescriptor(final Class<?> cls)
    throws MarshalException {
        XMLClassDescriptor classDesc = null;

        try {
            if (!isPrimitive(cls))
                classDesc = (XMLClassDescriptor) getResolver().resolve(cls);
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
                    prefix = DEFAULT_PREFIX + (++_namespaceCounter);
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
            LOG.warn("Error getting value from " + object, ise);
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
        else if (attDescriptor.isMultivalued() && (value != null)) {
            value = processXSListType(value, attDescriptor);
        }
        else if (value != null) {
            //-- handle hex/base64 content
            Class<?> objType = value.getClass();
            if (objType.isArray() && (objType.getComponentType() == Byte.TYPE)) {
                value = encodeBinaryData(value, attDescriptor.getSchemaType());
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


    private Object processXSListType(final Object value, XMLFieldDescriptor descriptor) 
    throws MarshalException {
        Object returnValue = null;
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
                if (v > 0) {
                    sb.append(' ');
                }
                Object collectionValue = enumeration.nextElement();
                //-- handle hex/base64 content
                Class<?> objType = collectionValue.getClass();
                if (objType.isArray() && (objType.getComponentType() == Byte.TYPE)) {
                    collectionValue = encodeBinaryData(collectionValue, descriptor.getComponentType());
                }
                
                sb.append(collectionValue.toString());
            }
            returnValue = sb;
        }
        
        return returnValue;
    }


    /**
     * Encode binary data.
     * @param valueToEncode The binary data to encode.
     * @param componentType The XML schema component type.
     * @return Encoded binary data in {@link String} form.
     */
    private Object encodeBinaryData(final Object valueToEncode,
            final String componentType) {
        String encodedValue;
        if (HexDecoder.DATA_TYPE.equals(componentType)) {
            encodedValue = HexDecoder.encode((byte[]) valueToEncode);
        } else {
            encodedValue = new String(Base64Encoder.encode((byte[]) valueToEncode));
        }
        return encodedValue;
    }


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
     * Processes the attributes for container objects.
     *
     * @param target the object currently being marshalled.
     * @param containerFieldDesc the XMLFieldDescriptor for the containter to process
     * @param atts the SAX attributes list to add any necessary attributes to.
     * @throws MarshalException If there's a problem marshalling the attribute(s).
     */
    private void processContainerAttributes
        (final Object target, final XMLFieldDescriptor containerFieldDesc, 
                final AttributesImpl atts)
        throws MarshalException {
        if (target.getClass().isArray()) {
             int length = Array.getLength(target);
             for (int j = 0; j < length; j++) {
                  Object item = Array.get(target, j);
                  if (item != null) {
                    processContainerAttributes(item, containerFieldDesc, atts);
                }
             }
             return;
        } else if (target instanceof Enumeration) {
            Enumeration enumeration = (Enumeration) target;
            while (enumeration.hasMoreElements()) {
                Object item = enumeration.nextElement();
                if (item != null) {
                    processContainerAttributes(item, containerFieldDesc, atts);
                }
            }
            return;
        }

        Object containerObject = containerFieldDesc.getHandler().getValue(target);

        if (containerObject == null) {
            return;
        }

        XMLClassDescriptor containerClassDesc
            = (XMLClassDescriptor) containerFieldDesc.getClassDescriptor();

        if (containerClassDesc == null) {
            containerClassDesc = getClassDescriptor(containerFieldDesc.getFieldType());
            if (containerClassDesc == null) {
                return;
            }
        }

        // Look for attributes
        XMLFieldDescriptor[] attrDescriptors = containerClassDesc.getAttributeDescriptors();
        for (int idx = 0; idx < attrDescriptors.length; idx++) {
            if (attrDescriptors[idx] == null) {
                continue;
            }
            if (attrDescriptors[idx].getLocationPath() == null 
                    || attrDescriptors[idx].getLocationPath().length() == 0) {
                processAttribute(containerObject, attrDescriptors[idx], atts);
            }
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
            prefix = DEFAULT_PREFIX+(++_namespaceCounter);
        result = (prefix.length() != 0)?prefix+":"+result.substring(idx+1):result.substring(idx+1);
        declareNamespace(prefix, nsURI);
        return result;
    }

    private void validate(final Object object) throws ValidationException {
        if  (_validate) {
            //-- we must have a valid element before marshalling
            Validator validator = new Validator();
            ValidationContext context = new ValidationContext();
            context.setInternalContext(getInternalContext());
//            context.setConfiguration(_config);
//            context.setResolver(_cdResolver);
            validator.validate(object, context);
        }
    }

    /**
     * Returns the value of the given Castor XML-specific property.
     * @param name Qualified name of the CASTOR XML-specific property.
     * @return The current value of the given property.
     * @since 1.1.2
    */
    public String getProperty(final String name) {
        return getInternalContext().getStringProperty(name);
    }

    /**
     * Sets a custom value of a given Castor XML-specific property.
     * @param name Name of the Castor XML property 
     * @param value Custom value to set.
     * @since 1.1.2
     */
    public void setProperty(final String name, final String value) {
        getInternalContext().setProperty(name, value);
        deriveProperties();
    }
    
    /**
     * Inner-class used for handling wrapper elements
     * and locations.
     */
    static class WrapperInfo {
        private String _localName = null;
        private String _qName = null;
        private String _location = null;

        WrapperInfo(final String localName, final String qName, final String location) {
            _localName = localName;
            _qName = qName;
            _location = location;
        }
    }


    static class MarshalState {
        private String _xpath = null;
        private XMLFieldDescriptor[] _nestedAtts = null;
        private int _nestedAttCount = 0;
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
            if (_xpath == null) {
                if (_parent != null) {
                    _xpath = _parent.getXPath() + "/" + _xmlName;
                }
                else {
                    _xpath = _xmlName;
                }
            }
            return _xpath;
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

    /**
     * To set the SAX {@link ContentHandler} which is used as destination at marshalling.
     * @param contentHandler the SAX {@link ContentHandler} to use as destination at marshalling
     */
    public void setContentHandler(final ContentHandler contentHandler) {
        _handler = contentHandler;
    }
} //-- Marshaller


