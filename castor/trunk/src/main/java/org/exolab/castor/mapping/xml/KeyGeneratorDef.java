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
 * Class KeyGeneratorDef.
 * 
 * @version $Revision$ $Date: 2006-01-07 15:48:31 -0700 (Sat, 07 Jan 2006) $
 */
public class KeyGeneratorDef implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _name
     */
    private java.lang.String _name;

    /**
     * Field _alias
     */
    private java.lang.String _alias;

    /**
     * Field _paramList
     */
    private java.util.ArrayList _paramList;


      //----------------/
     //- Constructors -/
    //----------------/

    public KeyGeneratorDef() 
     {
        super();
        _paramList = new java.util.ArrayList();
    } //-- org.exolab.castor.mapping.xml.KeyGeneratorDef()


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
     * Returns the value of field 'alias'.
     * 
     * @return String
     * @return the value of field 'alias'.
     */
    public java.lang.String getAlias()
    {
        return this._alias;
    } //-- java.lang.String getAlias() 

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
     * Sets the value of field 'alias'.
     * 
     * @param alias the value of field 'alias'.
     */
    public void setAlias(java.lang.String alias)
    {
        this._alias = alias;
    } //-- void setAlias(java.lang.String) 

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
     * Method unmarshal
     * 
     * 
     * 
     * @param reader
     * @return KeyGeneratorDef
     */
    public static org.exolab.castor.mapping.xml.KeyGeneratorDef unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.exolab.castor.mapping.xml.KeyGeneratorDef) Unmarshaller.unmarshal(org.exolab.castor.mapping.xml.KeyGeneratorDef.class, reader);
    } //-- org.exolab.castor.mapping.xml.KeyGeneratorDef unmarshal(java.io.Reader) 

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
