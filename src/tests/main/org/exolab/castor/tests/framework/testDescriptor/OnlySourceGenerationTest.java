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
 * Tests source generation only, and does not attempt to use the
 * generated code. While
 *  a SourceGeneratorTest is better because it is more thorough,
 * sometimes
 *  the only thing that requires testing is the code generation.
 *  
 * 
 * @version $Revision$ $Date$
 */
public class OnlySourceGenerationTest implements java.io.Serializable {


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
     * The data type to use in collections.
     *  
     */
    private org.exolab.castor.tests.framework.testDescriptor.types.CollectionType _collection = org.exolab.castor.tests.framework.testDescriptor.types.CollectionType.valueOf("vector");

    /**
     * Field _bindingFile
     */
    private java.lang.String _bindingFile;

    /**
     * A definition of a single Unit Test testcase.
     *  
     */
    private java.util.Vector _unitTestCaseList;


      //----------------/
     //- Constructors -/
    //----------------/

    public OnlySourceGenerationTest() 
     {
        super();
        this._schemaList = new java.util.Vector();
        setCollection(org.exolab.castor.tests.framework.testDescriptor.types.CollectionType.valueOf("vector"));
        this._unitTestCaseList = new java.util.Vector();
    } //-- org.exolab.castor.tests.framework.testDescriptor.OnlySourceGenerationTest()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * 
     * 
     * @param vSchema
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addSchema(java.lang.String vSchema)
        throws java.lang.IndexOutOfBoundsException
    {
        this._schemaList.addElement(vSchema);
    } //-- void addSchema(java.lang.String) 

    /**
     * 
     * 
     * @param index
     * @param vSchema
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addSchema(int index, java.lang.String vSchema)
        throws java.lang.IndexOutOfBoundsException
    {
        this._schemaList.add(index, vSchema);
    } //-- void addSchema(int, java.lang.String) 

    /**
     * 
     * 
     * @param vUnitTestCase
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addUnitTestCase(org.exolab.castor.tests.framework.testDescriptor.UnitTestCase vUnitTestCase)
        throws java.lang.IndexOutOfBoundsException
    {
        this._unitTestCaseList.addElement(vUnitTestCase);
    } //-- void addUnitTestCase(org.exolab.castor.tests.framework.testDescriptor.UnitTestCase) 

    /**
     * 
     * 
     * @param index
     * @param vUnitTestCase
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addUnitTestCase(int index, org.exolab.castor.tests.framework.testDescriptor.UnitTestCase vUnitTestCase)
        throws java.lang.IndexOutOfBoundsException
    {
        this._unitTestCaseList.add(index, vUnitTestCase);
    } //-- void addUnitTestCase(int, org.exolab.castor.tests.framework.testDescriptor.UnitTestCase) 

    /**
     * Method enumerateSchema
     * 
     * 
     * 
     * @return an Enumeration over all java.lang.String elements
     */
    public java.util.Enumeration enumerateSchema()
    {
        return this._schemaList.elements();
    } //-- java.util.Enumeration enumerateSchema() 

    /**
     * Method enumerateUnitTestCase
     * 
     * 
     * 
     * @return an Enumeration over all
     * org.exolab.castor.tests.framework.testDescriptor.UnitTestCase
     * elements
     */
    public java.util.Enumeration enumerateUnitTestCase()
    {
        return this._unitTestCaseList.elements();
    } //-- java.util.Enumeration enumerateUnitTestCase() 

    /**
     * Returns the value of field 'bindingFile'.
     * 
     * @return the value of field 'BindingFile'.
     */
    public java.lang.String getBindingFile()
    {
        return this._bindingFile;
    } //-- java.lang.String getBindingFile() 

    /**
     * Returns the value of field 'collection'. The field
     * 'collection' has the following description: The data type to
     * use in collections.
     *  
     * 
     * @return the value of field 'Collection'.
     */
    public org.exolab.castor.tests.framework.testDescriptor.types.CollectionType getCollection()
    {
        return this._collection;
    } //-- org.exolab.castor.tests.framework.testDescriptor.types.CollectionType getCollection() 

    /**
     * Returns the value of field 'property_File'.
     * 
     * @return the value of field 'Property_File'.
     */
    public java.lang.String getProperty_File()
    {
        return this._property_File;
    } //-- java.lang.String getProperty_File() 

