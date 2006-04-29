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
 *                 This type gathers the needed information to
 * generate a Java Class
 *                 from a binding file. Options such as generating
 * the equals method,
 *                 using wrapper classes for primitives or using
 * bound properties can
 *                 be defined via that element. When defined
 * locally the options override
 *                 the values defined in the castor.properties
 * file.
 *             
 * 
 * @version $Revision$ $Date$
**/
public class ClassType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    private java.lang.String _package;

    private java.lang.String _name;

    private boolean _final;

    /**
     * keeps track of state for field: _final
    **/
    private boolean _has_final;

    private boolean _abstract;

    /**
     * keeps track of state for field: _abstract
    **/
    private boolean _has_abstract;

    private boolean _equals;

    /**
     * keeps track of state for field: _equals
    **/
    private boolean _has_equals;

    private boolean _bound;

    /**
     * keeps track of state for field: _bound
    **/
    private boolean _has_bound;

    private java.util.Vector _implementsList;

    private java.lang.String _extends;


      //----------------/
     //- Constructors -/
    //----------------/

    public ClassType() {
        super();
        _implementsList = new Vector();
    } //-- org.exolab.castor.builder.binding.ClassType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * 
     * 
     * @param vImplements
    **/
    public void addImplements(java.lang.String vImplements)
        throws java.lang.IndexOutOfBoundsException
    {
        _implementsList.addElement(vImplements);
    } //-- void addImplements(java.lang.String) 

    /**
     * 
     * 
     * @param index
     * @param vImplements
    **/
    public void addImplements(int index, java.lang.String vImplements)
        throws java.lang.IndexOutOfBoundsException
    {
        _implementsList.insertElementAt(vImplements, index);
    } //-- void addImplements(int, java.lang.String) 

    /**
    **/
    public void deleteAbstract()
    {
        this._has_abstract= false;
    } //-- void deleteAbstract() 

    /**
    **/
    public void deleteBound()
    {
        this._has_bound= false;
    } //-- void deleteBound() 

    /**
    **/
    public void deleteEquals()
    {
        this._has_equals= false;
    } //-- void deleteEquals() 

    /**
    **/
    public void deleteFinal()
    {
        this._has_final= false;
    } //-- void deleteFinal() 

    /**
    **/
    public java.util.Enumeration enumerateImplements()
    {
        return _implementsList.elements();
    } //-- java.util.Enumeration enumerateImplements() 

    /**
     * Returns the value of field 'abstract'.
     * 
     * @return the value of field 'abstract'.
    **/
    public boolean getAbstract()
    {
        return this._abstract;
    } //-- boolean getAbstract() 

    /**
     * Returns the value of field 'bound'.
     * 
     * @return the value of field 'bound'.
    **/
    public boolean getBound()
    {
        return this._bound;
    } //-- boolean getBound() 

    /**
     * Returns the value of field 'equals'.
     * 
     * @return the value of field 'equals'.
    **/
    public boolean getEquals()
    {
        return this._equals;
    } //-- boolean getEquals() 

    /**
     * Returns the value of field 'extends'.
     * 
     * @return the value of field 'extends'.
    **/
    public java.lang.String getExtends()
    {
        return this._extends;
    } //-- java.lang.String getExtends() 

    /**
     * Returns the value of field 'final'.
     * 
     * @return the value of field 'final'.
    **/
    public boolean getFinal()
    {
        return this._final;
    } //-- boolean getFinal() 

    /**
     * 
     * 
     * @param index
    **/
    public java.lang.String getImplements(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _implementsList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (String)_implementsList.elementAt(index);
    } //-- java.lang.String getImplements(int) 

    /**
    **/
    public java.lang.String[] getImplements()
    {
        int size = _implementsList.size();
        java.lang.String[] mArray = new java.lang.String[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (String)_implementsList.elementAt(index);
        }
        return mArray;
    } //-- java.lang.String[] getImplements() 

    /**
    **/
    public int getImplementsCount()
    {
        return _implementsList.size();
    } //-- int getImplementsCount() 

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
     * Returns the value of field 'package'.
     * 
     * @return the value of field 'package'.
    **/
    public java.lang.String getPackage()
    {
        return this._package;
    } //-- java.lang.String getPackage() 

    /**
    **/
    public boolean hasAbstract()
    {
        return this._has_abstract;
    } //-- boolean hasAbstract() 

    /**
    **/
    public boolean hasBound()
    {
        return this._has_bound;
    } //-- boolean hasBound() 

    /**
    **/
    public boolean hasEquals()
    {
        return this._has_equals;
    } //-- boolean hasEquals() 

    /**
    **/
    public boolean hasFinal()
    {
        return this._has_final;
    } //-- boolean hasFinal() 

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
    public void removeAllImplements()
    {
        _implementsList.removeAllElements();
    } //-- void removeAllImplements() 

    /**
     * 
     * 
     * @param index
    **/
    public java.lang.String removeImplements(int index)
    {
        java.lang.Object obj = _implementsList.elementAt(index);
        _implementsList.removeElementAt(index);
        return (String)obj;
    } //-- java.lang.String removeImplements(int) 

    /**
     * Sets the value of field 'abstract'.
     * 
     * @param _abstract the value of field 'abstract'.
    **/
    public void setAbstract(boolean _abstract)
    {
        this._abstract = _abstract;
        this._has_abstract = true;
    } //-- void setAbstract(boolean) 

    /**
     * Sets the value of field 'bound'.
     * 
     * @param bound the value of field 'bound'.
    **/
    public void setBound(boolean bound)
    {
        this._bound = bound;
        this._has_bound = true;
    } //-- void setBound(boolean) 

    /**
     * Sets the value of field 'equals'.
     * 
     * @param equals the value of field 'equals'.
    **/
    public void setEquals(boolean equals)
    {
        this._equals = equals;
        this._has_equals = true;
    } //-- void setEquals(boolean) 

    /**
     * Sets the value of field 'extends'.
     * 
     * @param _extends the value of field 'extends'.
    **/
    public void setExtends(java.lang.String _extends)
    {
        this._extends = _extends;
    } //-- void setExtends(java.lang.String) 

    /**
     * Sets the value of field 'final'.
     * 
     * @param _final the value of field 'final'.
    **/
    public void setFinal(boolean _final)
    {
        this._final = _final;
        this._has_final = true;
    } //-- void setFinal(boolean) 

    /**
     * 
     * 
     * @param index
     * @param vImplements
    **/
    public void setImplements(int index, java.lang.String vImplements)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _implementsList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _implementsList.setElementAt(vImplements, index);
    } //-- void setImplements(int, java.lang.String) 

    /**
     * 
     * 
     * @param _implementsArray
    **/
    public void setImplements(java.lang.String[] _implementsArray)
    {
        //-- copy array
        _implementsList.removeAllElements();
        for (int i = 0; i < _implementsArray.length; i++) {
            _implementsList.addElement(_implementsArray[i]);
        }
    } //-- void setImplements(java.lang.String) 

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
     * Sets the value of field 'package'.
     * 
     * @param _package the value of field 'package'.
    **/
    public void setPackage(java.lang.String _package)
    {
        this._package = _package;
    } //-- void setPackage(java.lang.String) 

    /**
     * 
     * 
     * @param reader
    **/
    public static org.exolab.castor.builder.binding.ClassType unmarshalClassType(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.exolab.castor.builder.binding.ClassType) Unmarshaller.unmarshal(org.exolab.castor.builder.binding.ClassType.class, reader);
    } //-- org.exolab.castor.builder.binding.ClassType unmarshalClassType(java.io.Reader) 

    /**
    **/
    public void validate()
        throws org.exolab.castor.xml.ValidationException
    {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    } //-- void validate() 

}
