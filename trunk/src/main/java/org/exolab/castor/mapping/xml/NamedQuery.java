/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.1.2.1</a>, using an XML
 * Schema.
 * $Id$
 */

package org.exolab.castor.mapping.xml;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

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
     * Field _name.
     */
    private java.lang.String _name;

    /**
     * Field _query.
     */
    private java.lang.String _query;

    /**
     * Field _queryHintList.
     */
    private java.util.List _queryHintList;


      //----------------/
     //- Constructors -/
    //----------------/

    public NamedQuery() {
        super();
        this._queryHintList = new java.util.ArrayList();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * 
     * 
     * @param vQueryHint
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addQueryHint(
            final org.exolab.castor.mapping.xml.QueryHint vQueryHint)
    throws java.lang.IndexOutOfBoundsException {
        this._queryHintList.add(vQueryHint);
    }

    /**
     * 
     * 
     * @param index
     * @param vQueryHint
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addQueryHint(
            final int index,
            final org.exolab.castor.mapping.xml.QueryHint vQueryHint)
    throws java.lang.IndexOutOfBoundsException {
        this._queryHintList.add(index, vQueryHint);
    }

    /**
     * Method enumerateQueryHint.
     * 
     * @return an Enumeration over all possible elements of this
     * collection
     */
    public java.util.Enumeration enumerateQueryHint(
    ) {
        return java.util.Collections.enumeration(this._queryHintList);
    }

    /**
     * Returns the value of field 'name'.
     * 
     * @return the value of field 'Name'.
     */
    public java.lang.String getName(
    ) {
        return this._name;
    }

    /**
     * Returns the value of field 'query'.
     * 
     * @return the value of field 'Query'.
     */
    public java.lang.String getQuery(
    ) {
        return this._query;
    }

    /**
     * Method getQueryHint.
     * 
     * @param index
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     * @return the value of the
     * org.exolab.castor.mapping.xml.QueryHint at the given index
     */
    public org.exolab.castor.mapping.xml.QueryHint getQueryHint(
            final int index)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._queryHintList.size()) {
            throw new IndexOutOfBoundsException("getQueryHint: Index value '" + index + "' not in range [0.." + (this._queryHintList.size() - 1) + "]");
        }
        
        return (org.exolab.castor.mapping.xml.QueryHint) _queryHintList.get(index);
    }

    /**
     * Method getQueryHint.Returns the contents of the collection
     * in an Array.  <p>Note:  Just in case the collection contents
     * are changing in another thread, we pass a 0-length Array of
     * the correct type into the API call.  This way we <i>know</i>
     * that the Array returned is of exactly the correct length.
     * 
     * @return this collection as an Array
     */
    public org.exolab.castor.mapping.xml.QueryHint[] getQueryHint(
    ) {
        org.exolab.castor.mapping.xml.QueryHint[] array = new org.exolab.castor.mapping.xml.QueryHint[0];
        return (org.exolab.castor.mapping.xml.QueryHint[]) this._queryHintList.toArray(array);
    }

    /**
     * Method getQueryHintCount.
     * 
     * @return the size of this collection
     */
    public int getQueryHintCount(
    ) {
        return this._queryHintList.size();
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
     * Method iterateQueryHint.
     * 
     * @return an Iterator over all possible elements in this
     * collection
     */
    public java.util.Iterator iterateQueryHint(
    ) {
        return this._queryHintList.iterator();
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
    public void removeAllQueryHint(
    ) {
        this._queryHintList.clear();
    }

    /**
     * Method removeQueryHint.
     * 
     * @param vQueryHint
     * @return true if the object was removed from the collection.
     */
    public boolean removeQueryHint(
            final org.exolab.castor.mapping.xml.QueryHint vQueryHint) {
        boolean removed = _queryHintList.remove(vQueryHint);
        return removed;
    }

    /**
     * Method removeQueryHintAt.
     * 
     * @param index
     * @return the element removed from the collection
     */
    public org.exolab.castor.mapping.xml.QueryHint removeQueryHintAt(
            final int index) {
        java.lang.Object obj = this._queryHintList.remove(index);
        return (org.exolab.castor.mapping.xml.QueryHint) obj;
    }

    /**
     * Sets the value of field 'name'.
     * 
     * @param name the value of field 'name'.
     */
    public void setName(
            final java.lang.String name) {
        this._name = name;
    }

    /**
     * Sets the value of field 'query'.
     * 
     * @param query the value of field 'query'.
     */
    public void setQuery(
            final java.lang.String query) {
        this._query = query;
    }

    /**
     * 
     * 
     * @param index
     * @param vQueryHint
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void setQueryHint(
            final int index,
            final org.exolab.castor.mapping.xml.QueryHint vQueryHint)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._queryHintList.size()) {
            throw new IndexOutOfBoundsException("setQueryHint: Index value '" + index + "' not in range [0.." + (this._queryHintList.size() - 1) + "]");
        }
        
        this._queryHintList.set(index, vQueryHint);
    }

    /**
     * 
     * 
     * @param vQueryHintArray
     */
    public void setQueryHint(
            final org.exolab.castor.mapping.xml.QueryHint[] vQueryHintArray) {
        //-- copy array
        _queryHintList.clear();
        
        for (int i = 0; i < vQueryHintArray.length; i++) {
                this._queryHintList.add(vQueryHintArray[i]);
        }
    }

    /**
     * Method unmarshal.
     * 
     * @param reader
     * @throws org.exolab.castor.xml.MarshalException if object is
     * null or if any SAXException is thrown during marshaling
     * @throws org.exolab.castor.xml.ValidationException if this
     * object is an invalid instance according to the schema
     * @return the unmarshaled
     * org.exolab.castor.mapping.xml.NamedQuery
     */
    public static org.exolab.castor.mapping.xml.NamedQuery unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (org.exolab.castor.mapping.xml.NamedQuery) Unmarshaller.unmarshal(org.exolab.castor.mapping.xml.NamedQuery.class, reader);
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
