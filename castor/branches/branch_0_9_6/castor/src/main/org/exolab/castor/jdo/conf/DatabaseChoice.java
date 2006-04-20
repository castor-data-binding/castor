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
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.ContentHandler;

/**
 * Class DatabaseChoice.
 * 
 * @version $Revision$ $Date$
 */
public class DatabaseChoice implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _driver
     */
    private org.exolab.castor.jdo.conf.Driver _driver;

    /**
     * Field _dataSource
     */
    private org.exolab.castor.jdo.conf.DataSource _dataSource;

    /**
     * Field _jndi
     */
    private org.exolab.castor.jdo.conf.Jndi _jndi;


      //----------------/
     //- Constructors -/
    //----------------/

    public DatabaseChoice() {
        super();
    } //-- org.exolab.castor.jdo.conf.DatabaseChoice()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'dataSource'.
     * 
     * @return the value of field 'dataSource'.
     */
    public org.exolab.castor.jdo.conf.DataSource getDataSource()
    {
        return this._dataSource;
    } //-- org.exolab.castor.jdo.conf.DataSource getDataSource() 

    /**
     * Returns the value of field 'driver'.
     * 
     * @return the value of field 'driver'.
     */
    public org.exolab.castor.jdo.conf.Driver getDriver()
    {
        return this._driver;
    } //-- org.exolab.castor.jdo.conf.Driver getDriver() 

    /**
     * Returns the value of field 'jndi'.
     * 
     * @return the value of field 'jndi'.
     */
    public org.exolab.castor.jdo.conf.Jndi getJndi()
    {
        return this._jndi;
    } //-- org.exolab.castor.jdo.conf.Jndi getJndi() 

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
     * Sets the value of field 'dataSource'.
     * 
     * @param dataSource the value of field 'dataSource'.
     */
    public void setDataSource(org.exolab.castor.jdo.conf.DataSource dataSource)
    {
        this._dataSource = dataSource;
    } //-- void setDataSource(org.exolab.castor.jdo.conf.DataSource) 

    /**
     * Sets the value of field 'driver'.
     * 
     * @param driver the value of field 'driver'.
     */
    public void setDriver(org.exolab.castor.jdo.conf.Driver driver)
    {
        this._driver = driver;
    } //-- void setDriver(org.exolab.castor.jdo.conf.Driver) 

    /**
     * Sets the value of field 'jndi'.
     * 
     * @param jndi the value of field 'jndi'.
     */
    public void setJndi(org.exolab.castor.jdo.conf.Jndi jndi)
    {
        this._jndi = jndi;
    } //-- void setJndi(org.exolab.castor.jdo.conf.Jndi) 

    /**
     * Method unmarshal
     * 
     * @param reader
     */
    public static java.lang.Object unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.exolab.castor.jdo.conf.DatabaseChoice) Unmarshaller.unmarshal(org.exolab.castor.jdo.conf.DatabaseChoice.class, reader);
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
