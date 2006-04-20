/*
 * This class was automatically generated with
 * <a href="http://castor.exolab.org">Castor 0.8.12</a>, using an
 * XML Schema.
 * $Id$
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
public class ClassMappingDescriptor extends org.exolab.castor.xml.util.XMLClassDescriptorImpl {


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

    public ClassMappingDescriptor() {
        super();
        nsURI = "http://castor.exolab.org/";
        xmlName = "class";
        XMLFieldDescriptorImpl  desc           = null;
        XMLFieldHandler         handler        = null;
        FieldValidator          fieldValidator = null;
        //-- initialize attribute descriptors

        //-- _name
        desc = new XMLFieldDescriptorImpl(java.lang.String.class, "_name", "name", NodeType.Attribute);
        this.identity = desc;
        handler = (new XMLFieldHandler() {
            public Object getValue( Object object )
                throws IllegalStateException
            {
                ClassMapping target = (ClassMapping) object;
                return target.getName();
            }
            public void setValue( Object object, Object value)
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    ClassMapping target = (ClassMapping) object;
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
        desc.setNameSpaceURI("http://castor.exolab.org/");
        desc.setRequired(true);
        addFieldDescriptor(desc);

        //-- validation code for: _name
        fieldValidator = new FieldValidator();
        fieldValidator.setMinOccurs(1);
        desc.setValidator(fieldValidator);

        //-- _extends
        desc = new XMLFieldDescriptorImpl(java.lang.Object.class, "_extends", "extends", NodeType.Attribute);
        desc.setReference(true);
        handler = (new XMLFieldHandler() {
            public Object getValue( Object object )
                throws IllegalStateException
            {
                ClassMapping target = (ClassMapping) object;
                return target.getExtends();
            }
            public void setValue( Object object, Object value)
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    ClassMapping target = (ClassMapping) object;
                    target.setExtends( (java.lang.Object) value);
                }
                catch (Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            public Object newInstance( Object parent ) {
                return new java.lang.Object();
            }
        } );
        desc.setHandler(handler);
        desc.setNameSpaceURI("http://castor.exolab.org/");
        addFieldDescriptor(desc);

        //-- validation code for: _extends
        fieldValidator = new FieldValidator();
        desc.setValidator(fieldValidator);

        //-- _depends
        desc = new XMLFieldDescriptorImpl(java.lang.Object.class, "_depends", "depends", NodeType.Attribute);
        desc.setReference(true);
        handler = (new XMLFieldHandler() {
            public Object getValue( Object object )
                throws IllegalStateException
            {
                ClassMapping target = (ClassMapping) object;
                return target.getDepends();
            }
            public void setValue( Object object, Object value)
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    ClassMapping target = (ClassMapping) object;
                    target.setDepends( (java.lang.Object) value);
                }
                catch (Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            public Object newInstance( Object parent ) {
                return new java.lang.Object();
            }
        } );
        desc.setHandler(handler);
        desc.setNameSpaceURI("http://castor.exolab.org/");
        addFieldDescriptor(desc);

        //-- validation code for: _depends
        fieldValidator = new FieldValidator();
        desc.setValidator(fieldValidator);

        //-- _identity
        desc = new XMLFieldDescriptorImpl(java.lang.String.class, "_identity", "identity", NodeType.Attribute);
        handler = (new XMLFieldHandler() {
            public Object getValue( Object object )
                throws IllegalStateException
            {
                ClassMapping target = (ClassMapping) object;
                return target.getIdentity();
            }
            public void setValue( Object object, Object value)
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    ClassMapping target = (ClassMapping) object;
                    target.addIdentity( (java.lang.String) value);
                }
                catch (Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            public Object newInstance( Object parent ) {
                return new java.lang.String();
            }
        } );
        desc.setHandler( new CollectionFieldHandler(handler));
        desc.setNameSpaceURI("http://castor.exolab.org/");
        addFieldDescriptor(desc);

        //-- validation code for: _identity
        fieldValidator = new FieldValidator();
        fieldValidator.setMinOccurs(0);
        desc.setValidator(fieldValidator);

        //-- _timestamp
        desc = new XMLFieldDescriptorImpl(java.lang.String.class, "_timestamp", "timestamp", NodeType.Attribute);
        handler = (new XMLFieldHandler() {
            public Object getValue( Object object )
                throws IllegalStateException
            {
                ClassMapping target = (ClassMapping) object;
                return target.getTimestamp();
            }
            public void setValue( Object object, Object value)
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    ClassMapping target = (ClassMapping) object;
                    target.setTimestamp( (java.lang.String) value);
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
        desc.setNameSpaceURI("http://castor.exolab.org/");
        addFieldDescriptor(desc);

        //-- validation code for: _timestamp
        fieldValidator = new FieldValidator();
        fieldValidator.setValidator(new NameValidator(NameValidator.NMTOKEN));
        desc.setValidator(fieldValidator);


        //-- verifyConstructable
        desc = new XMLFieldDescriptorImpl(java.lang.Boolean.TYPE, "verifyConstructable", "verify-constructable", NodeType.Attribute);
        handler = (new XMLFieldHandler() {
            public Object getValue( Object object )
                throws IllegalStateException
            {
                ClassMapping target = (ClassMapping) object;
                if (target.getVerifyConstructable()) 
                    return null;
                return new Boolean(target.getVerifyConstructable());
            }
            public void setValue( Object object, Object value)
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    ClassMapping target = (ClassMapping) object;
                    target.setVerifyConstructable(((Boolean)value).booleanValue());
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
        desc.setNameSpaceURI("http://castor.exolab.org/");
        addFieldDescriptor(desc);

        //-- validation code for: verifyConstructable
        fieldValidator = new FieldValidator();
        fieldValidator.setValidator(new NameValidator(NameValidator.NMTOKEN));
        desc.setValidator(fieldValidator);


        //-- _access
        desc = new XMLFieldDescriptorImpl(org.exolab.castor.mapping.xml.types.AccessType.class, "_access", "access", NodeType.Attribute);
        handler = (new XMLFieldHandler() {
            public Object getValue( Object object )
                throws IllegalStateException
            {
                ClassMapping target = (ClassMapping) object;
                return target.getAccess();
            }
            public void setValue( Object object, Object value)
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    ClassMapping target = (ClassMapping) object;
                    target.setAccess( (org.exolab.castor.mapping.xml.types.AccessType) value);
                }
                catch (Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            public Object newInstance( Object parent ) {
                return null;
            }
        } );
        desc.setHandler( new EnumFieldHandler(org.exolab.castor.mapping.xml.types.AccessType.class, handler));
        desc.setImmutable(true);
        desc.setNameSpaceURI("http://castor.exolab.org/");
        addFieldDescriptor(desc);

        //-- validation code for: _access
        fieldValidator = new FieldValidator();
        desc.setValidator(fieldValidator);

        //-- _keyGenerator
        desc = new XMLFieldDescriptorImpl(java.lang.String.class, "_keyGenerator", "key-generator", NodeType.Attribute);
        desc.setImmutable(true);
        handler = (new XMLFieldHandler() {
            public Object getValue( Object object )
                throws IllegalStateException
            {
                ClassMapping target = (ClassMapping) object;
                return target.getKeyGenerator();
            }
            public void setValue( Object object, Object value)
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    ClassMapping target = (ClassMapping) object;
                    target.setKeyGenerator( (java.lang.String) value);
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
        desc.setNameSpaceURI("http://castor.exolab.org/");
        addFieldDescriptor(desc);

        //-- validation code for: _keyGenerator
        fieldValidator = new FieldValidator();
        { //-- local scope
            StringValidator sv = new StringValidator();
            sv.setWhiteSpace("preserve");
            fieldValidator.setValidator(sv);
        }
        desc.setValidator(fieldValidator);

        //-- _autoComplete
        desc = new XMLFieldDescriptorImpl(java.lang.Boolean.TYPE, "_autoComplete", "auto-complete", NodeType.Attribute);
        handler = (new XMLFieldHandler() {
            public Object getValue( Object object )
                throws IllegalStateException
            {
                ClassMapping target = (ClassMapping) object;
                if(!target.hasAutoComplete())
                    return null;
                return new Boolean(target.getAutoComplete());
            }
            public void setValue( Object object, Object value)
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    ClassMapping target = (ClassMapping) object;
                    // if null, use delete method for optional primitives
                    if (value == null) {
                        target.deleteAutoComplete();
                        return;
                    }
                    target.setAutoComplete( ((Boolean)value).booleanValue());
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
        desc.setNameSpaceURI("http://castor.exolab.org/");
        addFieldDescriptor(desc);

        //-- validation code for: _autoComplete
        fieldValidator = new FieldValidator();
        desc.setValidator(fieldValidator);

        //-- initialize element descriptors

        //-- _description
        desc = new XMLFieldDescriptorImpl(java.lang.String.class, "_description", "description", NodeType.Element);
        desc.setImmutable(true);
        handler = (new XMLFieldHandler() {
            public Object getValue( Object object )
                throws IllegalStateException
            {
                ClassMapping target = (ClassMapping) object;
                return target.getDescription();
            }
            public void setValue( Object object, Object value)
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    ClassMapping target = (ClassMapping) object;
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
        desc.setNameSpaceURI("http://castor.exolab.org/");
        desc.setMultivalued(false);
        addFieldDescriptor(desc);

        //-- validation code for: _description
        fieldValidator = new FieldValidator();
        { //-- local scope
            StringValidator sv = new StringValidator();
            sv.setWhiteSpace("preserve");
            fieldValidator.setValidator(sv);
        }
        desc.setValidator(fieldValidator);

        //-- _cacheTypeMapping
        desc = new XMLFieldDescriptorImpl(CacheTypeMapping.class, "_cacheTypeMapping", "cache-type", NodeType.Element);
        handler = (new XMLFieldHandler() {
            public Object getValue( Object object )
                throws IllegalStateException
            {
                ClassMapping target = (ClassMapping) object;
                return target.getCacheTypeMapping();
            }
            public void setValue( Object object, Object value)
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    ClassMapping target = (ClassMapping) object;
                    target.setCacheTypeMapping( (CacheTypeMapping) value);
                }
                catch (Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            public Object newInstance( Object parent ) {
                return new CacheTypeMapping();
            }
        } );
        desc.setHandler(handler);
        desc.setNameSpaceURI("http://castor.exolab.org/");
        desc.setMultivalued(false);
        addFieldDescriptor(desc);

        //-- validation code for: _cacheTypeMapping
        fieldValidator = new FieldValidator();
        desc.setValidator(fieldValidator);

        //-- _mapTo
        desc = new XMLFieldDescriptorImpl(MapTo.class, "_mapTo", "map-to", NodeType.Element);
        handler = (new XMLFieldHandler() {
            public Object getValue( Object object )
                throws IllegalStateException
            {
                ClassMapping target = (ClassMapping) object;
                return target.getMapTo();
            }
            public void setValue( Object object, Object value)
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    ClassMapping target = (ClassMapping) object;
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
        desc.setNameSpaceURI("http://castor.exolab.org/");
        desc.setMultivalued(false);
        addFieldDescriptor(desc);

        //-- validation code for: _mapTo
        fieldValidator = new FieldValidator();
        desc.setValidator(fieldValidator);

        //-- _fieldMappingList
        desc = new XMLFieldDescriptorImpl(FieldMapping.class, "_fieldMappingList", "field", NodeType.Element);
        handler = (new XMLFieldHandler() {
            public Object getValue( Object object )
                throws IllegalStateException
            {
                ClassMapping target = (ClassMapping) object;
                return target.getFieldMapping();
            }
            public void setValue( Object object, Object value)
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    ClassMapping target = (ClassMapping) object;
                    target.addFieldMapping( (FieldMapping) value);
                }
                catch (Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            public Object newInstance( Object parent ) {
                return new FieldMapping();
            }
        } );
        //desc.setHandler( new CollectionFieldHandler(handler));
        desc.setHandler(handler);
        desc.setNameSpaceURI("http://castor.exolab.org/");
        desc.setMultivalued(true);
        addFieldDescriptor(desc);

        //-- validation code for: _fieldMappingList
        fieldValidator = new FieldValidator();
        fieldValidator.setMinOccurs(0);
        desc.setValidator(fieldValidator);

        //-- _containerList
        desc = new XMLFieldDescriptorImpl(Container.class, "_containerList", "container", NodeType.Element);
        handler = (new XMLFieldHandler() {
            public Object getValue( Object object )
                throws IllegalStateException
            {
                ClassMapping target = (ClassMapping) object;
                return target.getContainer();
            }
            public void setValue( Object object, Object value)
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    ClassMapping target = (ClassMapping) object;
                    target.addContainer( (Container) value);
                }
                catch (Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            public Object newInstance( Object parent ) {
                return new Container();
            }
        } );
        desc.setHandler( new CollectionFieldHandler(handler));
        desc.setNameSpaceURI("http://castor.exolab.org/");
        desc.setMultivalued(true);
        addFieldDescriptor(desc);

        //-- validation code for: _containerList
        fieldValidator = new FieldValidator();
        fieldValidator.setMinOccurs(0);
        desc.setValidator(fieldValidator);

    } //-- org.exolab.castor.mapping.xml.ClassMappingDescriptor()


      //-----------/
     //- Methods -/
    //-----------/

    /**
    **/
    public org.exolab.castor.mapping.AccessMode getAccessMode()
    {
        return null;
    } //-- org.exolab.castor.mapping.AccessMode getAccessMode()

    /**
    **/
    public org.exolab.castor.mapping.ClassDescriptor getExtends()
    {
        return null;
    } //-- org.exolab.castor.mapping.ClassDescriptor getExtends()

    /**
    **/
    public org.exolab.castor.mapping.FieldDescriptor getIdentity()
    {
        return identity;
    } //-- org.exolab.castor.mapping.FieldDescriptor getIdentity()

    /**
    **/
    public java.lang.Class getJavaClass()
    {
        return org.exolab.castor.mapping.xml.ClassMapping.class;
    } //-- java.lang.Class getJavaClass()

    /**
    **/
    public java.lang.String getNameSpacePrefix()
    {
        return nsPrefix;
    } //-- java.lang.String getNameSpacePrefix()

    /**
    **/
    public java.lang.String getNameSpaceURI()
    {
        return nsURI;
    } //-- java.lang.String getNameSpaceURI()

    /**
    **/
    public org.exolab.castor.xml.TypeValidator getValidator()
    {
        return null;
    } //-- org.exolab.castor.xml.TypeValidator getValidator()

    /**
    **/
    public java.lang.String getXMLName()
    {
        return xmlName;
    } //-- java.lang.String getXMLName()

}
