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
 * Copyright 1999-2000 (C) Intalio Inc. All Rights Reserved.
 *
 * $Id$
 */

package org.exolab.castor.xml.schema.reader;

//-- imported classes and packages
import org.exolab.castor.xml.*;
import org.exolab.castor.xml.schema.*;
import org.xml.sax.*;

/**
 * A class for Unmarshalling AttributeGroups
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a>
 * @version $Revision$ $Date$
**/
public class AttributeGroupUnmarshaller extends SaxUnmarshaller {


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
     * The AttributeGroup we are constructing
    **/
    private AttributeGroup _attributeGroup = null;

    private boolean allowAnnotation   = true;
    private boolean foundAnyAttribute = false;
    private boolean isRef             = false;
    private boolean requireName       = false;

    private Schema _schema = null;

      //----------------/
     //- Constructors -/
    //----------------/

    /**
     * Creates a new AttributeGroupUnmarshaller
     * @param schema the Schema to which the AttributeGroup belongs
     * @param atts the AttributeList
     * @param resolver the resolver being used for reference resolving
    **/
    public AttributeGroupUnmarshaller
        (Schema schema, AttributeList atts)
        throws SAXException
    {
        super();
        this._schema = schema;


        String ref = atts.getValue(SchemaNames.REF_ATTR);
        if (ref != null) {
            if (ref.length() > 0) {
                isRef = true;
                _attributeGroup = new AttributeGroupReference(schema, ref);
            }
            else {
                String err = "The value of the '" + SchemaNames.REF_ATTR +
                    "' attribute for attribute group must contain a " +
                    "valid value.";
                throw new SAXException(err);
            }
        }
        else {
            AttributeGroupDecl attDecl = new AttributeGroupDecl(schema);
            _attributeGroup = attDecl;

            //-- handle attributes
            attDecl.setName(atts.getValue(SchemaNames.NAME_ATTR));
            attDecl.setId(atts.getValue(SchemaNames.ID_ATTR));
        }



    } //-- AttributeGroupUnmarshaller

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
        return SchemaNames.ATTRIBUTE_GROUP;
    } //-- elementName

    /**
     * Returns the AttributeGroup created by this
     * AttributeGroupUnmarshaller
     *
     * @return the AttributeGroup created by this
     * AttributeGroupUnmarshaller
    **/
    public AttributeGroup getAttributeGroup() {
        return _attributeGroup;
    } //-- getAttributeGroup

    /**
     * Returns the Object created by this SaxUnmarshaller
     * @return the Object created by this SaxUnmarshaller
    **/
    public Object getObject() {
        return getAttributeGroup();
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

        //-- <anyAttribute>
        if (SchemaNames.ANY_ATTRIBUTE.equals(name)) {
            if (foundAnyAttribute)
                error("an anyAttribute element can appear only once as a child "+
                    "of an 'AttributeGroup'.");
            foundAnyAttribute = true;
            allowAnnotation = true;
            unmarshaller
                 = new WildcardUnmarshaller(_attributeGroup, _schema, name, atts, getResolver());
        }
        //-- attribute declarations
        else if (SchemaNames.ATTRIBUTE.equals(name)) {
            allowAnnotation = false;

            if (isRef)
                error("AttributeGroup references may not have children.");

            unmarshaller
                = new AttributeUnmarshaller(_schema, atts, getResolver());
        }
        //-- element declarations
        else if (SchemaNames.ATTRIBUTE_GROUP.equals(name)) {
            allowAnnotation = false;
            if (isRef)
                error("AttributeGroup references may not have children.");
            unmarshaller
                = new AttributeGroupUnmarshaller(_schema, atts);
        }
        else if (name.equals(SchemaNames.ANNOTATION)) {
            if (!allowAnnotation) outOfOrder(name);
            unmarshaller = new AnnotationUnmarshaller(atts);
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

         //-- <anyAttribute>
        if (SchemaNames.ANY_ATTRIBUTE.equals(name)) {
           Wildcard wildcard =
                 ((WildcardUnmarshaller)unmarshaller).getWildcard();
            try {
                ((AttributeGroupDecl)_attributeGroup).setAnyAttribute(wildcard);
            } catch (SchemaException e) {
                throw new IllegalArgumentException(e.getMessage());
            }
        }


        //-- attribute declarations
        else if (SchemaNames.ATTRIBUTE.equals(name)) {
            AttributeDecl attrDecl =
                ((AttributeUnmarshaller)unmarshaller).getAttribute();

            ((AttributeGroupDecl)_attributeGroup).addAttribute(attrDecl);
        }
        //-- attribute group references
        else if (SchemaNames.ATTRIBUTE_GROUP.equals(name)) {

            Object obj = unmarshaller.getObject();

            try {
                ((AttributeGroupDecl)_attributeGroup).addReference(
                    (AttributeGroupReference) obj);
            }
            catch (ClassCastException ex) {
                String err = "AttributeGroups cannot contain new " +
                    "AttributeGroup definitions, only references to " +
                    "top-level AttributeGroups are allowed.";
                error(err);
            }
        }
        //-- annotation
        else if (SchemaNames.ANNOTATION.equals(name)) {
            Annotation ann = ((AnnotationUnmarshaller)unmarshaller).getAnnotation();
            _attributeGroup.addAnnotation(ann);
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

} //-- AttributeGroupUnmarshaller
