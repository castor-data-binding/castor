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

//-- imported classes and packages
import org.exolab.castor.xml.*;
import org.exolab.castor.xml.schema.*;
import org.xml.sax.*;

/**
 * A class for unmarshalling restriction elements of a simpleType
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a>
 * @version $Revision$ $Date$
**/
public class SimpleTypeRestrictionUnmarshaller extends SaxUnmarshaller {


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
    private SimpleTypeDefinition  _typeDefinition = null;

    private Schema      _schema          = null;
    private boolean     foundAnnotation  = false;
    private boolean     foundSimpleType  = false;
    private boolean     foundFacets      = false;

    /**
     * The base simpleType (ie the one we are restricting)
    **/
   // private SimpleType _baseType = null;

      //----------------/
     //- Constructors -/
    //----------------/

    /**
     * Creates a new RestrictionUnmarshaller
     * @param simpleType, the SimpleType being unmarshalled
     * @param atts the AttributeList
     * @param resolver the resolver being used for reference resolving
    **/
    public SimpleTypeRestrictionUnmarshaller
        (SimpleTypeDefinition typeDefinition, AttributeList atts)
        throws SAXException
    {
        super();

        _typeDefinition  = typeDefinition;
        _schema          = typeDefinition.getSchema();

        //-- base
        String base = atts.getValue(SchemaNames.BASE_ATTR);
        if ((base != null) && (base.length() > 0)) {

            XMLType baseType= _schema.getType(base);
            if (baseType == null)
                _typeDefinition.setBaseTypeName(base);
            else if (baseType.getStructureType() == Structure.COMPLEX_TYPE) {
                String err = "The base type of a simpleType cannot "+
                    "be a complexType.";
                throw new IllegalStateException(err);
            }
            else _typeDefinition.setBaseType( (SimpleType) baseType);
        }


    } //-- RestrictionUnmarshaller

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

            if (foundFacets || foundSimpleType)
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

            foundSimpleType = true;
            unmarshaller = new SimpleTypeUnmarshaller(_schema, atts);

        }
        else if (FacetUnmarshaller.isFacet(name)) {
            foundFacets = true;
            unmarshaller = new FacetUnmarshaller(name, atts);
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
            _typeDefinition.setAnnotation(ann);
        }
        else if (SchemaNames.SIMPLE_TYPE.equals(name)) {
            SimpleType type = (SimpleType) unmarshaller.getObject();
            _typeDefinition.setBaseType(type);
        }
        else {
            _typeDefinition.addFacet((Facet)unmarshaller.getObject());
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

} //-- SimpleTypeRestrictionUnmarshaller
