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
 * Class Books.
 * 
 * @version $Revision$ $Date$
 */
public class Books implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _bookList.
     */
    private java.util.List _bookList;


      //----------------/
     //- Constructors -/
    //----------------/

    public Books() {
        super();
        this._bookList = new java.util.ArrayList();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * 
     * 
     * @param vBook
     * @throws IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addBook(
            final org.castor.test.entity.Book vBook)
    throws IndexOutOfBoundsException {
        this._bookList.add(vBook);
    }

    /**
     *
     *
     * @param index
     * @param vBook
     * @throws IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addBook(
            final int index,
            final org.castor.test.entity.Book vBook)
    throws IndexOutOfBoundsException {
        this._bookList.add(index, vBook);
    }

    /**
     * Method enumerateBook.
     *
     * @return an Enumeration over all possible elements of this
     * collection
     */
    public java.util.Enumeration enumerateBook(
    ) {
        return java.util.Collections.enumeration(this._bookList);
    }

    /**
     * Method getBook.
     *
     * @param index
     * @throws IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     * @return the value of the org.castor.test.entity.Book
     * at the given index
     */
    public org.castor.test.entity.Book getBook(
            final int index)
    throws IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._bookList.size()) {
            throw new IndexOutOfBoundsException("getBook: Index value '" + index + "' not in range [0.." + (this._bookList.size() - 1) + "]");
        }

        return (org.castor.test.entity.Book) _bookList.get(index);
    }

    /**
     * Method getBook.Returns the contents of the collection in an
     * Array.  <p>Note:  Just in case the collection contents are
     * changing in another thread, we pass a 0-length Array of the
     * correct type into the API call.  This way we <i>know</i>
     * that the Array returned is of exactly the correct length.
     *
     * @return this collection as an Array
     */
    public org.castor.test.entity.Book[] getBook(
    ) {
        org.castor.test.entity.Book[] array = new org.castor.test.entity.Book[0];
        return (org.castor.test.entity.Book[]) this._bookList.toArray(array);
    }

    /**
     * Method getBookCount.
     *
     * @return the size of this collection
     */
    public int getBookCount(
    ) {
        return this._bookList.size();
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
     * Method iterateBook.
     *
     * @return an Iterator over all possible elements in this
     * collection
     */
    public java.util.Iterator iterateBook(
    ) {
        return this._bookList.iterator();
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
    public void removeAllBook(
    ) {
        this._bookList.clear();
    }

    /**
     * Method removeBook.
     *
     * @param vBook
     * @return true if the object was removed from the collection.
     */
    public boolean removeBook(
            final org.castor.test.entity.Book vBook) {
        boolean removed = _bookList.remove(vBook);
        return removed;
    }

    /**
     * Method removeBookAt.
     *
     * @param index
     * @return the element removed from the collection
     */
    public org.castor.test.entity.Book removeBookAt(
            final int index) {
        Object obj = this._bookList.remove(index);
        return (org.castor.test.entity.Book) obj;
    }

    /**
     *
     *
     * @param index
     * @param vBook
     * @throws IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void setBook(
            final int index,
            final org.castor.test.entity.Book vBook)
    throws IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._bookList.size()) {
            throw new IndexOutOfBoundsException("setBook: Index value '" + index + "' not in range [0.." + (this._bookList.size() - 1) + "]");
        }
        
        this._bookList.set(index, vBook);
    }

    /**
     * 
     * 
     * @param vBookArray
     */
    public void setBook(
            final org.castor.test.entity.Book[] vBookArray) {
        //-- copy array
        _bookList.clear();
        
        for (int i = 0; i < vBookArray.length; i++) {
                this._bookList.add(vBookArray[i]);
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
     * @return the unmarshaled org.castor.test.entity.Books
     */
    public static org.castor.test.entity.Books unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (org.castor.test.entity.Books) Unmarshaller.unmarshal(org.castor.test.entity.Books.class, reader);
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
