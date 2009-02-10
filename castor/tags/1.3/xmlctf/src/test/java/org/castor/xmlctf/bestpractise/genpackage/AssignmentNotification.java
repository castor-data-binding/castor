/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.2</a>, using an XML
 * Schema.
 * $Id$
 */

package org.castor.xmlctf.bestpractise.genpackage;

/**
 * Class AssignmentNotification.
 * 
 * @version $Revision$ $Date$
 */
public class AssignmentNotification implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _unwindCashflow.
     */
    private org.castor.xmlctf.bestpractise.genpackage.UnwindCashflow _unwindCashflow;

    /**
     * Field _partyReference.
     */
    private org.castor.xmlctf.bestpractise.genpackage.PartyReference _partyReference;

    /**
     * Field _assignmentRole.
     */
    private org.castor.xmlctf.bestpractise.genpackage.types.AssignmentRole _assignmentRole;

    /**
     * Field _priorNotification.
     */
    private boolean _priorNotification;

    /**
     * keeps track of state for field: _priorNotification
     */
    private boolean _has_priorNotification;

    /**
     * Field _contactName.
     */
    private java.lang.String _contactName;


      //----------------/
     //- Constructors -/
    //----------------/

    public AssignmentNotification() {
        super();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     */
    public void deletePriorNotification(
    ) {
        this._has_priorNotification= false;
    }

    /**
     * Returns the value of field 'assignmentRole'.
     * 
     * @return the value of field 'AssignmentRole'.
     */
    public org.castor.xmlctf.bestpractise.genpackage.types.AssignmentRole getAssignmentRole(
    ) {
        return this._assignmentRole;
    }

    /**
     * Returns the value of field 'contactName'.
     * 
     * @return the value of field 'ContactName'.
     */
    public java.lang.String getContactName(
    ) {
        return this._contactName;
    }

    /**
     * Returns the value of field 'partyReference'.
     * 
     * @return the value of field 'PartyReference'.
     */
    public org.castor.xmlctf.bestpractise.genpackage.PartyReference getPartyReference(
    ) {
        return this._partyReference;
    }

    /**
     * Returns the value of field 'priorNotification'.
     * 
     * @return the value of field 'PriorNotification'.
     */
    public boolean getPriorNotification(
    ) {
        return this._priorNotification;
    }

    /**
     * Returns the value of field 'unwindCashflow'.
     * 
     * @return the value of field 'UnwindCashflow'.
     */
    public org.castor.xmlctf.bestpractise.genpackage.UnwindCashflow getUnwindCashflow(
    ) {
        return this._unwindCashflow;
    }

    /**
     * Method hasPriorNotification.
     * 
     * @return true if at least one PriorNotification has been added
     */
    public boolean hasPriorNotification(
    ) {
        return this._has_priorNotification;
    }

    /**
     * Returns the value of field 'priorNotification'.
     * 
     * @return the value of field 'PriorNotification'.
     */
    public boolean isPriorNotification(
    ) {
        return this._priorNotification;
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
     * Sets the value of field 'assignmentRole'.
     * 
     * @param assignmentRole the value of field 'assignmentRole'.
     */
    public void setAssignmentRole(
            final org.castor.xmlctf.bestpractise.genpackage.types.AssignmentRole assignmentRole) {
        this._assignmentRole = assignmentRole;
    }

    /**
     * Sets the value of field 'contactName'.
     * 
     * @param contactName the value of field 'contactName'.
     */
    public void setContactName(
            final java.lang.String contactName) {
        this._contactName = contactName;
    }

    /**
     * Sets the value of field 'partyReference'.
     * 
     * @param partyReference the value of field 'partyReference'.
     */
    public void setPartyReference(
            final org.castor.xmlctf.bestpractise.genpackage.PartyReference partyReference) {
        this._partyReference = partyReference;
    }

    /**
     * Sets the value of field 'priorNotification'.
     * 
     * @param priorNotification the value of field
     * 'priorNotification'.
     */
    public void setPriorNotification(
            final boolean priorNotification) {
        this._priorNotification = priorNotification;
        this._has_priorNotification = true;
    }

    /**
     * Sets the value of field 'unwindCashflow'.
     * 
     * @param unwindCashflow the value of field 'unwindCashflow'.
     */
    public void setUnwindCashflow(
            final org.castor.xmlctf.bestpractise.genpackage.UnwindCashflow unwindCashflow) {
        this._unwindCashflow = unwindCashflow;
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
     * xml.srcgen.template.generated.AssignmentNotification
     */
    public static org.castor.xmlctf.bestpractise.genpackage.AssignmentNotification unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (org.castor.xmlctf.bestpractise.genpackage.AssignmentNotification) org.exolab.castor.xml.Unmarshaller.unmarshal(org.castor.xmlctf.bestpractise.genpackage.AssignmentNotification.class, reader);
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
