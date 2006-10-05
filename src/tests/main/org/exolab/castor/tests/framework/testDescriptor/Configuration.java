/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.0.3</a>, using an XML
 * Schema.
 * $Id$
 */

package org.exolab.castor.tests.framework.testDescriptor;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

/**
 * 
 * 
 * @version $Revision$ $Date$
 */
public class Configuration implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Internal choice value storage
     */
    private java.lang.Object _choiceValue;

    /**
     * Field _marshal
     */
    private org.exolab.castor.tests.framework.testDescriptor.Marshal _marshal;

    /**
     * Field _unmarshal
     */
    private org.exolab.castor.tests.framework.testDescriptor.Unmarshal _unmarshal;


      //----------------/
     //- Constructors -/
    //----------------/

    public Configuration() 
     {
        super();
    } //-- org.exolab.castor.tests.framework.testDescriptor.Configuration()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'choiceValue'. The field
     * 'choiceValue' has the following description: Internal choice
     * value storage
     * 
     * @return the value of field 'ChoiceValue'.
     */
    public java.lang.Object getChoiceValue()
    {
        return this._choiceValue;
    } //-- java.lang.Object getChoiceValue() 

    /**
     * Returns the value of field 'marshal'.
     * 
     * @return the value of field 'Marshal'.
     */
    public org.exolab.castor.tests.framework.testDescriptor.Marshal getMarshal()
    {
        return this._marshal;
    } //-- org.exolab.castor.tests.framework.testDescriptor.Marshal getMarshal() 

    /**
     * Returns the value of field 'unmarshal'.
     * 
     * @return the value of field 'Unmarshal'.
     */
    public org.exolab.castor.tests.framework.testDescriptor.Unmarshal getUnmarshal()
    {
        return this._unmarshal;
    } //-- org.exolab.castor.tests.framework.testDescriptor.Unmarshal getUnmarshal() 

    /**
     * Method isValid
     * 
     * 
     * 
     * @return true if this object is valid according to the schema
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
     * Sets the value of field 'marshal'.
     * 
     * @param marshal the value of field 'marshal'.
     */
    public void setMarshal(org.exolab.castor.tests.framework.testDescriptor.Marshal marshal)
    {
        this._marshal = marshal;
        this._choiceValue = marshal;
    } //-- void setMarshal(org.exolab.castor.tests.framework.testDescriptor.Marshal) 

    /**
     * Sets the value of field 'unmarshal'.
     * 
     * @param unmarshal the value of field 'unmarshal'.
     */
    public void setUnmarshal(org.exolab.castor.tests.framework.testDescriptor.Unmarshal unmarshal)
    {
        this._unmarshal = unmarshal;
        this._choiceValue = unmarshal;
    } //-- void setUnmarshal(org.exolab.castor.tests.framework.testDescriptor.Unmarshal) 

    /**
     * Method unmarshal
     * 
     * 
     * 
     * @param reader
     * @return the unmarshaled
     * org.exolab.castor.tests.framework.testDescriptor.Configuratio
     */
    public static org.exolab.castor.tests.framework.testDescriptor.Configuration unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.exolab.castor.tests.framework.testDescriptor.Configuration) Unmarshaller.unmarshal(org.exolab.castor.tests.framework.testDescriptor.Configuration.class, reader);
    } //-- org.exolab.castor.tests.framework.testDescriptor.Configuration unmarshal(java.io.Reader) 

    /**
     */
    public void validate()
        throws org.exolab.castor.xml.ValidationException
    {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    } //-- void validate() 

}
