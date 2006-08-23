/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.0.2</a>, using an XML
 * Schema.
 * $Id$
 */

package org.exolab.castor.mapping.xml;

import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

/**
 * Class Include.
 * 
 * @version $Revision$ $Date: 2006-01-07 15:48:31 -0700 (Sat, 07 Jan 2006) $
 */
public class Include implements java.io.Serializable {


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

    public Include() 
     {
        super();
    } //-- org.exolab.castor.mapping.xml.Include()


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
     * @return Include
     */
    public static org.exolab.castor.mapping.xml.Include unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.exolab.castor.mapping.xml.Include) Unmarshaller.unmarshal(org.exolab.castor.mapping.xml.Include.class, reader);
    } //-- org.exolab.castor.mapping.xml.Include unmarshal(java.io.Reader) 

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
