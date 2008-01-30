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
 * Copyright 1999-2002 (C) Intalio, Inc. All Rights Reserved.
 *
 * $Id$
 */

package org.exolab.castor.xml.schema.reader;

//-- imported classes and packages
import org.exolab.castor.xml.AttributeSet;
import org.exolab.castor.xml.Namespaces;
import org.exolab.castor.xml.XMLException;
import org.exolab.castor.xml.schema.Annotation;
import org.exolab.castor.xml.schema.ElementDecl;
import org.exolab.castor.xml.schema.Group;
import org.exolab.castor.xml.schema.ModelGroup;
import org.exolab.castor.xml.schema.Order;
import org.exolab.castor.xml.schema.Schema;
import org.exolab.castor.xml.schema.SchemaContext;
import org.exolab.castor.xml.schema.SchemaException;
import org.exolab.castor.xml.schema.SchemaNames;
import org.exolab.castor.xml.schema.Wildcard;

/**
 * A class for Unmarshalling ModelGroups
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a>
 * @version $Revision$ $Date: 2005-12-13 14:58:48 -0700 (Tue, 13 Dec 2005) $
**/
public class GroupUnmarshaller extends ComponentReader {

      //--------------------------/
     //- Static Class Variables -/
    //--------------------------/

    /**
     * The value of the maximum occurance wild card
     */
    private static final String MAX_OCCURS_WILDCARD = "unbounded";

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
     * The ModelGroup reference for the ModelGroup we are constructing
    **/
    private Group _group = null;

    /**
     * The Schema being "unmarshalled"
    **/
    private Schema _schema = null;

    /**
     * The element name of the Group to be unmarshalled.
    **/
    private String _element = SchemaNames.SEQUENCE;

    private boolean foundAll        = false;
    private boolean foundElement    = false;
    private boolean foundGroup      = false;
    private boolean foundModelGroup = false;
    private boolean foundAnnotation = false;

      //----------------/
     //- Constructors -/
    //----------------/

