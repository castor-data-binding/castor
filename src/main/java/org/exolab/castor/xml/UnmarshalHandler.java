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

//-- Castor imports
import org.castor.mapping.BindingType;
import org.castor.util.Base64Decoder;
import org.exolab.castor.util.Configuration;
import org.exolab.castor.util.ObjectFactory;
import org.exolab.castor.util.DefaultObjectFactory;
import org.exolab.castor.xml.descriptors.PrimitivesClassDescriptor;
import org.exolab.castor.xml.descriptors.StringClassDescriptor;
import org.exolab.castor.xml.util.*;
import org.exolab.castor.mapping.ClassDescriptor;
import org.exolab.castor.mapping.ExtendedFieldHandler;
import org.exolab.castor.mapping.FieldHandler;
import org.exolab.castor.mapping.MapItem;
import org.exolab.castor.mapping.loader.FieldHandlerImpl;

//-- xml related imports
import org.xml.sax.*;

import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;

import java.lang.reflect.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Stack;
import java.util.Enumeration;
import java.util.StringTokenizer;

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
     * Attribute prefix for prefixed namespace declaration
    **/
    private final static String XMLNS_PREFIX        = "xmlns:";
    private final static int    XMLNS_PREFIX_LENGTH = XMLNS_PREFIX.length();

    /**
     * The type attribute (xsi:type) used to denote the
     * XML Schema type of the parent element
    **/
    private static final String XSI_TYPE = "type";
    
    private static final String XML_SPACE = "space";
    private static final String XML_SPACE_WITH_PREFIX = "xml:space";
    private static final String PRESERVE = "preserve";

    //----------------------------/
    //- Private Member Variables -/
    //----------------------------/

    private Stack            _stateInfo    = null;
    private UnmarshalState   _topState     = null;
    private Class            _topClass     = null;

    /**
     * The top-level instance object, this may be set by the user
     * by calling #setRootObject();
    **/
    private Object           _topObject    = null;

    /**
     * A StringBuffer used to created Debug/Log messages
    **/
    //private StringBuffer     buf           = null;


    /**
     * Indicates whether or not collections should be cleared
     * upon first use (to remove default values, or old values).
     * False by default for backward compatibility.
     */
    private boolean          _clearCollections = false;

    /**
     * The Castor configuration
     */
    private Configuration    _config       = null;
    
    /**
     * A flag to indicate whether or not to generate debug information
    **/
    private boolean          debug         = false;

    /**
     * A flag to indicate we need to kill the _logWriter when
     * debug mode is false
    **/
    private boolean          killWriter    = false;

    /**
     * The SAX Document Locator
    **/
    private Locator          _locator      = null;

    /**
     * The PrintWriter to print log information to
    **/
    private PrintWriter      _logWriter    = null;

    /**
     * The ClassDescriptorResolver which is used to "resolve"
     * or find ClassDescriptors
    **/
    private XMLClassDescriptorResolver _cdResolver = null;

    /**
     * The IDResolver for resolving IDReferences
    **/
    private IDResolverImpl _idResolver = null;

   /**
    * The unmarshaller listener
    */
    private UnmarshalListener _unmarshalListener = null;
    
    /**
     * A flag indicating whether or not to perform validation
     **/
    private boolean _validate = true;

    private Hashtable _resolveTable = null;
    
	private HashMap _javaPackages = null;    

    private ClassLoader _loader = null;

    private static final StringClassDescriptor _stringDescriptor
        = new StringClassDescriptor();

    /**
     * A SAX2ANY unmarshaller in case we are dealing with {@literal <any>}
     */
     private SAX2ANY _anyUnmarshaller = null;

    /**
     * The any branch depth
     */
    private int _depth = 0;

    /**
     * The AnyNode to add (if any)
     */
     private org.exolab.castor.types.AnyNode _node = null;

    /**
     * The namespace stack
     */
    private Namespaces _namespaces = null;

    /**
     * A map of namespace URIs to Package Names
     */
    private HashMap _namespaceToPackage = null;

    /**
     * A reference to the ObjectFactory used to create instances
     * of the classes if the FieldHandler is not used.
     */
    private ObjectFactory _objectFactory = new DefaultObjectFactory();
    
    /**
     * A boolean to indicate that objects should
     * be re-used where appropriate
    **/
    private boolean _reuseObjects = false;

    /**
     * A boolean that indicates attribute processing should
     * be strict and an error should be flagged if any
     * extra attributes exist.
    **/
    private boolean _strictAttributes = false;

    /**
     * A boolean that indicates element processing should
     * be strict and an error should be flagged if any
     * extra elements exist
    **/
    private boolean _strictElements = true;

    /**
     * A depth counter that increases as we skip elements ( in startElement )
     * and decreases as we process and endElement. Only active if _strictElemnts
     */
    private int     _ignoreElementDepth = 0;

    /**
     * A flag to keep track of when a new namespace scope is needed
     */
    private boolean _createNamespaceScope = true;
    
    /**
     * Keeps track of the current element information
     * as passed by the parser
     */
    private ElementInfo _elemInfo = null;
    
    /**
     * A "reusable" AttributeSet, for use when handling
     * SAX 2 ContentHandler
     */
    private AttributeSetImpl _reusableAtts = null;
    
    /**
     * The top-level xml:space value
     */
    private boolean _wsPreserve = false;
    
    
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
    } //-- UnmarshalHandler

    /**
     * Creates a new UnmarshalHandler
     * 
     * @param _class the Class to create the UnmarshalHandler for
     */
    protected UnmarshalHandler(Class _class) {
        super();
        _stateInfo          = new Stack();
        _idResolver         = new IDResolverImpl();
		_javaPackages 		= new HashMap();        
        _topClass           = _class;
        _namespaces         = new Namespaces();
        _namespaceToPackage = new HashMap();
    } //-- UnmarshalHandler(Class)
    
    /**
     * Adds a mapping from the given namespace URI to the given
     * package name
     * 
     * @param nsURI the namespace URI to map from
     * @param packageName the package name to map to
     */
    public void addNamespaceToPackageMapping(String nsURI, String packageName) 
    {
        if (nsURI == null) nsURI = "";
        if (packageName == null) packageName = "";
    	_namespaceToPackage.put(nsURI, packageName);
        
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
        if (!_stateInfo.isEmpty()) {
            UnmarshalState state = (UnmarshalState)_stateInfo.peek();
            if (state != null)  {
                return state.object;
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
        if (_topState != null) return _topState.object;
        return null;
    } //-- getObject



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
     * Turns debuging on or off. If no Log Writer has been set, then
     * System.out will be used to display debug information
     * @param debug the flag indicating whether to generate debug information.
     * A value of true, will turn debuggin on.
     * @see #setLogWriter
    **/
    public void setDebug(boolean debug) {
        this.debug = debug;

        if (this.debug && (_logWriter == null)) {
            _logWriter = new PrintWriter(System.out, true);
            killWriter = true;
        }
        if ((!this.debug) && killWriter) {
            _logWriter = null;
            killWriter = false;
        }
    } //-- setDebug

    /**
     * Sets the IDResolver to use when resolving IDREFs for
     * which no associated element may exist in XML document.
     *
     * @param idResolver the IDResolver to use when resolving
     * IDREFs for which no associated element may exist in the
     * XML document.
    **/
    public void setIDResolver(IDResolver idResolver) {
        _idResolver.setResolver(idResolver);
    } //-- setIdResolver


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
        _strictElements = (!ignoreExtraElems);
    } //-- setIgnoreExtraElements


    /**
     * Sets the PrintWriter used for printing log messages
     * @param printWriter the PrintWriter to use when printing
     * log messages
    **/
    public void setLogWriter(PrintWriter printWriter) {
        this._logWriter = printWriter;
        killWriter = false;
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

    /**
     * Sets the ClassDescriptorResolver to use for loading and
     * resolving ClassDescriptors
     *
     * @param cdResolver the ClassDescriptorResolver to use
    **/
    public void setResolver(XMLClassDescriptorResolver cdResolver) {
        this._cdResolver = cdResolver;
    } //-- setResolver

    /**
     * Sets the root (top-level) object to use for unmarshalling into.
     *
     * @param root the instance to unmarshal into.
    **/
    public void setRootObject(Object root) {
        _topObject = root;
    } //-- setRootObject

    /**
     * Sets an {@link UnmarshalListener}.
     *
     * @param listener the UnmarshalListener to use with this instance
     * of the UnmarshalHandler.
    **/
    public void setUnmarshalListener (UnmarshalListener listener) {
        _unmarshalListener = listener;
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
        throws SAXException
    {
        if (debug) {
            StringBuffer sb = new StringBuffer(21 + length);
            sb.append("#characters: ");
            sb.append(ch, start, length);
            message(sb.toString());
        }
        
        //-- If we are skipping elements that have appeared in the XML but for
        //-- which we have no mapping, skip the text and return
        if ( _ignoreElementDepth > 0) {
            return;
        }

        if (_stateInfo.empty()) {
            return;
        }
        if (_anyUnmarshaller != null)
           _anyUnmarshaller.characters(ch, start, length);
        else {
             UnmarshalState state = (UnmarshalState)_stateInfo.peek();
             //-- handle whitespace
             boolean removedTrailingWhitespace = false;
             boolean removedLeadingWhitespace = false;
             if (!state.wsPreserve) {
                //-- trim leading whitespace characters
                while (length > 0) {
                    boolean whitespace = false;
                    switch(ch[start]) {
                        case ' ':
                        case '\r':
                        case '\n':
                        case '\t':
                        	whitespace = true;
                            break;
                        default:
                            break;
                    }
                    if (!whitespace) break;
                    removedLeadingWhitespace = true;
                    ++start;
                    --length;
                }
                
                if (length == 0) {
                    //-- we also need to mark trailing whitespace removed
                    //-- when we received only whitespace characters
                    removedTrailingWhitespace = removedLeadingWhitespace;
                }
                else {
                    //-- trim trailing whitespace characters
                    while (length > 0) {
                        boolean whitespace = false;
                        switch(ch[start+length-1]) {
                            case ' ':
                            case '\r':
                            case '\n':
                            case '\t':
                                whitespace = true;
                                break;
                            default:
                                break;
                        }
                        if (!whitespace) break;
                        removedTrailingWhitespace = true;
                        --length;
                    }
                }
             }
             
             if (state.buffer == null) state.buffer = new StringBuffer();
             else {
                //-- non-whitespace content exists, add a space
                if ((!state.wsPreserve) && (length > 0)) {
                	if (state.trailingWhitespaceRemoved || removedLeadingWhitespace)
                    {
                		state.buffer.append(' ');
                    }
                }
             }
             state.trailingWhitespaceRemoved = removedTrailingWhitespace;
             state.buffer.append(ch, start, length);
        }
    } //-- characters


    public void endDocument()
        throws org.xml.sax.SAXException
    {
        //-- I've found many application don't always call
        //-- #endDocument, so I usually never put any
        //-- important logic here

    } //-- endDocument


    public void endElement(String name)
        throws org.xml.sax.SAXException
    {
        
        
        if (debug) {
            message("#endElement: " + name);
        }
        
        //-- If we are skipping elements that have appeared in the XML but for
        //-- which we have no mapping, decrease the ignore depth counter and return
        if ( _ignoreElementDepth > 0) {
            --_ignoreElementDepth;
            return;
        }

        //-- Do delagation if necessary
        if (_anyUnmarshaller != null) {
            _anyUnmarshaller.endElement(name);
            --_depth;
            //we are back to the starting node
            if (_depth == 0) {
               _node = _anyUnmarshaller.getStartingNode();
               _anyUnmarshaller = null;
            }
            else return;
        }

        if (_stateInfo.empty()) {
            throw new SAXException("missing start element: " + name);
        }

        //-- * Begin Namespace Handling
        //-- XXX Note: This code will change when we update the XML event API

         int idx = name.indexOf(':');
         if (idx >= 0) {
             name = name.substring(idx+1);
         }
        //-- * End Namespace Handling

        UnmarshalState state = (UnmarshalState) _stateInfo.pop();

        //-- make sure we have the correct closing tag
        XMLFieldDescriptor descriptor = state.fieldDesc;
        
        if (!state.elementName.equals(name)) {
            
            //maybe there is still a container to end
            if (descriptor.isContainer()) {
                _stateInfo.push(state);
                //-- check for possible characters added to
                //-- the container's state that should
                //-- really belong to the parent state
                StringBuffer tmpBuffer = null;
                if (state.buffer != null) {
                    if (!isWhitespace(state.buffer)) {
                        if (state.classDesc.getContentDescriptor() == null) {
                            tmpBuffer = state.buffer;
                            state.buffer = null;
                        }
                    }
                }
                //-- end container
                endElement(state.elementName);
                
                if (tmpBuffer != null) {
                    state = (UnmarshalState) _stateInfo.peek();
                    if (state.buffer == null)
                        state.buffer = tmpBuffer;
                    else
                        state.buffer.append(tmpBuffer.toString());
                }
                endElement(name);
                return;
            }
            String err = "error in xml, expecting </" + state.elementName;
            err += ">, but received </" + name + "> instead.";
            throw new SAXException(err);
        }
        
        
        //-- clean up current Object
        Class type = state.type;

        if ( type == null ) {
            if (!state.wrapper) {
                //-- this message will only show up if debug
                //-- is turned on...how should we handle this case?
                //-- should it be a fatal error?
                message("Ignoring " + state.elementName + " no descriptor was found");
            }
            
            //-- handle possible location text content
            //-- TODO: cleanup location path support.
            //-- the following code needs to be improved as 
            //-- for searching descriptors in this manner can
            //-- be slow
            StringBuffer tmpBuffer = null;
            if (state.buffer != null) {
                if (!isWhitespace(state.buffer)) {
                    tmpBuffer = state.buffer;
                    state.buffer = null;
                }
            }
            if (tmpBuffer != null) {
                UnmarshalState targetState = state;
                String locPath = targetState.elementName;
                while ((targetState = targetState.parent) != null) {
                    if ((targetState.wrapper) || 
                        (targetState.classDesc == null))
                    {
                        locPath = targetState.elementName + "/" + locPath;
                        continue;
                    }
                    
                    XMLFieldDescriptor tmpDesc = targetState.classDesc.getContentDescriptor();
                    if (tmpDesc != null && locPath.equals(tmpDesc.getLocationPath())) {
                        if (targetState.buffer == null)
                            targetState.buffer = tmpBuffer;
                        else
                            targetState.buffer.append(tmpBuffer.toString());
                    }
                }
            }
            
            //-- remove current namespace scoping
            _namespaces = _namespaces.getParent();
            return;
        }

        //-- check for special cases
        boolean byteArray = false;

        if (type.isArray())
            byteArray = (type.getComponentType() == Byte.TYPE);

        //-- If we don't have an instance object and the Class type
        //-- is not a primitive or a byte[] we must simply return
        if ((state.object == null) && (!state.primitiveOrImmutable)) {
            //-- remove current namespace scoping
            _namespaces = _namespaces.getParent();
            return;
        }
        
        /// DEBUG System.out.println("end: " + name);

        if (state.primitiveOrImmutable) {
            
            String str = null;

            if (state.buffer != null) {
                str = state.buffer.toString();
                state.buffer.setLength(0);
            }

            if (type == String.class) {
                if (str != null)
                    state.object = str;
                else if (state.nil) {
                    state.object = null;
                }
                else {
                    state.object = "";
                }
            }
            //-- special handling for byte[]
            else if (byteArray) {
                if (str == null)
                    state.object = new byte[0];
                else {
                    //-- Base64 decoding
                    state.object = Base64Decoder.decode(str);
                }
            }
            else if (state.args != null) {
            	state.object = createInstance(state.type, state.args);
            }
            else state.object = toPrimitiveObject(type,str,state.fieldDesc);
        }
        else if (ArrayHandler.class.isAssignableFrom(state.type)) {
            state.object = ((ArrayHandler)state.object).getObject();
            state.type = state.object.getClass();
            
        }

        //-- check for character content
        if ((state.buffer != null) &&
            (state.buffer.length() > 0) &&
            (state.classDesc != null)) {
            XMLFieldDescriptor cdesc = state.classDesc.getContentDescriptor();
            if (cdesc != null) {
                Object value = state.buffer.toString();
                if (isPrimitive(cdesc.getFieldType()))
                    value = toPrimitiveObject(cdesc.getFieldType(), (String)value, state.fieldDesc);
                else {
                    Class valueType = cdesc.getFieldType();
                    //-- handle base64
                    if (valueType.isArray()
                            && (valueType.getComponentType() == Byte.TYPE)) {
                        value = Base64Decoder.decode((String) value);
                    }
                }


                try {
                    FieldHandler handler = cdesc.getHandler();
                    boolean addObject = true;
                    if (_reuseObjects) {
                        //-- check to see if we need to
                        //-- add the object or not
                        Object tmp = handler.getValue(state.object);
                        if (tmp != null) {
                            //-- Do not add object if values
                            //-- are equal
                            addObject = (!tmp.equals(value));
                        }
                    }
                    if (addObject) handler.setValue(state.object, value);
                }
                catch(java.lang.IllegalStateException ise) {
                    String err = "unable to add text content to ";
                    err += descriptor.getXMLName();
                    err += " due to the following error: " + ise;
                    throw new SAXException(err, ise);
                }
            }
            //-- Handle references
            else if (descriptor.isReference()) {
                UnmarshalState pState = (UnmarshalState)_stateInfo.peek();
                processIDREF(state.buffer.toString(), descriptor, pState.object);
                _namespaces = _namespaces.getParent();
                return;
            }
            else {
                //-- check for non-whitespace...and report error
                if (!isWhitespace(state.buffer)) {
                    String err = "Illegal Text data found as child of: "
                        + name;
                    err += "\n  value: \"" + state.buffer + "\"";
                    throw new SAXException(err);
                }
            }
        }
        
        //-- We're finished processing the object, so notify the
        //-- Listener (if any).
        if ( _unmarshalListener != null && state.object != null ) {
            _unmarshalListener.unmarshalled(state.object);
        }

        //-- if we are at root....just validate and we are done
         if (_stateInfo.empty()) {
             if (isValidating()) {
                ValidationException first = null;
                ValidationException last = null;
                
                //-- check unresolved references
                if (_resolveTable != null) {
                    Enumeration enumeration = _resolveTable.keys();
                    while (enumeration.hasMoreElements()) {
                        Object ref = enumeration.nextElement();
                        //if (ref.toString().startsWith(MapItem.class.getName())) continue;
                        String msg = "unable to resolve reference: " + ref;                        
                        if (first == null) {
                            first = new ValidationException(msg);
                            last = first;
                        }
                        else {
                            last.setNext(new ValidationException(msg));
                            last = last.getNext();
                        }                            
                    }
                }
                try {
                    Validator validator = new Validator();
                    ValidationContext context = new ValidationContext();
                    context.setResolver(_cdResolver);
                    context.setConfiguration(_config);
                    validator.validate(state.object, context);
                    validator.checkUnresolvedIdrefs(context);
                    context.cleanup();
                }
                catch(ValidationException vEx) {
                    if (first == null)
                        first = vEx;
                    else 
                        last.setNext(vEx);
                }
                if (first != null) {
                    throw new SAXException(first);
                }
            }
            return;
        }
         
        //-- Add object to parent if necessary

        if (descriptor.isIncremental()) {
            //-- remove current namespace scoping
           _namespaces = _namespaces.getParent();
           return; //-- already added
        }

        Object val = state.object;
        
        //--special code for AnyNode handling
        if (_node != null) {
           val = _node;
           _node = null;
        }

        //-- save fieldState
        UnmarshalState fieldState = state;

        //-- have we seen this object before?
        boolean firstOccurance = false;

        //-- get target object
        state = (UnmarshalState) _stateInfo.peek();
        if (state.wrapper) {
            state = fieldState.targetState;
        }
        
        //-- check to see if we have already read in
        //-- an element of this type. 
        //-- (Q: if we have a container, do we possibly need to 
        //--     also check the container's multivalued status?)
        if ( ! descriptor.isMultivalued() ) {

            if (state.isUsed(descriptor)) {
                
                String err = "element \"" + name;
                err += "\" occurs more than once. (parent class: " + state.type.getName() + ")";
                
                String location = name;
                while (!_stateInfo.isEmpty()) {
                    UnmarshalState tmpState = (UnmarshalState)_stateInfo.pop();
                    if (!tmpState.wrapper) {
                        if (tmpState.fieldDesc.isContainer()) continue;
                    }
                    location = state.elementName + "/" + location;
                }
                
                err += "\n location: /" + location;
                
                ValidationException vx =
                    new ValidationException(err);
                
            	throw new SAXException(vx);
            }
            state.markAsUsed(descriptor);
            //-- if this is the identity then save id
            if (state.classDesc.getIdentity() == descriptor) {
                state.key = val;
            }
        }
        else {
            //-- check occurance of descriptor
            if (!state.isUsed(descriptor)) {
                firstOccurance = true;
            }
                
            //-- record usage of descriptor
            state.markAsUsed(descriptor);            
        }

        try {
            FieldHandler handler = descriptor.getHandler();
            //check if the value is a QName that needs to
            //be resolved (ns:value -> {URI}value)
            String valueType = descriptor.getSchemaType();
            if ((valueType != null) && (valueType.equals(QNAME_NAME))) {
                 val = resolveNamespace(val);
            }

            boolean addObject = true;
            if (_reuseObjects && fieldState.primitiveOrImmutable) {
                 //-- check to see if we need to
                 //-- add the object or not
                 Object tmp = handler.getValue(state.object);
                 if (tmp != null) {
                     //-- Do not add object if values
                     //-- are equal
                     addObject = (!tmp.equals(val));
                 }
            }
            
            //-- special handling for mapped objects
            if (descriptor.isMapped()) {
                if (!(val instanceof MapItem)) {
                    MapItem mapItem = new MapItem(fieldState.key, val);
                    val = mapItem;
                }
                else {
                    //-- make sure value exists (could be a reference)
                    MapItem mapItem = (MapItem)val;
                    if (mapItem.getValue() == null) {
                        //-- save for later...
                        addObject = false;
                        addReference(mapItem.toString(), state.object, descriptor);
                    }
                }
            }
            
            if (addObject) {
                //-- clear any collections if necessary
                if (firstOccurance && _clearCollections) {
                    handler.resetValue(state.object);
                }
                
                //-- finally set the value!!
                handler.setValue(state.object, val);
                
                // If there is a parent for this object, pass along
                // a notification that we've finished adding a child
                if ( _unmarshalListener != null ) {
                    _unmarshalListener.fieldAdded(descriptor.getFieldName(), state.object, fieldState.object);
                }

            }

        }
        /*
        catch(java.lang.reflect.InvocationTargetException itx) {

            Throwable toss = itx.getTargetException();
            if (toss == null) toss = itx;

            String err = "unable to add '" + name + "' to <";
            err += state.descriptor.getXMLName();
            err += "> due to the following exception: " + toss;
            throw new SAXException(err);
        }
        */
        catch(Exception ex) {
            StringWriter sw = new StringWriter();
            PrintWriter  pw = new PrintWriter(sw);
            ex.printStackTrace(pw);
            pw.flush();
            String err = "unable to add '" + name + "' to <";
            err += state.fieldDesc.getXMLName();
            err += "> due to the following exception: \n";
            err += ">>>--- Begin Exception ---<<< \n";
            err += sw.toString();
            err += ">>>---- End Exception ----<<< \n";
            throw new SAXException(err, ex);
        }

        //-- remove current namespace scoping
        _namespaces = _namespaces.getParent();

    } //-- endElement

    /**
     * <p>ContentHandler#endElement</p>
     *
     * Signals the end of an element
     *
     * @param localName The name of the element.
     */
    public void endElement(String namespaceURI, String localName, String qName)
    throws org.xml.sax.SAXException {        
        if ((qName == null) || (qName.length() == 0)) {
            if ((localName == null) || (localName.length() == 0)) {
                String error = "Missing either 'qName' or 'localName', both cannot be null or emtpy.";
                throw new SAXException(error);
            }
            qName = localName;
            if ((namespaceURI != null) && (namespaceURI.length() > 0)) {
                //-- rebuild qName, for now
                String prefix = _namespaces.getNamespacePrefix(namespaceURI);
                if ((prefix != null) && (prefix.length() > 0))
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
        if (_anyUnmarshaller != null) {
            _anyUnmarshaller.endPrefixMapping(prefix);
        }
        
    } //-- endPrefixMapping


    public void ignorableWhitespace(char[] ch, int start, int length)
        throws org.xml.sax.SAXException
    {
        
        //-- If we are skipping elements that have appeared in the XML but for
        //-- which we have no mapping, skip the text and return
        if ( _ignoreElementDepth > 0) {
            return;
        }

        if (_stateInfo.empty()) {
            return;
        }
        
        if (_anyUnmarshaller != null)
           _anyUnmarshaller.ignorableWhitespace(ch, start, length);
        else {
             UnmarshalState state = (UnmarshalState)_stateInfo.peek();
             if (state.wsPreserve) {
                if (state.buffer == null) state.buffer = new StringBuffer();
                state.buffer.append(ch, start, length);
             }
        }
    } //-- ignorableWhitespace

    public void processingInstruction(String target, String data)
        throws org.xml.sax.SAXException
    {
        //-- do nothing for now
    } //-- processingInstruction


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
    public void startDocument()
        throws org.xml.sax.SAXException
    {

        //-- I've found many application don't always call
        //-- #startDocument, so I usually never put any
        //-- important logic here

    } //-- startDocument
    
    
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
        if (debug) {
            if ((qName != null) && (qName.length() > 0))
                message("#startElement: " + qName);
            else
                message("#startElement: " + localName);
        }
        
        //-- If we are skipping elements that have appeared in the XML but for
        //-- which we have no mapping, increase the ignore depth counter and return
        if ((!_strictElements) && (_ignoreElementDepth > 0)) {
            ++_ignoreElementDepth;
            return;
        }

        //-- if we are in an <any> section
        //-- we delegate the event handling
        if (_anyUnmarshaller != null) {
            _depth++;
           _anyUnmarshaller.startElement(namespaceURI, localName, qName, atts);
           return;
        }
        
        //-- Create a new namespace scope if necessary and
        //-- make sure the flag is reset to true
        if (_createNamespaceScope)
            _namespaces = _namespaces.createNamespaces();
        else
            _createNamespaceScope = true;
            
        
        if (_reusableAtts == null) {
            if (atts != null) 
                _reusableAtts = new AttributeSetImpl(atts.getLength());
            else {
                //-- we can't pass a null AttributeSet to the
                //-- startElement
                _reusableAtts = new AttributeSetImpl();
            }
        }
        else {
            _reusableAtts.clear();
        }
        
        //-- process attributes
        boolean hasQNameAtts = false;
        if ((atts != null) && (atts.getLength() > 0)) {
            //-- look for any potential namespace declarations
            //-- in case namespace processing was disable
            //-- on the parser
            for (int i = 0; i < atts.getLength(); i++) {
                String attName = atts.getQName(i);
                if ((attName != null) && (attName.length() > 0)) {
                    if (attName.equals(XMLNS)) {
                        _namespaces.addNamespace("", atts.getValue(i));
                    }
                    else if (attName.startsWith(XMLNS_PREFIX)) {
                        String prefix = attName.substring(XMLNS_PREFIX.length());
                        _namespaces.addNamespace(prefix, atts.getValue(i));
                    }
                    else {
                        //-- check for prefix
                        if (attName.indexOf(':') < 0) {
                            _reusableAtts.setAttribute(attName,
                                atts.getValue(i), atts.getURI(i));
                        }
                        else hasQNameAtts = true;
                    }
                }
                else {
                    //-- if attName is null or empty, just process as a normal
                    //-- attribute
                    attName = atts.getLocalName(i);
                    if (XMLNS.equals(attName)) {
                        _namespaces.addNamespace("", atts.getValue(i));
                    }
                    else {
                        _reusableAtts.setAttribute(attName, atts.getValue(i), atts.getURI(i));
                    }
                }
            }
        }
        //-- if we found any qName-only atts, process those
        if (hasQNameAtts) {
            for (int i = 0; i < atts.getLength(); i++) {
                String attName = atts.getQName(i);
                if ((attName != null) && (attName.length() > 0)) {
                    //-- process any non-namespace qName atts
                    if ((!attName.equals(XMLNS)) && (!attName.startsWith(XMLNS_PREFIX))) 
                    {
                        int idx = attName.indexOf(':');
                        if (idx >= 0) {
                            String prefix = attName.substring(0, idx);
                            attName = attName.substring(idx+1);
                            String nsURI = atts.getURI(i);
                            if ((nsURI == null) || (nsURI.length() == 0)) {
                                nsURI = _namespaces.getNamespaceURI(prefix);
                            }
                            _reusableAtts.setAttribute(attName, atts.getValue(i), nsURI);
                        }
                    }
                }
                //-- else skip already processed in previous loop
            }
        }
        
        //-- preserve parser passed arguments for any potential
        //-- delegation
        if (_elemInfo == null) {
            _elemInfo = new ElementInfo(null, atts);
        }
        else {
            _elemInfo.clear();
            _elemInfo.attributes = atts;
        }
        
        if ((localName == null) || (localName.length() == 0)) {
            if ((qName == null) || (qName.length() == 0)) {
                String error = "Missing either 'localName' or 'qName', both cannot be emtpy or null.";
                throw new SAXException(error);
            }
            localName = qName;
            _elemInfo.qName = qName;
        }
        else {
            if ((qName == null) || (qName.length() == 0)) {
                if ((namespaceURI == null) || (namespaceURI.length() == 0)) {
                    _elemInfo.qName = localName;
                }
                else {
                    String prefix = _namespaces.getNamespacePrefix(namespaceURI);
                    if ((prefix != null) && (prefix.length() > 0)) {
                        _elemInfo.qName = prefix + ":" + localName;
                    }
                }
                
            }
            else {
                _elemInfo.qName = qName;
            }
        }
        
        int idx = localName.indexOf(':');
        if (idx >= 0) {
            String prefix = localName.substring(0, idx);
            localName = localName.substring(idx+1);
            if ((namespaceURI == null) || (namespaceURI.length() == 0)) {
                namespaceURI = _namespaces.getNamespaceURI(prefix);
            }
        }
        else {
            //-- adjust empty namespace
            if ((namespaceURI != null) && (namespaceURI.length() == 0))
                namespaceURI = null;
        }
        
        //-- call private startElement
        startElement(localName, namespaceURI, _reusableAtts);
        
    } //-- startElement
    
    /**
     * <p>DocumentHandler#startElement</p>
     *
     * Signals the start of element.
     *
     * @param name The name of the element.
     * @param attList The AttributeList containing the associated attributes for the
     *        element.
     */
    public void startElement(String name, AttributeList attList)
    throws org.xml.sax.SAXException {
        if (debug) {
            message("#startElement: " + name);
        }
        
        //-- If we are skipping elements that have appeared in the XML but for
        //-- which we have no mapping, increase the ignore depth counter and return
        if ((!_strictElements) && (_ignoreElementDepth > 0)) {
            ++_ignoreElementDepth;
            return;
        }

        //-- if we are in an <any> section
        //-- we delegate the event handling
        if (_anyUnmarshaller != null) {
            _depth++;
           _anyUnmarshaller.startElement(name,attList);
           return;
        }
        
        if (_elemInfo == null) {
            _elemInfo = new ElementInfo(name, attList);
        }
        else {
            _elemInfo.clear();
            _elemInfo.qName = name;
            _elemInfo.attributeList = attList;
        }
        
        //-- The namespace of the given element
        String namespace = null;

        //-- Begin Namespace Handling :
        //-- XXX Note: This code will change when we update the XML event API

        _namespaces = _namespaces.createNamespaces();

        //-- convert AttributeList to AttributeSet and process
        //-- namespace declarations
        AttributeSet atts = processAttributeList(attList);

        String prefix = "";
        //String qName = name;
        int idx = name.indexOf(':');
        if (idx >= 0) {
             prefix = name.substring(0,idx);
             name = name.substring(idx+1);
        }

        namespace = _namespaces.getNamespaceURI(prefix);
        
        //-- End Namespace Handling
        
        //-- call private startElement method
        startElement(name, namespace, atts);
        
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
    private void startElement
        (String name, String namespace, AttributeSet atts) 
        throws SAXException
    {

        UnmarshalState state = null;
        String xmlSpace = null;


        //-- handle special atts
        if (atts != null) {
            //-- xml:space
            xmlSpace = atts.getValue(XML_SPACE, Namespaces.XML_NAMESPACE);
            if (xmlSpace == null) {
                xmlSpace = atts.getValue(XML_SPACE_WITH_PREFIX, "");
            }
        }

        if (_stateInfo.empty()) {
            //-- Initialize since this is the first element

            if (_topClass == null) {
                if (_topObject != null) {
                    _topClass = _topObject.getClass();
                }
            }
            if (_cdResolver == null) {
                if (_topClass == null) {
                    String err = "The class for the root element '" +
                        name + "' could not be found.";
                    throw new SAXException(err);
                }
                _cdResolver = (XMLClassDescriptorResolver) ClassDescriptorResolverFactory.createClassDescriptorResolver(BindingType.XML);
                _cdResolver.setClassLoader(_loader);
            }

            _topState = new UnmarshalState();            
            _topState.elementName = name;
            _topState.wsPreserve = (xmlSpace != null) ? PRESERVE.equals(xmlSpace) : _wsPreserve;
            
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
                            throw new SAXException("Class not found: " +
                                instanceClassname);
                        }
                    }
                }
                else {
                    classDesc = resolveByXMLName(name, namespace, null);
                    if (classDesc == null) {
                        classDesc = getClassDescriptor(name, _loader);
                        if (classDesc == null) {
                            classDesc = getClassDescriptor(JavaNaming.toJavaClassName(name));
                        }
                    }
                    if (classDesc != null) {
                        _topClass = classDesc.getJavaClass();
                    }
                }

                if (_topClass == null) {
                    String err = "The class for the root element '" +
                        name + "' could not be found.";
                    throw new SAXException(err);
                }
            }

            //-- create a "fake" FieldDescriptor for the root element
            XMLFieldDescriptorImpl fieldDesc
                = new XMLFieldDescriptorImpl(_topClass,
                                             name,
                                             name,
                                             NodeType.Element);

            _topState.fieldDesc = fieldDesc;
            //-- look for XMLClassDescriptor if null
            //-- always check resolver first
            if (classDesc == null)
                classDesc = getClassDescriptor(_topClass);
                
            //-- check for top-level primitives 
            if (classDesc == null) {
                if (isPrimitive(_topClass)) {
                    classDesc = new PrimitivesClassDescriptor(_topClass);
                    fieldDesc.setIncremental(false);
                    _topState.primitiveOrImmutable = true;
                }
            }
            
            fieldDesc.setClassDescriptor(classDesc);
            if (classDesc == null) {
                //-- report error
			    if ((!isPrimitive(_topClass)) &&
             (!Serializable.class.isAssignableFrom( _topClass )))
             throw new SAXException(MarshalException.NON_SERIALIZABLE_ERR);
          String err = "unable to create XMLClassDescriptor " +
                       "for class: " + _topClass.getName();
          throw new SAXException(err);
            }
            _topState.classDesc = classDesc;
            _topState.type = _topClass;

            if  ((_topObject == null) && (!_topState.primitiveOrImmutable)) {
                // Retrieving the xsi:type attribute, if present
                String topPackage = getJavaPackage(_topClass);
                
                if (instanceClassname == null)
                    instanceClassname = getInstanceType(atts, topPackage);
                else {
                    //-- instance type already processed above, reset
                    //-- to null to prevent entering next block
                    instanceClassname = null;
                }
                    
                if (instanceClassname != null) {
                    Class instanceClass = null;
                    try {

                        XMLClassDescriptor xcd =
                            getClassDescriptor(instanceClassname);

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
                                if (xcd != null)
                                    instanceClass = xcd.getJavaClass();
                            }
                        }

                        if (instanceClass == null) {
                            throw new SAXException("Class not found: " +
                                instanceClassname);
                        }

                        if (!_topClass.isAssignableFrom(instanceClass)) {
                            String err = instanceClass + " is not a subclass of "
                                + _topClass;
                            throw new SAXException(err);
                        }

                    }
                    catch(Exception ex) {
                        String msg = "unable to instantiate " +
                            instanceClassname + "; ";
                        throw new SAXException(msg + ex, ex);
                    }

                    //-- try to create instance of the given Class
                    Arguments args = processConstructorArgs(atts, classDesc);
                    _topState.object = createInstance(instanceClass, args);
                }
                //-- no xsi type information present
                else {
                    //-- try to create instance of the given Class
                    Arguments args = processConstructorArgs(atts, classDesc);
                    _topState.object = createInstance(_topClass, args);
                }
            }
            //-- otherwise use _topObject
            else {
                _topState.object = _topObject;
            }
            
            _stateInfo.push(_topState);
            
            if (!_topState.primitiveOrImmutable) {
                //--The top object has just been initialized
                //--notify the listener
                if ( _unmarshalListener != null )
                    _unmarshalListener.initialized(_topState.object);
                    
                processAttributes(atts, classDesc);
                if ( _unmarshalListener != null )
                    _unmarshalListener.attributesProcessed(_topState.object);
                processNamespaces(classDesc);
            }
            
            String pkg = getJavaPackage(_topClass);
            if (getMappedPackage(namespace) == null) 
            {
                addNamespaceToPackageMapping(namespace, pkg);
            }
            return;
        } //--rootElement


        //-- get MarshalDescriptor for the given element
        UnmarshalState parentState = (UnmarshalState)_stateInfo.peek();

        //Test if we can accept the field in the parentState
        //in case the parentState fieldDesc is a container
        //-- This following logic tests to see if we are in a
        //-- container and we need to close out the container
        //-- before proceeding:
        boolean canAccept = false;
        while ((parentState.fieldDesc != null) &&
               (parentState.fieldDesc.isContainer() && !canAccept) )
        {
            XMLClassDescriptor tempClassDesc = parentState.classDesc;

            //-- Find ClassDescriptor for Parent
            if (tempClassDesc == null) {
               tempClassDesc = (XMLClassDescriptor)parentState.fieldDesc.getClassDescriptor();
               if (tempClassDesc == null)
                  tempClassDesc = getClassDescriptor(parentState.object.getClass());
            }
            
            canAccept = tempClassDesc.canAccept(name, namespace, parentState.object);

            if (!canAccept) {
                //-- Does container class even handle this field?
                if (tempClassDesc.getFieldDescriptor(name, namespace, NodeType.Element) != null) {
                    if (!parentState.fieldDesc.isMultivalued()) { 
                        String error = "The container object (" + tempClassDesc.getJavaClass().getName();
                        error += ") cannot accept the child object associated with the element '" + name + "'";
                        error += " because the container is already full!";
                        ValidationException vx = new ValidationException(error);
                        throw new SAXException(vx);    
                    }
                }
                endElement(parentState.elementName);
                parentState = (UnmarshalState)_stateInfo.peek();
            }
            tempClassDesc = null;
        }
        
        
        


        //-- create new state object
        state = new UnmarshalState();
        state.elementName = name;
        state.parent = parentState;
        
        if (xmlSpace != null)        
            state.wsPreserve = PRESERVE.equals(xmlSpace);
        else
            state.wsPreserve = parentState.wsPreserve;
        
        _stateInfo.push(state);

        //-- make sure we should proceed
        if (parentState.object == null) {
            if (!parentState.wrapper) return;
        }

        Class _class = null;

        //-- Find ClassDescriptor for Parent
        XMLClassDescriptor classDesc = parentState.classDesc;
        if (classDesc == null) {
            classDesc = (XMLClassDescriptor)parentState.fieldDesc.getClassDescriptor();
            if (classDesc == null)
                classDesc = getClassDescriptor(parentState.object.getClass());
        } else {
            // classDesc.resetElementCount();
        }

        //----------------------------------------------------/
        //- Find FieldDescriptor associated with the element -/
        //----------------------------------------------------/
        
        //-- A reference to the FieldDescriptor associated
        //-- the the "current" element
        XMLFieldDescriptor descriptor = null; 
        
        //-- inherited class descriptor 
        //-- (only needed if descriptor cannot be found directly)
        XMLClassDescriptor cdInherited = null;
        
        
        //-- loop through stack and find correct descriptor
        //int pIdx = _stateInfo.size() - 2; //-- index of parentState
        UnmarshalState targetState = parentState;
        String path = "";
        StringBuffer pathBuf = null;
        int count = 0;
        boolean isWrapper = false;
        XMLClassDescriptor oldClassDesc = classDesc;
        while (descriptor == null) {
            
            //-- NOTE (kv 20050228): 
            //-- we need to clean this code up, I made this
            //-- fix to make sure the correct descriptor which
            //-- matches the location path is used
            if (path.length() > 0) {
                String tmpName = path + "/" + name;
                descriptor = classDesc.getFieldDescriptor(tmpName, namespace, NodeType.Element);
            }
            //-- End Patch
            
            if (descriptor == null) { 
                descriptor = classDesc.getFieldDescriptor(name, namespace, NodeType.Element);
            }
            
            //-- Namespace patch, should be moved to XMLClassDescriptor, but
            //-- this is the least intrusive patch at the moment. kv - 20030423
            if ((descriptor != null) && (!descriptor.isContainer())) {
                if ((namespace != null) && (namespace.length() > 0)) {
                    if (!namespaceEquals(namespace, descriptor.getNameSpaceURI())) {
                        //-- if descriptor namespace is not null, then we must
                        //-- have a namespace match, so set descriptor to null,
                        //-- or if descriptor is not a wildcard we can also
                        //-- set to null. 
                        if ((descriptor.getNameSpaceURI() != null) || (!descriptor.matches("*"))) {
                            descriptor = null;
                        }
                        
                    }
                }
            }
            //-- end namespace patch
            
            
            /*
               If descriptor is null, we need to handle possible inheritence,
               which might not be described in the current ClassDescriptor.
               This can be a slow process...for speed use the match attribute
               of the xml element in the mapping file. This logic might
               not be completely necessary, and perhaps we should remove it.
            */
            // handle multiple level locations (where count > 0) (CASTOR-1039)
            // if ((descriptor == null) && (count == 0) && (!targetState.wrapper)) {
            if ((descriptor == null) && (!targetState.wrapper)) {
                MarshalFramework.InheritanceMatch[] matches = null;
                try {
                    matches = searchInheritance(name, namespace, classDesc, _cdResolver);
                }
                catch(MarshalException rx) {
                    //-- TODO: 
                }
                if (matches.length != 0) {
                    InheritanceMatch match = null;
                    // It may be the case that this class descriptor can
                    // appear under multiple parent field descriptors.  Look
                    // for the first match whose parent file descriptor XML
                    // name matches the name of the element we are under
                    for(int i = 0; i < matches.length; i++) {
                        if(parentState.elementName.equals(matches[i].parentFieldDesc.getLocationPath())) {
                            match = matches[i];
                            break;
                        }
                    }
                    if(match == null) match = matches[0];                    
                    descriptor  = match.parentFieldDesc;
                    cdInherited = match.inheritedClassDesc;
                    break; //-- found
                }
                /* */
                
                // handle multiple level locations (where count > 0) (CASTOR-1039)
                // isWrapper = (isWrapper || hasFieldsAtLocation(name, classDesc));
                String tmpLocation = name;
                if (count > 0) { tmpLocation = path + "/" + name; }
                isWrapper = (isWrapper || hasFieldsAtLocation(tmpLocation, classDesc));
            }
            else if (descriptor != null) {
                String tmpPath = descriptor.getLocationPath();
                if (tmpPath == null) tmpPath = "";
                if (path.equals(tmpPath))break; //-- found
                descriptor = null; //-- not found, try again
            }
            else {
                if (pathBuf == null) 
                    pathBuf = new StringBuffer();
                else 
                    pathBuf.setLength(0);
                pathBuf.append(path);
                pathBuf.append('/');
                pathBuf.append(name);
                isWrapper = (isWrapper || hasFieldsAtLocation(pathBuf.toString(), classDesc));
            }
            
            if (descriptor != null && isValidating()) {
                try {
                    classDesc.checkDescriptorForCorrectOrderWithinSequence(descriptor, parentState, name);
                } catch (ValidationException e) {
                    throw new SAXException(e);
                }
            }

            //-- Make sure there are more parent classes on stack
            //-- otherwise break, since there is nothing to do
            //if (pIdx == 0) break;
            if (targetState == _topState) break;
            
            //-- adjust name and try parent
            if (count == 0)
                path = targetState.elementName;
            else {
                if (pathBuf == null) 
                    pathBuf = new StringBuffer();
                else 
                    pathBuf.setLength(0);
                pathBuf.append(targetState.elementName);
                pathBuf.append('/');
                pathBuf.append(path);
                path = pathBuf.toString();
            }
                
            //-- get 
            //--pIdx;
            //targetState = (UnmarshalState)_stateInfo.elementAt(pIdx);
            targetState = targetState.parent;
            classDesc = targetState.classDesc;
            count++;
        }
        
        //-- The field descriptor is still null, we face a problem
        if (descriptor == null) {
            
            //-- reset classDesc
            classDesc = oldClassDesc;
            
            //-- isWrapper?
            if (isWrapper) {
                state.classDesc = new XMLClassDescriptorImpl(ContainerElement.class, name);
                state.wrapper = true;
                if (debug) {
                    message("wrapper-element: " + name);
                }
                //-- process attributes
                processWrapperAttributes(atts);
                return;
            }
            
            String mesg = "unable to find FieldDescriptor for '" + name;
            mesg += "' in ClassDescriptor of " + classDesc.getXMLName();

            //-- unwrap classDesc, if necessary, for the check
            //-- Introspector.introspected done below
            if (classDesc instanceof InternalXMLClassDescriptor) {
                classDesc = ((InternalXMLClassDescriptor)classDesc).getClassDescriptor();
            }

            //-- If we are skipping elements that have appeared in the XML but for
            //-- which we have no mapping, increase the ignore depth counter and return
            if (! _strictElements) {
                ++_ignoreElementDepth;
                //-- remove the StateInfo we just added
                _stateInfo.pop();
                if (debug) {
                    message(mesg + " - ignoring extra element.");
                }
                return;
            }
            //if we have no field descriptor and
            //the class descriptor was introspected
            //just log it
            else if (Introspector.introspected(classDesc)) {
                //if (warn)
                message(mesg);
                return;
            }
            //-- otherwise report error since we cannot find a suitable 
            //-- descriptor
            else {
                throw new SAXException(mesg);
            }
        } //-- end null descriptor
        
        /// DEBUG: System.out.println("path: " + path);

        //-- Save targetState (used in endElement)
        if (targetState != parentState) {
            state.targetState = targetState;
            parentState = targetState; //-- reassign
        }

        Object object = parentState.object;
        //--container support
        if (descriptor.isContainer()) {
            //create a new state to set the container as the object
            //don't save the current state, it will be recreated later
            
            if (debug) {
                message("#container: " + descriptor.getFieldName());
            }
            
            //-- clear current state and re-use for the container
            state.clear();
            //-- inherit whitespace preserving from the parentState
            state.wsPreserve = parentState.wsPreserve;
            state.parent = parentState;
            
            //here we can hard-code a name or take the field name
            state.elementName = descriptor.getFieldName();
            state.fieldDesc = descriptor;
            state.classDesc = (XMLClassDescriptor)descriptor.getClassDescriptor();
            Object containerObject = null;

            //1-- the container is not multivalued (not a collection)
            if (!descriptor.isMultivalued()) {
                // Check if the container object has already been instantiated
                FieldHandler handler = descriptor.getHandler();
                containerObject = handler.getValue(object);
                if (containerObject != null){
                    if (state.classDesc != null) {
                    	if (state.classDesc.canAccept(name, namespace, containerObject)) {
                            //remove the descriptor from the used list
                            parentState.markAsNotUsed(descriptor);
                        }
                    }
                    else {
                        //remove the descriptor from the used list
                        parentState.markAsNotUsed(descriptor);
                    }
                }
                else {
                    containerObject = handler.newInstance(object);
                }

            }
            //2-- the container is multivalued
            else {
                Class containerClass = descriptor.getFieldType();
                try {
                     containerObject = containerClass.newInstance();
                }
                catch(Exception ex) {
                    throw new SAXException(ex);
                }
            }
            state.object = containerObject;
            state.type = containerObject.getClass();

            //we need to recall startElement()
            //so that we can find a more appropriate descriptor in for the given name
            _namespaces = _namespaces.createNamespaces();
            startElement(name, namespace, atts);
            return;
        }
        //--End of the container support
        
        

        //-- Find object type and create new Object of that type
        state.fieldDesc = descriptor;

        /* <update>
            *  we need to add this code back in, to make sure
            *  we have proper access rights.
            *
        if (!descriptor.getAccessRights().isWritable()) {
            if (debug) {
                buf.setLength(0);
                buf.append("The field for element '");
                buf.append(name);
                buf.append("' is read-only.");
                message(buf.toString());
            }
            return;
        }
        */

        //-- Find class to instantiate
        //-- check xml names to see if we should look for a more specific
        //-- ClassDescriptor, otherwise just use the one found in the
        //-- descriptor
        classDesc = null;
        if (cdInherited != null) classDesc = cdInherited;
        else if (!name.equals(descriptor.getXMLName()))
            classDesc = resolveByXMLName(name, namespace, null);

        if (classDesc == null)
            classDesc = (XMLClassDescriptor)descriptor.getClassDescriptor();
        FieldHandler handler = descriptor.getHandler();
        boolean useHandler = true;

        try {

            //-- Get Class type...first use ClassDescriptor,
            //-- since it could be more specific than
            //-- the FieldDescriptor
            if (classDesc != null) {
                _class = classDesc.getJavaClass();

                //-- XXXX This is a hack I know...but we
                //-- XXXX can't use the handler if the field
                //-- XXXX types are different
                if (descriptor.getFieldType() != _class) {
                    state.derived = true;
                }
            }
            else {
                _class = descriptor.getFieldType();
            }
            
            //-- This *shouldn't* happen, but a custom implementation
            //-- could return null in the XMLClassDesctiptor#getJavaClass
            //-- or XMLFieldDescriptor#getFieldType. If so, just replace
            //-- with java.lang.Object.class (basically "anyType").
            if (_class == null) {
                _class = java.lang.Object.class;
            }

            // Retrieving the xsi:type attribute, if present
            String currentPackage = getJavaPackage(parentState.type);
            String instanceType = getInstanceType(atts, currentPackage);
            if (instanceType != null) {
                
                Class instanceClass = null;
                try {

                    XMLClassDescriptor instanceDesc
                        = getClassDescriptor(instanceType, _loader);

                    boolean loadClass = true;

                    if (instanceDesc != null) {
                        instanceClass = instanceDesc.getJavaClass();
                        classDesc = instanceDesc;
                        if (instanceClass != null) {
                            loadClass = (!instanceClass.getName().equals(instanceType));
                        }
                    }

                    if (loadClass) {
                        instanceClass = loadClass(instanceType, null);
                        //the FieldHandler can be either an XMLFieldHandler
                        //or a FieldHandlerImpl
                        FieldHandler tempHandler = descriptor.getHandler();

                        boolean collection = false;
                        if (tempHandler instanceof FieldHandlerImpl) {
                            collection = ((FieldHandlerImpl)tempHandler).isCollection();
                        }
                        else {
                            collection = Introspector.isCollection(instanceClass);
                        }

                        if ( (! collection ) &&
                         ! _class.isAssignableFrom(instanceClass))
                        {
                            if (!isPrimitive(_class)) {
                                String err = instanceClass.getName()
                                    + " is not a subclass of " + _class.getName();
                                throw new SAXException(err);
                            }
                        }
                    }
                    _class = instanceClass;
                    useHandler = false;
                }
                catch(Exception ex) {
                    String msg = "unable to instantiate " + instanceType;
                    throw new SAXException(msg + "; " + ex, ex);
                }

            }

            //-- Handle ArrayHandler
            if (_class == Object.class) {
                if (parentState.object instanceof ArrayHandler)
                    _class = ((ArrayHandler)parentState.object).componentType();
            }
            
            //-- Handle support for "Any" type

            if (_class == Object.class) {
                Class pClass = parentState.type;
                ClassLoader loader = pClass.getClassLoader();
                //-- first look for a descriptor based
                //-- on the XML name
                classDesc = resolveByXMLName(name, namespace, loader);
                //-- if null, create classname, and try resolving
                String cname = null;
                if (classDesc == null) {
                    //-- create class name
                    cname = JavaNaming.toJavaClassName(name);
                    classDesc = getClassDescriptor(cname, loader);
                }
                //-- if still null, try using parents package
                if (classDesc == null) {
                    //-- use parent to get package information
                    String pkg = pClass.getName();
                    int idx = pkg.lastIndexOf('.');
                    if (idx > 0) {
                        pkg = pkg.substring(0,idx+1);
                        cname = pkg + cname;
                        classDesc = getClassDescriptor(cname, loader);
                    }
                }

                if (classDesc != null) {
                    _class = classDesc.getJavaClass();
                    useHandler = false;
                }
                else {
                    //we are dealing with an AnyNode
                    //1- creates a new SAX2ANY handler
                    _anyUnmarshaller = new SAX2ANY(_namespaces, state.wsPreserve);
                    //2- delegates the element handling
                    if (_elemInfo.attributeList != null) {
                        //-- SAX 1
                        _anyUnmarshaller.startElement(_elemInfo.qName, 
                            _elemInfo.attributeList);
                    }
                    else {
                        //-- SAX 2
                        _anyUnmarshaller.startElement(namespace, name, _elemInfo.qName, 
                            _elemInfo.attributes);
                    }
                    //first element so depth can only be one at this point
                    _depth = 1;
                    state.object = _anyUnmarshaller.getStartingNode();
                    state.type = _class;
                    //don't need to continue
                     return;
                }
            }
            
            boolean byteArray = false;
            if (_class.isArray())
                byteArray = (_class.getComponentType() == Byte.TYPE);

            //-- check for immutable
            if (isPrimitive(_class) ||
                descriptor.isImmutable() ||
                byteArray)
            {
                state.object = null;
                state.primitiveOrImmutable = true;  
                //-- handle immutable types, such as java.util.Locale
                if (descriptor.isImmutable()) {
                    if (classDesc == null)
                        classDesc = getClassDescriptor(_class);
                    state.classDesc = classDesc;
                    Arguments args = processConstructorArgs(atts, classDesc);
                    if ((args != null) && (args.size() > 0)) {
                    	state.args = args;
                    }
                }
            }
            else {
                if (classDesc == null)
                    classDesc = getClassDescriptor(_class);
                    
                //-- XXXX should remove this test once we can
                //-- XXXX come up with a better solution
                if ((!state.derived) && useHandler) {

                    boolean create = true;
                    if (_reuseObjects) {
                        state.object = handler.getValue(parentState.object);
                        create = (state.object == null);
                    }
                    if (create) {
                        Arguments args = processConstructorArgs(atts, classDesc);
                        if ((args.values != null) && (args.values.length > 0)) {
                            if (handler instanceof ExtendedFieldHandler) {
                                ExtendedFieldHandler efh = 
                                    (ExtendedFieldHandler)handler;
                                state.object = efh.newInstance(parentState.object, args.values);
                            }
                            else {
                                String err = "constructor arguments can only be " +
                                    "used with an ExtendedFieldHandler.";
                                throw new SAXException(err);
                            }
                        }
                        else {
                            state.object = handler.newInstance(parentState.object);
                        }
                    }
                }
                //-- reassign class in case there is a conflict
                //-- between descriptor#getFieldType and
                //-- handler#newInstance...I should hope not, but
                //-- who knows
                if (state.object != null) {
                    _class = state.object.getClass();
                    if (classDesc != null) {
                        if (classDesc.getJavaClass() != _class) {
                            classDesc = null;
                        }
                    }
                }
                else {
                    try {
                        if (_class.isArray()) {
                            state.object = new ArrayHandler(_class.getComponentType());
                            _class = ArrayHandler.class;
                        }
                        else {
                            Arguments args = processConstructorArgs(atts, classDesc);
                            state.object = createInstance(_class, args);
                            //state.object = _class.newInstance();
                        }
                    }
                    catch(java.lang.Exception ex) {
                        String err = "unable to instantiate a new type of: ";
                        err += className(_class);
                        err += "; " + ex.getMessage();
                        //storing causal exception using SAX non-standard method...
                        SAXException sx = new SAXException(err, ex);
                        //...and also using Java 1.4 method
                        //sx.initCause(ex);
                        throw sx;
                    }
                }
            }
            state.type = _class;
        }
        catch (java.lang.IllegalStateException ise) {
            message(ise.toString());
            throw new SAXException(ise);
        }

        //-- At this point we should have a new object, unless
        //-- we are dealing with a primitive type, or a special
        //-- case such as byte[]
        if (classDesc == null) {
            classDesc = getClassDescriptor(_class);
        }
        state.classDesc = classDesc;

        if ((state.object == null) && (!state.primitiveOrImmutable))
        {
            String err = "unable to unmarshal: " + name + "\n";
            err += " - unable to instantiate: " + className(_class);
            throw new SAXException(err);
        }

        //-- assign object, if incremental

        if (descriptor.isIncremental()) {
            if (debug) {
                message("debug: Processing incrementally for element: " + name);
            }
            try {
                handler.setValue(parentState.object, state.object);
            }
            catch(java.lang.IllegalStateException ise) {
                String err = "unable to add \"" + name + "\" to ";
                err += parentState.fieldDesc.getXMLName();
                err += " due to the following error: " + ise;
                throw new SAXException(err, ise);
            }
        }

        if (state.object != null) {
            //--The object has just been initialized
            //--notify the listener
            if ( _unmarshalListener != null )
                _unmarshalListener.initialized(state.object);
            processAttributes(atts, classDesc);
            if ( _unmarshalListener != null )
                _unmarshalListener.attributesProcessed(state.object);
            processNamespaces(classDesc);
        }
        else if ((state.type != null) && (!state.primitiveOrImmutable)) {
            if (atts != null) {
                processWrapperAttributes(atts);
                StringBuffer buffer = new StringBuffer();
                buffer.append("The current object for element '");
                buffer.append(name);
                buffer.append("\' is null. Processing attributes as location");
                buffer.append("/wrapper only and ignoring all other attribtes.");
                message(buffer.toString());
            }
        }
        else {
        	//-- check for special attributes, such as xsi:nil
            if (atts != null) {
            	String nil = atts.getValue(NIL_ATTR, XSI_NAMESPACE);
                state.nil = "true".equals(nil);
                processWrapperAttributes(atts);
            }
        }

    } //-- void startElement(String, AttributeList)



    /**
     * Indicates whether validation is enabled or not.
     * @return True if validation is enabled.
     */
    private boolean isValidating() {
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
        if (_anyUnmarshaller != null) {
            _anyUnmarshaller.startPrefixMapping(prefix, uri);
        }
        else if (_createNamespaceScope) {
            _namespaces = _namespaces.createNamespaces();
            _createNamespaceScope = false;
        }
        
        _namespaces.addNamespace(prefix, uri);
        
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
        String err = "Parsing Error : "+exception.getMessage()+'\n'+
                     "Line : "+ exception.getLineNumber() + '\n'+
                     "Column : "+exception.getColumnNumber() + '\n';
        throw new SAXException (err, exception);
    } //-- error

    public void fatalError(SAXParseException exception)
        throws org.xml.sax.SAXException
    {
        String err = "Parsing Error : "+exception.getMessage()+'\n'+
                     "Line : "+ exception.getLineNumber() + '\n'+
                     "Column : "+exception.getColumnNumber() + '\n';
        throw new SAXException (err, exception);

    } //-- fatalError


    public void warning(SAXParseException exception)
        throws org.xml.sax.SAXException
    {
        String err = "Parsing Error : "+exception.getMessage()+'\n'+
                     "Line : "+ exception.getLineNumber() + '\n'+
                     "Column : "+exception.getColumnNumber() + '\n';
        throw new SAXException (err, exception);

    } //-- warning

      //---------------------/
     //- Protected Methods -/
    //---------------------/

    /**
     * Sets the current Castor configuration. Currently this
     * Configuration is only used during Validation (which is
     * why this method is currently protected, since it has
     * no effect at this point on the actual configuration of 
     * the unmarshaller)
     *
     * Currently, this method should only be called by the 
     * Unmarshaller.
     */
    protected void setConfiguration(Configuration config) {
        _config = config;
    } //-- setConfiguration
    
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
    private void addReference(String idRef, Object parent, XMLFieldDescriptor descriptor) 
    {
        
        ReferenceInfo refInfo = new ReferenceInfo(idRef, parent, descriptor);
        
        if (_resolveTable == null) 
            _resolveTable = new Hashtable();
        else {
            refInfo.setNext((ReferenceInfo)_resolveTable.get(idRef));
        }
        _resolveTable.put(idRef, refInfo);
    } //-- addReference
    
    /**
     * Creates an instance of the given class /type, using 
     * the arguments provided (if there are any).
     * @param type The class type to be used during instantiation
     * @param args (Optional) arguments to be used during instantiation
     */
     private Object createInstance(final Class type, final Arguments args)
            throws SAXException {
        Object instance = null;
        try {
            if (args == null) {
                instance = _objectFactory.createInstance(type);
            } else {
                instance = _objectFactory.createInstance(type, args.types,
                        args.values);
            }
        } catch (Exception ex) {
            String msg = "Unable to instantiate " + type.getName() + "; ";
            throw new SAXException(msg, ex);
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
    private String getInstanceType(AttributeSet atts, String currentPackage) 
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
                typeNamespaceURI = _namespaces.getNamespaceURI(prefix);
            }

            //-- Retrieve the type corresponding to the schema name and
            //-- return it.
            XMLClassDescriptor classDesc = null;
            
            try {
                classDesc = _cdResolver.resolveByXMLName(type, typeNamespaceURI, _loader);            

                if (classDesc != null)
                    return classDesc.getJavaClass().getName();


                //-- if class descriptor is not found here, then no descriptors
                //-- existed in memory...try to load one based on name of
                //-- Schema type
                final String className = JavaNaming.toJavaClassName(type);
            
                String adjClassName = className;
                String mappedPackage = getMappedPackage(typeNamespaceURI);
                if ((mappedPackage != null) && (mappedPackage.length() > 0)) {
                    adjClassName = mappedPackage + "." + className;
                }
            	classDesc = _cdResolver.resolve(adjClassName, _loader);
                if (classDesc != null)
                    return classDesc.getJavaClass().getName();

                //-- try to use "current Package"
                if ((currentPackage != null) && currentPackage.length() > 0) {
                	adjClassName = currentPackage + '.' + className;
                }
                
                classDesc = _cdResolver.resolve(adjClassName, _loader);
                if (classDesc != null)
                    return classDesc.getJavaClass().getName();
                
                //-- Still can't find type, this may be due to an
                //-- attempt to unmarshal an older XML instance
                //-- that was marshalled with a previous Castor. A
                //-- bug fix in the XMLMappingLoader prevents old
                //-- xsi:type that are missing the "java:"
                classDesc = _cdResolver.resolve(type, _loader);
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
     * Looks up the package name from the given namespace URI
     * 
     * @param namespace the namespace URI to lookup
     * @return the package name or null.
     */
    private String getMappedPackage(String namespace) {
        String lookUpKey = (namespace != null) ? namespace : "";
        return (String)_namespaceToPackage.get(lookUpKey);
    } //-- getMappedPackage
    

    /**
     * Processes the given attribute list, and attempts to add each
     * Attribute to the current Object on the stack
     *
     * @param atts the AttributeSet to process
     * @param classDesc the classDesc to use during processing
     * @param element the element name used for error reporting
    **/
    private void processAttributes
        (AttributeSet atts, XMLClassDescriptor classDesc)
        throws org.xml.sax.SAXException
    {

        //-- handle empty attributes
        if ((atts == null) || (atts.getSize() == 0)) {
            if (classDesc != null) {
                XMLFieldDescriptor[] descriptors
                    = classDesc.getAttributeDescriptors();
                for (int i = 0; i < descriptors.length; i++) {
                    XMLFieldDescriptor descriptor = descriptors[i];
                    if (descriptor == null) continue;
                    //-- Since many attributes represent primitive
                    //-- fields, we add an extra validation check here
                    //-- in case the class doesn't have a "has-method".
                    if (descriptor.isRequired() && (isValidating() || debug)) {
                        String err = classDesc.getXMLName() + " is missing " +
                            "required attribute: " + descriptor.getXMLName();
                        if (_locator != null) {
                            err += "\n  - line: " + _locator.getLineNumber() +
                                " column: " + _locator.getColumnNumber();
                        }
                        if (isValidating()) {
                            throw new SAXException(err);
                        }
                        if (debug) {
                            message(err);
                        }
                    }
                }
            }
            return;
        }


        UnmarshalState state = (UnmarshalState)_stateInfo.peek();
        Object object = state.object;

        if (classDesc == null) {
            classDesc = state.classDesc;
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

        XMLFieldDescriptor[] descriptors = classDesc.getAttributeDescriptors();

        boolean[] processedAtts = new boolean[atts.getSize()];
        for (int i = 0; i < descriptors.length; i++) {

            XMLFieldDescriptor descriptor = descriptors[i];

            String name      = descriptor.getXMLName();
            String namespace = descriptor.getNameSpaceURI();

            int index = atts.getIndex(name, namespace);

            String attValue = null;
            if (index >= 0) {
                attValue = atts.getValue(index);
                processedAtts[index] = true;
            }

            try {
                processAttribute(name, namespace, attValue, descriptor, classDesc, object);
            }
            catch(java.lang.IllegalStateException ise) {
                String err = "unable to add attribute \"" + name + "\" to '";
                err += state.classDesc.getJavaClass().getName();
                err += "' due to the following error: " + ise;
                throw new SAXException(err, ise);
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
            if (processedAtts[i]) continue;

            String namespace = atts.getNamespace(i);
            String name = atts.getName(i);
            
            //-- skip XSI attributes
            if (XSI_NAMESPACE.equals(namespace)) {
                if (NIL_ATTR.equals(name)) {
                	String value = atts.getValue(i);
                    state.nil = ("true".equals(value));
                }
                continue;
            }
                

            if (name.startsWith(XML_PREFIX + ':')) {
                
                //-- XML specification specific attribute
                //-- It should be safe to ignore these...but
                //-- if you think otherwise...let use know!
                if (debug) {
                    String msg = "ignoring attribute '" + name +
                        "' for class: " +
                            state.classDesc.getJavaClass().getName();
                    message(msg);
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
                int pIdx = _stateInfo.size() - 2; //-- index of parentState
                String path = state.elementName;
                StringBuffer pathBuf = null;
                while (pIdx >= 0) {
                    UnmarshalState targetState = (UnmarshalState)_stateInfo.elementAt(pIdx);
                    --pIdx;
                    if (targetState.wrapper) {
                        //path = targetState.elementName + "/" + path;
                        if (pathBuf == null) 
                            pathBuf = new StringBuffer();
                        else
                            pathBuf.setLength(0);
                        pathBuf.append(targetState.elementName);
                        pathBuf.append('/');
                        pathBuf.append(path);
                        path = pathBuf.toString();
                        continue;
                    }
                    classDesc = targetState.classDesc;
                    descriptor = classDesc.getFieldDescriptor(name, namespace, NodeType.Attribute);
                
                    if (descriptor != null) {
                        String tmpPath = descriptor.getLocationPath();
                        if (tmpPath == null) tmpPath = "";
                        if (path.equals(tmpPath)) break; //-- found
                    }
                        
                    if (pathBuf == null) 
                        pathBuf = new StringBuffer();
                    else
                        pathBuf.setLength(0);
                    pathBuf.append(targetState.elementName);
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
                    String error = "The attribute '" + name +
                        "' appears illegally on element '" +
                        state.elementName + "'.";
                    throw new SAXException(error);
                }
                continue;
            }

            try {
                processAttribute(name, namespace, atts.getValue(i), descriptor, classDesc, object);
            }
            catch(java.lang.IllegalStateException ise) {
                String err = "unable to add attribute \"" + name + "\" to '";
                err += state.classDesc.getJavaClass().getName();
                err += "' due to the following error: " + ise;
                throw new SAXException(err, ise);
            }
        }

    } //-- processAttributes

    /**
     * Processes the given AttributeSet for wrapper elements.
     * 
     * @param atts the AttributeSet to process
     */
    private void processWrapperAttributes(AttributeSet atts)
        throws org.xml.sax.SAXException
    {
        
        UnmarshalState state = (UnmarshalState)_stateInfo.peek();
        
        //-- loop through attributes and look for the
        //-- ancestor objects that they may belong to
        for (int i = 0; i < atts.getSize(); i++) {
            String name = atts.getName(i);
            String namespace = atts.getNamespace(i);
            
            //-- skip XSI attributes
            if (XSI_NAMESPACE.equals(namespace))
                continue;
                
            XMLFieldDescriptor descriptor = null;
            XMLClassDescriptor classDesc = null;
            //-- check for nested attribute...loop through
            //-- stack and find correct descriptor
            int pIdx = _stateInfo.size() - 2; //-- index of parentState
            String path = state.elementName;
            StringBuffer pathBuf = null;
            UnmarshalState targetState = null;
            while (pIdx >= 0) {
                targetState = (UnmarshalState)_stateInfo.elementAt(pIdx);
                --pIdx;
                if (targetState.wrapper) {
                    //path = targetState.elementName + "/" + path;
                    if (pathBuf == null) 
                        pathBuf = new StringBuffer();
                    else
                        pathBuf.setLength(0);
                    pathBuf.append(targetState.elementName);
                    pathBuf.append('/');
                    pathBuf.append(path);
                    path = pathBuf.toString();
                    continue;
                }
                classDesc = targetState.classDesc;
                
                XMLFieldDescriptor[] descriptors = classDesc.getAttributeDescriptors();
                boolean found = false;
                for (int a = 0; a < descriptors.length; a++) {
                    descriptor = descriptors[a];
                    if (descriptor == null) continue;
                    if (descriptor.matches(name)) {
                        String tmpPath = descriptor.getLocationPath();
                        if (tmpPath == null) tmpPath = "";
                        if (path.equals(tmpPath)) {
                            found = true;
                            break;
                        }
                    }
                }
                if (found) break;
                        
                //path = targetState.elementName + "/" + path;
                if (pathBuf == null) 
                    pathBuf = new StringBuffer();
                else
                    pathBuf.setLength(0);
                pathBuf.append(targetState.elementName);
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
                    processAttribute(name, 
                                     namespace,
                                     atts.getValue(i), 
                                     descriptor, 
                                     classDesc, 
                                     targetState.object);
                }
                catch(java.lang.IllegalStateException ise) {
                    String err = "unable to add attribute \"" + name + "\" to '";
                    err += state.classDesc.getJavaClass().getName();
                    err += "' due to the following error: " + ise;
                    throw new SAXException(err, ise);
                }
            }
        }
        
    } //-- processWrapperAttributes
    
    /**
     * Processes the given Attribute
    **/
    private void processAttribute
        (String attName, String attNamespace, String attValue,
         XMLFieldDescriptor descriptor,
         XMLClassDescriptor classDesc,
         Object parent) throws org.xml.sax.SAXException
    {

        //Object value = attValue;
        while (descriptor.isContainer()) {
            FieldHandler handler = descriptor.getHandler();
            Object containerObject = handler.getValue(parent);

            if (containerObject == null) {
                containerObject = handler.newInstance(parent);
                handler.setValue(parent, containerObject);
            }

            ClassDescriptor containerClassDesc = ((XMLFieldDescriptorImpl)descriptor).getClassDescriptor();
            descriptor = ((XMLClassDescriptor)containerClassDesc).getFieldDescriptor(
            		attName, attNamespace, NodeType.Attribute);
            parent = containerObject;
        }

        if (attValue == null) {
             //-- Since many attributes represent primitive
             //-- fields, we add an extra validation check here
             //-- in case the class doesn't have a "has-method".
             if (descriptor.isRequired() && isValidating()) {
                String err = classDesc.getXMLName() + " is missing " +
                    "required attribute: " + attName;
                if (_locator != null) {
                    err += "\n  - line: " + _locator.getLineNumber() +
                        " column: " + _locator.getColumnNumber();
                }
                throw new SAXException(err);
            }
            return;
        }

        //-- if this is the identity then save id
        if (classDesc.getIdentity() == descriptor) {
            
            try {
                _idResolver.bind(attValue, parent, isValidating());
            } catch (ValidationException e) {
                throw new SAXException("Duplicate ID " + attValue + " encountered.", e);
            }

            //-- save key in current state
            UnmarshalState state = (UnmarshalState) _stateInfo.peek();
            state.key = attValue;

            //-- resolve waiting references
            resolveReferences(attValue, parent);
        }
        //-- if this is an IDREF(S) then resolve reference(s)
        else if (descriptor.isReference()) {
            if (descriptor.isMultivalued()) {
                StringTokenizer st = new StringTokenizer(attValue);
                while (st.hasMoreTokens()) {
                    processIDREF(st.nextToken(), descriptor, parent);
                }
            }
            else processIDREF(attValue, descriptor, parent);
            //-- object values have been set by processIDREF
            //-- simply return
            return;
        }
        
        //-- if it's a constructor argument, we can exit at this point
        //-- since constructor arguments have already been set
        if (descriptor.isConstructorArgument()) 
            return;

        //-- attribute handler
        FieldHandler handler = descriptor.getHandler();
        if(handler==null)
        	return;
        
        //-- attribute field type
        Class type = descriptor.getFieldType();
        String valueType = descriptor.getSchemaType();
        boolean isPrimative = isPrimitive(type);
        boolean isQName = valueType!=null && valueType.equals(QNAME_NAME);
        
        boolean isByteArray = false;
        if (type.isArray()) {
            isByteArray = (type.getComponentType() == Byte.TYPE);
        }

        
        //-- if this is an multi-value attribute
    	StringTokenizer attrValueTokenizer = null;
        if (descriptor.isMultivalued())
        {
        	attrValueTokenizer = new StringTokenizer(attValue);
        	if(attrValueTokenizer.hasMoreTokens())
        		attValue = attrValueTokenizer.nextToken();
        }
        
        while(true)
        {
        	//-- value to set
            Object value = attValue;
            //-- check for proper type and do type conversion
            if (isPrimative)
                value = toPrimitiveObject(type, attValue, descriptor);
            
            // special treatment for byte[]s
            if (isByteArray) {
                if (attValue == null)
                    value = new byte[0];
                else {
                    //-- Base64 decoding
                    value = Base64Decoder.decode(attValue);
                }
            }
            
            //-- check if the value is a QName that needs to
            //-- be resolved (ns:value -> {URI}value)
            if(isQName)
                value = resolveNamespace(value);
            //-- set value
            handler.setValue(parent, value);
            //-- more values?
            if(attrValueTokenizer==null)
                break;
            if(!attrValueTokenizer.hasMoreTokens())
                break;
            //-- next value
            attValue = attrValueTokenizer.nextToken();
        }

    } //-- processAttribute

    /**
     * Processes the given attribute set, and creates the
     * constructor arguments
     *
     * @param atts the AttributeSet to process
     * @param classDesc the XMLClassDescriptor of the objec
     * @return the array of constructor argument values.
     */
    private Arguments processConstructorArgs
        (AttributeSet atts, XMLClassDescriptor classDesc)
        throws org.xml.sax.SAXException
    {
        
        if (classDesc == null) return new Arguments();


        //-- Loop through Attribute Descriptors and build
        //-- the argument array

        //-- NOTE: Due to IDREF being able to reference an
        //-- un-yet unmarshalled object, we cannot handle
        //-- references as constructor arguments. 
        //-- kvisco - 20030421
        XMLFieldDescriptor[] descriptors = classDesc.getAttributeDescriptors();
        int count = 0;
        for (int i = 0; i < descriptors.length; i++) {
            XMLFieldDescriptor descriptor = descriptors[i];
            if (descriptor == null) continue;
            if (descriptor.isConstructorArgument()) ++count;
        }
        
        Arguments args = new Arguments();
        
        if (count == 0) return args;
        
        args.values = new Object[count];
        args.types  = new Class[count];
        
        for (int i = 0; i < descriptors.length; i++) {

            XMLFieldDescriptor descriptor = descriptors[i];
            if (descriptor == null) continue;
            if (!descriptor.isConstructorArgument()) continue;
            
            int argIndex = descriptor.getConstructorArgumentIndex();
            if (argIndex >= count) {
                String err = "argument index out of bounds: " + argIndex;
                throw new SAXException(err);
            }

            args.types[argIndex] = descriptor.getFieldType();
            String name      = descriptor.getXMLName();
            String namespace = descriptor.getNameSpaceURI();

            int index = atts.getIndex(name, namespace);

            if (index >= 0) {
                Object value = atts.getValue(index);
                //-- check for proper type and do type
                //-- conversion
                if (isPrimitive(args.types[argIndex]))
                    value = toPrimitiveObject(args.types[argIndex], (String)value, descriptor);
                //check if the value is a QName that needs to
                //be resolved (ns:value -> {URI}value)
                String valueType = descriptor.getSchemaType();
                if ((valueType != null) && (valueType.equals(QNAME_NAME))) {
                        value = resolveNamespace(value);
                }
                args.values[argIndex] = value;
            }
            else {
                args.values[argIndex] = null;
            }
        }
        return args;
    } //-- processConstructorArgs

    /**
     * Processes the given IDREF
     *
     * @param idRef the ID of the object in which to reference
     * @param descriptor the current FieldDescriptor
     * @param parent the current parent object
     * @return true if the ID was found and resolved properly
     */
    private boolean processIDREF
        (String idRef, XMLFieldDescriptor descriptor, Object parent)
    {
        Object value = _idResolver.resolve(idRef);
        if (value == null) {
            //-- save state to resolve later
            addReference(idRef, parent, descriptor);
        }
        else {
            FieldHandler handler = descriptor.getHandler();
            if (handler != null)
                handler.setValue(parent, value);
        }
        return (value != null);
    } //-- processIDREF

    /**
     * Processes the attributes and namespace declarations found
     * in the given SAX AttributeList. The global AttributeSet
     * is cleared and updated with the attributes. Namespace
     * declarations are added to the set of namespaces in scope.
     *
     * @param atts the AttributeList to process.
    **/
    private AttributeSet processAttributeList(AttributeList atts)
        throws SAXException
    {

        if (atts == null) return new AttributeSetImpl(0);


        //-- process all namespaces first
        int attCount = 0;
        boolean[] validAtts = new boolean[atts.getLength()];
        for (int i = 0; i < validAtts.length; i++) {
            String attName = atts.getName(i);
            if (attName.equals(XMLNS)) {
                _namespaces.addNamespace("", atts.getValue(i));
            }
            else if (attName.startsWith(XMLNS_PREFIX)) {
                String prefix = attName.substring(XMLNS_PREFIX_LENGTH);
                _namespaces.addNamespace(prefix, atts.getValue(i));
            }
            else {
                validAtts[i] = true;
                ++attCount;
            }
        }
        //-- process validAtts...if any exist
        AttributeSetImpl attSet = null;
        if (attCount > 0) {
            attSet = new AttributeSetImpl(attCount);
            for (int i = 0; i < validAtts.length; i++) {
                if (!validAtts[i]) continue;
                String namespace = null;
                String attName = atts.getName(i);
                int idx = attName.indexOf(':');
                if (idx > 0) {
                    String prefix = attName.substring(0, idx);
                    if (!prefix.equals(XML_PREFIX)) {
                        attName = attName.substring(idx+1);
                        namespace = _namespaces.getNamespaceURI(prefix);
                        if (namespace == null) {
                            String error = "The namespace associated with "+
                                "the prefix '" + prefix +
                                "' could not be resolved.";
                            throw new SAXException(error);

                        }
                    }
                }
                attSet.setAttribute(attName, atts.getValue(i), namespace);
            }
        }
        else attSet = new AttributeSetImpl(0);

        return attSet;

    } //-- method: processAttributeList

    /**
     * Saves local namespace declarations to the object
     * model if necessary.
     *
     * @param classDesc the current ClassDescriptor.
    **/
    private void processNamespaces(XMLClassDescriptor classDesc) {


        if (classDesc == null) return;

        //-- process namespace nodes
        XMLFieldDescriptor nsDescriptor =
            classDesc.getFieldDescriptor(null, null, NodeType.Namespace);

        if (nsDescriptor != null) {
            UnmarshalState state = (UnmarshalState) _stateInfo.peek();
            FieldHandler handler = nsDescriptor.getHandler();
            if (handler != null) {
                Enumeration enumeration = _namespaces.getLocalNamespacePrefixes();
                while (enumeration.hasMoreElements()) {
                    String nsPrefix = (String)enumeration.nextElement();
                    if (nsPrefix == null) nsPrefix = "";
                    String nsURI = _namespaces.getNamespaceURI(nsPrefix);
                    if (nsURI == null) nsURI = "";
                    MapItem mapItem = new MapItem(nsPrefix, nsURI);
                    handler.setValue(state.object, mapItem);
                }
            }
        }
    } //-- processNamespaces

    /**
     * Extracts the prefix and resolves it to it's associated namespace.
     * If the prefix is 'xml', then no resolution will occur, however
     * in all other cases the resolution will change the prefix:value
     * as {NamespaceURI}value
     *
     * @param value the QName to resolve.
     */
    private Object resolveNamespace(Object value)
        throws SAXException
    {

        if ( (value == null) || !(value instanceof String))
            return value;

        String result = (String)value;
        int idx = result.indexOf(':');
        String prefix = null;
        if (idx > 0) {
            prefix = result.substring(0,idx);
            if (XML_PREFIX.equals(prefix)) {
                //-- Do NOT Resolve the 'xml' prefix.
                return value;
            }
            result = result.substring(idx+1);
        }
        String namespace = _namespaces.getNamespaceURI(prefix);
        if  ((namespace != null) && (namespace.length() > 0)) {
            result = '{'+namespace+'}'+result;
            return result;
        }
        else if ((namespace == null) && (prefix!=null))
             throw new SAXException("The namespace associated with the prefix: '" + 
                prefix + "' is null.");
        else
            return result;

    }


    /**
     * Sends a message to all observers. Currently the only observer is
     * the logger.
     * @param msg the message to send to all message observers
    **/
    private void message(String msg) {
        if (_logWriter != null) {
            _logWriter.println(msg);
            _logWriter.flush();
        }
    } //-- message

    /**
     * Finds and returns an XMLClassDescriptor for the given class name.
     * If a ClassDescriptor could not be found one will attempt to
     * be generated.
     * @param className the name of the class to find the descriptor for
    **/
    private XMLClassDescriptor getClassDescriptor (String className)
        throws SAXException
    {
        Class type = null;
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
     * @param _class the Class to get the ClassDescriptor for
    **/
    private XMLClassDescriptor getClassDescriptor(Class _class)
        throws SAXException
    {
        if (_class == null) return null;


        //-- special case for strings
        if (_class == String.class)
            return _stringDescriptor;

        if (_class.isArray()) return null;
        if (isPrimitive(_class)) return null;

        if (_cdResolver == null)
            _cdResolver = (XMLClassDescriptorResolver) 
                ClassDescriptorResolverFactory.createClassDescriptorResolver(BindingType.XML);

        XMLClassDescriptor classDesc = null;


        try {
            classDesc = _cdResolver.resolveXML(_class);
        }
        catch(ResolverException rx) {
            // TODO
        }

        if (classDesc != null) {
            return new InternalXMLClassDescriptor(classDesc);
        }

        if (debug) {
            message(ERROR_DID_NOT_FIND_CLASSDESCRIPTOR + _class.getName());
        }
        
        return classDesc;
    } //-- getClassDescriptor


    /**
     * Finds and returns a ClassDescriptor for the given class. If
     * a ClassDescriptor could not be found one will attempt to
     * be generated.
     * @param className the name of the class to get the Descriptor for
    **/
    private XMLClassDescriptor getClassDescriptor
        (String className, ClassLoader loader)
        throws SAXException
    {
        if (_cdResolver == null)
            _cdResolver = (XMLClassDescriptorResolver) 
                ClassDescriptorResolverFactory.createClassDescriptorResolver(BindingType.XML);

        
        XMLClassDescriptor classDesc = null;
        try {
            classDesc = _cdResolver.resolve(className, loader);
        }
        catch(ResolverException rx) {
            throw new SAXException(rx);
        }
        

        if (classDesc != null) {
            return new InternalXMLClassDescriptor(classDesc);
        }

        if (debug) {
            message(ERROR_DID_NOT_FIND_CLASSDESCRIPTOR + className);
        }
        
        return classDesc;
    } //-- getClassDescriptor
    
    /**
     * Returns the XMLClassLoader
     */
    private XMLClassDescriptor resolveByXMLName
        (String name, String namespace, ClassLoader loader) 
        throws SAXException
    {
        
        try {
            return _cdResolver.resolveByXMLName(name, namespace, loader);
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
	private String getJavaPackage(Class type)
	{
		if (type == null)
			return null;
		String pkg = (String)_javaPackages.get(type);
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
    private String className(Class type) {
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
    private static boolean isWhitespace(StringBuffer sb) {
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
    private Class loadClass(String className, ClassLoader loader)
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
     * Resolves the current set of waiting references for the given Id
     * @param id the id that references are waiting for
     * @param value, the value of the resolved id
    **/
    private void resolveReferences(String id, Object value)
        throws org.xml.sax.SAXException
    {
        if ((id == null) || (value == null)) return;
        if (_resolveTable == null) return;

        ReferenceInfo refInfo = (ReferenceInfo)_resolveTable.remove(id);
        while (refInfo != null) {
            try {
                FieldHandler handler = refInfo.getDescriptor().getHandler();
                if (handler != null)
                    handler.setValue(refInfo.getTarget(), value);
                    
                //-- special handling for MapItems
                if (refInfo.getTarget() instanceof MapItem) {
                    resolveReferences(refInfo.getTarget().toString(), refInfo.getTarget());
                }
            }
            catch(java.lang.IllegalStateException ise) {

                String err = "Attempting to resolve an IDREF: " +
                        id + "resulted in the following error: " +
                        ise.toString();
                throw new SAXException(err, ise);
            }
            refInfo = refInfo.getNext();
        }
    } //-- resolveReferences

    /**
     * Converts a String to the given primitive object type
     *
     * @param type the class type of the primitive in which
     * to convert the String to
     * @param value the String to convert to a primitive
     * @return the new primitive Object
     */
    private Object toPrimitiveObject
        (Class type, String value, XMLFieldDescriptor fieldDesc) 
        throws SAXException
    {
        try {
            return toPrimitiveObject(type, value);
        }
        catch(Exception ex) {
            String err = "The following error occured while trying to ";
            err += "unmarshal field " + fieldDesc.getFieldName();
            UnmarshalState state = (UnmarshalState)_stateInfo.peek();
            if (state != null) {
                if (state.object != null) {
                    err += " of class " + state.object.getClass().getName();
                }
            }
            err += "\n";
            err += ex.getMessage();
            
            
            throw new SAXException(err, ex);
        }
    } //-- toPrimitiveObject


    /**
     * Converts a String to the given primitive object type
     *
     * @param type the class type of the primitive in which
     * to convert the String to
     * @param value the String to convert to a primitive
     * @return the new primitive Object
     */
    public static Object toPrimitiveObject(Class type, String value) {

        Object primitive = value;

        if (value != null) {
            //-- trim any numeric values
            if ((type != Character.TYPE) && (type != Character.class))
                value = value.trim();
        }
        
        boolean isNull = ((value == null) || (value.length() == 0));

        
        //-- I tried to order these in the order in which
        //-- (I think) types are used more frequently
        
        // int
        if ((type == Integer.TYPE) || (type == Integer.class)) {
            if (isNull)
                primitive = new Integer(0);
            else
                primitive = new Integer(value);
        }
        // boolean
        else if ((type == Boolean.TYPE) || (type == Boolean.class)) {
            if (isNull)
                primitive = new Boolean(false);
            else
				primitive = (value.equals("1") ||
							 value.toLowerCase().equals("true"))
								? Boolean.TRUE : Boolean.FALSE;
        }
        // double
        else if ((type == Double.TYPE) || (type == Double.class)) {
            if (isNull)
                primitive = new Double(0.0);
            else
                primitive = new Double(value);
        }
        // long
        else if ((type == Long.TYPE) || (type == Long.class)) {
            if (isNull)
                primitive = new Long(0);
            else
                primitive = new Long(value);
        }
        // char
        else if ((type == Character.TYPE) || (type == Character.class)) {
            if (!isNull)
                primitive = new Character(value.charAt(0));
            else
                primitive = new Character('\0');
        }
        // short
        else if ((type == Short.TYPE) || (type == Short.class)) {
            if (isNull)
                primitive = new Short((short)0);
            else
                primitive = new Short(value);
        }
        // float
        else if ((type == Float.TYPE) || (type == Float.class)) {
            if (isNull)
                primitive = new Float(0);
            else
                primitive = new Float(value);
        }
        // byte
        else if ((type == Byte.TYPE) || (type == Byte.class)) {
            if (isNull)
                primitive = new Byte((byte)0);
            else
                primitive = new Byte(value);
        }
        //BigDecimal
        else if (type == java.math.BigDecimal.class) {
            if (isNull)
               primitive = new java.math.BigDecimal(0);
            else primitive = new java.math.BigDecimal(value);
        }
        //BigInteger
        else if (type == java.math.BigInteger.class) {
            if (isNull)
               primitive = java.math.BigInteger.valueOf(0);
            else primitive = new java.math.BigInteger(value);
        }

        return primitive;
    } //-- toPrimitiveObject

    
    /**
     * A utility class for keeping track of the
     * qName and how the SAX parser passed attributes
     */
    class ElementInfo {
        
        String qName = null;
        Attributes attributes = null;
        AttributeList attributeList = null;
        
        ElementInfo() {
            super();
        }
        
        ElementInfo(String qName, Attributes atts) {
            super();
            this.qName = qName;
            this.attributes = atts;
        }
        
        ElementInfo(String qName, AttributeList atts) {
            super();
            this.qName = qName;
            this.attributeList = atts;
        }
        
        void clear() {
            qName = null;
            attributes = null;
            attributeList = null;
        }
    } //-- ElementInfo
    
    /**
     * Internal class used for passing constructor argument
     * information
     */
    class Arguments {
        Object[] values = null;
        Class[]  types  = null;
        
        public int size() {
        	if (values == null) return 0;
            return values.length;
        }
    } //-- Arguments

    /**
     * A class for handling Arrays during unmarshalling.
     *
     * @author <a href="mailto:kvisco@intalio.com">kvisco@intalio.com</a>
     */
    public static class ArrayHandler {
        
        Class _componentType = null;
        
        ArrayList _items = null;
        
        /**
         * Creates a new ArrayHandler 
         *
         * @param componentType the ComponentType for the array.
         */
        ArrayHandler(Class componentType) {
            if (componentType == null) {
                String err = "The argument 'componentType' may not be null.";
                throw new IllegalArgumentException(err);
            }
            _componentType = componentType;
            _items = new ArrayList();
        } //-- ArrayHandler
        
        /**
         * Adds the given object to the underlying array 
         *
         */
        public void addObject(Object obj) {
            if (obj == null) return;
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
        
        public Object getObject() {
            int size = _items.size();
            Object array = Array.newInstance(_componentType, size);
            for (int i = 0; i < size; i++)
                Array.set(array, i, _items.get(i));
            return array;
        }
       
        public Class componentType() {
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

} //-- Unmarshaller

