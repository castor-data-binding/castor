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

import org.exolab.castor.xml.AttributeSet;
import org.exolab.castor.xml.Namespaces;
import org.exolab.castor.xml.XMLException;
import org.exolab.castor.xml.schema.Annotation;
import org.exolab.castor.xml.schema.AttributeGroup;
import org.exolab.castor.xml.schema.ComplexType;
import org.exolab.castor.xml.schema.SchemaContext;
import org.exolab.castor.xml.schema.Group;
import org.exolab.castor.xml.schema.Schema;
import org.exolab.castor.xml.schema.SchemaException;
import org.exolab.castor.xml.schema.SchemaNames;
import org.exolab.castor.xml.schema.Wildcard;

import java.util.StringTokenizer;

/**
 * A class for Unmarshalling WildCard
 * @author <a href="mailto:blandin@intalio.com">Arnaud Blandin</a>
 * @version $Revision$ $Date: 2003-07-03 15:49:44 -0600 (Thu, 03 Jul 2003) $
**/
public class WildcardUnmarshaller extends ComponentReader {


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
     * The wildcard we are constructing
     */
     private Wildcard _wildcard = null;
    /**
     * The Schema being "unmarshalled"
    **/
    private Schema _schema = null;

    /**
     * The element name of the Group to be unmarshalled.
     */
    private String _element = SchemaNames.ANY;


      //----------------/
     //- Constructors -/
    //----------------/
    public WildcardUnmarshaller(
            final SchemaContext schemaContext,
            final ComplexType complexType, 
            final Schema schema,
            final String element,
            final AttributeSet atts) {
        this(schemaContext, schema, element, atts, new Wildcard(complexType));
    }

    public WildcardUnmarshaller(
            final SchemaContext schemaContext,
            final Group group,
            final Schema schema,
            final String element,
            final AttributeSet atts) {
        this(schemaContext, schema, element, atts, new Wildcard(group));
    }

    public WildcardUnmarshaller(
            final SchemaContext schemaContext,
            final AttributeGroup attGroup,
            final Schema schema,
            final String element,
            final AttributeSet atts) {
        this(schemaContext, schema, element, atts, new Wildcard(attGroup));
    }


    /**
     * Creates a new WildcardUnmarshaller.
     * @param schemaContext the {@link SchemaContext} to get some configuration settings from
     * @param schema the Schema to which the {@link Wildcard} belongs
     * @param element the name of the element
     * @param atts the AttributeList
    **/
    private WildcardUnmarshaller(
            final SchemaContext schemaContext,
            final Schema schema, 
            final String element, 
            final AttributeSet atts,
            final Wildcard wildcard) {
        super(schemaContext);
        _wildcard = wildcard;
        this._schema = schema;
        this._element = element;

        //-- handle attributes
        String attValue = null;

        if (SchemaNames.ANY_ATTRIBUTE.equals(element))
           _wildcard.setAttributeWildcard();
        _element = element;

        //--namespace
        attValue = atts.getValue(SchemaNames.NAMESPACE);

        if (attValue != null) {
           // check if there is more than one namespace
           StringTokenizer tokenizer = new StringTokenizer(attValue);
           while (tokenizer.hasMoreTokens()) {
               //need to retrieve all the namespaces
               String temp = tokenizer.nextToken();
               //if there is more than one namespace ##any or ##other should not
               //appear
               /**@todo optimize the following?*/
               if (tokenizer.countTokens() >1 )
                   if ( (SchemaNames.NAMESPACE_ANY.equals(temp)) ||
                        (SchemaNames.NAMESPACE_OTHER.equals(temp)) )
                        throw new IllegalArgumentException(temp+" is not valid when multiple namespaces are listed.");

               /**
                *@todo validation on the value of the attribute
                * we need a way to check the validity of an URI.
                * A temporary solution if to assume that the URI are URL.
                * @see SchemaNames#isNamespaceName()
                */
                if (SchemaNames.isNamespaceName(temp))
                   _wildcard.addNamespace(temp);
                else {
                     String err = "Invalid 'namespace' value: "+temp;
                     throw new IllegalArgumentException(err);
                }
           }
         }//if
         else _wildcard.addNamespace(SchemaNames.NAMESPACE_ANY);

        /*
         * @maxOccurs
         * If maxOccurs is present, the value is either unbounded
         * or the int value of the attribute, otherwise maxOccurs
         * equals the minOccurs value.
         */
        attValue = atts.getValue(SchemaNames.MAX_OCCURS_ATTR);
        if (attValue != null) {
            if (_wildcard.isAttributeWildcard())
                throw new IllegalStateException("'maxOccurs' is prohibited on a <anyAttribute> element.");
            if (MAX_OCCURS_WILDCARD.equals(attValue)) attValue = "-1";
            int maxOccurs = toInt(attValue);
            _wildcard.setMaxOccurs(maxOccurs);
        }
        //-- minOccurs
        attValue = atts.getValue("minOccurs");
        if (attValue != null) {
             if (_wildcard.isAttributeWildcard())
                throw new IllegalStateException("'minOccurs' is prohibited on a <anyAttribute> element.");
            _wildcard.setMinOccurs(toInt(attValue));
        }
        //-- processContents
        attValue = atts.getValue("processContents");

        if (attValue != null) {
           try {
               _wildcard.setProcessContents(attValue);
           } catch (SchemaException e) {
               throw new IllegalArgumentException(e.getMessage());
           }
        }


        //-- id
        _wildcard.setId(atts.getValue("id"));

    } //-- WildCardUnmarshaller

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
     * Returns the Wildcard unmarshalled by this Unmarshaller.
     * @return the unmarshalled Wildcard
     */
    public Wildcard getWildcard() {
        return _wildcard;
    }
    /**
     * Returns the Object created by this ComponentReader
     * @return the Object created by this ComponentReader
    **/
    public Object getObject() {
        return getWildcard();
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
         //-- <annotation>
        if (SchemaNames.ANNOTATION.equals(name)) {
            unmarshaller = new AnnotationUnmarshaller(getSchemaContext(), atts);
        }

        else {
            StringBuffer err = new StringBuffer("illegal element <");
            err.append(name);
            err.append("> found in <");
            err.append(_element);
            err.append(">");
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

        //-- <annotation>
        if (name == SchemaNames.ANNOTATION) {
            _schema.addAnnotation((Annotation)unmarshaller.getObject());
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

}