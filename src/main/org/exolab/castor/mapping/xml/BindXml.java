/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.0M1</a>, using an XML
 * Schema.
 * $Id$
 */

package org.exolab.castor.mapping.xml;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Enumeration;
import org.exolab.castor.mapping.xml.types.BindXmlAutoNamingType;
import org.exolab.castor.mapping.xml.types.BindXmlNodeType;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.ContentHandler;

/**
 * The 'bind-xml' element is used for specifying XML specific
 * databinding
 *  properties and behavior for a specific field. 'bind-xml' may
 * only appear
 *  as a child of a 'field' element.
 *  
 * 
 * @version $Revision$ $Date$
 */
public class BindXml implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * An optional attribute used for specifying the XML name for
     * the
     *  field associated with the 'bind-xml' element.
     *  
     */
    private java.lang.String _name;

    /**
     * Field _type
     */
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
     */
    private org.exolab.castor.mapping.xml.types.BindXmlAutoNamingType _autoNaming;

    /**
     * Allows specifying a nested location path for this field,
     *  the value should just be a simplified XPath like value
     *  where names are separated by "/".
     *  
     */
    private java.lang.String _location;

    /**
     * Field _matches
     */
    private java.lang.String _matches;

    /**
     * Field _reference
     */
    private boolean _reference;

    /**
     * keeps track of state for field: _reference
     */
    private boolean _has_reference;

    /**
     * Field _node
     */
    private org.exolab.castor.mapping.xml.types.BindXmlNodeType _node;

    /**
     * Field _QNamePrefix
     */
    private java.lang.String _QNamePrefix;

    /**
     * Field _transient
     */
    private boolean _transient;

    /**
     * keeps track of state for field: _transient
     */
    private boolean _has_transient;

    /**
     * Field _classMapping
     */
    private org.exolab.castor.mapping.xml.ClassMapping _classMapping;

    /**
     * Field _propertyList
     */
    private java.util.ArrayList _propertyList;


      //----------------/
     //- Constructors -/
    //----------------/

    public BindXml() 
     {
        super();
        _propertyList = new ArrayList();
    } //-- org.exolab.castor.mapping.xml.BindXml()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addProperty
     * 
     * 
     * 
     * @param vProperty
     */
    public void addProperty(org.exolab.castor.mapping.xml.Property vProperty)
        throws java.lang.IndexOutOfBoundsException
    {
        _propertyList.add(vProperty);
    } //-- void addProperty(org.exolab.castor.mapping.xml.Property) 

    /**
     * Method addProperty
     * 
     * 
     * 
     * @param index
     * @param vProperty
     */
    public void addProperty(int index, org.exolab.castor.mapping.xml.Property vProperty)
        throws java.lang.IndexOutOfBoundsException
    {
        _propertyList.add(index, vProperty);
    } //-- void addProperty(int, org.exolab.castor.mapping.xml.Property) 

    /**
     * Method clearProperty
     * 
     */
    public void clearProperty()
    {
        _propertyList.clear();
    } //-- void clearProperty() 

    /**
     * Method deleteReference
     * 
     */
    public void deleteReference()
    {
        this._has_reference= false;
    } //-- void deleteReference() 

    /**
     * Method deleteTransient
     * 
     */
    public void deleteTransient()
    {
        this._has_transient= false;
    } //-- void deleteTransient() 

    /**
     * Method enumerateProperty
     * 
     * 
     * 
     * @return Enumeration
     */
    public java.util.Enumeration enumerateProperty()
    {
        return new org.exolab.castor.util.IteratorEnumeration(_propertyList.iterator());
    } //-- java.util.Enumeration enumerateProperty() 

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
     * @return BindXmlAutoNamingType
     * @return the value of field 'autoNaming'.
     */
    public org.exolab.castor.mapping.xml.types.BindXmlAutoNamingType getAutoNaming()
    {
        return this._autoNaming;
    } //-- org.exolab.castor.mapping.xml.types.BindXmlAutoNamingType getAutoNaming() 

    /**
     * Returns the value of field 'classMapping'.
     * 
     * @return ClassMapping
     * @return the value of field 'classMapping'.
     */
    public org.exolab.castor.mapping.xml.ClassMapping getClassMapping()
    {
        return this._classMapping;
    } //-- org.exolab.castor.mapping.xml.ClassMapping getClassMapping() 

    /**
     * Returns the value of field 'location'. The field 'location'
     * has the following description: Allows specifying a nested
     * location path for this field,
     *  the value should just be a simplified XPath like value
     *  where names are separated by "/".
     *  
     * 
     * @return String
     * @return the value of field 'location'.
     */
    public java.lang.String getLocation()
    {
        return this._location;
    } //-- java.lang.String getLocation() 

    /**
     * Returns the value of field 'matches'.
     * 
     * @return String
     * @return the value of field 'matches'.
     */
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
     * @return String
     * @return the value of field 'name'.
     */
    public java.lang.String getName()
    {
        return this._name;
    } //-- java.lang.String getName() 

    /**
     * Returns the value of field 'node'.
     * 
     * @return BindXmlNodeType
     * @return the value of field 'node'.
     */
    public org.exolab.castor.mapping.xml.types.BindXmlNodeType getNode()
    {
        return this._node;
    } //-- org.exolab.castor.mapping.xml.types.BindXmlNodeType getNode() 

    /**
     * Method getProperty
     * 
     * 
     * 
     * @param index
     * @return Property
     */
    public org.exolab.castor.mapping.xml.Property getProperty(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _propertyList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (org.exolab.castor.mapping.xml.Property) _propertyList.get(index);
    } //-- org.exolab.castor.mapping.xml.Property getProperty(int) 

    /**
     * Method getProperty
     * 
     * 
     * 
     * @return Property
     */
    public org.exolab.castor.mapping.xml.Property[] getProperty()
    {
        int size = _propertyList.size();
        org.exolab.castor.mapping.xml.Property[] mArray = new org.exolab.castor.mapping.xml.Property[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (org.exolab.castor.mapping.xml.Property) _propertyList.get(index);
        }
        return mArray;
    } //-- org.exolab.castor.mapping.xml.Property[] getProperty() 

    /**
     * Method getPropertyCount
     * 
     * 
     * 
     * @return int
     */
    public int getPropertyCount()
    {
        return _propertyList.size();
    } //-- int getPropertyCount() 

    /**
     * Returns the value of field 'QNamePrefix'.
     * 
     * @return String
     * @return the value of field 'QNamePrefix'.
     */
    public java.lang.String getQNamePrefix()
    {
        return this._QNamePrefix;
    } //-- java.lang.String getQNamePrefix() 

    /**
     * Returns the value of field 'reference'.
     * 
     * @return boolean
     * @return the value of field 'reference'.
     */
    public boolean getReference()
    {
        return this._reference;
    } //-- boolean getReference() 

    /**
     * Returns the value of field 'transient'.
     * 
     * @return boolean
     * @return the value of field 'transient'.
     */
    public boolean getTransient()
    {
        return this._transient;
    } //-- boolean getTransient() 

    /**
     * Returns the value of field 'type'.
     * 
     * @return String
     * @return the value of field 'type'.
     */
    public java.lang.String getType()
    {
        return this._type;
    } //-- java.lang.String getType() 

    /**
     * Method hasReference
     * 
     * 
     * 
     * @return boolean
     */
    public boolean hasReference()
    {
        return this._has_reference;
    } //-- boolean hasReference() 

    /**
     * Method hasTransient
     * 
     * 
     * 
     * @return boolean
     */
    public boolean hasTransient()
    {
        return this._has_transient;
    } //-- boolean hasTransient() 

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
     * Method removeProperty
     * 
     * 
     * 
     * @param vProperty
     * @return boolean
     */
    public boolean removeProperty(org.exolab.castor.mapping.xml.Property vProperty)
    {
        boolean removed = _propertyList.remove(vProperty);
        return removed;
    } //-- boolean removeProperty(org.exolab.castor.mapping.xml.Property) 

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
     */
    public void setAutoNaming(org.exolab.castor.mapping.xml.types.BindXmlAutoNamingType autoNaming)
    {
        this._autoNaming = autoNaming;
    } //-- void setAutoNaming(org.exolab.castor.mapping.xml.types.BindXmlAutoNamingType) 

    /**
     * Sets the value of field 'classMapping'.
     * 
     * @param classMapping the value of field 'classMapping'.
     */
    public void setClassMapping(org.exolab.castor.mapping.xml.ClassMapping classMapping)
    {
        this._classMapping = classMapping;
    } //-- void setClassMapping(org.exolab.castor.mapping.xml.ClassMapping) 

    /**
     * Sets the value of field 'location'. The field 'location' has
     * the following description: Allows specifying a nested
     * location path for this field,
     *  the value should just be a simplified XPath like value
     *  where names are separated by "/".
     *  
     * 
     * @param location the value of field 'location'.
     */
    public void setLocation(java.lang.String location)
    {
        this._location = location;
    } //-- void setLocation(java.lang.String) 

    /**
     * Sets the value of field 'matches'.
     * 
     * @param matches the value of field 'matches'.
     */
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
     */
    public void setName(java.lang.String name)
    {
        this._name = name;
    } //-- void setName(java.lang.String) 

    /**
     * Sets the value of field 'node'.
     * 
     * @param node the value of field 'node'.
     */
    public void setNode(org.exolab.castor.mapping.xml.types.BindXmlNodeType node)
    {
        this._node = node;
    } //-- void setNode(org.exolab.castor.mapping.xml.types.BindXmlNodeType) 

    /**
     * Method setProperty
     * 
     * 
     * 
     * @param index
     * @param vProperty
     */
    public void setProperty(int index, org.exolab.castor.mapping.xml.Property vProperty)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _propertyList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _propertyList.set(index, vProperty);
    } //-- void setProperty(int, org.exolab.castor.mapping.xml.Property) 

    /**
     * Method setProperty
     * 
     * 
     * 
     * @param propertyArray
     */
    public void setProperty(org.exolab.castor.mapping.xml.Property[] propertyArray)
    {
        //-- copy array
        _propertyList.clear();
        for (int i = 0; i < propertyArray.length; i++) {
            _propertyList.add(propertyArray[i]);
        }
    } //-- void setProperty(org.exolab.castor.mapping.xml.Property) 

    /**
     * Sets the value of field 'QNamePrefix'.
     * 
     * @param QNamePrefix the value of field 'QNamePrefix'.
     */
    public void setQNamePrefix(java.lang.String QNamePrefix)
    {
        this._QNamePrefix = QNamePrefix;
    } //-- void setQNamePrefix(java.lang.String) 

    /**
     * Sets the value of field 'reference'.
     * 
     * @param reference the value of field 'reference'.
     */
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
     */
    public void setTransient(boolean _transient)
    {
        this._transient = _transient;
        this._has_transient = true;
    } //-- void setTransient(boolean) 

    /**
     * Sets the value of field 'type'.
     * 
     * @param type the value of field 'type'.
     */
    public void setType(java.lang.String type)
    {
        this._type = type;
    } //-- void setType(java.lang.String) 

    /**
     * Method unmarshal
     * 
     * 
     * 
     * @param reader
     * @return BindXml
     */
    public static org.exolab.castor.mapping.xml.BindXml unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.exolab.castor.mapping.xml.BindXml) Unmarshaller.unmarshal(org.exolab.castor.mapping.xml.BindXml.class, reader);
    } //-- org.exolab.castor.mapping.xml.BindXml unmarshal(java.io.Reader) 

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
