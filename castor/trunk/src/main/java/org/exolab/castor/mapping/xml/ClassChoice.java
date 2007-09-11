/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.1.2.1</a>, using an XML
 * Schema.
 * $Id$
 */

package org.exolab.castor.mapping.xml;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

/**
 * Class ClassChoice.
 * 
 * @version $Revision$ $Date$
 */
public class ClassChoice implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _fieldMappingList.
     */
    private java.util.List _fieldMappingList;

    /**
     * Field _containerList.
     */
    private java.util.List _containerList;


      //----------------/
     //- Constructors -/
    //----------------/

    public ClassChoice() {
        super();
        this._fieldMappingList = new java.util.ArrayList();
        this._containerList = new java.util.ArrayList();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * 
     * 
     * @param vContainer
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addContainer(
            final org.exolab.castor.mapping.xml.Container vContainer)
    throws java.lang.IndexOutOfBoundsException {
        this._containerList.add(vContainer);
    }

    /**
     * 
     * 
     * @param index
     * @param vContainer
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addContainer(
            final int index,
            final org.exolab.castor.mapping.xml.Container vContainer)
    throws java.lang.IndexOutOfBoundsException {
        this._containerList.add(index, vContainer);
    }

    /**
     * 
     * 
     * @param vFieldMapping
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addFieldMapping(
            final org.exolab.castor.mapping.xml.FieldMapping vFieldMapping)
    throws java.lang.IndexOutOfBoundsException {
        this._fieldMappingList.add(vFieldMapping);
    }

    /**
     * 
     * 
     * @param index
     * @param vFieldMapping
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addFieldMapping(
            final int index,
            final org.exolab.castor.mapping.xml.FieldMapping vFieldMapping)
    throws java.lang.IndexOutOfBoundsException {
        this._fieldMappingList.add(index, vFieldMapping);
    }

    /**
     * Method enumerateContainer.
     * 
     * @return an Enumeration over all possible elements of this
     * collection
     */
    public java.util.Enumeration enumerateContainer(
    ) {
        return java.util.Collections.enumeration(this._containerList);
    }

    /**
     * Method enumerateFieldMapping.
     * 
     * @return an Enumeration over all possible elements of this
     * collection
     */
    public java.util.Enumeration enumerateFieldMapping(
    ) {
        return java.util.Collections.enumeration(this._fieldMappingList);
    }

    /**
     * Method getContainer.
     * 
     * @param index
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     * @return the value of the
     * org.exolab.castor.mapping.xml.Container at the given index
     */
    public org.exolab.castor.mapping.xml.Container getContainer(
            final int index)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._containerList.size()) {
            throw new IndexOutOfBoundsException("getContainer: Index value '" + index + "' not in range [0.." + (this._containerList.size() - 1) + "]");
        }
        
        return (org.exolab.castor.mapping.xml.Container) _containerList.get(index);
    }

    /**
     * Method getContainer.Returns the contents of the collection
     * in an Array.  <p>Note:  Just in case the collection contents
     * are changing in another thread, we pass a 0-length Array of
     * the correct type into the API call.  This way we <i>know</i>
     * that the Array returned is of exactly the correct length.
     * 
     * @return this collection as an Array
     */
    public org.exolab.castor.mapping.xml.Container[] getContainer(
    ) {
        org.exolab.castor.mapping.xml.Container[] array = new org.exolab.castor.mapping.xml.Container[0];
        return (org.exolab.castor.mapping.xml.Container[]) this._containerList.toArray(array);
    }

    /**
     * Method getContainerCount.
     * 
     * @return the size of this collection
     */
    public int getContainerCount(
    ) {
        return this._containerList.size();
    }

    /**
     * Method getFieldMapping.
     * 
     * @param index
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     * @return the value of the
     * org.exolab.castor.mapping.xml.FieldMapping at the given index
     */
    public org.exolab.castor.mapping.xml.FieldMapping getFieldMapping(
            final int index)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._fieldMappingList.size()) {
            throw new IndexOutOfBoundsException("getFieldMapping: Index value '" + index + "' not in range [0.." + (this._fieldMappingList.size() - 1) + "]");
        }
        
        return (org.exolab.castor.mapping.xml.FieldMapping) _fieldMappingList.get(index);
    }

    /**
     * Method getFieldMapping.Returns the contents of the
     * collection in an Array.  <p>Note:  Just in case the
     * collection contents are changing in another thread, we pass
     * a 0-length Array of the correct type into the API call. 
     * This way we <i>know</i> that the Array returned is of
     * exactly the correct length.
     * 
     * @return this collection as an Array
     */
    public org.exolab.castor.mapping.xml.FieldMapping[] getFieldMapping(
    ) {
        org.exolab.castor.mapping.xml.FieldMapping[] array = new org.exolab.castor.mapping.xml.FieldMapping[0];
        return (org.exolab.castor.mapping.xml.FieldMapping[]) this._fieldMappingList.toArray(array);
    }

    /**
     * Method getFieldMappingCount.
     * 
     * @return the size of this collection
     */
    public int getFieldMappingCount(
    ) {
        return this._fieldMappingList.size();
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
     * Method iterateContainer.
     * 
     * @return an Iterator over all possible elements in this
     * collection
     */
    public java.util.Iterator iterateContainer(
    ) {
        return this._containerList.iterator();
    }

    /**
     * Method iterateFieldMapping.
     * 
     * @return an Iterator over all possible elements in this
     * collection
     */
    public java.util.Iterator iterateFieldMapping(
    ) {
        return this._fieldMappingList.iterator();
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
    public void removeAllContainer(
    ) {
        this._containerList.clear();
    }

    /**
     */
    public void removeAllFieldMapping(
    ) {
        this._fieldMappingList.clear();
    }

    /**
     * Method removeContainer.
     * 
     * @param vContainer
     * @return true if the object was removed from the collection.
     */
    public boolean removeContainer(
            final org.exolab.castor.mapping.xml.Container vContainer) {
        boolean removed = _containerList.remove(vContainer);
        return removed;
    }

    /**
     * Method removeContainerAt.
     * 
     * @param index
     * @return the element removed from the collection
     */
    public org.exolab.castor.mapping.xml.Container removeContainerAt(
            final int index) {
        java.lang.Object obj = this._containerList.remove(index);
        return (org.exolab.castor.mapping.xml.Container) obj;
    }

    /**
     * Method removeFieldMapping.
     * 
     * @param vFieldMapping
     * @return true if the object was removed from the collection.
     */
    public boolean removeFieldMapping(
            final org.exolab.castor.mapping.xml.FieldMapping vFieldMapping) {
        boolean removed = _fieldMappingList.remove(vFieldMapping);
        return removed;
    }

    /**
     * Method removeFieldMappingAt.
     * 
     * @param index
     * @return the element removed from the collection
     */
    public org.exolab.castor.mapping.xml.FieldMapping removeFieldMappingAt(
            final int index) {
        java.lang.Object obj = this._fieldMappingList.remove(index);
        return (org.exolab.castor.mapping.xml.FieldMapping) obj;
    }

    /**
     * 
     * 
     * @param index
     * @param vContainer
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void setContainer(
            final int index,
            final org.exolab.castor.mapping.xml.Container vContainer)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._containerList.size()) {
            throw new IndexOutOfBoundsException("setContainer: Index value '" + index + "' not in range [0.." + (this._containerList.size() - 1) + "]");
        }
        
        this._containerList.set(index, vContainer);
    }

    /**
     * 
     * 
     * @param vContainerArray
     */
    public void setContainer(
            final org.exolab.castor.mapping.xml.Container[] vContainerArray) {
        //-- copy array
        _containerList.clear();
        
        for (int i = 0; i < vContainerArray.length; i++) {
                this._containerList.add(vContainerArray[i]);
        }
    }

    /**
     * 
     * 
     * @param index
     * @param vFieldMapping
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void setFieldMapping(
            final int index,
            final org.exolab.castor.mapping.xml.FieldMapping vFieldMapping)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._fieldMappingList.size()) {
            throw new IndexOutOfBoundsException("setFieldMapping: Index value '" + index + "' not in range [0.." + (this._fieldMappingList.size() - 1) + "]");
        }
        
        this._fieldMappingList.set(index, vFieldMapping);
    }

    /**
     * 
     * 
     * @param vFieldMappingArray
     */
    public void setFieldMapping(
            final org.exolab.castor.mapping.xml.FieldMapping[] vFieldMappingArray) {
        //-- copy array
        _fieldMappingList.clear();
        
        for (int i = 0; i < vFieldMappingArray.length; i++) {
                this._fieldMappingList.add(vFieldMappingArray[i]);
        }
    }

    /**
     * Method unmarshal.
     * 
     * @param reader
     * @throws org.exolab.castor.xml.MarshalException if object is
     * null or if any SAXException is thrown during marshaling
     * @throws org.exolab.castor.xml.ValidationException if this
     * object is an invalid instance according to the schema
     * @return the unmarshaled
     * org.exolab.castor.mapping.xml.ClassChoice
     */
    public static org.exolab.castor.mapping.xml.ClassChoice unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (org.exolab.castor.mapping.xml.ClassChoice) Unmarshaller.unmarshal(org.exolab.castor.mapping.xml.ClassChoice.class, reader);
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
