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
 * Copyright 2000 (C) Intalio Inc. All Rights Reserved.
 *
 * $Id$
 */

package org.exolab.castor.xml.schema.reader;

import org.exolab.castor.xml.*;
import org.exolab.castor.xml.schema.*;
import org.xml.sax.*;

/**
 * A class for unmarshalling restriction elements of a complexContent
 * @author <a href="mailto:blandin@intalio.com">Arnaud Blandin</a>
 * @version $Revision$ $Date$
 * @todo validation stuff about the data
 * @todo set the flag of restriction for the complexType
**/
public class ComplexContentRestrictionUnmarshaller extends SaxUnmarshaller {


      //--------------------/
     //- Member Variables -/
    //--------------------/

    /**
     * The current SaxUnmarshaller
    **/
    private SaxUnmarshaller unmarshaller;

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
    private boolean     foundAttribute    = false;
    private boolean     foundAttributeGroup = false;
	private boolean     foundModelGroup     = false;

      //----------------/
     //- Constructors -/
    //----------------/
    /**
     * Creates a new RestrictionUnmarshaller
     * @param complexType, the complexType being unmarshalled
     * @param atts the AttributeList
     */
    public ComplexContentRestrictionUnmarshaller
        (ComplexType complexType, AttributeList atts, Resolver resolver)
        throws SAXException
    {
        super();
	    setResolver(resolver);
	    _complexType  = complexType;
        _schema  = complexType.getSchema();
        _id   = atts.getValue(SchemaNames.ID_ATTR);

        //-- base
        String base = atts.getValue(SchemaNames.BASE_ATTR);
        if ((base != null) && (base.length() > 0)) {

            XMLType baseType= _schema.getType(base);
            if (baseType == null)
                _complexType.setBase(base);
            // the base must not be a simpleType
			else if (baseType.getStructureType() == Structure.SIMPLE_TYPE) {
                String err ="complexType: "+(_complexType.getName()) != null?
                                            _complexType.getName():"\n";
                err += "A complex type cannot be a restriction"+
                    " of a simpleType.";
                throw new IllegalStateException(err);
            }
			// We are now sure the base is a complexType
			// but is it already a restriction? (see CR 5.11->restriction->1.1)
			else if (((ComplexType)baseType).isRestricted()) {
			       String err="complexType: "+(_complexType.getName()) != null?
                                            _complexType.getName():"\n";
				   err +="A complex type cannot be a restriction"+
					     " of a restriction.";
				  throw new IllegalStateException(err);
			} else
			{
				 // set the base
				 _complexType.setBaseType((ComplexType)baseType);
            }
		}


    } //-- SimpleContentRestrictionUnmarshaller

      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the name of the element that this SaxUnmarshaller
     * handles
     * @return the name of the element that this SaxUnmarshaller
     * handles
    **/
    public String elementName() {
        return SchemaNames.RESTRICTION;
    } //-- elementName

    /**
     * Returns the Object created by this SaxUnmarshaller
     * @return the Object created by this SaxUnmarshaller
    **/
    public Object getObject() {
        return null;
    } //-- getObject

    /**
     * @param name
     * @param atts
     * @see org.xml.sax.DocumentHandler
    **/
    public void startElement(String name, AttributeList atts)
        throws org.xml.sax.SAXException
    {
        //-- Do delagation if necessary
        if (unmarshaller != null) {
            unmarshaller.startElement(name, atts);
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
		else if (SchemaNames.ANY_ATTRIBUTE.equals(name)) {
            //-- not yet supported....
            error("anyAttribute is not yet supported.");
		}
		else illegalElement(name);

        unmarshaller.setDocumentLocator(getDocumentLocator());
    } //-- startElement

    /**
     *
     * @param name
    **/
    public void endElement(String name)
        throws org.xml.sax.SAXException
    {

        //-- Do delagation if necessary
        if ((unmarshaller != null) && (depth > 0)) {
            unmarshaller.endElement(name);
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

		//-- attribute
		else if (SchemaNames.ATTRIBUTE.equals(name)) {
            AttributeDecl attrDecl =
                ((AttributeUnmarshaller)unmarshaller).getAttribute();

             /**@todo do the validation later*/
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
        throws SAXException
    {
        //-- Do delagation if necessary
        if (unmarshaller != null) {
            unmarshaller.characters(ch, start, length);
        }
    } //-- characters
} //-- ComplexContentRestrictionUnmarshaller
