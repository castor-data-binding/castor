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


package org.exolab.castor.xml.schema;

import org.exolab.castor.xml.*;

import java.util.Vector;

/**
 * An XML Schema Attribute Definition
 * @author <a href="mailto:kvisco@intalio.com">Keith Visco</a>
 * @version $Revision$ $Date$
**/
public final class AttributeDecl extends Annotated {


    /**
     * The use attribute value for optional
    **/
    public static final String USE_OPTIONAL = "optional";

    /**
     * The use attribute value for prohibited
    **/
    public static final String USE_PROHIBITED = "prohibited";

    /**
     * The use attribute value for required
    **/
    public static final String USE_REQUIRED = "required";


    private static final short OPTIONAL   = 3;
    private static final short PROHIBITED = 4;
    private static final short REQUIRED   = 5;

    /**
     * Error message for a null argument
    **/
    private static String NULL_ARGUMENT
        = "A null argument was passed to the constructor of " +
           AttributeDecl.class.getName();

    /**
     * A flag to indicate a default value
    **/
    private boolean _default = false;

    /**
     * A flag to indicate a fixed value
    **/
    private boolean _fixed = false;

    /**
     * The default namespace form for this AttributeDecl (optional).
    **/
    private Form _form = null;

    /**
     * The id for this AttributeDecl
    **/
    private String _id = null;

    /**
     * The name of attributes defined by this AttributeDecl
    **/
    private String _name = null;

    /**
     * The Schema to which this AttributeDecl belongs
    **/
    private Schema _schema = null;

    /**
     * The simple type for this AttributeDecl.
    **/
    private SimpleType _simpleType = null;


    /**
     * The current value of the 'use' property. The value
     * is OPTIONAL by default.
    **/
    private short _useFlag = OPTIONAL;

    /**
     * The default or fixed value for attributes instances of this
     * attribute declaration.
    **/
    private String _value = null;


    /**
     * A reference to a top-level attribute
     */
    private String _attributeRef = null;

    /**
     * Creates a new AttrDecl with the given name
     * @param name of the Attribute defined by this attribute declaration
     * @param schema the schema that contains the new attrDecl
    **/
    public AttributeDecl(Schema schema, String name) {

        if (schema == null) {
            String err = NULL_ARGUMENT + "; 'schema' must not be null.";
            throw new IllegalArgumentException(err);
        }
        _schema  = schema;
        setName(name);
    } //-- AttributeDecl

    /**
     * Creates a new AttrDecl in the given schema.
     * @param schema the schema that contains the new attrDecl
    **/
    public AttributeDecl(Schema schema) {

        if (schema == null) {
            String err = NULL_ARGUMENT + "; 'schema' must not be null.";
            throw new IllegalArgumentException(err);
        }
        _schema  = schema;
    } //-- AttributeDecl


    /**
     * Returns the Form for this attribute declaration. The Form object species
     * whether or not names are qualified or unqualified for instances of
     * this attribute declaration. If null, the Form should be obtained from the
     * parent Schema.
     *
     * @return the Form for this attribute declaration, or null if not set.
    **/
    public Form getForm() {
        return _form;
    } //-- getForm

    /**
     * Returns the Id for this attribute declaration
     *
     * @return the Id for this attribute declaration
    **/
    public String getId() {
        return _id;
    } //-- getId

    /**
     * Returns the name of attributes defined by this AttributeDecl.
     *
     * @return the name of attributes defined by this AttributeDecl.
    **/
    public String getName() {
        return getName(false);
    } //-- getName

    /**
     * Returns the name of this Attribute declaration.
     *
     * @param ingoreRef If True the name of the referenced
     * attribute (if specified) is returned
     * @return the name of this attribute declaration
    **/
    public String getName(boolean ignoreRef) {
        if (isReference() && ignoreRef == false) {
            return _attributeRef;
        }
		else return _name;
    } //-- getName

    /**
     * Returns the data type associated with this AttributeDecl.
     *
     * @return the data type associated with this AttributeDecl.
    **/
    public SimpleType getSimpleType() {
        if (_simpleType == null)
            return null;
        return (SimpleType)_simpleType.getType();
    } //-- getSimpleType

    /**
     * Returns the AttributeDecl that this attribute definition references.
     * This will return null if this attribute definition does not reference
     * a different attribute definition.
     * @return the AttributeDecl that this attribute definition references
    **/
    public AttributeDecl getReference() {
        if (_attributeRef != null)
            return _schema.getAttribute(_attributeRef);
        return null;
    } //-- getReference
    /**
     * Returns the Schema that this AttributeGroupDecl belongs to.
     *
     * @return the Schema that this AttributeGroupDecl belongs to.
    **/
    public Schema getSchema() {
        return _schema;
    } //-- getSchema

    /**
     * Returns the value of the use attribute for this attribute
     * declaration.
     *
     * @return the value of the use attribute for this attribute
     * declaration
    **/
    public String getUse() {
        switch (_useFlag) {
            case PROHIBITED:
                return USE_PROHIBITED;
            case REQUIRED:
                return USE_REQUIRED;
            default:
                return USE_OPTIONAL;
        }
    } //-- getUse

    /**
     * Returns the default (or fixed) value of this Attribute declaration
     *
     * @return the default value of this attribute declaration
    **/
    public String getValue() {
        return _value;
    } //-- getValue

    /**
     * Returns true if the "default" flag is set.
     *
     * @return true if the "default" flag is set.
     */
    public boolean isDefault() {
        return (_default);
    } //-- isFixed


    /**
     * Returns true if the use attribute is equal to "optional".
     *
     * @return true if the use attribute is equal to "optional".
    **/
    public boolean isFixed() {
        return (_fixed);
    } //-- isFixed

