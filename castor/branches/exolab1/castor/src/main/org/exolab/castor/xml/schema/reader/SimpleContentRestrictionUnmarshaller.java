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

import org.exolab.castor.xml.AttributeSet;
import org.exolab.castor.xml.Namespaces;
import org.exolab.castor.xml.XMLException;
import org.exolab.castor.xml.schema.Annotation;
import org.exolab.castor.xml.schema.AttributeDecl;
import org.exolab.castor.xml.schema.AttributeGroupReference;
import org.exolab.castor.xml.schema.ComplexType;
import org.exolab.castor.xml.schema.ContentType;
import org.exolab.castor.xml.schema.Facet;
import org.exolab.castor.xml.schema.Resolver;
import org.exolab.castor.xml.schema.Schema;
import org.exolab.castor.xml.schema.SchemaException;
import org.exolab.castor.xml.schema.SchemaNames;
import org.exolab.castor.xml.schema.SimpleType;
import org.exolab.castor.xml.schema.Structure;
import org.exolab.castor.xml.schema.Wildcard;
import org.exolab.castor.xml.schema.XMLType;

/**
 * A class for unmarshalling restriction elements of a simpleContent
 * @author <a href="mailto:blandin@intalio.com">Arnaud Blandin</a>
 * @version $Revision$ $Date$
 * @todo supprot the correct restriction for facets and attributes
**/
public class SimpleContentRestrictionUnmarshaller extends ComponentReader {


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
     * The complexType we are unmarshalling
    **/
    private ComplexType _complexType = null;

    private Schema      _schema           = null;
    private String      _id               = null;
    private boolean     foundAnnotation   = false;
    private boolean     foundSimpleType   = false;
	private boolean     foundFacets       = false;
    private boolean     foundAnyAttribute = false;
	private boolean     foundAttribute    = false;
    private boolean     foundAttributeGroup = false;

    /**
     * The content type 
    **/
    private SimpleTypeDefinition _content = null;

      //----------------/
     //- Constructors -/
    //----------------/
    /**
     * Creates a new RestrictionUnmarshaller
     * @param complexType, the complexType being unmarshalled
     * @param atts the AttributeList
     */
    public SimpleContentRestrictionUnmarshaller
        (ComplexType complexType, AttributeSet atts, Resolver resolver)
        throws XMLException
    {
        super();
	    setResolver(resolver);
	    _complexType  = complexType;
        _complexType.setDerivationMethod(SchemaNames.RESTRICTION);
        _complexType.setRestriction(true);
        _schema  = complexType.getSchema();
        _id   = atts.getValue(SchemaNames.ID_ATTR);

        //-- base
        String base = atts.getValue(SchemaNames.BASE_ATTR);
        
        if ((base != null) && (base.length() > 0)) {

            XMLType baseType= _schema.getType(base);
            if (baseType == null)
                _complexType.setBase(base);
            else if (baseType.getStructureType() == Structure.SIMPLE_TYPE) {
                String err ="complexType: "+(_complexType.getName()) != null?
                                            _complexType.getName():"\n";
                err += "A complex type cannot be a restriction"+
                    " of a simpleType:";
                err += baseType.getName();
                throw new IllegalStateException(err);
            }
			//we are now sure that the base is a ComplexType
            //but is the base of this complexType a simpleType? (see 4.3.3->simpleContent->content type)
            else if ( ( (ComplexType) baseType).getContentType().getType() != ContentType.SIMPLETYPE) {
                String err ="complexType: "+(_complexType.getName()) != null?
                                            _complexType.getName():"\n";
                err += "In a simpleContent when using restriction the base type"+
                    " must be a complexType whose base is a simpleType.";
                throw new IllegalStateException(err);
            }

            else {
				 //retrieve the base type of this complexType
                 //the base type is the complexType but we have to
                 //work with the simple type of the content type.
                 ComplexType temp = (ComplexType)baseType;
                 _complexType.setBaseType(temp);
                 _complexType.setBase(temp.getName());
			     _content = new SimpleTypeDefinition(_schema, temp.getName(),_id);
			     
                 _content.setBaseType( temp.getContentType().getSimpleType());
                 temp = null;
            }
		}


    } //-- SimpleContentRestrictionUnmarshaller

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
        return SchemaNames.RESTRICTION;
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


        //-- annotation
        if (name.equals(SchemaNames.ANNOTATION)) {

            if (foundFacets || foundSimpleType ||
			    foundAttribute || foundAttributeGroup)
                error("An annotation must appear as the first child " +
                    "of 'restriction' elements.");

            if (foundAnnotation)
                error("Only one (1) annotation may appear as a child of "+
                    "'restriction' elements.");

            foundAnnotation = true;
            unmarshaller = new AnnotationUnmarshaller(atts);
        }

