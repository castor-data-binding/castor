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

import java.util.Vector;
import org.exolab.castor.xml.*;

/**
 * 
 *                 A binding element is defined to express the
 * binding between an XML Schema Component
 *                 and Java class OR a java interface or a java
 * member class. The XML Schema component can 
 *                 be an element, an attribute, a complexType or a
 * group. Attribute cannot be mapped to 
 *                 class, the reader of a binding file will take
 * care that class or interface are not
 *                 used for component whose xml-type is attribute.
 *             
 * 
 * @version $Revision$ $Date$
**/
public class ComponentBindingType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    private java.lang.String _name;

    private ComponentBindingTypeChoice _componentBindingTypeChoice;

    private java.util.Vector _elementBindingList;

    private java.util.Vector _attributeBindingList;

    private java.util.Vector _complexTypeBindingList;

    private java.util.Vector _groupBindingList;


      //----------------/
     //- Constructors -/
    //----------------/

    public ComponentBindingType() {
        super();
        _elementBindingList = new Vector();
        _attributeBindingList = new Vector();
        _complexTypeBindingList = new Vector();
        _groupBindingList = new Vector();
    } //-- org.exolab.castor.builder.binding.ComponentBindingType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * 
     * 
     * @param vAttributeBinding
    **/
    public void addAttributeBinding(ComponentBindingType vAttributeBinding)
        throws java.lang.IndexOutOfBoundsException
    {
        _attributeBindingList.addElement(vAttributeBinding);
    } //-- void addAttributeBinding(ComponentBindingType) 

    /**
     * 
     * 
     * @param index
     * @param vAttributeBinding
    **/
    public void addAttributeBinding(int index, ComponentBindingType vAttributeBinding)
        throws java.lang.IndexOutOfBoundsException
    {
        _attributeBindingList.insertElementAt(vAttributeBinding, index);
    } //-- void addAttributeBinding(int, ComponentBindingType) 

    /**
     * 
     * 
     * @param vComplexTypeBinding
    **/
    public void addComplexTypeBinding(ComponentBindingType vComplexTypeBinding)
        throws java.lang.IndexOutOfBoundsException
    {
        _complexTypeBindingList.addElement(vComplexTypeBinding);
    } //-- void addComplexTypeBinding(ComponentBindingType) 

    /**
     * 
     * 
     * @param index
     * @param vComplexTypeBinding
    **/
    public void addComplexTypeBinding(int index, ComponentBindingType vComplexTypeBinding)
        throws java.lang.IndexOutOfBoundsException
    {
        _complexTypeBindingList.insertElementAt(vComplexTypeBinding, index);
    } //-- void addComplexTypeBinding(int, ComponentBindingType) 

    /**
     * 
     * 
     * @param vElementBinding
    **/
    public void addElementBinding(ComponentBindingType vElementBinding)
        throws java.lang.IndexOutOfBoundsException
    {
        _elementBindingList.addElement(vElementBinding);
    } //-- void addElementBinding(ComponentBindingType) 

    /**
     * 
     * 
     * @param index
     * @param vElementBinding
    **/
    public void addElementBinding(int index, ComponentBindingType vElementBinding)
        throws java.lang.IndexOutOfBoundsException
    {
        _elementBindingList.insertElementAt(vElementBinding, index);
    } //-- void addElementBinding(int, ComponentBindingType) 

    /**
     * 
     * 
     * @param vGroupBinding
    **/
    public void addGroupBinding(ComponentBindingType vGroupBinding)
        throws java.lang.IndexOutOfBoundsException
    {
        _groupBindingList.addElement(vGroupBinding);
    } //-- void addGroupBinding(ComponentBindingType) 

    /**
     * 
     * 
     * @param index
     * @param vGroupBinding
    **/
    public void addGroupBinding(int index, ComponentBindingType vGroupBinding)
        throws java.lang.IndexOutOfBoundsException
    {
        _groupBindingList.insertElementAt(vGroupBinding, index);
    } //-- void addGroupBinding(int, ComponentBindingType) 

    /**
    **/
    public java.util.Enumeration enumerateAttributeBinding()
    {
        return _attributeBindingList.elements();
    } //-- java.util.Enumeration enumerateAttributeBinding() 

    /**
    **/
    public java.util.Enumeration enumerateComplexTypeBinding()
    {
        return _complexTypeBindingList.elements();
    } //-- java.util.Enumeration enumerateComplexTypeBinding() 

    /**
    **/
    public java.util.Enumeration enumerateElementBinding()
    {
        return _elementBindingList.elements();
    } //-- java.util.Enumeration enumerateElementBinding() 

    /**
    **/
    public java.util.Enumeration enumerateGroupBinding()
    {
        return _groupBindingList.elements();
    } //-- java.util.Enumeration enumerateGroupBinding() 

    /**
     * 
     * 
     * @param index
    **/
    public ComponentBindingType getAttributeBinding(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _attributeBindingList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (ComponentBindingType) _attributeBindingList.elementAt(index);
    } //-- ComponentBindingType getAttributeBinding(int) 

    /**
    **/
    public ComponentBindingType[] getAttributeBinding()
    {
        int size = _attributeBindingList.size();
        ComponentBindingType[] mArray = new ComponentBindingType[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (ComponentBindingType) _attributeBindingList.elementAt(index);
        }
        return mArray;
    } //-- ComponentBindingType[] getAttributeBinding() 

    /**
    **/
    public int getAttributeBindingCount()
    {
        return _attributeBindingList.size();
    } //-- int getAttributeBindingCount() 

    /**
     * 
     * 
     * @param index
    **/
    public ComponentBindingType getComplexTypeBinding(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _complexTypeBindingList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (ComponentBindingType) _complexTypeBindingList.elementAt(index);
    } //-- ComponentBindingType getComplexTypeBinding(int) 

    /**
    **/
    public ComponentBindingType[] getComplexTypeBinding()
    {
        int size = _complexTypeBindingList.size();
        ComponentBindingType[] mArray = new ComponentBindingType[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (ComponentBindingType) _complexTypeBindingList.elementAt(index);
        }
        return mArray;
    } //-- ComponentBindingType[] getComplexTypeBinding() 

    /**
    **/
    public int getComplexTypeBindingCount()
    {
        return _complexTypeBindingList.size();
    } //-- int getComplexTypeBindingCount() 

    /**
     * Returns the value of field 'componentBindingTypeChoice'.
     * 
     * @return the value of field 'componentBindingTypeChoice'.
    **/
    public ComponentBindingTypeChoice getComponentBindingTypeChoice()
    {
        return this._componentBindingTypeChoice;
    } //-- ComponentBindingTypeChoice getComponentBindingTypeChoice() 

    /**
     * 
     * 
     * @param index
    **/
    public ComponentBindingType getElementBinding(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _elementBindingList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (ComponentBindingType) _elementBindingList.elementAt(index);
    } //-- ComponentBindingType getElementBinding(int) 

    /**
    **/
    public ComponentBindingType[] getElementBinding()
    {
        int size = _elementBindingList.size();
        ComponentBindingType[] mArray = new ComponentBindingType[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (ComponentBindingType) _elementBindingList.elementAt(index);
        }
        return mArray;
    } //-- ComponentBindingType[] getElementBinding() 

    /**
    **/
    public int getElementBindingCount()
    {
        return _elementBindingList.size();
    } //-- int getElementBindingCount() 

    /**
     * 
     * 
     * @param index
    **/
    public ComponentBindingType getGroupBinding(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _groupBindingList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (ComponentBindingType) _groupBindingList.elementAt(index);
    } //-- ComponentBindingType getGroupBinding(int) 

    /**
    **/
    public ComponentBindingType[] getGroupBinding()
    {
        int size = _groupBindingList.size();
        ComponentBindingType[] mArray = new ComponentBindingType[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (ComponentBindingType) _groupBindingList.elementAt(index);
        }
        return mArray;
    } //-- ComponentBindingType[] getGroupBinding() 

    /**
    **/
    public int getGroupBindingCount()
    {
        return _groupBindingList.size();
    } //-- int getGroupBindingCount() 

    /**
     * Returns the value of field 'name'.
     * 
     * @return the value of field 'name'.
    **/
    public java.lang.String getName()
    {
        return this._name;
    } //-- java.lang.String getName() 

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
    **/
    public void removeAllAttributeBinding()
    {
        _attributeBindingList.removeAllElements();
    } //-- void removeAllAttributeBinding() 

    /**
    **/
    public void removeAllComplexTypeBinding()
    {
        _complexTypeBindingList.removeAllElements();
    } //-- void removeAllComplexTypeBinding() 

    /**
    **/
    public void removeAllElementBinding()
    {
        _elementBindingList.removeAllElements();
    } //-- void removeAllElementBinding() 

    /**
    **/
    public void removeAllGroupBinding()
    {
        _groupBindingList.removeAllElements();
    } //-- void removeAllGroupBinding() 

    /**
     * 
     * 
     * @param index
    **/
    public ComponentBindingType removeAttributeBinding(int index)
    {
        java.lang.Object obj = _attributeBindingList.elementAt(index);
        _attributeBindingList.removeElementAt(index);
        return (ComponentBindingType) obj;
    } //-- ComponentBindingType removeAttributeBinding(int) 

    /**
     * 
     * 
     * @param index
    **/
    public ComponentBindingType removeComplexTypeBinding(int index)
    {
        java.lang.Object obj = _complexTypeBindingList.elementAt(index);
        _complexTypeBindingList.removeElementAt(index);
        return (ComponentBindingType) obj;
    } //-- ComponentBindingType removeComplexTypeBinding(int) 

    /**
     * 
     * 
     * @param index
    **/
    public ComponentBindingType removeElementBinding(int index)
    {
        java.lang.Object obj = _elementBindingList.elementAt(index);
        _elementBindingList.removeElementAt(index);
        return (ComponentBindingType) obj;
    } //-- ComponentBindingType removeElementBinding(int) 

    /**
     * 
     * 
     * @param index
    **/
    public ComponentBindingType removeGroupBinding(int index)
    {
        java.lang.Object obj = _groupBindingList.elementAt(index);
        _groupBindingList.removeElementAt(index);
        return (ComponentBindingType) obj;
    } //-- ComponentBindingType removeGroupBinding(int) 

    /**
     * 
     * 
     * @param index
     * @param vAttributeBinding
    **/
    public void setAttributeBinding(int index, ComponentBindingType vAttributeBinding)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _attributeBindingList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _attributeBindingList.setElementAt(vAttributeBinding, index);
    } //-- void setAttributeBinding(int, ComponentBindingType) 

    /**
     * 
     * 
     * @param attributeBindingArray
    **/
    public void setAttributeBinding(ComponentBindingType[] attributeBindingArray)
    {
        //-- copy array
        _attributeBindingList.removeAllElements();
        for (int i = 0; i < attributeBindingArray.length; i++) {
            _attributeBindingList.addElement(attributeBindingArray[i]);
        }
    } //-- void setAttributeBinding(ComponentBindingType) 

    /**
     * 
     * 
     * @param index
     * @param vComplexTypeBinding
    **/
    public void setComplexTypeBinding(int index, ComponentBindingType vComplexTypeBinding)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _complexTypeBindingList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _complexTypeBindingList.setElementAt(vComplexTypeBinding, index);
    } //-- void setComplexTypeBinding(int, ComponentBindingType) 

    /**
     * 
     * 
     * @param complexTypeBindingArray
    **/
    public void setComplexTypeBinding(ComponentBindingType[] complexTypeBindingArray)
    {
        //-- copy array
        _complexTypeBindingList.removeAllElements();
        for (int i = 0; i < complexTypeBindingArray.length; i++) {
            _complexTypeBindingList.addElement(complexTypeBindingArray[i]);
        }
    } //-- void setComplexTypeBinding(ComponentBindingType) 

    /**
     * Sets the value of field 'componentBindingTypeChoice'.
     * 
     * @param componentBindingTypeChoice the value of field
     * 'componentBindingTypeChoice'.
    **/
    public void setComponentBindingTypeChoice(ComponentBindingTypeChoice componentBindingTypeChoice)
    {
        this._componentBindingTypeChoice = componentBindingTypeChoice;
    } //-- void setComponentBindingTypeChoice(ComponentBindingTypeChoice) 

    /**
     * 
     * 
     * @param index
     * @param vElementBinding
    **/
    public void setElementBinding(int index, ComponentBindingType vElementBinding)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _elementBindingList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _elementBindingList.setElementAt(vElementBinding, index);
    } //-- void setElementBinding(int, ComponentBindingType) 

    /**
     * 
     * 
     * @param elementBindingArray
    **/
    public void setElementBinding(ComponentBindingType[] elementBindingArray)
    {
        //-- copy array
        _elementBindingList.removeAllElements();
        for (int i = 0; i < elementBindingArray.length; i++) {
            _elementBindingList.addElement(elementBindingArray[i]);
        }
    } //-- void setElementBinding(ComponentBindingType) 

    /**
     * 
     * 
     * @param index
     * @param vGroupBinding
    **/
    public void setGroupBinding(int index, ComponentBindingType vGroupBinding)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _groupBindingList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _groupBindingList.setElementAt(vGroupBinding, index);
    } //-- void setGroupBinding(int, ComponentBindingType) 

    /**
     * 
     * 
     * @param groupBindingArray
    **/
    public void setGroupBinding(ComponentBindingType[] groupBindingArray)
    {
        //-- copy array
        _groupBindingList.removeAllElements();
        for (int i = 0; i < groupBindingArray.length; i++) {
            _groupBindingList.addElement(groupBindingArray[i]);
        }
    } //-- void setGroupBinding(ComponentBindingType) 

    /**
     * Sets the value of field 'name'.
     * 
     * @param name the value of field 'name'.
    **/
    public void setName(java.lang.String name)
    {
        this._name = name;
    } //-- void setName(java.lang.String) 

    /**
     * 
     * 
     * @param reader
    **/
    public static org.exolab.castor.builder.binding.ComponentBindingType unmarshalComponentBindingType(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.exolab.castor.builder.binding.ComponentBindingType) Unmarshaller.unmarshal(org.exolab.castor.builder.binding.ComponentBindingType.class, reader);
    } //-- org.exolab.castor.builder.binding.ComponentBindingType unmarshalComponentBindingType(java.io.Reader) 

    /**
    **/
    public void validate()
        throws org.exolab.castor.xml.ValidationException
    {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    } //-- void validate() 

}
