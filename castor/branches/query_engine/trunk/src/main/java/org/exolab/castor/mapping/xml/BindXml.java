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
 * The 'bind-xml' element is used for specifying XML specific
 * databinding
 *  properties and behavior for a specific field. 'bind-xml' may
 * only appear
 *  as a child of a 'field' element.
 *  
 * 
 * @version $Revision$ $Date$
 */
public class BindXml implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * An optional attribute used for specifying the XML name for
     * the
     *  field associated with the 'bind-xml' element.
     *  
     */
    private java.lang.String _name;

    /**
     * Field _type.
     */
    private java.lang.String _type;

    /**
     * Allows specifying how Castor should automatically determines
     *  the XML name of this field when no name has been specified.
     *  In most cases the XML name is determined by using the field
     * name,
     *  but in some cases the user may want to use the Class name
     * of
     *  the field type. This attribute allows choosing between the
     *  two approaches. If this attribute is not specified, the
     * field
     *  name is used as the XML name.
     *  
     */
    private org.exolab.castor.mapping.xml.types.BindXmlAutoNamingType _autoNaming;

    /**
     * Allows specifying a nested location path for this field,
     *  the value should just be a simplified XPath like value
     *  where names are separated by "/".
     *  
     */
    private java.lang.String _location;

    /**
     * Field _matches.
     */
    private java.lang.String _matches;

    /**
     * Field _reference.
     */
    private boolean _reference;

    /**
     * keeps track of state for field: _reference
     */
    private boolean _has_reference;

    /**
     * Field _node.
     */
    private org.exolab.castor.mapping.xml.types.BindXmlNodeType _node;

    /**
     * Field _QNamePrefix.
     */
    private java.lang.String _QNamePrefix;

    /**
     * Field _transient.
     */
    private boolean _transient;

    /**
     * keeps track of state for field: _transient
     */
    private boolean _has_transient;

    /**
     * Field _classMapping.
     */
    private org.exolab.castor.mapping.xml.ClassMapping _classMapping;

    /**
     * Field _propertyList.
     */
    private java.util.List _propertyList;


      //----------------/
     //- Constructors -/
    //----------------/

    public BindXml() {
        super();
        this._propertyList = new java.util.ArrayList();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * 
     * 
     * @param vProperty
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addProperty(
            final org.exolab.castor.mapping.xml.Property vProperty)
    throws java.lang.IndexOutOfBoundsException {
        this._propertyList.add(vProperty);
    }

    /**
     * 
     * 
     * @param index
     * @param vProperty
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addProperty(
            final int index,
            final org.exolab.castor.mapping.xml.Property vProperty)
    throws java.lang.IndexOutOfBoundsException {
        this._propertyList.add(index, vProperty);
    }

    /**
     */
    public void deleteReference(
    ) {
        this._has_reference= false;
    }

    /**
     */
    public void deleteTransient(
    ) {
        this._has_transient= false;
    }

    /**
     * Method enumerateProperty.
     * 
     * @return an Enumeration over all possible elements of this
     * collection
     */
    public java.util.Enumeration enumerateProperty(
    ) {
        return java.util.Collections.enumeration(this._propertyList);
    }

    /**
     * Returns the value of field 'autoNaming'. The field
     * 'autoNaming' has the following description: Allows
     * specifying how Castor should automatically determines
     *  the XML name of this field when no name has been specified.
     *  In most cases the XML name is determined by using the field
     * name,
     *  but in some cases the user may want to use the Class name
     * of
     *  the field type. This attribute allows choosing between the
     *  two approaches. If this attribute is not specified, the
     * field
     *  name is used as the XML name.
     *  
     * 
     * @return the value of field 'AutoNaming'.
     */
    public org.exolab.castor.mapping.xml.types.BindXmlAutoNamingType getAutoNaming(
    ) {
        return this._autoNaming;
    }

    /**
     * Returns the value of field 'classMapping'.
     * 
     * @return the value of field 'ClassMapping'.
     */
    public org.exolab.castor.mapping.xml.ClassMapping getClassMapping(
    ) {
        return this._classMapping;
    }

    /**
     * Returns the value of field 'location'. The field 'location'
     * has the following description: Allows specifying a nested
     * location path for this field,
     *  the value should just be a simplified XPath like value
     *  where names are separated by "/".
     *  
     * 
     * @return the value of field 'Location'.
     */
    public java.lang.String getLocation(
    ) {
        return this._location;
    }

    /**
     * Returns the value of field 'matches'.
     * 
     * @return the value of field 'Matches'.
     */
    public java.lang.String getMatches(
    ) {
        return this._matches;
    }

    /**
     * Returns the value of field 'name'. The field 'name' has the
     * following description: An optional attribute used for
     * specifying the XML name for the
     *  field associated with the 'bind-xml' element.
     *  
     * 
     * @return the value of field 'Name'.
     */
    public java.lang.String getName(
    ) {
        return this._name;
    }

    /**
     * Returns the value of field 'node'.
     * 
     * @return the value of field 'Node'.
     */
    public org.exolab.castor.mapping.xml.types.BindXmlNodeType getNode(
    ) {
        return this._node;
    }

    /**
     * Method getProperty.
     * 
     * @param index
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     * @return the value of the
     * org.exolab.castor.mapping.xml.Property at the given index
     */
    public org.exolab.castor.mapping.xml.Property getProperty(
            final int index)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._propertyList.size()) {
            throw new IndexOutOfBoundsException("getProperty: Index value '" + index + "' not in range [0.." + (this._propertyList.size() - 1) + "]");
        }
        
        return (org.exolab.castor.mapping.xml.Property) _propertyList.get(index);
    }

    /**
     * Method getProperty.Returns the contents of the collection in
     * an Array.  <p>Note:  Just in case the collection contents
     * are changing in another thread, we pass a 0-length Array of
     * the correct type into the API call.  This way we <i>know</i>
     * that the Array returned is of exactly the correct length.
     * 
     * @return this collection as an Array
     */
    public org.exolab.castor.mapping.xml.Property[] getProperty(
    ) {
        org.exolab.castor.mapping.xml.Property[] array = new org.exolab.castor.mapping.xml.Property[0];
        return (org.exolab.castor.mapping.xml.Property[]) this._propertyList.toArray(array);
    }

    /**
     * Method getPropertyCount.
     * 
     * @return the size of this collection
     */
    public int getPropertyCount(
    ) {
        return this._propertyList.size();
    }

    /**
     * Returns the value of field 'QNamePrefix'.
     * 
     * @return the value of field 'QNamePrefix'.
     */
    public java.lang.String getQNamePrefix(
    ) {
        return this._QNamePrefix;
    }

    /**
     * Returns the value of field 'reference'.
     * 
     * @return the value of field 'Reference'.
     */
    public boolean getReference(
    ) {
        return this._reference;
    }

    /**
     * Returns the value of field 'transient'.
     * 
     * @return the value of field 'Transient'.
     */
    public boolean getTransient(
    ) {
        return this._transient;
    }

    /**
     * Returns the value of field 'type'.
     * 
     * @return the value of field 'Type'.
     */
    public java.lang.String getType(
    ) {
        return this._type;
    }

    /**
     * Method hasReference.
     * 
     * @return true if at least one Reference has been added
     */
    public boolean hasReference(
    ) {
        return this._has_reference;
    }

    /**
     * Method hasTransient.
     * 
     * @return true if at least one Transient has been added
     */
    public boolean hasTransient(
    ) {
        return this._has_transient;
    }

    /**
     * Returns the value of field 'reference'.
     * 
     * @return the value of field 'Reference'.
     */
    public boolean isReference(
    ) {
        return this._reference;
    }

    /**
     * Returns the value of field 'transient'.
     * 
     * @return the value of field 'Transient'.
     */
    public boolean isTransient(
    ) {
        return this._transient;
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
     * Method iterateProperty.
     * 
     * @return an Iterator over all possible elements in this
     * collection
     */
    public java.util.Iterator iterateProperty(
    ) {
        return this._propertyList.iterator();
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
     */
    public void removeAllProperty(
    ) {
        this._propertyList.clear();
    }

    /**
     * Method removeProperty.
     * 
     * @param vProperty
     * @return true if the object was removed from the collection.
     */
    public boolean removeProperty(
            final org.exolab.castor.mapping.xml.Property vProperty) {
        boolean removed = _propertyList.remove(vProperty);
        return removed;
    }

    /**
     * Method removePropertyAt.
     * 
     * @param index
     * @return the element removed from the collection
     */
    public org.exolab.castor.mapping.xml.Property removePropertyAt(
            final int index) {
        java.lang.Object obj = this._propertyList.remove(index);
        return (org.exolab.castor.mapping.xml.Property) obj;
    }

    /**
     * Sets the value of field 'autoNaming'. The field 'autoNaming'
     * has the following description: Allows specifying how Castor
     * should automatically determines
     *  the XML name of this field when no name has been specified.
     *  In most cases the XML name is determined by using the field
     * name,
     *  but in some cases the user may want to use the Class name
     * of
     *  the field type. This attribute allows choosing between the
     *  two approaches. If this attribute is not specified, the
     * field
     *  name is used as the XML name.
     *  
     * 
     * @param autoNaming the value of field 'autoNaming'.
     */
    public void setAutoNaming(
            final org.exolab.castor.mapping.xml.types.BindXmlAutoNamingType autoNaming) {
        this._autoNaming = autoNaming;
    }

    /**
     * Sets the value of field 'classMapping'.
     * 
     * @param classMapping the value of field 'classMapping'.
     */
    public void setClassMapping(
            final org.exolab.castor.mapping.xml.ClassMapping classMapping) {
        this._classMapping = classMapping;
    }

    /**
     * Sets the value of field 'location'. The field 'location' has
     * the following description: Allows specifying a nested
     * location path for this field,
     *  the value should just be a simplified XPath like value
     *  where names are separated by "/".
     *  
     * 
     * @param location the value of field 'location'.
     */
    public void setLocation(
            final java.lang.String location) {
        this._location = location;
    }

    /**
     * Sets the value of field 'matches'.
     * 
     * @param matches the value of field 'matches'.
     */
    public void setMatches(
            final java.lang.String matches) {
        this._matches = matches;
    }

    /**
     * Sets the value of field 'name'. The field 'name' has the
     * following description: An optional attribute used for
     * specifying the XML name for the
     *  field associated with the 'bind-xml' element.
     *  
     * 
     * @param name the value of field 'name'.
     */
    public void setName(
            final java.lang.String name) {
        this._name = name;
    }

    /**
     * Sets the value of field 'node'.
     * 
     * @param node the value of field 'node'.
     */
    public void setNode(
            final org.exolab.castor.mapping.xml.types.BindXmlNodeType node) {
        this._node = node;
    }

    /**
     * 
     * 
     * @param index
     * @param vProperty
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void setProperty(
            final int index,
            final org.exolab.castor.mapping.xml.Property vProperty)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._propertyList.size()) {
            throw new IndexOutOfBoundsException("setProperty: Index value '" + index + "' not in range [0.." + (this._propertyList.size() - 1) + "]");
        }
        
        this._propertyList.set(index, vProperty);
    }

    /**
     * 
     * 
     * @param vPropertyArray
     */
    public void setProperty(
            final org.exolab.castor.mapping.xml.Property[] vPropertyArray) {
        //-- copy array
        _propertyList.clear();
        
        for (int i = 0; i < vPropertyArray.length; i++) {
                this._propertyList.add(vPropertyArray[i]);
        }
    }

    /**
     * Sets the value of field 'QNamePrefix'.
     * 
     * @param QNamePrefix the value of field 'QNamePrefix'.
     */
    public void setQNamePrefix(
            final java.lang.String QNamePrefix) {
        this._QNamePrefix = QNamePrefix;
    }

    /**
     * Sets the value of field 'reference'.
     * 
     * @param reference the value of field 'reference'.
     */
    public void setReference(
            final boolean reference) {
        this._reference = reference;
        this._has_reference = true;
    }

    /**
     * Sets the value of field 'transient'.
     * 
     * @param _transient
     * @param transient the value of field 'transient'.
     */
    public void setTransient(
            final boolean _transient) {
        this._transient = _transient;
        this._has_transient = true;
    }

    /**
     * Sets the value of field 'type'.
     * 
     * @param type the value of field 'type'.
     */
    public void setType(
            final java.lang.String type) {
        this._type = type;
    }

    /**
     * Method unmarshal.
     * 
     * @param reader
     * @throws org.exolab.castor.xml.MarshalException if object is
     * null or if any SAXException is thrown during marshaling
     * @throws org.exolab.castor.xml.ValidationException if this
     * object is an invalid instance according to the schema
     * @return the unmarshaled org.exolab.castor.mapping.xml.BindXml
     */
    public static org.exolab.castor.mapping.xml.BindXml unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (org.exolab.castor.mapping.xml.BindXml) Unmarshaller.unmarshal(org.exolab.castor.mapping.xml.BindXml.class, reader);
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
