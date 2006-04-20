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
import org.exolab.castor.xml.schema.Group;
import org.exolab.castor.xml.schema.ModelGroup;
import org.exolab.castor.xml.schema.Resolver;
import org.exolab.castor.xml.schema.Schema;
import org.exolab.castor.xml.schema.SchemaException;
import org.exolab.castor.xml.schema.SchemaNames;

/**
 * A class for Unmarshalling ModelGroups Definition
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a>
 * @version $Revisio:$ $Date$
**/
public class ModelGroupUnmarshaller extends ComponentReader {

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
        (Schema schema, AttributeSet atts, Resolver resolver)
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
            if (_group.getName() != null)
                 throw new IllegalArgumentException("In <group>: "+_group.getName()+"'maxOccurs' cannot appear in a named <group>");
            if (MAX_OCCURS_WILDCARD.equals(attValue)) attValue = "-1";
            int maxOccurs = toInt(attValue);
            _group.setMaxOccurs(maxOccurs);
        }
        //-- minOccurs
        attValue = atts.getValue("minOccurs");
        if (attValue != null) {
            if (_group.getName() != null)
                 throw new IllegalArgumentException("In <group>: "+_group.getName()+", 'minOccurs' cannot appear in a named <group>");
            _group.setMinOccurs(toInt(attValue));
        }

        //-- @ref
        attValue = atts.getValue("ref");
        if (attValue != null) {
            if (_group.getName() != null)
                 throw new IllegalArgumentException("In <group>: "+_group.getName()+", 'ref' cannot appear in a named <group>");
//            String name = attValue;
//            int idx = name.indexOf(':');
//            String ns = null;
//            if (idx >= 0) {
//                String nsPrefix = name.substring(0,idx);
//                name = name.substring(idx + 1);
//                ns = (String) schema.getNamespace(nsPrefix);
//                if (ns == null)  {
//                    String err = "in the <group> referring: "+attValue;
//                    err += " The Namespace prefix is not recognized '"+nsPrefix+"'";
//                    throw new IllegalArgumentException(err);
//                }
//            }
//
//            if (ns != null) {
//                Schema tempSchema = schema.getImportedSchema(ns);
//                if (tempSchema!=null) {
//                     _group.setSchema(tempSchema);
//                }
//                tempSchema = null;
//            }

            _group.setReference(attValue);
        }
        //-- id
        attValue = atts.getValue("id");
        _group.setId(attValue);
        //-- not yet supported


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
     * Returns the Object created by this ComponentReader
     * @return the Object created by this ComponentReader
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
        throws XMLException
    {
        //-- Do delagation if necessary
        if (unmarshaller != null) {
            unmarshaller.characters(ch, start, length);
        }
    } //-- characters

} //-- ModelGroupUnmarshaller