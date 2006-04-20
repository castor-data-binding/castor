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
public class DataSource implements java.io.Serializable {


      //--------------------/
     //- Member Variables -/
    //--------------------/

    private java.lang.String _className;

    private javax.sql.DataSource _params;


      //----------------/
     //- Constructors -/
    //----------------/

    public DataSource() {
        super();
    } //-- org.exolab.castor.jdo.conf.DataSource()


      //-----------/
     //- Methods -/
    //-----------/

    /**
    **/
    public java.lang.String getClassName() {
        return this._className;
    } //-- java.lang.String getClassName() 

    /**
    **/
    public javax.sql.DataSource getParams() {
        return this._params;
    } //-- javax.sql.DataSource getParams() 

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
     * @param _className
    **/
    public void setClassName(java.lang.String _className) {
        this._className = _className;
    } //-- void setClassName(java.lang.String) 

    /**
     * 
     * @param _params
    **/
    public void setParams(javax.sql.DataSource _params) {
        this._params = _params;
    } //-- void setParams(javax.sql.DataSource) 


    public javax.sql.DataSource createParams()
        throws IllegalStateException
    {
        Object params;

        try {
            params = Class.forName( _className ).newInstance();
        } catch ( Exception except ) {
            throw new IllegalStateException( except.toString() );
        }
        if ( params instanceof javax.sql.DataSource )
            return (javax.sql.DataSource) params;
        else
            throw new IllegalStateException( "Data source class name does not extend javax.sql.DataSource" );
    }

    /**
     * 
     * @param reader
    **/
    public static org.exolab.castor.jdo.conf.DataSource unmarshal(java.io.Reader reader) 
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.exolab.castor.jdo.conf.DataSource) Unmarshaller.unmarshal(org.exolab.castor.jdo.conf.DataSource.class, reader);
    } //-- org.exolab.castor.jdo.conf.DataSource unmarshal(java.io.Reader) 

    /**
    **/
    public void validate() 
        throws org.exolab.castor.xml.ValidationException
    {
        //Validator validator = new Validator();
        //validator.validate(this);
    } //-- void validate() 

}
