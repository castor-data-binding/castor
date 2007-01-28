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
 * Tests source generation and then tests the generated source,
 * testing
 *  both marshaling and unmarshaling.
 *  
 * 
 * @version $Revision$ $Date$
 */
public class SourceGeneratorTest implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _schemaList.
     */
    private java.util.Vector _schemaList;

    /**
     * Field _property_File.
     */
    private java.lang.String _property_File;

    /**
     * The data type to use in collections.
     *  
     */
    private org.exolab.castor.tests.framework.testDescriptor.types.CollectionType _collection = org.exolab.castor.tests.framework.testDescriptor.types.CollectionType.valueOf("vector");

    /**
     * Field _bindingFile.
     */
    private java.lang.String _bindingFile;

    /**
     * Field _package.
     */
    private java.lang.String _package;

    /**
     * The qualified name of the root Object.
     *  TODO: define a pattern to describe a Java quailified name.
     *  
     */
    private org.exolab.castor.tests.framework.testDescriptor.Root_Object _root_Object;

    /**
     * A definition of a single Unit Test testcase.
     *  
     */
    private java.util.Vector _unitTestCaseList;


      //----------------/
     //- Constructors -/
    //----------------/

    public SourceGeneratorTest() {
        super();
        this._schemaList = new java.util.Vector();
        setCollection(org.exolab.castor.tests.framework.testDescriptor.types.CollectionType.valueOf("vector"));
        this._unitTestCaseList = new java.util.Vector();
    }


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
    public void addSchema(
            final java.lang.String vSchema)
    throws java.lang.IndexOutOfBoundsException {
        this._schemaList.addElement(vSchema);
    }

    /**
     * 
     * 
     * @param index
     * @param vSchema
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addSchema(
            final int index,
            final java.lang.String vSchema)
    throws java.lang.IndexOutOfBoundsException {
        this._schemaList.add(index, vSchema);
    }

    /**
     * 
     * 
     * @param vUnitTestCase
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addUnitTestCase(
            final org.exolab.castor.tests.framework.testDescriptor.UnitTestCase vUnitTestCase)
    throws java.lang.IndexOutOfBoundsException {
        this._unitTestCaseList.addElement(vUnitTestCase);
    }

    /**
     * 
     * 
     * @param index
     * @param vUnitTestCase
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addUnitTestCase(
            final int index,
            final org.exolab.castor.tests.framework.testDescriptor.UnitTestCase vUnitTestCase)
    throws java.lang.IndexOutOfBoundsException {
        this._unitTestCaseList.add(index, vUnitTestCase);
    }

    /**
     * Method enumerateSchema.
     * 
     * @return an Enumeration over all java.lang.String elements
     */
    public java.util.Enumeration enumerateSchema(
    ) {
        return this._schemaList.elements();
    }

    /**
     * Method enumerateUnitTestCase.
     * 
     * @return an Enumeration over all
     * org.exolab.castor.tests.framework.testDescriptor.UnitTestCase
     * elements
     */
    public java.util.Enumeration enumerateUnitTestCase(
    ) {
        return this._unitTestCaseList.elements();
    }

    /**
     * Returns the value of field 'bindingFile'.
     * 
     * @return the value of field 'BindingFile'.
     */
    public java.lang.String getBindingFile(
    ) {
        return this._bindingFile;
    }

    /**
     * Returns the value of field 'collection'. The field
     * 'collection' has the following description: The data type to
     * use in collections.
     *  
     * 
     * @return the value of field 'Collection'.
     */
    public org.exolab.castor.tests.framework.testDescriptor.types.CollectionType getCollection(
    ) {
        return this._collection;
    }

    /**
     * Returns the value of field 'package'.
     * 
     * @return the value of field 'Package'.
     */
    public java.lang.String getPackage(
    ) {
        return this._package;
    }

    /**
     * Returns the value of field 'property_File'.
     * 
     * @return the value of field 'Property_File'.
     */
    public java.lang.String getProperty_File(
    ) {
        return this._property_File;
    }

    /**
     * Returns the value of field 'root_Object'. The field
     * 'root_Object' has the following description: The qualified
     * name of the root Object.
     *  TODO: define a pattern to describe a Java quailified name.
     *  
     * 
     * @return the value of field 'Root_Object'.
     */
    public org.exolab.castor.tests.framework.testDescriptor.Root_Object getRoot_Object(
    ) {
        return this._root_Object;
    }

    /**
     * Method getSchema.
     * 
     * @param index
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     * @return the value of the java.lang.String at the given index
     */
    public java.lang.String getSchema(
            final int index)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._schemaList.size()) {
            throw new IndexOutOfBoundsException("getSchema: Index value '" + index + "' not in range [0.." + (this._schemaList.size() - 1) + "]");
        }
        
        return (java.lang.String) _schemaList.get(index);
    }

    /**
     * Method getSchema.Returns the contents of the collection in
     * an Array.  <p>Note:  Just in case the collection contents
     * are changing in another thread, we pass a 0-length Array of
     * the correct type into the API call.  This way we <i>know</i>
     * that the Array returned is of exactly the correct length.
     * 
     * @return this collection as an Array
     */
    public java.lang.String[] getSchema(
    ) {
        java.lang.String[] array = new java.lang.String[0];
        return (java.lang.String[]) this._schemaList.toArray(array);
    }

    /**
     * Method getSchemaCount.
     * 
     * @return the size of this collection
     */
    public int getSchemaCount(
    ) {
        return this._schemaList.size();
    }

    /**
     * Method getUnitTestCase.
     * 
     * @param index
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     * @return the value of the
     * org.exolab.castor.tests.framework.testDescriptor.UnitTestCase
     * at the given index
     */
    public org.exolab.castor.tests.framework.testDescriptor.UnitTestCase getUnitTestCase(
            final int index)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._unitTestCaseList.size()) {
            throw new IndexOutOfBoundsException("getUnitTestCase: Index value '" + index + "' not in range [0.." + (this._unitTestCaseList.size() - 1) + "]");
        }
        
        return (org.exolab.castor.tests.framework.testDescriptor.UnitTestCase) _unitTestCaseList.get(index);
    }

    /**
     * Method getUnitTestCase.Returns the contents of the
     * collection in an Array.  <p>Note:  Just in case the
     * collection contents are changing in another thread, we pass
     * a 0-length Array of the correct type into the API call. 
     * This way we <i>know</i> that the Array returned is of
     * exactly the correct length.
     * 
     * @return this collection as an Array
     */
    public org.exolab.castor.tests.framework.testDescriptor.UnitTestCase[] getUnitTestCase(
    ) {
        org.exolab.castor.tests.framework.testDescriptor.UnitTestCase[] array = new org.exolab.castor.tests.framework.testDescriptor.UnitTestCase[0];
        return (org.exolab.castor.tests.framework.testDescriptor.UnitTestCase[]) this._unitTestCaseList.toArray(array);
    }

    /**
     * Method getUnitTestCaseCount.
     * 
     * @return the size of this collection
     */
    public int getUnitTestCaseCount(
    ) {
        return this._unitTestCaseList.size();
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
    public void removeAllSchema(
    ) {
        this._schemaList.clear();
    }

    /**
     */
    public void removeAllUnitTestCase(
    ) {
        this._unitTestCaseList.clear();
    }

    /**
     * Method removeSchema.
     * 
     * @param vSchema
     * @return true if the object was removed from the collection.
     */
    public boolean removeSchema(
            final java.lang.String vSchema) {
        boolean removed = _schemaList.remove(vSchema);
        return removed;
    }

    /**
     * Method removeSchemaAt.
     * 
     * @param index
     * @return the element removed from the collection
     */
    public java.lang.String removeSchemaAt(
            final int index) {
        java.lang.Object obj = this._schemaList.remove(index);
        return (java.lang.String) obj;
    }

    /**
     * Method removeUnitTestCase.
     * 
     * @param vUnitTestCase
     * @return true if the object was removed from the collection.
     */
    public boolean removeUnitTestCase(
            final org.exolab.castor.tests.framework.testDescriptor.UnitTestCase vUnitTestCase) {
        boolean removed = _unitTestCaseList.remove(vUnitTestCase);
        return removed;
    }

    /**
     * Method removeUnitTestCaseAt.
     * 
     * @param index
     * @return the element removed from the collection
     */
    public org.exolab.castor.tests.framework.testDescriptor.UnitTestCase removeUnitTestCaseAt(
            final int index) {
        java.lang.Object obj = this._unitTestCaseList.remove(index);
        return (org.exolab.castor.tests.framework.testDescriptor.UnitTestCase) obj;
    }

    /**
     * Sets the value of field 'bindingFile'.
     * 
     * @param bindingFile the value of field 'bindingFile'.
     */
    public void setBindingFile(
            final java.lang.String bindingFile) {
        this._bindingFile = bindingFile;
    }

    /**
     * Sets the value of field 'collection'. The field 'collection'
     * has the following description: The data type to use in
     * collections.
     *  
     * 
     * @param collection the value of field 'collection'.
     */
    public void setCollection(
            final org.exolab.castor.tests.framework.testDescriptor.types.CollectionType collection) {
        this._collection = collection;
    }

    /**
     * Sets the value of field 'package'.
     * 
     * @param _package
     * @param package the value of field 'package'.
     */
    public void setPackage(
            final java.lang.String _package) {
        this._package = _package;
    }

    /**
     * Sets the value of field 'property_File'.
     * 
     * @param property_File the value of field 'property_File'.
     */
    public void setProperty_File(
            final java.lang.String property_File) {
        this._property_File = property_File;
    }

    /**
     * Sets the value of field 'root_Object'. The field
     * 'root_Object' has the following description: The qualified
     * name of the root Object.
     *  TODO: define a pattern to describe a Java quailified name.
     *  
     * 
     * @param root_Object the value of field 'root_Object'.
     */
    public void setRoot_Object(
            final org.exolab.castor.tests.framework.testDescriptor.Root_Object root_Object) {
        this._root_Object = root_Object;
    }

    /**
     * 
     * 
     * @param index
     * @param vSchema
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void setSchema(
            final int index,
            final java.lang.String vSchema)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._schemaList.size()) {
            throw new IndexOutOfBoundsException("setSchema: Index value '" + index + "' not in range [0.." + (this._schemaList.size() - 1) + "]");
        }
        
        this._schemaList.set(index, vSchema);
    }

    /**
     * 
     * 
     * @param vSchemaArray
     */
    public void setSchema(
            final java.lang.String[] vSchemaArray) {
        //-- copy array
        _schemaList.clear();
        
        for (int i = 0; i < vSchemaArray.length; i++) {
                this._schemaList.add(vSchemaArray[i]);
        }
    }

    /**
     * 
     * 
     * @param index
     * @param vUnitTestCase
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void setUnitTestCase(
            final int index,
            final org.exolab.castor.tests.framework.testDescriptor.UnitTestCase vUnitTestCase)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._unitTestCaseList.size()) {
            throw new IndexOutOfBoundsException("setUnitTestCase: Index value '" + index + "' not in range [0.." + (this._unitTestCaseList.size() - 1) + "]");
        }
        
        this._unitTestCaseList.set(index, vUnitTestCase);
    }

    /**
     * 
     * 
     * @param vUnitTestCaseArray
     */
    public void setUnitTestCase(
            final org.exolab.castor.tests.framework.testDescriptor.UnitTestCase[] vUnitTestCaseArray) {
        //-- copy array
        _unitTestCaseList.clear();
        
        for (int i = 0; i < vUnitTestCaseArray.length; i++) {
                this._unitTestCaseList.add(vUnitTestCaseArray[i]);
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
     * org.exolab.castor.tests.framework.testDescriptor.SourceGeneratorTest
     */
    public static org.exolab.castor.tests.framework.testDescriptor.SourceGeneratorTest unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (org.exolab.castor.tests.framework.testDescriptor.SourceGeneratorTest) Unmarshaller.unmarshal(org.exolab.castor.tests.framework.testDescriptor.SourceGeneratorTest.class, reader);
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
