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
import org.exolab.castor.xml.*;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.DocumentHandler;

/**
 * 
 * @version $Revision$ $Date$
**/
public class MappingRoot implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    private java.lang.String _description;

    private java.util.Vector _includeList;

    private java.util.Vector _classMappingList;

    private java.util.Vector _keyGeneratorDefList;


      //----------------/
     //- Constructors -/
    //----------------/

    public MappingRoot() {
        super();
        _includeList = new Vector();
        _classMappingList = new Vector();
        _keyGeneratorDefList = new Vector();
    } //-- org.exolab.castor.mapping.xml.MappingRoot()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * 
     * @param vClassMapping
    **/
    public void addClassMapping(ClassMapping vClassMapping)
        throws java.lang.IndexOutOfBoundsException
    {
        _classMappingList.addElement(vClassMapping);
    } //-- void addClassMapping(ClassMapping) 

    /**
     * 
     * @param vInclude
    **/
    public void addInclude(Include vInclude)
        throws java.lang.IndexOutOfBoundsException
    {
        _includeList.addElement(vInclude);
    } //-- void addInclude(Include) 

    /**
     * 
     * @param vKeyGeneratorDef
    **/
    public void addKeyGeneratorDef(KeyGeneratorDef vKeyGeneratorDef)
        throws java.lang.IndexOutOfBoundsException
    {
        _keyGeneratorDefList.addElement(vKeyGeneratorDef);
    } //-- void addKeyGeneratorDef(KeyGeneratorDef) 

    /**
    **/
    public java.util.Enumeration enumerateClassMapping()
    {
        return _classMappingList.elements();
    } //-- java.util.Enumeration enumerateClassMapping() 

    /**
    **/
    public java.util.Enumeration enumerateInclude()
    {
        return _includeList.elements();
    } //-- java.util.Enumeration enumerateInclude() 

    /**
    **/
    public java.util.Enumeration enumerateKeyGeneratorDef()
    {
        return _keyGeneratorDefList.elements();
    } //-- java.util.Enumeration enumerateKeyGeneratorDef() 

    /**
     * 
     * @param index
    **/
    public ClassMapping getClassMapping(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _classMappingList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (ClassMapping) _classMappingList.elementAt(index);
    } //-- ClassMapping getClassMapping(int) 

    /**
    **/
    public ClassMapping[] getClassMapping()
    {
        int size = _classMappingList.size();
        ClassMapping[] mArray = new ClassMapping[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (ClassMapping) _classMappingList.elementAt(index);
        }
        return mArray;
    } //-- ClassMapping[] getClassMapping() 

    /**
    **/
    public int getClassMappingCount()
    {
        return _classMappingList.size();
    } //-- int getClassMappingCount() 

    /**
    **/
    public java.lang.String getDescription()
    {
        return this._description;
    } //-- java.lang.String getDescription() 

    /**
     * 
     * @param index
    **/
    public Include getInclude(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _includeList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (Include) _includeList.elementAt(index);
    } //-- Include getInclude(int) 

    /**
    **/
    public Include[] getInclude()
    {
        int size = _includeList.size();
        Include[] mArray = new Include[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (Include) _includeList.elementAt(index);
        }
        return mArray;
    } //-- Include[] getInclude() 

    /**
    **/
    public int getIncludeCount()
    {
        return _includeList.size();
    } //-- int getIncludeCount() 

    /**
     * 
     * @param index
    **/
    public KeyGeneratorDef getKeyGeneratorDef(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _keyGeneratorDefList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (KeyGeneratorDef) _keyGeneratorDefList.elementAt(index);
    } //-- KeyGeneratorDef getKeyGeneratorDef(int) 

    /**
    **/
    public KeyGeneratorDef[] getKeyGeneratorDef()
    {
        int size = _keyGeneratorDefList.size();
        KeyGeneratorDef[] mArray = new KeyGeneratorDef[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (KeyGeneratorDef) _keyGeneratorDefList.elementAt(index);
        }
        return mArray;
    } //-- KeyGeneratorDef[] getKeyGeneratorDef() 

    /**
    **/
    public int getKeyGeneratorDefCount()
    {
        return _keyGeneratorDefList.size();
    } //-- int getKeyGeneratorDefCount() 

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
    public void removeAllClassMapping()
    {
        _classMappingList.removeAllElements();
    } //-- void removeAllClassMapping() 

    /**
    **/
    public void removeAllInclude()
    {
        _includeList.removeAllElements();
    } //-- void removeAllInclude() 

    /**
    **/
    public void removeAllKeyGeneratorDef()
    {
        _keyGeneratorDefList.removeAllElements();
    } //-- void removeAllKeyGeneratorDef() 

    /**
     * 
     * @param index
    **/
    public ClassMapping removeClassMapping(int index)
    {
        Object obj = _classMappingList.elementAt(index);
        _classMappingList.removeElementAt(index);
        return (ClassMapping) obj;
    } //-- ClassMapping removeClassMapping(int) 

    /**
     * 
     * @param index
    **/
    public Include removeInclude(int index)
    {
        Object obj = _includeList.elementAt(index);
        _includeList.removeElementAt(index);
        return (Include) obj;
    } //-- Include removeInclude(int) 

    /**
     * 
     * @param index
    **/
    public KeyGeneratorDef removeKeyGeneratorDef(int index)
    {
        Object obj = _keyGeneratorDefList.elementAt(index);
        _keyGeneratorDefList.removeElementAt(index);
        return (KeyGeneratorDef) obj;
    } //-- KeyGeneratorDef removeKeyGeneratorDef(int) 

    /**
     * 
     * @param vClassMapping
     * @param index
    **/
    public void setClassMapping(ClassMapping vClassMapping, int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _classMappingList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _classMappingList.setElementAt(vClassMapping, index);
    } //-- void setClassMapping(ClassMapping, int) 

    /**
     * 
     * @param classMappingArray
    **/
    public void setClassMapping(ClassMapping[] classMappingArray)
    {
        //-- copy array
        _classMappingList.removeAllElements();
        for (int i = 0; i < classMappingArray.length; i++) {
            _classMappingList.addElement(classMappingArray[i]);
        }
    } //-- void setClassMapping(ClassMapping) 

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
     * @param vInclude
     * @param index
    **/
    public void setInclude(Include vInclude, int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _includeList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _includeList.setElementAt(vInclude, index);
    } //-- void setInclude(Include, int) 

    /**
     * 
     * @param includeArray
    **/
    public void setInclude(Include[] includeArray)
    {
        //-- copy array
        _includeList.removeAllElements();
        for (int i = 0; i < includeArray.length; i++) {
            _includeList.addElement(includeArray[i]);
        }
    } //-- void setInclude(Include) 

    /**
     * 
     * @param vKeyGeneratorDef
     * @param index
    **/
    public void setKeyGeneratorDef(KeyGeneratorDef vKeyGeneratorDef, int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _keyGeneratorDefList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _keyGeneratorDefList.setElementAt(vKeyGeneratorDef, index);
    } //-- void setKeyGeneratorDef(KeyGeneratorDef, int) 

    /**
     * 
     * @param keyGeneratorDefArray
    **/
    public void setKeyGeneratorDef(KeyGeneratorDef[] keyGeneratorDefArray)
    {
        //-- copy array
        _keyGeneratorDefList.removeAllElements();
        for (int i = 0; i < keyGeneratorDefArray.length; i++) {
            _keyGeneratorDefList.addElement(keyGeneratorDefArray[i]);
        }
    } //-- void setKeyGeneratorDef(KeyGeneratorDef) 

    /**
     * 
     * @param reader
    **/
    public static org.exolab.castor.mapping.xml.MappingRoot unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.exolab.castor.mapping.xml.MappingRoot) Unmarshaller.unmarshal(org.exolab.castor.mapping.xml.MappingRoot.class, reader);
    } //-- org.exolab.castor.mapping.xml.MappingRoot unmarshal(java.io.Reader) 

    /**
    **/
    public void validate()
        throws org.exolab.castor.xml.ValidationException
    {
        Validator validator = new Validator();
        validator.validate(this);
    } //-- void validate() 

}
