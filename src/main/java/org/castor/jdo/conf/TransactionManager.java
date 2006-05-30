/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.9.1</a>, using an XML
 * Schema.
 * $Id$
 */

package org.castor.jdo.conf;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.util.ArrayList;

import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

/**
 * Class TransactionManager.
 * 
 * @version $Revision$ $Date: 2005-11-19 10:17:16 -0700 (Sat, 19 Nov 2005) $
 */
public class TransactionManager implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _name
     */
    private java.lang.String _name = "local";

    /**
     * Field _paramList
     */
    private java.util.ArrayList _paramList;


      //----------------/
     //- Constructors -/
    //----------------/

    public TransactionManager() 
     {
        super();
        setName("local");
        _paramList = new ArrayList();
    } //-- org.castor.jdo.conf.TransactionManager()


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
    public void addParam(org.castor.jdo.conf.Param vParam)
        throws java.lang.IndexOutOfBoundsException
    {
        _paramList.add(vParam);
    } //-- void addParam(org.castor.jdo.conf.Param) 

    /**
     * Method addParam
     * 
     * 
     * 
     * @param index
     * @param vParam
     */
    public void addParam(int index, org.castor.jdo.conf.Param vParam)
        throws java.lang.IndexOutOfBoundsException
    {
        _paramList.add(index, vParam);
    } //-- void addParam(int, org.castor.jdo.conf.Param) 

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
        return new org.exolab.castor.util.IteratorEnumeration(_paramList.iterator());
    } //-- java.util.Enumeration enumerateParam() 

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
    public org.castor.jdo.conf.Param getParam(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _paramList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (org.castor.jdo.conf.Param) _paramList.get(index);
    } //-- org.castor.jdo.conf.Param getParam(int) 

    /**
     * Method getParam
     * 
     * 
     * 
     * @return Param
     */
    public org.castor.jdo.conf.Param[] getParam()
    {
        int size = _paramList.size();
        org.castor.jdo.conf.Param[] mArray = new org.castor.jdo.conf.Param[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (org.castor.jdo.conf.Param) _paramList.get(index);
        }
        return mArray;
    } //-- org.castor.jdo.conf.Param[] getParam() 

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
    public boolean removeParam(org.castor.jdo.conf.Param vParam)
    {
        boolean removed = _paramList.remove(vParam);
        return removed;
    } //-- boolean removeParam(org.castor.jdo.conf.Param) 

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
    public void setParam(int index, org.castor.jdo.conf.Param vParam)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _paramList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _paramList.set(index, vParam);
    } //-- void setParam(int, org.castor.jdo.conf.Param) 

    /**
     * Method setParam
     * 
     * 
     * 
     * @param paramArray
     */
    public void setParam(org.castor.jdo.conf.Param[] paramArray)
    {
        //-- copy array
        _paramList.clear();
        for (int i = 0; i < paramArray.length; i++) {
            _paramList.add(paramArray[i]);
        }
    } //-- void setParam(org.castor.jdo.conf.Param) 

    /**
     * Method unmarshal
     * 
     * 
     * 
     * @param reader
     * @return TransactionManager
     */
    public static org.castor.jdo.conf.TransactionManager unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.castor.jdo.conf.TransactionManager) Unmarshaller.unmarshal(org.castor.jdo.conf.TransactionManager.class, reader);
    } //-- org.castor.jdo.conf.TransactionManager unmarshal(java.io.Reader) 

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
