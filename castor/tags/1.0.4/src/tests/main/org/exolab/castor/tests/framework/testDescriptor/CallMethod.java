/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.0.3</a>, using an XML
 * Schema.
 * $Id$
 */

package org.exolab.castor.tests.framework.testDescriptor;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

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

    public CallMethod() 
     {
        super();
        this._valueList = new java.util.Vector();
    } //-- org.exolab.castor.tests.framework.testDescriptor.CallMethod()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * 
     * 
     * @param vValue
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addValue(org.exolab.castor.tests.framework.testDescriptor.Value vValue)
        throws java.lang.IndexOutOfBoundsException
    {
        this._valueList.addElement(vValue);
    } //-- void addValue(org.exolab.castor.tests.framework.testDescriptor.Value) 

    /**
     * 
     * 
     * @param index
     * @param vValue
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addValue(int index, org.exolab.castor.tests.framework.testDescriptor.Value vValue)
        throws java.lang.IndexOutOfBoundsException
    {
        this._valueList.add(index, vValue);
    } //-- void addValue(int, org.exolab.castor.tests.framework.testDescriptor.Value) 

    /**
     * Method enumerateValue
     * 
     * 
     * 
     * @return an Enumeration over all
     * org.exolab.castor.tests.framework.testDescriptor.Value
     * elements
     */
    public java.util.Enumeration enumerateValue()
    {
        return this._valueList.elements();
    } //-- java.util.Enumeration enumerateValue() 

    /**
     * Returns the value of field 'name'.
     * 
     * @return the value of field 'Name'.
     */
    public java.lang.String getName()
    {
        return this._name;
    } //-- java.lang.String getName() 

    /**
     * Method getValue
     * 
     * 
     * 
     * @param index
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     * @return the value of the
     * org.exolab.castor.tests.framework.testDescriptor.Value at
     * the given index
     */
    public org.exolab.castor.tests.framework.testDescriptor.Value getValue(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        // check bounds for index
        if (index < 0 || index >= this._valueList.size()) {
            throw new IndexOutOfBoundsException("getValue: Index value '" + index + "' not in range [0.." + (this._valueList.size() - 1) + "]");
        }
        
        return (org.exolab.castor.tests.framework.testDescriptor.Value) _valueList.get(index);
    } //-- org.exolab.castor.tests.framework.testDescriptor.Value getValue(int) 

    /**
     * Method getValue
     * 
     * 
     * 
     * @return this collection as an Array
     */
    public org.exolab.castor.tests.framework.testDescriptor.Value[] getValue()
    {
        int size = this._valueList.size();
        org.exolab.castor.tests.framework.testDescriptor.Value[] array = new org.exolab.castor.tests.framework.testDescriptor.Value[size];
        for (int index = 0; index < size; index++){
            array[index] = (org.exolab.castor.tests.framework.testDescriptor.Value) _valueList.get(index);
        }
        
        return array;
    } //-- org.exolab.castor.tests.framework.testDescriptor.Value[] getValue() 

    /**
     * Method getValueCount
     * 
     * 
     * 
     * @return the size of this collection
     */
    public int getValueCount()
    {
        return this._valueList.size();
    } //-- int getValueCount() 

    /**
     * Method isValid
     * 
     * 
     * 
     * @return true if this object is valid according to the schema
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
     * 
     * 
     * @param out
     * @throws org.exolab.castor.xml.MarshalException if object is
     * null or if any SAXException is thrown during marshaling
     * @throws org.exolab.castor.xml.ValidationException if this
     * object is an invalid instance according to the schema
     */
    public void marshal(java.io.Writer out)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        
        Marshaller.marshal(this, out);
    } //-- void marshal(java.io.Writer) 

    /**
     * 
     * 
     * @param handler
     * @throws java.io.IOException if an IOException occurs during
     * marshaling
     * @throws org.exolab.castor.xml.ValidationException if this
     * object is an invalid instance according to the schema
     * @throws org.exolab.castor.xml.MarshalException if object is
     * null or if any SAXException is thrown during marshaling
     */
    public void marshal(org.xml.sax.ContentHandler handler)
        throws java.io.IOException, org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        
        Marshaller.marshal(this, handler);
    } //-- void marshal(org.xml.sax.ContentHandler) 

    /**
     */
    public void removeAllValue()
    {
        this._valueList.clear();
    } //-- void removeAllValue() 

    /**
     * Method removeValue
     * 
     * 
     * 
     * @param vValue
     * @return true if the object was removed from the collection.
     */
    public boolean removeValue(org.exolab.castor.tests.framework.testDescriptor.Value vValue)
    {
        boolean removed = _valueList.remove(vValue);
        return removed;
    } //-- boolean removeValue(org.exolab.castor.tests.framework.testDescriptor.Value) 

    /**
     * Method removeValueAt
     * 
     * 
     * 
     * @param index
     * @return the element removed from the collection
     */
    public org.exolab.castor.tests.framework.testDescriptor.Value removeValueAt(int index)
    {
        Object obj = this._valueList.remove(index);
        return (org.exolab.castor.tests.framework.testDescriptor.Value) obj;
    } //-- org.exolab.castor.tests.framework.testDescriptor.Value removeValueAt(int) 

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
     * 
     * 
     * @param index
     * @param vValue
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void setValue(int index, org.exolab.castor.tests.framework.testDescriptor.Value vValue)
        throws java.lang.IndexOutOfBoundsException
    {
        // check bounds for index
        if (index < 0 || index >= this._valueList.size()) {
            throw new IndexOutOfBoundsException("setValue: Index value '" + index + "' not in range [0.." + (this._valueList.size() - 1) + "]");
        }
        
        this._valueList.set(index, vValue);
    } //-- void setValue(int, org.exolab.castor.tests.framework.testDescriptor.Value) 

    /**
     * 
     * 
     * @param vValueArray
     */
    public void setValue(org.exolab.castor.tests.framework.testDescriptor.Value[] vValueArray)
    {
        //-- copy array
        _valueList.clear();
        
        for (int i = 0; i < vValueArray.length; i++) {
                this._valueList.add(vValueArray[i]);
        }
    } //-- void setValue(org.exolab.castor.tests.framework.testDescriptor.Value) 

    /**
     * Method unmarshal
     * 
     * 
     * 
     * @param reader
     * @throws org.exolab.castor.xml.MarshalException if object is
     * null or if any SAXException is thrown during marshaling
     * @throws org.exolab.castor.xml.ValidationException if this
     * object is an invalid instance according to the schema
     * @return the unmarshaled
     * org.exolab.castor.tests.framework.testDescriptor.CallMethod
     */
    public static org.exolab.castor.tests.framework.testDescriptor.CallMethod unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.exolab.castor.tests.framework.testDescriptor.CallMethod) Unmarshaller.unmarshal(org.exolab.castor.tests.framework.testDescriptor.CallMethod.class, reader);
    } //-- org.exolab.castor.tests.framework.testDescriptor.CallMethod unmarshal(java.io.Reader) 

    /**
     * 
     * 
     * @throws org.exolab.castor.xml.ValidationException if this
     * object is an invalid instance according to the schema
     */
    public void validate()
        throws org.exolab.castor.xml.ValidationException
    {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    } //-- void validate() 

}