    /**
     * Method getSchema
     * 
     * 
     * 
     * @param index
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     * @return the value of the java.lang.String at the given index
     */
    public java.lang.String getSchema(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        // check bounds for index
        if (index < 0 || index >= this._schemaList.size()) {
            throw new IndexOutOfBoundsException("getSchema: Index value '" + index + "' not in range [0.." + (this._schemaList.size() - 1) + "]");
        }
        
        return (String)_schemaList.get(index);
    } //-- java.lang.String getSchema(int) 

    /**
     * Method getSchema
     * 
     * 
     * 
     * @return this collection as an Array
     */
    public java.lang.String[] getSchema()
    {
        int size = this._schemaList.size();
        java.lang.String[] array = new java.lang.String[size];
        for (int index = 0; index < size; index++){
            array[index] = (String)_schemaList.get(index);
        }
        
        return array;
    } //-- java.lang.String[] getSchema() 

    /**
     * Method getSchemaCount
     * 
     * 
     * 
     * @return the size of this collection
     */
    public int getSchemaCount()
    {
        return this._schemaList.size();
    } //-- int getSchemaCount() 

    /**
     * Method getUnitTestCase
     * 
     * 
     * 
     * @param index
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     * @return the value of the
     * org.exolab.castor.tests.framework.testDescriptor.UnitTestCase
     * at the given index
     */
    public org.exolab.castor.tests.framework.testDescriptor.UnitTestCase getUnitTestCase(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        // check bounds for index
        if (index < 0 || index >= this._unitTestCaseList.size()) {
            throw new IndexOutOfBoundsException("getUnitTestCase: Index value '" + index + "' not in range [0.." + (this._unitTestCaseList.size() - 1) + "]");
        }
        
        return (org.exolab.castor.tests.framework.testDescriptor.UnitTestCase) _unitTestCaseList.get(index);
    } //-- org.exolab.castor.tests.framework.testDescriptor.UnitTestCase getUnitTestCase(int) 

    /**
     * Method getUnitTestCase
     * 
     * 
     * 
     * @return this collection as an Array
     */
    public org.exolab.castor.tests.framework.testDescriptor.UnitTestCase[] getUnitTestCase()
    {
        int size = this._unitTestCaseList.size();
        org.exolab.castor.tests.framework.testDescriptor.UnitTestCase[] array = new org.exolab.castor.tests.framework.testDescriptor.UnitTestCase[size];
        for (int index = 0; index < size; index++){
            array[index] = (org.exolab.castor.tests.framework.testDescriptor.UnitTestCase) _unitTestCaseList.get(index);
        }
        
        return array;
    } //-- org.exolab.castor.tests.framework.testDescriptor.UnitTestCase[] getUnitTestCase() 

