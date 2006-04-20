/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.0M2</a>, using an XML
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
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.ContentHandler;

/**
 * Class MapTo.
 * 
 * @version $Revision$ $Date$
 */
public class MapTo implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _table
     */
    private java.lang.String _table;

    /**
     * Field _xml
     */
    private java.lang.String _xml;

    /**
     * Field _nsUri
     */
    private java.lang.String _nsUri;

    /**
     * Field _nsPrefix
     */
    private java.lang.String _nsPrefix;

    /**
     * Field _elementDefinition
     */
    private boolean _elementDefinition = false;

    /**
     * keeps track of state for field: _elementDefinition
     */
    private boolean _has_elementDefinition;

    /**
     * Field _ldapDn
     */
    private java.lang.String _ldapDn;

    /**
     * Field _ldapOc
     */
    private java.lang.String _ldapOc;


      //----------------/
     //- Constructors -/
    //----------------/

    public MapTo() 
     {
        super();
    } //-- org.exolab.castor.mapping.xml.MapTo()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method deleteElementDefinition
     * 
     */
    public void deleteElementDefinition()
    {
        this._has_elementDefinition= false;
    } //-- void deleteElementDefinition() 

    /**
     * Returns the value of field 'elementDefinition'.
     * 
     * @return boolean
     * @return the value of field 'elementDefinition'.
     */
    public boolean getElementDefinition()
    {
        return this._elementDefinition;
    } //-- boolean getElementDefinition() 

    /**
     * Returns the value of field 'ldapDn'.
     * 
     * @return String
     * @return the value of field 'ldapDn'.
     */
    public java.lang.String getLdapDn()
    {
        return this._ldapDn;
    } //-- java.lang.String getLdapDn() 

    /**
     * Returns the value of field 'ldapOc'.
     * 
     * @return String
     * @return the value of field 'ldapOc'.
     */
    public java.lang.String getLdapOc()
    {
        return this._ldapOc;
    } //-- java.lang.String getLdapOc() 

    /**
     * Returns the value of field 'nsPrefix'.
     * 
     * @return String
     * @return the value of field 'nsPrefix'.
     */
    public java.lang.String getNsPrefix()
    {
        return this._nsPrefix;
    } //-- java.lang.String getNsPrefix() 

    /**
     * Returns the value of field 'nsUri'.
     * 
     * @return String
     * @return the value of field 'nsUri'.
     */
    public java.lang.String getNsUri()
    {
        return this._nsUri;
    } //-- java.lang.String getNsUri() 

    /**
     * Returns the value of field 'table'.
     * 
     * @return String
     * @return the value of field 'table'.
     */
    public java.lang.String getTable()
    {
        return this._table;
    } //-- java.lang.String getTable() 

    /**
     * Returns the value of field 'xml'.
     * 
     * @return String
     * @return the value of field 'xml'.
     */
    public java.lang.String getXml()
    {
        return this._xml;
    } //-- java.lang.String getXml() 

    /**
     * Method hasElementDefinition
     * 
     * 
     * 
     * @return boolean
     */
    public boolean hasElementDefinition()
    {
        return this._has_elementDefinition;
    } //-- boolean hasElementDefinition() 

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
     * Sets the value of field 'elementDefinition'.
     * 
     * @param elementDefinition the value of field
     * 'elementDefinition'.
     */
    public void setElementDefinition(boolean elementDefinition)
    {
        this._elementDefinition = elementDefinition;
        this._has_elementDefinition = true;
    } //-- void setElementDefinition(boolean) 

    /**
     * Sets the value of field 'ldapDn'.
     * 
     * @param ldapDn the value of field 'ldapDn'.
     */
    public void setLdapDn(java.lang.String ldapDn)
    {
        this._ldapDn = ldapDn;
    } //-- void setLdapDn(java.lang.String) 

    /**
     * Sets the value of field 'ldapOc'.
     * 
     * @param ldapOc the value of field 'ldapOc'.
     */
    public void setLdapOc(java.lang.String ldapOc)
    {
        this._ldapOc = ldapOc;
    } //-- void setLdapOc(java.lang.String) 

    /**
     * Sets the value of field 'nsPrefix'.
     * 
     * @param nsPrefix the value of field 'nsPrefix'.
     */
    public void setNsPrefix(java.lang.String nsPrefix)
    {
        this._nsPrefix = nsPrefix;
    } //-- void setNsPrefix(java.lang.String) 

    /**
     * Sets the value of field 'nsUri'.
     * 
     * @param nsUri the value of field 'nsUri'.
     */
    public void setNsUri(java.lang.String nsUri)
    {
        this._nsUri = nsUri;
    } //-- void setNsUri(java.lang.String) 

    /**
     * Sets the value of field 'table'.
     * 
     * @param table the value of field 'table'.
     */
    public void setTable(java.lang.String table)
    {
        this._table = table;
    } //-- void setTable(java.lang.String) 

    /**
     * Sets the value of field 'xml'.
     * 
     * @param xml the value of field 'xml'.
     */
    public void setXml(java.lang.String xml)
    {
        this._xml = xml;
    } //-- void setXml(java.lang.String) 

    /**
     * Method unmarshal
     * 
     * 
     * 
     * @param reader
     * @return MapTo
     */
    public static org.exolab.castor.mapping.xml.MapTo unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.exolab.castor.mapping.xml.MapTo) Unmarshaller.unmarshal(org.exolab.castor.mapping.xml.MapTo.class, reader);
    } //-- org.exolab.castor.mapping.xml.MapTo unmarshal(java.io.Reader) 

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
