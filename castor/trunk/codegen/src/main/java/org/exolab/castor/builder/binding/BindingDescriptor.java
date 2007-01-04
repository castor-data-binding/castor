/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.0.5</a>, using an XML
 * Schema.
 * $Id$
 */

package org.exolab.castor.builder.binding;

/**
 * Class BindingDescriptor.
 * 
 * @version $Revision$ $Date$
 */
public final class BindingDescriptor
extends org.exolab.castor.xml.util.XMLClassDescriptorImpl {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field elementDefinition.
     */
    private boolean _elementDefinition;

    /**
     * Field nsPrefix.
     */
    private java.lang.String _nsPrefix;

    /**
     * Field nsURI.
     */
    private java.lang.String _nsURI;

    /**
     * Field xmlName.
     */
    private java.lang.String _xmlName;

    /**
     * Field identity.
     */
    private org.exolab.castor.xml.XMLFieldDescriptor _identity;


      //----------------/
     //- Constructors -/
    //----------------/

    public BindingDescriptor() {
        super();
        _nsURI = "http://www.castor.org/SourceGenerator/Binding";
        _xmlName = "binding";
        _elementDefinition = true;
        
        //-- set grouping compositor
        setCompositorAsSequence();
        org.exolab.castor.xml.util.XMLFieldDescriptorImpl  desc           = null;
        org.exolab.castor.mapping.FieldHandler             handler        = null;
        org.exolab.castor.xml.FieldValidator               fieldValidator = null;
        //-- initialize attribute descriptors
        
        //-- _defaultBindingType
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(
                org.exolab.castor.builder.binding.types.BindingType.class,
                "_defaultBindingType", "defaultBindingType",
                org.exolab.castor.xml.NodeType.Attribute);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {
            public java.lang.Object getValue(final java.lang.Object object) 
                throws IllegalStateException {
                Binding target = (Binding) object;
                return target.getDefaultBindingType();
            }
            public void setValue(final java.lang.Object object, final java.lang.Object value) 
                throws IllegalStateException, IllegalArgumentException {
                try {
                    Binding target = (Binding) object;
                    target.setDefaultBindingType(
                            (org.exolab.castor.builder.binding.types.BindingType) value);
                } catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            public java.lang.Object newInstance(final java.lang.Object parent) {
                return null;
            }
        };
        handler = new org.exolab.castor.xml.handlers.EnumFieldHandler(
                org.exolab.castor.builder.binding.types.BindingType.class, handler);
        desc.setImmutable(true);
        desc.setHandler(handler);
        desc.setMultivalued(false);
        addFieldDescriptor(desc);
        
        //-- validation code for: _defaultBindingType
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        desc.setValidator(fieldValidator);
        //-- initialize element descriptors
        
        //-- _includeList
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(
                org.exolab.castor.builder.binding.IncludeType.class,
                "_includeList", "include", org.exolab.castor.xml.NodeType.Element);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {
            public java.lang.Object getValue(final java.lang.Object object) 
            throws IllegalStateException {
                Binding target = (Binding) object;
                return target.getInclude();
            }
            public void setValue(final java.lang.Object object, final java.lang.Object value) 
            throws IllegalStateException, IllegalArgumentException {
                try {
                    Binding target = (Binding) object;
                    target.addInclude((org.exolab.castor.builder.binding.IncludeType) value);
                } catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            public void resetValue(final Object object)
            throws IllegalStateException, IllegalArgumentException {
                try {
                    Binding target = (Binding) object;
                    target.removeAllInclude();
                } catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            public java.lang.Object newInstance(final java.lang.Object parent) {
                return new org.exolab.castor.builder.binding.IncludeType();
            }
        };
        desc.setHandler(handler);
        desc.setNameSpaceURI("http://www.castor.org/SourceGenerator/Binding");
        desc.setMultivalued(true);
        addFieldDescriptor(desc);
        
        //-- validation code for: _includeList
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        fieldValidator.setMinOccurs(0);
        desc.setValidator(fieldValidator);
        //-- _packageList
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(
                org.exolab.castor.builder.binding.PackageType.class,
                "_packageList", "package", org.exolab.castor.xml.NodeType.Element);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {
            public java.lang.Object getValue(final java.lang.Object object) 
            throws IllegalStateException {
                Binding target = (Binding) object;
                return target.getPackage();
            }
            public void setValue(final java.lang.Object object, final java.lang.Object value) 
            throws IllegalStateException, IllegalArgumentException {
                try {
                    Binding target = (Binding) object;
                    target.addPackage((org.exolab.castor.builder.binding.PackageType) value);
                } catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            public void resetValue(final Object object)
            throws IllegalStateException, IllegalArgumentException {
                try {
                    Binding target = (Binding) object;
                    target.removeAllPackage();
                } catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            public java.lang.Object newInstance(final java.lang.Object parent) {
                return new org.exolab.castor.builder.binding.PackageType();
            }
        };
        desc.setHandler(handler);
        desc.setNameSpaceURI("http://www.castor.org/SourceGenerator/Binding");
        desc.setMultivalued(true);
        addFieldDescriptor(desc);
        
        //-- validation code for: _packageList
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        fieldValidator.setMinOccurs(0);
        desc.setValidator(fieldValidator);
        //-- _namingXML
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(
                org.exolab.castor.builder.binding.NamingXMLType.class,
                "_namingXML", "namingXML", org.exolab.castor.xml.NodeType.Element);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {
            public java.lang.Object getValue(final java.lang.Object object) 
            throws IllegalStateException {
                Binding target = (Binding) object;
                return target.getNamingXML();
            }
            public void setValue(final java.lang.Object object, final java.lang.Object value) 
            throws IllegalStateException, IllegalArgumentException {
                try {
                    Binding target = (Binding) object;
                    target.setNamingXML((org.exolab.castor.builder.binding.NamingXMLType) value);
                } catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            public java.lang.Object newInstance(final java.lang.Object parent) {
                return new org.exolab.castor.builder.binding.NamingXMLType();
            }
        };
        desc.setHandler(handler);
        desc.setNameSpaceURI("http://www.castor.org/SourceGenerator/Binding");
        desc.setMultivalued(false);
        addFieldDescriptor(desc);
        
        //-- validation code for: _namingXML
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        desc.setValidator(fieldValidator);
        //-- _elementBindingList
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(
                org.exolab.castor.builder.binding.ComponentBindingType.class,
                "_elementBindingList", "elementBinding", org.exolab.castor.xml.NodeType.Element);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {
            public java.lang.Object getValue(final java.lang.Object object) 
            throws IllegalStateException {
                Binding target = (Binding) object;
                return target.getElementBinding();
            }
            public void setValue(final java.lang.Object object, final java.lang.Object value) 
            throws IllegalStateException, IllegalArgumentException {
                try {
                    Binding target = (Binding) object;
                    target.addElementBinding(
                            (org.exolab.castor.builder.binding.ComponentBindingType) value);
                } catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            public void resetValue(final Object object)
            throws IllegalStateException, IllegalArgumentException {
                try {
                    Binding target = (Binding) object;
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
        desc.setValidator(fieldValidator);
        //-- _attributeBindingList
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(
                org.exolab.castor.builder.binding.ComponentBindingType.class,
                "_attributeBindingList", "attributeBinding",
                org.exolab.castor.xml.NodeType.Element);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {
            public java.lang.Object getValue(final java.lang.Object object) 
            throws IllegalStateException {
                Binding target = (Binding) object;
                return target.getAttributeBinding();
            }
            public void setValue(final java.lang.Object object, final java.lang.Object value) 
            throws IllegalStateException, IllegalArgumentException {
                try {
                    Binding target = (Binding) object;
                    target.addAttributeBinding(
                            (org.exolab.castor.builder.binding.ComponentBindingType) value);
                } catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            public void resetValue(final Object object)
            throws IllegalStateException, IllegalArgumentException {
                try {
                    Binding target = (Binding) object;
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
        desc.setValidator(fieldValidator);
        //-- _complexTypeBindingList
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(
                org.exolab.castor.builder.binding.ComponentBindingType.class,
                "_complexTypeBindingList", "complexTypeBinding",
                org.exolab.castor.xml.NodeType.Element);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {
            public java.lang.Object getValue(final java.lang.Object object) 
            throws IllegalStateException {
                Binding target = (Binding) object;
                return target.getComplexTypeBinding();
            }
            public void setValue(final java.lang.Object object, final java.lang.Object value) 
            throws IllegalStateException, IllegalArgumentException {
                try {
                    Binding target = (Binding) object;
                    target.addComplexTypeBinding(
                            (org.exolab.castor.builder.binding.ComponentBindingType) value);
                } catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            public void resetValue(final Object object)
            throws IllegalStateException, IllegalArgumentException {
                try {
                    Binding target = (Binding) object;
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
        desc.setValidator(fieldValidator);
        //-- _groupBindingList
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(
                org.exolab.castor.builder.binding.ComponentBindingType.class,
                "_groupBindingList", "groupBinding", org.exolab.castor.xml.NodeType.Element);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {
            public java.lang.Object getValue(final java.lang.Object object) 
            throws IllegalStateException {
                Binding target = (Binding) object;
                return target.getGroupBinding();
            }
            public void setValue(final java.lang.Object object, final java.lang.Object value) 
            throws IllegalStateException, IllegalArgumentException {
                try {
                    Binding target = (Binding) object;
                    target.addGroupBinding(
                            (org.exolab.castor.builder.binding.ComponentBindingType) value);
                } catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            public void resetValue(final Object object)
            throws IllegalStateException, IllegalArgumentException {
                try {
                    Binding target = (Binding) object;
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
        desc.setValidator(fieldValidator);
        //-- _enumBindingList
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(
                org.exolab.castor.builder.binding.ComponentBindingType.class,
                "_enumBindingList", "enumBinding", org.exolab.castor.xml.NodeType.Element);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {
            public java.lang.Object getValue(final java.lang.Object object) 
            throws IllegalStateException {
                Binding target = (Binding) object;
                return target.getEnumBinding();
            }
            public void setValue(final java.lang.Object object, final java.lang.Object value) 
            throws IllegalStateException, IllegalArgumentException {
                try {
                    Binding target = (Binding) object;
                    target.addEnumBinding(
                            (org.exolab.castor.builder.binding.ComponentBindingType) value);
                } catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            public void resetValue(final Object object)
            throws IllegalStateException, IllegalArgumentException {
                try {
                    Binding target = (Binding) object;
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
        desc.setValidator(fieldValidator);
        //-- _simpleTypeBindingList
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(
                org.exolab.castor.builder.binding.ComponentBindingType.class,
                "_simpleTypeBindingList", "simpleTypeBinding",
                org.exolab.castor.xml.NodeType.Element);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {
            public java.lang.Object getValue(final java.lang.Object object) 
                throws IllegalStateException {
                Binding target = (Binding) object;
                return target.getSimpleTypeBinding();
            }
            public void setValue(final java.lang.Object object, final java.lang.Object value) 
                throws IllegalStateException, IllegalArgumentException {
                try {
                    Binding target = (Binding) object;
                    target.addSimpleTypeBinding(
                            (org.exolab.castor.builder.binding.ComponentBindingType) value);
                } catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            public void resetValue(final Object object)
            throws IllegalStateException, IllegalArgumentException {
                try {
                    Binding target = (Binding) object;
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
        desc.setValidator(fieldValidator);
    } //-- org.exolab.castor.builder.binding.BindingDescriptor()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method getAccessMode.
     * 
     * @return the access mode specified for this class.
     */
    public org.exolab.castor.mapping.AccessMode getAccessMode() {
        return null;
    } //-- org.exolab.castor.mapping.AccessMode getAccessMode() 

    /**
     * Method getExtends.
     * 
     * @return the class descriptor of the class extended by this
     * class.
     */
    public org.exolab.castor.mapping.ClassDescriptor getExtends() {
        return null;
    } //-- org.exolab.castor.mapping.ClassDescriptor getExtends() 

    /**
     * Method getIdentity.
     * 
     * @return the identity field, null if this class has no
     * identity.
     */
    public org.exolab.castor.mapping.FieldDescriptor getIdentity() {
        return _identity;
    } //-- org.exolab.castor.mapping.FieldDescriptor getIdentity() 

    /**
     * Method getJavaClass.
     * 
     * @return the Java class represented by this descriptor.
     */
    public java.lang.Class getJavaClass() {
        return org.exolab.castor.builder.binding.Binding.class;
    } //-- java.lang.Class getJavaClass() 

    /**
     * Method getNameSpacePrefix.
     * 
     * @return the namespace prefix to use when marshalling as XML.
     */
    public java.lang.String getNameSpacePrefix() {
        return _nsPrefix;
    } //-- java.lang.String getNameSpacePrefix() 

    /**
     * Method getNameSpaceURI.
     * 
     * @return the namespace URI used when marshalling and
     * unmarshalling as XML.
     */
    public java.lang.String getNameSpaceURI() {
        return _nsURI;
    } //-- java.lang.String getNameSpaceURI() 

    /**
     * Method getValidator.
     * 
     * @return a specific validator for the class described by this
     * ClassDescriptor.
     */
    public org.exolab.castor.xml.TypeValidator getValidator() {
        return this;
    } //-- org.exolab.castor.xml.TypeValidator getValidator() 

    /**
     * Method getXMLName.
     * 
     * @return the XML Name for the Class being described.
     */
    public java.lang.String getXMLName() {
        return _xmlName;
    } //-- java.lang.String getXMLName() 

    /**
     * Method isElementDefinition.
     * 
     * @return true if XML schema definition of this Class is that
     * of a global
     * element or element with anonymous type definition.
     */
    public boolean isElementDefinition() {
        return _elementDefinition;
    } //-- boolean isElementDefinition() 

}
