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

import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import org.exolab.castor.mapping.xml.types.CollectionType;
import org.exolab.castor.xml.*;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.DocumentHandler;

/**
 * 
 * @version $Revision$ $Date$
**/
public class FieldMapping implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    private java.lang.String _name;

    private java.lang.String _type;

    private boolean _required = false;

    /**
     * keeps track of state for field: _required
    **/
    private boolean _has_required;

    private boolean _transient = false;

    /**
     * keeps track of state for field: _transient
    **/
    private boolean _has_transient;

    private boolean _direct = false;

    /**
     * keeps track of state for field: _direct
    **/
    private boolean _has_direct;

    private boolean _lazy = false;

    /**
     * keeps track of state for field: _lazy
    **/
    private boolean _has_lazy;


    private boolean _container = false;

    /**
     * keeps track of state for field: _container
    **/
    private boolean _has_container;


    private java.lang.String _getMethod;

    private java.lang.String _setMethod;

    private java.lang.String _createMethod;

    private org.exolab.castor.mapping.xml.types.CollectionType _collection;

    private java.lang.String _description;

    private Sql _sql;

    private BindXml _bindXml;

    private Ldap _ldap;


      //----------------/
     //- Constructors -/
    //----------------/

    public FieldMapping() {
        super();
    } //-- org.exolab.castor.mapping.xml.FieldMapping()


      //-----------/
     //- Methods -/
    //-----------/

    /**
    **/
    public void deleteDirect()
    {
        this._has_direct= false;
    } //-- void deleteDirect() 

    /**
    **/
    public void deleteLazy()
    {
        this._has_lazy= false;
    } //-- void deleteLazy() 

    /**
    **/
    public void deleteContainer()
    {
        this._has_container= false;
    } //-- void deleteContainer() 

    /**
    **/
    public void deleteRequired()
    {
        this._has_required= false;
    } //-- void deleteRequired() 

    /**
    **/
    public void deleteTransient()
    {
        this._has_transient= false;
    } //-- void deleteTransient() 

    /**
    **/
    public BindXml getBindXml()
    {
        return this._bindXml;
    } //-- BindXml getBindXml() 

    /**
    **/
    public org.exolab.castor.mapping.xml.types.CollectionType getCollection()
    {
        return this._collection;
    } //-- org.exolab.castor.mapping.xml.types.CollectionType getCollection() 

    /**
    **/
    public java.lang.String getCreateMethod()
    {
        return this._createMethod;
    } //-- java.lang.String getCreateMethod() 

    /**
    **/
    public java.lang.String getDescription()
    {
        return this._description;
    } //-- java.lang.String getDescription() 

    /**
    **/
    public boolean getDirect()
    {
        return this._direct;
    } //-- boolean getDirect() 

    /**
    **/
    public java.lang.String getGetMethod()
    {
        return this._getMethod;
    } //-- java.lang.String getGetMethod() 

    /**
    **/
    public boolean getLazy()
    {
        return this._lazy;
    } //-- boolean getLazy() 

    /**
    **/
    public boolean getContainer()
    {
        return this._container;
    } //-- boolean getContainer() 

    /**
    **/
    public Ldap getLdap()
    {
        return this._ldap;
    } //-- Ldap getLdap() 

    /**
    **/
    public java.lang.String getName()
    {
        return this._name;
    } //-- java.lang.String getName() 

    /**
    **/
    public boolean getRequired()
    {
        return this._required;
    } //-- boolean getRequired() 

    /**
    **/
    public java.lang.String getSetMethod()
    {
        return this._setMethod;
    } //-- java.lang.String getSetMethod() 

    /**
    **/
    public Sql getSql()
    {
        return this._sql;
    } //-- Sql getSql() 

    /**
    **/
    public boolean getTransient()
    {
        return this._transient;
    } //-- boolean getTransient() 

    /**
    **/
    public java.lang.String getType()
    {
        return this._type;
    } //-- java.lang.String getType() 

    /**
    **/
    public boolean hasDirect()
    {
        return this._has_direct;
    } //-- boolean hasDirect() 

    /**
    **/
    public boolean hasLazy()
    {
        return this._has_lazy;
    } //-- boolean hasLazy() 

    /**
    **/
    public boolean hasContainer()
    {
        return this._has_container;
    } //-- boolean hasContainer() 

    /**
    **/
    public boolean hasRequired()
    {
        return this._has_required;
    } //-- boolean hasRequired() 

    /**
    **/
    public boolean hasTransient()
    {
        return this._has_transient;
    } //-- boolean hasTransient() 

    /**
    **/
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
     * 
     * @param out
    **/
    public void marshal(java.io.Writer out)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        
        Marshaller.marshal(this, out);
    } //-- void marshal(java.io.Writer) 

    /**
     * 
     * @param handler
    **/
    public void marshal(org.xml.sax.DocumentHandler handler)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        
        Marshaller.marshal(this, handler);
    } //-- void marshal(org.xml.sax.DocumentHandler) 

    /**
     * 
     * @param _bindXml
    **/
    public void setBindXml(BindXml _bindXml)
    {
        this._bindXml = _bindXml;
    } //-- void setBindXml(BindXml) 

    /**
     * 
     * @param _collection
    **/
    public void setCollection(org.exolab.castor.mapping.xml.types.CollectionType _collection)
    {
        this._collection = _collection;
    } //-- void setCollection(org.exolab.castor.mapping.xml.types.CollectionType) 

    /**
     * 
     * @param _createMethod
    **/
    public void setCreateMethod(java.lang.String _createMethod)
    {
        this._createMethod = _createMethod;
    } //-- void setCreateMethod(java.lang.String) 

    /**
     * 
     * @param _description
    **/
    public void setDescription(java.lang.String _description)
    {
        this._description = _description;
    } //-- void setDescription(java.lang.String) 

    /**
     * 
     * @param _direct
    **/
    public void setDirect(boolean _direct)
    {
        this._direct = _direct;
        this._has_direct = true;
    } //-- void setDirect(boolean) 

    /**
     * 
     * @param _getMethod
    **/
    public void setGetMethod(java.lang.String _getMethod)
    {
        this._getMethod = _getMethod;
    } //-- void setGetMethod(java.lang.String) 

    /**
     * 
     * @param _lazy
    **/
    public void setLazy(boolean _lazy)
    {
        this._lazy = _lazy;
        this._has_lazy = true;
    } //-- void setLazy(boolean) 

    /**
     * 
     * @param _container
    **/
    public void setContainer(boolean _container)
    {
        this._container = _container;
        this._has_container = true;
    } //-- void setContainer(boolean) 

    /**
     * 
     * @param _ldap
    **/
    public void setLdap(Ldap _ldap)
    {
        this._ldap = _ldap;
    } //-- void setLdap(Ldap) 

    /**
     * 
     * @param _name
    **/
    public void setName(java.lang.String _name)
    {
        this._name = _name;
    } //-- void setName(java.lang.String) 

    /**
     * 
     * @param _required
    **/
    public void setRequired(boolean _required)
    {
        this._required = _required;
        this._has_required = true;
    } //-- void setRequired(boolean) 

    /**
     * 
     * @param _setMethod
    **/
    public void setSetMethod(java.lang.String _setMethod)
    {
        this._setMethod = _setMethod;
    } //-- void setSetMethod(java.lang.String) 

    /**
     * 
     * @param _sql
    **/
    public void setSql(Sql _sql)
    {
        this._sql = _sql;
    } //-- void setSql(Sql) 

    /**
     * 
     * @param _transient
    **/
    public void setTransient(boolean _transient)
    {
        this._transient = _transient;
        this._has_transient = true;
    } //-- void setTransient(boolean) 

    /**
     * 
     * @param _type
    **/
    public void setType(java.lang.String _type)
    {
        this._type = _type;
    } //-- void setType(java.lang.String) 

    /**
     * 
     * @param reader
    **/
    public static org.exolab.castor.mapping.xml.FieldMapping unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.exolab.castor.mapping.xml.FieldMapping) Unmarshaller.unmarshal(org.exolab.castor.mapping.xml.FieldMapping.class, reader);
    } //-- org.exolab.castor.mapping.xml.FieldMapping unmarshal(java.io.Reader) 

    /**
    **/
    public void validate()
        throws org.exolab.castor.xml.ValidationException
    {
        Validator validator = new Validator();
        validator.validate(this);
    } //-- void validate() 

}
