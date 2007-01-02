/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.0.5</a>, using an XML
 * Schema.
 * $Id$
 */

package org.exolab.castor.builder.binding;

/**
 * Class ComponentBindingTypeDescriptor.
 * 
 * @version $Revision$ $Date$
 */
public class ComponentBindingTypeDescriptor extends org.exolab.castor.xml.util.XMLClassDescriptorImpl {


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

    public ComponentBindingTypeDescriptor() 
     {
        super();
        nsURI = "http://www.castor.org/SourceGenerator/Binding";
        xmlName = "componentBindingType";
        elementDefinition = false;
        
        //-- set grouping compositor
        setCompositorAsSequence();
        org.exolab.castor.xml.util.XMLFieldDescriptorImpl  desc           = null;
        org.exolab.castor.mapping.FieldHandler             handler        = null;
        org.exolab.castor.xml.FieldValidator               fieldValidator = null;
        //-- initialize attribute descriptors
        
        //-- _name
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(java.lang.String.class, "_name", "name", org.exolab.castor.xml.NodeType.Attribute);
        desc.setImmutable(true);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {
            public java.lang.Object getValue(final java.lang.Object object) 
                throws IllegalStateException
            {
                ComponentBindingType target = (ComponentBindingType) object;
                return target.getName();
            }
            public void setValue(final java.lang.Object object, final java.lang.Object value) 
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    ComponentBindingType target = (ComponentBindingType) object;
                    target.setName((java.lang.String) value);
                }
                catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            public java.lang.Object newInstance(final java.lang.Object parent) {
                return null;
            }
        };
        desc.setHandler(handler);
        desc.setRequired(true);
        desc.setMultivalued(false);
        addFieldDescriptor(desc);
        
        //-- validation code for: _name
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        fieldValidator.setMinOccurs(1);
        { //-- local scope
            org.exolab.castor.xml.validators.StringValidator typeValidator = new org.exolab.castor.xml.validators.StringValidator();
            typeValidator.setWhiteSpace("preserve");
            fieldValidator.setValidator(typeValidator);
        }
        desc.setValidator(fieldValidator);
        //-- initialize element descriptors
        
