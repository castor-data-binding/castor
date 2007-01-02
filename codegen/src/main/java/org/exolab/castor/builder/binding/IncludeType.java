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
 *                  A binding file can include other binding files
 * by specifying the location  
 *                  (URI) of the binding files to include.
 *             
 * 
 * @version $Revision$ $Date: 2005-03-05 06:42:06 -0700 (Sat, 05 Mar 2005) $
**/
public class IncludeType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    private java.lang.String _URI;


      //----------------/
     //- Constructors -/
    //----------------/

    public IncludeType() {
        super();
    } //-- org.exolab.castor.builder.binding.IncludeType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'URI'.
     * 
     * @return the value of field 'URI'.
    **/
    public java.lang.String getURI() {
        return this._URI;
    } //-- java.lang.String getURI() 

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
     * Sets the value of field 'URI'.
     * 
     * @param URI the value of field 'URI'.
    **/
    public void setURI(final java.lang.String URI) {
        this._URI = URI;
    } //-- void setURI(java.lang.String) 

    /**
     * 
     * 
     * @param reader
    **/
    public static org.exolab.castor.builder.binding.IncludeType unmarshalIncludeType(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (org.exolab.castor.builder.binding.IncludeType) Unmarshaller.unmarshal(
                org.exolab.castor.builder.binding.IncludeType.class, reader);
    } //-- org.exolab.castor.builder.binding.IncludeType unmarshalIncludeType(java.io.Reader) 

    /**
    **/
    public void validate()
        throws org.exolab.castor.xml.ValidationException {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    } //-- void validate() 

}
