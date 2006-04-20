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
import java.util.Hashtable;

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

    //------------------------/
    //- Schema element names -/
    //------------------------/
    
    /**
     * Annotation element name.
     */
    private static final String ANNOTATION    =  "annotation";
    
    /**
     * Attribute element name.
     */
    private static final String ATTRIBUTE     =  "attribute";
    
    /**
     * ComplexType element name.
     */
    private static final String COMPLEX_TYPE  =  "complexType";
    
    /**
     * Documentation element name.
     */
    private static final String DOCUMENTATION =  "documentation";
    
    /**
     * Element element name.
     */
    private static final String ELEMENT       =  "element";
    
    /**
     * ModelGroup element name.
     */
    private static final String GROUP         =  "group";
    
    /**
     * Restriction element name.
     */
    private static final String RESTRICTION   =  "restriction";
    
    /**
     * Schema element name.
     */
    private static final String SCHEMA        =  "schema";
    
    /**
     * SimpleType element name
     */
    private static final String SIMPLE_TYPE   =  "simpleType";
    
    //-------------------/
    //- Attribute names -/
    //-------------------/
    
    private static final String ATTR_NAME   = "name";
    private static final String ATTR_TYPE   = "type";
    private static final String ATTR_VALUE  = "value";
    
    private static final String VALUE_FALSE = "false";
    private static final String VALUE_TRUE  = "true";
    
    
    /** 
     * For use with SAX AttributeList
     */
    private static final String CDATA          =  "CDATA";
    private static final String XMLNS_PREFIX   =  "xmlns:";
    private static final String XMLNS_DEFAULT  =  "xmlns";
    private static final String DEFAULT_PREFIX =  "xsd";
    
    /**
     * The DocumentHandler to send events to
     */
    private DocumentHandler   _handler = null;

    /**
     * The AttributeList to send events to
     */
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
    private void processAnnotated(Annotated annotated, String schemaPrefix)
        throws SAXException
    {
        Enumeration enum = annotated.getAnnotations();
        while (enum.hasMoreElements())
            processAnnotation( (Annotation) enum.nextElement(), schemaPrefix );

    } //-- processAnnotated

    /**
     * Processes the given annotation into events
     *
     * @param annotation the annotation to process into events
     * @param schemaPrefix the namespace prefix to use for schema elements
    **/
    private void processAnnotation(Annotation annotation, String schemaPrefix)
        throws SAXException
    {

        _atts.clear();

        String ELEM_ANNOTATION = schemaPrefix + ANNOTATION;

        _handler.startElement(ELEM_ANNOTATION, _atts);

        //-- process documentation elements
        Enumeration enum = annotation.getDocumentation();
        String ELEM_DOCUMENTATION = schemaPrefix + DOCUMENTATION;
        while (enum.hasMoreElements()) {
            Documentation doc = (Documentation) enum.nextElement();
            String content = doc.getContent();
            if ((content != null) && (content.length() > 0)) {
                char[] chars = content.toCharArray();
                _handler.startElement(ELEM_DOCUMENTATION, _atts);
                _handler.characters(chars, 0, chars.length);
                _handler.endElement(ELEM_DOCUMENTATION);
            }
        }

        _handler.endElement(ELEM_ANNOTATION);

    } //-- processAnnotations

    /**
     * Processes the given attribute declaration
     *
     * @param attribute the attribute declaration to process into events
     * @param schemaPrefix the namespace prefix to use for schema elements
    **/
    private void processAttribute(AttributeDecl attribute, String schemaPrefix)
        throws SAXException
    {
        String ELEM_ATTRIBUTE = schemaPrefix + ATTRIBUTE;

        _atts.clear();

        //-- name
        String value = attribute.getName();

        _atts.addAttribute("name", null, value);

        //-- type attribute
        boolean hasAnonymousType = false;
        SimpleType type = attribute.getSimpleType();
        if (type.getName() != null) {
            
            String typeName = type.getName();
            
            //-- add "xsd" prefix if necessary
            if ((typeName.indexOf(':') < 0) && type.isBuiltInType()) {
                typeName = schemaPrefix + typeName;
            }
            _atts.addAttribute(ATTR_TYPE, CDATA, typeName);
        }
        else hasAnonymousType = true;

        //-- required?
        if (attribute.isRequired()) {
            _atts.addAttribute("use", CDATA, "required");
        }
        
        //-- default value
        if (attribute.isDefault()) {
            _atts.addAttribute("default", CDATA, attribute.getValue());
        }
        
        //-- fixed value
        if (attribute.isFixed()) {
            _atts.addAttribute("fixed", CDATA, attribute.getValue());
        }

        _handler.startElement(ELEM_ATTRIBUTE, _atts);

        //-- process annotations
        processAnnotated(attribute, schemaPrefix);

        //-- process anonymous type if necessary
        if (hasAnonymousType) {
            processSimpleType(type, schemaPrefix);
        }

        _handler.endElement(ELEM_ATTRIBUTE);


    } //-- processAttribute

    /**
     * Processes the given complex type definition
     *
     * @param complexType the complex type definition to process into events
     * @param schemaPrefix the namespace prefix to use for schema elements
    **/
    private void processComplexType
        (ComplexType complexType, String schemaPrefix)
        throws SAXException
    {
        String ELEMENT_NAME = schemaPrefix + COMPLEX_TYPE;

        _atts.clear();

        if (complexType.isTopLevel()) {
            _atts.addAttribute("name", null, complexType.getName());
        }

        _handler.startElement(ELEMENT_NAME, _atts);

        //-- process annotations
        processAnnotated(complexType, schemaPrefix);

        //-- process group
        processContentModelGroup(complexType, schemaPrefix);

        //-- process Attributes, must appear last in a complex type
        Enumeration enum = complexType.getAttributeDecls();
        while (enum.hasMoreElements()) {
            processAttribute((AttributeDecl)enum.nextElement(), schemaPrefix);
        }

        _handler.endElement(ELEMENT_NAME);

    } //-- processComplexType

    /**
     * Processes the given ContentModelGroup
     *
     * @param contentModel the content model group to process into events
     * @param schemaPrefix the namespace prefix to use for schema elements
    **/
    private void processContentModelGroup
        (ContentModelGroup contentModel, String schemaPrefix)
        throws SAXException
    {
        Enumeration enum = contentModel.enumerate();
        while (enum.hasMoreElements()) {
            Structure structure = (Structure) enum.nextElement();
            switch (structure.getStructureType()) {
                case Structure.ELEMENT:
                    processElement((ElementDecl)structure, schemaPrefix);
                    break;
                case Structure.MODELGROUP:
                case Structure.GROUP:
                    processGroup((Group)structure, schemaPrefix);
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
    private void processElement(ElementDecl element, String schemaPrefix)
        throws SAXException
    {

        String ELEMENT_NAME = schemaPrefix + ELEMENT;
        
        _atts.clear();


        //-- name or reference
        String value = element.getName();
        if (value != null) {
            if (element.isReference())
                _atts.addAttribute(SchemaNames.REF_ATTR, CDATA, value);
            else
                _atts.addAttribute(ATTR_NAME, CDATA, value);
        }

        //-- minOccurs/maxOccurs
        int max = element.getMaxOccurs();
        int min = element.getMinOccurs();

        if (min != 1) {
            _atts.addAttribute(SchemaNames.MIN_OCCURS_ATTR, CDATA, 
                Integer.toString(min));
        }

        if (max < 0) {
            _atts.addAttribute(SchemaNames.MAX_OCCURS_ATTR, CDATA, 
                "unbounded");
        }
        else if (max > 1) {
            _atts.addAttribute(SchemaNames.MAX_OCCURS_ATTR, CDATA, 
                Integer.toString(max));
        }

        //-- type attribute
        boolean hasAnonymousType = false;
        if (!element.isReference()) {
            XMLType type = element.getType();
             
            //-- no type?
            if (type == null) {
                //-- do nothing
            }
            //-- anonymous (in-lined) type
            else if (type.getName() == null) {
                hasAnonymousType = true;
            } 
            //-- built-in simpleType
            else if (type instanceof SimpleType && ((SimpleType)type).isBuiltInType()){
                _atts.addAttribute(ATTR_TYPE, CDATA, 
                    schemaPrefix+type.getName());
            } 
            //-- type imported from another schema
            else if (isImportedType(type, element)) {
                String namespace = type.getSchema().getTargetNamespace();
                String prefix = getNSPrefix(element.getSchema(), namespace);
                if (prefix == null) {
                    //-- declare a temporary prefix
                    prefix = schemaPrefix + '2';
                    _atts.addAttribute("xmlns:" + prefix, CDATA, namespace);
                } 
                _atts.addAttribute(ATTR_TYPE, CDATA, 
                    prefix + ':' +type.getName());
            //-- otherwise...user defined type.
            } 
            else {
                _atts.addAttribute(ATTR_TYPE, CDATA, type.getName());
            }
        }

        //-- @abstract
        if (element.isAbstract()) {
            _atts.addAttribute(SchemaNames.ABSTRACT, CDATA, VALUE_TRUE);
        }
        
        //-- @block
        if (element.getBlock() != null) {
            _atts.addAttribute(SchemaNames.BLOCK_ATTR, CDATA,
                element.getBlock().toString());
        }
        
        //-- @default
        if (element.getDefaultValue() != null) {
            _atts.addAttribute(SchemaNames.DEFAULT_ATTR, CDATA,
                element.getDefaultValue());
        }
        
        //-- @fixed
        if (element.getFixedValue() != null) {
            _atts.addAttribute(SchemaNames.FIXED_ATTR, CDATA,
                element.getFixedValue());
        }
        
        //-- @final
        if (element.getFinal() != null) {
            _atts.addAttribute(SchemaNames.FINAL_ATTR, CDATA,
                element.getFinal().toString());
        }
        
        //-- @form
        Form form = element.getForm();
        if (form != null) {
            _atts.addAttribute(SchemaNames.FORM, CDATA, form.toString());
        }

        //-- @id
        if (element.getId() != null) {
            _atts.addAttribute(SchemaNames.ID_ATTR, CDATA, 
                element.getId());
        }
        
        //-- @nillable
        if (element.isNillable()) {
            _atts.addAttribute(SchemaNames.NILLABLE_ATTR, CDATA,
                VALUE_TRUE);
        }
        
        //-- @substitutionGroup
        if (element.getSubstitutionGroup() != null) {
            _atts.addAttribute(SchemaNames.SUBSTITUTION_GROUP_ATTR, CDATA,
                element.getSubstitutionGroup());
        }
        
        _handler.startElement(ELEMENT_NAME, _atts);

        //-- process annotations
        processAnnotated(element, schemaPrefix);

        //-- process anonymous type if necessary
        if (hasAnonymousType) {
            XMLType type = element.getType();
            if (type.isComplexType())
                processComplexType((ComplexType) type, schemaPrefix);
        }
        
        //-- process any identity-constraints
        Enumeration enum = element.getIdentityConstraints();
        while(enum.hasMoreElements()) {
            processIdentityConstraint((IdentityConstraint)enum.nextElement(),
                schemaPrefix);
        }

        _handler.endElement(ELEMENT_NAME);

    } //-- processElement

    /**
     * Processes the given group definition into SAX events
     *
     * @param group the group definition to process into SAX events
     * @param schemaPrefix the namespace prefix to use for schema elements
    **/
    private void processGroup(Group group, String schemaPrefix)
        throws SAXException
    {
        String ELEMENT_NAME = schemaPrefix;
        
        //-- ModelGroup
        String reference = null;
        if (group instanceof ModelGroup) {
            ELEMENT_NAME += GROUP;
            ModelGroup mGroup = (ModelGroup)group;
            if (mGroup.hasReference()) {
                ModelGroup refGroup = mGroup.getReference();
                if (refGroup != null) {
                    reference = refGroup.getName();
                }
            }
        }
        //-- Group
        else {
            ELEMENT_NAME += group.getOrder().toString();
        }

        _atts.clear();

        String groupName = group.getName();
        if (groupName != null) {
            _atts.addAttribute(ATTR_NAME, CDATA, groupName);
        }
        //-- reference
        else if (reference != null) {
            _atts.addAttribute("ref", CDATA, reference);
        }
        
        _handler.startElement(ELEMENT_NAME, _atts);

        //-- process annotations
        processAnnotated(group, schemaPrefix);

        //-- process content model
        processContentModelGroup(group, schemaPrefix);

        _handler.endElement(ELEMENT_NAME);

    } //-- processGroup

    /**
     * Processes the given IdentityConstraint
     *
     * @param constraint the IdentityConstraint to process
    **/
    private void processIdentityConstraint
        (IdentityConstraint constraint, String schemaPrefix) 
        throws SAXException
    {
        
        if (constraint == null) return;
        
        String ELEMENT_NAME = schemaPrefix;
        
        String name  = null;
        String id    = null;
        String refer = null;
        
        switch (constraint.getStructureType()) {
            case Structure.KEYREF:
                ELEMENT_NAME += SchemaNames.KEYREF;
                refer = ((KeyRef)constraint).getRefer();
                break;
            case Structure.UNIQUE:
                ELEMENT_NAME += SchemaNames.UNIQUE;
                break;
            default:
                ELEMENT_NAME += SchemaNames.KEY;
                break;
        }
        
        name = constraint.getName();
        id   = constraint.getId();
        
        _atts.clear();
        
        //-- name
        _atts.addAttribute(SchemaNames.NAME_ATTR, CDATA, 
            constraint.getName());
            
        //-- id
        if (id != null) {
            _atts.addAttribute(SchemaNames.ID_ATTR, CDATA, id);
        }
        
        //-- refer
        if (refer != null) {
            _atts.addAttribute(SchemaNames.REFER_ATTR, CDATA, refer);
        }
        
        _handler.startElement(ELEMENT_NAME, _atts);
        
        //-- process annotations
        processAnnotated(constraint, schemaPrefix);
        
        //-- process selector
        String ELEM_SELECTOR = schemaPrefix + SchemaNames.SELECTOR;
        String xpath = null;
        
        IdentitySelector selector = constraint.getSelector();
        xpath = selector.getXPath();
        id = selector.getId();
        _atts.clear();
        _atts.addAttribute(SchemaNames.XPATH_ATTR, CDATA, xpath);
        if (id != null) {
            _atts.addAttribute(SchemaNames.ID_ATTR, CDATA, id);
        }
        _handler.startElement(ELEM_SELECTOR, _atts);
        processAnnotated(selector, schemaPrefix);
        _handler.endElement(ELEM_SELECTOR);
        
        //-- process field(s)
        String ELEM_FIELD = schemaPrefix + SchemaNames.FIELD;
        Enumeration enum = constraint.getFields();
        while(enum.hasMoreElements()) {
            IdentityField field = (IdentityField) enum.nextElement();
            _atts.clear();
            id    = field.getId();
            xpath = field.getXPath();
            _atts.addAttribute(SchemaNames.XPATH_ATTR, CDATA, xpath);
            if (id != null) {
                _atts.addAttribute(SchemaNames.ID_ATTR, CDATA, id);
            }
            _handler.startElement(ELEM_FIELD, _atts);
            processAnnotated(field, schemaPrefix);
            _handler.endElement(ELEM_FIELD);
        }
        _handler.endElement(ELEMENT_NAME);
        
    } //-- processIdentityConstraint
    
    private void processSchema(Schema schema)
        throws SAXException
    {
        
        //-- calculate schema prefix
        String schemaPrefix = getNSPrefix(schema, schema.getSchemaNamespace());
        if (schemaPrefix == null) {
            schemaPrefix = DEFAULT_PREFIX;
        }
        
        //-- namespace declaration for xsd
        _atts.clear();
        if (schemaPrefix.length() == 0) {
            //-- declared as default namespace
            _atts.addAttribute(XMLNS_DEFAULT, CDATA, 
                schema.getSchemaNamespace());
        }
        else {
            //-- declare namespace + prefix
            _atts.addAttribute(XMLNS_PREFIX + schemaPrefix, CDATA, 
                schema.getSchemaNamespace());
        }
        


        //-- namespace declarations
        Enumeration keys = schema.getNamespaces().keys();
        while (keys.hasMoreElements()) {
            String nsPrefix = (String)keys.nextElement();
            if (!nsPrefix.equals(schemaPrefix)) {
                String ns = (String)schema.getNamespaces().get(nsPrefix);
                if (nsPrefix.length() > 0) {
                    _atts.addAttribute(XMLNS_PREFIX + nsPrefix, CDATA, ns);
                }
                else {
                    _atts.addAttribute(XMLNS_DEFAULT, CDATA, ns);
                }
            }
        }

        //-- targetNS
        String value = schema.getTargetNamespace();
        if (value != null)
            _atts.addAttribute(SchemaNames.TARGET_NS_ATTR, CDATA, value);

        //-- attributeFormDefault
        Form form = schema.getAttributeFormDefault();
        if (form != null) {
            _atts.addAttribute(SchemaNames.ATTR_FORM_DEFAULT_ATTR, CDATA,
                form.toString());
        }
        //-- elementFormDefault
        form = schema.getElementFormDefault();
        if (form != null) {
            _atts.addAttribute(SchemaNames.ELEM_FORM_DEFAULT_ATTR, CDATA,
                form.toString());
        }

        //-- blockDefault
        BlockList blockList = schema.getBlockDefault();
        if (blockList != null) {
            _atts.addAttribute(SchemaNames.BLOCK_DEFAULT_ATTR, CDATA,
                blockList.toString());
        }

        //-- finalDefault
        FinalList finalList = schema.getFinalDefault();
        if (finalList != null) {
            _atts.addAttribute(SchemaNames.FINAL_DEFAULT_ATTR, CDATA,
                finalList.toString());
        }


        //-- modify schemaPrefix to include ':'
        if (schemaPrefix.length() > 0) {
            schemaPrefix += ':';
        }
        
        _handler.startDocument();
        
        String ELEM_SCHEMA = schemaPrefix + SCHEMA;
        
        _handler.startElement(ELEM_SCHEMA, _atts);

        //-- process annotations
        processAnnotated(schema, schemaPrefix);

        Enumeration enum = null;
         //-- process all imported schemas
        enum = schema.getImportedSchema();
        while (enum.hasMoreElements()) {
            processImport((Schema)enum.nextElement(), schemaPrefix);
        }

        //-- process all top level element declarations
        enum = schema.getElementDecls();
        while (enum.hasMoreElements()) {
            processElement((ElementDecl) enum.nextElement(), 
                schemaPrefix);
        }
        
        //-- process all top level complex types
        enum = schema.getComplexTypes();
        while (enum.hasMoreElements()) {
            processComplexType((ComplexType) enum.nextElement(), 
                schemaPrefix);
        }
        
        //-- process all top level groups
        enum = schema.getModelGroups();
        while (enum.hasMoreElements()) {
            processGroup((Group)enum.nextElement(), schemaPrefix);
        }
        
        //-- process all top level simple types
        enum = schema.getSimpleTypes();
        while (enum.hasMoreElements()) {
            processSimpleType((SimpleType) enum.nextElement(),
                schemaPrefix);
        }

        _handler.endElement(ELEM_SCHEMA);

        _handler.endDocument();

    } //-- processSchema

    /**
     * Process an imported schema
     *
     * @param schema the imported Schema to process
     * @param schemaPrefix the namespace prefix to use for schema elements
    **/
    private void processImport(Schema schema, String schemaPrefix)
        throws SAXException
    {
        String ELEMENT_NAME = schemaPrefix + SchemaNames.IMPORT;
        _atts.clear();

        String namespace = schema.getTargetNamespace();
        String schemaLoc = schema.getSchemaLocation();
        
        _atts.addAttribute("namespace", null, namespace);
        _atts.addAttribute("schemaLocation", null, schemaLoc);
        _handler.startElement(ELEMENT_NAME, _atts);
        _handler.endElement(ELEMENT_NAME);
    } //-- processImport
    
    /**
     * Processes the given simple type definition
     *
     * @param simpleType the simple type definition to process into events
     * @param schemaPrefix the namespace prefix to use for schema elements
    **/
    private void processSimpleType
        (SimpleType simpleType, String schemaPrefix)
        throws SAXException
    {

        if (simpleType.isBuiltInType()) return;

        String ELEMENT_NAME = schemaPrefix + SIMPLE_TYPE;

        _atts.clear();

        String name = simpleType.getName();

        //-- top-level simple type
        if (name != null) {
            _atts.addAttribute(SchemaNames.NAME_ATTR, null, name);
        }

        _handler.startElement(ELEMENT_NAME, _atts);

        //-- handle restriction

        SimpleType base = (SimpleType)simpleType.getBaseType();
        if (base != null) {

            String ELEM_RESTRICTION = schemaPrefix + RESTRICTION;
            
            _atts.clear();
            _atts.addAttribute("base", null, base.getName());

            _handler.startElement(ELEM_RESTRICTION, _atts);

            //-- process facets
            Enumeration enum = simpleType.getFacets();
            while (enum.hasMoreElements()) {
                Facet facet = (Facet) enum.nextElement();
                _atts.clear();
                _atts.addAttribute("value", null, facet.getValue());
                String facetName = schemaPrefix + facet.getName();
                _handler.startElement(facetName, _atts);
                _handler.endElement(facetName);
            }

            _handler.endElement(ELEM_RESTRICTION);
        }

        _handler.endElement(ELEMENT_NAME);

    } //-- processSimpleType

    /**
     * Determines if a given XMLType is imported by the
     * schema containing the element that refers to it.
     *
     * @param type the type to be checked to see if it is imported
     * @param element the schema element that references the type
    **/
    private boolean isImportedType(XMLType type, ElementDecl element) {
        String targetNS = type.getSchema().getTargetNamespace();
        return (element.getSchema().getImportedSchema(targetNS) != null);
    } //-- isImportedType

    /**
     * Determines the proper namespace prefix for a namespace
     * by scanning all declared namespaces for the schema.
     *
     * @param schema the schema in which the namespace is declared
     * @param namespace the namespace for which a prefix will be returned
    **/
    private String getNSPrefix(Schema schema, String namespace){
        Hashtable namespaces = schema.getNamespaces();
        for (Enumeration e = namespaces.keys(); e.hasMoreElements();) {
            String prefix = (String)e.nextElement();
            String ns = (String)namespaces.get(prefix);
            if (ns != null && ns.equals(namespace)){
                return (prefix);
            }
        }
        return null;
    } //-- getNSPrefix


} //-- SchemaWriter
