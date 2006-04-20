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
import org.xml.sax.helpers.AttributeListImpl;
import org.xml.sax.helpers.ParserFactory;

import java.io.PrintWriter;
import java.io.Reader;
import java.io.Serializable;

import java.lang.reflect.*;

import java.util.Date;
import java.util.Hashtable;
import java.util.Stack;
import java.util.Vector;

/**
 * An unmarshaller to allowing unmarshalling of XML documents to
 * Java Objects. The Class must specify
 * the proper access methods (setters/getters) in order for instances
 * of the Class to be properly unmarshalled.
 * @author <a href="mailto:kvisco@exoffice.com">Keith Visco</a>
 * @version $Revision$ $Date$
**/
public class UnmarshalHandler implements DocumentHandler {
    
    
    //----------------------------/
    //- Private Member Variables -/
    //----------------------------/
    
    private static final Class[]  EMPTY_CLASS_ARGS  = new Class[0];
    private static final Object[] EMPTY_OBJECT_ARGS = new Object[0];
    
    private Stack            _stateInfo    = null;
    private UnmarshalState   _topState     = null;
    private Class            _topClass     = null;
    private Hashtable        _infoHash     = null;
    
    /**
     * The SAX Document Locator
    **/
    private Locator          _locator      = null;
    
    /**
     * The PrintWriter to print log information to
    **/
    private PrintWriter      _logWriter    = null;

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
     * A StringBuffer used to created Debug/Log messages
    **/
    private StringBuffer     buf           = null;
    
    
    //----------------/
    //- Constructors -/
    //----------------/
    
    /**
     * Creates a new UnmarshalHandler
     * @param _class the Class to create the UnmarshalHandler for
    **/
    protected UnmarshalHandler(Class _class) {
        super();
        _stateInfo = new Stack();
        _infoHash = new Hashtable();
        _topClass = _class;
        buf = new StringBuffer();
    } //-- UnmarshalHandler(Class)
    
    protected Object getObject() {
        if (_topState != null) return _topState.object;
        return null;
    } //-- getObject
    
    
    /**
     * Turns debuging on or off. If no Log Writer has been set, then
     * System.out will be used to display debug information
     * @param debug the flag indicating whether to generate debug information.
     * A value of true, will turn debuggin on. 
     * @see #setLogWriter.
    **/
    public void setDebug(boolean debug) {
        this.debug = debug;
        
        if (debug && (_logWriter == null)) {
            _logWriter = new PrintWriter(System.out, true);
            killWriter = true;
        }
        if ((!debug) && killWriter) {
            _logWriter = null;
            killWriter = false;
        }
    } //-- setDebug
    
    /**
     * Sets the PrintWriter used for printing log messages
     * @param printWriter the PrintWriter to use when printing
     * log messages
    **/
    public void setLogWriter(PrintWriter printWriter) {
        this._logWriter = printWriter;
        killWriter = false;
    } //-- setLogWriter
    
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
        UnmarshalState state = (UnmarshalState)_stateInfo.peek();
        