    /**
     * Returns true if the use attribute is equal to "optional".
     *
     * @return true if the use attribute is equal to "optional".
    **/
    public boolean isOptional() {
        return (_useFlag == OPTIONAL);
    } //-- isProhibited

    /**
     * Returns true if the use attribute is equal to "prohibited".
     *
     * @return true if the use attribute is equal to "prohibited".
    **/
    public boolean isProhibited() {
        return (_useFlag == PROHIBITED);
    } //-- isProhibited

    /**
     * Returns true if the 'use' attribute is equal to REQUIRED and
     * there is no specified value. If a value is specifed and the
     * 'use' attribute is  "required" then required is will return
     * false, because the attribute value automatically becomes
     * fixed.
     *
     * @return true if the use attribute is equal to "required" and
     * no default value has been specified, otherwise false
    **/
    public boolean isRequired() {
        return ((_value == null) && (_useFlag == REQUIRED));
    } //-- getRequired

    /**
     * Returns true if this attribute definition simply references another
     * attribute Definition
     * @return true if this attribute definition is a reference
     */
    public boolean isReference() {
        return (_attributeRef != null);
    } //-- isReference

    /**
     * Sets the Form for this attribute declaration. The Form object species
     * whether or not names are qualified or unqualified for instances of
     * this attribute declaration. If null, the Form is to be obtained from
     * the parent Schema.
     *
     * @param form the Form type for this attribute declaration.
    **/
    public void setForm(Form form) {
        _form = form;
    } //-- setForm

    /**
     * Sets the Id for this attribute declaration
     *
     * @param id the Id for this attribute declaration
    **/
    public void setId(String id) {
        _id = id;
    } //-- setId

    /**
     * Sets the name of attributes defined by this attribute definition
     * @param name the name of the this AttributeDecl
     * @exception IllegalArgumentException when the name is not valid
    **/
    public void setName(String name) {
        if (name == null) {
            String err = "AttributeDecl#setName: 'name' must not be null.";
            throw new IllegalArgumentException(err);
        }

        //-- handle namespace if necessary
        int idx = name.indexOf(':');
        if (idx >= 0) {
            String nsPrefix = name.substring(0,idx);
            //-- we should resolve nsPrefix...just ignore for now

            //-- use local name
            name = name.substring(idx+1);
        }

        if (name.length() == 0) {
            String err = "AttributeDecl#setName: 'name' must not be "+
                "zero-length.";
            throw new IllegalArgumentException(err);
        }

        _name = name;
    } //-- setName

    /**
     * Sets the reference for this attribute definition
     * @param reference the Attribute definition that this definition references
    **/
    public void setReference(AttributeDecl reference) {
        if (reference == null)
            this._attributeRef = null;
        else
            this._attributeRef = reference.getName();
    } //-- setReference

    /**
     * Sets the reference for this attribute definition
     * @param reference the name of the attribute definition that this
     * definition references
    **/
    public void setReference(String reference) {
        this._attributeRef = reference;
    } //-- setReference
    /**
     * Sets the SimpleType for this attribute declaration
     * @param simpleType the SimpleType for this attribute
     * declaration
    **/
    public void setSimpleType(SimpleType simpleType) {
        _simpleType = simpleType;
        if (simpleType != null) {
            simpleType.setParent(this);
        }
    } //-- setSimpleType

    /**
     * Sets the simple type of this attribute to be a reference.
     *
     * @param name the name of the simpleType being referenced, must
     * not be null.
    **/
    public void setSimpleTypeReference(String name) {
        SimpleTypeReference reference
            = new SimpleTypeReference(_schema, name);
        setSimpleType(reference);
    } //-- setSimpleTypeReference


    /**
     * Sets the 'use' attribute of this attribute declaration
     * Note: this should not be used to set the flag to FIXED or DEFAULT
     * @param value one of the following:
     * ("prohibited" | "optional" | "required")
     *
     * @see #USE_PROHIBITED
     * @see #USE_OPTIONAL
     * @see #USE_REQUIRED
    **/
    public void setUse(String value) {

        if (value == null) {
            _useFlag = OPTIONAL;
            return;
        }

        if (value.equals(USE_REQUIRED))
            _useFlag = REQUIRED;
        else if (value.equals(USE_OPTIONAL))
            _useFlag = OPTIONAL;
        else if (value.equals(USE_PROHIBITED))
            _useFlag = PROHIBITED;
        else {
            throw new IllegalArgumentException("Invalid value for 'use': " +
                value);
        }
    } //-- setUse

    /**
     * Sets the DEFAULT flag to true
     */
    public void setDefault() {
        _default = true;
    }

    /**
     * Sets the FIXED flag to true.
     */
    public void setFixed() {
        _fixed = true;
    }

    /**
     * Sets the default value (or fixed depending on the 'use' option) for
     * this attribute declaration
     *
     * @param value the default value
    **/
    public void setValue(String value) {
        _value = value;
    } //-- setValue



    //-------------------------------/
    //- Implementation of Structure -/
    //-------------------------------/

    /**
     * Returns the type of this Schema Structure
     * @return the type of this Schema Structure
    **/
    public short getStructureType() {
        return Structure.ATTRIBUTE;
    } //-- getStructureType

    /**
     * Checks the validity of this Attribute declaration
     * @exception ValidationException when this Attribute declaration
     * is invalid
    **/
    public void validate()
        throws ValidationException
    {
        if ((_attributeRef == null) && (_name == null))  {
            String err = "<attribute> is missing required 'name' attribute.";
            throw new ValidationException(err);
        }

    } //-- validate

} //-- AttrDecl
