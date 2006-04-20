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
 * Copyright 2001 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id$
 */

package org.exolab.castor.xml.schema.util;

import org.exolab.castor.xml.schema.*;

import org.xml.sax.*;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Stack;
import java.util.Vector;



/**
 * A Utility class which will attempt to create an XML Schema
 * Object Model based on a given XML instance document.
 *
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a>
 * @version $Revision$ $Date$ 
**/
public final class XMLInstance2SchemaHandler
    implements DocumentHandler, org.xml.sax.ErrorHandler
{


    private static final String XMLNS          = "xmlns";
    private static final String DEFAULT_PREFIX = "xsd";
      //--------------------/
     //- Member Variables -/
    //--------------------/
    
    /**
     * The Sax locator
    **/
    private Locator _locator = null;
    
    /**
     * The schema we are creating
    **/
    private Schema _schema = null;

    /**
     * The stack of element declarations
    **/
    private Stack _siStack = null;
    
    private String _nsPrefix = null;
    
    private Order  _defaultGroupOrder = Order.seq;
    
      //----------------/
     //- Constructors -/
    //----------------/

    /**
     * Creates a new XMLInstance2SchemaHandler
     *
    **/
    public XMLInstance2SchemaHandler() {
        this(null);
    } //-- XMLInstance2SchemaHandler

    /**
     * Creates a new XMLInstance2SchemaHandler
     *
    **/
    public XMLInstance2SchemaHandler(Schema schema) {
        super();
        
        _siStack   = new Stack();
        
        _schema = schema;
        //-- create Schema and initialize
        if (_schema == null) {
            _schema = new Schema();
            _schema.addNamespace(DEFAULT_PREFIX, Schema.DEFAULT_SCHEMA_NS);
            _nsPrefix = DEFAULT_PREFIX;
        }
        //-- find or declare namespace prefix
        else {
            _nsPrefix = null;
            Hashtable namespaces = _schema.getNamespaces();
            Enumeration enum = namespaces.keys();
            while (enum.hasMoreElements()) {
                String key = (String) enum.nextElement();
                if (namespaces.get(key).equals(Schema.DEFAULT_SCHEMA_NS)) {
                    _nsPrefix = key;
                    break;
                }
            }
            if (_nsPrefix == null) {
                _schema.addNamespace(DEFAULT_PREFIX, Schema.DEFAULT_SCHEMA_NS);
                _nsPrefix = DEFAULT_PREFIX;
            }
        }
    } //-- XMLInstance2SchemaHandler

      //-----------/
     //- Methods -/
    //-----------/
    
    /**
     * Returns the XML Schema object that is being used by this handler
     *
     * @return the XML Schema object that is being used by this handler
    **/
    public Schema getSchema() {
        return _schema;
    }
    
    /**
     * This method is used to set the default group type. Either
     * "sequence" or "all". The default is "sequence".
     *
     * @param order the default group order to use.
    **/
    protected void setDefaultGroupOrder(Order order) {
        _defaultGroupOrder = order;
    } //-- setDefaultGroupOrder
      
    //---------------------------------------/
    //- org.xml.sax.DocumentHandler methods -/
    //---------------------------------------/
    
    public void characters(char[] ch, int start, int length) 
        throws org.xml.sax.SAXException
    {
        if (_siStack.isEmpty()) return;
        
        StateInfo sInfo = (StateInfo)_siStack.peek();
        
        if (sInfo.buffer == null) {
            sInfo.buffer = new StringBuffer();
        }
        sInfo.buffer.append(ch, start, length);
        
        if (sInfo.complex) {
            sInfo.mixed = true;    
        }
    } //-- characters
    
    public void endDocument()
        throws org.xml.sax.SAXException
    {
        //-- do nothing
        
    } //-- endDocument
    
    public void endElement(String name) 
        throws org.xml.sax.SAXException
    {
        
        //-- strip namespace prefix
        int idx = name.indexOf(':');
        if (idx >= 0) {
            name = name.substring(idx+1);
        }
        
        StateInfo sInfo = (StateInfo) _siStack.pop();
        
        //-- if we don't have a type, it means there are no
        //-- children and therefore the type is a simpleType
        if (sInfo.element.getType() == null) {
            //-- create SimpleType (guess type)
            String typeName = _nsPrefix + ':' + 
                DatatypeHandler.guessType(sInfo.buffer.toString());
            sInfo.element.setTypeReference(typeName);
        }
        
        //-- put element into parent element or as top-level in schema
        if (!_siStack.isEmpty()) {
            StateInfo parentInfo = (StateInfo)_siStack.peek();
            ComplexType type = (ComplexType) parentInfo.element.getType();
            Group group = null;
            if (type == null) {
                parentInfo.complex = true;
                type = new ComplexType(_schema);
                parentInfo.element.setType(type);
                group = new Group();
                group.setOrder(_defaultGroupOrder);
                type.addGroup(group);
                group.addElementDecl(sInfo.element);
            }
            else {
                group = (Group) type.getParticle(0);
                //-- check for another element declaration with
                //-- same name ...
                ElementDecl element = group.getElementDecl(name);
                boolean checkGroupType = false;
                if (element != null) {
                    //-- if complex...merge definition
                    if (sInfo.complex) {
                        merge(element, sInfo.element);
                    }
                    element.setMaxOccurs(Particle.UNBOUNDED);
                    checkGroupType = true;
                }
                else {
                    group.addElementDecl(sInfo.element);
                }
                
                //-- change group type if necessary
                if (checkGroupType && (group.getOrder() == Order.seq)) {
                    //-- make sure element is last item in group,
                    //-- otherwise we need to switch to all
                    boolean found = false;
                    boolean changeType = false;
                    for (int i = 0; i < group.getParticleCount(); i++) {
                        if (found) {
                            changeType = true;
                            break;
                        }
                        if (element == group.getParticle(i)) found = true;
                    }
                    if (changeType) {
                        group.setOrder(Order.all);
                    }
                }
            }
        }
        else {
            _schema.addElementDecl(sInfo.element);
        }
        
    } //-- endElement


    public void ignorableWhitespace(char[] ch, int start, int length) 
        throws org.xml.sax.SAXException
    {
        //-- do nothing
        
    } //-- ignorableWhitespace

    public void processingInstruction(String target, String data) 
        throws org.xml.sax.SAXException
    {
        //-- do nothing

    } //-- processingInstruction
    
    public void setDocumentLocator(Locator locator) {
        this._locator = locator;
    } //-- setDocumentLocator
    
    public void startDocument()
        throws org.xml.sax.SAXException
    {
        //-- do nothing
        
    } //-- startDocument

    
    public void startElement(String name, AttributeList atts) 
        throws org.xml.sax.SAXException
    {
        
        //-- strip namespace prefix
        int idx = name.indexOf(':');
        if (idx >= 0) {
            name = name.substring(idx+1);
        }

        StateInfo sInfo = null;
        
        //-- if we are currently in another element 
        //-- definition...flag as complex content
        if (!_siStack.isEmpty()) {
            sInfo = (StateInfo)_siStack.peek();
            sInfo.complex = true;
        }
        
        //-- create current holder for stateInformation
        sInfo = new StateInfo();
        _siStack.push(sInfo);
        
        //-- create element definition
        sInfo.element = new ElementDecl(_schema, name);
        
        //-- create attributes
        for (int i = 0; i < atts.getLength(); i++) {
            
            String attName = atts.getName(i);
            
            //-- skip namespace declarations
            if (attName.equals(XMLNS)) continue;
            String prefix = "";
            idx = name.indexOf(':');
            if (idx >= 0) {
                prefix = name.substring(0, idx);
                name = name.substring(idx+1);
            }
            if (prefix.equals(XMLNS)) continue;
            
            AttributeDecl attr = new AttributeDecl(_schema, name);
            
            //-- guess simple type
            String typeName = _nsPrefix + ':' + 
                DatatypeHandler.guessType(atts.getValue(i));
                
            attr.setSimpleTypeReference(typeName);
            
            sInfo.attributes.addElement(attr);
        }
        
    } //-- startElement
    

    //------------------------------------/
    //- org.xml.sax.ErrorHandler methods -/
    //------------------------------------/
    
    public void error(SAXParseException exception)
        throws org.xml.sax.SAXException
    {
        throw exception;
        
    } //-- error
    
    public void fatalError(SAXParseException exception)
        throws org.xml.sax.SAXException
    {
        throw exception;
        
    } //-- fatalError
    
    
    public void warning(SAXParseException exception)
        throws org.xml.sax.SAXException
    {
        throw exception;
        
    } //-- warning
    
    //-------------------------/
    //- local private methods -/
    //-------------------------/
    
    /**
     * Determines if the given sequence of characters consists
     * of whitespace characters
     * @param chars an array of characters to check for whitespace
     * @param start the start index into the character array
     * @param length the number of characters to check
     * @return true if the characters specficied consist only
     * of whitespace characters
    **/
    private static boolean isWhiteSpace(char[] chars, int start, int length) {
        int max = start+length;
        for (int i = start; i < max; i++) {
            char ch = chars[i];
            switch(ch) {
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
    } //-- isWhiteSpace

    /**
     * Merges the two element declarations. The resulting
     * merge is placed in ElementDecl e1.
     *
     * @param e1 the main ElementDecl 
     * @param e2 the secondary ElementDecl to merge with e1
    **/
    private void merge(ElementDecl e1, ElementDecl e2) 
        throws SchemaException
    {
        
        //-- If types are simple types...simply ignore for now
        XMLType e1Type = e1.getType();
        XMLType e2Type = e2.getType();
         
        if (e1Type.isSimpleType() || e2Type.isSimpleType())
            return;
        
        //-- loop through all element/attribute declarations
        //-- of e2 and add them to e1 if they do not already exist
        //-- and mark them as optional
        
        Group e1Group = (Group)((ComplexType)e1Type).getParticle(0);
        Group e2Group = (Group)((ComplexType)e2Type).getParticle(0);
        
        Enumeration enum = e2Group.enumerate();
        while (enum.hasMoreElements()) {
            Particle particle = (Particle)enum.nextElement();
            if (particle.getStructureType() == Structure.ELEMENT) {
                ElementDecl element = (ElementDecl)particle;
                ElementDecl main = e1Group.getElementDecl(element.getName());
                if (main == null) {
                    e1Group.addElementDecl(element);
                    element.setMinOccurs(0);
                }
            }
        }
        
        //-- loop through all element/attribute declarations
        //-- of e1 and if they do not exist in e2, simply
        //-- mark them as optional
        enum = e1Group.enumerate();
        while (enum.hasMoreElements()) {
            Particle particle = (Particle)enum.nextElement();
            if (particle.getStructureType() == Structure.ELEMENT) {
                ElementDecl element = (ElementDecl)particle;
                if (e2Group.getElementDecl(element.getName()) == null) {
                    element.setMinOccurs(0);
                }
            }
        }
        
        
    } //-- merge
    
    /**
     * Inner-class to hold state
    **/
    class StateInfo {
        ElementDecl  element      = null;
        Vector       attributes   = null;
        StringBuffer buffer       = null;
        boolean      mixed        = false;
        boolean      complex      = false;
        
        public StateInfo() {
            super();
            attributes = new Vector();
        }
        
    } //-- StateInfo
    
} //--


