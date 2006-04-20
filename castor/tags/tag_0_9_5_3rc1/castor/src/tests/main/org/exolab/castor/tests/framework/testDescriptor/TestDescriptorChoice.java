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

import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.ContentHandler;

/**
 * Class TestDescriptorChoice.
 * 
 * @version $Revision$ $Date$
 */
public class TestDescriptorChoice implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _sourceGeneratorTest
     */
    private org.exolab.castor.tests.framework.testDescriptor.SourceGeneratorTest _sourceGeneratorTest;

    /**
     * Field _marshallingTest
     */
    private org.exolab.castor.tests.framework.testDescriptor.MarshallingTest _marshallingTest;

    /**
     * Field _schemaTest
     */
    private org.exolab.castor.tests.framework.testDescriptor.SchemaTest _schemaTest;


      //----------------/
     //- Constructors -/
    //----------------/

    public TestDescriptorChoice() {
        super();
    } //-- org.exolab.castor.tests.framework.testDescriptor.TestDescriptorChoice()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'marshallingTest'.
     * 
     * @return the value of field 'marshallingTest'.
     */
    public org.exolab.castor.tests.framework.testDescriptor.MarshallingTest getMarshallingTest()
    {
        return this._marshallingTest;
    } //-- org.exolab.castor.tests.framework.testDescriptor.MarshallingTest getMarshallingTest() 

    /**
     * Returns the value of field 'schemaTest'.
     * 
     * @return the value of field 'schemaTest'.
     */
    public org.exolab.castor.tests.framework.testDescriptor.SchemaTest getSchemaTest()
    {
        return this._schemaTest;
    } //-- org.exolab.castor.tests.framework.testDescriptor.SchemaTest getSchemaTest() 

    /**
     * Returns the value of field 'sourceGeneratorTest'.
     * 
     * @return the value of field 'sourceGeneratorTest'.
     */
    public org.exolab.castor.tests.framework.testDescriptor.SourceGeneratorTest getSourceGeneratorTest()
    {
        return this._sourceGeneratorTest;
    } //-- org.exolab.castor.tests.framework.testDescriptor.SourceGeneratorTest getSourceGeneratorTest() 

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
     * Sets the value of field 'marshallingTest'.
     * 
     * @param marshallingTest the value of field 'marshallingTest'.
     */
    public void setMarshallingTest(org.exolab.castor.tests.framework.testDescriptor.MarshallingTest marshallingTest)
    {
        this._marshallingTest = marshallingTest;
    } //-- void setMarshallingTest(org.exolab.castor.tests.framework.testDescriptor.MarshallingTest) 

    /**
     * Sets the value of field 'schemaTest'.
     * 
     * @param schemaTest the value of field 'schemaTest'.
     */
    public void setSchemaTest(org.exolab.castor.tests.framework.testDescriptor.SchemaTest schemaTest)
    {
        this._schemaTest = schemaTest;
    } //-- void setSchemaTest(org.exolab.castor.tests.framework.testDescriptor.SchemaTest) 

    /**
     * Sets the value of field 'sourceGeneratorTest'.
     * 
     * @param sourceGeneratorTest the value of field
     * 'sourceGeneratorTest'.
     */
    public void setSourceGeneratorTest(org.exolab.castor.tests.framework.testDescriptor.SourceGeneratorTest sourceGeneratorTest)
    {
        this._sourceGeneratorTest = sourceGeneratorTest;
    } //-- void setSourceGeneratorTest(org.exolab.castor.tests.framework.testDescriptor.SourceGeneratorTest) 

    /**
     * Method unmarshalTestDescriptorChoice
     * 
     * @param reader
     */
    public static java.lang.Object unmarshalTestDescriptorChoice(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.exolab.castor.tests.framework.testDescriptor.TestDescriptorChoice) Unmarshaller.unmarshal(org.exolab.castor.tests.framework.testDescriptor.TestDescriptorChoice.class, reader);
    } //-- java.lang.Object unmarshalTestDescriptorChoice(java.io.Reader) 

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
