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
import java.util.ArrayList;
import java.util.Enumeration;
import org.exolab.castor.mapping.xml.types.ClassMappingAccessType;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.ContentHandler;

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
     * Field _name
     */
    private java.lang.String _name;

    /**
     * Field _extends
     */
    private java.lang.Object _extends;

    /**
     * Field _depends
     */
    private java.lang.Object _depends;

    /**
     * Field _identity
     */
    private java.util.ArrayList _identity;

    /**
     * Field _access
     */
    private org.exolab.castor.mapping.xml.types.ClassMappingAccessType _access = org.exolab.castor.mapping.xml.types.ClassMappingAccessType.valueOf("shared");

    /**
     * Field _keyGenerator
     */
    private java.lang.String _keyGenerator;

    /**
     * Field _autoComplete
     */
    private boolean _autoComplete = false;

    /**
     * keeps track of state for field: _autoComplete
     */
    private boolean _has_autoComplete;

    /**
     * Field _verifyConstructable
     */
    private boolean _verifyConstructable = true;

    /**
     * keeps track of state for field: _verifyConstructable
     */
    private boolean _has_verifyConstructable;

    /**
     * Field _description
     */
    private java.lang.String _description;

    /**
     * Field _cacheTypeMapping
     */
    private org.exolab.castor.mapping.xml.CacheTypeMapping _cacheTypeMapping;

    /**
     * Field _mapTo
     */
    private org.exolab.castor.mapping.xml.MapTo _mapTo;

    /**
     * Field _classChoice
     */
    private org.exolab.castor.mapping.xml.ClassChoice _classChoice;


      //----------------/
     //- Constructors -/
    //----------------/

    public ClassMapping() 
     {
        super();
        _identity = new ArrayList();
        setAccess(org.exolab.castor.mapping.xml.types.ClassMappingAccessType.valueOf("shared"));
    } //-- org.exolab.castor.mapping.xml.ClassMapping()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addIdentity
     * 
     * 
     * 
     * @param vIdentity
     */
    public void addIdentity(java.lang.String vIdentity)
        throws java.lang.IndexOutOfBoundsException
    {
        _identity.add(vIdentity);
    } //-- void addIdentity(java.lang.String) 

    /**
     * Method addIdentity
     * 
     * 
     * 
     * @param index
     * @param vIdentity
     */
    public void addIdentity(int index, java.lang.String vIdentity)
        throws java.lang.IndexOutOfBoundsException
    {
        _identity.add(index, vIdentity);
    } //-- void addIdentity(int, java.lang.String) 

    /**
     * Method clearIdentity
     * 
     */
    public void clearIdentity()
    {
        _identity.clear();
    } //-- void clearIdentity() 

    /**
     * Method deleteAutoComplete
     * 
     */
    public void deleteAutoComplete()
    {
        this._has_autoComplete= false;
    } //-- void deleteAutoComplete() 

    /**
     * Method deleteVerifyConstructable
     * 
     */
    public void deleteVerifyConstructable()
    {
        this._has_verifyConstructable= false;
    } //-- void deleteVerifyConstructable() 

    /**
     * Method enumerateIdentity
     * 
     * 
     * 
     * @return Enumeration
     */
    public java.util.Enumeration enumerateIdentity()
    {
        return new org.exolab.castor.util.IteratorEnumeration(_identity.iterator());
    } //-- java.util.Enumeration enumerateIdentity() 

    /**
     * Returns the value of field 'access'.
     * 
     * @return ClassMappingAccessType
     * @return the value of field 'access'.
     */
    public org.exolab.castor.mapping.xml.types.ClassMappingAccessType getAccess()
    {
        return this._access;
    } //-- org.exolab.castor.mapping.xml.types.ClassMappingAccessType getAccess() 

    /**
     * Returns the value of field 'autoComplete'.
     * 
     * @return boolean
     * @return the value of field 'autoComplete'.
     */
    public boolean getAutoComplete()
    {
        return this._autoComplete;
    } //-- boolean getAutoComplete() 

    /**
     * Returns the value of field 'cacheTypeMapping'.
     * 
     * @return CacheTypeMapping
     * @return the value of field 'cacheTypeMapping'.
     */
    public org.exolab.castor.mapping.xml.CacheTypeMapping getCacheTypeMapping()
    {
        return this._cacheTypeMapping;
    } //-- org.exolab.castor.mapping.xml.CacheTypeMapping getCacheTypeMapping() 

    /**
     * Returns the value of field 'classChoice'.
     * 
     * @return ClassChoice
     * @return the value of field 'classChoice'.
     */
    public org.exolab.castor.mapping.xml.ClassChoice getClassChoice()
    {
        return this._classChoice;
    } //-- org.exolab.castor.mapping.xml.ClassChoice getClassChoice() 

    /**
     * Returns the value of field 'depends'.
     * 
     * @return Object
     * @return the value of field 'depends'.
     */
    public java.lang.Object getDepends()
    {
        return this._depends;
    } //-- java.lang.Object getDepends() 

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
     * Returns the value of field 'extends'.
     * 
     * @return Object
     * @return the value of field 'extends'.
     */
    public java.lang.Object getExtends()
    {
        return this._extends;
    } //-- java.lang.Object getExtends() 

    /**
     * Method getIdentity
     * 
     * 
     * 
     * @param index
     * @return String
     */
    public java.lang.String getIdentity(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _identity.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (String)_identity.get(index);
    } //-- java.lang.String getIdentity(int) 

    /**
     * Method getIdentity
     * 
     * 
     * 
     * @return String
     */
    public java.lang.String[] getIdentity()
    {
        int size = _identity.size();
        java.lang.String[] mArray = new java.lang.String[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (String)_identity.get(index);
        }
        return mArray;
    } //-- java.lang.String[] getIdentity() 

    /**
     * Method getIdentityCount
     * 
     * 
     * 
     * @return int
     */
    public int getIdentityCount()
    {
        return _identity.size();
    } //-- int getIdentityCount() 

    /**
     * Returns the value of field 'keyGenerator'.
     * 
     * @return String
     * @return the value of field 'keyGenerator'.
     */
    public java.lang.String getKeyGenerator()
    {
        return this._keyGenerator;
    } //-- java.lang.String getKeyGenerator() 

    /**
     * Returns the value of field 'mapTo'.
     * 
     * @return MapTo
     * @return the value of field 'mapTo'.
     */
    public org.exolab.castor.mapping.xml.MapTo getMapTo()
    {
        return this._mapTo;
    } //-- org.exolab.castor.mapping.xml.MapTo getMapTo() 

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
     * Returns the value of field 'verifyConstructable'.
     * 
     * @return boolean
     * @return the value of field 'verifyConstructable'.
     */
    public boolean getVerifyConstructable()
    {
        return this._verifyConstructable;
    } //-- boolean getVerifyConstructable() 

    /**
     * Method hasAutoComplete
     * 
     * 
     * 
     * @return boolean
     */
    public boolean hasAutoComplete()
    {
        return this._has_autoComplete;
    } //-- boolean hasAutoComplete() 

    /**
     * Method hasVerifyConstructable
     * 
     * 
     * 
     * @return boolean
     */
    public boolean hasVerifyConstructable()
    {
        return this._has_verifyConstructable;
    } //-- boolean hasVerifyConstructable() 

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
     * Method removeIdentity
     * 
     * 
     * 
     * @param vIdentity
     * @return boolean
     */
    public boolean removeIdentity(java.lang.String vIdentity)
    {
        boolean removed = _identity.remove(vIdentity);
        return removed;
    } //-- boolean removeIdentity(java.lang.String) 

    /**
     * Sets the value of field 'access'.
     * 
     * @param access the value of field 'access'.
     */
    public void setAccess(org.exolab.castor.mapping.xml.types.ClassMappingAccessType access)
    {
        this._access = access;
    } //-- void setAccess(org.exolab.castor.mapping.xml.types.ClassMappingAccessType) 

    /**
     * Sets the value of field 'autoComplete'.
     * 
     * @param autoComplete the value of field 'autoComplete'.
     */
    public void setAutoComplete(boolean autoComplete)
    {
        this._autoComplete = autoComplete;
        this._has_autoComplete = true;
    } //-- void setAutoComplete(boolean) 

    /**
     * Sets the value of field 'cacheTypeMapping'.
     * 
     * @param cacheTypeMapping the value of field 'cacheTypeMapping'
     */
    public void setCacheTypeMapping(org.exolab.castor.mapping.xml.CacheTypeMapping cacheTypeMapping)
    {
        this._cacheTypeMapping = cacheTypeMapping;
    } //-- void setCacheTypeMapping(org.exolab.castor.mapping.xml.CacheTypeMapping) 

    /**
     * Sets the value of field 'classChoice'.
     * 
     * @param classChoice the value of field 'classChoice'.
     */
    public void setClassChoice(org.exolab.castor.mapping.xml.ClassChoice classChoice)
    {
        this._classChoice = classChoice;
    } //-- void setClassChoice(org.exolab.castor.mapping.xml.ClassChoice) 

    /**
     * Sets the value of field 'depends'.
     * 
     * @param depends the value of field 'depends'.
     */
    public void setDepends(java.lang.Object depends)
    {
        this._depends = depends;
    } //-- void setDepends(java.lang.Object) 

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
     * Sets the value of field 'extends'.
     * 
     * @param _extends
     * @param extends the value of field 'extends'.
     */
    public void setExtends(java.lang.Object _extends)
    {
        this._extends = _extends;
    } //-- void setExtends(java.lang.Object) 

    /**
     * Method setIdentity
     * 
     * 
     * 
     * @param index
     * @param vIdentity
     */
    public void setIdentity(int index, java.lang.String vIdentity)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _identity.size())) {
            throw new IndexOutOfBoundsException();
        }
        _identity.set(index, vIdentity);
    } //-- void setIdentity(int, java.lang.String) 

    /**
     * Method setIdentity
     * 
     * 
     * 
     * @param identityArray
     */
    public void setIdentity(java.lang.String[] identityArray)
    {
        //-- copy array
        _identity.clear();
        for (int i = 0; i < identityArray.length; i++) {
            _identity.add(identityArray[i]);
        }
    } //-- void setIdentity(java.lang.String) 

    /**
     * Sets the value of field 'keyGenerator'.
     * 
     * @param keyGenerator the value of field 'keyGenerator'.
     */
    public void setKeyGenerator(java.lang.String keyGenerator)
    {
        this._keyGenerator = keyGenerator;
    } //-- void setKeyGenerator(java.lang.String) 

    /**
     * Sets the value of field 'mapTo'.
     * 
     * @param mapTo the value of field 'mapTo'.
     */
    public void setMapTo(org.exolab.castor.mapping.xml.MapTo mapTo)
    {
        this._mapTo = mapTo;
    } //-- void setMapTo(org.exolab.castor.mapping.xml.MapTo) 

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
     * Sets the value of field 'verifyConstructable'.
     * 
     * @param verifyConstructable the value of field
     * 'verifyConstructable'.
     */
    public void setVerifyConstructable(boolean verifyConstructable)
    {
        this._verifyConstructable = verifyConstructable;
        this._has_verifyConstructable = true;
    } //-- void setVerifyConstructable(boolean) 

    /**
     * Method unmarshal
     * 
     * 
     * 
     * @param reader
     * @return ClassMapping
     */
    public static org.exolab.castor.mapping.xml.ClassMapping unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.exolab.castor.mapping.xml.ClassMapping) Unmarshaller.unmarshal(org.exolab.castor.mapping.xml.ClassMapping.class, reader);
    } //-- org.exolab.castor.mapping.xml.ClassMapping unmarshal(java.io.Reader) 

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