    /**
     * Method getUnitTestCaseCount
     * 
     * 
     * 
     * @return the size of this collection
     */
    public int getUnitTestCaseCount()
    {
        return this._unitTestCaseList.size();
    } //-- int getUnitTestCaseCount() 

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
     */
    public void marshal(org.xml.sax.ContentHandler handler)
        throws java.io.IOException, org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        
        Marshaller.marshal(this, handler);
    } //-- void marshal(org.xml.sax.ContentHandler) 

    /**
     */
    public void removeAllSchema()
    {
        this._schemaList.clear();
    } //-- void removeAllSchema() 

    /**
     */
    public void removeAllUnitTestCase()
    {
        this._unitTestCaseList.clear();
    } //-- void removeAllUnitTestCase() 

    /**
     * Method removeSchema
     * 
     * 
     * 
     * @param vSchema
     * @return true if the object was removed from the collection.
     */
    public boolean removeSchema(java.lang.String vSchema)
    {
        boolean removed = _schemaList.remove(vSchema);
        return removed;
    } //-- boolean removeSchema(java.lang.String) 

    /**
     * Method removeSchemaAt
     * 
     * 
     * 
     * @param index
     * @return the element removed from the collection
     */
    public java.lang.String removeSchemaAt(int index)
    {
        Object obj = this._schemaList.remove(index);
        return (String)obj;
    } //-- java.lang.String removeSchemaAt(int) 

    /**
     * Method removeUnitTestCase
     * 
     * 
     * 
     * @param vUnitTestCase
     * @return true if the object was removed from the collection.
     */
    public boolean removeUnitTestCase(org.exolab.castor.tests.framework.testDescriptor.UnitTestCase vUnitTestCase)
    {
        boolean removed = _unitTestCaseList.remove(vUnitTestCase);
        return removed;
    } //-- boolean removeUnitTestCase(org.exolab.castor.tests.framework.testDescriptor.UnitTestCase) 

    /**
     * Method removeUnitTestCaseAt
     * 
     * 
     * 
     * @param index
     * @return the element removed from the collection
     */
    public org.exolab.castor.tests.framework.testDescriptor.UnitTestCase removeUnitTestCaseAt(int index)
    {
        Object obj = this._unitTestCaseList.remove(index);
        return (org.exolab.castor.tests.framework.testDescriptor.UnitTestCase) obj;
    } //-- org.exolab.castor.tests.framework.testDescriptor.UnitTestCase removeUnitTestCaseAt(int) 

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
     * Sets the value of field 'collection'. The field 'collection'
     * has the following description: The data type to use in
     * collections.
     *  
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
     * 
     * 
     * @param index
     * @param vSchema
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void setSchema(int index, java.lang.String vSchema)
        throws java.lang.IndexOutOfBoundsException
    {
        // check bounds for index
        if (index < 0 || index >= this._schemaList.size()) {
            throw new IndexOutOfBoundsException("setSchema: Index value '" + index + "' not in range [0.." + (this._schemaList.size() - 1) + "]");
        }
        
        this._schemaList.set(index, vSchema);
    } //-- void setSchema(int, java.lang.String) 

    /**
     * 
     * 
     * @param vSchemaArray
     */
    public void setSchema(java.lang.String[] vSchemaArray)
    {
        //-- copy array
        _schemaList.clear();
        
        for (int i = 0; i < vSchemaArray.length; i++) {
                this._schemaList.add(vSchemaArray[i]);
        }
    } //-- void setSchema(java.lang.String) 

    /**
     * 
     * 
     * @param index
     * @param vUnitTestCase
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void setUnitTestCase(int index, org.exolab.castor.tests.framework.testDescriptor.UnitTestCase vUnitTestCase)
        throws java.lang.IndexOutOfBoundsException
    {
        // check bounds for index
        if (index < 0 || index >= this._unitTestCaseList.size()) {
            throw new IndexOutOfBoundsException("setUnitTestCase: Index value '" + index + "' not in range [0.." + (this._unitTestCaseList.size() - 1) + "]");
        }
        
        this._unitTestCaseList.set(index, vUnitTestCase);
    } //-- void setUnitTestCase(int, org.exolab.castor.tests.framework.testDescriptor.UnitTestCase) 

    /**
     * 
     * 
     * @param vUnitTestCaseArray
     */
    public void setUnitTestCase(org.exolab.castor.tests.framework.testDescriptor.UnitTestCase[] vUnitTestCaseArray)
    {
        //-- copy array
        _unitTestCaseList.clear();
        
        for (int i = 0; i < vUnitTestCaseArray.length; i++) {
                this._unitTestCaseList.add(vUnitTestCaseArray[i]);
        }
    } //-- void setUnitTestCase(org.exolab.castor.tests.framework.testDescriptor.UnitTestCase) 

    /**
     * Method unmarshal
     * 
     * 
     * 
     * @param reader
     * @return the unmarshaled
     * org.exolab.castor.tests.framework.testDescriptor.OnlySourceGenerationTest
     */
    public static org.exolab.castor.tests.framework.testDescriptor.OnlySourceGenerationTest unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.exolab.castor.tests.framework.testDescriptor.OnlySourceGenerationTest) Unmarshaller.unmarshal(org.exolab.castor.tests.framework.testDescriptor.OnlySourceGenerationTest.class, reader);
    } //-- org.exolab.castor.tests.framework.testDescriptor.OnlySourceGenerationTest unmarshal(java.io.Reader) 

    /**
     */
    public void validate()
        throws org.exolab.castor.xml.ValidationException
    {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    } //-- void validate() 

}
