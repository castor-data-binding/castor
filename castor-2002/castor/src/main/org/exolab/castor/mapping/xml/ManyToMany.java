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
import java.util.Enumeration;
import java.util.Vector;
import org.exolab.castor.xml.*;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.DocumentHandler;

/**
 * 
 * @version $Revision$ $Date$
**/
public class ManyToMany implements java.io.Serializable {


      //--------------------/
     //- Member Variables -/
    //--------------------/

    private java.lang.String _name;

    private java.lang.String _description;

    private MapTo _mapTo;

    private java.util.Vector _typeMappingList;


      //----------------/
     //- Constructors -/
    //----------------/

    public ManyToMany() {
        super();
        _typeMappingList = new Vector();
    } //-- org.exolab.castor.mapping.xml.ManyToMany()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * 
     * @param vTypeMapping
    **/
    public void addTypeMapping(TypeMapping vTypeMapping) 
        throws java.lang.IndexOutOfBoundsException
    {
        _typeMappingList.addElement(vTypeMapping);
    } //-- void addTypeMapping(TypeMapping) 

    /**
    **/
    public java.util.Enumeration enumerateTypeMapping() {
        return _typeMappingList.elements();
    } //-- java.util.Enumeration enumerateTypeMapping() 

    /**
    **/
    public java.lang.String getDescription() {
        return this._description;
    } //-- java.lang.String getDescription() 

    /**
    **/
    public MapTo getMapTo() {
        return this._mapTo;
    } //-- MapTo getMapTo() 

    /**
    **/
    public java.lang.String getName() {
        return this._name;
    } //-- java.lang.String getName() 

    /**
    **/
    public java.lang.String getReferenceId() {
        return this._name;
    } //-- java.lang.String getReferenceId() 

    /**
     * 
     * @param index
    **/
    public TypeMapping getTypeMapping(int index) 
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _typeMappingList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (TypeMapping) _typeMappingList.elementAt(index);
    } //-- TypeMapping getTypeMapping(int) 

    /**
    **/
    public TypeMapping[] getTypeMapping() {
        int size = _typeMappingList.size();
        TypeMapping[] mArray = new TypeMapping[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (TypeMapping) _typeMappingList.elementAt(index);
        }
        return mArray;
    } //-- TypeMapping[] getTypeMapping() 

    /**
    **/
    public int getTypeMappingCount() {
        return _typeMappingList.size();
    } //-- int getTypeMappingCount() 

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
    **/
    public void removeAllTypeMapping() {
        _typeMappingList.removeAllElements();
    } //-- void removeAllTypeMapping() 

    /**
     * 
     * @param index
    **/
    public TypeMapping removeTypeMapping(int index) {
        Object obj = _typeMappingList.elementAt(index);
        _typeMappingList.removeElementAt(index);
        return (TypeMapping) obj;
    } //-- TypeMapping removeTypeMapping(int) 

    /**
     * 
     * @param _description
    **/
    public void setDescription(java.lang.String _description) {
        this._description = _description;
    } //-- void setDescription(java.lang.String) 

    /**
     * 
     * @param _mapTo
    **/
    public void setMapTo(MapTo _mapTo) {
        this._mapTo = _mapTo;
    } //-- void setMapTo(MapTo) 

    /**
     * 
     * @param _name
    **/
    public void setName(java.lang.String _name) {
        this._name = _name;
    } //-- void setName(java.lang.String) 

    /**
     * 
     * @param vTypeMapping
     * @param index
    **/
    public void setTypeMapping(TypeMapping vTypeMapping, int index) 
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _typeMappingList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _typeMappingList.setElementAt(vTypeMapping, index);
    } //-- void setTypeMapping(TypeMapping, int) 

    /**
     * 
     * @param reader
    **/
    public static org.exolab.castor.mapping.xml.ManyToMany unmarshal(java.io.Reader reader) 
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.exolab.castor.mapping.xml.ManyToMany) Unmarshaller.unmarshal(org.exolab.castor.mapping.xml.ManyToMany.class, reader);
    } //-- org.exolab.castor.mapping.xml.ManyToMany unmarshal(java.io.Reader) 

    /**
    **/
    public void validate() 
        throws org.exolab.castor.xml.ValidationException
    {
        org.exolab.castor.xml.Validator.validate(this, null);
    } //-- void validate() 

}
