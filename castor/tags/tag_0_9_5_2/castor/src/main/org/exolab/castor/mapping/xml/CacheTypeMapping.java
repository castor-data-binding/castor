/*
 * This class was automatically generated with 
 * <a href="http://castor.exolab.org">Castor 0.8.12</a>, using an
 * XML Schema.
 * $Id$
 */

package org.exolab.castor.mapping.xml;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import org.exolab.castor.mapping.xml.types.TypeType;
import org.exolab.castor.xml.*;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.DocumentHandler;

/**
 * 
 * @version $Revision$ $Date$
**/
public class CacheTypeMapping implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    private org.exolab.castor.mapping.xml.types.TypeType _type = org.exolab.castor.mapping.xml.types.TypeType.valueOf("count-limited");;

    private int _capacity;

    /**
     * keeps track of state for field: _capacity
    **/
    private boolean _has_capacity;


      //----------------/
     //- Constructors -/
    //----------------/

    public CacheTypeMapping() {
        super();
    } //-- org.exolab.castor.mapping.xml.CacheTypeMapping()


      //-----------/
     //- Methods -/
    //-----------/

    /**
    **/
    public void deleteCapacity()
    {
        this._has_capacity= false;
    } //-- void deleteCapacity() 

    /**
    **/
    public int getCapacity()
    {
        return this._capacity;
    } //-- int getCapacity() 

    /**
    **/
    public org.exolab.castor.mapping.xml.types.TypeType getType()
    {
        return this._type;
    } //-- org.exolab.castor.mapping.xml.types.TypeType getType() 

    /**
    **/
    public boolean hasCapacity()
    {
        return this._has_capacity;
    } //-- boolean hasCapacity() 

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
     * @param out
    **/
    public void marshal(java.io.Writer out)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        
        Marshaller.marshal(this, out);
    } //-- void marshal(java.io.Writer) 

    /**
     * 
     * @param handler
    **/
    public void marshal(org.xml.sax.DocumentHandler handler)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        
        Marshaller.marshal(this, handler);
    } //-- void marshal(org.xml.sax.DocumentHandler) 

    /**
     * 
     * @param _capacity
    **/
    public void setCapacity(int _capacity)
    {
        this._capacity = _capacity;
        this._has_capacity = true;
    } //-- void setCapacity(int) 

    /**
     * 
     * @param _type
    **/
    public void setType(org.exolab.castor.mapping.xml.types.TypeType _type)
    {
        this._type = _type;
    } //-- void setType(org.exolab.castor.mapping.xml.types.TypeType) 

    /**
     * 
     * @param reader
    **/
    public static org.exolab.castor.mapping.xml.CacheTypeMapping unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.exolab.castor.mapping.xml.CacheTypeMapping) Unmarshaller.unmarshal(org.exolab.castor.mapping.xml.CacheTypeMapping.class, reader);
    } //-- org.exolab.castor.mapping.xml.CacheTypeMapping unmarshal(java.io.Reader) 

    /**
    **/
    public void validate()
        throws org.exolab.castor.xml.ValidationException
    {
        Validator validator = new Validator();
        validator.validate(this);
    } //-- void validate() 

}
