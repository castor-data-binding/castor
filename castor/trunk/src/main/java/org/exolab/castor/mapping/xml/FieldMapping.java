/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.0M1</a>, using an XML
 * Schema.
 * $Id$
 */

package org.exolab.castor.mapping.xml;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import org.exolab.castor.mapping.xml.types.FieldMappingCollectionType;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.ContentHandler;

/**
 * Class FieldMapping.
 * 
 * @version $Revision$ $Date$
 */
public class FieldMapping implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _name
     */
    private java.lang.String _name;

    /**
     * Field _type
     */
    private java.lang.String _type;

    /**
     * Field _required
     */
    private boolean _required = false;

    /**
     * keeps track of state for field: _required
     */
    private boolean _has_required;

    /**
     * Field _transient
     */
    private boolean _transient = false;

    /**
     * keeps track of state for field: _transient
     */
    private boolean _has_transient;

    /**
     * Field _direct
     */
    private boolean _direct = false;

    /**
     * keeps track of state for field: _direct
     */
    private boolean _has_direct;

    /**
     * Field _lazy
     */
    private boolean _lazy = false;

    /**
     * keeps track of state for field: _lazy
     */
    private boolean _has_lazy;

    /**
     * Field _container
     */
    private boolean _container;

    /**
     * keeps track of state for field: _container
     */
    private boolean _has_container;

    /**
     * Field _getMethod
     */
    private java.lang.String _getMethod;

    /**
     * Field _hasMethod
     */
    private java.lang.String _hasMethod;

    /**
     * Field _setMethod
     */
    private java.lang.String _setMethod;

    /**
     * Field _createMethod
     */
    private java.lang.String _createMethod;

    /**
     * Field _handler
     */
    private java.lang.String _handler;

    /**
     * Field _collection
     */
    private org.exolab.castor.mapping.xml.types.FieldMappingCollectionType _collection;

    /**
     * Field _comparator
     */
    private java.lang.String _comparator;

    /**
     * Field _identity
     */
    private boolean _identity = false;

    /**
     * keeps track of state for field: _identity
     */
    private boolean _has_identity;

    /**
     * Field _description
     */
    private java.lang.String _description;

    /**
     * Field _sql
     */
    private org.exolab.castor.mapping.xml.Sql _sql;

    /**
     * The 'bind-xml' element is used for specifying XML specific
     * databinding
     *  properties and behavior for a specific field. 'bind-xml'
     * may only appear
     *  as a child of a 'field' element.
     *  
     */
    private org.exolab.castor.mapping.xml.BindXml _bindXml;

    /**
     * Field _ldap
     */
    private org.exolab.castor.mapping.xml.Ldap _ldap;


      //----------------/
     //- Constructors -/
    //----------------/

    public FieldMapping() 
     {
        super();
    } //-- org.exolab.castor.mapping.xml.FieldMapping()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method deleteContainer
     * 
     */
    public void deleteContainer()
    {
        this._has_container= false;
    } //-- void deleteContainer() 

    /**
     * Method deleteDirect
     * 
     */
    public void deleteDirect()
    {
        this._has_direct= false;
    } //-- void deleteDirect() 

    /**
     * Method deleteIdentity
     * 
     */
    public void deleteIdentity()
    {
        this._has_identity= false;
    } //-- void deleteIdentity() 

    /**
     * Method deleteLazy
     * 
     */
    public void deleteLazy()
    {
        this._has_lazy= false;
    } //-- void deleteLazy() 

    /**
     * Method deleteRequired
     * 
     */
    public void deleteRequired()
    {
        this._has_required= false;
    } //-- void deleteRequired() 

    /**
     * Method deleteTransient
     * 
     */
    public void deleteTransient()
    {
        this._has_transient= false;
    } //-- void deleteTransient() 

    /**
     * Returns the value of field 'bindXml'. The field 'bindXml'
     * has the following description: The 'bind-xml' element is
     * used for specifying XML specific databinding
     *  properties and behavior for a specific field. 'bind-xml'
     * may only appear
     *  as a child of a 'field' element.
     *  
     * 
     * @return BindXml
     * @return the value of field 'bindXml'.
     */
    public org.exolab.castor.mapping.xml.BindXml getBindXml()
    {
        return this._bindXml;
    } //-- org.exolab.castor.mapping.xml.BindXml getBindXml() 

    /**
     * Returns the value of field 'collection'.
     * 
     * @return FieldMappingCollectionType
     * @return the value of field 'collection'.
     */
    public org.exolab.castor.mapping.xml.types.FieldMappingCollectionType getCollection()
    {
        return this._collection;
    } //-- org.exolab.castor.mapping.xml.types.FieldMappingCollectionType getCollection() 

    /**
     * Returns the value of field 'comparator'.
     * 
     * @return String
     * @return the value of field 'comparator'.
     */
    public java.lang.String getComparator()
    {
        return this._comparator;
    } //-- java.lang.String getComparator() 

    /**
     * Returns the value of field 'container'.
     * 
     * @return boolean
     * @return the value of field 'container'.
     */
    public boolean getContainer()
    {
        return this._container;
    } //-- boolean getContainer() 

    /**
     * Returns the value of field 'createMethod'.
     * 
     * @return String
     * @return the value of field 'createMethod'.
     */
    public java.lang.String getCreateMethod()
    {
        return this._createMethod;
    } //-- java.lang.String getCreateMethod() 

    /**
     * Returns the value of field 'description'.
     * 
     * @return String
     * @return the value of field 'description'.
     */
    public java.lang.String getDescription()
    {
        return this._description;
    } //-- java.lang.String getDescription() 

    /**
     * Returns the value of field 'direct'.
     * 
     * @return boolean
     * @return the value of field 'direct'.
     */
    public boolean getDirect()
    {
        return this._direct;
    } //-- boolean getDirect() 

    /**
     * Returns the value of field 'getMethod'.
     * 
     * @return String
     * @return the value of field 'getMethod'.
     */
    public java.lang.String getGetMethod()
    {
        return this._getMethod;
    } //-- java.lang.String getGetMethod() 

    /**
     * Returns the value of field 'handler'.
     * 
     * @return String
     * @return the value of field 'handler'.
     */
    public java.lang.String getHandler()
    {
        return this._handler;
    } //-- java.lang.String getHandler() 

    /**
     * Returns the value of field 'hasMethod'.
     * 
     * @return String
     * @return the value of field 'hasMethod'.
     */
    public java.lang.String getHasMethod()
    {
        return this._hasMethod;
    } //-- java.lang.String getHasMethod() 

    /**
     * Returns the value of field 'identity'.
     * 
     * @return boolean
     * @return the value of field 'identity'.
     */
    public boolean getIdentity()
    {
        return this._identity;
    } //-- boolean getIdentity() 

    /**
     * Returns the value of field 'lazy'.
     * 
     * @return boolean
     * @return the value of field 'lazy'.
     */
    public boolean getLazy()
    {
        return this._lazy;
    } //-- boolean getLazy() 

    /**
     * Returns the value of field 'ldap'.
     * 
     * @return Ldap
     * @return the value of field 'ldap'.
     */
    public org.exolab.castor.mapping.xml.Ldap getLdap()
    {
        return this._ldap;
    } //-- org.exolab.castor.mapping.xml.Ldap getLdap() 

    /**
     * Returns the value of field 'name'.
     * 
     * @return String
     * @return the value of field 'name'.
     */
    public java.lang.String getName()
    {
        return this._name;
    } //-- java.lang.String getName() 

    /**
     * Returns the value of field 'required'.
     * 
     * @return boolean
     * @return the value of field 'required'.
     */
    public boolean getRequired()
    {
        return this._required;
    } //-- boolean getRequired() 

    /**
     * Returns the value of field 'setMethod'.
     * 
     * @return String
     * @return the value of field 'setMethod'.
     */
    public java.lang.String getSetMethod()
    {
        return this._setMethod;
    } //-- java.lang.String getSetMethod() 

    /**
     * Returns the value of field 'sql'.
     * 
     * @return Sql
     * @return the value of field 'sql'.
     */
    public org.exolab.castor.mapping.xml.Sql getSql()
    {
        return this._sql;
    } //-- org.exolab.castor.mapping.xml.Sql getSql() 

    /**
     * Returns the value of field 'transient'.
     * 
     * @return boolean
     * @return the value of field 'transient'.
     */
    public boolean getTransient()
    {
        return this._transient;
    } //-- boolean getTransient() 

    /**
     * Returns the value of field 'type'.
     * 
     * @return String
     * @return the value of field 'type'.
     */
    public java.lang.String getType()
    {
        return this._type;
    } //-- java.lang.String getType() 

    /**
     * Method hasContainer
     * 
     * 
     * 
     * @return boolean
     */
    public boolean hasContainer()
    {
        return this._has_container;
    } //-- boolean hasContainer() 

    /**
     * Method hasDirect
     * 
     * 
     * 
     * @return boolean
     */
    public boolean hasDirect()
    {
        return this._has_direct;
    } //-- boolean hasDirect() 

    /**
     * Method hasIdentity
     * 
     * 
     * 
     * @return boolean
     */
    public boolean hasIdentity()
    {
        return this._has_identity;
    } //-- boolean hasIdentity() 

    /**
     * Method hasLazy
     * 
     * 
     * 
     * @return boolean
     */
    public boolean hasLazy()
    {
        return this._has_lazy;
    } //-- boolean hasLazy() 

    /**
     * Method hasRequired
     * 
     * 
     * 
     * @return boolean
     */
    public boolean hasRequired()
    {
        return this._has_required;
    } //-- boolean hasRequired() 

    /**
     * Method hasTransient
     * 
     * 
     * 
     * @return boolean
     */
    public boolean hasTransient()
    {
        return this._has_transient;
    } //-- boolean hasTransient() 

    /**
     * Method isValid
     * 
     * 
     * 
     * @return boolean
     */
    public boolean isValid()
    {
        try {
            validate();
        }
        catch (org.exolab.castor.xml.ValidationException vex) {
            return false;
        }
        return true;
    } //-- boolean isValid() 

    /**
     * Method marshal
     * 
     * 
     * 
     * @param out
     */
    public void marshal(java.io.Writer out)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        
        Marshaller.marshal(this, out);
    } //-- void marshal(java.io.Writer) 

    /**
     * Method marshal
     * 
     * 
     * 
     * @param handler
     */
    public void marshal(org.xml.sax.ContentHandler handler)
        throws java.io.IOException, org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        
        Marshaller.marshal(this, handler);
    } //-- void marshal(org.xml.sax.ContentHandler) 

    /**
     * Sets the value of field 'bindXml'. The field 'bindXml' has
     * the following description: The 'bind-xml' element is used
     * for specifying XML specific databinding
     *  properties and behavior for a specific field. 'bind-xml'
     * may only appear
     *  as a child of a 'field' element.
     *  
     * 
     * @param bindXml the value of field 'bindXml'.
     */
    public void setBindXml(org.exolab.castor.mapping.xml.BindXml bindXml)
    {
        this._bindXml = bindXml;
    } //-- void setBindXml(org.exolab.castor.mapping.xml.BindXml) 

    /**
     * Sets the value of field 'collection'.
     * 
     * @param collection the value of field 'collection'.
     */
    public void setCollection(org.exolab.castor.mapping.xml.types.FieldMappingCollectionType collection)
    {
        this._collection = collection;
    } //-- void setCollection(org.exolab.castor.mapping.xml.types.FieldMappingCollectionType) 

    /**
     * Sets the value of field 'comparator'.
     * 
     * @param comparator the value of field 'comparator'.
     */
    public void setComparator(java.lang.String comparator)
    {
        this._comparator = comparator;
    } //-- void setComparator(java.lang.String) 

    /**
     * Sets the value of field 'container'.
     * 
     * @param container the value of field 'container'.
     */
    public void setContainer(boolean container)
    {
        this._container = container;
        this._has_container = true;
    } //-- void setContainer(boolean) 

    /**
     * Sets the value of field 'createMethod'.
     * 
     * @param createMethod the value of field 'createMethod'.
     */
    public void setCreateMethod(java.lang.String createMethod)
    {
        this._createMethod = createMethod;
    } //-- void setCreateMethod(java.lang.String) 

    /**
     * Sets the value of field 'description'.
     * 
     * @param description the value of field 'description'.
     */
    public void setDescription(java.lang.String description)
    {
        this._description = description;
    } //-- void setDescription(java.lang.String) 

    /**
     * Sets the value of field 'direct'.
     * 
     * @param direct the value of field 'direct'.
     */
    public void setDirect(boolean direct)
    {
        this._direct = direct;
        this._has_direct = true;
    } //-- void setDirect(boolean) 

    /**
     * Sets the value of field 'getMethod'.
     * 
     * @param getMethod the value of field 'getMethod'.
     */
    public void setGetMethod(java.lang.String getMethod)
    {
        this._getMethod = getMethod;
    } //-- void setGetMethod(java.lang.String) 

    /**
     * Sets the value of field 'handler'.
     * 
     * @param handler the value of field 'handler'.
     */
    public void setHandler(java.lang.String handler)
    {
        this._handler = handler;
    } //-- void setHandler(java.lang.String) 

    /**
     * Sets the value of field 'hasMethod'.
     * 
     * @param hasMethod the value of field 'hasMethod'.
     */
    public void setHasMethod(java.lang.String hasMethod)
    {
        this._hasMethod = hasMethod;
    } //-- void setHasMethod(java.lang.String) 

    /**
     * Sets the value of field 'identity'.
     * 
     * @param identity the value of field 'identity'.
     */
    public void setIdentity(boolean identity)
    {
        this._identity = identity;
        this._has_identity = true;
    } //-- void setIdentity(boolean) 

    /**
     * Sets the value of field 'lazy'.
     * 
     * @param lazy the value of field 'lazy'.
     */
    public void setLazy(boolean lazy)
    {
        this._lazy = lazy;
        this._has_lazy = true;
    } //-- void setLazy(boolean) 

    /**
     * Sets the value of field 'ldap'.
     * 
     * @param ldap the value of field 'ldap'.
     */
    public void setLdap(org.exolab.castor.mapping.xml.Ldap ldap)
    {
        this._ldap = ldap;
    } //-- void setLdap(org.exolab.castor.mapping.xml.Ldap) 

    /**
     * Sets the value of field 'name'.
     * 
     * @param name the value of field 'name'.
     */
    public void setName(java.lang.String name)
    {
        this._name = name;
    } //-- void setName(java.lang.String) 

    /**
     * Sets the value of field 'required'.
     * 
     * @param required the value of field 'required'.
     */
    public void setRequired(boolean required)
    {
        this._required = required;
        this._has_required = true;
    } //-- void setRequired(boolean) 

    /**
     * Sets the value of field 'setMethod'.
     * 
     * @param setMethod the value of field 'setMethod'.
     */
    public void setSetMethod(java.lang.String setMethod)
    {
        this._setMethod = setMethod;
    } //-- void setSetMethod(java.lang.String) 

    /**
     * Sets the value of field 'sql'.
     * 
     * @param sql the value of field 'sql'.
     */
    public void setSql(org.exolab.castor.mapping.xml.Sql sql)
    {
        this._sql = sql;
    } //-- void setSql(org.exolab.castor.mapping.xml.Sql) 

    /**
     * Sets the value of field 'transient'.
     * 
     * @param _transient
     * @param transient the value of field 'transient'.
     */
    public void setTransient(boolean _transient)
    {
        this._transient = _transient;
        this._has_transient = true;
    } //-- void setTransient(boolean) 

    /**
     * Sets the value of field 'type'.
     * 
     * @param type the value of field 'type'.
     */
    public void setType(java.lang.String type)
    {
        this._type = type;
    } //-- void setType(java.lang.String) 

    /**
     * Method unmarshal
     * 
     * 
     * 
     * @param reader
     * @return FieldMapping
     */
    public static org.exolab.castor.mapping.xml.FieldMapping unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.exolab.castor.mapping.xml.FieldMapping) Unmarshaller.unmarshal(org.exolab.castor.mapping.xml.FieldMapping.class, reader);
    } //-- org.exolab.castor.mapping.xml.FieldMapping unmarshal(java.io.Reader) 

    /**
     * Method validate
     * 
     */
    public void validate()
        throws org.exolab.castor.xml.ValidationException
    {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    } //-- void validate() 

}
