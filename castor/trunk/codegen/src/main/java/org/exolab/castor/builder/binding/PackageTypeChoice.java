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
public final class PackageTypeChoice implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    private java.lang.String _namespace;

    private java.lang.String _schemaLocation;


      //----------------/
     //- Constructors -/
    //----------------/

    public PackageTypeChoice() {
        super();
    } //-- org.exolab.castor.builder.binding.PackageTypeChoice()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'namespace'.
     * 
     * @return the value of field 'namespace'.
    **/
    public java.lang.String getNamespace() {
        return this._namespace;
    } //-- java.lang.String getNamespace() 

    /**
     * Returns the value of field 'schemaLocation'.
     * 
     * @return the value of field 'schemaLocation'.
    **/
    public java.lang.String getSchemaLocation() {
        return this._schemaLocation;
    } //-- java.lang.String getSchemaLocation() 

    /**
    **/
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
    **/
    public void marshal(final java.io.Writer out)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        Marshaller.marshal(this, out);
    } //-- void marshal(java.io.Writer) 

    /**
     * 
     * 
     * @param handler
    **/
    public void marshal(final org.xml.sax.ContentHandler handler)
    throws java.io.IOException, org.exolab.castor.xml.MarshalException,
           org.exolab.castor.xml.ValidationException {
        Marshaller.marshal(this, handler);
    } //-- void marshal(org.xml.sax.ContentHandler) 

    /**
     * Sets the value of field 'namespace'.
     * 
     * @param namespace the value of field 'namespace'.
    **/
    public void setNamespace(final java.lang.String namespace) {
        this._namespace = namespace;
    } //-- void setNamespace(java.lang.String) 

    /**
     * Sets the value of field 'schemaLocation'.
     * 
     * @param schemaLocation the value of field 'schemaLocation'.
    **/
    public void setSchemaLocation(final java.lang.String schemaLocation) {
        this._schemaLocation = schemaLocation;
    } //-- void setSchemaLocation(java.lang.String) 

    /**
     * 
     * 
     * @param reader
    **/
    public static org.exolab.castor.builder.binding.PackageTypeChoice unmarshalPackageTypeChoice(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (org.exolab.castor.builder.binding.PackageTypeChoice) Unmarshaller.unmarshal(
                org.exolab.castor.builder.binding.PackageTypeChoice.class, reader);
    } 

    /**
    **/
    public void validate()
        throws org.exolab.castor.xml.ValidationException {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    } //-- void validate() 

}
