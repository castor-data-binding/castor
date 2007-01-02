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
 * A binding element is defined to express the binding between an
 * XML Schema Component
 *  and Java class OR a java interface or a java member class. The
 * XML Schema component can 
 *  be an element, an attribute, a complexType or a group.
 * Attribute cannot be mapped to 
 *  class, the reader of a binding file will take care that class
 * or interface are not
 *  used for component whose xml-type is attribute.
 *  
 * 
 * @version $Revision$ $Date$
 */
public class ComponentBindingType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _name.
     */
    private java.lang.String _name;

    /**
     * Field _componentBindingTypeChoice.
     */
    private org.exolab.castor.builder.binding.ComponentBindingTypeChoice
            _componentBindingTypeChoice;

    /**
     * Field _elementBindingList.
     */
    private java.util.List _elementBindingList;

    /**
     * Field _attributeBindingList.
     */
    private java.util.List _attributeBindingList;

    /**
     * Field _complexTypeBindingList.
     */
    private java.util.List _complexTypeBindingList;

    /**
     * Field _groupBindingList.
     */
    private java.util.List _groupBindingList;

    /**
     * Field _enumBindingList.
     */
    private java.util.List _enumBindingList;

    /**
     * Field _simpleTypeBindingList.
     */
    private java.util.List _simpleTypeBindingList;


      //----------------/
     //- Constructors -/
    //----------------/

    public ComponentBindingType() {
        super();
        this._elementBindingList = new java.util.ArrayList();
        this._attributeBindingList = new java.util.ArrayList();
        this._complexTypeBindingList = new java.util.ArrayList();
        this._groupBindingList = new java.util.ArrayList();
        this._enumBindingList = new java.util.ArrayList();
        this._simpleTypeBindingList = new java.util.ArrayList();
    } //-- org.exolab.castor.builder.binding.ComponentBindingType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * 
     * 
     * @param vAttributeBinding
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addAttributeBinding(
            final org.exolab.castor.builder.binding.ComponentBindingType vAttributeBinding)
    throws java.lang.IndexOutOfBoundsException {
        this._attributeBindingList.add(vAttributeBinding);
    } //-- void addAttributeBinding(org.exolab.castor.builder.binding.ComponentBindingType) 

    /**
     * 
     * 
     * @param index
     * @param vAttributeBinding
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addAttributeBinding(
            final int index,
            final org.exolab.castor.builder.binding.ComponentBindingType vAttributeBinding)
    throws java.lang.IndexOutOfBoundsException {
        this._attributeBindingList.add(index, vAttributeBinding);
    } //-- void addAttributeBinding(int, org.exolab.castor.builder.binding.ComponentBindingType) 

    /**
     * 
     * 
     * @param vComplexTypeBinding
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addComplexTypeBinding(
            final org.exolab.castor.builder.binding.ComponentBindingType vComplexTypeBinding)
    throws java.lang.IndexOutOfBoundsException {
        this._complexTypeBindingList.add(vComplexTypeBinding);
    } //-- void addComplexTypeBinding(org.exolab.castor.builder.binding.ComponentBindingType) 

    /**
     * 
     * 
     * @param index
     * @param vComplexTypeBinding
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addComplexTypeBinding(
            final int index,
            final org.exolab.castor.builder.binding.ComponentBindingType vComplexTypeBinding)
    throws java.lang.IndexOutOfBoundsException {
        this._complexTypeBindingList.add(index, vComplexTypeBinding);
    } //-- void addComplexTypeBinding(int, org.exolab.castor.builder.binding.ComponentBindingType) 

    /**
     * 
     * 
     * @param vElementBinding
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addElementBinding(
            final org.exolab.castor.builder.binding.ComponentBindingType vElementBinding)
    throws java.lang.IndexOutOfBoundsException {
        this._elementBindingList.add(vElementBinding);
    } //-- void addElementBinding(org.exolab.castor.builder.binding.ComponentBindingType) 

    /**
     * 
     * 
     * @param index
     * @param vElementBinding
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addElementBinding(
            final int index,
            final org.exolab.castor.builder.binding.ComponentBindingType vElementBinding)
    throws java.lang.IndexOutOfBoundsException {
        this._elementBindingList.add(index, vElementBinding);
    } //-- void addElementBinding(int, org.exolab.castor.builder.binding.ComponentBindingType) 

    /**
     * 
     * 
     * @param vEnumBinding
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addEnumBinding(
            final org.exolab.castor.builder.binding.ComponentBindingType vEnumBinding)
    throws java.lang.IndexOutOfBoundsException {
        this._enumBindingList.add(vEnumBinding);
    } //-- void addEnumBinding(org.exolab.castor.builder.binding.ComponentBindingType) 

    /**
     * 
     * 
     * @param index
     * @param vEnumBinding
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addEnumBinding(
            final int index,
            final org.exolab.castor.builder.binding.ComponentBindingType vEnumBinding)
    throws java.lang.IndexOutOfBoundsException {
        this._enumBindingList.add(index, vEnumBinding);
    } //-- void addEnumBinding(int, org.exolab.castor.builder.binding.ComponentBindingType) 

    /**
     * 
     * 
     * @param vGroupBinding
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addGroupBinding(
            final org.exolab.castor.builder.binding.ComponentBindingType vGroupBinding)
    throws java.lang.IndexOutOfBoundsException {
        this._groupBindingList.add(vGroupBinding);
    } //-- void addGroupBinding(org.exolab.castor.builder.binding.ComponentBindingType) 

    /**
     * 
     * 
     * @param index
     * @param vGroupBinding
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addGroupBinding(
            final int index,
            final org.exolab.castor.builder.binding.ComponentBindingType vGroupBinding)
    throws java.lang.IndexOutOfBoundsException {
        this._groupBindingList.add(index, vGroupBinding);
    } //-- void addGroupBinding(int, org.exolab.castor.builder.binding.ComponentBindingType) 

    /**
     * 
     * 
     * @param vSimpleTypeBinding
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addSimpleTypeBinding(
            final org.exolab.castor.builder.binding.ComponentBindingType vSimpleTypeBinding)
    throws java.lang.IndexOutOfBoundsException {
        this._simpleTypeBindingList.add(vSimpleTypeBinding);
    } //-- void addSimpleTypeBinding(org.exolab.castor.builder.binding.ComponentBindingType) 

    /**
     * 
     * 
     * @param index
     * @param vSimpleTypeBinding
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addSimpleTypeBinding(
            final int index,
            final org.exolab.castor.builder.binding.ComponentBindingType vSimpleTypeBinding)
    throws java.lang.IndexOutOfBoundsException {
        this._simpleTypeBindingList.add(index, vSimpleTypeBinding);
    } //-- void addSimpleTypeBinding(int, org.exolab.castor.builder.binding.ComponentBindingType) 

    /**
     * Method enumerateAttributeBinding.
     * 
     * @return an Enumeration over all possible elements of this
     * collection
     */
    public java.util.Enumeration enumerateAttributeBinding() {
        return java.util.Collections.enumeration(this._attributeBindingList);
    } //-- java.util.Enumeration enumerateAttributeBinding() 

    /**
     * Method enumerateComplexTypeBinding.
     * 
     * @return an Enumeration over all possible elements of this
     * collection
     */
    public java.util.Enumeration enumerateComplexTypeBinding() {
        return java.util.Collections.enumeration(this._complexTypeBindingList);
    } //-- java.util.Enumeration enumerateComplexTypeBinding() 

    /**
     * Method enumerateElementBinding.
     * 
     * @return an Enumeration over all possible elements of this
     * collection
     */
    public java.util.Enumeration enumerateElementBinding() {
        return java.util.Collections.enumeration(this._elementBindingList);
    } //-- java.util.Enumeration enumerateElementBinding() 

    /**
     * Method enumerateEnumBinding.
     * 
     * @return an Enumeration over all possible elements of this
     * collection
     */
    public java.util.Enumeration enumerateEnumBinding() {
        return java.util.Collections.enumeration(this._enumBindingList);
    } //-- java.util.Enumeration enumerateEnumBinding() 

    /**
     * Method enumerateGroupBinding.
     * 
     * @return an Enumeration over all possible elements of this
     * collection
     */
    public java.util.Enumeration enumerateGroupBinding() {
        return java.util.Collections.enumeration(this._groupBindingList);
    } //-- java.util.Enumeration enumerateGroupBinding() 

    /**
     * Method enumerateSimpleTypeBinding.
     * 
     * @return an Enumeration over all possible elements of this
     * collection
     */
    public java.util.Enumeration enumerateSimpleTypeBinding() {
        return java.util.Collections.enumeration(this._simpleTypeBindingList);
    } //-- java.util.Enumeration enumerateSimpleTypeBinding() 

    /**
     * Method getAttributeBinding.
     * 
     * @param index
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     * @return the value of the
     * org.exolab.castor.builder.binding.ComponentBindingType at
     * the given index
     */
    public org.exolab.castor.builder.binding.ComponentBindingType getAttributeBinding(
            final int index)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._attributeBindingList.size()) {
            throw new IndexOutOfBoundsException(
                    "getAttributeBinding: Index value '" + index
                    + "' not in range [0.." + (this._attributeBindingList.size() - 1) + "]");
        }
        
        return (org.exolab.castor.builder.binding.ComponentBindingType)
                _attributeBindingList.get(index);
    } //-- org.exolab.castor.builder.binding.ComponentBindingType getAttributeBinding(int) 

    /**
     * Method getAttributeBinding.
     * 
     * @return this collection as an Array
     */
    public org.exolab.castor.builder.binding.ComponentBindingType[] getAttributeBinding() {
        int size = this._attributeBindingList.size();
        org.exolab.castor.builder.binding.ComponentBindingType[] array =
            new org.exolab.castor.builder.binding.ComponentBindingType[size];
        java.util.Iterator iter = _attributeBindingList.iterator();
        for (int index = 0; index < size; index++) {
            array[index] = (org.exolab.castor.builder.binding.ComponentBindingType) iter.next();
        }
        
        return array;
    } //-- org.exolab.castor.builder.binding.ComponentBindingType[] getAttributeBinding() 

    /**
     * Method getAttributeBindingCount.
     * 
     * @return the size of this collection
     */
    public int getAttributeBindingCount() {
        return this._attributeBindingList.size();
    } //-- int getAttributeBindingCount() 

    /**
     * Method getComplexTypeBinding.
     * 
     * @param index
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     * @return the value of the
     * org.exolab.castor.builder.binding.ComponentBindingType at
     * the given index
     */
    public org.exolab.castor.builder.binding.ComponentBindingType getComplexTypeBinding(
            final int index)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._complexTypeBindingList.size()) {
            throw new IndexOutOfBoundsException(
                    "getComplexTypeBinding: Index value '" + index
                    + "' not in range [0.." + (this._complexTypeBindingList.size() - 1) + "]");
        }
        
        return (org.exolab.castor.builder.binding.ComponentBindingType)
                _complexTypeBindingList.get(index);
    } //-- org.exolab.castor.builder.binding.ComponentBindingType getComplexTypeBinding(int) 

    /**
     * Method getComplexTypeBinding.
     * 
     * @return this collection as an Array
     */
    public org.exolab.castor.builder.binding.ComponentBindingType[] getComplexTypeBinding() {
        int size = this._complexTypeBindingList.size();
        org.exolab.castor.builder.binding.ComponentBindingType[] array =
            new org.exolab.castor.builder.binding.ComponentBindingType[size];
        java.util.Iterator iter = _complexTypeBindingList.iterator();
        for (int index = 0; index < size; index++) {
            array[index] = (org.exolab.castor.builder.binding.ComponentBindingType) iter.next();
        }
        
        return array;
    } //-- org.exolab.castor.builder.binding.ComponentBindingType[] getComplexTypeBinding() 

    /**
     * Method getComplexTypeBindingCount.
     * 
     * @return the size of this collection
     */
    public int getComplexTypeBindingCount() {
        return this._complexTypeBindingList.size();
    } //-- int getComplexTypeBindingCount() 

    /**
     * Returns the value of field 'componentBindingTypeChoice'.
     * 
     * @return the value of field 'ComponentBindingTypeChoice'.
     */
    public org.exolab.castor.builder.binding.ComponentBindingTypeChoice
            getComponentBindingTypeChoice() {
        return this._componentBindingTypeChoice;
    } 

    /**
     * Method getElementBinding.
     * 
     * @param index
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     * @return the value of the
     * org.exolab.castor.builder.binding.ComponentBindingType at
     * the given index
     */
    public org.exolab.castor.builder.binding.ComponentBindingType getElementBinding(final int index)
        throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._elementBindingList.size()) {
            throw new IndexOutOfBoundsException(
                    "getElementBinding: Index value '" + index
                    + "' not in range [0.." + (this._elementBindingList.size() - 1) + "]");
        }
        
        return (org.exolab.castor.builder.binding.ComponentBindingType)
                _elementBindingList.get(index);
    } //-- org.exolab.castor.builder.binding.ComponentBindingType getElementBinding(int) 

    /**
     * Method getElementBinding.
     * 
     * @return this collection as an Array
     */
    public org.exolab.castor.builder.binding.ComponentBindingType[] getElementBinding() {
        int size = this._elementBindingList.size();
        org.exolab.castor.builder.binding.ComponentBindingType[] array =
            new org.exolab.castor.builder.binding.ComponentBindingType[size];
        java.util.Iterator iter = _elementBindingList.iterator();
        for (int index = 0; index < size; index++) {
            array[index] = (org.exolab.castor.builder.binding.ComponentBindingType) iter.next();
        }
        
        return array;
    } //-- org.exolab.castor.builder.binding.ComponentBindingType[] getElementBinding() 

    /**
     * Method getElementBindingCount.
     * 
     * @return the size of this collection
     */
    public int getElementBindingCount() {
        return this._elementBindingList.size();
    } //-- int getElementBindingCount() 

    /**
     * Method getEnumBinding.
     * 
     * @param index
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     * @return the value of the
     * org.exolab.castor.builder.binding.ComponentBindingType at
     * the given index
     */
    public org.exolab.castor.builder.binding.ComponentBindingType getEnumBinding(final int index)
        throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._enumBindingList.size()) {
            throw new IndexOutOfBoundsException(
                    "getEnumBinding: Index value '" + index
                    + "' not in range [0.." + (this._enumBindingList.size() - 1) + "]");
        }
        
        return (org.exolab.castor.builder.binding.ComponentBindingType) _enumBindingList.get(index);
    } //-- org.exolab.castor.builder.binding.ComponentBindingType getEnumBinding(int) 

    /**
     * Method getEnumBinding.
     * 
     * @return this collection as an Array
     */
    public org.exolab.castor.builder.binding.ComponentBindingType[] getEnumBinding() {
        int size = this._enumBindingList.size();
        org.exolab.castor.builder.binding.ComponentBindingType[] array =
            new org.exolab.castor.builder.binding.ComponentBindingType[size];
        java.util.Iterator iter = _enumBindingList.iterator();
        for (int index = 0; index < size; index++) {
            array[index] = (org.exolab.castor.builder.binding.ComponentBindingType) iter.next();
        }
        
        return array;
    } //-- org.exolab.castor.builder.binding.ComponentBindingType[] getEnumBinding() 

    /**
     * Method getEnumBindingCount.
     * 
     * @return the size of this collection
     */
    public int getEnumBindingCount() {
        return this._enumBindingList.size();
    } //-- int getEnumBindingCount() 

    /**
     * Method getGroupBinding.
     * 
     * @param index
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     * @return the value of the
     * org.exolab.castor.builder.binding.ComponentBindingType at
     * the given index
     */
    public org.exolab.castor.builder.binding.ComponentBindingType getGroupBinding(final int index)
        throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._groupBindingList.size()) {
            throw new IndexOutOfBoundsException(
                    "getGroupBinding: Index value '" + index
                    + "' not in range [0.." + (this._groupBindingList.size() - 1) + "]");
        }
        
        return (org.exolab.castor.builder.binding.ComponentBindingType)
                _groupBindingList.get(index);
    } //-- org.exolab.castor.builder.binding.ComponentBindingType getGroupBinding(int) 

    /**
     * Method getGroupBinding.
     * 
     * @return this collection as an Array
     */
    public org.exolab.castor.builder.binding.ComponentBindingType[] getGroupBinding() {
        int size = this._groupBindingList.size();
        org.exolab.castor.builder.binding.ComponentBindingType[] array =
            new org.exolab.castor.builder.binding.ComponentBindingType[size];
        java.util.Iterator iter = _groupBindingList.iterator();
        for (int index = 0; index < size; index++) {
            array[index] = (org.exolab.castor.builder.binding.ComponentBindingType) iter.next();
        }
        
        return array;
    } //-- org.exolab.castor.builder.binding.ComponentBindingType[] getGroupBinding() 

    /**
     * Method getGroupBindingCount.
     * 
     * @return the size of this collection
     */
    public int getGroupBindingCount() {
        return this._groupBindingList.size();
    } //-- int getGroupBindingCount() 

    /**
     * Returns the value of field 'name'.
     * 
     * @return the value of field 'Name'.
     */
    public java.lang.String getName() {
        return this._name;
    } //-- java.lang.String getName() 

    /**
     * Method getSimpleTypeBinding.
     * 
     * @param index
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     * @return the value of the
     * org.exolab.castor.builder.binding.ComponentBindingType at
     * the given index
     */
    public org.exolab.castor.builder.binding.ComponentBindingType getSimpleTypeBinding(
            final int index)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._simpleTypeBindingList.size()) {
            throw new IndexOutOfBoundsException(
                    "getSimpleTypeBinding: Index value '" + index
                    + "' not in range [0.." + (this._simpleTypeBindingList.size() - 1) + "]");
        }
        
        return (org.exolab.castor.builder.binding.ComponentBindingType)
                _simpleTypeBindingList.get(index);
    } //-- org.exolab.castor.builder.binding.ComponentBindingType getSimpleTypeBinding(int) 

    /**
     * Method getSimpleTypeBinding.
     * 
     * @return this collection as an Array
     */
    public org.exolab.castor.builder.binding.ComponentBindingType[] getSimpleTypeBinding() {
        int size = this._simpleTypeBindingList.size();
        org.exolab.castor.builder.binding.ComponentBindingType[] array =
            new org.exolab.castor.builder.binding.ComponentBindingType[size];
        java.util.Iterator iter = _simpleTypeBindingList.iterator();
        for (int index = 0; index < size; index++) {
            array[index] = (org.exolab.castor.builder.binding.ComponentBindingType) iter.next();
        }
        
        return array;
    } //-- org.exolab.castor.builder.binding.ComponentBindingType[] getSimpleTypeBinding() 

    /**
     * Method getSimpleTypeBindingCount.
     * 
     * @return the size of this collection
     */
    public int getSimpleTypeBindingCount() {
        return this._simpleTypeBindingList.size();
    } //-- int getSimpleTypeBindingCount() 

    /**
     * Method isValid.
     * 
     * @return true if this object is valid according to the schema
     */
    public boolean isValid() {
        try {
            validate();
        } catch (org.exolab.castor.xml.ValidationException vex) {
            return false;
        }
        return true;
    } //-- boolean isValid() 

    /**
     * Method iterateAttributeBinding.
     * 
     * @return an Iterator over all possible elements in this
     * collection
     */
    public java.util.Iterator iterateAttributeBinding() {
        return this._attributeBindingList.iterator();
    } //-- java.util.Iterator iterateAttributeBinding() 

    /**
     * Method iterateComplexTypeBinding.
     * 
     * @return an Iterator over all possible elements in this
     * collection
     */
    public java.util.Iterator iterateComplexTypeBinding() {
        return this._complexTypeBindingList.iterator();
    } //-- java.util.Iterator iterateComplexTypeBinding() 

    /**
     * Method iterateElementBinding.
     * 
     * @return an Iterator over all possible elements in this
     * collection
     */
    public java.util.Iterator iterateElementBinding() {
        return this._elementBindingList.iterator();
    } //-- java.util.Iterator iterateElementBinding() 

    /**
     * Method iterateEnumBinding.
     * 
     * @return an Iterator over all possible elements in this
     * collection
     */
    public java.util.Iterator iterateEnumBinding() {
        return this._enumBindingList.iterator();
    } //-- java.util.Iterator iterateEnumBinding() 

    /**
     * Method iterateGroupBinding.
     * 
     * @return an Iterator over all possible elements in this
     * collection
     */
    public java.util.Iterator iterateGroupBinding() {
        return this._groupBindingList.iterator();
    } //-- java.util.Iterator iterateGroupBinding() 

    /**
     * Method iterateSimpleTypeBinding.
     * 
     * @return an Iterator over all possible elements in this
     * collection
     */
    public java.util.Iterator iterateSimpleTypeBinding() {
        return this._simpleTypeBindingList.iterator();
    } //-- java.util.Iterator iterateSimpleTypeBinding() 

    /**
     * 
     * 
     * @param out
     * @throws org.exolab.castor.xml.MarshalException if object is
     * null or if any SAXException is thrown during marshaling
     * @throws org.exolab.castor.xml.ValidationException if this
     * object is an invalid instance according to the schema
     */
    public void marshal(final java.io.Writer out)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
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
    public void marshal(final org.xml.sax.ContentHandler handler)
    throws java.io.IOException, org.exolab.castor.xml.MarshalException,
           org.exolab.castor.xml.ValidationException {
        Marshaller.marshal(this, handler);
    } //-- void marshal(org.xml.sax.ContentHandler) 

    /**
     */
    public void removeAllAttributeBinding() {
        this._attributeBindingList.clear();
    } //-- void removeAllAttributeBinding() 

    /**
     */
    public void removeAllComplexTypeBinding() {
        this._complexTypeBindingList.clear();
    } //-- void removeAllComplexTypeBinding() 

    /**
     */
    public void removeAllElementBinding() {
        this._elementBindingList.clear();
    } //-- void removeAllElementBinding() 

    /**
     */
    public void removeAllEnumBinding() {
        this._enumBindingList.clear();
    } //-- void removeAllEnumBinding() 

    /**
     */
    public void removeAllGroupBinding() {
        this._groupBindingList.clear();
    } //-- void removeAllGroupBinding() 

    /**
     */
    public void removeAllSimpleTypeBinding() {
        this._simpleTypeBindingList.clear();
    } //-- void removeAllSimpleTypeBinding() 

    /**
     * Method removeAttributeBinding.
     * 
     * @param vAttributeBinding
     * @return true if the object was removed from the collection.
     */
    public boolean removeAttributeBinding(
            final org.exolab.castor.builder.binding.ComponentBindingType vAttributeBinding) {
        boolean removed = _attributeBindingList.remove(vAttributeBinding);
        return removed;
    } //-- boolean removeAttributeBinding(org.exolab.castor.builder.binding.ComponentBindingType) 

    /**
     * Method removeAttributeBindingAt.
     * 
     * @param index
     * @return the element removed from the collection
     */
    public org.exolab.castor.builder.binding.ComponentBindingType removeAttributeBindingAt(
            final int index) {
        Object obj = this._attributeBindingList.remove(index);
        return (org.exolab.castor.builder.binding.ComponentBindingType) obj;
    } //-- org.exolab.castor.builder.binding.ComponentBindingType removeAttributeBindingAt(int) 

    /**
     * Method removeComplexTypeBinding.
     * 
     * @param vComplexTypeBinding
     * @return true if the object was removed from the collection.
     */
    public boolean removeComplexTypeBinding(
            final org.exolab.castor.builder.binding.ComponentBindingType vComplexTypeBinding) {
        boolean removed = _complexTypeBindingList.remove(vComplexTypeBinding);
        return removed;
    } //-- boolean removeComplexTypeBinding(org.exolab.castor.builder.binding.ComponentBindingType) 

    /**
     * Method removeComplexTypeBindingAt.
     * 
     * @param index
     * @return the element removed from the collection
     */
    public org.exolab.castor.builder.binding.ComponentBindingType removeComplexTypeBindingAt(
            final int index) {
        Object obj = this._complexTypeBindingList.remove(index);
        return (org.exolab.castor.builder.binding.ComponentBindingType) obj;
    } //-- org.exolab.castor.builder.binding.ComponentBindingType removeComplexTypeBindingAt(int) 

    /**
     * Method removeElementBinding.
     * 
     * @param vElementBinding
     * @return true if the object was removed from the collection.
     */
    public boolean removeElementBinding(
            final org.exolab.castor.builder.binding.ComponentBindingType vElementBinding) {
        boolean removed = _elementBindingList.remove(vElementBinding);
        return removed;
    } //-- boolean removeElementBinding(org.exolab.castor.builder.binding.ComponentBindingType) 

    /**
     * Method removeElementBindingAt.
     * 
     * @param index
     * @return the element removed from the collection
     */
    public org.exolab.castor.builder.binding.ComponentBindingType removeElementBindingAt(
            final int index) {
        Object obj = this._elementBindingList.remove(index);
        return (org.exolab.castor.builder.binding.ComponentBindingType) obj;
    } //-- org.exolab.castor.builder.binding.ComponentBindingType removeElementBindingAt(int) 

    /**
     * Method removeEnumBinding.
     * 
     * @param vEnumBinding
     * @return true if the object was removed from the collection.
     */
    public boolean removeEnumBinding(
            final org.exolab.castor.builder.binding.ComponentBindingType vEnumBinding) {
        boolean removed = _enumBindingList.remove(vEnumBinding);
        return removed;
    } //-- boolean removeEnumBinding(org.exolab.castor.builder.binding.ComponentBindingType) 

    /**
     * Method removeEnumBindingAt.
     * 
     * @param index
     * @return the element removed from the collection
     */
    public org.exolab.castor.builder.binding.ComponentBindingType removeEnumBindingAt(
            final int index) {
        Object obj = this._enumBindingList.remove(index);
        return (org.exolab.castor.builder.binding.ComponentBindingType) obj;
    } //-- org.exolab.castor.builder.binding.ComponentBindingType removeEnumBindingAt(int) 

    /**
     * Method removeGroupBinding.
     * 
     * @param vGroupBinding
     * @return true if the object was removed from the collection.
     */
    public boolean removeGroupBinding(
            final org.exolab.castor.builder.binding.ComponentBindingType vGroupBinding) {
        boolean removed = _groupBindingList.remove(vGroupBinding);
        return removed;
    } //-- boolean removeGroupBinding(org.exolab.castor.builder.binding.ComponentBindingType) 

    /**
     * Method removeGroupBindingAt.
     * 
     * @param index
     * @return the element removed from the collection
     */
    public org.exolab.castor.builder.binding.ComponentBindingType removeGroupBindingAt(
            final int index) {
        Object obj = this._groupBindingList.remove(index);
        return (org.exolab.castor.builder.binding.ComponentBindingType) obj;
    } //-- org.exolab.castor.builder.binding.ComponentBindingType removeGroupBindingAt(int) 

    /**
     * Method removeSimpleTypeBinding.
     * 
     * @param vSimpleTypeBinding
     * @return true if the object was removed from the collection.
     */
    public boolean removeSimpleTypeBinding(
            final org.exolab.castor.builder.binding.ComponentBindingType vSimpleTypeBinding) {
        boolean removed = _simpleTypeBindingList.remove(vSimpleTypeBinding);
        return removed;
    } //-- boolean removeSimpleTypeBinding(org.exolab.castor.builder.binding.ComponentBindingType) 

    /**
     * Method removeSimpleTypeBindingAt.
     * 
     * @param index
     * @return the element removed from the collection
     */
    public org.exolab.castor.builder.binding.ComponentBindingType removeSimpleTypeBindingAt(
            final int index) {
        Object obj = this._simpleTypeBindingList.remove(index);
        return (org.exolab.castor.builder.binding.ComponentBindingType) obj;
    } //-- org.exolab.castor.builder.binding.ComponentBindingType removeSimpleTypeBindingAt(int) 

    /**
     * 
     * 
     * @param index
     * @param vAttributeBinding
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void setAttributeBinding(
            final int index,
            final org.exolab.castor.builder.binding.ComponentBindingType vAttributeBinding)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._attributeBindingList.size()) {
            throw new IndexOutOfBoundsException(
                    "setAttributeBinding: Index value '" + index
                    + "' not in range [0.." + (this._attributeBindingList.size() - 1) + "]");
        }
        
        this._attributeBindingList.set(index, vAttributeBinding);
    } //-- void setAttributeBinding(int, org.exolab.castor.builder.binding.ComponentBindingType) 

    /**
     * 
     * 
     * @param vAttributeBindingArray
     */
    public void setAttributeBinding(
            final org.exolab.castor.builder.binding.ComponentBindingType[] vAttributeBindingArray) {
        //-- copy array
        _attributeBindingList.clear();
        
        for (int i = 0; i < vAttributeBindingArray.length; i++) {
                this._attributeBindingList.add(vAttributeBindingArray[i]);
        }
    } //-- void setAttributeBinding(org.exolab.castor.builder.binding.ComponentBindingType) 

    /**
     * 
     * 
     * @param index
     * @param vComplexTypeBinding
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void setComplexTypeBinding(
            final int index,
            final org.exolab.castor.builder.binding.ComponentBindingType vComplexTypeBinding)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._complexTypeBindingList.size()) {
            throw new IndexOutOfBoundsException(
                    "setComplexTypeBinding: Index value '" + index
                    + "' not in range [0.." + (this._complexTypeBindingList.size() - 1) + "]");
        }
        
        this._complexTypeBindingList.set(index, vComplexTypeBinding);
    } //-- void setComplexTypeBinding(int, org.exolab.castor.builder.binding.ComponentBindingType) 

    /**
     * 
     * 
     * @param vComplexTypeBindingArray
     */
    public void setComplexTypeBinding(
            final org.exolab.castor.builder.binding.ComponentBindingType[]
                  vComplexTypeBindingArray) {
        //-- copy array
        _complexTypeBindingList.clear();
        
        for (int i = 0; i < vComplexTypeBindingArray.length; i++) {
                this._complexTypeBindingList.add(vComplexTypeBindingArray[i]);
        }
    } //-- void setComplexTypeBinding(org.exolab.castor.builder.binding.ComponentBindingType) 

    /**
     * Sets the value of field 'componentBindingTypeChoice'.
     * 
     * @param componentBindingTypeChoice the value of field
     * 'componentBindingTypeChoice'.
     */
    public void setComponentBindingTypeChoice(
            final org.exolab.castor.builder.binding.ComponentBindingTypeChoice
                  componentBindingTypeChoice) {
        this._componentBindingTypeChoice = componentBindingTypeChoice;
    } 

    /**
     * 
     * 
     * @param index
     * @param vElementBinding
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void setElementBinding(
            final int index,
            final org.exolab.castor.builder.binding.ComponentBindingType vElementBinding)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._elementBindingList.size()) {
            throw new IndexOutOfBoundsException(
                    "setElementBinding: Index value '" + index
                    + "' not in range [0.." + (this._elementBindingList.size() - 1) + "]");
        }
        
        this._elementBindingList.set(index, vElementBinding);
    } //-- void setElementBinding(int, org.exolab.castor.builder.binding.ComponentBindingType) 

    /**
     * 
     * 
     * @param vElementBindingArray
     */
    public void setElementBinding(
            final org.exolab.castor.builder.binding.ComponentBindingType[] vElementBindingArray) {
        //-- copy array
        _elementBindingList.clear();
        
        for (int i = 0; i < vElementBindingArray.length; i++) {
                this._elementBindingList.add(vElementBindingArray[i]);
        }
    } //-- void setElementBinding(org.exolab.castor.builder.binding.ComponentBindingType) 

    /**
     * 
     * 
     * @param index
     * @param vEnumBinding
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void setEnumBinding(
            final int index,
            final org.exolab.castor.builder.binding.ComponentBindingType vEnumBinding)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._enumBindingList.size()) {
            throw new IndexOutOfBoundsException(
                    "setEnumBinding: Index value '" + index
                    + "' not in range [0.." + (this._enumBindingList.size() - 1) + "]");
        }
        
        this._enumBindingList.set(index, vEnumBinding);
    } //-- void setEnumBinding(int, org.exolab.castor.builder.binding.ComponentBindingType) 

    /**
     * 
     * 
     * @param vEnumBindingArray
     */
    public void setEnumBinding(
            final org.exolab.castor.builder.binding.ComponentBindingType[] vEnumBindingArray) {
        //-- copy array
        _enumBindingList.clear();
        
        for (int i = 0; i < vEnumBindingArray.length; i++) {
                this._enumBindingList.add(vEnumBindingArray[i]);
        }
    } //-- void setEnumBinding(org.exolab.castor.builder.binding.ComponentBindingType) 

    /**
     * 
     * 
     * @param index
     * @param vGroupBinding
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void setGroupBinding(
            final int index,
            final org.exolab.castor.builder.binding.ComponentBindingType vGroupBinding)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._groupBindingList.size()) {
            throw new IndexOutOfBoundsException(
                    "setGroupBinding: Index value '" + index
                    + "' not in range [0.." + (this._groupBindingList.size() - 1) + "]");
        }
        
        this._groupBindingList.set(index, vGroupBinding);
    } //-- void setGroupBinding(int, org.exolab.castor.builder.binding.ComponentBindingType) 

    /**
     * 
     * 
     * @param vGroupBindingArray
     */
    public void setGroupBinding(
            final org.exolab.castor.builder.binding.ComponentBindingType[] vGroupBindingArray) {
        //-- copy array
        _groupBindingList.clear();
        
        for (int i = 0; i < vGroupBindingArray.length; i++) {
                this._groupBindingList.add(vGroupBindingArray[i]);
        }
    } //-- void setGroupBinding(org.exolab.castor.builder.binding.ComponentBindingType) 

    /**
     * Sets the value of field 'name'.
     * 
     * @param name the value of field 'name'.
     */
    public void setName(final java.lang.String name) {
        this._name = name;
    } //-- void setName(java.lang.String) 

    /**
     * 
     * 
     * @param index
     * @param vSimpleTypeBinding
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void setSimpleTypeBinding(
            final int index,
            final org.exolab.castor.builder.binding.ComponentBindingType vSimpleTypeBinding)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._simpleTypeBindingList.size()) {
            throw new IndexOutOfBoundsException(
                    "setSimpleTypeBinding: Index value '" + index
                    + "' not in range [0.." + (this._simpleTypeBindingList.size() - 1) + "]");
        }
        
        this._simpleTypeBindingList.set(index, vSimpleTypeBinding);
    } //-- void setSimpleTypeBinding(int, org.exolab.castor.builder.binding.ComponentBindingType) 

    /**
     * 
     * 
     * @param vSimpleTypeBindingArray
     */
    public void setSimpleTypeBinding(
            final org.exolab.castor.builder.binding.ComponentBindingType[]
                  vSimpleTypeBindingArray) {
        //-- copy array
        _simpleTypeBindingList.clear();
        
        for (int i = 0; i < vSimpleTypeBindingArray.length; i++) {
                this._simpleTypeBindingList.add(vSimpleTypeBindingArray[i]);
        }
    } //-- void setSimpleTypeBinding(org.exolab.castor.builder.binding.ComponentBindingType) 

    /**
     * Method unmarshalComponentBindingType.
     * 
     * @param reader
     * @throws org.exolab.castor.xml.MarshalException if object is
     * null or if any SAXException is thrown during marshaling
     * @throws org.exolab.castor.xml.ValidationException if this
     * object is an invalid instance according to the schema
     * @return the unmarshaled
     * org.exolab.castor.builder.binding.ComponentBindingType
     */
    public static org.exolab.castor.builder.binding.ComponentBindingType
            unmarshalComponentBindingType(final java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (org.exolab.castor.builder.binding.ComponentBindingType) Unmarshaller.unmarshal(
                org.exolab.castor.builder.binding.ComponentBindingType.class, reader);
    } 

    /**
     * 
     * 
     * @throws org.exolab.castor.xml.ValidationException if this
     * object is an invalid instance according to the schema
     */
    public void validate()
        throws org.exolab.castor.xml.ValidationException {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    } //-- void validate() 

}
