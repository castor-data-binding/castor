/*
 * This class was automatically generated with 
 * <a href="http://castor.exolab.org">Castor 0.9.4</a>, using an
 * XML Schema.
 * $Id$
 */

package org.exolab.castor.builder.binding;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import org.exolab.castor.xml.*;

/**
 * 
 * 
 * @version $Revision$ $Date: 2005-03-05 06:42:06 -0700 (Sat, 05 Mar 2005) $
**/
public class ComponentBindingTypeChoice implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    private ClassType _javaClass;

    private Interface _interface;

    private FieldType _member;


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
     * Returns the value of field 'interface'.
     * 
     * @return the value of field 'interface'.
    **/
    public Interface getInterface()
    {
        return this._interface;
    } //-- Interface getInterface() 

    /**
     * Returns the value of field 'javaClass'.
     * 
     * @return the value of field 'javaClass'.
    **/
    public ClassType getJavaClass()
    {
        return this._javaClass;
    } //-- ClassType getJavaClass() 

    /**
     * Returns the value of field 'member'.
     * 
     * @return the value of field 'member'.
    **/
    public FieldType getMember()
    {
        return this._member;
    } //-- FieldType getMember() 

    /**
    **/
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
    **/
    public void marshal(java.io.Writer out)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        
        Marshaller.marshal(this, out);
    } //-- void marshal(java.io.Writer) 

    /**
     * 
     * 
     * @param handler
    **/
    public void marshal(org.xml.sax.ContentHandler handler)
        throws java.io.IOException, org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        
        Marshaller.marshal(this, handler);
    } //-- void marshal(org.xml.sax.ContentHandler) 

    /**
     * Sets the value of field 'interface'.
     * 
     * @param _interface the value of field 'interface'.
    **/
    public void setInterface(Interface _interface)
    {
        this._interface = _interface;
    } //-- void setInterface(Interface) 

    /**
     * Sets the value of field 'javaClass'.
     * 
     * @param javaClass the value of field 'javaClass'.
    **/
    public void setJavaClass(ClassType javaClass)
    {
        this._javaClass = javaClass;
    } //-- void setJavaClass(ClassType) 

    /**
     * Sets the value of field 'member'.
     * 
     * @param member the value of field 'member'.
    **/
    public void setMember(FieldType member)
    {
        this._member = member;
    } //-- void setMember(FieldType) 

    /**
     * 
     * 
     * @param reader
    **/
    public static org.exolab.castor.builder.binding.ComponentBindingTypeChoice unmarshalComponentBindingTypeChoice(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.exolab.castor.builder.binding.ComponentBindingTypeChoice) Unmarshaller.unmarshal(org.exolab.castor.builder.binding.ComponentBindingTypeChoice.class, reader);
    } //-- org.exolab.castor.builder.binding.ComponentBindingTypeChoice unmarshalComponentBindingTypeChoice(java.io.Reader) 

    /**
    **/
    public void validate()
        throws org.exolab.castor.xml.ValidationException
    {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    } //-- void validate() 

}
