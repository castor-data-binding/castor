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

import java.util.Vector;

import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

/**
 * Class ConfigurationType.
 * 
 * @version $Revision$ $Date: 2005-03-05 06:42:06 -0700 (Sat, 05 Mar 2005) $
 */
public class ConfigurationType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _callMethodList
     */
    private java.util.Vector _callMethodList;


      //----------------/
     //- Constructors -/
    //----------------/

    public ConfigurationType() {
        super();
        _callMethodList = new Vector();
    } //-- org.exolab.castor.tests.framework.testDescriptor.ConfigurationType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addCallMethod
     * 
     * @param vCallMethod
     */
    public void addCallMethod(org.exolab.castor.tests.framework.testDescriptor.CallMethod vCallMethod)
        throws java.lang.IndexOutOfBoundsException
    {
        _callMethodList.addElement(vCallMethod);
    } //-- void addCallMethod(org.exolab.castor.tests.framework.testDescriptor.CallMethod) 

    /**
     * Method addCallMethod
     * 
     * @param index
     * @param vCallMethod
     */
    public void addCallMethod(int index, org.exolab.castor.tests.framework.testDescriptor.CallMethod vCallMethod)
        throws java.lang.IndexOutOfBoundsException
    {
        _callMethodList.insertElementAt(vCallMethod, index);
    } //-- void addCallMethod(int, org.exolab.castor.tests.framework.testDescriptor.CallMethod) 

    /**
     * Method enumerateCallMethod
     */
    public java.util.Enumeration enumerateCallMethod()
    {
        return _callMethodList.elements();
    } //-- java.util.Enumeration enumerateCallMethod() 

    /**
     * Method getCallMethod
     * 
     * @param index
     */
    public org.exolab.castor.tests.framework.testDescriptor.CallMethod getCallMethod(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _callMethodList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (org.exolab.castor.tests.framework.testDescriptor.CallMethod) _callMethodList.elementAt(index);
    } //-- org.exolab.castor.tests.framework.testDescriptor.CallMethod getCallMethod(int) 

    /**
     * Method getCallMethod
     */
    public org.exolab.castor.tests.framework.testDescriptor.CallMethod[] getCallMethod()
    {
        int size = _callMethodList.size();
        org.exolab.castor.tests.framework.testDescriptor.CallMethod[] mArray = new org.exolab.castor.tests.framework.testDescriptor.CallMethod[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (org.exolab.castor.tests.framework.testDescriptor.CallMethod) _callMethodList.elementAt(index);
        }
        return mArray;
    } //-- org.exolab.castor.tests.framework.testDescriptor.CallMethod[] getCallMethod() 

    /**
     * Method getCallMethodCount
     */
    public int getCallMethodCount()
    {
        return _callMethodList.size();
    } //-- int getCallMethodCount() 

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
     * Method removeAllCallMethod
     */
    public void removeAllCallMethod()
    {
        _callMethodList.removeAllElements();
    } //-- void removeAllCallMethod() 

    /**
     * Method removeCallMethod
     * 
     * @param index
     */
    public org.exolab.castor.tests.framework.testDescriptor.CallMethod removeCallMethod(int index)
    {
        java.lang.Object obj = _callMethodList.elementAt(index);
        _callMethodList.removeElementAt(index);
        return (org.exolab.castor.tests.framework.testDescriptor.CallMethod) obj;
    } //-- org.exolab.castor.tests.framework.testDescriptor.CallMethod removeCallMethod(int) 

    /**
     * Method setCallMethod
     * 
     * @param index
     * @param vCallMethod
     */
    public void setCallMethod(int index, org.exolab.castor.tests.framework.testDescriptor.CallMethod vCallMethod)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _callMethodList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _callMethodList.setElementAt(vCallMethod, index);
    } //-- void setCallMethod(int, org.exolab.castor.tests.framework.testDescriptor.CallMethod) 

    /**
     * Method setCallMethod
     * 
     * @param callMethodArray
     */
    public void setCallMethod(org.exolab.castor.tests.framework.testDescriptor.CallMethod[] callMethodArray)
    {
        //-- copy array
        _callMethodList.removeAllElements();
        for (int i = 0; i < callMethodArray.length; i++) {
            _callMethodList.addElement(callMethodArray[i]);
        }
    } //-- void setCallMethod(org.exolab.castor.tests.framework.testDescriptor.CallMethod) 

    /**
     * Method unmarshalConfigurationType
     * 
     * @param reader
     */
    public static java.lang.Object unmarshalConfigurationType(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.exolab.castor.tests.framework.testDescriptor.ConfigurationType) Unmarshaller.unmarshal(org.exolab.castor.tests.framework.testDescriptor.ConfigurationType.class, reader);
    } //-- java.lang.Object unmarshalConfigurationType(java.io.Reader) 

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
