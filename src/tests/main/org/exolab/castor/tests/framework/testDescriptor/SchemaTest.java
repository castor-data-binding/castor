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
 * Tests a schema.
 *  
 * 
 * @version $Revision$ $Date$
 */
public class SchemaTest implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * A definition of a single Unit Test testcase.
     *  
     */
    private java.util.Vector _unitTestCaseList;


      //----------------/
     //- Constructors -/
    //----------------/

    public SchemaTest() 
     {
        super();
        this._unitTestCaseList = new java.util.Vector();
    } //-- org.exolab.castor.tests.framework.testDescriptor.SchemaTest()


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
     * org.exolab.castor.tests.framework.testDescriptor.SchemaTest
     */
    public static org.exolab.castor.tests.framework.testDescriptor.SchemaTest unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.exolab.castor.tests.framework.testDescriptor.SchemaTest) Unmarshaller.unmarshal(org.exolab.castor.tests.framework.testDescriptor.SchemaTest.class, reader);
    } //-- org.exolab.castor.tests.framework.testDescriptor.SchemaTest unmarshal(java.io.Reader) 

    /**
     */
    public void validate()
        throws org.exolab.castor.xml.ValidationException
    {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    } //-- void validate() 

}
