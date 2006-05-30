/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.9.1</a>, using an XML
 * Schema.
 * $Id$
 */

package org.castor.jdo.conf;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

/**
 * Class Mapping.
 * 
 * @version $Revision$ $Date: 2005-11-19 10:17:16 -0700 (Sat, 19 Nov 2005) $
 */
public class Mapping implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _href
     */
    private java.lang.String _href;


      //----------------/
     //- Constructors -/
    //----------------/

    public Mapping() 
     {
        super();
    } //-- org.castor.jdo.conf.Mapping()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'href'.
     * 
     * @return String
     * @return the value of field 'href'.
     */
    public java.lang.String getHref()
    {
        return this._href;
    } //-- java.lang.String getHref() 

    /**
     * Method isValid
     * 
     * 
     * 
     * @return boolean
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
     * Method marshal
     * 
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
     * Sets the value of field 'href'.
     * 
     * @param href the value of field 'href'.
     */
    public void setHref(java.lang.String href)
    {
        this._href = href;
    } //-- void setHref(java.lang.String) 

    /**
     * Method unmarshal
     * 
     * 
     * 
     * @param reader
     * @return Mapping
     */
    public static org.castor.jdo.conf.Mapping unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.castor.jdo.conf.Mapping) Unmarshaller.unmarshal(org.castor.jdo.conf.Mapping.class, reader);
    } //-- org.castor.jdo.conf.Mapping unmarshal(java.io.Reader) 

    /**
     * Method validate
     * 
     */
    public void validate()
        throws org.exolab.castor.xml.ValidationException
    {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    } //-- void validate() 

}
