/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.1-M2</a>, using an XML
 * Schema.
 * $Id$
 */

package org.exolab.castor.tests.framework.testDescriptor.descriptors;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import org.exolab.castor.tests.framework.testDescriptor.UnitTestCase;

/**
 * Class UnitTestCaseDescriptor.
 * 
 * @version $Revision$ $Date$
 */
public class UnitTestCaseDescriptor extends org.exolab.castor.xml.util.XMLClassDescriptorImpl {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _elementDefinition.
     */
    private boolean _elementDefinition;

    /**
     * Field _nsPrefix.
     */
    private java.lang.String _nsPrefix;

    /**
     * Field _nsURI.
     */
    private java.lang.String _nsURI;

    /**
     * Field _xmlName.
     */
    private java.lang.String _xmlName;

    /**
     * Field _identity.
     */
    private org.exolab.castor.xml.XMLFieldDescriptor _identity;


      //----------------/
     //- Constructors -/
    //----------------/

    public UnitTestCaseDescriptor() {
        super();
        _nsURI = "http://castor.exolab.org/Test";
        _xmlName = "UnitTestCase";
        _elementDefinition = true;
        
        //-- set grouping compositor
        setCompositorAsSequence();
        org.exolab.castor.xml.util.XMLFieldDescriptorImpl  desc           = null;
        org.exolab.castor.mapping.FieldHandler             handler        = null;
        org.exolab.castor.xml.FieldValidator               fieldValidator = null;
        //-- initialize attribute descriptors
        
        //-- initialize element descriptors
        
        //-- _name
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(java.lang.String.class, "_name", "Name", org.exolab.castor.xml.NodeType.Element);
        desc.setImmutable(true);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {
            public java.lang.Object getValue( java.lang.Object object ) 
                throws IllegalStateException
            {
                UnitTestCase target = (UnitTestCase) object;
                return target.getName();
            }
            public void setValue( java.lang.Object object, java.lang.Object value) 
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    UnitTestCase target = (UnitTestCase) object;
                    target.setName( (java.lang.String) value);
                } catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            public java.lang.Object newInstance(java.lang.Object parent) {
                return null;
            }
        };
        desc.setHandler(handler);
        desc.setNameSpaceURI("http://castor.exolab.org/Test");
        desc.setMultivalued(false);
        addFieldDescriptor(desc);
        
