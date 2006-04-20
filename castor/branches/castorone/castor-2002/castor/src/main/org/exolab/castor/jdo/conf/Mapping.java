/*
 * This class was automatically generated with 
 * <a href="http://castor.exolab.org">Castor 0.8.3 (2000502)</a>,
 * using an XML Schema.
 * $Id
 */

package org.exolab.castor.jdo.conf;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import org.exolab.castor.xml.*;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.DocumentHandler;

/**
 * 
 * @version $Revision$ $Date$
**/
public class Mapping implements java.io.Serializable {


      //--------------------/
     //- Member Variables -/
    //--------------------/

    private java.lang.String _href;


      //----------------/
     //- Constructors -/
    //----------------/

    public Mapping() {
        super();
    } //-- org.exolab.castor.jdo.conf.Mapping()


      //-----------/
     //- Methods -/
    //-----------/

    /**
    **/
    public java.lang.String getHref() {
        return this._href;
    } //-- java.lang.String getHref() 

    /**
    **/
    public boolean isValid() {
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
     * @param out
    **/
    public void marshal(java.io.Writer out) 
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        //-- we must have a valid element before marshalling
        //validate(false);
        
        Marshaller.marshal(this, out);
    } //-- void marshal(java.io.Writer) 

    /**
     * 
     * @param handler
    **/
    public void marshal(org.xml.sax.DocumentHandler handler) 
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        //-- we must have a valid element before marshalling
        //validate(false);
        
        Marshaller.marshal(this, handler);
    } //-- void marshal(org.xml.sax.DocumentHandler) 

    /**
     * 
     * @param _href
    **/
    public void setHref(java.lang.String _href) {
        this._href = _href;
    } //-- void setHref(java.lang.String) 

    /**
     * 
     * @param reader
    **/
    public static org.exolab.castor.jdo.conf.Mapping unmarshal(java.io.Reader reader) 
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.exolab.castor.jdo.conf.Mapping) Unmarshaller.unmarshal(org.exolab.castor.jdo.conf.Mapping.class, reader);
    } //-- org.exolab.castor.jdo.conf.Mapping unmarshal(java.io.Reader) 

    /**
    **/
    public void validate() 
        throws org.exolab.castor.xml.ValidationException
    {
        org.exolab.castor.xml.Validator.validate(this, null);
    } //-- void validate() 

}
