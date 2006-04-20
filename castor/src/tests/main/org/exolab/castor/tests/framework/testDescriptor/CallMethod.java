/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.5.2</a>, using an XML
 * Schema.
 * $Id$
 */

package org.exolab.castor.tests.framework.testDescriptor;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import java.util.Enumeration;
import java.util.Vector;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.ContentHandler;

/**
 * Class CallMethod.
 * 
 * @version $Revision$ $Date$
 */
public class CallMethod implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _name
     */
    private java.lang.String _name;

    /**
     * Field _valueList
     */
    private java.util.Vector _valueList;


      //----------------/
     //- Constructors -/
    //----------------/

    public CallMethod() {
        super();
        _valueList = new Vector();
    } //-- org.exolab.castor.tests.framework.testDescriptor.CallMethod()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addValue
     * 
     * @param vValue
     */
    public void addValue(org.exolab.castor.tests.framework.testDescriptor.Value vValue)
        throws java.lang.IndexOutOfBoundsException
    {
        _valueList.addElement(vValue);
    } //-- void addValue(org.exolab.castor.tests.framework.testDescriptor.Value) 

    /**
     * Method addValue
     * 
     * @param index
     * @param vValue
     */
    public void addValue(int index, org.exolab.castor.tests.framework.testDescriptor.Value vValue)
        throws java.lang.IndexOutOfBoundsException
    {
        _valueList.insertElementAt(vValue, index);
    } //-- void addValue(int, org.exolab.castor.tests.framework.testDescriptor.Value) 

    /**
     * Method enumerateValue
     */
    public java.util.Enumeration enumerateValue()
    {
        return _valueList.elements();
    } //-- java.util.Enumeration enumerateValue() 

    /**
     * Returns the value of field 'name'.
     * 
     * @return the value of field 'name'.
     */
    public java.lang.String getName()
    {
        return this._name;
    } //-- java.lang.String getName() 

    /**
     * Method getValue
     * 
     * @param index
     */
    public org.exolab.castor.tests.framework.testDescriptor.Value getValue(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _valueList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (org.exolab.castor.tests.framework.testDescriptor.Value) _valueList.elementAt(index);
    } //-- org.exolab.castor.tests.framework.testDescriptor.Value getValue(int) 

    /**
     * Method getValue
     */
    public org.exolab.castor.tests.framework.testDescriptor.Value[] getValue()
    {
        int size = _valueList.size();
        org.exolab.castor.tests.framework.testDescriptor.Value[] mArray = new org.exolab.castor.tests.framework.testDescriptor.Value[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (org.exolab.castor.tests.framework.testDescriptor.Value) _valueList.elementAt(index);
        }
        return mArray;
    } //-- org.exolab.castor.tests.framework.testDescriptor.Value[] getValue() 

    /**
     * Method getValueCount
     */
    public int getValueCount()
    {
        return _valueList.size();
    } //-- int getValueCount() 

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
     * Method removeAllValue
     */
    public void removeAllValue()
    {
        _valueList.removeAllElements();
    } //-- void removeAllValue() 

    /**
     * Method removeValue
     * 
     * @param index
     */
    public org.exolab.castor.tests.framework.testDescriptor.Value removeValue(int index)
    {
        java.lang.Object obj = _valueList.elementAt(index);
        _valueList.removeElementAt(index);
        return (org.exolab.castor.tests.framework.testDescriptor.Value) obj;
    } //-- org.exolab.castor.tests.framework.testDescriptor.Value removeValue(int) 

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
     * Method setValue
     * 
     * @param index
     * @param vValue
     */
    public void setValue(int index, org.exolab.castor.tests.framework.testDescriptor.Value vValue)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _valueList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _valueList.setElementAt(vValue, index);
    } //-- void setValue(int, org.exolab.castor.tests.framework.testDescriptor.Value) 

    /**
     * Method setValue
     * 
     * @param valueArray
     */
    public void setValue(org.exolab.castor.tests.framework.testDescriptor.Value[] valueArray)
    {
        //-- copy array
        _valueList.removeAllElements();
        for (int i = 0; i < valueArray.length; i++) {
            _valueList.addElement(valueArray[i]);
        }
    } //-- void setValue(org.exolab.castor.tests.framework.testDescriptor.Value) 

    /**
     * Method unmarshalCallMethod
     * 
     * @param reader
     */
    public static java.lang.Object unmarshalCallMethod(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.exolab.castor.tests.framework.testDescriptor.CallMethod) Unmarshaller.unmarshal(org.exolab.castor.tests.framework.testDescriptor.CallMethod.class, reader);
    } //-- java.lang.Object unmarshalCallMethod(java.io.Reader) 

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
