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
 * Class ClassMapping.
 * 
 * @version $Revision$ $Date$
 */
public class ClassMapping implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _name.
     */
    private java.lang.String _name;

    /**
     * Field _extends.
     */
    private java.lang.Object _extends;

    /**
     * Field _depends.
     */
    private java.lang.Object _depends;

    /**
     * Field _identity.
     */
    private java.util.List _identity;

    /**
     * Field _access.
     */
    private org.exolab.castor.mapping.xml.types.ClassMappingAccessType _access = org.exolab.castor.mapping.xml.types.ClassMappingAccessType.valueOf("shared");

    /**
     * Field _keyGenerator.
     */
    private java.lang.String _keyGenerator;

    /**
     * Field _autoComplete.
     */
    private boolean _autoComplete = false;

    /**
     * keeps track of state for field: _autoComplete
     */
    private boolean _has_autoComplete;

    /**
     * Field _verifyConstructable.
     */
    private boolean _verifyConstructable = true;

    /**
     * keeps track of state for field: _verifyConstructable
     */
    private boolean _has_verifyConstructable;

    /**
     * Field _description.
     */
    private java.lang.String _description;

    /**
     * Field _cacheTypeMapping.
     */
    private org.exolab.castor.mapping.xml.CacheTypeMapping _cacheTypeMapping;

    /**
     * Field _mapTo.
     */
    private org.exolab.castor.mapping.xml.MapTo _mapTo;

    /**
     * Field _namedQueryList.
     */
    private java.util.List _namedQueryList;

    /**
     * Field _classChoice.
     */
    private org.exolab.castor.mapping.xml.ClassChoice _classChoice;


      //----------------/
     //- Constructors -/
    //----------------/

    public ClassMapping() {
        super();
        this._identity = new java.util.ArrayList();
        setAccess(org.exolab.castor.mapping.xml.types.ClassMappingAccessType.valueOf("shared"));
        this._namedQueryList = new java.util.ArrayList();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * 
     * 
     * @param vIdentity
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addIdentity(
            final java.lang.String vIdentity)
    throws java.lang.IndexOutOfBoundsException {
        this._identity.add(vIdentity);
    }

    /**
     * 
     * 
     * @param index
     * @param vIdentity
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addIdentity(
            final int index,
            final java.lang.String vIdentity)
    throws java.lang.IndexOutOfBoundsException {
        this._identity.add(index, vIdentity);
    }

    /**
     * 
     * 
     * @param vNamedQuery
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addNamedQuery(
            final org.exolab.castor.mapping.xml.NamedQuery vNamedQuery)
    throws java.lang.IndexOutOfBoundsException {
        this._namedQueryList.add(vNamedQuery);
    }

    /**
     * 
     * 
     * @param index
     * @param vNamedQuery
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addNamedQuery(
            final int index,
            final org.exolab.castor.mapping.xml.NamedQuery vNamedQuery)
    throws java.lang.IndexOutOfBoundsException {
        this._namedQueryList.add(index, vNamedQuery);
    }

    /**
     */
    public void deleteAutoComplete(
    ) {
        this._has_autoComplete= false;
    }

    /**
     */
    public void deleteVerifyConstructable(
    ) {
        this._has_verifyConstructable= false;
    }

    /**
     * Method enumerateIdentity.
     * 
     * @return an Enumeration over all possible elements of this
     * collection
     */
    public java.util.Enumeration enumerateIdentity(
    ) {
        return java.util.Collections.enumeration(this._identity);
    }

    /**
     * Method enumerateNamedQuery.
     * 
     * @return an Enumeration over all possible elements of this
     * collection
     */
    public java.util.Enumeration enumerateNamedQuery(
    ) {
        return java.util.Collections.enumeration(this._namedQueryList);
    }

    /**
     * Returns the value of field 'access'.
     * 
     * @return the value of field 'Access'.
     */
    public org.exolab.castor.mapping.xml.types.ClassMappingAccessType getAccess(
    ) {
        return this._access;
    }

    /**
     * Returns the value of field 'autoComplete'.
     * 
     * @return the value of field 'AutoComplete'.
     */
    public boolean getAutoComplete(
    ) {
        return this._autoComplete;
    }

    /**
     * Returns the value of field 'cacheTypeMapping'.
     * 
     * @return the value of field 'CacheTypeMapping'.
     */
    public org.exolab.castor.mapping.xml.CacheTypeMapping getCacheTypeMapping(
    ) {
        return this._cacheTypeMapping;
    }

    /**
     * Returns the value of field 'classChoice'.
     * 
     * @return the value of field 'ClassChoice'.
     */
    public org.exolab.castor.mapping.xml.ClassChoice getClassChoice(
    ) {
        return this._classChoice;
    }

    /**
     * Returns the value of field 'depends'.
     * 
     * @return the value of field 'Depends'.
     */
    public java.lang.Object getDepends(
    ) {
        return this._depends;
    }

    /**
     * Returns the value of field 'description'.
     * 
     * @return the value of field 'Description'.
     */
    public java.lang.String getDescription(
    ) {
        return this._description;
    }

    /**
     * Returns the value of field 'extends'.
     * 
     * @return the value of field 'Extends'.
     */
    public java.lang.Object getExtends(
    ) {
        return this._extends;
    }

    /**
     * Method getIdentity.
     * 
     * @param index
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     * @return the value of the java.lang.String at the given index
     */
    public java.lang.String getIdentity(
            final int index)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._identity.size()) {
            throw new IndexOutOfBoundsException("getIdentity: Index value '" + index + "' not in range [0.." + (this._identity.size() - 1) + "]");
        }
        
        return (java.lang.String) _identity.get(index);
    }

    /**
     * Method getIdentity.Returns the contents of the collection in
     * an Array.  <p>Note:  Just in case the collection contents
     * are changing in another thread, we pass a 0-length Array of
     * the correct type into the API call.  This way we <i>know</i>
     * that the Array returned is of exactly the correct length.
     * 
     * @return this collection as an Array
     */
    public java.lang.String[] getIdentity(
    ) {
        java.lang.String[] array = new java.lang.String[0];
        return (java.lang.String[]) this._identity.toArray(array);
    }

    /**
     * Method getIdentityCount.
     * 
     * @return the size of this collection
     */
    public int getIdentityCount(
    ) {
        return this._identity.size();
    }

    /**
     * Returns the value of field 'keyGenerator'.
     * 
     * @return the value of field 'KeyGenerator'.
     */
    public java.lang.String getKeyGenerator(
    ) {
        return this._keyGenerator;
    }

    /**
     * Returns the value of field 'mapTo'.
     * 
     * @return the value of field 'MapTo'.
     */
    public org.exolab.castor.mapping.xml.MapTo getMapTo(
    ) {
        return this._mapTo;
    }

    /**
     * Returns the value of field 'name'.
     * 
     * @return the value of field 'Name'.
     */
    public java.lang.String getName(
    ) {
        return this._name;
    }

    /**
     * Method getNamedQuery.
     * 
     * @param index
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     * @return the value of the
     * org.exolab.castor.mapping.xml.NamedQuery at the given index
     */
    public org.exolab.castor.mapping.xml.NamedQuery getNamedQuery(
            final int index)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._namedQueryList.size()) {
            throw new IndexOutOfBoundsException("getNamedQuery: Index value '" + index + "' not in range [0.." + (this._namedQueryList.size() - 1) + "]");
        }
        
        return (org.exolab.castor.mapping.xml.NamedQuery) _namedQueryList.get(index);
    }

    /**
     * Method getNamedQuery.Returns the contents of the collection
     * in an Array.  <p>Note:  Just in case the collection contents
     * are changing in another thread, we pass a 0-length Array of
     * the correct type into the API call.  This way we <i>know</i>
     * that the Array returned is of exactly the correct length.
     * 
     * @return this collection as an Array
     */
    public org.exolab.castor.mapping.xml.NamedQuery[] getNamedQuery(
    ) {
        org.exolab.castor.mapping.xml.NamedQuery[] array = new org.exolab.castor.mapping.xml.NamedQuery[0];
        return (org.exolab.castor.mapping.xml.NamedQuery[]) this._namedQueryList.toArray(array);
    }

    /**
     * Method getNamedQueryCount.
     * 
     * @return the size of this collection
     */
    public int getNamedQueryCount(
    ) {
        return this._namedQueryList.size();
    }

    /**
     * Returns the value of field 'verifyConstructable'.
     * 
     * @return the value of field 'VerifyConstructable'.
     */
    public boolean getVerifyConstructable(
    ) {
        return this._verifyConstructable;
    }

    /**
     * Method hasAutoComplete.
     * 
     * @return true if at least one AutoComplete has been added
     */
    public boolean hasAutoComplete(
    ) {
        return this._has_autoComplete;
    }

    /**
     * Method hasVerifyConstructable.
     * 
     * @return true if at least one VerifyConstructable has been
     * added
     */
    public boolean hasVerifyConstructable(
    ) {
        return this._has_verifyConstructable;
    }

    /**
     * Returns the value of field 'autoComplete'.
     * 
     * @return the value of field 'AutoComplete'.
     */
    public boolean isAutoComplete(
    ) {
        return this._autoComplete;
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
     * Returns the value of field 'verifyConstructable'.
     * 
     * @return the value of field 'VerifyConstructable'.
     */
    public boolean isVerifyConstructable(
    ) {
        return this._verifyConstructable;
    }

    /**
     * Method iterateIdentity.
     * 
     * @return an Iterator over all possible elements in this
     * collection
     */
    public java.util.Iterator iterateIdentity(
    ) {
        return this._identity.iterator();
    }

    /**
     * Method iterateNamedQuery.
     * 
     * @return an Iterator over all possible elements in this
     * collection
     */
    public java.util.Iterator iterateNamedQuery(
    ) {
        return this._namedQueryList.iterator();
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
    public void removeAllIdentity(
    ) {
        this._identity.clear();
    }

    /**
     */
    public void removeAllNamedQuery(
    ) {
        this._namedQueryList.clear();
    }

    /**
     * Method removeIdentity.
     * 
     * @param vIdentity
     * @return true if the object was removed from the collection.
     */
    public boolean removeIdentity(
            final java.lang.String vIdentity) {
        boolean removed = _identity.remove(vIdentity);
        return removed;
    }

    /**
     * Method removeIdentityAt.
     * 
     * @param index
     * @return the element removed from the collection
     */
    public java.lang.String removeIdentityAt(
            final int index) {
        java.lang.Object obj = this._identity.remove(index);
        return (java.lang.String) obj;
    }

    /**
     * Method removeNamedQuery.
     * 
     * @param vNamedQuery
     * @return true if the object was removed from the collection.
     */
    public boolean removeNamedQuery(
            final org.exolab.castor.mapping.xml.NamedQuery vNamedQuery) {
        boolean removed = _namedQueryList.remove(vNamedQuery);
        return removed;
    }

    /**
     * Method removeNamedQueryAt.
     * 
     * @param index
     * @return the element removed from the collection
     */
    public org.exolab.castor.mapping.xml.NamedQuery removeNamedQueryAt(
            final int index) {
        java.lang.Object obj = this._namedQueryList.remove(index);
        return (org.exolab.castor.mapping.xml.NamedQuery) obj;
    }

    /**
     * Sets the value of field 'access'.
     * 
     * @param access the value of field 'access'.
     */
    public void setAccess(
            final org.exolab.castor.mapping.xml.types.ClassMappingAccessType access) {
        this._access = access;
    }

    /**
     * Sets the value of field 'autoComplete'.
     * 
     * @param autoComplete the value of field 'autoComplete'.
     */
    public void setAutoComplete(
            final boolean autoComplete) {
        this._autoComplete = autoComplete;
        this._has_autoComplete = true;
    }

    /**
     * Sets the value of field 'cacheTypeMapping'.
     * 
     * @param cacheTypeMapping the value of field 'cacheTypeMapping'
     */
    public void setCacheTypeMapping(
            final org.exolab.castor.mapping.xml.CacheTypeMapping cacheTypeMapping) {
        this._cacheTypeMapping = cacheTypeMapping;
    }

    /**
     * Sets the value of field 'classChoice'.
     * 
     * @param classChoice the value of field 'classChoice'.
     */
    public void setClassChoice(
            final org.exolab.castor.mapping.xml.ClassChoice classChoice) {
        this._classChoice = classChoice;
    }

    /**
     * Sets the value of field 'depends'.
     * 
     * @param depends the value of field 'depends'.
     */
    public void setDepends(
            final java.lang.Object depends) {
        this._depends = depends;
    }

    /**
     * Sets the value of field 'description'.
     * 
     * @param description the value of field 'description'.
     */
    public void setDescription(
            final java.lang.String description) {
        this._description = description;
    }

    /**
     * Sets the value of field 'extends'.
     * 
     * @param _extends
     * @param extends the value of field 'extends'.
     */
    public void setExtends(
            final java.lang.Object _extends) {
        this._extends = _extends;
    }

    /**
     * 
     * 
     * @param index
     * @param vIdentity
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void setIdentity(
            final int index,
            final java.lang.String vIdentity)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._identity.size()) {
            throw new IndexOutOfBoundsException("setIdentity: Index value '" + index + "' not in range [0.." + (this._identity.size() - 1) + "]");
        }
        
        this._identity.set(index, vIdentity);
    }

    /**
     * 
     * 
     * @param vIdentityArray
     */
    public void setIdentity(
            final java.lang.String[] vIdentityArray) {
        //-- copy array
        _identity.clear();
        
        for (int i = 0; i < vIdentityArray.length; i++) {
                this._identity.add(vIdentityArray[i]);
        }
    }

    /**
     * Sets the value of field 'keyGenerator'.
     * 
     * @param keyGenerator the value of field 'keyGenerator'.
     */
    public void setKeyGenerator(
            final java.lang.String keyGenerator) {
        this._keyGenerator = keyGenerator;
    }

    /**
     * Sets the value of field 'mapTo'.
     * 
     * @param mapTo the value of field 'mapTo'.
     */
    public void setMapTo(
            final org.exolab.castor.mapping.xml.MapTo mapTo) {
        this._mapTo = mapTo;
    }

    /**
     * Sets the value of field 'name'.
     * 
     * @param name the value of field 'name'.
     */
    public void setName(
            final java.lang.String name) {
        this._name = name;
    }

    /**
     * 
     * 
     * @param index
     * @param vNamedQuery
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void setNamedQuery(
            final int index,
            final org.exolab.castor.mapping.xml.NamedQuery vNamedQuery)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._namedQueryList.size()) {
            throw new IndexOutOfBoundsException("setNamedQuery: Index value '" + index + "' not in range [0.." + (this._namedQueryList.size() - 1) + "]");
        }
        
        this._namedQueryList.set(index, vNamedQuery);
    }

    /**
     * 
     * 
     * @param vNamedQueryArray
     */
    public void setNamedQuery(
            final org.exolab.castor.mapping.xml.NamedQuery[] vNamedQueryArray) {
        //-- copy array
        _namedQueryList.clear();
        
        for (int i = 0; i < vNamedQueryArray.length; i++) {
                this._namedQueryList.add(vNamedQueryArray[i]);
        }
    }

    /**
     * Sets the value of field 'verifyConstructable'.
     * 
     * @param verifyConstructable the value of field
     * 'verifyConstructable'.
     */
    public void setVerifyConstructable(
            final boolean verifyConstructable) {
        this._verifyConstructable = verifyConstructable;
        this._has_verifyConstructable = true;
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
     * org.exolab.castor.mapping.xml.ClassMapping
     */
    public static org.exolab.castor.mapping.xml.ClassMapping unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (org.exolab.castor.mapping.xml.ClassMapping) Unmarshaller.unmarshal(org.exolab.castor.mapping.xml.ClassMapping.class, reader);
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
