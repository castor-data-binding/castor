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

import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import org.exolab.castor.xml.*;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.DocumentHandler;

/**
 * 
 * @version $Revision$ $Date$
**/
public class FieldMapping implements java.io.Serializable {


      //--------------------/
     //- Member Variables -/
    //--------------------/

    private java.lang.String _name;

    private boolean _lazy = false;

    private boolean _direct = false;

    private boolean _transient = false;

    private java.lang.String _collection;

    private java.lang.String _setMethod;

    private java.lang.String _getMethod;

    private boolean _required = false;

    private java.lang.String _createMethod;

    private java.lang.String _type;

    private java.lang.Object _manyToMany;

    private java.lang.String _description;

    private Sql _sql;

    private Xml _xml;

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
    public java.lang.String getCollection() {
        return this._collection;
    } //-- java.lang.String getCollection() 

    /**
    **/
    public java.lang.String getCreateMethod() {
        return this._createMethod;
    } //-- java.lang.String getCreateMethod() 

    /**
    **/
    public java.lang.String getDescription() {
        return this._description;
    } //-- java.lang.String getDescription() 

    /**
    **/
    public boolean getDirect() {
        return this._direct;
    } //-- boolean getDirect() 

    /**
    **/
    public java.lang.String getGetMethod() {
        return this._getMethod;
    } //-- java.lang.String getGetMethod() 

    /**
    **/
    public boolean getLazy() {
        return this._lazy;
    } //-- boolean getLazy() 

    /**
    **/
    public Ldap getLdap() {
        return this._ldap;
    } //-- Ldap getLdap() 

    /**
    **/
    public java.lang.Object getManyToMany() {
        return this._manyToMany;
    } //-- java.lang.Object getManyToMany() 

    /**
    **/
    public java.lang.String getName() {
        return this._name;
    } //-- java.lang.String getName() 

    /**
    **/
    public boolean getRequired() {
        return this._required;
    } //-- boolean getRequired() 

    /**
    **/
    public java.lang.String getSetMethod() {
        return this._setMethod;
    } //-- java.lang.String getSetMethod() 

    /**
    **/
    public Sql getSql() {
        return this._sql;
    } //-- Sql getSql() 

    /**
    **/
    public boolean getTransient() {
        return this._transient;
    } //-- boolean getTransient() 

    /**
    **/
    public java.lang.String getType() {
        return this._type;
    } //-- java.lang.String getType() 

    /**
    **/
    public Xml getXml() {
        return this._xml;
    } //-- Xml getXml() 

    /**
    **/
    public boolean isValid() {
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
        //-- we must have a valid element before marshalling
        //validate(false);
        
        Marshaller.marshal(this, out);
    } //-- void marshal(java.io.Writer) 

    /**
     * 
     * @param handler
    **/
    public void marshal(org.xml.sax.DocumentHandler handler) 
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        //-- we must have a valid element before marshalling
        //validate(false);
        
        Marshaller.marshal(this, handler);
    } //-- void marshal(org.xml.sax.DocumentHandler) 

    /**
     * 
     * @param _collection
    **/
    public void setCollection(java.lang.String _collection) {
        this._collection = _collection;
    } //-- void setCollection(java.lang.String) 

    /**
     * 
     * @param _createMethod
    **/
    public void setCreateMethod(java.lang.String _createMethod) {
        this._createMethod = _createMethod;
    } //-- void setCreateMethod(java.lang.String) 

    /**
     * 
     * @param _description
    **/
    public void setDescription(java.lang.String _description) {
        this._description = _description;
    } //-- void setDescription(java.lang.String) 

    /**
     * 
     * @param _direct
    **/
    public void setDirect(boolean _direct) {
        this._direct = _direct;
    } //-- void setDirect(boolean) 

    /**
     * 
     * @param _getMethod
    **/
    public void setGetMethod(java.lang.String _getMethod) {
        this._getMethod = _getMethod;
    } //-- void setGetMethod(java.lang.String) 

    /**
     * 
     * @param _lazy
    **/
    public void setLazy(boolean _lazy) {
        this._lazy = _lazy;
    } //-- void setLazy(boolean) 

    /**
     * 
     * @param _ldap
    **/
    public void setLdap(Ldap _ldap) {
        this._ldap = _ldap;
    } //-- void setLdap(Ldap) 

    /**
     * 
     * @param _manyToMany
    **/
    public void setManyToMany(java.lang.Object _manyToMany) {
        this._manyToMany = _manyToMany;
    } //-- void setManyToMany(java.lang.Object) 

    /**
     * 
     * @param _name
    **/
    public void setName(java.lang.String _name) {
        this._name = _name;
    } //-- void setName(java.lang.String) 

    /**
     * 
     * @param _required
    **/
    public void setRequired(boolean _required) {
        this._required = _required;
    } //-- void setRequired(boolean) 

    /**
     * 
     * @param _setMethod
    **/
    public void setSetMethod(java.lang.String _setMethod) {
        this._setMethod = _setMethod;
    } //-- void setSetMethod(java.lang.String) 

    /**
     * 
     * @param _sql
    **/
    public void setSql(Sql _sql) {
        this._sql = _sql;
    } //-- void setSql(Sql) 

    /**
     * 
     * @param _transient
    **/
    public void setTransient(boolean _transient) {
        this._transient = _transient;
    } //-- void setTransient(boolean) 

    /**
     * 
     * @param _type
    **/
    public void setType(java.lang.String _type) {
        this._type = _type;
    } //-- void setType(java.lang.String) 

    /**
     * 
     * @param _xml
    **/
    public void setXml(Xml _xml) {
        this._xml = _xml;
    } //-- void setXml(Xml) 

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
        org.exolab.castor.xml.Validator.validate(this, null);
    } //-- void validate() 

}
