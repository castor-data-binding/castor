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
 * Portions of this file developed by Keith Visco after Jan 19 2005 are
 * Copyright (C) Keith Visco. All Rights Reserverd.
 *
 * $Id$
 */
package org.exolab.castor.xml;

import java.io.PrintWriter;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.StringTokenizer;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.castor.core.util.Base64Decoder;
import org.castor.core.util.HexDecoder;
import org.castor.xml.InternalContext;
import org.exolab.castor.mapping.ClassDescriptor;
import org.exolab.castor.mapping.FieldHandler;
import org.exolab.castor.mapping.MapItem;
import org.exolab.castor.util.DefaultObjectFactory;
import org.exolab.castor.util.ObjectFactory;
import org.exolab.castor.xml.descriptors.PrimitivesClassDescriptor;
import org.exolab.castor.xml.descriptors.StringClassDescriptor;
import org.exolab.castor.xml.parsing.AnyNodeUnmarshalHandler;
import org.exolab.castor.xml.parsing.AttributeSetBuilder;
import org.exolab.castor.xml.parsing.NamespaceHandling;
import org.exolab.castor.xml.parsing.StrictElementHandler;
import org.exolab.castor.xml.parsing.UnmarshalListenerDelegate;
import org.exolab.castor.xml.parsing.UnmarshalStateStack;
import org.exolab.castor.xml.parsing.primitive.objects.PrimitiveObjectFactory;
import org.exolab.castor.xml.util.XMLFieldDescriptorImpl;
import org.xml.sax.AttributeList;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.DocumentHandler;
import org.xml.sax.ErrorHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * An unmarshaller to allowing unmarshaling of XML documents to
 * Java Objects. The Class must specify
 * the proper access methods (setters/getters) in order for instances
 * of the Class to be properly unmarshaled.
 * 
 * @author <a href="mailto:keith AT kvisco DOT com">Keith Visco</a>
 * @version $Revision$ $Date: 2006-05-25 06:41:12 -0600 (Thu, 25 May 2006) $
 */