        if (state.buffer == null) state.buffer = new StringBuffer();
        state.buffer.append(ch, start, length);
    } //-- characters
    
    
    public void endDocument() 
        throws org.xml.sax.SAXException
    {
        //-- do nothing for now
    } //-- endDocument
    
    
    public void endElement(String name) 
        throws org.xml.sax.SAXException
    {
        
        //System.out.println("#endElement: " + name);
        
        if (_stateInfo.empty()) {
            throw new SAXException("missing start element: " + name);
        }
        
        if (hasNameSpace(name)) {
            name = getLocalPart(name);
        }
        
        UnmarshalState state = (UnmarshalState) _stateInfo.pop();
        //-- make sure we have the correct closing tag
        MarshalDescriptor descriptor = state.descriptor;
        if (!state.elementName.equals(name)) {            
            String err = "error in xml, expecting </" + state.elementName;
            err += ">, but recieved </" + name + "> instead.";
            throw new SAXException(err);
        }        
        
        //-- check for ignoring object
        if ((state.object == null) && (!state.type.isPrimitive()))
            return;
        
        //-- clean up current Object
        Class _class = state.type;
        
        if ((_class == String.class) || (_class.isPrimitive())) {
            
            String str = null;
            
            if (state.buffer != null) {
                str = state.buffer.toString();
            }
            if (_class == String.class)
                state.object = str;
            else
                state.object = MarshalHelper.toPrimitiveObject(_class,str);
        }
        
        
        
        //-- Add to parent Object if necessary
        if (_stateInfo.empty()) return;
        
        //-- check for character content
        if ((state.buffer != null) && 
            (state.buffer.length() > 0) &&
            (state.mInfo != null)) {
            
            MarshalDescriptor mdesc = state.mInfo.getContentDescriptor();
            if (mdesc != null) {
                try {
                    mdesc.setValue(state.object, state.buffer.toString());
                }
                catch(java.lang.IllegalAccessException iae) {
                    String err = "unable to add text content to ";
                    err += state.descriptor.getXMLName();
                    throw new SAXException(err);
                }
                catch(java.lang.reflect.InvocationTargetException itx) {
                    String err = "unable to add text content to ";
                    err += state.descriptor.getXMLName();
                    throw new SAXException(err);
                }
            }
        }
        
        //-- we can return if we already set the Object
        //-- in it's parent
        if (descriptor.isIncremental()) return;
        
        Object val = state.object;
        //-- get target object
        state = (UnmarshalState) _stateInfo.peek();
        try {
            descriptor.setValue(state.object, val);
        }
        catch(java.lang.IllegalAccessException iae) {
            String err = "unable to add \"" + name + "\" to ";
            err += state.descriptor.getXMLName();
            throw new SAXException(err);
        }
        catch(java.lang.reflect.InvocationTargetException itx) {
            String err = "unable to add \"" + name + "\" to ";
            err += state.descriptor.getXMLName();
            err += " due to an InvocationTargetException: " + itx.getMessage();
            throw new SAXException(err);
        }

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
    
    public void startDocument() 
        throws org.xml.sax.SAXException 
    {
        //-- do nothing for now
    } //-- startDocument
    
    public void startElement(String name, AttributeList atts) 
        throws org.xml.sax.SAXException
    {
        
        
        //System.out.println("#startElement: " + name);
        
        //-- handle namespaces
        
        String namespace = null;
        
        if (hasNameSpace(name)) {
            name = getLocalPart(name);
        }
        
        UnmarshalState state = null;
        
        if (_stateInfo.empty()) {
            //-- Initialize since this is the first element
            _topState = new UnmarshalState();
            _topState.elementName = name;
            
            SimpleMarshalDescriptor smd
                = new SimpleMarshalDescriptor(_topClass, name, name);
            
            _topState.descriptor = smd;
            
            //-- look for MarshalInfo
            MarshalInfo mInfo = getMarshalInfo(_topClass);
            smd.setMarshalInfo(mInfo);
            
            if (mInfo == null) {
                //-- report error
			    if ((!_topClass.isPrimitive()) &&
                    (!Serializable.class.isAssignableFrom( _topClass )))
                    throw new SAXException(MarshalException.NON_SERIALIZABLE_ERR);
                else {
                    String err = "unable to create MarshalInfo for class: ";
                    throw new SAXException(err + _topClass.getName());
                }
            }
            _topState.type = _topClass;
            //-- try to create instance of the given Class
            String err = null;
            try {
                _topState.object = _topClass.newInstance();
            }
            catch(java.lang.NoSuchMethodError nsme) {
                err = "no default constructor for class: " + _topClass.getName();
                throw new SAXException(err);
            }
            catch(java.lang.IllegalAccessException iae) {
                err = iae.getMessage();
            }
            catch(java.lang.InstantiationException ie) {
                err = ie.getMessage();
            }
            if (_topState.object == null) {
                String msg = "unable to instantiate " + 
                    _topClass.getName();
                if (err != null) msg = msg + "; " + err;
                throw new SAXException(msg);
            }
            
            _stateInfo.push(_topState);
            processAttributes(atts, mInfo);
            return;
        }
        
        
        //-- get MarshalDescriptor for the given element
            
        UnmarshalState parentState = (UnmarshalState)_stateInfo.peek();
        

        //-- create new state object
        state = new UnmarshalState();
        state.elementName = name;
        _stateInfo.push(state);
        
        //-- make sure we should proceed
        if (parentState.object == null) return;
        
        Class _class = null;
            
            
        MarshalInfo mInfo = parentState.descriptor.getMarshalInfo();
        if (mInfo == null) {
           mInfo = getMarshalInfo(parentState.object.getClass());
        }
        
        //-- find Descriptor
        MarshalDescriptor[] descriptors = mInfo.getElementDescriptors();
        MarshalDescriptor descriptor = null;
        for (int i = 0; i < descriptors.length; i++) {
            //-- check for null here, since I actually ran into this
            //-- problem
            if (descriptors[i] == null) continue;
            
            if (descriptors[i].matches(name)) {
                descriptor = descriptors[i];
                break;
            }
        }
            
        //-- Find object type and create new Object of that type
        if (descriptor != null) {
            
            state.descriptor = descriptor;
            
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
            
            //-- Find class to instantiate
            mInfo = descriptor.getMarshalInfo();
            
            
            //-- check descriptor for create method...
            Method creator = descriptor.getCreateMethod();
            
            if (creator == null) {
            
                _class = descriptor.getFieldType();
                
                if (_class == Object.class) {
                    
                    //-- create class name
                    String cname = MarshalHelper.toJavaName(name,true);
                    
                    //-- use parent to package information
                    String pkg = parentState.object.getClass().getName();
                    int idx = pkg.lastIndexOf('.');
                    if (idx > 0) {
                        pkg = pkg.substring(0,idx);
                        cname = pkg + cname;
                    }
                    mInfo = MarshalHelper.getMarshalInfo(cname);
                    if (mInfo != null) {
                        _class = mInfo.getClassType();
                    }
                }
                state.type = _class;
                
                //-- instantiate class
                try {
                    if (!_class.isPrimitive())
                        state.object = _class.newInstance();
                }
                catch(java.lang.NoSuchMethodError nsme) {
                    String err = "no default constructor for class: "; 
                    err += _class.getName();
                    throw new SAXException(err);
                }
                catch(java.lang.IllegalAccessException iae) { /* :-) */ }
                catch(java.lang.InstantiationException ie) { /* ;-) */};
                
            }
            //-- use creator method to create a new object
            else {
                
                if (debug) {
                    buf.setLength(0);
                    buf.append("debug: ");
                    buf.append("A create method was supplied for element '");
                    buf.append(name);
                    buf.append("'.");
                    message(buf.toString());
                }
                try {
                    
                    state.object = creator.invoke(parentState.object,
                                              EMPTY_OBJECT_ARGS);
                    //-- assign new class value                          
                    if (state.object != null) 
                        _class = state.object.getClass();
                        
                    state.type = _class;
                }
                catch (java.lang.IllegalAccessException iae) {
                    StringBuffer msg = new StringBuffer();
                    msg.append("unable to use the create method for: ");
                    msg.append(name);
                    msg.append(" - ");
                    msg.append(iae.toString());
                    message(msg.toString());
                }
                catch (java.lang.reflect.InvocationTargetException ite) {
                    StringBuffer msg = new StringBuffer();
                    msg.append("unable to use the create method for: ");
                    msg.append(name);
                    msg.append(" - ");
                    msg.append(ite.toString());
                    message(msg.toString());
                }
            }            
            
            if (mInfo == null) {
                mInfo = getMarshalInfo(_class);
            }
            state.mInfo = mInfo;
            
            if ((state.object == null) && (!state.type.isPrimitive())) {
                StringBuffer err = new StringBuffer("unable to unmarshal: ");
                err.append(name);
                err.append("\n");
                err.append("unable to instantiate: ");
                err.append(_class.getName());
                throw new SAXException(err.toString());
            }
            
            //-- assign object, if incremental
            
            if (descriptor.isIncremental()) {
                
                if (debug) {
                    StringBuffer msg = new StringBuffer();
                    msg.append("processing incrementally for element: ");
                    msg.append(name);
                    message(msg.toString());
                }
                
                try {
                    descriptor.setValue(parentState.object, state.object);
                }
                catch(java.lang.IllegalAccessException iae) {
                    String err = "unable to add \"" + name + "\" to ";
                    err += parentState.descriptor.getXMLName();
                    throw new SAXException(err);
                }
                catch(java.lang.reflect.InvocationTargetException itx) {
                    String err = "unable to add \"" + name + "\" to ";
                    err += parentState.descriptor.getXMLName();
                    throw new SAXException(err);
                }
            }
        }
        else {
            String msg = "unable to find MarshalDescriptor for: ";
            message(msg+name);
        }
        
        if (state.object != null)
            processAttributes(atts, mInfo);
        else if (!state.type.isPrimitive()){
            StringBuffer msg = new StringBuffer("current object for '");
            msg.append(name);
            msg.append("\' is null, ignoring attributes.");
            message(msg.toString());
        }
        
    } //-- void startElement(String, AttributeList) 
    
    
      //-------------------/
     //- Private Methods -/
    //-------------------/
    
    /**
     * Returns true if the given NCName (element name) is qualified
     * with a namespace prefix
     * @return true if the given NCName is qualified with a namespace
     * prefix
    **/
    private boolean hasNameSpace(String ncName) {
        return (ncName.indexOf(':')>0 );
    } //-- hasNameSpace
    
    /**
     * Returns the local part of the given NCName. The local part is anything
     * following the namespace prefix. If there is no namespace prefix
     * the returned name will be the same as the given name.
     * @return the local part of the given NCName.
    **/
    private String getLocalPart(String ncName) {
        int idx = ncName.indexOf(':');
        if (idx >= 0) return ncName.substring(idx+1);
        return ncName;
    } //-- getLocalPart
    
    
    /**
     * Processes the given attribute list, and attempts to add each 
     * Attribute to the current Object on the stack
    **/
    private void processAttributes(AttributeList atts, MarshalInfo mInfo) 
        throws org.xml.sax.SAXException
    {
        
        //System.out.println("#processAttributes");
        if (atts == null) {
            
            if ((mInfo != null) 
                && (mInfo.getAttributeDescriptors().length > 0)
                && (debug))
            {
                UnmarshalState state = (UnmarshalState)_stateInfo.peek();
                buf.setLength(0);
                buf.append("AttributeList for '");
                buf.append(state.elementName);
                buf.append("' is null, but Attribute Descriptors exist.");
                message(buf.toString());
            }
            return;
        }
        
        UnmarshalState state = (UnmarshalState)_stateInfo.peek();
        Object object = state.object;
        
        if (mInfo == null) {
            mInfo = state.mInfo;
            if (mInfo == null) {
                buf.setLength(0);
                buf.append("No MarshalInfo for '");
                buf.append(state.elementName);
                buf.append("', cannot process attributes.");
                message(buf.toString());
                return;
            }
        }
        
        
        MarshalDescriptor[] descriptors = mInfo.getAttributeDescriptors();
        
        for (int i = 0; i < descriptors.length; i++) {
            
            MarshalDescriptor descriptor = descriptors[i];
            
            if (!descriptor.getAccessRights().isWritable()) {
                if (debug) {
                    buf.setLength(0);
                    buf.append("Attribute '");
                    buf.append(descriptor.getXMLName());
                    buf.append("' is read-only - skipping.");
                    message(buf.toString());
                }
                continue;
            }
            
            String attName = descriptor.getXMLName();
            String attValue = atts.getValue(attName);
            if (attValue == null) {
                //-- error handling
                if (debug) {
                    buf.setLength(0);
                    buf.append("no attribute value with name \'");
                    buf.append(attName);
                    buf.append("\' exists in element \'");
                    buf.append(state.elementName);
                    buf.append("\'");
                    message(buf.toString());
                }
                continue;
            }
            
            Object value = attValue;
            
            //-- check for proper type
            Class type = descriptor.getFieldType();
            
            if (type.isPrimitive())
                value = MarshalHelper.toPrimitiveObject(type, attValue);
                
            try {
                descriptor.setValue(object, value);
            }
            catch(java.lang.IllegalAccessException iae) {
                String err = "unable to add attribute \"" + attName + "\" to ";
                err += state.descriptor.getXMLName();
                throw new SAXException(err);
            }
            catch(java.lang.reflect.InvocationTargetException itx) {
                String err = "unable to add attribute \"" + attName + "\" to ";
                err += state.descriptor.getXMLName();
                throw new SAXException(err);
            }
        }
        
    } //-- processAttributes
    
    /**
     * Sends a message to all observers. Currently the only observer is
     * the logger.
     * @param msg the message to send to all message observers
    **/
    private void message(String msg) {
        if (_logWriter != null) {
            _logWriter.print("[");
            _logWriter.print(new Date());
            _logWriter.print("] ");
            _logWriter.println(msg);
        }
    } //-- message
    
    /**
     * Finds and returns a MarshalInfo for the given class. If
     * a MarshalInfo could not be found one will attempt to 
     * be generated. This method makes use of a cache
     * for performance reasons.
     * @param _class the Class to get the MarshalInfo for
    **/
    private MarshalInfo getMarshalInfo(Class _class) 
        throws SAXException
    {
        if (_class == null) return null;
        
        MarshalInfo mInfo = (MarshalInfo)_infoHash.get(_class);
        if (mInfo != null) return mInfo;
        try {
            mInfo = MarshalHelper.getMarshalInfo(_class, _logWriter);
        }
        catch(java.io.IOException ioe) {
            throw new SAXException(ioe.getMessage());
        }
        if (mInfo != null) _infoHash.put(_class, mInfo);
        else {
            buf.setLength(0);
            buf.append("unable to find or create a MarshalInfo for class: ");
            buf.append(_class.getName());
            message(buf.toString());
        }
        return mInfo;
    } //-- getMarshalInfo
    
} //-- Unmarshaller

