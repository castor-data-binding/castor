/*
 * This class was automatically generated with 
 * <a href="http://castor.exolab.org">Castor 0.8.3 (2000502)</a>,
 * using an XML Schema.
 * $Id
 */

package org.exolab.castor.jdo.conf;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import java.util.Enumeration;
import java.util.Vector;
import org.exolab.castor.xml.*;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.DocumentHandler;

/**
 * 
 * @version $Revision$ $Date$
**/
public class Database implements java.io.Serializable {


      //--------------------/
     //- Member Variables -/
    //--------------------/

    private java.lang.String _engine = "generic";

    private java.lang.String _name;

    private Driver _driver;

    private DataSource _dataSource;

    private Jndi _jndi;

    private java.util.Vector _mappingList;


      //----------------/
     //- Constructors -/
    //----------------/

    public Database() {
        super();
        _mappingList = new Vector();
    } //-- org.exolab.castor.jdo.conf.Database()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * 
     * @param vMapping
    **/
    public void addMapping(Mapping vMapping) 
        throws java.lang.IndexOutOfBoundsException
    {
        _mappingList.addElement(vMapping);
    } //-- void addMapping(Mapping) 

    /**
    **/
    public java.util.Enumeration enumerateMapping() {
        return _mappingList.elements();
    } //-- java.util.Enumeration enumerateMapping() 

    /**
    **/
    public DataSource getDataSource() {
        return this._dataSource;
    } //-- DataSource getDataSource() 

    /**
    **/
    public Driver getDriver() {
        return this._driver;
    } //-- Driver getDriver() 

    /**
    **/
    public java.lang.String getEngine() {
        return this._engine;
    } //-- java.lang.String getEngine() 

    /**
    **/
    public Jndi getJndi() {
        return this._jndi;
    } //-- Jndi getJndi() 

    /**
     * 
     * @param index
    **/
    public Mapping getMapping(int index) 
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _mappingList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (Mapping) _mappingList.elementAt(index);
    } //-- Mapping getMapping(int) 

    /**
    **/
    public Mapping[] getMapping() {
        int size = _mappingList.size();
        Mapping[] mArray = new Mapping[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (Mapping) _mappingList.elementAt(index);
        }
        return mArray;
    } //-- Mapping[] getMapping() 

    /**
    **/
    public int getMappingCount() {
        return _mappingList.size();
    } //-- int getMappingCount() 

    /**
    **/
    public java.lang.String getName() {
        return this._name;
    } //-- java.lang.String getName() 

    /**
    **/
    public java.lang.String getReferenceId() {
        return this._name;
    } //-- java.lang.String getReferenceId() 

    /**
    **/
    public boolean isValid() {
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
        //-- we must have a valid element before marshalling
        //validate(false);
        
        Marshaller.marshal(this, out);
    } //-- void marshal(java.io.Writer) 

    /**
     * 
     * @param handler
    **/
    public void marshal(org.xml.sax.DocumentHandler handler) 
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        //-- we must have a valid element before marshalling
        //validate(false);
        
        Marshaller.marshal(this, handler);
    } //-- void marshal(org.xml.sax.DocumentHandler) 

    /**
    **/
    public void removeAllMapping() {
        _mappingList.removeAllElements();
    } //-- void removeAllMapping() 

    /**
     * 
     * @param index
    **/
    public Mapping removeMapping(int index) {
        Object obj = _mappingList.elementAt(index);
        _mappingList.removeElementAt(index);
        return (Mapping) obj;
    } //-- Mapping removeMapping(int) 

    /**
     * 
     * @param _dataSource
    **/
    public void setDataSource(DataSource _dataSource) {
        this._dataSource = _dataSource;
    } //-- void setDataSource(DataSource) 

    /**
     * 
     * @param _driver
    **/
    public void setDriver(Driver _driver) {
        this._driver = _driver;
    } //-- void setDriver(Driver) 

    /**
     * 
     * @param _engine
    **/
    public void setEngine(java.lang.String _engine) {
        this._engine = _engine;
    } //-- void setEngine(java.lang.String) 

    /**
     * 
     * @param _jndi
    **/
    public void setJndi(Jndi _jndi) {
        this._jndi = _jndi;
    } //-- void setJndi(Jndi) 

    /**
     * 
     * @param vMapping
     * @param index
    **/
    public void setMapping(Mapping vMapping, int index) 
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _mappingList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _mappingList.setElementAt(vMapping, index);
    } //-- void setMapping(Mapping, int) 

    /**
     * 
     * @param _name
    **/
    public void setName(java.lang.String _name) {
        this._name = _name;
    } //-- void setName(java.lang.String) 

    /**
     * 
     * @param reader
    **/
    public static org.exolab.castor.jdo.conf.Database unmarshal(java.io.Reader reader) 
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.exolab.castor.jdo.conf.Database) Unmarshaller.unmarshal(org.exolab.castor.jdo.conf.Database.class, reader);
    } //-- org.exolab.castor.jdo.conf.Database unmarshal(java.io.Reader) 

    /**
    **/
    public void validate() 
        throws org.exolab.castor.xml.ValidationException
    {
        org.exolab.castor.xml.Validator.validate(this, null);
    } //-- void validate() 

}
