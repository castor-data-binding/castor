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
public class FieldMappingDescriptor extends org.exolab.castor.xml.util.XMLClassDescriptorImpl {


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

    public FieldMappingDescriptor() {
        super();
        nsURI = "http://castor.exolab.org/";
        xmlName = "field";
        XMLFieldDescriptorImpl  desc           = null;
        XMLFieldHandler         handler        = null;
        FieldValidator          fieldValidator = null;
        //-- initialize attribute descriptors

        //-- _name
        desc = new XMLFieldDescriptorImpl(java.lang.String.class, "_name", "name", NodeType.Attribute);
        handler = (new XMLFieldHandler() {
            public Object getValue( Object object )
                throws IllegalStateException
            {
                FieldMapping target = (FieldMapping) object;
                return target.getName();
            }
            public void setValue( Object object, Object value)
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    FieldMapping target = (FieldMapping) object;
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
        fieldValidator.setValidator(new NameValidator(NameValidator.CDATA));
        desc.setValidator(fieldValidator);

        //-- _type
        desc = new XMLFieldDescriptorImpl(java.lang.String.class, "_type", "type", NodeType.Attribute);
        handler = (new XMLFieldHandler() {
            public Object getValue( Object object )
                throws IllegalStateException
            {
                FieldMapping target = (FieldMapping) object;
                return target.getType();
            }
            public void setValue( Object object, Object value)
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    FieldMapping target = (FieldMapping) object;
                    target.setType( (java.lang.String) value);
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

        //-- validation code for: _type
        fieldValidator = new FieldValidator();
        fieldValidator.setValidator(new NameValidator(NameValidator.CDATA));
        desc.setValidator(fieldValidator);

        //-- _required
        desc = new XMLFieldDescriptorImpl(java.lang.Boolean.TYPE, "_required", "required", NodeType.Attribute);
        handler = (new XMLFieldHandler() {
            public Object getValue( Object object )
                throws IllegalStateException
            {
                FieldMapping target = (FieldMapping) object;
                if(!target.hasRequired())
                    return null;
                return new Boolean(target.getRequired());
            }
            public void setValue( Object object, Object value)
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    FieldMapping target = (FieldMapping) object;
                    // if null, use delete method for optional primitives
                    if (value == null) {
                        target.deleteRequired();
                        return;
                    }
                    target.setRequired( ((Boolean)value).booleanValue());
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

        //-- validation code for: _required
        fieldValidator = new FieldValidator();
        desc.setValidator(fieldValidator);

        //-- _transient
        desc = new XMLFieldDescriptorImpl(java.lang.Boolean.TYPE, "_transient", "transient", NodeType.Attribute);
        handler = (new XMLFieldHandler() {
            public Object getValue( Object object )
                throws IllegalStateException
            {
                FieldMapping target = (FieldMapping) object;
                if(!target.hasTransient())
                    return null;
                return new Boolean(target.getTransient());
            }
            public void setValue( Object object, Object value)
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    FieldMapping target = (FieldMapping) object;
                    // if null, use delete method for optional primitives
                    if (value == null) {
                        target.deleteTransient();
                        return;
                    }
                    target.setTransient( ((Boolean)value).booleanValue());
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

        //-- validation code for: _transient
        fieldValidator = new FieldValidator();
        desc.setValidator(fieldValidator);

        //-- _direct
        desc = new XMLFieldDescriptorImpl(java.lang.Boolean.TYPE, "_direct", "direct", NodeType.Attribute);
        handler = (new XMLFieldHandler() {
            public Object getValue( Object object )
                throws IllegalStateException
            {
                FieldMapping target = (FieldMapping) object;
                if(!target.hasDirect())
                    return null;
                return new Boolean(target.getDirect());
            }
            public void setValue( Object object, Object value)
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    FieldMapping target = (FieldMapping) object;
                    // if null, use delete method for optional primitives
                    if (value == null) {
                        target.deleteDirect();
                        return;
                    }
                    target.setDirect( ((Boolean)value).booleanValue());
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

        //-- validation code for: _direct
        fieldValidator = new FieldValidator();
        desc.setValidator(fieldValidator);


        //-- _lazy
        desc = new XMLFieldDescriptorImpl(java.lang.Boolean.TYPE, "_lazy", "lazy", NodeType.Attribute);
        handler = (new XMLFieldHandler() {
            public Object getValue( Object object )
                throws IllegalStateException
            {
                FieldMapping target = (FieldMapping) object;
                if(!target.hasLazy())
                    return null;
                return new Boolean(target.getLazy());
            }
            public void setValue( Object object, Object value)
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    FieldMapping target = (FieldMapping) object;
                    // if null, use delete method for optional primitives
                    if (value == null) {
                        target.deleteLazy();
                        return;
                    }
                    target.setLazy( ((Boolean)value).booleanValue());
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

        //-- validation code for: _lazy
        fieldValidator = new FieldValidator();
        desc.setValidator(fieldValidator);

        //-- _container
        desc = new XMLFieldDescriptorImpl(java.lang.Boolean.TYPE, "_container", "container", NodeType.Attribute);
        handler = (new XMLFieldHandler() {
            public Object getValue( Object object )
                throws IllegalStateException
            {
                FieldMapping target = (FieldMapping) object;
                if(!target.hasContainer())
                    return null;
                return new Boolean(target.getContainer());
            }
            public void setValue( Object object, Object value)
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    FieldMapping target = (FieldMapping) object;
                    // if null, use delete method for optional primitives
                    if (value == null) {
                        target.deleteContainer();
                        return;
                    }
                    target.setContainer( ((Boolean)value).booleanValue());
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

        //-- validation code for: _container
        fieldValidator = new FieldValidator();
        desc.setValidator(fieldValidator);

        //-- _getMethod
        desc = new XMLFieldDescriptorImpl(java.lang.String.class, "_getMethod", "get-method", NodeType.Attribute);
        handler = (new XMLFieldHandler() {
            public Object getValue( Object object )
                throws IllegalStateException
            {
                FieldMapping target = (FieldMapping) object;
                return target.getGetMethod();
            }
            public void setValue( Object object, Object value)
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    FieldMapping target = (FieldMapping) object;
                    target.setGetMethod( (java.lang.String) value);
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

        //-- validation code for: _getMethod
        fieldValidator = new FieldValidator();
        fieldValidator.setValidator(new NameValidator(NameValidator.CDATA));
        desc.setValidator(fieldValidator);

        //-- _handler
        desc = new XMLFieldDescriptorImpl(java.lang.String.class, "_handler", "handler", NodeType.Attribute);
        handler = (new XMLFieldHandler() {
            public Object getValue( Object object )
                throws IllegalStateException
            {
                FieldMapping target = (FieldMapping) object;
                return target.getHandler();
            }
            public void setValue( Object object, Object value)
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    FieldMapping target = (FieldMapping) object;
                    target.setHandler( (java.lang.String) value);
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

        //-- validation code for: _handler
        fieldValidator = new FieldValidator();
        fieldValidator.setValidator(new NameValidator(NameValidator.CDATA));
        desc.setValidator(fieldValidator);
        
        //-- _hasMethod
        desc = new XMLFieldDescriptorImpl(java.lang.String.class, "_hasMethod", "has-method", NodeType.Attribute);
        handler = (new XMLFieldHandler() {
            public Object getValue( Object object )
                throws IllegalStateException
            {
                FieldMapping target = (FieldMapping) object;
                return target.getHasMethod();
            }
            public void setValue( Object object, Object value)
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    FieldMapping target = (FieldMapping) object;
                    target.setHasMethod( (java.lang.String) value);
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

        //-- validation code for: _hasMethod
        fieldValidator = new FieldValidator();
        fieldValidator.setValidator(new NameValidator(NameValidator.CDATA));
        desc.setValidator(fieldValidator);
        
        //-- _setMethod
        desc = new XMLFieldDescriptorImpl(java.lang.String.class, "_setMethod", "set-method", NodeType.Attribute);
        handler = (new XMLFieldHandler() {
            public Object getValue( Object object )
                throws IllegalStateException
            {
                FieldMapping target = (FieldMapping) object;
                return target.getSetMethod();
            }
            public void setValue( Object object, Object value)
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    FieldMapping target = (FieldMapping) object;
                    target.setSetMethod( (java.lang.String) value);
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

        //-- validation code for: _setMethod
        fieldValidator = new FieldValidator();
        fieldValidator.setValidator(new NameValidator(NameValidator.CDATA));
        desc.setValidator(fieldValidator);

        //-- _createMethod
        desc = new XMLFieldDescriptorImpl(java.lang.String.class, "_createMethod", "create-method", NodeType.Attribute);
        handler = (new XMLFieldHandler() {
            public Object getValue( Object object )
                throws IllegalStateException
            {
                FieldMapping target = (FieldMapping) object;
                return target.getCreateMethod();
            }
            public void setValue( Object object, Object value)
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    FieldMapping target = (FieldMapping) object;
                    target.setCreateMethod( (java.lang.String) value);
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

        //-- validation code for: _createMethod
        fieldValidator = new FieldValidator();
        fieldValidator.setValidator(new NameValidator(NameValidator.CDATA));
        desc.setValidator(fieldValidator);

        //-- _collection
        desc = new XMLFieldDescriptorImpl(org.exolab.castor.mapping.xml.types.CollectionType.class, "_collection", "collection", NodeType.Attribute);
        handler = (new XMLFieldHandler() {
            public Object getValue( Object object )
                throws IllegalStateException
            {
                FieldMapping target = (FieldMapping) object;
                return target.getCollection();
            }
            public void setValue( Object object, Object value)
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    FieldMapping target = (FieldMapping) object;
                    target.setCollection( (org.exolab.castor.mapping.xml.types.CollectionType) value);
                }
                catch (Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            public Object newInstance( Object parent ) {
                return null;
            }
        } );
        desc.setHandler( new EnumFieldHandler(org.exolab.castor.mapping.xml.types.CollectionType.class, handler));
        desc.setImmutable(true);
        desc.setNameSpaceURI("http://castor.exolab.org/");
        addFieldDescriptor(desc);

        //-- validation code for: _collection
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
                FieldMapping target = (FieldMapping) object;
                return target.getDescription();
            }
            public void setValue( Object object, Object value)
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    FieldMapping target = (FieldMapping) object;
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

        //-- _sql
        desc = new XMLFieldDescriptorImpl(Sql.class, "_sql", "sql", NodeType.Element);
        handler = (new XMLFieldHandler() {
            public Object getValue( Object object )
                throws IllegalStateException
            {
                FieldMapping target = (FieldMapping) object;
                return target.getSql();
            }
            public void setValue( Object object, Object value)
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    FieldMapping target = (FieldMapping) object;
                    target.setSql( (Sql) value);
                }
                catch (Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            public Object newInstance( Object parent ) {
                return new Sql();
            }
        } );
        desc.setHandler(handler);
        desc.setNameSpaceURI("http://castor.exolab.org/");
        desc.setMultivalued(false);
        addFieldDescriptor(desc);

        //-- validation code for: _sql
        fieldValidator = new FieldValidator();
        desc.setValidator(fieldValidator);

        //-- _bindXml
        desc = new XMLFieldDescriptorImpl(BindXml.class, "_bindXml", "bind-xml", NodeType.Element);
        //BACKWARD COMPATIBILITY
        desc.setMatches("bind-xml xml");
        handler = (new XMLFieldHandler() {
            public Object getValue( Object object )
                throws IllegalStateException
            {
                FieldMapping target = (FieldMapping) object;
                return target.getBindXml();
            }
            public void setValue( Object object, Object value)
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    FieldMapping target = (FieldMapping) object;
                    target.setBindXml( (BindXml) value);
                }
                catch (Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            public Object newInstance( Object parent ) {
                return new BindXml();
            }
        } );
        desc.setHandler(handler);
        desc.setNameSpaceURI("http://castor.exolab.org/");
        desc.setMultivalued(false);
        addFieldDescriptor(desc);

        //-- validation code for: _bindXml
        fieldValidator = new FieldValidator();
        desc.setValidator(fieldValidator);

        //-- _ldap
        desc = new XMLFieldDescriptorImpl(Ldap.class, "_ldap", "ldap", NodeType.Element);
        handler = (new XMLFieldHandler() {
            public Object getValue( Object object )
                throws IllegalStateException
            {
                FieldMapping target = (FieldMapping) object;
                return target.getLdap();
            }
            public void setValue( Object object, Object value)
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    FieldMapping target = (FieldMapping) object;
                    target.setLdap( (Ldap) value);
                }
                catch (Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            public Object newInstance( Object parent ) {
                return new Ldap();
            }
        } );
        desc.setHandler(handler);
        desc.setNameSpaceURI("http://castor.exolab.org/");
        desc.setMultivalued(false);
        addFieldDescriptor(desc);

        //-- validation code for: _ldap
        fieldValidator = new FieldValidator();
        desc.setValidator(fieldValidator);

    } //-- org.exolab.castor.mapping.xml.FieldMappingDescriptor()


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
        return org.exolab.castor.mapping.xml.FieldMapping.class;
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
