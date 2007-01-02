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
 * Class ComponentBindingTypeChoice.
 * 
 * @version $Revision$ $Date$
 */
public class ComponentBindingTypeChoice implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _javaClass.
     */
    private org.exolab.castor.builder.binding.ClassType _javaClass;

    /**
     * Field _interface.
     */
    private org.exolab.castor.builder.binding.Interface _interface;

    /**
     * Field _member.
     */
    private org.exolab.castor.builder.binding.FieldType _member;

    /**
     * Field _enumDef.
     */
    private org.exolab.castor.builder.binding.EnumBindingType _enumDef;


      //----------------/
     //- Constructors -/
    //----------------/

    public ComponentBindingTypeChoice() {
        super();
    } //-- org.exolab.castor.builder.binding.ComponentBindingTypeChoice()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'enumDef'.
     * 
     * @return the value of field 'EnumDef'.
     */
    public org.exolab.castor.builder.binding.EnumBindingType getEnumDef() {
        return this._enumDef;
    } //-- org.exolab.castor.builder.binding.EnumBindingType getEnumDef() 

    /**
     * Returns the value of field 'interface'.
     * 
     * @return the value of field 'Interface'.
     */
    public org.exolab.castor.builder.binding.Interface getInterface() {
        return this._interface;
    } //-- org.exolab.castor.builder.binding.Interface getInterface() 

    /**
     * Returns the value of field 'javaClass'.
     * 
     * @return the value of field 'JavaClass'.
     */
    public org.exolab.castor.builder.binding.ClassType getJavaClass() {
        return this._javaClass;
    } //-- org.exolab.castor.builder.binding.ClassType getJavaClass() 

    /**
     * Returns the value of field 'member'.
     * 
     * @return the value of field 'Member'.
     */
    public org.exolab.castor.builder.binding.FieldType getMember() {
        return this._member;
    } //-- org.exolab.castor.builder.binding.FieldType getMember() 

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
     * Sets the value of field 'enumDef'.
     * 
     * @param enumDef the value of field 'enumDef'.
     */
    public void setEnumDef(final org.exolab.castor.builder.binding.EnumBindingType enumDef) {
        this._enumDef = enumDef;
    } //-- void setEnumDef(org.exolab.castor.builder.binding.EnumBindingType) 

    /**
     * Sets the value of field 'interface'.
     * 
     * @param _interface
     * @param interface the value of field 'interface'.
     */
    public void setInterface(final org.exolab.castor.builder.binding.Interface _interface) {
        this._interface = _interface;
    } //-- void setInterface(org.exolab.castor.builder.binding.Interface) 

    /**
     * Sets the value of field 'javaClass'.
     * 
     * @param javaClass the value of field 'javaClass'.
     */
    public void setJavaClass(final org.exolab.castor.builder.binding.ClassType javaClass) {
        this._javaClass = javaClass;
    } //-- void setJavaClass(org.exolab.castor.builder.binding.ClassType) 

    /**
     * Sets the value of field 'member'.
     * 
     * @param member the value of field 'member'.
     */
    public void setMember(final org.exolab.castor.builder.binding.FieldType member) {
        this._member = member;
    } //-- void setMember(org.exolab.castor.builder.binding.FieldType) 

    /**
     * Method unmarshalComponentBindingTypeChoice.
     * 
     * @param reader
     * @throws org.exolab.castor.xml.MarshalException if object is
     * null or if any SAXException is thrown during marshaling
     * @throws org.exolab.castor.xml.ValidationException if this
     * object is an invalid instance according to the schema
     * @return the unmarshaled
     * org.exolab.castor.builder.binding.ComponentBindingTypeChoice
     */
    public static org.exolab.castor.builder.binding.ComponentBindingTypeChoice
            unmarshalComponentBindingTypeChoice(final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (org.exolab.castor.builder.binding.ComponentBindingTypeChoice)
                Unmarshaller.unmarshal(
                        org.exolab.castor.builder.binding.ComponentBindingTypeChoice.class, reader);
    } 

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
