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
public class SourceGeneratorTest implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    private java.lang.String _schema;

    private java.lang.String _property_File;

    private java.lang.String _fieldInfoFactory;

    private Root_Object _root_Object;

    private java.util.Vector _unitTestCaseList;


      //----------------/
     //- Constructors -/
    //----------------/

    public SourceGeneratorTest() {
        super();
        _unitTestCaseList = new Vector();
    } //-- org.exolab.castor.tests.framework.testDescriptor.SourceGeneratorTest()


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
     * Returns the value of field 'fieldInfoFactory'.
     * @return the value of field 'fieldInfoFactory'.
    **/
    public java.lang.String getFieldInfoFactory()
    {
        return this._fieldInfoFactory;
    } //-- java.lang.String getFieldInfoFactory() 

    /**
     * Returns the value of field 'property_File'.
     * @return the value of field 'property_File'.
    **/
    public java.lang.String getProperty_File()
    {
        return this._property_File;
    } //-- java.lang.String getProperty_File() 

    /**
     * Returns the value of field 'root_Object'.
     * @return the value of field 'root_Object'.
    **/
    public Root_Object getRoot_Object()
    {
        return this._root_Object;
    } //-- Root_Object getRoot_Object() 

    /**
     * Returns the value of field 'schema'.
     * @return the value of field 'schema'.
    **/
    public java.lang.String getSchema()
    {
        return this._schema;
    } //-- java.lang.String getSchema() 

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
     * Sets the value of field 'fieldInfoFactory'.
     * @param fieldInfoFactory the value of field 'fieldInfoFactory'
    **/
    public void setFieldInfoFactory(java.lang.String fieldInfoFactory)
    {
        this._fieldInfoFactory = fieldInfoFactory;
    } //-- void setFieldInfoFactory(java.lang.String) 

    /**
     * Sets the value of field 'property_File'.
     * @param property_File the value of field 'property_File'.
    **/
    public void setProperty_File(java.lang.String property_File)
    {
        this._property_File = property_File;
    } //-- void setProperty_File(java.lang.String) 

    /**
     * Sets the value of field 'root_Object'.
     * @param root_Object the value of field 'root_Object'.
    **/
    public void setRoot_Object(Root_Object root_Object)
    {
        this._root_Object = root_Object;
    } //-- void setRoot_Object(Root_Object) 

    /**
     * Sets the value of field 'schema'.
     * @param schema the value of field 'schema'.
    **/
    public void setSchema(java.lang.String schema)
    {
        this._schema = schema;
    } //-- void setSchema(java.lang.String) 

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
    public static org.exolab.castor.tests.framework.testDescriptor.SourceGeneratorTest unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.exolab.castor.tests.framework.testDescriptor.SourceGeneratorTest) Unmarshaller.unmarshal(org.exolab.castor.tests.framework.testDescriptor.SourceGeneratorTest.class, reader);
    } //-- org.exolab.castor.tests.framework.testDescriptor.SourceGeneratorTest unmarshal(java.io.Reader) 

    /**
    **/
    public void validate()
        throws org.exolab.castor.xml.ValidationException
    {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    } //-- void validate() 

}
