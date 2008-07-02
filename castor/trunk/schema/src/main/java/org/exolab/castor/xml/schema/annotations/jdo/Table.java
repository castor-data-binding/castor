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
 * Element 'table' is used to specify the table where the
 *  Object will be saved.
 *  
 * 
 * @version $Revision$ $Date$
 */
public class Table implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Attribute 'name' is used to specify the name of
     *  the table.
     *  
     */
    private java.lang.String _name;

    /**
     * Field _primaryKey.
     */
    private org.exolab.castor.xml.schema.annotations.jdo.PrimaryKey _primaryKey;

    /**
     * Field propertyChangeSupport.
     */
    private java.beans.PropertyChangeSupport propertyChangeSupport;


      //----------------/
     //- Constructors -/
    //----------------/

    public Table() {
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
     * Returns the value of field 'name'. The field 'name' has the
     * following description: Attribute 'name' is used to specify
     * the name of
     *  the table.
     *  
     * 
     * @return the value of field 'Name'.
     */
    public java.lang.String getName(
    ) {
        return this._name;
    }

    /**
     * Returns the value of field 'primaryKey'.
     * 
     * @return the value of field 'PrimaryKey'.
     */
    public org.exolab.castor.xml.schema.annotations.jdo.PrimaryKey getPrimaryKey(
    ) {
        return this._primaryKey;
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
     * Sets the value of field 'name'. The field 'name' has the
     * following description: Attribute 'name' is used to specify
     * the name of
     *  the table.
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
     * Sets the value of field 'primaryKey'.
     * 
     * @param primaryKey the value of field 'primaryKey'.
     */
    public void setPrimaryKey(
            final org.exolab.castor.xml.schema.annotations.jdo.PrimaryKey primaryKey) {
        java.lang.Object oldPrimaryKey = this._primaryKey;
        this._primaryKey = primaryKey;
        notifyPropertyChangeListeners("primaryKey", oldPrimaryKey, this._primaryKey);
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
     * org.exolab.castor.xml.schema.annotations.jdo.Table
     */
    public static org.exolab.castor.xml.schema.annotations.jdo.Table unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (org.exolab.castor.xml.schema.annotations.jdo.Table) Unmarshaller.unmarshal(org.exolab.castor.xml.schema.annotations.jdo.Table.class, reader);
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
