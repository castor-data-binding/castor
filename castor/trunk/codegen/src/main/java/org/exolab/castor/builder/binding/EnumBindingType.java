/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.0.5</a>, using an XML
 * Schema.
 * $Id$
 */

package org.exolab.castor.builder.binding;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

/**
 * This type allows the mapping between an XML schema enumeration
 *  and a java class that follows the type-safe enumeration
 * paradigm.
 *  Additionally, it allows the specify the name of the Java
 * constant
 *  definition for a given enumeraton value.
 *  
 * 
 * @version $Revision$ $Date$
 */
public class EnumBindingType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _enumClassName.
     */
    private org.exolab.castor.builder.binding.EnumClassName _enumClassName;

    /**
     * Field _enumMemberList.
     */
    private java.util.List _enumMemberList;


      //----------------/
     //- Constructors -/
    //----------------/

    public EnumBindingType() 
     {
        super();
        this._enumMemberList = new java.util.ArrayList();
    } //-- org.exolab.castor.builder.binding.EnumBindingType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * 
     * 
     * @param vEnumMember
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addEnumMember(org.exolab.castor.builder.binding.EnumMember vEnumMember)
        throws java.lang.IndexOutOfBoundsException
    {
        this._enumMemberList.add(vEnumMember);
    } //-- void addEnumMember(org.exolab.castor.builder.binding.EnumMember) 

    /**
     * 
     * 
     * @param index
     * @param vEnumMember
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addEnumMember(int index, org.exolab.castor.builder.binding.EnumMember vEnumMember)
        throws java.lang.IndexOutOfBoundsException
    {
        this._enumMemberList.add(index, vEnumMember);
    } //-- void addEnumMember(int, org.exolab.castor.builder.binding.EnumMember) 

    /**
     * Method enumerateEnumMember.
     * 
     * @return an Enumeration over all possible elements of this
     * collection
     */
    public java.util.Enumeration enumerateEnumMember()
    {
        return java.util.Collections.enumeration(this._enumMemberList);
    } //-- java.util.Enumeration enumerateEnumMember() 

    /**
     * Returns the value of field 'enumClassName'.
     * 
     * @return the value of field 'EnumClassName'.
     */
    public org.exolab.castor.builder.binding.EnumClassName getEnumClassName()
    {
        return this._enumClassName;
    } //-- org.exolab.castor.builder.binding.EnumClassName getEnumClassName() 

    /**
     * Method getEnumMember.
     * 
     * @param index
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     * @return the value of the
     * org.exolab.castor.builder.binding.EnumMember at the given
     * index
     */
    public org.exolab.castor.builder.binding.EnumMember getEnumMember(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        // check bounds for index
        if (index < 0 || index >= this._enumMemberList.size()) {
            throw new IndexOutOfBoundsException("getEnumMember: Index value '" + index + "' not in range [0.." + (this._enumMemberList.size() - 1) + "]");
        }
        
        return (org.exolab.castor.builder.binding.EnumMember) _enumMemberList.get(index);
    } //-- org.exolab.castor.builder.binding.EnumMember getEnumMember(int) 

    /**
     * Method getEnumMember.
     * 
     * @return this collection as an Array
     */
    public org.exolab.castor.builder.binding.EnumMember[] getEnumMember()
    {
        int size = this._enumMemberList.size();
        org.exolab.castor.builder.binding.EnumMember[] array = new org.exolab.castor.builder.binding.EnumMember[size];
        java.util.Iterator iter = _enumMemberList.iterator();
        for (int index = 0; index < size; index++){
            array[index] = (org.exolab.castor.builder.binding.EnumMember) iter.next();
        }
        
        return array;
    } //-- org.exolab.castor.builder.binding.EnumMember[] getEnumMember() 

    /**
     * Method getEnumMemberCount.
     * 
     * @return the size of this collection
     */
    public int getEnumMemberCount()
    {
        return this._enumMemberList.size();
    } //-- int getEnumMemberCount() 

    /**
     * Method isValid.
     * 
     * @return true if this object is valid according to the schema
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
     * Method iterateEnumMember.
     * 
     * @return an Iterator over all possible elements in this
     * collection
     */
    public java.util.Iterator iterateEnumMember()
    {
        return this._enumMemberList.iterator();
    } //-- java.util.Iterator iterateEnumMember() 

    /**
     * 
     * 
     * @param out
     * @throws org.exolab.castor.xml.MarshalException if object is
     * null or if any SAXException is thrown during marshaling
     * @throws org.exolab.castor.xml.ValidationException if this
     * object is an invalid instance according to the schema
     */
    public void marshal(java.io.Writer out)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        
        Marshaller.marshal(this, out);
    } //-- void marshal(java.io.Writer) 

    /**
     * 
     * 
     * @param handler
     * @throws java.io.IOException if an IOException occurs during
     * marshaling
     * @throws org.exolab.castor.xml.ValidationException if this
     * object is an invalid instance according to the schema
     * @throws org.exolab.castor.xml.MarshalException if object is
     * null or if any SAXException is thrown during marshaling
     */
    public void marshal(org.xml.sax.ContentHandler handler)
        throws java.io.IOException, org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        
        Marshaller.marshal(this, handler);
    } //-- void marshal(org.xml.sax.ContentHandler) 

    /**
     */
    public void removeAllEnumMember()
    {
        this._enumMemberList.clear();
    } //-- void removeAllEnumMember() 

    /**
     * Method removeEnumMember.
     * 
     * @param vEnumMember
     * @return true if the object was removed from the collection.
     */
    public boolean removeEnumMember(org.exolab.castor.builder.binding.EnumMember vEnumMember)
    {
        boolean removed = _enumMemberList.remove(vEnumMember);
        return removed;
    } //-- boolean removeEnumMember(org.exolab.castor.builder.binding.EnumMember) 

    /**
     * Method removeEnumMemberAt.
     * 
     * @param index
     * @return the element removed from the collection
     */
    public org.exolab.castor.builder.binding.EnumMember removeEnumMemberAt(int index)
    {
        Object obj = this._enumMemberList.remove(index);
        return (org.exolab.castor.builder.binding.EnumMember) obj;
    } //-- org.exolab.castor.builder.binding.EnumMember removeEnumMemberAt(int) 

    /**
     * Sets the value of field 'enumClassName'.
     * 
     * @param enumClassName the value of field 'enumClassName'.
     */
    public void setEnumClassName(org.exolab.castor.builder.binding.EnumClassName enumClassName)
    {
        this._enumClassName = enumClassName;
    } //-- void setEnumClassName(org.exolab.castor.builder.binding.EnumClassName) 

    /**
     * 
     * 
     * @param index
     * @param vEnumMember
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void setEnumMember(int index, org.exolab.castor.builder.binding.EnumMember vEnumMember)
        throws java.lang.IndexOutOfBoundsException
    {
        // check bounds for index
        if (index < 0 || index >= this._enumMemberList.size()) {
            throw new IndexOutOfBoundsException("setEnumMember: Index value '" + index + "' not in range [0.." + (this._enumMemberList.size() - 1) + "]");
        }
        
        this._enumMemberList.set(index, vEnumMember);
    } //-- void setEnumMember(int, org.exolab.castor.builder.binding.EnumMember) 

    /**
     * 
     * 
     * @param vEnumMemberArray
     */
    public void setEnumMember(org.exolab.castor.builder.binding.EnumMember[] vEnumMemberArray)
    {
        //-- copy array
        _enumMemberList.clear();
        
        for (int i = 0; i < vEnumMemberArray.length; i++) {
                this._enumMemberList.add(vEnumMemberArray[i]);
        }
    } //-- void setEnumMember(org.exolab.castor.builder.binding.EnumMember) 

    /**
     * Method unmarshalEnumBindingType.
     * 
     * @param reader
     * @throws org.exolab.castor.xml.MarshalException if object is
     * null or if any SAXException is thrown during marshaling
     * @throws org.exolab.castor.xml.ValidationException if this
     * object is an invalid instance according to the schema
     * @return the unmarshaled
     * org.exolab.castor.builder.binding.EnumBindingType
     */
    public static org.exolab.castor.builder.binding.EnumBindingType unmarshalEnumBindingType(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.exolab.castor.builder.binding.EnumBindingType) Unmarshaller.unmarshal(org.exolab.castor.builder.binding.EnumBindingType.class, reader);
    } //-- org.exolab.castor.builder.binding.EnumBindingType unmarshalEnumBindingType(java.io.Reader) 

    /**
     * 
     * 
     * @throws org.exolab.castor.xml.ValidationException if this
     * object is an invalid instance according to the schema
     */
    public void validate()
        throws org.exolab.castor.xml.ValidationException
    {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    } //-- void validate() 

}
