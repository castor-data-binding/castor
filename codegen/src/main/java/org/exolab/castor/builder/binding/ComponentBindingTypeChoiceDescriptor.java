/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.0.5</a>, using an XML
 * Schema.
 * $Id$
 */

package org.exolab.castor.builder.binding;

/**
 * Class ComponentBindingTypeChoiceDescriptor.
 * 
 * @version $Revision$ $Date$
 */
public class ComponentBindingTypeChoiceDescriptor extends org.exolab.castor.xml.util.XMLClassDescriptorImpl {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field elementDefinition.
     */
    private boolean elementDefinition;

    /**
     * Field nsPrefix.
     */
    private java.lang.String nsPrefix;

    /**
     * Field nsURI.
     */
    private java.lang.String nsURI;

    /**
     * Field xmlName.
     */
    private java.lang.String xmlName;

    /**
     * Field identity.
     */
    private org.exolab.castor.xml.XMLFieldDescriptor identity;


      //----------------/
     //- Constructors -/
    //----------------/

    public ComponentBindingTypeChoiceDescriptor() 
     {
        super();
        nsURI = "http://www.castor.org/SourceGenerator/Binding";
        elementDefinition = false;
        
        //-- set grouping compositor
        setCompositorAsChoice();
        org.exolab.castor.xml.util.XMLFieldDescriptorImpl  desc           = null;
        org.exolab.castor.mapping.FieldHandler             handler        = null;
        org.exolab.castor.xml.FieldValidator               fieldValidator = null;
        //-- initialize attribute descriptors
        
        //-- initialize element descriptors
        
        //-- _javaClass
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(org.exolab.castor.builder.binding.ClassType.class, "_javaClass", "java-class", org.exolab.castor.xml.NodeType.Element);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {
            public java.lang.Object getValue(java.lang.Object object) 
                throws IllegalStateException
            {
                ComponentBindingTypeChoice target = (ComponentBindingTypeChoice) object;
                return target.getJavaClass();
            }
            public void setValue(java.lang.Object object, java.lang.Object value) 
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    ComponentBindingTypeChoice target = (ComponentBindingTypeChoice) object;
                    target.setJavaClass((org.exolab.castor.builder.binding.ClassType) value);
                }
                catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            public java.lang.Object newInstance(java.lang.Object parent) {
                return new org.exolab.castor.builder.binding.ClassType();
            }
        };
        desc.setHandler(handler);
        desc.setNameSpaceURI("http://www.castor.org/SourceGenerator/Binding");
        desc.setRequired(true);
        desc.setMultivalued(false);
        addFieldDescriptor(desc);
        
