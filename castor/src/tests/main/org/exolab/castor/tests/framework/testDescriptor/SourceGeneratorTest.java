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
 * Class SourceGeneratorTest.
 * 
 * @version $Revision$ $Date$
 */
public class SourceGeneratorTest implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _schemaList
     */
    private java.util.Vector _schemaList;

    /**
     * Field _property_File
     */
    private java.lang.String _property_File;

    /**
     * Field _collection
     */
    private org.exolab.castor.tests.framework.testDescriptor.types.CollectionType _collection = org.exolab.castor.tests.framework.testDescriptor.types.CollectionType.valueOf("vector");

    /**
     * Field _bindingFile
     */
    private java.lang.String _bindingFile;

    /**
     * Field _root_Object
     */
    private org.exolab.castor.tests.framework.testDescriptor.RootType _root_Object;

    /**
     * Field _unitTestCaseList
     */
    private java.util.Vector _unitTestCaseList;


      //----------------/
     //- Constructors -/
    //----------------/

    public SourceGeneratorTest() {
        super();
        _schemaList = new Vector();
        setCollection(org.exolab.castor.tests.framework.testDescriptor.types.CollectionType.valueOf("vector"));
        _unitTestCaseList = new Vector();
    } //-- org.exolab.castor.tests.framework.testDescriptor.SourceGeneratorTest()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addSchema
     * 
     * @param vSchema
     */
    public void addSchema(java.lang.String vSchema)
        throws java.lang.IndexOutOfBoundsException
    {
        _schemaList.addElement(vSchema);
    } //-- void addSchema(java.lang.String) 

    /**
     * Method addSchema
     * 
     * @param index
     * @param vSchema
     */
    public void addSchema(int index, java.lang.String vSchema)
        throws java.lang.IndexOutOfBoundsException
    {
        _schemaList.insertElementAt(vSchema, index);
    } //-- void addSchema(int, java.lang.String) 

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
     * Method enumerateSchema
     */
    public java.util.Enumeration enumerateSchema()
    {
        return _schemaList.elements();
    } //-- java.util.Enumeration enumerateSchema() 

    /**
     * Method enumerateUnitTestCase
     */
    public java.util.Enumeration enumerateUnitTestCase()
    {
        return _unitTestCaseList.elements();
    } //-- java.util.Enumeration enumerateUnitTestCase() 

    /**
     * Returns the value of field 'bindingFile'.
     * 
     * @return the value of field 'bindingFile'.
     */
    public java.lang.String getBindingFile()
    {
        return this._bindingFile;
    } //-- java.lang.String getBindingFile() 

    /**
     * Returns the value of field 'collection'.
     * 
     * @return the value of field 'collection'.
     */
    public org.exolab.castor.tests.framework.testDescriptor.types.CollectionType getCollection()
    {
        return this._collection;
    } //-- org.exolab.castor.tests.framework.testDescriptor.types.CollectionType getCollection() 

    /**
     * Returns the value of field 'property_File'.
     * 
     * @return the value of field 'property_File'.
     */
    public java.lang.String getProperty_File()
    {
        return this._property_File;
    } //-- java.lang.String getProperty_File() 

    /**
     * Returns the value of field 'root_Object'.
     * 
     * @return the value of field 'root_Object'.
     */
    public org.exolab.castor.tests.framework.testDescriptor.RootType getRoot_Object()
    {
        return this._root_Object;
    } //-- org.exolab.castor.tests.framework.testDescriptor.RootType getRoot_Object() 

    /**
     * Method getSchema
     * 
     * @param index
     */
    public java.lang.String getSchema(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _schemaList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (String)_schemaList.elementAt(index);
    } //-- java.lang.String getSchema(int) 

    /**
     * Method getSchema
     */
    public java.lang.String[] getSchema()
    {
        int size = _schemaList.size();
        java.lang.String[] mArray = new java.lang.String[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (String)_schemaList.elementAt(index);
        }
        return mArray;
    } //-- java.lang.String[] getSchema() 

    /**
     * Method getSchemaCount
     */
    public int getSchemaCount()
    {
        return _schemaList.size();
    } //-- int getSchemaCount() 

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
     * Method removeAllSchema
     */
    public void removeAllSchema()
    {
        _schemaList.removeAllElements();
    } //-- void removeAllSchema() 

    /**
     * Method removeAllUnitTestCase
     */
    public void removeAllUnitTestCase()
    {
        _unitTestCaseList.removeAllElements();
    } //-- void removeAllUnitTestCase() 

    /**
     * Method removeSchema
     * 
     * @param index
     */
    public java.lang.String removeSchema(int index)
    {
        java.lang.Object obj = _schemaList.elementAt(index);
        _schemaList.removeElementAt(index);
        return (String)obj;
    } //-- java.lang.String removeSchema(int) 

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
     * Sets the value of field 'bindingFile'.
     * 
     * @param bindingFile the value of field 'bindingFile'.
     */
    public void setBindingFile(java.lang.String bindingFile)
    {
        this._bindingFile = bindingFile;
    } //-- void setBindingFile(java.lang.String) 

    /**
     * Sets the value of field 'collection'.
     * 
     * @param collection the value of field 'collection'.
     */
    public void setCollection(org.exolab.castor.tests.framework.testDescriptor.types.CollectionType collection)
    {
        this._collection = collection;
    } //-- void setCollection(org.exolab.castor.tests.framework.testDescriptor.types.CollectionType) 

    /**
     * Sets the value of field 'property_File'.
     * 
     * @param property_File the value of field 'property_File'.
     */
    public void setProperty_File(java.lang.String property_File)
    {
        this._property_File = property_File;
    } //-- void setProperty_File(java.lang.String) 

    /**
     * Sets the value of field 'root_Object'.
     * 
     * @param root_Object the value of field 'root_Object'.
     */
    public void setRoot_Object(org.exolab.castor.tests.framework.testDescriptor.RootType root_Object)
    {
        this._root_Object = root_Object;
    } //-- void setRoot_Object(org.exolab.castor.tests.framework.testDescriptor.RootType) 

    /**
     * Method setSchema
     * 
     * @param index
     * @param vSchema
     */
    public void setSchema(int index, java.lang.String vSchema)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _schemaList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _schemaList.setElementAt(vSchema, index);
    } //-- void setSchema(int, java.lang.String) 

    /**
     * Method setSchema
     * 
     * @param schemaArray
     */
    public void setSchema(java.lang.String[] schemaArray)
    {
        //-- copy array
        _schemaList.removeAllElements();
        for (int i = 0; i < schemaArray.length; i++) {
            _schemaList.addElement(schemaArray[i]);
        }
    } //-- void setSchema(java.lang.String) 

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
     * Method unmarshalSourceGeneratorTest
     * 
     * @param reader
     */
    public static java.lang.Object unmarshalSourceGeneratorTest(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.exolab.castor.tests.framework.testDescriptor.SourceGeneratorTest) Unmarshaller.unmarshal(org.exolab.castor.tests.framework.testDescriptor.SourceGeneratorTest.class, reader);
    } //-- java.lang.Object unmarshalSourceGeneratorTest(java.io.Reader) 

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
