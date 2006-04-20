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
 * Class JdoConf.
 * 
 * @version $Revision$ $Date$
 */
public class JdoConf implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _database
     */
    private org.exolab.castor.jdo.conf.Database _database;

    /**
     * Field _transactionDemarcation
     */
    private org.exolab.castor.jdo.conf.TransactionDemarcation _transactionDemarcation;


      //----------------/
     //- Constructors -/
    //----------------/

    public JdoConf() {
        super();
    } //-- org.exolab.castor.jdo.conf.JdoConf()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'database'.
     * 
     * @return the value of field 'database'.
     */
    public org.exolab.castor.jdo.conf.Database getDatabase()
    {
        return this._database;
    } //-- org.exolab.castor.jdo.conf.Database getDatabase() 

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
     * Sets the value of field 'database'.
     * 
     * @param database the value of field 'database'.
     */
    public void setDatabase(org.exolab.castor.jdo.conf.Database database)
    {
        this._database = database;
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
