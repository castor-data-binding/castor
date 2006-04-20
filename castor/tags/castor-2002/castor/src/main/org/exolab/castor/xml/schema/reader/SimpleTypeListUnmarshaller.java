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
 * Copyright 2001 (C) Intalio Inc. All Rights Reserved.
 *
 * $Id$
 */

package org.exolab.castor.xml.schema.reader;

//-- imported classes and packages
import org.exolab.castor.xml.*;
import org.exolab.castor.xml.schema.*;
import org.exolab.castor.xml.schema.simpletypes.ListType;

import org.xml.sax.*;

/**
 * A class for unmarshalling list elements of a simpleType.
 * Thanks to  <a href="mailto:cdchudasama@yahoo.com">Chetan Chudasama</a>
 * for his help.
 * @author <a href="mailto:blandin@intalio.com">Arnaud Blandin</a>
 * @author <a href="mailto:kvsico@intalio.com">Keith Visco</a>
 * @version $Revision$
**/
public class SimpleTypeListUnmarshaller extends SaxUnmarshaller {


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
     * The simpleType we are unmarshalling
    **/
    private ListType    _list             = null;
    private Schema      _schema          = null;
    private boolean     foundAnnotation  = false;
    private boolean     foundSimpleType  = false;
    private boolean     foundItemType    = false;


      //----------------/
     //- Constructors -/
    //----------------/

    /**
     * Creates a new ListUnmarshaller
     * @param simpleType, the SimpleType being unmarshalled
     * @param atts the AttributeList
     * @param resolver the resolver being used for reference resolving
    **/
    SimpleTypeListUnmarshaller(Schema schema, AttributeList atts)
        throws SAXException
    {
        super();
        
        _schema = schema;
        _list = new ListType(schema);

        //-- itemType
        String itemTypeName = atts.getValue(SchemaNames.ITEM_TYPE_ATTR);
        if ((itemTypeName != null) && (itemTypeName.length() > 0)) {

            foundItemType = true;
            XMLType itemType= _schema.getType(itemTypeName);
            if (itemType == null) {
                _list.setItemType(itemTypeName);
            }
            else if (itemType.getStructureType() == Structure.COMPLEX_TYPE) {
                String err = "The item type of a list cannot "+
                    "be a complexType.";
                throw new IllegalStateException(err);
            }
            else _list.setItemType((SimpleType)itemType);
        }
        
        //-- @id
        _list.setId(atts.getValue(SchemaNames.ID_ATTR));
        
    } //-- SimpleTypeListUnmarshaller

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
        return SchemaNames.LIST;
    } //-- elementName

    /**
     * Called to signal an end of unmarshalling. This method should
     * be overridden to perform any necessary clean up by an unmarshaller
    **/
    public void finish() throws SAXException {
        if ((!foundItemType) && (!foundSimpleType)) {
            String err = "Missing sub-component of <list>, either use "+
                " the 'itemType' attribute, or an anonymous simpleType.";
            error(err);
        }
    } //-- finish
    
    /**
     * Returns the Object created by this SaxUnmarshaller
     * @return the Object created by this SaxUnmarshaller
    **/
    public Object getObject() {
        if ((!foundItemType) && (!foundSimpleType)) {
            String err = "Missing sub-component of <list>, either use "+
                " the 'itemType' attribute, or an anonymous simpleType.";
            throw new IllegalStateException(err);
        }
        return _list;
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

            if (foundSimpleType)
                error("An annotation must appear as the first child " +
                    "of 'list' elements.");

            if (foundAnnotation)
                error("Only one (1) annotation may appear as a child of "+
                    "'list' elements.");

            foundAnnotation = true;
            unmarshaller = new AnnotationUnmarshaller(atts);
        }
        else if (SchemaNames.SIMPLE_TYPE.equals(name)) {
            if (foundItemType)
                error("A 'list' element can have either an 'itemType' or " +
                      " 'simpleType'.");

            if (foundSimpleType)
                error("Only one (1) 'simpleType' may appear as a child of "+
                    "'list' elements.");

            foundSimpleType = true;
            unmarshaller = new SimpleTypeUnmarshaller(_schema, atts);

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
            _list.setLocalAnnotation(ann);
        }
        else if (SchemaNames.SIMPLE_TYPE.equals(name)) {
            SimpleType type = (SimpleType) unmarshaller.getObject();
            _list.setItemType(type);
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

} //-- SimpleTypeListUnmarshaller
