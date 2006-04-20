/*
 * This class was automatically generated with 
 * <a href="http://castor.exolab.org">Castor 0.8.7</a>, using an
 * XML Schema.
 * $Id
 */

package org.exolab.castor.mapping.xml;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import org.exolab.castor.mapping.AccessMode;
import org.exolab.castor.mapping.ClassDescriptor;
import org.exolab.castor.mapping.FieldDescriptor;
import org.exolab.castor.xml.*;
import org.exolab.castor.xml.FieldValidator;
import org.exolab.castor.xml.TypeValidator;
import org.exolab.castor.xml.XMLFieldDescriptor;
import org.exolab.castor.xml.handlers.*;
import org.exolab.castor.xml.util.XMLFieldDescriptorImpl;
import org.exolab.castor.xml.validators.*;

/**
 * 
 * @version $Revision$ $Date$
**/
public class ManyToManyDescriptor implements org.exolab.castor.xml.XMLClassDescriptor {


      //--------------------/
     //- Member Variables -/
    //--------------------/

    private org.exolab.castor.xml.XMLFieldDescriptor[] elements;

    private org.exolab.castor.xml.XMLFieldDescriptor[] attributes;

    private org.exolab.castor.xml.util.XMLFieldDescriptorImpl contentDesc;

    private java.lang.String nsPrefix;

    private java.lang.String nsURI;

    private java.lang.String xmlName;

    private org.exolab.castor.xml.XMLFieldDescriptor identity;


      //----------------/
     //- Constructors -/
    //----------------/

    public ManyToManyDescriptor() {
        xmlName = "many-to-many";
        XMLFieldDescriptorImpl  desc           = null;
        XMLFieldHandler         handler        = null;
        FieldValidator          fieldValidator = null;
        //-- initialize attribute descriptors
        
        attributes = new XMLFieldDescriptorImpl[1];
        //-- _name
        desc = new XMLFieldDescriptorImpl(java.lang.String.class, "_name", "name", NodeType.Attribute);
        this.identity = desc;
        handler = (new XMLFieldHandler() {
            public Object getValue( Object object ) 
                throws IllegalStateException
            {
                ManyToMany target = (ManyToMany) object;
                return target.getName();
            }
            public void setValue( Object object, Object value) 
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    ManyToMany target = (ManyToMany) object;
                    target.setName( (java.lang.String) value);
                }
                catch (Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            public Object newInstance( Object parent ) {
                return new java.lang.String();
            }
        } );
        desc.setHandler(handler);
        desc.setRequired(true);
        attributes[0] = desc;
        
        //-- validation code for: _name
        fieldValidator = new FieldValidator();
        fieldValidator.setMinOccurs(1);
        desc.setValidator(fieldValidator);
        
        //-- initialize element descriptors
        
        elements = new XMLFieldDescriptorImpl[3];
        //-- _description
        desc = new XMLFieldDescriptorImpl(java.lang.String.class, "_description", "description", NodeType.Element);
        desc.setImmutable(true);
        handler = (new XMLFieldHandler() {
            public Object getValue( Object object ) 
                throws IllegalStateException
            {
                ManyToMany target = (ManyToMany) object;
                return target.getDescription();
            }
            public void setValue( Object object, Object value) 
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    ManyToMany target = (ManyToMany) object;
                    target.setDescription( (java.lang.String) value);
                }
                catch (Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            public Object newInstance( Object parent ) {
                return null;
            }
        } );
        desc.setHandler(handler);
        desc.setMultivalued(false);
        elements[0] = desc;
        
        //-- validation code for: _description
        fieldValidator = new FieldValidator();
        { //-- local scope
            StringValidator sv = new StringValidator();
            fieldValidator.setValidator(sv);
        }
        desc.setValidator(fieldValidator);
        
        //-- _mapTo
        desc = new XMLFieldDescriptorImpl(MapTo.class, "_mapTo", "map-to", NodeType.Element);
        handler = (new XMLFieldHandler() {
            public Object getValue( Object object ) 
                throws IllegalStateException
            {
                ManyToMany target = (ManyToMany) object;
                return target.getMapTo();
            }
            public void setValue( Object object, Object value) 
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    ManyToMany target = (ManyToMany) object;
                    target.setMapTo( (MapTo) value);
                }
                catch (Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            public Object newInstance( Object parent ) {
                return new MapTo();
            }
        } );
        desc.setHandler(handler);
        desc.setMultivalued(false);
        elements[1] = desc;
        
        //-- validation code for: _mapTo
        fieldValidator = new FieldValidator();
        desc.setValidator(fieldValidator);
        
        //-- _typeMappingList
        desc = new XMLFieldDescriptorImpl(TypeMapping.class, "_typeMappingList", "type", NodeType.Element);
        handler = (new XMLFieldHandler() {
            public Object getValue( Object object ) 
                throws IllegalStateException
            {
                ManyToMany target = (ManyToMany) object;
                return target.getTypeMapping();
            }
            public void setValue( Object object, Object value) 
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    ManyToMany target = (ManyToMany) object;
                    target.addTypeMapping( (TypeMapping) value);
                }
                catch (Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            public Object newInstance( Object parent ) {
                return new TypeMapping();
            }
        } );
        desc.setHandler(handler);
        desc.setRequired(true);
        desc.setMultivalued(true);
        elements[2] = desc;
        
        //-- validation code for: _typeMappingList
        fieldValidator = new FieldValidator();
        fieldValidator.setMinOccurs(2);
        desc.setValidator(fieldValidator);
        
    } //-- org.exolab.castor.mapping.xml.ManyToManyDescriptor()


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
    public org.exolab.castor.xml.XMLFieldDescriptor[] getAttributeDescriptors() {
        return attributes;
    } //-- org.exolab.castor.xml.XMLFieldDescriptor[] getAttributeDescriptors() 

