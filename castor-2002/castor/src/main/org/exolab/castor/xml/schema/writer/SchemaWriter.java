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
 * Copyright 1999-2001 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id$
 */

package org.exolab.castor.xml.schema.writer;

import java.io.Writer;
import java.io.IOException;
import java.util.Enumeration;

import org.exolab.castor.util.Configuration;
import org.exolab.castor.xml.schema.*;

import org.xml.sax.DocumentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributeListImpl;

import org.apache.xml.serialize.Serializer;

/**
 * A class for serializing Schema models
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a>
 * @version $Revision$ $Date$
**/
public class SchemaWriter {
    
    private DocumentHandler   _handler = null;
    
    private AttributeListImpl _atts = new AttributeListImpl();
        
    public static boolean enable = false;
    
    /**
     * Creates a new SchemaWriter for the given Writer
     *
     * @param writer the Writer to serialize to 
    **/
    public SchemaWriter(Writer writer) 
        throws IOException
    {
        if (!enable) unsupported();
            
        Serializer serializer = Configuration.getSerializer();

        if (serializer == null)
            throw new IOException("Unable to obtain serailizer");

        serializer.setOutputCharStream( writer );
        
        DocumentHandler handler = serializer.asDocumentHandler();
        
        if ( handler == null ) {
            String err = "The following serializer is not SAX capable: ";
            err += serializer.getClass().getName();
            err += "; cannot proceed.";
            throw new IOException( err );
        }
        
        _handler = handler;
        
    } //-- SchemaWriter
    
    /**
     * Creates a new SchemaWriter for the given DocumentHandler
     *
     * @param handler the DocumentHandler to send events to
    **/
    public SchemaWriter(DocumentHandler handler) {
        
        if (!enable) unsupported();
        
        if (handler == null)
            throw new IllegalArgumentException("DocumentHandler must not be null.");
                    
        _handler = handler;
        
    } //-- SchemaWriter
    
    private static final void unsupported() {
        String err = "This class is not yet supported. " +
            " If you really wish to use it, and you promise not to " +
            " complain about unsupported features, then set " +
            " SchemaWriter.enable to true, before you attempt to construct " +
            " this class.";
            
       throw new IllegalStateException ( err );
    }
    
    public void write(Schema schema) 
        throws SAXException
    {
        if (schema == null)
            throw new IllegalArgumentException("Schema must not be null.");
            
          
        processSchema(schema);
        
    } //-- write
    

    /**
     * Processes the given annotated structure into events
     *
     * @param annotated the annotated structure to process into events
    **/
    private void processAnnotated(Annotated annotated) 
        throws SAXException
    {
        Enumeration enum = annotated.getAnnotations();
        while (enum.hasMoreElements())
            processAnnotation( (Annotation) enum.nextElement() );
            
    } //-- processAnnotated
    
    /**
     * Processes the given annotation into events
     *
     * @param annotation the annotation to process into events
    **/
    private void processAnnotation(Annotation annotation) 
        throws SAXException
    {
        
        _atts.clear();
        
        String ELEMENT_NAME = "xsd:annotation";
        
        _handler.startElement(ELEMENT_NAME, _atts);
        
        Enumeration enum = annotation.getDocumentation();
        while (enum.hasMoreElements()) {
            Documentation doc = (Documentation) enum.nextElement();
            String content = doc.getContent();
            if ((content != null) && (content.length() > 0)) {
                char[] chars = content.toCharArray();
                _handler.startElement("xsd:documentation", _atts);
                _handler.characters(chars, 0, chars.length);
                _handler.endElement("xsd:documentation");
            }
        }
            
        _handler.endElement(ELEMENT_NAME);
        
    } //-- processAnnotations

    /**
     * Processes the given attribute declaration
     *
     * @param attribute the attribute declaration to process into events
    **/
    private void processAttribute(AttributeDecl attribute)
        throws SAXException
    {
        String ELEMENT_NAME = "xsd:attribute";
        
        _atts.clear();
        
        //-- name
        String value = attribute.getName();
        
        _atts.addAttribute("name", null, value);
         
        //-- type attribute
        boolean hasAnonymousType = false;
        SimpleType type = attribute.getSimpleType();
        if (type.getName() != null) {
            _atts.addAttribute("type", null, type.getName());
        }
        else hasAnonymousType = true;
        
        //-- use flag....kinda nasty
        if (attribute.isRequired()) {
            _atts.addAttribute("use", null, "required");
        }
        else if (attribute.isFixed()) {
            _atts.addAttribute("use", null, "fixed");
            _atts.addAttribute("value", null, attribute.getValue());
        }
        
        _handler.startElement(ELEMENT_NAME, _atts);
        
        //-- process annotations
        processAnnotated(attribute);
        
        //-- process anonymous type if necessary
        if (hasAnonymousType) {
            processSimpleType(type);
        }
        
        _handler.endElement(ELEMENT_NAME);
        
        
    } //-- processAttribute

    /**
     * Processes the given complex type definition
     *
     * @param complexType the complex type definition to process into events
    **/
    private void processComplexType(ComplexType complexType)
        throws SAXException
    {
        String ELEMENT_NAME = "xsd:complexType";
        
        _atts.clear();
        
        if (complexType.isTopLevel()) {
            _atts.addAttribute("name", null, complexType.getName());
        }
        
        _handler.startElement(ELEMENT_NAME, _atts);
        
        //-- process annotations
        processAnnotated(complexType);
        
        //-- process group
        processContentModelGroup( complexType );
        
        //-- process Attributes, must appear last in a complex type
        Enumeration enum = complexType.getAttributeDecls();
        while (enum.hasMoreElements())
            processAttribute((AttributeDecl) enum.nextElement());
            
        _handler.endElement(ELEMENT_NAME);
        
    } //-- processComplexType
    
