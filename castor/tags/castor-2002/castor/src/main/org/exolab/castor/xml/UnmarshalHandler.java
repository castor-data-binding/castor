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
 * Copyright 2002 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id$
 */

package org.exolab.castor.xml;

//-- Castor imports
import org.exolab.castor.util.MimeBase64Decoder;
import org.exolab.castor.util.List;
import org.exolab.castor.xml.descriptors.StringClassDescriptor;
import org.exolab.castor.xml.util.*;
import org.exolab.castor.mapping.ClassDescriptor;
import org.exolab.castor.mapping.FieldHandler;
import org.exolab.castor.mapping.MapItem;
import org.exolab.castor.mapping.loader.FieldHandlerImpl;

//-- xml related imports
import org.xml.sax.*;
import org.xml.sax.helpers.ParserFactory;

import java.io.PrintWriter;
import java.io.Reader;
import java.io.Serializable;
import java.io.StringWriter;

import java.lang.reflect.*;

import java.util.Hashtable;
import java.util.Stack;
import java.util.Vector;
import java.util.Enumeration;
import java.util.StringTokenizer;


/**
 * An unmarshaller to allowing unmarshalling of XML documents to
 * Java Objects. The Class must specify
 * the proper access methods (setters/getters) in order for instances
 * of the Class to be properly unmarshalled.
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a>
 * @version $Revision$ $Date$
**/
public final class UnmarshalHandler extends MarshalFramework
    implements DocumentHandler, ErrorHandler
{


    //---------------------------/
    //- Private Class Variables -/
    //---------------------------/

    private static final Class[]  EMPTY_CLASS_ARGS  = new Class[0];
    private static final Object[] EMPTY_OBJECT_ARGS = new Object[0];
    private static final String   EMPTY_STRING      = "";


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
    private StringBuffer     buf           = null;

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
    private ClassDescriptorResolver _cdResolver = null;

    /**
     * The IDResolver for resolving IDReferences
    **/
    private IDResolverImpl _idResolver = null;

    /**
     * A flag indicating whether or not to perform validation
    **/
    private boolean          _validate     = true;


    private Hashtable _resolveTable = null;

    private ClassLoader _loader = null;

    private static final StringClassDescriptor _stringDescriptor
        = new StringClassDescriptor();

    /**
     * A SAX2ANY unmarshaller in case we are dealing with <any>
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

    private Hashtable _nsPackageMappings = null;

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
     * @param _class the Class to create the UnmarshalHandler for
    **/
    protected UnmarshalHandler(Class _class) {
        super();
        _stateInfo    = new Stack();
        _idResolver   = new IDResolverImpl();
        _resolveTable = new Hashtable();
        buf           = new StringBuffer();
        _topClass     = _class;
        _namespaces   = new Namespaces();
        _nsPackageMappings = new Hashtable();
    } //-- UnmarshalHandler(Class)

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
    public void setResolver(ClassDescriptorResolver cdResolver) {
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
     * Sets the flag for validation
     * @param validate, a boolean to indicate whether or not
     * validation should be done during umarshalling. <br />
     * By default, validation will be performed.
    **/
    public void setValidation(boolean validate) {
        this._validate = validate;
    } //-- setValidation

    //-----------------------------------/
    //- SAX Methods for DocumentHandler -/
    //-----------------------------------/

    public void characters(char[] ch, int start, int length)
        throws SAXException
    {
        //System.out.println("#characters");

        if (_stateInfo.empty()) {
            return;
        }
        if (_anyUnmarshaller != null)
           _anyUnmarshaller.characters(ch, start, length);
        else {
             UnmarshalState state = (UnmarshalState)_stateInfo.peek();

             if (state.buffer == null) state.buffer = new StringBuffer();
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
                endElement(state.elementName);
                endElement(name);
                return;
            }
            else {
                String err = "error in xml, expecting </" + state.elementName;
                err += ">, but received </" + name + "> instead.";
                throw new SAXException(err);
            }
        }

        //-- clean up current Object
        Class type = state.type;

        if ( type == null ) {
            //-- this message will only show up if debug
            //-- is turned on...how should we handle this case?
            //-- should it be a fatal error?
            message("Ignoring " + state.elementName + " no descriptor was found");
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

        if (state.primitiveOrImmutable) {

            String str = null;

            if (state.buffer != null) {
                str = state.buffer.toString();
                state.buffer.setLength(0);
            }

            if (type == String.class) {
                if (str != null)
                    state.object = str;
                else
                    state.object = "";
            }
            //-- special handling for byte[]
            else if (byteArray) {
                if (str == null)
                    state.object = new byte[0];
                else {
                    //-- Base64 decoding
                    char[] chars = str.toCharArray();
                    MimeBase64Decoder decoder = new MimeBase64Decoder();
                    decoder.translate(chars, 0, chars.length);
                    state.object = decoder.getByteArray();
                }
            }
            else state.object = toPrimitiveObject(type,str);
        }

        //-- check for character content
        if ((state.buffer != null) &&
            (state.buffer.length() > 0) &&
            (state.classDesc != null)) {
           XMLFieldDescriptor cdesc = state.classDesc.getContentDescriptor();
            if (cdesc != null) {
               Object value = state.buffer.toString();
               if (isPrimitive(cdesc.getFieldType()))
                  value = toPrimitiveObject(cdesc.getFieldType(), (String)value);

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
                    throw new SAXException(err);
                }
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

        //-- if we are at root....just validate and we are done
         if (_stateInfo.empty()) {
             if (_validate) {
                try {
                    Validator validator = new Validator();
                    validator.validate(state.object, _cdResolver);
                }
                catch(ValidationException vEx) {
                    throw new SAXException(vEx);
                }
            }


            // As we are at the root, we check that we have no unresolved
            // references left.
//             if ( ! _resolveTable.isEmpty()) {
//                 String unresolvedRefs = new String();
//                 for (Enumeration e = _resolveTable.elements(); e.hasMoreElements() ;) {
//                     ReferenceInfo unresolved = (ReferenceInfo)e.nextElement();
//                     unresolvedRefs += unresolved.descriptor.getXMLName() + "=\"" +
//                                       unresolved.id + "\" in element <" +
//                                       ((XMLClassDescriptor)unresolved.descriptor.getContainingClassDescriptor()).getXMLName() + " ...>\n";
//                 }
//                 throw new SAXException("Unable to resolve the following IDREF(s) during unmarshalling:\n" + unresolvedRefs);
//             }

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

        //-- get target object
        state = (UnmarshalState) _stateInfo.peek();
        //-- check to see if we have already read in
        //-- an element of this type
        if (!descriptor.isMultivalued() ) {
            if (state.isUsed(descriptor)) {
                String err = "element \"" + name;
                err += "\" occurs more than once. (" + descriptor + ")";
                ValidationException vx =
                    new ValidationException(err);
                throw new SAXException(vx);
            }
            state.markAsUsed(descriptor);
        }
        else {
            //-- special handling for mapped objects
            if (descriptor.isMapped()) {
                MapItem mapItem = new MapItem(fieldState.key, val);
                val = mapItem;
            }
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
            if (addObject) handler.setValue(state.object, val);

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
            throw new SAXException(err);
        }

         //-- remove current namespace scoping
        _namespaces = _namespaces.getParent();

    } //-- endElement

    public void ignorableWhitespace(char[] ch, int start, int length)
        throws org.xml.sax.SAXException
    {
        //-- ignore
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

    public void startDocument()
        throws org.xml.sax.SAXException
    {

        //-- I've found many application don't always call
        //-- #startDocument, so I usually never put any
        //-- important logic here

    } //-- startDocument

    public void startElement(String name, AttributeList attList)
        throws org.xml.sax.SAXException
    {

        //-- if we are in an <any> section
        //-- we delegate the event handling
        if (_anyUnmarshaller != null) {
            _depth++;
           _anyUnmarshaller.startElement(name,attList);
           return;
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
        int idx = name.indexOf(':');
        if (idx >= 0) {
             prefix = name.substring(0,idx);
             name = name.substring(idx+1);
        }

        namespace = _namespaces.getNamespaceURI(prefix);
        //-- End Namespace Handling

        UnmarshalState state = null;

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
                else {
                    _cdResolver = new ClassDescriptorResolverImpl(_loader);
                }
            }

            _topState = new UnmarshalState();
            _topState.elementName = name;


            XMLClassDescriptor classDesc = null;
            //-- If _topClass is null, then we need to search
            //-- the resolver for one
            if (_topClass == null) {
                classDesc = _cdResolver.resolveByXMLName(name, namespace, null);

                if (classDesc != null)
                    _topClass = classDesc.getJavaClass();

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
            if (classDesc == null)
                classDesc = getClassDescriptor(_topClass);
            fieldDesc.setClassDescriptor(classDesc);
            if (classDesc == null) {
                //-- report error
			    if ((!_topClass.isPrimitive()) &&
                    (!Serializable.class.isAssignableFrom( _topClass )))
                    throw new SAXException(MarshalException.NON_SERIALIZABLE_ERR);
                else {
                    String err = "unable to create XMLClassDescriptor " +
                                 "for class: " + _topClass.getName();
                    throw new SAXException(err);
                }
            }
            _topState.classDesc = classDesc;
            _topState.type = _topClass;

            if (_topObject == null) {
                // Retrieving the xsi:type attribute, if present
                String topPackage = getJavaPackage(_topClass);
                String instanceClassname = getInstanceType(atts, topPackage);
                if (instanceClassname != null) {
                    Class instanceClass = null;
                    Object instance = null;
                    try {

                        XMLClassDescriptor xcd =
                            getClassDescriptor(instanceClassname);

                        if (xcd != null)
                            instanceClass = xcd.getJavaClass();

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
                        throw new SAXException(msg + ex);
                    }

                    //-- try to create instance of the given Class
                    try {
                        _topState.object = instanceClass.newInstance();
                    }
                    catch(Exception ex) {
                        String msg = "unable to instantiate " +
                            instanceClass.getName() + "; ";
                        throw new SAXException(msg + ex);
                    }

                }
                //-- no xsi type information present
                else {
                    //-- try to create instance of the given Class
                    try {
                        _topState.object = _topClass.newInstance();
                    }
                    catch(Exception ex) {
                        String msg = "unable to instantiate " +
                            _topClass.getName() + "; ";
                        throw new SAXException(msg + ex);
                    }
                }
            }
            //-- otherwise use _topObject
            else {
                _topState.object = _topObject;
            }
            _stateInfo.push(_topState);
            processAttributes(atts, classDesc);
            processNamespaces(classDesc);
            return;
        } //--rootElement


        //-- get MarshalDescriptor for the given element
        UnmarshalState parentState = (UnmarshalState)_stateInfo.peek();

        //Test if we can accept the field in the parentState
        //in case the parentState fieldDesc is a container
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

            canAccept = tempClassDesc.canAccept(name, parentState.object);

            if (!canAccept) {
                endElement(parentState.elementName);
                parentState = (UnmarshalState)_stateInfo.peek();
            }

            tempClassDesc = null;
        }


        //-- create new state object
        state = new UnmarshalState();
        state.elementName = name;
        _stateInfo.push(state);

        //-- make sure we should proceed
        if (parentState.object == null) return;

        Class _class = null;

        //-- Find ClassDescriptor for Parent
        XMLClassDescriptor classDesc = parentState.classDesc;
        if (classDesc == null) {
            classDesc = (XMLClassDescriptor)parentState.fieldDesc.getClassDescriptor();
            if (classDesc == null)
                classDesc = getClassDescriptor(parentState.object.getClass());
        }

        //-- Find FieldDescriptor for the element
        //-- we wish to unmarshal
        XMLFieldDescriptor descriptor = null;
        descriptor = classDesc.getFieldDescriptor(name, NodeType.Element);

        /*
          If descriptor is null, we need to handle possible inheritence,
          which might not be described in the current ClassDescriptor.
          This can be a slow process...for speed use the match attribute
          of the xml element in the mapping file. This logic might
          not be completely necessary, and perhaps we should remove it.
        */
        XMLClassDescriptor cdInherited = null;
        if (descriptor == null) {
             MarshalFramework.InheritanceMatch[] matches = searchInheritance(name, namespace, classDesc, _cdResolver);
             if (matches.length != 0) {
                 InheritanceMatch match = matches[0];
                 descriptor  = match.parentFieldDesc;
                 cdInherited = match.inheritedClassDesc;
             }
        }

        //the field descriptor is still null, we face a problem
        if (descriptor == null) {
            String msg = "unable to find FieldDescriptor for '" + name;
            msg += "' in ClassDescriptor of " + classDesc.getXMLName();
            //if we have no field descriptor and
            //the class descriptor was introspected
            //just log it
            if (Introspector.introspected(classDesc)) {
                message(msg);
                return;
            }
            //but if we could not find the field descriptor
            //whereas a class descriptor has been provided (using the
            //Source Generator for instance)
            else throw new SAXException(msg);

        }

        Object object = parentState.object;
        //--container support
        if (descriptor.isContainer()) {
            //create a new state a set the container as the object
            //don't save the current state, it will be recreated later
            _stateInfo.pop();
            state = new UnmarshalState();
            // swap states so that container comes before the
            // the state for the item contained within the container
            _stateInfo.push(state);

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
                if (containerObject != null)
                    //remove the descriptor from the used list
                    parentState.markAsNotUsed(descriptor);
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
            startElement(name, attList);
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
            classDesc = _cdResolver.resolveByXMLName(name, namespace, null);

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
            else
                _class = descriptor.getFieldType();

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
                            String err = instanceClass
                                + " is not a subclass of " + _class;
                            throw new SAXException(err);
                        }
                    }
                    _class = instanceClass;
                    useHandler = false;
                }
                catch(Exception ex) {
                    String msg = "unable to instantiate " + instanceType;
                    throw new SAXException(msg + "; " + ex);
                }

            }

            //-- Handle support for "Any" type

            if (_class == Object.class) {
                Class pClass = parentState.type;
                ClassLoader loader = pClass.getClassLoader();
                //-- first look for a descriptor based
                //-- on the XML name
                classDesc = _cdResolver.resolveByXMLName(name, namespace, loader);
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
                    idx = pkg.lastIndexOf('.');
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
                    _anyUnmarshaller = new SAX2ANY();
                    //2- delegates the element handling
                    _anyUnmarshaller.startElement(name, attList);
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
            }
            else {
                //-- XXXX should remove this test once we can
                //-- XXXX come up with a better solution
                if ((!state.derived) && useHandler) {

                    boolean create = true;
                    if (_reuseObjects) {
                        state.object = handler.getValue(parentState.object);
                        create = (state.object == null);
                    }
                    if (create) {
                        state.object = handler.newInstance(parentState.object);
                    }
                }
                //-- reassign class in case there is a conflict
                //-- between descriptor#getFieldType and
                //-- handler#newInstance...I should hope not, but
                //-- who knows
                if (state.object != null) {
                    _class = state.object.getClass();
                }
                else {
                    try {
                        state.object = _class.newInstance();
                    }
                    catch(java.lang.Exception ex) {
                        String err = "unable to instantiate a new type of: ";
                        err += className(_class);
                        throw new SAXException(err);
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
                buf.setLength(0);
                buf.append("debug: Processing incrementally for element: ");
                buf.append(name);
                message(buf.toString());
            }
            try {
                handler.setValue(parentState.object, state.object);
            }
            catch(java.lang.IllegalStateException ise) {
                String err = "unable to add \"" + name + "\" to ";
                err += parentState.fieldDesc.getXMLName();
                err += " due to the following error: " + ise;
                throw new SAXException(err);
            }
        }

        if (state.object != null) {
            processAttributes(atts, classDesc);
            processNamespaces(classDesc);
        }
        else if ((state.type != null) && (!state.primitiveOrImmutable)) {
            buf.setLength(0);
            buf.append("The current object for element '");
            buf.append(name);
            buf.append("\' is null, ignoring attributes.");
            message(buf.toString());
        }

    } //-- void startElement(String, AttributeList)

     //------------------------------------/
    //- org.xml.sax.ErrorHandler methods -/
    //------------------------------------/

    public void error(SAXParseException exception)
        throws org.xml.sax.SAXException
    {
        String err = "Parsing Error : "+exception.getMessage()+'\n'+
                     "Line : "+ exception.getLineNumber() + '\n'+
                     "Column : "+exception.getColumnNumber() + '\n';
        throw new SAXException (err);
    } //-- error

    public void fatalError(SAXParseException exception)
        throws org.xml.sax.SAXException
    {
        String err = "Parsing Error : "+exception.getMessage()+'\n'+
                     "Line : "+ exception.getLineNumber() + '\n'+
                     "Column : "+exception.getColumnNumber() + '\n';
        throw new SAXException (err);

    } //-- fatalError


    public void warning(SAXParseException exception)
        throws org.xml.sax.SAXException
    {
        String err = "Parsing Error : "+exception.getMessage()+'\n'+
                     "Line : "+ exception.getLineNumber() + '\n'+
                     "Column : "+exception.getColumnNumber() + '\n';
        throw new SAXException (err);

    } //-- warning

      //-------------------/
     //- Private Methods -/
    //-------------------/

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
    private String getInstanceType(AttributeSet atts, String currentPackage) {

        if (atts == null) return null;

        //-- find xsi:type attribute
        String type = atts.getValue(XSI_TYPE, XSI_NAMESPACE);

        if (type != null) {
            if (type.startsWith(JAVA_PREFIX)) {
                return type.substring(JAVA_PREFIX.length());
            }
            //-- Retrieve the type corresponding to the schema name and
            //-- return it.
            XMLClassDescriptor classDesc =
                _cdResolver.resolveByXMLName(type, null, _loader);

            if (classDesc != null)
                return classDesc.getJavaClass().getName();

            //-- if class descriptor is not found here, then no descriptors
            //-- existed in memory...try to load one based on name of
            //-- Schema type
            String className = JavaNaming.toJavaClassName(type);
            classDesc = _cdResolver.resolve(className, _loader);
            if (classDesc != null)
                return classDesc.getJavaClass().getName();

            //-- try to use "current Package"
            className = currentPackage + '.' + className;
            classDesc = _cdResolver.resolve(className, _loader);
            if (classDesc != null)
                return classDesc.getJavaClass().getName();


        }
        return null;
    } //-- getInstanceType

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
                    if (descriptor.isRequired() && (_validate || debug)) {
                        String err = classDesc.getXMLName() + " is missing " +
                            "required attribute: " + descriptor.getXMLName();
                        if (_locator != null) {
                            err += "\n  - line: " + _locator.getLineNumber() +
                                " column: " + _locator.getColumnNumber();
                        }
                        if (_validate) throw new SAXException(err);
                        if (debug) message(err);
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
                buf.setLength(0);
                buf.append("No ClassDescriptor for '");
                buf.append(state.elementName);
                buf.append("', cannot process attributes.");
                message(buf.toString());
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


            if (index >= 0) {
                processedAtts[index] = true;
            }
            //-- otherwise...for now just continue, this code needs to
            //-- change when we upgrade to new event API
            else continue;

            try {
                processAttribute(name, atts.getValue(index), descriptor, classDesc, object);
            }
            catch(java.lang.IllegalStateException ise) {
                String err = "unable to add attribute \"" + name + "\" to '";
                err += state.classDesc.getJavaClass().getName();
                err += "' due to the following error: " + ise;
                throw new SAXException(err);
            }
        }

        //-- Handle any non processed attributes...
        //-- This is useful for descriptors that might use
        //-- wild-cards or other types of matching..as well
        //-- as backward compatibility...attribute descriptors
        //-- were erronously getting set with the default
        //-- namespace by the source generator...this is
        //-- also true of the generated classes for the
        //-- Mapping Framework...we need to clean this up
        //-- at some point in the future.
        for (int i = 0; i < processedAtts.length; i++) {
            if (processedAtts[i]) continue;

            String name = atts.getName(i);

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
                classDesc.getFieldDescriptor(name, NodeType.Attribute);
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
                processAttribute(name, atts.getValue(i), descriptor, classDesc, object);
            }
            catch(java.lang.IllegalStateException ise) {
                String err = "unable to add attribute \"" + name + "\" to '";
                err += state.classDesc.getJavaClass().getName();
                err += "' due to the following error: " + ise;
                throw new SAXException(err);
            }
        }

    } //-- processAttributes

    /**
     * Processes the given Attribute
    **/
    private void processAttribute
        (String attName, String attValue,
         XMLFieldDescriptor descriptor,
         XMLClassDescriptor classDesc,
         Object parent) throws org.xml.sax.SAXException
    {

        Object value = attValue;
        while (descriptor.isContainer()) {
            FieldHandler handler = descriptor.getHandler();
            Object containerObject = handler.getValue(parent);

            if (containerObject == null) {
                containerObject = handler.newInstance(parent);
                handler.setValue(parent, containerObject);
            }

            ClassDescriptor containerClassDesc = ((XMLFieldDescriptorImpl)descriptor).getClassDescriptor();
            descriptor = ((XMLClassDescriptor)containerClassDesc).getFieldDescriptor(attName, NodeType.Attribute);
            parent = containerObject;
        }

        if (attValue == null) {
             //-- Since many attributes represent primitive
             //-- fields, we add an extra validation check here
             //-- in case the class doesn't have a "has-method".
             if (descriptor.isRequired() && _validate) {
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
            _idResolver.bind(attValue, parent);
            
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

        //-- check for proper type and do type
        //-- conversion
        Class type = descriptor.getFieldType();
        if (isPrimitive(type))
            value = toPrimitiveObject(type, attValue);
        //check if the value is a QName that needs to
        //be resolved (ns:value -> {URI}value)
        String valueType = descriptor.getSchemaType();
        if ((valueType != null) && (valueType.equals(QNAME_NAME))) {
                value = resolveNamespace(value);
        }
        FieldHandler handler = descriptor.getHandler();
        if (handler != null)
            handler.setValue(parent, value);

    } //-- processAttribute

    /**
     * Processes the given IDREF
     *
     * @param idRef the ID of the object in which to reference
     * @param descriptor the current FieldDescriptor
     * @param parent the current parent object
    **/
    private void processIDREF
        (String idRef, XMLFieldDescriptor descriptor, Object parent)
    {
        Object value = _idResolver.resolve(idRef);
        if (value == null) {
            //-- save state to resolve later
            ReferenceInfo refInfo
                = new ReferenceInfo(idRef, parent, descriptor);
            refInfo.next
                = (ReferenceInfo)_resolveTable.remove(idRef);
            _resolveTable.put(idRef, refInfo);
        }
        else {
            FieldHandler handler = descriptor.getHandler();
            if (handler != null)
                handler.setValue(parent, value);
        }
    } //-- processIDREF

    /**
     * Processes the attributes and namespace declarations found
     * in the given SAX AttributeList. The global AttributeSet
     * is cleared and updated with the attributes. Namespace
     * declarations are added to the set of namespaces in scope.
     *
     * @param atts the AttributeList to process.
    **/
    private AttributeSet processAttributeList(AttributeList atts) {

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
            classDesc.getFieldDescriptor(null, NodeType.Namespace);
                
        if (nsDescriptor != null) {
            UnmarshalState state = (UnmarshalState) _stateInfo.peek();
            FieldHandler handler = nsDescriptor.getHandler();
            if (handler != null) {
                Enumeration enum = _namespaces.getLocalNamespaces();
                while (enum.hasMoreElements()) {
                    String nsURI = (String)enum.nextElement();
                    String nsPrefix = _namespaces.getNamespacePrefix(nsURI);
                    if (nsPrefix == null) nsPrefix = "";
                    MapItem mapItem = new MapItem(nsPrefix, nsURI);
                    handler.setValue(state.object, mapItem);
                }
            }
        }
    } //-- processNamespaces
    
    /**
     * Extracts the prefix and resolves it.
     * The resolution will change the prefix:value as {NamespaceURI}value
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
            result = result.substring(idx+1);
        }
        String namespace = _namespaces.getNamespaceURI(prefix);
        if (namespace != null) {
            result = '{'+namespace+'}'+result;
            return result;
        }
        else if (prefix!=null)
             throw new SAXException("The namespace associated with the prefix:"+prefix+" is null.");
        else
            return value;

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
            _cdResolver = new ClassDescriptorResolverImpl();

        XMLClassDescriptor classDesc = null;


        classDesc = _cdResolver.resolve(_class);

        if (classDesc != null) return classDesc;

        //-- we couldn't create a ClassDescriptor, check for
        //-- error message
        if (_cdResolver.error()) {
            message(_cdResolver.getErrorMessage());
        }
        else {
            buf.setLength(0);
            buf.append("unable to find or create a ClassDescriptor for class: ");
            buf.append(_class.getName());
            message(buf.toString());
        }
        return classDesc;
    } //-- getMarshalInfo


    /**
     * Finds and returns a ClassDescriptor for the given class. If
     * a ClassDescriptor could not be found one will attempt to
     * be generated.
     * @param className the name of the class to get the Descriptor for
    **/
    private XMLClassDescriptor getClassDescriptor
        (String className, ClassLoader loader)
    {
        if (_cdResolver == null)
            _cdResolver = new ClassDescriptorResolverImpl();

        XMLClassDescriptor classDesc = _cdResolver.resolve(className, loader);

        if (classDesc != null) return classDesc;

        //-- we couldn't create a ClassDescriptor, check for
        //-- error message
        if (_cdResolver.error()) {
            message(_cdResolver.getErrorMessage());
        }
        else {
            buf.setLength(0);
            buf.append("unable to find or create a ClassDescriptor for class: ");
            buf.append(className);
            message(buf.toString());
        }
        return classDesc;
    } //-- getClassDescriptor

    /**
     * Returns the package for the given Class
     *
     * @param type the Class to return the package of
     * @return the package for the given Class
    **/
    private String getJavaPackage(Class type) {
        if (type == null) return null;
        String pkg = type.getName();
        int idx = pkg.lastIndexOf('.');
        if (idx > 0) {
            pkg = pkg.substring(0,idx);
        }
        else pkg = "";
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
    private boolean isWhitespace(StringBuffer sb) {
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
     * Resolves the current set of waiting references for the given Id
     * @param id the id that references are waiting for
     * @param value, the value of the resolved id
    **/
    private void resolveReferences(String id, Object value)
        throws org.xml.sax.SAXException
    {
        if ((id == null) || (value == null)) return;

        ReferenceInfo refInfo = (ReferenceInfo)_resolveTable.remove(id);
        while (refInfo != null) {
            try {
                FieldHandler handler = refInfo.descriptor.getHandler();
                if (handler != null)
                    handler.setValue(refInfo.target, value);
            }
            catch(java.lang.IllegalStateException ise) {

                String err = "Attempting to resolve an IDREF: " +
                        id + "resulted in the following error: " +
                        ise.toString();
                throw new SAXException(err);
            }
            refInfo = refInfo.next;
        }
    } //-- resolveReferences

    /**
     * Converts a String to the given primitive object type
     * @param type the class type of the primitive in which
     * to convert the String to
     * @param value the String to convert to a primitive
     * @return the new primitive Object
    **/
    public static Object toPrimitiveObject(Class type, String value) {

        Object primitive = null;

        //-- I tried to order these in the order in which
        //-- (I think) types are used more frequently

        boolean isNull = ((value == null) || (value.length() == 0));

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
                primitive = new Float((float)0);
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
        // otherwise do nothing
        else
            primitive = value;

        return primitive;
    } //-- toPrimitiveObject

    /**
     * Local IDResolver
    **/
    class IDResolverImpl implements IDResolver {

        private Hashtable  _idReferences = null;
        private IDResolver _idResolver   = null;

        IDResolverImpl() {
        } //-- IDResolverImpl

        void bind(String id, Object obj) {

            if (_idReferences == null)
                _idReferences = new Hashtable();


            _idReferences.put(id, obj);

        } //-- bind

        /**
         * Returns the Object whose id matches the given IDREF,
         * or null if no Object was found.
         * @param idref the IDREF to resolve.
         * @return the Object whose id matches the given IDREF.
        **/
        public Object resolve(String idref) {

            if (_idReferences != null) {
                Object obj = _idReferences.get(idref);
                if (obj != null) return obj;
            }

            if (_idResolver != null) {
                return _idResolver.resolve(idref);
            }
            return null;
        } //-- resolve


        void setResolver(IDResolver idResolver) {
            _idResolver = idResolver;
        }

    } //-- IDResolverImpl

    /**
     * Internal class used to save state for reference resolving
    **/
    class ReferenceInfo {
        String id = null;
        Object target = null;
        XMLFieldDescriptor descriptor = null;
        ReferenceInfo next = null;

        public ReferenceInfo() {
            super();
        }

        public ReferenceInfo
            (String id, Object target, XMLFieldDescriptor descriptor)
        {
            this.id = id;
            this.target = target;
            this.descriptor = descriptor;
        }
    }


} //-- Unmarshaller

