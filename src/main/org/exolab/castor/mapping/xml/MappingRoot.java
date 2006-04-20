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
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.ContentHandler;

/**
 * Class MappingRoot.
 * 
 * @version $Revision$ $Date$
 */
public class MappingRoot implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _description
     */
    private java.lang.String _description;

    /**
     * Field _includeList
     */
    private java.util.ArrayList _includeList;

    /**
     * Field _classMappingList
     */
    private java.util.ArrayList _classMappingList;

    /**
     * Field _keyGeneratorDefList
     */
    private java.util.ArrayList _keyGeneratorDefList;


      //----------------/
     //- Constructors -/
    //----------------/

    public MappingRoot() 
     {
        super();
        _includeList = new ArrayList();
        _classMappingList = new ArrayList();
        _keyGeneratorDefList = new ArrayList();
    } //-- org.exolab.castor.mapping.xml.MappingRoot()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addClassMapping
     * 
     * 
     * 
     * @param vClassMapping
     */
    public void addClassMapping(org.exolab.castor.mapping.xml.ClassMapping vClassMapping)
        throws java.lang.IndexOutOfBoundsException
    {
        _classMappingList.add(vClassMapping);
    } //-- void addClassMapping(org.exolab.castor.mapping.xml.ClassMapping) 

    /**
     * Method addClassMapping
     * 
     * 
     * 
     * @param index
     * @param vClassMapping
     */
    public void addClassMapping(int index, org.exolab.castor.mapping.xml.ClassMapping vClassMapping)
        throws java.lang.IndexOutOfBoundsException
    {
        _classMappingList.add(index, vClassMapping);
    } //-- void addClassMapping(int, org.exolab.castor.mapping.xml.ClassMapping) 

    /**
     * Method addInclude
     * 
     * 
     * 
     * @param vInclude
     */
    public void addInclude(org.exolab.castor.mapping.xml.Include vInclude)
        throws java.lang.IndexOutOfBoundsException
    {
        _includeList.add(vInclude);
    } //-- void addInclude(org.exolab.castor.mapping.xml.Include) 

    /**
     * Method addInclude
     * 
     * 
     * 
     * @param index
     * @param vInclude
     */
    public void addInclude(int index, org.exolab.castor.mapping.xml.Include vInclude)
        throws java.lang.IndexOutOfBoundsException
    {
        _includeList.add(index, vInclude);
    } //-- void addInclude(int, org.exolab.castor.mapping.xml.Include) 

    /**
     * Method addKeyGeneratorDef
     * 
     * 
     * 
     * @param vKeyGeneratorDef
     */
    public void addKeyGeneratorDef(org.exolab.castor.mapping.xml.KeyGeneratorDef vKeyGeneratorDef)
        throws java.lang.IndexOutOfBoundsException
    {
        _keyGeneratorDefList.add(vKeyGeneratorDef);
    } //-- void addKeyGeneratorDef(org.exolab.castor.mapping.xml.KeyGeneratorDef) 

    /**
     * Method addKeyGeneratorDef
     * 
     * 
     * 
     * @param index
     * @param vKeyGeneratorDef
     */
    public void addKeyGeneratorDef(int index, org.exolab.castor.mapping.xml.KeyGeneratorDef vKeyGeneratorDef)
        throws java.lang.IndexOutOfBoundsException
    {
        _keyGeneratorDefList.add(index, vKeyGeneratorDef);
    } //-- void addKeyGeneratorDef(int, org.exolab.castor.mapping.xml.KeyGeneratorDef) 

    /**
     * Method clearClassMapping
     * 
     */
    public void clearClassMapping()
    {
        _classMappingList.clear();
    } //-- void clearClassMapping() 

    /**
     * Method clearInclude
     * 
     */
    public void clearInclude()
    {
        _includeList.clear();
    } //-- void clearInclude() 

    /**
     * Method clearKeyGeneratorDef
     * 
     */
    public void clearKeyGeneratorDef()
    {
        _keyGeneratorDefList.clear();
    } //-- void clearKeyGeneratorDef() 

    /**
     * Method enumerateClassMapping
     * 
     * 
     * 
     * @return Enumeration
     */
    public java.util.Enumeration enumerateClassMapping()
    {
        return new org.exolab.castor.util.IteratorEnumeration(_classMappingList.iterator());
    } //-- java.util.Enumeration enumerateClassMapping() 

    /**
     * Method enumerateInclude
     * 
     * 
     * 
     * @return Enumeration
     */
    public java.util.Enumeration enumerateInclude()
    {
        return new org.exolab.castor.util.IteratorEnumeration(_includeList.iterator());
    } //-- java.util.Enumeration enumerateInclude() 

    /**
     * Method enumerateKeyGeneratorDef
     * 
     * 
     * 
     * @return Enumeration
     */
    public java.util.Enumeration enumerateKeyGeneratorDef()
    {
        return new org.exolab.castor.util.IteratorEnumeration(_keyGeneratorDefList.iterator());
    } //-- java.util.Enumeration enumerateKeyGeneratorDef() 

    /**
     * Method getClassMapping
     * 
     * 
     * 
     * @param index
     * @return ClassMapping
     */
    public org.exolab.castor.mapping.xml.ClassMapping getClassMapping(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _classMappingList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (org.exolab.castor.mapping.xml.ClassMapping) _classMappingList.get(index);
    } //-- org.exolab.castor.mapping.xml.ClassMapping getClassMapping(int) 

    /**
     * Method getClassMapping
     * 
     * 
     * 
     * @return ClassMapping
     */
    public org.exolab.castor.mapping.xml.ClassMapping[] getClassMapping()
    {
        int size = _classMappingList.size();
        org.exolab.castor.mapping.xml.ClassMapping[] mArray = new org.exolab.castor.mapping.xml.ClassMapping[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (org.exolab.castor.mapping.xml.ClassMapping) _classMappingList.get(index);
        }
        return mArray;
    } //-- org.exolab.castor.mapping.xml.ClassMapping[] getClassMapping() 

    /**
     * Method getClassMappingCount
     * 
     * 
     * 
     * @return int
     */
    public int getClassMappingCount()
    {
        return _classMappingList.size();
    } //-- int getClassMappingCount() 

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
     * Method getInclude
     * 
     * 
     * 
     * @param index
     * @return Include
     */
    public org.exolab.castor.mapping.xml.Include getInclude(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _includeList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (org.exolab.castor.mapping.xml.Include) _includeList.get(index);
    } //-- org.exolab.castor.mapping.xml.Include getInclude(int) 

    /**
     * Method getInclude
     * 
     * 
     * 
     * @return Include
     */
    public org.exolab.castor.mapping.xml.Include[] getInclude()
    {
        int size = _includeList.size();
        org.exolab.castor.mapping.xml.Include[] mArray = new org.exolab.castor.mapping.xml.Include[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (org.exolab.castor.mapping.xml.Include) _includeList.get(index);
        }
        return mArray;
    } //-- org.exolab.castor.mapping.xml.Include[] getInclude() 

    /**
     * Method getIncludeCount
     * 
     * 
     * 
     * @return int
     */
    public int getIncludeCount()
    {
        return _includeList.size();
    } //-- int getIncludeCount() 

    /**
     * Method getKeyGeneratorDef
     * 
     * 
     * 
     * @param index
     * @return KeyGeneratorDef
     */
    public org.exolab.castor.mapping.xml.KeyGeneratorDef getKeyGeneratorDef(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _keyGeneratorDefList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (org.exolab.castor.mapping.xml.KeyGeneratorDef) _keyGeneratorDefList.get(index);
    } //-- org.exolab.castor.mapping.xml.KeyGeneratorDef getKeyGeneratorDef(int) 

    /**
     * Method getKeyGeneratorDef
     * 
     * 
     * 
     * @return KeyGeneratorDef
     */
    public org.exolab.castor.mapping.xml.KeyGeneratorDef[] getKeyGeneratorDef()
    {
        int size = _keyGeneratorDefList.size();
        org.exolab.castor.mapping.xml.KeyGeneratorDef[] mArray = new org.exolab.castor.mapping.xml.KeyGeneratorDef[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (org.exolab.castor.mapping.xml.KeyGeneratorDef) _keyGeneratorDefList.get(index);
        }
        return mArray;
    } //-- org.exolab.castor.mapping.xml.KeyGeneratorDef[] getKeyGeneratorDef() 

    /**
     * Method getKeyGeneratorDefCount
     * 
     * 
     * 
     * @return int
     */
    public int getKeyGeneratorDefCount()
    {
        return _keyGeneratorDefList.size();
    } //-- int getKeyGeneratorDefCount() 

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
     * Method removeClassMapping
     * 
     * 
     * 
     * @param vClassMapping
     * @return boolean
     */
    public boolean removeClassMapping(org.exolab.castor.mapping.xml.ClassMapping vClassMapping)
    {
        boolean removed = _classMappingList.remove(vClassMapping);
        return removed;
    } //-- boolean removeClassMapping(org.exolab.castor.mapping.xml.ClassMapping) 

    /**
     * Method removeInclude
     * 
     * 
     * 
     * @param vInclude
     * @return boolean
     */
    public boolean removeInclude(org.exolab.castor.mapping.xml.Include vInclude)
    {
        boolean removed = _includeList.remove(vInclude);
        return removed;
    } //-- boolean removeInclude(org.exolab.castor.mapping.xml.Include) 

    /**
     * Method removeKeyGeneratorDef
     * 
     * 
     * 
     * @param vKeyGeneratorDef
     * @return boolean
     */
    public boolean removeKeyGeneratorDef(org.exolab.castor.mapping.xml.KeyGeneratorDef vKeyGeneratorDef)
    {
        boolean removed = _keyGeneratorDefList.remove(vKeyGeneratorDef);
        return removed;
    } //-- boolean removeKeyGeneratorDef(org.exolab.castor.mapping.xml.KeyGeneratorDef) 

    /**
     * Method setClassMapping
     * 
     * 
     * 
     * @param index
     * @param vClassMapping
     */
    public void setClassMapping(int index, org.exolab.castor.mapping.xml.ClassMapping vClassMapping)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _classMappingList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _classMappingList.set(index, vClassMapping);
    } //-- void setClassMapping(int, org.exolab.castor.mapping.xml.ClassMapping) 

    /**
     * Method setClassMapping
     * 
     * 
     * 
     * @param classMappingArray
     */
    public void setClassMapping(org.exolab.castor.mapping.xml.ClassMapping[] classMappingArray)
    {
        //-- copy array
        _classMappingList.clear();
        for (int i = 0; i < classMappingArray.length; i++) {
            _classMappingList.add(classMappingArray[i]);
        }
    } //-- void setClassMapping(org.exolab.castor.mapping.xml.ClassMapping) 

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
     * Method setInclude
     * 
     * 
     * 
     * @param index
     * @param vInclude
     */
    public void setInclude(int index, org.exolab.castor.mapping.xml.Include vInclude)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _includeList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _includeList.set(index, vInclude);
    } //-- void setInclude(int, org.exolab.castor.mapping.xml.Include) 

    /**
     * Method setInclude
     * 
     * 
     * 
     * @param includeArray
     */
    public void setInclude(org.exolab.castor.mapping.xml.Include[] includeArray)
    {
        //-- copy array
        _includeList.clear();
        for (int i = 0; i < includeArray.length; i++) {
            _includeList.add(includeArray[i]);
        }
    } //-- void setInclude(org.exolab.castor.mapping.xml.Include) 

    /**
     * Method setKeyGeneratorDef
     * 
     * 
     * 
     * @param index
     * @param vKeyGeneratorDef
     */
    public void setKeyGeneratorDef(int index, org.exolab.castor.mapping.xml.KeyGeneratorDef vKeyGeneratorDef)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _keyGeneratorDefList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _keyGeneratorDefList.set(index, vKeyGeneratorDef);
    } //-- void setKeyGeneratorDef(int, org.exolab.castor.mapping.xml.KeyGeneratorDef) 

    /**
     * Method setKeyGeneratorDef
     * 
     * 
     * 
     * @param keyGeneratorDefArray
     */
    public void setKeyGeneratorDef(org.exolab.castor.mapping.xml.KeyGeneratorDef[] keyGeneratorDefArray)
    {
        //-- copy array
        _keyGeneratorDefList.clear();
        for (int i = 0; i < keyGeneratorDefArray.length; i++) {
            _keyGeneratorDefList.add(keyGeneratorDefArray[i]);
        }
    } //-- void setKeyGeneratorDef(org.exolab.castor.mapping.xml.KeyGeneratorDef) 

    /**
     * Method unmarshal
     * 
     * 
     * 
     * @param reader
     * @return MappingRoot
     */
    public static org.exolab.castor.mapping.xml.MappingRoot unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.exolab.castor.mapping.xml.MappingRoot) Unmarshaller.unmarshal(org.exolab.castor.mapping.xml.MappingRoot.class, reader);
    } //-- org.exolab.castor.mapping.xml.MappingRoot unmarshal(java.io.Reader) 

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
