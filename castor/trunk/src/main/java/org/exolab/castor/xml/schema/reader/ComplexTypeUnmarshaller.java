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
 * Copyright 1999-2002 (C) Intalio Inc. All Rights Reserved.
 *
 * $Id$
 */

package org.exolab.castor.xml.schema.reader;

//-- imported classes and packages
import org.exolab.castor.xml.AttributeSet;
import org.exolab.castor.xml.Namespaces;
import org.exolab.castor.xml.XMLException;
import org.exolab.castor.xml.schema.Annotation;
import org.exolab.castor.xml.schema.AttributeDecl;
import org.exolab.castor.xml.schema.AttributeGroupReference;
import org.exolab.castor.xml.schema.ComplexType;
import org.exolab.castor.xml.schema.ContentType;
import org.exolab.castor.xml.schema.ElementDecl;
import org.exolab.castor.xml.schema.Group;
import org.exolab.castor.xml.schema.ModelGroup;
import org.exolab.castor.xml.schema.Resolver;
import org.exolab.castor.xml.schema.Schema;
import org.exolab.castor.xml.schema.SchemaException;
import org.exolab.castor.xml.schema.SchemaNames;
import org.exolab.castor.xml.schema.Wildcard;
import org.exolab.castor.xml.schema.XMLType;

/**
 * A class for Unmarshalling ComplexTypes
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a>
 * @version $Revision$ $Date$
**/
public class ComplexTypeUnmarshaller extends ComponentReader {


      //--------------------/
     //- Member Variables -/
    //--------------------/

    /**
     * The current ComponentReader
    **/
    private ComponentReader unmarshaller;

    /**
     * The current branch depth
    **/
    private int depth = 0;

    /**
     * The Attribute reference for the Attribute we are constructing
    **/
    private ComplexType _complexType = null;
	private boolean allowAnnotation     = true;
    private boolean foundAnnotation     = false;
    private boolean foundAnyAttribute   = false;
    private boolean foundAttributes     = false;
    private boolean foundSimpleContent  = false;
    private boolean foundComplexContent = false;
    private boolean foundModelGroup     = false;



    private Schema _schema = null;


      //----------------/
     //- Constructors -/
    //----------------/

    /**
     * Creates a new ComplexTypeUnmarshaller
     * @param schema the Schema to which the ComplexType belongs
     * @param atts the AttributeList
     * @param resolver the resolver being used for reference resolving
    **/
    public ComplexTypeUnmarshaller
        (Schema schema, AttributeSet atts, Resolver resolver)
        throws XMLException
    {
        super();
        setResolver(resolver);
        this._schema = schema;

        _complexType = schema.createComplexType();

        _complexType.useResolver(resolver);

        //-- handle attributes
        String attValue = null;

        _complexType.setName(atts.getValue(SchemaNames.NAME_ATTR));

        //-- read contentType
        String content = atts.getValue(SchemaNames.MIXED);

		if (content != null) {
            if (content.equals("true"))
		       _complexType.setContentType(ContentType.valueOf("mixed"));
            if (content.equals("false"))
			   _complexType.setContentType(ContentType.valueOf("elementOnly"));
		}

        //-- base and derivedBy
        String base = atts.getValue(SchemaNames.BASE_ATTR);
        if ((base != null) && (base.length() > 0)) {

            String derivedBy = atts.getValue("derivedBy");
            _complexType.setDerivationMethod(derivedBy);
            if ((derivedBy == null) ||
                (derivedBy.length() == 0) ||
                (derivedBy.equals("extension")))
            {
                XMLType baseType= schema.getType(base);
                if (baseType == null)
                    _complexType.setBase(base); //the base type has not been read
                else
                    _complexType.setBaseType(baseType);
            }
            else if (derivedBy.equals("restrictions")) {
                String err = "restrictions not yet supported for <type>.";
                throw new SchemaException(err);
            }
            else {
                String err = "invalid value for derivedBy attribute of ";
                err += "<type>: " + derivedBy;
                throw new SchemaException(err);
            }

        }

        //-- @abstract
        attValue = atts.getValue(SchemaNames.ABSTRACT);
        if (attValue != null) {
            Boolean bool = Boolean.valueOf(attValue);
            _complexType.setAbstract(bool.booleanValue());
        }
        
		//-- @block
        _complexType.setBlock(atts.getValue(SchemaNames.BLOCK_ATTR));
        
        //-- @final
        _complexType.setFinal(atts.getValue(SchemaNames.FINAL_ATTR));
        
        //-- @id
        _complexType.setId(atts.getValue(SchemaNames.ID_ATTR));

    } //-- ComplexTypeUnmarshaller

      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the name of the element that this ComponentReader
     * handles
     * @return the name of the element that this ComponentReader
     * handles
    **/
    public String elementName() {
        return SchemaNames.COMPLEX_TYPE;
    } //-- elementName