public final class UnmarshalHandler extends MarshalFramework
implements ContentHandler, DocumentHandler, ErrorHandler {
    /**
     * Logger from commons-logging.
     */
    static final Log LOG = LogFactory.getLog(UnmarshalHandler.class);

    /** resource bundle */
    protected static ResourceBundle resourceBundle;
    
    static {
        resourceBundle = ResourceBundle.getBundle("UnmarshalHandlerMessages", Locale
                .getDefault());
    }
    
    //---------------------------/
    //- Private Class Variables -/
    //---------------------------/

    /**
     * The error message when no class descriptor has been found
     * TODO: move to resource bundle
     */
    private static final String ERROR_DID_NOT_FIND_CLASSDESCRIPTOR =
        "unable to find or create a ClassDescriptor for class: ";

    /**
     * The built-in XML prefix used for xml:space, xml:lang
     * and, as the XML 1.0 Namespaces document specifies, are
     * reserved for use by XML and XML related specs.
    **/
    private static final String XML_PREFIX = "xml";

    /**
     * Attribute name for default namespace declaration
    **/
    private static final String   XMLNS             = "xmlns";

    /**
     * Attribute prefix for prefixed namespace declaration.
     **/
    private final static String XMLNS_PREFIX = "xmlns:";

    /**
     * The type attribute (xsi:type) used to denote the
     * XML Schema type of the parent element
    **/
    private static final String XSI_TYPE = "type";
    
    static final String XML_SPACE = "space";
    static final String XML_SPACE_WITH_PREFIX = "xml:space";
    static final String PRESERVE = "preserve";

    //----------------------------/
    //- Private Member Variables -/
    //----------------------------/

    private UnmarshalState   _topState     = null;
    private Class<?>         _topClass     = null;

    /**
     * The top-level instance object, this may be set by the user
     * by calling #setRootObject();.
    **/
    private Object           _topObject    = null;

    /**
     * Indicates whether or not collections should be cleared
     * upon first use (to remove default values, or old values).
     * False by default for backward compatibility.
     */
    private boolean          _clearCollections = false;

    /**
     * The SAX Document Locator.
    **/
    private Locator          _locator      = null;

    /**
     * The IDResolver for resolving IDReferences.
    **/
    private IDResolver _idResolver = null;

    /**
     * A flag indicating whether or not to perform validation.
     **/
    private boolean _validate = true;

    /**
     * Hashtable to store idReference and ReferenceInfo
     */
    private Hashtable<String, ReferenceInfo> _resolveTable = new Hashtable<String, ReferenceInfo>();
    
    private Map<Class<?>, String> _javaPackages = null;    

    private ClassLoader _loader = null;

    private static final StringClassDescriptor STRING_DESCRIPTOR
        = new StringClassDescriptor();

    /**
     * The AnyNode to add (if any).
     */
    private org.exolab.castor.types.AnyNode _node = null;

    /**
     * A reference to the ObjectFactory used to create instances
     * of the classes if the FieldHandler is not used.
     */
    private ObjectFactory _objectFactory = new DefaultObjectFactory();
    
    /**
     * A boolean to indicate that objects should
     * be re-used where appropriate.
    **/
    private boolean _reuseObjects = false;

    /**
     * A boolean that indicates attribute processing should
     * be strict and an error should be flagged if any
     * extra attributes exist.
    **/
    private boolean _strictAttributes = false;
    
    /**
     * The top-level xml:space value.
     */
    private boolean _wsPreserve = false;
    
    /**
     * {@link StrictElementHandler} that deals with (potentially) ignorable content.
     */
    private StrictElementHandler _strictElementHandler = new StrictElementHandler();
    
    /**
     * {@link UnmarshalStateStack} that saves UnmarshalStates on a stack.
     */
    private UnmarshalStateStack _stateStack = new UnmarshalStateStack();
    
    /**
     * {@link UnmarshalListenerDelegate} that deals with UnmarshalListener calls.
     */
    private UnmarshalListenerDelegate _delegateUnmarshalListener = new UnmarshalListenerDelegate();
    
    private AnyNodeUnmarshalHandler _anyNodeHandler = null;
    
    private NamespaceHandling _namespaceHandling = new NamespaceHandling();

    private AttributeSetBuilder _attributeSetFactory = null;
    
    //----------------/
    //- Constructors -/
    //----------------/

    /**
     * Creates a new UnmarshalHandler
     * The "root" class will be obtained by looking into the mapping
     * for a descriptor that matches the root element.
    **/
    protected UnmarshalHandler() {
        this(null);
    }

    /**
     * Creates a new UnmarshalHandler.
     * 
     * @param topClass the Class to create the UnmarshalHandler for
     */
    protected UnmarshalHandler(final Class<?> topClass) {
        this(null, topClass);
    }
    
    /**
     * Creates a new UnmarshalHandler.
     * @param internalContext the {@link InternalContext} to use
     * @param topClass the Class to work for
     */
    protected UnmarshalHandler(final InternalContext internalContext, final Class<?> topClass) {
        super(internalContext);
        _idResolver         = new IDResolverImpl();
        _javaPackages       = new HashMap<Class<?>, String>();
        _topClass           = topClass;
        _anyNodeHandler		= new AnyNodeUnmarshalHandler(_namespaceHandling);
        _attributeSetFactory = new AttributeSetBuilder(_namespaceHandling);
    }
    
    /**
     * Adds a mapping from the given namespace URI to the given
     * package name
     * 
     * @param nsURI the namespace URI to map from
     * @param packageName the package name to map to
     */
    public void addNamespaceToPackageMapping(String nsURI, String packageName) 
    {
    	_namespaceHandling.addNamespaceToPackageMapping(nsURI, packageName);
        
    } //-- addNamespaceToPackageMapping

    /**
     * Returns the Object that the UnmarshalHandler is currently
     * handling (within the object model), or null if the current
     * element is a simpleType.
     *
     * @return the Object currently being unmarshalled, or null if the
     * current element is a simpleType.
     */
    public Object getCurrentObject() {
    	if (!_stateStack.isEmpty()) {
			UnmarshalState state = _stateStack.getLastState();
			if (state != null) {
				return state.getObject();
			}
		}
		return null;
    } //-- getCurrentObject

    /**
     * Returns the "root" Object (ie. the entire object model)
     * being unmarshalled.
     *
     * @return the root Object being unmarshalled.
    **/
    public Object getObject() {
        if (_topState != null) {
            return _topState.getObject();
        }
        return null;
    }

    /**
     * Sets the ClassLoader to use when loading classes
     *
     * @param loader the ClassLoader to use
    **/
    public void setClassLoader(ClassLoader loader) {
        _loader = loader;
    } //-- setClassLoader

    /**
     * Sets whether or not to clear collections (including arrays)
     * upon first use to remove default values. By default, and
     * for backward compatibility with previous versions of Castor
     * this value is false, indicating that collections are not
     * cleared before initial use by Castor.
     *
     * @param clear the boolean value that when true indicates
     * collections should be cleared upon first use.
     */
    public void setClearCollections(boolean clear) {
        _clearCollections = clear;
    } //-- setClearCollections

    /**
	 * Included for backward compatibility. Debug is replaced with 
	 * commons-logging.
	 * @deprecated
    **/
    public void setDebug(boolean debug) {
    	// no-op
    }

    /**
     * Sets the IDResolver to use when resolving IDREFs for
     * which no associated element may exist in XML document.
     *
     * @param idResolver the IDResolver to use when resolving
     * IDREFs for which no associated element may exist in the
     * XML document.
    **/
    public void setIDResolver(final IDResolver idResolver) {
        ((IDResolverImpl) _idResolver).setResolver(idResolver);
    }


    /**
     * Sets whether or not attributes that do not match
     * a specific field should simply be ignored or
     * reported as an error. By default, extra attributes
     * are ignored.
     *
     * @param ignoreExtraAtts a boolean that when true will
     * allow non-matched attributes to simply be ignored.
    **/
    public void setIgnoreExtraAttributes(boolean ignoreExtraAtts) {
        _strictAttributes = (!ignoreExtraAtts);
    } //-- setIgnoreExtraAttributes

    /**
     * Sets whether or not elements that do not match
     * a specific field should simply be ignored or
     * reported as an error. By default, extra attributes
     * are ignored.
     *
     * @param ignoreExtraElems a boolean that when true will
     * allow non-matched attributes to simply be ignored.
    **/
    public void setIgnoreExtraElements(boolean ignoreExtraElems) {
        _strictElementHandler.setIgnoreExtraElements(ignoreExtraElems);
    } //-- setIgnoreExtraElements

    /**
     * Custom logging replaced with commons-logging.
     * @deprecated
    **/
    public void setLogWriter(PrintWriter printWriter) {
    	// no-op
    } //-- setLogWriter

    /**
     * Sets a boolean that when true indicates that objects
     * contained within the object model should be re-used
     * where appropriate. This is only valid when unmarshalling
     * to an existing object.
     *
     * @param reuse the boolean indicating whether or not
     * to re-use existing objects in the object model.
    **/
    public void setReuseObjects(boolean reuse) {
        _reuseObjects = reuse;
    } //-- setReuseObjects

//    /**
//     * Sets the ClassDescriptorResolver to use for loading and
//     * resolving ClassDescriptors
//     *
//     * @param cdResolver the ClassDescriptorResolver to use
//    **/
//    public void setResolver(XMLClassDescriptorResolver cdResolver) {
//        this._cdResolver = cdResolver;
//    } //-- setResolver

    /**
     * Sets the root (top-level) object to use for unmarshalling into.
     *
     * @param root the instance to unmarshal into.
    **/
    public void setRootObject(Object root) {
        _topObject = root;
    } //-- setRootObject

    /**
     * Sets an {@link org.exolab.castor.xml.UnmarshalListener}.
     *
     * @param listener the {@link org.exolab.castor.xml.UnmarshalListener} to use with this instance
     * of the UnmarshalHandler.
     * @deprecated please move to the new {@link org.castor.xml.UnmarshalListener} interface
     */
    public void setUnmarshalListener (org.exolab.castor.xml.UnmarshalListener listener) {
    	_delegateUnmarshalListener.setUnmarshalListener(listener);
    }

    /**
     * Sets an {@link org.castor.xml.UnmarshalListener}.
     *
     * @param listener the {@link org.castor.xml.UnmarshalListener} to use with this instance
     * of the UnmarshalHandler.
     */
    public void setUnmarshalListener (org.castor.xml.UnmarshalListener listener) {
    	_delegateUnmarshalListener.setUnmarshalListener(listener);
    }

    /**
     * Sets the flag for validation.
     * 
     * @param validate A boolean to indicate whether or not validation should be done
     *        during umarshalling.
     *        <br/>
     *        By default, validation will be performed.
     */
    public void setValidation(boolean validate) {
        this._validate = validate;
    } //-- setValidation
    
    /**
     * Sets the top-level whitespace (xml:space) to either
     * preserving or non preserving. The XML document
     * can override this value using xml:space on specific
     * elements. This sets the "default" behavior
     * when xml:space="default".
     *
     * @param preserve a boolean that when true enables
     * whitespace preserving by default. 
     */
    public void setWhitespacePreserve(boolean preserve) {
        _wsPreserve = preserve;
    } //-- setWhitespacePreserve

    //-----------------------------------/
    //- SAX Methods for DocumentHandler -/
    //-----------------------------------/

    public void characters(char[] ch, int start, int length)
        throws SAXException {
    	new CharactersProcessor(this).compute(ch, start, length);
    } //-- characters


    public void endDocument() throws org.xml.sax.SAXException {
        //-- I've found many application don't always call
        //-- #endDocument, so I usually never put any
        //-- important logic here
    } //-- endDocument


    public void endElement(String name) throws org.xml.sax.SAXException {
        new EndElementProcessor(this).compute(name);
    } //-- endElement

    /**
     * Decode binary data and return decoded value.
     * @param descriptor {@link XMLFieldDescriptor} instance for the field whose value requires decoding.
     * @param binaryData The binary data value to be decoded
     * @return Decode data.
     */
    byte[] decodeBinaryData(final XMLFieldDescriptor descriptor,
            final String binaryData) {
        //-- Base64/HexBinary decoding
        byte[] decodedValue;
        if ((descriptor.isMultivalued() 
                && HexDecoder.DATA_TYPE.equals(descriptor.getComponentType())) 
                || HexDecoder.DATA_TYPE.equals(descriptor.getSchemaType())) {
            decodedValue = HexDecoder.decode(binaryData);
        } else {
            decodedValue = Base64Decoder.decode(binaryData);
        }
        return decodedValue;
    }

    /**
     * <p>ContentHandler#endElement</p>
     *
     * Signals the end of an element
     *
     * @param localName The name of the element.
     */
    public void endElement(String namespaceURI, String localName, String qName)
    throws org.xml.sax.SAXException {        
        if (StringUtils.isEmpty(qName)) {
            if (StringUtils.isEmpty(localName)) {
				String error = resourceBundle
						.getString("unmarshalHandler.error.localName.and.qName.null");
                throw new SAXException(error);
            }
            qName = localName;
            if (StringUtils.isNotEmpty(namespaceURI)) {
                //-- rebuild qName, for now
                String prefix = _namespaceHandling.getNamespacePrefix(namespaceURI);
                if (StringUtils.isEmpty(prefix))
                    qName = prefix + ":" + localName;
            }
        }
       
        endElement(qName); 
    } //-- endElement
    
    
    /**
     * Signals to end the namespace prefix mapping
     * 
     * @param prefix the namespace prefix 
     */
    public void endPrefixMapping(String prefix)
        throws SAXException
    { 
        //-- nothing to do , already taken care of in 
        //-- endElement except if we are unmarshalling an 
        //-- AnyNode
    	if (_anyNodeHandler.hasAnyUnmarshaller()) {
    		_anyNodeHandler.endPrefixMapping(prefix);
		}
        
    } //-- endPrefixMapping


    public void ignorableWhitespace(char[] ch, int start, int length)
            throws org.xml.sax.SAXException {

        // -- If we are skipping elements that have appeared in the XML but for
        // -- which we have no mapping, skip the text and return
        if (_strictElementHandler.skipElement()) {
            return;
        }

        if (_stateStack.isEmpty()) {
            return;
        }

        if (_anyNodeHandler.hasAnyUnmarshaller()) {
            _anyNodeHandler.ignorableWhitespace(ch, start, length);
        } else {
            UnmarshalState state = _stateStack.getLastState();
            if (state.isWhitespacePreserving()) {
                if (state.getBuffer() == null)
                    state.setBuffer(new StringBuffer());
                state.getBuffer().append(ch, start, length);
            }
        }
    } // -- ignorableWhitespace

    public void processingInstruction(String target, String data)
            throws org.xml.sax.SAXException {
        // -- do nothing for now
    } // -- processingInstruction

    public void setDocumentLocator(Locator locator) {
        this._locator = locator;
    } //-- setDocumentLocator

    public Locator getDocumentLocator() {
        return _locator;
    } //-- getDocumentLocator

    /**
     * Signals that an entity was skipped by the parser
     *
     * @param name the skipped entity's name
     */
    public void skippedEntity(String name)
        throws SAXException
    {
        //-- do nothing
        
    } //-- skippedEntity
    
    /**
     * Signals the start of a new document
     */
    public void startDocument() throws org.xml.sax.SAXException {

        //-- I've found many application don't always call
        //-- #startDocument, so I usually never put any
        //-- important logic here

    } //-- startDocument
    
    private void extractNamespaceInformation(Attributes attributes) {

        if (attributes == null || attributes.getLength() == 0) {
            return;
        }

        // look for any potential namespace declarations in case namespace 
        // processing was disabled on the XML parser
        for (int i = 0; i < attributes.getLength(); i++) {
            String attributeName = attributes.getQName(i);
            if (StringUtils.isNotEmpty(attributeName)) {
                if (attributeName.equals(XMLNS)) {
                    _namespaceHandling.addDefaultNamespace(attributes.getValue(i));
                } else if (attributeName.startsWith(XMLNS_PREFIX)) {
                    String prefix = attributeName.substring(XMLNS_PREFIX.length());
                    _namespaceHandling.addNamespace(prefix, attributes.getValue(i));
                }
            } else {
                // -- if qName is null or empty, just process as a normal
                // -- attribute
                attributeName = attributes.getLocalName(i);
                if (XMLNS.equals(attributeName)) {
                    _namespaceHandling.addDefaultNamespace(attributes.getValue(i));
                }
            }
        }
    }
    
    /**
     * <p>ContentHandler#startElement</p>
     *
     * Signals the start of element.
     *
     * @param localName The name of the element.
     * @param atts The AttributeList containing the associated attributes for the element.
     */
    public void startElement(String namespaceURI, String localName, String qName, Attributes atts)
    throws org.xml.sax.SAXException {
        if (LOG.isTraceEnabled()) {
        	String trace;
            if (StringUtils.isNotEmpty(qName))
            	trace = MessageFormat
				.format(
						resourceBundle
								.getString("unmarshalHandler.log.trace.startElement"),
						new Object[] { qName });
            else
            	trace = MessageFormat
				.format(
						resourceBundle
								.getString("unmarshalHandler.log.trace.startElement"),
						new Object[] { localName });
            LOG.trace(trace);
        }
        
        //-- If we are skipping elements that have appeared in the XML but for
        //-- which we have no mapping, increase the ignore depth counter and return
        if(_strictElementHandler.skipStartElement()) {
        	return;
        }

        //-- if we are in an <any> section
        //-- we delegate the event handling
        if (_anyNodeHandler.hasAnyUnmarshaller()) {
        	_anyNodeHandler.startElement(namespaceURI, localName, qName, atts);
			return;
		}
        
        //-- Create a new namespace scope if necessary and
        //-- make sure the flag is reset to true
        if(_namespaceHandling.isNewNamespaceScopeNecessary()) {
        	_namespaceHandling.startNamespaceScope();
        } else {
            _namespaceHandling.setNewNamespaceScopeNecessary(true);
        }

        // extract namespace information
        extractNamespaceInformation(atts);
        
        // preserve parser passed arguments for any potential delegation
        String tmpQName = null;
        
        if (StringUtils.isEmpty(localName)) {
            if (StringUtils.isEmpty(qName)) {
                String error = resourceBundle.getString("unmarshalHandler.error.localName.and.qName.null");
                throw new SAXException(error);
            }
            localName = qName;
            tmpQName = qName;
        } else {
            if (StringUtils.isEmpty(qName)) {
                if (StringUtils.isEmpty(namespaceURI)) {
                	tmpQName = localName;
                } else {
                    String prefix = _namespaceHandling.getNamespacePrefix(namespaceURI);
                    if (StringUtils.isNotEmpty(prefix)) {
                    	tmpQName = prefix + ":" + localName;
                    }
                }
                
            } else {
            	tmpQName = qName;
            }
        }
        _anyNodeHandler.preservePassedArguments(tmpQName, atts);
        
        int idx = localName.indexOf(':');
        if (idx >= 0) {
            String prefix = localName.substring(0, idx);
            localName = localName.substring(idx+1);
            if (StringUtils.isEmpty(namespaceURI)) {
                namespaceURI = _namespaceHandling.getNamespaceURI(prefix);
            }
        } else {
            // check for default namespace declaration 
            String defaultNamespace = _namespaceHandling.getDefaultNamespaceURI();
            // TODO[WG]: remove unnecessary check as it simply is wrong
            if (defaultNamespace != null && !defaultNamespace.equals("http://castor.exolab.org")) {
                namespaceURI = defaultNamespace;
            }
            //-- adjust empty namespace
            if (StringUtils.isEmpty(namespaceURI))
                namespaceURI = null;
        }
        
        //-- call private startElement
        startElementProcessing(localName, namespaceURI, _attributeSetFactory.getAttributeSet(atts));
        
    } //-- startElement
 
    /**
     * <p>DocumentHandler#startElement</p>
     *
     * Signals the start of element.
     *
     * @param name The name of the element.
     * @param attList The AttributeList containing the associated attributes for the
     *        element.
     * @deprecated
     */
    public void startElement(String name, AttributeList attList)
    throws org.xml.sax.SAXException {
        if (LOG.isTraceEnabled()) {
            String trace = MessageFormat.format(resourceBundle.getString("unmarshalHandler.log.trace.startElement"),
                    new Object[] { name });
            LOG.trace(trace);
        }
        
        //-- If we are skipping elements that have appeared in the XML but for
        //-- which we have no mapping, increase the ignore depth counter and return
        if (_strictElementHandler.skipStartElement()) {
        	return;
        }

        //-- if we are in an <any> section
        //-- we delegate the event handling
        if (_anyNodeHandler.hasAnyUnmarshaller()) {
        	_anyNodeHandler.startElement(name, attList);
			return;
        }
        
        _anyNodeHandler.preservePassedArguments(name, attList);
        
        //-- The namespace of the given element
        String namespace = null;

        //-- Begin Namespace Handling :
        //-- XXX Note: This code will change when we update the XML event API

        _namespaceHandling.createNamespace();

        String prefix = "";
        //String qName = name;
        int idx = name.indexOf(':');
        if (idx >= 0) {
             prefix = name.substring(0,idx);
             name = name.substring(idx+1);
        }

        namespace = _namespaceHandling.getNamespaceURI(prefix);
        
        //-- End Namespace Handling
        
        //-- call private startElement method
        startElementProcessing(name, namespace, _attributeSetFactory.getAttributeSet(attList));
        
    } //-- startElement
        
    /**
     * Signals the start of an element with the given name.
     *
     * @param name the NCName of the element. It is an error
     * if the name is a QName (ie. contains a prefix).
     * @param namespace the namespace of the element. This may be null.
     * Note: A null namespace is not the same as the default namespace unless
     * the default namespace is also null.
     * @param atts the AttributeSet containing the attributes associated
     * with the element.
     */
    void startElementProcessing(String name, String namespace, AttributeSet atts)
            throws SAXException {
        new StartElementProcessor(this).compute(name, namespace, atts);
    }
 
	void processFirstElement(String name, String namespace,
			AttributeSet atts, String xmlSpace) throws SAXException {
		if (_topClass == null) {
		    if (_topObject != null) {
		        _topClass = _topObject.getClass();
		    }
		}
//            if (_cdResolver == null) {
//                if (_topClass == null) {
//                    String err = "The class for the root element '" +
//                        name + "' could not be found.";
//                    throw new SAXException(err);
//                }
//                _cdResolver = (XMLClassDescriptorResolver) ClassDescriptorResolverFactory.createClassDescriptorResolver(BindingType.XML);
//                _cdResolver.setClassLoader(_loader);
//            }
		if (getInternalContext().getXMLClassDescriptorResolver() == null) {
		    // Joachim 2007-09-04 check is new
		    String message = resourceBundle.getString("unmarshalHandler.log.warn.class.descriptor.not.set");
		    LOG.warn(message);
		    throw new IllegalStateException(message);
		}

		_topState = new UnmarshalState();            
		_topState.setElementName(name);
		_topState.setWhitespacePreserving((xmlSpace != null) ? PRESERVE.equals(xmlSpace) : _wsPreserve);
		
		XMLClassDescriptor classDesc = null;
		//-- If _topClass is null, then we need to search
		//-- the resolver for one
		String instanceClassname = null;
		if (_topClass == null) {

		    //-- check for xsi:type
		    instanceClassname = getInstanceType(atts, null);
		    if (instanceClassname != null) {
		        //-- first try loading class directly
		        try {
		            _topClass = loadClass(instanceClassname, null);
		        }
		        catch(ClassNotFoundException cnfe) {}
		            
		        if (_topClass == null) {
		            classDesc = getClassDescriptor(instanceClassname);
		            if (classDesc != null) {
		                _topClass = classDesc.getJavaClass();
		            }
		            if (_topClass == null) {
						String error = MessageFormat
								.format(
										resourceBundle
												.getString("unmarshalHandler.error.class.not.found"),
										new Object[] { instanceClassname });
		                throw new SAXException(error);
		            }
		        }
		    }
		    else {
		        classDesc = resolveByXMLName(name, namespace, null);
		        if (classDesc == null) {
		            classDesc = getClassDescriptor(name, _loader);
		            if (classDesc == null) {
		                classDesc = getClassDescriptor(getJavaNaming().toJavaClassName(name));
		            }
		        }
		        if (classDesc != null) {
		            _topClass = classDesc.getJavaClass();
		        }
		    }

		    if (_topClass == null) {
		    	String err = MessageFormat
				.format(
						resourceBundle
								.getString("unmarshalHandler.error.class.root.not.found"),
						new Object[] { name });
		        throw new SAXException(err);
		    }
		}

		//-- create a "fake" FieldDescriptor for the root element
		XMLFieldDescriptorImpl fieldDesc
		    = new XMLFieldDescriptorImpl(_topClass,
		                                 name,
		                                 name,
		                                 NodeType.Element);

		_topState.setFieldDescriptor(fieldDesc);
		//-- look for XMLClassDescriptor if null
		//-- always check resolver first
		if (classDesc == null) {
            classDesc = getClassDescriptor(_topClass);
        }
		    
		//-- check for top-level primitives 
		if (classDesc == null) {
		    if (isPrimitive(_topClass)) {
		        classDesc = new PrimitivesClassDescriptor(_topClass);
		        fieldDesc.setIncremental(false);
		        _topState.setPrimitiveOrImmutable(true);
		    }
		}
		
		fieldDesc.setClassDescriptor(classDesc);
		if (classDesc == null) {
		    //-- report error
		    if ((!isPrimitive(_topClass)) &&
		            (!Serializable.class.isAssignableFrom( _topClass ))) {
                throw new SAXException(MarshalException.NON_SERIALIZABLE_ERR);
            }

			String err = MessageFormat
					.format(
							resourceBundle
									.getString("unmarshalHandler.error.create.class.descriptor"),
							new Object[] { _topClass.getName() });
        
			throw new SAXException(err);
		}
		_topState.setClassDescriptor(classDesc);
		_topState.setType(_topClass);

		if  ((_topObject == null) && (!_topState.isPrimitiveOrImmutable())) {
		    // Retrieving the xsi:type attribute, if present
		    String topPackage = getJavaPackage(_topClass);
		    
		    if (instanceClassname == null) {
                instanceClassname = getInstanceType(atts, topPackage);
            } else {
		        //-- instance type already processed above, reset
		        //-- to null to prevent entering next block
		        instanceClassname = null;
		    }
		        
		    if (instanceClassname != null) {
		        Class<?> instanceClass = null;
		        try {

		            XMLClassDescriptor xcd = getClassDescriptor(instanceClassname);

		            boolean loadClass = true;
		            if (xcd != null) {
		                instanceClass = xcd.getJavaClass();
		                if (instanceClass != null) {
		                    loadClass = (!instanceClassname.equals(instanceClass.getName()));
		                }
		            }
		            
		            if (loadClass) {
		                try {
		                    instanceClass = loadClass(instanceClassname, null);
		                }
		                catch(ClassNotFoundException cnfe) {
		                    //-- revert back to ClassDescriptor's associated
		                    //-- class
		                    if (xcd != null) {
                                instanceClass = xcd.getJavaClass();
                            }
		                }
		            }

		            if (instanceClass == null) {
		            	String error = MessageFormat
						.format(
								resourceBundle
										.getString("unmarshalHandler.error.class.not.found"),
								new Object[] { instanceClassname });
		                throw new SAXException(error);
		            }

		            if (!_topClass.isAssignableFrom(instanceClass)) {
		            	String err = MessageFormat
						.format(
								resourceBundle
										.getString("unmarshalHandler.error.not.subclass"),
								new Object[] { instanceClass, _topClass });
		                throw new SAXException(err);
		            }

		        }
		        catch(Exception ex) {
		        	String err = MessageFormat
					.format(
							resourceBundle
									.getString("unmarshalHandler.error.unable.instantiate"),
							new Object[] { instanceClassname });
		            throw new SAXException(err, ex);
		        }

		        //-- try to create instance of the given Class
		        Arguments args = processConstructorArgs(atts, classDesc);
		        _topState.setObject(createInstance(instanceClass, args));
		    } else {
		        //-- no xsi type information present
		        //-- try to create instance of the given Class
		        Arguments args = processConstructorArgs(atts, classDesc);
		        _topState.setObject(createInstance(_topClass, args));
		    }
		} else {
		    //-- otherwise use _topObject
		    _topState.setObject(_topObject);
		}
		
		_stateStack.pushState(_topState);
		
		if (!_topState.isPrimitiveOrImmutable()) {
		    //--The top object has just been initialized
		    //--notify the listener
			Object stateObject = _topState.getObject();
			Object parentObject = (_topState.getParent() == null) ? null
					: _topState.getParent().getObject();
			
			_delegateUnmarshalListener.initialized(stateObject, parentObject);  
		    processAttributes(atts, classDesc);
		    _delegateUnmarshalListener.attributesProcessed(stateObject, parentObject);
		    _namespaceHandling.processNamespaces(classDesc,_stateStack.getLastState().getObject());
		}
		
		String pkg = getJavaPackage(_topClass);
		if (getMappedPackage(namespace) == null) {
		    addNamespaceToPackageMapping(namespace, pkg);
		}
	}


    /**
     * Indicates whether validation is enabled or not.
     * @return True if validation is enabled.
     */
    boolean isValidating() {
        return _validate;
    }

    /**
     * Signals to start the namespace - prefix mapping
     * 
     * @param prefix the namespace prefix to map
     * @param uri the namespace URI
     */
    public void startPrefixMapping(String prefix, String uri)
        throws SAXException
    { 
        
        //-- Patch for Xerces 2.x bug
        //-- prevent attempting to declare "XML" namespace
        if (Namespaces.XML_NAMESPACE_PREFIX.equals(prefix) && 
            Namespaces.XML_NAMESPACE.equals(uri))             
        {
            return;
        }
        else if (XMLNS.equals(prefix)) {
        	return;
        }
        //-- end Xerces 2.x bug
        
        //-- Forward the call to SAX2ANY 
        //-- or create a namespace node
        if (_anyNodeHandler.hasAnyUnmarshaller()) {
        	_anyNodeHandler.startPrefixMapping(prefix, uri);
        }
        else if(_namespaceHandling.isNewNamespaceScopeNecessary()) {
        	_namespaceHandling.stopNamespaceScope();
        }
        _namespaceHandling.addNamespace(prefix, uri);
        
        /*
        //-- add namespace declarations to set of current attributes        
        String attName = null;
        if ((prefix == null)  || (prefix.length() == 0))
            attName = XMLNS_DECL;
        else
            attName = XMLNS_PREFIX + prefix;
            
        _currentAtts.addAttribute(attName, uri);
        */
        
    } //-- startPrefixMapping


     //------------------------------------/
    //- org.xml.sax.ErrorHandler methods -/
    //------------------------------------/

    public void error(SAXParseException exception)
        throws org.xml.sax.SAXException
    {
		String error = MessageFormat
				.format(resourceBundle
						.getString("unmarshalHandler.error.sax.exception"),
						new Object[] { exception.getMessage(),
								exception.getLineNumber(), exception.getColumnNumber()});
        throw new SAXException (error, exception);
    } //-- error

    public void fatalError(SAXParseException exception)
        throws org.xml.sax.SAXException
    {
    	this.error(exception);

    } //-- fatalError


    public void warning(SAXParseException exception)
        throws org.xml.sax.SAXException
    {
    	this.error(exception);

    } //-- warning

      //---------------------/
     //- Protected Methods -/
    //---------------------/

    // TODO: Joachim 2007-09-04 remove me
//    /**
//     * Sets the current Castor configuration. Currently this
//     * Configuration is only used during Validation (which is
//     * why this method is currently protected, since it has
//     * no effect at this point on the actual configuration of 
//     * the unmarshaller)
//     *
//     * Currently, this method should only be called by the 
//     * Unmarshaller.
//     */
//    protected void setConfiguration(Configuration config) {
//        _config = config;
//    } //-- setConfiguration
    
      //-------------------/
     //- Private Methods -/
    //-------------------/

    /**
     * Adds the given reference to the "queue" until the referenced object
     * has been unmarshalled.
     *
     * @param idRef the ID being referenced
     * @param parent the target/parent object for the field
     * @param descriptor the XMLFieldDescriptor for the field
     */
    void addReference(final String idRef, final Object parent, 
            final XMLFieldDescriptor descriptor) {
        
        ReferenceInfo refInfo = new ReferenceInfo(idRef, parent, descriptor);
        refInfo.setNext(_resolveTable.get(idRef));
        _resolveTable.put(idRef, refInfo);
    }
    
    /**
     * Creates an instance of the given class /type, using 
     * the arguments provided (if there are any).
     * @param type The class type to be used during instantiation
     * @param args (Optional) arguments to be used during instantiation
     */
     Object createInstance(final Class<?> type, final Arguments args)
            throws SAXException {
        Object instance = null;
        try {
            if (args == null) {
                instance = _objectFactory.createInstance(type);
            } else {
                instance = _objectFactory.createInstance(type, args.getTypes(),
                        args.getValues());
            }
        } catch (Exception ex) {
        	String error = MessageFormat
			.format(resourceBundle
					.getString("unmarshalHandler.error.unable.instantiate"),
					new Object[] { type.getName() });
            throw new SAXException(error, ex);
        }
        return instance;
    } // -- createInstance
     
     /**
     * Returns the resolved instance type attribute (xsi:type).
     * If present the instance type attribute is resolved into
     * a java class name and then returned.
     *
     * @param atts the AttributeList to search for the instance type
     * attribute.
     * @return the java class name corresponding to the value of
     * the instance type attribute, or null if no instance type
     * attribute exists in the given AttributeList.
     */
    String getInstanceType(AttributeSet atts, String currentPackage) 
        throws SAXException
    {

        if (atts == null) return null;

        //-- find xsi:type attribute
        String type = atts.getValue(XSI_TYPE, XSI_NAMESPACE);

        if (type != null) {
            
            if (type.startsWith(JAVA_PREFIX)) {
                return type.substring(JAVA_PREFIX.length());
            }
            
            // check for namespace prefix in type
            int idx = type.indexOf(':');
            String typeNamespaceURI = null;
            if (idx >= 0) {
                // there is a namespace prefix
                String prefix = type.substring(0, idx);
                type = type.substring(idx + 1);
                typeNamespaceURI = _namespaceHandling.getNamespaceURI(prefix);
            }

            //-- Retrieve the type corresponding to the schema name and
            //-- return it.
            XMLClassDescriptor classDesc = null;
            
            try {
                classDesc = getInternalContext().getXMLClassDescriptorResolver().resolveByXMLName(type, typeNamespaceURI, _loader);            

                if (classDesc != null)
                    return classDesc.getJavaClass().getName();


                //-- if class descriptor is not found here, then no descriptors
                //-- existed in memory...try to load one based on name of
                //-- Schema type
                final String className = getJavaNaming().toJavaClassName(type);
            
                String adjClassName = className;
                String mappedPackage = getMappedPackage(typeNamespaceURI);
                if ((mappedPackage != null) && (mappedPackage.length() > 0)) {
                    adjClassName = mappedPackage + "." + className;
                }
            	classDesc = getInternalContext().getXMLClassDescriptorResolver().resolve(adjClassName, _loader);
                if (classDesc != null)
                    return classDesc.getJavaClass().getName();

                //-- try to use "current Package"
                if (StringUtils.isNotEmpty(currentPackage)) {
                	adjClassName = currentPackage + '.' + className;
                }
                
                classDesc = getInternalContext().getXMLClassDescriptorResolver().resolve(adjClassName, _loader);
                if (classDesc != null)
                    return classDesc.getJavaClass().getName();
                
                //-- Still can't find type, this may be due to an
                //-- attempt to unmarshal an older XML instance
                //-- that was marshalled with a previous Castor. A
                //-- bug fix in the XMLMappingLoader prevents old
                //-- xsi:type that are missing the "java:"
                classDesc = getInternalContext().getXMLClassDescriptorResolver().resolve(type, _loader);
                if (classDesc != null)
                    return classDesc.getJavaClass().getName();
            }
            catch(ResolverException rx) {
                throw new SAXException(rx);
            }
        }
        return null;
    } //-- getInstanceType
    
    /**
     * Looks up the package name from the given namespace URI.
     * 
     * @param namespace the namespace URI to lookup
     * @return the package name or null.
     */
    private String getMappedPackage(final String namespace) {
        return _namespaceHandling.getMappedPackage(namespace);
    }

    /**
     * Processes the given attribute list, and attempts to add each
     * {@link Attributes} to the current {@link Object} on the stack.
     *
     * @param atts the AttributeSet to process
     * @param classDesc the classDesc to use during processing
    **/
    void processAttributes(final AttributeSet atts, XMLClassDescriptor classDesc)
        throws SAXException {

        //-- handle empty attributes
        if ((atts == null) || (atts.getSize() == 0)) {
            if (classDesc != null) {
                XMLFieldDescriptor[] descriptors
                    = classDesc.getAttributeDescriptors();
                for (int i = 0; i < descriptors.length; i++) {
                    XMLFieldDescriptor descriptor = descriptors[i];
                    if (descriptor == null) {
                        continue;
                    }
                    //-- Since many attributes represent primitive
                    //-- fields, we add an extra validation check here
                    //-- in case the class doesn't have a "has-method".
                    if (descriptor.isRequired() && (isValidating() || LOG.isDebugEnabled())) {
                    	String errorMsg;
						if (_locator != null) {
							errorMsg = MessageFormat
									.format(
											resourceBundle
													.getString("unmarshalHandler.error.attribute.missing.location"),
											classDesc.getXMLName(), descriptor
													.getXMLName(), _locator
													.getLineNumber(), _locator
													.getColumnNumber());
						} else {
							errorMsg = MessageFormat
									.format(
											resourceBundle
													.getString("unmarshalHandler.error.attribute.missing"),
											classDesc.getXMLName(), descriptor
													.getXMLName());
						}
                        if (isValidating()) {
                            throw new SAXException(errorMsg);
                        }
                        LOG.debug(errorMsg);
                    }
                }
            }
            return;
        }


        UnmarshalState state = _stateStack.getLastState();
        Object object = state.getObject();

        if (classDesc == null) {
            classDesc = state.getClassDescriptor();
            if (classDesc == null) {
                //-- no class desc, cannot process atts
                //-- except for wrapper/location atts
                processWrapperAttributes(atts);                
                return;
            }
        }
        
        //-- First loop through Attribute Descriptors.
        //-- Then, if we have any attributes which
        //-- haven't been processed we can ask
        //-- the XMLClassDescriptor for the FieldDescriptor.

        boolean[] processedAtts = new boolean[atts.getSize()];
        XMLFieldDescriptor[] descriptors = classDesc.getAttributeDescriptors();
        for (XMLFieldDescriptor descriptor : descriptors) {

            String name      = descriptor.getXMLName();
            String namespace = descriptor.getNameSpaceURI();
            String path = descriptor.getLocationPath();
            StringBuffer fullAttributePath = new StringBuffer();
            
            if (StringUtils.isNotEmpty(path)) {
                fullAttributePath.append(path + "/"); 
            }
            
            fullAttributePath.append(name);
            
            if (!name.equals(fullAttributePath.toString())) {
                int index = atts.getIndex(name, namespace);
                if (index >= 0) {
                    processedAtts[index] = true;
                }
                continue;
            }

            int index = atts.getIndex(name, namespace);

            String attValue = null;
            if (index >= 0) {
                attValue = atts.getValue(index);
                processedAtts[index] = true;
            }

            try {
                processAttribute(name, namespace, attValue, descriptor, classDesc, object);
            } catch (IllegalStateException ise) {
            	String error = MessageFormat
    			.format(resourceBundle
    					.getString("unmarshalHandler.error.unable.add.attribute"),
    					new Object[] { name, state.getClassDescriptor().getJavaClass().getName(), ise });
                throw new SAXException(error, ise);
            }
        }

        //-- Handle any non processed attributes...
        //-- This is useful for descriptors that might use
        //-- wild-cards or other types of matching...as well
        //-- as backward compatibility...attribute descriptors
        //-- were erronously getting set with the default
        //-- namespace by the source generator...this is
        //-- also true of the generated classes for the
        //-- Mapping Framework...we need to clean this up
        //-- at some point in the future.
        for (int i = 0; i < processedAtts.length; i++) {
            if (processedAtts[i]) {
                continue;
            }

            String namespace = atts.getNamespace(i);
            String name = atts.getName(i);
            
            //-- skip XSI attributes
            if (XSI_NAMESPACE.equals(namespace)) {
                if (NIL_ATTR.equals(name)) {
                    String value = atts.getValue(i);
                    state.setNil(("true".equals(value)));
                }
                continue;
            }
                

            if (name.startsWith(XML_PREFIX + ':')) {
                
                //-- XML specification specific attribute
                //-- It should be safe to ignore these...but
                //-- if you think otherwise...let use know!
                if (LOG.isDebugEnabled()) {
                	String debugMsg = MessageFormat
        			.format(resourceBundle
        					.getString("unmarshalHandler.log.debug.ignore.extra.attribute"),
        					new Object[] { name, state.getClassDescriptor().getJavaClass().getName() });
                    LOG.debug(debugMsg);
                }
                continue;
            }

            //-- This really should handle namespace...but it currently
            //-- doesn't. Ignoring namespaces also helps with the
            //-- backward compatibility issue mentioned above.
            XMLFieldDescriptor descriptor =
                classDesc.getFieldDescriptor(name, namespace, NodeType.Attribute);
                
            if (descriptor == null) {
                //-- check for nested attribute...loop through
                //-- stack and find correct descriptor
                String path = state.getElementName();
                StringBuffer pathBuf = null;
                Integer parentStateIndex = _stateStack.getFirstParentStateIndex(); 
                while (parentStateIndex >= 0) {
                   UnmarshalState targetState = _stateStack.peekAtState(parentStateIndex--);
                   if (targetState.isWrapper()) {
                      //path = targetState.elementName + "/" + path;
                      pathBuf = resetStringBuffer(pathBuf);
                      pathBuf.append(targetState.getElementName());
                      pathBuf.append('/');
                      pathBuf.append(path);
                      path = pathBuf.toString();
                      continue;
                   }
                   classDesc = targetState.getClassDescriptor();
                   descriptor = classDesc.getFieldDescriptor(name, namespace, NodeType.Attribute);

                   if (descriptor != null) {
                      String tmpPath = descriptor.getLocationPath();
                      if (path.equals(StringUtils.defaultString(tmpPath))) {
                         _stateStack.resetParentState();
                         break; //-- found
                      }
                   }

                   pathBuf = resetStringBuffer(pathBuf);
                   pathBuf.append(targetState.getElementName());
                   pathBuf.append('/');
                   pathBuf.append(path);
                   path = pathBuf.toString();
                   //path = targetState.elementName + "/" + path;
                   //-- reset descriptor to make sure we don't
                   //-- exit the loop with a reference to a 
                   //-- potentially incorrect one.
                   descriptor = null;
                }
            }
            if (descriptor == null) {
                if (_strictAttributes) {
                    //-- handle error
                	String errorMsg = MessageFormat
        			.format(resourceBundle
        					.getString("unmarshalHandler.error.strict.attribute.error"),
        					new Object[] { name, state.getElementName() });
                    throw new SAXException(errorMsg);
                }
                continue;
            }

            try {
                processAttribute(name, namespace, atts.getValue(i), descriptor, classDesc, object);
            } catch (IllegalStateException ise) {
            	String errorMsg = MessageFormat
    			.format(resourceBundle
    					.getString("unmarshalHandler.error.unable.add.attribute"),
    					new Object[] { name, state.getClassDescriptor().getJavaClass().getName(), ise });
                throw new SAXException(errorMsg, ise);
            }
        }

    }

	/**
	 * Returns either the passed in StringBuffer and sets its length to 0, or if
	 * the StringBuffer is null, an empty StringBuffer
	 * 
	 * @param buffer
	 *            a StringBuffer, can be null
	 * @return returns an empty StringBuffer
	 */
	private StringBuffer resetStringBuffer(StringBuffer buffer) {
		if (buffer == null)
		    return new StringBuffer();
		
		buffer.setLength(0);
		return buffer;
	}

    /**
     * Processes the given AttributeSet for wrapper elements.
     * 
     * @param atts the AttributeSet to process
     * @throws SAXException If the AttributeSet cannot be processed
     */
    void processWrapperAttributes(final AttributeSet atts)
        throws SAXException {
        
        UnmarshalState state = _stateStack.getLastState();
        
        //-- loop through attributes and look for the
        //-- ancestor objects that they may belong to
        for (int i = 0; i < atts.getSize(); i++) {
            String name = atts.getName(i);
            String namespace = atts.getNamespace(i);
            
            //-- skip XSI attributes
            if (XSI_NAMESPACE.equals(namespace)) {
                continue;
            }
                
            XMLFieldDescriptor descriptor = null;
            XMLClassDescriptor classDesc = null;
            //-- check for nested attribute...loop through
            //-- stack and find correct descriptor
            String path = state.getElementName();
            StringBuffer pathBuf = null;
            UnmarshalState targetState = null;
            while (_stateStack.hasAnotherParentState()) {
                targetState = _stateStack.removeParentState();
                if (targetState.isWrapper()) {
                    pathBuf = resetStringBuffer(pathBuf);
                    pathBuf.append(targetState.getElementName());
                    pathBuf.append('/');
                    pathBuf.append(path);
                    path = pathBuf.toString();
                    continue;
                }
                classDesc = targetState.getClassDescriptor();
                
                XMLFieldDescriptor[] descriptors = classDesc.getAttributeDescriptors();
                boolean found = false;
                for (int a = 0; a < descriptors.length; a++) {
                    descriptor = descriptors[a];
                    if (descriptor == null) {
                        continue;
                    }
                    if (descriptor.matches(name)) {
                        String tmpPath = descriptor.getLocationPath();
                        if (path.equals(StringUtils.defaultString(tmpPath))) {
                            found = true;
                            break;
                        }
                    }
                }
                if (found) {
                    _stateStack.resetParentState();
                    break;
                }
                        
                pathBuf = resetStringBuffer(pathBuf);
                pathBuf.append(targetState.getElementName());
                pathBuf.append('/');
                pathBuf.append(path);
                path = pathBuf.toString();
                
                //-- reset descriptor to make sure we don't
                //-- exit the loop with a reference to a 
                //-- potentially incorrect one.
                descriptor = null;
            }
            if (descriptor != null) {
                try {
                    processAttribute(name, namespace, atts.getValue(i),
                            descriptor, classDesc, targetState.getObject());
                } catch (IllegalStateException ise) {
                	String errorMsg = MessageFormat
        			.format(resourceBundle
        					.getString("unmarshalHandler.error.unable.add.attribute"),
        					new Object[] { name, state.getClassDescriptor().getJavaClass().getName(), ise });
                    throw new SAXException(errorMsg, ise);
                }
            }
        }
        
    }
    
    /**
     * Processes the given Attribute.
    **/
    private void processAttribute
        (final String attName, final String attNamespace, String attValue,
         XMLFieldDescriptor descriptor,
         final XMLClassDescriptor classDesc,
         Object parent) throws SAXException {

        //Object value = attValue;
        while (descriptor.isContainer()) {
            FieldHandler handler = descriptor.getHandler();
            Object containerObject = handler.getValue(parent);

            if (containerObject == null) {
                containerObject = handler.newInstance(parent);
                handler.setValue(parent, containerObject);
            }

            ClassDescriptor containerClassDesc = 
                ((XMLFieldDescriptorImpl) descriptor).getClassDescriptor();
            descriptor = ((XMLClassDescriptor) containerClassDesc).getFieldDescriptor(
                    attName, attNamespace, NodeType.Attribute);
            parent = containerObject;
        }

        if (attValue == null) {
             //-- Since many attributes represent primitive
             //-- fields, we add an extra validation check here
             //-- in case the class doesn't have a "has-method".
             if (descriptor.isRequired() && isValidating()) {
				String errorMsg;
				if (_locator != null) {
					errorMsg = MessageFormat
							.format(
									resourceBundle
											.getString("unmarshalHandler.error.attribute.missing.location"),
									new Object[] { classDesc.getXMLName(),
											attName, _locator.getLineNumber(),
											_locator.getColumnNumber() });
				} else {
					errorMsg = MessageFormat
							.format(
									resourceBundle
											.getString("unmarshalHandler.error.attribute.missing"),
									new Object[] { classDesc.getXMLName(),
											attName });
				}
                throw new SAXException(errorMsg);
            }
            return;
        }

        //-- if this is the identity then save id
        if (classDesc.getIdentity() == descriptor) {
            
            try {
                ((IDResolverImpl) _idResolver).bind(attValue, parent, 
                        isValidating() && !getInternalContext().getLenientIdValidation());
            } catch (ValidationException e) {
            	String errorMsg = MessageFormat
    			.format(resourceBundle
    					.getString("unmarshalHandler.error.duplicated.id"),
    					new Object[] { attValue });
                throw new SAXException(errorMsg, e);
            }

            //-- save key in current state
            UnmarshalState state = _stateStack.getLastState();
            state.setKey(attValue);

            //-- resolve waiting references
            resolveReferences(attValue, parent);
        } else if (descriptor.isReference()) {
            //-- if this is an IDREF(S) then resolve reference(s)
            if (descriptor.isMultivalued()) {
                StringTokenizer st = new StringTokenizer(attValue);
                while (st.hasMoreTokens()) {
                    processIDREF(st.nextToken(), descriptor, parent);
                }
            } else {
                processIDREF(attValue, descriptor, parent);
            }
            //-- object values have been set by processIDREF
            //-- simply return
            return;
        }
        
        //-- if it's a constructor argument, we can exit at this point
        //-- since constructor arguments have already been set
        if (descriptor.isConstructorArgument()) {
            return;
        }

        //-- attribute handler
        FieldHandler handler = descriptor.getHandler();
        if (handler == null) {
            return;
        }
        
        //-- attribute field type
        Class<?> type = descriptor.getFieldType();
        String valueType = descriptor.getSchemaType();
        boolean isPrimative = isPrimitive(type);
        boolean isQName = StringUtils.equals(valueType, QNAME_NAME);
        
        boolean isByteArray = false;
        if (type.isArray()) {
            isByteArray = (type.getComponentType() == Byte.TYPE);
        }
        
        //-- if this is an multi-value attribute
        if (descriptor.isMultivalued()) {
            StringTokenizer attrValueTokenizer = new StringTokenizer(attValue);
            while (attrValueTokenizer.hasMoreTokens()) {
                attValue = attrValueTokenizer.nextToken();
                setAttributeValueOnObject(attValue, descriptor, parent, handler,
                        type, isPrimative, isQName, isByteArray);
            }
        } else {
            setAttributeValueOnObject(attValue, descriptor, parent, handler,
                    type, isPrimative, isQName, isByteArray);
        }

    }

    /**
     * Sets the value of an attribute on the target object, using the {@link FieldHandler}
     * provided.
     * @param attValue The attribute value.
     * @param descriptor Corresponding {@link XMLFieldDescriptor} instance for the attribute processed.
     * @param parent Parent object into which attribute value needs to be 'injected'.
     * @param handler {@link FieldHandler} used for 'value injection'.
     * @param type {@link Class} type.
     * @param isPrimitive Indicates whether the attribute value represents a primitive value.
     * @param isQName Indicates whether the attribute value represents a QName value.
     * @param isByteArray Indicates whether the attribute value represents a byte array.
     * @throws SAXException If there's a problem 'injecting' the attribute value into the target field.
     */
    private void setAttributeValueOnObject(final String attValue,
            final XMLFieldDescriptor descriptor, 
            final Object parent, 
            final FieldHandler handler,
            final Class<?> type, 
            final boolean isPrimitive, 
            final boolean isQName,
            final boolean isByteArray) throws SAXException {
        //-- value to set
        Object value = attValue;
        //-- special type conversion for primitives
        if (isPrimitive) {
            value = toPrimitiveObject(type, attValue, descriptor);
        }
        
        //-- special byte[]s provessing, if required
        if (isByteArray) {
            if (attValue == null) {
                value = new byte[0];
            } else {
                //-- Base64/hexbinary decoding
                if (HexDecoder.DATA_TYPE.equals(descriptor.getComponentType())) {
                    value = HexDecoder.decode(attValue);
                } else {
                    value = Base64Decoder.decode(attValue);
                }
            }
        }
        
        //-- QName resolution (ns:value -> {URI}value), if required
        if (isQName) {
            value = _namespaceHandling.resolveNamespace(value);
        }
        //-- set value
        handler.setValue(parent, value);
    }

    /**
     * Processes the given attribute set, and creates the
     * constructor arguments.
     *
     * @param atts the AttributeSet to process
     * @param classDesc the XMLClassDescriptor of the objec
     * @return the array of constructor argument values.
     * @throws SAXException If there's a problem creating the constructor argument set. 
     */
    Arguments processConstructorArgs
        (final AttributeSet atts, final XMLClassDescriptor classDesc)
        throws SAXException {
        
        if (classDesc == null) {
            return new Arguments();
        }

        //-- Loop through Attribute Descriptors and build
        //-- the argument array

        //-- NOTE: Due to IDREF being able to reference an
        //-- un-yet unmarshalled object, we cannot handle
        //-- references as constructor arguments. 
        //-- kvisco - 20030421
        int count = 0;
        XMLFieldDescriptor[] descriptors = classDesc.getAttributeDescriptors();
        for (XMLFieldDescriptor fieldDescriptor : descriptors) {
            if (fieldDescriptor == null) {
                continue;
            }
            if (fieldDescriptor.isConstructorArgument()) {
                ++count;
            }
        }
        
        Arguments args = new Arguments();
        
        if (count == 0) {
            return args;
        }
        
        args.setValues(new Object[count]);
        args.setTypes(new Class[count]);
        
        for (XMLFieldDescriptor descriptor : descriptors) {
            
            if (descriptor == null) {
                continue;
            }
            if (!descriptor.isConstructorArgument()) {
                continue;
            }
            
            int argIndex = descriptor.getConstructorArgumentIndex();
            if (argIndex >= count) {
            	String errorMsg = MessageFormat
    			.format(resourceBundle
    					.getString("unmarshalHandler.error.index.out.of.bound"),
    					new Object[] { argIndex });
                throw new SAXException(errorMsg);
            }

            args.setType(argIndex, descriptor.getFieldType());
            String name = descriptor.getXMLName();
            String namespace = descriptor.getNameSpaceURI();

            int index = atts.getIndex(name, namespace);

            if (index >= 0) {
                Object value = atts.getValue(index);
                //-- check for proper type and do type
                //-- conversion
                if (isPrimitive(args.getType(argIndex))) {
                    value = toPrimitiveObject(args.getType(argIndex), (String) value, descriptor);
                } else {
                    // check whether we are looking at an enum-style object, and if so,
                    // convert the (string) value
                    value = convertToEnumObject(descriptor, value);
                }
                
                //check if the value is a QName that needs to
                //be resolved (ns:value -> {URI}value)
                String valueType = descriptor.getSchemaType();
                if (StringUtils.equals(valueType, QNAME_NAME)) {
                        value = _namespaceHandling.resolveNamespace(value);
                }
                args.setValue(argIndex, value);
            } else {
                if (isPrimitive(args.getType(argIndex))) {
                    args.setValue(argIndex, toPrimitiveObject(args.getType(argIndex), null, descriptor));
                } else {
                    args.setValue(argIndex, null);
                }
            }
        }
        return args;
    }

    /**
     * Checks whether the actual value passed in should be converted to an enum-style class
     * instance.
     * 
     * @param descriptor The {@link XMLFieldDescriptor} instance in question.
     * @param value The actual value (which might need conversion).
     * @return The value, potentially converted to an enum-style class.
     */
    private Object convertToEnumObject(final XMLFieldDescriptor descriptor, Object value) {
        Class<?> fieldType = descriptor.getFieldType();
        Method valueOfMethod;
        try {
            valueOfMethod = fieldType.getMethod("valueOf", new Class[] {String.class});
            if (valueOfMethod != null 
                    && Modifier.isStatic(valueOfMethod.getModifiers())) {
                Class<?> returnType = valueOfMethod.getReturnType();
                if (returnType.isAssignableFrom(fieldType)) {
                    Object enumObject = valueOfMethod.invoke(null, new Object[] {value});
                    value = enumObject;
                }
            }
        } catch (SecurityException e) {
            // TODO: well, cannot do anything about it 
        } catch (NoSuchMethodException e) {
            // TODO: nothing to do, as it simply isn't an enum-style class
        } catch (IllegalArgumentException e) {
            // TODO: cannot really happen
        } catch (IllegalAccessException e) {
            // TODO: indicates that the valueOf() method isn't public
        } catch (InvocationTargetException e) {
            // TODO: hmm .. what else
        }
        return value;
    }

    /**
     * Processes the given IDREF.
     *
     * @param idRef the ID of the object in which to reference
     * @param descriptor the current FieldDescriptor
     * @param parent the current parent object
     * @return true if the ID was found and resolved properly
     */
    boolean processIDREF (final String idRef, final XMLFieldDescriptor descriptor, 
            final Object parent) {
        Object value = _idResolver.resolve(idRef);
        if (value == null) {
            //-- save state to resolve later
            addReference(idRef, parent, descriptor);
        } else {
            FieldHandler handler = descriptor.getHandler();
            if (handler != null) {
                handler.setValue(parent, value);
            }
        }
        return (value != null);
    }

    /**
     * Finds and returns an XMLClassDescriptor for the given class name.
     * If a ClassDescriptor could not be found one will attempt to
     * be generated.
     * @param className the name of the class to find the descriptor for
    **/
    private XMLClassDescriptor getClassDescriptor (String className)
        throws SAXException
    {
        Class<?> type = null;
        try {
            //-- use specified ClassLoader if necessary
		    if (_loader != null) {
		        type = _loader.loadClass(className);
		    }
		    //-- no loader available use Class.forName
		    else type = Class.forName(className);
		}
		catch (ClassNotFoundException cnfe) {
		    return null;
		}
        return getClassDescriptor(type);

    } //-- getClassDescriptor

    /**
     * Finds and returns an XMLClassDescriptor for the given class. If
     * a ClassDescriptor could not be found one will attempt to
     * be generated.
     * @param cls the Class to get the ClassDescriptor for
    **/
    XMLClassDescriptor getClassDescriptor(final Class<?> cls)
    throws SAXException {
        if (cls == null) { return null; }


        //-- special case for strings
        if (cls == String.class) { return STRING_DESCRIPTOR; }

        if (cls.isArray()) { return null; }
        if (isPrimitive(cls)) { return null; }

// TODO Joachim
//        if (_cdResolver == null)
//            _cdResolver = (XMLClassDescriptorResolver) 
//                ClassDescriptorResolverFactory.createClassDescriptorResolver(BindingType.XML);

        XMLClassDescriptor classDesc = null;

        try {
            InternalContext ctx = getInternalContext();
            classDesc = (XMLClassDescriptor) ctx.getXMLClassDescriptorResolver().resolve(cls);
        } catch (ResolverException rx) {
            // TODO
        }

        if (classDesc != null) {
            return new InternalXMLClassDescriptor(classDesc);
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug(ERROR_DID_NOT_FIND_CLASSDESCRIPTOR + cls.getName());
        }
        
        return classDesc;
    }


    /**
     * Finds and returns a ClassDescriptor for the given class. If
     * a ClassDescriptor could not be found one will attempt to
     * be generated.
     * @param className the name of the class to get the Descriptor for
    **/
    XMLClassDescriptor getClassDescriptor
        (String className, ClassLoader loader)
        throws SAXException
    {
// TODO: Joachim
//        if (_cdResolver == null)
//            _cdResolver = (XMLClassDescriptorResolver) 
//                ClassDescriptorResolverFactory.createClassDescriptorResolver(BindingType.XML);

        
        XMLClassDescriptor classDesc = null;
        try {
            classDesc = getInternalContext().getXMLClassDescriptorResolver().resolve(className, loader);
        }
        catch(ResolverException rx) {
            throw new SAXException(rx);
        }
        

        if (classDesc != null) {
            return new InternalXMLClassDescriptor(classDesc);
        }

        if (LOG.isDebugEnabled()) {
        	LOG.debug(ERROR_DID_NOT_FIND_CLASSDESCRIPTOR + className);
        }
        
        return classDesc;
    } //-- getClassDescriptor
    
    /**
     * Returns the XMLClassLoader
     */
    XMLClassDescriptor resolveByXMLName
        (String name, String namespace, ClassLoader loader) 
        throws SAXException
    {
        
        try {
            return getInternalContext().getXMLClassDescriptorResolver().resolveByXMLName(name, namespace, loader);
        }
        catch(ResolverException rx) {
            throw new SAXException(rx);
        }
        
    }

    /**
     * Returns the package for the given Class
     *
     * @param type the Class to return the package of
     * @return the package for the given Class
    **/
	String getJavaPackage(Class<?> type)
	{
		if (type == null)
			return null;
		String pkg = _javaPackages.get(type);
		if(pkg == null)
		{
			pkg = type.getName();
			int idx = pkg.lastIndexOf('.');
			if (idx > 0)
			pkg = pkg.substring(0,idx);
			else
			pkg = "";
			_javaPackages.put(type, pkg);
		}
		return pkg;
	} //-- getJavaPackage

    /**
     * Returns the name of a class, handles array types
     * @return the name of a class, handles array types
    **/
    String className(Class<?> type) {
        if (type.isArray()) {
            return className(type.getComponentType()) + "[]";
        }
        return type.getName();
    } //-- className


    /**
     * Checks the given StringBuffer to determine if it only
     * contains whitespace.
     *
     * @param sb the StringBuffer to check
     * @return true if the only whitespace characters were
     * found in the given StringBuffer
    **/
    static boolean isWhitespace(StringBuffer sb) {
        for (int i = 0; i < sb.length(); i++) {
            char ch = sb.charAt(i);
            switch (ch) {
                case ' ':
                case '\n':
                case '\t':
                case '\r':
                    break;
                default:
                    return false;
            }
        }
        return true;
    } //-- isWhitespace
    
    /**
     * Loads and returns the class with the given class name using the
     * given loader.
     * @param className the name of the class to load
     * @param loader the ClassLoader to use, this may be null.
    **/
    Class<?> loadClass(String className, ClassLoader loader)
        throws ClassNotFoundException
    {
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
     * Resolves the current set of waiting references for the given Id.
     * @param id the id that references are waiting for.
     * @param value the value of the resolved id.
     * @throws SAXException Indicates a problem resolving an IDREF
    **/
    private void resolveReferences(final String id, final Object value)
        throws org.xml.sax.SAXException {
        if ((id == null) || (value == null)) {
            return;
        }
        if (_resolveTable == null) {
            return;
        }

        ReferenceInfo refInfo = _resolveTable.remove(id);
        while (refInfo != null) {
            try {
                FieldHandler handler = refInfo.getDescriptor().getHandler();
                if (handler != null) {
                    handler.setValue(refInfo.getTarget(), value);
                }
                    
                //-- special handling for MapItems
                if (refInfo.getTarget() instanceof MapItem) {
                    resolveReferences(refInfo.getTarget().toString(), refInfo.getTarget());
                }
            } catch (java.lang.IllegalStateException ise) {
            	String errorMsg = MessageFormat
    			.format(resourceBundle
    					.getString("unmarshalHandler.error.resolving.idRef"),
    					new Object[] { id, ise.toString() });
                throw new SAXException(errorMsg, ise);
            }
            refInfo = refInfo.getNext();
        }
    }

    /**
     * Converts a String to the given primitive object type.
     *
     * @param type the class type of the primitive in which
     * to convert the String to
     * @param value the String to convert to a primitive
     * @param fieldDesc Descriptor for the given field (value)
     * @return the new primitive Object
     * @exception SAXException If the String cannot be converted to a primitive object type
     */
    Object toPrimitiveObject
        (final Class<?> type, final String value, final XMLFieldDescriptor fieldDesc) 
        throws SAXException {
        try {
            return toPrimitiveObject(type, value);
        } catch (Exception ex) {
            UnmarshalState state = _stateStack.getLastState();
            if (state != null) {
                if (state.getObject() != null) {
					String errorMsg = MessageFormat
							.format(
									resourceBundle
											.getString("unmarshalHandler.error.unmarshal.field.of.class"),
									new Object[] { fieldDesc.getFieldName(),
											state.getObject().getClass().getName() });
					throw new SAXException(errorMsg, ex);
                }
            }
			String errorMsg = MessageFormat.format(resourceBundle
					.getString("unmarshalHandler.error.unmarshal.field"),
					new Object[] { fieldDesc.getFieldName() });
			throw new SAXException(errorMsg, ex);
        }
    }


    /**
     * Converts a {@link String} to the given primitive object type.
     *
     * @param type the class type of the primitive in which
     * to convert the String to
     * @param value the {@link String} to convert to a primitive
     * @return the new primitive {@link Object}
     */
    public static Object toPrimitiveObject(final Class<?> type, String value) {
		return PrimitiveObjectFactory.getInstance().getObject(type, value);
    }
    
    /**
     * Internal class used for passing constructor argument
     * information.
     */
    class Arguments {
        /**
         * Constructor argument values.
         */
        private Object[] _values = null;
        /**
         * Constructor argument types.
         */
        private Class<?>[] _types  = null;
        
        /**
         * Returns the number of constructor arguments.
         * @return The number of constructor arguments.
         */
        public int size() {
            if (_values == null) {
                return 0;
            }
            return _values.length;
        }

        public Class<?>[] getTypes() {
            return _types;
        }

        public Object[] getValues() {
            return _values;
        }

        public Class<?> getType(int index) {
            return _types[index];
        }
        
        public void setValues(Object[] values) {
            _values = values;
        }

        public void setValue(int index, Object value) {
            _values[index] = value;
        }

        public void setTypes(Class<?>[] types) {
            _types = types;
        }

        public void setType(int index, Class<?> type) {
            _types[index] = type;
        }

    }

    /**
     * A class for handling Arrays during unmarshalling.
     *
     * @author <a href="mailto:kvisco@intalio.com">kvisco@intalio.com</a>
     */
    public static class ArrayHandler {
        
        Class<?> _componentType = null;
        
        ArrayList<Object> _items = null;
        
        /**
         * Creates a new ArrayHandler 
         *
         * @param componentType the ComponentType for the array.
         */
        ArrayHandler(final Class<?> componentType) {
            if (componentType == null) {
                String errMsg = resourceBundle.getString("unmarshalHandler.error.componentType.null");
                throw new IllegalArgumentException(errMsg);
            }
            _componentType = componentType;
            _items = new ArrayList<Object>();
        } //-- ArrayHandler
        
        /**
         * Adds the given object to the underlying array. 
         * @param obj The object to be added to the underlying array.
         */
        public void addObject(final Object obj) {
            if (obj == null) {
                return;
            }
            /* disable check for now until we write a 
               small function to handle primitive and their
               associated wrapper classes
            if (!_componentType.isAssignableFrom(obj.getClass())) {
                String err = obj.getClass().getName() + " is not an instanceof " +
                    _componentType.getName();
                throw new IllegalArgumentException(err);
            }
            */
            _items.add(obj);
        }
        
        /**
         * Returns the data handled by this class as an array.
         * @return The data handled internally in the form of an array.
         */
        public Object getObject() {
            int size = _items.size();
            Object array = Array.newInstance(_componentType, size);
            for (int i = 0; i < size; i++) {
                Array.set(array, i, _items.get(i));
            }
            return array;
        }
       
        /**
         * Returns the component type handled by this class.
         * @return The component type handled by this class.
         */
        public Class<?> componentType() {
            return _componentType;
        }
        
    } //-- ArrayHandler

	/**
     * Returns the ObjectFactory instance in use.
	 * @return the ObjectFactory instance in use.
	 */
	public ObjectFactory getObjectFactory() {
		return _objectFactory;
	}

	/**
     * Sets a (custom) ObjectFactory instance.
	 * @param objectFactory A (custom) ObjectFactory instance
	 */
	public void setObjectFactory(ObjectFactory objectFactory) {
		_objectFactory = objectFactory;
	}

	/**
	 * Returnss a refrence to the {@link UnmarshalStateStack} instance currently in use.
	 * @return The {@link UnmarshalStateStack} in use.
	 */
	public UnmarshalStateStack getStateStack() {
	    return _stateStack;
	}

    /**
     * Returns the top {@link UnmarshalState} instance from the {@link UnmarshalStateStack}.
     * @return The top {@link UnmarshalState} instance.
     */
    public UnmarshalState getTopState() {
        return _topState;
    }

    /**
     * Returns the {@link StrictElementHandler} in use.
     * @return The {@link StrictElementHandler} in use.
     */
    public StrictElementHandler getStrictElementHandler() {
        return _strictElementHandler;
    }

    /**
     * Returns the {@link NamespaceHandling} in use.
     * @return The currently active {@link NamespaceHandling} instance.
     */
    public NamespaceHandling getNamespaceHandling() {
        return _namespaceHandling;
    }

    /**
     * Returns the current {@link ClassLoader} in use.
     * @return The {@link ClassLoader} in use.
     */
    public ClassLoader getClassLoader() {
        return _loader;
    }

    /**
     * Returns the currently used {@link AnyNodeUnmarshalHandler} instance.
     * @return The {@link AnyNodeUnmarshalHandler} in use.
     */
    public AnyNodeUnmarshalHandler getAnyNodeHandler() {
        return _anyNodeHandler;
    }

    /**
     * Returns the currently active {@link UnmarshalListenerDelegate} instance
     * @return The active {@link UnmarshalListenerDelegate} in use.
     */
    public UnmarshalListenerDelegate getDelegateUnmarshalListener() {
        return _delegateUnmarshalListener;
    }

    /**
     * Indicats whether Object instances should be re-used. 
     * @return True if object instances should be re-used.
     */
    public boolean isReuseObjects() {
        return _reuseObjects;
    }

    /**
     * Hashtable to store idReference and ReferenceInfo
     * @return Hashtable
     */
	public Hashtable<String, ReferenceInfo> getResolveTable() {
		return _resolveTable;
	}

	/**
	 * returns the AnyNode (if any).
	 * @return AnyNode, could be null
	 */
	public org.exolab.castor.types.AnyNode getAnyNode() {
		return _node;
	}
	
	/**
	 * sets the AnyNode
	 * @param node AnyNode
	 */
	public void setAnyNode(org.exolab.castor.types.AnyNode node) {
		_node = node;
	}

	/**
	 * Indicates whether it's necessary to clear any collection or not.
	 * @return True if it's necessary to clear any collection.
	 */
	public boolean isClearCollections() {
		return _clearCollections;
	}
	
	
	
}

