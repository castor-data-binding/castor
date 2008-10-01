/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.2</a>, using an XML
 * Schema.
 * $Id$
 */

package org.castor.xmlctf.bestpractise.genpackage;

/**
 * Class UnderlyerAdjustment.
 * 
 * @version $Revision$ $Date$
 */
public class UnderlyerAdjustment extends org.castor.xmlctf.bestpractise.genpackage.CBMTransactionEvent 
implements java.io.Serializable
{


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _partyThree.
     */
    private org.castor.xmlctf.bestpractise.genpackage.PartyThree _partyThree;

    /**
     * Field _unwindCashflowList.
     */
    private java.util.Vector _unwindCashflowList;


      //----------------/
     //- Constructors -/
    //----------------/

    public UnderlyerAdjustment() {
        super();
        this._unwindCashflowList = new java.util.Vector();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * 
     * 
     * @param vUnwindCashflow
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addUnwindCashflow(
            final org.castor.xmlctf.bestpractise.genpackage.UnwindCashflow vUnwindCashflow)
    throws java.lang.IndexOutOfBoundsException {
        this._unwindCashflowList.addElement(vUnwindCashflow);
    }

    /**
     * 
     * 
     * @param index
     * @param vUnwindCashflow
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addUnwindCashflow(
            final int index,
            final org.castor.xmlctf.bestpractise.genpackage.UnwindCashflow vUnwindCashflow)
    throws java.lang.IndexOutOfBoundsException {
        this._unwindCashflowList.add(index, vUnwindCashflow);
    }

    /**
     * Method enumerateUnwindCashflow.
     * 
     * @return an Enumeration over all
     * xml.srcgen.template.generated.UnwindCashflow elements
     */
    public java.util.Enumeration enumerateUnwindCashflow(
    ) {
        return this._unwindCashflowList.elements();
    }

    /**
     * Returns the value of field 'partyThree'.
     * 
     * @return the value of field 'PartyThree'.
     */
    public org.castor.xmlctf.bestpractise.genpackage.PartyThree getPartyThree(
    ) {
        return this._partyThree;
    }

    /**
     * Method getUnwindCashflow.
     * 
     * @param index
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     * @return the value of the
     * xml.srcgen.template.generated.UnwindCashflow at the given
     * index
     */
    public org.castor.xmlctf.bestpractise.genpackage.UnwindCashflow getUnwindCashflow(
            final int index)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._unwindCashflowList.size()) {
            throw new IndexOutOfBoundsException("getUnwindCashflow: Index value '" + index + "' not in range [0.." + (this._unwindCashflowList.size() - 1) + "]");
        }
        
        return (org.castor.xmlctf.bestpractise.genpackage.UnwindCashflow) _unwindCashflowList.get(index);
    }

    /**
     * Method getUnwindCashflow.Returns the contents of the
     * collection in an Array.  <p>Note:  Just in case the
     * collection contents are changing in another thread, we pass
     * a 0-length Array of the correct type into the API call. 
     * This way we <i>know</i> that the Array returned is of
     * exactly the correct length.
     * 
     * @return this collection as an Array
     */
    public org.castor.xmlctf.bestpractise.genpackage.UnwindCashflow[] getUnwindCashflow(
    ) {
        org.castor.xmlctf.bestpractise.genpackage.UnwindCashflow[] array = new org.castor.xmlctf.bestpractise.genpackage.UnwindCashflow[0];
        return (org.castor.xmlctf.bestpractise.genpackage.UnwindCashflow[]) this._unwindCashflowList.toArray(array);
    }

    /**
     * Method getUnwindCashflowCount.
     * 
     * @return the size of this collection
     */
    public int getUnwindCashflowCount(
    ) {
        return this._unwindCashflowList.size();
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
        org.exolab.castor.xml.Marshaller.marshal(this, out);
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
        org.exolab.castor.xml.Marshaller.marshal(this, handler);
    }

    /**
     */
    public void removeAllUnwindCashflow(
    ) {
        this._unwindCashflowList.clear();
    }

    /**
     * Method removeUnwindCashflow.
     * 
     * @param vUnwindCashflow
     * @return true if the object was removed from the collection.
     */
    public boolean removeUnwindCashflow(
            final org.castor.xmlctf.bestpractise.genpackage.UnwindCashflow vUnwindCashflow) {
        boolean removed = _unwindCashflowList.remove(vUnwindCashflow);
        return removed;
    }

    /**
     * Method removeUnwindCashflowAt.
     * 
     * @param index
     * @return the element removed from the collection
     */
    public org.castor.xmlctf.bestpractise.genpackage.UnwindCashflow removeUnwindCashflowAt(
            final int index) {
        java.lang.Object obj = this._unwindCashflowList.remove(index);
        return (org.castor.xmlctf.bestpractise.genpackage.UnwindCashflow) obj;
    }

    /**
     * Sets the value of field 'partyThree'.
     * 
     * @param partyThree the value of field 'partyThree'.
     */
    public void setPartyThree(
            final org.castor.xmlctf.bestpractise.genpackage.PartyThree partyThree) {
        this._partyThree = partyThree;
    }

    /**
     * 
     * 
     * @param index
     * @param vUnwindCashflow
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void setUnwindCashflow(
            final int index,
            final org.castor.xmlctf.bestpractise.genpackage.UnwindCashflow vUnwindCashflow)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._unwindCashflowList.size()) {
            throw new IndexOutOfBoundsException("setUnwindCashflow: Index value '" + index + "' not in range [0.." + (this._unwindCashflowList.size() - 1) + "]");
        }
        
        this._unwindCashflowList.set(index, vUnwindCashflow);
    }

    /**
     * 
     * 
     * @param vUnwindCashflowArray
     */
    public void setUnwindCashflow(
            final org.castor.xmlctf.bestpractise.genpackage.UnwindCashflow[] vUnwindCashflowArray) {
        //-- copy array
        _unwindCashflowList.clear();
        
        for (int i = 0; i < vUnwindCashflowArray.length; i++) {
                this._unwindCashflowList.add(vUnwindCashflowArray[i]);
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
     * xml.srcgen.template.generated.CBMTransactionEvent
     */
    public static org.castor.xmlctf.bestpractise.genpackage.CBMTransactionEvent unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (org.castor.xmlctf.bestpractise.genpackage.CBMTransactionEvent) org.exolab.castor.xml.Unmarshaller.unmarshal(org.castor.xmlctf.bestpractise.genpackage.UnderlyerAdjustment.class, reader);
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
