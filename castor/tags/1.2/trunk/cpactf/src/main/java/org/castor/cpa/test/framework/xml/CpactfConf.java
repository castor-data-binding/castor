/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.1.2.1</a>, using an XML
 * Schema.
 * $Id$
 */

package org.castor.cpa.test.framework.xml;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

/**
 * Class CpactfConf.
 * 
 * @version $Revision$ $Date$
 */
public class CpactfConf implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _defaultDatabase.
     */
    private java.lang.String _defaultDatabase;

    /**
     * Field _defaultTransaction.
     */
    private java.lang.String _defaultTransaction;

    /**
     * Field _configurationList.
     */
    private java.util.List _configurationList;

    /**
     * Field _databaseList.
     */
    private java.util.List _databaseList;

    /**
     * Field _transactionList.
     */
    private java.util.List _transactionList;


      //----------------/
     //- Constructors -/
    //----------------/

    public CpactfConf() {
        super();
        this._configurationList = new java.util.ArrayList();
        this._databaseList = new java.util.ArrayList();
        this._transactionList = new java.util.ArrayList();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * 
     * 
     * @param vConfiguration
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addConfiguration(
            final org.castor.cpa.test.framework.xml.Configuration vConfiguration)
    throws java.lang.IndexOutOfBoundsException {
        this._configurationList.add(vConfiguration);
    }

    /**
     * 
     * 
     * @param index
     * @param vConfiguration
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addConfiguration(
            final int index,
            final org.castor.cpa.test.framework.xml.Configuration vConfiguration)
    throws java.lang.IndexOutOfBoundsException {
        this._configurationList.add(index, vConfiguration);
    }

    /**
     * 
     * 
     * @param vDatabase
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addDatabase(
            final org.castor.cpa.test.framework.xml.Database vDatabase)
    throws java.lang.IndexOutOfBoundsException {
        this._databaseList.add(vDatabase);
    }

    /**
     * 
     * 
     * @param index
     * @param vDatabase
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addDatabase(
            final int index,
            final org.castor.cpa.test.framework.xml.Database vDatabase)
    throws java.lang.IndexOutOfBoundsException {
        this._databaseList.add(index, vDatabase);
    }

    /**
     * 
     * 
     * @param vTransaction
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addTransaction(
            final org.castor.cpa.test.framework.xml.Transaction vTransaction)
    throws java.lang.IndexOutOfBoundsException {
        this._transactionList.add(vTransaction);
    }

    /**
     * 
     * 
     * @param index
     * @param vTransaction
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addTransaction(
            final int index,
            final org.castor.cpa.test.framework.xml.Transaction vTransaction)
    throws java.lang.IndexOutOfBoundsException {
        this._transactionList.add(index, vTransaction);
    }

    /**
     * Method enumerateConfiguration.
     * 
     * @return an Enumeration over all possible elements of this
     * collection
     */
    public java.util.Enumeration enumerateConfiguration(
    ) {
        return java.util.Collections.enumeration(this._configurationList);
    }

    /**
     * Method enumerateDatabase.
     * 
     * @return an Enumeration over all possible elements of this
     * collection
     */
    public java.util.Enumeration enumerateDatabase(
    ) {
        return java.util.Collections.enumeration(this._databaseList);
    }

    /**
     * Method enumerateTransaction.
     * 
     * @return an Enumeration over all possible elements of this
     * collection
     */
    public java.util.Enumeration enumerateTransaction(
    ) {
        return java.util.Collections.enumeration(this._transactionList);
    }

    /**
     * Method getConfiguration.
     * 
     * @param index
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     * @return the value of the
     * org.castor.cpa.test.framework.xml.Configuration at the given
     * index
     */
    public org.castor.cpa.test.framework.xml.Configuration getConfiguration(
            final int index)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._configurationList.size()) {
            throw new IndexOutOfBoundsException("getConfiguration: Index value '" + index + "' not in range [0.." + (this._configurationList.size() - 1) + "]");
        }
        
        return (org.castor.cpa.test.framework.xml.Configuration) _configurationList.get(index);
    }

    /**
     * Method getConfiguration.Returns the contents of the
     * collection in an Array.  <p>Note:  Just in case the
     * collection contents are changing in another thread, we pass
     * a 0-length Array of the correct type into the API call. 
     * This way we <i>know</i> that the Array returned is of
     * exactly the correct length.
     * 
     * @return this collection as an Array
     */
    public org.castor.cpa.test.framework.xml.Configuration[] getConfiguration(
    ) {
        org.castor.cpa.test.framework.xml.Configuration[] array = new org.castor.cpa.test.framework.xml.Configuration[0];
        return (org.castor.cpa.test.framework.xml.Configuration[]) this._configurationList.toArray(array);
    }

    /**
     * Method getConfigurationCount.
     * 
     * @return the size of this collection
     */
    public int getConfigurationCount(
    ) {
        return this._configurationList.size();
    }

    /**
     * Method getDatabase.
     * 
     * @param index
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     * @return the value of the
     * org.castor.cpa.test.framework.xml.Database at the given index
     */
    public org.castor.cpa.test.framework.xml.Database getDatabase(
            final int index)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._databaseList.size()) {
            throw new IndexOutOfBoundsException("getDatabase: Index value '" + index + "' not in range [0.." + (this._databaseList.size() - 1) + "]");
        }
        
        return (org.castor.cpa.test.framework.xml.Database) _databaseList.get(index);
    }

    /**
     * Method getDatabase.Returns the contents of the collection in
     * an Array.  <p>Note:  Just in case the collection contents
     * are changing in another thread, we pass a 0-length Array of
     * the correct type into the API call.  This way we <i>know</i>
     * that the Array returned is of exactly the correct length.
     * 
     * @return this collection as an Array
     */
    public org.castor.cpa.test.framework.xml.Database[] getDatabase(
    ) {
        org.castor.cpa.test.framework.xml.Database[] array = new org.castor.cpa.test.framework.xml.Database[0];
        return (org.castor.cpa.test.framework.xml.Database[]) this._databaseList.toArray(array);
    }

    /**
     * Method getDatabaseCount.
     * 
     * @return the size of this collection
     */
    public int getDatabaseCount(
    ) {
        return this._databaseList.size();
    }

    /**
     * Returns the value of field 'defaultDatabase'.
     * 
     * @return the value of field 'DefaultDatabase'.
     */
    public java.lang.String getDefaultDatabase(
    ) {
        return this._defaultDatabase;
    }

    /**
     * Returns the value of field 'defaultTransaction'.
     * 
     * @return the value of field 'DefaultTransaction'.
     */
    public java.lang.String getDefaultTransaction(
    ) {
        return this._defaultTransaction;
    }

    /**
     * Method getTransaction.
     * 
     * @param index
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     * @return the value of the
     * org.castor.cpa.test.framework.xml.Transaction at the given
     * index
     */
    public org.castor.cpa.test.framework.xml.Transaction getTransaction(
            final int index)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._transactionList.size()) {
            throw new IndexOutOfBoundsException("getTransaction: Index value '" + index + "' not in range [0.." + (this._transactionList.size() - 1) + "]");
        }
        
        return (org.castor.cpa.test.framework.xml.Transaction) _transactionList.get(index);
    }

    /**
     * Method getTransaction.Returns the contents of the collection
     * in an Array.  <p>Note:  Just in case the collection contents
     * are changing in another thread, we pass a 0-length Array of
     * the correct type into the API call.  This way we <i>know</i>
     * that the Array returned is of exactly the correct length.
     * 
     * @return this collection as an Array
     */
    public org.castor.cpa.test.framework.xml.Transaction[] getTransaction(
    ) {
        org.castor.cpa.test.framework.xml.Transaction[] array = new org.castor.cpa.test.framework.xml.Transaction[0];
        return (org.castor.cpa.test.framework.xml.Transaction[]) this._transactionList.toArray(array);
    }

    /**
     * Method getTransactionCount.
     * 
     * @return the size of this collection
     */
    public int getTransactionCount(
    ) {
        return this._transactionList.size();
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
     * Method iterateConfiguration.
     * 
     * @return an Iterator over all possible elements in this
     * collection
     */
    public java.util.Iterator iterateConfiguration(
    ) {
        return this._configurationList.iterator();
    }

    /**
     * Method iterateDatabase.
     * 
     * @return an Iterator over all possible elements in this
     * collection
     */
    public java.util.Iterator iterateDatabase(
    ) {
        return this._databaseList.iterator();
    }

    /**
     * Method iterateTransaction.
     * 
     * @return an Iterator over all possible elements in this
     * collection
     */
    public java.util.Iterator iterateTransaction(
    ) {
        return this._transactionList.iterator();
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
    public void removeAllConfiguration(
    ) {
        this._configurationList.clear();
    }

    /**
     */
    public void removeAllDatabase(
    ) {
        this._databaseList.clear();
    }

    /**
     */
    public void removeAllTransaction(
    ) {
        this._transactionList.clear();
    }

    /**
     * Method removeConfiguration.
     * 
     * @param vConfiguration
     * @return true if the object was removed from the collection.
     */
    public boolean removeConfiguration(
            final org.castor.cpa.test.framework.xml.Configuration vConfiguration) {
        boolean removed = _configurationList.remove(vConfiguration);
        return removed;
    }

    /**
     * Method removeConfigurationAt.
     * 
     * @param index
     * @return the element removed from the collection
     */
    public org.castor.cpa.test.framework.xml.Configuration removeConfigurationAt(
            final int index) {
        java.lang.Object obj = this._configurationList.remove(index);
        return (org.castor.cpa.test.framework.xml.Configuration) obj;
    }

    /**
     * Method removeDatabase.
     * 
     * @param vDatabase
     * @return true if the object was removed from the collection.
     */
    public boolean removeDatabase(
            final org.castor.cpa.test.framework.xml.Database vDatabase) {
        boolean removed = _databaseList.remove(vDatabase);
        return removed;
    }

    /**
     * Method removeDatabaseAt.
     * 
     * @param index
     * @return the element removed from the collection
     */
    public org.castor.cpa.test.framework.xml.Database removeDatabaseAt(
            final int index) {
        java.lang.Object obj = this._databaseList.remove(index);
        return (org.castor.cpa.test.framework.xml.Database) obj;
    }

    /**
     * Method removeTransaction.
     * 
     * @param vTransaction
     * @return true if the object was removed from the collection.
     */
    public boolean removeTransaction(
            final org.castor.cpa.test.framework.xml.Transaction vTransaction) {
        boolean removed = _transactionList.remove(vTransaction);
        return removed;
    }

    /**
     * Method removeTransactionAt.
     * 
     * @param index
     * @return the element removed from the collection
     */
    public org.castor.cpa.test.framework.xml.Transaction removeTransactionAt(
            final int index) {
        java.lang.Object obj = this._transactionList.remove(index);
        return (org.castor.cpa.test.framework.xml.Transaction) obj;
    }

    /**
     * 
     * 
     * @param index
     * @param vConfiguration
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void setConfiguration(
            final int index,
            final org.castor.cpa.test.framework.xml.Configuration vConfiguration)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._configurationList.size()) {
            throw new IndexOutOfBoundsException("setConfiguration: Index value '" + index + "' not in range [0.." + (this._configurationList.size() - 1) + "]");
        }
        
        this._configurationList.set(index, vConfiguration);
    }

    /**
     * 
     * 
     * @param vConfigurationArray
     */
    public void setConfiguration(
            final org.castor.cpa.test.framework.xml.Configuration[] vConfigurationArray) {
        //-- copy array
        _configurationList.clear();
        
        for (int i = 0; i < vConfigurationArray.length; i++) {
                this._configurationList.add(vConfigurationArray[i]);
        }
    }

    /**
     * 
     * 
     * @param index
     * @param vDatabase
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void setDatabase(
            final int index,
            final org.castor.cpa.test.framework.xml.Database vDatabase)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._databaseList.size()) {
            throw new IndexOutOfBoundsException("setDatabase: Index value '" + index + "' not in range [0.." + (this._databaseList.size() - 1) + "]");
        }
        
        this._databaseList.set(index, vDatabase);
    }

    /**
     * 
     * 
     * @param vDatabaseArray
     */
    public void setDatabase(
            final org.castor.cpa.test.framework.xml.Database[] vDatabaseArray) {
        //-- copy array
        _databaseList.clear();
        
        for (int i = 0; i < vDatabaseArray.length; i++) {
                this._databaseList.add(vDatabaseArray[i]);
        }
    }

    /**
     * Sets the value of field 'defaultDatabase'.
     * 
     * @param defaultDatabase the value of field 'defaultDatabase'.
     */
    public void setDefaultDatabase(
            final java.lang.String defaultDatabase) {
        this._defaultDatabase = defaultDatabase;
    }

    /**
     * Sets the value of field 'defaultTransaction'.
     * 
     * @param defaultTransaction the value of field
     * 'defaultTransaction'.
     */
    public void setDefaultTransaction(
            final java.lang.String defaultTransaction) {
        this._defaultTransaction = defaultTransaction;
    }

    /**
     * 
     * 
     * @param index
     * @param vTransaction
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void setTransaction(
            final int index,
            final org.castor.cpa.test.framework.xml.Transaction vTransaction)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._transactionList.size()) {
            throw new IndexOutOfBoundsException("setTransaction: Index value '" + index + "' not in range [0.." + (this._transactionList.size() - 1) + "]");
        }
        
        this._transactionList.set(index, vTransaction);
    }

    /**
     * 
     * 
     * @param vTransactionArray
     */
    public void setTransaction(
            final org.castor.cpa.test.framework.xml.Transaction[] vTransactionArray) {
        //-- copy array
        _transactionList.clear();
        
        for (int i = 0; i < vTransactionArray.length; i++) {
                this._transactionList.add(vTransactionArray[i]);
        }
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
     * org.castor.cpa.test.framework.xml.CpactfConf
     */
    public static org.castor.cpa.test.framework.xml.CpactfConf unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (org.castor.cpa.test.framework.xml.CpactfConf) Unmarshaller.unmarshal(org.castor.cpa.test.framework.xml.CpactfConf.class, reader);
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
