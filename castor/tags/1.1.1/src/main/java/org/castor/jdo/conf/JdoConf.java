/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.0.1</a>, using an XML
 * Schema.
 * $Id$
 */

package org.castor.jdo.conf;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

/**
 * Class JdoConf.
 * 
 * @version $Revision$ $Date$
 */
public class JdoConf implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _name
     */
    private java.lang.String _name = "jdo-conf";

    /**
     * Field _databaseList
     */
    private java.util.ArrayList _databaseList;

    /**
     * Field _transactionDemarcation
     */
    private org.castor.jdo.conf.TransactionDemarcation _transactionDemarcation;


      //----------------/
     //- Constructors -/
    //----------------/

    public JdoConf() 
     {
        super();
        setName("jdo-conf");
        _databaseList = new java.util.ArrayList();
    } //-- org.castor.jdo.conf.JdoConf()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addDatabase
     * 
     * 
     * 
     * @param vDatabase
     */
    public void addDatabase(org.castor.jdo.conf.Database vDatabase)
        throws java.lang.IndexOutOfBoundsException
    {
        _databaseList.add(vDatabase);
    } //-- void addDatabase(org.castor.jdo.conf.Database) 

    /**
     * Method addDatabase
     * 
     * 
     * 
     * @param index
     * @param vDatabase
     */
    public void addDatabase(int index, org.castor.jdo.conf.Database vDatabase)
        throws java.lang.IndexOutOfBoundsException
    {
        _databaseList.add(index, vDatabase);
    } //-- void addDatabase(int, org.castor.jdo.conf.Database) 

    /**
     * Method clearDatabase
     * 
     */
    public void clearDatabase()
    {
        _databaseList.clear();
    } //-- void clearDatabase() 

    /**
     * Method enumerateDatabase
     * 
     * 
     * 
     * @return Enumeration
     */
    public java.util.Enumeration enumerateDatabase()
    {
        return new org.exolab.castor.util.IteratorEnumeration(_databaseList.iterator());
    } //-- java.util.Enumeration enumerateDatabase() 

    /**
     * Method getDatabase
     * 
     * 
     * 
     * @param index
     * @return Database
     */
    public org.castor.jdo.conf.Database getDatabase(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index >= _databaseList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (org.castor.jdo.conf.Database) _databaseList.get(index);
    } //-- org.castor.jdo.conf.Database getDatabase(int) 

    /**
     * Method getDatabase
     * 
     * 
     * 
     * @return Database
     */
    public org.castor.jdo.conf.Database[] getDatabase()
    {
        int size = _databaseList.size();
        org.castor.jdo.conf.Database[] mArray = new org.castor.jdo.conf.Database[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (org.castor.jdo.conf.Database) _databaseList.get(index);
        }
        return mArray;
    } //-- org.castor.jdo.conf.Database[] getDatabase() 

    /**
     * Method getDatabaseCount
     * 
     * 
     * 
     * @return int
     */
    public int getDatabaseCount()
    {
        return _databaseList.size();
    } //-- int getDatabaseCount() 

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
     * Returns the value of field 'transactionDemarcation'.
     * 
     * @return TransactionDemarcation
     * @return the value of field 'transactionDemarcation'.
     */
    public org.castor.jdo.conf.TransactionDemarcation getTransactionDemarcation()
    {
        return this._transactionDemarcation;
    } //-- org.castor.jdo.conf.TransactionDemarcation getTransactionDemarcation() 

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
     * Method removeDatabase
     * 
     * 
     * 
     * @param vDatabase
     * @return boolean
     */
    public boolean removeDatabase(org.castor.jdo.conf.Database vDatabase)
    {
        boolean removed = _databaseList.remove(vDatabase);
        return removed;
    } //-- boolean removeDatabase(org.castor.jdo.conf.Database) 

    /**
     * Method setDatabase
     * 
     * 
     * 
     * @param index
     * @param vDatabase
     */
    public void setDatabase(int index, org.castor.jdo.conf.Database vDatabase)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index >= _databaseList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _databaseList.set(index, vDatabase);
    } //-- void setDatabase(int, org.castor.jdo.conf.Database) 

    /**
     * Method setDatabase
     * 
     * 
     * 
     * @param databaseArray
     */
    public void setDatabase(org.castor.jdo.conf.Database[] databaseArray)
    {
        //-- copy array
        _databaseList.clear();
        for (int i = 0; i < databaseArray.length; i++) {
            _databaseList.add(databaseArray[i]);
        }
    } //-- void setDatabase(org.castor.jdo.conf.Database) 

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
     * Sets the value of field 'transactionDemarcation'.
     * 
     * @param transactionDemarcation the value of field
     * 'transactionDemarcation'.
     */
    public void setTransactionDemarcation(org.castor.jdo.conf.TransactionDemarcation transactionDemarcation)
    {
        this._transactionDemarcation = transactionDemarcation;
    } //-- void setTransactionDemarcation(org.castor.jdo.conf.TransactionDemarcation) 

    /**
     * Method unmarshal
     * 
     * 
     * 
     * @param reader
     * @return JdoConf
     */
    public static org.castor.jdo.conf.JdoConf unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.castor.jdo.conf.JdoConf) Unmarshaller.unmarshal(org.castor.jdo.conf.JdoConf.class, reader);
    } //-- org.castor.jdo.conf.JdoConf unmarshal(java.io.Reader) 

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
