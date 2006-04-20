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

import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import org.exolab.castor.xml.*;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.ContentHandler;

/**
 * 
 *                  This element allows to define naming convention
 * when naming a complexType, element or
 *                  modelGroup. Indeed the user can decide of a
 * prefix to add to each class name as well
 *                  as a suffix. This naming style won't affect the
 * names entered in the binding file but only 
 *                  the XML Names.
 *             
 * 
 * @version $Revision$ $Date$
**/
public class NamingXMLType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    private NamingType _elementName;

    private NamingType _complexTypeName;

    private NamingType _modelGroupName;


      //----------------/
     //- Constructors -/
    //----------------/

    public NamingXMLType() {
        super();
    } //-- org.exolab.castor.builder.binding.NamingXMLType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'complexTypeName'.
     * 
     * @return the value of field 'complexTypeName'.
    **/
    public NamingType getComplexTypeName()
    {
        return this._complexTypeName;
    } //-- NamingType getComplexTypeName() 

    /**
     * Returns the value of field 'elementName'.
     * 
     * @return the value of field 'elementName'.
    **/
    public NamingType getElementName()
    {
        return this._elementName;
    } //-- NamingType getElementName() 

    /**
     * Returns the value of field 'modelGroupName'.
     * 
     * @return the value of field 'modelGroupName'.
    **/
    public NamingType getModelGroupName()
    {
        return this._modelGroupName;
    } //-- NamingType getModelGroupName() 

    /**
    **/
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
     * 
     * 
     * @param out
    **/
    public void marshal(java.io.Writer out)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        
        Marshaller.marshal(this, out);
    } //-- void marshal(java.io.Writer) 

    /**
     * 
     * 
     * @param handler
    **/
    public void marshal(org.xml.sax.ContentHandler handler)
        throws java.io.IOException, org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        
        Marshaller.marshal(this, handler);
    } //-- void marshal(org.xml.sax.ContentHandler) 

    /**
     * Sets the value of field 'complexTypeName'.
     * 
     * @param complexTypeName the value of field 'complexTypeName'.
    **/
    public void setComplexTypeName(NamingType complexTypeName)
    {
        this._complexTypeName = complexTypeName;
    } //-- void setComplexTypeName(NamingType) 

    /**
     * Sets the value of field 'elementName'.
     * 
     * @param elementName the value of field 'elementName'.
    **/
    public void setElementName(NamingType elementName)
    {
        this._elementName = elementName;
    } //-- void setElementName(NamingType) 

    /**
     * Sets the value of field 'modelGroupName'.
     * 
     * @param modelGroupName the value of field 'modelGroupName'.
    **/
    public void setModelGroupName(NamingType modelGroupName)
    {
        this._modelGroupName = modelGroupName;
    } //-- void setModelGroupName(NamingType) 

    /**
     * 
     * 
     * @param reader
    **/
    public static org.exolab.castor.builder.binding.NamingXMLType unmarshalNamingXMLType(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.exolab.castor.builder.binding.NamingXMLType) Unmarshaller.unmarshal(org.exolab.castor.builder.binding.NamingXMLType.class, reader);
    } //-- org.exolab.castor.builder.binding.NamingXMLType unmarshalNamingXMLType(java.io.Reader) 

    /**
    **/
    public void validate()
        throws org.exolab.castor.xml.ValidationException
    {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    } //-- void validate() 

}
