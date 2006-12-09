/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.0.5</a>, using an XML
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
 * Configuration for marshaling or unmarshaling or source
 * generation. Contains
 *  a list of methods to be called on the marshaler or unmarshaler
 * or source
 *  generator with the parameters to be provided for each method.
 *  
 * 
 * @version $Revision$ $Date$
 */
public class ConfigurationType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * A single method to call
     *  
     */
    private java.util.Vector _callMethodList;


      //----------------/
     //- Constructors -/
    //----------------/

    public ConfigurationType() 
     {
        super();
        this._callMethodList = new java.util.Vector();
    } //-- org.exolab.castor.tests.framework.testDescriptor.ConfigurationType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * 
     * 
     * @param vCallMethod
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addCallMethod(org.exolab.castor.tests.framework.testDescriptor.CallMethod vCallMethod)
        throws java.lang.IndexOutOfBoundsException
    {
        this._callMethodList.addElement(vCallMethod);
    } //-- void addCallMethod(org.exolab.castor.tests.framework.testDescriptor.CallMethod) 

    /**
     * 
     * 
     * @param index
     * @param vCallMethod
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addCallMethod(int index, org.exolab.castor.tests.framework.testDescriptor.CallMethod vCallMethod)
        throws java.lang.IndexOutOfBoundsException
    {
        this._callMethodList.add(index, vCallMethod);
    } //-- void addCallMethod(int, org.exolab.castor.tests.framework.testDescriptor.CallMethod) 

    /**
     * Method enumerateCallMethod
     * 
     * 
     * 
     * @return an Enumeration over all
     * org.exolab.castor.tests.framework.testDescriptor.CallMethod
     * elements
     */
    public java.util.Enumeration enumerateCallMethod()
    {
        return this._callMethodList.elements();
    } //-- java.util.Enumeration enumerateCallMethod() 

    /**
     * Method getCallMethod
     * 
     * 
     * 
     * @param index
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     * @return the value of the
     * org.exolab.castor.tests.framework.testDescriptor.CallMethod
     * at the given index
     */
    public org.exolab.castor.tests.framework.testDescriptor.CallMethod getCallMethod(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        // check bounds for index
        if (index < 0 || index >= this._callMethodList.size()) {
            throw new IndexOutOfBoundsException("getCallMethod: Index value '" + index + "' not in range [0.." + (this._callMethodList.size() - 1) + "]");
        }
        
        return (org.exolab.castor.tests.framework.testDescriptor.CallMethod) _callMethodList.get(index);
    } //-- org.exolab.castor.tests.framework.testDescriptor.CallMethod getCallMethod(int) 

    /**
     * Method getCallMethod
     * 
     * 
     * 
     * @return this collection as an Array
     */
    public org.exolab.castor.tests.framework.testDescriptor.CallMethod[] getCallMethod()
    {
        int size = this._callMethodList.size();
        org.exolab.castor.tests.framework.testDescriptor.CallMethod[] array = new org.exolab.castor.tests.framework.testDescriptor.CallMethod[size];
        for (int index = 0; index < size; index++){
            array[index] = (org.exolab.castor.tests.framework.testDescriptor.CallMethod) _callMethodList.get(index);
        }
        
        return array;
    } //-- org.exolab.castor.tests.framework.testDescriptor.CallMethod[] getCallMethod() 

    /**
     * Method getCallMethodCount
     * 
     * 
     * 
     * @return the size of this collection
     */
    public int getCallMethodCount()
    {
        return this._callMethodList.size();
    } //-- int getCallMethodCount() 

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
    public void removeAllCallMethod()
    {
        this._callMethodList.clear();
    } //-- void removeAllCallMethod() 

    /**
     * Method removeCallMethod
     * 
     * 
     * 
     * @param vCallMethod
     * @return true if the object was removed from the collection.
     */
    public boolean removeCallMethod(org.exolab.castor.tests.framework.testDescriptor.CallMethod vCallMethod)
    {
        boolean removed = _callMethodList.remove(vCallMethod);
        return removed;
    } //-- boolean removeCallMethod(org.exolab.castor.tests.framework.testDescriptor.CallMethod) 

    /**
     * Method removeCallMethodAt
     * 
     * 
     * 
     * @param index
     * @return the element removed from the collection
     */
    public org.exolab.castor.tests.framework.testDescriptor.CallMethod removeCallMethodAt(int index)
    {
        Object obj = this._callMethodList.remove(index);
        return (org.exolab.castor.tests.framework.testDescriptor.CallMethod) obj;
    } //-- org.exolab.castor.tests.framework.testDescriptor.CallMethod removeCallMethodAt(int) 

    /**
     * 
     * 
     * @param index
     * @param vCallMethod
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void setCallMethod(int index, org.exolab.castor.tests.framework.testDescriptor.CallMethod vCallMethod)
        throws java.lang.IndexOutOfBoundsException
    {
        // check bounds for index
        if (index < 0 || index >= this._callMethodList.size()) {
            throw new IndexOutOfBoundsException("setCallMethod: Index value '" + index + "' not in range [0.." + (this._callMethodList.size() - 1) + "]");
        }
        
        this._callMethodList.set(index, vCallMethod);
    } //-- void setCallMethod(int, org.exolab.castor.tests.framework.testDescriptor.CallMethod) 

    /**
     * 
     * 
     * @param vCallMethodArray
     */
    public void setCallMethod(org.exolab.castor.tests.framework.testDescriptor.CallMethod[] vCallMethodArray)
    {
        //-- copy array
        _callMethodList.clear();
        
        for (int i = 0; i < vCallMethodArray.length; i++) {
                this._callMethodList.add(vCallMethodArray[i]);
        }
    } //-- void setCallMethod(org.exolab.castor.tests.framework.testDescriptor.CallMethod) 

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
     * org.exolab.castor.tests.framework.testDescriptor.ConfigurationType
     */
    public static org.exolab.castor.tests.framework.testDescriptor.ConfigurationType unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.exolab.castor.tests.framework.testDescriptor.ConfigurationType) Unmarshaller.unmarshal(org.exolab.castor.tests.framework.testDescriptor.ConfigurationType.class, reader);
    } //-- org.exolab.castor.tests.framework.testDescriptor.ConfigurationType unmarshal(java.io.Reader) 

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