        //-- validation code for: _javaClass
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        fieldValidator.setMinOccurs(1);
        { //-- local scope
        }
        desc.setValidator(fieldValidator);
        //-- _interface
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(org.exolab.castor.builder.binding.Interface.class, "_interface", "interface", org.exolab.castor.xml.NodeType.Element);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {
            public java.lang.Object getValue(java.lang.Object object) 
                throws IllegalStateException
            {
                ComponentBindingTypeChoice target = (ComponentBindingTypeChoice) object;
                return target.getInterface();
            }
            public void setValue(java.lang.Object object, java.lang.Object value) 
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    ComponentBindingTypeChoice target = (ComponentBindingTypeChoice) object;
                    target.setInterface((org.exolab.castor.builder.binding.Interface) value);
                }
                catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            public java.lang.Object newInstance(java.lang.Object parent) {
                return new org.exolab.castor.builder.binding.Interface();
            }
        };
        desc.setHandler(handler);
        desc.setNameSpaceURI("http://www.castor.org/SourceGenerator/Binding");
        desc.setRequired(true);
        desc.setMultivalued(false);
        addFieldDescriptor(desc);
        
        //-- validation code for: _interface
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        fieldValidator.setMinOccurs(1);
        { //-- local scope
        }
        desc.setValidator(fieldValidator);
        //-- _member
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(org.exolab.castor.builder.binding.FieldType.class, "_member", "member", org.exolab.castor.xml.NodeType.Element);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {
            public java.lang.Object getValue(java.lang.Object object) 
                throws IllegalStateException
            {
                ComponentBindingTypeChoice target = (ComponentBindingTypeChoice) object;
                return target.getMember();
            }
            public void setValue(java.lang.Object object, java.lang.Object value) 
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    ComponentBindingTypeChoice target = (ComponentBindingTypeChoice) object;
                    target.setMember((org.exolab.castor.builder.binding.FieldType) value);
                }
                catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            public java.lang.Object newInstance(java.lang.Object parent) {
                return new org.exolab.castor.builder.binding.FieldType();
            }
        };
        desc.setHandler(handler);
        desc.setNameSpaceURI("http://www.castor.org/SourceGenerator/Binding");
        desc.setRequired(true);
        desc.setMultivalued(false);
        addFieldDescriptor(desc);
        
        //-- validation code for: _member
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        fieldValidator.setMinOccurs(1);
        { //-- local scope
        }
        desc.setValidator(fieldValidator);
        //-- _enumDef
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(org.exolab.castor.builder.binding.EnumBindingType.class, "_enumDef", "enum-def", org.exolab.castor.xml.NodeType.Element);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {
            public java.lang.Object getValue(java.lang.Object object) 
                throws IllegalStateException
            {
                ComponentBindingTypeChoice target = (ComponentBindingTypeChoice) object;
                return target.getEnumDef();
            }
            public void setValue(java.lang.Object object, java.lang.Object value) 
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    ComponentBindingTypeChoice target = (ComponentBindingTypeChoice) object;
                    target.setEnumDef((org.exolab.castor.builder.binding.EnumBindingType) value);
                }
                catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            public java.lang.Object newInstance(java.lang.Object parent) {
                return new org.exolab.castor.builder.binding.EnumBindingType();
            }
        };
        desc.setHandler(handler);
        desc.setNameSpaceURI("http://www.castor.org/SourceGenerator/Binding");
        desc.setRequired(true);
        desc.setMultivalued(false);
        addFieldDescriptor(desc);
        
        //-- validation code for: _enumDef
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        fieldValidator.setMinOccurs(1);
        { //-- local scope
        }
        desc.setValidator(fieldValidator);
    } //-- org.exolab.castor.builder.binding.ComponentBindingTypeChoiceDescriptor()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method getAccessMode.
     * 
     * @return the access mode specified for this class.
     */
    public org.exolab.castor.mapping.AccessMode getAccessMode()
    {
        return null;
    } //-- org.exolab.castor.mapping.AccessMode getAccessMode() 

    /**
     * Method getExtends.
     * 
     * @return the class descriptor of the class extended by this
     * class.
     */
    public org.exolab.castor.mapping.ClassDescriptor getExtends()
    {
        return null;
    } //-- org.exolab.castor.mapping.ClassDescriptor getExtends() 

    /**
     * Method getIdentity.
     * 
     * @return the identity field, null if this class has no
     * identity.
     */
    public org.exolab.castor.mapping.FieldDescriptor getIdentity()
    {
        return identity;
    } //-- org.exolab.castor.mapping.FieldDescriptor getIdentity() 

    /**
     * Method getJavaClass.
     * 
     * @return the Java class represented by this descriptor.
     */
    public java.lang.Class getJavaClass()
    {
        return org.exolab.castor.builder.binding.ComponentBindingTypeChoice.class;
    } //-- java.lang.Class getJavaClass() 

    /**
     * Method getNameSpacePrefix.
     * 
     * @return the namespace prefix to use when marshalling as XML.
     */
    public java.lang.String getNameSpacePrefix()
    {
        return nsPrefix;
    } //-- java.lang.String getNameSpacePrefix() 

    /**
     * Method getNameSpaceURI.
     * 
     * @return the namespace URI used when marshalling and
     * unmarshalling as XML.
     */
    public java.lang.String getNameSpaceURI()
    {
        return nsURI;
    } //-- java.lang.String getNameSpaceURI() 

    /**
     * Method getValidator.
     * 
     * @return a specific validator for the class described by this
     * ClassDescriptor.
     */
    public org.exolab.castor.xml.TypeValidator getValidator()
    {
        return this;
    } //-- org.exolab.castor.xml.TypeValidator getValidator() 

    /**
     * Method getXMLName.
     * 
     * @return the XML Name for the Class being described.
     */
    public java.lang.String getXMLName()
    {
        return xmlName;
    } //-- java.lang.String getXMLName() 

    /**
     * Method isElementDefinition.
     * 
     * @return true if XML schema definition of this Class is that
     * of a global
     * element or element with anonymous type definition.
     */
    public boolean isElementDefinition()
    {
        return elementDefinition;
    } //-- boolean isElementDefinition() 

}
