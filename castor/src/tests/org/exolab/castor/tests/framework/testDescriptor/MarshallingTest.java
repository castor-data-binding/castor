/*
 * This class was automatically generated with 
 * <a href="http://castor.exolab.org">Castor 0.9.3.9+</a>, using an
 * XML Schema.
 * $Id$
 */

package org.exolab.castor.tests.framework.testDescriptor;

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
public class MarshallingTest implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    private Root_Object _root_Object;

    private java.lang.String _mapping_File;

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
     * 
     * @param vUnitTestCase
    **/
    public void addUnitTestCase(UnitTestCase vUnitTestCase)
        throws java.lang.IndexOutOfBoundsException
    {
        _unitTestCaseList.addElement(vUnitTestCase);
    } //-- void addUnitTestCase(UnitTestCase) 

    /**
     * 
     * @param index
     * @param vUnitTestCase
    **/
    public void addUnitTestCase(int index, UnitTestCase vUnitTestCase)
        throws java.lang.IndexOutOfBoundsException
    {
        _unitTestCaseList.insertElementAt(vUnitTestCase, index);
    } //-- void addUnitTestCase(int, UnitTestCase) 

    /**
    **/
    public java.util.Enumeration enumerateUnitTestCase()
    {
        return _unitTestCaseList.elements();
    } //-- java.util.Enumeration enumerateUnitTestCase() 

    /**
     * Returns the value of field 'mapping_File'.
     * @return the value of field 'mapping_File'.
    **/
    public java.lang.String getMapping_File()
    {
        return this._mapping_File;
    } //-- java.lang.String getMapping_File() 

    /**
     * Returns the value of field 'root_Object'.
     * @return the value of field 'root_Object'.
    **/
    public Root_Object getRoot_Object()
    {
        return this._root_Object;
    } //-- Root_Object getRoot_Object() 

    /**
     * 
     * @param index
    **/
    public UnitTestCase getUnitTestCase(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _unitTestCaseList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (UnitTestCase) _unitTestCaseList.elementAt(index);
    } //-- UnitTestCase getUnitTestCase(int) 

    /**
    **/
    public UnitTestCase[] getUnitTestCase()
    {
        int size = _unitTestCaseList.size();
        UnitTestCase[] mArray = new UnitTestCase[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (UnitTestCase) _unitTestCaseList.elementAt(index);
        }
        return mArray;
    } //-- UnitTestCase[] getUnitTestCase() 

    /**
    **/
    public int getUnitTestCaseCount()
    {
        return _unitTestCaseList.size();
    } //-- int getUnitTestCaseCount() 

    /**
    **/
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
     * @param out
    **/
    public void marshal(java.io.Writer out)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        
        Marshaller.marshal(this, out);
    } //-- void marshal(java.io.Writer) 

    /**
     * 
     * @param handler
    **/
    public void marshal(org.xml.sax.DocumentHandler handler)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        
        Marshaller.marshal(this, handler);
    } //-- void marshal(org.xml.sax.DocumentHandler) 

    /**
    **/
    public void removeAllUnitTestCase()
    {
        _unitTestCaseList.removeAllElements();
    } //-- void removeAllUnitTestCase() 

    /**
     * 
     * @param index
    **/
    public UnitTestCase removeUnitTestCase(int index)
    {
        java.lang.Object obj = _unitTestCaseList.elementAt(index);
        _unitTestCaseList.removeElementAt(index);
        return (UnitTestCase) obj;
    } //-- UnitTestCase removeUnitTestCase(int) 

    /**
     * Sets the value of field 'mapping_File'.
     * @param mapping_File the value of field 'mapping_File'.
    **/
    public void setMapping_File(java.lang.String mapping_File)
    {
        this._mapping_File = mapping_File;
    } //-- void setMapping_File(java.lang.String) 

    /**
     * Sets the value of field 'root_Object'.
     * @param root_Object the value of field 'root_Object'.
    **/
    public void setRoot_Object(Root_Object root_Object)
    {
        this._root_Object = root_Object;
    } //-- void setRoot_Object(Root_Object) 

    /**
     * 
     * @param index
     * @param vUnitTestCase
    **/
    public void setUnitTestCase(int index, UnitTestCase vUnitTestCase)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _unitTestCaseList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _unitTestCaseList.setElementAt(vUnitTestCase, index);
    } //-- void setUnitTestCase(int, UnitTestCase) 

    /**
     * 
     * @param unitTestCaseArray
    **/
    public void setUnitTestCase(UnitTestCase[] unitTestCaseArray)
    {
        //-- copy array
        _unitTestCaseList.removeAllElements();
        for (int i = 0; i < unitTestCaseArray.length; i++) {
            _unitTestCaseList.addElement(unitTestCaseArray[i]);
        }
    } //-- void setUnitTestCase(UnitTestCase) 

    /**
     * 
     * @param reader
    **/
    public static org.exolab.castor.tests.framework.testDescriptor.MarshallingTest unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.exolab.castor.tests.framework.testDescriptor.MarshallingTest) Unmarshaller.unmarshal(org.exolab.castor.tests.framework.testDescriptor.MarshallingTest.class, reader);
    } //-- org.exolab.castor.tests.framework.testDescriptor.MarshallingTest unmarshal(java.io.Reader) 

    /**
    **/
    public void validate()
        throws org.exolab.castor.xml.ValidationException
    {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    } //-- void validate() 

}
