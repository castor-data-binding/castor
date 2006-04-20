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
 * A class for Unmarshalling SimpleTypes
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a>
 * @version $Revision$ $Date$
**/
public class SimpleTypeUnmarshaller extends SaxUnmarshaller {


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
     * The SimpleTypeDefinition we are unmarshalling
    **/
    private SimpleTypeDefinition _simpleTypeDef = null;

    private boolean foundAnnotation   = false;
    private boolean foundList         = false;
    private boolean foundRestriction  = false;
    private boolean foundUnion        = false;

      //----------------/
     //- Constructors -/
    //----------------/

    /**
     * Creates a new SimpleTypeUnmarshaller
     * @param schema the Schema to which the SimpleType belongs
     * @param atts the AttributeList
     * @param resolver the resolver being used for reference resolving
    **/
    public SimpleTypeUnmarshaller
        (Schema schema, AttributeList atts)
        throws SAXException
    {
        super();
        
        String name = atts.getValue(SchemaNames.NAME_ATTR);
        String id   = atts.getValue(SchemaNames.ID_ATTR);

        _simpleTypeDef = new SimpleTypeDefinition(schema, name, id);
        

    } //-- SimpleTypeUnmarshaller

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
        return SchemaNames.SIMPLE_TYPE;
    } //-- elementName

    /**
     * Returns the SimpleType created
    **/
    public SimpleType getSimpleType() {
        return _simpleTypeDef.createSimpleType();
    } //-- getSimpletype

    /**
     * Returns the Object created by this SaxUnmarshaller
     * @return the Object created by this SaxUnmarshaller
    **/
    public Object getObject() {
        return getSimpleType();
    } //-- getObject

    public void finish() throws SAXException {
        if (!(foundList || foundUnion || foundRestriction))
            error ("Invalid 'simpleType'; missing 'restriction' "+
                "| 'union' | 'list'.");
                
    } //-- finish
    
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

        if (SchemaNames.ANNOTATION.equals(name)) {
            
            if (foundAnnotation) 
                error("Only one (1) annotation may appear as a child of "+
                    "'simpleType'.");
            
            if (foundList || foundUnion || foundRestriction)
                error("An annotation may only appear as the first child "+
                    "of 'simpleType'.");
            
            foundAnnotation = true;
            unmarshaller = new AnnotationUnmarshaller(atts);
        }
        else if (SchemaNames.RESTRICTION.equals(name)) {
            
            if (foundList)
                error("A 'simpleType' cannot have both a 'list' and a "+
                    "'restriction' in the same definition.");
                    
            if (foundUnion)
                error("A 'simpleType' cannot have both a 'union' and a "+
                    "'restriction' in the same definition.");

                    
            foundRestriction = true;
            
            unmarshaller 
                = new SimpleTypeRestrictionUnmarshaller(_simpleTypeDef, atts);
        }
        else if (SchemaNames.LIST.equals(name)) {
            foundList = true;
            
            error("'list' elements are not yet supported.");
        }
        else if (SchemaNames.UNION.equals(name)) {
            foundUnion = true;
            
            error("'union' elements are not yet supported.");
        }
        else illegalElement(name);

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

        if (SchemaNames.ANNOTATION.equals(name)) {
            Annotation annotation = (Annotation)unmarshaller.getObject();
            _simpleTypeDef.setAnnotation(annotation);
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

} //-- SimpleTypeUnmarshaller
