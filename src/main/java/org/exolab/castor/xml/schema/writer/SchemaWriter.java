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

package org.exolab.castor.xml.schema.writer;

import java.io.Writer;
import java.io.IOException;
import java.util.Enumeration;
import org.exolab.castor.types.AnyNode;
import org.exolab.castor.util.LocalConfiguration;
import org.exolab.castor.xml.schema.*;
import org.exolab.castor.xml.schema.simpletypes.ListType;
import org.exolab.castor.xml.util.AnyNode2SAX;
import org.exolab.castor.xml.Namespaces;
import org.exolab.castor.xml.Serializer;

import org.xml.sax.DocumentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributeListImpl;

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
     * AppInfo element name
     */
    private static final String APPINFO       = "appinfo";
    
    /**
     * Attribute element name.
     */
    private static final String ATTRIBUTE     =  "attribute";

    /**
     * AttributeGroup element name.
     */
    private static final String ATTRIBUTE_GROUP = "attributeGroup";

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

    /**
     * This field is no longer used and only here for
     * backward compatibility.
     * @deprecated
    **/
    public static boolean enable = false;

    /**
     * Creates a new SchemaWriter for the given Writer
     *
     * @param writer the Writer to serialize to
    **/
    public SchemaWriter(Writer writer)
        throws IOException
    {

        Serializer serializer = LocalConfiguration.getInstance().getSerializer();

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

        if (handler == null)
            throw new IllegalArgumentException("DocumentHandler must not be null.");

        _handler = handler;

    } //-- SchemaWriter


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
        Enumeration enumeration = annotated.getAnnotations();
        while (enumeration.hasMoreElements())
            processAnnotation( (Annotation) enumeration.nextElement(), schemaPrefix );

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

        
        //--process appinfo elements
        Enumeration enumeration = annotation.getAppInfo();
        String ELEM_APPINFO = schemaPrefix + APPINFO;
        while (enumeration.hasMoreElements()) {
            AppInfo app = (AppInfo) enumeration.nextElement();
            
            String source = app.getSource();
            String sourceName = _atts.getName(0);
            boolean isSourceIsNull = (sourceName == null);
            boolean isSourceExists = false;
            
            if (!isSourceIsNull)
            {
                isSourceExists = sourceName.equals(SchemaNames.SOURCE_ATTR);
            }
            if (source != null && !isSourceExists)
                _atts.addAttribute(SchemaNames.SOURCE_ATTR, CDATA,source);

            _handler.startElement(ELEM_APPINFO, _atts);
            Enumeration anyNodes = app.getObjects();
            while (anyNodes.hasMoreElements()) {
                Object obj = anyNodes.nextElement();
                if (obj instanceof AnyNode) {
                    AnyNode2SAX anyNode2SAX = new AnyNode2SAX((AnyNode)obj);
                    anyNode2SAX.setDocumentHandler(_handler);
                    anyNode2SAX.start();
                }
                else {
                    char[] chars = obj.toString().toCharArray();
                    _handler.characters(chars, 0, chars.length);
                    
                }
            }
            _handler.endElement(ELEM_APPINFO);
        }
        
        //-- process documentation elements
        enumeration = annotation.getDocumentation();
        String ELEM_DOCUMENTATION = schemaPrefix + DOCUMENTATION;
        while (enumeration.hasMoreElements()) {
            Documentation doc = (Documentation) enumeration.nextElement();
            String source = doc.getSource();
            String sourceName = _atts.getName(0);
            boolean isSourceIsNull = (sourceName == null);
            boolean isSourceExists = false;
            
            if (!isSourceIsNull)
            {
                isSourceExists = sourceName.equals(SchemaNames.SOURCE_ATTR);
            }
            if (source != null && !isSourceExists)
            {
                _atts.addAttribute(SchemaNames.SOURCE_ATTR, CDATA,source);
            }

            _handler.startElement(ELEM_DOCUMENTATION, _atts);
            Enumeration anyNodes = doc.getObjects();
            while (anyNodes.hasMoreElements()) {
                Object obj = anyNodes.nextElement();
                if (obj instanceof AnyNode) {
                    AnyNode2SAX anyNode2SAX = new AnyNode2SAX((AnyNode)obj);
                    anyNode2SAX.setDocumentHandler(_handler);
                    anyNode2SAX.start();
                }
                else {
                    char[] chars = obj.toString().toCharArray();
                    _handler.characters(chars, 0, chars.length);
                    
                }
            }
            _handler.endElement(ELEM_DOCUMENTATION);
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

        boolean isReference = attribute.isReference();

        //-- name
        if (!isReference) {
            _atts.addAttribute(SchemaNames.NAME_ATTR, CDATA,
                attribute.getName());
        }
        else {
            _atts.addAttribute(SchemaNames.REF_ATTR, CDATA,
                attribute.getReferenceName());
        }


        //-- type attribute
        boolean hasAnonymousType = false;
        SimpleType type = attribute.getSimpleType();
        if ((!isReference) && (type != null)) {

            if (type.getName() != null) {

                String typeName = type.getName();

                //-- add prefix if necessary
                if (typeName.indexOf(':') < 0) {
                    if (type.isBuiltInType()) {
                        typeName = schemaPrefix + typeName;  // xsd prefix
                    }
                    else {
                        // resolve prefix
                        String namespace = type.getSchema().getTargetNamespace();
                        if (namespace == null) namespace = "";
                        String prefix = getNSPrefix(attribute.getSchema(), namespace);
                        if ((prefix != null) && (prefix.length() > 0))
                            typeName = prefix + ":" + typeName;
                    }
                }
                _atts.addAttribute(ATTR_TYPE, CDATA, typeName);
            }
            else hasAnonymousType = true;
        }

        // default or fixed values?
        //-- @default
        if (attribute.isDefault()) {
            _atts.addAttribute(SchemaNames.DEFAULT_ATTR, CDATA,
                attribute.getDefaultValue());
        }
        //-- @fixed
        else if (attribute.isFixed()) {
            _atts.addAttribute(SchemaNames.FIXED_ATTR, CDATA,
                attribute.getFixedValue());
        }

        //-- @form
        if (attribute.getForm() != null) {
            _atts.addAttribute(SchemaNames.FORM, CDATA,
                attribute.getForm().toString());
        }

        //-- @id (optional)
        if (attribute.getId() != null) {
            _atts.addAttribute(SchemaNames.ID_ATTR, CDATA,
                attribute.getId());
        }

        //-- use : required
        if (attribute.isRequired()) {
            _atts.addAttribute(SchemaNames.USE_ATTR, CDATA,
                AttributeDecl.USE_REQUIRED);
        }
        //-- use : prohibited
        else if (attribute.isProhibited()) {
            _atts.addAttribute(SchemaNames.USE_ATTR, CDATA,
                AttributeDecl.USE_PROHIBITED);
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
     * Processes the given attributeGroup declaration
     *
     * @param attGroup the attributeGroup declaration to process into events
     * @param schemaPrefix the namespace prefix to use for schema elements
    **/
    private void processAttributeGroup
        (AttributeGroup attGroup, String schemaPrefix)
        throws SAXException
    {
        String ELEM_ATTRIBUTE_GROUP = schemaPrefix + ATTRIBUTE_GROUP;

        _atts.clear();

        boolean isReference = (attGroup instanceof AttributeGroupReference);

        //-- name
        if (!isReference) {
            _atts.addAttribute(SchemaNames.NAME_ATTR, CDATA,
                ((AttributeGroupDecl)attGroup).getName());
        }
        else {
            _atts.addAttribute(SchemaNames.REF_ATTR, CDATA,
                ((AttributeGroupReference)attGroup).getReference());
        }

        //-- @id (optional)
        if (attGroup.getId() != null) {
            _atts.addAttribute(SchemaNames.ID_ATTR, CDATA,
                attGroup.getId());
        }

        _handler.startElement(ELEM_ATTRIBUTE_GROUP, _atts);

        //-- process annotations
        processAnnotated(attGroup, schemaPrefix);

        if (!isReference) {
            AttributeGroupDecl group = (AttributeGroupDecl)attGroup;
            Enumeration enumeration = group.getLocalAttributes();
            while (enumeration.hasMoreElements()) {
                processAttribute((AttributeDecl)enumeration.nextElement(),
                    schemaPrefix);
            }
            enumeration = group.getLocalAttributeGroupReferences();
            while (enumeration.hasMoreElements()) {
                processAttributeGroup((AttributeGroup)enumeration.nextElement(),
                    schemaPrefix);
            }
            
            if (group.getAnyAttribute() != null) {
                processWildcard(group.getAnyAttribute(), schemaPrefix);
            }
        }

        _handler.endElement(ELEM_ATTRIBUTE_GROUP);


    } //-- processAttributeGroup

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

        //-- handle top-level only attributes
        if (complexType.isTopLevel()) {

            //-- @name
            _atts.addAttribute(SchemaNames.NAME_ATTR, CDATA,
                complexType.getName());

            //-- @abstract
            if (complexType.isAbstract()) {
                _atts.addAttribute(SchemaNames.ABSTRACT, CDATA, VALUE_TRUE);
            }

            //-- @block
            if (complexType.getBlock() != null) {
                _atts.addAttribute(SchemaNames.BLOCK_ATTR, CDATA,
                    complexType.getBlock().toString());
            }

            //-- @final
            if (complexType.getFinal() != null) {
                _atts.addAttribute(SchemaNames.FINAL_ATTR, CDATA,
                    complexType.getFinal().toString());
            }
        } //-- isTopLevel

        //-- @id
        if (complexType.getId() != null) {
            _atts.addAttribute(SchemaNames.ID_ATTR, CDATA,
                complexType.getId());
        }

        //-- @mixed
        if (complexType.getContentType() == ContentType.mixed) {
            _atts.addAttribute(SchemaNames.MIXED, CDATA, VALUE_TRUE);
        }

        _handler.startElement(ELEMENT_NAME, _atts);

        //-- process annotations
        processAnnotated(complexType, schemaPrefix);


        //-- handle simpleContent/complexContent if we have a baseType.
        String ELEM_CONTENT    = null;
        String ELEM_DERIVATION = null;
        XMLType baseType = complexType.getBaseType();
        if (baseType != null) {
            if (complexType.isSimpleContent())
                ELEM_CONTENT = schemaPrefix + SchemaNames.SIMPLE_CONTENT;
            else
                ELEM_CONTENT = schemaPrefix + SchemaNames.COMPLEX_CONTENT;

            _atts.clear();
            if (complexType.isComplexContent()) {
                if (complexType.getContentType() == ContentType.mixed) {
                    _atts.addAttribute(SchemaNames.MIXED, CDATA,
                        VALUE_TRUE);
                }

            }
            _handler.startElement(ELEM_CONTENT, _atts);

            ELEM_DERIVATION = schemaPrefix +
                complexType.getDerivationMethod();

            String baseTypeName = baseType.getName();

            //-- add "xsd" prefix if necessary
            if (complexType.isSimpleContent()) {
                //the base type can be a complexType in extension
                if (baseType.isSimpleType()) {
                    SimpleType simpleType = (SimpleType)baseType;
                    if (baseTypeName.indexOf(':') < 0) {
                        if (simpleType.isBuiltInType()) {
                            baseTypeName = schemaPrefix + baseTypeName;
                        }
                        else {
                            String targetNamespace = simpleType.getSchema().getTargetNamespace();
                            String prefix = getNSPrefix(complexType.getSchema(), targetNamespace);
                            if ((prefix != null) && (prefix.length() > 0)) {
                                baseTypeName = prefix + ":" + baseTypeName;
                            }
                        }
                    }
                }
            }
            else if (complexType.isComplexContent()) {
                //--complexType: add 'xsd' only for anyType
                if (baseType.isAnyType()) {
                    if (baseTypeName.indexOf(':') <0 ) {
                       baseTypeName = schemaPrefix + baseTypeName;
                    }

                }
            } //--end of 'xsd' appending
            //add the targetNamespace prefix if necessary
            if (baseType.isComplexType()) {
                String targetNamespace = baseType.getSchema().getTargetNamespace();
                //-- targetNamespace is null
                if (targetNamespace == null) {
                	if (complexType.isRedefined()) {
                		targetNamespace =  complexType.getSchema().getTargetNamespace();
                	}
                }
                
                else {
                	String nsPrefix = getNSPrefix(complexType.getSchema(), targetNamespace);
	                if ((nsPrefix != null) && (nsPrefix.length() != 0))
	                    baseTypeName = nsPrefix +':'+ baseTypeName;
	                targetNamespace = null;
	                nsPrefix = null;
                }

            }
            _atts.clear();
            _atts.addAttribute(SchemaNames.BASE_ATTR, CDATA, baseTypeName);
            _handler.startElement(ELEM_DERIVATION, _atts);
            //--Any Facets to process?
            //--only relevant for the simpleContent with restriction
            if (complexType.isSimpleContent() && complexType.isRestricted()) {
                if (complexType.getContentType().getType() == ContentType.SIMPLE) {
                    SimpleContent simpleContent = (SimpleContent)complexType.getContentType();
                    SimpleType simpleType = simpleContent.getSimpleType();
                    //-- process facets
                    Enumeration enumeration = simpleType.getLocalFacets();
                    while (enumeration.hasMoreElements()) {
                        Facet facet = (Facet) enumeration.nextElement();
                        _atts.clear();
                        _atts.addAttribute(SchemaNames.VALUE_ATTR, CDATA,
                                           facet.getValue());
                        String facetName = schemaPrefix + facet.getName();
                        _handler.startElement(facetName, _atts);
                        Enumeration annotations = facet.getAnnotations();
                        while (annotations.hasMoreElements()) {
                            Annotation annotation = (Annotation)annotations.nextElement();
                            processAnnotation(annotation, schemaPrefix);
                        }
                       _handler.endElement(facetName);
                    } //--facets
                    enumeration = null;
                    simpleType = null;
                }
            }//--<simpleContent><restriction>

        }

        //-- process content model group
        processContentModelGroup(complexType, schemaPrefix);

        //-- process Attributes, must appear last in a complex type
        Enumeration enumeration = complexType.getLocalAttributeDecls();
        while (enumeration.hasMoreElements()) {
            processAttribute((AttributeDecl)enumeration.nextElement(), schemaPrefix);
        }
        enumeration = complexType.getAttributeGroupReferences();
        while (enumeration.hasMoreElements()) {
            processAttributeGroup((AttributeGroup)enumeration.nextElement(), schemaPrefix);
        }

        if (baseType != null) {
            _handler.endElement(ELEM_DERIVATION);
            _handler.endElement(ELEM_CONTENT);
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
        Enumeration enumeration = contentModel.enumerate();
        while (enumeration.hasMoreElements()) {
            Structure structure = (Structure) enumeration.nextElement();
            switch (structure.getStructureType()) {
                case Structure.ELEMENT:
                    processElement((ElementDecl)structure, schemaPrefix);
                    break;
                case Structure.MODELGROUP:
                case Structure.GROUP:
                    processGroup((Group)structure, schemaPrefix);
                    break;
                case Structure.WILDCARD:
                    processWildcard((Wildcard)structure, schemaPrefix);
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
            if (element.isReference()) {
                String targetNamespace = element.getReference().getSchema().getTargetNamespace();
                String nsPrefix = getNSPrefix(element.getSchema(), targetNamespace);
                if ((nsPrefix != null) && (nsPrefix.length() != 0))
                    value = nsPrefix +':'+ value;
                targetNamespace = null;
                nsPrefix = null;
                _atts.addAttribute(SchemaNames.REF_ATTR, CDATA, value);

            }
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
        else if (max != 1) {
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
            else if (type.isSimpleType() && ((SimpleType)type).isBuiltInType()){
                _atts.addAttribute(ATTR_TYPE, CDATA,
                    schemaPrefix+type.getName());
            }
            else if (type.getStructureType() == Structure.ANYTYPE) {
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
                String typeName = type.getName();
                //add the targetNamespace prefix if necessary
                String targetNamespace = element.getSchema().getTargetNamespace();
                if ( targetNamespace!=null ) {
                    String nsPrefix = getNSPrefix(element.getSchema(), targetNamespace);
                    if ((nsPrefix != null) && (nsPrefix.length() != 0))
                        typeName = nsPrefix +':'+ typeName;
                    targetNamespace = null;
                    nsPrefix = null;
                }

                _atts.addAttribute(ATTR_TYPE, CDATA, typeName);
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
            else if (type.isSimpleType())
                   processSimpleType((SimpleType)type, schemaPrefix);
        }

        //-- process any identity-constraints
        Enumeration enumeration = element.getIdentityConstraints();
        while(enumeration.hasMoreElements()) {
            processIdentityConstraint((IdentityConstraint)enumeration.nextElement(),
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
                    //-- prefix
                    String namespace = refGroup.getSchema().getTargetNamespace();
                    if (namespace == null) namespace = "";
                    String prefix = getNSPrefix(mGroup.getSchema(), namespace);
                    if ((prefix != null) && (prefix.length() > 0))
                        reference = prefix + ':' + reference;
                    
                }
            }
        }
        //-- Group
        else {
            ELEMENT_NAME += group.getOrder().toString();
        }

        _atts.clear();

        //-- @id
        if (group.getId() != null) {
            _atts.addAttribute(SchemaNames.ID_ATTR, CDATA,
                group.getId());
        }

        //-- reference
        if (reference != null) {
            _atts.addAttribute("ref", CDATA, reference);
        }
        else if (group.getName() != null) {
            _atts.addAttribute(ATTR_NAME, CDATA, group.getName());
        }

        //-- minOccurs/maxOccurs
        int max = group.getMaxOccurs();
        int min = group.getMinOccurs();

        if (min != 1) {
            _atts.addAttribute(SchemaNames.MIN_OCCURS_ATTR, CDATA,
                Integer.toString(min));
        }

        if (max < 0) {
            _atts.addAttribute(SchemaNames.MAX_OCCURS_ATTR, CDATA,
                "unbounded");
        }
        else if (max != 1) {
            _atts.addAttribute(SchemaNames.MAX_OCCURS_ATTR, CDATA,
                Integer.toString(max));
        }


        _handler.startElement(ELEMENT_NAME, _atts);

        //-- process annotations
        processAnnotated(group, schemaPrefix);

        //-- process content model
        if (reference == null) {
            processContentModelGroup(group, schemaPrefix);
        }

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
        Enumeration enumeration = constraint.getFields();
        while(enumeration.hasMoreElements()) {
            IdentityField field = (IdentityField) enumeration.nextElement();
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
        Namespaces namespaces = schema.getNamespaces();
        Enumeration keys = namespaces.getLocalNamespacePrefixes();
        while (keys.hasMoreElements()) {
            String nsPrefix = (String)keys.nextElement();
            if (!nsPrefix.equals(schemaPrefix)) {
                String ns = namespaces.getNamespaceURI(nsPrefix);
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

        //--@version
        if (schema.getVersion() != null) {
            _atts.addAttribute(SchemaNames.VERSION_ATTR, CDATA,schema.getVersion());
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

        Enumeration enumeration = null;
         //-- process all imported schemas
        enumeration = schema.getImportedSchema();
        while (enumeration.hasMoreElements()) {
            processImport((Schema)enumeration.nextElement(), schemaPrefix);
        }
        
        //-- process all cached included schemas
        enumeration = schema.getCachedIncludedSchemas();
        while (enumeration.hasMoreElements()) {
        	processIncludedSchema((Schema)enumeration.nextElement(), schemaPrefix);
        }
        
        //-- process all redefinitions
        enumeration = schema.getRedefineSchema();
        while (enumeration.hasMoreElements()) {
        	processRedefinition((RedefineSchema)enumeration.nextElement(), schema, schemaPrefix);
        }
        
        //-- process all top level attributeGroup declarations
        enumeration = schema.getAttributeGroups();
        while (enumeration.hasMoreElements()) {
        	boolean found = false;
        	AttributeGroup temp = (AttributeGroup) enumeration.nextElement();
        	//-- check if this attributeGroup is not 
            //-- part of a redefinition
           if (temp instanceof AttributeGroupDecl) {
	          if (((AttributeGroupDecl)temp).isRedefined())
	          	found = true;
           }
            
           //--check if this attributeGroup is not 
           //-- included 
           Enumeration includedSchemas = schema.getCachedIncludedSchemas();
           while (includedSchemas.hasMoreElements()) {
               Schema tempSchema = (Schema)includedSchemas.nextElement();
               if (temp instanceof AttributeGroupDecl) {
                   String name = ((AttributeGroupDecl)temp).getName();	
                   found = (tempSchema.getAttributeGroup(name)!= null);
               }
           }
           
           if (!found)
                processAttributeGroup(temp,schemaPrefix);
        }

        //-- process all top level attribute declarations
        enumeration = schema.getAttributes();
        while (enumeration.hasMoreElements()) {
        	AttributeDecl temp = (AttributeDecl) enumeration.nextElement();
        	boolean found = false;
        	//--check if this attributeGroup is not 
        	//-- included 
        	Enumeration includedSchemas = schema.getCachedIncludedSchemas();
        	while (includedSchemas.hasMoreElements()) {
        		Schema tempSchema = (Schema)includedSchemas.nextElement();
        		found = (tempSchema.getAttribute(temp.getName())!= null);
        	}
        	
        	if (!found)
                processAttribute(temp,schemaPrefix);
        }

        //-- process all top level element declarations
        enumeration = schema.getElementDecls();
        while (enumeration.hasMoreElements()) {
        	ElementDecl temp = (ElementDecl) enumeration.nextElement();
        	boolean found = false;
        	//--check if this attributeGroup is not 
        	//-- included 
        	Enumeration includedSchemas = schema.getCachedIncludedSchemas();
        	while (includedSchemas.hasMoreElements()) {
        		Schema tempSchema = (Schema)includedSchemas.nextElement();
        		found = (tempSchema.getElementDecl(temp.getName())!= null);
        	}
        	
        	if (!found)
        	    processElement(temp,schemaPrefix);
        }

        //-- process all top level complex types
        enumeration = schema.getComplexTypes();
        while (enumeration.hasMoreElements()) {
            ComplexType temp = (ComplexType) enumeration.nextElement();
            boolean found = false;
            //--check if this attributeGroup is not 
            //-- included 
            Enumeration includedSchemas = schema.getCachedIncludedSchemas();
            while (includedSchemas.hasMoreElements()) {
            	Schema tempSchema = (Schema)includedSchemas.nextElement();
            	found = (tempSchema.getComplexType(temp.getName())!= null);
            }
            if (!temp.isRedefined() && !found)
                processComplexType(temp, schemaPrefix);
        }

        //-- process all top level groups
        enumeration = schema.getModelGroups();
        while (enumeration.hasMoreElements()) {
        	ModelGroup temp = (ModelGroup)enumeration.nextElement();
        	boolean found = false;
        	//--check if this Group is not 
        	//-- included 
        	Enumeration includedSchemas = schema.getCachedIncludedSchemas();
        	while (includedSchemas.hasMoreElements()) {
        		Schema tempSchema = (Schema)includedSchemas.nextElement();
        		found = (tempSchema.getModelGroup(temp.getName())!= null);
        	}
        	
        	if (!temp.isRedefined() && !found)
        	    processGroup(temp, schemaPrefix);
        }

        //-- process all top level simple types
        enumeration = schema.getSimpleTypes();
        while (enumeration.hasMoreElements()) {
            SimpleType temp = (SimpleType) enumeration.nextElement();
            boolean found = false;
            //--check if this attributeGroup is not 
            //-- included 
            Enumeration includedSchemas = schema.getCachedIncludedSchemas();
            while (includedSchemas.hasMoreElements()) {
            	Schema tempSchema = (Schema)includedSchemas.nextElement();
            	found = (tempSchema.getSimpleType(temp.getName())!= null);
            }
            if (!temp.isRedefined() && !found)
                processSimpleType(temp, schemaPrefix);
        }

        _handler.endElement(ELEM_SCHEMA);

        _handler.endDocument();

    } //-- processSchema

    /**
     * Process a Wildcard (xsd:any) component
     *
     * @param wildcard the Wildcard to process
     * @param schemaPrefix the namespace prefix to use for schema elements
     */
    private void processWildcard(Wildcard wildcard, String schemaPrefix) 
        throws SAXException
    {
        
        String ELEMENT_NAME = null;
        if (wildcard.isAttributeWildcard())
            ELEMENT_NAME = schemaPrefix + SchemaNames.ANY_ATTRIBUTE;
        else 
            ELEMENT_NAME = schemaPrefix + SchemaNames.ANY;
            
        _atts.clear();
        
        //-- @namespace
        StringBuffer namespace = new StringBuffer();
        Enumeration enumeration = wildcard.getNamespaces();
        while (enumeration.hasMoreElements()) {
            if (namespace.length() > 0) namespace.append(' ');
            namespace.append(enumeration.nextElement().toString());
        }
        if (namespace.length() > 0) {
            _atts.addAttribute(SchemaNames.NAMESPACE, CDATA, namespace.toString());
        }
        
        //-- minOccurs/maxOccurs
        int max = wildcard.getMaxOccurs();
        int min = wildcard.getMinOccurs();

        if (min != 1) {
            _atts.addAttribute(SchemaNames.MIN_OCCURS_ATTR, CDATA,
                Integer.toString(min));
        }

        if (max < 0) {
            _atts.addAttribute(SchemaNames.MAX_OCCURS_ATTR, CDATA,
                "unbounded");
        }
        else if (max != 1) {
            _atts.addAttribute(SchemaNames.MAX_OCCURS_ATTR, CDATA,
                Integer.toString(max));
        }
        
        //-- @processContents
        String value = wildcard.getProcessContent();
        if (value != null) {
            _atts.addAttribute(SchemaNames.PROCESS_CONTENTS, CDATA, value);
        }
        _handler.startElement(ELEMENT_NAME, _atts);
        processAnnotated(wildcard, schemaPrefix);
        _handler.endElement(ELEMENT_NAME);
        
    } //-- processWildcard
    
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
     * Process an included schema
     *
     * @param schema the imported Schema to process
     * @param schemaPrefix the namespace prefix to use for schema elements
     **/
    private void processIncludedSchema(Schema schema, String schemaPrefix)
	throws SAXException
	{
    	String ELEMENT_NAME = schemaPrefix + SchemaNames.INCLUDE;
    	_atts.clear();

    	String schemaLoc = schema.getSchemaLocation();

    	_atts.addAttribute("schemaLocation", null, schemaLoc);
    	_handler.startElement(ELEMENT_NAME, _atts);
    	_handler.endElement(ELEMENT_NAME);
    } //-- processImport
    
    /**
     * Process a set of redefinitions 
     *
     * @param schema the redefined Schema to process
     * @param schemaPrefix the namespace prefix to use for schema elements
     **/
    private void processRedefinition(RedefineSchema schema, Schema parentSchema, String schemaPrefix)
	throws SAXException
	{
    	String ELEMENT_NAME = schemaPrefix + SchemaNames.REDEFINE;
    	_atts.clear();
        
    	String schemaLoc = schema.getSchemaLocation();
    	if (schemaLoc != "") 
      	    _atts.addAttribute("schemaLocation", null, schemaLoc);
    	
    	_handler.startElement(ELEMENT_NAME, _atts);
        
    	//-- process annotations
    	processAnnotated(schema, schemaPrefix);
    	
    	if (schemaLoc != "") {
	    	Enumeration enumeration = null;
	    	//--process complexTypes
	        enumeration = schema.enumerateComplexTypes();
	        while (enumeration.hasMoreElements()) {
	        	ComplexType type = (ComplexType)enumeration.nextElement();
	        	processComplexType(type, schemaPrefix);
	        }
	    	//--process simpleTypes
	        enumeration = schema.enumerateSimpleTypes();
	        while (enumeration.hasMoreElements()) {
	        	SimpleType type = (SimpleType)enumeration.nextElement();
	        	processSimpleType(type, schemaPrefix);
	        }
	    	//--process groups
	        enumeration = schema.enumerateGroups();
	        while (enumeration.hasMoreElements()) {
	        	ModelGroup group= (ModelGroup)enumeration.nextElement();
	        	processGroup(group, schemaPrefix);
	        }
	    	//--process AttributeGroups
	        enumeration = schema.enumerateAttributeGroups();
	        while (enumeration.hasMoreElements()) {
	        	AttributeGroupDecl attGroup = (AttributeGroupDecl)enumeration.nextElement();
	        	processAttributeGroup(attGroup, schemaPrefix);
	        }
    	}
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
            _atts.addAttribute(SchemaNames.NAME_ATTR, CDATA, name);
        }

        //-- @final
        if (simpleType.getFinal() != null) {
            _atts.addAttribute(SchemaNames.FINAL_ATTR, CDATA,
                simpleType.getFinal());
        }

        //-- @id
        if (simpleType.getId() != null) {
            _atts.addAttribute(SchemaNames.ID_ATTR, CDATA, simpleType.getId());
        }

        _handler.startElement(ELEMENT_NAME, _atts);

        //-- process annotations
        processAnnotated(simpleType, schemaPrefix);

        //-- handle restriction
        SimpleType base = (SimpleType)simpleType.getBaseType();
        boolean isRestriction = false;
        if (base != null) {
            if (simpleType instanceof ListType) {
                isRestriction = (base instanceof ListType);
            }
            else isRestriction = true;
        }
        
        if (isRestriction) {

            String ELEM_RESTRICTION = schemaPrefix + RESTRICTION;

            _atts.clear();

            String typeName = base.getName();
            //-- add "xsd" prefix if necessary
            if (typeName.indexOf(':') < 0) {
                if (base.isBuiltInType()) {
                    typeName = schemaPrefix + typeName;
                }
                else {
                    String targetNamespace = base.getSchema().getTargetNamespace();
                    String prefix = getNSPrefix(simpleType.getSchema(), targetNamespace);
                    if ((prefix != null) && (prefix.length() > 0)) {
                        typeName = prefix + ":" + typeName;
                    }
                }
            }
            _atts.addAttribute(SchemaNames.BASE_ATTR, CDATA, typeName);

            _handler.startElement(ELEM_RESTRICTION, _atts);

            //-- process facets
            Enumeration enumeration = simpleType.getLocalFacets();
            while (enumeration.hasMoreElements()) {
                Facet facet = (Facet) enumeration.nextElement();
                _atts.clear();
                _atts.addAttribute(SchemaNames.VALUE_ATTR, CDATA,
                    facet.getValue());
                String facetName = schemaPrefix + facet.getName();
                _handler.startElement(facetName, _atts);
                Enumeration annotations = facet.getAnnotations();
                while (annotations.hasMoreElements()) {
                    Annotation annotation = (Annotation)annotations.nextElement();
                    processAnnotation(annotation, schemaPrefix);
                }
                _handler.endElement(facetName);
            }

            _handler.endElement(ELEM_RESTRICTION);
        }
        else if (simpleType instanceof Union) {
            processUnion((Union)simpleType, schemaPrefix);
        }
        //-- handle List
        else {

            String ELEM_LIST = schemaPrefix + SchemaNames.LIST;

            _atts.clear();

            SimpleType itemType = ((ListType)simpleType).getItemType();

            boolean topLevel = (itemType.getParent() == itemType.getSchema());
            if (itemType.isBuiltInType() || topLevel) {
                String typeName = itemType.getName();
                //-- add "xsd" prefix if necessary
                if ((typeName.indexOf(':') < 0) && itemType.isBuiltInType()) {
                    typeName = schemaPrefix + typeName;
                }
                _atts.addAttribute("itemType", CDATA, typeName);
            }
            _handler.startElement(ELEM_LIST, _atts);

            //-- processAnnotations
            Annotation ann = ((ListType)simpleType).getLocalAnnotation();
            if (ann != null) {
                processAnnotation(ann, schemaPrefix);
            }
            //-- process simpleType if necessary
            if ((! topLevel) && (! itemType.isBuiltInType())) {
                processSimpleType(itemType, schemaPrefix);
            }
            _handler.endElement(ELEM_LIST);
        }

        _handler.endElement(ELEMENT_NAME);

    } //-- processSimpleType

    /**
     * Processes the given simpleType Union definition
     *
     * @param union the simpleType Union definition to process into events
     * @param schemaPrefix the namespace prefix to use for schema elements
    **/
    private void processUnion
        (Union union, String schemaPrefix)
        throws SAXException
    {

        String ELEMENT_NAME = schemaPrefix + SchemaNames.UNION;

        _atts.clear();

        if (union.getId() != null) {
            _atts.addAttribute(SchemaNames.ID_ATTR, CDATA,
                union.getId());
        }

        //-- process local simpleType references
        StringBuffer memberTypes = new StringBuffer();
        Enumeration enumeration = union.getMemberTypes();
        while (enumeration.hasMoreElements()) {
            SimpleType simpleType = (SimpleType)enumeration.nextElement();
            //-- ignore local simpleTypes;
            if (simpleType.getParent() != union.getSchema()) {
                continue;
            }
            //-- process top-level references
            if (memberTypes.length() > 0) memberTypes.append(' ');
            memberTypes.append(simpleType.getName());
        }
        if (memberTypes.length() > 0) {
            _atts.addAttribute(SchemaNames.MEMBER_TYPES_ATTR, CDATA,
                memberTypes.toString());
        }

        _handler.startElement(ELEMENT_NAME, _atts);

        //-- process local annotation
        Annotation annotation = union.getLocalAnnotation();
        if (annotation != null) {
            processAnnotation(annotation, schemaPrefix);
        }

        //-- process local simpleType definitions
        enumeration = union.getMemberTypes();
        while (enumeration.hasMoreElements()) {
            SimpleType simpleType = (SimpleType)enumeration.nextElement();
            //-- ignore top-level simpleTypes;
            if (simpleType.getParent() == union.getSchema())
                continue;
            processSimpleType(simpleType, schemaPrefix);
        }
        _handler.endElement(ELEMENT_NAME);

    } //-- processUnion

    /**
     * Determines if a given XMLType is imported by the
     * schema containing the element that refers to it.
     *
     * @param type the type to be checked to see if it is imported
     * @param element the schema element that references the type
    **/
    private boolean isImportedType(XMLType type, ElementDecl element) {
        String targetNS = type.getSchema().getTargetNamespace();
        if (targetNS != null)
            return (element.getSchema().getImportedSchema(targetNS) != null);
        else return false;
    } //-- isImportedType

    /**
     * Determines the proper namespace prefix for a namespace
     * by scanning all declared namespaces for the schema.
     *
     * @param schema the schema in which the namespace is declared
     * @param namespace the namespace for which a prefix will be returned
    **/
    private String getNSPrefix(Schema schema, String namespace){
    	if (namespace == null)
    		namespace = "";
    	return schema.getNamespaces().getNamespacePrefix(namespace);
    } //-- getNSPrefix
    

} //-- SchemaWriter
