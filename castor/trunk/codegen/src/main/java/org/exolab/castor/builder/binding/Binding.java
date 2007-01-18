/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.1-M2</a>, using an XML
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
 * The root element that contains the different binding elements.
 *  The binding file is written from a schema point of view and
 * follows the
 *  structure of an XML Schema.
 *  The root element can also be used to configure the default
 * binding type. 
 *  
 * 
 * @version $Revision$ $Date$
 */
public class Binding implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _defaultBindingType.
     */
    private org.exolab.castor.builder.binding.types.BindingType _defaultBindingType;

    /**
     * Field _includeList.
     */
    private java.util.List _includeList;

    /**
     * Field _packageList.
     */
    private java.util.List _packageList;

    /**
     * Field _namingXML.
     */
    private org.exolab.castor.builder.binding.NamingXMLType _namingXML;

    /**
     * Field _componentBindingList.
     */
    private java.util.List _componentBindingList;

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

    public Binding() {
        super();
        this._includeList = new java.util.ArrayList();
        this._packageList = new java.util.ArrayList();
        this._componentBindingList = new java.util.ArrayList();
        this._elementBindingList = new java.util.ArrayList();
        this._attributeBindingList = new java.util.ArrayList();
        this._complexTypeBindingList = new java.util.ArrayList();
        this._groupBindingList = new java.util.ArrayList();
        this._enumBindingList = new java.util.ArrayList();
        this._simpleTypeBindingList = new java.util.ArrayList();
    }


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
    }

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
    }

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
    }

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
    }

    /**
     * 
     * 
     * @param vComponentBinding
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addComponentBinding(
            final org.exolab.castor.builder.binding.ComponentBindingType vComponentBinding)
    throws java.lang.IndexOutOfBoundsException {
        this._componentBindingList.add(vComponentBinding);
    }

    /**
     * 
     * 
     * @param index
     * @param vComponentBinding
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addComponentBinding(
            final int index,
            final org.exolab.castor.builder.binding.ComponentBindingType vComponentBinding)
    throws java.lang.IndexOutOfBoundsException {
        this._componentBindingList.add(index, vComponentBinding);
    }

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
    }

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
    }

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
    }

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
    }

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
    }

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
    }

    /**
     * 
     * 
     * @param vInclude
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addInclude(
            final org.exolab.castor.builder.binding.IncludeType vInclude)
    throws java.lang.IndexOutOfBoundsException {
        this._includeList.add(vInclude);
    }

    /**
     * 
     * 
     * @param index
     * @param vInclude
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addInclude(
            final int index,
            final org.exolab.castor.builder.binding.IncludeType vInclude)
    throws java.lang.IndexOutOfBoundsException {
        this._includeList.add(index, vInclude);
    }

    /**
     * 
     * 
     * @param vPackage
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addPackage(
            final org.exolab.castor.builder.binding.PackageType vPackage)
    throws java.lang.IndexOutOfBoundsException {
        this._packageList.add(vPackage);
    }

    /**
     * 
     * 
     * @param index
     * @param vPackage
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addPackage(
            final int index,
            final org.exolab.castor.builder.binding.PackageType vPackage)
    throws java.lang.IndexOutOfBoundsException {
        this._packageList.add(index, vPackage);
    }

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
    }

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
    }

    /**
     * Method enumerateAttributeBinding.
     * 
     * @return an Enumeration over all possible elements of this
     * collection
     */
    public java.util.Enumeration enumerateAttributeBinding(
    ) {
        return java.util.Collections.enumeration(this._attributeBindingList);
    }

    /**
     * Method enumerateComplexTypeBinding.
     * 
     * @return an Enumeration over all possible elements of this
     * collection
     */
    public java.util.Enumeration enumerateComplexTypeBinding(
    ) {
        return java.util.Collections.enumeration(this._complexTypeBindingList);
    }

    /**
     * Method enumerateComponentBinding.
     * 
     * @return an Enumeration over all possible elements of this
     * collection
     */
    public java.util.Enumeration enumerateComponentBinding(
    ) {
        return java.util.Collections.enumeration(this._componentBindingList);
    }

    /**
     * Method enumerateElementBinding.
     * 
     * @return an Enumeration over all possible elements of this
     * collection
     */
    public java.util.Enumeration enumerateElementBinding(
    ) {
        return java.util.Collections.enumeration(this._elementBindingList);
    }

    /**
     * Method enumerateEnumBinding.
     * 
     * @return an Enumeration over all possible elements of this
     * collection
     */
    public java.util.Enumeration enumerateEnumBinding(
    ) {
        return java.util.Collections.enumeration(this._enumBindingList);
    }

    /**
     * Method enumerateGroupBinding.
     * 
     * @return an Enumeration over all possible elements of this
     * collection
     */
    public java.util.Enumeration enumerateGroupBinding(
    ) {
        return java.util.Collections.enumeration(this._groupBindingList);
    }

    /**
     * Method enumerateInclude.
     * 
     * @return an Enumeration over all possible elements of this
     * collection
     */
    public java.util.Enumeration enumerateInclude(
    ) {
        return java.util.Collections.enumeration(this._includeList);
    }

    /**
     * Method enumeratePackage.
     * 
     * @return an Enumeration over all possible elements of this
     * collection
     */
    public java.util.Enumeration enumeratePackage(
    ) {
        return java.util.Collections.enumeration(this._packageList);
    }

    /**
     * Method enumerateSimpleTypeBinding.
     * 
     * @return an Enumeration over all possible elements of this
     * collection
     */
    public java.util.Enumeration enumerateSimpleTypeBinding(
    ) {
        return java.util.Collections.enumeration(this._simpleTypeBindingList);
    }

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
            throw new IndexOutOfBoundsException("getAttributeBinding: Index value '" + index + "' not in range [0.." + (this._attributeBindingList.size() - 1) + "]");
        }
        
        return (org.exolab.castor.builder.binding.ComponentBindingType) _attributeBindingList.get(index);
    }

    /**
     * Method getAttributeBinding.Returns the contents of the
     * collection in an Array.  <p>Note:  Just in case the
     * collection contents are changing in another thread, we pass
     * a 0-length Array of the correct type into the API call. 
     * This way we <i>know</i> that the Array returned is of
     * exactly the correct length.
     * 
     * @return this collection as an Array
     */
    public org.exolab.castor.builder.binding.ComponentBindingType[] getAttributeBinding(
    ) {
        org.exolab.castor.builder.binding.ComponentBindingType[] array = new org.exolab.castor.builder.binding.ComponentBindingType[0];
        return (org.exolab.castor.builder.binding.ComponentBindingType[]) this._attributeBindingList.toArray(array);
    }

    /**
     * Method getAttributeBindingCount.
     * 
     * @return the size of this collection
     */
    public int getAttributeBindingCount(
    ) {
        return this._attributeBindingList.size();
    }

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
            throw new IndexOutOfBoundsException("getComplexTypeBinding: Index value '" + index + "' not in range [0.." + (this._complexTypeBindingList.size() - 1) + "]");
        }
        
        return (org.exolab.castor.builder.binding.ComponentBindingType) _complexTypeBindingList.get(index);
    }

    /**
     * Method getComplexTypeBinding.Returns the contents of the
     * collection in an Array.  <p>Note:  Just in case the
     * collection contents are changing in another thread, we pass
     * a 0-length Array of the correct type into the API call. 
     * This way we <i>know</i> that the Array returned is of
     * exactly the correct length.
     * 
     * @return this collection as an Array
     */
    public org.exolab.castor.builder.binding.ComponentBindingType[] getComplexTypeBinding(
    ) {
        org.exolab.castor.builder.binding.ComponentBindingType[] array = new org.exolab.castor.builder.binding.ComponentBindingType[0];
        return (org.exolab.castor.builder.binding.ComponentBindingType[]) this._complexTypeBindingList.toArray(array);
    }

    /**
     * Method getComplexTypeBindingCount.
     * 
     * @return the size of this collection
     */
    public int getComplexTypeBindingCount(
    ) {
        return this._complexTypeBindingList.size();
    }

    /**
     * Method getComponentBinding.
     * 
     * @param index
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     * @return the value of the
     * org.exolab.castor.builder.binding.ComponentBindingType at
     * the given index
     */
    public org.exolab.castor.builder.binding.ComponentBindingType getComponentBinding(
            final int index)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._componentBindingList.size()) {
            throw new IndexOutOfBoundsException("getComponentBinding: Index value '" + index + "' not in range [0.." + (this._componentBindingList.size() - 1) + "]");
        }
        
        return (org.exolab.castor.builder.binding.ComponentBindingType) _componentBindingList.get(index);
    }

    /**
     * Method getComponentBinding.Returns the contents of the
     * collection in an Array.  <p>Note:  Just in case the
     * collection contents are changing in another thread, we pass
     * a 0-length Array of the correct type into the API call. 
     * This way we <i>know</i> that the Array returned is of
     * exactly the correct length.
     * 
     * @return this collection as an Array
     */
    public org.exolab.castor.builder.binding.ComponentBindingType[] getComponentBinding(
    ) {
        org.exolab.castor.builder.binding.ComponentBindingType[] array = new org.exolab.castor.builder.binding.ComponentBindingType[0];
        return (org.exolab.castor.builder.binding.ComponentBindingType[]) this._componentBindingList.toArray(array);
    }

    /**
     * Method getComponentBindingCount.
     * 
     * @return the size of this collection
     */
    public int getComponentBindingCount(
    ) {
        return this._componentBindingList.size();
    }

    /**
     * Returns the value of field 'defaultBindingType'.
     * 
     * @return the value of field 'DefaultBindingType'.
     */
    public org.exolab.castor.builder.binding.types.BindingType getDefaultBindingType(
    ) {
        return this._defaultBindingType;
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
    public org.exolab.castor.builder.binding.ComponentBindingType getElementBinding(
            final int index)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._elementBindingList.size()) {
            throw new IndexOutOfBoundsException("getElementBinding: Index value '" + index + "' not in range [0.." + (this._elementBindingList.size() - 1) + "]");
        }
        
        return (org.exolab.castor.builder.binding.ComponentBindingType) _elementBindingList.get(index);
    }

    /**
     * Method getElementBinding.Returns the contents of the
     * collection in an Array.  <p>Note:  Just in case the
     * collection contents are changing in another thread, we pass
     * a 0-length Array of the correct type into the API call. 
     * This way we <i>know</i> that the Array returned is of
     * exactly the correct length.
     * 
     * @return this collection as an Array
     */
    public org.exolab.castor.builder.binding.ComponentBindingType[] getElementBinding(
    ) {
        org.exolab.castor.builder.binding.ComponentBindingType[] array = new org.exolab.castor.builder.binding.ComponentBindingType[0];
        return (org.exolab.castor.builder.binding.ComponentBindingType[]) this._elementBindingList.toArray(array);
    }

    /**
     * Method getElementBindingCount.
     * 
     * @return the size of this collection
     */
    public int getElementBindingCount(
    ) {
        return this._elementBindingList.size();
    }

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
    public org.exolab.castor.builder.binding.ComponentBindingType getEnumBinding(
            final int index)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._enumBindingList.size()) {
            throw new IndexOutOfBoundsException("getEnumBinding: Index value '" + index + "' not in range [0.." + (this._enumBindingList.size() - 1) + "]");
        }
        
        return (org.exolab.castor.builder.binding.ComponentBindingType) _enumBindingList.get(index);
    }

    /**
     * Method getEnumBinding.Returns the contents of the collection
     * in an Array.  <p>Note:  Just in case the collection contents
     * are changing in another thread, we pass a 0-length Array of
     * the correct type into the API call.  This way we <i>know</i>
     * that the Array returned is of exactly the correct length.
     * 
     * @return this collection as an Array
     */
    public org.exolab.castor.builder.binding.ComponentBindingType[] getEnumBinding(
    ) {
        org.exolab.castor.builder.binding.ComponentBindingType[] array = new org.exolab.castor.builder.binding.ComponentBindingType[0];
        return (org.exolab.castor.builder.binding.ComponentBindingType[]) this._enumBindingList.toArray(array);
    }

    /**
     * Method getEnumBindingCount.
     * 
     * @return the size of this collection
     */
    public int getEnumBindingCount(
    ) {
        return this._enumBindingList.size();
    }

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
    public org.exolab.castor.builder.binding.ComponentBindingType getGroupBinding(
            final int index)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._groupBindingList.size()) {
            throw new IndexOutOfBoundsException("getGroupBinding: Index value '" + index + "' not in range [0.." + (this._groupBindingList.size() - 1) + "]");
        }
        
        return (org.exolab.castor.builder.binding.ComponentBindingType) _groupBindingList.get(index);
    }

    /**
     * Method getGroupBinding.Returns the contents of the
     * collection in an Array.  <p>Note:  Just in case the
     * collection contents are changing in another thread, we pass
     * a 0-length Array of the correct type into the API call. 
     * This way we <i>know</i> that the Array returned is of
     * exactly the correct length.
     * 
     * @return this collection as an Array
     */
    public org.exolab.castor.builder.binding.ComponentBindingType[] getGroupBinding(
    ) {
        org.exolab.castor.builder.binding.ComponentBindingType[] array = new org.exolab.castor.builder.binding.ComponentBindingType[0];
        return (org.exolab.castor.builder.binding.ComponentBindingType[]) this._groupBindingList.toArray(array);
    }

    /**
     * Method getGroupBindingCount.
     * 
     * @return the size of this collection
     */
    public int getGroupBindingCount(
    ) {
        return this._groupBindingList.size();
    }

    /**
     * Method getInclude.
     * 
     * @param index
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     * @return the value of the
     * org.exolab.castor.builder.binding.IncludeType at the given
     * index
     */
    public org.exolab.castor.builder.binding.IncludeType getInclude(
            final int index)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._includeList.size()) {
            throw new IndexOutOfBoundsException("getInclude: Index value '" + index + "' not in range [0.." + (this._includeList.size() - 1) + "]");
        }
        
        return (org.exolab.castor.builder.binding.IncludeType) _includeList.get(index);
    }

    /**
     * Method getInclude.Returns the contents of the collection in
     * an Array.  <p>Note:  Just in case the collection contents
     * are changing in another thread, we pass a 0-length Array of
     * the correct type into the API call.  This way we <i>know</i>
     * that the Array returned is of exactly the correct length.
     * 
     * @return this collection as an Array
     */
    public org.exolab.castor.builder.binding.IncludeType[] getInclude(
    ) {
        org.exolab.castor.builder.binding.IncludeType[] array = new org.exolab.castor.builder.binding.IncludeType[0];
        return (org.exolab.castor.builder.binding.IncludeType[]) this._includeList.toArray(array);
    }

    /**
     * Method getIncludeCount.
     * 
     * @return the size of this collection
     */
    public int getIncludeCount(
    ) {
        return this._includeList.size();
    }

    /**
     * Returns the value of field 'namingXML'.
     * 
     * @return the value of field 'NamingXML'.
     */
    public org.exolab.castor.builder.binding.NamingXMLType getNamingXML(
    ) {
        return this._namingXML;
    }

    /**
     * Method getPackage.
     * 
     * @param index
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     * @return the value of the
     * org.exolab.castor.builder.binding.PackageType at the given
     * index
     */
    public org.exolab.castor.builder.binding.PackageType getPackage(
            final int index)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._packageList.size()) {
            throw new IndexOutOfBoundsException("getPackage: Index value '" + index + "' not in range [0.." + (this._packageList.size() - 1) + "]");
        }
        
        return (org.exolab.castor.builder.binding.PackageType) _packageList.get(index);
    }

    /**
     * Method getPackage.Returns the contents of the collection in
     * an Array.  <p>Note:  Just in case the collection contents
     * are changing in another thread, we pass a 0-length Array of
     * the correct type into the API call.  This way we <i>know</i>
     * that the Array returned is of exactly the correct length.
     * 
     * @return this collection as an Array
     */
    public org.exolab.castor.builder.binding.PackageType[] getPackage(
    ) {
        org.exolab.castor.builder.binding.PackageType[] array = new org.exolab.castor.builder.binding.PackageType[0];
        return (org.exolab.castor.builder.binding.PackageType[]) this._packageList.toArray(array);
    }

    /**
     * Method getPackageCount.
     * 
     * @return the size of this collection
     */
    public int getPackageCount(
    ) {
        return this._packageList.size();
    }

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
            throw new IndexOutOfBoundsException("getSimpleTypeBinding: Index value '" + index + "' not in range [0.." + (this._simpleTypeBindingList.size() - 1) + "]");
        }
        
        return (org.exolab.castor.builder.binding.ComponentBindingType) _simpleTypeBindingList.get(index);
    }

    /**
     * Method getSimpleTypeBinding.Returns the contents of the
     * collection in an Array.  <p>Note:  Just in case the
     * collection contents are changing in another thread, we pass
     * a 0-length Array of the correct type into the API call. 
     * This way we <i>know</i> that the Array returned is of
     * exactly the correct length.
     * 
     * @return this collection as an Array
     */
    public org.exolab.castor.builder.binding.ComponentBindingType[] getSimpleTypeBinding(
    ) {
        org.exolab.castor.builder.binding.ComponentBindingType[] array = new org.exolab.castor.builder.binding.ComponentBindingType[0];
        return (org.exolab.castor.builder.binding.ComponentBindingType[]) this._simpleTypeBindingList.toArray(array);
    }

    /**
     * Method getSimpleTypeBindingCount.
     * 
     * @return the size of this collection
     */
    public int getSimpleTypeBindingCount(
    ) {
        return this._simpleTypeBindingList.size();
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
     * Method iterateAttributeBinding.
     * 
     * @return an Iterator over all possible elements in this
     * collection
     */
    public java.util.Iterator iterateAttributeBinding(
    ) {
        return this._attributeBindingList.iterator();
    }

    /**
     * Method iterateComplexTypeBinding.
     * 
     * @return an Iterator over all possible elements in this
     * collection
     */
    public java.util.Iterator iterateComplexTypeBinding(
    ) {
        return this._complexTypeBindingList.iterator();
    }

    /**
     * Method iterateComponentBinding.
     * 
     * @return an Iterator over all possible elements in this
     * collection
     */
    public java.util.Iterator iterateComponentBinding(
    ) {
        return this._componentBindingList.iterator();
    }

    /**
     * Method iterateElementBinding.
     * 
     * @return an Iterator over all possible elements in this
     * collection
     */
    public java.util.Iterator iterateElementBinding(
    ) {
        return this._elementBindingList.iterator();
    }

    /**
     * Method iterateEnumBinding.
     * 
     * @return an Iterator over all possible elements in this
     * collection
     */
    public java.util.Iterator iterateEnumBinding(
    ) {
        return this._enumBindingList.iterator();
    }

    /**
     * Method iterateGroupBinding.
     * 
     * @return an Iterator over all possible elements in this
     * collection
     */
    public java.util.Iterator iterateGroupBinding(
    ) {
        return this._groupBindingList.iterator();
    }

    /**
     * Method iterateInclude.
     * 
     * @return an Iterator over all possible elements in this
     * collection
     */
    public java.util.Iterator iterateInclude(
    ) {
        return this._includeList.iterator();
    }

    /**
     * Method iteratePackage.
     * 
     * @return an Iterator over all possible elements in this
     * collection
     */
    public java.util.Iterator iteratePackage(
    ) {
        return this._packageList.iterator();
    }

    /**
     * Method iterateSimpleTypeBinding.
     * 
     * @return an Iterator over all possible elements in this
     * collection
     */
    public java.util.Iterator iterateSimpleTypeBinding(
    ) {
        return this._simpleTypeBindingList.iterator();
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
    public void removeAllAttributeBinding(
    ) {
        this._attributeBindingList.clear();
    }

    /**
     */
    public void removeAllComplexTypeBinding(
    ) {
        this._complexTypeBindingList.clear();
    }

    /**
     */
    public void removeAllComponentBinding(
    ) {
        this._componentBindingList.clear();
    }

    /**
     */
    public void removeAllElementBinding(
    ) {
        this._elementBindingList.clear();
    }

    /**
     */
    public void removeAllEnumBinding(
    ) {
        this._enumBindingList.clear();
    }

    /**
     */
    public void removeAllGroupBinding(
    ) {
        this._groupBindingList.clear();
    }

    /**
     */
    public void removeAllInclude(
    ) {
        this._includeList.clear();
    }

    /**
     */
    public void removeAllPackage(
    ) {
        this._packageList.clear();
    }

    /**
     */
    public void removeAllSimpleTypeBinding(
    ) {
        this._simpleTypeBindingList.clear();
    }

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
    }

    /**
     * Method removeAttributeBindingAt.
     * 
     * @param index
     * @return the element removed from the collection
     */
    public org.exolab.castor.builder.binding.ComponentBindingType removeAttributeBindingAt(
            final int index) {
        java.lang.Object obj = this._attributeBindingList.remove(index);
        return (org.exolab.castor.builder.binding.ComponentBindingType) obj;
    }

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
    }

    /**
     * Method removeComplexTypeBindingAt.
     * 
     * @param index
     * @return the element removed from the collection
     */
    public org.exolab.castor.builder.binding.ComponentBindingType removeComplexTypeBindingAt(
            final int index) {
        java.lang.Object obj = this._complexTypeBindingList.remove(index);
        return (org.exolab.castor.builder.binding.ComponentBindingType) obj;
    }

    /**
     * Method removeComponentBinding.
     * 
     * @param vComponentBinding
     * @return true if the object was removed from the collection.
     */
    public boolean removeComponentBinding(
            final org.exolab.castor.builder.binding.ComponentBindingType vComponentBinding) {
        boolean removed = _componentBindingList.remove(vComponentBinding);
        return removed;
    }

    /**
     * Method removeComponentBindingAt.
     * 
     * @param index
     * @return the element removed from the collection
     */
    public org.exolab.castor.builder.binding.ComponentBindingType removeComponentBindingAt(
            final int index) {
        java.lang.Object obj = this._componentBindingList.remove(index);
        return (org.exolab.castor.builder.binding.ComponentBindingType) obj;
    }

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
    }

    /**
     * Method removeElementBindingAt.
     * 
     * @param index
     * @return the element removed from the collection
     */
    public org.exolab.castor.builder.binding.ComponentBindingType removeElementBindingAt(
            final int index) {
        java.lang.Object obj = this._elementBindingList.remove(index);
        return (org.exolab.castor.builder.binding.ComponentBindingType) obj;
    }

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
    }

    /**
     * Method removeEnumBindingAt.
     * 
     * @param index
     * @return the element removed from the collection
     */
    public org.exolab.castor.builder.binding.ComponentBindingType removeEnumBindingAt(
            final int index) {
        java.lang.Object obj = this._enumBindingList.remove(index);
        return (org.exolab.castor.builder.binding.ComponentBindingType) obj;
    }

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
    }

    /**
     * Method removeGroupBindingAt.
     * 
     * @param index
     * @return the element removed from the collection
     */
    public org.exolab.castor.builder.binding.ComponentBindingType removeGroupBindingAt(
            final int index) {
        java.lang.Object obj = this._groupBindingList.remove(index);
        return (org.exolab.castor.builder.binding.ComponentBindingType) obj;
    }

    /**
     * Method removeInclude.
     * 
     * @param vInclude
     * @return true if the object was removed from the collection.
     */
    public boolean removeInclude(
            final org.exolab.castor.builder.binding.IncludeType vInclude) {
        boolean removed = _includeList.remove(vInclude);
        return removed;
    }

    /**
     * Method removeIncludeAt.
     * 
     * @param index
     * @return the element removed from the collection
     */
    public org.exolab.castor.builder.binding.IncludeType removeIncludeAt(
            final int index) {
        java.lang.Object obj = this._includeList.remove(index);
        return (org.exolab.castor.builder.binding.IncludeType) obj;
    }

    /**
     * Method removePackage.
     * 
     * @param vPackage
     * @return true if the object was removed from the collection.
     */
    public boolean removePackage(
            final org.exolab.castor.builder.binding.PackageType vPackage) {
        boolean removed = _packageList.remove(vPackage);
        return removed;
    }

    /**
     * Method removePackageAt.
     * 
     * @param index
     * @return the element removed from the collection
     */
    public org.exolab.castor.builder.binding.PackageType removePackageAt(
            final int index) {
        java.lang.Object obj = this._packageList.remove(index);
        return (org.exolab.castor.builder.binding.PackageType) obj;
    }

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
    }

    /**
     * Method removeSimpleTypeBindingAt.
     * 
     * @param index
     * @return the element removed from the collection
     */
    public org.exolab.castor.builder.binding.ComponentBindingType removeSimpleTypeBindingAt(
            final int index) {
        java.lang.Object obj = this._simpleTypeBindingList.remove(index);
        return (org.exolab.castor.builder.binding.ComponentBindingType) obj;
    }

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
            throw new IndexOutOfBoundsException("setAttributeBinding: Index value '" + index + "' not in range [0.." + (this._attributeBindingList.size() - 1) + "]");
        }
        
        this._attributeBindingList.set(index, vAttributeBinding);
    }

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
    }

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
            throw new IndexOutOfBoundsException("setComplexTypeBinding: Index value '" + index + "' not in range [0.." + (this._complexTypeBindingList.size() - 1) + "]");
        }
        
        this._complexTypeBindingList.set(index, vComplexTypeBinding);
    }

    /**
     * 
     * 
     * @param vComplexTypeBindingArray
     */
    public void setComplexTypeBinding(
            final org.exolab.castor.builder.binding.ComponentBindingType[] vComplexTypeBindingArray) {
        //-- copy array
        _complexTypeBindingList.clear();
        
        for (int i = 0; i < vComplexTypeBindingArray.length; i++) {
                this._complexTypeBindingList.add(vComplexTypeBindingArray[i]);
        }
    }

    /**
     * 
     * 
     * @param index
     * @param vComponentBinding
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void setComponentBinding(
            final int index,
            final org.exolab.castor.builder.binding.ComponentBindingType vComponentBinding)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._componentBindingList.size()) {
            throw new IndexOutOfBoundsException("setComponentBinding: Index value '" + index + "' not in range [0.." + (this._componentBindingList.size() - 1) + "]");
        }
        
        this._componentBindingList.set(index, vComponentBinding);
    }

    /**
     * 
     * 
     * @param vComponentBindingArray
     */
    public void setComponentBinding(
            final org.exolab.castor.builder.binding.ComponentBindingType[] vComponentBindingArray) {
        //-- copy array
        _componentBindingList.clear();
        
        for (int i = 0; i < vComponentBindingArray.length; i++) {
                this._componentBindingList.add(vComponentBindingArray[i]);
        }
    }

    /**
     * Sets the value of field 'defaultBindingType'.
     * 
     * @param defaultBindingType the value of field
     * 'defaultBindingType'.
     */
    public void setDefaultBindingType(
            final org.exolab.castor.builder.binding.types.BindingType defaultBindingType) {
        this._defaultBindingType = defaultBindingType;
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
            throw new IndexOutOfBoundsException("setElementBinding: Index value '" + index + "' not in range [0.." + (this._elementBindingList.size() - 1) + "]");
        }
        
        this._elementBindingList.set(index, vElementBinding);
    }

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
    }

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
            throw new IndexOutOfBoundsException("setEnumBinding: Index value '" + index + "' not in range [0.." + (this._enumBindingList.size() - 1) + "]");
        }
        
        this._enumBindingList.set(index, vEnumBinding);
    }

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
    }

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
            throw new IndexOutOfBoundsException("setGroupBinding: Index value '" + index + "' not in range [0.." + (this._groupBindingList.size() - 1) + "]");
        }
        
        this._groupBindingList.set(index, vGroupBinding);
    }

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
    }

    /**
     * 
     * 
     * @param index
     * @param vInclude
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void setInclude(
            final int index,
            final org.exolab.castor.builder.binding.IncludeType vInclude)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._includeList.size()) {
            throw new IndexOutOfBoundsException("setInclude: Index value '" + index + "' not in range [0.." + (this._includeList.size() - 1) + "]");
        }
        
        this._includeList.set(index, vInclude);
    }

    /**
     * 
     * 
     * @param vIncludeArray
     */
    public void setInclude(
            final org.exolab.castor.builder.binding.IncludeType[] vIncludeArray) {
        //-- copy array
        _includeList.clear();
        
        for (int i = 0; i < vIncludeArray.length; i++) {
                this._includeList.add(vIncludeArray[i]);
        }
    }

    /**
     * Sets the value of field 'namingXML'.
     * 
     * @param namingXML the value of field 'namingXML'.
     */
    public void setNamingXML(
            final org.exolab.castor.builder.binding.NamingXMLType namingXML) {
        this._namingXML = namingXML;
    }

    /**
     * 
     * 
     * @param index
     * @param vPackage
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void setPackage(
            final int index,
            final org.exolab.castor.builder.binding.PackageType vPackage)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._packageList.size()) {
            throw new IndexOutOfBoundsException("setPackage: Index value '" + index + "' not in range [0.." + (this._packageList.size() - 1) + "]");
        }
        
        this._packageList.set(index, vPackage);
    }

    /**
     * 
     * 
     * @param vPackageArray
     */
    public void setPackage(
            final org.exolab.castor.builder.binding.PackageType[] vPackageArray) {
        //-- copy array
        _packageList.clear();
        
        for (int i = 0; i < vPackageArray.length; i++) {
                this._packageList.add(vPackageArray[i]);
        }
    }

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
            throw new IndexOutOfBoundsException("setSimpleTypeBinding: Index value '" + index + "' not in range [0.." + (this._simpleTypeBindingList.size() - 1) + "]");
        }
        
        this._simpleTypeBindingList.set(index, vSimpleTypeBinding);
    }

    /**
     * 
     * 
     * @param vSimpleTypeBindingArray
     */
    public void setSimpleTypeBinding(
            final org.exolab.castor.builder.binding.ComponentBindingType[] vSimpleTypeBindingArray) {
        //-- copy array
        _simpleTypeBindingList.clear();
        
        for (int i = 0; i < vSimpleTypeBindingArray.length; i++) {
                this._simpleTypeBindingList.add(vSimpleTypeBindingArray[i]);
        }
    }

    /**
     * Method unmarshalBinding.
     * 
     * @param reader
     * @throws org.exolab.castor.xml.MarshalException if object is
     * null or if any SAXException is thrown during marshaling
     * @throws org.exolab.castor.xml.ValidationException if this
     * object is an invalid instance according to the schema
     * @return the unmarshaled
     * org.exolab.castor.builder.binding.Binding
     */
    public static org.exolab.castor.builder.binding.Binding unmarshalBinding(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (org.exolab.castor.builder.binding.Binding) Unmarshaller.unmarshal(org.exolab.castor.builder.binding.Binding.class, reader);
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
