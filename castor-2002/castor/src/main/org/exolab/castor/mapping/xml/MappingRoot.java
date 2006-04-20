/*
 * This class was automatically generated with 
 * <a href="http://castor.exolab.org">Castor 0.8 (20000324)</a>,
 * using an XML Schema.
 * $Id
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


      //--------------------/
     //- Member Variables -/
    //--------------------/

    private java.lang.String _description;

    private java.util.Vector _includeList;

    private java.util.Vector _classMappingList;


      //----------------/
     //- Constructors -/
    //----------------/

    public MappingRoot() {
        super();
        _includeList = new Vector();
        _classMappingList = new Vector();
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
    **/
    public java.util.Enumeration enumerateClassMapping() {
        return _classMappingList.elements();
    } //-- java.util.Enumeration enumerateClassMapping() 

    /**
    **/
    public java.util.Enumeration enumerateInclude() {
        return _includeList.elements();
    } //-- java.util.Enumeration enumerateInclude() 

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
    public ClassMapping[] getClassMapping() {
        int size = _classMappingList.size();
        ClassMapping[] mArray = new ClassMapping[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (ClassMapping) _classMappingList.elementAt(index);
        }
        return mArray;
    } //-- ClassMapping[] getClassMapping() 

    /**
    **/
    public int getClassMappingCount() {
        return _classMappingList.size();
    } //-- int getClassMappingCount() 

    /**
    **/
    public java.lang.String getDescription() {
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
    public Include[] getInclude() {
        int size = _includeList.size();
        Include[] mArray = new Include[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (Include) _includeList.elementAt(index);
        }
        return mArray;
    } //-- Include[] getInclude() 

    /**
    **/
    public int getIncludeCount() {
        return _includeList.size();
    } //-- int getIncludeCount() 

    /**
    **/
    public void removeAllClassMapping() {
        _classMappingList.removeAllElements();
    } //-- void removeAllClassMapping() 

    /**
    **/
    public void removeAllInclude() {
        _includeList.removeAllElements();
    } //-- void removeAllInclude() 

    /**
     * 
     * @param index
    **/
    public ClassMapping removeClassMapping(int index) {
        Object obj = _classMappingList.elementAt(index);
        _classMappingList.removeElementAt(index);
        return (ClassMapping) obj;
    } //-- ClassMapping removeClassMapping(int) 

    /**
     * 
     * @param index
    **/
    public Include removeInclude(int index) {
        Object obj = _includeList.elementAt(index);
        _includeList.removeElementAt(index);
        return (Include) obj;
    } //-- Include removeInclude(int) 

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
     * @param _description
    **/
    public void setDescription(java.lang.String _description) {
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

}
