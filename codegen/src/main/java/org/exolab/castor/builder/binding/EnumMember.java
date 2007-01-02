/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.0.5</a>, using an XML
 * Schema.
 * $Id$
 */

package org.exolab.castor.builder.binding;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

/**
 * Class EnumMember.
 * 
 * @version $Revision$ $Date$
 */
public class EnumMember implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _value.
     */
    private java.lang.String _value;

    /**
     * Field _javaName.
     */
    private java.lang.String _javaName;


      //----------------/
     //- Constructors -/
    //----------------/

    public EnumMember() {
        super();
    } //-- org.exolab.castor.builder.binding.EnumMember()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'javaName'.
     * 
     * @return the value of field 'JavaName'.
     */
    public java.lang.String getJavaName() {
        return this._javaName;
    } //-- java.lang.String getJavaName() 

    /**
     * Returns the value of field 'value'.
     * 
     * @return the value of field 'Value'.
     */
    public java.lang.String getValue() {
        return this._value;
    } //-- java.lang.String getValue() 

    /**
     * Method isValid.
     * 
     * @return true if this object is valid according to the schema
     */
    public boolean isValid() {
        try {
            validate();
        } catch (org.exolab.castor.xml.ValidationException vex) {
            return false;
        }
        return true;
    } //-- boolean isValid() 

    /**
     * 
     * 
     * @param out
     * @throws org.exolab.castor.xml.MarshalException if object is
     * null or if any SAXException is thrown during marshaling
     * @throws org.exolab.castor.xml.ValidationException if this
     * object is an invalid instance according to the schema
     */
    public void marshal(final java.io.Writer out)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        Marshaller.marshal(this, out);
    } //-- void marshal(java.io.Writer) 

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
    public void marshal(final org.xml.sax.ContentHandler handler)
    throws java.io.IOException, org.exolab.castor.xml.MarshalException,
           org.exolab.castor.xml.ValidationException {
        Marshaller.marshal(this, handler);
    } //-- void marshal(org.xml.sax.ContentHandler) 

    /**
     * Sets the value of field 'javaName'.
     * 
     * @param javaName the value of field 'javaName'.
     */
    public void setJavaName(final java.lang.String javaName) {
        this._javaName = javaName;
    } //-- void setJavaName(java.lang.String) 

    /**
     * Sets the value of field 'value'.
     * 
     * @param value the value of field 'value'.
     */
    public void setValue(final java.lang.String value) {
        this._value = value;
    } //-- void setValue(java.lang.String) 

    /**
     * Method unmarshalEnumMember.
     * 
     * @param reader
     * @throws org.exolab.castor.xml.MarshalException if object is
     * null or if any SAXException is thrown during marshaling
     * @throws org.exolab.castor.xml.ValidationException if this
     * object is an invalid instance according to the schema
     * @return the unmarshaled
     * org.exolab.castor.builder.binding.EnumMember
     */
    public static org.exolab.castor.builder.binding.EnumMember unmarshalEnumMember(
            final java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (org.exolab.castor.builder.binding.EnumMember) Unmarshaller.unmarshal(
                org.exolab.castor.builder.binding.EnumMember.class, reader);
    } //-- org.exolab.castor.builder.binding.EnumMember unmarshalEnumMember(java.io.Reader) 

    /**
     * 
     * 
     * @throws org.exolab.castor.xml.ValidationException if this
     * object is an invalid instance according to the schema
     */
    public void validate()
        throws org.exolab.castor.xml.ValidationException {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    } //-- void validate() 

}
