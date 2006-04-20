/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.2</a>, using an XML
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
 * Class MarshallingTest.
 * 
 * @version $Revision$ $Date$
 */
public class MarshallingTest implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _root_Object
     */
    private org.exolab.castor.tests.framework.testDescriptor.Root_Object _root_Object;

    /**
     * Field _unitTestCaseList
     */
    private java.util.Vector _unitTestCaseList;


      //----------------/
     //- Constructors -/
    //----------------/

    public MarshallingTest() {
        super();
        _unitTestCaseList = new Vector();
    } //-- org.exolab.castor.tests.framework.testDescriptor.MarshallingTest()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addUnitTestCase
     * 
     * @param vUnitTestCase
     */
    public void addUnitTestCase(org.exolab.castor.tests.framework.testDescriptor.UnitTestCase vUnitTestCase)
        throws java.lang.IndexOutOfBoundsException
    {
        _unitTestCaseList.addElement(vUnitTestCase);
    } //-- void addUnitTestCase(org.exolab.castor.tests.framework.testDescriptor.UnitTestCase) 

    /**
     * Method addUnitTestCase
     * 
     * @param index
     * @param vUnitTestCase
     */
    public void addUnitTestCase(int index, org.exolab.castor.tests.framework.testDescriptor.UnitTestCase vUnitTestCase)
        throws java.lang.IndexOutOfBoundsException
    {
        _unitTestCaseList.insertElementAt(vUnitTestCase, index);
    } //-- void addUnitTestCase(int, org.exolab.castor.tests.framework.testDescriptor.UnitTestCase) 

    /**
     * Method enumerateUnitTestCase
     */
    public java.util.Enumeration enumerateUnitTestCase()
    {
        return _unitTestCaseList.elements();
    } //-- java.util.Enumeration enumerateUnitTestCase() 

    /**
     * Method getRoot_ObjectReturns the value of field
     * 'root_Object'.
     * 
     * @return the value of field 'root_Object'.
     */
    public org.exolab.castor.tests.framework.testDescriptor.Root_Object getRoot_Object()
    {
        return this._root_Object;
    } //-- org.exolab.castor.tests.framework.testDescriptor.Root_Object getRoot_Object() 

    /**
     * Method getUnitTestCase
     * 
     * @param index
     */
    public org.exolab.castor.tests.framework.testDescriptor.UnitTestCase getUnitTestCase(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _unitTestCaseList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (org.exolab.castor.tests.framework.testDescriptor.UnitTestCase) _unitTestCaseList.elementAt(index);
    } //-- org.exolab.castor.tests.framework.testDescriptor.UnitTestCase getUnitTestCase(int) 

    /**
     * Method getUnitTestCase
     */
    public org.exolab.castor.tests.framework.testDescriptor.UnitTestCase[] getUnitTestCase()
    {
        int size = _unitTestCaseList.size();
        org.exolab.castor.tests.framework.testDescriptor.UnitTestCase[] mArray = new org.exolab.castor.tests.framework.testDescriptor.UnitTestCase[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (org.exolab.castor.tests.framework.testDescriptor.UnitTestCase) _unitTestCaseList.elementAt(index);
        }
        return mArray;
    } //-- org.exolab.castor.tests.framework.testDescriptor.UnitTestCase[] getUnitTestCase() 

    /**
     * Method getUnitTestCaseCount
     */
    public int getUnitTestCaseCount()
    {
        return _unitTestCaseList.size();
    } //-- int getUnitTestCaseCount() 

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
     * Method removeAllUnitTestCase
     */
    public void removeAllUnitTestCase()
    {
        _unitTestCaseList.removeAllElements();
    } //-- void removeAllUnitTestCase() 

    /**
     * Method removeUnitTestCase
     * 
     * @param index
     */
    public org.exolab.castor.tests.framework.testDescriptor.UnitTestCase removeUnitTestCase(int index)
    {
        java.lang.Object obj = _unitTestCaseList.elementAt(index);
        _unitTestCaseList.removeElementAt(index);
        return (org.exolab.castor.tests.framework.testDescriptor.UnitTestCase) obj;
    } //-- org.exolab.castor.tests.framework.testDescriptor.UnitTestCase removeUnitTestCase(int) 

    /**
     * Method setRoot_ObjectSets the value of field 'root_Object'.
     * 
     * @param root_Object the value of field 'root_Object'.
     */
    public void setRoot_Object(org.exolab.castor.tests.framework.testDescriptor.Root_Object root_Object)
    {
        this._root_Object = root_Object;
    } //-- void setRoot_Object(org.exolab.castor.tests.framework.testDescriptor.Root_Object) 

    /**
     * Method setUnitTestCase
     * 
     * @param index
     * @param vUnitTestCase
     */
    public void setUnitTestCase(int index, org.exolab.castor.tests.framework.testDescriptor.UnitTestCase vUnitTestCase)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _unitTestCaseList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _unitTestCaseList.setElementAt(vUnitTestCase, index);
    } //-- void setUnitTestCase(int, org.exolab.castor.tests.framework.testDescriptor.UnitTestCase) 

    /**
     * Method setUnitTestCase
     * 
     * @param unitTestCaseArray
     */
    public void setUnitTestCase(org.exolab.castor.tests.framework.testDescriptor.UnitTestCase[] unitTestCaseArray)
    {
        //-- copy array
        _unitTestCaseList.removeAllElements();
        for (int i = 0; i < unitTestCaseArray.length; i++) {
            _unitTestCaseList.addElement(unitTestCaseArray[i]);
        }
    } //-- void setUnitTestCase(org.exolab.castor.tests.framework.testDescriptor.UnitTestCase) 

    /**
     * Method unmarshal
     * 
     * @param reader
     */
    public static org.exolab.castor.tests.framework.testDescriptor.MarshallingTest unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.exolab.castor.tests.framework.testDescriptor.MarshallingTest) Unmarshaller.unmarshal(org.exolab.castor.tests.framework.testDescriptor.MarshallingTest.class, reader);
    } //-- org.exolab.castor.tests.framework.testDescriptor.MarshallingTest unmarshal(java.io.Reader) 

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
