/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.5.2</a>, using an XML
 * Schema.
 * $Id$
 */

package org.exolab.castor.jdo.conf;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Enumeration;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.ContentHandler;

/**
 * Class DataSource.
 * 
 * @version $Revision$ $Date$
 */
public class DataSource implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _className
     */
    private java.lang.String _className;

    /**
     * Field _paramList
     */
    private java.util.ArrayList _paramList;


      //----------------/
     //- Constructors -/
    //----------------/

    public DataSource() {
        super();
        _paramList = new ArrayList();
    } //-- org.exolab.castor.jdo.conf.DataSource()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addParam
     * 
     * @param vParam
     */
    public void addParam(org.exolab.castor.jdo.conf.Param vParam)
        throws java.lang.IndexOutOfBoundsException
    {
        _paramList.add(vParam);
    } //-- void addParam(org.exolab.castor.jdo.conf.Param) 

    /**
     * Method addParam
     * 
     * @param index
     * @param vParam
     */
    public void addParam(int index, org.exolab.castor.jdo.conf.Param vParam)
        throws java.lang.IndexOutOfBoundsException
    {
        _paramList.add(index, vParam);
    } //-- void addParam(int, org.exolab.castor.jdo.conf.Param) 

    /**
     * Method clearParam
     */
    public void clearParam()
    {
        _paramList.clear();
    } //-- void clearParam() 

    /**
     * Method enumerateParam
     */
    public java.util.Enumeration enumerateParam()
    {
        return new org.exolab.castor.util.IteratorEnumeration(_paramList.iterator());
    } //-- java.util.Enumeration enumerateParam() 

    /**
     * Returns the value of field 'className'.
     * 
     * @return the value of field 'className'.
     */
    public java.lang.String getClassName()
    {
        return this._className;
    } //-- java.lang.String getClassName() 

    /**
     * Method getParam
     * 
     * @param index
     */
    public org.exolab.castor.jdo.conf.Param getParam(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _paramList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (org.exolab.castor.jdo.conf.Param) _paramList.get(index);
    } //-- org.exolab.castor.jdo.conf.Param getParam(int) 

    /**
     * Method getParam
     */
    public org.exolab.castor.jdo.conf.Param[] getParam()
    {
        int size = _paramList.size();
        org.exolab.castor.jdo.conf.Param[] mArray = new org.exolab.castor.jdo.conf.Param[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (org.exolab.castor.jdo.conf.Param) _paramList.get(index);
        }
        return mArray;
    } //-- org.exolab.castor.jdo.conf.Param[] getParam() 

    /**
     * Method getParamCount
     */
    public int getParamCount()
    {
        return _paramList.size();
    } //-- int getParamCount() 

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
     * Method removeParam
     * 
     * @param vParam
     */
    public boolean removeParam(org.exolab.castor.jdo.conf.Param vParam)
    {
        boolean removed = _paramList.remove(vParam);
        return removed;
    } //-- boolean removeParam(org.exolab.castor.jdo.conf.Param) 

    /**
     * Sets the value of field 'className'.
     * 
     * @param className the value of field 'className'.
     */
    public void setClassName(java.lang.String className)
    {
        this._className = className;
    } //-- void setClassName(java.lang.String) 

    /**
     * Method setParam
     * 
     * @param index
     * @param vParam
     */
    public void setParam(int index, org.exolab.castor.jdo.conf.Param vParam)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _paramList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _paramList.set(index, vParam);
    } //-- void setParam(int, org.exolab.castor.jdo.conf.Param) 

    /**
     * Method setParam
     * 
     * @param paramArray
     */
    public void setParam(org.exolab.castor.jdo.conf.Param[] paramArray)
    {
        //-- copy array
        _paramList.clear();
        for (int i = 0; i < paramArray.length; i++) {
            _paramList.add(paramArray[i]);
        }
    } //-- void setParam(org.exolab.castor.jdo.conf.Param) 

    /**
     * Method unmarshal
     * 
     * @param reader
     */
    public static java.lang.Object unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.exolab.castor.jdo.conf.DataSource) Unmarshaller.unmarshal(org.exolab.castor.jdo.conf.DataSource.class, reader);
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