    /**
     * Processes the given ContentModelGroup
     *
     * @param contentModel the content model group to process into events
    **/
    private void processContentModelGroup(ContentModelGroup contentModel) 
        throws SAXException
    {
        Enumeration enum = contentModel.enumerate();
        while (enum.hasMoreElements()) {
            Structure structure = (Structure) enum.nextElement();
            switch (structure.getStructureType()) {
                case Structure.ELEMENT:
                    processElement( (ElementDecl) structure );
                    break;
                case Structure.GROUP:
                    processGroup( (Group) structure);
                    break;
                default:
                    break;
            }
        }
    } //-- contentModel
    
    /**
     * Processes the given element declaration
     * 
     * @param element the element declaration to process into events
    **/
    private void processElement(ElementDecl element) 
        throws SAXException
    {
        
        String ELEMENT_NAME = "xsd:element";        
        _atts.clear();
        
        
        //-- name or reference
        String value = element.getName();
        if (value != null) {
            if (element.isReference())
                _atts.addAttribute("ref", null, value);        
            else
                _atts.addAttribute("name", null, value);                
        }
        
        //-- minOccurs/maxOccurs
        int max = element.getMaxOccurs();
        int min = element.getMinOccurs();
        
        if (min != 1)
            _atts.addAttribute("minOccurs", null, Integer.toString(min));
            
        if (max < 0)
            _atts.addAttribute("maxOccurs", null, "unbounded");
        else if (max > 1)
            _atts.addAttribute("maxOccurs", null, Integer.toString(max));
            
        //-- type attribute
        boolean hasAnonymousType = false;
        if (!element.isReference()) {
            XMLType type = element.getType();
            if (type.getName() != null) {
                _atts.addAttribute("type", null, type.getName());
            }
            else hasAnonymousType = true;
        }
        
        //-- default
        String defaultValue = element.getDefaultValue();
        if (defaultValue != null) {
            _atts.addAttribute("default", null, defaultValue);
        }

        _handler.startElement(ELEMENT_NAME, _atts);
        
        //-- process annotations
        processAnnotated(element);
        
        //-- process anonymous type if necessary
        if (hasAnonymousType) {
            XMLType type = element.getType();
            if (type.isComplexType())
                processComplexType( (ComplexType) type);            
        }
        
        _handler.endElement(ELEMENT_NAME);        
        
    } //-- processElement
    
    /**
     * Processes the given group definition into SAX events
     *
     * @param group the group definition to process into SAX events
    **/
    private void processGroup(Group group)
        throws SAXException
    {
        String ELEMENT_NAME = "xsd:" + group.getOrder().toString();
        
        _atts.clear();
        
        String groupName = group.getName();
        if (groupName != null) {
            _atts.addAttribute("name", null, groupName);
        }
        
        _handler.startElement(ELEMENT_NAME, _atts);
        
        //-- process annotations
        processAnnotated(group);
        
        //-- process content model
        processContentModelGroup( group );
        
        _handler.endElement(ELEMENT_NAME);
        
    } //-- processGroup
    
    private void processSchema(Schema schema) 
        throws SAXException
    {
        
        String ELEMENT_NAME = "xsd:schema";
        
        _handler.startDocument();
        
        
        //-- namespace declaration for xsd
        _atts.addAttribute("xmlns:xsd", null, schema.getSchemaNamespace());
        
        //-- targetNS
        String value = schema.getTargetNamespace();
        if (value != null)
            _atts.addAttribute("targetNS", null, value);        
        
        _handler.startElement(ELEMENT_NAME, _atts);
        
        //-- process annotations
        processAnnotated(schema);
        
        Enumeration enum = null;
        //-- process all top level element declarations
        enum = schema.getElementDecls();
        while (enum.hasMoreElements()) {
            processElement((ElementDecl) enum.nextElement());
        }
        //-- process all top level complex types
        enum = schema.getComplexTypes();
        while (enum.hasMoreElements()) {
            processComplexType((ComplexType) enum.nextElement());
        }
        //-- process all top level simple types
        enum = schema.getSimpleTypes();
        while (enum.hasMoreElements()) {
            processSimpleType((SimpleType) enum.nextElement());
        }
        
        _handler.endElement(ELEMENT_NAME);        
        
        _handler.endDocument();
        
    } //-- processSchema
    
    /**
     * Processes the given simple type definition
     *
     * @param simpleType the simple type definition to process into events
    **/
    private void processSimpleType(SimpleType simpleType)
        throws SAXException
    {
        
        if (simpleType.isBuiltInType()) return;
        
        String ELEMENT_NAME = "xsd:simpleType";
        
        _atts.clear();
        
        String name = simpleType.getName();
        
        //-- top-level simple type
        if (name != null) {
            _atts.addAttribute("name", null, name);
        }
        
        _handler.startElement(ELEMENT_NAME, _atts);
        
        //-- handle restriction
        
        SimpleType base = (SimpleType)simpleType.getBaseType();
        if (base != null) {
            
            _atts.clear();
            _atts.addAttribute("base", null, base.getName());
            
            _handler.startElement("xsd:restriction", _atts);
            
            //-- process facets
            Enumeration enum = simpleType.getFacets();
            while (enum.hasMoreElements()) {
                Facet facet = (Facet) enum.nextElement();
                _atts.clear();
                _atts.addAttribute("value", null, facet.getValue());
                String facetName = "xsd:" + facet.getName();
                _handler.startElement(facetName, _atts);
                _handler.endElement(facetName);
            }
            
            _handler.endElement("xsd:restriction");
        }
        
        _handler.endElement(ELEMENT_NAME);
        
    } //-- processSimpleType
    
    
} //-- SchemaWriter
