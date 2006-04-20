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


/**
 * Class TestDescriptorChoiceDescriptor.
 * 
 * @version $Revision$ $Date$
 */
public class TestDescriptorChoiceDescriptor extends org.exolab.castor.xml.util.XMLClassDescriptorImpl {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field nsPrefix
     */
    private java.lang.String nsPrefix;

    /**
     * Field nsURI
     */
    private java.lang.String nsURI;

    /**
     * Field xmlName
     */
    private java.lang.String xmlName;

    /**
     * Field identity
     */
    private org.exolab.castor.xml.XMLFieldDescriptor identity;


      //----------------/
     //- Constructors -/
    //----------------/

    public TestDescriptorChoiceDescriptor() {
        super();
        nsPrefix = "test";
        nsURI = "http://castor.exolab.org/Test";
        
        //-- set grouping compositor
        setCompositorAsChoice();
        org.exolab.castor.xml.util.XMLFieldDescriptorImpl  desc           = null;
        org.exolab.castor.xml.XMLFieldHandler              handler        = null;
        org.exolab.castor.xml.FieldValidator               fieldValidator = null;
        //-- initialize attribute descriptors
        
        //-- initialize element descriptors
        
        //-- _sourceGeneratorTest
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(org.exolab.castor.tests.framework.testDescriptor.SourceGeneratorTest.class, "_sourceGeneratorTest", "SourceGeneratorTest", org.exolab.castor.xml.NodeType.Element);
        handler = (new org.exolab.castor.xml.XMLFieldHandler() {
            public java.lang.Object getValue( java.lang.Object object ) 
                throws IllegalStateException
            {
                TestDescriptorChoice target = (TestDescriptorChoice) object;
                return target.getSourceGeneratorTest();
            }
            public void setValue( java.lang.Object object, java.lang.Object value) 
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    TestDescriptorChoice target = (TestDescriptorChoice) object;
                    target.setSourceGeneratorTest( (org.exolab.castor.tests.framework.testDescriptor.SourceGeneratorTest) value);
                }
                catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            public java.lang.Object newInstance( java.lang.Object parent ) {
                return new org.exolab.castor.tests.framework.testDescriptor.SourceGeneratorTest();
            }
        } );
        desc.setHandler(handler);
        desc.setNameSpaceURI("http://castor.exolab.org/Test");
        desc.setMultivalued(false);
        addFieldDescriptor(desc);
        
        //-- validation code for: _sourceGeneratorTest
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        { //-- local scope
        }
        desc.setValidator(fieldValidator);
        //-- _marshallingTest
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(org.exolab.castor.tests.framework.testDescriptor.MarshallingTest.class, "_marshallingTest", "MarshallingTest", org.exolab.castor.xml.NodeType.Element);
        handler = (new org.exolab.castor.xml.XMLFieldHandler() {
            public java.lang.Object getValue( java.lang.Object object ) 
                throws IllegalStateException
            {
                TestDescriptorChoice target = (TestDescriptorChoice) object;
                return target.getMarshallingTest();
            }
            public void setValue( java.lang.Object object, java.lang.Object value) 
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    TestDescriptorChoice target = (TestDescriptorChoice) object;
                    target.setMarshallingTest( (org.exolab.castor.tests.framework.testDescriptor.MarshallingTest) value);
                }
                catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            public java.lang.Object newInstance( java.lang.Object parent ) {
                return new org.exolab.castor.tests.framework.testDescriptor.MarshallingTest();
            }
        } );
        desc.setHandler(handler);
        desc.setNameSpaceURI("http://castor.exolab.org/Test");
        desc.setMultivalued(false);
        addFieldDescriptor(desc);
        
        //-- validation code for: _marshallingTest
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        { //-- local scope
        }
        desc.setValidator(fieldValidator);
        //-- _schemaTest
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(org.exolab.castor.tests.framework.testDescriptor.SchemaTest.class, "_schemaTest", "SchemaTest", org.exolab.castor.xml.NodeType.Element);
        handler = (new org.exolab.castor.xml.XMLFieldHandler() {
            public java.lang.Object getValue( java.lang.Object object ) 
                throws IllegalStateException
            {
                TestDescriptorChoice target = (TestDescriptorChoice) object;
                return target.getSchemaTest();
            }
            public void setValue( java.lang.Object object, java.lang.Object value) 
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    TestDescriptorChoice target = (TestDescriptorChoice) object;
                    target.setSchemaTest( (org.exolab.castor.tests.framework.testDescriptor.SchemaTest) value);
                }
                catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            public java.lang.Object newInstance( java.lang.Object parent ) {
                return new org.exolab.castor.tests.framework.testDescriptor.SchemaTest();
            }
        } );
        desc.setHandler(handler);
        desc.setNameSpaceURI("http://castor.exolab.org/Test");
        desc.setMultivalued(false);
        addFieldDescriptor(desc);
        
        //-- validation code for: _schemaTest
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        { //-- local scope
        }
        desc.setValidator(fieldValidator);
    } //-- org.exolab.castor.tests.framework.testDescriptor.TestDescriptorChoiceDescriptor()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method getAccessMode
     */
    public org.exolab.castor.mapping.AccessMode getAccessMode()
    {
        return null;
    } //-- org.exolab.castor.mapping.AccessMode getAccessMode() 

    /**
     * Method getExtends
     */
    public org.exolab.castor.mapping.ClassDescriptor getExtends()
    {
        return null;
    } //-- org.exolab.castor.mapping.ClassDescriptor getExtends() 

    /**
     * Method getIdentity
     */
    public org.exolab.castor.mapping.FieldDescriptor getIdentity()
    {
        return identity;
    } //-- org.exolab.castor.mapping.FieldDescriptor getIdentity() 

    /**
     * Method getJavaClass
     */
    public java.lang.Class getJavaClass()
    {
        return org.exolab.castor.tests.framework.testDescriptor.TestDescriptorChoice.class;
    } //-- java.lang.Class getJavaClass() 

    /**
     * Method getNameSpacePrefix
     */
    public java.lang.String getNameSpacePrefix()
    {
        return nsPrefix;
    } //-- java.lang.String getNameSpacePrefix() 

    /**
     * Method getNameSpaceURI
     */
    public java.lang.String getNameSpaceURI()
    {
        return nsURI;
    } //-- java.lang.String getNameSpaceURI() 

    /**
     * Method getValidator
     */
    public org.exolab.castor.xml.TypeValidator getValidator()
    {
        return this;
    } //-- org.exolab.castor.xml.TypeValidator getValidator() 

    /**
     * Method getXMLName
     */
    public java.lang.String getXMLName()
    {
        return xmlName;
    } //-- java.lang.String getXMLName() 

}
