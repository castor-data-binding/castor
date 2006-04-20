/*
 * This class was automatically generated with 
 * <a href="http://castor.exolab.org">Castor 0.8.3 (2000502)</a>,
 * using an XML Schema.
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
public class Sql implements java.io.Serializable {


      //--------------------/
     //- Member Variables -/
    //--------------------/

    private java.lang.String _manyKey;

    private java.lang.String _dirty = "check";

    private java.lang.String _manyTable;

    private java.lang.String _type;

    private java.lang.String _name;


      //----------------/
     //- Constructors -/
    //----------------/

    public Sql() {
        super();
    } //-- org.exolab.castor.mapping.xml.Sql()


      //-----------/
     //- Methods -/
    //-----------/

    /**
    **/
    public java.lang.String getDirty() {
        return this._dirty;
    } //-- java.lang.String getDirty() 

    /**
    **/
    public java.lang.String getManyKey() {
        return this._manyKey;
    } //-- java.lang.String getManyKey() 

    /**
    **/
    public java.lang.String getManyTable() {
        return this._manyTable;
    } //-- java.lang.String getManyTable() 

    /**
    **/
    public java.lang.String getName() {
        return this._name;
    } //-- java.lang.String getName() 

    /**
    **/
    public java.lang.String getType() {
        return this._type;
    } //-- java.lang.String getType() 

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
     * @param _dirty
    **/
    public void setDirty(java.lang.String _dirty) {
        this._dirty = _dirty;
    } //-- void setDirty(java.lang.String) 

    /**
     * 
     * @param _manyKey
    **/
    public void setManyKey(java.lang.String _manyKey) {
        this._manyKey = _manyKey;
    } //-- void setManyKey(java.lang.String) 

    /**
     * 
     * @param _manyTable
    **/
    public void setManyTable(java.lang.String _manyTable) {
        this._manyTable = _manyTable;
    } //-- void setManyTable(java.lang.String) 

    /**
     * 
     * @param _name
    **/
    public void setName(java.lang.String _name) {
        this._name = _name;
    } //-- void setName(java.lang.String) 

    /**
     * 
     * @param _type
    **/
    public void setType(java.lang.String _type) {
        this._type = _type;
    } //-- void setType(java.lang.String) 

    /**
     * 
     * @param reader
    **/
    public static org.exolab.castor.mapping.xml.Sql unmarshal(java.io.Reader reader) 
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.exolab.castor.mapping.xml.Sql) Unmarshaller.unmarshal(org.exolab.castor.mapping.xml.Sql.class, reader);
    } //-- org.exolab.castor.mapping.xml.Sql unmarshal(java.io.Reader) 

    /**
    **/
    public void validate() 
        throws org.exolab.castor.xml.ValidationException
    {
        org.exolab.castor.xml.Validator.validate(this, null);
    } //-- void validate() 

}
