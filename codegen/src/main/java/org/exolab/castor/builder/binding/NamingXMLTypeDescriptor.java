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

import org.exolab.castor.xml.*;
import org.exolab.castor.xml.FieldValidator;
import org.exolab.castor.xml.util.XMLFieldDescriptorImpl;

/**
 * 
 * 
 * @version $Revision$ $Date: 2005-03-05 06:42:06 -0700 (Sat, 05 Mar 2005) $
**/
public class NamingXMLTypeDescriptor extends org.exolab.castor.xml.util.XMLClassDescriptorImpl {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    private java.lang.String nsPrefix;

    private java.lang.String nsURI;

    private java.lang.String xmlName;

    private org.exolab.castor.xml.XMLFieldDescriptor identity;


      //----------------/
     //- Constructors -/
    //----------------/

    public NamingXMLTypeDescriptor() {
        super();
        nsURI = "http://www.castor.org/SourceGenerator/Binding";
        xmlName = "namingXMLType";
        
        //-- set grouping compositor
        setCompositorAsSequence();
        XMLFieldDescriptorImpl  desc           = null;
        XMLFieldHandler         handler        = null;
        FieldValidator          fieldValidator = null;
        //-- initialize attribute descriptors
        
        //-- initialize element descriptors
        
        //-- _elementName
        desc = new XMLFieldDescriptorImpl(NamingType.class, "_elementName", "elementName", NodeType.Element);
        handler = (new XMLFieldHandler() {
            public java.lang.Object getValue(final java.lang.Object object) 
                throws IllegalStateException {
                NamingXMLType target = (NamingXMLType) object;
                return target.getElementName();
            }
            public void setValue(final java.lang.Object object, final java.lang.Object value) 
                throws IllegalStateException, IllegalArgumentException {
                try {
                    NamingXMLType target = (NamingXMLType) object;
                    target.setElementName((NamingType) value);
                } catch (Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            public java.lang.Object newInstance(final java.lang.Object parent) {
                return new NamingType();
            }
        });
        desc.setHandler(handler);
        desc.setNameSpaceURI("http://www.castor.org/SourceGenerator/Binding");
        desc.setMultivalued(false);
        addFieldDescriptor(desc);
        
        //-- validation code for: _elementName
        fieldValidator = new FieldValidator();
        desc.setValidator(fieldValidator);
        
        //-- _complexTypeName
        desc = new XMLFieldDescriptorImpl(NamingType.class, "_complexTypeName", "complexTypeName", NodeType.Element);
        handler = (new XMLFieldHandler() {
            public java.lang.Object getValue(final java.lang.Object object) 
                throws IllegalStateException {
                NamingXMLType target = (NamingXMLType) object;
                return target.getComplexTypeName();
            }
            public void setValue(final java.lang.Object object, final java.lang.Object value) 
                throws IllegalStateException, IllegalArgumentException {
                try {
                    NamingXMLType target = (NamingXMLType) object;
                    target.setComplexTypeName((NamingType) value);
                } catch (Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            public java.lang.Object newInstance(final java.lang.Object parent) {
                return new NamingType();
            }
        });
        desc.setHandler(handler);
        desc.setNameSpaceURI("http://www.castor.org/SourceGenerator/Binding");
        desc.setMultivalued(false);
        addFieldDescriptor(desc);
        
        //-- validation code for: _complexTypeName
        fieldValidator = new FieldValidator();
        desc.setValidator(fieldValidator);
        
        //-- _modelGroupName
        desc = new XMLFieldDescriptorImpl(NamingType.class, "_modelGroupName", "modelGroupName", NodeType.Element);
        handler = (new XMLFieldHandler() {
            public java.lang.Object getValue(final java.lang.Object object) 
                throws IllegalStateException {
                NamingXMLType target = (NamingXMLType) object;
                return target.getModelGroupName();
            }
            public void setValue(final java.lang.Object object, final java.lang.Object value) 
                throws IllegalStateException, IllegalArgumentException {
                try {
                    NamingXMLType target = (NamingXMLType) object;
                    target.setModelGroupName((NamingType) value);
                } catch (Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            public java.lang.Object newInstance(final java.lang.Object parent) {
                return new NamingType();
            }
        });
        desc.setHandler(handler);
        desc.setNameSpaceURI("http://www.castor.org/SourceGenerator/Binding");
        desc.setMultivalued(false);
        addFieldDescriptor(desc);
        
        //-- validation code for: _modelGroupName
        fieldValidator = new FieldValidator();
        desc.setValidator(fieldValidator);
        
    } //-- org.exolab.castor.builder.binding.NamingXMLTypeDescriptor()


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
        return identity;
    } //-- org.exolab.castor.mapping.FieldDescriptor getIdentity() 

    /**
    **/
    public java.lang.Class getJavaClass() {
        return org.exolab.castor.builder.binding.NamingXMLType.class;
    } //-- java.lang.Class getJavaClass() 

    /**
    **/
    public java.lang.String getNameSpacePrefix() {
        return nsPrefix;
    } //-- java.lang.String getNameSpacePrefix() 

    /**
    **/
    public java.lang.String getNameSpaceURI() {
        return nsURI;
    } //-- java.lang.String getNameSpaceURI() 

    /**
    **/
    public org.exolab.castor.xml.TypeValidator getValidator() {
        return this;
    } //-- org.exolab.castor.xml.TypeValidator getValidator() 

    /**
    **/
    public java.lang.String getXMLName() {
        return xmlName;
    } //-- java.lang.String getXMLName() 

}
