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
 * A class for Unmarshalling ModelGroups Definition
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a>
 * @version $Revisio:$ $Date$
**/
public class ModelGroupUnmarshaller extends SaxUnmarshaller {

      //--------------------------/
     //- Static Class Variables -/
    //--------------------------/

    private static final int MODEL_GROUP = 1;
    /**
     * The value of the maximum occurance wild card
     */
    private static final String MAX_OCCURS_WILDCARD = "unbounded";

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
     * The ModelGroup reference for the ModelGroup we are constructing
    **/
    private ModelGroup _group = null;

    /**
     * The Schema being "unmarshalled"
    **/
    private Schema _schema = null;

    /**
     * Flag to indicate if we have already encounter an <annotation>
     */
    private boolean foundAnnotation = false;
      //----------------/
     //- Constructors -/
    //----------------/

    /**
     * Creates a new ModelGroupUnmarshaller
     * @param schema the Schema to which the ModelGroup belongs
     * @param the element name for this type of group
     * @param atts the AttributeList
     * @param resolver the resolver being used for reference resolving
    **/
    public ModelGroupUnmarshaller
        (Schema schema, AttributeList atts, Resolver resolver)
    {
        super();
        setResolver(resolver);
        this._schema = schema;

        _group = new ModelGroup(_schema);

        //-- handle attributes
        String attValue = null;


        //-- set name
        _group.setName(atts.getValue("name"));

        /*
         * @maxOccurs
         * If maxOccurs is present, the value is either unbounded
         * or the int value of the attribute, otherwise maxOccurs
         * equals the minOccurs value.
         */
        attValue = atts.getValue(SchemaNames.MAX_OCCURS_ATTR);
        if (attValue != null) {
            if (MAX_OCCURS_WILDCARD.equals(attValue)) attValue = "-1";
            int maxOccurs = toInt(attValue);
            _group.setMaxOccurs(maxOccurs);
        }
        //-- minOccurs
        attValue = atts.getValue("minOccurs");
        if (attValue != null)
            _group.setMinOccurs(toInt(attValue));

        //-- id
        attValue = atts.getValue("id");
        _group.setId(attValue);
        //-- not yet supported

        //-- @ref
        attValue = atts.getValue("ref");
        if (attValue != null)
            _group.setReference(attValue);

    } //-- ModelGroupUnmarshaller

      //-----------/
     //- Methods -/
    //-----------/



    /**
     * Returns the Group that was unmarshalled by this Unmarshaller.
     * This method should only be called after unmarshalling
     * has been completed.
     *
     * @return the unmarshalled Group
    **/
    public ModelGroup getGroup() {
        return _group;
    } //-- getGroup

    /**
     * Returns the Object created by this SaxUnmarshaller
     * @return the Object created by this SaxUnmarshaller
    **/
    public Object getObject() {
        return getGroup();
    } //-- getObject

    /**
     * Sets the name of the element that this UnknownUnmarshaller handles
     * @param name the name of the element that this unmarshaller handles
    **/
    public String elementName() {
        return SchemaNames.GROUP;
    } //-- elementName

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
                error("Only one (1) 'annotation' is allowed as a child of "+
                    "element definitions.");

            foundAnnotation = true;
            unmarshaller = new AnnotationUnmarshaller(atts);
        }

        else if (SchemaNames.isGroupName(name)) {
            unmarshaller
                = new GroupUnmarshaller(_schema, name, atts, getResolver());
        }
        else {
            StringBuffer err = new StringBuffer("illegal element <");
            err.append(name);
            err.append("> found in <group>.");
            throw new SAXException(err.toString());
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

        //-- check for name mismatches
        if (unmarshaller != null) {
            if (!name.equals(unmarshaller.elementName())) {
                String err = "missing end element for ";
                err += unmarshaller.elementName();
                throw new SAXException(err);
            }
        }

        if (SchemaNames.ANNOTATION.equals(name)) {
            Annotation ann = (Annotation)unmarshaller.getObject();
            _group.addAnnotation(ann);
        }

         else if (SchemaNames.isGroupName(name)) {
            Group group = ((GroupUnmarshaller)unmarshaller).getGroup();
            _group.addGroup(group);
        }

        //-- have unmarshaller perform any necessary clean up
        unmarshaller.finish();
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

} //-- ModelGroupUnmarshaller