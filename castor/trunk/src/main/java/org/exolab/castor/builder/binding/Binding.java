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
     * Field _defaultBindingType
     */
    private org.exolab.castor.builder.binding.types.BindingType _defaultBindingType;

    /**
     * Field _includeList
     */
    private java.util.List _includeList;

    /**
     * Field _packageList
     */
    private java.util.List _packageList;

    /**
     * Field _namingXML
     */
    private org.exolab.castor.builder.binding.NamingXMLType _namingXML;

    /**
     * Field _elementBindingList
     */
    private java.util.List _elementBindingList;

    /**
     * Field _attributeBindingList
     */
    private java.util.List _attributeBindingList;

    /**
     * Field _complexTypeBindingList
     */
    private java.util.List _complexTypeBindingList;

    /**
     * Field _groupBindingList
     */
    private java.util.List _groupBindingList;

    /**
     * Field _enumBindingList
     */
    private java.util.List _enumBindingList;

    /**
     * Field _simpleTypeBindingList
     */
    private java.util.List _simpleTypeBindingList;


      //----------------/
     //- Constructors -/
    //----------------/

    public Binding() 
     {
        super();
        this._includeList = new java.util.ArrayList();
        this._packageList = new java.util.ArrayList();
        this._elementBindingList = new java.util.ArrayList();
        this._attributeBindingList = new java.util.ArrayList();
        this._complexTypeBindingList = new java.util.ArrayList();
        this._groupBindingList = new java.util.ArrayList();
        this._enumBindingList = new java.util.ArrayList();
        this._simpleTypeBindingList = new java.util.ArrayList();
    } //-- org.exolab.castor.builder.binding.Binding()


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
    public void addAttributeBinding(org.exolab.castor.builder.binding.ComponentBindingType vAttributeBinding)
        throws java.lang.IndexOutOfBoundsException
    {
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
    public void addAttributeBinding(int index, org.exolab.castor.builder.binding.ComponentBindingType vAttributeBinding)
        throws java.lang.IndexOutOfBoundsException
    {
        this._attributeBindingList.add(index, vAttributeBinding);
    } //-- void addAttributeBinding(int, org.exolab.castor.builder.binding.ComponentBindingType) 

    /**
     * 
     * 
     * @param vComplexTypeBinding
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addComplexTypeBinding(org.exolab.castor.builder.binding.ComponentBindingType vComplexTypeBinding)
        throws java.lang.IndexOutOfBoundsException
    {
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
    public void addComplexTypeBinding(int index, org.exolab.castor.builder.binding.ComponentBindingType vComplexTypeBinding)
        throws java.lang.IndexOutOfBoundsException
    {
        this._complexTypeBindingList.add(index, vComplexTypeBinding);
    } //-- void addComplexTypeBinding(int, org.exolab.castor.builder.binding.ComponentBindingType) 

    /**
     * 
     * 
     * @param vElementBinding
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addElementBinding(org.exolab.castor.builder.binding.ComponentBindingType vElementBinding)
        throws java.lang.IndexOutOfBoundsException
    {
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
    public void addElementBinding(int index, org.exolab.castor.builder.binding.ComponentBindingType vElementBinding)
        throws java.lang.IndexOutOfBoundsException
    {
        this._elementBindingList.add(index, vElementBinding);
    } //-- void addElementBinding(int, org.exolab.castor.builder.binding.ComponentBindingType) 

    /**
     * 
     * 
     * @param vEnumBinding
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addEnumBinding(org.exolab.castor.builder.binding.ComponentBindingType vEnumBinding)
        throws java.lang.IndexOutOfBoundsException
    {
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
    public void addEnumBinding(int index, org.exolab.castor.builder.binding.ComponentBindingType vEnumBinding)
        throws java.lang.IndexOutOfBoundsException
    {
        this._enumBindingList.add(index, vEnumBinding);
    } //-- void addEnumBinding(int, org.exolab.castor.builder.binding.ComponentBindingType) 

    /**
     * 
     * 
     * @param vGroupBinding
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addGroupBinding(org.exolab.castor.builder.binding.ComponentBindingType vGroupBinding)
        throws java.lang.IndexOutOfBoundsException
    {
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
    public void addGroupBinding(int index, org.exolab.castor.builder.binding.ComponentBindingType vGroupBinding)
        throws java.lang.IndexOutOfBoundsException
    {
        this._groupBindingList.add(index, vGroupBinding);
    } //-- void addGroupBinding(int, org.exolab.castor.builder.binding.ComponentBindingType) 

    /**
     * 
     * 
     * @param vInclude
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addInclude(org.exolab.castor.builder.binding.IncludeType vInclude)
        throws java.lang.IndexOutOfBoundsException
    {
        this._includeList.add(vInclude);
    } //-- void addInclude(org.exolab.castor.builder.binding.IncludeType) 

    /**
     * 
     * 
     * @param index
     * @param vInclude
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addInclude(int index, org.exolab.castor.builder.binding.IncludeType vInclude)
        throws java.lang.IndexOutOfBoundsException
    {
        this._includeList.add(index, vInclude);
    } //-- void addInclude(int, org.exolab.castor.builder.binding.IncludeType) 

    /**
     * 
     * 
     * @param vPackage
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addPackage(org.exolab.castor.builder.binding.PackageType vPackage)
        throws java.lang.IndexOutOfBoundsException
    {
        this._packageList.add(vPackage);
    } //-- void addPackage(org.exolab.castor.builder.binding.PackageType) 

    /**
     * 
     * 
     * @param index
     * @param vPackage
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addPackage(int index, org.exolab.castor.builder.binding.PackageType vPackage)
        throws java.lang.IndexOutOfBoundsException
    {
        this._packageList.add(index, vPackage);
    } //-- void addPackage(int, org.exolab.castor.builder.binding.PackageType) 

    /**
     * 
     * 
     * @param vSimpleTypeBinding
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addSimpleTypeBinding(org.exolab.castor.builder.binding.ComponentBindingType vSimpleTypeBinding)
        throws java.lang.IndexOutOfBoundsException
    {
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
    public void addSimpleTypeBinding(int index, org.exolab.castor.builder.binding.ComponentBindingType vSimpleTypeBinding)
        throws java.lang.IndexOutOfBoundsException
    {
        this._simpleTypeBindingList.add(index, vSimpleTypeBinding);
    } //-- void addSimpleTypeBinding(int, org.exolab.castor.builder.binding.ComponentBindingType) 

    /**
     * Method enumerateAttributeBinding
     * 
     * 
     * 
     * @return an Enumeration over all possible elements of this
     * collection
     */
    public java.util.Enumeration enumerateAttributeBinding()
    {
        return java.util.Collections.enumeration(this._attributeBindingList);
    } //-- java.util.Enumeration enumerateAttributeBinding() 

    /**
     * Method enumerateComplexTypeBinding
     * 
     * 
     * 
     * @return an Enumeration over all possible elements of this
     * collection
     */
    public java.util.Enumeration enumerateComplexTypeBinding()
    {
        return java.util.Collections.enumeration(this._complexTypeBindingList);
    } //-- java.util.Enumeration enumerateComplexTypeBinding() 

    /**
     * Method enumerateElementBinding
     * 
     * 
     * 
     * @return an Enumeration over all possible elements of this
     * collection
     */
    public java.util.Enumeration enumerateElementBinding()
    {
        return java.util.Collections.enumeration(this._elementBindingList);
    } //-- java.util.Enumeration enumerateElementBinding() 

    /**
     * Method enumerateEnumBinding
     * 
     * 
     * 
     * @return an Enumeration over all possible elements of this
     * collection
     */
    public java.util.Enumeration enumerateEnumBinding()
    {
        return java.util.Collections.enumeration(this._enumBindingList);
    } //-- java.util.Enumeration enumerateEnumBinding() 

    /**
     * Method enumerateGroupBinding
     * 
     * 
     * 
     * @return an Enumeration over all possible elements of this
     * collection
     */
    public java.util.Enumeration enumerateGroupBinding()
    {
        return java.util.Collections.enumeration(this._groupBindingList);
    } //-- java.util.Enumeration enumerateGroupBinding() 

    /**
     * Method enumerateInclude
     * 
     * 
     * 
     * @return an Enumeration over all possible elements of this
     * collection
     */
    public java.util.Enumeration enumerateInclude()
    {
        return java.util.Collections.enumeration(this._includeList);
    } //-- java.util.Enumeration enumerateInclude() 

    /**
     * Method enumeratePackage
     * 
     * 
     * 
     * @return an Enumeration over all possible elements of this
     * collection
     */
    public java.util.Enumeration enumeratePackage()
    {
        return java.util.Collections.enumeration(this._packageList);
    } //-- java.util.Enumeration enumeratePackage() 

    /**
     * Method enumerateSimpleTypeBinding
     * 
     * 
     * 
     * @return an Enumeration over all possible elements of this
     * collection
     */
    public java.util.Enumeration enumerateSimpleTypeBinding()
    {
        return java.util.Collections.enumeration(this._simpleTypeBindingList);
    } //-- java.util.Enumeration enumerateSimpleTypeBinding() 

    /**
     * Method getAttributeBinding
     * 
     * 
     * 
     * @param index
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     * @return the value of the
     * org.exolab.castor.builder.binding.ComponentBindingType at
     * the given index
     */
    public org.exolab.castor.builder.binding.ComponentBindingType getAttributeBinding(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        // check bounds for index
        if (index < 0 || index >= this._attributeBindingList.size()) {
            throw new IndexOutOfBoundsException("getAttributeBinding: Index value '" + index + "' not in range [0.." + (this._attributeBindingList.size() - 1) + "]");
        }
        
        return (org.exolab.castor.builder.binding.ComponentBindingType) _attributeBindingList.get(index);
    } //-- org.exolab.castor.builder.binding.ComponentBindingType getAttributeBinding(int) 

    /**
     * Method getAttributeBinding
     * 
     * 
     * 
     * @return this collection as an Array
     */
    public org.exolab.castor.builder.binding.ComponentBindingType[] getAttributeBinding()
    {
        int size = this._attributeBindingList.size();
        org.exolab.castor.builder.binding.ComponentBindingType[] array = new org.exolab.castor.builder.binding.ComponentBindingType[size];
        java.util.Iterator iter = _attributeBindingList.iterator();
        for (int index = 0; index < size; index++){
            array[index] = (org.exolab.castor.builder.binding.ComponentBindingType) iter.next();
        }
        
        return array;
    } //-- org.exolab.castor.builder.binding.ComponentBindingType[] getAttributeBinding() 

    /**
     * Method getAttributeBindingCount
     * 
     * 
     * 
     * @return the size of this collection
     */
    public int getAttributeBindingCount()
    {
        return this._attributeBindingList.size();
    } //-- int getAttributeBindingCount() 

    /**
     * Method getComplexTypeBinding
     * 
     * 
     * 
     * @param index
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     * @return the value of the
     * org.exolab.castor.builder.binding.ComponentBindingType at
     * the given index
     */
    public org.exolab.castor.builder.binding.ComponentBindingType getComplexTypeBinding(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        // check bounds for index
        if (index < 0 || index >= this._complexTypeBindingList.size()) {
            throw new IndexOutOfBoundsException("getComplexTypeBinding: Index value '" + index + "' not in range [0.." + (this._complexTypeBindingList.size() - 1) + "]");
        }
        
        return (org.exolab.castor.builder.binding.ComponentBindingType) _complexTypeBindingList.get(index);
    } //-- org.exolab.castor.builder.binding.ComponentBindingType getComplexTypeBinding(int) 

    /**
     * Method getComplexTypeBinding
     * 
     * 
     * 
     * @return this collection as an Array
     */
    public org.exolab.castor.builder.binding.ComponentBindingType[] getComplexTypeBinding()
    {
        int size = this._complexTypeBindingList.size();
        org.exolab.castor.builder.binding.ComponentBindingType[] array = new org.exolab.castor.builder.binding.ComponentBindingType[size];
        java.util.Iterator iter = _complexTypeBindingList.iterator();
        for (int index = 0; index < size; index++){
            array[index] = (org.exolab.castor.builder.binding.ComponentBindingType) iter.next();
        }
        
        return array;
    } //-- org.exolab.castor.builder.binding.ComponentBindingType[] getComplexTypeBinding() 

    /**
     * Method getComplexTypeBindingCount
     * 
     * 
     * 
     * @return the size of this collection
     */
    public int getComplexTypeBindingCount()
    {
        return this._complexTypeBindingList.size();
    } //-- int getComplexTypeBindingCount() 

    /**
     * Returns the value of field 'defaultBindingType'.
     * 
     * @return the value of field 'DefaultBindingType'.
     */
    public org.exolab.castor.builder.binding.types.BindingType getDefaultBindingType()
    {
        return this._defaultBindingType;
    } //-- org.exolab.castor.builder.binding.types.BindingType getDefaultBindingType() 

    /**
     * Method getElementBinding
     * 
     * 
     * 
     * @param index
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     * @return the value of the
     * org.exolab.castor.builder.binding.ComponentBindingType at
     * the given index
     */
    public org.exolab.castor.builder.binding.ComponentBindingType getElementBinding(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        // check bounds for index
        if (index < 0 || index >= this._elementBindingList.size()) {
            throw new IndexOutOfBoundsException("getElementBinding: Index value '" + index + "' not in range [0.." + (this._elementBindingList.size() - 1) + "]");
        }
        
        return (org.exolab.castor.builder.binding.ComponentBindingType) _elementBindingList.get(index);
    } //-- org.exolab.castor.builder.binding.ComponentBindingType getElementBinding(int) 

    /**
     * Method getElementBinding
     * 
     * 
     * 
     * @return this collection as an Array
     */
    public org.exolab.castor.builder.binding.ComponentBindingType[] getElementBinding()
    {
        int size = this._elementBindingList.size();
        org.exolab.castor.builder.binding.ComponentBindingType[] array = new org.exolab.castor.builder.binding.ComponentBindingType[size];
        java.util.Iterator iter = _elementBindingList.iterator();
        for (int index = 0; index < size; index++){
            array[index] = (org.exolab.castor.builder.binding.ComponentBindingType) iter.next();
        }
        
        return array;
    } //-- org.exolab.castor.builder.binding.ComponentBindingType[] getElementBinding() 

    /**
     * Method getElementBindingCount
     * 
     * 
     * 
     * @return the size of this collection
     */
    public int getElementBindingCount()
    {
        return this._elementBindingList.size();
    } //-- int getElementBindingCount() 

    /**
     * Method getEnumBinding
     * 
     * 
     * 
     * @param index
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     * @return the value of the
     * org.exolab.castor.builder.binding.ComponentBindingType at
     * the given index
     */
    public org.exolab.castor.builder.binding.ComponentBindingType getEnumBinding(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        // check bounds for index
        if (index < 0 || index >= this._enumBindingList.size()) {
            throw new IndexOutOfBoundsException("getEnumBinding: Index value '" + index + "' not in range [0.." + (this._enumBindingList.size() - 1) + "]");
        }
        
        return (org.exolab.castor.builder.binding.ComponentBindingType) _enumBindingList.get(index);
    } //-- org.exolab.castor.builder.binding.ComponentBindingType getEnumBinding(int) 

    /**
     * Method getEnumBinding
     * 
     * 
     * 
     * @return this collection as an Array
     */
    public org.exolab.castor.builder.binding.ComponentBindingType[] getEnumBinding()
    {
        int size = this._enumBindingList.size();
        org.exolab.castor.builder.binding.ComponentBindingType[] array = new org.exolab.castor.builder.binding.ComponentBindingType[size];
        java.util.Iterator iter = _enumBindingList.iterator();
        for (int index = 0; index < size; index++){
            array[index] = (org.exolab.castor.builder.binding.ComponentBindingType) iter.next();
        }
        
        return array;
    } //-- org.exolab.castor.builder.binding.ComponentBindingType[] getEnumBinding() 

    /**
     * Method getEnumBindingCount
     * 
     * 
     * 
     * @return the size of this collection
     */
    public int getEnumBindingCount()
    {
        return this._enumBindingList.size();
    } //-- int getEnumBindingCount() 

    /**
     * Method getGroupBinding
     * 
     * 
     * 
     * @param index
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     * @return the value of the
     * org.exolab.castor.builder.binding.ComponentBindingType at
     * the given index
     */
    public org.exolab.castor.builder.binding.ComponentBindingType getGroupBinding(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        // check bounds for index
        if (index < 0 || index >= this._groupBindingList.size()) {
            throw new IndexOutOfBoundsException("getGroupBinding: Index value '" + index + "' not in range [0.." + (this._groupBindingList.size() - 1) + "]");
        }
        
        return (org.exolab.castor.builder.binding.ComponentBindingType) _groupBindingList.get(index);
    } //-- org.exolab.castor.builder.binding.ComponentBindingType getGroupBinding(int) 

    /**
     * Method getGroupBinding
     * 
     * 
     * 
     * @return this collection as an Array
     */
    public org.exolab.castor.builder.binding.ComponentBindingType[] getGroupBinding()
    {
        int size = this._groupBindingList.size();
        org.exolab.castor.builder.binding.ComponentBindingType[] array = new org.exolab.castor.builder.binding.ComponentBindingType[size];
        java.util.Iterator iter = _groupBindingList.iterator();
        for (int index = 0; index < size; index++){
            array[index] = (org.exolab.castor.builder.binding.ComponentBindingType) iter.next();
        }
        
        return array;
    } //-- org.exolab.castor.builder.binding.ComponentBindingType[] getGroupBinding() 

    /**
     * Method getGroupBindingCount
     * 
     * 
     * 
     * @return the size of this collection
     */
    public int getGroupBindingCount()
    {
        return this._groupBindingList.size();
    } //-- int getGroupBindingCount() 

    /**
     * Method getInclude
     * 
     * 
     * 
     * @param index
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     * @return the value of the
     * org.exolab.castor.builder.binding.IncludeType at the given
     * index
     */
    public org.exolab.castor.builder.binding.IncludeType getInclude(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        // check bounds for index
        if (index < 0 || index >= this._includeList.size()) {
            throw new IndexOutOfBoundsException("getInclude: Index value '" + index + "' not in range [0.." + (this._includeList.size() - 1) + "]");
        }
        
        return (org.exolab.castor.builder.binding.IncludeType) _includeList.get(index);
    } //-- org.exolab.castor.builder.binding.IncludeType getInclude(int) 

    /**
     * Method getInclude
     * 
     * 
     * 
     * @return this collection as an Array
     */
    public org.exolab.castor.builder.binding.IncludeType[] getInclude()
    {
        int size = this._includeList.size();
        org.exolab.castor.builder.binding.IncludeType[] array = new org.exolab.castor.builder.binding.IncludeType[size];
        java.util.Iterator iter = _includeList.iterator();
        for (int index = 0; index < size; index++){
            array[index] = (org.exolab.castor.builder.binding.IncludeType) iter.next();
        }
        
        return array;
    } //-- org.exolab.castor.builder.binding.IncludeType[] getInclude() 

    /**
     * Method getIncludeCount
     * 
     * 
     * 
     * @return the size of this collection
     */
    public int getIncludeCount()
    {
        return this._includeList.size();
    } //-- int getIncludeCount() 

    /**
     * Returns the value of field 'namingXML'.
     * 
     * @return the value of field 'NamingXML'.
     */
    public org.exolab.castor.builder.binding.NamingXMLType getNamingXML()
    {
        return this._namingXML;
    } //-- org.exolab.castor.builder.binding.NamingXMLType getNamingXML() 

    /**
     * Method getPackage
     * 
     * 
     * 
     * @param index
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     * @return the value of the
     * org.exolab.castor.builder.binding.PackageType at the given
     * index
     */
    public org.exolab.castor.builder.binding.PackageType getPackage(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        // check bounds for index
        if (index < 0 || index >= this._packageList.size()) {
            throw new IndexOutOfBoundsException("getPackage: Index value '" + index + "' not in range [0.." + (this._packageList.size() - 1) + "]");
        }
        
        return (org.exolab.castor.builder.binding.PackageType) _packageList.get(index);
    } //-- org.exolab.castor.builder.binding.PackageType getPackage(int) 

    /**
     * Method getPackage
     * 
     * 
     * 
     * @return this collection as an Array
     */
    public org.exolab.castor.builder.binding.PackageType[] getPackage()
    {
        int size = this._packageList.size();
        org.exolab.castor.builder.binding.PackageType[] array = new org.exolab.castor.builder.binding.PackageType[size];
        java.util.Iterator iter = _packageList.iterator();
        for (int index = 0; index < size; index++){
            array[index] = (org.exolab.castor.builder.binding.PackageType) iter.next();
        }
        
        return array;
    } //-- org.exolab.castor.builder.binding.PackageType[] getPackage() 

    /**
     * Method getPackageCount
     * 
     * 
     * 
     * @return the size of this collection
     */
    public int getPackageCount()
    {
        return this._packageList.size();
    } //-- int getPackageCount() 

    /**
     * Method getSimpleTypeBinding
     * 
     * 
     * 
     * @param index
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     * @return the value of the
     * org.exolab.castor.builder.binding.ComponentBindingType at
     * the given index
     */
    public org.exolab.castor.builder.binding.ComponentBindingType getSimpleTypeBinding(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        // check bounds for index
        if (index < 0 || index >= this._simpleTypeBindingList.size()) {
            throw new IndexOutOfBoundsException("getSimpleTypeBinding: Index value '" + index + "' not in range [0.." + (this._simpleTypeBindingList.size() - 1) + "]");
        }
        
        return (org.exolab.castor.builder.binding.ComponentBindingType) _simpleTypeBindingList.get(index);
    } //-- org.exolab.castor.builder.binding.ComponentBindingType getSimpleTypeBinding(int) 

    /**
     * Method getSimpleTypeBinding
     * 
     * 
     * 
     * @return this collection as an Array
     */
    public org.exolab.castor.builder.binding.ComponentBindingType[] getSimpleTypeBinding()
    {
        int size = this._simpleTypeBindingList.size();
        org.exolab.castor.builder.binding.ComponentBindingType[] array = new org.exolab.castor.builder.binding.ComponentBindingType[size];
        java.util.Iterator iter = _simpleTypeBindingList.iterator();
        for (int index = 0; index < size; index++){
            array[index] = (org.exolab.castor.builder.binding.ComponentBindingType) iter.next();
        }
        
        return array;
    } //-- org.exolab.castor.builder.binding.ComponentBindingType[] getSimpleTypeBinding() 

    /**
     * Method getSimpleTypeBindingCount
     * 
     * 
     * 
     * @return the size of this collection
     */
    public int getSimpleTypeBindingCount()
    {
        return this._simpleTypeBindingList.size();
    } //-- int getSimpleTypeBindingCount() 

    /**
     * Method isValid
     * 
     * 
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
     * Method iterateAttributeBinding
     * 
     * 
     * 
     * @return an Iterator over all possible elements in this
     * collection
     */
    public java.util.Iterator iterateAttributeBinding()
    {
        return this._attributeBindingList.iterator();
    } //-- java.util.Iterator iterateAttributeBinding() 

    /**
     * Method iterateComplexTypeBinding
     * 
     * 
     * 
     * @return an Iterator over all possible elements in this
     * collection
     */
    public java.util.Iterator iterateComplexTypeBinding()
    {
        return this._complexTypeBindingList.iterator();
    } //-- java.util.Iterator iterateComplexTypeBinding() 

    /**
     * Method iterateElementBinding
     * 
     * 
     * 
     * @return an Iterator over all possible elements in this
     * collection
     */
    public java.util.Iterator iterateElementBinding()
    {
        return this._elementBindingList.iterator();
    } //-- java.util.Iterator iterateElementBinding() 

    /**
     * Method iterateEnumBinding
     * 
     * 
     * 
     * @return an Iterator over all possible elements in this
     * collection
     */
    public java.util.Iterator iterateEnumBinding()
    {
        return this._enumBindingList.iterator();
    } //-- java.util.Iterator iterateEnumBinding() 

    /**
     * Method iterateGroupBinding
     * 
     * 
     * 
     * @return an Iterator over all possible elements in this
     * collection
     */
    public java.util.Iterator iterateGroupBinding()
    {
        return this._groupBindingList.iterator();
    } //-- java.util.Iterator iterateGroupBinding() 

    /**
     * Method iterateInclude
     * 
     * 
     * 
     * @return an Iterator over all possible elements in this
     * collection
     */
    public java.util.Iterator iterateInclude()
    {
        return this._includeList.iterator();
    } //-- java.util.Iterator iterateInclude() 

    /**
     * Method iteratePackage
     * 
     * 
     * 
     * @return an Iterator over all possible elements in this
     * collection
     */
    public java.util.Iterator iteratePackage()
    {
        return this._packageList.iterator();
    } //-- java.util.Iterator iteratePackage() 

    /**
     * Method iterateSimpleTypeBinding
     * 
     * 
     * 
     * @return an Iterator over all possible elements in this
     * collection
     */
    public java.util.Iterator iterateSimpleTypeBinding()
    {
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
    public void removeAllAttributeBinding()
    {
        this._attributeBindingList.clear();
    } //-- void removeAllAttributeBinding() 

    /**
     */
    public void removeAllComplexTypeBinding()
    {
        this._complexTypeBindingList.clear();
    } //-- void removeAllComplexTypeBinding() 

    /**
     */
    public void removeAllElementBinding()
    {
        this._elementBindingList.clear();
    } //-- void removeAllElementBinding() 

    /**
     */
    public void removeAllEnumBinding()
    {
        this._enumBindingList.clear();
    } //-- void removeAllEnumBinding() 

    /**
     */
    public void removeAllGroupBinding()
    {
        this._groupBindingList.clear();
    } //-- void removeAllGroupBinding() 

    /**
     */
    public void removeAllInclude()
    {
        this._includeList.clear();
    } //-- void removeAllInclude() 

    /**
     */
    public void removeAllPackage()
    {
        this._packageList.clear();
    } //-- void removeAllPackage() 

    /**
     */
    public void removeAllSimpleTypeBinding()
    {
        this._simpleTypeBindingList.clear();
    } //-- void removeAllSimpleTypeBinding() 

    /**
     * Method removeAttributeBinding
     * 
     * 
     * 
     * @param vAttributeBinding
     * @return true if the object was removed from the collection.
     */
    public boolean removeAttributeBinding(org.exolab.castor.builder.binding.ComponentBindingType vAttributeBinding)
    {
        boolean removed = _attributeBindingList.remove(vAttributeBinding);
        return removed;
    } //-- boolean removeAttributeBinding(org.exolab.castor.builder.binding.ComponentBindingType) 

    /**
     * Method removeAttributeBindingAt
     * 
     * 
     * 
     * @param index
     * @return the element removed from the collection
     */
    public org.exolab.castor.builder.binding.ComponentBindingType removeAttributeBindingAt(int index)
    {
        Object obj = this._attributeBindingList.remove(index);
        return (org.exolab.castor.builder.binding.ComponentBindingType) obj;
    } //-- org.exolab.castor.builder.binding.ComponentBindingType removeAttributeBindingAt(int) 

    /**
     * Method removeComplexTypeBinding
     * 
     * 
     * 
     * @param vComplexTypeBinding
     * @return true if the object was removed from the collection.
     */
    public boolean removeComplexTypeBinding(org.exolab.castor.builder.binding.ComponentBindingType vComplexTypeBinding)
    {
        boolean removed = _complexTypeBindingList.remove(vComplexTypeBinding);
        return removed;
    } //-- boolean removeComplexTypeBinding(org.exolab.castor.builder.binding.ComponentBindingType) 

    /**
     * Method removeComplexTypeBindingAt
     * 
     * 
     * 
     * @param index
     * @return the element removed from the collection
     */
    public org.exolab.castor.builder.binding.ComponentBindingType removeComplexTypeBindingAt(int index)
    {
        Object obj = this._complexTypeBindingList.remove(index);
        return (org.exolab.castor.builder.binding.ComponentBindingType) obj;
    } //-- org.exolab.castor.builder.binding.ComponentBindingType removeComplexTypeBindingAt(int) 

    /**
     * Method removeElementBinding
     * 
     * 
     * 
     * @param vElementBinding
     * @return true if the object was removed from the collection.
     */
    public boolean removeElementBinding(org.exolab.castor.builder.binding.ComponentBindingType vElementBinding)
    {
        boolean removed = _elementBindingList.remove(vElementBinding);
        return removed;
    } //-- boolean removeElementBinding(org.exolab.castor.builder.binding.ComponentBindingType) 

    /**
     * Method removeElementBindingAt
     * 
     * 
     * 
     * @param index
     * @return the element removed from the collection
     */
    public org.exolab.castor.builder.binding.ComponentBindingType removeElementBindingAt(int index)
    {
        Object obj = this._elementBindingList.remove(index);
        return (org.exolab.castor.builder.binding.ComponentBindingType) obj;
    } //-- org.exolab.castor.builder.binding.ComponentBindingType removeElementBindingAt(int) 

    /**
     * Method removeEnumBinding
     * 
     * 
     * 
     * @param vEnumBinding
     * @return true if the object was removed from the collection.
     */
    public boolean removeEnumBinding(org.exolab.castor.builder.binding.ComponentBindingType vEnumBinding)
    {
        boolean removed = _enumBindingList.remove(vEnumBinding);
        return removed;
    } //-- boolean removeEnumBinding(org.exolab.castor.builder.binding.ComponentBindingType) 

    /**
     * Method removeEnumBindingAt
     * 
     * 
     * 
     * @param index
     * @return the element removed from the collection
     */
    public org.exolab.castor.builder.binding.ComponentBindingType removeEnumBindingAt(int index)
    {
        Object obj = this._enumBindingList.remove(index);
        return (org.exolab.castor.builder.binding.ComponentBindingType) obj;
    } //-- org.exolab.castor.builder.binding.ComponentBindingType removeEnumBindingAt(int) 

    /**
     * Method removeGroupBinding
     * 
     * 
     * 
     * @param vGroupBinding
     * @return true if the object was removed from the collection.
     */
    public boolean removeGroupBinding(org.exolab.castor.builder.binding.ComponentBindingType vGroupBinding)
    {
        boolean removed = _groupBindingList.remove(vGroupBinding);
        return removed;
    } //-- boolean removeGroupBinding(org.exolab.castor.builder.binding.ComponentBindingType) 

    /**
     * Method removeGroupBindingAt
     * 
     * 
     * 
     * @param index
     * @return the element removed from the collection
     */
    public org.exolab.castor.builder.binding.ComponentBindingType removeGroupBindingAt(int index)
    {
        Object obj = this._groupBindingList.remove(index);
        return (org.exolab.castor.builder.binding.ComponentBindingType) obj;
    } //-- org.exolab.castor.builder.binding.ComponentBindingType removeGroupBindingAt(int) 

    /**
     * Method removeInclude
     * 
     * 
     * 
     * @param vInclude
     * @return true if the object was removed from the collection.
     */
    public boolean removeInclude(org.exolab.castor.builder.binding.IncludeType vInclude)
    {
        boolean removed = _includeList.remove(vInclude);
        return removed;
    } //-- boolean removeInclude(org.exolab.castor.builder.binding.IncludeType) 

    /**
     * Method removeIncludeAt
     * 
     * 
     * 
     * @param index
     * @return the element removed from the collection
     */
    public org.exolab.castor.builder.binding.IncludeType removeIncludeAt(int index)
    {
        Object obj = this._includeList.remove(index);
        return (org.exolab.castor.builder.binding.IncludeType) obj;
    } //-- org.exolab.castor.builder.binding.IncludeType removeIncludeAt(int) 

    /**
     * Method removePackage
     * 
     * 
     * 
     * @param vPackage
     * @return true if the object was removed from the collection.
     */
    public boolean removePackage(org.exolab.castor.builder.binding.PackageType vPackage)
    {
        boolean removed = _packageList.remove(vPackage);
        return removed;
    } //-- boolean removePackage(org.exolab.castor.builder.binding.PackageType) 

    /**
     * Method removePackageAt
     * 
     * 
     * 
     * @param index
     * @return the element removed from the collection
     */
    public org.exolab.castor.builder.binding.PackageType removePackageAt(int index)
    {
        Object obj = this._packageList.remove(index);
        return (org.exolab.castor.builder.binding.PackageType) obj;
    } //-- org.exolab.castor.builder.binding.PackageType removePackageAt(int) 

    /**
     * Method removeSimpleTypeBinding
     * 
     * 
     * 
     * @param vSimpleTypeBinding
     * @return true if the object was removed from the collection.
     */
    public boolean removeSimpleTypeBinding(org.exolab.castor.builder.binding.ComponentBindingType vSimpleTypeBinding)
    {
        boolean removed = _simpleTypeBindingList.remove(vSimpleTypeBinding);
        return removed;
    } //-- boolean removeSimpleTypeBinding(org.exolab.castor.builder.binding.ComponentBindingType) 

    /**
     * Method removeSimpleTypeBindingAt
     * 
     * 
     * 
     * @param index
     * @return the element removed from the collection
     */
    public org.exolab.castor.builder.binding.ComponentBindingType removeSimpleTypeBindingAt(int index)
    {
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
    public void setAttributeBinding(int index, org.exolab.castor.builder.binding.ComponentBindingType vAttributeBinding)
        throws java.lang.IndexOutOfBoundsException
    {
        // check bounds for index
        if (index < 0 || index >= this._attributeBindingList.size()) {
            throw new IndexOutOfBoundsException("setAttributeBinding: Index value '" + index + "' not in range [0.." + (this._attributeBindingList.size() - 1) + "]");
        }
        
        this._attributeBindingList.set(index, vAttributeBinding);
    } //-- void setAttributeBinding(int, org.exolab.castor.builder.binding.ComponentBindingType) 

    /**
     * 
     * 
     * @param vAttributeBindingArray
     */
    public void setAttributeBinding(org.exolab.castor.builder.binding.ComponentBindingType[] vAttributeBindingArray)
    {
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
    public void setComplexTypeBinding(int index, org.exolab.castor.builder.binding.ComponentBindingType vComplexTypeBinding)
        throws java.lang.IndexOutOfBoundsException
    {
        // check bounds for index
        if (index < 0 || index >= this._complexTypeBindingList.size()) {
            throw new IndexOutOfBoundsException("setComplexTypeBinding: Index value '" + index + "' not in range [0.." + (this._complexTypeBindingList.size() - 1) + "]");
        }
        
        this._complexTypeBindingList.set(index, vComplexTypeBinding);
    } //-- void setComplexTypeBinding(int, org.exolab.castor.builder.binding.ComponentBindingType) 

    /**
     * 
     * 
     * @param vComplexTypeBindingArray
     */
    public void setComplexTypeBinding(org.exolab.castor.builder.binding.ComponentBindingType[] vComplexTypeBindingArray)
    {
        //-- copy array
        _complexTypeBindingList.clear();
        
        for (int i = 0; i < vComplexTypeBindingArray.length; i++) {
                this._complexTypeBindingList.add(vComplexTypeBindingArray[i]);
        }
    } //-- void setComplexTypeBinding(org.exolab.castor.builder.binding.ComponentBindingType) 

    /**
     * Sets the value of field 'defaultBindingType'.
     * 
     * @param defaultBindingType the value of field
     * 'defaultBindingType'.
     */
    public void setDefaultBindingType(org.exolab.castor.builder.binding.types.BindingType defaultBindingType)
    {
        this._defaultBindingType = defaultBindingType;
    } //-- void setDefaultBindingType(org.exolab.castor.builder.binding.types.BindingType) 

    /**
     * 
     * 
     * @param index
     * @param vElementBinding
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void setElementBinding(int index, org.exolab.castor.builder.binding.ComponentBindingType vElementBinding)
        throws java.lang.IndexOutOfBoundsException
    {
        // check bounds for index
        if (index < 0 || index >= this._elementBindingList.size()) {
            throw new IndexOutOfBoundsException("setElementBinding: Index value '" + index + "' not in range [0.." + (this._elementBindingList.size() - 1) + "]");
        }
        
        this._elementBindingList.set(index, vElementBinding);
    } //-- void setElementBinding(int, org.exolab.castor.builder.binding.ComponentBindingType) 

    /**
     * 
     * 
     * @param vElementBindingArray
     */
    public void setElementBinding(org.exolab.castor.builder.binding.ComponentBindingType[] vElementBindingArray)
    {
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
    public void setEnumBinding(int index, org.exolab.castor.builder.binding.ComponentBindingType vEnumBinding)
        throws java.lang.IndexOutOfBoundsException
    {
        // check bounds for index
        if (index < 0 || index >= this._enumBindingList.size()) {
            throw new IndexOutOfBoundsException("setEnumBinding: Index value '" + index + "' not in range [0.." + (this._enumBindingList.size() - 1) + "]");
        }
        
        this._enumBindingList.set(index, vEnumBinding);
    } //-- void setEnumBinding(int, org.exolab.castor.builder.binding.ComponentBindingType) 

    /**
     * 
     * 
     * @param vEnumBindingArray
     */
    public void setEnumBinding(org.exolab.castor.builder.binding.ComponentBindingType[] vEnumBindingArray)
    {
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
    public void setGroupBinding(int index, org.exolab.castor.builder.binding.ComponentBindingType vGroupBinding)
        throws java.lang.IndexOutOfBoundsException
    {
        // check bounds for index
        if (index < 0 || index >= this._groupBindingList.size()) {
            throw new IndexOutOfBoundsException("setGroupBinding: Index value '" + index + "' not in range [0.." + (this._groupBindingList.size() - 1) + "]");
        }
        
        this._groupBindingList.set(index, vGroupBinding);
    } //-- void setGroupBinding(int, org.exolab.castor.builder.binding.ComponentBindingType) 

    /**
     * 
     * 
     * @param vGroupBindingArray
     */
    public void setGroupBinding(org.exolab.castor.builder.binding.ComponentBindingType[] vGroupBindingArray)
    {
        //-- copy array
        _groupBindingList.clear();
        
        for (int i = 0; i < vGroupBindingArray.length; i++) {
                this._groupBindingList.add(vGroupBindingArray[i]);
        }
    } //-- void setGroupBinding(org.exolab.castor.builder.binding.ComponentBindingType) 

    /**
     * 
     * 
     * @param index
     * @param vInclude
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void setInclude(int index, org.exolab.castor.builder.binding.IncludeType vInclude)
        throws java.lang.IndexOutOfBoundsException
    {
        // check bounds for index
        if (index < 0 || index >= this._includeList.size()) {
            throw new IndexOutOfBoundsException("setInclude: Index value '" + index + "' not in range [0.." + (this._includeList.size() - 1) + "]");
        }
        
        this._includeList.set(index, vInclude);
    } //-- void setInclude(int, org.exolab.castor.builder.binding.IncludeType) 

    /**
     * 
     * 
     * @param vIncludeArray
     */
    public void setInclude(org.exolab.castor.builder.binding.IncludeType[] vIncludeArray)
    {
        //-- copy array
        _includeList.clear();
        
        for (int i = 0; i < vIncludeArray.length; i++) {
                this._includeList.add(vIncludeArray[i]);
        }
    } //-- void setInclude(org.exolab.castor.builder.binding.IncludeType) 

    /**
     * Sets the value of field 'namingXML'.
     * 
     * @param namingXML the value of field 'namingXML'.
     */
    public void setNamingXML(org.exolab.castor.builder.binding.NamingXMLType namingXML)
    {
        this._namingXML = namingXML;
    } //-- void setNamingXML(org.exolab.castor.builder.binding.NamingXMLType) 

    /**
     * 
     * 
     * @param index
     * @param vPackage
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void setPackage(int index, org.exolab.castor.builder.binding.PackageType vPackage)
        throws java.lang.IndexOutOfBoundsException
    {
        // check bounds for index
        if (index < 0 || index >= this._packageList.size()) {
            throw new IndexOutOfBoundsException("setPackage: Index value '" + index + "' not in range [0.." + (this._packageList.size() - 1) + "]");
        }
        
        this._packageList.set(index, vPackage);
    } //-- void setPackage(int, org.exolab.castor.builder.binding.PackageType) 

    /**
     * 
     * 
     * @param vPackageArray
     */
    public void setPackage(org.exolab.castor.builder.binding.PackageType[] vPackageArray)
    {
        //-- copy array
        _packageList.clear();
        
        for (int i = 0; i < vPackageArray.length; i++) {
                this._packageList.add(vPackageArray[i]);
        }
    } //-- void setPackage(org.exolab.castor.builder.binding.PackageType) 

    /**
     * 
     * 
     * @param index
     * @param vSimpleTypeBinding
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void setSimpleTypeBinding(int index, org.exolab.castor.builder.binding.ComponentBindingType vSimpleTypeBinding)
        throws java.lang.IndexOutOfBoundsException
    {
        // check bounds for index
        if (index < 0 || index >= this._simpleTypeBindingList.size()) {
            throw new IndexOutOfBoundsException("setSimpleTypeBinding: Index value '" + index + "' not in range [0.." + (this._simpleTypeBindingList.size() - 1) + "]");
        }
        
        this._simpleTypeBindingList.set(index, vSimpleTypeBinding);
    } //-- void setSimpleTypeBinding(int, org.exolab.castor.builder.binding.ComponentBindingType) 

    /**
     * 
     * 
     * @param vSimpleTypeBindingArray
     */
    public void setSimpleTypeBinding(org.exolab.castor.builder.binding.ComponentBindingType[] vSimpleTypeBindingArray)
    {
        //-- copy array
        _simpleTypeBindingList.clear();
        
        for (int i = 0; i < vSimpleTypeBindingArray.length; i++) {
                this._simpleTypeBindingList.add(vSimpleTypeBindingArray[i]);
        }
    } //-- void setSimpleTypeBinding(org.exolab.castor.builder.binding.ComponentBindingType) 

    /**
     * Method unmarshalBinding
     * 
     * 
     * 
     * @param reader
     * @throws org.exolab.castor.xml.MarshalException if object is
     * null or if any SAXException is thrown during marshaling
     * @throws org.exolab.castor.xml.ValidationException if this
     * object is an invalid instance according to the schema
     * @return the unmarshaled
     * org.exolab.castor.builder.binding.Binding
     */
    public static org.exolab.castor.builder.binding.Binding unmarshalBinding(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.exolab.castor.builder.binding.Binding) Unmarshaller.unmarshal(org.exolab.castor.builder.binding.Binding.class, reader);
    } //-- org.exolab.castor.builder.binding.Binding unmarshalBinding(java.io.Reader) 

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
