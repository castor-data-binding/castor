/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.1.1</a>, using an XML
 * Schema.
 * $Id$
 */

package org.exolab.castor.tests.framework.testDescriptor;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

/**
 * Class ExpectedSources.
 * 
 * @version $Revision$ $Date$
 */
public class ExpectedSources implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _expectedSourceList.
     */
    private java.util.Vector _expectedSourceList;


      //----------------/
     //- Constructors -/
    //----------------/

    public ExpectedSources() {
        super();
        this._expectedSourceList = new java.util.Vector();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * 
     * 
     * @param vExpectedSource
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addExpectedSource(
            final java.lang.String vExpectedSource)
    throws java.lang.IndexOutOfBoundsException {
        this._expectedSourceList.addElement(vExpectedSource);
    }

    /**
     * 
     * 
     * @param index
     * @param vExpectedSource
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addExpectedSource(
            final int index,
            final java.lang.String vExpectedSource)
    throws java.lang.IndexOutOfBoundsException {
        this._expectedSourceList.add(index, vExpectedSource);
    }

    /**
     * Method enumerateExpectedSource.
     * 
     * @return an Enumeration over all java.lang.String elements
     */
    public java.util.Enumeration enumerateExpectedSource(
    ) {
        return this._expectedSourceList.elements();
    }

    /**
     * Method getExpectedSource.
     * 
     * @param index
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     * @return the value of the java.lang.String at the given index
     */
    public java.lang.String getExpectedSource(
            final int index)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._expectedSourceList.size()) {
            throw new IndexOutOfBoundsException("getExpectedSource: Index value '" + index + "' not in range [0.." + (this._expectedSourceList.size() - 1) + "]");
        }
        
        return (java.lang.String) _expectedSourceList.get(index);
    }

    /**
     * Method getExpectedSource.Returns the contents of the
     * collection in an Array.  <p>Note:  Just in case the
     * collection contents are changing in another thread, we pass
     * a 0-length Array of the correct type into the API call. 
     * This way we <i>know</i> that the Array returned is of
     * exactly the correct length.
     * 
     * @return this collection as an Array
     */
    public java.lang.String[] getExpectedSource(
    ) {
        java.lang.String[] array = new java.lang.String[0];
        return (java.lang.String[]) this._expectedSourceList.toArray(array);
    }

    /**
     * Method getExpectedSourceCount.
     * 
     * @return the size of this collection
     */
    public int getExpectedSourceCount(
    ) {
        return this._expectedSourceList.size();
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
    public void removeAllExpectedSource(
    ) {
        this._expectedSourceList.clear();
    }

    /**
     * Method removeExpectedSource.
     * 
     * @param vExpectedSource
     * @return true if the object was removed from the collection.
     */
    public boolean removeExpectedSource(
            final java.lang.String vExpectedSource) {
        boolean removed = _expectedSourceList.remove(vExpectedSource);
        return removed;
    }

    /**
     * Method removeExpectedSourceAt.
     * 
     * @param index
     * @return the element removed from the collection
     */
    public java.lang.String removeExpectedSourceAt(
            final int index) {
        java.lang.Object obj = this._expectedSourceList.remove(index);
        return (java.lang.String) obj;
    }

    /**
     * 
     * 
     * @param index
     * @param vExpectedSource
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void setExpectedSource(
            final int index,
            final java.lang.String vExpectedSource)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._expectedSourceList.size()) {
            throw new IndexOutOfBoundsException("setExpectedSource: Index value '" + index + "' not in range [0.." + (this._expectedSourceList.size() - 1) + "]");
        }
        
        this._expectedSourceList.set(index, vExpectedSource);
    }

    /**
     * 
     * 
     * @param vExpectedSourceArray
     */
    public void setExpectedSource(
            final java.lang.String[] vExpectedSourceArray) {
        //-- copy array
        _expectedSourceList.clear();
        
        for (int i = 0; i < vExpectedSourceArray.length; i++) {
                this._expectedSourceList.add(vExpectedSourceArray[i]);
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
     * org.exolab.castor.tests.framework.testDescriptor.ExpectedSources
     */
    public static org.exolab.castor.tests.framework.testDescriptor.ExpectedSources unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (org.exolab.castor.tests.framework.testDescriptor.ExpectedSources) Unmarshaller.unmarshal(org.exolab.castor.tests.framework.testDescriptor.ExpectedSources.class, reader);
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
