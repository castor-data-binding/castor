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
public class ContainerDescriptor extends org.exolab.castor.xml.util.XMLClassDescriptorImpl {


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

    public ContainerDescriptor() {
        super();
        nsURI = "http://castor.exolab.org/";
        xmlName = "container";
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
                Container target = (Container) object;
                return target.getName();
            }
            public void setValue( Object object, Object value)
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    Container target = (Container) object;
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
        fieldValidator.setValidator(new NameValidator(NameValidator.NMTOKEN));
        desc.setValidator(fieldValidator);

        //-- _type
        desc = new XMLFieldDescriptorImpl(java.lang.String.class, "_type", "type", NodeType.Attribute);
        handler = (new XMLFieldHandler() {
            public Object getValue( Object object )
                throws IllegalStateException
            {
                Container target = (Container) object;
                return target.getType();
            }
            public void setValue( Object object, Object value)
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    Container target = (Container) object;
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
        fieldValidator.setValidator(new NameValidator(NameValidator.NMTOKEN));
        desc.setValidator(fieldValidator);

        //-- _required
        desc = new XMLFieldDescriptorImpl(java.lang.Boolean.TYPE, "_required", "required", NodeType.Attribute);
        handler = (new XMLFieldHandler() {
            public Object getValue( Object object )
                throws IllegalStateException
            {
                Container target = (Container) object;
                if(!target.hasRequired())
                    return null;
                return new Boolean(target.getRequired());
            }
            public void setValue( Object object, Object value)
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    Container target = (Container) object;
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

        //-- _direct
        desc = new XMLFieldDescriptorImpl(java.lang.Boolean.TYPE, "_direct", "direct", NodeType.Attribute);
        handler = (new XMLFieldHandler() {
            public Object getValue( Object object )
                throws IllegalStateException
            {
                Container target = (Container) object;
                if(!target.hasDirect())
                    return null;
                return new Boolean(target.getDirect());
            }
            public void setValue( Object object, Object value)
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    Container target = (Container) object;
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

        //-- _getMethod
        desc = new XMLFieldDescriptorImpl(java.lang.String.class, "_getMethod", "get-method", NodeType.Attribute);
        handler = (new XMLFieldHandler() {
            public Object getValue( Object object )
                throws IllegalStateException
            {
                Container target = (Container) object;
                return target.getGetMethod();
            }
            public void setValue( Object object, Object value)
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    Container target = (Container) object;
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
        fieldValidator.setValidator(new NameValidator(NameValidator.NMTOKEN));
        desc.setValidator(fieldValidator);

        //-- _setMethod
        desc = new XMLFieldDescriptorImpl(java.lang.String.class, "_setMethod", "set-method", NodeType.Attribute);
        handler = (new XMLFieldHandler() {
            public Object getValue( Object object )
                throws IllegalStateException
            {
                Container target = (Container) object;
                return target.getSetMethod();
            }
            public void setValue( Object object, Object value)
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    Container target = (Container) object;
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
        fieldValidator.setValidator(new NameValidator(NameValidator.NMTOKEN));
        desc.setValidator(fieldValidator);

        //-- _createMethod
        desc = new XMLFieldDescriptorImpl(java.lang.String.class, "_createMethod", "create-method", NodeType.Attribute);
        handler = (new XMLFieldHandler() {
            public Object getValue( Object object )
                throws IllegalStateException
            {
                Container target = (Container) object;
                return target.getCreateMethod();
            }
            public void setValue( Object object, Object value)
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    Container target = (Container) object;
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
        fieldValidator.setValidator(new NameValidator(NameValidator.NMTOKEN));
        desc.setValidator(fieldValidator);

        //-- initialize element descriptors

        //-- _description
        desc = new XMLFieldDescriptorImpl(java.lang.String.class, "_description", "description", NodeType.Element);
        desc.setImmutable(true);
        handler = (new XMLFieldHandler() {
            public Object getValue( Object object )
                throws IllegalStateException
            {
                Container target = (Container) object;
                return target.getDescription();
            }
            public void setValue( Object object, Object value)
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    Container target = (Container) object;
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

        //-- _fieldMapping
        desc = new XMLFieldDescriptorImpl(FieldMapping.class, "_fieldMapping", "field", NodeType.Element);
        handler = (new XMLFieldHandler() {
            public Object getValue( Object object )
                throws IllegalStateException
            {
                Container target = (Container) object;
                return target.getFieldMapping();
            }
            public void setValue( Object object, Object value)
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    Container target = (Container) object;
                    target.setFieldMapping( (FieldMapping) value);
                }
                catch (Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            public Object newInstance( Object parent ) {
                return new FieldMapping();
            }
        } );
        desc.setHandler(handler);
        desc.setNameSpaceURI("http://castor.exolab.org/");
        desc.setRequired(true);
        desc.setMultivalued(false);
        addFieldDescriptor(desc);

        //-- validation code for: _fieldMapping
        fieldValidator = new FieldValidator();
        fieldValidator.setMinOccurs(1);
        desc.setValidator(fieldValidator);

    } //-- org.exolab.castor.mapping.xml.ContainerDescriptor()


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
        return org.exolab.castor.mapping.xml.Container.class;
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
