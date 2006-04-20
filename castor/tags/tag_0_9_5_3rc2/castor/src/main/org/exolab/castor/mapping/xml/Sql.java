/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
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
import java.util.Enumeration;
import java.util.Vector;
import org.exolab.castor.mapping.xml.types.DirtyType;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.ContentHandler;

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
     * Field _name
     */
    private java.util.Vector _name;

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
    private java.util.Vector _manyKey;

    /**
     * Field _readonly
     */
    private boolean _readonly = false;

    /**
     * keeps track of state for field: _readonly
     */
    private boolean _has_readonly;

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
    private org.exolab.castor.mapping.xml.types.DirtyType _dirty = org.exolab.castor.mapping.xml.types.DirtyType.valueOf("check");


      //----------------/
     //- Constructors -/
    //----------------/

    public Sql() {
        super();
        _name = new Vector();
        _manyKey = new Vector();
        setDirty(org.exolab.castor.mapping.xml.types.DirtyType.valueOf("check"));
    } //-- org.exolab.castor.mapping.xml.Sql()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addManyKey
     * 
     * @param vManyKey
     */
    public void addManyKey(java.lang.String vManyKey)
        throws java.lang.IndexOutOfBoundsException
    {
        _manyKey.addElement(vManyKey);
    } //-- void addManyKey(java.lang.String) 

    /**
     * Method addManyKey
     * 
     * @param index
     * @param vManyKey
     */
    public void addManyKey(int index, java.lang.String vManyKey)
        throws java.lang.IndexOutOfBoundsException
    {
        _manyKey.insertElementAt(vManyKey, index);
    } //-- void addManyKey(int, java.lang.String) 

    /**
     * Method addName
     * 
     * @param vName
     */
    public void addName(java.lang.String vName)
        throws java.lang.IndexOutOfBoundsException
    {
        _name.addElement(vName);
    } //-- void addName(java.lang.String) 

    /**
     * Method addName
     * 
     * @param index
     * @param vName
     */
    public void addName(int index, java.lang.String vName)
        throws java.lang.IndexOutOfBoundsException
    {
        _name.insertElementAt(vName, index);
    } //-- void addName(int, java.lang.String) 

    /**
     * Method deleteReadonly
     */
    public void deleteReadonly()
    {
        this._has_readonly= false;
    } //-- void deleteReadonly() 

    /**
     * Method deleteTransient
     */
    public void deleteTransient()
    {
        this._has_transient= false;
    } //-- void deleteTransient() 

    /**
     * Method enumerateManyKey
     */
    public java.util.Enumeration enumerateManyKey()
    {
        return _manyKey.elements();
    } //-- java.util.Enumeration enumerateManyKey() 

    /**
     * Method enumerateName
     */
    public java.util.Enumeration enumerateName()
    {
        return _name.elements();
    } //-- java.util.Enumeration enumerateName() 

    /**
     * Returns the value of field 'dirty'.
     * 
     * @return the value of field 'dirty'.
     */
    public org.exolab.castor.mapping.xml.types.DirtyType getDirty()
    {
        return this._dirty;
    } //-- org.exolab.castor.mapping.xml.types.DirtyType getDirty() 

    /**
     * Method getManyKey
     * 
     * @param index
     */
    public java.lang.String getManyKey(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _manyKey.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (String)_manyKey.elementAt(index);
    } //-- java.lang.String getManyKey(int) 

    /**
     * Method getManyKey
     */
    public java.lang.String[] getManyKey()
    {
        int size = _manyKey.size();
        java.lang.String[] mArray = new java.lang.String[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (String)_manyKey.elementAt(index);
        }
        return mArray;
    } //-- java.lang.String[] getManyKey() 

    /**
     * Method getManyKeyCount
     */
    public int getManyKeyCount()
    {
        return _manyKey.size();
    } //-- int getManyKeyCount() 

    /**
     * Returns the value of field 'manyTable'.
     * 
     * @return the value of field 'manyTable'.
     */
    public java.lang.String getManyTable()
    {
        return this._manyTable;
    } //-- java.lang.String getManyTable() 

    /**
     * Method getName
     * 
     * @param index
     */
    public java.lang.String getName(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _name.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (String)_name.elementAt(index);
    } //-- java.lang.String getName(int) 

    /**
     * Method getName
     */
    public java.lang.String[] getName()
    {
        int size = _name.size();
        java.lang.String[] mArray = new java.lang.String[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (String)_name.elementAt(index);
        }
        return mArray;
    } //-- java.lang.String[] getName() 

    /**
     * Method getNameCount
     */
    public int getNameCount()
    {
        return _name.size();
    } //-- int getNameCount() 

    /**
     * Returns the value of field 'readonly'.
     * 
     * @return the value of field 'readonly'.
     */
    public boolean getReadonly()
    {
        return this._readonly;
    } //-- boolean getReadonly() 

    /**
     * Returns the value of field 'transient'.
     * 
     * @return the value of field 'transient'.
     */
    public boolean getTransient()
    {
        return this._transient;
    } //-- boolean getTransient() 

    /**
     * Returns the value of field 'type'.
     * 
     * @return the value of field 'type'.
     */
    public java.lang.String getType()
    {
        return this._type;
    } //-- java.lang.String getType() 

    /**
     * Method hasReadonly
     */
    public boolean hasReadonly()
    {
        return this._has_readonly;
    } //-- boolean hasReadonly() 

    /**
     * Method hasTransient
     */
    public boolean hasTransient()
    {
        return this._has_transient;
    } //-- boolean hasTransient() 

    /**
     * Method isValid
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
     * @param handler
     */
    public void marshal(org.xml.sax.ContentHandler handler)
        throws java.io.IOException, org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        
        Marshaller.marshal(this, handler);
    } //-- void marshal(org.xml.sax.ContentHandler) 

    /**
     * Method removeAllManyKey
     */
    public void removeAllManyKey()
    {
        _manyKey.removeAllElements();
    } //-- void removeAllManyKey() 

    /**
     * Method removeAllName
     */
    public void removeAllName()
    {
        _name.removeAllElements();
    } //-- void removeAllName() 

    /**
     * Method removeManyKey
     * 
     * @param index
     */
    public java.lang.String removeManyKey(int index)
    {
        java.lang.Object obj = _manyKey.elementAt(index);
        _manyKey.removeElementAt(index);
        return (String)obj;
    } //-- java.lang.String removeManyKey(int) 

    /**
     * Method removeName
     * 
     * @param index
     */
    public java.lang.String removeName(int index)
    {
        java.lang.Object obj = _name.elementAt(index);
        _name.removeElementAt(index);
        return (String)obj;
    } //-- java.lang.String removeName(int) 

    /**
     * Sets the value of field 'dirty'.
     * 
     * @param dirty the value of field 'dirty'.
     */
    public void setDirty(org.exolab.castor.mapping.xml.types.DirtyType dirty)
    {
        this._dirty = dirty;
    } //-- void setDirty(org.exolab.castor.mapping.xml.types.DirtyType) 

    /**
     * Method setManyKey
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
        _manyKey.setElementAt(vManyKey, index);
    } //-- void setManyKey(int, java.lang.String) 

    /**
     * Method setManyKey
     * 
     * @param manyKeyArray
     */
    public void setManyKey(java.lang.String[] manyKeyArray)
    {
        //-- copy array
        _manyKey.removeAllElements();
        for (int i = 0; i < manyKeyArray.length; i++) {
            _manyKey.addElement(manyKeyArray[i]);
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
        _name.setElementAt(vName, index);
    } //-- void setName(int, java.lang.String) 

    /**
     * Method setName
     * 
     * @param nameArray
     */
    public void setName(java.lang.String[] nameArray)
    {
        //-- copy array
        _name.removeAllElements();
        for (int i = 0; i < nameArray.length; i++) {
            _name.addElement(nameArray[i]);
        }
    } //-- void setName(java.lang.String) 

    /**
     * Sets the value of field 'readonly'.
     * 
     * @param readonly the value of field 'readonly'.
     */
    public void setReadonly(boolean readonly)
    {
        this._readonly = readonly;
        this._has_readonly = true;
    } //-- void setReadonly(boolean) 

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
     * @param reader
     */
    public static org.exolab.castor.mapping.xml.Sql unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.exolab.castor.mapping.xml.Sql) Unmarshaller.unmarshal(org.exolab.castor.mapping.xml.Sql.class, reader);
    } //-- org.exolab.castor.mapping.xml.Sql unmarshal(java.io.Reader) 

    /**
     * Method validate
     */
    public void validate()
        throws org.exolab.castor.xml.ValidationException
    {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    } //-- void validate() 

}
