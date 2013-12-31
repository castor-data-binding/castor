/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.2</a>, using an XML
 * Schema.
 * $Id$
 */

package org.castor.test.entity;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

/**
 * Class Authors.
 * 
 * @version $Revision$ $Date$
 */
public class Authors implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _authorList.
     */
    private java.util.List _authorList;


      //----------------/
     //- Constructors -/
    //----------------/

    public Authors() {
        super();
        this._authorList = new java.util.ArrayList();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * 
     * 
     * @param vAuthor
     * @throws IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addAuthor(
            final org.castor.test.entity.Author vAuthor)
    throws IndexOutOfBoundsException {
        this._authorList.add(vAuthor);
    }

    /**
     *
     *
     * @param index
     * @param vAuthor
     * @throws IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addAuthor(
            final int index,
            final org.castor.test.entity.Author vAuthor)
    throws IndexOutOfBoundsException {
        this._authorList.add(index, vAuthor);
    }

    /**
     * Method enumerateAuthor.
     *
     * @return an Enumeration over all possible elements of this
     * collection
     */
    public java.util.Enumeration enumerateAuthor(
    ) {
        return java.util.Collections.enumeration(this._authorList);
    }

    /**
     * Method getAuthor.
     *
     * @param index
     * @throws IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     * @return the value of the
     * org.castor.test.entity.Author at the given index
     */
    public org.castor.test.entity.Author getAuthor(
            final int index)
    throws IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._authorList.size()) {
            throw new IndexOutOfBoundsException("getAuthor: Index value '" + index + "' not in range [0.." + (this._authorList.size() - 1) + "]");
        }

        return (org.castor.test.entity.Author) _authorList.get(index);
    }

    /**
     * Method getAuthor.Returns the contents of the collection in
     * an Array.  <p>Note:  Just in case the collection contents
     * are changing in another thread, we pass a 0-length Array of
     * the correct type into the API call.  This way we <i>know</i>
     * that the Array returned is of exactly the correct length.
     *
     * @return this collection as an Array
     */
    public org.castor.test.entity.Author[] getAuthor(
    ) {
        org.castor.test.entity.Author[] array = new org.castor.test.entity.Author[0];
        return (org.castor.test.entity.Author[]) this._authorList.toArray(array);
    }

    /**
     * Method getAuthorCount.
     *
     * @return the size of this collection
     */
    public int getAuthorCount(
    ) {
        return this._authorList.size();
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
     * Method iterateAuthor.
     *
     * @return an Iterator over all possible elements in this
     * collection
     */
    public java.util.Iterator iterateAuthor(
    ) {
        return this._authorList.iterator();
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
    public void removeAllAuthor(
    ) {
        this._authorList.clear();
    }

    /**
     * Method removeAuthor.
     *
     * @param vAuthor
     * @return true if the object was removed from the collection.
     */
    public boolean removeAuthor(
            final org.castor.test.entity.Author vAuthor) {
        boolean removed = _authorList.remove(vAuthor);
        return removed;
    }

    /**
     * Method removeAuthorAt.
     *
     * @param index
     * @return the element removed from the collection
     */
    public org.castor.test.entity.Author removeAuthorAt(
            final int index) {
        Object obj = this._authorList.remove(index);
        return (org.castor.test.entity.Author) obj;
    }

    /**
     *
     *
     * @param index
     * @param vAuthor
     * @throws IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void setAuthor(
            final int index,
            final org.castor.test.entity.Author vAuthor)
    throws IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._authorList.size()) {
            throw new IndexOutOfBoundsException("setAuthor: Index value '" + index + "' not in range [0.." + (this._authorList.size() - 1) + "]");
        }
        
        this._authorList.set(index, vAuthor);
    }

    /**
     * 
     * 
     * @param vAuthorArray
     */
    public void setAuthor(
            final org.castor.test.entity.Author[] vAuthorArray) {
        //-- copy array
        _authorList.clear();
        
        for (int i = 0; i < vAuthorArray.length; i++) {
                this._authorList.add(vAuthorArray[i]);
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
     * @return the unmarshaled org.castor.test.entity.Author
     */
    public static org.castor.test.entity.Authors unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (org.castor.test.entity.Authors) Unmarshaller.unmarshal(org.castor.test.entity.Authors.class, reader);
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