		else if (SchemaNames.SIMPLE_TYPE.equals(name)) {
            if (foundSimpleType)
                error("Only one (1) 'simpleType' may appear as a child of "+
                    "'restriction' elements.");

            if (foundFacets)
                error("A 'simpleType', as a child of 'restriction' "+
                    "elements, must appear before any facets.");

            if (foundAttribute || foundAttributeGroup)
	             error("A 'simpleType', as a child of 'restriction' "+
                    "elements, must appear before any attribute elements.");

			foundSimpleType = true;
            unmarshaller = new SimpleTypeUnmarshaller(_schema, atts);

        }
        else if (FacetUnmarshaller.isFacet(name)) {
            foundFacets = true;
            if (foundAttribute || foundAttributeGroup)
	             error("A 'facet', as a child of 'restriction' "+
                    "elements, must appear before any attribute elements.");

			unmarshaller = new FacetUnmarshaller(name, atts);
        }
        else if (SchemaNames.ATTRIBUTE.equals(name)) {
             foundAttribute = true;
             unmarshaller = new AttributeUnmarshaller(_schema,atts, getResolver());
		}
		else if (SchemaNames.ATTRIBUTE_GROUP.equals(name)) {

			 //--In a complexType we only reference attribute group
			 if (atts.getValue(SchemaNames.REF_ATTR) == null) {
                error("A 'complexType' may contain referring "+
                    "attributeGroups, but not defining ones.");
             }
			 foundAttributeGroup = true;
			 unmarshaller = new AttributeGroupUnmarshaller(_schema,atts);
		}
        //-- <anyAttribute>
        else if (SchemaNames.ANY_ATTRIBUTE.equals(name)) {
           foundAnyAttribute = true;
            unmarshaller
                 = new WildcardUnmarshaller(_complexType, _schema, name, atts, getResolver());
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

        //-- annotation
        if (SchemaNames.ANNOTATION.equals(name)) {
            Annotation ann = ((AnnotationUnmarshaller)unmarshaller).getAnnotation();
            _complexType.addAnnotation(ann);
        }
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
        //Note: the attributes are added to the complexType
        //since a simpleType does not contain attributes at all
        //-- attribute
		else if (SchemaNames.ATTRIBUTE.equals(name)) {
            AttributeDecl attrDecl =
                ((AttributeUnmarshaller)unmarshaller).getAttribute();

			/**@todo add the validation code later*/

			/*ComplexType baseType = (ComplexType)_complexType.getBaseType();
			if ( (baseType.getAttributeDecls() == null) ||
			     (baseType.getAttributeDecl(attrDecl.getName()) == null) )
				      error("The restricted attribute must be present in the"
                             +" base type.");
             baseType = null;*/

            _complexType.addAttributeDecl(attrDecl);
        }
        //-- attribute groups
        else if (SchemaNames.ATTRIBUTE_GROUP.equals(name)) {
            AttributeGroupReference attrGroupRef =
                (AttributeGroupReference) unmarshaller.getObject();
            _complexType.addAttributeGroupReference(attrGroupRef);
        }
        //-- simpleType
		else if (SchemaNames.SIMPLE_TYPE.equals(name)) {
            SimpleType type = (SimpleType) unmarshaller.getObject();
            ContentType contentType = ContentType.valueOf("simpleType");
            contentType.setSimpleType(type);
            _complexType.setContentType(contentType);
            type = null;
            contentType = null;
           
        }
        //--facet
		else {
            Facet f = (Facet)unmarshaller.getObject();
         	_content.addFacet((Facet)unmarshaller.getObject());
         	foundFacets = true;
            //set the flag in order to create the new base in
            //the finish() method
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

    /**
     * Terminates the process of this restriction by
     * setting a proper base.
     * We set a new base if the base simple type has been restricted
     * by the use of facets since all other restrictions may concern the
     * complexType character of the type (i.e attribute for instance is
     * only related to a complexType...)
     */
    public void finish() {

        if (foundFacets) {
            SimpleType baseType = _content.createSimpleType();
            ContentType contentType = ContentType.valueOf("simpleType");
            contentType.setSimpleType(baseType);
            _complexType.setContentType(contentType);
            baseType = null;
            contentType = null;
        }
		//the restriction was properly handle
		//we can set the flag
		_complexType.setRestriction(true);
    }
} //-- SimpleContentRestrictionUnmarshaller