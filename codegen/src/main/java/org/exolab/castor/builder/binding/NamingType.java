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
public class NamingType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    private java.lang.String _prefix;

    private java.lang.String _suffix;


      //----------------/
     //- Constructors -/
    //----------------/

    public NamingType() {
        super();
    } //-- org.exolab.castor.builder.binding.NamingType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'prefix'.
     * 
     * @return the value of field 'prefix'.
    **/
    public java.lang.String getPrefix() {
        return this._prefix;
    } //-- java.lang.String getPrefix() 

    /**
     * Returns the value of field 'suffix'.
     * 
     * @return the value of field 'suffix'.
    **/
    public java.lang.String getSuffix() {
        return this._suffix;
    } //-- java.lang.String getSuffix() 

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
     * Sets the value of field 'prefix'.
     * 
     * @param prefix the value of field 'prefix'.
    **/
    public void setPrefix(final java.lang.String prefix) {
        this._prefix = prefix;
    } //-- void setPrefix(java.lang.String) 

    /**
     * Sets the value of field 'suffix'.
     * 
     * @param suffix the value of field 'suffix'.
    **/
    public void setSuffix(final java.lang.String suffix) {
        this._suffix = suffix;
    } //-- void setSuffix(java.lang.String) 

    /**
     * 
     * 
     * @param reader
    **/
    public static org.exolab.castor.builder.binding.NamingType unmarshalNamingType(final java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (org.exolab.castor.builder.binding.NamingType) Unmarshaller.unmarshal(org.exolab.castor.builder.binding.NamingType.class, reader);
    } //-- org.exolab.castor.builder.binding.NamingType unmarshalNamingType(java.io.Reader) 

    /**
    **/
    public void validate()
        throws org.exolab.castor.xml.ValidationException {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    } //-- void validate() 

}
