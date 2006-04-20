/*
 * This class was automatically generated with 
 * <a href="http://castor.exolab.org">Castor 0.9.3.21+</a>, using
 * an XML Schema.
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
import org.exolab.castor.xml.*;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.ContentHandler;

/**
 * 
 * 
 * @version $Revision$ $Date$
**/
public class TestDescriptorChoice implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    private SourceGeneratorTest _sourceGeneratorTest;

    private MarshallingTest _marshallingTest;

    private SchemaTest _schemaTest;


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
    **/
    public MarshallingTest getMarshallingTest()
    {
        return this._marshallingTest;
    } //-- MarshallingTest getMarshallingTest() 

    /**
     * Returns the value of field 'schemaTest'.
     * 
     * @return the value of field 'schemaTest'.
    **/
    public SchemaTest getSchemaTest()
    {
        return this._schemaTest;
    } //-- SchemaTest getSchemaTest() 

    /**
     * Returns the value of field 'sourceGeneratorTest'.
     * 
     * @return the value of field 'sourceGeneratorTest'.
    **/
    public SourceGeneratorTest getSourceGeneratorTest()
    {
        return this._sourceGeneratorTest;
    } //-- SourceGeneratorTest getSourceGeneratorTest() 

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
     * 
     * @param handler
    **/
    public void marshal(org.xml.sax.ContentHandler handler)
        throws java.io.IOException, org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        
        Marshaller.marshal(this, handler);
    } //-- void marshal(org.xml.sax.ContentHandler) 

    /**
     * Sets the value of field 'marshallingTest'.
     * 
     * @param marshallingTest the value of field 'marshallingTest'.
    **/
    public void setMarshallingTest(MarshallingTest marshallingTest)
    {
        this._marshallingTest = marshallingTest;
    } //-- void setMarshallingTest(MarshallingTest) 

    /**
     * Sets the value of field 'schemaTest'.
     * 
     * @param schemaTest the value of field 'schemaTest'.
    **/
    public void setSchemaTest(SchemaTest schemaTest)
    {
        this._schemaTest = schemaTest;
    } //-- void setSchemaTest(SchemaTest) 

    /**
     * Sets the value of field 'sourceGeneratorTest'.
     * 
     * @param sourceGeneratorTest the value of field
     * 'sourceGeneratorTest'.
    **/
    public void setSourceGeneratorTest(SourceGeneratorTest sourceGeneratorTest)
    {
        this._sourceGeneratorTest = sourceGeneratorTest;
    } //-- void setSourceGeneratorTest(SourceGeneratorTest) 

    /**
     * 
     * 
     * @param reader
    **/
    public static org.exolab.castor.tests.framework.testDescriptor.TestDescriptorChoice unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.exolab.castor.tests.framework.testDescriptor.TestDescriptorChoice) Unmarshaller.unmarshal(org.exolab.castor.tests.framework.testDescriptor.TestDescriptorChoice.class, reader);
    } //-- org.exolab.castor.tests.framework.testDescriptor.TestDescriptorChoice unmarshal(java.io.Reader) 

    /**
    **/
    public void validate()
        throws org.exolab.castor.xml.ValidationException
    {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    } //-- void validate() 

}
