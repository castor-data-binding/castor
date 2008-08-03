/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.1-M2</a>, using an XML
 * Schema.
 * $Id: Configuration.java 6721 2007-01-05 04:33:29Z ekuns $
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
 * @version $Revision: 6721 $ $Date$
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
     * Field _marshal.
     */
    private org.exolab.castor.tests.framework.testDescriptor.Marshal _marshal;

    /**
     * Field _unmarshal.
     */
    private org.exolab.castor.tests.framework.testDescriptor.Unmarshal _unmarshal;


      //----------------/
     //- Constructors -/
    //----------------/

    public Configuration() {
        super();
    }


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
    public java.lang.Object getChoiceValue(
    ) {
        return this._choiceValue;
    }

    /**
     * Returns the value of field 'marshal'.
     * 
     * @return the value of field 'Marshal'.
     */
    public org.exolab.castor.tests.framework.testDescriptor.Marshal getMarshal(
    ) {
        return this._marshal;
    }

    /**
     * Returns the value of field 'unmarshal'.
     * 
     * @return the value of field 'Unmarshal'.
     */
    public org.exolab.castor.tests.framework.testDescriptor.Unmarshal getUnmarshal(
    ) {
        return this._unmarshal;
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
     * Sets the value of field 'marshal'.
     * 
     * @param marshal the value of field 'marshal'.
     */
    public void setMarshal(
            final org.exolab.castor.tests.framework.testDescriptor.Marshal marshal) {
        this._marshal = marshal;
        this._choiceValue = marshal;
    }

    /**
     * Sets the value of field 'unmarshal'.
     * 
     * @param unmarshal the value of field 'unmarshal'.
     */
    public void setUnmarshal(
            final org.exolab.castor.tests.framework.testDescriptor.Unmarshal unmarshal) {
        this._unmarshal = unmarshal;
        this._choiceValue = unmarshal;
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
     * org.exolab.castor.tests.framework.testDescriptor.Configuratio
     */
    public static org.exolab.castor.tests.framework.testDescriptor.Configuration unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (org.exolab.castor.tests.framework.testDescriptor.Configuration) Unmarshaller.unmarshal(org.exolab.castor.tests.framework.testDescriptor.Configuration.class, reader);
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