    /**
     *
    **/
    public ComplexType getComplexType() {
        return _complexType;
    } //-- getComplexType

    /**
     * Returns the Object created by this ComponentReader
     * @return the Object created by this ComponentReader
    **/
    public Object getObject() {
        return getComplexType();
    } //-- getObject

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
     * @param nsDecls the namespace declarations being declared for this 
     * element. This may be null.
    **/
    public void startElement(String name, String namespace, AttributeSet atts,
        Namespaces nsDecls)
        throws XMLException
    {
        //-- Do delagation if necessary
        if (unmarshaller != null) {
            unmarshaller.startElement(name, namespace, atts, nsDecls);
            ++depth;
            return;
        }

        //-- <anyAttribute>
        if (SchemaNames.ANY_ATTRIBUTE.equals(name)) {
            if (foundComplexContent)
                error("an anyAttribute element cannot appear as a child "+
                    "of 'complexType' if 'complexContent' also exists");
            if (foundSimpleContent)
                error("an anyAttribute element cannot appear as a child "+
                    "of 'complexType' if 'simpleContent' also exists");
            foundAnyAttribute = true;
            allowAnnotation = true;
            unmarshaller
                 = new WildcardUnmarshaller(_complexType, _schema, name, atts, getResolver());
        }

        //-- attribute declarations
        else if (SchemaNames.ATTRIBUTE.equals(name)) {
            if (foundComplexContent)
                error("an attribute definition cannot appear as a child "+
                    "of 'complexType' if 'complexContent' also exists");
            if (foundSimpleContent)
                error("an 'attribute' definition cannot appear as a child "+
                    "of 'complexType' if 'simpleContent' also exists");
            if (foundAnyAttribute)
               error("an attribute definition cannot appear after "+
                    "the definition of an 'anyAttribute' element");
            foundAttributes = true;
            allowAnnotation = false;
            unmarshaller
                = new AttributeUnmarshaller(_schema, atts, getResolver());
        }
        //-- attribute group declarations
        else if (SchemaNames.ATTRIBUTE_GROUP.equals(name)) {
            //-- make sure we have an attribute group
            //-- reference and not a definition

            if (atts.getValue(SchemaNames.REF_ATTR) == null) {
                error("A 'complexType' may contain referring "+
                    "attributeGroups, but not defining ones.");
            }
            if (foundComplexContent)
                error("an attributeGroup reference cannot appear as a child "+
                    "of 'complexType' if 'complexContent' also exists");
            if (foundSimpleContent)
                error("an attributeGroup reference cannot appear as a child "+
                    "of 'complexType' if 'simpleContent' also exists");
            if (foundAnyAttribute)
               error("an 'attributeGroup' reference cannot appear after "+
                    "the definition of an 'anyAttribute' element");

            foundAttributes = true;
            allowAnnotation = false;
            unmarshaller
                = new AttributeGroupUnmarshaller(_schema, atts);
        }
        //-- simpleContent
        else if (SchemaNames.SIMPLE_CONTENT.equals(name)) {

            if (foundAttributes)
                error("'simpleContent' and attribute definitions cannot both "+
                    "appear as children of 'complexType' at the same time.");
            if (foundComplexContent)
                error("'simpleContent' and 'complexContent' cannot both "+
                    "appear as children of 'complexType'.");
            if (foundSimpleContent)
                error("Only one (1) 'simpleContent' may appear as a child of "+
                    "'complexType'.");
            if (foundModelGroup)
                error("'simpleContent' cannot appear as a child of "+
                    "'complexType' if 'all', 'sequence', 'choice' or "+
                    "'group' also exist");

            foundSimpleContent = true;
            allowAnnotation = false;
			_complexType.setSimpleContent(true);
            unmarshaller
                = new SimpleContentUnmarshaller(_complexType, atts, getResolver());
        }
        //-- complexContent
        else if (SchemaNames.COMPLEX_CONTENT.equals(name)) {

            if (foundAttributes)
                error("'complexContent' and attribute definitions cannot both "+
                    "appear as children of 'complexType' at the same time.");
            if (foundSimpleContent)
                error("'simpleContent' and 'complexContent' cannot both "+
                    "appear as children of 'complexType'.");
            if (foundComplexContent)
                error("Only one (1) 'complexContent' may appear as a child of "+
                    "'complexType'.");
            if (foundModelGroup)
                error("'complexContent' cannot appear as a child of "+
                    "'complexType' if 'all', 'sequence', 'choice' or "+
                    "'group' also exist");

            foundComplexContent = true;
            allowAnnotation = false;

            _complexType.setComplexContent(true);
			unmarshaller
                = new ComplexContentUnmarshaller(_complexType, atts, getResolver());
        }
         //--<group>
        else if ( name.equals(SchemaNames.GROUP) )
        {
            if (foundAttributes)
                error("'" + name + "' must appear before any attribute "+
                    "definitions when a child of 'complexType'.");
            if (foundComplexContent)
                error("'"+name+"' and 'complexContent' cannot both "+
                    "appear as children of 'complexType'.");
            if (foundSimpleContent)
                error("'"+name+"' and 'simpleContent' cannot both "+
                    "appear as children of 'complexType'.");
            if (foundModelGroup)
                error("'"+name+"' cannot appear as a child of 'complexType' "+
                    "if another 'all', 'sequence', 'choice' or "+
                    "'group' also exists.");

            foundModelGroup = true;
            allowAnnotation = false;
            unmarshaller
                = new ModelGroupUnmarshaller(_schema, atts, getResolver());
        }
        //-- ModelGroup declarations (choice, all, sequence)
        else if ( (SchemaNames.isGroupName(name)) && (name != SchemaNames.GROUP) )
        {

            if (foundAttributes)
                error("'" + name + "' must appear before any attribute "+
                    "definitions when a child of 'complexType'.");
            if (foundComplexContent)
                error("'"+name+"' and 'complexContent' cannot both "+
                    "appear as children of 'complexType'.");
            if (foundSimpleContent)
                error("'"+name+"' and 'simpleContent' cannot both "+
                    "appear as children of 'complexType'.");
            if (foundModelGroup)
                error("'"+name+"' cannot appear as a child of 'complexType' "+
                    "if another 'all', 'sequence', 'choice' or "+
                    "'group' also exists.");

            foundModelGroup = true;
            allowAnnotation = false;
            unmarshaller
                = new GroupUnmarshaller(_schema, name, atts, getResolver());
        }
        else if (name.equals(SchemaNames.ANNOTATION)) {
            if (allowAnnotation) {
                unmarshaller = new AnnotationUnmarshaller(atts);
                allowAnnotation = false;
                foundAnnotation = true;
            }
            else {
                if (foundAnnotation) {
                    error("Only one (1) annotation may appear as a child of "+
                        "'complexType' elements");
                }
                error("An annotation must appear as the first child of " +
                    "'complexType' elements.");
            }
        }
        else illegalElement(name);

        unmarshaller.setDocumentLocator(getDocumentLocator());
    } //-- startElement

