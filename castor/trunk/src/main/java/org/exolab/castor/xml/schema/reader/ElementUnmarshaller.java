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
 * Copyright 1999-2003 (C) Intalio Inc. All Rights Reserved.
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
import org.exolab.castor.xml.schema.Form;
import org.exolab.castor.xml.schema.IdentityConstraint;
import org.exolab.castor.xml.schema.Resolver;
import org.exolab.castor.xml.schema.Schema;
import org.exolab.castor.xml.schema.SchemaException;
import org.exolab.castor.xml.schema.SchemaNames;
import org.exolab.castor.xml.schema.XMLType;

/**
 * A class for Unmarshalling element definitions
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a>
 *
 * @version $Revision$ $Date: 2003-03-03 02:57:21 -0700 (Mon, 03 Mar 2003) $
 */
public class ElementUnmarshaller extends ComponentReader {

    /**
     * The value of the maximum occurance wild card
    **/
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
     * The element reference for the element definition we are "unmarshalling".
    **/
    private ElementDecl _element = null;


    private CharacterUnmarshaller charUnmarshaller = null;

    private Schema _schema = null;

    private boolean foundAnnotation         = false;
    private boolean foundIdentityConstraint = false;
    private boolean foundSimpleType         = false;
    private boolean foundComplexType        = false;
    private boolean foundTypeReference      = false;

      //----------------/
     //- Constructors -/
    //----------------/

    /**
     * Creates a new ElementUnmarshaller
     * @param schema the Schema to which the Element belongs
     * @param atts the AttributeList
     * @param resolver the resolver being used for reference resolving
    **/
    public ElementUnmarshaller
        (Schema schema, AttributeSet atts, Resolver resolver)
        throws XMLException
    {
        super();
        setResolver(resolver);

        this._schema = schema;

        _element = new ElementDecl(schema);

        String attValue = null;

        //-- @ref
        attValue = atts.getValue(SchemaNames.REF_ATTR);
        if (attValue != null) {
            _element.setReferenceName(attValue);
            //-- report error if name attr exists also
            if (atts.getValue(SchemaNames.NAME_ATTR) != null) {
                error("The attributes 'ref' and 'name' appearing on " +
                    "element declarations are mutually exclusive.");
            }
            validateRefAtts(atts);
        }
        //-- @name
        else {
            _element.setName(atts.getValue(SchemaNames.NAME_ATTR));
        }

        //-- @abstract
        attValue = atts.getValue(SchemaNames.ABSTRACT);
        if (attValue != null) {
            _element.setAbstract((new Boolean(attValue)).booleanValue());
        }
        
        //-- @block
        _element.setBlock(atts.getValue(SchemaNames.BLOCK_ATTR));
        
        //-- @default
        attValue = atts.getValue(SchemaNames.DEFAULT_ATTR);
        if (attValue != null) {
            if (_element.getFixedValue() != null)
                error("'default' and 'fixed' must not both be present.");
            _element.setDefaultValue(attValue);
        }

        //-- @final
        _element.setFinal(atts.getValue(SchemaNames.FINAL_ATTR));

        //-- @abstract
        final boolean isAbstract = new Boolean(atts.getValue(SchemaNames.ABSTRACT)).booleanValue();
        if (isAbstract) {
            _element.setAbstract(isAbstract);
        }

        //-- @fixed
        attValue = atts.getValue(SchemaNames.FIXED_ATTR);
        if (attValue != null) {
            if (_element.getDefaultValue() != null)
                throw new IllegalArgumentException("'default' and 'fixed' must not both be present.");
            _element.setFixedValue(attValue);
        }
        
        //-- @form
        attValue = atts.getValue(SchemaNames.FORM);
        if (attValue != null) {
            _element.setForm(Form.valueOf(attValue));
        }
        
        //-- @id
        _element.setId(atts.getValue(SchemaNames.ID_ATTR));
        
        //-- @substitutionGroup
        attValue = atts.getValue(SchemaNames.SUBSTITUTION_GROUP_ATTR);
        if (attValue != null) {
            _element.setSubstitutionGroup(attValue);
        }
        
        //-- @type
        attValue = atts.getValue(SchemaNames.TYPE_ATTR);
        if (attValue != null) {
            foundTypeReference = true;
            _element.setTypeReference(attValue);
        }
        

        //-- @nillable
        attValue = atts.getValue(SchemaNames.NILLABLE_ATTR);
        if (attValue != null) {
            if (attValue.equals("true") || attValue.equals("1")) { 
                _element.setNillable(true);
            }
            else if (!attValue.equals("false") && !attValue.equals("0")) {
                String err = "Invalid value for the 'nillable' attribute of "+
                    "an element definition: " + attValue;
                throw new IllegalArgumentException(err);
            }
        }

        /*
         * @minOccurs
         * if minOccurs is present the value is the int value of
         * of the attribute, otherwise minOccurs is 1.
         */
        attValue = atts.getValue(SchemaNames.MIN_OCCURS_ATTR);
        int minOccurs = 1;
        if (attValue != null) {
            minOccurs = toInt(attValue);
            _element.setMinOccurs(minOccurs);
        }

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
            _element.setMaxOccurs(maxOccurs);
        }
        else if (minOccurs > 0)
            _element.setMaxOccurs(minOccurs);
        else
            _element.setMaxOccurs(1);

