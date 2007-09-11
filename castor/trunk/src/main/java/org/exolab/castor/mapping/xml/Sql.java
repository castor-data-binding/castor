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
 * Class Sql.
 * 
 * @version $Revision$ $Date$
 */
public class Sql implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _name.
     */
    private java.util.List _name;

    /**
     * Field _type.
     */
    private java.lang.String _type;

    /**
     * Field _manyTable.
     */
    private java.lang.String _manyTable;

    /**
     * Field _manyKey.
     */
    private java.util.List _manyKey;

    /**
     * Field _readOnly.
     */
    private boolean _readOnly = false;

    /**
     * keeps track of state for field: _readOnly
     */
    private boolean _has_readOnly;

    /**
     * Field _transient.
     */
    private boolean _transient;

    /**
     * keeps track of state for field: _transient
     */
    private boolean _has_transient;

    /**
     * Field _dirty.
     */
    private org.exolab.castor.mapping.xml.types.SqlDirtyType _dirty = org.exolab.castor.mapping.xml.types.SqlDirtyType.valueOf("check");


      //----------------/
     //- Constructors -/
    //----------------/

    public Sql() {
        super();
        this._name = new java.util.ArrayList();
        this._manyKey = new java.util.ArrayList();
        setDirty(org.exolab.castor.mapping.xml.types.SqlDirtyType.valueOf("check"));
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * 
     * 
     * @param vManyKey
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addManyKey(
            final java.lang.String vManyKey)
    throws java.lang.IndexOutOfBoundsException {
        this._manyKey.add(vManyKey);
    }

    /**
     * 
     * 
     * @param index
     * @param vManyKey
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addManyKey(
            final int index,
            final java.lang.String vManyKey)
    throws java.lang.IndexOutOfBoundsException {
        this._manyKey.add(index, vManyKey);
    }

    /**
     * 
     * 
     * @param vName
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addName(
            final java.lang.String vName)
    throws java.lang.IndexOutOfBoundsException {
        this._name.add(vName);
    }

    /**
     * 
     * 
     * @param index
     * @param vName
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addName(
            final int index,
            final java.lang.String vName)
    throws java.lang.IndexOutOfBoundsException {
        this._name.add(index, vName);
    }

    /**
     */
    public void deleteReadOnly(
    ) {
        this._has_readOnly= false;
    }

    /**
     */
    public void deleteTransient(
    ) {
        this._has_transient= false;
    }

    /**
     * Method enumerateManyKey.
     * 
     * @return an Enumeration over all possible elements of this
     * collection
     */
    public java.util.Enumeration enumerateManyKey(
    ) {
        return java.util.Collections.enumeration(this._manyKey);
    }

    /**
     * Method enumerateName.
     * 
     * @return an Enumeration over all possible elements of this
     * collection
     */
    public java.util.Enumeration enumerateName(
    ) {
        return java.util.Collections.enumeration(this._name);
    }

    /**
     * Returns the value of field 'dirty'.
     * 
     * @return the value of field 'Dirty'.
     */
    public org.exolab.castor.mapping.xml.types.SqlDirtyType getDirty(
    ) {
        return this._dirty;
    }

    /**
     * Method getManyKey.
     * 
     * @param index
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     * @return the value of the java.lang.String at the given index
     */
    public java.lang.String getManyKey(
            final int index)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._manyKey.size()) {
            throw new IndexOutOfBoundsException("getManyKey: Index value '" + index + "' not in range [0.." + (this._manyKey.size() - 1) + "]");
        }
        
        return (java.lang.String) _manyKey.get(index);
    }

    /**
     * Method getManyKey.Returns the contents of the collection in
     * an Array.  <p>Note:  Just in case the collection contents
     * are changing in another thread, we pass a 0-length Array of
     * the correct type into the API call.  This way we <i>know</i>
     * that the Array returned is of exactly the correct length.
     * 
     * @return this collection as an Array
     */
    public java.lang.String[] getManyKey(
    ) {
        java.lang.String[] array = new java.lang.String[0];
        return (java.lang.String[]) this._manyKey.toArray(array);
    }

    /**
     * Method getManyKeyCount.
     * 
     * @return the size of this collection
     */
    public int getManyKeyCount(
    ) {
        return this._manyKey.size();
    }

    /**
     * Returns the value of field 'manyTable'.
     * 
     * @return the value of field 'ManyTable'.
     */
    public java.lang.String getManyTable(
    ) {
        return this._manyTable;
    }

    /**
     * Method getName.
     * 
     * @param index
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     * @return the value of the java.lang.String at the given index
     */
    public java.lang.String getName(
            final int index)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._name.size()) {
            throw new IndexOutOfBoundsException("getName: Index value '" + index + "' not in range [0.." + (this._name.size() - 1) + "]");
        }
        
        return (java.lang.String) _name.get(index);
    }

    /**
     * Method getName.Returns the contents of the collection in an
     * Array.  <p>Note:  Just in case the collection contents are
     * changing in another thread, we pass a 0-length Array of the
     * correct type into the API call.  This way we <i>know</i>
     * that the Array returned is of exactly the correct length.
     * 
     * @return this collection as an Array
     */
    public java.lang.String[] getName(
    ) {
        java.lang.String[] array = new java.lang.String[0];
        return (java.lang.String[]) this._name.toArray(array);
    }

    /**
     * Method getNameCount.
     * 
     * @return the size of this collection
     */
    public int getNameCount(
    ) {
        return this._name.size();
    }

    /**
     * Returns the value of field 'readOnly'.
     * 
     * @return the value of field 'ReadOnly'.
     */
    public boolean getReadOnly(
    ) {
        return this._readOnly;
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
     * Method hasReadOnly.
     * 
     * @return true if at least one ReadOnly has been added
     */
    public boolean hasReadOnly(
    ) {
        return this._has_readOnly;
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
     * Returns the value of field 'readOnly'.
     * 
     * @return the value of field 'ReadOnly'.
     */
    public boolean isReadOnly(
    ) {
        return this._readOnly;
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
     * Method iterateManyKey.
     * 
     * @return an Iterator over all possible elements in this
     * collection
     */
    public java.util.Iterator iterateManyKey(
    ) {
        return this._manyKey.iterator();
    }

    /**
     * Method iterateName.
     * 
     * @return an Iterator over all possible elements in this
     * collection
     */
    public java.util.Iterator iterateName(
    ) {
        return this._name.iterator();
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
    public void removeAllManyKey(
    ) {
        this._manyKey.clear();
    }

    /**
     */
    public void removeAllName(
    ) {
        this._name.clear();
    }

    /**
     * Method removeManyKey.
     * 
     * @param vManyKey
     * @return true if the object was removed from the collection.
     */
    public boolean removeManyKey(
            final java.lang.String vManyKey) {
        boolean removed = _manyKey.remove(vManyKey);
        return removed;
    }

    /**
     * Method removeManyKeyAt.
     * 
     * @param index
     * @return the element removed from the collection
     */
    public java.lang.String removeManyKeyAt(
            final int index) {
        java.lang.Object obj = this._manyKey.remove(index);
        return (java.lang.String) obj;
    }

    /**
     * Method removeName.
     * 
     * @param vName
     * @return true if the object was removed from the collection.
     */
    public boolean removeName(
            final java.lang.String vName) {
        boolean removed = _name.remove(vName);
        return removed;
    }

    /**
     * Method removeNameAt.
     * 
     * @param index
     * @return the element removed from the collection
     */
    public java.lang.String removeNameAt(
            final int index) {
        java.lang.Object obj = this._name.remove(index);
        return (java.lang.String) obj;
    }

    /**
     * Sets the value of field 'dirty'.
     * 
     * @param dirty the value of field 'dirty'.
     */
    public void setDirty(
            final org.exolab.castor.mapping.xml.types.SqlDirtyType dirty) {
        this._dirty = dirty;
    }

    /**
     * 
     * 
     * @param index
     * @param vManyKey
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void setManyKey(
            final int index,
            final java.lang.String vManyKey)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._manyKey.size()) {
            throw new IndexOutOfBoundsException("setManyKey: Index value '" + index + "' not in range [0.." + (this._manyKey.size() - 1) + "]");
        }
        
        this._manyKey.set(index, vManyKey);
    }

    /**
     * 
     * 
     * @param vManyKeyArray
     */
    public void setManyKey(
            final java.lang.String[] vManyKeyArray) {
        //-- copy array
        _manyKey.clear();
        
        for (int i = 0; i < vManyKeyArray.length; i++) {
                this._manyKey.add(vManyKeyArray[i]);
        }
    }

    /**
     * Sets the value of field 'manyTable'.
     * 
     * @param manyTable the value of field 'manyTable'.
     */
    public void setManyTable(
            final java.lang.String manyTable) {
        this._manyTable = manyTable;
    }

    /**
     * 
     * 
     * @param index
     * @param vName
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void setName(
            final int index,
            final java.lang.String vName)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._name.size()) {
            throw new IndexOutOfBoundsException("setName: Index value '" + index + "' not in range [0.." + (this._name.size() - 1) + "]");
        }
        
        this._name.set(index, vName);
    }

    /**
     * 
     * 
     * @param vNameArray
     */
    public void setName(
            final java.lang.String[] vNameArray) {
        //-- copy array
        _name.clear();
        
        for (int i = 0; i < vNameArray.length; i++) {
                this._name.add(vNameArray[i]);
        }
    }

    /**
     * Sets the value of field 'readOnly'.
     * 
     * @param readOnly the value of field 'readOnly'.
     */
    public void setReadOnly(
            final boolean readOnly) {
        this._readOnly = readOnly;
        this._has_readOnly = true;
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
     * @return the unmarshaled org.exolab.castor.mapping.xml.Sql
     */
    public static org.exolab.castor.mapping.xml.Sql unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (org.exolab.castor.mapping.xml.Sql) Unmarshaller.unmarshal(org.exolab.castor.mapping.xml.Sql.class, reader);
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
