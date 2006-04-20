/*
 * This class was automatically generated with 
 * <a href="http://castor.exolab.org">Castor 0.9.3.9+</a>, using an
 * XML Schema.
 * $Id$
 */

package org.exolab.castor.mapping.xml;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import org.exolab.castor.mapping.xml.types.BindXmlAutoNamingType;
import org.exolab.castor.mapping.xml.types.BindXmlNodeType;
import org.exolab.castor.xml.*;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.DocumentHandler;

/**
 * 
 *           The 'bind-xml' element is used for specifying XML
 * specific databinding
 *           properties and behavior for a specific field.
 * 'bind-xml' may only appear
 *           as a child of a 'field' element.
 *        
 * 
 * @version $Revision$ $Date$
**/
public class BindXml implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * An optional attribute used for specifying the XML name for
     * the
     *  field associated with the 'bind-xml' element.
     *  
    **/
    private java.lang.String _name;

    private java.lang.String _type;

    /**
     * Allows specifying how Castor should automatically determines
     *  the XML name of this field when no name has been specified.
     *  In most cases the XML name is determined by using the field
     * name,
     *  but in some cases the user may want to use the Class name
     * of
     *  the field type. This attribute allows choosing between the
     *  two approaches. If this attribute is not specified, the
     * field
     *  name is used as the XML name.
     *  
    **/
    private org.exolab.castor.mapping.xml.types.BindXmlAutoNamingType _autoNaming;

    private java.lang.String _matches;

    private boolean _reference;

    /**
     * keeps track of state for field: _reference
    **/
    private boolean _has_reference;

    private org.exolab.castor.mapping.xml.types.BindXmlNodeType _node;

    private java.lang.String _QNamePrefix;

    private boolean _transient;

    /**
     * keeps track of state for field: _transient
    **/
    private boolean _has_transient;


      //----------------/
     //- Constructors -/
    //----------------/

    public BindXml() {
        super();
    } //-- org.exolab.castor.mapping.xml.BindXml()


      //-----------/
     //- Methods -/
    //-----------/

    /**
    **/
    public void deleteReference()
    {
        this._has_reference= false;
    } //-- void deleteReference() 

    /**
    **/
    public void deleteTransient()
    {
        this._has_transient= false;
    } //-- void deleteTransient() 

    /**
     * Returns the value of field 'autoNaming'. The field
     * 'autoNaming' has the following description: Allows
     * specifying how Castor should automatically determines
     *  the XML name of this field when no name has been specified.
     *  In most cases the XML name is determined by using the field
     * name,
     *  but in some cases the user may want to use the Class name
     * of
     *  the field type. This attribute allows choosing between the
     *  two approaches. If this attribute is not specified, the
     * field
     *  name is used as the XML name.
     *  
     * 
     * @return the value of field 'autoNaming'.
    **/
    public org.exolab.castor.mapping.xml.types.BindXmlAutoNamingType getAutoNaming()
    {
        return this._autoNaming;
    } //-- org.exolab.castor.mapping.xml.types.BindXmlAutoNamingType getAutoNaming() 

    /**
     * Returns the value of field 'matches'.
     * 
     * @return the value of field 'matches'.
    **/
    public java.lang.String getMatches()
    {
        return this._matches;
    } //-- java.lang.String getMatches() 

    /**
     * Returns the value of field 'name'. The field 'name' has the
     * following description: An optional attribute used for
     * specifying the XML name for the
     *  field associated with the 'bind-xml' element.
     *  
     * 
     * @return the value of field 'name'.
    **/
    public java.lang.String getName()
    {
        return this._name;
    } //-- java.lang.String getName() 

    /**
     * Returns the value of field 'node'.
     * 
     * @return the value of field 'node'.
    **/
    public org.exolab.castor.mapping.xml.types.BindXmlNodeType getNode()
    {
        return this._node;
    } //-- org.exolab.castor.mapping.xml.types.BindXmlNodeType getNode() 

    /**
     * Returns the value of field 'QNamePrefix'.
     * 
     * @return the value of field 'QNamePrefix'.
    **/
    public java.lang.String getQNamePrefix()
    {
        return this._QNamePrefix;
    } //-- java.lang.String getQNamePrefix() 

    /**
     * Returns the value of field 'reference'.
     * 
     * @return the value of field 'reference'.
    **/
    public boolean getReference()
    {
        return this._reference;
    } //-- boolean getReference() 

    /**
     * Returns the value of field 'transient'.
     * 
     * @return the value of field 'transient'.
    **/
    public boolean getTransient()
    {
        return this._transient;
    } //-- boolean getTransient() 

    /**
     * Returns the value of field 'type'.
     * 
     * @return the value of field 'type'.
    **/
    public java.lang.String getType()
    {
        return this._type;
    } //-- java.lang.String getType() 

    /**
    **/
    public boolean hasReference()
    {
        return this._has_reference;
    } //-- boolean hasReference() 

    /**
    **/
    public boolean hasTransient()
    {
        return this._has_transient;
    } //-- boolean hasTransient() 

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
    public void marshal(org.xml.sax.DocumentHandler handler)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        
        Marshaller.marshal(this, handler);
    } //-- void marshal(org.xml.sax.DocumentHandler) 

    /**
     * Sets the value of field 'autoNaming'. The field 'autoNaming'
     * has the following description: Allows specifying how Castor
     * should automatically determines
     *  the XML name of this field when no name has been specified.
     *  In most cases the XML name is determined by using the field
     * name,
     *  but in some cases the user may want to use the Class name
     * of
     *  the field type. This attribute allows choosing between the
     *  two approaches. If this attribute is not specified, the
     * field
     *  name is used as the XML name.
     *  
     * 
     * @param autoNaming the value of field 'autoNaming'.
    **/
    public void setAutoNaming(org.exolab.castor.mapping.xml.types.BindXmlAutoNamingType autoNaming)
    {
        this._autoNaming = autoNaming;
    } //-- void setAutoNaming(org.exolab.castor.mapping.xml.types.BindXmlAutoNamingType) 

    /**
     * Sets the value of field 'matches'.
     * 
     * @param matches the value of field 'matches'.
    **/
    public void setMatches(java.lang.String matches)
    {
        this._matches = matches;
    } //-- void setMatches(java.lang.String) 

    /**
     * Sets the value of field 'name'. The field 'name' has the
     * following description: An optional attribute used for
     * specifying the XML name for the
     *  field associated with the 'bind-xml' element.
     *  
     * 
     * @param name the value of field 'name'.
    **/
    public void setName(java.lang.String name)
    {
        this._name = name;
    } //-- void setName(java.lang.String) 

    /**
     * Sets the value of field 'node'.
     * 
     * @param node the value of field 'node'.
    **/
    public void setNode(org.exolab.castor.mapping.xml.types.BindXmlNodeType node)
    {
        this._node = node;
    } //-- void setNode(org.exolab.castor.mapping.xml.types.BindXmlNodeType) 

    /**
     * Sets the value of field 'QNamePrefix'.
     * 
     * @param QNamePrefix the value of field 'QNamePrefix'.
    **/
    public void setQNamePrefix(java.lang.String QNamePrefix)
    {
        this._QNamePrefix = QNamePrefix;
    } //-- void setQNamePrefix(java.lang.String) 

    /**
     * Sets the value of field 'reference'.
     * 
     * @param reference the value of field 'reference'.
    **/
    public void setReference(boolean reference)
    {
        this._reference = reference;
        this._has_reference = true;
    } //-- void setReference(boolean) 

    /**
     * Sets the value of field 'transient'.
     * 
     * @param _transient
     * @param transient the value of field 'transient'.
    **/
    public void setTransient(boolean _transient)
    {
        this._transient = _transient;
        this._has_transient = true;
    } //-- void setTransient(boolean) 

    /**
     * Sets the value of field 'type'.
     * 
     * @param type the value of field 'type'.
    **/
    public void setType(java.lang.String type)
    {
        this._type = type;
    } //-- void setType(java.lang.String) 

    /**
     * 
     * 
     * @param reader
    **/
    public static org.exolab.castor.mapping.xml.BindXml unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.exolab.castor.mapping.xml.BindXml) Unmarshaller.unmarshal(org.exolab.castor.mapping.xml.BindXml.class, reader);
    } //-- org.exolab.castor.mapping.xml.BindXml unmarshal(java.io.Reader) 

    /**
    **/
    public void validate()
        throws org.exolab.castor.xml.ValidationException
    {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    } //-- void validate() 

}
