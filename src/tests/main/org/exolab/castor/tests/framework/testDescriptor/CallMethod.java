/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.1-M2</a>, using an XML
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
 * A single method to call
 *  
 * 
 * @version $Revision$ $Date$
 */
public class CallMethod implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _name.
     */
    private java.lang.String _name;

    /**
     * A parameter to be provided to a method.
     *  
     */
    private java.util.Vector _valueList;


      //----------------/
     //- Constructors -/
    //----------------/

    public CallMethod() {
        super();
        this._valueList = new java.util.Vector();
    }


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
    public void addValue(
            final org.exolab.castor.tests.framework.testDescriptor.Value vValue)
    throws java.lang.IndexOutOfBoundsException {
        this._valueList.addElement(vValue);
    }

    /**
     * 
     * 
     * @param index
     * @param vValue
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addValue(
            final int index,
            final org.exolab.castor.tests.framework.testDescriptor.Value vValue)
    throws java.lang.IndexOutOfBoundsException {
        this._valueList.add(index, vValue);
    }

    /**
     * Method enumerateValue.
     * 
     * @return an Enumeration over all
     * org.exolab.castor.tests.framework.testDescriptor.Value
     * elements
     */
    public java.util.Enumeration enumerateValue(
    ) {
        return this._valueList.elements();
    }

    /**
     * Returns the value of field 'name'.
     * 
     * @return the value of field 'Name'.
     */
    public java.lang.String getName(
    ) {
        return this._name;
    }

    /**
     * Method getValue.
     * 
     * @param index
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     * @return the value of the
     * org.exolab.castor.tests.framework.testDescriptor.Value at
     * the given index
     */
    public org.exolab.castor.tests.framework.testDescriptor.Value getValue(
            final int index)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._valueList.size()) {
            throw new IndexOutOfBoundsException("getValue: Index value '" + index + "' not in range [0.." + (this._valueList.size() - 1) + "]");
        }
        
        return (org.exolab.castor.tests.framework.testDescriptor.Value) _valueList.get(index);
    }

    /**
     * Method getValue.Returns the contents of the collection in an
     * Array.  <p>Note:  Just in case the collection contents are
     * changing in another thread, we pass a 0-length Array of the
     * correct type into the API call.  This way we <i>know</i>
     * that the Array returned is of exactly the correct length.
     * 
     * @return this collection as an Array
     */
    public org.exolab.castor.tests.framework.testDescriptor.Value[] getValue(
    ) {
        org.exolab.castor.tests.framework.testDescriptor.Value[] array = new org.exolab.castor.tests.framework.testDescriptor.Value[0];
        return (org.exolab.castor.tests.framework.testDescriptor.Value[]) this._valueList.toArray(array);
    }

    /**
     * Method getValueCount.
     * 
     * @return the size of this collection
     */
    public int getValueCount(
    ) {
        return this._valueList.size();
    }

    /**
     * Method isValid.
     * 
     * @return true if this object is valid according to the schema
     */
    public boolean isValid(
    ) {
        try {
            validate();
        } catch (org.exolab.castor.xml.ValidationException vex) {
            return false;
        }
        return true;
    }

    /**
     * 
     * 
     * @param out
     * @throws org.exolab.castor.xml.MarshalException if object is
     * null or if any SAXException is thrown during marshaling
     * @throws org.exolab.castor.xml.ValidationException if this
     * object is an invalid instance according to the schema
     */
    public void marshal(
            final java.io.Writer out)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        Marshaller.marshal(this, out);
    }

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
    public void marshal(
            final org.xml.sax.ContentHandler handler)
    throws java.io.IOException, org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        Marshaller.marshal(this, handler);
    }

    /**
     */
    public void removeAllValue(
    ) {
        this._valueList.clear();
    }

    /**
     * Method removeValue.
     * 
     * @param vValue
     * @return true if the object was removed from the collection.
     */
    public boolean removeValue(
            final org.exolab.castor.tests.framework.testDescriptor.Value vValue) {
        boolean removed = _valueList.remove(vValue);
        return removed;
    }

    /**
     * Method removeValueAt.
     * 
     * @param index
     * @return the element removed from the collection
     */
    public org.exolab.castor.tests.framework.testDescriptor.Value removeValueAt(
            final int index) {
        Object obj = this._valueList.remove(index);
        return (org.exolab.castor.tests.framework.testDescriptor.Value) obj;
    }

    /**
     * Sets the value of field 'name'.
     * 
     * @param name the value of field 'name'.
     */
    public void setName(
            final java.lang.String name) {
        this._name = name;
    }

    /**
     * 
     * 
     * @param index
     * @param vValue
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void setValue(
            final int index,
            final org.exolab.castor.tests.framework.testDescriptor.Value vValue)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._valueList.size()) {
            throw new IndexOutOfBoundsException("setValue: Index value '" + index + "' not in range [0.." + (this._valueList.size() - 1) + "]");
        }
        
        this._valueList.set(index, vValue);
    }

    /**
     * 
     * 
     * @param vValueArray
     */
    public void setValue(
            final org.exolab.castor.tests.framework.testDescriptor.Value[] vValueArray) {
        //-- copy array
        _valueList.clear();
        
        for (int i = 0; i < vValueArray.length; i++) {
                this._valueList.add(vValueArray[i]);
        }
    }

    /**
     * Method unmarshal.
     * 
     * @param reader
     * @throws org.exolab.castor.xml.MarshalException if object is
     * null or if any SAXException is thrown during marshaling
     * @throws org.exolab.castor.xml.ValidationException if this
     * object is an invalid instance according to the schema
     * @return the unmarshaled
     * org.exolab.castor.tests.framework.testDescriptor.CallMethod
     */
    public static org.exolab.castor.tests.framework.testDescriptor.CallMethod unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (org.exolab.castor.tests.framework.testDescriptor.CallMethod) Unmarshaller.unmarshal(org.exolab.castor.tests.framework.testDescriptor.CallMethod.class, reader);
    }

    /**
     * 
     * 
     * @throws org.exolab.castor.xml.ValidationException if this
     * object is an invalid instance according to the schema
     */
    public void validate(
    )
    throws org.exolab.castor.xml.ValidationException {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    }

}
