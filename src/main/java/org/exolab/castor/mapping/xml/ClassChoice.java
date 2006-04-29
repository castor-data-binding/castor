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
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.ContentHandler;

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
     * Field _fieldMappingList
     */
    private java.util.ArrayList _fieldMappingList;

    /**
     * Field _containerList
     */
    private java.util.ArrayList _containerList;


      //----------------/
     //- Constructors -/
    //----------------/

    public ClassChoice() 
     {
        super();
        _fieldMappingList = new ArrayList();
        _containerList = new ArrayList();
    } //-- org.exolab.castor.mapping.xml.ClassChoice()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addContainer
     * 
     * 
     * 
     * @param vContainer
     */
    public void addContainer(org.exolab.castor.mapping.xml.Container vContainer)
        throws java.lang.IndexOutOfBoundsException
    {
        _containerList.add(vContainer);
    } //-- void addContainer(org.exolab.castor.mapping.xml.Container) 

    /**
     * Method addContainer
     * 
     * 
     * 
     * @param index
     * @param vContainer
     */
    public void addContainer(int index, org.exolab.castor.mapping.xml.Container vContainer)
        throws java.lang.IndexOutOfBoundsException
    {
        _containerList.add(index, vContainer);
    } //-- void addContainer(int, org.exolab.castor.mapping.xml.Container) 

    /**
     * Method addFieldMapping
     * 
     * 
     * 
     * @param vFieldMapping
     */
    public void addFieldMapping(org.exolab.castor.mapping.xml.FieldMapping vFieldMapping)
        throws java.lang.IndexOutOfBoundsException
    {
        _fieldMappingList.add(vFieldMapping);
    } //-- void addFieldMapping(org.exolab.castor.mapping.xml.FieldMapping) 

    /**
     * Method addFieldMapping
     * 
     * 
     * 
     * @param index
     * @param vFieldMapping
     */
    public void addFieldMapping(int index, org.exolab.castor.mapping.xml.FieldMapping vFieldMapping)
        throws java.lang.IndexOutOfBoundsException
    {
        _fieldMappingList.add(index, vFieldMapping);
    } //-- void addFieldMapping(int, org.exolab.castor.mapping.xml.FieldMapping) 

    /**
     * Method clearContainer
     * 
     */
    public void clearContainer()
    {
        _containerList.clear();
    } //-- void clearContainer() 

    /**
     * Method clearFieldMapping
     * 
     */
    public void clearFieldMapping()
    {
        _fieldMappingList.clear();
    } //-- void clearFieldMapping() 

    /**
     * Method enumerateContainer
     * 
     * 
     * 
     * @return Enumeration
     */
    public java.util.Enumeration enumerateContainer()
    {
        return new org.exolab.castor.util.IteratorEnumeration(_containerList.iterator());
    } //-- java.util.Enumeration enumerateContainer() 

    /**
     * Method enumerateFieldMapping
     * 
     * 
     * 
     * @return Enumeration
     */
    public java.util.Enumeration enumerateFieldMapping()
    {
        return new org.exolab.castor.util.IteratorEnumeration(_fieldMappingList.iterator());
    } //-- java.util.Enumeration enumerateFieldMapping() 

    /**
     * Method getContainer
     * 
     * 
     * 
     * @param index
     * @return Container
     */
    public org.exolab.castor.mapping.xml.Container getContainer(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _containerList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (org.exolab.castor.mapping.xml.Container) _containerList.get(index);
    } //-- org.exolab.castor.mapping.xml.Container getContainer(int) 

    /**
     * Method getContainer
     * 
     * 
     * 
     * @return Container
     */
    public org.exolab.castor.mapping.xml.Container[] getContainer()
    {
        int size = _containerList.size();
        org.exolab.castor.mapping.xml.Container[] mArray = new org.exolab.castor.mapping.xml.Container[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (org.exolab.castor.mapping.xml.Container) _containerList.get(index);
        }
        return mArray;
    } //-- org.exolab.castor.mapping.xml.Container[] getContainer() 

    /**
     * Method getContainerCount
     * 
     * 
     * 
     * @return int
     */
    public int getContainerCount()
    {
        return _containerList.size();
    } //-- int getContainerCount() 

    /**
     * Method getFieldMapping
     * 
     * 
     * 
     * @param index
     * @return FieldMapping
     */
    public org.exolab.castor.mapping.xml.FieldMapping getFieldMapping(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _fieldMappingList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (org.exolab.castor.mapping.xml.FieldMapping) _fieldMappingList.get(index);
    } //-- org.exolab.castor.mapping.xml.FieldMapping getFieldMapping(int) 

    /**
     * Method getFieldMapping
     * 
     * 
     * 
     * @return FieldMapping
     */
    public org.exolab.castor.mapping.xml.FieldMapping[] getFieldMapping()
    {
        int size = _fieldMappingList.size();
        org.exolab.castor.mapping.xml.FieldMapping[] mArray = new org.exolab.castor.mapping.xml.FieldMapping[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (org.exolab.castor.mapping.xml.FieldMapping) _fieldMappingList.get(index);
        }
        return mArray;
    } //-- org.exolab.castor.mapping.xml.FieldMapping[] getFieldMapping() 

    /**
     * Method getFieldMappingCount
     * 
     * 
     * 
     * @return int
     */
    public int getFieldMappingCount()
    {
        return _fieldMappingList.size();
    } //-- int getFieldMappingCount() 

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
     * Method removeContainer
     * 
     * 
     * 
     * @param vContainer
     * @return boolean
     */
    public boolean removeContainer(org.exolab.castor.mapping.xml.Container vContainer)
    {
        boolean removed = _containerList.remove(vContainer);
        return removed;
    } //-- boolean removeContainer(org.exolab.castor.mapping.xml.Container) 

    /**
     * Method removeFieldMapping
     * 
     * 
     * 
     * @param vFieldMapping
     * @return boolean
     */
    public boolean removeFieldMapping(org.exolab.castor.mapping.xml.FieldMapping vFieldMapping)
    {
        boolean removed = _fieldMappingList.remove(vFieldMapping);
        return removed;
    } //-- boolean removeFieldMapping(org.exolab.castor.mapping.xml.FieldMapping) 

    /**
     * Method setContainer
     * 
     * 
     * 
     * @param index
     * @param vContainer
     */
    public void setContainer(int index, org.exolab.castor.mapping.xml.Container vContainer)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _containerList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _containerList.set(index, vContainer);
    } //-- void setContainer(int, org.exolab.castor.mapping.xml.Container) 

    /**
     * Method setContainer
     * 
     * 
     * 
     * @param containerArray
     */
    public void setContainer(org.exolab.castor.mapping.xml.Container[] containerArray)
    {
        //-- copy array
        _containerList.clear();
        for (int i = 0; i < containerArray.length; i++) {
            _containerList.add(containerArray[i]);
        }
    } //-- void setContainer(org.exolab.castor.mapping.xml.Container) 

    /**
     * Method setFieldMapping
     * 
     * 
     * 
     * @param index
     * @param vFieldMapping
     */
    public void setFieldMapping(int index, org.exolab.castor.mapping.xml.FieldMapping vFieldMapping)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _fieldMappingList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _fieldMappingList.set(index, vFieldMapping);
    } //-- void setFieldMapping(int, org.exolab.castor.mapping.xml.FieldMapping) 

    /**
     * Method setFieldMapping
     * 
     * 
     * 
     * @param fieldMappingArray
     */
    public void setFieldMapping(org.exolab.castor.mapping.xml.FieldMapping[] fieldMappingArray)
    {
        //-- copy array
        _fieldMappingList.clear();
        for (int i = 0; i < fieldMappingArray.length; i++) {
            _fieldMappingList.add(fieldMappingArray[i]);
        }
    } //-- void setFieldMapping(org.exolab.castor.mapping.xml.FieldMapping) 

    /**
     * Method unmarshal
     * 
     * 
     * 
     * @param reader
     * @return ClassChoice
     */
    public static org.exolab.castor.mapping.xml.ClassChoice unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.exolab.castor.mapping.xml.ClassChoice) Unmarshaller.unmarshal(org.exolab.castor.mapping.xml.ClassChoice.class, reader);
    } //-- org.exolab.castor.mapping.xml.ClassChoice unmarshal(java.io.Reader) 

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
