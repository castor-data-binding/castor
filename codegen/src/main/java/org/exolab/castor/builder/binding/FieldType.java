/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.1-M2</a>, using an XML
 * Schema.
 * $Id$
 */

package org.exolab.castor.builder.binding;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

/**
 * This type represents the binding for class member. It allows the
 * definition
 *  of its name and java type as well as an implementation of
 * FieldHandler 
 *  to help the Marshalling framework in handling that member.
 * Defining a validator is also
 *  possible. The names given for the validator and the
 * fieldHandler must be fully qualified
 *  
 * 
 * @version $Revision$ $Date$
 */
public class FieldType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _name.
     */
    private java.lang.String _name;

    /**
     * Field _javaType.
     */
    private java.lang.String _javaType;

    /**
     * Field _wrapper.
     */
    private boolean _wrapper;

    /**
     * keeps track of state for field: _wrapper
     */
    private boolean _has_wrapper;

    /**
     * Field _handler.
     */
    private java.lang.String _handler;

    /**
     * Field _collection.
     */
    private org.exolab.castor.builder.binding.types.FieldTypeCollectionType _collection;

    /**
     * Field _visibility.
     */
    private org.exolab.castor.builder.binding.types.FieldTypeVisibilityType _visibility;

    /**
     * Field _validator.
     */
    private java.lang.String _validator;


      //----------------/
     //- Constructors -/
    //----------------/

    public FieldType() {
        super();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     */
    public void deleteWrapper(
    ) {
        this._has_wrapper= false;
    }

    /**
     * Returns the value of field 'collection'.
     * 
     * @return the value of field 'Collection'.
     */
    public org.exolab.castor.builder.binding.types.FieldTypeCollectionType getCollection(
    ) {
        return this._collection;
    }

    /**
     * Returns the value of field 'handler'.
     * 
     * @return the value of field 'Handler'.
     */
    public java.lang.String getHandler(
    ) {
        return this._handler;
    }

    /**
     * Returns the value of field 'javaType'.
     * 
     * @return the value of field 'JavaType'.
     */
    public java.lang.String getJavaType(
    ) {
        return this._javaType;
    }

    /**
     * Returns the value of field 'name'.
     * 
     * @return the value of field 'Name'.
     */
    public java.lang.String getName(
    ) {
        return this._name;
    }

    /**
     * Returns the value of field 'validator'.
     * 
     * @return the value of field 'Validator'.
     */
    public java.lang.String getValidator(
    ) {
        return this._validator;
    }

    /**
     * Returns the value of field 'visibility'.
     * 
     * @return the value of field 'Visibility'.
     */
    public org.exolab.castor.builder.binding.types.FieldTypeVisibilityType getVisibility(
    ) {
        return this._visibility;
    }

    /**
     * Returns the value of field 'wrapper'.
     * 
     * @return the value of field 'Wrapper'.
     */
    public boolean getWrapper(
    ) {
        return this._wrapper;
    }

    /**
     * Method hasWrapper.
     * 
     * @return true if at least one Wrapper has been added
     */
    public boolean hasWrapper(
    ) {
        return this._has_wrapper;
    }

    /**
     * Method isValid.
     * 
     * @return true if this object is valid according to the schema
     */
    public boolean isValid(
    ) {
        try {
            validate();
        } catch (org.exolab.castor.xml.ValidationException vex) {
            return false;
        }
        return true;
    }

    /**
     * Returns the value of field 'wrapper'.
     * 
     * @return the value of field 'Wrapper'.
     */
    public boolean isWrapper(
    ) {
        return this._wrapper;
    }

    /**
     * 
     * 
     * @param out
     * @throws org.exolab.castor.xml.MarshalException if object is
     * null or if any SAXException is thrown during marshaling
     * @throws org.exolab.castor.xml.ValidationException if this
     * object is an invalid instance according to the schema
     */
    public void marshal(
            final java.io.Writer out)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        Marshaller.marshal(this, out);
    }

    /**
     * 
     * 
     * @param handler
     * @throws java.io.IOException if an IOException occurs during
     * marshaling
     * @throws org.exolab.castor.xml.ValidationException if this
     * object is an invalid instance according to the schema
     * @throws org.exolab.castor.xml.MarshalException if object is
     * null or if any SAXException is thrown during marshaling
     */
    public void marshal(
            final org.xml.sax.ContentHandler handler)
    throws java.io.IOException, org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        Marshaller.marshal(this, handler);
    }

    /**
     * Sets the value of field 'collection'.
     * 
     * @param collection the value of field 'collection'.
     */
    public void setCollection(
            final org.exolab.castor.builder.binding.types.FieldTypeCollectionType collection) {
        this._collection = collection;
    }

    /**
     * Sets the value of field 'handler'.
     * 
     * @param handler the value of field 'handler'.
     */
    public void setHandler(
            final java.lang.String handler) {
        this._handler = handler;
    }

    /**
     * Sets the value of field 'javaType'.
     * 
     * @param javaType the value of field 'javaType'.
     */
    public void setJavaType(
            final java.lang.String javaType) {
        this._javaType = javaType;
    }

    /**
     * Sets the value of field 'name'.
     * 
     * @param name the value of field 'name'.
     */
    public void setName(
            final java.lang.String name) {
        this._name = name;
    }

    /**
     * Sets the value of field 'validator'.
     * 
     * @param validator the value of field 'validator'.
     */
    public void setValidator(
            final java.lang.String validator) {
        this._validator = validator;
    }

    /**
     * Sets the value of field 'visibility'.
     * 
     * @param visibility the value of field 'visibility'.
     */
    public void setVisibility(
            final org.exolab.castor.builder.binding.types.FieldTypeVisibilityType visibility) {
        this._visibility = visibility;
    }

    /**
     * Sets the value of field 'wrapper'.
     * 
     * @param wrapper the value of field 'wrapper'.
     */
    public void setWrapper(
            final boolean wrapper) {
        this._wrapper = wrapper;
        this._has_wrapper = true;
    }

    /**
     * Method unmarshalFieldType.
     * 
     * @param reader
     * @throws org.exolab.castor.xml.MarshalException if object is
     * null or if any SAXException is thrown during marshaling
     * @throws org.exolab.castor.xml.ValidationException if this
     * object is an invalid instance according to the schema
     * @return the unmarshaled
     * org.exolab.castor.builder.binding.FieldType
     */
    public static org.exolab.castor.builder.binding.FieldType unmarshalFieldType(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (org.exolab.castor.builder.binding.FieldType) Unmarshaller.unmarshal(org.exolab.castor.builder.binding.FieldType.class, reader);
    }

    /**
     * 
     * 
     * @throws org.exolab.castor.xml.ValidationException if this
     * object is an invalid instance according to the schema
     */
    public void validate(
    )
    throws org.exolab.castor.xml.ValidationException {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    }

}