    /**
    **/
    public org.exolab.castor.xml.XMLFieldDescriptor getContentDescriptor() {
        return contentDesc;
    } //-- org.exolab.castor.xml.XMLFieldDescriptor getContentDescriptor() 

    /**
    **/
    public org.exolab.castor.xml.XMLFieldDescriptor[] getElementDescriptors() {
        return elements;
    } //-- org.exolab.castor.xml.XMLFieldDescriptor[] getElementDescriptors() 

    /**
     * Returns the XML field descriptor matching the given
     * xml name and nodeType. If NodeType is null, then
     * either an AttributeDescriptor, or ElementDescriptor
     * may be returned. Null is returned if no matching
     * descriptor is available.
     *
     * @param name the xml name to match against
     * @param nodeType, the NodeType to match against, or null if
     * the node type is not known.
     * @return the matching descriptor, or null if no matching
     * descriptor is available.
     *
    **/
    public XMLFieldDescriptor getFieldDescriptor
        (String name, NodeType nodeType) 
    {
        
        boolean wild = (nodeType == null);
        
        if (wild || (nodeType == NodeType.Element)) {
            XMLFieldDescriptor desc = null;
            for (int i = 0; i < elements.length; i++) {
                desc = elements[i];
                if (desc == null) continue;
                if (desc.matches(name)) return desc;
            }
        }
        
        if (wild || (nodeType == NodeType.Attribute)) {
            XMLFieldDescriptor desc = null;
            for (int i = 0; i < attributes.length; i++) {
                desc = attributes[i];
                if (desc == null) continue;
                if (desc.matches(name)) return desc;
            }
        }
        
        return null;
        
    } //-- getFieldDescriptor

    /**
    **/
    public org.exolab.castor.mapping.ClassDescriptor getExtends() {
        return null;
    } //-- org.exolab.castor.mapping.ClassDescriptor getExtends() 

    /**
    **/
    public org.exolab.castor.mapping.FieldDescriptor[] getFields() {
        int size = attributes.length + elements.length;
        if (contentDesc != null) ++size;
        
        FieldDescriptor[] fields = new FieldDescriptor[size];
        int c = 0;
        for (int i = 0; i < attributes.length; i++)
            fields[c++] = attributes[i];
        
        for (int i = 0; i < elements.length; i++)
            fields[c++] = elements[i];
        
        if (contentDesc != null) fields[c] = contentDesc;
        
        return fields;
    } //-- org.exolab.castor.mapping.FieldDescriptor[] getFields() 

    /**
    **/
    public org.exolab.castor.mapping.FieldDescriptor getIdentity() {
        return identity;
    } //-- org.exolab.castor.mapping.FieldDescriptor getIdentity() 

    /**
    **/
    public java.lang.Class getJavaClass() {
        return org.exolab.castor.mapping.xml.ManyToMany.class;
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
        return null;
    } //-- org.exolab.castor.xml.TypeValidator getValidator() 

    /**
    **/
    public java.lang.String getXMLName() {
        return xmlName;
    } //-- java.lang.String getXMLName() 

}
