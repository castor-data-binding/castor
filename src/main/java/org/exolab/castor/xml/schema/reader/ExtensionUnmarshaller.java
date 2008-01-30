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
 * Copyright 2000-2002 (C) Intalio Inc. All Rights Reserved.
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
import org.exolab.castor.xml.schema.Group;
import org.exolab.castor.xml.schema.ModelGroup;
import org.exolab.castor.xml.schema.Schema;
import org.exolab.castor.xml.schema.SchemaContext;
import org.exolab.castor.xml.schema.SchemaException;
import org.exolab.castor.xml.schema.SchemaNames;
import org.exolab.castor.xml.schema.SimpleContent;
import org.exolab.castor.xml.schema.SimpleType;
import org.exolab.castor.xml.schema.Wildcard;
import org.exolab.castor.xml.schema.XMLType;

/**
 * A class for Unmarshalling extension elements
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a>
 * @version $Revision$ $Date: 2005-12-13 14:58:48 -0700 (Tue, 13 Dec 2005) $
**/
public class ExtensionUnmarshaller extends ComponentReader {


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
    private Schema      _schema      = null;

    private boolean foundAnnotation  = false;
    private boolean foundAttributes  = false;
    private boolean foundModelGroup  = false;


      //----------------/
     //- Constructors -/
    //----------------/

    /**
     * Creates a new ExtensionUnmarshaller.
     * @param schemaContext the {@link SchemaContext} to get some configuration settings from
     * @param complexType the ComplexType being unmarshalled
     * @param atts the AttributeList
    **/
    public ExtensionUnmarshaller (
            final SchemaContext schemaContext,
            final ComplexType complexType,
            final AttributeSet atts)
        throws XMLException {
        super(schemaContext);

        _complexType = complexType;
        _schema      = complexType.getSchema();

        _complexType.setDerivationMethod(SchemaNames.EXTENSION);

        //-- base
        String base = atts.getValue(SchemaNames.BASE_ATTR);
        if ((base != null) && (base.length() > 0)) {

            XMLType baseType= _schema.getType(base);
            if (baseType == null) {
                _complexType.setBase(base); //the base type has not been read
                if (_complexType.isSimpleContent()) {
                    _complexType.setContentType(new SimpleContent(_schema, base));
                }
            }
		    else {
				 //--we cannot extend a simpleType in <complexContent>
				 if ( (baseType.isSimpleType()) &&
					  (_complexType.isComplexContent()) ) {
					String err = "In a 'complexContent', the base attribute "+
                    "must be a complexType but "+ base+" is a simpleType.\n";
                    error(err);
				 }
				 _complexType.setBase(base);
                 _complexType.setBaseType(baseType);
                if (_complexType.isSimpleContent()) {
                    //--set the content type
                    if (baseType.isSimpleType()) {
                        SimpleType simpleType = (SimpleType)baseType;
                	    _complexType.setContentType(new SimpleContent(simpleType));
                    } 
                    else {
                        ComplexType temp = (ComplexType)baseType;
                        SimpleContent simpleContent = (SimpleContent) temp.getContentType();
                        _complexType.setContentType(simpleContent.copy());
                    }
                }
                   
		    }

        }

    } //-- ExtensionUnmarshaller

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
        return SchemaNames.EXTENSION;
    } //-- elementName

    /**
     * Returns the Object created by this ComponentReader
     * @return the Object created by this ComponentReader
    **/
    public Object getObject() {
        return null;
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
            unmarshaller
                 = new WildcardUnmarshaller(getSchemaContext(), _complexType, _schema, name, atts);
        }

        //-- attribute declarations
        else if (SchemaNames.ATTRIBUTE.equals(name)) {
            foundAttributes = true;
            unmarshaller
                = new AttributeUnmarshaller(getSchemaContext(), _schema, atts);
        }
        //-- attribute group declarations
        else if (SchemaNames.ATTRIBUTE_GROUP.equals(name)) {

            //-- make sure we have an attribute group
            //-- reference and not a definition

            if (atts.getValue(SchemaNames.REF_ATTR) == null) {
                String err = "A complexType may contain referring "+
                    "attribute groups, but not defining ones.";
                error(err);
            }

            foundAttributes = true;
            unmarshaller
                = new AttributeGroupUnmarshaller(getSchemaContext(), _schema, atts);
        }
        //--<group>
        else if ( name.equals(SchemaNames.GROUP) )
        {
            if (foundAttributes)
                error("'" + name + "' must appear before any attribute "+
                    "definitions when a child of 'complexType'.");
            if (foundModelGroup)
                error("'"+name+"' cannot appear as a child of 'complexType' "+
                    "if another 'all', 'sequence', 'choice' or "+
                    "'group' also exists.");

            foundModelGroup = true;
            unmarshaller
                = new ModelGroupUnmarshaller(getSchemaContext(), _schema, atts);
        }
        else if (SchemaNames.isGroupName(name) && (name != SchemaNames.GROUP) ) {
            if (foundAttributes)
                error("'"+name+"' must appear before attribute "+
                    "definitions in an 'extension' element.");

            if (foundModelGroup)
                error("'"+name+"' cannot appear as a child of 'extension' "+
                    "if another 'all', 'sequence', 'choice' or "+
                    "'group' already exists.");

            if (_complexType.isSimpleContent())
                error("'"+name+"' may not appear in a 'extension' of "+
                    "'simpleContent'.");

            foundModelGroup = true;
            unmarshaller
                = new GroupUnmarshaller(getSchemaContext(), _schema, name, atts);
        }
        //-- element declarations
        else if (SchemaNames.ANY_ATTRIBUTE.equals(name)) {
            //-- not yet supported....
            error("anyAttribute is not yet supported.");
        }
        else if (name.equals(SchemaNames.ANNOTATION)) {
            if (foundAttributes || foundModelGroup)
                error("An annotation must appear as the first child of an " +
                    "'extension' element.");

            if (foundAnnotation)
                error("Only one (1) annotation may appear as the child of "+
                    "an 'extension' element.");

            foundAnnotation = true;
            unmarshaller = new AnnotationUnmarshaller(getSchemaContext(), atts);
        }
        else illegalElement(name);

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
        else if (SchemaNames.ATTRIBUTE.equals(name)) {
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

} //-- ExtensionUnmarshaller
