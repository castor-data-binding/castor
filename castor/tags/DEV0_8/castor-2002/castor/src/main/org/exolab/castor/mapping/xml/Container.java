/*
 * This class was automatically generated with 
 * <a href="http://castor.exolab.org">Castor 0.8 (20000324)</a>,
 * using an XML Schema.
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
public class Container implements java.io.Serializable {


      //--------------------/
     //- Member Variables -/
    //--------------------/

    private java.lang.String _setMethod;

    private java.lang.String _createMethod;

    private java.lang.String _getMethod;

    private java.lang.String _type;

    private boolean _required = false;

    private java.lang.String _name;

    private java.lang.String _description;

    private java.util.Vector _fieldMappingList;


      //----------------/
     //- Constructors -/
    //----------------/

    public Container() {
        super();
        _fieldMappingList = new Vector();
    } //-- org.exolab.castor.mapping.xml.Container()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * 
     * @param vFieldMapping
    **/
    public void addFieldMapping(FieldMapping vFieldMapping) 
        throws java.lang.IndexOutOfBoundsException
    {
        _fieldMappingList.addElement(vFieldMapping);
    } //-- void addFieldMapping(FieldMapping) 

    /**
    **/
    public java.util.Enumeration enumerateFieldMapping() {
        return _fieldMappingList.elements();
    } //-- java.util.Enumeration enumerateFieldMapping() 

    /**
    **/
    public java.lang.String getCreateMethod() {
        return this._createMethod;
    } //-- java.lang.String getCreateMethod() 

    /**
    **/
    public java.lang.String getDescription() {
        return this._description;
    } //-- java.lang.String getDescription() 

    /**
     * 
     * @param index
    **/
    public FieldMapping getFieldMapping(int index) 
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _fieldMappingList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (FieldMapping) _fieldMappingList.elementAt(index);
    } //-- FieldMapping getFieldMapping(int) 

    /**
    **/
    public FieldMapping[] getFieldMapping() {
        int size = _fieldMappingList.size();
        FieldMapping[] mArray = new FieldMapping[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (FieldMapping) _fieldMappingList.elementAt(index);
        }
        return mArray;
    } //-- FieldMapping[] getFieldMapping() 

    /**
    **/
    public int getFieldMappingCount() {
        return _fieldMappingList.size();
    } //-- int getFieldMappingCount() 

    /**
    **/
    public java.lang.String getGetMethod() {
        return this._getMethod;
    } //-- java.lang.String getGetMethod() 

    /**
    **/
    public java.lang.String getName() {
        return this._name;
    } //-- java.lang.String getName() 

    /**
    **/
    public boolean getRequired() {
        return this._required;
    } //-- boolean getRequired() 

    /**
    **/
    public java.lang.String getSetMethod() {
        return this._setMethod;
    } //-- java.lang.String getSetMethod() 

    /**
    **/
    public java.lang.String getType() {
        return this._type;
    } //-- java.lang.String getType() 

    /**
    **/
    public void removeAllFieldMapping() {
        _fieldMappingList.removeAllElements();
    } //-- void removeAllFieldMapping() 

    /**
     * 
     * @param index
    **/
    public FieldMapping removeFieldMapping(int index) {
        Object obj = _fieldMappingList.elementAt(index);
        _fieldMappingList.removeElementAt(index);
        return (FieldMapping) obj;
    } //-- FieldMapping removeFieldMapping(int) 

    /**
     * 
     * @param _createMethod
    **/
    public void setCreateMethod(java.lang.String _createMethod) {
        this._createMethod = _createMethod;
    } //-- void setCreateMethod(java.lang.String) 

    /**
     * 
     * @param _description
    **/
    public void setDescription(java.lang.String _description) {
        this._description = _description;
    } //-- void setDescription(java.lang.String) 

    /**
     * 
     * @param vFieldMapping
     * @param index
    **/
    public void setFieldMapping(FieldMapping vFieldMapping, int index) 
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _fieldMappingList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _fieldMappingList.setElementAt(vFieldMapping, index);
    } //-- void setFieldMapping(FieldMapping, int) 

    /**
     * 
     * @param _getMethod
    **/
    public void setGetMethod(java.lang.String _getMethod) {
        this._getMethod = _getMethod;
    } //-- void setGetMethod(java.lang.String) 

    /**
     * 
     * @param _name
    **/
    public void setName(java.lang.String _name) {
        this._name = _name;
    } //-- void setName(java.lang.String) 

    /**
     * 
     * @param _required
    **/
    public void setRequired(boolean _required) {
        this._required = _required;
    } //-- void setRequired(boolean) 

    /**
     * 
     * @param _setMethod
    **/
    public void setSetMethod(java.lang.String _setMethod) {
        this._setMethod = _setMethod;
    } //-- void setSetMethod(java.lang.String) 

    /**
     * 
     * @param _type
    **/
    public void setType(java.lang.String _type) {
        this._type = _type;
    } //-- void setType(java.lang.String) 

}
