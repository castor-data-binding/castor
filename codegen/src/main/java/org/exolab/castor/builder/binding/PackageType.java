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
 *                  Mappings between a namespace and a java package
 * can directly 
 *                  be defined in the binding file. This element
 * allows also the mapping
 *                  between a package and a schemaLocation.
 *             
 * 
 * @version $Revision$ $Date: 2005-03-05 06:42:06 -0700 (Sat, 05 Mar 2005) $
**/
public class PackageType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    private java.lang.String _name;

    private PackageTypeChoice _packageTypeChoice;


      //----------------/
     //- Constructors -/
    //----------------/

    public PackageType() {
        super();
    } //-- org.exolab.castor.builder.binding.PackageType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'name'.
     * 
     * @return the value of field 'name'.
    **/
    public java.lang.String getName() {
        return this._name;
    } //-- java.lang.String getName() 

    /**
     * Returns the value of field 'packageTypeChoice'.
     * 
     * @return the value of field 'packageTypeChoice'.
    **/
    public PackageTypeChoice getPackageTypeChoice() {
        return this._packageTypeChoice;
    } //-- PackageTypeChoice getPackageTypeChoice() 

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
        throws java.io.IOException, org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        
        Marshaller.marshal(this, handler);
    } //-- void marshal(org.xml.sax.ContentHandler) 

    /**
     * Sets the value of field 'name'.
     * 
     * @param name the value of field 'name'.
    **/
    public void setName(final java.lang.String name) {
        this._name = name;
    } //-- void setName(java.lang.String) 

    /**
     * Sets the value of field 'packageTypeChoice'.
     * 
     * @param packageTypeChoice the value of field
     * 'packageTypeChoice'.
    **/
    public void setPackageTypeChoice(final PackageTypeChoice packageTypeChoice) {
        this._packageTypeChoice = packageTypeChoice;
    } //-- void setPackageTypeChoice(PackageTypeChoice) 

    /**
     * 
     * 
     * @param reader
    **/
    public static org.exolab.castor.builder.binding.PackageType unmarshalPackageType(final java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (org.exolab.castor.builder.binding.PackageType) Unmarshaller.unmarshal(org.exolab.castor.builder.binding.PackageType.class, reader);
    } //-- org.exolab.castor.builder.binding.PackageType unmarshalPackageType(java.io.Reader) 

    /**
    **/
    public void validate()
        throws org.exolab.castor.xml.ValidationException {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    } //-- void validate() 

}
