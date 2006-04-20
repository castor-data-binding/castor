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

import org.exolab.castor.xml.Namespaces;
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
            Namespaces namespaces = _schema.getNamespaces();
            Enumeration enum = namespaces.getLocalNamespacePrefixes();
            while (enum.hasMoreElements()) {
                String key = (String) enum.nextElement();
                if (namespaces.getNamespaceURI(key).equals(Schema.DEFAULT_SCHEMA_NS)) {
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
        //-- children and therefore the type is a simpleType or
        //-- simpleContent
        if ((sInfo.element.getType() == null) && (sInfo.buffer != null)) {
            
            //-- create SimpleType (guess type)
            String typeName = _nsPrefix + ':' + 
                DatatypeHandler.guessType(sInfo.buffer.toString());
            sInfo.element.setTypeReference(typeName);
            //-- simpleContent
            if (sInfo.attributes.size() > 0) {
                ComplexType cType = new ComplexType(_schema);
                //-- SHOULD CHANGE THIS TO SIMPLE CONTENT WHEN
                //-- SCHEMA WRITER BUGS ARE FIXED
                cType.setContentType(ContentType.mixed);
                sInfo.element.setType(cType);
                Group group = new Group();
                group.setOrder(_defaultGroupOrder);
                //-- add attributes
                try {
                    cType.addGroup(group);
                    for (int i = 0; i < sInfo.attributes.size(); i++) {
                        AttributeDecl attDecl = 
                            (AttributeDecl)sInfo.attributes.elementAt(i);
                        cType.addAttributeDecl(attDecl);
                    }
                }
                catch(SchemaException sx) {
                    throw new SAXException(sx);
                }
            }
        }
        else {
            ComplexType cType = (ComplexType)sInfo.element.getType();
            
            if ((cType == null) && (sInfo.attributes.size() > 0)) {
                cType = new ComplexType(_schema);
                sInfo.element.setType(cType);
                Group group = new Group();
                group.setOrder(_defaultGroupOrder);
                //-- add attributes
                try {
                    cType.addGroup(group);
                }
                catch(SchemaException sx) {
                    throw new SAXException(sx);
                }
            }
            
            if (cType != null) {
                //-- add attributes
                try {
                    for (int i = 0; i < sInfo.attributes.size(); i++) {
                        AttributeDecl attDecl = 
                            (AttributeDecl)sInfo.attributes.elementAt(i);
                        cType.addAttributeDecl(attDecl);
                    }
                }
                catch(SchemaException sx) {
                    throw new SAXException(sx);
                }
            }
        }
        
        //-- put element into parent element or as top-level in schema
        if (!_siStack.isEmpty()) {
            StateInfo parentInfo = (StateInfo)_siStack.peek();
            ComplexType type = (ComplexType) parentInfo.element.getType();
            Group group = null;
            if ((type == null) || (type.getParticleCount() == 0)) {
                if (type == null) {
                    parentInfo.complex = true;
                    type = new ComplexType(_schema);
                    parentInfo.element.setType(type);
                }
                group = new Group();
                group.setOrder(_defaultGroupOrder);
                try {
                    type.addGroup(group);
                    //-- add element
                    group.addElementDecl(sInfo.element);
                }
                catch(SchemaException sx) {
                    throw new SAXException(sx);
                }
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
                        try {
                            merge(element, sInfo.element);
                        }
                        catch(SchemaException sx) {
                            throw new SAXException(sx);
                        }
                    }
                    element.setMaxOccurs(Particle.UNBOUNDED);
                    checkGroupType = true;
                }
                else {
                    try {
                        group.addElementDecl(sInfo.element);
                    }
                    catch(SchemaException sx) {
                        throw new SAXException(sx);
                    }
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
            try {
                _schema.addElementDecl(sInfo.element);
                
                //-- make complexType top-level also
                //XMLType type = sInfo.element.getType();
                //if ((type != null) && (type.isComplexType())) {
                //    if (type.getName() == null) {
                //        type.setName(sInfo.element.getName() + "Type");
                //        _schema.addComplexType((ComplexType)type);
                //    }
                //}
            }
            catch(SchemaException sx) {
                throw new SAXException(sx);
            }
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
        
        boolean topLevel = false;
        //-- if we are currently in another element 
        //-- definition...flag as complex content
        if (!_siStack.isEmpty()) {
            sInfo = (StateInfo)_siStack.peek();
            sInfo.complex = true;
        }
        else {
            topLevel = true;
        }
        
        //-- create current holder for stateInformation
        sInfo = new StateInfo();
        sInfo.topLevel = topLevel;
        _siStack.push(sInfo);
        
        //-- create element definition
        sInfo.element = new ElementDecl(_schema, name);
        
        //-- create attributes
        for (int i = 0; i < atts.getLength(); i++) {
            
            String attName = atts.getName(i);
            
            //-- skip namespace declarations
            if (attName.equals(XMLNS)) continue;
            String prefix = "";
            idx = attName.indexOf(':');
            if (idx >= 0) {
                prefix = attName.substring(0, idx);
                attName = attName.substring(idx+1);
            }
            if (prefix.equals(XMLNS)) continue;
            
            AttributeDecl attr = new AttributeDecl(_schema, attName);
            
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
        
        XMLType e1Type = e1.getType();
        XMLType e2Type = e2.getType();
         
        //-- Make sure types are not null and if so create them
        if (e1Type == null) {
            if (e2Type == null) return; //-- nothing to merge
            else {
                if (e2Type.isSimpleType()) {
                    e1.setType(e2Type);
                }
                else {
                    ComplexType cType = new ComplexType(_schema);
                    Group group = new Group();
                    group.setOrder(_defaultGroupOrder);
                    cType.addGroup(group);
                    e1.setType(cType);
                    e1Type = cType;
                }
            }
        }
        else if (e2Type == null) {
            if (e1Type.isSimpleType()) {
                e2.setType(e1Type);
            }
            else {
                ComplexType cType = new ComplexType(_schema);
                Group group = new Group();
                group.setOrder(_defaultGroupOrder);
                cType.addGroup(group);
                e2.setType(cType);
                e2Type = cType;
            }
        }
        
        //-- both simple types
        if (e1Type.isSimpleType() && e2Type.isSimpleType()) {
            if (!e1Type.getName().equals(e2Type.getName())) {
                String typeName = _nsPrefix + ':' +
                    DatatypeHandler.whichType(e1Type.getName(),
                        e2Type.getName());
                e1.setType(null);
                e1.setTypeReference(typeName);
            }
            return;
        }
        //-- e1 is simple, e2 is complex
        else if (e1Type.isSimpleType()) {
            ComplexType cType = new ComplexType(_schema);
            e1.setType(cType);
            Group group = new Group();
            group.setOrder(_defaultGroupOrder);
            cType.addGroup(group);
            cType.setContentType(ContentType.mixed);
            e1Type = cType;
            //-- do not return here...we need to now treat as both
            //-- were complex
        }
        //-- e2 is simple, e1 is complex
        else if (e2Type.isSimpleType()) {
            ComplexType cType = new ComplexType(_schema);
            e2.setType(cType);
            Group group = new Group();
            group.setOrder(_defaultGroupOrder);
            cType.addGroup(group);
            cType.setContentType(ContentType.mixed);
            e2Type = cType;
            //-- do not return here...we need to now treat as both
            //-- were complex
        }
        
        //-- both complex types
        ComplexType cType1 = (ComplexType)e1Type;
        ComplexType cType2 = (ComplexType)e2Type;
        
        //-- loop through all element/attribute declarations
        //-- of e2 and add them to e1 if they do not already exist
        //-- and mark them as optional
        
        Group e1Group = (Group) cType1.getParticle(0);
        if (e1Group == null) {
            e1Group = new Group();
            e1Group.setOrder(_defaultGroupOrder);
            cType1.addGroup(e1Group);
            
        }
        Group e2Group = (Group) cType2.getParticle(0);
        if (e2Group == null) {
            e2Group = new Group();
            e2Group.setOrder(_defaultGroupOrder);
            cType2.addGroup(e2Group);
            
        }
        
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
                else {
                    merge(main, element);
                }
            }
        }
        //-- add all attributes from type2
        enum = cType2.getAttributeDecls();
        
        while (enum.hasMoreElements()) {
            //-- check for attribute with same name
            AttributeDecl attNew =  (AttributeDecl)enum.nextElement();
                    
            String attName = attNew.getName();
            AttributeDecl attPrev = cType1.getAttributeDecl(attName);
            if (attPrev == null) {
                attNew.setUse(AttributeDecl.USE_OPTIONAL);
                cType1.addAttributeDecl(attNew);
            }
            else {
                String type1 = attPrev.getSimpleType().getName();
                String type2 = attNew.getSimpleType().getName();
                if (!type1.equals(type2)) {
                    String typeName = _nsPrefix + ':' + 
                        DatatypeHandler.whichType(type1, type2);
                    attPrev.setSimpleTypeReference(typeName);                        }
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
        Namespaces   namespaces   = null;
        ElementDecl  element      = null;
        Vector       attributes   = null;
        StringBuffer buffer       = null;
        boolean      mixed        = false;
        boolean      complex      = false;
        boolean      topLevel     = false;
        
        public StateInfo() {
            super();
            attributes = new Vector();
        }
        
    } //-- StateInfo
    
} //--


