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
 * Class TestDescriptorChoice.
 * 
 * @version $Revision$ $Date$
 */
public class TestDescriptorChoice implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Tests source generation and then tests the generated source,
     * testing
     *  both marshaling and unmarshaling.
     *  
     */
    private org.exolab.castor.tests.framework.testDescriptor.SourceGeneratorTest _sourceGeneratorTest;

    /**
     * Test marshaling.
     *  
     */
    private org.exolab.castor.tests.framework.testDescriptor.MarshallingTest _marshallingTest;

    /**
     * Tests a schema.
     *  
     */
    private org.exolab.castor.tests.framework.testDescriptor.SchemaTest _schemaTest;

    /**
     * Tests source generation only, and does not attempt to use
     * the generated code. While
     *  a SourceGeneratorTest is better because it is more
     * thorough, sometimes
     *  the only thing that requires testing is the code
     * generation.
     *  
     */
    private org.exolab.castor.tests.framework.testDescriptor.OnlySourceGenerationTest _onlySourceGenerationTest;


      //----------------/
     //- Constructors -/
    //----------------/

    public TestDescriptorChoice() 
     {
        super();
    } //-- org.exolab.castor.tests.framework.testDescriptor.TestDescriptorChoice()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'marshallingTest'. The field
     * 'marshallingTest' has the following description: Test
     * marshaling.
     *  
     * 
     * @return the value of field 'MarshallingTest'.
     */
    public org.exolab.castor.tests.framework.testDescriptor.MarshallingTest getMarshallingTest()
    {
        return this._marshallingTest;
    } //-- org.exolab.castor.tests.framework.testDescriptor.MarshallingTest getMarshallingTest() 

    /**
     * Returns the value of field 'onlySourceGenerationTest'. The
     * field 'onlySourceGenerationTest' has the following
     * description: Tests source generation only, and does not
     * attempt to use the generated code. While
     *  a SourceGeneratorTest is better because it is more
     * thorough, sometimes
     *  the only thing that requires testing is the code
     * generation.
     *  
     * 
     * @return the value of field 'OnlySourceGenerationTest'.
     */
    public org.exolab.castor.tests.framework.testDescriptor.OnlySourceGenerationTest getOnlySourceGenerationTest()
    {
        return this._onlySourceGenerationTest;
    } //-- org.exolab.castor.tests.framework.testDescriptor.OnlySourceGenerationTest getOnlySourceGenerationTest() 

    /**
     * Returns the value of field 'schemaTest'. The field
     * 'schemaTest' has the following description: Tests a schema.
     *  
     * 
     * @return the value of field 'SchemaTest'.
     */
    public org.exolab.castor.tests.framework.testDescriptor.SchemaTest getSchemaTest()
    {
        return this._schemaTest;
    } //-- org.exolab.castor.tests.framework.testDescriptor.SchemaTest getSchemaTest() 

    /**
     * Returns the value of field 'sourceGeneratorTest'. The field
     * 'sourceGeneratorTest' has the following description: Tests
     * source generation and then tests the generated source,
     * testing
     *  both marshaling and unmarshaling.
     *  
     * 
     * @return the value of field 'SourceGeneratorTest'.
     */
    public org.exolab.castor.tests.framework.testDescriptor.SourceGeneratorTest getSourceGeneratorTest()
    {
        return this._sourceGeneratorTest;
    } //-- org.exolab.castor.tests.framework.testDescriptor.SourceGeneratorTest getSourceGeneratorTest() 

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
     * Sets the value of field 'marshallingTest'. The field
     * 'marshallingTest' has the following description: Test
     * marshaling.
     *  
     * 
     * @param marshallingTest the value of field 'marshallingTest'.
     */
    public void setMarshallingTest(org.exolab.castor.tests.framework.testDescriptor.MarshallingTest marshallingTest)
    {
        this._marshallingTest = marshallingTest;
    } //-- void setMarshallingTest(org.exolab.castor.tests.framework.testDescriptor.MarshallingTest) 

    /**
     * Sets the value of field 'onlySourceGenerationTest'. The
     * field 'onlySourceGenerationTest' has the following
     * description: Tests source generation only, and does not
     * attempt to use the generated code. While
     *  a SourceGeneratorTest is better because it is more
     * thorough, sometimes
     *  the only thing that requires testing is the code
     * generation.
     *  
     * 
     * @param onlySourceGenerationTest the value of field
     * 'onlySourceGenerationTest'.
     */
    public void setOnlySourceGenerationTest(org.exolab.castor.tests.framework.testDescriptor.OnlySourceGenerationTest onlySourceGenerationTest)
    {
        this._onlySourceGenerationTest = onlySourceGenerationTest;
    } //-- void setOnlySourceGenerationTest(org.exolab.castor.tests.framework.testDescriptor.OnlySourceGenerationTest) 

    /**
     * Sets the value of field 'schemaTest'. The field 'schemaTest'
     * has the following description: Tests a schema.
     *  
     * 
     * @param schemaTest the value of field 'schemaTest'.
     */
    public void setSchemaTest(org.exolab.castor.tests.framework.testDescriptor.SchemaTest schemaTest)
    {
        this._schemaTest = schemaTest;
    } //-- void setSchemaTest(org.exolab.castor.tests.framework.testDescriptor.SchemaTest) 

    /**
     * Sets the value of field 'sourceGeneratorTest'. The field
     * 'sourceGeneratorTest' has the following description: Tests
     * source generation and then tests the generated source,
     * testing
     *  both marshaling and unmarshaling.
     *  
     * 
     * @param sourceGeneratorTest the value of field
     * 'sourceGeneratorTest'.
     */
    public void setSourceGeneratorTest(org.exolab.castor.tests.framework.testDescriptor.SourceGeneratorTest sourceGeneratorTest)
    {
        this._sourceGeneratorTest = sourceGeneratorTest;
    } //-- void setSourceGeneratorTest(org.exolab.castor.tests.framework.testDescriptor.SourceGeneratorTest) 

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
     * org.exolab.castor.tests.framework.testDescriptor.TestDescriptorChoice
     */
    public static org.exolab.castor.tests.framework.testDescriptor.TestDescriptorChoice unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.exolab.castor.tests.framework.testDescriptor.TestDescriptorChoice) Unmarshaller.unmarshal(org.exolab.castor.tests.framework.testDescriptor.TestDescriptorChoice.class, reader);
    } //-- org.exolab.castor.tests.framework.testDescriptor.TestDescriptorChoice unmarshal(java.io.Reader) 

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
