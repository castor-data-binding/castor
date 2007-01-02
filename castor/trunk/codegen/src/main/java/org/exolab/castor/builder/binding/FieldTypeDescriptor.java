/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.0.5</a>, using an XML
 * Schema.
 * $Id$
 */

package org.exolab.castor.builder.binding;

/**
 * Class FieldTypeDescriptor.
 * 
 * @version $Revision$ $Date$
 */
public final class FieldTypeDescriptor
extends org.exolab.castor.xml.util.XMLClassDescriptorImpl {


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

    public FieldTypeDescriptor() {
        super();
        nsURI = "http://www.castor.org/SourceGenerator/Binding";
        xmlName = "fieldType";
        elementDefinition = false;
        org.exolab.castor.xml.util.XMLFieldDescriptorImpl  desc           = null;
        org.exolab.castor.mapping.FieldHandler             handler        = null;
        org.exolab.castor.xml.FieldValidator               fieldValidator = null;
        //-- initialize attribute descriptors
        
        //-- _name
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(
                java.lang.String.class, "_name", "name",
                org.exolab.castor.xml.NodeType.Attribute);
        desc.setImmutable(true);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {
            public java.lang.Object getValue(final java.lang.Object object) 
                throws IllegalStateException {
                FieldType target = (FieldType) object;
                return target.getName();
            }
            public void setValue(final java.lang.Object object, final java.lang.Object value) 
                throws IllegalStateException, IllegalArgumentException {
                try {
                    FieldType target = (FieldType) object;
                    target.setName((java.lang.String) value);
                } catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            public java.lang.Object newInstance(final java.lang.Object parent) {
                return null;
            }
        };
        desc.setHandler(handler);
        desc.setMultivalued(false);
        addFieldDescriptor(desc);
        
        //-- validation code for: _name
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        { //-- local scope
            org.exolab.castor.xml.validators.StringValidator typeValidator =
                new org.exolab.castor.xml.validators.StringValidator();
            typeValidator.setWhiteSpace("preserve");
            fieldValidator.setValidator(typeValidator);
        }
        desc.setValidator(fieldValidator);
        //-- _javaType
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(
                java.lang.String.class, "_javaType", "java-type",
                org.exolab.castor.xml.NodeType.Attribute);
        desc.setImmutable(true);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {
            public java.lang.Object getValue(final java.lang.Object object) 
                throws IllegalStateException {
                FieldType target = (FieldType) object;
                return target.getJavaType();
            }
            public void setValue(final java.lang.Object object, final java.lang.Object value) 
                throws IllegalStateException, IllegalArgumentException {
                try {
                    FieldType target = (FieldType) object;
                    target.setJavaType((java.lang.String) value);
                } catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            public java.lang.Object newInstance(final java.lang.Object parent) {
                return null;
            }
        };
        desc.setHandler(handler);
        desc.setMultivalued(false);
        addFieldDescriptor(desc);
        
        //-- validation code for: _javaType
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        { //-- local scope
            org.exolab.castor.xml.validators.StringValidator typeValidator =
                new org.exolab.castor.xml.validators.StringValidator();
            typeValidator.setWhiteSpace("preserve");
            fieldValidator.setValidator(typeValidator);
        }
        desc.setValidator(fieldValidator);
        //-- _wrapper
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(
                Boolean.TYPE, "_wrapper", "wrapper",
                org.exolab.castor.xml.NodeType.Attribute);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {
            public java.lang.Object getValue(final java.lang.Object object) 
                throws IllegalStateException {
                FieldType target = (FieldType) object;
                if (!target.hasWrapper()) { return null; }
                return (target.getWrapper() ? java.lang.Boolean.TRUE : java.lang.Boolean.FALSE);
            }
            public void setValue(final java.lang.Object object, final java.lang.Object value) 
                throws IllegalStateException, IllegalArgumentException {
                try {
                    FieldType target = (FieldType) object;
                    // if null, use delete method for optional primitives 
                    if (value == null) {
                        target.deleteWrapper();
                        return;
                    }
                    target.setWrapper(((java.lang.Boolean) value).booleanValue());
                } catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            public java.lang.Object newInstance(final java.lang.Object parent) {
                return null;
            }
        };
        desc.setHandler(handler);
        desc.setMultivalued(false);
        addFieldDescriptor(desc);
        
        //-- validation code for: _wrapper
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        { //-- local scope
            org.exolab.castor.xml.validators.BooleanValidator typeValidator =
                new org.exolab.castor.xml.validators.BooleanValidator();
            fieldValidator.setValidator(typeValidator);
        }
        desc.setValidator(fieldValidator);
        //-- _handler
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(
                java.lang.String.class, "_handler", "handler",
                org.exolab.castor.xml.NodeType.Attribute);
        desc.setImmutable(true);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {
            public java.lang.Object getValue(final java.lang.Object object) 
                throws IllegalStateException {
                FieldType target = (FieldType) object;
                return target.getHandler();
            }
            public void setValue(final java.lang.Object object, final java.lang.Object value) 
                throws IllegalStateException, IllegalArgumentException {
                try {
                    FieldType target = (FieldType) object;
                    target.setHandler((java.lang.String) value);
                } catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            public java.lang.Object newInstance(final java.lang.Object parent) {
                return null;
            }
        };
        desc.setHandler(handler);
        desc.setMultivalued(false);
        addFieldDescriptor(desc);
        
        //-- validation code for: _handler
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        { //-- local scope
            org.exolab.castor.xml.validators.StringValidator typeValidator =
                new org.exolab.castor.xml.validators.StringValidator();
            typeValidator.setWhiteSpace("preserve");
            fieldValidator.setValidator(typeValidator);
        }
        desc.setValidator(fieldValidator);
        //-- _collection
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(
                org.exolab.castor.builder.binding.types.FieldTypeCollectionType.class,
                "_collection", "collection", org.exolab.castor.xml.NodeType.Attribute);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {
            public java.lang.Object getValue(final java.lang.Object object) 
                throws IllegalStateException {
                FieldType target = (FieldType) object;
                return target.getCollection();
            }
            public void setValue(final java.lang.Object object, final java.lang.Object value) 
                throws IllegalStateException, IllegalArgumentException {
                try {
                    FieldType target = (FieldType) object;
                    target.setCollection(
                            (org.exolab.castor.builder.binding.types.FieldTypeCollectionType)
                                    value);
                } catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            public java.lang.Object newInstance(final java.lang.Object parent) {
                return null;
            }
        };
        handler = new org.exolab.castor.xml.handlers.EnumFieldHandler(
                org.exolab.castor.builder.binding.types.FieldTypeCollectionType.class, handler);
        desc.setImmutable(true);
        desc.setHandler(handler);
        desc.setMultivalued(false);
        addFieldDescriptor(desc);
        
        //-- validation code for: _collection
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        { //-- local scope
        }
        desc.setValidator(fieldValidator);
        //-- _visibility
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(
                org.exolab.castor.builder.binding.types.FieldTypeVisibilityType.class,
                "_visibility", "visibility", org.exolab.castor.xml.NodeType.Attribute);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {
            public java.lang.Object getValue(final java.lang.Object object) 
                throws IllegalStateException {
                FieldType target = (FieldType) object;
                return target.getVisibility();
            }
            public void setValue(final java.lang.Object object, final java.lang.Object value) 
                throws IllegalStateException, IllegalArgumentException {
                try {
                    FieldType target = (FieldType) object;
                    target.setVisibility(
                            (org.exolab.castor.builder.binding.types.FieldTypeVisibilityType)
                                    value);
                } catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            public java.lang.Object newInstance(final java.lang.Object parent) {
                return null;
            }
        };
        handler = new org.exolab.castor.xml.handlers.EnumFieldHandler(
                org.exolab.castor.builder.binding.types.FieldTypeVisibilityType.class, handler);
        desc.setImmutable(true);
        desc.setHandler(handler);
        desc.setMultivalued(false);
        addFieldDescriptor(desc);
        
        //-- validation code for: _visibility
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        { //-- local scope
        }
        desc.setValidator(fieldValidator);
        //-- _validator
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(
                java.lang.String.class, "_validator", "validator",
                org.exolab.castor.xml.NodeType.Attribute);
        desc.setImmutable(true);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {
            public java.lang.Object getValue(final java.lang.Object object) 
                throws IllegalStateException {
                FieldType target = (FieldType) object;
                return target.getValidator();
            }
            public void setValue(final java.lang.Object object, final java.lang.Object value) 
                throws IllegalStateException, IllegalArgumentException {
                try {
                    FieldType target = (FieldType) object;
                    target.setValidator((java.lang.String) value);
                } catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            public java.lang.Object newInstance(final java.lang.Object parent) {
                return null;
            }
        };
        desc.setHandler(handler);
        desc.setMultivalued(false);
        addFieldDescriptor(desc);
        
        //-- validation code for: _validator
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        { //-- local scope
            org.exolab.castor.xml.validators.StringValidator typeValidator =
                new org.exolab.castor.xml.validators.StringValidator();
            typeValidator.setWhiteSpace("preserve");
            fieldValidator.setValidator(typeValidator);
        }
        desc.setValidator(fieldValidator);
        //-- initialize element descriptors
        
    } //-- org.exolab.castor.builder.binding.FieldTypeDescriptor()


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
        return identity;
    } //-- org.exolab.castor.mapping.FieldDescriptor getIdentity() 

    /**
     * Method getJavaClass.
     * 
     * @return the Java class represented by this descriptor.
     */
    public java.lang.Class getJavaClass() {
        return org.exolab.castor.builder.binding.FieldType.class;
    } //-- java.lang.Class getJavaClass() 

    /**
     * Method getNameSpacePrefix.
     * 
     * @return the namespace prefix to use when marshalling as XML.
     */
    public java.lang.String getNameSpacePrefix() {
        return nsPrefix;
    } //-- java.lang.String getNameSpacePrefix() 

    /**
     * Method getNameSpaceURI.
     * 
     * @return the namespace URI used when marshalling and
     * unmarshalling as XML.
     */
    public java.lang.String getNameSpaceURI() {
        return nsURI;
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
        return xmlName;
    } //-- java.lang.String getXMLName() 

    /**
     * Method isElementDefinition.
     * 
     * @return true if XML schema definition of this Class is that
     * of a global
     * element or element with anonymous type definition.
     */
    public boolean isElementDefinition() {
        return elementDefinition;
    } //-- boolean isElementDefinition() 

}
