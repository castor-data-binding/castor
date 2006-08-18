/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.0.2</a>, using an XML
 * Schema.
 * $Id$
 */

package org.exolab.castor.mapping.xml;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.ContentHandler;

/**
 * Class Container.
 * 
 * @version $Revision$ $Date: 2006-01-07 15:48:31 -0700 (Sat, 07 Jan 2006) $
 */
public class Container implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _name
     */
    private java.lang.String _name;

    /**
     * Field _type
     */
    private java.lang.String _type;

    /**
     * Field _required
     */
    private boolean _required = false;

    /**
     * keeps track of state for field: _required
     */
    private boolean _has_required;

    /**
     * Field _direct
     */
    private boolean _direct = false;

    /**
     * keeps track of state for field: _direct
     */
    private boolean _has_direct;

    /**
     * Field _getMethod
     */
    private java.lang.String _getMethod;

    /**
     * Field _setMethod
     */
    private java.lang.String _setMethod;

    /**
     * Field _createMethod
     */
    private java.lang.String _createMethod;

    /**
     * Field _description
     */
    private java.lang.String _description;

    /**
     * Field _fieldMapping
     */
    private org.exolab.castor.mapping.xml.FieldMapping _fieldMapping;


      //----------------/
     //- Constructors -/
    //----------------/

    public Container() 
     {
        super();
    } //-- org.exolab.castor.mapping.xml.Container()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method deleteDirect
     * 
     */
    public void deleteDirect()
    {
        this._has_direct= false;
    } //-- void deleteDirect() 

    /**
     * Method deleteRequired
     * 
     */
    public void deleteRequired()
    {
        this._has_required= false;
    } //-- void deleteRequired() 

    /**
     * Returns the value of field 'createMethod'.
     * 
     * @return String
     * @return the value of field 'createMethod'.
     */
    public java.lang.String getCreateMethod()
    {
        return this._createMethod;
    } //-- java.lang.String getCreateMethod() 

    /**
     * Returns the value of field 'description'.
     * 
     * @return String
     * @return the value of field 'description'.
     */
    public java.lang.String getDescription()
    {
        return this._description;
    } //-- java.lang.String getDescription() 

    /**
     * Returns the value of field 'direct'.
     * 
     * @return boolean
     * @return the value of field 'direct'.
     */
    public boolean getDirect()
    {
        return this._direct;
    } //-- boolean getDirect() 

    /**
     * Returns the value of field 'fieldMapping'.
     * 
     * @return FieldMapping
     * @return the value of field 'fieldMapping'.
     */
    public org.exolab.castor.mapping.xml.FieldMapping getFieldMapping()
    {
        return this._fieldMapping;
    } //-- org.exolab.castor.mapping.xml.FieldMapping getFieldMapping() 

    /**
     * Returns the value of field 'getMethod'.
     * 
     * @return String
     * @return the value of field 'getMethod'.
     */
    public java.lang.String getGetMethod()
    {
        return this._getMethod;
    } //-- java.lang.String getGetMethod() 

    /**
     * Returns the value of field 'name'.
     * 
     * @return String
     * @return the value of field 'name'.
     */
    public java.lang.String getName()
    {
        return this._name;
    } //-- java.lang.String getName() 

    /**
     * Returns the value of field 'required'.
     * 
     * @return boolean
     * @return the value of field 'required'.
     */
    public boolean getRequired()
    {
        return this._required;
    } //-- boolean getRequired() 

    /**
     * Returns the value of field 'setMethod'.
     * 
     * @return String
     * @return the value of field 'setMethod'.
     */
    public java.lang.String getSetMethod()
    {
        return this._setMethod;
    } //-- java.lang.String getSetMethod() 

    /**
     * Returns the value of field 'type'.
     * 
     * @return String
     * @return the value of field 'type'.
     */
    public java.lang.String getType()
    {
        return this._type;
    } //-- java.lang.String getType() 

    /**
     * Method hasDirect
     * 
     * 
     * 
     * @return boolean
     */
    public boolean hasDirect()
    {
        return this._has_direct;
    } //-- boolean hasDirect() 

    /**
     * Method hasRequired
     * 
     * 
     * 
     * @return boolean
     */
    public boolean hasRequired()
    {
        return this._has_required;
    } //-- boolean hasRequired() 

    /**
     * Method isValid
     * 
     * 
     * 
     * @return boolean
     */
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
     * Method marshal
     * 
     * 
     * 
     * @param out
     */
    public void marshal(java.io.Writer out)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        
        Marshaller.marshal(this, out);
    } //-- void marshal(java.io.Writer) 

    /**
     * Method marshal
     * 
     * 
     * 
     * @param handler
     */
    public void marshal(org.xml.sax.ContentHandler handler)
        throws java.io.IOException, org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        
        Marshaller.marshal(this, handler);
    } //-- void marshal(org.xml.sax.ContentHandler) 

    /**
     * Sets the value of field 'createMethod'.
     * 
     * @param createMethod the value of field 'createMethod'.
     */
    public void setCreateMethod(java.lang.String createMethod)
    {
        this._createMethod = createMethod;
    } //-- void setCreateMethod(java.lang.String) 

    /**
     * Sets the value of field 'description'.
     * 
     * @param description the value of field 'description'.
     */
    public void setDescription(java.lang.String description)
    {
        this._description = description;
    } //-- void setDescription(java.lang.String) 

    /**
     * Sets the value of field 'direct'.
     * 
     * @param direct the value of field 'direct'.
     */
    public void setDirect(boolean direct)
    {
        this._direct = direct;
        this._has_direct = true;
    } //-- void setDirect(boolean) 

    /**
     * Sets the value of field 'fieldMapping'.
     * 
     * @param fieldMapping the value of field 'fieldMapping'.
     */
    public void setFieldMapping(org.exolab.castor.mapping.xml.FieldMapping fieldMapping)
    {
        this._fieldMapping = fieldMapping;
    } //-- void setFieldMapping(org.exolab.castor.mapping.xml.FieldMapping) 

    /**
     * Sets the value of field 'getMethod'.
     * 
     * @param getMethod the value of field 'getMethod'.
     */
    public void setGetMethod(java.lang.String getMethod)
    {
        this._getMethod = getMethod;
    } //-- void setGetMethod(java.lang.String) 

    /**
     * Sets the value of field 'name'.
     * 
     * @param name the value of field 'name'.
     */
    public void setName(java.lang.String name)
    {
        this._name = name;
    } //-- void setName(java.lang.String) 

    /**
     * Sets the value of field 'required'.
     * 
     * @param required the value of field 'required'.
     */
    public void setRequired(boolean required)
    {
        this._required = required;
        this._has_required = true;
    } //-- void setRequired(boolean) 

    /**
     * Sets the value of field 'setMethod'.
     * 
     * @param setMethod the value of field 'setMethod'.
     */
    public void setSetMethod(java.lang.String setMethod)
    {
        this._setMethod = setMethod;
    } //-- void setSetMethod(java.lang.String) 

    /**
     * Sets the value of field 'type'.
     * 
     * @param type the value of field 'type'.
     */
    public void setType(java.lang.String type)
    {
        this._type = type;
    } //-- void setType(java.lang.String) 

    /**
     * Method unmarshal
     * 
     * 
     * 
     * @param reader
     * @return Container
     */
    public static org.exolab.castor.mapping.xml.Container unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.exolab.castor.mapping.xml.Container) Unmarshaller.unmarshal(org.exolab.castor.mapping.xml.Container.class, reader);
    } //-- org.exolab.castor.mapping.xml.Container unmarshal(java.io.Reader) 

    /**
     * Method validate
     * 
     */
    public void validate()
        throws org.exolab.castor.xml.ValidationException
    {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    } //-- void validate() 

}
