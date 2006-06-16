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
    private org.castor.jdo.conf.Driver _driver;

    /**
     * Field _dataSource
     */
    private org.castor.jdo.conf.DataSource _dataSource;

    /**
     * Field _jndi
     */
    private org.castor.jdo.conf.Jndi _jndi;


      //----------------/
     //- Constructors -/
    //----------------/

    public DatabaseChoice() 
     {
        super();
    } //-- org.castor.jdo.conf.DatabaseChoice()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'dataSource'.
     * 
     * @return DataSource
     * @return the value of field 'dataSource'.
     */
    public org.castor.jdo.conf.DataSource getDataSource()
    {
        return this._dataSource;
    } //-- org.castor.jdo.conf.DataSource getDataSource() 

    /**
     * Returns the value of field 'driver'.
     * 
     * @return Driver
     * @return the value of field 'driver'.
     */
    public org.castor.jdo.conf.Driver getDriver()
    {
        return this._driver;
    } //-- org.castor.jdo.conf.Driver getDriver() 

    /**
     * Returns the value of field 'jndi'.
     * 
     * @return Jndi
     * @return the value of field 'jndi'.
     */
    public org.castor.jdo.conf.Jndi getJndi()
    {
        return this._jndi;
    } //-- org.castor.jdo.conf.Jndi getJndi() 

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
     * Sets the value of field 'dataSource'.
     * 
     * @param dataSource the value of field 'dataSource'.
     */
    public void setDataSource(org.castor.jdo.conf.DataSource dataSource)
    {
        this._dataSource = dataSource;
    } //-- void setDataSource(org.castor.jdo.conf.DataSource) 

    /**
     * Sets the value of field 'driver'.
     * 
     * @param driver the value of field 'driver'.
     */
    public void setDriver(org.castor.jdo.conf.Driver driver)
    {
        this._driver = driver;
    } //-- void setDriver(org.castor.jdo.conf.Driver) 

    /**
     * Sets the value of field 'jndi'.
     * 
     * @param jndi the value of field 'jndi'.
     */
    public void setJndi(org.castor.jdo.conf.Jndi jndi)
    {
        this._jndi = jndi;
    } //-- void setJndi(org.castor.jdo.conf.Jndi) 

    /**
     * Method unmarshal
     * 
     * 
     * 
     * @param reader
     * @return DatabaseChoice
     */
    public static org.castor.jdo.conf.DatabaseChoice unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.castor.jdo.conf.DatabaseChoice) Unmarshaller.unmarshal(org.castor.jdo.conf.DatabaseChoice.class, reader);
    } //-- org.castor.jdo.conf.DatabaseChoice unmarshal(java.io.Reader) 

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
