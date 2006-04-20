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
 * A class for Unmarshalling ComplexTypes
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a>
 * @version $Revision$ $Date$
**/
public class ComplexTypeUnmarshaller extends SaxUnmarshaller {


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
     * The Attribute reference for the Attribute we are constructing
    **/
    private ComplexType _complexType = null;

    private boolean allowRefines = true;

    private boolean allowContentModel = true;

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
        (Schema schema, AttributeList atts, Resolver resolver)
        throws SAXException
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
        String content = atts.getValue(SchemaNames.CONTENT_ATTR);
        if (content != null) {
            _complexType.setContentType(ContentType.valueOf(content));
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
                throw new SAXException(err);
            }
            else {
                String err = "invalid value for derivedBy attribute of ";
                err += "<type>: " + derivedBy;
                throw new SAXException(err);
            }

        }

    } //-- ComplexTypeUnmarshaller

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
        return SchemaNames.COMPLEX_TYPE;
    } //-- elementName

    /**
     *
    **/
    public ComplexType getComplexType() {
        return _complexType;
    } //-- getComplexType

    /**
     * Returns the Object created by this SaxUnmarshaller
     * @return the Object created by this SaxUnmarshaller
    **/
    public Object getObject() {
        return getComplexType();
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

        //-- attribute declarations
        if (SchemaNames.ATTRIBUTE.equals(name)) {
            allowRefines = false;
            allowContentModel = false;
            unmarshaller
                = new AttributeUnmarshaller(_schema, atts, getResolver());
        }
        //-- element declarations
        else if (SchemaNames.ELEMENT.equals(name)) {
            allowRefines = false;
            if (allowContentModel)
                unmarshaller
                    = new ElementUnmarshaller(_schema, atts, getResolver());
            else
                outOfOrder(name);
        }
        //-- ModelGroup declarations (choice, all, sequence, group)
        else if (SchemaNames.isGroupName(name)) {
            allowRefines = false;
            if (allowContentModel)
                unmarshaller
                    = new GroupUnmarshaller(_schema, name, atts, getResolver());
            else
                outOfOrder(name);
        }
        else if (name.equals("restrictions")) {
            String err = "<restrictions> element not yet supported.";
            throw new SAXException(err);
        }
        else if (name.equals(SchemaNames.ANNOTATION)) {
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

        //-- attribute declarations
        if (SchemaNames.ATTRIBUTE.equals(name)) {
            AttributeDecl attrDecl =
                ((AttributeUnmarshaller)unmarshaller).getAttribute();

            _complexType.addAttributeDecl(attrDecl);
        }
        //-- element declarations
        else if (SchemaNames.ELEMENT.equals(name)) {

            ElementDecl element =
                ((ElementUnmarshaller)unmarshaller).getElement();
            _complexType.addElementDecl(element);
        }
        //-- group declarations (all, choice, group, sequence)
        else if (SchemaNames.isGroupName(name)) {
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
        throws SAXException
    {
        //-- Do delagation if necessary
        if (unmarshaller != null) {
            unmarshaller.characters(ch, start, length);
        }
    } //-- characters

} //-- ComplexTypeUnmarshaller
