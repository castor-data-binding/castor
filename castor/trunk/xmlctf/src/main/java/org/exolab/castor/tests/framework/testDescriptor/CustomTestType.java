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
 * This element specifies an optional user-provided test. A user-
 *  provided test consists of a test class name to be instantiated
 *  and a non-static method to call for the method.
 *  
 * 
 * @version $Revision$ $Date$
 */
public class CustomTestType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _testClass.
     */
    private java.lang.String _testClass;

    /**
     * Field _methods.
     */
    private org.exolab.castor.tests.framework.testDescriptor.Methods _methods;


      //----------------/
     //- Constructors -/
    //----------------/

    public CustomTestType() {
        super();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'methods'.
     * 
     * @return the value of field 'Methods'.
     */
    public org.exolab.castor.tests.framework.testDescriptor.Methods getMethods(
    ) {
        return this._methods;
    }

    /**
     * Returns the value of field 'testClass'.
     * 
     * @return the value of field 'TestClass'.
     */
    public java.lang.String getTestClass(
    ) {
        return this._testClass;
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
     * Sets the value of field 'methods'.
     * 
     * @param methods the value of field 'methods'.
     */
    public void setMethods(
            final org.exolab.castor.tests.framework.testDescriptor.Methods methods) {
        this._methods = methods;
    }

    /**
     * Sets the value of field 'testClass'.
     * 
     * @param testClass the value of field 'testClass'.
     */
    public void setTestClass(
            final java.lang.String testClass) {
        this._testClass = testClass;
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
     * org.exolab.castor.tests.framework.testDescriptor.CustomTestType
     */
    public static org.exolab.castor.tests.framework.testDescriptor.CustomTestType unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (org.exolab.castor.tests.framework.testDescriptor.CustomTestType) Unmarshaller.unmarshal(org.exolab.castor.tests.framework.testDescriptor.CustomTestType.class, reader);
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
