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
 * A class for Unmarshalling facets
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a>
 * @version $Revision$ $Date$
**/
public class FacetUnmarshaller extends SaxUnmarshaller {


      //--------------------/
     //- Member Variables -/
    //--------------------/

    /**
     * The current SaxUnmarshaller
    **/
    private SaxUnmarshaller unmarshaller = null;

    /**
     * The current branch depth
    **/
    private int depth = 0;

    /**
     * The Facet we are constructing
    **/
    private Facet _facet = null;

    /**
     * The element name of the Facet currently being unmarshalled
    **/
    private String _elementName = null;

      //----------------/
     //- Constructors -/
    //----------------/

    /**
     * Creates a new FacetUnmarshaller
     * @param name the name of the Facet
     * @param atts the AttributeList
    **/
    public FacetUnmarshaller (String name, AttributeList atts)
        throws SAXException
    {
        super();

        _elementName = name;

        if (!isFacet(name)) {
            String err = "'" + name + "' is not a valid or supported facet.";
            throw new IllegalArgumentException(err);
        }

        //-- handle attributes
        String attValue = null;

        _facet = new Facet(name, atts.getValue(SchemaNames.VALUE_ATTR));

    } //-- ArchetypeUnmarshaller

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
        return _elementName;
    } //-- elementName

    /**
     *
    **/
    public Facet getFacet() {
        return _facet;
    } //-- getArchetype

    /**
     * Returns the Object created by this SaxUnmarshaller
     * @return the Object created by this SaxUnmarshaller
    **/
    public Object getObject() {
        return getFacet();
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

        if (SchemaNames.ANNOTATION.equals(name)) {
            unmarshaller = new AnnotationUnmarshaller(atts);
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

        if (unmarshaller == null)
            throw new SAXException("missing start element: " + name);
        else if (SchemaNames.ANNOTATION.equals(name)) {
            Annotation annotation = (Annotation)unmarshaller.getObject();
            _facet.addAnnotation(annotation);
        }

    } //-- endElement

    public void characters(char[] ch, int start, int length)
        throws SAXException
    {
        //-- Do delagation if necessary
        if (unmarshaller != null) {
            unmarshaller.characters(ch, start, length);
        }
    } //-- characters

    private boolean isFacet(String name) {

        if (Facet.DURATION.equals(name))      return true;
        if (Facet.ENCODING.equals(name))      return true;
        if (Facet.ENUMERATION.equals(name))   return true;
        if (Facet.LENGTH.equals(name))        return true;
        if (Facet.PATTERN.equals(name))       return true;
        if (Facet.PRECISION.equals(name))     return true;
        if (Facet.MAX_EXCLUSIVE.equals(name)) return true;
        if (Facet.MIN_EXCLUSIVE.equals(name)) return true;
        if (Facet.MAX_INCLUSIVE.equals(name)) return true;
        if (Facet.MIN_INCLUSIVE.equals(name)) return true;
        if (Facet.MAX_LENGTH.equals(name))    return true;
        if (Facet.MIN_LENGTH.equals(name))    return true;
        if (Facet.PERIOD.equals(name))        return true;
        if (Facet.SCALE.equals(name))         return true;

        return false;
    } //-- isFacet

} //-- FacetUnmarshaller