    /**
     * Signals to end of the element with the given name.
     *
     * @param name the NCName of the element. It is an error
     * if the name is a QName (ie. contains a prefix).
     * @param namespace the namespace of the element.
    **/
    public void endElement(String name, String namespace)
        throws XMLException
    {

        //-- Do delagation if necessary
        if ((unmarshaller != null) && (depth > 0)) {
            unmarshaller.endElement(name, namespace);
            --depth;
            return;
        }
        //-- have unmarshaller perform any necessary clean up
        unmarshaller.finish();

        //-- <anyAttribute>
        if (SchemaNames.ANY_ATTRIBUTE.equals(name)) {
           Wildcard wildcard =
                 ((WildcardUnmarshaller)unmarshaller).getWildcard();
            try {
                _complexType.setAnyAttribute(wildcard);
            } catch (SchemaException e) {
                throw new IllegalArgumentException(e.getMessage());
            }
        }
        //-- attribute declarations
        if (SchemaNames.ATTRIBUTE.equals(name)) {
            AttributeDecl attrDecl =
                ((AttributeUnmarshaller)unmarshaller).getAttribute();

            _complexType.addAttributeDecl(attrDecl);
        }
        //-- attribute groups
        else if (SchemaNames.ATTRIBUTE_GROUP.equals(name)) {
            AttributeGroupReference attrGroupRef =
                (AttributeGroupReference) unmarshaller.getObject();
            _complexType.addAttributeGroupReference(attrGroupRef);
        }
        //-- element declarations
        else if (SchemaNames.ELEMENT.equals(name)) {

            ElementDecl element =
                ((ElementUnmarshaller)unmarshaller).getElement();
            _complexType.addElementDecl(element);
        }
        //--group
        else if (name.equals(SchemaNames.GROUP)) {
            ModelGroup group = ((ModelGroupUnmarshaller)unmarshaller).getGroup();
            _complexType.addGroup(group);
        }

        //-- group declarations (all, choice, sequence)
        else if ( (SchemaNames.isGroupName(name)) && (name != SchemaNames.GROUP) )
        {
            Group group = ((GroupUnmarshaller)unmarshaller).getGroup();
            _complexType.addGroup(group);
        }
        //-- annotation
        else if (SchemaNames.ANNOTATION.equals(name)) {
            Annotation ann = ((AnnotationUnmarshaller)unmarshaller).getAnnotation();
            _complexType.addAnnotation(ann);
        }

        unmarshaller = null;
    } //-- endElement

    public void characters(char[] ch, int start, int length)
        throws XMLException
    {
        //-- Do delagation if necessary
        if (unmarshaller != null) {
            unmarshaller.characters(ch, start, length);
        }
    } //-- characters

} //-- ComplexTypeUnmarshaller
