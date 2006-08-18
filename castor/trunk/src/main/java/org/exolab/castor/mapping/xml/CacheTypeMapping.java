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

import java.util.Collections;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

/**
 * Class CacheTypeMapping.
 * 
 * @version $Revision$ $Date: 2006-01-07 15:48:31 -0700 (Sat, 07 Jan 2006) $
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
     * Field _debug
     */
    private boolean _debug = false;

    /**
     * keeps track of state for field: _debug
     */
    private boolean _has_debug;

    /**
     * Field _capacity
     */
    private int _capacity;

    /**
     * keeps track of state for field: _capacity
     */
    private boolean _has_capacity;

    /**
     * Field _paramList
     */
    private java.util.ArrayList _paramList;


      //----------------/
     //- Constructors -/
    //----------------/

    public CacheTypeMapping() 
     {
        super();
        setType("count-limited");
        _paramList = new java.util.ArrayList();
    } //-- org.exolab.castor.mapping.xml.CacheTypeMapping()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addParam
     * 
     * 
     * 
     * @param vParam
     */
    public void addParam(org.exolab.castor.mapping.xml.Param vParam)
        throws java.lang.IndexOutOfBoundsException
    {
        _paramList.add(vParam);
    } //-- void addParam(org.exolab.castor.mapping.xml.Param) 

    /**
     * Method addParam
     * 
     * 
     * 
     * @param index
     * @param vParam
     */
    public void addParam(int index, org.exolab.castor.mapping.xml.Param vParam)
        throws java.lang.IndexOutOfBoundsException
    {
        _paramList.add(index, vParam);
    } //-- void addParam(int, org.exolab.castor.mapping.xml.Param) 

    /**
     * Method clearParam
     * 
     */
    public void clearParam()
    {
        _paramList.clear();
    } //-- void clearParam() 

    /**
     * Method deleteCapacity
     * 
     */
    public void deleteCapacity()
    {
        this._has_capacity= false;
    } //-- void deleteCapacity() 

    /**
     * Method deleteDebug
     * 
     */
    public void deleteDebug()
    {
        this._has_debug= false;
    } //-- void deleteDebug() 

    /**
     * Method enumerateParam
     * 
     * 
     * 
     * @return Enumeration
     */
    public java.util.Enumeration enumerateParam()
    {
        return Collections.enumeration(_paramList);
    } //-- java.util.Enumeration enumerateParam() 

    /**
     * Returns the value of field 'capacity'.
     * 
     * @return int
     * @return the value of field 'capacity'.
     */
    public int getCapacity()
    {
        return this._capacity;
    } //-- int getCapacity() 

    /**
     * Returns the value of field 'debug'.
     * 
     * @return boolean
     * @return the value of field 'debug'.
     */
    public boolean getDebug()
    {
        return this._debug;
    } //-- boolean getDebug() 

    /**
     * Method getParam
     * 
     * 
     * 
     * @param index
     * @return Param
     */
    public org.exolab.castor.mapping.xml.Param getParam(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index >= _paramList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (org.exolab.castor.mapping.xml.Param) _paramList.get(index);
    } //-- org.exolab.castor.mapping.xml.Param getParam(int) 

    /**
     * Method getParam
     * 
     * 
     * 
     * @return Param
     */
    public org.exolab.castor.mapping.xml.Param[] getParam()
    {
        int size = _paramList.size();
        org.exolab.castor.mapping.xml.Param[] mArray = new org.exolab.castor.mapping.xml.Param[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (org.exolab.castor.mapping.xml.Param) _paramList.get(index);
        }
        return mArray;
    } //-- org.exolab.castor.mapping.xml.Param[] getParam() 

    /**
     * Method getParamCount
     * 
     * 
     * 
     * @return int
     */
    public int getParamCount()
    {
        return _paramList.size();
    } //-- int getParamCount() 

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
     * Method hasCapacity
     * 
     * 
     * 
     * @return boolean
     */
    public boolean hasCapacity()
    {
        return this._has_capacity;
    } //-- boolean hasCapacity() 

    /**
     * Method hasDebug
     * 
     * 
     * 
     * @return boolean
     */
    public boolean hasDebug()
    {
        return this._has_debug;
    } //-- boolean hasDebug() 

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
     * Method removeParam
     * 
     * 
     * 
     * @param vParam
     * @return boolean
     */
    public boolean removeParam(org.exolab.castor.mapping.xml.Param vParam)
    {
        boolean removed = _paramList.remove(vParam);
        return removed;
    } //-- boolean removeParam(org.exolab.castor.mapping.xml.Param) 

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
     * Sets the value of field 'debug'.
     * 
     * @param debug the value of field 'debug'.
     */
    public void setDebug(boolean debug)
    {
        this._debug = debug;
        this._has_debug = true;
    } //-- void setDebug(boolean) 

    /**
     * Method setParam
     * 
     * 
     * 
     * @param index
     * @param vParam
     */
    public void setParam(int index, org.exolab.castor.mapping.xml.Param vParam)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index >= _paramList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _paramList.set(index, vParam);
    } //-- void setParam(int, org.exolab.castor.mapping.xml.Param) 

    /**
     * Method setParam
     * 
     * 
     * 
     * @param paramArray
     */
    public void setParam(org.exolab.castor.mapping.xml.Param[] paramArray)
    {
        //-- copy array
        _paramList.clear();
        for (int i = 0; i < paramArray.length; i++) {
            _paramList.add(paramArray[i]);
        }
    } //-- void setParam(org.exolab.castor.mapping.xml.Param) 

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
     * @return CacheTypeMapping
     */
    public static org.exolab.castor.mapping.xml.CacheTypeMapping unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.exolab.castor.mapping.xml.CacheTypeMapping) Unmarshaller.unmarshal(org.exolab.castor.mapping.xml.CacheTypeMapping.class, reader);
    } //-- org.exolab.castor.mapping.xml.CacheTypeMapping unmarshal(java.io.Reader) 

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
