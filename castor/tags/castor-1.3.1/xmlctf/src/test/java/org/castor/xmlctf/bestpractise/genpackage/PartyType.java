/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.2</a>, using an XML
 * Schema.
 * $Id$
 */

package org.castor.xmlctf.bestpractise.genpackage;

/**
 * Class PartyType.
 * 
 * @version $Revision$ $Date$
 */
public class PartyType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _id.
     */
    private java.lang.String _id;

    /**
     * Field _partyId.
     */
    private org.castor.xmlctf.bestpractise.genpackage.PartyId _partyId;

    /**
     * Field _partyName.
     */
    private java.lang.String _partyName;


      //----------------/
     //- Constructors -/
    //----------------/

    public PartyType() {
        super();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'id'.
     * 
     * @return the value of field 'Id'.
     */
    public java.lang.String getId(
    ) {
        return this._id;
    }

    /**
     * Returns the value of field 'partyId'.
     * 
     * @return the value of field 'PartyId'.
     */
    public org.castor.xmlctf.bestpractise.genpackage.PartyId getPartyId(
    ) {
        return this._partyId;
    }

    /**
     * Returns the value of field 'partyName'.
     * 
     * @return the value of field 'PartyName'.
     */
    public java.lang.String getPartyName(
    ) {
        return this._partyName;
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
     * Sets the value of field 'id'.
     * 
     * @param id the value of field 'id'.
     */
    public void setId(
            final java.lang.String id) {
        this._id = id;
    }

    /**
     * Sets the value of field 'partyId'.
     * 
     * @param partyId the value of field 'partyId'.
     */
    public void setPartyId(
            final org.castor.xmlctf.bestpractise.genpackage.PartyId partyId) {
        this._partyId = partyId;
    }

    /**
     * Sets the value of field 'partyName'.
     * 
     * @param partyName the value of field 'partyName'.
     */
    public void setPartyName(
            final java.lang.String partyName) {
        this._partyName = partyName;
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
     * xml.srcgen.template.generated.PartyType
     */
    public static org.castor.xmlctf.bestpractise.genpackage.PartyType unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (org.castor.xmlctf.bestpractise.genpackage.PartyType) org.exolab.castor.xml.Unmarshaller.unmarshal(org.castor.xmlctf.bestpractise.genpackage.PartyType.class, reader);
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
