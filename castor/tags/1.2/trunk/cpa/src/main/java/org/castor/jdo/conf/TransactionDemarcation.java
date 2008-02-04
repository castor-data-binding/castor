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
 * Class TransactionDemarcation.
 * 
 * @version $Revision$ $Date$
 */
public class TransactionDemarcation implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _mode
     */
    private java.lang.String _mode;

    /**
     * Field _transactionManager
     */
    private org.castor.jdo.conf.TransactionManager _transactionManager;


      //----------------/
     //- Constructors -/
    //----------------/

    public TransactionDemarcation() 
     {
        super();
    } //-- org.castor.jdo.conf.TransactionDemarcation()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'mode'.
     * 
     * @return String
     * @return the value of field 'mode'.
     */
    public java.lang.String getMode()
    {
        return this._mode;
    } //-- java.lang.String getMode() 

    /**
     * Returns the value of field 'transactionManager'.
     * 
     * @return TransactionManager
     * @return the value of field 'transactionManager'.
     */
    public org.castor.jdo.conf.TransactionManager getTransactionManager()
    {
        return this._transactionManager;
    } //-- org.castor.jdo.conf.TransactionManager getTransactionManager() 

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
     * Sets the value of field 'mode'.
     * 
     * @param mode the value of field 'mode'.
     */
    public void setMode(java.lang.String mode)
    {
        this._mode = mode;
    } //-- void setMode(java.lang.String) 

    /**
     * Sets the value of field 'transactionManager'.
     * 
     * @param transactionManager the value of field
     * 'transactionManager'.
     */
    public void setTransactionManager(org.castor.jdo.conf.TransactionManager transactionManager)
    {
        this._transactionManager = transactionManager;
    } //-- void setTransactionManager(org.castor.jdo.conf.TransactionManager) 

    /**
     * Method unmarshal
     * 
     * 
     * 
     * @param reader
     * @return TransactionDemarcation
     */
    public static org.castor.jdo.conf.TransactionDemarcation unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.castor.jdo.conf.TransactionDemarcation) Unmarshaller.unmarshal(org.castor.jdo.conf.TransactionDemarcation.class, reader);
    } //-- org.castor.jdo.conf.TransactionDemarcation unmarshal(java.io.Reader) 

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
