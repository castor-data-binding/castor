/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.1-M2</a>, using an XML
 * Schema.
 * $Id: SchemaTest.java 6767 2007-01-19 05:53:39Z ekuns $
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
 * @version $Revision: 6767 $ $Date$
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

    public SchemaTest() {
        super();
        this._unitTestCaseList = new java.util.Vector();
    }


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
    public void removeAllUnitTestCase(
    ) {
        this._unitTestCaseList.clear();
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
     * org.exolab.castor.tests.framework.testDescriptor.SchemaTest
     */
    public static org.exolab.castor.tests.framework.testDescriptor.SchemaTest unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (org.exolab.castor.tests.framework.testDescriptor.SchemaTest) Unmarshaller.unmarshal(org.exolab.castor.tests.framework.testDescriptor.SchemaTest.class, reader);
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
