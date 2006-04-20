/*
 * This class was automatically generated with 
 * <a href="http://castor.exolab.org">Castor 0.8.7</a>, using an
 * XML Schema.
 * $Id
 */

package org.exolab.castor.mapping.xml;

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
public class TypeMapping implements java.io.Serializable {


      //--------------------/
     //- Member Variables -/
    //--------------------/

    private java.lang.Object _type;

    private java.lang.String _sql;


      //----------------/
     //- Constructors -/
    //----------------/

    public TypeMapping() {
        super();
    } //-- org.exolab.castor.mapping.xml.TypeMapping()


      //-----------/
     //- Methods -/
    //-----------/

    /**
    **/
    public java.lang.String getReferenceId() {
        return this._sql;
    } //-- java.lang.String getReferenceId() 

    /**
    **/
    public java.lang.String getSql() {
        return this._sql;
    } //-- java.lang.String getSql() 

    /**
    **/
    public java.lang.Object getType() {
        return this._type;
    } //-- java.lang.Object getType() 

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
     * @param _sql
    **/
    public void setSql(java.lang.String _sql) {
        this._sql = _sql;
    } //-- void setSql(java.lang.String) 

    /**
     * 
     * @param _type
    **/
    public void setType(java.lang.Object _type) {
        this._type = _type;
    } //-- void setType(java.lang.Object) 

    /**
     * 
     * @param reader
    **/
    public static org.exolab.castor.mapping.xml.TypeMapping unmarshal(java.io.Reader reader) 
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.exolab.castor.mapping.xml.TypeMapping) Unmarshaller.unmarshal(org.exolab.castor.mapping.xml.TypeMapping.class, reader);
    } //-- org.exolab.castor.mapping.xml.TypeMapping unmarshal(java.io.Reader) 

    /**
    **/
    public void validate() 
        throws org.exolab.castor.xml.ValidationException
    {
        org.exolab.castor.xml.Validator.validate(this, null);
    } //-- void validate() 

}
