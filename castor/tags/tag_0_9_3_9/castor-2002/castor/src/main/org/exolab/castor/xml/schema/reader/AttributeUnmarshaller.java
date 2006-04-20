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
 * Copyright 1999 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id$
 */

package org.exolab.castor.xml.schema.reader;

//-- imported classes and packages
import org.exolab.castor.xml.*;
import org.exolab.castor.xml.schema.*;
import org.xml.sax.*;

/**
 * The Unmarshaller for Attribute declarations
 *
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a>
 * @version $Revision$ $Date$
**/
public class AttributeUnmarshaller extends SaxUnmarshaller {


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
    private AttributeDecl _attribute = null;

    private Schema _schema = null;

    private boolean foundAnnotation = false;
    private boolean foundSimpleType = false;


      //----------------/
     //- Constructors -/
    //----------------/

    public AttributeUnmarshaller
        (Schema schema, AttributeList atts, Resolver resolver)
    {
        super();
        this._schema = schema;

        setResolver(resolver);


        _attribute = new AttributeDecl(schema);

        //--@ref
        String attValue = atts.getValue(SchemaNames.REF_ATTR);
        if (attValue != null) {
            _attribute.setReference(attValue);
        }

        //-- @name
        attValue = atts.getValue(SchemaNames.NAME_ATTR);
        if (attValue != null) {
            if (_attribute.isReference()) {
                String err = "An attribute cannot have a 'name' attribute and a 'ref' attribute at the same time.";
                throw new IllegalStateException(err);
            }
            else _attribute.setName(attValue);
        }
        
        //-- @default
        attValue = atts.getValue(SchemaNames.DEFAULT_ATTR);
        if (attValue != null) {
            _attribute.setValue(attValue);
            _attribute.setDefault();
        }
        
        //-- @id
        _attribute.setId(atts.getValue(SchemaNames.ID_ATTR));

        //-- @fixed
        attValue = atts.getValue(SchemaNames.FIXED_ATTR);
        if (attValue != null) {
            if (_attribute.isDefault())
                throw new IllegalArgumentException("'default' and 'fixed' must not both be present.");
            _attribute.setValue(attValue);
            _attribute.setFixed();
        }

        //-- @form
        attValue = atts.getValue(SchemaNames.FORM);
        if (attValue != null) {
            _attribute.setForm(Form.valueOf(attValue));
        }
        
        //-- @type
        attValue = atts.getValue(SchemaNames.TYPE_ATTR);
        if (attValue != null) {
            _attribute.setSimpleTypeReference(attValue);
        }
        
        //-- @use
        attValue = atts.getValue(SchemaNames.USE_ATTR);
        if (attValue != null) {
            if (_attribute.isDefault() && (!attValue.equals(AttributeDecl.USE_OPTIONAL)) )
                throw new IllegalArgumentException("When 'default' is present, the 'use' attribute must have the value 'optional'.");
           _attribute.setUse(attValue);
        }

    } //-- AttributeUnmarshaller

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
        return SchemaNames.ATTRIBUTE;
    } //-- elementName

    /**
     *
    **/
    public AttributeDecl getAttribute() {
        return _attribute;
    } //-- getAttribute

    /**
     * Returns the Object created by this SaxUnmarshaller
     * @return the Object created by this SaxUnmarshaller
    **/
    public Object getObject() {
        return getAttribute();
    } //-- getObject

    /**
     *
     * @param name
     * @param atts
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

        if (SchemaNames.ANNOTATION.equals(name)) {

            if (foundAnnotation)
                error("Only one (1) annotation is allowed as a child of " +
                    "an attribute declaration.");

            if (foundSimpleType)
                error("An annotation may only appear as the first child of "+
                    "an attribute declaration.");

            foundAnnotation = true;
            unmarshaller = new AnnotationUnmarshaller(atts);
        }
        else if (SchemaNames.SIMPLE_TYPE.equals(name)) {
            if (foundSimpleType)
                error("Only one (1) simpleType is allowed as a child of " +
                    "an attribute declaration.");

            foundSimpleType = true;
            unmarshaller = new SimpleTypeUnmarshaller(_schema, atts);
        }
        else {
            illegalElement(name);
        }

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

        //-- call unmarshaller finish to perform any necessary cleanup
        unmarshaller.finish();

        if (SchemaNames.ANNOTATION.equals(name)) {
            Annotation ann = (Annotation) unmarshaller.getObject();
            _attribute.addAnnotation(ann);
        }
        else if (SchemaNames.SIMPLE_TYPE.equals(name)) {
            SimpleType simpleType =
                ((SimpleTypeUnmarshaller)unmarshaller).getSimpleType();
            _attribute.setSimpleType(simpleType);
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

} //-- AttributeUnmarshaller
