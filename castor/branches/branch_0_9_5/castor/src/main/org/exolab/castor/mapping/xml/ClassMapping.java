/*
 * This class was automatically generated with 
 * <a href="http://castor.exolab.org">Castor 0.8.12</a>, using an
 * XML Schema.
 * $Id$
 */

package org.exolab.castor.mapping.xml;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import java.util.Enumeration;
import java.util.Vector;
import org.exolab.castor.mapping.xml.types.AccessType;
import org.exolab.castor.xml.*;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.DocumentHandler;

/**
 * 
 * @version $Revision$ $Date$
**/
public class ClassMapping implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    private java.lang.String _name;

    private java.lang.Object _extends;

    private java.lang.Object _depends;

    private java.util.Vector _identity;

    private java.lang.String _timestamp;

    private org.exolab.castor.mapping.xml.types.AccessType _access = org.exolab.castor.mapping.xml.types.AccessType.valueOf("shared");;

    private java.lang.String _keyGenerator;

    private boolean _autoComplete = false;

    /**
     * keeps track of state for field: _autoComplete
    **/
    private boolean _has_autoComplete;

    private java.lang.String _description;

    private CacheTypeMapping _cacheTypeMapping;

    private MapTo _mapTo;

    private java.util.Vector _fieldMappingList;

    private java.util.Vector _containerList;


    /**
     * A flag which allows by-passing Castor's check for
     * whether or not a class has a default constructor
     */
    private boolean _verifyConstructable = true;
    
      //----------------/
     //- Constructors -/
    //----------------/

    public ClassMapping() {
        super();
        _identity = new Vector();
        _fieldMappingList = new Vector();
        _containerList = new Vector();
    } //-- org.exolab.castor.mapping.xml.ClassMapping()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * 
     * @param vContainer
    **/
    public void addContainer(Container vContainer)
        throws java.lang.IndexOutOfBoundsException
    {
        _containerList.addElement(vContainer);
    } //-- void addContainer(Container) 

    /**
     * 
     * @param vFieldMapping
    **/
    public void addFieldMapping(FieldMapping vFieldMapping)
        throws java.lang.IndexOutOfBoundsException
    {
        _fieldMappingList.addElement(vFieldMapping);
    } //-- void addFieldMapping(FieldMapping) 

    /**
     * 
     * @param vIdentity
    **/
    public void addIdentity(java.lang.String vIdentity)
        throws java.lang.IndexOutOfBoundsException
    {
        _identity.addElement(vIdentity);
    } //-- void addIdentity(java.lang.String) 

    /**
    **/
    public void deleteAutoComplete()
    {
        this._has_autoComplete= false;
    } //-- void deleteAutoComplete() 

    /**
    **/
    public java.util.Enumeration enumerateContainer()
    {
        return _containerList.elements();
    } //-- java.util.Enumeration enumerateContainer() 

    /**
    **/
    public java.util.Enumeration enumerateFieldMapping()
    {
        return _fieldMappingList.elements();
    } //-- java.util.Enumeration enumerateFieldMapping() 

    /**
    **/
    public java.util.Enumeration enumerateIdentity()
    {
        return _identity.elements();
    } //-- java.util.Enumeration enumerateIdentity() 

    /**
    **/
    public org.exolab.castor.mapping.xml.types.AccessType getAccess()
    {
        return this._access;
    } //-- org.exolab.castor.mapping.xml.types.AccessType getAccess() 

    /**
    **/
    public boolean getAutoComplete()
    {
        return this._autoComplete;
    } //-- boolean getAutoComplete() 

    /**
    **/
    public CacheTypeMapping getCacheTypeMapping()
    {
        return this._cacheTypeMapping;
    } //-- CacheTypeMapping getCacheTypeMapping() 

    /**
     * 
     * @param index
    **/
    public Container getContainer(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _containerList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (Container) _containerList.elementAt(index);
    } //-- Container getContainer(int) 

    /**
    **/
    public Container[] getContainer()
    {
        int size = _containerList.size();
        Container[] mArray = new Container[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (Container) _containerList.elementAt(index);
        }
        return mArray;
    } //-- Container[] getContainer() 

    /**
    **/
    public int getContainerCount()
    {
        return _containerList.size();
    } //-- int getContainerCount() 

    /**
    **/
    public java.lang.Object getDepends()
    {
        return this._depends;
    } //-- java.lang.Object getDepends() 

    /**
    **/
    public java.lang.String getDescription()
    {
        return this._description;
    } //-- java.lang.String getDescription() 

    /**
    **/
    public java.lang.Object getExtends()
    {
        return this._extends;
    } //-- java.lang.Object getExtends() 

    /**
     * 
     * @param index
    **/
    public FieldMapping getFieldMapping(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _fieldMappingList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (FieldMapping) _fieldMappingList.elementAt(index);
    } //-- FieldMapping getFieldMapping(int) 

    /**
    **/
    public FieldMapping[] getFieldMapping()
    {
        int size = _fieldMappingList.size();
        FieldMapping[] mArray = new FieldMapping[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (FieldMapping) _fieldMappingList.elementAt(index);
        }
        return mArray;
    } //-- FieldMapping[] getFieldMapping() 

    /**
    **/
    public int getFieldMappingCount()
    {
        return _fieldMappingList.size();
    } //-- int getFieldMappingCount() 

    /**
     * 
     * @param index
    **/
    public java.lang.String getIdentity(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _identity.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (String)_identity.elementAt(index);
    } //-- java.lang.String getIdentity(int) 

    /**
    **/
    public java.lang.String[] getIdentity()
    {
        int size = _identity.size();
        java.lang.String[] mArray = new String[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (String)_identity.elementAt(index);
        }
        return mArray;
    } //-- java.lang.String[] getIdentity() 

    /**
    **/
    public int getIdentityCount()
    {
        return _identity.size();
    } //-- int getIdentityCount() 


    /**
    **/
    public java.lang.String getKeyGenerator()
    {
        return this._keyGenerator;
    } //-- java.lang.String getKeyGenerator() 

    /**
    **/
    public MapTo getMapTo()
    {
        return this._mapTo;
    } //-- MapTo getMapTo() 

    /**
    **/
    public java.lang.String getName()
    {
        return this._name;
    } //-- java.lang.String getName() 

    /**
    **/
    public java.lang.String getTimestamp()
    {
        return this._timestamp;
    } //-- java.lang.String getTimestamp() 

    /**
    **/
    public boolean getVerifyConstructable()
    {
        return _verifyConstructable;
    } //-- boolean getVerifyConstructable() 
    
    /**
    **/
    public boolean hasAutoComplete()
    {
        return this._has_autoComplete;
    } //-- boolean hasAutoComplete() 

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
     * @param out
    **/
    public void marshal(java.io.Writer out)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        
        Marshaller.marshal(this, out);
    } //-- void marshal(java.io.Writer) 

    /**
     * 
     * @param handler
    **/
    public void marshal(org.xml.sax.DocumentHandler handler)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        
        Marshaller.marshal(this, handler);
    } //-- void marshal(org.xml.sax.DocumentHandler) 

    /**
    **/
    public void removeAllContainer()
    {
        _containerList.removeAllElements();
    } //-- void removeAllContainer() 

    /**
    **/
    public void removeAllFieldMapping()
    {
        _fieldMappingList.removeAllElements();
    } //-- void removeAllFieldMapping() 

    /**
    **/
    public void removeAllIdentity()
    {
        _identity.removeAllElements();
    } //-- void removeAllIdentity() 

    /**
     * 
     * @param index
    **/
    public Container removeContainer(int index)
    {
        Object obj = _containerList.elementAt(index);
        _containerList.removeElementAt(index);
        return (Container) obj;
    } //-- Container removeContainer(int) 

    /**
     * 
     * @param index
    **/
    public FieldMapping removeFieldMapping(int index)
    {
        Object obj = _fieldMappingList.elementAt(index);
        _fieldMappingList.removeElementAt(index);
        return (FieldMapping) obj;
    } //-- FieldMapping removeFieldMapping(int) 

    /**
     * 
     * @param index
    **/
    public java.lang.String removeIdentity(int index)
    {
        Object obj = _identity.elementAt(index);
        _identity.removeElementAt(index);
        return (String)obj;
    } //-- java.lang.String removeIdentity(int) 

    /**
     * 
     * @param _access
    **/
    public void setAccess(org.exolab.castor.mapping.xml.types.AccessType _access)
    {
        this._access = _access;
    } //-- void setAccess(org.exolab.castor.mapping.xml.types.AccessType) 

    /**
     * 
     * @param _autoComplete
    **/
    public void setAutoComplete(boolean _autoComplete)
    {
        this._autoComplete = _autoComplete;
        this._has_autoComplete = true;
    } //-- void setAutoComplete(boolean) 

    /**
     * 
     * @param _cacheTypeMapping
    **/
    public void setCacheTypeMapping(CacheTypeMapping _cacheTypeMapping)
    {
        this._cacheTypeMapping = _cacheTypeMapping;
    } //-- void setCacheTypeMapping(CacheTypeMapping) 

    /**
     * 
     * @param vContainer
     * @param index
    **/
    public void setContainer(Container vContainer, int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _containerList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _containerList.setElementAt(vContainer, index);
    } //-- void setContainer(Container, int) 

    /**
     * 
     * @param containerArray
    **/
    public void setContainer(Container[] containerArray)
    {
        //-- copy array
        _containerList.removeAllElements();
        for (int i = 0; i < containerArray.length; i++) {
            _containerList.addElement(containerArray[i]);
        }
    } //-- void setContainer(Container) 

    /**
     * 
     * @param _depends
    **/
    public void setDepends(java.lang.Object _depends)
    {
        this._depends = _depends;
    } //-- void setDepends(java.lang.Object) 

    /**
     * 
     * @param _description
    **/
    public void setDescription(java.lang.String _description)
    {
        this._description = _description;
    } //-- void setDescription(java.lang.String) 

    /**
     * 
     * @param _extends
    **/
    public void setExtends(java.lang.Object _extends)
    {
        this._extends = _extends;
    } //-- void setExtends(java.lang.Object) 

    /**
     * 
     * @param vFieldMapping
     * @param index
    **/
    public void setFieldMapping(FieldMapping vFieldMapping, int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _fieldMappingList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _fieldMappingList.setElementAt(vFieldMapping, index);
    } //-- void setFieldMapping(FieldMapping, int) 

    /**
     * 
     * @param fieldMappingArray
    **/
    public void setFieldMapping(FieldMapping[] fieldMappingArray)
    {
        //-- copy array
        _fieldMappingList.removeAllElements();
        for (int i = 0; i < fieldMappingArray.length; i++) {
            _fieldMappingList.addElement(fieldMappingArray[i]);
        }
    } //-- void setFieldMapping(FieldMapping) 

    /**
     * 
     * @param vIdentity
     * @param index
    **/
    public void setIdentity(java.lang.String vIdentity, int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _identity.size())) {
            throw new IndexOutOfBoundsException();
        }
        _identity.setElementAt(vIdentity, index);
    } //-- void setIdentity(java.lang.String, int) 

    /**
     * 
     * @param identityArray
    **/
    public void setIdentity(java.lang.String[] identityArray)
    {
        //-- copy array
        _identity.removeAllElements();
        for (int i = 0; i < identityArray.length; i++) {
            _identity.addElement(identityArray[i]);
        }
    } //-- void setIdentity(java.lang.String) 

    /**
     * 
     * @param _keyGenerator
    **/
    public void setKeyGenerator(java.lang.String _keyGenerator)
    {
        this._keyGenerator = _keyGenerator;
    } //-- void setKeyGenerator(java.lang.String) 

    /**
     * 
     * @param _mapTo
    **/
    public void setMapTo(MapTo _mapTo)
    {
        this._mapTo = _mapTo;
    } //-- void setMapTo(MapTo) 

    /**
     * 
     * @param _name
    **/
    public void setName(java.lang.String _name)
    {
        this._name = _name;
    } //-- void setName(java.lang.String) 

    /**
     * 
     * @param _timestamp
    **/
    public void setTimestamp(java.lang.String _timestamp)
    {
        this._timestamp = _timestamp;
    } //-- void setTimestamp(java.lang.String) 
    
    
    /**
     * 
     * @param verify
    **/
    public void setVerifyConstructable(boolean verify)
    {
        this._verifyConstructable = verify;
    } //-- void setVerifyConstructable(boolean) 

    /**
     * 
     * @param reader
    **/
    public static org.exolab.castor.mapping.xml.ClassMapping unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.exolab.castor.mapping.xml.ClassMapping) Unmarshaller.unmarshal(org.exolab.castor.mapping.xml.ClassMapping.class, reader);
    } //-- org.exolab.castor.mapping.xml.ClassMapping unmarshal(java.io.Reader) 

    /**
    **/
    public void validate()
        throws org.exolab.castor.xml.ValidationException
    {
        Validator validator = new Validator();
        validator.validate(this);
    } //-- void validate() 

}
