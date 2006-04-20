/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.9.1</a>, using an XML
 * Schema.
 * $Id$
 */

package org.castor.jdo.conf;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.util.ArrayList;

import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

/**
 * Class Database.
 * 
 * @version $Revision$ $Date$
 */
public class Database implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _name
     */
    private java.lang.String _name;

    /**
     * Field _engine
     */
    private java.lang.String _engine = "generic";

    /**
     * Field _databaseChoice
     */
    private org.castor.jdo.conf.DatabaseChoice _databaseChoice;

    /**
     * Field _mappingList
     */
    private java.util.ArrayList _mappingList;


      //----------------/
     //- Constructors -/
    //----------------/

    public Database() 
     {
        super();
        setEngine("generic");
        _mappingList = new ArrayList();
    } //-- org.castor.jdo.conf.Database()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addMapping
     * 
     * 
     * 
     * @param vMapping
     */
    public void addMapping(org.castor.jdo.conf.Mapping vMapping)
        throws java.lang.IndexOutOfBoundsException
    {
        _mappingList.add(vMapping);
    } //-- void addMapping(org.castor.jdo.conf.Mapping) 

    /**
     * Method addMapping
     * 
     * 
     * 
     * @param index
     * @param vMapping
     */
    public void addMapping(int index, org.castor.jdo.conf.Mapping vMapping)
        throws java.lang.IndexOutOfBoundsException
    {
        _mappingList.add(index, vMapping);
    } //-- void addMapping(int, org.castor.jdo.conf.Mapping) 

    /**
     * Method clearMapping
     * 
     */
    public void clearMapping()
    {
        _mappingList.clear();
    } //-- void clearMapping() 

    /**
     * Method enumerateMapping
     * 
     * 
     * 
     * @return Enumeration
     */
    public java.util.Enumeration enumerateMapping()
    {
        return new org.exolab.castor.util.IteratorEnumeration(_mappingList.iterator());
    } //-- java.util.Enumeration enumerateMapping() 

    /**
     * Returns the value of field 'databaseChoice'.
     * 
     * @return DatabaseChoice
     * @return the value of field 'databaseChoice'.
     */
    public org.castor.jdo.conf.DatabaseChoice getDatabaseChoice()
    {
        return this._databaseChoice;
    } //-- org.castor.jdo.conf.DatabaseChoice getDatabaseChoice() 

    /**
     * Returns the value of field 'engine'.
     * 
     * @return String
     * @return the value of field 'engine'.
     */
    public java.lang.String getEngine()
    {
        return this._engine;
    } //-- java.lang.String getEngine() 

    /**
     * Method getMapping
     * 
     * 
     * 
     * @param index
     * @return Mapping
     */
    public org.castor.jdo.conf.Mapping getMapping(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _mappingList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (org.castor.jdo.conf.Mapping) _mappingList.get(index);
    } //-- org.castor.jdo.conf.Mapping getMapping(int) 

    /**
     * Method getMapping
     * 
     * 
     * 
     * @return Mapping
     */
    public org.castor.jdo.conf.Mapping[] getMapping()
    {
        int size = _mappingList.size();
        org.castor.jdo.conf.Mapping[] mArray = new org.castor.jdo.conf.Mapping[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (org.castor.jdo.conf.Mapping) _mappingList.get(index);
        }
        return mArray;
    } //-- org.castor.jdo.conf.Mapping[] getMapping() 

    /**
     * Method getMappingCount
     * 
     * 
     * 
     * @return int
     */
    public int getMappingCount()
    {
        return _mappingList.size();
    } //-- int getMappingCount() 

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
     * Method removeMapping
     * 
     * 
     * 
     * @param vMapping
     * @return boolean
     */
    public boolean removeMapping(org.castor.jdo.conf.Mapping vMapping)
    {
        boolean removed = _mappingList.remove(vMapping);
        return removed;
    } //-- boolean removeMapping(org.castor.jdo.conf.Mapping) 

    /**
     * Sets the value of field 'databaseChoice'.
     * 
     * @param databaseChoice the value of field 'databaseChoice'.
     */
    public void setDatabaseChoice(org.castor.jdo.conf.DatabaseChoice databaseChoice)
    {
        this._databaseChoice = databaseChoice;
    } //-- void setDatabaseChoice(org.castor.jdo.conf.DatabaseChoice) 

    /**
     * Sets the value of field 'engine'.
     * 
     * @param engine the value of field 'engine'.
     */
    public void setEngine(java.lang.String engine)
    {
        this._engine = engine;
    } //-- void setEngine(java.lang.String) 

    /**
     * Method setMapping
     * 
     * 
     * 
     * @param index
     * @param vMapping
     */
    public void setMapping(int index, org.castor.jdo.conf.Mapping vMapping)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _mappingList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _mappingList.set(index, vMapping);
    } //-- void setMapping(int, org.castor.jdo.conf.Mapping) 

    /**
     * Method setMapping
     * 
     * 
     * 
     * @param mappingArray
     */
    public void setMapping(org.castor.jdo.conf.Mapping[] mappingArray)
    {
        //-- copy array
        _mappingList.clear();
        for (int i = 0; i < mappingArray.length; i++) {
            _mappingList.add(mappingArray[i]);
        }
    } //-- void setMapping(org.castor.jdo.conf.Mapping) 

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
     * Method unmarshal
     * 
     * 
     * 
     * @param reader
     * @return Database
     */
    public static org.castor.jdo.conf.Database unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.castor.jdo.conf.Database) Unmarshaller.unmarshal(org.castor.jdo.conf.Database.class, reader);
    } //-- org.castor.jdo.conf.Database unmarshal(java.io.Reader) 

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
