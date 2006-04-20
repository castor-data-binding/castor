/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.5.3</a>, using an XML
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
 * Class CacheTypeMapping.
 * 
 * @version $Revision$ $Date$
 */
public class CacheTypeMapping implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _type
     */
    private java.lang.String _type = "count-limited";

    /**
     * Field _capacity
     */
    private int _capacity;

    /**
     * keeps track of state for field: _capacity
     */
    private boolean _has_capacity;


      //----------------/
     //- Constructors -/
    //----------------/

    public CacheTypeMapping() {
        super();
        setType("count-limited");
    } //-- org.exolab.castor.mapping.xml.CacheTypeMapping()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method deleteCapacity
     */
    public void deleteCapacity()
    {
        this._has_capacity= false;
    } //-- void deleteCapacity() 

    /**
     * Returns the value of field 'capacity'.
     * 
     * @return the value of field 'capacity'.
     */
    public int getCapacity()
    {
        return this._capacity;
    } //-- int getCapacity() 

    /**
     * Returns the value of field 'type'.
     * 
     * @return the value of field 'type'.
     */
    public java.lang.String getType()
    {
        return this._type;
    } //-- java.lang.String getType() 

    /**
     * Method hasCapacity
     */
    public boolean hasCapacity()
    {
        return this._has_capacity;
    } //-- boolean hasCapacity() 

    /**
     * Method isValid
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
     * @param handler
     */
    public void marshal(org.xml.sax.ContentHandler handler)
        throws java.io.IOException, org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        
        Marshaller.marshal(this, handler);
    } //-- void marshal(org.xml.sax.ContentHandler) 

    /**
     * Sets the value of field 'capacity'.
     * 
     * @param capacity the value of field 'capacity'.
     */
    public void setCapacity(int capacity)
    {
        this._capacity = capacity;
        this._has_capacity = true;
    } //-- void setCapacity(int) 

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
     * @param reader
     */
    public static java.lang.Object unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.exolab.castor.mapping.xml.CacheTypeMapping) Unmarshaller.unmarshal(org.exolab.castor.mapping.xml.CacheTypeMapping.class, reader);
    } //-- java.lang.Object unmarshal(java.io.Reader) 

    /**
     * Method validate
     */
    public void validate()
        throws org.exolab.castor.xml.ValidationException
    {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    } //-- void validate() 

}
