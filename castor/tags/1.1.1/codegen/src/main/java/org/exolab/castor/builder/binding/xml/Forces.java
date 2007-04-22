/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.1</a>, using an XML
 * Schema.
 * $Id$
 */

package org.exolab.castor.builder.binding.xml;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

/**
 * Class Forces.
 * 
 * @version $Revision$ $Date$
 */
public class Forces implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _forceList.
     */
    private java.util.List _forceList;


      //----------------/
     //- Constructors -/
    //----------------/

    public Forces() {
        super();
        this._forceList = new java.util.ArrayList();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * 
     * 
     * @param vForce
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addForce(
            final java.lang.String vForce)
    throws java.lang.IndexOutOfBoundsException {
        this._forceList.add(vForce);
    }

    /**
     * 
     * 
     * @param index
     * @param vForce
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addForce(
            final int index,
            final java.lang.String vForce)
    throws java.lang.IndexOutOfBoundsException {
        this._forceList.add(index, vForce);
    }

    /**
     * Method enumerateForce.
     * 
     * @return an Enumeration over all possible elements of this
     * collection
     */
    public java.util.Enumeration enumerateForce(
    ) {
        return java.util.Collections.enumeration(this._forceList);
    }

    /**
     * Method getForce.
     * 
     * @param index
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     * @return the value of the java.lang.String at the given index
     */
    public java.lang.String getForce(
            final int index)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._forceList.size()) {
            throw new IndexOutOfBoundsException("getForce: Index value '" + index + "' not in range [0.." + (this._forceList.size() - 1) + "]");
        }
        
        return (java.lang.String) _forceList.get(index);
    }

    /**
     * Method getForce.Returns the contents of the collection in an
     * Array.  <p>Note:  Just in case the collection contents are
     * changing in another thread, we pass a 0-length Array of the
     * correct type into the API call.  This way we <i>know</i>
     * that the Array returned is of exactly the correct length.
     * 
     * @return this collection as an Array
     */
    public java.lang.String[] getForce(
    ) {
        java.lang.String[] array = new java.lang.String[0];
        return (java.lang.String[]) this._forceList.toArray(array);
    }

    /**
     * Method getForceCount.
     * 
     * @return the size of this collection
     */
    public int getForceCount(
    ) {
        return this._forceList.size();
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
     * Method iterateForce.
     * 
     * @return an Iterator over all possible elements in this
     * collection
     */
    public java.util.Iterator iterateForce(
    ) {
        return this._forceList.iterator();
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
    public void removeAllForce(
    ) {
        this._forceList.clear();
    }

    /**
     * Method removeForce.
     * 
     * @param vForce
     * @return true if the object was removed from the collection.
     */
    public boolean removeForce(
            final java.lang.String vForce) {
        boolean removed = _forceList.remove(vForce);
        return removed;
    }

    /**
     * Method removeForceAt.
     * 
     * @param index
     * @return the element removed from the collection
     */
    public java.lang.String removeForceAt(
            final int index) {
        java.lang.Object obj = this._forceList.remove(index);
        return (java.lang.String) obj;
    }

    /**
     * 
     * 
     * @param index
     * @param vForce
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void setForce(
            final int index,
            final java.lang.String vForce)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._forceList.size()) {
            throw new IndexOutOfBoundsException("setForce: Index value '" + index + "' not in range [0.." + (this._forceList.size() - 1) + "]");
        }
        
        this._forceList.set(index, vForce);
    }

    /**
     * 
     * 
     * @param vForceArray
     */
    public void setForce(
            final java.lang.String[] vForceArray) {
        //-- copy array
        _forceList.clear();
        
        for (int i = 0; i < vForceArray.length; i++) {
                this._forceList.add(vForceArray[i]);
        }
    }

    /**
     * Method unmarshalForces.
     * 
     * @param reader
     * @throws org.exolab.castor.xml.MarshalException if object is
     * null or if any SAXException is thrown during marshaling
     * @throws org.exolab.castor.xml.ValidationException if this
     * object is an invalid instance according to the schema
     * @return the unmarshaled
     * org.exolab.castor.builder.binding.xml.Forces
     */
    public static org.exolab.castor.builder.binding.xml.Forces unmarshalForces(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (org.exolab.castor.builder.binding.xml.Forces) Unmarshaller.unmarshal(org.exolab.castor.builder.binding.xml.Forces.class, reader);
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
