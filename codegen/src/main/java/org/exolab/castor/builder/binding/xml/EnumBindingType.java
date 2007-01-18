/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.1-M2</a>, using an XML
 * Schema.
 * $Id$
 */

package org.exolab.castor.builder.binding.xml;

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
    private org.exolab.castor.builder.binding.xml.EnumClassName _enumClassName;

    /**
     * Field _enumMemberList.
     */
    private java.util.List _enumMemberList;


      //----------------/
     //- Constructors -/
    //----------------/

    public EnumBindingType() {
        super();
        this._enumMemberList = new java.util.ArrayList();
    }


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
    public void addEnumMember(
            final org.exolab.castor.builder.binding.xml.EnumMember vEnumMember)
    throws java.lang.IndexOutOfBoundsException {
        this._enumMemberList.add(vEnumMember);
    }

    /**
     * 
     * 
     * @param index
     * @param vEnumMember
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addEnumMember(
            final int index,
            final org.exolab.castor.builder.binding.xml.EnumMember vEnumMember)
    throws java.lang.IndexOutOfBoundsException {
        this._enumMemberList.add(index, vEnumMember);
    }

    /**
     * Method enumerateEnumMember.
     * 
     * @return an Enumeration over all possible elements of this
     * collection
     */
    public java.util.Enumeration enumerateEnumMember(
    ) {
        return java.util.Collections.enumeration(this._enumMemberList);
    }

    /**
     * Returns the value of field 'enumClassName'.
     * 
     * @return the value of field 'EnumClassName'.
     */
    public org.exolab.castor.builder.binding.xml.EnumClassName getEnumClassName(
    ) {
        return this._enumClassName;
    }

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
    public org.exolab.castor.builder.binding.xml.EnumMember getEnumMember(
            final int index)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._enumMemberList.size()) {
            throw new IndexOutOfBoundsException("getEnumMember: Index value '" + index + "' not in range [0.." + (this._enumMemberList.size() - 1) + "]");
        }
        
        return (org.exolab.castor.builder.binding.xml.EnumMember) _enumMemberList.get(index);
    }

    /**
     * Method getEnumMember.Returns the contents of the collection
     * in an Array.  <p>Note:  Just in case the collection contents
     * are changing in another thread, we pass a 0-length Array of
     * the correct type into the API call.  This way we <i>know</i>
     * that the Array returned is of exactly the correct length.
     * 
     * @return this collection as an Array
     */
    public org.exolab.castor.builder.binding.xml.EnumMember[] getEnumMember(
    ) {
        org.exolab.castor.builder.binding.xml.EnumMember[] array = new org.exolab.castor.builder.binding.xml.EnumMember[0];
        return (org.exolab.castor.builder.binding.xml.EnumMember[]) this._enumMemberList.toArray(array);
    }

    /**
     * Method getEnumMemberCount.
     * 
     * @return the size of this collection
     */
    public int getEnumMemberCount(
    ) {
        return this._enumMemberList.size();
    }

    /**
     * Method isValid.
     * 
     * @return true if this object is valid according to the schema
     */
    public boolean isValid(
    ) {
        try {
            validate();
        } catch (org.exolab.castor.xml.ValidationException vex) {
            return false;
        }
        return true;
    }

    /**
     * Method iterateEnumMember.
     * 
     * @return an Iterator over all possible elements in this
     * collection
     */
    public java.util.Iterator iterateEnumMember(
    ) {
        return this._enumMemberList.iterator();
    }

    /**
     * 
     * 
     * @param out
     * @throws org.exolab.castor.xml.MarshalException if object is
     * null or if any SAXException is thrown during marshaling
     * @throws org.exolab.castor.xml.ValidationException if this
     * object is an invalid instance according to the schema
     */
    public void marshal(
            final java.io.Writer out)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        Marshaller.marshal(this, out);
    }

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
    public void marshal(
            final org.xml.sax.ContentHandler handler)
    throws java.io.IOException, org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        Marshaller.marshal(this, handler);
    }

    /**
     */
    public void removeAllEnumMember(
    ) {
        this._enumMemberList.clear();
    }

    /**
     * Method removeEnumMember.
     * 
     * @param vEnumMember
     * @return true if the object was removed from the collection.
     */
    public boolean removeEnumMember(
            final org.exolab.castor.builder.binding.xml.EnumMember vEnumMember) {
        boolean removed = _enumMemberList.remove(vEnumMember);
        return removed;
    }

    /**
     * Method removeEnumMemberAt.
     * 
     * @param index
     * @return the element removed from the collection
     */
    public org.exolab.castor.builder.binding.xml.EnumMember removeEnumMemberAt(
            final int index) {
        java.lang.Object obj = this._enumMemberList.remove(index);
        return (org.exolab.castor.builder.binding.xml.EnumMember) obj;
    }

    /**
     * Sets the value of field 'enumClassName'.
     * 
     * @param enumClassName the value of field 'enumClassName'.
     */
    public void setEnumClassName(
            final org.exolab.castor.builder.binding.xml.EnumClassName enumClassName) {
        this._enumClassName = enumClassName;
    }

    /**
     * 
     * 
     * @param index
     * @param vEnumMember
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void setEnumMember(
            final int index,
            final org.exolab.castor.builder.binding.xml.EnumMember vEnumMember)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._enumMemberList.size()) {
            throw new IndexOutOfBoundsException("setEnumMember: Index value '" + index + "' not in range [0.." + (this._enumMemberList.size() - 1) + "]");
        }
        
        this._enumMemberList.set(index, vEnumMember);
    }

    /**
     * 
     * 
     * @param vEnumMemberArray
     */
    public void setEnumMember(
            final org.exolab.castor.builder.binding.xml.EnumMember[] vEnumMemberArray) {
        //-- copy array
        _enumMemberList.clear();
        
        for (int i = 0; i < vEnumMemberArray.length; i++) {
                this._enumMemberList.add(vEnumMemberArray[i]);
        }
    }

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
    public static org.exolab.castor.builder.binding.xml.EnumBindingType unmarshalEnumBindingType(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (org.exolab.castor.builder.binding.xml.EnumBindingType) Unmarshaller.unmarshal(org.exolab.castor.builder.binding.xml.EnumBindingType.class, reader);
    }

    /**
     * 
     * 
     * @throws org.exolab.castor.xml.ValidationException if this
     * object is an invalid instance according to the schema
     */
    public void validate(
    )
    throws org.exolab.castor.xml.ValidationException {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    }

}
