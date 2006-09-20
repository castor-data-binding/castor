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
import org.exolab.castor.xml.schema.Group;
import org.exolab.castor.xml.schema.Resolver;
import org.exolab.castor.xml.schema.Schema;
import org.exolab.castor.xml.schema.SchemaException;
import org.exolab.castor.xml.schema.SchemaNames;
import org.exolab.castor.xml.schema.Wildcard;
import org.exolab.castor.xml.schema.XMLType;

/**
 * A class for unmarshalling restriction elements of a complexContent
 * @author <a href="mailto:blandin@intalio.com">Arnaud Blandin</a>
 * @version $Revision$ $Date: 2006-04-14 04:14:43 -0600 (Fri, 14 Apr 2006) $
 * TODO: validation stuff about the data
 * TODO: set the flag of restriction for the complexType

**/
public class ComplexContentRestrictionUnmarshaller extends ComponentReader {


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
    private boolean     foundAnnotation   = false;
    private boolean     foundAttribute    = false;
    private boolean     foundAttributeGroup = false;
	private boolean     foundModelGroup     = false;

      //----------------/
     //- Constructors -/
    //----------------/
    /**
     * Creates a new RestrictionUnmarshaller
     * @param complexType the complexType being unmarshalled
     * @param atts the AttributeList
     */
    public ComplexContentRestrictionUnmarshaller
        (ComplexType complexType, AttributeSet atts, Resolver resolver)
        throws XMLException
    {
        super();
	    setResolver(resolver);
	    _complexType  = complexType;
        _schema  = complexType.getSchema();

        _complexType.setDerivationMethod(SchemaNames.RESTRICTION);
        _complexType.setRestriction(true);

        //-- base
        String base = atts.getValue(SchemaNames.BASE_ATTR);
        if ((base != null) && (base.length() > 0)) {

            XMLType baseType= _schema.getType(base);
            if (baseType == null)
                _complexType.setBase(base);
            // the base must not be a simpleType
			else if (baseType.isSimpleType()) {
                String err ="complexType: "+(_complexType.getName()) != null?
                                            _complexType.getName():"\n";
                err += "A complex type cannot be a restriction"+
                    " of a simpleType.";
                throw new IllegalStateException(err);
            }
            else if (!baseType.isAnyType()) {
			     // We are now sure the base is a complexType
			    // but is it already a restriction? (see PR 5.11->restriction->1.1)
			    
			    //-- KV 2004-03-15
			    //-- Need to valididate this constraint...I couldn't
			    //-- find it in the XML Schema 1.0 Recommendation,
			    //-- commenting out for now.
                /*
			    if (((ComplexType)baseType).isRestricted()) {
			       String err="complexType: "+(_complexType.getName()) != null?
                                            _complexType.getName():"\n";
				   err +="A complex type cannot be a restriction"+
					     " of a restriction.";
                   throw new IllegalStateException(err);
			    }
			    
			    */
            }
            _complexType.setBase(base);
			_complexType.setBaseType(baseType);

		}


    } //-- ComplexContentRestrictionUnmarshaller

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

            if (foundModelGroup || foundAttribute || foundAttributeGroup)
                error("An annotation must appear as the first child " +
                    "of 'restriction' elements.");

            if (foundAnnotation)
                error("Only one (1) annotation may appear as a child of "+
                    "'restriction' elements.");

            foundAnnotation = true;
            unmarshaller = new AnnotationUnmarshaller(atts);
        }

	   //-- ModelGroup declarations (choice, all, sequence, group)
        else if (SchemaNames.isGroupName(name)) {

            if (foundAttribute || foundAttributeGroup)
                error("'" + name + "' must appear before any attribute "+
                    "definitions when a child of 'restriction'.");
            if (foundModelGroup)
                error("'"+name+"' cannot appear as a child of 'restriction' "+
                    "if another 'all', 'sequence', 'choice' or "+
                    "'group' also exists.");

            foundModelGroup = true;
            unmarshaller
                = new GroupUnmarshaller(_schema, name, atts, getResolver());
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
        else if (SchemaNames.ANY_ATTRIBUTE.equals(name)) {
            Wildcard wildcard =
                 ((WildcardUnmarshaller)unmarshaller).getWildcard();
            try {
                _complexType.setAnyAttribute(wildcard);
            } catch (SchemaException e) {
                throw new IllegalArgumentException(e.getMessage());
            }
        }
		//-- attribute
		else if (SchemaNames.ATTRIBUTE.equals(name)) {
            AttributeDecl attrDecl =
                ((AttributeUnmarshaller)unmarshaller).getAttribute();

             /* TODO: do the validation later*/
            _complexType.addAttributeDecl(attrDecl);
        }
        //-- attribute groups
        else if (SchemaNames.ATTRIBUTE_GROUP.equals(name)) {
            AttributeGroupReference attrGroupRef =
                (AttributeGroupReference) unmarshaller.getObject();
            _complexType.addAttributeGroupReference(attrGroupRef);
        }
		//-- group declarations (all, choice, group, sequence)
        else if (SchemaNames.isGroupName(name)) {
            Group group = ((GroupUnmarshaller)unmarshaller).getGroup();
            _complexType.addGroup(group);
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
} //-- ComplexContentRestrictionUnmarshaller