        charUnmarshaller = new CharacterUnmarshaller();
    } //-- ElementUnmarshaller

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
        return SchemaNames.ELEMENT;
    } //-- elementName

    /**
     *
    **/
    public ElementDecl getElement() {
        return _element;
    } //-- getElement

    /**
     * Returns the Object created by this ComponentReader
     * @return the Object created by this ComponentReader
    **/
    public Object getObject() {
        return _element;
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
            if (foundSimpleType || foundIdentityConstraint ||foundComplexType)
                error("An annotation may only appear as the first child "+
                    "of an element definition.");


            if (foundAnnotation)
                error("Only one (1) 'annotation' is allowed as a child of "+
                    "element definitions.");

            foundAnnotation = true;
            unmarshaller = new AnnotationUnmarshaller(atts);
        }
        else if (SchemaNames.COMPLEX_TYPE.equals(name)) {

            if (foundComplexType)
                error("Only one (1) 'complexType' may appear in an "+
                    "element definition.");
            if (foundSimpleType)
                error("Both 'simpleType' and 'complexType' cannot appear "+
                    "in the same element definition.");
            if (foundTypeReference)
                error("Both 'type' attribute and 'complexType' element "+
                    "cannot appear in the same element definition.");
                

            if (foundIdentityConstraint)
                error("A 'complexType' must appear before 'key', "+
                    "'keyref' and 'unique' elements.");

            foundComplexType = true;
            unmarshaller
                = new ComplexTypeUnmarshaller(_schema, atts, getResolver());
        }
        else if (SchemaNames.SIMPLE_TYPE.equals(name)) {

            if (foundSimpleType)
                error("Only one (1) 'simpleType' may appear in an "+
                    "element definition.");
            if (foundComplexType)
                error("Both 'simpleType' and 'complexType' cannot appear "+
                    "in the same element definition.");
            if (foundTypeReference)
                error("Both 'type' attribute and 'simpleType' element "+
                    "cannot appear in the same element definition.");

            if (foundIdentityConstraint)
                error("A 'simpleType' must appear before 'key', "+
                    "'keyref' and 'unique' elements.");

            foundSimpleType = true;
            unmarshaller = new SimpleTypeUnmarshaller(_schema, atts);
        }
        else if (SchemaNames.KEY.equals(name) || 
                 SchemaNames.KEYREF.equals(name) ||
                 SchemaNames.UNIQUE.equals(name)) 
        {
            foundIdentityConstraint = true;
            unmarshaller = new IdentityConstraintUnmarshaller(name, atts);
        }
        else illegalElement(name);

        unmarshaller.setResolver(getResolver());
        unmarshaller.setDocumentLocator(getDocumentLocator());

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
        if ((unmarshaller != null) && (charUnmarshaller != unmarshaller)) {
            if (!name.equals(unmarshaller.elementName())) {
                String err = "missing end element for ";
                err += unmarshaller.elementName();
                throw new SchemaException(err);
            }
        }

        //-- call finish for any necessary cleanup
        unmarshaller.finish();

        if (SchemaNames.ANNOTATION.equals(name)) {
            Annotation ann = (Annotation)unmarshaller.getObject();
            _element.addAnnotation(ann);
        }
        else if (SchemaNames.COMPLEX_TYPE.equals(name)) {

            XMLType xmlType
                = ((ComplexTypeUnmarshaller)unmarshaller).getComplexType();

            _element.setType(xmlType);

        }
        else if (SchemaNames.SIMPLE_TYPE.equals(name)) {
            XMLType xmlType
                = ((SimpleTypeUnmarshaller)unmarshaller).getSimpleType();
            _element.setType(xmlType);
        }
        else if (SchemaNames.KEY.equals(name) || 
                 SchemaNames.KEYREF.equals(name) ||
                 SchemaNames.UNIQUE.equals(name)) 
        {
            IdentityConstraint constraint 
                = (IdentityConstraint) unmarshaller.getObject();
            _element.addIdentityConstraint(constraint);
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

    /**
     * Makes sure only minOccurs, maxOccurs, id, and ref occur
     * for element references.
     *
     * @param atts the AttributeSet to process
     */
    private static void validateRefAtts(AttributeSet atts) 
        throws XMLException
    {
        
        StringBuffer errors = null;
        
        for (int i = 0; i < atts.getSize(); i++) {
            String name = atts.getName(i);
            if (SchemaNames.REF_ATTR.equals(name)) 
                continue;
            else if (SchemaNames.MAX_OCCURS_ATTR.equals(name)) 
                continue;
            else if (SchemaNames.MIN_OCCURS_ATTR.equals(name)) 
                continue;
            else if (SchemaNames.ID_ATTR.equals(name)) 
                continue;
            else {
                //-- check namespace
                String namespace = atts.getNamespace(i);
                
                //-- If we have no namespace (ie no prefix) or we
                //-- have the XSD Namespace then throw error
                if ((namespace == null) || 
                    (namespace.length() == 0) ||
                     namespace.equals(SchemaUnmarshaller.XSD_NAMESPACE))
                {
                    //-- unprefixed attribute...assume XML Schema namespace
                    String error = "The attribute '" + name + 
                        "' must not appear on an element reference.";
                    if (errors == null)
                        errors = new StringBuffer(error);
                    else
                        errors.append(error);
                        
                    errors.append(System.getProperty("line.separator"));
                    
                    
                }
                //-- otherwise we have a namespaced attribute from a different
                //-- namespace..this is valid...continue
            }
        }
        
        if (errors != null)
            throw new XMLException(errors.toString());
            
    } //-- validateRefAtts

} //-- ElementUnmarshaller
