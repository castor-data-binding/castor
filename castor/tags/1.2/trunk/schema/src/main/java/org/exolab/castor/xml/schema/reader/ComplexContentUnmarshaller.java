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
 * Copyright 1999-2002 (C) Intalio Inc. All Rights Reserved.
 *
 * $Id$
 */

package org.exolab.castor.xml.schema.reader;

//-- imported classes and packages
import org.exolab.castor.xml.AttributeSet;
import org.exolab.castor.xml.Namespaces;
import org.exolab.castor.xml.XMLException;
import org.exolab.castor.xml.schema.Annotation;
import org.exolab.castor.xml.schema.ComplexType;
import org.exolab.castor.xml.schema.ContentType;
import org.exolab.castor.xml.schema.SchemaContext;
import org.exolab.castor.xml.schema.SchemaNames;

/**
 * A class for Unmarshalling simpleContent
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a>
 * @version $Revision$ $Date: 2006-04-14 04:14:43 -0600 (Fri, 14 Apr 2006) $
**/
public class ComplexContentUnmarshaller extends ComponentReader {

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
     * The Attribute reference for the Attribute we are constructing
    **/
    private ComplexType _complexType = null;

    private boolean foundAnnotation  = false;
    private boolean foundExtension   = false;
    private boolean foundRestriction = false;

      //----------------/
     //- Constructors -/
    //----------------/

    /**
     * Creates a new ComplexContentUnmarshaller.
     * @param schemaContext the schema context to get some configuration settings from
     * @param complexType the complexType we are unmarshalling
     * @param atts the AttributeList
    **/
    public ComplexContentUnmarshaller(
            final SchemaContext schemaContext, 
            final ComplexType complexType, 
            final AttributeSet atts)
    throws XMLException {
        super(schemaContext);

        _complexType = complexType;

        //-- read contentType
        String content = atts.getValue(SchemaNames.MIXED);

        if (content != null) {
            if (content.equals("true")) {
                _complexType.setContentType(ContentType.valueOf("mixed"));
            }
            if (content.equals("false")) {
                _complexType.setContentType(ContentType.valueOf("elementOnly"));
            }
        }

    } //-- ComplexContentUnmarshaller

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
        return SchemaNames.COMPLEX_CONTENT;
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

        //-- extension
        if (SchemaNames.EXTENSION.equals(name)) {

            if (foundExtension)
                error("Only (1) 'extension' element may appear as a child "+
                    "of 'complexContent' elements.");

            if (foundRestriction)
                error("Both 'extension' and 'restriction' elements may not "+
                    "appear as children of the same complexContent "+
                    "definition.");

            foundExtension = true;

            ExtensionUnmarshaller extension =
                new ExtensionUnmarshaller(getSchemaContext(), _complexType, atts);
            unmarshaller = extension;
        }
        //-- restriction
        else if (SchemaNames.RESTRICTION.equals(name)) {

            if (foundRestriction)
                error("Only (1) 'restriction' element may appear as a child "+
                    "of 'complexContent' elements.");

            if (foundExtension)
                error("Both 'extension' and 'restriction' elements may not "+
                    "appear as children of the same complexContent "+
                    "definition.");

            foundRestriction = true;
			unmarshaller=
			new ComplexContentRestrictionUnmarshaller(getSchemaContext(), _complexType, atts);
        }
        //-- annotation
        else if (name.equals(SchemaNames.ANNOTATION)) {
            if (foundAnnotation)
                error("Only (1) 'annotation' element may appear as a child "+
                    "of 'complexContent' elements.");

            if (foundRestriction || foundExtension)
                error("An 'annotation' may only appear as the first child "+
                    "of a 'complexContent' element.");

            foundAnnotation = true;
            unmarshaller = new AnnotationUnmarshaller(getSchemaContext(), atts);
        }
        else illegalElement(name);

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

} //-- ComplexContentUnmarshaller
