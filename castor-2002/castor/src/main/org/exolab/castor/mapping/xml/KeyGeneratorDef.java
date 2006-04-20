/*
 * This class was automatically generated with 
 * <a href="http://castor.exolab.org">Castor 0.8.7</a>, using an
 * XML Schema.
 * $Id
 */

package org.exolab.castor.mapping.xml;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import java.util.Enumeration;
import java.util.Vector;
import org.exolab.castor.xml.*;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.DocumentHandler;

/**
 * 
 * @version $Revision$ $Date$
**/
public class KeyGeneratorDef implements java.io.Serializable {


      //--------------------/
     //- Member Variables -/
    //--------------------/

    private java.lang.String _name;

    private java.lang.String _alias;

    private java.util.Vector _paramList;


      //----------------/
     //- Constructors -/
    //----------------/

    public KeyGeneratorDef() {
        super();
        _paramList = new Vector();
    } //-- org.exolab.castor.mapping.xml.KeyGeneratorDef()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * 
     * @param vParam
    **/
    public void addParam(Param vParam) 
        throws java.lang.IndexOutOfBoundsException
    {
        _paramList.addElement(vParam);
    } //-- void addParam(Param) 

    /**
    **/
    public java.util.Enumeration enumerateParam() {
        return _paramList.elements();
    } //-- java.util.Enumeration enumerateParam() 

    /**
    **/
    public java.lang.String getAlias() {
        return this._alias;
    } //-- java.lang.String getAlias() 

    /**
    **/
    public java.lang.String getName() {
        return this._name;
    } //-- java.lang.String getName() 

    /**
     * 
     * @param index
    **/
    public Param getParam(int index) 
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _paramList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (Param) _paramList.elementAt(index);
    } //-- Param getParam(int) 

    /**
    **/
    public Param[] getParam() {
        int size = _paramList.size();
        Param[] mArray = new Param[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (Param) _paramList.elementAt(index);
        }
        return mArray;
    } //-- Param[] getParam() 

    /**
    **/
    public int getParamCount() {
        return _paramList.size();
    } //-- int getParamCount() 

    /**
    **/
    public java.lang.String getReferenceId() {
        return this._name;
    } //-- java.lang.String getReferenceId() 

    /**
    **/
    public boolean isValid() {
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
        //-- we must have a valid element before marshalling
        //validate(false);
        
        Marshaller.marshal(this, out);
    } //-- void marshal(java.io.Writer) 

    /**
     * 
     * @param handler
    **/
    public void marshal(org.xml.sax.DocumentHandler handler) 
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        //-- we must have a valid element before marshalling
        //validate(false);
        
        Marshaller.marshal(this, handler);
    } //-- void marshal(org.xml.sax.DocumentHandler) 

    /**
    **/
    public void removeAllParam() {
        _paramList.removeAllElements();
    } //-- void removeAllParam() 

    /**
     * 
     * @param index
    **/
    public Param removeParam(int index) {
        Object obj = _paramList.elementAt(index);
        _paramList.removeElementAt(index);
        return (Param) obj;
    } //-- Param removeParam(int) 

    /**
     * 
     * @param _alias
    **/
    public void setAlias(java.lang.String _alias) {
        this._alias = _alias;
    } //-- void setAlias(java.lang.String) 

    /**
     * 
     * @param _name
    **/
    public void setName(java.lang.String _name) {
        this._name = _name;
    } //-- void setName(java.lang.String) 

    /**
     * 
     * @param vParam
     * @param index
    **/
    public void setParam(Param vParam, int index) 
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _paramList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _paramList.setElementAt(vParam, index);
    } //-- void setParam(Param, int) 

    /**
     * 
     * @param reader
    **/
    public static org.exolab.castor.mapping.xml.KeyGeneratorDef unmarshal(java.io.Reader reader) 
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.exolab.castor.mapping.xml.KeyGeneratorDef) Unmarshaller.unmarshal(org.exolab.castor.mapping.xml.KeyGeneratorDef.class, reader);
    } //-- org.exolab.castor.mapping.xml.KeyGeneratorDef unmarshal(java.io.Reader) 

    /**
    **/
    public void validate() 
        throws org.exolab.castor.xml.ValidationException
    {
        org.exolab.castor.xml.Validator.validate(this, null);
    } //-- void validate() 

}
