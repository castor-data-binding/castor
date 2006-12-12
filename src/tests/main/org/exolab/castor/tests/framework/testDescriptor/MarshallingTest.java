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
 * Test marshaling.
 *  
 * 
 * @version $Revision$ $Date$
 */
public class MarshallingTest implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * The qualified name of the root Object.
     *  TODO: define a pattern to describe a Java quailified name.
     *  
     */
    private org.exolab.castor.tests.framework.testDescriptor.Root_Object _root_Object;

    /**
     */
    private org.exolab.castor.tests.framework.testDescriptor.Configuration _configuration;

    /**
     * A definition of a single Unit Test testcase.
     *  
     */
    private java.util.Vector _unitTestCaseList;


      //----------------/
     //- Constructors -/
    //----------------/

    public MarshallingTest() 
     {
        super();
        this._unitTestCaseList = new java.util.Vector();
    } //-- org.exolab.castor.tests.framework.testDescriptor.MarshallingTest()


      //-----------/
     //- Methods -/
    //-----------/

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
     * Returns the value of field 'configuration'.
     * 
     * @return the value of field 'Configuration'.
     */
    public org.exolab.castor.tests.framework.testDescriptor.Configuration getConfiguration()
    {
        return this._configuration;
    } //-- org.exolab.castor.tests.framework.testDescriptor.Configuration getConfiguration() 

    /**
     * Returns the value of field 'root_Object'. The field
     * 'root_Object' has the following description: The qualified
     * name of the root Object.
     *  TODO: define a pattern to describe a Java quailified name.
     *  
     * 
     * @return the value of field 'Root_Object'.
     */
    public org.exolab.castor.tests.framework.testDescriptor.Root_Object getRoot_Object()
    {
        return this._root_Object;
    } //-- org.exolab.castor.tests.framework.testDescriptor.Root_Object getRoot_Object() 

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
        java.util.Iterator iter = _unitTestCaseList.iterator();
        for (int index = 0; index < size; index++){
            array[index] = (org.exolab.castor.tests.framework.testDescriptor.UnitTestCase) iter.next();
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
    public void removeAllUnitTestCase()
    {
        this._unitTestCaseList.clear();
    } //-- void removeAllUnitTestCase() 

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
     * Sets the value of field 'configuration'.
     * 
     * @param configuration the value of field 'configuration'.
     */
    public void setConfiguration(org.exolab.castor.tests.framework.testDescriptor.Configuration configuration)
    {
        this._configuration = configuration;
    } //-- void setConfiguration(org.exolab.castor.tests.framework.testDescriptor.Configuration) 

    /**
     * Sets the value of field 'root_Object'. The field
     * 'root_Object' has the following description: The qualified
     * name of the root Object.
     *  TODO: define a pattern to describe a Java quailified name.
     *  
     * 
     * @param root_Object the value of field 'root_Object'.
     */
    public void setRoot_Object(org.exolab.castor.tests.framework.testDescriptor.Root_Object root_Object)
    {
        this._root_Object = root_Object;
    } //-- void setRoot_Object(org.exolab.castor.tests.framework.testDescriptor.Root_Object) 

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
     * @throws org.exolab.castor.xml.MarshalException if object is
     * null or if any SAXException is thrown during marshaling
     * @throws org.exolab.castor.xml.ValidationException if this
     * object is an invalid instance according to the schema
     * @return the unmarshaled
     * org.exolab.castor.tests.framework.testDescriptor.MarshallingTest
     */
    public static org.exolab.castor.tests.framework.testDescriptor.MarshallingTest unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.exolab.castor.tests.framework.testDescriptor.MarshallingTest) Unmarshaller.unmarshal(org.exolab.castor.tests.framework.testDescriptor.MarshallingTest.class, reader);
    } //-- org.exolab.castor.tests.framework.testDescriptor.MarshallingTest unmarshal(java.io.Reader) 

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