        //-- _componentBindingTypeChoice
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(org.exolab.castor.builder.binding.ComponentBindingTypeChoice.class, "_componentBindingTypeChoice", "-error-if-this-is-used-", org.exolab.castor.xml.NodeType.Element);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {
            public java.lang.Object getValue(final java.lang.Object object) 
                throws IllegalStateException
            {
                ComponentBindingType target = (ComponentBindingType) object;
                return target.getComponentBindingTypeChoice();
            }
            public void setValue(final java.lang.Object object, final java.lang.Object value) 
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    ComponentBindingType target = (ComponentBindingType) object;
                    target.setComponentBindingTypeChoice((org.exolab.castor.builder.binding.ComponentBindingTypeChoice) value);
                }
                catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            public java.lang.Object newInstance(final java.lang.Object parent) {
                return new org.exolab.castor.builder.binding.ComponentBindingTypeChoice();
            }
        };
        desc.setHandler(handler);
        desc.setContainer(true);
        desc.setClassDescriptor(new org.exolab.castor.builder.binding.ComponentBindingTypeChoiceDescriptor());
        desc.setNameSpaceURI("http://www.castor.org/SourceGenerator/Binding");
        desc.setRequired(true);
        desc.setMultivalued(false);
        addFieldDescriptor(desc);
        
        //-- validation code for: _componentBindingTypeChoice
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        fieldValidator.setMinOccurs(1);
        { //-- local scope
        }
        desc.setValidator(fieldValidator);
        //-- _elementBindingList
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(org.exolab.castor.builder.binding.ComponentBindingType.class, "_elementBindingList", "elementBinding", org.exolab.castor.xml.NodeType.Element);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {
            public java.lang.Object getValue(final java.lang.Object object) 
                throws IllegalStateException
            {
                ComponentBindingType target = (ComponentBindingType) object;
                return target.getElementBinding();
            }
            public void setValue(final java.lang.Object object, final java.lang.Object value) 
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    ComponentBindingType target = (ComponentBindingType) object;
                    target.addElementBinding((org.exolab.castor.builder.binding.ComponentBindingType) value);
                }
                catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            public void resetValue(final Object object) throws IllegalStateException, IllegalArgumentException {
                try {
                    ComponentBindingType target = (ComponentBindingType) object;
                    target.removeAllElementBinding();
                } catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            public java.lang.Object newInstance(final java.lang.Object parent) {
                return new org.exolab.castor.builder.binding.ComponentBindingType();
            }
        };
        desc.setHandler(handler);
        desc.setNameSpaceURI("http://www.castor.org/SourceGenerator/Binding");
        desc.setMultivalued(true);
        addFieldDescriptor(desc);
        
        //-- validation code for: _elementBindingList
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        fieldValidator.setMinOccurs(0);
        { //-- local scope
        }
        desc.setValidator(fieldValidator);
        //-- _attributeBindingList
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(org.exolab.castor.builder.binding.ComponentBindingType.class, "_attributeBindingList", "attributeBinding", org.exolab.castor.xml.NodeType.Element);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {
            public java.lang.Object getValue(final java.lang.Object object) 
                throws IllegalStateException
            {
                ComponentBindingType target = (ComponentBindingType) object;
                return target.getAttributeBinding();
            }
            public void setValue(final java.lang.Object object, final java.lang.Object value) 
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    ComponentBindingType target = (ComponentBindingType) object;
                    target.addAttributeBinding((org.exolab.castor.builder.binding.ComponentBindingType) value);
                }
                catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            public void resetValue(final Object object) throws IllegalStateException, IllegalArgumentException {
                try {
                    ComponentBindingType target = (ComponentBindingType) object;
                    target.removeAllAttributeBinding();
                } catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            public java.lang.Object newInstance(final java.lang.Object parent) {
                return new org.exolab.castor.builder.binding.ComponentBindingType();
            }
        };
        desc.setHandler(handler);
        desc.setNameSpaceURI("http://www.castor.org/SourceGenerator/Binding");
        desc.setMultivalued(true);
        addFieldDescriptor(desc);
        
        //-- validation code for: _attributeBindingList
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        fieldValidator.setMinOccurs(0);
        { //-- local scope
        }
        desc.setValidator(fieldValidator);
        //-- _complexTypeBindingList
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(org.exolab.castor.builder.binding.ComponentBindingType.class, "_complexTypeBindingList", "complexTypeBinding", org.exolab.castor.xml.NodeType.Element);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {
            public java.lang.Object getValue(final java.lang.Object object) 
                throws IllegalStateException
            {
                ComponentBindingType target = (ComponentBindingType) object;
                return target.getComplexTypeBinding();
            }
            public void setValue(final java.lang.Object object, final java.lang.Object value) 
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    ComponentBindingType target = (ComponentBindingType) object;
                    target.addComplexTypeBinding((org.exolab.castor.builder.binding.ComponentBindingType) value);
                }
                catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            public void resetValue(final Object object) throws IllegalStateException, IllegalArgumentException {
                try {
                    ComponentBindingType target = (ComponentBindingType) object;
                    target.removeAllComplexTypeBinding();
                } catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            public java.lang.Object newInstance(final java.lang.Object parent) {
                return new org.exolab.castor.builder.binding.ComponentBindingType();
            }
        };
        desc.setHandler(handler);
        desc.setNameSpaceURI("http://www.castor.org/SourceGenerator/Binding");
        desc.setMultivalued(true);
        addFieldDescriptor(desc);
        
        //-- validation code for: _complexTypeBindingList
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        fieldValidator.setMinOccurs(0);
        { //-- local scope
        }
        desc.setValidator(fieldValidator);
        //-- _groupBindingList
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(org.exolab.castor.builder.binding.ComponentBindingType.class, "_groupBindingList", "groupBinding", org.exolab.castor.xml.NodeType.Element);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {
            public java.lang.Object getValue(final java.lang.Object object) 
                throws IllegalStateException
            {
                ComponentBindingType target = (ComponentBindingType) object;
                return target.getGroupBinding();
            }
            public void setValue(final java.lang.Object object, final java.lang.Object value) 
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    ComponentBindingType target = (ComponentBindingType) object;
                    target.addGroupBinding((org.exolab.castor.builder.binding.ComponentBindingType) value);
                }
                catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            public void resetValue(final Object object) throws IllegalStateException, IllegalArgumentException {
                try {
                    ComponentBindingType target = (ComponentBindingType) object;
                    target.removeAllGroupBinding();
                } catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            public java.lang.Object newInstance(final java.lang.Object parent) {
                return new org.exolab.castor.builder.binding.ComponentBindingType();
            }
        };
        desc.setHandler(handler);
        desc.setNameSpaceURI("http://www.castor.org/SourceGenerator/Binding");
        desc.setMultivalued(true);
        addFieldDescriptor(desc);
        
        //-- validation code for: _groupBindingList
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        fieldValidator.setMinOccurs(0);
        { //-- local scope
        }
        desc.setValidator(fieldValidator);
        //-- _enumBindingList
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(org.exolab.castor.builder.binding.ComponentBindingType.class, "_enumBindingList", "enumBinding", org.exolab.castor.xml.NodeType.Element);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {
            public java.lang.Object getValue(final java.lang.Object object) 
                throws IllegalStateException
            {
                ComponentBindingType target = (ComponentBindingType) object;
                return target.getEnumBinding();
            }
            public void setValue(final java.lang.Object object, final java.lang.Object value) 
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    ComponentBindingType target = (ComponentBindingType) object;
                    target.addEnumBinding((org.exolab.castor.builder.binding.ComponentBindingType) value);
                }
                catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            public void resetValue(final Object object) throws IllegalStateException, IllegalArgumentException {
                try {
                    ComponentBindingType target = (ComponentBindingType) object;
                    target.removeAllEnumBinding();
                } catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            public java.lang.Object newInstance(final java.lang.Object parent) {
                return new org.exolab.castor.builder.binding.ComponentBindingType();
            }
        };
        desc.setHandler(handler);
        desc.setNameSpaceURI("http://www.castor.org/SourceGenerator/Binding");
        desc.setMultivalued(true);
        addFieldDescriptor(desc);
        
        //-- validation code for: _enumBindingList
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        fieldValidator.setMinOccurs(0);
        { //-- local scope
        }
        desc.setValidator(fieldValidator);
        //-- _simpleTypeBindingList
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(org.exolab.castor.builder.binding.ComponentBindingType.class, "_simpleTypeBindingList", "simpleTypeBinding", org.exolab.castor.xml.NodeType.Element);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {
            public java.lang.Object getValue(final java.lang.Object object) 
                throws IllegalStateException
            {
                ComponentBindingType target = (ComponentBindingType) object;
                return target.getSimpleTypeBinding();
            }
            public void setValue(final java.lang.Object object, final java.lang.Object value) 
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    ComponentBindingType target = (ComponentBindingType) object;
                    target.addSimpleTypeBinding((org.exolab.castor.builder.binding.ComponentBindingType) value);
                }
                catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            public void resetValue(final Object object) throws IllegalStateException, IllegalArgumentException {
                try {
                    ComponentBindingType target = (ComponentBindingType) object;
                    target.removeAllSimpleTypeBinding();
                } catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            public java.lang.Object newInstance(final java.lang.Object parent) {
                return new org.exolab.castor.builder.binding.ComponentBindingType();
            }
        };
        desc.setHandler(handler);
        desc.setNameSpaceURI("http://www.castor.org/SourceGenerator/Binding");
        desc.setMultivalued(true);
        addFieldDescriptor(desc);
        
        //-- validation code for: _simpleTypeBindingList
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        fieldValidator.setMinOccurs(0);
        { //-- local scope
        }
        desc.setValidator(fieldValidator);
    } //-- org.exolab.castor.builder.binding.ComponentBindingTypeDescriptor()


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
        return org.exolab.castor.builder.binding.ComponentBindingType.class;
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
