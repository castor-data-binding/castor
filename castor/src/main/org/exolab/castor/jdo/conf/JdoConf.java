/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.5.3</a>, using an XML
 * Schema.
 * $Id$
 */

package org.exolab.castor.jdo.conf;

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
 * Class JdoConf.
 * 
 * @version $Revision$ $Date$
 */
public class JdoConf implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _databaseList
     */
    private java.util.ArrayList _databaseList;

    /**
     * Field _transactionDemarcation
     */
    private org.exolab.castor.jdo.conf.TransactionDemarcation _transactionDemarcation;


      //----------------/
     //- Constructors -/
    //----------------/

    public JdoConf() {
        super();
        _databaseList = new ArrayList();
    } //-- org.exolab.castor.jdo.conf.JdoConf()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addDatabase
     * 
     * @param vDatabase
     */
    public void addDatabase(org.exolab.castor.jdo.conf.Database vDatabase)
        throws java.lang.IndexOutOfBoundsException
    {
        _databaseList.add(vDatabase);
    } //-- void addDatabase(org.exolab.castor.jdo.conf.Database) 

    /**
     * Method addDatabase
     * 
     * @param index
     * @param vDatabase
     */
    public void addDatabase(int index, org.exolab.castor.jdo.conf.Database vDatabase)
        throws java.lang.IndexOutOfBoundsException
    {
        _databaseList.add(index, vDatabase);
    } //-- void addDatabase(int, org.exolab.castor.jdo.conf.Database) 

    /**
     * Method clearDatabase
     */
    public void clearDatabase()
    {
        _databaseList.clear();
    } //-- void clearDatabase() 

    /**
     * Method enumerateDatabase
     */
    public java.util.Enumeration enumerateDatabase()
    {
        return new org.exolab.castor.util.IteratorEnumeration(_databaseList.iterator());
    } //-- java.util.Enumeration enumerateDatabase() 

    /**
     * Method getDatabase
     * 
     * @param index
     */
    public org.exolab.castor.jdo.conf.Database getDatabase(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _databaseList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (org.exolab.castor.jdo.conf.Database) _databaseList.get(index);
    } //-- org.exolab.castor.jdo.conf.Database getDatabase(int) 

    /**
     * Method getDatabase
     */
    public org.exolab.castor.jdo.conf.Database[] getDatabase()
    {
        int size = _databaseList.size();
        org.exolab.castor.jdo.conf.Database[] mArray = new org.exolab.castor.jdo.conf.Database[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (org.exolab.castor.jdo.conf.Database) _databaseList.get(index);
        }
        return mArray;
    } //-- org.exolab.castor.jdo.conf.Database[] getDatabase() 

    /**
     * Method getDatabaseCount
     */
    public int getDatabaseCount()
    {
        return _databaseList.size();
    } //-- int getDatabaseCount() 

    /**
     * Returns the value of field 'transactionDemarcation'.
     * 
     * @return the value of field 'transactionDemarcation'.
     */
    public org.exolab.castor.jdo.conf.TransactionDemarcation getTransactionDemarcation()
    {
        return this._transactionDemarcation;
    } //-- org.exolab.castor.jdo.conf.TransactionDemarcation getTransactionDemarcation() 

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
     * Method removeDatabase
     * 
     * @param vDatabase
     */
    public boolean removeDatabase(org.exolab.castor.jdo.conf.Database vDatabase)
    {
        boolean removed = _databaseList.remove(vDatabase);
        return removed;
    } //-- boolean removeDatabase(org.exolab.castor.jdo.conf.Database) 

    /**
     * Method setDatabase
     * 
     * @param index
     * @param vDatabase
     */
    public void setDatabase(int index, org.exolab.castor.jdo.conf.Database vDatabase)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _databaseList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _databaseList.set(index, vDatabase);
    } //-- void setDatabase(int, org.exolab.castor.jdo.conf.Database) 

    /**
     * Method setDatabase
     * 
     * @param databaseArray
     */
    public void setDatabase(org.exolab.castor.jdo.conf.Database[] databaseArray)
    {
        //-- copy array
        _databaseList.clear();
        for (int i = 0; i < databaseArray.length; i++) {
            _databaseList.add(databaseArray[i]);
        }
    } //-- void setDatabase(org.exolab.castor.jdo.conf.Database) 

    /**
     * Sets the value of field 'transactionDemarcation'.
     * 
     * @param transactionDemarcation the value of field
     * 'transactionDemarcation'.
     */
    public void setTransactionDemarcation(org.exolab.castor.jdo.conf.TransactionDemarcation transactionDemarcation)
    {
        this._transactionDemarcation = transactionDemarcation;
    } //-- void setTransactionDemarcation(org.exolab.castor.jdo.conf.TransactionDemarcation) 

    /**
     * Method unmarshal
     * 
     * @param reader
     */
    public static java.lang.Object unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.exolab.castor.jdo.conf.JdoConf) Unmarshaller.unmarshal(org.exolab.castor.jdo.conf.JdoConf.class, reader);
    } //-- java.lang.Object unmarshal(java.io.Reader) 

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
