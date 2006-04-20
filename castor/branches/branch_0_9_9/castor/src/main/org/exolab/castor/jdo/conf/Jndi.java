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

import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.Validator;

/**
 * 
 * @version $Revision$ $Date$
**/
public class Jndi implements java.io.Serializable {


      //--------------------/
     //- Member Variables -/
    //--------------------/

    private java.lang.String _name;


      //----------------/
     //- Constructors -/
    //----------------/

    public Jndi() {
        super();
    } //-- org.exolab.castor.jdo.conf.Jndi()


      //-----------/
     //- Methods -/
    //-----------/

    /**
    **/
    public java.lang.String getName() {
        return this._name;
    } //-- java.lang.String getName() 

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
     * @param _name
    **/
    public void setName(java.lang.String _name) {
        this._name = _name;
    } //-- void setName(java.lang.String) 

    /**
     * 
     * @param reader
    **/
    public static org.exolab.castor.jdo.conf.Jndi unmarshal(java.io.Reader reader) 
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.exolab.castor.jdo.conf.Jndi) Unmarshaller.unmarshal(org.exolab.castor.jdo.conf.Jndi.class, reader);
    } //-- org.exolab.castor.jdo.conf.Jndi unmarshal(java.io.Reader) 

    /**
    **/
    public void validate() 
        throws org.exolab.castor.xml.ValidationException
    {
        Validator validator = new Validator();
        validator.validate(this);
    } //-- void validate() 

}
