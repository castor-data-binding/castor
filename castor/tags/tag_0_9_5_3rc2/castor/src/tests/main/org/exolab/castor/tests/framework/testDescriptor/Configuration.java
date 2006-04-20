/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.5.2</a>, using an XML
 * Schema.
 * $Id$
 */

package org.exolab.castor.tests.framework.testDescriptor;

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
 * Class Configuration.
 * 
 * @version $Revision$ $Date$
 */
public class Configuration implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _marshal
     */
    private org.exolab.castor.tests.framework.testDescriptor.ConfigurationType _marshal;

    /**
     * Field _unmarshal
     */
    private org.exolab.castor.tests.framework.testDescriptor.ConfigurationType _unmarshal;


      //----------------/
     //- Constructors -/
    //----------------/

    public Configuration() {
        super();
    } //-- org.exolab.castor.tests.framework.testDescriptor.Configuration()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'marshal'.
     * 
     * @return the value of field 'marshal'.
     */
    public org.exolab.castor.tests.framework.testDescriptor.ConfigurationType getMarshal()
    {
        return this._marshal;
    } //-- org.exolab.castor.tests.framework.testDescriptor.ConfigurationType getMarshal() 

    /**
     * Returns the value of field 'unmarshal'.
     * 
     * @return the value of field 'unmarshal'.
     */
    public org.exolab.castor.tests.framework.testDescriptor.ConfigurationType getUnmarshal()
    {
        return this._unmarshal;
    } //-- org.exolab.castor.tests.framework.testDescriptor.ConfigurationType getUnmarshal() 

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
     * Sets the value of field 'marshal'.
     * 
     * @param marshal the value of field 'marshal'.
     */
    public void setMarshal(org.exolab.castor.tests.framework.testDescriptor.ConfigurationType marshal)
    {
        this._marshal = marshal;
    } //-- void setMarshal(org.exolab.castor.tests.framework.testDescriptor.ConfigurationType) 

    /**
     * Sets the value of field 'unmarshal'.
     * 
     * @param unmarshal the value of field 'unmarshal'.
     */
    public void setUnmarshal(org.exolab.castor.tests.framework.testDescriptor.ConfigurationType unmarshal)
    {
        this._unmarshal = unmarshal;
    } //-- void setUnmarshal(org.exolab.castor.tests.framework.testDescriptor.ConfigurationType) 

    /**
     * Method unmarshalConfiguration
     * 
     * @param reader
     */
    public static java.lang.Object unmarshalConfiguration(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.exolab.castor.tests.framework.testDescriptor.Configuration) Unmarshaller.unmarshal(org.exolab.castor.tests.framework.testDescriptor.Configuration.class, reader);
    } //-- java.lang.Object unmarshalConfiguration(java.io.Reader) 

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