    /**
     * Creates a new GroupUnmarshaller.
     * @param schemaContext the {@link SchemaContext} to get some configuration settings from
     * @param schema the Schema to which the Group belongs
     * @param element the element name for this type of group
     * @param atts the AttributeList
    **/
    public GroupUnmarshaller(
            final SchemaContext schemaContext,
            final Schema schema,
            final String element,
            final AttributeSet atts) {
        super(schemaContext);

        this._schema = schema;

        _group = new Group();
        //-- handle attributes
        String attValue = null;


        if (SchemaNames.SEQUENCE.equals(element)) {
                _group.setOrder(Order.seq);
        }
        else if (SchemaNames.CHOICE.equals(element)) {
             _group.setOrder(Order.choice);
        }
        else if (SchemaNames.ALL.equals(element)) {
               foundAll = true;
             _group.setOrder(Order.all);
        }
        else {
                String err = "Invalid group element name: '" +
                    element + "'";
                throw new IllegalArgumentException(err);
            }


        _element = element;

        //-- set name
        attValue = atts.getValue(SchemaNames.NAME_ATTR);
        if (attValue != null) {
            _group.setName(attValue);
        }

        /*
         * @maxOccurs
         * If maxOccurs is present, the value is either unbounded
         * or the int value of the attribute, otherwise maxOccurs
         * equals the minOccurs value.
         */
        attValue = atts.getValue(SchemaNames.MAX_OCCURS_ATTR);

        if (attValue != null) {
            if (MAX_OCCURS_WILDCARD.equals(attValue))
                _group.setMaxOccurs(-1);
            else
                _group.setMaxOccurs(toInt(attValue));
        }
        //-- minOccurs
        attValue = atts.getValue("minOccurs");
        if (attValue != null)
            _group.setMinOccurs(toInt(attValue));

        if (_group.getOrder() == Order.all) {
            if (_group.getMaxOccurs() != 1) {
               String err = "Wrong maxOccurs value for a <all>:"+_group.getMaxOccurs();
               err += "\n1 is the only possible value.";
               throw new IllegalArgumentException(err);
            }
            if (_group.getMinOccurs() > 1) {
               String err = "Wrong minOccurs value for a <all>:"+_group.getMinOccurs();
               err += "\n0 or 1 are the only possible values.";
               throw new IllegalArgumentException(err);
            }
        }
        //-- id
        _group.setId(atts.getValue("id"));

    } //-- GroupUnmarshaller

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
        return _element;
    } //-- elementName


    /**
     * Returns the Group that was unmarshalled by this Unmarshaller.
     * This method should only be called after unmarshalling
     * has been completed.
     *
     * @return the unmarshalled Group
    **/
    public Group getGroup() {
        return _group;
    } //-- getGroup

    /**
     * Returns the Object created by this ComponentReader
     * @return the Object created by this ComponentReader
    **/
    public Object getObject() {
        return getGroup();
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

        if (SchemaNames.ANNOTATION.equals(name)) {
            if (foundElement || foundGroup ||foundModelGroup)
                error("An annotation may only appear as the first child "+
                    "of an element definition.");


            if (foundAnnotation)
                error("Only one (1) 'annotation' is allowed as a child of "+
                    "element definitions.");

            foundAnnotation = true;
            unmarshaller = new AnnotationUnmarshaller(getSchemaContext(), atts);
        }
        else if (SchemaNames.ELEMENT.equals(name)) {
            foundElement = true;
            unmarshaller
                = new ElementUnmarshaller(getSchemaContext(), _schema, atts);
        }
        //--group
        else if (name.equals(SchemaNames.GROUP))
        {
            foundModelGroup = true;
            unmarshaller
                = new ModelGroupUnmarshaller(getSchemaContext(), _schema, atts);
        }

        //--all, sequence, choice
        else if ( (SchemaNames.isGroupName(name)) && (name != SchemaNames.GROUP) )
        {
            foundGroup = true;
            if (SchemaNames.ALL.equals(name))
               foundAll = true;
            unmarshaller
                = new GroupUnmarshaller(getSchemaContext(), _schema, name, atts);
        }
        //--any
        else if (SchemaNames.ANY.equals(name)) {
             if (foundAll)
                error("<any> can not appear as a child of a <all> element");
             unmarshaller
                 = new WildcardUnmarshaller(getSchemaContext(), _group, _schema, name, atts);
        }

        else {
            StringBuffer err = new StringBuffer("illegal element <");
            err.append(name);
            err.append("> found in <group>.");
            throw new SchemaException(err.toString());
        }

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
        //-- check for name mismatches
        if (unmarshaller != null) {
            if (!name.equals(unmarshaller.elementName())) {
                String err = "missing end element for ";
                err += unmarshaller.elementName();
                throw new SchemaException(err);
            }
        }

        //-- have unmarshaller perform any necessary clean up
        unmarshaller.finish();

         //-- <any>
        if (SchemaNames.ANY.equals(name)) {
           Wildcard wildcard =
                 ((WildcardUnmarshaller)unmarshaller).getWildcard();
            try {
                _group.addWildcard(wildcard);
            } catch (SchemaException e) {
                throw new IllegalArgumentException(e.getMessage());
            }
        }
        if (SchemaNames.ANNOTATION.equals(name)) {
            Annotation ann = (Annotation)unmarshaller.getObject();
            _group.addAnnotation(ann);
        }
        else if (SchemaNames.ELEMENT.equals(name)) {
            ElementDecl element = (ElementDecl) unmarshaller.getObject();
            _group.addElementDecl(element);
        }
        else if (name.equals(SchemaNames.GROUP)) {
            ModelGroup group = (ModelGroup) unmarshaller.getObject();
            _group.addGroup(group);
        }
        else if ( (SchemaNames.isGroupName(name)) && (name != SchemaNames.GROUP) )
        {
            Group group = ((GroupUnmarshaller)unmarshaller).getGroup();
            _group.addGroup(group);
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

} //-- GroupUnmarshaller
