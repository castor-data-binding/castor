/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.2</a>, using an XML
 * Schema.
 * $Id$
 */

package org.castor.xmlctf.bestpractise.genpackage;

/**
 * Class PartialTermination.
 * 
 * @version $Revision$ $Date$
 */
public class PartialTermination implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _partyOne.
     */
    private org.castor.xmlctf.bestpractise.genpackage.PartyOne _partyOne;

    /**
     * Field _partyTwo.
     */
    private org.castor.xmlctf.bestpractise.genpackage.PartyTwo _partyTwo;

    /**
     * Field _partyThreeHref.
     */
    private org.castor.xmlctf.bestpractise.genpackage.PartyThreeHref _partyThreeHref;

    /**
     * Field _partialAmount.
     */
    private java.math.BigDecimal _partialAmount;

    /**
     * Field _assignmentNotificationList.
     */
    private java.util.Vector _assignmentNotificationList;

    /**
     * Field _partialTerminationChoice.
     */
    private org.castor.xmlctf.bestpractise.genpackage.PartialTerminationChoice _partialTerminationChoice;


      //----------------/
     //- Constructors -/
    //----------------/

    public PartialTermination() {
        super();
        this._assignmentNotificationList = new java.util.Vector();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * 
     * 
     * @param vAssignmentNotification
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addAssignmentNotification(
            final org.castor.xmlctf.bestpractise.genpackage.AssignmentNotification vAssignmentNotification)
    throws java.lang.IndexOutOfBoundsException {
        this._assignmentNotificationList.addElement(vAssignmentNotification);
    }

    /**
     * 
     * 
     * @param index
     * @param vAssignmentNotification
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addAssignmentNotification(
            final int index,
            final org.castor.xmlctf.bestpractise.genpackage.AssignmentNotification vAssignmentNotification)
    throws java.lang.IndexOutOfBoundsException {
        this._assignmentNotificationList.add(index, vAssignmentNotification);
    }

    /**
     * Method enumerateAssignmentNotification.
     * 
     * @return an Enumeration over all
     * xml.srcgen.template.generated.AssignmentNotification elements
     */
    public java.util.Enumeration enumerateAssignmentNotification(
    ) {
        return this._assignmentNotificationList.elements();
    }

    /**
     * Method getAssignmentNotification.
     * 
     * @param index
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     * @return the value of the
     * xml.srcgen.template.generated.AssignmentNotification at the
     * given index
     */
    public org.castor.xmlctf.bestpractise.genpackage.AssignmentNotification getAssignmentNotification(
            final int index)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._assignmentNotificationList.size()) {
            throw new IndexOutOfBoundsException("getAssignmentNotification: Index value '" + index + "' not in range [0.." + (this._assignmentNotificationList.size() - 1) + "]");
        }
        
        return (org.castor.xmlctf.bestpractise.genpackage.AssignmentNotification) _assignmentNotificationList.get(index);
    }

    /**
     * Method getAssignmentNotification.Returns the contents of the
     * collection in an Array.  <p>Note:  Just in case the
     * collection contents are changing in another thread, we pass
     * a 0-length Array of the correct type into the API call. 
     * This way we <i>know</i> that the Array returned is of
     * exactly the correct length.
     * 
     * @return this collection as an Array
     */
    public org.castor.xmlctf.bestpractise.genpackage.AssignmentNotification[] getAssignmentNotification(
    ) {
        org.castor.xmlctf.bestpractise.genpackage.AssignmentNotification[] array = new org.castor.xmlctf.bestpractise.genpackage.AssignmentNotification[0];
        return (org.castor.xmlctf.bestpractise.genpackage.AssignmentNotification[]) this._assignmentNotificationList.toArray(array);
    }

    /**
     * Method getAssignmentNotificationCount.
     * 
     * @return the size of this collection
     */
    public int getAssignmentNotificationCount(
    ) {
        return this._assignmentNotificationList.size();
    }

    /**
     * Returns the value of field 'partialAmount'.
     * 
     * @return the value of field 'PartialAmount'.
     */
    public java.math.BigDecimal getPartialAmount(
    ) {
        return this._partialAmount;
    }

    /**
     * Returns the value of field 'partialTerminationChoice'.
     * 
     * @return the value of field 'PartialTerminationChoice'.
     */
    public org.castor.xmlctf.bestpractise.genpackage.PartialTerminationChoice getPartialTerminationChoice(
    ) {
        return this._partialTerminationChoice;
    }

    /**
     * Returns the value of field 'partyOne'.
     * 
     * @return the value of field 'PartyOne'.
     */
    public org.castor.xmlctf.bestpractise.genpackage.PartyOne getPartyOne(
    ) {
        return this._partyOne;
    }

    /**
     * Returns the value of field 'partyThreeHref'.
     * 
     * @return the value of field 'PartyThreeHref'.
     */
    public org.castor.xmlctf.bestpractise.genpackage.PartyThreeHref getPartyThreeHref(
    ) {
        return this._partyThreeHref;
    }

    /**
     * Returns the value of field 'partyTwo'.
     * 
     * @return the value of field 'PartyTwo'.
     */
    public org.castor.xmlctf.bestpractise.genpackage.PartyTwo getPartyTwo(
    ) {
        return this._partyTwo;
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
    public void removeAllAssignmentNotification(
    ) {
        this._assignmentNotificationList.clear();
    }

    /**
     * Method removeAssignmentNotification.
     * 
     * @param vAssignmentNotification
     * @return true if the object was removed from the collection.
     */
    public boolean removeAssignmentNotification(
            final org.castor.xmlctf.bestpractise.genpackage.AssignmentNotification vAssignmentNotification) {
        boolean removed = _assignmentNotificationList.remove(vAssignmentNotification);
        return removed;
    }

    /**
     * Method removeAssignmentNotificationAt.
     * 
     * @param index
     * @return the element removed from the collection
     */
    public org.castor.xmlctf.bestpractise.genpackage.AssignmentNotification removeAssignmentNotificationAt(
            final int index) {
        java.lang.Object obj = this._assignmentNotificationList.remove(index);
        return (org.castor.xmlctf.bestpractise.genpackage.AssignmentNotification) obj;
    }

    /**
     * 
     * 
     * @param index
     * @param vAssignmentNotification
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void setAssignmentNotification(
            final int index,
            final org.castor.xmlctf.bestpractise.genpackage.AssignmentNotification vAssignmentNotification)
    throws java.lang.IndexOutOfBoundsException {
        // check bounds for index
        if (index < 0 || index >= this._assignmentNotificationList.size()) {
            throw new IndexOutOfBoundsException("setAssignmentNotification: Index value '" + index + "' not in range [0.." + (this._assignmentNotificationList.size() - 1) + "]");
        }
        
        this._assignmentNotificationList.set(index, vAssignmentNotification);
    }

    /**
     * 
     * 
     * @param vAssignmentNotificationArray
     */
    public void setAssignmentNotification(
            final org.castor.xmlctf.bestpractise.genpackage.AssignmentNotification[] vAssignmentNotificationArray) {
        //-- copy array
        _assignmentNotificationList.clear();
        
        for (int i = 0; i < vAssignmentNotificationArray.length; i++) {
                this._assignmentNotificationList.add(vAssignmentNotificationArray[i]);
        }
    }

    /**
     * Sets the value of field 'partialAmount'.
     * 
     * @param partialAmount the value of field 'partialAmount'.
     */
    public void setPartialAmount(
            final java.math.BigDecimal partialAmount) {
        this._partialAmount = partialAmount;
    }

    /**
     * Sets the value of field 'partialTerminationChoice'.
     * 
     * @param partialTerminationChoice the value of field
     * 'partialTerminationChoice'.
     */
    public void setPartialTerminationChoice(
            final org.castor.xmlctf.bestpractise.genpackage.PartialTerminationChoice partialTerminationChoice) {
        this._partialTerminationChoice = partialTerminationChoice;
    }

    /**
     * Sets the value of field 'partyOne'.
     * 
     * @param partyOne the value of field 'partyOne'.
     */
    public void setPartyOne(
            final org.castor.xmlctf.bestpractise.genpackage.PartyOne partyOne) {
        this._partyOne = partyOne;
    }

    /**
     * Sets the value of field 'partyThreeHref'.
     * 
     * @param partyThreeHref the value of field 'partyThreeHref'.
     */
    public void setPartyThreeHref(
            final org.castor.xmlctf.bestpractise.genpackage.PartyThreeHref partyThreeHref) {
        this._partyThreeHref = partyThreeHref;
    }

    /**
     * Sets the value of field 'partyTwo'.
     * 
     * @param partyTwo the value of field 'partyTwo'.
     */
    public void setPartyTwo(
            final org.castor.xmlctf.bestpractise.genpackage.PartyTwo partyTwo) {
        this._partyTwo = partyTwo;
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
     * xml.srcgen.template.generated.PartialTermination
     */
    public static org.castor.xmlctf.bestpractise.genpackage.PartialTermination unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (org.castor.xmlctf.bestpractise.genpackage.PartialTermination) org.exolab.castor.xml.Unmarshaller.unmarshal(org.castor.xmlctf.bestpractise.genpackage.PartialTermination.class, reader);
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
