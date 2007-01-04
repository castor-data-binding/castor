/*
 * This class was automatically generated with 
 * <a href="http://castor.exolab.org">Castor 0.9.4</a>, using an
 * XML Schema.
 * $Id$
 */

package org.exolab.castor.builder.binding;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import org.exolab.castor.xml.FieldValidator;
import org.exolab.castor.xml.NodeType;
import org.exolab.castor.xml.XMLFieldHandler;
import org.exolab.castor.xml.util.XMLFieldDescriptorImpl;
import org.exolab.castor.xml.validators.BooleanValidator;
import org.exolab.castor.xml.validators.StringValidator;

/**
 * 
 * 
 * @version $Revision$ $Date: 2005-03-05 06:42:06 -0700 (Sat, 05 Mar 2005) $
**/
public final class ClassTypeDescriptor
extends org.exolab.castor.xml.util.XMLClassDescriptorImpl {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    private java.lang.String _nsPrefix;

    private java.lang.String _nsURI;

    private java.lang.String _xmlName;

    private org.exolab.castor.xml.XMLFieldDescriptor _identity;


      //----------------/
     //- Constructors -/
    //----------------/

    public ClassTypeDescriptor() {
        super();
        _nsURI = "http://www.castor.org/SourceGenerator/Binding";
        _xmlName = "classType";
        
        //-- set grouping compositor
        setCompositorAsSequence();
        XMLFieldDescriptorImpl  desc           = null;
        XMLFieldHandler         handler        = null;
        FieldValidator          fieldValidator = null;
        //-- initialize attribute descriptors
        
        //-- _package
        desc = new XMLFieldDescriptorImpl(
                java.lang.String.class, "_package", "package", NodeType.Attribute);
        desc.setImmutable(true);
        handler = (new XMLFieldHandler() {
            public java.lang.Object getValue(final java.lang.Object object) 
                throws IllegalStateException {
                ClassType target = (ClassType) object;
                return target.getPackage();
            }
            public void setValue(final java.lang.Object object, final java.lang.Object value) 
                throws IllegalStateException, IllegalArgumentException {
                try {
                    ClassType target = (ClassType) object;
                    target.setPackage((java.lang.String) value);
                } catch (Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            public java.lang.Object newInstance(final java.lang.Object parent) {
                return null;
            }
        });
        desc.setHandler(handler);
        addFieldDescriptor(desc);
        
        //-- validation code for: _package
        fieldValidator = new FieldValidator();
        StringValidator sv1 = new StringValidator();
        sv1.setWhiteSpace("preserve");
        fieldValidator.setValidator(sv1);
        desc.setValidator(fieldValidator);
        
        //-- _name
        desc = new XMLFieldDescriptorImpl(
                java.lang.String.class, "_name", "name", NodeType.Attribute);
        desc.setImmutable(true);
        handler = (new XMLFieldHandler() {
            public java.lang.Object getValue(final java.lang.Object object) 
                throws IllegalStateException {
                ClassType target = (ClassType) object;
                return target.getName();
            }
            public void setValue(final java.lang.Object object, final java.lang.Object value) 
                throws IllegalStateException, IllegalArgumentException {
                try {
                    ClassType target = (ClassType) object;
                    target.setName((java.lang.String) value);
                } catch (Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            public java.lang.Object newInstance(final java.lang.Object parent) {
                return null;
            }
        });
        desc.setHandler(handler);
        addFieldDescriptor(desc);
        
        //-- validation code for: _name
        fieldValidator = new FieldValidator();
        StringValidator sv2 = new StringValidator();
        sv2.setWhiteSpace("preserve");
        fieldValidator.setValidator(sv2);
        desc.setValidator(fieldValidator);
        
        //-- _final
        desc = new XMLFieldDescriptorImpl(
                java.lang.Boolean.TYPE, "_final", "final", NodeType.Attribute);
        handler = (new XMLFieldHandler() {
            public java.lang.Object getValue(final java.lang.Object object) 
                throws IllegalStateException {
                ClassType target = (ClassType) object;
                if (!target.hasFinal()) { return null; }
                return new Boolean(target.getFinal());
            }
            public void setValue(final java.lang.Object object, final java.lang.Object value) 
                throws IllegalStateException, IllegalArgumentException {
                try {
                    ClassType target = (ClassType) object;
                    // if null, use delete method for optional primitives 
                    if (value == null) {
                        target.deleteFinal();
                        return;
                    }
                    target.setFinal(((Boolean) value).booleanValue());
                } catch (Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            public java.lang.Object newInstance(final java.lang.Object parent) {
                return null;
            }
        });
        desc.setHandler(handler);
        addFieldDescriptor(desc);
        
        //-- validation code for: _final
        fieldValidator = new FieldValidator();
        BooleanValidator bv1 = new BooleanValidator();
        fieldValidator.setValidator(bv1);
        desc.setValidator(fieldValidator);
        
        //-- _abstract
        desc = new XMLFieldDescriptorImpl(
                java.lang.Boolean.TYPE, "_abstract", "abstract", NodeType.Attribute);
        handler = (new XMLFieldHandler() {
            public java.lang.Object getValue(final java.lang.Object object) 
                throws IllegalStateException {
                ClassType target = (ClassType) object;
                if (!target.hasAbstract()) { return null; }
                return new Boolean(target.getAbstract());
            }
            public void setValue(final java.lang.Object object, final java.lang.Object value) 
                throws IllegalStateException, IllegalArgumentException {
                try {
                    ClassType target = (ClassType) object;
                    // if null, use delete method for optional primitives 
                    if (value == null) {
                        target.deleteAbstract();
                        return;
                    }
                    target.setAbstract(((Boolean) value).booleanValue());
                } catch (Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            public java.lang.Object newInstance(final java.lang.Object parent) {
                return null;
            }
        });
        desc.setHandler(handler);
        addFieldDescriptor(desc);
        
        //-- validation code for: _abstract
        fieldValidator = new FieldValidator();
        BooleanValidator bv2 = new BooleanValidator();
        fieldValidator.setValidator(bv2);
        desc.setValidator(fieldValidator);
        
        //-- _equals
        desc = new XMLFieldDescriptorImpl(
                java.lang.Boolean.TYPE, "_equals", "equals", NodeType.Attribute);
        handler = (new XMLFieldHandler() {
            public java.lang.Object getValue(final java.lang.Object object) 
                throws IllegalStateException {
                ClassType target = (ClassType) object;
                if (!target.hasEquals()) { return null; }
                return new Boolean(target.getEquals());
            }
            public void setValue(final java.lang.Object object, final java.lang.Object value) 
                throws IllegalStateException, IllegalArgumentException {
                try {
                    ClassType target = (ClassType) object;
                    // if null, use delete method for optional primitives 
                    if (value == null) {
                        target.deleteEquals();
                        return;
                    }
                    target.setEquals(((Boolean) value).booleanValue());
                } catch (Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            public java.lang.Object newInstance(final java.lang.Object parent) {
                return null;
            }
        });
        desc.setHandler(handler);
        addFieldDescriptor(desc);
        
        //-- validation code for: _equals
        fieldValidator = new FieldValidator();
        BooleanValidator bv3 = new BooleanValidator();
        fieldValidator.setValidator(bv3);
        desc.setValidator(fieldValidator);
        
        //-- _bound
        desc = new XMLFieldDescriptorImpl(
                java.lang.Boolean.TYPE, "_bound", "bound", NodeType.Attribute);
        handler = (new XMLFieldHandler() {
            public java.lang.Object getValue(final java.lang.Object object) 
                throws IllegalStateException {
                ClassType target = (ClassType) object;
                if (!target.hasBound()) { return null; }
                return new Boolean(target.getBound());
            }
            public void setValue(final java.lang.Object object, final java.lang.Object value) 
                throws IllegalStateException, IllegalArgumentException {
                try {
                    ClassType target = (ClassType) object;
                    // if null, use delete method for optional primitives 
                    if (value == null) {
                        target.deleteBound();
                        return;
                    }
                    target.setBound(((Boolean) value).booleanValue());
                } catch (Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            public java.lang.Object newInstance(final java.lang.Object parent) {
                return null;
            }
        });
        desc.setHandler(handler);
        addFieldDescriptor(desc);
        
        //-- validation code for: _bound
        fieldValidator = new FieldValidator();
        BooleanValidator bv4 = new BooleanValidator();
        fieldValidator.setValidator(bv4);
        desc.setValidator(fieldValidator);
        
        //-- initialize element descriptors
        
        //-- _implementsList
        desc = new XMLFieldDescriptorImpl(
                java.lang.String.class, "_implementsList", "implements", NodeType.Element);
        desc.setImmutable(true);
        handler = (new XMLFieldHandler() {
            public java.lang.Object getValue(final java.lang.Object object) 
                throws IllegalStateException {
                ClassType target = (ClassType) object;
                return target.getImplements();
            }
            public void setValue(final java.lang.Object object, final java.lang.Object value) 
                throws IllegalStateException, IllegalArgumentException {
                try {
                    ClassType target = (ClassType) object;
                    target.addImplements((java.lang.String) value);
                } catch (Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            public java.lang.Object newInstance(final java.lang.Object parent) {
                return null;
            }
        });
        desc.setHandler(handler);
        desc.setNameSpaceURI("http://www.castor.org/SourceGenerator/Binding");
        desc.setMultivalued(true);
        addFieldDescriptor(desc);
        
        //-- validation code for: _implementsList
        fieldValidator = new FieldValidator();
        fieldValidator.setMinOccurs(0);
        StringValidator sv3 = new StringValidator();
        sv3.setWhiteSpace("preserve");
        fieldValidator.setValidator(sv3);
        desc.setValidator(fieldValidator);
        
        //-- _extends
        desc = new XMLFieldDescriptorImpl(
                java.lang.String.class, "_extends", "extends", NodeType.Element);
        desc.setImmutable(true);
        handler = (new XMLFieldHandler() {
            public java.lang.Object getValue(final java.lang.Object object) 
                throws IllegalStateException {
                ClassType target = (ClassType) object;
                return target.getExtends();
            }
            public void setValue(final java.lang.Object object, final java.lang.Object value) 
                throws IllegalStateException, IllegalArgumentException {
                try {
                    ClassType target = (ClassType) object;
                    target.setExtends((java.lang.String) value);
                } catch (Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            public java.lang.Object newInstance(final java.lang.Object parent) {
                return null;
            }
        });
        desc.setHandler(handler);
        desc.setNameSpaceURI("http://www.castor.org/SourceGenerator/Binding");
        desc.setMultivalued(false);
        addFieldDescriptor(desc);
        
        //-- validation code for: _extends
        fieldValidator = new FieldValidator();
        StringValidator sv4 = new StringValidator();
        sv4.setWhiteSpace("preserve");
        fieldValidator.setValidator(sv4);
        desc.setValidator(fieldValidator);
        
    } //-- org.exolab.castor.builder.binding.ClassTypeDescriptor()


      //-----------/
     //- Methods -/
    //-----------/

    /**
    **/
    public org.exolab.castor.mapping.AccessMode getAccessMode() {
        return null;
    } //-- org.exolab.castor.mapping.AccessMode getAccessMode() 

    /**
    **/
    public org.exolab.castor.mapping.ClassDescriptor getExtends() {
        return null;
    } //-- org.exolab.castor.mapping.ClassDescriptor getExtends() 

    /**
    **/
    public org.exolab.castor.mapping.FieldDescriptor getIdentity() {
        return _identity;
    } //-- org.exolab.castor.mapping.FieldDescriptor getIdentity() 

    /**
    **/
    public java.lang.Class getJavaClass() {
        return org.exolab.castor.builder.binding.ClassType.class;
    } //-- java.lang.Class getJavaClass() 

    /**
    **/
    public java.lang.String getNameSpacePrefix() {
        return _nsPrefix;
    } //-- java.lang.String getNameSpacePrefix() 

    /**
    **/
    public java.lang.String getNameSpaceURI() {
        return _nsURI;
    } //-- java.lang.String getNameSpaceURI() 

    /**
    **/
    public org.exolab.castor.xml.TypeValidator getValidator() {
        return this;
    } //-- org.exolab.castor.xml.TypeValidator getValidator() 

    /**
    **/
    public java.lang.String getXMLName() {
        return _xmlName;
    } //-- java.lang.String getXMLName() 

}
