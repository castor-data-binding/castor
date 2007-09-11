/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.1.2.1</a>, using an XML
 * Schema.
 * $Id$
 */

package org.exolab.castor.mapping.xml;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

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
     * Field _table.
     */
    private java.lang.String _table;

    /**
     * Field _xml.
     */
    private java.lang.String _xml;

    /**
     * Field _nsUri.
     */
    private java.lang.String _nsUri;

    /**
     * Field _nsPrefix.
     */
    private java.lang.String _nsPrefix;

    /**
     * Field _elementDefinition.
     */
    private boolean _elementDefinition = false;

    /**
     * keeps track of state for field: _elementDefinition
     */
    private boolean _has_elementDefinition;

    /**
     * Field _ldapDn.
     */
    private java.lang.String _ldapDn;

    /**
     * Field _ldapOc.
     */
    private java.lang.String _ldapOc;


      //----------------/
     //- Constructors -/
    //----------------/

    public MapTo() {
        super();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     */
    public void deleteElementDefinition(
    ) {
        this._has_elementDefinition= false;
    }

    /**
     * Returns the value of field 'elementDefinition'.
     * 
     * @return the value of field 'ElementDefinition'.
     */
    public boolean getElementDefinition(
    ) {
        return this._elementDefinition;
    }

    /**
     * Returns the value of field 'ldapDn'.
     * 
     * @return the value of field 'LdapDn'.
     */
    public java.lang.String getLdapDn(
    ) {
        return this._ldapDn;
    }

    /**
     * Returns the value of field 'ldapOc'.
     * 
     * @return the value of field 'LdapOc'.
     */
    public java.lang.String getLdapOc(
    ) {
        return this._ldapOc;
    }

    /**
     * Returns the value of field 'nsPrefix'.
     * 
     * @return the value of field 'NsPrefix'.
     */
    public java.lang.String getNsPrefix(
    ) {
        return this._nsPrefix;
    }

    /**
     * Returns the value of field 'nsUri'.
     * 
     * @return the value of field 'NsUri'.
     */
    public java.lang.String getNsUri(
    ) {
        return this._nsUri;
    }

    /**
     * Returns the value of field 'table'.
     * 
     * @return the value of field 'Table'.
     */
    public java.lang.String getTable(
    ) {
        return this._table;
    }

    /**
     * Returns the value of field 'xml'.
     * 
     * @return the value of field 'Xml'.
     */
    public java.lang.String getXml(
    ) {
        return this._xml;
    }

    /**
     * Method hasElementDefinition.
     * 
     * @return true if at least one ElementDefinition has been added
     */
    public boolean hasElementDefinition(
    ) {
        return this._has_elementDefinition;
    }

    /**
     * Returns the value of field 'elementDefinition'.
     * 
     * @return the value of field 'ElementDefinition'.
     */
    public boolean isElementDefinition(
    ) {
        return this._elementDefinition;
    }

    /**
     * Method isValid.
     * 
     * @return true if this object is valid according to the schema
     */
    public boolean isValid(
    ) {
        try {
            validate();
        } catch (org.exolab.castor.xml.ValidationException vex) {
            return false;
        }
        return true;
    }

    /**
     * 
     * 
     * @param out
     * @throws org.exolab.castor.xml.MarshalException if object is
     * null or if any SAXException is thrown during marshaling
     * @throws org.exolab.castor.xml.ValidationException if this
     * object is an invalid instance according to the schema
     */
    public void marshal(
            final java.io.Writer out)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        Marshaller.marshal(this, out);
    }

    /**
     * 
     * 
     * @param handler
     * @throws java.io.IOException if an IOException occurs during
     * marshaling
     * @throws org.exolab.castor.xml.ValidationException if this
     * object is an invalid instance according to the schema
     * @throws org.exolab.castor.xml.MarshalException if object is
     * null or if any SAXException is thrown during marshaling
     */
    public void marshal(
            final org.xml.sax.ContentHandler handler)
    throws java.io.IOException, org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        Marshaller.marshal(this, handler);
    }

    /**
     * Sets the value of field 'elementDefinition'.
     * 
     * @param elementDefinition the value of field
     * 'elementDefinition'.
     */
    public void setElementDefinition(
            final boolean elementDefinition) {
        this._elementDefinition = elementDefinition;
        this._has_elementDefinition = true;
    }

    /**
     * Sets the value of field 'ldapDn'.
     * 
     * @param ldapDn the value of field 'ldapDn'.
     */
    public void setLdapDn(
            final java.lang.String ldapDn) {
        this._ldapDn = ldapDn;
    }

    /**
     * Sets the value of field 'ldapOc'.
     * 
     * @param ldapOc the value of field 'ldapOc'.
     */
    public void setLdapOc(
            final java.lang.String ldapOc) {
        this._ldapOc = ldapOc;
    }

    /**
     * Sets the value of field 'nsPrefix'.
     * 
     * @param nsPrefix the value of field 'nsPrefix'.
     */
    public void setNsPrefix(
            final java.lang.String nsPrefix) {
        this._nsPrefix = nsPrefix;
    }

    /**
     * Sets the value of field 'nsUri'.
     * 
     * @param nsUri the value of field 'nsUri'.
     */
    public void setNsUri(
            final java.lang.String nsUri) {
        this._nsUri = nsUri;
    }

    /**
     * Sets the value of field 'table'.
     * 
     * @param table the value of field 'table'.
     */
    public void setTable(
            final java.lang.String table) {
        this._table = table;
    }

    /**
     * Sets the value of field 'xml'.
     * 
     * @param xml the value of field 'xml'.
     */
    public void setXml(
            final java.lang.String xml) {
        this._xml = xml;
    }

    /**
     * Method unmarshal.
     * 
     * @param reader
     * @throws org.exolab.castor.xml.MarshalException if object is
     * null or if any SAXException is thrown during marshaling
     * @throws org.exolab.castor.xml.ValidationException if this
     * object is an invalid instance according to the schema
     * @return the unmarshaled org.exolab.castor.mapping.xml.MapTo
     */
    public static org.exolab.castor.mapping.xml.MapTo unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (org.exolab.castor.mapping.xml.MapTo) Unmarshaller.unmarshal(org.exolab.castor.mapping.xml.MapTo.class, reader);
    }

    /**
     * 
     * 
     * @throws org.exolab.castor.xml.ValidationException if this
     * object is an invalid instance according to the schema
     */
    public void validate(
    )
    throws org.exolab.castor.xml.ValidationException {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    }

}
