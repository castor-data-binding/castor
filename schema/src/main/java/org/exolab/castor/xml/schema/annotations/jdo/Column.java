/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.1.2.1</a>, using an XML
 * Schema.
 * $Id$
 */

package org.exolab.castor.xml.schema.annotations.jdo;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

/**
 * Element 'column' is used to specify the column where the
 *  property of an object will be saved.
 *  
 * 
 * @version $Revision$ $Date$
 */
public class Column implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Attribute 'name' is used to specify the name of
     *  the column.
     *  
     */
    private java.lang.String _name;

    /**
     * Attribute 'type' is used to specify the JDO-type
     *  of the column.
     *  
     */
    private java.lang.String _type;

    /**
     * Attribute 'read-only' is used to set off
     *  changing the column. If true, no update can be
     *  performed.
     *  
     */
    private boolean _readOnly = false;

    /**
     * keeps track of state for field: _readOnly
     */
    private boolean _has_readOnly;

    /**
     * Specifies if this field accepts NULL values or
     *  not.
     *  
     */
    private boolean _acceptNull = true;

    /**
     * keeps track of state for field: _acceptNull
     */
    private boolean _has_acceptNull;

    /**
     * Field propertyChangeSupport.
     */
    private java.beans.PropertyChangeSupport propertyChangeSupport;


      //----------------/
     //- Constructors -/
    //----------------/

    public Column() {
        super();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Registers a PropertyChangeListener with this class.
     * 
     * @param pcl The PropertyChangeListener to register.
     */
    public void addPropertyChangeListener(
            final java.beans.PropertyChangeListener pcl) {
        if (propertyChangeSupport == null) {
            propertyChangeSupport = new java.beans.PropertyChangeSupport(this);
        }
        propertyChangeSupport.addPropertyChangeListener(pcl);
    }

    /**
     */
    public void deleteAcceptNull(
    ) {
        this._has_acceptNull= false;
        notifyPropertyChangeListeners("acceptNull", (this._acceptNull ? java.lang.Boolean.TRUE : java.lang.Boolean.FALSE), null);
    }

    /**
     */
    public void deleteReadOnly(
    ) {
        this._has_readOnly= false;
        notifyPropertyChangeListeners("readOnly", (this._readOnly ? java.lang.Boolean.TRUE : java.lang.Boolean.FALSE), null);
    }

    /**
     * Returns the value of field 'acceptNull'. The field
     * 'acceptNull' has the following description: Specifies if
     * this field accepts NULL values or
     *  not.
     *  
     * 
     * @return the value of field 'AcceptNull'.
     */
    public boolean getAcceptNull(
    ) {
        return this._acceptNull;
    }

    /**
     * Returns the value of field 'name'. The field 'name' has the
     * following description: Attribute 'name' is used to specify
     * the name of
     *  the column.
     *  
     * 
     * @return the value of field 'Name'.
     */
    public java.lang.String getName(
    ) {
        return this._name;
    }

    /**
     * Returns the value of field 'readOnly'. The field 'readOnly'
     * has the following description: Attribute 'read-only' is used
     * to set off
     *  changing the column. If true, no update can be
     *  performed.
     *  
     * 
     * @return the value of field 'ReadOnly'.
     */
    public boolean getReadOnly(
    ) {
        return this._readOnly;
    }

    /**
     * Returns the value of field 'type'. The field 'type' has the
     * following description: Attribute 'type' is used to specify
     * the JDO-type
     *  of the column.
     *  
     * 
     * @return the value of field 'Type'.
     */
    public java.lang.String getType(
    ) {
        return this._type;
    }

    /**
     * Method hasAcceptNull.
     * 
     * @return true if at least one AcceptNull has been added
     */
    public boolean hasAcceptNull(
    ) {
        return this._has_acceptNull;
    }

    /**
     * Method hasReadOnly.
     * 
     * @return true if at least one ReadOnly has been added
     */
    public boolean hasReadOnly(
    ) {
        return this._has_readOnly;
    }

    /**
     * Returns the value of field 'acceptNull'. The field
     * 'acceptNull' has the following description: Specifies if
     * this field accepts NULL values or
     *  not.
     *  
     * 
     * @return the value of field 'AcceptNull'.
     */
    public boolean isAcceptNull(
    ) {
        return this._acceptNull;
    }

    /**
     * Returns the value of field 'readOnly'. The field 'readOnly'
     * has the following description: Attribute 'read-only' is used
     * to set off
     *  changing the column. If true, no update can be
     *  performed.
     *  
     * 
     * @return the value of field 'ReadOnly'.
     */
    public boolean isReadOnly(
    ) {
        return this._readOnly;
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
     * Notifies all registered PropertyChangeListeners when a bound
     * property's value changes.
     * 
     * @param fieldName the name of the property that has changed.
     * @param newValue the new value of the property.
     * @param oldValue the old value of the property.
     */
    protected void notifyPropertyChangeListeners(
            final java.lang.String fieldName,
            final java.lang.Object oldValue,
            final java.lang.Object newValue) {
        if (propertyChangeSupport == null) return;
        propertyChangeSupport.firePropertyChange(fieldName,oldValue,newValue);
    }

    /**
     * Method removePropertyChangeListener.Removes the given
     * PropertyChangeListener from this classes list of
     * ProperyChangeListeners.
     * 
     * @param pcl The PropertyChangeListener to remove.
     * @return always returns true if pcl != null
     */
    public boolean removePropertyChangeListener(
            final java.beans.PropertyChangeListener pcl) {
        if (propertyChangeSupport == null) return false;
        propertyChangeSupport.removePropertyChangeListener(pcl);
        return true;
    }

    /**
     * Sets the value of field 'acceptNull'. The field 'acceptNull'
     * has the following description: Specifies if this field
     * accepts NULL values or
     *  not.
     *  
     * 
     * @param acceptNull the value of field 'acceptNull'.
     */
    public void setAcceptNull(
            final boolean acceptNull) {
        java.lang.Object oldAcceptNull = (this._acceptNull ? java.lang.Boolean.TRUE : java.lang.Boolean.FALSE);
        this._acceptNull = acceptNull;
        this._has_acceptNull = true;
        notifyPropertyChangeListeners("acceptNull", oldAcceptNull, (this._acceptNull ? java.lang.Boolean.TRUE : java.lang.Boolean.FALSE));
    }

    /**
     * Sets the value of field 'name'. The field 'name' has the
     * following description: Attribute 'name' is used to specify
     * the name of
     *  the column.
     *  
     * 
     * @param name the value of field 'name'.
     */
    public void setName(
            final java.lang.String name) {
        java.lang.Object oldName = this._name;
        this._name = name;
        notifyPropertyChangeListeners("name", oldName, this._name);
    }

    /**
     * Sets the value of field 'readOnly'. The field 'readOnly' has
     * the following description: Attribute 'read-only' is used to
     * set off
     *  changing the column. If true, no update can be
     *  performed.
     *  
     * 
     * @param readOnly the value of field 'readOnly'.
     */
    public void setReadOnly(
            final boolean readOnly) {
        java.lang.Object oldReadOnly = (this._readOnly ? java.lang.Boolean.TRUE : java.lang.Boolean.FALSE);
        this._readOnly = readOnly;
        this._has_readOnly = true;
        notifyPropertyChangeListeners("readOnly", oldReadOnly, (this._readOnly ? java.lang.Boolean.TRUE : java.lang.Boolean.FALSE));
    }

    /**
     * Sets the value of field 'type'. The field 'type' has the
     * following description: Attribute 'type' is used to specify
     * the JDO-type
     *  of the column.
     *  
     * 
     * @param type the value of field 'type'.
     */
    public void setType(
            final java.lang.String type) {
        java.lang.Object oldType = this._type;
        this._type = type;
        notifyPropertyChangeListeners("type", oldType, this._type);
    }

    /**
     * Method unmarshal.
     * 
     * @param reader
     * @throws org.exolab.castor.xml.MarshalException if object is
     * null or if any SAXException is thrown during marshaling
     * @throws org.exolab.castor.xml.ValidationException if this
     * object is an invalid instance according to the schema
     * @return the unmarshaled
     * org.exolab.castor.xml.schema.annotations.jdo.Column
     */
    public static org.exolab.castor.xml.schema.annotations.jdo.Column unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (org.exolab.castor.xml.schema.annotations.jdo.Column) Unmarshaller.unmarshal(org.exolab.castor.xml.schema.annotations.jdo.Column.class, reader);
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
