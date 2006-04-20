/*
 * This class was automatically generated with 
 * <a href="http://castor.exolab.org">Castor 0.8.12</a>, using an
 * XML Schema.
 * $Id$
 */

package org.exolab.castor.mapping.xml;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import java.util.Enumeration;
import java.util.Vector;
import org.exolab.castor.mapping.xml.types.DirtyType;
import org.exolab.castor.xml.*;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.DocumentHandler;

/**
 * 
 * @version $Revision$ $Date$
**/
public class Sql implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    private java.util.Vector _name;

    private java.util.Vector _type;

    private java.lang.String _manyTable;

    private java.util.Vector _manyKey;

    private org.exolab.castor.mapping.xml.types.DirtyType _dirty = org.exolab.castor.mapping.xml.types.DirtyType.valueOf("check");;


      //----------------/
     //- Constructors -/
    //----------------/

    public Sql() {
        super();
        _name = new Vector();
        _type = new Vector();
        _manyKey = new Vector();
    } //-- org.exolab.castor.mapping.xml.Sql()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * 
     * @param vManyKey
    **/
    public void addManyKey(java.lang.String vManyKey)
        throws java.lang.IndexOutOfBoundsException
    {
        _manyKey.addElement(vManyKey);
    } //-- void addManyKey(java.lang.String) 

    /**
     * 
     * @param vName
    **/
    public void addName(java.lang.String vName)
        throws java.lang.IndexOutOfBoundsException
    {
        _name.addElement(vName);
    } //-- void addName(java.lang.String) 

    /**
     * 
     * @param vType
    **/
    public void addType(java.lang.String vType)
        throws java.lang.IndexOutOfBoundsException
    {
        _type.addElement(vType);
    } //-- void addType(java.lang.String) 

    /**
    **/
    public java.util.Enumeration enumerateManyKey()
    {
        return _manyKey.elements();
    } //-- java.util.Enumeration enumerateManyKey() 

    /**
    **/
    public java.util.Enumeration enumerateName()
    {
        return _name.elements();
    } //-- java.util.Enumeration enumerateName() 

    /**
    **/
    public java.util.Enumeration enumerateType()
    {
        return _type.elements();
    } //-- java.util.Enumeration enumerateType() 

    /**
    **/
    public org.exolab.castor.mapping.xml.types.DirtyType getDirty()
    {
        return this._dirty;
    } //-- org.exolab.castor.mapping.xml.types.DirtyType getDirty() 

    /**
     * 
     * @param index
    **/
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
    **/
    public java.lang.String[] getManyKey()
    {
        int size = _manyKey.size();
        java.lang.String[] mArray = new String[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (String)_manyKey.elementAt(index);
        }
        return mArray;
    } //-- java.lang.String[] getManyKey() 

    /**
    **/
    public int getManyKeyCount()
    {
        return _manyKey.size();
    } //-- int getManyKeyCount() 

    /**
    **/
    public java.lang.String getManyTable()
    {
        return this._manyTable;
    } //-- java.lang.String getManyTable() 

    /**
     * 
     * @param index
    **/
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
    **/
    public java.lang.String[] getName()
    {
        int size = _name.size();
        java.lang.String[] mArray = new String[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (String)_name.elementAt(index);
        }
        return mArray;
    } //-- java.lang.String[] getName() 

    /**
    **/
    public int getNameCount()
    {
        return _name.size();
    } //-- int getNameCount() 

    /**
     * 
     * @param index
    **/
    public java.lang.String getType(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _type.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (String)_type.elementAt(index);
    } //-- java.lang.String getType(int) 

    /**
    **/
    public java.lang.String[] getType()
    {
        int size = _type.size();
        java.lang.String[] mArray = new String[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (String)_type.elementAt(index);
        }
        return mArray;
    } //-- java.lang.String[] getType() 

    /**
    **/
    public int getTypeCount()
    {
        return _type.size();
    } //-- int getTypeCount() 

    /**
    **/
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
     * 
     * @param out
    **/
    public void marshal(java.io.Writer out)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        
        Marshaller.marshal(this, out);
    } //-- void marshal(java.io.Writer) 

    /**
     * 
     * @param handler
    **/
    public void marshal(org.xml.sax.DocumentHandler handler)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        
        Marshaller.marshal(this, handler);
    } //-- void marshal(org.xml.sax.DocumentHandler) 

    /**
    **/
    public void removeAllManyKey()
    {
        _manyKey.removeAllElements();
    } //-- void removeAllManyKey() 

    /**
    **/
    public void removeAllName()
    {
        _name.removeAllElements();
    } //-- void removeAllName() 

    /**
    **/
    public void removeAllType()
    {
        _type.removeAllElements();
    } //-- void removeAllType() 

    /**
     * 
     * @param index
    **/
    public java.lang.String removeManyKey(int index)
    {
        Object obj = _manyKey.elementAt(index);
        _manyKey.removeElementAt(index);
        return (String)obj;
    } //-- java.lang.String removeManyKey(int) 

    /**
     * 
     * @param index
    **/
    public java.lang.String removeName(int index)
    {
        Object obj = _name.elementAt(index);
        _name.removeElementAt(index);
        return (String)obj;
    } //-- java.lang.String removeName(int) 

    /**
     * 
     * @param index
    **/
    public java.lang.String removeType(int index)
    {
        Object obj = _type.elementAt(index);
        _type.removeElementAt(index);
        return (String)obj;
    } //-- java.lang.String removeType(int) 

    /**
     * 
     * @param _dirty
    **/
    public void setDirty(org.exolab.castor.mapping.xml.types.DirtyType _dirty)
    {
        this._dirty = _dirty;
    } //-- void setDirty(org.exolab.castor.mapping.xml.types.DirtyType) 

    /**
     * 
     * @param vManyKey
     * @param index
    **/
    public void setManyKey(java.lang.String vManyKey, int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _manyKey.size())) {
            throw new IndexOutOfBoundsException();
        }
        _manyKey.setElementAt(vManyKey, index);
    } //-- void setManyKey(java.lang.String, int) 

    /**
     * 
     * @param manyKeyArray
    **/
    public void setManyKey(java.lang.String[] manyKeyArray)
    {
        //-- copy array
        _manyKey.removeAllElements();
        for (int i = 0; i < manyKeyArray.length; i++) {
            _manyKey.addElement(manyKeyArray[i]);
        }
    } //-- void setManyKey(java.lang.String) 

    /**
     * 
     * @param _manyTable
    **/
    public void setManyTable(java.lang.String _manyTable)
    {
        this._manyTable = _manyTable;
    } //-- void setManyTable(java.lang.String) 

    /**
     * 
     * @param vName
     * @param index
    **/
    public void setName(java.lang.String vName, int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _name.size())) {
            throw new IndexOutOfBoundsException();
        }
        _name.setElementAt(vName, index);
    } //-- void setName(java.lang.String, int) 

    /**
     * 
     * @param nameArray
    **/
    public void setName(java.lang.String[] nameArray)
    {
        //-- copy array
        _name.removeAllElements();
        for (int i = 0; i < nameArray.length; i++) {
            _name.addElement(nameArray[i]);
        }
    } //-- void setName(java.lang.String) 

    /**
     * 
     * @param vType
     * @param index
    **/
    public void setType(java.lang.String vType, int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _type.size())) {
            throw new IndexOutOfBoundsException();
        }
        _type.setElementAt(vType, index);
    } //-- void setType(java.lang.String, int) 

    /**
     * 
     * @param typeArray
    **/
    public void setType(java.lang.String[] typeArray)
    {
        //-- copy array
        _type.removeAllElements();
        for (int i = 0; i < typeArray.length; i++) {
            _type.addElement(typeArray[i]);
        }
    } //-- void setType(java.lang.String) 

    /**
     * 
     * @param reader
    **/
    public static org.exolab.castor.mapping.xml.Sql unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.exolab.castor.mapping.xml.Sql) Unmarshaller.unmarshal(org.exolab.castor.mapping.xml.Sql.class, reader);
    } //-- org.exolab.castor.mapping.xml.Sql unmarshal(java.io.Reader) 

    /**
    **/
    public void validate()
        throws org.exolab.castor.xml.ValidationException
    {
        Validator validator = new Validator();
        validator.validate(this);
    } //-- void validate() 

}
