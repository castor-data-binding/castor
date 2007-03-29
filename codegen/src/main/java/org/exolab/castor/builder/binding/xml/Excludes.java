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
 * Class Excludes.
 * 
 * @version $Revision$ $Date$
 */
public class Excludes implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _excludeList.
     */
    private java.util.List _excludeList;


      //----------------/
     //- Constructors -/
    //----------------/

    public Excludes() {
        super();
        this._excludeList = new java.util.ArrayList();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * 
     * 
     * @param vExclude
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addExclude(
            final org.exolab.castor.builder.binding.xml.Exclude vExclude)
    throws java.lang.IndexOutOfBoundsException {
        this._excludeList.add(vExclude);
    }

    /**
     * 
     * 
     * @param index
     * @param vExclude
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addExclude(
            final int index,
            final org.exolab.castor.builder.binding.xml.Exclude vExclude)
    throws java.lang.IndexOutOfBoundsException {
        this._excludeList.add(index, vExclude);
    }

    /**
     * Method enumerateExclude.
     * 
     * @return an Enumeration over all possible elements of this
     * collection
     */
    public java.util.Enumeration enumerateExclude(
    ) {
        return java.util.Collections.enumeration(this._excludeList);
    }

    /**
     * Method getExclude.
     * 
     * @param index
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     * @return the value of the
     * org.exolab.castor.builder.binding.xml.Exclude at the given
     * index
     */
    public org.exolab.castor.builder.binding.xml.Exclude getExclude(
            final int index)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._excludeList.size()) {
            throw new IndexOutOfBoundsException("getExclude: Index value '" + index + "' not in range [0.." + (this._excludeList.size() - 1) + "]");
        }
        
        return (org.exolab.castor.builder.binding.xml.Exclude) _excludeList.get(index);
    }

    /**
     * Method getExclude.Returns the contents of the collection in
     * an Array.  <p>Note:  Just in case the collection contents
     * are changing in another thread, we pass a 0-length Array of
     * the correct type into the API call.  This way we <i>know</i>
     * that the Array returned is of exactly the correct length.
     * 
     * @return this collection as an Array
     */
    public org.exolab.castor.builder.binding.xml.Exclude[] getExclude(
    ) {
        org.exolab.castor.builder.binding.xml.Exclude[] array = new org.exolab.castor.builder.binding.xml.Exclude[0];
        return (org.exolab.castor.builder.binding.xml.Exclude[]) this._excludeList.toArray(array);
    }

    /**
     * Method getExcludeCount.
     * 
     * @return the size of this collection
     */
    public int getExcludeCount(
    ) {
        return this._excludeList.size();
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
     * Method iterateExclude.
     * 
     * @return an Iterator over all possible elements in this
     * collection
     */
    public java.util.Iterator iterateExclude(
    ) {
        return this._excludeList.iterator();
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
    public void removeAllExclude(
    ) {
        this._excludeList.clear();
    }

    /**
     * Method removeExclude.
     * 
     * @param vExclude
     * @return true if the object was removed from the collection.
     */
    public boolean removeExclude(
            final org.exolab.castor.builder.binding.xml.Exclude vExclude) {
        boolean removed = _excludeList.remove(vExclude);
        return removed;
    }

    /**
     * Method removeExcludeAt.
     * 
     * @param index
     * @return the element removed from the collection
     */
    public org.exolab.castor.builder.binding.xml.Exclude removeExcludeAt(
            final int index) {
        java.lang.Object obj = this._excludeList.remove(index);
        return (org.exolab.castor.builder.binding.xml.Exclude) obj;
    }

    /**
     * 
     * 
     * @param index
     * @param vExclude
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void setExclude(
            final int index,
            final org.exolab.castor.builder.binding.xml.Exclude vExclude)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._excludeList.size()) {
            throw new IndexOutOfBoundsException("setExclude: Index value '" + index + "' not in range [0.." + (this._excludeList.size() - 1) + "]");
        }
        
        this._excludeList.set(index, vExclude);
    }

    /**
     * 
     * 
     * @param vExcludeArray
     */
    public void setExclude(
            final org.exolab.castor.builder.binding.xml.Exclude[] vExcludeArray) {
        //-- copy array
        _excludeList.clear();
        
        for (int i = 0; i < vExcludeArray.length; i++) {
                this._excludeList.add(vExcludeArray[i]);
        }
    }

    /**
     * Method unmarshalExcludes.
     * 
     * @param reader
     * @throws org.exolab.castor.xml.MarshalException if object is
     * null or if any SAXException is thrown during marshaling
     * @throws org.exolab.castor.xml.ValidationException if this
     * object is an invalid instance according to the schema
     * @return the unmarshaled
     * org.exolab.castor.builder.binding.xml.Excludes
     */
    public static org.exolab.castor.builder.binding.xml.Excludes unmarshalExcludes(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (org.exolab.castor.builder.binding.xml.Excludes) Unmarshaller.unmarshal(org.exolab.castor.builder.binding.xml.Excludes.class, reader);
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