        //-- validation code for: _name
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        { //-- local scope
            org.exolab.castor.xml.validators.StringValidator typeValidator;
            typeValidator = new org.exolab.castor.xml.validators.StringValidator();
            fieldValidator.setValidator(typeValidator);
            typeValidator.setWhiteSpace("preserve");
        }
        desc.setValidator(fieldValidator);
        //-- _unitTestCaseChoice
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(org.exolab.castor.tests.framework.testDescriptor.UnitTestCaseChoice.class, "_unitTestCaseChoice", "-error-if-this-is-used-", org.exolab.castor.xml.NodeType.Element);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {
            public java.lang.Object getValue( java.lang.Object object ) 
                throws IllegalStateException
            {
                UnitTestCase target = (UnitTestCase) object;
                return target.getUnitTestCaseChoice();
            }
            public void setValue( java.lang.Object object, java.lang.Object value) 
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    UnitTestCase target = (UnitTestCase) object;
                    target.setUnitTestCaseChoice( (org.exolab.castor.tests.framework.testDescriptor.UnitTestCaseChoice) value);
                } catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            public java.lang.Object newInstance(java.lang.Object parent) {
                return new org.exolab.castor.tests.framework.testDescriptor.UnitTestCaseChoice();
            }
        };
        desc.setHandler(handler);
        desc.setContainer(true);
        desc.setClassDescriptor(new org.exolab.castor.tests.framework.testDescriptor.descriptors.UnitTestCaseChoiceDescriptor());
        desc.setNameSpaceURI("http://castor.exolab.org/Test");
        desc.setMultivalued(false);
        addFieldDescriptor(desc);
        
        //-- validation code for: _unitTestCaseChoice
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        { //-- local scope
        }
        desc.setValidator(fieldValidator);
        //-- _customTest
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(org.exolab.castor.tests.framework.testDescriptor.CustomTest.class, "_customTest", "CustomTest", org.exolab.castor.xml.NodeType.Element);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {
            public java.lang.Object getValue( java.lang.Object object ) 
                throws IllegalStateException
            {
                UnitTestCase target = (UnitTestCase) object;
                return target.getCustomTest();
            }
            public void setValue( java.lang.Object object, java.lang.Object value) 
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    UnitTestCase target = (UnitTestCase) object;
                    target.setCustomTest( (org.exolab.castor.tests.framework.testDescriptor.CustomTest) value);
                } catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            public java.lang.Object newInstance(java.lang.Object parent) {
                return new org.exolab.castor.tests.framework.testDescriptor.CustomTest();
            }
        };
        desc.setHandler(handler);
        desc.setNameSpaceURI("http://castor.exolab.org/Test");
        desc.setMultivalued(false);
        addFieldDescriptor(desc);
        
        //-- validation code for: _customTest
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        { //-- local scope
        }
        desc.setValidator(fieldValidator);
        //-- _configuration
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(org.exolab.castor.tests.framework.testDescriptor.Configuration.class, "_configuration", "Configuration", org.exolab.castor.xml.NodeType.Element);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {
            public java.lang.Object getValue( java.lang.Object object ) 
                throws IllegalStateException
            {
                UnitTestCase target = (UnitTestCase) object;
                return target.getConfiguration();
            }
            public void setValue( java.lang.Object object, java.lang.Object value) 
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    UnitTestCase target = (UnitTestCase) object;
                    target.setConfiguration( (org.exolab.castor.tests.framework.testDescriptor.Configuration) value);
                } catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            public java.lang.Object newInstance(java.lang.Object parent) {
                return new org.exolab.castor.tests.framework.testDescriptor.Configuration();
            }
        };
        desc.setHandler(handler);
        desc.setNameSpaceURI("http://castor.exolab.org/Test");
        desc.setMultivalued(false);
        addFieldDescriptor(desc);
        
        //-- validation code for: _configuration
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        { //-- local scope
        }
        desc.setValidator(fieldValidator);
        //-- _input
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(java.lang.String.class, "_input", "Input", org.exolab.castor.xml.NodeType.Element);
        desc.setImmutable(true);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {
            public java.lang.Object getValue( java.lang.Object object ) 
                throws IllegalStateException
            {
                UnitTestCase target = (UnitTestCase) object;
                return target.getInput();
            }
            public void setValue( java.lang.Object object, java.lang.Object value) 
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    UnitTestCase target = (UnitTestCase) object;
                    target.setInput( (java.lang.String) value);
                } catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            public java.lang.Object newInstance(java.lang.Object parent) {
                return null;
            }
        };
        desc.setHandler(handler);
        desc.setNameSpaceURI("http://castor.exolab.org/Test");
        desc.setMultivalued(false);
        addFieldDescriptor(desc);
        
        //-- validation code for: _input
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        { //-- local scope
            org.exolab.castor.xml.validators.StringValidator typeValidator;
            typeValidator = new org.exolab.castor.xml.validators.StringValidator();
            fieldValidator.setValidator(typeValidator);
            typeValidator.setWhiteSpace("preserve");
        }
        desc.setValidator(fieldValidator);
        //-- _goldFile
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(java.lang.String.class, "_goldFile", "GoldFile", org.exolab.castor.xml.NodeType.Element);
        desc.setImmutable(true);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {
            public java.lang.Object getValue( java.lang.Object object ) 
                throws IllegalStateException
            {
                UnitTestCase target = (UnitTestCase) object;
                return target.getGoldFile();
            }
            public void setValue( java.lang.Object object, java.lang.Object value) 
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    UnitTestCase target = (UnitTestCase) object;
                    target.setGoldFile( (java.lang.String) value);
                } catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            public java.lang.Object newInstance(java.lang.Object parent) {
                return null;
            }
        };
        desc.setHandler(handler);
        desc.setNameSpaceURI("http://castor.exolab.org/Test");
        desc.setMultivalued(false);
        addFieldDescriptor(desc);
        
        //-- validation code for: _goldFile
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        { //-- local scope
            org.exolab.castor.xml.validators.StringValidator typeValidator;
            typeValidator = new org.exolab.castor.xml.validators.StringValidator();
            fieldValidator.setValidator(typeValidator);
            typeValidator.setWhiteSpace("preserve");
        }
        desc.setValidator(fieldValidator);
        //-- _objectBuilder
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(java.lang.String.class, "_objectBuilder", "ObjectBuilder", org.exolab.castor.xml.NodeType.Element);
        desc.setImmutable(true);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {
            public java.lang.Object getValue( java.lang.Object object ) 
                throws IllegalStateException
            {
                UnitTestCase target = (UnitTestCase) object;
                return target.getObjectBuilder();
            }
            public void setValue( java.lang.Object object, java.lang.Object value) 
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    UnitTestCase target = (UnitTestCase) object;
                    target.setObjectBuilder( (java.lang.String) value);
                } catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            public java.lang.Object newInstance(java.lang.Object parent) {
                return null;
            }
        };
        desc.setHandler(handler);
        desc.setNameSpaceURI("http://castor.exolab.org/Test");
        desc.setMultivalued(false);
        addFieldDescriptor(desc);
        
        //-- validation code for: _objectBuilder
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        { //-- local scope
            org.exolab.castor.xml.validators.StringValidator typeValidator;
            typeValidator = new org.exolab.castor.xml.validators.StringValidator();
            fieldValidator.setValidator(typeValidator);
            typeValidator.setWhiteSpace("preserve");
        }
        desc.setValidator(fieldValidator);
        //-- _failure
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(org.exolab.castor.tests.framework.testDescriptor.Failure.class, "_failure", "Failure", org.exolab.castor.xml.NodeType.Element);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {
            public java.lang.Object getValue( java.lang.Object object ) 
                throws IllegalStateException
            {
                UnitTestCase target = (UnitTestCase) object;
                return target.getFailure();
            }
            public void setValue( java.lang.Object object, java.lang.Object value) 
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    UnitTestCase target = (UnitTestCase) object;
                    target.setFailure( (org.exolab.castor.tests.framework.testDescriptor.Failure) value);
                } catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            public java.lang.Object newInstance(java.lang.Object parent) {
                return new org.exolab.castor.tests.framework.testDescriptor.Failure();
            }
        };
        desc.setHandler(handler);
        desc.setNameSpaceURI("http://castor.exolab.org/Test");
        desc.setMultivalued(false);
        addFieldDescriptor(desc);
        
        //-- validation code for: _failure
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        { //-- local scope
        }
        desc.setValidator(fieldValidator);
        //-- _skip
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(java.lang.Boolean.TYPE, "_skip", "Skip", org.exolab.castor.xml.NodeType.Element);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {
            public java.lang.Object getValue( java.lang.Object object ) 
                throws IllegalStateException
            {
                UnitTestCase target = (UnitTestCase) object;
                if (!target.hasSkip()) { return null; }
                return (target.getSkip() ? java.lang.Boolean.TRUE : java.lang.Boolean.FALSE);
            }
            public void setValue( java.lang.Object object, java.lang.Object value) 
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    UnitTestCase target = (UnitTestCase) object;
                    // if null, use delete method for optional primitives 
                    if (value == null) {
                        target.deleteSkip();
                        return;
                    }
                    target.setSkip( ((java.lang.Boolean) value).booleanValue());
                } catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            public java.lang.Object newInstance(java.lang.Object parent) {
                return null;
            }
        };
        desc.setHandler(handler);
        desc.setNameSpaceURI("http://castor.exolab.org/Test");
        desc.setMultivalued(false);
        addFieldDescriptor(desc);
        
        //-- validation code for: _skip
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        { //-- local scope
            org.exolab.castor.xml.validators.BooleanValidator typeValidator;
            typeValidator = new org.exolab.castor.xml.validators.BooleanValidator();
            fieldValidator.setValidator(typeValidator);
        }
        desc.setValidator(fieldValidator);
        //-- _listener
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(org.exolab.castor.tests.framework.testDescriptor.Listener.class, "_listener", "Listener", org.exolab.castor.xml.NodeType.Element);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {
            public java.lang.Object getValue( java.lang.Object object ) 
                throws IllegalStateException
            {
                UnitTestCase target = (UnitTestCase) object;
                return target.getListener();
            }
            public void setValue( java.lang.Object object, java.lang.Object value) 
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    UnitTestCase target = (UnitTestCase) object;
                    target.setListener( (org.exolab.castor.tests.framework.testDescriptor.Listener) value);
                } catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            public java.lang.Object newInstance(java.lang.Object parent) {
                return new org.exolab.castor.tests.framework.testDescriptor.Listener();
            }
        };
        desc.setHandler(handler);
        desc.setNameSpaceURI("http://castor.exolab.org/Test");
        desc.setMultivalued(false);
        addFieldDescriptor(desc);
        
        //-- validation code for: _listener
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        { //-- local scope
        }
        desc.setValidator(fieldValidator);
        //-- _schemaDifferencesList
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(org.exolab.castor.tests.framework.testDescriptor.SchemaDifferences.class, "_schemaDifferencesList", "SchemaDifferences", org.exolab.castor.xml.NodeType.Element);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {
            public java.lang.Object getValue( java.lang.Object object ) 
                throws IllegalStateException
            {
                UnitTestCase target = (UnitTestCase) object;
                return target.getSchemaDifferences();
            }
            public void setValue( java.lang.Object object, java.lang.Object value) 
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    UnitTestCase target = (UnitTestCase) object;
                    target.addSchemaDifferences( (org.exolab.castor.tests.framework.testDescriptor.SchemaDifferences) value);
                } catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            public void resetValue(Object object) throws IllegalStateException, IllegalArgumentException {
                try {
                    UnitTestCase target = (UnitTestCase) object;
                    target.removeAllSchemaDifferences();
                } catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            public java.lang.Object newInstance(java.lang.Object parent) {
                return new org.exolab.castor.tests.framework.testDescriptor.SchemaDifferences();
            }
        };
        desc.setHandler(handler);
        desc.setNameSpaceURI("http://castor.exolab.org/Test");
        desc.setMultivalued(true);
        addFieldDescriptor(desc);
        
        //-- validation code for: _schemaDifferencesList
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        fieldValidator.setMinOccurs(0);
        fieldValidator.setMaxOccurs(2);
        { //-- local scope
        }
        desc.setValidator(fieldValidator);
        //-- _commentList
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(java.lang.String.class, "_commentList", "Comment", org.exolab.castor.xml.NodeType.Element);
        desc.setImmutable(true);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {
            public java.lang.Object getValue( java.lang.Object object ) 
                throws IllegalStateException
            {
                UnitTestCase target = (UnitTestCase) object;
                return target.getComment();
            }
            public void setValue( java.lang.Object object, java.lang.Object value) 
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    UnitTestCase target = (UnitTestCase) object;
                    target.addComment( (java.lang.String) value);
                } catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            public void resetValue(Object object) throws IllegalStateException, IllegalArgumentException {
                try {
                    UnitTestCase target = (UnitTestCase) object;
                    target.removeAllComment();
                } catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            public java.lang.Object newInstance(java.lang.Object parent) {
                return null;
            }
        };
        desc.setHandler(handler);
        desc.setNameSpaceURI("http://castor.exolab.org/Test");
        desc.setMultivalued(true);
        addFieldDescriptor(desc);
        
        //-- validation code for: _commentList
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        fieldValidator.setMinOccurs(0);
        { //-- local scope
            org.exolab.castor.xml.validators.StringValidator typeValidator;
            typeValidator = new org.exolab.castor.xml.validators.StringValidator();
            fieldValidator.setValidator(typeValidator);
            typeValidator.setWhiteSpace("preserve");
        }
        desc.setValidator(fieldValidator);
        //-- _javaSourceVersion
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(java.lang.Float.TYPE, "_javaSourceVersion", "JavaSourceVersion", org.exolab.castor.xml.NodeType.Element);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {
            public java.lang.Object getValue( java.lang.Object object ) 
                throws IllegalStateException
            {
                UnitTestCase target = (UnitTestCase) object;
                if (!target.hasJavaSourceVersion()) { return null; }
                return new java.lang.Float(target.getJavaSourceVersion());
            }
            public void setValue( java.lang.Object object, java.lang.Object value) 
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    UnitTestCase target = (UnitTestCase) object;
                    // if null, use delete method for optional primitives 
                    if (value == null) {
                        target.deleteJavaSourceVersion();
                        return;
                    }
                    target.setJavaSourceVersion( ((java.lang.Float) value).floatValue());
                } catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            public java.lang.Object newInstance(java.lang.Object parent) {
                return null;
            }
        };
        desc.setHandler(handler);
        desc.setNameSpaceURI("http://castor.exolab.org/Test");
        desc.setMultivalued(false);
        addFieldDescriptor(desc);
        
        //-- validation code for: _javaSourceVersion
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        { //-- local scope
            org.exolab.castor.xml.validators.FloatValidator typeValidator;
            typeValidator = new org.exolab.castor.xml.validators.FloatValidator();
            fieldValidator.setValidator(typeValidator);
            typeValidator.setMinInclusive((float) -3.4028235E38);
            typeValidator.setMaxInclusive((float) 3.4028235E38);
        }
        desc.setValidator(fieldValidator);
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method getAccessMode.
     * 
     * @return the access mode specified for this class.
     */
    public org.exolab.castor.mapping.AccessMode getAccessMode(
    ) {
        return null;
    }

    /**
     * Method getIdentity.
     * 
     * @return the identity field, null if this class has no
     * identity.
     */
    public org.exolab.castor.mapping.FieldDescriptor getIdentity(
    ) {
        return _identity;
    }

    /**
     * Method getJavaClass.
     * 
     * @return the Java class represented by this descriptor.
     */
    public java.lang.Class getJavaClass(
    ) {
        return org.exolab.castor.tests.framework.testDescriptor.UnitTestCase.class;
    }

    /**
     * Method getNameSpacePrefix.
     * 
     * @return the namespace prefix to use when marshaling as XML.
     */
    public java.lang.String getNameSpacePrefix(
    ) {
        return _nsPrefix;
    }

    /**
     * Method getNameSpaceURI.
     * 
     * @return the namespace URI used when marshaling and
     * unmarshaling as XML.
     */
    public java.lang.String getNameSpaceURI(
    ) {
        return _nsURI;
    }

    /**
     * Method getValidator.
     * 
     * @return a specific validator for the class described by this
     * ClassDescriptor.
     */
    public org.exolab.castor.xml.TypeValidator getValidator(
    ) {
        return this;
    }

    /**
     * Method getXMLName.
     * 
     * @return the XML Name for the Class being described.
     */
    public java.lang.String getXMLName(
    ) {
        return _xmlName;
    }

    /**
     * Method isElementDefinition.
     * 
     * @return true if XML schema definition of this Class is that
     * of a global
     * element or element with anonymous type definition.
     */
    public boolean isElementDefinition(
    ) {
        return _elementDefinition;
    }

}
