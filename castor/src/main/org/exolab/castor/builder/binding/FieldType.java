/*
 * This class was automatically generated with 
 * <a href="http://castor.exolab.org">Castor 0.9.4</a>, using an
 * XML Schema.
 * $Id$
 */

package org.exolab.castor.builder.binding;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import org.exolab.castor.builder.binding.types.FieldTypeCollectionType;
import org.exolab.castor.xml.*;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.ContentHandler;

/**
 * 
 *                 This type represents the binding for class
 * member. It allows the definition
 *                 of its name and java type as well as an
 * implementation of FieldHandler 
 *                 to help the Marshalling framework in handling
 * that member. Defining a validator is also
 *                 possible. The names given for the validator and
 * the fieldHandler must be fully qualified
 *             
 * 
 * @version $Revision$ $Date$
**/
public class FieldType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    private java.lang.String _name;

    private java.lang.String _javaType;

    private boolean _wrapper;

    /**
     * keeps track of state for field: _wrapper
    **/
    private boolean _has_wrapper;

    private java.lang.String _handler;

    private org.exolab.castor.builder.binding.types.FieldTypeCollectionType _collection;

    private java.lang.String _validator;


      //----------------/
     //- Constructors -/
    //----------------/

    public FieldType() {
        super();
    } //-- org.exolab.castor.builder.binding.FieldType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
    **/
    public void deleteWrapper()
    {
        this._has_wrapper= false;
    } //-- void deleteWrapper() 

    /**
     * Returns the value of field 'collection'.
     * 
     * @return the value of field 'collection'.
    **/
    public org.exolab.castor.builder.binding.types.FieldTypeCollectionType getCollection()
    {
        return this._collection;
    } //-- org.exolab.castor.builder.binding.types.FieldTypeCollectionType getCollection() 

    /**
     * Returns the value of field 'handler'.
     * 
     * @return the value of field 'handler'.
    **/
    public java.lang.String getHandler()
    {
        return this._handler;
    } //-- java.lang.String getHandler() 

    /**
     * Returns the value of field 'javaType'.
     * 
     * @return the value of field 'javaType'.
    **/
    public java.lang.String getJavaType()
    {
        return this._javaType;
    } //-- java.lang.String getJavaType() 

    /**
     * Returns the value of field 'name'.
     * 
     * @return the value of field 'name'.
    **/
    public java.lang.String getName()
    {
        return this._name;
    } //-- java.lang.String getName() 

    /**
     * Returns the value of field 'validator'.
     * 
     * @return the value of field 'validator'.
    **/
    public java.lang.String getValidator()
    {
        return this._validator;
    } //-- java.lang.String getValidator() 

    /**
     * Returns the value of field 'wrapper'.
     * 
     * @return the value of field 'wrapper'.
    **/
    public boolean getWrapper()
    {
        return this._wrapper;
    } //-- boolean getWrapper() 

    /**
    **/
    public boolean hasWrapper()
    {
        return this._has_wrapper;
    } //-- boolean hasWrapper() 

    /**
    **/
    public boolean isValid()
    {
        try {
            validate();
        }
        catch (org.exolab.castor.xml.ValidationException vex) {
            return false;
        }
        return true;
    } //-- boolean isValid() 

    /**
     * 
     * 
     * @param out
    **/
    public void marshal(java.io.Writer out)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        
        Marshaller.marshal(this, out);
    } //-- void marshal(java.io.Writer) 

    /**
     * 
     * 
     * @param handler
    **/
    public void marshal(org.xml.sax.ContentHandler handler)
        throws java.io.IOException, org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        
        Marshaller.marshal(this, handler);
    } //-- void marshal(org.xml.sax.ContentHandler) 

    /**
     * Sets the value of field 'collection'.
     * 
     * @param collection the value of field 'collection'.
    **/
    public void setCollection(org.exolab.castor.builder.binding.types.FieldTypeCollectionType collection)
    {
        this._collection = collection;
    } //-- void setCollection(org.exolab.castor.builder.binding.types.FieldTypeCollectionType) 

    /**
     * Sets the value of field 'handler'.
     * 
     * @param handler the value of field 'handler'.
    **/
    public void setHandler(java.lang.String handler)
    {
        this._handler = handler;
    } //-- void setHandler(java.lang.String) 

    /**
     * Sets the value of field 'javaType'.
     * 
     * @param javaType the value of field 'javaType'.
    **/
    public void setJavaType(java.lang.String javaType)
    {
        this._javaType = javaType;
    } //-- void setJavaType(java.lang.String) 

    /**
     * Sets the value of field 'name'.
     * 
     * @param name the value of field 'name'.
    **/
    public void setName(java.lang.String name)
    {
        this._name = name;
    } //-- void setName(java.lang.String) 

    /**
     * Sets the value of field 'validator'.
     * 
     * @param validator the value of field 'validator'.
    **/
    public void setValidator(java.lang.String validator)
    {
        this._validator = validator;
    } //-- void setValidator(java.lang.String) 

    /**
     * Sets the value of field 'wrapper'.
     * 
     * @param wrapper the value of field 'wrapper'.
    **/
    public void setWrapper(boolean wrapper)
    {
        this._wrapper = wrapper;
        this._has_wrapper = true;
    } //-- void setWrapper(boolean) 

    /**
     * 
     * 
     * @param reader
    **/
    public static org.exolab.castor.builder.binding.FieldType unmarshalFieldType(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.exolab.castor.builder.binding.FieldType) Unmarshaller.unmarshal(org.exolab.castor.builder.binding.FieldType.class, reader);
    } //-- org.exolab.castor.builder.binding.FieldType unmarshalFieldType(java.io.Reader) 

    /**
    **/
    public void validate()
        throws org.exolab.castor.xml.ValidationException
    {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    } //-- void validate() 

}
