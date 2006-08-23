/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.0.2</a>, using an XML
 * Schema.
 * $Id$
 */

package org.exolab.castor.mapping.xml;

import java.util.Collections;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

/**
 * Class NamedQuery.
 * 
 * @version $Revision$ $Date$
 */
public class NamedQuery implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _name
     */
    private java.lang.String _name;

    /**
     * Field _query
     */
    private java.lang.String _query;

    /**
     * Field _queryHintList
     */
    private java.util.ArrayList _queryHintList;


      //----------------/
     //- Constructors -/
    //----------------/

    public NamedQuery() 
     {
        super();
        _queryHintList = new java.util.ArrayList();
    } //-- org.exolab.castor.mapping.xml.NamedQuery()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addQueryHint
     * 
     * 
     * 
     * @param vQueryHint
     */
    public void addQueryHint(org.exolab.castor.mapping.xml.QueryHint vQueryHint)
        throws java.lang.IndexOutOfBoundsException
    {
        _queryHintList.add(vQueryHint);
    } //-- void addQueryHint(org.exolab.castor.mapping.xml.QueryHint) 

    /**
     * Method addQueryHint
     * 
     * 
     * 
     * @param index
     * @param vQueryHint
     */
    public void addQueryHint(int index, org.exolab.castor.mapping.xml.QueryHint vQueryHint)
        throws java.lang.IndexOutOfBoundsException
    {
        _queryHintList.add(index, vQueryHint);
    } //-- void addQueryHint(int, org.exolab.castor.mapping.xml.QueryHint) 

    /**
     * Method clearQueryHint
     * 
     */
    public void clearQueryHint()
    {
        _queryHintList.clear();
    } //-- void clearQueryHint() 

    /**
     * Method enumerateQueryHint
     * 
     * 
     * 
     * @return Enumeration
     */
    public java.util.Enumeration enumerateQueryHint()
    {
        return Collections.enumeration(_queryHintList);
    } //-- java.util.Enumeration enumerateQueryHint() 

    /**
     * Returns the value of field 'name'.
     * 
     * @return String
     * @return the value of field 'name'.
     */
    public java.lang.String getName()
    {
        return this._name;
    } //-- java.lang.String getName() 

    /**
     * Returns the value of field 'query'.
     * 
     * @return String
     * @return the value of field 'query'.
     */
    public java.lang.String getQuery()
    {
        return this._query;
    } //-- java.lang.String getQuery() 

    /**
     * Method getQueryHint
     * 
     * 
     * 
     * @param index
     * @return QueryHint
     */
    public org.exolab.castor.mapping.xml.QueryHint getQueryHint(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index >= _queryHintList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (org.exolab.castor.mapping.xml.QueryHint) _queryHintList.get(index);
    } //-- org.exolab.castor.mapping.xml.QueryHint getQueryHint(int) 

    /**
     * Method getQueryHint
     * 
     * 
     * 
     * @return QueryHint
     */
    public org.exolab.castor.mapping.xml.QueryHint[] getQueryHint()
    {
        int size = _queryHintList.size();
        org.exolab.castor.mapping.xml.QueryHint[] mArray = new org.exolab.castor.mapping.xml.QueryHint[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (org.exolab.castor.mapping.xml.QueryHint) _queryHintList.get(index);
        }
        return mArray;
    } //-- org.exolab.castor.mapping.xml.QueryHint[] getQueryHint() 

    /**
     * Method getQueryHintCount
     * 
     * 
     * 
     * @return int
     */
    public int getQueryHintCount()
    {
        return _queryHintList.size();
    } //-- int getQueryHintCount() 

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
     * Method removeQueryHint
     * 
     * 
     * 
     * @param vQueryHint
     * @return boolean
     */
    public boolean removeQueryHint(org.exolab.castor.mapping.xml.QueryHint vQueryHint)
    {
        boolean removed = _queryHintList.remove(vQueryHint);
        return removed;
    } //-- boolean removeQueryHint(org.exolab.castor.mapping.xml.QueryHint) 

    /**
     * Sets the value of field 'name'.
     * 
     * @param name the value of field 'name'.
     */
    public void setName(java.lang.String name)
    {
        this._name = name;
    } //-- void setName(java.lang.String) 

    /**
     * Sets the value of field 'query'.
     * 
     * @param query the value of field 'query'.
     */
    public void setQuery(java.lang.String query)
    {
        this._query = query;
    } //-- void setQuery(java.lang.String) 

    /**
     * Method setQueryHint
     * 
     * 
     * 
     * @param index
     * @param vQueryHint
     */
    public void setQueryHint(int index, org.exolab.castor.mapping.xml.QueryHint vQueryHint)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index >= _queryHintList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _queryHintList.set(index, vQueryHint);
    } //-- void setQueryHint(int, org.exolab.castor.mapping.xml.QueryHint) 

    /**
     * Method setQueryHint
     * 
     * 
     * 
     * @param queryHintArray
     */
    public void setQueryHint(org.exolab.castor.mapping.xml.QueryHint[] queryHintArray)
    {
        //-- copy array
        _queryHintList.clear();
        for (int i = 0; i < queryHintArray.length; i++) {
            _queryHintList.add(queryHintArray[i]);
        }
    } //-- void setQueryHint(org.exolab.castor.mapping.xml.QueryHint) 

    /**
     * Method unmarshal
     * 
     * 
     * 
     * @param reader
     * @return NamedQuery
     */
    public static org.exolab.castor.mapping.xml.NamedQuery unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.exolab.castor.mapping.xml.NamedQuery) Unmarshaller.unmarshal(org.exolab.castor.mapping.xml.NamedQuery.class, reader);
    } //-- org.exolab.castor.mapping.xml.NamedQuery unmarshal(java.io.Reader) 

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
