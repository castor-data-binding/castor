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
import org.exolab.castor.mapping.xml.types.SqlDirtyType;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.ContentHandler;

/**
 * Class Sql.
 * 
 * @version $Revision$ $Date: 2006-01-07 15:48:31 -0700 (Sat, 07 Jan 2006) $
 */
public class Sql implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _name
     */
    private java.util.ArrayList _name;

    /**
     * Field _type
     */
    private java.lang.String _type;

    /**
     * Field _manyTable
     */
    private java.lang.String _manyTable;

    /**
     * Field _manyKey
     */
    private java.util.ArrayList _manyKey;

    /**
     * Field _readOnly
     */
    private boolean _readOnly = false;

    /**
     * keeps track of state for field: _readOnly
     */
    private boolean _has_readOnly;

    /**
     * Field _transient
     */
    private boolean _transient;

    /**
     * keeps track of state for field: _transient
     */
    private boolean _has_transient;

    /**
     * Field _dirty
     */
    private org.exolab.castor.mapping.xml.types.SqlDirtyType _dirty = org.exolab.castor.mapping.xml.types.SqlDirtyType.valueOf("check");


      //----------------/
     //- Constructors -/
    //----------------/

    public Sql() 
     {
        super();
        _name = new ArrayList();
        _manyKey = new ArrayList();
        setDirty(org.exolab.castor.mapping.xml.types.SqlDirtyType.valueOf("check"));
    } //-- org.exolab.castor.mapping.xml.Sql()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addManyKey
     * 
     * 
     * 
     * @param vManyKey
     */
    public void addManyKey(java.lang.String vManyKey)
        throws java.lang.IndexOutOfBoundsException
    {
        _manyKey.add(vManyKey);
    } //-- void addManyKey(java.lang.String) 

    /**
     * Method addManyKey
     * 
     * 
     * 
     * @param index
     * @param vManyKey
     */
    public void addManyKey(int index, java.lang.String vManyKey)
        throws java.lang.IndexOutOfBoundsException
    {
        _manyKey.add(index, vManyKey);
    } //-- void addManyKey(int, java.lang.String) 

    /**
     * Method addName
     * 
     * 
     * 
     * @param vName
     */
    public void addName(java.lang.String vName)
        throws java.lang.IndexOutOfBoundsException
    {
        _name.add(vName);
    } //-- void addName(java.lang.String) 

    /**
     * Method addName
     * 
     * 
     * 
     * @param index
     * @param vName
     */
    public void addName(int index, java.lang.String vName)
        throws java.lang.IndexOutOfBoundsException
    {
        _name.add(index, vName);
    } //-- void addName(int, java.lang.String) 

    /**
     * Method clearManyKey
     * 
     */
    public void clearManyKey()
    {
        _manyKey.clear();
    } //-- void clearManyKey() 

    /**
     * Method clearName
     * 
     */
    public void clearName()
    {
        _name.clear();
    } //-- void clearName() 

    /**
     * Method deleteReadOnly
     * 
     */
    public void deleteReadOnly()
    {
        this._has_readOnly= false;
    } //-- void deleteReadOnly() 

    /**
     * Method deleteTransient
     * 
     */
    public void deleteTransient()
    {
        this._has_transient= false;
    } //-- void deleteTransient() 

    /**
     * Method enumerateManyKey
     * 
     * 
     * 
     * @return Enumeration
     */
    public java.util.Enumeration enumerateManyKey()
    {
        return new org.exolab.castor.util.IteratorEnumeration(_manyKey.iterator());
    } //-- java.util.Enumeration enumerateManyKey() 

    /**
     * Method enumerateName
     * 
     * 
     * 
     * @return Enumeration
     */
    public java.util.Enumeration enumerateName()
    {
        return new org.exolab.castor.util.IteratorEnumeration(_name.iterator());
    } //-- java.util.Enumeration enumerateName() 

    /**
     * Returns the value of field 'dirty'.
     * 
     * @return SqlDirtyType
     * @return the value of field 'dirty'.
     */
    public org.exolab.castor.mapping.xml.types.SqlDirtyType getDirty()
    {
        return this._dirty;
    } //-- org.exolab.castor.mapping.xml.types.SqlDirtyType getDirty() 

    /**
     * Method getManyKey
     * 
     * 
     * 
     * @param index
     * @return String
     */
    public java.lang.String getManyKey(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _manyKey.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (String)_manyKey.get(index);
    } //-- java.lang.String getManyKey(int) 

    /**
     * Method getManyKey
     * 
     * 
     * 
     * @return String
     */
    public java.lang.String[] getManyKey()
    {
        int size = _manyKey.size();
        java.lang.String[] mArray = new java.lang.String[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (String)_manyKey.get(index);
        }
        return mArray;
    } //-- java.lang.String[] getManyKey() 

    /**
     * Method getManyKeyCount
     * 
     * 
     * 
     * @return int
     */
    public int getManyKeyCount()
    {
        return _manyKey.size();
    } //-- int getManyKeyCount() 

    /**
     * Returns the value of field 'manyTable'.
     * 
     * @return String
     * @return the value of field 'manyTable'.
     */
    public java.lang.String getManyTable()
    {
        return this._manyTable;
    } //-- java.lang.String getManyTable() 

    /**
     * Method getName
     * 
     * 
     * 
     * @param index
     * @return String
     */
    public java.lang.String getName(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _name.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (String)_name.get(index);
    } //-- java.lang.String getName(int) 

    /**
     * Method getName
     * 
     * 
     * 
     * @return String
     */
    public java.lang.String[] getName()
    {
        int size = _name.size();
        java.lang.String[] mArray = new java.lang.String[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (String)_name.get(index);
        }
        return mArray;
    } //-- java.lang.String[] getName() 

    /**
     * Method getNameCount
     * 
     * 
     * 
     * @return int
     */
    public int getNameCount()
    {
        return _name.size();
    } //-- int getNameCount() 

    /**
     * Returns the value of field 'readOnly'.
     * 
     * @return boolean
     * @return the value of field 'readOnly'.
     */
    public boolean getReadOnly()
    {
        return this._readOnly;
    } //-- boolean getReadOnly() 

    /**
     * Returns the value of field 'transient'.
     * 
     * @return boolean
     * @return the value of field 'transient'.
     */
    public boolean getTransient()
    {
        return this._transient;
    } //-- boolean getTransient() 

    /**
     * Returns the value of field 'type'.
     * 
     * @return String
     * @return the value of field 'type'.
     */
    public java.lang.String getType()
    {
        return this._type;
    } //-- java.lang.String getType() 

    /**
     * Method hasReadOnly
     * 
     * 
     * 
     * @return boolean
     */
    public boolean hasReadOnly()
    {
        return this._has_readOnly;
    } //-- boolean hasReadOnly() 

    /**
     * Method hasTransient
     * 
     * 
     * 
     * @return boolean
     */
    public boolean hasTransient()
    {
        return this._has_transient;
    } //-- boolean hasTransient() 

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
     * Method removeManyKey
     * 
     * 
     * 
     * @param vManyKey
     * @return boolean
     */
    public boolean removeManyKey(java.lang.String vManyKey)
    {
        boolean removed = _manyKey.remove(vManyKey);
        return removed;
    } //-- boolean removeManyKey(java.lang.String) 

    /**
     * Method removeName
     * 
     * 
     * 
     * @param vName
     * @return boolean
     */
    public boolean removeName(java.lang.String vName)
    {
        boolean removed = _name.remove(vName);
        return removed;
    } //-- boolean removeName(java.lang.String) 

    /**
     * Sets the value of field 'dirty'.
     * 
     * @param dirty the value of field 'dirty'.
     */
    public void setDirty(org.exolab.castor.mapping.xml.types.SqlDirtyType dirty)
    {
        this._dirty = dirty;
    } //-- void setDirty(org.exolab.castor.mapping.xml.types.SqlDirtyType) 

    /**
     * Method setManyKey
     * 
     * 
     * 
     * @param index
     * @param vManyKey
     */
    public void setManyKey(int index, java.lang.String vManyKey)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _manyKey.size())) {
            throw new IndexOutOfBoundsException();
        }
        _manyKey.set(index, vManyKey);
    } //-- void setManyKey(int, java.lang.String) 

    /**
     * Method setManyKey
     * 
     * 
     * 
     * @param manyKeyArray
     */
    public void setManyKey(java.lang.String[] manyKeyArray)
    {
        //-- copy array
        _manyKey.clear();
        for (int i = 0; i < manyKeyArray.length; i++) {
            _manyKey.add(manyKeyArray[i]);
        }
    } //-- void setManyKey(java.lang.String) 

    /**
     * Sets the value of field 'manyTable'.
     * 
     * @param manyTable the value of field 'manyTable'.
     */
    public void setManyTable(java.lang.String manyTable)
    {
        this._manyTable = manyTable;
    } //-- void setManyTable(java.lang.String) 

    /**
     * Method setName
     * 
     * 
     * 
     * @param index
     * @param vName
     */
    public void setName(int index, java.lang.String vName)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _name.size())) {
            throw new IndexOutOfBoundsException();
        }
        _name.set(index, vName);
    } //-- void setName(int, java.lang.String) 

    /**
     * Method setName
     * 
     * 
     * 
     * @param nameArray
     */
    public void setName(java.lang.String[] nameArray)
    {
        //-- copy array
        _name.clear();
        for (int i = 0; i < nameArray.length; i++) {
            _name.add(nameArray[i]);
        }
    } //-- void setName(java.lang.String) 

    /**
     * Sets the value of field 'readOnly'.
     * 
     * @param readOnly the value of field 'readOnly'.
     */
    public void setReadOnly(boolean readOnly)
    {
        this._readOnly = readOnly;
        this._has_readOnly = true;
    } //-- void setReadOnly(boolean) 

    /**
     * Sets the value of field 'transient'.
     * 
     * @param _transient
     * @param transient the value of field 'transient'.
     */
    public void setTransient(boolean _transient)
    {
        this._transient = _transient;
        this._has_transient = true;
    } //-- void setTransient(boolean) 

    /**
     * Sets the value of field 'type'.
     * 
     * @param type the value of field 'type'.
     */
    public void setType(java.lang.String type)
    {
        this._type = type;
    } //-- void setType(java.lang.String) 

    /**
     * Method unmarshal
     * 
     * 
     * 
     * @param reader
     * @return Sql
     */
    public static org.exolab.castor.mapping.xml.Sql unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.exolab.castor.mapping.xml.Sql) Unmarshaller.unmarshal(org.exolab.castor.mapping.xml.Sql.class, reader);
    } //-- org.exolab.castor.mapping.xml.Sql unmarshal(java.io.Reader) 

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
